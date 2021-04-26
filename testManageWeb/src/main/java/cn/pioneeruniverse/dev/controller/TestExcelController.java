package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 *
 * @ClassName: TestExcelController
 * @Description:
 * @author author
 * @date 2020年8月26日 15:12:25
 *
 */
@RestController
@RequestMapping("testReport")
public class TestExcelController {
	@RequestMapping(value = "toReport",method = RequestMethod.GET)
    public ModelAndView toTestExcel(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("exportTestExcel/exportTestExcel");
        return view;
    }
	
	@RequestMapping(value = "toDefectProReport",method = RequestMethod.GET)
    public ModelAndView toDefectProReport(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/defectProReport");
        return view;
    }
	
	@RequestMapping(value = "toDefectProReportSample",method = RequestMethod.GET)
    public ModelAndView toDefectProReportSample(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("timeRange", request.getParameter("timeRange"));
        view.addObject("systemIds", request.getParameter("systemIds"));
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/sample/defectProReportSample");
        return view;
    }
	
	@RequestMapping(value = "toDevVersionReport",method = RequestMethod.GET)
    public ModelAndView toDevVersionReport(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/devVersionReport");
        return view;
    }
	
	@RequestMapping(value = "toDevVersionReportSample",method = RequestMethod.GET)
    public ModelAndView toDevVersionReportSample(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("time", request.getParameter("time"));
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/sample/devVersionReportSample");
        return view;
    }
	
	@RequestMapping(value = "toWorseSystemReport",method = RequestMethod.GET)
    public ModelAndView toWorseSystemReport(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/worseSystemReport");
        return view;
    }
	
	@RequestMapping(value = "toWorseSystemReportSample",method = RequestMethod.GET)
    public ModelAndView toWorseSystemReportSample(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("time", request.getParameter("time"));
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/sample/worseSystemReportSample");
        return view;
    }
	
	@RequestMapping(value = "toDefectTotalReport",method = RequestMethod.GET)
    public ModelAndView toDefectTotalReport(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/defectTotalReport");
        return view;
    }
	
	@RequestMapping(value = "toDefectTotalReportSample",method = RequestMethod.GET)
    public ModelAndView toDefectTotalReportSample(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("time", request.getParameter("time"));
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/sample/defectTotalReportSample");
        return view;
    }
	
	@RequestMapping(value = "toDefectLevelReport",method = RequestMethod.GET)
    public ModelAndView toDefectLevelReport(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/defectLevelReport");
        return view;
    }
	
	@RequestMapping(value = "toDefectLevelReportSample",method = RequestMethod.GET)
    public ModelAndView toDefectLevelReportSample(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("time", request.getParameter("time"));
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testReport/sample/defectLevelReportSample");
        return view;
    }
}
