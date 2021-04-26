package cn.pioneeruniverse.system.bo;

import java.io.Serializable;


/**
 * 
* @ClassName: TradeAreaBo
* @Description: 没用的类
* @author author
* @date 2020年8月23日 下午2:51:12
*
 */
public class TradeAreaBo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -413929943302344750L;

	
	private Long id;
	
	private String tradeAreaName;
	
	private String tradeAreaNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeAreaName() {
		return tradeAreaName;
	}

	public void setTradeAreaName(String tradeAreaName) {
		this.tradeAreaName = tradeAreaName;
	}

	public String getTradeAreaNumber() {
		return tradeAreaNumber;
	}

	public void setTradeAreaNumber(String tradeAreaNumber) {
		this.tradeAreaNumber = tradeAreaNumber;
	}
	
	
}
