package cn.pioneeruniverse.dev.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.service.projectNotice.IProjectNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.entity.TblNoticeInfo;
import cn.pioneeruniverse.dev.service.notice.INoticeService;

/**
 * 系统公告类
 * @author zhoudu
 *
 */
@RestController
@RequestMapping("notice")
public class NoticeController extends BaseController {

	@Autowired
	private INoticeService iNoticeService;

    @Autowired
    private IProjectNoticeService iProjectNoticeService;


    /**
     * 根据id获取公告信息
     * @param id 公告id
     * @return Map<String,Object>
     */
    @RequestMapping(value="selectNoticeById",method=RequestMethod.POST)
	public Map<String,Object> selectNoticeById(Long id) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			TblNoticeInfo notice = iNoticeService.selectNoticeById(id);
			result.put("data", notice);
		}catch(Exception e){
			return this.handleException(e, "获取公告详情失败");
		}
		return result;
	}
	
    /**
     * 获取所以公告内容查询列表
     * @param request
     * @param notice 封装的查询
     * @param page 第几页
     * @param rows 每页数量
     * @param type 进入类型1新建项目类公告进入，2公告菜单进入
     * @param programId 项目id 如1，2，3
     * @return Map<String,Object>
     */
	@RequestMapping(value="getAllNotice",method=RequestMethod.POST)
	public Map<String,Object> getAllNotice(HttpServletRequest request, TblNoticeInfo notice, Integer page, Integer rows,Integer type,String programId) {

    	Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			if (StringUtil.isNotEmpty(notice.getCreateDateStr())) {//如果有时间内容，转换下时间封装
				String[] dateArr = notice.getCreateDateStr().split(" - ");
				Date date = DateUtil.parseDate(dateArr[0].trim());
				notice.setCreateStartDate(date);
				date = DateUtil.parseDate(dateArr[1].trim() + " 23:59:59", DateUtil.fullFormat);
				notice.setCreateEndDate(date);
			}
			if (StringUtil.isNotEmpty(notice.getValidDateStr())) {
				String[] dateArr = notice.getValidDateStr().split(" - ");
				Date date = DateUtil.parseDate(dateArr[0].trim());
				notice.setStartTime(date);
				date = DateUtil.parseDate(dateArr[1].trim());
				notice.setEndTime(date);
			}
			result = iNoticeService.getAllNotice(request, notice, page, rows, type, programId);
		}catch(Exception e){
			return this.handleException(e, "获取公告详情失败");
		}
		return result;
	}
	
	/**
	 * 插入公告
	 * @param request
	 * @param noticeJson TblNoticeInfo的json字符串格式
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="insertNotice",method=RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
	public Map<String,Object> insertNotice(HttpServletRequest request,@RequestBody String noticeJson) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			TblNoticeInfo notice = new TblNoticeInfo();
			if (StringUtil.isNotEmpty(noticeJson)) {
				notice = JSONObject.parseObject(noticeJson, TblNoticeInfo.class);
				if (StringUtil.isNotEmpty(notice.getNoticeContent()) && notice.getNoticeType() != null
						&& notice.getStartTime() != null && notice.getEndTime() != null) {//关键数据存在时才处理
					iNoticeService.insertNotice(request, notice);
                    if(notice.getNoticeType() == 2){//如果是项目公告时，额外处理
                        Long number = iProjectNoticeService.selectNoticeInfoByID(notice);
                        notice.setId(number);
                        iProjectNoticeService.insertProjectNotice(notice);
                        if (notice.getProjectIds() != null && notice.getProjectIds() != ""){
                            String str = notice.getProjectIds();
                            String[] projectType = str.split(",");
                            for (int i =0;i<projectType.length;i++){
                                notice.setProjectIds(projectType[i]);
                                iProjectNoticeService.insertProjectNotice(notice);
                            }
                        }
                    }
                   
				} else {
					result.put("status", Constants.ITMP_RETURN_FAILURE);
					result.put("errorMessage", "新增公告失败");
				}
			}
		}catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return this.handleException(e, "新增公告失败");
		}
		return result;
	}
	
	/**
	 * 更新公告
	 * @param request
	 * @param noticeJson
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="updateNotice",method=RequestMethod.POST)
	public Map<String,Object> updateNotice(HttpServletRequest request,@RequestBody String noticeJson) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			TblNoticeInfo notice = new TblNoticeInfo();
			if (StringUtil.isNotEmpty(noticeJson)) {
				notice = JSONObject.parseObject(noticeJson, TblNoticeInfo.class);
				if (StringUtil.isNotEmpty(notice.getNoticeContent()) && notice.getNoticeType() != null
						&& notice.getStartTime() != null && notice.getEndTime() != null && notice.getId() != null) {
					
					iNoticeService.updateNotice(request, notice);
				} else {
					result.put("status", Constants.ITMP_RETURN_FAILURE);
					result.put("errorMessage", "更新公告失败");
				}
			}
		}catch(Exception e){
			return this.handleException(e, "更新公告失败");
		}
		return result;
	}
	
	/**
	 * 删除公告
	 * @param request
	 * @param id
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="deleteNotice",method=RequestMethod.POST)
	public Map<String,Object> deleteNotice(HttpServletRequest request, Long id) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			if (id != null) {
				TblNoticeInfo notice = new TblNoticeInfo();
				notice.setId(id);
				iNoticeService.deleteNotice(request, notice);
			} else {
				result.put("status", Constants.ITMP_RETURN_FAILURE);
				result.put("errorMessage", "删除公告失败");
			}
		}catch(Exception e){
			return this.handleException(e, "删除公告失败");
		}
		return result;
	}
	
	/**
	 * 删除多个公告内容
	 * @param request
	 * @param ids
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="deleteNoticeList",method=RequestMethod.POST)
	public Map<String,Object> deleteNoticeList(HttpServletRequest request, String ids) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			if (StringUtil.isNotEmpty(ids)) {
				iNoticeService.deleteNoticeList(request, ids);
			} else {
				result.put("status", Constants.ITMP_RETURN_FAILURE);
				result.put("errorMessage", "删除公告失败");
			}
		}catch(Exception e){
			return this.handleException(e, "删除公告失败");
		}
		return result;
	}
	
	/**
	 * 获取时间内有效的系统公告列表
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="getAllValidSystemNotice",method=RequestMethod.POST)
	public Map<String,Object> getAllValidSystemNotice(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			
			List<TblNoticeInfo> list = iNoticeService.getAllValidSystemNotice(request);
			result.put("list",list);
		}catch(Exception e){
			return this.handleException(e, "获取展示公告失败");
		}
		return result;
	}
	
	/**
	 * 获取时间内有效的项目公告列表
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="getAllValidProjectNotice",method=RequestMethod.POST)
	public Map<String,Object> getAllValidProjectNotice(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			
			List<TblNoticeInfo> list = iNoticeService.getAllValidProjectNotice(request);
			result.put("list",list);
		}catch(Exception e){
			return this.handleException(e, "获取展示公告失败");
		}
		return result;
	}
	

}
