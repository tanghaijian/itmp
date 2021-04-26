package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 
* @ClassName: DatabaseManageController
* @Description: 
* @author author
* @date 2020年8月11日 下午8:49:35
*
 */
@RestController
@RequestMapping("database")
public class DatabaseManageController {

	/**
	 * 
	* @Title: toConstruction
	* @Description: 数据库配置首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
	@RequestMapping("toDataBase")
	public ModelAndView  toConstruction(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("dataBase/dataBase");
		return view;
	}
}
