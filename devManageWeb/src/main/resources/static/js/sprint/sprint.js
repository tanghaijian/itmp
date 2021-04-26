var id = '';
var systemIdList = '';
var selectSystemIds = [];

$(function () {
	if ($('#system_id').val()) {
		$('#systemId').val($('#system_id').val());
		$('#systemName').val($('#system_Name').val());
	}
	pageInit();
	showSystemName();
	formValidator();
    refactorFormValidator();
	//搜索展开和收起
	$("#downBtn").click(function () {
		if ($(this).children("span").hasClass("fa-caret-up")) {
			$(this).children("span").removeClass("fa-caret-up");
			$(this).children("span").addClass("fa-sort-desc");
			$("#search_div").slideUp(200);
		} else {
			$(this).children("span").removeClass("fa-sort-desc");
			$(this).children("span").addClass("fa-caret-up");
			$("#search_div").slideDown(200);
		}
	});
	$("#new_sprintStartDate").datetimepicker({
		minView: "month",
		format: "yyyy-mm-dd",
		autoclose: true,
		todayBtn: true,
		language: 'zh-CN',
		pickerPosition: "bottom-left"
	});
	$("#new_sprintEndDate").datetimepicker({
		minView: "month",
		format: "yyyy-mm-dd",
		autoclose: true,
		todayBtn: true,
		language: 'zh-CN',
		pickerPosition: "bottom-left"
	});
	$("#edit_sprintStartDate").datetimepicker({
		minView: "month",
		format: "yyyy-mm-dd",
		autoclose: true,
		todayBtn: true,
		language: 'zh-CN',
		pickerPosition: "bottom-left"
	});
	$("#edit_sprintEndDate").datetimepicker({
		minView: "month",
		format: "yyyy-mm-dd",
		autoclose: true,
		todayBtn: true,
		language: 'zh-CN',
		pickerPosition: "bottom-left"
	});
	//所有的Input标签，在输入值后出现清空的按钮
	$('.color input[type=text]').parent().css("position", "relative");
	$('.color input[type=text]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
	$(".color input[type=text]").bind("input propertychange", function () {
		if ($(this).val() != "") {
			$(this).parent().children(".btn_clear").css("display", "block");
		} else {
			$(this).parent().children(".btn_clear").css("display", "none");
		}
	})
});

//表格数据加载
function pageInit() {
	$("#loading").css('display', 'block');
	$("#list2").jqGrid({
		url: '/devManage/sprintManage/getSprintInfos',
		datatype: 'json',
		mtype: "POST",
		height: 'auto',
		width: $(".content-table").width() * 0.999,
		colNames: ['id', '冲刺名称', '所属系统', '冲刺状态', '开始日期', '结束日期', '操作'],
		postData: {
			'systemIds': $('#system_id').val()
		},
		colModel: [
			{ name: 'id', index: 'sprintIdList', hidden: true },
			{
				name: 'sprintName', index: 'sprintName'/*,
            	searchoptions:{sopt:['cn']},
                formatter : function(value,grid, row, index) {
                    var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                    return '<a class="a_style" onclick="checkSprint('+rows+')">'+row.sprintName+'</a>'
                }*/},
			{ name: 'systemName', index: 'systemName' },
			{
				name: 'validStatus', index: 'validStatus',
				//冲刺状态转换为中文显示
				formatter: function (value, grid, row, index) {
					if (row.validStatus == 1) {
						return '正常';
					}
					if (row.validStatus == 2) {
						return '关闭';
					}
				}
			},
			{ name: 'sprintStartDate', index: 'sprintStartDate' },
			{ name: 'sprintEndDate', index: 'sprintEndDate' },
			{
				name: '操作', index: '操作', align: "center", fixed: true, sortable: false, resize: false, search: false, width: 200,
				formatter: function (value, grid, rows, state) {
					var row = JSON.stringify(rows).replace(/"/g, '&quot;');
					var span_ = "<span>&nbsp;|&nbsp;</span>";
					var opt_status = [];
					var a = '<a class="a_style" onclick="edit(' + row + ')">编辑</a>';
					if (sprintEdit == true) {
						opt_status.push(a);
					}
					var b = '<a class="a_style" onclick="delect(' + row + ')">删除</a>';
					if (sprintDelete == true) {
						opt_status.push(b);
					}
					if (rows.validStatus == 1 && sprintClose == true) {
						var c = '<a class="a_style" onclick="closeSprint(' + row + ')">关闭</a>';
						opt_status.push(c);
					}
					if (rows.validStatus == 2 && sprintOpen == true) {
						var d = '<a class="a_style" onclick="openSprint(' + row + ')">开启</a>';
						opt_status.push(d);
					}
					return opt_status.join(span_);
				}
			},
		],
		rowNum: 10,
		rowTotal: 200,
		rowList: [10, 20, 30],
		rownumWidth: 40,
		pager: '#pager2',
		sortname: 'id',
		loadtext: "数据加载中......",
		viewrecords: true, //是否要显示总记录数 
		jsonReader: {
			repeatitems: false,
			root: "data",
		},
		gridComplete: function () {
			$("[data-toggle='tooltip']").tooltip();
		},
		loadComplete: function () {
			$("#loading").css('display', 'none');
		}
	}).trigger("reloadGrid");
}

//条件搜索
function searchInfo() {
	$("#loading").css('display', 'block');
	$("#list2").jqGrid('setGridParam', {
		postData: {
			"sprintName": $.trim($("#sprintName").val()),
			"systemIds": $('#systemId').val(),
			"validStatus": $("#validStatus").val(),
		},
		page: 1,
		loadComplete: function () {
			$("#loading").css('display', 'none');
		}
	}).trigger("reloadGrid"); //重新载入
}

//重置条件
function clearSearch() {
	$('#sprintName').val("");
	$("#systemName").val('');
	$("#systemId").val('');
	$("#validStatus").selectpicker('val', '');
	searchInfo();
}

//===========================项目========================
//查询关联项目
function getProject_list(){
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
				sprintType:1
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
		if ($("#sysbutton").attr("data-sys") == "new") {
			$("#newProject_listId").val(selectContent.id);
			$("#newProject_list").val(selectContent.projectName).change();
			$("#new_systemId").val('');
      $("#new_systemName").val('').change();
		}
		if ($("#sysbutton").attr("data-sys") == "edit") {
			$("#editProject_listId").val(selectContent.id);
			$("#editProject_list").val(selectContent.projectName).change();
			$("#edit_systemId").val('');
      $("#edit_systemName").val('').change();
		}
		$("#project_modal").modal('hide');
	}
}

//删除
function delect(row) {
	var id = row.sprintIdList;
	console.log("deleteId:"+id);
	layer.alert('确认要删除此冲刺任务？', {
		icon: 0,
		btn: ['确定', '取消']
	}, function () {
		$.ajax({
			url: "/devManage/sprintManage/deleteSprintInfo",
			method: "post",
			data: {
				"sprintIdList": id
			},
			success: function (data) {
				if (data.status == 1) {
					layer.alert('删除成功!', { icon: 1 });
					pageInit();
				} if (data.status == 2) {
					layer.alert('删除失败!', { icon: 2 });
					pageInit();
				}
			},
		});
	})
}

//关闭任务
function closeSprint(row) {
	var id = row.sprintIdList;
	layer.alert('确认要关闭此冲刺任务？', {
		icon: 0,
		btn: ['确定', '取消']
	}, function () {
		$.ajax({
			url: "/devManage/sprintManage/closeSprint",
			method: "post",
			data: {
				"id": id
			},
			success: function (data) {
				if (data.status == 1) {
					layer.alert('关闭成功!', { icon: 1 });
					pageInit();
				} if (data.status == 2) {
					layer.alert('关闭失败!', { icon: 2 });
					pageInit();
				}
			},
		});
	})
}

//开启冲刺
function openSprint(row) {
    var id = row.sprintIdList;
    console.log("id："+id)
	layer.alert('确认要开启此冲刺任务？', {
		icon: 0,
		btn: ['确定', '取消']
	}, function () {
		$.ajax({
			url: "/devManage/sprintManage/openSprint",
			method: "post",
			data: {
				"id": id
			},
			success: function (data) {
				if (data.status == 1) {
					layer.alert('开启成功!', { icon: 1 });
					pageInit();
				} if (data.status == 2) {
					layer.alert('开启失败!', { icon: 2 });
					pageInit();
				}
			},
		});
	})
}


//新增
function newSprint_btn() {
	searchAdd();
	$("#newform").data('bootstrapValidator').destroy();
	if ($('#project_Id').val()) {
		$('#newProject_listId').val($('#project_Id').val());
		$('#newProject_list').val($('#z_project_Name').val());
		$('#new_systemId').val($('#system_id').val());
		$('#new_systemName').val($('#system_Name').val());
	}
	formValidator();
	$("#new_sprint").modal("show");
}

//新增数据清空
function searchAdd() {
	$("#new_sprintName").val("");
	$("#new_system").selectpicker('val', '');
	$("#new_sprintStartDate").val("");
	$("#new_sprintEndDate").val("");
	$("#new_systemName").val("");
	$('#new_systemId').val('');
	$('#newProjectPlanId').val('');
	$('#newProjectPlanName').val('');
	$("#sysbutton").attr("data-sys","new");
	if(!$("#project_Id").val()){
		$("#newProject_listId").val('');
		$("#newProject_list").val('');
	}
}

//新增提交
function addSprint() {
	$('#newform').data('bootstrapValidator').validate();
	if (!$('#newform').data("bootstrapValidator").isValid()) {
		return;
	}
	if ($('#new_sprintStartDate').val() == '') {
		layer.alert('冲刺任务开始日期不能为空!', {
			icon: 0,
		})
		return;
	}
	if ($('#new_sprintEndDate').val() == '') {
		layer.alert('冲刺任务结束日期不能为空!', {
			icon: 0,
		})
		return;
	}
	console.log("项目id:"+$("#project_Id").val());
	$.ajax({
		type: "post",
		url: "/devManage/sprintManage/addSprintInfo",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify({
			"sprintName": $("#new_sprintName").val(),
			"systemId": $('#new_systemId').val(),
			"sprintStartDate": $("#new_sprintStartDate").val(),
			"sprintEndDate": $("#new_sprintEndDate").val(),
			"projectPlanId": $("#newProjectPlanId").val(),
			"projectIds": $("#newProject_listId").val(),
			"systemList": $('#new_systemId').val()
		}),
		success: function (data) {
			layer.alert('新增冲刺任务成功!', {
				icon: 1,
			}, function (index) {
				$("#new_sprint").modal("hide");
				layer.close(index);
				searchInfo();
			})
		}
	});
}

//编辑
function edit(row) {
	$("#loading").css('display', 'block');
	$("#sysbutton").attr("data-sys","edit");
	id = row.sprintIdList;
  systemIdList = row.systemIdList;
	$.ajax({
		type: "post",
		url: "/devManage/sprintManage/getSprintInfoById",
		dataType: "json",
		data: {
			"sprintIdList": id
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			$("#edit_systemName").val();
			$("#edit_sprintName").val();
			$("#edit_systemId").val();
			$("#edit_sprintStartDate").val("");
			$("#edit_sprintEndDate").val('');
			$("#edit_sprintName").val(data.data.sprintName);
			$("#edit_systemId").val(data.data.systemId);
			$("#edit_systemName").val(data.data.systemName);
			/* $("#edit_system option").each( function (i, n) {
				    if (n.value == data.data.systemId) {
				        n.selected = true;
				    }
				});
				$(".selectpicker").selectpicker('refresh');*/
			$("#edit_sprintStartDate").val(data.data.sprintStartDate);
			$("#edit_sprintEndDate").val(data.data.sprintEndDate);
			$('#editProjectPlanId').val('');
			$('#editProjectPlanName').val('');
			$('#editProjectPlanId').val(data.data.projectPlanId);
			$('#editProjectPlanName').val(data.data.projectPlanName);

			$("#editProject_listId").val('');
			$("#editProject_list").val('');
			$("#editProject_listId").val(data.data.projectIds);
			$("#editProject_list").val(data.data.projectName);
            $("#newProject_listId").val('');
			$("#edit_sprint").modal("show");
		}
	});
}

//编辑提交
function editSprint() {
	$('#newform2').data('bootstrapValidator').validate();
	if (!$('#newform2').data("bootstrapValidator").isValid()) {
		return;
	}
	$.ajax({
		type: "post",
		url: "/devManage/sprintManage/updateSprintInfo",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify({
			'id': id,
			'sprintName': $('#edit_sprintName').val(),
			'systemId': $('#edit_systemId').val(),
			'sprintStartDate': $('#edit_sprintStartDate').val(),
			'sprintEndDate': $('#edit_sprintEndDate').val(),
			"projectPlanId": $("#editProjectPlanId").val(),
      "projectIds": $("#editProject_listId").val(),
			'sprintIdList':id,
			'systemIdList':systemIdList
		}),
		success: function (data) {
			layer.alert('编辑成功!', {
				icon: 1,
			})
			$("#edit_sprint").modal("hide");
			pageInit();
		}
	});
}

//详情
//function checkSprint(rows){
//	
//}

//获取系统名称并组装成下拉框option
function showSystemName() {
	$.ajax({
		url: "/devManage/sprintManage/getAllSystem",
		method: "post",
		dataType: "json",
		success: function (data) {
			var list = data.data;
			for (var i = 0; i < list.length; i++) {
				//先创建好select里面的option元素       
				var option = document.createElement("option");
				//转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				$(option).val(list[i].id);
				//给option的text赋值,这就是你点开下拉框能够看到的东西          
				$(option).text(list[i].systemName);
				//获取select 下拉框对象,并将option添加进select            
				$('#systemName').append(option);
			}
			for (var i = 0; i < list.length; i++) {
				//先创建好select里面的option元素       
				var option = document.createElement("option");
				//转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				$(option).val(list[i].id);
				//给option的text赋值,这就是你点开下拉框能够看到的东西          
				$(option).text(list[i].systemName);
				//获取select 下拉框对象,并将option添加进select            
				$('#new_system').append(option);
			}
			for (var i = 0; i < list.length; i++) {
				//先创建好select里面的option元素       
				var option = document.createElement("option");
				//转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				$(option).val(list[i].id);
				//给option的text赋值,这就是你点开下拉框能够看到的东西          
				$(option).text(list[i].systemName);
				//获取select 下拉框对象,并将option添加进select            
				$('#edit_system').append(option);
			}

			$('.selectpicker').selectpicker('refresh');
		}

	});
}



//表单验证
function formValidator() {
	$('#newform').bootstrapValidator({
		excluded: [":disabled"],
		message: '输入有误',//通用的验证失败消息
		feedbackIcons: {//根据验证结果显示的各种图标
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			new_sprintName: {
				validators: {
					notEmpty: {
						message: '冲刺任务名称不能为空'
					},
					stringLength: {
						max: 300,
						message: '冲刺任务名称长度必须小于500字符'
					}
				}
			},
			new_system: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '涉及系统不能为空'
					}
				}
			},
            newProject_list: {
                trigger: "change",
                validators: {
                    notEmpty: {
                        message: '关联名称不能为空'
                    }
                }
            }
		}
	});
	$('#newform2').bootstrapValidator({
		excluded: [":disabled"],
		message: '输入有误',//通用的验证失败消息
		feedbackIcons: {//根据验证结果显示的各种图标
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			edit_sprintName: {
				validators: {
					notEmpty: {
						message: '冲刺任务名称不能为空'
					},
					stringLength: {
						max: 300,
						message: '冲刺任务名称长度必须小于500字符'
					}
				}
			},
			edit_system: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '涉及系统不能为空'
					}
				}
			},
			editProject_list: {
				trigger: "change",
					validators: {
					notEmpty: {
						message: '关联名称不能为空'
					}
				}
			}
		}
	});
}

//====================涉及系统弹框  ztt==========================
$(function () {
	$("#systemName").click(function () {
		$("#sysbutton").attr("data-sys", "list");
		$("#selSys").modal("show");
		clearSearchSys();
		selectSystemIds = [];
		systemTableShowAll();
	});

	$("#new_systemName").click(function () {
		$("#sysbutton").attr("data-sys", "new");
		$("#selSys").modal("show");
		clearSearchSys();
		systemTableShow2();
	});
	$("#edit_systemName").click(function () {
		$("#sysbutton").attr("data-sys", "edit");
		$("#selSys").modal("show");
		clearSearchSys();
		systemTableShow2();
	});
	$("#systemSearch").click(function () {
		if ($("#sysbutton").attr("data-sys") == "list") {
			systemTableShowAll();
		} else {
			systemTableShow2();
		}
	});

	$("#newProjectPlanName").click(function () {
		if ($("#new_systemId").val() == '') {
			layer.alert("请先选择系统！！！", { icon: 2 });
		} else {
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
	$("#editProjectPlanName").click(function () {
		if ($("#edit_systemId").val() == '') {
			layer.alert("请先选择系统！！！", { icon: 2 });
		} else {
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
})
//新增编辑关联子系统弹框搜索
function systemTableShow2() {
	let pdi = $("#newProject_listId").val();
	if ($("#sysbutton").attr("data-sys") == "edit") {
		pdi = $("#editProject_listId").val();
	}
	$("#systemTable2").bootstrapTable('destroy');
	$("#systemTable2").bootstrapTable({
		url: "/devManage/systeminfo/selectAllSystemInfo",
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
				developmentMode: '',//冲刺只能选开发模式是择敏态的系统
				systemName: $.trim($("#SCsystemName").val()),
				systemCode: $.trim($("#SCsystemCode").val()),
        projectId:pdi,
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
//搜索条件可以多选
function systemTableShowAll() {
	$("#systemTable2").bootstrapTable('destroy');
	$("#systemTable2").bootstrapTable({
		url: "/devManage/systeminfo/selectAllSystemInfo2",
		method: "post",
		queryParamsType: "",
		pagination: true,
		sidePagination: "server",
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber: 1,
		pageSize: 10,
		pageList: [5, 10, 15],
		//singleSelect : true,//单选
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

		},
		onCheckAll: function (rows) {//全选
			for (var i = 0; i < rows.length; i++) {
				//if(selectSystemIds.indexOf(rows[i])<=-1){
				selectSystemIds.push(rows[i]);
				//}
			}
		},
		onUncheckAll: function (rows) {
			for (var i = 0; i < rows.length; i++) {
				if (selectSystemIds.indexOf(rows[i]) > -1) {
					selectSystemIds.splice(selectSystemIds.indexOf(rows[i]), 1);
				}
			}
		},
		onCheck: function (row) {//选中复选框
			//if(selectSystemIds.indexOf(row)<=-1){
			selectSystemIds.push(row);
			//}
		},
		onUncheck: function (row) {//取消复选框
			if (selectSystemIds.indexOf(row) > -1) {
				selectSystemIds.splice(selectSystemIds.indexOf(row), 1);
			}
		}


	});
}

//子系统选择后提交
function commitSys() {
	if ($("#sysbutton").attr("data-sys") == "list") {
		if (selectSystemIds.length <= 0) {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		} else {
			var ids = '';
			var names = '';
			for (var i = 0; i < selectSystemIds.length; i++) {
				ids += selectSystemIds[i].id + ",";
				names += selectSystemIds[i].systemName + ',';
			}
			$("#systemId").val(ids);
			$("#systemName").val(names.substring(0, names.length - 1));
		}
	} else {

		var selectContent = $("#systemTable2").bootstrapTable('getSelections')[0];
		if (typeof (selectContent) == 'undefined') {
			layer.alert('请选择一列数据！', { icon: 0 });
			return false;
		}else{
			if ($("#sysbutton").attr("data-sys") == "new") {
				$("#new_systemId").val(selectContent.id);
				$("#new_systemName").val(selectContent.systemName).change();
			}
			if ($("#sysbutton").attr("data-sys") == "edit") {
				$("#edit_systemId").val(selectContent.id);
				$("#edit_systemName").val(selectContent.systemName).change();
			}
			systemIdList = selectContent.id;
		}
	}
	$("#selSys").modal("hide");

}
//重置
function clearSearchSys() {
	$("#SCsystemName").val('');
	$("#SCsystemCode").val('');
	$(".color1 .btn_clear").css("display", "none");
}


//=========================项目计划弹窗start=====================
function planTableShow() {
	var systemId;
	if ($("#planbutton").attr("data-ProjectPlan") == "new") {
		systemId = $("#new_systemId").val();
	} else {
		systemId = $("#edit_systemId").val();
	}
	if($("#system_id").val()){
		systemId = $("#system_id").val().split(',')[0];
	}
	$("#planTable2").bootstrapTable('destroy');
	$("#planTable2").bootstrapTable({
		url: "/devManage/devtask/getAllProjectPlan",
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
				planCode: $.trim($("#planCode").val()),
				planName: $.trim($("#planName").val()),
				responsibleUserName: $("#responsibleUserName").val(),
				projectName: $("#projectName").val(),
				systemId: systemId,
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
			field: "PLAN_CODE",
			title: "计划编号",
			align: 'center'
		}, {
			field: "PLAN_NAME",
			title: "计划名称",
			align: 'center'
		}, {
			field: "projectName",
			title: "所属项目",
			align: 'center'
		}, {
			field: "responsibleUserName",
			title: "责任人",
			align: 'center'
		}],
		onLoadSuccess: function () {

		},
		onLoadError: function () {

		}
	});
}

function commitPlan() {
	var selectContent = $("#planTable2").bootstrapTable('getSelections')[0];
	if (typeof (selectContent) == 'undefined') {
		layer.alert('请选择一列数据！', { icon: 0 });
		return false;
	} else {
		if ($("#planbutton").attr("data-ProjectPlan") == "new") {
			$("#newProjectPlanId").val(selectContent.id);
			$("#newProjectPlanName").val(selectContent.PLAN_NAME).change();
		}
		if ($("#planbutton").attr("data-ProjectPlan") == "edit") {
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
	$(".color1 .btn_clear").css("display", "none");
}
//===========================项目计划弹窗end=================================
//重构表单验证
function refactorFormValidator() {
    $('#edit_sprint').on('hidden.bs.modal', function () {
        $("#newform2").data('bootstrapValidator').destroy();
        $('#newform2').data('bootstrapValidator', null);
        formValidator();
    })
    $('#new_sprint').on('hidden.bs.modal', function () {
        $("#newform").data('bootstrapValidator').destroy();
        $('#newform').data('bootstrapValidator', null);
        formValidator();
    })
}