package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_data_dic")
public class TblDataDic extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 4639097273797835613L;
    private String termName; //数据字典类型名称
    private String termCode;//数据字典类型编码
    private String valueName; //数据字典值，显示名
    private String valueCode; //数据字典值，编码
    private Integer valueSeq; //显示顺序，排序用

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermCode() {
        return termCode;
    }

    public void setTermCode(String termCode) {
        this.termCode = termCode;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public Integer getValueSeq() {
        return valueSeq;
    }

    public void setValueSeq(Integer valueSeq) {
        this.valueSeq = valueSeq;
    }
}
