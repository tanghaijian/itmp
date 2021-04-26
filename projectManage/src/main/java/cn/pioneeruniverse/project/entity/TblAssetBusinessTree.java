package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 *@author liushan
 *@Description  业务树实体类
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_asset_business_tree")
public class TblAssetBusinessTree extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long assetTreeTierId;// 资产业务树层级表ID

    private String businessTreeName; // 名称

    private String businessTreeShortName; // 简称

    private String businessTreeCode; // 资产编码

    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;// 上级

    private String parentIds; // 上级ID（1，2，3）

    private String remark;// 说明

    private Integer businessTreeStatus; // 状态（1:有效，2:无效）

    public Long getAssetTreeTierId() {
        return assetTreeTierId;
    }

    public void setAssetTreeTierId(Long assetTreeTierId) {
        this.assetTreeTierId = assetTreeTierId;
    }

    public String getBusinessTreeName() {
        return businessTreeName;
    }

    public void setBusinessTreeName(String businessTreeName) {
        this.businessTreeName = businessTreeName == null ? null : businessTreeName.trim();
    }

    public String getBusinessTreeShortName() {
        return businessTreeShortName;
    }

    public void setBusinessTreeShortName(String businessTreeShortName) {
        this.businessTreeShortName = businessTreeShortName == null ? null : businessTreeShortName.trim();
    }

    public String getBusinessTreeCode() {
        return businessTreeCode;
    }

    public void setBusinessTreeCode(String businessTreeCode) {
        this.businessTreeCode = businessTreeCode == null ? null : businessTreeCode.trim();
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
        this.parentIds = parentIds == null ? null : parentIds.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getBusinessTreeStatus() {
        return businessTreeStatus;
    }

    public void setBusinessTreeStatus(Integer businessTreeStatus) {
        this.businessTreeStatus = businessTreeStatus;
    }
}