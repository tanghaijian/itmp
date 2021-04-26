package cn.pioneeruniverse.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 *
 * @ClassName: DeliveryChartController
 * @Description: 运维类项目
 * @author author
 * @date 2020年8月24日 15:09:43
 *
 */
@RestController
@RequestMapping("oamproject")
public class OamProjectInfoController {
	

	/**
	 * 
	* @Title: toOamProject
	* @Description: 运维类项目首页
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping("toOamProject")
	public ModelAndView toOamProject(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("project/oamProjectManage");
		return view;
	}
	/**
	 * 
	* @Title: toEditProject
	* @Description: 跳转到详情或者编辑页面带详情数据（同一个页面）
	* @author author
	* @param id 项目id
	* @param type type=1,是编辑,type=2,是查看 
	* @param home 1从项目管理中的团队进入，空从项目主页进入
	* @return ModelAndView
	 */
	@RequestMapping("toEditProject")
	public ModelAndView toEditProject(Long id,String type,Long home) {
		ModelAndView view = new ModelAndView();
		view.addObject("id", id);
		view.addObject("type", type);
		view.addObject("home", home);
		view.setViewName("project/editProject");
		return view;
	}


	/**
	 * 
	* @Title: toRisk
	* @Description: 运维类项目-风险管理
	* @author author
	* @param id 项目ID
	* @param name 项目名称
	* @param request
	* @return
	 */
	@RequestMapping("toRisk")
	public ModelAndView toRisk(Long id,String name,HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("id", id);
		view.addObject("name", name);
		view.addObject("toProjectType", 1);//项目类型，1运维类，2新建类
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("userId", CommonUtil.getCurrentUserId(request));
		view.addObject("userName", CommonUtil.getCurrentUserName(request));
		view.setViewName("newProject/riskManage");
		return view;
	}
	
	/**
	 * @deprecated
	* @Title: toMenuRisk
	* @Description: 过期不用
	* @author author
	* @param id
	* @param name
	* @param request
	* @return
	 */
	@RequestMapping("toMenuRisk")
	public ModelAndView toMenuRisk(Long id,String name,HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("id", id);
		view.addObject("name", name);
		view.addObject("toProjectType", 1);
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("userId", CommonUtil.getCurrentUserId(request));
		view.addObject("userName", CommonUtil.getCurrentUserName(request));
		view.addObject("entryType", 1);
		view.setViewName("newProject/riskManage");
		return view;
	}
	
	/**
	 * 
	* @Title: toWiki
	* @Description: 运维类项目-wiki
	* @author author
	* @param id 项目ID
	* @param projectName 项目名称
	* @param request
	* @return
	 */
	@RequestMapping("toWiki")
	public ModelAndView toWiki(Long id,String projectName,HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("id", id);
		view.addObject("projectName", projectName);
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("project/wiki");
		return view;
	}
}
