//工作任务弹出框
var _SeeFiles =[];
var modified  = "<span>&nbsp;修改为&nbsp;</span>";
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
	$("#checkAttTable").empty();
	$("#SeeFileTable").empty();
	$("#taskRemark").empty();
	$("#checkfiles").val("");
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
	$("#check_devTaskType").html("");
	$("#wrequirementName").html('');
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
		$("#check_devTaskType").html(data.devTaskType);
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
						
						_span='<span>未经任何操作</span>'
					}else{
					var Detail=JSON.parse(logDetail); 
					for (var s=0;s<Detail.length;s++) {
						//alert(Detail[s].newValue);
						var value=Detail[s].oldValue;
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
					if(logDetail!=""&&logDetail!=undefined){
						str+='<span>'+logDetail+'</span>'
					}
					
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
						default:_td_icon = '<img src="'+_icon_word+'" />';break;
					}
					var row =  JSON.stringify( data.logAttachement[j]).replace(/"/g, '&quot;');
					_tr+='<tr><div class="fileTb" style="cursor:pointer" onclick ="download2('+row+')">'+attType+_td_icon+_td_name+"</tr>";
				
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
		modalType = 'check';
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


//========================缺陷详情弹框==============
var defectStatusList = '';
var solveStatusList = '';
var emergencyLevelList = '';
var defectTypeList = '';
var defectSourceList = '';
var severityLevelList = '';
var rejectionList = '';
var _icon_PNG = "../static/images/file/PNG.png";
var _icon_JPG = "../static/images/file/JPG.png";
var _icon_BMP = "../static/images/file/BMP.png";
var _icon_WENHAO = "../static/images/file/text.png";
var defectUrl = '/devManage/';
var jiantou = "<span>&nbsp;&nbsp;修改为&nbsp;&nbsp;</span>";
var editAttList = [];

$(function(){
	$.ajax({
		url:"/devManage/devtask/getDefectDic",
		type:"post",
		dataType:"json",
		async:false,
		success:function(data){
			defectSourceList = data["defectSource"];
			defectStatusList = data["defectStatus"];
			solveStatusList = data["dftSolveStatus"];
			emergencyLevelList = data["dftEmergencyLevel"];
			defectTypeList = data["defectType"];
			severityLevelList = data["dftSeverityLevel"];
			rejectionList = data["dftRejectReson"];
		}
	})
})


//缺陷详情页面
function showDefect( defectId ){
	$("#loading").css('display','block');
    reset_opt();
    $("#dealLogDiv").empty();
   /*收起弹框里面的日志内容*/
	_flag=1;
	$("#checkDefectID").val( defectId );
	$(".defectHandlingLog .def_content").css("display", "none" );

	$(".down_content").css("display", "block" );
	$("#defectBaseInfo .def_title .fa").removeClass("fa-angle-double-down");
    $("#defectBaseInfo .def_title .fa").addClass("fa-angle-double-up");

    $(".defectHandlingLog .def_title .fa").removeClass("fa-angle-double-up");
    $(".defectHandlingLog .def_title .fa").addClass("fa-angle-double-down");

    submitDefectStatus = "checkDefect";
    attList(defectId,"#check_table");
    $.ajax({
        url:defectUrl+"defect/getDefectRecentLogById",
        method:"post",
        data:{
            defectId:defectId
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {

                if(data.devTask != null && data.devTask != "undefined"){
                    $("#check_devTaskCode").text(data.devTask.devTaskCode);
                    $("#check_devTaskName").text(data.devTask.devTaskName);
                }

                if(data.feature != null && data.feature != "undefined"){
                    $("#check_reqFetureCode").text(data.feature.featureCode);
                    $("#check_reqFetureName").text(data.feature.featureName);
                }

                // 最新一次的日志信息
                if (data.data != null) {
                    var string = '';
                    var _span = '';
                    var logDetail = JSON.parse(data.data.logDetail);
                    if (logDetail.length > 0) {
                        if (logDetail[0].remark != undefined) {
                            string += logDetail[0].remark + "<br >";
                        }
                    } else {
                        _span = '<span>' + '未填写任何备注' + '</span><br/>';
                    }

                    if (data.data.logAttachementList != null && data.data.logAttachementList.length > 0) {
                        _span = '';
                        var status_1 = 0;
                        var status_2 = 0;
                        string += '<a class="a_style moreDefectInfo" onclick="showMoreInfo(this)">更多备注信息 ></a>' +
                            '<div class="file-upload-list"><table class="file-upload-tb" >';
                        for (var j = 0; j < data.data.logAttachementList.length; j++) {

                            var logAttachementList = data.data.logAttachementList;
                            if (logAttachementList[j].status == 1){
                                string += '<tr><td>';

                                var LogFileType = logAttachementList[j].fileType;
                                var classType = '';
                                classType = classPath(LogFileType, classType);

                                if (status_1 == 0){
                                    string +="<span>新增附件：</span></td><td>";
                                } else {
                                    string +="<span></span></td><td>";
                                }

                                string += classType;

                                var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                string += '&nbsp;<a download="' + 1 + '"  class="span_a_style" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                status_1++;
                            } else if(logAttachementList[j].status == 2){
                                string += '<tr><td>';

                                var LogFileType = logAttachementList[j].fileType;
                                var classType = '';
                                classType = classPath(LogFileType, classType);

                                if (status_2 == 0){
                                    string +="<span>删除附件：</span></td><td>";
                                } else {
                                    string +="<span></span></td><td>";
                                }

                                string += classType;

                                var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                string += '<a download="' + 1 + '"  class="span_a_style" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                status_2++;
                            }
                        }
                        string += '</table></div>';
                    }
                    string += _span;
                    $(".defectDescribe").append(string);
                }
                $("#checkDefectInfoDiv").modal("show");
            }
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}
/*附件列表*/
function attTable(data,idName){
    fileTableName = idName;
    $(idName).bootstrapTable("destroy");
    $(idName).bootstrapTable({
        pagination : false,
        showHeader: false,
        sidePagination:'client',
        data:data,
        showFooter: false,
        columns : [{
            field : "fileNameOld",
            title : "fileNameOld",
            align : 'left',
            formatter : function(value, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                var classType = '';
                classType = classPath(row.fileType,classType);

                if (idName == "#check_table" || idName == "#dev_check_table"){
                    return '<div class="fileTb" style="cursor:pointer" >'+classType+'    <a download="'+1+'" onclick="dftDownloadFile('+rows+')">'+row.fileNameOld+'</a></div>';
                } else if (idName == "#edit_newFileTable"){
                    return '<div class="fileTb">'+classType+'    '+row.fileNameOld+'</div>';
                }
            }
        },{
            field : "opt",
            title : "操作",
            align : 'center',
            formatter : function(value, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                if (idName == "#check_table" || idName == "#dev_check_table"){
                    return '';
                } else if (idName == "#edit_newFileTable"){
                    if(row.id == null){
                        var name =  JSON.stringify({fileName:row.fileNameOld}).replace(/"/g, '&quot;');
                        return '<div class="span_a_style"><span class="a_style" onclick="delFile('+name+',this)">删除</span></div>';
                    } else {
                        return '<div class="span_a_style"><span class="a_style" onclick="deleteAtt(\'' + rows +'\',this,\'' + index +'\')">删除</span></div>';
                    }
                }

            }
        }],
        onLoadSuccess:function(){
            $("#loading").css('display','none');
        },
        onLoadError : function() {

        },
        formatNoMatches: function(){
            return "";
        }
    });
}

//附件列表后台接口
function attList(id,FileTable){
    $("#loading").css('display','block');
    /*查询附件,显示附件列表*/
    $.ajax({
        url:defectUrl+"defect/findAttList",
        method:"post",
        data:{
            defectId:id
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                var att = data.attList;
                attTable(att,FileTable);
                if (att != null && att.length > 0){
                    for( var i = 0,len = att.length;i < len ; i++){
                        editAttList.push(att[i].fileNameOld);
                    }
                }

                if(data.defectInfo != null ){
                    if(submitDefectStatus == "checkDefect"){
                        var rows = data.defectInfo;
                        $("#check_systemName").text(rows.systemName);
                        $("#check_defectCode").text(rows.defectCode);
                        $("#check_submitUserName").text(rows.submitUserName);
                        $("#check_assignUserName").text(rows.assignUserName != null?rows.assignUserName:"");
                        $("#check_defectOverview").html(  htmlDecodeJQ( rows.defectOverview )  );
                        $("#check_defectSummary").text(rows.defectSummary);

                        if (rows.defectStatus == 5 && rows.solveStatus != null){
                            $("#check_solveStatus_span").css("display","inline");
                            for (var i = 0,len = solveStatusList.length;i < len;i++) {
                                if(rows.solveStatus == solveStatusList[i].value){
                                    $("#check_solveStatus").text(solveStatusList[i].innerHTML);
                                    break;
                                }
                            }
                        }

                        if (rows.defectStatus == 3 && rows.rejectReason != null){
                            $("#check_rejectReason_span").css("display","inline");
                            for (var i = 0,len = rejectionList.length;i < len;i++) {
                                if(rows.rejectReason == rejectionList[i].value){
                                    $("#check_rejectReason").text(rejectionList[i].innerHTML);
                                    break;
                                }
                            }
                        }

                        rows["checkDefectType"] = "#check_defectType";
                        rows["checkDefectSource"] = "#check_defectSource";
                        rows["checkDefectStatus"] = "#check_defectStatus";
                        rows["checkSeverityLevel"] = "#check_severityLevel";
                        rows["checkEmergencyLevel"] = "#check_emergencyLevel";
                        dicDefectSelect(rows);
                    }
                }

            }
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

//数据字典valuecode转valuename
function dicDefectSelect(rows){
    for (var i = 0,len = defectTypeList.length;i < len;i++) {
        if(rows.defectType == defectTypeList[i].valueCode){
            $(rows.checkDefectType).html(defectTypeList[i].valueName);
            break;
        }
    }

    for (var i = 0,len = defectSourceList.length;i < len;i++) {
        if(rows.defectSource == defectSourceList[i].valueCode){
            $(rows.checkDefectSource).html(defectSourceList[i].valueName);
            break;
        }
    }

    for (var i = 0,len = defectStatusList.length;i < len;i++) {
        if(rows.defectStatus == defectStatusList[i].valueCode){
            $(rows.checkDefectStatus).html(defectStatusList[i].valueName);
            break;
        }
    }

    for (var i = 0,len = severityLevelList.length;i < len;i++) {
        if(rows.severityLevel == severityLevelList[i].valueCode){
            var classColor = "classColor"+rows.severityLevel;
            var severityLevelData = "<span class='"+classColor+"'>"+severityLevelList[i].valueName+"</span>";
            $(rows.checkSeverityLevel).html(severityLevelData);
            break;
        }
    }

    for (var i = 0,len = emergencyLevelList.length;i < len;i++) {
        if(rows.emergencyLevel == emergencyLevelList[i].valueCode){
            var classColor = "classColor"+rows.emergencyLevel;
            var emergencyLevellData = "<span class='"+classColor+"'>P"+emergencyLevelList[i].valueName+"</span>";
            $(rows.checkEmergencyLevel).html(emergencyLevellData);
            break;
        }
    }
}

//重置
function reset_opt(){
	  $("#opt_defectRemark").val('');
	    $("#opt_assignUserId").val('');
	    $("#opt_assignUserName").val('');
	    $("#opt_solution_defectId").val('');
	    $("#opt_solution_submitUserId").val('');
	    $("#opt_solution_oldAssignUserId").val('');
	    $("#dealLogDiv").empty();
	    $(".defectDescribe").empty();

	    $("#opt_solution_defectCode").val('');
	    $("#dev_checkDefectID").val('');
	    $("#optFileTable tbody").html("");
	    $("#opt_rejection").selectpicker('val', '');
	    $("#opt_solution").selectpicker('val', '');
	    $("#opt_rejectionShow").selectpicker('val', '');

	    $("#opt_rejectionShow").empty();

	    $("#check_severityLevel").empty();
	    $("#check_emergencyLevel").empty();
	    $("#check_assignUserName").empty();
	    $("#check_submitUserName").empty();
	    $("#check_systemName").empty();
	    $("#check_defectCode").empty();
	    $("#check_defectType").empty();
	    $("#check_defectSource").empty();
	    $("#check_defectOverview").empty();
	    $("#check_table tbody").html("");


	    $("#dev_check_reqFetureCode").empty();
	    $("#dev_check_reqFetureName").empty();
	    $("#dev_check_devTaskCode").empty();
	    $("#dev_check_devTaskName").empty();

	    $("#dev_check_severityLevel").empty();
	    $("#dev_check_emergencyLevel").empty();
	    $("#dev_check_assignUserName").empty();
	    $("#dev_check_submitUserName").empty();
	    $("#dev_check_systemName").empty();
	    $("#dev_check_defectCode").empty();
	    $("#dev_check_defectType").empty();
	    $("#dev_check_defectSource").empty();
	    $("#dev_check_defectOverview").empty();
	    $("#dev_check_table tbody").html("");

	    $("#check_reqFetureCode").empty();
	    $("#check_reqFetureName").empty();
	    $("#check_devTaskCode").empty();
	    $("#check_devTaskName").empty();

	    $(".solution").css("display","none");
	    $("#opt_Restored").css("display","none");
	    $(".transmit").css("display","none");
	    $(".rejection").css("display","none");
	    $(".opt_rejectionDiv").css("display","none");
	    $("#opt_RejectDefect").css("display","none");
	    $("#opt_submitDefect").css("display","none");
	    $("#opt_submitAgainDefect").css("display","none");
	    $("#check_solveStatus_span").css("display","none");
	    $("#check_rejectReason_span").css("display","none");
	    $("#edit_submitDefect").css("display","none");
	    $("#edit_stagDefect").css("display","none");

	    $("#check_dev_task_startDate").val('');
	    $("#check_dev_task_endDate").val('');
	    $("#check_dev_task_workNum").val('');
	    $("#check_dev_task_id").val('');
	    $("#check_dev_task").css("display","none");


    rejectDefectData = {};
    sendRow = {};
    __defectAgainData = {};
}
function down2( This ){
    if( $(This).hasClass("fa-angle-double-down") ){
        if( _flag==1 ){
            $("#loading").css('display','block');
            $.ajax({
                url:defectUrl+"defect/getDefectLogById",
                method:"post",
                data:{
                    defectId:$("#checkDefectID").val()
                },
                success:function(data){
                    $("#loading").css('display','none');
                    if (data.status == 2){
                        layer.alert(data.errorMessage, {
                            icon: 2,
                            title: "提示信息"
                        });
                    } else if(data.status == 1){
                        if(data.data != null){
                            var str='';
                            for( var i=0;i<data.data.length;i++ ){
                                if( i==( data.data.length-1 ) ){
                                    str+= '<div class="logDiv lastLog">';
                                }else{
                                    str+= '<div class="logDiv">'
                                }
                                var log_Type = data.data[i].logType;


                                str+= '<div class="logDiv_title"><span class="orderNum"></span><span class="fontWeihgt">'+log_Type+'</span>&nbsp;&nbsp;&nbsp;'+
                                    '<span>'+ data.data[i].userName +'</span>&nbsp;&nbsp;&nbsp;<span>'+ data.data[i].createDate +'</span></div>'+
                                    '<div class="logDiv_cont"><div class="logDiv_contBorder">';
                                var _span = '';
                                switch(data.data[i].logType)
                                {
                                    case "新建缺陷":
                                        str+="<br />";
                                        break;
                                    case "更新状态":

                                        var childStr='';
                                        if( data.data[i].logDetail != null && data.data[i].logDetail.length>0 ){
                                            var logDetail=JSON.parse( data.data[i].logDetail );
                                            if(logDetail != null && logDetail.length > 0){
                                                for( var j=0;j<logDetail.length;j++ ){
                                                    if( j == 0 ){
                                                        if( logDetail[0].remark!=undefined ){
                                                            childStr+="备注内容：<span class='span_font-weight'>"+logDetail[0].remark+"</span><br >";
                                                        }else{
                                                            var oldName=logDetail[j].oldValue;
                                                            var newName=logDetail[j].newValue;

                                                            var arr = logDetailList(logDetail[j].fieldName,oldName,newName);
                                                            childStr+='<span>'+logDetail[j].fieldName+'：<span class="span_font-weight">"'+arr[0]+'"</span>'+ jiantou +'<span class="span_font-weight">"'+arr[1]+'"</span></span><br />';

                                                        }
                                                    }else{
                                                        var oldName=logDetail[j].oldValue;
                                                        var newName=logDetail[j].newValue;

                                                        var arr = logDetailList(logDetail[j].fieldName,oldName,newName);
                                                        childStr+='<span>'+logDetail[j].fieldName+'：<span class="span_font-weight">"'+arr[0]+'"</span>'+ jiantou +'<span class="span_font-weight">"'+arr[1]+'"</span></span><br />';

                                                    }
                                                }
                                            } else {
                                                _span = '<span>未经任何操作</span><br/>';
                                            }
                                        } else {
                                            _span = '<span>未经任何操作</span><br/>';
                                        }
                                        if( data.data[i].logAttachementList != null && data.data[i].logAttachementList.length>0 ){
                                            _span = '';

                                            var status_1 = 0;
                                            var status_2 = 0;
                                            childStr+='<div class="file-upload-list"><table class="file-upload-tb">';
                                            for( var j=0;j<data.data[i].logAttachementList.length;j++ ){

                                                var logAttachementList = data.data[i].logAttachementList;

                                                if(logAttachementList[j].status == 1){

                                                    childStr+='<tr><td>';
                                                    var classType = '';
                                                    var LogFileType = logAttachementList[j].fileType;
                                                    classType = classPath(LogFileType,classType);

                                                    if (status_1 == 0){
                                                        childStr +="<span>新增附件：</span></td><td>";
                                                    } else {
                                                        childStr +="<span></span></td><td>";
                                                    }

                                                    childStr += classType;

                                                    var row =  JSON.stringify( logAttachementList[j]  ).replace(/"/g, '&quot;');
                                                    childStr+='&nbsp;<a  class="span_a_style" download="'+1+'" onclick="downloadFile(  '+ row +'  )">'+ logAttachementList[j].fileNameOld +'</a></td><tr>';
                                                    status_1++;
                                                } else if(logAttachementList[j].status == 2){

                                                    childStr+='<tr><td>';

                                                    var classType = '';
                                                    var LogFileType = logAttachementList[j].fileType;
                                                    classType = classPath(LogFileType,classType);

                                                    if (status_2 == 0){
                                                        childStr +="<span>删除附件：</span></td><td>";
                                                    } else {
                                                        childStr +="<span></span></td><td>";
                                                    }

                                                    childStr += classType;

                                                    var row =  JSON.stringify( logAttachementList[j]  ).replace(/"/g, '&quot;');
                                                    childStr+='&nbsp;<a class="span_a_style" download="'+1+'" onclick="downloadFile(  '+ row +'  )">'+ logAttachementList[j].fileNameOld +'</a></td><tr>';
                                                    status_2++;
                                                }

                                            }
                                            childStr+='</table></div>';
                                        }
                                        childStr += _span;
                                        str+='<div class="logDiv_contRemark">';
                                        str+=childStr;
                                        str+='</div>';
                                        break;
                                    case "修改内容":
                                        var childStr='';
                                        var logDetail=JSON.parse( data.data[i].logDetail );
                                        if( logDetail != null && logDetail.length>0 ){
                                            for( var j=0;j<logDetail.length;j++ ){ 
                                                if( j == 0 ){
                                                	if( logDetail[0].remark!=undefined ){
                                                        childStr+="备注内容：<span class='span_font-weight'>"+logDetail[0].remark+"</span><br >";
                                                    }else{
                                                        var oldName=logDetail[j].oldValue;
                                                        var newName=logDetail[j].newValue;
                                                       
                                                        var arr = logDetailList(logDetail[j].fieldName,oldName,newName); 
                                                        if( logDetail[j].fieldName=="缺陷描述" ){
                                                        	childStr+='<span>缺陷描述已修改。</span>'
                                                        }else if( logDetail[j].fieldName=="扩展字段"  ){
                                                      	  var obj1=JSON.parse( oldName ).field;
                                                      	  var obj2=JSON.parse( newName ).field;
                                                      	  childStr+='<span>'+logDetail[j].fieldName+'：</span>';
                                                      	  childStr+=dealExtend( obj1 , obj2 ); 
                                                        }else{
                                                        	childStr+='<span>'+logDetail[j].fieldName+'：<span class="span_font-weight">"'+arr[0]+'"</span>'+ jiantou +'<span class="span_font-weight">"'+arr[1]+'"</span></span><br />'
                                                        } 
                                                    }
                                                }else{
                                                	var oldName=logDetail[j].oldValue;
                                                    var newName=logDetail[j].newValue;
                                                   
                                                   
                                                    var arr = logDetailList(logDetail[j].fieldName,oldName,newName); 
                                                    if( logDetail[j].fieldName=="缺陷描述" ){
                                                    	childStr+='<span>缺陷描述已修改。</span>'
                                                    }else if( logDetail[j].fieldName=="扩展字段"  ){
                                                  	  var obj1=JSON.parse( oldName ).field;
                                                  	  var obj2=JSON.parse( newName ).field;
                                                  	  childStr+='<span>'+logDetail[j].fieldName+'：</span>';
                                                  	  childStr+=dealExtend( obj1 , obj2 ); 
                                                    }else{
                                                    	childStr+='<span>'+logDetail[j].fieldName+'：<span class="span_font-weight">"'+arr[0]+'"</span>'+ jiantou +'<span class="span_font-weight">"'+arr[1]+'"</span></span><br />'
                                                    }
                                                }
                                            }
                                        }else{
                                            _span = '<span>未经任何操作</span><br/>';
                                        }

                                        if( data.data[i].logAttachementList != null && data.data[i].logAttachementList.length>0 ){
                                            _span = '';
                                            var status_1 = 0;
                                            var status_2 = 0;
                                            childStr+='<div class="file-upload-list"><table class="file-upload-tb">';
                                            for( var j=0;j<data.data[i].logAttachementList.length;j++ ){
                                                var logAttachementList = data.data[i].logAttachementList;

                                                if(logAttachementList[j].status == 1){
                                                    childStr+='<tr><td>';
                                                    var classType = '';
                                                    var LogFileType = logAttachementList[j].fileType;
                                                    classType = classPath(LogFileType,classType);

                                                    if (status_1 == 0){
                                                        childStr +="<span>新增附件：</span></td><td>";
                                                    } else {
                                                        childStr +="<span></span></td><td>";
                                                    }

                                                    childStr += classType;
                                                    var row =  JSON.stringify( logAttachementList[j]  ).replace(/"/g, '&quot;');
                                                    childStr+='&nbsp;<a  class="span_a_style" download="'+1+'" onclick="downloadFile(  '+ row +'  )">'+ logAttachementList[j].fileNameOld +'</a></td><tr>';
                                                    status_1++;
                                                } else if(logAttachementList[j].status == 2){
                                                    childStr+='<tr><td>';

                                                    var classType = '';
                                                    var LogFileType = logAttachementList[j].fileType;
                                                    classType = classPath(LogFileType,classType);

                                                    if (status_2 == 0){
                                                        childStr +="<span>删除附件：</span></td><td>";
                                                    } else {
                                                        childStr +="<span></span></td><td>";
                                                    }
                                                    childStr += classType;

                                                    var row =  JSON.stringify( logAttachementList[j]  ).replace(/"/g, '&quot;');
                                                    childStr+='&nbsp;<a class="span_a_style" download="'+1+'" onclick="downloadFile(  '+ row +'  )">'+ logAttachementList[j].fileNameOld +'</a></td><tr>';
                                                    status_2++;
                                                }

                                            }
                                            childStr+='</table></div>';
                                        }
                                        childStr += _span;
                                        str+='<div class="logDiv_contRemark">';
                                        str+=childStr;
                                        str+='</div>';
                                        break;
                                    default:
                                        break;
                                }
                                str+='</div></div></div>';
                            }

                            $("#dealLogDiv").append( str );
                            $(This).removeClass("fa-angle-double-down");
                            $(This).addClass("fa-angle-double-up");
                            $(This).parents('.allInfo').children(".def_content").slideDown(100);
                        }
                    }
                    _flag=0;
                },
                error:function(){
                    $("#loading").css('display','none');
                    layer.alert(errorDefect, {
                        icon: 2,
                        title: "提示信息"
                    });
                }
            });
        }else if( _flag==0 ){
            $(This).removeClass("fa-angle-double-down");
            $(This).addClass("fa-angle-double-up");
            $(This).parents('.allInfo').children(".def_content").slideDown(100);
        }
    }else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
    }

}
// 提取文件图片显示
function classPath(fileType,classType){
    switch (fileType){
        case "doc":
        case "docx":
            classType = '<img src="'+_icon_word+'" />';
            break;
        case "xls":
        case "xlsx":
            classType = '<img src="'+_icon_excel+'" />';
            break;
        case "txt":
            classType = '<img src="'+_icon_text+'" />';
            break;
        case "pdf":
            classType = '<img src="'+_icon_pdf+'" />';
            break;
        case "JPG":
        case "jpg":
            classType ='<img src="'+_icon_JPG+'" />';
            break;
        case "PNG":
        case "png":
            classType ='<img src="'+_icon_PNG+'" />';
            break;
        default:
            classType ='<img src="'+_icon_WENHAO+'" />';
            break;
    }
    return classType;
}
function showMoreInfo(This){
    if( $(This).next(".file-upload-list").css("display")=="none" ){
    	$(This).html("收起备注信息 >");
    	$(This).next(".file-upload-list").css("display","block");
    }else {
    	$(This).html("更多备注信息 >");
    	$(This).next(".file-upload-list").css("display","none");
    }
}

//日志详情列表
function logDetailList(fieldName,oldName,newName){
    var arr = [];
    switch (fieldName){
        case "缺陷状态":
            for (var k = 0;k < defectStatusList.length; k++) {
                if( defectStatusList[k].valueCode!='' ){
                    if( oldName == defectStatusList[k].valueCode ){
                        oldName=defectStatusList[k].valueName;
                    }
                    if( newName == defectStatusList[k].valueCode ){
                        newName=defectStatusList[k].valueName;
                    }
                }
            }
            break;
        case "缺陷类型":
            for (var k = 0;k < defectTypeList.length; k++) {
                if( defectTypeList[k].valueCode!='' ){
                    if( oldName == defectTypeList[k].valueCode ){
                        oldName=defectTypeList[k].valueName;
                    }
                    if( newName == defectTypeList[k].valueCode ){
                        newName=defectTypeList[k].valueName;
                    }
                }
            }
            break;
        case "缺陷来源":
            for (var k = 0;k < defectSourceList.length; k++) {
                if( defectSourceList[k].valueCode!='' ){
                    if( oldName == defectSourceList[k].valueCode ){
                        oldName=defectSourceList[k].valueName;
                    }
                    if( newName == defectSourceList[k].valueCode ){
                        newName=defectSourceList[k].valueName;
                    }
                }
            }
            break;
        case "严重级别":
            for (var k = 0;k < severityLevelList.length; k++) {
                if( severityLevelList[k].valueCode!='' ){
                    if( oldName == severityLevelList[k].valueCode ){
                        oldName=severityLevelList[k].valueName;
                    }
                    if( newName == severityLevelList[k].valueCode ){
                        newName=severityLevelList[k].valueName;
                    }
                }
            }
            break;
        case "紧急程度":
            for (var k = 0;k < emergencyLevelList.length; k++) {
                if( emergencyLevelList[k].valueCode!='' ){
                    if( oldName == emergencyLevelList[k].valueCode ){
                        oldName = "P"+emergencyLevelList[k].valueName;
                    }
                    if( newName == emergencyLevelList[k].valueCode ){
                        newName = "P"+emergencyLevelList[k].valueName;
                    }
                }
            }
            break;
        case "驳回理由":
            for (var k = 0;k < rejectionList.length; k++) {
                if( rejectionList[k].valueCode!='' ){
                    if( oldName == rejectionList[k].valueCode ){
                        oldName=rejectionList[k].valueName;
                    }
                    if( newName == rejectionList[k].valueCode ){
                        newName=rejectionList[k].valueName;
                    }
                }
            }
            break;
        case "解决情况状态":
            for (var k = 0;k < solveStatusList.length; k++) {
                if( solveStatusList[k].valueCode!='' ){
                    if( oldName == solveStatusList[k].valueCode ){
                        oldName=solveStatusList[k].valueName;
                    }
                    if( newName == solveStatusList[k].valueCode ){
                        newName=solveStatusList[k].valueName;
                    }
                }
            }
            break;
        default:
            break;
        }
    arr.push(oldName);
    arr.push(newName);
    return arr;
}
//缺陷处理日志中 扩展字段的 处理方法
function dealExtend( obj1 , obj2 ){ 
	var str=''; 
	var oldObj={};
	var newObj={};
	for( var i=0 ; i<obj1.length ; i++ ){
		if( obj1[ i ].required == "true" ){
			oldObj[ obj1[i].fieldName ] = {};
			oldObj[ obj1[i].fieldName ].valueName = obj1[i].valueName;
			oldObj[ obj1[i].fieldName ].labelName = obj1[i].labelName; 
		} 
	}
	for( var i=0 ; i<obj2.length ; i++ ){
		if( obj2[ i ].required == "true" ){
			newObj[ obj2[i].fieldName ] = {};
			newObj[ obj2[i].fieldName ].valueName = obj2[i].valueName;
			newObj[ obj2[i].fieldName ].labelName = obj2[i].labelName; 
		} 
	} 
	for( var key in newObj ){
	    if( oldObj[ key ] == undefined ){
	    	str += newObj[ key ].labelName + ' 修改为  "' + newObj[ key ].valueName + '"<br>';
	    } else{
	    	if( oldObj[ key ].valueName != newObj[ key ].valueName ){
	    		str +=newObj[ key ].labelName +  '"' + oldObj[ key ].valueName + '" 修改为  "' + newObj[ key ].valueName + '"<br>';
	    	}
	    }  
	} 
	return str;
}
//下载文件
function dftDownloadFile(row){
    var url = "/zuul/devManage/defect/downloadFile?fileS3Bucket="+row.fileS3Bucket+"&fileNameOld="+row.fileNameOld+"&fileS3Key="+row.fileS3Key;
    window.location.href = encodeURI(url);
}