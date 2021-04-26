package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblDefectLog;
import cn.pioneeruniverse.dev.entity.TblTestTaskLog;

public interface TblTestTaskLogMapper {

    int insertAdd(TblTestTaskLog record);


   List<TblTestTaskLog>  selectByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 修改日志
    * @Date 2020/9/8
    * @param log
    * @return void
    **/
    void updateLogById(TblDefectLog log);
    
    /**
    * @author author
    * @Description 新增测试工作任务日志 （命名表示疑惑）
    * @Date 2020/9/8
    * @param defectLog
    * @return void
    **/
    void insertDefectLog(TblTestTaskLog defectLog);
}
