package cn.pioneeruniverse.dev.service.serverinfo.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.CommonUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblServerInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemDeployMapper;
import cn.pioneeruniverse.dev.entity.TblServerInfo;
import cn.pioneeruniverse.dev.service.serverinfo.IServerInfoService;
@Service
@Transactional(readOnly = true)
public class ServerInfoServiceImpl implements IServerInfoService{

	@Autowired
	private TblServerInfoMapper tblServerInfoMapper;
	@Autowired
	private TblSystemDeployMapper tblSystemDeployMapper;
	
	/**
	* @author author
	* @Description 根据id查询
	* @Date 2020/9/9
	* @param id
	* @return cn.pioneeruniverse.dev.entity.TblServerInfo
	**/
	@Override
	public TblServerInfo selectById(Long id) {
		// TODO Auto-generated method stub
		return tblServerInfoMapper.selectByPrimaryKey(id);
	}

	/**
	* @author author
	* @Description 根据条件分页查询(jqGrid)
	* @Date 2020/9/9
	* @param systemInfoJqGridPage
	* @param tblServerInfo
	* @param httpServletRequest
	* @return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.dev.entity.TblServerInfo>
	**/
	@Override
	public JqGridPage<TblServerInfo> findServerInfo(JqGridPage<TblServerInfo> systemInfoJqGridPage,
			TblServerInfo tblServerInfo,HttpServletRequest httpServletRequest) {
		Long currentUserId = CommonUtil.getCurrentUserId(httpServletRequest);
		PageHelper.startPage(systemInfoJqGridPage.getJqGridPrmNames().getPage(), systemInfoJqGridPage.getJqGridPrmNames().getRows());
		List<TblServerInfo> serverInfoList = new ArrayList<>();
		if(new CommonUtils().currentUserWithAdmin(httpServletRequest) == false){
			//serverInfoList = tblServerInfoMapper.selectByCon(tblServerInfo,CommonUtil.getCurrentUserId(httpServletRequest),null);
			serverInfoList = tblServerInfoMapper.selectAllServerInfo(tblServerInfo, null, currentUserId);
			//查询此用户可看系统
//			List<String> systemInfos=tblServerInfoMapper.selectByUserId( currentUserId);
//			List<TblServerInfo> serverInfoNew = new ArrayList<>();
//			for(TblServerInfo tblServerInfoold :serverInfoList ){
//				if(tblServerInfoold.getSystemId()!=null ){
//					if(systemInfos.contains(tblServerInfoold.getSystemId().toString())){
//						serverInfoNew.add(tblServerInfoold);
//					}
//				}
//			}
//			serverInfoList=serverInfoNew;
		}else {
			serverInfoList = tblServerInfoMapper.selectAllServerInfo(tblServerInfo, null, null);
		}
		PageInfo<TblServerInfo> pageInfo = new PageInfo<TblServerInfo>(serverInfoList);
		systemInfoJqGridPage.processDataForResponse(pageInfo);
		return systemInfoJqGridPage;
	}

	@Override
	@Transactional(readOnly = false)
	public void insertServerInfo(TblServerInfo tblServerInfo,HttpServletRequest request) {
		CommonUtil.setBaseValue(tblServerInfo, request);
		tblServerInfoMapper.insert(tblServerInfo);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateServerInfoById(TblServerInfo tblServerInfo,HttpServletRequest request) {
		tblServerInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblServerInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		tblServerInfoMapper.updateByPrimaryKeySelective(tblServerInfo);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteServerInfoById(Long id) {
		tblServerInfoMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateServerInfoStatusById(Long id, Integer status,HttpServletRequest request) {
		TblServerInfo serverInfo = new TblServerInfo();
		serverInfo.setId(id);
		serverInfo.setStatus(status);
		updateServerInfoById(serverInfo,request);
	}

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
	@Override
	public Map<String, Object> findServerInfo2(String hostName, String IP,String haveHost, Integer page, Integer rows,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		TblServerInfo serverInfo = new TblServerInfo();
		serverInfo.setHostName(hostName.length() == 0?null:hostName);
		serverInfo.setStatus(1);
		serverInfo.setIp(IP.length() == 0?null:IP);
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		PageHelper.startPage(page,rows);
		List<Long> list = JSON.parseObject(haveHost, List.class);
		List<TblServerInfo> serverInfoList = tblServerInfoMapper.selectByCon(serverInfo,CommonUtil.getCurrentUserId(request),haveHost.equals("[]")?null:list);
		PageInfo<TblServerInfo> pageInfo = new PageInfo<>(serverInfoList);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}
	/**
	 * 根据条件分页查询(Bootstrap)
	 * @param haveHost(已有服务器)
	 */
	/**
	* @author author
	* @Description 根据条件分页查询(Bootstrap)
	* @Date 2020/9/9
	* @param hostName
	* @param IP
	* @param haveHost
	* @param page
	* @param rows
	* @param systemId
	* @param request
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@Override
	public Map<String, Object> findServerInfo3(String hostName, String IP,String haveHost, Integer page, Integer rows,Long systemId,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		TblServerInfo serverInfo = new TblServerInfo();
		serverInfo.setHostName(hostName.length() == 0?null:hostName);
		serverInfo.setStatus(1);
		serverInfo.setIp(IP.length() == 0?null:IP);
		serverInfo.setSystemId(systemId);
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		PageHelper.startPage(page,rows);
		List<Long> list = JSON.parseObject(haveHost, List.class);
		List<TblServerInfo> serverInfoList = tblServerInfoMapper.selectByCon(serverInfo,currentUserId,haveHost.equals("[]")?null:list);
		//查询此用户可看系统
//		List<String> systemInfos=tblServerInfoMapper.selectByUserId( currentUserId);
//		List<TblServerInfo> serverInfoNew = new ArrayList<>();
//		if (systemInfos.contains(String.valueOf(systemId))) {
//			for (TblServerInfo tblServerInfoold : serverInfoList) {
//				if (tblServerInfoold.getSystemId() != null) {
//					if (String.valueOf(systemId).equals(tblServerInfoold.getSystemId().toString())) {
//						serverInfoNew.add(tblServerInfoold);
//					}
//				}
//			}
//		}
		PageInfo<TblServerInfo> pageInfo = new PageInfo<>(serverInfoList);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}
}
