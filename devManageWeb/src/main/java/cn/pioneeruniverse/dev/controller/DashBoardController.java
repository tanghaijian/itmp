package cn.pioneeruniverse.dev.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.controller.BaseController;


/**
 * 
* @ClassName: DashBoardController
* @Description: dashboard 的controller
* @author author
* @date 2020年8月11日 下午8:47:57
*
 */
@RestController
@RequestMapping("dashBoard")
public class DashBoardController extends BaseController{
	
	//需求附件url
	@Value("${requirement.att.url}")
	private String reqAttUrl;
	
	/**
	 * 
	* @Title: toWorkDesk
	* @Description: 个人工作台首页
	* @author author
	* @return
	* @throws
	 */
	@RequestMapping(value = "toWorkDesk")
	public ModelAndView toWorkDesk() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dashBoard/workDeskNew");
		modelAndView.addObject("reqAttUrl",reqAttUrl);
		return modelAndView;
	}
		
	/**
	 * 
	* @Title: toDashBoard
	* @Description: dashboard首页
	* @author author
	* @return
	* @throws
	 */
	@RequestMapping(value = "toDashBoard")
	public ModelAndView toDashBoard() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dashBoard/dashBoard");
		return modelAndView;
	}
}
