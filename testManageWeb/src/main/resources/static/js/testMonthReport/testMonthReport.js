var monthTime = _getQueryString('monthTime')
var hidePP=true
var globalData = {
    data: [],
    module: {},
    dataMap: {},
} //全局数据
/*data 变量列表 dataMap 对应的*/
$(function () {
    getCurrentUserRole()
    queryData()
})

function queryData() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/getAllMonthReportData",
        method: "post",
        data: {time: monthTime},
        dataType: "json",
        success: function (res) {
            $("#loading").css('display', 'none');
            if (res.status === '1') {
                globalData = res
                globalData.dataMap = getDataMap(res.data)
                initPage()
            }
        }
    });
}

function initPage() {
    setValue(globalData.dataMap||{})
    p1.init()
    p3.init()
    hideProject_period(hidePP)
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
    var _dataMap = {}
    for (var i = 0; i < list.length; i++) {
        var _list = list[i].list || []
        if(list[i].code==='listNew'&&_list.length>0){//项目期系统相关是否展示
            hidePP=false
        }
        for (var j = 0; j < _list.length; j++) {
            var item = _list[j]
            _dataMap[item.code] = {
                name: item.name,
                value: item.value,
                typeCode: list[i].code,
                typeName: list[i].name
            }
        }
    }
    return _dataMap
}

/*修改变量的值后 把所有的相同的值修改掉*/
function changeAllSameTpl(dataAttr, key, val) {
    var list = $('span[data-key=' + key + ']', $('[data-data="' + dataAttr + '"]'));
    for (var i = 0; i < list.length; i++) {
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
function isMoreTwo(num) {
    return num >= 2
}

/*渲染tpl list*/
function renderTplList(target, attrs, className, el) {
    var _el = el ? el : 'p'
    var data_data = 'dataMap'
    var data_list = 'data'
    var data_tpl = 'module.' + attrs
    var data = _getObject('globalData', data_data)
    var tpl = _getObject('globalData', data_tpl) || []
    var html = ''
    var _className = className ? className : ''
    for (var i = 0; i < tpl.length; i++) {
        var _tpl = renderTpl(tpl[i].content, data)
        html += '<' + _el + ' class="edit_tpl ' + _className + '" data-num="' + tpl[i].num + '" data-id="' + tpl[i].id + '" data-data="' + data_data + '" data-list="' + data_list + '" data-tpl="' + data_tpl + '.' + i + '" data-index="' + i + '">' + _tpl + '</' + _el + '>'
    }
    target.html(html)
    target.attr('data-data', data_data)
    target.attr('data-list', data_list)
    target.attr('data-tpl', data_tpl)
    target.attr('data-className', className)
    target.attr('data-el', el)
}

/*删除添加后重新渲染*/
function renderTplListUpdate(dataTpl) {
    var attr = "[data-tpl='" + dataTpl + "']"
    $(attr).each(function (index, target) {
        var className = $(target).attr('data-className')
        var el = $(target).attr('data-el')
        renderTplList($(target), dataTpl.split('.')[1], className, el)
    })

}

function formatType(type) {
    switch (type) {
        case 1:
            return '敏捷类系统'
        case 2:
            return '运维期（非敏捷）'
        case 3:
            return '项目期（非敏捷）'
        default:
            return '数据异常'
    }
}

function blankTable(col) {
    return '<tr><td colspan="' + col + '"><p style="padding:10px 20px;text-align: center;color: #999;font-size: 12px;">暂无相关数据</p></td></tr>'
}

/*
* time 2012-02
* str '$1/$2'
* */
function formatTime(time, str) {
    if (!time) return 'null'
    var regex = /(\d{4})-(\d{1,2})/;
    return time.replace(regex, str)
}

function formatLevel(type) {
    switch (type) {
        case 1:
            return '1-建议性缺陷'
        case 2:
            return '2-文字错误'
        case 3:
            return '3-轻微缺陷'
        case 4:
            return '4-一般性缺陷'
        case 5:
            return '5-严重缺陷'
        default:
            return '数据异常'
    }
}

/*
* list []
* yAxis：[name1,name]
* xAxis 名称
* */
function formatChartData(list, xAxis, yAxis) {
    var data = {}
    data[xAxis] = []
    for (var i = 0; i < yAxis.length; i++) {
        data[yAxis[i]] = []
    }
    for (var j = 0; j < list.length; j++) {
        var item = list[j]
        if (xAxis === 'yearMonth') {
            data[xAxis].push(formatTime(item[xAxis], '$2月'))
        } else {
            data[xAxis].push(item[xAxis])
        }

        for (var i = 0; i < yAxis.length; i++) {
            data[yAxis[i]].push(item[yAxis[i]])
        }
    }
    return data
}

/*
type 类型(1 业务 2 缺陷)
percentage 占比
list []
name 字段名称
*/
function formatPerData(list, name) {
    var data = [{name: '业务需求', value: 0}, {name: '缺陷修复', value: 0}]
    for (var i = 0; i < list.length; i++) {
        var item = list[i]
        switch (item.type) {
            case 1:
                data[0].value = item.taskNumber
                break;
            case 2:
                data[1].value = item.taskNumber
                break
            default:
                break
        }
    }
    return data
}


//0管理员 1 月报负责人
function getCurrentUserRole() {
    $.ajax({
        url: "/testManage/monthlyReport/getCurrentUserRole",
        method: "post",
        dataType: "json",
        success: function (res) {
            if (res.status === '1') {
                if (res.role === '0' || res.role === '1') {
                    initEdit()
                }
            }

        }
    });
}

/*table 展示link */

//table 展示
function showExcelTable(_this) {
    var url = $(_this).attr('data-url')
    var type = $(_this).attr('data-type')
    queryTableData(url,type)
    $('#excel_table_type1').css('display','none')
    $('#excel_table_type2').css('display','none')
    $('#excel_table_type3').css('display','none')
    $('#watch_excel').modal('show')
}

function queryTableData(url,type) {
    if(!url) return
    var qData = {
        getRemainDefect: {yearMonth: monthTime},
        getThisMonthlySystemData: {yearMonth: monthTime},
        getThisYearSystemData: {yearMonth: monthTime},
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/" + url,
        method: "post",
        dataType: "json",
        data: qData[url] || {},
        success: function (res) {
            if (res.status === '1') {
                $('#excel_table_type'+type).css('display','block')
                if(type==="1"){  //本月
                    renderExcelTableMonth(url,[res.agileDataList,res.devDataList,res.proDataList])
                }else if(type==="2"){ //全年
                    $('#excel_table_type1').css('display','block')//2在1中
                    renderExcelTableMonth(url,[res.agileDataList,res.devDataList,res.proDataList])
                }else if(type==="3"){//总结
                    renderExcelTable(url, res.data)
                }
            }

        },
        complete: function () {
            $("#loading").css('display', 'none');
        }
    });
}
/*遗留缺陷清单 漏检经验总结*/
function renderExcelTable(url, data) {

    var columns = {
        getRemainDefect: [//缺陷清单
            {
                field: "id",
                title: "缺陷ID",
            },
            {
                field: "systemName",
                title: "项目",
            },
            {
                field: "defectSummary",
                title: "摘要",
            },
            {
                field: "requirementCode",
                title: "需求编号",
            },
            {
                field: "defectStatus",
                title: "状态",
            },
            {
                field: "assignUserId",
                title: "分配给",
            },
            {
                field: "developUserId",
                title: "开发人",
            },
            {
                field: "windowName",
                title: "检测于版本",
            },
            {
                field: "defectSource",
                title: "缺陷发现阶段",
            },
            {
                field: "severityLevel",
                title: "缺陷等级",
            },
            {
                field: "createBy",
                title: "创建人",
            }
        ],
        getAllUndetectedSummary: [//漏检经验总结
            {
                field: "itmsCode",
                title: "Mantis/ITSM编号",
            },
            {
                field: "systemName",
                title: "项目",
            },
            {
                field: "seriousLevel",
                title: "严重性",
            },
            {
                field: "reportDate",
                title: "报告日期",
            },
            {
                field: "summary",
                title: "摘要",
            },
            {
                field: "requirementCode",
                title: "需求编号",
            },
            {
                field: "reasonAnalysis",
                title: "原因分析",
            },
            {
                field: "missedReason",
                title: "漏检原因",
            },
            {
                field: "experienceSummary",
                title: "测试经验总结",
            }
        ],
    }
    var _colums = columns[url]
    $("#excel_table").bootstrapTable('destroy');
    $("#excel_table").bootstrapTable({
        data: data,
        columns: _colums,
        onLoadSuccess: function () {
        },
        onLoadError: function () {
        },
        onClickCell: function (field, value, row, $element) {
            /*$element.attr('contenteditable', true);
            $element.blur(function () {
                let index = $element.parent().data('index');
                let tdValue = $element.html();
                saveData('#excel_table',index, field, tdValue);
            })*/
        }
    });
}
function renderExcelTableMonth(url, list) {
    var title={
        getThisMonthlySystemData:[
            [ //title
                {
                    "title": "本月缺陷率统计--敏捷类系统",
                    "halign": "center",
                    "align": "center",
                    "colspan": 11
                }
            ],
            [
                {
                    "title": "本月缺陷率统计--运维期系统(非敏捷)",
                    "halign": "center",
                    "align": "center",
                    "colspan": 11
                }
            ],
            [
                {
                    "title": "本月缺陷率统计--项目期系统(非敏捷)",
                    "halign": "center",
                    "align": "center",
                    "colspan": 11
                }
            ],
        ],
        getThisYearSystemData:[
            [ //title
                {
                    "title": "全年各项目测试数量和质量统计(累计目前)--敏捷类系统",
                    "halign": "center",
                    "align": "center",
                    "colspan": 11
                }
            ],
            [
                {
                    "title": "全年各项目测试数量和质量统计(累计目前)--运维期系统（非敏捷）",
                    "halign": "center",
                    "align": "center",
                    "colspan": 11
                }
            ],
            [
                {
                    "title": "全年各项目测试数量和质量统计(累计目前)--项目期系统（非敏捷）",
                    "halign": "center",
                    "align": "center",
                    "colspan": 11
                }
            ],
        ],
    }
    var columns = {
        getThisMonthlySystemData: [
            [//缺陷清单
                {
                    field: "systemName",
                    title: "系统名称",
                },
                {
                    field: "taskNumber",
                    title: "测试任务数",
                },
                {
                    field: "defectNumber",
                    title: "测试发现缺陷数",
                },
                {
                    field: "repairedDefectNumber",
                    title: "修复缺陷数",
                },
                {
                    field: "unrepairedDefectNumber",
                    title: "遗留缺陷数",
                },
                {
                    field: "designCaseNumber",
                    title: "设计用例数",
                },
                {
                    field: "defectPercent",
                    title: "缺陷率",
                    formatter: function (value, row, index) {
                        return value + "%";
                    }
                },
                {
                    field: "totalRepairRound",
                    title: "累计修复轮次",
                },
                {
                    field: "avgRepairRound",
                    title: "平均修复轮次",
                },
                {
                    field: "lastmonthUndefectedNumber",
                    title: "本月漏检缺陷数",
                },
                {
                    field: "lastmonthUndefectedBelonger",
                    title: "漏检缺陷归属",
                }
            ]
        ],
        getThisYearSystemData: [
            [//缺陷清单
                {
                    field: "systemName",
                    title: "系统名称",
                },
                {
                    field: "taskNumber",
                    title: "测试任务数",
                },
                {
                    field: "defectNumber",
                    title: "测试发现缺陷数",
                },
                {
                    field: "repairedDefectNumber",
                    title: "修复缺陷数",
                },
                {
                    field: "unrepairedDefectNumber",
                    title: "遗留缺陷数",
                },
                {
                    field: "designCaseNumber",
                    title: "设计用例数",
                },
                {
                    field: "defectPercent",
                    title: "缺陷率",
                    formatter: function (value, row, index) {
                        return value + "%";
                    }
                },
                {
                    field: "undefectedNumber",
                    title: "漏检缺陷数",
                },
                {
                    field: "defectedRate",
                    title: "检出率",
                    formatter: function (value, row, index) {
                        return value + "%";
                    }
                },
                {
                    field: "totalRepairRound",
                    title: "累计修复轮次",
                },
                {
                    field: "avgRepairRound",
                    title: "平均累计修复轮次",
                }
            ]
        ],
    }
    var _colums = columns[url]
    for(var i=0;i<list.length;i++){
        var __colums=_deepClone(_colums)
        __colums.unshift(title[url][i])
        $("#excel_table_"+(i+1)).bootstrapTable('destroy');
        $("#excel_table_"+(i+1)).bootstrapTable({
            data: list[i],
            columns: __colums,
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            },
            onClickCell: function (field, value, row, $element) {
               /* $element.attr('contenteditable', true);
                $element.blur(function () {
                    let index = $element.parent().data('index');
                    let tdValue = $element.html();
                    saveData('#'+$element.parent().parent().parent().attr('id'),index, field, tdValue);
                })*/
            }
        });
    }

}

function saveData(id,index, field, value) {
    $(id).bootstrapTable('updateCell', {
        index: index,       //行索引
        field: field,       //列名
        value: value        //cell值
    })
}

//根据判断是否有权限是否初始化编辑功能
function initEdit() {
    /*模板可编辑*/
    tplEditModal.init()
    /*变量可编辑*/
//具体url等等通过获取父级属性信息 变量编辑
   /* var editP = new EditPopper({
        el: 'edit_var',
        success: function (target, val, hide) {
            var key = $(target).attr('data-key') //元素自身上
            var url = $(target).parent().attr('data-url') //父元素上
            var dataAttr = $(target).parent().attr('data-data') //data 的属性指向(父元素上)
            // todo 调用服务
            _setObject('globalData', dataAttr + '.' + key + '.value', val)
            changeAllSameTpl(dataAttr, key, val)
            hide()
        },
    })*/
//新增 删除
    var oper = new MenuRightPopper({
        success: function (_data, _hide) {
            _hide()
            var data = _getObject('globalData', _data.data) //获取对应data
            var list = _getObject('globalData', _data.list) //获取对应data
            var data_tpl = '新增模板'
            var tpl = {id: null, content: data_tpl} //获取对应data
            tplEditModal.show({data: data, list: list, tpl: tpl}, function (nTpl, hide) {
                var pData = {
                    date: monthTime,
                    module: _data.name.split('.')[1],
                    num: _data.num,
                    content: nTpl,
                }
                $("#loading").css('display', 'block');
                $.ajax({
                    url: "/testManage/monthlyReport/addModule",
                    method: "post",
                    dataType: "json",
                    data: pData,
                    success: function (res) {
                        if (res.status === '1') {
                            var list = _getObject('globalData', _data.name)
                            list.splice(_data.num, 0, res.data)
                            renderTplListUpdate(_data.name) //全局
                            // renderTplList($(_data.target), _data.name.split('.')[1]) //单个
                            hide()
                            layer.alert('新增成功!', {
                                icon: 1,
                            })
                        } else {
                            layer.alert('新增失败!', {
                                icon: 2,
                            })
                        }

                    },
                    complete: function () {
                        $("#loading").css('display', 'none');
                    }
                });
            })
        },
        delete: function (_data, _hide) {
            var pData = {
                date: monthTime,
                module: _data.name.split('.')[1],
                id: _data.id,
                num: _data.num
            }
            $("#loading").css('display', 'block');
            $.ajax({
                url: "/testManage/monthlyReport/deleteModule",
                method: "post",
                dataType: "json",
                data: pData,
                success: function (res) {
                    if (res.status === '1') {
                        var list = _getObject('globalData', _data.name)
                        list.splice(_data.index, 1)
                        renderTplListUpdate(_data.name) //全局
                        // renderTplList($(_data.target), _data.name.split('.')[1])
                        _hide()
                        layer.alert('删除成功!', {
                            icon: 1,
                        })
                    } else {
                        layer.alert('删除失败!', {
                            icon: 2,
                        })
                    }

                },
                complete: function () {
                    $("#loading").css('display', 'none');
                }
            });
        },
        cancel: function (data, hide) {
            hide()
        },
    })
}

/*隐藏项目期 - 非敏捷*/
function hideProject_period(hide) {
    $('[data-hide=project_period]').each(function (index,_this) {
        $(_this).css('display',hide?'none':'')
    })
    $('.hide_pp').eq(2).css('display',hide?'none':'')
}
/*年月对应的修改value*/
function setValue(data) {
    $('.set-value').each(function (index,_this) {
        var _val=$(_this).attr('data-value')
        var _value=data[_val]?data[_val].value:''
        $(_this).html(_value)
    })
}

var p1 = {
    init: function () {
        renderTplList($('#module_1_1'), 'module_1_1')
        renderTplList($('#module_1_2'), 'module_1_2')
        renderTplList($('#page20_department'), 'module_1_2')

    }
}
var p3 = {
    init: function () {
        renderTplList($('#module_3_1'), 'module_3_1')
        renderTplList($('#module_3_2'), 'module_3_2', 'font_spot hide_pp', 'li')
        renderTplList($('#module_3_3'), 'module_3_3', 'notes')
        this.initTable(globalData.pageData.page3_1, $('#p3table1_body'))
        this.initTable(globalData.pageData.page3_2, $('#p3table2_body'))
        this.initTable2(globalData.pageData.page3_3, $('#p3table3_body'))
        this.initTable2(globalData.pageData.page3_4, $('#p3table4_body'))
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var rate = data[i].defectPercent, name = data[i].systemName
            var cn = isMoreTwo(rate) ? 'red_tr' : '';
            html += '<tr class="' + cn + '">' +
                ' <td>' + name + '</td>' +
                ' <td>' + rate + '%</td>' +
                ' </tr>'
        }
        if (data.length === 0) html = blankTable(2)
        target.html(html)
    },
    initTable2: function (data, target) {
        var html = '',redList=data.red||[],greenList=data.green||[]
        for (var i = 0; i < greenList.length; i++) {
            var rate = greenList[i].defectPercent, name = greenList[i].systemName
            html += '<tr>' +
                ' <td>' + name + '</td>' +
                ' <td>' + rate + '%</td>' +
                ' </tr>'
        }
        for (var i = 0; i < redList.length; i++) {
            var rate = redList[i].defectPercent, name = redList[i].systemName
            html += '<tr class="red_tr">' +
                ' <td>' + name + '</td>' +
                ' <td>' + rate + '%</td>' +
                ' </tr>'
        }
        if (redList.length === 0&&greenList.length===0) html = blankTable(2)
        target.html(html)
    },
}
var p5 = {
    init: function () {
        var page5 = globalData.pageData.page5 || []
        this.initTable(page5, $('#p5table1'))
        this.initChart(formatChartData(page5, 'yearMonth', ['planWindowsNumber', 'tempWindowsNumber', 'countWindowsNumber']))
        renderTplList($('#module_5_1'), 'module_5_1', 'arrow_class mg_10')
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>' + formatTime(item.yearMonth, '$1年$2月') + '</td>\n' +
                ' <td>' + item.planWindowsNumber + '</td>\n' +
                ' <td>' + item.tempWindowsNumber + '</td>\n' +
                ' <td>' + item.countWindowsNumber + '</td>\n' +
                '  </tr>'
        }
        if (data.length === 0) html = blankTable(4)
        target.html(html)
    },
    initChart: function (data) {
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
            tooltip: {
                show:true
            },
            legend: {
                data: ['计划内版本数', '临时版本数', '月总版本数'],
                top: 'middle',
                orient: 'vertical',
                right: 10
            },
            xAxis: {
                type: 'category',
                data: data.yearMonth
            },
            yAxis: {},
            series: [
                {
                    name: '计划内版本数',
                    type: 'line',
                    data: data.planWindowsNumber,
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'circle',
                    symbolSize: 10
                },
                {
                    name: '临时版本数',
                    type: 'line',
                    data: data.tempWindowsNumber,
                    itemStyle: {
                        color: '#7FD4E9'
                    },
                    symbol: 'diamond',
                    symbolSize: 10
                },
                {
                    name: '月总版本数',
                    type: 'line',
                    data: data.countWindowsNumber,
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
var p6 = {
    init: function () {
        var page6 = globalData.pageData.page6
        var chartData = formatChartData(page6, 'yearMonth', ['tempAddTaskNumber', 'tempDelTaskNumber', 'versionChangeRate'])
        this.initChart1(chartData)
        this.initChart2(chartData)
        renderTplList($('#module_6_1'), 'module_6_1', 'arrow_class mg_10')
        renderTplList($('#module_6_2'), 'module_6_2', 'mark')
    },
    initChart1: function (data) {

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
            tooltip: {
                show:true
            },
            legend: {
                data: ['新增', '减少'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            xAxis: {
                type: 'category',
                data: data.yearMonth
            },
            yAxis: {},
            series: [
                {
                    name: '新增',
                    type: 'bar',
                    data: data.tempAddTaskNumber,
                    itemStyle: {
                        color: '#22A0D7'
                    }
                },
                {
                    name: '减少',
                    type: 'bar',
                    data: data.tempDelTaskNumber,
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
    initChart2: function (data) {
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
            tooltip: {
                show:true
            },
            legend: {
                data: ['各月份版本变更率趋势图'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            xAxis: {
                type: 'category',
                data: data.yearMonth
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
                    data: data.versionChangeRate,
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
var p7 = {
    init: function () {
        this.initTable(globalData.pageData.page7_1, $('#p71table1'))

        this.initChart(formatPerData(globalData.pageData.page7_2 || [], 'taskNumber'))
        renderTplList($('#module_7_1'), 'module_7_1', 'arrow_class')
        renderTplList($('#module_7_2'), 'module_7_2',)
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>' + item.systemName + '</td>\n' +
                ' <td>' + item.taskNumber + '</td>\n' +
                ' <td>' + item.designCaseNumber + '</td>\n' +
                ' <td>' + item.defectNumber + '</td>\n' +
                ' <td>' + item.defectPercent + '%</td>\n' +
                '  </tr>'
        }
        if (data.length === 0) html = blankTable(5)
        target.html(html)
    },
    initChart: function (data) {
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
            tooltip: {
                show:true
            },
            legend: {
                data: ['业务需求', '缺陷修复'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            series: [
                {
                    name: '类别',
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    label: {
                        formatter: '{c} \n {d}%',
                        position: 'inner',
                    },
                    data: data,
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
var p8 = {
    init: function () {
        var data = formatChartData(globalData.pageData.page8_1, 'yearMonth', ['totalTaskNumber', 'designCaseNumber', 'defectNumber'])
        this.initChart1(data)
        this.initChart2(formatPerData(globalData.pageData.page8_2 || [], 'taskNumber'))
        this.initChart3(data)
        this.initChart4(data)
        renderTplList($('#module_8_1'), 'module_8_1', 'arrow_class')
        renderTplList($('#module_8_2'), 'module_8_2',)
    },
    initChart1: function (data) {

        var myChart71 = echarts.init(document.getElementById('page7_chart1'));

        // 指定图表的配置项和数据
        var option71 = {
            grid: {
                bottom: 40,
                left:60
            },
            title: {
                text: '任务数',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            tooltip: {
                show:true
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
                data: data.yearMonth
            },
            yAxis: {},
            series: [
                {
                    name: '任务数',
                    type: 'bar',
                    data: data.totalTaskNumber,
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
    initChart2: function (data) {
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
            tooltip: {
                show:true
            },
            legend: {
                data: ['业务需求', '缺陷修复'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            series: [
                {
                    name: '类别',
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    label: {
                        formatter: '{c} \n {d}%',
                        position: 'inner',
                    },
                    data: data,
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
    initChart3: function (data) {
        var myChart73 = echarts.init(document.getElementById('page7_chart3'));
        // 指定图表的配置项和数据
        var option73 = {
            grid: {
                bottom: 40,
                left:60
            },
            title: {
                text: '用例数',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            tooltip: {
                show:true
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
                data: data.yearMonth
            },
            yAxis: {},
            series: [
                {
                    name: '用例数',
                    type: 'bar',
                    data: data.designCaseNumber,
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
    initChart4: function (data) {
        var myChart74 = echarts.init(document.getElementById('page7_chart4'));

        // 指定图表的配置项和数据
        var option74 = {
            grid: {
                bottom: 40,
                left:60
            },
            title: {
                text: '缺陷数',
                left: 'center',
                top: 5,
                textStyle: {
                    fontWeight: 400
                }
            },
            tooltip: {
                show:true
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
                data: data.yearMonth
            },
            yAxis: {},
            series: [
                {
                    name: '缺陷数',
                    type: 'bar',
                    data: data.defectNumber,
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
var p9 = {
    init: function () {
        this.initTable(globalData.pageData.page9_1, $('#p72table1'))
        this.initChart(formatPerData(globalData.pageData.page9_2 || [], 'taskNumber'))
        renderTplList($('#module_9_1'), 'module_9_1', 'arrow_class')
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>' + item.systemName + '</td>\n' +
                ' <td>' + item.taskNumber + '</td>\n' +
                ' <td>' + item.designCaseNumber + '</td>\n' +
                ' <td>' + item.defectNumber + '</td>\n' +
                ' <td>' + item.defectPercent + '%</td>\n' +
                '  </tr>'
        }

        if (data.length === 0) html = blankTable(5)
        target.html(html)
    },
    initChart: function (data) {
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
            tooltip: {
                show:true
            },
            legend: {
                data: ['业务需求', '缺陷修复'],
                right: 10,
                top: 'middle',
                orient: 'vertical'
            },
            series: [
                {
                    name: '类别',
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    label: {
                        formatter: '{c} \n {d}%',
                        position: 'inner',
                    },
                    data: data,
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
var p10 = {
    init: function () {
        this.initChart1(formatChartData(globalData.pageData.page10_1, 'yearMonth', ['avgRepairRound']))
        this.initChart2(formatChartData(globalData.pageData.page10_2, 'systemName', ['avgRepairRound']))
        renderTplList($('#module_10_1'), 'module_10_1', 'arrow_class')
    },
    initChart1: function (data) {
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
            tooltip: {
                show:true
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
                data: data.yearMonth,
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
                    data: data.avgRepairRound,
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
    initChart2: function (data) {
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
            tooltip: {
                show:true
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
                data: data.systemName,
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
                    data: data.avgRepairRound,
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
var p11 = {
    init: function () {
        this.initTable1(globalData.pageData.page11_1, $('#p10table1'))
        this.initTable2(globalData.pageData.page11_2, $('#p10table2'))
        var page11_2 = globalData.pageData.page11_2
        var yData = [
            {
                name: '1-建议性缺陷',
                value: 0
            },
            {
                name: '2-文字错误',
                value: 0
            },
            {
                name: '3-轻微缺陷',
                value: 0
            },
            {
                name: '4-一般性缺陷',
                value: 0
            },
            {
                name: '5-严重缺陷',
                value: 0
            }
        ]
        for (var i = 0; i < page11_2.length; i++) {
            var item = page11_2[i], level = item.level
            if (level > 0) yData[level - 1].value = item.defectNumber
        }
        this.initChart(yData)
        renderTplList($('#module_11_1'), 'module_11_1', 'arrow_class')
    },
    initTable1: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td style="text-align: left;padding-left: 12%">' + formatType(item.systemType) + '</td>\n' +
                ' <td>' + item.defectNumber + '</td>\n' +
                '  </tr>'
        }
        target.html(html)
    },
    initTable2: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            if (item.level === 0) continue
            html += '<tr >\n' +
                ' <td style="text-align: left;padding-left: 15%">' + formatLevel(item.level) + '</td>\n' +
                ' <td>' + item.defectNumber + '</td>\n' +
                '  </tr>'
        }
        target.html(html)
    },
    initChart: function (yData) {
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
            tooltip: {
                show:true
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
                    data: yData,
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
var p12 = {
    init: function () {
        this.initTable(globalData.pageData.page12, $('#p11table'))
        renderTplList($('#module_12_1'), 'module_12_1', 'arrow_class')
    },
    initTable: function (data, target) {
        var html = ''
        for (var i = 0; i < data.length; i++) {
            var item = data[i]
            html += '<tr >\n' +
                ' <td>' + formatTime(item.yearMonth, '$1年$2月') + '</td>\n' +
                ' <td>' + item.undetectedNumber + '</td>\n' +
                '  </tr>'
        }

        if (data.length === 0) html = blankTable(2)
        target.html(html)
    },
    initChart: function () {
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
            tooltip: {
                show:true
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
var p14 = {
    init: function () {
        this.initChart(formatChartData(globalData.pageData.page14, 'systemName', ['defectPercent']))
        renderTplList($('#module_14_1'), 'module_14_1', 'arrow_class')
    },
    initChart: function (data) {
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
            tooltip: {
                show:true
            },
            xAxis: {
                type: 'category',
                data: data.systemName,
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
                    data: data.defectPercent,
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10,
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}%',
                        color: '#000',
                        rotate:90,
                        offset:[6,-15],
                        fontSize:10
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
var p15 = {
    init: function () {
        this.initChart(formatChartData(globalData.pageData.page15, 'systemName', ['defectPercent']))
        renderTplList($('#module_15_1'), 'module_15_1', 'arrow_class')
    },
    initChart: function (data) {
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
            tooltip: {
                show:true
            },
            xAxis: {
                type: 'category',
                data: data.systemName,
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
                    data: data.defectPercent,
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10,
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}%',
                        color: '#000',
                        rotate:90,
                        offset:[6,-15],
                        fontSize:10
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
var p17 = {
    init: function () {
        this.initChart(formatChartData(globalData.pageData.page17, 'systemName', ['defectPercent']))
        renderTplList($('#module_17_1'), 'module_17_1', 'arrow_class')
    },
    initChart: function (data) {
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
            tooltip: {
                show:true
            },
            xAxis: {
                type: 'category',
                data: data.systemName,
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
                    data: data.defectPercent,
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10,
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}%',
                        color: '#000',
                        rotate:90,
                        offset:[6,-15],
                        fontSize:10
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
var p18 = {
    init: function () {
        this.initChart(formatChartData(globalData.pageData.page18, 'systemName', ['defectPercent']))
        renderTplList($('#module_18_1'), 'module_18_1', 'arrow_class')
        renderTplList($('#module_18_2'), 'module_18_2',)
    },
    initChart: function (data) {
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
            tooltip: {
                show:true
            },
            xAxis: {
                type: 'category',
                data: data.systemName,
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
                    data: data.defectPercent,
                    itemStyle: {
                        color: '#22A0D7'
                    },
                    symbol: 'diamond',
                    symbolSize: 10,
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{c}%',
                        color: '#000',
                        rotate:90,
                        offset:[6,-15],
                        fontSize:10
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
var p19 = {
    init: function () {
        renderTplList($('#module_19_1'), 'module_19_1')
        renderTplList($('#module_19_2'), 'module_19_2')

    }
}