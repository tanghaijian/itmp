package cn.pioneeruniverse.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.system.entity.UiFavorite;
import cn.pioneeruniverse.system.service.uifavorite.UiFavoriteService;

/**
*  界面收藏Controller 测试管理平台用
* @author:tingting
* @version:2019年1月25日 下午5:01:19 
*/
@RestController
@RequestMapping("uifavorite")
public class UiFavoriteController extends BaseController{
	@Autowired
	private UiFavoriteService uiFavoriteService;
	@Autowired
	private RedisUtils redisUtils;
	
	/**
	 * 
	* @Title: addAndUpdate
	* @Description: 新增或修改页面收藏信息
	* @author author
	* @param request
	* @param uiFavorite 页面收藏信息
	* @return map key :status 1正常返回，2异常返回
	* @throws
	 */
	@RequestMapping("addAndUpdate")
	public Map<String, Object> addAndUpdate(HttpServletRequest request,UiFavorite uiFavorite){
		Map<String, Object> result = new HashMap<>();
		try {
			Long uid = CommonUtil.getCurrentUserId(request);
			uiFavorite.setUserId(Integer.parseInt(uid.toString()));
			uiFavoriteService.addAndUpdate(uiFavorite,request);
			result.put("status", "success");
		} catch (Exception e) {
			return super.handleException(e, "页面收藏失败！");
		}
		return result;
	}
	
	/**
	 * 
	* @Title: getFavoriteContent
	* @Description: 获取某个页面的收藏方案
	* @author author
	* @param request
	* @param menuUrl 当前页面的url
	* @return map key :status =1 正常返回，status=2返回异常
	*                  favoriteContent 返回的业务数据：String页面收藏方案的json格式
	* @throws
	 */
	@RequestMapping("getFavoriteContent")
	public Map<String, Object> getFavoriteContent(HttpServletRequest request,String menuUrl){
		Map<String, Object> map = new HashMap<>();
			try {
				Long uid = CommonUtil.getCurrentUserId(request);
				Map<String, Object> object = (Map<String, Object>) redisUtils.get("TBL_UI_FAVORIITE");
				if (object!=null) {
					List<Map<String, Object>>  list =  (List<Map<String, Object>>) object.get(uid.toString());
					if (list!=null && list.size()>0) {
						for (Map<String, Object> innerMap : list) {
							if (innerMap.containsKey(menuUrl)) {
								String favoriteContent = (String) innerMap.get(menuUrl);
								if (!StringUtils.isBlank(favoriteContent)) {
									map.put("favoriteContent", favoriteContent);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				return  super.handleException(e, "获取页面收藏失败！");
			}
		return map;
	}	
}
