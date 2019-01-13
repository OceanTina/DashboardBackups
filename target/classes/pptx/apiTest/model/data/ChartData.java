package pptx.apiTest.model.data;

import java.util.List;
import java.util.Map;

public class ChartData {
    private List<String> categories;
    Map<String, List<String>> serials;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Map<String, List<String>> getSerials() {
        return serials;
    }

    public void setSerials(Map<String, List<String>> serials) {
        this.serials = serials;
    }
}
