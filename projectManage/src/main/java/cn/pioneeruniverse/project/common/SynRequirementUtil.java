package cn.pioneeruniverse.project.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.pioneeruniverse.project.vo.SynRequirement;
import cn.pioneeruniverse.project.dao.mybatis.RequirementMapper;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.service.dept.DeptInfoService;
import cn.pioneeruniverse.project.service.user.UserService;

/**
 * it全流程需求对象转化
 * @author
 * @version 2020年8月10日 下午2:50:02
 */
@Component
public class SynRequirementUtil {

	@Autowired
	private UserService userService;
	@Autowired
	private DeptInfoService deptInfoService;	
	@Autowired
	private RequirementMapper tblRequirementInfoMapper;
	
	private static SynRequirementUtil synRequirementUtil;
	
	@PostConstruct  
	public void init() {       
		synRequirementUtil = this; 
		synRequirementUtil.userService = this.userService;
		synRequirementUtil.deptInfoService = this.deptInfoService;
		synRequirementUtil.tblRequirementInfoMapper = this.tblRequirementInfoMapper;
	}
	
	/**
	 * 
	* @Title: SynTblRequirementInfo
	* @Description: 将报文对应的需求bean转换为本系统对应的需求bean
	* @author author
	* @param synReq
	* @return
	* @throws
	 */
	public static TblRequirementInfo SynTblRequirementInfo(SynRequirement synReq) {
		TblRequirementInfo tri = new TblRequirementInfo();
		if(synReq!=null) {
			tri.setItcdRequirementId(synReq.getReqId());
			tri.setRequirementCode(synReq.getReqCode());
			tri.setRequirementName(synReq.getReqName());						
			tri.setRequirementStatus(synReq.getReqStatus());	
			tri.setRequirementSource(synReq.getReqResource());			
			tri.setRequirementType(synReq.getReqType());
			tri.setRequirementPlan(synReq.getReqPlan());
			tri.setRequirementPriority(synReq.getReqPriority());
			tri.setExpectOnlineDate(synReq.getReqExpectdate());
			tri.setPlanOnlineDate(synReq.getReqPlandate());
			tri.setActualOnlineDate(synReq.getReqActdate());
			tri.setApplyUserId(getUserId(synReq.getCreateEmpcode()));			
			tri.setApplyDeptId(getDeptId(synReq.getReqDepcode()));			
			tri.setDevelopmentDeptId(getDeptId(synReq.getReqDevsectioncode()));			
			tri.setOpenDate(synReq.getOpenDate());
			if(synReq.getReqDescription()!=null&&synReq.getReqDescription().length()>200){
				String overview = synReq.getReqDescription().substring(0,200);
				synReq.setReqDescription(overview);
			}
			tri.setRequirementOverview(synReq.getReqDescription());
			tri.setImportantRequirementStatus(synReq.getIsKeyReq());
			tri.setImportantRequirementType(synReq.getKeyReqType());
			tri.setImportantRequirementDelayStatus(synReq.getIsKeyReqDelay());
			tri.setImportantRequirementOnlineQuarter(synReq.getKeyReqPlanOnlineQuarter());
			tri.setImportantRequirementDelayReason(synReq.getKeyReqDelayReason());			
			tri.setDirectIncome(synReq.getReqDirectincome());
			tri.setForwardIncome(synReq.getReqForwardincome());
			tri.setRecessiveIncome(synReq.getReqRecessiveincome());
			tri.setDirectCostReduction(synReq.getReqDirectcost());
			tri.setForwardCostReduction(synReq.getReqForwardcost());
			tri.setAnticipatedIncome(synReq.getAnticipated());
			tri.setEstimateCost(synReq.getItestimatethecost());
			tri.setHangupStatus(synReq.getReqHangup());
			tri.setHangupDate(synReq.getHangupDate());
			tri.setChangeCount(synReq.getReqModifycount());
			tri.setDevelopmentManageUserId(getUserId(synReq.getReqDevmngcode()));
			tri.setRequirementManageUserId(getUserId(synReq.getReqReqmngempcode()));
			tri.setRequirementAcceptanceUserId(getUserId(synReq.getReqitacceptance()));
			tri.setRequirementProperty(synReq.getReqProperty());
			tri.setRequirementClassify(synReq.getItReqClassify());
			tri.setRequirementSubdivision(synReq.getReqSubdivision());
			tri.setPlanIntegrationTestDate(synReq.getReqCoupletTime());
			tri.setActualIntegrationTestDate(synReq.getReqActCouupletTime());
			tri.setAcceptanceDescription(synReq.getAcceptanceDescription());
			tri.setAcceptanceTimeliness(synReq.getReqAcceptance());
			tri.setDataMigrationStatus(getNumber(synReq.getIsOlddata()));
			if(synReq.getTotalWorkload()!=null) {
				tri.setWorkload(synReq.getTotalWorkload());
			}
			tri.setParentId(getReqId(synReq.getParentReq()));
			tri.setStatus(1);
			tri.setCreateBy((long) 1);
			tri.setLastUpdateBy((long) 1);
			if(synReq.getCreateDate()==null||synReq.getCreateDate().toString().equals("")) {
				tri.setCreateDate(DateToTimestamp(new Date()));				
			}else {
				tri.setCreateDate(DateToTimestamp(synReq.getCreateDate()));				
			}
			if(synReq.getUdpateDate()!=null||synReq.getUdpateDate().toString().equals("")) {
				tri.setLastUpdateDate(DateToTimestamp(new Date()));
			}else {				
				tri.setLastUpdateDate(DateToTimestamp(synReq.getUdpateDate()));
			}
		}
		return tri;		
	}

	/**
	 * @Description: 根据用户code获取用户id
	 * @author author
	 * @param userCode 用户code
	 * @return Long
	 * @throws
	 */
	public static Long getUserId(String userCode) {
		 Long userId = synRequirementUtil.userService.findIdByUserAccount(userCode);
		 if(userId!=null&&userId.longValue()>0) {
			 return userId;
		 }else {
			 return null;
		 }
	}
	/**
	 * @Description: 根据部门code获取用户id
	 * @author author
	 * @param deptCode 部门code
	 * @return Long
	 * @throws
	 */
	public static Long getDeptId(String deptCode) {
		 Long deptId = synRequirementUtil.deptInfoService.findIdByDeptNumber(deptCode);
		 if(deptId!=null&&deptId.longValue()>0) {
			 return deptId;
		 }else {
			 return null;
		 }
	}
	
	public static Timestamp DateToTimestamp(Date date) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());  
		String dateStr = "";  
        //format的格式可以任意           
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        try {  
            dateStr = sdf.format(date);  
            ts = Timestamp.valueOf(dateStr);
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return ts;  
	}
	
	public static String getNumber(String id) {
		if(id!= null&&("0").equals(id)) {
			id="2";
		}else{
			id=null;
		}
		return id;
	}
	/**
	 * @Description: 获取it全流程平台需求id所对应的itmgr平台的需求id
	 * @author author
	 * @param reqCode 需求code
	 * @return Long
	 * @throws
	 */
	public static Long getReqId(Long reqCode) {
		if(reqCode!=null&&reqCode.longValue()>0) {
			TblRequirementInfo requirementInfo = synRequirementUtil.tblRequirementInfoMapper.getRequirementByItcd(reqCode);
			if(requirementInfo!=null&&requirementInfo.getId()!=null) {
				reqCode= requirementInfo.getId();
			}else {
				reqCode= null;
			}
		}
		return reqCode;
	}
}
