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
* @ClassName: WorkTaskController
* @Description: 开发工作任务
* @author author
* @date 2020年8月11日 下午9:28:55
*
 */
@RestController
@RequestMapping("worktask")
public class WorkTaskController extends BaseController{
	
	
	/**
	 * 
	* @Title: toConstruction
	* @Description: 开发工作任务管理首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping("toWorktask")
	public ModelAndView  toConstruction(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("userId", CommonUtil.getCurrentUserId(request));
		view.addObject("userName", CommonUtil.getCurrentUserName(request));
		String devTaskStatus = request.getParameter("devTaskStatus");
		view.addObject("devTaskStatus", devTaskStatus);
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("worktask/worktaskManage");
		return view;
	}
	
	

}
