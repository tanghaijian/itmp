package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDefectRemark;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
/**
 *
 * @ClassName:TblDefectRemarkMapper
 * @Description:缺陷mapper
 * @author author
 * @date 2020年8月16日
 *
 */

public interface TblDefectRemarkMapper extends BaseMapper<TblDefectRemark> {
    int deleteByPrimaryKey(Long id);
    /**
     *@author liushan
     *@Description 判断新增
     *@Date 2020/8/14
     * @param record
     *@return int
     **/
    int insertSelective(TblDefectRemark record);
    /**
     *@author liushan
     *@Description 根据id查询
     *@Date 2020/8/14
     * @param id
     *@return int
     **/
    TblDefectRemark selectByPrimaryKey(Long id);

    /**
    *@author liushan
    *@Description 判断修改
    *@Date 2020/8/14
     * @param record
    *@return int
    **/
    int updateByPrimaryKeySelective(TblDefectRemark record);

    /**
    *@author liushan
    *@Description 修改操作
    *@Date 2020/8/14
     * @param record
    *@return int
    **/
    int updateByPrimaryKey(TblDefectRemark record);

    /**
    *@author liushan
    *@Description 新增缺陷备注
    *@Date 2020/8/14
     * @param defectRemark
    *@return void
    **/
    void insertDefectRemark(TblDefectRemark defectRemark);

    /**
    *@author liushan
    *@Description 根据缺陷id查询备注
    *@Date 2020/8/14
     * @param id
    *@return java.lang.Long[]
    **/
    Long[] findRemarkByDefectId(Long id);

    /**
    *@author liushan
    *@Description 批量移除备注
    *@Date 2020/8/14
     * @param remarkId
    *@return void
    **/
    void removeDefectRemark(Long[] remarkId);

    /**
    *@author liushan
    *@Description 根据缺陷id查询备注
    *@Date 2020/8/14
     * @param defectId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectRemark>
    **/
	List<TblDefectRemark> getRemarkByDefectId(Long defectId);
}