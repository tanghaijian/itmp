package cn.pioneeruniverse.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 *
 * @ClassName: ProgramInfoController
 * @Description:
 * @author author
 * @date 2020年8月26日 15:09:43
 *
 */
@RestController
@RequestMapping("program")
public class ProgramInfoController {

	/**
	 * 
	* @Title: toOamProject
	* @Description: 项目群管理
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping("toProgramManage")
	public ModelAndView toOamProject(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("program/programManage");
		return view;
	}
	
	/**
	 * 
	* @Title: toProgramDetail
	* @Description: 项目群管理-管理
	* @author author
	* @param request
	* @param id 项目群ID
	* @return
	 */
	@RequestMapping("toProgramDetail")
	public ModelAndView toProgramDetail(HttpServletRequest request,Integer id) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("id",id);
		view.setViewName("program/programDetail");
		return view;
	}
}
