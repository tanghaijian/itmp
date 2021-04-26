package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblProjectResourcePlanning;

public interface TblProjectResourcePlanningMapper extends BaseMapper<TblProjectResourcePlanning> {
    /**
    * @author author
    * @Description 物理删除
    * @Date 2020/9/23
    * @param id
    * @return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 新增
    * @Date 2020/9/23
    * @param record
    * @return int
    **/
    int insertSelective(TblProjectResourcePlanning record);

    /**
    * @author author
    * @Description 根据id查询
    * @Date 2020/9/23
    * @param id
    * @return cn.pioneeruniverse.dev.entity.TblProjectResourcePlanning
    **/
    TblProjectResourcePlanning selectByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 判断修改操作
    * @Date 2020/9/23
    * @param record
    * @return int
    **/
    int updateByPrimaryKeySelective(TblProjectResourcePlanning record);

    /**
    * @author author
    * @Description 根据id修改
    * @Date 2020/9/23
    * @param record
    * @return int
    **/
    int updateByPrimaryKey(TblProjectResourcePlanning record);
}