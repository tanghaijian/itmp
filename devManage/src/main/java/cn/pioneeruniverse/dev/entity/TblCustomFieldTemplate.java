package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_custom_field_template")
public class TblCustomFieldTemplate extends BaseEntity {

    /**
     * 扩展表的表名，注意开发库的表需要以_itmpdb结尾
     **/
    private String customForm; //扩展表的表名，注意开发库的表需要以_itmpdb结尾

    /**
     * 扩展的字段
     **/
    private String customField; //扩展的字段

    /**
     * 字段名称
     **/
    @TableField(exist=false)
    private String columnName;

    /**
     * 字段注解
     **/
    @TableField(exist=false)
    private String columnComment;

    public String getCustomForm() {
        return customForm;
    }
    public void setCustomForm(String customForm) {
        this.customForm = customForm;
    }

    public String getCustomField() {
        return customField;
    }
    public void setCustomField(String customField) {
        this.customField = customField;
    }

    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }
    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
}