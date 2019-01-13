package util;

import jdk.internal.org.objectweb.asm.TypeReference;
import org.apache.poi.util.BoundedInputStream;
import org.codehaus.jackson.map.DeserializationConfig;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static ThreadLocal<ObjectMapper> mapper = ThreadLocal.withInitial(() -> new ObjectMapper());

    public static ObjectMapper getMapper()
    {
        return mapper.get();
    }

    public static String toJson(Object obj)
    {

        try {
            return mapper.get().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T fromJson(InputStream input, TypeReference<T>  typeRef)
    {
        try {
            mapper.get().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.get().readValue(input, typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T fromJson(InputStream input, Class<T> objClass)
    {
        try {
            mapper.get().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.get().readValue(input, objClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T fromJson(String jsonStr, Class<T> objClass)
    {
        try {
            mapper.get().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.get().readValue(jsonStr, objClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T fromJson(String jsonStr, TypeReference<T> typeRef)
    {
        mapper.get().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.get().readValue(jsonStr, typeRef);
    }

    public static String loadJsonFromFile(String filePath)
    {
        File file = new File(filePath);
        if((!file.exists()) || (!file.isFile()))
        {
            return null;
        }

        BufferedReader reader = null;
        StringBuilder jsonstr = new StringBuilder();


        try {
            FileInputStream inputStream = new FileInputStream(file);
            BoundedInputStream bis = new BoundedInputStream(inputStream);
            InputStreamReader isr = new InputStreamReader(bis);
            reader = new BufferedReader(isr, 2048);
            String tempString = null;

            while ((tempString = reader.readLine()) != null)
            {
                jsonstr.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null)
            {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonstr.toString();
    }
}
