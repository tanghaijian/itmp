package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblArtifactQualityDetail;

public interface TblArtifactQualityDetailMapper extends BaseMapper<TblArtifactQualityDetail> {

    /**
    * @author author
    * @Description 物理删除
    * @Date 2020/9/21
    * @param id
    * @return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 新增
    * @Date 2020/9/21
    * @param record
    * @return java.lang.Integer
    **/
    Integer insert(TblArtifactQualityDetail record);

    /**
    * @author author
    * @Description 判断新增操作
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int insertSelective(TblArtifactQualityDetail record);

    /**
    * @author author
    * @Description 根据id查询
    * @Date 2020/9/21
    * @param id
    * @return cn.pioneeruniverse.dev.entity.TblArtifactQualityDetail
    **/
    TblArtifactQualityDetail selectByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 判断修改操作
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int updateByPrimaryKeySelective(TblArtifactQualityDetail record);

    /**
    * @author author
    * @Description 根据id修改
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int updateByPrimaryKey(TblArtifactQualityDetail record);
}