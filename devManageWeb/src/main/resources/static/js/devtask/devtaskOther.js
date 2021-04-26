/**
 * Created by ztt
 */
  var reqStatus = '';
  var systemId = '';
  var selectDftIds = [];
  var selectReqIds = [];
  var selectUserIds = [];
  var selectSysIds = [];
  var selectWinIds = [];
	var selectSprints = [];
	
	var is_two_search_flag = false;


var collection_obj = {
	  menuUrl : '../devtask/toDevTask',
	  filterName : uid,    //当前用户id
	  search : '',
	  isCollect : false ,
	  table_list : '#list2' ,	//表格id
};
  
$(function(){
	search_settings();// 人员模糊查询监听器
	$("#two_search_div_btn").click(function(){
		if(is_two_search_flag){
			is_two_search_flag = false;
			$("#two_search_div").hide();
		}else{
			is_two_search_flag = true;
			$("#two_search_div").show();
		}
	})
	
	$('#userModalreqF').on('hide.bs.modal', function () {
	     $("#Working_state").val(1);
		 $("#Working_state").selectpicker('refresh');
	});
	
	
	$("#sprintNames").click(function(){
		clearSearchSpr();
		selectSprints = [];
		$("#sprintModal").modal("show");
		sprintAll();
	})
	 $("#win_windowDate").datetimepicker({
    	minView:"month",
    	format: "yyyy-mm-dd",
    	autoclose: true,
    	todayBtn: true,
    	language: 'zh-CN',
    	pickerPosition: "bottom-left"
    });
//    $("#requirementName").click(function(){
//    	$("#reqbutton").attr("data-req","list");
//    	$("#reqStatus").empty();
//    	selectReqIds = [];
//    	$("#selReq").modal("show");
//    	reqStatus = '';
//    	clearSearchReq(); 
//    	getReqStatus();
//    	reqTableShowAll();
//    });
    $("#newrequirementName").click(function(){
    	$("#reqbutton").attr("data-req","new");
    	$("#reqStatus").empty();
    	$("#selReq").modal("show");
    	reqStatus = 'cancel';
    	clearSearchReq(); 
    	getReqStatus();
    	reqTableShow();
    });
    $("#editrequirementName").click(function(){
    	$("#reqbutton").attr("data-req","edit");
    	$("#reqStatus").empty();
    	$("#selReq").modal("show");
    	reqStatus = 'cancel';
    	clearSearchReq(); 
    	getReqStatus();
    	reqTableShow();
    });
    
    $("#systemNameReq").click(function(){
    	$("#sysbutton").attr("data-sys","list");
    	selectSysIds=[];
    	 $("#selSys").modal("show");
    	 clearSearchSys();
    	 systemTableShowAll();
		});
    
    $("#newsystemName").click(function(){
    	if(!$("#newProject_listId").val()){
    		layer.msg('请先选择项目!',{icon:0});
    		return;
    	}
    	$("#sysbutton").attr("data-sys","new");
		 $("#selSys").modal("show");
		 clearSearchSys();
		 systemTableShow2();
    });
    $("#editsystemName").click(function(){
    	$("#sysbutton").attr("data-sys","edit");
		 $("#selSys").modal("show");
		 clearSearchSys();
		 systemTableShow2();
   });
  
//    $("#devManPostName").click(function(){
//    	$("#userbutton").attr("data-user", "list");
//    	$("#deptName").empty();
//    	$("#companyName").empty();
//    	selectUserIds =[];
//    	 $("#userModalreqF").modal("show");
//    	 clearSearchUser();
//    	 userTableShow2();
//    });
//    $("#execteUserName").click(function(){
//    	$("#userbutton").attr("data-user", "list2");
//    	$("#deptName").empty();
//    	$("#companyName").empty();
//    	selectUserIds =[];
//    	 
//    	$("#userModalreqF").modal("show");
//    	clearSearchUser();
//    	userTableShow2();
//     });
    /*$("#newdevManageUserName").click(function(){
    	systemId = $("#newsystemId").val();
    	excuteUserName='';
    	$("#userbutton").attr("data-user", "newMan");
    	$("#deptName").empty();
    	$("#companyName").empty();
    	 
    	 $("#userModalreqF").modal("show");
    	 clearSearchUser();
    	 userTableShow2();
    });*/
    /*$("#editdevManageUserName").click(function(){
    	systemId = $("#editsystemId").val();
    	excuteUserName='';
    	$("#userbutton").attr("data-user", "editMan");
    	$("#deptName").empty();
    	$("#companyName").empty();
    	 
    	 $("#userModalreqF").modal("show");
    	 clearSearchUser();
    	 userTableShow2();
    });*/
    
    /*$("#newexecuteUserName").click(function(){
    	systemId = $("#newsystemId").val();
    	excuteUserName='';
    	$("#userbutton").attr("data-user", "new");
    	$("#deptName").empty();
    	$("#companyName").empty();
    	 
	   	 $("#userModalreqF").modal("show");
		 clearSearchUser();
		 userTableShow2();
	});*/
    
    /*$("#editexecuteUserName").click(function(){
    	systemId = $("#editsystemId").val();
    	excuteUserName='';
    	$("#userbutton").attr("data-user", "edit");
    	$("#deptName").empty();
    	$("#companyName").empty();
    	 
	   	 $("#userModalreqF").modal("show");
		 clearSearchUser();
		 userTableShow2();
	});*/
    /*$("#updateexecuteUserName").click(function(){
    	excuteUserName='';
    	$("#userbutton").attr("data-user", "allupdate");
    	$("#deptName").empty();
    	$("#companyName").empty();
    	 
	   	 $("#userModalreqF").modal("show");
		 clearSearchUser();
		 userTableShow2();
	});*/
    $("#newSynDefectCode").click(function(){
        $("#dftbutton").attr("data-dft","newSyn");
        $("#defModal").modal("show");
        $("#dftReqFeatureId").val('');
        selectDftIds = [];
        resetDft();
        dftTableShow();
    })
//    $("#editSynDefectCode").click(function(){
//        $("#dftbutton").attr("data-dft","editSyn");
//        $("#defModal").modal("show");
//        selectDftIds = [];
//        resetDft();
//        dftTableShow();
//    })

    $("#newdefectCode").click(function(){
    	$("#dftbutton").attr("data-dft","new");
    	$("#defModal").modal("show");
    	$("#dftReqFeatureId").val('');
    	selectDftIds = [];
        resetDft();
    	dftTableShow();
    })
    $("#editdefectCode").click(function(){
    	$("#dftbutton").attr("data-dft","edit");
    	$("#defModal").modal("show");
    	selectDftIds = [];
        resetDft();
    	dftTableShow();
    })
    /*$("#transferUserName").click(function(){
    	$("#userbutton").attr("data-user", "transfer");
    	$("#deptName").empty();
    	$("#companyName").empty();
    	 
	   	$("#userModalreqF").modal("show");
	   	systemId = $("#transferSystemId").val();
		clearSearchUser();
		userTableShow2();
    })*/
    
     
    
//    $("#planVersionName").click(function(){
//    	$("#windowButton").attr("data-window","list");
//    	winReset();
//    	selectWinIds=[];
//    	$("#comWindowModal").modal("show");
//    	commitWindowAll();
//    });
    
     $("#newcommitWindowName").click(function(){
    	$("#windowButton").attr("data-window","new");
    	winReset();
    	selectWinIds=[];
    	$("#comWindowModal").modal("show");
    	commitWindow();
    });
     
     $("#editcommitWindowName").click(function(){
    	$("#windowButton").attr("data-window","edit");
    	winReset();
    	selectWinIds=[];
    	$("#comWindowModal").modal("show");
    	commitWindow();
    });
     
    
    $("#systemSearch").click(function(){
    	if($("#sysbutton").attr("data-sys") == "list"){
    		systemTableShowAll();
    	}else{
    		systemTableShow2();
    	}
    });
    $("#reqSearch").click(function(){
    	if($("#reqbutton").attr("data-req") == "list"){
    		reqTableShowAll();
    	}else{
    		reqTableShow();
    	}
    });

    $("#newProjectPlanName").click(function(){
    	if($("#newsystemId").val()==''){
            layer.msg("请先选择系统！！！", { icon: 0});
		}else{
            $("#planbutton").attr("data-ProjectPlan", "new");
            $("#planCode").empty();
            $("#planName").empty();
            $("#responsibleUserName").empty();
            $("#projectName").empty();

            $("#selProjectPlan").modal("show");
            clearSearchPlan();
            planTableShow();
		}
    });

    $("#editProjectPlanName").click(function(){
        if($("#editsystemId").val()==''){
            layer.msg("请先选择系统！！！", { icon: 0});
        }else{
            $("#planbutton").attr("data-ProjectPlan", "edit");
            $("#planCode").empty();
            $("#planName").empty();
            $("#responsibleUserName").empty();
            $("#projectName").empty();

            $("#selProjectPlan").modal("show");
            clearSearchPlan();
            planTableShow();
		}
    });
   
    
    defectStatusList = $("#defectStatus").find("option");
    getProject();
    _select_box_show();
})

//更多操作弹窗
function _select_box_show() {
	$(document).on('click', '._select_box_show', function (e) {
        $('._select_box_menu').hide();
		var top = $(this).offset().top - $(document).scrollTop();
        var left = $(this).offset().left - $(document).scrollLeft();
		$(this).next().css({
			position: 'fixed',
			top: top + $(this).height() + 'px',
			left: left - 15 + 'px',
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

//===========================项目========================
//选择项目配置
function select_project() {
	fuzzy_search_radio({
		'ele': 'newProject_list',
		'url': '/devManage/devtask/getProjectListByProjectName',
		'params': {
			projectName: '',
		},
		'name': 'projectName',
		'id': 'id',
		'top': '26px',
		'dataName': 'projectInfoList',
		'userId': $('#newProject_listId'),
		'out': true,
		'not_jqgrid': true,
		'otherDataName': 'systemInfoList',
	});
	$('#newProject_list_list').on('click', '.newProject_list_search_item ', function () {
		var data = JSON.parse($(this).attr('data'));
		var other_data = $(this).data('other');
		var val = data.projectName;
		var id = $(this).data('id');
		var systemId = data.systemId;
		var systemName = data.systemName1;
		var projectType = data.projectType;
		$('#newProject_listId').val(id);
		$('#newProject_list').val(val).attr('username', val).change();
		$('#newProject_list_list').hide();
//		if(other_data){
//			let is_system_flag = false;
//			other_data.length && other_data.map(function(v){
//				if($('#newsystemId').val() == v.id){
//					is_system_flag = true;
//				}
//			})	
//			if(!is_system_flag){
//				$('#newsystemId').val('');
//				$('#newsystemName').val('').change();
//			}
//		}else{
			$('#editsystemId').val('');
			$('#editsystemName').val('').change();
			$('#newsystemId').val('');
			$('#newsystemName').val('').change();
//		}
		if (systemId) {
			$('#newsystemId').val(systemId);
			$('#newsystemName').val(systemName).change();
			$('#editsystemId').val(systemId);
			$('#editsystemName').val(systemName).change();
		}
		if(projectType == 2){
			$('.is_new_project').hide();
			$('.is_oac_project').show();
		}else{
			$('.is_new_project').show();
			$('.is_oac_project').hide();
		}
	})
}

//===========================项目========================
//查询关联项目
/*function getProject_list(){
	$("#Project_Table").bootstrapTable('destroy');
	$("#Project_Table").bootstrapTable({  
		url:"/devManage/systeminfo/getProjectListByProjectName",
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
				projectName:$("#project_Name").val(),
                projectCode:$("#project_Code").val(),
				pageNumber:params.pageNumber,
				pageSize:params.pageSize, 
			}
			return param;
		},
		responseHandler : function(res) {
			return {
					total:res.total,
					rows:res.projectInfo,
			};
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
				field : "projectCode",
				title : "项目编码",
				align : 'center'
		},{
				field : "projectName",
				title : "项目名称",
				align : 'center'
		}],
		onLoadSuccess:function(a,b,c,d){
				console.log(a,b,c,d)
		},
		onLoadError : function() {

		}
	});
	$("#project_modal").modal('show');
}

//关联项目弹窗搜索清空
function clearSearchProject(){
	$('#project_Name').val('');
	$('#project_Code').val('');
}

//选择项目
function select_project(This){
	var selectContent = $("#Project_Table").bootstrapTable('getSelections')[0];
	if(!selectContent) {
		layer.alert('请选择一列数据！', {icon: 0});
		return false;
	}else{
		$("#editsystemId").val('').removeAttr('systemcode');
		$("#editsystemName").val('').change();
		$("#newsystemId").val('').removeAttr('systemcode');
		$("#newsystemName").val('').change();
		$("#projectInput" ).val("");
		$("#modalInput" ).val("");
		$("#projectOwn").empty();
		$("#newsystemVersionBranch").val('').empty();
		$("#repairVersionBranch").val('').empty();

		$("#editsystemVersionBranch").empty();
		$("#editSprintId").empty();
		$("#editdevTaskStatus").empty();
		

		$("#newProject_listId").val(selectContent.id);
		$("#newProject_list").val(selectContent.projectName).change();
		$("#ndevelopmentMode").val(selectContent.developmentMode);

		$("#newsprintDiv").hide();
		$("#newSprintId").empty().selectpicker('refresh');
		$("#newstoryPointDiv").hide();

		$("#sprintDiv").hide();
		$("#editSprintId").empty();
		$("#estoryPointDiv").hide();
		$("#newsystem_div").show();
		$("#project_modal").modal('hide');
	}
}*/

//===========================冲刺========================
function sprintAll(){
	$("#sprintTable").bootstrapTable('destroy');
    $("#sprintTable").bootstrapTable({  
    	url:"/devManage/sprintManage/getAllsprint",
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
        		 "sprintName" :  $.trim($("#sprintName").val()),
                 "systemName" :  $("#ssystemName").val(),
                 pageNum:params.pageNumber,
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
            field : "sprintName",
            title : "冲刺名称",
            align : 'center'
        },{
            field : "systemName",
            title : "所属系统",
            align : 'center'
        },{
        	field : "sprintStartDate",
        	title : "开始日期",
        	align : 'center'
        },{
        	field : "sprintEndDate",
        	title : "结束日期",
        	align : 'center'
        }],
        onLoadSuccess:function(){
        	 
        },
        onLoadError : function() {

        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		selectSprints.push(rows[i]);
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectSprints.indexOf(rows[i])>-1){
        			selectSprints.splice(selectSprints.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectWinIds.indexOf(row)<=-1){
        	selectSprints.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectSprints.indexOf(row)>-1){
        		selectSprints.splice(selectSprints.indexOf(row),1);
        	}
        }
    });
}
function commitSprint(){
	if(selectSprints.length<=0){
		 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	}else{
		var ids = '';
		var names = '';
		for(var i=0;i<selectSprints.length;i++){
			ids += selectSprints[i].sprintIdList+",";
			names += selectSprints[i].sprintName+',';
		}
	    $("#sprintIds").val(ids);
		$("#sprintNames").val(names.substring(0,names.length-1)).change();
		$("#sprintModal").modal("hide");
	}
	
	
}
function clearSearchSpr(){
	$("#sprintName").val("");
	$("#ssystemName").val("");
}
//========================投产窗口start=====================
function commitWindow(){
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
        singleSelect : true,//单选
        queryParams : function(params) {
             var param={ 
        		 "windowName" :  $.trim($("#win_windowName").val()),
                 "windowDate" :  $("#win_windowDate").val(),
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
function commitWindowAll(){
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
                 "windowDate" :  $("#win_windowDate").val(),
                 "windowType" :  $("#win_windowType").val(),
        		 pageNumber:params.pageNumber,
        		 pageSize:params.pageSize, 
             }
            return param;
        },
        responseHandler : function(res) {
            return {
                total:res.total,
                rows:res.rows,
						};
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
        	formatter:function(value,row,index){
        		var typeList = $("#win_windowType").find("option");
        		for(var i =0;i<typeList.length;i++){
        			if(typeList[i].value == value){
        				return typeList[i].innerHTML;
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
	if($("#windowButton").attr("data-window")== 'list'){
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
			if($("#windowButton").attr("data-window")== 'list'){
			    $("#planVersion").val(ids);
				$("#planVersionName").val(names.substring(0,names.length-1)).change();
			}
	 	}
	}else{
		var selectContent = $("#comWindowTable").bootstrapTable('getSelections')[0];
	    if(typeof(selectContent) == 'undefined') {
	    	 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	    }else{
	    	 if($("#windowButton").attr("data-window") == 'new'){
				 $("#newcommitWindowId").val(selectContent.id);
				 $("#newcommitWindowName").val(selectContent.windowName).change();
			 }
			 if($("#windowButton").attr("data-window")== 'edit'){
				 $("#editcommitWindowId").val(selectContent.id);
				 $("#editcommitWindowName").val(selectContent.windowName).change();
			 }
			
	    }
	}
	 
	$("#comWindowModal").modal("hide");

}
function winReset(){
	$("#win_windowName").val("");
	$("#win_windowDate").val("");
	$("#win_windowType").selectpicker('val', '');
	
}

//=========================缺陷弹框start=====================
function dftTableShow(){
    var featureSource;
    if($("#dftbutton").attr("data-dft") == "newSyn" || $("#dftbutton").attr("data-dft") == "new"){
	 	featureSource = $("#newrequirementFeatiureSource").val();
    }
    if($("#dftbutton").attr("data-dft") == "editSyn" || $("#dftbutton").attr("data-dft") == "edit"){
        featureSource = $("#editrequirementFeatureSource").val();
    }
    $("#defectList").bootstrapTable("destroy");
    $("#defectList").bootstrapTable({
        url:"/devManage/devtask/listDftNoReqFeature",
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        queryParams : function(params) {
            var param={
                featureSource : featureSource,
            	reqFeatureId : $("#dftReqFeatureId").val(),
                defectCode:$.trim($("#defectCode").val()),
                defectSummary:$.trim($("#defectSummary").val()),
                defectStatus:$.trim($("#defectStatus").find("option:selected").val()),
                pageNumber:params.pageNumber,
                pageSize:params.pageSize
            };
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
            formatter : stateFormatter
        },{
            field : "defectCode",
            title : "缺陷编号",
            align : 'center',
        },{
            field : "defectSummary",
            title : "缺陷摘要",
            align : 'left'
        },{
            field : "defectStatus",
            title : "缺陷状态",
            align : 'center',
            formatter : function(value, row, index) {
                for (var i = 0,len = defectStatusList.length;i < len;i++) {
                    if(row.defectStatus == defectStatusList[i].valueCode){
                       // var _status = "<input type='hidden' id='list_defectStatus' value='"+defectStatusList[i].valueName+"'>";
                        return defectStatusList[i].valueName //+_status
                    }
                }
            }
        },{
            field : "submitUserName",
            title : "提出人",
            align : 'center',
            formatter : function(value, row, index) {
                return toStr(row.submitUserName) + "  |  " + toStr(row.submitDate);
            }
        }
        ],
        onLoadSuccess:function(){
            $("#loading").css('display','none');
        },
        onLoadError : function() {

        },
       
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectDftIds.indexOf(rows[i])<=-1){
        			selectDftIds.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectDftIds.indexOf(rows[i])>-1){
        			selectDftIds.splice(selectDftIds.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectDftIds.indexOf(row)<=-1){
        		selectDftIds.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectDftIds.indexOf(row)>-1){
        		selectDftIds.splice(selectDftIds.indexOf(row),1);
        	}
        }
    });
}

function stateFormatter(value, row, index) {
    var defectArr={};
    if($("#dftbutton").attr("data-dft") == "edit") {
        defectArr = $("#editdefectId").val().split(",");
    }
    if($("#dftbutton").attr("data-dft") == "editSyn" ) {
        defectArr = $("#editSynDefectId").val().split(",");
    }
	for (var i = 0; i < defectArr.length; i++) {
		if (defectArr[i] == row.id) {
			var flag = 0;
			for (var key in selectDftIds) {
				if (selectDftIds[key].id == row.id) {
					var flag = 1;
				}
			}
			if (flag != 1) {
				selectDftIds.push(row);
			}
			return {
				//disabled : true,//设置是否可用
				checked: true//设置选中
			};
		}
	}
	return value;
}
	
function commitDft(){
	//var selectContent = $("#defectList").bootstrapTable('getSelections');
	
	if(selectDftIds.length <=0) {
    	 layer.alert('请选择一列数据！', {icon: 0});
       return false;
    }else{
    	var ids = '';
    	var codes = '';
    	for(var i =0;i<selectDftIds.length;i++){
    		ids += selectDftIds[i].id+",";
    		codes += selectDftIds[i].defectCode+",";
    	}
		if($("#dftbutton").attr("data-dft") == "new"){
			$("#newdefectId").val(ids);
			$("#newdefectCode").val(codes).change();
		}
		if($("#dftbutton").attr("data-dft") == "edit"){
			$("#editdefectId").val(ids);
			$("#editdefectCode").val(codes).change();
		}
        if($("#dftbutton").attr("data-dft") == "newSyn"){
            $("#newSynDefectId").val(ids);
            $("#newSynDefectCode").val(codes).change();
        }
        if($("#dftbutton").attr("data-dft") == "editSyn"){
            $("#editSynDefectId").val(ids);
            $("#editSynDefectCode").val(codes).change();
        }
		$("#defModal").modal('hide');
    }
}

function resetDft(){
    $("#defectCode").val('');
    $("#defectSummary").val('');
    $("#defectStatus").selectpicker('val', '');
    $(".color1 .btn_clear").css("display","none");
}
//=========================缺陷弹框end ======================

//=========================需求弹框start=====================
function reqTableShow(){
	$("#listReq").bootstrapTable('destroy');
    $("#listReq").bootstrapTable({  
    	url:"/projectManage/requirement/getAllRequirement2",
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
            	 requirementCode : $.trim($("#reqCode").val()),
            	 requirementName  :$.trim( $("#reqName").val()),
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
        	 
        },
        onLoadError : function() {

        }
    });
}

function reqTableShowAll(){
	$("#listReq").bootstrapTable('destroy');
    $("#listReq").bootstrapTable({  
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
            	 requirementCode : $.trim($("#reqCode").val()),
            	 requirementName  :$.trim( $("#reqName").val()),
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
        	 
        },
        onLoadError : function() {

        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectReqIds .indexOf(rows[i])<=-1){
        		selectReqIds.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectReqIds.indexOf(rows[i])>-1){
        			selectReqIds.splice(selectReqIds.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectReqIds.indexOf(row)<=-1){
        	  selectReqIds.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectReqIds.indexOf(row)>-1){
        		selectReqIds.splice(selectReqIds.indexOf(row),1);
        	}
        }
    });
}

function getReqStatus(){
	 $.ajax({
	  type:"POST",
      url:"/projectManage/requirement/getDataDicList",
      dataType:"json",
      data:{
      	datadictype:'reqStatus'        	
  	},
  	 success:function(data){
  		$("#reqStatus").empty();
  		$("#reqStatus").append("<option value=''>请选择</option>")
  		for(var i=0;i<data.length;i++){
            var opt = "<option value='" + data[i].valueCode + "'>" + data[i].valueName + "</option>";
            $("#reqStatus").append(opt);
            }
		$('.selectpicker').selectpicker('refresh'); 
  	 }
	 });
}

function insertInput(){
	if($("#reqbutton").attr("data-req") == "list"){
		if(selectReqIds.length<=0){
	 		 layer.alert('请选择一列数据！', {icon: 0});
	 	       return false;
	 	}else{
	 		var ids = '';
	 		var codes = '';
	 		for(var i=0;i<selectReqIds.length;i++){
	 			ids += selectReqIds[i].id+",";
	 			codes += selectReqIds[i].REQUIREMENT_CODE+',';
	 		}
	 		$("#requirementId").val(ids);
			$("#requirementName").val(codes).change();
	 	}
		
	}else{
		var selectContent = $("#listReq").bootstrapTable('getSelections')[0];
	    if(typeof(selectContent) == 'undefined') {
	    	 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	    }else{
	    	if($("#reqbutton").attr("data-req") == "new"){
				$("#newrequirementId").val(selectContent.id);
				$("#newrequirementName").val(selectContent.REQUIREMENT_CODE).change();
			}
			if($("#reqbutton").attr("data-req") == "edit"){
				$("#editrequirementId").val(selectContent.id);
				$("#editrequirementName").val(selectContent.REQUIREMENT_CODE).change();
			}
	    }
	}

	$("#selReq").modal("hide");
	
}

//重置
function clearSearchReq() {	
    $('#reqCode').val("");
    $('#reqName').val("");
    $("#reqStatus").selectpicker('val', '');
//    $(".color1 .btn_clear").css("display","none");
}
//===========================需求弹框end=================================

//====================涉及系统弹框start==========================
function systemTableShow2(){
	if($("#z_project_Id").val()){
		$("#SC_project_body").show();
	}  
	$("#SCsystemFlag_body").hide();
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
				let sys_name = $.trim($("#SCsystemName").val());
					var param={ 
						systemName:sys_name,
						systemCode:$.trim($("#SCsystemCode").val()),
						projectId:$("#newProject_listId").val(),
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
function systemTableShowAll(){
	if($("#z_project_Id").val()){
		$("#SC_project_body").hide();
	}
	$("#SCsystemFlag_body").show();
	$("#systemTable2").bootstrapTable('destroy');
	$("#loading").show();
	$("#systemTable2").bootstrapTable({  
		url:"/devManage/systeminfo/selectAllSystemInfo2",
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
						systemName:$.trim($("#SCsystemName").val()),
						systemCode:$.trim($("#SCsystemCode").val()),
						systemFlag:$.trim($("#SCsystemFlag").val()),
						flag:1,
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
			},
			/*{
				field : "systemShortName",
				title : "子系统简称",
				align : 'center'
			},{
				field : "projectName",
				title : "所属项目",
				align : 'center'
			}*/
			{
				field : "manangeUserNames",
				title : "项目管理岗",
				align : 'center'
			}
			],
			onLoadSuccess:function(){
				$("#loading").hide();
			},
			onLoadError : function() {
				$("#loading").hide();
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

function commitSys(){
	 if($("#sysbutton").attr("data-sys") == "list"){
		 if(selectSysIds.length<=0){
	 		 layer.alert('请选择一列数据！', {icon: 0});
	 	       return false;
	 	}else{
	 		var ids = '';
	 		var names = '';
	 		for(var i=0;i<selectSysIds.length;i++){
	 			ids += selectSysIds[i].id+",";
	 			names += selectSysIds[i].systemName+',';
	 		}
			$("#systemId").val(ids);
	    	$("#systemNameReq").val(names).change();
	 	}
	 }else{
		 var selectContent = $("#systemTable2").bootstrapTable('getSelections')[0];
	     if(typeof(selectContent) == 'undefined') {
	    	 layer.alert('请选择一列数据！', {icon: 0});
	        return false;
	     }else{
	    	 if($("#sysbutton").attr("data-sys") == "new"){
	     		$("#newsystemId").val(selectContent.id); 
	     		$("#newsystemId").attr( "systemCode", selectContent.systemCode);  
	     		if(selectContent.developmentMode!=null && selectContent.developmentMode!=undefined){
	   	    		$("#ndevelopmentMode").val(selectContent.developmentMode);
	   	    	}else{
	   	    		$("#ndevelopmentMode").val('');
	   	    	}
	  	    	$("#newsystemName").val(selectContent.systemName).change();
	     	 }
	     	 if($("#sysbutton").attr("data-sys") == "edit"){
	      		$("#editsystemId").val(selectContent.id);
	      		$("#editsystemId").attr( "systemCode", selectContent.systemCode);
	      		if(selectContent.developmentMode!=null && selectContent.developmentMode!=undefined){
	   	    		$("#edevelopmentMode").val(selectContent.developmentMode);
	   	    	}else{
	   	    		$("#edevelopmentMode").val('');
	   	    	}
	   	    	$("#editsystemName").val(selectContent.systemName).change();
	      	 }
	     }
	 } 
	 $("#selSys").modal("hide"); 
}
//重置
function clearSearchSys(){
	$("#SCsystemName").val('');
	$("#SCsystemCode").val('');
	$("#SCsystemFlag").val('');
//	 $(".color1 .btn_clear").css("display","none");
}
//====================涉及系统弹框end==========================

//====================人员弹框start=======================
function userTableShow2(){
	$("#loading").css('display','block');
	var projectIds = $("#project").val();
	if(projectIds!=null && projectIds!=''){
		projectIds = projectIds.join(",");
	}
	$('select#project').prev().find('.bs-select-all').click();
	if($("#userbutton").attr("data-user")== 'list' || $("#userbutton").attr("data-user")== 'list2'){
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
	     //   singleSelect : true,//单选
	        queryParams : function(params) {
		             var param={
	        			// id:excuteUserName,
	        			//systemId:systemId,
		            	 projectIds:projectIds,
		         		 userName: $.trim($("#userNameReqF").val()),
		         		 companyName :  $("#companyName").val(),
		         		 deptName : $("#deptName").val(),
		         		 userStatus : $("#Working_state").selectpicker('val'),//在职状态
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
	}else{
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
	        			 id:excuteUserName,
	        			 projectIds:projectIds,
		         		 userName: $.trim($("#userNameReqF").val()),
		         		 companyName :  $("#companyName").val(),
		         		 userStatus : $("#Working_state").selectpicker('val'),//在职状态
		         		 deptName : $("#deptName").val(),
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
	        	 $("#loading").css('display','none');
	        },
	        onLoadError : function() {
	
	        }
	    });
	}
		
}
function userTableShowAll(){
	$("#loading").css('display','block');
	var projectIds = $("#project").val();
	if(projectIds!=null && projectIds!=''){
		projectIds = projectIds.join(",");
	}
	if($("#userbutton").attr("data-user")== 'list' || $("#userbutton").attr("data-user")== 'list2'){
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
	     //   singleSelect : true,//单选
	        queryParams : function(params) {
		             var param={
	        			// id:excuteUserName,
	        			//systemId:systemId,
		            	 projectIds:projectIds,
		         		 userName: $.trim($("#userNameReqF").val()),
		         		 companyName :  $("#companyName").val(),
		         		 deptName : $("#deptName").val(),
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
	}else{
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
	        			 id:excuteUserName,
	        			 projectIds:projectIds,
		         		 userName: $.trim($("#userNameReqF").val()),
		         		 companyName :  $("#companyName").val(),
		         		 deptName : $("#deptName").val(),
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
	        	 $("#loading").css('display','none');
	        },
	        onLoadError : function() {
	
	        }
	    });
	}
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
        }
    });
}*/
function commitUserReqF(){	 
	if($("#userbutton").attr("data-user")== 'list' || $("#userbutton").attr("data-user")== 'list2'){
		if(selectUserIds.length<=0){
	 		 layer.alert('请选择一列数据！', {icon: 0});
	 	       return false;
	 	}else{
	 		var ids = '';
	 		var names = '';
	 		for(var i=0;i<selectUserIds.length;i++){
	 			ids += selectUserIds[i].id+",";
	 			names += selectUserIds[i].userName+',';
	 		}
			if($("#userbutton").attr("data-user")== 'list'){
			    $("#devManPost").val(ids);
				$("#devManPostName").val(names).change();
			}
	   		if($("#userbutton").attr("data-user")== 'list2'){
	   			$("#execteUserId").val(ids);
	   			$("#execteUserName").val(names).change();
	   		 }
	 	}
	}else{
		var selectContent = $("#userTableReqF").bootstrapTable('getSelections')[0]; 
	    if(typeof(selectContent) == 'undefined') {
	    	 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	    }else{
	    	/* if($("#userbutton").attr("data-user") == 'new'){
				 $("#newexecuteUser").val(selectContent.id);
				 $("#newexecuteUserName").val(selectContent.userName).change();
			 }*/
			 /*if($("#userbutton").attr("data-user")== 'edit'){
				 $("#editexecuteUser").val(selectContent.id);
				 $("#editexecuteUserName").val(selectContent.userName).change();
			 }*/
			 /*if($("#userbutton").attr("data-user")==  'newMan'){
				 $("#newdevManageUser").val(selectContent.id);
				 $("#newdevManageUserName").val(selectContent.userName).change();
			 }*/
			 /*if($("#userbutton").attr("data-user")==  'editMan'){
				 $("#editdevManageUser").val(selectContent.id);
				 $("#editdevManageUserName").val(selectContent.userName).change();
			 }*/
			 
			 /*if($("#userbutton").attr("data-user")==  'transfer'){
				 $("#transferId").val(selectContent.id);
				 $("#transferUserName").val(selectContent.userName).change();
			 }*/
			 if($("#userbutton").attr("data-user")=="split"){
				 $("#sWorkDivid").val(selectContent.id);
				 $("#sWorkDividUserName").val(selectContent.userName).change();
			 }
			 /*if($("#userbutton").attr("data-user")=="allupdate"){
				 $("#updateexecuteUserId").val(selectContent.id);
				 $("#updateexecuteUserName").val(selectContent.userName).change();
			 }*/
			 if($("#userbutton").attr("data-user")=="other"){
				 $( thisInputVal ).prev().val(selectContent.id);
				 $( thisInputVal ).val( selectContent.userName ).change();
			 }
	    }
	}
	 
	$("#userModalreqF").modal("hide");

}

function clearSearchUser(){
	 $("#userNameReqF").val('');
	 $("#employeeNumber").val('');
	/* $("#deptName").selectpicker('val', '');
	 $("#companyName").selectpicker('val', '');*/
	 $("#deptName").val('');
	 $("#companyName").val('');
//	 $(".color1 .btn_clear").css("display","none");
}

function clearSearchUser2(){
	$("#userNameReqF").val('');
	$("#employeeNumber").val('');
	$("#deptName").val('');
	$("#companyName").val('');
	$("#project").selectpicker('val', '');
	$("#project").selectpicker('refresh');
	$(".color1 .btn_clear").css("display","none");
}
//====================人员弹框end=======================

//=========================项目计划弹窗start=====================
function planTableShow(){
	var systemId;
    if($("#planbutton").attr("data-ProjectPlan") == "new"){
        systemId = $("#newsystemId").val();
    }else{
        systemId = $("#editsystemId").val();
	}
		$('#loading').show();
    $("#planTable2").bootstrapTable('destroy');
    $("#planTable2").bootstrapTable({
        url:"/devManage/devtask/getAllProjectPlan",
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
                planCode : $.trim($("#planCode").val()),
                planName : $.trim($("#planName").val()),
                responsibleUserName : $("#responsibleUserName").val(),
                projectName : $("#projectName").val(),
                systemId : systemId,
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
            field : "PLAN_CODE",
            title : "计划编号",
            align : 'center'
        },{
            field : "PLAN_NAME",
            title : "计划名称",
            align : 'center'
        },{
            field : "projectName",
            title : "所属项目",
            align : 'center'
        },{
            field : "responsibleUserName",
            title : "责任人",
            align : 'center'
        }],
        onLoadSuccess:function(){
					$('#loading').hide();
        },
        onLoadError : function() {
					$('#loading').hide();
        }
    });
}

function commitPlan(){
	var selectContent = $("#planTable2").bootstrapTable('getSelections')[0];
	if(typeof(selectContent) == 'undefined') {
		layer.alert('请选择一列数据！', {icon: 0});
		return false;
	}else{
		if($("#planbutton").attr("data-ProjectPlan") == "new"){
			$("#newProjectPlanId").val(selectContent.id);
			$("#newProjectPlanName").val(selectContent.PLAN_NAME).change();
		}
        if($("#planbutton").attr("data-ProjectPlan") == "edit"){
            $("#editProjectPlanId").val(selectContent.id);
            $("#editProjectPlanName").val(selectContent.PLAN_NAME).change();
		}
	}

    $("#selProjectPlan").modal("hide");
}

//重置
function clearSearchPlan() {
    $('#planCode').val("");
    $('#planName').val("");
    $('#responsibleUserName').val("");
    $('#projectName').val("");
    $(".color1 .btn_clear").css("display","none");
}
//===========================项目计划弹窗end=================================


//获取当前登录用户所在项目（结项的项目除外） ztt
function getProject(){
	$('#loading').show();
	$.ajax({
		url:"/devManage/structure/getAllproject",
		dataType:"json",
		type:"post",
		async:false,
		success:function(data){
			if(data.list!=undefined && data.list.length>0){
				for(var i=0;i <data.list.length;i++){
					$("#project").append("<option selected value='"+data.list[i].id+"'>"+data.list[i].projectName+"</option>")
				}
			}
			$('.selectpicker').selectpicker('refresh');
			if(data.status == '2'){
				layer.alert("查询项目失败！！！", { icon: 2});
			}
			$('#loading').hide();
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
		}
	})
}



//显示/隐藏列
function addCheckBox() {
	$("#colGroup").empty();
	var str = "";
	str = '<div class="onesCol"><input type="checkbox" value="featureCode" onclick="showHideCol( this )" /><span>任务编号</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="featureName" onclick="showHideCol( this )" /><span>任务名称</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="requirementFeatureStatus" onclick="showHideCol( this )" /><span>状态</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="systemName" onclick="showHideCol( this )" /><span>子系统</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="requirementCode" onclick="showHideCol( this )" /><span>关联需求</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="manageUserName" onclick="showHideCol( this )" /><span>开发管理岗</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="executeUserName" onclick="showHideCol( this )" /><span>执行人</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="windowName" onclick="showHideCol( this )" /><span>投产窗口</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="sprintName" onclick="showHideCol( this )" /><span>冲刺</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="requirementFeaturePriority" onclick="showHideCol( this )" /><span>优先级</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="deployStatus" onclick="showHideCol( this )" /><span>部署状态</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="opt" onclick="showHideCol( this )" /><span>操作</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="estimateRemainWorkload" onclick="showHideCol( this )" /><span>剩余工作量</span></div>';
		
	$("#colGroup").append(str)
}

function showHideCol(This) {
	var colModel = $("#list2").jqGrid('getGridParam', 'colModel');
	var width = 0;// 获取当前列的列宽
	var arr = [];
//	collection_obj.search = '';
//	$("#colGroup").find('input').each(function(idx,val){
//		if ($(val).is(':checked')) {
//			collection_obj.search += $(val).val() + ',';
//		}
//	})
	if(!colModel) return
	for (var i = 0; i < colModel.length; i++) {
		if (colModel[i]["hidden"] == false) {
			if (colModel[i]["name"] != "cb") {
				arr.push(colModel[i]["hidden"]);
			}
		}
	}
	if ($(This).is(':checked')) {
		$("#list2").setGridParam().hideCol($(This).attr('value'));
		$("#list2").setGridWidth($('.wode').width());
		if (arr.length == 1) {
			$("#list2").jqGrid('setGridState', 'hidden');
		}
	} else {
		$("#list2").jqGrid('setGridState', 'visible');
		$("#list2").setGridParam().showCol($(This).attr('value'));
		$("#list2").setGridWidth($('.wode').width());
	}
}


function default_list(){
	var data = [];
	jQuery("#list3").jqGrid({
        data:data,
        datatype: 'local', 
        mtype:"post",
        height: 'auto',
        multiselect : true, 
        width: $(".content-table").width()*0.999,
        colNames:['id','systemCodeReviewStatus','systemId','developmentMode','任务编号', '任务名称','状态','子系统','关联需求'/*,'所属处室'*/,'管理岗','执行人','投产窗口','冲刺','优先级','部署状态','预估剩余工作量(人天)','操作','投产时间'],
        colModel:[ 
            {name:'id',width:30,index:'id',hidden:true},
            {name:'systemCodeReviewStatus',index:'systemCodeReviewStatus',hidden:true},
            {name:'systemId',index:'systemId',hidden:true},
            {name:'developmentMode',index:'developmentMode',hidden:true},
            {name:'featureCode',index:'featureCode',width:110,},
            {name:'featureName',index:'featureName',width: 300},
            {name:'requirementFeatureStatus',index:'requirementFeatureStatus'},
            {name:'systemName',index:'systemName',/*searchoptions:{sopt:['cn']}*/}, 
            {name:'requirementCode',index:'requirementCode',/*searchoptions:{sopt:['cn']}*/},
//            {name:'deptName',index:'deptName',/*searchoptions:{sopt:['cn']}*/},
            {name:'manageUserName',width:110,index:'manageUserName',/*searchoptions:{sopt:['cn']}*/},
            {name:'executeUserName',width:110,index:'executeUserName',/*searchoptions:{sopt:['cn']}*/},
            {name:'windowName',width:130,index:'windowName',},
            {name:'sprintName',index:'sprintName'},
            {name:"requirementFeaturePriority",width:80,index:'requirementFeaturePriority'
            },
			{name:'deployStatus',index:'部署状态',
            },
            {name:'estimateRemainWorkload',width: 80,index:'estimateRemainWorkload'},
            {
                name:'opt',
                index:'操作',
                width: 220,
                fixed:true,
                align:"center",
                sortable:false,
                resize:false,
            },
            {name:'windowDate',index:'windowDate',hidden:true}
        ],
        rowNum:10,
        rowTotal: 200,
        rowList : [5,10,20,30], 
        rownumWidth: 40,
        pager: '#pager3',
        sortable:true,   //是否可排序
        sortorder: 'desc',
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true, //是否要显示总记录数
        beforeRequest:function(){
        	$("#loading").css('display','block');
        },
        gridComplete: function(){
			$("[data-toggle='tooltip']").tooltip();
        },
        loadComplete :function(){
        	$("#loading").css('display','none');
        },
        loadError:function(){
            $("#loading").css('display','none');
        }
    }).trigger("reloadGrid");
}


//模糊搜索配置 
function search_settings(){
	var search_arr = [
		{
			ele : 'newdevManageUserName',
			userId : 'newdevManageUser',
		},
		{
			ele : 'newexecuteUserName',
			userId : 'newexecuteUser'
		},
		{
			ele : 'updateexecuteUserName',
			userId : 'updateexecuteUserId'
		},
		{
			ele : 'editdevManageUserName',
			userId : 'editdevManageUser'
		},
		{
			ele : 'editexecuteUserName',
			userId : 'editexecuteUser'
		},
		{
			ele : 'transferUserName',
			userId : 'transferId'
		}
	]
	search_arr.map(function(val){
		fuzzy_search_radio2({
			ele : val.ele, 
			url : '/system/user/getUserByNameOrACC', 
			top : '28px', 
			name : 'userName', 
			account : 'userAccount', 
			id : 'id', 
			userId : $("#"+val.userId), 
			rows : 'userInfo', 
		});
	})
}

//模糊搜索配置 
var searchUserNameIds = [];
function searchUserName(obj) {
	var id = $(obj).attr("id");
	if (searchUserNameIds.indexOf(id)=="-1") {
		searchUserNameIds.push(id);
		fuzzy_search_radio2({
			ele : id,
			url : '/system/user/getUserByNameOrACC',
			top : '28px',
			name : 'userName',
			account : 'userAccount',
			id : 'id',
			userId : $(obj).parent().find(".sWorkDivid"),
			rows : 'userInfo',
		});
	}
}

 

