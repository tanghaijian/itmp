package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblSystemDbConfig;
import cn.pioneeruniverse.dev.entity.TblSystemDbConfigVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface TblSystemDbConfigMapper extends BaseMapper<TblSystemDbConfig> {
    int deleteByPrimaryKey(Long id);



    int insertSelective(TblSystemDbConfig record);

    TblSystemDbConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDbConfig record);

    int updateByPrimaryKey(TblSystemDbConfig record);

    List<TblSystemDbConfigVo> findDbConfigListPage(TblSystemDbConfigVo tblSystemDbConfigVo);

    List<TblSystemDbConfigVo> findDbConfigListPageByUserId(TblSystemDbConfigVo tblSystemDbConfigVo);
}