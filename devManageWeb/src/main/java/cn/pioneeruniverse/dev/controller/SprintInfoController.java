package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * 
* @ClassName: SprintInfoController
* @Description: 冲刺管理
* @author author
* @date 2020年8月11日 下午9:18:23
*
 */
@RestController
@RequestMapping("sprintManageui")
public class SprintInfoController {

	/**
	 * 
	* @Title: toSprint
	* @Description: 冲刺首页
	* @author author
	* @param request
	* @param projectId 项目ID
	* @param projectName 项目名称
	* @param systemId 系统ID
	* @param systemName 系统名称
	* @return
	* @throws
	 */
	@RequestMapping(value = "toSprint")
	public ModelAndView toSprint(HttpServletRequest request, Long projectId, String projectName, String systemId, String systemName) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("uid", CommonUtil.getCurrentUserId(request));
		view.addObject("projectId", projectId);
		view.addObject("projectName",projectName);
		view.addObject("systemId", systemId);
		view.addObject("systemName", systemName);
		view.setViewName("sprint/sprint");
		return view;
	}
}
