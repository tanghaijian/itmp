package cn.pioneeruniverse.project.vo;

import java.util.Date;


public class SynRequirement{

	 /**
	  * IT全流程需求ID
	  **/
	 private Long reqId; 			//IT全流程需求ID

	/**
	 * 需求编号
	 **/
	 private String reqCode; 		//需求编号

	/**
	 * 需求名称
	 **/
	 private String reqName; 		//需求名称

	/**
	 * 连接到MangoDB上的ID(需求描述)
	 **/
	 private String reqDescription; //连接到MangoDB上的ID(需求描述)

	/**
	 * 需求来源
	 **/
	 private String reqResource; 	//需求来源

	/**
	 * 需求类型
	 **/
	 private String reqType; 		//需求类型

	/**
	 * 需求优先级
	 **/
	 private String reqPriority; 	//需求优先级

	/**
	 * 需求计划
	 **/
	 private String reqPlan; 		//需求计划

	/**
	 * 期望上线时间
	 **/
	 private Date reqExpectdate; 	//期望上线时间

	/**
	 * 计划上线时间
	 **/
	 private Date reqPlandate; 		//计划上线时间

	/**
	 * 实际上线时间
	 **/
	 private Date reqActdate; 		//实际上线时间

	/**
	 * 需求状态
	 **/
	 private String reqStatus; 		//需求状态

	/**
	 * 需求提出人代码
	 **/
	 private String createEmpcode; 	//需求提出人代码

	/**
	 * 需求提出人所属部门代码
	 **/
	 private String createDepcode; 	//需求提出人所属部门代码(忽略)

	/**
	 * 需求归属部门
	 **/
	 private String reqDepcode; 	//需求归属部门

	/**
	 * 需求归属开发处室：为开发管理岗归属处室
	 **/
	 private String reqDevsectioncode; 	//需求归属开发处室：为开发管理岗归属处室

	/**
	 * 父需求ID
	 **/
	 private Long parentReq; 			//父需求ID

	/**
	 * 直接收益指标：5分制
	 **/
	 private Long reqDirectincome; 		//直接收益指标：5分制

	/**
	 * 远期收益指标：5分制
	 **/
	 private Long reqForwardincome; 	//远期收益指标：5分制

	/**
	 * 隐性收益：5分制
	 **/
	 private Long reqRecessiveincome; 	//隐性收益：5分制

	/**
	 * 直接成本节约：5分制
	 **/
	 private Long reqDirectcost; 		//直接成本节约：5分制

	/**
	 * 远期成本节约：5分制
	 **/
	 private Long reqForwardcost; 		//远期成本节约：5分制

	/**
	 * 需求挂起时间
	 **/
	 private Date hangupDate; 			//需求挂起时间

	/**
	 * 1:挂起 0:正常
	 **/
	 private String reqHangup; 			//1:挂起 0:正常

	/**
	 * 1:需要0：不需要(忽略)
	 **/
	 private String isTechreview; 		//1:需要0：不需要(忽略)

	/**
	 * 开发管理岗人员
	 **/
	 private String reqDevmngcode; 		//开发管理岗人员

	/**
	 * 需求管理岗人员
	 **/
	 private String reqReqmngempcode; 	//需求管理岗人员

	/**
	 * 更新日期
	 **/
	 private Date udpateDate; 			//更新日期

	/**
	 * 需求创建日期
	 **/
	 private Date createDate; 			//需求创建日期

	/**
	 * 预计收益
	 **/
	 private String anticipated; 			//预计收益

	/**
	 * 	/**验收描述
	 **/
	 private String acceptanceDescription; 	//验收描述

	/**
	 * 需求变更次数
	 **/
	 private Long reqModifycount; 			//需求变更次数
	 private String isAcceptanceouttime; 	//1:是 2:否(忽略)
	 private String reqProperty; 			//需求性质（数据字典)
	 private String reqitacceptance; 		//业务验收人
	 private String itReqClassify; 			//IT需求分类（数据字典)
	 private Date openDate; 				//需求开启时间
	 private String itestimatethecost; 		//IT预估成本
	 private String isOlddata; 				//是否迁移数据：1是
	 private String keyReqType; 			//重点需求类型（数据字典)
	 private String keyReqPlanOnlineQuarter;//重点需求计划上线季度
	 private String keyReqDelayReason; 		//重点需求延误原因
	 private Double totalWorkload; 			//总开发工作量（临时任务不算）
	 private String reqAcceptance; 			//验收时效
	 private Date reqCoupletTime; 			//计划联调开始时间
	 private Date reqActCouupletTime; 		//实际联调开始时间
	 private String reqSubdivision; 		//需求细分类型（数据字典)
	 private String isKeyReq; 				//是否重点需求 1:是 0:否
	 private String isKeyReqDelay; 			//重点需求是否延期 1:是 0:否
	public Long getReqId() {
		return reqId;
	}
	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}
	public String getReqCode() {
		return reqCode;
	}
	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}
	public String getReqName() {
		return reqName;
	}
	public void setReqName(String reqName) {
		this.reqName = reqName;
	}
	public String getReqDescription() {
		return reqDescription;
	}
	public void setReqDescription(String reqDescription) {
		this.reqDescription = reqDescription;
	}
	public String getReqResource() {
		return reqResource;
	}
	public void setReqResource(String reqResource) {
		this.reqResource = reqResource;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getReqPriority() {
		return reqPriority;
	}
	public void setReqPriority(String reqPriority) {
		this.reqPriority = reqPriority;
	}
	public String getReqPlan() {
		return reqPlan;
	}
	public void setReqPlan(String reqPlan) {
		this.reqPlan = reqPlan;
	}
	public Date getReqExpectdate() {
		return reqExpectdate;
	}
	public void setReqExpectdate(Date reqExpectdate) {
		this.reqExpectdate = reqExpectdate;
	}
	public Date getReqPlandate() {
		return reqPlandate;
	}
	public void setReqPlandate(Date reqPlandate) {
		this.reqPlandate = reqPlandate;
	}
	public Date getReqActdate() {
		return reqActdate;
	}
	public void setReqActdate(Date reqActdate) {
		this.reqActdate = reqActdate;
	}
	public String getReqStatus() {
		return reqStatus;
	}
	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}
	public String getCreateEmpcode() {
		return createEmpcode;
	}
	public void setCreateEmpcode(String createEmpcode) {
		this.createEmpcode = createEmpcode;
	}
	public String getCreateDepcode() {
		return createDepcode;
	}
	public void setCreateDepcode(String createDepcode) {
		this.createDepcode = createDepcode;
	}
	public String getReqDepcode() {
		return reqDepcode;
	}
	public void setReqDepcode(String reqDepcode) {
		this.reqDepcode = reqDepcode;
	}
	public String getReqDevsectioncode() {
		return reqDevsectioncode;
	}
	public void setReqDevsectioncode(String reqDevsectioncode) {
		this.reqDevsectioncode = reqDevsectioncode;
	}
	public Long getParentReq() {
		return parentReq;
	}
	public void setParentReq(Long parentReq) {
		this.parentReq = parentReq;
	}
	public Long getReqDirectincome() {
		return reqDirectincome;
	}
	public void setReqDirectincome(Long reqDirectincome) {
		this.reqDirectincome = reqDirectincome;
	}
	public Long getReqForwardincome() {
		return reqForwardincome;
	}
	public void setReqForwardincome(Long reqForwardincome) {
		this.reqForwardincome = reqForwardincome;
	}
	public Long getReqRecessiveincome() {
		return reqRecessiveincome;
	}
	public void setReqRecessiveincome(Long reqRecessiveincome) {
		this.reqRecessiveincome = reqRecessiveincome;
	}
	public Long getReqDirectcost() {
		return reqDirectcost;
	}
	public void setReqDirectcost(Long reqDirectcost) {
		this.reqDirectcost = reqDirectcost;
	}
	public Long getReqForwardcost() {
		return reqForwardcost;
	}
	public void setReqForwardcost(Long reqForwardcost) {
		this.reqForwardcost = reqForwardcost;
	}
	public Date getHangupDate() {
		return hangupDate;
	}
	public void setHangupDate(Date hangupDate) {
		this.hangupDate = hangupDate;
	}
	public String getReqHangup() {
		return reqHangup;
	}
	public void setReqHangup(String reqHangup) {
		this.reqHangup = reqHangup;
	}
	public String getIsTechreview() {
		return isTechreview;
	}
	public void setIsTechreview(String isTechreview) {
		this.isTechreview = isTechreview;
	}
	public String getReqDevmngcode() {
		return reqDevmngcode;
	}
	public void setReqDevmngcode(String reqDevmngcode) {
		this.reqDevmngcode = reqDevmngcode;
	}
	public String getReqReqmngempcode() {
		return reqReqmngempcode;
	}
	public void setReqReqmngempcode(String reqReqmngempcode) {
		this.reqReqmngempcode = reqReqmngempcode;
	}
	public Date getUdpateDate() {
		return udpateDate;
	}
	public void setUdpateDate(Date udpateDate) {
		this.udpateDate = udpateDate;
	}	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAnticipated() {
		return anticipated;
	}
	public void setAnticipated(String anticipated) {
		this.anticipated = anticipated;
	}
	public String getAcceptanceDescription() {
		return acceptanceDescription;
	}
	public void setAcceptanceDescription(String acceptanceDescription) {
		this.acceptanceDescription = acceptanceDescription;
	}
	public Long getReqModifycount() {
		return reqModifycount;
	}
	public void setReqModifycount(Long reqModifycount) {
		this.reqModifycount = reqModifycount;
	}
	public String getIsAcceptanceouttime() {
		return isAcceptanceouttime;
	}
	public void setIsAcceptanceouttime(String isAcceptanceouttime) {
		this.isAcceptanceouttime = isAcceptanceouttime;
	}
	public String getReqProperty() {
		return reqProperty;
	}
	public void setReqProperty(String reqProperty) {
		this.reqProperty = reqProperty;
	}
	public String getReqitacceptance() {
		return reqitacceptance;
	}
	public void setReqitacceptance(String reqitacceptance) {
		this.reqitacceptance = reqitacceptance;
	}
	public String getItReqClassify() {
		return itReqClassify;
	}
	public void setItReqClassify(String itReqClassify) {
		this.itReqClassify = itReqClassify;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	public String getItestimatethecost() {
		return itestimatethecost;
	}
	public void setItestimatethecost(String itestimatethecost) {
		this.itestimatethecost = itestimatethecost;
	}
	public String getIsOlddata() {
		return isOlddata;
	}
	public void setIsOlddata(String isOlddata) {
		this.isOlddata = isOlddata;
	}
	public String getKeyReqType() {
		return keyReqType;
	}
	public void setKeyReqType(String keyReqType) {
		this.keyReqType = keyReqType;
	}
	public String getKeyReqPlanOnlineQuarter() {
		return keyReqPlanOnlineQuarter;
	}
	public void setKeyReqPlanOnlineQuarter(String keyReqPlanOnlineQuarter) {
		this.keyReqPlanOnlineQuarter = keyReqPlanOnlineQuarter;
	}
	public String getKeyReqDelayReason() {
		return keyReqDelayReason;
	}
	public void setKeyReqDelayReason(String keyReqDelayReason) {
		this.keyReqDelayReason = keyReqDelayReason;
	}
	public Double getTotalWorkload() {
		return totalWorkload;
	}
	public void setTotalWorkload(Double totalWorkload) {
		this.totalWorkload = totalWorkload;
	}
	public String getReqAcceptance() {
		return reqAcceptance;
	}
	public void setReqAcceptance(String reqAcceptance) {
		this.reqAcceptance = reqAcceptance;
	}
	public Date getReqCoupletTime() {
		return reqCoupletTime;
	}
	public void setReqCoupletTime(Date reqCoupletTime) {
		this.reqCoupletTime = reqCoupletTime;
	}
	public Date getReqActCouupletTime() {
		return reqActCouupletTime;
	}
	public void setReqActCouupletTime(Date reqActCouupletTime) {
		this.reqActCouupletTime = reqActCouupletTime;
	}
	public String getReqSubdivision() {
		return reqSubdivision;
	}
	public void setReqSubdivision(String reqSubdivision) {
		this.reqSubdivision = reqSubdivision;
	}
	public String getIsKeyReq() {
		return isKeyReq;
	}
	public void setIsKeyReq(String isKeyReq) {
		this.isKeyReq = isKeyReq;
	}
	public String getIsKeyReqDelay() {
		return isKeyReqDelay;
	}
	public void setIsKeyReqDelay(String isKeyReqDelay) {
		this.isKeyReqDelay = isKeyReqDelay;
	}
	
	
	
}