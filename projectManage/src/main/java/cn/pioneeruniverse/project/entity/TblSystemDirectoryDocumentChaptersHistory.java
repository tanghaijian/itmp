package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
*@author liushan
*@Description 系统目录文档章节历史表
*@Date 2020/1/13
*@Param 
*@return 
**/
@TableName("tbl_system_directory_document_chapters_history")
public class TblSystemDirectoryDocumentChaptersHistory extends BaseEntity {

    /**
     * 系统目录文档表ID
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemDirectoryDocumentId;

    /**
     * 文档版本
     **/
    private Integer documentVersion;

    /**
     * 系统目录文档章节表ID
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemDirectoryDocumentChaptersId;

    /**
     * 章节名称
     **/
    private String chaptersName;

    /**
     * 章节层级
     **/
    private Integer chaptersLevel;

    /**
     * 章节顺序
     **/
    private Integer chaptersOrder;

    /**
     * 章节版本
     **/
    private Integer chaptersVersion;

    /**
     * 签出用户（用户表主键
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private String checkoutUserId;

    /**
     * S3对象存储的BUCKET
     **/
    private String chaptersS3Bucket;

    /**
     * S3对象存储文件KEY
     **/
    private String chaptersS3Key;

    /**
     * MONGO存储KEY
     **/
    private String chaptersMongoKey;

    /**
     *  章节内容MD5
     **/
    private String chaptersContentMd5;

    /**
     * S3对象存储文件KEY(存放临时数据)
     **/
    private String chaptersTempS3Key;

    /**
     * MONGO存储KEY(存放临时数据)
     **/
    private String chaptersTempMongoKey;

    /**
     *  章节临时内容MD5
     **/
    private String chaptersTempContentMd5;


    /**
     * 上级章节ID
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
     * 上级章节ID（1，2，3）
     **/
    private String parentIds;

    @TableField(exist = false)
    private TblRequirementInfo relatedRequirement;
    @TableField(exist = false)
    private String checkoutUserName;
    @TableField(exist = false)
    private String createName;

    public Long getSystemDirectoryDocumentId() {
        return systemDirectoryDocumentId;
    }

    public void setSystemDirectoryDocumentId(Long systemDirectoryDocumentId) {
        this.systemDirectoryDocumentId = systemDirectoryDocumentId;
    }

    public Integer getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(Integer documentVersion) {
        this.documentVersion = documentVersion;
    }

    public Long getSystemDirectoryDocumentChaptersId() {
        return systemDirectoryDocumentChaptersId;
    }

    public void setSystemDirectoryDocumentChaptersId(Long systemDirectoryDocumentChaptersId) {
        this.systemDirectoryDocumentChaptersId = systemDirectoryDocumentChaptersId;
    }

    public String getChaptersName() {
        return chaptersName;
    }

    public void setChaptersName(String chaptersName) {
        this.chaptersName = chaptersName;
    }

    public Integer getChaptersLevel() {
        return chaptersLevel;
    }

    public void setChaptersLevel(Integer chaptersLevel) {
        this.chaptersLevel = chaptersLevel;
    }

    public Integer getChaptersOrder() {
        return chaptersOrder;
    }

    public void setChaptersOrder(Integer chaptersOrder) {
        this.chaptersOrder = chaptersOrder;
    }

    public Integer getChaptersVersion() {
        return chaptersVersion;
    }

    public void setChaptersVersion(Integer chaptersVersion) {
        this.chaptersVersion = chaptersVersion;
    }

    public String getCheckoutUserId() {
        return checkoutUserId;
    }

    public void setCheckoutUserId(String checkoutUserId) {
        this.checkoutUserId = checkoutUserId;
    }

    public String getCheckoutUserName() {
        return checkoutUserName;
    }

    public void setCheckoutUserName(String checkoutUserName) {
        this.checkoutUserName = checkoutUserName;
    }

    public String getChaptersS3Bucket() {
        return chaptersS3Bucket;
    }

    public void setChaptersS3Bucket(String chaptersS3Bucket) {
        this.chaptersS3Bucket = chaptersS3Bucket;
    }

    public String getChaptersS3Key() {
        return chaptersS3Key;
    }

    public void setChaptersS3Key(String chaptersS3Key) {
        this.chaptersS3Key = chaptersS3Key;
    }

    public String getChaptersMongoKey() {
        return chaptersMongoKey;
    }

    public void setChaptersMongoKey(String chaptersMongoKey) {
        this.chaptersMongoKey = chaptersMongoKey;
    }

    public String getChaptersContentMd5() {
        return chaptersContentMd5;
    }

    public void setChaptersContentMd5(String chaptersContentMd5) {
        this.chaptersContentMd5 = chaptersContentMd5;
    }

    public String getChaptersTempS3Key() {
        return chaptersTempS3Key;
    }

    public void setChaptersTempS3Key(String chaptersTempS3Key) {
        this.chaptersTempS3Key = chaptersTempS3Key;
    }

    public String getChaptersTempMongoKey() {
        return chaptersTempMongoKey;
    }

    public void setChaptersTempMongoKey(String chaptersTempMongoKey) {
        this.chaptersTempMongoKey = chaptersTempMongoKey;
    }

    public String getChaptersTempContentMd5() {
        return chaptersTempContentMd5;
    }

    public void setChaptersTempContentMd5(String chaptersTempContentMd5) {
        this.chaptersTempContentMd5 = chaptersTempContentMd5;
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

    public TblRequirementInfo getRelatedRequirement() {
        return relatedRequirement;
    }

    public void setRelatedRequirement(TblRequirementInfo relatedRequirement) {
        this.relatedRequirement = relatedRequirement;
    }

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}
    
    
}