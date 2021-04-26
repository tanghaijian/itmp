/**
 * Description: 关于文件的各种操作
 * Author:xukai
 * Date: 2018/12/24 下午 2:06
 */
var defectUrl = '/devManage/';

var errorDefect = '请仔细检查数据！！！';
var modalType="";
var deleteAttaches = [];

var editAttList = [];
var formFileList = [];
var defectFormFileList = [];// 缺陷上传文件
var defectAttIds = [];

var _files = [];

var _seefiles=[];
var _newSeefiles=[];
// 文件上传
function uploadFile(defectId,logId) {
    if (formFileList.length <= 0) {

        if (defectAttIds.length > 0) {
            deleteAtts(defectId, logId);
        } else {
            $("#loading").css('display', 'none');
            layer.alert('操作成功！', {
                icon: 1,
                title: "提示信息"
            });
            $("#commitBug").modal("hide");
            $(".rightCaseDivNew").css("display", "none");
            //reset_opt();
        }
        initTable();
        return false;
    }
    var files = new FormData();
    var filesSize = 0;
    for (var i = 0, len2 = formFileList.length; i < len2; i++) {
        filesSize += formFileList[i].size;
        files.append("files", formFileList[i]);
    }

    if (filesSize > 1048576000) {
        layer.alert('文件太大,请删选！！！', {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }

    $("#loading").css('display', 'block');
    $.ajax({
        type: "post",
        url: '/zuul' + defectUrl + 'defect/updateFiles?id=' + defectId + "&logId=" + logId,
        dataType: "json",
        data: files,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                if (defectAttIds.length > 0) {
                    deleteAtts(defectId, logId);
                } else {
                    layer.alert('操作成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    getAllExecuteCase();
                    $("#commitBug").modal("hide");

                    // reset_opt();
                }
                formFileList = [];
                editAttList = [];
                initTable();
            }

        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })
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
                var att = data.attList;
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


//移除上传文件
function bugDelFile(name,that){
    $("#newFileTable").bootstrapTable('remove',{
        field:"fileNameOld",
        values:[name.fileName]
    });
    $(that).parent().parent().remove();
    removeFileName(name.fileName);

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

    if (editAttList != null){
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


/*测试执行的附件*/
function upFileList(){ 
	//列表展示
	$(".myUpfile").change(function(){
		var files = this.files;
		var formFile = new FormData();
		var filesSize = 0;
		for (var i = 0,len2 = files.length;i < len2;i++){
	        filesSize += files[i].size;
	      //  files.append("files",formFileList[i]);
	    }
		if(filesSize > 104857600){
	        layer.alert('文件太大,请删选！！！', {
	            icon: 2,
	            title: "提示信息"
	        });
	        return false;
	    }
		outer:for(var i=0,len=files.length;i<len;i++){
			var file = files[i];
			if(file.size<=0){
				layer.alert(file.name+"文件为空！", {icon: 0});
				continue;
			}
			if(file.size > 10485760){
	            layer.alert(file.name+'：超过10MB！', {
	                icon: 2,
	                title: "提示信息"
	            });
	            return false;
	        }
			var fileList = [];
			var oldFileList = [];
			if(modalType == 'new'){
				fileList=_files;
			}else if(modalType == 'seeFile'){
        		oldFileList=_seefiles;
        		fileList=_newSeefiles;
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
							console.log("文件 "+file.name+" 读取成功！");
						}
					}; 
				})(i);   
			}  
			//列表展示
			var _tr = '';
			var file_type = file.name.split("\.")[1];
            var _td_icon = filePicClassPath(file_type); // common.js
			var _td_name = '<span >'+file.name+'</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
			var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteFile(this)" style="color:red">删除</a></td>';
			var _table=$(this).parent().parent().parent().parent().parent().next(".fileDiv").children(".fileTable");
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
					}else if(modalType == 'seeFile'){
						_newSeefiles.push(data[k]);
	        		}
					$(".fileTable span").each(function(o){ 
						if($(this).text() == data[k].fileNameOld){
							$(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
							$(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
						}
					});
				}
				if(modalType == 'new'){
					$("#files").val(JSON.stringify(_files));
					clearUploadFile('newUploadFile');
				}else if(modalType == 'seeFile'){
	        		$("#seeFiles").val(JSON.stringify(checkUploadFile));
	        		clearUploadFile('');
	        	}
			},
			error:errorFunMsg
		});
	});

    $("#uploadFile").change(function(){
        var files = this.files;
        if (files.length > 0){
            for (var i = 0,len = files.length; i < len; i++){
                var file = files[i];
                var _fileName = file.name;

                var flag = etectionFile(file,defectFormFileList,_fileName,null);
                if (flag != false){
                    defectFormFileList.push(file);

                    //列表展示
                    var name =  JSON.stringify({fileName:_fileName}).replace(/"/g, '&quot;');
                    var _tr = '';
                    var index = _fileName .lastIndexOf("\.");
                    var file_type  = _fileName .substring(index + 1, _fileName.length);
                    var _td_icon = filePicClassPath(file_type); // common.js
                    var _td_name = '<span>'+_fileName+'</span><i class="file-Id"></i></div></td>';
                    var _td_opt = '<td class="font_right"><a href="javascript:void(0);" class="del-file-button"  onclick="bugDelFile('+name+',this)" >删除</a></td>';
                    var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                    _tr+='<tr><td><div class="fileTb" style="width: 600px;">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
                    _table.append(_tr);
                }
            }
        }
        resetFile();
    });
}

//删除已经存在
function deleteAttachements(that,attache){
    $(that).parent().parent().remove();
    var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
    removeFile(fileS3Key);
    deleteAttaches.push(attache);
    $.ajax({
        type:'post',
        url:'/testManage/testExecution/delFile',
        data:{
            id:attache.id
        },
        success:function(data){
            if(data.status=="1"){
                layer.alert('删除成功！', {
                    icon: 1,
                    title: "提示信息"
                });
            }else if(data.status == "2"){
                layer.alert('删除失败！', {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error:errorFunMsg
    });
}

//移除上传文件
function deleteFile(that){
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
		
	}
}
//清空上传文件
function clearUploadFile(idName){
	$('#'+idName+'').val('');
}

function download(row){
	var fileS3Bucket = row.fileS3Bucket;
	var fileS3Key = row.fileS3Key;
	var fileNameOld = row.fileNameOld;
	window.location.href = "/testManage/worktask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld;
}