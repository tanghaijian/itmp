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


var thisInputVal;

var editDevtaskStatus = '';

var project_Id = '';
$(function(){
	select_project();
	 banEnterSearch();
	uploadFileList();
	getDeployStatus();
   // devtaskStatusList = $("#devTaskStatus").find("option");
    devtaskStatusList = getReqFeatureStatus();
    reqFeaturePriorityList = $("#reqFeaturePriority").find("option");
    $("#devManPost").val(uid);
    $("#devManPostName").val(username);
    //搜索展开和收起
    downOrUpButton();
    
    if(devtaskStatusList!=null){
    	$.each(devtaskStatusList,function(index,value){
    		$("#devTaskStatus").append('<option value="'+value.valueCode+'">'+value.valueName+'</option>')
    	})
    }
    findByStatus(); 
    //时间控件 配置参数信息
    
    addDateChoose();
    
    /*$("#sWorkStart").datetimepicker({
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
    dataControl("sWorkStart","sWorkEndStart");*/
   
 
    //所有的Input标签，在输入值后出现清空的按钮
    $('input[type="text"]').parent().css("position","relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange",function(){
    	if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    }) 
  //   $('.clear').parent().css("position","relative");
  //   $('.clear').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $(".clear").change(function(){
	    if( $(this).val()!="" ){
	 	   $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    });
    
    $('.clear').bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    }) 
    
    //新增开发任务时，任务来源触发事件
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
    
    //编辑开发任务时，任务来源变化触发事件
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
    
    //新增开发任务时，关联系统变化时触发事件
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
    
    //编辑开发任务时，系统变化触发事件
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
});
$(document).ready(function (){
	 var parameterArr={};   //url中的参数
   parameterArr.arr = window.location.href.split( "?" ); 
   parameterArr.parameterArr = parameterArr.arr[1].split( "," );
   parameterArr.obj={};
   for( var i = 0 ; i < parameterArr.parameterArr.length ; i++ ){
   	var obj = parameterArr.parameterArr[i].split( "=" );  
   	parameterArr.obj[ obj[0] ] = obj[1];
   }  
   split( parameterArr.obj.rowId , parameterArr.obj.systemId ); 
})
 

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
//获取同需求下其他开发任务的投产窗口，在设置投产窗口时进行提示：与该任务属于同需求下的开发任务的投产窗口不一致
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

     
//拆分提交
function splitCommit(){
	$('#splitForm').data('bootstrapValidator').validate();
	if(!$('#splitForm').data('bootstrapValidator').isValid()){
		return;
	} 
	
	var reqFeatureId = $("#splitDevtaskId").val();
	var commissioningWindowId = $("#splitCommitWindowId").val();
	var requirementFeatureStatus = $("#splitStatus").val();
	var devTasksArr = []; 
	
	for( var i = 0 ; i < $( "#works .onedatas" ).length ; i++ ){
		var element = $( "#works .onedatas" ).eq( i ); 
		var obj = {
			"devTaskName":element.find( ".sWorkSummary" ).val(),
			"devTaskOverview":element.find( ".sWorkOverView" ).val(),
			"planStartDate":new Date( element.find( ".sWorkStart" ).val() ), 
			"planEndDate":new Date( element.find( ".sWorkEnd" ).val() ), 
			"planWorkload":element.find( ".sWorkPlanWorkload" ).val(),
			"devUserId":element.find( ".sWorkDivid" ).val(),  
			"devTaskPriority":element.find( ".splitDevTaskPriority" ).eq(1).val(),
			"devTaskType":element.find( ".splitDevTaskType" ).eq(1).val(),
            "sprintId":$("#ssprintId").val(),
		} 
		devTasksArr.push( obj );
	}
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/splitDevTasks",
		type:"post",
		dataType:"json",
		data:{
			"id":reqFeatureId,
			"commissioningWindowId":commissioningWindowId,
			"requirementFeatureStatus":requirementFeatureStatus,
			"sprintId":$("#ssprintId").val(),
			"devTasks": JSON.stringify( devTasksArr ),
			"projectId":project_Id,
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){
				layer.alert('拆分成功！', {
					icon: 1, 
					yes: function(index, layero){
						window.parent.pageInit();
						window.parent.layer.closeAll();
					},
					cancel: function(index, layero){ 
						window.parent.pageInit();
						window.parent.layer.closeAll();
					}
				});  
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
function sameReq( This ){
	var summary = $("#splitSummary").text();
	var workname = '';
	if((summary.indexOf('|'))==-1){
		workname = summary.slice(0);
	}else{
		workname = summary.slice(summary.indexOf('|')+2);//正常是+1  +2是因为|后还有个空格
	}
    $(This).parents(".onedatas").find(".sWorkSummary").eq(0).val(workname).change();
    $(This).parents(".onedatas").find(".sWorkOverView").eq(0).val($("#sOverView").text()).change();
}
 
//拆分弹框
function split( id , systemId  ){    
	$("#sWorkEndStart").unbind("change");
	$("#sWorkStart").unbind("change");
	$("#sWorkPlanWorkload").unbind("change"); 
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
			project_Id = data.projectId;
			$("#ssprintId").val(data.sprintId);
			$("#ssprintName").text(data.sprintName);
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
	})
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
    $("#splitForm").bootstrapValidator({
    	 excluded:[":disabled"],//关键配置1，表示只对于禁用域不进行验证，其他的表单元素都要验证
　　　　message: 'This value is not valid',//通用的验证失败消息
	   　feedbackIcons: {//根据验证结果显示的各种图标
	       　　　　　　　　valid: 'glyphicon glyphicon-ok',
	       　　　　　　　　invalid: 'glyphicon glyphicon-remove',
	       　　　　　　　　validating: 'glyphicon glyphicon-refresh'
	   　　　　　　　　   },
	   fields: {
		   sWorkDividUserName:{
			   trigger:"change",
			   validators: {
             	 notEmpty: {
                      message: '执行人不能为空'
                  },
                  
               }
           },
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
          splitDevTaskPriority:{
			   trigger:"change",
			   validators: {
             	 notEmpty: {
                      message: '优先级不能为空'
                  },
                  
               }
         },
           splitDevTaskType:{
               trigger:"change",
               validators: {
                   notEmpty: {
                       message: '任务类型不能为空'
                   },
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
        	  trigger:"change",
          	validators: {
             	 notEmpty: {
                      message: '计划开始日期不能为空'
                  },
                  callback:{/*自定义，可以在这里与其他输入项联动校验*/
                      message: '开始时间必须小于结束日期！',
                      callback:function(value, validator,$field){ 
                      	if( value == "" ){
                      		return true;
                      	}else{
                      		if( $field.parent().parent().next().find( "input" ).val() == '' ){
                      			return true;
                      		}else{
                      			var start = new Date( value );
                      			var end = new Date( $field.parent().parent().next().find( "input" ).val() );
                      			if( start.getTime() > end.getTime() ){
                      				return false;
                      			} 
                      			return true;
                      		}
                      	}
                      }
          		}
             }
          },
          endWork:{
        	  trigger:"change",
          	validators: {
                	 notEmpty: {
                         message: '计划结束日期不能为空'
                     },
                     callback:{/*自定义，可以在这里与其他输入项联动校验*/
                         message: '结束日期必须大于开始日期！',
                         callback:function(value, validator,$field){ 
                         	if( value == "" ){
                         		return true;
                         	}else{
                         		if( $field.parent().parent().prev().find( "input" ).val() == '' ){
                         			return true;
                         		}else{
                         			var start = new Date($field.parent().parent().prev().find( "input" ).val() );
                         			var end = new Date( value );
                         			if( start.getTime() > end.getTime() ){
                         				return false;
                         			} 
                         			return true;
                         		}
                         	}
                         }
             		}
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


function addDataEvents( This ){
	thisInputVal = This; 
	
	$("#userbutton").attr("data-user", "other");
	$("#deptName").empty();
	$("#companyName").empty();
	 
   	$("#userModalreqF").modal("show");
   	systemId = $("#splitSystemId").val();
   	excuteUserName='';
	 clearSearchUser();
	 userTableShow2();
}

var workTaskIndex = 1;
function addWorkJob(){
	var option = $("#splitDevTaskPriority").find("option"); 
	var splitDevTaskType = $("#splitDevTaskType").find("option");
	var str = '<div class="onedatas canEdit">'+
	    '<div class="del_icon" onclick="delWorks(this)"> <span class="fa fa-times"></span> </div>'+
		'<div class="rowdiv">'+
			'<div class="form-group def_col_36">'+
				'<label class="def_col_4 control-label fontWeihgt"><span class="redStar">*</span>任务摘要：</label>'+
				'<div class="def_col_28"><input type="text" class="form-control sWorkSummary" placeholder="请输入" name="sWorkSummary" /></div>'+
				'<button type="button" onclick="sameReq(this)" class=" btn btn-primary" style="margin-left: 20px;">同任务</button>'+
			'</div>'+
		'</div>'+
		'<div class="rowdiv">'+
			'<div class="form-group def_col_36">'+
				'<label class="def_col_4 control-label fontWeihgt"><span class="redStar">*</span>任务描述：</label>'+
				'<div class="def_col_32">'+
					'<textarea class="form-control sWorkOverView" placeholder="请输入" name="sWorkOverView"></textarea>'+
				'</div>'+
			'</div>'+
		'</div>'+ 
		'<div class="rowdiv">'+
			'<div class="form-group def_col_12">'+
				'<label class="def_col_12 control-label fontWeihgt"><span class="redStar">*</span>计划开始：</label>'+
				'<div class="def_col_24">'+
					'<input size="16" type="text" readonly class="form-control pointStyle readonly form_datetime sWorkStart" readonly placeholder="请选择" name="startWork" />'+
					'<span onclick="clearCon(this)" class="btn_clear"></span>'+
				'</div>'+
			'</div>'+
			'<div class="form-group def_col_12">'+
				'<label class="def_col_12 control-label fontWeihgt"><span class="redStar">*</span>计划结束：</label>'+
				'<div class="def_col_24">'+
					'<input size="16" type="text" readonly class="form-control pointStyle readonly form_datetime sWorkEnd" readonly placeholder="请选择" name="endWork" />'+
					'<span onclick="clearCon(this)" class="btn_clear"></span>'+
				'</div>'+
			'</div>'+
			'<div class="form-group def_col_12">'+
				'<label class="def_col_19  control-label fontWeihgt"><span class="redStar">*</span>预计工作量(人天)：</label>'+
				'<div class="def_col_17 "><input type="text" class="form-control sWorkPlanWorkload" placeholder="请输入" name="sWorkPlanWorkload" /></div> '+
			'</div>'+
		'</div>'+
		'<div class="rowdiv ss">'+
			'<div class="form-group def_col_12">'+
				'<label class="def_col_12 control-label fontWeihgt"><span class="redStar">*</span>任务分配：</label>'+
				'<div class="def_col_24 chanceMyself">'+
					'<input class="sWorkDivid" type="hidden" id="sWorkDivid_'+workTaskIndex+'"/>'+
					'<input type="text" onclick="searchUserName(this)" id="sWorkDividUserName_'+workTaskIndex+'" placeholder="请输入关键字搜索" class="form-control def_col_20 sWorkDividUserName" name="sWorkDividUserName" />'+
					'<div class="def_col_16"><button type="button" class="btn btn-primary chanceMyselfBtm" onclick = "selectMyself(this)">选自己</button></div>'+
				'</div>'+
			'</div>'+ 
			'<div class="form-group def_col_12">'+
				'<label class="def_col_12 control-label fontWeihgt">优先级：</label>'+
				'<div class="def_col_24 chanceMyself">'+
					'<select class="selectpicker splitDevTaskPriority" name="splitDevTaskPriority">';
					 for( var i = 0; i < option.length ; i++ ){
						 str+='<option value="'+ option[i].value +'">'+ option[i].text +'</option>';
					 }
					 str+='</select>'+
				'</div>'+
			'</div>'+
			 '<div class="form-group def_col_12">'+
				 '<label class="def_col_12 control-label fontWeihgt"><span class="redStar">*</span>任务类型：</label>'+
				 '<div class="def_col_24 chanceMyself">'+
					 '<select class="selectpicker splitDevTaskType" name="splitDevTaskType">';
						for( var i = 0; i < splitDevTaskType.length ; i++ ){
							str+='<option value="'+ splitDevTaskType[i].value +'">'+ splitDevTaskType[i].text +'</option>';
						}
    					str+='</select>'+
        		'</div>'+
        	'</div>'+
        '</div>'+
	'</div>';
	$( "#works" ).append( str );
	workTaskIndex++;
	$('.selectpicker').selectpicker('refresh');
	$('input[type="text"]').parent().css("position","relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange",function(){
    	if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    }) 
	addDateChoose(); 
	addValidator();
}
function addDateChoose(){ 
	$(".sWorkStart").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left",
    }).on('changeDate',function(e) {   
    	 $( e.currentTarget ).change(); 
    	 $( this ).parent().parent().next().find("input").change(); 
    	 $( this ).parent().find('.btn_clear').show();
    }); 
    $(".sWorkEnd").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('changeDate',function(e) {
        $( e.currentTarget ).change();  
		$( this ).parent().parent().prev().find("input").change(); 
		$( this ).parent().find('.btn_clear').show();
    });   
}
 
function delWorks( This ){
	$( This ).parents(".onedatas").remove();
	addValidator()
}
function addValidator(){
	$("#splitForm").bootstrapValidator('removeField','sWorkSummary');
	$("#splitForm").bootstrapValidator('removeField','sWorkOverView');
	$("#splitForm").bootstrapValidator('removeField','startWork');
	$("#splitForm").bootstrapValidator('removeField','endWork');
	$("#splitForm").bootstrapValidator('removeField','sWorkPlanWorkload');
	$("#splitForm").bootstrapValidator('removeField','splitDevTaskType');

	$("#splitForm").bootstrapValidator("addField", "sWorkDividUserName", {
		  trigger:"change",
	   	  validators: {
	       	 notEmpty: {
	                message: '执行人不能为空'
	            },
	       }
	}); 
	$("#splitForm").bootstrapValidator("addField", "sWorkSummary", {
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
	}); 
	$("#splitForm").bootstrapValidator("addField", "sWorkOverView", {
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
	}); 
	$("#splitForm").bootstrapValidator("addField", "startWork", {
		trigger:"change",
	   	validators: {
      	   notEmpty: {
               message: '计划开始日期不能为空'
           },
           callback:{/*自定义，可以在这里与其他输入项联动校验*/
               message: '开始时间必须小于结束日期！',
               callback:function(value, validator,$field){ 
               	if( value == "" ){
               		return true;
               	}else{
               		if( $field.parent().parent().next().find( "input" ).val() == '' ){
               			return true;
               		}else{
               			var start = new Date( value );
               			var end = new Date( $field.parent().parent().next().find( "input" ).val() );
               			if( start.getTime() > end.getTime() ){
               				return false;
               			} 
               			return true;
               		}
               	}
            }
   		 }
	    }
	});
	$("#splitForm").bootstrapValidator("addField", "endWork", {
		trigger:"change",
	   	validators: {
         	 notEmpty: {
                  message: '计划结束日期不能为空'
             },
             callback:{/*自定义，可以在这里与其他输入项联动校验*/
                 message: '结束日期必须大于开始日期！',
                 callback:function(value, validator,$field){ 
                 	if( value == "" ){
                 		return true;
                 	}else{
                 		if( $field.parent().parent().prev().find( "input" ).val() == '' ){
                 			return true;
                 		}else{
                 			var start = new Date($field.parent().parent().prev().find( "input" ).val() );
                 			var end = new Date( value );
                 			if( start.getTime() > end.getTime() ){
                 				return false;
                 			} 
                 			return true;
                 		}
                 	}
                 }
     		}
        }
	});
    $("#splitForm").bootstrapValidator("addField", "sWorkPlanWorkload", {
        validators: {
            notEmpty: {
                message: '预计工作量不能为空'
            },
            regexp: {
                regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
                message: '请输入大于0且小于等于500的正数'
            },
        }
    });
	$("#splitForm").bootstrapValidator("addField", "splitDevTaskType", {
		validators: {
      		notEmpty: {
            	message: '任务类型不能为空'
            }
    	}
	});
} 
function clearCon( that ){
	 $(that).parent().children('input').val("").change();
}