package cn.pioneeruniverse.system.vo.role;

import cn.pioneeruniverse.system.vo.BaseVo;

/**
 * 
* @ClassName: TblRoleInfo
* @Description: 角色信息
* @author author
* @date 2020年9月4日 上午10:48:32
*
 */
public class TblRoleInfo  extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3636108394306466797L;

	//角色名
    private String roleName;
    //角色编码
    private String roleCode;
    //该角色下关联的菜单id
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
