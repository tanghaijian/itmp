/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 */

// 导入附件
function add_file() {
	$('#opt_uploadFile').val('');
    $("#importModal").modal("show");
}

//提交附件
function import_files() {
	if(!$('#opt_uploadFile').val()){
		layer.alert('请选择上传文件 ！', {
            icon: 0,
            title: "提示信息"
        });
		return;
	}
	var formData = new FormData();
    var files = $('#opt_uploadFile')[0].files;
    console.log(files);
    for (var i = 0; i < files.length; i++) {
        formData.append('files', $('#opt_uploadFile')[0].files[i]);  // 1
        formData.append('fileName', $('#opt_uploadFile')[0].files[i].name);
    }
    formData.append("systemDirectoryDocumentId", parameterArr.obj.id);
    formData.append("systemDirectoryDocumentChaptersId",chapter_id );
    formData.append("attachmentType", 1);
 	formData.append("requirementCode", cs_requirementCode);
	formData.append("currentUserAccount", currentUserAccount);
	formData.append("featureCode", featureCode);
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/itRedocumentLibrary/uploadChaptersFiles",
        type: "post",
        data: formData,
        processData: false,
        contentType: false,
//        dataType: "json",
        success: function (data) {
           if (data.status == 1) {
                layer.alert('上传成功 ！', {
                    icon: 1,
                    title: "提示信息"
                });
                $('#opt_uploadFile').val('');
                select_Files_list(1);
                $("#importModal").modal("hide");
           } else {
               layer.alert('内部错误！', {
                   icon: 2,
                   title: "提示信息"
               });
           }
           $("#loading").css('display', 'none');
        },
        error: errorFunMsg
    })
}

//附件列表
function select_Files_list(status) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/itRedocumentLibrary/selectChaptersFiles",
        type: "post",
        data: {
             id: chapter_id// 章节id
        },
        success: function (data) {
            if (data.status == 1) {
        		$('#file_Table_tbody').empty();
            	data.files.map(function(val,idx){
                    var data_row = JSON.stringify(val).replace(/"/g, '&quot;');
                    var url = "/projectManage/assetsLibraryRq/downAtts?attachmentS3Bucket=" + encodeURIComponent(val.attachmentS3Bucket) 
                    + "&attachmentS3Key=" + encodeURIComponent(val.attachmentS3Key) + "&attachmentNameOld=" + encodeURIComponent(val.attachmentNameOld);
                    var str_htm = '<tr><td class="file_Table_style"><a class="a_style" href="' + url + '" download="' + val.attachmentNameOld + '">' 
                	+ val.attachmentNameOld + '</a></td>'+
//                    var str_htm = '<tr><td class="file_Table_style">'+ val.attachmentNameOld + '</td>'+
                        '<td style="width:150px;">'+
                        '<textarea id="copy_File_btn_'+val.id+'" style="height: 0;width: 0;">'+val.attachmentUrl+'</textarea>'+
                        '<a class="a_style" onclick="copy_File_url(copy_File_btn_'+val.id+')">复制文件地址</a>&nbsp;&nbsp;|&nbsp;&nbsp;'+
                        '<a class="a_style" onclick="removeChaptersFile('+data_row+')">删除</a>'+
                        '</td></tr>';
            		$('#file_Table_tbody').append(str_htm);
                })
                if(status == 2){
                	$("#import_list_Modal").modal('show');
                }
            }
            $("#loading").css('display', 'none');
        },
        error: function (data) {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误!", {
					icon: 2,
					title: "提示信息"
			});
		}
    })
}

//复制文件地址
function copy_File_url(ele){
	document.execCommand("delete");
	ele.select(); // 选择对象
	document.execCommand("Copy"); // 执行浏览器复制命令
	console.log("已复制好，可贴粘。");
}

//章节移除附件
function removeChaptersFile(row) {
	$("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/itRedocumentLibrary/removeChaptersFile",
        type: "post",
        data: {
            id: row.id, //参数附件id
            "currentUserAccount":currentUserAccount
        },
        success: function (data) {
            if (data.status == 1) {
            	select_Files_list(1);
            	layer.alert('删除成功 ！', {
                    icon: 1,
                    title: "提示信息"
                });
            }
            $("#loading").css('display', 'none');
        },
        error: function (data) {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误!", {
					icon: 2,
					title: "提示信息"
			});
		}
    })
}

