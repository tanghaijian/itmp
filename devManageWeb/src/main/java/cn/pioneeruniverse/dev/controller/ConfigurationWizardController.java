package cn.pioneeruniverse.dev.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.controller.BaseController;


/**
 * 
* @ClassName: ConfigurationWizardController
* @Description: 配置向导controller
* @author author
* @date 2020年8月11日 下午8:47:09
*
 */
@RestController
@RequestMapping("configurationWizard")
public class ConfigurationWizardController extends BaseController{

	/**
	 * 
	* @Title: toConfigurationWizard
	* @Description: 配置向导首页
	* @author author
	* @return
	* @throws
	 */
	@RequestMapping(value = "toConfigurationWizard")
	public ModelAndView toConfigurationWizard() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("configurationWizard/configurationWizard");
		return modelAndView;
	}
}
