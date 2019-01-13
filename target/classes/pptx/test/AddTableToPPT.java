package test;

import pptx.apiTest.element.FileProvider;
import pptx.apiTest.element.TableProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.apiTest.model.data.TableData;
import pptx.apiTest.model.style.TableStyle;
import pptx.contstant.PPTxContstant;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.util.ArrayList;
import java.util.List;

public class AddTableToPPT {
    public static void main(String[] args) {
        try {
            XMLSlideShow pptx = FileProvider.openPPTxFile(PPTxContstant.PPT_BLANK);
            XSLFSlide slide = FileProvider.createSlide(pptx, "Table Test");
            //创建表格
            TableData tableData = new TableData();
            List<String> headers = new ArrayList<>();
            headers.add("2016");
            List<List<String>> rows = new ArrayList<>();
            List<String> row = new ArrayList<>();
            row.add("2016-04");
            rows.add(row);
            tableData.setHeaders(headers);
            tableData.setRows(rows);
            TableStyle tableStyle = new TableStyle();
            TableProvider.addTableWithHeader(slide, tableStyle, tableData, 100, 250, 100,100);
        } catch (PPTxException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
