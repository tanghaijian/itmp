package cn.pioneeruniverse.project.service.programProjectHome.impl;

import cn.pioneeruniverse.project.dao.mybatis.programProject.ProgramProjectMapper;
import cn.pioneeruniverse.project.dto.*;
import cn.pioneeruniverse.project.service.programProjectHome.ProgramProjectHomeService;
import cn.pioneeruniverse.project.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProgramProjectHomeImpl implements ProgramProjectHomeService {

    @Autowired
    private ProgramProjectMapper programProjectMapper;

    /**
     *  项目群基本信息
     * @param programId 项目群id
     * @return ProgramInfoHomeVO
     */
    @Override
    public ProgramInfoHomeVO selectProgramInfoHomeByID(Integer programId) {
        return programProjectMapper.selectProgramInfoHomeByID(programId);
    }

    /**
     * 项目群子项目
     * @param programId 项目群id
     * @return List<ProgramProjectVO>
     */
    @Override
    public List<ProgramProjectVO> selectProgramProjectList(Integer programId) {
        return programProjectMapper.selectProgramProjectList(programId);
    }

    /**
     * 项目群风险信息总数
     * @param programId 项目群id
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectProgramRiskInfoCount(Integer programId) {
        return programProjectMapper.selectProgramRiskInfoCount(programId);
    }

    /**
     * 项目群风险本周新增
     * @param programId 项目群id
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectProgramWeedAdd(Integer programId) {
        return programProjectMapper.selectProgramWeedAdd(programId);
    }

    /**
     *  项目群风险未解决数
     * @param programId 项目群id
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectProgramOutStandingNumber(Integer programId) {
        return programProjectMapper.selectProgramOutStandingNumber(programId);
    }

    /**
     * 项目群变更总数
     * @param programId 项目群id
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectProgramProjectChangeInfoCount(Integer programId) {
        return programProjectMapper.selectProgramProjectChangeInfoCount(programId);
    }

    /**
     * 项目群变更本周新增
     * @param programId 项目群id
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectProgramProjectChangeWeedAdd(Integer programId) {
        return programProjectMapper.selectProgramProjectChangeWeedAdd(programId);
    }

    /**
     * 项目群变更未解决数
     * @param programId 项目群id
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectProgramConfirmationNumber(Integer programId) {
        return programProjectMapper.selectProgramConfirmationNumber(programId);
    }

    /**
     * 项目群里程碑
     * @param programId 项目群id
     * @return List<ProjectPlanDTO>
     */
    @Override
    public List<ProjectPlanDTO> selectProgramProjectPlan(Integer programId) {
    return programProjectMapper.selectProgramProjectPlan(programId);
    }

    /**
     *  项目群冲刺表项目周期
     * @param currentDate 当天
     * @param programId 项目群id
     * @return List<ProjectPlanNewDTO>
     */
    @Override
    public List<ProjectPlanNewDTO> selectProgramProjectPeriod(String currentDate, Integer programId) {
        return programProjectMapper.selectProgramProjectPeriod(currentDate,programId);
    }

    /**
     * 项目群燃尽图(预计工作量)
     * @param currentDate 当天
     * @param programId 项目群id
     * @param projectPlan 项目计划
     * @return List<ProjectPlanNewDTO>
     */
    @Override
    public List<ProjectPlanNewDTO> selectProgramProjectPlanNewList(String currentDate, Integer programId, List<ProjectPlanNewDTO> projectPlan) {
        List<ProjectPlanNewDTO> projectPlanNewDTOS = programProjectMapper.selectProgramProjectPlanNewList(currentDate, programId, projectPlan);
        if (projectPlanNewDTOS != null && projectPlanNewDTOS.size()>0 && projectPlan != null && projectPlan.size()>0){
            List<Long> sprintId = new ArrayList<>();
            for (int i =0 ;i<projectPlan.size();i++){
                sprintId.add(projectPlan.get(i).getSprintId());
            }
            List<ProjectPlanNewDTO> projectPlanNew = programProjectMapper.selectEstimateRemainWorkloadNewList(currentDate, programId, sprintId);
            if (projectPlanNew != null && projectPlanNew.size()>0){
                for(int i =0;i<projectPlanNewDTOS.size();i++){
                    for (int j =0;j<projectPlanNew.size();j++){
                        if (projectPlanNewDTOS.get(i).getProjectId().equals(projectPlanNew.get(j).getProjectId()) && projectPlanNewDTOS.get(i).getCreateDate().toString().equals(projectPlanNew.get(j).getCreateDate().toString())){
                            projectPlanNewDTOS.get(i).setEstimateRemainWorkload(projectPlanNew.get(j).getEstimateRemainWorkload());
                        }
                    }
                }
            }

        }
        return projectPlanNewDTOS;
    }

    /**
     * 项目群公告
     * @param currentDate 当天
     * @param programId 项目群id
     * @return List<ProjectNoticeDTO>
     */
    @Override
    public List<ProjectNoticeDTO> selectProgramProjectNotice(String currentDate, Integer programId) {
        return programProjectMapper.selectProgramProjectNotice(currentDate,programId);
    }

    /**
     *  获取项目计划表信息
     * @param programId 项目群id
     * @return List<ProgramProjectPlanVO>
     */
    @Override
    public List<ProgramProjectPlanVO> selectProgramProjectPlanList(String currentDate, Integer programId) {
        return programProjectMapper.selectProgramProjectPlanList(currentDate,programId);
    }

    /**
     * 获取项目群系统id
     * @param programId 项目群id
     * @return List<PeripheralSystemDTO>
     */
    @Override
    public List<PeripheralSystemDTO> selectProgramSystemId(Integer programId) {
        return programProjectMapper.selectProgramSystemId(programId);
    }

    /**
     * 项目群动态
     * @param projectId 项目ID列表
     * @return List<MessageInfoVO>
     */
    @Override
    public List<MessageInfoVO> selectProgramMessageInfoList(List<String> projectId) {
        return programProjectMapper.selectProgramMessageInfoList(projectId);
    }

   
    /**
     * 
    * @Title: Date
    * @Description: 记录项目周期情况
    * @author author
    * @param projectPeriod 项目计划
    * @return List<ProgramVO>
     */
    public static  List<ProgramVO> Date(List<ProjectPlanNewDTO> projectPeriod){
//        Map<Object,Object> mapValues = new HashMap<>();
        List<ProgramVO> programList = new ArrayList<>();
        if ( projectPeriod != null){
            try {
                Calendar from = Calendar.getInstance();
                Calendar to = Calendar.getInstance();
                for(int i =0 ;i<projectPeriod.size();i++){
//                    Map<Object,Object> map = new HashMap<>();
                    ProgramVO program = new ProgramVO();

                    List<SpringInfoVO> slots = new ArrayList<SpringInfoVO>();
                    Date start = null;
                    Date stop = null;
                    start = new SimpleDateFormat("yyyy-MM-dd").parse(projectPeriod.get(i).getPlanNewStartDate().toString());
                    stop = new SimpleDateFormat("yyyy-MM-dd").parse(projectPeriod.get(i).getPlanNewEndDate().toString());
                    from.setTime(start);
                    to.setTime(stop);
                    to.add(Calendar.DAY_OF_YEAR, 1);
                    while (from.getTime().getTime()!= to.getTime().getTime()) {
//                        ProjectPlanNewDTO projectPlanNewDTO = new ProjectPlanNewDTO();
//                        projectPlanNewDTO.setDate(new SimpleDateFormat("yyyy-MM-dd").format(from.getTime()));
//                        projectPlanNewDTO.setProjectId(projectPeriod.get(i).getProjectId());
//                        slots.add(projectPlanNewDTO);
                        SpringInfoVO milestones = new SpringInfoVO();
                        milestones.setDataTime(new SimpleDateFormat("yyyy-MM-dd").format(from.getTime()));
                        slots.add(milestones);
                        from.add(Calendar.DAY_OF_YEAR, 1);
                    }
                    to.add(Calendar.DAY_OF_YEAR, -1) ;
//                    map.put("projectId",projectPeriod.get(i).getProjectId());
                    program.setProjectId(projectPeriod.get(i).getProjectId());
//                    map.put("milestones",slots);
                    program.setMilestones(slots);
                    programList.add(i,program);
                }
                return programList;
            } catch (ParseException e) {
                e.printStackTrace();
                return programList;
            }
        }
        return programList;
    }

}
