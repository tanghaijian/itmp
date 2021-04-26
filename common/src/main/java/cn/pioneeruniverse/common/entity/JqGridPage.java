package cn.pioneeruniverse.common.entity;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
* @ClassName: JqGridPage
* @Description: jqgrid分页封装
* @author author
* @date 2020年8月21日 下午4:13:53
*
* @param <T>
 */
public class JqGridPage<T extends BaseEntity> implements Serializable {
    private static final long serialVersionUID = 9081663781709034389L;

    //jqGrid请求参数
    private JqGridPrmNames jqGridPrmNames;

    /*
    * jqGrid回传参数
    **/
    private Integer page;//当前页
    private Integer total;//页码总数
    private Long records;//数据行数   总条数
    private Collection<JqGridRows<T>> rows;
    
    private String status;  //状态
    private String message;	//错误信息

    public JqGridPage() {
    }

    /**
     * 
    * <p>Title: </p>
    * <p>Description: jqGrid页面相关字段
    * @author author
    * @param request
    * @param response
     */
    public JqGridPage(HttpServletRequest request, HttpServletResponse response) {
    	
    	//请求页码
        String page = request.getParameter("page");
        JqGridPrmNames jqGridPrmNames = new JqGridPrmNames();
        if (StringUtils.isNumeric(page)) {
            jqGridPrmNames.setPage(Integer.parseInt(page));
        }
        
      //请求行数
        String rows = request.getParameter("rows");
        if (StringUtils.isNumeric(rows)) {
            jqGridPrmNames.setRows(Integer.parseInt(rows));
        }
     
        //用于排序的列名
        String sidx = request.getParameter("sidx");
        if (StringUtils.isNotBlank(sidx)) {
            jqGridPrmNames.setSidx(sidx);
        }
      //采用的排序方式 desc or asc
        String sord = request.getParameter("sord");
        if (StringUtils.isNotBlank(sord)) {
            jqGridPrmNames.setSord(sord);
        }
      //是否搜索请求
        String search = request.getParameter("search");
        if (StringUtils.isNotBlank(search)) {
            jqGridPrmNames.setSearch(Boolean.parseBoolean(search));
        }
      //已经发送的请求次数
        String nd = request.getParameter("nd");
        if (StringUtils.isNumeric(nd)) {
            jqGridPrmNames.setNd(Long.parseLong(nd));
        }
        
        //请求总行数
        String totalrows = request.getParameter("totalrows");
        if (StringUtils.isNumeric(totalrows)) {
            jqGridPrmNames.setTotalrows(Integer.parseInt(totalrows));
        }
        //请求需要过滤的字符串
        String filters = request.getParameter("filters");
        if (StringUtils.isNotEmpty(filters)) {
            jqGridPrmNames.setFilters(filters);
        }
        this.jqGridPrmNames = jqGridPrmNames;
    }

    /**
     * 
    * @Title: filtersAttrToEntityField
    * @Description: 根据过滤规则，将对应的对象字段赋值
    * @author author
    * @param t
    * @throws Exception
    * @throws
     */
    public void filtersAttrToEntityField(T t) throws Exception {
        if (this.getJqGridPrmNames().getFilters() != null && CollectionUtil.isNotEmpty(this.getJqGridPrmNames().getFilters().getRules())) {
            Class<? extends Object> clazz = t.getClass();
            for (JqGridPrmFiltersRule rule : this.getJqGridPrmNames().getFilters().getRules()) {
                Field field = clazz.getDeclaredField(rule.getField());
                field.setAccessible(true);
                field.set(t, ConvertUtils.convert(field.getType() == String.class ? rule.getData().toString().trim() : rule.getData(), field.getType()));
            }
        }
    }

    /**
     * 
    * @Title: filtersAttrToEntityField
    * @Description: 根据过滤规则，生成新的对象，并对对象字段进行赋值。
    * @author author
    * @param t
    * @throws Exception
    * @throws
     */
    public T filtersToEntityField(T t) throws Exception {
        if (this.getJqGridPrmNames().getFilters() != null && CollectionUtil.isNotEmpty(this.getJqGridPrmNames().getFilters().getRules())) {
            List<JqGridPrmFiltersRule> rules = this.getJqGridPrmNames().getFilters().getRules();
            Class<? extends Object> clazz = t.getClass();
            t = (T) Class.forName(clazz.getName()).newInstance();
            for (JqGridPrmFiltersRule rule : rules) {
                Field field = clazz.getDeclaredField(rule.getField());
                field.setAccessible(true);
                field.set(t, ConvertUtils.convert(field.getType() == String.class ? rule.getData().toString().trim() : rule.getData(), field.getType()));
            }
        }
        return t;
    }

    /**
     * 
    * @Title: processDataForResponse
    * @Description: 封装返回的数据
    * @author author
    * @param pageInfo PageHelper返回封装PageInfo对象
    * @throws
     */
    public void processDataForResponse(PageInfo pageInfo) {
        this.setPage(pageInfo.getPageNum());//当前页
        this.setTotal(pageInfo.getPages());//总也
        this.setRecords(pageInfo.getTotal());//总条数
        this.setCells(pageInfo.getList());//数据

    }


    @SuppressWarnings("unchecked")
    public void setCells(Collection<T> collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            Collection<JqGridRows<T>> rowsCollection = new ArrayList<>();
            int size = collection.size();
            Object[] ts = collection.toArray();
            for (int i = 0; i < size; i++) {
                rowsCollection.add(new JqGridRows<T>((T) ts[i]));
            }
            setRows(rowsCollection);
        }
    }

    @JsonIgnore
    public JqGridPrmNames getJqGridPrmNames() {
        return jqGridPrmNames;
    }

    public void setJqGridPrmNames(JqGridPrmNames jqGridPrmNames) {
        this.jqGridPrmNames = jqGridPrmNames;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


    public Long getRecords() {
        return records;
    }

    public void setRecords(Long records) {
        this.records = records;
    }

    public Collection<JqGridRows<T>> getRows() {
        return rows;
    }

    public void setRows(Collection<JqGridRows<T>> rows) {
        this.rows = rows;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
}
