package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * 
* @ClassName: PackageController
* @Description: 包件管理
* @author author
* @date 2020年8月11日 下午9:16:30
*
 */
@RestController
@RequestMapping("package")
public class PackageController {
	
	/**
	 * 
	* @Title: toConstruction
	* @Description: 包件管理首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping("toPackage")
	public ModelAndView  toConstruction(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("package/packageManage");
		return view;
	}
}
