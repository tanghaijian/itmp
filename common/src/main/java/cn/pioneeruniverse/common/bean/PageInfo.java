package cn.pioneeruniverse.common.bean;

public class PageInfo {

    /** 当前页码 */
    private int pageIndex;
    /** 每页显示记录数 */
    private int pageSize;
    /** 总页数 */
    private long totalRowNum;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRowNum() {
        return totalRowNum;
    }

    public void setTotalRowNum(long totalRowNum) {
        this.totalRowNum = totalRowNum;
    }
}
