package cn.pioneeruniverse.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.CommonUtils;
/**
 *
 * @ClassName: NoticeController
 * @Description: 系统公告类
 * @author author
 *
 */
@RestController
@RequestMapping("notice")
public class NoticeController {
	
	/**
	 * 
	* @Title: toNoticeManage
	* @Description: 公告管理首页
	* @author author
	* @param request
	* @param projectIds 选择的项目IDS，如1，2，3
	* @param type 1系统公告，2项目公告
	* @return
	* @throws
	 */
	@RequestMapping("toNoticeManage")
	public ModelAndView toNoticeManage(HttpServletRequest request, String projectIds, String type) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		Boolean flag = new CommonUtils().currentUserWithAdmin(request);
		view.addObject("manageFlag", flag);
		view.addObject("projectIds", projectIds);
		view.addObject("type", type);
		view.setViewName("notice/noticeManage");
		return view;
	}
	
	/**
	 * 
	* @Title: toNoticeDetail
	* @Description: 公告详情
	* @author author
	* @param request
	* @param noticeId 公告ID
	* @return
	* @throws
	 */
	@RequestMapping("toNoticeDetail")
	public ModelAndView toNoticeDetail(HttpServletRequest request, Long noticeId) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("noticeId", noticeId);
		view.setViewName("notice/noticeDetail");
		return view;
	}

}
