package cn.pioneeruniverse.dev.controller.defectReport;

import cn.pioneeruniverse.dev.dto.TestDefectInfoDTO;
import cn.pioneeruniverse.dev.entity.monthlyReport.*;
import javafx.util.Pair;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by aviyy on 2020/10/21.
 */
public class MonthlyController {


    public Map<String, Object> getDefectInfoCountHandler(@RequestBody TestDefectInfoDTO defectInfo){
        Map<String, Object> datas = new HashMap<>();
        Map<String, Object> module = new HashMap<>();
        /*
1.获取各类数据
2.获取模板数据
3.数据入map
4.初始化模板数据，即变量替换
         */
            //规划内版本数
//        data.put("plan_version_num",{"name":"规划内版本数","value":5});
            //临时版本数
//        data.put("limit_version_num",{"name":"临时版本数","value":6});

//        String modele_1_1_1 = "本月有{{plan_version_num}}次规划内版本,{{limit_version_num}}次临时版本；临时增加任务数4个,临时撤销任务数2个；版本变更率为2.05%，软件测试风险(注1)为低；";
        /*{
            content1:"本月有{{plan_version_num}}次规划内版本,{{limit_version_num}}次临时版本；临时增加任务数4个,临时撤销任务数2个；版本变更率为2.05%，软件测试风险(注1)为低；",
            content2:"本月有{{规划内版本数}}次规划内版本,{{临时版本数}}次临时版本；临时增加任务数4个,临时撤销任务数2个；版本变更率为2.05%，软件测试风险(注1)为低；",
            content3:"本月有5次规划内版本,6次临时版本；临时增加任务数4个,临时撤销任务数2个；版本变更率为2.05%，软件测试风险(注1)为低；"
        }



        type code



*/




        /*

        数据分类：
        1.基本数据类型
            base
        2.系统数据类型
            system
        3.敏捷类系统
            agile
        4.运维期系统
            operation
        5.项目期系统
            new
        6.缺陷等级
            defectLevel
{
    base:








}

         */

        List<Data> listOne = new ArrayList<>();

        List<Data> listBase = new ArrayList<>();
        List<Data> listSystem = new ArrayList<>();
        List<Data> listAgile = new ArrayList<>();
        List<Data> listOperation = new ArrayList<>();
        List<Data> listNew = new ArrayList<>();
        List<Data> listDefectLevel = new ArrayList<>();


        Data mapBase = new Data("listBase", "基本数据类型", null, listBase);
        listOne.add(mapBase);

        Data mapSystem = new Data("listSystem", "系统数据类型", null, listSystem);
        listOne.add(mapSystem);

        Data mapAgile = new Data("listAgile", "敏捷类系统", null, listAgile);
        listOne.add(mapAgile);

        Data mapOperation = new Data("listOperation", "运维期系统", null, listOperation);

        listOne.add(mapOperation);

        Data mapNew = new Data("listNew", "项目期系统", null, listNew);
        listOne.add(mapNew);

        Data mapDefectLevel = new Data("listDefectLevel", "缺陷等级", null, listDefectLevel);
        listOne.add(mapDefectLevel);


        listOne.add(mapBase);




        TblReportMonthlyBase base = new TblReportMonthlyBase();//基表，列表（检出率）

        Data planWindowsNumber = new Data("planWindowsNumber", "计划内版本次数", base.getPlanWindowsNumber());
        Data tempWindowsNumber = new Data("tempWindowsNumber", "临时版本次数", base.getTempWindowsNumber());
        Data allWindowsNumber = new Data("allWindowsNumber", "总版本数", base.getPlanWindowsNumber() + base.getTempWindowsNumber());
        Data tempAddTaskNumber = new Data("tempAddTaskNumber", "临时增加任务数", base.getTempAddTaskNumber());
        Data tempDelTaskNumber = new Data("tempDelTaskNumber", "临时删除任务数", base.getTempDelTaskNumber());
        Data changePercent = new Data("changePercent", "版本变更率", base.getChangePercent());
        Data risk = new Data("risk", "软件测试风险", base.getChangePercent() > 20 ? "高" : base.getChangePercent() > 10 ? "中" : "低");
        Data avgRepairRound = new Data("avgRepairRound", "平均缺陷修复轮次", base.getAvgRepairRound());
        Data undetectedNumber = new Data("undetectedNumber", "漏检数", base.getUndetectedNumber());
        Data detectedRate = new Data("detectedRate", "检出率", base.getDetectedRate());

        listSystem.add(planWindowsNumber);
        listSystem.add(tempWindowsNumber);
        listSystem.add(allWindowsNumber);
        listSystem.add(tempAddTaskNumber);
        listSystem.add(tempDelTaskNumber);
        listSystem.add(changePercent);
        listSystem.add(risk);
        listSystem.add(avgRepairRound);
        listSystem.add(undetectedNumber);
        listSystem.add(detectedRate);



        String[] yearMonth = base.getYearMonth().split("-");

        Data year = new Data("year", "年", yearMonth[0]);
        Data month = new Data("month", "月", yearMonth[1]);

        listBase.add(year);
        listBase.add(month);

        TblReportMonthlySystemTypeData typeData1 = new TblReportMonthlySystemTypeData();//敏捷

        Data systemNum1 = new Data("systemNum1", "敏捷类系统数", typeData1.getSystemNum());
        Data totalTaskNumber1 = new Data("totalTaskNumber1", "敏捷类系统测试任务总数", typeData1.getTotalTaskNumber());

        Data designCaseNumber1 = new Data("designCaseNumber1", "敏捷类系统设计用例数", typeData1.getDesignCaseNumber());
        Data defectNumber1 = new Data("defectNumber1", "敏捷类系统缺陷数", typeData1.getDefectNumber());
        Double defectPercentNum1 = typeData1.getDefectPercent();
        Data defectPercent1 = new Data("defectPercent1", "敏捷类系统缺陷率", defectPercentNum1);
        Data quality1 = new Data("quality1", "敏捷类系统开发质量", defectPercentNum1 > 4 ? "较低" : defectPercentNum1 > 2 ? "一般" : "较高");

        listAgile.add(systemNum1);
        listAgile.add(totalTaskNumber1);
        listAgile.add(designCaseNumber1);
        listAgile.add(defectNumber1);
        listAgile.add(defectPercent1);
        listAgile.add(quality1);

        TblReportMonthlySystemTypeData typeData2 = new TblReportMonthlySystemTypeData();//运维

        Data systemNum2 = new Data("systemNum2", "运维期系统数", typeData2.getSystemNum());
        Data designCaseNumber2 = new Data("designCaseNumber2", "运维期系统设计用例数", typeData2.getDesignCaseNumber());
        Data defectNumber2 = new Data("defectNumber2", "运维期系统缺陷数", typeData2.getDefectNumber());
        Double defectPercentNum2 = typeData2.getDefectPercent();
        Data defectPercent2 = new Data("defectPercent2", "运维期系统缺陷率", defectPercentNum2);
        Data quality2 = new Data("quality2", "运维期系统开发质量", defectPercentNum2 > 4 ? "较低" : defectPercentNum2 > 2 ? "一般" : "较高");


        listOperation.add(systemNum2);
        listOperation.add(designCaseNumber2);
        listOperation.add(defectNumber2);
        listOperation.add(defectPercent2);
        listOperation.add(quality2);


        TblReportMonthlySystemTypeData typeData3 = new TblReportMonthlySystemTypeData();//项目

        Data systemNum3 = new Data("systemNum3", "项目期系统数", typeData3.getSystemNum());
        Data designCaseNumber3 = new Data("designCaseNumber3", "项目期系统设计用例数", typeData3.getDesignCaseNumber());
        Data defectNumber3 = new Data("defectNumber3", "项目期系统缺陷数", typeData3.getDefectNumber());
        Double defectPercentNum3 = typeData3.getDefectPercent();
        Data defectPercent3 = new Data("defectPercent3", "项目期系统缺陷率", defectPercentNum3);
        Data quality3 = new Data("quality3", "项目期系统开发质量", defectPercentNum3 > 4 ? "较低" : defectPercentNum3 > 2 ? "一般" : "较高");


        listNew.add(systemNum3);
        listNew.add(designCaseNumber3);
        listNew.add(defectNumber3);
        listNew.add(defectPercent3);
        listNew.add(quality3);

        List<TblReportMonthlySystemData> systemDatas = new ArrayList<>();
        TblReportMonthlySystemData systemData = new TblReportMonthlySystemData();

        List<String> top = new ArrayList<>();
        systemDatas.add(systemData);
        for(TblReportMonthlySystemData d : systemDatas){
            if(d.getAvgRepairRound() > 1){
                if(d.getSystemType() == 1){
                    top.add(d.getSystemName() + "(敏捷)");
                } else {
                    top.add(d.getSystemName());
                }

            }
        }
        String tops = String.join("、", top);//平均修复轮次大于1系统

        Data avgRepairRoundMoreThanOne = new Data("avgRepairRoundMoreThanOne", "平均修复轮次大于1系统", tops);

        listSystem.add(avgRepairRoundMoreThanOne);









        //TODO 排序得出主要缺陷
        TblReportMonthlyDefectLevel defectLevel0_4 = new TblReportMonthlyDefectLevel();//所有系统 systemType=0,level=4

        //一般性缺陷(主要缺陷)
        Data defectNumber0_4 = new Data("defectNumber0_4", "一般性缺陷数", defectLevel0_4.getDefectNumber());//一般性缺陷数
        Data percentage0_4 = new Data("percentage0_4", "一般性缺陷占比", defectLevel0_4.getPercentage());//一般性缺陷占比
        listDefectLevel.add(defectNumber0_4);
        listDefectLevel.add(percentage0_4);





        TblReportMonthlyDefectLevel defectLevel0_5 = new TblReportMonthlyDefectLevel();//所有系统 systemType=0,level=5

        Data defectNumber0_5 = new Data("defectNumber0_5", "严重缺陷数", defectLevel0_5.getDefectNumber());//严重缺陷数
        Data percentage0_5 = new Data("percentage0_5", "严重缺陷占比", defectLevel0_5.getPercentage());//严重缺陷占比

        listDefectLevel.add(defectNumber0_5);
        listDefectLevel.add(percentage0_5);




        TblReportMonthlyDefectLevel defectLevel1_5 = new TblReportMonthlyDefectLevel();//敏捷 systemType=1,level=5
        TblReportMonthlyDefectLevel defectLevel2_5 = new TblReportMonthlyDefectLevel();//运维 systemType=2,level=5
        TblReportMonthlyDefectLevel defectLevel3_5 = new TblReportMonthlyDefectLevel();//项目 systemType=3,level=5
        Integer defectNumber1_5 = defectLevel1_5.getDefectNumber();

        Integer defectNumber2_5 = defectLevel2_5.getDefectNumber();

        Integer defectNumber3_5 = defectLevel3_5.getDefectNumber();

        Integer defectNumberMax_5;
        TblReportMonthlyDefectLevel defectLevelMax_5;
        if(defectNumber1_5 > defectNumber2_5){
            defectNumberMax_5 = defectNumber1_5;
            defectLevelMax_5 = defectLevel1_5;
        } else {
            defectNumberMax_5 = defectNumber2_5;
            defectLevelMax_5 = defectLevel2_5;
        }
        defectLevelMax_5 = defectNumberMax_5 > defectNumber3_5 ? defectLevelMax_5 : defectLevel3_5;

        String systemName = defectLevelMax_5.getSystemType() == 1 ? "敏捷" : defectLevelMax_5.getSystemType() == 2 ? "运维" : "项目";

        Data defectLevelMaxSystemName = new Data("defectLevelMaxSystemName", "严重缺陷占比最大系统", systemName);//严重缺陷占比最大系统

        Data defectLevelMaxSystemPercentage = new Data("defectLevelMaxSystemPercentage", "严重缺陷占比最大系统缺陷数", defectLevelMax_5.getDefectNumber());//严重缺陷占比最大系统缺陷数


        listDefectLevel.add(defectLevelMaxSystemName);
        listDefectLevel.add(defectLevelMaxSystemPercentage);


        //漏检情况（在上面）


        //敏捷 （在上面）
        //全年累计
        //TODO 每个系统的累计数据哪里来？Excel导入

        List<TblReportCumulativeSystemData> cumulativeSystemDatas = new ArrayList<TblReportCumulativeSystemData>();//项目

        List<TblReportCumulativeSystemData> cumulativeSystemDatas2 = new ArrayList<TblReportCumulativeSystemData>();//项目

        Integer cumulativeDesignCaseNumberNum = 0;
        Integer cumulativeDefectNumberNum= 0;

        for(TblReportCumulativeSystemData cumulativeSystemData : cumulativeSystemDatas){
            if(cumulativeSystemData.getSystemType() == 1){//敏捷
                cumulativeDesignCaseNumberNum += cumulativeSystemData.getDesignCaseNumber();
                cumulativeDefectNumberNum += cumulativeSystemData.getDefectNumber();
            } else if(cumulativeSystemData.getSystemType() == 2){//运维
                if(cumulativeSystemData.getTaskNumber() > 5 ){
                    cumulativeSystemDatas2.add(cumulativeSystemData);
                }
            }

        }

        Double cumulativeDefectPercentNum = cumulativeDesignCaseNumberNum == 0 ? 0 : (cumulativeDefectNumberNum*1.0/cumulativeDesignCaseNumberNum);
        //敏捷类
        Data cumulativeDesignCaseNumber = new Data("cumulativeDesignCaseNumber", "全年累计设计用例数", cumulativeDesignCaseNumberNum);
        Data cumulativeDefectNumber = new Data("cumulativeDefectNumber", "全年累计缺陷数", cumulativeDefectNumberNum);
        Data cumulativeDefectPercent = new Data("cumulativeDefectPercent", "全年平均缺陷率", cumulativeDefectPercentNum);
        Data cumulativeQuality = new Data("cumulativeQuality", "全年平均开发质量", cumulativeDefectPercentNum > 4 ? "较低" : cumulativeDefectPercentNum > 2 ? "一般" : "较高");


        listAgile.add(cumulativeDesignCaseNumber);
        listAgile.add(cumulativeDefectNumber);
        listAgile.add(cumulativeDefectPercent);
        listAgile.add(cumulativeQuality);






        List<TblReportCumulativeSystemData> cumulativeSortedSystemDatas2 = cumulativeSystemDatas2.stream().sorted((a,b)->a.getDefectPercent().compareTo(b.getDefectPercent())).collect(Collectors.toList());


        //全年累计缺陷率最低的系统排名

        String cumulativeSystemTop = "";
        String cumulativeSystemTail = "";






        //全年累计缺陷率最高的系统排名


        for(int i = 0; i< cumulativeSortedSystemDatas2.size(); i++){
            if(i < 2){
                cumulativeSystemTop += (cumulativeSortedSystemDatas2.get(i).getSystemName() + "(缺陷率" + cumulativeSortedSystemDatas2.get(i).getDefectPercent() + "%)、");

            }else if(i == 2){
                cumulativeSystemTop += (cumulativeSortedSystemDatas2.get(i).getSystemName() + "(缺陷率" + cumulativeSortedSystemDatas2.get(i).getDefectPercent() + "%)");

            }
            if(i == cumulativeSortedSystemDatas2.size()-1){
                cumulativeSystemTail += (cumulativeSortedSystemDatas2.get(i).getSystemName() + "(缺陷率" + cumulativeSortedSystemDatas2.get(i).getDefectPercent() + "%)、");
            } else if(i > cumulativeSortedSystemDatas2.size()-4){
                cumulativeSystemTail += (cumulativeSortedSystemDatas2.get(i).getSystemName() + "(缺陷率" + cumulativeSortedSystemDatas2.get(i).getDefectPercent() + "%)、");

            }
        }
        Data cumulativeSortedSystemDatasTop = new Data("cumulativeSortedSystemDatasTop", "运维期系统全年累计缺陷率最低的系统排名", cumulativeSystemTop);
        Data cumulativeSortedSystemDatasLail = new Data("cumulativeSortedSystemDatasLail", "运维期系统全年累计缺陷率最高的系统排名", cumulativeSystemTail);



        listOperation.add(cumulativeSortedSystemDatasTop);

        listOperation.add(cumulativeSortedSystemDatasLail);



//        List<Data> listall = listOne.stream().flatMap(m -> m.getList().stream()).collect(Collectors.toList());

//        Map<String, Data> mapall = listall.stream().collect(Collectors.toMap(d->d.getCode(),d->d));

        Map<String, Data> mapAll = listOne.stream().flatMap(m -> m.getList().stream()).collect(Collectors.toMap(d -> d.getCode(), d -> d));


            List<TblReportMonthlyModule> modules = new ArrayList<>();

//        TblReportMonthlyModule oldModule = new TblReportMonthlyModule("2020-09",1,1,1,
//                "本月有{{plan_version_num}}次规划内版本,{{limit_version_num}}次临时版本；临时增加任务数{{临时增加任务数}}个,临时撤销任务数{{临时撤销任务数}}个；版本变更率为2.05%，软件测试风险(注1)为低；");

        Map<String, List<TblReportMonthlyModule>> moduleMap = new HashMap();

        modules.forEach(x -> {
            delModule(x,mapAll);
            String  key = "module_" + x.getPage() + "_" + x.getArea();
            List<TblReportMonthlyModule> modulePageAreas = moduleMap.get(key);
            if(modulePageAreas == null){
                modulePageAreas = new ArrayList<>();
                moduleMap.put(key, modulePageAreas);
            }
            modulePageAreas.add(x);
        });

        //TODO 漏检
        //TODO 遗留缺陷
        return null;
    }

    public void delModule(TblReportMonthlyModule module,Map<String, Data> map){

        module.setContentValue(delModule(module.getContent(), map));

    }

    public String delModule(String module,Map<String, Data> map){
        int begin = module.indexOf("{{");
        if(begin > 0){
            int end = module.indexOf("}}");
            if (end > 0) {
                String key = module.substring(begin+2,end);
                String value = map.get(key).getValue().toString();
                module = module.replace("{{" + key + "}}",value);
                return delModule(module, map);
            }else{
                return module;
            }

        }else{
            return module;
        }
    }
}
