package cn.pioneeruniverse.project.service.programProjectHome;

import cn.pioneeruniverse.project.dto.*;
import cn.pioneeruniverse.project.vo.MessageInfoVO;
import cn.pioneeruniverse.project.vo.ProgramInfoHomeVO;
import cn.pioneeruniverse.project.vo.ProgramProjectPlanVO;
import cn.pioneeruniverse.project.vo.ProgramProjectVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProgramProjectHomeService {

    /**
     *  项目群基本信息
     * @param programId
     * @return
     */
    ProgramInfoHomeVO selectProgramInfoHomeByID(Integer programId);

    /**
     *  项目群子项目
     * @param programId
     * @return
     */
    List<ProgramProjectVO> selectProgramProjectList(Integer programId);

    /**
     * 项目群风险信息总数
     * @param programId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectProgramRiskInfoCount(Integer programId);

    /**
     * 项目群风险本周新增
     * @param programId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectProgramWeedAdd(Integer programId);

    /**
     *  项目群风险未解决数
     * @param programId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectProgramOutStandingNumber(Integer programId);

    /**
     * 项目群变更总数
     * @param programId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectProgramProjectChangeInfoCount(Integer programId);

    /**
     * 项目群变更本周新增
     * @param programId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectProgramProjectChangeWeedAdd(Integer programId);

    /**
     *  项目群变更未解决数
     * @param programId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectProgramConfirmationNumber(Integer programId);


    /**
     * 项目群里程碑
     * @param programId
     * @return
     */
    List<ProjectPlanDTO> selectProgramProjectPlan(Integer programId);


    /**
     *  项目群冲刺表项目周期
     * @param currentDate
     * @param programId
     * @return
     */
    List<ProjectPlanNewDTO> selectProgramProjectPeriod(@Param("currentDate") String currentDate, @Param("programId") Integer programId);

    /**
     * 项目群燃尽图
     * @param currentDate
     * @param programId
     * @return
     */
    List<ProjectPlanNewDTO> selectProgramProjectPlanNewList(@Param("currentDate") String currentDate,@Param("programId") Integer programId,@Param("projectPlan")  List<ProjectPlanNewDTO> projectPlan);

    /**
     * 项目群公告
     * @param currentDate
     * @param programId
     * @return
     */
    List<ProjectNoticeDTO> selectProgramProjectNotice(@Param("currentDate") String currentDate, @Param("programId") Integer programId);

    /**
     *  获取项目计划表信息
     * @param programId
     * @return
     */
    List<ProgramProjectPlanVO> selectProgramProjectPlanList(@Param("currentDate") String currentDate,@Param("programId") Integer programId);

    /**
     * 获取项目群系统id
     * @param programId
     * @return
     */
    List<PeripheralSystemDTO> selectProgramSystemId(Integer programId);

    /**
     * 项目群动态
     * @param projectId
     * @return
     */
    List<MessageInfoVO> selectProgramMessageInfoList(@Param("projectId") List<String> projectId);

}
