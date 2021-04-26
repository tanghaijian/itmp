package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblReportMonthlyUndetectedSummary extends BaseEntity {


    private String itmsCode; //ITMS编号

    private String systemName; //项目名称
    
    private String seriousLevel; //严重性

    private String reportDate; //报告日期

    private String summary; //摘要

    private String requirementCode; //需求编号

    private String reasonAnalysis ;//原因分析

    private String missedReason ; //漏检原因

    private String experienceSummary;//测试经验总结


    public String getItmsCode() {
        return itmsCode;
    }

    public void setItmsCode(String itmsCode) {
        this.itmsCode = itmsCode;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSeriousLevel() {
        return seriousLevel;
    }

    public void setSeriousLevel(String seriousLevel) {
        this.seriousLevel = seriousLevel;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }

    public String getReasonAnalysis() {
        return reasonAnalysis;
    }

    public void setReasonAnalysis(String reasonAnalysis) {
        this.reasonAnalysis = reasonAnalysis;
    }

    public String getMissedReason() {
        return missedReason;
    }

    public void setMissedReason(String missedReason) {
        this.missedReason = missedReason;
    }

    public String getExperienceSummary() {
        return experienceSummary;
    }

    public void setExperienceSummary(String experienceSummary) {
        this.experienceSummary = experienceSummary;
    }
}
