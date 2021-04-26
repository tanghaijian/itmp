package cn.pioneeruniverse.dev.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName: TblNoticeInfo
 * @Description: 通知类
 * @author author
 *
 */
@TableName("tbl_notice_info")
public class TblNoticeInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer noticeType;//类型

	private String projectIds;//项目id

	private String noticeContent;

	private Date startTime;//开始时间

	private Date endTime;//结束时间

	@TableField(exist = false)
	private String userName;//姓名
	
	@TableField(exist = false)
	private String projectNames; //项目名

	@TableField(exist = false)
	private Date createStartDate;//查询开始创建时间

	@TableField(exist = false)
	private Date createEndDate;//查询结束创建时间

	@TableField(exist = false)
	private String createDateStr;//查询条件日期字符串

	@TableField(exist = false)
	private String validDateStr;//查询条件日期字符串
	
	@TableField(exist = false)
	private List<Long> currentUserProjectList;//查询项目组
	
	public Integer getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}

	public String getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(String projectIds) {
		this.projectIds = projectIds == null ? null : projectIds.trim();
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent == null ? null : noticeContent.trim();
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getProjectNames() {
		return projectNames;
	}

	public void setProjectNames(String projectNames) {
		this.projectNames = projectNames;
	}

	public Date getCreateStartDate() {
		return createStartDate;
	}

	public void setCreateStartDate(Date createStartDate) {
		this.createStartDate = createStartDate;
	}

	public Date getCreateEndDate() {
		return createEndDate;
	}

	public void setCreateEndDate(Date createEndDate) {
		this.createEndDate = createEndDate;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getValidDateStr() {
		return validDateStr;
	}

	public void setValidDateStr(String validDateStr) {
		this.validDateStr = validDateStr;
	}

	public List<Long> getCurrentUserProjectList() {
		return currentUserProjectList;
	}

	public void setCurrentUserProjectList(List<Long> currentUserProjectList) {
		this.currentUserProjectList = currentUserProjectList;
	}


}