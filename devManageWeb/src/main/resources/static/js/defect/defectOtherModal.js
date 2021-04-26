/**
 * Description: 工作任务、开发任务弹框
 * Author:liushan
 * Date: 2019/3/5 下午 6:25
 */
var modalType = '';
var _checkfiles = [];
var _SeeFiles = [];
$(function(){

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

    // 开发任务上传文件
    $("#checkuploadFile_req").change(function(){
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
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile_work(this)">删除</a></td>';
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
                    clearUploadFile('checkuploadFile_req');
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

/*查看工作任务*/
function  getSee(id,requirementFeatureId){
    $.ajax({
        method:"post",
        url:"/devManage/worktask/getSeeDetail",
        data:{id:id,
            requirementFeatureId:requirementFeatureId},
        success : function(data) {
            data["requirementFeatureId"] = requirementFeatureId;
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

//工作任务详情
function selectdetail(data){
    _checkfiles = [];
    if(data.devTasks != null){
        $("#windowProduction").text("");
        $("#windowProduction").text(data.devTasks.windowName);
        $("#systemVersion").text("");
        $("#systemVersion").text(toStr(data.devTasks.versionName)+"-->"+toStr(data.devTasks.systemScmBranch));
    }

    $("#loading").css('display','block');
    $("#checkAttTable").empty();
    $("#SeeFileTable").empty();
    $("#taskRemark").empty();
    $("#checkfiles").val("");
    $("#handleLogs").empty();

    $("#requirementFeatureId").val(data.requirementFeatureId);

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
    $("#work_systemId").html("");
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
    $("#tyaskRemark").val("");
    $("#planStartDate").html("");
    $("#planEndDate").html("");
    $("#SplanWorkload").html("");
    $("#actualEndDate").html("");
    $("#actualStartDate").html("");
    $("#actualWorkload").html("");
    $("#SdevCode").html("["+map.devTaskCode+"]"+"  |  "+ map.devTaskName);
    $("#SdevOverview").html(map.devTaskOverview);
    var devTaskStatus=map.devStatus;
    $("#SStatus").html(devTaskStatus);

    $("#DevTaskID").val(map.id);

    $("#devuserID").html(map.devuserName);

    $("#createBy").html(map.createName);

    $("#createDate").html(datFmt(new Date(map.createDate),"yyyy年MM月dd"));
    if(map.planStartDate!=null){
        var planstartDate = datFmt(new Date(map.planStartDate),"yyyy年MM月dd");
        $("#planStartDate").html(planstartDate);
    }
    if(map.planEndDate!=null){
        var planEndDate = datFmt(new Date(map.planEndDate),"yyyy年MM月dd");
        $("#planEndDate").html(planEndDate);
    }

    if(map.actualStartDate!=null){
        var actualStartDate=datFmt(new Date(map.actualStartDate),"yyyy年MM月dd");
        $("#actualStartDate").html(actualStartDate);
    }
    if(map.actualEndDate!=null){
        var actualEndDate = datFmt(new Date(map.actualEndDate),"yyyy年MM月dd");
        $("#actualEndDate").html(actualEndDate);
    }
    $("#actualWorkload").html(map.actualWorkload);
    $("#featureName").html(map.featureName);
    $("#SplanWorkload").html(map.planWorkload);
    $("#featureOverview").html(map.featureOverview);
    var featureStatus=map.requirementFeatureStatus;
    $("#requirementFeatureStatus").html(featureStatus);

    $("#requirementName").html("");
    $("#requirementName").html(map.requirementName);

    $("#manageUserId").html(map.manageUserName);
    $("#executeUserId").html(map.executeUserName);
    $("#work_systemId").html(map.systemName);

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
            _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="work_download('+row+')">'+_td_icon+" "+_td_name+'</tr>';
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
                        var _td_name_a = "<a onclick='work_download("+row+")'>"+ _td_name +"</a>";
                        _tr+='<div class="fileTb" style="cursor:pointer">'+_td_icon + _td_name_a;

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

                    _span='<span>未经任何操作</span>'
                }else{
                    var Detail=JSON.parse(logDetail);
                    for (var s=0;s<Detail.length;s++) {
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
                    }
                }
            }else{
                str+='<span>'+logDetail+'</span>'
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
                        var _td_name_a = "<a onclick='work_download("+row+")'>"+ _td_name +"</a>";
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
        $("#handleLogs").append(str);
    }
    modalType = 'check';
    $("#loading").css('display','none');
    $("#selectdetail").modal("show");
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
            layer.alert('保存成功！！！', {
                icon: 1,
                title: "提示信息"
            });
            _checkfiles = [];
            $("#checkfiles").val('');
            var requirementFeatureId = $("#requirementFeatureId").val();
            getSee(id,requirementFeatureId);
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

//移除上传文件
function delFile_work(that,id){
    var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
    $(that).parent().parent().remove();
    removeFile(fileS3Key);
}
function work_download(row){
    var fileS3Bucket = row.fileS3Bucket;
    var fileS3Key = row.fileS3Key;
    var fileNameOld = row.fileNameOld;
    window.location.href = "/devManage/worktask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld;
}


/*------------------开发任务------------------*/
//查看
function showDevTask(id) {
    layer.open({
        type: 2,
        title: '开发任务详情',
        shadeClose: true,
        shade: false,
        area: ['100%', '100%'],
        id: "1",
        tipsMore: true,
        anim: 2,
        content:  '/devManageui/devtask/toInfo?rowId='+id,
        btn: '关闭' ,
        btnAlign: 'c', //按钮居中
        yes: function(){
            layer.closeAll();
        }
    });
}

//文件下载
function download1(that){
    var fileS3Bucket = $(that).children(".file-bucket").text();
    var fileS3Key = $(that).children(".file-s3Key").text();
    var fileNameOld = $(that).children("span").text();
    var url = encodeURI("/devManage/devtask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld);
    window.location.href = url;

}

//提交备注
function saveRemark(){
    var remark = $("#remarkDetail").val();
    var id = $("#checkReqFeatureId").val();
    if($("#remarkDetail").val()==''){
        layer.alert('备注内容不能为空！！！', {icon: 0});
        return;
    }
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
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

//查看页面查看附件
function showFile(){
    var reqFeatureId = $("#checktaskId").val();
    var requirementId =  $("#checkItcdReqId").val();

    if(requirementId== ''){
        layer.alert('需求id为空，不可以查看附件！', {icon: 0});
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
        content:  reqAttUrl +"?reqId="+requirementId+"&reqtaskId="+reqFeatureId+"&taskreqFlag=task"
    });

}