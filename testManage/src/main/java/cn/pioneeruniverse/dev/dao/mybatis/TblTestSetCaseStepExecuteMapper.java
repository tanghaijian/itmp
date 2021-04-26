package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblDefectLogAttachement;
import cn.pioneeruniverse.dev.entity.TblTestSetCaseStep;
import cn.pioneeruniverse.dev.entity.TblTestSetCaseStepExecute;

public interface TblTestSetCaseStepExecuteMapper extends BaseMapper<TblTestSetCaseStepExecute>{

	int insertSelective(List<Map<String, String>> list);

    List<TblTestSetCaseStepExecute>  selectByPrimaryKey(Long id);
    
    
    int insertPass(List<TblTestSetCaseStep> list);


    /**
    *@author liushan
    *@Description 添加测试案例执行步骤
    *@Date 2020/3/25
    *@Param [param]
    *@return void
    **/
    void insertSelectiveMap(Map<String,Object> param);
}