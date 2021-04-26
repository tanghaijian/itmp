package cn.pioneeruniverse.system.controller.dic;


import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 
* @ClassName: DataDicController
* @Description: 数据字典管理页面controller
* @author author
* @date 2020年8月19日 下午9:26:45
*
 */
@RestController
@RequestMapping("dataDic")
public class DataDicController {


	/**
	 * 
	* @Title: toDicManage
	* @Description: 数据字典首页
	* @author author
	* @param request
	* @return
	 */
    @RequestMapping(value = "toDicManage")
    public ModelAndView toDicManage(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("url",Constants.SYSTEM_MANAGE_UI_URL+request.getRequestURI());
        view.setViewName("systemManagement/dataDictionaryManagement");
        return view;
    }

    /**
     * 
    * @Title: toDataDicModal
    * @Description: 新建/编辑数据字典
    * @author author
    * @param request
    * @return
    * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "toDataDicModal")
    public ModelAndView toDataDicModal(HttpServletRequest request) throws UnsupportedEncodingException {
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.addObject("type", request.getParameter("type"));
        view.addObject("termCode", URLDecoder.decode(request.getParameter("termCode"), "UTF-8"));
        view.addObject("termName", URLDecoder.decode(request.getParameter("termName"), "UTF-8"));
        view.setViewName("systemManagement/dataDictionaryEdit");
        return view;
    }
}
