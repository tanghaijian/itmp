package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
*@author liushan
*@Description 系统目录文档章节需求关系表
*@Date 2020/1/14
*@Param
*@return
**/
@TableName("tbl_system_directory_document_chapters_requirement")
public class TblSystemDirectoryDocumentChaptersRequirement extends BaseEntity {

    /**
     * 系统目录文档章节表ID
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemDirectoryDocumentChaptersId;

    /**
     * 需求表ID
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requirementId;
    @JsonSerialize(using = ToStringSerializer.class)
    private String requirementCode;

    /**
     * 章节版本
     **/
    private Integer chaptersVersion;

    public Long getSystemDirectoryDocumentChaptersId() {
        return systemDirectoryDocumentChaptersId;
    }

    public void setSystemDirectoryDocumentChaptersId(Long systemDirectoryDocumentChaptersId) {
        this.systemDirectoryDocumentChaptersId = systemDirectoryDocumentChaptersId;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public Integer getChaptersVersion() {
        return chaptersVersion;
    }

    public void setChaptersVersion(Integer chaptersVersion) {
        this.chaptersVersion = chaptersVersion;
    }

	public  String getRequirementCode() {
		return requirementCode;
	}

	public void setRequirementCode( String requirementCode) {
		this.requirementCode = requirementCode;
	}
}