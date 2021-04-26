package cn.pioneeruniverse.project.controller.AssetLibrary;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:59 2019/12/31
 * @Modified By:
 */
@RestController
@RequestMapping("documentLibrary")
public class DocumentLibraryController {

	/**
	 * 
	* @Title: documentLibrary
	* @Description: 新建类项目-管理-文档 链接页面
	* @author author
	* @param request
	* @return
	 */
    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    public ModelAndView documentLibrary(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("id",  request.getParameter("id"));
        view.addObject("name",   request.getParameter("name"));
      
        view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
        view.setViewName("newProject/documentLibrary");
        
        return view;
    }

}
