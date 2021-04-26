package cn.pioneeruniverse.dev.vo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;

import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.common.utils.ExcelField;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureDeployStatusMapper;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureDeployStatus;

/**
 * 类说明
 * 
 * @author:tingting
 * @version:2018年12月5日 下午3:13:08
 */
public class TestTaskVo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String featureName;

	private String featureCode;

	private String featureOverview;

	private Long projectId;

	private Long systemId;

	private Integer createType;
	
	private String createTypeStr;

	private Long requirementId;

	private String requirementFeatureStatus;

	private String deployStatus;

	private String deployStatusName;

	private Integer manageUserId;

	private Integer executeUserId;

	private String temporaryStatus;// 临时任务 （1=是 2=否）

	private Integer deptId;

	private String handleSuggestion;

	private Date planStartDate;

	private Date planEndDate;

	private Double estimateWorkload;

	private Date actualStartDate;

	private Date actualEndDate;

	private Double actualWorkload;

	private Date planSitStartDate;

	private Date planSitEndDate;

	private Double estimateSitWorkload;

	private Date actualSitStartDate;

	private Date actualSitEndDate;

	private Double actualSitWorkload;

	private Date planPptStartDate;

	private Date planPptEndDate;

	private Double estimatePptWorkload;

	private Date actualPptStartDate;

	private Date actualPptEndDate;

	private Double actualPptWorkload;

	private Long commissioningWindowId;// 投产窗口（计划版本）

	private String statusName;

	private String systemName;

	private String requirementCode;

	private String deptName;

	private String manageUserName;

	private String executeUserName;

	private String windowName;

	private Integer allDefectAmount;

	private Integer allTestCaseAmount;

	private Integer defectNum;
	
	private String defectNumStr;

	private Integer testCaseNum;
	
	private String testCaseNumStr;

	private Integer requirementFeatureSource;
	
	private String requirementFeatureSourceStr;

	private Long uid;

	private List<Long> deploys;

	private String commissioningWindowIds;// 用来存放 以，分割的投产窗口id字符串
	private String manageUserIds;// 用来存放 以，分割的管理人id字符串
	private String requirementIds;// 用来存放 以，分割的需求id字符串
	private String systemIds;// 用来存放 以，分割的系统id字符串
	private String executeUserIds;// 用来存放 以，分割的执行人id字符串

	private List<Map<String, Object>> brothers = new ArrayList<>();

	private Integer featureSource;

	private Integer changeNumber;
	
	private String changeNumberStr;

	private String requirmentType;

	private Timestamp pptDeployTime;

	private Timestamp submitTestTime;

	private String sourceName;

	private String reqResources;
	
	private List<Long> tUserIds;
	
	private Boolean flag;
	
	private String sidx; //排序字段
	
	private String sord; //排序顺序
	
	private Integer deployStatu;//部署状态
	
	private List<TblRequirementFeatureDeployStatus> deployStatusList;  //部署状态集合

	public Integer getRequirementFeatureSource() {
		return requirementFeatureSource;
	}

	public void setRequirementFeatureSource(Integer requirementFeatureSource) {
		this.requirementFeatureSource = requirementFeatureSource;
	}

	public String getReqResources() {
		return reqResources;
	}

	public void setReqResources(String reqResources) {
		this.reqResources = reqResources;
	}

	public String getCommissioningWindowIds() {
		return commissioningWindowIds;
	}

	public void setCommissioningWindowIds(String commissioningWindowIds) {
		this.commissioningWindowIds = commissioningWindowIds;
	}

	public String getManageUserIds() {
		return manageUserIds;
	}

	public void setManageUserIds(String manageUserIds) {
		this.manageUserIds = manageUserIds;
	}

	public String getRequirementIds() {
		return requirementIds;
	}

	public void setRequirementIds(String requirementIds) {
		this.requirementIds = requirementIds;
	}

	public String getSystemIds() {
		return systemIds;
	}

	public void setSystemIds(String systemIds) {
		this.systemIds = systemIds;
	}

	public String getExecuteUserIds() {
		return executeUserIds;
	}

	public void setExecuteUserIds(String executeUserIds) {
		this.executeUserIds = executeUserIds;
	}

	@ExcelField(title = "案例数", type = 1, align = 1, sort = 130)
	public Integer getTestCaseNum() {
		return testCaseNum;
	}

	public void setTestCaseNum(Integer testCaseNum) {
		this.testCaseNum = testCaseNum;
	}

	@ExcelField(title = "部署状态",type =1,align = 1 ,sort = 100)
	public String getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(String deployStatus) {
		this.deployStatus = deployStatus;
	}

	public String getDeployStatusName() {
		return deployStatusName;
	}

	public void setDeployStatusName(String deployStatusName) {
		this.deployStatusName = deployStatusName;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	@ExcelField(title = "任务名称", type = 1, align = 1, sort = 20)
	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	@ExcelField(title = "任务编号", type = 1, align = 1, sort = 10)
	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public String getFeatureOverview() {
		return featureOverview;
	}

	public void setFeatureOverview(String featureOverview) {
		this.featureOverview = featureOverview;
	}

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

	public Long getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(Long requirementId) {
		this.requirementId = requirementId;
	}

	public String getRequirementFeatureStatus() {
		return requirementFeatureStatus;
	}

	public void setRequirementFeatureStatus(String requirementFeatureStatus) {
		this.requirementFeatureStatus = requirementFeatureStatus;
	}

	public Integer getManageUserId() {
		return manageUserId;
	}

	public void setManageUserId(Integer manageUserId) {
		this.manageUserId = manageUserId;
	}

	public Integer getExecuteUserId() {
		return executeUserId;
	}

	public void setExecuteUserId(Integer executeUserId) {
		this.executeUserId = executeUserId;
	}

	//@ExcelField(title = "临时任务", type = 1, align = 1, sort = 260)
	public String getTemporaryStatus() {
		if ("1".equals(temporaryStatus)) {
			temporaryStatus = "是";
		} else if ("2".equals(temporaryStatus)) {
			temporaryStatus = "否";
		}
		return temporaryStatus;
	}

	public void setTemporaryStatus(String temporaryStatus) {
		this.temporaryStatus = temporaryStatus;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	//@ExcelField(title = "处理意见", type = 1, align = 1, sort = 270)
	public String getHandleSuggestion() {
		return handleSuggestion;
	}

	public void setHandleSuggestion(String handleSuggestion) {
		this.handleSuggestion = handleSuggestion;
	}

	public Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public Double getEstimateWorkload() {
		return estimateWorkload;
	}

	public void setEstimateWorkload(Double estimateWorkload) {
		this.estimateWorkload = estimateWorkload;
	}

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Double getActualWorkload() {
		return actualWorkload;
	}

	public void setActualWorkload(Double actualWorkload) {
		this.actualWorkload = actualWorkload;
	}

	// @ExcelField(title="预计系测开始时间", type=1, align=1, sort=120)
	public Date getPlanSitStartDate() {
		return planSitStartDate;
	}

	public void setPlanSitStartDate(Date planSitStartDate) {
		this.planSitStartDate = planSitStartDate;
	}

	// @ExcelField(title="预计系测结束时间", type=1, align=1, sort=130)
	public Date getPlanSitEndDate() {
		return planSitEndDate;
	}

	public void setPlanSitEndDate(Date planSitEndDate) {
		this.planSitEndDate = planSitEndDate;
	}

	// @ExcelField(title="预计系测工作量", type=1, align=1, sort=140)
	public Double getEstimateSitWorkload() {
		return estimateSitWorkload;
	}

	public void setEstimateSitWorkload(Double estimateSitWorkload) {
		this.estimateSitWorkload = estimateSitWorkload;
	}

	//@ExcelField(title = "实际系测开始时间", type = 1, align = 1, sort = 150)
	public Date getActualSitStartDate() {
		return actualSitStartDate;
	}

	public void setActualSitStartDate(Date actualSitStartDate) {
		this.actualSitStartDate = actualSitStartDate;
	}

	//@ExcelField(title = "实际系测结束时间", type = 1, align = 1, sort = 160)
	public Date getActualSitEndDate() {
		return actualSitEndDate;
	}

	public void setActualSitEndDate(Date actualSitEndDate) {
		this.actualSitEndDate = actualSitEndDate;
	}

	//@ExcelField(title = "实际系测工作量", type = 1, align = 1, sort = 170)
	public Double getActualSitWorkload() {
		return actualSitWorkload;
	}

	public void setActualSitWorkload(Double actualSitWorkload) {
		this.actualSitWorkload = actualSitWorkload;
	}

	// @ExcelField(title="预计版测开始时间", type=1, align=1, sort=180)
	public Date getPlanPptStartDate() {
		return planPptStartDate;
	}

	public void setPlanPptStartDate(Date planPptStartDate) {
		this.planPptStartDate = planPptStartDate;
	}

	// @ExcelField(title="预计版测结束时间", type=1, align=1, sort=190)
	public Date getPlanPptEndDate() {
		return planPptEndDate;
	}

	public void setPlanPptEndDate(Date planPptEndDate) {
		this.planPptEndDate = planPptEndDate;
	}

	// @ExcelField(title="预计版测工作量", type=1, align=1, sort=200)
	public Double getEstimatePptWorkload() {
		return estimatePptWorkload;
	}

	public void setEstimatePptWorkload(Double estimatePptWorkload) {
		this.estimatePptWorkload = estimatePptWorkload;
	}

	//@ExcelField(title = "实际版测开始时间", type = 1, align = 1, sort = 210)
	public Date getActualPptStartDate() {
		return actualPptStartDate;
	}

	public void setActualPptStartDate(Date actualPptStartDate) {
		this.actualPptStartDate = actualPptStartDate;
	}

	//@ExcelField(title = "实际版测结束时间", type = 1, align = 1, sort = 220)
	public Date getActualPptEndDate() {
		return actualPptEndDate;
	}

	public void setActualPptEndDate(Date actualPptEndDate) {
		this.actualPptEndDate = actualPptEndDate;
	}

	//@ExcelField(title = "实际版测工作量", type = 1, align = 1, sort = 230)
	public Double getActualPptWorkload() {
		return actualPptWorkload;
	}

	public void setActualPptWorkload(Double actualPptWorkload) {
		this.actualPptWorkload = actualPptWorkload;
	}

	public Long getCommissioningWindowId() {
		return commissioningWindowId;
	}

	public void setCommissioningWindowId(Long commissioningWindowId) {
		this.commissioningWindowId = commissioningWindowId;
	}

	@ExcelField(title = "任务状态", type = 1, align = 1, sort = 30)
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@ExcelField(title = "涉及系统", type = 1, align = 1, sort = 40)
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@ExcelField(title = "关联需求", type = 1, align = 1, sort = 50)
	public String getRequirementCode() {
		return requirementCode;
	}

	public void setRequirementCode(String requirementCode) {
		this.requirementCode = requirementCode;
	}

	//@ExcelField(title = "所属处室", type = 1, align = 1, sort = 60)
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@ExcelField(title = "测试管理岗", type = 1, align = 1, sort = 70)
	public String getManageUserName() {
		return manageUserName;
	}

	public void setManageUserName(String manageUserName) {
		this.manageUserName = manageUserName;
	}

	@ExcelField(title = "执行人", type = 1, align = 1, sort = 80)
	public String getExecuteUserName() {
		return executeUserName;
	}

	public void setExecuteUserName(String executeUserName) {
		this.executeUserName = executeUserName;
	}

	//@ExcelField(title = "计划版本", type = 1, align = 1, sort = 90)
	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public List<Map<String, Object>> getBrothers() {
		return brothers;
	}

	public void setBrothers(List<Map<String, Object>> brothers) {
		this.brothers = brothers;
	}

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	@ExcelField(title = "缺陷数", type = 1, align = 1, sort = 140)
	public Integer getAllDefectAmount() {
		return allDefectAmount;
	}

	public void setAllDefectAmount(Integer allDefectAmount) {
		this.allDefectAmount = allDefectAmount;
	}

	public Integer getAllTestCaseAmount() {
		return allTestCaseAmount;
	}

	public void setAllTestCaseAmount(Integer allTestCaseAmount) {
		this.allTestCaseAmount = allTestCaseAmount;
	}

	public Integer getDefectNum() {
		return defectNum;
	}

	public void setDefectNum(Integer defectNum) {
		this.defectNum = defectNum;
	}

	public List<Long> getDeploys() {
		return deploys;
	}

	public void setDeploys(List<Long> deploys) {
		this.deploys = deploys;
	}

	public Integer getFeatureSource() {
		return featureSource;
	}

	public void setFeatureSource(Integer featureSource) {
		this.featureSource = featureSource;
	}

	@ExcelField(title = "需求变更次数", type = 1, align = 1, sort = 110)
	public Integer getChangeNumber() {
		return changeNumber;
	}

	public void setChangeNumber(Integer changeNumber) {
		this.changeNumber = changeNumber;
	}

	//@ExcelField(title = "是否监管需求", type = 1, align = 1, sort = 120)
	public String getRequirmentType() {
		return requirmentType;
	}

	public void setRequirmentType(String requirmentType) {
		this.requirmentType = requirmentType;
	}

	//@ExcelField(title = "发布版测时间", type = 1, align = 1, sort = 250)
	public Timestamp getPptDeployTime() {
		return pptDeployTime;
	}

	public void setPptDeployTime(Timestamp pptDeployTime) {
		this.pptDeployTime = pptDeployTime;
	}

	//@ExcelField(title = "提交测试时间", type = 1, align = 1, sort = 240)
	public Timestamp getSubmitTestTime() {
		return submitTestTime;
	}

	public void setSubmitTestTime(Timestamp submitTestTime) {
		this.submitTestTime = submitTestTime;
	}

	//@ExcelField(title = "任务类型", type = 1, align = 1, sort = 100)
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public List<Long> gettUserIds() {
		return tUserIds;
	}

	public void settUserIds(List<Long> tUserIds) {
		this.tUserIds = tUserIds;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public Integer getDeployStatu() {
		return deployStatu;
	}

	public void setDeployStatu(Integer deployStatu) {
		this.deployStatu = deployStatu;
	}

	@ExcelField(title = "任务类型", type = 1, align = 1, sort = 100)
	public String getRequirementFeatureSourceStr() {
		return requirementFeatureSourceStr;
	}

	public void setRequirementFeatureSourceStr(String requirementFeatureSourceStr) {
		this.requirementFeatureSourceStr = requirementFeatureSourceStr;
	}

	public String getDefectNumStr() {
		return defectNumStr;
	}

	public void setDefectNumStr(String defectNumStr) {
		this.defectNumStr = defectNumStr;
	}

	public String getTestCaseNumStr() {
		return testCaseNumStr;
	}

	public void setTestCaseNumStr(String testCaseNumStr) {
		this.testCaseNumStr = testCaseNumStr;
	}

	public String getChangeNumberStr() {
		return changeNumberStr;
	}

	public void setChangeNumberStr(String changeNumberStr) {
		this.changeNumberStr = changeNumberStr;
	}

	@ExcelField(title = "创建方式",type = 1,align = 1,sort = 100)
	public String getCreateTypeStr() {
		return createTypeStr;
	}

	public void setCreateTypeStr(String createTypeStr) {
		this.createTypeStr = createTypeStr;
	}

	public List<TblRequirementFeatureDeployStatus> getDeployStatusList() {
		return deployStatusList;
	}

	public void setDeployStatusList(List<TblRequirementFeatureDeployStatus> deployStatusList) {
		this.deployStatusList = deployStatusList;
	}

	
	
	
}
