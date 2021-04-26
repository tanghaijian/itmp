package cn.pioneeruniverse.dev.feignInterface;


import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.dev.feignFallback.DevManageToTestManageFallback;

/**
 * 
* @ClassName: DevManageToTestManageInterface
* @Description: 开发模块到测试模块的feign调用
* @author author
* @date 2020年8月12日 下午9:54:13
*
 */
@FeignClient(value = "testManage", fallbackFactory = DevManageToTestManageFallback.class)
public interface DevManageToTestManageInterface {

	/**
	 * 
	* @Title: syncDefect
	* @Description: 开发缺陷同步到测试缺陷
	* @author author
	* @param objectJson {opt:'',value:''} opt操作：insert,update,remove,delete  value:缺陷信息
	* @return map key -status=1正常，2异常
	* @throws
	 */
    @RequestMapping(value = "defect/syncDefect", method = RequestMethod.POST)
    Map<String,Object> syncDefect(@RequestBody String objectJson);

    /**
     * 
    * @Title: syncDefectAtt
    * @Description: 同步附件
    * @author author
    * @param objectJson {opt:'',value:''} opt操作：insert，remove  value：附件信息
    * @return map key -status=1正常，2异常
    * @throws
     */
    @RequestMapping(value = "defect/syncDefectAtt", method = RequestMethod.POST)
    Map<String,Object> syncDefectAtt(@RequestBody String objectJson);
    
    /**
     * 
    * @Title: synReqFeatureDeployStatus
    * @Description: 同步开发任务的部署状态到测试任务的部署状态
    * @author author
    * @param requirementId 需求ID
    * @param systemId 系统ID
    * @param deployStatus 部署状态
    * @param loginfo 日志信息
    * @return map key -status=1正常，2异常
    * @throws
     */
    @RequestMapping(value ="testtask/synReqFeatureDeployStatus",method = RequestMethod.POST)
    Map<String, Object> synReqFeatureDeployStatus( @RequestParam("requirementId") Long requirementId,@RequestParam("systemId")
            Long systemId,@RequestParam("deployStatus") String deployStatus, @RequestParam("loginfo")String loginfo);
    
    /**
     * 
    * @Title: synReqFeatureDeployStatus1
    * @Description: 同步开发任务的部署状态到测试任务的部署状态：通过问题编号查找测试任务
    * @author author
    * @param questionNumber 问题编号
    * @param deployStatus 部署状态
    * @param loginfo 日志信息
    * @return map key -status=1正常，2异常
    * @throws
     */
    @RequestMapping(value ="testtask/synReqFeatureDeployStatus1",method = RequestMethod.POST)
    Map<String, Object> synReqFeatureDeployStatus1( @RequestParam("questionNumber")String questionNumber,
           @RequestParam("deployStatus") String deployStatus, @RequestParam("loginfo")String loginfo);
   
    /**
     * 
    * @Title: insertDefectAttachement
    * @Description: 同步测试缺陷附件(未用)
    * @author author
    * @param defectAttache 附件信息
    * @return map key -status=1正常，2异常
    * @throws
     */
    @RequestMapping(value ="defect/insertDefectAttachement",method = RequestMethod.POST)
    Map<String,Object> insertDefectAttachement(@RequestBody String defectAttache);

    /**
     * 
    * @Title: synReqFeaturewindow
    * @Description: 同步开发任务投产窗口到测试任务投产窗口
    * @author author
    * @param requirementId 需求ID
    * @param systemId 系统ID
    * @param commissioningWindowId 投产窗口ID
    * @param loginfo 日志
    * @param beforeName 变更前投产窗口名
    * @param afterName 变更后投产窗口名
    * @return map key -status=1正常，2异常
    * @throws
     */
    @RequestMapping(value ="testtask/synReqFeaturewindow",method = RequestMethod.POST)
    Map<String, Object> synReqFeaturewindow(@RequestParam("requirementId")Long requirementId, @RequestParam("systemId")Long systemId, @RequestParam("commissioningWindowId")Long commissioningWindowId, @RequestParam("loginfo") String loginfo, @RequestParam("beforeName")String beforeName, @RequestParam("afterName")String afterName);

    /**
     * 
    * @Title: synReqFeaturewindow1
    * @Description: 同步开发任务投产窗口到测试任务投产窗口：通过问题编号关联测试任务
    * @author author
    * @param questionNumber 问题编号
    * @param commissioningWindowId 投产窗口ID
    * @param loginfo 日志
    * @param beforeName 变更前投产窗口名
    * @param afterName 变更后投产窗口名
    * @return map key -status=1正常，2异常
    * @throws
     */
    @RequestMapping(value ="testtask/synReqFeaturewindow1",method = RequestMethod.POST)
    Map<String, Object> synReqFeaturewindow1(@RequestParam("questionNumber")String questionNumber, @RequestParam("commissioningWindowId")Long commissioningWindowId, @RequestParam("loginfo") String loginfo, @RequestParam("beforeName")String beforeName, @RequestParam("afterName")String afterName);

    /**
     * 
    * @Title: synReqFeatureDept
    * @Description: 同步开发任务所属部门到测试任务所属部门
    * @author author
    * @param requirementId 需求ID
    * @param systemId 系统ID
    * @param deptId 部门ID
    * @param loginfo 日志
    * @param deptBeforeName 变更前部门名
    * @param deptAfterName 变更后部门名
    * @return map key -status=1正常，2异常
    * @throws
     */
    @RequestMapping(value="testtask/synReqFeatureDept",method = RequestMethod.POST)
    Map<String, Object> synReqFeatureDept(@RequestParam("requirementId")Long requirementId, @RequestParam("systemId")Long systemId, @RequestParam("deptId")Integer deptId, @RequestParam("loginfo")String loginfo, @RequestParam("deptBeforeName")String deptBeforeName,
			@RequestParam("deptAfterName")String deptAfterName);


    /**
     * 
    * @Title: detailEnvDate
    * @Description:更新测试任务提交系测时间和版测时间
    * @author author
    * @param list 封装的map key requirementId  ,key2-systemId
    * @param envName 环境名称，系测版测之类
    * @param timestamp 时间
    * @return map key -status=1正常，2异常
    * @throws
     */
    @RequestMapping(value ="testtask/detailEnvDate",method = RequestMethod.POST)
    Map<String,Object> detailEnvDate(@RequestParam("list")String list, @RequestParam("envName")String envName, @RequestParam("timestamp")String timestamp);

    /**
     * 
    * @Title: test1
    * @Description: （未用）
    * @author author
    * @param envName
    * @return
    * @throws
     */
    @RequestMapping(value ="testtask/test1",method = RequestMethod.POST)
    Map<String,Object> test1( @RequestParam("envName")String envName);

    /**
     * 
    * @Title: getNewFeatureCode
    * @Description: 获取任务编号：有一定生成规则
    * @author author
    * @return 任务编号
    * @throws
     */
    @RequestMapping(value = "testtask/getNewFeatureCode",method=RequestMethod.POST)
    String getNewFeatureCode();
}
