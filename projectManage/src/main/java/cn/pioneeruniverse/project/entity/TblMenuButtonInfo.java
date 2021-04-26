package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@TableName("tbl_menu_button_info")
public class TblMenuButtonInfo extends TreeData {

    /**
     *
     */
    private static final long serialVersionUID = -6870185996775233451L;

    private Integer menuButtonType;//类型（1：菜单、2：按钮）
    private String menuButtonName;//菜单名
    private String menuButtonCode;//菜单编码

    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;//父菜单

    private String parentIds;//所有父菜单ID
    private String css;//显示样式
    private String url;//点击事件url
    private Integer menuOrder;//顺序
    private Integer validStatus;//是否有效
    @TableField(exist = false)
    private List<TblMenuButtonInfo> childMenu;//子菜单
    @TableField(exist = false)
    private List<TblRoleInfo> ownRoles;//拥有该菜单的所有权限

    public Integer getMenuButtonType() {
        return menuButtonType;
    }

    public void setMenuButtonType(Integer menuButtonType) {
        this.menuButtonType = menuButtonType;
    }

    public String getMenuButtonName() {
        return menuButtonName;
    }

    public void setMenuButtonName(String menuButtonName) {
        this.menuButtonName = menuButtonName;
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

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TblMenuButtonInfo> getChildMenu() {
        return childMenu;
    }

    public void setChildMenu(List<TblMenuButtonInfo> childMenu) {
        this.childMenu = childMenu;
    }

    public List<TblRoleInfo> getOwnRoles() {
        return ownRoles;
    }

    public void setOwnRoles(List<TblRoleInfo> ownRoles) {
        this.ownRoles = ownRoles;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public String getMenuButtonCode() {
        return menuButtonCode;
    }

    public void setMenuButtonCode(String menuButtonCode) {
        this.menuButtonCode = menuButtonCode;
    }
}
