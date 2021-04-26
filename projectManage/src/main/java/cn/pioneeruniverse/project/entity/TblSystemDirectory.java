package cn.pioneeruniverse.project.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblSystemDirectory
* @Description:系统目录bean
* @author author
* @date 2020年8月31日 上午11:16:03
*
 */
@TableName("tbl_system_directory")
public class TblSystemDirectory extends BaseEntity {

    private static final long serialVersionUID = -3326783192921906798L;
    private Integer directoryType;// 文档目录类型（数据字典 1=文档库；2=WIKI）
   
    private Long projectId;//项目ID

    private Integer projectType;//项目类型（数据字典 1=IT运维类；2=IT新建类）
    
    private String documentTypes;//当前目录下可存放文档类型，,文档类型表主键,以,隔开

    private String dirName;//目录名

    private Integer orderNumber;//顺序

    private Integer tierNumber;//目录层级号

    private Long parentId;//父目录ID

    private String parentIds;//所有父目录ID

    private Integer createType;//创建方式（1:自动创建，2:手工创建）

    @TableField(exist = false)
    private List<TblSystemDirectory> children;//子目录

    @TableField(exist = false)
    private String documentType2s; //当前目录下可存放文档类型，对应数据字典的valuecode并以,隔开存储

    @TableField(exist = false)
    private Long systemId; //系统ID
    @TableField(exist = false)
    private String systemName; //系统名
    
    @TableField(exist = false)
    private Long systemDirectoryTemplateId; //模板ID
    
    //以下对应ztree组装时使用
    @TableField(exist = false)
    private Boolean leaf; 
    
    @TableField(exist = false)
    private Integer level;
    @TableField(exist = false)
    private Boolean isLeaf;
    @TableField(exist = false)
    private Boolean isSelect;
    @TableField(exist = false)
    private Boolean loaded;
    @TableField(exist = false)
    private Boolean expanded;
    
    @TableField(exist = false)
    private Integer readAuth;//读权限
    @TableField(exist = false)
    private Integer writeAuth;//写权限
    
    
    
    
    public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Boolean getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}

	public Boolean getLoaded() {
		return loaded;
	}

	public void setLoaded(Boolean loaded) {
		this.loaded = loaded;
	}

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

	public Integer getReadAuth() {
		return readAuth;
	}

	public void setReadAuth(Integer readAuth) {
		this.readAuth = readAuth;
	}

	public Integer getWriteAuth() {
		return writeAuth;
	}

	public void setWriteAuth(Integer writeAuth) {
		this.writeAuth = writeAuth;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

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

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public List<TblSystemDirectory> getChildren() {
        return children;
    }

    public void setChildren(List<TblSystemDirectory> children) {
        this.children = children;
    }

    public Long getSystemDirectoryTemplateId() {
        return systemDirectoryTemplateId;
    }

    public void setSystemDirectoryTemplateId(Long systemDirectoryTemplateId) {
        this.systemDirectoryTemplateId = systemDirectoryTemplateId;
    }

    public String getDocumentType2s() {
        return documentType2s;
    }

    public void setDocumentType2s(String documentType2s) {
        this.documentType2s = documentType2s;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

	public String getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(String documentTypes) {
		this.documentTypes = documentTypes;
	}

	

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Integer getDirectoryType() {
		return directoryType;
	}

	public void setDirectoryType(Integer directoryType) {
		this.directoryType = directoryType;
	}
    
}
