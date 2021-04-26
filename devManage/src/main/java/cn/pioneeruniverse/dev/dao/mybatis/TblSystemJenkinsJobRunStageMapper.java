package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemJenkinsJobRunStage;

public interface TblSystemJenkinsJobRunStageMapper extends BaseMapper<TblSystemJenkinsJobRunStage> {
    int deleteByPrimaryKey(Long id);

    Integer insert(TblSystemJenkinsJobRunStage record);

    int insertSelective(TblSystemJenkinsJobRunStage record);

    TblSystemJenkinsJobRunStage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemJenkinsJobRunStage record);

    int updateByPrimaryKey(TblSystemJenkinsJobRunStage record);
}