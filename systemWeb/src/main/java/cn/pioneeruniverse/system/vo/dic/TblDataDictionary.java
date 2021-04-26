package cn.pioneeruniverse.system.vo.dic;

import cn.pioneeruniverse.system.vo.BaseVo;

/**
 * 
* @ClassName: TblDataDictionary
* @Description: 数据字典
* @author author
* @date 2020年9月4日 上午10:45:10
*
 */
public class TblDataDictionary extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1782134754937933244L;
	//数据字典编码
	private String termCode;
	//数据字典项编码
	private Integer valueCode;
	//数据字典名称
    private String termName;
    //数据字典项名称
    private String valueName;
    //排序号
	private Integer valueSeq;

	public String getTermCode() {
		return termCode;
	}

	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}

	public Integer getValueCode() {
		return valueCode;
	}

	public void setValueCode(Integer valueCode) {
		this.valueCode = valueCode;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public Integer getValueSeq() {
		return valueSeq;
	}

	public void setValueSeq(Integer valueSeq) {
		this.valueSeq = valueSeq;
	}
}
