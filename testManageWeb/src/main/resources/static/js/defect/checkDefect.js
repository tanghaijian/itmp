var defectUrl = '/testManage/';
var _flag = 1;

var edit_info = {};

var editAttList = [];
var rejectionList = [];
var formFileList = [];
var testtaskStatusList = [];

var errorDefect = '系统内部错误，请联系管理员 ！！！';
var noPermission = '没有操作权限 ！！！';
var jiantou = "<span>&nbsp;&nbsp;修改为&nbsp;&nbsp;</span>";

var submitDefectStatus = "";
var _checkfiles = [];

var parameterArr = {};
parameterArr.arr = window.location.href.split("?");
parameterArr.parameterArr = parameterArr.arr[1].split(",");
parameterArr.obj = {};
for (var i = 0; i < parameterArr.parameterArr.length; i++) {
    var obj = parameterArr.parameterArr[i].split("=");
    parameterArr.obj[obj[0]] = obj[1];
}

$(document).ready(function () {

    rejectionList = $("#opt_rejection").find("option");
    testtaskStatusList = $("#testTaskStatus").find("option");

    $(".return").bind("click", function () {
        window.parent.layer.closeAll();
    });
    initPage();

    opt_uploadFile();
    check_uploadFileList();
});
function initPage() {
    checkDefect(parameterArr.obj.defectId);
    down2();
}
/*查询当前信息*/
function checkDefect(defectId) {
    $("#loading").css('display', 'block');
    /*清空暂时省略*/
    _flag = 1;
    $("#checkDefectID").val(defectId);
    $("#defectID").html(defectId);
    submitDefectStatus = "checkDefect";
    attList(defectId, "#check_table");
    $("#checkDefectDiv").modal("show");
}

function checkDefectMessage(rows) {
    resetCheckDefectMessage();
    $(".dealLog").css("display", "none");
    $(".workRemarks").css("display", "block");
    $("#check_title").text(rows.defectCode + "   |  " + isValueNull(rows.defectSummary));
    $("#check_systemName").text(isValueNull(rows.systemName));
    $("#check_defectCode").text(rows.defectCode);
    $("#check_submitDate").text(isValueNull(rows.submitDate));

    $("#check_req_code").text(isValueNull(rows.requirementCode));

    if (rows.testTaskCode != null) {
        $("#check_testTaskCode").html("<a class='a_style' onclick='getSee(" + rows.testTaskId + ")'>" + rows.testTaskCode + "</a>   " + "|   <a class='a_style' onclick='getSee(" + rows.testTaskId + ")'>" + rows.testTaskName + "</a>");
    }

    if (rows.testSetCaseExecuteId != null) {
        $("#check_testCaseName").html('<a  class="a_style" onclick="getExecuteCaseDetails(' + rows.testSetCaseExecuteId + ',' + null + ')">' + rows.testCaseName + '</a>');
    }

    $("#check_submitUserName").text(isValueNull(rows.submitUserName));
    $("#check_assignUserName").text(isValueNull(rows.assignUserName));
    $("#check_defectOverview").html(htmlDecodeJQ(rows.defectOverview));
    $("#check_defectSummary").text(isValueNull(rows.defectSummary));

    $("#check_testUserName").text(rows.testUserName == null ? '' : rows.testUserName);
    $("#check_developUserName").text(rows.developUserName == null ? '' : rows.developUserName);
//    $("#check_remark").text(rows.remark==null?'':rows.remark);
    $("#check_repairRound").text(isValueNull(rows.repairRound));
    if(rows.projectType == 2){    //新建类
	}else{
		$("#check_window").text(isValueNull(rows.windowName));
	}

    $("#check_projectGroupName").text(isValueNull(rows.projectGroupName));//
    $("#check_closeTime").text(isValueNull(rows.closeTime));
    $("#check_assetSystemTreeName").text(isValueNull(rows.assetSystemTreeName));//
    if(rows.discoveryEnvironment){
    	$('select#new_discovery_Environment').find('option').each(function(idx,val){
        	if(val.value == isValueNull(rows.discoveryEnvironment)){
        		$("#check_discovery_Environment").text(val.innerText);
        	}
        })
    }
    
    $("#check_detectedSystemVersionName").text(isValueNull(rows.detectedSystemVersionName));//
    $("#check_repairSystemVersionName").text(isValueNull(rows.repairSystemVersionName));//
    $("#check_expectRepairDate").text(isValueNull(rows.expectRepairDate));
    $("#check_estimateWorkload").text(isValueNull(rows.estimateWorkload));
    $("#check_rootCauseAnalysis").html(htmlDecodeJQ(rows.rootCauseAnalysis));

    if (rows.defectStatus == 5) {
        $("#check_solveStatus_span").css("display", "inline");
        for (var i = 0, len = solveStatusList.length; i < len; i++) {
            if (rows.solveStatus == solveStatusList[i][0]) {
                $("#check_solveStatus").text(solveStatusList[i][1]);
                break;
            }
        }
    }

    if (rows.defectStatus == 3) {
        $("#check_rejectReason_span").css("display", "inline");
        for (var i = 0, len = rejectionList.length; i < len; i++) {
            if (rows.rejectReason == rejectionList[i].value) {
                $("#check_rejectReason").text(rejectionList[i].innerHTML);
                break;
            }
        }
    }

    rows["checkDefectType"] = "#check_defectType";
    rows["checkDefectSource"] = "#check_defectSource";
    rows["checkDefectStatus"] = "#check_defectStatus";
    rows["checkSeverityLevel"] = "#check_severityLevel";
    rows["checkEmergencyLevel"] = "#check_emergencyLevel";
    dicDefectSelect(rows);
    addBtn(rows);
    // 打开新的页面查看图片
    openNewWindowCheckImg();
}

// 清空操作
function resetCheckDefectMessage() {
    $("#check_title").empty();
    $("#check_severityLevel").empty();
    $("#check_emergencyLevel").empty();
    $("#check_defectType").empty();
    $("#check_defectSource").empty();
    $("#check_defectStatus").empty();

    $("#check_systemName").text('');
    $("#check_defectCode").text('');
    $("#check_submitDate").text('');

    $("#check_req_code").text('');
    $("#check_testTaskCode").empty();
    $("#check_testTaskName").empty();
    $("#check_testCaseName").empty();
    $("#check_submitUserName").text('');
    $("#check_assignUserName").text('');
    $("#check_defectOverview").empty();
    $("#check_defectSummary").text('');
    $("#check_solveStatus").empty('');
    $("#check_rejectReason").empty('');

    $("#check_reqFetureCode").empty('');
    $("#check_reqFetureName").empty('');
    $("#check_testUserName").text('');
    $("#defectRemark").val('');
    $("#defectRemarks").empty();
    $("#checkAttTable2").empty();

    $("#check_projectGroupName").empty('');
    $("#check_closeTime").empty('');
    $("#check_assetSystemTreeName").empty('');
    $("#check_discovery_Environment").text('');
    $("#check_detectedSystemVersionName").empty('');
    $("#check_repairSystemVersionName").empty('');
    $("#check_expectRepairDate").empty('');
    $("#check_estimateWorkload").empty('');
    $("#check_rootCauseAnalysis").empty('');
}

function checkDefectRemark(data) {
    var str = '';
    for (var i = 0; i < data.remarks.length; i++) {
        var style = '';
        if (i == data.remarks.length - 1) {
            style = ' lastLog';
        }
        str += '<div class="logDiv' + style + '"><div class="logDiv_title"><span class="orderNum"></span>' +
            '<span>' + data.remarks[i].userName + '  | ' + data.remarks[i].userAccount + '</span>&nbsp;&nbsp;&nbsp;<span>' + datFmt(new Date(data.remarks[i].createDate), "yyyy-MM-dd hh:mm:ss") + '</span></div>' +
            '<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><pre class="def_pre">' + data.remarks[i].defectRemark + '</pre>' +
            '<div class="file-upload-list">';
        if (data.remarkAtts.length > 0) {
            str += '<table class="file-upload-tb">';
            var _trAll = '';
            for (var j = 0; j < data.remarkAtts.length; j++) {
                var _tr = '';
                if ((data.remarkAtts[j].defectRemarkId) == (data.remarks[i].id)) {

                    var file_type = data.remarkAtts[j].fileType;
                    var file_name = data.remarkAtts[j].fileNameOld;
                    var _td_icon = filePicClassPath(file_type); // common.js
                    var _td_name = '<span>' + file_name + '</span>';
                    var row = JSON.stringify(data.remarkAtts[j]).replace(/"/g, '&quot;');
                    _tr += '<tr><div class="fileTb" style="cursor:pointer" onclick ="downloadFile(' + row + ')">' + _td_icon + _td_name + "</tr>";

                }
                if (_tr != undefined) {
                    _trAll += _tr;
                }

            }
            str += _trAll + '</table>';
        }
        str += '</div></div></div></div></div>';

    }
    $("#defectRemarks").append(str);
}

function dicDefectSelect(rows) {
    for (var i = 0, len = defectTypeList.length; i < len; i++) {
        if (rows.defectType == defectTypeList[i][0]) {
            $(rows.checkDefectType).text(defectTypeList[i][1]);
            break;
        }
    }

    for (var i = 0, len = defectSourceList.length; i < len; i++) {
        if (rows.defectSource == defectSourceList[i][0]) {
            $(rows.checkDefectSource).text(defectSourceList[i][1]);
            break;
        }
    }
    for (var i = 0, len = defectStatusList.length; i < len; i++) {
        if (rows.defectStatus == defectStatusList[i][0]) {
            $(rows.checkDefectStatus).text(defectStatusList[i][1]);
            break;
        }
    }
    for (var i = 0, len = severityLevelList.length; i < len; i++) {
        if (rows.severityLevel == severityLevelList[i][0]) {
            var classColor = "classColor" + rows.severityLevel;
            var severityLevelData = "<span class='" + classColor + "'>" + severityLevelList[i][1] + "</span>";
            $(rows.checkSeverityLevel).html(severityLevelData);
            break;
        }
    }

    for (var i = 0, len = emergencyLevelList.length; i < len; i++) {
        if (rows.emergencyLevel == emergencyLevelList[i][0]) {
            var classColor = "classColor" + rows.emergencyLevel;
            var emergencyLevellData = "<span class='" + classColor + "'>" + emergencyLevelList[i][1] + "</span>";
            $(rows.checkEmergencyLevel).html(emergencyLevellData);
            break;
        }
    }
}

function down2() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: defectUrl + "defect/getDefectLogById",
        method: "post",
        data: {
            defectId: $("#checkDefectID").val()
        },
        success: function (data) {
            $("#dealLogDiv").empty();
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                if (data.data != null) {
                    var str = '';
                    for (var i = 0; i < data.data.length; i++) {
                        if (i == ( data.data.length - 1 )) {
                            str += '<div class="logDiv lastLog">';
                        } else {
                            str += '<div class="logDiv">'
                        }
                        var log_Type = data.data[i].logType;


                        str += '<div class="logDiv_title"><span class="orderNum"></span><span class="fontWeihgt">' + log_Type + '</span>&nbsp;&nbsp;&nbsp;' +
                            '<span>' + data.data[i].userName + '</span>&nbsp;&nbsp;&nbsp;<span>' + data.data[i].createDate + '</span></div>' +
                            '<div class="logDiv_cont"><div class="logDiv_contBorder">';
                        var _span = '';

                        switch (data.data[i].logType) {
                            case "新建缺陷":
                                str += "<br />";
                                break;
                            case "更新状态":
                                var childStr = '';
                                if (data.data[i].logDetail != null && data.data[i].logDetail.length > 0) {
                                    var logDetail = JSON.parse(data.data[i].logDetail);
                                    if (logDetail != null && logDetail.length > 0) {
                                        for (var j = 0; j < logDetail.length; j++) {
                                            if (j == 0) {
                                                if (logDetail[0].remark != undefined) {
                                                    childStr += "备注内容：<span class='span_font-weight'>" + logDetail[0].remark + "</span><br >";
                                                } else {
                                                    var oldName = logDetail[j].oldValue;
                                                    var newName = logDetail[j].newValue;

                                                    var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                                                    if (logDetail[j].fieldName == "缺陷描述") {
                                                        childStr += '<span>缺陷描述已修改。</span>'
                                                    } else {
                                                        childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                                                    }
                                                }
                                            } else {
                                                var oldName = logDetail[j].oldValue;
                                                var newName = logDetail[j].newValue;

                                                var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                                                if (logDetail[j].fieldName == "缺陷描述") {
                                                    childStr += '<span>缺陷描述已修改。</span>'
                                                } else {
                                                    childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                                                }
                                            }
                                        }
                                    } else {
                                        _span = '<span>未经任何操作</span><br/>';
                                    }
                                } else {
                                    _span = '<span>未经任何操作</span><br/>';
                                }
                                if (data.data[i].logAttachementList != null && data.data[i].logAttachementList.length > 0) {
                                    _span = '';

                                    var status_1 = 0;
                                    var status_2 = 0;
                                    childStr += '<div class="file-upload-list"><table class="file-upload-tb">';
                                    for (var j = 0; j < data.data[i].logAttachementList.length; j++) {

                                        var logAttachementList = data.data[i].logAttachementList;

                                        if (logAttachementList[j].status == 1) {

                                            childStr += '<tr><td>';
                                            var classType = '';
                                            var LogFileType = logAttachementList[j].fileType;
                                            classType = filePicClassPath(LogFileType, classType);

                                            if (status_1 == 0) {
                                                childStr += "<span>新增附件：</span></td><td>";
                                            } else {
                                                childStr += "<span></span></td><td>";
                                            }

                                            childStr += classType;

                                            var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                            childStr += '&nbsp;<a  class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                            status_1++;
                                        } else if (logAttachementList[j].status == 2) {

                                            childStr += '<tr><td>';

                                            var classType = '';
                                            var LogFileType = logAttachementList[j].fileType;
                                            classType = filePicClassPath(LogFileType, classType);

                                            if (status_2 == 0) {
                                                childStr += "<span>删除附件：</span></td><td>";
                                            } else {
                                                childStr += "<span></span></td><td>";
                                            }

                                            childStr += classType;

                                            var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                            childStr += '&nbsp;<a class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                            status_2++;
                                        }

                                    }
                                    childStr += '</table></div>';
                                }
                                childStr += _span;
                                str += '<div class="logDiv_contRemark">';
                                str += childStr;
                                str += '</div>';
                                break;
                            case "修改内容":
                                var childStr = '';
                                var logDetail = JSON.parse(data.data[i].logDetail);
                                if (logDetail != null && logDetail.length > 0) {
                                    for (var j = 0; j < logDetail.length; j++) {
                                        if (j == 0) {
                                            if (logDetail[0].remark != undefined) {
                                                childStr += "备注内容：<span class='span_font-weight'>" + logDetail[0].remark + "</span><br >";
                                            } else {
                                                var oldName = logDetail[j].oldValue;
                                                var newName = logDetail[j].newValue;

                                                var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                                                if (logDetail[j].fieldName == "缺陷描述") {
                                                    childStr += '<span>缺陷描述已修改。</span><br />'
                                                } else if (logDetail[j].fieldName == "扩展字段") {
                                                    var obj1 = JSON.parse(oldName).field;
                                                    var obj2 = JSON.parse(newName).field;
                                                    childStr += '<span>' + logDetail[j].fieldName + '：</span>';
                                                    childStr += dealExtend(obj1, obj2);
                                                } else {
                                                    childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                                                }
                                            }
                                        } else {
                                            var oldName = logDetail[j].oldValue;
                                            var newName = logDetail[j].newValue;

                                            var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                                            if (logDetail[j].fieldName == "缺陷描述") {
                                                childStr += '<span>缺陷描述已修改。</span><br />'
                                            } else if (logDetail[j].fieldName == "扩展字段") {
                                                var obj1 = JSON.parse(oldName).field;
                                                var obj2 = JSON.parse(newName).field;
                                                childStr += '<span>' + logDetail[j].fieldName + '：</span>';
                                                childStr += dealExtend(obj1, obj2);
                                            } else {
                                                childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                                            }
                                        }
                                    }
                                } else {
                                    _span = '<span>未经任何操作</span><br/>';
                                }

                                if (data.data[i].logAttachementList != null && data.data[i].logAttachementList.length > 0) {
                                    _span = '';
                                    var status_1 = 0;
                                    var status_2 = 0;
                                    childStr += '<div class="file-upload-list"><table class="file-upload-tb">';
                                    for (var j = 0; j < data.data[i].logAttachementList.length; j++) {
                                        var logAttachementList = data.data[i].logAttachementList;

                                        if (logAttachementList[j].status == 1) {
                                            childStr += '<tr><td>';
                                            var classType = '';
                                            var LogFileType = logAttachementList[j].fileType;
                                            classType = filePicClassPath(LogFileType, classType);

                                            if (status_1 == 0) {
                                                childStr += "<span>新增附件：</span></td><td>";
                                            } else {
                                                childStr += "<span></span></td><td>";
                                            }

                                            childStr += classType;
                                            var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                            childStr += '&nbsp;<a  class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                            status_1++;
                                        } else if (logAttachementList[j].status == 2) {
                                            childStr += '<tr><td>';

                                            var classType = '';
                                            var LogFileType = logAttachementList[j].fileType;
                                            classType = filePicClassPath(LogFileType, classType);

                                            if (status_2 == 0) {
                                                childStr += "<span>删除附件：</span></td><td>";
                                            } else {
                                                childStr += "<span></span></td><td>";
                                            }
                                            childStr += classType;

                                            var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                            childStr += '&nbsp;<a class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                            status_2++;
                                        }

                                    }
                                    childStr += '</table></div>';
                                }
                                childStr += _span;
                                str += '<div class="logDiv_contRemark">';
                                str += childStr;
                                str += '</div>';
                                break;
                            default:
                                break;
                        }
                        str += '</div></div></div>';
                    }

                    $("#dealLogDiv").append(str);

                }
            }
            _flag = 0;
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });

}

function logDetailList(fieldName, oldName, newName) {
    var arr = [];
    switch (fieldName) {
        case "缺陷状态":
            for (var k = 0; k < defectStatusList.length; k++) {
                if (defectStatusList[k].value != '') {
                    if (oldName == defectStatusList[k][0]) {
                        oldName = defectStatusList[k][1];
                    }
                    if (newName == defectStatusList[k][0]) {
                        newName = defectStatusList[k][1];
                    }
                }
            }
            break;
        case "缺陷类型":
            for (var k = 0; k < defectTypeList.length; k++) {
                if (defectTypeList[k].value != '') {
                    if (oldName == defectTypeList[k][0]) {
                        oldName = defectTypeList[k][1];
                    }
                    if (newName == defectTypeList[k][0]) {
                        newName = defectTypeList[k][1];
                    }
                }
            }
            break;
        case "缺陷来源":
            for (var k = 0; k < defectSourceList.length; k++) {
                if (defectSourceList[k].value != '') {
                    if (oldName == defectSourceList[k][0]) {
                        oldName = defectSourceList[k][1];
                    }
                    if (newName == defectSourceList[k][0]) {
                        newName = defectSourceList[k][1];
                    }
                }
            }
            break;
        case "严重级别":
            for (var k = 0; k < severityLevelList.length; k++) {
                if (severityLevelList[k].value != '') {
                    if (oldName == severityLevelList[k][0]) {
                        oldName = severityLevelList[k][1];
                    }
                    if (newName == severityLevelList[k][0]) {
                        newName = severityLevelList[k][1];
                    }
                }
            }
            break;
        case "紧急程度":
            for (var k = 0; k < emergencyLevelList.length; k++) {
                if (emergencyLevelList[k].value != '') {
                    if (oldName == emergencyLevelList[k][0]) {
                        oldName = emergencyLevelList[k][1];
                    }
                    if (newName == emergencyLevelList[k][0]) {
                        newName = emergencyLevelList[k][1];
                    }
                }
            }
            break;
        case "驳回理由":
            for (var k = 0; k < rejectionList.length; k++) {
                if (rejectionList[k].value != '') {
                    if (oldName == rejectionList[k].value) {
                        oldName = rejectionList[k].innerHTML;
                    }
                    if (newName == rejectionList[k].value) {
                        newName = rejectionList[k].innerHTML;
                    }
                }
            }
            break;
        case "解决情况状态":
            for (var k = 0; k < solveStatusList.length; k++) {
                if (solveStatusList[k].value != '') {
                    if (oldName == solveStatusList[k][0]) {
                        oldName = solveStatusList[k][1];
                    }
                    if (newName == solveStatusList[k][0]) {
                        newName = solveStatusList[k][1];
                    }
                }
            }
            break;
        default:
            break;
    }
    arr.push(oldName);
    arr.push(newName);
    return arr;
}

function addBtn(row) {
    var a = $('<botton class="btn btn-primary">撤   销</botton>');
    a.bind("click", function () {
        getDefectEntity(row.id, 3);
    });
    var b = $('<botton class="btn btn-primary">关   闭</botton>');
    b.bind("click", function () {
        getDefectEntity(row.id, 4);
    });
    var c = $('<botton class="btn btn-primary">提   交</botton>');
    c.bind("click", function () {
        getDefectEntity(row.id, 5);
    });
    var d = $('<botton class="btn btn-primary">删   除</botton>');
    d.bind("click", function () {
        getDefectEntity(row.id, 6);
    });
    var f = $('<botton class="btn btn-primary">驳    回</botton>');
    f.bind("click", function () {
        getDefectEntity(row.id, 8);
    });
    var g = $('<botton class="btn btn-primary">确   认</botton>');
    g.bind("click", function () {
        getDefectEntity(row.id, 10);
    });
    var h = $('<botton class="btn btn-primary">再次提交</botton>');
    h.bind("click", function () {
        getDefectEntity(row.id, 11);
    });
    var i = $('<botton class="btn btn-primary">修复完成</botton>');
    i.bind("click", function () {
        getDefectEntity(row.id, 12);
    });
    var j = $('<botton class="btn btn-primary">再次提交</botton>');
    j.bind("click", function () {
        getDefectEntity(row.id, 13);
    });
    var k = $('<botton class="btn btn-primary">重新打开</botton>');
    k.bind("click", function () {
        getDefectEntity(row.id, 14);
    });

    $("#bottomBtn").empty();

    if (row.submitUserId == currentUserId || row.assignUserId == currentUserId) {
        // 新建状态:编辑 提交 撤销 关闭 删除
        if (row.defectStatus == 1) {
            if (defectSubmit == true) {
                $("#bottomBtn").append(c)
            }
            if (defectRepeal == true) {
                $("#bottomBtn").append(a)
            }
            if (defectDelete == true) {
                $("#bottomBtn").append(d)
            }
        }
        // 待确认状态 开发：编辑 转交 确认 驳回（ 测试： 编辑，撤销 ，关闭）
        if (row.defectStatus == 2) {
            if (defectAffirm == true) {
                $("#bottomBtn").append(g)
            }
            if (defectReject == true) {
                $("#bottomBtn").append(f)
            }
        }
        //拒绝状态：测试：编辑  撤销 再次提交 --> 待确认
        if (row.defectStatus == 3) {
            if (defectSubmitAgain == true) {
                $("#bottomBtn").append(h)
            }
            if (defectRepeal == true) {
                $("#bottomBtn").append(a)
            }
        }
        // 修复中状态 开发：编辑 修复完成
        if (row.defectStatus == 4) {
            if (defectRepairComplete == true) {
                $("#bottomBtn").append(i)
            }
        }
        //待检测状态 编辑 关闭 再次打开
        if (row.defectStatus == 5) {
            if (defectAgainOpen == true) {
                $("#bottomBtn").append(k)
            }
            if (defectClose == true) {
                $("#bottomBtn").append(b)
            }
        }
        // 关闭
        if (row.defectStatus == 7) {
            if (defectAgainOpen == true) {
                $("#bottomBtn").append(k)
            }
        }
        // 重新打开： 修复完毕  状态：已修复5
        if (row.defectStatus == 10) {
            if (defectRepairComplete == true) {
                $("#bottomBtn").append(i)
            }
        }
    }

    var returnBtn = $('<botton class="btn btn-default">返    回</botton>');
    returnBtn.bind("click", function () {
        window.parent.layer.closeAll();
    });
    $("#bottomBtn").append(returnBtn);
}
function getDefectEntity(defectId, Opt) {
    $.ajax({
        url: defectUrl + "defect/getDefectEntity",
        type: "post",
        data: {
            defectId: defectId
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                optFun(data.defectInfo, Opt);
            }
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })
}
function optFun(defectInfo, Opt) {
    switch (Opt) {
        case 3:
            repeal(defectInfo);
            break;
        case 4:
            opt_close(defectInfo);
            break;
        case 5:
            submitDefect_a(defectInfo);
            break;
        case 6:
            deleteDefect(defectInfo);
            break;
        case 8:
            rejectDefectModal(defectInfo);
            break;
        case 10:
            affirm(defectInfo);
            break;
        case 11:
            submitAgainModal(defectInfo);
            break;
        case 12:
            repairCompleteModal(defectInfo);//zheli
            break;
        case 13:
            againModal(defectInfo);
            break;
        case 14:
            againOpen(defectInfo); //重新打开
            break;
        default:
            break;
    }
}
function againOpen(row) {
    layer.confirm("确认重新打开该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        opt_updateDefectStatus(row, 10);
    })
}

/*撤销*/
function repeal(rows) {
    layer.confirm("确认撤销该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        opt_updateDefectStatus(rows, 6);
    })
}
/*关闭*/
function opt_close(rows) {
    layer.confirm("确认关闭该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        //var row = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
        opt_updateDefectStatus(rows, 7);
    })
}

/*修改缺陷状态*/
function opt_updateDefectStatus(row, defectStatus) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: defectUrl + "defect/updateDefectStatus",
        type: "post",
        data: {
            defectId: row.id,
            defectStatus: defectStatus,
            submitUserId: row.submitUserId
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                layer.alert('操作成功 ！', {
                    icon: 1,
                    title: "提示信息"
                });
                initPage();
            }
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })

}
//列表的提交按钮
function submitDefect_a(row) {
    formFileList = [];
    var data = row;
    submitDefectStatus = "edit";
    if (data.assignUserId == null) {
        layer.alert("必须选择指派人！", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    } else {
        edit_info.assignUserId = data.assignUserId;
        edit_info.id = data.id;
        updateDefectStatus(2);
    }
}

//新增 编辑 缺陷
function updateDefectStatus(defectStatus) {
    var files = new FormData();
    /*if(formFileList.length > 0 ){
     var filesSize = 0;
     for (var i = 0,len2 = formFileList.length;i < len2;i++){
     filesSize += formFileList[i].size;
     files.append("files",formFileList[i]);
     }

     if(filesSize > 1048576000){
     layer.alert('文件太大,请删选！！！', {
     icon: 2,
     title: "提示信息"
     });
     return false;
     }

     }*/

    /* if (submitDefectStatus == "new"){
     var obj = {};
     obj.assignUserId = $("#new_assignUserId").val();
     obj.testTaskId = $("#testTaskId").val();
     obj.requirementCode = $("#new_requirementCode").val();
     obj.systemId = $("#systemId").val();
     obj.commissioningWindowId = $("#new_commissioningWindowId").val();
     obj.defectSummary = htmlEncodeJQ($.trim($("#new_defectSummary").val()));
     obj.repairRound = htmlEncodeJQ($.trim($("#new_repairRound").val()));
     obj.defectOverview =  $("#defectOverview").html();
     obj.defectType = $.trim($("#new_defectType").find("option:selected").val());
     obj.defectSource = $.trim($("#new_defectSource").find("option:selected").val());
     obj.severityLevel = $.trim($("#new_severityLevel").find("option:selected").val());
     obj.emergencyLevel = $.trim($("#new_emergencyLevel").find("option:selected").val());
     obj.defectStatus = defectStatus;
     files.append("defectInfo",JSON.stringify(obj));

     $("#loading").css('display','block');
     // 新增缺陷
     $.ajax({
     type: "post",
     url:"/zuul"+defectUrl+'defect/insertDefect',
     dataType: "json",
     data:files,
     cache: false,
     processData: false,
     contentType: false,
     success: function(data) {
     $("#loading").css('display','none');
     if (data.status == 2){
     layer.alert(data.errorMessage, {
     icon: 2,
     title: "提示信息"
     });
     } else if(data.status == 1){
     layer.alert('操作成功！', {
     icon: 1,
     title: "提示信息"
     });
     $("#newDefect").modal("hide");
     reset_opt();
     formFileList = [];
     editAttList = [];
     initTable();
     }
     },
     error:function(){
     $("#loading").css('display','none');
     layer.alert(errorDefect, {
     icon: 2,
     title: "提示信息"
     });
     }
     })
     } else */
    if (submitDefectStatus == "edit") {
        // 编辑缺陷
        $("#loading").css('display', 'block');
        var obj = {};
        obj.id = edit_info.id;
        obj.assignUserId = edit_info.assignUserId;

        obj.defectStatus = defectStatus;
        files.append("defectInfo", JSON.stringify(obj));

        $.ajax({
            type: "post",
            url: "/zuul" + defectUrl + 'defect/updateDefect',
            dataType: "json",
            data: files,
            cache: false,
            processData: false,
            contentType: false,
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    layer.alert('操作成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    initPage();
                    formFileList = [];
                    editAttList = [];
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
}

function deleteDefect(rows) {
    layer.confirm("确定删除该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {

        layer.closeAll('dialog');

        //var data = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
        var data = rows;
        $("#loading").css('display', 'block');
        $.ajax({
            url: defectUrl + "defect/removeDefect",
            method: "post",
            data: {
                id: data.id,
                submitUserId: data.submitUserId
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    layer.alert('删除缺陷成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    window.history.back(-1);
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    });
}


/*驳回操作 状态变为 拒绝*/
var rejectDefectData = {};
function rejectDefectModal(rows) {
    formFileList = [];
    //rejectDefectData = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
    rejectDefectData = rows;

    $(".rejection").css("display", "block");
    $("#opt_RejectDefect").css("display", "inline-block");
    $("#rejectDiv").modal("show");
}


/*驳回*/
function opt_rejectDefect() {
    var rejection = $.trim($("#opt_rejection").find("option:selected").val());
    if (rejection == '') {
        layer.alert("必须选择驳回理由！", {
            icon: 2,
            title: "提示信息"
        })
    } else {
        $("#loading").css('display', 'block');

        $.ajax({
            url: defectUrl + "defect/updateDefectwithTBC",
            type: "post",
            data: {
                defectId: rejectDefectData.id,
                defectRemark: $("#opt_defectRemark").val(),
                rejectReason: rejection,
                defectStatus: 3,
                submitUserId: rejectDefectData.submitUserId,
                oldAssignUserId: rejectDefectData.assignUserId
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    remarkUploadFile(data.logId);
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
}
/*备注日志上传文件 后台*/
function remarkUploadFile(logId) {
    if (formFileList.length <= 0) {
        $("#loading").css('display', 'none');
        layer.alert('操作成功！', {
            icon: 1,
            title: "提示信息"
        });

        initPage();
        $("#rejectDiv").modal("hide");
        /* reset_opt();*/
        return false;
    }
    var remarkFiles = new FormData();
    var remarkFilesSize = 0;
    for (var i = 0, len2 = formFileList.length; i < len2; i++) {
        remarkFilesSize += formFileList[i].size;
        remarkFiles.append("files", formFileList[i]);
    }
    if (remarkFilesSize > 1048576000) {
        layer.alert('文件太大,请删选！！！', {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        type: "post",
        url: '/zuul' + defectUrl + 'defect/updateRemarkLogFiles?logId=' + logId,
        dataType: "json",
        data: remarkFiles,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                layer.alert('操作成功！', {
                    icon: 1,
                    title: "提示信息"
                });
                formFileList = [];
                initPage();
                $("#rejectDiv").modal("hide");
            }
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })
}
/*确认操作：状态改为处理中*/
function affirm(row) {
    //var row = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
    layer.confirm("确定确认该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        $("#loading").css('display', 'block');
        $.ajax({
            url: defectUrl + "defect/updateDefectwithTBC",
            type: "post",
            data: {
                defectId: row.id,
                defectStatus: 4,
                submitUserId: row.submitUserId,
                oldAssignUserId: row.assignUserId
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    layer.alert('操作成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    initPage();
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    })
}
/*再次提交 操作: 状态变为待确认 */
var __defectAgainData = {};
function submitAgainModal(row) {
    /* reset_opt();*/
    $("#opt_rejectionShow").empty()
    formFileList = [];
    /*var data = rows.replace(/\n/g,"\\r\\n");
     var row = JSON.parse(data);*/
    __defectAgainData = row;
    var classColor = 'rejectionSituation';
    var rejection = $("#opt_rejection").find("option");
    for (var i = 0, len = rejection.length; i < len; i++) {
        if (row.rejectReason == rejection[i].value) {
            classColor += row.rejectReason;
            var opt = "<span class='" + classColor + "'>" + rejection[i].innerHTML + "</span>";
            $("#opt_rejectionShow").append(opt);
        }
    }
    $(".opt_rejectionDiv").css("display", "block");
    $("#opt_submitAgainDefect").css("display", "inline-block");
    $("#rejectDiv").modal("show");
}
/*再次提交请求后台 状态变为待确认*/
function submitAgainDefect() {
    $("#loading").css('display', 'block');

    $.ajax({
        url: defectUrl + "defect/updateDefectStatus",
        type: "post",
        data: {
            defectId: __defectAgainData.id,
            defectRemark: htmlEncodeJQ($.trim($("#opt_defectRemark").val())),
            defectStatus: 2,
            submitUserId: __defectAgainData.submitUserId
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                remarkUploadFile(data.logId);
            }
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })

}

var _repairCompleteDefectData = {};
function repairCompleteModal(rows) {
    /*reset_opt();*/
    formFileList = [];
    // _repairCompleteDefectData = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
    _repairCompleteDefectData = rows;
    $(".solution").css("display", "block");
    $("#opt_Restored").css("display", "inline-block");
    $("#rejectDiv").modal("show");
}

/*修复完成，请求后台操作*/
function repairComplete() {
    var solution = $.trim($("#opt_solution").find("option:selected").val())
    if (solution == '') {
        layer.alert("必须选择解决情况！", {
            icon: 2,
            title: "提示信息"
        })
    } else {
        $("#loading").css('display', 'block');

        $.ajax({
            url: defectUrl + "defect/updateDefectwithTBC",
            type: "post",
            data: {
                defectId: _repairCompleteDefectData.id,
                defectRemark: htmlEncodeJQ($.trim($("#opt_defectRemark").val())),
                solveStatus: solution,
                defectStatus: 5,
                submitUserId: _repairCompleteDefectData.submitUserId,
                oldAssignUserId: _repairCompleteDefectData.assignUserId
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {

                    remarkUploadFile(data.logId);
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
}
/*再次打开*/
function againModal(row) {
    /* reset_opt();*/
    formFileList = [];
    var updateStatus = '';
    for (var i = 0, len = defectStatusList.length; i < len; i++) {
        if (2 == defectStatusList[i][0]) {
            updateStatus = defectStatusList[i][1];
            break;
        }
    }
    // var row = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
    layer.confirm("确认再次打开该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        opt_updateDefectStatus(row, 2);
    })
}

//清空文件
function resetFile() {
    $("#opt_uploadFile").val("");
}

function down(This) {
    if ($(This).hasClass("fa-angle-double-down")) {
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
        $(This).parents('.allInfo').children(".connect_div").slideDown(100);
    } else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
        $(This).parents('.allInfo').children(".connect_div").slideUp(100);
    }
}
function showField(data) {
    $("#canEditField").empty();
    if (data != null && data != "undefined" && data.length > 0) {
        for (var i = 0; i < data.length; i++) {
            var aLabel = '<div class="def_col_6 font_right fontWeihgt">' + data[i].label + '：</div>';
            var aLabelContent = '<div class="def_col_12">' + data[i].valueName + '</div>';
            $("#canEditField").append(aLabel, aLabelContent);
        }
    }
}

var _seefiles = [];
//关联测试案例详情页面
function getExecuteCaseDetails(id, setId) {
    $("#checkUpfileTable").empty();
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/testExecution/getExecuteCaseDetails",
        method: "post",
        data: {
            "testSetCaseExecuteId": id,
            'testSetCaseId': setId
        },
        success: function (data) {
            modalType = 'seeFile';
            if (data.listCase.caseExecuteResult == 2) {
                $(".successBg").css("display", "inhert");
                $(".failBg").css("display", "none");
            } else if (data.listCase.caseExecuteResult == 3) {
                $(".successBg").css("display", "none");
                $(".failBg").css("display", "inhert");
            }
            $("#checkTitle").html(data.listCase.caseNumber + " " + data.listCase.caseName);
            $("#checkNameAndTime").html(data.userName + " | " + data.listCase.createDate);
            $("#checkCasePrecondition").html(data.listCase.casePrecondition);
            $("#checkExcuteRemark").html(data.listCase.excuteRemark);

            $("#checkRelatedDefects").empty();
            for (var i = 0; i < data.listDefect.length; i++) {
                var str = '';
                str += '<div class=rowdiv><div class="def_col_5">';

                for (var j = 0; j < $("#relatedDefectsType option").length; j++) {
                    if (data.listDefect[i].defectType == $("#relatedDefectsType option").eq(j).val()) {
                        str += '<span class="classColor' + data.listDefect[i].defectType + '">' + $("#relatedDefectsType option").eq(j).text() + '</span>' +
                            '<span class="classColor' + data.listDefect[i].defectType + '">P' + $("#relatedDefectsType option").eq(j).val() + '</span>';
                    }
                }
                str += ' </div><div class="def_col_6 defectCode" title="' + data.listDefect[i].defectCode + '">' + data.listDefect[i].defectCode +
                    '</div><div class="def_col_25 fontWrop">' + data.listDefect[i].defectSummary + '</div></div>';
                $("#checkRelatedDefects").append(str);
            }

            // 测试案例步骤
            $("#checkCaseSteps").bootstrapTable('destroy');
            $("#checkCaseSteps").bootstrapTable({
                queryParamsType: "",
                pagination: false,
                data: data.listStep,
                sidePagination: "server",
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                columns: [{
                    field: "id",
                    title: "id",
                    visible: false,
                    align: 'center'
                }, {
                    field: "stepOrder",
                    title: "步骤",
                    width: 50,
                    align: 'center'
                }, {
                    field: "stepDescription",
                    title: "步骤描述",
                    width: 300,
                    align: 'center'
                }, {
                    field: "stepExpectedResult",
                    title: "预期结果",
                    align: 'center'
                }, {
                    field: "stepExecuteResult",
                    title: "执行结果",
                    width: 100,
                    align: 'center',
                    formatter: function (value, row, index) {
                        for (var j = 0; j < $("#executionSelect option").length; j++) {
                            if (row.stepExecuteResult == $("#executionSelect option").eq(j).val()) {
                                return '<span class="spanClass' + row.stepExecuteResult + '">' + $("#executionSelect option").eq(j).text() + '</span>';
                            }
                        }
                    }
                }, {
                    field: "stepActualResult",
                    title: "执行情况",
                    align: 'center'
                }],
                onLoadSuccess: function () {
                },
                onLoadError: function () {
                }
            });
            if ((data.listFile).length > 0) {
                var _table = $("#checkUpfileTable");
                var attMap = data.listFile;
                //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                for (var i = 0; i < attMap.length; i++) {
                    var _tr = '';
                    var file_name = attMap[i].fileNameOld;
                    var file_type = attMap[i].fileType;

                    var _td_icon = filePicClassPath(file_type); // common.js
                    var row = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    var _td_name = '<a style="font-family:Helvetica Neue,Helvetica,Arial,sans-serif;" onclick="downloadTaseCase(' + row + ')">' + file_name + '</a><i class = "file-bucket">' + attMap[i].fileS3Bucket + '</i><i class = "file-s3Key">' + attMap[i].fileS3Key + '</i></td>';

                    _tr += '<tr><td><div class="fileTb" style="cursor:pointer" >' + _td_icon + " " + _td_name + '</tr>';
                    _table.append(_tr);
                    _seefiles.push(data.listFile[i]);
                    $("#seeFiles").val(JSON.stringify(_seefiles));
                }
            }
            $(".right_div").css("display", "none"); //显示右边的案例div
            $(".rightCaseDivCheck").css("display", "block"); //显示右边的案例div
            $("#loading").css('display', 'none');
            $("#checkTestCaseModal").modal("show");
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

//缺陷处理日志中 扩展字段的 处理方法
function dealExtend(obj1, obj2) {
    var str = '';
    var oldObj = {};
    var newObj = {};
    for (var i = 0; i < obj1.length; i++) {
        if (obj1[i].required == "true") {
            oldObj[obj1[i].fieldName] = {};
            oldObj[obj1[i].fieldName].valueName = obj1[i].valueName;
            oldObj[obj1[i].fieldName].labelName = obj1[i].labelName;
        }
    }
    for (var i = 0; i < obj2.length; i++) {
        if (obj2[i].required == "true") {
            newObj[obj2[i].fieldName] = {};
            newObj[obj2[i].fieldName].valueName = obj2[i].valueName;
            newObj[obj2[i].fieldName].labelName = obj2[i].labelName;
        }
    }
    for (var key in newObj) {
        if (oldObj[key] == undefined) {
            str += newObj[key].labelName + ' 修改为  "' + newObj[key].valueName + '"<br>';
        } else {
            if (oldObj[key].valueName != newObj[key].valueName) {
                str += newObj[key].labelName + '"' + oldObj[key].valueName + '" 修改为  "' + newObj[key].valueName + '"<br>';
            }
        }
    }
    return str;
}

// 显示指定的div
function showThisDiv(This, num) {
    if ($(This).hasClass("def_changeTit")) {
        $("#titleOfwork .def_controlTit").addClass("def_changeTit");
        $(This).removeClass("def_changeTit");
        if (num == 1) {
            $(".dealLog").css("display", "none");
            $(".workRemarks").css("display", "block");
        } else if (num == 2) {
            $(".dealLog").css("display", "block");
            $(".workRemarks").css("display", "none");
        }
    }
}

//提交备注
function addDefectRemark() {
    var defectRemark = $.trim($("#defectRemark").val());
//	if(defectRemark==""||defectRemark==undefined){
//		layer.alert('备注信息不能为空！', {
//            icon: 2,
//            title: "提示信息"
//        });
//		return;
//	}
    if (defectRemark == '' && $("#checkfiles2").val() == '') {
        layer.alert('备注内容和附件不能同时为空！！！', {icon: 0});
        return;
    }
    var id = $.trim($("#defectID").text());
    var obj = {};
    obj.defectRemark = $.trim($("#defectRemark").val());
    obj.defectId = id;
    var remark = JSON.stringify(obj);
    $.ajax({
        type: "post",
        url: "/testManage/defect/addDefectRemark",
        data: {
            remark: remark,
            attachFiles: $("#checkfiles2").val()
        },
        success: function (data) {
            layer.alert('保存成功！', {
                icon: 1,
                title: "提示信息"
            });
            _checkfiles = [];
            $("#checkfiles2").val('');
            $("#checkuploadFile_defect").val('');
            checkDefect(id);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }


    });
}

//文件上传，并列表展示
function check_uploadFileList() {
    //列表展示
    $("#checkuploadFile_defect").change(function () {
        var files = this.files;
        var formFile = new FormData();

        /*if(!fileAcceptBrowser()){
         for(var i=0,len=files.length;i<len;i++){
         var file_type = files[i].name.split(".")[1];
         if(file_type!="doc"&&file_type!="docx"&&file_type!="xls"&&file_type!="xlsx"&&file_type!="txt"&&file_type!="pdf"){
         layer.alert('上传文件格式错误! 请上传后缀名为.doc，.docx，.xls，.xlsx，.txt，.pdf的文件', {icon:0});
         return false;
         }
         }
         }*/

        outer: for (var i = 0, len = files.length; i < len; i++) {
            var file = files[i];
            if (file.size <= 0) {
                layer.alert(file.name + "文件为空！", {icon: 0});
                continue;
            }
            var fileList = _checkfiles;

            for (var j = 0; j < fileList.length; j++) {
                if (fileList[j].fileNameOld == file.name) {
                    layer.alert(file.name + "文件已存在！！！", {icon: 0});
                    continue outer;
                }
            }
            formFile.append("files", file);
            //读取文件
            if (window.FileReader) {
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onerror = function (e) {
                    layer.alert("文件" + file.name + " 读取出现错误", {icon: 0});
                    return false;
                };
                reader.onload = function (e) {
                    if (e.target.result) {
                    }
                };
            }

            //列表展示
            var _tr = '';
            var file_name = file.name.split("\.")[0];
            var file_type = file.name.split("\.")[1];
            var _td_icon = filePicClassPath(file_type); // common.js
            var _td_name = '<span >' + file.name + '</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';

            var _table = $(this).parent(".file-upload-select").next(".file-upload-list").children("table");
            _tr += '<tr><td><div class="fileTb">' + _td_icon + '  ' + _td_name + _td_opt + '</tr>';
            _table.append(_tr);

        }
        //上传文件
        $.ajax({
            type: 'post',
            url: '/zuul/testManage/testtask/uploadFile',
            contentType: false,
            processData: false,
            dataType: "json",
            data: formFile,
            success: function (data) {
                for (var k = 0, len = data.length; k < len; k++) {
                    _checkfiles.push(data[k]);
                    $(".file-upload-tb span").each(function (o) {
                        if ($(this).text() == data[k].fileNameOld) {
                            //$(this).parent().children(".file-url").html(data[k].filePath);
                            $(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
                            $(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
                        }
                    });
                }
                $("#checkfiles2").val(JSON.stringify(_checkfiles));
                clearUploadFile('checkuploadFile');
            },
            error: function () {
                layer.alert("系统内部错误，请联系管理员！！！", {icon: 2});
            }
        });
    });
}

//清空上传文件
function clearUploadFile(idName) {
    //$(idName).wrap('<form></form>');
    //$(idName).unwrap();
    $('#' + idName + '').val('');
}

//移除上传文件
function delFile(that) {
    var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
    /*var url = $(that).parent().prev().children().children(".file-url").text(); 
     var fileS3Bucket = $(that).parent().prev().children().children(".file-bucket").text(); 
     $.ajax({
     type:'post',
     url:'/testManage/testtask/delFile',
     data:{
     url:url,
     fileS3Bucket :fileS3Bucket,
     fileS3Key:fileS3Key
     },
     success:function(data){
     if(data.status=="success"){
     alert("删除成功！");
     }else if(data.fail == "2"){
     alert("删除失败！！！");
     }
     }
     });*/
    $(that).parent().parent().remove();
    removeFile(fileS3Key);
}

// 移除文件
function removeFile(fileS3Key) {
    var _file = $("#checkfiles2").val();
    if (_file != "") {
        var files = JSON.parse(_file);
        for (var i = 0, len = files.length; i < len; i++) {
            if (files[i].fileS3Key == fileS3Key) {
                Array.prototype.splice.call(files, i, 1);
                break;
            }
        }
        _files = files;
        $("#checkfiles2").val(JSON.stringify(files));
    }
    for (i = 0; i < _checkfiles.length; i++) {
        if (_checkfiles[i].fileS3Key == fileS3Key) {
            _checkfiles.splice(i, 1);
        }
    }
    $("#checkuploadFile_defect").val('');
}

