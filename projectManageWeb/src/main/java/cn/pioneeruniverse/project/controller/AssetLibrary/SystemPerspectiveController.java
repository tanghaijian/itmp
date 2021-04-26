package cn.pioneeruniverse.project.controller.AssetLibrary;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @deprecated
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:13 2019/11/15
 * @Modified By:
 */
@RestController
@RequestMapping("assetLibrary/systemPerspective")
public class SystemPerspectiveController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView systemPerspective() {
        ModelAndView view = new ModelAndView();
        view.setViewName("assetsLibrary/assetsLibrarySystem/assetsLibrarySystem");
        return view;
    }

}

