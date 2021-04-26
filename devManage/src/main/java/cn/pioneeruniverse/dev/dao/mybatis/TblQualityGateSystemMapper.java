package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblQualityGateSystem;
import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TblQualityGateSystemMapper extends BaseMapper<TblQualityGateSystem> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblQualityGateSystem record);

    TblQualityGateSystem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblQualityGateSystem record);

    /**
    * @author author
    * @Description 根据id修改
    * @Date 2020/9/23
    * @param record
    * @return int
    **/
    int updateByPrimaryKey(TblQualityGateSystem record);
}