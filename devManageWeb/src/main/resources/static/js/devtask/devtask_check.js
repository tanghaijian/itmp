/**
 * Created by ztt
 */
var _icon_word ="../static/images/devtask/word.png";
var _icon_excel ="../static/images/devtask/excel.png";
var _icon_text = "../static/images/devtask/text.png";
var _icon_pdf = "../static/images/devtask/pdf.png";
var _icon_img = "../static/images/devtask/img.png";
var _icon_other = "../static/images/devtask/other.png";
var _files = [];
var _editfiles = [];
var _checkfiles = [];
var modalType = '';
var workStatus ='';
var windowId = '';
var ids = [];
var devtaskStatusList ='';//状态
var excuteUserName='';
var deployStatusData =[];//部署状态
var reqFeaturePriorityList = [];//优先级

var editDevtaskStatus = '';
var reqPriorityList = '';
var impReqStatusList='';
var parameterArr={};
var requirementParentId='';
$(function(){
	 banEnterSearch();
	 uploadFileList();
	getDeployStatus();
   // devtaskStatusList = $("#devTaskStatus").find("option");
    devtaskStatusList = getReqFeatureStatus();
    reqFeaturePriorityList = $("#reqFeaturePriority").find("option");
    $("#devManPost").val(uid);
    $("#devManPostName").val(username); 
    
    if(devtaskStatusList!=null){
    	$.each(devtaskStatusList,function(index,value){
    		$("#devTaskStatus").append('<option value="'+value.valueCode+'">'+value.valueName+'</option>')
    	})
    }
    findByStatus(); 
    //时间控件 配置参数信息
    
    reqPriorityList=$("#reqPriority").find("option");
    impReqStatusList=$("#impReqStatus").find("option");
    $("#startWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
    	  $(this).parent().children(".btn_clear").css("display","block");
       });
    $("#endWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
  	  $(this).parent().children(".btn_clear").css("display","block");
    });
    dataControl("startWork","endWork");
    $("#handlestartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('hide',function(e) {
	                $('#handleForm').data('bootstrapValidator')
	                    .updateStatus('startWork', 'NOT_VALIDATED',null)
	                    .validateField('startWork');
	            }).on('change',function(){
		  $(this).parent().children(".btn_clear").css("display","block");
       });
    $("#handleendWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('hide',function(e) {
    	                $('#handleForm').data('bootstrapValidator')
    	                    .updateStatus('endWork', 'NOT_VALIDATED',null)
    	                    .validateField('endWork');
    	            }).on('change',function(){
      	  $(this).parent().children(".btn_clear").css("display","block");
        });
    dataControl("handlestartWork","handleendWork");

   $("#eastartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
  	  $(this).parent().children(".btn_clear").css("display","block");
    });
    $("#eaendWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
  	  $(this).parent().children(".btn_clear").css("display","block");
    });
    dataControl("eastartWork","eaendWork");

    $("#epstartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
  	  $(this).parent().children(".btn_clear").css("display","block");
    });
    
    $("#ependWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
  	  $(this).parent().children(".btn_clear").css("display","block");
    });
    
    dataControl("epstartWork","ependWork");
    $("#sWorkStart").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left",
    }).on('hide',function(e) {
    	                $('#splitForm').data('bootstrapValidator')
    	                    .updateStatus('startWork', 'NOT_VALIDATED',null)
    	                    .validateField('startWork');
    	            }).on('change',function(){
      	  $(this).parent().children(".btn_clear").css("display","block");
        });
    	
    $("#sWorkEndStart").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('hide',function(e) {
    	                $('#splitForm').data('bootstrapValidator')
    	                    .updateStatus('endWork', 'NOT_VALIDATED',null)
    	                    .validateField('endWork');
    	            }).on('change',function(){
      	  $(this).parent().children(".btn_clear").css("display","block");
        });
    dataControl("sWorkStart","sWorkEndStart");
     
    //新增开发任务来源change事件
    $("#newrequirementFeatiureSource").change(function(){ 
    	if($("#newrequirementFeatiureSource").val()==1){
    		$("#questDiv").hide();
    		$("#reqDiv").show();
    		$("#dftDiv").hide();
    	}else if($("#newrequirementFeatiureSource").val()==2){
    		$("#reqDiv").hide();
    		$("#questDiv").show();
    		$("#dftDiv").hide();
    	}else if($("#newrequirementFeatiureSource").val()==3){
    		$("#reqDiv").hide();
    		$("#questDiv").hide();
    		$("#dftDiv").show();
    	}else if($("#newrequirementFeatiureSource").val()==''){
    		$("#reqDiv").hide();
    		$("#questDiv").hide();
    		$("#dftDiv").hide();
    	}    	
    });
    
    //编辑开发任务来源change事件
    $("#editrequirementFeatureSource").change(function(){ 
    	if($("#editrequirementFeatureSource").val()==1){
    		$("#equestDiv").hide();
    		$("#ereqDiv").show();
    		$("#edftDiv").hide();
    	}else if($("#editrequirementFeatureSource").val()==2){
    		$("#ereqDiv").hide();
    		$("#equestDiv").show();
    		$("#edftDiv").hide();
    	}else if($("#editrequirementFeatureSource").val()==3){
    		$("#ereqDiv").hide();
    		$("#equestDiv").hide();
    		$("#edftDiv").show();
    	}else if($("#editrequirementFeatureSource").val()==''){
    		$("#ereqDiv").hide();
    		$("#equestDiv").hide();
    		$("#edftDiv").hide();
    	}    	
    });
    
    //关联子系统change事件
    $("#newsystemName").change(function(){
    	var systemId = $("#newsystemId").val();
    	if(systemId!=null&& systemId!=''){
	    	$.ajax({
	    		url:"/devManage/systemVersion/getSystemVersionByCon",
	    		dataType:"json",
	    		type:"post",
	    		data:{
	    			systemId:systemId
	    		},
	    		success:function(data){
	    			$("#newsystemVersionBranch").empty();
	    			$("#newsystemVersionBranch").append("<option value=''>请选择</option>");
	    			
	    			var systemVersionStr='';
	  				$.each(data.rows,function(index,value){
							systemVersionStr += '<option value="'+value.id+','+value.groupFlag+'">'+value.version+'--> '+toStr(value.groupFlag)+'</option>'
						});
	  				$("#newsystemVersionBranch").append(systemVersionStr);
	    			
	    			/*if(data.systemVersionBranchs.length>0){
	    				for(var i=0; i<data.systemVersionBranchs.length;i++){
	    					if(data.systemVersionBranchs[i].scmBranch!=null&&data.systemVersionBranchs[i].systemVersionId!=undefined ){
		    					$("#newsystemVersionBranch").append('<option value="'+data.systemVersionBranchs[i].systemVersionId+','+data.systemVersionBranchs[i].scmBranch+'">'+data.systemVersionBranchs[i].systemVersionName+'-->'+data.systemVersionBranchs[i].scmBranch+'</option>');
		    				}
	    				}
	    			}*/
	    			$('.selectpicker').selectpicker('refresh'); 
	    			
	    		if($("#ndevelopmentMode").val() == 1){//关联的系统是敏态 可以选择选择冲刺
	   				 $.ajax({
	   					 url:"/devManage/devtask/getSprintBySystemId",
	   					 data:{
	   						 systemId:$("#newsystemId").val()
	   					 },
	   					 type:"post",
	   					 dataType:"json",
	   					 success:function(data){
	   						 $("#newSprintId").empty();
	   						 $("#newSprintId").append("<option value=''>请选择</option>");
	   						 if(data.sprintInfos.length>0){
	   							 $("#newsprintDiv").show();
	   							 for(var i = 0;i<data.sprintInfos.length;i++){
	   								 $("#newSprintId").append('<option value="'+data.sprintInfos[i].id+'">'+data.sprintInfos[i].sprintName+'</option>')
	   							 }
	   						 }
	   		        		$('.selectpicker').selectpicker('refresh'); 
	   					 }
	   				 })
	   				//故事点
	   				$("#newstoryPointDiv").show();
	   				$("#editdevTaskStatus").empty();
	   				$("#editdevTaskStatus").append("<option value=''>请选择</option>");
	   			}else{
	   				 $("#newsprintDiv").hide();
	   				 $("#newSprintId").empty();
	   				$("#newstoryPointDiv").hide();
	   				
	   			}
	    			
	    			
	    		}
	    	})
    	}
    });
    
    $("#editsystemName").change(function(){
    	var systemId = $("#editsystemId").val();
    	if(systemId!=null && systemId!=''){
    		$.ajax({
        		url:"/devManage/systemVersion/getSystemVersionByCon",
        		dataType:"json",
        		type:"post",
        		data:{
        			systemId:systemId
        		},
        		success:function(data){
        			$("#editsystemVersionBranch").empty();
        			$("#editsystemVersionBranch").append("<option value=''>请选择</option>");
        			var systemVersionStr='';
	  				$.each(data.rows,function(index,value){
							systemVersionStr += '<option value="'+value.id+','+value.groupFlag+'">'+value.version+'--> '+value.groupFlag+'</option>'
						});
	  				$("#editsystemVersionBranch").append(systemVersionStr);
        			/*if(data.systemVersionBranchs.length>0){
        				for(var i=0; i<data.systemVersionBranchs.length;i++){
        					if( data.systemVersionBranchs[i].scmBranch!=null && data.systemVersionBranchs[i].systemVersionId!=undefined){
        						$("#editsystemVersionBranch").append('<option value="'+data.systemVersionBranchs[i].systemVersionId+','+data.systemVersionBranchs[i].scmBranch+'">'+data.systemVersionBranchs[i].systemVersionName+'-->'+data.systemVersionBranchs[i].scmBranch+'</option>');
        					}
        				}
        			}*/
        			
        			$('.selectpicker').selectpicker('refresh'); 
        			
        		}
        	});
    		if($("#edevelopmentMode").val() == 1){//关联的系统是敏态 可以选择选择冲刺
				 $.ajax({
					 url:"/devManage/devtask/getSprintBySystemId",
					 data:{
						 systemId:$("#editsystemId").val()
					 },
					 type:"post",
					 dataType:"json",
					 success:function(data){
						 $("#editSprintId").empty();
						 $("#editSprintId").append("<option value=''>请选择</option>");
						 if(data.sprintInfos.length>0){
							 $("#sprintDiv").show();
							 for(var i = 0;i<data.sprintInfos.length;i++){
								 $("#editSprintId").append('<option value="'+data.sprintInfos[i].id+'">'+data.sprintInfos[i].sprintName+'</option>')
							 }
						 }
		        		$('.selectpicker').selectpicker('refresh'); 
					 }
				 })
				 //故事点
				 $("#estoryPointDiv").show();
				 $("#editdevTaskStatus").empty();
	   			 $("#editdevTaskStatus").append("<option value=''>请选择</option>");
	   			 
	   			$("#editdevTaskStatus").attr("disabled",false);
	   			 if(devtaskStatusList!=null){
	   				for(var i=0;i<devtaskStatusList.length;i++){
						if(devtaskStatusList[i].valueCode!=""){
							var flag = '';
							if(editDevtaskStatus == devtaskStatusList[i].valueCode){
								flag = "selected";
							}
		   		    		$("#editdevTaskStatus").append('<option '+flag+' value="'+devtaskStatusList[i].valueCode+'">'+devtaskStatusList[i].valueName+'</option>')

						}
	   				}
	   		    }
				
			}else{
				 $("#sprintDiv").hide();
				 $("#editSprintId").empty();
				 $("#estoryPointDiv").hide();
				 
				$("#editdevTaskStatus").empty();
   				$("#editdevTaskStatus").append("<option value=''>请选择</option>");
   				for(var i=0;i<devtaskStatusList.length;i++){
					if(devtaskStatusList[i].valueCode!=""){
						var flag = '';
						if(editDevtaskStatus == devtaskStatusList[i].valueCode ){
							flag = "selected";
						}
						$("#editdevTaskStatus").append('<option '+flag+' value="'+devtaskStatusList[i].valueCode+'">'+devtaskStatusList[i].valueName+'</option>');
					}
				}
   				$("#editdevTaskStatus").attr("disabled",true);
			}
    	}
    	
    });
    //以上为原先重构之前代码，没有仔细查看
    //以下为分离出的查看功能   
    parameterArr.arr = window.location.href.split( "?" ); 
    parameterArr.parameterArr = parameterArr.arr[1].split( "," );
    parameterArr.obj={};
    for( var i = 0 ; i < parameterArr.parameterArr.length ; i++ ){
    	var obj = parameterArr.parameterArr[i].split( "=" );  
    	parameterArr.obj[ obj[0] ] = obj[1];
    }    
    showDevTask( parameterArr.obj.rowId ); 
    
    $(".layui-layer-title .focusInfo",window.parent.document).bind("click",function(){
    	var flag;
    	if( $( this ).hasClass("noheart") ){
    		$( this ).removeClass("noheart")
    		$(this).attr("title","点击不再关注");
    		flag = 1;
    	}else {
    		$( this ).addClass("noheart")
    		$(this).attr("title","点击关注");
    		flag = 2;
    	} 
    	$.ajax({
            type:"POST",
            url:"/devManage/devtask/changeAttention",
            dataType:"json",
            data:{
            	id: $( this ).attr("id"),
            	attentionStatus: flag
            }, 
            success:function( data ){
            	 if( data.status == "success" ){
            		 if( flag ==1 ){
            			 layer.alert("关注成功！",{icon:1});
            		 }else{
            			 layer.alert("取消关注成功！",{icon:1});
            		 }
            	 }else{
            		 layer.alert("系统内部错误！！！",{icon:2});
            	 }
            }    	
        }); 
    })
    
});

 

function findByStatus(){
	var ids = $("#requirementFeatureStatus").val();
	var arr = ids.split(",");
	$("#devTaskStatus option").each( function (i, n) {
	    for (var j = 0,len = arr.length; j < len ; j++){
	        if (n.value == arr[j]) {
	            n.selected = true;
	        }
	    }
	});
	}

function getDiffWindow(rows){
	var diffbrother;
	$.ajax({
		url:"/devManage/devtask/getBrotherDiffWindow",
		dataType:"json",
		type:"post",
		async:false,
		data:{
			id:rows.id,
			requirementId:rows.requirementId,
			commissioningWindowId:rows.commissioningWindowId
		},
		success:function(data){
			diffbrother = data['brothers'];
		},
		error:function(){
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
	return diffbrother;
}

//合并
function findSyn(row){
	var id = row.id;
	var requirementId = row.requirementId;
	var systemId = row.systemId;
	$.ajax({
		url:'/devManage/devtask/findSynDevTask',
		dataType:'json',
		type:'post',
		data:{
			id:id,
			requirementId:requirementId,
			systemId:systemId
		},
		success : function(data){
			if(data.length>0){
				if(data.length == 1){
					var str = "您确定要合并"+data[0].featureCode+" | "+data[0].featureName+"吗？";
					layer.confirm(str, {
						  btn: ['确定', '取消'] ,
						  icon:3
						}, function(index, layero){
						$.ajax({
							url:'/devManage/devtask/merge',
							dataType:'json',
							type:'post',
							data:{
								id:id,
								synId:data[0].id,
								taskId:data[0].taskId,
								featureCode:data[0].featureCode
							},
							success : function(data){
								if(data.status == "success"){
									layer.alert('合并成功！', {icon: 1});
									pageInit();
								
								}else if(data.status == "2"){
									layer.alert('合并失败！！！', {icon: 2});
								}
							},
							error:function(){
					            $("#loading").css('display','none');
					            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
					        }
						})
					})
				}else{
					$("#reqFId").val('');
					$("#reqFId").val(id);
					$("#mergeDiv").empty();
					var str ="";
					for(var i=0;i<data.length;i++){
						str+="<div><input name='synDT' value="+data[i].taskId+" featureCode="+data[i].featureCode+" type='radio' id="+data[i].id+"  />"+data[i].featureCode+" | "+data[i].featureName+"</div><br/>";
					}
					$("#mergeDiv").append(str);
					
					$("#mergeDevTask").modal("show");
				}
			}else{
				layer.alert('没有需要合并的同步开发任务！！！', {icon: 0});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
}

//合并开发任务
function mergeCommit(){
	var id = $("#reqFId").val();
	var taskId = $("#mergeDiv input[name='synDT']:checked").val();  
	var selectId = $("input[name='synDT']").filter(':checked').attr( 'id' ); 
	var featureCode = $("input[name='synDT']").filter(':checked').attr( 'featureCode' ); 
	if(selectId == undefined){
		layer.alert('请选择一个同步任务！！！', {icon: 0});
	}
	$("#loading").css('display','block');
	$.ajax({
		url:'/devManage/devtask/merge',
		dataType:'json',
		type:'post',
		data:{
			id:id,
			synId:selectId,
			taskId:taskId,
			featureCode:featureCode,
		},
		success : function(data){
			 $("#loading").css('display','none');
			if(data.status == "success"){
				layer.alert('合并成功！', {icon: 1});
				pageInit();
				$("#mergeDevTask").modal("hide");
			}else if(data.status == "2"){
				layer.alert('合并失败！！！', {icon: 2});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
}

//查询信息
function searchInfo(){
	var devTaskStatus = $("#devTaskStatus").val();
	if(devTaskStatus!=null && devTaskStatus!=''){
		devTaskStatus = devTaskStatus.join(",");
	}
	 var reqFeaturePriority = $("#reqFeaturePriority").val();
     if(reqFeaturePriority!=null && reqFeaturePriority!=''){
    	 reqFeaturePriority = reqFeaturePriority.join(",");
     }
	var planVersion = $("#planVersion").val();
	$("#loading").css('display','block');
	$("#list2").jqGrid('setGridParam',{ 
        postData:{
        	 "featureCode" :$.trim( $("#devTaskCode").val()),
             "featureName" : $.trim( $("#featureName").val()),
             "manageUserIds" : $("#devManPost").val(),
             "requirementFeatureStatus" : devTaskStatus, 
             "commissioningWindowIds" :  planVersion,
             "requirementIds" :  $("#requirementId").val(),
             "systemIds" :  $("#systemId").val(),
             "createType": $("#createTye").val(),
             "executeUserIds":$("#execteUserId").val(),
             "reqFeaturePrioritys":reqFeaturePriority,
             "sprints":$("#sprintIds").val()
             },  
        page:1
    }); 
	$("#list2").jqGrid('setGridParam',{
		url:"/devManage/devtask/getAllDevTask2",
		loadComplete :function(){
			$("#loading").css('display','none');		
		}	
	}).trigger("reloadGrid"); //重新载入
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
			
			$("#checkdeloStatus").text('');
			if(data.deployStatus){
				var Status_str = '';
				deployStatusData.map(function(v){
					if(data.deployStatus.indexOf(v.valueCode) != -1){
						Status_str += v.valueName;
					}
				})
				$("#checkdeloStatus").text(Status_str);
			}
			
			$("#checkoutrequirementDiv").hide();
			$("#checkDefectDiv").hide();
			$("#checkRequstNumberDiv").hide();
			$("#checksystemName").text('');
			$("#executeProjectGroupId").text('');
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
            $("#checkProjectPlanName").text('');
			
            $("#reset_requirementName").text('');
            $("#reset_requirementCode").text('');
            $("#reset_requirementName").attr("requirementId","");
            requirementParentId="";
            $("#reset_createBy").text('');
            $("#reset_createDate").text('');
			$(".layui-layer-title span",window.parent.document).text( toStr(data.featureCode) +" | "+ toStr(data.featureName) );
			
			$(".layui-layer-title .focusInfo",window.parent.document).attr("id",parameterArr.obj.rowId);
  			if( data.attentionStatus == 2 ){
  				//不关注
  				$(".layui-layer-title .focusInfo",window.parent.document).addClass("noheart");
  				$(".layui-layer-title .focusInfo",window.parent.document).attr("title","点击关注"); 
  			}else if( data.attentionStatus == 1 ){
  				//关注
  				$(".layui-layer-title .focusInfo",window.parent.document).removeClass("noheart");
  				$(".layui-layer-title .focusInfo",window.parent.document).attr("title","点击不再关注"); 
  			} 
  			
			$("#checkdevTaskOverview").text(data.featureOverview);

			// $("#checkreqFeaturePriority").text(data.requirementFeaturePriority);
            if(data.requirementFeaturePriority != null){
                for (var i = 0,len = reqFeaturePriorityList.length;i < len;i++) {
                    if(data.requirementFeaturePriority == reqFeaturePriorityList[i].value){
                        $("#checkreqFeaturePriority").text(reqFeaturePriorityList[i].innerHTML);
                    }
                }
            }else{
                $("#checkreqFeaturePriority").text('');
            }

			$("#checkSprintName").text(data.sprintName);
			$("#checkStoryPoint").text(data.storyPoint); 
			$("#modelName").text(data.systemTreeName);
			$("#checkRepairVersion").text(data.repairVersionName);
			requirementParentId=data.requirementParentId;
			showField( data.fields )
			
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
			$("#executeProjectGroupId").text("");
			$("#executeProjectGroupId").text(data.executeProjectGroupName);

			$("#checksystemName").text("");
			$("#checksystemName").text(data.systemName);
			
			$("#checkProjectName").text("");
			$("#checkProjectName").text(data.projectName);

			$("#checkoutrequirement").text("");
			$("#checkRequstNumber").text("");
			
			$("#checkWindowName").text("");
			$("#checkWindowName").text(data.windowName);
			$("#checkSystemVersion").text("");
			$("#checkSystemVersion").text(toStr(data.versionName)+"-->"+toStr(data.systemScmBranch));
			
			$("#checkStatus").text("");
			if(data.checkStatus == 2){
				$("#checkStatus").text('待验收');
			}else if(data.checkStatus == 1){
				$("#checkStatus").text('无需验收');
			}else if(data.checkStatus == 3){
				$("#checkStatus").text('验收完成');
			}
			
			$("#checkItcdReqId").val("");
			$("#checkItcdReqId").val(data.itcdRequrementId);
			$("#checktaskId").val("");
			$("#checktaskId").val(data.taskId);
			
			//$("#reset_requirementName").text(data.requirementName);
			if(data.requirementName!=undefined){
				$("#reNameAndCode").prepend( '<div class=" "><div class="def_col_36 fontWeihgt"><a class="a_style" requirementId="" id="reset_requirementName" onclick="showRequirement()">'+toStr(data.requirementCode)+'</a>'+'  '+data.requirementName+'</div></div>');
				
			}else{
				 $("#reNameAndCode").css('display','none');
				 $("#reNameAndCode").empty();
			}
			
			if(data.requirementPriority!=undefined){
	  			for (var i = 0,len = reqPriorityList.length;i < len;i++) {
	                if(data.requirementPriority.toLowerCase() == reqPriorityList[i].value.toLowerCase()){               	
	                	$("#reset_priority").text(reqPriorityList[i].innerHTML);  
	                }
	            }
  			}
			if(data.imporiantRequirementStatus!=undefined){
	  			for (var i = 0,len = impReqStatusList.length;i < len;i++) {
	                if(data.imporiantRequirementStatus.toLowerCase() == impReqStatusList[i].value.toLowerCase()){               	
	                	$("#reset_imporiantRequirementStatus").text(impReqStatusList[i].innerHTML);  
	                }
	            }
  			}
            //$("#reset_requirementCode").text(data.requirementCode);
            $("#reset_createBy").text(data.requirementApplyName);
            if(data.requirementCreateDate!=undefined){
            	$("#reset_createDate").text(datFmt(new Date(data.requirementCreateDate),"yyyy-MM-dd hh:mm:ss"));
            }
            
            $("#reset_requirementName").attr("requirementId",data.requirementId);
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
							dftName+= '<a class="a_style" onclick="showDefect('+data.defectInfos[i].id+')"> '+data.defectInfos[i].defectCode+'</a>,';
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
			$("#estimateRemainWorkload").text(data.estimateRemainWorkload); //预估剩余工作量(人天)
			$("#checkactStartDate").text('');
			$("#checkactStartDate").text(data.actualStartDate);
			$("#checkactEndDate").text('');
			$("#checkactEndDate").text(data.actualEndDate);
			$("#checkactWorkload").text('');
			$("#checkactWorkload").text(data.actualWorkload);
			$("#checkhandSug").text('');
			$("#checkhandSug").text(data.handleSuggestion);
            $("#checkProjectPlanName").text(data.projectPlanName);
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
					$("#connectDiv").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="getSee('+data.list[i].id+')">'+data.list[i].devTaskCode+'</a>'+'   '+data.list[i].devTaskName+'</div>'+
							'<div class="def_col_36">预估工作情况：'+toStr(data.list[i].planStartDate)+'~'+toStr(data.list[i].planEndDate)+' '+toStr(data.list[i].planWorkLoad)+'人天</div>'+
							'<div class="def_col_36">实际工作情况：'+toStr(data.list[i].actualStartDate)+'~'+toStr(data.list[i].actualEndDate)+' '+toStr(data.list[i].actualWorkload)+'人天</div>'+
							'<div class="def_col_36">'+data.list[i].devtaskStatusName+' '+toStr(data.list[i].devUsername)+'</div></div>');
				}
			}
			//相关开发任务的显示
			if(data.brother!=undefined){
				for(var i=0;i<data.brother.length;i++){
					$("#brother_div").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="showDevTask('+data.brother[i].id+')">'+toStr(data.brother[i].featureCode)+'</a>'+'  '+data.brother[i].featureName+'</div>'+
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
						if(data.logs[i].logType!=null&&data.logs[i].logType.indexOf("新增开发任务")=="-1"){
							logDetail = "未作任何修改";
						}
						if(logDetail==''){
							style2= 'style="display: none;"';
						}
						addDiv = '<br>';
					}else{
						logDetail = data.logs[i].logDetail;
						logDetail=logDetail.replace(/；/g,"<br/>");
						logDetail = logDetail.replace(/null/g,' ');
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
			$("#loading").css('display','none'); 
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
	modalType = 'check'; 
}
//提交备注
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
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
}
//转派提交
function transferCommit(){
	if($("#transferId").val()==''){
		layer.alert("转派人不能为空！！！",{icon:0});
		return;
	}
	if($("#transferId").val()== $("#tEcecuteUserId").val()){
		layer.alert("不可以转派给同一个人！！！",{icon:0});
		return;
	}
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/transfer",
		dataType:"json",
		type:"post",
		data:{
			id:$("#transferDevtaskId").val(),
			executeUserId : $("#transferId").val(),
			transferRemark : $("#transferRemark").val()
		},
		success : function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){
				layer.alert('转派成功！', {icon: 1});
				pageInit();
				$("#transferDevTask").modal("hide");
			}else if(data.status =="2"){
				layer.alert('转派失败！！！', {icon: 2});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
}
//拆分提交
function splitCommit(){
	var reqFeatureId = $("#splitDevtaskId").val();
	var devTaskName= $("#sWorkSummary").val();
	var devTaskOverview = $("#sWorkOverView").val();
	var planStartDate = $("#sWorkStart").val();
	var planEndDate = $("#sWorkEndStart").val();
	var devUserId = $("#sWorkDivid").val();
	var commissioningWindowId = $("#splitCommitWindowId").val();
	var requirementFeatureStatus = $("#splitStatus").val();
	var planWorkload = $("#sWorkPlanWorkload").val();
	
	$('#splitForm').data('bootstrapValidator').validate();
	if(!$('#splitForm').data('bootstrapValidator').isValid()){
		return;
	}
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/splitDevTask",
		type:"post",
		dataType:"json",
		data:{
			"id":reqFeatureId,
			"devTaskName":devTaskName,
			"devTaskOverview":devTaskOverview,
			"planWorkload":planWorkload,
			"devUserId":devUserId,
			"startDate":planStartDate,
			"endDate":planEndDate,
			"commissioningWindowId":commissioningWindowId,
			"requirementFeatureStatus":requirementFeatureStatus,
			"sprintId":$("#ssprintId").val()
			
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){
				layer.alert('拆分成功！', {icon: 1});
				pageInit();
				$("#splitDevTask").modal("hide");
			}else if(data.status == "2"){
				layer.alert('拆分失败！！！', {icon: 2});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})	
}

//同任务
function sameReq(){
	var summary = $("#splitSummary").text();
	var workname = '';
	if((summary.indexOf('|'))==-1){
		workname = summary.slice(0);
	}else{
		workname = summary.slice(summary.indexOf('|')+2);//正常是+1  +2是因为|后还有个空格
	}
	$("#sWorkSummary").val(workname).change();
	$("#sWorkOverView").val($("#sOverView").text()).change();
}

//转派 弹框
function transfer(row){
	var id = row.id;
	var systemId = row.systemId;
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/getOneDevTask3",
		dataType:"json",
		type:"post",
		data:{
			"id":id,
			"systemId":systemId
		},
		success:function(data){
			$("#loading").css('display','none');
			$("#tRequirementDiv").hide();
			$("#tDefectDiv").hide();
			$("#tRequstNumberDiv").hide();
			
			$("#transferRemark").val('');
			$("#transferSystemId").val('');
			$("#transferSystemId").val(systemId);
			$("#transferId").val('');
			$("#tEcecuteUserId").val('');
			excuteUserName = '';
			excuteUserName = data.executeUserId;
			$("#transferUserName").val('');
			$("#transferDevtaskId").val(data.id);
			$("#tSystemId").text('');
			$("#tDefect").text('');
			$("#tDeptId").text('');
			$("#tRequirementId").text('');
			$("#tRequstNumber").text('');
		    $("#transferCommitWindowId").val(data.commissioningWindowId);
		    $("#transferStatus").val(data.requirementFeatureStatus);
			$("#tSummary").text(toStr(data.featureCode) +" | "+ toStr(data.featureName));
			$("#tOverView").text(data.featureOverview);
			var statusName = '';
			for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
                if(devtaskStatusList[i].valueCode == data.requirementFeatureStatus){
                	statusName=  devtaskStatusList[i].valueName;
             	   break;
                }
              }
			$("#tDevtaskStatus").text(statusName);
			$("#tManageUser").text(data.manageUserName);
			$("#tEcecuteUserId").val(data.executeUserId);
			$("#tEcecuteUser").text(data.executeUserName);
			$("#tSystemId").text(data.systemName);
			
			if(data.requirementFeatureSource!=undefined){
				if(data.requirementFeatureSource==1){
					$("#tRequirementDiv").show();
					$("#tDefectDiv").hide();
					$("#tRequstNumberDiv").hide();
					$("#tRequirementId").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
				}else if(data.requirementFeatureSource==2){
					$("#tRequirementDiv").hide();
					$("#tDefectDiv").hide();
					$("#tRequstNumberDiv").show();
					$("#tRequstNumber").text(data.questionNumber);
				}else if(data.requirementFeatureSource==3){
					$("#tRequirementDiv").hide();
					$("#tDefectDiv").show();
					$("#tRequstNumberDiv").hide();
					var dftName='';
					for(var i=0;i<data.defectInfos.length;i++){
						if(data.defectInfos[i].requirementFeatureId == id){
							dftName+= data.defectInfos[i].defectCode+",";
						}
					}
					$("#tDefect").text(dftName.substring(0,dftName.length-1));
				}
			}
			
			$("#tDeptId").text(data.deptName);
			
			$("#tWorkStart").val(data.planStartDate);
			$("#tWorkEndStart").val(data.planEndDate);
			$("#tWorkPlanWorkload").val(data.planWorkload);
			
			var a = '';
			if(data.temporaryStatus == 1){
				a = "是";
			}
			if(data.temporaryStatus == 2){
				a="否";
			}
			$("#tTemporaryTask").text(a);
			
            $('.selectpicker').selectpicker('refresh'); 
          
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
	$("#transferDevTask").modal("show");
	
}
//拆分弹框
function split(row){ 
	layer.open({
      type: 2,
      title: '开发任务',
      shadeClose: true,
      shade: false,
      maxmin: true, //开启最大化最小化按钮
      area: ['90%', '90%'],
      offset: '4%',
      content: '/devManageui/devtask/toSplit'
    });
	 
	
	
	/*$(".countWorkloadDiv").css( "display","none" );
	
	$("#sWorkEndStart").unbind("change");
	$("#sWorkStart").unbind("change");
	$("#sWorkPlanWorkload").unbind("change");
	$('#splitDevTask').on('hide.bs.modal', function () {
		     $('#splitForm').bootstrapValidator('resetForm');
		});
	var id = row.id;
	var systemId = row.systemId;
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/getOneDevTask",
		dataType:"json",
		type:"post",
		data:{
			"id":id,
			"systemId":systemId
		},
		success:function(data){
			$("#loading").css('display','none');
			$("#sRequirementDiv").hide();
			$("#sDefectDiv").hide();
			$("#sRequstNumberDiv").hide();
			$("#sWorkSummary").val("");
			$("#sWorkOverView").val('');
			$("#sWorkPlanWorkload").val('');
			$('#sWorkStart').val('');
			$('#sWorkEndStart').val('');
			$("#splitSystemId").val('');
			$("#splitSystemId").val(systemId);
			$("#sWorkDivid").val('');
			$("#splitDevtaskId").val(data.id);
			$("#sWorkDividUserName").val('');
			$("#sSystemId").text('');
			$("#sDefect").text('');
			$("#sDeptId").text('');
			$("#sRequirementId").text('');
			$("#sRequstNumber").text('');
			 $("#sworkDevTaskDiv").empty();
			 $("#ssprintId").val('');
			 $("#ssprintName").val('');
			 
			 $("#ssprintId").val(data.sprintId);
			 $("#ssprintName").val(data.sprintName);
		    $("#splitCommitWindowId").val(data.commissioningWindowId);
		    $("#splitStatus").val(data.requirementFeatureStatus);
			$("#splitSummary").text(toStr(data.featureCode) +" | "+ toStr(data.featureName));
			$("#sOverView").text(data.featureOverview);
			var statusName = '';
			for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
                if(devtaskStatusList[i].valueCode == data.requirementFeatureStatus){
                	statusName=  devtaskStatusList[i].valueName;
             	   break;
                }
              }
			$("#sDevtaskStatus").text(statusName);
			$("#sManageUser").text(data.manageUserName);
			$("#sEcecuteUser").text(data.executeUserName);
			$("#sSystemId").text(data.systemName);
			
			if(data.developmentMode!=undefined && data.developmentMode == 1){
				$("#ssprintDiv").show();
			}else{
				$("#ssprintDiv").hide();
			}
			
			if(data.requirementFeatureSource!=undefined){
				if(data.requirementFeatureSource==1){
					$("#sRequirementDiv").show();
					$("#sDefectDiv").hide();
					$("#sRequstNumberDiv").hide();
					$("#sRequirementId").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
				}else if(data.requirementFeatureSource==2){
					$("#sRequirementDiv").hide();
					$("#sDefectDiv").hide();
					$("#sRequstNumberDiv").show();
					$("#sRequstNumber").text(data.questionNumber);
				}else if(data.requirementFeatureSource==3){
					$("#sRequirementDiv").hide();
					$("#sDefectDiv").show();
					$("#sRequstNumberDiv").hide();
					var dftName='';
					for(var i=0;i<data.defectInfos.length;i++){
						if(data.defectInfos[i].requirementFeatureId == id){
							dftName+= data.defectInfos[i].defectCode+",";
						}
					}
					$("#sDefect").text(dftName.substring(0,dftName.length-1));
				}
			}
			
			$("#sDeptId").text(data.deptName);
			
			$("#sWorkStart").val(data.planStartDate);
			$("#sWorkEndStart").val(data.planEndDate);
			$("#sWorkPlanWorkload").val(data.planWorkload);
			
			var a = '';
			if(data.temporaryStatus == 1){
				a = "是";
			}
			if(data.temporaryStatus == 2){
				a="否";
			}
			$("#sTemporaryTask").text(a);
			
            $('.selectpicker').selectpicker('refresh'); 
            
          //下属工作任务的显示
			if(data.list!=undefined){
				for(var i=0;i<data.list.length;i++){
					 $("#sworkDevTaskDiv").append( '<div class="rowdiv "><div class="def_col_36"><a class="a_style" onclick="getSee('+data.list[i].id+')">'+data.list[i].devTaskCode+'</a>'+'   '+data.list[i].devTaskName+'</div>'+
							'<div class="def_col_36">预估工作情况：'+toStr(data.list[i].planStartDate)+'~'+toStr(data.list[i].planEndDate)+' '+toStr(data.list[i].planWorkLoad)+'人天</div>'+
							'<div class="def_col_36">实际工作情况：'+toStr(data.list[i].actualStartDate)+'~'+toStr(data.list[i].actualEndDate)+' '+toStr(data.list[i].actualWorkload)+'人天</div>'+
							'<div class="def_col_36">'+data.list[i].devtaskStatusName+' '+toStr(data.list[i].devUsername)+'</div></div>');
				}
			}
			
            $("#splitDevTask").modal("show");
            
            $('#sWorkStart').trigger('change');
        	$("#sWorkStart").bind("change",function(){
        		if($("#sWorkStart").val() < data.planStartDate){
        			layer.alert("您选择的日期早于当前开发任务的预计开始时间！",{icon:0});
        		}
            });
        	 $('#sWorkEndStart').trigger('change');
	        	$("#sWorkEndStart").bind("change",function(){
	        		if($("#sWorkEndStart").val() > data.planEndDate){
	        			layer.alert("您选择的日期晚于当前开发任务的预计结束时间！",{icon:0});
	        		}
	            });
	       $("#sWorkPlanWorkload").change(function(){
	    	   if($("#sWorkPlanWorkload").val()>data.planWorkload){
	    		   layer.alert("您输入的预计工作量大于当前开发任务的预计工作量！",{icon:0});
	    	   }
	       });
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})*/
}
//查看人员未完成工作量 当前任务所属系统下所有开发任务下所有工作任务  统计 姓名 工单数量 预估工作量
function checkNoCompleteWorkload(){
	var systemId = $("#splitSystemId").val();
	if(systemId == null || systemId == ''){
		layer.alert("该任务还未关联系统，不可以查看！",{icon:1});
	}
	$.ajax({
		url:"/devManage/worktask/countWorkloadBysystemId",
		dataType:"json",
		type:"post",
		data:{
			systemId:systemId
		},
		success:function(data){
			countWorkloadTable(data.countDatas);
			$(".countWorkloadDiv").css( "display","block" );
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
}
function countWorkloadTable(data){
	$("#countWorkloads").bootstrapTable('destroy');
	$("#countWorkloads").bootstrapTable({
		data : data,
		method : "post",
	    cache: false,
		columns : [{
			field : "userName",
			title : "开发人员姓名",
			align : 'center'
		},{
			field : "workNum",
			title : "工单数量",
			align : 'center'
		},{
			field : "planWorkLoads",
			title : "预计总工作量",
			align : 'center',
		}]
	});
}

//编辑提交
function editDevTask(){
	var id = $("#editDevTaskId").val();
	var featureName = $("#efeatureName").val();
	var featureOverview = $("#efeatureOverview").val();
	var systemId = '';
	if($("#editsystemName").val() != ''){
		systemId = $("#editsystemId").val();
	}
	var deptId = $("#editdeptId").val();
	var requirementId = '';
	if($("#editrequirementName").val()!=''){
		requirementId = $("#editrequirementId").val();
	}
	var executeUserId = $("#editexecuteUser").val();
	var manageUserId = $("#editdevManageUser").val();
	
	var planStartDate = $("#epstartWork").val();
	var planEndDate = $("#ependWork").val();
	var estimateWorkload= $("#eestimateWorkload").val();
	var actualStartDate = $("#eastartWork").val();
	var actualEndDate = $("#eaendWork").val();
	var actualWorkload = $("#eactualWorkload").val();
	var commissioningWindowId = $("#editcommitWindowId").val();
	var requirementFeatureSource = $("#editrequirementFeatureSource").val();
	var questionNumber = $("#editquestionNumber").val();
	var sprintId = $("#editSprintId").val();
	var versionArr = $("#editsystemVersionBranch").val().split(",");
	var systemVersionId = versionArr[0];
	var systemScmBranch = versionArr[1];
	$('#editForm').data('bootstrapValidator').validate();
	if(!$('#editForm').data('bootstrapValidator').isValid()){
		return;
	}
	/*var _fileStr = [];
	for(var i=0;i<_editfiles.length;i++){
		_fileStr.push(_editfiles[i].fileNameOld);
	}
	var s = _fileStr.join(",")+",";
	for(var i=0;i<_fileStr.length;i++){
		if(s.replace(_fileStr[i]+",","").indexOf(_fileStr[i]+",")>-1){
			layer.alert(_fileStr[i]+"文件重复！！！", {icon: 0});
			return;
		}
	}*/
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/updateDevTask",
		dataType:"json",
		type:"post",
		data:{
			"id" : id ,
		    "featureName":featureName,
			"featureOverview":featureOverview,
		    "systemId": systemId,
			"deptId": deptId,
			"requirementId": requirementId,
			"executeUserId":executeUserId,
			"manageUserId":manageUserId,
			"pstartDate": planStartDate,
			"pendDate": planEndDate,
			"estimateWorkload":estimateWorkload,
			"astartDate":actualStartDate,
			"aendDate":actualEndDate,
			"actualWorkload": actualWorkload,
			"commissioningWindowId":commissioningWindowId,
			"requirementFeatureSource":requirementFeatureSource,
			"questionNumber":questionNumber,
			"defectIds":$("#editdefectId").val(),
			"systemVersionId":systemVersionId,
			"systemScmBranch":systemScmBranch,
			"requirementFeatureStatus":$("#editdevTaskStatus").val(),
			"attachFiles" :$("#editfiles").val(),
			"sprintId":sprintId,
			"storyPoint":$("#editstoryPoint").val(),
			"requirementFeaturePriority":$("#editreqFeaturePriority").val()
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status=="success"){
				layer.alert('编辑成功！', {icon: 1});
				pageInit();
				$("#editDevTask").modal("hide");
			}else if(data.status == "2"){
				layer.alert('编辑失败！！！', {icon: 2});
			}else if(data.status =="repeat"){
				layer.alert('该任务名称已存在！！！', {icon: 0});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
}

//编辑弹出框
function edit(row){
	
	layer.open({
      type: 2,
      title: '开发任务',
      shadeClose: true,
      shade: false,
      maxmin: true, //开启最大化最小化按钮
      area: ['90%', '500px'],
      content: '/devManageui/devtask/toEdit'
    });
	/*$('#editDevTask').on('hide.bs.modal', function () {
		     $('#editForm').bootstrapValidator('resetForm');
		});
	var id = row.id;
	editDevtaskStatus = '';
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/getOneDevTask3",
		dataType:"json",
	    type:"post",
		data:{
			"id":id
		},
		success:function(data){
			if(data.status == "2"){
				layer.alert("数据加载失败！",{icon:2});
			}
			$("#edevelopmentMode").val();
			$("#editSprintId").empty();
			$("#editSprintId").append('<option value="">请选择</option>');
			$("#editsystemId").val('');
			$("#editsystemName").val('');
			$("#editexecuteUser").val('');
			$("#editsystemId").val('');
			$("#editrequirementName").val('');
			$("#dftReqFeatureId").val('');
			$("#editdefectCode").val('');
			$("#editdefectId").val('');
			$("#edevelopmentMode").val('');
			$("#editreqFeaturePriority").empty();
			$("#editreqFeaturePriority").append('<option value="">请选择</option>');
			$("#editrequirementId").val(data.requirementId);
			$("#editrequirementFeatureSource").empty();
			$("#editrequirementFeatureSource").append('<option value="">请选择</option>');
			//$("#editdevManageUser").empty();
			//$("#editdevManageUser").append('<option value="">请选择</option>');
			$("#editexecuteUser").val('');
			$("#editexecuteUserName").val('');
			$("#editdevManageUser").val('');
			$("#editdevManageUserName").val('');
			$("#editfiles").val('');
			$("#editdevTaskStatus").empty();
			$("#editdeptId").val('');
			$("#editDeptName").val('');
			$("#editcommitWindowId").val("");
			$("#editcommitWindowName").val("");
			//$("#editcommitWindowId").empty();
			//$("#editcommitWindowId").append('<option value="">请选择</option>');
			$("#editrequirementId").empty();
			$("#editFileTable").empty();
			$("#editsystemVersionBranch").empty();
			$("#editsystemVersionBranch").append("<option value=''>请选择</option>");
			$("#editdevTaskStatus").empty();
			$("#editdevTaskStatus").append("<option value=''>请选择</option>");
			
			$("#editDevTaskId").val(data.id);
			$("#dftReqFeatureId").val(data.id);
			$("#efeatureName").val(data.featureName);
			$("#efeatureOverview").val(data.featureOverview);
			$("#epstartWork").val(data.planStartDate);
			$("#ependWork").val(data.planEndDate);
			$("#eestimateWorkload").val(data.planWorkload);
			$("#eastartWork").val(data.actualStartDate);
			$("#eaendWork").val(data.actualEndDate);
			$("#eactualWorkload").val(data.actualWorkload);
			$("#editexecuteUser").val(data.executeUserId);
			$("#editsystemId").val(data.systemId);
			$("#editrequirementId").val(data.requirementId);
			$("#editquestionNumber").val(data.questionNumber).change();
			$("#editdevManageUser").val(data.manageUserId);
			$("#editdeptId").val(data.deptId);
			$("#editDeptName").val(data.deptName).change();
			$("#editdevManageUserName").val(data.manageUserName);
			$("#editexecuteUserName").val(data.executeUserName);
			$("#editrequirementName").val(data.requirementCode).change();
			$("#editsystemName").val( data.systemName ); 
			$("#editcommitWindowId").val(data.commissioningWindowId);
			$("#editcommitWindowName").val(data.windowName).change();
			
			editDevtaskStatus = data.requirementFeatureStatus;
			
			if(reqFeaturePriorityList!=null){
				for (var i = 0,len = reqFeaturePriorityList.length;i < len;i++) {
					var flag = '';
					if(reqFeaturePriorityList[i].value == data.requirementFeaturePriority){
						flag = "selected";
					}
	                $("#editreqFeaturePriority").append('<option '+flag+' value="'+reqFeaturePriorityList[i].value+'"> '+reqFeaturePriorityList[i].innerHTML+'</option>')
	             }
			}
			if(data.createType =="1"){
				$("#editCreateFile").show();
			}else {
				$("#editCreateFile").hide();
			}
			
			if($("#editsystemId").val()!=null && $("#editsystemId").val()!=''){
				$.ajax({
		    		url:"/devManage/systemVersion/getSystemVersionByCon",
		    		dataType:"json",
		    		type:"post",
		    		data:{
		    			systemId:$("#editsystemId").val()
		    		},
		    		success:function(data2){
		    			$("#editsystemVersionBranch").empty();
		    			$("#editsystemVersionBranch").append("<option value=''>请选择</option>");
		    			
		    			var systemVersionStr='';
		  				$.each(data2.rows,function(index,value){
		  					var flag = '';
		  					if(data.systemScmBranch == value.groupFlag && data.systemVersionId == value.id){
	    						flag = 'selected';
	    					}
		  					
							systemVersionStr += '<option '+flag+' value="'+value.id+','+value.groupFlag+'">'+value.version+'--> '+toStr(value.groupFlag)+'</option>'
						});
		  				$("#editsystemVersionBranch").append(systemVersionStr);
		    			if(data2.systemVersionBranchs.length>0){
		    				for(var i=0; i<data2.systemVersionBranchs.length;i++){
		    					var flag = '';
		    					if(data.systemScmBranch == data2.systemVersionBranchs[i].scmBranch && data.systemVersionId == data2.systemVersionBranchs[i].systemVersionId){
		    						flag = 'selected';
		    					}
		    					if(data2.systemVersionBranchs[i].systemVersionId!=undefined && data2.systemVersionBranchs[i].scmBranch!=null){
		    						$("#editsystemVersionBranch").append('<option '+flag+' value="'+data2.systemVersionBranchs[i].systemVersionId+','+data2.systemVersionBranchs[i].scmBranch+'">'+data2.systemVersionBranchs[i].systemVersionName+'-->'+data2.systemVersionBranchs[i].scmBranch+'</option>');
		    					}
		    				}
		    			}
		    			$('.selectpicker').selectpicker('refresh'); 
		    			
		    		}
		    	});
				if(data.developmentMode == 1){
					$.ajax({
						 url:"/devManage/devtask/getSprintBySystemId",
						 data:{
							 systemId:$("#editsystemId").val()
						 },
						 type:"post",
						 dataType:"json",
						 success:function(data2){
							 $("#editSprintId").empty();
							 $("#editSprintId").append("<option value=''>请选择</option>");
							 if(data2.sprintInfos.length>0){
								 $("#sprintDiv").show();
								 for(var i = 0;i<data2.sprintInfos.length;i++){
									 var flag = '';
									 if(data2.sprintInfos[i].id == data.sprintId){
										 flag = 'selected'
									 }
									 $("#editSprintId").append('<option '+flag+' value="'+data2.sprintInfos[i].id+'">'+data2.sprintInfos[i].sprintName+'</option>')
								 }
							 }
			        		$('.selectpicker').selectpicker('refresh'); 
						 }
					 });
					$("#estoryPointDiv").show();
					$("#editstoryPoint").val(data.storyPoint);
					
					$("#editdevTaskStatus").attr("disabled",false);
					for(var i=0;i<devtaskStatusList.length;i++){
						if(devtaskStatusList[i].valueCode!=""){
							var flag = '';
							if(data.requirementFeatureStatus == devtaskStatusList[i].valueCode){
								flag = "selected";
							}
							
							$("#editdevTaskStatus").append('<option '+flag+' value="'+devtaskStatusList[i].valueCode+'">'+devtaskStatusList[i].valueName+'</option>');
						}
					}
				}else{
					for(var i=0;i<devtaskStatusList.length;i++){
						if(devtaskStatusList[i].valueCode!=""){
							var flag = '';
							if(data.requirementFeatureStatus == devtaskStatusList[i].valueCode){
								flag = "selected";
							}
							$("#editdevTaskStatus").append('<option '+flag+' value="'+devtaskStatusList[i].valueCode+'">'+devtaskStatusList[i].valueName+'</option>');
						}
					}
					$("#editdevTaskStatus").attr("disabled",true);
				}
			}
			if(data.requirementFeatureSource == '1'){
				$("#equestDiv").hide();
	    		$("#ereqDiv").show();
	    		$("#edftDiv").hide();
			}else if(data.requirementFeatureSource == '2'){
				$("#equestDiv").show();
	    		$("#ereqDiv").hide();
	    		$("#edftDiv").hide();
			}else if(data.requirementFeatureSource =='3'){
	    		$("#ereqDiv").hide();
	    		$("#equestDiv").hide();
	    		$("#edftDiv").show();
			}
					
			for(var j=0;j<data.users.length;j++){
				
				if(data.users[j].id == data.manageUserId){
					$("#editdevManageUserName").val(data.users[j].userName);
				}
				if(data.users[j].id == data.executeUserId){
					$("#editexecuteUserName").val(data.users[j].userName);
				}
			}
			if(data.depts!=undefined && data.depts!=null){
				for(var a=0;a<data.depts.length;a++){
					var flag = "";
					if(data.depts[a].id == data.deptId){
						flag = "selected";
					}
					$("#editdeptId").append(' <option '+flag+' value="'+data.depts[a].id+'">'+data.depts[a].deptName+'</option>');
				}
			}
			
			for(var b=0;b<data.requirementInfos.length;b++){
				if(data.requirementInfos[b].id ==data.requirementId){
					$("#editrequirementName").val(data.requirementCode);
				}
			}
			var dftName='';
			var dftId ='';
			if(data.defectInfos!=null && data.defectInfos!=undefined){
				for(var i=0;i<data.defectInfos.length;i++){
					if(data.defectInfos[i].requirementFeatureId == data.id){
						dftName+= data.defectInfos[i].defectCode+",";
						dftId += data.defectInfos[i].id+",";
					}
				}
				$("#editdefectId").val(dftId);
				$("#editdefectCode").val(dftName.substring(0,dftName.length-1)).change();
			}
		
			if(data.cmmitWindows!=undefined && data.cmmitWindows!=null){
				for(var c=0;c<data.cmmitWindows.length;c++){
					var flag = "";
					if(data.cmmitWindows[c].id == data.commissioningWindowId){
						flag = "selected";
					}
					$("#editcommitWindowId").append(' <option '+flag+' value="'+data.cmmitWindows[c].id+'">'+data.cmmitWindows[c].windowName+'</option>');
				}
			}
			
			if(data.dics!=null && data.dics!=undefined){
				for(var i=0;i<data.dics.length;i++){
					var flag='';
					if(data.dics[i].valueCode == data.requirementFeatureSource){
						flag = "selected";
					}
					$("#editrequirementFeatureSource").append('<option '+flag+' value="'+data.dics[i].valueCode+'">'+data.dics[i].valueName+'</option>');
				}
			}
			
			
			//附件
			_editfiles=[];
			if(data.attachements!=undefined && data.attachements!=null){
				if(data.attachements.length>0){
					var _table=$("#editFileTable");
					//var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
					for(var i=0;i<data.attachements.length;i++){
						var _tr = '';
						var file_name = data.attachements[i].fileNameOld;
						var file_type = data.attachements[i].fileType;
						var file_id =  data.attachements[i].id;
						var _td_icon;
						var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.attachements[i].fileS3Bucket+'</i><i class = "file-s3Key">'+data.attachements[i].fileS3Key+'</i></td>';
						var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
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
						_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(this)">'+_td_icon+" "+_td_name+_td_opt+'</tr>'; 
						_table.append(_tr); 
						_editfiles.push(data.attachements[i]);
						$("#editfiles").val(JSON.stringify(_editfiles));
					}
				}
			}
			
			$('.selectpicker').selectpicker('refresh'); 
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
	$("#loading").css('display','none');
	modalType = 'edit';
	$("#editDevTask").modal("show");*/
}
//部门模糊搜索
$(function () {
	var loseInputMillsecs = 500;
	var clocker = null;
	//编辑页面部门模糊搜索
	$("#editDeptName").on('input propertychange', function(){
		innerKeydown();		
	})
	
	function innerKeydown(){
		if(null == clocker){
			clocker = setTimeout(loadData,loseInputMillsecs);
			//console.info(clocker);
		}else{		//连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData,loseInputMillsecs);
		}
	}
	
	function loadData(){
		if($("#editDeptName").val().trim() == '') {
	        $('.selectBox').hide()
	        return
	    }
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: {'deptName': $("#editDeptName").val().trim()},
			dataType: "json",
			success: function (data) {
				var depts= data.depts;
	        	if (depts.length>0) {
	        		var itemStr = ''
	        		for (var i = 0; i < depts.length; i++) {
	        			itemStr += '<li class="selectItem" id="'+depts[i].id+'">'+depts[i].deptName+'</li>'
	        		}
	        		$('.selectUl').html(itemStr)
	        		$('.selectBox').show()
	        	} else {
	        		$('.selectBox').hide()
	        	}
			},
			error:function(){
	            $("#loading").css('display','none');
	            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	        }
		})
		clocker = null;
	}		
	
	$("#selectBox").on('click', '.selectItem', function(){
		$("#editDeptName").val($(this).html());
		$("#editdeptId").val($(this).attr("id"));
		$("#selectBox").hide()
	})
	
	$(document).click(function(){
		$("#selectBox").hide()
	})
	//新增页面模糊搜索
	$("#newDeptName").on('input propertychange', function(){
		innerKeydown2();		
	})
	
	function innerKeydown2(){
		if(null == clocker){
			clocker = setTimeout(loadData2,loseInputMillsecs);
			//console.info(clocker);
		}else{		//连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData2,loseInputMillsecs);
		}
	}
	
	function loadData2(){
		if($("#newDeptName").val().trim() == '') {
	        $('.selectBox').hide()
	        return;
	    }
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: {'deptName': $("#newDeptName").val().trim()},
			dataType: "json",
			success: function (data) {
				var depts= data.depts;
	        	if (depts.length>0) {
	        		var itemStr = ''
	        		for (var i = 0; i < depts.length; i++) {
	        			itemStr += '<li class="selectItem" id="'+depts[i].id+'">'+depts[i].deptName+'</li>'
	        		}
	        		$('.selectUl').html(itemStr)
	        		$('.selectBox').show()
	        	} else {
	        		$('.selectBox').hide()
	        	}
			},
			error:function(){
	            $("#loading").css('display','none');
	            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	        }
		})
		clocker = null;
	}		
	
	$("#newselectBox").on('click', '.selectItem', function(){
		$("#newDeptName").val($(this).html());
		$("#newdeptId").val($(this).attr("id"));
		$("#newselectBox").hide()
	})
	
	$(document).click(function(){
		$("#newselectBox").hide()
	})
	
});
//处理提交
function handleDevTask(){ 
	var id = $("#reqFeatureId").val();
	var actualStartDate = $("#handlestartWork").val();
	var actualEndDate = $("#handleendWork").val();
	var actualWorkload = $("#handleactWorkload").val();
	$('#handleForm').data('bootstrapValidator').validate();
	if(!$('#handleForm').data('bootstrapValidator').isValid()){
		$('#handleDevTask').on('hide.bs.modal', function () {
			     $('#handleForm').bootstrapValidator('resetForm');
			});
		return ;
	}
	if(workStatus=="false"){ 
		layer.confirm('该任务下尚有未实施完成的工作任务，是否要确定处理完成？', {
			  btn: ['确定', '取消'] ,
			  icon:3
			}, function(index, layero){//确定
				$("#loading").css('display','block');
				$.ajax({
					url:"/devManage/devtask/handleDevTask",
					type:"post",
					dataType:"json",
					data:{
						"id":id,
						"startDate":actualStartDate,
						"endDate":actualEndDate,
						"actualWorkload":actualWorkload,
						"handleRemark":$("#handleRemark").val()
					},
					success: function(data){
						workStatus="";
						$("#loading").css('display','none');
						if(data.status == "success"){
							layer.alert('处理成功！', {icon: 1});
							pageInit();
			  				$("#handleDevTask").modal("hide"); 
						}else if (data.status == "2"){
							layer.alert('处理失败！！！', {icon: 2});
						}
					},
					error:function(){
			            $("#loading").css('display','none');
			            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
			        }
				});
			}, function(index){//取消
				$('#handleDevTask').on('hide.bs.modal', function () {
					     $('#handleForm').bootstrapValidator('resetForm');
					});
			});
		}else{
			$.ajax({
				url:"/devManage/devtask/handleDevTask",
				type:"post",
				dataType:"json",
				data:{
					"id":id,
					"startDate":actualStartDate,
					"endDate":actualEndDate,
					"actualWorkload":actualWorkload,
					"handleRemark":$("#handleRemark").val()
				},
				success: function(data){
					$("#loading").css('display','none');
					if(data.status == "success"){
						layer.alert('处理成功！', {icon: 1});
						pageInit();
		  				$("#handleDevTask").modal("hide"); 
					}else if (data.status == "2"){
						layer.alert('处理失败！！！', {icon: 2});
					}
				},
				error:function(){
		            $("#loading").css('display','none');
		            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
		        }
			});
		}
}
//处理弹出框
function handle(row){
	$('#handleDevTask').on('hide.bs.modal', function () {
		     $('#handleForm').bootstrapValidator('resetForm');
		});
	var id = row.id;
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/getOneDevTask",
		type:"post",
		dataType:"json",
		data:{
			"id":id
		},
		success: function(data) {
			$("#loading").css('display','none');
			$("#handleConnectDiv").empty();
			$("#handleBrotheDiv").empty();
			$("#handleRequirementDiv").hide();
			$("#handleDefectDiv").hide();
			$("#handleRequstNumberDiv").hide();
			$("#handledeptName").text('');
			$("#handlepreStartDate").text('');
			$("#handlepreEndDate").text('');
			$("#handlepreWorkload").text('');
			$("#handlehandSug").text('');
			$("#handlesystemName").text('');
			$("#handlerequirement").text('');
			$("#handleDefect").text('');
			$("#handleRequstNumber").text('');
			$("#handlestartWork").val("");
			$("#handleactWorkload").val("");
			$("#handleendWork").val("");
			$("#handleRemark").val("");
			$("#handlestartWork").val("");
			$("#handleendWork").val("");
			$("#handleactWorkload").val("");
			$("#reqFeatureId").val(data.id);
			$("#handledevTaskTitle").text( toStr(data.featureCode)+" | "+data.featureName );
			$("#handledevTaskOverview").text(data.featureOverview);
			var statusName = '';
			for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
                if(devtaskStatusList[i].valueCode == data.requirementFeatureStatus){
                	statusName=  devtaskStatusList[i].valueName;
             	   break;
                }
              }
			$("#handledevTaskStatus").text(statusName);
			$("#handledevManPost").text(data.manageUserName);
			$("#handleexecutor").text(data.executeUserName);
			$("#handlesystemName").text(data.systemName);
			if(data.requirementFeatureSource!=undefined){
				if(data.requirementFeatureSource==1){
					$("#handleRequirementDiv").show();
					$("#handleDefectDiv").hide();
					$("#handleRequstNumberDiv").hide();
					$("#handlerequirement").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
				}else if(data.requirementFeatureSource==2){
					$("#handleRequirementDiv").hide();
					$("#handleDefectDiv").hide();
					$("#handleRequstNumberDiv").show();
					$("#handleRequstNumber").text(data.questionNumber);
				}else if(data.requirementFeatureSource==3){
					$("#handleRequirementDiv").hide();
					$("#handleDefectDiv").show();
					$("#handleRequstNumberDiv").hide();
					var dftName='';
					for(var i=0;i<data.defectInfos.length;i++){
						if(data.defectInfos[i].requirementFeatureId == id){
							dftName+= data.defectInfos[i].defectCode+",";
						}
					}
					$("#handleDefect").text(dftName.substring(0,dftName.length-1));
				}
			}
			
			$("#handledeptName").text(data.deptName);
			
			$("#handlestartWork").val(data.actualStartDate==undefined ? data.minActStartDate : data.actualStartDate);
			$("#handleendWork").val(data.actualEndDate == undefined ? data.maxActEndDate : data.actualEndDate);
			$("#handleactWorkload").val(data.actualWorkload == undefined ? data.sumActWorkLoad : data.actualWorkload);
			
			var status = "";
			if(data.temporaryStatus =="1"){
				status = "是";
			}else if(data.temporaryStatus =="2"){
				status = "否";
			}
			$("#handletemporaryTask").text(status);//1是2否
			$("#handlepreStartDate").text(data.planStartDate);
			$("#handlepreEndDate").text(data.planEndDate);
			$("#handlepreWorkload").text(data.planWorkload);
			
			$("#handlehandSug").text(data.handleSuggestion);
			//下属工作任务
			workStatus ="";
			if(data.list.length>0){
				for(var i=0;i<data.list.length;i++){
					if(data.list[i].devtaskStatus!=3 && data.list[i].devtaskStatus!=0){
						workStatus = "false";
						break;
					}
				}
				for(var i=0;i<data.list.length;i++){
					$("#handleConnectDiv").append( '<div class="rowdiv"><div class="form-group col-md-2"><label class=" control-label font_left" ><a class="a_style" onclick="getSee('+data.list[i].id+')">'+toStr(data.list[i].devTaskCode)+'</a></label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+data.list[i].devTaskName+'</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+data.list[i].devtaskStatusName+'</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+toStr(data.list[i].devUsername)+'</label></div><div class="form-group col-md-3"><label class=" control-label font_left" >'+toStr(data.list[i].actualStartDate)+'~'+toStr(data.list[i].actualEndDate)+'</label></div><div class="form-group col-md-1"><label class=" control-label font_left" >'+toStr(data.list[i].actualWorkload)+'人天</label></div></div>');
				}
			}else{
				workStatus="true";
			}
			// 相关开发任务
			if(data.brother!=undefined){
				for(var i=0;i<data.brother.length;i++){
					$("#handleBrotheDiv").append( '<div class="rowdiv"><div class="form-group col-md-2"><label class=" control-label font_left" ><a class="a_style" onclick="showDevTask('+data.brother[i].id+')">'+toStr(data.brother[i].featureCode)+'</a></label></div><div class="form-group col-md-3"><label class=" control-label font_left" >'+data.brother[i].featureName+'</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+data.brother[i].statusName+'</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+toStr(data.brother[i].executeUserName)+'</label></div><div class="form-group col-md-3"><label class="col-sm-6 control-label  fontWeihgt">预排期：</label><label class="col-sm-6 control-label font_left fontWeihgt" >'+toStr(data.brother[i].windowName)+'</label></div></div>');
				}
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
    $("#handleDevTask").modal("show");
	
}
//新增提交
function addCommit(){
	console.log("dev_chck_add!!!")
	var featureName = $("#nfeatureName").val();
	var featureOverview = $("#nfeatureOverview").val();
	var manageUserId = $("#newdevManageUser").val();
	var executeUserId = $("#newexecuteUser").val();
	var systemId = $("#newsystemId").val();
	var requirementId = $("#newrequirementId").val();
	var deptId = $("#newdeptId").val();
	var planStartDate = $("#startWork").val();
	var planEndDate = $("#endWork").val();
	var estimateWorkload = $("#nestimateWorkload").val();
	var commissioningWindowId = $("#newcommitWindowId").val();
	var requirementFeatureSource = $("#newrequirementFeatiureSource").val();
	var questionNumber = $("#newquestion").val();
	var newdefectId = $("#newdefectId").val();
	$('#newform').data('bootstrapValidator').validate();
	if(!$('#newform').data('bootstrapValidator').isValid()){
		return;
	}
	var versionArr = $("#newsystemVersionBranch").val().split(",");
	var systemVersionId = versionArr[0];
	var systemScmBranch = versionArr[1];
	/*var _fileStr = [];
	for(var i=0;i<_files.length;i++){
		_fileStr.push(_files[i].fileNameOld);
	}
	var s = _fileStr.join(",")+",";
	for(var i=0;i<_fileStr.length;i++){
		if(s.replace(_fileStr[i]+",","").indexOf(_fileStr[i]+",")>-1){
			layer.alert(_fileStr[i]+"文件重复！！！", {icon: 0});
			return ;
		}
	}*/
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/addDevTask",
		dataType:"json",
		type:"post",
		data:{
			 "featureName" : featureName,
			 "featureOverview" : featureOverview,
			 "manageUserId" : manageUserId,
			 "executeUserId" :executeUserId,
			 "systemId" : systemId,
			 "requirementId" : requirementId,
			 "deptId" :deptId,
			 "startDate" : planStartDate,
			 "endDate" : planEndDate,
			 "estimateWorkload" : estimateWorkload,
			 "commissioningWindowId":commissioningWindowId,
			 "requirementFeatureSource":requirementFeatureSource,
			 "questionNumber":questionNumber,
			 "defectIds":newdefectId,
			 "systemVersionId":systemVersionId,
			 "systemScmBranch":systemScmBranch,
			 "requirementFeaturePriority":$("#newreqFeaturePriority").val(),
			 "sprintId":$("#newSprintId").val(),
			 "storyPoint":$("#newstoryPoint").val(),
			 "attachFiles" :$("#files").val()
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){
				layer.alert('保存成功！', {icon: 1});
				pageInit();
  				$("#newDevTask").modal("hide");
			}else if (data.status == "2"){
				layer.alert('保存失败！！！', {icon: 2});
			}else if(data.status =="repeat"){
				layer.alert('该任务名称已存在！！！', {icon: 0});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
}

//新增弹框
function newDevTask_btn(){
    console.log("new_add!!!");
	$('#newDevTask').on('hide.bs.modal', function () {
		     $('#newform').bootstrapValidator('resetForm');
		});
	$("#reqDiv").hide();
	$("#questDiv").hide();
	$("#dftDiv").hide();
	$("#newsprintDiv").hide();
	$("#newstoryPointDiv").hide();
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/toAddData",
		dataType:"json",
		type:"post",
		success:function(data){
			$("#loading").css('display','none');
			$("#nfeatureName").val("");
			$("#nfeatureOverview").val("");
			$("#startWork").val("");
			$("#endWork").val("");
			$("#nestimateWorkload").val("");
			$("#newquestionNumber").val("");
			$("#newsystemId").val('');
			$("#newsystemName").val('');
			$("#newdevManageUser").val('');
			$("#newdevManageUserName").val('');
			$("#newFileTable").empty();
			$("#newdevManageUser").val('');
			$("#newdevManageUser").val('');
			$("#newexecuteUser").val('');
			$("#newexecuteUserName").val('');
			$("#files").val('');
			$("#newdeptId").val('');
			$("#newDeptName").val('');
			$("#newrequirementId").val('');
			$("#newrequirementName").val('');
			$("#newcommitWindowId").empty();
			$("#newcommitWindowId").append('<option value="">请选择</option>');
			$("#newreqFeaturePriority").empty();
			$("#newreqFeaturePriority").append('<option value="">请选择</option>');
			$("#newrequirementFeatiureSource").empty();
			$("#newrequirementFeatiureSource").append('<option value="">请选择</option>');
			$("#newsystemVersionBranch").empty();
			$("#newsystemVersionBranch").append('<option value="">请选择</option>');
			$("#newSprintId").empty();
			$("#newSprintId").append('<option value="">请选择</option>');
			$("#newstoryPoint").val('');
		/*	if(data.depts!=null && data.depts!=undefined){
				for(var a=0;a<data.depts.length;a++){
					$("#newdeptId").append(' <option value="'+data.depts[a].id+'">'+data.depts[a].deptName+'</option>');
				}
			}*/
			if(reqFeaturePriorityList!=null){
				for (var i = 0,len = reqFeaturePriorityList.length;i < len;i++) {
	                $("#newreqFeaturePriority").append('<option value="'+reqFeaturePriorityList[i].value+'"> '+reqFeaturePriorityList[i].innerHTML+'</option>')
	             }
			}
			if(data.cmmitWindows!=null && data.cmmitWindows!=undefined){
				for(var c=0;c<data.cmmitWindows.length;c++){
					$("#newcommitWindowId").append(' <option value="'+data.cmmitWindows[c].id+'">'+data.cmmitWindows[c].windowName+'</option>');
				} 
			}
			if(data.dics!=null && data.dics!=undefined){
				for(var i=0;i<data.dics.length;i++){
					$("#newrequirementFeatiureSource").append('<option value="'+data.dics[i].valueCode+'">'+data.dics[i].valueName+'</option>');
				}
			}
			
			$('.selectpicker').selectpicker('refresh'); 
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
	modalType = 'new';
	$("#newDevTask").modal("show");
	_files=[];
	
}

/*function getManageUsers() {
	$("#loading").css('display','block');
    $.ajax({
        type: "post",
        url: "/system/user/getUserNoCon",
        dataType: "json",
        success: function(data) {
        	$("#loading").css('display','none');
        	 for(var i = 0; i < data.length; i++) {
                 var id = data[i].id;
                 var name = data[i].userName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#devManPost").append(opt);
        	 }
        	 $('.selectpicker').selectpicker('refresh'); 
        }
    });
}*/


/*function getSearchData() {
    $.ajax({
        type: "post",
        url: "/devManage/devtask/getSearchData",
        dataType: "json",
        success: function(data) {
            for(var i = 0; i < data.requirementInfos.length; i++) {
                var reqid = data.requirementInfos[i].id;
                var reqname = data.requirementInfos[i].requirementName;
                var opt = "<option value='" + reqid + "'>" + reqname + "</option>";
                $("#requirementId").append(opt);
            }
           
            for(var i = 0; i < data.systemInfos.length; i++) {
                var sysid = data.systemInfos[i].id;
                var sysname = data.systemInfos[i].systemName;
                var opt = "<option value='" + sysid + "'>" + sysname + "</option>";
                $("#systemId").append(opt);
            }

            for(var i = 0; i < data.windows.length; i++) {
                var windowId = data.windows[i].id;
                var windowName = data.windows[i].windowName;
                //var windowVersion = data.windows[i].windowVersion;
                var opt = "<option value='" + windowId + "'>" + windowName + "</option>";
                $("#planVersion").append(opt);
            }
            
            for(var i = 0; i < data.dataDic.length; i++) {
                var statusCode = data.dataDic[i].valueCode;
                var statusName = data.dataDic[i].valueName;
                var opt = "<option value='" + statusCode + "'>" + statusName + "</option>";
                $("#devTaskStatus").append(opt);
            }
            $('.selectpicker').selectpicker('refresh'); 

        }
    });
}*/
//重置
function clearSearch() {
    $('#devTaskCode').val("");
    $("#featureName").val("");
    $('#devManPost').val("");
    $('#devManPostName').val('');
    $('#execteUserName').val('');
    $('#execteUserId').val('');
   /* $("#devManPost").val(uid);
    $("#devManPostName").val(username);*/
    $("#systemNameReq").val('');
    $("#systemId").val('');
    $("#devTaskStatus").selectpicker('val', '');
    $("#reqFeaturePriority").selectpicker('val', '');
    $("#planVersion").val('');
    $("#planVersionName").val('');
    $("#requirementId").val('');
    $("#requirementName").val('');
    $("#systemId").val('');
    $("#sprintNames").val('');
    $("#sprintIds").val('');
    $("#createTye").selectpicker('val', '');
    $(".color1 .btn_clear").css("display","none");
}
//清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
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
//文件上传，并列表展示
function uploadFileList(){ 
	//列表展示
	$(".upload-files").change(function(){ 
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
		$("#loading",window.parent.document).css('display','block');
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
	        	$("#loading",window.parent.document).css('display','none');
	        },
	        error:function(){
	        	$("#loading",window.parent.document).css('display','none');
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

//文件下载
function download(that){
	var fileS3Bucket = $(that).children(".file-bucket").text();
	var fileS3Key = $(that).children(".file-s3Key").text();
	var fileNameOld = $(that).children("span").text();
	var url = encodeURI("/devManage/devtask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld);
	window.location.href = url;
	
}
//导出
function exportTask_btn(){
	var obj = {};
	 obj.featureCode = $.trim( $("#devTaskCode").val());
     obj.featureName =  $.trim( $("#featureName").val());
     obj.manageUserIds = $("#devManPost").val();
    // obj.requirementFeatureStatus = $("#devTaskStatus").val();
     var planVersion = $("#planVersion").val();
     obj.commissioningWindowIds = planVersion;
     obj.requirementIds = $("#requirementId").val();
     obj.systemIds = $("#systemId").val();
     obj.createType = $("#createTye").val();
     obj.executeUserIds = $("#execteUserId").val();
     obj.sprints = $("#sprintIds").val();
     var reqFeaturePriority = $("#reqFeaturePriority").val();
     if(reqFeaturePriority!=null && reqFeaturePriority!=''){
    	 reqFeaturePriority = reqFeaturePriority.join(",");
     }
     obj.reqFeaturePrioritys =reqFeaturePriority ;
     var devTaskStatus = $("#devTaskStatus").val();
 	if(devTaskStatus!=null && devTaskStatus!=''){
 		devTaskStatus = devTaskStatus.join(",");
 	}
 	obj.requirementFeatureStatus = devTaskStatus;
     var obj1 = JSON.stringify(obj);
     window.location.href = "/zuul/devManage/devtask/exportExcel?reqFeatue="+encodeURIComponent(obj1, 'utf-8');
	 
	
}

//点击调整排期按钮
function toWindow(){
	idArr=[];
	var idArr=$('#list2').jqGrid('getGridParam','selarrrow')
	if( idArr.length==0 ){
		layer.alert('请选择至少一个开发任务!',{
				 icon: 0,
		}) 
		return ;
	}else{
		//console.log(idArr);
		ids = idArr;
		$("#scheduling").modal("show");
		searchWindows();
		}
}

//调整排期弹窗
function searchWindows(){  
	$("#windowTable").bootstrapTable('destroy');
    $("#windowTable").bootstrapTable({  
    	url:"/devManage/devtask/selectWindows",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 5,
        pageList : [ 5, 10, 15],
        queryParams : function(params) {
             var param={ 
            		 windowName:$.trim($("#windowName").val()),
            		 pageNumber:params.pageNumber,
                     pageSize:params.pageSize, 
             }
            return param;
        },
        
        columns : [{
        	 field : "id",
             title : "", 
             width : 30,
             formatter : function ( value, rows ,index ){
            	 var row = JSON.stringify(rows).replace(/"/g, '&quot;');
            	 return "<input type='checkbox' onclick='onlyOneJob( "+row+",this )' class='onlyOneJob' />";
             }
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "windowName",
            title : "窗口名称",
            align : 'center'
        },{
            field : "windowDate",
            title : "投产日",
            align : 'center'
        }],
        onLoadSuccess:function(){
        	$("#scheduling").modal("show");
        },
        onLoadError : function() {

        }
    });
}
function onlyOneJob(row,This){  
	$("#windowTable .onlyOneJob").prop('checked',false);
	$(This).prop('checked',true);
	windowId = row.id;
	//console.log(windowId);
}
function clearSearchWindows(){
	$("#windowName").val("");
	searchWindows();
}

function relatedWindow(){
	var rows = [];
	var flag=0;
	$(".jobArea").empty();
	for(var i = 0; i<ids.length; i++){
		var rowData = $("#list2").jqGrid('getRowData',ids[i]);
		rows.push(rowData);
	}
	for(var i = 0; i<rows.length; i++){
		if(rows[i].windowName != ''){
			flag = 1;
		}
	}
	if( flag==0 ){
		confirmCommission();
		return;
	}
	for( var i=0;i<rows.length;i++ ){
		if( rows[i].windowName != ''  ){
			var str='';
			str+='<div class="oneJob">'+rows[i].featureCode+' | '+rows[i].featureName +' &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp'+rows[i].windowDate +'投产</div>';
			$(".jobArea").append( str );
		} 
	} 
	$("#confirmModel").modal("show");
}


//调整排期确定
function confirmCommission(){
	$.ajax({
		type:"post", 
		url:"/projectManage/commissioningWindow/relationRequirement",
		dataType:"json",
		data:{
			ids:ids,
			windowId : windowId,
		},
		success : function(data) {
			layer.alert('调整排期成功!',{
				 icon: 1,
			},function(index){
				$("#confirmModel").modal("hide"); 
				$("#scheduling").modal("hide"); 
				layer.close(index);
				$("#list2").trigger("reloadGrid");
			})  
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
}
// ===================批量修改==================================

function toAllUpdate(){
	$("#reqFeatureIds").val('');
	$("#updateexecuteUserId").val('');
	$("#updateexecuteUserName").val('');
	$("#updateSprintId").empty();
	$("#updateSprintId").append("<option value=''>请选择</option>");
	$("#updatedevTaskStatus").empty();
	$("#updatedevTaskStatus").append("<option value=''>请选择</option>");
	
	idArr=[];
	var idArr=$('#list2').jqGrid('getGridParam','selarrrow')
	if( idArr.length==0 ){
		layer.alert('请选择至少一个开发任务!',{
				 icon: 0,
		}) 
		return ;
	}else{
		var systemArr = [];
		for(var j=0;j<idArr.length;j++){
			systemArr.push( $("#list2").jqGrid('getRowData',idArr[j]).systemId);
		}
		
		var hash=[];
		  for (var i = 0; i < systemArr.length; i++) {
		     if(hash.indexOf(systemArr[i])==-1){
		      hash.push(systemArr[i]);
		     }
		  }
		 if(hash.length>1){
			 layer.alert('请选择属于同一系统的开发任务!',{
				 icon: 0,
			 });
			 return;
		 }else{
			 var reqFeatureIds =idArr;
			$("#reqFeatureIds").val(reqFeatureIds);
			for(var i=0;i<devtaskStatusList.length;i++){
				if(devtaskStatusList[i].valueCode!=""){
					$("#updatedevTaskStatus").append('<option value="'+devtaskStatusList[i].valueCode+'">'+devtaskStatusList[i].valueName+'</option>');
				}
			}
			
			getUpdateData(hash.join(''));
		
			$("#allUpdateModel").modal("show"); 
		 }
		
		
	}
}
function getUpdateData(systemId){
	$.ajax({
		url:"/devManage/displayboard/getSprintBySystemId",
		dataType:"json",
		type:"post",
		data:{systemId:systemId},
		success:function(data){
			if(data.sprintInfos!=undefined && data.sprintInfos.length>0){
				for(var i=0;i<data.sprintInfos.length;i++){
					$("#updateSprintId").append('<option value="'+data.sprintInfos[i].id+'">'+data.sprintInfos[i].sprintName+'</option>')
				}
			}
			$('.selectpicker').selectpicker('refresh'); 
		}
	
	})
}
function updateSprints(){
	
	if($("#updateSprintId").val()=='' && $("#updatedevTaskStatus").val()=='' && $("#updateexecuteUserId").val()==''){
		layer.alert('请至少填写其中一项！',{ icon: 0,});
		return;
	}
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/updateSprints",
		dataType:"json",
		type:"post",
		data:{
			ids: $("#reqFeatureIds").val(),
			sprintId: $("#updateSprintId").val(),
			devTaskStatus :$("#updatedevTaskStatus").val(),
			executeUserId:$("#updateexecuteUserId").val()
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status =="success"){
				layer.alert("修改成功！",{icon:1});
				$("#allUpdateModel").modal("hide");
				pageInit();
			}else{
				layer.alert("修改失败！",{icon:2});
			}
		}
	})
}


//==============================end================================
//查看页面查看附件
function showFile(){
	var reqFeatureId = $("#checktaskId").val();
	var requirementId =  $("#checkItcdReqId").val();
	
	if(requirementId== ''){
		layer.alert('该任务下没有附件！', {icon: 0});
		return;
	}
	/*window.open("http://10.1.13.105:7001/ITCDWeb/attachment/findAttachment?reqId="+13199+"&reqtaskId="+13199+"&taskreqFlag=requirement",'height=100, width=100');
 	window.location.href = "http://10.1.13.105:7001/ITCDWeb/attachment/findAttachment2?reqId="+13199+"&reqtaskId="+reqFeatureId+"&taskreqFlag=task"
	*/
	layer.open({
	  type: 2,
	  area: ['700px', '450px'],
	  fixed: false, //不固定
	  maxmin: true,
	  btnAlign: 'c',
	  title:"相关附件",
	  content: reqAttUrl+"?reqId="+htmlEncodeJQ(requirementId)+"&reqtaskId="+htmlEncodeJQ(reqFeatureId)+"&taskreqFlag=task"
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

//获取部署状态
function getDeployStatus(){
	$.ajax({
		url:"/devManage/devtask/getDeployStatus",
		method:"post", 
		dataType:"json",
  		success:function(data){  
  			deployStatusData = data.deployStatus;
        }, 
        error:function(){
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
}
//表单校验
$(function () {
    $('#newform').bootstrapValidator({
    	 excluded:[":disabled"],
　　　　message: 'This value is not valid',//通用的验证失败消息
        　feedbackIcons: {//根据验证结果显示的各种图标
            　　　　　　　　valid: 'glyphicon glyphicon-ok',
            　　　　　　　　invalid: 'glyphicon glyphicon-remove',
            　　　　　　　　validating: 'glyphicon glyphicon-refresh'
        　　　　　　　　   },
        fields: {
        	featureName: {
                validators: {
                    notEmpty: {
                        message: '名称不能为空'
                    },
                    stringLength: {
                        min:5,
                        max: 300,
                        message: '长度必须在5-300之间'
                    }
                }
            },
            featureOverview: {
                validators: {
                	 notEmpty: {
                         message: '描述不能为空'
                     },
                    
                }
            },
            nestimateWorkload:{
            	validators: {
               	 notEmpty: {
                        message: '工作量不能为空'
                    },
                    numeric: {
						   message: '只能输入数字'
	                	},
	                    regexp: {
	                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
	                	      message: '请输入大于0且小于等于500的正数'
	                	 },
               }
            },
            newstoryPoint:{
            	validators: {
                   numeric: {
					   message: '只能输入数字'
                	},
                    regexp: {
                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
                	      message: '请输入大于0且小于等于500的正数'
                	 },
                  }
            },
            newdevManageUserName:{
            	 trigger:"change",
            	validators: {
                  	 notEmpty: {
                           message: '管理岗不能为空'
                       },
                  }
            },
            newexecuteUserName:{
            	 trigger:"change",
            	validators: {
                  	 notEmpty: {
                           message: '执行人不能为空'
                       },
                  }
            },
            newsystemName:{
            	trigger:"change",
            	validators:{
            		notEmpty:{
            			message:"关联系统不能为空"
            		},
            	}
            }
            
        }
    });
    $("#editForm").bootstrapValidator({
    　　　　message: 'This value is not valid',//通用的验证失败消息
       　feedbackIcons: {//根据验证结果显示的各种图标
           　　　　　　　　valid: 'glyphicon glyphicon-ok',
           　　　　　　　　invalid: 'glyphicon glyphicon-remove',
           　　　　　　　　validating: 'glyphicon glyphicon-refresh'
       　　　　　　　　   },
       fields: {
    	   efeatureName: {
               validators: {
                   notEmpty: {
                       message: '名称不能为空'
                   },
                   stringLength: {
                       min:5,
                       max: 300,
                       message: '长度必须在5-300之间'
                   }
               }
           },
           efeatureOverview: {
               validators: {
               	 notEmpty: {
                        message: '描述不能为空'
                    },
               }
           },
           editdevManageUserName:{
           	validators: {
                 	 notEmpty: {
                          message: '管理岗不能为空'
                      },
                 }
           },
           editexecuteUserName:{
           	validators: {
                 	 notEmpty: {
                          message: '执行人不能为空'
                      },
                 }
           },
           editsystemName:{
        	   trigger:"change",
        	   validators:{
           		notEmpty:{
           			message:"关联系统不能为空"
           		},
           	}
        	   
           },
           eestimateWorkload:{
           	validators: {
              	 notEmpty: {
                       message: '工作量不能为空'
                   },
                   numeric: {
						   message: '只能输入数字'
	                	},
	                    regexp: {
	                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
	                	      message: '请输入大于0且小于等于500的正数'
	                	 },
        			}
              
           },
           eactualWorkload:{
        		validators: {
                      numeric: {
   						   message: '只能输入数字'
   	                	},
   	                    regexp: {
   	                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
   	                	      message: '请输入大于0且小于等于500的正数'
   	                	 },
           			}
           },
           editstoryPoint:{
           	validators: {
                  numeric: {
					   message: '只能输入数字'
               	},
                   regexp: {
               	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
               	      message: '请输入大于0且小于等于500的正数'
               	 },
              }
           },
       }
   });
    
    $("#splitForm").bootstrapValidator({
    	 excluded:[":disabled"],//关键配置1，表示只对于禁用域不进行验证，其他的表单元素都要验证
　　　　message: 'This value is not valid',//通用的验证失败消息
	   　feedbackIcons: {//根据验证结果显示的各种图标
	       　　　　　　　　valid: 'glyphicon glyphicon-ok',
	       　　　　　　　　invalid: 'glyphicon glyphicon-remove',
	       　　　　　　　　validating: 'glyphicon glyphicon-refresh'
	   　　　　　　　　   },
	   fields: {
		   sWorkSummary:{
			   trigger:"change",
          	 validators: {
              	 notEmpty: {
                       message: '摘要不能为空'
                   },
                   stringLength: {
                       min:5,
                       max: 300,
                       message: '长度必须在5-300之间'
                   }
              }
          },
          sWorkOverView:{
        	  trigger:"change",
          	 validators: {
              	 notEmpty: {
                       message: '描述不能为空'
                   },
                   stringLength: {
                       min: 0,
                       max: 500,
                       message: '长度必须小于500'
                   }
              }
          },
          startWork:{
          	validators: {
             	 notEmpty: {
                      message: '计划开始日期不能为空'
                  },
             }
          },
          endWork:{
          	validators: {
                	 notEmpty: {
                         message: '计划结束日期不能为空'
                     },
                }
          },
          sWorkPlanWorkload:{
        	  validators: {
             	 notEmpty: {
                      message: '预计工作量不能为空'
                  },
                  regexp: {
               	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
               	      message: '请输入大于0且小于等于500的正数'
               	 },
             }
          }
          
          
	   }
    });
  
    $("#handleForm").bootstrapValidator({
　　　　message: 'This value is not valid',//通用的验证失败消息
	   　feedbackIcons: {//根据验证结果显示的各种图标
	       　　　　　　　　valid: 'glyphicon glyphicon-ok',
	       　　　　　　　　invalid: 'glyphicon glyphicon-remove',
	       　　　　　　　　validating: 'glyphicon glyphicon-refresh'
	   　　　　　　　　   },
	   fields: {
		   startWork:{
	          	validators: {
	             	 notEmpty: {
	                      message: '实际开始日期不能为空'
	                  },
	             }
	          },
	          endWork:{
	          	validators: {
	                	 notEmpty: {
	                         message: '实际结束日期不能为空'
	                     },
	                }
	          },
		   handleactWorkload :{
			   validators: {
	             	 notEmpty: {
	                      message: '工作量不能为空'
	                  },
	                  numeric: {
  						   message: '只能输入数字'
  	                	},
	                  regexp: {
	                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
	                	      message: '请输入大于0且小于等于500的正数'
	                	 },
	             }
		   }
	   }
    });
});
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


//需求详情
function showRequirement() {
	var id=$("#reset_requirementName").attr("requirementId");
	var code=$("#reset_requirementCode").text();
	var name=$("#reset_requirementName").text();
	layer.open({
      type: 2,
      title:  '<i class="focusInfo" title="关注"  ></i><span>'+ code + ' | ' + name + '</span>',
      shadeClose: true,
      shade: false, 
      move: false,
      area: ['100%', '100%'], 
      tipsMore: true,
      anim: 2,
      content:  '/projectManageui/requirement/toRequirementDetail?requirementId='+id+',requirementParentId='+requirementParentId,
      btn: '关闭' ,
      btnAlign: 'c', //按钮居中
      yes: function(){
          layer.closeAll();
      }
    });
}
