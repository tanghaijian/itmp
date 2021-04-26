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
* @ClassName: SystemJenkinsParameterController
* @Description: 构建参数配置
* @author author
* @date 2020年8月11日 下午9:23:42
*
 */
@RestController
@RequestMapping("jenkinsParameter")
public class SystemJenkinsParameterController  extends BaseController{
	
	/**
	 * 
	* @Title: toJenkinsParameter
	* @Description: 构建参数配置首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping("toJenkinsParameter")
	public ModelAndView  toJenkinsParameter(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("JenkinsParameter/JenkinsParameter");
		return view;
	}
}
