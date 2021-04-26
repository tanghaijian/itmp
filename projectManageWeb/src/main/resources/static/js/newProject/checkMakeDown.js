/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 */


/**
 * @description 全局变量
 */
var testEditormdView1,testEditormdView2,parameterArr = {},chapter_id = '',chapter_content = {},chartpt_all_length = '';
var mydata = {};
/**
 * @description 章节树配置
 */
var setting_a_charts = {
	data: {
		simpleData: {
			enable: true
		}
	},
	view:{
		nameIsHTML: true
	}
};
parameterArr.arr = window.location.href.split("?");
parameterArr.parameterArr = parameterArr.arr[1].split(",");
parameterArr.obj = {};
for (var i = 0; i < parameterArr.parameterArr.length; i++) {
    var obj = parameterArr.parameterArr[i].split("=");
    parameterArr.obj[obj[0]] = obj[1];
}
var readAuth = parameterArr.obj.readAuth == 1 ? true : false;//读权限
var writeAuth = parameterArr.obj.writeAuth == 1 ? true : false;//写权限
var exportUrl = '';
/**
 * @description 章节树配置
 */
if(parameterArr.obj.type == 2){   // 读权限
	$('.writeAuth_btn').hide();
	mydata = {
	    treeArr: [],    //用来储存，点击取得树节点及其所有的祖先节点,显示在头部
	    treeSetting: {   // 树的配置信息
			check: {
				enable: true
			},
	        data: {
	            simpleData: {
	                enable: true
	            },
	            key: {
	                name: "chaptersName"
	            },
	        },
	        treeNode: {
	        	order:'chaptersOrder'
	        },
	        callback: {
	            onClick: getDocumentTable,
	        }
	    }
	}
}else{     // 读写权限
	mydata = {
	    treeArr: [],    //用来储存，点击取得树节点及其所有的祖先节点,显示在头部
	    treeSetting: {   // 树的配置信息
			check: {
				enable: true
			},
	        data: {
	            simpleData: {
	                enable: true
	            },
	            key: {
	                name: "chaptersName"
	            },
	        },
	        treeNode: {
	        	order:'chaptersOrder'
	        },
	        edit : {
                enable: true,
                editNameSelectAll: true,
                drag:{
                	prev : true,
                	inner : true,
                	next : true,
                },
                showRenameBtn: true,
                showRemoveBtn: true
            },
	        view : {
//	        	    addDiyDom: addDiyDom,
                showIcon: false,
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom,
                // selectedMulti: false
        	},
	        callback: {
	            onClick: getDocumentTable,
	            beforeRemove: beforeRemove,
	            beforeRename: beforeRename,
	            beforeDrop: beforeDrop,
	        }
	    }
	}
}

$(document).ready(function () {
    init();
    $(".headReturn").on("click", function () {
    	window.localStorage.removeItem('showMakeDownFlag');
        localStorage.setItem('showMakeDownFlag',true);
        window.parent.layer.closeAll();
        window.parent.location.reload();
    })
    $("#mkd_remark_btn").on("click", function () {
    	$("#mkd_remark_modal").modal('show');
    })
    $("#save_mkd_remark").on("click", function () {
    	$("#mkd_remark_modal").modal('hide');
    })
    
    //新建一级章节
    $("#addNotic_btn").click(function () {
        $("#add_article_id").attr('htype','1');
        $("#add_article_modal").modal('show');
    })
	$("#export_btn").click(function () {
		var newCheckArr =[];
		var zTreeOjb = $.fn.zTree.getZTreeObj("docTree");   
		//获取复选框/单选框选中的节点：
		var checkedNodes = zTreeOjb.getCheckedNodes();
		if(!checkedNodes.length ){
			layer.alert("请选择导出章节!", {
                icon: 2,
                title: "提示信息"
            });
			return;
		}else{
			checkedNodes.map(function(item,value){ 
//				if( !item.isParent ){
					newCheckArr.push( item.id );
//				}
			})
		} 
		var newCheckStr = JSON.stringify( newCheckArr ); 
		exportUrl = "/projectManage/documentChapters/export?systemDirectoryDocumentId="+ parameterArr.obj.id +"&idStr="+newCheckStr; 
		$("#export_Modal").modal("show");  
		//选择章节导出
		//url : /projectManage/documentChapters/exportWord
		//参数:systemDirectoryDocumentId(文档id)、idStr(数组的json字符串)
    })
    formValidator();
    refactorFormValidator();
});

/**
 * @description 导出提交
 */
function exportCommit(){
	if( $('#ulFileExport input[name="file"]:checked').val()==undefined ) {
		layer.alert("请选择!", {
            icon: 0,
            title: "提示信息"
        });
	}else{
		exportUrl += "&type="+ $('#ulFileExport input[name="file"]:checked').val();
    }
    $("#loading").css('display', 'block');
	window.location.href = exportUrl;
    $("#export_Modal").modal("hide");
    $("#loading").css('display', 'none');
}

/**
 * @description 签出
 */
function sign_off_btn() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/systemDirectoryDocumentOperate/signOff",
        type: "post",
        data: {
            id:chapter_id,
        },
        success: function (data) {
        	$('#signBtn').addClass('disableBtn');
            $('#signBtn').removeAttr('onclick');
            $('#add_mkd_remark').val('');
            $("#editDiv").css("display", "flex");
            $(".requirement").css("display", "none");
            $(".editHeadDiv").css("display", "block");
            $(".checkHeadDiv").css("display", "none");
            $("#mkd_remark_btn").css("display", "inline-block");
            var mdddm = data.content;
            $("#editDiv").empty();
            $("#editDiv").append("<div id='test-editormd-view2'></div>");
            testEditormdView2 = editormd("test-editormd-view2", {
                markdown: mdddm,
                path: "/systemui/static/js/frame/editor/",
                theme: "eclipse",
                previewTheme: "eclipse",
                editorTheme: "eclipse",
                codeFold: true,
                saveHTMLToTextarea: true,    // 保存 HTML 到 Textarea
                searchReplace: true,
                htmlDecode: "style,script,iframe|on*",            // 开启 HTML 标签解析，为了安全性，默认不开启
                emoji: true,
                taskList: true,
                tocm: true,         // Using [TOCM]
                tex: true,                   // 开启科学公式TeX语言支持，默认关闭
                flowChart: true,             // 开启流程图支持，默认关闭
                sequenceDiagram: true,       // 开启时序/序列图支持，默认关闭,
                /**上传图片相关配置如下*/
    	        imageUpload : true,
    	        imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
    	        imageUploadURL : "/projectManage/systemDirectoryDocumentOperate/mkdUpload", //这个是上传图片时的访问地址
                onload: function () {
                     initPasteDragImg(this); //必须
                     
                }
            });
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

//暂存  提交
function mkd_submit(status) {
    var url_pra = 'submit';
    let submit_data = {};
    if (status == 1) {      //暂存
        url_pra = 'temporaryStorage';
    }
    if(parameterArr.obj.status == 2){
    }
    if (status == 3) {      //取消
        url_pra = 'cancel';
        submit_data = {
        		id:chapter_content.id,
        }
    }else{
    	let mkd_cont = $('.editormd-markdown-textarea').val();
    	chapter_content.content = mkd_cont;
    	var converter = new showdown.Converter();
    	var _dcode = converter.makeHtml(mkd_cont);
    	submit_data = {
                contentHtml:HTMLEncode(_dcode),//编码
        		systemDirectory:JSON.stringify(chapter_content),
        }
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/systemDirectoryDocumentOperate/" + url_pra,
        type: "post",
        data: submit_data,
        success: function (data) {
        	if(data.code == 1){
        		getTree();
        		if (status == 1) {   
        			layer.alert("暂存成功!", {
    	                icon: 1,
    	                title: "提示信息"
    	            });
        	    }else if (status == 2) {    
        	        layer.alert("提交成功!", {
    	                icon: 1,
    	                title: "提示信息"
    	            });
        	    }else if (status == 3) {  
        	    	if(parameterArr.obj.status == 2){
        	    		layer.alert("已取消编辑!", {
        	                icon: 1,
        	                title: "提示信息"
        	            });
        	    	}else{
        	    		layer.alert("已取消签出!", {
        	                icon: 1,
        	                title: "提示信息"
        	            });
        	    	}
        	    }
        	}else{
        		layer.alert("系统错误,请联系管理员!", {
	                icon: 2,
	                title: "提示信息"
	            });
        	}
            $("#editDiv").hide();
            $(".requirement").show();
            $(".editHeadDiv").hide();
            $(".checkHeadDiv").show();
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

function init() {
    $("#loading").css('display', 'block');
    getTree(2)
    if (parameterArr.obj.status == 1) {
        //查看
        $("#editDiv").css("display", "none");
        $(".requirement").css("display", "flex");
        $(".editHeadDiv").css("display", "none");
        $(".checkHeadDiv").css("display", "block");
        testEditormdView1 = editormd.markdownToHTML("test-editormd-view1", {
            htmlDecode: "style,script,iframe",  // you can filter tags decode
            emoji: true,
            taskList: true,
            tex: true,  // 默认不解析
            flowChart: true,  // 默认不解析
            sequenceDiagram: true,  // 默认不解析
            onload: function () {
            	
            }
        });
        $("#loading").css('display', 'none');
    } else {
        //编辑 
        $("#editDiv").css("display", "flex");
        $(".requirement").css("display", "none");
        $(".editHeadDiv").css("display", "block");
        $(".checkHeadDiv").css("display", "none");
        var mdddm = '';
        $("#editDiv").empty();
        $("#editDiv").append("<div id='test-editormd-view2'></div>");
        testEditormdView2 = editormd("test-editormd-view2", {
            markdown: mdddm,
            path: "/systemui/static/js/frame/editor/",
            theme: "eclipse",
            previewTheme: "eclipse",
            editorTheme: "eclipse",
            codeFold: true,
            saveHTMLToTextarea: true,    // 保存 HTML 到 Textarea
            searchReplace: true,
            htmlDecode: "style,script,iframe|on*",            // 开启 HTML 标签解析，为了安全性，默认不开启
            emoji: true,
            taskList: true,
            tocm: true,         // Using [TOCM]
            tex: true,                   // 开启科学公式TeX语言支持，默认关闭
            flowChart: true,             // 开启流程图支持，默认关闭
            sequenceDiagram: true,       // 开启时序/序列图支持，默认关闭,
            /**上传图片相关配置如下*/
	        imageUpload : true,
	        imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
	        imageUploadURL : "/projectManage/systemDirectoryDocumentOperate/mkdUpload", //这个是上传图片时的访问地址
            onload: function () {
                 initPasteDragImg(this); //必须
                 
            }
        });
        $("#loading").css('display', 'none');
    }
}

//菜单栏 
function getTree(type) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentLibrary/getAllDocChapters",
        type: "post",
        data: {
            systemDirectoryDocumentId: parameterArr.obj.id,
        },
        success: function (data) {
        	if(parameterArr.obj.status == 2){
        			chapter_content = data.chapters[0];
        	}
        	let t_arr = data.chapters.map(v=>{
        		v.open = true;
        		return v;
        	})
            $.fn.zTree.init($("#docTree"), mydata.treeSetting, data.chapters.sort(compare));
        	let t_num = 0;
        	data.chapters.map(v=>{
        		if(v.chaptersLevel == 1){
        			t_num++;
        		}
        	})
            chartpt_all_length =t_num; 
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

//点击判断 非需求目录 需求目录
function getDocumentTable(event, treeId, treeNode, clickFlag) {
    $(".notRequirement").css("display", "flex");
    $(".requirement").css("display", "none");
    mydata.treeArr = [];
    getParentNameArr(treeNode.id,treeNode);
    $("#title").text(mydata.treeArr.join(" > "));
}

//==================================  ztree树操作 章节操作

//查看章节
function getParentNameArr(ID,content) {
    chapter_id = ID;
    chapter_content = content;
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentLibrary/selectChaptersMarkDown",
        type: "post",
        data: {
            id: ID,
        },
        success: function (data) {
            if (data.status == 1) {
            	mkd_more_ChaptersRelatedData(chapter_id,0);
                let msg = data.chapters;
                if(data.buttonState && data.buttonState == 2 && msg.checkoutStatus == 2){
//                    console.log('可以签出')
                    $('#signBtn').removeClass('disableBtn');
                    $('#signBtn').attr('onclick','sign_off_btn()');
                }else{ 
            		$('#signBtn').addClass('disableBtn');
                    $('#signBtn').removeAttr('onclick');
                }
                $('.version').text('当前版本：v' + msg.chaptersVersion + '.0');
                $('.Check_out_people').hide();
                if(msg.checkoutStatus && msg.checkoutStatus == 1){
                	$('.Check_out_people').text('签出').show();
                	$('.Check_out_user').text('签出人：' + isValueNull(msg.checkoutUserName)).show();
                }else{
                	$('.Check_out_user').hide();
                }
                if(parameterArr.obj.type != 2 && data.buttonState && data.buttonState == 1){ //暂存进入编辑
                    $("#editDiv").css("display", "flex");
                    $(".requirement").css("display", "none");
                    $(".editHeadDiv").css("display", "block");
                    $(".checkHeadDiv").css("display", "none");
                    var mdddm = data.markdown;
                    
                    $("#mkd_remark_btn").css("display", "inline-block");
                    
                    $("#editDiv").empty();
                    $("#editDiv").append("<div id='test-editormd-view2'></div>");
                    testEditormdView2 = editormd("test-editormd-view2", {
                        markdown: mdddm,
                        path: "/systemui/static/js/frame/editor/",
                        theme: "eclipse",
                        previewTheme: "eclipse",
                        editorTheme: "eclipse",
                        codeFold: true,
                        saveHTMLToTextarea: true,    // 保存 HTML 到 Textarea
                        searchReplace: true,
                        htmlDecode: "style,script,iframe|on*",            // 开启 HTML 标签解析，为了安全性，默认不开启
                        emoji: true,
                        taskList: true,
                        tocm: true,         // Using [TOCM]
                        tex: true,                   // 开启科学公式TeX语言支持，默认关闭
                        flowChart: true,             // 开启流程图支持，默认关闭
                        sequenceDiagram: true,       // 开启时序/序列图支持，默认关闭,
                        imageUpload : true,
                        imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
                        imageUploadURL : "/projectManage/systemDirectoryDocumentOperate/mkdUpload", //这个是上传图片时的访问地址
                        onload: function () {
                            initPasteDragImg(this); //必须
                            $("#loading").css('display', 'none');
                        }
                    });
                }else{  //查看 
                	$('.build').text('创建人：' + msg.createUserName + '   ' + msg.createDate);
                    $('.last').text('最后更新：' + isValueNull(msg.checkoutUserName) + '  ' + isValueNull(msg.lastUpdateDate));
                    $("#editDiv").css("display", "none");
                    $(".requirement").css("display", "flex");
                    $(".editHeadDiv").css("display", "none");
                    $(".checkHeadDiv").css("display", "block");
                    $('#test-editormd-view1').html('');
                    if(data.markdown){
                    	$('#test-editormd-view1').append('<textarea id="append-test1">'+data.markdown+'</textarea>');
                    }
                    testEditormdView1 = editormd.markdownToHTML("test-editormd-view1", {
                        htmlDecode: "style,script,iframe",  // you can filter tags decode
                        emoji: true,
                        taskList: true,
                        tex: true,  // 默认不解析
                        flowChart: true,  // 默认不解析
                        sequenceDiagram: true,  // 默认不解析
                        onload: function () {
                        }
                    });
                    let AttachmentList = data.chapters.relatedSystemDirectoryDocumentAttachmentList;
                    if(AttachmentList.length){  //附件
                    	let _str = '';
                    	AttachmentList.map((val,idx)=>{
                        	if(idx <= 4){
                        		_str += `<div class="flieDiv">
                        			<a class="a_style" href="/projectManage/assetsLibraryRq/downAtts?attachmentS3Bucket=${val.attachmentS3Bucket}
										&attachmentS3Key=${val.attachmentS3Key}&attachmentNameOld=${val.attachmentNameOld}" download="${val.attachmentNameOld}">${val.attachmentNameOld}</a>
								</div>`;
                        	}
                        })
                        $('#file_download_list').html(`
                            <div class="titleDiv">
                                <span class="font"> 附件 (${AttachmentList.length}) </span>
                                <span class="more a_style" onclick="mkd_more_ChaptersFiles(${chapter_id},2)"> more </span>
                            </div>
                            <div class="fileLists">${_str}</div>
                        `);
                    }else{
                    	$('#file_download_list').html(`
                            <div class="titleDiv">
                                <span class="font"> 附件 (0) </span>
                                <span class="more a_style"> more </span>
                            </div>
                            <div class="fileLists"></div>
                        `);
                    }
                    $("#loading").css('display', 'none');
                }
            }
            
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

//新建其他章节
function addHoverDom(treeId, treeNode) {
  var sObj = $("#" + treeNode.tId + "_span");
  if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
  var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
      + "' onfocus='this.blur();'></span>";
  sObj.after(addStr);
  var btn = $("#addBtn_" + treeNode.tId);
  if (btn) btn.bind("click", function () {
	$("#add_article_id").val(JSON.stringify(treeNode)).attr('htype','2');
  	$("#add_article_modal").modal('show');
  	return false;
  });
};

//新建章节    重命名  提交
function addNotic_submit() {
    $('#add_article_form').data('bootstrapValidator').validate();
    if (!$('#add_article_form').data('bootstrapValidator').isValid()) {
        return false;
    }
    var dataObj = {};
    if($("#add_article_id").attr('htype') == '1'){  //新建一级章节    参数
        dataObj = {
            systemDirectoryDocumentId: parameterArr.obj.id,
            chaptersName: $.trim($('#add_article_name').val()),
            chaptersLevel: 1,  //1 => 1级
            chaptersVersion: 1,
            chaptersOrder:chartpt_all_length + 1,
            parentId: '',
            parentIds: '',
        }
    }else{              //新建其他章节
        mydata.clickArr = [];
        let treeNode = JSON.parse($("#add_article_id").val());
        dataObj.systemDirectoryDocumentId = parameterArr.obj.id;
        dataObj.chaptersName = $.trim($('#add_article_name').val());
        dataObj.parentId = treeNode.id;
            if(treeNode.parentId){
            dataObj.chaptersLevel = treeNode.parentIds.split(',').length + 2;
            }else{
                dataObj.chaptersLevel = 2;
            }
        dataObj.chaptersVersion = 1;
        dataObj.chaptersOrder = '';
        if (treeNode.parentIds) {
            dataObj.parentIds = treeNode.parentIds + "," + treeNode.id;
        } else {
            dataObj.parentIds = (treeNode.id || '') + ",";
        }
        dataObj.projectType = treeNode.projectType;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentLibrary/addOrUpdateDocChapters",
        type: "post",
        data: dataObj,
        success: function (data) {
            if (data.status == 1) {
                //    var zTree = $.fn.zTree.getZTreeObj("docTree");
                //    zTree.addNodes(treeNode, data.sysDocChapters);
                getTree(1);
                $("#add_article_modal").modal('hide');
                $("#loading").css('display', 'none');
                layer.alert('新增成功!', { icon: 1 });
            }
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

//移动章节
function beforeDrop(treeId, treeNodes, targetNode, moveType) {
	if(targetNode == null){
		return false;
	}else{
		let parentId = targetNode.id,parentIds = '',chaptersOrder = '',chaptersLevel = targetNode.chaptersLevel;
		if(!targetNode.pId){	//外层移动
			if(moveType == 'next'){
				chaptersOrder = targetNode.chaptersOrder + 1;
				parentId = '';
			}else if(moveType == 'prev'){
				chaptersOrder = targetNode.chaptersOrder - 1 || 1;
				parentId = '';
			}else if(moveType == 'inner'){
				parentIds = targetNode.id+",";
				chaptersOrder = 1;
				chaptersLevel = targetNode.chaptersLevel + 1;
			}
		}else{
			parentIds = targetNode.parentIds;
			if(moveType == 'next'){
				chaptersOrder = targetNode.chaptersOrder + 1;
				parentId = targetNode.pId;
			}else if(moveType == 'prev'){
				chaptersOrder = targetNode.chaptersOrder;
				parentId = targetNode.pId;
			}else if(moveType == 'inner'){
				parentIds = targetNode.parentIds +','+targetNode.id;
				chaptersOrder = 1;
				chaptersLevel = targetNode.chaptersLevel + 1;
			}
		}
//		if(chaptersLevel == 1){
//			return false;
//		}
		$("#loading").css('display', 'block');
	    $.ajax({
	        url: "/projectManage/documentLibrary/moveDocChapters",
	        type: "post",
	        dataType: "json",
	        data: {
	            id: treeNodes[0].id,
	            parentId: parentId,
	            parentIds: parentIds,
	            chaptersOrder:chaptersOrder,
                chaptersLevel:chaptersLevel,
                systemDirectoryDocumentId:parameterArr.obj.id
	        },
	        success: function (data) {
	            if (data.status == 1) {
	            	getTree();
	            	layer.alert('移动成功!', { icon: 1 });
	            }else{
	            	return false;
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
}

//删除章节
function beforeRemove(treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("docTree");
    layer.confirm('您是确定要删除？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $("#loading").css('display', 'block');
        $.ajax({
            url: '/projectManage/documentLibrary/deleteDocChapters',
            type: "post",
            data: {
                id: treeNode.id
            },
            success: function (data) {
                if (data.status == 1) {
                    layer.closeAll();
                    layer.alert("删除成功!", {
                        icon: 1,
                        title: "提示信息"
                    });
                    zTree.removeNode(treeNode);
                    // zTree.removeChildNodes(treeNode);
                }
            },
            error: function (data) {
                $("#loading").css('display', 'none');
                layer.alert("系统内部错误!", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })

    }, function () {
        layer.closeAll();
    });
    return false;
}

//重命名章节
function beforeRename(treeId, treeNode, newName, isCancel) {
	if (newName.length == 0) {
        setTimeout(function () {
            var zTree = $.fn.zTree.getZTreeObj("docTree");
            zTree.cancelEditName();
            layer.alert("节点名称不能为空!", {
                icon: 0,
                title: "提示信息"
            });
        }, 0);
        return false;
    } else {
        $("#loading").css('display', 'block');
        $.ajax({
            url: "/projectManage/documentLibrary/addOrUpdateDocChapters",
            type: "post",
            data: {
                id: treeNode.id,
                chaptersName: newName,
            },
            success: function (data) {
                if (data.status == 1) {
                    layer.alert("重命名成功!", {
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
    return true;
//    if($("#add_article_id").attr('htype') == 'rename'){
//    	$.ajax({
//            url: "/projectManage/documentLibrary/addOrUpdateDocChapters",
//            type: "post",
//            data: {
//                id: $("#add_article_id").val(),
//                chaptersName: $.trim($('#add_article_name').val()),
//            },
//            success: function (data) {
//                if (data.status == 1) {
//                    layer.alert("重命名成功!", {
//                        icon: 1,
//                        title: "提示信息"
//                    });
//                }
//            },
//            error: function (data) {
//                layer.alert("系统内部错误!", {
//                    icon: 2,
//                    title: "提示信息"
//                });
//            }
//        })
//        return;
//    }
//	$("#add_article_id").val(treeNode.id).attr('htype','rename');
//  	$("#add_article_modal").modal('show');
}

//获取祖先节点ID 
function getParentNameArrID(treeNode) {
    mydata.clickArr.unshift(treeNode.id)
    if (treeNode.level <= 1) {
        return;
    } else {
        var parentObj = treeNode.getParentNode();
        getParentNameArrID(parentObj);
    }
}

function removeHoverDom(treeId, treeNode) {
    $("#addBtn_" + treeNode.tId).unbind().remove();
};
// ===========================  ztree树操作     end 
//章节历史版本
function select_history() {
	if(!chapter_id){
		layer.alert('请先选择左侧目录!',{icon:0})
		return;
	};
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/assetLibrary/directoryChaptersHistory/getChaptersHistory",
        type: "post",
        data: {
            systemDirectoryDocumentChaptersId: chapter_id,
        },
        success: function (data) {
            if (data.status == 1) {
                $("#history_list_Table").bootstrapTable('destroy');
                $("#history_list_Table").bootstrapTable({
                    data : data.data,
                    method:"post",
                    contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
                    queryParamsType:"",
                    pagination : false,
                    sidePagination: "server", 
                    columns: [{
                        checkbox: true,
                        width: "30px"
                    }, {
                        field: "id",
                        title: "id",
                        visible: false,
                        align: 'center'
                    }, {
                        field: "chaptersVersion",
                        title: "版本号",
                        align: 'center',
                        class:'a_style',
                        formatter : function(value, row, index) {
                            return '<a href="#" onclick="select_history_to('+row.id+','+value+')">v'+value+'.0</a>';
                        }
                    }, {
                        field: "checkoutUserName",
                        title: "修改人",
                        align: 'center',
                    }, {
                        field: "lastUpdateDate",
                        title: "更新时间",
                        align: 'center'
                    }, 
                    {
                        field: "relatedRequirement",
                        title: "关联需求变更单",
                        align: 'center',
                        formatter : function(value, row, index) {
                            if(row.relatedRequirement && row.relatedRequirement.requirementName){
                                return row.relatedRequirement.requirementName || '';
                            }
                        }
                    },
                    ],
                    onLoadSuccess: function () {
                        $("#userModal").modal('show');
                    },
                    onLoadError: function () {

                    },
                });
                $("#history_list_Modal").modal('show');
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

//查看某个版本章节
function select_history_to(ID,chaptersVersion){
	$("#loading").show();
	$.ajax({
        url: "/projectManage/assetLibrary/directoryChaptersHistory/getChaptersHistoryVersion",
        type: "post",
        data: {
            id: ID,
        },
        success: function (data) {
        	if(data.status == 1){
	        	$("#his_charpt_tit").text('章节信息v'+chaptersVersion+'.0');
	        	$("#test-editormd-view33").html('');
	        	if(data.markdown){
                	$('#test-editormd-view33').append('<textarea id="append-test33">'+data.markdown+'</textarea>');
                }
	        	editormd.markdownToHTML("test-editormd-view33", {
	                htmlDecode: "style,script,iframe",  // you can filter tags decode
	                emoji: true,
	                taskList: true,
	                tex: true,  // 默认不解析
	                flowChart: true,  // 默认不解析
	                sequenceDiagram: true,  // 默认不解析
	                onload: function () {
	                	$("#loading").css('display', 'none');
	                }
	            });
	        	$("#associated_charpt_Modal").modal('show');
	        	
        	}
        	$("#loading").hide();
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

// 查询章节的关联文档
function mkd_more_ChaptersRelatedData(ID,status) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/assetsLibraryRq/getChapterRelation",
        type: "post",
        data: {
        	 chapterId: ID// 章节id
        },
        success: function (data) {
        	if(status == 2){
        		if (data.list.length) {
        			
        		}
        		$('#information_all_Modal').modal('show');
        	}else{
	            if (data.list.length) {
	            	if(status == 2){
	            		$('#information_all_Modal').modal('show');
	            	}else{
		                $('#relevanceInfo_list').html(`
		                    <div class="titleDiv">
		                        <span class="font"> 关联文档信息 </span>
		                    </div>
		                    <div class="fileLists ztree" id="relevanceInfo_list_ztree"></div>
		                `);
		                let temp_arr = data.list.map(v=>{
		                	if(v.pId == 0){
		                		v.name = v.name + '<strong>【文档名称】</strong>';
		                	}
		                	return v;
		                })
		                $.fn.zTree.init($("#relevanceInfo_list_ztree"), setting_a_charts, temp_arr);
	            	}
	            }else{
	            	$('#relevanceInfo_list').html(`
	                    <div class="titleDiv">
	                        <span class="font"> 关联文档信息 </span>
	                    </div>
	                    <div class="fileLists"></div>
	                `);
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

function formValidator() {
    $('#add_article_form').bootstrapValidator({
        excluded: [':disabled'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live: 'enabled',
        fields: {
            add_article_name: {
                // trigger: "change",
                validators: {
                    notEmpty: {
                        message: '条目名称不能为空！'
                    },
                    callback: {
                        message: '条目名称最大输入50个字符！',
                        callback: function (value, validator, $field) {
                            if ($field.val().length <= 50) {
                                return true;
                            }
                            return false;
                        }
                    }
                }
            },
        }
    });
}

function refactorFormValidator() {
    $('#add_article_modal').on('hidden.bs.modal', function () {
    	$('#add_article_name').val('');
    	$('#add_article_id').val('').removeAttr('htype');
        $("#add_article_form").data('bootstrapValidator').destroy();
        $('#add_article_form').data('bootstrapValidator', null);
        formValidator();
    });
}

//排序
function compare(x, y) {//比较函数
    if (x.chaptersOrder < y.chaptersOrder) {
        return -1;
    } else if (x.chaptersOrder > y.chaptersOrder) {
        return 1;
    } else {
        return 0;
    }
}

//html编码
function HTMLEncode(html) {
    var temp = document.createElement("div");
    (temp.textContent != null) ? (temp.textContent = html) : (temp.innerText = html);
    var output = temp.innerHTML;
    temp = null;
    return output;
}

