package cn.pioneeruniverse.project.vo;

import java.io.Serializable;

public class SpringInfoVO implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;

    /**
     * 日期
     **/
    private String dataTime;    //日期

    /**
     * 实际工作量
     **/
    private Double estimateRemainWorkload;  //实际工作量

    /**
     * 预计工作量
     **/
    private Double estimateWorkload;        //预计工作量

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public Double getEstimateRemainWorkload() {
        return estimateRemainWorkload;
    }

    public void setEstimateRemainWorkload(Double estimateRemainWorkload) {
        this.estimateRemainWorkload = estimateRemainWorkload;
    }

    public Double getEstimateWorkload() {
        return estimateWorkload;
    }

    public void setEstimateWorkload(Double estimateWorkload) {
        this.estimateWorkload = estimateWorkload;
    }


    @Override
    public String toString() {
        return "SpringInfoVO{" +
                "dataTime='" + dataTime + '\'' +
                ", estimateRemainWorkload=" + estimateRemainWorkload +
                ", estimateWorkload=" + estimateWorkload +
                '}';
    }
}
