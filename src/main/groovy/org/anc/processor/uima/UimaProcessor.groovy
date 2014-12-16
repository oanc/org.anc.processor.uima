package org.anc.processor.uima

import org.anc.index.core.IndexImpl
import org.xces.graf.io.dom.ResourceHeader

import javax.ws.rs.Path

@Path("/uima")
class UimaProcessor extends AbstractProcessor{

    public static final Set<String> ACCEPTABLE =
            ["f.penn", "f.s", "f.cb", "f.event", "f.fn", "f.fntok",
             "f.logical", "f.mpqa", "f.ns", "f.ne", "f.ptb", "f.ptbtok",
             "f.vc"] as HashSet<String>

    public ConllProcessor() {
        processor = new org.anc.tool.uima.UimaProcessor();
        //TODO This path should not be hard coded.
        header = new ResourceHeader(new File("/var/corpora/MASC-3.0.0/resource-header.xml"))
        index = new IndexImpl().loadMasc3Index()
        setAcceptable(ACCEPTABLE)
    }
}
