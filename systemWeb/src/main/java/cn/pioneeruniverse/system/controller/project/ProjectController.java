package cn.pioneeruniverse.system.controller.project;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
/**
 *@deprecated
 * @ClassName: ProjectController
 * @Description: 项目Controller ,废弃：具体项目管理请参考运维类项目管理：OamProjectController新建类项目管理：NewProjectController
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
@RestController
@RequestMapping("project")
public class ProjectController {
	/**
	 * 
	* @Title: toDeptManage
	* @Description: 
	* @author author
	* @return ModelAndView
	 */
	@RequestMapping("toProjectManage")
	public ModelAndView  toDeptManage(){
		ModelAndView view = new ModelAndView();
		view.setViewName("projectManagement/projectManagement");
		return view;
	}
	
	@RequestMapping("toDemand")
	public ModelAndView  todemand(){
		ModelAndView view = new ModelAndView();
		view.setViewName("projectManagement/demandManagement");
		return view;
	}
	
	@RequestMapping("toScheduling")
	public ModelAndView  toScheduling(){
		ModelAndView view = new ModelAndView();
		view.setViewName("projectManagement/schedulingManagement");
		return view;
	}
	
	
}
