/**
 * Created by ztt
 */
var reqStatus = '';
var selectReqIds = [];
var selectUserIds = [];
var selectSysIds = [];
var selectWinIds = [];
var windowTypeList = [];
var numbers = [];
var plan_setting = {
	data: {
		key: {
			name:"planName",
		},
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId",
		}
	},
	callback: {
		onClick: plan_onClick
	}
};


//模糊搜索配置
function search_settings(){
	var search_arr = [
		{
			ele : 'newtestManageUserName',
			userId : 'newtestManageUser',
		},
		{
			ele : 'newexecuteUserName',
			userId : 'newexecuteUser',
		},
		{
			ele : 'edittestManageUserName',
			userId : 'edittestManageUser',
		},
		{
			ele : 'editexecuteUserName',
			userId : 'editexecuteUser',
		},
		{
			ele : 'transferUserName',
			userId : 'transferId',
		},
		{
			ele : 'sWorkDividUserName',
			userId : 'sWorkDivid',
		},
		{
			ele : 'sWorkAssignUserName',
			userId : 'sWorkAssignUserId',
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

$(function () {
	search_settings();
	windowTypeList = $("#win_windowType").find("option");
	//getProject();
	select_project();
	dateComponent();
	$(document).on('click',function (e) {
		if(!$(e.target).parents().hasClass('new_plan_class') && !$(e.target).parents().hasClass('treeSearch')){
			$('#new_plan_body').hide();
			$('#edit_plan_body').hide();
		}
	})
	$("#requirementName").click(function () {
		$("#reqbutton").attr("data-req", "list");
		$("#reqStatus").empty();
		selectReqIds = [];
		$("#selReq").modal("show");
		reqStatus = '';
		clearSearchReq();
		getReqStatus();
		reqTableShowAll();
	});
	$("#newrequirementName").click(function () {
		$("#reqbutton").attr("data-req", "new");
		$("#reqStatus").empty();
		$("#selReq").modal("show");
		reqStatus = 'cancel';
		clearSearchReq();
		getReqStatus();
		reqTableShow();
	});
	$("#editrequirementName").click(function () {
		$("#reqbutton").attr("data-req", "edit");
		$("#reqStatus").empty();
		$("#selReq").modal("show");
		reqStatus = 'cancel';
		clearSearchReq();
		getReqStatus();
		reqTableShow();
	});

	$("#systemName").click(function () {
		$("#sysbutton").attr("data-sys", "list");
		selectSysIds = [];
		$("#selSys").modal("show");
		clearSearchSys();
		systemTableShowAll();

	});

	$("#newsystemName").click(function () {
		$("#sysbutton").attr("data-sys", "new");

		clearSearchSys();
		if (!$("#newProjectId").val()) {
			layer.alert('请先选择项目!', { icon: 0 })
			return;
		}
		systemTableShowAll();
		$("#selSys").modal("show");
	});
	$("#editsystemName").click(function () {
		$("#sysbutton").attr("data-sys", "edit");

		clearSearchSys();
		if (!$("#editProjectId").val()) {
			layer.alert('请先选择项目!', { icon: 0 })
			return;
		}
		systemTableShowAll();
		$("#selSys").modal("show");
	});
	$("#devManPostName").click(function () {
		$("#userbutton").attr("data-user", "list");
		$("#deptName").empty();
		$("#companyName").empty();
		selectUserIds = [];
		$("#userModal").modal("show");
		clearSearchUser();

		userTableShow();
	});
	$("#execteUserName").click(function () {
		$("#userbutton").attr("data-user", "list2");
		$("#deptName").empty();
		$("#companyName").empty();
		selectUserIds = [];
		$("#userModal").modal("show");
		clearSearchUser();

		userTableShow();
	});
	/*
	$("#newtestManageUserName").click(function () {
		systemId = $("#newsystemId").val();
		excuteUserName = '';
		$("#userbutton").attr("data-user", "newMan");
		$("#deptName").empty();
		$("#companyName").empty();
		$("#userModal").modal("show");
		clearSearchUser();

		userTableShow();
	});
	$("#edittestManageUserName").click(function () {
		systemId = $("#editsystemId").val();
		excuteUserName = '';
		$("#userbutton").attr("data-user", "editMan");
		$("#deptName").empty();
		$("#companyName").empty();
		$("#userModal").modal("show");
		clearSearchUser();

		userTableShow();
	});

	$("#newexecuteUserName").click(function () {
		systemId = $("#newsystemId").val();
		excuteUserName = '';
		$("#userbutton").attr("data-user", "new");
		$("#deptName").empty();
		$("#companyName").empty();
		$("#userModal").modal("show");
		clearSearchUser();

		userTableShow();
	});

	$("#editexecuteUserName").click(function () {
		systemId = $("#editsystemId").val();
		excuteUserName = '';
		$("#userbutton").attr("data-user", "edit");
		$("#deptName").empty();
		$("#companyName").empty();
		$("#userModal").modal("show");
		clearSearchUser();

		userTableShow();
	});
	$("#transferUserName").click(function () {
		$("#userbutton").attr("data-user", "transfer");
		$("#deptName").empty();
		$("#companyName").empty();
		$("#userModal").modal("show");
		//systemId = $("#transferSystemId").val();
		clearSearchUser();

		userTableShow();
	});

	$("#sWorkDividUserName").click(function () {
		$("#userbutton").attr("data-user", "split");
		$("#deptName").empty();
		$("#companyName").empty();
		$("#userModal").modal("show");
		//systemId = $("#transferSystemId").val();
		excuteUserName = '';
		clearSearchUser();

		userTableShow();
	});
	$("#sWorkAssignUserName").click(function () {
		$("#userbutton").attr("data-user", "split2");
		$("#deptName").empty();
		$("#companyName").empty();
		$("#userModal").modal("show");
		excuteUserName = '';
		clearSearchUser();

		userTableShow();
	});
	
	$("#planVersionName").click(function () {
		$("#windowButton").attr("data-window", "list");
		winReset();
		selectWinIds = [];
		$("#comWindowModal").modal("show");
		commitWindowAll();
	});
	 */
	$("#newcommitWindowName").click(function () {
		$("#windowButton").attr("data-window", "new");
		winReset();
		selectWinIds = [];
		$("#comWindowModal").modal("show");
		commitWindow();
	});

	$("#editcommitWindowName").click(function () {
		$("#windowButton").attr("data-window", "edit");
		winReset();
		selectWinIds = [];
		$("#comWindowModal").modal("show");
		commitWindow();
	});

	$("#commitWindowSearch").click(function () {
		if ($("#windowButton").attr("data-window") == "list") {
			commitWindowAll();
		} else {
			commitWindow();
		}
	});

	$("#systemSearch").click(function () {
		if ($("#sysbutton").attr("data-sys") == "list") {
			systemTableShowAll();
		} else {
			//systemTableShow();
			systemTableShowAll();
		}
	});
	$("#reqSearch").click(function () {
		if ($("#reqbutton").attr("data-req") == "list") {
			reqTableShowAll();
		} else {
			reqTableShow();
		}
	});
	/*$("#userSearch").click(function(){
		if($("#userbutton").attr("data-user") == "list" || $("#userbutton").attr("data-user") == "list2"){
			userTableShowAll();
		}else{
			userTableShow();
		}
	})
	*/
	_opt_html = $("#developmentDept").html();

})
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
//========================投产窗口start=====================
function commitWindow() {
	var startDate = '';
	var endDate = '';
	if ($("#timeDate").val() != "") {
		var arr = $("#timeDate").val().split(" - ");
		startDate = arr[0];
		endDate = arr[1];
	}
	$("#comWindowTable").bootstrapTable('destroy');
	$("#comWindowTable").bootstrapTable({
		url: "/projectManage/commissioningWindow/selectCommissioningWindows2",
		method: "post",
		queryParamsType: "",
		pagination: true,
		sidePagination: "server",
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber: 1,
		pageSize: 10,
		pageList: [5, 10, 15],
		singleSelect: true,//单选
		queryParams: function (params) {
			var param = {
				"windowName": $.trim($("#win_windowName").val()),
				"startDate": startDate,
				"endDate": endDate,
				"windowType": $("#win_windowType").val(),
				pageNumber: params.pageNumber,
				pageSize: params.pageSize,
			}
			return param;
		},
		columns: [{
			checkbox: true,
			width: "30px",
		}, {
			field: "id",
			title: "id",
			visible: false,
			align: 'center'
		}, {
			field: "windowName",
			title: "窗口名称",
			align: 'center'
		}, {
			field: "windowDate",
			title: "投产日期",
			align: 'center'
		}, {
			field: "windowType",
			title: "窗口类型",
			align: 'center',
			formatter: function (value, row, index) {
				for (var i = 0; i < windowTypeList.length; i++) {
					if (windowTypeList[i].value == row.windowType) {
						return windowTypeList[i].innerHTML;
					}
				}

			}
		}],
		onLoadSuccess: function () {

		},
		onLoadError: function () {

		}
	});
}
function commitWindowAll() {
	var startDate = '';
	var endDate = '';
	if ($("#timeDate").val() != "") {
		var arr = $("#timeDate").val().split(" - ");
		startDate = arr[0];
		endDate = arr[1];
	}
	$("#comWindowTable").bootstrapTable('destroy');
	$("#comWindowTable").bootstrapTable({
		url: "/projectManage/commissioningWindow/selectCommissioningWindows2",
		method: "post",
		queryParamsType: "",
		pagination: true,
		sidePagination: "server",
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber: 1,
		pageSize: 10,
		pageList: [5, 10, 15],
		// singleSelect : true,//单选
		queryParams: function (params) {
			var param = {
				"windowName": $.trim($("#win_windowName").val()),
				"startDate": startDate,
				"endDate": endDate,
				"windowType": $("#win_windowType").val(),
				pageNumber: params.pageNumber,
				pageSize: params.pageSize,
			}
			return param;
		},
		columns: [{
			checkbox: true,
			width: "30px",
		}, {
			field: "id",
			title: "id",
			visible: false,
			align: 'center'
		}, {
			field: "windowName",
			title: "窗口名称",
			align: 'center'
		}, {
			field: "windowDate",
			title: "投产日期",
			align: 'center'
		}, {
			field: "windowType",
			title: "窗口类型",
			align: 'center',
			formatter: function (value, row, index) {
				for (var i = 0; i < windowTypeList.length; i++) {
					if (windowTypeList[i].value == value) {
						return windowTypeList[i].innerHTML;
					}
				}

			}
		}],
		onLoadSuccess: function () {

		},
		onLoadError: function () {

		},
		onCheckAll: function (rows) {//全选
			for (var i = 0; i < rows.length; i++) {
				//if(selectWinIds .indexOf(rows[i])<=-1){
				selectWinIds.push(rows[i]);
				//}
			}
		},
		onUncheckAll: function (rows) {
			for (var i = 0; i < rows.length; i++) {
				if (selectWinIds.indexOf(rows[i]) > -1) {
					selectWinIds.splice(selectWinIds.indexOf(rows[i]), 1);
				}
			}
		},
		onCheck: function (row) {//选中复选框
			//if(selectWinIds.indexOf(row)<=-1){
			selectWinIds.push(row);
			//}
		},
		onUncheck: function (row) {//取消复选框
			if (selectWinIds.indexOf(row) > -1) {
				selectWinIds.splice(selectWinIds.indexOf(row), 1);
			}
		}
	});
}
function commitWin() {
	if ($("#windowButton").attr("data-window") == 'list') {
		if (selectWinIds.length <= 0) {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		} else {
			var ids = '';
			var names = '';
			for (var i = 0; i < selectWinIds.length; i++) {
				ids += selectWinIds[i].id + ",";
				names += selectWinIds[i].windowName + ',';
			}
			if ($("#windowButton").attr("data-window") == 'list') {
				$("#planVersion").val(ids);
				$("#planVersionName").val(names).change();
			}
		}
	} else {
		var selectContent = $("#comWindowTable").bootstrapTable('getSelections')[0];
		if (typeof (selectContent) == 'undefined') {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		} else {
			if ($("#windowButton").attr("data-window") == 'new') {
				$("#newcommitWindowId").val(selectContent.id);
				$("#newcommitWindowName").val(selectContent.windowName).change();
			}
			if ($("#windowButton").attr("data-window") == 'edit') {
				$("#editcommitWindowId").val(selectContent.id);
				$("#editcommitWindowName").val(selectContent.windowName).change();
			}

		}
	}

	$("#comWindowModal").modal("hide");

}
function winReset() {
	$("#win_windowName").val("");
	$("#timeDate").val("");
	$("#win_windowType").selectpicker('val', '');

}
//=========================需求弹框start=====================
function reqTableShow() {
	$("#listReq").bootstrapTable('destroy');
	$("#listReq").bootstrapTable({
		url: '/testManage/testtask/getAllReq',
		method: "post",
		queryParamsType: "",
		pagination: true,
		sidePagination: "server",
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber: 1,
		pageSize: 10,
		pageList: [5, 10, 15],
		singleSelect: true,//单选
		queryParams: function (params) {
			var param = {
				requirementCode: $.trim($("#reqCode").val()),
				requirementName: $.trim($("#reqName").val()),
				requirementStatus: $("#reqStatus").val(),
				pageNumber: params.pageNumber,
				pageSize: params.pageSize,
			}
			return param;
		},
		columns: [{
			checkbox: true,
			width: "30px",
			formatter: function (value, row, index) {
				if (reqStatus == 'cancel') {
					if (row.reqStatusName == "已取消") {
						return {
							disabled: true,//设置是否可用
							checked: false//设置选中
						};
						return value;
					}
				}

			}
		}, {
			field: "id",
			title: "id",
			visible: false,
			align: 'center'
		}, {
			field: "REQUIREMENT_CODE",
			title: "需求编号",
			align: 'center'
		}, {
			field: "REQUIREMENT_NAME",
			title: "需求名称",
			align: 'center'
		}, {
			field: "reqStatusName",
			title: "需求状态",
			align: 'center'
		}, {
			field: "reqSourceName",
			title: "需求来源",
			align: 'center'
		}, {

			field: "reqTypeName",
			title: "需求类型",
			align: 'center'
		}],
		onLoadSuccess: function () {

		},
		onLoadError: function () {

		}
	});
}
function reqTableShowAll() {
	$("#listReq").bootstrapTable('destroy');
	$("#listReq").bootstrapTable({
		url: "/testManage/testtask/getAllReq",
		method: "post",
		queryParamsType: "",
		pagination: true,
		sidePagination: "server",
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber: 1,
		pageSize: 10,
		pageList: [5, 10, 15],
		// singleSelect : true,//单选
		queryParams: function (params) {
			var param = {
				requirementCode: $.trim($("#reqCode").val()),
				requirementName: $.trim($("#reqName").val()),
				requirementStatus: $("#reqStatus").val(),
				pageNumber: params.pageNumber,
				pageSize: params.pageSize,
			}
			return param;
		},
		columns: [{
			checkbox: true,
			width: "30px",
			formatter: function (value, row, index) {
				if (reqStatus == 'cancel') {
					if (row.reqStatusName == "已取消") {
						return {
							disabled: true,//设置是否可用
							checked: false//设置选中
						};
						return value;
					}
				}

			}
		}, {
			field: "id",
			title: "id",
			visible: false,
			align: 'center'
		}, {
			field: "REQUIREMENT_CODE",
			title: "需求编号",
			align: 'center'
		}, {
			field: "REQUIREMENT_NAME",
			title: "需求名称",
			align: 'center'
		}, {
			field: "reqStatusName",
			title: "需求状态",
			align: 'center'
		}, {
			field: "reqSourceName",
			title: "需求来源",
			align: 'center'
		}, {

			field: "reqTypeName",
			title: "需求类型",
			align: 'center'
		}],
		onLoadSuccess: function () {

		},
		onLoadError: function () {

		},
		onCheckAll: function (rows) {//全选
			for (var i = 0; i < rows.length; i++) {
				//if(selectReqIds .indexOf(rows[i])<=-1){
				selectReqIds.push(rows[i]);
				//}
			}
		},
		onUncheckAll: function (rows) {
			for (var i = 0; i < rows.length; i++) {
				if (selectReqIds.indexOf(rows[i]) > -1) {
					selectReqIds.splice(selectReqIds.indexOf(rows[i]), 1);
				}
			}
		},
		onCheck: function (row) {//选中复选框
			//if(selectReqIds.indexOf(row)<=-1){
			selectReqIds.push(row);
			//}
		},
		onUncheck: function (row) {//取消复选框
			if (selectReqIds.indexOf(row) > -1) {
				selectReqIds.splice(selectReqIds.indexOf(row), 1);
			}
		}
	});
}
function getReqStatus() {
	$.ajax({
		type: "POST",
		url: "/testManage/testtask/getDataDicList",
		dataType: "json",
		data: {
			datadictype: 'reqStatus'
		},
		success: function (data) {
			$("#reqStatus").empty();
			$("#reqStatus").append("<option value=''>请选择</option>")
			for (var i = 0; i < data.length; i++) {
				var opt = "<option value='" + data[i].valueCode + "'>" + data[i].valueName + "</option>";
				$("#reqStatus").append(opt);
			}
			$('.selectpicker').selectpicker('refresh');
		}
	});
}

function insertInput() {
	if ($("#reqbutton").attr("data-req") == "list") {
		if (selectReqIds.length <= 0) {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		} else {
			var ids = '';
			var codes = '';
			for (var i = 0; i < selectReqIds.length; i++) {
				ids += selectReqIds[i].id + ",";
				codes += selectReqIds[i].REQUIREMENT_CODE + ',';
			}
			$("#requirementId").val(ids);
			$("#requirementName").val(codes).change();
		}

	} else {
		var selectContent = $("#listReq").bootstrapTable('getSelections')[0];
		if (typeof (selectContent) == 'undefined') {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		} else {
			if ($("#reqbutton").attr("data-req") == "new") {
				$("#newrequirementId").val(selectContent.id);
				$("#newrequirementName").val(selectContent.REQUIREMENT_CODE).change();
				findDeptNumber(selectContent.id, 1);
			}
			if ($("#reqbutton").attr("data-req") == "edit") {
				$("#editrequirementId").val(selectContent.id);
				$("#editrequirementName").val(selectContent.REQUIREMENT_CODE).change();
				findDeptNumber(selectContent.id, 2);
			}
		}
	}

	$("#selReq").modal("hide");

}
var _opt_html = '';
function findDeptNumber(id, type) {

	$("#developmentDept option").each(function () {
		numbers.push($(this).val());
	})
	$.ajax({
		url: "/testManage/testtask/findDeptNumber",
		dataType: "json",
		async: false,
		type: "post",
		data: {
			id: id
		},
		success: function (data) {
			if (type == 1) {
				$("#developmentDept").empty();
				$("#developmentDept").append(_opt_html);
				if (data.data != null) {
					if (!numbers.includes(data.data.deptNumber)) {
						$("#developmentDept").append("<option selected value='" + data.data.deptNumber + "'>" + data.data.deptName + "</option>");
						$("#developmentDept").selectpicker('refresh');
					} else {
						$("#developmentDept").selectpicker('val', data.data.deptNumber);
						$("#developmentDept").selectpicker('refresh');
					}
				}
				//				if(data.data != null){		
				//					$("#developmentDeptNumber2").val(data.data.deptNumber);
				//					$("#developmentDept2").val(data.data.deptName);
				//				}else{
				//					$("#developmentDeptNumber2").val('');
				//					$("#developmentDept2").val('');
				//				}
				//				$("#development2").css("display","block");
				//				$("#development").css("display","none");
			} else if (type == 2) {
				$("#editDevelopmentDept").empty();
				console.log(111)
				$("#editDevelopmentDept").append(_opt_html);
				if (data.data != null) {
					if (!numbers.includes(data.data.deptNumber)) {
						$("#editDevelopmentDept").append("<option selected value='" + data.data.deptNumber + "'>" + data.data.deptName + "</option>");
						$("#editDevelopmentDept").selectpicker('refresh');
					} else {
						$("#editDevelopmentDept").selectpicker('val', data.data.deptNumber);
						$("#editDevelopmentDept").selectpicker('refresh');
					}
				}
				/*if(data.data != null){
					$("#editDevelopmentDeptNumber2").val(data.data.deptNumber);
					$("#editDevelopmentDept2").val(data.data.deptName);
				}else{
					$("#editDevelopmentDeptNumber2").val('');
					$("#editDevelopmentDept2").val('');
				}
				$("#editDevelopment").css("display","none");
				$("#editDevelopment2").css("display","block");*/
			}
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})

}

//重置
function clearSearchReq() {
	$('#reqCode').val("");
	$('#reqName').val("");
	$("#reqStatus").selectpicker('val', '');
	$("#selReq .color1 .btn_clear").css("display", "none");
}
//===========================需求弹框end=================================

//====================涉及系统弹框start==========================
function systemTableShow() {
	$("#systemTable").bootstrapTable('destroy');
	$("#systemTable").bootstrapTable({
		url: "/testManage/testtask/selectAllSystemInfo",
		method: "post",
		queryParamsType: "",
		pagination: true,
		sidePagination: "server",
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber: 1,
		pageSize: 10,
		pageList: [5, 10, 15],
		singleSelect: true,//单选
		queryParams: function (params) {
			var param = {
				systemName: $.trim($("#SCsystemName").val()),
				systemCode: $.trim($("#SCsystemCode").val()),
				pageNumber: params.pageNumber,
				pageSize: params.pageSize,
			}
			return param;
		},
		columns: [{
			checkbox: true,
			width: "30px"
		}, {
			field: "id",
			title: "id",
			visible: false,
			align: 'center'
		}, {
			field: "systemCode",
			title: "子系统编码",
			align: 'center'
		}, {
			field: "systemName",
			title: "子系统名称",
			align: 'center'
		}, {
			field: "systemShortName",
			title: "子系统简称",
			align: 'center'
		}, {
			field: "projectName",
			title: "所属项目",
			align: 'center'
		}],
		onLoadSuccess: function () {

		},
		onLoadError: function () {

		}
	});
}


function systemTableShowAll() {
	var projectId = '';
	if ($("#sysbutton").attr("data-sys") == "new") {
		projectId = $("#newProjectId").val();
	} else if ($("#sysbutton").attr("data-sys") == "edit") {
		projectId = $("#editProjectId").val();
	}
	$("#systemTable").bootstrapTable('destroy');
	$("#systemTable").bootstrapTable({
		url: "/testManage/testtask/selectAllSystemInfo2",
		method: "post",
		queryParamsType: "",
		pagination: true,
		sidePagination: "server",
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber: 1,
		pageSize: 10,
		pageList: [5, 10, 15],
		singleSelect: true,//单选
		queryParams: function (params) {
			var param = {
				systemName: $.trim($("#SCsystemName").val()),
				systemCode: $.trim($("#SCsystemCode").val()),
				projectId: projectId,
				pageNumber: params.pageNumber,
				pageSize: params.pageSize,
			}
			return param;
		},
		columns: [{
			checkbox: true,
			width: "30px"
		}, {
			field: "id",
			title: "id",
			visible: false,
			align: 'center'
		}, {
			field: "systemCode",
			title: "子系统编码",
			align: 'center'
		}, {
			field: "systemName",
			title: "子系统名称",
			align: 'center'
		}, {
			field: "systemShortName",
			title: "子系统简称",
			align: 'center'
		}, {
			field: "projectName",
			title: "所属项目",
			align: 'center'
		}],
		onLoadSuccess: function () {

		},
		onLoadError: function () {

		},
		onCheckAll: function (rows) {//全选
			for (var i = 0; i < rows.length; i++) {
				//if(selectSysIds .indexOf(rows[i])<=-1){
				selectSysIds.push(rows[i]);
				//}
			}
		},
		onUncheckAll: function (rows) {
			for (var i = 0; i < rows.length; i++) {
				if (selectSysIds.indexOf(rows[i]) > -1) {
					selectSysIds.splice(selectSysIds.indexOf(rows[i]), 1);
				}
			}
		},
		onCheck: function (row) {//选中复选框
			//if(selectSysIds.indexOf(row)<=-1){
			selectSysIds.push(row);
			//}
		},
		onUncheck: function (row) {//取消复选框
			if (selectSysIds.indexOf(row) > -1) {
				selectSysIds.splice(selectSysIds.indexOf(row), 1);
			}
		}
	});
}

function commitSys() {
	$('#add_agility_inner').hide();
	$('#edit_agility_inner').hide();
	if ($("#sysbutton").attr("data-sys") == "list") {
		if (selectSysIds.length <= 0) {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		} else {
			var ids = '';
			var names = '';
			for (var i = 0; i < selectSysIds.length; i++) {
				ids += selectSysIds[i].id + ",";
				names += selectSysIds[i].systemName + ',';
			}
			$("#systemId").val(ids);
			$("#systemName").val(names).change();
		}
	} else {
		var selectContent = $("#systemTable").bootstrapTable('getSelections')[0];
		if (typeof (selectContent) == 'undefined') {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		} else {
			if ($("#sysbutton").attr("data-sys") == "new") {
				$("#newsystemId").val(selectContent.id);
				$("#newsystemName").val(selectContent.systemName).change();

				if (selectContent.developmentMode == 1) {
					$('#add_agility_inner').show();
				}
			}
			if ($("#sysbutton").attr("data-sys") == "edit") {
				$("#editsystemId").val(selectContent.id);
				$("#editsystemName").val(selectContent.systemName).change();

				if (selectContent.developmentMode == 1) {
					$('#edit_agility_inner').show();
				}
			}
		}
	}
	$("#selSys").modal("hide");

}
//重置
function clearSearchSys() {
	$("#SCsystemName").val('');
	$("#SCsystemCode").val('');
	$("#selSys .color1 .btn_clear").css("display", "none");
}
//====================涉及系统弹框end==========================

//====================人员弹框start=======================
function userTableShow() {
	getProject();
	var projectIds = $("#project").val();
	if (projectIds != null && projectIds != '') {
		projectIds = projectIds.join(",");
	}
	$("#loading").css('display', 'block');
	if ($("#userbutton").attr("data-user") == 'list' || $("#userbutton").attr("data-user") == 'list2') {
		$("#userTable").bootstrapTable('destroy');
		$("#userTable").bootstrapTable({
			url: "/system/user/getAllUserModal2",
			method: "post",
			queryParamsType: "",
			pagination: true,
			sidePagination: "server",
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			pageNumber: 1,
			pageSize: 10,
			pageList: [5, 10, 15],
			//   singleSelect : true,//单选
			queryParams: function (params) {
				var param = {
					// id:excuteUserName,
					//systemId:systemId,
					projectIds: projectIds,
					userName: $.trim($("#userNameReqF").val()),
					companyName: $("#companyName").val(),
					deptName: $("#deptName").val(),
					pageNumber: params.pageNumber,
					pageSize: params.pageSize,
				}
				return param;
			},
			columns: [{
				checkbox: true,
				width: "30px"
			}, {
				field: "id",
				title: "id",
				visible: false,
				align: 'center'
			}, {
				field: "userName",
				title: "姓名",
				align: 'center'
			}, {
				field: "userAccount",
				title: "用户名",
				align: 'center'
			}, {
				field: "companyName",
				title: "所属公司",
				align: 'center'
			}, {
				field: "deptName",
				title: "所属处室",
				align: 'center'
			}],
			onLoadSuccess: function () {
				$("#loading").css('display', 'none');
			},
			onLoadError: function () {

			},
			onCheckAll: function (rows) {//全选
				for (var i = 0; i < rows.length; i++) {
					//if(selectUserIds.indexOf(rows[i])<=-1){
					selectUserIds.push(rows[i]);
					//}
				}
			},
			onUncheckAll: function (rows) {
				for (var i = 0; i < rows.length; i++) {
					if (selectUserIds.indexOf(rows[i]) > -1) {
						selectUserIds.splice(selectUserIds.indexOf(rows[i]), 1);
					}
				}
			},
			onCheck: function (row) {//选中复选框
				//if(selectUserIds.indexOf(row)<=-1){
				selectUserIds.push(row);
				//}
			},
			onUncheck: function (row) {//取消复选框
				if (selectUserIds.indexOf(row) > -1) {
					selectUserIds.splice(selectUserIds.indexOf(row), 1);
				}
			}
		});

	} else {
		$("#userTable").bootstrapTable('destroy');
		$("#userTable").bootstrapTable({
			url: "/system/user/getAllUserModal2",
			method: "post",
			queryParamsType: "",
			pagination: true,
			sidePagination: "server",
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			pageNumber: 1,
			pageSize: 10,
			pageList: [5, 10, 15],
			singleSelect: true,//单选
			queryParams: function (params) {
				var param = {
					id: excuteUserName,
					projectIds: projectIds,
					// systemId:systemId, 
					userName: $.trim($("#userNameReqF").val()),
					companyName: $("#companyName").val(),
					deptName: $("#deptName").val(),
					pageNumber: params.pageNumber,
					pageSize: params.pageSize,
				}
				return param;
			},
			columns: [{
				checkbox: true,
				width: "30px"
			}, {
				field: "id",
				title: "id",
				visible: false,
				align: 'center'
			}, {
				field: "userName",
				title: "姓名",
				align: 'center'
			}, {
				field: "userAccount",
				title: "用户名",
				align: 'center'
			}, {
				field: "companyName",
				title: "所属公司",
				align: 'center'
			}, {
				field: "deptName",
				title: "所属处室",
				align: 'center'
			}],
			onLoadSuccess: function () {
				$("#loading").css('display', 'none');
			},
			onLoadError: function () {

			}
		});
	}
}
function userTableShowAll() {
	var projectIds = $("#project").val();
	if (projectIds != null && projectIds != '') {
		projectIds = projectIds.join(",");
	}
	$("#loading").css('display', 'block');
	if ($("#userbutton").attr("data-user") == 'list' || $("#userbutton").attr("data-user") == 'list2') {
		$("#userTable").bootstrapTable('destroy');
		$("#userTable").bootstrapTable({
			url: "/system/user/getAllUserModal2",
			method: "post",
			queryParamsType: "",
			pagination: true,
			sidePagination: "server",
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			pageNumber: 1,
			pageSize: 10,
			pageList: [5, 10, 15],
			//   singleSelect : true,//单选
			queryParams: function (params) {
				var param = {
					// id:excuteUserName,
					//systemId:systemId,
					projectIds: projectIds,
					userName: $.trim($("#userNameReqF").val()),
					companyName: $("#companyName").val(),
					deptName: $("#deptName").val(),
					pageNumber: params.pageNumber,
					pageSize: params.pageSize,
				}
				return param;
			},
			columns: [{
				checkbox: true,
				width: "30px"
			}, {
				field: "id",
				title: "id",
				visible: false,
				align: 'center'
			}, {
				field: "userName",
				title: "姓名",
				align: 'center'
			}, {
				field: "userAccount",
				title: "用户名",
				align: 'center'
			}, {
				field: "companyName",
				title: "所属公司",
				align: 'center'
			}, {
				field: "deptName",
				title: "所属处室",
				align: 'center'
			}],
			onLoadSuccess: function () {
				$("#loading").css('display', 'none');
			},
			onLoadError: function () {

			},
			onCheckAll: function (rows) {//全选
				for (var i = 0; i < rows.length; i++) {
					//if(selectUserIds.indexOf(rows[i])<=-1){
					selectUserIds.push(rows[i]);
					//}
				}
			},
			onUncheckAll: function (rows) {
				for (var i = 0; i < rows.length; i++) {
					if (selectUserIds.indexOf(rows[i]) > -1) {
						selectUserIds.splice(selectUserIds.indexOf(rows[i]), 1);
					}
				}
			},
			onCheck: function (row) {//选中复选框
				//if(selectUserIds.indexOf(row)<=-1){
				selectUserIds.push(row);
				//}
			},
			onUncheck: function (row) {//取消复选框
				if (selectUserIds.indexOf(row) > -1) {
					selectUserIds.splice(selectUserIds.indexOf(row), 1);
				}
			}


		});
	} else {
		$("#userTable").bootstrapTable('destroy');
		$("#userTable").bootstrapTable({
			url: "/system/user/getAllUserModal2",
			method: "post",
			queryParamsType: "",
			pagination: true,
			sidePagination: "server",
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			pageNumber: 1,
			pageSize: 10,
			pageList: [5, 10, 15],
			singleSelect: true,//单选
			queryParams: function (params) {
				var param = {
					id: excuteUserName,
					projectIds: projectIds,
					// systemId:systemId, 
					userName: $.trim($("#userNameReqF").val()),
					companyName: $("#companyName").val(),
					deptName: $("#deptName").val(),
					pageNumber: params.pageNumber,
					pageSize: params.pageSize,
				}
				return param;
			},
			columns: [{
				checkbox: true,
				width: "30px"
			}, {
				field: "id",
				title: "id",
				visible: false,
				align: 'center'
			}, {
				field: "userName",
				title: "姓名",
				align: 'center'
			}, {
				field: "userAccount",
				title: "用户名",
				align: 'center'
			}, {
				field: "companyName",
				title: "所属公司",
				align: 'center'
			}, {
				field: "deptName",
				title: "所属处室",
				align: 'center'
			}],
			onLoadSuccess: function () {
				$("#loading").css('display', 'none');
			},
			onLoadError: function () {

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
}

function getCompany() {
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


function commitUser() {
	if ($("#userbutton").attr("data-user") == 'list' || $("#userbutton").attr("data-user") == 'list2') {
		if (selectUserIds.length <= 0) {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		} else {
			var ids = '';
			var names = '';
			for (var i = 0; i < selectUserIds.length; i++) {
				ids += selectUserIds[i].id + ",";
				names += selectUserIds[i].userName + ',';
			}
			if ($("#userbutton").attr("data-user") == 'list') {
				$("#devManPost").val(ids);
				$("#devManPostName").val(names).change();
			}

			if ($("#userbutton").attr("data-user") == 'list2') {
				$("#execteUserId").val(ids);
				$("#execteUserName").val(names).change();
			}
		}
	} else {
		var selectContent = $("#userTable").bootstrapTable('getSelections')[0];
		if (typeof (selectContent) == 'undefined') {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		} else {
			if ($("#userbutton").attr("data-user") == 'new') {
				$("#newexecuteUser").val(selectContent.id);
				$("#newexecuteUserName").val(selectContent.userName).change();
			}
			if ($("#userbutton").attr("data-user") == 'edit') {
				$("#editexecuteUser").val(selectContent.id);
				$("#editexecuteUserName").val(selectContent.userName).change();
			}
			if ($("#userbutton").attr("data-user") == 'newMan') {
				$("#newtestManageUser").val(selectContent.id);
				$("#newtestManageUserName").val(selectContent.userName).change();
			}
			if ($("#userbutton").attr("data-user") == 'editMan') {
				$("#edittestManageUser").val(selectContent.id);
				$("#edittestManageUserName").val(selectContent.userName).change();
			}
			if ($("#userbutton").attr("data-user") == 'transfer') {
				$("#transferId").val(selectContent.id);
				$("#transferUserName").val(selectContent.userName).change();
			}
			if ($("#userbutton").attr("data-user") == "split") {
				$("#sWorkDivid").val(selectContent.id);
				$("#sWorkDividUserName").val(selectContent.userName).change();
			}
			if ($("#userbutton").attr("data-user") == "split2") {
				$("#sWorkAssignUserId").val(selectContent.id);
				$("#sWorkAssignUserName").val(selectContent.userName).change();
			}
		}
	}
	$("#userModal").modal("hide");

}
*/
function clearSearchUser() {
	$("#userNameReqF").val('');
	$("#employeeNumber").val('');
	$("#deptName").val('');
	$("#companyName").val('');
	$("#project").selectpicker('val', '');
	$("#userModal .color1 .btn_clear").css("display", "none");
}
//====================人员弹框end=======================

//获取部门 ztt
function getDept(deptId) {
	$("#newdeptId").empty();
	$("#editdeptId").empty();
	$("#newdeptId").append('<option value="">请选择</option>');
	$("#editdeptId").append('<option value="">请选择</option>');
	
	$.ajax({
		url: "/system/dept/getDeptByDeptName",
		dataType: "json",
		type: "post",
		async: false,
		success: function (data) {
			console.log(deptId);
			if (data.depts != undefined && data.depts.length > 0) {
				for (var i = 0; i < data.depts.length; i++) {
					var str="";
					if(deptId == data.depts[i].id){
						str = "selected";
					}
					$("#newdeptId").append("<option value='" + data.depts[i].id + "'>" + data.depts[i].deptName + "</option>")
					$("#editdeptId").append("<option value='" + data.depts[i].id + "' "+str+">" + data.depts[i].deptName + "</option>")
				}
			}
			/*$('.selectpicker').selectpicker('refresh');*/
			if (data.status == '2') {
				layer.alert("查询部门失败！！！", { icon: 2 });
			}
		},
		error: function () {
		}
	})
}
//获取当前登录用户所在项目（结项的项目除外） ztt
function getProject() {
	$("#project").empty();
	//var projectIds = '';
	$.ajax({
		url: "/devManage/displayboard/getAllProject",
		dataType: "json",
		type: "post",
		async: false,
		success: function (data) {
			if (data.projects != undefined && data.projects.length > 0) {
				for (var i = 0; i < data.projects.length; i++) {
					//projectIds += data.projects[i].id+",";
					$("#project").append("<option selected value='" + data.projects[i].id + "'>" + data.projects[i].projectName + "</option>")

				}
				//$('#project option').attr('selected', true);
			}

			$('.selectpicker').selectpicker('refresh');
			if (data.status == '2') {
				layer.alert("查询项目失败！！！", { icon: 2 });
			}
		},
		error: function () {
			//			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
		}
	})
	//return projectIds;

}


//===========================项目========================
//选择项目配置
function select_project() {
	fuzzy_search_radio({
		'ele': 'newProjectName',
		'url': '/testManage/testtask/getProjectListByProjectName',
		'params': {
			projectName: '',
		},
		'name': 'projectName',
		'code': 'projectCode',
		'id': 'id',
		'top': '26px',
		'dataName': 'projectInfoList',
		'userId': $('#newProjectId'),
		'out': true,
		'not_jqgrid': true,
		'otherDataName': 'systemInfoList',
	});
	fuzzy_search_radio({
		'ele': 'editProjectName',
		'url': '/testManage/testtask/getProjectListByProjectName',
		'params': {
			projectName: '',
		},
		'name': 'projectName',
		'code': 'projectCode',
		'id': 'id',
		'top': '26px',
		'dataName': 'projectInfoList',
		'userId': $('#editProjectId'),
		'out': true,
		'not_jqgrid': true,
		'otherDataName': 'systemInfoList',
	});
	$('#newProjectName_list').on('click', '.newProjectName_search_item', function () {
		var data = JSON.parse($(this).attr('data'));
		var val = data.projectName;
		var id = $(this).data('id');
		var systemId = data.systemId;
		var systemName = data.systemName;
		$('#newProjectId').val(id);
		$('#newProjectName').val(val).attr('username', val).change();
		$('#newProjectName_list').hide();
		
		if (data.projectType == 2) {    //新建类
			clear_new_option();
			$('.is_new_project').hide();
			$('.new_project_option').show();
		} else {
			$('.is_new_project').show();
			$('.new_project_option').hide();
		}
		if (systemId) {
			$('#newsystemId').val(systemId);
			$('#newsystemName').val(systemName).change();
		}else{
			get_project_system(1);
		}
		$("#new_project_plan_id").val('');
		$("#new_project_plan_name").val('');
		
	})
	$('#editProjectName_list').on('click', '.editProjectName_search_item', function () {
		var data = JSON.parse($(this).attr('data'));
		var val = data.projectName;
		var id = $(this).data('id');
		var systemId = data.systemId;
		var systemName = data.systemName;
		$('#editProjectId').val(id);
		$('#editProjectName').val(val).attr('username', val).change();
		$('#editProjectName_list').hide();
		
		if (data.projectType == 2) { //新建项目
			clear_new_option();
			$('.is_new_project').hide();
			$('.new_project_option').show();
		} else {
			$('.is_new_project').show();
			$('.new_project_option').hide();
		}
		if (systemId) {
			$('#editsystemId').val(systemId);
			$('#editsystemName').val(systemName).change();
		}else{
			get_project_system(2);
		}
		$("#edit_project_plan_id").val('');
		$("#edit_project_plan_name").val('');
	})
}

//获取项目下的系统
function get_project_system(status) {
	var projectId = '';
	if (status  == 1) {
		projectId = $("#newProjectId").val();
	} else if (status  == 2) {
		projectId = $("#editProjectId").val();
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/selectAllSystemInfo2",
		dataType: "json",
		type: "post",
		data: {
			systemName: '',
			systemCode: '',
			projectId: projectId,
			pageNumber: 1,
			pageSize: 99,
		},
		success: function (data) {
			if(data.rows.length){
				var systemIds = data.rows.map(function(v){
					return v.id;
				})
				if(status  == 2){
					if(!systemIds.includes(+$('#editsystemId').val())){
						$('#editsystemId').val('');
						$('#editsystemName').val('').change();
					}
				}else if(status  == 1){
					if(!systemIds.includes(+$('#newsystemId').val())){
						$('#newsystemId').val('');
						$('#newsystemName').val('').change();
					}
				}
			}else{
				$('#editsystemId').val('');
				$('#editsystemName').val('').change();
				$('#newsystemId').val('');
				$('#newsystemName').val('').change();
			}
			$("#loading").css('display', 'none');
		}
	})
}

//新建类项目清空
function clear_new_option() {
	$('#featureSource').val('');
	$('#newcommitWindowId').val('');
	$('#newcommitWindowName').val('');
	$('#SubmitTestTime').val('');
	$('#PptDeployTime').val('');
	$('#importantRequirementType').val('');

	$('#editFeature').val('');
	$('#editcommitWindowId').val('');
	$('#editcommitWindowName').val('');
	$('#editsubmitTestTime').val('');
	$('#editPptDeployTime').val('');
	$('#editimportantRequirementType').val('');
}

//表单验证
function oac_FormValidator() {
	$('#newform').bootstrapValidator({
		excluded: [":disabled"],
		message: 'This value is not valid',//通用的验证失败消息
		feedbackIcons: {//根据验证结果显示的各种图标
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			featureName: {
				validators: {
					notEmpty: {
						message: '名称不能为空'
					},
					stringLength: {
						min: 2,
						max: 100,
						message: '长度必须在2-100之间'
					}
				}
			},
			featureOverview: {
				validators: {
					notEmpty: {
						message: '描述不能为空'
					},
					stringLength: {
						min: 2,
						max: 500,
						message: '长度必须在2-500之间'
					}
				}
			},
			newProjectName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '项目不能为空'
					}
				}
			},
			newtestManageUserName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '管理岗不能为空'
					},
				}
			},
			newexecuteUserName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '执行人不能为空'
					},
				}
			},
			newsystemName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: "关联系统不能为空"
					},
				}
			},

			
			nestimateWorkload: {
				validators: {
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
						message: '请输入大于0且小于等于500的正数'
					}
				}
			},
			pptWorkload: {
				validators: {
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
						message: '请输入大于0且小于等于500的正数'
					}
				}
			}
		}
	});

	$("#editForm").bootstrapValidator({
		message: 'This value is not valid',//通用的验证失败消息
		feedbackIcons: {//根据验证结果显示的各种图标
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			efeatureName: {
				validators: {
					notEmpty: {
						message: '名称不能为空'
					},
					stringLength: {
						min: 2,
						max: 100,
						message: '长度必须在2-100之间'
					}
				}
			},
			editProjectName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '项目不能为空'
					}
				}
			},
			efeatureOverview: {
				validators: {
					notEmpty: {
						message: '描述不能为空'
					},
					stringLength: {
						min: 2,
						max: 500,
						message: '长度必须在2-500之间'
					}
				}
			},
			editimportantRequirementType: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: "监管需求不能为空"
					},
				}

			},
			editsystemName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: "关联系统不能为空"
					},
				}

			},
			edittestManageUserName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '管理岗不能为空'
					},
				}
			},
			editexecuteUserName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '执行人不能为空'
					},
				}
			},

			editcommitWindowName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '投产窗口不能为空'
					},
				}
			},
			editFeature: {
				validators: {
					notEmpty: {
						message: '任务类型不能为空'
					},
				}
			},
			enestimateWorkload: {
				validators: {
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
						message: '请输入大于0且小于等于500的正数'
					}
				}
			},
			epptWorkload: {
				validators: {
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
						message: '请输入大于0且小于等于500的正数'
					}
				}
			},
			editSitWorkload: {
				validators: {
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
						message: '请输入大于0小于等于500的正数'
					}
				}
			},
			editPptWorkload: {
				validators: {
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
						message: '请输入大于0小于等于500的正数'
					}
				}
			},
			estartWork: {
				trigger: "change",
				validators: {
					callback: {/*自定义，可以在这里与其他输入项联动校验*/
						message: '开始时间必须小于结束日期！',
						callback: function (value, validator, $field) {
							if (value == "") {
								return true;
							} else {
								if ($("#eendWork").val() == '') {
									return true;
								} else {
									var start = new Date(value);
									var end = new Date($("#eendWork").val());
									if (start.getTime() > end.getTime()) {
										return false;
									}
									return true;
								}
							}
						}
					}
				}
			},
			eendWork: {
				trigger: "change",
				validators: {
					callback: {/*自定义，可以在这里与其他输入项联动校验*/
						message: '结束时间必须大于开始日期！',
						callback: function (value, validator, $field) {
							if (value == "") {
								return true;
							} else {
								if ($("#estartWork").val() == '') {
									return true;
								} else {
									var start = new Date($("#estartWork").val());
									var end = new Date(value);
									if (start.getTime() > end.getTime()) {
										return false;
									}
									return true;
								}
							}
						}
					}
				}
			},
			epptstartWork: {
				trigger: "change",
				validators: {
					callback: {/*自定义，可以在这里与其他输入项联动校验*/
						message: '开始时间必须小于结束日期！',
						callback: function (value, validator, $field) {
							if (value == "") {
								return true;
							} else {
								if ($("#epptendWork").val() == '') {
									return true;
								} else {
									var start = new Date(value);
									var end = new Date($("#epptendWork").val());
									if (start.getTime() > end.getTime()) {
										return false;
									}
									return true;
								}
							}
						}
					}
				}
			},
			epptendWork: {
				trigger: "change",
				validators: {
					callback: {/*自定义，可以在这里与其他输入项联动校验*/
						message: '结束时间必须大于开始日期！',
						callback: function (value, validator, $field) {
							if (value == "") {
								return true;
							} else {
								if ($("#epptstartWork").val() == '') {
									return true;
								} else {
									var start = new Date($("#epptstartWork").val());
									var end = new Date(value);
									if (start.getTime() > end.getTime()) {
										return false;
									}
									return true;
								}
							}
						}
					}
				}
			}
		}
	});
}





//获取项目计划
function select_project_plan(status){
	var project_id = $('#newProjectId').val();
	if(status == 2){
		project_id = $('#editProjectId').val();
	}
	if(!project_id){
		layer.msg('请先选择项目!!!',{icon:0})
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/getProjectPlanTree",
		dataType: "json",
		type: "post",
		data: {
			projectId: project_id,
		},
		success: function (data) {
//			if(data.status == 1){
				if(data.list && data.list.length){
					if(status == 2){
						$.fn.zTree.init($("#edit_plan_tree"), plan_setting, data.list);
						fuzzySearch('edit_plan_tree', '#edit_plan_treeSearchInput', null, true); //初始化模糊搜索方法
						$("#edit_plan_body").show();
					}else{
						$.fn.zTree.init($("#new_plan_tree"), plan_setting, data.list);
						fuzzySearch('new_plan_tree','#new_plan_treeSearchInput', null, true); //初始化模糊搜索方法
						$("#new_plan_body").show();
					}
				}
//			}
			$("#loading").css('display', 'none');
		}
	})
}


function plan_onClick(event, treeId, treeNode, clickFlag){
	if(treeNode.oldname){
		$("#new_project_plan_id").val(treeNode.id);
		$("#new_project_plan_name").val(treeNode.oldname);
		$("#edit_project_plan_id").val(treeNode.id);
		$("#edit_project_plan_name").val(treeNode.oldname);
	}else{
		$("#new_project_plan_id").val(treeNode.id);
		$("#new_project_plan_name").val(treeNode.planName);
		$("#edit_project_plan_id").val(treeNode.id);
		$("#edit_project_plan_name").val(treeNode.planName);
	}
	$("#new_plan_body").hide();
	$("#edit_plan_body").hide();
}



