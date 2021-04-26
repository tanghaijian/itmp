package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.entity.TblTestSetCaseExecute;
import org.apache.ibatis.annotations.Param;

public interface TblTestSetCaseExecuteMapper {
	 //添加
	int insertSelective(Map<String, Object> map);
	 //更新执行结果
	 int updateSelective(Map<String, Object> map);

	 //查询测试执行
	List<TblTestSetCaseExecute> findeSetCase(@Param("testSetId") Long testSetId,@Param("caseNumber") String caseNumber,@Param("start") Integer start,@Param("pageSize") Integer pageSize);
	Integer countfindeSetCase(@Param("testSetId") Long testSetId,@Param("caseNumber") String caseNumber);
	 // 根据测试集查询 
	 TblTestSetCaseExecute selectByID(Long id);
	 String selectNameById(Long id);

	 int insertCodeBatch (Map<String, Object> map);
	 
	 List<TblTestSetCaseExecute>  selectByTestSet(Long testSetId);
	List<TblTestSetCaseExecute> getCaseExecute(Map<String, Object> map);
	Long getCaseExecuteCount(Long devID);
	List<String> findExecuteUser(Long devID);
	
	/**
	*@author liushan
	*@Description 根据测试集案例添加测试执行案例
	*@Date 2020/3/25
	*@Param [param]
	*@return void
	**/
	void insertByTestSetCase(Map<String,Object> param);
}
