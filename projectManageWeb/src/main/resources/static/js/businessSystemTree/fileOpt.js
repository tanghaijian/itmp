// 文件操作
var defectAttIds = [];
var editAttList = [];
var formFileList = [];

// 文件图片地址
var filePicAddress = {
    _icon_word :"../static/images/file/word.png",
    _icon_excel : "../static/images/file/excel.png",
    _icon_text :"../static/images/file/text.png",
    _icon_pdf :"../static/images/file/pdf.png",
    _icon_PNG : "../static/images/file/PNG.png",
    _icon_JPG :"../static/images/file/JPG.png",
    _icon_BMP :"../static/images/file/BMP.png",
    _icon_WENHAO : "../static/images/file/text.png"
};

// 上传文件操作
function uploadListAllOpt(){

    // 新建上传文件
    $("#uploadFile").change(function(){
        var files = this.files;
        if (files.length > 0){
            for (var i = 0,len = files.length; i < len; i++){
                var file = files[i];
                var _fileName = file.name;

                var flag  = etectionFile(file,formFileList,_fileName,editAttList);

                if (flag != false){
                    formFileList.push(file);

                    //列表展示
                    var name =  JSON.stringify({fileName:_fileName}).replace(/"/g, '&quot;');
                    var _tr = '';
                    var index = _fileName .lastIndexOf("\.");
                    var file_type  = _fileName .substring(index + 1, _fileName.length);
                    var _td_icon = '';
                    var _td_name = '<span>'+_fileName+'</span><i class="file-Id"></i></div></td>';
                    var _td_opt = '<td class="font_right"><a href="javascript:void(0);" class="del-file-button" onclick="removeFile('+name+',this)">删除</a></td>';
                    _td_icon =  classPath(file_type,_td_icon);
                    _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
                    $("#newFileTable").append(_tr);
                }
            }
        }
        resetFile()
    });

    // 备注上传文件
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
                    var _td_icon = '';
                    var _td_name = '<span>'+_fileName+'</span><i class="file-Id"></i></div></td>';
                    var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="removeFile('+name+',this)">删除</a></td>';
                    _td_icon =  classPath(file_type,_td_icon);
                    _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
                    $("#optFileTable").append(_tr);
                }
            }
        }

        resetFile();
    });
}

// 附件列表
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
                if (idName == "#check_table"){
                    var obj =  JSON.stringify({filePath:row.filePath,"fileNameOld":row.fileNameOld}).replace(/"/g, '&quot;');
                    return '<div class="fileTb" style="cursor:pointer" >'+classType+'    <a download="'+1+'" onclick="downloadFile('+obj+')">'+row.fileNameOld+'</a></div>';
                } else if (idName == "#newFileTable"){
                    return '<div class="fileTb">'+classType+'    '+row.fileNameOld+'</div>';
                }
            }
        },{
            field : "opt",
            title : "操作",
            align : 'center',
            formatter : function(value, row, index) {
                if (idName == "#check_table"){
                    return '';
                } else if (idName == "#newFileTable"){
                    if(row.id == null){
                        var name =  JSON.stringify({fileName:row.fileNameOld}).replace(/"/g, '&quot;');
                        return '<div class="span_a_style"><span class="a_style" onclick="removeFile('+name+',this)">删除</span></div>';
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
            });
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
        url:defectUrl+"findDefectWithAttList",
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

                if(data.defectInfo != null ){
                    if( defectOpt.submitDefectStatus == "edit"){
                        editMessage(data.defectInfo);
                    }
                }

                var att = data.defectInfo.defectDrcAttachementList;
                attTable(att,FileTable);
                if (att != null && att.length > 0){
                    for( var i = 0,len = att.length;i < len ; i++){
                        editAttList.push(att[i].fileNameOld);
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

//移除未上传到后台的文件
function removeFile(name,that){
    $("#edit_newFileTable").bootstrapTable('remove',{
        field:"fileNameOld",
        values:[name.fileName]
    });
    $(that).parent().parent().remove();
     removeFileName(name.fileName);
}

// 删除附件
function deleteAtt(rows,that,index){
    $(fileTableName).bootstrapTable('remove',{
        field:"fileNameOld",
        values:[rows.fileNameOld]
    });
    defectAttIds.push(rows.id);
    removeFileName(rows.fileNameOld);
}

function removeFileName(fileName){
    formFileList.splice(formFileList.indexOf(fileName),1);
    editAttList.splice(editAttList.indexOf(fileName),1);
}

// 提取文件图片显示
function classPath(fileType,classType){
    switch (fileType){
        case "doc":
        case "docx":
            classType = '<img src="'+ filePicAddress._icon_word +'" />';
            break;
        case "xls":
        case "xlsx":
            classType = '<img src="'+ filePicAddress._icon_excel +'" />';
            break;
        case "txt":
            classType = '<img src="'+ filePicAddress._icon_text +'" />';
            break;
        case "pdf":
            classType = '<img src="'+ filePicAddress._icon_pdf +'" />';
            break;
        case "JPG":
        case "jpg":
        case "gif":
            classType ='<img src="'+ filePicAddress._icon_JPG +'" />';
            break;
        case "bmp":
        case "BMP":
            classType ='<img src="'+ filePicAddress._icon_BMP +'" />';
            break;
        case "PNG":
        case "png":
            classType ='<img src="'+ filePicAddress._icon_PNG +'" />';
            break;
        default:
            classType ='<img src="'+ filePicAddress._icon_WENHAO +'" />';
            break;
    }
    return classType;
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
        layer.alert(_fileNameSpan+'：超过10MB！', {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }

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

// 下载文件
function downloadFile(row){
    var url = "/zuul/system/file/downloadFile?filePath="+row.filePath+"&fileNameOld="+row.fileNameOld;
    window.location.href = encodeURI(url);
}

//清空文件
function resetFile(){
    $("#opt_uploadFile").val("");
    $("#uploadFile").val("");
}