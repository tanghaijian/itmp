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
 * Description:缺陷管理前端controller
 * Author:liushan
 * Date: 2018/12/10 下午 12:02
 */
@RestController
@RequestMapping("defect")
public class DefectController extends BaseController {
	@Value("${requirement.att.url}")
	private String reqAttUrl;

	/**
	*@author liushan
	*@Description 缺陷页面
	*@Date 2020/8/12
	 * @param request
	*@return org.springframework.web.servlet.ModelAndView
	**/
    @RequestMapping(value = "toDefect",method = RequestMethod.GET)
    public ModelAndView toDefect(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        String reqFiD = request.getParameter("reqFiD");
        String reqFName = request.getParameter("reqFName");
        String reqStatus = request.getParameter("reqStatus");
        view.addObject("reqFiD", reqFiD);// 开发任务id
        view.addObject("reqFName", reqFName);// // 开发任务状态开发任务名称
        view.addObject("reqStatus", reqStatus);
        view.addObject("systemName", request.getParameter("systemName"));// 系统名称
        view.addObject("windowDate", request.getParameter("windowDate"));// 投产窗口时间
        view.addObject("status", request.getParameter("status"));//状态
        view.addObject("workTaskId", request.getParameter("workTaskId"));// 工作任务id
        view.addObject("workTaskCode", request.getParameter("workTaskCode"));// 工作任务编号
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("userId",CommonUtil.getCurrentUserId(request));// 用户id
        view.addObject("userName", CommonUtil.getCurrentUserName(request));// 用户名称
        view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
        view.setViewName("defectManagement/defectManagement");
        return view;
    }

    /**
    *@author liushan
    *@Description 缺陷查看详情页面
    *@Date 2020/8/12
     * @param request
    *@return org.springframework.web.servlet.ModelAndView
    **/
    @RequestMapping(value = "toCheckDefect",method = RequestMethod.GET)
    public ModelAndView toCheckDefect(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("defectId", request.getParameter("defectId"));
        view.addObject("userId",CommonUtil.getCurrentUserId(request));
        view.addObject("userName", CommonUtil.getCurrentUserName(request));
        view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
        view.addObject("reqAttUrl",reqAttUrl);
        view.setViewName("defectManagement/checkDefect");
        return view;
    }


}
