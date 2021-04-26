// 文件上传列表格式
function uploadFileList(){
    $("#uploadFile").change(function(){
    	var files = this.files;    	
        if (files.length > 0){
            for (var i = 0,len = files.length; i < len; i++){            	
		        var file = files[i];
		        var _fileName = file.name;
		
		        var flag = etectionFile(file,formFileList,_fileName,null);
		        if (flag != false ){
		        	
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
			        var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
			        _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
			        _table.append(_tr);
		        }
            }
        }
        resetFile();
    });
}

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
			        $("#editFileTable").bootstrapTable('prepend', {
			            fileNameOld:file.name,
			            fileType:file_type,
			            id:null
			        })
		        }		        	        		        
        	}       	
        }
        resetFile();
    });
}

// 文件上传
function uploadFile(reqId,requirementStatus){
    /*var opt ='';
    if (modalType == "new"){
        opt ="新增";
    } else if( modalType == "edit"){
        opt ="修改";
    }*/
    if (formFileList.length <= 0){        
        if (modalType == "new"){
        	$("#loading").css('display','none');
            layer.alert('新增需求成功！', {
                icon: 1,
                title: "提示信息"
            });
            $("#newReq").modal("hide");  
            pageInit();
        } else if(modalType == "edit"){
        	deleteAtt(delFile,requirementStatus);
        }   
        return false;
    }
    var files = new FormData();
    
    var remarkFilesSize = 0;
    for (var i = 0,len2 = formFileList.length;i < len2;i++){
    	remarkFilesSize += formFileList[i].size;
        files.append("files",formFileList[i]);
    }
    if(remarkFilesSize > 1048576000){
        layer.alert('上传文件总体不能超过1000M,请删选！！！', {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }
    $("#loading").css('display','block');
    $.ajax({
        type: "post",
        url:'/zuul/projectManage/requirement/uploadFile?reqId='+reqId,
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
                formFileList = [];
                editAttList = [];
                if (modalType == "new"){
                	layer.alert('新增需求成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    $("#newReq").modal("hide");  
                    pageInit();
                } else if(modalType == "edit"){                    
                    deleteAtt(delFile,requirementStatus);
                }              
            }           
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert("请检查上传文件信息", {
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

//移除上传文件
function removeFile(name,that){
    $("#editFileTable").bootstrapTable('remove',{
        field:"fileNameOld",
        values:[name.fileName]
    });
    $(that).parent().parent().remove();
        
    for (var i = 0,len1 = formFileList.length;i < len1;i++){
        if (formFileList[i].name == name.fileName){
            formFileList.splice(i,1);
            break;
        }
    }
    for (var ii = 0,len1 = editAttList.length;ii < len1;ii++){
        if (editAttList[ii] == name.fileName){
        	editAttList.splice(ii,1);
            break;
        }
    }
}

//移除上传文件
function removeFile1(name,id,that){
	
    $("#editFileTable").bootstrapTable('remove',{
        field:"fileNameOld",
        values:[name.fileName]
    });
    $(that).parent().parent().remove();
    for (var i = 0,len1 = formFileList.length;i < len1;i++){
        if (formFileList[i].name == name.fileName){
            formFileList.splice(i,1);
            break;
        }
    }
    for (var ii = 0,len1 = editAttList.length;ii < len1;ii++){
        if (editAttList[ii] == name.fileName){
        	editAttList.splice(ii,1);
            break;
        }
    }
    delFile.push(id);
}
// 附件列表后台接口
function attList(att,FileTable){     
    attTable(att,FileTable);
    if (att != null && att.length > 0){
        for( var i = 0,len = att.length;i < len ; i++){
            editAttList.push(att[i].fileNameOld);
        }
    }     
}

// 下载文件
function downloadFile(row){
    var url = "/zuul/projectManage/requirement/downloadFile?filePath="+row.filePath+"&fileS3Bucket="+
    row.fileS3Bucket+"&fileNameOld="+row.fileNameOld+"&fileS3Key="+row.fileS3Key;
    
    window.location.href = encodeURI(url);
}

// 删除附件
function deleteAtt(ids,status){
    $("#loading").css('display','block');
    if (ids.length <= 0){ 
    	$("#loading").css('display','none');   
    	layer.alert('修改需求成功！', {
            icon: 1,
            title: "提示信息"
        });    			
    	$("#editReq").modal("hide");
    	pageInit();
        return false;
    }
    $.ajax({
        url:"/zuul/projectManage/requirement/removeAtt",
        method:"post",
        traditional:true,
        data:{
            ids:ids
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
            	layer.alert('修改需求成功！', {
                    icon: 1,
                    title: "提示信息"
                });              	
                $("#editReq").modal("hide");
            	pageInit();
            }                       
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert("请检查删除文件信息", {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

// 提取文件图片显示
function classPath(fileType,classType){
    switch (fileType){
        case "doc":
        case "docx":
            classType = '<img src="'+_icon_word+'" />';
            break;
        case "xls":
        case "xlsx":
            classType = '<img src="'+_icon_excel+'" />';
            break;
        case "txt":
            classType = '<img src="'+_icon_text+'" />';
            break;
        case "pdf":
            classType = '<img src="'+_icon_pdf+'" />';
            break;
        case "JPG":
        case "jpg":
            classType ='<img src="'+_icon_JPG+'" />';
            break;
        case "PNG":
        case "png":
            classType ='<img src="'+_icon_PNG+'" />';
            break;
        default:
            classType ='<img src="'+_icon_WENHAO+'" />';
            break;
    }
    return classType;
}

// 检测文件
function etectionFile(file,formFileList,_fileName,editAttList){
    var _fileSize = file.size;
    if (_fileSize <= 0){
        layer.alert('不能上传空文件！', {
            icon: 2,
            title: "提示信息"
        });        
        return false;
    }

    if(_fileSize > 10485760){
        layer.alert(_fileName+'超过10M,无法上传！', {
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
            layer.alert("文件" + _fileName +" 读取出现错误", {
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
                var _fileNameSpan = '<span style="color: red">'+_fileName+'</span>';
                layer.alert(_fileNameSpan +"  文件已存在，请勿重新添加", {
                    icon: 2,
                    title: "提示信息"
                });                
                return false;
            }
        }
    }

    if (editAttList != null){
        for(var i = 0,len = editAttList.length;i < len;i++){
            if(_fileName == editAttList[i]){
                var _fileNameSpan = '<span style="color: red">'+_fileName+'</span>';
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
    $("#edit_uploadFile").val("");
    $("#uploadFile").val("");
} 