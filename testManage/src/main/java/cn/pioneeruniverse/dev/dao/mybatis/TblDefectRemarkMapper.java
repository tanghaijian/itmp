package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDefectRemark;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TblDefectRemarkMapper extends BaseMapper<TblDefectRemark> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblDefectRemark record);

    TblDefectRemark selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblDefectRemark record);

    int updateByPrimaryKey(TblDefectRemark record);

    /**
    * @author author
    * @Description 新增备注
    * @Date 2020/9/21
    * @param defectRemark
    * @return void
    **/
    void insertDefectRemark(TblDefectRemark defectRemark);

    /**
    * @author author
    * @Description 根据缺陷id查询所属备注
    * @Date 2020/9/21
    * @param id
    * @return java.lang.Long[]
    **/
    Long[] findRemarkByDefectId(Long id);

    /**
    * @author author
    * @Description 批量逻辑删除备注 STATUS = 2
    * @Date 2020/9/21
    * @param remarkId
    * @return void
    **/
    void removeDefectRemark(Long[] remarkId);

    /**
    * @author author
    * @Description 根据缺陷id查询备注
    * @Date 2020/9/21
    * @param defectId
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectRemark>
    **/
	List<TblDefectRemark> getRemarkByDefectId(Long defectId);

}