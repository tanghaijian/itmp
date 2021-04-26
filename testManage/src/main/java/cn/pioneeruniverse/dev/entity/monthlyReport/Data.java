package cn.pioneeruniverse.dev.entity.monthlyReport;

import java.util.List;

/**
 * Created by aviyy on 2020/10/22.
 */
public class Data {
    private String code;
    private String name;
    private Object value;

    private List<Data> list;

    public Data() {
    }

    public Data(String code, String name, Object value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }
    public Data(String code, String name, Object value, List<Data> list) {
        this.code = code;
        this.name = name;
        this.value = value;
        this.list = list;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Data> getList() {
        return list;
    }

    public void setList(List<Data> list) {
        this.list = list;
    }
}
