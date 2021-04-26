package cn.pioneeruniverse.project.controller;

import cn.pioneeruniverse.common.utils.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * Author:liushan
 * Date: 2019/5/14 下午 2:00
 */
@RestController
@RequestMapping("business")
public class BusinessTreeController {


    /**
    *@author liushan
    *@Description 业务层级页面
    *@Date 2019/9/3
    *@Param [request]
    *@return org.springframework.web.servlet.ModelAndView
    **/
    @RequestMapping(value = "toBusiness",method = RequestMethod.GET)
    public ModelAndView toBusiness(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("userId",CommonUtil.getCurrentUserId(request));
        view.addObject("userName", CommonUtil.getCurrentUserName(request));
        view.setViewName("BusinessTree/AssetBusinessTree");
        return view;
    }

    /**
    *@author liushan
    *@Description 业务层级树页面
    *@Date 2019/9/3
    *@Param [request]
    *@return org.springframework.web.servlet.ModelAndView
    **/
    @RequestMapping(value = "toBusinessTree",method = RequestMethod.GET)
    public ModelAndView toBusinessTree(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("userId",CommonUtil.getCurrentUserId(request));
        view.addObject("userName", CommonUtil.getCurrentUserName(request));
        view.setViewName("BusinessTree/BusinessTree");
        return view;
    }

    /**
    *@author liushan
    *@Description 业务树维护（新的需求）
    *@Date 2019/9/3
    *@Param [request]
    *@return org.springframework.web.servlet.ModelAndView
    **/
    @RequestMapping(value = "toBusinessNewTree",method = RequestMethod.GET)
    public ModelAndView toBusinessNewTree(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("userId",CommonUtil.getCurrentUserId(request));
        view.addObject("userName", CommonUtil.getCurrentUserName(request));
        view.addObject("type",request.getParameter("type"));//1.业务树维护，2系统树维护
        view.setViewName("businessSystemTreeUpdate/BusinessSystemTreeUpdate");
        return view;
    }
}
