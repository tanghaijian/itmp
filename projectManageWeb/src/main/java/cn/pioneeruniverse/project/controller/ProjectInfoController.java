package cn.pioneeruniverse.project.controller;

import java.util.Map;

import cn.pioneeruniverse.project.feignInterface.ProjectManageWebToProjectManageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName: ProjectInfoController
 * @Description: (项目模块)
 * @author author
 * @date Jul 28, 2020 11:33:07 AM
 *
 */
@RestController
@RequestMapping("project")
public class ProjectInfoController {

	@Autowired
	private ProjectManageWebToProjectManageInterface projectInfoInterface;

	/**
	 *
	 * @Title: toProject
	 * @Description: 项目主页跳转
	 * @author author
	 * @return
	 */
	@RequestMapping("toProject")
	public ModelAndView toProject() {
		ModelAndView view = new ModelAndView();
		view.setViewName("project/projectManage");
		return view;
	}

	/**
	 *
	 * @Title: toProjectDetails
	 * @Description: 详情页面跳转
	 * @author author
	 * @param id
	 * @return
	 */
	@RequestMapping("toProjectDetails")
	public ModelAndView toProjectDetails(Long id) {
		ModelAndView view = new ModelAndView();
		Map<String, Object> map = projectInfoInterface.selectProjectById(id);
		view.addObject(map);
		view.setViewName("");
		return view;
	}
}
