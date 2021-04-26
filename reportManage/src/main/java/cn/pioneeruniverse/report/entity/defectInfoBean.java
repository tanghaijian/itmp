package cn.pioneeruniverse.report.entity;

import java.io.Serializable;

public class defectInfoBean implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 日期
     **/
    private String date;

    /**
     * 新增数量
     **/
    private Integer theNewNumber;  //

    /**
     * 1,2数量
     **/
    private Integer seriousNumber; //

    /**
     * 3,4,5数量
     **/
    private Integer mildNumber; //

    public defectInfoBean() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTheNewNumber() {
        return theNewNumber;
    }

    public void setTheNewNumber(Integer theNewNumber) {
        this.theNewNumber = theNewNumber;
    }

    public Integer getSeriousNumber() {
        return seriousNumber;
    }

    public void setSeriousNumber(Integer seriousNumber) {
        this.seriousNumber = seriousNumber;
    }

    public Integer getMildNumber() {
        return mildNumber;
    }

    public void setMildNumber(Integer mildNumber) {
        this.mildNumber = mildNumber;
    }

    @Override
    public String toString() {
        return "defectInfoBean{" +
                "date='" + date + '\'' +
                ", theNewNumber=" + theNewNumber +
                ", seriousNumber=" + seriousNumber +
                ", mildNumber=" + mildNumber +
                '}';
    }
}
