package cn.pioneeruniverse.system.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 
* @ClassName: TblRoleMenuButton
* @Description: 角色和菜单关联关系
* @author author
* @date 2020年9月2日 下午4:16:28
*
 */
@TableName("tbl_role_menu_button")
public class TblRoleMenuButton extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3371077281191777872L;
	//角色ID
	@JsonSerialize(using = ToStringSerializer.class)
	private Long roleId;
	//菜单ID
	@JsonSerialize(using = ToStringSerializer.class)
	private Long menuButtonId;
	

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuButtonId() {
		return menuButtonId;
	}

	public void setMenuButtonId(Long menuButtonId) {
		this.menuButtonId = menuButtonId;
	}

}
