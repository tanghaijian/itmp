package cn.pioneeruniverse.common.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * Created by admin on 2018/11/1.
 * 封装jqGrid的prmNames所有参数
 * prmNames是jqGrid的一个重要选项，用于设置jqGrid将要向Server传递的参数名称
 */
public class JqGridPrmNames implements Serializable {
    private static final long serialVersionUID = -7820403946626903987L;

    //请求页码
    private Integer page;

    //请求行数
    private Integer rows;

    //用于排序的列名
    private String sidx;

    //采用的排序方式
    private String sord;

    //是否搜索请求
    private Boolean search;

    //已经发送的请求次数
    private Long nd;

    //需从Server得到总共多少行数据
    private Integer totalrows;

    //过滤
    private JqGridPrmFilters filters;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public Boolean getSearch() {
        return search;
    }

    public void setSearch(Boolean search) {
        this.search = search;
    }

    public Long getNd() {
        return nd;
    }

    public void setNd(Long nd) {
        this.nd = nd;
    }

    public Integer getTotalrows() {
        return totalrows;
    }

    public void setTotalrows(Integer totalrows) {
        this.totalrows = totalrows;
    }

    public JqGridPrmFilters getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
    	filters = filters.replaceAll("&quot;", "\"");
        JqGridPrmFilters jqGridPrmFilters = JSON.parseObject(filters, JqGridPrmFilters.class);
        this.filters = jqGridPrmFilters;
    }

}
