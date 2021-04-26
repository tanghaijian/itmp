package cn.pioneeruniverse.system.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblRoleInfo  extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3636108394306466797L;

	//角色名称
    private String roleName;
    //角色编码
    private String roleCode;
    //角色关联的菜单ID
    private String menuIds;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

}
