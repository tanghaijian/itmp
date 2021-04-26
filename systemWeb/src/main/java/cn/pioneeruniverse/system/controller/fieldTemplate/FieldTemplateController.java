package cn.pioneeruniverse.system.controller.fieldTemplate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
* @ClassName: FieldTemplateController
* @Description: 自定义属性配置页面跳转controller
* @author author
* @date 2020年9月3日 下午2:52:01
*
 */
@RestController
@RequestMapping("fieldTemplate")
public class FieldTemplateController {

	/**
	 * 
	* @Title: RequirementManage
	* @Description: 系统信息管理-自定义属性配置
	* @author author
	* @return ModelAndView
	 */
    @RequestMapping(value="toFieldTemplate")
    public ModelAndView RequirementManage(){
        ModelAndView view = new ModelAndView();
        view.setViewName("fieldTemplate/fieldTemplate");
        return view;
    }
}