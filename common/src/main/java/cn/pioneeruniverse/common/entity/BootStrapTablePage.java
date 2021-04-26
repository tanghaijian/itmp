package cn.pioneeruniverse.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 针对bootstraptable分页
 * @Date: Created in 17:39 2018/12/18
 * @Modified By:
 */
public class BootStrapTablePage<T extends BaseEntity> implements Serializable {
    private static final long serialVersionUID = -4909025063087292168L;

    private Integer pageNumber;
    private Integer pageSize;
    private Integer total;
    private List<T> rows;
    private Object otherData;

    public BootStrapTablePage(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }


    public BootStrapTablePage(HttpServletRequest request, HttpServletResponse response) {
        String pageNumber = request.getParameter("pageNumber");
        if (StringUtils.isNumeric(pageNumber)) {
            this.pageNumber = (Integer.parseInt(pageNumber));
        }
        String pageSize = request.getParameter("pageSize");
        if (StringUtils.isNumeric(pageSize)) {
            this.pageSize = (Integer.parseInt(pageSize));
        }
    }

    public BootStrapTablePage(Integer total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }


    @JsonIgnore
    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    @JsonIgnore
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Object getOtherData() {
        return otherData;
    }

    public void setOtherData(Object otherData) {
        this.otherData = otherData;
    }
}
