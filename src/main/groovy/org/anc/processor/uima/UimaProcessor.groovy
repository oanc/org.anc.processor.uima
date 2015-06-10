package org.anc.processor.uima

import org.anc.processor.Abstract.AbstractProcessor
import javax.ws.rs.Path

@Path("/uima")
class UimaProcessor extends AbstractProcessor{

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
}
