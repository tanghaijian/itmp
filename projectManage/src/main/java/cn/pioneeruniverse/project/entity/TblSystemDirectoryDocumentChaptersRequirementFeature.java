package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@TableName("tbl_system_directory_document_chapters_requirement_feature")
public class TblSystemDirectoryDocumentChaptersRequirementFeature extends BaseEntity {

    /**
     * 系统目录文档章节表ID
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemDirectoryDocumentChaptersId;

    /**
     * 开发任务表ID
     **/
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requirementFeatureId;

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

    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }

    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }

    public Integer getChaptersVersion() {
        return chaptersVersion;
    }

    public void setChaptersVersion(Integer chaptersVersion) {
        this.chaptersVersion = chaptersVersion;
    }
}