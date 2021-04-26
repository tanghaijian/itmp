package cn.pioneeruniverse.project.controller.AssetLibrary;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:26 2019/11/12
 * @Modified By:
 */
@RestController
@RequestMapping("assetLibraryNew")
public class CommonModalController {

	/**
	 * @deprecated
	* @Title: systemChooseModal
	* @Description: 未用
	* @author author
	* @return
	 */
    @RequestMapping(value = "systemChooseModal", method = RequestMethod.GET)
    public ModelAndView systemChooseModal() {
         ModelAndView view = new ModelAndView();
         view.setViewName("assetsLibrary/chooseSystem");
         return view;
    } 

}
