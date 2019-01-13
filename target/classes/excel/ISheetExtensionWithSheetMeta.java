package excel;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface ISheetExtensionWithSheetMeta extends ISheetExtension {

    void render(Sheet sheet, SheetMeta sheetMeta, List<?> data, int startIndex, int endIndex);
}
