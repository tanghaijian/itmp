package cn.pioneeruniverse.system.entity;

import java.io.Serializable;

/**
 * @deprecated
* @ClassName: CardMade
* @Description: （未用）
* @author author
* @date 2020年8月19日 下午9:08:18
*
 */
public class CardMade implements Serializable {
	private Long userId;
	private String cardUID;
	private String writeUID;
	private String userName;
	private String ClearUID;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCardUID() {
		return cardUID;
	}
	public void setCardUID(String cardUID) {
		this.cardUID = cardUID;
	}
	public String getWriteUID() {
		return writeUID;
	}
	public void setWriteUID(String writeUID) {
		this.writeUID = writeUID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getClearUID() {
		return ClearUID;
	}
	public void setClearUID(String clearUID) {
		ClearUID = clearUID;
	}
	@Override
	public String toString() {
		return "CardMade [userId=" + userId + ", cardUID=" + cardUID + ", writeUID=" + writeUID + ", userName="
				+ userName + ", ClearUID=" + ClearUID + "]";
	}
}
