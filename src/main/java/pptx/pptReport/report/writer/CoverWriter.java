package pptx.pptReport.report.writer;

import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pptx.apiTest.element.ShapeProvider;
import pptx.apiTest.element.TextBoxProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.pptReport.PPTxSlideWriter;

import java.util.List;
import java.util.Optional;

@Component
public class CoverWriter extends PPTxSlideWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoverWriter.class);
    private static final int FIRSTSLIDE = 0;

    public CoverWriter() {
        slidesindex.add(FIRSTSLIDE);
    }

    public void writeSlide()throws PPTxException
    {
        String projectName = null;

        try {
            projectName = ProjectRoa.getProjectNameByTaskId(this.taskId);
            LOGGER.error("export PPT-ALL report:taskId{} and project name {}",taskId, projectName);

            //获取模板的第一页
            XSLFSlide firstSlide = slideMap.get(FIRSTSLIDE);

            List<XSLFTextBox> textBoxList = ShapeProvider.getShapeListByType(firstSlide, XSLFTextBox.class);

            Optional.ofNullable(textBoxList.get(Commons.INDEX_THREE)).orElseThrow(() -> new PPTxException("TextBox not exist!"));

            TextBoxProvider.replaceText(textBoxList.get(Commons.INDEX_THREE), "%S", projectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
