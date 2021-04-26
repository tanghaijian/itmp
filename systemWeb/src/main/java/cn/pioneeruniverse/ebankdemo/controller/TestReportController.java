package cn.pioneeruniverse.ebankdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:43 2019/2/12
 * @Modified By:
 */
@RestController
@RequestMapping("testReport")
public class TestReportController {

    @RequestMapping("toReport")
    public ModelAndView toReport() {
        ModelAndView view = new ModelAndView("ebankdemo/testReport");
        return view;
    }

    @RequestMapping("toWord")
    public ModelAndView toWord() {
        ModelAndView view = new ModelAndView("ebankdemo/wordShow");
        return view;
    }


}
