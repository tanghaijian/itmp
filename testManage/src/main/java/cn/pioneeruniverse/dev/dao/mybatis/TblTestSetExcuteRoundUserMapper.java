package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblTestSetExcuteRoundUser;

public interface TblTestSetExcuteRoundUserMapper extends BaseMapper<TblTestSetExcuteRoundUser>{
    int deleteByPrimaryKey(Long id);

    Integer insert(TblTestSetExcuteRoundUser record);

    int insertSelective(TblTestSetExcuteRoundUser record);

    TblTestSetExcuteRoundUser selectByPrimaryKey(Long id);
    
    List<Map<String, Object>> selectByCon(TblTestSetExcuteRoundUser testSetExcuteRoundUser);

    int updateByPrimaryKeySelective(TblTestSetExcuteRoundUser record);

    int updateByPrimaryKey(TblTestSetExcuteRoundUser record);
    
    int updateBatchStatus(@Param("list") List<Long> list);
    
    int batchInsert(@Param("list") List<TblTestSetExcuteRoundUser> list);
    
}