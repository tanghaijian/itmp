package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemDeploy;

public interface TblSystemDeployMapper extends BaseMapper<TblSystemDeploy> {
    int deleteByPrimaryKey(Long id);

    Integer insert(TblSystemDeploy record);

    TblSystemDeploy selectByPrimaryKey(Long id);
    
    List<TblSystemDeploy> selectByCon(TblSystemDeploy tblSystemDeploy);

    int updateByPrimaryKeySelective(TblSystemDeploy record);
    
    List<String> getServersByUserId(@Param("userId") Long userId);

}