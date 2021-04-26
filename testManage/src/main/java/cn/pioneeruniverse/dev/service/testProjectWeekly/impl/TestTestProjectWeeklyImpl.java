package cn.pioneeruniverse.dev.service.testProjectWeekly.impl;

import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.projectWeeklyDao.TestProjectWeeklyMapper;
import cn.pioneeruniverse.dev.dto.TestDefectInfoDTO;
import cn.pioneeruniverse.dev.dto.TestTaskDeliverDTO;
import cn.pioneeruniverse.dev.entity.TestDefectInfoBean;
import cn.pioneeruniverse.dev.entity.TestRequirementFeatureTimeTraceBean;
import cn.pioneeruniverse.dev.service.testProjectWeekly.ITestProjectWeeklyServer;
import cn.pioneeruniverse.dev.vo.TestDefectResolvedVO;
import cn.pioneeruniverse.dev.vo.TestRequirementFeatureTimeTraceVO;
import cn.pioneeruniverse.dev.vo.TestDefectInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class TestTestProjectWeeklyImpl implements ITestProjectWeeklyServer {

    @Autowired
    private TestProjectWeeklyMapper projectWeeklyMapper;

    @Override
    public Map<String, Object> getTaskDeliverList(TestTaskDeliverDTO taskDeliver) {
        Map<String, Object> map = new HashMap<>();
        List<TestRequirementFeatureTimeTraceBean> timeTraceList = getList(taskDeliver);
        map.put("timeTraceList", timeTraceList);
        return map;
    }

    @Override
    public Map<String, Object> getDefectInfoCountHandler(TestDefectInfoDTO defectInfo) {
        //缺陷数据
        List<TestDefectInfoVO> defectInfoVO = projectWeeklyMapper.getDefectInfoCountHandler(defectInfo);
        //获取时间段周期
        List<Date> betweenDate = getBetweenDate(defectInfo.getStartTime(), defectInfo.getEndTime());
        List<TestDefectInfoBean> defectInfoList = new ArrayList<>();
        for (Date date : betweenDate){
           TestDefectInfoBean defect = new TestDefectInfoBean();
            int theNewNumber=0,seriousNumber=0,mildNumber=0;
            //缺陷状态为已关闭 &&  提出日期  <= 当前选择日期  && 关闭时间 > 当前选择日期
            for (TestDefectInfoVO info : defectInfoVO){
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
        Map<String,Object> mapValues = new HashMap<>(7);
        mapValues.put("defectInfoList",defectInfoList);
        mapValues.put("defectInfoVO",defectInfoVO);
        return mapValues;
    }




    @Override
    public List<TestDefectResolvedVO> getDefectResolvedHandler(TestDefectInfoDTO defectInfoDTO) {
        List<TestDefectResolvedVO> systemInfoList = new ArrayList<>();
        if (defectInfoDTO.getSystemId().size() == 0 || defectInfoDTO.getSystemId() == null){
            return systemInfoList;
        }else{
            List<TestDefectResolvedVO> defectResolvedList = projectWeeklyMapper.getDefectResolvedHandler(defectInfoDTO);
            systemInfoList = projectWeeklyMapper.getSystemInfoList(defectInfoDTO.getSystemId());
            //数据处理
            for (TestDefectResolvedVO defect : defectResolvedList){
                for (TestDefectResolvedVO Info : systemInfoList){
                    if (defect.getSystemId().equals(Info.getSystemId())){
                        Info.setCount(defect.getCount());
                    }
                }
            }
            //缺陷列表数据
            List<TestDefectResolvedVO> defectResolvedVO = projectWeeklyMapper.selectDefectInfoList(defectInfoDTO);
            String systemDate = DateUtil.formatDate(new java.sql.Date(System.currentTimeMillis()), "yyyy-MM-dd");
            for (TestDefectResolvedVO resolved : defectResolvedVO){
                for (TestDefectResolvedVO Info : systemInfoList){
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
     * @param number
     * @param defect
     */
    private void compareDefectResolved(Integer number, TestDefectResolvedVO defect){
            if (number > 20){
                defect.setFiveClass(defect.getFiveClass()+1);
            }else if(number >= 11 && number <= 20){
                defect.setFourClass(defect.getFourClass()+1);
            }else if (number >= 6 && number <= 10){
                defect.setThreeClass(defect.getThreeClass()+1);
            }else if (number >= 3 && number <= 5){
                defect.setTwoClass(defect.getTwoClass()+1);
            }else if (number >= 1 && number <= 2){
                defect.setOneClass(defect.getOneClass()+1);
            }
    }


    /**
     * 任务交付累计流图数据处理
     * @param taskDeliver
     * @return
     */
    public List<TestRequirementFeatureTimeTraceBean> getList(TestTaskDeliverDTO taskDeliver){
        List<TestRequirementFeatureTimeTraceBean> list = new ArrayList<>();
        List<TestRequirementFeatureTimeTraceVO> timeTraceList = projectWeeklyMapper.selectBySystemId1(taskDeliver);
        List<Date> dateList= getBetweenDate(taskDeliver.getStartTime(),taskDeliver.getEndTime());
        for(Date date:dateList ) {
            TestRequirementFeatureTimeTraceBean trace1 = new TestRequirementFeatureTimeTraceBean();
            int onLine=0,stayOnline=0,inVerification=0,waitValid=0,achieve=0,process=0,ready=0;
            for(TestRequirementFeatureTimeTraceVO trace:timeTraceList ) {
                    if(contrastDate(date,trace.getRequirementFeatureProdCompleteTime())){
                        onLine=onLine+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureTestCompleteTime())) {
                        stayOnline=stayOnline+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureTestingTime())) {
                        inVerification=inVerification+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureFirstTestDeployTime())) {
                        waitValid=waitValid+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureDevCompleteTime())) {
                        achieve=achieve+1; continue;
                    }else if(contrastDate(date,trace.getCodeFirstCommitTime())) {
                        process=process+1; continue;
                    }else if(contrastDate(date,trace.getRequirementFeatureCreateTime())) {
                        ready=ready+1; continue;
                    }


            }

            if (taskDeliver.getDeliveryStatus().size() == 0){
                trace1.setOnLine(onLine);
                trace1.setStayOnline(stayOnline);
                trace1.setInVerification(inVerification);
                trace1.setWaitValid(waitValid);
                trace1.setAchieve(achieve);
                trace1.setProcess(process);
                trace1.setReady(ready);
            }else{
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
