package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblSystemDirectoryRoleRelation
* @Description: 系统目录与角色关联bean
* @author author
* @date 2020年8月31日 下午1:03:58
*
 */
@TableName("tbl_system_directory_role_relation")
public class TblSystemDirectoryRoleRelation extends BaseEntity{
  

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long systemDirectoryRoleId;//系统目录角色ID

    private Long systemDirectoryId;//系统目录ID

    private Integer readAuth;//读权限

    private Integer writeAuth;//写权限

   

    public Long getSystemDirectoryRoleId() {
        return systemDirectoryRoleId;
    }

    public void setSystemDirectoryRoleId(Long systemDirectoryRoleId) {
        this.systemDirectoryRoleId = systemDirectoryRoleId;
    }

    public Long getSystemDirectoryId() {
        return systemDirectoryId;
    }

    public void setSystemDirectoryId(Long systemDirectoryId) {
        this.systemDirectoryId = systemDirectoryId;
    }

	public Integer getReadAuth() {
		return readAuth;
	}

	public void setReadAuth(Integer readAuth) {
		this.readAuth = readAuth;
	}

	public Integer getWriteAuth() {
		return writeAuth;
	}

	public void setWriteAuth(Integer writeAuth) {
		this.writeAuth = writeAuth;
	}

   

    
}