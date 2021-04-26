package cn.pioneeruniverse.project.vo;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * @Author:
 * @Description: 需求vo表
 * @Date: Created in 15:06 2020/08/25
 * @Modified By:
 */
public class RequirementVo extends BaseEntity{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private	Long parentId;	//父ID
	private	String parentIds;	//所有父ID(如：,1,2,3,)
	private String reqStatusName;	//需求状态名称(数据字典)
	private String reqSourceName;	//需求来源名称(数据字典)
	private String reqTypeName;		//需求类型名称(数据字典)
	private String userName;	//需求提出人名称
	private String deptName;	//归属部门名称
	private	String reqPlanName;	//需求计划名称(数据字典)
	private	String reqPriorityName;//需求优先级名称(数据字典)
	private	String devDeptName;	//开发部门名称

	//重点需求相关
	private	String impReqStatusName;//是否重点需求(1=是，2=否)(数据字典)
	private	String impReqDelayStatusName;//重点需求是否延误(1=是，2=否)(数据字典)

	//其他信息
	private	String hangupStatusName;	//需求是否挂起(1=是；2=否)(数据字典)
	private	String devManageUserName;	//开发管理人员(用户表主键)
	private	String reqManageUserName;	//需求管理人员(用户表主键)
	private	String reqAcceptanceUserName;//需求验收人员(用户表主键)
	private	String dataMigrationStatusName;//是否数据迁移(状态 1=是；2=否)(数据字典)

	private String systemId;//查询条件(涉及系统id)
	private String featureId;//查询条件(开发任务id)

	private String DevManageIds;//查询条件(开发岗Id)
	private String reqManageIds;//查询条件(需求纲id)
	private String funCount;//需求功能点
	private Long userId;	//当前登陆用户


	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getReqStatusName() {
		return reqStatusName;
	}
	public void setReqStatusName(String reqStatusName) {
		this.reqStatusName = reqStatusName;
	}
	public String getReqSourceName() {
		return reqSourceName;
	}
	public void setReqSourceName(String reqSourceName) {
		this.reqSourceName = reqSourceName;
	}
	public String getReqTypeName() {
		return reqTypeName;
	}
	public void setReqTypeName(String reqTypeName) {
		this.reqTypeName = reqTypeName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getReqPlanName() {
		return reqPlanName;
	}
	public void setReqPlanName(String reqPlanName) {
		this.reqPlanName = reqPlanName;
	}
	public String getReqPriorityName() {
		return reqPriorityName;
	}
	public void setReqPriorityName(String reqPriorityName) {
		this.reqPriorityName = reqPriorityName;
	}
	public String getDevDeptName() {
		return devDeptName;
	}
	public void setDevDeptName(String devDeptName) {
		this.devDeptName = devDeptName;
	}
	public String getImpReqStatusName() {
		return impReqStatusName;
	}
	public void setImpReqStatusName(String impReqStatusName) {
		this.impReqStatusName = impReqStatusName;
	}
	public String getImpReqDelayStatusName() {
		return impReqDelayStatusName;
	}
	public void setImpReqDelayStatusName(String impReqDelayStatusName) {
		this.impReqDelayStatusName = impReqDelayStatusName;
	}
	public String getHangupStatusName() {
		return hangupStatusName;
	}
	public void setHangupStatusName(String hangupStatusName) {
		this.hangupStatusName = hangupStatusName;
	}
	public String getDevManageUserName() {
		return devManageUserName;
	}
	public void setDevManageUserName(String devManageUserName) {
		this.devManageUserName = devManageUserName;
	}
	public String getReqManageUserName() {
		return reqManageUserName;
	}
	public void setReqManageUserName(String reqManageUserName) {
		this.reqManageUserName = reqManageUserName;
	}
	public String getReqAcceptanceUserName() {
		return reqAcceptanceUserName;
	}
	public void setReqAcceptanceUserName(String reqAcceptanceUserName) {
		this.reqAcceptanceUserName = reqAcceptanceUserName;
	}
	public String getDataMigrationStatusName() {
		return dataMigrationStatusName;
	}
	public void setDataMigrationStatusName(String dataMigrationStatusName) {
		this.dataMigrationStatusName = dataMigrationStatusName;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public String getFunCount() {
		return funCount;
	}
	public void setFunCount(String funCount) {
		this.funCount = funCount;
	}

	public String getDevManageIds() {
		return DevManageIds;
	}

	public void setDevManageIds(String devManageIds) {
		DevManageIds = devManageIds;
	}

	public String getReqManageIds() {
		return reqManageIds;
	}

	public void setReqManageIds(String reqManageIds) {
		this.reqManageIds = reqManageIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}