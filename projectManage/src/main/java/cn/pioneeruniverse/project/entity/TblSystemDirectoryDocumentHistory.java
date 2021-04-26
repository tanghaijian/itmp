package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;


/**
*@author liushan
*@Description 系统目录文档历史表
*@Date 2020/1/8
*@Param
*@return
**/
@TableName("tbl_system_directory_document_history")
public class TblSystemDirectoryDocumentHistory extends BaseEntity {


    /**
     * 系统目录文档表ID
     **/
    private Long systemDirectoryDocumentId;

    /**
     * 需求id
     **/
    private Long requirementId;

    /**
     * 文档名称
     **/
    private String documentName;

    /**
     * 文档版本
     **/
    private Integer documentVersion;

    /**
     * 文档类型（数据字典）
     **/
    private Integer documentType;

    /**
     * 存储方式（1:文档，2:MARKDOWN）
     **/
    private Integer saveType;

    /**
     * 签出用户（用户表主键）
     **/
    private Long checkOutUserId;

    /**
     * S3对象存储的BUCKET
     **/
    private String documentS3Bucket;

    /**
     * S3对象存储文件KEY
     **/
    private String documentS3Key;

    /**
     * MONGO存储文件KEY
     **/
    private String documentMongoKey;

    @TableField(exist = false)
    private TblRequirementInfo relatedRequirement;

    @TableField(exist = false)
    private String updateUserName;


    public Long getSystemDirectoryDocumentId() {
        return systemDirectoryDocumentId;
    }

    public void setSystemDirectoryDocumentId(Long systemDirectoryDocumentId) {
        this.systemDirectoryDocumentId = systemDirectoryDocumentId;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName == null ? null : documentName.trim();
    }

    public Integer getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(Integer documentVersion) {
        this.documentVersion = documentVersion;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public Integer getSaveType() {
        return saveType;
    }

    public void setSaveType(Integer saveType) {
        this.saveType = saveType;
    }

    public String getDocumentS3Bucket() {
        return documentS3Bucket;
    }

    public void setDocumentS3Bucket(String documentS3Bucket) {
        this.documentS3Bucket = documentS3Bucket == null ? null : documentS3Bucket.trim();
    }

    public String getDocumentS3Key() {
        return documentS3Key;
    }

    public void setDocumentS3Key(String documentS3Key) {
        this.documentS3Key = documentS3Key == null ? null : documentS3Key.trim();
    }

    public String getDocumentMongoKey() {
        return documentMongoKey;
    }

    public void setDocumentMongoKey(String documentMongoKey) {
        this.documentMongoKey = documentMongoKey == null ? null : documentMongoKey.trim();
    }


    public Long getCheckOutUserId() {
        return checkOutUserId;
    }

    public void setCheckOutUserId(Long checkOutUserId) {
        this.checkOutUserId = checkOutUserId;
    }

    public TblRequirementInfo getRelatedRequirement() {
        return relatedRequirement;
    }

    public void setRelatedRequirement(TblRequirementInfo relatedRequirement) {
        this.relatedRequirement = relatedRequirement;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
}