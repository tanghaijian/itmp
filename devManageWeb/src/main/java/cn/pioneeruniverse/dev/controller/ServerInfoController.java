package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * 
* @ClassName: ServerInfoController
* @Description:服务器配置
* @author author
* @date 2020年8月11日 下午9:17:15
*
 */
@RestController
@RequestMapping("serverinfo")
public class ServerInfoController {
	
	/**
	 * 
	* @Title: toServerInfo
	* @Description: 服务器配置首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping("toServerInfo")
	public ModelAndView  toServerInfo(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.setViewName("serverinfo/serverinfo");
		return view;
	}
}
