package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.feignInterface.TestManageWebToSystemInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.feignInterface.TestManageWebToTestManageInterface;


/**
* 测试任务前台Controller 
* @author:tingting
* @version:2018年11月12日 上午11:25:38 
*/

@RestController
@RequestMapping("testtask")
public class TestTaskController extends BaseController{
	@Autowired
	private TestManageWebToTestManageInterface testTaskInterface;
	@Autowired
	private TestManageWebToSystemInterface userInterface;
	@Value("${requirement.att.url}")
	private String reqAttUrl;
	@Autowired
	private RedisUtils redisUtils;
	
	
	@RequestMapping(value = "toTestTask")
	public ModelAndView toDevTaskPage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		LinkedHashMap map = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
		List<String> roleCodes = (List<String>) map.get("roles"); 
		modelAndView.addObject("systemName", request.getParameter("systemName"));
		modelAndView.addObject("windowDate", request.getParameter("windowDate"));
		modelAndView.addObject("token", CommonUtil.getToken(request));
		modelAndView.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
		modelAndView.addObject("uid",CommonUtil.getCurrentUserId(request));
		modelAndView.addObject("username",CommonUtil.getCurrentUserName(request));
		modelAndView.addObject("reqAttUrl",reqAttUrl);
		modelAndView.addObject("roleCodes",roleCodes);
		modelAndView.setViewName("testtask/testtaskList");
		return modelAndView;
	}
	
	@RequestMapping(value="getData",method = RequestMethod.POST)
	public Map<String, Object> getData(Long id){
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> map = testTaskInterface.toAddData();
			//List<TblDeptInfo> depts = userInterface.getDept();
			//result.put("depts", depts);
			result.putAll(map);
			if (id!=null && !"".equals(id)) {
				Map<String, Object> devtask = testTaskInterface.getOneDevTask(id);
				result.putAll(devtask);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
		}
		
		return result;
		
	}
	
	@GetMapping(value = "toTestTaskInfo")
	public ModelAndView toTestTaskInfo(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("token", CommonUtil.getToken(request));
		modelAndView.addObject("reqAttUrl",reqAttUrl);
		modelAndView.setViewName("testtask/testTaskInfo");
		return modelAndView;
	}

}
