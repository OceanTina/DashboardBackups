package redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SerializeUtil
{
    private static final Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

    private SerializeUtil()
    {
    }

    public static byte[] serialize(Object object)
    {
        ObjectOutputStream objectOutput = null;

        ByteArrayOutputStream byteArrayOutput = null;
        try
        {
            byteArrayOutput = new ByteArrayOutputStream();

            objectOutput = new ObjectOutputStream(byteArrayOutput);

            objectOutput.writeObject(object);

            return byteArrayOutput.toByteArray();
        }
        catch (IOException e)
        {
            logger.error("serialize object fail!", e);
        }
        return new byte[0];
    }

    public static Object unserialize(byte[] bytes)
    {
        ObjectInputStream in = null;
        try
        {
            in = new ObjectInputStream(new ByteArrayInputStream(bytes));

            return in.readObject();
        }
        catch (ClassNotFoundException ex)
        {
            logger.error("unserialize object fail!", ex);
        }
        catch (IOException e)
        {
            logger.error("unserialize object fail!", e);
        }
        return null;
    }
}