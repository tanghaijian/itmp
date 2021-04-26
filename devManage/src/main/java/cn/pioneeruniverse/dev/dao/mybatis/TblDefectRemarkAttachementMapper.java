package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDefectRemarkAttachement;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TblDefectRemarkAttachementMapper extends BaseMapper<TblDefectRemarkAttachement> {
    /**
    *@author liushan
    *@Description 根据id删除
    *@Date 2020/8/14
     * @param id
    *@return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    *@author liushan
    *@Description 判断新增
    *@Date 2020/8/14
     * @param record
    *@return int
    **/
    int insertSelective(TblDefectRemarkAttachement record);

    /**
    *@author liushan
    *@Description 根据id查询备注附件
    *@Date 2020/8/14
     * @param id
    *@return cn.pioneeruniverse.dev.entity.TblDefectRemarkAttachement
    **/
    TblDefectRemarkAttachement selectByPrimaryKey(Long id);

    /**
    *@author liushan
    *@Description 判断修改
    *@Date 2020/8/14
     * @param record
    *@return int
    **/
    int updateByPrimaryKeySelective(TblDefectRemarkAttachement record);

    /**
    *@author liushan
    *@Description 修改备注附件
    *@Date 2020/8/14
     * @param record
    *@return int
    **/
    int updateByPrimaryKey(TblDefectRemarkAttachement record);

    /**
    *@author liushan
    *@Description 根据remarkId 批量移除 所属附件
    *@Date 2020/8/14
     * @param remarkId
    *@return void
    **/
    void removeRemarkAttachements(Long[] remarkId);

    /**
    *@author liushan
    *@Description 根据缺陷id查询备注附件
    *@Date 2020/8/14
     * @param defectId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectRemarkAttachement>
    **/
	List<TblDefectRemarkAttachement> getRemarkAttsByDefectId(Long defectId);

	/**
	*@author liushan
	*@Description 批量新增附件
	*@Date 2020/8/14
	 * @param files
	*@return void
	**/
	void addRemarkAttachement(List<TblDefectRemarkAttachement> files);
}