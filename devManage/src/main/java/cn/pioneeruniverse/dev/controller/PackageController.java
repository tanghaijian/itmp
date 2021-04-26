package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.dev.entity.TblArtifactInfo;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.nexus.NexusSearchVO;
import cn.pioneeruniverse.dev.service.packages.PackageService;

/**
 * 
* @ClassName: PackageController
* @Description: 包件管理controller
* @author author
* @date 2020年8月9日 上午11:15:13
*
 */
@RestController
@RequestMapping("package")
public class PackageController extends BaseController {
	@Autowired
	private PackageService packageService;

	/**
	 * 
	* @Title: getPackage
	* @Description: 查询包件，列表展示
	* @author author
	* @param workTask 开发工作任务
	* @param systemId 系统ID
	* @param projectId 项目ID
	* @param tagStr 标记
	* @param rows 每页条数
	* @param page 第几页
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "getPackage", method = RequestMethod.POST)
	public Map<String, Object> getPackage(String workTask,Long systemId,Long projectId,String tagStr,Integer rows,Integer page) {
		NexusSearchVO nexusSearchVO = null;
		Map<String, Object> map = null;
		try {
			if (workTask != null &&!workTask.isEmpty()) {
				nexusSearchVO = JSON.parseObject(workTask, NexusSearchVO.class);
				map = packageService.findNexusType(nexusSearchVO,systemId, projectId,tagStr,rows,page);
			}
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}

	/**
	 * 包件标记
	 * @param artifactInfoStr 包件信息
	 * @param tagStr 标记信息
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "addFeature", method = RequestMethod.POST)
	public Map<String, Object> addFeature(String artifactInfoStr,String tagStr,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			packageService.addArtifactFrature(artifactInfoStr,tagStr,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result=super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取Artifact信息，详情
	 * @param id 包件id
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "getArtifactInfo", method = RequestMethod.POST)
	public Map<String, Object> getArtifactInfo(Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = packageService.getArtifactInfo(id);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result=super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 关联开发任务
	 * @param requirementFeatureIds
	 * @param artifactInfoId  包件ID
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "relateFeature", method = RequestMethod.POST)
	public Map<String, Object> relateFeature(String requirementFeatureIds,Long artifactInfoId,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
          if (requirementFeatureIds != null && !requirementFeatureIds.equals("")) {
            String[] requirementFeatureIdsArray = requirementFeatureIds.split(",");
            for (String requirementFeatureId : requirementFeatureIdsArray) {
            packageService.relateFeature(Long.parseLong(requirementFeatureId), artifactInfoId, request);
           }
			}
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result=super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 
	* @Title: removeFeature
	* @Description: 移出包件和开发任务的关联，包件标识时，添加和移出
	* @author author
	* @param id 包件ID
	* @param request
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "removeFeature", method = RequestMethod.POST)
	public Map<String, Object> removeFeature(Long id,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			packageService.removeFeature(id,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result=super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	  *   批量去除开发任务关联
	 * @param ids
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "removeManyFeature", method = RequestMethod.POST)
	public Map<String, Object> removeManyFeature(String ids,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			packageService.removeManyFeature(ids,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result=super.handleException(e, e.getMessage());
		}
		return result;
	}

	/**
	 * 获取任务信息
	 * @param tblRequirementFeature
	 * @param pageNumber
	 * @param pageSize
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "getFeatur", method = RequestMethod.POST)
	public Map<String, Object> getFeatur(TblRequirementFeature tblRequirementFeature, Integer pageNumber,
										 Integer pageSize, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			return packageService.findFrature(tblRequirementFeature, pageNumber, pageSize);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}

	/**
	 * 取消标记
	 * @param id
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "removeTag", method = RequestMethod.POST)
	public Map<String, Object> removeTag(Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			packageService.deleteTagByArtifactInfoId(id);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result=super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 删除包件,同时也会删除Nexus上面的包件
	 * @param artifactJson
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "deletePackage", method = RequestMethod.POST)
	public Map<String, Object> deletePackage(String artifactJson) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblArtifactInfo> artifactList = JSON.parseArray(artifactJson, TblArtifactInfo.class);
			String errorMessage = packageService.deletePackage(artifactList);
			if (StringUtil.isNotEmpty(errorMessage)) {
				result.put("status", Constants.ITMP_RETURN_FAILURE);
				result.put("errorMessage", errorMessage);
			}
		} catch (Exception e) {
			result=super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 下载制品包
	 * @param nexusPath
	 */
	@RequestMapping(value = "downPackage")
	public void downPackage(String nexusPath,String  repositoryName, HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//获取nexus
			packageService.downPackage(nexusPath,repositoryName,request,response);
//			packageService.addArtifactFrature(artifactInfoStr,tagStr,request);
//			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
//			result=super.handleException(e, e.getMessage());
		}
		
	}
	
	//获取该系统配置的环境
	@PostMapping("getEnvType")
	public Map<String, Object> getEnvType(Long id){
		Map<String,Object> map = new HashMap<>();
		try {
			map = packageService.getEnvType(id);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 获取该系统的GroupAndArtifactID
	 * @param systemId系统id
	 * @return Map<String,Object>
	 */
	@PostMapping("getGroupAndArtifactID")
	public Map<String, Object> getGroupAndArtifactID(Long systemId){
		Map<String,Object> map = new HashMap<>();
		try {
			map=packageService.getGroupAndArtifactID(systemId);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
}
