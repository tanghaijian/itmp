package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.project.vo.SystemDirectoryRelateVo;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_system_directory_document_chapters")
public class TblSystemDirectoryDocumentChapters extends SystemDirectoryRelateVo {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Long systemDirectoryDocumentId;
    @TableField(exist = false)
    private String documentName;

    private Long systemDirectoryDocumentChaptersId;//系统目录文档章节ID

    private Integer documentVersion;//文档版本

    private String chaptersName;//章节名称

    private Integer chaptersLevel;//章节层级

    private Integer chaptersOrder;//章节顺序

    private Integer chaptersVersion;//章节版本

    private Integer checkoutStatus;//签出状态1 是 2 否

    private Long checkoutUserId;//签出用户

    private String chaptersS3Bucket;//S3对象存储的BUCKET

    private String chaptersS3Key;//S3对象存储文件KEY
    
    private String chaptersS3Key2;//S3对象存储html文件KEY2

    private String chaptersMongoKey;//MONGO存储KEY

    private String chaptersContentMd5;//章节内容MD5

    private String chaptersTempS3Key;//S3对象存储文件KEY(存放临时数据)

    private String chaptersTempMongoKey;//MONGO存储KEY(存放临时数据)

    private String chaptersTempS3Key2;//S3对象存储html文件KEY2

    private String chaptersTempContentMd5;//章节临时内容MD5

    private Long parentId;//上级章节ID
    
    @TableField(exist = false)
    private Long pId;
    /*被关联的章节*/
    @TableField(exist=false)
    private List<TblSystemDirectoryDocumentChapters> relateChapters;
    /*子章节*/
    @TableField(exist = false)
    private List<TblSystemDirectoryDocumentChapters> childChapters;

    private String parentIds;
    
    public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

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

    public String getChaptersName() {
        return chaptersName;
    }

    public void setChaptersName(String chaptersName) {
        this.chaptersName = chaptersName == null ? null : chaptersName.trim();
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

    public String getChaptersS3Bucket() {
        return chaptersS3Bucket;
    }

    public void setChaptersS3Bucket(String chaptersS3Bucket) {
        this.chaptersS3Bucket = chaptersS3Bucket == null ? null : chaptersS3Bucket.trim();
    }

    public String getChaptersS3Key() {
        return chaptersS3Key;
    }

    public void setChaptersS3Key(String chaptersS3Key) {
        this.chaptersS3Key = chaptersS3Key == null ? null : chaptersS3Key.trim();
    }

    public String getChaptersMongoKey() {
        return chaptersMongoKey;
    }

    public void setChaptersMongoKey(String chaptersMongoKey) {
        this.chaptersMongoKey = chaptersMongoKey == null ? null : chaptersMongoKey.trim();
    }

    public String getChaptersContentMd5() {
        return chaptersContentMd5;
    }

    public void setChaptersContentMd5(String chaptersContentMd5) {
        this.chaptersContentMd5 = chaptersContentMd5 == null ? null : chaptersContentMd5.trim();
    }

    public String getChaptersTempS3Key() {
        return chaptersTempS3Key;
    }

    public void setChaptersTempS3Key(String chaptersTempS3Key) {
        this.chaptersTempS3Key = chaptersTempS3Key == null ? null : chaptersTempS3Key.trim();
    }

    public String getChaptersTempMongoKey() {
        return chaptersTempMongoKey;
    }

    public void setChaptersTempMongoKey(String chaptersTempMongoKey) {
        this.chaptersTempMongoKey = chaptersTempMongoKey == null ? null : chaptersTempMongoKey.trim();
    }

    public String getChaptersTempContentMd5() {
        return chaptersTempContentMd5;
    }

    public void setChaptersTempContentMd5(String chaptersTempContentMd5) {
        this.chaptersTempContentMd5 = chaptersTempContentMd5 == null ? null : chaptersTempContentMd5.trim();
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

    public Long getSystemDirectoryDocumentChaptersId() {
        return systemDirectoryDocumentChaptersId;
    }

    public void setSystemDirectoryDocumentChaptersId(Long systemDirectoryDocumentChaptersId) {
        this.systemDirectoryDocumentChaptersId = systemDirectoryDocumentChaptersId;
    }

    
    
    public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public List<TblSystemDirectoryDocumentChapters> getRelateChapters() {
		return relateChapters;
	}

	public void setRelateChapters(List<TblSystemDirectoryDocumentChapters> relateChapters) {
		this.relateChapters = relateChapters;
	}

    public String getChaptersTempS3Key2() {
        return chaptersTempS3Key2;
    }

    public void setChaptersTempS3Key2(String chaptersTempS3Key2) {
        this.chaptersTempS3Key2 = chaptersTempS3Key2;
    }

    public String getChaptersS3Key2() {
		return chaptersS3Key2;
	}

	public void setChaptersS3Key2(String chaptersS3Key2) {
		this.chaptersS3Key2 = chaptersS3Key2;
	}

    public List<TblSystemDirectoryDocumentChapters> getChildChapters() {
		return childChapters;
	}

	public void setChildChapters(List<TblSystemDirectoryDocumentChapters> childChapters) {
		this.childChapters = childChapters;
	}

	@Override
    public String toString() {
        return "TblSystemDirectoryDocumentChapters{" +
                "systemDirectoryDocumentId=" + systemDirectoryDocumentId +
                ", documentName='" + documentName + '\'' +
                ", systemDirectoryDocumentChaptersId=" + systemDirectoryDocumentChaptersId +
                ", documentVersion=" + documentVersion +
                ", chaptersName='" + chaptersName + '\'' +
                ", chaptersLevel=" + chaptersLevel +
                ", chaptersOrder=" + chaptersOrder +
                ", chaptersVersion=" + chaptersVersion +
                ", checkoutStatus=" + checkoutStatus +
                ", checkoutUserId=" + checkoutUserId +
                ", chaptersS3Bucket='" + chaptersS3Bucket + '\'' +
                ", chaptersS3Key='" + chaptersS3Key + '\'' +
                ", chaptersS3Key2='" + chaptersS3Key2 + '\'' +
                ", chaptersMongoKey='" + chaptersMongoKey + '\'' +
                ", chaptersContentMd5='" + chaptersContentMd5 + '\'' +
                ", chaptersTempS3Key='" + chaptersTempS3Key + '\'' +
                ", chaptersTempMongoKey='" + chaptersTempMongoKey + '\'' +
                ", chaptersTempS3Key2='" + chaptersTempS3Key2 + '\'' +
                ", chaptersTempContentMd5='" + chaptersTempContentMd5 + '\'' +
                ", parentId=" + parentId +
                ", pId=" + pId +
                ", relateChapters=" + relateChapters +
                ", parentIds='" + parentIds + '\'' +
                '}';
    }
}