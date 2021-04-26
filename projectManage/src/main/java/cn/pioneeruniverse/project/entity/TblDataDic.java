package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *@author
 *@Description 数据字典类
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_data_dic")
public class TblDataDic extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4639097273797835613L;
	private String termName;  //数据字典名称
	private String termCode;  //数据字典编码
	private String valueName;//数据字典项名称
    private String valueCode;  //数据字典项编码
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
