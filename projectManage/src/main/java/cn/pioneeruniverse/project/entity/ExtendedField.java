package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.bean.PropertyInfo;

/**
 *@author
 *@Description 拓展字段类
 *@Date 2020/8/7
 *@return
 **/
public class ExtendedField {
    @PropertyInfo(name = "字段显示名")
    private String label;
    @PropertyInfo(name = "字段名")
    private String fieldName;
    @PropertyInfo(name = "字段类型(数据字典）")
    private String type;
    @PropertyInfo(name = "允许空值")
    private String required;
    @PropertyInfo(name = "最大长度")
    private String maxLength;
    @PropertyInfo(name = "默认值")
    private String defaultValue;
    @PropertyInfo(name = "有效无效")
    private String status;
    @PropertyInfo(name = "只读")
    private String readOnly;
    @PropertyInfo(name = "枚举类型")
    private String enums;
    @PropertyInfo(name = "扩展字段对应的值")
    private String valueName;

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getRequired() {
        return required;
    }
    public void setRequired(String required) {
        this.required = required;
    }

    public String getMaxLength() {
        return maxLength;
    }
    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getReadOnly() {
        return readOnly;
    }
    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public String getEnums() {
        return enums;
    }
    public void setEnums(String enums) {
        this.enums = enums;
    }

    public String getValueName() {
        return valueName;
    }
    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
}
