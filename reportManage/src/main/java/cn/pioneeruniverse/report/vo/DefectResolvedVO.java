package cn.pioneeruniverse.report.vo;

import java.io.Serializable;
import java.sql.Date;

public class DefectResolvedVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     **/
    private Long id;  //

    /**
     * 系统id
     **/
    private Long systemId;  //

    /**
     * 系统名称
     **/
    private String systemName; //系统名称

    /**
     * 创建日期
     **/
    private Date createDate;  //创建日期

    /**
     * 总数
     **/
    private Integer count;  //总数

    /**
     * 1-2
     **/
    private int oneClass; //1-2

    /**
     * 3-5
     **/
    private int twoClass; //3-5

    /**
     * 6-10
     **/
    private int threeClass; //6-10

    /**
     * 11-20
     **/
    private int fourClass; //11-20

    /**
     * 21以上
     **/
    private int fiveClass; //21以上


    public DefectResolvedVO() {
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getOneClass() {
        return oneClass;
    }

    public void setOneClass(int oneClass) {
        this.oneClass = oneClass;
    }

    public int getTwoClass() {
        return twoClass;
    }

    public void setTwoClass(int twoClass) {
        this.twoClass = twoClass;
    }

    public int getThreeClass() {
        return threeClass;
    }

    public void setThreeClass(int threeClass) {
        this.threeClass = threeClass;
    }

    public int getFourClass() {
        return fourClass;
    }

    public void setFourClass(int fourClass) {
        this.fourClass = fourClass;
    }

    public int getFiveClass() {
        return fiveClass;
    }

    public void setFiveClass(int fiveClass) {
        this.fiveClass = fiveClass;
    }

    @Override
    public String toString() {
        return "DefectResolvedVO{" +
                "id=" + id +
                ", systemId=" + systemId +
                ", systemName='" + systemName + '\'' +
                ", createDate=" + createDate +
                ", count=" + count +
                ", oneClass=" + oneClass +
                ", twoClass=" + twoClass +
                ", threeClass=" + threeClass +
                ", fourClass=" + fourClass +
                ", fiveClass=" + fiveClass +
                '}';
    }
}
