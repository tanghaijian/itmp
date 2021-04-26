package cn.pioneeruniverse.dev.yiranUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.dto.TaskFeatuDTO;
import cn.pioneeruniverse.dev.entity.TblCustomFieldTemplate;

public class DefectExport {

    /**
     * 定义表头
     * @param tblCustomFieldTemplate
     * @return
     */
    public static  String[] ArrayString(TblCustomFieldTemplate tblCustomFieldTemplate){
        String[] titleValues = null;
        /*int i =0;
        if (tblCustomFieldTemplate != null){
            JSONObject jsonObject = JSON.parseObject(tblCustomFieldTemplate.getCustomField());
            Object field = jsonObject.get("field");
            JSONArray objects = JSON.parseArray(field.toString());
            if (objects.size()>0){
                for (int j=0;j<objects.size();j++){
                    if (objects.getJSONObject(j).get("status").equals("1")){
                        i++;
                    }
                }
            }
        }*/
//        titleValues =new String[30+i];
        titleValues =new String[16];
        titleValues[0] ="缺陷编号";
        titleValues[1] ="缺陷摘要";
        titleValues[2] ="缺陷状态";
        titleValues[3] ="缺陷来源";
        titleValues[4] ="缺陷等级";
        titleValues[5] ="紧急程度";
        titleValues[6] ="缺陷类型";
        titleValues[7] ="修复轮次";
        titleValues[8] ="投产窗口";
        titleValues[9] ="主修复人";
        titleValues[10] ="测试人";
        titleValues[11] ="提出人";
        titleValues[12] ="开发人员";
        titleValues[13] ="提出日期";
        titleValues[14] ="涉及系统";
        titleValues[15] ="所属需求";
        return titleValues;
    }

    /**
     * 特殊字符处理
     * @param str
     * @return
     */
    public static String conversion(String str){
        try{
            if(str != null){
                str = str.replace("&lt;p&gt;","");
                str = str.replace("&lt;/p&gt;","");
                str = str.replace("&lt;br&gt;","");
                str = str.replace("&amp;nbsp;&lt;/p&gt;&lt;p&gt;","");
                str = str.replace("<p>","");
                str = str.replace("<br>","");
                str = str.replace("</p>","");
                return  str;
            }else{
                return  "";
            }
        }catch (Exception e){
            return  "";
        }
    }

    /**
     *  日期转换
     * @param date
     * @param format1
     * @return
     */
    public static String getDateString(Date date, String format1) {
        try {
            if(date==null){
                return "";
            }
            return new SimpleDateFormat(format1).format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static Map<String, Object> taskFeatuDate(List<TaskFeatuDTO> taskFeatuDTOList, Map<String, Object> oneTestTask){
        Map<String, Object> stringObjectMap = null;
        if (taskFeatuDTOList != null && oneTestTask != null){
            long tActualStartDate = 0;  //系测初始化开始时间
            long tActualEndDate = 0;    //系测初始化结束时间
            long versionStartDate = 0;  //版测初始化开始时间
            long versionEndDate = 0;    //版测初始化结束时间
            for (int i =0; i<taskFeatuDTOList.size();i++){
                if (taskFeatuDTOList.get(i).getTestStag() == 1){   // 1 系测 2 版测
                    stringObjectMap = compareDate(tActualStartDate,tActualEndDate,oneTestTask, taskFeatuDTOList.get(i).getActualStartDate(), taskFeatuDTOList.get(i).getActualEndDate());
                    tActualStartDate = taskFeatuDTOList.get(i).getActualStartDate() == null ? 0 : Long.parseLong(DefectExport.getDateString(taskFeatuDTOList.get(i).getActualStartDate(), "yyyyMMdd"));
                    tActualEndDate = taskFeatuDTOList.get(i).getActualEndDate() == null ? 0 : Long.parseLong(DefectExport.getDateString(taskFeatuDTOList.get(i).getActualEndDate(), "yyyyMMdd"));
                }
                if (taskFeatuDTOList.get(i).getTestStag() == 2){
                    stringObjectMap = compareVersionDate(versionStartDate,versionEndDate,oneTestTask, taskFeatuDTOList.get(i).getActualStartDate(), taskFeatuDTOList.get(i).getActualEndDate());
                    versionStartDate = taskFeatuDTOList.get(i).getActualStartDate() == null ? 0 : Long.parseLong(DefectExport.getDateString(taskFeatuDTOList.get(i).getActualStartDate(), "yyyyMMdd"));
                    versionEndDate = taskFeatuDTOList.get(i).getActualEndDate() == null ? 0 : Long.parseLong(DefectExport.getDateString(taskFeatuDTOList.get(i).getActualEndDate(), "yyyyMMdd"));

                }
            }
            return  stringObjectMap;
        }
        return  oneTestTask;
    }

    /**
     * 系测数据比较
     * @param tActualStartDate
     * @param tActualEndDate
     * @param oneTestTask
     * @param ActualStartDate
     * @param ActualEndDate
     * @return
     */
    public static Map<String, Object> compareDate(long tActualStartDate,long tActualEndDate,Map<String, Object> oneTestTask,Date ActualStartDate,Date ActualEndDate){
        if (ActualEndDate != null && ActualStartDate != null){
            long ActualStart =  Long.parseLong(DefectExport.getDateString(ActualStartDate, "yyyyMMdd"));  //开始时间
            long ActualEnd = Long.parseLong(DefectExport.getDateString(ActualEndDate, "yyyyMMdd"));     //结束时间
            if(tActualStartDate == 0){
                oneTestTask.put("actualSitStartDate",DefectExport.getDateString(ActualStartDate,"yyyy-MM-dd"));
            }
            if (tActualEndDate == 0){
                oneTestTask.put("actualSitEndDate",DefectExport.getDateString(ActualEndDate,"yyyy-MM-dd"));
            }
            if (tActualStartDate > ActualStart){
                oneTestTask.put("actualSitStartDate",DefectExport.getDateString(ActualStartDate,"yyyy-MM-dd"));
            }
            if(ActualEnd > tActualEndDate){
                oneTestTask.put("actualSitEndDate",DefectExport.getDateString(ActualEndDate,"yyyy-MM-dd"));
            }
            return  oneTestTask;
        }

        if (ActualEndDate == null && ActualStartDate == null){
            oneTestTask.put("actualSitStartDate","");
            oneTestTask.put("actualSitEndDate","");
        }

        return  oneTestTask;
    }


    /**
     * 版测数据比较
     * @param tActualStartDate
     * @param tActualEndDate
     * @param oneTestTask
     * @param ActualStartDate
     * @param ActualEndDate
     * @return
     */
    public static Map<String, Object> compareVersionDate(long tActualStartDate,long tActualEndDate,Map<String, Object> oneTestTask,Date ActualStartDate,Date ActualEndDate){
        if (ActualEndDate != null && ActualStartDate != null){
            long ActualStart =  Long.parseLong(DefectExport.getDateString(ActualStartDate, "yyyyMMdd"));  //开始时间
            long ActualEnd = Long.parseLong(DefectExport.getDateString(ActualEndDate, "yyyyMMdd"));     //结束时间
            if(tActualStartDate == 0){
                oneTestTask.put("actualPptStartDate",DefectExport.getDateString(ActualStartDate,"yyyy-MM-dd"));
            }
            if (tActualEndDate == 0){
                oneTestTask.put("actualPptEndDate",DefectExport.getDateString(ActualEndDate,"yyyy-MM-dd"));
            }
            if (tActualStartDate > ActualStart){
                oneTestTask.put("actualPptStartDate",DefectExport.getDateString(ActualStartDate,"yyyy-MM-dd"));
            }
            if(ActualEnd > tActualEndDate){
                oneTestTask.put("actualPptEndDate",DefectExport.getDateString(ActualEndDate,"yyyy-MM-dd"));
            }
            return  oneTestTask;
        }

        if (ActualEndDate == null && ActualStartDate == null){
            oneTestTask.put("actualPptStartDate","");
            oneTestTask.put("actualPptEndDate","");
        }

        return  oneTestTask;
    }

}
