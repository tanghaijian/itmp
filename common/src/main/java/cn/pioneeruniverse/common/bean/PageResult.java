package cn.pioneeruniverse.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
* @ClassName: PageResult
* @Description: 分页，未用
* @author author
* @date 2020年8月21日 下午1:49:14
*
* @param <T>
 */
public class PageResult<T> extends BasePage<T> implements Serializable {
	private static final long serialVersionUID = 1L;
 	protected List<T> result = null;
	 
	private Class<?> returnListClassType=null;
	 

	 
	public Class<?> getReturnListClassType() {
		return returnListClassType;
	}

	public void setReturnListClassType(Class<?> returnListClassType) {
		this.returnListClassType = returnListClassType;
	}

	public PageResult(Class<?> returnListClassType) {
		this.returnListClassType=returnListClassType;
	}
	public PageResult() {
 	}
	public PageResult(int pageSize) {
		this.pageSize = pageSize;
	}
	public PageResult(int pageSize,int pageNo) {
		setPageSize(pageSize);
		setPageNo(pageNo);
	}
	public PageResult<T> pageNo(long thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	 
	public PageResult<T> pageSize(int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}
 

	public PageResult<T> orderBy(String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}

 
	public PageResult<T> autoCount(boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}
 
	public List<T> getResult() {
		if (this.result == null) {
			return new ArrayList<T>();
		}
		return this.result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}
}