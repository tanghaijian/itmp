/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 * 新建类项目-管理-文档-md文档类型页面js
 */
$(()=>{
	
})

// 章节关联所有附件（more）
function mkd_more_ChaptersFiles(ID,status) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentLibrary/selectChaptersFiles",
        type: "post",
        data: {
             id: chapter_id// 章节id
        },
        success: function (data) {
            if (data.status == 1) {
            	if(status == 0){
                }else{
                	$('#accessory_list_tbody').empty();
                	data.files.map((val,idx)=>{
                		$('#accessory_list_tbody').append(`
                            	<tr class="">
									<td class="file_Table_style">${val.attachmentNameOld}</td>
									<td style="width:50px;">
										<a class="a_style" href="/projectManage/assetsLibraryRq/downAtts?attachmentS3Bucket=${val.attachmentS3Bucket}
										&attachmentS3Key=${val.attachmentS3Key}&attachmentNameOld=${val.attachmentNameOld}" download="${val.attachmentNameOld}">下载</a>
									</td>
								</tr>
                    	`);
	                })
//	                window.location.href = "/projectManage/assetsLibraryRq/downAtts?attachmentS3Bucket="+attachmentS3Bucket + 
//										 "&attachmentS3Key="+attachmentS3Key+ "&attachmentNameOld="+attachmentNameOld;
	                $('#accessory_all_Modal').modal('show');
                }
                $("#loading").css('display', 'none');
            }
        },
        error: function (data) {
//            console.log(data);
        }
    })
}

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
    }
    formData.append("systemDirectoryDocumentId", parameterArr.obj.id);
    formData.append("systemDirectoryDocumentChaptersId",chapter_id);
    formData.append("attachmentType", 1);
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/zuul/projectManage/documentLibrary/uploadChaptersFiles",
        type: "post",
        data: formData,
        processData: false,
        contentType: false,
//        dataType: "json",
        success: function (data) {
//            if (data.status == 1) {
                layer.alert('上传成功 ！', {
                    icon: 1,
                    title: "提示信息"
                });
                $('#opt_uploadFile').val('');
                select_Files_list(1);
                $("#importModal").modal("hide");
                $("#loading").css('display', 'none');
//            } else {
////                layer.alert('内部错误！', {
////                    icon: 2,
////                    title: "提示信息"
////                });
//            }
        },
        error: errorFunMsg
    })
}

//附件列表
function select_Files_list(status) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentLibrary/selectChaptersFiles",
        type: "post",
        data: {
             id: chapter_id// 章节id
        },
        success: function (data) {
            if (data.status == 1) {
        		$('#file_Table_tbody').empty();
            	data.files.map((val,idx)=>{
            		$('#file_Table_tbody').append(`
                        	<tr>
								<td class="file_Table_style">${val.attachmentNameOld}</td>
								<td style="width:150px;">
									<textarea id="copy_File_btn_${val.id}" style="height: 0;width: 0;">${val.attachmentUrl}</textarea>
									<a class="a_style" onclick="copy_File_url(copy_File_btn_${val.id})">复制文件地址</a>&nbsp;&nbsp;|&nbsp;&nbsp;
									<a class="a_style" onclick='removeChaptersFile(${JSON.stringify(val)})'>删除</a>
								</td>
							</tr>
                	`);
                })
                if(status == 2){
                	$("#import_list_Modal").modal('show');
                }
                $("#loading").css('display', 'none');
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
//	window.clipboardData.setData("Text","");
	
	document.execCommand("delete");
	ele.select(); // 选择对象
	document.execCommand("Copy"); // 执行浏览器复制命令
//	window.clipboardData.setData("Text",$(ele).text());
	console.log("已复制好，可贴粘。");
}

//章节移除附件
function removeChaptersFile(row) {
	$("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentLibrary/removeChaptersFile",
        type: "post",
        data: {
            id: row.id //参数附件id
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
