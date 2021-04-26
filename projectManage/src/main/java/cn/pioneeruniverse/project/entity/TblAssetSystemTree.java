package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.common.utils.ExcelField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 *@author liushan
 *@Description  系统树实体类
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_asset_system_tree")
public class TblAssetSystemTree extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long assetTreeTierId;// 资产树层级表ID

    private String systemTreeName;// 资产名称

    private String systemTreeShortName;// 资产简称

    private String systemTreeCode;// 资产编码

    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId; // 上级ID

    private String parentIds; // 上级ID（1，2，3）

    private String remark; // 说明

    private String systemTreeStatus; // 状态（1:有效，2:无效）

    @JsonSerialize(using = ToStringSerializer.class)
    private Long businessSystemId;// 关联关系id

    public Long getAssetTreeTierId() {
        return assetTreeTierId;
    }

    public void setAssetTreeTierId(Long assetTreeTierId) {
        this.assetTreeTierId = assetTreeTierId;
    }

    @ExcelField(title="关联系统条目名称", type=1, align=1, sort=4)
    public String getSystemTreeName() {
        return systemTreeName;
    }

    public void setSystemTreeName(String systemTreeName) {
        this.systemTreeName = systemTreeName == null ? null : systemTreeName.trim();
    }

    public String getSystemTreeShortName() {
        return systemTreeShortName;
    }

    public void setSystemTreeShortName(String systemTreeShortName) {
        this.systemTreeShortName = systemTreeShortName == null ? null : systemTreeShortName.trim();
    }

    public String getSystemTreeCode() {
        return systemTreeCode;
    }

    public void setSystemTreeCode(String systemTreeCode) {
        this.systemTreeCode = systemTreeCode == null ? null : systemTreeCode.trim();
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

    public String getSystemTreeStatus() {
        return systemTreeStatus;
    }

    public void setSystemTreeStatus(String systemTreeStatus) {
        this.systemTreeStatus = systemTreeStatus;
    }

    public Long getBusinessSystemId() {
        return businessSystemId;
    }

    public void setBusinessSystemId(Long businessSystemId) {
        this.businessSystemId = businessSystemId;
    }
}