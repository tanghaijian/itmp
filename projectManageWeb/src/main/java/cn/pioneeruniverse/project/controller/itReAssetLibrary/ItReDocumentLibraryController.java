package cn.pioneeruniverse.project.controller.itReAssetLibrary;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.utils.CommonUtil;


/**
 * @deprecated
* @ClassName: ItReDocumentLibraryController
* @Description: 未用
* @author author
* @date 2020年8月31日 下午2:31:58
*
 */
@RestController
@RequestMapping("itReDocumentLibrary")
public class ItReDocumentLibraryController {

	@RequestMapping(value = "/toItReDocument", method = RequestMethod.GET)
	public ModelAndView toAssociatedDemand(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("currentUserId", CommonUtil.getCurrentUserId(request));
		view.addObject("token", CommonUtil.getToken(request));
		view.setViewName("assetsLibrary/associatedDemand");
		return view;
	}
	

}
