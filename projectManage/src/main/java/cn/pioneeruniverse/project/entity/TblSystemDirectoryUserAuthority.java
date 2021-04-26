package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;


/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:系统目录用户权限bean
 * @Date: Created in 13:38 2019/12/5
 * @Modified By:
 */
@TableName("tbl_system_directory_user_authority")
public class TblSystemDirectoryUserAuthority extends BaseEntity{

    private static final long serialVersionUID = 775687946467262326L;

    private Long systemDirectoryId; //目录ID

    private Long userId; //用户ID

    private Integer readAuth; //读权限

    private Integer writeAuth;//写权限

    public Long getSystemDirectoryId() {
        return systemDirectoryId;
    }

    public void setSystemDirectoryId(Long systemDirectoryId) {
        this.systemDirectoryId = systemDirectoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
