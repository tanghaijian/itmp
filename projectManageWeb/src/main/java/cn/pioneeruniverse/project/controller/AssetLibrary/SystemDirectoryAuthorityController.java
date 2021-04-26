package cn.pioneeruniverse.project.controller.AssetLibrary;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @deprecated
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:18 2019/12/5
 * @Modified By:
 */
@RestController
@RequestMapping("/assetLibrary/directoryAuthority")
public class SystemDirectoryAuthorityController {

	/**
	 * 
	* @Title: documentAuthority
	* @Description: 目录权限首页
	* @author author
	* @return ModelAndView
	 */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView documentAuthority() {
        ModelAndView view = new ModelAndView();
        view.setViewName("");
        return view;
    }


}
