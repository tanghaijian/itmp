/**
 * Description: 缺陷管理JavaScript
 * Author:liushan
 * Date: 2018/12/10 下午 2:21
 */
// 文件图标
var _icon_word = "../static/images/file/word.png";
var _icon_excel = "../static/images/file/excel.png";
var _icon_text = "../static/images/file/text.png";
var _icon_pdf = "../static/images/file/pdf.png";
var _icon_PNG = "../static/images/file/PNG.png";
var _icon_JPG = "../static/images/file/JPG.png";
var _icon_BMP = "../static/images/file/BMP.png";
var _icon_WENHAO = "../static/images/file/text.png";

var userModalStatus = '';
var testTaskModalStatus = '';
var submitDefectStatus = '';

var defectStatusList = '';
var solveStatusList = '';
var rejectionList = '';
var severityLevelList = '';
var emergencyLevelList = '';
var defectTypeList = '';
var defectSourceList = '';
var defectInfoForExportObj = {};

var fileTableName = '';
var _flag = 1;
var editAttList = [];
var formFileList = [];
var _checkfiles = [];

var errorDefect = '系统内部错误，请联系管理员 ！！！';
var noPermission = '没有操作权限 ！！！';
var defectUrl = '/devManage/';

var jiantou = "<span>&nbsp;&nbsp;修改为&nbsp;&nbsp;</span>";


$(document).ready(function () {
    $("#loading").css('display', 'block');
    $("#sel_assingUserId").val(currentUserId);
    $("#sel_assingUserName").val(currentUserName);
    // 缺陷状态
    defectStatusList = $("#defectStatus").find("option");
    // 解决情况
    solveStatusList = $("#opt_solution").find("option");
    // 驳回理由
    rejectionList = $("#opt_rejection").find("option");
    // 严重级别
    severityLevelList = $("#severityLevel").find("option");
    // 紧急程度
    emergencyLevelList = $("#emergencyLevel").find("option");
    // 缺陷类型
    defectTypeList = $("#defectType").find("option");
    // 缺陷来源
    defectSourceList = $("#defectSource").find("option");

    findByStatus();
    default_list();
    uploadFileList();
    edit_uploadList();
    opt_uploadFile();
    check_uploadFileList();
    
    

    /*新建人员*/
    $("#new_assignUserName").click(function () {
        userModalStatus = "new";
        userInit();
    });

    /*编辑人员*/
    $("#edit_assignUserName").click(function () {
        userModalStatus = "edit";
        userInit();
    });

    /*查询人员*/
    $("#submitUserName").click(function () {
        userModalStatus = "select";
        userInit();
    });

    /*转交操作，人员展示*/
    $("#opt_assignUserName").click(function () {
        userModalStatus = "opt";
        userInit();
    });

    // 主修复人 查询
    $("#sel_assingUserName").click(function () {
        userModalStatus = "assing";
        userInit();
    });

    /*查询系统*/
    $("#systemName").click(function () {
        systemInit();
    });

    /*查询需求*/
    $("#requirementName").click(function () {
        reqInit();
    });

    /*工作任务弹框*/
    $("#testTaskName").click(function () {
        testTaskInit();
    });

    $("#edit_testTaskName").click(function () {
        testTaskInit();
    });
    
    /*设置投产*/
    $("#setComWindow_name").click(function () {
    	winReset();
        ComWindowShow(true);
        $("#comWindowModal").modal("show");
    	$('#userbutton').data('user','set');
    });
    
    /*投产窗口*/
    $("#windowName").click(function () {
    	ComWindowInit();
    	$('#userbutton').data('user','default');
    });


    //常规用法
    laydate.render({
        elem: '#submitDate',
        btns: ['now', 'confirm'],
        trigger: 'click',
        done: function (value, date, endDate) {
            $("#submitDate").next().css("display", "block");
        }
    });

    laydate.render({
        elem: '#check_dev_task_startDate',
        format: "yyyy年MM月dd日"
    });

    laydate.render({
        elem: '#check_dev_task_endDate',
        format: "yyyy年MM月dd日"
    });

    //搜索展开和收起
    downOrUpButton();


    // 所有的Input标签，在输入值后出现清空的按钮
    buttonClear();

    // 表格样式
    tableMouseover("#defectList");

    banEnterSearch();
    formValidator();
    refactorFormValidator();
});

// 选择自己的缺陷
function selectMyselfDefect(that) {
    $($(that).parent().parent().children('input[type^="hidden"]')).val(currentUserId);
    $($(that).parent().parent().children('input[type^="text"]')).val(currentUserName).change(function () {
        $(this).parent().children(".btn_clear").css("display", "block");
    }).change();
}

/*缺陷列表*/
function initTable() {
    var page = $('#defectList').getGridParam('page');
    $("#defectList").jqGrid("clearGridData");
    $("#defectList").jqGrid("setGridParam", {page: page != null && page != undefined ? page : 1});
    $("#defectList").jqGrid({
        url: defectUrl + "defect/list",
        datatype: 'json',
        mtype: "POST",
        height: 'auto',
//        width: $(".content-table").width() * 0.999,
        autowidth: true,
        //显示水平滚动条
        altRows: true,
        altclass: 'differ',
        shrinkToFit:false,
        autoScroll: true,
        rowNum: 10,
        rowTotal: 200,
        rowList: [10, 20, 30],
        rownumWidth: 40,
        pager: '#defectListPager',
        sortable: true,   //是否可排序
        sortorder: 'desc',
        sortname: 'id',
        loadtext: "数据加载中......",
        viewrecords: true, //是否要显示总记录数
        postData: {
        	defectCode: $.trim($("#defectCode").val()),
            defectSummary: $.trim($("#defectSummary").val()),
            submitDate: $("#submitDate").val(),
            defectSourceStr: $("#defectSource").val() != null ? $("#defectSource").val().toString() : '',
            severityLevelStr: $("#severityLevel").val() != null ? $("#severityLevel").val().toString() : '',
            emergencyLevelStr: $("#emergencyLevel").val() != null ? $("#emergencyLevel").val().toString() : '',
            defectTypeStr: $("#defectType").val() != null ? $("#defectType").val().toString() : '',
            requirementCodeStr: $.trim($("#requirementCode").val()),
            systemIdStr: $("#sel_systemId").val() != null ? $("#sel_systemId").val().toString() : '',
            commissioningWindowIdStr: $("#windowId").val() != null ? $("#windowId").val().toString() : "",
            submitUserIdStr: $("#submitUserId").val() != null ? $("#submitUserId").val().toString() : "",
            assignUserIdStr: $("#sel_assingUserId").val() != null ? $("#sel_assingUserId").val().toString() : "",
            defectStatusList: $("#defectStatus").val() != null ? $("#defectStatus").val().toString() : ''
        },
        colNames: ['id', '缺陷编号', '缺陷摘要', '缺陷状态', '所属需求', '涉及系统', '投产窗口', '主修复人', '提出人', '操作'],
        colModel: [{name: 'id', index: 'id', hidden: true}, {
            name: "defectCode",
            index: "defectCode",
            align: 'center',
            width: 150,
            searchoptions: {sopt: ['cn']},
            formatter: function (value, grid, row, index) {
                return '<a class="a_style" onclick="checkDefect(' + row.id + ')">' + row.defectCode + '</a>'
            }
        }, {
            name: "defectSummary",
            index: "defectSummary",
            align: 'left',
            width: 300,
            searchoptions: {sopt: ['cn']},
            formatter: function (value, grid, row, index) {
                var data = '';
                var classColor = 'classColor';
                var classColor1 = 'classColor';
                for (var i = 0, len = severityLevelList.length; i < len; i++) {
                    if (row.severityLevel == severityLevelList[i].value) {
                        classColor += row.severityLevel;
                        data += "<div><span class=" + classColor + ">" + severityLevelList[i].innerHTML + "</span>";
                        break;
                    }
                }
                for (var i = 0, len = emergencyLevelList.length; i < len; i++) {
                    if (row.emergencyLevel == emergencyLevelList[i].value) {
                        classColor1 += row.emergencyLevel;
                        data += "<span class=" + classColor1 + ">" + emergencyLevelList[i].innerHTML + "</span>";
                        break;
                    }
                }
                data += "<span>" + row.defectSummary + "</span></div>";
                return data
            }
        }, {
            name: "defectStatus",
            index: "defectStatus",
            align: 'center',
            width: 100,
            stype: 'select',
            searchoptions: {
                value: function () {
                    var arr = {0: "请选择"};
                    for (var i = 0, len = defectStatusList.length; i < len; i++) {
                        arr[defectStatusList[i].value] = defectStatusList[i].innerHTML;
                    }
                    return arr;
                },
                sopt: ['cn']
            },
            formatter: function (value, grid, row, index) {
                for (var i = 0, len = defectStatusList.length; i < len; i++) {
                    if (row.defectStatus == defectStatusList[i].value) {
                        var _status = "<input type='hidden' id='list_defectStatus' value='" + defectStatusList[i].innerHTML + "'>";
                        return defectStatusList[i].innerHTML + _status
                    }
                }
            }
        }, {
            name: "requirementCode",
            index: "requirementCode",
            align: 'center',
            width: 150,
            searchoptions: {sopt: ['cn']}
        }, {
            name: "systemName",
            index: "systemName",
            align: 'center',
            width: 150,
            searchoptions: {sopt: ['cn']}
        }, {
            name: "windowName",
            index: "windowName",
            align: 'center',
            width: 100,
            searchoptions: {sopt: ['cn']}
        }, {
            name: "assignUserName",
            index: "assignUserName",
            align: 'center',
            width: 100,
            searchoptions: {sopt: ['cn']}
        }, {
            name: "submitUserName",
            index: "submitUserName",
            align: 'center',
            width: 200,
            searchoptions: {sopt: ['cn']},
            formatter: function (value, grid, row, index) {
                value = value == null ? '' : value + "<span>&nbsp;|&nbsp;</span>";
                return value + row.submitDate;
            }
        }, {
            name: "opt",
            index: "opt",
            align: 'center',
            width: 400,
            search: false,
            sortable: false,
            formatter: function (value, grid, row, index) {
                var span_ = "<span>&nbsp;|&nbsp;</span>";
                var row_data = JSON.stringify(row).replace(/"/g, '&quot;');
                var a1 = '<a  href="javascript:void(0);" class="edit-opt" onclick="checkDefectList(' + row.id + ')">查看</a>';
                var d = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 1 + ')">转交</a>';
                var f = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 2 + ')">驳回</a>';
                var _dev_a1 = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 3 + ')">修复完成</a>';
                
                var s_w = '<a  href="javascript:void(0);" class="edit-opt" onclick="setCommissioningWindow(' + row_data + ',1)">设置投产窗口</a>';
                var s_c = '<a  href="javascript:void(0);" class="edit-opt" onclick="setCommissioningWindow(' + row.id + ',2)">关闭</a>';
                
                var opt_status = [];
                var is_TransferOpt = false;
                var manageUsers = row.manageUsers?row.manageUsers.split(","):[];
                if(manageUsers.indexOf(""+currentUserId) != -1){  //项目管理岗开放转交按钮
                    if (defectSend == true) {
                        opt_status.push(d); //转交
                        is_TransferOpt = true;
                    }
                }
                if (row.assignUserId == currentUserId) {  //主修复人
//                    var opt_status = [];
                    var f1 = '<a  href="javascript:void(0);" class="edit-opt" onclick="affirmDefectModal(' + row.id + ')">确认缺陷</a>';
                    // 待确认状态 开发：编辑 转交 确认 驳回（ 测试： 编辑，撤销 ，关闭）
                    if (row.defectStatus == 2) {  //待确认
                        if (defectSend == true) {
                            if(!is_TransferOpt){
                                opt_status.push(d); //转交
                            }
                        }
                        if (defectAffirm == true) {
                            opt_status.push(f1);  //确认缺陷
                        }
                        if (defectReject == true) {
                            opt_status.push(f); //驳回
                        }
                    }

                    // 修复中 已挂起：修复完成和驳回
                    if (row.defectStatus == 4 || row.defectStatus == 8) {
                        if (defectRepairComplete == true) {
                            opt_status.push(_dev_a1);// 修复完成
                        }
                        if (defectReject == true) {
                            opt_status.push(f);// 驳回
                        }
                    }

                    // 重新打开： 确认、驳回按钮
                    if (row.defectStatus == 10) {
                        if (defectAffirm == true) {
                            opt_status.push(f1);
                        }
                        if (defectReject == true) {
                            opt_status.push(f);
                        }
                    }

                    if (row.defectStatus != 4 && row.defectStatus != 2 && row.defectStatus != 8 && row.defectStatus != 10) {
                        var opt = a1;
                        defectView == true ? opt : opt = '';
                        return opt;
                    }
                    if(row.defectSource == 5){  //缺陷来源是生产
                        if(row.defectStatus != 7){   //状态不是关闭的缺陷
                            opt_status.push(s_w);
                        }
                    	if(row.requirementFeatureStatus && row.requirementFeatureStatus == '03'){  //  devtaskStatus == 1 开发任务状态是“实施完成”
                            opt_status.push(s_c);
                        }
                    }
                    return opt_status.join(span_);
                } else {
                	if(is_TransferOpt){
                		return opt_status;
                	}else{
                		return '';
                	}
                }
            }
        }],
        loadComplete: function () {
            $("#loading").css('display', 'none');
            defectInfoForExportObj = $("#defectList").jqGrid('getGridParam', "postData");
        },
        beforeRequest: function () {
            $("#loading").css('display', 'block');
        },
        loadError: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    }).trigger("reloadGrid");
    //$("#defectList").jqGrid('filterToolbar', {searchOperators: true});
}

//设置投产窗口
function setCommissioningWindow(data,status){
    if(status == 1){
    	$('#set_defect_ComWindow_id').val('');
        $('#setComWindow_value').val('');
        $('#setComWindow_name').val('');
        
        $('#set_defect_ComWindow_id').val(data.id);
        $('#setComWindow_value').val(data.commissioningWindowId);
        $('#setComWindow_name').val(data.windowName);
        $('#setComWindowModal').modal('show');
    }else{
         layer.confirm('确定要关闭吗?',{
	    	btn: ['确定', '取消'], 
	    	icon: 0,
	    	title: "提示信息"
         },function(){
        	 layer.closeAll('dialog');
        	 $("#loading").css('display', 'block');
        	 $.ajax({
    	        url: "/devManage/defect/closeDefect",
    	        type: "post",
    	        data: {
    	            defectId: data,
    	        },
    	        success: function (data) {
    	            if (data.status == 1) {
    	            	initTable();
    	            	layer.alert('缺陷已关闭!', {
        	                icon: 1,
        	                title: "提示信息"
        	            });
    	            }
    	            $("#loading").css('display', 'none');
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
}

// 提交投产窗口
function commit_set_Window(){
	if(!$('#setComWindow_value').val()){
		$('#setComWindowModal').modal('hide');
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
        url: "/devManage/defect/updateCommissioningWindowId",
        type: "post",
        data: {
            defectId: $('#set_defect_ComWindow_id').val(),
            commissioningWindowId: $('#setComWindow_value').val()
        },
        success: function (data) {
            if (data.status == 1) {
            	initTable();
            	layer.alert('设置成功!', {
	                icon: 1,
	                title: "提示信息"
	            });
            	$('#setComWindowModal').modal('hide');
            }
            $("#loading").css('display', 'none');
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

// 获取缺陷
function getDefectEntity(defectId, Opt, mid) {

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
                optFun(data.defectInfo, Opt, mid);
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

function optFun(defectInfo, Opt, mid) {
    switch (Opt) {
        case 1:
            send(defectInfo, mid);
            break;
        case 2:
            rejectDefectModal(defectInfo);
            break;
        case 3:
            dev_repairCompleteModal(defectInfo);
            break;
        default:
            break;
    }
}

function findByStatus() {
    var ids = $("#defectStatusList").val();
    var arr = ids.split(",");
    $("#defectStatus option").each(function (i, n) {
        for (var j = 0, len = arr.length; j < len; j++) {
            if (n.value == arr[j]) {
                n.selected = true;
            }
        }
    });
}

var is_first_search = false;
function searchInfo() {
	if(!is_first_search){
		$('#list_body2').remove();
		$('#list_body').show();
		initTable();
		is_first_search = true;
		return;
	}
    /*$(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
    $(".ui-search-toolbar .ui-search-input select").val('0');*/

    $("#loading").css('display', 'block');
    
    $("#defectList").jqGrid('setGridParam', {
        url: defectUrl + "defect/list",
        postData: {
            defectCode: $.trim($("#defectCode").val()),
            defectSummary: $.trim($("#defectSummary").val()),
            submitDate: $("#submitDate").val(),
            defectStatusList: $("#defectStatus").val() != null ? $("#defectStatus").val().toString() : '',
            defectSourceStr: $("#defectSource").val() != null ? $("#defectSource").val().toString() : '',
            severityLevelStr: $("#severityLevel").val() != null ? $("#severityLevel").val().toString() : '',
            emergencyLevelStr: $("#emergencyLevel").val() != null ? $("#emergencyLevel").val().toString() : '',
            defectTypeStr: $("#defectType").val() != null ? $("#defectType").val().toString() : '',
            requirementCodeStr: $.trim($("#requirementCode").val()),
            systemIdStr: $("#sel_systemId").val() != null ? $("#sel_systemId").val().toString() : '',
            commissioningWindowIdStr: $("#windowId").val() != null ? $("#windowId").val().toString() : "",
            submitUserIdStr: $("#submitUserId").val() != null ? $("#submitUserId").val().toString() : "",
            assignUserIdStr: $("#sel_assingUserId").val() != null ? $("#sel_assingUserId").val().toString() : "",
            filters: ""
        },
        page: 1,
        loadComplete: function () {
            $("#loading").css('display', 'none');
            defectInfoForExportObj = $("#defectList").jqGrid('getGridParam', "postData");
        }
    }).trigger("reloadGrid"); //重新载入
}
/*再次打开*/
function againModal(rows, that) {
    reset_opt();
    formFileList = [];
    var updateStatus = '';
    for (var i = 0, len = defectStatusList.length; i < len; i++) {
        if (2 == defectStatusList[i].value) {
            updateStatus = defectStatusList[i].innerHTML;
            break;
        }
    }
    var row = JSON.parse(rows.replace(/\n/g, "\\r\\n"));
    var status = $(that).parent().parent().find("#list_defectStatus").val();
    layer.confirm("确认再次打开该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        opt_updateDefectStatus(row.id, 2, status, updateStatus);
    })
}

/*撤销*/
function repeal(rows, that) {
    layer.confirm("确认撤销该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        var updateStatus = '';
        for (var i = 0, len = defectStatusList.length; i < len; i++) {
            if (6 == defectStatusList[i].value) {
                updateStatus = defectStatusList[i].innerHTML;
                break;
            }
        }

        var row = JSON.parse(rows.replace(/\n/g, "\\r\\n"));
        var status = $(that).parent().parent().find("#list_defectStatus").val();
        opt_updateDefectStatus(row.id, 6, status, updateStatus);
    })
}

/*关闭*/
function opt_close(rows, that) {
    layer.confirm("确认关闭该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        var row = JSON.parse(rows.replace(/\n/g, "\\r\\n"));
        opt_updateDefectStatus(row.id, 7);
    })
}

/*修改缺陷状态*/
function opt_updateDefectStatus(defectId, defectStatus) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: defectUrl + "defect/updateDefectStatus",
        type: "post",
        data: {
            defectId: defectId,
            defectStatus: defectStatus
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                layer.alert('操作成功 ！', {
                    icon: 1,
                    title: "提示信息"
                });
                initTable();
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

/*修复完成 状态变为 待检测*/
function repairCompleteModal(rows, that) {
    reset_opt();
    formFileList = [];
    var _repairCompleteDefectData = JSON.parse(rows.replace(/\n/g, "\\r\\n"));
    $("#opt_solution_defectId").val(_repairCompleteDefectData.id);
    $("#opt_solution_submitUserId").val(_repairCompleteDefectData.submitUserId);
    $("#opt_solution_oldAssignUserId").val(_repairCompleteDefectData.assignUserId);
    $(".solution").css("display", "block");
    $("#opt_Restored").css("display", "inline-block");
    $("#rejectDiv").modal("show");
}

/*修复完成，请求后台操作*/
function repairComplete(defectStatus) {
    $('#rejectDivForm').data('bootstrapValidator').validate();
    if (!$('#rejectDivForm').data('bootstrapValidator').isValid()) {
        return;
    }

    var solution = $.trim($("#opt_solution").find("option:selected").val());
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
                defectId: $("#opt_solution_defectId").val(),
                defectRemark: htmlEncodeJQ($.trim($("#opt_defectRemark").val())),
                solveStatus: solution,
                defectStatus: defectStatus,
                submitUserId: $("#opt_solution_submitUserId").val(),
                oldAssignUserId: $("#opt_solution_oldAssignUserId").val(),
                actualStartDate: $("#check_dev_task_startDate").val(),
                actualEndDate: $("#check_dev_task_endDate").val(),
                actualWorkload: $("#check_dev_task_workNum").val()
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    remarkUploadFile(data.logId);
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
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

// 修改工作任务的状态
function updateDevTaskStatus() {
    var obj = {};
    obj.id = $("#check_dev_task_id").val();
    obj.actualStartDate = $("#check_dev_task_startDate").val();
    obj.actualEndDate = $("#check_dev_task_endDate").val();
    obj.actualWorkload = $("#check_dev_task_workNum").val();
    var handle = JSON.stringify(obj);
    $.ajax({
        method: "post",
        url: "/devManage/worktask/HandleDev",
        data: {
            handle,
            "HattachFiles": ""
        },
        success: function (data) {
            $("#loading").css('display', 'none');
        }
    });
}


/*再次提交 操作: 状态变为待确认 */
var __defectAgainData = {};
function submitAgainModal(rows, that) {
    reset_opt();
    formFileList = [];
    var data = rows.replace(/\n/g, "\\r\\n");
    var row = JSON.parse(data);
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
            defectStatus: 2
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
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

/*确认操作：状态改为处理中*/
function affirm(defectId) {

    $("#loading").css('display', 'block');
    $.ajax({
        url: defectUrl + "defect/updateDefectwithTBC",
        type: "post",
        data: {
            defectId: defectId,
            defectStatus: 4,
            submitUserId: '',
            oldAssignUserId: $("#dev_checkAssignUserId").val()
        },
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
                initTable();
                $("#rejectDiv").modal("hide");
                $("#new_WorkTaskModal").modal("hide");
            } else if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
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

/*驳回操作 状态变为 拒绝*/
var rejectDefectData = {};
function rejectDefectModal(defectInfo) {
    reset_opt();
    formFileList = [];
    rejectDefectData = defectInfo;
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
                defectRemark: htmlEncodeJQ($.trim($("#opt_defectRemark").val())),
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
                } else if (data.status == 1) {
                    remarkUploadFile(data.logId);
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
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

/*转交*/
var sendRow = {};
function send(defectInfo, mid) {
    reset_opt();
    formFileList = [];
    sendRow = defectInfo;
    $("#opt_assignUserId").val(sendRow.assignUserId);
    $("#opt_systemId").val(sendRow.systemId);
    $("#opt_submitDefect").css("display", "inline-block");
    $(".transmit").css("display", "block");
    $("#rejectDiv").modal("show");
}

/*转交操作 ---> 提交 还是待确认的状态*/
function opt_submitDefect() {
    var assignUserId = $("#opt_assignUserId").val();
    if (assignUserId == '' || sendRow.assignUserId == assignUserId) {
        layer.alert("请选择转派人!(不能是相同用户)", {
            icon: 2,
            title: "提示信息"
        })
    } else {
        $("#loading").css('display', 'block');
        $.ajax({
            url: defectUrl + "defect/updateDefectwithTBC",
            type: "post",
            data: {
                defectId: sendRow.id,
                defectRemark: htmlEncodeJQ($.trim($("#opt_defectRemark").val())),
                assignUserId: assignUserId,
                manageUserId:  $("#opt_manageUserId").val(),
                defectStatus: 2,
                submitUserId: sendRow.submitUserId,
                oldAssignUserId: sendRow.assignUserId
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    remarkUploadFile(data.logId);
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
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

/*编辑缺陷*/
function edit(row) {
    defectAttIds = [];
    testTaskModalStatus = "edit";
    submitDefectStatus = "edit";
    newDefect_reset();
    var rows = row.replace(/\n/g, "\\r\\n");
    var data = JSON.parse(rows);
    // 待测试
    /* if (data.testTaskId != null){
     getTestTask(data.testTaskId);
     }*/
    $("#edit_requirementCode").val(data.requirementCode);
    $("#edit_commissioningWindowId").val(data.commissioningWindowId);

    $("#edit_defectId").val(data.id);
    $("#edit_systemId").val(data.systemId);
    $("#edit_system_Name").val(data.systemName);
    $("#edit_repairRound").val(data.repairRound);
    $("#edit_testCaseId").val(data.testCaseId);
    // 测试案例
    $("#edit_testCaseName").val('');
    $("#edit_defectOverview").val(data.defectOverview);
    $("#edit_defectSummary").val(data.defectSummary);

    $("#edit_defectType option").each(function (i, n) {
        if (n.value == data.defectType) {
            n.selected = true;
        }
    });

    $("#edit_severityLevel option").each(function (i, n) {
        if (n.value == data.severityLevel) {
            n.selected = true;
        }
    });
    $("#edit_emergencyLevel option").each(function (i, n) {
        if (n.value == data.emergencyLevel) {
            n.selected = true;
        }
    });
    $("#edit_defectStatus option").each(function (i, n) {
        if (n.value == data.defectStatus) {
            n.selected = true;
        }
    });

    if (data.defectStatus == 1) {
        $("#edit_defectStatus").attr("disabled", "true");
    } else {
        $("#edit_defectStatus").attr("disabled", false);
    }

    $("#edit_assignUserId").val(data.assignUserId);
    $("#edit_submitUserId").val(data.submitUserId);
    $("#edit_assignUserName").val(data.assignUserName);
    $("#edit_newFileTable").val('');
    $(".selectpicker").selectpicker('refresh');

    if (data.defectStatus == 1) {
        $("#edit_submitDefect").css("display", "inline-block");
        $("#edit_stagDefect").css("display", "inline-block");
    } else {
        $("#edit_submit").css("display", "inline-block");
    }
    attList(data.id, "#edit_newFileTable");
    $("#editDefect").modal("show");
}

// 新建缺陷
function newDefect_btn() {
    newDefect_reset();
    testTaskModalStatus = "new";
    submitDefectStatus = "new";
    defectAttIds = [];
    $(".selectpicker").selectpicker('refresh');
    $("#newDefect").modal("show");
}

// 删除缺陷
function deleteDefect(rows) {

    layer.confirm("确定删除该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {

        layer.closeAll('dialog');

        var data = JSON.parse(rows.replace(/\n/g, "\\r\\n"));
        $("#loading").css('display', 'block');
        $.ajax({
            url: defectUrl + "defect/removeDefect",
            method: "post",
            data: {
                id: data.id
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    layer.alert('删除缺陷成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    initTable();
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

// 暂存缺陷
function stagDefect() {

    if (submitDefectStatus == "new") {
        $('#newDefectFrom').data('bootstrapValidator').validate();
        if (!$('#newDefectFrom').data('bootstrapValidator').isValid()) {
            return false;
        }
    } else if (submitDefectStatus == "edit") {
        $('#edit_DefectFrom').data('bootstrapValidator').validate();
        if (!$('#edit_DefectFrom').data('bootstrapValidator').isValid()) {
            return false;
        }
    }

    updateDefectStatus(1);
}

// 表格暂存的按钮
function stagDefect_a(row) {
    submitDefectStatus = "edit";
    var data = JSON.parse(row.replace(/\n/g, "\\r\\n"));
    $("#edit_defectId").val(data.id);
    updateDefectStatus(1);
}

// 列表的提交按钮
function submitDefect_a(row) {
    formFileList = [];
    var data = JSON.parse(row.replace(/\n/g, "\\r\\n"));
    submitDefectStatus = "edit";
    if (data.assignUserId == null) {
        layer.alert("必须选择指派人！", {
            icon: 2,
            title: "提示信息"
        })
    } else {
        $("#edit_defectId").val(data.id);
        $("#edit_testTaskId").val(data.testTaskId);
        $("#edit_testTaskName").val(data.testTaskName);
        $("#edit_systemId").val(data.systemId);
        $("#edit_system_Name").val(data.systemName);
        $("#edit_repairRound").val(data.repairRound);
        $("#edit_testCaseId").val(data.testCaseId);
        // 测试案例
        $("#edit_testCaseName").val('');
        $("#edit_defectOverview").val(data.defectOverview);
        $("#edit_defectSummary").val(data.defectSummary);

        updateDefectStatus(2);
    }
}

// 提交缺陷 状态变为：待确认
function submitDefect() {
    var assignUserId = '';
    if (submitDefectStatus == "new") {
        $('#newDefectFrom').data('bootstrapValidator').validate();
        if (!$('#newDefectFrom').data('bootstrapValidator').isValid()) {
            return false;
        }
        assignUserId = $("#new_assignUserId").val();
    } else if (submitDefectStatus == "edit") {
        $('#edit_DefectFrom').data('bootstrapValidator').validate();
        if (!$('#edit_DefectFrom').data('bootstrapValidator').isValid()) {
            return false;
        }
        assignUserId = $("#edit_assignUserId").val();
    }

    if (assignUserId == '') {
        layer.alert("必须选择指派人！", {
            icon: 2,
            title: "提示信息"
        })
    } else {
        updateDefectStatus(2);
    }

}

/*编辑：确定按钮*/
function opt_submit() {
    $('#edit_DefectFrom').data('bootstrapValidator').validate();
    if (!$('#edit_DefectFrom').data('bootstrapValidator').isValid()) {
        return false;
    }

    submitDefectStatus = "edit";
    var status = $.trim($("#edit_defectStatus").find("option:selected").val())
    updateDefectStatus(status);
}

// 新增 编辑 缺陷
function updateDefectStatus(defectStatus) {
    if (submitDefectStatus == "new") {
        $("#loading").css('display', 'block');
        // 新增缺陷
        $.ajax({
            type: "post",
            url: defectUrl + 'defect/insertDefect',
            dataType: "json",
            data: {
                assignUserId: $("#new_assignUserId").val(),
                testTaskId: $("#testTaskId").val(),
                requirementCode: $("#new_requirementCode").val(),
                systemId: $("#systemId").val(),
                testCaseId: $("#testCaseId").val(),
                commissioningWindowId: $("#new_commissioningWindowId").val(),
                defectSummary: $.trim($("#new_defectSummary").val()),
                repairRound: $.trim($("#new_repairRound").val()),
                defectOverview: $.trim($("#defectOverview").val()),
                defectType: $.trim($("#new_defectType").find("option:selected").val()),
                defectSource: $.trim($("#new_defectSource").find("option:selected").val()),
                severityLevel: $.trim($("#new_severityLevel").find("option:selected").val()),
                emergencyLevel: $.trim($("#new_emergencyLevel").find("option:selected").val()),
                defectStatus: defectStatus
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    uploadFile(data.defectId, data.logId);
                }
            }
        })
    } else if (submitDefectStatus == "edit") {
        // 编辑缺陷
        $("#loading").css('display', 'block');
        var defectId = $("#edit_defectId").val();
        $.ajax({
            type: "post",
            url: defectUrl + 'defect/updateDefect',
            dataType: "json",
            data: {
                id: defectId,
                assignUserId: $("#edit_assignUserId").val(),
                submitUserId: $("#edit_submitUserId").val(),
                testTaskId: $("#edit_testTaskId").val(),
                requirementCode: $("#edit_requirementCode").val(),
                systemId: $("#edit_systemId").val(),
                testCaseId: $("#edit_testCaseId").val(),
                commissioningWindowId: $("#edit_commissioningWindowId").val(),
                repairRound: $.trim($("#edit_repairRound").val()),
                defectSummary: $.trim($("#edit_defectSummary").val()),
                defectType: $.trim($("#edit_defectType").find("option:selected").val()),
                defectSource: $.trim($("#edit_defectSource").find("option:selected").val()),
                severityLevel: $.trim($("#edit_severityLevel").find("option:selected").val()),
                emergencyLevel: $.trim($("#edit_emergencyLevel").find("option:selected").val()),
                defectOverview: $.trim($("#edit_defectOverview").val()),
                defectStatus: defectStatus
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    if (data.defectId != null && data.defectId != undefined && data.logId != null && data.logId != undefined) {
                        uploadFile(data.defectId, data.logId);
                    }
                }
            }
        })
    }


}

// 清空
function reset() {
    $("#submitDate").val('');
    $("#defectCode").val('');
    $("#defectSummary").val('');
    $("#windowId").val('');
    $("#windowName").val('');
    $("#createDate").val('');
    $("#submitUserId").val('');
    $("#submitUserName").val('');
    $("#defectStatus").selectpicker('val', '0');
    $("#defectSource").selectpicker('val', '');
    $("#severityLevel").selectpicker('val', '');
    $("#emergencyLevel").selectpicker('val', '');
    $("#windowName").selectpicker('val', '');
    $("#requirementId").val('');
    $("#requirementName").val('');
    $("#requirementCode").val('');
    $("#sel_systemId").val('');
    $("#systemName").val('');
    $("#defectType").selectpicker('val', '');
    $("#taskCode").val('');
    $("#taskName").val('');
    $("#taskState").selectpicker('val', '');
    $("#sel_assingUserId").val('');
    $("#sel_assingUserName").val('');
    $(".collection").children("span").removeClass("fa-heart").addClass("fa-heart-o");
    winReset();
    reqReset();
    sysReset();
    userReset();
    $(".color1 .btn_clear").css("display", "none");
    /* $("#reqFiDAndName").css("display","none");*/
    $("#reqFiD").val("");
    $("#reqFName").val("");
}

// 新建/编辑清空
function newDefect_reset() {
    /*新建的字段*/
    $("#testTaskId").val('');
    $("#testTaskName").val('');
    $("#systemId").val('');
    $("#system_Name").val('');
    $("#testCaseId").val('');
    $("#testCaseName").val('');
    $("#new_repairRound").val('');
    $("#defectOverview").val('');
    $("#new_defectSummary").val('');
    $("#new_defectType").selectpicker('val', '');
    $("#new_defectSource").selectpicker('val', '');
    $("#new_defectSource").attr("disabled", false);
    $("#new_severityLevel").selectpicker('val', '');
    $("#new_emergencyLevel").selectpicker('val', '');
    $("#new_assignUserId").val('');
    $("#new_assignUserName").val('');
    $("#newFileTable tbody").html("");

    /*编辑的字段*/
    $("#edit_testTaskId").val('');
    $("#edit_testTaskName").val('');
    $("#edit_systemId").val('');
    $("#edit_system_Name").val('');
    $("#edit_testCaseId").val('');
    $("#edit_testCaseName").val('');
    $("#edit_repairRound").val('');
    $("#edit_defectOverview").val('');
    $("#edit_defectSummary").val('');
    $("#edit_defectType").selectpicker('val', '');
    $("#edit_defectStatus").selectpicker('val', '');
    $("#edit_defectSource").selectpicker('val', '');
    $("#edit_defectSource").attr("disabled", false);
    $("#edit_severityLevel").selectpicker('val', '');
    $("#edit_emergencyLevel").selectpicker('val', '');
    $("#edit_assignUserId").val('');
    $("#edit_assignUserName").val('');
    $("#edit_newFileTable tbody").html("");

    $("#edit_stagDefect").css("display", "none");
    $("#edit_Restored").css("display", "none");
    $("#edit_RejectDefect").css("display", "none");
    $("#edit_submitAgainDefect").css("display", "none");
    $("#edit_submitDefect").css("display", "none");
    $("#edit_closeDefect").css("display", "none");
    $("#edit_submit").css("display", "none");

    // 清空定义数据
    formFileList = [];
    editAttList = [];
}

// 操作清空
function reset_opt() {
    $("#opt_defectRemark").val('');
    $("#opt_assignUserId").val('');
    $("#opt_assignUserName").val('');
    $("#opt_solution_defectId").val('');
    $("#opt_solution_submitUserId").val('');
    $("#opt_solution_oldAssignUserId").val('');
    $("#dealLogDiv").empty();
    $(".defectDescribe").empty();

    $("#opt_solution_defectCode").val('');
    $("#dev_checkDefectID").val('');
    $("#optFileTable tbody").html("");
    $("#opt_rejection").selectpicker('val', '');
    $("#opt_solution").selectpicker('val', '');
    $("#opt_rejectionShow").selectpicker('val', '');

    $("#opt_rejectionShow").empty();

    $("#check_severityLevel").empty();
    $("#check_emergencyLevel").empty();
    $("#check_assignUserName").empty();
    $("#check_submitUserName").empty();
    $("#check_systemName").empty();
    $("#check_defectCode").empty();
    $("#check_defectType").empty();
    $("#check_defectSource").empty();
    $("#check_defectOverview").empty();
    $("#check_table tbody").html("");


    $("#dev_check_reqFetureCode").empty();
    $("#dev_check_reqFetureName").empty();
    $("#dev_check_devTaskCode").empty();
    $("#dev_check_devTaskName").empty();

    $("#dev_check_severityLevel").empty();
    $("#dev_check_emergencyLevel").empty();
    $("#dev_check_assignUserName").empty();
    $("#dev_check_submitUserName").empty();
    $("#dev_check_systemName").empty();
    $("#dev_check_defectCode").empty();
    $("#dev_check_defectType").empty();
    $("#dev_check_defectSource").empty();
    $("#dev_check_defectOverview").empty();
    $("#dev_check_table tbody").html("");

    $("#check_reqFetureCode").empty();
    $("#check_reqFetureName").empty();
    $("#check_devTaskCode").empty();
    $("#check_devTaskName").empty();

    $(".solution").css("display", "none");
    $("#opt_Restored").css("display", "none");
    $(".transmit").css("display", "none");
    $(".rejection").css("display", "none");
    $(".opt_rejectionDiv").css("display", "none");
    $("#opt_RejectDefect").css("display", "none");
    $("#opt_submitDefect").css("display", "none");
    $("#opt_submitAgainDefect").css("display", "none");
    $("#check_solveStatus_span").css("display", "none");
    $("#check_rejectReason_span").css("display", "none");
    $("#edit_submitDefect").css("display", "none");
    $("#edit_stagDefect").css("display", "none");

    $("#check_dev_task_startDate").val('');
    $("#check_dev_task_endDate").val('');
    $("#check_dev_task_workNum").val('');
    $("#check_dev_task_id").val('');
    $("#check_dev_task").css("display", "none");


    $("#checkfiles2").val('');
    $("#checkuploadFile_defect").val('');
    $("#defectRemarks").empty();
    $("#defectRemark").val("");
    $("#checkAttTable2").empty();

    dev_defect_reset();

    rejectDefectData = {};
    sendRow = {};
    __defectAgainData = {};
}

// 查看
function checkDefectList(defectId) {
    checkDefect(defectId)
}

/*查询当前信息*/
function checkDefect(defectId) {
    $("#loading").css('display', 'block');
    $("#titleOfwork").children("span").eq(0).click();
    reset_opt();
    /*收起弹框里面的日志内容*/
    _flag = 1;
    $("#checkDefectID").val(defectId);

    $(".down_content").css("display", "block");
    $("#defectBaseInfo .def_title .fa").removeClass("fa-angle-double-down");
    $("#defectBaseInfo .def_title .fa").addClass("fa-angle-double-up");

    $(".defectHandlingLog .def_title .fa").removeClass("fa-angle-double-up");
    $(".defectHandlingLog .def_title .fa").addClass("fa-angle-double-down");

    submitDefectStatus = "checkDefect";
    attList(defectId, "#check_table");

    down2();

    $.ajax({
        url: defectUrl + "defect/getDefectRecentLogById",
        method: "post",
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
                if (data.devTask != null && data.devTask != "undefined") {
                    $("#check_devTaskCode").html("<a class='a_style' onclick='getSee(" + data.devTask.id + "," + data.devTask.requirementFeatureId + ")'>" + data.devTask.devTaskCode + "</a>");
                    $("#check_devTaskName").html("<a class='a_style' onclick='getSee(" + data.devTask.id + "," + data.devTask.requirementFeatureId + ")'>" + data.devTask.devTaskName + "</a>");
                }

                if (data.feature != null && data.feature != "undefined") {
                    $("#check_reqFetureCode").html("<a class='a_style' onclick='showDevTask(" + data.feature.id + ")'>" + data.feature.featureCode + "</a>");
                    $("#check_reqFetureName").html("<a class='a_style' onclick='showDevTask(" + data.feature.id + ")'>" + data.feature.featureName + "</a>");
                }

                $("#checkDefectDiv").modal("show");
            }
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
// 查看缺陷备注
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
                    var _td_icon;
                    var _td_name = '<span>' + file_name + '</span>';
                    switch (file_type) {
                        case "doc":
                        case "docx":
                            _td_icon = '<img src="' + _icon_word + '" />';
                            break;
                        case "xls":
                        case "xlsx":
                            _td_icon = '<img src=' + _icon_excel + ' />';
                            break;
                        case "txt":
                            _td_icon = '<img src="' + _icon_text + '" />';
                            break;
                        case "pdf":
                            _td_icon = '<img src="' + _icon_pdf + '" />';
                            break;
                        default:
                            _td_icon = '<img src="' + _icon_word + '" />';
                            break;
                    }
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
//提交备注
function addDefectRemark() {
    var defectRemark = $.trim($("#defectRemark").val());
//	if(defectRemark==""||defectRemark==undefined){
//		layer.alert('备注信息不能为空！', {
//          icon: 2,
//          title: "提示信息"
//      });
//		return;
//	}
    if (defectRemark == '' && $("#checkfiles2").val() == '') {
        layer.alert('备注内容和附件不能同时为空！！！', {icon: 0});
        return;
    }
    var id = $.trim($("#checkDefectID").val());
    var obj = {};
    obj.defectRemark = $.trim($("#defectRemark").val());
    obj.defectId = id;
    var remark = JSON.stringify(obj);
    $.ajax({
        type: "post",
        url: "/devManage/defect/addDefectRemark",
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
            $("#defectRemarks").empty();
            $("#defectRemark").val("");
            $("#checkAttTable2").empty();

            checkDefect(id);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }


    });
}
// 数据字典
function dicDefectSelect(rows) {
    for (var i = 0, len = defectTypeList.length; i < len; i++) {
        if (rows.defectType == defectTypeList[i].value) {
            $(rows.checkDefectType).html(defectTypeList[i].innerHTML);
            break;
        }
    }

    for (var i = 0, len = defectSourceList.length; i < len; i++) {
        if (rows.defectSource == defectSourceList[i].value) {
            $(rows.checkDefectSource).html(defectSourceList[i].innerHTML);
            break;
        }
    }

    for (var i = 0, len = defectStatusList.length; i < len; i++) {
        if (rows.defectStatus == defectStatusList[i].value) {
            $(rows.checkDefectStatus).html(defectStatusList[i].innerHTML);
            break;
        }
    }

    for (var i = 0, len = severityLevelList.length; i < len; i++) {
        if (rows.severityLevel == severityLevelList[i].value) {
            var classColor = "classColor" + rows.severityLevel;
            var severityLevelData = "<span class='" + classColor + "'>" + severityLevelList[i].innerHTML + "</span>";
            $(rows.checkSeverityLevel).html(severityLevelData);
            break;
        }
    }

    for (var i = 0, len = emergencyLevelList.length; i < len; i++) {
        if (rows.emergencyLevel == emergencyLevelList[i].value) {
            var classColor = "classColor" + rows.emergencyLevel;
            var emergencyLevellData = "<span class='" + classColor + "'>" + emergencyLevelList[i].innerHTML + "</span>";
            $(rows.checkEmergencyLevel).html(emergencyLevellData);
            break;
        }
    }
}

// 控制页面选择栏 收缩
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

// 日志显示
function down2() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: defectUrl + "defect/getDefectLogById",
        method: "post",
        data: {
            defectId: $("#checkDefectID").val()
        },
        success: function (data) {
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
                                                    childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />';

                                                }
                                            } else {
                                                var oldName = logDetail[j].oldValue;
                                                var newName = logDetail[j].newValue;

                                                var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                                                childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />';

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
                                            classType = classPath(LogFileType, classType);

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
                                            classType = classPath(LogFileType, classType);

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
                                            classType = classPath(LogFileType, classType);

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
                                            classType = classPath(LogFileType, classType);

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

// 日志字段解析
function logDetailList(fieldName, oldName, newName) {
    var arr = [];
    switch (fieldName) {
        case "缺陷状态":
            for (var k = 0; k < defectStatusList.length; k++) {
                if (defectStatusList[k].value != '') {
                    if (oldName == defectStatusList[k].value) {
                        oldName = defectStatusList[k].innerHTML;
                    }
                    if (newName == defectStatusList[k].value) {
                        newName = defectStatusList[k].innerHTML;
                    }
                }
            }
            break;
        case "缺陷类型":
            for (var k = 0; k < defectTypeList.length; k++) {
                if (defectTypeList[k].value != '') {
                    if (oldName == defectTypeList[k].value) {
                        oldName = defectTypeList[k].innerHTML;
                    }
                    if (newName == defectTypeList[k].value) {
                        newName = defectTypeList[k].innerHTML;
                    }
                }
            }
            break;
        case "缺陷来源":
            for (var k = 0; k < defectSourceList.length; k++) {
                if (defectSourceList[k].value != '') {
                    if (oldName == defectSourceList[k].value) {
                        oldName = defectSourceList[k].innerHTML;
                    }
                    if (newName == defectSourceList[k].value) {
                        newName = defectSourceList[k].innerHTML;
                    }
                }
            }
            break;
        case "严重级别":
            for (var k = 0; k < severityLevelList.length; k++) {
                if (severityLevelList[k].value != '') {
                    if (oldName == severityLevelList[k].value) {
                        oldName = severityLevelList[k].innerHTML;
                    }
                    if (newName == severityLevelList[k].value) {
                        newName = severityLevelList[k].innerHTML;
                    }
                }
            }
            break;
        case "紧急程度":
            for (var k = 0; k < emergencyLevelList.length; k++) {
                if (emergencyLevelList[k].value != '') {
                    if (oldName == emergencyLevelList[k].value) {
                        oldName = emergencyLevelList[k].innerHTML;
                    }
                    if (newName == emergencyLevelList[k].value) {
                        newName = emergencyLevelList[k].innerHTML;
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
                    if (oldName == solveStatusList[k].value) {
                        oldName = solveStatusList[k].innerHTML;
                    }
                    if (newName == solveStatusList[k].value) {
                        newName = solveStatusList[k].innerHTML;
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

// 显示自定义字段
function showField(data) {
    if (data == null) {
        return;
    }
    $("#canEditField").empty();
    $("#devCanEditField").empty();
    if (data.length <= 0) {
        $("#canEditField").css("display", "none");
        $("#devCanEditField").css("display", "none");
    } else {
        $("#canEditField").css("display", "block");
        $("#devCanEditField").css("display", "block");
        for (var i = 0; i < data.length; i++) {
            var aLabel = '<div class="def_col_6 font_right fontWeihgt">' + data[i].label + '：</div>';
            var aLabelContent = '<div class="def_col_12">' + data[i].valueName + '</div>';
            $("#canEditField").append(aLabel, aLabelContent);
            $("#devCanEditField").append(aLabel, aLabelContent);
        }
    }
}
/*--------导出缺陷-----------*/
function exportDefect_btn() {
    var exportDefect_form = document.createElement("form");
    exportDefect_form.action = defectUrl + "defect/export";
    exportDefect_form.target = "_self";
    exportDefect_form.method = "post";
    exportDefect_form.style.display = "none";
    for (var x in defectInfoForExportObj) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = defectInfoForExportObj[x];
        exportDefect_form.appendChild(opt);
    }
    document.body.appendChild(exportDefect_form);
    exportDefect_form.submit();
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
            var _td_icon;
            var _td_name = '<span >' + file.name + '</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
            var _td_opt = '<td><a class="del-file-button" onclick="delFileWen(this)">删除</a></td>';
            switch (file_type) {
                case "doc":
                case "docx":
                    _td_icon = '<img src="' + _icon_word + '" />';
                    break;
                case "xls":
                case "xlsx":
                    _td_icon = '<img src=' + _icon_excel + ' />';
                    break;
                case "txt":
                    _td_icon = '<img src="' + _icon_text + '" />';
                    break;
                case "pdf":
                    _td_icon = '<img src="' + _icon_pdf + '" />';
                    break;
                case "png":
                case "jpeg":
                case "jpg":
                    _td_icon = '<img src="' + _icon_img + '"/>';
                    break;
                default:
                    _td_icon = '<img src="' + _icon_other + '" />';
                    break;
            }
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
function delFileWen(that) {
    var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
    /*	var url = $(that).parent().prev().children().children(".file-url").text();
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
    removeFileWeb(fileS3Key);
}

function removeFileWeb(fileS3Key) {
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
function default_list(){
	var data = [];
    $("#list2").jqGrid({
    	data:data,
        datatype: 'local',
        contentType: "application/json; charset=utf-8",
        mtype:"post",
        height: 'auto',
        multiselect : true,
        width: $(".content-table").width()*0.999,
        colNames: ['id', '缺陷编号', '缺陷摘要', '缺陷状态', '所属需求', '涉及系统', '投产窗口', '主修复人', '提出人', '操作'],
        colModel: [{name: 'id', index: 'id', hidden: true}, 
        	{
            name: "defectCode",
            index: "defectCode",
            align: 'center',
            width: 150
            
        }, {
            name: "defectSummary",
            index: "defectSummary",
            align: 'left',
            width: 300
            
        }, {
            name: "defectStatus",
            index: "defectStatus",
            align: 'center',
            width: 100
            
        }, {
            name: "requirementCode",
            index: "requirementCode",
            align: 'center',
            width: 150
        }, {
            name: "systemName",
            index: "systemName",
            align: 'center',
            width: 150
        }, {
            name: "windowName",
            index: "windowName",
            align: 'center',
            width: 100
        }, {
            name: "assignUserName",
            index: "assignUserName",
            align: 'center',
            width: 100
        }, {
            name: "submitUserName",
            index: "submitUserName",
            align: 'center',
            width: 200
           
        }, {
            name:'opt',
            index:'操作',
            align:"left",
            width:80,
            sortable:false,
        }],
       
        rowNum:10,
        rowTotal: 200,
        rowList : [10,20,30],
        rownumWidth: 40,
        pager: '#pager2',
        sortable:true,   //是否可排序
        sortorder: 'desc',
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true, //是否要显示总记录数
        loadComplete :function(){
            $("#loading").css('display','none');
        },
        beforeRequest:function(){
            $("#loading").css('display','block');
        }
    }).trigger("reloadGrid");
}

