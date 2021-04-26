//package cn.pioneeruniverse.project.controller.projectHome;
//
//import cn.pioneeruniverse.common.utils.CommonUtil;
//import cn.pioneeruniverse.project.dto.*;
//import cn.pioneeruniverse.project.service.projectHome.ProjectHomeService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.sql.Date;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@RestController
//@RequestMapping("projectHome")
//public class ProjectHomeController {
//
//    private static final Logger logger = LoggerFactory.getLogger(ProjectHomeController.class);
//
//
//    @Autowired
//    private ProjectHomeService projectHomeService;
//
//
//    /**
//     * 系统主页数据获取
//     * @param id  项目id
//     * @param type  类型
//     * @return
//     */
//    @RequestMapping(value = "home")
//    public Map selectSystemInfoByID(Integer id ,Integer type, HttpServletRequest request){
//        Long uid  = CommonUtil.getCurrentUserId(request);
//        Map<String,Object> mapValues = new HashMap<>(15);
//        if (id != null && type != null){
//            ProjectHomeDTO projectHome = projectHomeService.selectSystemInfoByID(id);
//            List<PeripheralSystemDTO> peripheralSystemList = projectHomeService.selectSystemInfoByNameList(id);
//            //项目成员
//            List<PeripheralSystemDTO> systemUserNameList = projectHomeService.selectSystemInfoUserNameByIdList(id);
//            List<ProjectNoticeDTO> notice = null;
//            for (int i =0 ; i<systemUserNameList.size(); i++){
//                if (uid == systemUserNameList.get(i).getId() || uid == 1){
//                    //项目公告
//                   notice = projectHomeService.selectProjectNoticeList(id);
//                }
//            }
//            mapValues.put("notice",notice);
//            if (type == 2){ //瀑布模型
//                logger.info("瀑布模型");
//                List<TaskForceDTO> taskForceList = projectHomeService.selectRequirementFeatureCount(id);
//                List<ProjectPlanDTO> projectPlan = projectHomeService.selectProjectPlanById(id);
//                mapValues.put("taskForce",this.TaskForceMethod(taskForceList));
//                mapValues.put("projectPlan",projectPlan);
//            }else{ //敏捷模型
//                logger.info("敏捷模型");
//                SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = new Date(System.currentTimeMillis());
//                String format = simpleFormatter.format(date);
//                List<TaskForceDTO> taskForceAgility = projectHomeService.selectRequirementFeatureAgilityCount(format, id);
//                List<TaskForceDTO> springNextWork = projectHomeService.springNextWork(format, id);
//
//                //燃尽图周期
//                ProjectPlanNewDTO projectPeriod = projectHomeService.selectProjectPeriod(format, id);
//                //燃尽图
//                Map map = projectHomeService.selectProjectPlanNewList(format, id, projectPeriod);
//
//                mapValues.put("taskForceAgility",this.TaskForceMethod(taskForceAgility));
//                mapValues.put("springNextWork",this.TaskForceMethod(springNextWork));
//                mapValues.put("projectPeriod",map.get("projectPeriod"));
//                mapValues.put("period",map.get("period"));
//                mapValues.put("countWorkload",map.get("countWorkload"));
//            }
//            RiskInfoAndChangeInfoDTO riskInfoCount = projectHomeService.selectRiskInfoCount(id);
//            RiskInfoAndChangeInfoDTO weedAdd = projectHomeService.selectWeedAdd(id);
//            RiskInfoAndChangeInfoDTO outStandingNumber = projectHomeService.selectOutStandingNumber(id);
//            RiskInfoAndChangeInfoDTO changeInfoCount = projectHomeService.selectProjectChangeInfoCount(id);
//            RiskInfoAndChangeInfoDTO changeWeedAdd = projectHomeService.selectProjectChangeWeedAdd(id);
//            RiskInfoAndChangeInfoDTO confirmationNumber = projectHomeService.selectConfirmationNumber(id);
//
//            RiskInfoAndChangeInfoDTO riskInfo = new RiskInfoAndChangeInfoDTO();
//            riskInfo.setRiskInfoCount(riskInfoCount.getRiskInfoCount() == null ? 0 : riskInfoCount.getRiskInfoCount());
//            riskInfo.setRiskWeekAdd(weedAdd.getRiskWeekAdd() == null ? 0 : weedAdd.getRiskWeekAdd());
//            riskInfo.setOutStandingNumber(outStandingNumber.getOutStandingNumber() == null ? 0 : outStandingNumber.getOutStandingNumber());
//            riskInfo.setChangeInfoCount(changeInfoCount.getChangeInfoCount() == null ? 0 : changeInfoCount.getChangeInfoCount());
//            riskInfo.setChangeWeekAdd(changeWeedAdd.getChangeWeekAdd() == null ? 0 : changeWeedAdd.getChangeWeekAdd());
//            riskInfo.setConfirmationNumber(confirmationNumber.getConfirmationNumber() == null ? 0 :confirmationNumber.getConfirmationNumber());
//
//            //项目动态
//            List<ProjectNoticeDTO> projectNotice = projectHomeService.selectProjectNoticeList(id);
//            mapValues.put("projectNotice",projectNotice);
//            //项目关联系统
//            List<ProjectHomeDTO> interactedSystem = projectHomeService.projectSystemList(id);
//
//            mapValues.put("projectHome",projectHome);
//            mapValues.put("peripheralSystemList",peripheralSystemList);
//            mapValues.put("systemUserNameList",systemUserNameList);
//            mapValues.put("riskInfo",riskInfo);
//            mapValues.put("id",id);
//            mapValues.put("type",type);
//            mapValues.put("interactedSystem",interactedSystem);
//            mapValues.put("message","success");
//            logger.info("mapValues:"+mapValues);
//            return mapValues;
//        }
//        mapValues.put("message","parameter is null");
//        return mapValues;
//    }
//
//    @RequestMapping(value = "menu")
//    public Map MenuButtonInfo(String url){
//        Map<String,Object> mapValues = new HashMap<>(2);
//        if (url != null){
//            ProjectHomeIndexDTO projectUrl = projectHomeService.selectMenuButtonInfo(url);
//            mapValues.put("projectUrl",projectUrl);
//            mapValues.put("message","success");
//            return  mapValues;
//        }
//
//        mapValues.put("message","parameter is null");
//        return  mapValues;
//
//    }
//
//
//    /**
//     *  任务实施参数处理
//     * @param taskForceList
//     * @return
//     */
//    private static TaskForceDTO TaskForceMethod(List<TaskForceDTO> taskForceList){
//        TaskForceDTO taskForce = new TaskForceDTO();
//        if (taskForceList != null){
//            try{
//                for (int i = 0;i<taskForceList.size();i++){
//                    taskForce.setTaskCount((taskForceList.get(i).getCount() == null ? 0 : taskForceList.get(i).getCount())+(taskForce.getTaskCount() == null ? 0 : taskForce.getTaskCount()));
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 1) taskForce.setToImplement(taskForceList.get(i).getCount());
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 2) taskForce.setInImplementation(taskForceList.get(i).getCount());
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 3) taskForce.setOffTheStocks(taskForceList.get(i).getCount());
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 0) taskForce.setCanceled(taskForceList.get(i).getCount());
//                    if (taskForceList.get(i).getRequirementFeatureStatus() != null && taskForceList.get(i).getRequirementFeatureStatus() == 19) taskForce.setPostponed(taskForceList.get(i).getCount());
//                }
//                return  taskForce;
//            }catch (Exception e){
//                logger.info("任务实施参数处理异常！");
//                e.printStackTrace();
//                return taskForce;
//            }
//
//        }
//        return taskForce;
//    }
//
//
//
//}
