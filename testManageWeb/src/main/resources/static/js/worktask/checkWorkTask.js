var taskStateList = '';
var modified = "<span>&nbsp;修改为&nbsp;</span>";

var _icon_word = "../static/images/devtask/word.png",_icon_excel = "../static/images/devtask/excel.png",_icon_text = "../static/images/devtask/text.png",_icon_pdf = "../static/images/devtask/pdf.png";
var _files = [],_editfiles = [],_checkfiles = [],modalType = '',_handlefiles = [],_SeeFiles = [],_Newhandlefiles = [],_Dhandlefiles = [],_newDhandlefiles = [],Neweditfiles = [],deleteAttaches = [],examineAttaches = [],newexamineAttaches = [];
var taskStateList = '';

var tableIdArr=[ 'userTable' , 'reqTable' ,'TaskTable' ,'systemTable','TestPopupTable','EditPopupTable','withinUserTable' ,'comWindowTable'];
 
var modified = "<span>&nbsp;修改为&nbsp;</span>";
var userStatus = '';
var windowTypeList = [];
var selectWinIds = [];
var assignUser = '';

var parameterArr = {};
parameterArr.arr = window.location.href.split("?");
parameterArr.parameterArr = parameterArr.arr[1].split(",");
parameterArr.obj = {};
for (var i = 0; i < parameterArr.parameterArr.length; i++) {
    var obj = parameterArr.parameterArr[i].split("="); 
    parameterArr.obj[obj[0]] = obj[1];
}

$(function () { 
	$(".headReturn").bind("click", function () { 
        window.parent.layer.closeAll();
    });
	taskStateList = $("#taskState").find("option");
	uploadFileList();
	getSee(parameterArr.obj.id)
	 
}); 
function getSee(id) {
	$.ajax({

		method: "post",
		url: "/testManage/worktask/getSeeDetail",
		data: { "id": id },
		success: function (data) {
			selectdetail(data)
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
		}


	});
}

//查看
function selectdetail(data) {
	$("#loading").css('display', 'block'); 
	$( "#titleOfwork" ).children("span").eq(0).click();
	_checkfiles = [];
	var map = data.dev;
	$("#checkfiles").val("");
	$("#checkAttTable").empty();
	$("#taskRemark").empty();
	$("#handleLogs").empty();
	$("#SeeFileTable").empty();
	$("#tyaskRemark").val("");
	$("#SdevCode").html("");
	$("#SdevName").html("");
	$("#SdevOverview").html("");
	$("#SStatus").html("");
	$("#devuserID").html("");
	//	$("#planStartDate").html("");
	//	$("#planEndDate").html("");
	$("#featureName").html(""); 	
	//	$("#SplanWorkload").html("");
	$("#featureOverview").html("");
	$("#requirementFeatureStatus").html("");
	$("#manageUserId").html("");
	$("#executeUserId").html("");
	$("#systemId").html("");
	$("#requirementSource").html("");
	$("#requirementType").html("");
	$("#requirementPriority").html("");
	$("#requirementPanl").html("");
	$("#requirementStatus").html("");
	$("#requirementName").html("");
	$("#applyUserId").html("");
	$("#applyDeptId").html("");
	$("#expectOnlineDate").html("");
	$("#planOnlineDate").html("");
	$("#lastUpdateDate3").html("");
	$("#createDate3").html("");
	$("#openDate").html("");
	$("#requirementOverview").val("");
	$("#testStage2").text("");
	$("#checkEnvironmentType").html('');
	$("#requirementOverview").text(map.requirementOverview);
	$("#SdevCode").html(map.testTaskCode);
	$("#SdevName").html(map.testTaskName);
	$("#SdevOverview").html(map.testTaskOverview);  
	var devTaskStatus = show_status( map.testDevTaskStatus ) 
	$("#implementation_status").empty();
	$("#implementation_status").append( devTaskStatus );
	$("#SStatus").html( map.testDevTaskStatus);
	$("#devuserID").html(map.devuserName);
	$("#testTaskID").html(map.id);
	$("#createBy").html(map.createName);
	if (map.createDate != null) {
		$("#testStage2").text(map.testStage);
	}

	if (map.createDate != null) {
		$("#createDate").html(datFmt(new Date(map.createDate), "yyyy年MM月dd"));
	}

	//		if(map.planStartDate!=null){
	//			var planstartDate=datFmt(new Date(map.planStartDate),"yyyy年MM月dd");
	//			$("#planStartDate").html(planstartDate);
	//		}
	//		if(map.planEndDate!=null){
	//			var planEndDate=datFmt(new Date(map.planEndDate),"yyyy年MM月dd");
	//			$("#planEndDate").html(planEndDate);
	//		}
	//		$("#SplanWorkload").html(map.planWorkload);
	var users = map.executeUser.join(',');
	$("#cases").html("");
	$("#caseExecutes").html("");
	$("#executeUser").html("");
	$("#defectNum").html("");
	$("#checkTaskAssignUser").html("");
	$("#cases").html(map.designCaseNumber);
	$("#caseExecutes").html(map.executeCaseNumber);
	$("#executeUser").html(users);
	$("#defectNum").html(map.defectNum);
	$("#checkTaskAssignUser").html(map.taskAssignUserName);

	$("#checkChange").empty();
	$("#checkRequirementFeatureSource").empty();
	$("#checkImportantRequirementType").empty();
	$("#checkPptDeployTime").empty();
	$("#checkSubmitTestTime").empty();
	$("#checkChange").text(map.requirementChangeNumber == null ? '' : map.requirementChangeNumber);
	$("#checkRequirementFeatureSource").text(map.requirementFeatureSource == null ? '' : map.requirementFeatureSource);
  var importantRequirement = "";
  if(map.importantRequirementType=='1'){
      importantRequirement="是"
  }else if(map.importantRequirementType=='2'){
      importantRequirement="否"
  }
  $("#checkImportantRequirementType").text(importantRequirement);
	$("#checkPptDeployTime").text(map.pptDeployTime == null ? '' : timestampToTime(map.pptDeployTime));
	$("#checkSubmitTestTime").text(map.submitTestTime == null ? '' : timestampToTime(map.submitTestTime));



	$("#actualStartDate").html("");
	if (map.actualStartDate != null) {
		var actualStartDate = datFmt(new Date(map.actualStartDate), "yyyy年MM月dd");
		$("#actualStartDate").html(actualStartDate);
	}
	$("#actualEndDate").html("");
	if (map.actualEndDate != null) {
		var actualEndDate = datFmt(new Date(map.actualEndDate), "yyyy年MM月dd");
		$("#actualEndDate").html(actualEndDate);
	}
	$("#actualWorkload").html("");
	$("#actualWorkload").html(map.actualWorkload);
	
	$("#featureName").attr( 'val', map.featureId );
	$("#featureName").text(map.featureCode + " | " + map.featureName);
	$("#testWorkModal .modal-title").text(map.featureCode + "&nbsp|&nbsp" + map.featureName);
	
	$("#requirementCode").text( toStr(map.requirementCode) )
	$("#reqModal .modal-title").text(map.requirementCode);
	
	$("#featureOverview").html(map.featureOverview);
	$("#requirementFeatureStatus").html(map.requirementFeatureStatusName);

	$("#manageUserId").html(map.manageUserName);
	$("#executeUserId").html(map.executeUserName);
	$("#systemId").html(map.systemName);
	$("#requirementName").html(map.requirementName);
	$("#requirementSource").html(map.requirementSource);
	$("#requirementType").html(map.requirementType);
	$("#requirementPriority").html(map.requirementPriority);
	$("#requirementPanl").html(map.requirementPanl);
	$("#requirementStatus").html(map.requirementStatus);
	$("#applyUserId").html(map.applyUserName);
	$("#applyDeptId").html(map.applyDeptName);
	
	for(var i=0;i< environmentTypeList.length;i++){
		if(environmentTypeList[i][0] == map.environmentType){
			$("#checkEnvironmentType").html(environmentTypeList[i][1]);
		}
	}

	
	if (map.expectOnlineDate != null) {
		var expectOnlineDate = datFmt(new Date(map.expectOnlineDate), "yyyy年MM月dd");
		$("#expectOnlineDate").html(expectOnlineDate);
	}
	if (map.planOnlineDate != null) {
		var planOnlineDate = datFmt(new Date(map.planOnlineDate), "yyyy年MM月dd");
		$("#planOnlineDate").html(planOnlineDate);
	}
	if (map.openDate != null) {
		var openDate = datFmt(new Date(map.openDate), "yyyy年MM月dd");
		$("#openDate").html(openDate);
	}
	if (map.createDate3 != null) {
		var createDate3 = datFmt(new Date(map.createDate3), "yyyy年MM月dd");
		$("#createDate3").html(createDate3);
	}

	if (map.lastUpdateDate3 != null) {
		var lastUpdateDate3 = datFmt(new Date(map.lastUpdateDate3), "yyyy年MM月dd");
		$("#lastUpdateDate3").html(lastUpdateDate3);
	}
	if (data.attachements != undefined) {
		var _table = $("#SeeFileTable");
		var attMap = data.attachements;
		//var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
		for (var i = 0; i < attMap.length; i++) {
			var _tr = '';
			var file_name = attMap[i].fileNameOld;
			var file_type = attMap[i].fileType;
			var file_id = attMap[i].id;
			var _td_icon;
			//<i class="file-url">'+data.attachements[i].filePath+'</i>
			var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + attMap[i].fileS3Bucket + '</i><i class = "file-s3Key">' + attMap[i].fileS3Key + '</i></td>';
			switch (file_type) {
				case "doc":
				case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
				case "xls":
				case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
				case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
				case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
				default: _td_icon = '<img src="' + _icon_word + '" />'; break;
			}
			var row = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
			_tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + _td_icon + " " + _td_name + '</tr>';
			_table.append(_tr);
			_SeeFiles.push(attMap[i]);
			$("#SeeFiles").val(JSON.stringify(_SeeFiles));
		}
	}

	//备注
	if (data.rmark != undefined) {
		var str = '';
		for (var i = 0; i < data.rmark.length; i++) {
			var style = '';
			if (i == data.rmark.length - 1) {
				style = ' lastLog';
			}
			str += '<div class="logDiv' + style + '"><div class="logDiv_title"><span class="orderNum"></span>' +
				'<span>' + data.rmark[i].userName + '  | ' + data.rmark[i].userAccount + '</span>&nbsp;&nbsp;&nbsp;<span>' + datFmt(new Date(data.rmark[i].createDate), "yyyy-MM-dd hh:mm:ss") + '</span></div>' +
				'<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><span>' + data.rmark[i].testTaskRemark + '</span>' +
				'<div class="file-upload-list">';
			if (data.Attchement.length > 0) {
				str += '<table class="file-upload-tb">';
				var _trAll = '';
				for (var j = 0; j < data.Attchement.length; j++) {
					var _tr = '';
					if ((data.Attchement[j].testTaskRemarkId) == (data.rmark[i].id)) {

						var file_type = data.Attchement[j].fileType;
						var file_name = data.Attchement[j].fileNameOld;
						var _td_icon;
						var _td_name = '<span>' + file_name + '</span>';
						switch (file_type) {
							case "doc":
							case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
							case "xls":
							case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
							case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
							case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
							default: _td_icon = '<img src="' + _icon_word + '" />'; break;
						}
						var row = JSON.stringify(data.Attchement[j]).replace(/"/g, '&quot;');
						_tr += '<tr><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + _td_icon + _td_name + "</tr>";

					}
					if (_tr != undefined) {
						_trAll += _tr;
					}

				}
				str += _trAll + '</table>';
			}

			str += '</div></div></div></div></div>';

		}
		$("#taskRemark").append(str);

	}

	//处理日志
	if (data.logs != undefined) {
		var _span = "";
		var str = '';
		for (var i = 0; i < data.logs.length; i++) {
			var style = '';
			if (i == data.logs.length - 1) {
				style = ' lastLog';
			}
			var style2 = '';
			var addDiv = '';
			var logDetail = '';
			if (data.logs[i].logDetail == null || data.logs[i].logDetail == '') {
				if (data.logAttachement.length > 0) {
					for (var j = 0; j < data.logAttachement.length; j++) {
						if (data.logs[i].id != data.logAttachement[j].devTaskLogId) {
							style2 = 'style="display:none"';
							addDiv = '</br>';
						}
					}
				} else {
					style2 = 'style="display:none"';
					addDiv = '</br>';
				}



			} else {
				logDetail = data.logs[i].logDetail;
				logDetail = logDetail.replace(/;/g, "<br/>");
			}


			str += '<div class="logDiv' + style + '"><div class="logDiv_title"><span class="orderNum"></span><span>' + data.logs[i].logType + '</span>&nbsp;&nbsp;&nbsp;' +
				'<span>' + data.logs[i].userName + '  | ' + data.logs[i].userAccount + '</span>&nbsp;&nbsp;&nbsp;<span>' + datFmt(new Date(data.logs[i].createDate), "yyyy-MM-dd hh:mm:ss") + '</span></div>' +
				'<div class="logDiv_cont" ><div class="logDiv_contBorder"><div class="logDiv_contRemark" ' + style2 + '>'

			var ifjson = isJSON(logDetail);
			if (ifjson) {
				_span = "";
				if (logDetail == "[]" || logDetail == "" || logDetail == undefined) {
					_span = '<span>未做任何操作</span>'
				} else {
					var Detail = JSON.parse(logDetail);
					for (var s = 0; s < Detail.length; s++) {
						//alert(Detail[s].newValue);
						var value = Detail[s].oldValue
						if (value == "" || value == undefined || value == null) {
							value = ""
						}
						if (Detail[s].remark != "" && Detail[s].remark != undefined) {
							str += '<span>备注信息：' + Detail[s].remark + '</span></br>'
						}
						if ("" != Detail[s].newValue && undefined != Detail[s].newValue) {
							if (value == null || value == "") {
								value = "&nbsp;&nbsp"
							}
							str += '<span>' + Detail[s].fieldName + "：" + "<span style='font-weight:bold'>" + '"' + value + '"' + "</span>" + modified + "<span style='font-weight:bold'>" + '"' + Detail[s].newValue + '"' + "</span>" + '</span></br>'
						}
					}
				}

			} else {
				str += '<span>' + logDetail + '</span>'
			}

			str += '<div class="file-upload-list">';
			if (data.logAttachement.length > 0) {
				str += '<table class="file-upload-tb">';
				var _trAll = '';
				for (var j = 0; j < data.logAttachement.length; j++) {
					if ((data.logAttachement[j].testTaskLogId) == (data.logs[i].id)) {
						var attType = '';
						_span = "";
						if (data.logAttachement[j].status == 1) {//新增的日志
							attType = "<lable>新增附件：</lable>";
						} else if (data.logAttachement[j].status == 2) {//删除的日志
							attType = "<lable>删除附件：</lable>";
						}
						var _tr = '';
						var file_name = data.logAttachement[j].fileNameOld;
						var file_type = data.logAttachement[j].fileType;
						var _td_icon;
						var _td_name = '<span>' + file_name + '</span></div></td>';
						//var _td_name = '<span>'+file_name+'</span><i class="file-url">'+data.logAttachement[j].filePath+'</i><i class = "file-bucket">'+data.logAttachement[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.logAttachement[j].fileS3Key+'</i></td>';

						switch (file_type) {
							case "doc":
							case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
							case "xls":
							case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
							case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
							case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
							default: _td_icon = '<img src="' + _icon_word + '" />'; break;
						}
						var row = JSON.stringify(data.logAttachement[j]).replace(/"/g, '&quot;');
						_tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + attType + _td_icon + _td_name + '</tr>';
						_trAll += _tr;
					}
				}

			}
			if (_trAll == undefined) { _trAll = "" }
			_trAll += _span;
			//_span="";
			str += _trAll + '</table>';
			_trAll = "";
			str += '</div></div>' + addDiv + '</div></div></div>';

		}
		$("#handleLogs").append(str);
	}
	modalType = 'check';
	$("#loading").css('display', 'none');
	$("#selectdetail").modal("show");
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
function show_status( value ){
	var statusVal='';
	for( var i = 0;i<taskStateList.length;i++ ){ 
		if( taskStateList[i].text == value ){ 
			statusVal = taskStateList[i].value 
		}
	} 
	switch (statusVal) {
		case '1':return "<span class='doing1'>" + value + "</span>";break;
		case '2':return "<span class='doing2'>" + value + "</span>";break;
		case '3':return "<span class='doing3'>" + value + "</span>";break;
		case '0':return "<span class='doing4'>" + value + "</span>";break;
		case '4':return "<span class='doing5'>" + value + "</span>";break;
		case '5':return "<span class='doing6'>" + value + "</span>";break;
		case '6':return "<span class='doing7'>" + value + "</span>";break;
		case '7':return "<span class='doing8'>" + value + "</span>";break;
		case '8':return "<span class='doing9'>" + value + "</span>";break;
		case '9':return "<span class='doing10'>" + value + "</span>";break;
		case '10':return "<span class='doing11'>" + value + "</span>";break;
		case '11':return "<span class='doing12'>" + value + "</span>";break;
		default:return "<span class='doing'>" + value + "</span>";break;
	}
}
function timestampToTime(timestamp) {
	if (timestamp == null) {
		return;
	}
	var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
	var Y = date.getFullYear() + '-';
	var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
	var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
	return Y + M + D;
}
function isJSON(str) {
	if (typeof str == 'string') {
		try {
			JSON.parse(str);
			return true;
		} catch (e) {
			return false;
		}
	}
}
function down(This) {
	if ($(This).hasClass("fa-angle-double-down")) {
		$(This).removeClass("fa-angle-double-down");
		$(This).addClass("fa-angle-double-up");
		$(This).parents('.allInfo').children(".def_content").slideDown(100);
	} else {
		$(This).addClass("fa-angle-double-down");
		$(This).removeClass("fa-angle-double-up");
		$(This).parents('.allInfo').children(".def_content").slideUp(100);
	}
}

//文件上传，并列表展示
function uploadFileList() {
	//列表展示
	$(".upload-files").change(function () {
		var files = this.files;
		var formFile = new FormData();
		/*if(!fileAcceptBrowser()){
			for(var i=0,len=files.length;i<len;i++){
				var file_type = files[i].name.split(".")[1];
				if(file_type!="doc"&&file_type!="docx"&&file_type!="xls"&&file_type!="xlsx"&&file_type!="txt"&&file_type!="pdf"){
					layer.alert('上传文件格式错误! 请上传后缀名为.doc，.docx，.xls，.xlsx，.txt，.pdf的文件', {icon:0});
					return false;
				}
			}
		}*/
		var filesSize = 0;
		for (var i = 0, len2 = files.length; i < len2; i++) {
			filesSize += files[i].size;
			//  files.append("files",formFileList[i]);
		}
		if (filesSize > 1048576000) {
			layer.alert('文件太大,请删选！！！', {
				icon: 2,
				title: "提示信息"
			});
			return false;
		}
		outer: for (var i = 0, len = files.length; i < len; i++) {
			var file = files[i];
			if (file.size <= 0) {
				layer.alert(file.name + "文件为空！", { icon: 0 });
				continue;
			}
			if (file.size > 10485760) {
				layer.alert(file.name + '：超过10MB！', {
					icon: 2,
					title: "提示信息"
				});
				return false;
			}
			var fileList = [];
			var oldFileList = [];
			if (modalType == 'new') {
				fileList = _files;
			} else if (modalType == 'edit') {
				oldFileList = _editfiles;
				fileList = Neweditfiles;
			} else if (modalType == 'check') {
				fileList = _checkfiles;
			} else if (modalType == 'dHandle') {
				oldFileList = _Dhandlefiles;
				fileList = _newDhandlefiles;
			} else if (modalType == 'Handle') {
				oldFileList = _handlefiles;
				fileList = _Newhandlefiles;
			} else if (modalType == "examine") {
				oldFileList = examineAttaches;
				fileList = newexamineAttaches;
			}
			for (var j = 0; j < fileList.length; j++) {
				if (fileList[j].fileNameOld == file.name) {
					layer.alert(file.name + " 文件已存在", {
						icon: 2,
						title: "提示信息"
					});
					continue outer;
				}
			}
			for (var j = 0; j < oldFileList.length; j++) {
				if (oldFileList[j].fileNameOld == file.name) {
					layer.alert(file.name + " 文件已存在", {
						icon: 2,
						title: "提示信息"
					});
					continue outer;
				}
			}
			formFile.append("files", file);
			//读取文件
			if (window.FileReader) {
				(function (i) {
					var file = files[i];
					var reader = new FileReader();
					reader.readAsDataURL(file);
					reader.onerror = function (e) {
						layer.alert("文件" + file.name + " 读取出现错误", { icon: 0 });
						return false;
					};
					reader.onload = function (e) {
						if (e.target.result) {
							console.log("文件 " + file.name + " 读取成功！");
						}
					};
				})(i);
			}
			//列表展示
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
				case "xlsx": _td_icon = '<img src="' + _icon_excel + '" />'; break;
				case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
				case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
				default: _td_icon = '<img src="' + _icon_word + '" />'; break;
			}
			var _table = $(this).parent(".file-upload-select").next(".file-upload-list").children("table");
			_tr += '<tr><td><div class="fileTb">' + _td_icon + '  ' + _td_name + _td_opt + '</tr>';
			_table.append(_tr);

		}
		//上传文件
		$("#loading",window.parent.document).css('display','block');
		$.ajax({
			type: 'post',
			url: '/zuul/testManage/worktask/uploadFile',
			contentType: false,
			processData: false,
			dataType: "json",
			data: formFile,

			success: function (data) {
				for (var k = 0, len = data.length; k < len; k++) {
					if (modalType == 'new') {
						_files.push(data[k]);
					} else if (modalType == 'edit') {
						Neweditfiles.push(data[k]);
						//_editfiles.push(data[k]); 
					} else if (modalType == 'check') {

						_checkfiles.push(data[k]);
					} else if (modalType == 'dHandle') {
						_newDhandlefiles.push(data[k]);
					} else if (modalType == 'Handle') {
						_Newhandlefiles.push(data[k]);
					} else if (modalType == "examine") {
						newexamineAttaches.push(data[k]);
					}
					$(".file-upload-tb span").each(function (o) {
						if ($(this).text() == data[k].fileNameOld) {
							//$(this).parent().children(".file-url").html(data[k].filePath);
							$(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
							$(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
						}
					});
				}
				if (modalType == 'new') {
					$("#files").val(JSON.stringify(_files));
					clearUploadFile('uploadFile');
				} else if (modalType == 'edit') {
					//$("#editfiles").val(JSON.stringify(_editfiles));
					$("#newFiles").val(JSON.stringify(Neweditfiles));
					clearUploadFile('edituploadFile');
				} else if (modalType == 'check') {
					$("#checkfiles").val(JSON.stringify(_checkfiles));
					clearUploadFile('checkuploadFile');
				} else if (modalType == 'dHandle') {
					$("#NewDHattachFiles").val(JSON.stringify(_newDhandlefiles));
					clearUploadFile('DHituploadFile');
				} else if (modalType == 'Handle') {
					$("#newHattachFiles").val(JSON.stringify(_Newhandlefiles));
					clearUploadFile('HituploadFile');
				} else if (modalType == "examine") {
					$("#newExamineFiles").val(JSON.stringify(newexamineAttaches));
					clearUploadFile('examineUploadFile');
				}
				$("#loading",window.parent.document).css('display','none');
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				$("#loading",window.parent.document).css('display','none');
				layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
			}
		});
	});
}
//移除上传文件
function delFile(that, id) {
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
	/*var url = $(that).parent().prev().children().children(".file-url").text(); 
	var fileS3Bucket = $(that).parent().prev().children().children(".file-bucket").text(); */

	$(that).parent().parent().remove();
	removeFile(fileS3Key);
}

function deleteAttachements(that, attache) {
	$(that).parent().parent().remove();
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
	removeFile(fileS3Key);
	deleteAttaches.push(attache);
	/*$.ajax({
			type:'post',
			url:'/testManage/worktask/delFile',
			data:{
					id:attache.id
			},
			success:function(data){
					if(data.status=="success"){
							layer.alert('删除成功！', {
									icon: 1,
									title: "提示信息"
							});
					}else if(data.status == "fail"){
							layer.alert('删除失败！', {
									icon: 2,
									title: "提示信息"
							});
					}
			}
	});*/
}

//移除暂存数组中的指定文件
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
		var _newfile = $("#newFiles").val();
		if (_file != "") {
			var files = JSON.parse(_file);
			for (var i = 0; i < files.length; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_editfiles = files;
			$("#editfiles").val(JSON.stringify(files));
		}
		if (_newfile != "") {
			var files = JSON.parse(_newfile);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			Neweditfiles = files;
			$("#newFiles").val(JSON.stringify(files));
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
	} else if (modalType == 'dHandle') {
		var _file = $("#DHattachFiles").val();
		var _newfile = $("#NewDHattachFiles").val();
		if (_file != "") {
			var files = JSON.parse(_file);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_Dhandlefiles = files;
			$("#DHattachFiles").val(JSON.stringify(files));
		}
		if (_newfile != "") {
			var files = JSON.parse(_newfile);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_newDhandlefiles = files;
			$("#NewDHattachFiles").val(JSON.stringify(files));
		}
	} else if (modalType == 'Handle') {
		var _file = $("#HattachFiles").val();
		var _newfile = $("#newHattachFiles").val();
		if (_file != "") {
			var files = JSON.parse(_file);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_handlefiles = files;
			$("#HattachFiles").val(JSON.stringify(files));
		}
		if (_newfile != "") {
			var files = JSON.parse(_newfile);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_Newhandlefiles = files;
			$("#newHattachFiles").val(JSON.stringify(files));
		}
	} else if (modalType == 'examine') {
		var _file = $("#OverSeeFiles").val();
		var _newfile = $("#newExamineFiles").val();
		if (_file != "") {
			var files = JSON.parse(_file);
			for (var i = 0; i < files.length; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			examineAttaches = files;
			$("#OverSeeFiles").val(JSON.stringify(files));
		}
		if (_newfile != "") {
			var files = JSON.parse(_newfile);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			newexamineAttaches = files;
			$("#newExamineFiles").val(JSON.stringify(files));
		}
	}
}
//文件下载
function download(row) {
	var fileS3Bucket = row.fileS3Bucket;
	var fileS3Key = row.fileS3Key;
	var fileNameOld = row.fileNameOld;
	window.location.href = "/testManage/worktask/downloadFile?fileS3Bucket=" + fileS3Bucket + "&fileS3Key=" + fileS3Key + "&fileNameOld=" + fileNameOld;
}
//清空上传文件
function clearUploadFile(idName) {
	$('#' + idName + '').val('');
}
