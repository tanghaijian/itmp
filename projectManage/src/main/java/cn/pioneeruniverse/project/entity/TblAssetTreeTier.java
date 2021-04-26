package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *@author liushan
 *@Description  业务、系统层级实体类
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_asset_tree_tier")
public class TblAssetTreeTier extends BaseEntity {

    private Long assetTreeType; // 资产树类型（1:业务树，2:系统树）

    private String assetTreeName;// 资产树层名称

    private Long tierNumber;// 层号

    @TableField(exist = false)
    private String createName;// 创建人

    @TableField(exist = false)
    private String lastUpdateName;// 修改人

    public Long getAssetTreeType() {
        return assetTreeType;
    }

    public void setAssetTreeType(Long assetTreeType) {
        this.assetTreeType = assetTreeType;
    }

    public String getAssetTreeName() {
        return assetTreeName;
    }

    public void setAssetTreeName(String assetTreeName) {
        this.assetTreeName = assetTreeName == null ? null : assetTreeName.trim();
    }

    public Long getTierNumber() {
        return tierNumber;
    }

    public void setTierNumber(Long tierNumber) {
        this.tierNumber = tierNumber;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getLastUpdateName() {
        return lastUpdateName;
    }

    public void setLastUpdateName(String lastUpdateName) {
        this.lastUpdateName = lastUpdateName;
    }
}