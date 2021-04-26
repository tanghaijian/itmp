package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName: TblSystemDirectoryDocument
 * @Description: 系统目录文档类
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
@TableName("tbl_system_directory_document")
public class TblSystemDirectoryDocument extends BaseEntity {

    private Long systemDirectoryId;//系统目录id

    private Integer projectType;//项目类型

    private Long requirementId;//需求id

    private String documentName;//文档名称

    private Integer documentVersion;

    private Integer documentType;//文档类型

    private Integer saveType;//保存类型

    private Integer checkoutStatus;//检出状态

    private String documentS3Bucket;//S3桶

    private String documentS3Key;//S3 KEY

    private String documentMongoKey; //mongodbKEY

    private String documentTempS3Key; //临时保存文档的s3Key

    private String documentTempMongoKey;//临时保存文档的mongodb key




    public Long getSystemDirectoryId() {
        return systemDirectoryId;
    }

    public void setSystemDirectoryId(Long systemDirectoryId) {
        this.systemDirectoryId = systemDirectoryId;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
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

    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
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

    public String getDocumentTempS3Key() {
        return documentTempS3Key;
    }

    public void setDocumentTempS3Key(String documentTempS3Key) {
        this.documentTempS3Key = documentTempS3Key == null ? null : documentTempS3Key.trim();
    }

    public String getDocumentTempMongoKey() {
        return documentTempMongoKey;
    }

    public void setDocumentTempMongoKey(String documentTempMongoKey) {
        this.documentTempMongoKey = documentTempMongoKey == null ? null : documentTempMongoKey.trim();
    }





}