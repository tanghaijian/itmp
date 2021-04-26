$(function () {
    pageInit();

    //搜索展开和收起
    $("#downBtn").click(function () {
        if ($(this).children("span").hasClass("fa-caret-up")) {
            $(this).children("span").removeClass("fa-caret-up");
            $(this).children("span").addClass("fa-sort-desc");
            $("#search_div").slideUp(200);
        } else {
            $(this).children("span").removeClass("fa-sort-desc");
            $(this).children("span").addClass("fa-caret-up");
            $("#search_div").slideDown(200);
        }
    });

// 搜索框 收藏按钮 js 部分
    /* $(".collection").click(function () {
     if( $(this).children("span").hasClass("fa-heart-o") ){
     $(this).children("span").addClass("fa-heart");
     $(this).children("span").removeClass("fa-heart-o");
     }else {
     $(this).children("span").addClass("fa-heart-o");
     $(this).children("span").removeClass("fa-heart");
     }
     });*/

    //所有的Input标签，在输入值后出现清空的按钮
    $('input[type="text"]').parent().css("position", "relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange", function () {
        if ($(this).val() != "") {
            $(this).parent().children(".btn_clear").css("display", "block");
        } else {
            $(this).parent().children(".btn_clear").css("display", "none");
        }
    });
});
//表格数据加载
function pageInit() {
    jQuery("#list2").jqGrid({
        url: '/system/dataDic/getDataDictPage',
        datatype: 'json',
        mtype: "post",
        postData: {
            termName: $.trim($("#DicName").val()),
            termCode: $.trim($("#DicCode").val()),
            status: $("#DicType").val()
        },
        height: 'auto',
        width: $(".content-table").width() * 0.999,
        colNames: ['id', '字典名称', '字典编码', '有效状态', '操作'],
        colModel: [
            {name: 'id', index: 'id', hidden: true},
            {name: 'termName', index: 'termName', sorttype: 'string', searchoptions: {sopt: ['cn']}},
            {name: 'termCode', index: 'termCode', searchoptions: {sopt: ['cn']}},
            {
                name: 'status', index: 'status', searchoptions: {sopt: ['cn']},
                formatter: function (value, grid, rows) {
                    return value == "1" ? "有效" : "无效";
                }
            },
            {
                name: 'edit',
                index: 'edit',
                align: "center",
                fixed: true,
                sortable: false,
                resize: false,
                search: false,
                formatter: function (value, grid, rows, state) {
                    var opt_status = [];
                    if (rows.status == "1") {
                        if (dicEdit == true) {
                            opt_status.push("<a  href='javascript:void(0);'  onclick='modifyDataDic(\"modify\",\"" + rows.termCode + "\",\"" + rows.termName + "\")'>编辑</a>");
                        }

                        if (devInvalid == true) {
                            opt_status.push("<a  href='javascript:void(0);'  onclick='editStatus(\"" + rows.termCode + "\",2)'>设为无效</a>");
                        }
                        return opt_status.join('<span> | </span>');
                    } else {
                        if (deveffective == true) {
                            opt_status.push("<a href='javascript:void(0);' onclick='editStatus(\"" + rows.termCode + "\",1)'>设为有效</a>");
                        }
                        return opt_status.join('<span> | </span>');
                    }
                }
            }
        ],
        rowNum: 10,
        rowTotal: 200,
        rowList: [5, 10, 20, 30],
        rownumWidth: 40,
        pager: '#pager2',
        sortable: true,   //是否可排序
        sortorder: 'asc',
        sortname: 'id',
        loadtext: "数据加载中......",
        viewrecords: true, //是否要显示总记录数
        beforeRequest: function () {
            $("#loading").css('display', 'block');
        },
        loadComplete: function (xhr) {
            $("#loading").css('display', 'none');
            var ids = $("#list2").jqGrid("getDataIDs");
            //获取所有行的id
            var rowDatas = $("#list2").jqGrid("getRowData");
            //获取所有行的数据
            for (var i = 0; i < (rowDatas.length); i++) {
                var rowData = rowDatas[i];
                var validStatus = rowData.status;
                if (validStatus == "无效") {
                    $("#" + ids[i] + " td").css("background-color", "#F2F2F2");
                    $("#" + ids[i] + " td").css("text-decoration", "line-through");
                    $("#" + ids[i] + " a").parent("td").css("text-decoration", "none");
                }
            }
        }
    });
}
//搜索
function searchInfo() {
    $("#list2").jqGrid('setGridParam', {
        url: '/system/dataDic/getDataDictPage',
        postData: {
            termName: $.trim($("#DicName").val()),
            termCode: $.trim($("#DicCode").val()),
            status: $("#DicType").val()
        },
        page: 1
    }).trigger("reloadGrid");
}

//清空搜索内容
function clearSearch() {
    $('#DicName').val("");
    $('#DicCode').val("");
    $("#DicType").selectpicker('val', '');
    searchInfo();
}

//清空表格内容
function clearContent(that) {
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display", "none");
}

//增改数据字典
function modifyDataDic(type, termCode, termName) {
    $('#modifyDataDictModal').modal({
        'show': true,
        'remote': '/systemui/dataDic/toDataDicModal?type=' + type + '&termCode=' + encodeURIComponent(encodeURIComponent(termCode)) + '&termName=' + encodeURIComponent(encodeURIComponent(termName))
    });
}

//修改状态
function editStatus(termCode, status) {
    $.ajax({
        type: "POST",
        url: "/system/dataDic/updateStatus",
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        data: {
            termCode: termCode,
            status: status
        },
        success: function (msg) {
            if (msg.flag) {
                layer.alert("修改成功", {
                    icon: 1,
                    title: "提示信息"
                });
                searchInfo();
            } else {
                layer.alert("修改失败", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        }
    });
}


