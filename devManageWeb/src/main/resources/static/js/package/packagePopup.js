$(function(){
	$("#systemName").click(function(){
		$("#sysbutton").attr("data-sys","list");
		$("#selSys").modal("show");
		clearSearch();
		systemTableShow();
	});

	$("#projectName").click(function(){
		$("#projectModal").modal("show");
		clearProject();
		projectShow();
	});
})

/*-----------------项目弹框--------------*/
function projectShow(){
	$("#loading").css('display', 'block');
	$("#projectTable").bootstrapTable('destroy');
	$("#projectTable").bootstrapTable({
		url : "/projectManage/oamproject/selectOamProject2",
		method : "post",
		queryParamsType : "",
		pagination : true,
		sidePagination : "server",
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 5, 10, 15 ],
		singleSelect : true,// 单选
		queryParams : function(params) {
			var param = {
				page : params.pageNumber,
				rows : params.pageSize,
				projectName : $.trim($("#projectModalName").val()),
				projectStatusName : $.trim($("#projectStatus").val()),
				projectManageName : "",
				developManageName : ""
			};
			return param;
		},
		columns : [ {
			checkbox : true,
			width : "30px"
		}, {
			field : "id",
			title : "id",
			visible : false,
			align : 'center'
		}, {
			field : "projectCode",
			title : "项目编号",
			align : 'center'
		}, {
			field : "projectName",
			title : "项目名称",
			align : 'center'
		}, {
			field : "projectStatusName",
			title : "状态",
			align : 'center'
		} ],
		onLoadSuccess : function() {
			$("#loading").css('display', 'none');
		},
		onLoadError : function() {

		}
	});
}

//清空项目搜索条件
function clearProject(){
	$("#projectModalName").val("");
	$("#projectStatus").selectpicker('val', '');
}

//项目弹出提交
function commitPro(){
	var selectContent = $("#projectTable").bootstrapTable('getSelections')[0];
	if(typeof(selectContent) == 'undefined') {
		layer.alert('请选择一列数据！', {icon: 0});
		return false;
	}else{
		$("#projectId").val(selectContent.id);
		$("#projectName").val(selectContent.projectName);
		$("#projectModal").modal("hide");
	}
}


/*-----------------工作任务弹框--------------*/
function DevTaskShow() {
	$("#loading").css('display', 'block');
	$("#featureTable").bootstrapTable('destroy');
	$("#featureTable").bootstrapTable({
		url : "/devManage/package/getFeatur",
		method : "post",
		queryParamsType : "",
		pagination : true,
		sidePagination : "server",
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 5, 10, 15 ],
		singleSelect : false,// 单选
		queryParams : function(params) {
			var param = {
				pageNumber : params.pageNumber,
				pageSize : params.pageSize,
				featureName : $.trim($("#FeatureName").val()),
				featureCode : $.trim($("#FeatureCode").val()),
				requirementId : $.trim($("#artifactInfoId").val()),
				systemId : $.trim($("#systemId").val()),
				requirementId : $("#artifactInfoId").val(),
				sprintId:$("#sprint").val(),
				commissioningWindowId:$("#window").val()
			};
			return param;
		},
		columns : [ {
			checkbox : true,
			width : "30px"
		}, {
			field : "id",
			title : "id",
			visible : false,
			align : 'center'
		}, {
			field : "featureCode",
			title : "任务编号",
			align : 'center'
		}, {
			field : "featureName",
			title : "任务名称",
			align : 'center'
		}, {
			field : "requirementFeatureStatus",
			title : "状态",
			align : 'center',

		} ],
		onLoadSuccess : function() {
			$("#loading").css('display', 'none');
		},
		onLoadError : function() {

		}
	});
}

//清空
function clearFeatuer() {
	$("#FeatureName").val('');
	$("#FeatureCode").val('');
}

//添加关联
function relate(){
	var row = $('#featureTable').bootstrapTable('getSelections')[0];
	if(row == '' || row == undefined){
		layer.alert('请选择任务！',{icon:0,title:"提示信息"});
		return;
	}
	var rows = $('#featureTable').bootstrapTable('getSelections');
	var ids = '';
	$.each(rows,function(index,value){
		ids += value.id + ',';
	});
	var artifactInfoId = $("#artifactInfoId").val();
	$("#loading").css('display', 'block');
	$.ajax({
		type : "post",
		url : "/devManage/package/relateFeature",
		// transformRequest : function(obj) {
		// return angular.toJson(obj);
		// },
		data : {
			'requirementFeatureIds':ids,
			'artifactInfoId':artifactInfoId
		},
		success : function(data) {
			if (data.status == 2) {
				layer.alert('关联任务失败！原因：'+data.errorMessage, {
					icon : 2,
					title : "提示信息"
				});
			} else {
				layer.alert('关联任务成功！', {
					icon : 1,
					title : "提示信息"
				});
				loadFeature(artifactInfoId);
			}
			$("#loading").css('display', 'none');
			$("#featureP").modal('hide');
		}
	});
}

/*-----------------系统弹框--------------*/
function systemTableShow(){
	$("#systemTable").bootstrapTable('destroy');
	$("#systemTable").bootstrapTable({
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

//重置
function clearSearch(){
	$("#systemGroupId").empty();
	$("#systemArtifactID").empty();
	$("#systemId").val('');
	$("#systemName").val('');
	$("#snapshotRepositoryName").selectpicker('val', '');
	$("#systemGroupId").selectpicker('val', '');
	$("#systemArtifactID").selectpicker('val', '');
	$("#systemVersion").val('');
	$("#projectName").val('');
	$("#tag").selectpicker('val', '');
}

//选择系统
function commitSys(){
	var selectContent = $("#systemTable").bootstrapTable('getSelections')[0];
	if(typeof(selectContent) == 'undefined') {
		layer.alert('请选择一列数据！', {icon: 0});
		return false;
	}else{
		if($("#sysbutton").attr("data-sys") == "list"){
			$("#systemId").val(selectContent.id);
			$("#systemName").val(selectContent.systemName);
			$("#searchGroupId").val(selectContent.groupId);
			$("#searchArtifactId").val(selectContent.artifactId);
			var repStr = '';
			if(selectContent.snapshotRepositoryName != '' && selectContent.snapshotRepositoryName != null){
				repStr += '<option value = "'+selectContent.snapshotRepositoryName+'" >快照仓库：'+selectContent.snapshotRepositoryName+'</option>';
			}
			if(selectContent.releaseRepositoryName != '' && selectContent.releaseRepositoryName != null){
				repStr += '<option value = "'+selectContent.releaseRepositoryName+'" >发布仓库：'+selectContent.releaseRepositoryName+'</option>';
			}
			$("#snapshotRepositoryName").html(repStr);
			getGroupAndArtifactID(selectContent.id);
			$("#snapshotRepositoryName").selectpicker('refresh');
			
		}
//   	 if($("#sysbutton").attr("data-sys") == "new"){
//   		$("#newsystemId").val(selectContent.id);
//	    	$("#newsystemName").val(selectContent.systemName); 
//   	 }
//   	 if($("#sysbutton").attr("data-sys") == "edit"){
//    		$("#editsystemId").val(selectContent.id);
// 	    	$("#editsystemName").val(selectContent.systemName); 
//    	 }
		$("#selSys").modal("hide");
	}

}

//获取该系统下的group和artifactId,并对下拉框进行填充
function getGroupAndArtifactID(systemId){
	
	$.ajax({
		url:"/devManage/package/getGroupAndArtifactID",
		type:"post",
		dataType:"json",
		data:{
			systemId:systemId
		},
		success:function(data){
			if(data.gruupId.length>0){
				var gruupId=data.gruupId;
				var repStr = '';
				for(var i=0;i<gruupId.length;i++){
					if(gruupId[i]!=null&&gruupId[i]!=""){
						repStr += '<option value = "'+gruupId[i]+'" >'+gruupId[i]+'</option>';
					}
					
				}
				if(repStr!=""){
					//$("#systemGroupId").append("<option  selected ='selected' value=''>请选择</option>");
					$("#systemGroupId").append(repStr);
				}
			}
			if(data.artifactId.length>0){
				var artifactId=data.artifactId;
				var repStr2 = '';
				for(var i=0;i<artifactId.length;i++){
					if(artifactId[i]!=null&&artifactId[i]!=""){
						repStr2 += '<option value = "'+artifactId[i]+'" >'+artifactId[i]+'</option>';
					}
				}
				if(repStr2!=""){
					$("#systemArtifactID").append("<option  selected ='selected' value=''>请选择</option>");
					$("#systemArtifactID").append(repStr2);
				}
				
			}
			$('.selectpicker').selectpicker('refresh');
		}
	})

}