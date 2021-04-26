package cn.pioneeruniverse.project.service.projectHome.impl;

import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.project.dao.mybatis.projectHome.ProjectHomeMapper;
import cn.pioneeruniverse.project.dto.*;
import cn.pioneeruniverse.project.service.projectHome.ProjectHomeService;
import cn.pioneeruniverse.project.vo.MessageInfoVO;
import cn.pioneeruniverse.project.vo.TaskForceDataDicVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class ProjectHomeImpl implements ProjectHomeService {


    @Autowired
    private ProjectHomeMapper projectHomeMapper;

    /**
     * 
    * @Title: selectSystemInfoByID
    * @Description: 根据项目ID获取项目信息
    * @author author
    * @param id 项目id
    * @return ProjectHomeDTO
     */
    @Override
    public ProjectHomeDTO selectSystemInfoByID(Integer id) {
        return projectHomeMapper.selectSystemInfoByID(id);
    }

    /**
     * 
    * @Title: selectSystemInfoByNameList
    * @Description: 通过项目ID获取周边系统名
    * @author author
    * @param projectId 项目id
    * @return List<PeripheralSystemDTO>
     */
    @Override
    public List<PeripheralSystemDTO> selectSystemInfoByNameList(@Param("projectId") Integer projectId) {
        return projectHomeMapper.selectSystemInfoByNameList(projectId);
    }

    /**
     * 
    * @Title: selectSystemInfoUserNameByIdList
    * @Description: 根据项目和用户获取未过期的公告和项目成员列表
    * @author author
    * @param projectId 项目id
    * @param uid 人员id
    * @return Map<String,Object>
     */
    @Override
    public Map<String,Object> selectSystemInfoUserNameByIdList(Integer projectId,Long uid) {
        Map<String,Object> mapValues = new HashMap<>();
        //项目成员列表
        List<PeripheralSystemDTO> systemUserNameList = projectHomeMapper.selectSystemInfoUserNameByIdList(projectId);
        //项目组用户列表
        mapValues.put("systemUserNameList",systemUserNameList);
        List<ProjectNoticeDTO> notice = null;
        String format = DateUtil.formatDate(new java.sql.Date(System.currentTimeMillis()), "yyyy-MM-dd");
        if (systemUserNameList != null && systemUserNameList.size() >0){
            for (int i =0 ; i<systemUserNameList.size(); i++){
            	//在该项目下的成员才获取项目公告
                if (uid == systemUserNameList.get(i).getId()){
                    //项目公告
                    notice = projectHomeMapper.selectProjectNoticeList(format,projectId);
                }
            }
        }else if (uid == 1){//管理员
            notice = projectHomeMapper.selectProjectNoticeList(format,projectId);
        }
        //项目公告
        mapValues.put("notice",notice);
        return mapValues;
    }

    /**
     * 任务实施情况(瀑布模型)
     * @param projectId 项目id
     * @return Map<String,Object>
     */
    @Override
    public Map selectRequirementFeatureCount(Integer projectId) {
        Map<String,Object> mapValues = new HashMap<>();
        Integer count = 0 ;
        //开发任务状态字典数据
        List<TaskForceDataDicVO> dataDic = projectHomeMapper.selectDataDicList();
        //任务实施情况
        List<TaskForceDTO> taskForce = projectHomeMapper.selectRequirementFeatureCount(projectId);
        if (!dataDic.isEmpty() && !taskForce.isEmpty()){
            this.TaskForceUtil(dataDic, taskForce);
            //实施任务数
            count = this.dadaDicCount(dataDic);
            mapValues.put("dadaDic",dataDic);
        }
        //实施任务数
        mapValues.put("dadaDicCount",count);
        return mapValues;
    }

    /**
     * 任务实施情况(敏捷模型)
     * @param currentDate 当天
     * @return Map<String,Object>
     */
    @Override
    public Map selectRequirementFeatureAgilityCount(@Param("currentDate") String currentDate,@Param("projectId") Integer projectId) {
        Map<String,Object> mapValues = new HashMap<>();
        Integer count = 0 ;
        //开发任务状态字典数据
        List<TaskForceDataDicVO> dataDicFeatureAgility = projectHomeMapper.selectDataDicList();
        //任务实施情况
        List<TaskForceDTO> taskForceFeatureAgility = projectHomeMapper.selectRequirementFeatureAgilityCount(currentDate, projectId);
        if (!dataDicFeatureAgility.isEmpty() && !taskForceFeatureAgility.isEmpty()){
            this.TaskForceUtil(dataDicFeatureAgility, taskForceFeatureAgility);
            count = this.dadaDicCount(dataDicFeatureAgility);
            mapValues.put("dadaDic",dataDicFeatureAgility);

            mapValues.put("code",1);
        }
        //实施任务数
        mapValues.put("dadaDicCount",count);
        return mapValues;
    }

    /**
     *  下周冲刺任务
     * @param currentDate 当天
     * @param systemId 系统id
     * @return Map<String,Object>
     */
    @Override
    public Map springNextWork(@Param("currentDate") String currentDate,@Param("systemId") Integer systemId) {
        Map<String,Object> mapValues = new HashMap<>();
        Integer count = 0 ;
        //开发任务状态字典数据
        List<TaskForceDataDicVO> dataDic = projectHomeMapper.selectDataDicList();
        //任务实施情况
        List<TaskForceDTO> nextFeatureAgility = projectHomeMapper.springNextWork(currentDate, systemId);
        if (!dataDic.isEmpty() && !nextFeatureAgility.isEmpty()){
            this.TaskForceUtil(dataDic, nextFeatureAgility);
            count = this.dadaDicCount(dataDic);
            mapValues.put("dadaDic",dataDic);
            mapValues.put("code",3);
        }
        //任务实施数
        mapValues.put("dadaDicCount",count);
        return mapValues;
    }

    /**
     * 风险信息总数
     * @param projectId 项目id
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectRiskInfoCount(Integer projectId) {
        return projectHomeMapper.selectRiskInfoCount(projectId);
    }

    /**
     * 本周新增
     * @param projectId
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectWeedAdd(Integer projectId) {
        return projectHomeMapper.selectWeedAdd(projectId);
    }

    /**
     *  未解决数
     * @param projectId
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectOutStandingNumber(Integer projectId) {
        return projectHomeMapper.selectOutStandingNumber(projectId);
    }


    /**
     * 项目变更总数
     * @param projectId
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectProjectChangeInfoCount(Integer projectId) {
        return projectHomeMapper.selectProjectChangeInfoCount(projectId);
    }

    /**
     * 本周新增（变更）
     * @param projectId
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectProjectChangeWeedAdd(Integer projectId) {
        return projectHomeMapper.selectProjectChangeWeedAdd(projectId);
    }

    /**
     *  未解决数（变更）
     * @param projectId
     * @return RiskInfoAndChangeInfoDTO
     */
    @Override
    public RiskInfoAndChangeInfoDTO selectConfirmationNumber(Integer projectId) {
        return projectHomeMapper.selectConfirmationNumber(projectId);
    }

    /**
     *  项目动态
     * @param projectId
     * @return List<ProjectNoticeDTO>
     */
    @Override
    public List<ProjectNoticeDTO> selectProjectNoticeList( String currentDate,Integer projectId) {
        return projectHomeMapper.selectProjectNoticeList(currentDate,projectId);
    }


    /**
     *  项目计划（里程碑）
     * @param projectId
     * @return List<ProjectPlanDTO>
     */
    @Override
    public List<ProjectPlanDTO> selectProjectPlanById(Integer projectId) {
        List<ProjectPlanDTO> projectPlan = new ArrayList<>();
        List<ProjectPlanDTO> projectPlanBegin = projectHomeMapper.selectProjectPlanById(projectId);
        List<ProjectPlanDTO> projectPlanEnd = projectHomeMapper.selectProjectPlanEndById(projectId);
        //开始里程牌
        for (int i=0; i<projectPlanBegin.size();i++){
            ProjectPlanDTO projectPlanDTO = new ProjectPlanDTO();
            projectPlanDTO.setProjectId(projectPlanBegin.get(i).getProjectId());
            projectPlanDTO.setPlanName(projectPlanBegin.get(i).getPlanName());
            projectPlanDTO.setPlanStartDate(projectPlanBegin.get(i).getPlanStartDate());
            projectPlanDTO.setPlanEndDate(projectPlanBegin.get(i).getPlanEndDate());
            projectPlanDTO.setCurrentProgress(projectPlanBegin.get(i).getCurrentProgress());
            projectPlan.add(projectPlanDTO);
        }

        //结束里程碑
        for (int i =0 ; i<projectPlanEnd.size();i++){
            ProjectPlanDTO projectPlanEndDTO = new ProjectPlanDTO();
            projectPlanEndDTO.setProjectId(projectPlanEnd.get(i).getProjectId());
            projectPlanEndDTO.setPlanName(projectPlanEnd.get(i).getPlanName());
            projectPlanEndDTO.setPlanStartDate(projectPlanEnd.get(i).getPlanStartDate());
            projectPlanEndDTO.setPlanEndDate(projectPlanEnd.get(i).getPlanEndDate());
            projectPlanEndDTO.setCurrentProgress(projectPlanEnd.get(i).getCurrentProgress());
            projectPlan.add(projectPlanEndDTO);
        }
        //去重
        for  ( int  i  =   0 ; i  <  projectPlan.size()  -   1 ; i ++ )  {
            for  ( int  j  =  projectPlan.size()  -   1 ; j  >  i; j -- )  {
                if  (projectPlan.get(j).getPlanName().equals(projectPlan.get(i).getPlanName()))  {
                    projectPlan.remove(j);
                }
            }
        }
        return projectPlan;
    }

    /**
     *  获取冲刺表项目周期
     * @param currentDate 当天
     * @param projectId 项目id
     * @return ProjectPlanNewDTO
     */
    @Override
    public ProjectPlanNewDTO selectProjectPeriod(@Param("currentDate") String currentDate, @Param("projectId") Integer projectId ) {
        List<ProjectPlanNewDTO> projectPlan = projectHomeMapper.selectProjectPeriod(currentDate, projectId);
        ProjectPlanNewDTO projectPlanNew = new ProjectPlanNewDTO();
        if (projectPlan != null && projectPlan.size()>0){
            String beginTime = null;
            String endTime = null;
            List<Long> sprintId = new ArrayList<>();
            Long[] begin = new Long[projectPlan.size()];
            Long[] end = new Long[projectPlan.size()];
            for (int i =0;i<projectPlan.size();i++){
                try{
                    sprintId.add(projectPlan.get(i).getSprintId());
                    begin[i] = this.simpleDateFormatMethod(projectPlan.get(i).getPlanNewStartDate().toString());
                    end[i] = this.simpleDateFormatMethod(projectPlan.get(i).getPlanNewEndDate().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            projectPlanNew.setPlanNewStartDate(this.getMin(begin));
            projectPlanNew.setPlanNewEndDate(this.getMax(end));
            projectPlanNew.setSprintListId(sprintId);
        }
        return projectPlanNew;
    }

    /**
     *  日期转换
     * @return long
     */
    private static long simpleDateFormatMethod(String date) throws  Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long timeValue = format.parse(date).getTime();
        return timeValue;
    }


    /**
     * 燃尽图
     * @param currentDate 当前日期
     * @param projectId 项目id
     * @return  Map<String,Object>
     */
    @Override
    public Map selectProjectPlanNewList(@Param("currentDate") String currentDate, @Param("projectId") Integer projectId, @Param("projectPeriod") ProjectPlanNewDTO projectPeriod, @Param("sprintId") List<Long> sprintId) {
        Map<String,Object> mapValues = new HashMap<>();
        if(projectPeriod != null){
            List<ProjectPlanNewDTO> projectPlanNew = projectHomeMapper.selectProjectPlanNewList(currentDate, projectId,projectPeriod);
            if (projectPlanNew != null && projectPlanNew.size()>0){
            	//拆分时间周期，将计划开始日期到计划结束日期之间的每个日期进行分别统计
                List<ProjectPlanNewDTO> period = this.Date(projectPeriod);
                //获取预计工作量
                for (int i =0;i<projectPlanNew.size();i++){
                    for(int j=0;j<period.size();j++){
                        if (projectPlanNew.get(i).getCreateDate().toString().equals(period.get(j).getDate())){
                            period.get(j).setEstimateWorkload(projectPlanNew.get(i).getEstimateWorkload());
                        }
                    }
                }

               // 燃尽图历史数据
//                List<ProjectPlanNewDTO> featureHistory = projectHomeMapper.selectFeatureHistoryList(currentDate, projectId, sprintId);
//                if(featureHistory != null && featureHistory.size()>0){
//                    for (int i =0; i<featureHistory.size(); i++){ //获取实际工作量数据
//                        for (int j =0; j<period.size(); j++){
//                            if (period.get(j).getDate().equals(featureHistory.get(i).getCreateDate().toString())){
//                                period.get(j).setEstimateRemainWorkload(featureHistory.get(i).getEstimateRemainWorkload());
//                            }
//                        }
//                    }
//                }

        // 预估剩余实际工作量
        if (sprintId != null) {


          for (int j = 0; j < period.size(); j++) {
            Map<String, Object> param = new HashMap<>();
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
              try {
                  Date date = simpleDateFormat.parse(period.get(j).getDate());
                  param.put("createDate",date);
                  param.put("sprintId", sprintId);
                  Double remainWorkLoad = projectHomeMapper.selectRemainWorkLoad(param);

                  if (remainWorkLoad == null) {
                      period.get(j).setEstimateRemainWorkload(new Double(0));
                  } else {
                     period.get(j).setEstimateRemainWorkload(remainWorkLoad);
                  }
              } catch (ParseException e) {
                  e.printStackTrace();
              }

          }
        }

                double count = 0;
                if (projectPlanNew != null && projectPlanNew.size()>0){
                    for (int i = 0; i<projectPlanNew.size(); i++){
                        count = count +projectPlanNew.get(i).getEstimateWorkload(); //获取总工时
                    }
                }

                mapValues.put("projectPeriod",period);
                mapValues.put("period",period.size());
                mapValues.put("countWorkload",count);
                return mapValues;
            }
        }

        return mapValues;
    }





    /**
     *  项目主页信息
     * @param url
     * @return
     */
    @Override
    public ProjectHomeIndexDTO selectMenuButtonInfo(String url) {
        return projectHomeMapper.selectMenuButtonInfo(url);
    }

    /**
     *  查询项目关联系统
     * @param project
     * @return
     */
    @Override
    public List<ProjectHomeDTO> projectSystemList(Integer project) {
        return projectHomeMapper.projectSystemList(project);
    }

    /**
     * 项目动态
     * @param projectId
     * @return
     */
    @Override
    public List<MessageInfoVO> selectMessageInfoList(Integer projectId) {
        return projectHomeMapper.selectMessageInfoList(projectId);
    }


    /**
     *  查询菜单信息
     * @param menuName
     * @return
     */
    @Override
    public List<ProjectHomeIndexDTO> selectMenuButtonInfoById(String menuName) {
        return projectHomeMapper.selectMenuButtonInfoById(menuName);
    }


    /**
     * 获取冲刺项目周期
     * @return
     */
    public static List<ProjectPlanNewDTO> Date(ProjectPlanNewDTO projectPeriod){
        List<ProjectPlanNewDTO> slots = new ArrayList<ProjectPlanNewDTO>();
        if ( projectPeriod != null){
            try {
                Calendar from = Calendar.getInstance();
                Calendar to = Calendar.getInstance();
                Date start = null;
                Date stop = null;
                start = new SimpleDateFormat("yyyy-MM-dd").parse(projectPeriod.getPlanNewStartDate());
                stop = new SimpleDateFormat("yyyy-MM-dd").parse(projectPeriod.getPlanNewEndDate());
                from.setTime(start);
                to.setTime(stop);
                to.add(Calendar.DAY_OF_YEAR, 1);
                while (from.getTime().getTime()!= to.getTime().getTime()) {
                    ProjectPlanNewDTO projectPlanNewDTO = new ProjectPlanNewDTO();
                    projectPlanNewDTO.setDate(new SimpleDateFormat("yyyy-MM-dd").format(from.getTime()));
                    slots.add(projectPlanNewDTO);
                    from.add(Calendar.DAY_OF_YEAR, 1);
                }
                to.add(Calendar.DAY_OF_YEAR, -1);
                return slots;
            } catch (ParseException e) {
                e.printStackTrace();
                return slots;
            }
        }
       return slots;
    }

    /**
     *  获取数组最大值
     * @param arr
     * @return
     */
    private static String getMax(Long[] arr) {
        // 定义一个参照物
        Long max = arr[0];
        //遍历数组
        for (int i = 0; i < arr.length; i++) {
            //判断大小
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return DateUtil.formatDate(new Date(max), "yyyy-MM-dd");
    }

    /**
     *  获取数组最小值
     * @param arr
     * @return
     */
    private static String getMin(Long[] arr){
        // 定义一个参照物
        Long min = arr[0];
        //遍历数组
        for (int i = 0; i < arr.length; i++) {
            //判断大小
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return DateUtil.formatDate(new Date(min), "yyyy-MM-dd");
    }

    /**
     * 
    * @Title: TaskForceUtil
    * @Description: 根据数据字典的状态和开发任务状态匹配，分类计算总数
    * @author author
    * @param dataDic
    * @param taskForce
    * @return
    * @throws
     */
    private List<TaskForceDataDicVO> TaskForceUtil(List<TaskForceDataDicVO> dataDic, List<TaskForceDTO> taskForce){
        for (int i=0; i<dataDic.size(); i++){
            for (int j=0; j<taskForce.size(); j++){
                if (dataDic.get(i).getValueCode() == taskForce.get(j).getRequirementFeatureStatus()){
                    dataDic.get(i).setCount(taskForce.get(j).getCount());
                }
            }
        }
        return dataDic;
    }


    /**
     * 任务实施总数
     * @return
     */
    private Integer dadaDicCount(List<TaskForceDataDicVO> TaskForce){
        Integer count = 0;
        if (!TaskForce.isEmpty()){
            for (int i = 0; i<TaskForce.size();i++){
                if (TaskForce.get(i).getCount() != null){
                    count = count + TaskForce.get(i).getCount();
                }
            }
        }
        return count;
    }

}
