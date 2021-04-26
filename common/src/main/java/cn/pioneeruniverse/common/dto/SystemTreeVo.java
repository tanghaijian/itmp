package cn.pioneeruniverse.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Description:
 * Author:liushan
 * Date: 2019/5/16 上午 9:53
 */
public class SystemTreeVo{

    /**
    *@author liushan
    *@Description 资产树层级表ID
    **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long assetTreeTierId;

    /**
     *@author liushan
     *@Description 名称
     **/
    private String systemTreeName;

    /**
     *@author liushan
     *@Description 简称
     **/
    private String systemTreeShortName;

    /**
     *@author liushan
     *@Description 资产编码
     **/
    private String systemTreeCode;

    /**
    *@author liushan
    *@Description 状态（1:有效，2:无效）
    **/
    private Integer systemTreeStatus;

    /**
     *@author liushan
     *@Description 上级
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
    *@author liushan
    *@Description 上级ID（1，2，3）
    *@return
    **/
    private String parentIds;

    /**
     *@author liushan
     *@Description 说明
     *@return
     **/
    private String remark;

    public Long getAssetTreeTierId() {
        return assetTreeTierId;
    }

    public void setAssetTreeTierId(Long assetTreeTierId) {
        this.assetTreeTierId = assetTreeTierId;
    }

    public String getSystemTreeName() {
        return systemTreeName;
    }

    public void setSystemTreeName(String systemTreeName) {
        this.systemTreeName = systemTreeName;
    }

    public String getSystemTreeShortName() {
        return systemTreeShortName;
    }

    public void setSystemTreeShortName(String systemTreeShortName) {
        this.systemTreeShortName = systemTreeShortName;
    }

    public String getSystemTreeCode() {
        return systemTreeCode;
    }

    public void setSystemTreeCode(String systemTreeCode) {
        this.systemTreeCode = systemTreeCode;
    }

    public Integer getSystemTreeStatus() {
        return systemTreeStatus;
    }

    public void setSystemTreeStatus(Integer systemTreeStatus) {
        this.systemTreeStatus = systemTreeStatus;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
