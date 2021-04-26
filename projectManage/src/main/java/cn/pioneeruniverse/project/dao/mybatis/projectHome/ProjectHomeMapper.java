package cn.pioneeruniverse.project.dao.mybatis.projectHome;

import cn.pioneeruniverse.project.dto.*;
import cn.pioneeruniverse.project.vo.MessageInfoVO;
import cn.pioneeruniverse.project.vo.TaskForceDataDicVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProjectHomeMapper {
    /**
     *  根据项目id获取项目基本信息
     * @param id
     * @return
     */
    ProjectHomeDTO selectSystemInfoByID(Integer id);

    /**
     *  根据系统名获取周边系统
     * @param projectId
     * @return
     */
    List<PeripheralSystemDTO> selectSystemInfoByNameList(@Param("projectId") Integer projectId);

    /**
     *  根据项目id获取项目成员
     * @param projectId
     * @return
     */
    List<PeripheralSystemDTO> selectSystemInfoUserNameByIdList(Integer projectId);

    /**
     * 任务实施情况(瀑布模型)
     * @param projectId
     * @return
     */
    List<TaskForceDTO> selectRequirementFeatureCount(Integer projectId);

    /**
     * 获取开发任务状态字典数据
     * @return
     */
    List<TaskForceDataDicVO> selectDataDicList();


    /**
     * 任务实施情况(敏捷模型)
     * @param currentDate
     * @return
     */
    List<TaskForceDTO> selectRequirementFeatureAgilityCount(@Param("currentDate") String currentDate,@Param("projectId") Integer projectId);

    /**
     *  下周冲刺任务
     * @param currentDate
     * @param projectId
     * @return
     */
    List<TaskForceDTO> springNextWork(@Param("currentDate") String currentDate,@Param("projectId") Integer projectId);

    /**
     * 风险信息总数
     * @param projectId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectRiskInfoCount(Integer projectId);

    /**
     * 本周新增（风险）
     * @param projectId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectWeedAdd(Integer projectId);

    /**
     *  未解决数（风险）
     * @param projectId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectOutStandingNumber(Integer projectId);


    /**
     * 项目变更总数
     * @param projectId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectProjectChangeInfoCount(Integer projectId);

    /**
     * 本周新增（变更）
     * @param projectId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectProjectChangeWeedAdd(Integer projectId);

    /**
     *  未解决数（变更）
     * @param projectId
     * @return
     */
    RiskInfoAndChangeInfoDTO selectConfirmationNumber(Integer projectId);

    /**
     *  项目动态
     * @param projectId
     * @return
     */
    List<ProjectNoticeDTO> selectProjectNoticeList(@Param("currentDate") String currentDate,@Param("projectId") Integer projectId);

    /**
     *  项目计划（里程碑开始）
     * @param projectId
     * @return
     */
    List<ProjectPlanDTO> selectProjectPlanById(Integer projectId);

    /**
     * 项目计划（里程碑结束）
     * @param projectId
     * @return
     */
    List<ProjectPlanDTO> selectProjectPlanEndById(Integer projectId);

    /**
     *  获取冲刺表项目周期
     * @param currentDate
     * @param projectId
     * @return
     */
    List<ProjectPlanNewDTO> selectProjectPeriod(@Param("currentDate") String currentDate,@Param("projectId") Integer projectId);


    /**
     * 燃尽图
     * @param currentDate
     * @param projectId
     * @return
     */
    List<ProjectPlanNewDTO> selectProjectPlanNewList(@Param("currentDate") String currentDate,@Param("projectId") Integer projectId,@Param("projectPeriod") ProjectPlanNewDTO projectPeriod);

   //燃尽图历史数据
    List<ProjectPlanNewDTO> selectFeatureHistoryList(@Param("currentDate") String currentDate,@Param("projectId") Integer projectId,@Param("sprintId") List<Long> sprintId);



    /**
     *  项目主页信息
     * @param url
     * @return
     */
    ProjectHomeIndexDTO selectMenuButtonInfo(String url);

    /**
     *  查询项目关联系统
     * @param projectId
     * @return
     */
    List<ProjectHomeDTO> projectSystemList(@Param("projectId") Integer projectId);

    /**
     * 项目动态
     * @param projectId
     * @return
     */
    List<MessageInfoVO> selectMessageInfoList(@Param("projectId")Integer projectId);

    /**
     *  查询菜单信息
     * @param menuName
     * @return
     */
    List<ProjectHomeIndexDTO> selectMenuButtonInfoById(@Param("menuName")String menuName);


    Double selectRemainWorkLoad(Map<String,Object> param);
}
