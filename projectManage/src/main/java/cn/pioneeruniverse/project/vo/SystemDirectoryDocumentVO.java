package cn.pioneeruniverse.project.vo;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class SystemDirectoryDocumentVO implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;

    /**
     * 章节id
     **/
    private Long id;  //章节id

    /**
     * 文档id
     **/
    private Long systemDirectoryDocumentId; //文档id

    /**
     * 文档名称
     **/
    private String documentName; //文档名称

    /**
     * 章节名称
     **/
    private String chaptersName;    //章节名称

    /**
     * 签出状态（1:是，2:否）
     **/
    private Integer checkoutStatus; //签出状态（1:是，2:否）

    /**
     * 签出用户
     **/
    private Long checkoutUserId; //签出用户

    /**
     * S3对象存储文件KEY
     **/
    private String documentS3Key; //S3对象存储文件KEY

    /**
     * S3对象存储文件KEY
     **/
    private String documentTempS3Key; //S3对象存储文件KEY(存放临时数据)

    /**
     * S3对象存储的BUCKET
     **/
    private String documentS3Bucket; //S3对象存储的BUCKET

    /**
     * 创建者
     **/
    private Long createBy; //创建者

    /**
     * 创建者姓名
     **/
    private String createByName; //创建者姓名

    /**
     * 创建时间
     **/
    private String createDate;  //创建时间

    /**
     * 上次修改者
     **/
    private Long lastUpdateBy;      //上次修改者

    /**
     * 上次修改者姓名
     **/
    private String lastUpdateByName; //上次修改者姓名

    /**
     * 上次修改时间
     **/
    private String lastUpdateDate;   //上次修改时间

    /**
     * S3文档内容
     **/
    private String DirectoryDocumentContent;    //S3文档内容

    /**
     * 文档类型
     **/
    private Integer documentType;   //文档类型

    /**
     * 存储方式（1:文档，2:MARKDOWN）
     **/
    private Integer saveType;   //存储方式（1:文档，2:MARKDOWN）

    /**
     * 文档版本
     **/
    private Integer documentVersion;   //文档版本

    /**
     * 章节版本
     **/
    private Integer chaptersVersion;    //章节版本

    /**
     * 关联系统
     **/
    private Long systemId;  //关联系统

    /**
     * MONGO存储KEY
     **/
    private String documentMongoKey;  //MONGO存储KEY

    /**
     * MONGO存储KEY(存放临时数据)
     **/
    private String documentTempMongoKey;  //MONGO存储KEY(存放临时数据)

    /**
     * 内容
     **/
    private String content; //内容

    /**
     * html内容
     **/
    private String contentHtml; //html内容

    /**
     * S3对象存储文件KEY2
     **/
    private String chaptersS3Key;//S3对象存储文件KEY2

    /**
     * S3对象存储文件KEY2(html_Key)
     **/
    private String chaptersS3Key2;//S3对象存储文件KEY2(html_Key)

    /**
     * 存放临时数S3对象存储文件KEY2
     **/
    private String chaptersTempS3Key;//存放临时数S3对象存储文件KEY2

    /**
     * 存放临时数S3对象存储文件KEY2(html_Key)
     **/
    private String chaptersTempS3Key2;//存放临时数S3对象存储文件KEY2(html_Key)

    /**
     * S3对象存储的BUCKET
     **/
    private String chaptersS3Bucket; //S3对象存储的BUCKET

    /**
     * 类型1-txt/2-html
     **/
    private String type;   //类型1-txt/2-html

    /**
     * 需求Code
     **/
    private String requirementCode;//需求Code

    /**
     * 开发任务Code
     **/
    private String featureCode;//开发任务Code
    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    public Long getCheckoutUserId() {
        return checkoutUserId;
    }

    public void setCheckoutUserId(Long checkoutUserId) {
        this.checkoutUserId = checkoutUserId;
    }

    public String getDocumentS3Key() {
        return documentS3Key;
    }

    public void setDocumentS3Key(String documentS3Key) {
        this.documentS3Key = documentS3Key;
    }

    public String getDocumentTempS3Key() {
        return documentTempS3Key;
    }

    public void setDocumentTempS3Key(String documentTempS3Key) {
        this.documentTempS3Key = documentTempS3Key;
    }

    public String getDocumentS3Bucket() {
        return documentS3Bucket;
    }

    public void setDocumentS3Bucket(String documentS3Bucket) {
        this.documentS3Bucket = documentS3Bucket;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getLastUpdateByName() {
        return lastUpdateByName;
    }

    public void setLastUpdateByName(String lastUpdateByName) {
        this.lastUpdateByName = lastUpdateByName;
    }

    public String getLastUpdateDate() {
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.lastUpdateDate = format.format(date);
        return format.format(date);
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getDirectoryDocumentContent() {
        return DirectoryDocumentContent;
    }

    public void setDirectoryDocumentContent(String directoryDocumentContent) {
        DirectoryDocumentContent = directoryDocumentContent;
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

    public Integer getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(Integer documentVersion) {
        this.documentVersion = documentVersion;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentMongoKey() {
        return documentMongoKey;
    }

    public void setDocumentMongoKey(String documentMongoKey) {
        this.documentMongoKey = documentMongoKey;
    }

    public String getDocumentTempMongoKey() {
        return documentTempMongoKey;
    }

    public void setDocumentTempMongoKey(String documentTempMongoKey) {
        this.documentTempMongoKey = documentTempMongoKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSystemDirectoryDocumentId() {
        return systemDirectoryDocumentId;
    }

    public void setSystemDirectoryDocumentId(Long systemDirectoryDocumentId) {
        this.systemDirectoryDocumentId = systemDirectoryDocumentId;
    }

    public String getChaptersName() {
        return chaptersName;
    }

    public void setChaptersName(String chaptersName) {
        this.chaptersName = chaptersName;
    }

    public Integer getChaptersVersion() {
        return chaptersVersion;
    }

    public void setChaptersVersion(Integer chaptersVersion) {
        this.chaptersVersion = chaptersVersion;
    }

    public String getChaptersS3Key2() {
        return chaptersS3Key2;
    }

    public void setChaptersS3Key2(String chaptersS3Key2) {
        this.chaptersS3Key2 = chaptersS3Key2;
    }

    public String getChaptersTempS3Key2() {
        return chaptersTempS3Key2;
    }

    public void setChaptersTempS3Key2(String chaptersTempS3Key2) {
        this.chaptersTempS3Key2 = chaptersTempS3Key2;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChaptersS3Key() {
        return chaptersS3Key;
    }

    public void setChaptersS3Key(String chaptersS3Key) {
        this.chaptersS3Key = chaptersS3Key;
    }

    public String getChaptersTempS3Key() {
        return chaptersTempS3Key;
    }

    public void setChaptersTempS3Key(String chaptersTempS3Key) {
        this.chaptersTempS3Key = chaptersTempS3Key;
    }

    public String getChaptersS3Bucket() {
        return chaptersS3Bucket;
    }

    public void setChaptersS3Bucket(String chaptersS3Bucket) {
        this.chaptersS3Bucket = chaptersS3Bucket;
    }

    public String getRequirementCode() {
		return requirementCode;
	}

	public void setRequirementCode(String requirementCode) {
		this.requirementCode = requirementCode;
	}
	
	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	@Override
    public String toString() {
        return "SystemDirectoryDocumentVO{" +
                "id=" + id +
                ", systemDirectoryDocumentId=" + systemDirectoryDocumentId +
                ", documentName='" + documentName + '\'' +
                ", chaptersName='" + chaptersName + '\'' +
                ", checkoutStatus=" + checkoutStatus +
                ", checkoutUserId=" + checkoutUserId +
                ", documentS3Key='" + documentS3Key + '\'' +
                ", documentTempS3Key='" + documentTempS3Key + '\'' +
                ", documentS3Bucket='" + documentS3Bucket + '\'' +
                ", createBy=" + createBy +
                ", createByName='" + createByName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", lastUpdateBy=" + lastUpdateBy +
                ", lastUpdateByName='" + lastUpdateByName + '\'' +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                ", DirectoryDocumentContent='" + DirectoryDocumentContent + '\'' +
                ", documentType=" + documentType +
                ", saveType=" + saveType +
                ", documentVersion=" + documentVersion +
                ", chaptersVersion=" + chaptersVersion +
                ", systemId=" + systemId +
                ", documentMongoKey='" + documentMongoKey + '\'' +
                ", documentTempMongoKey='" + documentTempMongoKey + '\'' +
                ", content='" + content + '\'' +
                ", contentHtml='" + contentHtml + '\'' +
                ", chaptersS3Key='" + chaptersS3Key + '\'' +
                ", chaptersS3Key2='" + chaptersS3Key2 + '\'' +
                ", chaptersTempS3Key='" + chaptersTempS3Key + '\'' +
                ", chaptersTempS3Key2='" + chaptersTempS3Key2 + '\'' +
                ", chaptersS3Bucket='" + chaptersS3Bucket + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
