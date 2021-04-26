package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblProgramProject
* @Description:项目和项目群关联关系
* @author author
* @date 2020年8月7日 下午4:00:52
*
 */
@TableName("tbl_program_project")
public class TblProgramProject extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private Long programId;//项目群ID
	
	private Long projectId;//项目ID

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	
}
