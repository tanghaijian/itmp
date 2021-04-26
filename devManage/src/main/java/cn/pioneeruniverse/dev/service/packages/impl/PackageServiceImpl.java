package cn.pioneeruniverse.dev.service.packages.impl;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;

import cn.hutool.core.collection.CollectionUtil;
import cn.pioneeruniverse.common.nexus.NexusAssetBO;
import cn.pioneeruniverse.common.nexus.NexusComponentBO;
import cn.pioneeruniverse.common.nexus.NexusSearchVO;
import cn.pioneeruniverse.common.nexus.NexusUtil;
import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.common.sonar.client.SonarQubeClientApi;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblArtifactInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblArtifactQualityDetailMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblArtifactRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblArtifactTagMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblQualityGateDetailMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblQualityGateSystemMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblQualityMetricMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblToolInfoMapper;
import cn.pioneeruniverse.dev.entity.TblArtifactInfo;
import cn.pioneeruniverse.dev.entity.TblArtifactQualityDetail;
import cn.pioneeruniverse.dev.entity.TblArtifactRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblArtifactTag;
import cn.pioneeruniverse.dev.entity.TblQualityGateDetail;
import cn.pioneeruniverse.dev.entity.TblQualityGateSystem;
import cn.pioneeruniverse.dev.entity.TblQualityMetric;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblSystemModuleJenkinsJobRun;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
import cn.pioneeruniverse.dev.service.packages.PackageService;

@Service
@Transactional(readOnly = true)
public class PackageServiceImpl implements PackageService {
	
	public final static Logger logger = LoggerFactory.getLogger(PackageServiceImpl.class);
	
	@Autowired
	private TblRequirementFeatureMapper requirementFeatureMapper;
	@Autowired
	private TblToolInfoMapper tblToolInfoMapper;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private TblSystemInfoMapper tblSystemInfoMapper;
	@Autowired
	private TblArtifactInfoMapper artifactInfoMapper;
	@Autowired
	private TblArtifactTagMapper artifactTagMapper;
	@Autowired
	private TblArtifactRequirementFeatureMapper artifactRequirementFeatureMapper;
	@Autowired
	private TblSystemInfoMapper systemInfoMapper;
	@Autowired
	private TblQualityGateSystemMapper tblQualityGateSystemMapper;
	@Autowired
	private TblQualityGateDetailMapper tblQualityGateDetailMapper;
	@Autowired
	private TblQualityMetricMapper tblQualityMetricMapper;
	@Autowired
	private TblArtifactQualityDetailMapper tblArtifactQualityDetailMapper;


	/**
	 * 
	* @Title: findFrature
	* @Description: 查询开发任务
	* @author author
	* @param tblRequirementFeature 封装的查询条件
	* @param page 第几页
	* @param rows 每页条数
	* @return map key rows:返回的数据，total：返回的总数目
	 */
	@Override
	public Map<String, Object> findFrature(TblRequirementFeature tblRequirementFeature, Integer page, Integer rows) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<TblRequirementFeature> list = null;
		//分页查询
		if (page != null && rows != null) {
			PageHelper.startPage(page, rows);
			list = requirementFeatureMapper.findFrature(tblRequirementFeature);
			//替换数据字典名称
			Object status = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
			Map<String, Object> mapstatus = JSON.parseObject(status.toString());
			for (int i = 0; i < list.size(); i++) {
				for (String key : mapstatus.keySet()) {
					if (key.equals(list.get(i).getRequirementFeatureStatus())) {
						list.get(i).setRequirementFeatureStatus(mapstatus.get(key).toString());
					}
				}
			}
			PageInfo<TblRequirementFeature> pageInfo = new PageInfo<TblRequirementFeature>(list);
			//返回的数据
			result.put("rows", list);
			//总页数
			result.put("total", pageInfo.getTotal());
			return result;
		} else {
			//整体查询
			result.put("rows", requirementFeatureMapper.findFrature(tblRequirementFeature));
		}

		return result;

	}

	/**
	 * 
	* @Title: addArtifactFrature
	* @Description: 包件标记
	* @author author
	* @param artifactInfoStr 包件信息
	* @param tagStr 标记
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void addArtifactFrature(String artifactInfoStr, String tagStr, HttpServletRequest request) {
		TblArtifactInfo tblArtifactInfo = JSON.parseObject(artifactInfoStr, TblArtifactInfo.class);
		Long artifactId = tblArtifactInfo.getId();
		if (artifactId != null) {
			//删除原有的标记
			artifactTagMapper.deleteByArtifactId(artifactId);
			//修改标记，不应该更新包件(只有制品才更新包件时间)
//			tblArtifactInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
//			tblArtifactInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
			artifactInfoMapper.updateByPrimaryKeySelective(tblArtifactInfo);
		} else {
			CommonUtil.setBaseValue(tblArtifactInfo, request);
			artifactInfoMapper.insert(tblArtifactInfo);
		}
		//新增标记
		String[] tagArr = tagStr.split(",");
		for (String env : tagArr) {
			TblArtifactTag artifactTag = new TblArtifactTag();
			artifactTag.setEnvironmentType(Integer.valueOf(env));
			artifactTag.setArtifactId(tblArtifactInfo.getId());
			CommonUtil.setBaseValue(artifactTag, request);
			artifactTag.setTagUserId(artifactTag.getCreateBy());
			artifactTag.setTagTime(artifactTag.getCreateDate());
			artifactTagMapper.insert(artifactTag);
		}
	}


	/**
	 * 
	* @Title: addArtifactFrature
	* @Description: 包件标记，构建调用
	* @author author
	* @param backMap 封装的条件，systemId:系统ID，sonarflag：是否代码扫描1是，2否
	* @param tblArtifactInfo 包件信息
	* @param moduleJobRunList 模块的jenkins的job
	* @param tagStr 标记
	* @param userId
	* @param artType 2修改，1新增
	* @throws SonarQubeException
	 */
	@Override
	@Transactional(readOnly = false)
	public void addArtifactFrature(Map<String, Object> backMap, TblArtifactInfo tblArtifactInfo, List<TblSystemModuleJenkinsJobRun> moduleJobRunList, 
			String tagStr,long userId,String artType) throws SonarQubeException {
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		if(artType.equals("2")){
			//判断是否已存在数据库
			Map<String,Object> param=new HashMap<>();
			param.put("SYSTEM_ID",backMap.get("systemId"));
			param.put("GROUP_ID",tblArtifactInfo.getGroupId());
			param.put("ARTIFACT_ID",tblArtifactInfo.getArtifactId());
			param.put("VERSION",tblArtifactInfo.getVersion());
			param.put("NEXUS_PATH",tblArtifactInfo.getNexusPath());
			param.put("STATUS",1);
			List<TblArtifactInfo> ts=artifactInfoMapper.selectByMap(param);
			//已存在则更新
			if(ts.size()>0){
				    TblArtifactInfo tblArtifactInfo1=ts.get(0);

//					tblArtifactInfo.setLastUpdateBy(userId);
//					tblArtifactInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
				    tblArtifactInfo1.setLastUpdateBy(userId);
				    tblArtifactInfo1.setLastUpdateDate(stamp);
					artifactInfoMapper.updateByPrimaryKeySelective(tblArtifactInfo1);
				    tblArtifactInfo.setId(tblArtifactInfo1.getId());


			}else{
				//不存在则新增
				tblArtifactInfo.setCreateBy(userId);
				tblArtifactInfo.setCreateDate(stamp);
				tblArtifactInfo.setLastUpdateBy(userId);
				tblArtifactInfo.setLastUpdateDate(stamp);
				tblArtifactInfo.setStatus(1);
				artifactInfoMapper.insert(tblArtifactInfo);
			}


			//判断标记是否存在，不存在：新增，存在：更新
			Map<String,Object> paramTag=new HashMap<>();
			paramTag.put("artifactId",tblArtifactInfo.getId());
			paramTag.put("env",tagStr);
			List<TblArtifactTag> tagFlags=	artifactTagMapper.selectByArtifactIdAndEnv(paramTag);
			if(tagFlags.size()>0){
				TblArtifactTag updateArtifactTag = new TblArtifactTag();
				updateArtifactTag.setId(tagFlags.get(0).getId());
				updateArtifactTag.setLastUpdateBy(userId);
				updateArtifactTag.setLastUpdateDate(stamp);
				artifactTagMapper.updateByPrimaryKeySelective(updateArtifactTag);

			}else{
				addTag(tagStr,userId,tblArtifactInfo);
			}

		} else {

			tblArtifactInfo.setCreateBy(userId);
			tblArtifactInfo.setCreateDate(stamp);
			tblArtifactInfo.setLastUpdateBy(userId);
			tblArtifactInfo.setLastUpdateDate(stamp);
			tblArtifactInfo.setStatus(1);
			artifactInfoMapper.insert(tblArtifactInfo);
			addTag(tagStr,userId,tblArtifactInfo);
		}
		//来自页面构建的是否Sonar扫描判断,1需要sonar扫描
		String sonarflag = (String)backMap.get("sonarflag");
		if (StringUtil.isNotEmpty(sonarflag) && sonarflag.equals("1")) {
			addQualityInfo(backMap, tblArtifactInfo, moduleJobRunList, userId);
		}

	}

	/**
	  *  添加质量门禁判断内容，根据代码扫描结果入库
	 * @param backMap
	 * @param tblArtifactInfo 包件信息
	 * @param moduleJobRunList 模块Jenkins运行结果
	 * @param userId 
	 * @throws SonarQubeException
	 */
	private void addQualityInfo(Map<String, Object> backMap, TblArtifactInfo tblArtifactInfo, 
			List<TblSystemModuleJenkinsJobRun> moduleJobRunList, Long userId) throws SonarQubeException {
		TblQualityGateSystem gateSystem = new TblQualityGateSystem();
		gateSystem.setSystemId(tblArtifactInfo.getSystemId());
		gateSystem.setStatus(1);
		gateSystem = tblQualityGateSystemMapper.selectOne(gateSystem);//一个系统只能绑定一个质量门禁
		if (gateSystem != null && tblArtifactInfo.getId() != null) {
			
			TblToolInfo sonartool = new TblToolInfo();
			Map<String, Object> param = new HashMap<>();
			param.put("STATUS", 1);
			//1:GIT，2:SVN，3:SONAR，4:JENKINS，5:ARTIFACTORY,6:NEXUS
			param.put("TOOL_TYPE", 3);
			List<TblToolInfo> toolList = tblToolInfoMapper.selectByMap(param);
			if (toolList.size() > 0) {
				Timestamp stamp = new Timestamp(System.currentTimeMillis());
				EntityWrapper<TblQualityGateDetail> gateDetailWrapper = new EntityWrapper<TblQualityGateDetail>();
    			gateDetailWrapper.eq("QUALITY_GATE_ID", gateSystem.getQualityGateId());
    			gateDetailWrapper.eq("STATUS", 1);
    			List<TblQualityGateDetail> gateDetailList = tblQualityGateDetailMapper.selectList(gateDetailWrapper);//获取质量门禁详细
    			EntityWrapper<TblQualityMetric> metricWrapper = new EntityWrapper<TblQualityMetric>();
    			metricWrapper.eq("STATUS", 1);
    			List<TblQualityMetric> qualityMetricList = tblQualityMetricMapper.selectList(metricWrapper);//获取所有指标
				
				sonartool = toolList.get(0);
				String sonarUrl = "http://" + sonartool.getIp() + ":" + sonartool.getPort() + "/";
				String token = sonartool.getSonarApiToken();
				SonarQubeClientApi sonar = new SonarQubeClientApi(sonarUrl, token);//获取Sonar对象
				
				 /***************从Sonar的API获取质量门禁指标******************/
				List<TblArtifactQualityDetail> artifactQualityDetailSaveList = new ArrayList<TblArtifactQualityDetail>();
				String projectKey = getProjectKey(backMap, tblArtifactInfo, moduleJobRunList);//拿到projectKey
                String qualityGateResult = sonar.getQualityGatesApi().getProjectStatus(projectKey, sonarUrl);
                if (qualityGateResult != null && qualityGateResult.length() > 0) {
                	JSONObject qualityGateObj = JSONObject.parseObject(qualityGateResult);
                	JSONObject projectObj = qualityGateObj.getJSONObject("projectStatus");
//                	String pStatus = projectObj.getString("status");
                	
                	JSONArray conditionsArray = projectObj.getJSONArray("conditions");
					for (int y = 0; y < conditionsArray.size(); y++) {
						JSONObject conditionObj = conditionsArray.getJSONObject(y);
//							String status = conditionObj.getString("status");
						String metricKey = conditionObj.getString("metricKey");//key:new_reliability_rating
//							String comparator = conditionObj.getString("comparator");//比较符
//							String periodIndex = conditionObj.getString("periodIndex");
//							String errorThreshold = conditionObj.getString("errorThreshold");//设定值
						String actualValue = conditionObj.getString("actualValue");//实际值
						
						for (TblQualityMetric qualityMetric : qualityMetricList) {
							if (qualityMetric.getMetricCode().trim().equals(metricKey)) {
								
								/***************验证质量门禁指标******************/
								Integer qualityMetricStatus = validateQualityMetric(gateDetailList, qualityMetric, actualValue);
								TblArtifactQualityDetail bean = new TblArtifactQualityDetail();
								bean.setArtifactId(tblArtifactInfo.getId());
								bean.setQualityMetricId(qualityMetric.getId());
								bean.setQualityMetricValue(actualValue);
								bean.setQualityMetricStatus(qualityMetricStatus);
								bean.setCreateBy(userId);
								bean.setCreateDate(stamp);
								bean.setStatus(1);
								artifactQualityDetailSaveList.add(bean);
								break;
							}
						}
					}
				}
				
                /***************保存指标******************/
                EntityWrapper<TblArtifactQualityDetail> qualityDetailWrapper = new EntityWrapper<TblArtifactQualityDetail>();
    			qualityDetailWrapper.eq("ARTIFACT_ID", tblArtifactInfo.getId());
                tblArtifactQualityDetailMapper.delete(qualityDetailWrapper);
	            for (TblArtifactQualityDetail bean : artifactQualityDetailSaveList) {
	            	tblArtifactQualityDetailMapper.insert(bean);
				}
				
			}
			
		}
		
	}

	/**
	  * 验证指标
	 * @param gateDetailList 质量门禁明细
	 * @param qualityMetric质量指标
	 * @param actualValueStr  扫描结果
	 * @return Integer  指标判断结果：1=SUCCESS；2=ERROR; 3=WARNING
	 */
	private Integer validateQualityMetric(List<TblQualityGateDetail> gateDetailList, TblQualityMetric qualityMetric, String actualValueStr) {
		Integer status = 1; //指标判断结果：1=SUCCESS；2=ERROR; 3=WARNING
		for (TblQualityGateDetail tblQualityGateDetail : gateDetailList) {
			if (tblQualityGateDetail.getQualityMetricId().equals(qualityMetric.getId())) {
				String warningValueStr = tblQualityGateDetail.getWarningValue();
				String forbiddenValueStr = tblQualityGateDetail.getForbiddenValue();
				
				if (qualityMetric.getMetricValueType() == 2) {//1:数字，2:字符串
					JSONObject metricValuesObj = JSONObject.parseObject(qualityMetric.getMetricValues());
					if (StringUtil.isNotEmpty(warningValueStr)) {
						warningValueStr = metricValuesObj.getString(warningValueStr);
					}
					if (StringUtil.isNotEmpty(forbiddenValueStr)) {
						forbiddenValueStr = metricValuesObj.getString(forbiddenValueStr);
					}
				}
				String operatorType = tblQualityGateDetail.getOperatorType();
				
				Double actualValue = Double.parseDouble(actualValueStr);
				if (StringUtil.isNotEmpty(warningValueStr)) {
					Double warningValue = Double.parseDouble(warningValueStr);
					if (judgeValue(operatorType, actualValue, warningValue)) {
						status = 3;
					}
				}
				if (StringUtil.isNotEmpty(forbiddenValueStr)) {
					Double forbiddenValue = Double.parseDouble(forbiddenValueStr);
					if (judgeValue(operatorType, actualValue, forbiddenValue)) {
						status = 2;
					}
				}
				break;
			}
		}
		return status;
	}

	/**
	 * 数值判断: actualValue > setValue 实际值 大于 设定值=true，用于判断sonar扫描结果是success、error还是warning
	 * @param operatorType 判断条件
	 * @param actualValue 实际值
	 * @param setValue 设置值
	 * 
	 * @return Boolean
	 */
	private Boolean judgeValue(String operatorType, Double actualValue, Double setValue) {
		Boolean judge = false;
		switch (operatorType) {
		case "<":// 如果实际值小于设置值
			judge = actualValue < setValue;
			break;
		case ">":
			judge = actualValue > setValue;
			break;
		case "==":
			judge = actualValue == setValue;
			break;
		case "!=":
			judge = actualValue != setValue;
			break;
		case "<=":
			judge = actualValue <= setValue;
			break;
		case ">=":
			judge = actualValue >= setValue;
			break;
		}
		return judge;
	}

	/**
	 * 获取到用于Sonar的ProjectKey。
	 * @param backMap
	 * @param tblArtifactInfo
	 * @param moduleJobRunList
	 * @return String
	 */
	private String getProjectKey(Map<String, Object> backMap, TblArtifactInfo tblArtifactInfo,
			List<TblSystemModuleJenkinsJobRun> moduleJobRunList) {
		
		String currentProjectKey = "";
		TblSystemModuleJenkinsJobRun currentModuleJobRun = null;
		for (TblSystemModuleJenkinsJobRun moduleJobRun : moduleJobRunList) {
			if (moduleJobRun.getSystemModuleId() != null) {
				if (moduleJobRun.getSystemModuleId().equals(tblArtifactInfo.getSystemModuleId())) {
					currentModuleJobRun = moduleJobRun;
					break;
				}
			} else {
				currentModuleJobRun = moduleJobRun;
			}
		}
		if (currentModuleJobRun != null) {
			String moduleRunJson = backMap.get("moduleRunJson") == null ? "" : backMap.get("moduleRunJson").toString();
			JSONArray jArray = JSONObject.parseArray(moduleRunJson);
			for (int i = 0; i < jArray.size(); i++) {
				JSONObject moduleObj = jArray.getJSONObject(i);
				Long moduleRunId = moduleObj.getLong("moduleRunId");
				String projectKey = moduleObj.getString("projectKey");
				if (moduleRunId.equals(currentModuleJobRun.getId())) {
					currentProjectKey = projectKey;
					break;
				}
			}
		}
		return currentProjectKey;
	}

	/**
	 * 
	* @Title: addTag
	* @Description: 包件标记
	* @author author
	* @param tagStr 环境类型
	* @param userId
	* @param tblArtifactInfo
	 */
	private void addTag(String tagStr,Long userId,TblArtifactInfo tblArtifactInfo){
		TblArtifactTag artifactTag = new TblArtifactTag();
		artifactTag.setEnvironmentType(Integer.valueOf(tagStr));
		artifactTag.setArtifactId(tblArtifactInfo.getId());
		artifactTag.setCreateBy(userId);
		artifactTag.setCreateDate(new Timestamp(new Date().getTime()));
		artifactTag.setLastUpdateBy(userId);
		artifactTag.setLastUpdateDate(new Timestamp(new Date().getTime()));
		artifactTag.setStatus(1);
		artifactTag.setTagUserId(artifactTag.getCreateBy());
		artifactTag.setTagTime(artifactTag.getCreateDate());
		artifactTagMapper.insert(artifactTag);
	}



	/**
	* @author author
	* @Description 查询包件信息
	* @Date 2020/9/15
	* @param nexusSearchVO
	* @param systemId
	* @param projectId
	* @param tagStr
	* @param rows
	* @param page
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@Override
	public Map<String, Object> findNexusType(NexusSearchVO nexusSearchVO, Long systemId, Long projectId, String tagStr,
			Integer rows, Integer page) throws Exception {
		Map<String, Object> jqGridPage = new HashMap<>();
		List<NexusAssetBO> listBo = null;
		//查找Nexus工具信息，然后连接nexus查找相应的包件信息
		List<TblToolInfo> list = tblToolInfoMapper.findNexusType();
		NexusUtil NexusUtil = new NexusUtil(list.get(0));
		listBo = NexusUtil.searchAssetList(nexusSearchVO);
		
		//从系统中查找相应的包件信息
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("repository", nexusSearchVO.getRepository());
		paramMap.put("groupId", nexusSearchVO.getGroupId());
		paramMap.put("artifactId", nexusSearchVO.getArtifactId());
		paramMap.put("systemId", systemId);
		paramMap.put("projectId", projectId);
		if (StringUtil.isNotEmpty(tagStr)) {
			List<String> tagList = CollectionUtil.toList(tagStr.split(","));
			paramMap.put("tagList", tagList);
		}
		List<Map<String, Object>> systemPackageList = tblSystemInfoMapper.FindSystemPackage(paramMap);
		//nexus仓库和系统中的包件信息整理后分页返回
		jqGridPage = getPackageList(jqGridPage, listBo, systemPackageList, tagStr, rows, page);

		return jqGridPage;
	}

	/**
	 * 查询出包件数据：没有标签查询条件时，按分页来查，有标签时，先查出标签相关数据，再分页。
	 * @param jqGridPage 
	 * @param listBo
	 * @param systemPackageList
	 * @param tagStr
	 * @param startRow
	 * @param endRow
	 * @return
	 */
	private Map<String, Object> getPackageList(Map<String, Object> jqGridPage, List<NexusAssetBO> listBo, List<Map<String, Object>> systemPackageList,
			String tagStr, int rows, int page) {
		Integer startRow = rows * (page - 1); // 开始记录
		Integer endRow = rows * page; // 结束记录
		
		int count = 1;
		List<Map<String, Object>> packageList = new ArrayList<>();
		for (int i = 0; i < listBo.size(); i++) {
			NexusAssetBO assetBO = listBo.get(i);
			Map<String, Object> packageMap = null;
			//通过路径和artifactId判断是否和系统中存的包件信息相符，相符则为同一个包件
			for (Map<String, Object> temp : systemPackageList) {
				if (temp.get("artifactId") != null && assetBO.getArtifactId().equals(temp.get("artifactId").toString())
					&& temp.get("nexusPath") != null && assetBO.getPath().equals(temp.get("nexusPath").toString())) {
					packageMap = temp;
					break;
				}
			}
			
			if (packageMap != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("numCount", count++);
				map.putAll(packageMap); // 将获取的map所有值放入新的map中
				if (!packageMap.containsValue(assetBO.getVersion())) {
					map.put("environmentType", "");
					map.put("artifactInfoId", "");
				} else {
					String envType = "";
					String envName = "";
					if(packageMap.get("environmentType") != null) {
						envType = packageMap.get("environmentType").toString();
						String[] envTypeArr = envType.split(",");
						Map<String,String> envMap = JsonUtil.fromJson((String) redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE"), Map.class);
						for (String envStr : envTypeArr) {
							envName +=  StringUtils.isEmpty(envMap.get(envStr))?"":(envMap.get(envStr) + ",");
						}
					}
					map.put("environmentType", envType);
					map.put("environmentName", StringUtils.isEmpty(envName)?envName : envName.substring(0,envName.length()-1));
				}

				map.put("repository", assetBO.getRepository());
				map.put("group", assetBO.getGroup());
				map.put("artifactId", assetBO.getArtifactId());
				map.put("version", assetBO.getVersion());
				map.put("path", assetBO.getPath());
				map.put("md5", assetBO.getChecksum() == null ? "" : assetBO.getChecksum().get("md5"));
				packageList.add(map);
			}
		}
		
		Collections.sort(packageList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return ((Date)o2.get("updateDate")).compareTo((Date)o1.get("updateDate"));
			}
		});
		
		int records = packageList.size();
		Integer pages = (int) Math.ceil((double) records / rows); // 总页数
		jqGridPage.put("page", page);
		jqGridPage.put("records", records);
		jqGridPage.put("total", pages);
		int start = startRow < records ? startRow : 0;
		int end = endRow < records ? endRow : records;
		jqGridPage.put("rows", packageList.subList(start, end));
		return jqGridPage;
	}
	
	/**
	 * 
	* @Title: deletePackage
	* @Description: 删除包件
	* @author author
	* @param artifactList
	* @return
	* @throws
	 */
	@Override
	@Transactional(readOnly = false)
	public String deletePackage(List<TblArtifactInfo> artifactList) {

		List<TblToolInfo> list = tblToolInfoMapper.findNexusType();
		NexusUtil nexusUtil = new NexusUtil(list.get(0));
		
		NexusSearchVO searchVO = new NexusSearchVO();
		List<NexusComponentBO> componentBO = null;
		String errorMessage = "";
		/**********先从数据库表删除数据，成功后再从Nexus上删除***********/
		for (TblArtifactInfo artifact : artifactList) {
			try {
				if (StringUtil.isNotEmpty(artifact.getRepository())
						&& StringUtil.isNotEmpty(artifact.getGroupId()) 
						&& StringUtil.isNotEmpty(artifact.getArtifactId())
						&& StringUtil.isNotEmpty(artifact.getMd5())) {
					searchVO.setRepository(artifact.getRepository());
					searchVO.setGroupId(artifact.getGroupId());
					searchVO.setArtifactId(artifact.getArtifactId());
					searchVO.setMd5(artifact.getMd5());
					componentBO = nexusUtil.searchComponentList(searchVO);
					//将component的字符串ID保存到MD5里面
					if (componentBO != null && componentBO.size() == 1) {
						artifact.setMd5(componentBO.get(0).getId());
					} else {
						artifact.setMd5(null);
					}
				}
				deleteArtifactAndTag(artifact);
			} catch (Exception e) {
				errorMessage += artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion() + "删除数据失败 ";
				logger.error(e.getMessage(), e);
			}
		}
		
		Thread thread = new Thread(new ThreadRunnable(nexusUtil, artifactList));
		thread.start();
		return errorMessage;
	}
	
	/**
	 * 批量删除Nexus上Component数据，因为删除1条耗时10秒左右，所以单独用一个线程处理。
	 * @author zhoudu
	 *
	 */
	class ThreadRunnable implements Runnable {
		NexusUtil nexusUtil;
		private List<TblArtifactInfo> artifactList;
		public ThreadRunnable(NexusUtil nexusUtil, List<TblArtifactInfo> artifactList) {
			this.nexusUtil = nexusUtil;
			this.artifactList = artifactList;
		}
		@Override
		public void run() {
			String info = "";
			for (TblArtifactInfo artifact : artifactList) {
				try {
					String result = nexusUtil.deleteComponent(artifact.getMd5());
					info += "NEXUS DELETE:id:" + artifact.getId() + ":" + artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion() + "删除：" + result;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			logger.info(info);
		}
	}
	
	/**
	 * 
	* @Title: deleteArtifactAndTag
	* @Description: 删除包件
	* @author author
	* @param artifact
	* @throws
	 */
	private void deleteArtifactAndTag(TblArtifactInfo artifact) {
		TblArtifactRequirementFeature feature = new TblArtifactRequirementFeature();
		feature.setStatus(2);
		EntityWrapper<TblArtifactRequirementFeature> wrapper = new EntityWrapper<TblArtifactRequirementFeature>();
		wrapper.eq("ARTIFACT_ID", artifact.getId());
		artifactRequirementFeatureMapper.update(feature, wrapper);
		
		artifactTagMapper.deleteByArtifactId(artifact.getId());
		
		TblArtifactInfo deleteBean = new TblArtifactInfo();
		deleteBean.setId(artifact.getId());
		deleteBean.setStatus(2);
		artifactInfoMapper.updateByPrimaryKeySelective(deleteBean);
	}

	/**
	* @author author
	* @Description 获取Artifact信息
	* @Date 2020/9/15
	* @param id
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@Override
	public Map<String, Object> getArtifactInfo(Long id) {
		Map<String, Object> map = new HashMap<>();
		TblArtifactInfo artifactInfo = artifactInfoMapper.selectByPrimaryKey(id);
		map.put("artifactInfo", artifactInfo);
		List<TblRequirementFeature> requirementFeatures = artifactRequirementFeatureMapper.selectByArtifactId(id);
		for (TblRequirementFeature tblRequirementFeature : requirementFeatures) {
			String status = tblRequirementFeature.getRequirementFeatureStatus();
			Map<String, Object> dictMap = JSON.parseObject(
					(String) redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS"), Map.class);
			tblRequirementFeature.setRequirementFeatureStatus((String) dictMap.get(status));
		}
		map.put("requirementFeatures", requirementFeatures);
		return map;
	}
	
	/**
	* @author author
	* @Description 通过ids获取包件List
	* @Date 2020/9/15
	* @param artifactIds
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblArtifactInfo>
	**/
	@Override
	public List<TblArtifactInfo> getPackageListById(String artifactIds) {
		EntityWrapper<TblArtifactInfo> wrapper = new EntityWrapper<TblArtifactInfo>();
		wrapper.in("ID", artifactIds);
		wrapper.orderBy("SYSTEM_MODULE_ID");
		List<TblArtifactInfo> artifactList = artifactInfoMapper.selectList(wrapper);
		return artifactList;
	}
	
	/**
	* @author author
	* @Description 根据制品表IDS查询制品包质量明细表
	* @Date 2020/9/15
	* @param artifactIds
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblArtifactQualityDetail>
	**/
	@Override
	public List<TblArtifactQualityDetail> getArtifactQualityDetailList(String artifactIds) {
		EntityWrapper<TblArtifactQualityDetail> wrapper = new EntityWrapper<TblArtifactQualityDetail>();
		wrapper.in("ARTIFACT_ID", artifactIds);
		List<TblArtifactQualityDetail> list = tblArtifactQualityDetailMapper.selectList(wrapper);
		return list;
	}

	/**
	* @author author
	* @Description 关联任务
	* @Date 2020/9/15
	* @param requirementFeatureId
	* @param artifactInfoId
	* @param request
	* @return void
	**/
	@Override
	@Transactional(readOnly = false)
	public void relateFeature(Long requirementFeatureId, Long artifactInfoId, HttpServletRequest request) {
		TblArtifactRequirementFeature artifactRequirementFeature = new TblArtifactRequirementFeature();
		artifactRequirementFeature.setRequirementFeatureId(requirementFeatureId);
		artifactRequirementFeature.setArtifactId(artifactInfoId);
		CommonUtil.setBaseValue(artifactRequirementFeature, request);
		artifactRequirementFeatureMapper.insert(artifactRequirementFeature);
	}

	/**
	* @author author
	* @Description 去除关联
	* @Date 2020/9/15
	* @param id
	* @param request
	* @return void
	**/
	@Override
	@Transactional(readOnly = false)
	public void removeFeature(Long id, HttpServletRequest request) {
		TblArtifactRequirementFeature artifactRequirementFeature = new TblArtifactRequirementFeature();
		artifactRequirementFeature.setId(id);
		artifactRequirementFeature.setStatus(2);
		artifactRequirementFeature.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		artifactRequirementFeature.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		artifactRequirementFeatureMapper.updateByPrimaryKeySelective(artifactRequirementFeature);
	}

	/**
	* @author author
	* @Description 批量去除关联
	* @Date 2020/9/15
	* @param ids
	* @param request
	* @return void
	**/
	@Override
	@Transactional(readOnly = false)
	public void removeManyFeature(String ids, HttpServletRequest request) {
		if (StringUtils.isNotBlank(ids)) {
			List<Integer> idList = JSON.parseArray(ids, Integer.class);
			for (Integer id : idList) {
				removeFeature((long) id, request);
			}
		}
	}

	/**
	* @author author
	* @Description 根据ArtifactInfoId删除tag
	* @Date 2020/9/15
	* @param artifactInfoId
	* @return void
	**/
	@Override
	@Transactional(readOnly = false)
	public void deleteTagByArtifactInfoId(Long artifactInfoId) {
		artifactTagMapper.deleteByArtifactId(artifactInfoId);
	}

	/**
	* @author author
	* @Description 查找最新的jar包
	* @Date 2020/9/15
	* @param systemId
	* @param systemModuleId
	* @param env
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblArtifactInfo>
	**/
	@Override
	public List<TblArtifactInfo> findNewPackage(Long systemId, Long systemModuleId, Integer env) {
		List<TblArtifactInfo> artifactInfoList = artifactInfoMapper.findNewPackage(systemId, systemModuleId, env);
		if (artifactInfoList != null) {
			for (TblArtifactInfo tblArtifactInfo : artifactInfoList) {
				//if (tblArtifactInfo.getRepository().equals("maven-snapshots")) {
//					tblArtifactInfo.setVersion(tblArtifactInfo.getVersion().substring(0, 5));
					tblArtifactInfo.setVersion(tblArtifactInfo.getVersion());
					//获取此jar包所打的标签
					detailTagEnv(tblArtifactInfo);

				//}
			}
		}
		return artifactInfoList;
	}

	private void  detailTagEnv(TblArtifactInfo tblArtifactInfo){
		//获取此jar包所打的标签
		List<TblArtifactTag> tags= artifactTagMapper.selectTagByArtifactId(tblArtifactInfo.getId());
		StringBuffer buffer=new StringBuffer();
		for(TblArtifactTag tblArtifactTag:tags){
			Object redisEnvType = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
			Map<String, Object> envMap = JSON.parseObject(redisEnvType.toString());
			String envName = envMap.get(tblArtifactTag.getEnvironmentType() + "").toString();
			buffer.append(envName+",");

		}
		tblArtifactInfo.setTags(buffer.deleteCharAt(buffer.length() - 1).toString());
	}


	/**
	* @author author
	* @Description 查找jar包d对应的开发任务
	* @Date 2020/9/15
	* @param artifactids
	* @return java.util.List<java.lang.String>
	**/
	@Override
	public List<String> findRequidsByartId(String artifactids) {
		Map<String, Object> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		String[] ids = artifactids.split(",");
		for (String s : ids) {
			list.add(s);
		}
		map.put("artIds", list);
		List<String> aList = artifactInfoMapper.findRequidsByartId(map);
		return aList;
	}
	@Override
	public void downPackage(String nexusPath, String repositoryName,HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取nexustool类
		 try {
		TblToolInfo nexusToolInfo = new TblToolInfo();
		Map<String, Object> param = new HashMap<>();
		param.put("status", 1);
		param.put("TOOL_TYPE", 6);
		List<TblToolInfo> toolList =tblToolInfoMapper.selectByMap(param);
		if (toolList != null && toolList.size() > 0) {
			nexusToolInfo=toolList.get(0);
		}	  
		NexusUtil nexusUtil=new NexusUtil(nexusToolInfo);
		nexusUtil.downloadPackage(request,response,nexusToolInfo,repositoryName, nexusPath );
	   } catch (URISyntaxException e) {
		
		e.printStackTrace();
	}
		
	}

	@Override
	public Map<String, Object> getEnvType(Long id) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		String types = systemInfoMapper.getEnvTypeById(id);
		if(types!=null) {
			String[] strings = types.split(",");
			for(String str : strings) {
				String s = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE").toString();
				JSONObject jsonObject = JSONObject.parseObject(s);
				Object object = jsonObject.get(str);
				map.put(str, object);
			}
		}
		
		return map;
	}

	@Override
	public Map<String, Object> getGroupAndArtifactID(Long systemId) {
		Map<String,Object> map = new HashMap<>();
		Set<String> gruupList=systemInfoMapper.getGruupId(systemId);
		List<String> gruupList2=systemInfoMapper.getModuleGruupId(systemId);
		if(gruupList!=null) {
			for(int i=0;i<gruupList2.size();i++) {
				if(StringUtil.isNotEmpty(gruupList2.get(i))) {
					gruupList.add(gruupList2.get(i));
				}
				
			}
			map.put("gruupId", gruupList);
		}else {
			map.put("gruupId", gruupList2);
		}
		Set<String> artifactIdList=systemInfoMapper.getArtifactId(systemId);
		List<String> artifactIdList2=systemInfoMapper.getModuleArtifactId(systemId);
		if(artifactIdList!=null) {
			for(int i=0;i<artifactIdList2.size();i++) {
				if(StringUtil.isNotEmpty(artifactIdList2.get(i))) {
					artifactIdList.add(artifactIdList2.get(i));
				}
			}
			map.put("artifactId", artifactIdList);
		}else {
			map.put("artifactId", artifactIdList2);
		}
		
		
		return map;
	}
	
}
