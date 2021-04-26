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
var devtaskStatusList ='';// 状态
var excuteUserName='';
var deployStatusData =[];// 部署状态
var reqFeaturePriorityList = [];// 优先级
 
var editDevtaskStatus = '';

var system = {
    codeReviewStatus:2, // 系统评审状态 是否代码评审（1:是，2:否）
	codeReviewSubmitBtn:true, // 实施完成状态是否允许提交
    codeReviewDevTaskListMsg:"代码审核通过" // 需要实施完成的工作任务
};

$(function(){
	// getProjectGroup(); 
	select_project();
	banEnterSearch();
	uploadFileList(); 
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
    // findByStatus();
      
    // 时间控件 配置参数信息
      
   $("#eastartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
    	
    	$("#editForm").data('bootstrapValidator')
        .updateStatus("eaendWork", 'NOT_VALIDATED')
        .validateField("eaendWork");

    });
    $("#eaendWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
    	
    	$("#editForm").data('bootstrapValidator')
        .updateStatus("eastartWork", 'NOT_VALIDATED')
        .validateField("eastartWork");
    	
    }); 

    $("#epstartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
    	$("#editForm").data('bootstrapValidator')
        .updateStatus("ependWork", 'NOT_VALIDATED')
        .validateField("ependWork");
    });
    
    $("#ependWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
    	$("#editForm").data('bootstrapValidator')
        .updateStatus("epstartWork", 'NOT_VALIDATED')
        .validateField("epstartWork");
    });
     
      
  
    $("#editrequirementFeatureSource").change(function(){ 
    	if($("#editrequirementFeatureSource").val()==1){
    		$("#equestDiv").hide();
    		$("#ereqDiv").show();
//    		$("#edftDiv").hide();
    	}else if($("#editrequirementFeatureSource").val()==2){
    		$("#ereqDiv").hide();
    		$("#equestDiv").show();
//    		$("#edftDiv").hide();
    	}else if($("#editrequirementFeatureSource").val()==3){
//    		$("#ereqDiv").hide();
//    		$("#equestDiv").hide();
//    		$("#edftDiv").show();
    	}else if($("#editrequirementFeatureSource").val()==''){
//    		$("#ereqDiv").hide();
//    		$("#equestDiv").hide();
//    		$("#edftDiv").hide();
    	}    	
    });
    
    // 所有的Input标签，在输入值后出现清空的按钮
    $('input[type="text"]').parent().css("position","relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange",function(){
    	if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    }) 
//    $('.clear').parent().css("position","relative");
//    $('.clear').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
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
    
    //编辑系统名称change事件
    $("#editsystemName").change(function(){
    	var systemId = $("#editsystemId").val();
    	if(systemId!=null && systemId!=''){
    		$.ajax({
        		url:"/devManage/systemVersion/getSystemVersionByCon",
        		dataType:"json",
        		type:"post",
        		data:{
        			systemId:systemId,
        			status:1
        		},
        		success:function(data){
                    system.codeReviewStatus = "";
                    system.codeReviewStatus = data.system.codeReviewStatus;
					$("#editsystemVersionBranch").empty();
        			$("#editsystemVersionBranch").append("<option value=''>请选择</option>");
        			$("#editrepairSystemVersion").empty();
        			$("#editrepairSystemVersion").append("<option value=''>请选择</option>");
        			var systemVersionStr='';
        			var editrepairSystemVersionStr='';
	  				$.each(data.rows,function(index,value){
							systemVersionStr += '<option value="'+value.id+','+value.groupFlag+'">'+value.version+'--> '+value.groupFlag+'</option>';
							editrepairSystemVersionStr += '<option value="'+value.id+'">'+value.version+'</option>';
	  				});
	  				$("#editsystemVersionBranch").append(systemVersionStr);
	  				$("#editrepairSystemVersion").append(editrepairSystemVersionStr);
        			/*
					 * if(data.systemVersionBranchs.length>0){ for(var i=0; i<data.systemVersionBranchs.length;i++){
					 * if( data.systemVersionBranchs[i].scmBranch!=null &&
					 * data.systemVersionBranchs[i].systemVersionId!=undefined){
					 * $("#editsystemVersionBranch").append('<option
					 * value="'+data.systemVersionBranchs[i].systemVersionId+','+data.systemVersionBranchs[i].scmBranch+'">'+data.systemVersionBranchs[i].systemVersionName+'-->'+data.systemVersionBranchs[i].scmBranch+'</option>'); } } }
					 */
        			
        			$('.selectpicker').selectpicker('refresh'); 
        			
        		}
        	});
    		if($("#edevelopmentMode").val() != 2){// 关联的系统是敏态 可以选择选择冲刺
				 $.ajax({
					 url:"/devManage/devtask/getSprintBySystemId",
					 data:{
						 systemId:$("#editsystemId").val(),
						 projectId:$("#newProject_listId").val(),
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
				 // 故事点
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
				
			}else{    //1:敏态           2:稳态
				//系统是稳态    显示      待实施,“实施中”、“实施完成”、“取消”
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
						let valueName = devtaskStatusList[i].valueName;
						if(valueName == '待实施' || valueName == '实施中' ||  valueName == '实施完成' ||  valueName == '取消'){
							$("#editdevTaskStatus").append('<option '+flag+' value="'+devtaskStatusList[i].valueCode+'">'+devtaskStatusList[i].valueName+'</option>');
						}
//						$("#editdevTaskStatus").append('<option '+flag+' value="'+devtaskStatusList[i].valueCode+'">'+devtaskStatusList[i].valueName+'</option>');
					}
				}
//   				$("#editdevTaskStatus").attr("disabled",true);
			}
    	}
    	
    });
}); 

$(document).ready(function (){
	 var parameterArr={};   
    parameterArr.arr = window.location.href.split( "?" ); 
    parameterArr.parameterArr = parameterArr.arr[1].split( "," );
    parameterArr.obj={};
    for( var i = 0 ; i < parameterArr.parameterArr.length ; i++ ){
    	var obj = parameterArr.parameterArr[i].split( "=" );  
    	parameterArr.obj[ obj[0] ] = obj[1];
    } 
    edit(parameterArr.obj.rowId,parameterArr.obj.isAccept);

    //开发任务状态变更时，逻辑判断03实施完成
    $("#editdevTaskStatus").change(function(){
        // 需要评审
        if(system.codeReviewStatus == 1 && $("#editdevTaskStatus").val() == "03"){
            selectDevTaskByReqFeatureId(parameterArr.obj.rowId)
        } else if($("#editdevTaskStatus").val() == "03"){
        	system.codeReviewDevTaskListMsg="";
    		getDevNotOverByFeaureId(parameterArr.obj.rowId);
    		system.codeReviewSubmitBtn = true;
        }else {
        	
            system.codeReviewSubmitBtn = true;
		}
        
        if($("#check_Status").val() == 2 && $("#editdevTaskStatus").val() == "03"){
        	layer.alert('该开发任务需要先完成“用户验收”方可“完成实施”!', {
	            title: "提示信息",icon:2
	        });
        	$("#editdevTaskStatus").val('');
        	$("#editdevTaskStatus").selectpicker('refresh');
        }
    });
    
    //需求变化
    $("#editrequirementName").change(function(){
    	var systemId = $("#editsystemId").val();
    	var requirementId= $("#editrequirementId").val();
    	if(systemId!=""&&requirementId!=""){
    		getFeatureBySystemAndRequirement(systemId,requirementId);
    	}
    });
    //关联系统变化
    $("#editsystemName").change(function(){
    	var systemId = $("#editsystemId").val();
    	var requirementId= $("#editrequirementId").val();
    	if(systemId!=""&&requirementId!=""){
    		getFeatureBySystemAndRequirement(systemId,requirementId);
    	}
    });
});
//通过开发任务ID获取开发工作任务信息
function selectDevTaskByReqFeatureId(reqFeatureId){
	
    $("#loading").css('display','block');
    $.ajax({
        url:"/devManage/devtask/selectDevTaskByReqFeatureIds",
        dataType:"json",
        type:"post",
        data:{
            "ids":reqFeatureId
        },
        success:function(data){
            $("#loading").css('display','none');
            if(data.devTasks.length > 0){
                system.codeReviewSubmitBtn = false;
                system.codeReviewDevTaskListMsg = "<div>";
                for (var i = 0; i < data.devTasks.length; i++) {
                    system.codeReviewDevTaskListMsg += "<div><a class='a_style' href='#' onclick='parent.parent.toPageAndValue(\""+
                        			isValueNull(data.codeReViewManage.menuButtonName)+"\","
									+data.codeReViewManage.id+",\""
									+data.codeReViewManage.url+"?devTaskCode="+data.devTasks[i].devTaskCode+"\")'>"+data.devTasks[i].devTaskCode+"</a></div>";
                }
                system.codeReviewDevTaskListMsg += '未通过代码审核，请到“代码审核管理”菜单审核！</div>';
               /* layer.alert(system.codeReviewDevTaskListMsg, {
                    title: "提示信息",
                    closeBtn: 0,
                    btn:["取消"]
                });*/
            } else {
                system.codeReviewSubmitBtn = true;
                system.codeReviewDevTaskListMsg = "代码审核通过";
			}
            getDevNotOverByFeaureId(reqFeatureId);
        },
        error:errorFunMsg
    })
}
function getDevNotOverByFeaureId(reqFeatureId){
   
    $.ajax({
        url:"/devManage/devtask/getDevNotOverByFeaureId",
        dataType:"json",
        type:"post",
        data:{
            "featureId":reqFeatureId
        },
        success:function(data){
            if(data.devData.length > 0){
                if(system.codeReviewDevTaskListMsg!="代码审核通过"){
                	 system.codeReviewDevTaskListMsg += "<div>";
                	 for (var i = 0; i < data.devData.length; i++) {
                		 system.codeReviewDevTaskListMsg+= "<div>"+data.devData[i].devTaskCode+"</div>";
                     }
                     system.codeReviewDevTaskListMsg += '该开发任务下未实施完成的工作任务！</div>';
                     layer.alert(system.codeReviewDevTaskListMsg, {
                         title: "提示信息",
                         closeBtn: 0,
                         btn:["取消"]
                     });	
                }else {
                	system.codeReviewDevTaskListMsg = "<div>";
               	 for (var i = 0; i < data.devData.length; i++) {
               		 system.codeReviewDevTaskListMsg+= "<div>"+data.devData[i].devTaskCode+"</div>";
                    }
                    system.codeReviewDevTaskListMsg += '该开发任务下未实施完成的工作任务！</div>';
                    layer.alert(system.codeReviewDevTaskListMsg, {
                        title: "提示信息",
                        closeBtn: 0,
                        btn:["取消"]
                    });
                }
               
            }
        },
        error:errorFunMsg
    })
}
// 编辑提交
function editDevTask(){
    if(system.codeReviewSubmitBtn == false){
        layer.alert(system.codeReviewDevTaskListMsg, {
            title: "提示信息",
            closeBtn: 0,
            btn:["取消"]
        });
        return false;
	}
	var executeProjectGroupId=getCheckedZtree();
	var assetSystemTreeId=getModalCheckedZtree();
	var id = $("#editDevTaskId").val();
	var featureName = $("#efeatureName").val();
	var featureOverview = $("#efeatureOverview").val();
	var systemId = '';
	if($("#editsystemName").val() != ''){
		systemId = $("#editsystemId").val();
	}
	var deptId = $("#editdeptId").val();
	var executeUserId = $("#editexecuteUser").val();
	var manageUserId = $("#editdevManageUser").val();
	var planStartDate = $("#epstartWork").val();
	var planEndDate = $("#ependWork").val();
	var estimateWorkload= $("#eestimateWorkload").val();
	var actualStartDate = $("#eastartWork").val();
	var actualEndDate = $("#eaendWork").val();
	var actualWorkload = $("#eactualWorkload").val();
	var estimateRemainWorkload = $("#estimateRemainWorkload").val();   //预估剩余工作量(人天)
	var commissioningWindowId = $("#editcommitWindowId").val();
	var requirementFeatureSource = $("#editrequirementFeatureSource").val();
	//var questionNumber = $("#editquestionNumber").val();
	var sprintId = $("#editSprintId").val();
	var versionArr = $("#editsystemVersionBranch").val().split(",");
	var systemVersionId = versionArr[0];
	var systemScmBranch = versionArr[1];
	//var repairSystemVersionId = $("#repairVersionBranch").val();
	var repairSystemVersionId = $("#editrepairVersion").val();

    var requirementId= $("#editrequirementId").val();
//    var editDefectId;
//    if(requirementFeatureSource==1){
////        requirementId = $("#editrequirementId").val();
//    }else if(requirementFeatureSource == 2){
//        editDefectId = $("#editSynDefectId").val();
//    }else if(requirementFeatureSource == 3){
//        editDefectId = $("#editdefectId").val();
//    }

    // 判断如果任务状态为实施完成，那么则添加必填字段校验
    var editdevTaskStatus= $("#editdevTaskStatus").val();
    if (editdevTaskStatus=="03") {
    	var fields = {
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
	   };
    	$("#editForm").bootstrapValidator("addField","eastartWork",fields.startWork);
    	$("#editForm").bootstrapValidator("addField","eaendWork",fields.endWork);
    	$("#editForm").bootstrapValidator("addField","eactualWorkload",fields.handleactWorkload);
	}else {
		$("#editForm").bootstrapValidator("removeField","eastartWork");
    	$("#editForm").bootstrapValidator("removeField","eaendWork");
    	$("#editForm").bootstrapValidator("removeField","eactualWorkload");
    	$("#editForm").data('bootstrapValidator').destroy();
    	$('#editForm').data('bootstrapValidator',null);
    	formValidator();
	}


	$('#editForm').data('bootstrapValidator').validate();
	if(!$('#editForm').data('bootstrapValidator').isValid()){
		return;
	}
	
	var fieldTemplate = getFieldData( "editFieldDiv" ); 
	for( var i=0;i< fieldTemplate.field.length;i++ ){   
	 	if(  fieldTemplate.field[i].required == "false" ){
	 		if(  fieldTemplate.field[i].valueName == ""||  fieldTemplate.field[i].valueName == null|| fieldTemplate.field[i].valueName == undefined ){
	 			$("#loading").css('display','none');
	 			layer.alert( fieldTemplate.field[i].labelName+"不能为空", {
	                 icon: 2,
	                 title: "提示信息"
	             });
	     		 
	     		return;
	     	}
	 	}
	} 
	
	var _fileStr = [];
	for(var i=0;i<_editfiles.length;i++){
		_fileStr.push(_editfiles[i].fileNameOld);
	}
	var s = _fileStr.join(",")+",";
	for(var i=0;i<_fileStr.length;i++){
		if(s.replace(_fileStr[i]+",","").indexOf(_fileStr[i]+",")>-1){
			layer.alert(_fileStr[i]+"文件重复！！！", {icon: 0});
			return;
		}
	}
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
			"executeUserId":executeUserId,
			"manageUserId":manageUserId,
			"pstartDate": planStartDate,
			"pendDate": planEndDate,
			"estimateWorkload":estimateWorkload,
			"astartDate":actualStartDate,
			"aendDate":actualEndDate,
			"actualWorkload": actualWorkload,
			"estimateRemainWorkload": estimateRemainWorkload,
			"commissioningWindowId":commissioningWindowId,
//			"requirementFeatureSource":requirementFeatureSource,
			//"questionNumber":questionNumber,
            "requirementId": requirementId,
//			"defectIds":editDefectId,
			"systemVersionId":systemVersionId,
			"systemScmBranch":systemScmBranch,
			"repairSystemVersionId":repairSystemVersionId,
			"assetSystemTreeId":assetSystemTreeId,
			"requirementFeatureStatus":$("#editdevTaskStatus").val(),
			"attachFiles" :$("#editfiles").val(),
			"sprintId":sprintId,
			"storyPoint":$("#editstoryPoint").val(),
			"requirementFeaturePriority":$("#editreqFeaturePriority").val(),
			"createType":$("#createType2").val(),
			"executeProjectGroupId":executeProjectGroupId,
			"fieldTemplate":JSON.stringify(fieldTemplate),
			"projectPlanId" : $("#editProjectPlanId").val(),
			"projectId":$("#newProject_listId").val()

		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status=="success"){
                parent.pageInit();
                setTimeout(function (){
                    parent.layer.closeAll('iframe');
                    parent.layer.alert('编辑成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                }, 200);
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

// 编辑弹出框
function edit(id,isAccept){
	$('.is_new_project').show();
	$('.is_oac_project').show();
	$('#editDevTask').on('hide.bs.modal', function () {
	     $('#editForm').bootstrapValidator('resetForm');
	}); 
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
			$("#check_Status").val(data.checkStatus);
			if(isAccept && isAccept == 2 && data.checkStatus == 2){
				$("#CheckHandle_btn").show();
			}else{
				$("#CheckHandle_btn").hide();
			}

            system.codeReviewStatus = 2;
			system.codeReviewStatus = data.systemCodeReviewStatus;
			$("#createType").text('');
			if(data.createType == 1){
				$("#createType").text('自建任务');
			}else{
				$("#createType").text('同步任务');
			}
			
			if(data.projectType == 2){
				$('.is_new_project').hide();
				$('.is_oac_project').show();
			}else{
				$('.is_new_project').show();
				$('.is_oac_project').hide();
			}

			$("#edevelopmentMode").val('');
			$("#editSprintId").empty();
			$("#editSprintId").append('<option value="">请选择</option>');
			
			$("#editsystemName").val('');
			$("#editexecuteUser").val('');

			$("#newProject_listId").val('');
			$("#newProject_list").val('');

			$("#editsystemId").val('');
			$("#editrequirementName").val('');
			$("#dftReqFeatureId").val('');
			$("#editdefectCode").val('');
			$("#editdefectId").val('');
			$("#editSynDefectCode").val('');
			$("#editSynDefectId").val('');
			
			$("#editreqFeaturePriority").empty();
			$("#editreqFeaturePriority").append('<option value="">请选择</option>');
			$("#editrequirementId").val(data.requirementId);
			$("#editrequirementFeatureSource").empty();
			$("#editrequirementFeatureSource").append('<option value="">请选择</option>');
			// $("#editdevManageUser").empty();
			// $("#editdevManageUser").append('<option value="">请选择</option>');
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
			// $("#editcommitWindowId").empty();
			// $("#editcommitWindowId").append('<option value="">请选择</option>');
			$("#editrequirementId").empty();
			$("#editFileTable").empty();
			$("#editsystemVersionBranch").empty();
			$("#editrepairVersion").empty();
			$("#editFieldDiv").empty();
			 
			$("#editsystemVersionBranch").append("<option value=''>请选择</option>");
			$("#editrepairVersion").append("<option value=''>请选择</option>");
			
			$("#editdevTaskStatus").empty();
			$("#editdevTaskStatus").append("<option value=''>请选择</option>");

			$("#edevelopmentMode").val(data.developmentMode);
			$("#newProject_listId").val(data.projectId);
			$("#newProject_list").val(data.projectName);
			
			$("#editDevTaskId").val(data.id);
			$("#dftReqFeatureId").val(data.id);
			$("#efeatureName").val(data.featureName);
			$("#efeatureOverview").val(data.featureOverview);
			
			$("#epstartWork").val(data.planStartDate);  
			$("#ependWork").val(data.planEndDate); 
			
			$("#eestimateWorkload").val(data.planWorkload);
			$("#estimateRemainWorkload").val(data.estimateRemainWorkload);

			$("#eastartWork").val(data.actualStartDate); 
			$("#eaendWork").val(data.actualEndDate); 
			
			$("#eactualWorkload").val(data.actualWorkload);
			$("#editexecuteUser").val(data.executeUserId);
			$("#editsystemId").val(data.systemId);
			$("#editrequirementId").val(data.requirementId);
			//$("#editquestionNumber").val(data.questionNumber).change();
			$("#editdevManageUser").val(data.manageUserId);
			$("#editdeptId").val(data.deptId);
			$("#editDeptName").val(data.deptName).change();
			$("#editdevManageUserName").val(data.manageUserName);
			$("#editexecuteUserName").val(data.executeUserName);
			$("#editrequirementName").val(data.requirementCode);//.change()
			$("#editsystemName").val( data.systemName ); 
			$("#editcommitWindowId").val(data.commissioningWindowId);
			$("#editcommitWindowName").val(data.windowName).change();
			$("#createType2").val(data.createType);
            // 项目小组 
			checkedGroup(data.executeProjectGroupId,data.systemId,data.assetSystemTreeId,data.projectId);
			$("#editProjectPlanId").val('');
			$("#editProjectPlanId").val(data.projectPlanId);
			$("#editProjectPlanName").val('');
			$("#editProjectPlanName").val(data.projectPlanName);
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
			// 自定义字段
			var field = data.fields;
			if( field != undefined ){
		    	for( var i = 0;i < field.length;i++ ){
		        	appendDataType( field[i] , 'editFieldDiv' , 'edit' ); 
		        }
		    }
			 
			if($("#editsystemId").val()!=null && $("#editsystemId").val()!=''){
				$.ajax({
		    		url:"/devManage/systemVersion/getSystemVersionByCon",
		    		dataType:"json",
		    		type:"post",
		    		data:{
		    			systemId:$("#editsystemId").val(),
		    			status:1
		    		},
		    		success:function(data2){
		    			$("#editsystemVersionBranch").empty();
		    			$("#editsystemVersionBranch").append("<option value=''>请选择</option>");
		    			$("#editrepairVersion").empty();
		    			$("#editrepairVersion").append("<option value=''>请选择</option>");
		    			 
		    			var systemVersionStr='';
		    			var editrepairVersionStr=''; 
		    			
		  				$.each(data2.rows,function(index,value){
		  					var flag = '';
		  					var flag1 = '';
		  					if(data.systemScmBranch == value.groupFlag && data.systemVersionId == value.id){
	    						flag = 'selected';
	    					}
		  					 
		  					if( data.repairSystemVersionId == value.id ){
	    						flag1 = 'selected';
	    					}
							systemVersionStr += '<option '+flag+' value="'+value.id+','+value.groupFlag+'">'+value.version+'--> '+toStr(value.groupFlag)+'</option>';
							editrepairVersionStr += '<option '+flag1+' value="'+value.id+'">'+value.version+'</option>';
		  				});
		  				$("#editsystemVersionBranch").append(systemVersionStr);
		  				$("#editrepairVersion").append(editrepairVersionStr);
		    			
		    			$('.selectpicker').selectpicker('refresh'); 
		    			
		    		}
		    	});
				if(data.developmentMode == 1){    //1:敏态           2:稳态
					$.ajax({
						 url:"/devManage/devtask/getSprintBySystemId",
						 data:{
							 systemId:$("#editsystemId").val(),
							 projectId:$("#newProject_listId").val(),
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
				}else if(data.developmentMode == 2){    //1:敏态           2:稳态
					//系统是稳态    显示      待实施,“实施中”、“实施完成”、“取消”
					for(var i=0;i<devtaskStatusList.length;i++){
						if(devtaskStatusList[i].valueCode!=""){
							var flag = '';
							if(data.requirementFeatureStatus == devtaskStatusList[i].valueCode){
								flag = "selected";
							}
							let valueName = devtaskStatusList[i].valueName;
							if(valueName == '待实施' || valueName == '实施中' ||  valueName == '实施完成' ||  valueName == '取消'){
								$("#editdevTaskStatus").append('<option '+flag+' value="'+devtaskStatusList[i].valueCode+'">'+devtaskStatusList[i].valueName+'</option>');
							}
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
//					$("#editdevTaskStatus").attr("disabled",true);
				}
			}
			if(data.requirementFeatureSource == '1'){
				$("#equestDiv").hide();
	    		$("#ereqDiv").show();
//	    		$("#edftDiv").hide();
			}else if(data.requirementFeatureSource == '2'){
				$("#equestDiv").show();
	    		$("#ereqDiv").hide();
//	    		$("#edftDiv").hide();
			}else if(data.requirementFeatureSource =='3'){
//	    		$("#ereqDiv").hide();
//	    		$("#equestDiv").hide();
//	    		$("#edftDiv").show();
			}
			var dftName='';
			var dftId ='';
            var dftSynName='';
            var dftSynId ='';
//			if(data.defectInfos!=null && data.defectInfos!=undefined){
//				for(var i=0;i<data.defectInfos.length;i++){
//					if(data.defectInfos[i].requirementFeatureId == data.id){
//						if(data.defectInfos[i].createType==1){
//                            dftName+= data.defectInfos[i].defectCode+",";
//                            dftId += data.defectInfos[i].id+",";
//						}else{
//                            dftSynName+= data.defectInfos[i].defectCode+",";
//                            dftSynId += data.defectInfos[i].id+",";
//						}
//					}
//				}
//				$("#editdefectId").val(dftId);
//				$("#editdefectCode").val(dftName.substring(0,dftName.length-1)).change();
//                
//			}
//			$("#editSynDefectId").val(dftSynId);
            $("#editSynDefectCode").val(data.questionNumber);
			if(data.dics!=null && data.dics!=undefined){
				for(var i=0;i<data.dics.length;i++){
					var flag='';
					if(data.dics[i].valueCode == data.requirementFeatureSource){
						flag = "selected";
					}
					$("#editrequirementFeatureSource").append('<option '+flag+' value="'+data.dics[i].valueCode+'">'+data.dics[i].valueName+'</option>');
				}
			}
			
			
			// 附件
			_editfiles=[];
			if(data.attachements!=undefined && data.attachements!=null){
				if(data.attachements.length>0){
					var _table=$("#editFileTable");
					// var
					// _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
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
}
// 部门模糊搜索
$(function () {
	var loseInputMillsecs = 500;
	var clocker = null;
	// 编辑页面部门模糊搜索
	$("#editDeptName").on('input propertychange', function(){
		innerKeydown();		
	})
	
	function innerKeydown(){
		if(null == clocker){
			clocker = setTimeout(loadData,loseInputMillsecs);
			// console.info(clocker);
		}else{		// 连续击键，重新开始计时
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
	// 新增页面模糊搜索
	$("#newDeptName").on('input propertychange', function(){
		innerKeydown2();		
	})
	
	function innerKeydown2(){
		if(null == clocker){
			clocker = setTimeout(loadData2,loseInputMillsecs);
			// console.info(clocker);
		}else{		// 连续击键，重新开始计时
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
// 文件上传，并列表展示
function uploadFileList(){ 
	// 列表展示
	$(".upload-files").change(function(){ 
		var files = this.files;
		var formFile = new FormData();
		
		/*
		 * if(!fileAcceptBrowser()){ for(var i=0,len=files.length;i<len;i++){
		 * var file_type = files[i].name.split(".")[1];
		 * if(file_type!="doc"&&file_type!="docx"&&file_type!="xls"&&file_type!="xlsx"&&file_type!="txt"&&file_type!="pdf"){
		 * layer.alert('上传文件格式错误! 请上传后缀名为.doc，.docx，.xls，.xlsx，.txt，.pdf的文件',
		 * {icon:0}); return false; } } }
		 */
		
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
			// 读取文件
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
							// console.log("文件 "+file.name+" 读取成功！");
						}
					}; 
				})(i);   
			} 
			// 列表展示
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
		// 上传文件
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

// 清空上传文件
function clearUploadFile(idName){
	// $(idName).wrap('<form></form>');
	// $(idName).unwrap();
	$('#'+idName+'').val('');
}

// 移除上传文件
function delFile(that){
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text(); 
	$(that).parent().parent().remove();
	removeFile(fileS3Key);
}

// 移除暂存数组中的指定文件
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

// 文件下载
function download(that){
	var fileS3Bucket = $(that).children(".file-bucket").text();
	var fileS3Key = $(that).children(".file-s3Key").text();
	var fileNameOld = $(that).children("span").text();
	var url = encodeURI("/devManage/devtask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld);
	window.location.href = url;
	
}
  
 
function onlyOneJob(row,This){  
	$("#windowTable .onlyOneJob").prop('checked',false);
	$(This).prop('checked',true);
	windowId = row.id;
	// console.log(windowId);
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


// 调整排期确定
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

function formValidator(){
    $("#editForm").bootstrapValidator({
    　　　　message: 'This value is not valid',// 通用的验证失败消息
       　feedbackIcons: {// 根据验证结果显示的各种图标
           　　　　　　　　valid: 'glyphicon glyphicon-ok',
           　　　　　　　　invalid: 'glyphicon glyphicon-remove',
           　　　　　　　　validating: 'glyphicon glyphicon-refresh'
       　　　　　　　　   },
       fields: {
//    	   modalInput: {
//    		  trigger:"change",
//		   	  validators: {
//		       	 notEmpty: {
//		                message: '模块不能为空'
//		            },
//		       }
//		   },
    	   editProjectGroup: {
    		   trigger:"change",
               validators: {
               	 notEmpty: {
                        message: '项目小组不能为空'
                    },
                   
               }
           },
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
           editreqFeaturePriority:{
        	   trigger:"change",
        	   validators:{
           		notEmpty:{
           			message:"优先级不能为空"
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
					 newProject_list:{
						trigger:"change",
						validators:{
							notEmpty:{
								message:"关联项目不能为空"
							},
						}
					},
        // estimateRemainWorkload:{
        //     validators: {
        //         notEmpty: {
        //             message: '预估剩余工作量不能为空'
        //         },
        //         numeric: {
        //             message: '只能输入数字'
        //         },
        //         regexp: {
        //             regexp: /^500$|^[0](\.[\d]+)$|^([0-9]|[0-9]\d)(\.\d+)*$|^([0-9]|[0-9]\d|[1-4]\d\d)(\.\d+)*$/,
        //             message: '请输入0-500的数字'
        //         },
        //     }
        // },
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
           epstartWork:{
              	trigger:"change",
              	validators: {
              		notEmpty:{
						message:"预计开始时间不能为空"
					},
              		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                          message: '开始时间必须小于结束日期！',
                          callback:function(value, validator,$field){
                          	if( value == "" ){
                          		return true;
                          	}else{
                          		if( $( "#ependWork" ).val() == '' ){
                          			return true;
                          		}else{
                          			var start = new Date( value );
                          			var end = new Date( $( "#ependWork" ).val() );
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
              ependWork:{
              	trigger:"change",
              	validators: {
              		notEmpty:{
						message:"预计结束时间不能为空"
					},
              		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                          message: '结束时间必须大于开始日期！',
                          callback:function(value, validator,$field){
                          	if( value == "" ){
                          		return true;
                          	}else{
                          		if( $( "#epstartWork" ).val() == '' ){
                          			return true;
                          		}else{
                          			var start = new Date( $( "#epstartWork" ).val() );
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
              eastartWork:{
                 	trigger:"change",
                 	validators: {
                 		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                             message: '开始时间必须小于结束日期！',
                             callback:function(value, validator,$field){
                             	if( value == "" ){
                             		return true;
                             	}else{
                             		if( $( "#eaendWork" ).val() == '' ){
                             			return true;
                             		}else{
                             			var start = new Date( value );
                             			var end = new Date( $( "#eaendWork" ).val() );
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
                 eaendWork:{
                 	trigger:"change",
                 	validators: {
                 		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                             message: '结束时间必须大于开始日期！',
                             callback:function(value, validator,$field){
                             	if( value == "" ){
                             		return true;
                             	}else{
                             		if( $( "#eastartWork" ).val() == '' ){
                             			return true;
                             		}else{
                             			var start = new Date( $( "#eastartWork" ).val() );
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
             }
       }
   });
}
   
// 表单校验
$(function () { 

	formValidator();
    
    $("#splitForm").bootstrapValidator({
    	 excluded:[":disabled"],// 关键配置1，表示只对于禁用域不进行验证，其他的表单元素都要验证
　　　　message: 'This value is not valid',// 通用的验证失败消息
	   　feedbackIcons: {// 根据验证结果显示的各种图标
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
　　　　message: 'This value is not valid',// 通用的验证失败消息
	   　feedbackIcons: {// 根据验证结果显示的各种图标
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

// 获取开发任务状态
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


function getProjectGroup(){

	$.ajax({
		url:"/devManage/devtask/getProjectGroup",
		dataType:"json",
		type:"post",

		success:function(data){ 
			var znodes=JSON.parse( data.zNodes );  
			$.fn.zTree.init($("#projectOwn"),setting,znodes);
			var zTree = $.fn.zTree.getZTreeObj("projectOwn");   
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		}

	});

}

function checkedGroup(id,systemId,codeId,projectId) { 
	// 初始化
	$.ajax({
		url:"/devManage/devtask/getProjectGroupByProjectId",
		dataType:"json",
		data:{
			systemId:systemId
		},
		type:"post",
		success:function(data){
			if(data.zNodes!="") { 
				var znodes = JSON.parse(data.zNodes); 
				if (znodes == "") {
					$.fn.zTree.init($("#projectOwn"), setting, "");
				} else {
					$.fn.zTree.init($("#projectOwn"), setting, znodes); 
					var zTree = $.fn.zTree.getZTreeObj("projectOwn");
					var node = zTree.getNodeByParam("id", id); 
					zTree.cancelSelectedNode();
					zTree.selectNode(node, true); 
					if( node==null ){
						$("#projectInput").val('');
					}else{
						$("#projectInput").val(node.name);
					} 
				}
			} 
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		} 
	});   
	$.ajax({
		url:"/projectManage/systemTree/getSystemTreeByCode",
		dataType:"json",
		data:{
			"projectId":projectId
		},
		type:"post",
		success:function(data){  
			if(data.data!="") { 
				var znodes = data.data; 
				if (znodes == "") {
					$.fn.zTree.init($("#modalOwn"), modalSetting, "");
					$("#editForm").bootstrapValidator('removeField','modalInput');
				} else {
					$.fn.zTree.init($("#modalOwn"), modalSetting, znodes); 
					var zTree = $.fn.zTree.getZTreeObj("modalOwn");
					var node = zTree.getNodeByParam("id", codeId); 
					
					zTree.cancelSelectedNode();
					zTree.selectNode(node, true);  
					if( node==null ){
						$("#modalInput").val('').change();
					}else{ 
						var name=getAllParentName(node);
						$("#modalInput").val(name).change();
					} 
//					$("#editForm").bootstrapValidator("addField", "modalInput", {
//						  trigger:"change",
//					   	  validators: {
//					       	 notEmpty: {
//					                message: '模块不能为空'
//					            },
//					       }
//					}); 
				}
			} 
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		} 
	}); 
}

//
// function getProjectGropId(){
// var systemId=$("#editsystemId").val();
// $.ajax({
// url:"/devManage/devtask/getProjectGroupBySystemId",
// dataType:"json",
// data:{
// systemId:systemId
// },
// type:"post",
//
// success:function(data){
// $( "#projectInput" ).val( "" );
// var zTree = $.fn.zTree.getZTreeObj("projectOwn");
// zTree.cancelSelectedNode();
// if(data.size==1){
// var node = zTree.getNodeByParam("id",data.id);
// $( "#projectInput" ).val( node.name )
// zTree.selectNode(node,true);
// }
// },
// error:function(){
// layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
// }
//
// });
// return devtaskStatusList;
// }


function getProjectGropId(){
	$( "#projectInput" ).val("").change();
	$( "#modalInput" ).val("");
	var projectId=$("#newProject_listId").val();
	var systemCode=$("#editsystemId").attr( "systemCode"); 
	$.ajax({
		url:"/devManage/devtask/getProjectGroupByProjectId1",
		dataType:"json",
		data:{
			"projectId":projectId
		},
		type:"post", 
		success:function(data){  
            if(data.zNodes!="") {
				var znodes = JSON.parse(data.zNodes);

				if (znodes == "") {
					$.fn.zTree.init($("#projectOwn"), setting, "");
				} else {
					$.fn.zTree.init($("#projectOwn"), setting, znodes);

					if (znodes.length == 1) {
						var zTreea = $.fn.zTree.getZTreeObj("projectOwn");
						var node = zTreea.getNodeByParam("id", znodes[0].id);
						zTreea.selectNode(node, true);
						$("#projectInput").val(node.name).change();
					}

				}
			} 
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		}

	}); 
	$.ajax({
		url:"/projectManage/systemTree/getSystemTreeByCode",
		dataType:"json",
		data:{
			"projectId":$("#newProject_listId").val()
		},
		type:"post",
		success:function(data){  
			console.log( data )
			var znodes=data.data; 
			if(znodes==""){ 
				$("#editForm").bootstrapValidator('removeField','modalInput');
			}else { 
				$.fn.zTree.init($("#modalOwn"), modalSetting, znodes); 
				if(znodes.length==1){
					var zTreea = $.fn.zTree.getZTreeObj("modalOwn");
					var node = zTreea.getNodeByParam("id",znodes[0].id); 
					zTreea.selectNode(node,true);
					var name=getAllParentName(node);
					$("#modalInput").val(name).change();
				} 
//				$("#editForm").bootstrapValidator("addField", "modalInput", {
//					trigger:"change",  
//					validators: {
//				       	 notEmpty: {
//				                message: '模块不能为空'
//				            },
//				       }
//				}); 
			} 
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		}

	}); 
	return devtaskStatusList;
}

function appendDataType( thisData , id ,status){  
	var obj=$('<div class="form-group def_col_12"></div>');  
	switch ( thisData.type ) {
		case "int":
			obj.attr( "dataType","int");
			var labelName=$( '<div class="def_col_12">'+ thisData.label +'：</div>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_24"><input maxlength="'+ thisData.maxLength +'" fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="int" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input maxlength="'+ thisData.maxLength +'" fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="int" value="'+ thisData.valueName +'" /></div>' );
			}
			labelContent.children( " input " ).bind("keyup",function (){
				this.value=this.value.replace(/\D/gi,"");
			})
			obj.append( labelName , labelContent ); 
			break;
		case "float": 
			obj.attr( "dataType","float")
			var labelName=$( '<div class="def_col_12">'+ thisData.label +'：</div>' );
			if( status== "new" ){	
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="number" class="form-control" placeholder="请输入" name="float" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="number" class="form-control" placeholder="请输入" name="float" value="'+ thisData.valueName +'" /></div>' );
			}
			obj.append( labelName , labelContent );				
			break;
		case "varchar":
			obj.attr( "dataType","varchar") 
			var labelName=$( '<div class="def_col_12">'+ thisData.label +'：</div>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_24"><input  maxlength="'+ thisData.maxLength +'"  fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="varchar" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input  maxlength="'+ thisData.maxLength +'"  fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="varchar" value="'+ thisData.valueName +'" /></div>' );
			}
			obj.append( labelName , labelContent );		
			break;
		case "data":
			obj.attr( "dataType","data")  
			var labelName=$( '<div class="def_col_12">'+ thisData.label +'：</div>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="new'+ thisData.fieldName +'" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="edit'+ thisData.fieldName +'" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="'+ thisData.valueName +'" /></div>' ); 	
			}
			obj.append( labelName , labelContent );
			break;
		case "timestamp": 
			obj.attr( "dataType","timestamp")  
			var labelName=$( '<div class="def_col_12">'+ thisData.label +'：</div>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="new'+ thisData.fieldName +'" type="text" readonly id="new_'+ thisData.fieldName +'" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="edit'+ thisData.fieldName +'" type="text" readonly id="new_'+ thisData.fieldName +'" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="'+ thisData.valueName +'" /></div>' );
			}  
			obj.append( labelName , labelContent );
			break;
		case "enum": 
			obj.attr( "dataType","enum")
			var select=$( '<select class="selectpicker" requireded="'+ thisData.required +'" fName="'+ thisData.fieldName +'"></select>' )
			var options=JSON.parse(  thisData.enums  );
			select.append( '<option value=""  fName="'+ thisData.fieldName +'">请选择</option>'  );
			for( var i=0;i<options.length;i++ ){
				if( options[i].enumStatus == 1 ){ 
					if(  status== "edit" && options[i].value == thisData.valueName  ){
						select.append( '<option value="'+options[i].value+'" selected >'+options[i].value+'</option>'  );
					}else{
						select.append( '<option value="'+options[i].value+'">'+options[i].value+'</option>'  );
					}
					 
				}
			}  
			var labelName=$( '<div class="def_col_12">'+ thisData.label +'：</div>' );
			var labelContent=$( '<div class="def_col_24"></div>' );
			labelContent.append( select  );
			obj.append( labelName , labelContent );
			break;
		default:
			break;
	} 
	$( "#"+id ).append( obj );
	if( obj.attr( "dataType") == "data" ){ 
		 laydate.render({
	        elem: "#"+ obj.find( "input" )[0].id, 
	        trigger: 'click',
	        done:function(value,date,endDate){
	            $( "#"+ obj.find( "input" )[0].id ).next().css("display","block");
	        }
	    });
	}else if( obj.attr( "dataType") == "timestamp" ){ 
		 laydate.render({
		        elem: "#"+ obj.find( "input" )[0].id, 
		        trigger: 'click',
		        type: 'datetime', 
		        format: 'yyyy-MM-dd HH:mm:ss', 
		        done:function(value,date,endDate){
		            $( "#"+ obj.find( "input" )[0].id ).next().css("display","block");
		        }
		    });
		} 
	$(".selectpicker").selectpicker('refresh');
}
function getFieldData( id ){
	var data = {"field":[]};
	for( var i=0;i< $( "#"+ id +" > div" ).length;i++ ){ 
    	// int float varchar data timestamp enum
    	var obj={};
    	var type = $( "#"+ id +" > div" ).eq( i ).attr( "dataType" )
    	if( type == "int" || type == "float" || type == "varchar" || type == "data" || type == "timestamp" ){ 
    		obj.fieldName=$( "#"+ id +" > div" ).eq( i ).find( "input" ).attr( "fName" ); 
    		obj.required=$( "#"+ id +" > div" ).eq( i ).find( "input" ).attr( "requireded" );  
    		obj.valueName=$( "#"+ id +" > div" ).eq( i ).find( "input" ).val(); 
    		obj.labelName=$( "#"+ id +" > div" ).eq( i ).children("div").eq( 0 ).text(); 
    	}else if(  type == "enum"  ){ 
    		obj.fieldName=$( "#"+ id +" > div" ).eq( i ).find( "select" ).attr( "fName" );
    		obj.required=$( "#"+ id +" > div" ).eq( i ).find( "select" ).attr( "requireded" );  
    		obj.valueName=$( "#"+ id +" > div" ).eq( i ).find( "select" ).val();
    		obj.labelName=$( "#"+ id +" > div" ).eq( i ).children("div").eq( 0 ).text(); 
    	}
    	data.field.push( obj );
    } 
	return data;
}
function getCheckedZtree(){
	var zTree = $.fn.zTree.getZTreeObj("projectOwn");
	if(zTree!=null) {
		var nodes = zTree.getSelectedNodes();
		var id = "";
		for (var i = 0, l = nodes.length; i < l; i++) {
			id = nodes[i].id;   // 获取选中节点的id值
		}
	}else{
		id="";
	}
	return id;
}
function getModalCheckedZtree(){
	var zTree = $.fn.zTree.getZTreeObj("modalOwn");
	if(zTree!=null) {
		var nodes = zTree.getSelectedNodes();
		var id = "";
		for (var i = 0, l = nodes.length; i < l; i++) {
			id = nodes[i].id;   // 获取选中节点的id值
		}
	}else{
		id="";
	} 
	return id;
}

//查询系统和需求下有多少开发任务
function getFeatureBySystemAndRequirement(systemId,requirementId){
	
	$.ajax({
		url:"/devManage/devtask/getFeatureBySystemAndRequirement",
		dataType:"json",
		data:{
			"systemId":systemId,
			"requirementId":requirementId
		},
		type:"post",
		success: function (data) {
			 if(data.feature.length > 0){
                 var featureData = "<div>该系统和需求下已存在开发任务！";
                 var id = $("#editDevTaskId").val();
                 for (var i = 0; i < data.feature.length; i++) {
                	 if(id!=data.feature[i].id){
                		 featureData+= "<div>任务编号："+data.feature[i].featureCode+"</div>";
                	 }
                	
                 }
                 featureData += '</div>';
                 if(featureData!="<div>该系统和需求下已存在开发任务！</div>"){
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





