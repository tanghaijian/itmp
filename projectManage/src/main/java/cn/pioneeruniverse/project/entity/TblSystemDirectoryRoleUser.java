package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblSystemDirectoryRoleUser
* @Description: 系统目录角色与人员关联bean
* @author author
* @date 2020年8月31日 下午1:04:43
*
 */
@TableName("tbl_system_directory_role_user")
public class TblSystemDirectoryRoleUser extends BaseEntity{
   

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long systemDirectoryRoleId;//系统目录角色ID

    private Long userId;//人员ID

    

    public Long getSystemDirectoryRoleId() {
        return systemDirectoryRoleId;
    }

    public void setSystemDirectoryRoleId(Long systemDirectoryRoleId) {
        this.systemDirectoryRoleId = systemDirectoryRoleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    
}