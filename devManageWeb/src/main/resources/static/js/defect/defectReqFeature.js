/**
 * Description: 延后解决并创建一个开发任务
 * Author:liushan
 * Date: 2019/1/10 下午 5:11
 */
var _icon_word ="../static/images/devtask/word.png";
var _icon_excel ="../static/images/devtask/excel.png";
var _icon_text = "../static/images/devtask/text.png";
var _icon_pdf = "../static/images/devtask/pdf.png";
var _files = [];
$(function(){
	var loseInputMillsecs = 500;
	var clocker = null;
	uploadFileList2();
    //时间控件 配置参数信息
    $("#reqFstartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
    $("#reqFendWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
   
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
	
	 $("#newsystemName2").change(function(){
	    	var systemId = $("#newsystemId").val();
	    	if(systemId!=null&& systemId!=''){
		    	$.ajax({
		    		url:"/devManage/devtask/getSystemVersionBranch",
		    		dataType:"json",
		    		type:"post",
		    		data:{
		    			systemId:systemId
		    		},
		    		success:function(data){
		    			$("#newsystemVersionBranch").empty();
		    			$("#newsystemVersionBranch").append("<option value=''>请选择</option>");
		    			if(data.systemVersionBranchs.length>0){
		    				for(var i=0; i<data.systemVersionBranchs.length;i++){
		    					if(data.systemVersionBranchs[i].scmBranch!=null&&data.systemVersionBranchs[i].systemVersionId!=undefined ){
			    					$("#newsystemVersionBranch").append('<option value="'+data.systemVersionBranchs[i].systemVersionId+','+data.systemVersionBranchs[i].scmBranch+'">'+data.systemVersionBranchs[i].systemVersionName+'-->'+data.systemVersionBranchs[i].scmBranch+'</option>');
			    				}
		    				}
		    			}
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
		   			}else{
		   				 $("#newsprintDiv").hide();
		   				 $("#newSprintId").empty();
		   			}
		    		}
		    	})
	    	}
	    });
});



//新增提交
function addCommit(){ 
	var featureName = $("#nfeatureName").val();
	var featureOverview = $("#nfeatureOverview").val();
	var manageUserId = $("#newdevManageUser").val();
	var executeUserId = $("#newexecuteUser").val();
	var systemId = $("#newsystemId").val();
	var deptId = $("#newdeptId").val();
	var planStartDate = $("#startWork").val();
	var planEndDate = $("#endWork").val();
	var estimateWorkload = $("#nestimateWorkload").val();
	var commissioningWindowId = $("#newcommitWindowId").val();
	$('#newform').data('bootstrapValidator').validate();
	if(!$('#newform').data('bootstrapValidator').isValid()){
		return;
	}
	var versionArr = $("#newsystemVersionBranch").val().split(",");
	var systemVersionId = versionArr[0];
	var systemScmBranch = versionArr[1];
	/*
	 * 
                
       remarkUploadFile(data.logId);
	
	*/
	var solution = $.trim($("#opt_solution").find("option:selected").val());
	var defect = {};
	defect.id = $("#opt_solution_defectId").val();
	defect.solveStatus = solution;
	defect.defectStatus = 8;
	defect.actualStartDate = $("#check_dev_task_startDate").val();
	defect.actualEndDate = $("#check_dev_task_endDate").val();
	defect = JSON.stringify(defect);
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
			 "deptId" :deptId,
			 "startDate" : planStartDate,
			 "endDate" : planEndDate,
			 "estimateWorkload" : estimateWorkload,
			 "commissioningWindowId":commissioningWindowId,
			 "requirementFeatureSource":3,
			 "defectIds":$("#newdefectId").val(),
			 "attachFiles" :$("#files").val(),
			 "requirementFeaturePriority":$("#newreqFeaturePriority").val(),
			 "sprintId":$("#newSprintId").val(),
			 "systemVersionId":systemVersionId,
			 "systemScmBranch":systemScmBranch,
			  dftJsonString : defect,
			  dftActualWorkload : $("#check_dev_task_workNum").val(),
			  defectRemark : htmlEncodeJQ($.trim($("#opt_defectRemark").val()))
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){
				// 创建一个开发任务后 需要修改缺陷的状态
                repairComplete(8);
			}else if (data.status == "2"){
				layer.alert('保存失败！！！', {icon: 2});
			}else if(data.status =="repeat"){
				layer.alert('该任务名称已存在！！！', {icon: 0});
			}

		}
	});
}
//新增弹框
function DelayRepairAndCreateDevTask(){
	$("#newsprintDiv").hide();
	$('#newDevTask').on('hide.bs.modal', function () {
		     $('#newform').bootstrapValidator('resetForm');
		});
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/toAddData",
		dataType:"json",
		type:"post",
		success:function(data){
			$("#loading").css('display','none');
			$("#ndevelopmentMode").val('');
			$("#newdeptId").val('');
			$("#newDeptName").val('');
			$("#nfeatureName").val("");
			$("#nfeatureOverview").val("");
			$("#startWork").val("");
			$("#endWork").val("");
			$("#nestimateWorkload").val("");
			$("#newquestionNumber").val("");
			$("#newsystemId").val( $("#opt_solution_systemId").val());
			$("#newsystemName2").val($("#opt_solution_systemName").val());
			$("#newdevManageUser").val('');
			$("#newdevManageUserName").val('');
			$("#newFileTable").empty();
			$("#newdevManageUser").val('');
			$("#newdevManageUser").val('');
			$("#newexecuteUser").val('');
			$("#newexecuteUserName").val('');
			$("#files").val('');
			var defectid = $("#opt_solution_defectId").val();
			$("#newdefectId").val(defectid);
			var defectCode = $("#opt_solution_defectCode").val();
			$("#newdefectCode2").val(defectCode);
			
			//$("#newdeptId").empty();
			//$("#newdeptId").append('<option value="">请选择</option>');
			$("#newrequirementId").val('');
			$("#newrequirementName").val('');
			$("#newcommitWindowId").empty();
			$("#newcommitWindowId").append('<option value="">请选择</option>');
			$("#newreqFeaturePriority").empty();
			$("#newreqFeaturePriority").append('<option value="">请选择</option>');
			$("#newSprintId").empty();
			$("#newSprintId").append('<option value="">请选择</option>');
			$("#newsystemVersionBranch").empty();
			$("#newsystemVersionBranch").append('<option value="">请选择</option>');

			
			if(data.reqFeaturePriorityList.length>0){
				for (var i = 0,len = data.reqFeaturePriorityList.length;i < len;i++) {
	                $("#newreqFeaturePriority").append('<option value="'+data.reqFeaturePriorityList[i].valueCode+'"> '+data.reqFeaturePriorityList[i].valueName+'</option>')
	             }
			}
			
			/*for(var a=0;a<data.depts.length;a++){
				$("#newdeptId").append(' <option value="'+data.depts[a].id+'">'+data.depts[a].deptName+'</option>');
			}*/
			/*for(var c=0;c<data.cmmitWindows.length;c++){
				$("#newcommitWindowId").append(' <option value="'+data.cmmitWindows[c].id+'">'+data.cmmitWindows[c].windowName+'</option>');
			} */
			
			$('.selectpicker').selectpicker('refresh'); 
		}
	});
	modalType = 'new';
	$("#newDevTask").modal("show");
	_files=[];
	
}
//文件上传，并列表展示
function uploadFileList2(){ 
	//列表展示
	$("#uploadFileReqF").change(function(){ 
		var files = this.files;
		var formFile = new FormData();
		
		if(!fileAcceptBrowser()){
			for(var i=0,len=files.length;i<len;i++){
				var file_type = files[i].name.split(".")[1];
				if(file_type!="doc"&&file_type!="docx"&&file_type!="xls"&&file_type!="xlsx"&&file_type!="txt"&&file_type!="pdf"){
					layer.alert('上传文件格式错误! 请上传后缀名为.doc，.docx，.xls，.xlsx，.txt，.pdf的文件', {icon:0});
					return false;
				}
			}
		}
		
		outer:for(var i=0,len=files.length;i<len;i++){
			var file = files[i];
			if(file.size<=0){
				layer.alert(file.name+"文件为空！", {icon: 0});
				continue;
			}
			var fileList = [];
    		if(modalType == 'new'){
    			fileList=_files;
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
	        url:'/zuul/devManage/devtask/uploadFile',
	        contentType: false,  
	        processData: false,
	        dataType: "json",
	        data:formFile,
	        success:function(data){ 
	        	for(var k=0,len=data.length;k<len;k++){
	        		if(modalType == 'new'){
	        			_files.push(data[k]); 
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
	        		clearUploadFile2('uploadFileReqF');
	        	}
	        }
	    });
	});
}

//清空上传文件
function clearUploadFile2(idName){
	//$(idName).wrap('<form></form>');
	//$(idName).unwrap();
	$('#'+idName+'').val('');
}

//移除上传文件
function delFile2(that){
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text(); 
	$(that).parent().parent().remove();
	removeFile2(fileS3Key);
}

//移除暂存数组中的指定文件
function removeFile2(fileS3Key){
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
		
	}
}
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
                     stringLength: {
                         min: 0,
                         max: 500,
                         message: '长度必须小于500'
                     }
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
	                	      message: '请输入大于0的正数'
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
            }
            
        }
    });
});