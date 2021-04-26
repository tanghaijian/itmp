package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblSystemDirectoryTemplate
* @Description: 系统目录模板
* @author author
* @date 2020年8月31日 下午1:07:15
*
 */
@TableName("tbl_system_directory_template")
public class TblSystemDirectoryTemplate extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private Integer projectType;//项目类型（数据字典 1=IT运维类；2=IT新建类）
	
	private Integer directoryType;//目录归属类型（1:项目，2:系统）
	
	private String dirName;//目录名
	
	private Integer orderNumber;//目录顺序
	
	private Integer tierNumber;//层级号
	
	private String documentTypes;//目录可创建的文档类型,文档类型表主键,以,隔开
	
	private Long parentId;//父目录
	
	private String parentIds;//所有父目录，以,隔开
	
	private String documentType2s;//文档类，数据字典对应的valuecode，以,隔开

	public Integer getProjectType() {
		return projectType;
	}

	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getTierNumber() {
		return tierNumber;
	}

	public void setTierNumber(Integer tierNumber) {
		this.tierNumber = tierNumber;
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

	public String getDocumentType2s() {
		return documentType2s;
	}

	public void setDocumentType2s(String documentType2s) {
		this.documentType2s = documentType2s;
	}

	public Integer getDirectoryType() {
		return directoryType;
	}

	public void setDirectoryType(Integer directoryType) {
		this.directoryType = directoryType;
	}

	public String getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(String documentTypes) {
		this.documentTypes = documentTypes;
	}
	
	
}
