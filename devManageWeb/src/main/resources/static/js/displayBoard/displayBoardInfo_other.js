//created by ztt
var _icon_word ="../static/images/devtask/word.png";
var _icon_excel ="../static/images/devtask/excel.png";
var _icon_text = "../static/images/devtask/text.png";
var _icon_pdf = "../static/images/devtask/pdf.png";
var _icon_img = "../static/images/devtask/img.png";
var _icon_other = "../static/images/devtask/other.png";
var _dcheckfiles = [];
var _wcheckfiles = [];
var _SeeFiles =[];
var devtaskStatusList =[];
var modified  = "<span>&nbsp;修改为：&nbsp;</span>";
//开发任务弹框
function showReqFeature(id){
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
			$("#checksystemName").text('');
			$("#checkdeptName").text('');
			$("#dcheckfiles").val('');
			$("#checkreqFeaturePriority").text('');
			$("#connectDiv").empty();
			$("#brother_div").empty();
			$("#defect_div").empty();
			$("#checkFileTable").empty();
			$("#remarkBody").empty();
			$("#remarkDetail").val('');
			$("#dcheckAttTable").empty();
			$("#handleLogs").empty();
			$("#brother_div").empty();
			$("#checkSprintName").text('');
			$("#checkStoryPoint").text('');
			$("#checkDefect").empty();
			$("#checkdevTaskTitle").text( toStr(data.featureCode) + toStr(data.featureName) );
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
			
			$("#checkdevManPost").text("");
			$("#checkdevManPost").text(data.manageUserName);
			$("#checkexecutor").text("");
			$("#checkexecutor").text(data.executeUserName);
			$("#checksystemName").text("");
			$("#checksystemName").text(data.systemName);
			$("#checkoutrequirement").text("");
			$("#checkRequstNumber").text("");
			
			$("#checkWindowName").text("");
			$("#checkWindowName").text(data.windowName);
			$("#checkSystemVersion").text("");
			$("#checkSystemVersion").text(toStr(data.versionName)+"-->"+toStr(data.systemScmBranch));
			
			$("#checkItcdReqId").val("");
			$("#checkItcdReqId").val(data.itcdRequrementId);
			$("#checktaskId").val("");
			$("#checktaskId").val(data.taskId);
			
			//开发任务来源
			if(data.requirementFeatureSource!=undefined){
				if(data.requirementFeatureSource==1){//业务需求
					$("#checkoutrequirementDiv").show();
					$("#checkDefectDiv").hide();
					$("#checkRequstNumberDiv").hide();
					$("#checkoutrequirement").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
				}else if(data.requirementFeatureSource==2){//生产问题
					$("#checkoutrequirementDiv").hide();
					$("#checkDefectDiv").hide();
					$("#checkRequstNumberDiv").show();
					$("#checkRequstNumber").text(data.questionNumber);
				}else if(data.requirementFeatureSource==3){//测试缺陷
					$("#checkoutrequirementDiv").hide();
					$("#checkDefectDiv").show();
					$("#checkRequstNumberDiv").hide();
					var dftName='';
					for(var i=0;i<data.defectInfos.length;i++){
						if(data.defectInfos[i].requirementFeatureId == id){
							var obj = JSON.stringify(    data.defectInfos[i]   ).replace(/"/g, '&quot;');
							//dftName+= '<a class="a_style" onclick="showDefect('+obj+')"> '+data.defectInfos[i].defectCode+'</a>,';
							dftName+= data.defectInfos[i].defectCode+',';
						}
					}
					$("#checkDefect").append(dftName.substring(0,dftName.length-1));
				}
			}
			$("#checkdeptName").text(data.deptName);
			var status = "";
			if(data.temporaryStatus =="1"){
				status = "是";
			}else if(data.temporaryStatus =="2"){
				status = "否";
			}
			$("#checktemporaryTask").text(status);//1是2否
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
			$("#checkhandSug").text('');
			$("#checkhandSug").text(data.handleSuggestion);
			$("#checkReqFeatureId").val(id);
			var type = '';
			if(data.createType == "1" ){
				type = "自建";
				$("#createfiles").show();
				$("#synfiles").hide();
			}else if(data.createType == "2"){
				type = "同步";
				$("#synfiles").show();
				$("#createfiles").hide();
			}
			$("#checkcreateType").text(type);
			//下属工作任务的显示
			if(data.list!=undefined){
				for(var i=0;i<data.list.length;i++){
					$("#connectDiv").append( 
							//'<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="getSee('+data.list[i].id+')">'+data.list[i].devTaskCode+'</a>'+'   '+data.list[i].devTaskName+'</div>'+
							'<div class="rowdiv "><div class="def_col_36 fontWeihgt">'+data.list[i].devTaskCode+'   '+data.list[i].devTaskName+'</div>'+
							'<div class="def_col_36">预估工作情况：'+toStr(data.list[i].planStartDate)+'~'+toStr(data.list[i].planEndDate)+' '+toStr(data.list[i].planWorkLoad)+'人天</div>'+
							'<div class="def_col_36">实际工作情况：'+toStr(data.list[i].actualStartDate)+'~'+toStr(data.list[i].actualEndDate)+' '+toStr(data.list[i].actualWorkload)+'人天</div>'+
							'<div class="def_col_36">'+data.list[i].devtaskStatusName+' '+toStr(data.list[i].devUsername)+'</div></div>');
				}
			}
			//相关开发任务的显示
			if(data.brother!=undefined){
				for(var i=0;i<data.brother.length;i++){
					$("#brother_div").append( 
							//'<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="showReqFeature('+data.brother[i].id+')">'+toStr(data.brother[i].featureCode)+'</a>'+'  '+data.brother[i].featureName+'</div>'+
							'<div class="rowdiv "><div class="def_col_36 fontWeihgt">'+toStr(data.brother[i].featureCode)+'  '+data.brother[i].featureName+'</div>'+
							'<div class="def_col_36">实际工作情况：'+toStr(data.brother[i].actualStartDate)+'~'+toStr(data.brother[i].actualEndDate)+' '+toStr(data.brother[i].actualWorkload)+'人天</div>'+
							'<div class="def_col_36">'+data.brother[i].statusName+' '+toStr(data.brother[i].executeUserName)+' 预排期：'+toStr(data.brother[i].windowName)+'</div>');
				}
			} 
			//相关附件显示
			if(data.attachements!=undefined){
				var _table = $("#checkFileTable");
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
					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(this)">'+_td_icon+" "+_td_name+'</tr>'; 
					
					_table.append(_tr);  
				}
			}
			//备注
			 _dcheckfiles = [];
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
					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(this)">'+_td_icon+_td_name+'</tr>'; 
					_trAll +=_tr;
					}
					str+= _trAll+'</table>';
				}
				
				str += '</div></div></div></div></div>';
						
			}
				$("#remarkBody").append(str);
			
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
					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(this)">'+attType+_td_icon+_td_name+'</tr>'; 
					_trAll +=_tr;
					}
					str+= _trAll+'</table>';
				}
				
				str += '</div></div>'+addDiv+'</div></div></div>';
						
			}
				$("#handleLogs").append(str);
			}
			
		}
	});
	modalType = 'dcheck';
    $("#checkDevTask").modal("show");
}
//提交备注
function saveRemark(){
	var remark = $("#remarkDetail").val();
	var id = $("#checkReqFeatureId").val();
	if($("#remarkDetail").val()==''&& $("#dcheckfiles").val()==''){
		layer.alert('备注内容和附件不能同时为空！！！', {icon: 0});
		return;
	}
	$.ajax({
		url:"/devManage/devtask/addRemark",
		dataType:"json",
		type:"post",
		data:{
			id:id,
			requirementFeatureRemark:remark,
			attachFiles :$("#dcheckfiles").val() 
		},
		success : function(data){
			if(data.status == "success"){
				layer.alert('保存成功！', {icon: 1});
				showReqFeature(id);
				 _dcheckfiles = [];
				$("#dcheckfiles").val('');
			}else if(data.status =="2"){
				layer.alert('保存失败！！！', {icon: 2});
			}
		}
	});
}
//文件下载
function download(that){
	var fileS3Bucket = $(that).children(".file-bucket").text();
	var fileS3Key = $(that).children(".file-s3Key").text();
	var fileNameOld = $(that).children("span").text();
	var url = encodeURI("/devManage/devtask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld);
	window.location.href = url;
	
}
function showThisDiv(This,num){
	if( $(This).hasClass("def_changeTit") ){
		$("#titleOfwork .def_controlTit").addClass("def_changeTit");
		$(This).removeClass("def_changeTit");
		if( num==1 ){
			$(".dealLog").css("display","none");
			$(".workRemarks").css("display","block");
		}else if( num==2 ){
			$(".dealLog").css("display","block");
			$(".workRemarks").css("display","none");
		}
	}   
}
function showFile(){
	var reqFeatureId = $("#checktaskId").val();
	var requirementId =  $("#checkItcdReqId").val();
	
	if(requirementId== ''){
		layer.alert('该任务下没有附件！', {icon: 0});
		return;
	}
	layer.open({
	  type: 2,
	  area: ['700px', '450px'],
	  fixed: false, //不固定
	  maxmin: true,
	  btnAlign: 'c',
	  title:"相关附件",
	  content: reqAttUrl+"?reqId="+requirementId+"&reqtaskId="+reqFeatureId+"&taskreqFlag=task"
	});
}
//查看需求的附件
function showReqFile(){
	var requirementId =  $("#checkItcdReqId").val();
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

/*==================================工作任务弹框===========================*/
function  getSee(id){
    $.ajax({
		method:"post", 
		url:"/devManage/worktask/getSeeDetail",
		data:{id:id},
		success : function(data) {
			selectdetail(data)
		}
	});
}
function selectdetail(data){
	$("#loading").css('display','block');
	$("#wcheckAttTable").empty();
	$("#SeeFileTable").empty();
	$("#taskRemark").empty();
	$("#wcheckfiles").val("");
	$("#whandleLogs").empty();
	var map=data.dev;
	$("#SdevCode").html("");
	$("#SdevName").html("");
	$("#SdevOverview").html("");
	$("#SStatus").html("");
	$("#devuserID").html("");
	
	$("#wfeatureName").html("");
	
	$("#wfeatureOverview").html("");
	$("#wrequirementFeatureStatus").html("");
	$("#wmanageUserId").html("");
	$("#wexecuteUserId").html("");
	$("#wsystemId").html("");
	$("#wrequirementSource").html("");
	$("#wrequirementType").html("");
	$("#wrequirementPriority").html("");
	$("#wrequirementPanl").html("");
	$("#wrequirementStatus").html("");
	$("#wapplyUserId").html("");
	$("#wapplyDeptId").html("");
	$("#expectOnlineDate").html("");
	$("#planOnlineDate").html("");
	$("#lastUpdateDate3").html("");
	$("#createDate3").html("");
	$("#openDate").html("");
	$("#tyaskRemark").val("");
	$("#wplanStartDate").html("");
	$("#wplanEndDate").html("");
	$("#wSplanWorkload").html("");
	$("#wactualEndDate").html("");
	$("#wactualStartDate").html("");
	$("#wactualWorkload").html("");
	$("#wrequirementName").html('');
	$("#sprintName").text("");
	//增加优先级
    $("#viewDevTaskPriority").text("");
    $("#check_devTaskType").text("");
    $("#viewDevTaskPriority").html(map.devTaskPriority);
    $("#check_devTaskType").html(data.devTaskType);
	$("#wrequirementName").html(map.requirementName);
		$("#SdevCode").html(map.devTaskCode);
		$("#SdevName").html(map.devTaskName);
		$("#SdevOverview").html(map.devTaskOverview);
		var devTaskStatus=map.devStatus;
		$("#SStatus").html(devTaskStatus);
		
		$("#DevTaskID").val(map.id);
		$("#devuserID").html(map.devuserName);
		
		$("#wcreateBy").html(map.createName);
		
		$("#wcreateDate").html(datFmt(new Date(map.createDate),"yyyy年MM月dd"));
		if(map.planStartDate!=null){
			var planstartDate=datFmt(new Date(map.planStartDate),"yyyy年MM月dd");
			$("#wplanStartDate").html(planstartDate);
		}
		if(map.planEndDate!=null){
			var planEndDate=datFmt(new Date(map.planEndDate),"yyyy年MM月dd");
			$("#wplanEndDate").html(planEndDate);
		}
		
		if(map.actualStartDate!=null){
			var actualStartDate=datFmt(new Date(map.actualStartDate),"yyyy年MM月dd");
			$("#wactualStartDate").html(actualStartDate);
		}
		if(map.actualEndDate!=null){
			var actualEndDate=datFmt(new Date(map.actualEndDate),"yyyy年MM月dd");
			$("#wactualEndDate").html(actualEndDate);
		}
		$("#wactualWorkload").html(map.actualWorkload);
		$("#wfeatureName").html(map.featureName);
		$("#wSplanWorkload").html(map.planWorkload);
		$("#wfeatureOverview").html(map.featureOverview);
		var featureStatus=map.requirementFeatureStatus;
		$("#wrequirementFeatureStatus").html(featureStatus);
		
		
		$("#wmanageUserId").html(map.manageUserName);
		$("#wexecuteUserId").html(map.executeUserName);
		$("#wsystemId").html(map.systemName);
		
		$("#wrequirementSource").html(map.requirementSource);
		$("#wrequirementType").html(map.requirementType);
		$("#wrequirementPriority").html(map.requirementPriority);
		$("#wrequirementPanl").html(map.requirementPanl);
		$("#wrequirementStatus").html(map.requirementStatus);
		$("#wapplyUserId").html(map.applyUserName);
		$("#wapplyDeptId").html(map.applyDeptName);
		if(map.sprintName!=null){
			$("#sprintNameDiv").css('display','block');
			$("#sprintName").html(map.sprintName);
		}else{
			$("#sprintNameDiv").css('display','none');
		}
		if(map.expectOnlineDate!=null){
			var expectOnlineDate=datFmt(new Date(map.expectOnlineDate),"yyyy年MM月dd");
			$("#expectOnlineDate").html(expectOnlineDate);
		}
		if(map.planOnlineDate!=null){
			var planOnlineDate=datFmt(new Date(map.planOnlineDate),"yyyy年MM月dd");
			$("#planOnlineDate").html(planOnlineDate);
		}
		if(map.openDate!=null){
			var openDate=datFmt(new Date(map.openDate),"yyyy年MM月dd");
			$("#openDate").html(openDate);
		}
		if(map.createDate3!=null){
			var createDate3=datFmt(new Date(map.createDate3),"yyyy年MM月dd");
			$("#createDate3").html(createDate3);
		}
		
		if(map.lastUpdateDate3!=null){
			var lastUpdateDate3=datFmt(new Date(map.lastUpdateDate3),"yyyy年MM月dd");
			$("#lastUpdateDate3").html(lastUpdateDate3);
		}
		$("#SeeFiles").val("");
		if(data.attachements!=undefined){
			var _table=$("#SeeFileTable");
			var attMap=data.attachements;
			//var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
			for(var i=0 ;i<attMap.length;i++){
				var _tr = '';
				var file_name = attMap[i].fileNameOld;
				var file_type = attMap[i].fileType;
				var file_id =  attMap[i].id;
				var _td_icon;
				//<i class="file-url">'+data.attachements[i].filePath+'</i>
				var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
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
				var row =  JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
				_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download2('+row+')">'+_td_icon+" "+_td_name+'</tr>'; 
				_table.append(_tr); 
				_SeeFiles.push(attMap[i]);
				$("#SeeFiles").val(JSON.stringify(_SeeFiles));
			}
		}
		
		//备注
		if(data.rmark!=undefined){
			var str ='';
			for(var i=0;i<data.rmark.length;i++){
				var style= '';
				if(i==data.rmark.length-1){
					style= ' lastLog';
				}
			 str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span>'+
			'<span>'+data.rmark[i].userName+'  | '+data.rmark[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.rmark[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
		    '<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><span>'+data.rmark[i].devTaskRemark+'</span>'+
			'<div class="file-upload-list">';
			 if(data.Attchement.length>0){
				 str +='<table class="file-upload-tb">';
					var _trAll = '';
					for(var j=0;j<data.Attchement.length;j++){
						var _tr = '';
						if((data.Attchement[j].devTaskRemarkId)==(data.rmark[i].id)){
							
							var file_type = data.Attchement[j].fileType;
							var file_name = data.Attchement[j].fileNameOld;
							var _td_icon;
							var _td_name = '<span>'+file_name+'</span>';
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
							var row =  JSON.stringify( data.Attchement[j]).replace(/"/g, '&quot;');
							_tr+='<tr><div class="fileTb" style="cursor:pointer" onclick ="download2('+row+')">'+_td_icon+_td_name+"</tr>"; 
						
					}
						if(_tr!=undefined){
							_trAll +=_tr;
						}
						
					}
					str+= _trAll+'</table>';
					
				}
			
			str += '</div></div></div></div></div>';
					
		}
			$("#taskRemark").append(str);
		
		}
		//处理日志
		if(data.logs!=undefined){
			var _span="";
			var str ='';
			for(var i=0;i<data.logs.length;i++){
				var style= '';
				if(i==data.logs.length-1){
					style= ' lastLog';
				}
				 
				var style2='';
				var addDiv = '';
				var logDetail = '';
				if(data.logs[i].logDetail==null || data.logs[i].logDetail==''){
						if(data.logAttachement.length>0){
					}else{
						style2= 'style="display:none"';
						addDiv = '</br>';
					}
				}else{
					logDetail = data.logs[i].logDetail;
					logDetail=logDetail.replace(/;/g,"<br/>");
				}
			 str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span><span>'+data.logs[i].logType+'</span>&nbsp;&nbsp;&nbsp;'+
			'<span>'+data.logs[i].userName+'  | '+data.logs[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.logs[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
		    '<div class="logDiv_cont" ><div class="logDiv_contBorder"><div class="logDiv_contRemark" '+style2+'>'
		    
		    var ifjson =isJSON(logDetail); 
			
				if(ifjson){
					if(logDetail=="[]"||logDetail==""||logDetail==undefined){
						
						_span='<span>未经任何操作</span>'
					}else{
					var Detail=JSON.parse(logDetail); 
					for (var s=0;s<Detail.length;s++) {
						//alert(Detail[s].newValue);
						var value=Detail[s].oldValue
						if(value==""||value==undefined||value==null){
							value=""
						}
						if(Detail[s].remark!=""&&Detail[s].remark!=undefined){
							str+='<span>备注信息：'+Detail[s].remark+'</span></br>'	
						}
						if(undefined!=Detail[s].newValue){//""!=Detail[s].newValue&&
							if(value==null||value==""){
								value="&nbsp;&nbsp";
							}
							newValue=Detail[s].newValue;
							if(newValue==null||newValue==""){
								newValue="&nbsp;&nbsp";
							}
							str+='<span>'+Detail[s].fieldName+"："+"<span style='font-weight:bold'>"+'"'+value+'"'+"</span>"+modified+"<span style='font-weight:bold'>"+'"'+newValue+'"'+'</span></br>'	
						}
					}
					}
				}else{
					str+='<span>'+logDetail+'</span>'
				}
		    	
				str+='<div class="file-upload-list">';
			if(data.logAttachement.length>0){
				str +='<table class="file-upload-tb">';
				var _trAll = '';
				for(var j=0;j<data.logAttachement.length;j++){
					if((data.logAttachement[j].devTaskLogId)==(data.logs[i].id)){
						_span="";
						
					var attType = '';
					if(data.logAttachement[j].status == 1){//新增的日志
						attType = "<lable>新增附件：</lable>";
					}else if(data.logAttachement[j].status == 2){//删除的日志
						attType = "<lable>删除附件：</lable>";
					}
					var _tr = '';
					var file_name = data.logAttachement[j].fileNameOld;
					var file_type = data.logAttachement[j].fileType;
					var _td_icon;
					var _td_name = '<span>'+file_name+'</span>';
					//var _td_name = '<span>'+file_name+'</span><i class="file-url">'+data.logAttachement[j].filePath+'</i><i class = "file-bucket">'+data.logAttachement[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.logAttachement[j].fileS3Key+'</i></td>';
					
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
					var row =  JSON.stringify( data.logAttachement[j]).replace(/"/g, '&quot;');
					_tr+='<tr><div class="fileTb" style="cursor:pointer" onclick ="download2('+row+')">'+_td_icon+_td_name+'</tr>';
				
					_trAll +=_tr;
				}
					
				}
				
			}
			if(_trAll==undefined){_trAll=""}
			_trAll+=_span;
			_span="";
			str+= _trAll+'</table>';
			str += '</div></div>'+addDiv+'</div></div></div>';
					
		}
			$("#whandleLogs").append(str);
		}
		modalType = 'wcheck';
		$("#loading").css('display','none');
	  $("#selectdetail").modal("show");
}
//提交备注
function addDevRemark(){
	var devTaskRemark=$.trim($("#tyaskRemark").val());
	if(devTaskRemark==""||devTaskRemark==undefined){
		layer.alert('备注信息不能为空！', {
            icon: 2,
            title: "提示信息"
        });
		return;
	}
	var id=$.trim($("#DevTaskID").val());
		 var obj = {};
		 obj.devTaskRemark =$.trim($("#tyaskRemark").val());
		 obj.devTaskId =id;
		var remark=JSON.stringify(obj); 
	$.ajax({
		type:"post",
		url:"/devManage/worktask/addTaskRemark",
		data:{
			remark:remark,
			attachFiles :$("#wcheckfiles").val()
			},
		success: function(data) {
			layer.alert("保存成功！！！",{icon:1});
			_wcheckfiles = [];
			$("#wcheckfiles").val(''); 
			getSee(id);
		}
		
	});
}
function showThisDiv2(This,num){
	if( $(This).hasClass("def_changeTit") ){
		$("#titleOfwork .def_controlTit").addClass("def_changeTit");
		$(This).removeClass("def_changeTit");
		if( num==1 ){
			$(".dealLog").css("display","none");
			$(".workRemarks").css("display","block");
		}else if( num==2 ){
			$(".dealLog").css("display","block");
			$(".workRemarks").css("display","none");
		}
	}   
}
//文件下载
function download2(row){
	var fileS3Bucket = row.fileS3Bucket;
	var fileS3Key = row.fileS3Key;
	var fileNameOld = row.fileNameOld;
	window.location.href = "/devManage/worktask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld;
}

function isJSON(str) {
    if (typeof str == 'string') {
        try {
            JSON.parse(str);
            return true;
        } catch(e) {
            return false;
        }
    }
}

//文件上传，并列表展示
function uploadFileList(){ 
	//列表展示
	$("#checkuploadFile").change(function(){ 
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
		
		outer:for(var i=0,len=files.length;i<len;i++){
			var file = files[i];
			if(file.size<=0){
				layer.alert(file.name+"文件为空！", {icon: 0});
				continue;
			}
			var fileList = [];
    		if(modalType == 'dcheck'){
        		fileList=_dcheckfiles;
        	}else 
    		if(modalType == 'wcheck'){
        		fileList=_wcheckfiles;
        	}
    		for(var j=0;j<fileList.length;j++){
    			if(fileList[j].fileNameOld ==file.name){
    				layer.alert(file.name+"文件已存在！！！",{icon:0});
    				continue outer;
    			}
    		}
    		
			formFile.append("files", file);
			//读取文件
			if (window.FileReader) {    
				(function(i){
					var file = files[i];
					var reader = new FileReader();    
					reader.readAsDataURL(file);   
				    reader.onerror = function(e) {
				    	  layer.alert("文件" + file.name +" 读取出现错误", {icon: 0});
				        return false;
				    }; 
					reader.onload = function (e) {
						if(e.target.result) {
							console.log("文件 "+file.name+" 读取成功！");
						}
					}; 
				})(i);   
			} 
			//列表展示
			var _tr = '';
			var file_name = file.name.split("\.")[0];
			var file_type = file.name.split("\.")[1];
			var _td_icon;
			var _td_name = '<span >'+file.name+'</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
			var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
			switch(file_type){
				case "doc":
				case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
				case "xls":
				case "xlsx":_td_icon = '<img src="'+_icon_excel+'" />';break;
				case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
				case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
				case "png":
				case "jpeg":
				case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
				default:_td_icon = '<img src="'+ _icon_other+'" />';break;
			}
			var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
			_tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>'; 
			_table.append(_tr);  
			
		} 
		var url = '';
		if(modalType == 'dcheck'){
			url = '/zuul/devManage/devtask/uploadFile';
		}else if(modalType == 'wcheck'){
			url = '/zuul/devManage/worktask/uploadFile';
		}
		//上传文件
    	$.ajax({
	        type:'post',
	        url:url,
	        contentType: false,  
	        processData: false,
	        dataType: "json",
	        data:formFile,
	        success:function(data){ 
	        	for(var k=0,len=data.length;k<len;k++){
	        		
	        		if(modalType == 'dcheck'){
	        			_dcheckfiles.push(data[k]);
	        		}else if(modalType == 'wcheck'){
	        			_wcheckfiles.push(data[k]);
	        		}
		        	$(".file-upload-tb span").each(function(o){ 
		        		if($(this).text() == data[k].fileNameOld){ 
		        			$(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
		        			$(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
		        		}
		        	});
	        	}
	        	if(modalType == 'dcheck'){
	        		$("#dcheckfiles").val(JSON.stringify(_dcheckfiles));
	        		clearUploadFile('checkuploadFile');
	        	}else if(modalType == 'wcheck'){
	        		$("#wcheckfiles").val(JSON.stringify(_wcheckfiles));
	        		clearUploadFile('checkuploadFile');
	        	}
	        }
	    });
	});
}

//清空上传文件
function clearUploadFile(idName){
	//$(idName).wrap('<form></form>');
	//$(idName).unwrap();
	$('#'+idName+'').val('');
}

//移除上传文件
function delFile(that){
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text(); 
	$(that).parent().parent().remove();
	removeFile(fileS3Key);
}

//移除暂存数组中的指定文件
function removeFile(fileS3Key){
	if(modalType == 'dcheck'){
		var _file = $("#dcheckfiles").val();
		if(_file != ""){s
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_dcheckfiles  = files;
			$("#dcheckfiles").val(JSON.stringify(files));
		}
	}
	if(modalType == 'wcheck'){
		var _file = $("#wcheckfiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_wcheckfiles  = files;
			$("#wcheckfiles").val(JSON.stringify(files));
		}
	}
}
function down(This){
    if( $(This).hasClass("fa-angle-double-down") ){
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
        $(This).parents('.allInfo').children(".connect_div").slideDown(100);
    }else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
        $(This).parents('.allInfo').children(".connect_div").slideUp(100);
    }
}
