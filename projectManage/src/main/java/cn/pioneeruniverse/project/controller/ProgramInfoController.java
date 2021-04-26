package cn.pioneeruniverse.project.controller;

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

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.entity.TblProgramInfo;
import cn.pioneeruniverse.project.service.program.ProgramService;

/**
 * 
* @ClassName: ProgramInfoController
* @Description: 项目群controller
* @author author
* @date 2020年8月7日 下午4:41:47
*
 */
@RestController
@RequestMapping("program")
public class ProgramInfoController extends BaseController {
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private RedisUtils redisUtils;
	
	/**
	 * 项目群列表查询
	 * @param programInfo 封装的查询条件
	 * @param request
	 * @param page 第几页
	 * @param rows 每页条数
	 * @return Map<String, Object> status=1正常返回，2异常返回
	 */
	@RequestMapping(value="getAllPrograms",method=RequestMethod.POST)
	public Map<String, Object> getAllPrograms(TblProgramInfo programInfo, Integer page,Integer rows, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			Long uid  = CommonUtil.getCurrentUserId(request);
			LinkedHashMap codeMap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) codeMap.get("roles");
			List<TblProgramInfo> list = programService.getAllPrograms(programInfo, uid, roleCodes, page, rows);
			List<TblProgramInfo> list2 = programService.getAllPrograms(programInfo, uid, roleCodes, 1, Integer.MAX_VALUE);
			double total = Math.ceil(list2.size()*1.0/rows);
			//查询的总条目数
			map.put("records", list2.size()); 
			//当前页
			map.put("page", page);	
			//总页数
			map.put("total", total); 
			//每页数据
			map.put("data", list);				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return super.handleException(e, "项目群查询失败！");
		}
		return map;
	}
	
	
	/**
	 * 
	* @Title: getProgramById
	* @Description: 根据项目群ID，显示详情
	* @author author
	* @param id 项目群ID
	* @return Map<String, Object> status=1正常返回，2异常返回
	 */
	@RequestMapping(value="getProgramById",method=RequestMethod.POST)
	public Map<String, Object> getProgramById(Long id){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblProgramInfo programInfo = programService.getProgramById(id);
			//项目群信息
			map.put("data", programInfo);
		} catch (Exception e) {
			return super.handleException(e, "获取项目群详情失败！");
		}
		return map;
	}
	
	/**
	 * 
	* @Title: updateProgram
	* @Description: 编辑项目群
	* @author author
	* @param programInfo 项目群信息
	* @param request
	* @return Map<String, Object> status:1正常，2异常
	 */
	@RequestMapping(value="updateProgram",method=RequestMethod.POST)
	public Map<String, Object> updateProgram(@RequestBody String programInfo, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblProgramInfo tblProgramInfo = JSONObject.parseObject(programInfo, TblProgramInfo.class);
			programService.updateProgram(tblProgramInfo, request);
		} catch (Exception e) {
			return super.handleException(e, "编辑项目群失败！");
		}
		return map;
	}

}
