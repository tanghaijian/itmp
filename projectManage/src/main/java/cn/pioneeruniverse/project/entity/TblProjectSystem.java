package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblProjectSystem
* @Description: 项目和系统关联
* @author author
* @date 2020年8月7日 下午4:26:37
*
 */
@TableName("tbl_project_system")
public class TblProjectSystem extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private Long projectId;//项目ID
	
	private Long systemId;//系统ID
	
	private Integer relationType; //关联关系：（1:被开发系统，2:周边相关系统）

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Integer getRelationType() {
		return relationType;
	}

	public void setRelationType(Integer relationType) {
		this.relationType = relationType;
	}
	
}
