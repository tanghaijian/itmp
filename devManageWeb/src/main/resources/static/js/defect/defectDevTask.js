/**
 * Description:  确认缺陷并创建一个工作任务
 * Author:liushan
 * Date: 2019/1/10 下午 5:10
 */
var _icon_word ="../static/images/devtask/word.png";
var _icon_excel ="../static/images/devtask/excel.png";
var _icon_text = "../static/images/devtask/text.png";
var _icon_pdf = "../static/images/devtask/pdf.png";
var _addworkFile = [];
var workModalType="";
var is_check_Collect_flag = true;//是否继续收藏
var collection_obj = {
		  menuUrl : '../devManageui/defect/toDefect',
		  filterName : '',
		  search : [],
		  isCollect : false
		};
$(function(){
	addWork_uploadList();
	  //时间控件 配置参数信息
    laydate.render({
        elem: '#startWork'
    });
    laydate.render({
        elem: '#endWork'
    });

    // 上传文件
    $("#checkuploadFile").change(function(){
        var files = this.files;
        var formFile = new FormData();
        outer:for(var i=0,len=files.length;i<len;i++){
            var file = files[i];
            if(file.size<=0){
                layer.alert(file.name+"文件为空！", {icon: 0});
                continue;
            }

            var fileList = [];
            var oldFileList = [];
            if(workModalType == 'new'){
                fileList=_addworkFile;
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
            formFile.append("files", file);
            //读取文件
            if (window.FileReader) {
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onerror = function(e) {
                    layer.alert("文件" + file.name +" 读取出现错误", {
                        icon: 2,
                        title: "提示信息"
                    });
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
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delworkFile(this)">删除</a></td>';
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
                    if(modalType == 'check'){
                        _checkfiles.push(data[k]);
                    }
                    $(".file-upload-tb span").each(function(o){
                        if($(this).text() == data[k].fileNameOld){
                            //$(this).parent().children(".file-url").html(data[k].filePath);
                            $(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
                            $(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
                        }
                    });
                }
                if(modalType == 'check'){
                    $("#checkfiles").val(JSON.stringify(_checkfiles));
                    clearUploadFile('checkuploadFile');
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
    });
    other();
    collect_handle();
    // 收藏
    getCollection(); 
  
	
});

// 显示
function tShowWithinManPopup(This){
	var ele_id =$(This).attr('id');
	var id =$(This).prev().val();
	withinUserShow(id);
	cleanUser();
	$("#userbutton").data("ipt",ele_id);
	$("#withinUserModal").modal("show");
}

//人员弹窗选择人员点击确定后触发，将选择的结果赋值到主页面上
function addUserID(){
	var ele = $("#userbutton").data("ipt");
    var rowData = $("#withinUserTable").bootstrapTable('getSelections')[0];
    if(typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        })
    }else {
    	$("#" + ele).prev().val(rowData.USER_ID).change();
		$("#" + ele).val(rowData.USER_NAME).change();
		$("#withinUserModal").modal("hide");
    }
}

//清除人员搜索信息
function cleanUser(){
    $("#withinUserName").val("");
    $("#withinDeptName").selectpicker('val', '');
    $("#withinCompanyName").selectpicker('val', '');

}

//添加
function addWorkTask(){ 
	//生产问题 传          缺陷id,缺陷名称,缺陷描述,
	var defectId=$('#dev_checkDefectID').val();
	var systemId=$("#dev_systemId").val();
	var defectSummary=$("#dev_check_defectSummary").html();
	var defectOverview=$("#dev_check_defectOverview").html();
	var check_status = $("[name='devCheckStatus']:checked").val();//验收标记        2待验收、1无需验收，默认“待验证”
	var files=$("#addfiles").val();
	// 所需的缺陷ID
	var defectIdWithDevTask  = $("#dev_checkDefectID").val();
	var obj={};
	obj.requirementFeatureId = $("#Attribute").attr("featureCode");  //关联开发任务
	obj.defectID =defectIdWithDevTask;
	obj.devTaskName=$("#ataskName").val();//任务名称
	obj.devTaskPriority=$("#devTaskPriority").val();//优先级
	obj.devTaskOverview=$("#taskOverview").val();//任务描述
	obj.planStartDate=$("#startWork").val();//预计开始时间
	obj.planEndDate=$("#endWork").val();//预计结束时间
	obj.planWorkload=$("#workLoad").val();//预计工作量
	obj.devUserId=$("#new_taskUserId").val();//任务分配
	obj.commissioningWindowId= $("#Attribute").attr("commissioningWindowId");
	obj.requirementFeatureStatus=$("#Attribute").attr("requirementFeatureStatus");
	var objStr=JSON.stringify(obj); 
	$('#new_WorkTaskForm').data('bootstrapValidator').validate();
	if(!$('#new_WorkTaskForm').data('bootstrapValidator').isValid()){
		return;
	}
	
	var sub_url = '/devManage/worktask/addDevTask';
	var sub_params = {
			objStr,
	 		"attachFiles":files
		}
	if($('#dev_defectSource').val() == 5){
		var devTasksArr = []; 
		
		for( var i = 0 ; i < $( "#works .onedatas" ).length ; i++ ){
			var element = $( "#works .onedatas" ).eq( i ); 
			var obj1 = {
                requirementFeatureId : $("#Attribute").attr("featureCode"),
                defectID :defectIdWithDevTask,
				"devTaskName":element.find( ".sWorkSummary1" ).val(),
				"devTaskOverview":element.find( ".sWorkOverView1" ).val(),
				"planStartDate":element.find( ".sWorkStart1" ).val(), 
				"planEndDate":element.find( ".sWorkEnd1" ).val(), 
				"planWorkload":element.find( ".sWorkPlanWorkload1" ).val(),
				"devUserId":element.find( ".sWorkDivid1" ).val(),  
				"devTaskPriority":element.find( ".splitDevTaskPriority1" ).eq(1).val(),
                commissioningWindowId :$("#Attribute").attr("commissioningWindowId"),
                requirementFeatureStatus:$("#Attribute").attr("requirementFeatureStatus"),
			} 
			devTasksArr.push( obj1 );
		}
        devTasksArr.push( obj );
		sub_url = '/devManage/worktask/addDevTask1';
		sub_params = {
			"defect":JSON.stringify({
				id:$('#dev_checkDefectID').val(),
				systemId:$("#dev_systemId").val(),
                defectCode:$("#dev_check_defectCode").text(),
				defectSummary:$("#dev_check_defectSummary").html(),
				defectOverview:$("#dev_check_defectOverview").html(),
				checkStatus : check_status,
			}),
			"workTask":JSON.stringify(devTasksArr),
	 		"attachFiles":files
		}
		
	}
	$("#loading").css('display','block');
 $.ajax({
	 type:"post",
 	 url:sub_url,
 	//contentType: "application/json; charset=utf-8",
 	data:sub_params,
 	success : function(data) {
        if (data.status == 2){
            layer.alert(data.errorMessage, {
                icon: 2,
                title: "提示信息"
            });
        } else if(data.status == 1){
            //affirm(defectIdWithDevTask);
            layer.alert('操作成功！', {
                icon: 1,
                title: "提示信息"
            });
            initTable();
            $("#rejectDiv").modal("hide");
            $("#new_WorkTaskModal").modal("hide");
        }
        $("#loading").css('display','none');
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

//显示开发任务弹窗
function showAdPopup(){
	clearstatus();
	DevPopup();
	$("#DevPopup").modal("show");
}

//清空搜索内容
function clearstatus() {
    $('#RelationCode').val("");
    $('#RelationName').val("");
    $("#PoStatus").selectpicker('val', '');
    $(".color1 .btn_clear").css("display","none");
}

//确认缺陷并创建工作任务中关联开发任务弹框查询
function DevPopup(){
	var featureStatusList=$("#PoStatus").find("option");
    var systemId=$("#dev_systemId").val();
    var requirementCode=$("#dev_requirementCode").val();
	$("#loading").css('display','block');
    $("#devPopupTable").bootstrapTable('destroy');
    $("#devPopupTable").bootstrapTable({  
    	url:"/devManage/worktask/getAllFeature1",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
        singleSelect : true,//单选
        queryParams : function(params) {
             var param={
                systemId:systemId,
                requirementCode:requirementCode,
            	featureName:$.trim($("#RelationName").val()),
            	featureCode:$.trim($("#RelationCode").val()),
            	FeatureStatus:$.trim($("#PoStatus").val()),
                pageNumber:params.pageNumber,
                pageSize:params.pageSize, 
             }
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px"
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "FEATURE_CODE",
            title : "任务编码",
            align : 'center'
        },{
            field : "COMMISSIONING_WINDOW_ID",
            visible : false,
            align : 'center'
        },{
            field : "FEATURE_NAME",
            title : "任务名称",
            align : 'center'
        },{
            field : "PLAN_END_DATE",
            visible : false,
            align : 'center'
        },{
            field : "PLAN_START_DATE",
            visible : false,
            align : 'center'
        },{
            field : "ESTIMATE_WORKLOAD",
            visible : false,
            align : 'center'
        },{
        	field : "REQUIREMENT_FEATURE_STATUS",
        	title : "任务状态",
        	align : 'center',
        	formatter : function(value,rows, index) {
        		for (var i = 0,len = featureStatusList.length;i < len;i++) {
        			if(rows.REQUIREMENT_FEATURE_STATUS == featureStatusList[i].value){
        				var _status = "<input type='hidden' id='list_featureStatusList' value='"+featureStatusList[i].innerHTML+"'>";
                        return featureStatusList[i].innerHTML+_status
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
            });
        }
    });
}
//赋值开发任务
function addPopup(){
	var rowData = $("#devPopupTable").bootstrapTable('getSelections')[0];
    if(typeof(rowData) == 'undefined') {
        layer.alert("请选择一条数据",{
            icon:2,
            title:"提示信息"
        })
    } else {

        $("#featureCode").val(rowData.FEATURE_NAME).change();
        $("#Attribute").attr("featureCode",rowData.ID);
        $("#Attribute").attr("commissioningWindowId",rowData.COMMISSIONING_WINDOW_ID); 
        $("#Attribute").attr("requirementFeatureStatus",rowData.REQUIREMENT_FEATURE_STATUS);
        if(rowData.PLAN_START_DATE!=undefined){
      	  $("#startWork").val(rowData.PLAN_START_DATE);
        }else{
        	$("#startWork").val("");
        }
        if(rowData.ESTIMATE_WORKLOAD!=undefined){
        	$("#workLoad").val(rowData.ESTIMATE_WORKLOAD).change();
        }else{
        	$("#workLoad").val("");
        }
      if(rowData.PLAN_END_DATE!=undefined){
    	  $("#endWork").val(rowData.PLAN_END_DATE);
      }else{
      	 $("#endWork").val("");
      }
        $("#DevPopup").modal("hide");
    }
	
}

//移除上传文件
function delworkFile(that,id){
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text(); 
	$.ajax({
        type:'post',
        url:'/devManage/worktask/delFile',
        data:{
        		id:id
        	 },
        success:function(data){
        	if(data.status=="success"){
        		layer.alert('删除成功！', {
                    icon: 1,
                    title: "提示信息"
                });
        	}else if(data.fail == "fail"){
        		layer.alert('删除失败！', {
                    icon: 1,
                    title: "提示信息"
                });
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
	$(that).parent().parent().remove();
	removeFile(fileS3Key);
}
//移除暂存数组中的指定文件
function removeFile(fileS3Key){
	if(workModalType == "new"){
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
			_addworkFile=[];
			$("#addfiles").val(JSON.stringify(files));
		}
		
	}
}

//初始化附件上传功能
function addWork_uploadList(){
	$("#AddworkUploadFile").change(function(){
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
            if(workModalType == 'new'){
                fileList=_addworkFile;
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
            formFile.append("files", file);
            //读取文件
            if (window.FileReader) {
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onerror = function(e) {
                    layer.alert("文件" + file.name +" 读取出现错误", {
                        icon: 2,
                        title: "提示信息"
                    });
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
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delworkFile(this)">删除</a></td>';
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
                    if(workModalType == 'new'){
                        _addworkFile.push(data[k]);
                    }
                    $(".file-upload-tb span").each(function(o){
                        if($(this).text() == data[k].fileNameOld){
                            //$(this).parent().children(".file-url").html(data[k].filePath);
                            $(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
                            $(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
                        }
                    });
                }
                if(workModalType == 'new'){
                    $("#addfiles").val(JSON.stringify(_addworkFile));
                    clearUploadFile('AddworkUploadFile');
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
    });
}

//清空上传文件
function clearUploadFile(idName){
	$('#'+idName+'').val('');
}

//内人员弹窗
function withinUserShow(notWithUserID){
    $("#loading").css('display','block');
    $("#withinUserTable").bootstrapTable('destroy');
    $("#withinUserTable").bootstrapTable({
        url:"/system/user/selectById",
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        singleSelect : true,//单选
        queryParams : function(params) {
        	   var param = {
   	                pageNumber:params.pageNumber,
   	                pageSize:params.pageSize,
   	                userName:$.trim($("#withinUserName").val()),
   	                deptId:$.trim($("#withinDeptName").find("option:selected").val()),
   	                companyId:$.trim($("#withinCompanyName").find("option:selected").val()),
   	                notWithUserID:notWithUserID
   	            };
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px"
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "USER_NAME",
            title : "姓名",
            align : 'center'
        },{
            field : "COMPANY_NAME",
            title : "所属公司",
            align : 'center'
        },{
            field : "DEPT_NAME",
            title : "所属部门",
            align : 'center'
        }],
        onLoadSuccess:function(){
            $("#loading").css('display','none');
        },
        onLoadError : function() {
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });

}

//人员弹框中初始化部门查询下拉框
function getDevDept() {
    $("#withinDeptName").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: "/system/user/getDept",
        dataType: "json",
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].deptName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#withinDeptName").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
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
//人员弹框中初始化公司查询下拉框
function getDevCompany() {
    $("#withinCompanyName").append("<option value=''>请选择</option>");
    $.ajax({
        type: "post",
        url: "/system/user/getCompany",
        dataType: "json",
        success: function(data) {
            for(var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].companyName;
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#withinCompanyName").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
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

//搜索框 收藏按钮控制 js 部分
function collect_handle() {
  $(".collection").click(function () {
	  collection_obj.search = [];
    if ($(this).children("span").hasClass("fa-heart-o")) {
      collection_obj.search.push(
        { "type": "input", "value": { "defectCode": $("#defectCode").val()},"isData": _is_null("defectCode") },
        { "type": "input", "value": { "defectSummary": $("#defectSummary").val()}, "isData":_is_null("defectSummary") },
        { "type": "input", "value": { "submitUserId": $("#submitUserId").val()}, "isData":_is_null("submitUserId") },
        { "type": "input", "value": { "submitUserName": $("#submitUserName").val()}, "isData":_is_null("submitUserName") },
        { "type": "input", "value": { "submitDate": $("#submitDate").val()}, "isData":_is_null("submitDate") },
        { "type": "select", "value": { "defectStatus": change_str('defectStatus')}, "isData":_is_null("defectStatus") },
        { "type": "select", "value": { "defectSource": change_str('defectSource')}, "isData":_is_null("defectSource") },
        { "type": "select", "value": { "severityLevel": change_str('severityLevel')}, "isData":_is_null("severityLevel") },
        {"type": "select", "value": { "emergencyLevel": change_str('emergencyLevel')}, "isData":_is_null("emergencyLevel") },
        { "type": "input", "value": { "windowName": $("#windowName").val()}, "isData":_is_null("windowName") },
        { "type": "input", "value": { "windowId": $("#windowId").val()}, "isData":_is_null("windowId") },
        { "type": "input", "value": { "requirementCode": $("#requirementCode").val()}, "isData":_is_null("requirementCode") }, 
        { "type": "input", "value": { "requirementName": $("#requirementName").val()}, "isData":_is_null("requirementName") },
        { "type": "input", "value": { "sel_systemId": $("#sel_systemId").val()}, "isData":_is_null("sel_systemId") }, 
        { "type": "input", "value": { "systemName": $("#systemName").val()}, "isData":_is_null("systemName") },
        { "type": "select", "value": { "defectType": change_str('defectType')}, "isData":_is_null("defectType") },
        { "type": "input", "value": { "sel_assingUserId": $("#sel_assingUserId").val()}, "isData":_is_null("sel_assingUserId") },
        { "type": "input", "value": { "sel_assingUserName": $("#sel_assingUserName").val()},"isData": _is_null("sel_assingUserName")}
      )
      var isResult = collection_obj.search.some(function (item) {
        return item.isData
      })
      collection_obj.isCollect = isResult;
      if(isResult){
    	  $('#collect_Name').val('');
          $('#collect_Modal').modal('show');
      }
    } else {
      layer.confirm('确定要取消收藏吗?', {btn: ['确定', '取消'],icon:0, title: "提示"}, function () {
        collection_obj.isCollect = false;
        collect_submit();
      })
    }
  })
}
//收藏提交
function collect_submit(){
  var sub_data = {};
  var sub_url = 'collectCondition';
  var is_name = true;
  if(collection_obj.isCollect){
    if(!$('#collect_Name').val()){
      is_name = false;
    }
    sub_data = {
      'menuUrl': collection_obj.menuUrl,
      'filterName': $('#collect_Name').val(),
      'defectReport': JSON.stringify(collection_obj.search),
    }
  }else{
    sub_data = { id : $("#projectType2").val()};
    sub_url = 'updateDefectReport';
  };
  if(!is_name) {
    layer.alert('请填写方案名称!', {
      icon: 0,
    })
    return;
  };
  $("#loading").css('display', 'block');
  $.ajax({
    type: "post",
    url: "/report/defectReport/" + sub_url,
    dataType: "json",
    data: sub_data,
    success: function (data) {
        if(collection_obj.isCollect){
          if (data.code == 1) {
	          $(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
	          layer.alert('收藏成功!', {
	            icon: 1,
	          })
	          $('#collect_Modal').modal('hide');
          }
        }else{
        	reset();
	      $(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
	      layer.alert('已取消收藏!', {
	        icon: 1,
	      })
	    }
        getCollection();
        $("#loading").css('display', 'none');
    },
    error: function () {
      $("#loading").css('display', 'none');
      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
    }
  });
}
function _is_null(ele){
	  return $('#'+ele).val() ? true : false;
	}
//初始化查询条件收藏方案下拉框
function getCollection() {
	  $("#projectType2").empty();
	  $("#projectType2").append('<option class="defaultSelect" value="">选择收藏方案</option>');
	  $("#loading").css('display','block');
	  $.ajax({
	    url:"/report/defectReport/selectDefectReportList",
	    dataType:'json',
	    type:'post',
	    data:{
	      menuUrl:'../devManageui/defect/toDefect',
	    },
	    success : function(data){
	      if(data.length){
	        data.map(v=>{
	        	if(v.lastUseFlag==1){
	        		 $("#projectType2 .defaultSelect").after(`<option value="${v.id}">${v.filterName}</option>`); 
	    		  }else{
	    			  $("#projectType2").append(`<option value="${v.id}">${v.filterName}</option>`);  
	    		  }
	        })
	      }
	      $("#projectType2").selectpicker('refresh');
	      $("#loading").css('display','none');
	    },
	    error:function(){
	      $("#loading").css('display','none');
	      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	    }
	  })
	}
//切换收藏
function tab_option(This){
	reset();
  var _id = $(This).val();
  if(!_id) {
	  $(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
	  return;
  };
  is_check_Collect_flag = true;
  $("#loading").css('display','block');
  $.ajax({
    url:"/report/defectReport/selectDefectReportById",
    dataType:'json',
    type:'post',
    data:{
    "menuUrl": "../devManageui/defect/toDefect",
     id:_id,
    },
    success : function(data){
      var msg = JSON.parse(data.favoriteContent);
      if(msg){
    	for(var i=0;i<msg.length;i++){
          if (msg[i].type == "select") {
            for (var key in msg[i].value) {
              $("#" + key).val(change_str_num(msg[i].value[key]));
            }
          } else {
            for (var key in msg[i].value) {
              $("#" + key).val(msg[i].value[key]);
            }
          }
        }
        $(".selectpicker").selectpicker('refresh');
        $(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
        searchInfo();
      }
      $("#loading").css('display','none');
    },
    error:function(){
      $("#loading").css('display','none');
      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
    }
  })
}
function change_str(ele){
	  if($('#'+ele).val()){
	    var arr = $('#'+ele).val().map(v=>{ 
	      return v > 9 ? v : '0'+v
	    })
	    return arr.join();
	  }else{
	    return '';
	  }
	}

function change_str_num(str){
	  if(str){
		return str.split(',').map(v=>{ 
	      return v > 9 ? v : +v
	    })
	  }else{
	    return '';
	  }
	}

function other() {
	  var search_ele = ['defectCode','defectSummary','submitUserName','submitDate','defectStatus','defectSource','severityLevel','emergencyLevel','windowName','requirementName','systemName','defectType','sel_assingUserName'];
	  search_ele.map(function(ele){
		  $('#'+ ele).on('change', function () {
			  if(is_check_Collect_flag){
				  $(".collection").children("span").removeClass("fa-heart").addClass("fa-heart-o");
			  }
		  })
	  })
}