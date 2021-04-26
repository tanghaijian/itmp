package cn.pioneeruniverse.system.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_role_info")
public class TblRoleInfo  extends TreeData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3636108394306466797L;
	//角色名
    private String roleName;
  //角色编码
    private String roleCode;
  //项目ID
	private Long projectId;
	//人员岗位（项目组岗位权限中的岗位）
	private Integer userPost;
	//该角色下所拥有的菜单ID
    @TableField(exist = false)
    private String menuIds;
  //角色所拥有的菜单LIST
    @TableField(exist = false)
    private List<TblMenuButtonInfo> menuList;

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

	public List<TblMenuButtonInfo> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<TblMenuButtonInfo> menuList) {
		this.menuList = menuList;
	}

	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Integer getUserPost() {
		return userPost;
	}
	public void setUserPost(Integer userPost) {
		this.userPost = userPost;
	}
}
