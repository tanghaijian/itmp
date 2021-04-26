package cn.pioneeruniverse.project.vo;


import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.project.entity.TblRequirementFeature;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;

import java.util.List;

/**
 * Description: 系统文件公共关联信息
 * Author:liushan
 * Date: 2020/1/14 下午 1:57
 */
public class SystemDirectoryRelateVo extends BaseEntity {

	//创建人姓名
    private String createUserName;

  //更新人姓名
    private String updateUserName;

  //检出人姓名
    private String checkoutUserName;

    /**
     * 关联的需求
     **/
    private List<TblRequirementInfo> relatedRequirementList;

    /**
     * 关联的开发任务
     **/
    private List<TblRequirementFeature> relatedRequirementFeatureList;

    /**
     * 关联的文档附件
     **/
    private List<TblSystemDirectoryDocumentAttachment> relatedSystemDirectoryDocumentAttachmentList;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getCheckoutUserName() {
        return checkoutUserName;
    }

    public void setCheckoutUserName(String checkoutUserName) {
        this.checkoutUserName = checkoutUserName;
    }

    public List<TblRequirementInfo> getRelatedRequirementList() {
        return relatedRequirementList;
    }

    public void setRelatedRequirementList(List<TblRequirementInfo> relatedRequirementList) {
        this.relatedRequirementList = relatedRequirementList;
    }

    public List<TblRequirementFeature> getRelatedRequirementFeatureList() {
        return relatedRequirementFeatureList;
    }

    public void setRelatedRequirementFeatureList(List<TblRequirementFeature> relatedRequirementFeatureList) {
        this.relatedRequirementFeatureList = relatedRequirementFeatureList;
    }

    public List<TblSystemDirectoryDocumentAttachment> getRelatedSystemDirectoryDocumentAttachmentList() {
        return relatedSystemDirectoryDocumentAttachmentList;
    }

    public void setRelatedSystemDirectoryDocumentAttachmentList(List<TblSystemDirectoryDocumentAttachment> relatedSystemDirectoryDocumentAttachmentList) {
        this.relatedSystemDirectoryDocumentAttachmentList = relatedSystemDirectoryDocumentAttachmentList;
    }
}
