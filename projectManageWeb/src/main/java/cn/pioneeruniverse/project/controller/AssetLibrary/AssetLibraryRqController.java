package cn.pioneeruniverse.project.controller.AssetLibrary;

import cn.pioneeruniverse.common.utils.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
/**
 * @Author: weiji
 * @Description:
 * @Modified By:
 */
@RestController
@RequestMapping("assetRq")
public class AssetLibraryRqController {

	
	/**
	 * 
	* @Title: toPersonnelManagement
	* @Description: 资产库(需求视角)首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping("toRequirement")
	public ModelAndView toPersonnelManagement(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("currentUserId", CommonUtil.getCurrentUserId(request));
		view.addObject("token", CommonUtil.getToken(request));
		//view.setViewName("assetsLibirary/assetsLibraryNeeds/assetsLibraryNeeds");
        view.setViewName("assetsLibrary/assetsLibraryNeeds/assetsLibraryNeeds");
		return view;
	}
	
	/**
	 * 
	* @Title: toAssociatedDemand
	* @Description: 资产库(需求视角)，查询：关联需求跳转页面
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "/toAssociatedDemand", method = RequestMethod.GET)
	public ModelAndView toAssociatedDemand(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("currentUserId", CommonUtil.getCurrentUserId(request));
		view.addObject("token", CommonUtil.getToken(request));
		view.setViewName("assetsLibrary/associatedDemand");
		return view;
	}

	/**
	 * 
	* @Title: toDevelopmentTask
	* @Description: 资产库(需求视角)，查询：开发任务跳转页面
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "/toDevelopmentTask", method = RequestMethod.GET)
	public ModelAndView toDevelopmentTask(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("currentUserId", CommonUtil.getCurrentUserId(request));
		view.addObject("token", CommonUtil.getToken(request));
		view.setViewName("assetsLibrary/developmentTask");
		return view;
	}

}
