package cn.pioneeruniverse.project.service.question;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.project.entity.TblQuestionInfo;
import cn.pioneeruniverse.project.entity.TblQuestionLog;
/**
 *
 * @ClassName:QuestionService
 * @Description 问题service
 * @author author
 * @date 2020年8月27日
 *
 */
public interface QuestionService {
	/**
	 * 查询
	 * @param projectId
	 * @param request
	 * @return List<TblQuestionInfo>
	 */

	List<TblQuestionInfo> getQuestions(Long projectId, HttpServletRequest request);

	/**
	 * 删除
	 * @param id
	 * @param request
	 */

	void deleteQuestion(Long id, HttpServletRequest request);

	/**
	 * 新增
	 * @param tblQuestionInfo
	 * @param request
	 */

	void insertQuestion(TblQuestionInfo tblQuestionInfo, HttpServletRequest request);

	/**
	 * 查询
	 * @param id
	 * @return TblQuestionInfo
	 */

	TblQuestionInfo getQuestionById(Long id);

	List<TblQuestionLog> getQuestionLog(Long id);

	void updateQuestion(TblQuestionInfo tblQuestionInfo, HttpServletRequest request);

	List<TblQuestionInfo> getQuestionByProgram(Long programId, HttpServletRequest request);

}
