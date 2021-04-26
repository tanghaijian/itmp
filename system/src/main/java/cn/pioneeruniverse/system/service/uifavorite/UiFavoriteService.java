package cn.pioneeruniverse.system.service.uifavorite;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.system.entity.UiFavorite;

/**
*  类说明 
* @author:tingting
* @version:2019年1月28日 上午9:40:13 
*/
public interface UiFavoriteService {

	void addAndUpdate(UiFavorite uiFavorite, HttpServletRequest request);

}
