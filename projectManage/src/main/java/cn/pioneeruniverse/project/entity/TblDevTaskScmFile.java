package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

@TableName("tbl_dev_task_scm_file")
public class TblDevTaskScmFile extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 工作任务表主键
	 **/
	private Long devTaskId;// 工作任务表主键

	/**
	 * 工作任务代码关联表
	 **/
	private Long devTaskScmId;// 工作任务代码关联表

	/**
	 * 源码管理地址
	 **/
	private String scmUrl;// 源码管理地址

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

	public String getScmUrl() {
		return scmUrl;
	}

	public void setScmUrl(String scmUrl) {
		this.scmUrl = scmUrl;
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

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	

}
