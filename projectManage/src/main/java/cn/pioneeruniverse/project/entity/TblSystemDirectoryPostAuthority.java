package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 系统目录岗位权限bean
 * @Date: Created in 12:02 2019/12/5
 * @Modified By:
 */
@TableName("tbl_system_directory_post_authority")
public class TblSystemDirectoryPostAuthority extends BaseEntity{

    private static final long serialVersionUID = -5037204287417806196L;

    private Long systemDirectoryId; //系统目录ID
    private Integer userPost; //岗位
    private Integer readAuth;//读权限
    private Integer writeAuth;//写权限

    public Long getSystemDirectoryId() {
        return systemDirectoryId;
    }

    public void setSystemDirectoryId(Long systemDirectoryId) {
        this.systemDirectoryId = systemDirectoryId;
    }

    public Integer getUserPost() {
        return userPost;
    }

    public void setUserPost(Integer userPost) {
        this.userPost = userPost;
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
