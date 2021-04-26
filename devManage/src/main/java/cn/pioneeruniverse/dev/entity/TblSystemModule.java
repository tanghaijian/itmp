package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
/**
 * @Author:weiji
 * @Description:子模块实体类
 */
@TableName("tbl_system_module")
public class TblSystemModule extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long systemId;//系统ID

	private String moduleName; // 微服务名称

	private String moduleCode; // 微服务编号

	private Integer buildDependency;//构建依赖（1:必须，2:非必须）
	
	private Integer buildDependencySequence;//构建顺序

	private String dependencySystemModuleIds;//依赖的系统模块
	
	private String groupId;//maven groupid

	private String artifactId;//maven artifactid
	
	private String compileCommand; //编译命令
	
	private Integer buildType;//构建工具，maven ant等
	
	private String snapshotRepositoryName; //快照仓库名

	private String releaseRepositoryName;//正式仓库名
	
	private String buildToolVersion;//构建工具版本
	
	private String packageSuffix; //包件后缀jar,war...
	
	private String jdkVersion;

	private String relativePath;//编译相对路径
	
	private Integer firstCompileFlag;//第一次编译标记（1:是，2:否）

	private String  systemModuleFlag;		//子系统标签
	public String getCompileCommand() {
		return compileCommand;
	}

	public void setCompileCommand(String compileCommand) {
		this.compileCommand = compileCommand;
	}

	public Integer getBuildDependency() {
		return buildDependency;
	}

	public void setBuildDependency(Integer buildDependency) {
		this.buildDependency = buildDependency;
	}

	public Integer getBuildDependencySequence() {
		return buildDependencySequence;
	}

	public void setBuildDependencySequence(Integer buildDependencySequence) {
		this.buildDependencySequence = buildDependencySequence;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getDependencySystemModuleIds() {
		return dependencySystemModuleIds;
	}

	public void setDependencySystemModuleIds(String dependencySystemModuleIds) {
		this.dependencySystemModuleIds = dependencySystemModuleIds;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Integer getBuildType() {
		return buildType;
	}

	public void setBuildType(Integer buildType) {
		this.buildType = buildType;
	}

	public String getSnapshotRepositoryName() {
		return snapshotRepositoryName;
	}

	public void setSnapshotRepositoryName(String snapshotRepositoryName) {
		this.snapshotRepositoryName = snapshotRepositoryName;
	}

	public String getReleaseRepositoryName() {
		return releaseRepositoryName;
	}

	public void setReleaseRepositoryName(String releaseRepositoryName) {
		this.releaseRepositoryName = releaseRepositoryName;
	}

	public String getBuildToolVersion() {
		return buildToolVersion;
	}

	public void setBuildToolVersion(String buildToolVersion) {
		this.buildToolVersion = buildToolVersion;
	}

	public String getPackageSuffix() {
		return packageSuffix;
	}

	public void setPackageSuffix(String packageSuffix) {
		this.packageSuffix = packageSuffix;
	}

	public String getJdkVersion() {
		return jdkVersion;
	}

	public void setJdkVersion(String jdkVersion) {
		this.jdkVersion = jdkVersion;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public Integer getFirstCompileFlag() {
		return firstCompileFlag;
	}

	public void setFirstCompileFlag(Integer firstCompileFlag) {
		this.firstCompileFlag = firstCompileFlag;
	}

	public String getSystemModuleFlag() {
		return systemModuleFlag;
	}
	public void setSystemModuleFlag(String systemModuleFlag) {
		this.systemModuleFlag = systemModuleFlag == null ? null : systemModuleFlag.trim();
	}
}