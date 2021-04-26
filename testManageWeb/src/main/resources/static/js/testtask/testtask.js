/**
 * Created by ztt
 */
var _icon_word = "../static/images/devtask/word.png", _icon_excel = "../static/images/devtask/excel.png", _icon_text = "../static/images/devtask/text.png", _icon_pdf = "../static/images/devtask/pdf.png", _icon_img = "../static/images/devtask/img.png", _icon_other = "../static/images/devtask/other.png";
var _files = [], _editfiles = [], _handlefiles = [], _checkfiles = [], testtaskStatusList = [], deployStatusData = [], deployStatus = [];
var modalType = '', excuteUserName = '', workStatus = '';
var idArr = ['userTable', 'systemTable', 'listReq', 'comWindowTable'];
var tUserIds = '';
var contentArr = []; // 搜索条件
var filter = '';// 页面初始化参数
var favoriteContent = '';// 初始筛选器内容
var isBegain = true;
var flagNum="";
var parameterArr = {};
parameterArr.arr = window.location.href.split("?");
parameterArr.parameterArr = parameterArr.arr[1]?parameterArr.arr[1].split("&"):[];
parameterArr.obj = {};
for (var i = 0; i < parameterArr.parameterArr.length; i++) {
    var obj = parameterArr.parameterArr[i].split("=");
    parameterArr.obj[obj[0]] = obj[1];
}
$(function () {
	getFilter();
	getExsitParam();
	pageInit();
	addTitle();
	banEnterSearch();
	uploadFileList();
	getDeployStatus();
	urlParam() ;
	addCheckBox();
	tableClearSreach();// 清空状态 清空表格搜索框 暂时这么写 搜藏功能做好了要删除
	testtaskStatusList = $("#testTaskStatus").find("option");
	// 搜索展开和收起
	downOrUpButton();
	// 时间控件 配置参数信息
	__Time_Control();
	// 所有的Input标签，在输入值后出现清空的按钮
	$('input[type="text"]').parent().css("position", "relative");
	$('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
	$('input[type="text"]').bind("input propertychange change", function () {
		if ($(this).val() != "") {
			$(this).parent().children(".btn_clear").css("display", "block");
		} else {
			$(this).parent().children(".btn_clear").css("display", "none");
		}
	});
	$('.clear').parent().css("position", "relative");
	$('.clear').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
	$(".clear").change(function () {
		if ($(this).val() != "") {
			$(this).parent().children(".btn_clear").css("display", "block");
		} else {
			$(this).parent().children(".btn_clear").css("display", "none");
		}
	});

	$('.clear').bind("input propertychange", function () {
		if ($(this).val() != "") {
			$(this).parent().children(".btn_clear").css("display", "block");
		} else {
			$(this).parent().children(".btn_clear").css("display", "none");
		}
	})

	collect_handle();
	formTesting();
	$('#newtestTask').on('hide.bs.modal', function () {
		$('#add_agility_inner').hide();  // 敏态
		$('#canEditField').empty();
		$("#newform").data('bootstrapValidator').destroy();
		$('#newform').data('bootstrapValidator', null);
		oac_FormValidator();
	});
	$('#edittestTask').on('hide.bs.modal', function () {
		$('#edit_agility_inner').hide();  // 敏态
		$("#editFieldDiv").empty();
		$("#editForm").data('bootstrapValidator').destroy();
		$('#editForm').data('bootstrapValidator', null);
		oac_FormValidator();
	});
	
	$("[name=filter]").change(function(){
		var value = $(this).val();
		if(value == "1"){
			$(".newFilter").removeClass('hide');
			$(".editFilter").addClass('hide');
			$(".fieldTable").removeClass('hide');
			$("#configFilter").selectpicker('val',"");
		}else if(value == "2"){
			$(".editFilter").removeClass('hide');
			$(".newFilter").addClass('hide');
			$(".fieldTable").removeClass('hide');
		}else{
			$(".editFilter").removeClass('hide');
			$(".newFilter").addClass('hide');
			$(".fieldTable").addClass('hide');
		}
	});
	
	$('#configFilter').on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue) {
		var id = $(this).selectpicker('val');
		if(id == ""){
			return;
		}
			var inputs = $("[id*='sh_']");
			$.each(inputs,function(index,dom){
				$(dom).val("");
			});
			$("#loading").css('display', 'block');
			$.ajax({
				type: "POST",
				url: "/report/defectReport/selectDefectReportById",
				dataType: "json",
				asycn:false,
				data: {
					'menuUrl': "/testManageui/testtask/toTestTask",
					"id": id
				},
				success: function (data) {
					$("#loading").css('display', 'none');
					var content = data.favoriteContent;
					if(content != ""){
						var searchArr = JSON.parse(content);
						$.each(searchArr,function(index,value){
							$("#sh_"+value.field).val(value.value);
						});
					}
					
				}
			});
	});
	
	$('#filterType').on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue) {
		var id = $(this).selectpicker('val');
		if(id == ""){
			return;
		}
			var inputs = $("[id*='gs_']");
			$.each(inputs,function(index,dom){
				$(dom).val("");
			});
			$("#loading").css('display', 'block');
			$.ajax({
				type: "POST",
				url: "/report/defectReport/selectDefectReportById",
				dataType: "json",
				data: {
					'menuUrl': "/testManageui/testtask/toTestTask",
					"id": id
				},
				success: function (data) {
					$("#loading").css('display', 'none');
					var content = data.favoriteContent;
					var filterParam = getfilterParam(content);
					searchInfo(JSON.stringify(filterParam));
				}
			});
	});
	
	oac_FormValidator();
	addClickRow(idArr)

	// 新建敏态 关联开发任务 搜索
	fuzzy_search_radio({
		'ele': 'add_devRequirementFeature_name',
		'url': '/devManage/devtask/getAllDevTask2',
		'params': {
			featureCode: '',
			requirementFeatureStatus: '',
			manageUserIds: '',
			systemIds: '',
			rows: 10,
			page: 1,
			totalrows: 200,
			featureCode: '',
			commissioningWindowIds: '',
			requirementIds: '',
			createType: '',
			executeUserIds: '',
			reqFeaturePrioritys: '',
			sprints: '',
		},
		'name': 'featureName',
		'id': 'id',
		'top': '26px',
		'dataName': 'rows',
		'userId': $('#add_devRequirementFeatureId'),
	});

	// 编辑敏态 关联开发任务 搜索
	fuzzy_search_radio({
		'ele': 'edit_iddevRequirementFeature_name',
		'url': '/devManage/devtask/getAllDevTask2',
		'params': {
			featureCode: '',
			requirementFeatureStatus: '',
			manageUserIds: '',
			systemIds: '',
			rows: 10,
			page: 1,
			totalrows: 200,
			featureCode: '',
			commissioningWindowIds: '',
			requirementIds: '',
			createType: '',
			executeUserIds: '',
			reqFeaturePrioritys: '',
			sprints: '',
		},
		'name': 'featureName',
		'id': 'id',
		'top': '26px',
		'dataName': 'rows',
		'userId': $('#edit_iddevRequirementFeatureId'),
	});
	
	if (uifavorite){
		getCollection();
	}
	$("#newsystemName").change(function(){
    	var systemId = $("#newsystemId").val();
    	var requirementId= $("#newrequirementId").val();
    	if(systemId!=""&&requirementId!=""){
    		getFeatureBySystemAndRequirement(systemId,requirementId);
    	}
    	
    });
	 $("#newrequirementName").change(function(){
	    	var systemId = $("#newsystemId").val();
	    	var requirementId= $("#newrequirementId").val();
	    	if(systemId!=""&&requirementId!=""){
	    		getFeatureBySystemAndRequirement(systemId,requirementId);
	    	}
	    	
	    });
	 
	 $("#editrequirementName").change(function(){
	    	var systemId = $("#editsystemId").val();
	    	var requirementId= $("#editrequirementId").val();
	    	if(systemId!=""&&requirementId!=""){
	    		getFeatureBySystemAndRequirement(systemId,requirementId);
	    	}
	    });
	    $("#editsystemName").change(function(){
	    	var systemId = $("#editsystemId").val();
	    	var requirementId= $("#editrequirementId").val();
	    	if(systemId!=""&&requirementId!=""){
	    		getFeatureBySystemAndRequirement(systemId,requirementId);
	    	}
	    });
	
});
// 时间控件 配置参数信息
function __Time_Control() {
	var _timeArr_ = ["startWork", "endWork", "pptstartWork", "pptendWork", "handlePptstartWork"
		, "handlePptendWork", "estartWork", "eendWork", "epptstartWork", "epptendWork"
		, 'add_planStartDate', 'add_planEndDate', 'edit_planStartDate', 'edit_planEndDate'];
	_timeArr_.forEach(function (item) {
		$("#" + item).datetimepicker({
			minView: "month",
			format: "yyyy-mm-dd",
			autoclose: true,
			todayBtn: true,
			language: 'zh-CN',
			pickerPosition: "top-left"
		}).on('change', function () {
			$(this).parent().children(".btn_clear").css("display", "block");
		});
	})

	var _Time_ = ["sWorkStart", "sWorkEndStart", "spWorkStart", "spWorkEndStart", "PptDeployTime", "SubmitTestTime", "editPptDeployTime", "editsubmitTestTime"];
	_Time_.forEach(function (item) {
		$("#" + item).datetimepicker({
			minView: "month",
			format: "yyyy-mm-dd",
			autoclose: true,
			todayBtn: true,
			language: 'zh-CN',
			pickerPosition: "top-left"
		})
		/* dataControl(item); */
	})

	$("#handleSitstartWork").datetimepicker({
		minView: "month",
		format: "yyyy-mm-dd",
		autoclose: true,
		todayBtn: true,
		language: 'zh-CN',
		pickerPosition: "top-left"
	}).on('hide', function (e) {
		$('#handleForm').data('bootstrapValidator')
			.updateStatus('handleSitstartWork', 'NOT_VALIDATED', null)
			.validateField('handleSitstartWork');
	}).on('change', function () {
		$(this).parent().children(".btn_clear").css("display", "block");
	});
	$("#handleSitendWork").datetimepicker({
		minView: "month",
		format: "yyyy-mm-dd",
		autoclose: true,
		todayBtn: true,
		language: 'zh-CN',
		pickerPosition: "top-left"
	}).on('hide', function (e) {
		$('#handleForm').data('bootstrapValidator')
			.updateStatus('handleSitendWork', 'NOT_VALIDATED', null)
			.validateField('handleSitendWork');
	}).on('change', function () {
		$(this).parent().children(".btn_clear").css("display", "block");
	});
}
// 搜索框 收藏按钮控制 js 部分
function collect_handle() {
	$(".collection").click(function () {
		if ($(this).children("span").hasClass("fa-heart-o")) {
			id = $(".contentNav  .nav_active", parent.document).attr('val');
			var obj = {
				search: [],  // 查询搜索框数据
				table: [],  // 表格搜索框数据
			};
			// 搜索框数据 格式 {"type":"input / window / select" , "value": {"xxx1":
			// $("#xx").val(),"xxx2": $("#xx").val(),...... } }
			obj.search.push(
				{ "isCollect": true, "isData": false },// 第一条判断是否收藏
				{
					"type": "input", "value": { "testTaskCode": $("#testTaskCode").val() }, "isData":
						_is_null($("#testTaskCode"))
				},
				{ "type": "input", "value": { "featureName": $("#featureName").val() }, "isData": _is_null($("#featureName")) },
				{ "type": "window", "value": { "devManPost": $("#devManPost").val(), "devManPostName": $("#devManPostName").val() }, "isData": _is_null($("#devManPost"), $("devManPostName")) },
				{ "type": "select", "value": { "testTaskStatus": $("#testTaskStatus").val() }, "isData": _is_null($("#testTaskStatus")) },
				{ "type": "window", "value": { "planVersion": $("#planVersion").val(), "planVersionName": $("#planVersionName").val() }, "isData": _is_null($("#planVersion"), $("planVersionName")) },
				{ "type": "window", "value": { "requirementId": $("#requirementId").val(), "requirementName": $("#requirementName").val() }, "isData": _is_null($("#requirementId"), $("requirementName")) },
				{
					"type": "window", "value": { "systemId": $("#systemId").val(), "systemName": $("#systemName").val() }
					, "isData": _is_null($("#systemId"), $("systemName"))
				},
				{ "type": "select", "value": { "createTye": $("#createTye").val() }, "isData": _is_null($("#createTye")) },
				{ "type": "select", "value": { "testTaskType": $("#testTaskType").val() }, "isData": _is_null($("#testTaskType")) },
				{ "type": "window", "value": { "execteUserId": $("#execteUserId").val(), "execteUserName": $("#execteUserName").val() }, "isData": _is_null($("#execteUserId"), $("execteUserName")) }
			)
			var isResult = obj.search.some(function (item) {
				return item.isData
			})
			$("#loading").css('display', 'block');
			// 表格数据
			for (var i = 0; i < $('#colGroup .onesCol').length; i++) {
				obj.table.push({ "value": $('#colGroup .onesCol input').eq(i).attr('value'), "checked": $('#colGroup .onesCol input').eq(i).prop('checked') })
			}
			$.ajax({
				type: "post",
				url: "/system/uifavorite/addAndUpdate",
				dataType: "json",
				data: {
					'menuUrl': pageUrl,
					'favoriteContent': JSON.stringify(obj),
				},
				success: function (data) {
					if (data.status == "success") {
						$("#loading").css('display', 'none');
						$(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
						layer.alert('收藏成功!', {
							icon: 1,
						})
					}
				},
				error: function () {
					$("#loading").css('display', 'none');
					layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
				}
			});
		} else {
			$("#loading").css('display', 'block');
			id = $(".contentNav  .nav_active", parent.document).attr('val');
			var obj = {
				search: [],  // 查询搜索框数据
			};
			obj.search.push({ "isCollect": false, "isData": false }) // 是否收藏
			$.ajax({
				type: "post",
				url: "/system/uifavorite/addAndUpdate",
				dataType: "json",
				data: {
					'menuUrl': pageUrl,
					'favoriteContent': JSON.stringify(obj),
				},
				success: function (data) {
					if (data.status == "success") {
						$("#loading").css('display', 'none');
						$(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
						layer.alert('取消收藏!', {
							icon: 2,
						})
					}
				},
				error: function () {
					$("#loading").css('display', 'none');
					layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
				}
			});
		}
	})
}
// 获取收藏信息
function getCollection() {
	const _search = uifavorite.search
	if (!_search) return
	var isResult = _search.some(item => item.isData)
	if (isResult && _search[0].isCollect) {
		for (var i in _search) {
			if (_search[i].type == "select") {
				for (var key in _search[i].value) {
					$("#" + key).selectpicker('val', _search[i].value[key]).change();
				}
			} else {
				for (var key in _search[i].value) {
					$("#" + key).val(_search[i].value[key]).change();
				}
			}
		}
		var tableFlag = 0;
		for (var i in uifavorite.table) {
			$("#colGroup .onesCol input[value^=" + uifavorite.table[i].value + "]").prop("checked", uifavorite.table[i].checked);
			if (uifavorite.table[i].checked) {
				$("#list2").setGridParam().hideCol(uifavorite.table[i].value);
			} else {
				tableFlag = 1;
			}
		}
		if (tableFlag == 0) {
			$("#list2").jqGrid('setGridState', 'hidden');
		}
		$("#list2").setGridWidth($('.wode').width());
		$(".selectpicker").selectpicker('refresh');
		$(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
	} else {
		$(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
	}
}
// 下载类型判断
// function __file_type(file_type){}
// 显示不同实施状态
// function _implementation_status(status,value){}
// 获取部署状态
function getDeployStatus() {
	$.ajax({
		url: "/testManage/testtask/getDeployStatus",
		method: "post",
		dataType: "json",
		success: function (data) {
			deployStatusData = data.deployStatus;
		},
		error: function () {
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
}
var colNameArr = ['id', '任务编号', '任务摘要', '涉及系统', '关联需求', '投产窗口', '状态', '测试管理岗', '执行人','任务类型','创建方式', '部署状态', '案例', '缺陷',/* '任务类型', */'需求变更次数', '操作'];
var colIndex = ['id','featureCode','featureName','systemName','requirementCode','windowName','requirementFeatureStatus','manageUserName','executeUserName','requirementFeatureSourceStr','createTypeStr','deployStatus','testCaseNumStr','defectNumStr','changeNumberStr'];
// 表格数据加载
function pageInit() {
	/*var testTaskStatus2 = '';
	var testTaskStatus = $("#testTaskStatus").val();
	if (testTaskStatus != null && testTaskStatus != '') {
		testTaskStatus2 = testTaskStatus.join(",");
	}*/
	$("#loading").css('display', 'block');
	jQuery("#list2").jqGrid({
		url: '/testManage/testtask/getAllTestTask2',
		datatype: 'json',
		mtype: "post",
		height: 'auto',
		postData: { 
			//"manageUserIds": $("#devManPost").val(),
			//"requirementFeatureStatus": testTaskStatus2, 
			"filters":filter
		},
		// multiselect: false,
		autowidth: true,
        //显示水平滚动条
        altRows: true,
        altclass: 'differ',
        shrinkToFit:false,
        autoScroll: true,
		colNames: colNameArr,
		colModel: [
			{ name: 'id', index: 'id', hidden: true },
			{
				name: 'featureCode', width: 170, index: 'featureCode', searchoptions: { sopt: ['cn']}, formatter: function (value, grid, rows) {
					if (value == undefined) {
						value = '';
					}
					return "<a class='a_style' onclick='showtestTask(" + rows.id + ")'>" + value + "</a>";
				}
			},
			{
				name: 'featureName', index: 'featureName', searchoptions: {sopt: ['cn'] }, width: 200, formatter: function (value, grid, rows) {
					var str = '';
					if (rows.createType == 1) {// aviyy 20201103 确认字段，后端通过字典表配置
						str += "<sup class='micSup'>自建任务</sup>";
					} else if (rows.createType == 0){//提前下发
                        str += "<sup class='micSup'>提前下发</sup>";
					} else if (rows.createType == -1){//生产问题确认
                        str += "<sup class='micSup'>生产问题</sup>";
                    }
					return "<a class='a_style' onclick='showtestTask(" + rows.id + ")'>" + rows.featureName + "</a>" + str;
				}
			},
			{ name: 'systemName', index: 'systemName', searchoptions: { sopt: ['cn'] } },
			{ name: 'requirementCode', index: 'requirementCode', searchoptions: { sopt: ['cn'] } },
			{ name: 'windowName', index: 'windowName', searchoptions: { sopt: ['cn'] } },
			{
				name: 'requirementFeatureStatus', index: 'requirementFeatureStatus', searchoptions: {
					sopt: ['cn']},
					width: 100,
				formatter: function (value, grid, rows) {
					for (var i = 0, len = testtaskStatusList.length; i < len; i++) {
						if (testtaskStatusList[i].value == rows.requirementFeatureStatus) {
							value = testtaskStatusList[i].innerHTML;
							break;
						}
					}
					switch (rows.requirementFeatureStatus) {
						case '01': return "<span class='doing1'>" + value + "</span>"; break;
						case '02': return "<span class='doing2'>" + value + "</span>"; break;
						case '03': return "<span class='doing3'>" + value + "</span>"; break;
						case '00': return "<span class='doing4'>" + value + "</span>"; break;
						case '04': return "<span class='doing5'>" + value + "</span>"; break;
						case '05': return "<span class='doing6'>" + value + "</span>"; break;
						case '06': return "<span class='doing7'>" + value + "</span>"; break;
						case '07': return "<span class='doing8'>" + value + "</span>"; break;
						case '08': return "<span class='doing9'>" + value + "</span>"; break;
						case '09': return "<span class='doing10'>" + value + "</span>"; break;
						case '10': return "<span class='doing11'>" + value + "</span>"; break;
						case '11': return "<span class='doing12'>" + value + "</span>"; break;
						default: return "<span class='doing'>" + value + "</span>"; break;

					}
				}
			},
			{ name: 'manageUserName', index: 'manageUserName', searchoptions: { sopt: ['cn'] },width: 100 },
			{ name: 'executeUserName', index: 'executeUserName', searchoptions: { sopt: ['cn'] },width: 100 },
			{ name: 'requirementFeatureSourceStr', index: 'requirementFeatureSourceStr', searchoptions: { sopt: ['cn'] },width: 100 },
			{ name: 'createTypeStr', index: 'createTypeStr', searchoptions: { sopt: ['cn'] },width: 100 },
            { name:'deployStatus', index:'deployStatus', searchoptions: {
					sopt: ['cn']},width: 100
				/*
				 * name: "deployStatus", index: 'deployStatus', searchoptions: {
				 * sopt: ['cn'] }, formatter: function (value, grid, rows) { var
				 * valueName = ''; if (rows.deployStatus != undefined &&
				 * rows.deployStatus != null) { var statusids =
				 * rows.deployStatus.split(","); for (var j = 0; j <
				 * statusids.length; j++) { for (var i = 0; i <
				 * deployStatusData.length; i++) { if
				 * (deployStatusData[i].valueCode == statusids[j]) { valueName +=
				 * deployStatusData[i].valueName + "，"; } } } } return
				 * valueName.substring(0, valueName.length - 1); }
				 */
			},
			{
				name: 'testCaseNumStr', index: 'testCaseNumStr',
				width: 100,  searchoptions:{sopt:['cn']}, 
				formatter: function (value, grid, rows, state) {
					if (value == null) {
						return '';
					} else {
						var row = JSON.stringify(rows).replace(/"/g, '&quot;');
						return value;
					}
				}
			},
			{
				name: 'defectNumStr', index: 'defectNumStr',  searchoptions:{sopt:['cn']}, 
				width: 100,
				formatter: function (value, grid, rows, state) {
					if (value == null) {
						return '';
					} else {
						var row = JSON.stringify(rows).replace(/"/g, '&quot;');
						return '<a  class="a_style" onclick="showDefect(' + row + ')">' + value + '</span>'
					}
				}
			},
			// {
			// name: 'requirementFeatureSource', index:
			// 'requirementFeatureSource', search: false,
			// /*searchoptions:{sopt:['cn']},*/
			// width: 100,
			// formatter: function (value, grid, rows, state) {
			// if (value == null) {
			// return '';
			// } else {
			// for( var
			// i=0;i<document.getElementById('testTaskType').options.length ;i++
			// ){
			// if( document.getElementById('testTaskType').options[i].value ==
			// rows.requirementFeatureSource ){

			// return document.getElementById('testTaskType').options[i].text;
			// }
			// }
			// return '';

			// }
			// }
			// },
			{ name: 'changeNumberStr', index: 'changeNumberStr', width: 100,searchoptions:{sopt:['cn']},  },
			{
				name: 'opt',
				index: 'opt',
				fixed: true,
				sortable: false,
				resize: false,
				search: false,
				width: 100,
				align: "center",
				formatter: function (value, grid, rows, state) {
					var row = JSON.stringify(rows).replace(/"/g, '&quot;');
					// tUserIds = rows.tUserIds;
					if (uid == rows.manageUserId || uid == rows.executeUserId || rows.tUserIds.indexOf(uid) != -1 || uid == rows.createBy) {

						var str = "";
						var str2 = '';
						var opt_status = [];
						if (editPermission == true) {
							str = '<a class="a_style" onclick="edit(' + row + ')">编辑</a> ';
							opt_status.push(str);
						}
						if (rows.requirementFeatureStatus != "00" && rows.requirementFeatureStatus != "08"
							&& rows.requirementFeatureStatus != "09" && rows.requirementFeatureStatus != "10") {
							var s = '';
							if (str != '') {
								s = " | ";
							}
							var transferStr = '';
							if (transferPermission == true) {
								transferStr = '<a class="a_style" onclick="split(' + row + ',2)">转派</a>  '
								opt_status.push('<a class="a_style" onclick="split(' + row + ',2)">转派</a>');
							}
							var s2 = '';
							if (transferStr != '') {
								s2 = ' | ';
							}
							var splitStr = '';
							if (splitPermission == true) {
								splitStr = s2 + '<a class="a_style" onclick="split(' + row + ',1)">拆分</a> '
								opt_status.push('<a class="a_style" onclick="split(' + row + ',1)">拆分</a>');
							}
							var s3 = '';
							if (splitStr != '') {
								s3 = ' | ';
							}
							var handleStr = '';
							if (handleEditPermission == true) {
								handleStr = s3 + ' <a class="a_style" onclick="handle(' + row + ')">处理</a>'
								opt_status.push('<a class="a_style" onclick="handle(' + row + ')">处理</a>');
							}
							var s4 = '';
							if (deployPermission == true) {
								s4 = ' | <a class="a_style" onclick="sureDeploy(' + row + ')">部署确认</a>';
								opt_status.push('<a class="a_style" onclick="sureDeploy(' + row + ')">部署确认</a>');
							}
							var s5 = '';
							if (mergePermission == true && rows.flag == true) {
								s5 = ' | <a class="a_style" onclick="merge(' + row + ')">合并</a>';
								opt_status.push('<a class="a_style" onclick="merge(' + row + ')">合并</a>');
							}
							// str2 = s + transferStr + splitStr + handleStr +
							// s4 + s5;
						}
						// return str + str2;
						var opt_str = opt_status.join('');
						return `
							<li role="presentation" class="dropdown">
								<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
								<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
							</li>
						`;
					} else {
						return '';
					}
				}
			}
		],
		rowNum: 10,
		rowTotal: 200,
		rowList: [10, 30, 50, 100],
		rownumWidth: 40,
		pager: '#pager2',
		sortable: true,   // 是否可排序
		sortorder: 'desc',
		sortname: 'id',
		viewrecords: true, // 是否要显示总记录数
		beforeRequest: function () {
			$("#loading").css('display', 'block');
			var inputs = $("[id*=gs_]");
        	var searchNum=0;
        	$.each(inputs,function(index,dom){
        		var field = $(dom).attr("id").split("_")[1];
        		if($(dom).val()==""){
        			searchNum=searchNum+1;
        		}
        	});
        	
        	if(searchNum==15){
        		if(flagNum!=2){
        			layer.alert("请填写搜索条件!", { icon: 0 });
            		$("#loading").css('display', 'none');
          	 		return false;
          	 		flagNum=1;
        		}else{
        			flagNum=1;
        		}
        		
        	}
		},
		gridComplete: function () {
			$("[data-toggle='tooltip']").tooltip();
			getSearchValue();
			
		},
		loadComplete: function (data) {
			$("#loading").css('display', 'none');
			if(data.status == "2"){
				layer.alert(data.message,{icon:0});
			}
				addTableTitle();
				if(isBegain){
					getfilterParam(favoriteContent);
					isBegain = false;
				}
				getSearchValue();
			// $('#cb_list2').hide();
		},
		loadError: function (xhr,status) {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 2 });
		}
	}).trigger("reloadGrid");
	$("#list2").jqGrid('filterToolbar', { searchOperators: true });
}
//自定义筛选器 重置
function clearConfigFilter(){
	var inputs = $("[id*=sh_]");   //获取所有筛选器输入框
	$.each(inputs,function(index,dom){
		$(dom).val("");
	})
}

//自定筛选器 搜索
function configFilterSearch(){
	//根据筛选器弹框页面输入内容 赋值给列表表格搜索条件
	var content = [];
	var isAllNull = true;
	var inputs = $("[id*=sh_]");   //获取所有筛选器输入框
	$.each(inputs,function(index,dom){
		var id = $(dom).attr("id");
		var value = $(dom).val();
		var contentJson = {};
		contentJson.field = id.split('_')[1];
		contentJson.value = value;
		$("#gs_"+id.split('_')[1]).val('');
		if(value != ''){
			content.push(contentJson);
			$("#gs_"+id.split('_')[1]).val(value);
			isAllNull = false;
		}
	});
	if(isAllNull){
		layer.alert("搜索条件不能全部为空！", { icon: 0 });
		return;
	}
	content = JSON.stringify(content);
	var filterParam = getfilterParam(content);
	filterParam = JSON.stringify(filterParam);
	searchInfo(filterParam);
	$("#configFilterModal").modal('hide');
}
// 根据筛选器内容 组装 搜索条件filter
function getfilterParam(content){
	var filterParam = {};
	filterParam.groupOp = "AND";
	var rules = [];
	if(content != ""){
		var searchArr = JSON.parse(content);
		$.each(searchArr,function(index,value){
			var searchJson = {};
			searchJson.field = value.field;
			searchJson.op = "cn";
			searchJson.data = value.value;
			rules.push(searchJson);
			$("#gs_"+value.field).val(value.value);
		});
	}
	filterParam.rules = rules;
	return filterParam;
}
/*从其它页面跳转过来的*/
function getExsitParam(){
	var filterParam = {};
	filterParam.groupOp = "AND";
	var rules = [];
	var content = [];
	if(existSystemName != "$systemName"){
		var searchJson = {};
		searchJson.field = "systemName";
		searchJson.op = "cn";
		searchJson.data = existSystemName;
		rules.push(searchJson);
		$("#gs_systemName").val(existSystemName);
		var contentJson = {field:"systemName",value:existSystemName};
		content.push(contentJson);
	}
	if(existWindowDate != "$windowDate"){
		existWindowDate = replaceSearchDate(existWindowDate);
		var searchJson = {};
		searchJson.field = "windowName";
		searchJson.op = "cn";
		searchJson.data = existWindowDate;
		rules.push(searchJson);
		$("#gs_windowName").val(existWindowDate);
		var contentJson = {field:"windowName",value:existWindowDate};
		content.push(contentJson);
	}
	if(existSystemName != "$systemName" || existWindowDate != "$windowDate"){
		$('#filterType').selectpicker('val',"");
		$('#filterType').selectpicker("refresh");
		filterParam.rules = rules;
		filter = JSON.stringify(filterParam);
		favoriteContent = JSON.stringify(content);
	}
}

function replaceSearchDate(ele){
	if(ele.indexOf("&lt;=") != -1 || ele.indexOf("&lt;") != -1
			|| ele.indexOf("&gt;=") != -1 || ele.indexOf("&gt;") != -1) {
		 ele = ele.replace("&lt;=", "<=");
		 ele = ele.replace("&lt;", "<");
		 ele = ele.replace("&gt;=", ">=");
		 ele = ele.replace("&gt;", ">");
	}
	return ele;
}

function getSearchValue(){
	var condition = "";
	for(var i = 1;i < colIndex.length;i++){
		var searchVal = $("#gs_"+colIndex[i]).val();
		if(searchVal != ""){
			condition += "["+colNameArr[i]+":"+searchVal+"]"+" ";
		}
	}
	if(condition != ""){
		$("#condition").text("筛选条件："+condition);
	}else{
		$("#condition").text("");
	}
}

function getFilter(){
	$("#loading").css('display', 'block');
	$("[name=filterType] option[value!='']").remove();
	$.ajax({
		type: "POST",
		url: "/report/defectReport/selectDefectReportList",
		dataType: "json",
		async:false,
		data: {
			"menuUrl": "/testManageui/testtask/toTestTask"
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			$.each(data,function(index,value){
				if(value.filterName!=null){
					// 先创建好select里面的option元素
					var option = document.createElement("option");
					// 转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值
					$(option).val(value.id);
					// 给option的text赋值,这就是你点开下拉框能够看到的东西
					$(option).text(value.filterName);
					// 获取select 下拉框对象,并将option添加进select
					$('[name=filterType]').append(option);
					if(value.lastUseFlag==1){
						$('#filterType').selectpicker('val',value.id);
						var filterParam = getfilterParam(value.favoriteContent);
						favoriteContent = value.favoriteContent;
						filter = JSON.stringify(filterParam);
					}
					/*if(index == data.length-1 && isBegain){   // 最新筛选器且是第一次进页面
						$('#filterType').selectpicker('val',value.id);
						var filterParam = getfilterParam(value.favoriteContent);
						favoriteContent = value.favoriteContent;
						filter = JSON.stringify(filterParam);
					}*/
				}
			});
			$('[name=filterType]').selectpicker('refresh');
		}
	});
}
//自定义筛选器 弹框
function configFilterShow(){
	var inputs = $("[id*=gs_]");
	$.each(inputs,function(index,dom){
		var field = $(dom).attr("id").split("_")[1];
		$("#sh_"+field).val($(dom).val());
	});
	$("#configFilterModal").modal('show');
}
//自定义筛选器 保存提交
function configFilterCommit(){
	var type = $("[name=filter]:checked").val();
	var filterValue = $("#configFilter").selectpicker("val");
	var filterName = $("#configFilter").find("option:selected").text();
	var inputs = $("[id*=sh_]");
	var isAllNull = true;   //判断是否所有内容都为空
	var filterArr = [];
	$.each(inputs,function(index,dom){
		var contentJson = {};
		var id = $(dom).attr('id');
		var value = $(dom).val();
		contentJson.field = id.split("_")[1];
		contentJson.value = value;
		if(value != ""){
			filterArr.push(contentJson);
			isAllNull = false;
		}
	});
	$("#loading").css('display', 'block');
	if(type == "2"){//修改
		if(filterValue == ""){
			layer.alert("未选择筛选器！", { icon: 0 });
			$("#loading").css('display', 'none');
			return;
		}
		if(isAllNull){
			layer.alert("搜索条件不能全部为空！", { icon: 0 });
			$("#loading").css('display', 'none');
			return;
		}
		$.ajax({
			type: "POST",
			url: "/report/defectReport/updateInfo",
			dataType: "json",
			data: {
				"id":filterValue,
				"filterName": filterName,
				"favoriteContent": JSON.stringify(filterArr)
			},
			success: function (data) {
				$("#loading").css('display', 'none');
				if(data.code = 1){
					layer.alert("修改成功！", { icon: 1 });
				}else{
					layer.alert("修改失败！", { icon: 2 });
				}
			}
		});
	}else if(type == "1"){//新增
		var filterName = $("#filterName").val();
		var flag = jugeFilterName(filterName);
		if(!flag){
			$("#loading").css('display', 'none');
			return;
		}
		if(isAllNull){
			layer.alert("搜索条件不能全部为空！", { icon: 0 });
			$("#loading").css('display', 'none');
			return;
		}
		$.ajax({
			type: "POST",
			url: "/report/defectReport/collectCondition",
			dataType: "json",
			data: {
				"filterName": filterName,
				"defectReport": JSON.stringify(filterArr),
				"menuUrl": "/testManageui/testtask/toTestTask"
			},
			success: function (data) {
				$("#loading").css('display', 'none');
				if(data.code = 1){
					layer.alert("添加成功！", { icon: 1 });
					getFilter();
					$("#configFilterModal").modal('hide');
				}else{
					layer.alert("添加失败！", { icon: 2 });
				}
			}
		});
	}else{//删除
		$.ajax({
			type: "POST",
			url: "/report/defectReport/updateDefectReport",
			dataType: "json",
			data: {
				"id": filterValue
			},
			success: function (data) {
				$("#loading").css('display', 'none');
				if(data > 0){
					getFilter();
					layer.alert("删除成功！", { icon: 1 });
				}else{
					layer.alert("删除失败！", { icon: 2 });
				}
			}
		});
	}
}

// 判断过虑器名称
function jugeFilterName(filterName){
	var options = $("#configFilter").find("option");
	var isRepeat = false; 
	if(filterName == ""){
		layer.alert("筛选器名称为空",{icon:0});
		return false;
	}
	$.each(options,function(index,dom){     // 判断筛选器重复
		var text = $(dom).text();
		if(text == filterName){
			isRepeat = true;
			return false;
		}
	});
	if(isRepeat){
		layer.alert("筛选器名称重复",{icon:0});
		return false;
	}
	return true;
}

function clearSearch(){
	var inputs = $("[id*=gs_]");
	$.each(inputs,function(index,dom){
		$(dom).val("");
	})
}

// 部署确认弹框
function sureDeploy(row) {
	$.ajax({
		type: "POST",
		url: "/testManage/testtask/findDeployByReqFeatureId1",
		dataType: "json",
		data: {
			"featureId": row.id
		},
		success: function (data1) {
			var deployStatus = data1["deployArr"];;
			var statusArr = [];
			if (deployStatus != undefined && deployStatus != null) {
				var statusArr = deployStatus.split(",");
			}
			$("#dReqFeatureId").val('');
			$("#dReqFeatureId").val(row.id);
			$("#deployDiv").empty();
			var str = '';
			outer: for (var i = 0; i < deployStatusData.length; i++) {
				for (var j = 0; j < statusArr.length; j++) {
					var flag = '';
					if (deployStatusData[i].valueCode == statusArr[j]) {
						flag = "checked";
						str += `
					<div class="rowdiv">
						<input type="checkbox" checked="${flag}" name="deployStatus" value="${deployStatusData[i].valueCode}" />
						&nbsp; &nbsp;${deployStatusData[i].valueName}
					</div>
				`;
						continue outer;
					}
				}
				str += `
		<div class="rowdiv">
			<input type="checkbox" name="deployStatus" value="${deployStatusData[i].valueCode}"/> 
			&nbsp;  &nbsp;${deployStatusData[i].valueName}
		</div>`;
			}
			$("#deployDiv").append(str);
			$("#sureDeploy").modal("show");
		}
	})
}
// 部署确认提交
function deployCommit() {
	var reqFeatureId = $("#dReqFeatureId").val();
	var s = '';
	$('input[name="deployStatus"]:checked').each(function () {
		s += $(this).val() + ",";
	});
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/sureDeploy",
		dataType: "json",
		type: "post",
		data: {
			deployStatus: s,
			reqFeatureId: reqFeatureId
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			if (data.status == 'success') {
				$("#sureDeploy").modal("hide");
				layer.alert("修改成功！", { icon: 1 });
				flagNum=2;
				pageInit();
			} else if (data.status == '2') {
				$("#sureDeploy").modal("hide");
				layer.alert("修改失败！", { icon: 2 });
			}
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
}
// 跳转到测试集列表
// function showTestSet(row) {
// var curl = testSetMenuUrl.split("?")[0] + "?testTaskId=" +
// htmlEncodeJQ(row.id);
// var sessionStorage = window.sessionStorage;
// sessionStorage.setItem("testSetFlag", "false");
// window.parent.toPageAndValue(testSetMenuName, testSetMenuId, curl);
// }

// 跳转到缺陷列表页面
function showDefect(row) {
	var reqFId = row.id;
	var reqFCode = row.featureCode;
	var curl = defectMenuUrl.split("?")[0] + "?reqFiD=" + htmlEncodeJQ(reqFId) + "&reqFName=" + htmlEncodeJQ(reqFCode) + "&reqStatus=not 撤销";
	window.parent.toPageAndValue("测试缺陷管理", defectMenuId, curl);
}
// 合并
function merge(row) {
	// var id = row.id;
	var requirementId = row.requirementId;
	var systemId = row.systemId;
	var commissioningWindowId = row.commissioningWindowId;
	layer.alert('确认合并此任务？', {
		icon: 3,
		btn: ['确定', '取消']
	}, function () {
		$.ajax({
			url: '/testManage/testtask/mergeTestTask',
			method: "post",
			data: {
				systemId: systemId,
				requirementId: requirementId,
				commissioningWindowId: commissioningWindowId
			},
			success: function (data) {
				if (data.status == "success") {
					layer.alert('合并成功！', { icon: 1 });
					flagNum=2;
					pageInit();

				} else if (data.status == "2") {
					layer.alert('合并失败！！！', { icon: 2 });
				}
			},
			error: function () {
				$("#loading").css('display', 'none');
				layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
			}
		});
	})
}


// 查询信息
function searchInfo(param) {
	/*var testTaskStatus2 = '';
	var testTaskStatus = $("#testTaskStatus").val();
	if (testTaskStatus != null && testTaskStatus != '') {
		testTaskStatus2 = testTaskStatus.join(",");
	}
	var testTaskType2 = '';
	var testTaskType = $("#testTaskType").val();
	if (testTaskType != null && testTaskType != '') {
		testTaskType2 = testTaskType.join(",");
	}
	var planVersion = $("#planVersion").val();*/
	var gridParam = {
			url: "/testManage/testtask/getAllTestTask2",
			loadComplete: function () {
				$("#loading").css('display', 'none');
			}
		};
	if(param != undefined){
		gridParam.postData = {filters:param};
	}
	$("#loading").css('display', 'block');
	$("#list2").jqGrid('setGridParam', gridParam).trigger("reloadGrid"); // 重新载入
}

// 查看 实施状态
function show_status(rows) {
	for (var i = 0, len = testtaskStatusList.length; i < len; i++) {
		if (testtaskStatusList[i].value == rows) {
			value = testtaskStatusList[i].innerHTML;
			break;
		}
	}
	switch (rows) {
		case '01': return "<span class='doing1'>" + value + "</span>"; break;
		case '02': return "<span class='doing2'>" + value + "</span>"; break;
		case '03': return "<span class='doing3'>" + value + "</span>"; break;
		case '00': return "<span class='doing4'>" + value + "</span>"; break;
		case '04': return "<span class='doing5'>" + value + "</span>"; break;
		case '05': return "<span class='doing6'>" + value + "</span>"; break;
		case '06': return "<span class='doing7'>" + value + "</span>"; break;
		case '07': return "<span class='doing8'>" + value + "</span>"; break;
		case '08': return "<span class='doing9'>" + value + "</span>"; break;
		case '09': return "<span class='doing10'>" + value + "</span>"; break;
		case '10': return "<span class='doing11'>" + value + "</span>"; break;
		case '11': return "<span class='doing12'>" + value + "</span>"; break;
		default: return "<span class='doing'>" + value + "</span>"; break;
	}
}

// 查看
function showtestTask(id) {
	var id = id;
	layer.open({
		type: 2,
		title: false,
		closeBtn: 0,
		shadeClose: true,
		shade: false,
		move: false,
		area: ['100%', '100%'],
		// id: "1",
		tipsMore: true,
		anim: 2,
		content: '/testManageui/testtask/toTestTaskInfo?id=' + id,
		btn: ['导出','关闭'],
		btnAlign: 'c', // 按钮居中
		yes: function(){
			exportWord(id);
		},
		no: function () {
			layer.closeAll();
		}
	});
}
// 提交备注
function saveRemark() {

	if ($("#tyaskRemark").val() == '' && $("#checkfiles").val() == '') {
		layer.alert('备注内容和附件不能同时为空！！！', { icon: 0 });
		return;
	}
	/*
	 * var _fileStr = []; for(var i=0;i<_checkfiles.length;i++){
	 * _fileStr.push(_checkfiles[i].fileNameOld); } var s =
	 * _fileStr.join(",")+","; for(var i=0;i<_fileStr.length;i++){
	 * if(s.replace(_fileStr[i]+",","").indexOf(_fileStr[i]+",")>-1){
	 * layer.alert(_fileStr[i]+"文件重复！！！", {icon: 0}); return ; } }
	 */
	var remark = $("#tyaskRemark").val();
	var id = $("#checkReqFeatureId").val();
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/addRemark",
		dataType: "json",
		type: "post",
		data: {
			id: id,
			requirementFeatureRemark: remark,
			attachFiles: $("#checkfiles").val()
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			if (data.status == "success") {
				layer.alert('保存成功！', { icon: 1 });
				// showtestTask(id);
				initPage(id);
				$("#checkfiles").val('');
			} else if (data.status == "2") {
				layer.alert('保存失败！！！', { icon: 2 });
			}
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	});
}
// 转派提交
function transferCommit() {
	if ($("#transferId").val() == '') {
		layer.alert("转派人不能为空！！！", { icon: 0 });
		return;
	}

	if ($("#transferId").val() == $("#sEcecuteUserId").val()) {
		layer.alert("不可以转派给同一个人！！！", { icon: 0 });
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/transfer",
		dataType: "json",
		traditional: true,
		type: "post",
		data: {
			id: $("#splittestTaskId").val(),
			executeUserId: $("#transferId").val(),
			transferRemark: $("#transferRemark").val(),
			"tUserIds": tUserIds,
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			if (data.status == "noPermission") {
				layer.alert('您没有操作权限！！！', { icon: 0 });
			}
			if (data.status == "success") {
				layer.alert('转派成功！', { icon: 1 });
				flagNum=2;
				pageInit();
				$("#splittestTask").modal("hide");
			} else if (data.status == "2") {
				layer.alert('转派失败！！！', { icon: 2 });
			}
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
}

function showField(data) {
	$("#checkEditField").empty();
	if (data != null && data != "undefined" && data.length > 0) {
		for (var i = 0; i < data.length; i++) {
			var str = '<div class="form-group col-md-6">' +
				'<label class="control-label fontWeihgt">' + data[i].label + '：</label>' +
				'<label class="control-label font_left">' + data[i].valueName + '</label>' +
				'</div>';
			$("#checkEditField").append(str);
		}
	}
}
// 拆分提交
function splitCommit() {
	var reqFeatureId = $("#splittestTaskId").val();
	var testTaskName = $("#sWorkSummary").val();
	var testTaskOverview = $("#sWorkOverView").val();

	var planStartDate = '';
	var planEndDate = '';
	var planWorkload = '';
	// if($("#testStage").val()==1){
	// planStartDate = $("#sWorkStart").val();
	// planEndDate = $("#sWorkEndStart").val();
	// planWorkload = $("#sWorkPlanWorkload").val();
	// }else if(($("#testStage").val()==2)){
	// planStartDate = $("#spWorkStart").val();
	// planEndDate = $("#spWorkEndStart").val();
	// planWorkload = $("#spWorkPlanWorkload").val();
	// }
	var devUserId = $("#sWorkDivid").val();
	var commissioningWindowId = $("#splitCommitWindowId").val();
	var requirementFeatureStatus = $("#splitStatus").val();
	var testStage = $("#testStage").val() || 0;
	var taskAssignUserId = $("#sWorkAssignUserId").val();
	var environmentType = $("#environmentType").val();
	$('#splitForm').data('bootstrapValidator').validate();
	if (!$('#splitForm').data('bootstrapValidator').isValid()) {
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/splitTestTask",
		type: "post",
		traditional: true,
		dataType: "json",
		data: {
			"id": reqFeatureId,
			"testTaskName": testTaskName,
			"testTaskOverview": testTaskOverview,
			// "planWorkload":planWorkload,
			"testUserId": devUserId,
			// "startDate":planStartDate,
			// "endDate":planEndDate,
			"commissioningWindowId": commissioningWindowId,
			"requirementFeatureStatus": requirementFeatureStatus,
			"testStage": testStage,
			"taskAssignUserId": taskAssignUserId,
			"environmentType": environmentType,
			"tUserIds": tUserIds,
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			if (data.status == "noPermission") {
				layer.alert('您没有操作权限！！！', { icon: 0 });
			}
			if (data.status == "success") {
				layer.alert('拆分成功！', { icon: 1 });
				flagNum=2;
				pageInit();
				$("#splittestTask").modal("hide");
			} else if (data.status == "2") {
				layer.alert('拆分失败！！！', { icon: 2 });
			}
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
}

// 同需求
function sameReq() {
	var summary = $("#splitSummary").text();
	var workname = '';
	if ((summary.indexOf('|')) == -1) {
		workname = summary.slice(0);
	} else {
		workname = summary.slice(summary.indexOf('|') + 2);// 正常是+1
															// +2是因为|后还有个空格
	}
	$("#sWorkSummary").val(workname).change();
	$("#sWorkOverView").val($("#sOverView").text()).change();
}

// 拆分/转派弹框
function split(row, type) {
	$('#splittestTask').on('hide.bs.modal', function () {
		$('#splitForm').bootstrapValidator('resetForm');
	});
	var id = row.id;
	var systemId = row.systemId;
	tUserIds = row.tUserIds;
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/tosplit",
		dataType: "json",
		type: "post",
		data: {
			"id": id,
			"systemId": systemId
		},
		success: function (data) {
			
			$("#rowSitId").hide();
			$("#rowPptId").hide();
			$("#sSystemId").text('');
			$("#sDeptId").text('');
			$("#sRequirementId").text('');
			$("#sWorkSummary").val("");
			$("#sWorkOverView").val('');
			$("#sWorkPlanWorkload").val('');
			$('#sWorkStart').val('');
			$('#sWorkEndStart').val('');
			$("#spWorkStart").val('');
			$("#spWorkEndStart").val('');
			$("#spWorkPlanWorkload").val('');
			$("#transferRemark").val('');
			$("#transferSystemId").val('');
			$("#transferSystemId").val(systemId);
			$("#transferId").val('');
			$("#sEcecuteUserId").val('');
			excuteUserName = '';
			excuteUserName = data.executeUserId;
			// $("#transferId").val(data.executeUserId);
			$("#transferUserName").val('');
			$("#sWorkDividUserName").val('');
			$("#sWorkAssignUserId").val('');
			$("#sWorkAssignUserName").val('');
			$("#sChange").empty();
			$("#sRequirementFeatureSource").empty();
			$("#sImportantRequirementType").empty();
			$("#sPptDeployTime").empty();
			$("#sSubmitTestTime").empty();

			$("#splittestTaskId").val(data.id);
			$("#splitCommitWindowId").val(data.commissioningWindowId);
			$("#splitStatus").val(data.requirementFeatureStatus);
			$("#splitSummary").text(toStr(data.featureCode) + " | " + data.featureName);
			$("#sOverView").text(data.featureOverview);
			var statusName = '';
			for (var i = 0; i < testtaskStatusList.length; i++) {
				if (testtaskStatusList[i].value == data.requirementFeatureStatus) {
					statusName = testtaskStatusList[i].innerHTML;
				}
			}
			$("#stestTaskStatus").text(statusName);
			$("#sManageUser").text(data.manageUserName);
			$("#sEcecuteUserId").val(data.executeUserId);
			$("#sEcecuteUser").text(data.executeUserName);
			$("#sSystemId").text(data.systemName);
			$("#sRequirementId").text(data.requirementCode /*
															 * + " | "
															 * +data.requirementName
															 */);
			$("#sDeptId").text(data.deptName);
			$("#sChange").text(data.requirementChangeNumber);
			$("#sRequirementFeatureSource").text(data.featureSource);
			var importantRequirement = "";
			if (data.importantRequirementType == '1') {
				importantRequirement = "是"
			} else if (data.importantRequirementType == '2') {
				importantRequirement = "否"
			}
			$("#sImportantRequirementType").text(importantRequirement);
			$("#sPptDeployTime").text(timestampToTime(data.pptDeployTime));
			$("#sSubmitTestTime").text(timestampToTime(data.submitTestTime));

			$("#testStage").change(function () {
				if ($("#testStage").val() == 1) {
					$("#sWorkEndStart").unbind("change");
					$("#sWorkStart").unbind("change");
					$("#sWorkPlanWorkload").unbind("change");

					$("#rowSitId").show();
					$("#rowPptId").hide();
					$('#sWorkStart').trigger('change');
					$('#sWorkEndStart').trigger('change');
					$("#sWorkStart").val(data.planSitStartDate);
					$("#sWorkEndStart").val(data.planSitEndDate);
					$("#sWorkPlanWorkload").val(data.estimateSitWorkload);
					$("#sWorkStart").bind("change", function () {
						$(this).parent().children(".btn_clear").css("display", "block");
						if ($("#sWorkStart").val() < data.planSitStartDate) {
							layer.alert("您选择的日期早于当前开发任务的系测预计开始时间！", { icon: 0 });
						}
					});
					$("#sWorkEndStart").bind("change", function () {
						$(this).parent().children(".btn_clear").css("display", "block");
						if ($("#sWorkEndStart").val() > data.planSitEndDate) {
							layer.alert("您选择的日期晚于当前开发任务的系测预计结束时间！", { icon: 0 });
						}
					});
					$("#sWorkPlanWorkload").change(function () {
						if ($("#sWorkPlanWorkload").val() > data.estimateSitWorkload) {
							layer.alert("您输入的预计工作量大于当前开发任务的预计工作量！", { icon: 0 });
						}
					});
				} else if ($("#testStage").val() == 2) {
					$("#spWorkEndStart").unbind("change");
					$("#spWorkStart").unbind("change");
					$("#spWorkPlanWorkload").unbind("change");
					$("#rowPptId").show();
					$("#rowSitId").hide();
					$('#spWorkStart').trigger('change');
					$('#spWorkEndStart').trigger('change');
					$("#spWorkStart").val(data.planPptStartDate);
					$("#spWorkEndStart").val(data.planPptEndDate);
					$("#spWorkPlanWorkload").val(data.estimatePptWorkload);
					$("#spWorkStart").bind("change", function () {
						$(this).parent().children(".btn_clear").css("display", "block");
						if ($("#spWorkStart").val() < data.planPptStartDate) {
							layer.alert("您选择的日期早于当前开发任务的版测预计开始时间！", { icon: 0 });
						}
					});
					$("#spWorkEndStart").bind("change", function () {
						$(this).parent().children(".btn_clear").css("display", "block");
						if ($("#spWorkEndStart").val() > data.planPptEndDate) {
							layer.alert("您选择的日期晚于当前开发任务的版测预计结束时间！", { icon: 0 });
						}
					});
					$("#spWorkPlanWorkload").change(function () {
						if ($("#spWorkPlanWorkload").val() > data.estimatePptWorkload) {
							layer.alert("您输入的预计工作量大于当前开发任务的预计工作量！", { icon: 0 });
						}
					});
				}
			});
			var a = '';
			if (data.temporaryStatus == 1) {
				a = "是";
			}
			if (data.temporaryStatus == 2) {
				a = "否";
			}
			$("#sTemporaryTask").text(a);

			$("#testStage").empty();
			$("#testStage").append('<option value="">请选择</option>');
			for (var i = 0; i < data.dataDics.length; i++) {
				var id = data.dataDics[i].valueCode;
				var name = data.dataDics[i].valueName;
				var opt = "<option value='" + id + "'>" + name + "</option>";
				$("#testStage").append(opt);
			}
			$("#environmentType").empty();
			$("#environmentType").append('<option value="">请选择</option>');
			if (data.environmentType != undefined && data.environmentType != null) {
				var arr = data.environmentType.split(",");
				for (var i = 0; i < arr.length; i++) {
					for (var j = 0; j < environmentTypeList.length; j++) {
						if (arr[i] == environmentTypeList[j][0]) {
							var opt = "<option value='" + environmentTypeList[j][0] + "'>" + environmentTypeList[j][1] + "</option>";
							$("#environmentType").append(opt);
							break;
						}
					}
				}
			}


			if (data.projectType == 2) {
				$('.is_new_project').hide();
				$("#splitForm").bootstrapValidator("removeField","testStage");
			} else {
				$('.is_new_project').show();
				$("#splitForm").bootstrapValidator("addField","testStage",{
					 validators: {
						 notEmpty: {
							 message: '测试阶段不能为空'
						 },
					 }
				})
			}
			$('.selectpicker').selectpicker('refresh');
			$("#loading").css('display', 'none');
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}

	})
	$("#splittestTask").modal("show");
	if (type == '1') {
		$("#titleName").text("工作任务拆分");
		$("#splitFooter").show();
		$("#transferFooter").hide();
		$("#footer1").hide();
		$("#footer2").show();

	} else if (type == '2') {
		$("#titleName").text("测试任务转派");
		$("#splitFooter").hide();
		$("#transferFooter").show();
		$("#footer1").show();
		$("#footer2").hide();
	}
}
// 编辑提交
function edittestTask() {

	$('#editForm').data('bootstrapValidator').validate();
	if (!$('#editForm').data('bootstrapValidator').isValid()) {
		return;
	}
	/*
	 * if(!$("#editcommitWindowId").val()){
	 * layer.alert('请选择投产窗口！',{title:'提示信息',icon:2}) return; }
	 */

	var id = $("#edittestTaskId").val();
	var featureName = $("#efeatureName").val();
	var featureOverview = $("#efeatureOverview").val();
	var systemId = '';
	if ($("#editsystemName").val() != '') {
		systemId = $("#editsystemId").val();
	}
	var deptId = $("#editdeptId").val();
	var requirementId = '';
	if ($("#editrequirementName").val() != '') {
		requirementId = $("#editrequirementId").val();
	}
	var executeUserId = $("#editexecuteUser").val();
	var manageUserId = $("#edittestManageUser").val();
	var commissioningWindowId = $("#editcommitWindowId").val();
	var requirementFeatureSource = $("#editrequirementFeatureSource").val();
	var questionNumber = $("#editquestionNumber").val();
	var actualSitStartDate = $("#estartWork").val();
	var actualSitEndDate = $("#eendWork").val();
	/* var estimateSitWorkload= $("#enestimateWorkload").val(); */
	var actualPptStartDate = $("#epptstartWork").val();
	var actualPptEndDate = $("#epptendWork").val();
	/* var estimatePptWorkload = $("#epptWorkload").val(); */

	var fieldTemplate = getFieldData("editFieldDiv");
	for (var i = 0; i < fieldTemplate.field.length; i++) {
		if (fieldTemplate.field[i].required == "false") {
			if (fieldTemplate.field[i].valueName == "" || fieldTemplate.field[i].valueName == null || fieldTemplate.field[i].valueName == undefined) {
				$("#loading").css('display', 'none');
				layer.alert(fieldTemplate.field[i].labelName + "不能为空", {
					icon: 2,
					title: "提示信息"
				});
				return;
			}
		}
	}
	var developmentDeptNumber = $("#editDevelopmentDept").val();
	/*
	 * if($("#editrequirementName").val()=='' ||
	 * $("#editrequirementName").val()==null){ developmentDeptNumber =
	 * $("#editDevelopmentDept").val(); }else{ developmentDeptNumber =
	 * $("#editDevelopmentDeptNumber2").val(); }
	 */
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/updateTestTask",
		dataType: "json",
		traditional: true,
		type: "post",
		data: {
			"id": id,
			"featureName": featureName,
			"featureOverview": featureOverview,
			"systemId": systemId,
			"deptId": deptId,
			"requirementId": requirementId,
			"requirementCode": $("#editrequirementName").val(),
			"executeUserId": executeUserId,
			"manageUserId": manageUserId,
			"pstartDate": actualSitStartDate,
			"pendDate": actualSitEndDate,
			// "estimateSitWorkload":estimateSitWorkload,
			"astartDate": actualPptStartDate,
			"aendDate": actualPptEndDate,
			// "estimatePptWorkload": estimatePptWorkload,
			"commissioningWindowId": commissioningWindowId,
			"requirementFeatureStatus": $("#edittestTaskStatus").val(),
			"attachFiles": $("#editfiles").val(),

			"createType": $("#createType2").val(),
			"requirementFeatureSource": $("#editFeature").val(),
			"requirementChangeNumber": $("#editChange").val(),
			"importantRequirementType": $("#editimportantRequirementType").val(),
			"pptDeployTime": $("#editPptDeployTime").val(),
			"submitTestTime": $("#editsubmitTestTime").val(),
			"tUserIds": tUserIds,
			'fieldTemplate': JSON.stringify(fieldTemplate),
			'developmentDeptNumber': developmentDeptNumber,

			devRequirementFeatureId: $('#edit_iddevRequirementFeatureId').val(),
			estimateWorkload: $.trim($('#edit_estimateWorkload').val()),
			estimateRemainWorkload: $.trim($('#edit_estimateRemainWorkload').val()),
			actualWorkload: $.trim($('#edit_actualWorkload').val()),
			"projectId": $("#editProjectId").val(),
			"projectPlanId": $("#edit_project_plan_id").val(),
			"planStartDate1": $("#edit_planStartDate").val(),
			"planEndDate1": $("#edit_planEndDate").val(),
			// "actualSitWorkload": $("#editSitWorkload").val(),
			// "actualPptWorkload": $("#editPptWorkload").val(),
			// 扩展字段
			// "editExtendField1" : $("#editExtendField1").val(),
			// "editExtendField2" : $("#editExtendField2").val(),
			// "editExtendField3" : $("#editExtendField3").val(),
			// "editExtendField4" : $("#editExtendField4").val(),
			// "editExtendField5" : $("#editExtendField5").val(),
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			if (data.status == "noPermission") {
				layer.alert('您没有操作权限！！！', { icon: 0 });
			}
			if (data.status == "success") {
				layer.alert('编辑成功！', { icon: 1 });
				flagNum=2;
				pageInit();
				$("#edittestTask").modal("hide");
			} else if (data.status == "2") {
				layer.alert('编辑失败！！！', { icon: 2 });
			} else if (data.status == "repeat") {
				layer.alert('该任务名称已存在！！！', { icon: 0 });
			}
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	});
}

// 编辑弹出框
function edit(row) {
	$('.is_new_project').show();
	$('#edit_agility_inner').hide();
	search_radio_clear('edit_iddevRequirementFeature_name', $('#edit_iddevRequirementFeatureId'));
	search_radio_clear('editProjectName', $('#editProjectId'));
	$('#edit_estimateWorkload').val('');
	$('#edit_estimateRemainWorkload').val('');
	$('#edit_actualWorkload').val('');
	//getDept();
	var id = row.id;
	tUserIds = row.tUserIds;
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/getOneTestTask3",
		dataType: "json",
		type: "post",
		data: {
			"id": id
		},
		success: function (data) {
			if (data.status == "2") {
				layer.alert("数据加载失败！", { icon: 2 });
			}
			$("#editProjectId").val(data.projectId);
			$("#editProjectName").val(data.projectName);
			$("#editProjectName").attr('username',data.projectName);
			
			$("#edit_project_plan_id").val(data.projectPlanId);
			$("#edit_project_plan_name").val(data.planName);

			$("#editfiles").val();
			$("#editsystemId").val('');
			$("#editsystemName").val('');
			$("#editexecuteUser").val('');
			$("#editsystemId").val('');
			$("#editrequirementName").val('');
			$("#edittestTaskStatus").empty();
			$("#editrequirementFeatureSource").empty();
			$("#editrequirementFeatureSource").append('<option value="">请选择</option>');
			$("#edittestManageUser").empty();
			$("#edittestManageUser").append('<option value="">请选择</option>');
			$("#editexecuteUser").val('');
			$("#editexecuteUserName").val('');
			$("#edittestManageUser").val('');
			$("#edittestManageUserName").val('');
			$("#editreqCode").val('');
			$("#editdeptId").val('');
			$("#editDeptName").val('');
			$("#editcommitWindowId").val("");
			$("#editcommitWindowName").val("");
			$("#editrequirementId").val('');
			$("#editFileTable").empty();
			$("#editDevelopmentDept").val('');

			$("#edit_planStartDate").val("");
			$("#edit_planEndDate").val("");

			if (data.field != undefined) {
				for (var i = 0; i < data.field.length; i++) {
					appendDataType(data.field[i], 'editFieldDiv', 'edit');
				}
			}

			$("#edit_planStartDate").val(data.planStartDate);
			$("#edit_planEndDate").val(data.planEndDate);

			$("#edittestTaskId").val(data.id);
			$("#efeatureName").val(isValueNull(data.featureName));
			$("#efeatureOverview").val(isValueNull(data.featureOverview));
			$("#epstartWork").val(data.planStartDate);
			$("#ependWork").val(data.planEndDate);
			$("#eestimateWorkload").val(data.planWorkload);
			//$("#editdeptId").val(data.deptId);
			getDept(data.deptId);
			$("#editDeptName").val(isValueNull(data.deptName)).change();

			$("#estartWork").val(data.actualSitStartDate);
			$("#eendWork").val(data.actualSitEndDate);

			$("#epptstartWork").val(data.actualPptStartDate);

			$("#epptendWork").val(data.actualPptEndDate);

			$("#editexecuteUser").val(data.executeUserId);
			$("#editsystemId").val(data.systemId);
			$("#editrequirementId").val(data.requirementId);
			$("#edittestManageUser").val(data.manageUserId);

			$("#edittestManageUserName").val(data.manageUserName);
			$("#edittestManageUserName").attr('username',data.manageUserName);
			$("#editexecuteUserName").val(data.executeUserName);
			$("#editexecuteUserName").attr('username',data.executeUserName);
			$("#editsystemName").val(data.systemName);
			if (data.developmentMode == 1) {// 敏态
				search_radio_give('edit_iddevRequirementFeature_name', data.devRequirementFeatureName, $('#edit_iddevRequirementFeatureId'), data.devRequirementFeatureId);
				$('#edit_estimateWorkload').val(data.estimateWorkload);
				$('#edit_estimateRemainWorkload').val(data.estimateRemainWorkload);
				$('#edit_actualWorkload').val(data.actualWorkload);
			}

			$("#editrequirementName").val(data.requirementCode);//.change()
			$("#editcommitWindowId").val(data.commissioningWindowId);
			$("#editcommitWindowName").val(data.windowName);
			$("#edittestTaskStatus").empty();
			$("#edittestTaskStatus").append("<option value=''>请选择</option>");
			$("#createType2").val(data.createType);
			$("#editChange").val('');
			$("#editChange").val(data.requirementChangeNumber);
			$("#editFeature").val('');
			$("#editFeature").val(data.requirementFeatureSource);
//			$("#editFeature option").each(function (i, n) {
//				if (n.value == data.requirementFeatureSource) {
//					n.selected = true;
//				}
//			});
			$("#editimportantRequirementType").val('');
			$("#editimportantRequirementType").val(data.importantRequirementType);
			$("#editPptDeployTime").val('');
			$("#editPptDeployTime").val(timestampToTime(data.pptDeployTime));
			$("#editsubmitTestTime").val('');
			$("#editsubmitTestTime").val(timestampToTime(data.submitTestTime));

			if (data.createType == "1") {
				$("#editCreateFile").show();
				$("#editFeature").removeAttr("disabled");
			} else {
				$("#editFeature").attr("disabled", true);
				$("#editCreateFile").hide();
			}
			$("#editDevelopmentDept").empty();
			$("#editDevelopmentDept").append(_opt_html);
			if (data.requirementDeptNumber != undefined && data.requirementDeptName != undefined) {
				if (numbers.includes(data.requirementDeptNumber) == false) {
					$("#editDevelopmentDept").append("<option selected value='" + data.requirementDeptNumber + "'>" + data.requirementDeptName + "</option>");
					$("#editDevelopmentDept").selectpicker('refresh');
				} else {
					$("#editDevelopmentDept").val(data.requirementDeptNumber);
				}
			}
			if (data.projectType == 2) {   // 新建项目
				clear_new_option();
				$('.is_new_project').hide();
				$('.new_project_option').show();
			} else {
				$('.is_new_project').show();
				$('.new_project_option').hide();
			}


			for (var i = 0; i < testtaskStatusList.length; i++) {
				if (testtaskStatusList[i].value != "") {
					var flag = '';
					if (data.requirementFeatureStatus == testtaskStatusList[i].value) {
						flag = "selected";
					}

					$("#edittestTaskStatus").append('<option ' + flag + ' value="' + testtaskStatusList[i].value + '">' + testtaskStatusList[i].innerHTML + '</option>');
				}
			}
			// 附件
			_editfiles = [];
			if (data.attachements != undefined) {
				var _table = $("#editFileTable");
				for (var i = 0; i < data.attachements.length; i++) {
					var _tr = '';
					var file_name = data.attachements[i].fileNameOld;
					var file_type = data.attachements[i].fileType;
					var file_id = data.attachements[i].id;
					var _td_icon;
					var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + data.attachements[i].fileS3Bucket + '</i><i class = "file-s3Key">' + data.attachements[i].fileS3Key + '</i></td>';
					var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
					switch (file_type) {
						case "doc":
						case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
						case "xls":
						case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
						case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
						case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
						case "png":
						case "jpeg":
						case "jpg": _td_icon = '<img src="' + _icon_img + '"/>'; break;
						default: _td_icon = '<img src="' + _icon_other + '" />'; break;
					}
					_tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download2(this)">' + _td_icon + " " + _td_name + _td_opt + '</tr>';
					_table.append(_tr);
					_editfiles.push(data.attachements[i]);
					$("#editfiles").val(JSON.stringify(_editfiles));
				}
			}

			$('.selectpicker').selectpicker('refresh');
			$("#loading").css('display', 'none');
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
	modalType = 'edit';
	$("#edittestTask").modal("show");

}

function timestampToTime(timestamp) {
	if (timestamp == null) {
		return;
	}
	var date = new Date(timestamp);// 时间戳为10位需*1000，时间戳为13位的话不需乘1000
	var Y = date.getFullYear() + '-';
	var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
	var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
	return Y + M + D;
}

$(function () {
	var loseInputMillsecs = 500;
	var clocker = null;
	// 编辑页面部门
	$("#editDeptName").on('input propertychange', function () {
		innerKeydown();
	})
	function innerKeydown() {
		if (null == clocker) {
			clocker = setTimeout(loadData, loseInputMillsecs);
		} else {		// 连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData, loseInputMillsecs);
		}
	}

	function loadData() {
		if ($("#editDeptName").val().trim() == '') {
			$('.selectBox').hide()
			return;
		}
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: { 'deptName': $("#editDeptName").val().trim() },
			dataType: "json",
			success: function (data) {
				var depts = data.depts;
				if (depts.length > 0) {
					var itemStr = ''
					for (var i = 0; i < depts.length; i++) {
						itemStr += '<li class="selectItem" id="' + depts[i].id + '">' + depts[i].deptName + '</li>'
					}
					$('.selectUl').html(itemStr)
					$('.selectBox').show()
				} else {
					$('.selectBox').hide()
				}
			},
			error: function () {
				$("#loading").css('display', 'none');
				layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
			}
		})
		clocker = null;
	}

	/*$("#selectBox").on('click', '.selectItem', function () {
		$("#editDeptName").val($(this).html());
		$("#editdeptId").val($(this).attr("id"));
		$("#selectBox").hide()
	})
*/
	$(document).click(function () {
		$("#selectBox").hide()
	})

	// 新增页面部门
	$("#newDeptName").on('input propertychange', function () {
		innerKeydown2();
	})

	function innerKeydown2() {
		if (null == clocker) {
			clocker = setTimeout(loadData2, loseInputMillsecs);
		} else {		// 连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData2, loseInputMillsecs);
		}
	}

	function loadData2() {
		if ($("#newDeptName").val().trim() == '') {
			$('.selectBox').hide()
			return;
		}
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: { 'deptName': $("#newDeptName").val().trim() },
			dataType: "json",
			success: function (data) {
				var depts = data.depts;
				if (depts.length > 0) {
					var itemStr = ''
					for (var i = 0; i < depts.length; i++) {
						itemStr += '<li class="selectItem" id="' + depts[i].id + '">' + depts[i].deptName + '</li>'
					}
					$('.selectUl').html(itemStr)
					$('.selectBox').show()
				} else {
					$('.selectBox').hide()
				}
			},
			error: function () {
				$("#loading").css('display', 'none');
				layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
			}

		})
		clocker = null;
	}

	/*$("#newselectBox").on('click', '.selectItem', function () {
		$("#newDeptName").val($(this).html());
		$("#newdeptId").val($(this).attr("id"));
		$("#newselectBox").hide()
	})*/

	$(document).click(function () {
		$("#newselectBox").hide()
	})

});


// 处理提交
function handletestTask() {
	var id = $("#reqFeatureId").val();
	var actualSitstartWork = $("#handleSitstartWork").val();
	var actualSitendWork = $("#handleSitendWork").val();
	var actualSitWorkload = $("#handleSitWorkload").val();

	var actualPptstartWork = $("#handlePptstartWork").val();
	var actualPptendWork = $("#handlePptendWork").val();
	var actualPptWorkload = $("#handlPptWorkload").val();
	$('#handleForm').data('bootstrapValidator').validate();
	if (!$('#handleForm').data('bootstrapValidator').isValid()) {

		return;
	}
	/*
	 * var _fileStr = []; for(var i=0;i<_handlefiles.length;i++){
	 * _fileStr.push(_handlefiles[i].fileNameOld); } var s =
	 * _fileStr.join(",")+","; for(var i=0;i<_fileStr.length;i++){
	 * if(s.replace(_fileStr[i]+",","").indexOf(_fileStr[i]+",")>-1){
	 * layer.alert(_fileStr[i]+"文件重复！！！", {icon: 0});
	 * 
	 * return; } }
	 */
	if (workStatus == "false") {
		layer.confirm('该任务下尚有未实施完成的工作任务，是否要确定处理完成？', {
			btn: ['确定', '取消'],
			icon: 3
		}, function (index, layero) {
			$("#loading").css('display', 'block');
			$.ajax({
				url: "/testManage/testtask/handleTestTask",
				type: "post",
				traditional: true,
				dataType: "json",
				data: {
					"id": id,
					"startDate": actualSitstartWork,
					"endDate": actualSitendWork,
					"pptstartDate": actualPptstartWork,
					"pptendDate": actualPptendWork,
					"actualSitWorkload": actualSitWorkload,
					"actualPptWorkload": actualPptWorkload,
					"sitTestCaseAmount": $("#sitTestCaseNum").val(),
					"pptTestCaseAmount": $("#pptTestCaseNum").val(),
					"attachFiles": $("#handlefiles").val(),
					"tUserIds": tUserIds,

				},
				success: function (data) {
					$("#loading").css('display', 'none');
					if (data.status == "noPermission") {
						layer.alert('您没有操作权限！！！', { icon: 0 });
					}
					if (data.status == "success") {
						layer.alert('处理成功！', { icon: 1 });
						flagNum=2;
						pageInit();
						$("#handletestTask").modal("hide");
					} else if (data.status == "2") {
						layer.alert('处理失败！！！', { icon: 2 });
					}
				},
				error: function () {
					$("#loading").css('display', 'none');
					layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
				}
			});
		}, function (index) {
			$('#handleDevTask').on('hide.bs.modal', function () {
				$('#handleForm').bootstrapValidator('resetForm');
			});
		});
	} else {
		$("#loading").css('display', 'block');
		$.ajax({
			url: "/testManage/testtask/handleTestTask",
			type: "post",
			dataType: "json",
			data: {
				"id": id,
				"startDate": actualSitstartWork,
				"endDate": actualSitendWork,
				"pptstartDate": actualPptstartWork,
				"pptendDate": actualPptendWork,
				"actualSitWorkload": actualSitWorkload,
				"actualPptWorkload": actualPptWorkload,
				"sitTestCaseAmount": $("#sitTestCaseNum").val(),
				"pptTestCaseAmount": $("#pptTestCaseNum").val(),
				"attachFiles": $("#handlefiles").val()

			},
			success: function (data) {
				$("#loading").css('display', 'none');
				if (data.status == "success") {
					layer.alert('处理成功！', { icon: 1 });
					flagNum=2;
					pageInit();
					$("#handletestTask").modal("hide");
				} else if (data.status == "2") {
					layer.alert('处理失败！！！', { icon: 2 });
				}
			},
			error: function () {
				$("#loading").css('display', 'none');
				layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
			}
		});

	}
}
// 处理弹出框
function handle(row) {
	$('#handletestTask').on('hide.bs.modal', function () {
		$('#handleForm').bootstrapValidator('resetForm');
	});
	var id = row.id;
	tUserIds = row.tUserIds;
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/getOneTestTask",
		type: "post",
		dataType: "json",
		data: {
			"id": id
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			$("#handlefiles").val("");
			$("#handleFileTable").empty();
			$("#handleConnectDiv").empty();
			$("#handleBrotheDiv").empty();
			$("#handledeptName").text('');
			$("#handlepreStartDate").text('');
			$("#handlepreEndDate").text('');
			$("#handlepreWorkload").text('');
			$("#handlehandSug").text('');
			$("#handlesystemName").text('');
			$("#handlerequirement").text('');
			$("#handlePptstartWork").val("");
			$("#handlePptendWork").val("");
			$("#handlPptWorkload").val("");
			$("#handleSitstartWork").val("");
			$("#handleSitendWork").val("");
			$("#handleSitWorkload").val("");
			$("#reqFeatureId").val(data.id);
			$("#handletestTaskTitle").text(toStr(data.featureCode) + " | " + data.featureName);
			$("#handletestTaskOverview").text(data.featureOverview);
			var statusName = '';
			for (var i = 0; i < testtaskStatusList.length; i++) {
				if (testtaskStatusList[i].value == data.requirementFeatureStatus) {
					statusName = testtaskStatusList[i].innerHTML;
				}
			}
			$("#handletestTaskStatus").text(statusName);
			$("#handledevManPost").text(data.manageUserName);
			$("#handleexecutor").text(data.executeUserName);
			$("#handlesystemName").text(data.systemName);
			$("#handlerequirement").text(data.requirementCode/*
																 * +" |
																 * "+data.requirementName
																 */);
			$("#handledeptName").text(data.deptName);

			$("#handleSitstartWork").val(data.actualSitStartDate == undefined ? data.actSitStart : data.actualSitStartDate);
			$("#handleSitendWork").val(data.actualSitEndDate == undefined ? data.actSitEnd : data.actualSitEndDate);
			$("#handleSitWorkload").val(data.actualSitWorkload == undefined ? data.actSitWork : data.actualSitWorkload);
			$("#sitTestCaseNum").val(data.sitTestCaseNum);
			$("#sitDefectNum").val(data.sitDefectNum);
			$("#handlePptstartWork").val(data.actualPptStartDate == undefined ? data.actPptStart : data.actualPptStartDate);
			$("#handlePptendWork").val(data.actualPptEndDate == undefined ? data.actPptEnd : data.actualPptEndDate);
			$("#handlPptWorkload").val(data.actualPptWorkload == undefined ? data.actPptWork : data.actualPptWorkload);
			$("#pptTestCaseNum").val(data.pptTestCaseNum);
			$("#pptDefectNum").val(data.pptDefectNum);

			var status = "";
			if (data.temporaryStatus == "1") {
				status = "是";
			} else if (data.temporaryStatus == "2") {
				status = "否";
			}
			$("#handletemporaryTask").text(status);// 1是2否
			/*
			 * $("#handlepreStartDate").text(data.planStartDate);
			 * $("#handlepreEndDate").text(data.planEndDate);
			 * $("#handlepreWorkload").text(data.planWorkload);
			 */
			// $("#handlepreSitStartDate").text('');
			// $("#handlepreSitStartDate").text(data.planSitStartDate);
			// $("#handlepreSitEndDate").text('');
			// $("#handlepreSitEndDate").text(data.planSitEndDate);
			// $("#handlepreSitWorkload").text('');
			// $("#handlepreSitWorkload").text(data.estimateSitWorkload);

			// $("#handleprePptStartDate").text(data.planPptStartDate);
			// $("#handleprePptEndDate").text('');
			// $("#handleprePptEndDate").text(data.planPptEndDate);
			// $("#handleprePptWorkload").text('');
			// $("#handleprePptWorkload").text(data.estimatePptWorkload);


			$("#handlehandSug").text(data.handleSuggestion);
			workStatus = "";
			if (data.list.length > 0) {
				for (var i = 0; i < data.list.length; i++) {
					if (data.list[i].testTaskStatus != 3 && data.list[i].testTaskStatus != 0) {
						workStatus = "false";
						break;
					}
				}
				for (var i = 0; i < data.list.length; i++) {
					$("#handleConnectDiv").append('<div class="rowdiv"><div class="form-group col-md-2"><label class=" control-label font_left" ><a class="a_style" onclick="getSee(' + data.list[i].id + ')">' + data.list[i].testTaskCode + '</a></label></div><div class="form-group col-md-2"><label class=" control-label font_left" >' + data.list[i].testTaskName + '</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >' + data.list[i].testTaskStatusName + '</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >' + toStr(data.list[i].testUserName) + '</label></div><div class="form-group col-md-3"><label class=" control-label font_left" >' + toStr(data.list[i].testStageName) + ' : ' + toStr(data.list[i].actualStartDate) + '~' + toStr(data.list[i].actualEndDate) + '</label></div><div class="form-group col-md-1"><label class=" control-label font_left" >' + toStr(data.list[i].actualWorkload) + '人天</label></div></div>');
				}
			}
			// 附件
			_handlefiles = [];
			if (data.attachements != undefined) {
				var _table = $("#handleFileTable");
				for (var i = 0; i < data.attachements.length; i++) {
					var _tr = '';
					var file_name = data.attachements[i].fileNameOld;
					var file_type = data.attachements[i].fileType;
					var file_id = data.attachements[i].id;
					var _td_icon;
					// <i class="file-url">'+data.attachements[i].filePath+'</i>
					var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + data.attachements[i].fileS3Bucket + '</i><i class = "file-s3Key">' + data.attachements[i].fileS3Key + '</i></td>';
					var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
					switch (file_type) {
						case "doc":
						case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
						case "xls":
						case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
						case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
						case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
						case "png":
						case "jpeg":
						case "jpg": _td_icon = '<img src="' + _icon_img + '"/>'; break;
						default: _td_icon = '<img src="' + _icon_other + '" />'; break;
					}
					_tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download2(this)">' + _td_icon + " " + _td_name + _td_opt + '</tr>';
					_table.append(_tr);
					_handlefiles.push(data.attachements[i]);
					$("#handlefiles").val(JSON.stringify(_handlefiles));
				}
			}
			/*
			 * // 相关工作任务 if(data.brother!=undefined){ for(var i=0;i<data.brother.length;i++){
			 * if(data.brother[i].featureCode==undefined){
			 * data.brother[i].featureCode=""; } $("#handleBrotheDiv").append( '<div
			 * class="rowdiv"><div class="form-group col-md-2"><label class="
			 * control-label font_left" >'+data.brother[i].featureCode+'</label></div><div
			 * class="form-group col-md-3"><label class=" control-label
			 * font_left" >'+data.brother[i].featureName+'</label></div><div
			 * class="form-group col-md-2"><label class=" control-label
			 * font_left" >'+data.brother[i].statusName+'</label></div><div
			 * class="form-group col-md-2"><label class=" control-label
			 * font_left" >'+data.brother[i].executeUserName+'</label></div><div
			 * class="form-group col-md-3"><label class="col-sm-6 control-label
			 * fontWeihgt">预排期：</label><label class="col-sm-6 control-label
			 * font_left fontWeihgt" >'+data.brother[i].windowName+'</label></div></div>'); } }
			 */
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	});
	modalType = 'handle';
	$("#handletestTask").modal("show");

}
// 新增提交
function addCommit() {
	$('#newform').data('bootstrapValidator').validate();
	if (!$('#newform').data('bootstrapValidator').isValid()) {
		return;
	}
	var fieldTemplate = getFieldData("canEditField");
	for (var i = 0; i < fieldTemplate.field.length; i++) {
		if (fieldTemplate.field[i].required == "false") {
			if (fieldTemplate.field[i].valueName == "" || fieldTemplate.field[i].valueName == null || fieldTemplate.field[i].valueName == undefined) {
				$("#loading").css('display', 'none');
				layer.alert(fieldTemplate.field[i].labelName + "不能为空", {
					icon: 2,
					title: "提示信息"
				});
				return;
			}
		}
	}
	var developmentDeptNumber = $("#developmentDept").val();
	var featureName = $("#nfeatureName").val();
	var featureOverview = $("#nfeatureOverview").val();
	var manageUserId = $("#newtestManageUser").val();
	var executeUserId = $("#newexecuteUser").val();
	var systemId = $("#newsystemId").val();
	var requirementId = $("#newrequirementId").val();
	var deptId = $("#newdeptId").val();

	var commissioningWindowId = $("#newcommitWindowId").val();

	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/addTestTask",
		dataType: "json",
		type: "post",
		data: {
			"featureName": featureName,
			"featureOverview": featureOverview,
			"manageUserId": manageUserId,
			"executeUserId": executeUserId,
			"systemId": systemId,
			"requirementId": requirementId,
			"requirementCode": $("#newrequirementName").val(),

			"deptId": deptId,
			"commissioningWindowId": commissioningWindowId,

			"requirementChangeNumber": $("#change").val(),
			"requirementFeatureSource": $("#featureSource").val(),
			"pptDeployTime": $("#PptDeployTime").val(),
			"submitTestTime": $("#SubmitTestTime").val(),
			"importantRequirementType": $("#importantRequirementType").val(),
			"attachFiles": $("#files").val(),
			'fieldTemplate': JSON.stringify(fieldTemplate),
			'developmentDeptNumber': developmentDeptNumber,

			devRequirementFeatureId: $('#add_devRequirementFeatureId').val(),
			estimateWorkload: $.trim($('#add_estimateWorkload').val()),
			estimateRemainWorkload: $.trim($('#add_estimateRemainWorkload').val()),
			actualWorkload: $.trim($('#add_actualWorkload').val()),
			"projectId": $("#newProjectId").val(),
			"projectPlanId": $("#new_project_plan_id").val(),
			"planStartDate1": $("#add_planStartDate").val(),
			"planEndDate1": $("#add_planEndDate").val(),
			// "estimateSitWorkload" : estimateWorkload,
			// "pptendWork":$("#pptendWork").val(),
			// "pptstartWork":$("#pptstartWork").val(),
			// "estimatePptWorkload":$("#pptWorkload").val(),
			// "requirementFeatureSource":requirementFeatureSource,
			// 扩展字段
			// "extendField1" : $("#extendField1").val(),
			// "extendField2" : $("#extendField2").val(),
			// "extendField3" : $("#extendField3").val(),
			// "extendField4" : $("#extendField4").val(),
			// "extendField5" : $("#extendField5").val(),
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			if (data.status == "success") {
				layer.alert('保存成功！', { icon: 1 });
				flagNum=2;
				$("#newtestTask").modal("hide");
				$(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
				$(".ui-search-toolbar .ui-search-input select").val('0');
				$(".ui-search-toolbar .ui-search-input  .btn_clear").css("display", "none");
				var field=[{"field":"featureCode","value":data.feature.featureCode}];
                var filterParam =getfilterParam(JSON.stringify(field));
                $("#list2").jqGrid("setGridParam", { postData: {
                    "filters":JSON.stringify(filterParam)
                }}).trigger("reloadGrid");
                
				
			} else if (data.status == "2") {
				layer.alert('保存失败！！！', { icon: 2 });
			} else if (data.status == "repeat") {
				layer.alert('该任务名称已存在！！！', { icon: 0 });
			}
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	});
}

// 新增弹框
function newtestTask_btn() {
	$('#new_plan_body').hide();
	$('#edit_plan_body').hide();
	$("#new_project_plan_id").val('');
	$("#new_project_plan_name").val('');
	
	$('.is_new_project').show();
	search_radio_clear('add_devRequirementFeature_name', $('#add_devRequirementFeatureId'));
	search_radio_clear('newProjectName', $('#newProjectId'));
	$('#add_estimateWorkload').val('');
	$('#add_estimateRemainWorkload').val('');
	$('#add_actualWorkload').val('');
	$("#nfeatureName").val("");// 任务名称
	$("#nfeatureOverview").val("");// 任务描述
	$("#newtestManageUserName").val('');// 管理岗
	$("#newexecuteUserName").val('');// 执行人
	$("#newsystemName").val('');// 系统
	$("#newrequirementName").val('');// 需求
	$("#newDeptName").val('');// 所需处室
	$("#newcommitWindowName").val('');// 投产窗口
	$("#featureSource").selectpicker('val', '');// 任务类型
	$("#change").val('0'); // 需求变更次数
	$("#SubmitTestTime").val('');// 提交测试日期
	$("#PptDeployTime").val('');// 发布测试如期
	$("#importantRequirementType").prop('selectedIndex', 0);// 是否监管需求
	$("#newFileTable").empty(); // 附件


	$("#newtestManageUser").val('');//管理岗id
	$("#newexecuteUser").val('');//执行人id
	$("#newsystemId").val('');//系统id
	$("#newrequirementId").val('');//需求id
	$("#newdeptId").selectpicker('val','');  //所需处室id
	$("#newcommitWindowId").val('');//投产窗口id
	$("#developmentDept").val('');//开发处室
	//	$("#developmentDeptNumber2").val('');//开发处室
	//	$("#developmentDept2").val('');//开发处室

	$("#startWork").val("");
	$("#endWork").val("");
	$("#pptstartWork").val('');
	$("#pptendWork").val('');

	$("#nestimateWorkload").val("");
	$("#newquestionNumber").val("");
	$("#pptWorkload").val('');

	$("#add_planStartDate").val("");
	$("#add_planEndDate").val("");

	$("#development2").css("display", "none");
	$("#development").css("display", "block");
	getDept();
	
	modalType = 'new';
	$("#newtestTask").modal("show");
	_files = [];
	// 添加自定义字段
	addField();
	$('.selectpicker').selectpicker('refresh');
}
// 导出
function exportTask_btn() {
	 var obj = {};
	    
	 var inputs = $("[id*=gs_]");
	 $.each(inputs,function(index,dom){
		 var field = $(dom).attr("id").split("_")[1];
		 obj[field]=$(dom).val();
	 });
	    
	obj.sidx = "id";
	obj.sord = "desc";

	var obj1 = JSON.stringify(obj);
	window.location.href = "/zuul/testManage/testtask/exportExcel?reqFeatue=" + encodeURIComponent(obj1, 'utf-8');

	// $("#searchForm").attr("action","/zuul/testManage/testtask/exportExcel");
	// $("#searchForm").submit();
}

function exportWord(id){
	window.location.href = "/zuul/testManage/testtask/exportWord?id=" + id;
}

// 清空表格筛选
function tableClearSreach() {
	$(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
	$(".ui-search-toolbar .ui-search-input select").val('0');
}
// 显示/隐藏列
function addCheckBox() {
	$("#colGroup").empty();
	var str = "";
	str = '<div class="onesCol"><input type="checkbox" value="featureCode" onclick="showHideCol( this )" /><span>任务编号</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="featureName" onclick="showHideCol( this )" /><span>任务摘要</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="systemName" onclick="showHideCol( this )" /><span>涉及系统</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="requirementCode" onclick="showHideCol( this )" /><span>关联需求</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="windowName" onclick="showHideCol( this )" /><span>投产窗口</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="requirementFeatureStatus" onclick="showHideCol( this )" /><span>状态</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="manageUserName" onclick="showHideCol( this )" /><span>测试管理岗</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="executeUserName" onclick="showHideCol( this )" /><span>执行人</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="requirementFeatureSourceStr" onclick="showHideCol( this )" /><span>任务类型</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="createTypeStr" onclick="showHideCol( this )" /><span>创建方式</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="deployStatus" onclick="showHideCol( this )" /><span>部署状态</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="testCaseNumStr" onclick="showHideCol( this )" /><span>案例</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="defectNumStr" onclick="showHideCol( this )" /><span>缺陷</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="changeNumberStr" onclick="showHideCol( this )" /><span>需求变更次数</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="opt" onclick="showHideCol( this )" /><span>操作</span></div>';
	$("#colGroup").append(str)
}
function showHideCol(This) {
	var colModel = $("#list2").jqGrid('getGridParam', 'colModel');
	var width = 0;// 获取当前列的列宽
	var arr = [];
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

// 重置
function clearSearch() {
	$('#testTaskCode').val("");
	$("#featureName").val("");
	$('#devManPost').val("");
	$('#devManPostName').val('');
	$('#execteUserName').val('');
	$('#execteUserId').val('');
	$("#systemName").val('');
	$("#systemId").val('');
	$("#testTaskStatus").selectpicker('val', '');
	$("#testTaskType").selectpicker('val', '');
	$("#planVersion").val('');
	$("#planVersionName").val('');
	$("#requirementId").val('');
	$("#requirementName").val('');
	$("#systemId").val('');
	$("#createTye").selectpicker('val', '');
	$(".color1 .btn_clear").css("display", "none");
}
// 清空表格内容
function clearContent(that) {
	$(that).parent().children('input').val("").change();
	$(that).parent().children(".btn_clear").css("display", "none");
}


function down(This) {
	if ($(This).hasClass("fa-angle-double-down")) {
		$(This).removeClass("fa-angle-double-down");
		$(This).addClass("fa-angle-double-up");
		$(This).parents('.allInfo').children(".def_content").slideDown(100);
		$(This).parents('.allInfo').children(".connect_div").slideDown(100);
		if (!$(This).prev().hasClass("def_changeTit")) {
			$(This).parents('.allInfo').children("#handleLogs").slideDown(100);
		}
	} else {
		$(This).addClass("fa-angle-double-down");
		$(This).removeClass("fa-angle-double-up");
		$(This).parents('.allInfo').children(".def_content").slideUp(100);
		$(This).parents('.allInfo').children(".connect_div").slideUp(100);
		$(This).parents('.allInfo').children("#handleLogs").slideUp(100);
	}
}
// 文件上传，并列表展示
function uploadFileList() {
	// 列表展示
	$(".upload-files").change(function () {
		var files = this.files;
		var formFile = new FormData();

		/*
		 * if(!fileAcceptBrowser()){ for(var i=0,len=files.length;i<len;i++){
		 * var file_type = files[i].name.split(".")[1];
		 * if(file_type!="doc"&&file_type!="docx"&&file_type!="xls"&&file_type!="xlsx"&&file_type!="txt"&&file_type!="pdf"){
		 * layer.alert('上传文件格式错误! 请上传后缀名为.doc，.docx，.xls，.xlsx，.txt，.pdf的文件',
		 * {icon:0}); return false; } } }
		 */

		outer: for (var i = 0, len = files.length; i < len; i++) {
			var file = files[i];
			if (file.size <= 0) {
				layer.alert(file.name + "文件为空！", { icon: 0 });
				continue;
			}
			var fileList = [];
			if (modalType == 'new') {
				fileList = _files;
			} else if (modalType == 'edit') {
				fileList = _editfiles;
			} else if (modalType == 'check') {
				fileList = _checkfiles;
			}

			for (var j = 0; j < fileList.length; j++) {
				if (fileList[j].fileNameOld == file.name) {
					layer.alert(file.name + "文件已存在！！！", { icon: 0 });
					continue outer;
				}
			}
			formFile.append("files", file);
			// 读取文件
			if (window.FileReader) {
				var reader = new FileReader();
				reader.readAsDataURL(file);
				reader.onerror = function (e) {
					layer.alert("文件" + file.name + " 读取出现错误", { icon: 0 });
					return false;
				};
				reader.onload = function (e) {
					if (e.target.result) {
					}
				};
			}

			// 列表展示
			var _tr = '';
			var file_name = file.name.split("\.")[0];
			var file_type = file.name.split("\.")[1];
			var _td_icon;
			var _td_name = '<span >' + file.name + '</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
			var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
			switch (file_type) {
				case "doc":
				case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
				case "xls":
				case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
				case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
				case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
				case "png":
				case "jpeg":
				case "jpg": _td_icon = '<img src="' + _icon_img + '"/>'; break;
				default: _td_icon = '<img src="' + _icon_other + '" />'; break;
			}
			var _table = $(this).parent(".file-upload-select").next(".file-upload-list").children("table");
			_tr += '<tr><td><div class="fileTb">' + _td_icon + '  ' + _td_name + _td_opt + '</tr>';
			_table.append(_tr);

		}
		// 上传文件
		$("#loading", window.parent.document).css('display', 'block');
		$.ajax({
			type: 'post',
			url: '/zuul/testManage/testtask/uploadFile',
			contentType: false,
			processData: false,
			dataType: "json",
			data: formFile,
			success: function (data) {
				for (var k = 0, len = data.length; k < len; k++) {
					if (modalType == 'new') {
						_files.push(data[k]);
					} else if (modalType == 'edit') {
						_editfiles.push(data[k]);
					} else if (modalType == 'handle') {
						_handlefiles.push(data[k]);
					} else if (modalType == 'check') {
						_checkfiles.push(data[k]);
					}
					$(".file-upload-tb span").each(function (o) {
						if ($(this).text() == data[k].fileNameOld) {
							// $(this).parent().children(".file-url").html(data[k].filePath);
							$(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
							$(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
						}
					});
				}
				if (modalType == 'new') {
					$("#files").val(JSON.stringify(_files));
					clearUploadFile('uploadFile');
				} else if (modalType == 'edit') {
					$("#editfiles").val(JSON.stringify(_editfiles));
					clearUploadFile('edituploadFile');
				} else if (modalType == 'handle') {
					$("#handlefiles").val(JSON.stringify(_handlefiles));
					clearUploadFile('handleuploadFile');
				} else if (modalType == 'check') {
					$("#checkfiles").val(JSON.stringify(_checkfiles));
					clearUploadFile('checkuploadFile');
				}
				$("#loading", window.parent.document).css('display', 'none');
			},
			error: function () {
				$("#loading", window.parent.document).css('display', 'none');
				layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
			}
		});
	});
}

// 清空上传文件
function clearUploadFile(idName) {
	// $(idName).wrap('<form></form>');
	// $(idName).unwrap();
	$('#' + idName + '').val('');
}

// 移除上传文件
function delFile(that) {
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
	/*
	 * var url =
	 * $(that).parent().prev().children().children(".file-url").text(); var
	 * fileS3Bucket =
	 * $(that).parent().prev().children().children(".file-bucket").text();
	 * $.ajax({ type:'post', url:'/testManage/testtask/delFile', data:{ url:url,
	 * fileS3Bucket :fileS3Bucket, fileS3Key:fileS3Key },
	 * success:function(data){ if(data.status=="success"){ alert("删除成功！"); }else
	 * if(data.fail == "2"){ alert("删除失败！！！"); } } });
	 */
	$(that).parent().parent().remove();
	removeFile(fileS3Key);
}

// 移除暂存数组中的指定文件
function removeFile(fileS3Key) {
	if (modalType == "new") {
		var _file = $("#files").val();
		if (_file != "") {
			var files = JSON.parse(_file);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_files = files;
			$("#files").val(JSON.stringify(files));
		}

	} else if (modalType == 'edit') {
		var _file = $("#editfiles").val();
		if (_file != "") {
			var files = JSON.parse(_file);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_editfiles = files;
			$("#editfiles").val(JSON.stringify(files));
		}
	} else if (modalType == 'handle') {
		var _file = $("#handlefiles").val();
		if (_file != "") {
			var files = JSON.parse(_file);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_handlefiles = files;
			$("#handlefiles").val(JSON.stringify(files));
		}
	} else if (modalType == 'check') {
		var _file = $("#checkfiles").val();
		if (_file != "") {
			var files = JSON.parse(_file);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_checkfiles = files;
			$("#checkfiles").val(JSON.stringify(files));
		}
	}

}

// 文件下载
function download2(that) {
	var fileS3Bucket = $(that).children(".file-bucket").text();
	var fileS3Key = $(that).children(".file-s3Key").text();
	var fileNameOld = $(that).children("span").text();
	var url = encodeURI("/testManage/testtask/downloadFile?fileS3Bucket=" + fileS3Bucket + "&fileS3Key=" + fileS3Key + "&fileNameOld=" + fileNameOld);
	window.location.href = url;

}

// 查看页面查看附件
function showFile() {
	var reqFeatureId = $("#checktaskId").val();
	var requirementId = $("#checkItcdReqId").val();

	if (requirementId == '') {
		layer.alert('该任务下没有附件！', { icon: 0 });
		return;
	}
	layer.open({
		type: 2,
		area: ['700px', '450px'],
		fixed: false, // 不固定
		maxmin: true,
		btnAlign: 'c',
		title: "相关附件",
		content: reqAttUrl + "?reqId=" + htmlEncodeJQ(requirementId) + "&reqtaskId=" + htmlEncodeJQ(reqFeatureId) + "&taskreqFlag=task"
	});
}

function showThisDiv(This, num) {
	if ($(This).hasClass("def_changeTit")) {
		$("#titleOfwork .def_controlTit").addClass("def_changeTit");
		$(This).removeClass("def_changeTit");
		if (num == 1) {
			$(".dealLog").css("display", "none");
			$(".workRemarks").css("display", "block");
		} else if (num == 2) {
			$(".dealLog").css("display", "block");
			$(".workRemarks").css("display", "none");
		}
	}
}


// 表单校验
function formTesting() {

	$("#splitForm").bootstrapValidator({
		excluded: [":disabled"],// 关键配置1，表示只对于禁用域不进行验证，其他的表单元素都要验证
		message: 'This value is not valid',// 通用的验证失败消息
		feedbackIcons: {// 根据验证结果显示的各种图标
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			sWorkSummary: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '摘要不能为空'
					},
					stringLength: {
						min: 2,
						max: 100,
						message: '长度必须在2-100之间'
					}
				}
			},
			sWorkOverView: {
				trigger: "change",
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
//			 testStage: {
//				 validators: {
//					 notEmpty: {
//						 message: '测试阶段不能为空'
//					 },
//				 }
//			 },
			sWorkDividUserName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '测试人员不能为空'
					},
				}
			},
			sWorkAssignUserName: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '任务分配不能为空'
					},
				}
			},
			sWorkPlanWorkload: {
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

	$("#handleForm").bootstrapValidator({
		message: 'This value is not valid',// 通用的验证失败消息
		feedbackIcons: {// 根据验证结果显示的各种图标
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
		  /*
			 * handleSitstartWork:{ validators: { notEmpty: { message:
			 * '实际开始日期不能为空' }, } }, handleSitendWork:{ validators: { notEmpty: {
			 * message: '实际结束日期不能为空' }, } },
			 */
			handleSitWorkload: {
				validators: {
					/*
					 * notEmpty: { message: '工作量不能为空' },
					 */
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
						message: '请输入大于0小于等于500的正数'
					}
				}
			},
			handlPptWorkload: {
				validators: {
					/*
					 * notEmpty: { message: '工作量不能为空' },
					 */
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
						message: '请输入大于0小于等于500的正数'
					}
				}
			},
			sitTestCaseNum: {
				validators: {
					/*
					 * notEmpty: { message: '系测用例数不能为空' },
					 */
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^\d+(\.\d{0,2})?$/,
						message: '请输入大于0的正数'
					}
				}
			},
		  /*
			 * sitDefectNum:{ validators: { notEmpty: { message: '系测缺陷数不能为空' }, } },
			 */

			pptTestCaseNum: {
				validators: {
					/*
					 * notEmpty: { message: '版测用例数不能为空' },
					 */
					numeric: {
						message: '只能输入数字'
					},
					regexp: {
						regexp: /^\d+(\.\d{0,2})?$/,
						message: '请输入大于0的正数'
					}
				}
			},
		  /*
			 * pptDefectNum:{ validators: { notEmpty: { message: '版测缺陷数不能为空' }, } },
			 */

		}
	});
}


function addClickRow(idArr) {
	for (var i = 0; i < idArr.length; i++) {
		$("#" + idArr[i]).on("click-row.bs.table", function (e, row, $element) {
			$element.children("td").find("input[type=checkbox]").click();
		})
	}

}

function addField() {
	$.ajax({
		url: "/testManage/fieldTemplate/findFieldByReqFeature",
		method: "post",
		success: function (data) {
			if (!data.field) return;
			for (var i = 0; i < data.field.length; i++) {
				appendDataType(data.field[i], 'canEditField', 'new');
			}
		}
	});
}
function appendDataType(thisData, id, status) {
	var obj = $('<div class="def_col_18"></div>');
	switch (thisData.type) {
		case "int":
			obj.attr("dataType", "int");
			var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
			if (status == "new") {
				var labelContent = $('<div class="def_col_28"><input maxlength="' + thisData.maxLength + '" fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" name="int" value="' + thisData.defaultValue + '" /></div>');
			} else {
				var labelContent = $('<div class="def_col_28"><input maxlength="' + thisData.maxLength + '" fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" name="int" value="' + thisData.valueName + '" /></div>');
			}
			labelContent.children(" input ").bind("keyup", function () {
				this.value = this.value.replace(/\D/gi, "");
			})
			obj.append(labelName, labelContent);
			break;
		case "float":
			obj.attr("dataType", "float")
			var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
			if (status == "new") {
				var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="number" class="form-control" placeholder="请输入" name="float" value="' + thisData.defaultValue + '" /></div>');
			} else {
				var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="number" class="form-control" placeholder="请输入" name="float" value="' + thisData.valueName + '" /></div>');
			}
			obj.append(labelName, labelContent);
			break;
		case "varchar":
			obj.attr("dataType", "varchar")
			var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
			if (status == "new") {
				var labelContent = $('<div class="def_col_28"><input  maxlength="' + thisData.maxLength + '"  fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" name="varchar" value="' + thisData.defaultValue + '" /></div>');
			} else {
				var labelContent = $('<div class="def_col_28"><input  maxlength="' + thisData.maxLength + '"  fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" name="varchar" value="' + thisData.valueName + '" /></div>');
			}
			obj.append(labelName, labelContent);
			break;
		case "data":
			obj.attr("dataType", "data")
			var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
			if (status == "new") {
				var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="new' + thisData.fieldName + '" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="' + thisData.defaultValue + '" /></div>');
			} else {
				var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="edit' + thisData.fieldName + '" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="' + thisData.valueName + '" /></div>');
			}
			obj.append(labelName, labelContent);
			break;
		case "timestamp":
			obj.attr("dataType", "timestamp")
			var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
			if (status == "new") {
				var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="new' + thisData.fieldName + '" type="text" readonly id="new_' + thisData.fieldName + '" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="' + thisData.defaultValue + '" /></div>');
			} else {
				var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="edit' + thisData.fieldName + '" type="text" readonly id="new_' + thisData.fieldName + '" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="' + thisData.valueName + '" /></div>');
			}
			obj.append(labelName, labelContent);
			break;
		case "enum":
			obj.attr("dataType", "enum")
			var select = $('<select class="selectpicker" requireded="' + thisData.required + '" fName="' + thisData.fieldName + '"></select>')
			var options = JSON.parse(thisData.enums);
			select.append('<option value=""  fName="' + thisData.fieldName + '">请选择</option>');
			for (var i = 0; i < options.length; i++) {
				if (options[i].enumStatus == 1) {
					if (status == "edit" && options[i].value == thisData.valueName) {
						select.append('<option value="' + options[i].value + '" selected >' + options[i].value + '</option>');
					} else {
						select.append('<option value="' + options[i].value + '">' + options[i].value + '</option>');
					}

				}
			}
			var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
			var labelContent = $('<div class="def_col_28"></div>');
			labelContent.append(select);
			obj.append(labelName, labelContent);
			break;
		default:
			break;
	}
	$("#" + id).append(obj);
	if (obj.attr("dataType") == "data") {
		laydate.render({
			elem: "#" + obj.find("input")[0].id,
			trigger: 'click',
			done: function (value, date, endDate) {
				$("#" + obj.find("input")[0].id).next().css("display", "block");
			}
		});
	} else if (obj.attr("dataType") == "timestamp") {
		laydate.render({
			elem: "#" + obj.find("input")[0].id,
			trigger: 'click',
			type: 'datetime',
			format: 'yyyy-MM-dd HH:mm:ss',
			done: function (value, date, endDate) {
				$("#" + obj.find("input")[0].id).next().css("display", "block");
			}
		});
	}
	$(".selectpicker").selectpicker('refresh');
}
function getFieldData(id) {
	var data = { "field": [] };
	for (var i = 0; i < $("#" + id + " > div").length; i++) {
		// int float varchar data timestamp enum
		var obj = {};
		var type = $("#" + id + " > div").eq(i).attr("dataType")
		if (type == "int" || type == "float" || type == "varchar" || type == "data" || type == "timestamp") {
			obj.fieldName = $("#" + id + " > div").eq(i).find("input").attr("fName");
			obj.required = $("#" + id + " > div").eq(i).find("input").attr("requireded");
			obj.valueName = $("#" + id + " > div").eq(i).find("input").val();
			obj.labelName = $("#" + id + " > div").eq(i).children("div").eq(0).text();
		} else if (type == "enum") {
			obj.fieldName = $("#" + id + " > div").eq(i).find("select").attr("fName");
			obj.required = $("#" + id + " > div").eq(i).find("select").attr("requireded");
			obj.valueName = $("#" + id + " > div").eq(i).find("select").val();
			obj.labelName = $("#" + id + " > div").eq(i).children("div").eq(0).text();
		}
		data.field.push(obj);
	}
	return data;
} 
function urlParam(){
	if( parameterArr.obj.testTaskStatus ){ 
		var arr = parameterArr.obj.testTaskStatus;
		arr.split(",").map(function(val,index){
			$("#testTaskStatus option[value="+ val +"]").attr("selected", true);
		})
	}
	if( parameterArr.obj.userId ){  
		$("#manageUserId").val( parameterArr.obj.userId )
		$("#devManPostName").val( decodeURIComponent(parameterArr.obj.userName)  )
	}
}

function addTitle(){
	for(var i = 1;i < colIndex.length;i++){
		if(colIndex[i] == "requirementFeatureStatus" || colIndex[i] == "deployStatus" || colIndex[i] == "requirementFeatureSourceStr" || colIndex[i] == "createTypeStr"){
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not (*abc* or *def*):查找不包含abc或不包含def的结果。" );
		}else if(colIndex[i] == "testCaseNumStr" || colIndex[i] == "defectNumStr" || colIndex[i] == "changeNumberStr"){
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n7、=:表示等于。\n示例如下:\n<100、>100、<=100、>=100、=100、>100 and <200、>=100 and <=200、\n<100 or >200、(>=100 and <=200) or (>=400 and <=500)" );
		}else if(colIndex[i] == "windowName"){
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n7、搜索内容必须为yyyyMMdd格式\n示例如下:\n<20200101、>20200101、<=20200101、>=20200101、>20200101 and <20200201、>=20200101 and <=20200201、\n<20200101 or >20200201、(>=20200101 and <=20200201) or (>=20200401 and <=20200501)" );
		}else{
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、and:表示同时满足条件。\n3、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* and *def*:查找同时包含abc和包含def的结果。\n5、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not *abc* or not *def*:查找不包含abc或不包含def的结果。\n8、not (*abc* and *def*):查找不同时包含abc和def的结果。\n9、not (*abc* or *def*):查找不包含abc或不包含def的结果。\n10、(*ab* and *cd*) or (*df* and *gh*):查找同时包含ab和cd或者同时包含df和gh的结果。" );
		}
	}
}

function addTableTitle(){
	for(var i = 1;i < colIndex.length;i++){
		if(colIndex[i] == "requirementFeatureStatus" || colIndex[i] == "deployStatus" || colIndex[i] == "requirementFeatureSourceStr" || colIndex[i] == "createTypeStr"){
			$("#gs_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not (*abc* or *def*):查找不包含abc或不包含def的结果。" );
		}else if(colIndex[i] == "testCaseNumStr" || colIndex[i] == "defectNumStr" || colIndex[i] == "changeNumberStr" ){
			$("#gs_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n7、=:表示等于。\n示例如下:\n<100、>100、<=100、>=100、=100、>100 and <200、>=100 and <=200、\n<100 or >200、(>=100 and <=200) or (>=400 and <=500)" );
		}else if(colIndex[i] == "windowName"){
			$("#gs_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n7、搜索内容必须为yyyyMMdd格式\n示例如下:\n<20200101、>20200101、<=20200101、>=20200101、>20200101 and <20200201、>=20200101 and <=20200201、\n<20200101 or >20200201、(>=20200101 and <=20200201) or (>=20200401 and <=20200501)" );
		}else{
			$("#gs_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、and:表示同时满足条件。\n3、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* and *def*:查找同时包含abc和包含def的结果。\n5、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not *abc* or not *def*:查找不包含abc或不包含def的结果。\n8、not (*abc* and *def*):查找不同时包含abc和def的结果。\n9、not (*abc* or *def*):查找不包含abc或不包含def的结果。\n10、(*ab* and *cd*) or (*df* and *gh*):查找同时包含ab和cd或者同时包含df和gh的结果。" );
		}
	}
}
//查询系统和需求下有多少开发任务
function getFeatureBySystemAndRequirement(systemId,requirementId){
	
	$.ajax({
		url:"/testManage/testtask/getFeatureBySystemAndRequirement",
		dataType:"json",
		data:{
			"systemId":systemId,
			"requirementId":requirementId
		},
		type:"post",
		success: function (data) {
			 if(data.feature.length > 0){
                 var featureData = "<div>该系统和需求下已存在测试任务！";
                 var id = $("#edittestTaskId").val();
                 for (var i = 0; i < data.feature.length; i++) {
                	 if(id!=data.feature[i].id){
                		 featureData+= "<div>任务编号："+data.feature[i].featureCode+"</div>";
                	 }
                 }
                 featureData += '</div>';
                 if(featureData!="<div>该系统和需求下已存在测试任务！</div>"){
                	 layer.alert(featureData, {
                         title: "提示信息",
                         closeBtn: 0,
                         btn:["取消"]
                     });
                 }
                
             } 
		},
		error: function () {
			layer.alert("查询失败！", { icon: 2 });
			$("#loading").css('display', 'none');
		}
	});
}