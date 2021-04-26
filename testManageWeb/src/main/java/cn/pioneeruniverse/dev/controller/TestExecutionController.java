package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: 测试执行管理 前台
 * Author:liushan
 * Date: 2019/1/21 下午 4:15
 */
@RestController
@RequestMapping("testExecution")
public class TestExecutionController {

    /**
     * 测试案例管理主页面
     * @param request
     * @return
     */
    @RequestMapping(value = "toTestCase",method = RequestMethod.GET)
    public ModelAndView toTestCase(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testManagement/testCaseManagement");
        return view;
    }
    
   


    /**
     * 测试执行管理主页面
     * @param request
     * @return
     */
    @RequestMapping(value = "toTestCaseRun",method = RequestMethod.GET)
    public ModelAndView toTestCaseRun(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
        view.addObject("testSetName",request.getParameter("testSetName"));
        view.addObject("testTaskName",request.getParameter("testTaskName"));
        view.setViewName("testExecutionManagement/testExecutionManagement");
        return view;
    }

    /**
     * 测试案例执行记录页面
     * @param request
     * @return
     */
    @RequestMapping(value = "toTestCaseExecutionResult",method = RequestMethod.GET)
    public ModelAndView toTestCaseExecutionResult(@RequestParam(value = "testSetId") Long testSetId, @RequestParam(value = "caseNumber") String caseNumber,HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("testSetId", testSetId);
        view.addObject("caseNumber", caseNumber);
        view.addObject("refreshtoType", 1);
        view.setViewName("testExecutionManagement/testCaseExecutionResult");
        return view;
    }

    /**
     * 测试案例执行案例详细信息页面
     * @param request
     * @return
     */
    @RequestMapping(value = "toCheckTestCaseExecution",method = RequestMethod.GET)
    public ModelAndView toCheckTestCaseExecution(@RequestParam(value = "testCaseId") Long testCaseId, @RequestParam(value = "testSetId") Long testSetId,HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("testCaseId", testCaseId);
        view.addObject("testSetId", testSetId);
        view.addObject("refreshtoType", 2);
        view.setViewName("testExecutionManagement/checkTestCaseExecution");
        return view;
    }
}
