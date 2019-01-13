package excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MergeColumnMeta
{
    public static final class MergeColumnRowMeta
    {
        private final String value;

        private int minRowIndex;

        private int maxRowIndex;

        private MergeColumnRowMeta(String value, int minRowIndex, int maxRowIndex)
        {
            this.value = value;
            this.minRowIndex = minRowIndex;
            this.maxRowIndex = maxRowIndex;
        }

        private boolean hasMultiRows()
        {
            return maxRowIndex != minRowIndex;
        }

        public String getValue()
        {
            return value;
        }

        public int getMinRowIndex()
        {
            return minRowIndex;
        }

        public int getMaxRowIndex()
        {
            return maxRowIndex;
        }

        /**
         * 获取行列表。
         * @return 合并行列表。from min to max
         */
        public List<Integer> getRowIndexList()
        {
            List<Integer> rowIdxList = new ArrayList<Integer>(maxRowIndex - minRowIndex + 1);
            for (int i = minRowIndex; i <= maxRowIndex; i++)
            {
                rowIdxList.add(i);
            }
            return rowIdxList;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MergeColumnMeta.class);
    private int columnIndex;
    private List<MergeColumnRowMeta> columnMergeRowIndexs = new LinkedList<MergeColumnRowMeta>();

    public MergeColumnMeta(int columnIndex)
    {
        this.columnIndex = columnIndex;
    }

    public void setColumnIndex(int columnIndex)
    {
        this.columnIndex = columnIndex;
    }
    public int getColumnIndex()
    {
        return columnIndex;
    }

    public Iterator<MergeColumnRowMeta> mergeRowIndexItr(boolean removeSingleRow)
    {
        // 返回的时候，倒序，以rowIndex从小到大顺序返回
        List<MergeColumnRowMeta> mergeRowData = new ArrayList<MergeColumnRowMeta>(columnMergeRowIndexs);
        Collections.reverse(mergeRowData);
        if (removeSingleRow)
        {
            Iterator<MergeColumnRowMeta> rowIndexItr = mergeRowData.iterator();
            for (;rowIndexItr.hasNext();)
            {
                MergeColumnRowMeta rowMeta = rowIndexItr.next();
                if (!rowMeta.hasMultiRows())
                {
                    rowIndexItr.remove();
                }
            }
        }
        return mergeRowData.iterator();
    }

    public void tryMergeRowData(int rowIndex, String value)
    {
        ListIterator<MergeColumnRowMeta> mergeRowMetaItr = columnMergeRowIndexs.listIterator();
        mergeRowValToRowListItr(rowIndex, value, mergeRowMetaItr);
    }

    private void mergeRowValToRowListItr(int rowIndex, String value, ListIterator<MergeColumnRowMeta> mergeRowMetaItr)
    {
        if (!mergeRowMetaItr.hasNext())
        {
            mergeRowMetaItr.add(new MergeColumnRowMeta(value, rowIndex, rowIndex));
            return;
        }
        MergeColumnRowMeta firstRowMeta = mergeRowMetaItr.next();
        if (firstRowMeta.maxRowIndex + 1 < rowIndex)
        {
            insertBeforCurrPos(new MergeColumnRowMeta(value, rowIndex, rowIndex),
                    mergeRowMetaItr, columnMergeRowIndexs);
            return;
        }
        if (firstRowMeta.minRowIndex - 1 > rowIndex)
        {
            /**
             * 找到合适的merge点
             * 此处使用迭代替换递归，能避免线程栈空间溢出风险
             */
            while (mergeRowMetaItr.hasNext())
            {
                MergeColumnRowMeta rowMeta = mergeRowMetaItr.next();
                if(rowMeta.minRowIndex -1 > rowIndex)
                {
                    continue;
                }
                mergeRowMetaItr.previous();
                break;
            }
            mergeRowValToRowListItr(rowIndex, value, mergeRowMetaItr);
            return;
        }
        //其他情况rowIndex
        if(!firstRowMeta.value.equals(value))
        {
            if(rowIndex < firstRowMeta.minRowIndex)
            {
                mergeRowValToRowListItr(rowIndex, value, mergeRowMetaItr);
            }
            else if(rowIndex > firstRowMeta.maxRowIndex)
            {
                insertBeforCurrPos(new MergeColumnRowMeta(value, rowIndex, rowIndex),
                        mergeRowMetaItr, columnMergeRowIndexs);
            }
            else
            {
                LOGGER.error("");
            }
            return;
        }
        else
        {
            firstRowMeta.minRowIndex = Math.min(firstRowMeta.maxRowIndex, rowIndex);
        }
        if(!mergeRowMetaItr.hasNext())
        {
            return;
        }
        MergeColumnRowMeta secondRowMeta = mergeRowMetaItr.next();
        if(secondRowMeta != null)
        {
            if(firstRowMeta.minRowIndex <= secondRowMeta.maxRowIndex + 1
                    && firstRowMeta.value.equals(secondRowMeta.value))
            {
                mergeRowMetaItr.remove();
                firstRowMeta.minRowIndex = secondRowMeta.minRowIndex;
            }
        }
    }

    private static <T> void  insertBeforCurrPos(T data, ListIterator<T> listItr, List<T> dataList)
    {
        if(listItr.hasPrevious())
        {
            listItr.previous();
            listItr.add(data);
        }
        else
        {
            //队列最前
            dataList.add(0, data);
        }
    }
}
