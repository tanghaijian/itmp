/**
 * Description: 开发管理平台的缺陷管理
 * Author:liushan
 * Date: 2019/1/4 下午 1:55
 */

var _SeeFiles =[];
var  _checkfiles = [];
$(function () {
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
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onerror = function(e) {
                    layer.alert("文件" + file.name +" 读取出现错误", {icon: 0});
                    return false;
                };
                reader.onload = function (e) {
                    if(e.target.result) {
                        /*console.log("文件 "+file.name+" 读取成功！");*/
                    }
                };
            }

            //列表展示
            var _tr = '';
            var file_name = file.name.split("\.")[0];
            var file_type = file.name.split("\.")[1];
            var _td_icon = filePicClassPath(file_type); // common.js
            var _td_name = '<span >'+file.name+'</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile_other(this)">删除</a></td>';
            var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
            _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
            _table.append(_tr);

        }
        //上传文件
        $.ajax({
            type:'post',
            url:'/zuul/testManage/testtask/uploadFile',
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
                    }else if(modalType == 'handle'){
                        _handlefiles.push(data[k]);
                    }else if(modalType == 'check'){
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
                if(modalType == 'new'){
                    $("#files").val(JSON.stringify(_files));
                    clearUploadFile('uploadFile');
                }else if(modalType == 'edit'){
                    $("#editfiles").val(JSON.stringify(_editfiles));
                    clearUploadFile('edituploadFile');
                }else if(modalType == 'handle'){
                    $("#handlefiles").val(JSON.stringify(_handlefiles));
                    clearUploadFile('handleuploadFile');
                }else if(modalType == 'check'){
                    $("#checkfiles").val(JSON.stringify(_checkfiles));
                    clearUploadFile('checkuploadFile');
                }
            }
        });
    });

    $("#checkuploadFile_testWork").change(function(){
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
            if(modalType == 'new'){
                fileList=_files;
            }else if(modalType == 'edit'){
                oldFileList=_editfiles;
                fileList=Neweditfiles;
            }else if(modalType == 'check'){
                fileList=_checkfiles;
            }else if(modalType=='dHandle'){
                oldFileList=_Dhandlefiles;
                fileList=_newDhandlefiles;
            }else if(modalType=='Handle'){
                oldFileList=_handlefiles
                fileList=_Newhandlefiles;
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
                            /*console.log("文件 "+file.name+" 读取成功！");*/
                        }
                    };
                })(i);
            }
            //列表展示
            var _tr = '';
            var file_name = file.name.split("\.")[0];
            var file_type = file.name.split("\.")[1];

            var _td_icon = filePicClassPath(file_type); // common.js
            var _td_name = '<span >'+file.name+'</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile_other(this)">删除</a></td>';

            var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
            _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
            _table.append(_tr);

        }
        //上传文件
        $.ajax({
            type:'post',
            url:'/zuul/testManage/worktask/uploadFile',
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
                    $("#files").val(JSON.stringify(_files));
                    clearUploadFile('uploadFile');
                }else if(modalType == 'edit'){
                    //$("#editfiles").val(JSON.stringify(_editfiles));
                    $("#newFiles").val(JSON.stringify(Neweditfiles));
                    clearUploadFile('edituploadFile');
                }else if(modalType == 'check'){
                    $("#checkfiles").val(JSON.stringify(_checkfiles));
                    clearUploadFile('checkuploadFile');
                }else if(modalType=='dHandle'){
                    $("#NewDHattachFiles").val(JSON.stringify(_newDhandlefiles));
                    clearUploadFile('DHituploadFile');
                }else if(modalType=='Handle'){
                    $("#newHattachFiles").val(JSON.stringify(_Newhandlefiles));
                    clearUploadFile('HituploadFile');
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
});

// 确认缺陷
function affirmDefectModal(rows ){
    /*$("#loading").css('display','block');*/
    reset_opt();
    //var rows = JSON.parse(row.replace(/\n/g,"\\r\\n"));
    $("#dev_checkDefectID").val( rows.id );

    $(".down_content").css("display", "block" );
    $("#dev_defectBaseInfo .def_title .fa").removeClass("fa-angle-double-down");
    $("#dev_defectBaseInfo .def_title .fa").addClass("fa-angle-double-up");

    $(".defectHandlingLog .def_title .fa").removeClass("fa-angle-double-up");
    $(".defectHandlingLog .def_title .fa").addClass("fa-angle-double-down");

    $("#dev_check_systemName").text(rows.systemName);
    $("#dev_check_defectCode").text(rows.defectCode);
    $("#dev_check_testTaskName").text(rows.testTaskName);
    $("#dev_check_testCaseName").text(rows.testCaseName);
    $("#dev_check_submitUserName").text(rows.submitUserName);
    $("#dev_check_assignUserName").text(rows.assignUserName);
    $("#dev_check_defectOverview").html(rows.defectOverview);
    $("#dev_check_defectSummary").text(rows.defectSummary);

    rows["checkDefectType"] = "#dev_check_defectType";
    rows["checkDefectSource"] = "#dev_check_defectSource";
    rows["checkDefectStatus"] = "#dev_check_defectStatus";
    rows["checkSeverityLevel"] = "#dev_check_severityLevel";
    rows["checkEmergencyLevel"] = "#dev_check_emergencyLevel";
    dicDefectSelect(rows);

    //  缺陷详情页面接口
    attList(rows.id,"#dev_check_table");

    $("#dev_checkDefectDiv").modal("show");

}

// 驳回缺陷
function dev_rejectDefectModal(){
    $("#dev_checkDefectDiv").modal("hide");
    var id = $("#dev_checkDefectID").val();
    var rows = JSON.stringify({id:id});
    rejectDefectModal(rows);
}

// 待检测状态：修复完成
function dev_repairCompleteModal(rows,that){
    reset_opt();
    formFileList = [];
    _repairCompleteDefectAgainStatus = $(that).parent().parent().find("#list_defectStatus").val();
    _repairCompleteDefectData = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
    $(".solution").css("display","block");
    $("#rejectDiv").modal("show");
}

// 待检测状态：根据选中解决情况，显示不同按钮
function solveStatusShowBtu(){
    var opt_solution = $("#opt_solution").find("option:selected").val();
    if (opt_solution == 1){
        $("#opt_Restored").css("display","none");
        $(".opt_DelayRepair").css("display","inline-block");
    } else if(opt_solution == 2){
        $("#opt_Restored").css("display","inline-block");
        $(".opt_DelayRepair").css("display","none");
    } else {
        $(".opt_DelayRepair").css("display","none");
        $("#opt_Restored").css("display","none");
    }
}
// 清空
function dev_defect_reset(){
    $(".opt_DelayRepair").css("display","none");
}

/*--------------------测试任务查看页面--------------------------*/
//查看
function showtestTask(id) {
    var id = id;
    $("#loading").css('display','block');
    $.ajax({
        url:"/testManage/testtask/getOneTestTask2",
        type:"post",
        dataType:"json",
        data:{
            "id":id
        },
        success: function(data) {
            $("#loading").css('display','none');
            $("#checksystemName").text('');
            $("#checkdeptName").text('');
            $("#connectDiv").empty();
            $("#workTaskDiv").empty();
            $("#checkFileTable").empty();
            $("#remarkBody").empty();
            $("#remarkDetail").val('');
            $("#checkAttTable_req").empty();
            $("#handleLogs").empty();
            $("#brother_div").empty();
            $("#checktestTaskTitle").text(toStr(data.featureCode) +" | "+toStr(data.featureName) );
            
            $("#checkItcdReqId").val("");
			$("#checkItcdReqId").val(data.itcdRequrementId);
			$("#checktaskId").val("");
			$("#checktaskId").val(data.taskId);
			
			
            $("#checktestTaskOverview").text(data.featureOverview);
            var statusName = '';
             
            for(var i=0;i<testtaskStatusList.length;i++){
                if( testtaskStatusList[i].value == data.requirementFeatureStatus ){
                    statusName = testtaskStatusList[i].innerHTML;
                }
            }
            $("#checktestTaskStatus").text( statusName );
            $("#checkdevManPost").text(data.manageUserName);
            $("#checkexecutor").text(data.executeUserName);
            $("#checksystemName").text('');
            $("#checksystemName").text(data.systemName);
            $("#checkoutrequirement").text('');
            $("#checkoutrequirement").text(toStr(data.requirementCode)/*+" | "+data.requirementName*/);
            $("#checkdeptName").text(data.deptName);
            $("#checkWindow").text('');
            $("#checkWindow").text(data.windowName);
            
            $("#checkChange").empty();
			$("#checkRequirementSource").empty();
			$("#checkImportantRequirementType").empty();
			$("#checkPptDeployTime").empty();
			$("#checkSubmitTestTime").empty();
			$("#checkChange").text(data.requirementChangeNumber);
			$("#checkRequirementSource").text(data.featureSource);
            var importantRequirement = "";
            if(data.importantRequirementType=='1'){
                importantRequirement="是"
            }else if(data.importantRequirementType=='2'){
                importantRequirement="否"
            }
            $("#checkImportantRequirementType").text(importantRequirement);

			$("#checkPptDeployTime").text(timestampToTime(data.pptDeployTime));
			$("#checkSubmitTestTime").text(timestampToTime(data.submitTestTime));
			
             
            var status = "";
            if(data.temporaryStatus =="1"){
                status = "是";
            }else if(data.temporaryStatus =="2"){
                status = "否";
            }
            $("#checktemporaryTask").text(status);//1是2否
//            $("#checkpreSitStartDate").text('');
//            $("#checkpreSitStartDate").text(data.planSitStartDate);
//            $("#checkpreSitEndDate").text('');
//            $("#checkpreSitEndDate").text(data.planSitEndDate);
//            $("#checkpreSitWorkload").text('');
//            $("#checkpreSitWorkload").text(data.estimateSitWorkload);
            $("#checkactSitStartDate").text('');
            $("#checkactSitStartDate").text(data.actualSitStartDate);
            $("#checkactSitEndDate").text('');
            $("#checkactSitEndDate").text(data.actualSitEndDate);
            $("#checkactSitWorkload").text('');
            $("#checkactSitWorkload").text(data.actualSitWorkload);
//            $("#checkprePptStartDate").text('');
//            $("#checkprePptStartDate").text(data.planPptStartDate);
//            $("#checkprePptEndDate").text('');
//            $("#checkprePptEndDate").text(data.planPptEndDate);
//            $("#checkprePptWorkload").text('');
//            $("#checkprePptWorkload").text(data.estimatePptWorkload);
            $("#checkactPptStartDate").text('');
            $("#checkactPptStartDate").text(data.actualPptStartDate);
            $("#checkactPptEndDate").text('');
            $("#checkactPptEndDate").text(data.actualPptEndDate);
            $("#checkactPptWorkload").text('');
            $("#checkactPptWorkload").text(data.actualPptWorkload);
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
			} else if (data.createType == "0") {//TODO aviyy 20201103
                type = "提前下发";
                $("#createfiles").show();
                $("#synfiles").hide();
            } else if (data.createType == "-1") {
                type = "生产问题确认";
                $("#createfiles").show();
                $("#synfiles").hide();
            }
            $("#checkcreateType").text(type);
            if(data.list!=undefined){
                for(var i=0;i<data.list.length;i++){
                    $("#connectDiv").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt">'+data.list[i].testTaskCode+' '+data.list[i].testTaskName+'</div>'+
                        '<div class="def_col_36">实际工作情况：'+toStr(data.list[i].actualStartDate)+'~'+toStr(data.list[i].actualEndDate)+' '+toStr(data.list[i].actualWorkload)+'人天</div>'+
                        '<div class="def_col_36">'+data.list[i].testTaskStatusName+' '+toStr(data.list[i].testUserName)+'</div></div>');
                }
            }
            //相关任务的显示
            if(data.brother!=undefined){
                for(var i=0;i<data.brother.length;i++){
                    if(data.brother[i].featureCode==undefined){
                        data.brother[i].featureCode="";
                    }
                    $("#brother_div").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="showtestTask('+data.brother[i].id+')">'+data.brother[i].featureCode+'</a>   '+data.brother[i].featureName+'</div>'+
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
                    var _td_icon = filePicClassPath(file_type); // common.js
                    //<i class="file-url">'+data.attachements[i].filePath+'</i>
                    var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.attachements[i].fileS3Bucket+'</i><i class = "file-s3Key">'+data.attachements[i].fileS3Key+'</i></td>';
                    _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download_feature(this)">'+_td_icon+" "+_td_name+'</tr>';

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
                            var _td_icon = filePicClassPath(file_type); // common.js
                            //<i class="file-url">'+data.remarks[i].remarkAttachements[j].filePath+'</i>
                            var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.remarks[i].remarkAttachements[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.remarks[i].remarkAttachements[j].fileS3Key+'</i></td>';

                            _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download_feature(this)">'+_td_icon+_td_name+'</tr>';
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
                    if((data.logs[i].logDetail==null||data.logs[i].logDetail=='')&&(data.logs[i].logAttachements==null || data.logs[i].logAttachements.length<=0)){
                        if(data.logs[i].logType!="新增测试任务"){
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
                            var _td_icon = filePicClassPath(file_type); // common.js
                            //<i class="file-url">'+data.logs[i].logAttachements[j].filePath+'</i>
                            var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.logs[i].logAttachements[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.logs[i].logAttachements[j].fileS3Key+'</i></td>';

                            _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download_feature(this)">'+attType+_td_icon+_td_name+'</tr>';
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
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
    modalType = 'check';
    $("#checktestTask").modal("show");
}

function timestampToTime(timestamp) {
	if(timestamp == null){
		return;
	}
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = date.getDate() < 10 ? '0'+date.getDate() : date.getDate() ; 
    return Y+M+D;
} 

//提交备注
function saveRemark(){
    if($("#remarkDetail").val()==''){
        layer.alert('备注内容不能为空！！！', {icon: 0});
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
    var remark = $("#remarkDetail").val();
    var id = $("#checkReqFeatureId").val();
    $.ajax({
        url:"/testManage/testtask/addRemark",
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
                showtestTask(id);
                $("#checkfiles").val('');
            }else if(data.status == "2"){
                layer.alert('保存失败！！！', {icon: 2});
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

// 为空或者未定义或者为NULL或者是字符串去空格
function toStr(value1){
    if(value1==undefined ||value1==null||value1=="null"||value1=="NULL"){
        return "";
    }else if(!isNaN(value1)){
        return value1;
    }else{
        return value1.toString().trim();
    }
}
//日期转换
function datFmt(date,fmt) { // author: meizz
    var o = {
        "M+": date.getMonth() + 1, // 月份
        "d+": date.getDate(), // 日
        "h+": date.getHours(), // 小时
        "m+": date.getMinutes(), // 分
        "s+": date.getSeconds(), // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3), // 季度
        "S": date.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

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
//文件下载
function download_feature(that){
    var fileS3Bucket = $(that).children(".file-bucket").text();
    var fileS3Key = $(that).children(".file-s3Key").text();
    var fileNameOld = $(that).children("span").text();
    var url = encodeURI("/testManage/testtask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld);
    window.location.href = url;
}
//移除上传文件
function delFile_other(that){
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
    }else if(modalType == 'handle'){
        var _file = $("#handlefiles").val();
        if(_file != ""){
            var files = JSON.parse(_file);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _handlefiles = files;
            $("#handlefiles").val(JSON.stringify(files));
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
//清空上传文件
function clearUploadFile(idName){
    $('#'+idName+'').val('');
}

/*--------------------测试任务查看页面--------------------------*/


/*---------------------工作任务查看页面--------------------------*/
function  getSee(id){
    $.ajax({

        method:"post",
        url:"/testManage/worktask/getSeeDetail",
        data:{"id":id},
        success : function(data) {
            selectdetail(data)
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

//查看
function selectdetail(data){
    $("#loading").css('display','block');
    _checkfiles=[];
    var map=data.dev;
    $("#checkfiles").val("");
    $("#checkAttTable").empty();
    $("#taskRemark").empty();
    $("#handleLogs_test").empty();
    $("#SeeFileTable").empty();
    $("#tyaskRemark").val("");
    $("#SdevCode").html("");
    $("#SdevName").html("");
    $("#SdevOverview").html("");
    $("#SStatus").html("");
    $("#devuserID").html("");
//    $("#planStartDate").html("");
//    $("#planEndDate").html("");
    $("#featureName").html("");
//    $("#SplanWorkload").html("");
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
    $("#applyDeptId").html("");
    $("#expectOnlineDate").html("");
    $("#planOnlineDate").html("");
    $("#lastUpdateDate3").html("");
    $("#createDate3").html("");
    $("#openDate").html("");
    $("#SdevCode").html(map.testTaskCode);
    $("#SdevName").html(map.testTaskName);
    $("#SdevOverview").html(map.testTaskOverview);
    var devTaskStatus=map.devTaskStatus;
    if(devTaskStatus=="1"){
        $("#SStatus").html("待实施");
    }else if(devTaskStatus=="2"){
        $("#SStatus").html("实施中");
    }else if(devTaskStatus=="3"){
        $("#SStatus").html("实施完成");
    }
    $("#devuserID").html(map.devuserName);
    $("#testTaskID").html(map.id);
    $("#createBy").html(map.createName);
    $("#check_testStage").text(map.testStage);
    
    if(map.createDate!=null){
        $("#createDate").html(datFmt(new Date(map.createDate),"yyyy年MM月dd"));
    }

//    if(map.planStartDate!=null){
//        var planstartDate=datFmt(new Date(map.planStartDate),"yyyy年MM月dd");
//        $("#planStartDate").html(planstartDate);
//    }
//    if(map.planEndDate!=null){
//        var planEndDate=datFmt(new Date(map.planEndDate),"yyyy年MM月dd");
//        $("#planEndDate").html(planEndDate);
//    }
//    $("#SplanWorkload").html(map.planWorkload);

    var users = map.executeUser.join(',');
	$("#cases").html("");
	$("#caseExecutes").html("");
	$("#executeUser").html("");
	$("#cases").html(map.cases);
	$("#caseExecutes").html(map.caseExecutes);
	$("#executeUser").html(users);


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

    $("#featureName").html(map.featureName);
    $("#featureOverview").html(map.featureOverview);
    var featureStatus=map.requirementFeatureStatus;
    if(featureStatus=="1"){
        $("#requirementFeatureStatus").html("系测待测试");
    }else if(featureStatus=="2"){
        $("#requirementFeatureStatus").html("系测中");
    }else if(featureStatus=="3"){
        $("#requirementFeatureStatus").html("系测审核中");
    }else if(featureStatus=="4"){
        $("#requirementFeatureStatus").html("系测完成");
    }else if(featureStatus=="5"){
        $("#requirementFeatureStatus").html("版测待测试");
    }else if(featureStatus=="6"){
        $("#requirementFeatureStatus").html("版测中");
    }else if(featureStatus=="7"){
        $("#requirementFeatureStatus").html("版测审核中");
    }else if(featureStatus=="8"){
        $("#requirementFeatureStatus").html("版测完成");
    }else if(featureStatus=="9"){
        $("#requirementFeatureStatus").html("退回");
    }else if(featureStatus=="10"){
        $("#requirementFeatureStatus").html("挂起");
    }else if(featureStatus=="0"){
        $("#requirementFeatureStatus").html("撤销");
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
            var _td_icon = filePicClassPath(file_type); // common.js
            //<i class="file-url">'+data.attachements[i].filePath+'</i>
            var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
            var row =  JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
            _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download_work('+row+')">'+_td_icon+" "+_td_name+'</tr>';
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
                        var _td_icon = filePicClassPath(file_type); // common.js
                        var _td_name = '<span>'+file_name+'</span>';

                        var row =  JSON.stringify( data.Attchement[j]).replace(/"/g, '&quot;');
                        var _td_name_a = "<a onclick='download_work("+row+")'>"+_td_name +"</a>";
                        _tr+='<div class="fileTb" style="cursor:pointer">'+_td_icon+_td_name_a;

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
                            str+='<span>'+Detail[s].fieldName+"："+value+"→"+Detail[s].newValue+'</span></br>'
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
                        var _td_icon = filePicClassPath(file_type); // common.js
                        var _td_name = '<span>'+file_name+'</span>';
                        //var _td_name = '<span>'+file_name+'</span><i class="file-url">'+data.logAttachement[j].filePath+'</i><i class = "file-bucket">'+data.logAttachement[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.logAttachement[j].fileS3Key+'</i></td>';


                        var row =  JSON.stringify( data.logAttachement[j]).replace(/"/g, '&quot;');
                        var _td_name_a = "<a onclick='download_work("+row+")'>"+_td_name +"</a>";
                        _tr+='<div class="fileTb" style="cursor:pointer">'+attType+_td_icon+_td_name_a;
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
        $("#handleLogs_test").append(str);
    }
    modalType = 'check';
    $("#loading").css('display','none');
    $("#selectdetail").modal("show");
}

//提交备注
function addTestRemark(){
    var testTaskRemark=$.trim($("#tyaskRemark").val());
    if(testTaskRemark==""||testTaskRemark==undefined){
        layer.alert('备注信息不能为空！', {
            icon: 2,
            title: "提示信息"
        });
        return;
    }
    var id=$.trim($("#testTaskID").text());
    var obj = {};
    obj.testTaskRemark =$.trim($("#tyaskRemark").val());
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
            layer.alert('保存成功！！！', {
                icon: 1,
                title: "提示信息"
            });
            _checkfiles = [];
            $("#checkfiles").val('');
            getSee(id);
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
function download_work(row){
    var fileS3Bucket = row.fileS3Bucket;
    var fileS3Key = row.fileS3Key;
    var fileNameOld = row.fileNameOld;
    window.location.href = "/testManage/worktask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld;
}
/*---------------------工作任务查看页面--------------------------*/


//同步jira附件弹窗
function syncJira(){
	$("#workCodes").val('');
	$("#workCodes_modal").modal("show");
}


//同步jira提交
var syncJiraKey;
function syncJira_submit(){
	if(!$("#workCodes").val()){
		layer.msg("请输入编号!!!",{icon : 0});
		return;
	}
	var codes = $.trim($("#workCodes").val()).replace("，",",");
	$("#loading").css('display', 'block'); 
	$.ajax({
		method:"post", 
		url:"/testManage/defect/jiraFileByCode",
		data:{
		    "defectJiraIds":codes,
            key:syncJiraKey
        },
		success : function(data) { 
			if( data.status == 1 ){
                if(data.code != null && data.code != undefined && data.code.length > 0){
                    layer.alert(data.code.join("<br>"));
                } else {
                    layer.alert("同步成功",{icon : 1});
                }
                syncJiraKey = data.key;
                $("#workCodes_modal").modal("hide");
	        } else {
                layer.alert("同步失败",{icon : 2});
	        }
			$("#loading").css('display', 'none'); 
		}
	})
}
















