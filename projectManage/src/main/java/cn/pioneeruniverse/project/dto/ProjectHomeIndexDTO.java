package cn.pioneeruniverse.project.dto;

import java.io.Serializable;

public class ProjectHomeIndexDTO implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;

    /**
     * 菜单按钮id
     **/
    private Long menuButtonId;

    /**
     * 菜单按钮名称
     **/
    private String menuButtonName;

    /**
     * 地址
     **/
    private String url;

    public Long getMenuButtonId() {
        return menuButtonId;
    }

    public void setMenuButtonId(Long menuButtonId) {
        this.menuButtonId = menuButtonId;
    }

    public String getMenuButtonName() {
        return menuButtonName;
    }

    public void setMenuButtonName(String menuButtonName) {
        this.menuButtonName = menuButtonName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ProjectHomeIndexDTO{" +
                "menuButtonId=" + menuButtonId +
                ", menuButtonName='" + menuButtonName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
