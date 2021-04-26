package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.entity.TblServerInfo;
import cn.pioneeruniverse.dev.service.serverinfo.IServerInfoService;


/**
 * 
* @ClassName: ServerInfoController
* @Description: 服务器信息配置
* @author author
* @date 2020年8月16日 下午7:24:51
*
 */
@RestController
@RequestMapping("serverinfo")
public class ServerInfoController extends BaseController{
	@Autowired
	private IServerInfoService serverInfoService;
	

	/**
	 *  条件查询所有服务器信息带分页(jqGrid)
	 * @param hostName 主机名称
	 * @param status 服务器状态
	 * @param ip ip地址
	 * @param systemId 系统id
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return JqGridPage<TblServerInfo>
	 */
		@RequestMapping(value = "getAllServerInfo", method = RequestMethod.POST)
		public JqGridPage<TblServerInfo> getAllServerInfo(String hostName,Integer status, String ip, Long systemId, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
			JqGridPage<TblServerInfo> jqGridPage = null;
			try {
				TblServerInfo serverInfo = new TblServerInfo();
				serverInfo.setHostName(("").equals(hostName)?null:hostName);
				serverInfo.setStatus(status);
				serverInfo.setIp(ip);
				serverInfo.setSystemId(systemId);
				jqGridPage = serverInfoService.findServerInfo(new JqGridPage<TblServerInfo>(httpServletRequest, httpServletResponse), serverInfo,httpServletRequest);
			} catch (Exception e) {
				super.handleException(e, e.getMessage());
			}
			return jqGridPage;
		}
		
		/**
		 * 条件查询所有服务器信息带分页(Bootstrap)
		 * @param hostName
		 * @param page
		 * @param rows
		 * @return Map<String,Object>
		 */
		@RequestMapping(value = "getAllServerInfo2", method = RequestMethod.POST)
		public Map<String, Object> getAllServerInfo2(String hostName,String IP,String haveHost,Integer page,Integer rows,HttpServletRequest request){
			Map<String, Object> result = new HashMap<>();
			try {
				result = serverInfoService.findServerInfo2(hostName, IP,haveHost, page, rows,request);
			}catch (Exception e) {
				result = super.handleException(e, e.getMessage());
			}
			return result;
		}


	/**
	 * 部署配置查询服务器
	 * @param hostName
	 * @param page
	 * @param rows
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "getAllServerInfo3", method = RequestMethod.POST)
	public Map<String, Object> getAllServerInfo3(String hostName,String IP,String haveHost,Integer page,Integer rows,Long systemId,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try {
			result = serverInfoService.findServerInfo3(hostName, IP,haveHost, page, rows,systemId,request);
		}catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}




		
		/**
		 * 新增服务器
		 * @param serverinfoStr
		 * @return Map<String,Object>
		 */
		@RequestMapping(value = "addServerInfo", method = RequestMethod.POST)
		public Map<String, Object> addServerInfo(String serverinfoStr,HttpServletRequest request){
			Map<String, Object> result = new HashMap<>();
			try {
				TblServerInfo tblServerInfo = JSON.parseObject(serverinfoStr, TblServerInfo.class);
				serverInfoService.insertServerInfo(tblServerInfo,request);
				result.put("status", Constants.ITMP_RETURN_SUCCESS);
			} catch (Exception e) {
				result = super.handleException(e, e.getMessage());
			}
			return result;
		}
		
		/**
		 * 修改服务器
		 * @param serverInfo
		 * @return Map<String,Object>
		 */
		@RequestMapping(value = "updateServerInfo", method = RequestMethod.POST)
		public Map<String, Object> updateServerInfo(String serverinfoStr,HttpServletRequest request){
			Map<String, Object> result = new HashMap<>();
			try {
				TblServerInfo serverInfo = JSON.parseObject(serverinfoStr, TblServerInfo.class);
				serverInfoService.updateServerInfoById(serverInfo,request);
				result.put("status", Constants.ITMP_RETURN_SUCCESS);
			} catch (Exception e) {
				result = super.handleException(e, e.getMessage());
			}
			return result;
		}
		
		/**
		 * 修改服务器状态
		 * @param serverInfo
		 * @return Map<String,Object>
		 */
		@RequestMapping(value = "updateServerInfoStatus", method = RequestMethod.POST)
		public Map<String, Object> updateServerInfoStatus(Long id,Integer status,HttpServletRequest request){
			Map<String, Object> result = new HashMap<>();
			try {
				serverInfoService.updateServerInfoStatusById(id, status,request);
				result.put("status", Constants.ITMP_RETURN_SUCCESS);
			} catch (Exception e) {
				result = super.handleException(e, e.getMessage());
			}
			return result;
		}
}
