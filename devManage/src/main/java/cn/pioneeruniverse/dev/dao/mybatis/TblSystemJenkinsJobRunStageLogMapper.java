package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemJenkinsJobRunStageLog;

public interface TblSystemJenkinsJobRunStageLogMapper extends BaseMapper<TblSystemJenkinsJobRunStageLog> {
    int deleteByPrimaryKey(Long id);

    Integer insert(TblSystemJenkinsJobRunStageLog record);

    int insertSelective(TblSystemJenkinsJobRunStageLog record);

    TblSystemJenkinsJobRunStageLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemJenkinsJobRunStageLog record);

    int updateByPrimaryKeyWithBLOBs(TblSystemJenkinsJobRunStageLog record);

    int updateByPrimaryKey(TblSystemJenkinsJobRunStageLog record);

	TblSystemJenkinsJobRunStageLog selectJobRunStageLog(Map<String, Object> paraMap);
}