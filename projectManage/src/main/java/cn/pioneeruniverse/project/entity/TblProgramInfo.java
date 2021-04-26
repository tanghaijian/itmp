package cn.pioneeruniverse.project.entity;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblProgramInfo
* @Description: 项目群
* @author author
* @date 2020年8月7日 下午3:59:57
*
 */
@TableName("tbl_program_info")
public class TblProgramInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private String programName;//群名
	
	private String programNumber;//群编号
	
	private Long manageUserId;  //项目群经理
	
	private String programIntro;  //项目群简介
	
	private String programBackground;  //项目群背景
	
	private String remark;  //备注
	
	@Transient
	private String projectIds;//项目ids
	
	@Transient
	private String manageUser;//项目群经理
	
	@Transient
	private List<TblProjectInfo> projectList;//群中的项目组
	

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramNumber() {
		return programNumber;
	}

	public void setProgramNumber(String programNumber) {
		this.programNumber = programNumber;
	}

	public Long getManageUserId() {
		return manageUserId;
	}

	public void setManageUserId(Long manageUserId) {
		this.manageUserId = manageUserId;
	}

	public String getProgramIntro() {
		return programIntro;
	}

	public void setProgramIntro(String programIntro) {
		this.programIntro = programIntro;
	}

	public String getProgramBackground() {
		return programBackground;
	}

	public void setProgramBackground(String programBackground) {
		this.programBackground = programBackground;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(String projectIds) {
		this.projectIds = projectIds;
	}

	public String getManageUser() {
		return manageUser;
	}

	public void setManageUser(String manageUser) {
		this.manageUser = manageUser;
	}

	public List<TblProjectInfo> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<TblProjectInfo> projectList) {
		this.projectList = projectList;
	}
	
	
}
