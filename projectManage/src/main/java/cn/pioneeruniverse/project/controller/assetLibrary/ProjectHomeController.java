package cn.pioneeruniverse.project.controller.assetLibrary;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.project.dto.*;
import cn.pioneeruniverse.project.service.programProjectHome.ProgramProjectHomeService;
import cn.pioneeruniverse.project.service.projectHome.ProjectHomeService;
import cn.pioneeruniverse.project.vo.MessageInfoVO;
import cn.pioneeruniverse.project.vo.ProgramProjectPlanVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.*;

/**
 * 
* @ClassName: ProjectHomeController
* @Description: 菜单项目管理中的管理主页controller
* @author author
* @date 2020年8月5日 下午2:41:31
*
 */
@RestController
@RequestMapping("projectHome")
public class ProjectHomeController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectHomeController.class);


    @Autowired
    private ProjectHomeService projectHomeService;

    @Autowired
    private ProgramProjectHomeService programProjectHomeService;


    /**
     * 系统主页数据获取
     * @param id  项目id
     * @param type  类型
     * @return map key值对应项目管理首页的每个展示元素
     */
    @RequestMapping(value = "home")
    public Map selectSystemInfoByID(Integer id ,Integer type, HttpServletRequest request){
        Map<String,Object> mapValues = new HashMap<>(20);
        if (id != null && type != null){
            String format = DateUtil.formatDate(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
            ProjectHomeDTO projectHome = projectHomeService.selectSystemInfoByID(id);
            List<PeripheralSystemDTO> peripheralSystemList = projectHomeService.selectSystemInfoByNameList(id);
            //项目成员
            Long uid  = CommonUtil.getCurrentUserId(request);
            Map<String, Object> homeMap = projectHomeService.selectSystemInfoUserNameByIdList(id, uid);
            //项目公告
            mapValues.put("notice",homeMap.get("notice"));

            if (type == 2){ //瀑布模型
                List<ProjectPlanDTO> projectPlan = projectHomeService.selectProjectPlanById(id);
                Map dataDicMap = projectHomeService.selectRequirementFeatureCount(id);
                //项目计划
                mapValues.put("projectPlan",projectPlan);
                //数据字典资源
                mapValues.put("dadaDicSource",dataDicMap);
            }else{ //敏捷模型
                Map dataDicMap = projectHomeService.selectRequirementFeatureAgilityCount(format, id);
                Map springNextWork = projectHomeService.springNextWork(format, id);
               //数据字典
                mapValues.put("dadaDicSource",dataDicMap);
                //下一次冲刺
                mapValues.put("nextDadaDicSource",springNextWork);
                //燃尽图周期
                ProjectPlanNewDTO projectPeriod = projectHomeService.selectProjectPeriod(format, id);
                //燃尽图
                if (projectPeriod != null){
                    Map map = projectHomeService.selectProjectPlanNewList(format, id, projectPeriod, projectPeriod.getSprintListId());
                    mapValues.put("projectPeriod",map.get("projectPeriod"));//燃尽图信息 ProjectPlanNewDTO
                    mapValues.put("period",map.get("period"));//统计的数量
                    mapValues.put("countWorkload",map.get("countWorkload"));//工作量
                }
            }
            //风险和变更
            RiskInfoAndChangeInfoDTO riskInfoCount = projectHomeService.selectRiskInfoCount(id);
            RiskInfoAndChangeInfoDTO weedAdd = projectHomeService.selectWeedAdd(id);
            RiskInfoAndChangeInfoDTO outStandingNumber = projectHomeService.selectOutStandingNumber(id);
            RiskInfoAndChangeInfoDTO changeInfoCount = projectHomeService.selectProjectChangeInfoCount(id);
            RiskInfoAndChangeInfoDTO changeWeedAdd = projectHomeService.selectProjectChangeWeedAdd(id);
            RiskInfoAndChangeInfoDTO confirmationNumber = projectHomeService.selectConfirmationNumber(id);
            RiskInfoAndChangeInfoDTO riskInfo = new RiskInfoAndChangeInfoDTO();
            riskInfo.setRiskInfoCount(riskInfoCount.getRiskInfoCount() == null ? 0 : riskInfoCount.getRiskInfoCount());
            riskInfo.setRiskWeekAdd(weedAdd.getRiskWeekAdd() == null ? 0 : weedAdd.getRiskWeekAdd());
            riskInfo.setOutStandingNumber(outStandingNumber.getOutStandingNumber() == null ? 0 : outStandingNumber.getOutStandingNumber());
            riskInfo.setChangeInfoCount(changeInfoCount.getChangeInfoCount() == null ? 0 : changeInfoCount.getChangeInfoCount());
            riskInfo.setChangeWeekAdd(changeWeedAdd.getChangeWeekAdd() == null ? 0 : changeWeedAdd.getChangeWeekAdd());
            riskInfo.setConfirmationNumber(confirmationNumber.getConfirmationNumber() == null ? 0 :confirmationNumber.getConfirmationNumber());
            //项目关联系统
            List<ProjectHomeDTO> interactedSystem = projectHomeService.projectSystemList(id);
            //项目动态
            List<ProjectNoticeDTO> projectNotice = projectHomeService.selectProjectNoticeList(format,id);
            mapValues.put("projectNotice",projectNotice);
            //项目动态
            List<MessageInfoVO>  messageInfo = projectHomeService.selectMessageInfoList(id);
            mapValues.put("messageInfo",messageInfo);
            //项目计划
            List<ProgramProjectPlanVO> programProjectPlanVOS = programProjectHomeService.selectProgramProjectPlanList(format,id);
            mapValues.put("plan",programProjectPlanVOS);
            mapValues.put("projectHome",projectHome);
            mapValues.put("peripheralSystemList",peripheralSystemList);
            mapValues.put("systemUserNameList",homeMap.get("systemUserNameList"));
            mapValues.put("riskInfo",riskInfo);
            mapValues.put("id",id);
            mapValues.put("type",type);
            mapValues.put("interactedSystem",interactedSystem);
            return mapValues;
        }

        mapValues.put("message","parameter is null");
        return mapValues;
    }

    /**
     * 
    * @Title: MenuButtonInfo
    * @Description: 获取当前URL下的菜单按钮信息，右上角导航按钮连接页面的按钮信息
    * @author author
    * @param url 当前页面URL
    * @return map projectUrl 项目主页url  message:返回信息
    * @throws
     */
    @RequestMapping(value = "menu")
    public Map MenuButtonInfo(String url){
        Map<String,Object> mapValues = new HashMap<>(2);
        if (url != null){
            ProjectHomeIndexDTO projectUrl = projectHomeService.selectMenuButtonInfo(url);
            mapValues.put("projectUrl",projectUrl);
            mapValues.put("message","success");
            return  mapValues;
        }
        mapValues.put("message","parameter is null");
        return  mapValues;
    }

    /**
     * 通过名称获取菜单按钮信息
     * @param menuName 菜单名
     * @return map projectUrl 项目主页url  message:返回信息
     */
    @RequestMapping(value = "selectMenuButtonInfo")
    public Map selectMenuButtonInfo(String menuName){
        Map<String,Object> mapValues = new HashMap<>(2);
        if (menuName != null){
            List<ProjectHomeIndexDTO> buttonInfo = projectHomeService.selectMenuButtonInfoById(menuName);
            mapValues.put("projectUrl",buttonInfo);
            mapValues.put("message","success");
            return  mapValues;
        }
        mapValues.put("message","parameter is null");
        return  mapValues;
    }

    /**
     *  任务实施参数处理
     * @param taskForceList
     * @return
     */
    private static TaskForceDTO TaskForceMethod(List<TaskForceDTO> taskForceList){
        TaskForceDTO taskForce = new TaskForceDTO();
        if (!taskForceList.isEmpty()){
            try{
                for (int i = 0;i<taskForceList.size();i++){
//                    taskForce.setTaskCount((taskForceList.get(i).getCount() == null ? 0 : taskForceList.get(i).getCount())+(taskForce.getTaskCount() == null ? 0 : taskForce.getTaskCount()));
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 1) taskForce.setToImplement(taskForceList.get(i).getCount());
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 2) taskForce.setInImplementation(taskForceList.get(i).getCount());
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 3) taskForce.setOffTheStocks(taskForceList.get(i).getCount());
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 0) taskForce.setCanceled(taskForceList.get(i).getCount());
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 19) taskForce.setPostponed(taskForceList.get(i).getCount());
                }
                return  taskForce;
            }catch (Exception e){
                logger.error("任务实施参数处理异常,{}"+e.getMessage());
                return taskForce;
            }

        }
        return taskForce;
    }


}
