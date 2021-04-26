package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * @Author:
 * @Description: 数据字典实体类
 * @Date: Created in 15:06 2020/08/25
 * @Modified By:
 */
public class TblDataDic extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4639097273797835613L;
	private String termName;  //1几名称
	private String termCode;  //2
	private String valueName;//i名称
    private String valueCode;  //code
    private Integer valueSeq;  //排序
    
	
	public String getTermName() {
		return termName;
	}
	public void setTermName(String termName) {
		this.termName = termName;
	}
	public String getTermCode() {
		return termCode;
	}
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	
	public String getValueCode() {
		return valueCode;
	}
	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}
	public Integer getValueSeq() {
		return valueSeq;
	}
	public void setValueSeq(Integer valueSeq) {
		this.valueSeq = valueSeq;
	}
	
	
	
	
	
}
