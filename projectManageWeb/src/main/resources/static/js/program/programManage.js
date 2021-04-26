/**
 * 
 */
$(() => {
	pageInit();
	formValidator();
	refactorFormValidator();
	
	//查询：项目群经理弹窗
	$("#manageUser").click(function () {
		selectUserIds = [];
		$("#userbutton").attr("data-user", "manageUser");
		clearSearchUser();
		userTableShow2(2);
	});

	$("#programManager").click(function () {
		selectUserIds = [];
		$("#userbutton").attr("data-user", "programManageUser");
		clearSearchUser();
		userTableShow2(2);
	});
	downOrUpButton();
})

/**
 * @description 表格数据加载
 */
function pageInit() {
	$("#loading").css('display', 'block');
	$("#program_list").jqGrid({
		url: '/projectManage/program/getAllPrograms',
		datatype: 'json',
		mtype: "POST",
		height: 'auto',
		width: $(".content-table").width() * 0.999,
		colNames: ['id', '项目群编号', '项目群名称', '项目群经理', '操作'],
		postData: {},
		colModel: [
			{ name: 'id', index: 'id', hidden: true },
			{ name: 'programNumber', index: 'programNumber' },
			{ name: 'programName', index: 'programName' },
			{ name: 'manageUser', index: 'manageUser' },
			{
				name: '操作', align: "center", fixed: true, sortable: false, resize: false, search: false, width: 140,
				formatter: function (value, grid, rows, state) {
					// var row = JSON.stringify(rows).replace(/"/g, '&quot;');
					var span_ = "<span>&nbsp;|&nbsp;</span>";
					var a = '<a class="a_style" onclick="editProgram(' + rows.id + ')">编辑</a>';
					var b = '<a class="a_style" onclick="show_detail(' + rows.id + ',' + rows.projectIds + ')">管理</a>';
					var opt_status = [];
					// if (projectEdit == true) {
					opt_status.push(a);
					// }
					// if (userPost == true) {
					opt_status.push(b);
					// }
					return opt_status.join(span_);
				}
			},
		],
		rowNum: 10,
		rowTotal: 200,
		rowList: [10, 20, 30],
		rownumWidth: 40,
		pager: '#pager2',
		sortable: false,
		loadtext: "数据加载中......",
		viewrecords: true, //是否要显示总记录数 
		jsonReader: {
			repeatitems: false,
			root: "data",
		},
		gridComplete: function () {
		},
		loadComplete: function () {
			$("#loading").css('display', 'none');
		},
		beforeRequest: function () {
			$("#loading").css('display', 'block');
		}
	}).trigger("reloadGrid");
}

/**
 * @description 项目群主页
 */
function show_detail(ID) {
	var data = { "menuButtonId": "9911", "url": "../projectManageui/program/toProgramDetail", "menuButtonName": "新建类项目群主页" };
	window.parent.toPageAndValue(data.menuButtonName, data.menuButtonId, data.url + "?id=" + ID);
}

/**
 * @description 条件搜索
 */
function searchInfo() {
	$("#program_list").jqGrid('setGridParam', {
		postData: {
			"programNumber": $.trim($("#programNumber").val()),
			"programName": $.trim($("#programName").val()),
			"manageUserId": $("#manageUserId").val(),
		},
		page: 1
	}).trigger("reloadGrid"); //重新载入
}

/**
 * @description 重置搜索条件
 */
function clearSearch() {
	$("#programNumber").val('');
	$("#programName").val('');
	$("#manageUserId").val('');
	$("#manageUser").val('');
}

/**
 * @description 清空人员条件
 */
function clearSearchUser() {
	$("#userName").val('');
	$("#deptName").val('');
	$("#companyName").val('');
}

/**
 * @description 人员弹窗
 * @param userName
 * @param companyName
 * @param deptName
 */
function userTableShow2(status) {
	if(status == 2){    //默认不查询
		$("#userModal").modal("show");
	}else{
		$("#loading").css('display', 'block');
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
					userName: $.trim($("#userName").val()),
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
				$("#userModal").modal("show");
				$("#loading").css('display', 'none');
			},
			onLoadError: function () {
				$("#loading").css('display', 'none');
			}
		});
	}
}

/**
 * @description 人员选择
 */
function commitUser() {
	var selectContent = $("#userTable").bootstrapTable('getSelections')[0];
	if (typeof (selectContent) == 'undefined') {
		layer.alert('请选择一列数据！', { icon: 0 });
		return false;
	} else {
		if ($("#userbutton").attr("data-user") == 'manageUser') {
			$("#manageUserId").val(selectContent.id);
			$("#manageUser").val(selectContent.userName).change();
		}
		if ($("#userbutton").attr("data-user") == 'programManageUser') {
			$("#programManagerId").val(selectContent.id);
			$("#programManager").val(selectContent.userName).change();
		}
		$("#userModal").modal("hide");
	}
}

/**
 * @description 新增项目群
 */
function addProgram() {
	_clear_val('#add_group_form');
	$("#project_group_table tbody").html("");
	$("#select_project").data("type", 'add');
	$("#add_group_modal").modal('show');
}

/**
 * @description 清空value
 */
function _clear_val(parent) {
	$(parent).find('input').each(function (idx, val) {
		$(val).val('');
	})
	//  $(parent).find('select').each(function (idx, val) {
	//    $(val).val('');
	//  })
	$(parent).find('select').each(function (idx, val) {
		$(val).selectpicker('val', '');
	})
	$(".selectpicker").selectpicker('refresh');
	$(parent).find('textarea').each(function (idx, val) {
		$(val).val('');
	})
}

/**
 * @description 新增提交
 */
function add_group_submit() {
	$('#add_group_form').data('bootstrapValidator').validate();
	if (!$('#add_group_form').data("bootstrapValidator").isValid()) {
		return;
	}
	var projectIds = '';
	$("#project_group_tbody tr").each(function (idx, val) {
		projectIds += $(val).data('id') + ',';
	})
	if (projectIds == '') {
		layer.alert("请添加项目", { icon: 0 });
		return
	}
	var submit_data = {
		programName: $.trim($('#projectGroupName').val()),
		manageUserId: $.trim($('#programManagerId').val()),
		projectIds: projectIds,
		programIntro: $.trim($('#projectGroupInfo').val()),
		programBackground: $.trim($('#projectGroupBackground').val()),
		remark: $.trim($('#projectGroupRemark').val()),
	};
	$("#loading").css('display', 'block');
	$.ajax({
		type: "post",
		url: "/projectManage/newProject/insertProgram",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(submit_data),
		success: function (data) {
			if (data.status == 1) {
				layer.alert("新增成功", { icon: 1 });
				$("#add_group_modal").modal("hide");
				$("#program_list").trigger("reloadGrid");
			} if (data.status == 2) {
				layer.alert("新增失败", { icon: 2 });
			}
			$("#loading").css('display', 'none');
		}
	});

}

/**
 * @description 清空项目弹窗搜索
 */
function clearSearchUser_project(){
	$("#project_Code").val('');
	$("#project_Name").val('');
    $("#dept_Name").val('');
}

/**
 * @description 项目群  新增项目
 */
function add_project_group(){
	$("#loading").css('display', 'block');
    $("#group_list_Table").bootstrapTable('destroy');
    $("#group_list_Table").bootstrapTable({
        url:"/projectManage/newProject/getNewProjectByPage",
        method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
        singleSelect : false,
        queryParams : function(params) {
            var param={
            	projectCode: $.trim($("#project_Code").val()),
            	projectName :  $.trim($("#project_Name").val()),
                deptName : $.trim($("#dept_Name").val()),
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
            field : "projectCode",
            title : "项目编号",
            align : 'center'
        },{
            field : "projectName",
            title : "项目名称",
            align : 'center'
        },{
            field : "planStartDate",
            title : "计划起止日期",
            align : 'center',
            formatter:function(value,row,index){
                return isValueNull(row.planStartDate)+" - "+isValueNull(row.planEndDate);
            }
        },{
            field : "projectStatusName",
            title : "项目状态",
            align : 'center'
        },
    {
    	field : "deptName",
    	title : "所属处室",
    	align : 'center'
    },],
        onLoadSuccess:function(){
          $('#group_list_Modal').modal('show');
          $("#loading").css('display', 'none');
        },
        onLoadError : function() {
        	$("#loading").css('display', 'none');
		},
	});
}

/**
 * @description 选择项目
 */
function push_group() {
	var selectContent = $("#group_list_Table").bootstrapTable('getSelections')[0];
	if (typeof (selectContent) == 'undefined') {
		layer.alert('请选择一列数据！', { icon: 0 });
		return false;
	} else {

		var ids = '';
		var names = '';
		var _flag = true;
		var selectContent = $("#group_list_Table").bootstrapTable('getSelections');
		var parent_name = '';
		if ($("#select_project").data("type") == 'add') {
			parent_name = $("#project_group_tbody");
		} else {
			parent_name = $("#project_group_tbody2");
		}
		parent_name.find('tr').each(function (index, value) {
			selectContent.map(function (val, idx) {
				if ($(value).data('id') == val.id) {
					layer.alert('编号' + val.projectCode + '已存在,不能重复添加', { icon: 0 })
					_flag = false;
					return
				}
			})
		})
		if (!_flag) return
		selectContent.map(function (val, idx) {
			parent_name.append(`
			        <tr data-id="${val.id}">
			          <td>${val.projectCode}</td>
			          <td>${val.projectName}</td>
			          <td>${val.planStartDate || ''} - ${val.planEndDate || ''}</td>
			          <td>${val.projectStatusName}</td>
			          <td>${val.deptName}</td>
			          <td><a href="#" class="a_style" onclick="removeThis(this)">移除</a></td>
			        </tr>
			      `);
		})
		$("#group_list_Modal").modal("hide");
	}
}

/**
 * @description 删除未提交项目
 */
function removeThis(This) {
	$(This).parent().parent().remove();
}


//编辑
function editProgram(id) {
	$("#select_project").data("type", 'edit');
	$("#project_group_tbody2").empty();
	_clear_val('#edit_group_form');
	$("#loading").css('display', 'block');
	$.ajax({
		type: "post",
		url: "/projectManage/program/getProgramById",
		dataType: "json",
		data: {
			'id': id
		},
		success: function (data) {
			var result = data.data;
			$('#edit_id').val(result.id);
			$('#editProgramName').val(result.programName);
			$('#editProgramManage').val(result.manageUser);
			$('#editProgramManageId').val(result.manageUserId);
			$('#editProgramIntro').val(result.programIntro);
			$('#editProgramBackground').val(result.programBackground);
			$('#editRemark').val(result.remark);
			for (var i = 0; i < result.projectList.length; i++) {
				console.log(result.projectList[i].peojectCode)
				$("#project_group_tbody2").append(
					"<tr data-id=" + result.projectList[i].id + ">" +
					"<td>" + result.projectList[i].projectCode + "</td>" +
					"<td>" + result.projectList[i].projectName + "</td>" +
					"<td>" + isValueNull(result.projectList[i].planStartDate) + " - " + isValueNull(result.projectList[i].planEndDate) + "</td>" +
					"<td>" + result.projectList[i].projectStatusName + "</td>" +
					"<td>" + result.projectList[i].deptName + "</td>" +
					"<td><a href='#' class='a_style' onclick='removeThis(this)'>移除</a></td></tr>"
				);
			}
			$("#edit_group_modal").modal('show');
			$("#loading").css('display', 'none');
		}
	})
}

//编辑提交
function edit_group_submit() {
	$('#edit_group_form').data('bootstrapValidator').validate();
	if (!$('#edit_group_form').data("bootstrapValidator").isValid()) {
		return;
	}
	var projectIds = '';
	$("#project_group_tbody2 tr").each(function (idx, val) {
		projectIds += $(val).data('id') + ',';
		console.log(projectIds)
	})
	console.log(projectIds)
	if (projectIds == '') {
		layer.alert("请选择项目", { icon: 0 });
		return
	}
	var submit_data = {
		id: $('#edit_id').val(),
		programName: $.trim($('#editProgramName').val()),
		manageUserId: $.trim($('#editProgramManageId').val()),
		projectIds: projectIds,
		programIntro: $.trim($('#editProgramIntro').val()),
		programBackground: $.trim($('#editProgramBackground').val()),
		remark: $.trim($('#editRemark').val()),
	};
	$("#loading").css('display', 'block');
	$.ajax({
		type: "post",
		url: "/projectManage/program/updateProgram",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(submit_data),
		success: function (data) {
			if (data.status == 1) {
				layer.alert("编辑成功", { icon: 1 });
				$("#edit_group_modal").modal("hide");
				$("#program_list").trigger("reloadGrid");
			} if (data.status == 2) {
				layer.alert("编辑失败", { icon: 2 });
			}
			$("#loading").css('display', 'none');
		}
	});
}


//表单校验
function formValidator() {
	$('#add_group_form').bootstrapValidator({
		message: '不能为空',//通用的验证失败消息
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			projectGroupName: {
				validators: {
					notEmpty: {
						message: '此项不能为空'
					}
				}
			},
			programManager: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '此项不能为空'
					}
				}
			},
		}
	})
	$('#edit_group_form').bootstrapValidator({
		message: '不能为空',//通用的验证失败消息
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			editProgramName: {
				validators: {
					notEmpty: {
						message: '此项不能为空'
					}
				}
			},
			editProgramManage: {
				validators: {
					notEmpty: {
						message: '此项不能为空'
					}
				}
			},
		}
	})
}

//重构表单验证
function refactorFormValidator() {
	$('#add_group_modal').on('hidden.bs.modal', function () {
		$("#add_group_form").data('bootstrapValidator').destroy();
		$('#add_group_form').data('bootstrapValidator', null);
		formValidator();
	})
	$('#edit_group_modal').on('hidden.bs.modal', function () {
		$("#edit_group_form").data('bootstrapValidator').destroy();
		$('#edit_group_form').data('bootstrapValidator', null);
		formValidator();
	})
}