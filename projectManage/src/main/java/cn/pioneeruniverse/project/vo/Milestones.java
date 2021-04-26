package cn.pioneeruniverse.project.vo;

import java.io.Serializable;

//里程碑，项目计划用
public class Milestones implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;
  //里程碑名
    private String name;
  //日期
    private String date;
  //进度，完成百分比
    private Integer progress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Milestones{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", progress='" + progress + '\'' +
                '}';
    }
}
