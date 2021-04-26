package cn.pioneeruniverse.dev.dao.mybatis;


import cn.pioneeruniverse.dev.entity.TblTopSystemInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface TblTopSystemInfoMapper extends BaseMapper<TblTopSystemInfo> {
    int deleteByPrimaryKey(Long id);

   // int insert(TblTopSystemInfo record);

    int insertSelective(TblTopSystemInfo record);

    TblTopSystemInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblTopSystemInfo record);

    int updateByPrimaryKey(TblTopSystemInfo record);

    void insertNew(TblTopSystemInfo tblTopSystemInfo);

    List<TblTopSystemInfo> getTopSysteminfosByCode(String systemCode);
}