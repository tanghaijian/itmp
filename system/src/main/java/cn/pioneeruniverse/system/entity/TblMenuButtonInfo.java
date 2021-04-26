package cn.pioneeruniverse.system.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@TableName("tbl_menu_button_info")
public class TblMenuButtonInfo extends TreeData {

    /**
     *
     */
    private static final long serialVersionUID = -6870185996775233451L;
  //按钮类型1菜单，2按钮
    private Integer menuButtonType; 
  //名称
    private String menuButtonName;
  //编码
    private String menuButtonCode;
  //父菜单ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
  //所有父菜单ID
    private String parentIds;
  //菜单css样式
    private String css; 
  //菜单对应的url
    private String url;
  //顺序
    private Integer menuOrder;
  //有效状态 1=有效；2=无效
    private Integer validStatus;
  //是否开放(1:是，2:否)
    private Integer openStatus;
  //子菜单
    @TableField(exist = false)
    private List<TblMenuButtonInfo> childMenu;
  //拥有该菜单的所有权限
    @TableField(exist = false)
    private List<TblRoleInfo> ownRoles;

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

	public Integer getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(Integer openStatus) {
		this.openStatus = openStatus;
	}
}
