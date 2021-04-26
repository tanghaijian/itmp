package cn.pioneeruniverse.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * @deprecated
 * @Author: 
 * @Description:
 * @Date: Created in 16:13 2019/10/25
 * @Modified By:
 */
@RestController
@RequestMapping("deliveryChart")
public class DeliveryChartController {
	
	/**
	 * 
	* @Title: toDeliveryChart
	* @Description: 废弃，移至projectManageui/deliveryChart/toDeliveryChart
	* @author author
	* @param request
	* @return
	* @return ModelAndView
	 */
	@RequestMapping("toDeliveryChart")
	public ModelAndView toDeliveryChart(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.setViewName("deliveryChart/deliveryChart");
		System.out.println("xxxx");
		return view;
	}
}
