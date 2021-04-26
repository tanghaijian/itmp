package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDefectLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface TblDefectLogMapper extends BaseMapper<TblDefectLog> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblDefectLog record);

    TblDefectLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblDefectLog record);

    int updateByPrimaryKeyWithBLOBs(TblDefectLog record);

    int updateByPrimaryKey(TblDefectLog record);

    void insertDefectLog(TblDefectLog defectLog);

    /**
    *@author liushan
    *@Description 根据缺陷id查询日志
    *@Date 2020/8/13
     * @param defectId
    *@return cn.pioneeruniverse.dev.entity.TblDefectLog
    **/
    TblDefectLog findLogByDefectId(Long defectId);

    /**
    *@author liushan
    *@Description 根据缺陷id和日志类型查询日志信息
    *@Date 2020/8/13
     * @param defectLog
    *@return cn.pioneeruniverse.dev.entity.TblDefectLog
    **/
    TblDefectLog findLogByDidAndType(TblDefectLog defectLog);

    /**
    *@author liushan
    *@Description 根据id修改缺陷日志
    *@Date 2020/8/13
     * @param log
    *@return void
    **/
    void updateLogById(TblDefectLog log);

    /**
    *@author liushan
    *@Description 查询当前缺陷的所有日志
    *@Date 2020/8/13
     * @param defectId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectLog>
    **/
    List<TblDefectLog> getDefectLogsById(Long defectId);

    /**
    *@author liushan
    *@Description 查询所属缺陷的日志最大的日志id
    *@Date 2020/8/13
     * @param defectId
    *@return java.lang.Long
    **/
    Long getDefectMaxLogId(Long defectId);

    /**
    *@author liushan
    *@Description 查询最近的一次日志
    *@Date 2020/8/13
     * @param logId
    *@return cn.pioneeruniverse.dev.entity.TblDefectLog
    **/
    TblDefectLog getDefectRecentLogById(Long logId);
}