package cn.pioneeruniverse.dev.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * @Author:
 * @Description:系统JENKINS参数枚举值实体类
 */
@TableName("tbl_system_jenkins_parameter_values")
public class TblSystemJenkinsParameterValues  extends BaseEntity{
	private Long systemJenkinsParameterId; //TblSystemJenkinsParameter的ID
	
	private String parameterValue;//枚举值
	
	private Integer valueSeq;//排序
	
	private String systemJenkinsId;//对应的TblSystemJenkins的id，具体的jenkins的job信息
	@TableField(exist = false)
	private List<ZtreeObj> listZtree;//前端树形展示时转换使用

	public Long getSystemJenkinsParameterId() {
		return systemJenkinsParameterId;
	}

	public void setSystemJenkinsParameterId(Long systemJenkinsParameterId) {
		this.systemJenkinsParameterId = systemJenkinsParameterId;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public Integer getValueSeq() {
		return valueSeq;
	}

	public void setValueSeq(Integer valueSeq) {
		this.valueSeq = valueSeq;
	}

	public String getSystemJenkinsId() {
		return systemJenkinsId;
	}

	public void setSystemJenkinsId(String systemJenkinsId) {
		this.systemJenkinsId = systemJenkinsId;
	}

	public List<ZtreeObj> getListZtree() {
		return listZtree;
	}

	public void setListZtree(List<ZtreeObj> listZtree) {
		this.listZtree = listZtree;
	}
	
	
}
