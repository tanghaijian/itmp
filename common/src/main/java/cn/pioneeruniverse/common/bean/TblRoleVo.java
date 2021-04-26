package cn.pioneeruniverse.common.bean;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * Description: 角色vo类
 * Author:liushan
 * Date: 2019/4/2 上午 9:54
 */
public class TblRoleVo extends BaseEntity {

    private String roleName; //角色名

    private String roleCode;//角色编码

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
}
