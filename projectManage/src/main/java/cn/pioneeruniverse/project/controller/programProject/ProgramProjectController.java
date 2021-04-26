package cn.pioneeruniverse.project.controller.programProject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.project.dto.ProjectNoticeDTO;
import cn.pioneeruniverse.project.dto.ProjectPlanDTO;
import cn.pioneeruniverse.project.dto.ProjectPlanNewDTO;
import cn.pioneeruniverse.project.dto.RiskInfoAndChangeInfoDTO;
import cn.pioneeruniverse.project.service.programProjectHome.ProgramProjectHomeService;
import cn.pioneeruniverse.project.service.programProjectHome.impl.ProgramProjectHomeImpl;
import cn.pioneeruniverse.project.vo.MessageInfoVO;
import cn.pioneeruniverse.project.vo.Milestones;
import cn.pioneeruniverse.project.vo.ProgramInfoHomeVO;
import cn.pioneeruniverse.project.vo.ProgramProjectVO;
import cn.pioneeruniverse.project.vo.ProgramVO;
import cn.pioneeruniverse.project.vo.SpringInfoVO;

/**
 * 项目群
 */
@RestController
@RequestMapping("program")
public class ProgramProjectController {




    @Autowired
    private ProgramProjectHomeService programProjectHomeService;



    /**
     * 
    * @Title: programProject
    * @Description: 项目群管理首页
    * @author author
    * @param id 项目群ID
    * @return Map<String ,Object>
     */
    @RequestMapping(value = "programProjectHome")
    public Map programProject(Integer id){
        Map<String ,Object> mapValues = new HashMap<>();
        if (id != null){
        	//项目群概况
            ProgramInfoHomeVO programInfoHome = programProjectHomeService.selectProgramInfoHomeByID(id);
            //项目群中子项目
            List<ProgramProjectVO> programProject = programProjectHomeService.selectProgramProjectList(id);
            //风险数
            RiskInfoAndChangeInfoDTO riskInfoCount = programProjectHomeService.selectProgramRiskInfoCount(id);
            //本周新增风险
            RiskInfoAndChangeInfoDTO riskWeekAdd = programProjectHomeService.selectProgramWeedAdd(id);
            //未解决风险
            RiskInfoAndChangeInfoDTO outStandingNumber = programProjectHomeService.selectProgramOutStandingNumber(id);
            //总变更数
            RiskInfoAndChangeInfoDTO changeInfoCount = programProjectHomeService.selectProgramProjectChangeInfoCount(id);
            //本周新增变更
            RiskInfoAndChangeInfoDTO changeWeekAdd = programProjectHomeService.selectProgramProjectChangeWeedAdd(id);
            //待确认变更
            RiskInfoAndChangeInfoDTO confirmationNumber = programProjectHomeService.selectProgramConfirmationNumber(id);

            //重新组装项目群变更 和 风险信息，前台直接展示
            RiskInfoAndChangeInfoDTO riskInfoAndChangeInfo = new RiskInfoAndChangeInfoDTO();
            riskInfoAndChangeInfo.setRiskInfoCount(riskInfoCount.getRiskInfoCount());
            riskInfoAndChangeInfo.setRiskWeekAdd(riskWeekAdd.getRiskWeekAdd());
            riskInfoAndChangeInfo.setOutStandingNumber(outStandingNumber.getOutStandingNumber());
            riskInfoAndChangeInfo.setChangeInfoCount(changeInfoCount.getChangeInfoCount());
            riskInfoAndChangeInfo.setChangeWeekAdd(changeWeekAdd.getChangeWeekAdd());
            riskInfoAndChangeInfo.setConfirmationNumber(confirmationNumber.getConfirmationNumber());

            //项目群过程
            List<ProjectPlanDTO> programProjectPlan = programProjectHomeService.selectProgramProjectPlan(id);
            for (int i =0 ;i<programProject.size();i++){
                List<Milestones> milestonesList = new ArrayList<>();
                for (int j =0;j<programProjectPlan.size();j++){
                    if (programProject.get(i).getProjectId().equals(programProjectPlan.get(j).getProjectId())){
                        Milestones milestones = new Milestones();
                        milestones.setName(programProjectPlan.get(j).getPlanName());
                        milestones.setDate(programProjectPlan.get(j).getPlanStartDate()+"~"+programProjectPlan.get(j).getPlanEndDate());
                        milestones.setProgress(programProjectPlan.get(j).getCurrentProgress());
                        milestonesList.add(milestones);
                        programProject.get(i).setMilestones(milestonesList);
                    }
                }
            }

            //燃尽图周期
            SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String currentDate = simpleFormatter.format(date);
            List<ProjectPlanNewDTO> program = programProjectHomeService.selectProgramProjectPeriod(currentDate, id);
            List<ProgramVO> plan= ProgramProjectHomeImpl.Date(program);
            //项目群燃尽图
            List<ProjectPlanNewDTO> projectPlanNew = programProjectHomeService.selectProgramProjectPlanNewList(currentDate, id, program);
            //数据处理
            plan = this.Program(plan, projectPlanNew,programProject);
            //项目群公告
            List<ProjectNoticeDTO> projectNotice = programProjectHomeService.selectProgramProjectNotice(currentDate, id);
            //项目群动态
            List<String> projectId = new ArrayList<>();
            List<MessageInfoVO> messageInfo = new ArrayList<>();
            if (programProject != null && programProject.size()>0){
                for (int i=0; i<programProject.size();i++){
                    projectId.add(String.valueOf(programProject.get(i).getProjectId()));
                }
                messageInfo =  programProjectHomeService.selectProgramMessageInfoList(projectId);
            }

            //项目群计划
            mapValues.put("plan",plan);
            //项目群概况
            mapValues.put("programInfoHome",programInfoHome);
            //项目群子项目
            mapValues.put("programProject",programProject);
            //项目群风险和变更
            mapValues.put("riskInfoAndChangeInfo",riskInfoAndChangeInfo);
            //项目公告:最顶层的消息
            mapValues.put("projectNotice",projectNotice);
            //项目动态，一般指新项目，工作分配等等
            mapValues.put("messageInfo",messageInfo);
            //方法调用结果，前端判断用
            mapValues.put("message","success");
            return  mapValues;
        }
        mapValues.put("message","parameter is null");
        return mapValues;
    }


    /**
     * 
    * @Title: Program
    * @Description: 组装项目计划信息
    * @author author
    * @param plan 项目群计划
    * @param projectPlanNew 
    * @param programProject
    * @return List<ProgramVO>
     */
    private  List<ProgramVO> Program( List<ProgramVO> plan, List<ProjectPlanNewDTO> projectPlanNew,List<ProgramProjectVO> programProject){
        if (plan != null && projectPlanNew != null){
            try{
                for (int i =0 ;i<plan.size();i++){
                    double count =0;
                    List<SpringInfoVO> milestones = plan.get(i).getMilestones();
                    plan.get(i).setMinus(milestones.size()-1);
                    for (int j=0;j<projectPlanNew.size();j++){
                        if (plan.get(i).getProjectId().equals(projectPlanNew.get(j).getProjectId())){
                            for (int k =0;k<milestones.size();k++){
                                if (milestones.get(k).getDataTime().equals(projectPlanNew.get(j).getCreateDate().toString())){
                                    milestones.get(k).setEstimateWorkload(projectPlanNew.get(j).getEstimateWorkload());
                                    milestones.get(k).setEstimateRemainWorkload(projectPlanNew.get(j).getEstimateRemainWorkload());
                                }
                            }
                            count = count +projectPlanNew.get(j).getEstimateWorkload();
                        }
                    }
                    plan.get(i).setProjectCount(count);
                    plan.get(i).setDevelopmentMode(1);
                    plan.get(i).setMilestones(milestones);
                }

                for (int i =0; i<plan.size();i++){
                    for (int j =0; j<programProject.size();j++){
                        if (plan.get(i).getProjectId().equals(programProject.get(j).getProjectId())){
                            plan.get(i).setProjectName(programProject.get(j).getProjectName());
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
                return plan;
            }

        }
        return plan;
    }

}
