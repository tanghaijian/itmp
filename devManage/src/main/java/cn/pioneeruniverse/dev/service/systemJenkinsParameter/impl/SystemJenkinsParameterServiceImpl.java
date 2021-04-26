package cn.pioneeruniverse.dev.service.systemJenkinsParameter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemDeployeUserConfigMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemJenkinsMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemJenkinsParameterMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemJenkinsParameterValuesMapper;
import cn.pioneeruniverse.dev.entity.TblSystemDeployeUserConfig;
import cn.pioneeruniverse.dev.entity.TblSystemJenkins;
import cn.pioneeruniverse.dev.entity.TblSystemJenkinsParameter;
import cn.pioneeruniverse.dev.entity.TblSystemJenkinsParameterValues;
import cn.pioneeruniverse.dev.entity.ZtreeObj;
import cn.pioneeruniverse.dev.service.systemJenkinsParameter.SystemJenkinsParameterService;
import com.github.pagehelper.PageInfo;
@Service("systemJenkinsParameterService")
@Transactional(readOnly = true)
public class SystemJenkinsParameterServiceImpl implements SystemJenkinsParameterService {
	private final static Logger log = LoggerFactory.getLogger(SystemJenkinsParameterServiceImpl.class);
	@Autowired
	private TblSystemJenkinsParameterMapper systemJenkinsParameterMapper;
	@Autowired
	private TblSystemJenkinsParameterValuesMapper systemJenkinsParameterValuesMapper;
	@Autowired
	private TblSystemJenkinsMapper tblSystemJenkinsMapper;
	@Autowired
	RedisUtils redisUtils;
	
	@Autowired
	private TblSystemDeployeUserConfigMapper tblSystemDeployeUserConfigMapper;
	/**
	 *  获取参数名称
	 * @param systemJenkinsParameter
	 * @param rows
	 * @param page
	 * @return  Map<String, Object>
	 */
	@Override
	public Map getSystemJenkinsParameterList(
			TblSystemJenkinsParameter systemJenkinsParameter,Integer rows,Integer page) {
		List<TblSystemJenkinsParameter> list = new ArrayList();
		Map<String, Object> result = new HashMap<String, Object>();
		if (page != null && rows != null) {
			  PageHelper.startPage(page, rows);
			  list= systemJenkinsParameterMapper.getSystemJenkinsParameterList(systemJenkinsParameter);
			  PageInfo<TblSystemJenkinsParameter> pageInfo = new PageInfo<TblSystemJenkinsParameter>(list);
			  
			  result.put("rows", list);
			  result.put("records", pageInfo.getTotal());
			  result.put("total", pageInfo.getPages());
			  result.put("page", page < pageInfo.getPages() ? page : pageInfo.getPages());
			  return result;
		}else {
			 result.put("rwos", systemJenkinsParameterMapper.getSystemJenkinsParameterList(systemJenkinsParameter));
		}
		return result;
	}
	/**
	 *  新建注册任务参数化配置
	 * @param systemJenkinsParameter
	 * @param systemJenkinsParameterValues
	 * @param request
	 * @return Map<String, Object>
	 */
	@Override
	@Transactional(readOnly = false)
	public void addSystemJenkinsParameter(TblSystemJenkinsParameter systemJenkinsParameter,
			String systemJenkinsParameterValues, HttpServletRequest request) {
		List<TblSystemJenkinsParameterValues> systemJenkinsParameterValuesList = JsonUtil.fromJson(systemJenkinsParameterValues, JsonUtil.createCollectionType(List.class, TblSystemJenkinsParameterValues.class));
		Long createBy=CommonUtil.getCurrentUserId(request);
		systemJenkinsParameter.setCreateBy(createBy);
		systemJenkinsParameterMapper.addSystemJenkinsParameter(systemJenkinsParameter);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", systemJenkinsParameterValuesList);
		map.put("systemJenkinsParameterId", systemJenkinsParameter.getId());
		map.put("createBy", createBy);
		systemJenkinsParameterValuesMapper.insertParameterValues(map);
	}
	/**
	 *  查询参数化
	 * @param systemJenkinsParameterId 主键
	 * @param request
	 * @return
	 */
	@Override
	public TblSystemJenkinsParameter selectSystemJenkinsParameterById(Long systemJenkinsParameterId,HttpServletRequest request) {
		TblSystemJenkinsParameter systemJenkinsParameter=systemJenkinsParameterMapper.getSystemJenkinsParameterById(systemJenkinsParameterId);
		List<TblSystemJenkinsParameterValues> list=systemJenkinsParameterValuesMapper.selectParameterValuesById(systemJenkinsParameterId);
		if(list.size() != 0) {
			Object tblToolInfo = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
			Map<String, Object> envMap = JSON.parseObject(tblToolInfo.toString());
			Map<String, Object> map = new HashMap<>();
			map.put("system_id", systemJenkinsParameter.getSystemId());
			//map.put("DEPLOY_STATUS", 1);
			map.put("STATUS", 1);
			//map.put("JOB_TYPE", 2);// 部署
			map.put("create_type", 2);// 手动
			List<TblSystemJenkins> listJ = tblSystemJenkinsMapper.selectByMap(map);
			
			List<TblSystemJenkins> systemJenkins=new ArrayList<>();
			for(TblSystemJenkins tt : listJ){
				if (tt.getEnvironmentType() != null){
					tt.setRootPom(tt.getId() + "");
					if(judgeEnvByuser(request,systemJenkinsParameter.getSystemId(),tt.getEnvironmentType().toString())){
						tt.setEnvironmentTypeName(envMap.get(tt.getEnvironmentType().toString()).toString());
						systemJenkins.add(tt);
					}
				}
			}
			if (listJ != null && listJ.size() > 0) {
				
				for(int i=0;i<list.size();i++) {
					List<ZtreeObj> listZtree=getZtree(listJ);
					for(int j=0;j<listZtree.size();j++) {
						String systemJenkinsId=list.get(i).getSystemJenkinsId();
						String ztreeId=listZtree.get(j).getRealId();
						if(systemJenkinsId.indexOf(""+ztreeId+",")>-1){
							listZtree.get(j).setChecked(true);
						}else {
							listZtree.get(j).setChecked(false);
						}
						
					}
					list.get(i).setListZtree(listZtree);
				}
				
			} 
			
			systemJenkinsParameter.setParameterValuesList(list);
		}
		return systemJenkinsParameter;
	}
	/**
	 *  编辑参数化
	 * @param systemJenkinsParameter
	 * @param systemJenkinsParameterValues
	 * @param deleteIds 删除id
	 */
	@Override
	@Transactional(readOnly = false)
	public void editSystemJenkinsParameter(TblSystemJenkinsParameter systemJenkinsParameter,
			String systemJenkinsParameterValues,String deleteIds, HttpServletRequest request) {
		Long userId = CommonUtil.getCurrentUserId(request);
		Long id=systemJenkinsParameter.getId();
				
		List<TblSystemJenkinsParameterValues> systemJenkinsParameterValuesList = JsonUtil.fromJson(systemJenkinsParameterValues, JsonUtil.createCollectionType(List.class, TblSystemJenkinsParameterValues.class));
		
		systemJenkinsParameter.setCreateBy(userId);
		systemJenkinsParameterMapper.updateSystemJenkinsParameter(systemJenkinsParameter);
		if(systemJenkinsParameterValuesList != null && systemJenkinsParameterValuesList.size() > 0) {
			for (TblSystemJenkinsParameterValues tblSystemJenkinsParameterValues : systemJenkinsParameterValuesList) {
				if(tblSystemJenkinsParameterValues.getId()==null) {  //新增
					tblSystemJenkinsParameterValues.setSystemJenkinsParameterId(id);
					tblSystemJenkinsParameterValues.setStatus(1);
					tblSystemJenkinsParameterValues.setCreateBy(userId);
					systemJenkinsParameterValuesMapper.insertParameterValuesOne(tblSystemJenkinsParameterValues);
				}else {          //修改
					Long parameterValuesId = tblSystemJenkinsParameterValues.getId();
					tblSystemJenkinsParameterValues.setLastUpdateBy(userId);
					systemJenkinsParameterValuesMapper.updateParameterValues(tblSystemJenkinsParameterValues);
				}
			}
		}
		//List<Long> deleteIdList = JsonUtil.fromJson(deleteIds, JsonUtil.createCollectionType(List.class, Long.class));
		
		if(!deleteIds.equals("")) {
			List<Long> deleteIdList =new ArrayList<>();
			String ids[]=deleteIds.split(",");
			for(int i=0;i<ids.length;i++) {
				deleteIdList.add(Long.parseLong(ids[i]));
			}
			systemJenkinsParameterValuesMapper.deleteParameterValues(deleteIdList);
		}
		
		
	}
	/**
	 * 
	* @Title: systemSystemName
	* @Description: 通过系统名获取系统id
	* @author author
	* @param systemName 系统名
	* @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> systemSystemName(String systemName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemName", systemName);
		return systemJenkinsParameterMapper.systemSystemName(map);
	}
	/**
	 *  查询参数 以树的结构显示
	 * @param systemId 系统id
	 * @param request
	 * @return
	 */
	
	@Override
	public Map<String, Object> selectJenkinsTree(Long systemId,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		Object tblToolInfo = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
		Map<String, Object> envMap = JSON.parseObject(tblToolInfo.toString());
		map.put("system_id", systemId);
		//map.put("DEPLOY_STATUS", 1);
		map.put("STATUS", 1);
		//map.put("JOB_TYPE", 2);// 部署
		map.put("create_type", 2);// 手动
		try {
			List<TblSystemJenkins> list = tblSystemJenkinsMapper.selectByMap(map);
			List<TblSystemJenkins> systemJenkins=new ArrayList<>();
			for(TblSystemJenkins tt : list){
				if (tt.getEnvironmentType() != null){
					tt.setRootPom(tt.getId() + "");
					if(judgeEnvByuser(request,systemId,tt.getEnvironmentType().toString())){
						tt.setEnvironmentTypeName(envMap.get(tt.getEnvironmentType().toString()).toString());
						systemJenkins.add(tt);
					}
				}
			}
			
			if (list != null && list.size() > 0) {
				List<ZtreeObj> listZtree=getZtree(list);
				/*for(int i=0;i<listZtree.size();i++) {
					if(listZtree.get(i).getRealId()==null) {
						listZtree.get(i).setNocheck(true);
					}
				}*/
				map.put("ztreeObjs",listZtree );
			} else {
				map.put("ztreeObjs", "");
			}
			map.put("status", "success");
			return map;
		}catch (Exception e) {
			map.put("status", "fail");
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return map;
		}
	}
	private List<ZtreeObj> getZtree(List<TblSystemJenkins> list) {

        String[] a = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getJobPath() == null || list.get(i).getJobPath().equals("/")) {
				String jobName=list.get(i).getJobName();
				jobName=jobName.replaceAll("/","--");
                a[i] = jobName+","+list.get(i).getRootPom()+","+list.get(i).getEnvironmentTypeName();
            } else {
                if (list.get(i).getJobPath().endsWith("/")) {
					String jobName=list.get(i).getJobName();
					jobName=jobName.replaceAll("/","--");
                    a[i] =
                            list.get(i).getJobPath().substring(0, list.get(i).getJobPath().length() - 1)
                                    + "/"
                                    + jobName+","+list.get(i).getRootPom()+","+list.get(i).getEnvironmentTypeName();
                } else {
					String jobName=list.get(i).getJobName();
					jobName=jobName.replaceAll("/","--");
                    a[i] = list.get(i).getJobPath() + "/" + jobName+","+list.get(i).getRootPom()+","+list.get(i).getEnvironmentTypeName();
                }
            }
        }


        List<ZtreeObj> ztreeObjs = new ArrayList<>();

        for (int y = 0; y < a.length; y++) {
            String[] b = a[y].split("/");
            b = deleteArrayNull(b);
            String ids="";
            for (int i = 0; i < b.length; i++) {

                ZtreeObj ztreeObj = new ZtreeObj();
                if (!b[i].equals("")) {
                    if ( b.length == 1) {
                        String[] ps=b[i].split(",");
                        String oldName=ps[0].replaceAll("--","/");
                        String name=oldName;
                        String uid=ps[1];
                        String envName=ps[2];
                        ztreeObj.setRealId(uid);
                        ztreeObj.setDocType(envName);
                        ztreeObj.setName(name);


                        ztreeObj.setpId("0");

                        ztreeObj.setId(b[i]);

                    } else if( b.length == 2) {
                        if(i==0){
                            ztreeObj.setpId("0");
                            ztreeObj.setName(b[i]);
                            ztreeObj.setId(b[i]);
                        } else {
                            String[] ps=b[i].split(",");
							String oldName=ps[0].replaceAll("--","/");
                            String name=oldName;
                            String uid=ps[1];
                            String envName=ps[2];
                            ztreeObj.setRealId(uid);
                            ztreeObj.setDocType(envName);
                            ztreeObj.setName(name);

                            ztreeObj.setpId(b[i - 1]);

                            ztreeObj.setId(b[i]);
                        }
                    }else{
                        if(i==0){
                            ztreeObj.setpId("0");
                            ztreeObj.setName(b[i]);
                            ids = ids + b[i];
                            ztreeObj.setId(ids);
                        } else {
                            if(i==(b.length-1)){
                                String[] ps=b[i].split(",");
								String oldName=ps[0].replaceAll("--","/");
                                String name=oldName;
                                String uid=ps[1];
                                String envName=ps[2];
                                ztreeObj.setRealId(uid);
                                ztreeObj.setDocType(envName);
                                ztreeObj.setName(name);
                                ztreeObj.setpId(ids);
                                ids = ids + b[i];
                                ztreeObj.setId(ids);

                              } else {

                            ztreeObj.setpId(ids);
                            ids = ids + b[i];
                            ztreeObj.setName(b[i]);
                            ztreeObj.setId(ids);
                            }
                        }
                    }

                    ztreeObjs.add(ztreeObj);
                }
            }


        }

        return ztreeObjs;
    }
	 private String[] deleteArrayNull(String string[]) {
			String strArr[] = string;

			// step1: 定义一个list列表，并循环赋值
			ArrayList<String> strList = new ArrayList<String>();
			for (int i = 0; i < strArr.length; i++) {
				strList.add(strArr[i]);
			}

			// step2: 删除list列表中所有的空值
			while (strList.remove(null));
			while (strList.remove(""));

			// step3: 把list列表转换给一个新定义的中间数组，并赋值给它
			String strArrLast[] = strList.toArray(new String[strList.size()]);

			return strArrLast;
		}
	 private boolean judgeEnvByuser(HttpServletRequest request, long systemId, String envType) {
			Long currentUserId = CommonUtil.getCurrentUserId(request);
			Map<String, Object> param = new HashMap<>();

			param.put("SYSTEM_ID", systemId);
			param.put("STATUS", 1);
			param.put("ENVIRONMENT_TYPE", envType);
			List<TblSystemDeployeUserConfig> tblSystemDeployeUserConfigs = tblSystemDeployeUserConfigMapper.selectByMap(param);
			if (tblSystemDeployeUserConfigs != null && tblSystemDeployeUserConfigs.size() > 0) {

				TblSystemDeployeUserConfig tblSystemDeployeUserConfig = tblSystemDeployeUserConfigs.get(0);
				String userIds = tblSystemDeployeUserConfig.getUserIds();
				if (userIds != null) {
					List<String> list = Arrays.asList(userIds.split(","));
					if (list.contains(currentUserId.toString())) {
						return true;
					} else {
						return false;
					}
				}

			}
			return true;
		}
}
