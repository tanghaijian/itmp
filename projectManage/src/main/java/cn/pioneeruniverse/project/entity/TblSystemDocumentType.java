package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblSystemDocumentType
* @Description: 文档类型bean
* @author author
* @date 2020年8月31日 下午1:17:02
*
 */
public class TblSystemDocumentType extends BaseEntity{
	private String documentType;//文档类型
	
	private Integer classify;//类别：'1、需求类，2、系统类

	private String valueCode;//显示值编号

	private Integer valueSeq;//排序

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Integer getClassify() {
		return classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
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
