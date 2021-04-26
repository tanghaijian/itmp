package cn.pioneeruniverse.project.dto;

import java.io.Serializable;

/**
 * 变更和风险
 */
public class RiskInfoAndChangeInfoDTO implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;
    private Integer riskInfoCount;  //风险总数
    private Integer riskWeekAdd;    //本周新增(风险)
    private Integer outStandingNumber;  //未解决数风险)
    private Integer changeInfoCount;    //变更总数
    private Integer changeWeekAdd;  //本周新增(变更)
    private Integer confirmationNumber; //待确认数变更)

    public Integer getRiskInfoCount() {
        return riskInfoCount;
    }

    public void setRiskInfoCount(Integer riskInfoCount) {
        this.riskInfoCount = riskInfoCount;
    }

    public Integer getRiskWeekAdd() {
        return riskWeekAdd;
    }

    public void setRiskWeekAdd(Integer riskWeekAdd) {
        this.riskWeekAdd = riskWeekAdd;
    }

    public Integer getOutStandingNumber() {
        return outStandingNumber;
    }

    public void setOutStandingNumber(Integer outStandingNumber) {
        this.outStandingNumber = outStandingNumber;
    }

    public Integer getChangeInfoCount() {
        return changeInfoCount;
    }

    public void setChangeInfoCount(Integer changeInfoCount) {
        this.changeInfoCount = changeInfoCount;
    }

    public Integer getChangeWeekAdd() {
        return changeWeekAdd;
    }

    public void setChangeWeekAdd(Integer changeWeekAdd) {
        this.changeWeekAdd = changeWeekAdd;
    }

    public Integer getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(Integer confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    @Override
    public String toString() {
        return "RiskInfoAndChangeInfoDTO{" +
                "riskInfoCount=" + riskInfoCount +
                ", riskWeekAdd=" + riskWeekAdd +
                ", outStandingNumber=" + outStandingNumber +
                ", changeInfoCount=" + changeInfoCount +
                ", changeWeekAdd=" + changeWeekAdd +
                ", confirmationNumber=" + confirmationNumber +
                '}';
    }
}
