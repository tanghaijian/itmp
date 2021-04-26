package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblCaseCatalog;

public interface TblCaseCatalogMapper {

    /**
    * @author author
    * @Description 新增操作
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int insert(TblCaseCatalog record);

    /**
    * @author author
    * @Description 根据id查询测试案例日志
    * @Date 2020/9/21
    * @param id
    * @return cn.pioneeruniverse.dev.entity.TblCaseCatalog
    **/
    TblCaseCatalog selectByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 判断修改操作
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int updateByPrimaryKeySelective(TblCaseCatalog record);
    
    /**
    * @author author
    * @Description 根据目录查询测试案例目录
    * @Date 2020/9/21
    * @param tblCaseCatalog
    * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> selectCaseCatalogsByCon(TblCaseCatalog tblCaseCatalog);

    /**
    * @author author
    * @Description 根据系统查询测试案例id和目录名称
    * @Date 2020/9/21
    * @param systemId
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblCaseCatalog>
    **/
    List<TblCaseCatalog> selectCaseCatalogsBySystemId(@Param("systemId") Long systemId);
 
}