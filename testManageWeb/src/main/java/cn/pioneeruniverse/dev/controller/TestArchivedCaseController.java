package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 *
 * @ClassName: TestArchivedCaseController
 * @Description: 归档案例
 * @author author
 * @date 2020年8月26日 15:12:25
 *
 */
@RestController
@RequestMapping("testArchivedCase")
public class TestArchivedCaseController {

	 /**
     * 归档案例管理
     * @param request
     * @return
     * wjdz
     * 2019年3月18日
     * 下午5:22:37
     */
    @RequestMapping(value = "toTestArchivedCase",method = RequestMethod.GET)
    public ModelAndView toTestArchivedCase(HttpServletRequest request) {
    	ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
    	view.addObject("url",Constants.TEST_MANAGE_UI_URL+request.getRequestURI());
    	view.addObject("uid",CommonUtil.getCurrentUserId(request));
    	view.addObject("username",CommonUtil.getCurrentUserName(request));
        view.setViewName("testManagement/testArchivedCaseManagement");
        return view;
    }
}
