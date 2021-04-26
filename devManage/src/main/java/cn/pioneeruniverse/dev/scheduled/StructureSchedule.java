package cn.pioneeruniverse.dev.scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.entity.TblSystemJenkins;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
import cn.pioneeruniverse.dev.notify.StructureNotify;
import cn.pioneeruniverse.dev.service.build.IJenkinsBuildService;
import cn.pioneeruniverse.dev.service.structure.IStructureService;

/**
 * 手动构建部署定时轮询
 */
@Component
public class StructureSchedule {
	@Autowired
	private IStructureService iStructureService;
	@Autowired
	RedisUtils redisUtils;
	@Autowired
	private StructureNotify structureNotify;
	@Autowired
	private IJenkinsBuildService iJenkinsBuildService;
	private final static Logger log = LoggerFactory.getLogger(StructureSchedule.class);
	//private static final SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static ExecutorService threadPool;
	{
		threadPool = Executors.newCachedThreadPool();

	}

	// 手动构建
//    @Scheduled(fixedRate = 3800000)	
//	public void scheduledManualStructure() {
//		log.info("scheduled 处理自定义坏数据- print time every 1 hour :"+formate.format(new Date()));
//		// 获取所有手动构建时间超过1小时的id
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("status", 1);
//		paramMap.put("build_status", 2);
//		paramMap.put("create_type", 2);
//		paramMap.put("job_type", 1);
//		List<TblSystemJenkins> jenkins = iStructureService.selectJenkinsByMap(paramMap);
//		if (jenkins.size() <= 0) {
//
//		} else {
//			for (TblSystemJenkins tblSystemJenkins : jenkins) {
//
//				iStructureService.detailErrorStructure(tblSystemJenkins,"2","1");
//
//			}
//		}
//
//	}
//    
// // 手动部署
//    @Scheduled(fixedRate = 3800000)	
//	public void scheduledDeployManualStructure() {
//		log.info("scheduled 处理自定义坏数据- print time every 1 hour :"+formate.format(new Date()));
//		// 获取所有手动构建时间超过1小时的id
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("status", 1);
//		paramMap.put("build_status", 2);
//		paramMap.put("create_type", 2);
//		paramMap.put("job_type", 2);
//		List<TblSystemJenkins> jenkins = iStructureService.selectJenkinsByMap(paramMap);
//		if (jenkins.size() <= 0) {
//
//		} else {
//			for (TblSystemJenkins tblSystemJenkins : jenkins) {
//
//				iStructureService.detailErrorStructure(tblSystemJenkins,"2","1");
//
//			}
//		}
//
//	}
//	 //自动构建
//    @Scheduled(fixedRate = 3600000)	
//	public void scheduledAutoStructure(){
//		log.info("scheduled 处理自动坏数据 print time every 1 hour:{}"+formate.format(new Date()));
//		// 获取所有手动构建时间超过1小时的id
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("status", 1);
//		paramMap.put("build_status", 2);
////		paramMap.put("create_type", 1);
////		paramMap.put("job_type", 1);
//		List<TblSystemScm> scms = iStructureService.selectScmByMap(paramMap);
//		
//		if (scms.size() <= 0) {
//
//		} else {
//			for (TblSystemScm scm : scms) {
//
//				iStructureService.detailAutoErrorStructure(scm,"1","1",null);
//
//			}
//		}
//	    
//	}
//    
//    //自动部署
//    @Scheduled(fixedRate = 3700000)	
//	public void scheduledAutoDeployStructure(){
//		log.info("scheduled 处理自动坏数据 print time every 1 hour:{}"+formate.format(new Date()));
//		// 获取所有手动构建时间超过1小时的id
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("status", 1);
//		paramMap.put("deploy_status", 2);
////		paramMap.put("create_type", 1);
////		paramMap.put("job_type", 1);
//		List<TblSystemScm> scms = iStructureService.selectScmByMap(paramMap);
//		
//		if (scms.size() <= 0) {
//
//		} else {
//			for (TblSystemScm scm : scms) {
//
//				iStructureService.detailAutoErrorStructure(scm,"2","1",null);
//
//			}
//		}
//	    
//	}
//    
//    


	/**
	 * 轮询手动构建任务
	 */
	@Scheduled(fixedRate = 60000)
	public void structPolling() {
    	String dateStr = DateUtil.formatDate(new Date(), DateUtil.fullFormat);
		log.info("构建轮询开始structCallback>>>>>" + dateStr);
		try {
			List<Map<String, Object>> itmpMaps = (List<Map<String, Object>>) redisUtils.get("structCallback");
			List<Map<String, Object>> refreshMapsList = new ArrayList<>();
			if(itmpMaps!=null && !itmpMaps.isEmpty()) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("构建轮询structCallback所有任务:");
				for (Map<String, Object> map : itmpMaps) {
					String jobName = map.get("jobName") == null ? null : map.get("jobName").toString();
					Integer jobNumber = map.get("jobNumber") == null ? null : Integer.parseInt(map.get("jobNumber").toString());
					Long jenkinsToolId = map.get("jenkinsToolId") == null ? null : Long.parseLong(map.get("jenkinsToolId").toString());
					String systemJenkinsId = map.get("systemJenkinsId") == null ? null : map.get("systemJenkinsId").toString();
					buffer.append("[").append("jobName:").append(jobName)
					.append(" jobNumber:").append(jobNumber)
					.append(" jenkinsToolId:").append(jenkinsToolId)
					.append(" systemJenkinsId:").append(systemJenkinsId).append("]");
				}
				log.info(buffer.toString());
				buffer.setLength(0);
				
				Map<Long, TblToolInfo> tblToolInfoMap = new HashMap<Long, TblToolInfo>();
				Map<String, TblSystemJenkins> tblSystemJenkinsMap = new HashMap<String, TblSystemJenkins>();
				TblToolInfo tblToolInfo = null;
				TblSystemJenkins tblSystemJenkins = null;
				Iterator<Map<String, Object>> it=itmpMaps.iterator();
				while(it.hasNext()){
					Map<String, Object> map = it.next();
					String jobName = map.get("jobName") == null ? null : map.get("jobName").toString();
					Integer jobNumber = map.get("jobNumber") == null ? null : Integer.parseInt(map.get("jobNumber").toString());
					Long jenkinsToolId = map.get("jenkinsToolId") == null ? null : Long.parseLong(map.get("jenkinsToolId").toString());
					String systemJenkinsId = map.get("systemJenkinsId") == null ? null : map.get("systemJenkinsId").toString();
					try {
						tblToolInfo = getTool(tblToolInfoMap, jenkinsToolId);
						tblSystemJenkins = getSystemJenkins(tblSystemJenkinsMap, systemJenkinsId);
						Boolean flag = iJenkinsBuildService.isJenkinsBuilding(tblToolInfo, tblSystemJenkins, jobName, jobNumber);
						if(flag == false){//已结束
							detailStructCallBack(map);
							refreshMapsList.add(map);
						}
						buffer.append("构建轮询structCallback任务完成信息:")
						.append(" jobName:").append(jobName)
						.append(" jobNumber:").append(jobNumber)
						.append(" isBuilding:").append(flag)
						.append(" currentDate:").append(DateUtil.formatDate(new Date(), DateUtil.fullFormat));
						log.info(buffer.toString());
						buffer.setLength(0);//清空
					} catch (Exception e) {
						e.printStackTrace();
						log.info("构建轮询structCallback错误任务:" + jobName + ":" + e.getMessage());
						log.error("构建轮询structCallback错误任务:" + jobName + ":" + e.getMessage(), e);
					}
				}
				
				List<Map<String, Object>> oldMaps = (List<Map<String, Object>>) redisUtils.get("structCallback");
				List<Map<String, Object>> saveMapsList = refreshRedisMap(refreshMapsList, oldMaps);
				redisUtils.set("structCallback", saveMapsList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("构建轮询structCallback错误ERROR:" + dateStr + ":" + e.getMessage());
			log.error("构建轮询structCallback错误ERROR:" + dateStr + ":" + e.getMessage(), e);
		}
		log.info("构建轮询结束structCallback>>>>>" + dateStr);
	}

	/**
	 * 轮询手动部署任务
	 */
	@Scheduled(fixedRate = 60000)
	public void deployPolling(){
		String dateStr = DateUtil.formatDate(new Date(), DateUtil.fullFormat);
		log.info("部署轮询开始deployCallback>>>>>" + dateStr);
		try {
			List<Map<String, Object>> itmpMaps = (List<Map<String, Object>>) redisUtils.get("deployCallback");
			List<Map<String, Object>> refreshMapsList = new ArrayList<>();
			if(itmpMaps!=null && !itmpMaps.isEmpty()) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("部署轮询deployCallback所有任务:");
				for (Map<String, Object> map : itmpMaps) {
					String jobName = map.get("jobName") == null ? null : map.get("jobName").toString();
					Integer jobNumber = map.get("jobNumber") == null ? null : Integer.parseInt(map.get("jobNumber").toString());
					Long jenkinsToolId = map.get("jenkinsToolId") == null ? null : Long.parseLong(map.get("jenkinsToolId").toString());
					String systemJenkinsId = map.get("systemJenkinsId") == null ? null : map.get("systemJenkinsId").toString();
					buffer.append("[").append("jobName:").append(jobName)
					.append(" jobNumber:").append(jobNumber)
					.append(" jenkinsToolId:").append(jenkinsToolId)
					.append(" systemJenkinsId:").append(systemJenkinsId).append("]");
				}
				log.info(buffer.toString());
				buffer.setLength(0);
				
				Map<Long, TblToolInfo> tblToolInfoMap = new HashMap<Long, TblToolInfo>();
				Map<String, TblSystemJenkins> tblSystemJenkinsMap = new HashMap<String, TblSystemJenkins>();
				TblToolInfo tblToolInfo = null;
				TblSystemJenkins tblSystemJenkins = null;
				Iterator<Map<String, Object>> it = itmpMaps.iterator();
				while(it.hasNext()){
					Map<String, Object> map = it.next();
					String jobName = map.get("jobName") == null ? null : map.get("jobName").toString();
					Integer jobNumber = map.get("jobNumber") == null ? null : Integer.parseInt(map.get("jobNumber").toString());
					Long jenkinsToolId = map.get("jenkinsToolId") == null ? null : Long.parseLong(map.get("jenkinsToolId").toString());
					String systemJenkinsId = map.get("systemJenkinsId") == null ? null : map.get("systemJenkinsId").toString();
					try {
						tblToolInfo = getTool(tblToolInfoMap, jenkinsToolId);
						tblSystemJenkins = getSystemJenkins(tblSystemJenkinsMap, systemJenkinsId);
		                Boolean flag = iJenkinsBuildService.isJenkinsBuilding(tblToolInfo, tblSystemJenkins, jobName, jobNumber);
						if(flag == false) {//已结束
							detailDeployCallBack(map);
							refreshMapsList.add(map);
						}
						buffer.append("部署轮询deployCallback任务完成信息:")
						.append(" jobName:").append(jobName)
						.append(" jobNumber:").append(jobNumber)
						.append(" isBuilding:").append(flag)
						.append(" currentDate:").append(DateUtil.formatDate(new Date(), DateUtil.fullFormat));
						log.info(buffer.toString());
						buffer.setLength(0);//清空
					} catch (Exception e) {
						e.printStackTrace();
						log.info("构建轮询structCallback错误任务:" + jobName + ":" + e.getMessage());
						log.error("构建轮询structCallback错误任务:" + jobName + ":" + e.getMessage(), e);
					}
				}
				
				List<Map<String, Object>> oldMaps = (List<Map<String, Object>>) redisUtils.get("deployCallback");
				List<Map<String, Object>> saveMapsList = refreshRedisMap(refreshMapsList, oldMaps);
				redisUtils.set("deployCallback", saveMapsList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("部署轮询deployCallback错误ERROR:" + dateStr + ":" + e.getMessage());
			log.error("部署轮询deployCallback错误ERROR:" + dateStr + ":" + e.getMessage(), e);
		}
		log.info("部署轮询结束deployCallback>>>>>" + dateStr);
	}

    private void detailStructCallBack(Map<String, Object> paramMap) throws Exception {
		ObjectMapper json = new ObjectMapper();
		String param = "";
		param = json.writeValueAsString(paramMap);
		structureNotify.callBackManualJenkins(param);
		//itmpMaps.remove(paramMap);

	}
    
    
    
    private void detailDeployCallBack(Map<String, Object> paramMap) throws Exception {
		ObjectMapper json = new ObjectMapper();
		String param = "";
		param = json.writeValueAsString(paramMap);
		structureNotify.callBackManualDepolyJenkins(param);
		//itmpMaps.remove(paramMap);
	}
    
    private TblSystemJenkins getSystemJenkins(Map<String, TblSystemJenkins> tblSystemJenkinsMap, String systemJenkinsId) {
		TblSystemJenkins tblSystemJenkins;
		if (tblSystemJenkinsMap.containsKey(systemJenkinsId)) {
			tblSystemJenkins = tblSystemJenkinsMap.get(systemJenkinsId);
		} else {
			tblSystemJenkins = iStructureService.selectSystemJenkinsById(systemJenkinsId);
			tblSystemJenkinsMap.put(systemJenkinsId, tblSystemJenkins);
		}
		return tblSystemJenkins;
	}

	private TblToolInfo getTool(Map<Long, TblToolInfo> tblToolInfoMap, Long jenkinsToolId) {
		TblToolInfo tblToolInfo;
		if (tblToolInfoMap.containsKey(jenkinsToolId)) {
			tblToolInfo = tblToolInfoMap.get(jenkinsToolId);
		} else {
			tblToolInfo = iStructureService.geTblToolInfo(jenkinsToolId) ;
			tblToolInfoMap.put(jenkinsToolId, tblToolInfo);
		}
		return tblToolInfo;
	}
	
	/**
	 * 将读取redis的数据中，已经处理完成 的去除，然后保存到reids
	 * @param refreshMapsList
	 * @param oldMaps
	 * @return
	 */
	private List<Map<String, Object>> refreshRedisMap(List<Map<String, Object>> refreshMapsList, List<Map<String, Object>> oldMaps) {
		List<Map<String, Object>> saveMapsList = new ArrayList<>();
		for (Map<String, Object> oldBean : oldMaps) {
			String jobName = oldBean.get("jobName") == null ? null : oldBean.get("jobName").toString();
			Integer jobNumber = oldBean.get("jobNumber") == null ? null : Integer.parseInt(oldBean.get("jobNumber").toString());
			saveMapsList.add(oldBean);
			if (jobName != null && jobNumber != null) {
				for (Map<String, Object> refreshBean : refreshMapsList) {
					String jobNameRefresh = refreshBean.get("jobName") == null ? null : refreshBean.get("jobName").toString();
					Integer jobNumberRefresh = refreshBean.get("jobNumber") == null ? null : Integer.parseInt(refreshBean.get("jobNumber").toString());
					if (jobName.equals(jobNameRefresh) && jobNumber.equals(jobNumberRefresh)) {
						saveMapsList.remove(oldBean);
						break;
					}
				}
				
			}
		}
		return saveMapsList;
	}
	
}
