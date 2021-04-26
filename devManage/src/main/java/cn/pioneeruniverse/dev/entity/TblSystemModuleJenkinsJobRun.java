package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
/**
 * @Author:weiji
 * @Description:系统模块JENKINS任务执行实体类
 */
@TableName("tbl_system_module_jenkins_job_run")
public class TblSystemModuleJenkinsJobRun extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long systemJenkinsJobRun;//TblSystemJenkinsJobRun的ID

	private String jobName;//任务名称

	private Long systemId;//系统ID
	private Long systemModuleId;//系统模块ID

	private Long systemScmId;//系统源码主键

	private Integer sonarBugs;//sonar扫描结果bug数
	private Integer sonarVulnerabilities;//sonar扫描结果VULNERABILITIES数
	private Integer sonarCodeSmells;//sonar扫描结果CODE SMELLS数
	private Double sonarCoverage;//sonar扫描覆盖率
	private Double sonarDuplications;//sonar扫描重复率
	private String sonarQualityGate;//sonar QUALITY GATE
    private Integer sonarUnitTestNumber;//单元测试数
	private Integer createType;//任务创建方式（1:系统自动，2:自定义）
	private Integer jobType;//系统构建状态（1=构建中；2=成功；3=失败;4=自动化测试中;5=测试未通过）

    public Integer getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(Integer buildStatus) {
        this.buildStatus = buildStatus;
    }

    private Integer buildStatus;

	@TableField(exist = false)
	private Integer bugsCount;	//记录bugs条数
	@TableField(exist = false)
	private Integer vulnerabilitiesCount;//记录Vulnerabilitiess条数
	@TableField(exist = false)
	private Integer smellsCount;//记录Smells条数
	@TableField(exist = false)
	private Double coverageCount;//记录Coverage条数
	@TableField(exist = false)
	private Double duplicationsCount;//记录Duplications条数
	

	public Integer getJobType() {
		return jobType;
	}

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSonarQualityGate() {
        return sonarQualityGate;
    }

    public void setSonarQualityGate(String sonarQualityGate) {
        this.sonarQualityGate = sonarQualityGate;
    }

    public Long getSystemJenkinsJobRun() {
        return systemJenkinsJobRun;
    }

    public void setSystemJenkinsJobRun(Long systemJenkinsJobRun) {
        this.systemJenkinsJobRun = systemJenkinsJobRun;
    }

    public Long getSystemModuleId() {
        return systemModuleId;
    }

    public void setSystemModuleId(Long systemModuleId) {
        this.systemModuleId = systemModuleId;
    }

    public Integer getSonarBugs() {
        return sonarBugs;
    }

    public void setSonarBugs(Integer sonarBugs) {
        this.sonarBugs = sonarBugs;
    }

    public Integer getSonarVulnerabilities() {
        return sonarVulnerabilities;
    }

    public void setSonarVulnerabilities(Integer sonarVulnerabilities) {
        this.sonarVulnerabilities = sonarVulnerabilities;
    }
    public Integer getSonarUnitTestNumber() {
        return sonarUnitTestNumber;
    }

    public void setSonarUnitTestNumber(Integer sonarUnitTestNumber) {
        this.sonarUnitTestNumber = sonarUnitTestNumber;
    }

    public Integer getSonarCodeSmells() {
        return sonarCodeSmells;
    }

    public void setSonarCodeSmells(Integer sonarCodeSmells) {
        this.sonarCodeSmells = sonarCodeSmells;
    }

    public Double getSonarCoverage() {
        return sonarCoverage;
    }

    public void setSonarCoverage(Double sonarCoverage) {
        this.sonarCoverage = sonarCoverage;
    }

    public Double getSonarDuplications() {
        return sonarDuplications;
    }

    public void setSonarDuplications(Double sonarDuplications) {
        this.sonarDuplications = sonarDuplications;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getSystemScmId() {
        return systemScmId;
    }

    public void setSystemScmId(Long systemScmId) {
        this.systemScmId = systemScmId;
    }

    public Integer getCreateType() {
        return createType;
    }

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	public Integer getBugsCount() {
		return bugsCount;
	}

	public void setBugsCount(Integer bugsCount) {
		this.bugsCount = bugsCount;
	}

	public Integer getVulnerabilitiesCount() {
		return vulnerabilitiesCount;
	}

	public void setVulnerabilitiesCount(Integer vulnerabilitiesCount) {
		this.vulnerabilitiesCount = vulnerabilitiesCount;
	}

	public Integer getSmellsCount() {
		return smellsCount;
	}

	public void setSmellsCount(Integer smellsCount) {
		this.smellsCount = smellsCount;
	}

	public Double getCoverageCount() {
		return coverageCount;
	}

	public void setCoverageCount(Double coverageCount) {
		this.coverageCount = coverageCount;
	}

	public Double getDuplicationsCount() {
		return duplicationsCount;
	}

	public void setDuplicationsCount(Double duplicationsCount) {
		this.duplicationsCount = duplicationsCount;
	}
	
	
}