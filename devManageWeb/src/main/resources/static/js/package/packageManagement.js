/**
 * @author fanwentao
 */
var map = '';
$(function() {
	pageInit();
	// 搜索展开和收起
	$("#downBtn").click(function() {
		if ($(this).children("span").hasClass("fa-caret-up")) {
			$(this).children("span").removeClass("fa-caret-up");
			$(this).children("span").addClass("fa-sort-desc");
			$("#search_div").slideUp(200);
		} else {
			$(this).children("span").removeClass("fa-sort-desc");
			$(this).children("span").addClass("fa-caret-up");
			$("#search_div").slideDown(200);
		}
	})
	$("#createDate").datetimepicker({
		minView : "month",
		format : "yyyy-mm-dd",
		autoclose : true,
		todayBtn : true,
		language : 'zh-CN',
		pickerPosition : "bottom-left"
	});
});

//初始化表格数据
function pageInit() {
	$("#packagelist").jqGrid('clearGridData');
	$("#packagelist")
		.jqGrid(
			{
				url : '/devManage/package/getPackage',
				contentType : "application/json; charset=utf-8",
				mtype : "post",
				height : 'auto',
				multiselect : true,
				width : $(".content-table").width() * 0.999,
				colNames : [ 'numCount', 'id', 'artifactInfoId', 'md5', '系统编号', '系统名称', '微服务名称',
					'Repository', 'Group', 'ArtifactID', 'Version',
					'时间戳', '包件标记', '操作' ],
				colModel : [
					{
						name : 'numCount',
						index : 'numCount',
						key : true,
						hidden : true
					},
					{
						name : 'id',
						index : 'id',
						hidden : true
					},
					{
						name : 'artifactInfoId',
						index : 'artifactInfoId',
						hidden : true
					},
					{
						name : 'md5',
						index : 'md5',
						hidden : true
					},
					{
						name : 'systemCode',
						index : 'systemCode',
						width : '100',
						sorttype : 'string',
						searchoptions : {
							sopt : [ 'cn' ]
						}
					},
					{
						name : 'systemName',
						index : 'systemName',
						searchoptions : {
							sopt : [ 'cn' ]
						},
						formatter : function(value, grid, rows,
											 state) {
							if (rows.moduleName != ''
								&& rows.moduleName != undefined) {
								return value
									+ "<sup class='micSup'>多模块系统</sup>";
							} else {
								return value;
							}
						}
					},
					{
						name : 'moduleName',
						index : 'moduleName',
						searchoptions : {
							sopt : [ 'cn' ]
						}
					},
					{
						name : 'repository',
						index : 'repository'
					},
					{
						name : 'group',
						index : 'group'
					},
					{
						name : 'artifactId',
						index : 'artifactId',
						width : '100',
						searchoptions : {
							sopt : [ 'cn' ]
						}
					},
					{
						name : 'version',
						index : 'version',
						searchoptions : {
							sopt : [ 'cn' ]
						}
					},
					{
						name : 'updateDate',
						index : 'updateDate',
						searchoptions : {
							sopt : [ 'cn' ]
						},
						formatter : function(value, grid, rows, state) {
							if (rows.updateDate != '' && rows.updateDate != undefined) {
								return getyyyy_MM_dd_HH_mm_ss(value);
							} else {
								return '';
							}
						}
					},
					{
						name : 'environmentName',
						index : 'environmentName',
						searchoptions : {
							sopt : [ 'cn' ]
						}
					},
					{
						name : '操作',
						index : '操作',
						align : "center",
						fixed : true,
						sortable : false,
						resize : false,
						search : false,
						width : '200',

						formatter : function(value, grid, rows,
											 state) {
							var result = "";
							var row = JSON.stringify(rows).replace(
								/"/g, '&quot;');
							result += '<a class="a_style" href="#" onclick="downPackage('
								+ row + ')">下载</a>';

							if(rows.systemCode != null && rows.systemCode != ""){

								if (rows.environmentType == ""
									|| rows.environmentType == null
									|| rows.environmentType == undefined) {
									if(packageTag){
										result += '<span> | </span>';
										result += '<a class="a_style" href="#" onclick="packageSign('
											+ row + ')">包件标记</a>';
									}
								} else {
									if(packageEdit){
										result += '<span> | </span>';
										result += '<a class="a_style" href="#" onclick="packageSign('
											+ row + ')">修改标记</a>';
									}
									if(packageCancel){
										result += '<span> | </span>';
										result += '<a class="a_style" href="#" onclick="deleteTag('
											+ rows.artifactInfoId + ')">取消标记</a>';
									}
								}
								if (packageDelete) {
									result += '<span> | </span>';
									result += '<a class="a_style" href="#" onclick="deletePackage('
										+ row + ')">删除</a>';
								}
							}
							return result;
						}
					} ],
				rowNum : 10,
				rowTotal : 200,
				rowList : [ 10, 20, 30 ],
				rownumWidth : 40,
				pager : '#packagepager',
				sortable : true, // 是否可排序
				sortorder : 'asc',
				sortname : 'id',
				loadtext : "数据加载中......",
				viewrecords : true, // 是否要显示总记录数
				loadComplete: function (){
					$("#loading").css('display','none');
				},
				beforeRequest:function(){
					$("#loading").css('display','block');
				},
				loadError:function(xhr,status,error){
					console.info(error);
					$("#loading").css('display','none');
					layer.alert("系统错误",{icon : 2});
				}
			}).trigger("reloadGrid");
}

//加载包件信息
function packageSign(rows) {
	$("#deploy").empty();
	getEnvType(rows.id);
	$("#title_code").html(rows.systemCode);
	$("#title_name").html(rows.systemName);
	$("#artifactInfoId").val(rows.artifactInfoId);
	$("#repository").text(rows.repository);
	$("#version").text(rows.version);
	$("#name").text(rows.systemName);
	$("#group").text(rows.group);
	$("#path").val(rows.path);
	$("#systemId").val(rows.id);
	$("#systemModuleId").val(rows.moduleId);
	$("#artifactId").val(rows.artifactId);
	$("#remark").val('');
	for(var key in map){
		str = '<div class="col-md-2" style="margin-bottom:10px;"> <span><input value='+key+'  type="checkbox" name="envType"  />'+map[key]+'</span> </div>'
		$("#deploy").append(str);
	}
	$("#packageSign [name=envType]").prop('checked',false);
	if(rows.environmentType != undefined){
		var envArr = rows.environmentType.split(',');
		$.each(envArr,function(index,value){
			$("#packageSign [name=envType][value="+value+"]").prop('checked',true);
		})
	}
	$("#packageSign").modal("show");
	if(rows.artifactInfoId != undefined && rows.artifactInfoId != ''){
		$("#packageSign .content-btn").show();
		$("#packageSign .content-table").show();
		loadFeature(rows.artifactInfoId);
	}else{
		$("#packageSign .content-btn").hide();
		$("#packageSign .content-table").hide();
	}
}

//获取环境类型
function getEnvType(id){
	$.ajax({
		type : "post",
		url : "/devManage/package/getEnvType",
		async:false,
		data : {
			'id':id
		},
		success : function(data) {
			map = data;
		}
	});
}

//删除标记
function deleteTag(id){
	layer.confirm("确定取消包件标记吗？",{
		btn: ['确定','取消'],
		title: "提示信息"
	},function(){
		$.ajax({
			type : "post",
			url : "/devManage/package/removeTag",
			// transformRequest : function(obj) {
			// return angular.toJson(obj);
			// },
			data : {
				'id':id
			},
			success : function(data) {
				if (data.status == 2) {
					layer.alert('取消包件标记失败！原因：'+data.errorMessage, {
						icon : 2,
						title : "提示信息"
					});
				} else {
					layer.alert('取消包件标记成功！', {
						icon : 1,
						title : "提示信息"
					});
					searchInfo();
				}
				$("#loading").css('display', 'none');
				$("#featureP").modal('hide');
			}
		});
	});
}
//批量删除包件
function deletePackages() {
	var idArr=$('#packagelist').jqGrid('getGridParam','selarrrow')
    if(idArr.length<=0 ){
    	layer.alert("请选择至少一个包件", { icon: 3});
    	return;
    }
	
	var artifactArr=[];
	for(var i=0;i<idArr.length;i++){
        var rows = $("#packagelist").jqGrid('getRowData', idArr[i]);
        var artifactObj = {};
        artifactObj.id = rows.artifactInfoId;
        artifactObj.repository = rows.repository;
        artifactObj.groupId = rows.group;
        artifactObj.artifactId = rows.artifactId;
        artifactObj.version = rows.version;
        artifactObj.md5 = rows.md5;
        artifactArr.push(artifactObj);
	}
	deletePackageOK(artifactArr);
}

//单笔删除包件
function deletePackage(row) {
	var artifactArr=[];
	var artifactObj = {};
	artifactObj.id = row.artifactInfoId;
	artifactObj.repository = row.repository;
	artifactObj.groupId = row.group;
	artifactObj.artifactId = row.artifactId;
	artifactObj.version = row.version;
	artifactObj.md5 = row.md5;
    artifactArr.push(artifactObj);
    deletePackageOK(artifactArr);
}

function deletePackageOK(artifactArr) {
	layer.confirm("确定删除包件吗？",{
		btn: ['确定','取消'],
		title: "提示信息"
	},function(){
		$("#loading").css('display', 'block');
		$.ajax({
			type : "post",
			url : "/devManage/package/deletePackage",
			data : {
				'artifactJson':JSON.stringify(artifactArr)
			},
			success : function(data) {
				$("#loading").css('display', 'none');
				if (data.status == 2) {
					layer.alert('删除包件失败！原因：'+data.errorMessage, {
						icon : 2,
						title : "提示信息"
					});
				} else {
					layer.alert('删除包件成功！', {
						icon : 1,
						title : "提示信息"
					});
					searchInfo();
				}
				$("#loading").css('display', 'none');
				$("#featureP").modal('hide');
			}
		});
	});
	
}

//加载artifact信息
function loadFeature(id){
	$.ajax({
		type : "post",
		url : "/devManage/package/getArtifactInfo",
		// transformRequest : function(obj) {
		// return angular.toJson(obj);
		// },
		data : {
			'id' : id,
		},
		success : function(data) {
			if (data.status == 2) {
				layer.alert('查询包件失败！原因：'+data.errorMessage, {
					icon : 2,
					title : "提示信息"
				});
			} else {
				if(data.artifactInfo != undefined && data.requirementFeatures != undefined){
					$("#remark").val(data.artifactInfo.remark);
					loadingFeatureTable(data.requirementFeatures);
				}
			}
		}
	});
}

//移除关联
removeFeature = function(id){
	var artifactInfoId = $("#artifactInfoId").val();
	layer.confirm("确定删除此条关联任务吗？",{
		btn: ['确定','取消'],
		title: "提示信息"
	},function(){
		$.ajax({
			type : "post",
			url : "/devManage/package/removeFeature",
			// transformRequest : function(obj) {
			// return angular.toJson(obj);
			// },
			data : {
				'id':id
			},
			success : function(data) {
				if (data.status == 2) {
					layer.alert('移除关联任务失败！原因：'+data.errorMessage, {
						icon : 2,
						title : "提示信息"
					});
				} else {
					layer.alert('移除关联任务成功！', {
						icon : 1,
						title : "提示信息"
					});
					loadFeature(artifactInfoId);
				}
				$("#loading").css('display', 'none');
				$("#featureP").modal('hide');
			}
		});
	});
}
//批量移除关联任务
function removeManyFeature(){
	var artifactInfoId = $("#artifactInfoId").val();
	var rows = $('#signTable').bootstrapTable('getSelections');
	var ids = [];
	$.each(rows,function(index,value){
		ids.push(value.id);
	});
	layer.confirm("确定删除选中的关联任务吗？",{
		btn: ['确定','取消'],
		title: "提示信息"
	},function(){
		$.ajax({
			type : "post",
			url : "/devManage/package/removeManyFeature",
			// transformRequest : function(obj) {
			// return angular.toJson(obj);
			// },
			data : {
				'ids':JSON.stringify(ids)
			},
			success : function(data) {
				if (data.status == 2) {
					layer.alert('移除关联任务失败！原因：'+data.errorMessage, {
						icon : 2,
						title : "提示信息"
					});
				} else {
					layer.alert('移除关联任务成功！', {
						icon : 1,
						title : "提示信息"
					});
					loadFeature(artifactInfoId);
				}
				$("#loading").css('display', 'none');
				$("#featureP").modal('hide');
			}
		});
	});
}

// 加载关联任务表格
function loadingFeatureTable(data) {
	$("#loading").css('display', 'block');
	$('#signTable')
		.bootstrapTable('destroy')
		.bootstrapTable(
			{
				method : 'post',
				cache : false,
				striped : true,
				queryParamsType : "",
				pagination : true,
				sidePagination : "server",
				columns : [
					{
						checkbox : true,
						width : "30px"
					},
					{
						field : 'id',
						title : 'id',
						visible : false
					},
					{
						field : 'featureCode',
						title : '任务编号',
						sorttype : 'string',
						align : 'center',
						searchoptions : {
							sopt : [ 'cn' ]
						}
					},
					{
						field : 'featureName',
						title : '任务名称',
						align : 'center',
						searchoptions : {
							sopt : [ 'cn' ]
						}
					},
					{
						field : 'requirementFeatureStatus',
						title : '任务状态',
						align : 'center',
						searchoptions : {
							sopt : [ 'cn' ]
						}
					},
					{
						title : '操作',
						align : 'center',
						fixed : true,
						sortable : false,
						resize : false,
						search : false,
						formatter : function(value, grid, rows,
											 state) {
							var result = "";
							result += '<a class="a_style" href="javascript:;" onclick="removeFeature('
								+ grid.id + ')">移除</a>';
							return result;
						}
					} ],
				data : data
			});
	$("#loading").css('display', 'none');
}


//通过系统ID获取投产窗口，用于包件标记在关联开发任务时，初始化投产窗口下拉框
function getWindowsBySystemId(systemId){

	$.ajax({
		url:"/devManage/deploy/getWindowsLimit",
		type:"post",
		dataType:"json",
		data:{
			systemId:systemId,

		},
		success:function(data){
			$("#window").empty();
			$("#window").append("<option value=''>请选择</option>");
			if(data.windows.length>0){

				for(var i = 0;i<data.windows.length;i++){
					if(data.windows[i].featureStatus=="defaultSelect"){

						$("#window").append('<option selected value="'+data.windows[i].id+'">'+data.windows[i].windowName+'</option>')
					}else {
						$("#window").append('<option value="' + data.windows[i].id + '">' + data.windows[i].windowName + '</option>');
					}
				}
			}
			$('.selectpicker').selectpicker('refresh');
		}
	})


}

//通过系统ID获取冲刺，用于包件标记在关联开发任务时，初始化冲刺下拉框
function getSprintBySystemId(systemId){

	$.ajax({
		url:"/devManage/deploy/getSprintBySystemId",
		type:"post",
		dataType:"json",
		data:{
			systemId:systemId
		},
		success:function(data){
			$("#sprint").empty();
			$("#sprint").append("<option value=''>请选择</option>");
			if(data.sprintInfos.length>0){

				for(var i = 0;i<data.sprintInfos.length;i++){
					if(data.sprintInfos[i].default=="true"){
						$("#sprint").append('<option selected value="'+data.sprintInfos[i].id+'">'+data.sprintInfos[i].sprintName+'</option>')
					}else {
						$("#sprint").append('<option value="' + data.sprintInfos[i].id + '">' + data.sprintInfos[i].sprintName + '</option>')
					}
				}
			}
			$('.selectpicker').selectpicker('refresh');
		}
	})


}

//显示开发任务弹框
function showFeature() {

	getSprintBySystemId($.trim($("#systemId").val()));
	getWindowsBySystemId($.trim($("#systemId").val()));
	setTimeout(function () {
		DevTaskShow();
	},1500)
	//DevTaskShow();
	$("#featureP").modal("show");
}

// 搜索
function searchInfo() {
	var obj = {};
	obj.repository = $.trim($("#snapshotRepositoryName").selectpicker('val'));
	obj.groupId = $.trim($("#systemGroupId").val());
	obj.artifactId = $.trim($("#systemArtifactID").val());
	obj.q = $.trim($("#systemVersion").val());
	obj = JSON.stringify(obj);
	var tag = $("#tag").selectpicker('val');
	var tagStr = "";
	if(tag != null){
		$.each(tag,function(index,value){
			tagStr += value+',';
		})
	}
	var systemId = $("#systemId").val();
	var snapshotRepositoryName = $("#snapshotRepositoryName").selectpicker('val');
	if (systemId == "") {
		layer.alert('未选择系统', {
			icon : 0
		});
		return false;
	}
	if (snapshotRepositoryName == "") {
		layer.alert('系统无仓库Repository', {
			icon : 0
		});
		return false;
	}
	// 重新载入
	$("#packagelist").jqGrid('setGridParam', {
		url : '/devManage/package/getPackage',
		datatype : 'json',
		postData : {
			"workTask" : obj,
			"systemId" : systemId,
			"projectId": $("#projectId").val(),
			"tagStr" : tagStr.substr(0,tagStr.length-1)
		},
		page : 1
	}).trigger("reloadGrid");
}
//重置
function clearSearch(){
	$("#systemId").val("");
	$("#systemName").val("");
	$("#snapshotRepositoryName").val("");
	$("#project").val("");
	$("#tag").val("");
}

function clearSearchSys() {
	$("#SCsystemName").val("");
	$("#SCsystemCode").val("");
}


// 包件标记
function commitTag() {
	var data = {};
	var id = $("#artifactInfoId").val();
	if(id != ''){
		data['id'] = id;
	}else{
		data['systemId'] = $("#systemId").val();
		data['systemModuleId'] = $("#systemModuleId").val();
		data['repository'] = $("#repository").text();
		data['groupId'] = $("#group").text();
		data['artifactId'] = $("#artifactId").val();
		data['version'] = $("#version").text();
		data['nexusPath'] = $("#path").val();
	}
	data['remark'] = $("#remark").val();
	var envDoms = $("#packageSign [name=envType]:checked");
	var str = "";
	$.each(envDoms, function(index, dom) {
		str += $(dom).val() + ",";
	});
	if(str == ""){
		layer.alert("请勾选至少一个标记",{icon : 0});
		return ;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		type : "post",
		url : "/devManage/package/addFeature",
		// transformRequest : function(obj) {
		// return angular.toJson(obj);
		// },
		data : {
			'artifactInfoStr' : JSON.stringify(data),
			'tagStr' : str.substr(0, str.length - 1)
		},
		success : function(data) {
			if (data.status == 2) {
				layer.alert('包件标记失败！原因：'+data.errorMessage, {
					icon : 2,
					title : "提示信息"
				});
			} else {
				layer.alert('包件标记成功！', {
					icon : 1,
					title : "提示信息"
				});
				searchInfo();
			}
			$("#loading").css('display', 'none');
			$("#packageSign").modal('hide');
		}
	});
}


//移除开发任务
function removeFeature() {
	$("#featureP").modal("hide");
}
//下载包件
function downPackage(row){
	var paras = 'repositoryName=' + row.repository + "&nexusPath=" + row.path;
	window.location.href = "/devManage/package/downPackage?" + paras;

}

