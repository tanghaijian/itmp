package cn.pioneeruniverse.dev.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;

import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.common.utils.ExcelField;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.bean.PropertyInfo;

@TableName("tbl_test_task")
public class TblTestTask extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @PropertyInfo(name = "\u4efb\u52a1\u540d\u79f0", length = 15)//任务名称
    private String testTaskName;

    private String testTaskCode;//任务编号
    
    @PropertyInfo(name = "\u4efb\u52a1\u63cf\u8ff0", length = 15)//任务描述
    private String testTaskOverview;
    @PropertyInfo(name = "\u5173\u8054\u5de5\u4f5c\u4efb\u52a1", length = 15)//关联任务ID
    private Long requirementFeatureId;
    @PropertyInfo(name = "\u72b6\u6001", length = 15)//任务状态
    private Integer testTaskStatus;
    private String workTaskStatus;
    @PropertyInfo(name = "\u6d4b\u8bd5\u9636\u6bb5", length = 15)//版测 系测
    private Integer testStage;
    @TableField(exist=false)
    private String testStageName;
    @PropertyInfo(name = "\u5206\u914d\u4eba\u5458", length = 15)//测试人员ID
    private Long testUserId;
    
    private Long taskAssignUserId;//任务分配人员
    @TableField(exist = false)
    @PropertyInfo(name = "\u4efb\u52a1\u5206\u914d", length = 15)
    private String taskAssignUserName; //任务分配人员名字
    
    @TableField(exist = false)
    private List<Integer> testStatusList;
    @TableField(exist = false)
    @PropertyInfo(name = "\u6d4b\u8bd5\u4eba\u5458", length = 15)//用户名称
    private String userName;
    
    private String createName;//创建人名称
    //需求code
    @TableField(exist = false)//需求编号
    private String requirementCode;


    @TableField(exist = false)
    private List<Integer> featureStatusList;
    @TableField(exist = false)
    private Integer featureStatus;//关联测试任务编号

    @PropertyInfo(name = "\u8ba1\u5212\u5f00\u59cb\u65f6\u95f4", length = 15)//计划开始时间
    private Date planStartDate;
    @PropertyInfo(name = "\u8ba1\u5212\u7ed3\u675f\u65f6\u95f4", length = 15)//计划结束时间
    private Date planEndDate;
    @PropertyInfo(name = "\u8ba1\u5212\u5de5\u4f5c\u91cf", length = 15)//计划工作量
    private Double planWorkload;
    @PropertyInfo(name = "\u5b9e\u9645\u5f00\u59cb\u65f6\u95f4", length = 15)//实际开始时间
    private Date actualStartDate;
    @PropertyInfo(name = "\u5b9e\u9645\u7ed3\u675f\u65f6\u95f4", length = 15)//实际结束时间
    private Date actualEndDate;
    @PropertyInfo(name = "\u5b9e\u9645\u5de5\u4f5c\u91cf", length = 15)//实际工作量
    private Double actualWorkload;
    
    @PropertyInfo(name = "\u8bbe\u8ba1\u6848\u4f8b\u6570", length = 15)
    private Integer designCaseNumber;//设计案例数
    
    @PropertyInfo(name = "\u6267\u884c\u6848\u4f8b\u6570", length = 15)
    private Integer executeCaseNumber;//执行案例数

    private Long commissioningWindowId;//窗口ID

    private Long parentId;//父ID

    private Long oldTestUserId;// 旧的操作人

    private String dependenceSystemIds;

    private String dependenceSystemModuleIds;
    @TableField(exist = false)
    private String systemName;//系统名称
    @TableField(exist = false)
    private Long systemId;//系统ID
    @TableField(exist = false)
    private String systemCode;//系统code
    
    @TableField(exist = false)
    private String featureCode;//关联测试任务code

    @TableField(exist = false)
    private String featureName;//关联测试任务名称

    /*@TableField(exist = false)
    private List<TblSystemInfo> systemInfoList;*/// 当前人的所属系统
    
    @TableField(exist = false)
    private Long caseNum;//案例数
    
    @TableField(exist = false)
    private Long defectNum;//缺陷数
    
    @Transient
    @PropertyInfo(name = "\u6295\u4ea7\u7a97\u53e3", length = 15)
    private String windowName;
    @Transient
    private String commissioningWindowIds;
    
    @Transient
	private String sidx; //排序字段
	
	@Transient
	private String sord; //排序顺序
	
	@Transient
	private List<Long> tUserIds;
	
	private Integer  environmentType;//环境类型
	 @TableField(exist = false)
	private String environmentTypes;//关联测试任务的所属系统的环境类型
    @TableField(exist = false)
    private int projectType;//项目类型
    @TableField(exist = false)
    private int projectId;//项目id
    /**
     * 测试集
     **/
	private List<Map<String, Object>> setList = new ArrayList<>();
	
    public String getEnvironmentTypes() {
		return environmentTypes;
	}

	public void setEnvironmentTypes(String environmentTypes) {
		this.environmentTypes = environmentTypes;
	}

	public Integer getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(Integer environmentType) {
		this.environmentType = environmentType;
	}

	//@ExcelField(title="关联测试任务名称", type=1, align=1, sort=7)
    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
    @ExcelField(title="涉及系统", type=1, align=1, sort=4)
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
    //@ExcelField(title="关联测试任务编号", type=1, align=1, sort=6)
    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }
    @ExcelField(title="任务名称", type=1, align=1, sort=2)
    public String getTestTaskName() {
        return testTaskName;
    }

    public void setTestTaskName(String testTaskName) {
        this.testTaskName = testTaskName == null ? null : testTaskName.trim();
    }
    @ExcelField(title="任务编号", type=1, align=1, sort=1)
    public String getTestTaskCode() {
        return testTaskCode;
    }

    public void setTestTaskCode(String testTaskCode) {
        this.testTaskCode = testTaskCode == null ? null : testTaskCode.trim();
    }

    public String getTestTaskOverview() {
        return testTaskOverview;
    }

    public void setTestTaskOverview(String testTaskOverview) {
        this.testTaskOverview = testTaskOverview == null ? null : testTaskOverview.trim();
    }

    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }

    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }
    
    public Integer getTestTaskStatus() {
        return testTaskStatus;
    }
    @ExcelField(title="任务状态", type=1, align=1, sort=3)
    public String getWorkTaskStatus() {
		return workTaskStatus;
	}

	public void setWorkTaskStatus(String workTaskStatus) {
		this.workTaskStatus = workTaskStatus;
	}

	public void setTestTaskStatus(Integer testTaskStatus) {
        this.testTaskStatus = testTaskStatus;
    }

    public Integer getTestStage() {
        return testStage;
    }

    public void setTestStage(Integer testStage) {
        this.testStage = testStage;
    }

    public Long getTestUserId() {
        return testUserId;
    }

    public void setTestUserId(Long testUserId) {
        this.testUserId = testUserId;
    }
//    @ExcelField(title="计划开始时间", type=1, align=1, sort=10)
    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }
//    @ExcelField(title="计划结束时间", type=1, align=1, sort=11)
    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }
//    @ExcelField(title="计划工作量", type=1, align=1, sort=12)
    public Double getPlanWorkload() {
        return planWorkload;
    }

    public void setPlanWorkload(Double planWorkload) {
        this.planWorkload = planWorkload;
    }
    //@ExcelField(title="实际开始时间", type=1, align=1, sort=14)
    public Date getActualStartDate() {
        return actualStartDate;
    }
    @ExcelField(title="测试人员", type=1, align=1, sort=8)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }
    //@ExcelField(title="实际结束时间", type=1, align=1, sort=15)
    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }
    //@ExcelField(title="实际工作量", type=1, align=1, sort=16)
    public Double getActualWorkload() {
        return actualWorkload;
    }

    public void setActualWorkload(Double actualWorkload) {
        this.actualWorkload = actualWorkload;
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

    public String getDependenceSystemIds() {
        return dependenceSystemIds;
    }

    public void setDependenceSystemIds(String dependenceSystemIds) {
        this.dependenceSystemIds = dependenceSystemIds == null ? null : dependenceSystemIds.trim();
    }

    public String getDependenceSystemModuleIds() {
        return dependenceSystemModuleIds;
    }

    public void setDependenceSystemModuleIds(String dependenceSystemModuleIds) {
        this.dependenceSystemModuleIds = dependenceSystemModuleIds == null ? null : dependenceSystemModuleIds.trim();
    }

    public List<Integer> getTestStatusList() {
        return testStatusList;
    }

    public void setTestStatusList(List<Integer> testStatusList) {
        this.testStatusList = testStatusList;
    }
    @ExcelField(title="关联需求", type=1, align=1, sort=5)
    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }

    public List<Integer> getFeatureStatusList() {
        return featureStatusList;
    }

    public void setFeatureStatusList(List<Integer> featureStatusList) {
        this.featureStatusList = featureStatusList;
    }

    public Integer getFeatureStatus() {
        return featureStatus;
    }

    public void setFeatureStatus(Integer featureStatus) {
        this.featureStatus = featureStatus;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }
    //@ExcelField(title="任务创建人", type=1, align=1, sort=9)
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

    public Long getOldTestUserId() {
        return oldTestUserId;
    }

    public void setOldTestUserId(Long oldTestUserId) {
        this.oldTestUserId = oldTestUserId;
    }

	public Long getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(Long caseNum) {
		this.caseNum = caseNum;
	}
	@ExcelField(title="缺陷数", type=1, align=1, sort=20)
	public Long getDefectNum() {
		return defectNum;
	}

	public void setDefectNum(Long defectNum) {
		this.defectNum = defectNum;
	}

	@ExcelField(title="投产窗口", type=1, align=1, sort=12)
	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	//@ExcelField(title="设计案例数", type=1, align=1, sort=17)
	public Integer getDesignCaseNumber() {
		return designCaseNumber;
	}

	public void setDesignCaseNumber(Integer designCaseNumber) {
		this.designCaseNumber = designCaseNumber;
	}

	@ExcelField(title="执行案例数", type=1, align=1, sort=18)
	public Integer getExecuteCaseNumber() {
		return executeCaseNumber;
	}

	public void setExecuteCaseNumber(Integer executeCaseNumber) {
		this.executeCaseNumber = executeCaseNumber;
	}

	public String getCommissioningWindowIds() {
		return commissioningWindowIds;
	}

	public void setCommissioningWindowIds(String commissioningWindowIds) {
		this.commissioningWindowIds = commissioningWindowIds;
	}

	public Long getTaskAssignUserId() {
		return taskAssignUserId;
	}

	public void setTaskAssignUserId(Long taskAssignUserId) {
		this.taskAssignUserId = taskAssignUserId;
	}

	@ExcelField(title="任务分配", type=1, align=1, sort=13)
	public String getTaskAssignUserName() {
		return taskAssignUserName;
	}

	public void setTaskAssignUserName(String taskAssignUserName) {
		this.taskAssignUserName = taskAssignUserName;
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

	public List<Long> gettUserIds() {
		return tUserIds;
	}

	public void settUserIds(List<Long> tUserIds) {
		this.tUserIds = tUserIds;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	@ExcelField(title="测试阶段", type=1, align=1, sort=9)
	public String getTestStageName() {
		return testStageName;
	}

	public void setTestStageName(String testStageName) {
		this.testStageName = testStageName;
	}

    public List<Map<String, Object>> getSetList() {
        return setList;
    }

    public void setSetList(List<Map<String, Object>> setList) {
        this.setList = setList;
    }

    /*public List<TblSystemInfo> getSystemInfoList() {
        return systemInfoList;
    }

    public void setSystemInfoList(List<TblSystemInfo> systemInfoList) {
        this.systemInfoList = systemInfoList;
    }*/

    public int getProjectType() {
        return projectType;
    }
    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }
}