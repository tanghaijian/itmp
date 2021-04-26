package cn.pioneeruniverse.project.dao.mybatis;

import cn.pioneeruniverse.project.entity.TblAssetBusinessSystem;
import feign.Param;

/**
*@author author
*@Description 业务和系统关联
*@Date 2020/8/26
*@return 
**/
public interface TblAssetBusinessSystemMapper {
    /**
    *@author liushan
    *@Description 删除
    *@Date 2020/8/13
     * @param id
    *@return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    *@author liushan
    *@Description 添加
    *@Date 2020/8/13
     * @param record
    *@return int
    **/
    int insert(TblAssetBusinessSystem record);

    /**
    *@author liushan
    *@Description 判断添加
    *@Date 2020/8/13
     * @param record
    *@return int
    **/
    int insertSelective(TblAssetBusinessSystem record);

    /**
    *@author liushan
    *@Description 根据id查询
    *@Date 2020/8/13
     * @param id
    *@return cn.pioneeruniverse.project.entity.TblAssetBusinessSystem
    **/
    TblAssetBusinessSystem selectByPrimaryKey(Long id);

    /**
    *@author liushan
    *@Description 判断修改
    *@Date 2020/8/13
     * @param record
    *@return int
    **/
    int updateByPrimaryKeySelective(TblAssetBusinessSystem record);

    /**
    *@author liushan
    *@Description 根据id修改
   *@Date 2020/8/13
     * @param record
    *@return int
    **/
    int updateByPrimaryKey(TblAssetBusinessSystem record);

    /**
    *@author liushan
    *@Description 根据businessSystemIds移除
    *@Date 2020/8/13
     * @param businessSystemIds
    *@return void
    **/
    void remove(@Param("businessSystemIds") Long[] businessSystemIds);
}