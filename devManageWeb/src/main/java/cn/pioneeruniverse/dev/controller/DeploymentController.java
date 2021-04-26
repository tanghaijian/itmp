package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * 
* @ClassName: DeploymentController
* @Description: 部署
* @author author
* @date 2020年8月11日 下午8:53:03
*
 */
@RestController
@RequestMapping("deploy")
public class DeploymentController {

	/**
	 * 
	* @Title: toStructureManage
	* @Description: 部署首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping(value = "toDeploy")
	public ModelAndView toStructureManage(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		 view.addObject("currentUserId", CommonUtil.getCurrentUserId(request));
		 view.addObject("token", CommonUtil.getToken(request));
		view.setViewName("deployment/deployment");
		return view;
	}
}
