package cn.pioneeruniverse.dev.service.systemversion;

import cn.pioneeruniverse.dev.entity.TblSystemVersion;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public interface ISystemVersionService {
    /**
    * @author author
    * @Description 根据条件查询所有版本
    * @Date 2020/9/14
    * @param systemId
    * @param status
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
     Map<String, Object> getSystemVersionByCon(Long systemId,Integer status);

     /**
     * @author author
     * @Description 添加版本
     * @Date 2020/9/14
     * @param systemVersion
     * @param request
     * @return java.lang.Integer
     **/
     Integer insertVersion(TblSystemVersion systemVersion, HttpServletRequest request);

     /**
     * @author author
     * @Description 修改版本
     * @Date 2020/9/14
     * @param systemVersion
     * @param request
     * @return java.lang.Integer
     **/
     Integer updateVersion(TblSystemVersion systemVersion, HttpServletRequest request);

     /**
     * @author author
     * @Description 关闭或者开启版本
     * @Date 2020/9/14
     * @param systemVersion
     * @param request
     * @return java.lang.Integer
     **/
     Integer closeOrOpenSystemVersion (TblSystemVersion systemVersion, HttpServletRequest request);

     /**
     * @author author
     * @Description 查询版本
     * @Date 2020/9/14
     * @param systemVersionId
     * @return cn.pioneeruniverse.dev.entity.TblSystemVersion
     **/
     TblSystemVersion getSystemVersionBySystemVersionId(Long systemVersionId);

     /**
     * @author author
     * @Description 根据条件查询所有版本
     * @Date 2020/9/14
     * @param systemId
     * @param envType
     * @param status
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> getSystemVersionByBd(Long systemId, String envType, Integer status);

    /**
    * @author author
    * @Description 根据条件查询所有版本
    * @Date 2020/9/14
    * @param systemId
    * @param status
    * @param env
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String, Object> getSystemVersionByConNew(Long systemId, Integer status,String env);
}
