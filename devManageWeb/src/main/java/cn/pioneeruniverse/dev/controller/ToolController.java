package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * 
* @ClassName: ToolController
* @Description: 工具配置
* @author author
* @date 2020年8月11日 下午9:24:09
*
 */
@RestController
@RequestMapping("development")
public class ToolController extends BaseController {

	/**
	 * 
	* @Title: toSystemInformation
	* @Description: 工具配置首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
    @RequestMapping("toTool")
    public ModelAndView toSystemInformation(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.setViewName("tool/toolInformationConfiguration");
        return view;
    }
}
