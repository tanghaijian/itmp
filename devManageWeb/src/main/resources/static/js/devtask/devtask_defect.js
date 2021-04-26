/**
 * Created by ztt
 */
  var reqStatus = '';
$(function(){
	getProject();
	 $("#w_windowDate").datetimepicker({
    	minView:"month",
    	format: "yyyy-mm-dd",
    	autoclose: true,
    	todayBtn: true,
    	language: 'zh-CN',
    	pickerPosition: "bottom-left"
    });
	
    $("#newsystemName2").click(function(){
    	$("#sysbutton").attr("data-sys","new");
		 $("#selSys").modal("show");
		 clearSearchSys();
		 systemTableShow2();
    });
   
  
    $("#newdevManageUserName").click(function(){
    	$("#userbutton").attr("data-user", "newMan");
    	$("#deptNameReq").empty();
    	$("#companyNameReq").empty();
    	 $("#userModalreqF").modal("show");
    	 clearSearchUser();
    	 userTableShow2();
    	
    });
   
    
    $("#newexecuteUserName").click(function(){
    	$("#userbutton").attr("data-user", "new");
    	$("#deptNameReq").empty();
    	$("#companyNameReq").empty();
	   	 $("#userModalreqF").modal("show");
		 clearSearchUser();
		 userTableShow2();
	});
    
    $("#newcommitWindowName").click(function(){
    	$("#windowButton").attr("data-window","new");
    	winReset();
    	$("#comWindowModalReqFeature").modal("show");
    	commitWindowReqFeature();
    });
    

})
//======================投产窗口================================

//开发缺陷-修复完成-延迟修复并新建开发任务  ：投产窗口查询事件
function commitWindowReqFeature(){
	$("#comWindowTableReqFeature").bootstrapTable('destroy');
    $("#comWindowTableReqFeature").bootstrapTable({  
    	url:"/projectManage/commissioningWindow/selectCommissioningWindows2",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
        singleSelect : true,//单选
        queryParams : function(params) {
             var param={ 
        		 "windowName" :  $.trim($("#w_windowName").val()),
                 "windowDate" :  $("#w_windowDate").val(),
                 "windowType" :  $("#w_windowType").val(),
        		 pageNumber:params.pageNumber,
        		 pageSize:params.pageSize, 
             }
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "windowName",
            title : "窗口名称",
            align : 'center'
        },{
            field : "windowDate",
            title : "投产日期",
            align : 'center'
        },{
        	field : "typeName",
        	title : "窗口类型",
        	align : 'center'
        }],
        onLoadSuccess:function(){
        	 
        },
        onLoadError : function() {

        }
    });
}

//开发缺陷-修复完成-延迟修复并新建开发任务，提交投产窗口选择的数据
function commitWinReqFeature(){
	var selectContent = $("#comWindowTableReqFeature").bootstrapTable('getSelections')[0];
    if(typeof(selectContent) == 'undefined') {
    	 layer.alert('请选择一列数据！', {icon: 0});
       return false;
    }else{
    	 if($("#windowButton").attr("data-window") == 'new'){
			 $("#newcommitWindowId").val(selectContent.id);
			 $("#newcommitWindowName").val(selectContent.windowName).change();
		 }
	$("#comWindowModalReqFeature").modal("hide");

    }
}
function winResetReqFeature(){
	$("#win_windowName").val("");
	$("#win_windowDate").val("");
	$("#win_windowType").selectpicker('val', '');
	
}

//====================涉及系统弹框start==========================
//开发缺陷-修复完成-延迟修复并新建开发任务，关联子系统弹框查询
function systemTableShow2(){  
	$("#systemTable2").bootstrapTable('destroy');
    $("#systemTable2").bootstrapTable({  
    	url:"/devManage/systeminfo/selectAllSystemInfo",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
        singleSelect : true,//单选
        queryParams : function(params) {
             var param={ 
            	 systemName:$.trim($("#SCsystemName").val()),
            	 systemCode:$.trim($("#SCsystemCode").val()),
                 pageNumber:params.pageNumber,
                 pageSize:params.pageSize, 
             }
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
        	 
        },
        onLoadError : function() {

        }
    });
}

//开发缺陷-修复完成-延迟修复并新建开发任务，关联子系统弹框，选择系统后确定事件
function commitSystem(){
	 var selectContent = $("#systemTable2").bootstrapTable('getSelections')[0];
     if(typeof(selectContent) == 'undefined') {
    	 layer.alert('请选择一列数据！', {icon: 0});
        return false;
     }else{
    	
    	 if($("#sysbutton").attr("data-sys") == "new"){
    		$("#newsystemId").val(selectContent.id);
    		if(selectContent.developmentMode!=null && selectContent.developmentMode!=undefined){
   	    		$("#ndevelopmentMode").val(selectContent.developmentMode);
   	    	}else{
   	    		$("#ndevelopmentMode").val('');
   	    	}
 	    	$("#newsystemName2").val(selectContent.systemName).change(); 
    	 }
    	
    	 $("#selSys").modal("hide");
     }

}
//重置
function clearSearchSys(){
	$("#SCsystemName").val('');
	$("#SCsystemCode").val('');
	 $(".color1 .btn_clear").css("display","none");
}
//====================涉及系统弹框end==========================

//====================人员弹框start=======================
function userTableShow2(){
	 getProject();
	var projectIds = $("#project").val();
	if(projectIds!=null && projectIds!=''){
		projectIds = projectIds.join(",");
	}
	$("#userTableReqF").bootstrapTable('destroy');
			$("#userTableReqF").bootstrapTable({  
		    	url:"/system/user/getAllUserModal2",
		    	method:"post",
		        queryParamsType:"",
		        pagination : true,
		        sidePagination: "server",
		        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		        pageNumber : 1,
		        pageSize : 10,
		        pageList : [ 5, 10, 15],
		        singleSelect : true,//单选
		        queryParams : function(params) {
			             var param={
			            	 //systemId:$("#newsystemId").val(),
			            	 projectIds:projectIds,
			         		 userName: $.trim($("#userNameReqF").val()),
			         		 companyName :  $("#companyNameReq").val(),
			         		 deptName : $("#deptNameReq").val(),
			                 pageNumber:params.pageNumber,
			                 pageSize:params.pageSize, 
		             }
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
		            field : "userName",
		            title : "姓名",
		            align : 'center'
		        },{
		            field : "userAccount",
		            title : "用户名",
		            align : 'center'
		        },{
		        	field : "companyName",
		        	title : "所属公司",
		        	align : 'center'
		        },{
		            field : "deptName",
		            title : "所属处室",
		            align : 'center'
		        }],
		        onLoadSuccess:function(){
		        
		        },
		        onLoadError : function() {

		        }
		    }); 
}

//开发缺陷-修复完成-延迟修复并创建开发任务，管理岗、执行人人员弹出框查询事件
function userTableShowAll(){
	var projectIds = $("#project").val();
	if(projectIds!=null && projectIds!=''){
		projectIds = projectIds.join(",");
	}
	$("#userTableReqF").bootstrapTable('destroy');
			$("#userTableReqF").bootstrapTable({  
		    	url:"/system/user/getAllUserModal2",
		    	method:"post",
		        queryParamsType:"",
		        pagination : true,
		        sidePagination: "server",
		        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		        pageNumber : 1,
		        pageSize : 10,
		        pageList : [ 5, 10, 15],
		        singleSelect : true,//单选
		        queryParams : function(params) {
			             var param={
			            	 //systemId:$("#newsystemId").val(),
			            	 projectIds:projectIds,
			         		 userName: $.trim($("#userNameReqF").val()),
			         		 companyName :  $("#companyNameReq").val(),
			         		 deptName : $("#deptNameReq").val(),
			                 pageNumber:params.pageNumber,
			                 pageSize:params.pageSize, 
		             }
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
		            field : "userName",
		            title : "姓名",
		            align : 'center'
		        },{
		            field : "userAccount",
		            title : "用户名",
		            align : 'center'
		        },{
		        	field : "companyName",
		        	title : "所属公司",
		        	align : 'center'
		        },{
		            field : "deptName",
		            title : "所属处室",
		            align : 'center'
		        }],
		        onLoadSuccess:function(){
		        
		        },
		        onLoadError : function() {

		        }
		    }); 
	    
}

/*function getDeptReq() {
    $("#deptNameReq").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: "/system/user/getDept",
        dataType: "json",
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].deptName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#deptNameReq").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}

function getCompanyReq() {
    $("#companyNameReq").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: "/system/user/getCompany",
        dataType: "json",
        success: function(data) {
        	console.log(data);
            for(var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].companyName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#companyNameReq").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}*/

//开发缺陷-修复完成-延迟修复并创建开发任务，管理岗、执行人人员弹出框确定事件
function commitUserReqF(){
	var selectContent = $("#userTableReqF").bootstrapTable('getSelections')[0];
    if(typeof(selectContent) == 'undefined') {
    	 layer.alert('请选择一列数据！', {icon: 0});
       return false;
    }else{
		
		 if($("#userbutton").attr("data-user") == 'new'){
			 $("#newexecuteUser").val(selectContent.id);
			 $("#newexecuteUserName").val(selectContent.userName).change();
		 }
		
		 if($("#userbutton").attr("data-user")==  'newMan'){
			 $("#newdevManageUser").val(selectContent.id);
			 $("#newdevManageUserName").val(selectContent.userName).change();
		 }
		
		$("#userModalreqF").modal("hide");
	 }
}

function clearSearchUser(){
	 $("#userNameReqF").val('');
	 $("#deptNameReq").val('');
	 $("#companyNameReq").val('');
	 $("#project").selectpicker('val', '');
	 $(".color1 .btn_clear").css("display","none");
}
//====================人员弹框end=======================

//获取当前登录用户所在项目（结项的项目除外） 
function getProject(){
	$("#project").empty();
	//var projectIds = '';
	$.ajax({
		url:"/devManage/displayboard/getAllProject",
		dataType:"json",
		type:"post",
		async:false,
		success:function(data){
			if(data.projects!=undefined && data.projects.length>0){
				for(var i=0;i <data.projects.length;i++){
					//projectIds += data.projects[i].id+",";
					$("#project").append("<option selected value='"+data.projects[i].id+"'>"+data.projects[i].projectName+"</option>")
					
				}
				//$('#project option').attr('selected', true);
			}
			
			 $('.selectpicker').selectpicker('refresh');
			 if(data.status == '2'){
				layer.alert("查询项目失败！！！", { icon: 2});
			}
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
		}
	})
	//return projectIds;
	
}

