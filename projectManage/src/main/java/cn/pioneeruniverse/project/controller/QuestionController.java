package cn.pioneeruniverse.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.entity.TblQuestionInfo;
import cn.pioneeruniverse.project.entity.TblQuestionLog;
import cn.pioneeruniverse.project.service.question.QuestionService;

/**
 * 
* @ClassName: QuestionController
* @Description: 问题管理
* @author author
* @date 2020年8月28日 下午6:52:41
*
 */
@RestController
@RequestMapping("question")
public class QuestionController extends BaseController {

	@Autowired
	private QuestionService questionService;
	
	/**
	 * 问题列表
	 * @param projectId 项目ID
	 * @param request
	 * @return Map<String, Object> status=1正常，2异常   
	 *                             data   List<TblQuestionInfo>问题列表
	 */
	@RequestMapping(value="getQuestions",method=RequestMethod.POST)
	public Map<String,Object> getQuestions(Long projectId, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblQuestionInfo> list = questionService.getQuestions(projectId, request);
			map.put("data", list);
		} catch (Exception e) {
			return super.handleException(e, "问题列表查询失败！");
		}
		return map;
	}
	
	
	/**
	 * 删除问题
	 * @param id 问题ID
	 * @param request
	 * @return Map<String,Object> status=1正常，2异常
	 */
	@RequestMapping(value="deleteQuestion",method=RequestMethod.POST)
	public Map<String,Object> deleteQuestion(Long id, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			questionService.deleteQuestion(id, request);
		} catch (Exception e) {
			return super.handleException(e, "问题删除失败！");
		}
		return map;
	}
	
	
	/**
	 * 新增问题
	 * @param questionInfo 问题信息
	 * @param request
	 * @return Map<String,Object> status=1正常，2异常
	 */
	@RequestMapping(value="insertQuestion",method=RequestMethod.POST)
	public Map<String,Object> insertQuestion(@RequestBody String questionInfo, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblQuestionInfo tblQuestionInfo = JSONObject.parseObject(questionInfo, TblQuestionInfo.class);
			questionService.insertQuestion(tblQuestionInfo, request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return super.handleException(e, "问题新增失败！");
		}
		return map;
	}
	
	
	/**
	 * 问题详情
	 * @param id 问题ID
	 * @return map key:data value: TblQuestionInfo
	 *             key:logs value: List<TblQuestionLog> logs
	 */
	@RequestMapping(value="getQuestionById",method=RequestMethod.POST)
	public Map<String,Object> getQuestionById(Long id){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblQuestionInfo tblQuestionInfo = questionService.getQuestionById(id);
			//问题日志
			List<TblQuestionLog> logs = questionService.getQuestionLog(id);
			map.put("data", tblQuestionInfo);
			map.put("logs", logs);
		} catch (Exception e) {
			return super.handleException(e, "获取问题详情失败！");
		}
		return map;
	}
	
	
	/**
	 * 编辑问题
	 * @param questionInfo 问题信息
	 * @param request
	 * @return Map<String,Object> status=1正常，2异常
	 */
	@RequestMapping(value="updateQuestion",method=RequestMethod.POST)
	public Map<String,Object> updateQuestion(@RequestBody String questionInfo, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblQuestionInfo tblQuestionInfo = JSONObject.parseObject(questionInfo, TblQuestionInfo.class);
			questionService.updateQuestion(tblQuestionInfo, request);
		} catch (Exception e) {
			return super.handleException(e, "问题编辑失败！");
		}
		return map;
	}
	
	
	/**
	 * 项目群管理问题列表
	 * @param programId 项目群ID
	 * @param request
	 * @return map key:data value:List<TblQuestionInfo>
	 */
	@RequestMapping(value="getQuestionByProgram",method=RequestMethod.POST)
	public Map<String,Object> getQuestionByProgram(Long programId, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblQuestionInfo> list = questionService.getQuestionByProgram(programId, request);
			map.put("data", list);
		} catch (Exception e) {
			return super.handleException(e, "获取问题列表失败！");
		}
		return map;
	}
	
}
