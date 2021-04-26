package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDefectLogAttachement;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface TblDefectLogAttachementMapper extends BaseMapper<TblDefectLogAttachement> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblDefectLogAttachement record);

    TblDefectLogAttachement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblDefectLogAttachement record);

    int updateByPrimaryKey(TblDefectLogAttachement record);

    /**
    *@author liushan
    *@Description 根据日志id查询日志附件
    *@Date 2020/8/13
     * @param logId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectLogAttachement>
    **/
    List<TblDefectLogAttachement> selectLogAttachementBylogId(Long logId);
}