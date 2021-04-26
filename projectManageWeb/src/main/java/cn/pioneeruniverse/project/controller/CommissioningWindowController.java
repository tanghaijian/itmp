package cn.pioneeruniverse.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * 
* @ClassName: CommissioningWindowController
* @Description: 投产窗口：排期controller
* @author author
* @date 2020年8月19日 下午9:09:43
*
 */
@RestController
@RequestMapping("commissioningWindow")
public class CommissioningWindowController {

	/**
	 * 
	* @Title: toOamProject
	* @Description: 排期首页
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping("toCommissioningWindow")
	public ModelAndView toOamProject(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("project/commissioningWindow");
		return view;
	}
	
}
