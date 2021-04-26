package cn.pioneeruniverse.common.sonar.bean;
/**
 *
 * @ClassName:Paging
 * @deprecated
 * @author author
 * @date 2020年8月24日
 *
 */
public class Paging {

    private int pageIndex;//页数
    private int pageSize;//条数
    private int total;//共计

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }
}
