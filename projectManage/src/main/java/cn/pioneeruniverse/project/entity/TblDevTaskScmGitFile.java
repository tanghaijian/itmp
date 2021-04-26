package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

@TableName("tbl_dev_task_scm_git_file")
public class TblDevTaskScmGitFile extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 工作任务表主键
	 **/
	private Long devTaskId;// 工作任务表主键

	/**
	 * 工作任务表主键
	 **/
	private Long devTaskScmId;// 工作任务表主键

	/**
	 * 工具表主键
	 **/
	private Long toolId;// 工具表主键

	/**
	 * GIT仓库主键
	 **/
	private Long gitRepositoryId;// GIT仓库主键

	/**
	 * GIT分支
	 **/
	private String gitBranch;// GIT分支

	/**
	 * 源码提交版本号
	 **/
	private String commitNumber;// 源码提交版本号

	/**
	 * 本工作任务首次提交源码的上一版本号
	 **/
	private String lastCommitNumber;// 本工作任务首次提交源码的上一版本号

	/**
	 * 提交文件
	 **/
	private String commitFile;// 提交文件

	/**
	 * 重命名前文件路径
	 **/
	private String beforeRenameFile;// 重命名前文件路径

	/**
	 * 操作类型
	 **/
	private String operateType;// 操作类型

	public Long getDevTaskId() {
		return devTaskId;
	}

	public void setDevTaskId(Long devTaskId) {
		this.devTaskId = devTaskId;
	}

	public Long getDevTaskScmId() {
		return devTaskScmId;
	}

	public void setDevTaskScmId(Long devTaskScmId) {
		this.devTaskScmId = devTaskScmId;
	}

	public Long getToolId() {
		return toolId;
	}

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}

	public Long getGitRepositoryId() {
		return gitRepositoryId;
	}

	public void setGitRepositoryId(Long gitRepositoryId) {
		this.gitRepositoryId = gitRepositoryId;
	}

	public String getGitBranch() {
		return gitBranch;
	}

	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}

	public String getCommitNumber() {
		return commitNumber;
	}

	public void setCommitNumber(String commitNumber) {
		this.commitNumber = commitNumber;
	}

	public String getLastCommitNumber() {
		return lastCommitNumber;
	}

	public void setLastCommitNumber(String lastCommitNumber) {
		this.lastCommitNumber = lastCommitNumber;
	}

	public String getCommitFile() {
		return commitFile;
	}

	public void setCommitFile(String commitFile) {
		this.commitFile = commitFile;
	}

	public String getBeforeRenameFile() {
		return beforeRenameFile;
	}

	public void setBeforeRenameFile(String beforeRenameFile) {
		this.beforeRenameFile = beforeRenameFile;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	
	
}
