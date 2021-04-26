/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 * 新建类项目-管理-文档主页js
 */
//全局变量
var mydata = {
    status: "false",
    treeArr: [],    //用来储存，点击取得树节点及其所有的祖先节点,显示在头部 
    clickArr: [],   //用来储存，点击添加节点 取得树节点及其所有的祖先节点ID 
    thisData: '',
    exportUrl: '',
    before_node_id: '',
    document_all_: [],
    treeSetting: {   // 树的配置信息 
        data: {
            simpleData: {
                enable: true
            },
            key: {
                name: "dirName"
            },
        },
        edit: {
            enable: true,
            editNameSelectAll: true,
            showRenameBtn: showRenameBtn,
            showRemoveBtn: showRemoveBtn
        },
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            selectedMulti: false
        },
        callback: {
            onClick: getDocumentTable,
            beforeRemove: beforeRemove,
            beforeRename: beforeRename,
            onRename: onRename
        }
    }
}

var readAuth = false;//读权限
var writeAuth = false;//写权限

//存储url中的参数
var parameterArr = {};
parameterArr.arr = window.location.href.split("?");
parameterArr.parameterArr = parameterArr.arr[1].split(",");
parameterArr.obj = {};
for (var i = 0; i < parameterArr.parameterArr.length; i++) {
    var obj = parameterArr.parameterArr[i].split("=");
    parameterArr.obj[obj[0]] = obj[1];
}

$(document).ready(function () {
    $("#loading").css('display', 'block');
    $(".headTitle").text(decodeURIComponent(parameterArr.obj.name) + "：资产库");
    getTree();
    //权限设置（按目录）
    $("#listsPower").on("click", function () {
        layer.open({
            type: 2,
            title: '权限设置',
            shadeClose: true,
            move: false,
            area: ['95%', '95%'],
            offset: "4%",
            shade: 0.3,
            tipsMore: true,
            anim: 2,
            content: '/projectManageui/newProject/toDocPermission?projectId=' + parameterArr.obj.id,
            btn: ['关闭'],
            btnAlign: 'c', //按钮居中 
            btn: function () {
                layer.closeAll();
            }
        });
    })
    //上传文档
    $("#upDoc").on("click", function () {
        $("#docTypes").val('');
        $("#systemSys").val('');
        $('.selectpicker').selectpicker('refresh');
        $("#updateForm")[0].reset();
        $("#updateDoc").modal("show");
    })
    //新建MakeDown文档
    $("#newMKDoc").on('click', function () {
        $("#mdTypes").val('');
        $("#mdName").val('');
        $("#new_systemSys").val('');
        $('.selectpicker').selectpicker('refresh');
        $("#newMKDocDiv").modal("show");
    })
    formValidator();
    refactorFormValidator();
});

//左侧目录树形结构 
function getTree() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentLibrary/getDirectoryTree",
        type: "post",
        data: {
            projectId: parameterArr.obj.id,
        },
        success: function (data) {
            $.fn.zTree.init($("#menuTree"), mydata.treeSetting, data);
            fuzzySearch('menuTree', '#treeSearchInput', null, true);  //初始化模糊搜索方法
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

//查看文档 
function getDocumentTable(event, treeId, treeNode, clickFlag) {
    mydata.thisData = "";
    $('.powerDiv_List').hide();
    $('.notRequirement').show();
    $(".headRight>.btn").css("display", "inline-block");
    $("#cancel_btn").css("display", "none");
    $("#return").css("display", "none");
    $(".headTitle").show();
    $(".headTitle_assco").hide();
    mydata.thisData = treeNode;
    getDocumentType(treeNode);
    $("#title").css("display", "block");
    mydata.treeArr = [];
    getParentNameArr(treeNode);
    $("#title").text(mydata.treeArr.join(" > "));
    getTableData(treeNode);
}

//获取文档
function getTableData(treeNode) {
    before_node_id = treeNode;
    $("#loading").css('display', 'block');
    $.ajax({
		url:"/projectManage/assetLibrary/directoryAuthority/getCurrentUserSystemDirecoryAuth",
		type:"post",
		dataType:"json",
		data:{
			projectId :parameterArr.obj.id,
			systemDirectoryId : treeNode.id
		},
		success:function(data){
			writeAuth = data.auth.writeAuth == 1 ? true : false;
            readAuth = data.auth.readAuth == 1 ? true : false;
			if (readAuth) {
                if (writeAuth) {  //全  权限
                    $('.readAuth_btn').show();
                    $('.writeAuth_btn').show();
                } else {   //   只读   权限
                    $('.writeAuth_btn').hide();
                    $('.readAuth_btn').show();
                }
            } else {  //无权限
                $('.readAuth_btn').hide();
                $('.writeAuth_btn').hide();
            }
            $("#loading").css('display', 'block');
            $.ajax({
                url: "/projectManage/documentLibrary/getDocumentFile",
                type: "post",
                data: {
                    id: treeNode.id,
                },
                success: function (data) {
                    if (data.type == "documents") {
                        $(".noData").css("display", "none");
                        $(".myTableDiv").css("display", "block");
                        mydata.document_all_ = data.data;
                        if(readAuth){
                            getTable(data.data);
                        }else{
                        	getTable([]);
                        }
                    } else {
                        $(".noData").css("display", "none");
                        $(".myTableDiv").css("display", "block");
                        getTable([]);
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
		},
		error:function (data){
			$("#loading").css('display', 'none');
            layer.alert("系统内部错误!", {
                icon: 2,
                title: "提示信息"
            });
		}
	})
    
}

// 非需求目录 --> 生成标题
function getParentNameArr(treeNode) {
    mydata.treeArr.unshift(treeNode.dirName)
    if (treeNode.level == 0) {
        return;
    } else {
        var parentObj = treeNode.getParentNode();
        getParentNameArr(parentObj);
    }
}

// 非需求目录 --> 形成表格
function getTable(data) {
    $("#loading").css('display', 'block');
    $("#documentList").bootstrapTable('destroy');
    $("#documentList").bootstrapTable({
        data: data,
        method: "post",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType: "",
        pagination: false,
        sidePagination: "server",
        columns: [{
            field: "id",
            title: "id",
            visible: false,
            align: 'center'
        }, {
            field: "documentName",
            title: "文档名称",
            align: 'left',
            formatter: function (value, row, index) {
                var a;
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                if (row.saveType == 1) {
                    //                    a = value + '<span title="附件类文件" class="downfilesIcon"></span>';
                    let _value = ` <a class="a_style readAuth_btn" href="/projectManage/assetsLibraryRq/downObject?documentS3Bucket=${row.documentS3Bucket}&documentS3Key=${row.documentS3Key}&documentName=${row.documentName}" download="${row.documentName}">${row.documentName}</a>`;
                    a = _value + '<span title="附件类文件，点击可以下载" class="downfilesIcon readAuth_btn"></span>';
                } else {
                    a = '<a class="a_style readAuth_btn" onclick="showMakeDown(\'' + row.id + '\')">' + value + '</a><span title="Markdown文件，点击可以在线浏览编辑" class="marksIcon"></span>';
                }
                return a;
            }
        }, {
            field: "createDate",
            title: "创建信息",
            align: 'center',
            formatter: function (value, row, index) {
                return isValueNull(row.createUserName) + " " + isValueNull(row.createDate);
            }
        }, {
            field: "lastUpdateDate",
            title: "最后更新时间",
            align: 'center',
            formatter: function (value, row, index) {
                return isValueNull(row.updateUserName) + " " + isValueNull(row.lastUpdateDate);
            }
        }, {
            field: "opt",
            title: "操作",
            align: 'center',
            formatter: function (value, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                var str = '<a class="a_style" onclick="getHistory(' + rows + ')">历史版本</a>';
                if(writeAuth == 1){
                    str +=   ' | <a class="a_style" onclick="delDoc(' + rows + ')">移除</a>';
                }
                if (row.saveType == 1) {
                    var url = "/projectManage/assetsLibraryRq/downObject?documentS3Bucket=" + encodeURIComponent(row.documentS3Bucket) + "&documentS3Key=" + encodeURIComponent(row.documentS3Key) + "&documentName=" + encodeURIComponent(row.documentName);
                    if(writeAuth == 1){
                        str += ' | <a class="a_style readAuth_btn" onclick="coverUpdate(' + rows + ')">覆盖上传</a>';
                    }
                    str += ' | <a class="a_style readAuth_btn" href="' + url + '" download="' + row.documentName + '">导出</a>';
                } else if (row.saveType == 2) {
                    str += ' | <a class="a_style readAuth_btn" onclick="exportFile(' + rows + ')">导出</a>';
                }
                return str;
            }
        }],
        onLoadSuccess: function (data) {
            $("#loading").css('display', 'none');
        },
        onLoadError: function () {
            $("#loading").css('display', 'none');
            layer.alert("系统内部错误!", {
                icon: 2,
                title: "提示信息"
            });
        },
        onPageChange: function () {
            $("#loading").css('display', 'block');
        }
    });
}

//删除文档
function delDoc(row) {
    layer.confirm('您是确定要删除？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $("#loading").css('display', 'block');
        $.ajax({
            url: "/projectManage/documentLibrary/delectDocumentFile",
            type: "post",
            data: {
                id: row.id,
            },
            success: function (data) {
                getTableData(mydata.thisData)
                layer.alert("移除成功!", {
                    icon: 1,
                    title: "提示信息"
                });
                $("#loading").css('display', 'none');
            },
            error: function (data) {
                $("#loading").css('display', 'none');
                layer.alert("系统内部错误!", {
                    icon: 0,
                    title: "提示信息"
                });
            }
        })
        layer.closeAll();
    }, function () {
        layer.closeAll();
    });
}

//显示导出框
function exportFile(row) {
    $("#export_Modal").modal("show");
    mydata.exportUrl = "/projectManage/documentChapters/exportByDocumentId?systemDirectoryDocumentId=" + row.id;
}

//请求后台正式导出
function exportCommit() {
    if ($('#ulFileExport input[name="file"]:checked').val() == undefined) {
        layer.alert("请选择!", {
            icon: 0,
            title: "提示信息"
        });
    } else {
        mydata.exportUrl += "&type=" + $('#ulFileExport input[name="file"]:checked').val();
    }
    window.location.href = mydata.exportUrl;
    $("#export_Modal").modal("hide");
}

//文档历史版本
function getHistory(row) {
    layer.open({
        type: 2,
        title: '历史版本',
        shadeClose: true,
        move: false,
        area: ['94%', '90%'],
        offset: "4%",
        shade: 0.3,
        tipsMore: true,
        anim: 2,
        content: '/projectManageui/requirementPerspective/history?id=' + row.id + ',type=' + row.saveType,
        btn: ['关闭'],
        btnAlign: 'c', //按钮居中 
        btn: function () {
            layer.closeAll();
        }
    });
}

//ztree树操作  
function beforeRemove(treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("menuTree");
    //询问框 
    layer.confirm('您是确定要删除？' + treeNode.dirName, {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var dataObj = {};
        var url = '';
        if (treeNode.level == "0") {
            dataObj = {
                systemId: treeNode.systemId
            };
            url = '/projectManage/systemPerspective/delSystemDirectoryBySystemId';
            zTree.removeChildNodes(treeNode);
        } else {
            dataObj = {
                id: treeNode.id
            };
            url = '/projectManage/documentLibrary/delDirectory';
            zTree.removeNode(treeNode);
        }
        $.ajax({
            url: url,
            type: "post",
            data: dataObj,
            success: function (data) {
                layer.alert("删除成功!", {
                    icon: 1,
                    title: "提示信息"
                });
            },
            error: function (data) {
                layer.alert("系统内部错误!", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
        layer.closeAll();
    }, function () {
        layer.closeAll();
    });
    return false;
}
//左侧目录节点，重命名
function beforeRename(treeId, treeNode, newName, isCancel) {
    if (newName.length == 0) {
        setTimeout(function () {
            var zTree = $.fn.zTree.getZTreeObj("menuTree");
            zTree.cancelEditName();
            layer.alert("节点名称不能为空!", {
                icon: 0,
                title: "提示信息"
            });
        }, 0);
        return false;
    } else {
        $.ajax({
            url: "/projectManage/documentLibrary/updateDirectoryName",
            type: "post",
            data: {
                directoryId: treeNode.id,
                directoryName: newName,
            },
            success: function (data) {
                layer.alert("重命名成功!", {
                    icon: 1,
                    title: "提示信息"
                });
            },
            error: function (data) {
                layer.alert("系统内部错误!", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
    return true;
}

function onRename(e, treeId, treeNode, isCancel) {
    console.log(5)
}

//显示重命名图标
function showRenameBtn(treeId, treeNode) {
    if (treeNode.createType == "1") {//1系统自动创建的不可以更改名称，2手动创建的才可以改名
        return false;
    } else {
        return true;
    }
}

//显示删除图标
function showRemoveBtn(treeId, treeNode) {
    if (treeNode.level == "0") {//第一层不给删除
        return false;
    } else {
        if (treeNode.createType == "1") {//自动创建不可以删除
            return false;
        } else {
            return true;//手动创建可以删除
        }
    }
}

//左边目录树形结构，添加节点
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
        + "' onfocus='this.blur();'></span>";
    sObj.after(addStr);
    var btn = $("#addBtn_" + treeNode.tId);


    if (btn) btn.bind("click", function () {
        console.log(treeNode)

        mydata.clickArr = [];
        var dataObj = {};
        dataObj.projectId = parameterArr.obj.id;
        dataObj.parentId = treeNode.id;
        dataObj.parentIds = treeNode.parentIds + "," + treeNode.id;
        dataObj.projectType = treeNode.projectType;
        dataObj.dirName = "新建文件夹";

        $.ajax({
            url: "/projectManage/documentLibrary/addDirectory",
            type: "post",
            /* async: false,*/
            data: dataObj,
            success: function (data) {
                console.log(data);
                var zTree = $.fn.zTree.getZTreeObj("menuTree");
                zTree.addNodes(treeNode, { id: (data.data), projectId: treeNode.id, dirName: "新建文件夹" });
            },
            error: function (data) {
                console.log(data);
            }
        })
        return false;
    });
};

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
//ztree树操作                 end

//获取文档类型
function getDocumentType(treeNode) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentLibrary/getDocumentTypes",
        type: "post",
        data: {
            directoryId: treeNode.id,
        },
        success: function (data) {
            $("#docTypes").empty();
            $("#mdTypes").empty();
            $("#systemSys").empty();
            $("#new_systemSys").empty();

            $("#docTypes").append("<option disabled selected hidden>请选择</option>");
            $("#mdTypes").append("<option disabled selected hidden>请选择</option>");
            $("#systemSys").append("<option disabled selected hidden>请选择</option>");
            $("#new_systemSys").append("<option disabled selected hidden>请选择</option>");

            if (data.data) {
                data.data.map(function (item) {
                    $("#docTypes").append("<option value=" + item.valueCode + ">" + item.valueName + "</option>");
                    $("#mdTypes").append("<option value=" + item.valueCode + ">" + item.valueName + "</option>");
                })
            }
            if (data.system) {
                data.system.map(function (item) {
                    $("#systemSys").append("<option value=" + item.systemId + ">" + item.systemName + "</option>");
                    $("#new_systemSys").append("<option value=" + item.systemId + ">" + item.systemName + "</option>");
                })
            }
            $('.selectpicker').selectpicker('refresh');
            $("#loading").css('display', 'none');
        },
        error: function (data) {
            console.log(data);
        }
    })
}

/*上传文档*/
function uploadDoc() {
    var formData = new FormData();
    var files = $('#upfile')[0].files;
    $('#updateForm').data('bootstrapValidator').validate();
    if (!$('#updateForm').data("bootstrapValidator").isValid()) {
        return;
    }
    if(!files.length){
    	layer.alert("请选择上传文件!", {
            icon: 0,
            title: "提示信息"
        });
    	return;
    }
    for (var i = 0; i < files.length; i++) {
        formData.append('file', $('#upfile')[0].files[i]);
    }
    formData.append('systemDirectoryId', mydata.thisData.id);
    formData.append('systemId', $("#systemSys").val() || '');
    formData.append('projectType', 2);
    formData.append('documentVersion', 1);
    formData.append('documentType', $("#docTypes").val());
    formData.append('saveType', 1);
    $("#loading").show();
    $.ajax({
        url: "/zuul/projectManage/documentLibrary/uploadNewDocument",
        type: "post",
        contentType: false,
        processData: false,
        data: formData,
        success: function (data) {
        	if(data.flag){
        		getDocumentType(mydata.thisData);
                $("#updateDoc").modal("hide");
                layer.alert("上传成功!", {
                    icon: 1,
                    title: "提示信息"
                });
                before_node_id && getTableData(before_node_id);
        	}else{
                layer.alert("上传失败，系统内部错误!", {
                    icon: 2,
                    title: "提示信息"
                });
        	}
        	$("#loading").hide();
        },
        error: function (data) {
        	$("#loading").hide();
            layer.alert("上传失败，系统内部错误!", {
                icon: 2,
                title: "提示信息"
            });
        }
    })
}

/* 新建MK文档 保存按钮 */
function newMKDoc() {
    let re_name_flag = true;
    if (mydata.document_all_.length) {
        mydata.document_all_.map(val => {
            if (val.documentName == $("#mdName").val()) {
                re_name_flag = false;
            }
        })
    }
    if (!re_name_flag) {
        layer.alert("文档名称已存在!", {
            icon: 0,
            title: "提示信息"
        });
        return;
    }
    $('#newMKDoc_form').data('bootstrapValidator').validate();
    if (!$('#newMKDoc_form').data("bootstrapValidator").isValid()) {
        return;
    }
    $("#loading").show();
    $.ajax({
        url: "/projectManage/documentLibrary/upNewMarkDocument",
        type: "post",
        data: {
            systemDirectoryId: mydata.thisData.id,
            systemId: $("#new_systemSys").val() || '',
            projectType: 2,
            documentVersion: 1,
            documentType: $("#mdTypes").val(),
            documentName: $("#mdName").val(),
            saveType: 2,
        },
        success: function (data) {
            var url = '/projectManageui/requirementPerspective/toEditAndCheckMarkDown?id=' + data.documentMarkId + ',status=2,type=1,readAuth=1,writeAuth=1';
            openMakeDownPage(url);
        },
        error: function (data) {
            layer.alert("上传失败，系统内部错误!", {
                icon: 2,
                title: "提示信息"
            });
        }
    })
}

function showMakeDown(id) {
    //status( 1为查看  2为编辑  )
    //type(1 文档库可操作)
	var readAuth_num = readAuth ? 1 : 2;
	var writeAuth_num = writeAuth ? 1 : 2;
    var url = '/projectManageui/requirementPerspective/toEditAndCheckMarkDown?id=' + id + ',status=1,type='+writeAuth_num+',readAuth=' + readAuth_num + ',writeAuth=' + writeAuth_num;
    openMakeDownPage(url);
}

function openMakeDownPage(url) {
    layer.open({
        type: 2,
        title: false,
        move: false,
        closeBtn: 0,
        area: ['100%', '100%'],
        shade: 0.3,
        tipsMore: true,
        anim: 2,
        content: url,
    });
    $("#newMKDocDiv").modal("hide");
    $("#loading").hide();
}

function coverUpdate(row) {
    $("#coverUpFile").attr("idValue", row.id)
    $("#coverForm")[0].reset();
    $("#coverModal").modal("show");

}

//覆盖上传文档
function coverUpload() {
    var formData = new FormData();
    var files = $('#coverUpFile')[0].files;
    if(!files.length){
    	layer.alert("请选择上传文件!", {
            icon: 0,
            title: "提示信息"
        });
    	return;
    }
    for (var i = 0; i < files.length; i++) {
        formData.append('file', $('#coverUpFile')[0].files[i]);
    }
    formData.append('id', $("#coverUpFile").attr("idValue"));
    $("#loading").show();
    $.ajax({
        url: "/zuul/projectManage/documentLibrary/coverUploadOldDocument",
        type: "post",
        contentType: false,
        processData: false,
        data: formData,
        success: function (data) {
            $("#coverModal").modal("hide");
            layer.alert("覆盖上传成功!", {
                icon: 1,
                title: "提示信息"
            });
            before_node_id && getTableData(before_node_id);
            $("#loading").hide();
        },
        error: function (data) {
            $("#loading").hide();
            layer.alert("系统内部错误!", {
                icon: 0,
                title: "提示信息"
            });
        }
    })
}

function formValidator() {
    $('#newMKDoc_form').bootstrapValidator({
        message: '不能为空',//通用的验证失败消息
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
	         mdTypes:{
		        validators: {
		          notEmpty: {
		            message: '此项不能为空'
		          }
		        }
	        },
            mdName: {
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
        }
    })
    $('#updateForm').bootstrapValidator({
        message: '不能为空',//通用的验证失败消息
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	docTypes:{
		        validators: {
		          notEmpty: {
		            message: '此项不能为空'
		          }
		        }
	        },
	        
        }
    })
}

//重构表单验证
function refactorFormValidator() {
    $('#newMKDocDiv').on('hidden.bs.modal', function () {
        $("#newMKDoc_form").data('bootstrapValidator').destroy();
        $('#newMKDoc_form').data('bootstrapValidator', null);
        formValidator();
    })
    $('#updateDoc').on('hidden.bs.modal', function () {
        $("#updateForm").data('bootstrapValidator').destroy();
        $('#updateForm').data('bootstrapValidator', null);
        formValidator();
    })
}






