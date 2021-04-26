package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblQuestionInfo;

public interface TblQuestionInfoMapper {
	/**
	 * 获取问题
	 * @param projectId
	 * @return
	 */
	List<TblQuestionInfo> getQuestions(Long projectId);

	/**
	 * 删除
	 * @param questionInfo
	 */

	void deleteQuestion(TblQuestionInfo questionInfo);

	/**
	 * 插入
	 * @param tblQuestionInfo
	 */

	void insertQuestion(TblQuestionInfo tblQuestionInfo);

	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */

	TblQuestionInfo getQuestionById(Long id);

	void updateQuestion(TblQuestionInfo tblQuestionInfo);

	/**
	 * 根据programId查询
	 * @param programId
	 * @return
	 */

	List<TblQuestionInfo> getQuestionByProgram(Long programId);

}
