package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {
    public static boolean isEmpty(Collection<?> coll)
    {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    public static <T>List<List<T>> splitList(List<T> list, final int LENGTH)
    {
        List<List<T>> partList = new ArrayList<List<T>>();
        int SIZE = list.size();
        for(int i = 0; i < SIZE; i += LENGTH)
        {
            partList.add(new ArrayList<T>(list.subList(i, Math.min(SIZE, i + LENGTH))));
        }
        return partList;
    }
}
