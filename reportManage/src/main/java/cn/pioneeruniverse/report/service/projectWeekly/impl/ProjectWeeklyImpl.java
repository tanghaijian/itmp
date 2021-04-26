package cn.pioneeruniverse.report.service.projectWeekly.impl;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.report.dao.mybatis.report.projectWeeklyDao.ProjectWeeklyMapper;
import cn.pioneeruniverse.report.dto.DefectInfoDTO;
import cn.pioneeruniverse.report.dto.TaskDeliverDTO;
import cn.pioneeruniverse.report.entity.RequirementFeatureTimeTraceBean;
import cn.pioneeruniverse.report.entity.defectInfoBean;
import cn.pioneeruniverse.report.service.projectWeekly.IProjectWeeklyServer;
import cn.pioneeruniverse.report.vo.DefectInfoVO;
import cn.pioneeruniverse.report.vo.DefectResolvedVO;
import cn.pioneeruniverse.report.vo.RequirementFeatureTimeTraceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class ProjectWeeklyImpl implements IProjectWeeklyServer {

    @Autowired
    private ProjectWeeklyMapper projectWeeklyMapper;

    /**
     * 
    * @Title: getTaskDeliverList
    * @Description: 任务交付累计流图
    * @author author
    * @param taskDeliver
    * @return map key:timeTraceList 返回的数据
    * @throws
     */
    @Override
    public Map<String, Object> getTaskDeliverList(TaskDeliverDTO taskDeliver) {
        Map<String, Object> map = new HashMap<>();
        List<RequirementFeatureTimeTraceBean> timeTraceList = getList(taskDeliver);
        map.put("timeTraceList", timeTraceList);
        return map;
    }

    /**
     * 
    * @Title: getDefectInfoCountHandler
    * @Description: 根据查询条件获取选择的时间范围内的缺陷数目，并且按照缺陷状态和紧急程度每天统计
    *               总体逻辑：比如时间范围为：2020-01-01 到 2020-03-01
    *               在这段时间提出的并且（当前还没有关闭的缺陷或者当前状态已经关闭，但是在统计当天以后关闭的）。
    *               比如统计：2020-02-02这一天，那么统计的条件就是在2020-01-01到2020-03-01提出的缺陷，到目前为止依然没有关闭的缺陷。
    *               或者当前已经关闭了，但是关闭日期大于2020-02-02这一天
    * @author author
    * @param defectInfo 封装的查询条件
    * @return map key:defectInfoList 返回的每天的缺陷数量
    * @throws
     */
    @Override
    public Map<String, Object> getDefectInfoCountHandler(DefectInfoDTO defectInfo) {
        //查询出startTime 到 endTime 这一段时间内不是新建和撤销的缺陷
        List<DefectInfoVO> defectInfoVO = projectWeeklyMapper.getDefectInfoCountHandler(defectInfo);
        //获取选择的时间之间所有的日期，用于每天统计显示
        List<Date> betweenDate = getBetweenDate(defectInfo.getStartTime(), defectInfo.getEndTime());
        List<defectInfoBean> defectInfoList = new ArrayList<>();
        for (Date date : betweenDate){
            defectInfoBean defect = new defectInfoBean();
            int theNewNumber=0,seriousNumber=0,mildNumber=0;
            //缺陷状态为已关闭 &&  提出日期  <= 当前选择日期  && 关闭时间 > 当前选择日期
            for (DefectInfoVO info : defectInfoVO){
                if(defectInfoContrastDate(date,info.getSubmitDate())){
                    theNewNumber = theNewNumber+1;
                    continue;
                }else if (info.getDefectStatus() == 7 && info.getEmergencyLevel() != null && info.getSubmitDate() != null && info.getCloseTime() != null && info.getSubmitDate().getTime() <= date.getTime() && info.getCloseTime().getTime() > date.getTime()){
                    //紧急程度 1，2  seriousNumber，紧急程度 3,4,5 mildNumber
                    if (info.getEmergencyLevel() == 1 || info.getEmergencyLevel() == 2){
                        seriousNumber = seriousNumber +1;
                        continue;
                    }else if(info.getEmergencyLevel() == 3 || info.getEmergencyLevel() == 4 || info.getEmergencyLevel() == 5){
                        mildNumber = mildNumber +1;
                        continue;
                    }
                }else if(info.getDefectStatus() != 7 && info.getEmergencyLevel() != null && info.getSubmitDate() != null && date.getTime() > info.getSubmitDate().getTime()){
                    //紧急程度 1，2  seriousNumber，紧急程度 3,4,5 mildNumber
                    if (info.getEmergencyLevel() == 1 || info.getEmergencyLevel() == 2){
                        seriousNumber = seriousNumber +1;
                        continue;
                    }else if(info.getEmergencyLevel() == 3 || info.getEmergencyLevel() == 4 || info.getEmergencyLevel() == 5){
                        mildNumber = mildNumber +1;
                        continue;
                    }
                }
            }
            defect.setDate(DateUtil.getDateString(date, "yyyy-MM-dd"));
            defect.setTheNewNumber(theNewNumber);
            defect.setSeriousNumber(seriousNumber);
            defect.setMildNumber(mildNumber);
            defectInfoList.add(defect);
        }
        Collections.reverse(defectInfoList);
        Map<String,Object> mapValues = new HashMap<>(1);
        mapValues.put("defectInfoList",defectInfoList);
        return mapValues;
    }



    /**
     * 
    * @Title: getDefectResolvedHandler
    * @Description: 获取未解决的缺陷数，并按系统分类返回
    *               限制条件：缺陷状态不是新建、撤销、关闭的缺陷，其他根据查询条件限制
    * @author author
    * @param defectInfoDTO
    * @return
    * @throws
     */
    @Override
    public List<DefectResolvedVO> getDefectResolvedHandler(DefectInfoDTO defectInfoDTO) {
        List<DefectResolvedVO> systemInfoList = new ArrayList<>();
        if (defectInfoDTO.getSystemId().size() == 0 || defectInfoDTO.getSystemId() == null){
            return systemInfoList;
        }else{
        	//根据系统分类，获取每个系统的缺陷数
            List<DefectResolvedVO> defectResolvedList = projectWeeklyMapper.getDefectResolvedHandler(defectInfoDTO);
            systemInfoList = projectWeeklyMapper.getSystemInfoList(defectInfoDTO.getSystemId());
            //数据处理，剔除掉不在查询条件中的系统缺陷
            for (DefectResolvedVO defect : defectResolvedList){
                for (DefectResolvedVO Info : systemInfoList){
                    if (defect.getSystemId().equals(Info.getSystemId())){
                        Info.setCount(defect.getCount());
                    }
                }
            }
            //缺陷列表数据，获取根据查询条件筛选出的非新建、非关闭、非撤销的缺陷
            List<DefectResolvedVO> defectResolvedVO = projectWeeklyMapper.selectDefectInfoList(defectInfoDTO);
            String systemDate = DateUtil.formatDate(new java.sql.Date(System.currentTimeMillis()), "yyyy-MM-dd");
            for (DefectResolvedVO resolved : defectResolvedVO){
                for (DefectResolvedVO Info : systemInfoList){
                    if (resolved.getSystemId().equals(Info.getSystemId())){
                        //缺陷待解决统计图数据比较
                        compareDefectResolved(compareDate(resolved.getCreateDate().toString(), systemDate),Info);
                    }
                }
            }
            return systemInfoList;
        }
    }



    /**
     * 缺陷待解决统计图数据比较
     * @param number 提出日期距离当天日期天数
     * @param defect
     */
    private void compareDefectResolved(Integer number, DefectResolvedVO defect){
    	    //21天以上5级
            if (number > 20){
                defect.setFiveClass(defect.getFiveClass()+1);
            }else if(number >= 11 && number <= 20){//11-20天4级
                defect.setFourClass(defect.getFourClass()+1);
            }else if (number >= 6 && number <= 10){//6-10天3级
                defect.setThreeClass(defect.getThreeClass()+1);
            }else if (number >= 3 && number <= 5){//3-5天2级
                defect.setTwoClass(defect.getTwoClass()+1);
            }else if (number >= 1 && number <= 2){//1-2天1级
                defect.setOneClass(defect.getOneClass()+1);
            }
    }


    /**
     * 
    * @Title: getList
    * @Description: 任务交付累计流图数据处理，按选择的时间区间，每天显示
    * @author author
    * @param taskDeliver 封装的查询条件
    * @return
    * @throws
     */
    public List<RequirementFeatureTimeTraceBean> getList(TaskDeliverDTO taskDeliver){
        List<RequirementFeatureTimeTraceBean> list = new ArrayList<>();
        List<RequirementFeatureTimeTraceVO> timeTraceList = projectWeeklyMapper.selectBySystemId1(taskDeliver);
        List<Date> dateList= getBetweenDate(taskDeliver.getStartTime(),taskDeliver.getEndTime());
        for(Date date:dateList ) {
            RequirementFeatureTimeTraceBean trace1 = new RequirementFeatureTimeTraceBean();
            //已上线，待上线，验证中，待验证，实现完成，实现中，就绪
            int onLine=0,stayOnline=0,inVerification=0,waitValid=0,achieve=0,process=0,ready=0;
            for(RequirementFeatureTimeTraceVO trace:timeTraceList ) {
            	//统计时间大于上线时间，则表示已上线
                    if(contrastDate(date,trace.getRequirementFeatureProdCompleteTime())){
                        onLine=onLine+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureTestCompleteTime())) {//统计的时间大于测试完成时间，待上线
                        stayOnline=stayOnline+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureTestingTime())) {//统计的时间大于首次实施中的时间，验证中
                        inVerification=inVerification+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureFirstTestDeployTime())) {//统计的时间大于首次上测试环境的时间，待验证
                        waitValid=waitValid+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureDevCompleteTime())) {//统计的时间大于状态变成实施完成的时间，实现完成
                        achieve=achieve+1; continue;
                    }else if(contrastDate(date,trace.getCodeFirstCommitTime())) {//统计时间大于第一次提交代码的时间，视线中
                        process=process+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureCreateTime())) {//统计时间大于开发任务创建的时间，就绪。
                        ready=ready+1; continue;
                    }


            }

           // 7已上线，6待上线，5验证中，4待验证，3实现完成，2实现中，1就绪
            //deliverStatus 存储的数组初始为[0,0,0,0,0,0,0],下标0-6表示下拉框中的状态，对应的0，1表示对应的状态选择情况：0未选择，1已选择 ，如deliverStatus[0]表示就绪，deliverStatus[0]=1表示选择了就绪，deliverStatus[0]=0表示没选择就绪
            if (taskDeliver.getDeliveryStatus().size() == 0){
                trace1.setOnLine(onLine);
                trace1.setStayOnline(stayOnline);
                trace1.setInVerification(inVerification);
                trace1.setWaitValid(waitValid);
                trace1.setAchieve(achieve);
                trace1.setProcess(process);
                trace1.setReady(ready);
            }else{
            	//如果查询条件有选择已上线，那么已上线就是显示，否则显示0
                trace1.setOnLine(taskDeliver.getDeliveryStatus().get(6) == 1 ? onLine : 0);
                trace1.setStayOnline(taskDeliver.getDeliveryStatus().get(5) == 1 ? stayOnline : 0);
                trace1.setInVerification(taskDeliver.getDeliveryStatus().get(4) == 1 ? inVerification : 0);
                trace1.setWaitValid(taskDeliver.getDeliveryStatus().get(3) == 1 ? waitValid : 0);
                trace1.setAchieve(taskDeliver.getDeliveryStatus().get(2) == 1 ? achieve : 0);
                trace1.setProcess(taskDeliver.getDeliveryStatus().get(1) == 1 ? process : 0);
                trace1.setReady(taskDeliver.getDeliveryStatus().get(0) == 1 ? ready : 0);
                }
            trace1.setCliskDate(date);
            list.add(trace1);
        }
        Collections.reverse(list);
        return list;
    }


    public static Double getTaskDays(Date startDate,Date endDate){
        //每小时毫秒数
        long nh = 1000 * 60 * 60;
        double taskDay=0.0;
        if(startDate!=null&&endDate!=null) {
            Long diff =endDate.getTime()-startDate.getTime();
            double hour = new BigDecimal((float)diff/nh).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            taskDay=new BigDecimal((float)hour/24).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return taskDay;
    }

    public static boolean contrastDate(Date date1, Date date2) {
        if(date2==null){
            return false;
        }else{
            if(date1.getTime()>=date2.getTime()){
                return true;
            }else {
                return false;
            }
        }
    }

    public static List<Date> getBetweenDate(String startTime, String endTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 声明保存日期集合
        List<Date> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime()<=endDate.getTime()){
                // 把日期添加到集合
                list.add(endDate);
                // 设置日期
                calendar.setTime(endDate);
                //把日期增加一天
                calendar.add(Calendar.DATE, -1);
                // 获取增加后的日期
                endDate=calendar.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 缺陷统计图日期比较
     * @param date1
     * @param date2
     * @return
     */
    public static boolean defectInfoContrastDate(Date date1, Date date2) {
        if(date2 == null){
            return false;
        }else{
            if(date1.getTime() == date2.getTime()){
                return true;
            }else {
                return false;
            }
        }
    }

    public static List<Date> dateToWeek(Date mdate) {
        int b = mdate.getDay();
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        Long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a-1, fdate);
        }
        return list;
    }

    /**
     * 日期比较相差天数
     * @param createDate   比较的日期
     * @param systemDate  当前系统日期
     * @return
     * @throws Exception
     */
    private static Integer compareDate(String createDate, String systemDate){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fDate = sdf.parse(createDate);
            Date oDate = sdf.parse(systemDate);
            return  Integer.parseInt((oDate.getTime()-fDate.getTime())/(1000*3600*24)+"");
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


}
