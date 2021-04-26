package cn.pioneeruniverse.system.dao.mybatis.uifavorite;

import cn.pioneeruniverse.system.entity.UiFavorite;

/**
*  类说明 
* @author:tingting
* @version:2019年1月28日 上午9:43:50 
*/
public interface UifavoriteDao {

	void update(UiFavorite uiFavorite);

	void insert(UiFavorite uiFavorite);

	UiFavorite findByMenuUrlAndUserId(UiFavorite uiFavorite);

}
