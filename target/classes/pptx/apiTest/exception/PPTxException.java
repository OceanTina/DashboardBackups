package pptx.apiTest.exception;


import java.util.function.Supplier;

public class PPTxException extends Exception implements Supplier {
    public PPTxException(String errorCode, Exception e)
    {
        super(errorCode,e);
    }

    public PPTxException(String errorCode)
    {
        super(errorCode);
    }


    public PPTxException get() {
        return new PPTxException(super.getMessage());
    }
}
