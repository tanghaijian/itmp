package cn.pioneeruniverse.system.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
* 页面收藏 
* @author:tingting
* @version:2019年1月28日 上午9:34:16 
*/
@TableName("tbl_ui_favorite")
public class UiFavorite extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户ID
	private Integer userId;
	//菜单编号
	private String menuUrl;
	//收藏内容
	private String favoriteContent;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getFavoriteContent() {
		return favoriteContent;
	}

	public void setFavoriteContent(String favoriteContent) {
		this.favoriteContent = favoriteContent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UiFavorite [userId=" + userId + ", menuUrl=" + menuUrl + ", favoriteContent=" + favoriteContent + "]";
	}
	
}
