/**
 * Description: 各种弹框的js
 * Author:liushan
 * Date: 2018/12/24 下午 2:04
 */
var modalUrl = "/devManage/";
var systemUrl = "/system/";
var projectUrl = "/projectManage/";

var select_rows = new Array();

$(document).ready(function () {
    checkSelectRows("#userTable");
    checkSelectRows("#systemTable");
    checkSelectRows("#listReq");
    checkSelectRows("#ComWindowTable");
});

/*-----------------人员弹框-----------------*/
function userTableShow(notWithUserID,systemId,singleValue){
    singleValue = singleValue == undefined?$("#userTable").bootstrapTable('getOptions').singleSelect:singleValue;
    $("#loading").css('display','block');
    $("#userTable").bootstrapTable('destroy');
    $("#userTable").bootstrapTable({
        url:systemUrl+"user/getAllUserModal",
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        singleSelect : singleValue,//单选
        clickToSelect : true, //是否启用点击选中行
        maintainSelected : true,
        queryParams : function(params) {
            var param = {
                pageNumber:params.pageNumber,
                pageSize:params.pageSize,
                userName:$.trim($("#userName").val()),
                deptName:$.trim($("#deptName").val()),
                companyName:$.trim($("#companyName").val()),
                notWithUserID:notWithUserID,
                systemId:systemId,
                projectIds:$("#defect_project").val() != null?$("#defect_project").val().toString():""
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
            field : "userName",
            title : "姓名",
            align : 'center'
        },{
            field : "userAccount",
            title : "用户名",
            align : 'center'
        },{
            field : "deptName",
            title : "所属部门",
            align : 'center'
        },{
            field : "companyName",
            title : "所属公司",
            align : 'center'
        }],
        onLoadSuccess:function(data){
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


//获取当前登录用户所在项目（结项的项目除外）
function getDefectProject(){
    $("#defect_project").empty();
    $.ajax({
        url:projectUrl+"project/getAllProject",
        dataType:"json",
        type:"post",
        async:false,
        success:function(data){
            if(data.status == "2"){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == "1"){
                if(data.projects!=undefined && data.projects.length > 0){
                    for(var i=0;i <data.projects.length;i++){
                        $("#defect_project").append("<option selected value='"+data.projects[i].id+"'>"+data.projects[i].projectName+"</option>")
                    }
                }
                $('.selectpicker').selectpicker('refresh');
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
}

/*-----------------部门下拉框---------------*/
function getDept() {
    $("#deptName").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: systemUrl+"user/getDept",
        dataType: "json",
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].deptName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#deptName").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}

/*-----------------公司下拉框---------------*/
function getCompany() {
    $("#companyName").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: systemUrl+"user/getCompany",
        dataType: "json",
        success: function(data) {
            for(var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].companyName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#companyName").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}

/*-----------------系统弹框-----------------*/
function systemTableShow(singleValue){
    singleValue = singleValue == undefined?$("#systemTable").bootstrapTable('getOptions').singleSelect:singleValue;
    $("#loading").css('display','block');
    $("#systemTable").bootstrapTable('destroy');
    $("#systemTable").bootstrapTable({
        url:modalUrl+"modal/selectAllSystemInfo",
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

/*-----------------需求弹框-----------------*/
function reqTableShow(singleValue){
    singleValue = singleValue == undefined?$("#listReq").bootstrapTable('getOptions').singleSelect:singleValue;
    $("#loading").css('display','block');
    $("#listReq").bootstrapTable('destroy');
    $("#listReq").bootstrapTable({
        url:projectUrl+"requirement/getAllRequirement2",
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        singleSelect : singleValue,//单选
        clickToSelect : true, //是否启用点击选中行
        maintainSelected : true,
        queryParams : function(params) {
            var param = {
                pageNumber:params.pageNumber,
                pageSize:params.pageSize,
                requirementCode:$.trim($("#reqCode").val()),
                requirementName:$.trim($("#reqName").val()),
                requirementStatus:$.trim($("#reqStatus").find("option:selected").val())
            };
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
            formatter:function(value, row, index) {
                for( var i = 0, len = select_rows.length; i < len; i++){
                    if(row.id.toString() == select_rows[i].id.toString()  || reqStatus == 'cancel' && row.reqStatusName == "已取消"){
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
            field : "REQUIREMENT_CODE",
            title : "需求编码",
            align : 'center'
        },{
            field : "REQUIREMENT_NAME",
            title : "需求名称",
            align : 'center'
        },{
            field : "reqStatusName",
            title : "需求状态",
            align : 'center'
        },{
            field : "reqSourceName",
            title : "需求来源",
            align : 'center'
        },{
            field : "reqTypeName",
            title : "需求类型",
            align : 'center'
        }],
        onLoadSuccess:function(){
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

/*-----------------工作任务弹框--------------*/
function testTaskShow(){
    $("#loading").css('display','block');
    $("#testTaskTable").bootstrapTable('destroy');
    $("#testTaskTable").bootstrapTable({
        url:modalUrl+"modal/getAllTestTask",
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        singleSelect : true,//单选
        clickToSelect : true, //是否启用点击选中行
        maintainSelected : true,
        queryParams : function(params) {
            var param = {
                pageNumber:params.pageNumber,
                pageSize:params.pageSize,
                testTaskCode:$.trim($("#taskCode").val()),
                testTaskName:$.trim($("#taskName").val()),
                testTaskStatus:$.trim($("#taskState").find("option:selected").val())
            };
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px"
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "testTaskCode",
            title : "任务编号",
            align : 'center'
        },{
            field : "testTaskName",
            title : "任务名称",
            align : 'center'
        },{
            field : "testTaskStatus",
            title : "状态",
            align : 'center',
            formatter : function(value, row, index) {
                var className = "doing";
                var taskStateList = $("#taskState").find("option");
                for (var i = 0,len = taskStateList.length;i < len;i++) {
                    if(row.testTaskStatus == taskStateList[i].value){
                        className +=row.testTaskStatus;
                        return "<span class="+className+">"+taskStateList[i].innerHTML+"</span>";
                    }
                }
            }
        },{
            field : "systemId",
            title : "systemId",
            visible : false,
            align : 'center'
        },{
            field : "systemName",
            title : "systemName",
            visible : false,
            align : 'center'
        }],
        onLoadSuccess:function(){
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

/*-----------------投产窗口弹框--------------*/
function ComWindowShow(singleValue){
    singleValue = singleValue == undefined?$("#ComWindowTable").bootstrapTable('getOptions').singleSelect:singleValue;
    $("#loading").css('display','block');
    $("#ComWindowTable").bootstrapTable('destroy');
    $("#ComWindowTable").bootstrapTable({
        url:modalUrl+"modal/getAllComWindow",
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        singleSelect : singleValue,//单选
        clickToSelect : true, //是否启用点击选中行
        maintainSelected : true,
        queryParams : function(params) {
            var param = {
                pageNumber:params.pageNumber,
                pageSize:params.pageSize,
                windowName:$.trim($("#win_windowName").val()),
                windowType:$.trim($("#win_windowType").find("option:selected").val()),
                windowVersion:$.trim($("#windowVersion").val())
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
            field : "windowDate",
            title : "窗口日期",
            align : 'center'
        },{
            field : "windowName",
            title : "窗口名称",
            align : 'center'
        },{
            field : "windowType",
            title : "窗口类型",
            align : 'center',
            formatter : function(value, row, index) {
                var className = "doing";
                var windowTypeList = $("#win_windowType").find("option");
                for (var i = 0,len = windowTypeList.length;i < len;i++) {
                    if(row.windowType == windowTypeList[i].value){
                        className +=row.windowType;
                        return "<span class="+className+">"+windowTypeList[i].innerHTML+"</span>";
                    }
                }
            }
        },{
            field : "windowVersion",
            title : "窗口版本号",
            /*visible : false,*/
            align : 'center'
        }],
        onLoadSuccess:function(){
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

/**
*@author liushan
*@Description bootstrapTable 复选框分页选择
*@Date 2020/8/13
 * @param null
*@return 
**/
function checkSelectRows(id){
    $(id).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var singleSelect = $(id).bootstrapTable('getOptions').singleSelect;
        var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
        if(singleSelect == true ){// 单选
            select_rows = [];
            select_rows.push(datas[0]);
        } else { // 多选
            examine(e.type,datas);                              // 保存到全局 Array() 里
        }
    });
}

/**
*@author liushan
*@Description 保存到全局 Array() 里
*@Date 2020/8/13
 * @param null
*@return 
**/
function examine(type,datas){
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            select_rows.indexOf(v.id.toString()) == -1 ? select_rows.push(v) : -1;
        });
    }else{
        $.each(datas,function(i,v){
            select_rows.splice(select_rows.indexOf(v),1);    //删除取消选中行
        });
    }
}

