package cn.pioneeruniverse.system.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
* @ClassName: ErrorController
* @Description: 异常页面跳转
* @author author
* @date 2020年9月3日 下午3:05:17
*
 */
@RestController
@RequestMapping("error")
public class ErrorController {

	Logger log = LoggerFactory.getLogger(ErrorController.class);
	
	/**
	 * 
	* @Title: to404Page
	* @Description: 404页面
	* @author author
	* @return
	* @return ModelAndView
	 */
	@RequestMapping(value="/404")
	public ModelAndView  to404Page(){
		ModelAndView model = new ModelAndView();
		model.setViewName("error/404");
		return model; 
	}
	
	/**
	 * 
	* @Title: to500Page
	* @Description: 500页面
	* @author author
	* @return
	* @return ModelAndView
	 */
	@RequestMapping(value="/500")
	public ModelAndView  to500Page(){
		log.info("ErrorController.to500Page");
		ModelAndView model = new ModelAndView();
		model.setViewName("error/500");
		return model; 
	}
	
	
	/**
	 * 
	* @Title: to400Page
	* @Description: 400页面
	* @author author
	* @return
	* @return ModelAndView
	 */
	@RequestMapping(value="/400")
	public ModelAndView  to400Page(){
		ModelAndView model = new ModelAndView();
		model.setViewName("error/400");
		return model; 
	}
}
