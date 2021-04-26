package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblTestSetCaseStepExecuteAttachement;

public interface TblTestSetCaseStepExecuteAttachementMapper extends BaseMapper<TblTestSetCaseStepExecuteAttachement> {
    int deleteByPrimaryKey(Long id);

    Integer insert(TblTestSetCaseStepExecuteAttachement record);

    int insertSelective(TblTestSetCaseStepExecuteAttachement record);

    TblTestSetCaseStepExecuteAttachement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblTestSetCaseStepExecuteAttachement record);

    int updateByPrimaryKey(TblTestSetCaseStepExecuteAttachement record);
}