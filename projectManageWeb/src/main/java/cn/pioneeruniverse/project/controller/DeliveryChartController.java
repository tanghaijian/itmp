package cn.pioneeruniverse.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 *
 * @ClassName: DeliveryChartController
 * @Description:
 * @author author
 * @date 2020年8月24日 15:09:43
 *
 */
@RestController
@RequestMapping("deliveryChart")
public class DeliveryChartController {

	/**
	 * 
	* @Title: toDeliveryChart
	* @Description: 项目管理-交付视图
	* @author author
	* @param request
	* @return
	 */
	@RequestMapping("toDeliveryChart")
	public ModelAndView toDeliveryChart(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.setViewName("deliveryChart/deliveryChart");
		return view;
	}
}
