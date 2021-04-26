package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.CaseStepVo;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblTestSetCaseStep;

public interface TblTestSetCaseStepMapper extends BaseMapper<TblTestSetCaseStep>{

    List<CaseStepVo> findByPrimaryKey(Long id);

	List<TblTestSetCaseStep> selectByPrimaryKey(Long id);

    void deleteByCaseId(@Param("caseId") Long caseId);
    
    Integer insert(TblTestSetCaseStep testSetCaseStep);
    
    void updateStatus(List<Long> list);
    
    void updateStatusByCaseIds(@Param("list") List<Long> list);
    
    void updateCaseStep(TblTestSetCaseStep testSetCaseStep);
    
    void batchInsert(List<TblTestSetCaseStep> list);
    
    List<TblTestSetCaseStep> getAllCaseStepByCaseId(@Param("testSetCaseId") Long testSetCaseId);
    
    List<TblTestSetCaseStep> getCaseStepByCaseNumber(@Param("list") List<String> list,@Param("testSetId") Long testSetId);

    /**
    *@author liushan
    *@Description 根据测试案例id添加测试案例步骤数据
    *@Date 2020/3/23
    *@Param [id, currentUserId]
    *@return void
    **/
    void insertByTestSetCaseId(@Param("oldTestSetCaseId") Long oldTestSetCaseId,@Param("newTestSetCaseId") Long newTestSetCaseId,@Param("currentUserId")  Long currentUserId);

}