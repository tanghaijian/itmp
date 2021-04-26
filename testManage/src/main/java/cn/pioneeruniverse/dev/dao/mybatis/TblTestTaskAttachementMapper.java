package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.entity.TblTestTaskAttachement;
import cn.pioneeruniverse.dev.entity.TblTestTaskLogAttachement;

public interface TblTestTaskAttachementMapper {
    //int deleteByPrimaryKey(Long id);

    void addAttachement(List<TblTestTaskAttachement> list);

    //int insertSelective(TblTestTaskAttachement record);

    List<TblTestTaskAttachement> selectByPrimaryKey(Long testId);

    //int updateByPrimaryKeySelective(TblTestTaskAttachement record);

    int updateByPrimaryKey(TblTestTaskAttachement record);
    void updateNo(Map<String, Object> map);
}