
var parameterArr={}; //存储跳转url中带的参数
var reqSourceList = '';
var reqTypeList = '';
var reqPriorityList = '';
var impReqStatusList = '';
var reqStatusList = '';
var reqPlanList = '';
var impReqTypeList = '';
var impReqDelayList = '';
var hangupStatusList = '';
var reqPropertyList = '';
var reqClassifyList = '';
var reqSubdivisionList = '';
var dataMigrationList = '';
var featureStatusList = '';
var devTaskStatus = '';
$(function(){
	parameterArr.arr = window.location.href.split( "?" ); 
    parameterArr.parameterArr = parameterArr.arr[1].split( "," );
    parameterArr.obj={};
    for( var i = 0 ; i < parameterArr.parameterArr.length ; i++ ){
    	var obj = parameterArr.parameterArr[i].split( "=" );  
    	parameterArr.obj[ obj[0] ] = obj[1];
    }  
    reqStatusList = $("#reqStatus1").find("option");
    featureStatusList=$("#SCfeatureStatus").find("option");
    reqSourceList = $("#reqSource").find("option");
    reqTypeList = $("#reqType").find("option");
    reqPriorityList=$("#reqPriority").find("option");
    reqPlanList=$("#reqPlan").find("option");
    impReqStatusList=$("#impReqStatus").find("option");
    impReqTypeList=$("#impReqType").find("option");
    impReqDelayList=$("#impReqDelay").find("option"); 
    hangupStatusList=$("#hangupStatus").find("option");	
    reqPropertyList=$("#reqProperty").find("option");
    reqClassifyList=$("#reqClassify").find("option");
    reqSubdivisionList=$("#reqSubdivision").find("option");
    dataMigrationList=$("#dataMigration").find("option");
    featureStatusList=$("#SCfeatureStatus").find("option");
    devTaskStatus = $("#devTaskStatus").find("option");
    impReqTypeList=$("#impReqType").find("option");	   
    details( parameterArr.obj.requirementId ,parameterArr.obj.requirementParentId); 
});

//需求详情，rid:需求ID，parentId:父需求ID
function details(rid,parentId){	
	if(parentId=="undefined"){
		parentId=undefined;
	}
	$("#loading").css( "display","block" );
	$("#checkSysModal").empty();
	cleanDetails(); 
	$.ajax({		
		type:"POST",
        url:"/projectManage/requirement/findRequirementById",
        dataType:"json",
        data:{
        	rIds:rid,
        	parentIds:parentId
    	},
  		success:function(data){    
  			//关注状态
  			$( "#details .focusInfo" ).attr("id",rid);
  			if( data.attentionStatus == 2 ){
  				//不关注
  				$( "#details .focusInfo" ).addClass("noheart");
  				$( "#details .focusInfo" ).attr("title","点击关注"); 
  			}else if( data.attentionStatus == 1 ){
  				//关注
  				$( "#details .focusInfo" ).removeClass("noheart");
  				$( "#details .focusInfo" ).attr("title","点击不再关注"); 
  			} 
  			
  			$("#itcdReqId").val();
  			$("#itcdReqId").val(data['data'].itcdRequirementId); 			
  			$("#reqCodeDetails").text( data["data"].requirementCode +" | " + data["data"].requirementName);
  			//需求模块
  			addSysModal( data.data.list )
  			//自定义扩展字段
  			showField( data.fields )
  			//基本信息		
  			if(data["data"].requirementSource!=null){
	  			for (var i = 0,len = reqSourceList.length;i < len;i++) {
	                if(data["data"].requirementSource.toLowerCase() == reqSourceList[i].value.toLowerCase()){               	
	                	$("#reqSourceNameDetails").html(reqSourceList[i].innerHTML);  
	                }
	            }
  			}
  			if(data["data"].requirementType!=null){
	  			for (var i = 0,len = reqTypeList.length;i < len;i++) {
	                if(data["data"].requirementType.toLowerCase() == reqTypeList[i].value.toLowerCase()){               	
	                	$("#reqTypeNameDetails").html(reqTypeList[i].innerHTML);  
	                }
	            }
  			}
  			if(data["data"].requirementPriority!=null){
	  			for (var i = 0,len = reqPriorityList.length;i < len;i++) {
	                if(data["data"].requirementPriority.toLowerCase() == reqPriorityList[i].value.toLowerCase()){               	
	                	$("#reqPriorityNameDetails").html(reqPriorityList[i].innerHTML);  
	                }
	            }
  			}
  			if(data["data"].requirementStatus!=null){
	  			for (var i = 0,len = reqStatusList.length;i < len;i++) {
	                if(data["data"].requirementStatus.toLowerCase() == reqStatusList[i].value.toLowerCase()){               	
	                	$("#reqStatusNameDetails").html(reqStatusList[i].innerHTML);  
	                }
	            }
  			}
  			if(data["data"].requirementPlan!=null){
  				for (var i = 0,len = reqPlanList.length;i < len;i++) {  				
  	                if(data["data"].requirementPlan.toLowerCase() == reqPlanList[i].value.toLowerCase()){               	
  	                	$("#reqPlanNameDetails").html(reqPlanList[i].innerHTML);  
  	                }
  	            }
  			} 					
  			$("#expectOnlineDateDetails").html(timestampToTime(data["data"].expectOnlineDate));			
  			$("#planOnlineDateDetails").html(timestampToTime(data["data"].planOnlineDate));
  			$("#actualOnlineDateDetails").html(timestampToTime(data["data"].actualOnlineDate));
  			$("#userNameDetails").html(data["data"].userName);			
  			$("#deptNameDetails").html(data["data"].deptName);
  			$("#devDeptNameDetails").html(data["data"].devDeptName);
  			var triFather= data["triFather"];
  			var triSon=data["triSon"];
  			if(triFather==undefined){
  				$("#relevancyDetails").html(triSon);  //关联需求		
  			}else{
  				$("#relevancyDetails").html(triFather.requirementCode+" | "+triFather.requirementName+", "+triSon);  //关联需求	
  			}  			
  			$("#createDateDetails").html(timestampToTime(data["data"].createDate));			
  			$("#lastUpdateDateDetails").html(timestampToTime(data["data"].lastUpdateDate));
  			$("#openDateDetails").html(timestampToTime(data["data"].openDate));
  			$("#requirementOverviewDetails").html(data["data"].requirementOverview);
  			$("#checkReqs").html('');
  			var requirementNames = data["data"].requirementNames;
  			if(requirementNames != null){
  				$("#checkReqs").html(requirementNames.join(",")); 
  			}
  			$("#checkCloseTime").html(data["data"].closeTime);
  			$("#relevancyDetails").html(data["data"].parentCode);
  			//重点需求
  			$("#impReqStatusNameDetails").html(data["data"].impReqStatusName);	
  			for (var i = 0,len = impReqStatusList.length;i < len;i++) {
                if(data["data"].importantRequirementStatus == impReqStatusList[i].value){
                	$("#impReqStatusNameDetails").html(impReqStatusList[i].innerHTML);  
                }
            }
  			for (var i = 0,len = impReqTypeList.length;i < len;i++) {
                if(data["data"].importantRequirementType == impReqTypeList[i].value){
                	$("#impReqTypeDetails").html(impReqTypeList[i].innerHTML);  
                }
            }  	
  			for (var i = 0,len = impReqDelayList.length;i < len;i++) {
                if(data["data"].importantRequirementDelayStatus == impReqDelayList[i].value){
                	$("#impReqDelayStatusNameDetails").html(impReqDelayList[i].innerHTML);  
                }
            }		
  			$("#impReqOnlineQuarterDetails").html(data["data"].importantRequirementOnlineQuarter+" 季度");
  			$("#impReqDelayReasonDetails").html(data["data"].importantRequirementDelayReason);			
  			//成本与收益
  			$("#directIncomeDetails").html(data["data"].directIncome); 
  			$("#forwardIncomeDetails").html(data["data"].forwardIncome);
  			$("#recessiveIncomeDetails").html(data["data"].recessiveIncome);
  			$("#directCostReductionDetails").html(data["data"].directCostReduction);
  			$("#forwardCostReductionDetails").html(data["data"].forwardCostReduction);  			
  			$("#anticipatedIncomeDetails").html(data["data"].anticipatedIncome);
  			$("#estimateCostDetails").html(data["data"].estimateCost);
  			//其他信息 		
  			for (var i = 0,len = hangupStatusList.length;i < len;i++) {
                if(data["data"].hangupStatus == hangupStatusList[i].value){
                	$("#hangupStatusNameDetails").html(hangupStatusList[i].innerHTML);  
                }
            }
  			$("#hangupDateDetails").html(timestampToTime(data["data"].hangupDate));
  			$("#changeCountDetails").html(data["data"].changeCount);  			
  			$("#devManageUserNameDetails").html(data["data"].devManageUserName); 
  			$("#reqManageUserNameDetails").html(data["data"].reqManageUserName);
  			$("#reqAcceptanceUserNameDetails").html(data["data"].reqAcceptanceUserName); 	
  			for (var i = 0,len = reqPropertyList.length;i < len;i++) {
                if(data["data"].requirementProperty == reqPropertyList[i].value){               	
                	$("#requirementPropertyDetails").html(reqPropertyList[i].innerHTML);
                }
            }  			  				
  			for (var i = 0,len = reqClassifyList.length;i < len;i++) {
                if(data["data"].requirementClassify == reqClassifyList[i].value){               	
                	$("#requirementClassifyDetails").html(reqClassifyList[i].innerHTML);  
                }
            } 			
  			for (var i = 0,len = reqSubdivisionList.length;i < len;i++) {
                if(data["data"].requirementSubdivision == reqSubdivisionList[i].value){               	
                	$("#requirementSubdivisionDetails").html(reqSubdivisionList[i].innerHTML);  
                }
            }		
  			$("#planIntegrationTestDateDetails").html(timestampToTime(data["data"].planIntegrationTestDate));
  			$("#actualIntegrationTestDateDetails").html(timestampToTime(data["data"].actualIntegrationTestDate));
  			$("#acceptanceDescriptionDetails").html(data["data"].acceptanceDescription);
  			if(data["data"].acceptanceTimeliness=="1"){
  				$("#acceptanceTimelinessDetails").html("及时");
  			}else{
  				$("#acceptanceTimelinessDetails").html("非及时");
  			} 			
  			for (var i = 0,len = dataMigrationList.length;i < len;i++) {
                if(data["data"].dataMigrationStatus == dataMigrationList[i].value){               	
                	$("#dataMigrationStatusNameDetails").html(dataMigrationList[i].innerHTML);  
                }
            }
  			$("#workloadDetails").html(data["data"].workload);  			
  			$("#connectDiv").empty();
			var code = data.featureCode == undefined ? "": data.featureCode +" | ";
			var trf= data["trf"];
			console.log( data["trf"] );
			if(trf!=undefined){
				for(var i=0;i<trf.length;i++){
					var noBorder="";
					var featureStatusObj = getStatusName( trf[i].requirementFeatureStatus ); 
					/*for (var j = 0,len = featureStatusList.length;j < len;j++) {
	        			if(trf[i].requirementFeatureStatus == featureStatusList[j].value){
	        				featureStatusName = featureStatusList[j].innerHTML;
	        				
	                    }
	                }*/
					/*var rowdiv ="rowdiv"+i;
					$("#connectDiv").append('<div id='+rowdiv+'>');	*/														
					/*$("#"+rowdiv).append( 
						'<div class="rowdiv"><div class="form-group col-md-2"><label class=" control-label font_left">'+trf[i].featureCode+'</label></div>'+
						'<div class="form-group col-md-2"><label class=" control-label font_left">'+trf[i].featureName+'</label></div>'+
						'<div class="form-group col-md-2"><label class=" control-label font_left">'+delUndefined(trf[i].systemName)+'</label></div>'+
						'<div class="form-group col-md-2"><label class=" control-label font_left">'+delUndefined(trf[i].userName)+'</label></div>'+
						'<div class="form-group col-md-2"><label class=" control-label font_left">'+delUndefined(featureStatusName)+'</label></div>'+
						'<div class="form-group col-md-2"><label class=" control-label font_left">'+delUndefined(trf[i].windowName)+'</label></div></div>');
					*/
					if( i == 0 ){
						noBorder = "noBorder"
					}
					var str='<div class="oneTask '+noBorder+'">'+
					    '<div class="name">'+
					        '<span>'+trf[i].featureCode+'</span>'+
					        '<span class="status status'+featureStatusObj.num+'">'+featureStatusObj.name+'</span>'+
					    '</div>'+
					    '<div class="describe">'+trf[i].featureName+'</div>'+
					    '<div class="rowdiv">'+
					        '<div class="def_col_22 large">'+
					            '<span class="fa fa-th-large"></span> '+
					            '<span> 关联系统:'+delUndefined(trf[i].systemName)+'</span>'+
					        '</div>'+
					        '<div class="def_col_14 user"> '+
					            '<span class="fa fa-user"></span>'+
					            '<span> 执行人:'+delUndefined(trf[i].userName)+'</span>'+
					        '</div>'+
					    '</div>'+
					'</div>';
					$("#connectDiv").append( str ); 
					var workTask = findWorkTask(trf[i].id);
					for(var j=0;j<workTask.length;j++){
						var workStr='';
						var noBorder=''; 
						var workTaskObj =  getWorkTaskStatus( workTask[j].devtaskStatus ); 
						/*if( j == workTask.length-1 ){
							noBorder = "noBorder";
						}*/
						var workStr='<div class="oneWorkTask '+noBorder+'">'+
						    '<div class="name">'+
							    '<div class="rowdiv">'+
							        '<div class="def_col_24 large">'+
							            '<span class="fa fa-check-square"></span> '+
							            '<span title="'+workTask[j].devTaskCode+'"> '+workTask[j].devTaskCode+'</span>'+
							        '</div>'+
							        '<div class="def_col_12 user"> '+
							            '<span class="fa fa-user"></span>'+
							            '<span title="'+delUndefined(workTask[j].devUsername)+'"> '+delUndefined(workTask[j].devUsername)+'</span>'+
							        '</div>'+
							    '</div>'+   
						        '<span class="status status0'+workTaskObj.num+'">'+workTaskObj.name+'</span>'+
						    '</div>'+
						    '<div class="describe">'+workTask[j].devTaskName+'</div>'+ 
						'</div>';
						
						/*$("#"+rowdiv).append(
							'<div class="rowdiv"><div class="form-group col-md-1"><img class=" control-label font_left" src="/projectManageui/static/images/req.png" style="padding-left:15px;"/></div>'+
							'<div class="form-group col-md-2"><label class=" control-label font_left">'+workTask[j].devTaskCode+'</label></div>'+
							'<div class="form-group col-md-3"><label class=" control-label font_left">'+workTask[j].devTaskName+'</label></div>'+
							'<div class="form-group col-md-2"><label class=" control-label font_left">'+delUndefined(workTask[j].devUsername)+'</label></div>'+
							'<div class="form-group col-md-4"><label class=" control-label font_left">'+status+'</label></div></div>');				
						*/
						$("#connectDiv").append( workStr );
					}
					 
				}
			}
			attList(data.attachements,"#checkFileTable");
			
			
			layer.open({ 
			  type: 1,
			  title: false,
			  closeBtn: 0,
		      shadeClose: true,
		      shade: false, 
		      move: false,
		      area: ['100%', '100%'], 
		      id: "1",
		      tipsMore: true,
		      anim: 2,
		      content:  $('#details'),
		      success: function(layero, index){
		    	  $("#loading").css( "display","none" );
	    	  }
		    }); 
        },
  		error:function(){ 
  		}        
    }); 
}


function cleanDetails(){		
	$("#reqCodeDetails").html('');
	$("#reqNameDetails").html('');
	//基本信息
	$("#reqSourceNameDetails").html('');
	$("#reqTypeNameDetails").html('');
	$("#reqPriorityNameDetails").html('');
	$("#reqStatusNameDetails").html('');
	$("#reqPlanNameDetails").html('');	
	$("#expectOnlineDateDetails").html('');		
	$("#planOnlineDateDetails").html('');
	$("#actualOnlineDateDetails").html('');
	$("#userNameDetails").html('');	
	$("#deptNameDetails").html('');
	$("#devDeptNameDetails").html('');		
	$("#relevancyDetails").html(''); //关联需求				
				
	$("#createDateDetails").html('');
	$("#lastUpdateDateDetails").html('');
	$("#openDateDetails").html('');
	$("#requirementOverviewDetails").html('');	  			
	//重点需求
	$("#impReqStatusNameDetails").html('');
	$("#impReqTypeDetails").html('');
	$("#impReqDelayStatusNameDetails").html('');
	$("#impReqOnlineQuarterDetails").html('');
	$("#impReqDelayReasonDetails").html('');
	//成本与收益
	$("#directIncomeDetails").html('');
	$("#forwardIncomeDetails").html('');
	$("#recessiveIncomeDetails").html('');
	$("#directCostReductionDetails").html('');
	$("#forwardCostReductionDetails").html('');		
	$("#anticipatedIncomeDetails").html('');
	$("#estimateCostDetails").html('');
	//其他信息 			
	$("#hangupStatusNameDetails").html('');
	$("#hangupDateDetails").html('');
	$("#changeCountDetails").html('');
	$("#devManageUserNameDetails").html('');
	$("#reqManageUserNameDetails").html('');
	$("#reqAcceptanceUserNameDetails").html('');		
	$("#requirementPropertyDetails").html('');
	$("#requirementClassifyDetails").html('');
	$("#requirementSubdivisionDetails").html('');		
	$("#planIntegrationTestDateDetails").html('');
	$("#actualIntegrationTestDateDetails").html('');
	$("#acceptanceDescriptionDetails").html('');
	$("#acceptanceTimelinessDetails").html('');
	$("#dataMigrationStatusNameDetails").html('');
	$("#workloadDetails").html('');
}

//需求关联系统信息列表
function addSysModal( data ){ 
	if( data == null ){
		return ;
	}
	for( var i=0;i<data.length;i++ ){
		var str = '<div class="rowdiv"><div class="form-group col-md-4">'+
				  '<label class="control-label fontWeihgt col-md-3">系统:</label>'+
				  '<label class="control-label font_left col-md-9"><span>'+isValueNull( data[i].systemName )+'</span></label></div>'+
				  '<div class="form-group col-md-4">'+
				  '<label class="control-label fontWeihgt col-md-3">模块:</label>'+
				  '<label class="control-label font_left col-md-9"><span>'+isValueNull( data[i].assetSystemName )+'</span></label></div>'+
				  '<div class="form-group col-md-4">'+
				  '<label class="control-label fontWeihgt col-md-3">功能点数:</label>'+
				  '<label class="control-label font_left col-md-9"><span>'+isValueNull( data[i].functionCount )+'</span></label></div></div>';
		$( "#checkSysModal" ).append( str );
	}
}

function showField( data ){ 
	$( "#checkEditField" ).empty();
	if(data != null && data != "undefined" && data.length > 0 ){
        for( var i=0;i<data.length;i++ ){
            var aLabel='<div class="form-group col-md-4">  <label class="control-label fontWeihgt">'+ data[i].label +'：</label>' +
            '<label class="control-label font_left">'+ data[i].valueName +'</label></div>';
            $( "#checkEditField" ).append( aLabel);
        }
    }
}
function delUndefined(str){
	if(str==undefined){
		str="";
	}
	return str;
}
function getWorkTaskStatus( status ){
	var obj={};
	obj.name = "无状态";
	obj.num = 0;
	for (var j = 0,len = devTaskStatus.length;j < len;j++) {  
		if(status == devTaskStatus[j].value){ 
			obj.name = devTaskStatus[j].innerHTML;
			obj.num = devTaskStatus[j].value;
        }
    }
	return obj;
}
function findWorkTask(id){	
	var workTask;
	$.ajax({
        type:"POST",
        url:"/devManage/devtask/getOneDevTask",
        dataType:"json",
        data:{
        	"id":id	        	
    	},
    	async:false,
        success:function(data1){
        	workTask =data1["list"];  	
        }    	
    });
	return workTask;
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

                if (idName == "#checkFileTable"){
                    return '<div class="fileTb" style="cursor:pointer;">'+classType
                    +'    <a style="color:#454545 !important;" download="'+1+'" onclick="downloadFile('+rows+')">'+row.fileNameOld+'</a></div>';
                } else if (idName == "#editFileTable"){
                    return '<div class="fileTb">'+classType+'    '+row.fileNameOld+'</div>';
                }
            }
        },{
            field : "opt",
            title : "操作",
            align : 'center',
            formatter : function(value, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                if (idName == "#checkFileTable"){
                    return '';
                } else if (idName == "#editFileTable"){
                    if(row.id == null){
                        var name =  JSON.stringify({fileName:row.fileNameOld}).replace(/"/g, '&quot;');                     
                        return '<div class="span_a_style"><span class="a_style" onclick="removeFile('+name+',this)">删除</span></div>';
                    } else {                    	
                    	var name =  JSON.stringify({fileName:row.fileNameOld}).replace(/"/g, '&quot;');                     
                        return '<div class="span_a_style"><span class="a_style" onclick="removeFile1('+name+','+row.id+',this)">删除</span></div>';
                        /*return '<div class="span_a_style"><span class="a_style" onclick="deleteAtt(\'' + rows +'\',this,\'' + index +'\')">删除</span></div>';*/
                    }
                }

            }
        }],
        onLoadSuccess:function(){
            $("#loading").css('display','none');
        },
        onLoadError : function() {

        }
    });
}

//查看页面查看附件
function showFile(){
	var requirementId =  $("#itcdReqId").val();
	if(requirementId== ''){
		layer.alert('该需求下没有附件！', {icon: 0});
		return;
	}
	layer.open({
	  type: 2,
	  area: ['700px', '450px'],
	  fixed: false, //不固定
	  maxmin: true,
	  btnAlign: 'c',
	  title:"相关附件",
	  content: reqAttUrl+"?reqId="+requirementId+"&reqtaskId="+requirementId+"&taskreqFlag=requirement"
	});

}
//附件列表后台接口
function attList(att,FileTable){     
    attTable(att,FileTable);
    if (att != null && att.length > 0){
        for( var i = 0,len = att.length;i < len ; i++){
            editAttList.push(att[i].fileNameOld);
        }
    }     
}
function getStatusName( status ){
	var obj={};
	for (var j = 0,len = featureStatusList.length;j < len;j++) { 
		if(status == featureStatusList[j].value){
			obj.name = featureStatusList[j].innerHTML;
			obj.num = featureStatusList[j].value;
        }
    }
	return obj;
}
function timestampToTime(timestamp) {
	var Y,M,D;
	if(timestamp!=null){
	    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
	    Y = date.getFullYear() + '年';
	    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '月';
	    D = date.getDate() + '日';
	    //var h = date.getHours() + ':';
	    //var m = date.getMinutes() + ':';
	    //var s = date.getSeconds();	    
	}
	return Y+M+D;
}