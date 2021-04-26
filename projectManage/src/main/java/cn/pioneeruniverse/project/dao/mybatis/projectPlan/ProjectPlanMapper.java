package cn.pioneeruniverse.project.dao.mybatis.projectPlan;

import cn.pioneeruniverse.project.entity.TblProjectPlanInfo;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.vo.JqueryGanttVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProjectPlanMapper {
    //获取正式版本项目计划（主页面）
    List<JqueryGanttVO> getProjectPlan(Long projectId);
    //获取项目计划模板
    List<JqueryGanttVO> getProjectPlanTemplate();
    //获取项目计划状态（项目表）
    Integer getProjectPlanStatus(Long projectId);
    //修改项目计划状态（项目表）
    void updateProjectStatus(@Param("planStatus")Integer planStatus,
                             @Param("projectId") Long projectId);
    //新增正式版本项目计划
    void insertProjectPlan(TblProjectPlanInfo projectPlan);
    //删除正式版本项目计划
    void deleteProjectPlanByProjectId(Long projectId);
    //获取正式版本项目计划
    List<TblProjectPlanInfo> getProjectPlanByProjectId(Long projectId);


    List<TblUserInfo> selectUserInfoVague(String userAndAccount);

    Map<String,Object> getProjectPlanById(Long id);

    List<TblUserInfo> selectUserInfoVagueInProject(Map<String,Object> param);
}
