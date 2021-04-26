package cn.pioneeruniverse.system.controller.menu;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * 
* @ClassName: MenuController
* @Description: 菜单按钮管理
* @author author
* @date 2020年9月3日 下午2:55:17
*
 */
@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {

	/**
	 * 
	* @Title: toUserManage
	* @Description: 系统信息管理-菜单管理
	* @author author
	* @param request
	* @return
	* @return ModelAndView
	 */
    @RequestMapping(value = "toMenuManage")
    public ModelAndView toUserManage(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.setViewName("systemManagement/menuManagement");
        return view;
    }
}
