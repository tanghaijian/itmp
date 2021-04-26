package cn.pioneeruniverse.system.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
* @ClassName: TblCustomFieldTemplate
* @Description: 自定义属性配置字段
* @author author
* @date 2020年7月31日 下午7:14:30
*
 */
@TableName("tbl_custom_field_template")
public class TblCustomFieldTemplate extends BaseEntity {

    //表名	
    private String customForm;
    //字段
    private String customField;
    //字段名
    @TableField(exist=false)
    private String columnName;
    //字段备注
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