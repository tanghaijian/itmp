package cn.pioneeruniverse.project.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class JqueryGanttVO implements Serializable {

    /**
     * id
     **/
    private Long id;                    //id

    /**
     * planCode项目计划编码
     **/
    private String code;                //planCode

    /**
     * planName项目计划名称
     **/
    private String name;                //planName

    /**
     * planSchedule 
     **/
    private int progress;               //planSchedule

    /**
     * 无对应
     **/
    private boolean progressByWorklog;      //无对应
    private int relevance;                  //无对应
    private String type;                    //无对应
    private String typeId;                  //无对应

    /**
     * deliverables
     **/
    private String description;         //deliverables
    private int level;                      //无对应
    private String status;                  //无对应
    private String depends;                 //无对应
    private boolean canWrite = true;               //无对应
    //计划开始日期
    private Timestamp start;            //planStartDate
    //计划工期
    private double duration;           //planDuration
    //计划结束日期
    private Timestamp end;              //planEndDate
    //开始里程碑
    private boolean startIsMilestone;   //planStartMilestone
    //结束里程碑
    private boolean endIsMilestone;     //planEndMilestone
    private boolean collapsed;              //无对应
    private String[] assigs = new String[0];                //无对应
    private boolean hasChild;               //无对应


    private Long userId;                 //人员id
    private String userName;             //人员名称
    private Integer startIsMilestone1;   //开始里程碑
    private Integer endIsMilestone1;     //结束里程碑
    private Long parentId;               //父ID
    private String parentIds;            //所有父ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isProgressByWorklog() {
        return progressByWorklog;
    }

    public void setProgressByWorklog(boolean progressByWorklog) {
        this.progressByWorklog = progressByWorklog;
    }

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepends() {
        return depends;
    }

    public void setDepends(String depends) {
        this.depends = depends;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public boolean isStartIsMilestone() {
        return startIsMilestone;
    }

    public void setStartIsMilestone(boolean startIsMilestone) {
        this.startIsMilestone = startIsMilestone;
    }

    public boolean isEndIsMilestone() {
        return endIsMilestone;
    }

    public void setEndIsMilestone(boolean endIsMilestone) {
        this.endIsMilestone = endIsMilestone;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public String[] getAssigs() {
        return assigs;
    }

    public void setAssigs(String[] assigs) {
        this.assigs = assigs;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStartIsMilestone1() {
        return startIsMilestone1;
    }

    public void setStartIsMilestone1(Integer startIsMilestone1) {
        this.startIsMilestone1 = startIsMilestone1;
    }

    public Integer getEndIsMilestone1() {
        return endIsMilestone1;
    }

    public void setEndIsMilestone1(Integer endIsMilestone1) {
        this.endIsMilestone1 = endIsMilestone1;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
}
