var curDate = (new Date()).getTime();
var reg = /^([0]|[1-9][0-9]*)$/

var tblReportMonthlyBaseList = []
var tblReportMonthlySystemDataList = []
var changeType = -1  //-1 月报负责人 ;index 项目负责人 数据index /*审核 确认 发起评审都在实时*/
var canSave = false //是否可以修改变量
var canSure = false //是否可以确认审核
var canChangePeople = false //是否可以修改负责人
var canInitiateReview = false //是否可以发起评审
var canSaveHistory = false //是否可以保存历史
var isHistory = false //是否是历史
var updateStatus = false //是否可以操作
var canConfigSystem = false
var tempTime = ''
$(function () {
    downOrUpButton()//搜索 收缩
    getCurrentUserRole()
    setTempTime()
    //所有的Input标签，在输入值后出现清空的按钮
    $('input[type="text"]').parent().css("position", "relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange", function () {
        if ($(this).val() != "") {
            $(this).parent().children(".btn_clear").css("display", "block");
        } else {
            $(this).parent().children(".btn_clear").css("display", "none");
        }
    });


    laydate.render({
        elem: '#startDate'
        , type: 'month'
        , max: curDate
    });
    laydate.render({
        elem: '#reportDate'
        , max: curDate
    });

});

/*25及以前取上个月 26及以后取本月*/
function setTempTime() {
    var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth();
    var day = myDate.getDate();
    if (day >= 26) {
        month += 1
    }
    if (month <= 0) {
        month = 12;
        year -= 1
    }
    $('#startDate').val(year + '-' + month)
}

function getCurrentUserRole() {
    $.ajax({
        url: "/testManage/monthlyReport/getCurrentUserRole",
        method: "post",
        dataType: "json",
        success: function (res) {
            if (res.status === '1') {
                if (res.role === '0') {
                    canChangePeople = true
                    canSave = true
                    canSure = true
                    canSaveHistory = true
                    canInitiateReview = true
                    canConfigSystem = true
                } else if (res.role === '1') {
                    canSave = true
                    canSure = true
                    canSaveHistory = true
                    canInitiateReview = true
                    canConfigSystem = true
                } else if (res.role === '2') {
                    canSure = true
                }
                initPageBtn()//根据权限初始化一些按钮的隐/显
            }

        }
    });
}

function initPageBtn() {
    $('#configSystemBtn').css('display', canConfigSystem ? 'inline-block' : 'none') //配置系统
    $('#saveHis').css('display', (canSaveHistory && !isHistory && updateStatus) ? 'block' : 'none')
    $('#initiateReviewBtn').css('display', (canInitiateReview && !isHistory && updateStatus) ? 'inline-block' : 'none')
    $('#missExperienceBtn').css('display', (canInitiateReview && updateStatus) ? 'inline-block' : 'none')
}

function clearSearch() {
    $("#startDate").val('');
    $("#endDate").val('');
    $("#systemId").selectpicker('val', '');
}

function queryHisByTime() {
    var time = $("#startDate").val();
    if (time == "") {
        layer.alert("时间为空", {icon: 0});
        return;
    }
    tempTime = time
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/queryHisByTime",
        method: "post",
        data: {time: time},
        dataType: "json",
        success: function (data) {
            updateStatus = data.updateStatus
            $("#loading").css('display', 'none');
            tblReportMonthlyBaseList = data.tblReportMonthlyBaseList
            tblReportMonthlySystemDataList = data.tblReportMonthlySystemDataList
            isHistory = true
            initPageBtn()
            createDevVersionTable(data.tblReportMonthlyBaseList);
            createDevSystemTable(data.tblReportMonthlySystemDataList);

        }
    });
}

function queryData(time) {
    var time = time || $("#startDate").val();
    if (time == "" || time === undefined) {
        layer.alert("时间为空", {icon: 0});
        return;
    }
    tempTime = time
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/queryReport",
        method: "post",
        data: {time: time},
        dataType: "json",
        success: function (data) {
            updateStatus = data.updateStatus
            $("#loading").css('display', 'none');
            isHistory = false
            tblReportMonthlyBaseList = data.tblReportMonthlyBaseList
            tblReportMonthlySystemDataList = data.tblReportMonthlySystemDataList
            initPageBtn()
            createDevVersionTable(data.tblReportMonthlyBaseList);
            createDevSystemTable(data.tblReportMonthlySystemDataList);

        }
    });
}

function createDevVersionTable(data) {
    $("#devVertionTable").bootstrapTable('destroy');
    $("#devVertionTable").bootstrapTable({
        queryParamsType: "",
        data: data,
        columns: [
            {
                field: "yearMonth",
                title: "时间",
                align: 'center',
                class: "stepOrder",
                width: '100px',
                formatter: function (value, row, index) {
                    return '<span style="white-space: nowrap">' + value.split('-')[0] + "年" + value.split('-')[1] + "月" + '</span>';
                }
            }, {
                field: "planWindowsNumber",
                title: "计划内版本次数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
                formatter: function (value, row, index) {
                    return '<div class="def_tableDiv2"><input ' + (canSave && !isHistory && updateStatus ? "" : "disabled") + ' type="text" class="form-control"  id="planWindowsNumber" value="' + value + '"/></div>';
                }
            }, {
                field: "tempWindowsNumber",
                title: "临时版本次数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
                formatter: function (value, row, index) {
                    return '<div class="def_tableDiv2"><input ' + (canSave && !isHistory && updateStatus ? "" : "disabled") + ' type="text" class="form-control"  id="tempWindowsNumber" value="' + value + '"/></div>';
                }
            }, {
                field: "tempAddTaskNumber",
                title: "临时增加任务数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
                formatter: function (value, row, index) {
                    return '<div class="def_tableDiv2"><input ' + (canSave && !isHistory && updateStatus ? "" : "disabled") + '  type="text" class="form-control" onchange="calculatePercent()" id="addNumber" value="' + value + '"/></div>';
                }
            }, {
                field: "tempDelTaskNumber",
                title: "临时删除任务数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
                formatter: function (value, row, index) {

                    return '<div class="def_tableDiv2"><input ' + (canSave && !isHistory && updateStatus ? "" : "disabled") + ' type="text" class="form-control" onchange="calculatePercent()" id="delNumber" value="' + value + '"/></div>';
                }
            }, {
                field: "totalTaskNumber",
                title: "测试任务总数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
                formatter: function (value, row, index) {
                    return '<span id="totalTaskNumber">' + value + '</span>';
                }
            }, {
                field: "requirementNumber",
                title: "业务需求数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "defectNumber",
                title: "缺陷数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "changePercent",
                title: "变更率",
                align: 'center',
                class: "stepOrder",
                width: '50px',
                formatter: function (value, row, index) {
                    return '<span id="changePercent">' + value + '%</span>';
                }
            }, {
                field: "repairRound",
                title: "总修复轮次",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "avgRepairRound",
                title: "平均修复轮次",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "userName",
                title: "负责人",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "",
                title: "操作",
                align: 'center',
                class: "stepOrder",
                width: '50px',
                visible: (canSave && !isHistory) || canChangePeople,
                formatter: function (value, row, index) {
                    return '<p style="white-space: nowrap">' +
                        (canSave && !isHistory && updateStatus ? '<a href="javascript:void(0);"  class="edit-opt" onclick="saveTable()">保存</a> ' : '') +
                        (canChangePeople ? '<a href="javascript:void(0);"  class="edit-opt" onclick="changePerson(-1)">负责人</a>' : '') +
                        '</p>';
                }
            }],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
        }
    });
}

function calculatePercent() {
    var addNumber = $("#addNumber").val();
    var delNumber = $("#delNumber").val();
    var totalTaskNumber = $("#totalTaskNumber").text();
    if (!reg.test(addNumber)) {
        $("#addNumber").val("");
        return;
    }
    if (!reg.test(delNumber)) {
        $("#delNumber").val("");
        return;
    }
    var percent = (parseInt(addNumber) + parseInt(delNumber)) / parseInt(totalTaskNumber) * 100;
    $("#changePercent").text(totalTaskNumber == 0 ? "0%" : percent.toFixed(2) + "%");
}

function createDevSystemTable(data) {
    if (data && data.length > 0) {
        $('#saveHisBtn').css('display', 'inline-block')
    } else {
        $('#saveHisBtn').css('display', 'none')
    }
    var isReview = tblReportMonthlyBaseList[0] ? tblReportMonthlyBaseList[0].auditStatus === 1 : false
    $("#devSystemTable").bootstrapTable('destroy');
    $("#devSystemTable").bootstrapTable({
        queryParamsType: "",
        data: data,
        columns: [
            {
                field: "index",
                title: "序号",
                align: 'center',
                class: "stepOrder",
                width: '30px',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }
            , {
                field: "yearMonth",
                title: "时间",
                align: 'center',
                class: "stepOrder",
                width: '80px',
                formatter: function (value, row, index) {
                    var time = $("#startDate").val();
                    return '<span style="white-space: nowrap">' + time.split('-')[0] + "年" + time.split('-')[1] + "月" + '</span>';
                }
            }, {
                field: "systemName",
                title: "系统名称",
                align: 'center',
                class: "stepOrder",
                width: '100px',
            }, {
                field: "taskNumber",
                title: "测试任务数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "defectNumber",
                title: "缺陷数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "repairedDefectNumber",
                title: "修复缺陷数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "unrepairedDefectNumber",
                title: "遗留缺陷数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "designCaseNumber",
                title: "设计用例数",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "defectPercent",
                title: "缺陷率",
                align: 'center',
                class: "stepOrder",
                width: '50px',
                formatter: function (value, row, index) {
                    return '<span>' + parseFloat(value).toFixed(2) + '%' + '</span>';
                }
            }, {
                field: "totalRepairRound",
                title: "累计修复轮次",
                align: 'center',
                class: "stepOrder",
                width: '30px',
            }, {
                field: "avgRepairRound",
                title: "平均修复轮次",
                align: 'center',
                class: "stepOrder",
                width: '30px',
                formatter: function (value, row, index) {
                    return '<span>' + parseFloat(value).toFixed(2) + '</span>';
                }
            }, {
                field: "auditStatus",
                title: "审核",
                align: 'center',
                class: "stepOrder",
                width: '30px',
                formatter: function (value, row, index) {
                    if (value === 0) {
                        value = "否"
                    } else if (value === 1) {
                        value = "是"
                    }
                    return '<span>' + value + '</span>';
                }
            }, {
                field: "userName",
                title: "负责人",
                align: 'center',
                class: "stepOrder",
                width: '30px',
            }, {
                field: "lastmonthUndefectedNumber",
                title: "上月漏检缺陷数",
                align: 'center',
                class: "stepOrder",
                width: '30px',
                formatter: function (value, row, index) {

                    return '<div class="def_tableDiv2"><input ' + (canSave && !isHistory && updateStatus ? "" : "disabled") + ' type="text" class="form-control" name="lastMonthDefectNum' + index + '" value="' + value + '"/></div>';
                }
            }, {
                field: "repairRound",
                title: "操作",
                align: 'center',
                class: "stepOrder",
                width: '100px',
                visible: (canSave && isHistory) || (canSure && isHistory) || canChangePeople,
                formatter: function (value, row, index) {
                    return '<p style="white-space: nowrap">' +
                        (canSave && !isHistory && updateStatus ? '<a href="javascript:void(0);"  class="edit-opt" onclick="saveSystemTable(' + index + ')">保存</a>  ' : '') +
                        (canSure && !isHistory && isReview && updateStatus ? '<a href="javascript:void(0);"  class="edit-opt" onclick="confirmSystemTable(' + index + ')">确认</a>  ' : '') +
                        (canChangePeople ? '<a href="javascript:void(0);"  class="edit-opt" onclick="changePerson(' + index + ')">负责人</a>' : '') +
                        '</p>';
                }
            }],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
        }
    });
}

function saveHis() {
    var allDevVersionTableData = $('#devVertionTable').bootstrapTable('getData');
    var allSystemTableData = $('#devSystemTable').bootstrapTable('getData');
    var flag = true;
    $.each(allDevVersionTableData, function (index, value) {
        value.planWindowsNumber = $("#planWindowsNumber").val();
        value.tempWindowsNumber = $("#tempWindowsNumber").val();
        value.tempAddTaskNumber = $("#addNumber").val();
        value.tempDelTaskNumber = $("#delNumber").val();
        value.changePercent = $("#changePercent").text().split("%")[0];
        if (value.changePercent > 100) {
            layer.alert("临时增加任务数 与 临时删除任务数 之和不能大于任务总数", {icon: 0});
            flag = false;
            return false;
        }
    })
    $.each(allSystemTableData, function (index, value) {
        value.yearMonth = $("#startDate").val();
        value.defectPercent = parseFloat(value.defectPercent).toFixed(2);
        value.avgRepairRound = parseFloat(value.avgRepairRound).toFixed(2);
        value.lastmonthUndefectedNumber = $("[name=lastMonthDefectNum" + index + "]").val();
        if (!reg.test(value.lastmonthUndefectedNumber)) {
            layer.alert("第" + (index + 1) + "行漏检缺陷数不为整数!", {icon: 0});
            flag = false;
            return false;
        }
    })
    if (!flag) {
        return;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/queryHis",
        method: "post",
        data: {
            time: $("#startDate").val()
        },
        dataType: "json",
        success: function (data) {
            if (data.flag) {
                save(allDevVersionTableData, allSystemTableData);
            } else {
                layer.confirm('存在历史数据，是否覆盖?', function (index) {
                    //do something
                    save(allDevVersionTableData, allSystemTableData);
                    layer.close(index);
                });
            }
            $("#loading").css('display', 'none');
        }
    });

}

function save(allDevVersionTableData, allSystemTableData) {
    $.ajax({
        url: "/testManage/monthlyReport/insertHis",
        method: "post",
        data: {
            allDevVersionTableData: JSON.stringify(allDevVersionTableData),
            allSystemTableData: JSON.stringify(allSystemTableData)
        },
        dataType: "json",
        success: function (data) {
            if (data.status == 1) {
                layer.alert("保存成功", {icon: 1});
            } else {
                layer.alert("保存失败，" + data.errorMessage, {icon: 2});
            }
            $("#loading").css('display', 'none');
        }
    });
}

function post(url, params) {
    // 创建form元素
    var temp_form = document.createElement("form");
    // 设置form属性
    temp_form.action = url;
    temp_form.target = "_self";
    temp_form.method = "post";
    temp_form.style.display = "none";
    // 处理需要传递的参数
    for (var x in params) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = params[x];
        temp_form.appendChild(opt);
    }
    document.body.appendChild(temp_form);
    // 提交表单
    temp_form.submit();
}

function saveTable() {
    var data = {
        planWindowsNumber: $('#planWindowsNumber').val(),
        tempWindowsNumber: $('#tempWindowsNumber').val(),
        tempAddTaskNumber: $('#addNumber').val(),
        tempDelTaskNumber: $('#delNumber').val(),
        id: tblReportMonthlyBaseList[0].id,
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/updateMonthlyBaseData",
        method: "post",
        data: data,
        dataType: "json",
        success: function (res) {
            if (res.status === '1') {
                layer.alert('操作成功!', {
                    icon: 1,
                })
            } else {
                layer.alert('操作失败!', {
                    icon: 2,
                })
            }
        },
        complete: function () {
            $("#loading").css('display', 'none');
        }
    });
}

function saveSystemTable(index) {
    var row = tblReportMonthlySystemDataList[index]
    var lastmonthUndefectedNumber = $("[name=lastMonthDefectNum" + index + "]").val();
    if (!reg.test(lastmonthUndefectedNumber)) {
        layer.alert("第" + (index + 1) + "行漏检缺陷数不为整数!", {icon: 0});
        return false;
    }
    for (k in row) {
        if (!row[k] && row[k] !== 0 && row[k] !== false) {
            delete row[k]
        }
    }
    row.lastmonthUndefectedNumber = lastmonthUndefectedNumber
    row.yearMonth = tempTime
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/updateMonthlySystemData",
        method: "post",
        data: row,
        dataType: "json",
        success: function (res) {
            if (res.status === '1') {
                layer.alert('操作成功!', {
                    icon: 1,
                })
            } else {
                layer.alert('操作失败!', {
                    icon: 2,
                })
            }
        },
        complete: function () {
            $("#loading").css('display', 'none');
        }
    });
}

function confirmSystemTable(index) {
    var row = tblReportMonthlySystemDataList[index]
    for (k in row) {
        if (!row[k] && row[k] !== 0 && row[k] !== false) {
            delete row[k]
        }
    }
    row.yearMonth = tempTime
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/auditSystemData",
        method: "post",
        data: row,
        dataType: "json",
        success: function (res) {
            if (res.status === '1') {
                tblReportMonthlySystemDataList[index].auditStatus = 1
                $('#devSystemTable').bootstrapTable('updateRow', {
                    index: index,
                    row: {
                        auditStatus: 1,
                    }
                });
                layer.alert('操作成功!', {
                    icon: 1,
                })
            } else {
                layer.alert('操作失败!', {
                    icon: 2,
                })
            }
        },
        complete: function () {
            $("#loading").css('display', 'none');
        }
    });

}

function changePerson(type) {
    var userName = ''
    changeType = type
    if (changeType === -1) {
        userName = tblReportMonthlyBaseList[0].userName
    } else {
        userName = tblReportMonthlySystemDataList[changeType].userName
    }
    $('#newtestManageUserName').val('')
    $('#newtestManageUser').val('')

    $('#change_people').modal("show");
    $('#currentChartPeople').val(userName)

}

function addChangePeopleCommit() {
    var userId = $('#newtestManageUser').val()
    var userName = $('#newtestManageUserName').val()
    var systemId = '', userType = 1, systemName = ''
    if (changeType === -1) {
        systemId = ''
    } else {
        userType = 2
        systemId = tblReportMonthlySystemDataList[changeType].systemId
        systemName = tblReportMonthlySystemDataList[changeType].systemName
    }
    var data = {
        userType: userType,
        userId: userId,
        userName: userName,
        systemName: systemName,
        systemId: systemId
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/addMonthlyConfig",
        method: "post",
        data: data,
        dataType: "json",
        success: function (res) {
            if (res.status === '1') {
                if (changeType === -1) {
                    tblReportMonthlyBaseList[0].userName = userName
                    $('#devVertionTable').bootstrapTable('updateRow', {
                        index: 0,
                        row: {
                            userName: userName,
                        }
                    });
                } else {
                    tblReportMonthlySystemDataList[changeType].userName = userName
                    $('#devSystemTable').bootstrapTable('updateRow', {
                        index: changeType,
                        row: {
                            userName: userName,
                        }
                    });
                }

                $('#change_people').modal("hide");
                layer.alert('操作成功!', {
                    icon: 1,
                })
            } else {
                layer.alert('操作失败!', {
                    icon: 2,
                })
            }

        },
        complete: function () {
            $("#loading").css('display', 'none');
        }
    });
}

function toMonthlyDetail() {
    var time = $("#startDate").val();
    if (time == "") {
        layer.alert("时间为空", {icon: 0});
        return;
    }
    window.open('/testManageui/testMonthReport/toTestMonthReport?monthTime=' + time)
}

function startAudit() {
    var id = tblReportMonthlyBaseList[0] ? tblReportMonthlyBaseList[0].id : ''
    if (!id) return false
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/initiateAudit",
        method: "post",
        data: {id: id, yearMonth: tempTime},
        dataType: "json",
        success: function (res) {
            if (res && res.status === '1') {
                queryData(tempTime)
                layer.alert('操作成功!', {
                    icon: 1,
                })
            } else {
                layer.alert('操作失败!', {
                    icon: 2,
                })
            }
        },
        complete: function () {
            $("#loading").css('display', 'none');
        }
    });
}


/*漏检经验总结*/
var missExperienceData = []
var missObj = {}
var missType = 'edit'

function showMissExperienceList(show) {
    $('#content-1').css('display', !show ? 'block' : 'none')
    $('#content-2').css('display', show ? 'block' : 'none')
    if (show) queryMissExperienceList()
}

function queryMissExperienceList() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/getAllUndetectedSummary",
        method: "post",
        dataType: "json",
        success: function (res) {
            if (res.status === '1') {
                missExperienceData = res.data
                initMissExperienceListTable(res.data)
            }
            $("#loading").css('display', 'none');
        }
    });
}

function initMissExperienceListTable(data) {
    var columns = [//漏检经验总结
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
            width: "100px",
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
        },
        {
            field: "id",
            title: "操作",
            formatter: function (value, row, index) {
                return '<p style="white-space: nowrap">' +
                    '<a href="javascript:void(0);"  class="edit-opt" onclick="deleteMissExperience(' + index + ')">删除</a> ' +
                    '<a href="javascript:void(0);"  class="edit-opt" onclick="editMissExperience(' + index + ')">编辑</a> ' +
                    '' + '</p>';
            }
        }
    ]
    $("#missExperienceListTable").bootstrapTable('destroy');
    $("#missExperienceListTable").bootstrapTable({
        data: data,
        columns: columns,
    });
}

function deleteMissExperience(index) {
    missObj = missExperienceData[index] || {}
    if (!missObj.id) return false
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/monthlyReport/deleteUndetectedSummary",
        method: "post",
        data: {id: missObj.id},
        dataType: "json",
        success: function (res) {
            if (res && res.status === '1') {
                missExperienceData.splice(index, 1)
                $('#missExperienceListTable').bootstrapTable('remove', {
                    field:'id',
                    values:missObj.id
                });
                layer.alert('操作成功!', {
                    icon: 1,
                })
            } else {
                layer.alert('操作失败!', {
                    icon: 2,
                })
            }
            $("#loading").css('display', 'none');
        }
    });
}

function editMissExperience(index) {
    missType = 'edit'
    missObj = missExperienceData[index] || {}
    $('#missExperienceEdit').modal('show')
    $('.miss-data').each(function (index, _this) {
        var id = $(_this).attr('data-id')
        $(_this).val(missObj[id] || '')
    })
}

function submitMissExperience() {
    $('.miss-data').each(function (index, _this) {
        var id = $(_this).attr('data-id')
        missObj[id] = $(_this).val()
    })
    var url = missType === 'add' ? "/testManage/monthlyReport/addUndetectedSummary" : "/testManage/monthlyReport/updateUndetectedSummary"
    $("#loading").css('display', 'block');
    $.ajax({
        url: url,
        method: "post",
        data: missObj,
        dataType: "json",
        success: function (res) {
            if (res && res.status === '1') {
                $('#missExperienceEdit').modal('hide')
                queryMissExperienceList()
                layer.alert('操作成功!', {
                    icon: 1,
                })
            } else {
                layer.alert('操作失败!', {
                    icon: 2,
                })
            }
            $("#loading").css('display', 'none');
        }
    });
}

function addMissExperience() {
    missType = 'add'
    missObj = {}
    $('.miss-data').each(function (index, _this) {
        $(_this).val('')
    })
    $('#missExperienceEdit').modal('show')
}