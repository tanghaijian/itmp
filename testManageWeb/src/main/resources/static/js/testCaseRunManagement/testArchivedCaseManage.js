/**
 * Description: 归档案例管理
 */

var span_ = "<span>&nbsp;|&nbsp;</span>";
var aids = [], selectUserIds = [];
var editId = '', caseNumber2 = '', caseName2 = '', caseType2 = '', systemIds2 = '', uIds2 = '';
var ff = null;
var tableIdArr = ['userTable']
$(document).ready(function () {
    formValidator();
    showSystems();
    initTable();
    addCheckBox2();

    //拖拽完成后的这条数据，并且可以获取这行数据的上一行数据和下一行数据
    //    $("#test-case-table").bootstrapTable({
    //        onReorderRowsDrop: function (table, row) {
    //            dynamicTableSortNum();
    //            return false;
    //        }
    //    });
    $("#new_system").on('change', function(){
    	$(".new_menuSelect").css("display","block");
    	$.ajax({
	        url: "/testManage/testCase/getCaseCatalogBySystem",
	        method: "post", 
	        data: {
	        	systemId:$(this).val(),
	        },
	        success: function (data) {
	        	$("#new_menu").empty();
	        	data.cataLogs.map(function(val,inde){
	        		 var option = document.createElement("option"); 
	                 $(option).val(val.id); 
	                 $(option).text(val.catalogName);
	                 $('#new_menu').append(option);
	        	})
	        	 $('.selectpicker').selectpicker('refresh');
	        },
	    }); 
    })
    $("#edit_archivedSystem").on('change', function(){ 
    	$.ajax({
	        url: "/testManage/testCase/getCaseCatalogBySystem",
	        method: "post", 
	        data: {
	        	systemId:$(this).val(),
	        },
	        success: function (data) {
	        	$("#edit_menu").empty();
	        	data.cataLogs.map(function(val,inde){
	        		 var option = document.createElement("option"); 
	                 $(option).val(val.id); 
	                 $(option).text(val.catalogName);
	                 $('#edit_menu').append(option);
	        	})
	        	 $('.selectpicker').selectpicker('refresh');
	        },
	    }); 
    })
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
    addClickRow(tableIdArr);
});

function addClickRow(idArr) {
    for (var i = 0; i < idArr.length; i++) {
        $("#" + idArr[i]).on("click-row.bs.table", function (e, row, $element) {
            $element.children("td").find("input[type=checkbox]").click();
        })
    }

}

// 搜索框 收藏按钮控制 js 部分
function collect_handle() {
    $(".collection").click(function () {
        if ($(this).children("span").hasClass("fa-heart-o")) {
            id = $(".contentNav  .nav_active", parent.document).attr('val');
            var obj = {
                search: [],
                table: [],
            };
            //搜索框数据    格式 {"type":"input / window / select" , "value": {"xxx1": $("#xx").val(),"xxx2": $("#xx").val(),...... }  }
            obj.search.push({"isCollect": true, "isData": false}) //是否收藏
            //第一行
            obj.search.push({
                "type": "input",
                "value": {"caseNumber": $("#caseNumber").val()},
                "isData": _is_null($("#caseNumber"))
            });
            obj.search.push({
                "type": "input",
                "value": {"caseName": $("#caseName").val()},
                "isData": _is_null($("#caseName"))
            });
            obj.search.push({
                "type": "select",
                "value": {"caseType": $("#archivedCaseType").val()},
                "isData": _is_null($("#archivedCaseType"))
            });
            obj.search.push({
                "type": "select",
                "value": {"systemIds": $("#systemIds").val()},
                "isData": _is_null($("#systemIds"))
            });
            //第二行
            obj.search.push({
                "type": "input",
                "value": {"userName": $("#userName").val()},
                "isData": _is_null($("#userName"))
            });
            var isResult = obj.search.some(function (item) {
                return item.isData
            })
            if (!isResult) return;
            //表格数据
            for (var i = 0; i < $('#colGroup2 .onesCol').length; i++) {
                obj.table.push({
                    "value": $('#colGroup2 .onesCol input').eq(i).attr('value'),
                    "checked": $('#colGroup2 .onesCol input').eq(i).prop('checked')
                })
            }
            layer.closeAll('dialog');
            $.ajax({
                type: "post",
                url: "/system/uifavorite/addAndUpdate",
                dataType: "json",
                data: {
                    'menuUrl': pageUrl,
                    'favoriteContent': JSON.stringify(obj)
                },
                success: function (data) {
                    if (data.status == "success") {
                        $(".collection").children("span").addClass("fa-heart");
                        $(".collection").children("span").removeClass("fa-heart-o");
                        $("#loading").css('display', 'none');
                        layer.alert('收藏成功 ！', {
                            icon: 1,
                        })
                    } else if (data.status == 2) {
                        layer.alert('收藏失败 ！', {
                            icon: 2,
                        })
                    }
                },
                error: function () {
                    $("#loading").css('display', 'none');
                    layer.alert(errorDefect, {
                        icon: 2,
                        title: "提示信息"
                    });
                }
            });
        } else {
            id = $(".contentNav  .nav_active", parent.document).attr('val');
            var obj = {
                search: [],  //查询搜索框数据
            };
            obj.search.push({"isCollect": false, "isData": false}) //是否收藏
            $("#loading").css('display', 'block');
            $.ajax({
                type: "post",
                url: "/system/uifavorite/addAndUpdate",
                dataType: "json",
                data: {
                    'menuUrl': pageUrl,
                    'favoriteContent': JSON.stringify(obj)
                },
                success: function (data) {
                    if (data.status == "success") {
                        $(".collection").children("span").addClass("fa-heart-o");
                        $(".collection").children("span").removeClass("fa-heart");
                        $("#loading").css('display', 'none');
                        layer.alert('取消收藏 ！', {
                            icon: 2,
                        })
                    } else if (data.status == 2) {
                        layer.alert('取消失败 ！', {
                            icon: 2,
                        })
                    }
                },
                error: function () {
                    $("#loading").css('display', 'none');
                    layer.alert(errorDefect, {
                        icon: 2,
                        title: "提示信息"
                    });
                }
            });
        }
    })
}

//获取收藏
function getCollection() {
    if (!uifavorite.search) return;
    var isResult = uifavorite.search.some(function (item) {
        return item.isData
    })
    if (isResult && uifavorite.search[0].isCollect) {
        for (var i in uifavorite.search) {
            if (uifavorite.search[i].type == "select") {
                for (var key in uifavorite.search[i].value) {
                    $("#" + key).selectpicker('val', uifavorite.search[i].value[key]);
                }
            } else {
                for (var key in uifavorite.search[i].value) {
                    $("#" + key).val(uifavorite.search[i].value[key]);
                }
            }
        }
        var tableFlag = 0;
        for (var i in uifavorite.table) {
            $("#colGroup .onesCol input[value^=" + uifavorite.table[i].value + "]").prop("checked", uifavorite.table[i].checked);
            if (uifavorite.table[i].checked) {
                $("#archivedCaseList").setGridParam().hideCol(uifavorite.table[i].value);
            } else {
                tableFlag = 1;
            }
        }
        if (tableFlag == 0) {
            $("#archivedCaseList").jqGrid('setGridState', 'hidden');
        }
        $("#archivedCaseList").setGridWidth($('.wode').width());
        $(".selectpicker").selectpicker('refresh');
        $(".collection").children("span").addClass("fa-heart");
        $(".collection").children("span").removeClass("fa-heart-o");
    } else {
        $(".collection").children("span").addClass("fa-heart-o");
        $(".collection").children("span").removeClass("fa-heart");
    }
}

// 归档案例列表
function initTable() {
    $("#loading").css('display', 'none');
    $("#archivedCaseList").jqGrid({  
        height: 'auto',
        multiselect: true, 
        postData: {},
        colNames: ['id', '案例编号', '业务类型', '模块', '测试项', '案例描述', '所属系统', '归档状态', '案例名称', '创建人', ' 操作'],
        colModel: [{name: 'id', index: 'id', hidden: true},
            {
                name: "caseNumber",
                index: "caseNumber",
                searchoptions: {sopt: ['cn']},
                formatter: function (value, grid, row, index) {
                    var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                    return '<a class="a_style" onclick="checkArchivedCase(' + rows + ')">' + row.caseNumber + '</a>'
                }
            }, {
                name: "businessType",
                index: "businessType",
                searchoptions: {sopt: ['cn']}
            }, {
                name: "moduleName",
                index: "moduleName",
                searchoptions: {sopt: ['cn']}
            }, {
                name: "testPoint",
                index: "testPoint",
                searchoptions: {sopt: ['cn']}
            }, {
                name: "caseDescription",
                index: "caseDescription",
                searchoptions: {sopt: ['cn']}
            }, {
                searchoptions: {sopt: ['cn']},
                name: "systemName",
                index: "systemName"
            }, {
                name: "archiveStatus",
                index: "archiveStatus",
                stype: 'select',
                hidden: true,
                searchoptions: {
                    value: function () {
                        var arr = {'0': ""};
                        arr[1] = "未归档";
                        arr[2] = "已归档";
                        return arr;
                    },
                    sopt: ['cn']
                },
                formatter: function (value, grid, row, index) {
                    return '已归档';
                }
            }, {
                name: "caseName",
                index: "caseName",
                hidden: true,
                searchoptions: {sopt: ['cn']},
            }, {
                name: "userName",
                index: "userName",
                searchoptions: {sopt: ['cn']},
                formatter: function (value, grid, row, index) {
                    return row.userName + " | " + row.createDate;
                }
            }, {
                name: "opt",
                index: "操作",
                align: 'center',
                width: 150,
                search: false,
                formatter: function (value, grid, row, index) {
                    var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                    var a = '<li><a  href="javascript:void(0);" class="edit-opt" onclick="edit(' + rows + ')">编辑</a></li>';
                    var c = '<li><a  href="javascript:void(0);" class="edit-opt" onclick="removeArchivedCase(' + rows + ')">案例移除</a></li>';
                    var opt = [];
                    if (testArchivedCaseEdit == true) {
                        opt.push(a);
                    }
                    if (testArchivedCaseDelete == true) {
                        opt.push(c);
                    }
                    var opt_str = opt.join("") 
                    return  `
						<li role="presentation" class="dropdown">
						<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
						<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
					</li>
				`;
                }
            },
        ],
        rowNum: 10,
        rowTotal: 200,
        rowList: [10, 20, 30],
        rownumWidth: 40,
        pager: '#casePager',
        sortname: 'id',
        viewrecords: true, //是否要显示总记录数
        jsonReader: {
            repeatitems: false,
            root: "data",
        },
        gridComplete: function () {
            var dat = $("#archivedCaseList").jqGrid('getGridParam', "postData");
            ff = JSON.stringify(dat.filters);
        },
        loadComplete: function () {
            $("#loading").css('display', 'none');
        }
    }).trigger("reloadGrid");
    $("#archivedCaseList").jqGrid('filterToolbar', {searchOperators: true});
}

// 条件搜索
function searchInfo() {
    var systemIds = $("#systemIds").val();
    if (systemIds != null && systemIds != '') {
        systemIds = systemIds.join(",");
    }
    caseNumber2 = $.trim($("#caseNumber").val());
    caseName2 = $.trim($("#caseName").val());
    caseType2 = $("#archivedCaseType").val();
    systemIds2 = systemIds == null ? '' : systemIds;
    uIds2 = $("#userId").val(),
        $("#loading").css('display', 'block');
    $("#archivedCaseList").jqGrid('setGridParam', {
    	url: "/testManage/testArchivedCase/getArchivedCases", 
        datatype: 'json',
        mtype: "POST",
        postData: {
            "caseNumber": $.trim($("#caseNumber").val()),
            "caseName": $.trim($("#caseName").val()),
            "caseType": $("#archivedCaseType").val(),
            "systemIds": systemIds,
            "uIds": $("#userId").val(),
            "filters": ""
        },
        page: 1,
        loadComplete: function () {
            $("#loading").css('display', 'none');
        }
    }).trigger("reloadGrid"); //重新载入
}

//重置条件
function clearSearch() {
    $('#caseNumber').val("");
    $('#caseName').val("");
    $('#archivedCaseType').selectpicker('val', '');
    $('#systemIds').selectpicker('val', '');
    $('#userName').val("");
    $('#userId').val("");
    searchInfo();
}

//清空表格搜索
function tableClearSreach() {
    $(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
    $(".ui-search-toolbar .ui-search-input select").val('0');
    $("#archivedCaseList").jqGrid('setGridParam', {
        postData: {
            "caseNumber": "",
            "caseName": "",
            "businessType": "",
            "moduleName": "",
            "testPoint": "",
            "caseDescription": "",
            "caseType": "",
            "systemName": "",
            "userName": ""
        },
        page: 1,
        loadComplete: function () {
            searchInfo();
            $("#loading").css('display', 'none');
        }
    }).trigger("reloadGrid"); //重新载入
}

function showUserModel() {
    selectUserIds = [];
    clearSearchUser();
    getProject();
    searchInfoUser();
}

function clearSearchUser() {
    $("#uName").val('');
    $("#companyName").val('');
    $("#deptName").val('');
    $("#userModal .color1 .btn_clear").css("display", "none");
}

function searchInfoUser() {
    $("#loading").css('display', 'block');
    var projectIds = $("#project").val();
    if (projectIds != null && projectIds != '') {
        projectIds = projectIds.join(",");
    }
    $("#userTable").bootstrapTable('destroy');
    $("#userTable").bootstrapTable({
        url: "/system/user/getAllUserModal2",
        method: "post",
        queryParamsType: "",
        pagination: true,
        sidePagination: "server",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 15, 20],
        queryParams: function (params) {
            var param = {
                projectIds: projectIds,
                userName: $.trim($("#uName").val()),
                companyName: $.trim($("#companyName").val()),
                deptName: $.trim($("#deptName").val()),
                pageNumber: params.pageNumber,
                pageSize: params.pageSize,
            }
            return param;
        },
        columns: [{
            checkbox: true,
            width: "30px"
        }, {
            field: "userName",
            title: "姓名",
            align: 'center'
        }, {
            field: "userAccount",
            title: "用户名",
            align: 'center'
        }, {
            field: "deptName",
            title: "所属部门",
            align: 'center'
        }, {
            field: "companyName",
            title: "所属公司",
            align: 'center'
        }],

        onLoadSuccess: function () {
            $("#loading").css('display', 'none');
            $("#userModal").modal("show");
        },
        onLoadError: function () {

        },
        onCheckAll: function (rows) {//全选
            for (var i = 0; i < rows.length; i++) {
                //if(selectUserIds.indexOf(rows[i])<=-1){
                selectUserIds.push(rows[i]);
                //}
            }
        },
        onUncheckAll: function (rows) {
            for (var i = 0; i < rows.length; i++) {
                if (selectUserIds.indexOf(rows[i]) > -1) {
                    selectUserIds.splice(selectUserIds.indexOf(rows[i]), 1);
                }
            }
        },
        onCheck: function (row) {//选中复选框
            //if(selectUserIds.indexOf(row)<=-1){
            selectUserIds.push(row);
            //}
        },
        onUncheck: function (row) {//取消复选框
            if (selectUserIds.indexOf(row) > -1) {
                selectUserIds.splice(selectUserIds.indexOf(row), 1);
            }
        }
    });
}

function selectMan() {
    //	var rows = $("#userTable").bootstrapTable('getSelections');
    //	var ids=[];
    //	var names='';
    //	for(var i=0;i<rows.length;i++){
    //		ids.push(rows[i].id);
    //		names+=rows[i].userName+",";
    //	}
    if (selectUserIds.length <= 0) {
        layer.alert('请选择一列数据！', {icon: 0});
        return false;
    } else {
        $("#userId").val('');
        $("#userName").val('');
        var ids = [];
        var names = [];
        for (var i = 0; i < selectUserIds.length; i++) {
            ids.push(selectUserIds[i].id);
            names.push(selectUserIds[i].userName);
        }
        $("#userId").val(ids);
        $("#userName").val(names);
    }
    $("#userModal").modal("hide");
}

//获取当前登录用户所在项目（结项的项目除外）
function getProject() {
    $("#project").empty();
    $.ajax({
        url: "/testManage/modal/getAllProject",
        dataType: "json",
        type: "post",
        async: false,
        success: function (data) {
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                if (data.projects != undefined && data.projects.length > 0) {
                    for (var i = 0; i < data.projects.length; i++) {
                        $("#project").append("<option selected value='" + data.projects[i].id + "'>" + data.projects[i].projectName + "</option>")
                    }
                }
                $('.selectpicker').selectpicker('refresh');
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

//详情
function checkArchivedCase(rows) {

    $("#loading").css('display', 'block');
    $("#checkTestCaseModal").modal("show");
    $.ajax({
        url: "/testManage/testArchivedCase/getArchivedCaseById",
        method: "post",
        data: {
            "id": rows.id
        },
        success: function (data) {
        	$("#check_Num").text(data.data.caseNumber);
            $("#check_caseName").html(data.data.caseName);
            $("#check_system").html(data.data.systemName);
            
            $("#createBy").html(data.data.userName);
            $("#createDate").html(data.data.createTime);
            $("#lastUpdateBy").html(data.data.lastUpdateUser);
            $("#lastUpdateDate").html(data.data.lastUpdateTime);
            $("#casePrecondition").html(data.data.casePrecondition);
            $("#expectResult").html(data.data.expectResult);
            $("#inputData").html(data.data.inputData);
            $("#testPoint").html(data.data.testPoint);
            $("#moduleName").html(data.data.moduleName);
            $("#businessType").html(data.data.businessType);
            $("#check_caseDescription").html(data.data.caseDescription);
            //形成表格数据
            createCaseStepTable(data.data.caseSteps == null ? "" : data.data.caseSteps);
            $("#loading").css('display', 'none');
        },
    });
}

function createCaseStepTable(data) {
    $("#caseSteps").bootstrapTable('destroy');
    $("#caseSteps").bootstrapTable({
        queryParamsType: "",
        pagination: false,
        data: data,
        columns: [{
            field: "stepOrder",
            title: "步骤",
            align: 'center',
            class: "stepOrder",
            width: '50px',
        }, {
            field: "stepDescription",
            title: "步骤描述",
            align: 'center',
        }, {
            field: "stepExpectedResult",
            title: "预期结果",
            align: 'center',
        }],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
        }
    });
    if (data.length == 0) {
        var _data = {stepOrder: '', stepDescription: '', stepExpectedResult: ''};
        $("#caseSteps").bootstrapTable('append', _data);
    }

}

// 编辑操作
function edit(rows) {
    editId = rows.id;
    $("#loading").css('display', 'block');
    $("#editArchivedCaseModal").modal("show");
    $.ajax({
        url: "/testManage/testArchivedCase/getArchivedCaseById",
        type: "post",
        dataType: "json",
        data: {
            "id": rows.id
        },
        success: function (data) {
            $("#edit_archivedCaseName").val(data.data.caseName);
            $("#edit_archivedSystem option").each(function (i, n) {
                if (n.text == data.data.systemName) {
                    n.selected = true;
                    $.ajax({
                        url: "/testManage/testCase/getCaseCatalogBySystem",
                        method: "post",
                        data: {
                            systemId: data.data.systemId,
                        },
                        success: function (data2) {
                            data2.cataLogs.map(function(val,inde){
                                var option = document.createElement("option");
                                $(option).val(val.id);
                                $(option).text(val.catalogName);
                                $('#edit_menu').append(option);
                            })
                            $("#edit_menu option").each(function (i, n) {
                                if (n.value == data.data.caseCatalogId) {
                                    n.selected = true;
                                }
                            });
                            $('.selectpicker').selectpicker('refresh');
                        },
                    });
                }
            });

            $("#edit_archivedCaseType option").each(function (i, n) {
                if (n.value == data.data.caseType) {
                    n.selected = true;
                }
            });
            $("#edit_casePrecondition").text(data.data.casePrecondition);
            $("#edit_expectResult").val(data.data.expectResult);
            $("#edit_inputData").val(data.data.inputData);
            $("#edit_testPoint").val(data.data.testPoint);
            $("#edit_moduleName").val(data.data.moduleName);
            $("#edit_businessType").val(data.data.businessType);
            $("#editCaseDescription").val(data.data.caseDescription);

            //形成表格数据
            createEditTable(data.data.caseSteps == null ? "" : data.data.caseSteps);
            $("#loading").css('display', 'none');
            $(".selectpicker").selectpicker('refresh');
        }
    });
}

function createEditTable(data) {
    $("#editCaseSteps").bootstrapTable('destroy');
    $("#editCaseSteps").bootstrapTable({
        queryParamsType: "",
        pagination: false,
        data: data,
        columns: [{
            field: "id",
            title: "id",
            class: "hideCaseID",
        }, {
            field: "stepOrder",
            title: "步骤",
            align: 'center',
            class: "stepOrder",
            width: '50px',
        }, {
            field: "stepDescription",
            title: "步骤描述",
            align: 'center',
            formatter: function (value, row, index) {
                return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock">' + row.stepDescription + '</textarea  style="resize:none;"></div>';
            }
        }, {
            field: "stepExpectedResult",
            title: "预期结果",
            align: 'center',
            formatter: function (value, row, index) {
                return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock">' + row.stepExpectedResult + '</textarea  style="resize:none;"></div>';
            }
        }, {
            field: "操作",
            title: "操作",
            align: 'center',
            class: "handleBtn",
            width: 210,
            formatter: function (value, row, index) {
                var str = '<a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | ' +
                    '<a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | ' +
                    '<a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | ' +
                    '<a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a>';
                return str;
            }
        }],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
        }
    });
    if (data.length == 0) {
        $("#editCaseSteps>tbody").empty();
    }

}


//表格四种操作
function addSteps() {
    var i = $("#editCaseSteps>tbody>tr").length + 1;
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">' + i + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
    $("#editCaseSteps>tbody").append(str);
    changeStepOrder2();
}

function addEditTableRow(This) {

    var i = $("#editCaseSteps>tbody>tr").length + 1;
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">' + i + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
    $(This).parent().parent().after(str);
    changeStepOrder2()
}

function delEditTableRow(This) {
    $(This).parent().parent().remove();
    changeStepOrder2();
}

function upTableRow(This) {
    var $tr = $(This).parents("tr");
    if ($tr.index() == 0) {
        alert("首行数据不可上移");
    } else {
        $tr.fadeOut(200).fadeIn(100).prev().fadeOut(100).fadeIn(100);
        $tr.prev().before($tr);
        changeStepOrder2();a
    }
}

function downTableRow(This) {
    var $tr = $(This).parents("tr");
    if ($tr.index() == $("#editCaseSteps>tbody>tr").length - 1) {
        alert("最后一条数据不可下移");
    } else {
        $tr.fadeOut(200).fadeIn(100).next().fadeOut(100).fadeIn(100);
        $tr.next().after($tr);
        changeStepOrder2();
    }
}

//动态改变表格步骤值
function changeStepOrder2() {
    for (var i = 0; i < $("#editCaseSteps>tbody>tr").length; i++) {
        $("#editCaseSteps>tbody>tr").eq(i).find(".stepOrder").text((i + 1));
    }
}

//编辑提交
function editTestCase2() {
    $('#editTestCaseForm2').data('bootstrapValidator').validate();
    if (!$('#editTestCaseForm2').data("bootstrapValidator").isValid()) {
        return;
    }
    var editCaseStep = []
    for (var i = 0; i < $("#editCaseSteps>tbody>tr").length; i++) {
        var obj = {}
        id = $("#editCaseSteps>tbody>tr").eq(i).find(".hideCaseID").text();
        if (id != '') {
            obj.id = $("#editCaseSteps>tbody>tr").eq(i).find(".hideCaseID").text();
        }
        obj.stepOrder = $("#editCaseSteps>tbody>tr").eq(i).find(".stepOrder").text();
        obj.stepDescription = $("#editCaseSteps>tbody>tr").eq(i).find(".def_tableInput").eq(0).val();
        obj.stepExpectedResult = $("#editCaseSteps>tbody>tr").eq(i).find(".def_tableInput").eq(1).val();
        editCaseStep.push(obj);
    }
    $.ajax({
        url: "/testManage/testArchivedCase/editCaseInfo",
        method: "post", 
        data: {
        	tblCaseInfo : JSON.stringify({
        		"id": editId,
                "caseName": $("#edit_archivedCaseName").val(),
                "systemId": $('#edit_archivedSystem option:selected').val(),
                "caseType": $('#edit_archivedCaseType option:selected').val(),
                "casePrecondition": $("#edit_casePrecondition").val(),
                'expectResult': $('#edit_expectResult').val(),
                'inputData': $('#edit_inputData').val(),
                'testPoint': $('#edit_testPoint').val(),
                'moduleName': $('#edit_moduleName').val(),
                'businessType': $('#edit_businessType').val(),
                'caseCatalogId': $('#edit_menu').val(),
                'caseDescription': $('#editCaseDescription').val(),
            }),
        	caseSteps : JSON.stringify(editCaseStep)
        }, 
        success: function (data) {
            layer.alert('编辑成功!', {
                icon: 1,
            })
            $("#editArchivedCaseModal").modal("hide");
            searchInfo();
        },
    });
}

//移除归档案例
function removeArchivedCase(rows) {
    var ids = [];
    ids.push(rows.id);
    layer.alert('确认要移除此案例？', {
        icon: 0,
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: "/testManage/testArchivedCase/removeArchivedTest",
            method: "post",
            data: {
                "ids": ids
            },
            success: function (data) {
            	var zTree = $.fn.zTree.getZTreeObj("menuTree");
                node = zTree.getSelectedNodes();
                node[0].caseCount--;
                zTree.updateNode( node );
                $(".ztree li a.curSelectedNode>.tree_case_num").text("案例数:"+node[0].caseCount)
                initTable();
                layer.alert('移除成功!', {
                    icon: 1,
                })
            },
        });
    })
}

//移除案例（批量移除）
function removeArchivedCase_btn() {
    var ids = [];
    var ids = $('#archivedCaseList').jqGrid('getGridParam', 'selarrrow')
    if (ids.length == 0) {
        layer.alert('请至少选择一个案例移除!', {
            icon: 0,
        })
        return;
    } else {
        layer.alert('确认要移除所选中的案例？', {
            icon: 0,
            btn: ['确定', '取消']
        }, function () {
            $.ajax({
                url: "/testManage/testArchivedCase/removeArchivedTest",
                method: "post",
                data: {
                    "ids": ids
                },
                success: function (data) { 
                	var zTree = $.fn.zTree.getZTreeObj("menuTree");
                	node = zTree.getSelectedNodes();  
                	node[0].caseCount = node[0].caseCount - ids.length;
                    zTree.updateNode( node );
                	$(".ztree li a.curSelectedNode>.tree_case_num").text("案例数:"+node[0].caseCount)
                    initTable();
                    layer.alert('移除成功!', {
                        icon: 1,
                    })
                },
            });
        })
    }
}

//导出案例
function exportTestCase_btn() {
    window.location.href = "/testManage/testArchivedCase/exportExcel?caseNumber=" + caseNumber2 + "&caseName=" + caseName2 + "&caseType=" + caseType2 +
        "&systemIds=" + systemIds2 + "&uIds=" + uIds2 + "&filters=" + ff + "&caseCatalogId=" + mydata.caseTreeNode.realId;

}

//显示导入案例弹窗
function importArchivedCase_btn() {
    $("#upfile").val("");
    $("#importPerson").modal("show");
}

//导出模板
function exportTemplate() {
    window.location.href = "/testManage/testArchivedCase/exportTemplet";
}

//导入
function upload() {
    var formData = new FormData();
    formData.append("file", document.getElementById("upfile").files[0]);
    formData.append("caseCatalogId",  mydata.caseTreeNode.realId  );
    if (document.getElementById("upfile").files[0] == undefined) {
        layer.alert("请选择文件", {icon: 0});
        return;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/zuul/testManage/testArchivedCase/importExcel",
        type: "POST",
        data: formData,

        /**必须false才会自动加上正确的Content-Type*/

        contentType: false,

        /*必须false才会避开jQuery对 formdata 的默认处理
        XMLHttpRequest会对 formdata 进行正确的处理*/

        processData: false,
        success: function (data) {
            if (data.status == 2) {
                layer.alert("导入失败，原因:" + data.errorMessage, {icon: 2});
                $("#importPerson").modal("hide");
            } else {
                var zTree = $.fn.zTree.getZTreeObj("menuTree");
                node = zTree.getSelectedNodes();
                node[0].caseCount = node[0].caseCount + data.caseCount?data.caseCount:0 ;
                zTree.updateNode( node );
                $(".ztree li a.curSelectedNode>.tree_case_num").text("案例数:"+data.caseCount)

                layer.alert("导入成功", {icon: 1});
                $("#importPerson").modal("hide");
                searchInfo();
            }
            $("#loading").css('display', 'none');
        },
        error: function () {
            layer.alert("上传失败！", {icon: 2});
            $("#loading").css('display', 'none');
        }
    });
}

/*---------------新建案例操作start--------------------*/
function newTestCase_btn2() { 
    newTestCase_reset();
    $("#newTestCaseForm").data('bootstrapValidator').destroy();
    formValidator();
    createAddTable(); 
    $(".new_menuSelect").css("display","block"); 
	$.ajax({
        url: "/testManage/testCase/getCaseCatalogBySystem",
        method: "post", 
        data: {
        	systemId:mydata.caseTreeNode.systemId,
        },
        success: function (data) {
        	$("#new_menu").empty();
        	data.cataLogs.map(function(val,inde){
        		 var option = document.createElement("option"); 
                 $(option).val(val.id); 
                 $(option).text(val.catalogName);
                 $('#new_menu').append(option);
        	}) 
        	$( "#new_menu" ).val( mydata.caseTreeNode.realId ) 
    	    $( "#new_system" ).val( mydata.caseTreeNode.systemId )  
    	    $('.selectpicker').selectpicker('refresh');
    	    
        	$("#newTestCaseModal").modal("show")
        },
    });  ;
}

function createAddTable() {
    var data = [];
    $("#addCaseSteps").bootstrapTable('destroy');
    $("#addCaseSteps").bootstrapTable({
        queryParamsType: "",
        data: data,
        columns: [
            {
                field: "stepOrder",
                title: "步骤",
                align: 'center',
                class: "stepOrder",
                width: '50px',
            }, {
                field: "stepDescription",
                title: "步骤描述",
                align: 'center',
                formatter: function (value, row, index) {
                    return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div>';
                }
            }, {
                field: "stepExpectedResult",
                title: "预期结果",
                align: 'center',
                formatter: function (value, row, index) {
                    return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div>';
                }
            }, {
                field: "操作",
                title: "操作",
                align: 'center',
                width: 210,
                formatter: function (value, row, index) {
                    var str = '<a class="a_style" style="cursor:pointer" onclick="addTableRow( this )">增加下一步骤 </a> | ' +
                        '<a class="a_style defColor" style="cursor:pointer" onclick="delTableRow(this)">删除</a> | ' +
                        '<a class="a_style" style="cursor:pointer" onclick="upAddTableRow(this)">上移</a> | ' +
                        '<a class="a_style" style="cursor:pointer" onclick="downAddTableRow(this)">下移</a>';
                    return str;
                }
            }],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
        }
    });
    if (data.length == 0) {
        $("#addCaseSteps>tbody").empty();
    }
}

function down(This) {
    if ($(This).hasClass("fa-angle-double-down")) {
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
        $(This).parents('.allInfo').children(".connect_div").slideDown(100);
    } else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
        $(This).parents('.allInfo').children(".connect_div").slideUp(100);
    }
}

//案例步骤表格四种操作
function addCaseSteps() {
    var i = $("#addCaseSteps>tbody>tr").length + 1;
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">' + i + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="addTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upAddTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downAddTableRow(this)">下移</a></td> </tr>';
    $("#addCaseSteps>tbody").append(str);
    changeStepOrder();
}

function addTableRow(This) {

    var i = $("#addCaseSteps>tbody>tr").length + 1;
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">' + i + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="addTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upAddTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downAddTableRow(this)">下移</a></td> </tr>';
    $(This).parent().parent().after(str);
    changeStepOrder()
}

function delTableRow(This) {
    $(This).parent().parent().remove();
    changeStepOrder();
}

function upAddTableRow(This) {
    var $tr = $(This).parents("tr");
    if ($tr.index() == 0) {
        alert("首行数据不可上移");
    } else {
        $tr.fadeOut(200).fadeIn(100).prev().fadeOut(100).fadeIn(100);
        $tr.prev().before($tr);
        changeStepOrder();
    }
}

function downAddTableRow(This) {
    var $tr = $(This).parents("tr");
    if ($tr.index() == $("#addCaseSteps>tbody>tr").length - 1) {
        alert("最后一条数据不可下移");
    } else {
        $tr.fadeOut(200).fadeIn(100).next().fadeOut(100).fadeIn(100);
        $tr.next().after($tr);
        changeStepOrder();
    }
}

function addStep_btn() {
    var i = $("#editCaseSteps>tbody>tr").length + 1;
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">' + i + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
    $("#editCaseSteps").append(str);
}

//动态改变表格步骤值
function changeStepOrder() {
    for (var i = 0; i < $("#addCaseSteps>tbody>tr").length; i++) {
        $("#addCaseSteps>tbody>tr").eq(i).find(".stepOrder").text((i + 1));
    }
}

//新建提交
function submitTestCase() {
    $('#newTestCaseForm').data('bootstrapValidator').validate();
    if (!$('#newTestCaseForm').data("bootstrapValidator").isValid()) {
        return;
    }
    var CaseStep = [];
    for (var i = 0; i < $("#addCaseSteps>tbody>tr").length; i++) {
        var obj = {}
        obj.stepOrder = $("#addCaseSteps>tbody>tr").eq(i).find(".stepOrder").text();
        obj.stepDescription = $("#addCaseSteps>tbody>tr").eq(i).find(".def_tableInput").eq(0).val();
        if (obj.stepDescription == "") {
            layer.alert("第" + (i + 1) + "行步骤描述为空", {icon: 0});
            return;
        }
        obj.stepExpectedResult = $("#addCaseSteps>tbody>tr").eq(i).find(".def_tableInput").eq(1).val();
        if (obj.stepExpectedResult == "") {
            layer.alert("第" + (i + 1) + "行预期结果为空", {icon: 0});
            return;
        }
        CaseStep.push(obj);
    }
    $.ajax({
        type: "post",
        url: "/testManage/testArchivedCase/insertArchivedCase",
        dataType: "json", 
        data:{
        	tblCaseInfo : JSON.stringify({
        		 'systemId': $('#new_system option:selected').val(),
                 'caseType': $('#new_caseType option:selected').val(),
                 'caseName': $('#new_caseName').val(),
                 'casePrecondition': $('#new_precodition').val(),
                 'expectResult': $('#new_expectResult').val(),
                 'inputData': $('#new_inputData').val(),
                 'testPoint': $('#new_testPoint').val(),
                 'moduleName': $('#new_moduleName').val(),
                 'businessType': $('#new_businessType').val(),
                 'caseCatalogId': $('#new_menu').val(),
                 'caseDescription': $('#addCaseDescription').val(),
            }),
        	caseSteps :  JSON.stringify(CaseStep)
        }, 
        success: function (data) {
            var zTree = $.fn.zTree.getZTreeObj("menuTree");
            node = zTree.getSelectedNodes();
            node[0].caseCount++;
            zTree.updateNode( node );
            $(".ztree li a.curSelectedNode>.tree_case_num").text("案例数:"+node[0].caseCount);
            layer.alert('新增测试案例成功!', {
                icon: 1,
            }, function (index) {
                $("#newTestCaseModal").modal("hide");
                layer.close(index);
                searchInfo();
            })
        }
    });
}

/*---------------新建案例操作end----------------------*/


// 新建：清空
function newTestCase_reset() {
    $("#new_system").selectpicker('val', '');
    $("#new_caseType").selectpicker('val', '');
    $("#new_caseName").val('');
    $("#new_precodition").val('');
    $('#new_expectResult').val('');
    $('#new_inputData').val('');
    $('#new_testPoint').val('');
    $('#new_moduleName').val('');
    $('#new_businessType').val('');
    $('#addCaseDescription').val('');
}

//新增案例涉及系统的下拉框
function showSystems() {
    $.ajax({
        url: "/testManage/testCase/getAllSystem",
        method: "post",
        dataType: "json",
        success: function (data) {
            var list = data.data;
            for (var i = 0; i < list.length; i++) {
                var option = document.createElement("option");
                $(option).val(list[i].id);
                $(option).text(list[i].systemName);
                $('#edit_archivedSystem').append(option);
            }
            for (var i = 0; i < list.length; i++) {
                var option = document.createElement("option");
                $(option).val(list[i].id);
                $(option).text(list[i].systemName);
                $('#systemIds').append(option);
            }
            for (var i = 0; i < list.length; i++) {
                var option = document.createElement("option");
                $(option).val(list[i].id);
                $(option).text(list[i].systemName);
                $('#new_system').append(option);
            }
            $('.selectpicker').selectpicker('refresh');
            collect_handle();
            if (!uifavorite) return;
            getCollection();
        }

    });
}

function addCheckBox2() {
    $("#colGroup2").empty();
    var str = "";
    str = '<div class="onesCol"><input type="checkbox" value="caseNumber" onclick="showHideCol( this )" /><span>案例编号</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="caseName" onclick="showHideCol( this )" /><span>案例名称</span></div>' +
        //不需要案例类型 '<div class="onesCol"><input type="checkbox" value="caseType" onclick="showHideCol( this )" /><span>案例类型</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="systemName" onclick="showHideCol( this )" /><span>所属系统</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="userName" onclick="showHideCol( this )" /><span>创建人</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="opt" onclick="showHideCol( this )" /><span>操作</span></div>';
    $("#colGroup2").append(str)
}

function showHideCol(This) {
    var colModel = $("#archivedCaseList").jqGrid('getGridParam', 'colModel');
    var width = 0;//获取当前列的列宽
    var arr = [];
    for (var i = 0; i < colModel.length; i++) {
        if (colModel[i]["hidden"] == false) {
            arr.push(colModel[i]["hidden"]);
        }
    }
    if ($(This).is(':checked')) {
        $("#archivedCaseList").setGridParam().hideCol($(This).attr('value'));
        $("#archivedCaseList").setGridWidth($('.wode').width());
        if (arr.length == 1) {
            $("#archivedCaseList").jqGrid('setGridState', 'hidden');
        }
    } else {
        $("#archivedCaseList").jqGrid('setGridState', 'visible');
        $("#archivedCaseList").setGridParam().showCol($(This).attr('value'));
        $("#archivedCaseList").setGridWidth($('.wode').width());
    }
}

function down(This) {
    if ($(This).hasClass("fa-angle-double-down")) {
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
    } else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
    }
}

function formValidator() {
    $('#newTestCaseForm').bootstrapValidator({
        message: '输入有误',//通用的验证失败消息
        feedbackIcons: {//根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            new_caseName: {
                validators: {
                    notEmpty: {
                        message: '案例名称不能为空'
                    },
                    stringLength: {
                        max: 100,
                        message: '案例名称长度必须小于100字符'
                    }
                }
            },
            /*new_expectResult: {
                    validators: {
                            notEmpty: {
                                    message: '预期结果不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '预期结果长度必须小于500字符'
                            }
                    }
            },
            new_inputData: {
                    validators: {
                            notEmpty: {
                                    message: '输入数据不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '输入数据长度必须小于500字符'
                            }
                    }
            },
            new_testPoint: {
                    validators: {
                            notEmpty: {
                                    message: '测试项不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '测试项长度必须小于500字符'
                            }
                    }
            },
            new_moduleName: {
                    validators: {
                            notEmpty: {
                                    message: '模块不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '模块长度必须小于500字符'
                            }
                    }
            },
            new_businessType: {
                    validators: {
                            notEmpty: {
                                    message: '业务类型不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '业务类型长度必须小于500字符'
                            }
                    }
            },*/
            new_system: {
                validators: {
                    notEmpty: {
                        message: '涉及系统不能为空'
                    }
                }
            },
            new_caseType: {
                validators: {
                    notEmpty: {
                        message: '案例类型不能为空'
                    }
                }
            }
        }
    });
    $('#editTestCaseForm2').bootstrapValidator({
        message: '输入有误',//通用的验证失败消息
        feedbackIcons: {//根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            edit_archivedCaseName: {
                validators: {
                    notEmpty: {
                        message: '案例名称不能为空'
                    },
                    stringLength: {
                        max: 100,
                        message: '案例名称长度必须小于100字符'
                    }
                }
            },
            /*edit_expectResult: {
                    validators: {
                            notEmpty: {
                                    message: '预期结果不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '预期结果长度必须小于500字符'
                            }
                    }
            },
            edit_inputData: {
                    validators: {
                            notEmpty: {
                                    message: '输入数据不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '输入数据长度必须小于500字符'
                            }
                    }
            },
            edit_testPoint: {
                    validators: {
                            notEmpty: {
                                    message: '测试项不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '测试项长度必须小于500字符'
                            }
                    }
            },
            edit_moduleName: {
                    validators: {
                            notEmpty: {
                                    message: '模块不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '模块长度必须小于500字符'
                            }
                    }
            },
            edit_businessType: {
                    validators: {
                            notEmpty: {
                                    message: '业务类型不能为空'
                            } ,
                            stringLength: {
                                    max:300,
                                    message: '业务类型长度必须小于500字符'
                            }
                    }
            },*/
            edit_archivedSystem: {
                validators: {
                    notEmpty: {
                        message: '涉及系统不能为空'
                    }
                }
            },
            edit_archivedCaseType: {
                validators: {
                    notEmpty: {
                        message: '案例类型不能为空'
                    }
                }
            }
        }
    });
}
