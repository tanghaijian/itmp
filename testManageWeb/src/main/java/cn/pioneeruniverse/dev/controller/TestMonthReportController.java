package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.feignInterface.TestManageWebToSystemInterface;
import cn.pioneeruniverse.dev.feignInterface.TestManageWebToTestManageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
* 测试任务前台Controller 
* @author:tingting
* @version:2018年11月12日 上午11:25:38 
*/

@RestController
@RequestMapping("testMonthReport")
public class TestMonthReportController extends BaseController{
	@Autowired
	private TestManageWebToTestManageInterface testTaskInterface;
	@Autowired
	private TestManageWebToSystemInterface userInterface;
	@Value("${requirement.att.url}")
	private String reqAttUrl;
	@Autowired
	private RedisUtils redisUtils;

	/**
	 *
	 * @Title: toTestMonthReport
	 * @Description: 报表中心-测试月报管理
	 * @author author
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toTestMonthReport", method = RequestMethod.GET)
	public ModelAndView toTestMonthReport(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("testMonthReport/testMonthReport");
		return view;
	}
	/**
	 *
	 * @Title: toTestMonthReport
	 * @Description: 报表中心-测试月报列表
	 * @author author
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toTestMonthList", method = RequestMethod.GET)
	public ModelAndView toTestMonthList(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("testMonthReport/testMonthList");
		return view;
	}
	/**
	 *
	 * @Title: toTestMonthReport
	 * @Description: 报表中心-测试月报统计
	 * @author author
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toTestMonthReportStatis", method = RequestMethod.GET)
	public ModelAndView toTestMonthReportStatis(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("uid",CommonUtil.getCurrentUserId(request));
		view.addObject("username",CommonUtil.getCurrentUserName(request));
		view.setViewName("testMonthReport/testMonthReportStatis");
		return view;
	}

}
