package excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class QThread implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(QThread.class);

    public QThread() {
    }

    public abstract void doAction();

    public abstract String getTreadName();

    public void run()
    {
        LOGGER.info("doTask begin :taskName =" + this.getTreadName() + "hashCode = " + this.toString());

        this.doAction();
    }
}
