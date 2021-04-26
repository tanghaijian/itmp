
//markdown编辑器，拖拽式复制图片
function initPasteDragImg(Editor) {
	Editor.addEventListener('paste', function (event) {
		// var files = event.dataTransfer.files;
		var items = (event.clipboardData || window.clipboardData).items;
		var file = null;
		if (items && items.length) {
			// 搜索剪切板items
			for (var i = 0; i < items.length; i++) {
				if (items[i].type.indexOf('image') !== -1) {
					file = items[i].getAsFile();
					break;
				}
			}
		} else {
			console.log("当前浏览器不支持");
			return;
		}
		if (!file) {
			console.log("粘贴内容非图片");
			return;
		}
		uploadImg(file);
	});
	Editor.addEventListener("dragover", function (e) {
		e.preventDefault()
		e.stopPropagation()
	})
	Editor.addEventListener("dragenter", function (e) {
		e.preventDefault()
		e.stopPropagation()
	})
	Editor.addEventListener("drop", function (e) {
		e.preventDefault()
		e.stopPropagation()
		var files = this.files || e.dataTransfer.files;
		uploadImg(files[0]);
	})
}

//上传图片到后台
function uploadImg(files) {
	var fileData = files;//获取到一个FileList对象中的第一个文件( File 对象),是我们上传的文件
	var fileName = fileData.name.split('.')[1];
	if (!/(png|jpg|jpeg|gif|bmp|ico)$/.test(fileName)) {
		var formData = new FormData();
//	    for (var i = 0; i < files.length; i++) {
//	        formData.append('files', $('#opt_uploadFile')[0].files[i]);
//	    }
	    formData.append('files', fileData);
	    formData.append("systemDirectoryDocumentId", chapter_content.systemDirectoryDocumentId);
	    formData.append("systemDirectoryDocumentChaptersId",chapter_content.id);
	    formData.append("attachmentType", 1);
	    $("#loading").css('display', 'block');
	    $.ajax({
	        url: "/zuul/projectManage/documentLibrary/uploadChaptersFiles",
	        type: "post",
	        data: formData,
	        processData: false,
	        contentType: false,
	        success: function (data) {
	        	//回显到编辑器
	        	if(data.status == 1){
		            var file_html = '<a class="a_style" href="/projectManage/assetsLibraryRq/downAtts?attachmentS3Bucket='+data.attachmentS3Bucket+
						'&attachmentS3Key='+data.attachmentS3Key+'&attachmentNameOld='+data.attachmentNameOld+'" download="'
						+data.attachmentNameOld+'">'+data.attachmentNameOld+'</a>';
		            editor2.appendHtml(file_html);
					editor2.loadPlugin('autoheight');
					layer.alert('上传成功 ！', {
		                icon: 1,
		                title: "提示信息"
		            });
					$('#importModal').modal('hide');
	        	}
	            $("#loading").css('display', 'none');
	        },
	        error: errorFunMsg
	    })
	}else{ //上传图片
		var reader = new FileReader();
		reader.readAsDataURL(fileData);//异步读取文件内容，结果用data:url的字符串形式表示
		reader.onload = function (e) {
			var base_img = this.result;
			editor2.appendHtml('<p><img class="img_style" src="' + base_img + '" /></p>');
			editor2.loadPlugin('autoheight');
			$('#importModal').modal('hide');
		}
	}
}