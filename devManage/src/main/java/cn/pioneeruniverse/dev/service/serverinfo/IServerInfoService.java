package cn.pioneeruniverse.dev.service.serverinfo;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.entity.TblServerInfo;

public interface IServerInfoService {
	/**
	 * @author author
	 * @Description 根据id查询
	 * @Date 2020/9/9
	 * @param id
	 * @return cn.pioneeruniverse.dev.entity.TblServerInfo
	 **/
	TblServerInfo selectById(Long id);
	/**
	 * @author author
	 * @Description 根据条件分页查询(jqGrid)
	 * @Date 2020/9/9
	 * @param systemInfoJqGridPage
	 * @param tblServerInfo
	 * @param httpServletRequest
	 * @return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.dev.entity.TblServerInfo>
	 **/
	JqGridPage<TblServerInfo> findServerInfo(JqGridPage<TblServerInfo> systemInfoJqGridPage, TblServerInfo tblServerInfo,HttpServletRequest httpServletRequest);
	/**
	 * @author author
	 * @Description 根据条件分页查询(Bootstrap)
	 * @Date 2020/9/9
	 * @param hostName
	 * @param IP
	 * @param haveHost haveHost(已有服务器)
	 * @param page
	 * @param rows
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	Map<String, Object> findServerInfo2(String hostName,String IP,String haveHost, Integer page,Integer rows,HttpServletRequest request);
	
	void insertServerInfo(TblServerInfo tblServerInfo,HttpServletRequest request);
	
	void updateServerInfoById(TblServerInfo tblServerInfo,HttpServletRequest request);
	
	void deleteServerInfoById(Long id);
	
	void updateServerInfoStatusById(Long id,Integer status,HttpServletRequest request);
	/**
	 * @author author
	 * @Description 根据条件分页查询(Bootstrap)
	 * @Date 2020/9/9
	 * @param hostName
	 * @param ip
	 * @param haveHost
	 * @param page
	 * @param rows
	 * @param systemId
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 **/
    Map<String, Object> findServerInfo3(String hostName, String ip, String haveHost, Integer page, Integer rows,Long systemId, HttpServletRequest request);
}
