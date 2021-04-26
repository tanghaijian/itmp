package cn.pioneeruniverse.project.common;

import javax.annotation.PostConstruct;

import cn.pioneeruniverse.project.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.pioneeruniverse.project.vo.SynRequirementSystem;
import cn.pioneeruniverse.project.dao.mybatis.SystemInfoMapper;
import cn.pioneeruniverse.project.entity.TblRequirementSystem;

/**
 * it全流程需求系统对象转化
 * @author
 * @version 2020年8月10日 下午2:50:02
 */
@Component
public class SynRequirementSystemUtil {

	@Autowired
	private UserService userService;
	@Autowired
	private SystemInfoMapper systemInfoMapper;
	
	private static SynRequirementSystemUtil synReqSystemUtil;
	
	@PostConstruct  
	public void init() {       
		synReqSystemUtil = this;
		synReqSystemUtil.userService=this.userService;
		synReqSystemUtil.systemInfoMapper=this.systemInfoMapper;
	}
	
	/**
	 * 
	* @Title: SynTblRequirementSystem
	* @Description: 将报文对应的需求和系统关系bean转换为本系统的需求和系统关系bean
	* @author author
	* @param synReqSystem 报文对应的bean
	* @return
	* @throws
	 */
	public static TblRequirementSystem SynTblRequirementSystem(SynRequirementSystem synReqSystem) {
		TblRequirementSystem trs = new TblRequirementSystem();
		if(synReqSystem!=null) {
			trs.setReqSystemId(synReqSystem.getReqsystemId());
			trs.setId(synReqSystem.getReqsystemId());
			trs.setRequirementId(synReqSystem.getReqId());
			trs.setSystemId(getSystemId(synReqSystem.getApplicationCode()));						
			trs.setFunctionCount(getFunctionCount(synReqSystem.getFunctionCount()));
			trs.setMainSystemFlag(getMainSystemFlag(synReqSystem.getIsMainsystem()));
			trs.setDevManageUserId(getUserId(synReqSystem.getAppDevmngcode()));
			trs.setTestManageUserId(getUserId(synReqSystem.getAppTestmngcode()));
		}
		return trs;		
	}
		
	/**
	 * 
	* @Title: getFunctionCount
	* @Description: 功能点处理
	* @author author
	* @param functionCount 报文中字符串形式的功能点数
	* @return
	* @throws
	 */
	public static Long getFunctionCount(String functionCount) {
		Long FunctionCount1=null;
		if(functionCount!= null&&!("").equals(functionCount)) {
			FunctionCount1=Long.valueOf(functionCount);
		}
		return FunctionCount1;
	}
	
	/**
	 * 
	* @Title: getMainSystemFlag
	* @Description: 是否主系统
	* @author author
	* @param isMainsystem null或0:相关系统，1:主系统
	* @return
	* @throws
	 */
	public static Long getMainSystemFlag(String isMainsystem) {
		Long mainSystemFlag=null;
		if(isMainsystem!= null&&!("").equals(isMainsystem)) {
			mainSystemFlag=Long.valueOf(isMainsystem);
		}
		return mainSystemFlag;
	}
	/**
	 * @Description: 根据用户code获取用户id
	 * @author author
	 * @param userCode 用户code
	 * @return Long
	 * @throws
	 */
	public static Long getUserId(String userCode) {
		Long userId = synReqSystemUtil.userService.findIdByUserAccount(userCode);
		if(userId!=null&&userId.longValue()>0) {
			return userId;
		}else {
			return null;
		}
	}
	/**
	 * @Description: 根据系统code获取用户id
	 * @author author
	 * @param systemCode 系统code
	 * @return Long
	 * @throws
	 */
	public static Long getSystemId(String systemCode) {
		Long systemId = synReqSystemUtil.systemInfoMapper.findSystemIdBySystemCode(systemCode);
		 if(systemId!=null&&systemId.longValue()>0) {
			 return systemId;
		 }else {
			 return null;
		 }
	}		
}
