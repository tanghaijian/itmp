
//markdown编辑时，拖拽粘贴图片
function initPasteDragImg(Editor){
    var doc = document.getElementById(Editor.id)
    doc.addEventListener('paste', function (event) {
//    	var files = this.files || e.dataTransfer.files;
//    	console.log(files)
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
        console.log(file)
        uploadImg(file,Editor);
    });
    var dashboard = document.getElementById(Editor.id)
    dashboard.addEventListener("dragover", function (e) {
        e.preventDefault()
        e.stopPropagation()
    })
    dashboard.addEventListener("dragenter", function (e) {
        e.preventDefault()
        e.stopPropagation()
    })
    dashboard.addEventListener("drop", function (e) {
        e.preventDefault()
        e.stopPropagation()
     var files = this.files || e.dataTransfer.files;
     uploadImg(files[0],Editor);
     })
}

//上传图片到后台
function uploadImg(file,Editor){
    var fileData = file;//获取到一个FileList对象中的第一个文件( File 对象),是我们上传的文件
    var fileName  = fileData.name.split('.')[1];
    if(/\.(png|jpg|jpeg|gif|bmp|ico)$/.test(fileName)){
    	layer.alert("上传非图片!",{icon:0});
        return;
    }
    var reader = new FileReader();
    reader.readAsDataURL(fileData);//异步读取文件内容，结果用data:url的字符串形式表示
    reader.onload = function(e) {
   	var base_img = this.result;
	      $.ajax({
		        url: '/zuul/projectManage/systemDirectoryDocumentOperate/mkdUpload',
		        type: 'post',
		        data:{
		        	fileType:fileName,
               	 	base64Code:base_img.split(',')[1],
                },
		        success: function (msg) {
		        	if(msg.code == 1){
		        		//上传成功后，回显到编辑器
		        		var url = 'http://' + document.location.host +'/projectManage/systemDirectoryDocumentOperate/queryPic/' + msg.s3Key;
	                    Editor.insertValue("![图片alt]("+url+" ''图片title'')");
//		                if(/\.(png|jpg|jpeg|gif|bmp|ico)$/.test(url)){
//		                    Editor.insertValue("![图片alt]("+url+" ''图片title'')");
//		                }else{
//		                    Editor.insertValue("[下载附件]("+url+")");
//		                }  
		        	}
		        }
		    });
	  }
}