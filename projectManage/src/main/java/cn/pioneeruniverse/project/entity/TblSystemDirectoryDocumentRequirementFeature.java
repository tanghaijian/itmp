package cn.pioneeruniverse.project.entity;

import java.sql.Timestamp;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * xukai
 * @author 18670
 *
 */
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
@TableName("tbl_system_directory_document_requirement_feature")
public class TblSystemDirectoryDocumentRequirementFeature {
	 @JsonSerialize(using = ToStringSerializer.class)
	 private Long id;

	 /**
	  * 系统目录文档需求关系表
	  **/
	 private Long systemDirectoryDocumentId;
	 

	 private Long requiremenFeaturetId;
	 /**
	  * 版本
	  **/
	 private Integer documentVersion;
	 /**
	  * 更新人
	  **/
	 private Long updateUserId;
	 /**
	  * 更新时间
	  **/
	 @JSONField(format="yyyy-MM-dd HH:mm:ss")
	 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	 private Timestamp updateTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSystemDirectoryDocumentId() {
		return systemDirectoryDocumentId;
	}
	public void setSystemDirectoryDocumentId(Long systemDirectoryDocumentId) {
		this.systemDirectoryDocumentId = systemDirectoryDocumentId;
	}
	public Long getRequiremenFeaturetId() {
		return requiremenFeaturetId;
	}
	public void setRequiremenFeaturetId(Long requiremenFeaturetId) {
		this.requiremenFeaturetId = requiremenFeaturetId;
	}
	public Integer getDocumentVersion() {
		return documentVersion;
	}
	public void setDocumentVersion(Integer documentVersion) {
		this.documentVersion = documentVersion;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}
