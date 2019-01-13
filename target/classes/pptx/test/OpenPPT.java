package test;

import pptx.apiTest.element.FileProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.contstant.PPTxContstant;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

public class OpenPPT {
    public static void main(String[] args) {
        try {
            XMLSlideShow pptx = FileProvider.openPPTxFile(PPTxContstant.PPT_BLANK);
            XMLSlideShow subPPTx = FileProvider.openPPTxFile(PPTxContstant.PPT_TEMPLATE);
            XMLSlideShow outputPPTx = FileProvider.openPPTxFile(PPTxContstant.PPT_OUTPUT);
        } catch (PPTxException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
