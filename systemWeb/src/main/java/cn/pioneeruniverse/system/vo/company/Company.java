package cn.pioneeruniverse.system.vo.company;

import cn.pioneeruniverse.system.vo.BaseVo;
/**
 *
 * @ClassName: Company
 * @Description: 公司Vo
 * @author author
 *
 */
public class Company extends BaseVo{
	//公司ID
	private Long id;
	//公司名称
	private String companyName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	

}
