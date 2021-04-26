package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;


import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblSystemVersion;

public interface TblSystemVersionMapper extends BaseMapper<TblSystemVersion> {

    List<TblSystemVersion> getSystemVersionByCon(TblSystemVersion systemVersion);

    TblSystemVersion selectByPrimaryKey(Long id);

    List<TblSystemVersion> getSystemVersionByVersion(TblSystemVersion record);

    Integer insertVersion(TblSystemVersion record);

    int updateByPrimaryKeySelective(TblSystemVersion record);



    List<TblSystemVersion> getSystemVersionsBySystemId(Long systemId);

    String getVersionName(Long systemVersionId);

    List<TblSystemVersion> getSystemVersionByBd(Map<String, Object> param);
}