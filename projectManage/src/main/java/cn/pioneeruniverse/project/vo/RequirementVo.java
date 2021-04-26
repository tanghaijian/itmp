package cn.pioneeruniverse.project.vo;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;


public class RequirementVo extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private	Long parentId;	//父ID
	private	String parentIds;	//所有父ID(如：,1,2,3,)
	/*基本信息*/
	@TableField(exist = false)
	private String reqStatusName;	//需求状态名称(数据字典)
	@TableField(exist = false)
	private String reqSourceName;	//需求来源名称(数据字典)
	@TableField(exist = false)
	private String reqTypeName;		//需求类型名称(数据字典)
	@TableField(exist = false)
    private String userName;	//需求提出人名称
	@TableField(exist = false)
    private String deptName;	//归属部门名称	
	
	@TableField(exist = false)
	private	String reqPlanName;	//需求计划名称(数据字典)
	@TableField(exist = false)
	private	String reqPriorityName;//需求优先级名称(数据字典)
	@TableField(exist = false)
	private	String devDeptName;	//开发部门名称

	/*重点需求相关*/
	@TableField(exist = false)
	private	String impReqStatusName;//是否重点需求(1=是，2=否)(数据字典)
	@TableField(exist = false)
	private	String impReqDelayStatusName;//重点需求是否延误(1=是，2=否)(数据字典)

	/*其他信息*/
	@TableField(exist = false)
	private	String hangupStatusName;	//需求是否挂起(1=是；2=否)(数据字典)
	@TableField(exist = false)
    private	String devManageUserName;	//开发管理人员(用户表主键)
	@TableField(exist = false)
	private	String reqManageUserName;	//需求管理人员(用户表主键)
	@TableField(exist = false)
	private	String reqAcceptanceUserName;//需求验收人员(用户表主键)
	@TableField(exist = false)
	private	String dataMigrationStatusName;//是否数据迁移(状态 1=是；2=否)(数据字典)
	
	@TableField(exist = false)
	private String systemId;//查询条件(涉及系统id)
	@TableField(exist = false)
	private String featureId;//查询条件(开发任务id)

	@TableField(exist = false)
	private String DevManageIds;//查询条件(开发岗Id)
	@TableField(exist = false)
	private String reqManageIds;//查询条件(需求纲id)
	@TableField(exist = false)
	private String funCount;//需求功能点
	@TableField(exist = false)
	private Long userId;	//当前登陆用户
	@TableField(exist = false)
	private List<String> requirementNames;//关联需求名称(code)
	@TableField(exist = false)
	private String systemIds;//系统ids
	@TableField(exist = false)
	private Long moduleId;//模块id
	@TableField(exist = false)
	private String systemNames;//系统名称
	@TableField(exist = false)
	private String moduleName;//模块名称
	@TableField(exist = false)
	private String parentCode;//父需求

	
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
	public List<String> getRequirementNames() {
		return requirementNames;
	}
	public void setRequirementNames(List<String> requirementNames) {
		this.requirementNames = requirementNames;
	}
	public String getSystemIds() {
		return systemIds;
	}
	public void setSystemIds(String systemIds) {
		this.systemIds = systemIds;
	}
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public String getSystemNames() {
		return systemNames;
	}
	public void setSystemNames(String systemNames) {
		this.systemNames = systemNames;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}