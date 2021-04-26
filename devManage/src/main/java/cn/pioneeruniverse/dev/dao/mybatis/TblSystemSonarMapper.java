package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemSonar;

public interface TblSystemSonarMapper extends BaseMapper<TblSystemSonar> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemSonar record);

    TblSystemSonar selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemSonar record);

    int updateByPrimaryKey(TblSystemSonar record);
    List<TblSystemSonar> selectBySystemId(Long id);
   // List<TblSystemSonar> selectById(Long id);


	List<TblSystemSonar> selectByMapLimit(Map<String, Object> mapParam);

	Integer insertNew(TblSystemSonar tblSystemSonar);
}