package util;

public class TempFileUtils {
    public static void clearTempFile(String tempPath) {
        FileUtils.clearDir(tempPath);
    }
}
