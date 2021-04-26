package cn.pioneeruniverse.project.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
/**
 *
 * @ClassName:TblSprintBurnout
 * @Description 冲击燃尽表类
 * @author author
 * @date 2020年8月25日
 *
 */
@TableName("tbl_sprint_burnout")
public class TblSprintBurnout {
    private Long id;

    /**
     * 预估剩余工作量（人天）
     **/
    private Double estimateRemainWorkload;//预估剩余工作量（人天）

    public Double getDevEstimateRemainWorkload() {
        return devEstimateRemainWorkload;
    }

    public void setDevEstimateRemainWorkload(Double devEstimateRemainWorkload) {
        this.devEstimateRemainWorkload = devEstimateRemainWorkload;
    }

    public Double getTestEstimateRemainWorkload() {
        return testEstimateRemainWorkload;
    }

    public void setTestEstimateRemainWorkload(Double testEstimateRemainWorkload) {
        this.testEstimateRemainWorkload = testEstimateRemainWorkload;
    }

    private Double devEstimateRemainWorkload;//开发预估剩余工作量（人天）
    private Double testEstimateRemainWorkload;//测试预估剩余工作量（人天)
    private Long sprintId;

    private Date createDate;//创建日期

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEstimateRemainWorkload() {
        return estimateRemainWorkload;
    }

    public void setEstimateRemainWorkload(Double estimateRemainWorkload) {
        this.estimateRemainWorkload = estimateRemainWorkload;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}