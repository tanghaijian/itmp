 var selectSysIds = [];
 var selectUserIds =[];
 var selectTaskIds =[];
 var selectRequirement =[];
function withinUserShow(notWithUserID,devId){
	 $("#loading").css('display','block');
	    $("#withinUserTable").bootstrapTable('destroy');
	    $("#withinUserTable").bootstrapTable({
	        url:"/testManage/worktask/getAllTextUser",
	        method:"post",
	        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
	        queryParamsType:"",
	        pagination : true,
	        sidePagination: "server",
	        pageNumber : 1,
	        pageSize : 10,
	        pageList : [ 10, 25, 50, 100 ],
	        singleSelect : true,//单选
	        queryParams : function(params) {
	            var param = {
	                pageNumber:params.pageNumber,
	                pageSize:params.pageSize,
	                userName:$.trim($("#withinUserName").val()),
	                deptName:$.trim($("#withinDeptName").val()),
	                companyName:$.trim($("#withinCompanyName").val()),
	                devID:devId,
	                notWithUserID:notWithUserID
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
	            field : "USER_NAME",
	            title : "姓名",
	            align : 'center'
	        },{
	            field : "COMPANY_NAME",
	            title : "所属公司",
	            align : 'center'
	        },{
	            field : "DEPT_NAME",
	            title : "所属部门",
	            align : 'center'
	        }],
	        onLoadSuccess:function(){
	            $("#loading").css('display','none');
	        },
	        onLoadError : function() {

	        }
	    });
	
}
//列表人员弹窗
/*function userByGroupShow(notWithUserID){
	 $("#loading").css('display','block');
	    $("#withinUserTable").bootstrapTable('destroy');
	    $("#withinUserTable").bootstrapTable({
	        url:"/system/user/selectById",
	        method:"post",
	        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
	        queryParamsType:"",
	        pagination : true,
	        sidePagination: "server",
	        pageNumber : 1,
	        pageSize : 10,
	        pageList : [ 10, 25, 50, 100 ],
	        singleSelect : true,//单选
	        queryParams : function(params) {
	            var param = {
	                pageNumber:params.pageNumber,
	                pageSize:params.pageSize,
	                userName:$.trim($("#withinUserName").val()),
	                deptName:$.trim($("#withinDeptName").val()),
	                companyName:$.trim($("#withinCompanyName").val()),
	                notWithUserID:notWithUserID
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
	            field : "USER_NAME",
	            title : "姓名",
	            align : 'center'
	        },{
	            field : "USER_ACCOUNT",
	            title : "用户名",
	            align : 'center'
	        },{
	            field : "COMPANY_NAME",
	            title : "所属公司",
	            align : 'center'
	        },{
	            field : "DEPT_NAME",
	            title : "所属部门",
	            align : 'center'
	        }],
	        onLoadSuccess:function(){
	            $("#loading").css('display','none');
	        },
	        onLoadError : function() {

	        }
	    });
	
}
*/
function userTableShow(notWithUserID){
	var projectIds = $("#project").val();
	if(projectIds!=null && projectIds!=''){
		projectIds = projectIds.join(",");
	}
	$("#loading").css('display','block');
    $("#userTable").bootstrapTable('destroy');
    $("#userTable").bootstrapTable({
        url:"/system/user/getAllUserModal2",
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        //singleSelect : true,//单选
        queryParams : function(params) {
            var param = {
            	id:notWithUserID,
       			userName: $.trim($("#userName").val()),
       			companyName :  $("#companyName").val(),
       			deptName : $("#deptName").val(),
       			projectIds:projectIds,
       			systemId:"",
       			pageNumber:params.pageNumber,
       			pageSize:params.pageSize,
               
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
            title : "所属部门",
            align : 'center'
        }],
        onLoadSuccess:function(){
            $("#loading").css('display','none');
        },
        onLoadError : function() {

        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectUserIds.indexOf(rows[i])<=-1){
        		selectUserIds.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectUserIds.indexOf(rows[i])>-1){
        			selectUserIds.splice(selectUserIds.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectUserIds.indexOf(row)<=-1){
        	selectUserIds.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectUserIds.indexOf(row)>-1){
        		selectUserIds.splice(selectUserIds.indexOf(row),1);
        	}
        }
    });
}

function TaskPopup(){
	var featureStatusList=$("#TaskPType").find("option");
	$("#loading").css('display','block');
    $("#TaskTable").bootstrapTable('destroy');
    $("#TaskTable").bootstrapTable({  
    	url:"/testManage/worktask/getAllFeatureTask",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
        //singleSelect : true,//单选
        queryParams : function(params) {
             var param={ 
            	featureName:$.trim($("#TaskPName").val()),
            	featureCode:$.trim($("#TaskPCode").val()),
            	featureStatusList:$.trim($("#TaskPType").val()),
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
            field : "FEATURE_CODE",
            title : "任务编码",
            align : 'center'
        },{
            field : "COMMISSIONING_WINDOW_ID",
            visible : false,
            align : 'center'
        },{
            field : "FEATURE_NAME",
            title : "任务名称",
            align : 'center'
        },{
        	field : "REQUIREMENT_FEATURE_STATUS",
        	title : "任务状态",
        	align : 'center',
        	formatter : function(value,rows, index) {
        		for (var i = 0,len = featureStatusList.length;i < len;i++) {
        			if(rows.REQUIREMENT_FEATURE_STATUS == featureStatusList[i].value){
        				var _status = "<input type='hidden' id='list_featureStatusList' value='"+featureStatusList[i].innerHTML+"'>";
                        return featureStatusList[i].innerHTML+_status
                    }
                }
            }
        }],
        onLoadSuccess:function(){
        	   $("#loading").css('display','none');
        },
        onLoadError : function() {

        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectUserIds.indexOf(rows[i])<=-1){
        		selectTaskIds.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectTaskIds.indexOf(rows[i])>-1){
        			selectTaskIds.splice(selectTaskIds.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectUserIds.indexOf(row)<=-1){
        	selectTaskIds.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectTaskIds.indexOf(row)>-1){
        		selectTaskIds.splice(selectTaskIds.indexOf(row),1);
        	}
        }
    });
	
	
    
}
function reqPopup(){
//	$("#ReqType").selectpicker('val', '');
    $("#loading").css('display','block');
    $("#reqTable").bootstrapTable('destroy');
    $("#reqTable").bootstrapTable({
    	url:"/projectManage/requirement/getAllRequirement2",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
        //singleSelect : true,//单选
        queryParams : function(params) {
             var param={ 
            	 requirementCode : $.trim($("#ReqCode").val()),
            	 requirementName  :$.trim( $("#ReqName").val()),
            	 requirementStatus : $("#reqStatus").val(),
        		 pageNumber:params.pageNumber,
        		 pageSize:params.pageSize, 
             }
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
            formatter:function(value, row, index) {
            	if(reqStatus == 'cancel'){
            		if (row.reqStatusName == "已取消"){
                        return {
                            disabled : true,//设置是否可用
                            checked : false//设置选中
                        	};
                        	return value;
                    }
            	}
                
             }
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "REQUIREMENT_CODE",
            title : "需求编号",
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
        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectSysIds .indexOf(rows[i])<=-1){
        		selectRequirement.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectRequirement.indexOf(rows[i])>-1){
        			selectRequirement.splice(selectRequirement.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectSysIds.indexOf(row)<=-1){
        	selectRequirement.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectRequirement.indexOf(row)>-1){
        		selectRequirement.splice(selectRequirement.indexOf(row),1);
        	}
        }
    });
	
    
}

function SystemPopup(){
	$("#loading").css('display','block');
    $("#systemTable").bootstrapTable('destroy');
    $("#systemTable").bootstrapTable({
        url:"/testManage/modal/selectAllSystemInfo",
        method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100],
        //singleSelect : true,//单选
        queryParams : function(params) {
            var param={
                systemName:$.trim($("#SystemName").val()),
                systemCode:$.trim($("#SystemCode").val()),
                pageNumber:params.pageNumber,
                pageSize:params.pageSize
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

        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectSysIds .indexOf(rows[i])<=-1){
        		selectSysIds.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectSysIds.indexOf(rows[i])>-1){
        			selectSysIds.splice(selectSysIds.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectSysIds.indexOf(row)<=-1){
        	 selectSysIds.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectSysIds.indexOf(row)>-1){
        		selectSysIds.splice(selectSysIds.indexOf(row),1);
        	}
        }
    });
	
    
}

function EditDevPopup(){
	var featureStatusList=$("#edPoStatus").find("option");
	$("#loading").css('display','block');
    $("#EditPopupTable").bootstrapTable('destroy');
    $("#EditPopupTable").bootstrapTable({  
    	url:"/testManage/worktask/getAllFeature",
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
            	featureName:$.trim($("#edRelationName").val()),
            	featureCode:$.trim($("#edRelationCode").val()),
            	featureStatusList:$.trim($("#edPoStatus").val()),
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
            field : "FEATURE_CODE",
            title : "任务编码",
            align : 'center'
        },{
            field : "COMMISSIONING_WINDOW_ID",
            visible : false,
            align : 'center'
        },{
            field : "FEATURE_NAME",
            title : "任务名称",
            align : 'center'
        },{
            field : "PLAN_END_DATE",
            visible : false,
            align : 'center'
        },{
            field : "PLAN_START_DATE",
            visible : false,
            align : 'center'
        },{
            field : "ESTIMATE_WORKLOAD",
            visible : false,
            align : 'center'
        },{
        	field : "REQUIREMENT_FEATURE_STATUS",
        	title : "任务状态",
        	align : 'center',
        	formatter : function(value,rows, index) {
        		for (var i = 0,len = featureStatusList.length;i < len;i++) {
        			if(rows.REQUIREMENT_FEATURE_STATUS == featureStatusList[i].value){
        				var _status = "<input type='hidden' id='list_featureStatusList' value='"+featureStatusList[i].innerHTML+"'>";
                        return featureStatusList[i].innerHTML+_status
                    }
                }
            }
        }],
        onLoadSuccess:function(){
        	   $("#loading").css('display','none');
        },
        onLoadError : function() {

        }
    });
    
}


function DevPopup(){
	var featureStatusList=$("#PoStatus").find("option");
	$("#loading").css('display','block');
    $("#TestPopupTable").bootstrapTable('destroy');
    $("#TestPopupTable").bootstrapTable({  
    	url:"/testManage/worktask/getAllFeature",
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
            	featureName:$.trim($("#RelationName").val()),
            	featureCode:$.trim($("#RelationCode").val()),
            	featureStatusList:$.trim($("#PoStatus").val()),
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
            field : "FEATURE_CODE",
            title : "任务编码",
            align : 'center'
        },{
            field : "COMMISSIONING_WINDOW_ID",
            visible : false,
            align : 'center'
        },{
            field : "FEATURE_NAME",
            title : "任务名称",
            align : 'center'
        },{
            field : "PLAN_END_DATE",
            visible : false,
            align : 'center'
        },{
            field : "PLAN_START_DATE",
            visible : false,
            align : 'center'
        },{
            field : "ESTIMATE_WORKLOAD",
            visible : false,
            align : 'center'
        },{
        	field : "REQUIREMENT_FEATURE_STATUS",
        	title : "任务状态",
        	align : 'center',
        	formatter : function(value,rows, index) {
        		for (var i = 0,len = featureStatusList.length;i < len;i++) {
        			if(rows.REQUIREMENT_FEATURE_STATUS == featureStatusList[i].value){
        				var _status = "<input type='hidden' id='list_featureStatusList' value='"+featureStatusList[i].innerHTML+"'>";
                        return featureStatusList[i].innerHTML+_status
                    }
                }
            }
        }],
        onLoadSuccess:function(){
        	   $("#loading").css('display','none');
        },
        onLoadError : function() {

        }
    });
	
	
    
}

function ReqSelectRow()  
{  
    $("#reqTable").jqGrid('resetSelection');  
    return(true);  
} 

function systemSelectRow()  
{  
    $("#systemTable").jqGrid('resetSelection');  
    return(true);  
}
function edbeforeSelectRow()  
{  
    $("#EditPopupTable").jqGrid('resetSelection');  
    return(true);  
} 
function TaskbeforeSelectRow()  
{  
    $("#TaskTable").jqGrid('resetSelection');  
    return(true);  
} 

function beforeSelectRow()  
{  
    $("#TestPopupTable").jqGrid('resetSelection');  
    return(true);  
} 
function beforeSelectRow1() {  
    $("#userTable").jqGrid('resetSelection');  
    return(true);  
}






//清空搜索内容
function clearstatus() {
    $('#RelationCode').val("");
    $('#RelationName').val("");
    $("#PoStatus").selectpicker('val', '');
    $("#DevPopup .color1 .btn_clear").css("display","none");
}
//清空系统搜索内容
function clearSystem() {
    $('#SystemName').val("");
    $('#SystemCode').val("");
    $("#SystemP .color1 .btn_clear").css("display","none");
}
//清空任务搜索内容
function clearTask() {
    $('#TaskPCode').val("");
    $('#TaskPName').val("");
    $("#TaskPType").selectpicker('val', '');
    $("#taskP .color1 .btn_clear").css("display","none");

}
//清空需求搜索内容
function clearReq() {
	 $('#ReqCode').val("");
	 $('#ReqName').val("");
	 $("#reqStatus").selectpicker('val', '');
	 //$(".color1 .btn_clear").css("display","none")
	$("#requirementP .color1 .btn_clear").css("display","none"); 
}
function edclearstatus() {
    $('#edRelationCode').val("");
    $('#edRelationName').val("");
    $("#edPoStatus").selectpicker('val', '');
    $("#EditPopup .color1 .btn_clear").css("display","none"); 
}
function clearSearchUser(){
	$("#project").selectpicker('val', '');
	 $("#userName").val("");
	 //$("#employeeNumber").val("");
	 $("#deptName").val("");
	 $("#companyName").val("");
	 $("#userModal .color1 .btn_clear").css("display","none");
}
function searchInfoUser(){
	userTableShow();
}



function addPopup(){
	var rowData = $("#TestPopupTable").bootstrapTable('getSelections')[0];
    if(typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        })
    } else {
       //$("#sel_systemId").val(rowData.id);
        $("#featureCode").val(rowData.FEATURE_NAME).change();
        $("#addTest").data('bootstrapValidator').destroy();
        $('#addTest').data('bootstrapValidator', null);
        $("#editTestF").data('bootstrapValidator').destroy();
        $('#editTestF').data('bootstrapValidator', null);
        if(rowData.projectType == 2){   //新建类项目
        	$('#edtestStage').val('');
        	$('#testStage').val('');
			$('.is_new_project').hide();
			new_FormValidator();
		}else{
			$('.is_new_project').show();
			oac_FormValidator();
		}
        
        if(rowData.PLAN_START_DATE!=undefined){
      	  $("#startWork").val(rowData.PLAN_START_DATE);
      }else{
      	 $("#startWork").val("");
      }
    if(rowData.ESTIMATE_WORKLOAD!=undefined){
    	  $("#workLoad").val(rowData.ESTIMATE_WORKLOAD).change();
    }else{
    	 $("#workLoad").val("").change();
    }
    if(rowData.PLAN_END_DATE!=undefined){
    	$("#endWork").val(rowData.PLAN_END_DATE);
    }else{
    	$("#endWork").val("");
    }
    
    if(rowData.PLAN_SIT_START_DATE!=undefined){
    	$("#planSitStartDate").val(rowData.PLAN_SIT_START_DATE);
    }else{
    	$("#planSitStartDate").val("");
    }
    if(rowData.PLAN_SIT_END_DATE!=undefined){
    	$("#planSitEndDate").val(rowData.PLAN_SIT_END_DATE);
    }else{
    	$("#planSitEndDate").val("");
    }
    if(rowData.ESTIMATE_SIT_WORKLOAD!=undefined){
    	$("#estimateSitWorkload").val(rowData.ESTIMATE_SIT_WORKLOAD);
    }else{
    	$("#estimateSitWorkload").val("");
    }
    
    if(rowData.PLAN_PPT_START_DATE!=undefined){
    	$("#planPptStartDate").val(rowData.PLAN_PPT_START_DATE);
    }else{
    	$("#planPptStartDate").val("");
    }
    if(rowData.PLAN_PPT_END_DATE!=undefined){
    	$("#planPptEndDate").val(rowData.PLAN_PPT_END_DATE);
    }else{
    	$("#planPptEndDate").val("");
    }
    if(rowData.ESTIMATE_PPT_WORKLOAD!=undefined){
    	$("#estimatePptWorkload").val(rowData.ESTIMATE_PPT_WORKLOAD);
    }else{
    	$("#estimatePptWorkload").val("");
    }
    
    
    if($("#testStage").val()==1){
  	  if(rowData.PLAN_SIT_START_DATE!=undefined){
  		  $("#startWork").val(rowData.PLAN_SIT_START_DATE);  
  	  }
  	  if(rowData.PLAN_SIT_END_DATE!=undefined){
  		  $("#endWork").val(rowData.PLAN_SIT_END_DATE);
  	  }
  	  if(rowData.ESTIMATE_SIT_WORKLOAD!=undefined){
  		  $("#workLoad").val(rowData.ESTIMATE_SIT_WORKLOAD).change();
  	  }else{
  		 $("#workLoad").val("").change();
  	  }
    }else if($("#testStage").val()==2){
  	  if(rowData.PLAN_PPT_START_DATE!=undefined){
  		  $("#startWork").val(rowData.PLAN_PPT_START_DATE);  
  	  }
  	  if(rowData.PLAN_PPT_END_DATE!=undefined){
  		  $("#endWork").val(rowData.PLAN_PPT_END_DATE);
  	  }
  	  if(rowData.ESTIMATE_PPT_WORKLOAD!=undefined){
  		  $("#workLoad").val(rowData.ESTIMATE_PPT_WORKLOAD).change();
  	  }else{
  		 $("#workLoad").val("").change();
  	  }
  	  
    }
    $("#add_environmentType").empty();
	$("#add_environmentType").append('<option value="">请选择</option>');
	if(rowData.ENVIRONMENT_TYPE!=undefined && rowData.ENVIRONMENT_TYPE!=null){
		var arr = rowData.ENVIRONMENT_TYPE.split(",");
		for(var i=0;i<arr.length;i++){
			for(var j=0;j<environmentTypeList.length;j++){
				if(arr[i] == environmentTypeList[j][0]){
					var opt = "<option value='" + environmentTypeList[j][0] + "'>" + environmentTypeList[j][1] + "</option>";
					$("#add_environmentType").append(opt);
					break;
				}
			}
		}
	}
	$('.selectpicker').selectpicker('refresh');
    
        $("#Attribute").attr("featureCode",rowData.ID);
        $("#Attribute").attr("commissioningWindowId",rowData.COMMISSIONING_WINDOW_ID); 
        $("#Attribute").attr("requirementFeatureStatus",rowData.REQUIREMENT_FEATURE_STATUS);
        $("#Attribute").attr("environmentType",rowData.ENVIRONMENT_TYPE);
        //$('#TestPopupTable .bs-checkbox').prop("checked",false);
        $("#DevPopup").modal("hide");
        
        //$('#TestPopupTable').children(':checkbox').prop('checked',false); 
    }
	
}


function editPopup(){
	var rowData = $("#EditPopupTable").bootstrapTable('getSelections')[0];
    if(typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        })
    } else {
    	$("#addTest").data('bootstrapValidator').destroy();
        $('#addTest').data('bootstrapValidator', null);
        $("#editTestF").data('bootstrapValidator').destroy();
        $('#editTestF').data('bootstrapValidator', null);
 		if(rowData.projectType == 2){  //新建类
 			$('#edtestStage').val('');
        	$('#testStage').val('');
			$('.is_new_project').hide();
			new_FormValidator();
 		}else{
 			$('.is_new_project').show();
			oac_FormValidator();
 		}
       //$("#sel_systemId").val(rowData.id);
        $("#edfeatureCode").val(rowData.FEATURE_NAME).change();
        if(rowData.PLAN_START_DATE!=undefined){
        	  $("#edstartWork").val(rowData.PLAN_START_DATE);
          }else{
          	$("#edstartWork").val("");
          }
          if(rowData.ESTIMATE_WORKLOAD!=undefined){
          	$("#edworkLoad").val(rowData.ESTIMATE_WORKLOAD).change();
          }else{
          	$("#edworkLoad").val("").change();
          }
          if(rowData.PLAN_END_DATE!=undefined){
          	$("#edendWork").val(rowData.PLAN_END_DATE); 
          }else{
          	$("#edendWork").val(""); 
          }
          
          if($("#edtestStage").val()==1){
        	  if(rowData.PLAN_SIT_START_DATE!=undefined){
        		  $("#edstartWork").val(rowData.PLAN_SIT_START_DATE);  
        	  }
        	  if(rowData.PLAN_SIT_END_DATE!=undefined){
        		  $("#edendWork").val(rowData.PLAN_SIT_END_DATE);
        	  }
        	  if(rowData.ESTIMATE_SIT_WORKLOAD!=undefined){
        		  $("#edworkLoad").val(rowData.ESTIMATE_SIT_WORKLOAD).change(); 
        	  }else{
        		  $("#edworkLoad").val("").change();
        	  }
          }else if($("#edtestStage").val()==2){
        	  if(rowData.PLAN_PPT_START_DATE!=undefined){
        		  $("#edstartWork").val(rowData.PLAN_PPT_START_DATE);  
        	  }
        	  if(rowData.PLAN_PPT_END_DATE!=undefined){
        		  $("#edendWork").val(rowData.PLAN_PPT_END_DATE);
        	  }
        	  if(rowData.ESTIMATE_PPT_WORKLOAD!=undefined){
        		  $("#edworkLoad").val(rowData.ESTIMATE_PPT_WORKLOAD).change();
        	  }else{
        		  $("#edworkLoad").val("").change();
        	  }
        	  
          }
          
          if(rowData.PLAN_SIT_START_DATE!=undefined){
          	$("#edplanSitStartDate").val(rowData.PLAN_SIT_START_DATE);
          }else{
          	$("#edplanSitStartDate").val("");
          }
          if(rowData.PLAN_SIT_END_DATE!=undefined){
          	$("#edplanSitEndDate").val(rowData.PLAN_SIT_END_DATE);
          }else{
          	$("#edplanSitEndDate").val("");
          }
          if(rowData.ESTIMATE_SIT_WORKLOAD!=undefined){
          	$("#edestimateSitWorkload").val(rowData.ESTIMATE_SIT_WORKLOAD);
          }else{
          	$("#edestimateSitWorkload").val("");
          }
          
          if(rowData.PLAN_PPT_START_DATE!=undefined){
          	$("#edplanPptStartDate").val(rowData.PLAN_PPT_START_DATE);
          }else{
          	$("#edplanPptStartDate").val("");
          }
          if(rowData.PLAN_PPT_END_DATE!=undefined){
          	$("#edplanPptEndDate").val(rowData.PLAN_PPT_END_DATE);
          }else{
          	$("#edplanPptEndDate").val("");
          }
          if(rowData.ESTIMATE_PPT_WORKLOAD!=undefined){
          	$("#edestimatePptWorkload").val(rowData.ESTIMATE_PPT_WORKLOAD);
          }else{
          	$("#edestimatePptWorkload").val("");
          }
          $("#editEnvironmentType").empty();
      	$("#editEnvironmentType").append('<option value="">请选择</option>');
      	if(rowData.ENVIRONMENT_TYPE!=undefined && rowData.ENVIRONMENT_TYPE!=null){
      		var arr = rowData.ENVIRONMENT_TYPE.split(",");
      		for(var i=0;i<arr.length;i++){
      			for(var j=0;j<environmentTypeList.length;j++){
      				if(arr[i] == environmentTypeList[j][0]){
      					var opt = "<option value='" + environmentTypeList[j][0] + "'>" + environmentTypeList[j][1] + "</option>";
      					$("#editEnvironmentType").append(opt);
      					break;
      				}
      			}
      		}
      	}
      	$('.selectpicker').selectpicker('refresh');
        $("#edAttribute").attr("edfeatureCode",rowData.ID);
        $("#edAttribute").attr("edcommissioningWindowId",""); 
        $("#edAttribute").attr("edcommissioningWindowId",rowData.COMMISSIONING_WINDOW_ID); 
        $("#edAttribute").attr("edrequirementFeatureStatus",rowData.REQUIREMENT_FEATURE_STATUS);
        $("#EditPopup").modal("hide");
    }
	
}

function removeEdit(){
	  $("#EditPopup").modal("hide");
}


function showEdPopup(){
	edclearstatus();
	EditDevPopup();
	$("#EditPopup").modal("show");
}
function showAdPopup(){
	clearstatus();
	DevPopup();
	$("#DevPopup").modal("show");
}


function removeSystem(){
	  $("#SystemP").modal("hide");
}

function showTask(){
	selectTaskIds=[];
	clearTask();
	TaskPopup();
	$("#taskP").modal("show");
}
function showRequirement(){
	selectRequirement=[];
	clearReq();
	reqPopup();
	$("#requirementP").modal("show");
}
//系统
function showSystem(){
	clearSystem();
	selectSysIds=[];
	 SystemPopup();
	$("#SystemP").modal("show");
}
//人员
function showManPopup(){
	selectUserIds=[];
	clearSearchUser();
	getProject();
	userTableShow();
	/*getDept();
	getCompany();*/
	$("#userModal").modal("show");
}


//需求确认
function selectReq(){
	if(selectRequirement.length<=0){
		 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	} else {
			var ids = '';
			var names = '';
	    	for(var i=0;i<selectRequirement.length;i++){
	 			ids += selectRequirement[i].id+",";
	 			if(i==selectRequirement.length-1){
	 				names += selectRequirement[i].REQUIREMENT_CODE
	 			}else{
	 				names += selectRequirement[i].REQUIREMENT_CODE+',';
	 			}
	 			
	 		}
	}
	    $("#relationDemand").val(names).change();
	    $("#requirementP").modal("hide");
	    
	/*var rowData = $("#reqTable").bootstrapTable('getSelections')[0];
    if(typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        })
    } else {
        $("#relationDemand").val(rowData.REQUIREMENT_CODE);
        $("#requirementP").modal("hide");
    }*/
}
//系统确认
function selectSystem(){
	if(selectSysIds.length<=0){
		 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	} else {
			var ids = '';
			var names = '';
	    	for(var i=0;i<selectSysIds.length;i++){
	 			ids += selectSysIds[i].id+",";
	 			if(i==selectSysIds.length-1){
	 				names += selectSysIds[i].systemName
	 			}else{
	 				names += selectSysIds[i].systemName+',';
	 			}
	 			
	 		}
	    	//$("#sel_systemId").val(rowData.id);
	        $("#involveSystem").val(names).change();
	        $("#SystemP").modal("hide");
	    }
	
}
//人员确认
function selectMan(){
	if(selectUserIds.length<=0){
		 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	}else {
		var ids = '';
		var names = '';
		for(var i=0;i<selectUserIds.length;i++){
			if(i==selectUserIds.length-1){
				ids += selectUserIds[i].id;
	 			names += selectUserIds[i].userName;
			}else{
			ids += selectUserIds[i].id+",";
			names += selectUserIds[i].userName+',';
			}
		}
           $("#developer").val(names).change();
           $("#man_devUserId").val(ids);
       $("#userModal").modal("hide");
   }
	
}
//任务确认
function selectTask(){
	if(selectTaskIds.length<=0){
		 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	}else {
		var ids = '';
 		var names = '';
 		for(var i=0;i<selectTaskIds.length;i++){
 			if(i==selectTaskIds.length-1){
 				ids += selectTaskIds[i].id;
	 			names += selectTaskIds[i].FEATURE_CODE;
 			}else{
 				ids += selectTaskIds[i].id+",";
	 			names += selectTaskIds[i].FEATURE_CODE+',';
 			}
 			
 		}
 		
        $("#parallelTask").val(names).change();
       
        $("#taskP").modal("hide");
    }
	/*
	var rowData = $("#TaskTable").bootstrapTable('getSelections')[0];
    if(typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        })
    }else {
            $("#parallelTask").val(rowData.FEATURE_CODE);
       
        $("#taskP").modal("hide");
    }*/
	
}
//需求状态
function ReqStatus(){
	$.ajax({
		method:"post", 
		url:"/testManage/worktask/ReqStatus",
		success : function(data) {
			for(var i in data){
                  var opt = "<option value='" + i + "'>" + data[i] + "</option>";
                  $("#ReqType").append(opt);
                  }
			$('.selectpicker').selectpicker('refresh'); 
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){ 
			layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
		}
		
		
	});
}
 
//系统状态
function ReqSystem(){
	$.ajax({
		method:"post", 
		url:"/testManage/worktask/ReqSystem",
		success : function(data) {
			for(var i in data){
                  var opt = "<option value='" + i + "'>" + data[i] + "</option>";
                  $("#SystemType").append(opt);
                  }
			$('.selectpicker').selectpicker('refresh'); 
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){ 
			layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
		}
		
		
	});
}

/*function getDept() {
    $("#deptName").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: "/system/user/getDept",
        dataType: "json",
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].deptName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#deptName").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){ 
			layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
		}
		
    });
}*/
/*function getCompany() {
    $("#companyName").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: "/system/user/getCompany",
        dataType: "json",
        success: function(data) {
            for(var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].companyName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#companyName").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){ 
			layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
		}
		
    });
}
 */

//提交备注
function addTestRemark(){
	var testTaskRemark=$.trim($("#tyaskRemark").val());
//	if(testTaskRemark==""||testTaskRemark==undefined){
//		layer.alert('备注信息不能为空！', {
//            icon: 2,
//            title: "提示信息"
//        });
//		return;
//	}
	if (testTaskRemark == '' &&  $("#checkfiles").val() == '') {
		layer.alert('备注内容和附件不能同时为空！！！', { icon: 0 });
		return;
	}
	var id=$.trim($("#testTaskID").text());
		 var obj = {};
		 obj.testTaskRemark =$.trim($("#tyaskRemark").val());
		 obj.testTaskId =id;
		var remark=JSON.stringify(obj); 
	$.ajax({
		type:"post",
		url:"/testManage/worktask/addTaskRemark",
		data:{
			remark:remark,
			attachFiles :$("#checkfiles").val()
			},
		success: function(data) {
			layer.alert('保存成功！', {
                icon: 1,
                title: "提示信息"
            });
			_checkfiles = [];
			$("#checkfiles").val(''); 
			getSee(id);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){ 
			layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
		}
		
		
	});
}

//转派or分派
/*function tShowWithinManPopup(){
	var type=$("#userPopupType").val();
	if(type=="Transfer"){
		var id=$("#TransferUser").attr("userID");
		var devId=$("#TransferDevID").val();
		userByGroupShow(id);
		$("#withinUserModal").modal("show");
	}else if(type=="Assign"){
		var id=$("#assignUser").attr("assignUserID");
		var divID=$("#assigDevID").val();
		userByGroupShow(id);
		$("#withinUserModal").modal("show");
		
	} else if(userStatus=="insertWorkTask"){
        var id=$("#new_taskUserId").val();
        userByGroupShow(id);
        $("#withinUserModal").modal("show");
    }else if(userStatus=="updatetWorkTask"){
        var id = $("#edit_taskUserId").val();
        var divID = $("#taskID").val();
        userByGroupShow(id);
        
        $("#withinUserModal").modal("show");
    }else if(userStatus=="search"){
        var id = $("#taskAssignUserId").val();
        userByGroupShow(id);
        
        $("#withinUserModal").modal("show");
    }else if(userStatus=="add"){
        var id = $("#add_taskAssignUserId").val();
        userByGroupShow(id);
        
        $("#withinUserModal").modal("show");
    }else if(userStatus=="edit"){
        var id = $("#edit_taskAssignUserId").val();
        userByGroupShow(id);
        
        $("#withinUserModal").modal("show");
    }
}*/
function removeUser(){
	cleanUser();
	 $("#withinUserModal").modal("hide");
}
//清除人员搜索信息
function cleanUser(){
	$("#withinUserName").val("");
	 $("#withinDeptName").val("");
	 $("#withinCompanyName").val("");
	 $("#withinUserModal .color1 .btn_clear").css("display","none");
}
/*function getWithinDept() {
    $("#withinDeptName").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: "/system/user/getDept",
        dataType: "json",
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].deptName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#withinDeptName").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}
function getwithinCompany() {
    $("#withinCompanyName").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: "/system/user/getCompany",
        dataType: "json",
        success: function(data) {
            for(var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].companyName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#withinCompanyName").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}
*/
function addUserID(){
	var type=$("#userPopupType").val();
	  var rowData = $("#withinUserTable").bootstrapTable('getSelections')[0];
	    if(typeof(rowData) == 'undefined') {
	        layer.alert("请选择一条数据",{
	            icon:2,
	            title:"提示信息"
	        })
	    }else {

	    	if(type=="Transfer"){
	    		var id=$("#TransferUser").attr("userID");
	    		
	    		$("#TransferUser").val(rowData.USER_NAME).change();
	    		$("#TransferUser").attr("userID",rowData.USER_ID);
	    		cleanUser();
	 	        $("#withinUserModal").modal("hide");
	    	}else if(type=="Assign"){
	    		 $("#assignUser").val(rowData.USER_NAME).change();
	    		 $("#assignUser").attr("assignUserID",rowData.USER_ID);
	    		 cleanUser();
	 	        $("#withinUserModal").modal("hide");
	    	}else if(userStatus=="insertWorkTask"){
                $("#new_taskUserId").val(rowData.USER_ID).change();
                $("#new_taskUser").val(rowData.USER_NAME).change();
                cleanUser();
                $("#withinUserModal").modal("hide");
            }else if(userStatus=="updatetWorkTask"){
                $("#edit_taskUserId").val(rowData.USER_ID).change();
                $("#edtaskUser").val(rowData.USER_NAME).change();
                cleanUser();
                $("#withinUserModal").modal("hide");
            }else if(userStatus=="search"){
                $("#taskAssignUserId").val(rowData.USER_ID).change();
                $("#taskAssignUser").val(rowData.USER_NAME).change();
                cleanUser();
                $("#withinUserModal").modal("hide");
            }else if(userStatus=="add"){
                $("#add_taskAssignUserId").val(rowData.USER_ID).change();
                $("#add_taskAssignUser").val(rowData.USER_NAME).change();
                cleanUser();
                $("#withinUserModal").modal("hide");
            }else if(userStatus=="edit"){
                $("#edit_taskAssignUserId").val(rowData.USER_ID).change();
                $("#edit_taskAssignUser").val(rowData.USER_NAME).change();
                cleanUser();
                $("#withinUserModal").modal("hide");
            }
	           
	    }
}
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

function dateComponent() {
	var locale = {
		"format": 'yyyy-mm-dd',
		"separator": " -222 ",
		"applyLabel": "确定",
		"cancelLabel": "取消",
		"fromLabel": "起始时间",
		"toLabel": "结束时间'",
		"customRangeLabel": "自定义",
		"weekLabel": "W",
		"daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
		"monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
		"firstDay": 1
	};
	$("#timeDate").daterangepicker({ 'locale': locale });
}

function winReset(){
	$("#win_windowName").val("");
	$("#timeDate").val("");
	$("#win_windowType").selectpicker('val', '');
	
}

function searchWindows(){
	windowTypeList =  $("#win_windowType").find("option");
	commitWindowAll();
}

function commitWindowAll(){
	var startDate = '';
	var endDate = '';
	if( $( "#timeDate" ).val() != "" ){
		var arr = $( "#timeDate" ).val().split(" - ");
		startDate = arr[0];
		endDate = arr[1];
	} 
	$("#comWindowTable").bootstrapTable('destroy');
    $("#comWindowTable").bootstrapTable({  
    	url:"/projectManage/commissioningWindow/selectCommissioningWindows2",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
       // singleSelect : true,//单选
        queryParams : function(params) {
             var param={ 
        		 "windowName" :  $.trim($("#win_windowName").val()),
        		 "startDate" : startDate,
        		 "endDate" : endDate,
                 "windowType" :  $("#win_windowType").val(),
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
        	field : "windowType",
        	title : "窗口类型",
        	align : 'center',
	    	  formatter:function(value, row, index) {
	    		for(var i = 0;i<windowTypeList.length;i++){
	    			 if(windowTypeList[i].value == value){
		    			  return windowTypeList[i].innerHTML;
		    		  }
	    		}
	    		 
	    	  }
        }],
        onLoadSuccess:function(){
        	 
        },
        onLoadError : function() {

        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectWinIds .indexOf(rows[i])<=-1){
        		selectWinIds.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectWinIds.indexOf(rows[i])>-1){
        			selectWinIds.splice(selectWinIds.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectWinIds.indexOf(row)<=-1){
        	selectWinIds.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectWinIds.indexOf(row)>-1){
        		selectWinIds.splice(selectWinIds.indexOf(row),1);
        	}
        }
    });
}

function commitWin(){
	if(selectWinIds.length<=0){
		 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	}else{
		var ids = '';
 		var names = '';
 		for(var i=0;i<selectWinIds.length;i++){
 			ids += selectWinIds[i].id+",";
 			names += selectWinIds[i].windowName+',';
 		}
 		$("#windowId").val(ids);
		$("#windowName").val(names).change();
	}
	$("#comWindowModal").modal("hide");
}