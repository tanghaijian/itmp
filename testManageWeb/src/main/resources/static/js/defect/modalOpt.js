/**
 * Description: 弹框的操作
 * Author:liushan
 * Date: 2019/1/9 上午 9:50
 */



//模糊搜索配置
function search_settings(){
	var search_arr = [
		{
			ele : 'new_assignUserName',
			userId : 'new_assignUserId',
		},
		{
			ele : 'new_testUserName',
			userId : 'new_testUserId',
		},
		{
			ele : 'new_developer',
			userId : 'new_developerId',
		},
		{
			ele : 'edit_testUserName',
			userId : 'edit_testUserId',
		},
		{
			ele : 'edit_developer',
			userId : 'edit_developerId',
		},
		{
			ele : 'edit_assignUserName',
			userId : 'edit_assignUserId',
		},
		{
			ele : 'opt_assignUserName',
			userId : 'opt_assignUserId',
		},
	]
	search_arr.map(function(val){
		fuzzy_search_radio2({
			ele : val.ele, 
			url : '/testManage/testtask/getUserByNameOrACC', 
			top : '28px', 
			name : 'userName', 
			account : 'userAccount', 
			id : 'id', 
			userId : $("#"+val.userId), 
			rows : 'userInfo', 
		});
	})
}





/*--------------工作任务操作---------------*/

function selectTestTask() {
    var rowData = $("#testTaskTable").bootstrapTable('getSelections')[0];
    if (typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据", {
            icon: 2,
            title: "提示信息"
        })
    } else {
        if (testTaskModalStatus == "new") {
        	var systemId = $("#systemId").val();
//            $("#new_defectSource option").each( function (i, n) {
//                if (n.value == rowData.testStage) {
//                    n.selected = true;
            $("#new_defectSource").selectpicker("val", rowData.testStage).change();
            $("#new_defectSource").attr("disabled", "disabled");
//                }
//            });

            $("#testTaskId").val(rowData.id);
            $("#testTaskName").val(rowData.testTaskName);
            $('#testTaskName').parent().css("position", "relative");
            $('#testTaskName').parent().append("<span onclick='clearContent3(this)' class='btn_clear' style='display:block;'></span>");
            $("#systemId").val(rowData.systemId);
            $("#systemCode").val(rowData.systemCode);
            $("#system_Name").val(rowData.systemName).change(function () {
                
            }).change();
            if(systemId!=rowData.systemId){
            	$("#new_assignUserId").val('');
                $("#new_assignUserName").val('');
                $("#projectGroupId").val('');
                $("#assetSystemTreeId").val('');
                $("#detectedSystemVersionId").selectpicker('val','');
        		$("#repairSystemVersionId").selectpicker('val','');                
            }
            
            $("#new_windowId").val(rowData.commissioningWindowId);
            $("#new_windowName").val(rowData.windowName).change();
            $("#new_windowName").attr("disabled", "disabled");

            $("#new_requirementCode").val(rowData.requirementCode);
//            $("#new_commissioningWindowId").val(rowData.commissioningWindowId);
            $("#testTaskModal").modal("hide");
        } else if (testTaskModalStatus == "edit") {
        	var systemId = $("#edit_systemId").val();
//            $("#edit_defectSource option").each( function (i, n) {
//                if (n.value == rowData.testStage) {
//                    n.selected = true;
            $("#edit_defectSource").selectpicker("val", rowData.testStage).change();
            $("#edit_defectSource").attr("disabled", "true");
//                }
//            });

            $("#edit_testTaskId").val(rowData.id);
            $("#edit_testTaskName").val(rowData.testTaskName);
            $("#edit_testTaskName").next().css("display", "block");
            $("#edit_systemId").val(rowData.systemId);
            $("#edit_systemCode").val(rowData.systemCode);
            $("#edit_system_Name").val(rowData.systemName).change(function () {
            	
            }).change();
            if(systemId!=rowData.systemId){
        		$("#edit_assignUserId").val('');
        		$("#edit_assignUserName").val('');
        		$("#edit_testUserId").val('');
        		$("#edit_testUserName").val('');
        		$("#edit_developerId").val('');
        		$("#edit_developer").val('');
        		$("#edit_projectGroupId").val('');
        		$("#edit_assetSystemTreeId").val('');
        		$("#edit_detectedSystemVersionId").selectpicker('val','');
        		$("#edit_repairSystemVersionId").selectpicker('val','');
        		
        	}

            $("#edit_windowId").val(rowData.commissioningWindowId);
            $("#edit_windowName").val(rowData.windowName).change();
            $("#edit_windowName").attr("disabled", "disabled");

            $("#edit_requirementCode").val(rowData.requirementCode);
//            $("#edit_commissioningWindowId").val(rowData.commissioningWindowId);
            $("#testTaskModal").modal("hide");
        }
        $('.selectpicker').selectpicker('refresh');
    }
}

function clearContent3(that) {
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display", "none");
//	$("#new_windowName").removeClass( "canntClick" );
    $('#systemId').val('');
    $("#system_Name").val('');
    $(that).parent().parent().parent().next().children().removeClass('has-success');
    $(that).parent().parent().parent().next().children().addClass('has-error');
    $(that).parent().parent().parent().next().children().find('i').removeClass('glyphicon-ok');
    $(that).parent().parent().parent().next().children().find('i').addClass('glyphicon-remove');
    $('#new_defectSource').removeAttr("disabled");
    $("#new_defectSource").val('').change();
    $("#new_defectSource").selectpicker('refresh');
    $('#new_windowName').removeAttr("disabled");
    $("#new_windowName").val('').change();
}

function testTaskInit() {
    reset();
    $("#donginguserId").val(currentUserId);
    $("#donginguser").val(currentUserName);
    $("#taskState").selectpicker('val', '2');
    $("#taskState").selectpicker('refresh');
    testTaskShow();
    $("#testTaskModal").modal("show");
}


/*----------------人员操作-----------------*/
/*function userInit() {
    userReset();
    $("#userSysRole").css("display", "none");
    userTableShowSel();
}

function userTableShowSel() {
    var notWithUserID = '';
    var systemId = '';
    if (userModalStatus == "select") {
        getProject();
        userTableShow(null, systemId, false);
    } else if (userModalStatus == "workSelect") {
        getProject();
        userTableShow(null, null, false);
    } else if (userModalStatus == "select2") {
        getProject();
        userTableShow(null, null, false);
    } else if (userModalStatus == "select3") {
        getProject();
        userTableShow(null, null, false);
    } else if (userModalStatus == "select4") {
        getProject();
        userTableShow(null, null, false);
    } else {
        $("#userSysRole").css("display", "block");
        if (userModalStatus == "new") {
            notWithUserID = $("#new_assignUserId").val();
            systemId = $("#systemId").val();
            $("#userSystemName").val($("#system_Name").val());
        } else if (userModalStatus == "new2") { //新建测试人
            notWithUserID = $("#new_testUserId").val();
            systemId = $("#systemId").val();
            $("#userSystemName").val($("#system_Name").val());
        } else if (userModalStatus == "new3") { //新建开发人员
            notWithUserID = $("#new_developerId").val();
            systemId = $("#systemId").val();
            $("#userSystemName").val($("#system_Name").val());
        } else if (userModalStatus == "edit") {// 编辑中的转交
            notWithUserID = $("#edit_assignUserId").val();
            systemId = $("#edit_systemId").val();
            $("#userSystemName").val($("#edit_system_Name").val());
        } else if (userModalStatus == "edit2") {// 编辑中的转交
            notWithUserID = $("#edit_testUserId").val();
            systemId = $("#edit_systemId").val();
            $("#userSystemName").val($("#edit_system_Name").val());
        } else if (userModalStatus == "edit3") {// 编辑中的开发人员
            notWithUserID = $("#edit_developerId").val();
            systemId = $("#edit_systemId").val();
            $("#userSystemName").val($("#edit_system_Name").val());
        } else if (userModalStatus == "opt") {// 转交
            notWithUserID = $("#opt_assignUserId").val();
            systemId = $("#opt_systemId").val();
            $("#userSystemName").val($("#opt_systemId").attr("systemName"));
        }
        if (systemId == "") {
            layer.alert("请先选择系统！", {
                icon: 0,
                title: "提示信息"
            });
            return;
        }
        getProjectBySystemId(systemId);
        userTableShow(notWithUserID, systemId, true);
    }
    $("#userModal").modal("show");
}

function userTableShowSelect() {
    var notWithUserID = '';
    var systemId = '';
    if (userModalStatus == "select") {
        userTableShow(null, systemId, false);
    } else if (userModalStatus == "workSelect") {
        userTableShow(null, null, false);
    } else if (userModalStatus == "select2") {
        userTableShow(null, null, false);
    } else if (userModalStatus == "select3") {
        userTableShow(null, null, false);
    } else if (userModalStatus == "select4") {
        userTableShow(null, null, false);
    } else {
        if (userModalStatus == "new") {
            notWithUserID = $("#new_assignUserId").val();
            systemId = $("#systemId").val();
            $("#userSystemName").val($("#system_Name").val());
        } else if (userModalStatus == "new2") { //新建测试人
            notWithUserID = $("#new_testUserId").val();
            systemId = $("#systemId").val();
            $("#userSystemName").val($("#system_Name").val());
        } else if (userModalStatus == "new3") { //新建开发人员
            notWithUserID = $("#new_developerId").val();
            systemId = $("#systemId").val();
            $("#userSystemName").val($("#system_Name").val());
        } else if (userModalStatus == "edit") {// 编辑中的转交
            notWithUserID = $("#edit_assignUserId").val();
            systemId = $("#edit_systemId").val();
            $("#userSystemName").val($("#edit_system_Name").val());
        } else if (userModalStatus == "edit2") {// 编辑中的转交
            notWithUserID = $("#edit_testUserId").val();
            systemId = $("#edit_systemId").val();
            $("#userSystemName").val($("#edit_system_Name").val());
        } else if (userModalStatus == "edit3") {// 编辑中的开发人员
            notWithUserID = $("#edit_developerId").val();
            systemId = $("#edit_systemId").val();
            $("#userSystemName").val($("#edit_system_Name").val());
        } else if (userModalStatus == "opt") {// 转交
            notWithUserID = $("#opt_assignUserId").val();
            systemId = $("#opt_systemId").val();
            $("#userSystemName").val($("#opt_systemId").attr("systemName"));
        }
        userTableShow(notWithUserID, systemId, true);
    }
}
*/
function selectUserPost() {
    if ($("#project").val() == null) {
        layer.alert("请先选择项目！", {
            icon: 0,
            title: "提示信息"
        });
        $("#userPost").selectpicker('val', '');
        return;
    }
}

/*function commitUser() {
    if (userModalStatus == "select" || userModalStatus == "workSelect" || userModalStatus == "select2" || userModalStatus == "select3" || userModalStatus == "select4") { // 多选
        //var rowData = $("#userTable").bootstrapTable('getSelections');
        var rowData = select_rows;
        if (typeof(rowData) == 'undefined' || rowData.length == 0) {
            layer.alert("请至少选择一条数据", {
                icon: 2,
                title: "提示信息"
            });
            return false;
        } else {
            var ids = [];
            var names = [];
            for (var i = 0, len = rowData.length; i < len; i++) {
                ids.push(rowData[i].id);
                names.push(rowData[i].userName);
            }
            if (userModalStatus == "select") {
                $("#submitUserId").val(ids);
                $("#submitUserName").val(names).change(function () {
                    $(this).parent().children(".btn_clear").css("display", "block");
                }).change();
            } else if (userModalStatus == "workSelect") {
                $("#donginguserId").val(ids);
                $("#donginguser").val(names).change(function () {
                    $(this).parent().children(".btn_clear").css("display", "block");
                }).change();
            } else if (userModalStatus == "select2") {
                $("#assignUserId").val(ids);
                $("#assignUser").val(names).change(function () {
                    $(this).parent().children(".btn_clear").css("display", "block");
                }).change();
            } else if (userModalStatus == "select3") {
                $("#testUserId").val(ids);
                $("#testUser").val(names).change(function () {
                    $(this).parent().children(".btn_clear").css("display", "block");
                }).change();
            } else if (userModalStatus == "select4") {
                $("#developerId").val(ids);
                $("#developer").val(names).change(function () {
                    $(this).parent().children(".btn_clear").css("display", "block");
                }).change();
            }
        }
    } else {// 单选
        var rowData = $("#userTable").bootstrapTable('getSelections')[0];
        if (typeof(rowData) == 'undefined') {
            layer.alert("请选择一条数据", {
                icon: 2,
                title: "提示信息"
            });
            return false;
        } else {
            if (userModalStatus == "new") {
                $("#new_assignUserId").val(rowData.id);
                $("#new_assignUserName").val(rowData.userName);
            } else if (userModalStatus == "new2") {
                $("#new_testUserId").val(rowData.id);
                $("#new_testUserName").val(rowData.userName);
            } else if (userModalStatus == "new3") {
                $("#new_developerId").val(rowData.id);
                $("#new_developer").val(rowData.userName);
            } else if (userModalStatus == "edit") {
                $("#edit_assignUserId").val(rowData.id);
                $("#edit_assignUserName").val(rowData.userName);
            } else if (userModalStatus == "edit2") {
                $("#edit_testUserId").val(rowData.id);
                $("#edit_testUserName").val(rowData.userName);
            } else if (userModalStatus == "edit3") {
                $("#edit_developerId").val(rowData.id);
                $("#edit_developer").val(rowData.userName);
            } else if (userModalStatus == "opt") {
                $("#opt_assignUserId").val(rowData.id);
                $("#opt_assignUserName").val(rowData.userName);
            }
        }
    }
    $("#userModal").modal("hide");
}*/

function userReset() {
    select_rows = new Array();
    $("#userName").val('');
    $("#deptName").val('');
    $("#companyName").val('');
    $("#project").selectpicker('val', '');
    $("#userPost").selectpicker('val', '');
    $("#userModal .color1 .btn_clear").css("display", "none");
}

/*-----------------系统操作-----------------*/
function systemInit() {
    sysReset();
    if (systemModalStatus == "select") {
        project_systemTableShow(false);
    } else {
        project_systemTableShow(true);
    }
}

function commitSys() {
    if (systemModalStatus == "select") {
        var rowData = $("#systemTable").bootstrapTable('getSelections');
        if (typeof(rowData) == 'undefined' || rowData.length == 0) {
            layer.alert("请至少选择一条数据", {
                icon: 2,
                title: "提示信息"
            });
            return false;
        } else {
            var ids = [];
            var names = [];
            for (var i = 0, len = rowData.length; i < len; i++) {
                ids.push(rowData[i].id);
                names.push(rowData[i].systemName);
            }
            $("#sel_systemId").val(ids);
            $("#systemName").val(names).change();
        }
    } else {
        var rowData = $("#systemTable").bootstrapTable('getSelections')[0];
        if (typeof(rowData) == 'undefined') {
            layer.alert("请选择一条数据", {
                icon: 2,
                title: "提示信息"
            });
            return false;
        } else {
            if (systemModalStatus == "new") {
            	var systemId = $("#systemId").val();
                $("#systemId").val(rowData.id);
                $("#systemCode").val(rowData.systemCode);
                $("#system_Name").val(rowData.systemName).change();
                if(systemId!=rowData.id){
                	$("#new_assignUserId").val('');
                    $("#new_assignUserName").val('');
                    $("#projectGroupId").val('');
                    $("#assetSystemTreeId").val('');
                    $("#detectedSystemVersionId").selectpicker('val','');
            		$("#repairSystemVersionId").selectpicker('val','');
                }
//                $('#assetSystemTreeId').click();
            } else if (systemModalStatus == "edit") {
            	var systemId = $("#edit_systemId").val();
                $("#edit_systemId").val(rowData.id).change();
                $("#edit_systemCode").val(rowData.systemCode).change();
                $("#edit_system_Name").val(rowData.systemName).change();
                if(systemId!=rowData.id){
            		$("#edit_assignUserId").val('');
            		$("#edit_assignUserName").val('');
            		$("#edit_testUserId").val('');
            		$("#edit_testUserName").val('');
            		$("#edit_developerId").val('');
            		$("#edit_developer").val('');
            		$("#edit_projectGroupId").val('');
            		$("#edit_assetSystemTreeId").val('');
            		$("#edit_detectedSystemVersionId").selectpicker('val','');
            		$("#edit_repairSystemVersionId").selectpicker('val','');
            		
            	}
//                $('#edit_assetSystemTreeId').click();
            }
        }
    }
    $("#systemModal").modal("hide");
}

function sysReset() {
    select_rows = new Array();
    $("#sel_systemName").val('');
    $("#sel_systemCode").val('');
    $("#systemModal .color1 .btn_clear").css("display", "none");
}


/*-----------------需求操作-----------------*/
function reqInit() {
    reqReset();
    reqTableShow(false);
    $("#selReq").modal("show");
}

function commitReq() {
    //var rowData = $("#listReq").bootstrapTable('getSelections');
    var rowData = select_rows;
    if (typeof(rowData) == 'undefined' || rowData.length == 0) {
        layer.alert("请至少选择一条数据", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    } else {
        var codes = [];
        for (var i = 0, len = rowData.length; i < len; i++) {
            codes.push(rowData[i].REQUIREMENT_CODE);
        }
        $("#requirementCode").val(codes).change(function () {
            $(this).parent().children(".btn_clear").css("display", "block");
        }).change();
        $("#requirementName").val(codes).change(function () {
            $(this).parent().children(".btn_clear").css("display", "block");
        }).change();
        $("#selReq").modal("hide");
    }
}

function reqReset() {
    select_rows = new Array();
    $("#reqCode").val('');
    $("#reqName").val('');
    $("#reqStatus").selectpicker('val', '');
    $("#selReq .color1 .btn_clear").css("display", "none");
}


/*---------------投产窗口操作---------------*/
function ComWindowInit() {
    winReset();
    ComWindowShow(false);
    $("#userbutton1").css("display", "inline-block");
    $("#userbutton2").css("display", "none");
    $("#userbutton3").css("display", "none");
    $("#comWindowModal").modal("show");
}

function commitWin() {
    //var rowData = $("#ComWindowTable").bootstrapTable('getSelections');
    var rowData = select_rows;
    if (typeof(rowData) == 'undefined' || rowData.length == 0) {
        layer.alert("请至少选择一条数据", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    } else {
        var ids = [];
        var windowName = [];
        for (var i = 0, len = rowData.length; i < len; i++) {
            ids.push(rowData[i].id);
            windowName.push(rowData[i].windowName);
        }

        $("#windowId").val(ids);
        $("#windowName").val(windowName).change(function () {
            $(this).parent().children(".btn_clear").css("display", "block");
        }).change();
        $("#comWindowModal").modal("hide");
    }
}
function ComWindowInit2() {
    winReset();
    $("#userbutton2").css("display", "inline-block");
    $("#userbutton1").css("display", "none");
    $("#userbutton3").css("display", "none");

    ComWindowShow(true);
    $("#comWindowModal").modal("show");
}

function commitWin2() {
    var rowData = $("#ComWindowTable").bootstrapTable('getSelections');
//	var rowData = select_rows;
    if (typeof(rowData) == 'undefined' || rowData.length == 0) {
        layer.alert("请选择一条数据", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    } else {
//		var ids = [];
//		var windowName = [];
//		for(var i = 0,len = rowData.length;i < len;i++){
//			ids.push(rowData[i].id);
//			windowName.push(rowData[i].windowName);
//		}

        $("#new_windowId").val(rowData[0].id);
        $("#new_windowName").val(rowData[0].windowName).change();
        $("#comWindowModal").modal("hide");
    }
}
function ComWindowInit3() {
    winReset();
    $("#userbutton3").css("display", "inline-block");
    $("#userbutton1").css("display", "none");
    $("#userbutton2").css("display", "none");

    ComWindowShow(true);
    $("#comWindowModal").modal("show");
}

function commitWin3() {
    var rowData = $("#ComWindowTable").bootstrapTable('getSelections');
//	var rowData = select_rows;
    if (typeof(rowData) == 'undefined' || rowData.length == 0) {
        layer.alert("请选择一条数据", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    } else {
//		var ids = [];
//		var windowName = [];
//		for(var i = 0,len = rowData.length;i < len;i++){
//			ids.push(rowData[i].id);
//			windowName.push(rowData[i].windowName);
//		}

        $("#edit_windowId").val(rowData[0].id);
        $("#edit_windowName").val(rowData[0].windowName).change();
        $("#comWindowModal").modal("hide");
    }
}


/*-----------------系统弹框-----------------*/
function project_systemTableShow(singleValue){
    $("#loading").css('display','block');
    var projectId = '';
	if (systemModalStatus == "new") {
		projectId = $("#newProjectId").val();
	} else if(systemModalStatus == "edit"){
		projectId = $("#editProjectId").val();
	}
    $("#systemTable").bootstrapTable('destroy');
    $("#systemTable").bootstrapTable({
        url:"/testManage/testtask/selectAllSystemInfo2",
        method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100],
        singleSelect : singleValue,//单选
        clickToSelect : true, //是否启用点击选中行
        maintainSelected : true,
        queryParams : function(params) {
            var param={
                systemName:$.trim($("#sel_systemName").val()),
                systemCode:$.trim($("#sel_systemCode").val()),
                projectId: projectId,
                pageNumber:params.pageNumber,
                pageSize:params.pageSize
            };
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
            formatter:function(value,row,index){
                for( var i = 0, len = select_rows.length; i < len; i++){
                    if(row.id.toString() == select_rows[i].id.toString()){
                        return {
                            checked:true
                        }
                    }
                }
                return {
                    checked:false
                }

            }
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "systemCode",
            title : "子系统编码",
            align : 'center'
        },{
            field : "systemName",
            title : "子系统名称",
            align : 'center'
        },{
            field : "systemShortName",
            title : "子系统简称",
            align : 'center'
        },{
            field : "projectName",
            title : "所属项目",
            align : 'center'
        }],
        onLoadSuccess:function(){
        	$("#systemModal").modal("show");
            $("#loading").css('display','none');
        },
        onLoadError : function() {
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        },
        onPageChange:function(){
            $("#loading").css('display','block');
        }
    });
    
}

