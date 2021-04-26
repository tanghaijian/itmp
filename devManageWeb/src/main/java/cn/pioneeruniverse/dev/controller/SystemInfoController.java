package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * 
* @ClassName: SystemInfoController
* @Description: 系统配置
* @author author
* @date 2020年8月11日 下午9:20:11
*
 */
@RestController
@RequestMapping("systeminfo")
public class SystemInfoController extends BaseController{
	
	/**
	 * 
	* @Title: toSystemPage
	* @Description: 系统配置首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "toSystem")
	public ModelAndView toSystemPage(HttpServletRequest request) {
		ModelAndView view  = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("systeminfo/systeminfoList");
		return view;
	}

	/**
	 * 
	* @Title: toSystemScm
	* @Description: 源码配置
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "toSystemScm")
	public ModelAndView toSystemScm(HttpServletRequest request) {
		ModelAndView view  = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("systeminfo/systemScmList");
		return view;
	}

	/**
	 * 
	* @Title: toSystemDeploy
	* @Description: 模块配置
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "toSystemDeploy")
	public ModelAndView toSystemDeploy(HttpServletRequest request) {
		ModelAndView view  = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("systeminfo/systemDeploy");
		return view;
	}

	/**
	 * 
	* @Title: toVersionManagement
	* @Description: 版本配置
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "toVersionManagement")
	public ModelAndView toVersionManagement(HttpServletRequest request) {
		ModelAndView view  = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("systeminfo/versionManagement");
		return view;
	}

	/**
	 * 
	* @Title: toAddSystemModel
	* @Description: 添加系统模块
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "toAddSystemModel")
	public ModelAndView toAddSystemModel(HttpServletRequest request) {
		ModelAndView view  = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("systeminfo/addSystemModel");
		return view;
	}

	/**
	 * 
	* @Title: toDeploymentConfig
	* @Description: 部署配置
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "toDeploymentConfig")
	public ModelAndView toDeploymentConfig(HttpServletRequest request) {
		ModelAndView view  = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("systeminfo/deploymentConfig");
		return view;
	}

	/**
	 * 
	* @Title: toAutomatedTesting
	* @Description: 自动化测试部署
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "toAutomatedTesting")
	public ModelAndView toAutomatedTesting(HttpServletRequest request) {
		ModelAndView view  = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("systeminfo/automatedTest");
		return view;
	}

	/**
	 * 
	* @Title: toEnvironmentConfig
	* @Description: 环境配置
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "toEnvironmentConfig")
	public ModelAndView toEnvironmentConfig(HttpServletRequest request) {
		ModelAndView view  = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("systeminfo/environmentConfig");
		return view;
	}

}
