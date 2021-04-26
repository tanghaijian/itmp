package cn.pioneeruniverse.dev.service.sprint.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.dev.dao.mybatis.TblSprintInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.entity.TblSprintInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.service.sprint.SprintInfoService;

@Service
public class SprintInfoServiceImpl implements SprintInfoService {
	
	@Autowired
	private TblSystemInfoMapper tblSystemInfoMapper;
	
	@Autowired
	private TblSprintInfoMapper tblSprintInfoMapper;
	@Autowired
	private TblRequirementFeatureMapper tblRequirementFeatureMapper;

	/**
	*@author liushan
	*@Description 获取系统下拉框的数据
	*@Date 2020/8/3
	 * @param 
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSystemInfo>
	**/
	@Override
	@Transactional(readOnly=true)
	public List<TblSystemInfo> getAllSystem() {
		return tblSystemInfoMapper.selectAll();
	}

	/**
	*@author liushan
	*@Description 冲刺管理列表显示
	*@Date 2020/8/3
	 * @param sprintName 冲刺名称
 * @param uid 用户id
 * @param systemIds 系统ids
 * @param validStatus 状态 1=有效；2=无效
 * @param roleCodes 角色权限
 * @param page 分页
 * @param rows
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	**/
	@Override
	@Transactional(readOnly=true)
	public List<TblSprintInfo> getSprintInfos(String sprintName,Long uid,String systemIds, Integer validStatus,List<String> roleCodes,Integer page, Integer rows) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("sprintName", sprintName);
		map.put("systemIds", systemIds);
		map.put("uid", uid);
		map.put("validStatus", validStatus);
		map.put("start", (page-1)*rows);
		map.put("rows", rows);
		List<TblSprintInfo> list = new ArrayList<>();
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
			 list = tblSprintInfoMapper.getSprintInfos(map);
		}else {
			 list = tblSprintInfoMapper.getSprintInfoCondition(map);
		}
		
		/*for (TblSprintInfo tblSprintInfo : list) {
			tblSprintInfo.setSystemName(tblSystemInfoMapper.getSystemName(tblSprintInfo.getSystemId()));
			
		}*/
		return list;
	}
	
	/**
	*@author liushan
	*@Description 分页总数
	*@Date 2020/8/3  参数同上
	 * @param sprintName
	 * @param uid
	 * @param systemIds
	 * @param validStatus
	 * @param roleCodes
	*@return java.lang.Integer
	**/
	@Override
	public Integer getSprintInfosCount(String sprintName, Long uid, String systemIds, Integer validStatus,List<String> roleCodes) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("sprintName", sprintName);
		map.put("systemIds", systemIds);
		map.put("uid", uid);
		map.put("validStatus", validStatus);
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
			return tblSprintInfoMapper.getSprintInfosCount(map);
		}else {
			return tblSprintInfoMapper.getSprintInfosCountCondition(map);
		}
		
	}

	/**
	*@author liushan
	*@Description 删除冲刺任务
	*@Date 2020/8/3
	 * @param request
	 * @param sprintIdList 需要删除的冲刺ids
	*@return void
	**/
	@Override
	@Transactional(readOnly=false)
	public void deleteSprintInfo(HttpServletRequest request, String sprintIdList) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<>();
		map.put("updateUser", CommonUtil.getCurrentUserId(request));
		map.put("updateTime", new Timestamp(new Date().getTime()));
        String[] split = sprintIdList.split(",");
        for (int i=0;i<split.length;i++){
            map.put("id", split[i]);
            tblSprintInfoMapper.deleteSprintInfo(map);
        }

	}

	/**
	*@author liushan
	*@Description 新增冲刺任务
	*@Date 2020/8/3
	 * @param sprintInfo 新增冲刺数据
 * @param request
	*@return void
	**/
	@Override
	@Transactional(readOnly=false)
	public void addSprintInfo(TblSprintInfo sprintInfo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		sprintInfo.setStatus(1);
		sprintInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		sprintInfo.setCreateDate(new Timestamp(new Date().getTime()));
		sprintInfo.setValidStatus(1);
		if (sprintInfo.getSystemList() != null){
            String[] split = sprintInfo.getSystemList().split(",");
            for (int i=0;i<split.length;i++){
                sprintInfo.setSystemId(Long.parseLong(split[i]));
                tblSprintInfoMapper.addSprintInfo(sprintInfo);
            }

        }

	}

	/**
	*@author liushan
	*@Description 编辑数据回显
	*@Date 2020/8/3
	 * @param id 冲刺id
	*@return cn.pioneeruniverse.dev.entity.TblSprintInfo
	**/
	@Override
	@Transactional(readOnly=true)
	public TblSprintInfo getSprintInfoById(Long id) {
		// TODO Auto-generated method stub
		return tblSprintInfoMapper.selectByPrimaryKey(id);
	}

	/**
	*@author liushan
	*@Description 编辑任务
	*@Date 2020/8/3
	 * @param sprintInfo
 * @param request
	*@return void
	**/
	@Override
	@Transactional(readOnly=false)
	public void updateSprintInfo(TblSprintInfo sprintInfo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		sprintInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		sprintInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
        String[] split = sprintInfo.getSprintIdList().split(",");
        String[] systemIdListSplit = sprintInfo.getSystemIdList().split(",");
        for (int i =0;i<split.length;i++){
            sprintInfo.setId(Long.parseLong(split[i]));
            sprintInfo.setSystemId(Long.parseLong(systemIdListSplit[i]));
            tblSprintInfoMapper.updateByPrimaryKeySelective(sprintInfo);
        }

	}

	/**
	*@author liushan
	*@Description 关闭任务
	*@Date 2020/8/3
	 * @param id 冲刺id
 * @param request
	*@return void
	**/
	@Override
	@Transactional(readOnly=false)
	public void closeSprint(String id, HttpServletRequest request) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<>();
		map.put("updateUser", CommonUtil.getCurrentUserId(request));
		map.put("updateTime", new Timestamp(new Date().getTime()));
        String[] split = id.split(",");
        for (int i=0;i<split.length;i++){
            map.put("id", split[i]);
            tblSprintInfoMapper.closeSprint(map);
        }
	}

	/**
	*@author liushan
	*@Description 冲刺列表数据
	*@Date 2020/8/3
	 * @param sprintName 冲刺名称
 * @param uid 用户id
 * @param systemName 子系统名称
 * @param roleCodes 角色权限
 * @param pageNum 分页
 * @param pageSize
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblSprintInfo>
	**/
	@Override
	public List<TblSprintInfo> getAllSprints(String sprintName, Long uid, String systemName, List<String> roleCodes,
			Integer pageNum, Integer pageSize) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("sprintName", sprintName);
		map.put("systemName", systemName);
		map.put("uid", uid);
		map.put("start", (pageNum-1)*pageSize);
		map.put("rows", pageSize);
		List<TblSprintInfo> list = new ArrayList<>();
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
			 list = tblSprintInfoMapper.getSprintInfos(map);
		}else {
			 list = tblSprintInfoMapper.getSprintInfoCondition(map);
		}
		
		return list;
	}

	/**
	*@author liushan
	*@Description 冲刺列表数据 分页总数
	*@Date 2020/8/3 参数同上
	 * @param sprintName
 * @param uid
 * @param systemName
 * @param roleCodes
	*@return java.lang.Integer
	**/
	@Override
	public Integer getAllSprinsCount(String sprintName, Long uid, String systemName, List<String> roleCodes) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("sprintName", sprintName);
		map.put("systemName", systemName);
		map.put("uid", uid);
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
			return tblSprintInfoMapper.getSprintInfosCount(map);
		}else {
			return tblSprintInfoMapper.getSprintInfosCountCondition(map);
		}
	}

	/**
	*@author liushan
	*@Description 开启任务
	*@Date 2020/8/3
	 * @param id 冲刺id
 * @param request
	*@return void
	**/
	@Override
	@Transactional(readOnly=false)
	public void openSprint(String id, HttpServletRequest request) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<>();
		map.put("updateUser", CommonUtil.getCurrentUserId(request));
		map.put("updateTime", new Timestamp(new Date().getTime()));
        String[] split = id.split(",");
        for (int i=0;i<split.length;i++){
            map.put("id", split[i]);
            tblSprintInfoMapper.openSprint(map);
        }

	}

	/**
	 *  根据冲刺id获取该冲刺相关信息
	 * @param sprintIdLists
	 * @return
	 */
	@Override
	public TblSprintInfo selectSprintInfoById(@Param("sprintIdLists") List<Object> sprintIdLists) {
		return tblSprintInfoMapper.selectSprintInfoById(sprintIdLists);
	}

	/**
	*@author liushan
	*@Description 更新冲刺任务预估剩余时间
	*@Date 2020/8/3
	 * @param map
	*@return void
	**/
	@Override
	public void updateSprintWorkLoad(Map<String, Object> map) {
//		String add=map.get("add").toString();
//		int type = (int) map.get("type");
//
//		if(add.equals("true")){
//
//				Double newWorkLoad = (Double) map.get("newWorkLoad");
//				Long devTaskId = (Long) map.get("devTaskId");
//				TblRequirementFeature tblRequirementFeature =
//						tblRequirementFeatureMapper.selectById(devTaskId);
//				if (tblRequirementFeature.getSprintId() != null) {
//					// 获取冲刺数据
//					TblSprintInfo tblSprintInfo =
//							tblSprintInfoMapper.selectById(tblRequirementFeature.getSprintId());
//					BigDecimal work = new BigDecimal(Double.toString(newWorkLoad));
//                if (type == 1) { // 测试任务 新增
//
//					if(tblSprintInfo.getTestEstimteWorkload()==null){
//						tblSprintInfo.setTestEstimteWorkload(newWorkLoad);
//					}else{
//						tblSprintInfo.setTestEstimteWorkload( new BigDecimal(tblSprintInfo.getTestEstimteWorkload()).add(work).doubleValue());
//					}
//
//
//                }else{
//                	//开发任务新增
//					if(tblSprintInfo.getDevEstimteWorkload()==null){
//						tblSprintInfo.setDevEstimteWorkload(newWorkLoad);
//					}else{
//						tblSprintInfo.setDevEstimteWorkload( new BigDecimal(tblSprintInfo.getDevEstimteWorkload()).add(work).doubleValue());
//					}
//
//				}
//
//
//					tblSprintInfoMapper.updateById(tblSprintInfo);
//				}
//
//
//
//
//
//    } else {
     int type = (int) map.get("type");
     //开发工作任务id
      Long devTaskId = Long.parseLong(map.get("devTaskId").toString()) ;
      //原有工作量
      Double oldWorkLoad = Double.parseDouble(map.get("oldWorkLoad").toString());
      //现有工作量
      Double newWorkLoad =  Double.parseDouble(map.get("newWorkLoad").toString());
      BigDecimal oldWork = new BigDecimal(Double.toString(oldWorkLoad));
      BigDecimal newWork = new BigDecimal(Double.toString(newWorkLoad));

      // 获取冲刺表主键
      TblRequirementFeature tblRequirementFeature =
          tblRequirementFeatureMapper.selectById(devTaskId);
      if (tblRequirementFeature.getSprintId() != null) {
        // 获取冲刺数据
        TblSprintInfo tblSprintInfo =
            tblSprintInfoMapper.selectByPrimaryKey(tblRequirementFeature.getSprintId());
		  TblSprintInfo newTblsprintInfo=new TblSprintInfo();
        if (type == 1) {
          // 修改测试预估剩余时间
          Double testWork = tblSprintInfo.getTestEstimteWorkload();
          BigDecimal work = new BigDecimal(Double.toString(testWork));
          BigDecimal a = work.subtract(oldWork);
//          if(a.compareTo(new BigDecimal(0))<0){
//			  newTblsprintInfo.setTestEstimteWorkload(newWork.doubleValue());
//        } else {
			if( a.add(newWork).compareTo(new BigDecimal(0))<0){
				newTblsprintInfo.setTestEstimteWorkload(new Double(0));
        	} else {
         		newTblsprintInfo.setTestEstimteWorkload(a.add(newWork).doubleValue());
			}
         // }

			Double estimteWorkload=new BigDecimal(newTblsprintInfo.getTestEstimteWorkload()).add(new BigDecimal(tblSprintInfo.getDevEstimteWorkload())).doubleValue();
			newTblsprintInfo.setEstimteWorkload(estimteWorkload);

        } else {
          // 修改开发
          Double testWork = tblSprintInfo.getDevEstimteWorkload();
          BigDecimal work = new BigDecimal(Double.toString(testWork));
          BigDecimal a = work.subtract(oldWork);
//			if(a.compareTo(new BigDecimal(0))<0){
//				newTblsprintInfo.setDevEstimteWorkload(newWork.doubleValue());
//        } else {
//				newTblsprintInfo.setDevEstimteWorkload(a.add(newWork).doubleValue());
//          }
			if( a.add(newWork).compareTo(new BigDecimal(0))<0){
				newTblsprintInfo.setDevEstimteWorkload(new Double(0));
			} else {
				newTblsprintInfo.setDevEstimteWorkload(a.add(newWork).doubleValue());
			}

			Double estimteWorkload=new BigDecimal(newTblsprintInfo.getDevEstimteWorkload()).add(new BigDecimal(tblSprintInfo.getTestEstimteWorkload())).doubleValue();
			newTblsprintInfo.setEstimteWorkload(estimteWorkload);
        }

        newTblsprintInfo.setId(tblSprintInfo.getId());
		  tblSprintInfoMapper.updateByPrimaryKeySelective(newTblsprintInfo);
       // tblSprintInfoMapper.updateById(tblSprintInfo);
      }
      }
//	}

}
