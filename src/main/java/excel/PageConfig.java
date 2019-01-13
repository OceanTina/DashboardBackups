package excel;

import java.util.List;

public class PageConfig {
    private static final int DEFALUT_MAX_PAGESIZE = 5000;

    private List<?> data;
    private List<CellMergeRegion> mergeRegionList;
    private int pageSize;
    private int curPage;
    private int totalPages;
    private int totalNum;
    private String sheetName;


    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
        this.totalNum = this.data.size();
        this.totalPages = countTotalPage(totalNum, pageSize);
    }

    public List<CellMergeRegion> getMergeRegionList() {
        return mergeRegionList;
    }

    public void setMergeRegionList(List<CellMergeRegion> mergeRegionList) {
        this.mergeRegionList = mergeRegionList;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize > DEFALUT_MAX_PAGESIZE ? DEFALUT_MAX_PAGESIZE : pageSize;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    private int countTotalPage(int totalNumber, int iPageSize)
    {
        totalNumber = totalNumber < 0 ? 0 : totalNumber;
        iPageSize = iPageSize < 1 ? 1 : iPageSize;
        int totalPage = totalNumber / iPageSize;
        final int surplus = totalNumber % iPageSize;
        if(surplus > 0)
        {
            ++totalPage;
        }
        return totalPage;
    }

    public List<?> page()
    {
        curPage = curPage < 1 ? 1 : curPage;
        curPage = curPage > totalPages ? totalPages : curPage;
        int start = (curPage - 1) * pageSize;
        int end = start + pageSize > totalNum ? totalNum : (start + pageSize);
        return data.subList(start, end);
    }

    public void reset()
    {
        if(this.data !=null)
        {
            this.data.clear();
        }
        this.data = null;
        if(this.mergeRegionList != null)
        {
            this.mergeRegionList.clear();
        }
        this.mergeRegionList = null;
        this.curPage = 1;
    }
}
