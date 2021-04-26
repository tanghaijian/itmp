package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
* @ClassName: TblSystemDirectoryDocument
* @Description: 系统文档bean
* @author author
* @date 2020年8月31日 上午11:22:24
*
 */
@TableName("tbl_system_directory_document")
public class TblSystemDirectoryDocument extends BaseEntity {

	 private Long systemDirectoryId;//系统目录ID

	    private Long systemId;//系统ID

	    private String documentName;//文档名

	    private Integer documentVersion;//文档版本

	    private Long documentType;//文档类型，tbl_system_document_type主键
	    @TableField(exist = false)
	    private String valueCode;
	    
	   

	    @TableField(exist = false)
	    private String systemName;//系统名

//	    private Integer documentType1;
	//
//	    private Integer documentType2;

	    private Integer saveType;//存储方式（1:文档，2:MARKDOWN）

	    private Integer checkoutStatus;//检出状态

	    private Long checkoutUserId;//检出人

	    private String documentS3Bucket;

	    private String documentS3Key;

	    private String documentMongoKey;

	    private String documentTempS3Key;

	    private String documentTempMongoKey;

	    @TableField(exist = false)
	    private String documentTypeName;//文档类型名

	    @TableField(exist = false)
	    private String checkOutUserName;//检出人姓名

	    @TableField(exist = false)
	    private String documentS3Content;//文档存于s3的详情内容

	    @TableField(exist = false)
	    private String createUserName;

	    @TableField(exist = false)
	    private String updateUserName;

	    @TableField(exist = false)
	    private Integer projectType;

	    @TableField(exist = false)
	    private Long requirementId;
	    @TableField(exist = false)
	    private String requirementCode;
	    @TableField(exist = false)
	    private String featureCode;
	    
	    @TableField(exist = false)
	    private Boolean existRelatedInfo;//是否存在关联信息
	    @TableField(exist = false)
	    private List<TblSystemDirectoryDocumentChapters> listChapters;//文档下的章节信息
	    
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


	    public String getDocumentTypeName() {
	        return documentTypeName;
	    }

	    public void setDocumentTypeName(String documentTypeName) {
	        this.documentTypeName = documentTypeName;
	    }

	    public String getCheckOutUserName() {
	        return checkOutUserName;
	    }

	    public void setCheckOutUserName(String checkOutUserName) {
	        this.checkOutUserName = checkOutUserName;
	    }

	    public String getDocumentS3Content() {
	        return documentS3Content;
	    }

	    public void setDocumentS3Content(String documentS3Content) {
	        this.documentS3Content = documentS3Content;
	    }

	    public Long getCheckoutUserId() {
	        return checkoutUserId;
	    }

	    public void setCheckoutUserId(Long checkoutUserId) {
	        this.checkoutUserId = checkoutUserId;
	    }

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
	//
//	    public Integer getDocumentType1() {
//	        return documentType1;
//	    }
	//
//	    public void setDocumentType1(Integer documentType1) {
//	        this.documentType1 = documentType1;
//	    }
	//
//	    public Integer getDocumentType2() {
//	        return documentType2;
//	    }
	//
//	    public void setDocumentType2(Integer documentType2) {
//	        this.documentType2 = documentType2;
//	    }

	    public Long getSystemId() {
	        return systemId;
	    }

	    public void setSystemId(Long systemId) {
	        this.systemId = systemId;
	    }

	    public Boolean getExistRelatedInfo() {
	        return existRelatedInfo;
	    }

	    public void setExistRelatedInfo(Boolean existRelatedInfo) {
	        this.existRelatedInfo = existRelatedInfo;
	    }

	    public Long getDocumentType() {
	        return documentType;
	    }

	    public void setDocumentType(Long documentType) {
	        this.documentType = documentType;
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
		
		public String getSystemName() {
			return systemName;
		}

		public void setSystemName(String systemName) {
			this.systemName = systemName;
		}

		public String getValueCode() {
			return valueCode;
		}

		public void setValueCode(String valueCode) {
			this.valueCode = valueCode;
		}

		public List<TblSystemDirectoryDocumentChapters> getListChapters() {
			return listChapters;
		}

		public void setListChapters(List<TblSystemDirectoryDocumentChapters> listChapters) {
			this.listChapters = listChapters;
		}
	    
}