/**
 * Description: 开发管理平台的缺陷管理
 * Author:liushan
 * Date: 2019/1/4 下午 1:55
 */

// 确认缺陷
function affirmDefectModal(defectId){
    $("#loading").css('display','block');
    reset_opt();
    submitDefectStatus = "devCheckDefect";
    attList(defectId,"#dev_check_table");
    $("#dev_checkDefectDiv").modal("show");

}

// 查看
function devCheckDefect(rows){

    $("#dev_systemId").val("");
    $("#dev_requirementCode").val("");
    $("#dev_systemId").val( rows.systemId );
    $("#dev_requirementCode").val( rows.requirementCode );
    $("#dev_defectSource").val( rows.defectSource );

    $("#dev_checkDefectID").val( rows.id );
    $("#dev_checkSubmitUserId").val( rows.submitUserId );
    $("#dev_checkAssignUserId").val( rows.assignUserId );

    $(".down_content").css("display", "block" );
    $("#dev_defectBaseInfo .def_title .fa").removeClass("fa-angle-double-down");
    $("#dev_defectBaseInfo .def_title .fa").addClass("fa-angle-double-up");

    $(".defectHandlingLog .def_title .fa").removeClass("fa-angle-double-up");
    $(".defectHandlingLog .def_title .fa").addClass("fa-angle-double-down");


    $("#dev_check_systemName").html(rows.systemName);
    $("#dev_check_defectCode").html(rows.defectCode);
    $("#dev_check_testTaskName").html(rows.testTaskName);
    $("#dev_check_testCaseName").html(rows.testCaseName);
    $("#dev_check_submitUserName").html(rows.submitUserName);
    $("#dev_check_assignUserName").html(rows.assignUserName);
    $("#dev_check_defectOverview").html(rows.defectOverview);
    $("#dev_check_defectSummary").html(rows.defectSummary);

    $("#dev_check_projectGroupName").text(isValueNull(rows.projectGroupName));//
    $("#dev_check_closeTime").text(isValueNull(rows.closeTime));
    $("#dev_check_assetSystemTreeName").text(isValueNull(rows.assetSystemTreeName));//
    $("#dev_check_detectedSystemVersionName").text(isValueNull(rows.detectedSystemVersionName));//
    $("#dev_check_repairSystemVersionName").text(isValueNull(rows.repairSystemVersionName));//
    $("#dev_check_expectRepairDate").text(isValueNull(rows.expectRepairDate));
    $("#dev_check_estimateWorkload").text(isValueNull(rows.estimateWorkload));
    $("#dev_check_Discovery_environment").text('');
    if(rows.discoveryEnvironment){
    	$('select#new_discovery_Environment').find('option').each(function(idx,val){
        	if(val.value == isValueNull(rows.discoveryEnvironment)){
        		$("#dev_check_Discovery_environment").text(val.innerText);
        	}
        })
    }
    $("#dev_check_rootCauseAnalysis").html(htmlDecodeJQ(rows.rootCauseAnalysis));

    rows["checkDefectType"] = "#dev_check_defectType";
    rows["checkDefectSource"] = "#dev_check_defectSource";
    rows["checkDefectStatus"] = "#dev_check_defectStatus";
    rows["checkSeverityLevel"] = "#dev_check_severityLevel";
    rows["checkEmergencyLevel"] = "#dev_check_emergencyLevel";
    dicDefectSelect(rows);

    if(rows.devTaskId != null || rows.requirementFeatureId != null){
        $.ajax({
            url:defectUrl+"defect/getDefectRecentLogById",
            method:"post",
            data:{
                defectId:rows.id
            },
            success:function(data){
                $("#loading").css('display','none');
                if (data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    if(data.devTask != null && data.devTask != undefined){
                        $("#dev_check_devTaskCode").html("<a class='a_style' onclick='getSee("+rows.devTaskId+","+rows.requirementFeatureId+")'>"+data.devTask.devTaskCode+"</a>");
                        $("#dev_check_devTaskName").html("<a class='a_style' onclick='getSee("+rows.devTaskId+","+rows.requirementFeatureId+")'>"+data.devTask.devTaskName+"</a>");
                    }

                    if(data.feature != null && data.feature != undefined){
                        $("#dev_check_reqFetureCode").html("<a class='a_style' onclick='showDevTask("+rows.requirementFeatureId+")'>"+data.feature.featureCode+"</a>");
                        $("#dev_check_reqFetureName").html("<a class='a_style' onclick='showDevTask("+rows.requirementFeatureId+")'>"+data.feature.featureName+"</a>");
                    }
                }
            },
            error:function(){
                $("#loading").css('display','none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        });
    }
	openNewWindowCheckImg();
}

// 驳回缺陷
function dev_rejectDefectModal(){
    $("#dev_checkDefectDiv").modal("hide");
    var id = $("#dev_checkDefectID").val();
    var submitUserId = $("#dev_checkSubmitUserId").val();
    var assignUserId = $("#dev_checkAssignUserId").val();
    var rows = {id:id,submitUserId:submitUserId,assignUserId:assignUserId};
    rejectDefectModal(rows);
}

// 待检测状态：修复完成
function dev_repairCompleteModal(rows){
    reset_opt();
    formFileList = [];
    $("#opt_solution_defectId").val(rows.id);
    $("#opt_solution_submitUserId").val(rows.submitUserId);
    $("#opt_solution_oldAssignUserId").val(rows.assignUserId);
    $("#opt_solution_defectCode").val(rows.defectCode);
    $("#opt_solution_systemId").val(rows.systemId);
    $("#opt_solution_systemName").val(rows.systemName);

    $("#check_dev_task_id").val(rows.devTaskId);
    var workTask = getWorkTask(rows.devTaskId);
    var actualStartDate = workTask.actualStartDate;
    if (actualStartDate != null && actualStartDate != undefined){
        $("#check_dev_task_startDate").val(datFmt(new Date(actualStartDate),"yyyy年MM月dd日"));
    } else {
        $("#check_dev_task_startDate").val(datFmt(new Date(),"yyyy年MM月dd日"));
    }

    $(".solution").css("display","block");
    $("#check_dev_task").css("display","block");
    $("#rejectDiv").modal("show");
}

// 待检测状态：根据选中解决情况，显示不同按钮
function solveStatusShowBtu(){
    var opt_solution = $("#opt_solution").find("option:selected").val();
    if (opt_solution == 1){
        $("#opt_Restored").css("display","none");
        $(".opt_DelayRepair").css("display","inline-block");
    } else if(opt_solution == 2){
        $("#opt_Restored").css("display","inline-block");
        $(".opt_DelayRepair").css("display","none");
    } else {
        $(".opt_DelayRepair").css("display","none");
        $("#opt_Restored").css("display","none");
    }
}

// 延后解决并创建一个开发任务
function DelayRepairAndCreateDevTaskSelect(){
    $('#rejectDivForm').data('bootstrapValidator').validate();
    if(!$('#rejectDivForm').data('bootstrapValidator').isValid()){
        return;
    }

    DelayRepairAndCreateDevTask();

    //$("#rejectDiv").modal("hide");
    $("#new_DevTaskModal").modal("show");
}

// 确认缺陷并 创建一个工作任务
function createWorkTask(){
	$( "#works" ).empty();
	//来源是生产问题            提示自动创建任务   
	if($('#dev_defectSource').val() == 5){
		$("#featureCode").val("自动创建开发任务和测试任务").removeAttr("onclick").addClass("bor_shadow_none");
//		$("#ataskName").attr("readonly");
//		$("#taskOverview").attr("readonly");
		$("#devCheckStatus_body").show();
	}else{
		$("#featureCode").val("").attr("onclick","showAdPopup()").removeClass("bor_shadow_none");
//		$("#ataskName").removeAttr("readonly");
//		$("#taskOverview").removeAttr("readonly");
		$("#devCheckStatus_body").hide();
	}
	_addworkFile=[];
    $("#dev_checkDefectDiv").modal("hide");
	$("#new_WorkTask_FileTable").empty();
	$("#addfiles").val("");
	$("#taskUser").empty();
	$("#ataskName").val("");
	$("#taskOverview").val("");
	 $("#devTaskPriority").selectpicker('val', '');
	$("#startWork").val("");
	$("#startWork").val(datFmt(new Date(),"yyyy-MM-dd"));
	$("#endWork").val("");
	$("#workLoad").val("");
    $("#new_taskUserId").val(currentUserId);
    $("#new_taskUser").val(currentUserName);
    
    $("#ataskName").val(defectSummary_msg);
    $("#taskOverview").val(defectOverview_msg);
	//getUserName();
	var systemId=$("#dev_systemId").val();
	var requirementCode=$("#dev_requirementCode").val();
	 $.ajax({
        method:"post",
        url:"/devManage/devtask/getReqFeatureByReqCodeAndSystemId",
        data:{
            "requirementCode":requirementCode,
            "systemId":systemId
        },
        success : function(data) {
            if(data.toString() != '{}' && data.data != null ){
                if( data.data.length > 0 && data.data.length<2){
                    $("#featureCode").val(data.data[0].featureName);
                    $("#Attribute").attr("featureCode",data.data[0].id);
                    $("#Attribute").attr("commissioningWindowId",data.data[0].commissioningWindowId);
                    $("#Attribute").attr("requirementFeatureStatus",data.data[0].requirementFeatureStatus);
                    if(data.data[0].planStartDate!=undefined){
                        $("#startWork").val(datFmt(new Date(data.data[0].planStartDate),"yyyy-MM-dd"));
                    }else{
                        $("#startWork").val("");
                    }
                    if(data.data[0].estimateWorkload!=undefined){
                        $("#workLoad").val(data.data[0].estimateWorkload);
                    }else{
                        $("#workLoad").val("");
                    }
                    if(data.data[0].planEndDate!=undefined){
                        $("#endWork").val(datFmt(new Date(data.data[0].planEndDate),"yyyy-MM-dd"));
                    }else{
                        $("#endWork").val("");
                    }
                }
                //$('#featureCode').removeAttr('onclick');//去掉a标签中的onclick事件
            }
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
	workModalType = 'new';
    $("#new_WorkTaskModal").modal("show");
}
//根据开发工作任务ID获取详细的开发工作任务
function getWorkTask(id){
    var workTask = {};
    if(id == null){
        return workTask;
    }
    $.ajax({
        type: "post",
        url:defectUrl+'worktask/getWorkTask',
        dataType: "json",
        async: false, // 同步
        data:{
            id:id
        },
        success: function(data) {
            workTask = data;
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
    return workTask;
}

// 清空
function dev_defect_reset(){
    $(".opt_DelayRepair").css("display","none");
}

//日期转换
function datFmt(date,fmt) { // author: meizz
    var o = {
        "M+": date.getMonth() + 1, // 月份
        "d+": date.getDate(), // 日
        "h+": date.getHours(), // 小时
        "m+": date.getMinutes(), // 分
        "s+": date.getSeconds(), // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3), // 季度
        "S": date.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};