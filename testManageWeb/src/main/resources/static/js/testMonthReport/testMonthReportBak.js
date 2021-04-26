var monthTime=_getQueryString('monthTime')

var globalData = {
    data:[],
    module: {},
    dataMap: {},
} //全局数据
var gData={
    "data": [
        {
            "code": "listBase",
            "name": "基本数据类型",
            "value": null,
            "list": [
                {
                    "code": "year",
                    "name": "年",
                    "value": "2020",
                    "list": null
                },
                {
                    "code": "month",
                    "name": "月",
                    "value": "5",
                    "list": null
                }
            ]
        },
        {
            "code": "listSystem",
            "name": "系统数据类型",
            "value": null,
            "list": []
        },
        {
            "code": "listAgile",
            "name": "敏捷类系统",
            "value": null,
            "list": []
        },
        {
            "code": "listOperation",
            "name": "运维期系统",
            "value": null,
            "list": []
        },
        {
            "code": "listNew",
            "name": "项目期系统",
            "value": null,
            "list": []
        },
        {
            "code": "listDefectLevel",
            "name": "缺陷等级",
            "value": null,
            "list": []
        }
    ],
    "module": {
        "module_15_1": [
            {
                "id": "30",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 15,
                "area": 1,
                "num": 1,
                "content": "本月30个运维期系统（非敏捷）总体缺陷率为2.29%，开发质量(注2)一般。",
                "contentName": null,
                "contentValue": "本月30个运维期系统（非敏捷）总体缺陷率为2.29%，开发质量(注2)一般。"
            },
            {
                "id": "31",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 15,
                "area": 1,
                "num": 2,
                "content": "质量好的系统请继续保持，质量差的系统请控制开发质量，降低缺陷率。",
                "contentName": null,
                "contentValue": "质量好的系统请继续保持，质量差的系统请控制开发质量，降低缺陷率。"
            }
        ],
        "module_14_1": [
            {
                "id": "28",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 14,
                "area": 1,
                "num": 1,
                "content": "本月5个敏捷类系统总体缺陷率为2.23%，开发质量(注2)一般；",
                "contentName": null,
                "contentValue": "本月5个敏捷类系统总体缺陷率为2.23%，开发质量(注2)一般；"
            },
            {
                "id": "29",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 14,
                "area": 1,
                "num": 2,
                "content": "质量好的系统请继续保持，质量差的系统请控制开发质量，降低缺陷率。",
                "contentName": null,
                "contentValue": "质量好的系统请继续保持，质量差的系统请控制开发质量，降低缺陷率。"
            }
        ],
        "module_17_1": [
            {
                "id": "32",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 17,
                "area": 1,
                "num": 1,
                "content": "2020年敏捷类系统累计发现缺陷1988个， 设计测试用例83512个，平均测试缺陷率2.38%，开发质量(注2)一般。",
                "contentName": null,
                "contentValue": "2020年敏捷类系统累计发现缺陷1988个， 设计测试用例83512个，平均测试缺陷率2.38%，开发质量(注2)一般。"
            },
            {
                "id": "33",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 17,
                "area": 1,
                "num": 2,
                "content": "其中，财产险理赔系统2018版及筋斗云-开放平台缺陷率较高，请关注开发质量，降低缺陷率。",
                "contentName": null,
                "contentValue": "其中，财产险理赔系统2018版及筋斗云-开放平台缺陷率较高，请关注开发质量，降低缺陷率。"
            }
        ],
        "module_18_2": [
            {
                "id": "37",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 18,
                "area": 2,
                "num": 1,
                "content": "*注：全年任务数小于等于5个的系统，不参与按系统的全年累计排名。",
                "contentName": null,
                "contentValue": "*注：全年任务数小于等于5个的系统，不参与按系统的全年累计排名。"
            }
        ],
        "module_19_1": [
            {
                "id": "38",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 19,
                "area": 1,
                "num": 1,
                "content": "9月遗留缺陷清单",
                "contentName": null,
                "contentValue": "9月遗留缺陷清单"
            }
        ],
        "module_18_1": [
            {
                "id": "34",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 18,
                "area": 1,
                "num": 1,
                "content": "全年任务数超过5个的运维期系统（非敏捷）中，全年累计缺陷率最低的系统排名为：应收应付系统2013版(缺陷率0.00%）、呼叫中心服务系统2013版(缺陷率0.14%） 、车辆标的库系统2017版(缺陷率0.18%）。",
                "contentName": null,
                "contentValue": "全年任务数超过5个的运维期系统（非敏捷）中，全年累计缺陷率最低的系统排名为：应收应付系统2013版(缺陷率0.00%）、呼叫中心服务系统2013版(缺陷率0.14%） 、车辆标的库系统2017版(缺陷率0.18%）。"
            },
            {
                "id": "35",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 18,
                "area": 1,
                "num": 2,
                "content": "全年任务数超过5个的系统中，全年累计缺陷率最高的系统排名为：风险管理信息系统2017版(缺陷率23.91%）、筋斗云－再保(6月至今）(缺陷率8.11%）、筋斗云－报表(7月至今）(缺陷率7.87%）。",
                "contentName": null,
                "contentValue": "全年任务数超过5个的系统中，全年累计缺陷率最高的系统排名为：风险管理信息系统2017版(缺陷率23.91%）、筋斗云－再保(6月至今）(缺陷率8.11%）、筋斗云－报表(7月至今）(缺陷率7.87%）。"
            },
            {
                "id": "36",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 18,
                "area": 1,
                "num": 3,
                "content": "质量好的系统请继续保持，质量差的系统请控制开发质量，降低缺陷率。",
                "contentName": null,
                "contentValue": "质量好的系统请继续保持，质量差的系统请控制开发质量，降低缺陷率。"
            }
        ],
        "module_19_2": [
            {
                "id": "39",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 19,
                "area": 2,
                "num": 1,
                "content": "漏检经验总结",
                "contentName": null,
                "contentValue": "漏检经验总结"
            }
        ],
        "module_11_1": [
            {
                "id": "24",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 11,
                "area": 1,
                "num": 1,
                "content": "本月所有系统中，严重缺陷占比1.35%(共36个），较前几月有明显下降。严重缺陷中，敏捷类系统占比最大（共27个）。",
                "contentName": null,
                "contentValue": "本月所有系统中，严重缺陷占比1.35%(共36个），较前几月有明显下降。严重缺陷中，敏捷类系统占比最大（共27个）。"
            },
            {
                "id": "25",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 11,
                "area": 1,
                "num": 2,
                "content": "请严格控制敏捷类系统的开发质量，降低严重缺陷的个数。",
                "contentName": null,
                "contentValue": "请严格控制敏捷类系统的开发质量，降低严重缺陷的个数。"
            },
            {
                "id": "26",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 11,
                "area": 1,
                "num": 3,
                "content": "一般性缺陷仍是主要缺陷，占比最大（占86.95%）。",
                "contentName": null,
                "contentValue": "一般性缺陷仍是主要缺陷，占比最大（占86.95%）。"
            }
        ],
        "module_10_1": [
            {
                "id": "21",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 10,
                "area": 1,
                "num": 1,
                "content": "本月系统平均缺陷修复轮次为1.05，与一至八修复轮次数据相比，基本持平并略有下降，修复质量为一般。",
                "contentName": null,
                "contentValue": "本月系统平均缺陷修复轮次为1.05，与一至八修复轮次数据相比，基本持平并略有下降，修复质量为一般。"
            },
            {
                "id": "22",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 10,
                "area": 1,
                "num": 2,
                "content": "本月缺陷修复轮次大于1的系统：意健险理赔系统2018版（敏捷)、车险理赔系统2015版、筋斗云-开放平台、筋斗云-车险、智能费控系统2019版及移动理赔系统。",
                "contentName": null,
                "contentValue": "本月缺陷修复轮次大于1的系统：意健险理赔系统2018版（敏捷)、车险理赔系统2015版、筋斗云-开放平台、筋斗云-车险、智能费控系统2019版及移动理赔系统。"
            },
            {
                "id": "23",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 10,
                "area": 1,
                "num": 3,
                "content": "缺陷修复轮次影响测试时效，请相关系统加强缺陷修复质量管理，降低缺陷修复轮次，保证缺陷的修复质量。",
                "contentName": null,
                "contentValue": "缺陷修复轮次影响测试时效，请相关系统加强缺陷修复质量管理，降低缺陷修复轮次，保证缺陷的修复质量。"
            }
        ],
        "module_12_1": [
            {
                "id": "27",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 12,
                "area": 1,
                "num": 1,
                "content": "9月无测试漏检，检出率为100.00%。",
                "contentName": null,
                "contentValue": "9月无测试漏检，检出率为100.00%。"
            }
        ],
        "module_6_2": [
            {
                "id": "14",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 6,
                "area": 2,
                "num": 1,
                "content": "版本变更率=(临时增加任务数+临时删减任务数)/测试任务总数*100%",
                "contentName": null,
                "contentValue": "版本变更率=(临时增加任务数+临时删减任务数)/测试任务总数*100%"
            }
        ],
        "module_7_1": [
            {
                "id": "15",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 7,
                "area": 1,
                "num": 1,
                "content": "本月敏捷类系统测试任务数为194个，设计用例15465个，共发现缺陷345个，总体缺陷率为2.23%，敏捷类系统开发质量(注2)一般；",
                "contentName": null,
                "contentValue": "本月敏捷类系统测试任务数为194个，设计用例15465个，共发现缺陷345个，总体缺陷率为2.23%，敏捷类系统开发质量(注2)一般；"
            }
        ],
        "module_7_2": [
            {
                "id": "16",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 7,
                "area": 2,
                "num": 1,
                "content": "注：本月起，系统分类统计口径发生变化。根据调研结果，上述五个系统后续归入敏捷类系统进行统计分析。",
                "contentName": null,
                "contentValue": "注：本月起，系统分类统计口径发生变化。根据调研结果，上述五个系统后续归入敏捷类系统进行统计分析。"
            }
        ],
        "module_8_1": [
            {
                "id": "17",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 8,
                "area": 1,
                "num": 1,
                "content": "本月运维期(非敏捷)系统测试任务数为292个，设计用例17355个，共发现缺陷398个，总体缺陷率为2.29%，非敏捷运维期系统开发质量(注2)一般；",
                "contentName": null,
                "contentValue": "本月运维期(非敏捷)系统测试任务数为292个，设计用例17355个，共发现缺陷398个，总体缺陷率为2.29%，非敏捷运维期系统开发质量(注2)一般；"
            }
        ],
        "module_5_1": [
            {
                "id": "10",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 5,
                "area": 1,
                "num": 1,
                "content": "9月运维期系统共发布8个版本，高于去年同期水平（2019年月平均版本数为6.83）",
                "contentName": null,
                "contentValue": "9月运维期系统共发布8个版本，高于去年同期水平（2019年月平均版本数为6.83）"
            },
            {
                "id": "11",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 5,
                "area": 1,
                "num": 2,
                "content": "请继续做好版本规划，控制版本变更，保证版本质量。",
                "contentName": null,
                "contentValue": "请继续做好版本规划，控制版本变更，保证版本质量。"
            }
        ],
        "module_6_1": [
            {
                "id": "12",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 6,
                "area": 1,
                "num": 1,
                "content": "月临时新增需求4个，临时减少需求2个，版本变更率为2.05%,总体呈下降趋势。",
                "contentName": null,
                "contentValue": "月临时新增需求4个，临时减少需求2个，版本变更率为2.05%,总体呈下降趋势。"
            },
            {
                "id": "13",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 6,
                "area": 1,
                "num": 2,
                "content": "希望各系统继续关注版本任务控制的稳定性，保持较低的版本变更率。",
                "contentName": null,
                "contentValue": "希望各系统继续关注版本任务控制的稳定性，保持较低的版本变更率。"
            }
        ],
        "module_3_1": [
            {
                "id": "4",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 3,
                "area": 1,
                "num": 1,
                "content": "本月有2次规划内版本,6次临时版本；临时增加任务数4个,临时撤销任务数2个；版本变更率为2.05%，软件测试风险(注1)为低；",
                "contentName": null,
                "contentValue": "本月有2次规划内版本,6次临时版本；临时增加任务数4个,临时撤销任务数2个；版本变更率为2.05%，软件测试风险(注1)为低；"
            },
            {
                "id": "5",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 3,
                "area": 1,
                "num": 2,
                "content": "敏捷类系统测试任务数为194个，设计用例15465个，共发现缺陷345个，总体缺陷率为2.23%，开发质量(注2)一般；",
                "contentName": null,
                "contentValue": "敏捷类系统测试任务数为194个，设计用例15465个，共发现缺陷345个，总体缺陷率为2.23%，开发质量(注2)一般；"
            },
            {
                "id": "6",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 3,
                "area": 1,
                "num": 3,
                "content": "运维期(非敏捷)系统测试任务数为292个，设计用例17355个，共发现缺陷398个，总体缺陷率为2.29%，开发质量(注2)一般；",
                "contentName": null,
                "contentValue": "运维期(非敏捷)系统测试任务数为292个，设计用例17355个，共发现缺陷398个，总体缺陷率为2.29%，开发质量(注2)一般；"
            },
            {
                "id": "7",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 3,
                "area": 1,
                "num": 4,
                "content": "项目期(非敏捷)系统测试任务数为16个，设计用例1576个，共发现缺陷58个，总体缺陷率为3.68%，开发质量(注2)一般；",
                "contentName": null,
                "contentValue": "项目期(非敏捷)系统测试任务数为16个，设计用例1576个，共发现缺陷58个，总体缺陷率为3.68%，开发质量(注2)一般；"
            }
        ],
        "module_3_2": [
            {
                "id": "8",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 3,
                "area": 2,
                "num": 1,
                "content": "注3：为配合信息科技部PMO/质量/安全处 每月KPI统计，每月月报统计口径为上月26日至本月25日。",
                "contentName": null,
                "contentValue": "注3：为配合信息科技部PMO/质量/安全处 每月KPI统计，每月月报统计口径为上月26日至本月25日。"
            },
            {
                "id": "9",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 3,
                "area": 2,
                "num": 2,
                "content": "注4：本月起，系统统计口径更改为敏捷类系统、运维期系统（非敏捷）及项目期系统（非敏捷）三类。",
                "contentName": null,
                "contentValue": "注4：本月起，系统统计口径更改为敏捷类系统、运维期系统（非敏捷）及项目期系统（非敏捷）三类。"
            }
        ],
        "module_1_1": [
            {
                "id": "1",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 1,
                "area": 1,
                "num": 1,
                "content": "2020年9月软件质量分析月报",
                "contentName": null,
                "contentValue": "2020年9月软件质量分析月报"
            }
        ],
        "module_1_2": [
            {
                "id": "2",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 1,
                "area": 2,
                "num": 1,
                "content": "信息科技部测试共享处",
                "contentName": null,
                "contentValue": "信息科技部测试共享处"
            },
            {
                "id": "3",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 1,
                "area": 2,
                "num": 2,
                "content": "2020年10月12日",
                "contentName": null,
                "contentValue": "2020年10月12日"
            }
        ],
        "module_8_2": [
            {
                "id": "18",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 8,
                "area": 2,
                "num": 1,
                "content": "平台采用敏捷开发模式外，其余系统均是稳态开发模式，后续归入运维期系统（非敏捷）类进行统计分析。",
                "contentName": null,
                "contentValue": "平台采用敏捷开发模式外，其余系统均是稳态开发模式，后续归入运维期系统（非敏捷）类进行统计分析。"
            }
        ],
        "module_9_1": [
            {
                "id": "19",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 9,
                "area": 1,
                "num": 1,
                "content": "本月项目期(非敏捷)系统测试任务数为16个，设计用例1576个，共发现缺陷58个，总体缺陷率为3.68%，项目期系统(非敏捷）开发质量(注2)一般；",
                "contentName": null,
                "contentValue": "本月项目期(非敏捷)系统测试任务数为16个，设计用例1576个，共发现缺陷58个，总体缺陷率为3.68%，项目期系统(非敏捷）开发质量(注2)一般；"
            },
            {
                "id": "20",
                "status": 1,
                "createBy": 1,
                "createDate": "2020-10-28 16:02:21",
                "lastUpdateBy": 1,
                "lastUpdateDate": "2020-10-28 16:02:21",
                "yearMonth": "2020-10",
                "page": 9,
                "area": 1,
                "num": 2,
                "content": "请关注项目期系统开发质量，降低缺陷率；",
                "contentName": null,
                "contentValue": "请关注项目期系统开发质量，降低缺陷率；"
            }
        ]
    },
    "status": "1"
}
/*data 变量列表 dataMap 对应的*/
console.log('月报id'+monthTime);
$(function () {
    // queryTplData()
    queryData()

})
globalData=gData
globalData.dataMap=getDataMap(gData.data)

// function queryTplData() {
//     $("#loading").css('display', 'block');
//     $.ajax({
//         url: "/testManage/monthlyReport/testReport",
//         method: "post",
//         data: {time:monthTime},
//         dataType: "json",
//         success: function (res) {
//             if(res.status==='1'){
//                 if(res.data){
//                     globalData.data=res.data
//                     globalData.dataMap=getDataMap(res.data)
//                 }
//                 res.module?globalData.module=res.module:''
//
//             }
//             $("#loading").css('display', 'none');
//         }
//     });
// }
function queryData() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/getAllMonthReportData",
        method: "post",
        data: {time:monthTime},
        dataType: "json",
        success: function (res) {
            $("#loading").css('display', 'none');
            if(res.status==='1'){
                globalData=res
                globalData.dataMap=getDataMap(res.data)
            }
        }
    });
}
function initPage() {
    p1.init()
    p3.init()
    p5.init()
    p6.init()
    p7.init()
    p8.init()
    p9.init()
    p10.init()
    p11.init()
    p12.init()
    p14.init()
    p15.init()
    p17.init()
    p18.init()
    p19.init()
}
/*获取点击chart 位置数据信息*/
function getChartInfo(params) {
    var event = params.event.event
    return {
        pos: {left: event.offsetX, top: event.offsetY + 20},
        data: params.data,
        dataIndex: params.dataIndex,
        name: params.name,
        seriesName: params.seriesName,
        seriesIndex: params.seriesIndex,
    }
}
function getDataMap(list) {
    var _dataMap={}
    for (var i=0;i<list.length;i++){
        var _list=list[i].list||[]
        for(var j=0;j<_list.length;j++){
            var item=_list[j]
            _dataMap[item.code]={
                name:item.name,
                value:item.value,
                typeCode:list[i].code,
                typeName:list[i].name
            }
        }
    }
    return _dataMap
}
/*修改变量的值后 把所有的相同的值修改掉*/
function changeAllSameTpl(dataAttr,key,val){
    var list=$('span[data-key=' + key + ']',$('[data-data="'+dataAttr+'"]'));
    for(var i=0;i<list.length;i++){
        $(list[i]).html(val)
    }
}
// legend 换行函数 num  换行字数
function formatterLegend(params, num) {
//超过十个字符就换行展示
//                     return (name.length > 10 ? (name.slice(0,10)+"...") : name );
    var newParamsName = "";// 最终拼接成的字符串
    var paramsNameNumber = params.length;// 实际标签的个数
    var provideNumber = num ? num : 10;// 每行能显示的字的个数
    var rowNumber = Math.ceil(paramsNameNumber / provideNumber);// 换行的话，需要显示几行，向上取整

    // 条件等同于rowNumber>1
    if (paramsNameNumber > provideNumber) {

        for (var p = 0; p < rowNumber; p++) {
            var tempStr = "";// 表示每一次截取的字符串
            var start = p * provideNumber;// 开始截取的位置
            var end = start + provideNumber;// 结束截取的位置
            // 此处特殊处理最后一行的索引值
            if (p == rowNumber - 1) {
                // 最后一次不换行
                tempStr = params.substring(start, paramsNameNumber);
            } else {
                // 每一次拼接字符串并换行
                tempStr = params.substring(start, end) + "\n";
            }
            newParamsName += tempStr;// 最终拼成的字符串
        }
    } else {
        // 将旧标签的值赋给新标签
        newParamsName = params;
    }
    //将最终的字符串返回
    return newParamsName
}
//rate >=2%
function isMoreTwo(str) {
    return Number(str.substr(0, 1)) >= 2
}
/*渲染tpl list*/
function renderTplList(target, attrs, className) {
    var data_data = 'dataMap'
    var data_list = 'data'
    var data_tpl = 'module.'+attrs
    var data = _getObject('globalData', data_data)
    var tpl = _getObject('globalData', data_tpl)||[]
    var html = ''
    var _className = className ? className : ''
    for (var i = 0; i < tpl.length; i++) {
        var _tpl = renderTpl(tpl[i].content, data)
        html += '<p class="edit_tpl ' + _className + '" data-id="'+tpl[i].id+'" data-data="' + data_data + '" data-list="' + data_list + '" data-tpl="' + data_tpl + '.' + i + '" data-index="' + i + '">' + _tpl + '</p>'
    }
    target.html(html)
    target.attr('data-data', data_data)
    target.attr('data-list', data_list)
    target.attr('data-tpl', data_tpl)
}

/*模板可编辑*/
tplEditModal.init()
/*变量可编辑*/
//具体url等等通过获取父级属性信息
var editP=new EditPopper({
    el:'edit_var',
    success:function (target,val,hide) {
        var key=$(target).attr('data-key') //元素自身上
        var url=$(target).parent().attr('data-url') //父元素上
        var dataAttr=$(target).parent().attr('data-data') //data 的属性指向(父元素上)
        // todo 调用服务
        _setObject('globalData',dataAttr+'.'+key+'.value',val)
        changeAllSameTpl(dataAttr,key,val)
        hide()
    },
})

var p1 = {
    init: function () {
        renderTplList($('#module_1_1'), 'module_1_1')
        renderTplList($('#module_1_2'), 'module_1_2')
        renderTplList($('#page19_department'), 'module_1_2')

    }
}
p1.init()

/*page3*/
globalData.p3Data = {
    table1: [
        {
            name: 'ECIF系统',
            rate: '0%'
        },
        {
            name: '车辆标的库系统2017版',
            rate: '0%'
        },
        {
            name: '非车人身双代系统2009版',
            rate: '0%'
        },
        {
            name: '呼叫系统服务中心2103版',
            rate: '0%'
        },
        {
            name: '集中收款平台2013版',
            rate: '0%'
        },
        {
            name: '洗钱风险管理平台2013版',
            rate: '0%'
        },
        {
            name: '续保管理平台2016版',
            rate: '0%'
        },
        {
            name: '应收应付系统2013版',
            rate: '0%'
        },
    ],
    table2: [
        {
            name: '车险理赔系统2015版',
            rate: '1.21%'
        },
        {
            name: '增值服务管理系统',
            rate: '1.67%'
        },
        {
            name: '意健险理赔系统2018版',
            rate: '2.16%'
        },
        {
            name: '财产理赔系统2018版',
            rate: '2.20%'
        },
        {
            name: '筋斗云-开放平台',
            rate: '4.17%'
        },
    ],
    table3: [
        {
            name: '风险管理信息系统2017版',
            rate: '43.75%'
        },
        {
            name: '筋斗云-再保',
            rate: '7.53%'
        },
        {
            name: '筋斗云-车险',
            rate: '5.4%'
        },
    ],
    table4: [
        {
            name: '移动理赔',
            rate: '1.85%'
        },
        {
            name: '智能费控系统2019版',
            rate: '5.52%'
        },
    ]
}
// table1
var p3 = {
    init: function () {
        renderTplList($('#module_3_1'), 'module_3_1')
        renderTplList($('#module_3_2'), 'module_3_2','notes')
        this.initTable(globalData.p3Data.table1, $('#p3table1_body'))
        this.initTable(globalData.p3Data.table2, $('#p3table2_body'))
        this.initTable(globalData.p3Data.table3, $('#p3table3_body'))
        this.initTable(globalData.p3Data.table4, $('#p3table4_body'))
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var rate = data[i].rate, name = data[i].name
            var cn = isMoreTwo(data[i].rate) ? 'red_tr' : '';
            html += '<tr class="' + cn + '">' +
                ' <td>' + name + '</td>' +
                ' <td>' + rate + '</td>' +
                ' </tr>'
        }
        target.html(html)
    },
}
p3.init()

globalData.p5Data = {
    table: [
        {
            planNum: '1',
            temNum: '4',
            allNum: '5',
        },
        {
            planNum: '1',
            temNum: '0',
            allNum: '1',
        },
        {
            planNum: '2',
            temNum: '3',
            allNum: '5',
        },
        {
            planNum: '2',
            temNum: '4',
            allNum: '6',
        },
        {
            planNum: '1',
            temNum: '3',
            allNum: '4',
        },
        {
            planNum: '2',
            temNum: '2',
            allNum: '4',
        },
        {
            planNum: '2',
            temNum: '4',
            allNum: '6',
        },
        {
            planNum: '2',
            temNum: '2',
            allNum: '4',
        },
        {
            planNum: '2',
            temNum: '6',
            allNum: '8',
        },
        {
            planNum: '',
            temNum: '',
            allNum: '',
        },
        {
            planNum: '',
            temNum: '',
            allNum: '',
        },
        {
            planNum: '',
            temNum: '',
            allNum: '',
        },
    ],
}
var p5 = {
    init: function () {
        this.initTable(globalData.p5Data.table, $('#p5table1'))
        this.initChart()

        renderTplList($('#module_5_1'), 'module_5_1','arrow_class mg_10')
        // renderTplList($('#p5tpl1'), 'p5Data.tpl1Data', 'arrow_class mg_10')
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < 12; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>2020年' + i + 1 + '月</td>\n' +
                ' <td>' + item.planNum + '</td>\n' +
                ' <td>' + item.temNum + '</td>\n' +
                ' <td>' + item.allNum + '</td>\n' +
                '  </tr>'
        }
        target.html(html)
    },
    initChart: function () {
        var myChart51 = echarts.init(document.getElementById('page5_chart'));
        // 指定图表的配置项和数据
        var option51 = {
            grid: {
                left: 40,
                bottom: 40,
                right: 140
            },
            title: {
                text: '各月份版本数趋势图',
                left: '30%',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            tooltip: {},
            legend: {
                data: ['计划内版本数', '临时版本数', '月总版本数'],
                top: 'middle',
                orient: 'vertical',
                right: 10
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {},
            series: [
                {
                    name: '计划内版本数',
                    type: 'line',
                    data: [1, 1, 2, 2, 1, 2, 2, 2, 2],
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'circle',
                    symbolSize: 10
                },
                {
                    name: '临时版本数',
                    type: 'line',
                    data: [4, 0, 3, 4, 3, 2, 4, 2, 3],
                    itemStyle: {
                        color: '#7FD4E9'
                    },
                    symbol: 'diamond',
                    symbolSize: 10
                },
                {
                    name: '月总版本数',
                    type: 'line',
                    data: [5, 1, 5, 6, 1, 4, 4, 6, 4],
                    itemStyle: {
                        color: '#A5A5A5'
                    },
                    symbol: 'triangle',
                    symbolSize: 10
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart51.setOption(option51);

        myChart51.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page5_chart',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option51.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart51.setOption(option51);
                    hide()
                }
            })

        });
    },
}
p5.init()

var p6 = {
    init: function () {
        this.initChart1()
        this.initChart2()
        renderTplList($('#module_6_1'), 'module_6_1','arrow_class mg_10')
        renderTplList($('#module_6_2'), 'module_6_2','mark mgl_40 mgt_20')
    },
    initChart1: function () {

        var myChart61 = echarts.init(document.getElementById('page6_chart1'));

        // 指定图表的配置项和数据
        var option61 = {
            grid: {
                bottom: 40,
                right: 80
            },
            title: {
                text: '2020年各月份新增&减少任务数',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['新增', '减少'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {},
            series: [
                {
                    name: '新增',
                    type: 'bar',
                    data: [1, 1, 2, 2, 1, 2, 2, 2, 2],
                    itemStyle: {
                        color: '#22A0D7'
                    }
                },
                {
                    name: '减少',
                    type: 'bar',
                    data: [4, 0, 3, 4, 3, 2, 4, 2],
                    itemStyle: {
                        color: '#7FD4E9'
                    }
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart61.setOption(option61);
        myChart61.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page6_chart1',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option61.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart61.setOption(option61);
                    hide()
                }
            })

        });
    },
    initChart2: function () {
        var myChart62 = echarts.init(document.getElementById('page6_chart2'));

        // 指定图表的配置项和数据
        var option62 = {
            grid: {
                bottom: 40,
                right: 200
            },
            title: {
                text: '各月份版本变更率趋势图',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['各月份版本变更率趋势图'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {
                axisLabel: {
                    formatter: function (val) {
                        return val + '%';
                    }
                },
            },
            series: [
                {
                    name: '各月份版本变更率趋势图',
                    type: 'line',
                    data: [25, 10, 10.4, 14.5, 15.2, 15.6, 4, 5, 4.5],
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10
                },
            ]
        };
        myChart62.setOption(option62);
        myChart62.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page6_chart2',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option62.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart62.setOption(option62);
                    hide()
                }
            })

        });
    }
}
p6.init()

globalData.p7Data={
    table: [
        {
            name: '车险理赔系统2015版',
            taskNum: '16',
            caseNum: '5722',
            defectNum: '16',
            defectRate: '1.21%',
        },
        {
            name: '增值服务管理系统',
            taskNum: '13',
            caseNum: '957',
            defectNum: '13',
            defectRate: '1.67%',
        },
        {
            name: '意健险理赔系统2018版',
            taskNum: '44',
            caseNum: '2967',
            defectNum: '44',
            defectRate: '2.16%',
        },
        {
            name: '财产险理赔系统2018版',
            taskNum: '38',
            caseNum: '2364',
            defectNum: '38',
            defectRate: '2.22%',
        },
        {
            name: '筋斗云-开放平台',
            taskNum: '83',
            caseNum: '3455',
            defectNum: '83',
            defectRate: '4.17%',
        },
    ],
}
var p7={
    init:function () {
        this.initTable(globalData.p7Data.table,$('#p71table1'))
        this.initChart()
        renderTplList($('#module_7_1'),'module_7_1','arrow_class')
        renderTplList($('#module_7_2'),'module_7_2',)
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>' + item.name + '</td>\n' +
                ' <td>' + item.taskNum + '</td>\n' +
                ' <td>' + item.caseNum + '</td>\n' +
                ' <td>' + item.defectNum + '</td>\n' +
                ' <td>' + item.defectRate + '</td>\n' +
                '  </tr>'
        }
        target.html(html)
    },
    initChart:function () {
        var myChart711 = echarts.init(document.getElementById('page71_chart1'));

        // 指定图表的配置项和数据
        var option711 = {
            grid: {
                bottom: 40,
                right: 100
            },
            title: {
                text: '任务类型',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['业务需求', '缺陷修复'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            series: [
                {
                    name: '业务需求',
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    label: {
                        formatter: '{c} \n {d}%',
                        position: 'inner',
                    },
                    data: [
                        {
                            name: '业务需求',
                            value: 200
                        },
                        {
                            name: '缺陷修复',
                            value: 100
                        },
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    itemStyle: {
                        color: function (params) {
                            //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                            var colorList = ['#22A0D7', '#3BD1D5'];
                            return colorList[params.dataIndex]
                        }
                    }
                },
            ]
        };
        myChart711.setOption(option711);
        myChart711.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page71_chart1',
                value: info.data.value,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option711.series[info.seriesIndex].data[info.dataIndex].value = val;
                    myChart711.setOption(option711);
                    hide()
                }
            })

        });
    }
}
p7.init()


var p8={
    init:function () {
        this.initChart1()
        this.initChart2()
        this.initChart3()
        this.initChart4()
        renderTplList($('#module_8_1'),'module_8_1','arrow_class')
        renderTplList($('#module_8_2'),'module_8_2',)
    },
    initChart1:function () {

        var myChart71 = echarts.init(document.getElementById('page7_chart1'));

        // 指定图表的配置项和数据
        var option71 = {
            grid: {
                bottom: 40,
            },
            title: {
                text: '任务数',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['任务数'],
                right: 10,
                top: 'middle',
                orient: 'vertical',
                show: false
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {},
            series: [
                {
                    name: '任务数',
                    type: 'bar',
                    data: [1, 1, 2, 2, 1, 2, 2, 2, 2],
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}',
                    },
                    itemStyle: {
                        color: '#22A0D7'
                    }
                },
            ]
        };
        myChart71.setOption(option71);
        myChart71.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page7_chart1',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option71.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart71.setOption(option71);
                    hide()
                }
            })

        });
    },
    initChart2:function () {
        var myChart72 = echarts.init(document.getElementById('page7_chart2'));
        // 指定图表的配置项和数据
        var option72 = {
            grid: {
                bottom: 40,
                right: 100
            },
            title: {
                text: '任务类型',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['业务需求', '缺陷修复'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            series: [
                {
                    name: '业务需求',
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    label: {
                        formatter: '{c} \n {d}%',
                        position: 'inner',
                    },
                    data: [
                        {
                            name: '业务需求',
                            value: 200
                        },
                        {
                            name: '缺陷修复',
                            value: 100
                        },
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    itemStyle: {
                        color: function (params) {
                            //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                            var colorList = ['#22A0D7', '#3BD1D5'];
                            return colorList[params.dataIndex]
                        }
                    }
                },
            ]
        };
        myChart72.setOption(option72);
        myChart72.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page7_chart2',
                value: info.data.value,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option72.series[info.seriesIndex].data[info.dataIndex].value = val;
                    myChart72.setOption(option72);
                    hide()
                }
            })

        });
    },
    initChart3:function () {
        var myChart73 = echarts.init(document.getElementById('page7_chart3'));
        // 指定图表的配置项和数据
        var option73 = {
            grid: {
                bottom: 40,
            },
            title: {
                text: '用例数',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['用例数'],
                right: 10,
                top: 'middle',
                orient: 'vertical',
                show: false
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {},
            series: [
                {
                    name: '用例数',
                    type: 'bar',
                    data: [1, 1, 2, 2, 1, 2, 2, 2, 2],
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}',
                    },
                    itemStyle: {
                        color: '#22A0D7'
                    }
                },
            ]
        };
        myChart73.setOption(option73);
        myChart73.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page7_chart3',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option73.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart73.setOption(option73);
                    hide()
                }
            })

        });
    },
    initChart4:function () {
        var myChart74 = echarts.init(document.getElementById('page7_chart4'));

        // 指定图表的配置项和数据
        var option74 = {
            grid: {
                bottom: 40,
            },
            title: {
                text: '缺陷数',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['缺陷数'],
                right: 10,
                top: 'middle',
                orient: 'vertical',
                show: false
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {},
            series: [
                {
                    name: '缺陷数',
                    type: 'bar',
                    data: [1, 1, 24, 2, 13, 22, 2, 22, 2],
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}',
                    },
                    itemStyle: {
                        color: '#22A0D7'
                    }
                },
            ]
        };
        myChart74.setOption(option74);
        myChart74.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page7_chart4',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option74.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart74.setOption(option74);
                    hide()
                }
            })

        });
    },
}
p8.init()

globalData.p9Data={
    table:[
        {
            name:'车险理赔系统2015版',
            taskNum:'16',
            caseNum:'5772',
            defectNum:'16',
            defectRate:'1.21%',
        },
        {
            name:'增值服务管理系统',
            taskNum:'16',
            caseNum:'5772',
            defectNum:'16',
            defectRate:'1.21%',
        },
        {
            name:'意健险理赔系统2018版',
            taskNum:'16',
            caseNum:'5772',
            defectNum:'16',
            defectRate:'1.21%',
        },
        {
            name:'财产险理赔系统2018版',
            taskNum:'16',
            caseNum:'5772',
            defectNum:'16',
            defectRate:'1.21%',
        },
        {
            name:'筋斗云-开放平台',
            taskNum:'16',
            caseNum:'5772',
            defectNum:'16',
            defectRate:'1.21%',
        }
    ]
}
var p9={
    init:function () {
        this.initTable(globalData.p9Data.table,$('#p72table1'))
        this.initChart()
        renderTplList($('#module_9_1'),'module_9_1','arrow_class')
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>' + item.name + '</td>\n' +
                ' <td>' + item.taskNum + '</td>\n' +
                ' <td>' + item.caseNum + '</td>\n' +
                ' <td>' + item.defectNum + '</td>\n' +
                ' <td>' + item.defectRate + '</td>\n' +
                '  </tr>'
        }
        target.html(html)
    },
    initChart:function () {
        var myChart721 = echarts.init(document.getElementById('page72_chart1'));

        // 指定图表的配置项和数据
        var option721 = {
            grid: {
                bottom: 40,
                right: 100
            },
            title: {
                text: '任务类型',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['业务需求', '缺陷修复'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            series: [
                {
                    name: '业务需求',
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    label: {
                        formatter: '{c} \n {d}%',
                        position: 'inner',
                    },
                    data: [
                        {
                            name: '业务需求',
                            value: 200
                        },
                        {
                            name: '缺陷修复',
                            value: 100
                        },
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    itemStyle: {
                        color: function (params) {
                            //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                            var colorList = ['#22A0D7', '#3BD1D5'];
                            return colorList[params.dataIndex]
                        }
                    }
                },
            ]
        };

        myChart721.setOption(option721);
        myChart721.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page72_chart1',
                value: info.data.value,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option721.series[info.seriesIndex].data[info.dataIndex].value = val;
                    myChart721.setOption(option721);
                    hide()
                }
            })

        });
    }
}
p9.init()

globalData.p10Data={
    tpl1Data: {
        list: [{
            name: '测试',
            list: [
                {
                    name: 'seriousRate',
                    value: 5,
                    mark: '严重缺陷比'
                },
                {
                    name: 'seriousNum',
                    value: 5,
                    mark: '严重缺陷数'
                },
                {
                    name: 'agileNum',
                    value: 5,
                    mark: '敏捷类个数'
                },
                {
                    name: 'generalityRate',
                    value: 5,
                    mark: '一般性比例'
                },
            ]
        }],
        data:{
            seriousRate:{name:'严重缺陷比',value:1.35},
            generalityRate:{name:'严重缺陷数',value:86.95},
            seriousNum:{name:'敏捷类个数',value:36},
            agileNum:{name:'一般性比例',value:27},
        },
        tpl: [
            '本月所有系统中，严重缺陷占比{{seriousRate}}%(共{{seriousNum}}个），较前几月有明显下降。严重缺陷中，敏捷类系统占比最大（共{{agileNum}}个）。',
            '请严格控制敏捷类系统的开发质量，降低严重缺陷的个数。',
            '一般性缺陷仍是主要缺陷，占比最大（占{{generalityRate}}%）。',
        ]
    },
}
var p10={
    init:function () {
        this.initChart1()
        this.initChart2()
        renderTplList($('#module_10_1'),'module_10_1','arrow_class')
    },
    initChart1:function () {
        var myChart91 = echarts.init(document.getElementById('page9_chart1'));

        // 指定图表的配置项和数据
        var option91 = {
            grid: {
                bottom: 40,
                right: 130
            },
            title: {
                text: '缺陷修复轮次趋势图',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['缺陷修复轮次趋势图'],
                right: 10,
                top: 'middle',
                orient: 'vertical',
                formatter: function (param) {
                    return formatterLegend(param, 6)
                }
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
                axisLabel: {
                    interval: 0,
                    rotate: -40,
                    fontSize: 10
                }
            },
            yAxis: {},
            series: [
                {
                    name: '缺陷修复轮次趋势图',
                    type: 'line',
                    barWidth: 20,
                    data: [1, 5, 2, 4, 1, 2, 2],
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10
                },
            ]
        };

        myChart91.setOption(option91);
        myChart91.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page9_chart1',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option91.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart91.setOption(option91);
                    hide()
                }
            })

        });
    },
    initChart2:function () {
        var myChart92 = echarts.init(document.getElementById('page9_chart2'));

        // 指定图表的配置项和数据
        var option92 = {
            grid: {
                bottom: 80,
            },
            title: {
                text: '平均修复轮次大于1的系统',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['平均修复轮次大于1的系统'],
                right: 10,
                top: 'middle',
                orient: 'vertical',
                show: false
            },
            xAxis: {
                type: 'category',
                data: ['意健险理赔系统', '车险理赔系统2015版', '基本法系统2019版', '财产险理赔系统2020版', 'ECIF系统2016版'],
                axisLabel: {
                    interval: 0,
                    rotate: 40,
                    fontSize: 10
                }
            },
            yAxis: {},
            series: [
                {
                    name: '缺陷修复轮次趋势图',
                    type: 'bar',
                    barWidth: 20,
                    data: [1, 5, 2, 4, 1],
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}',
                        color: '#000'
                    },
                    itemStyle: {
                        color: '#22A0D7'
                    }
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart92.setOption(option92);
        myChart92.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page9_chart2',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option92.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart92.setOption(option92);
                    hide()
                }
            })

        });
    }
}
p10.init()


globalData.p11Data={
    table1: [
        {
            name: '运维期系统',
            num: '200',
        },
        {
            name: '项目期系统',
            num: '22',
        },
        {
            name: '新核心系统',
            num: '160',
        }
    ],
    table2: [
        {
            name: '1-建议性缺陷',
            num: '200',
        },
        {
            name: '2-文字错误',
            num: '22',
        },
        {
            name: '3-轻微缺陷',
            num: '160',
        },
        {
            name: '4-一般性缺陷',
            num: '160',
        },
        {
            name: '5-严重缺陷',
            num: '160',
        },
    ],
}
var p11={
    init:function () {
        this.initTable1(globalData.p11Data.table1,$('#p10table1'))
        this.initTable2(globalData.p11Data.table2,$('#p10table2'))
        this.initChart()
        renderTplList($('#module_11_1'),'module_11_1','arrow_class')
    },
    initTable1: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>' + item.name + '</td>\n' +
                ' <td>' + item.num + '</td>\n' +
                '  </tr>'
        }
        target.html(html)
    },
    initTable2: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>' + item.name + '</td>\n' +
                ' <td>' + item.num + '</td>\n' +
                '  </tr>'
        }
        target.html(html)
    },
    initChart:function () {
        var myChart101 = echarts.init(document.getElementById('page10_chart1'));

        // 指定图表的配置项和数据
        var option101 = {
            grid: {
                bottom: 40,
                right: 100
            },
            title: {
                text: '本月缺陷等级分布情况',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['1-建议性缺陷', '2-文字错误', '3-轻微缺陷', '4-一般性缺陷', '5-严重缺陷'],
                right: 10,
                top: 'middle',
                orient: 'vertical',
                itemWidth: 10,
                itemHeight: 10,
                icon: 'rect'
            },
            series: [
                {
                    name: '本月缺陷等级分布情况',
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    label: {
                        formatter: '{d}%',
                    },
                    data: [
                        {
                            name: '1-建议性缺陷',
                            value: 20
                        },
                        {
                            name: '2-文字错误',
                            value: 100
                        },
                        {
                            name: '3-轻微缺陷',
                            value: 100
                        },
                        {
                            name: '4-一般性缺陷',
                            value: 100
                        },
                        {
                            name: '5-严重缺陷',
                            value: 100
                        }
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    itemStyle: {
                        color: function (params) {
                            //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                            var colorList = ['#22A0D7', '#3BD1D5', '#FF7D7D', '#954ECA', '#FFCB25'];
                            return colorList[params.dataIndex]
                        }
                    }
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart101.setOption(option101);
        myChart101.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page10_chart1',
                value: info.data.value,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option101.series[info.seriesIndex].data[info.dataIndex].value = val;
                    myChart101.setOption(option101);
                    hide()
                }
            })

        });
    }
}
p11.init()

globalData.p12Data={
    table: [
        {
            name: '2020年1月',
            num: '200',
        },
        {
            name: '2020年2月',
            num: '220',
        },
        {
            name: '2020年3月',
            num: '167',
        },
        {
            name: '2020年4月',
            num: '180',
        },
        {
            name: '2020年5月',
            num: '160',
        },
        {
            name: '2020年6月',
            num: '120',
        },
        {
            name: '2020年7月',
            num: '180',
        },
        {
            name: '2020年8月',
            num: '150',
        },
        {
            name: '2020年9月',
            num: '120',
        },
    ],
}
var p12={
    init:function () {
        this.initTable(globalData.p12Data.table,$('#p11table'))
        renderTplList($('#module_12_1'),'module_12_1','arrow_class')
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>' + item.name + '</td>\n' +
                ' <td>' + item.num + '</td>\n' +
                '  </tr>'
        }
        target.html(html)
    },
    initChart:function () {
        var myChart101 = echarts.init(document.getElementById('page10_chart1'));

        // 指定图表的配置项和数据
        var option101 = {
            grid: {
                bottom: 40,
                right: 100
            },
            title: {
                text: '本月缺陷等级分布情况',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            legend: {
                data: ['1-建议性缺陷', '2-文字错误', '3-轻微缺陷', '4-一般性缺陷', '5-严重缺陷'],
                right: 10,
                top: 'middle',
                orient: 'vertical',
                itemWidth: 10,
                itemHeight: 10,
                icon: 'rect'
            },
            series: [
                {
                    name: '本月缺陷等级分布情况',
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    label: {
                        formatter: '{d}%',
                    },
                    data: [
                        {
                            name: '1-建议性缺陷',
                            value: 20
                        },
                        {
                            name: '2-文字错误',
                            value: 100
                        },
                        {
                            name: '3-轻微缺陷',
                            value: 100
                        },
                        {
                            name: '4-一般性缺陷',
                            value: 100
                        },
                        {
                            name: '5-严重缺陷',
                            value: 100
                        }
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    itemStyle: {
                        color: function (params) {
                            //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                            var colorList = ['#22A0D7', '#3BD1D5', '#FF7D7D', '#954ECA', '#FFCB25'];
                            return colorList[params.dataIndex]
                        }
                    }
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart101.setOption(option101);
        myChart101.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page10_chart1',
                value: info.data.value,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option101.series[info.seriesIndex].data[info.dataIndex].value = val;
                    myChart101.setOption(option101);
                    hide()
                }
            })

        });
    }
}
p12.init()


var p14={
    init:function () {
        this.initChart()
        renderTplList($('#module_14_1'),'module_14_1','arrow_class')
    },
    initChart:function () {
        var myChart1311 = echarts.init(document.getElementById('page131_chart1'));

        // 指定图表的配置项和数据
        var option1311 = {
            grid: {
                bottom: 120,
            },
            title: {
                text: '本月测试缺陷率统计（敏捷类系统）',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            xAxis: {
                type: 'category',
                data: ['车险理赔系统2005版', '风险管理信息系统2017版', '集中收款平台2013版', '人身系统2003版', '应收应付系统2003版'],
                axisLabel: {
                    interval: 0,
                    rotate: 40,
                    fontSize: 10
                }
            },
            yAxis: {
                axisLabel: {
                    formatter: function (val) {
                        return val + '%';
                    }
                },
            },
            series: [
                {
                    name: '本月测试缺陷率统计（敏捷类系统）',
                    type: 'bar',
                    barWidth: 30,
                    data: [3.3, 4.1, 4.3, 4.6, 4.7],
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10,
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}%',
                        color: '#000'
                    },
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart1311.setOption(option1311);
        myChart1311.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page131_chart1',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option1311.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart1311.setOption(option1311);
                    hide()
                }
            })

        });
    },
}
p14.init()

var p15={
    init:function () {
        this.initChart()
        renderTplList($('#module_15_1'),'module_15_1','arrow_class')
    },
    initChart:function () {
        var myChart131 = echarts.init(document.getElementById('page13_chart1'));

        // 指定图表的配置项和数据
        var option131 = {
            grid: {
                bottom: 120,
            },
            title: {
                text: '本月测试缺陷率统计（运维期系统非敏捷）',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            xAxis: {
                type: 'category',
                data: ['车辆标的库系统2017版', '非车人身双代系统2009版', '风险管理信息系统2017版', '集中收款平台2013版', '人身系统2003版', '应收应付系统2003版', '呼叫中心服务系统2003版', '收费系统2003版', '销售还礼系统2003版', '续保管理系统2003版', '理赔医保通系统2003版', '车险理赔系统2005版'],
                axisLabel: {
                    interval: 0,
                    rotate: 40,
                    fontSize: 10
                }
            },
            yAxis: {
                axisLabel: {
                    formatter: function (val) {
                        return val + '%';
                    }
                },
            },
            series: [
                {
                    name: '本月测试缺陷率统计（运维期系统非敏捷）',
                    type: 'bar',
                    barWidth: 20,
                    data: [0, 0, 0.2, 0.3, 0.4, 0.5, 1.3, 1.4, 4.3, 4.6, 4.7, 8.2],
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10,
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}%',
                        color: '#000'
                    },
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart131.setOption(option131);
        myChart131.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page13_chart1',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option131.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart131.setOption(option131);
                    hide()
                }
            })

        });
    },
}
p15.init()


globalData.p17Data={
    tpl1Data: {
        list: [{
            name: '数据',
            list: [
                {
                    name: 'year',
                    value: 5,
                    mark: '年份'
                },
                {
                    name: 'defectNum',
                    value: 5,
                    mark: '缺陷数'
                },
                {
                    name: 'testNum',
                    value: 5,
                    mark: '测试用例数'
                },
                {
                    name: 'rate',
                    value: 5,
                    mark: '平均缺陷率'
                },
            ]
        }],
        data: {
            year: {name:'年份',value:'2020'},
            defectNum:{name:'缺陷数',value:1988},
            testNum:{name:'测试任务数',value:83512},
            rate:{name:'平均缺陷率',value:'2.38'}
        },
        tpl: [
            '{{year}}年敏捷类系统累计发现缺陷{{defectNum}}个， 设计测试用例{{testNum}}个，平均测试缺陷率{{rate}}%，开发质量(注2)一般。',
            '其中，财产险理赔系统2018版及筋斗云-开放平台缺陷率较高，请关注开发质量，降低缺陷率。',
        ]
    },
}
var p17={
    init:function () {
        this.initChart()
        renderTplList($('#module_17_1'),'module_17_1','arrow_class')
    },
    initChart:function () {
        var myChart151 = echarts.init(document.getElementById('page15_chart1'));

        // 指定图表的配置项和数据
        var option151 = {
            grid: {
                bottom: 120,
            },
            title: {
                text: '全年测试缺陷率统计（敏捷类系统）',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            xAxis: {
                type: 'category',
                data: ['车辆标的库系统2017版', '收费系统2003版', '续保管理系统2003版', '理赔医保通系统2003版', '车险理赔系统2005版'],
                axisLabel: {
                    interval: 0,
                    rotate: 40,
                    fontSize: 10
                }
            },
            yAxis: {
                axisLabel: {
                    formatter: function (val) {
                        return val + '%';
                    }
                },
            },
            series: [
                {
                    name: '全年测试缺陷率统计（敏捷类系统）',
                    type: 'bar',
                    barWidth: 30,
                    data: [3.4, 4.5, 4.6, 4.7, 8.2],
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10,
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}%',
                        color: '#000'
                    },
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart151.setOption(option151);
        myChart151.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page15_chart1',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option151.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart151.setOption(option151);
                    hide()
                }
            })

        });
    },
}
p17.init()

var p18={
    init:function () {
        this.initChart()
        renderTplList($('#module_18_1'),'module_18_1','arrow_class')
        renderTplList($('#module_18_2'),'module_18_2',)
    },
    initChart:function () {
        var myChart1521 = echarts.init(document.getElementById('page152_chart1'));

        // 指定图表的配置项和数据
        var option1521 = {
            grid: {
                bottom: 120,
            },
            title: {
                text: '全年测试缺陷率统计（运维期系统（非敏捷））',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            xAxis: {
                type: 'category',
                data: ['车辆标的库系统2017版', '非车人身双代系统2009版', '风险管理信息系统2017版', '集中收款平台2013版', '人身系统2003版', '应收应付系统2003版', '呼叫中心服务系统2003版', '收费系统2003版', '销售还礼系统2003版', '续保管理系统2003版', '理赔医保通系统2003版', '车险理赔系统2005版'],
                axisLabel: {
                    interval: 0,
                    rotate: 40,
                    fontSize: 10
                }
            },
            yAxis: {
                axisLabel: {
                    formatter: function (val) {
                        return val + '%';
                    }
                },
            },
            series: [
                {
                    name: '全年测试缺陷率统计（运维期系统（非敏捷））',
                    type: 'bar',
                    barWidth: 20,
                    data: [0, 0, 0.2, 0.5, 0.4, 0.5, 1.5, 1.4, 4.3, 4.6, 4.7, 8.2],
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10,
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}%',
                        color: '#000'
                    },
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart1521.setOption(option1521);
        myChart1521.on('dblclick', function (params) {
            var info = getChartInfo(params)
            popper.show({
                el: 'page152_chart1',
                value: info.data,
                pos: info.pos,
                title: info.seriesName + '-' + info.name,
                success: function (val, hide) {
                    option1521.series[info.seriesIndex].data[info.dataIndex] = val;
                    myChart1521.setOption(option1521);
                    hide()
                }
            })

        });
    },
}
p18.init()

/*page1*/
var p19 = {
    init: function () {
        renderTplList($('#module_19_1'), 'module_19_1')
        renderTplList($('#module_19_2'), 'module_19_2')
    }
}
p19.init()