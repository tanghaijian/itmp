package cn.pioneeruniverse.system.controller.handBook;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * 
* @ClassName: HandBookController
* @Description: 操作手册
* @author author
* @date 2020年9月3日 下午2:52:55
*
 */
@RestController
@RequestMapping("handBook")
public class HandBookController extends BaseController {

	/**
	 * 
	* @Title: toHandBook
	* @Description: 系统信息管理-操作手册上传
	* @author author
	* @param request
	* @return ModelAndView
	 */
	@RequestMapping(value = "toHandBook")
	public ModelAndView toHandBook(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		//type=1上传操作手册，2上传eclipse插件
		view.addObject("type", 1);
		view.addObject("token", CommonUtil.getToken(request));
		view.setViewName("handBook/handBook");
		return view;
	}
}
