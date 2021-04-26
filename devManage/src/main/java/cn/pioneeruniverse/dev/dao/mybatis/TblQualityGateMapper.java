package cn.pioneeruniverse.dev.dao.mybatis;


import cn.pioneeruniverse.dev.entity.TblQualityGate;
import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TblQualityGateMapper extends BaseMapper<TblQualityGate> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblQualityGate record);

    TblQualityGate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblQualityGate record);

    /**
    * @author author
    * @Description 根据id修改
    * @Date 2020/9/23
    * @param record
    * @return int
    **/
    int updateByPrimaryKey(TblQualityGate record);
}