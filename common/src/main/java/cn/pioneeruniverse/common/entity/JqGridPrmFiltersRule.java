package cn.pioneeruniverse.common.entity;

import java.io.Serializable;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:08 2019/1/29
 * @Modified By:
 */
public class JqGridPrmFiltersRule implements Serializable {

    private static final long serialVersionUID = 4649167836624435178L;

    private String field;//字段
    private String op;//操作
    private Object data;//值

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
