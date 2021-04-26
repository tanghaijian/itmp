package cn.pioneeruniverse.common.entity;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 前端ajax请求返回封装
 * @Date: Created in 15:40 2018/12/28
 * @Modified By:
 */
public class AjaxModel implements Serializable {
    private static final long serialVersionUID = -8533626619379837297L;

    //是否成功
    private Boolean flag;

    private Object data;//返回数据

    public AjaxModel() {

    }

    public AjaxModel(Boolean flag) {
        this.flag = flag;
    }

    public AjaxModel(Boolean flag, Object data) {
        this.flag = flag;
        this.data = data;
    }

    public static AjaxModel SUCCESS() {
        return new AjaxModel(true);
    }

    public static AjaxModel SUCCESS(Object data) {
        return new AjaxModel(true, data);
    }

    public static AjaxModel FAIL(Exception e) {
        Map<String, Object> map = new HashMap() {{
            put("message", e.getMessage());
            put("cause", e.getCause());
        }};
        return new AjaxModel(false, map);
    }

    public static AjaxModel FAIL(Exception e, Object o) {
        Map<String, Object> map = new HashMap() {{
            put("data", o);
            put("message", e.getMessage());
            put("cause", e.getCause());
        }};
        return new AjaxModel(false, map);
    }

    public static AjaxModel FAIL(Object data) {
        return new AjaxModel(false, data);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
