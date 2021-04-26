package cn.pioneeruniverse.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
/**
 *
 * @ClassName: TestSetController
 * @Description: 测试案例
 * @author author
 * @date 2020年8月26日 15:12:25
 *
 */
@RestController
@RequestMapping("testSet")
public class TestSetController {

	@RequestMapping("toTestSet")
	public ModelAndView  toTestSet(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("testTaskId", request.getParameter("testTaskId"));
		view.addObject("workTaskId", request.getParameter("workTaskId"));
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("testSet/testSetList");
		return view;
	}
	@RequestMapping("toTestSetTable")
	public ModelAndView  toTestSetTable(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("testTaskId", request.getParameter("testTaskId"));
		view.addObject("workTaskId", request.getParameter("workTaskId"));
		view.addObject("uid", CommonUtil.getCurrentUserId(request));
		view.addObject("username", CommonUtil.getCurrentUserName(request));
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("testSet/testSetTable");
		return view;
	}
	@RequestMapping("toTestSetCase")
	public ModelAndView  toTestSetCase(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		String testSetId = request.getParameter("testSetId");
		view.addObject("testSetId",testSetId);
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("testSet/testSetCaseModal");
		return view;
	}
}
