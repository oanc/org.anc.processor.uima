package org.anc.processor.uima

import org.anc.conf.AnnotationConfig
import org.anc.processor.Abstract.AbstractProcessor
import org.anc.processor.uima.i18n.Messages

//import org.anc.processor.Abstract.Messages
import org.anc.tool.api.ProcessorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response

@Path("/uima")
class UimaProcessor extends AbstractProcessor{

    protected Logger logger = LoggerFactory.getLogger(UimaProcessor)
    private static final Messages MESSAGES = new Messages()

//<<<<<<< Updated upstream
    public UimaProcessor() {
        super(["f.penn", "f.sentences", "f.biber", "f.c5", "f.c7", "f.cb",
               "f.content", "f.event", "f.hepple", "f.logical",
               "f.mpqa", "f.nc", "f.ne", "f.none","f.slate_coref",
               "f.vc", "f.ptb", "f.ptbtok", "f.fn", "f.fntok", "f.s"])
//=======
//    public static final Set<String> ACCEPTABLE =
//            ["f.penn", "f.s", "f.cb", "f.event", "f.fn", "f.fntok",
//             "f.logical", "f.mpqa", "f.ns", "f.ne", "f.ptb", "f.ptbtok",
//             "f.vc"] as HashSet<String>
//
//    public UimaProcessor() {
//>>>>>>> Stashed changes
        processor = new org.anc.tool.uima.UimaProcessor()
    }

    /**
     * Function called to process a document
     * @param annotations The list of annotations to process the doc with
     * @param docID The document ID
     * @return An error indicating an invalid parameter or the document processed with
     *         the annotations
     */
    @GET
    Response process(@QueryParam('annotations') String annotations,
                     @QueryParam('id') String docID) {
        logger.info("Request received.")
        if (annotations == null) {
            logger.warn(MESSAGES.NO_ANNOTATIONS)
            return Response.status(400).entity(MESSAGES.NO_ANNOTATIONS).build()
        }
        if (docID == null) {
            logger.warn("No document ID")
            return Response.status(400).entity("No document ID").build()
        }

        logger.debug("Attempting to process {}", docID)



        def selectedAnnotations = parseAnnotations(annotations)
        System.out.println(selectedAnnotations)
        File inputFile = index.get(docID)
        if (inputFile == null) {
            logger.debug("No document with id {}", docID)
            return Response.status(400).entity(MESSAGES.INVALID_ID).build();
        }

        if (!validAnnotations(selectedAnnotations)) {
            logger.debug("Invalid annotations selected.")
            return Response.status(400).entity(MESSAGES.INVALID_TYPE).build()
        }

        // TODO The output file should be placed in a known location (e.g. /tmp/conll-processor) that
        // can be cleaned/deleted on startup to prevent filling the disk if the service crashes.

        File outputFile = File.createTempFile("processor-rs", "txt");

        try {
            processor.reset()
            processor.resourceHeader = header
            processor.annotationTypes = new AnnotationConfig(selectedAnnotations)
            // TODO This should be removed as it is only used to make the XMLProcessor
            // stop complaining about a setting it doesn't actually use.
            processor.setOptions("Discard")
            processor.initialize()
            processor.process(inputFile, outputFile);
            return Response.ok(outputFile.text).build();
        }
        catch (ProcessorException e) {
            logger.error("Unable to process document.", e)
            // NOTE: This really is a server error and should return 500 Server Error
            // and not 400 Bad Request. The request was fine, we just choked.
            return Response.serverError().entity(e.getMessage()).build()
        }
        finally {
            // Delete the temporary output file. This is best done in a finally block
            // so it gets deleted regardless of how execution exits the try block.
            if (!outputFile.delete()) {
                logger.error("Unable to delete temporary file {}", outputFile.path)
                // Hopefully we can delete the file when the service exits.
                outputFile.deleteOnExit();
            }
        }
    }

}
