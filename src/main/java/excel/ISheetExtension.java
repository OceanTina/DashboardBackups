package excel;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface ISheetExtension {

    void render(Sheet sheet, List<?> data, int startIndex, int endIndex);

    void render(Sheet sheet, String conditionJson, int startIndex, POISupport poiSupport);
}
