package cn.pioneeruniverse.project.controller.projectPlan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.entity.TblProjectPlanApproveUser;
import cn.pioneeruniverse.project.entity.TblProjectPlanHistory;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.service.projectPlan.ProjectPlanService;

/**
 * Description: 项目计划管理controller
 * Author:
 * Date: 2020/08/11 下午 16:41
 */
@RestController
@RequestMapping("plan")
public class ProjectPlanController extends BaseController {


    @Autowired
    private ProjectPlanService projectPlanService;


   /**
    * 
   * @Title: getProjectPlan
   * @Description: 获取项目计划
   * @author author
   * @param projectId 项目ID
   * @param request
   * @return map data  项目计划列表
		         planStatus 项目计划状态1:草稿，2:待审批，3:审批中 , 4：正式已审批
                 planNumber 项目计划最大版本号
		         approveUserConfig 项目计划审批人
		         userId 当前登录人员
    */
    @RequestMapping(value="getProjectPlan",method=RequestMethod.POST)
    public Map<String, Object> getProjectPlan(Long projectId,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            map = projectPlanService.getProjectPlan(projectId,request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "项目计划查询失败！");
        }
        return map;
    }

    /**
     * 项目第一次提交计划
     * @param projectPlanList 项目计划对象
     * @param projectId 项目id
     * @param request
     * @return map status  1正常 ，2异常
     */
    @RequestMapping(value="insertProjectPlan",method=RequestMethod.POST)
    public Map<String, Object> insertProjectPlan(String projectPlanList,Long projectId, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            projectPlanService.insertProjectPlan(projectId,projectPlanList, request);
            map.put("status", 1);
        } catch (Exception e) {
            return super.handleException(e, "项目计划新增失败！");
        }
        return map;
    }

    /**
     * 提交项目计划变更
     * @param projectId 项目id
     * @param projectPlanList 项目计划对象
     * @param updateMessage 变更说明
     * @param approveUsers 变更审批人
     * @param request
     * @return map status  1正常 ，2异常
     */
    @RequestMapping(value="updateProjectPlan",method=RequestMethod.POST)
    public Map<String, Object> updateProjectPlan(Long projectId,String projectPlanList,
                     String updateMessage,String approveUsers, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            projectPlanService.updateProjectPlan(projectId,projectPlanList,approveUsers,updateMessage, request);
            map.put("status", 1);
        } catch (Exception e) {
            return super.handleException(e, "项目计划变更失败！");
        }
        return map;
    }

    /**
     * 重新提交项目计划变更
     * @param projectId 项目id
     * @param projectPlanList 项目计划对象
     * @param approveRequestId 项目计划审批申请表id
     * @param updateMessage 变更说明
     * @param approveUsers 变更审批人
     * @param request
     * @return map status  1正常 ，2异常
     */
    @RequestMapping(value="updateProjectPlan2",method=RequestMethod.POST)
    public Map<String, Object> updateProjectPlan2(Long projectId,String projectPlanList,Long approveRequestId,
                                     String updateMessage,String approveUsers, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            projectPlanService.updateProjectPlan2(projectId,projectPlanList,approveUsers,
                    updateMessage,approveRequestId, request);
            map.put("status", 1);
        } catch (Exception e) {
            return super.handleException(e, "项目计划变更失败！");
        }
        return map;
    }

    /**
     * 
    * @Title: getAllPlanNumber
    * @Description:获取所有项目计划历史版本号，查看变更历史时，会先选择版本号，此处就是获取此版本号
    * @author author
    * @param projectId 项目ID
    * @return map status  1正常 ，2异常
    *             data 项目版本号list
     */
    @RequestMapping(value="getAllPlanNumber",method=RequestMethod.POST)
    public Map<String, Object> getAllPlanNumber(Long projectId){
        Map<String,Object> map = new HashMap<>();
        try {
            List<TblProjectPlanHistory> list = projectPlanService.getAllPlanNumber(projectId);
            map.put("data",list);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "计划版本查询失败！");
        }
        return map;
    }

    /**
     * 
    * @Title: getPlanNumberByNumber
    * @Description: 通过版本号从项目计划历史中获取项目计划信息
    * @author author
    * @param projectId 项目ID
    * @param planNumber 版本号
    * @return map status  1正常 ，2异常
    *             data   甘特图形式的项目计划信息
     */
    @RequestMapping(value="getPlanNumberByNumber",method=RequestMethod.POST)
    public Map<String, Object> getPlanNumberByNumber(Long projectId,Integer planNumber){
        Map<String,Object> map = new HashMap<>();
        try {
            map = projectPlanService.getPlanNumberByNumber(projectId,planNumber);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "历史版本查询失败！");
        }
        return map;
    }


    /**
     * 获取变更审批页面需要的信息
     * @param projectId 项目id
     * @param approveRequestId 项目计划审批申请表id
     * @return map    requestLog 变更申请日志信息
		              projectPlan 项目计划
		              requestDetail 申请详情
		              approveRequest 变更申请
		              status  1正常 ，2异常
     */
    @RequestMapping(value="getPlanApproveRequest",method=RequestMethod.POST)
    public Map<String, Object> getPlanApproveRequest(Long projectId,Long approveRequestId,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            map = projectPlanService.getPlanApproveRequest(projectId,approveRequestId,request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "审批信息查询失败！");
        }
        return map;
    }

    /**
     * 取消变更
     * @param projectId 项目id
     * @param approveRequestId 项目计划审批申请表id
     * @return map status  1正常 ，2异常
     */
    @RequestMapping(value="callOffUpdate",method=RequestMethod.POST)
    public Map<String, Object> callOffUpdate(Long projectId,Long approveRequestId,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            projectPlanService.callOffUpdate(projectId,approveRequestId,request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "取消变更失败！");
        }
        return map;
    }
    
    /**
     * 
    * @Title: getProjectPlan1
    * @Description: 项目计划信息 
    * @author author
    * @param projectId 项目ID
    * @return map data 项目计划信息，甘特图封装
                  planNumber 版本号
     */
    @RequestMapping(value="getProjectPlan1",method=RequestMethod.POST)
    public Map<String, Object> getProjectPlan1(Long projectId){
        Map<String,Object> map = new HashMap<>();
        try {
            map = projectPlanService.getProjectPlan1(projectId);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "项目计划查询失败！");
        }
        return map;
    }
    /**
     * 审批人审批
     * @param approveUser 项目计划审批人表id
     * @return map status  1正常 ，2异常
     */
    @RequestMapping(value="approve",method=RequestMethod.POST)
    public Map<String, Object> approve(TblProjectPlanApproveUser approveUser, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            projectPlanService.approve(approveUser,request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return super.handleException(e, "审批失败！");
        }
        return map;
    }

    /**
     * 
    * @Title: selectUserInfoVague
    * @Description: 根据用户账号或姓名信息获取用户详情
    * @author author
    * @param userInfo 查询封装的用户信息
    * @return List<TblUserInfo>
     */
    @RequestMapping(value = "selectUserInfoVague",method= RequestMethod.POST)
    public List<TblUserInfo> selectUserInfoVague(TblUserInfo userInfo) {
        return projectPlanService.selectUserInfoVague(userInfo);
    }

    /**
     * 
    * @Title: selectUserInfoVagueInProject
    * @Description: 获取与系统相关的项目下的用户信息，并且满足用户相关筛选信息
    * @author author
    * @param userInfo 用户筛选信息，账号和姓名
    * @param systemId 系统ID
    * @param request
    * @return
     */
    @RequestMapping(value = "selectUserInfoVagueInProject",method= RequestMethod.POST)
    public List<TblUserInfo> selectUserInfoVagueInProject(TblUserInfo userInfo,Long systemId, HttpServletRequest request) {
        return projectPlanService.selectUserInfoVagueInProject(userInfo,systemId,request);
    }

    /**
     * 
    * @Title: getProjectPlanById
    * @Description: 通过ID获取项目计划
    * @author author
    * @param id 项目ID
    * @return map id 
    *             planCode 计划编码
    *             planName 计划名称
    *             planLevel 计划层级
     */
    @RequestMapping(value = "getProjectPlanById",method= RequestMethod.POST)
    public Map<String,Object>  getProjectPlanById(Long id) {
        Map<String,Object> map = projectPlanService.getProjectPlanById(id);
        return map;
    }
}
