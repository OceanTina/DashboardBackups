package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.Set;

public class SafeResourceUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    public static OutputStream getSafeLinuxOStream(String filePath, boolean isGroupReadShare) throws IOException
    {
        //先校验文件路径
        File file = new File(safeFilePath(filePath));
        if (!file.exists())
        {
            boolean flag = file.getParentFile().mkdirs();
            if (!flag)
            {
                LOGGER.error("mkdirs return false");
            }
        }
        Path path = file.toPath();
        FileAttribute<Set<PosixFilePermission>> attr = getDefaultFileAttribute(file, isGroupReadShare);
        EnumSet<StandardOpenOption> localEnumSet = EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        Files.newByteChannel(path, localEnumSet, attr).close();
        OutputStream out = Files.newOutputStream(path);
        return out;
    }

    public static OutputStream getSafeLinuxOStream(File file, boolean isGroupReadShare) throws IOException
    {
        String path = file.getPath();
        return getSafeLinuxOStream(path,isGroupReadShare);
    }

    public static String safeFilePath(String sFilePath) throws IOException
    {
        if (sFilePath == null)
        {
            return null;
        }

        String fAbsolutePath = getFileCanonicalPath(sFilePath);
        boolean isSafePath = false;
        for (String whitePath : LINUX_WHITE_FILE_PATH)
        {
            if (fAbsolutePath.startsWith(whitePath))
            {
                isSafePath = true;
                break;
            }
        }
        if (!isSafePath)
        {
            throw new IOException("Path Manipulation error");
        }
        return sFilePath;
    }
}
