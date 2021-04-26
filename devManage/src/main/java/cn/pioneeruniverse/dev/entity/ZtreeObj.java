package cn.pioneeruniverse.dev.entity;

import java.util.List;
/**
 *
 *
 * @Description:  树形节点，用于页面树形展示用
 * @author author
 * @date 2020年8月10日 下午14:27:12
 *
 */
public class ZtreeObj {

    private String id;//主键
    private String realId;//真实id
    private String name;//节点名称
    private String code;//编码
    private boolean open;//节点是否打开
    private boolean nocheck;//未选中
    private  boolean checked;//选中
    public List<ZtreeObj> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<ZtreeObj> childrens) {
        this.childrens = childrens;
    }

    List<ZtreeObj> childrens;

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    private String child;
    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    private String children;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    private String systemId;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    private String pId;
    private String level;
    private String requireId;
    private String docType;

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    private String parentIds;




    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    private boolean  isParent;
    private String type;
    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealId() {
        return realId;
    }

    public void setRealId(String realId) {
        this.realId = realId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRequireId() {
        return requireId;
    }

    public void setRequireId(String requireId) {
        this.requireId = requireId;
    }

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
