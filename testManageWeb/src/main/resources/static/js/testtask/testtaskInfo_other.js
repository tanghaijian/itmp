//工作任务弹出框
var _SeeFiles =[];
var modified  = "<span>&nbsp;修改为&nbsp;</span>";
function  getSee(id){
    $.ajax({
		method:"post", 
		url:"/testManage/worktask/getSeeDetail",
		data:{"id":id},
		success : function(data) {
			selectdetail(data)
		}
	});
}
//查看
function selectdetail(data){
	$("#loading").css('display','block');
	_checkfiles=[];
	var map=data.dev;
	$("#checkfiles").val("");
	$(".workFileTableClass").empty();
	$("#taskRemark").empty();
	$("#workhandleLogs").empty();
	$("#SeeFileTable").empty();
	$("#worktaskRemark").val("");
	$("#SdevCode").html("");
	$("#SdevName").html("");
	$("#SdevOverview").html("");
	$("#SStatus").html("");
	$("#devuserID").html("");
//	$("#planStartDate").html("");
//	$("#planEndDate").html("");
	$("#wfeatureName").html("");
//	$("#SplanWorkload").html("");
	$("#featureOverview").html("");
	$("#requirementFeatureStatus").html("");
	$("#manageUserId").html("");
	$("#executeUserId").html("");
	$("#wsystemId").html("");
	$("#requirementSource").html("");
	$("#requirementType").html("");
	$("#requirementPriority").html("");
	$("#requirementPanl").html("");
	$("#requirementStatus").html("");
	$("#wrequirementName").html("");
	$("#applyUserId").html("");
	$("#applyDeptId").html("");
	$("#expectOnlineDate").html("");
	$("#planOnlineDate").html("");
	$("#lastUpdateDate3").html("");
	$("#createDate3").html("");
	$("#openDate").html("");
	$("#requirementOverview").val("");
	$("#testStage2").text("");
	$("#requirementOverview").val(map.requirementOverview);
	$("#SdevOverview").html(map.testTaskOverview);
	$("#SdevName").html(map.testTaskName);
	$("#SdevCode").html(map.testTaskCode);  
	var devTaskStatus=map.testDevTaskStatus;
//	if(devTaskStatus=="1"){
//		$("#SStatus").html("待实施");
//	}else if(devTaskStatus=="2"){
//		$("#SStatus").html("实施中");
//	}else if(devTaskStatus=="3"){
//		$("#SStatus").html("实施完成");
//	}
	$("#SStatus").text(devTaskStatus);
	$("#devuserID").html(map.devuserName);
	$("#testTaskID").val(map.id);
	$("#createBy").html(map.createName);
	if (map.createDate != null) {
		$("#testStage2").text(map.testStage);
	}
	if(map.createDate!=null){
		$("#createDate").html(datFmt(new Date(map.createDate),"yyyy年MM月dd"));
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
		$("#defectNumber").html("");
		$("#cases").html(map.designCaseNumber);
		$("#caseExecutes").html(map.executeCaseNumber);
		$("#executeUser").html(users);
		$("#defectNumber").html(map.defectNum);
	
		$("#wcheckChange").empty();
		$("#checkRequirementFeatureSource").empty();
		$("#wcheckImportantRequirementType").empty();
		$("#wcheckPptDeployTime").empty();
		$("#wcheckSubmitTestTime").empty();
		$("#wcheckChange").text(map.requirementChangeNumber == null ? '' : map.requirementChangeNumber);
		$("#checkRequirementFeatureSource").text(map.requirementFeatureSource == null ? '' : map.requirementFeatureSource);
		$("#wcheckImportantRequirementType").text(map.importantRequirementType == '1' ? '是' : '否');
		$("#wcheckPptDeployTime").text(map.pptDeployTime == null ? '' : timestampToTime(map.pptDeployTime));
		$("#wcheckSubmitTestTime").text(map.submitTestTime == null ? '' : timestampToTime(map.submitTestTime));
	
		
		$("#actualStartDate").html("");
		if(map.actualStartDate!=null){
			var actualStartDate=datFmt(new Date(map.actualStartDate),"yyyy年MM月dd");
			$("#actualStartDate").html(actualStartDate);
		}
		$("#actualEndDate").html("");
		if(map.actualEndDate!=null){
			var actualEndDate=datFmt(new Date(map.actualEndDate),"yyyy年MM月dd");
			$("#actualEndDate").html(actualEndDate);
		}
		$("#actualWorkload").html("");
		$("#actualWorkload").html(map.actualWorkload);
		
		$("#wfeatureName").html(map.featureCode + "&nbsp|&nbsp" + map.featureName);
		$("#featureOverview").html(map.featureOverview);
		$("#requirementFeatureStatus").html(map.requirementFeatureStatusName);
		
		$("#manageUserId").html(map.manageUserName);
		$("#executeUserId").html(map.executeUserName);
		$("#wsystemId").html(map.systemName);
		$("#wrequirementName").html(map.requirementName);
		$("#requirementSource").html(map.requirementSource);
		$("#requirementType").html(map.requirementType);
		$("#requirementPriority").html(map.requirementPriority);
		$("#requirementPanl").html(map.requirementPanl);
		$("#requirementStatus").html(map.requirementStatus);
		$("#applyUserId").html(map.applyUserName);
		$("#applyDeptId").html(map.applyDeptName);
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
					default:_td_icon = '<img src="'+_icon_word+'" />';break;
				}
				var row =  JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
				_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+" "+_td_name+'</tr>'; 
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
		    '<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><span>'+data.rmark[i].testTaskRemark+'</span>'+
			'<div class="file-upload-list">';
			 if(data.Attchement.length>0){
				 str +='<table class="file-upload-tb">';
					var _trAll = '';
					for(var j=0;j<data.Attchement.length;j++){
						var _tr = '';
						if((data.Attchement[j].testTaskRemarkId)==(data.rmark[i].id)){
							
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
								default:_td_icon = '<img src="'+_icon_word+'" />';break;
							}
							var row =  JSON.stringify( data.Attchement[j]).replace(/"/g, '&quot;');
							_tr+='<tr><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+_td_name+'</tr>'; 
						
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
				if(data.logs[i].logDetail==null|| data.logs[i].logDetail==''){
					if(data.logAttachement.length>0){
						for(var j=0;j<data.logAttachement.length;j++){
							if(data.logs[i].id!=data.logAttachement[j].devTaskLogId){
								style2= 'style="display:none"';
								addDiv = '</br>';
							}
						}
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
					_span="";
					if(logDetail=="[]"||logDetail==""||logDetail==undefined){
						_span='<span>未做任何操作</span>'
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
						if(""!=Detail[s].newValue&&undefined!=Detail[s].newValue){
							str+='<span>'+Detail[s].fieldName+"："+"“<span style='font-weight:bold'>"+value+"</span>”"+modified+"“<span style='font-weight:bold'>"+Detail[s].newValue+"</span>”"+'</span></br>'		
						}
					}}
					
				}else{
					str+='<span>'+logDetail+'</span>'
				}
		    	
				str+='<div class="file-upload-list">';
			if(data.logAttachement.length>0){
				str +='<table class="file-upload-tb">';
				var _trAll = '';
				for(var j=0;j<data.logAttachement.length;j++){
					if((data.logAttachement[j].testTaskLogId)==(data.logs[i].id)){
					var attType = '';
					_span="";
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
						default:_td_icon = '<img src="'+_icon_word+'" />';break;
					}
					var row =  JSON.stringify( data.logAttachement[j]).replace(/"/g, '&quot;');
					_tr+='<tr><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+attType+_td_icon+_td_name+'</tr>';
				_trAll +=_tr;
				}
				}
				
			}
			if(_trAll==undefined){_trAll=""}
			_trAll+=_span;
			//_span="";
			str+= _trAll+'</table>';
			_trAll="";
			str += '</div></div>'+addDiv+'</div></div></div>';
					
		}
			$("#workhandleLogs").append(str);
		}
		modalType = 'check';
		 $("#loading").css('display','none');
	  $("#selectdetail").modal("show");
}

//提交备注
function addTestRemark(){
	var testTaskRemark=$.trim($("#worktaskRemark").val());
	if(testTaskRemark==""||testTaskRemark==undefined){
		layer.alert('备注信息不能为空！', {
            icon: 2,
            title: "提示信息"
        });
		return;
	}
	var id=$.trim($("#testTaskID").val());
		 var obj = {};
		 obj.testTaskRemark =$.trim($("#worktaskRemark").val());
		 obj.testTaskId =id;
		var remark=JSON.stringify(obj); 
	$.ajax({
		type:"post",
		url:"/testManage/worktask/addTaskRemark",
		data:{
			remark:remark,
			attachFiles :$("#checkfiles").val()
			},
		success: function(data) {
			layer.alert("保存成功！！！",{icon:1});
			_checkfiles = [];
			$("#checkfiles").val(''); 
			getSee(id);
		}
		
	});
}
function showThisDiv2(This,num){
	if( $(This).hasClass("def_changeTit") ){
		$("#wtitleOfwork .def_controlTit").addClass("def_changeTit");
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
function download(row){
	var fileS3Bucket = row.fileS3Bucket;
	var fileS3Key = row.fileS3Key;
	var fileNameOld = row.fileNameOld;
	window.location.href = "/testManage/worktask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld;
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