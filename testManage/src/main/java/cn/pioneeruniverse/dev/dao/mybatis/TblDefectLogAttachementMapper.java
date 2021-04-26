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
    *@author author
    *@Description 根据日志id查询所属日志文件
    *@Date 2020/8/19
     * @param logId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectLogAttachement>
    **/
    List<TblDefectLogAttachement> selectLogAttachementBylogId(Long logId);
}