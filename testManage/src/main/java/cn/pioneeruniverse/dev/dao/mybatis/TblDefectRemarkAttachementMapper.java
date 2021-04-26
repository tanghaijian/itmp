package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDefectRemarkAttachement;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TblDefectRemarkAttachementMapper extends BaseMapper<TblDefectRemarkAttachement> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblDefectRemarkAttachement record);

    TblDefectRemarkAttachement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblDefectRemarkAttachement record);

    int updateByPrimaryKey(TblDefectRemarkAttachement record);

    /**
    *@author author
    *@Description 移除缺陷备注附件
    *@Date 2020/8/19
     * @param remarkId
    *@return void
    **/
    void removeRemarkAttachements(Long[] remarkId);

    /**
    *@author author
    *@Description  添加备注附件
    *@Date 2020/8/19
     * @param files
    *@return void
    **/
	void addRemarkAttachement(List<TblDefectRemarkAttachement> files);

	/***
	*@author author
	*@Description 查询缺陷备注
	*@Date 2020/8/19
	 * @param defectId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectRemarkAttachement>
	**/
	List<TblDefectRemarkAttachement> getRemarkAttsByDefectId(Long defectId);
}