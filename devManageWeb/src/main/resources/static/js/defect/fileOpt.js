/**
 * Description: 关于文件的各种操作
 * Author:liushan
 * Date: 2018/12/24 下午 2:06
 */

var defectAttIds = [], defectSummary_msg = '', defectOverview_msg = '';

/*编辑文件上传列表*/
function edit_uploadList(){
    $("#edit_uploadFile").change(function(){
        var files = this.files;
        if (files.length > 0){
            for (var i = 0,len = files.length; i < len; i++){
                var file = files[i];
                var _fileName = file.name;

                var flag = etectionFile(file,formFileList,_fileName,editAttList);
                if (flag != false){
                    formFileList.push(file);

                    var index = _fileName .lastIndexOf("\.");
                    var file_type  = _fileName .substring(index + 1, _fileName.length);
                    $("#edit_newFileTable").bootstrapTable('prepend', {
                        fileNameOld:file.name,
                        fileType:file_type,
                        id:null
                    })
                }
            }
        }
        resetFile()
    });
}

// 文件上传 后台
function uploadFile(defectId,logId){
    if (formFileList.length <= 0){

        if (defectAttIds.length > 0){
            deleteAtts(defectId,logId);
        } else {
            $("#loading").css('display','none');
            layer.alert('操作成功！', {
                icon: 1,
                title: "提示信息"
            });
            if (submitDefectStatus == "new"){
                $("#newDefect").modal("hide");
            } else if(submitDefectStatus == "edit"){
                $("#editDefect").modal("hide");
            }
        }
        reset_opt();
        initTable();
        return false;
    }
    var files = new FormData();
    var filesSize = 0;
    for (var i = 0,len2 = formFileList.length;i < len2;i++){
        filesSize += formFileList[i].size;
        files.append("files",formFileList[i]);
    }

    if(filesSize > 1048576000){
        layer.alert('文件太大,请删选！！！', {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }

    $("#loading").css('display','block');
    $.ajax({
        type: "post",
        url:'/zuul'+defectUrl+'defect/updateFiles?id='+defectId+"&logId="+logId,
        dataType: "json",
        data:files,
        cache: false,
        processData: false,
        contentType: false,
        success:function (data) {
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                if (defectAttIds.length > 0){
                    deleteAtts(defectId,logId);
                } else {
                    layer.alert('操作成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    if (submitDefectStatus == "new"){
                        $("#newDefect").modal("hide");
                    } else if(submitDefectStatus == "edit"){
                        $("#editDefect").modal("hide");
                    }
                    reset_opt();
                    formFileList = [];
                    editAttList = [];
                    initTable();
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
    })
}

// 文件上传列表格式
function uploadFileList(){
    $("#uploadFile").change(function(){
        var files = this.files;
        if (files.length > 0){
            for (var i = 0,len = files.length; i < len; i++){
                var file = files[i];
                var _fileName = file.name;

                var flag = etectionFile(file,formFileList,_fileName,null);
                if (flag != false){
                    formFileList.push(file);

                    //列表展示
                    var name =  JSON.stringify({fileName:_fileName}).replace(/"/g, '&quot;');
                    var _tr = '';
                    var index = _fileName .lastIndexOf("\.");
                    var file_type  = _fileName .substring(index + 1, _fileName.length);
                    var _td_name = '<span>'+_fileName+'</span><i class="file-Id"></i></div></td>';
                    var _td_opt = '<td class="font_right"><a href="javascript:void(0);" class="del-file-button" onclick="delFile('+name+',this)">删除</a></td>';
                    var _td_icon = filePicClassPath(file_type); // common.js
                    var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                    _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
                    _table.append(_tr);
                }
            }
        }
        resetFile()
    });

}

/*备注日志上传文件列表*/
function opt_uploadFile(){
    $("#opt_uploadFile").change(function(){
        var files = this.files;
        if (files.length > 0){
            for (var i = 0,len = files.length; i < len; i++){
                var file = files[i];
                var _fileName = file.name;

                var flag = etectionFile(file,formFileList,_fileName,null);

                if(flag != false) {
                    formFileList.push(file);

                    //列表展示
                    var name =  JSON.stringify({fileName:_fileName}).replace(/"/g, '&quot;');
                    var _tr = '';
                    var index = _fileName .lastIndexOf("\.");
                    var file_type  = _fileName .substring(index + 1, _fileName.length);
                    var _td_name = '<span>'+_fileName+'</span><i class="file-Id"></i></div></td>';
                    var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile('+name+',this)">删除</a></td>';
                    var _td_icon = filePicClassPath(file_type); // common.js
                    var _table=$("#optFileTable");
                    _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
                    _table.append(_tr);
                }
            }
        }

        resetFile();
    });
}

/*备注日志上传文件 后台*/
function remarkUploadFile(logId){
    if (formFileList.length <= 0){
        $("#loading").css('display','none');
        layer.alert('操作成功 ！', {
            icon: 1,
            title: "提示信息"
        });

        initTable();
        $("#newDevTask").modal("hide");
        $("#rejectDiv").modal("hide");
        reset_opt();
        return false;
    }
    var remarkFiles = new FormData();
    var remarkFilesSize = 0;
    for (var i = 0,len2 = formFileList.length;i < len2;i++){
        remarkFilesSize += formFileList[i].size;
        remarkFiles.append("files",formFileList[i]);
    }

    if(remarkFilesSize > 1048576000){
        layer.alert('文件太大,请删选！！！', {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }

    $("#loading").css('display','block');
    $.ajax({
        type: "post",
        url:'/zuul'+defectUrl+'defect/updateRemarkLogFiles?logId='+logId,
        dataType: "json",
        data:remarkFiles,
        cache: false,
        processData: false,
        contentType: false,
        success:function (data) {
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                layer.alert('操作成功！', {
                    icon: 1,
                    title: "提示信息"
                });
                formFileList = [];
                initTable();
                $("#newDevTask").modal("hide");
                $("#rejectDiv").modal("hide");
                reset_opt();
            }
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })
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
                var classType = filePicClassPath(row.fileType); // common.js
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
                if (idName == "#check_table" || idName == "#dev_check_table"){
                    return '';
                } else if (idName == "#edit_newFileTable"){
                    if(row.id == null){
                        var name =  JSON.stringify({fileName:row.fileNameOld}).replace(/"/g, '&quot;');
                        return '<div class="span_a_style"><span class="a_style" onclick="delFile('+name+',this)">删除</span></div>';
                    } else {
                        var obj = {"id":row.id,"fileNameOld":row.fileNameOld};
                        obj = JSON.stringify(obj).replace(/"/g, '&quot;');
                        return '<div class="span_a_style"><span class="a_style" onclick="deleteAtt(' + obj +',this,\'' + index +'\')">删除</span></div>';
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

// 附件列表后台接口
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
            	showField( data.field );
                var att = data.attList;
                attTable(att,FileTable);
                if (att != null && att.length > 0){
                    for( var i = 0,len = att.length;i < len ; i++){
                        editAttList.push(att[i].fileNameOld);
                    }
                }

                if(data.defectInfo != null ){
                    var rows = data.defectInfo;
                    defectSummary_msg = rows.defectSummary;
                    defectOverview_msg = rows.defectOverview;
                    if(submitDefectStatus == "checkDefect"){
                        $("#check_systemName").text(rows.systemName != null?rows.systemName:'');
                        $("#check_defectCode").text(rows.defectCode);
                        $("#check_submitUserName").text(rows.submitUserName);
                        $("#check_assignUserName").text(rows.assignUserName != null?rows.assignUserName:"");
                        $("#check_defectOverview").html( htmlDecodeJQ(rows.defectOverview) );
                        $("#check_defectSummary").text(rows.defectSummary);
                        $("#Discovery_environment").text('');
                        if(rows.discoveryEnvironment){
                        	$('select#new_discovery_Environment').find('option').each(function(idx,val){
                            	if(val.value == isValueNull(rows.discoveryEnvironment)){
                            		$("#Discovery_environment").text(val.innerText);
                            	}
                            })
                        }

                        $("#check_projectGroupName").text(isValueNull(rows.projectGroupName));//
                        $("#check_closeTime").text(isValueNull(rows.closeTime));
                        $("#check_assetSystemTreeName").text(isValueNull(rows.assetSystemTreeName));//
                        $("#check_detectedSystemVersionName").text(isValueNull(rows.detectedSystemVersionName));//
                        $("#check_repairSystemVersionName").text(isValueNull(rows.repairSystemVersionName));//
                        $("#check_expectRepairDate").text(isValueNull(rows.expectRepairDate));
                        $("#check_estimateWorkload").text(isValueNull(rows.estimateWorkload));
                        $("#check_rootCauseAnalysis").html(htmlDecodeJQ(rows.rootCauseAnalysis));

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
                        
                        if(data.remarks.length > 0){
                        	checkDefectRemark(data);
                        }
                        openNewWindowCheckImg();
                    } else if(submitDefectStatus == "devCheckDefect"){
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

// 下载文件
function downloadFile(row){
    var url = "/zuul"+defectUrl+"defect/downloadFile?fileS3Bucket="+row.fileS3Bucket+"&fileNameOld="+row.fileNameOld+"&fileS3Key="+row.fileS3Key;
    window.location.href = encodeURI(url);
}

//移除上传文件
function delFile(name,that){
    $("#edit_newFileTable").bootstrapTable('remove',{
        field:"fileNameOld",
        values:[name.fileName]
    });
    $(that).parent().parent().remove();
    removeFileName(name.fileName);

}

// 删除附件
function deleteAtt(row,that,index){
    $(fileTableName).bootstrapTable('remove',{
        field:"fileNameOld",
        values:[row.fileNameOld]
    });
    defectAttIds.push(row.id);
    removeFileName(row.fileNameOld);
}

function removeFileName(fileName){
    for (var ii = 0,len1 = formFileList.length;ii < len1;ii++){
        if (formFileList[ii].name == fileName){
            formFileList.splice(ii,1);
            break;
        }
    }

    for (var iii = 0,len1 = editAttList.length;iii < len1;iii++){
        if (editAttList[iii] == fileName){
            editAttList.splice(iii,1);
            break;
        }
    }
}

function deleteAtts(defectId,logId){
    $("#loading").css('display','block');
    $.ajax({
        url:defectUrl+"defect/removeAtts",
        method:"post",
        traditional:true,
        data:{
            ids: defectAttIds,
            defectId:defectId,
            logId:logId
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                layer.alert("操作成功", {
                    icon: 1,
                    title: "提示信息"
                });
                defectAttIds = [];
                if (submitDefectStatus == "new"){
                    $("#newDefect").modal("hide");
                } else if(submitDefectStatus == "edit"){
                    $("#editDefect").modal("hide");
                }
                reset_opt();
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

// 检测文件
function etectionFile(file,formFileList,_fileName,editAttList){
    var _fileSize = file.size;
    var _fileNameSpan = '<span style="color: red">'+_fileName+'</span>';
    if (_fileSize <= 0){
        layer.alert('不能上传空文件！', {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }

    if(_fileSize > 10485760){
        layer.alert(_fileNameSpan+'：文件太大！', {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }

    /*if(!fileAcceptBrowser()){
        var index = _fileName .lastIndexOf("\.");
        var file_type  = _fileName .substring(index + 1, _fileName.length);
        if(file_type!="doc"&&file_type!="docx"&&file_type!="xls"&&file_type!="xlsx"&&file_type!="txt"&&file_type!="pdf"&&file_type!="jpg"&&file_type!="png"){
            var file_typeSpan = '<span style="color: red">.'+file_type+'</span>';
            layer.alert('不支持'+file_typeSpan+'文件格式', {
                icon: 2,
                title: "提示信息"
            });
            return false;
        }
    }*/

    //读取文件
    if (window.FileReader) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onerror = function(e) {
            layer.alert("文件" + _fileNameSpan +" 读取出现错误", {
                icon: 2,
                title: "提示信息"
            });
            return false;
        };
        reader.onload = function (e) {
            if(e.target.result) {
                /*console.log("文件 "+file.name+" 读取成功！");*/
            }
        };
    }


    if(formFileList != null && formFileList.length > 0 ){
        for(var ii = 0,len = formFileList.length;ii < len ; ii++){
            if(formFileList[ii].name == _fileName){
                layer.alert(_fileNameSpan +"  文件已存在，请勿重新添加", {
                    icon: 2,
                    title: "提示信息"
                });
                return false;
            }
        }
    }

    if (editAttList != null && editAttList.length > 0){
        for(var i = 0,len = editAttList.length;i < len;i++){
            if(_fileName == editAttList[i]){
                layer.alert(_fileNameSpan +"  文件已存在，请勿重新添加", {
                    icon: 2,
                    title: "提示信息"
                });
                return false;
            }
        }
    }

}

//清空文件
function resetFile(){
    $("#opt_uploadFile").val("");
    $("#edit_uploadFile").val("");
    $("#uploadFile").val("");
}