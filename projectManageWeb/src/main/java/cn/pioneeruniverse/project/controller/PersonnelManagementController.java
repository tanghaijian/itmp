package cn.pioneeruniverse.project.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @ClassName: DeliveryChartController
 * @Description: 人员管理
 * @author author
 * @date 2020年8月25日 15:09:43
 *
 */
@RestController
@RequestMapping("personnelManagement")
public class PersonnelManagementController {

	
	/**
	 * 
	* @Title: toPersonnelManagement
	* @Description: 项目管理-人员管理
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping("toPersonnelManagement")
	public ModelAndView toPersonnelManagement(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url", Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("personnelManagement/personnelManagement");
		return view;
	}
}
