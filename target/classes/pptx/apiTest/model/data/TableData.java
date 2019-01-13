package pptx.apiTest.model.data;

import java.util.List;

public class TableData {
    private List<String> headers;
    private List<List<String>> rows;

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }
}
