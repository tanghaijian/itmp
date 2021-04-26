var parameterArr={};
var _icon_word = "../static/images/devtask/word.png", _icon_excel = "../static/images/devtask/excel.png", _icon_text = "../static/images/devtask/text.png", _icon_pdf = "../static/images/devtask/pdf.png", _icon_img = "../static/images/devtask/img.png", _icon_other = "../static/images/devtask/other.png";
var _files = [], _editfiles = [], _handlefiles = [], _checkfiles = [], testtaskStatusList = [], deployStatusData = [], _checkfiles2 = [];
var modalType = '', excuteUserName = '', workStatus = '';
var idArr=[ 'userTable' , 'systemTable' ,'listReq' ,'comWindowTable' ];
var devtaskStatusList ='';//开发任务状态
$(document).ready(function(){
	
	testtaskStatusList = $("#testTaskStatus").find("option");
	
	parameterArr.arr = window.location.href.split( "?" ); 
    parameterArr.parameterArr = parameterArr.arr[1].split( "," );
    parameterArr.obj={};
    for( var i = 0 ; i < parameterArr.parameterArr.length ; i++ ){
	   	var obj = parameterArr.parameterArr[i].split( "=" );  
	   	if( obj[1] == "undefined" ){
	   		parameterArr.obj[ obj[0] ] = '';
	   	}else{
	   		parameterArr.obj[ obj[0] ] = obj[1];
	   	} 
    }    
    $(".headReturn").bind("click",function (){
    	parent.layer.closeAll(); 
    })
    uploadFileList();
	initPage();
	devtaskStatusList = getReqFeatureStatus();
});
function initPage(idr){
	$('#check_agility_inner').hide();
	$('#check_iddevRequirementFeature_name').val('');
	$('#check_estimateWorkload').val('');
	$('#check_estimateRemainWorkload').val('');
	$('#check_actualWorkload').val('');

	var id;
	if( idr == undefined ){
		id = parameterArr.obj.id
	}else{
		id = idr
	} 
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/getOneTestTask2",
		type: "post",
		dataType: "json",
		data: {
			"id": id
		},
		success: function (data) {
			$("#questionNumber").text(data.questionNumber);
			
			$("#checkEditField").empty();
			
			$("#checksystemName").text('');
			$("#workcheckAttTable").empty();
			$("#checkdeptName").text('');
			$("#connectDiv").empty();
			$("#workTaskDiv").empty();
			$("#checkFileTable").empty();
			$("#remarkBody").empty();
			$("#tyaskRemark").val('');
			$("#checkAttTable").empty();
			$("#handleLogs").empty();
			$("#brother_div").empty();
			$("#checkChange").empty();
			$("#checkRequirementSource").empty();
			$("#checkImportantRequirementType").empty();
			$("#checkPptDeployTime").empty();
			$("#checkSubmitTestTime").empty();
			$("#checkRequirementDept").empty();
			$("#develop").empty();
			$("#checkProjectName").text(data.projectName);
			$("#check_planStartDate").text(data.planStartDate);
			$("#check_planEndDate").text(data.planEndDate);

			$("#checkItcdReqId").val("");
			$("#checkItcdReqId").val(data.itcdRequrementId);
			$("#checktaskId").val("");
			$("#checktaskId").val(data.taskId);
			var result_status = show_status(data.requirementFeatureStatus);
			$('#implementation_status').html(result_status);
			
			$("#checktestTaskTitle").text(isValueNull(data.featureCode) + " | " + isValueNull(data.featureName));
			$("#checktestTaskTitle").attr('title',isValueNull(data.featureCode) + " | " + isValueNull(data.featureName));
			
			$("#checktestTaskOverview").text( isValueNull(data.featureOverview) );
			var statusName = '';
			for (var i = 0; i < testtaskStatusList.length; i++) {
				if (testtaskStatusList[i].value == data.requirementFeatureStatus) {
					statusName = testtaskStatusList[i].innerHTML;
				}
			}
			$("#checktestTaskStatus").text(statusName);
			$("#checkdevManPost").text(data.manageUserName);
			$("#checkexecutor").text(data.executeUserName);
			$("#checksystemName").text('');
			$("#checksystemName").text(data.systemName);
			if(data.developmentMode == 1){
				$('#check_iddevRequirementFeature_name').text(data.devRequirementFeatureName || '');
				$('#check_estimateWorkload').text(data.estimateWorkload || '');
				$('#check_estimateRemainWorkload').text(data.estimateRemainWorkload || '');
				$('#check_actualWorkload').text(data.actualWorkload || '');
				// $('#check_agility_inner').show();
			}

			$("#window").text('');
			$("#window").text(data.windowName);
			$("#checkoutrequirement").text('');
			$("#checkoutrequirement").text( isValueNull(data.requirementCode) );
			
			$("#checkProjectPlanName").text('');
			$("#checkProjectPlanName").text( isValueNull(data.planName) );
			
			$("#checkdeptName").text( isValueNull(data.deptName) );
			$("#checkChange").text( isValueNull(data.requirementChangeNumber) );
			$("#checkRequirementSource").text(data.featureSource);
			$("#checkRequirementDept").text(data.requirementDeptName);

            var importantRequirement = "";
			if(data.importantRequirementType=='1'){
                importantRequirement="是"
			}else if(data.importantRequirementType=='2'){
                importantRequirement="否"
			}
            $("#checkImportantRequirementType").text(importantRequirement);

			$("#checkPptDeployTime").text(timestampToTime(data.pptDeployTime));
			$("#checkSubmitTestTime").text(timestampToTime(data.submitTestTime));

			var status = "";
			if (data.temporaryStatus == "1") {
				status = "是";
			} else if (data.temporaryStatus == "2") {
				status = "否";
			}
//			$("#checktemporaryTask").text(status);//1是2否
			//实际系测开始时间
			$("#checkpreSitStartDate").text('');
			$("#checkpreSitStartDate").text(data.actSitStart);
			//实际系测结束时间
			$("#checkpreSitEndDate").text('');
			$("#checkpreSitEndDate").text(data.actSitEnd);
			//实际版测开始时间
			$("#checkprePptStartDate").text('');
			$("#checkprePptStartDate").text(data.actPptStart);
			//实际版测结束时间
			$("#checkprePptEndDate").text('');
			$("#checkprePptEndDate").text(data.actPptEnd);
			//实际系测工作量 
			$("#checkpreSitWorkload").text('');
			$("#checkpreSitWorkload").text(data.workload==null?0:data.workload);
			//实际板测工作量
			$("#checkprePptWorkload").text('');
			$("#checkprePptWorkload").text(data.workload2==null?0:data.workload2);
			//系测设计案例数
			$("#designCaseNumber").text('');
			$("#designCaseNumber").text(data.designCaseNumber==null?0:data.designCaseNumber);
			//版测设计案例数
			$("#designCaseNumber2").text('');
			$("#designCaseNumber2").text(data.designCaseNumber2==null?0:data.designCaseNumber2);
			//系测执行案例数
			$("#executeCaseNumber").text('');
			$("#executeCaseNumber").text(data.executeCaseNumber==null?0:data.executeCaseNumber);
			//版测执行案例数
			$("#executeCaseNumber2").text('');
			$("#executeCaseNumber2").text(data.executeCaseNumber2==null?0:data.executeCaseNumber2);
			//系测缺陷数
			$("#defectNum").empty(); 
			$("#defectNum").append( '<a class="a_style" onclick="showDefect(\''+ id +'\',\''+data.featureCode + '\',1 )">'+ data.defectNum +'</a>' );
			//版测缺陷数

			$("#defectNum2").empty();
			$("#defectNum2").append( '<a class="a_style" onclick="showDefect(\''+ id +'\',\''+data.featureCode + '\',2 )">'+ data.defectNum2 +'</a>' ); 
			
			
			showField( data.field )
			
			$("#checkhandSug").text('');
			$("#checkhandSug").text(data.handleSuggestion);
			$("#checkReqFeatureId").val(id);
			var type = '';
			if (data.createType == "1") {
				type = "自建";
				$("#createfiles").show();
				$("#synfiles").hide();
			} else if (data.createType == "2") {
				type = "同步";
				$("#synfiles").show();
				$("#createfiles").hide();
			} else if (data.createType == "0") {// aviyy 20201103
                type = "提前下发";
                $("#createfiles").show();
                $("#synfiles").hide();
            } else if (data.createType == "-1") {
                type = "生产问题";
                $("#createfiles").show();
                $("#synfiles").hide();
            }
			$("#checkcreateType").text(type);
			//下属工作任务的展开
			if (data.list != undefined) {
				for (var i = 0; i < data.list.length; i++) {
					$("#connectDiv").append('<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="getSee(' + data.list[i].id + ')">' + data.list[i].testTaskCode + '</a>' + ' ' + data.list[i].testTaskName + '</div>' +
						'<div class="def_col_36">实际工作情况：' + toStr(data.list[i].actualStartDate) + '~' + toStr(data.list[i].actualEndDate) + ' ' + toStr(data.list[i].actualWorkload) + '人天</div>' +
						'<div class="def_col_36">' + data.list[i].testTaskStatusName + ' ' + toStr(data.list[i].testUserName) + '</div></div>');
				}
			}
			//相关测试任务的展开
			if (data.brother != undefined) {
				for (var i = 0; i < data.brother.length; i++) {
					if (data.brother[i].featureCode == undefined) {
						data.brother[i].featureCode = "";
					}
					$("#brother_div").append('<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="initPage(' + data.brother[i].id + ')">' + data.brother[i].featureCode + '</a>   ' + data.brother[i].featureName + '</div>' +
						'<div class="def_col_36">实际工作情况：' + toStr(data.brother[i].actualStartDate) + '~' + toStr(data.brother[i].actualEndDate) + ' ' + toStr(data.brother[i].actualWorkload) + '人天</div>' +
						'<div class="def_col_36">' + data.brother[i].statusName + ' ' + toStr(data.brother[i].executeUserName) + ' 预排期：' + toStr(data.brother[i].windowName) + '</div>');
				}
			}
			//相关开发任务
			if(data.requirementList != undefined){
				if (data.requirementList.list.length > 0) {
					for (var i = 0; i < data.requirementList.list.length; i++) {
	//					if (data.brother[i].featureCode == undefined) {
	//						data.brother[i].featureCode = "";
	//					}
						$("#develop").append('<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="showDevTask(' + data.requirementList.list[i].id + ')">' + data.requirementList.list[i].featureCode + '</a>   ' + data.requirementList.list[i].featureName + '</div>' +
							'<div class="def_col_36">任务状态：' + data.requirementList.list[i].featureStatus + '</div>' +
							'<div class="def_col_36">开发管理岗：' + data.requirementList.list[i].userName + '</div>');
					}
				}
			}
			//相关附件显示
			if (data.attachements != undefined) {
				var _table = $("#checkFileTable");
				for (var i = 0; i < data.attachements.length; i++) {
					var _tr = '';
					var file_name = data.attachements[i].fileNameOld;
					var file_type = data.attachements[i].fileType;
					var _td_icon;
					//<i class="file-url">'+data.attachements[i].filePath+'</i>
					var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + data.attachements[i].fileS3Bucket + '</i><i class = "file-s3Key">' + data.attachements[i].fileS3Key + '</i></td>';
					switch (file_type) {
						case "doc":
						case "docx":_td_icon = '<img src="' + _icon_word + '" />'; break;
						case "xls":
						case "xlsx":_td_icon = '<img src=' + _icon_excel + ' />'; break;
						case "txt":_td_icon = '<img src="' + _icon_text + '" />'; break;
						case "pdf":_td_icon = '<img src="' + _icon_pdf + '" />'; break;
						case "png":
						case "jpeg":
						case "jpg":_td_icon = '<img src="' + _icon_img + '"/>'; break;
						default:_td_icon = '<img src="' + _icon_other + '" />'; break;
					}
					_tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download2(this)">' + _td_icon + " " + _td_name + '</tr>';

					_table.append(_tr);
				}
			}
			//扩展字段
			// $("#check_extendField1").text(data.check_extendField1);
			//备注
			_checkfiles = [];
			if (data.remarks != undefined) {
				var str = '';
				for (var i = 0; i < data.remarks.length; i++) {
					var style = '';
					if (i == data.remarks.length - 1) {
						style = ' lastLog';
					}
					str += '<div class="logDiv' + style + '"><div class="logDiv_title"><span class="orderNum"></span>' +
						'<span>' + data.remarks[i].userName + '  | ' + data.remarks[i].userAccount + '</span>&nbsp;&nbsp;&nbsp;<span>' + datFmt(new Date(data.remarks[i].createDate), "yyyy-MM-dd hh:mm:ss") + '</span></div>' +
						'<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><span>' + data.remarks[i].requirementFeatureRemark + '</span>' +
						'<div class="file-upload-list">';
					if (data.remarks[i].remarkAttachements.length > 0) {
						str += '<table class="file-upload-tb">';
						var _trAll = '';
						for (var j = 0; j < data.remarks[i].remarkAttachements.length; j++) {

							var _tr = '';
							var file_name = data.remarks[i].remarkAttachements[j].fileNameOld;
							var file_type = data.remarks[i].remarkAttachements[j].fileType;
							var _td_icon;
							//<i class="file-url">'+data.remarks[i].remarkAttachements[j].filePath+'</i>
							var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + data.remarks[i].remarkAttachements[j].fileS3Bucket + '</i><i class = "file-s3Key">' + data.remarks[i].remarkAttachements[j].fileS3Key + '</i></td>';
							switch (file_type) {
								case "doc":
								case "docx":_td_icon = '<img src="' + _icon_word + '" />'; break;
								case "xls":
								case "xlsx":_td_icon = '<img src=' + _icon_excel + ' />'; break;
								case "txt":_td_icon = '<img src="' + _icon_text + '" />'; break;
								case "pdf":_td_icon = '<img src="' + _icon_pdf + '" />'; break;
								case "png":
								case "jpeg":
								case "jpg":_td_icon = '<img src="' + _icon_img + '"/>'; break;
								default:_td_icon = '<img src="' + _icon_other + '" />'; break;
							}
							_tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download2(this)">' + _td_icon + _td_name + '</tr>';
							_trAll += _tr;
						}
						str += _trAll + '</table>';
					}
					str += '</div></div></div></div></div>';
				}
				$("#remarkBody").append(str);
			}
			//处理日志
			if (data.logs != undefined) {
				var str = '';
				for (var i = 0; i < data.logs.length; i++) {
					var style = '';
					if (i == data.logs.length - 1) {
						style = ' lastLog';
					}
					var addDiv = '';
					var logDetail = '';
					var style2 = '';
					if ((data.logs[i].logDetail == null || data.logs[i].logDetail == '') && (data.logs[i].logAttachements == null || data.logs[i].logAttachements.length <= 0)) {
						if (data.logs[i].logType != "新增测试任务") {
							logDetail = "未作任何修改";
						}
						if (logDetail == '') {
							style2 = 'style="display: none;"';
						}
						addDiv = '<br>';
					} else {
						logDetail = data.logs[i].logDetail;
						logDetail = logDetail.replace(/；/g, "<br/>");
					}
					str += '<div class="logDiv' + style + '"><div class="logDiv_title"><span class="orderNum"></span><span>' + data.logs[i].logType + '</span>&nbsp;&nbsp;&nbsp;' +
						'<span>' + data.logs[i].userName + '  | ' + data.logs[i].userAccount + '</span>&nbsp;&nbsp;&nbsp;<span>' + datFmt(new Date(data.logs[i].createDate), "yyyy-MM-dd hh:mm:ss") + '</span></div>' +
						'<div class="logDiv_cont" ><div class="logDiv_contBorder"><div class="logDiv_contRemark" ' + style2 + '><span>' + logDetail + '</span>' +
						'<div class="file-upload-list">';
					if (data.logs[i].logAttachements.length > 0) {
						str += '<table class="file-upload-tb">';
						var _trAll = '';
						for (var j = 0; j < data.logs[i].logAttachements.length; j++) {
							var attType = '';
							if (data.logs[i].logAttachements[j].status == 1) {//新增的日志
								attType = "<lable>新增附件：</lable>";
							} else if (data.logs[i].logAttachements[j].status == 2) {//删除的日志
								attType = "<lable>删除附件：</lable>";
							}
							var _tr = '';
							var file_name = data.logs[i].logAttachements[j].fileNameOld;
							var file_type = data.logs[i].logAttachements[j].fileType;
							var _td_icon;
							//<i class="file-url">'+data.logs[i].logAttachements[j].filePath+'</i>
							var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + data.logs[i].logAttachements[j].fileS3Bucket + '</i><i class = "file-s3Key">' + data.logs[i].logAttachements[j].fileS3Key + '</i></td>';
							switch (file_type) {
								case "doc":
								case "docx":_td_icon = '<img src="' + _icon_word + '" />'; break;
								case "xls":
								case "xlsx":_td_icon = '<img src=' + _icon_excel + ' />'; break;
								case "txt":_td_icon = '<img src="' + _icon_text + '" />'; break;
								case "pdf":_td_icon = '<img src="' + _icon_pdf + '" />'; break;
								case "png":
								case "jpeg":
								case "jpg":_td_icon = '<img src="' + _icon_img + '"/>'; break;
								default:_td_icon = '<img src="' + _icon_other + '" />'; break;
							}
							_tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download2(this)">' + attType + _td_icon + _td_name + '</tr>';
							_trAll += _tr;
						}
						str += _trAll + '</table>';
					}

					str += '</div></div>' + addDiv + '</div></div></div>';

				}
				$("#handleLogs").append(str);
			}
			$('.is_new_project').show();
			if(data.projectType == 2){
				$('.is_new_project').hide();
			}
			$("#loading").css('display', 'none');

		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	});
	modalType = 'check'; 
}

//相关开发任务详情
function showDevTask(id){
	var id = id;
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/getOneDevTask2",
		type:"post",
		dataType:"json",
		data:{
			"id":id
		},
		success: function(data) {
			$("#loading").css('display','none'); 
			$("#checkoutrequirementDiv").hide();
			$("#checkDefectDiv").hide();
			$("#checkRequstNumberDiv").hide();
			$("#checkDevSystemName").text('');
			$("#executeProjectGroupId").text('');
			$("#checkDevDeptName").text('');
			$("#checkDevFiles").val('');
			$("#checkreqFeaturePriority").text('');
			$("#checkDevFileTable").empty();
			$("#remarkBody2").empty();
			$("#remarkDetail").val('');
			$("#checkAttTable").empty();
			$("#handleLogs2").empty();
			$("#checkDefect").empty();
			$("#checkSprintName").text('');
			$("#checkStoryPoint").text('');
			
			$("#SdevCode2").text( toStr(data.featureCode));
			$("#SdevName2").text( toStr(data.featureName));
			
			$("#checkdevTaskOverview").text(data.featureOverview);
			$("#checkreqFeaturePriority").text(data.requirementFeaturePriority);
			$("#checkSprintName").text(data.sprintName);
			$("#checkStoryPoint").text(data.storyPoint);
			
			var statusName = '';
			for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
                if(devtaskStatusList[i].valueCode == data.requirementFeatureStatus){
                	statusName=  devtaskStatusList[i].valueName;
             	   break;
                }
              }
			$("#checkdevTaskStatus").text(statusName);
			
			$("#checkdevManPost2").text("");
			$("#checkdevManPost2").text(data.manageUserName);
			$("#checkexecutor2").text("");
			$("#checkexecutor2").text(data.executeUserName);
			$("#executeProjectGroupId").text("");
			$("#executeProjectGroupId").text(data.executeProjectGroupName);

			$("#checkDevSystemName").text("");
			$("#checkDevSystemName").text(data.systemName);

			$("#checkoutrequirement2").text("");
			$("#checkRequstNumber").text("");
			
			$("#checkWindowName").text("");
			$("#checkWindowName").text(data.windowName);
			$("#checkSystemVersion").text("");
			$("#checkSystemVersion").text(toStr(data.versionName)+"-->"+toStr(data.systemScmBranch));
			
			$("#checkItcdReqId2").val("");
			$("#checkItcdReqId2").val(data.itcdRequrementId);
			$("#checktaskId2").val("");
			$("#checktaskId2").val(data.taskId);
			
			if(data.requirementFeatureSource!=undefined){
				if(data.requirementFeatureSource==1){
					$("#checkoutrequirementDiv").show();
					$("#checkDefectDiv").hide();
					$("#checkRequstNumberDiv").hide();
					$("#checkoutrequirement2").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
				}else if(data.requirementFeatureSource==2){
					$("#checkoutrequirementDiv").hide();
					$("#checkDefectDiv").hide();
					$("#checkRequstNumberDiv").show();
					$("#checkRequstNumber").text(data.questionNumber);
				}else if(data.requirementFeatureSource==3){
					$("#checkoutrequirementDiv").hide();
					$("#checkDefectDiv").show();
					$("#checkRequstNumberDiv").hide();
					var dftName='';
					for(var i=0;i<data.defectInfos.length;i++){
						if(data.defectInfos[i].requirementFeatureId == id){
							//var obj = JSON.stringify(    data.defectInfos[i]   ).replace(/"/g, '&quot;');
							dftName+= '<a class="a_style" onclick="showDefect('+data.defectInfos[i].id+')"> '+data.defectInfos[i].defectCode+'</a>,';
						}
					}
					$("#checkDefect").append(dftName.substring(0,dftName.length-1));
				}
			}
			$("#checkDevDeptName").text(data.deptName);
			var status = "";
			if(data.temporaryStatus =="1"){
				status = "是";
			}else if(data.temporaryStatus =="2"){
				status = "否";
			}
			$("#checktemporaryTask2").text(status);//1是2否
			$("#checkpreStartDate").text('');
			$("#checkpreStartDate").text(data.planStartDate);
			$("#checkpreEndDate").text('');
			$("#checkpreEndDate").text(data.planEndDate);
			$("#checkpreWorkload").text('');
			$("#checkpreWorkload").text(data.planWorkload);
			$("#checkactStartDate").text('');
			$("#checkactStartDate").text(data.actualStartDate);
			$("#checkactEndDate").text('');
			$("#checkactEndDate").text(data.actualEndDate);
			$("#checkactWorkload").text('');
			$("#checkactWorkload").text(data.actualWorkload);
			$("#checkhandSug2").text('');
			$("#checkhandSug2").text(data.handleSuggestion);
			$("#checkReqFeatureId2").val(id);
			var type = '';
			if(data.createType == "1" ){
				type = "自建";
				$("#createfiles2").show();
				$("#synfiles2").hide();
			} else if(data.createType == "2"){
				type = "同步";
				$("#synfiles2").show();
				$("#createfiles2").hide();
			} else if (data.createType == "3") {//TODO aviyy
                type = "提前下发";
                $("#createfiles2").show();
                $("#synfiles2").hide();
            } else if (data.createType == "4") {
                type = "生产问题确认";
                $("#createfiles2").show();
                $("#synfiles2").hide();
            }
			$("#checkcreateType2").text(type);
			//相关附件显示
			if(data.attachements!=undefined){
				var _table = $("#checkDevFileTable");
				for(var i=0;i<data.attachements.length;i++){
					var _tr = '';
					var file_name = data.attachements[i].fileNameOld;
					var file_type = data.attachements[i].fileType;
					var _td_icon;
					var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.attachements[i].fileS3Bucket+'</i><i class = "file-s3Key">'+data.attachements[i].fileS3Key+'</i></td>';
					switch(file_type){
						case "doc":
						case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
						case "xls":
						case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
						case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
						case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
						case "png":
						case "jpeg":
						case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
						default:_td_icon = '<img src="'+ _icon_other+'" />';break;
					}
					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download2(this)">'+_td_icon+" "+_td_name+'</tr>'; 
					
					_table.append(_tr);  
				}
			}
			//备注
			 _checkfiles2 = [];
			if(data.remarks!=undefined){
				var str ='';
				for(var i=0;i<data.remarks.length;i++){
					var style= '';
					if(i==data.remarks.length-1){
						style= ' lastLog';
					}
				 str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span>'+
				'<span>'+data.remarks[i].userName+'  | '+data.remarks[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.remarks[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
			    '<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><span>'+data.remarks[i].requirementFeatureRemark+'</span>'+
				'<div class="file-upload-list">';
				if(data.remarks[i].remarkAttachements.length>0){
					str +='<table class="file-upload-tb">';
					var _trAll = '';
					for(var j=0;j<data.remarks[i].remarkAttachements.length;j++){
						
						var _tr = '';
						var file_name = data.remarks[i].remarkAttachements[j].fileNameOld;
						var file_type = data.remarks[i].remarkAttachements[j].fileType;
						var _td_icon;
						var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.remarks[i].remarkAttachements[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.remarks[i].remarkAttachements[j].fileS3Key+'</i></td>';
						switch(file_type){
							case "doc":
							case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
							case "xls":
							case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
							case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
							case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
							case "png":
							case "jpeg":
							case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
							default:_td_icon = '<img src="'+ _icon_other+'" />';break;
						}
					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download2(this)">'+_td_icon+_td_name+'</tr>'; 
					_trAll +=_tr;
					}
					str+= _trAll+'</table>';
				}
				
				str += '</div></div></div></div></div>';
						
			}
				$("#remarkBody2").append(str);
			
			}
			//处理日志
			if(data.logs!=undefined){
				var str ='';
				for(var i=0;i<data.logs.length;i++){
					var style= '';
					if(i==data.logs.length-1){
						style= ' lastLog';
					}
					var addDiv = '';
					var logDetail = '';
					var style2 = '';
					
					if((data.logs[i].logDetail==null || data.logs[i].logDetail=='')&&(data.logs[i].logAttachements==null || data.logs[i].logAttachements.length<=0)){
						if(data.logs[i].logType!="新增开发任务"){
							logDetail = "未作任何修改";
						}
						if(logDetail==''){
							style2= 'style="display: none;"';
						}
						addDiv = '<br>';
					}else{
						logDetail = data.logs[i].logDetail;
						logDetail=logDetail.replace(/；/g,"<br/>");
					}
					
				
				 str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span><span>'+data.logs[i].logType+'</span>&nbsp;&nbsp;&nbsp;'+
				'<span>'+data.logs[i].userName+'  | '+data.logs[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.logs[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
			    '<div class="logDiv_cont" ><div class="logDiv_contBorder"><div class="logDiv_contRemark" '+style2+'><span>'+logDetail+'</span>'+
				'<div class="file-upload-list">';
				if(data.logs[i].logAttachements.length>0){
					str +='<table class="file-upload-tb">';
					var _trAll = '';
					for(var j=0;j<data.logs[i].logAttachements.length;j++){
						var attType = '';
						if(data.logs[i].logAttachements[j].status == 1){//新增的日志
							attType = "<lable>新增附件：</lable>";
						}else if(data.logs[i].logAttachements[j].status == 2){//删除的日志
							attType = "<lable>删除附件：</lable>";
						}
						var _tr = '';
						var file_name = data.logs[i].logAttachements[j].fileNameOld;
						var file_type = data.logs[i].logAttachements[j].fileType;
						var _td_icon;
						var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.logs[i].logAttachements[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.logs[i].logAttachements[j].fileS3Key+'</i></td>';
						switch(file_type){
							case "doc":
							case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
							case "xls":
							case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
							case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
							case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
							case "png":
							case "jpeg":
							case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
							default:_td_icon = '<img src="'+ _icon_other+'" />';break;
						}
					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download2(this)">'+attType+_td_icon+_td_name+'</tr>'; 
					_trAll +=_tr;
					}
					str+= _trAll+'</table>';
				}
				
				str += '</div></div>'+addDiv+'</div></div></div>';
						
			}
				$("#handleLogs2").append(str);
			}
			$("#devTask").modal("show");
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
	modalType = 'dev'; 
}

//查看需求的附件
function showReqFile(){
	var requirementId =  $("#checkItcdReqId2").val();
	if(requirementId== ''){
		layer.alert('该任务所关联的需求下没有附件！', {icon: 0});
		return;
	}
	layer.open({
	  type: 2,
	  area: ['700px', '450px'],
	  fixed: false, //不固定
	  maxmin: true,
	  btnAlign: 'c',
	  title:"相关需求附件",
	  content: reqAttUrl+"?reqId="+requirementId+"&reqtaskId="+requirementId+"&taskreqFlag=requirement"
	});
}

//相关开发任务附件
function showFile2() {
	var reqFeatureId = $("#checktaskId2").val();
	var requirementId = $("#checkItcdReqId2").val();

	if (requirementId == '') {
		layer.alert('该任务下没有附件！', { icon: 0 });
		return;
	}
	layer.open({
		type: 2,
		area: ['700px', '450px'],
		fixed: false, //不固定
		maxmin: true,
		btnAlign: 'c',
		title: "相关附件",
		content: reqAttUrl + "?reqId=" + htmlEncodeJQ(requirementId) + "&reqtaskId=" + htmlEncodeJQ(reqFeatureId) + "&taskreqFlag=task"
	});
}

//获取开发任务状态
function getReqFeatureStatus(){
	var devtaskStatusList = '';
	$.ajax({
		url:"/devManage/devtask/getReqFeatureStatus",
		dataType:"json",
		type:"post",
		async:false, 
		success:function(data){
			devtaskStatusList = data.reqFeatureStatus;
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		}
		
	});
	return devtaskStatusList;
}

//提交开发任务备注
function saveDevRemark(){
	var remark = $("#remarkDetail").val();
	var id = $("#checkReqFeatureId2").val();
	if($("#remarkDetail").val()==''&& $("#checkDevFiles").val()==''){
		layer.alert('备注内容和附件不能同时为空！！！', {icon: 0});
		return;
	}
	/*var _fileStr = [];
	for(var i=0;i<_checkfiles.length;i++){
		_fileStr.push(_checkfiles[i].fileNameOld);
	}
	var s = _fileStr.join(",")+",";
	for(var i=0;i<_fileStr.length;i++){
		if(s.replace(_fileStr[i]+",","").indexOf(_fileStr[i]+",")>-1){
			layer.alert(_fileStr[i]+"文件重复！！！", {icon: 0});
			return ;
		}
	}*/
	$.ajax({
		url:"/devManage/devtask/addRemark",
		dataType:"json",
		type:"post",
		data:{
			id:id,
			requirementFeatureRemark:remark,
			attachFiles :$("#checkDevFiles").val() 
		},
		success : function(data){
			if(data.status == "success"){
				layer.alert('保存成功！', {icon: 1});
				showDevTask(id);
				 _checkfiles2 = [];
				$("#checkDevFiles").val('');
			}else if(data.status =="2"){
				layer.alert('保存失败！！！', {icon: 2});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
}

//查看 实施状态
function show_status(rows) {
	for (var i = 0, len = testtaskStatusList.length; i < len; i++) {
		if (testtaskStatusList[i].value == rows) {
			var value = testtaskStatusList[i].innerHTML;
			break;
		}
	}
	switch (rows) {
		case '01':return "<span class='doing1'>" + value + "</span>";break;
		case '02':return "<span class='doing2'>" + value + "</span>";break;
		case '03':return "<span class='doing3'>" + value + "</span>";break;
		case '00':return "<span class='doing4'>" + value + "</span>";break;
		case '04':return "<span class='doing5'>" + value + "</span>";break;
		case '05':return "<span class='doing6'>" + value + "</span>";break;
		case '06':return "<span class='doing7'>" + value + "</span>";break;
		case '07':return "<span class='doing8'>" + value + "</span>";break;
		case '08':return "<span class='doing9'>" + value + "</span>";break;
		case '09':return "<span class='doing10'>" + value + "</span>";break;
		case '10':return "<span class='doing11'>" + value + "</span>";break;
		case '11':return "<span class='doing12'>" + value + "</span>";break;
		default:return "<span class='doing'>" + value + "</span>";break;
	}
}
function showField( data ){ 
	$( "#checkEditField" ).empty();
	if(data != null && data != "undefined" && data.length > 0 ){
        for( var i=0;i<data.length;i++ ){
        	var str='<div class="form-group col-md-6">'+
				'<label class="control-label fontWeihgt">'+ data[i].label +'：</label>'+
				'<label class="control-label font_left">'+ data[i].valueName +'</label>'+
			'</div>';
        	$( "#checkEditField" ).append( str );  
        }
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
//提交备注
function saveRemark() {
	
	if ($("#tyaskRemark").val() == '' && $("#checkfiles").val() == '') {
		layer.alert('备注内容和附件不能同时为空！！！', { icon: 0 });
		return;
	}
	/*var _fileStr = [];
	for(var i=0;i<_checkfiles.length;i++){
		_fileStr.push(_checkfiles[i].fileNameOld);
	}
	var s = _fileStr.join(",")+",";
	for(var i=0;i<_fileStr.length;i++){
		if(s.replace(_fileStr[i]+",","").indexOf(_fileStr[i]+",")>-1){
			layer.alert(_fileStr[i]+"文件重复！！！", {icon: 0});
			return ;
		}
	}*/
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
				initPage(id);
				_checkfiles = [];
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
			} else if (modalType == 'dev') {
				fileList = _checkfiles2;
			}

			for (var j = 0; j < fileList.length; j++) {
				if (fileList[j].fileNameOld == file.name) {
					layer.alert(file.name + "文件已存在！！！", { icon: 0 });
					continue outer;
				}
			}
			formFile.append("files", file);
			//读取文件
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

			//列表展示
			var _tr = '';
			var file_name = file.name.split("\.")[0];
			var file_type = file.name.split("\.")[1];
			var _td_icon;
			var _td_name = '<span >' + file.name + '</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
			var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
			switch (file_type) {
				case "doc":
				case "docx":_td_icon = '<img src="' + _icon_word + '" />'; break;
				case "xls":
				case "xlsx":_td_icon = '<img src=' + _icon_excel + ' />'; break;
				case "txt":_td_icon = '<img src="' + _icon_text + '" />'; break;
				case "pdf":_td_icon = '<img src="' + _icon_pdf + '" />'; break;
				case "png":
				case "jpeg":
				case "jpg":_td_icon = '<img src="' + _icon_img + '"/>'; break;
				default:_td_icon = '<img src="' + _icon_other + '" />'; break;
			}
			var _table = $(this).parent(".file-upload-select").next(".file-upload-list").children("table");
			_tr += '<tr><td><div class="fileTb">' + _td_icon + '  ' + _td_name + _td_opt + '</tr>';
			_table.append(_tr);

		}
		//上传文件
		$("#loading",window.parent.document).css('display','block');
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
					} else if (modalType == 'dev') {
						_checkfiles2.push(data[k]);
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
					$("#editfiles").val(JSON.stringify(_editfiles));
					clearUploadFile('edituploadFile');
				} else if (modalType == 'handle') {
					$("#handlefiles").val(JSON.stringify(_handlefiles));
					clearUploadFile('handleuploadFile');
				} else if (modalType == 'check') {
					$("#checkfiles").val(JSON.stringify(_checkfiles));
					clearUploadFile('checkuploadFile');
				} else if (modalType == 'dev') {
					$("#checkDevFiles").val(JSON.stringify(_checkfiles2));
					clearUploadFile('checkuploadFile2');
				}
				$("#loading",window.parent.document).css('display','none');
			},
			error: function () {
				$("#loading",window.parent.document).css('display','none');
				layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
			}
		});
	});
}

//清空上传文件
function clearUploadFile(idName) {
	//$(idName).wrap('<form></form>');
	//$(idName).unwrap();
	$('#' + idName + '').val('');
}

//移除上传文件
function delFile(that) {
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
	/*var url = $(that).parent().prev().children().children(".file-url").text(); 
	var fileS3Bucket = $(that).parent().prev().children().children(".file-bucket").text(); 
	$.ajax({
        type:'post',
        url:'/testManage/testtask/delFile',
        data:{
        		url:url,
        		fileS3Bucket :fileS3Bucket,
        		fileS3Key:fileS3Key
        	 },
        success:function(data){
        	if(data.status=="success"){
        		alert("删除成功！");
        	}else if(data.fail == "2"){
        		alert("删除失败！！！");
        	}
        }
    });*/
	$(that).parent().parent().remove();
	removeFile(fileS3Key);
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
	} else if (modalType == 'dev') {
		var _file = $("#checkDevFiles").val();
		if (_file != "") {
			var files = JSON.parse(_file);
			for (var i = 0, len = files.length; i < len; i++) {
				if (files[i].fileS3Key == fileS3Key) {
					Array.prototype.splice.call(files, i, 1);
					break;
				}
			}
			_checkfiles2 = files;
			$("#checkDevFiles").val(JSON.stringify(files));
		}
	}

}

//文件下载
function download2(that) {
	var fileS3Bucket = $(that).children(".file-bucket").text();
	var fileS3Key = $(that).children(".file-s3Key").text();
	var fileNameOld = $(that).children("span").text();
	var url = encodeURI("/testManage/testtask/downloadFile?fileS3Bucket=" + fileS3Bucket + "&fileS3Key=" + fileS3Key + "&fileNameOld=" + fileNameOld);
	window.location.href = url;

}

//查看页面查看附件
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
		fixed: false, //不固定
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
function showDefect(id ,featureCode ,status) {
	var reqFId =  id;
	var reqFCode =  featureCode;
	var curl = defectMenuUrl.split("?")[0] + "?reqFiD=" + htmlEncodeJQ(reqFId) + "&reqFName=" + htmlEncodeJQ(reqFCode)+ "&status=" + htmlEncodeJQ(status);
	window.parent.parent.toPageAndValue(defectMenuName, defectMenuId, curl); 
}