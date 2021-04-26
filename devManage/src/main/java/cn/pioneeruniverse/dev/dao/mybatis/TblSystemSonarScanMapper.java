package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemSonarScan;

public interface TblSystemSonarScanMapper extends BaseMapper<TblSystemSonarScan> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemSonarScan record);

    TblSystemSonarScan selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemSonarScan record);

    int updateByPrimaryKey(TblSystemSonarScan record);
}