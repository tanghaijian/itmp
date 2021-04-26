package cn.pioneeruniverse.project.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
/**
 *
 * @ClassName:ReportController
 * @Description:报表控制类
 * @author author
 * @date 2020年8月16日
 *
 */
@RestController
public class ReportController {

	/**
	 * 
	* @Title: toDefectReport
	* @Description: 报表中心-缺陷报表
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping(value = "/toDefectReport", method = RequestMethod.GET)
	public ModelAndView toDefectReport(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("report/defectReport");
		return view;
	}
	
	/**
	 * 
	* @Title: toDevWorkReport
	* @Description: 报表中心-开发任务报表
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping(value = "/toDevWorkReport", method = RequestMethod.GET)
	public ModelAndView toDevWorkReport(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("report/devWorkReport");
		return view;
	}
	
	
	/**
	 * 
	* @Title: toTestWorkReport
	* @Description: 报表中心-测试任务报表
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping(value = "/toTestWorkReport", method = RequestMethod.GET)
	public ModelAndView toTestWorkReport(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("report/testWorkReport");
		return view;
	}
	
	/**
	 * 
	* @Title: toWorkWeekReport
	* @Description: 报表中心-任务交付累计流图
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping(value = "/toWorkWeekReport", method = RequestMethod.GET)
	public ModelAndView toWorkWeekReport(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("weekReport/workWeekReport");
		return view;
	}
	
	/**
	 * 
	* @Title: toDefectInfoReport
	* @Description: 报表中心-缺陷统计图
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping(value = "/toDefectInfoReport", method = RequestMethod.GET)
	public ModelAndView toDefectInfoReport(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("weekReport/defectInfoReport");
		return view;
	}
	
	
	/**
	 * 
	* @Title: toDefectResolvedReport
	* @Description: 报表中心-缺陷待解决统计图
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping(value = "/toDefectResolvedReport", method = RequestMethod.GET)
	public ModelAndView toDefectResolvedReport(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("weekReport/defectResolvedReport");
		return view;
	}
}
