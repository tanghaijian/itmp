/**
 * 开发工作任务弹窗
 */ 
var selectSysIds = [];//选择的系统ID
 var selectUserIds =[];
 var selectTaskIds =[];
 var selectRequirement =[];
var is_two_search_flag = false;
var is_check_Collect_flag = true;//是否继续收藏
var collection_obj = {
	  menuUrl : '../worktask/toWorktask',
	  filterName : currentUserId,    //当前用户id
	  search : '',
	  isCollect : false ,
	  table_list : '#devlist' ,	//表格id
};
var collection2_obj = {
		  menuUrl : '../devManageui/worktask/toWorktask',
		  filterName : '',
		  search : [],
		  isCollect : false
		};
 $(function(){
	getProject();
	system_move_handle();
	$('#userModal').on('hide.bs.modal', function () {
	     $("#Working_state").val(1);
		 $("#Working_state").selectpicker('refresh');
	});
	$('#withinUserModal').on('hide.bs.modal', function () {
		$("#Working_state2").val(1);
		$("#Working_state2").selectpicker('refresh');
	});
	var remarkUser = {
	    'ele': 'involveSystem',
	    'url': '/devManage/systeminfo/selectAllSystemInfo2',
	    'params':{         
	        'systemName':'',
	        'systemCode':'',
	        'notIds':'',
	        'flag':1,
	        'pageNumber':1,
	        'pageSize':10
	    },
	    'name':'systemName',
	    'id':'id',
	    'top':'29px',
		'userId':$('#involveSystem_ids'),
		'checkbox':true,//多选
	};
	fuzzy_search(remarkUser);
	
	$("#two_search_div_btn").click(function(){
		if(is_two_search_flag){
			is_two_search_flag = false;
			$("#two_search_div").hide();
		}else{
			is_two_search_flag = true;
			$("#two_search_div").show();
		}
	})
	other();
    collect_handle();
    getCollection2(); 
	_select_box_show();
})

//更多操作点击后展示
function _select_box_show() {
	$(document).on('click', '._select_box_show', function (e) {
        $('._select_box_menu').hide();
		var top = $(this).offset().top - $(document).scrollTop();
        var left = $(this).offset().left - $(document).scrollLeft();
		$(this).next().css({
			position: 'fixed',
			top: top + $(this).height() + 'px',
			left: left - 10 + 'px',
		});
		_top = top + $(this).height();
		scroll_flag = true;
		
		if($(this).hasClass('active')){
			$(this).next('._select_box_menu').hide();
			$(this).removeClass('active').children('span').removeClass('fa-angle-down').addClass('fa-angle-up');
			scroll_flag = false;
		}else{
			$(this).addClass('active').children('span').removeClass('fa-angle-up').addClass('fa-angle-down');
			$(this).next('._select_box_menu').show();
		}
	})
	$(document).on('click', function (e) {
        if(!$(e.target).hasClass('_select_box_show') && !$(e.target).hasClass('_select_box_menu')){
            $('._select_box_menu').hide();
            $('._select_box_menu').prev().removeClass('active').children('span').removeClass('fa-angle-down').addClass('fa-angle-up');
        }
	})
}


//显示系统弹框
function showSystem(){
	clearSystem();
	selectSysIds=[];
	 SystemPopup();
	 if($("#involveSystem_checked_list").children().length){
		 var all_data = [];
		 $("#involveSystem_checked_list").children().each(function(idx,val){
			 all_data.push(JSON.parse($(val).attr('data')));
		 })
		 SystemPopup_checked(all_data);
	 }else{
		 SystemPopup_checked([]);
	 }
	$("#SystemP").modal("show");
}

function searchSystem(){
	SystemPopup();
}

//未选系统
function SystemPopup(){
	$("#loading").css('display','block');
    $("#systemTable").bootstrapTable('destroy');
    $("#systemTable").bootstrapTable({
        url:"/devManage/systeminfo/selectAllSystemInfo2",
        method:"post",
        queryParamsType:"",
        pagination : true,
        uniqueId:"id",
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
                pageSize:params.pageSize,
                flag:1,
                notIds:$("#involveSystem_ids").val()
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
            field : "manangeUserNames",
            title : "项目管理岗",
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

//移除系统事件
function system_move_handle(){
	
	$("#move_right_btn").on('click',function(){    //添加系统
		var table_data = $("#systemTable").bootstrapTable('getSelections');
		if(table_data.length){
			table_data.map(function(v){
				$('#systemTable').bootstrapTable('removeByUniqueId',v.id);
				var ids = $('#involveSystem_ids').val() + v.id;
				if($('#involveSystem_ids').val()){
					ids = $('#involveSystem_ids').val() + ',' + v.id;
				}
				$('#involveSystem_ids').val(ids);
				$('#involveSystem_checked_list').append(`<li class="involveSystem_tag_item tag_item involveSystem_tag_item${v.id}" 
					userid="${v.id}" title="${v.systemName}" data='${JSON.stringify(v)}'>${v.systemName}</li>`);
			})
			var len = $('#involveSystem_checked_list').children().length;
			let tit = '';
			$('.involveSystem_tag_item').each(function (idx, val) {
				tit += $(val).text() + ',';
			});
			$('#involveSystem_tit').text(`已选${len}项`).attr('title',tit).show();
			$('#systemTable2').bootstrapTable('append',table_data);
		}
		if(table_data.length >= 10){
			SystemPopup();
		}
	})
	$("#move_left_btn").on('click',function(){    //移除系统
		var table_data = $("#systemTable2").bootstrapTable('getSelections');
		if(table_data.length){
			table_data.map(function(v){
				$('#systemTable2').bootstrapTable('removeByUniqueId',v.id);
				var ids = $('#involveSystem_ids').val().replace(v.id + ',',"");
				$('#involveSystem_ids').val(ids);
				$('.involveSystem_tag_item' + v.id).remove();
			})
			var len = $('#involveSystem_checked_list').children().length;
			let tit = '';
			$('.involveSystem_tag_item').each(function (idx, val) {
				tit += $(val).text() + ',';
			});
			$('#involveSystem_tit').text(`已选${len}项`).attr('title',tit).show();
			$('#systemTable').bootstrapTable('append',table_data);  
		}
	})
}

//已选系统
function SystemPopup_checked(data){
    $("#systemTable2").bootstrapTable('destroy');
    $("#systemTable2").bootstrapTable({
        data:data,
        method:"post",
        queryParamsType:"",
        pagination : true,
        uniqueId:"id",
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100],
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
            field : "manangeUserNames",
            title : "项目管理岗",
            align : 'center'
        }],
        onLoadSuccess:function(){
            $("#loading").css('display','none');
        },
        onLoadError : function() {
        	$("#loading").css('display','none');
        },
    });
	
    
}


//编辑页面选择开发任务弹框查询事件
function EditDevPopup(){
	
	var featureStatusList=$("#PoStatus").find("option");
	$("#loading").css('display','block');
    $("#EditPopupTable").bootstrapTable('destroy');
    $("#EditPopupTable").bootstrapTable({  
    	url:"/devManage/worktask/getAllFeature",
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
            	FeatureStatus:$.trim($("#edPoStatus").val()),
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

//新增开发工作任务时，关联开发任务弹窗【搜索】事件
function DevPopup(){
	var featureStatusList=$("#PoStatus").find("option");
	$("#loading").css('display','block');
    $("#devPopupTable").bootstrapTable('destroy');
    $("#devPopupTable").bootstrapTable({  
    	url:"/devManage/worktask/getAllFeature",
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
            	FeatureStatus:$.trim($("#PoStatus").val()),
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
function beforeSelectRow()  
{  
    $("#devPopupTable").jqGrid('resetSelection');  
    return(true);  
} 
function beforeSelectRow1() {  
    $("#userTable").jqGrid('resetSelection');  
    return(true);  
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
    $("#requirementP .color1 .btn_clear").css("display","none");
}
//清空搜索内容
function clearstatus() {
    $('#RelationCode').val("");
    $('#RelationName').val("");
    $("#PoStatus").selectpicker('val', '');
    $(".color1 .btn_clear").css("display","none");
}



//清空系统搜索内容
function clearSystem() {
    $('#SystemName').val("");
    $('#SystemCode').val("");
    $("#SystemType").selectpicker('val', '');
    $("#SystemP .color1 .btn_clear").css("display","none");
}

//编辑时，清空搜索条件
function edclearstatus() {
    $('#edRelationCode').val("");
    $('#edRelationName').val("");
    $("#edPoStatus").selectpicker('val', '');
    $("#EditPopup .color1 .btn_clear").css("display","none");

}
//赋值开发任务
function addPopup(){
	var rowData = $("#devPopupTable").bootstrapTable('getSelections')[0];
    if(typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        })
    } else {
       //$("#sel_systemId").val(rowData.id);
    	if(rowData.developmentMode!=null && rowData.developmentMode!=undefined){
         	$("#ndevelopmentMode").val(rowData.developmentMode);
         }else{
         	$("#ndevelopmentMode").val('');
         }
    	 $("#newSprintId").val(rowData.sprintId);
 		 $("#newSprintName").val(rowData.sprintName);
 		 
        $("#featureCode").val(rowData.FEATURE_NAME).change();
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
        $("#wSystemId").val(rowData.systemId);
        $("#Attribute").attr("featureCode",rowData.ID);
        $("#Attribute").attr("commissioningWindowId",rowData.COMMISSIONING_WINDOW_ID); 
        $("#Attribute").attr("requirementFeatureStatus",rowData.REQUIREMENT_FEATURE_STATUS);
        $("#DevPopup").modal("hide");
    }
	
}
//系统确认
function selectSystem(){
	// var rowData = $("#systemTable").bootstrapTable('getSelections')[0];
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
//需求确认
function selectReq(){
	var rowData = $("#reqTable").bootstrapTable('getSelections')[0];

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
	
}

//人员确认
function selectMan(){
	 // var rowData = $("#userTable").bootstrapTable('getSelections')[0];
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
	            $("#developer").val(names);
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
		 			names += selectTaskIds[i].FEATURE_code;
	 			}else{
	 				ids += selectTaskIds[i].id+",";
		 			names += selectTaskIds[i].FEATURE_code+',';
	 			}
	 			
	 		}
	            $("#parallelTask").val(names).change();
	       
	        $("#taskP").modal("hide");
	    }/*
	            $("#parallelTask").val(rowData.FEATURE_code);
	       
	        $("#taskP").modal("hide");
	    }*/
}

//选择开发任务弹框提交
function editPopup(){
	var rowData = $("#EditPopupTable").bootstrapTable('getSelections')[0];
    if(typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        })
    } else {
       //$("#sel_systemId").val(rowData.id);
    	
    	//必须在  $("#edfeatureCode")赋值前加 勿改 ztt
    	if(rowData.developmentMode!=null && rowData.developmentMode!=undefined){
         	$("#edevelopmentMode").val(rowData.developmentMode);
         }else{
         	$("#edevelopmentMode").val('');
         }
    	 $("#editSystemId").val(rowData.systemId);
    	 
    	 $("#editSprintId").val(rowData.sprintId);
 		 $("#editSprintName").val(rowData.sprintName);
    	 
    	 
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
        	$("#edendWork").val("").change();
        }
       
        $("#edAttribute").attr("edfeatureCode",rowData.ID);
        $("#edAttribute").attr("edcommissioningWindowId",rowData.COMMISSIONING_WINDOW_ID); 
        $("#edAttribute").attr("edrequirementFeatureStatus",rowData.REQUIREMENT_FEATURE_STATUS);
        $("#EditPopup").modal("hide");
    }
	
}


//隐藏编辑弹窗
function removeEdit(){
	  $("#EditPopup").modal("hide");
}

//清空人员搜索条件
function clearSearchUser(){
	$("#project").selectpicker('val', '');
	 $("#userName").val('');
	 $("#employeeNumber").val('');
	 $("#deptName").val('');
	 $("#companyName").val('');
	 $("#userModal .color1 .btn_clear").css("display","none");
}
//开发工作任务首页，显示搜索条件：开发人员
function showManPopup(){
	selectUserIds=[];
	clearSearchUser();
	$("#deptName").val("");
	$("#companyName").val("");
	$('select#project').prev().find('.bs-select-all').click();
	$("#userModal").modal("show");
}

//废弃
function devManPopup(){
	
	//getWithinDept();
	//getwithinCompany();
	cleanUser();
	$("#withinUserModal").modal("show");
	// tShowWithinManPopup();
}
//显示人员弹窗（废弃）
function tShowWithinManPopup(){
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
	}else if(type=="insertWorkTask"){
		var id='';
		userByGroupShow(id);
		$("#withinUserModal").modal("show");
	}else if(type=="updatetWorkTask"){
		var id = $("#edit_taskUserId").val();
		var divID = $("#taskID").val();
		userByGroupShow(id);
		
		$("#withinUserModal").modal("show");
	}
	
}

//编辑开发工作任务时，显示关联开发任务弹窗
function showEdPopup(){
	edclearstatus();
	EditDevPopup();
	//EdFeatureStatus();
	$("#EditPopup").modal("show");
}

//新增开发工作任务时，显示关联开发任务弹窗
function showAdPopup(){
	clearstatus();
	DevPopup();
	$("#DevPopup").modal("show");
}

function showRequirement(){
	clearReq();
	selectRequirement=[];
	reqPopup();
	$("#requirementP").modal("show");
}

function removeReq(){
	$("#requirementP").modal("hide");
}

//搜索条件，显示关联开发任务弹窗
function showTask(){
	selectTaskIds=[];
	clearTask();
	TaskPopup();
	$("#taskP").modal("show");
}



function removeSystem(){
	  $("#SystemP").modal("hide");
}

 

//系统状态
/*function ReqSystem(){
	$("#SystemType").empty();
	$.ajax({
		method:"post", 
		url:"/devManage/worktask/ReqSystem",
		success : function(data) {
			for(var i in data){
                  var opt = "<option value='" + i + "'>" + data[i] + "</option>";
                  $("#SystemType").append(opt);
                  }
			$('.selectpicker').selectpicker('refresh'); 
		}
		
	});
}*/
 

/*function searchInfoUser(){
	$("#loading").css('display','block');
	 var obj = {};
	 obj.userName = $.trim($("#userName").val());
	 obj.companyId =  $("#companyName").val();
	 obj.deptId = $("#deptName").val();
	 obj = JSON.stringify(obj); 
	$("#userTable").jqGrid('setGridParam',{ 
		url:'/system/user/getAllUser',
		    datatype:'json',
	       postData:{
	        "FindUser":obj
	       }, 
	       page:1
	   }).trigger("reloadGrid");  
	$("#loading").css('display','none');
}*/
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
        }
    });
}*/
//开发状态
/*function AdFeatureStatus(){
	$("#PoStatus").empty();
	$.ajax({
		method:"post", 
		url:"/devManage/worktask/FeatureStatus",
		success : function(data) {
			for(var i in data){
                  var opt = "<option value='" + i + "'>" + data[i] + "</option>";
                  $("#PoStatus").append(opt);
                  }
			$('.selectpicker').selectpicker('refresh'); 
		}
		
	});
}*/
//开发状态
/*function EdFeatureStatus(){
	$("#edPoStatus").empty();
	$.ajax({
		method:"post", 
		url:"/devManage/worktask/FeatureStatus",
		success : function(data) {
			for(var i in data){
                  var opt = "<option value='" + i + "'>" + data[i] + "</option>";
                  $("#edPoStatus").append(opt);
                  }
			$('.selectpicker').selectpicker('refresh'); 
		}
		
	});
}
*/
//提交备注
function addDevRemark(){
	var devTaskRemark=$.trim($("#tyaskRemark").val());
	if(devTaskRemark==""||devTaskRemark==undefined){
		layer.alert('备注信息不能为空！', {
            icon: 2,
            title: "提示信息"
        });
		return;
	}
	var id=$.trim($("#DevTaskID").val());
	var requirementFeatureId=$.trim($("#requirementFeatureId").val());
		 var obj = {};
		 obj.devTaskRemark =$.trim($("#tyaskRemark").val());
		 obj.devTaskId =id;
		var remark=JSON.stringify(obj); 
	$.ajax({
		type:"post",
		url:"/devManage/worktask/addTaskRemark",
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
			getSee(id,requirementFeatureId);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){ 
			layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
		}
		
	});
}
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
        }
    });
}
*/

/*function getwithinCompany() {
	$("#withinCompanyName").val("");
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
}*/
/*function getWithinDept() {
	 $("#withinDeptName").empty();
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
}*/
//清除人员搜索信息
function cleanUser(){
	$("#withinUserName").val("");
	$("#withinDeptName").val('');
	$("#withinCompanyName").val('');
	$("#withinUserModal .color1 .btn_clear").css("display","none");
	 
}
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
	 	        $("#withinUserModal").modal("hide");
	    	}else if(type=="Assign"){
	    		 $("#assignUser").val(rowData.USER_NAME).change();
	    		 $("#assignUser").attr("assignUserID",rowData.USER_ID);
	 	        $("#withinUserModal").modal("hide");
	    	}else if(type=="insertWorkTask"){
                $("#new_taskUserId").val(rowData.USER_ID).change();
                $("#new_taskUser").val(rowData.USER_NAME).change();
                $("#withinUserModal").modal("hide");
            }else if(type=="updatetWorkTask"){
                $("#edit_taskUserId").val(rowData.USER_ID).change();
                $("#edtaskUser").val(rowData.USER_NAME).change();
                $("#withinUserModal").modal("hide");
            }
	           
	    }
}
//获取当前登录用户所在项目（结项的项目除外） ztt
function getProject(){
	$("#project").empty();
	//var projectIds = '';
	$("#loading").show();
	$.ajax({
		url:"/devManage/displayboard/getAllProject",
		dataType:"json",
		type:"post",
		async:false,
		success:function(data){
			if(data.projects!=undefined && data.projects.length>0){
				for(var i=0;i <data.projects.length;i++){
					//projectIds += data.projects[i].id+",";
					$("#project").append("<option  selected  value='"+data.projects[i].id+"'>"+data.projects[i].projectName+"</option>")
					
				}
			}
			
			$('.selectpicker').selectpicker('refresh');
			if(data.status == '2'){
				layer.alert("查询项目失败！！！", { icon: 2});
			}
			$("#loading").hide();
		},
		error:function(){
			$("#loading").hide();
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
		}
	})
	//return projectIds;
	
}



//内人员弹窗
function withinUserShow(notWithUserID,devId){
	 $("#loading").css('display','block');
	    $("#withinUserTable").bootstrapTable('destroy');
	    $("#withinUserTable").bootstrapTable({
	        url:"/system/user/getAllDevUser",
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

function userByGroupShow(notWithUserID){
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
	                userStatus : $("#Working_state2").val(),//在职状态
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

//列表人员弹窗
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
	       // singleSelect : true,//单选
	        queryParams : function(params) {
	            var param = {
	                pageNumber:params.pageNumber,
	                pageSize:params.pageSize,
	                userName:$.trim($("#userName").val()),
	                deptName:$.trim($("#deptName").val()),
	                userStatus : $("#Working_state").val(),//在职状态
	                companyName:$.trim($("#companyName").val()),
	                systemId:"",
	                projectIds:projectIds,
	                id:notWithUserID
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
  	url:"/devManage/devtask/getAllFeature",
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
          	requirementFeatureStatus:$.trim($("#TaskPType").val()),
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
          field : "FEATURE_code",
          title : "任务编码",
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
     // singleSelect : true,//单选
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

//收藏JS
//搜索框 收藏按钮控制 js 部分
function collect_handle() {
  $(".collection2").click(function () {
	  collection2_obj.search = [];
    if ($(this).children("span").hasClass("fa-heart-o")) {
    	var involveSystem = [];
    	$('.involveSystem_tag_item').each(function (idx, val) {
    		involveSystem.push({
    			id : $(val).attr('userid'),
    			systemName : $(val).text(),
    		});
		});
    	collection2_obj.search.push(
        { "type": "input", "value": { "taskCode": $("#taskCode").val()},"isData": _is_null("taskCode") },
        { "type": "input", "value": { "taskName": $("#taskName").val()}, "isData":_is_null("taskName") },
        { "type": "select", "value": { "taskState":  change_str('taskState')}, "isData":_is_null("taskState") },
        { "type": "input", "value": { "requirement_Code": $("#requirement_Code").val()}, "isData":_is_null("requirement_Code") },
        { "type": "input", "value": { "requirement_Name": $("#requirement_Name").val()}, "isData":_is_null("requirement_Name") },
        { "type": "input", "value": { "developer": $("#developer").val()}, "isData":_is_null("developer") },
        { "type": "select", "value": { "sel_projectName": change_str('sel_projectName')}, "isData":_is_null("sel_projectName") },
        { "type": "input", "value": { "parallelTask": $("#parallelTask").val()}, "isData":_is_null("parallelTask") },
        
        { "type": "array", "value": { "involveSystem": involveSystem}, "isData":_is_null("involveSystem_ids") },
//        { "type": "input", "value": { "involveSystem_ids": $("#involveSystem_ids").val()}, "isData":_is_null("involveSystem_ids") },
        
        { "type": "select", "value": { "workSource": change_str('workSource')}, "isData":_is_null("workSource") },
        { "type": "input", "value": { "sprintNames": $("#sprintNames").val()}, "isData":_is_null("sprintNames") },
        { "type": "input", "value": { "sprintIds": $("#sprintIds").val()}, "isData":_is_null("sprintIds") }, 
        { "type": "select", "value": { "sDevPriority": change_str('sDevPriority')}, "isData":_is_null("sDevPriority") },
        { "type": "input", "value": { "dev_createDate": $("#dev_createDate").val()}, "isData":_is_null("dev_createDate") },
        )
      var isResult = collection2_obj.search.some(function (item) {
        return item.isData
      })
      collection2_obj.isCollect = isResult;
      if(isResult){
    	  $('#collect_Name').val('');
          $('#collect_Modal').modal('show');
      }
    } else {
      layer.confirm('确定要取消收藏吗?', {btn: ['确定', '取消'],icon:0, title: "提示"}, function () {
    	  collection2_obj.isCollect = false;
        collect_submit2();
      })
    }
  })
}
//收藏提交
function collect_submit2(){
  var sub_data = {};
  var sub_url = 'collectCondition';
  var is_name = true;
  if(collection2_obj.isCollect){
    if(!$('#collect_Name').val()){
      is_name = false;
    }
    sub_data = {
      'menuUrl': collection2_obj.menuUrl,
      'filterName': $('#collect_Name').val(),
      'defectReport': JSON.stringify(collection2_obj.search),
    }
  }else{
    sub_data = { id : $("#projectType2").val()};
    sub_url = 'updateDefectReport';
  };
  if(!is_name) {
    layer.alert('请填写方案名称!', {
      icon: 0,
    })
    return;
  };
  $("#loading").css('display', 'block');
  $.ajax({
    type: "post",
    url: "/report/defectReport/" + sub_url,
    dataType: "json",
    data: sub_data,
    success: function (data) {
        if(collection2_obj.isCollect){
          if (data.code == 1) {
	          $(".collection2").children("span").addClass("fa-heart").removeClass("fa-heart-o");
	          layer.alert('收藏成功!', {
	            icon: 1,
	          })
	          $('#collect_Modal').modal('hide');
          }
        }else{
        	clearSearch();
	      $(".collection2").children("span").addClass("fa-heart-o").removeClass("fa-heart");
	      layer.alert('已取消收藏!', {
	        icon: 1,
	      })
	    }
        getCollection2();
        $("#loading").css('display', 'none');
    },
    error: function () {
      $("#loading").css('display', 'none');
      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
    }
  });
}
function _is_null(ele){
	  return $('#'+ele).val() ? true : false;
	}
function getCollection2() {
	  $("#projectType2").empty();
	  $("#projectType2").append('<option class="defaultSelect" value="">选择收藏方案</option>');
	  $("#loading").css('display','block');
	  $.ajax({
	    url:"/report/defectReport/selectDefectReportList",
	    dataType:'json',
	    type:'post',
	    data:{
	      menuUrl:'../devManageui/worktask/toWorktask',
	    },
	    success : function(data){
	      if(data.length){
	        data.map(v=>{
	        	if(v.lastUseFlag==1){
	        		 $("#projectType2 .defaultSelect").after(`<option value="${v.id}">${v.filterName}</option>`); 
	    		  }else{
	    			  $("#projectType2").append(`<option value="${v.id}">${v.filterName}</option>`);  
	    		  }
	        })
	      }
	      $("#projectType2").selectpicker('refresh');
	      $("#loading").css('display','none');
	    },
	    error:function(){
	      $("#loading").css('display','none');
	      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	    }
	  })
	}
//切换收藏
function tab_option(This){
	clearSearch();
  var _id = $(This).val();
  if(!_id) {
	  $(".collection2").children("span").addClass("fa-heart-o").removeClass("fa-heart");
	  return;
  };
  is_check_Collect_flag = true;
  $("#loading").css('display','block');
  $.ajax({
    url:"/report/defectReport/selectDefectReportById",
    dataType:'json',
    type:'post',
    data:{
    "menuUrl": "../devManageui/worktask/toWorktask",
     id:_id,
    },
    success : function(data){
      var msg = JSON.parse(data.favoriteContent);
      if(msg){
    	for(var i=0;i<msg.length;i++){
          if (msg[i].type == "select") {
            for (var key in msg[i].value) {
              $("#" + key).val(change_str_num(msg[i].value[key]));
            }
          }else if (msg[i].type == "array") {
  			search_checkbox_give('involveSystem', $("#involveSystem_ids"), msg[i].value.involveSystem, 'systemName', 'id') //输入框id,存id元素,数组数据,展示字段名,展示字段id
          } else {
            for (var key in msg[i].value) {
              $("#" + key).val(msg[i].value[key]);
            }
          }
        }
        $(".selectpicker").selectpicker('refresh');
        $(".collection2").children("span").addClass("fa-heart").removeClass("fa-heart-o");
        searchInfo();
      }
      $("#loading").css('display','none');
    },
    error:function(){
      $("#loading").css('display','none');
      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
    }
  })
}
function change_str(ele){
	  if($('#'+ele).val()){
		  if(ele=="workSource"||ele=="sDevPriority"){
			  return $('#'+ele).val();
		  }else{
			  var   arr = $('#'+ele).val().map(v=>{ 
				  return v > 9 ? v : '0'+v
			  })  
			  return arr.join();
		  }
	    
	   
	  }else{
	    return '';
	  }
	}

function change_str_num(str){
	  if(str){
		return str.split(',').map(v=>{ 
	      return v > 9 ? v : +v
	    })
	  }else{
	    return '';
	  }
	}

function other() {
	  var search_ele = ['taskCode','taskName','taskState','requirement_Code','requirement_Name','developer','sel_projectName','parallelTask','involveSystem','workSource','sprintNames','sDevPriority','dev_createDate'];
	  search_ele.map(function(ele){
		  $('#'+ ele).on('change', function () {
			  if(is_check_Collect_flag){
				  $(".collection2").children("span").removeClass("fa-heart").addClass("fa-heart-o");
			  }
		  })
	  })
}





