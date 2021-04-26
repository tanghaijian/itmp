package cn.pioneeruniverse.dev.entity;

import java.io.Serializable;

/**
*@author liushan
*@Description 缺陷bean类
*@Date 2020/8/11
*@return 
**/
public class TestDefectInfoBean implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 日期
     **/
    private String date;   //日期

    /**
     * 新增数量
     **/
    private Integer theNewNumber;  //新增数量

    /**
     * 1,2数量
     **/
    private Integer seriousNumber; //1,2数量

    /**
     * 3,4,5数量
     **/
    private Integer mildNumber; //3,4,5数量

    public TestDefectInfoBean() {
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
