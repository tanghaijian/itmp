package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;


/**
 * 
* @ClassName: DisplayBoardController
* @Description: 
* @author author
* @date 2020年8月11日 下午9:03:59
*
 */
@RestController
@RequestMapping("displayboard")
public class DisplayBoardController extends BaseController{
	@Value("${requirement.att.url}")
	private String reqAttUrl;
	
	/**
	 * 
	* @Title: toDisPlayBoard
	* @Description: 个人看板
	* @author author
	* @param request
	* @param id
	* @return
	* @throws
	 */
	@RequestMapping("toDisPlayBoard")
	public ModelAndView toDisPlayBoard(HttpServletRequest request, Long id) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("reqAttUrl",reqAttUrl);
		view.setViewName("displayBoard/displayBoard");
		view.addObject("id", id);
		return view;
	}

}
