package cn.pioneeruniverse.project.vo;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * Description: jgrid树形结构vo类
 * Author:liushan
 * Date: 2019/5/28 上午 9:47
 */
public class TreeVo extends BaseEntity {
	// 级别
    private Integer level;
 // boolean 是否为叶子节点
    private Boolean isLeaf;
 // 父节点
    private Long parent;
 // boolean 是否加载完成
    private Boolean loaded;
  //boolean 表示此节点是否展开
    private Boolean expanded;
  //节点id
    private Long nodeid;
 // 级别
    private Integer n_level;

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

    public Long getNodeid() {
        return nodeid;
    }

    public void setNodeid(Long nodeid) {
        this.nodeid = nodeid;
    }

    public Integer getN_level() {
        return n_level;
    }

    public void setN_level(Integer n_level) {
        this.n_level = n_level;
    }
}
