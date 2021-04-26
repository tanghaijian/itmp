package cn.pioneeruniverse.dev.vo;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * Description: 同步缺陷
 * Author:liushan
 * Date: 2019/5/28 上午 9:47
 */
public class SynDefectInfo extends BaseEntity {

    private String problemNumber;   //  问题单号
    private String problemTitle;    //  问题标题
    private String detailedDesc;    //  问题描述
    private String problemType;     //  问题类型
    private String problemLevel;   //  问题等级
    private String systemCode;      //  系统编号
    private String reporterCode;    //  报告人员工号
    private Timestamp reportTime;      //  报告故障时间
    private Timestamp acknowledgeTime; //  确认时间
    private String line3Code;       //  3线修复人
    private String line4Code;       //  4线修复人
    private String problemUrl;      //  问题链接


    public String getProblemNumber() {
        return problemNumber;
    }
    public void setProblemNumber(String problemNumber) {
        this.problemNumber = problemNumber;
    }

    public String getProblemTitle() {
        return problemTitle;
    }
    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public String getDetailedDesc() {
        return detailedDesc;
    }
    public void setDetailedDesc(String detailedDesc) {
        this.detailedDesc = detailedDesc;
    }

    public String getProblemType() {
        return problemType;
    }
    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getProblemLevel() {
        return problemLevel;
    }
    public void setProblemLevel(String problemLevel) {
        this.problemLevel = problemLevel;
    }

    public String getSystemCode() {
        return systemCode;
    }
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getReporterCode() {
        return reporterCode;
    }
    public void setReporterCode(String reporterCode) {
        this.reporterCode = reporterCode;
    }

    public Timestamp getReportTime() {
        return reportTime;
    }
    public void setReportTime(Timestamp reportTime) {
        this.reportTime = reportTime;
    }

    public Timestamp getAcknowledgeTime() {
        return acknowledgeTime;
    }
    public void setAcknowledgeTime(Timestamp acknowledgeTime) {
        this.acknowledgeTime = acknowledgeTime;
    }

    public String getLine3Code() {
        return line3Code;
    }
    public void setLine3Code(String line3Code) {
        this.line3Code = line3Code;
    }

    public String getLine4Code() {
        return line4Code;
    }
    public void setLine4Code(String line4Code) {
        this.line4Code = line4Code;
    }

    public String getProblemUrl() {
        return problemUrl;
    }
    public void setProblemUrl(String problemUrl) {
        this.problemUrl = problemUrl;
    }
}
