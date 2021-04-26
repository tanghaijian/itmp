package cn.pioneeruniverse.system.vo.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import cn.pioneeruniverse.system.vo.BaseVo;
import cn.pioneeruniverse.system.vo.role.TblRoleInfo;

/**
 * 
* @ClassName: TblMenuButtonInfo
* @Description: 菜单按钮信息
* @author author
* @date 2020年9月4日 上午10:46:10
*
 */
public class TblMenuButtonInfo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6870185996775233451L;

	//类型，1菜单，2按钮
	private Integer menuButtonType;
	//显示名称
	private String menuButtonName;
	//父菜单按钮
	private Long parentId;
	//所有父菜单按钮ID
	private String parentIds;
	//样式
	private String css;
	//菜单按钮事件对应的url
	private String url;
	//顺序
	private Integer menuOrder;
	//子菜单
	private List<TblMenuButtonInfo> childMenu;
	//关联的角色信息
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
	
	/**
	 * 
	* @Title: getRoleAuthorities
	* @Description: 将菜单关联的角色信息组装成security需要的格式
	* @author author
	* @return Collection<ConfigAttribute>
	 */
	public Collection<ConfigAttribute> getRoleAuthorities() {
		Collection<ConfigAttribute> roleAuthorities = new ArrayList<ConfigAttribute>();
		ConfigAttribute configAttribute = null;
		for (TblRoleInfo role : this.ownRoles) {
			configAttribute = new SecurityConfig("ROLE_"+role.getRoleCode());
			roleAuthorities.add(configAttribute);
		}
		return roleAuthorities;
	}
	public Integer getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}
}
