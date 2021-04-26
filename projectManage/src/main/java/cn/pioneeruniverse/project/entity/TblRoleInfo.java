package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.List;
/**
 *
 * @ClassName: TblRoleInfo
 * @Description: 角色实体类
 * @author author
 *
 */

@TableName("tbl_role_info")
public class TblRoleInfo extends TreeData {

	private static final long serialVersionUID = 3636108394306466797L;

    private String roleName; //角色名称
    private String roleCode;//角色编号
    private Long projectId;//项目id
    private Integer userPost;//用户职位

	@TableField(exist = false)
	private String menuIds;// 菜单id

	@TableField(exist = false)
	private List<TblMenuButtonInfo> menuList;//角色所拥有的菜单LIST

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
}
