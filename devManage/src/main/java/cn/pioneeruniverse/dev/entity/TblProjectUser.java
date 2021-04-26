package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *@deprecated
 * @ClassName: TblProjectUser
 * @Description: 项目用户关联
 * @author author
 *
 */
@TableName("tbl_project_user")
public class TblProjectUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long projectId;//项目id

    private Long userId;//用户ID

    private Integer userPost; //用户岗位

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUserPost() {
        return userPost;
    }

    public void setUserPost(Integer userPost) {
        this.userPost = userPost;
    }

}