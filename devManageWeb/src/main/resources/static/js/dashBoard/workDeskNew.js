/**
 * Created by 朱颜辞镜花辞树 on 2018/9/18.

 */
var _icon_word ="../static/images/devtask/word.png";
var _icon_excel ="../static/images/devtask/excel.png";
var _icon_text = "../static/images/devtask/text.png";
var _icon_pdf = "../static/images/devtask/pdf.png";
var _icon_img = "../static/images/devtask/img.png";
var _icon_other = "../static/images/devtask/other.png";
var devtaskStatusList =[];//状态
var _SeeFiles =[];
var modified  = "<span>&nbsp;修改为&nbsp;</span>";
var _flag=1;
var defectStatusList = '';
var solveStatusList = '';
var rejectionList = '';
var severityLevelList = '';
var emergencyLevelList = '';
var defectTypeList = '';
var defectSourceList = '';
var editAttList = [];
var errorDefect = '系统内部错误，请联系管理员 ！！！';
var noPermission = '没有操作权限 ！！！';
var devDefectUrl = '/devManage/';
var jiantou = "<span>&nbsp;&nbsp;修改为&nbsp;&nbsp;</span>";
var _files = [];
var _editfiles = [];
var _checkfiles = [];//工作备注的附件
var modalType = '';
 $(document).ready(function() {
	 $(".fa-bell-o").bind("click",function(){  
    	if( $(this).parent().parent().hasClass("shrink") ){
    		window.localStorage.workDesk = "hide";
    		$(this).parent().parent().removeClass("shrink");
    	}else{
    		window.localStorage.workDesk = "show";
    		$(this).parent().parent().addClass("shrink");
    	}
    })
    if( window.localStorage.workDesk == undefined || window.localStorage.workDesk == "hide" ){
    	$(".fa-bell-o").parent().parent().removeClass("shrink");
    }else{
    	$(".fa-bell-o").parent().parent().addClass("shrink");
    }
	 uploadFileList();
	 uploadFileList2();
    $("#details").setWorkDesk();//调用下面js中的方法     
    getRemindInfo();
//    defectStatusList = $("#defectStatus").find("option");
    devtaskStatusList = getReqFeatureStatus();
    solveStatusList = $("#opt_solution").find("option");
    rejectionList = $("#opt_rejection").find("option");
    severityLevelList = $("#severityLevel").find("option");
    emergencyLevelList = $("#emergencyLevel").find("option");
    defectTypeList = $("#defectType").find("option");
    defectSourceList = $("#defectSource").find("option");
    var clipboard = new ClipboardJS("#copyWordCode");
    clipboard.on('success', function(e) {
        layer.msg("<span style='color:white'>复制成功</span>", {
            area:["150px","48px"],
            time:2000
        });
    });
    clipboard.on('error', function(e) {
        layer.msg("<span style='color:white'>复制失败,请手动复制</span>", {
            area:["150px","48px"],
            time:2000
        });
    }); 
});
jQuery.fn.extend({
	setWorkDesk:function(type){
		var target = this;
	    $.ajax({
	    	type:"POST",
            url:"/devManage/dashBoard/getUserDesk",
            dataType:"json",
            success:function(data){ 
				//待处理流程	
            	$("#processing_flow_List").empty();
            	var approveRequests = data.approveRequests;
							if(approveRequests.length){
								for(var i=0;i<approveRequests.length;i++){
									let temp = JSON.stringify({
										projectId : approveRequests[i].projectId,	
										projectName : approveRequests[i].projectName,	
										requestUserId : approveRequests[i].id,	
									});
									str =`<div class="aTasks rowdiv">
										<div class="def_col_4">
											<span class="taskStatus colorpink">PMO审批</span>
										</div>
										<div class="def_col_16">
											<a onclick='showPlanChart(${temp})' class="pointStyle">项目里程碑审批</a> | 
											<a onclick='showPlanChart(${temp})' class="pointStyle" title="${delNull(approveRequests[i].projectName)}">${delNull(approveRequests[i].projectName)}</a>
										</div>     						
										<div class="def_col_9">
											<span title="${delNull(approveRequests[i].userName)}">提交人：${delNull(approveRequests[i].userName)}</span>
										</div>
									</div>`			
									$("#processing_flow_List").append( str );
								} 
							}
							var approveRequests1 = data.approveRequests1;
							if(approveRequests1.length){
								for(var i=0;i<approveRequests1.length;i++){
									let temp = JSON.stringify({
											projectId : approveRequests1[i].projectId,	
											projectName : approveRequests1[i].projectName,	
											requestUserId : approveRequests1[i].id,	
									});
									str =`<div class="aTasks rowdiv">
											<div class="def_col_4">
												<span class="taskStatus colorInImplement">PMO申请</span>
											</div>
											<div class="def_col_16">
												<a onclick='showPlanChart(${temp})' class="pointStyle">项目里程碑申请</a> | 
												<a onclick='showPlanChart(${temp})' class="pointStyle" title="${delNull(approveRequests1[i].projectName)}">${delNull(approveRequests1[i].projectName)}</a>
											</div>     						
											<div class="def_col_9">
												<span title="${delNull(approveRequests1[i].userName)}">提交人：${delNull(approveRequests1[i].userName)}</span>
											</div>
										</div>`			
									$("#processing_flow_List").append( str );
								} 
							}

            	//开发任务  - 开发管理岗代办 
            	var featureList=data["featureList"];
            	for(var i=0;i<featureList.length;i++){
            		var span=getSpan(featureList[i].statusName);
      				var str ='<div class="aTasks rowdiv">'+
      				   '<div class="def_col_4">'+span+'</div>'+
      				   '<div class="def_col_16"><a onclick="showDevTask('+featureList[i].id+')" class="pointStyle" title="'+delNull(featureList[i].featureCode)+'">'+
      				      delNull(featureList[i].featureCode)+'</a> | <a onclick="showDevTask('+featureList[i].id+')" class="pointStyle" title="'+delNull(featureList[i].featureName)+'">'+
      				      delNull(featureList[i].featureName)+'</a></div>'+     						
      				   '<div class="def_col_9"><span title="'+delNull(featureList[i].systemName)+'">'+
      				   	  delNull(featureList[i].systemName)+'</span></div>'+
                       '<div class="def_col_7"><span title="'+delNull(featureList[i].windowName)+'">'+
                       	  delNull(featureList[i].windowName)+'</span></div></div>'
      				$("#featureList").append( str );
      			}
            	//开发任务  - 执行人代办
            	var excuteFeatureList=data["excuteFeatureList"];
            	for(var i=0;i<excuteFeatureList.length;i++){ 
            		var span=getSpan2(excuteFeatureList[i].requirementFeatureStatus,1);
      				var str ='<div class="aTasks rowdiv">'+
      				   '<div class="def_col_4">'+span+'</div>'+
      				   '<div class="def_col_16"><a onclick="showDevTask('+excuteFeatureList[i].id+')" class="pointStyle" title="'+delNull(excuteFeatureList[i].featureCode)+'">'+
      				      delNull(excuteFeatureList[i].featureCode)+'</a> | <a onclick="showDevTask('+excuteFeatureList[i].id+')" class="pointStyle" title="'+delNull(excuteFeatureList[i].featureName)+'">'+
      				      delNull(excuteFeatureList[i].featureName)+'</a></div>'+     						
      				   '<div class="def_col_9"><span title="'+delNull(excuteFeatureList[i].systemName)+'">'+
      				   	  delNull(excuteFeatureList[i].systemName)+'</span></div>'+
                       '<div class="def_col_7"><span title="'+delNull(excuteFeatureList[i].windowName)+'">'+
                       	  delNull(excuteFeatureList[i].windowName)+'</span></div></div>'
      				$("#excuteFeatureList").append( str );
      			}
            	//工作任务
            	var taskList=data["devTaskList"];
            	for(var i=0;i<taskList.length;i++){
            		var span=getSpan(taskList[i].statusName);
      				str ='<div class="aTasks rowdiv">'+
      				   '<div class="def_col_4">'+ span+'</div>'+
      				   '<div class="def_col_16"><a onclick="showWorkTask('+taskList[i].id+','+taskList[i].requirementFeatureId+')" class="pointStyle" title="'+delNull(taskList[i].devTaskCode)+'">'+
      				      delNull(taskList[i].devTaskCode)+'</a> | <a onclick="showWorkTask('+taskList[i].id+','+taskList[i].requirementFeatureId+')" class="pointStyle" title="'+delNull(taskList[i].devTaskName)+'">'+
      				      delNull(taskList[i].devTaskName)+'</a></div>'+     						
      				   '<div class="def_col_9"><span title="'+delNull(taskList[i].systemName)+'">'+
      				   	  delNull(taskList[i].systemName)+'</span></div></div>'			
      				$("#taskList").append( str );
      			} 
            	
            	//我的缺陷
            	var defectList=data["defectList"];
            	for(var i=0;i<defectList.length;i++){
            		var span=getSpan(defectList[i].statusName);
      				str ='<div class="aTasks rowdiv">'+
      				   '<div class="def_col_4">'+ span+'</div>'+
      				   '<div class="def_col_16"><a onclick="showDefect('+defectList[i].id+')" class="pointStyle" title="'+delNull(defectList[i].defectCode)+'">'+
      				      delNull(defectList[i].defectCode)+'</a> | <a onclick="showDefect('+defectList[i].id+')" class="pointStyle" title="'+delNull(defectList[i].defectSummary)+'">'+
      				      delNull(defectList[i].defectSummary)+'</a></div>'+     						
      				   '<div class="def_col_9"><span title="'+delNull(defectList[i].systemName)+'">'+
      				      delNull(defectList[i].systemName)+'</span></div></div>'			
      				$("#defectList").append( str );
      			}  
            }//success
        });//$.ajax
	    $.ajax({
	    	type:"POST",
            url:"/testManage/testWorkplace/getTestUserDask",
            dataType:"json",
            success:function(data){    
            	//开发任务
            	var featureList=data.featureList;
            	for(var i=0;i<featureList.length;i++){
            		var span=getSpan2(featureList[i].requirementFeatureStatus,1);
      				str ='<div class="aTasks rowdiv">'+
      				   '<div class="def_col_4">'+span+'</div>'+
      				   '<div class="def_col_16"><a onclick="showtestTask('+featureList[i].id+')" class="pointStyle" title="'+delNull(featureList[i].featureCode)+'">'+
      				      delNull(featureList[i].featureCode)+'</a> | <a onclick="showtestTask('+featureList[i].id+')" class="pointStyle" title="'+delNull(featureList[i].featureName)+'">'+
      				      delNull(featureList[i].featureName)+'</a></div>'+     						
      				   '<div class="def_col_9"><span title="'+delNull(featureList[i].systemName)+'">'+
      				   	  delNull(featureList[i].systemName)+'</span></div>'+
                       '<div class="def_col_7"><span title="'+delNull(featureList[i].windowName)+'">'+
                       	  delNull(featureList[i].windowName)+'</span></div></div>'
      				$("#testList").append( str );
      			}
            	var excuteFeatureList=data.excuteFeatureList;
            	for(var i=0;i<excuteFeatureList.length;i++){
            		var span=getSpan2(excuteFeatureList[i].requirementFeatureStatus,1);
      				str ='<div class="aTasks rowdiv">'+
      				   '<div class="def_col_4">'+span+'</div>'+
      				   '<div class="def_col_16"><a onclick="showtestTask('+excuteFeatureList[i].id+')" class="pointStyle" title="'+delNull(excuteFeatureList[i].featureCode)+'">'+
      				      delNull(excuteFeatureList[i].featureCode)+'</a> | <a onclick="showtestTask('+excuteFeatureList[i].id+')" class="pointStyle" title="'+delNull(excuteFeatureList[i].featureName)+'">'+
      				      delNull(excuteFeatureList[i].featureName)+'</a></div>'+     						
      				   '<div class="def_col_9"><span title="'+delNull(excuteFeatureList[i].systemName)+'">'+
      				   	  delNull(excuteFeatureList[i].systemName)+'</span></div>'+
                       '<div class="def_col_7"><span title="'+delNull(excuteFeatureList[i].windowName)+'">'+
                       	  delNull(excuteFeatureList[i].windowName)+'</span></div></div>'
      				$("#testExcuteFeatureList").append( str );
      			}
            	//工作任务
            	var taskList=data["testTaskList"];
            	for(var i=0;i<taskList.length;i++){
            		var span=getSpan2(taskList[i].testTaskStatus,2);
      				str ='<div class="aTasks rowdiv">'+
      				   '<div class="def_col_5">'+ span+'</div>'+
      				   '<div class="def_col_15"><a onclick="showTestWorkTask('+taskList[i].id+')" class="pointStyle" title="'+delNull(taskList[i].devTaskCode)+'">'+
      				      delNull(taskList[i].testTaskCode)+'</a> | <a onclick="showTestWorkTask('+taskList[i].id+'	)" class="pointStyle" title="'+delNull(taskList[i].devTaskName)+'">'+
      				      delNull(taskList[i].testTaskName)+'</a></div>'+     						
      				   '<div class="def_col_9"><span title="'+delNull(taskList[i].systemName)+'">'+
      				   	  delNull(taskList[i].systemName)+'</span></div>'+
      				   	'<div class="def_col_7"><span title="'+  ( taskList[i].testStage == 1 ? "系测" : "版测" )  +'">'+
      				  ( taskList[i].testStage == 1 ? "系测" : "版测" ) +'</span></div></div>'			
      				$("#testWorkList").append( str );
      			} 
            	
            	//我的缺陷
            	var defectList=data["defectList"];
            	for(var i=0;i<defectList.length;i++){
            		var span=getSpan2(defectList[i].defectStatus,3);
      				str ='<div class="aTasks rowdiv">'+
      				   '<div class="def_col_4">'+ span+'</div>'+
      				   '<div class="def_col_16"><a onclick="checkDefect('+defectList[i].id+')" class="pointStyle" title="'+delNull(defectList[i].defectCode)+'">'+
      				      delNull(defectList[i].defectCode)+'</a> | <a onclick="checkDefect('+defectList[i].id+')" class="pointStyle" title="'+delNull(defectList[i].defectSummary)+'">'+
      				      delNull(defectList[i].defectSummary)+'</a></div>'+     						
      				   '<div class="def_col_9"><span title="'+delNull(defectList[i].systemName)+'">'+
      				      delNull(defectList[i].systemName)+'</span></div></div>'			
      				$("#testDefectList").append( str );
      			}  
            }//success
        });//$.ajax
    }//setRoleList:function(type)
});//jQuery.fn.extend

//根据状态不同显示不同的颜色
function getSpan (statusName) {
	var span=""
	if(statusName=='待实施'){
		span='<span class="taskStatus colorpink">'+statusName+'</span>'
	}else if(statusName=='待确认'){
		span='<span class="taskStatus colorConfirm">'+statusName+'</span>'
	}else if(statusName=='实施中'){
		span='<span class="taskStatus colorInImplement">'+statusName+'</span>'
	}else if(statusName=='修复中'){
		span='<span class="taskStatus colorInRepair">'+statusName+'</span>'
	}else if(statusName=='待开发'){
		span='<span class="taskStatus colorpink">'+statusName+'</span>'
	}else if(statusName=='开发中'){
		span='<span class="taskStatus colorInImplement">'+statusName+'</span>'
	}else{
		span='<span class="taskStatus">'+span+'</span>'
	}
	return span;
}

//根据状态显示不同的颜色
function getSpan2 (statusName,status) {
	var span="";
	var colorClassArr = [ "colorpink","colorConfirm","colorInImplement" ,"colorInRepair" ]
	if( status == 1 ){
		reqFeatureStatusList.map(function(val,index){ 
			if( Number( statusName ) == Number( val[0] ) ){
				var colorNum = Number( statusName ) % colorClassArr.length;
				span='<span class="taskStatus '+ colorClassArr[colorNum] +'">'+val[1]+'</span>';
			}
		})
	}else if( status == 2 ){
		testTaskStatusList.map(function(val,index){ 
			if( Number( statusName ) == Number( val[0] ) ){
				var colorNum = Number( statusName ) % colorClassArr.length;
				span='<span class="taskStatus '+ colorClassArr[colorNum] +'">'+val[1]+'</span>';
			}
		})
	}else if( status == 3 ){
		testDefectStatusList.map(function(val,index){ 
			if( Number( statusName ) == Number( val[0] ) ){
				var colorNum = Number( statusName ) % colorClassArr.length;
				span='<span class="taskStatus '+ colorClassArr[colorNum] +'">'+val[1]+'</span>';
			}
		})
	}
	  
	return span; 
} 

//由环境编码获取环境名
function getEnvironmentType (environmentType) {
	var str=""
    $.ajax({
        type:"POST",
        url:"/devManage/dashBoard/getEnvironmentType",
        dataType:"json",
        data:{
            "environmentType":environmentType
        },
        async:false,
        success:function(data){
            str = data.typeValue;
        }
    });
	return str;
}

//由构建状态重新组装显示：未用
function getBuildStatus(buildStatus) {
	var str=""
    if(buildStatus==1){
    	str="构建中 | 代码扫描中 ";
    }else if(buildStatus==2){
    	str="构建成功 | 代码扫描成功 ";
    }else if(buildStatus==3){
    	str="构建失败 | 代码扫描失败 ";
    }
	return str;
}

function timestampToTime(timestamp) {
	var str="";
	if(timestamp!=null){
	    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
	    var Y = date.getFullYear() + '-';
	    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	    var D = date.getDate() + '&nbsp;';
	    var h = date.getHours() + ':';
	    var m = date.getMinutes() + ':';
	    var s = date.getSeconds();
	    str=Y+M+D+h+m+s;
	}
    return str;
}

function getColor(j){
	var color="";
	if(j==0){
		color="#49A9EE";
	}else if(j==1){
		color="#98D87D";
	}else if(j==2){
		color="#FFD86E";
	}else if(j==3){
		color="#F3857B";
	}else if(j==4){
		color="#8996E6";
	}else if(j==5){
		color="#FF0000";
	}else if(j==6){
		color="#FF9900";
	}else if(j==7){
		color="#FF00FF";
	}else if(j==8){
		color="#0000FF";
	}else if(j==9){
		color="#00FFFF";
	}else if(j==10){
		color="#CCCCFF";
	}	
	return color;
}

function delNull(str){
	if(str==undefined){
		str="";
	}
	return str;
}

//点击More跳转到开发任务首页
function toDevTask(){
	var url = devTaskMenuUrl+"?requirementFeatureStatus=01,02";
	window.parent.toPageAndValue( devTaskMenuName , devTaskMenuId , url );
}
//点击More跳转到工作任务首页
function toWorkTask(){
	var url = worktaskMenuUrl+"?devTaskStatus=1,2";
	window.parent.toPageAndValue( worktaskMenuName , worktaskMenuId , url );
}

//点击More跳转到缺陷首页
function toDefect(){
	var url = defectUrl+"?defectStatusList=2,4";
	window.parent.toPageAndValue( defectName , detectId , url );
}

//点击More跳转到测试任务首页
function toTestTask(){ 
	var url = testTaskMenuUrl+"?testTaskStatus=01,02,05,06&userName="+$("#userName",window.parent.document).val() +"&userId="+$("#userId",window.parent.document).val();
	window.parent.toPageAndValue( testTaskMenuName , testTaskMenuId , url );
}
//点击More跳转到测试工作任务首页
function toTestWorkTask(){
	var url = testWorktaskMenuUrl+"?TaskPType=1,2&userName="+$("#userName",window.parent.document).val() +"&userId="+$("#userId",window.parent.document).val();
	window.parent.toPageAndValue( testWorktaskMenuName , testWorktaskMenuId , url );
}
//点击More跳转到测试缺陷首页
function toTestDefect(){
	var url = testDefectUrl+"&defectStatus=2,4&userName="+$("#userName",window.parent.document).val() +"&userId="+$("#userId",window.parent.document).val();
	window.parent.toPageAndValue( testDefectName , testDetectId , url );
}
//文件下载
function download(that){
	var fileS3Bucket = $(that).children(".file-bucket").text();
	var fileS3Key = $(that).children(".file-s3Key").text();
	var fileNameOld = $(that).children("span").text();
	var url = encodeURI("/devManage/devtask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld);
	window.location.href = url;
}
//查看需求附件
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
	  content: reqAttUrl +"?reqId="+requirementId+"&reqtaskId="+requirementId+"&taskreqFlag=requirement"
	});
}

//开发任务查看详情
//function showFeature(id) {
//	var id = id;
//	$("#loading").css('display','block');
//	$.ajax({
//		url:"/devManage/devtask/getOneDevTask2",
//		type:"post",
//		dataType:"json",
//		data:{
//			"id":id
//		},
//		success: function(data) {
//			$("#loading").css('display','none');
//			$(".dealLog").css('display','none');
//			$(".workRemarks").css('display','block');
//			$("#checkoutrequirementDiv").hide();
//			$("#checkDefectDiv").hide();
//			$("#checkRequstNumberDiv").hide();
//			$("#checksystemName").text('');
//			$("#checkdeptName").text('');
//			$("#checkfiles").val('');
//			$("#checkreqFeaturePriority").text('');
//			$("#connectDiv").empty();
//			$("#brother_div").empty();
//			$("#defect_div").empty();
//			$("#checkFileTable").empty();
//			$("#remarkBody").empty();
//			$("#remarkDetail").val('');
//			$("#checkAttTable").empty();
//			$("#handleLogs").empty();
//			$("#brother_div").empty();
//			$("#checkDefect").empty();
//			$("#checkdevTaskTitle").text( toStr(data.featureCode) + toStr(data.featureName) );
//			$("#checkdevTaskOverview").text(data.featureOverview);
//			$("#checkreqFeaturePriority").text(data.requirementFeaturePriority);
//			
//			var statusName = '';
//			for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
//                if(devtaskStatusList[i].value == data.requirementFeatureStatus){
//                	statusName=  devtaskStatusList[i].innerHTML;
//             	   break;
//                }
//              }
//			$("#checkdevTaskStatus").text(statusName);
//			
//			$("#checkdevManPost").text("");
//			$("#checkdevManPost").text(data.manageUserName);
//			$("#checkexecutor").text("");
//			$("#checkexecutor").text(data.executeUserName);
//			$("#checksystemName").text("");
//			$("#checksystemName").text(data.systemName);
//			$("#checkoutrequirement").text("");
//			$("#checkRequstNumber").text("");
//			
//			$("#checkWindowName").text("");
//			$("#checkWindowName").text(data.windowName);
//			$("#checkSystemVersion").text("");
//			$("#checkSystemVersion").text(toStr(data.versionName)+"-->"+toStr(data.systemScmBranch));
//			
//			$("#checkItcdReqId").val("");
//			$("#checkItcdReqId").val(data.itcdRequrementId);
//			$("#checktaskId").val("");
//			$("#checktaskId").val(data.taskId);
//			
//			if(data.requirementFeatureSource!=undefined){
//				if(data.requirementFeatureSource==1){
//					$("#checkoutrequirementDiv").show();
//					$("#checkDefectDiv").hide();
//					$("#checkRequstNumberDiv").hide();
//					$("#checkoutrequirement").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
//				}else if(data.requirementFeatureSource==2){
//					$("#checkoutrequirementDiv").hide();
//					$("#checkDefectDiv").hide();
//					$("#checkRequstNumberDiv").show();
//					$("#checkRequstNumber").text(data.questionNumber);
//				}else if(data.requirementFeatureSource==3){
//					$("#checkoutrequirementDiv").hide();
//					$("#checkDefectDiv").show();
//					$("#checkRequstNumberDiv").hide();
//					var dftName='';
//					for(var i=0;i<data.defectInfos.length;i++){
//						if(data.defectInfos[i].requirementFeatureId == id){
//							var obj = JSON.stringify(    data.defectInfos[i]   ).replace(/"/g, '&quot;');
//							dftName+= data.defectInfos[i].defectCode;
//						}
//					}
//					$("#checkDefect").append(dftName.substring(0,dftName.length-1));
//				}
//			}
//			$("#checkdeptName").text(data.deptName);
//			var status = "";
//			if(data.temporaryStatus =="1"){
//				status = "是";
//			}else if(data.temporaryStatus =="2"){
//				status = "否";
//			}
//			$("#checktemporaryTask").text(status);//1是2否
//			$("#checkpreStartDate").text('');
//			$("#checkpreStartDate").text(data.planStartDate);
//			$("#checkpreEndDate").text('');
//			$("#checkpreEndDate").text(data.planEndDate);
//			$("#checkpreWorkload").text('');
//			$("#checkpreWorkload").text(data.planWorkload);
//			$("#checkactStartDate").text('');
//			$("#checkactStartDate").text(data.actualStartDate);
//			$("#checkactEndDate").text('');
//			$("#checkactEndDate").text(data.actualEndDate);
//			$("#checkactWorkload").text('');
//			$("#checkactWorkload").text(data.actualWorkload);
//			$("#checkhandSug").text('');
//			$("#checkhandSug").text(data.handleSuggestion);
//			$("#checkReqFeatureId").val(id);
//			var type = '';
//			if(data.createType == "1" ){
//				type = "自建";
//				$("#createfiles").show();
//				$("#synfiles").hide();
//			}else if(data.createType == "2"){
//				type = "同步";
//				$("#synfiles").show();
//				$("#createfiles").hide();
//			}
//			$("#checkcreateType").text(type);
//			//下属工作任务的显示
//			if(data.list!=undefined){
//				for(var i=0;i<data.list.length;i++){
//					$("#connectDiv").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt">'+data.list[i].id+data.list[i].devTaskCode+'   '+data.list[i].devTaskName+'</div>'+
//							'<div class="def_col_36">预估工作情况：'+toStr(data.list[i].planStartDate)+'~'+toStr(data.list[i].planEndDate)+' '+toStr(data.list[i].planWorkLoad)+'人天</div>'+
//							'<div class="def_col_36">实际工作情况：'+toStr(data.list[i].actualStartDate)+'~'+toStr(data.list[i].actualEndDate)+' '+toStr(data.list[i].actualWorkload)+'人天</div>'+
//							'<div class="def_col_36">'+data.list[i].devtaskStatusName+' '+toStr(data.list[i].devUsername)+'</div></div>');
//				}
//			}
//			//相关开发任务的显示
//			if(data.brother!=undefined){
//				for(var i=0;i<data.brother.length;i++){
//					$("#brother_div").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt">'+data.brother[i].id+')">'+toStr(data.brother[i].featureCode)+'  '+data.brother[i].featureName+'</div>'+
//							'<div class="def_col_36">实际工作情况：'+toStr(data.brother[i].actualStartDate)+'~'+toStr(data.brother[i].actualEndDate)+' '+toStr(data.brother[i].actualWorkload)+'人天</div>'+
//							'<div class="def_col_36">'+data.brother[i].statusName+' '+toStr(data.brother[i].executeUserName)+' 预排期：'+toStr(data.brother[i].windowName)+'</div>');
//				}
//			} 
//			//相关附件显示
//			if(data.attachements!=undefined){
//				var _table = $("#checkFileTable");
//				for(var i=0;i<data.attachements.length;i++){
//					var _tr = '';
//					var file_name = data.attachements[i].fileNameOld;
//					var file_type = data.attachements[i].fileType;
//					var _td_icon;
//					var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.attachements[i].fileS3Bucket+'</i><i class = "file-s3Key">'+data.attachements[i].fileS3Key+'</i></td>';
//					switch(file_type){
//						case "doc":
//						case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
//						case "xls":
//						case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
//						case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
//						case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
//						case "png":
//						case "jpeg":
//						case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
//						default:_td_icon = '<img src="'+ _icon_other+'" />';break;
//					}
//					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(this)">'+_td_icon+" "+_td_name+'</tr>'; 
//					
//					_table.append(_tr);  
//				}
//			}
//			//备注
//			 _checkfiles = [];
//			if(data.remarks!=undefined){
//				var str ='';
//				for(var i=0;i<data.remarks.length;i++){
//					var style= '';
//					if(i==data.remarks.length-1){
//						style= ' lastLog';
//					}
//				 str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span>'+
//				'<span>'+data.remarks[i].userName+'  | '+data.remarks[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.remarks[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
//			    '<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><span>'+data.remarks[i].requirementFeatureRemark+'</span>'+
//				'<div class="file-upload-list">';
//				if(data.remarks[i].remarkAttachements.length>0){
//					str +='<table class="file-upload-tb">';
//					var _trAll = '';
//					for(var j=0;j<data.remarks[i].remarkAttachements.length;j++){
//						
//						var _tr = '';
//						var file_name = data.remarks[i].remarkAttachements[j].fileNameOld;
//						var file_type = data.remarks[i].remarkAttachements[j].fileType;
//						var _td_icon;
//						var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.remarks[i].remarkAttachements[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.remarks[i].remarkAttachements[j].fileS3Key+'</i></td>';
//						switch(file_type){
//							case "doc":
//							case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
//							case "xls":
//							case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
//							case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
//							case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
//							case "png":
//							case "jpeg":
//							case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
//							default:_td_icon = '<img src="'+ _icon_other+'" />';break;
//						}
//					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(this)">'+_td_icon+_td_name+'</tr>'; 
//					_trAll +=_tr;
//					}
//					str+= _trAll+'</table>';
//				}
//				
//				str += '</div></div></div></div></div>';
//						
//			}
//				$("#remarkBody").append(str);
//			
//			}
//			//处理日志
//			if(data.logs!=undefined){
//				var str ='';
//				for(var i=0;i<data.logs.length;i++){
//					var style= '';
//					if(i==data.logs.length-1){
//						style= ' lastLog';
//					}
//					var addDiv = '';
//					var logDetail = '';
//					var style2 = '';
//					
//					if((data.logs[i].logDetail==null || data.logs[i].logDetail=='')&&(data.logs[i].logAttachements==null || data.logs[i].logAttachements.length<=0)){
//						if(data.logs[i].logType!="新增开发任务"){
//							logDetail = "未作任何修改";
//						}
//						if(logDetail==''){
//							style2= 'style="display: none;"';
//						}
//						addDiv = '<br>';
//					}else{
//						logDetail = data.logs[i].logDetail;
//						logDetail=logDetail.replace(/；/g,"<br/>");
//					}
//					
//				
//				 str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span><span>'+data.logs[i].logType+'</span>&nbsp;&nbsp;&nbsp;'+
//				'<span>'+data.logs[i].userName+'  | '+data.logs[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.logs[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
//			    '<div class="logDiv_cont" ><div class="logDiv_contBorder"><div class="logDiv_contRemark" '+style2+'><span>'+logDetail+'</span>'+
//				'<div class="file-upload-list">';
//				if(data.logs[i].logAttachements.length>0){
//					str +='<table class="file-upload-tb">';
//					var _trAll = '';
//					for(var j=0;j<data.logs[i].logAttachements.length;j++){
//						var attType = '';
//						if(data.logs[i].logAttachements[j].status == 1){//新增的日志
//							attType = "<lable>新增附件：</lable>";
//						}else if(data.logs[i].logAttachements[j].status == 2){//删除的日志
//							attType = "<lable>删除附件：</lable>";
//						}
//						var _tr = '';
//						var file_name = data.logs[i].logAttachements[j].fileNameOld;
//						var file_type = data.logs[i].logAttachements[j].fileType;
//						var _td_icon;
//						var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.logs[i].logAttachements[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.logs[i].logAttachements[j].fileS3Key+'</i></td>';
//						switch(file_type){
//							case "doc":
//							case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
//							case "xls":
//							case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
//							case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
//							case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
//							case "png":
//							case "jpeg":
//							case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
//							default:_td_icon = '<img src="'+ _icon_other+'" />';break;
//						}
//					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(this)">'+attType+_td_icon+_td_name+'</tr>'; 
//					_trAll +=_tr;
//					}
//					str+= _trAll+'</table>';
//				}
//				
//				str += '</div></div>'+addDiv+'</div></div></div>';
//						
//			}
//				$("#handleLogs").append(str);
//			}
//			
//		}
//	});
//	modalType = 'check';
//    $("#checkDevTask").modal("show");
//}
//提交开发任务备注
function saveRemark(){
	var remark = $("#remarkDetail").val();
	var id = $("#checkReqFeatureId").val();
	if($("#remarkDetail").val()==''&& $("#checkfiles").val()==''){
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
			attachFiles :$("#checkfiles").val() 
		},
		success : function(data){
			if(data.status == "success"){
				layer.alert('保存成功！', {icon: 1});
				showDevTask(id);
				 _checkfiles = [];
				$("#checkfiles").val('');
			}else if(data.status =="2"){
				layer.alert('保存失败！！！', {icon: 2});
			}
		}
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


//查看
function showDevTask(id) {
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
			$("#checksystemName").text('');
			$("#checkdeptName").text('');
			$("#checkfiles").val('');
			$("#checkreqFeaturePriority").text('');
			$("#connectDiv").empty();
			$("#brother_div").empty();
			$("#defect_div").empty();
			$("#checkFileTable").empty();
			$("#remarkBody").empty();
			$("#remarkDetail").val('');
			$("#checkAttTable").empty();
			$("#handleLogs").empty();
			$("#brother_div").empty();
			$("#checkDefect").empty();
			$("#checkSprintName").text('');
			$("#checkStoryPoint").text('');
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
			
			if(data.requirementFeatureSource!=undefined){
				if(data.requirementFeatureSource==1){
					$("#checkoutrequirementDiv").show();
					$("#checkDefectDiv").hide();
					$("#checkRequstNumberDiv").hide();
					$("#checkoutrequirement").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
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
							dftName+= '<a class="a_style" onclick="showDefect2('+data.defectInfos[i].id+')"> '+data.defectInfos[i].defectCode+'</a>,';
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
					$("#connectDiv").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt">'+data.list[i].devTaskCode+'   '+data.list[i].devTaskName+'</div>'+
							'<div class="def_col_36">预估工作情况：'+toStr(data.list[i].planStartDate)+'~'+toStr(data.list[i].planEndDate)+' '+toStr(data.list[i].planWorkLoad)+'人天</div>'+
							'<div class="def_col_36">实际工作情况：'+toStr(data.list[i].actualStartDate)+'~'+toStr(data.list[i].actualEndDate)+' '+toStr(data.list[i].actualWorkload)+'人天</div>'+
							'<div class="def_col_36">'+data.list[i].devtaskStatusName+' '+toStr(data.list[i].devUsername)+'</div></div>');
				}
			}
			//相关开发任务的显示
			if(data.brother!=undefined){
				for(var i=0;i<data.brother.length;i++){
					$("#brother_div").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt">'+toStr(data.brother[i].featureCode)+'  '+data.brother[i].featureName+'</div>'+
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
			 _checkfiles = [];
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
			
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
	modalType = 'check';
    $("#checkDevTask").modal("show");
}

//开发任务详情页面中的关联缺陷详情
function showDefect2( defectId ){
	  $("#loading").css('display','block');
	    reset_opt();
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
	        url:devDefectUrl+"defect/getDefectRecentLogById",
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
	                    $("#check_devTaskCode").html(data.devTask.id+","+data.devTask.requirementFeatureId+data.devTask.devTaskCode);
	                    $("#check_devTaskName").html(data.devTask.id+","+data.devTask.requirementFeatureId+data.devTask.devTaskName);
	                }

	                if(data.feature != null && data.feature != "undefined"){
	                    $("#check_reqFetureCode").html(data.feature.id+data.feature.featureCode);
	                    $("#check_reqFetureName").html(data.feature.id+data.feature.featureName);
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
	                $("#checkDefectModel").modal("show");
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
//查看开发工作任务详情
function showWorkTask(id,requirementFeatureId){
	  $("#requirementFeatureId").val(requirementFeatureId);
	    $.ajax({
			method:"post", 
			url:"/devManage/worktask/getSeeDetail",
			data:{id:id,
              requirementFeatureId:requirementFeatureId},
			success : function(data) {
				selectdetail(data);
			}
		});
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
    		if(modalType == 'new'){
    			fileList=_files;
        	}else if(modalType == 'edit'){
        		fileList=_editfiles;
        	}else if(modalType == 'check'){
        		fileList=_checkfiles;
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
							//console.log("文件 "+file.name+" 读取成功！");
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
				case "bmp":
				case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
				default:_td_icon = '<img src="'+ _icon_other+'" />';break;
			}
			var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
			_tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>'; 
			_table.append(_tr);  
			
		} 
		//上传文件
    	$.ajax({
	        type:'post',
	        url:'/zuul/devManage/devtask/uploadFile',
	        contentType: false,  
	        processData: false,
	        dataType: "json",
	        data:formFile,
	        success:function(data){ 
	        	for(var k=0,len=data.length;k<len;k++){
	        		
	        		if(modalType == 'new'){
	        			_files.push(data[k]); 
	        		}else if(modalType == 'edit'){
	        			_editfiles.push(data[k]); 
	        		}else if(modalType == 'check'){
	        			_checkfiles.push(data[k]);
	        		}
		        	$(".file-upload-tb span").each(function(o){ 
		        		if($(this).text() == data[k].fileNameOld){ 
		        			$(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
		        			$(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
		        		}
		        	});
	        	}
	        	if(modalType == 'new'){
	        		$("#files").val(JSON.stringify(_files));
	        		clearUploadFile('uploadFile');
	        	}else if(modalType == 'edit'){
	        		$("#editfiles").val(JSON.stringify(_editfiles));
	        		clearUploadFile('edituploadFile');
	        	}else if(modalType == 'check'){
	        		$("#checkfiles").val(JSON.stringify(_checkfiles));
	        		clearUploadFile('checkuploadFile');
	        	}
	        },
	        error:function(){
	            $("#loading").css('display','none');
	            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
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
	if(modalType == "new"){
		var _file = $("#files").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_files = files;
			$("#files").val(JSON.stringify(files));
		}
		
	}else if(modalType == 'edit'){
		var _file = $("#editfiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_editfiles = files;
			$("#editfiles").val(JSON.stringify(files));
		}
	}else if(modalType == 'check'){
		var _file = $("#checkfiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_checkfiles  = files;
			$("#checkfiles").val(JSON.stringify(files));
		}
	}
}

//查看工作任务
function selectdetail(data){
	   /* $.ajax({
	        url:"/devManage/devtask/getOneDevTask2",
	        type:"post",
	        dataType:"json",
	        data:{
	            "id":data.requirementFeatureId
	        },
	        success: function(data) {
	            console.log(" data.windowName : "+ data.windowName)
	            $("#windowProduction").text("");
	            $("#windowProduction").text(data.windowName);
	            $("#systemVersion").text("");
	            $("#systemVersion").text(toStr(data.versionName)+"-->"+toStr(data.systemScmBranch));

	        }
	    });*/
	    $("#windowProduction").text("");
	    if(data.devTasks.windowName==undefined){
	    	 $("#windowProduction").text("");
	    }else{
	    	 $("#windowProduction").text(data.devTasks.windowName);
	    }
	   
	    $("#systemVersion").text("");
	    $("#systemVersion").text(toStr(data.devTasks.versionName)+"-->"+toStr(data.devTasks.systemScmBranch));
		
		$("#loading").css('display','block');
		$("#checkAttTable2").empty();
		$("#SeeFileTable").empty();
		$("#taskRemark").empty();
		$("#checkfiles").val("");
		$("#handleLogs2").empty();
		var map=data.dev;
		$("#SdevCode").html("");
		$("#SdevOverview").html("");
		$("#SStatus").html("");
		$("#devuserID").html("");
		
		$("#featureName").html("");
		
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
		$("#applyUserId").html("");
		$("#requirmentOverview").text("");
		$("#applyDeptId").html("");
		$("#expectOnlineDate").html("");
		$("#planOnlineDate").html("");
		$("#lastUpdateDate3").html("");
		$("#createDate3").html("");
		$("#openDate").html("");
		$("#tyaskRemark").val("");
		$("#planStartDate").html("");
		$("#planEndDate").html("");
		$("#SplanWorkload").html("");
		$("#actualEndDate").html("");
		$("#actualStartDate").html("");
		$("#actualWorkload").html("");
		$("#sprintName").text("");
			$("#SdevCode").html("["+map.devTaskCode+"]"+"  |  "+ map.devTaskName);
			$("#SdevOverview").html(map.devTaskOverview);
			var devTaskStatus=map.devStatus;
			$("#SStatus").html(devTaskStatus);
			
			$("#DevTaskID").val(map.id);
			$("#devuserID").html(map.devuserName);
			
			$("#createBy").html(map.createName);
			
			$("#createDate").html(datFmt(new Date(map.createDate),"yyyy年MM月dd"));
			if(map.planStartDate!=null){
				var planstartDate=datFmt(new Date(map.planStartDate),"yyyy年MM月dd");
				$("#planStartDate").html(planstartDate);
			}
			if(map.planEndDate!=null){
				var planEndDate=datFmt(new Date(map.planEndDate),"yyyy年MM月dd");
				$("#planEndDate").html(planEndDate);
			}
			
			if(map.actualStartDate!=null){
				var actualStartDate=datFmt(new Date(map.actualStartDate),"yyyy年MM月dd");
				$("#actualStartDate").html(actualStartDate);
			}
			if(map.actualEndDate!=null){
				var actualEndDate=datFmt(new Date(map.actualEndDate),"yyyy年MM月dd");
				$("#actualEndDate").html(actualEndDate);
			}
			$("#actualWorkload").html(map.actualWorkload);
			$("#featureName").html(map.devTaskCode+"&nbsp|&nbsp"+map.featureName);
			$("#SplanWorkload").html(map.planWorkload);
			$("#featureOverview").html(map.featureOverview);
			var featureStatus=map.requirementFeatureStatus;
			$("#requirementFeatureStatus").html(featureStatus);
			
			$("#requirementName").html("");
			$("#requirementName").html(map.requirementName);
			if(map.requirementOverview==null){
				$("#requirmentOverview").text("");
			}else{
				$("#requirmentOverview").text(map.requirementOverview);
			}
			$("#manageUserId").html(map.manageUserName);
			$("#executeUserId").html(map.executeUserName);
			$("#systemId").html(map.systemName);
			
			$("#requirementSource").html(map.requirementSource);
			$("#requirementType").html(map.requirementType);
			$("#requirementPriority").html(map.requirementPriority);
			$("#requirementPanl").html(map.requirementPanl);
			$("#requirementStatus").html(map.requirementStatus);
			$("#applyUserId").html(map.applyUserName);
			$("#applyDeptId").html(map.applyDeptName);
			$("#sprintName").html(map.sprintName);
			
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
									default:_td_icon = '<img src="'+_icon_word+'" />';break;
								}
								var row =  JSON.stringify( data.Attchement[j]).replace(/"/g, '&quot;');
								_tr+='<tr><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+_td_name+"</tr>"; 
							
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
						_tr+='<tr><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+attType+_td_icon+_td_name+"</tr>";
					
						_trAll +=_tr;
					}
						
					}
					
				}
				if(_trAll==undefined){_trAll=""}
				_trAll+=_span;
				
				str+= _trAll+'</table>';
				_trAll="";
				str += '</div></div>'+addDiv+'</div></div></div>';
						
			}
				$("#handleLogs2").append(str);
			}
			modalType = 'check';
			$("#loading").css('display','none');
		  $("#selectdetail").modal("show");
	}

function showThisDiv2(This,num){
	if( $(This).hasClass("def_changeTit") ){
		$("#titleOfwork2 .def_controlTit").addClass("def_changeTit");
		$(This).removeClass("def_changeTit");
		if( num==1 ){
			$("#handleLogs2").css("display","none");
			$("#remarks").css("display","block");
		}else if( num==2 ){
			$("#handleLogs2").css("display","block");
			$("#remarks").css("display","none");
		}
	}   
}

//文件上传，并列表展示
function uploadFileList2(){ 
	//列表展示
	$("#checkuploadFile2").change(function(){ 
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
			var oldFileList = [];
    		if(modalType == 'new'){
    			fileList=_files;
        	}else if(modalType == 'edit'){
        		fileList=Neweditfiles;
        		oldFileList=_editfiles;
        	}else if(modalType == 'check'){
        		fileList=_checkfiles;
        	}else if(modalType=='dHandle'){
        		fileList=_newDhandlefiles;
        		oldFileList=_Dhandlefiles;
    		}else if(modalType=='Handle'){
    			oldFileList=_handlefiles
    			fileList=_Newhandlefiles;
    		}else if(modalType=='review'){
    			fileList=NewCodeFiles;
    			oldFileList=OldCodeFiles;
    		}else if(modalType=="NOreview"){
    			fileList=_newNoReviewFailed;
    			oldFileList=_NoreviewFailed;
    		}
    		for(var j=0;j<fileList.length;j++){
    			if(fileList[j].fileNameOld ==file.name){
    				layer.alert(file.name +" 文件已存在", {
	                    icon: 2,
	                    title: "提示信息"
	                });
    				continue outer;
    			}
    		}
    		for(var j=0;j<oldFileList.length;j++){
    			if(oldFileList[j].fileNameOld ==file.name){
    				layer.alert(file.name +" 文件已存在", {
	                    icon: 2,
	                    title: "提示信息"
	                });
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
			var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile2(this)">删除</a></td>';
			switch(file_type){
				case "doc":
				case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
				case "xls":
				case "xlsx":_td_icon = '<img src="'+_icon_excel+'" />';break;
				case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
				case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
				default:_td_icon = '<img src="'+_icon_word+'" />';break;
			}
			var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
			_tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>'; 
			_table.append(_tr);  
			
		} 
		//上传文件
    	$.ajax({
	        type:'post',
	        url:'/zuul/devManage/worktask/uploadFile',
	        contentType: false,  
	        processData: false,
	        dataType: "json",
	        data:formFile,
	       
	        success:function(data){ 
	        	for(var k=0,len=data.length;k<len;k++){
	        		if(modalType == 'new'){
	        			_files.push(data[k]); 
	        		}else if(modalType == 'edit'){
	        			Neweditfiles.push(data[k]);
	        		}else if(modalType == 'check'){
	        			_checkfiles.push(data[k]);
	        		}else if(modalType=='dHandle'){
	        			_newDhandlefiles.push(data[k]);
	        		}else if(modalType=='Handle'){
	        			_Newhandlefiles.push(data[k]);
	        		}else if(modalType=='review'){
	        			NewCodeFiles.push(data[k]);
	        		}else if(modalType=='NOreview'){
	        			_newNoReviewFailed.push(data[k]);
	        		}
		        	$(".file-upload-tb span").each(function(o){ 
		        		if($(this).text() == data[k].fileNameOld){ 
		        			//$(this).parent().children(".file-url").html(data[k].filePath);
		        			$(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
		        			$(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
		        		}
		        	});
	        	}
	        	if(modalType == 'new'){
	        		$("#addfiles").val(JSON.stringify(_files));
	        		clearUploadFile2('uploadFile');
	        	}else if(modalType == 'edit'){
	        		//$("#editfiles").val(JSON.stringify(_editfiles));
	        		$("#newFiles").val(JSON.stringify(Neweditfiles));
	        		clearUploadFile2('edituploadFile');
	        	}else if(modalType == 'check'){
	        		$("#checkfiles2").val(JSON.stringify(_checkfiles));
	        		clearUploadFile2('checkuploadFile2');
	        	}else if(modalType=='dHandle'){
	        		$("#NewDHattachFiles").val(JSON.stringify(_newDhandlefiles));
	        		clearUploadFile2('DHituploadFile');
        		}else if(modalType=='Handle'){
        			$("#newHattachFiles").val(JSON.stringify(_Newhandlefiles));
	        		clearUploadFile2('HituploadFile');
        		}else if(modalType=='review'){
        			$("#NewCodeFiles").val(JSON.stringify(NewCodeFiles));
	        		clearUploadFile2('CodeFile');
        		}else if(modalType=='NOreview'){
        			$("#newreviewFiles").val(JSON.stringify(_newNoReviewFailed));
	        		clearUploadFile2('reviewUploadFile');
        		}
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown){ 
				layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
			}
	    });
	});
}

//清空上传文件
function clearUploadFile2(idName){
	/*$(idName).wrap('<form></form>');
	$(idName).unwrap();*/
	$('#'+idName+'').val('');
}

//移除上传文件
function delFile2(that,id){
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
	$(that).parent().parent().remove();
	removeFile2(fileS3Key);
}

//移除暂存数组中的指定文件
function removeFile2(fileS3Key){
	if(modalType == "new"){//新增
		var _file = $("#addfiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_files = files;
			$("#addfiles").val(JSON.stringify(files));
		}
		
	}else if(modalType == 'edit'){//修改
		var _file = $("#editfiles").val();
		var _newfile = $("#newFiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0;i<files.length;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_editfiles = files;
			$("#editfiles").val(JSON.stringify(files));
		}
		if(_newfile != ""){
			var files = JSON.parse(_newfile);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			Neweditfiles = files;
			$("#newFiles").val(JSON.stringify(files));
		}
	}else if(modalType == 'check'){
		var _file = $("#checkfiles2").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_checkfiles  = files;
			$("#checkfiles2").val(JSON.stringify(files));
		}
	}else if(modalType == 'dHandle'){
		var _file = $("#DHattachFiles").val();
		var _newfile = $("#NewDHattachFiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_Dhandlefiles  = files;
			$("#DHattachFiles").val(JSON.stringify(files));
		}
		if(_newfile != ""){
			var files = JSON.parse(_newfile);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_newDhandlefiles = files;
			$("#NewDHattachFiles").val(JSON.stringify(files));
		}
	}else if(modalType == 'Handle'){
		var _file = $("#HattachFiles").val();
		var _newfile = $("#newHattachFiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_handlefiles  = files;
			$("#HattachFiles").val(JSON.stringify(files));
		}
		if(_newfile != ""){
			var files = JSON.parse(_newfile);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_Newhandlefiles = files;
			$("#newHattachFiles").val(JSON.stringify(files));
		}
	}else if(modalType == 'review'){
		var _file = $("#OldCodeFiles").val();
		var _newfile = $("#NewCodeFiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			OldCodeFiles = files;
			$("#OldCodeFiles").val(JSON.stringify(files));
		}
		if(_newfile != ""){
			var files = JSON.parse(_newfile);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			 NewCodeFiles= files;
			$("#NewCodeFiles").val(JSON.stringify(files));
		}
	}else if(modalType == 'NOreview'){
		var _file = $("#reviewFiles").val();
		var _newfile = $("#newreviewFiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_NoreviewFailed = files;
			$("#reviewFiles").val(JSON.stringify(files));
		}
		if(_newfile != ""){
			var files = JSON.parse(_newfile);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_newNoReviewFailed= files;
			$("#newreviewFiles").val(JSON.stringify(files));
		}
	}
}

//提交工作任务备注
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
			attachFiles :$("#checkfiles2").val()
			},
		success: function(data) {
			layer.alert("保存成功！！！",{icon:1});
			_checkfiles = [];
			$("#checkfiles2").val(''); 
			getSee(id);
		}
		
	});
}

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


function reset_opt(){
    $("#opt_defectRemark").val('');
    $("#opt_assignUserId").val('');
    $("#opt_assignUserName").val('');
    $("#opt_solution_defectId").val('');
    $("#opt_solution_submitUserId").val('');
    $("#opt_solution_oldAssignUserId").val('');

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

    dev_defect_reset();

    rejectDefectData = {};
    sendRow = {};
    __defectAgainData = {};
}
//清空
function dev_defect_reset(){
    $(".opt_DelayRepair").css("display","none");
}
function showDefect(defectId){
    $("#loading").css('display','block');
    reset_opt();
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
        url:devDefectUrl+"defect/getDefectRecentLogById",
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
                    $("#check_devTaskCode").html("<a class='a_style' onclick='getSee("+data.devTask.id+","+data.devTask.requirementFeatureId+")'>"+data.devTask.devTaskCode+"</a>");
                    $("#check_devTaskName").html("<a class='a_style' onclick='getSee("+data.devTask.id+","+data.devTask.requirementFeatureId+")'>"+data.devTask.devTaskName+"</a>");
                }

                if(data.feature != null && data.feature != "undefined"){
                    $("#check_reqFetureCode").html("<a class='a_style' onclick='showDevTask("+data.feature.id+")'>"+data.feature.featureCode+"</a>");
                    $("#check_reqFetureName").html("<a class='a_style' onclick='showDevTask("+data.feature.id+")'>"+data.feature.featureName+"</a>");
                }

                // 最新一次的日志信息
                if (data.data != null) {
                    var string = '';
                    var _span = '';
                    var logDetail = ( data.data.logDetail == null ? [] : JSON.parse(data.data.logDetail) );
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
                $("#checkDefectModel").modal("show");
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

function dicDefectSelect(rows){
    for (var i = 0,len = defectTypeList.length;i < len;i++) {
        if(rows.defectType == defectTypeList[i].value){
            $(rows.checkDefectType).html(defectTypeList[i].innerHTML);
            break;
        }
    }

    for (var i = 0,len = defectSourceList.length;i < len;i++) {
        if(rows.defectSource == defectSourceList[i].value){
            $(rows.checkDefectSource).html(defectSourceList[i].innerHTML);
            break;
        }
    }

    for (var i = 0,len = defectStatusList.length;i < len;i++) {
        if(rows.defectStatus == defectStatusList[i].value){
            $(rows.checkDefectStatus).html(defectStatusList[i].innerHTML);
            break;
        }
    }

    for (var i = 0,len = severityLevelList.length;i < len;i++) {
        if(rows.severityLevel == severityLevelList[i].value){
            var classColor = "classColor"+rows.severityLevel;
            var severityLevelData = "<span class='"+classColor+"'>"+severityLevelList[i].innerHTML+"</span>";
            $(rows.checkSeverityLevel).html(severityLevelData);
            break;
        }
    }

    for (var i = 0,len = emergencyLevelList.length;i < len;i++) {
        if(rows.emergencyLevel == emergencyLevelList[i].value){
            var classColor = "classColor"+rows.emergencyLevel;
            var emergencyLevellData = "<span class='"+classColor+"'>"+emergencyLevelList[i].innerHTML+"</span>";
            $(rows.checkEmergencyLevel).html(emergencyLevellData);
            break;
        }
    }
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

//下载文件
function downloadFile(row){
    var url = "/zuul"+devDefectUrl+"defect/downloadFile?fileS3Bucket="+row.fileS3Bucket+"&fileNameOld="+row.fileNameOld+"&fileS3Key="+row.fileS3Key;
    window.location.href = encodeURI(url);
}
//附件列表后台接口
function attList(id,FileTable){
    $("#loading").css('display','block');
    /*查询附件,显示附件列表*/
    $.ajax({
        url:devDefectUrl+"defect/findAttList",
        method:"post",
        data:{
            defectId:id
        },
        success:function(data){
//        	$("#dealLogDiv").empty();
//        	$("#dealLogDiv").css('display','block');
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
                	var rows = data.defectInfo;
                    if(submitDefectStatus == "checkDefect"){
                        $("#check_systemName").text(rows.systemName);
                        $("#check_defectCode").text(rows.defectCode);
                        $("#check_submitUserName").text(rows.submitUserName);
                        $("#check_assignUserName").text(rows.assignUserName != null?rows.assignUserName:"");
                        $("#check_defectOverview").html(rows.defectOverview);
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
                        
                    }else if(submitDefectStatus == "devCheckDefect"){
                        devCheckDefect(rows);
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
                    return '<div class="fileTb" style="cursor:pointer" >'+classType+'    <a download="'+1+'" onclick="downloadFile('+rows+')">'+row.fileNameOld+'</a></div>';
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
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            })
        },
        formatNoMatches: function(){
            return "";
        }
    });
}
// 提取文件图片显示
function classPath(fileType,classType){
    switch (fileType){
        case "doc":
        case "docx":
            classType = '<img src="../static/images/file/word.png" />';
            break;
        case "xls":
        case "xlsx":
            classType = '<img src="../static/images/file/excel.png" />';
            break;
        case "txt":
            classType = '<img src="../static/images/file/text.png" />';
            break;
        case "pdf":
            classType = '<img src="../static/images/file/pdf.png" />';
            break;
        case "JPG":
        case "jpg":
            classType ='<img src="../static/images/file/JPG.png" />';
            break;
        case "bmp":
        case "BMP":
            classType ='<img src="../static/images/file/BMP.png" />';
            break;
        case "PNG":
        case "png":
            classType ='<img src="../static/images/file/PNG.png" />';
            break;
        default:
            classType ='<img src="../static/images/file/text.png" />';
            break;
    }
    return classType;
}
function down2( This ){
    if( $(This).hasClass("fa-angle-double-down") ){
        if( _flag==1 ){
            $("#loading").css('display','block');
            $.ajax({
                url:devDefectUrl+"defect/getDefectLogById",
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
                                        if( data.data[i].logDetail.length>0 ){
                                            var logDetail=JSON.parse( data.data[i].logDetail );
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
                                        if( data.data[i].logAttachementList.length>0 ){
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
                                        if( logDetail.length>0 ){
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
                                        }else{
                                            _span = '<span>未经任何操作</span><br/>';
                                        }

                                        if( data.data[i].logAttachementList.length>0 ){
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
function logDetailList(fieldName,oldName,newName){
    var arr = [];
    switch (fieldName){
        case "缺陷状态":
            for (var k = 0;k < defectStatusList.length; k++) {
                if( defectStatusList[k].value!='' ){
                    if( oldName == defectStatusList[k].value ){
                        oldName=defectStatusList[k].innerHTML;
                    }
                    if( newName == defectStatusList[k].value ){
                        newName=defectStatusList[k].innerHTML;
                    }
                }
            }
            break;
        case "缺陷类型":
            for (var k = 0;k < defectTypeList.length; k++) {
                if( defectTypeList[k].value!='' ){
                    if( oldName == defectTypeList[k].value ){
                        oldName=defectTypeList[k].innerHTML;
                    }
                    if( newName == defectTypeList[k].value ){
                        newName=defectTypeList[k].innerHTML;
                    }
                }
            }
            break;
        case "缺陷来源":
            for (var k = 0;k < defectSourceList.length; k++) {
                if( defectSourceList[k].value!='' ){
                    if( oldName == defectSourceList[k].value ){
                        oldName=defectSourceList[k].innerHTML;
                    }
                    if( newName == defectSourceList[k].value ){
                        newName=defectSourceList[k].innerHTML;
                    }
                }
            }
            break;
        case "严重级别":
            for (var k = 0;k < severityLevelList.length; k++) {
                if( severityLevelList[k].value!='' ){
                    if( oldName == severityLevelList[k].value ){
                        oldName=severityLevelList[k].innerHTML;
                    }
                    if( newName == severityLevelList[k].value ){
                        newName=severityLevelList[k].innerHTML;
                    }
                }
            }
            break;
        case "紧急程度":
            for (var k = 0;k < emergencyLevelList.length; k++) {
                if( emergencyLevelList[k].value!='' ){
                    if( oldName == emergencyLevelList[k].value ){
                        oldName = emergencyLevelList[k].innerHTML;
                    }
                    if( newName == emergencyLevelList[k].value ){
                        newName = emergencyLevelList[k].innerHTML;
                    }
                }
            }
            break;
        case "驳回理由":
            for (var k = 0;k < rejectionList.length; k++) {
                if( rejectionList[k].value!='' ){
                    if( oldName == rejectionList[k].value ){
                        oldName=rejectionList[k].innerHTML;
                    }
                    if( newName == rejectionList[k].value ){
                        newName=rejectionList[k].innerHTML;
                    }
                }
            }
            break;
        case "解决情况状态":
            for (var k = 0;k < solveStatusList.length; k++) {
                if( solveStatusList[k].value!='' ){
                    if( oldName == solveStatusList[k].value ){
                        oldName=solveStatusList[k].innerHTML;
                    }
                    if( newName == solveStatusList[k].value ){
                        newName=solveStatusList[k].innerHTML;
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
//获取消息提醒
function  getRemindInfo(){
	$.ajax({
		method:"post", 
		url:"/system/message/getAllMessage",
		data:{
			messageJson:"",
			rows: 10,
			page: 1,
		},
		success : function(data) { 

			var sjrList=data.rows;
  			for(var i=0;i<sjrList.length;i++){
  				if(i==sjrList.length-1){
  					var str='<div class="logDiv lastLog">';
  				}else{
  					var str='<div class="logDiv">';
  				}
                var environmentType;
  				if(sjrList[i].createType==1){
                    environmentType=getEnvironmentType(sjrList[i].environmentType)
				}else{
                    environmentType=sjrList[i].jobName;
				}

  				str+='<div class="logDiv_title"><span class="orderNum"></span>'+
	                 '<div class="fontWeihgt">'+sjrList[i].messageTitle+'</div>'+ 
   					'</div><div class="logDiv_cont"><div class="logDiv_contBorder">'+sjrList[i].messageContent+'<br/>'+
   					'<div>'+sjrList[i].createDate+'</div><br/></div></div></div>';
  				$("#rightBlock3").append( str ); 
  			}
		}
	});
}

//查看审批
function showPlanChart({projectId,projectName,requestUserId}){
	$.ajax({
    url: '/projectManage/plan/getPlanApproveRequest',
    type: "post",
    async : false,
    dataType: "json",
    data: {
      "projectId": projectId,
      "approveRequestId": requestUserId,
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      if(data.status == 1){
				$("#project_id").val(projectId);
				$("#add_tit").text(projectName);

        $("#check_status").text('');
        $("#check_user").text('');
        $("#check_date").text('');
        $("#check_Workload").text('');
        $("#up_before_tbody").empty();
        $("#up_after_tbody").empty();
        $("#logs_body").empty();
        $("#remark").text('');

        $("#current_userId").val(data.userId1);
        $("#approve_user_id").val(data.approveRequest.id);
        $("#approveRequestId").val(data.id);
        $("#check_status").text(data.requestLog[data.requestLog.length - 1].logType);
        $("#check_user").text(data.approveRequest.userName);
        $("#check_date").text(new Date(data.approveRequest.commitDate).toISOString().split('T')[0]);
        $("#check_Workload").text(data.approveRequest.requestReason);
        data.projectPlan.length && data.projectPlan.map((val,idx)=>{
          $('#up_before_tbody').append(`
            <tr class="rowdiv">
              <td>${idx + 1}</td>
              <td>${val.planCode}</td>
              <td>${val.planName}</td>
              <td>${val.planStartDate}~<br>${val.planEndDate}</td>
              <td>${val.responsibleUser}</td>
              <td>${val.deliverables}</td>
            </tr>
          `);
        })
        data.requestDetail.length && data.requestDetail.map((val,idx)=>{
        $('#up_after_tbody').append(`
          <tr class="rowdiv">
            <td class="">${idx + 1}</td>
            <td class="">${val.planCode || ''}</td>
            <td class="">${val.planName || ''}</td>
            <td class="">${val.planStartDate || ''}~<br>${val.planEndDate || ''}</td>
            <td class="">${val.responsibleUser || ''}</td>
            <td class="">${val.deliverables || ''}</td>
          </tr>
        `);
      })
        data.requestLog.map(val=>{
          $('#logs_body').append(`
            <div class="logDiv">
              <div class="logDiv_title">
                <span class="orderNum"></span>
                <span class="w_150_block">${val.logType}</span>&nbsp;&nbsp;&nbsp;
                <span>${val.userName}  | ${val.userAccount}</span>&nbsp;&nbsp;&nbsp;<br>
                <span style="padding-left: 21px;">${val.createDate}</span>
              </div>
              <div class="logDiv_cont">
                <div class="logDiv_contBorder">
                  <div class="">
                    <span>${val.logDetail || ''}</span>
                  </div>
                </div>
              </div>
            </div>
          `);
        })
        $('#remark').removeAttr('disabled');
        if(data.succ == 1){ //下一级
          $('.approve_btn1').show();
        }else if(data.succ == 2){ //办结
          $('.approve_btn2').show();
          $('.approve_btn1').eq(1).show();
        }else{
        	$('#remark').attr('disabled',true);
        }
        $('#approve_Modal').modal('show');
      }else{
        layer.alert(data.errorMessage,{icon:2});
      }
      $("#loading").css('display', 'none');
    }
  })
}

//审批提交
function approve_flow(status){
//  if(status == 3 && !$("#remark").val()){
//    layer.alert('请填写审批意见!',{icon:2});
//    return;
//  }
  $.ajax({
    url: '/projectManage/plan/approve',
    type: "post",
    async : false,
    dataType: "json",
    data: {
      "projectId": $("#project_id").val(),
      "operate": status,
      "projectPlanApproveRequestId": $("#approve_user_id").val(),
      "approveSuggest": $("#remark").val(),
      "userId": $("#current_userId").val(),
      "id":$("#approveRequestId").val() ,
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      if(data.status == 1){
				layer.alert('审批提交成功!',{icon:1});
				$("#details").setWorkDesk();//调用下面js中的方法     
    		getRemindInfo();
        $('#approve_Modal').modal('hide');
      }else{
        layer.alert(data.errorMessage,{icon:2});
      }
      $("#loading").css('display', 'none');
    }
  })
}
//查看
function showtestTask(id) { 
	layer.open({
	     type: 2,
	     title: false,
	     closeBtn: 0,
	     shadeClose: true,
	     shade: false, 
	     move: false,
	     area: ['100%', '100%'],  
	     tipsMore: true,
	     anim: 2,
	     content:'/testManageui/testtask/toTestTaskInfo?id='+ id,
	     btn: ['关闭'] ,
	     btnAlign: 'c', //按钮居中 
	     no:function(){
		    layer.closeAll(); 
		 } 
	}); 
	return ;  
}
function showTestWorkTask(id){
	layer.open({
	     type: 2,
	     title: false,
	     closeBtn: 0,
	     shadeClose: true,
	     shade: false, 
	     move: false,
	     area: ['100%', '100%'],  
	     tipsMore: true,
	     anim: 2,
	     content:'/testManageui/workTask/toWorkTaskCheck?id='+ id,
	     btn: ['关闭'] ,
	     btnAlign: 'c', //按钮居中 
	     no:function(){
		    layer.closeAll(); 
		 } 
	}); 
	return ;  
}

function checkDefect(defectId) {
    $("#loading").css('display', 'block');
    layer.open({
        type: 2,
        title: false,
        closeBtn: 0,
        shadeClose: true,
        shade: false,
        move: false,
        area: ['100%', '100%'],
        tipsMore: true,
        anim: 2,
        content: "/testManageui/defect/toCheckDefect?" + "defectId=" + defectId,
        success: function (layero, index) {
            $("#loading").css("display", "none");
        }
    });
    /*window.location.href =   "/testManageui/defect/toCheckDefect?" + "defectId=" + defectId ;*/
}