package cn.pioneeruniverse.dev.dto;

import java.io.Serializable;
import java.util.Date;
/**
 *
 * @ClassName:TaskFeatuDTO
 * @Description
 * @author author
 * @date 2020年8月26日
 *
 */
public class TaskFeatuDTO implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;

    private Long id;    //主建
    private Date actualStartDate;   //开始时间
    private Date actualEndDate;     //结束时间
    private Integer testStag;       //测试阶段（数据字典，1:系测，2:版测）

    public TaskFeatuDTO() {
    }


    public TaskFeatuDTO(Long id, Date actualStartDate, Date actualEndDate, Integer testStag) {
        this.id = id;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.testStag = testStag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Integer getTestStag() {
        return testStag;
    }

    public void setTestStag(Integer testStag) {
        this.testStag = testStag;
    }

    @Override
    public String toString() {
        return "TaskFeatuDTO{" +
                "id=" + id +
                ", actualStartDate=" + actualStartDate +
                ", actualEndDate=" + actualEndDate +
                ", testStag=" + testStag +
                '}';
    }
}