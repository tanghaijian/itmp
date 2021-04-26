/**
 * Created by  on 2020/2/12.
 * it全流程测试主页面js
 */

var document_all_ = [];
$(document).ready(function () {
    getTableData();
    other_handle();
    formValidator();
    refactorFormValidator();
});

//根据后台传递到前端的需求和开发任务获取对应的文档
function getTableData() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/itRedocumentLibrary/getDocumentByRequirement",
        type: "post",
        data: {
            requirementCode: cs_requirementCode,
            featureCode:taskCode,
        },
        success: function (data) {
            if (data.status == 1) {
                $(".noData").css("display", "none");
                $(".myTableDiv").css("display", "block");
                document_all_ = data.data;
                getTable(data.data);
                getDocumentType();
            } else {
                getTable([]);
            }
            $("#loading").css('display', 'none');
        },
        error: function (data) {
            getTable([]);
            $("#loading").css('display', 'none');
        }
    })
}

//文档   形成表格
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
//        pagination : true,
//        queryParams : function(params) {
//        	var params = {
//        		pageNumber: params.pageNumber,
//        		pageSize: params.pageSize,
//    			requirementCode: cs_requirementCode,
//    	        featureCode:taskCode,	
//        	}
//        	return params;
//        },
//        responseHandler : function(res) {
//        	return {
//        		"total":res.data.length,
//                "rows":res.data
//        	};
//        },
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
                    var url = "/projectManage/assetsLibraryRq/downObject?documentS3Bucket=" + encodeURIComponent(row.documentS3Bucket) + "&documentS3Key=" + encodeURIComponent(row.documentS3Key) + "&documentName=" + encodeURIComponent(row.documentName);
                    a = '<a class="a_style" href="' + url + '" download="' + value + '">' + value + '</a><span title="附件类文件，点击可以下载" class="downfilesIcon"></span>';
                } else {
                    a = '<a class="a_style" onclick="showMakeDown(\'' + row.id + '\')">' + value + '</a><span title="Markdown文件，点击可以在线浏览编辑" class="marksIcon"></span>';
                }
                return a;
            }
        }, {
            field: "documentTypeName",
            title: "文档类型",
            align: 'center'
        }, {
            field: "createData",
            title: "创建信息",
            align: 'center',
            formatter: function (value, row, index) {
            	if(row.createUserName==null){
            		return "";
            	}else{
            		return row.createUserName+" "+row.createDate;
            	}
            	 
            }
        }, {
            field: "updateData",
            title: "最后更新信息",
            align: 'center',
            formatter: function (value, row, index) {
            	if(row.updateUserName==null){
            		return "";
            	}else{
            		return row.updateUserName+" "+row.lastUpdateDate;
            	}
           	
           }
        }, {
            field: "opt",
            title: "操作",
            align: 'center',
            width: 300,
            formatter: function (value, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                var str = '<a class="a_style" onclick="delDoc(' + rows + ')">删除</a>' + ' | ' + '<a class="a_style" onclick="getHistory(' + row.id + ')">历史版本</a>';
                if (row.saveType == 2) {  //md文档
                    str += ' | ' + '<a class="a_style" onclick="select_version(' + rows + ')">版本分析</a>' + ' | ' + '<a class="a_style" onclick="export_md(' + row.id + ')">导出</a>';
                } else if (row.saveType == 1) {
					str += ' | ' + '<a class="a_style readAuth_btn" onclick="coverUpdate(' + rows + ')">覆盖上传</a>';
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

//获取文档类型
function getDocumentType() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/itRedocumentLibrary/getTypeAndSystem",
        type: "post",
        data: {
            featureCode:taskCode,
        },
        success: function (data) {
            $("#docTypes").empty();
            $("#mdTypes").empty();

            $("#docTypes").append("<option disabled selected hidden>请选择</option>");
            $("#mdTypes").append("<option disabled selected hidden>请选择</option>");

            if (data.data.documentType) {
                for(var i=0;i<data.data.documentType.length;i++){
                	var item = data.data.documentType[i];
                    $("#docTypes").append("<option value=" + item.documentType + ">" + item.documentTypeName + "</option>");
                    $("#mdTypes").append("<option value=" + item.documentType + ">" + item.documentTypeName + "</option>");
                }
            }
            $('.selectpicker').selectpicker('refresh');
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

/* 新建MK文档 提交 */
function newMKDoc() {
    var re_name_flag = true;
    if (document_all_.length) {
    	for(var i=0;i<document_all_.length;i++){
        	var val = document_all_[i];
            if (val.documentName == $("#mdName").val()) {
                re_name_flag = false;
            }
        }
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
        url: "/projectManage/itRedocumentLibrary/upNewMarkDocument",
        type: "post",
        data: {
            currentUserAccount: currentUserAccount,
            requirementCode: cs_requirementCode,
            featureCode:taskCode,
            projectType: 2,
            documentVersion: 1,
            documentType: $("#mdTypes").val(),
            documentName: $("#mdName").val(),
            saveType: 2,

        },
        success: function (data) {
            var url = '/projectManageui/newProject/toItEditAndCheckMarkDown?id=' + data.documentMarkId + ',status=2,currentUserAccount=' + currentUserAccount + ',cs_requirementCode=' + cs_requirementCode+ ',featureCode=' + taskCode;
            openMakeDownPage(url)
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

/*上传文档*/
function uploadDoc() {
    var formData = new FormData();
    var _params = 'uploadNewDocument';
    if($("#updateDoc").find('.modal-title').attr('typ') == 2){  //覆盖上传
    	if (!$('#coverUpFile')[0].files.length) {
            $('#upfile_invalid_tit_re').removeClass('_hide');
            return;
        }
        _params = 'coverUploadOldDocument';
        var files = $('#coverUpFile')[0].files;
        for (var i = 0; i < files.length; i++) {
            formData.append('file', $('#coverUpFile')[0].files[i]);
            formData.append('fileName', $('#coverUpFile')[0].files[i].name);
        }
        formData.append('id', $("#coverUpFile").attr("idValue"));
    }else{
        $('#updateForm').data('bootstrapValidator').validate();
        if (!$('#updateForm').data("bootstrapValidator").isValid()) {
            return;
        }
        if ($('#upfile')[0].files.length) {

        } else {
            $('#upfile_invalid_tit').removeClass('_hide');
            return;
        }
        var files = $('#upfile')[0].files;
        for (var i = 0; i < files.length; i++) {
            formData.append('file', $('#upfile')[0].files[i]);
            formData.append('fileName', $('#upfile')[0].files[i].name);
        }
        
        formData.append('projectType', 2);
        formData.append('documentVersion', 1);
        formData.append('documentType', $("#docTypes").val());
        formData.append('saveType', 1);
    }
    formData.append('requirementCode', cs_requirementCode);
    formData.append('featureCode',taskCode);
    formData.append('currentUserAccount', currentUserAccount);
    $("#loading").show();
    $.ajax({
        url: "/projectManage/itRedocumentLibrary/"+_params,
        type: "post",
        contentType: false,
        processData: false,
        data: formData,
        success: function (data) {
            if (data.flag) {
                getTableData();
                $("#coverModal").modal("hide");
                $("#updateDoc").modal("hide");
                layer.alert("上传成功!", {
                    icon: 1,
                    title: "提示信息"
                });
            } else {
                layer.alert("上传失败!", {
                    icon: 2,
                    title: "提示信息"
                });
            }
            $("#loading").hide();
            //            before_node_id && getTableData(before_node_id);
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

//覆盖上传
function coverUpdate(row) {
    $("#updateDoc").find('.modal-title').attr('typ',2);
    $("#coverUpFile").attr("idValue", row.id)
    $("#coverForm")[0].reset();
    $("#coverModal").modal("show");
}

//导出
function export_md(ID,version) {
    $('#export_id').val(ID);
    $('#export_version').val(version);
    $("#export_Modal").modal("show");
}

//导出提交
function export_submit() {
    var _val = $('input[name="expot_ipt"]:checked').val();
    $("#loading").css('display', 'block');
    var exp_url = "/projectManage/itReDocumentChapters/exportByDocumentId?systemDirectoryDocumentId=" + $('#export_id').val() + "&type=" + _val;
    if($('#export_version').val()){
    	exp_url = "/projectManage/itReDocumentChapters/exportByDocumentIdAndVersion?systemDirectoryDocumentId=" + $('#export_id').val() + "&type=" + _val
    		+ "&version=" + $('#export_version').val();
    }
    window.location.href = exp_url;
    $("#export_Modal").modal("hide");
    $("#loading").css('display', 'none');
}

//其他按钮操作
function other_handle() {
    //上传文档
    $("#upDoc").on("click", function () {
        $("#updateDoc").find('.modal-title').attr('typ',1);
        $("#docTypes").val('');
        $('.selectpicker').selectpicker('refresh');
        $("#updateForm")[0].reset();
        $("#updateDoc").modal("show");
    })
    //新建MakeDown文档
    $("#newMKDoc").on('click', function () {
        $("#mdTypes").val('');
        $("#mdName").val('');
        $('.selectpicker').selectpicker('refresh');
        $("#newMKDocDiv").modal("show");
    })
    $('#upfile').on('change', function () {
        if ($('#upfile')[0].files) {
            $('#upfile_invalid_tit').addClass('_hide');
        } else {
            $('#upfile_invalid_tit').removeClass('_hide');
        }
    })
    $('#coverUpFile').on('change', function () {
    	if ($('#coverUpFile')[0].files) {
    		$('#upfile_invalid_tit_re').addClass('_hide');
    	} else {
    		$('#upfile_invalid_tit_re').removeClass('_hide');
    	}
    })
    $("#chapters_select").change(function () {
        var opt = $("#chapters_select").val();
        var type = $("#chapters_select").attr('type');
        get_version_ass(opt, type, 2);
    });
    $('#newMKDoc_btn').on('click',function(){
    	newMKDoc();
    })
}

//删除文档
function delDoc(row) {
    layer.confirm('您是确定要删除？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $("#loading").css('display', 'block');
        $.ajax({
            url: "/projectManage/itRedocumentLibrary/delectDocumentFile",
            type: "post",
            data: {
                id: row.id,
                currentUserAccount: currentUserAccount,
                requirementCode: cs_requirementCode,
                featureCode:taskCode,
                documentType:row.documentType,
            },
            success: function (data) {
                layer.alert("移除成功!", {
                    icon: 1,
                    title: "提示信息"
                });
                getTableData();
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
        layer.closeAll();
    }, function () {
        layer.closeAll();
    });
}

//历史版本
function getHistory(tid) {  
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/itResystemPerspective/getDocumentHistory",
        type: "post",
        data: {
            documentId: tid,
            requirementCode:cs_requirementCode,
            featureCode:taskCode,
        },
        success: function (data) {
            get_history_Table(data);
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

//以表格形式展示文档历史版本信息
function get_history_Table(data) {
    $("#historyTable").bootstrapTable('destroy');
    $("#historyTable").bootstrapTable({
        data: data.data,
        method: "post",
        queryParamsType: "",
        pagination: false,
        sidePagination: "server",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        columns: [{
            field: "id",
            title: "id",
            visible: false,
            align: 'center'
        }, {
            field: "documentVersion",
            title: "版本号",
            align: 'left',
            formatter: function (value, row, index) {
                return "V" + value + ".0";
            }
        }, {
            field: "updateUserName",
            title: "修改人",
            align: 'left'
        }, {
            field: "lastUpdateDate",
            title: "更新时间",
            align: 'left'
        }, {
            field: "relatedRequirement",
            title: "关联需求变更单",
            align: 'left',
            formatter: function (value, row, index) {
                if (value == null) {
                    return "";
                } else {
                    return isValueNull(value.requirementCode) + " " + isValueNull(value.requirementName);
                }
            }
        }, {
            field: "opt",
            title: "操作",
            align: 'center',
            formatter: function (value, row, index) {
                rows = JSON.stringify(row).replace(/"/g, '&quot;');
                if(row.saveType == 1){  //附件下载，saveType:1普通附件，2markdown
                    var url = "/projectManage/assetsLibraryRq/downObject?documentS3Bucket=" + encodeURIComponent(row.documentS3Bucket) + "&documentS3Key=" + encodeURIComponent(row.documentS3Key) + "&documentName=" + encodeURIComponent(row.documentName);
                    return '<a class="a_style" href="' + url + '" download="' + row.documentName + '">下载</a>';
                }else if(row.saveType == 2){   //md导出
                    return '<a class="a_style" onclick="export_md(' + row.systemDirectoryDocumentId + ',' + row.documentVersion + ')">导出</a>';
                }
            }
        }],
        onLoadSuccess: function () {

        },
        onLoadError: function () {

        }
    });
    $('#history_list_Modal').modal('show');
    $("#loading").css('display', 'none');
}

//跳转md 页面
function showMakeDown(id) {
    //status( 1为查看  2为编辑  )
    var url = '/projectManageui/newProject/toItEditAndCheckMarkDown?id=' + id + ',status=1' + ',currentUserAccount=' + currentUserAccount + ',cs_requirementCode=' + cs_requirementCode+ ',featureCode=' + taskCode;
    openMakeDownPage(url)
}

//markdown文件弹窗
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

//版本分析
function select_version(rows) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/itReDocumentChapters/getVersionContrast",
        type: "post",
        data: {
        	systemDirectoryDocumentId:rows.id,
            requirementCode: cs_requirementCode
        },
        success: function (data) {
            if (data.status == 1) {
                $('#doc_id').val(rows.id);
                $('#versions_comparison_tit').text(data.name);
                $('#document_tit').text(rows.documentName);
                $('#document_tit2').text(rows.documentName);
                $('#versions_comparison_tit').text(rows.documentName);
                var insertList = '<td class="a_style" style="cursor: pointer;">'+data.insertNum+'</td>';
                if (data.insertList.length) {
                	var insertList_data = JSON.stringify(data.insertList).replace(/"/g, '&quot;');
                    insertList = '<td class="a_style" onclick="get_version_ass('+insertList_data+',1,1)" style="cursor: pointer;">'+data.insertNum+'</td>';
                }
                var updateList = '<td class="a_style" style="cursor: pointer;">'+data.updateNum+'</td>';
                if (data.updateList.length) {
                	var insertList_data = JSON.stringify(data.updateList).replace(/"/g, '&quot;');
                    updateList = '<td class="a_style" onclick="get_version_ass('+insertList_data+',2,1)" style="cursor: pointer;">'+data.updateNum+'</td>';
                }
                var deleteList = '<td class="a_style" style="cursor: pointer;">'+data.deleteNum+'</td>';
                if (data.deleteList.length) {
                	var insertList_data = JSON.stringify(data.deleteList).replace(/"/g, '&quot;');
                    deleteList = '<td class="a_style" onclick="get_version_ass('+insertList_data+',3,1)" style="cursor: pointer;">'+data.deleteNum+'</td>';
                }
                var str_htm = 
                    '<tr><th>添加的条目数量</th>'+insertList+'<td>'+data.insertPer+'</td></tr>'+
                    '<tr><th>修改的条目数量</th>'+updateList+'<td>'+data.updatePer+'</td></tr>'+
                    '<tr><th>删除的条目数量</th>'+deleteList+'<td>'+data.deletePer+'</td></tr>'+
                    '<tr><th>合计</th><td style="cursor: pointer;">'+data.sumNum+'</td><td>'+data.sumPer+'</td></tr>';
                $('#versions_comparison_tbody').html(str_htm);
                $('#versions_comparison_Modal').modal('show');
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

//版本对比详情，arr对比的条目信息；type:1新增的条目，2更新的条目，3删除的条目；status:1文档，2章节
function get_version_ass(arr, type, status) {
    if (!arr.length) return;
    var re_id = arr[0].id;
    if (status == 2) {
        re_id = arr;
    } else {
        if (!arr.length) {
            layer.alert('暂无条目对比!', { icon: 0 })
            return;
        } else {
            $('#chapters_select').empty();
            $('#chapters_select').removeAttr('type');
            $('#chapters_select').attr('type', type);
        	for(var i=0;i<arr.length;i++){
            	var v = arr[i];
                $('#chapters_select').append('<option value="'+v.id+'">'+v.chapterName+'</option>');
            }
        }
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/itRedocumentLibrary/selectChaptersMarkDownContrast",
        type: "post",
        data: {
            type:type,
            id: re_id  //章节id
        },
        success: function (data) {
            if (data.status == 1) {
                $('#createUserName').text(isValueNull(data.latestVersion.chapters.checkoutUserName));
                $('#createDate').text(isValueNull(data.latestVersion.chapters.lastUpdateDate));
                // 变化前
                $('#test-editormd-view55').html('');
                if (data.lastVersion) {
                    $('#test-editormd-view55').append('<textarea id="append-test55">' + isValueNull(data.lastVersion) + '</textarea>');
                }
                editormd.markdownToHTML("test-editormd-view55", {
                    htmlDecode: "style,script,iframe",
                    emoji: true,
                    taskList: true,
                    tex: true,  // 默认不解析
                    flowChart: true,  // 默认不解析
                    sequenceDiagram: true,  // 默认不解析
                });
                // 变化后
                $('#test-editormd-view66').html('');
                if (data.latestVersion.markdown) {
                    $('#test-editormd-view66').append('<textarea id="append-test66">' + isValueNull(data.latestVersion.markdown) + '</textarea>');
                }
                editormd.markdownToHTML("test-editormd-view66", {
                    htmlDecode: "style,script,iframe",
                    emoji: true,
                    taskList: true,
                    tex: true,  // 默认不解析
                    flowChart: true,  // 默认不解析
                    sequenceDiagram: true,  // 默认不解析
                });
                $('#versions_detail_Modal').modal('show');
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

//需求确认书
function create_doc_book() {
    window.location.href = "/projectManage/itReDocumentChapters/export?systemDirectoryDocumentId=" +
        $('#doc_id').val() + "&idStr=" + "&type=1";
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
            mdTypes: {
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
            docTypes: {
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            // upfile:{
            //     validators: {
            //         notEmpty: {
            //         message: '此项不能为空'
            //         }
            //     }
            // },
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
        $('#upfile_invalid_tit').addClass('_hide');
        formValidator();
    })
    $('#coverModal').on('hidden.bs.modal', function () {
    	$('#upfile_invalid_tit_re').addClass('_hide');
    })
}


/**
 * 解决数据出现null，页面出现null
 */
function isValueNull(value) {
    return value != null && value != undefined ? htmlDecodeJQ(value) : '';
}


//转义  元素的innerHTML内容即为转义后的字符
function htmlEncodeJQ(str) {
  return $('<span/>').text(str).html();
}
//解析 
function htmlDecodeJQ(str) {
  return $('<span/>').html(str).text();
}