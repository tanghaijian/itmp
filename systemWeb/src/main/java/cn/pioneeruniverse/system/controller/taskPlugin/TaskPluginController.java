package cn.pioneeruniverse.system.controller.taskPlugin;

import cn.pioneeruniverse.common.utils.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:  eclipse插件
 * Author:liushan
 * Date: 2020/6/8 下午 2:02
 */
@RestController
@RequestMapping("taskPlugin")
public class TaskPluginController {

	/**
	 * 
	* @Title: toHandBook
	* @Description: 系统信息管理-eclipse插件上传
	* @author author
	* @param request
	* @return ModelAndView
	 */
    @RequestMapping(value = "toTaskPlugin")
    public ModelAndView toHandBook(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        //type=1:操作手册上传，2：eclipse插件上传
        view.addObject("type", 2);
        view.addObject("token", CommonUtil.getToken(request));
        view.setViewName("handBook/handBook");
        return view;
    }
}
