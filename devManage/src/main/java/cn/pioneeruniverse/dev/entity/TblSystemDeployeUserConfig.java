package cn.pioneeruniverse.dev.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName: TblSystemDeployeUserConfig
 * @Description: 部署人员实体累
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
@TableName("tbl_system_deploye_user_config")
public class TblSystemDeployeUserConfig extends BaseEntity {


    private Long systemId;//系统id

    private Integer environmentType;//环境类型

    private String userIds;//用户id


    public String getEnvironmentTypeName() {
        return environmentTypeName;
    }

    public void setEnvironmentTypeName(String environmentTypeName) {
        this.environmentTypeName = environmentTypeName;
    }



    @TableField(exist = false)
    private String environmentTypeName;

    public List<TblUserInfo> getTblUserInfos() {
        return tblUserInfos;
    }

    public void setTblUserInfos(List<TblUserInfo> tblUserInfos) {
        this.tblUserInfos = tblUserInfos;
    }

    @TableField(exist = false)
    private List<TblUserInfo> tblUserInfos;


    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Integer getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(Integer environmentType) {
        this.environmentType = environmentType;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds == null ? null : userIds.trim();
    }


}