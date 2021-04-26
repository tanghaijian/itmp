package cn.pioneeruniverse.ebankdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:29 2019/2/12
 * @Modified By:
 */
@RestController
@RequestMapping("testProcess")
public class TestProcessController {

    @RequestMapping(value = "toRuleConfigure")
    public ModelAndView toRuleConfigure() {
        ModelAndView view = new ModelAndView("ebankdemo/ruleConfigure");
        return view;
    }


    @RequestMapping(value = {"processHandle", "processHandle/begin"})
    public ModelAndView toProcessHandle() {
        ModelAndView view = new ModelAndView("ebankdemo/processBegin");
        return view;
    }


    @RequestMapping(value = "processHandle/defineStrategy")
    public ModelAndView toDefineStrategy() {
        ModelAndView view = new ModelAndView("ebankdemo/processDefineStrategy");
        return view;
    }


    @RequestMapping(value = "processHandle/surveyAndPrepare")
    public ModelAndView toSurveyAndPrepare(String testType) {
        ModelAndView view = new ModelAndView("ebankdemo/processSurveyAndPrepare");
        view.addObject("testType", testType);
        return view;
    }


    @RequestMapping(value = "processHandle/execute")
    public ModelAndView toExecute(String testType) {
        ModelAndView view = new ModelAndView("ebankdemo/processExecute");
        view.addObject("testType", testType);
        return view;
    }


    @RequestMapping(value = "processHandle/end")
    public ModelAndView toEnd(String testType) {
        ModelAndView view = new ModelAndView("ebankdemo/processEnd");
        view.addObject("testType", testType);
        return view;
    }

}
