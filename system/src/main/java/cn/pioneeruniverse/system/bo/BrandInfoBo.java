package cn.pioneeruniverse.system.bo;

import java.io.Serializable;

/**
 * 
* @ClassName: BrandInfoBo
* @Description: 没用的类
* @author author
* @date 2020年8月23日 下午2:50:57
*
 */
public class BrandInfoBo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3179207241774039440L;

	private Long id;
	
	private String brandName;
	
	private String brandNumber;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandNumber() {
		return brandNumber;
	}

	public void setBrandNumber(String brandNumber) {
		this.brandNumber = brandNumber;
	}

	
}
