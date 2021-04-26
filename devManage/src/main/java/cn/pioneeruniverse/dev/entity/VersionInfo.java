package cn.pioneeruniverse.dev.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *
 * @ClassName:VersionInfo
 * @Description:版本类，部署时需要选择的内容和自动化运维平台接口报文部分内容
 * @author author
 * @date 2020年8月19日
 *
 */
public class VersionInfo {
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale="zh",timezone="GMT+8")
	public Date releaseTime;		//计划发布时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale="zh",timezone="GMT+8")
	public Date recoveryTime;		//计划恢复时间
	public String versionNumber;	//版本唯一编码，系统编码+"_"+发布类型+"_"+日期+"_"+序号
	public int versionType;		//环境类型，0PRD-IN表示内外网发布,1PRD-OUT外网发布
	public int sqlType;			//sql类型，1-结构变更、2-数据处理
	public String versionFilePath;	//版本文件地址,ftp文件地址类似SystemCode + "/" + SystemCode + "_" + envName + "_"+ 时间戳
	public String versionManager;	//版本负责人
	public String departmant;		//所属处室

	public Date getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Date getRecoveryTime() {
		return recoveryTime;
	}
	public void setRecoveryTime(Date recoveryTime) {
		this.recoveryTime = recoveryTime;
	}

	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public int getVersionType() {
		return versionType;
	}
	public void setVersionType(int versionType) {
		this.versionType = versionType;
	}

	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	public String getVersionFilePath() {
		return versionFilePath;
	}
	public void setVersionFilePath(String versionFilePath) {
		this.versionFilePath = versionFilePath;
	}

	public String getVersionManager() {
		return versionManager;
	}
	public void setVersionManager(String versionManager) {
		this.versionManager = versionManager;
	}

	public String getDepartmant() {
		return departmant;
	}
	public void setDepartmant(String departmant) {
		this.departmant = departmant;
	}
}
