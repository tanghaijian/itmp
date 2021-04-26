package cn.pioneeruniverse.project.vo;

import java.io.Serializable;
import java.util.List;
/**
 *
 * @ClassName:ProgramVO
 * @Description
 * @author author
 * @date 2020年8月26日
 *
 */
public class ProgramVO implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;
    private Long projectId;//项目id
    private String projectName;//项目名称
    private Integer developmentMode;//开发模式
    private Double projectCount;//项目工作量
    private List<SpringInfoVO> milestones;//工作量情况，预计工作量和实际工作量
    private Integer minus;//milestones对应的size-1,用于页面图表显示

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<SpringInfoVO> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<SpringInfoVO> milestones) {
        this.milestones = milestones;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getDevelopmentMode() {
        return developmentMode;
    }

    public void setDevelopmentMode(Integer developmentMode) {
        this.developmentMode = developmentMode;
    }

    public Double getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Double projectCount) {
        this.projectCount = projectCount;
    }

    public Integer getMinus() {
        return minus;
    }

    public void setMinus(Integer minus) {
        this.minus = minus;
    }

    @Override
    public String toString() {
        return "ProgramVO{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", developmentMode=" + developmentMode +
                ", projectCount=" + projectCount +
                ", milestones=" + milestones +
                ", minus=" + minus +
                '}';
    }
}
