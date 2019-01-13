package test;

import pptx.apiTest.element.FileProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.contstant.PPTxContstant;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.util.ArrayList;
import java.util.List;

public class CopyPPTx {

    public static void main(String[] args) {
        List<Integer> indexLst = new ArrayList<>();
        indexLst.add(0);
        indexLst.add(1);
        indexLst.add(2);
        try {
            XMLSlideShow outPutPPTx = FileProvider.copyPPTxFile(PPTxContstant.PPT_TEMPLATE,
                    PPTxContstant.PPT_TEMPLATE, indexLst);
            FileProvider.savePPTxFile(outPutPPTx, PPTxContstant.PPT_OUTPUT);
        } catch (PPTxException e) {
            e.printStackTrace();
        }
    }
}
