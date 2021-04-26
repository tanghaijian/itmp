package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblDevTaskLog;

public interface TblDevTaskLogMapper {

    /**
    * @author author
    * @Description 新增开发工作任务日志
    * @Date 2020/9/22
    * @param record
    * @return int
    **/
    int insertAdd(TblDevTaskLog record);

    /**
    * @author author
    * @Description 根据id查询
    * @Date 2020/9/22
    * @param id
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskLog>
    **/
   List<TblDevTaskLog>  selectByPrimaryKey(Long id);
   /**
   * @author author
   * @Description 更新开发工作任务日志内容
   * @Date 2020/9/22
   * @param log
   * @return void
   **/
    void updateLogById(TblDevTaskLog log);
    
    /**
    * @author author
    * @Description 新增开发工作任务日志
    * @Date 2020/9/22
    * @param defectLog
    * @return void
    **/
    void insertDefectLog(TblDevTaskLog defectLog);
}
