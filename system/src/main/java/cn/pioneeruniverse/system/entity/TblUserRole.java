package cn.pioneeruniverse.system.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
* @ClassName: TblUserRole
* @Description: 用户与角色关联bean
* @author author
* @date 2020年9月2日 下午4:21:48
*
 */
@TableName("tbl_user_role")
public class TblUserRole  extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7593470309355719831L;
	//用户ID
	private Long userId;
	//角色ID
	private Long roleId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
}
