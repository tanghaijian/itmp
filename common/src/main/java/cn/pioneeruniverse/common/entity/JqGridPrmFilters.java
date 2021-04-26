package cn.pioneeruniverse.common.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:05 2019/1/29
 * @Modified By:
 */
public class JqGridPrmFilters implements Serializable {

    private static final long serialVersionUID = 1568853007403009917L;

    private String groupOp;//字段之间关系

    private List<JqGridPrmFiltersRule> rules;//规则

    public String getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<JqGridPrmFiltersRule> getRules() {
        return rules;
    }

    public void setRules(List<JqGridPrmFiltersRule> rules) {
        this.rules = rules;
    }
}
