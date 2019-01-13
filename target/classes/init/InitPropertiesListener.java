package init;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class InitPropertiesListener implements ServletContextListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(InitPropertiesListener.class);

    private Properties props = new Properties();
    private Map<Object, Object> map = new HashedMap();

    /**
     * 系统初始化把日志输路径加载的环境变量里面
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        InputStream inputStream = null;

        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("constant.properties");
            props.load(inputStream);
            map = props;

            for(Map.Entry<Object, Object> entry : map.entrySet())
            {
                BaseConstants.propertiesMap.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if(null !=inputStream)
                {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
