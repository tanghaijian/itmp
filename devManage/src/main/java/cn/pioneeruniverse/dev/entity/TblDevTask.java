package cn.pioneeruniverse.dev.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cn.pioneeruniverse.common.constants.DicConstants;
import org.springframework.data.annotation.Transient;

import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.common.utils.ExcelField;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_dev_task")
public class TblDevTask extends BaseEntity {

    //工作任务名称
    @PropertyInfo(name = "\u5de5\u4f5c\u6458\u8981", length = 15)
    private String devTaskName;
    //缺陷表ID
    private Long defectID;
    //工作任务CODE
    private String devTaskCode;

    // 任务类型（数据字典）
    @PropertyInfo(name = "\u4efb\u52a1\u7c7b\u578b",dicTermCode = DicConstants.DEV_TASK_DEV_TASK_TYPE)
    private Integer devTaskType;

    @TableField(exist = false)
    private String fieldJson;

    // @PropertyInfo(name="扩展字段")
    private String fieldTemplate;

    //描述
    @PropertyInfo(name = "\u4efb\u52a1\u63cf\u8ff0", length = 15)
    private String devTaskOverview;
    //需求特性表id
    @PropertyInfo(name = "\u5173\u8054\u5de5\u4f5c\u4efb\u52a1", length = 15)
    private Long requirementFeatureId;
    //任务状态
    @PropertyInfo(name = "\u72b6\u6001", length = 15)
    private Integer devTaskStatus;
    @TableField(exist = false)
    private String workTaskStatus;
    //任务状态集合
    @TableField(exist = false)
    private List<Integer> devStatus;
    //关联任务状态
    @TableField(exist = false)
    private List<Integer> FeatureStatus;
    //系统名称
    @TableField(exist = false)
    private String systemName;
    //任务状态名称
    @TableField(exist = false)
    private String statusName;
    //需求Id
    @TableField(exist = false)
    private Long requirementId;
    //需求CODE
    @TableField(exist = false)
    private String requirementCode;


    public String getSystemIds() {
        return systemIds;
    }

    public void setSystemIds(String systemIds) {
        this.systemIds = systemIds;
    }

    @TableField(exist = false)
    private String systemIds;


    @TableField(exist = false)
    private String requirementNewName;

    public String getRequirementNewName() {
        return requirementNewName;
    }

    public void setRequirementNewName(String requirementNewName) {
        this.requirementNewName = requirementNewName;
    }

    @TableField(exist = false)
    private String requirementNewCode;

    public String getRequirementNewCode() {
        return requirementNewCode;
    }

    public void setRequirementNewCode(String requirementNewCode) {
        this.requirementNewCode = requirementNewCode;
    }



    @TableField(exist = false)
    private String userNewName;
    //关联开发任务CODE
    @TableField(exist = false)
    private String featureCode;
    //关联开发任务名称
    @TableField(exist = false)
    private String featureName;
    //开发人员名字
    @TableField(exist = false)
    @PropertyInfo(name = "\u5f00\u53d1\u4eba\u5458\u59d3\u540d", length = 15)
    private String userName;
    //开发人员ID
    @PropertyInfo(name = "\u5f00\u53d1\u4eba\u5458", length = 15)
    private Long devUserId;
    @TableField(exist = false)
    private Long userId;
    //计划开始日期
    @PropertyInfo(name = "\u8ba1\u5212\u5f00\u59cb\u65f6\u95f4", length = 15)
    private Date planStartDate;
    //计划结束日期
    @PropertyInfo(name = "\u8ba1\u5212\u7ed3\u675f\u65f6\u95f4", length = 15)
    private Date planEndDate;
    //预估工作量（人天）
    @PropertyInfo(name = "\u8ba1\u5212\u5de5\u4f5c\u91cf", length = 15)
    private Double planWorkload;
    //预估剩余工作量（人天）
    @PropertyInfo(name = "\u8ba1\u5212\u5de5\u4f5c\u91cf", length = 15)
    private Double estimateRemainWorkload;
    //实际开始日期
    @PropertyInfo(name = "\u5b9e\u9645\u5f00\u59cb\u65f6\u95f4", length = 15)
    private Date actualStartDate;
    //实际结束日期
    @PropertyInfo(name = "\u5b9e\u9645\u7ed3\u675f\u65f6\u95f4", length = 15)
    private Date actualEndDate;
    //实际工作量
    @PropertyInfo(name = "\u5b9e\u9645\u5de5\u4f5c\u91cf", length = 15)
    private Double actualWorkload;
    //投产窗口表主键
    private Long commissioningWindowId;

    //冲刺表主键
    private Long sprintId;

    @TableField(exist = false)
    private String sprintName;
    @TableField(exist = false)
    private String sprintNameExcel;


    @TableField(exist = false)
    private Long executeProjectGroupId;

    //系统的开发模式（1:敏态，2:稳态）
    @TableField(exist = false)
    private Integer developmentMode;
    //工作任务关联开发任务关联的系统id
    @TableField(exist = false)
    private Long systemId;

    private Long projectId;
    //父ID
    private Long parentId;

    // 系统的代码是否代码评审（1:是，2:否）
    private Integer codeReviewStatus;
    //开发任务状态
    @TableField(exist = false)
    private Integer requirementFeatureStatus;
    //开发任务来源
    @TableField(exist = false)
    private Integer requirementFeatureSource;

    //关联的系统数
    @TableField(exist = false)
    private Integer systemCount;
    //创建人
    @TableField(exist = false)
    private String createName;
    //用户账号
    @TableField(exist = false)
    private String userAccount;
    //用户svn账号
    @TableField(exist = false)
    private String userScmAccount;


    public String getDevTaskPrioritys() {
        return devTaskPrioritys;
    }

    public void setDevTaskPrioritys(String devTaskPrioritys) {
        this.devTaskPrioritys = devTaskPrioritys;
    }

    @TableField(exist = false)
    private String devTaskPrioritys;


    //优先级
    @PropertyInfo(name = "\u4f18\u5148\u7ea7", length = 15)
    private Integer devTaskPriority;

    @TableField(exist = false)
    private String sprints; //所选冲刺id

    @TableField(exist = false)
    private Long manageUserId; //管理岗id

    @TableField(exist = false)
    private String sidx; //排序字段

    @TableField(exist = false)
    private String sord; //排序顺序

    @TableField(exist = false)
    private Integer needCodeReview; //是否需要代码评审
    @TableField(exist = false)
    private Timestamp createStartDate;
    @TableField(exist = false)
    private Timestamp createEndDate;
    public String getUserNewName() {
        return userNewName;
    }

    public void setUserNewName(String userNewName) {
        this.userNewName = userNewName;
    }

    //新增字段
    private Long devTaskId;//开发任务表主键
    private Integer scmType; //仓库类型（1=SVN；2=GIT）
    private Long tbtsID;   //工作任务代码关联表ID

    public Long getTbtsID() {
        return tbtsID;
    }

    public void setTbtsID(Long tbtsID) {
        this.tbtsID = tbtsID;
    }

    public Long getDevTaskId() {
        return devTaskId;
    }

    public void setDevTaskId(Long devTaskId) {
        this.devTaskId = devTaskId;
    }

    public Integer getScmType() {
        return scmType;
    }

    public void setScmType(Integer scmType) {
        this.scmType = scmType;
    }

    public String getFieldJson() {
        return fieldJson;
    }

    public void setFieldJson(String fieldJson) {
        this.fieldJson = fieldJson;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Integer getDevelopmentMode() {
        return developmentMode;
    }

    public void setDevelopmentMode(Integer developmentMode) {
        this.developmentMode = developmentMode;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public List<Integer> getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(List<Integer> devStatus) {
        this.devStatus = devStatus;
    }

    @ExcelField(title = "任务编号", type = 1, align = 1, sort = 1)
    public String getDevTaskCode() {
        return devTaskCode;
    }

    public void setDevTaskCode(String devTaskCode) {
        this.devTaskCode = devTaskCode;
    }

    public Integer getDevTaskType() {
        return devTaskType;
    }

    public void setDevTaskType(Integer devTaskType) {
        this.devTaskType = devTaskType;
    }

    @ExcelField(title = "任务名称", type = 1, align = 1, sort = 2)
    public String getDevTaskName() {
        return devTaskName;
    }

    public void setDevTaskName(String devTaskName) {
        this.devTaskName = devTaskName == null ? null : devTaskName.trim();
    }

    @ExcelField(title = "涉及系统", type = 1, align = 1, sort = 4)
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    @ExcelField(title = "关联需求", type = 1, align = 1, sort = 5)
    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }

    @ExcelField(title = "关联开发任务编号", type = 1, align = 1, sort = 6)
    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    @ExcelField(title = "关联开发任务名称", type = 1, align = 1, sort = 7)
    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    @ExcelField(title = "任务状态", type = 1, align = 1, sort = 3)
    public String getWorkTaskStatus() {
        return workTaskStatus;
    }

    public void setWorkTaskStatus(String workTaskStatus) {
        this.workTaskStatus = workTaskStatus;
    }

    public String getDevTaskOverview() {
        return devTaskOverview;
    }

    public void setDevTaskOverview(String devTaskOverview) {
        this.devTaskOverview = devTaskOverview == null ? null : devTaskOverview.trim();
    }


    public Integer getDevTaskStatus() {
        return devTaskStatus;
    }

    public void setDevTaskStatus(Integer devTaskStatus) {
        this.devTaskStatus = devTaskStatus;
    }

    @ExcelField(title = "任务创建人", type = 1, align = 1, sort = 9)
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @ExcelField(title = "计划开始时间", type = 1, align = 1, sort = 10)
    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    @ExcelField(title = "计划结束时间", type = 1, align = 1, sort = 11)
    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    @ExcelField(title = "计划工作量", type = 1, align = 1, sort = 12)
    public Double getPlanWorkload() {
        return planWorkload;
    }

    public void setPlanWorkload(Double planWorkload) {
        this.planWorkload = planWorkload;
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

    public Integer getRequirementFeatureStatus() {
        return requirementFeatureStatus;
    }

    public void setRequirementFeatureStatus(Integer requirementFeatureStatus) {
        this.requirementFeatureStatus = requirementFeatureStatus;
    }


    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }

    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }

    public Long getDevUserId() {
        return devUserId;
    }

    public void setDevUserId(Long devUserId) {
        this.devUserId = devUserId;
    }

    public Long getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(Long commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }


    @ExcelField(title = "开发人员", type = 1, align = 1, sort = 8)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Integer> getFeatureStatus() {
        return FeatureStatus;
    }

    public void setFeatureStatus(List<Integer> featureStatus) {
        FeatureStatus = featureStatus;
    }

    public Integer getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(Integer systemCount) {
        this.systemCount = systemCount;
    }

    public Long getDefectID() {
        return defectID;
    }

    public void setDefectID(Long defectID) {
        this.defectID = defectID;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getRequirementFeatureSource() {
        return requirementFeatureSource;
    }

    public void setRequirementFeatureSource(Integer requirementFeatureSource) {
        this.requirementFeatureSource = requirementFeatureSource;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public Integer getCodeReviewStatus() {
        return codeReviewStatus;
    }

    public void setCodeReviewStatus(Integer codeReviewStatus) {
        this.codeReviewStatus = codeReviewStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @ExcelField(title = "优先级", type = 1, align = 1, sort = 9)
    public Integer getDevTaskPriority() {
        return devTaskPriority;
    }

    public void setDevTaskPriority(Integer devTaskPriority) {
        this.devTaskPriority = devTaskPriority;
    }

    public String getUserScmAccount() {
        return userScmAccount;
    }

    public void setUserScmAccount(String userScmAccount) {
        this.userScmAccount = userScmAccount;
    }

    public String getSprints() {
        return sprints;
    }

    public void setSprints(String sprints) {
        this.sprints = sprints;
    }

    public Long getManageUserId() {
        return manageUserId;
    }

    public void setManageUserId(Long manageUserId) {
        this.manageUserId = manageUserId;
    }

    public Long getExecuteProjectGroupId() {
        return executeProjectGroupId;
    }

    public void setExecuteProjectGroupId(Long executeProjectGroupId) {
        this.executeProjectGroupId = executeProjectGroupId;
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

    public String getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(String fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

    public Integer getNeedCodeReview() {
        return needCodeReview;
    }

    public void setNeedCodeReview(Integer needCodeReview) {
        this.needCodeReview = needCodeReview;
    }


    private String defectCode;

    @ExcelField(title = "缺陷编号", type = 1, align = 1, sort = 12)
    public String getDefectCode() {
        return defectCode;
    }

    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode;
    }

    @ExcelField(title = "评审状态", type = 1, align = 1, sort = 12)
    public String getCodeReviewStatusName() {
        return codeReviewStatusName;
    }

    public void setCodeReviewStatusName(String codeReviewStatusName) {
        this.codeReviewStatusName = codeReviewStatusName;
    }

    @ExcelField(title = "投产窗口", type = 1, align = 1, sort = 12)
    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    @ExcelField(title = "父任务", type = 1, align = 1, sort = 12)
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @TableField(exist = false)
    private String codeReviewStatusName;
    @TableField(exist = false)
    private String windowName;
    @TableField(exist = false)
    private String parentName;

    @ExcelField(title = "实际开始时间", type = 1, align = 1, sort = 14)
    public Date getAuBeginDate() {
        return auBeginDate;
    }

    public void setAuBeginDate(Date auBeginDate) {
        this.auBeginDate = auBeginDate;
    }

    @ExcelField(title = "实际结束时间", type = 1, align = 1, sort = 15)
    public Date getAuEndDate() {
        return auEndDate;
    }

    public void setAuEndDate(Date auEndDate) {
        this.auEndDate = auEndDate;
    }

    @ExcelField(title = "实际工作量", type = 1, align = 1, sort = 16)
    public Double getAuWorkLoad() {
        return auWorkLoad;
    }

    public void setAuWorkLoad(Double auWorkLoad) {
        this.auWorkLoad = auWorkLoad;
    }

    private Date auBeginDate;
    private Date auEndDate;
    private Double auWorkLoad;

    @ExcelField(title = "开发工作任务描述", type = 1, align = 1, sort = 9)
    public String getDevTaskNote() {
        return devTaskNote;
    }

    public void setDevTaskNote(String devTaskNote) {
        this.devTaskNote = devTaskNote;
    }

    private String devTaskNote;


    public List<ExtendedField> getExtendedFields() {
        return extendedFields;
    }

    public void setExtendedFields(List<ExtendedField> extendedFields) {
        this.extendedFields = extendedFields;
    }

    @TableField(exist = false)
    private List<ExtendedField> extendedFields;

    @ExcelField(title = "预估剩余工作量", type = 1, align = 1, sort = 12)
    public Double getEstimateRemainWorkload() {
        return estimateRemainWorkload;
    }

    @ExcelField(title = "冲刺", type = 1, align = 1, sort = 17)
    public String getSprintNameExcel() {
        return sprintNameExcel;
    }

    public void setSprintNameExcel(String sprintNameExcel) {
        this.sprintNameExcel = sprintNameExcel;
    }

    public void setEstimateRemainWorkload(Double estimateRemainWorkload) {
        this.estimateRemainWorkload = estimateRemainWorkload;
    }

    //    private Date  actualStartDate;
//    private Date actualEndDate;
//    private Double actualWorkload;


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Timestamp getCreateStartDate() {
        return createStartDate;
    }

    public void setCreateStartDate(Timestamp createStartDate) {
        this.createStartDate = createStartDate;
    }

    public Timestamp getCreateEndDate() {
        return createEndDate;
    }

    public void setCreateEndDate(Timestamp createEndDate) {
        this.createEndDate = createEndDate;
    }

    @Override
    public String toString() {
        return "TblDevTask{" +
                "devTaskName='" + devTaskName + '\'' +
                ", defectID=" + defectID +
                ", devTaskCode='" + devTaskCode + '\'' +
                ", devTaskId=" + devTaskId +
                ", scmType=" + scmType +
                ", tbtsID=" + tbtsID +
                ", fieldJson='" + fieldJson + '\'' +
                ", fieldTemplate='" + fieldTemplate + '\'' +
                ", devTaskOverview='" + devTaskOverview + '\'' +
                ", requirementFeatureId=" + requirementFeatureId +
                ", devTaskStatus=" + devTaskStatus +
                ", workTaskStatus='" + workTaskStatus + '\'' +
                ", devStatus=" + devStatus +
                ", FeatureStatus=" + FeatureStatus +
                ", systemName='" + systemName + '\'' +
                ", statusName='" + statusName + '\'' +
                ", requirementId=" + requirementId +
                ", requirementCode='" + requirementCode + '\'' +
                ", featureCode='" + featureCode + '\'' +
                ", featureName='" + featureName + '\'' +
                ", userName='" + userName + '\'' +
                ", devUserId=" + devUserId +
                ", userId=" + userId +
                ", planStartDate=" + planStartDate +
                ", planEndDate=" + planEndDate +
                ", planWorkload=" + planWorkload +
                ", estimateRemainWorkload=" + estimateRemainWorkload +
                ", actualStartDate=" + actualStartDate +
                ", actualEndDate=" + actualEndDate +
                ", actualWorkload=" + actualWorkload +
                ", commissioningWindowId=" + commissioningWindowId +
                ", sprintId=" + sprintId +
                ", sprintName='" + sprintName + '\'' +
                ", sprintNameExcel='" + sprintNameExcel + '\'' +
                ", executeProjectGroupId=" + executeProjectGroupId +
                ", developmentMode=" + developmentMode +
                ", systemId=" + systemId +
                ", projectId=" + projectId +
                ", parentId=" + parentId +
                ", codeReviewStatus=" + codeReviewStatus +
                ", requirementFeatureStatus=" + requirementFeatureStatus +
                ", requirementFeatureSource=" + requirementFeatureSource +
                ", systemCount=" + systemCount +
                ", createName='" + createName + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", userScmAccount='" + userScmAccount + '\'' +
                ", devTaskPriority=" + devTaskPriority +
                ", sprints='" + sprints + '\'' +
                ", manageUserId=" + manageUserId +
                ", sidx='" + sidx + '\'' +
                ", sord='" + sord + '\'' +
                ", needCodeReview=" + needCodeReview +
                ", defectCode='" + defectCode + '\'' +
                ", codeReviewStatusName='" + codeReviewStatusName + '\'' +
                ", windowName='" + windowName + '\'' +
                ", parentName='" + parentName + '\'' +
                ", auBeginDate=" + auBeginDate +
                ", auEndDate=" + auEndDate +
                ", auWorkLoad=" + auWorkLoad +
                ", devTaskNote='" + devTaskNote + '\'' +
                ", extendedFields=" + extendedFields +
                '}';
    }
}