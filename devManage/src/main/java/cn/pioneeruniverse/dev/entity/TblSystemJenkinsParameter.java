package cn.pioneeruniverse.dev.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 * @Author:
 * @Description:系统JENKINS参数实体类
 */
@TableName("tbl_system_jenkins_parameter")
public class TblSystemJenkinsParameter extends BaseEntity{
	private Long systemId;//系统ID
	@TableField(exist = false)
	private String systemName;//系统名称
	
	private String parameterName;//参数名
	
	private Integer selectType;//选择框类型（1：单选；2：多选）
	@TableField(exist = false)
	private String  selectTypeValue;//选择框类型名
	@TableField(exist = false)
	private List<TblSystemJenkinsParameterValues>  parameterValuesList;//对应的枚举值
	
	public Long getSystemId() {
		return systemId;
	}
	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public Integer getSelectType() {
		return selectType;
	}
	public void setSelectType(Integer selectType) {
		this.selectType = selectType;
	}
	public List<TblSystemJenkinsParameterValues> getParameterValuesList() {
		return parameterValuesList;
	}
	public void setParameterValuesList(List<TblSystemJenkinsParameterValues> parameterValuesList) {
		this.parameterValuesList = parameterValuesList;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getSelectTypeValue() {
		return selectTypeValue;
	}
	public void setSelectTypeValue(String selectTypeValue) {
		this.selectTypeValue = selectTypeValue;
	}
	
}
