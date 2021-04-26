package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.util.List;

/**
 * Description:树形数据封装实体类
 * Author:liushan
 * Date: 2018/11/2
 * Time: 下午 2:34
 */
public class TreeData extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String url;// 路径
    private String Menu;// 菜单名称
    private List<TblMenuButtonInfo>  buttonList;//按钮集合
    private Boolean isSelect;// 是否选中
    private Integer level;// 级别
    private Boolean isLeaf;// boolean 是否为叶子节点
    private Long parent;// 父节点
    private Boolean loaded;// boolean 是否加载完成
    private Boolean expanded;//boolean 表示此节点是否展开

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }
    
    

    public List<TblMenuButtonInfo> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<TblMenuButtonInfo> buttonList) {
		this.buttonList = buttonList;
	}

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public Boolean getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
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
}
