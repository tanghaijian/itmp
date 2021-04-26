/**
 * Description: 弹框的操作
 * Author:liushan
 * Date: 2019/1/9 上午 9:50
 */

/*--------------工作任务操作---------------*/
function selectTestTask(){
    var rowData = $("#testTaskTable").bootstrapTable('getSelections')[0];
    if(typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        })
    } else {
        if (testTaskModalStatus == "new"){
            $("#new_defectSource option").each( function (i, n) {
                if (n.value == rowData.testStage) {
                    n.selected = true;
                    $("#new_defectSource").attr("disabled","true");
                }
            });

            $("#testTaskId").val(rowData.id);
            $("#testTaskName").val(rowData.testTaskName);
            $("#systemId").val(rowData.systemId);
            $("#system_Name").val(rowData.systemName);
            $("#new_requirementCode").val(rowData.requirementCode);
            $("#new_commissioningWindowId").val(rowData.commissioningWindowId);
            $("#testTaskModal").modal("hide");
        } else if (testTaskModalStatus == "edit"){

            $("#edit_defectSource option").each( function (i, n) {
                if (n.value == rowData.testStage) {
                    n.selected = true;
                    $("#edit_defectSource").attr("disabled","true");
                }
            });

            $("#edit_testTaskId").val(rowData.id);
            $("#edit_testTaskName").val(rowData.testTaskName);
            $("#edit_systemId").val(rowData.systemId);
            $("#edit_system_Name").val(rowData.systemName);
            $("#edit_requirementCode").val(rowData.requirementCode);
            $("#edit_commissioningWindowId").val(rowData.commissioningWindowId);
            $("#testTaskModal").modal("hide");
        }
        $('.selectpicker').selectpicker('refresh');
    }
}

function testTaskInit(){
    reset();
    testTaskShow();
    $("#testTaskModal").modal("show");
}



/*----------------人员操作-----------------*/
function userInit(){
    userReset();
    getDefectProject();
    userTableShowSel();
    $("#userModal").modal("show");
}

//人员弹框搜索，根据具体业务类别获取页面不同取值，userModalStatus参考defectManagement.js
function userTableShowSel(){
    var notWithUserID = '';
    var systemId = '';
    if (userModalStatus == "select" || userModalStatus == "assing"){//主修复人和普通查询弹框
        userTableShow(null,systemId,false);
    } else {
        if (userModalStatus == "new"){//新建人员弹框
            notWithUserID = $("#new_assignUserId").val();
            systemId = $("#systemId").val();
        } else if (userModalStatus == "edit"){//编辑人员弹框
            notWithUserID =  $("#edit_assignUserId").val();
            systemId = $("#edit_systemId").val();
        } else if (userModalStatus == "opt"){//转派人员
            notWithUserID =  $("#opt_assignUserId").val();
            systemId = $("#opt_systemId").val();
        }
        userTableShow(notWithUserID,systemId,true);
    }
}

//选择后提交用户信息
function commitUser(){
    if(userModalStatus == "select"){ // 多选
        //var rowData = $("#userTable").bootstrapTable('getSelections');
        var rowData = select_rows;
        if(typeof(rowData) == 'undefined' || rowData.length == 0) {
            layer.alert("请至少选择一条数据",{
                icon:2,
                title:"提示信息"
            });
            return false;
        } else {
            var ids = [];
            var names = [];
            for(var i = 0,len = rowData.length; i < len;i++){
                ids.push(rowData[i].id);
                names.push(rowData[i].userName);
            }

            $("#submitUserId").val(ids);
            $("#submitUserName").val(names).change(function(){
                $(this).parent().children(".btn_clear").css("display","block");
            }).change();
        }
    } else  if(userModalStatus == "assing"){
        //var rowData = $("#userTable").bootstrapTable('getSelections');
        var rowData = select_rows;
        if(typeof(rowData) == 'undefined' || rowData.length == 0) {
            layer.alert("请至少选择一条数据",{
                icon:2,
                title:"提示信息"
            });
            return false;
        } else {
            var ids = [];
            var names = [];
            for(var i = 0,len = rowData.length; i < len;i++){
                ids.push(rowData[i].id);
                names.push(rowData[i].userName);
            }

            $("#sel_assingUserId").val(ids);
            $("#sel_assingUserName").val(names).change(function(){
                $(this).parent().children(".btn_clear").css("display","block");
            }).change();
        }
    } else {// 单选
        var rowData = $("#userTable").bootstrapTable('getSelections')[0];
        if(typeof(rowData) == 'undefined') {
            layer.alert("请选择一条数据",{
                icon:2,
                title:"提示信息"
            });
            return false;
        } else {
            if (userModalStatus == "new"){
                $("#new_assignUserId").val(rowData.id);
                $("#new_assignUserName").val(rowData.userName);
            } else if (userModalStatus == "edit"){
                $("#edit_assignUserId").val(rowData.id);
                $("#edit_assignUserName").val(rowData.userName);
            } else if (userModalStatus == "opt"){
                $("#opt_assignUserId").val(rowData.id);
                $("#opt_assignUserName").val(rowData.userName);
            }
        }
    }
    $("#userModal").modal("hide");

}
//重置查询条件
function userReset(){
    select_rows = new Array();
    $("#userName").val('');
    $("#deptName").val('');
    $("#companyName").val('');
    $("#defect_project").selectpicker('val', '');
    $("#userModal .color1 .btn_clear").css("display","none");

}

/*-----------------系统操作-----------------*/
function systemInit(){
    sysReset();
    systemTableShow(false);
    $("#systemModal").modal("show");
}

//系统弹框选择后提交
function commitSys(){
    //var rowData = $("#systemTable").bootstrapTable('getSelections');
    var rowData = select_rows;
    if(typeof(rowData) == 'undefined' || rowData.length == 0) {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        });
        return false;
    } else {
        var ids = [];
        var names = [];
        for(var i = 0,len = rowData.length; i < len;i++){
            ids.push(rowData[i].id);
            names.push(rowData[i].systemName);
        }
        $("#sel_systemId").val(ids);
        $("#systemName").val(names).change(function(){
            $(this).parent().children(".btn_clear").css("display","block");
        }).change();
    }
    $("#systemModal").modal("hide");
}

function sysReset(){
    select_rows = new Array();
    $("#sel_systemName").val('');
    $("#sel_systemCode").val('');
    $("#systemModal .color1 .btn_clear").css("display","none");
}



/*-----------------需求操作-----------------*/
function reqInit(){
    reqReset();
    reqTableShow(false);
    $("#selReq").modal("show");
}

//需求弹框选择后提交
function commitReq(){
    //var rowData = $("#listReq").bootstrapTable('getSelections');
    var rowData = select_rows;
    if(typeof(rowData) == 'undefined' || rowData.length == 0 ) {
        layer.alert("请至少选择一条数据",{
            icon:2,
            title:"提示信息"
        });
        return false;
    } else {
        var codes = [];
        for(var i = 0,len = rowData.length;i < len;i++){
            codes.push(rowData[i].REQUIREMENT_CODE);
        }
        $("#requirementCode").val(codes).change(function(){
            $(this).parent().children(".btn_clear").css("display","block");
        }).change();
        $("#requirementName").val(codes).change(function(){
            $(this).parent().children(".btn_clear").css("display","block");
        }).change();
        $("#selReq").modal("hide");
    }
}
//重置查询条件
function reqReset(){
    select_rows = new Array();
    $("#reqCode").val('');
    $("#reqName").val('');
    $("#reqStatus").selectpicker('val', '');
    $("#selReq .color1 .btn_clear").css("display","none");
}



/*---------------投产窗口操作---------------*/
function ComWindowInit(){
    winReset();
    ComWindowShow(false);
    $("#comWindowModal").modal("show");
}

//投产窗口选择数据后提交
function commitWin(){
    //var rowData = $("#ComWindowTable").bootstrapTable('getSelections');
    var rowData = select_rows;
    if(typeof(rowData) == 'undefined' || rowData.length == 0 ) {
        layer.alert("请至少选择一条数据",{
            icon:2,
            title:"提示信息"
        });
        return false;
    } else {
        var ids = [];
        var windowName = [];
        for(var i = 0,len = rowData.length;i < len;i++){
            ids.push(rowData[i].id);
            windowName.push(rowData[i].windowName);
        }
        
        if($('#userbutton').data('user') == 'set'){
        	$("#setComWindow_value").val(ids);
            $("#setComWindow_name").val(windowName).change(function(){
                $(this).parent().children(".btn_clear").css("display","block");
            }).change();
        }else{
        	$("#windowId").val(ids);
            $("#windowName").val(windowName).change(function(){
                $(this).parent().children(".btn_clear").css("display","block");
            }).change();
        }
        $("#comWindowModal").modal("hide");
    }
}
//重置查询条件
function winReset(){
    select_rows = new Array();
    $("#win_windowName").val('');
    $("#windowVersion").val('');
    $("#win_windowType").selectpicker('val', '');
    $("#comWindowModal .color1 .btn_clear").css("display","none");
}

