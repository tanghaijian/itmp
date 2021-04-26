package cn.pioneeruniverse.project.vo;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.ExcelField;
import cn.pioneeruniverse.project.entity.TblAssetSystemTree;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * Description:系统/业务树vo类
 * Author:liushan
 * Date: 2019/5/16 上午 9:53
 */
public class BusinessSystemTreeVo extends TreeVo {

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
    private String businessSystemTreeName;

    /**
     *@author liushan
     *@Description 简称
     **/
    private String businessSystemTreeShortName;

    /**
     *@author liushan
     *@Description 资产编码
     **/
    private String businessSystemTreeCode;

    /**
    *@author liushan
    *@Description 状态（1:有效，2:无效）
    **/
    private Integer businessSystemTreeStatus;

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

    /**
     *@author liushan
     *@Description 层级
     *@return
     **/
    private Long assetTreeTierNumber;

    /**
     *@author liushan
     *@Description 最后更新人
     *@return
     **/
    private String lastUpdateName;

    /**
     *@author liushan
     *@Description 最后更新人
     *@return
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long businessSystemTreeId;

    /**
     *@author liushan
     *@Description 上一级
     *@return
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentFrontId;

    /**
     *@author liushan
     *@Description 上级条目名称
     *@return
     **/
    private String parentTreeName;

    /**
     *@author liushan
     *@Description 上级所有的
     *@return
     **/
    private String parentFrontIds;

    /**
     *@author liushan
     *@Description 上级条目名称
     *@return
     **/
    private String parentTreeNames;

    /**
     *@author liushan
     *@Description 资产树类型（1:业务树，2:系统树）
     *@return
     **/
    private Long assetTreeType;

    /**
     *@author liushan
     *@Description 业务关联的系统
     *@return
     **/
    private List<TblAssetSystemTree> systemTrees;
    private List<BusinessSystemTreeVo> businessSystemTrees;

    /**
     *@author liushan
     *@Description 控制树节点是否打开
     *@return
     **/
    private Boolean isParentNode = true;

    /**
     *@author liushan
     *@Description 控制树节点是否打开
     *@return
     **/
    private String open;

    public BusinessSystemTreeVo(){}

    public BusinessSystemTreeVo(HttpServletRequest request){
        Long currentUserId = CommonUtil.getCurrentUserId(request);
        long time = System.currentTimeMillis();
        this.setCreateBy(currentUserId);
        this.setLastUpdateBy(currentUserId);
        this.setCreateDate(new Timestamp(time));
        this.setLastUpdateDate(new Timestamp(time));
    }

    public BusinessSystemTreeVo(Long currentUserId){
        long time = System.currentTimeMillis();
        this.setCreateBy(currentUserId);
        this.setLastUpdateBy(currentUserId);
        this.setCreateDate(new Timestamp(time));
        this.setLastUpdateDate(new Timestamp(time));
    }

    public Long getAssetTreeTierId() {
        return assetTreeTierId;
    }

    public void setAssetTreeTierId(Long assetTreeTierId) {
        this.assetTreeTierId = assetTreeTierId;
    }

    @ExcelField(title="条目名称*", type=1, align=1)
    public String getBusinessSystemTreeName() {
        return businessSystemTreeName;
    }

    public void setBusinessSystemTreeName(String businessSystemTreeName) {
        this.businessSystemTreeName = businessSystemTreeName;
    }

    @ExcelField(title="条目简称", type=1, align=1, sort=1)
    public String getBusinessSystemTreeShortName() {
        return businessSystemTreeShortName;
    }

    public void setBusinessSystemTreeShortName(String businessSystemTreeShortName) {
        this.businessSystemTreeShortName = businessSystemTreeShortName;
    }

    @ExcelField(title="条目编码", type=1, align=1, sort=2)
    public String getBusinessSystemTreeCode() {
        return businessSystemTreeCode;
    }

    public void setBusinessSystemTreeCode(String businessSystemTreeCode) {
        this.businessSystemTreeCode = businessSystemTreeCode;
    }

    public Integer getBusinessSystemTreeStatus() {
        return businessSystemTreeStatus;
    }

    public void setBusinessSystemTreeStatus(Integer businessSystemTreeStatus) {
        this.businessSystemTreeStatus = businessSystemTreeStatus;
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

    @ExcelField(title="说明", type=1, align=1, sort=3)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getAssetTreeTierNumber() {
        return assetTreeTierNumber;
    }

    public void setAssetTreeTierNumber(Long assetTreeTierNumber) {
        this.assetTreeTierNumber = assetTreeTierNumber;
    }

    public String getLastUpdateName() {
        return lastUpdateName;
    }

    public void setLastUpdateName(String lastUpdateName) {
        this.lastUpdateName = lastUpdateName;
    }

    public String getParentTreeName() {
        return parentTreeName;
    }

    public void setParentTreeName(String parentTreeName) {
        this.parentTreeName = parentTreeName;
    }

    public Long getBusinessSystemTreeId() {
        return businessSystemTreeId;
    }

    public void setBusinessSystemTreeId(Long businessSystemTreeId) {
        this.businessSystemTreeId = businessSystemTreeId;
    }

    public Long getAssetTreeType() {
        return assetTreeType;
    }

    public void setAssetTreeType(Long assetTreeType) {
        this.assetTreeType = assetTreeType;
    }

    @ExcelField(title="关联系统条目名称", type=1, align=1, sort=4,isList = true)
    public List<TblAssetSystemTree> getSystemTrees() {
        return systemTrees;
    }

    public void setSystemTrees(List<TblAssetSystemTree> systemTrees) {
        this.systemTrees = systemTrees;
    }

    public Long getParentFrontId() {
        return parentFrontId;
    }

    public void setParentFrontId(Long parentFrontId) {
        this.parentFrontId = parentFrontId;
    }

    public String getParentFrontIds() {
        return parentFrontIds;
    }

    public void setParentFrontIds(String parentFrontIds) {
        this.parentFrontIds = parentFrontIds;
    }

    @ExcelField(title="上级条目名称", type=1, align=1,sort = -1,splitRegex="/")
    public String getParentTreeNames() {
        return parentTreeNames;
    }

    public void setParentTreeNames(String parentTreeNames) {
        this.parentTreeNames = parentTreeNames;
    }

    public List<BusinessSystemTreeVo> getBusinessSystemTrees() {
        return businessSystemTrees;
    }

    public void setBusinessSystemTrees(List<BusinessSystemTreeVo> businessSystemTrees) {
        this.businessSystemTrees = businessSystemTrees;
    }

    public Boolean getParentNode() {
        return isParentNode;
    }

    public void setParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        BusinessSystemTreeVo that = (BusinessSystemTreeVo) o;
        return Objects.equals(assetTreeTierId, that.assetTreeTierId) &&
                Objects.equals(businessSystemTreeName, that.businessSystemTreeName) &&
                Objects.equals(businessSystemTreeShortName, that.businessSystemTreeShortName) &&
                Objects.equals(businessSystemTreeCode, that.businessSystemTreeCode) &&
                Objects.equals(businessSystemTreeStatus, that.businessSystemTreeStatus) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(parentIds, that.parentIds) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(assetTreeTierNumber, that.assetTreeTierNumber) &&
                Objects.equals(lastUpdateName, that.lastUpdateName) &&
                Objects.equals(businessSystemTreeId, that.businessSystemTreeId) &&
                Objects.equals(parentFrontId, that.parentFrontId) &&
                Objects.equals(parentTreeName, that.parentTreeName) &&
                Objects.equals(parentFrontIds, that.parentFrontIds) &&
                Objects.equals(parentTreeNames, that.parentTreeNames) &&
                Objects.equals(assetTreeType, that.assetTreeType) &&
                Objects.equals(systemTrees, that.systemTrees) &&
                Objects.equals(businessSystemTrees, that.businessSystemTrees) &&
                Objects.equals(isParentNode, that.isParentNode) &&
                Objects.equals(open, that.open);
    }

    @Override
    public int hashCode() {

        return Objects.hash(assetTreeTierId, businessSystemTreeName, businessSystemTreeShortName, businessSystemTreeCode, businessSystemTreeStatus, parentId, parentIds, remark, assetTreeTierNumber, lastUpdateName, businessSystemTreeId, parentFrontId, parentTreeName, parentFrontIds, parentTreeNames, assetTreeType, systemTrees, businessSystemTrees, isParentNode, open);
    }
}
