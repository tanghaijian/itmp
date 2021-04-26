/*
    K.escape(val)   //特殊字符转换成HTML
    '<div id="abc">&</div>' 返回"&lt;div id=&quot;abc&quot;&gt;&amp;&lt;/div&gt;"
    K.unescape(val)   //特殊HTML entities转换成字符
    '&lt;div id=&quot;abc&quot;&gt;&amp;&lt;/div&gt;'; //返回"<div id="abc">&</div>"

    editor2.html(`<h1 id="type" style="font-family:&quot;Microsoft YaHei&quot;;">KindEditor Main Test</h1>`);  //赋值
    editor2.loadPlugin('autoheight');
    editor2.create();

    editor2.html('');//清空

    editor2.appendHtml('后加入内容');

    editor2.readonly();//只读

    editor2.readonly(false);//可编辑

    editor2.remove();//销毁

    console.log(editor2.isEmpty());//是否为空

    editor2.sync();//提交前将HTML数据设置到textarea
    console.log(editor2.html());//提交
*/

var mydata = {
    treeArr: [],    //用来储存，点击取得树节点及其所有的祖先节点,显示在头部
    treeSetting: {   // 树的配置信息
        //    	check: {
        //			enable: true
        //		},
        data: {
            simpleData: {
                enable: true
            },
            key: {
                name: "chaptersName"
            },
        },
        edit: {
            enable: true,
            editNameSelectAll: true,
            drag: {
                prev: true,
                inner: true,
                next: true,
            },
            showRenameBtn: true,
            showRemoveBtn: showRemoveBtn
        },
        view: {
            showIcon: true,
            showLine: true,
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
        },
        treeNode: {
            order: 'chaptersOrder'
        },
        callback: {
            beforeClick: beforeClick,
            //            onClick: getDocumentTable,
            beforeRemove: beforeRemove,
            beforeRename: beforeRename,
            beforeDrop: beforeDrop,
        }
    }
}

var System_arr = [];//查询框所有的系统
var System_Ids = [];
var animate_time = 600;//动画时间
var chapter_content = '';//点击查看数据
var chapter_id = '';//点击查看id
var parentNode = '';  //父级数据
var current_treeId = ''; //当前ztree对象

var editor2, editor2_check;

$(function () {
    //select_System();
    getWikiTree();
    formValidator();
    refactorFormValidator();

    KindEditor.ready(function (K) {
        editor2 = K.create(K('textarea[name=content2]').get(), {
            minHeight: '500px',
            resizeType: 1,//1只能改变高度，0不能拖动
            langType: 'zh-CN',
            basePath: '../',//根目录路径
            items: [
                'source', '|', 'undo', 'redo', '|', 'preview', 'print',
                'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'insertfile'
                , 'table', 'hr', 'pagebreak', 'link', 'unlink'
            ],
            afterCreate: function () {//编辑器创建后执行
                this.loadPlugin('autoheight');
                var ele = $('.ke-edit-iframe').contents().find('.ke-content').get(0);
                initPasteDragImg(ele);
                $('.ke-icon-image').on('click', function () {
//                	$('#import_Label').text('上传图片');
                	$('#opt_uploadFile').val('');
                	$('#uploadFile_type').val(1);
                    $('#importModal').modal('show');
                })
                $('.ke-icon-insertfile').on('click', function () {
//                	$('#import_Label').text('上传文件');
                	$('#opt_uploadFile').val('');
                	$('#uploadFile_type').val(2);
                    $('#importModal').modal('show');
                })
            },
            afterSelectFile: function (e) {//图片空间选择文件后执行
            },
            afterChange: function () {//内容发生变化后执行
                this.loadPlugin('autoheight');
            }
        });
        editor2_check = K.create(K('textarea[name=content_check]').get(), {
            width: '700px',
            minHeight: '500px',
            resizeType: 0,//1只能改变高度，0不能拖动
            langType: 'zh-CN',
            basePath: '../',//根目录路径
            items: [],
            afterCreate: function () {//编辑器创建后执行
                this.loadPlugin('autoheight');
            }
        });
    });
})


//  获取文档id
function getWikiTree() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/wikiLibrary/getWikiTree",
        method: "post",
        data: {
            "projectId": $('#projectId').val()
        },
        success: function (data) {
            if (data.status == 1) {
                $('#systemDiretoryId').val(data.systemDiretoryId);
                select_System();
            } else {
                layer.alert("系统内部错误,请联系管理员!!!", { icon: 2 });
            }
            $("#loading").css('display', 'none');
        },
    });
}

//  获取查询下拉框系统
function select_System() {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/wikiLibrary/selectSystemByProjectId",
        method: "post",
        data: {
            "projectId": $('#projectId').val(),
        },
        success: function (data) {
            if (data.length) {
                System_arr = data;
                System_arr.map(function (v) {
                    $('#system_body').append('<option value="' + v.systemDirectoryDocumentId + '">' + v.systemName + '</option>');
                })
                if (data.length == 1) {
                    $('#system_body').val(data[0].systemDirectoryDocumentId);
                    create_ztree();
                }
                $('#system_body').selectpicker('refresh');
            }
            $("#loading").css('display', 'none');
        },
    });
}

//添加系统树
function create_ztree() {
    $('#docTree').empty();
    var ids = $('#system_body').val();
    if (ids == null) {
        layer.alert("请选择需要查询得系统", { icon: 0 });
        return false;
    }

    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/wikiLibrary/getAllDocChapters",
        method: "post",
        data: {
            "systemDirectoryDocumentIds": ids.toString(),
        },
        success: function (data) {
            if (data.data.length) {
                data.data.map(function (v, idx) {
                    $('#docTree').append(`
                        <li>
                        <p class="rowdiv">
                            <span id="menuTree_${v.id}_switch" title="" class="def_col_2 tree_icon button level0 switch roots_close" treenode_switch=""  val="${v.id}"></span>
                            <span id="menuTree_${v.id}_ico" title=" treenode_ico="" class="def_col_2  button ico_close" style=""></span> 
                            <span class="def_col_28 bold _show_ellipsis" title="${v.systemName}">${v.systemName}</span>
                        </p>
                        <ul class="milestone_item _hide" id="milestone_item${idx}"></ul>
                        </li>
                    `);
                    $.fn.zTree.init($("#milestone_item" + idx), mydata.treeSetting, v.listChapters);
                })
                tree_down_up();
            }
            $("#loading").css('display', 'none');
        },
    });
}

function tree_down_up() {
    $('.tree_icon').eq(0).removeClass('roots_close').addClass('roots_open');//默认第一个展开
    $('.milestone_item').eq(0).slideDown(animate_time);
    $('.tree_icon').bind('click', function () {
        if (!$(this).hasClass('roots_open')) {
            $(this).removeClass('roots_close').addClass('roots_open');
            $(this).parent().siblings('.milestone_item').slideDown(animate_time);
        } else {
            $(this).parent().siblings('.milestone_item').slideUp(animate_time);
            $(this).removeClass('roots_open').addClass('roots_close'); 
        }
    })
}


//提交内容
function submit_HTML(status) {
    var _url = '/projectManage/wikiLibrary/submit';
    var _html = HTMLEncode(editor2.html());
    // var converter = new showdown.Converter();
    // var _dcode = converter.makeHtml(_html);
    // chapter_content.content = HTMLEncode(_dcode);
    chapter_content.content = _html;
    var sub_data = {
        "systemDirectory": JSON.stringify(chapter_content),
        "contentHtml": JSON.stringify(_html),
    }
    if (status == 1) {
        _url = '/projectManage/wikiLibrary/temporaryStorage';
    } else if (status == 3) {
        _url = '/projectManage/systemDirectoryDocumentOperate/cancel';
        sub_data = {
            id: chapter_content.id,
        }
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: _url,
        method: "post",
        data: sub_data,
        success: function (data) {
            if (data.code == 1) {
                if (status == 1) {
                    layer.alert('暂存成功!', { icon: 1 });
                } else if (status == 2) {
                    layer.alert('提交成功!', { icon: 1 });
                    var treeObj = $.fn.zTree.getZTreeObj(current_treeId);
                    if (data.result.chaptersVersion != undefined) {
                        parentNode.chaptersVersion = data.result.chaptersVersion;
                        treeObj.updateNode(parentNode);
                    }
                    $('.version').text('当前版本：v' + data.result.chaptersVersion + '.0');
                    editor2.sync();//提交前将HTML数据设置到textarea
                } else {
                    layer.alert('取消签出成功!', { icon: 1 });
                }
                $('#signBtn').addClass('disableBtn');
                editor2.readonly();//只读
                $('.editHeadDiv').hide();
                $('.checkHeadDiv').show();
            } else {
                layer.alert("系统内部错误!", {
                    icon: 2,
                    title: "提示信息"
                });
            }
            $("#loading").css('display', 'none');
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert("系统内部错误!", {
                icon: 2,
                title: "提示信息"

            });
        }
    })
}

//================================================     ztree树操作
function showRemoveBtn(treeId, treeNode) {
    return treeNode.pId;  //根节点不允许删除
}

//清除其他ztree  样式
function beforeClick(treeId, treeNode, clickFlag) {
    $('.ztree li a.curSelectedNode').removeClass('curSelectedNode');
    $('.ztree li a').find('.button.add').remove();
    $('.ztree li a').find('.button.edit').remove();
    $('.ztree li a').find('.button.remove').remove();
    $('.ztree li a').find('.button.icon02_ico_docu').remove();
    current_treeId = treeId;
    parentNode = treeNode;
    getDocumentTable(null, treeId, treeNode);
}

//查看章节
function getDocumentTable(event, treeId, treeNode, clickFlag) {
    editor2.html('');//清空
    chapter_id = treeNode.id;
    chapter_content = treeNode;
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/wikiLibrary/selectChaptersMarkDown",
        type: "post",
        data: {
            id: treeNode.id,
        },
        success: function (data) {
            if (data.status == 1) {
                $('#editor').show();
                $('.noData').hide();
                var msg = data.chapters;
                var _html = isValueNull(data.markdown);

                editor2.html(htmlDecode(_html));  //赋值
                editor2.loadPlugin('autoheight');
                editor2.readonly();//查看
                if (_html) {
                } else {
                    //                	editor2.html('<h1>暂无数据...</h1>');
                }
                if (data.buttonState && data.buttonState == 2 && msg.checkoutStatus == 2) {
                    $('#signBtn').removeClass('disableBtn');
                    $('#signBtn').attr('onclick', 'sign_off_btn()');
                } else {
                    $('#signBtn').addClass('disableBtn');
                    $('#signBtn').removeAttr('onclick');
                }
                $('.version').text('当前版本：v' + msg.chaptersVersion + '.0');
                $('.Check_out_people').hide();
                if (msg.checkoutStatus && msg.checkoutStatus == 1) {
                    $('.Check_out_people').text('签出').show();
                    $('.Check_out_user').text('签出人：' + isValueNull(msg.checkoutUserName)).show();
                } else {
                    $('.Check_out_user').hide();
                }
                $('.Check_out_people').hide();
                if (msg.checkoutStatus && msg.checkoutStatus == 1) {
                    $('.Check_out_people').text('签出').show();
                    $('.Check_out_user').text('签出人：' + isValueNull(msg.checkoutUserName)).show();
                } else {
                    $('.Check_out_user').hide();
                }
                if (data.buttonState && data.buttonState == 1) { //暂存进入编辑
                    editor2.readonly(false);//可编辑
                    $(".editHeadDiv").css("display", "block");
                    $(".checkHeadDiv").css("display", "none");
                } else {  //查看 
                    $('.build').text('创建人：' + msg.createUserName + '   ' + msg.createDate).attr('title', msg.createUserName + '   ' + msg.createDate);
                    $('.last').text('最后更新：' + isValueNull(msg.checkoutUserName) + '  ' + isValueNull(msg.lastUpdateDate)).attr('title', isValueNull(msg.checkoutUserName) + '  ' + isValueNull(msg.lastUpdateDate));
                    $("#editDiv").css("display", "none");
                    $(".editHeadDiv").css("display", "none");
                    $(".checkHeadDiv").css("display", "block");
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

//签出
function sign_off_btn() {
    editor2.html('');//清空
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/wikiLibrary/signOff",
        type: "post",
        data: {
            id: chapter_id,
        },
        success: function (data) {
            if (data.code == 1) {
                var _html = isValueNull(data.content);
                editor2.html(htmlDecode(_html));  //赋值
                editor2.loadPlugin('autoheight');
                editor2.readonly(false);//可编辑
                $('.editHeadDiv').show();
                $('.checkHeadDiv').hide();
            }
            $("#loading").css('display', 'none');
        }
    })
}

//新建其他章节
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
    var addStr = "<span class='button add' title='新建章节' id='addBtn_" + treeNode.tId
        + "' onfocus='this.blur();'></span>";
    var checkStr = "<span class='button icon02_ico_docu' id='addBtn_2" + treeNode.tId
        + "' onfocus='this.blur();' title='历史版本'></span>";
    sObj.after(addStr);
    //    sObj.after(addStr + checkStr);
    var btn = $("#addBtn_" + treeNode.tId);
    if (btn) btn.bind("click", function () {
        parentNode = treeNode;
        current_treeId = treeId;
        $("#add_article_id").val(JSON.stringify(treeNode)).attr('htype', '2');
        $("#add_article_modal").modal('show');
        return false;
    });
};

function removeHoverDom(treeId, treeNode) {
    $("#addBtn_" + treeNode.tId).unbind().remove();
    //    $("#addBtn_2" + treeNode.tId).unbind().remove();
};

//新建章节    重命名  提交
function addNotic_submit() {
    $('#add_article_form').data('bootstrapValidator').validate();
    if (!$('#add_article_form').data('bootstrapValidator').isValid()) {
        return false;
    }
    mydata.clickArr = [];
    var dataObj = {};
    //    if ($("#add_article_id").attr('htype') == 1) {  //新建一级章节    参数
    //        dataObj = {
    //            systemDirectoryDocumentId: parameterArr.obj.id,
    //            chaptersName: $.trim($('#add_article_name').val()),
    //            chaptersLevel: 1,  //1 => 1级
    //            chaptersVersion: 1,
    //            chaptersOrder: chartpt_all_length + 1,
    //            parentId: '',
    //            parentIds: '',
    //        }
    //    } else {
    var treeNode = JSON.parse($("#add_article_id").val());
    dataObj.systemDirectoryDocumentId = treeNode.systemDirectoryDocumentId;
    dataObj.chaptersName = $.trim($('#add_article_name').val());
    dataObj.parentId = treeNode.id;
    if (treeNode.parentId) {
        dataObj.chaptersLevel = treeNode.parentIds.split(',').length + 2;
    } else {
        dataObj.chaptersLevel = 2;
    }
    dataObj.chaptersVersion = 0;
    dataObj.chaptersOrder = '';
    if (treeNode.parentIds) {
        dataObj.parentIds = treeNode.parentIds + "," + treeNode.id;
    } else {
        dataObj.parentIds = (treeNode.id || '') + ",";
    }
    dataObj.projectType = treeNode.projectType;
    //    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentLibrary/addOrUpdateDocChapters",
        type: "post",
        data: dataObj,
        success: function (data) {
            if (data.status == 1) {
                var treeObj = $.fn.zTree.getZTreeObj(current_treeId);
                var newNode = data.sysDocChapters;
                newNode = treeObj.addNodes(parentNode, newNode);
                $("#add_article_modal").modal('hide');
                $("#loading").css('display', 'none');
                //  layer.alert('新增成功!', { icon: 1 });
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
    if (targetNode == null || treeNodes[0].systemDirectoryDocumentId != targetNode.systemDirectoryDocumentId) {
        return false;
    } else {
        var parentId = targetNode.id, parentIds = '', chaptersOrder = '', chaptersLevel = targetNode.chaptersLevel, is_move_flag = true;
        if (!targetNode.pId) {	//外层移动
            if (moveType == 'next') {
                chaptersOrder = targetNode.chaptersOrder + 1;
                if (treeNodes[0].pId == targetNode.pId) {
                    is_move_flag = false;
                }
                parentId = '';
            } else if (moveType == 'prev') {
                chaptersOrder = targetNode.chaptersOrder - 1 || 1;
                if (treeNodes[0].pId == targetNode.pId) {
                    is_move_flag = false;
                }
                parentId = '';
            } else if (moveType == 'inner') {
                parentIds = targetNode.pId;
                chaptersOrder = 1;
                chaptersLevel = targetNode.chaptersLevel + 1;
            }
        } else {
            parentIds = targetNode.pId;
            if (moveType == 'next') {
                chaptersOrder = targetNode.chaptersOrder + 1;
                parentId = targetNode.pId;
                if (treeNodes[0].pId == targetNode.pId) {
                    is_move_flag = false;
                }
            } else if (moveType == 'prev') {
                chaptersOrder = targetNode.chaptersOrder;
                parentId = targetNode.pId;
                if (treeNodes[0].pId == targetNode.pId) {
                    is_move_flag = false;
                }
            } else if (moveType == 'inner') {
                parentIds = targetNode.pId + ',' + targetNode.id;
                chaptersOrder = 1;
                chaptersLevel = targetNode.chaptersLevel + 1;
            }
        }
        $("#loading").css('display', 'block');
        $.ajax({
            url: "/projectManage/documentLibrary/moveDocChapters",
            type: "post",
            dataType: "json",
            data: {
                id: treeNodes[0].id,
                parentId: parentId,
                parentIds: parentIds,
                chaptersOrder: chaptersOrder,
                chaptersLevel: chaptersLevel,
                systemDirectoryDocumentId: treeNodes[0].systemDirectoryDocumentId
            },
            success: function (data) {
                if (data.status == 1) {
                    create_ztree();
                    layer.alert('移动成功!', { icon: 1 });
                } else {
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

//查看某个版本章节
function select_history_to(ID) {
    editor2_check.html('');//清空
    $("#loading").show();
    $.ajax({
        url: "/projectManage/assetLibrary/directoryChaptersHistory/getChaptersHistoryVersion",
        type: "post",
        data: {
            id: ID,
        },
        success: function (data) {
            if (data.status == 1) {
                $("#his_charpt_tit").text('章节信息');
                var _html = isValueNull(data.markdown);
                editor2_check.html(htmlDecode(_html));  //赋值
                editor2_check.loadPlugin('autoheight');
                editor2_check.readonly();//查看
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

//删除章节
function beforeRemove(treeId, treeNode, a, b, c) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);

    layer.confirm('您是确定要删除？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        layer.closeAll();
        $("#loading").show();
        $.ajax({
            url: '/projectManage/documentLibrary/deleteDocChapters',
            type: "post",
            data: {
                id: treeNode.id,
            },
            success: function (data) {
                if (data.status == 1) {
                    layer.closeAll();
                    //   layer.alert("删除成功!", {
                    //       icon: 1,
                    //       title: "提示信息"
                    //   });
                    zTree.removeNode(treeNode);
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

    }, function () {
        layer.closeAll();
    });
    return false;
}

//重命名章节
function beforeRename(treeId, treeNode, newName, isCancel) {
    if (newName.length == 0) {
        setTimeout(function () {
            layer.alert("节点名称不能为空!", {
                icon: 0,
                title: "提示信息"
            });
        }, 0);
        return false;
    } else {
        $("#loading").show();
        $.ajax({
            url: "/projectManage/documentLibrary/addOrUpdateDocChapters",
            type: "post",
            data: {
                id: treeNode.id,
                chaptersName: newName,
            },
            success: function (data) {
                /*if (data.status == 1) {
                    layer.alert("重命名成功!", {
                        icon: 1,
                        title: "提示信息"
                    });
                }*/
                $("#loading").hide();
            },
            error: function (data) {
                $("#loading").hide();
                layer.alert("系统内部错误!", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
    return true;
}

//章节历史版本
function select_history() {
    if (!chapter_id) {
        layer.alert('请先选择左侧目录!', { icon: 0 })
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
                    data: data.data,
                    method: "post",
                    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                    queryParamsType: "",
                    pagination: false,
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
                        class: 'a_style',
                        formatter: function (value, row, index) {
                            return '<a onclick="select_history_to(' + row.id + ')">v' + value + '.0</a>';
                        }
                    }, {
                        field: "checkoutUserName",
                        title: "修改人",
                        align: 'center',
                    }, {
                        field: "lastUpdateDate",
                        title: "更新时间",
                        align: 'center'
                    },/* {
                        field: "relatedRequirement",
                        title: "关联需求变更单",
                        align: 'center'
                    }*/,],
                    onLoadSuccess: function () {
                        $("#userModal").modal('show');
                    },
                    onLoadError: function () {

                    },
                });
                $("#history_list_Modal").modal('show');
                $("#loading").css('display', 'none');
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
        $('#add_article_id').val('');
        $('#add_article_name').val('').removeAttr('htype');
        $("#add_article_form").data('bootstrapValidator').destroy();
        $('#add_article_form').data('bootstrapValidator', null);
        formValidator();
    });
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
	var files = $('#opt_uploadFile')[0].files[0];
//	if($('#uploadFile_type').val() == 1){
		uploadImg(files);
//	}
}


/*function create_ztree() {
var ids = $('#system_body').val();
$("#loading").css('display', 'block');
if(System_Ids.length){
	ids.map(function (v,idx) {
		if (!System_Ids.includes(v.id.toString())) {
			get_tree(v.id);
		}
	})
}
$("#loading").css('display', 'none');
tree_down_up();
//$('#docTree').empty();
}*/

function isValueNull(val) {
    if (val) {
        return val == 'null' ? '' : val;
    } else {
        return '';
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

/*2.用浏览器内部转换器实现html解码*/
function htmlDecode(text) {
    //1.首先动态创建一个容器标签元素，如DIV
    var temp = document.createElement("div");
    //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
    temp.innerHTML = text;
    //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
    var output = temp.innerText || temp.textContent;
    temp = null;
    return output;
}
