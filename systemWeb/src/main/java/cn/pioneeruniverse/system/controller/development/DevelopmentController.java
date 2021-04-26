package cn.pioneeruniverse.system.controller.development;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * @deprecated
* @ClassName: DevelopmentController
* @Description: 废弃
* @author author
* @date 2020年9月3日 下午2:50:35
*
 */
@RestController
@RequestMapping("development")
public class DevelopmentController {
	@RequestMapping("toConstruction")
	public ModelAndView  toConstruction(){
		ModelAndView view = new ModelAndView();
		view.setViewName("developmentManagement/constructionManagement");
		return view;
	}
	
	@RequestMapping("toDevelopment")
	public ModelAndView  toDevelopment(){
		ModelAndView view = new ModelAndView();
		view.setViewName("developmentManagement/developmentTaskManagement");
		return view;
	}
	
}
