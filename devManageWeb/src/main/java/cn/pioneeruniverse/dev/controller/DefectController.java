package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 
* @ClassName: DefectController
* @Description: 开发缺陷
* @author author
* @date 2020年8月11日 下午8:51:23
*
 */
@RestController
@RequestMapping("defect")
public class DefectController extends BaseController {
	@Value("${requirement.att.url}")
	private String reqAttUrl;

	/**
	 * 
	* @Title: toDefect
	* @Description: 开发缺陷首页
	* @author author
	* @param request
	* @return
	* @throws
	 */
    @RequestMapping(value = "toDefect",method = RequestMethod.GET)
    public ModelAndView toDefect(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("userId",CommonUtil.getCurrentUserId(request));
        view.addObject("userName", CommonUtil.getCurrentUserName(request));
        String defectStatusList = request.getParameter("defectStatusList");
		view.addObject("defectStatusList", defectStatusList);
        view.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
        view.addObject("reqAttUrl",reqAttUrl);
        view.setViewName("defectManagement/defectManagement");
        return view;
    }

}
