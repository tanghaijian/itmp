package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureLog;

public interface TblRequirementFeatureLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblRequirementFeatureLog record);

    int insertSelective(TblRequirementFeatureLog record);

    TblRequirementFeatureLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementFeatureLog record);

    int updateByPrimaryKey(TblRequirementFeatureLog record);

    /**
    *@author liushan
    *@Description 根据开发任务id查询日志
    *@Date 2020/8/14
     * @param id
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeatureLog>
    **/
	List<TblRequirementFeatureLog> findByReqFeatureId(Long id);
}