package cn.pioneeruniverse.dev.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonSyntaxException;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.service.sprint.SprintInfoService;

/**
*@author liushan
*@Description 冲刺管理
*@Date 2020/8/4
**/
@RestController
@RequestMapping("sprintManage")
public class SprintInfoController extends BaseController {

	@Autowired
	private SprintInfoService sprintInfoService;
	/**
	* @author author
	* @Description 注入redis
	* @Date 2020/9/18
	**/
	@Autowired
	private RedisUtils redisUtils;
	
	/**
	 * 获取系统下拉框数据
	 * @return Map<String,Object> status=1正常返回，2异常返回
	 * wjdz
	 * 2019年3月12日
	 * 上午9:59:14
	 */
	@RequestMapping(value="getAllSystem",method=RequestMethod.POST)
	public Map<String, Object> getAllSystem(){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblSystemInfo> list = sprintInfoService.getAllSystem();
			//返回的数据
			map.put("data", list);
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
		return map;
	}
	
	/**
	 * 冲刺管理列表展示分页查询
	 * @param sprintName 冲刺名
	 * @param systemIds 系统id
	 * @param page 第几页
	 * @param rows 每页大小
	 * @return Map<String,Object> status=1正常返回，2异常返回
	 * 2019年3月12日
	 * 上午10:57:39
	 */
	@RequestMapping(value="getSprintInfos",method=RequestMethod.POST)
	public Map<String, Object> getSprintInfos(String sprintName, String systemIds, Integer validStatus, Integer page,Integer rows,HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			Long uid = CommonUtil.getCurrentUserId(request);
			LinkedHashMap usermap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) usermap.get("roles");
			
			List<TblSprintInfo> list = sprintInfoService.getSprintInfos(sprintName,uid, systemIds, validStatus,roleCodes, page, rows);
			Integer count = sprintInfoService.getSprintInfosCount(sprintName,uid, systemIds, validStatus,roleCodes);
			double total = Math.ceil(count*1.0/rows);
			map.put("records", count);    //查询的总条目数
			map.put("page", page);					//当前页
			map.put("total", total);             //总页数
			map.put("data", list);				//每页数据
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
		return map;
	}
	
	//冲刺弹框 bootstrap ztt
	/**
	*@author liushan
	*@Description 冲刺弹框 bootstrap
	*@Date 2020/8/3
	 * @param sprintName 冲刺名称
	 * @param systemName 子系统名称
	 * @param pageNum 分页
	 * @param pageSize
	 * @param request
	*@return java.util.Map<java.lang.String,java.lang.Object> status=1正常返回，2异常返回
	**/
	@RequestMapping(value="getAllsprint",method=RequestMethod.POST)
	public Map<String, Object> getAllsprint(String sprintName, String systemName, Integer pageNum,Integer pageSize,HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			Long uid = CommonUtil.getCurrentUserId(request);
			LinkedHashMap usermap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) usermap.get("roles");
			
			List<TblSprintInfo> list = sprintInfoService.getAllSprints(sprintName,uid, systemName,roleCodes, pageNum, pageSize);
			Integer count = sprintInfoService.getAllSprinsCount(sprintName,uid, systemName,roleCodes);
			map.put("total", count);             //总页数
			map.put("rows", list);				//每页数据
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
		return map;
	}
	
	
	/**
	 * 删除冲刺任务
	 * @param request
	 * @param sprintIdList
	 * @return Map<String,Object> status=1正常返回，2异常返回
	 * 2019年3月12日
	 * 下午5:12:26
	 */
	@RequestMapping(value="deleteSprintInfo",method=RequestMethod.POST)
	public Map<String, Object> deleteSprintInfo(HttpServletRequest request,String sprintIdList){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
            sprintInfoService.deleteSprintInfo(request, sprintIdList);
		} catch (Exception e) {
			return super.handleException(e, "删除失败！");
		}
		return map;
	}
	
	/**
	 * 新增
	 * @param tblSprintInfo
	 * @param request
	 * @return Map<String,Object> status=1正常返回，2异常返回
	 * 2019年3月15日
	 * 上午9:58:41
	 */
	@RequestMapping(value="addSprintInfo",method=RequestMethod.POST)
	public Map<String, Object> addSprintInfo(@RequestBody String tblSprintInfo, HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
//			Gson gson = new Gson();
//			TblSprintInfo sprintInfo = gson.fromJson(tblSprintInfo, TblSprintInfo.class);
			TblSprintInfo sprintInfo = JSON.parseObject(tblSprintInfo,TblSprintInfo.class);
			sprintInfoService.addSprintInfo(sprintInfo, request);
		} catch (Exception e) {
			return super.handleException(e, "新增失败！");
		}
		return map;
	}
	
	/**
	 * 编辑数据回显
	 * @param id 冲刺id
	 * @return Map<String,Object> status=1正常返回，2异常返回
	 * 2019年3月15日
	 * 上午10:23:56
	 */
	@RequestMapping(value="getSprintInfoById",method=RequestMethod.POST)
	public Map<String, Object> getSprintInfoById(Long id,String sprintIdList){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
//			TblSprintInfo tblSprintInfo = sprintInfoService.getSprintInfoById(id);
			if(sprintIdList != null){
                String[] split = sprintIdList.split(",");
                List<Object> sprintIdLists = new ArrayList<>();
                for (int i=0;i<split.length;i++){
                    sprintIdLists.add(split[i]);
                }
                TblSprintInfo tblSprintInfo = sprintInfoService.selectSprintInfoById(sprintIdLists);
                //冲刺信息
                map.put("data", tblSprintInfo);
            }
		} catch (Exception e) {
			return super.handleException(e, "获取数据失败！");
		}
	return map;	
	}

	/**
	 * 编辑任务
	 * @param tblSprintInfo 冲刺信息
	 * @return Map<String,Object> status=1正常返回，2异常返回
	 * 2019年3月15日
	 * 上午10:55:22
	 */
	@RequestMapping(value="updateSprintInfo",method=RequestMethod.POST)
	public Map<String, Object> updateSprintInfo(@RequestBody String tblSprintInfo, HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblSprintInfo sprintInfo = JSON.parseObject(tblSprintInfo,TblSprintInfo.class);
			sprintInfoService.updateSprintInfo(sprintInfo, request);
		} catch (JsonSyntaxException e) {
			return super.handleException(e, "更新数据失败！");
		}
		return map;
	}
	
	/**
	 * 关闭冲刺
	 * @param id 冲刺id
	 * @param request
	 * @return Map<String,Object> status=1正常返回，2异常返回
	 * wjdz
	 * 2019年4月22日
	 * 下午2:15:36
	 */
	@RequestMapping(value="closeSprint",method=RequestMethod.POST)
	public Map<String, Object> closeSprint(String id, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			sprintInfoService.closeSprint(id, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return super.handleException(e, "处理失败！");
		}
		return map;
	}
	
	/**
	 * 开启任务
	 * @param id 冲刺id
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="openSprint",method=RequestMethod.POST)
	public Map<String, Object> openSprint(String id, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			sprintInfoService.openSprint(id, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return super.handleException(e, "处理失败！");
		}
		return map;
	}
	/**
	 * 更新冲刺任务预估剩余时间
	 */
	@RequestMapping(value="updateSprintWorkLoad",method=RequestMethod.POST)
	public void updateSprintWorkLoad(String param, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			map=JSON.parseObject(param);
			sprintInfoService.updateSprintWorkLoad(map);
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}

	}
}
