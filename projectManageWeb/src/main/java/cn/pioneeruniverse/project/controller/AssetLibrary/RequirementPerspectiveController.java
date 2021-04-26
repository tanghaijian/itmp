package cn.pioneeruniverse.project.controller.AssetLibrary;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:29 2019/11/11
 * @Modified By:
 */
@RestController
@RequestMapping("requirementPerspective")
public class RequirementPerspectiveController {

	/**
	 * 
	* @Title: toSystem
	* @Description: 资产库（需求视角），查询：涉及子系统弹框
	* @author author
	* @return
	 */
    @RequestMapping(value = "/toSystem", method = RequestMethod.GET)
    public ModelAndView toSystem() {
        ModelAndView view = new ModelAndView();
        view.setViewName("assetsLibrary/chooseSystem");
        return view;
    }
    
    /**
     * 
    * @Title: history
    * @Description: 资产库（需求视角）-右部分文档列表-历史版本链接地址
    * @author author
    * @return
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ModelAndView history() {
        ModelAndView view = new ModelAndView();
        view.setViewName("assetsLibrary/history");
        return view;
    }
    /**
     * 
    * @Title: relevanceInfo
    * @Description: 资产库（需求视角）-右部分文档列表-关联信息链接地址
    * @author author
    * @return
     */
    @RequestMapping(value = "/relevanceInfo", method = RequestMethod.GET)
    public ModelAndView relevanceInfo() {
        ModelAndView view = new ModelAndView();
        view.setViewName("assetsLibrary/relevanceInfo");
        return view;
    }

    /**
    *@author liushan
    *@Description MarkDown页面编辑和查看
    *@Date 2020/1/9
    *@Param []
    *@return org.springframework.web.servlet.ModelAndView
    **/
    @RequestMapping(value = "/toEditAndCheckMarkDown", method = RequestMethod.GET)
    public ModelAndView toEditAndCheckMarkDown() {
        ModelAndView view = new ModelAndView();
        view.setViewName("assetsLibrary/editAndCheckMarkDown");
        return view;
    }
}
