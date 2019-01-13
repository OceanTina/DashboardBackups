package pptx.pptReport;

import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.util.HashSet;
import java.util.Set;

public class PPTxSlideDeleter {
    private static Set<Integer> deleteIndex = new HashSet<Integer>();

    public static void addIndex(int i)
    {
        deleteIndex.add(i);
    }

    public static void setDeleteIndex(int i)
    {
        deleteIndex.remove(i);
    }

    public static void removeSlide(XMLSlideShow slideShow)
    {
        deleteIndex.stream().sorted((a,b) -> (b-a)).forEach(i -> slideShow.removeSlide(i));
        deleteIndex.clear();
    }
}
