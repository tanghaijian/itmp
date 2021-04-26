package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_data_dic")
public class TblDataDic extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 4639097273797835613L;
    private String termName;
    private String termCode;
    private String valueName;
    private String valueCode;
    private Integer valueSeq;

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
