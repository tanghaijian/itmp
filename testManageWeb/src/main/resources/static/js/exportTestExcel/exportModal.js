var selectSysIds = [];
var systemType=0

function showSystem(systemClass){
	clearSearchSys();
	selectSysIds = [];
	systemType=systemClass
	systemTableShowAll();
	$("#sysbutton").attr('systemClass',systemClass);
	$("#selSys").modal('show');
}

function showConfigModal(type){
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/getOamAndNewSystem",
		method: "post",
		dataType: "json",
		success: function (data) {
			$("#loading").css('display', 'none');
			createSystemTable(data.oamSystem,"oamSystem");
			createSystemTable(data.newSystem,"newSystem");
			createSystemTable(data.agileSystem,"agileSystem"); //敏捷期系统 //测试月报
			$("#configModal").modal('show');
		}
	});
}

function createSystemTable(data,tableId){
	if($("#"+tableId+"Table").length<=0) return false
	$("#"+tableId+"Table").bootstrapTable('destroy');
    $("#"+tableId+"Table").bootstrapTable({
        queryParamsType:"",
        data:data,
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
		}],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
}
/*系统选择*/
function systemTableShowAll() {
	$("#systemTable").bootstrapTable('destroy');
	$("#systemTable").bootstrapTable({
		url: systemType===3?"/testManage/testReport/selectAllSystemInfoByAgile":"/testManage/testReport/selectAllSystemInfo",
		method: "post",
		queryParamsType: "",
		pagination: true,
		sidePagination: "server",
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber: 1,
		pageSize: 10,
		pageList: [5, 10, 15],
		singleSelect: false,//单选
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

function commitSys(){
	if(selectSysIds.length == 0){
		layer.alert("至少选择一条记录",{icon:0});
		return;
	}
	var ids = [];
	$.each(selectSysIds,function(index,value){
		ids.push(value.id);
	})
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/addSystemClass",
		method: "post",
		dataType: "json",
		data:{
			systemIdStr:ids.join(","),
			systemClass:$("#sysbutton").attr('systemClass')
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			if(data.status == 1){
				layer.alert("添加成功",{icon:1});
				$("#selSys").modal('hide');
				showConfigModal();
			}else{
				layer.alert("添加失败",{icon:2});
			}
		}
	});
}

function batchDelete(tableId,removeType){
	var systemClass = $("#sysbutton").attr('systemClass');
	var system = $("#"+tableId+"").bootstrapTable('getSelections');
	debugger;
	if(system[0] == undefined){
		layer.alert("至少选择一条记录",{icon:0});
		return;
	}
	var systemIdArr = [];
	$.each(system,function(index,value){
		systemIdArr.push(value.id);
	});
	layer.confirm('是否移除选中系统?', {icon: 3, title:'提示'}, function(index){
		  //do something
		$("#loading").css('display', 'block');
		$.ajax({
			url: "/testManage/testReport/addSystemClass",
			method: "post",
			dataType: "json",
			data:{
				systemIdStr:systemIdArr.join(","),
				systemClass:removeType?removeType:''
			},
			success: function (data) {
				$("#loading").css('display', 'none');
				if(data.status == 1){
					layer.alert("删除成功",{icon:1});
					showConfigModal();
				}else{
					layer.alert("删除失败",{icon:2});
				}
			}
		});
		  layer.close(index);
		});
}

function clearSearchSys(){
	$("#SCsystemCode").val("");
	$("#SCsystemName").val("");
}
