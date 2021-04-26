package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblSystemDirectoryRole
* @Description: 系统目录角色bean
* @author author
* @date 2020年8月31日 下午1:02:05
*
 */
@TableName("tbl_system_directory_role")
public class TblSystemDirectoryRole extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long projectId;//项目ID

    private String roleName;//角色名

   

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    
}