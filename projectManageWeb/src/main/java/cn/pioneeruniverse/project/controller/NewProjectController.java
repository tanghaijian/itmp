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
 * @ClassName: DeliveryChartController
 * @Description: 新建类项目
 * @author author
 * @date 2020年8月24日 15:09:43
 *
 */
@RestController
@RequestMapping("newProject")
public class NewProjectController {
	
	/**
	 * 
	* @Title: toNewProject
	* @Description: 新建项目类首页
	* @author author
	* @param request
	* @param name 项目名称，在由项目链接跳转弹窗时，传入名称直接显示到页面
	* @return
	 */
	@RequestMapping("toNewProject")
	public ModelAndView toNewProject(HttpServletRequest request,String name) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("name",name);
		view.setViewName("newProject/newProject");
		return view;
	}

	/**
	 * 
	* @Title: toNewProjectDetail
	* @Description: 新建类项目-管理 链接跳转到项目管理主页
	* @author author
	* @param request
	* @param id 项目ID
	* @param type 开发模式1敏捷，2稳态
	* @return
	 */
	@RequestMapping("toNewProjectDetail")
	public ModelAndView toNewProjectDetail(HttpServletRequest request,Integer id,Integer type) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("id",id);
		view.addObject("type",type);
		view.setViewName("newProject/newProjectDetail");
		return view;
	}

	/**
	 * 
	* @Title: toPlanChart
	* @Description: 新建类项目-管理-项目计划链接页面
	* @author author
	* @param request
	* @param id 项目ID
	* @param name 项目名称
	* @param requestUserId  项目计划变更申请人
	* @return
	 */
	@RequestMapping("toPlanChart")
	public ModelAndView toPlanChart(HttpServletRequest request,Integer id, String name,Long requestUserId) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("id",id);
		//项目名
		view.addObject("name",name);
		//项目计划变更申请人
		view.addObject("requestUserId",requestUserId);
		view.setViewName("newProject/planChart");
		return view;
	}
	
	/**
	 * 
	* @Title: toPlanManage
	* @Description: 新建类项目-管理-项目计划-切换视图链接页面
	* @author author
	* @param request
	* @param id 项目ID
	* @param name 项目名
	* @param version 项目计划版本
	* @return
	 */
	@RequestMapping("toPlanManage")
	public ModelAndView toPlanManage(HttpServletRequest request,Integer id, String name,Integer version) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("id",id);
		view.addObject("name",name);
		view.addObject("version",version);
		view.setViewName("newProject/planManage");
		return view;
	}

	/**
	 * 
	* @Title: toRiskManage
	* @Description: 新建类项目-管理-风险管理
	* @author author
	* @param request
	* @param id 项目id
	* @param userId 用户id
	* @param userName 用户姓名
	* @param name 项目名
	* @param type  1运维类，2新建类
	* @param toProjectType 项目类型1运维类，2新建类
	* @return ModelAndView
	 */
	@RequestMapping("toRiskManage")
	public ModelAndView toRiskManage(HttpServletRequest request,Integer id, Long userId, String userName, String name, Integer type,Integer toProjectType) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("id",id);
		view.addObject("userId",userId);
		view.addObject("userName",userName);
		view.addObject("name",name);
		view.addObject("type",type);
		view.addObject("toProjectType",toProjectType);
		view.setViewName("newProject/riskManage");
		return view;
	}
	
	/**
	 * 
	* @Title: toUpdateManage
	* @Description: 新建类项目-管理-变更
	* @author author
	* @param request
	* @param id 项目ID
	* @param userId 用户ID
	* @param userName 姓名
	* @param name 项目名
	* @param type 项目类型
	* @return
	* @throws
	 */
	@RequestMapping("toUpdateManage")
	public ModelAndView toUpdateManage(HttpServletRequest request,Integer id, Long userId, String userName, String name, Integer type) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("id",id);
		view.addObject("userId",userId);
		view.addObject("userName",userName);
		view.addObject("name",name);
		view.addObject("type",type);
		view.setViewName("newProject/updateManage");
		return view;
	}
	
	/**
	 * 
	* @Title: toQuestionManage
	* @Description: 新建类项目-管理-问题链接
	* @author author
	* @param request
	* @param id  项目ID
	* @param userId 用户ID
	* @param userName 姓名
	* @param name 项目名称
	* @param type 项目类型
	* @return
	 */
	@RequestMapping("toQuestionManage")
	public ModelAndView toQuestionManage(HttpServletRequest request,Integer id, Long userId, String userName, String name, Integer type) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("id",id);
		view.addObject("userId",userId);
		view.addObject("userName",userName);
		//项目名
		view.addObject("name",name);
		view.addObject("type",type);
		view.setViewName("newProject/questionManage");
		return view;
	}
	
	/**
	 * 
	* @Title: toItPlantWide
	* @Description: 开放给IT全流程的文档链接
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping("toItPlantWide")
	public ModelAndView toItPlantWide(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("requirementCode", request.getParameter("reqCode"));
		view.addObject("taskCode", request.getParameter("taskCode"));
		view.addObject("token", request.getParameter("token"));
		view.setViewName("newProject/itPlantWide");
		return view;
	}
	
	/**
	 * 
	* @Title: toItEditAndCheckMarkDown
	* @Description: IT全流程文档编辑页面
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping("toItEditAndCheckMarkDown")
	public ModelAndView toItEditAndCheckMarkDown(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		/*view.addObject("currentUserAccount",request.getParameter("currentUserAccount"));
		view.addObject("cs_requirementCode",request.getParameter("cs_requirementCode"));*/
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("newProject/itEditAndCheckMarkDown");
		return view;
	}
	
	
	/**
	 * 
	* @Title: toDocPermission
	* @Description: 资产库权限编辑页面
	* @author author
	* @param request
	* @param projectId 项目ID
	* @return
	 */
	@RequestMapping(value = "/toDocPermission", method = RequestMethod.GET)
	public ModelAndView toDocPermission(HttpServletRequest request, Integer projectId) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("projectId",projectId);
		view.setViewName("newProject/docPermission");
		return view;
	}

}
