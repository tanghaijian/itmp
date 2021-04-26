/**
 * Description: 测试案例管理
 */

var span_ = "<span>&nbsp;|&nbsp;</span>";
var aids = [], selectUserIds = [];
var editId = '', caseNumber2 = '', caseName2 = '', caseType2 = '', systemIds2 = '', archiveStatus2 = '', uIds2 = '';
var ff = null;
var tableIdArr = ['userTable'];
$(document).ready(function () {
    initTable();
    showSystems();
    formValidator();
    addCheckBox();
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
    $("#edit_system").on('change', function(){
    	$(".edit_menuSelect").css("display","block");
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
                "value": {"caseType": $("#caseType").val()},
                "isData": _is_null($("#caseType"))
            });
            obj.search.push({
                "type": "select",
                "value": {"systemId": $("#systemId").val()},
                "isData": _is_null($("#systemId"))
            });
            //第二行
            obj.search.push({
                "type": "select",
                "value": {"archiveStatus": $("#archiveStatus").val()},
                "isData": _is_null($("#archiveStatus"))
            });
            obj.search.push({
                "type": "input",
                "value": {"userName": $("#userName").val()},
                "isData": _is_null($("#userName"))
            });
            var isResult = obj.search.some(function (item) {
                return item.isData
            })
            if (!isResult) return;
            $("#loading").css('display', 'block');
            //表格数据
            for (var i = 0; i < $('#colGroup .onesCol').length; i++) {
                obj.table.push({
                    "value": $('#colGroup .onesCol input').eq(i).attr('value'),
                    "checked": $('#colGroup .onesCol input').eq(i).prop('checked')
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
            obj.search.push({"isCollect": false, "isData": false})
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

//新增案例涉及系统的下拉框
function showSystems() {
    $.ajax({
        url: "/testManage/testCase/getAllSystem",
        method: "post",
        dataType: "json",
        success: function (data) {
            var list = data.data;
            system_arr = data.data;
            //获取后台收藏的数据，放在ready的最下面!!!!
            for (var i = 0; i < list.length; i++) {
                //先创建好select里面的option元素
                var option = document.createElement("option");
                //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值
                $(option).val(list[i].id);
                //给option的text赋值,这就是你点开下拉框能够看到的东西
                $(option).text(list[i].systemName);
                //获取select 下拉框对象,并将option添加进select
                $('#new_system').append(option);
            }
            for (var i = 0; i < list.length; i++) {
                //先创建好select里面的option元素
                var option = document.createElement("option");
                //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值
                $(option).val(list[i].id);
                //给option的text赋值,这就是你点开下拉框能够看到的东西
                $(option).text(list[i].systemName);
                //获取select 下拉框对象,并将option添加进select
                $('#edit_system').append(option);
            }
            for (var i = 0; i < list.length; i++) {
                //先创建好select里面的option元素
                var option = document.createElement("option");
                //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值
                $(option).val(list[i].id);
                //给option的text赋值,这就是你点开下拉框能够看到的东西
                $(option).text(list[i].systemName);
                //获取select 下拉框对象,并将option添加进select
                $('#systemId').append(option);
            }
            $('.selectpicker').selectpicker('refresh');
            collect_handle();
            if (!uifavorite) return
            getCollection();
        }
    });
}

// 获取收藏
function getCollection() {
    if (!uifavorite.search) return
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
                $("#caseList").setGridParam().hideCol(uifavorite.table[i].value);
            } else {
                tableFlag = 1;
            }
        }
        if (tableFlag == 0) {
            $("#caseList").jqGrid('setGridState', 'hidden');
        }
        $("#caseList").setGridWidth($('.wode').width());
        $(".selectpicker").selectpicker('refresh');
        $(".collection").children("span").addClass("fa-heart");
        $(".collection").children("span").removeClass("fa-heart-o");
    } else {
        $(".collection").children("span").addClass("fa-heart-o");
        $(".collection").children("span").removeClass("fa-heart");
    }
}

// 条件搜索
function searchInfo() {
    var systemIds = $("#systemId").val();
    if (systemIds != null && systemIds != '') {
        systemIds = systemIds.join(",");
    }
    caseNumber2 = $.trim($("#caseNumber").val());
    caseName2 = $.trim($("#caseName").val());
    caseType2 = $("#caseType").val();
    systemIds2 = systemIds == null ? '' : systemIds;
    archiveStatus2 = $("#archiveStatus").val();
    uIds2 = $("#userId").val(),
        $("#loading").css('display', 'block');
    $("#caseList").jqGrid('setGridParam', { 
    	url: "/testManage/testCase/getCaselist",
        postData: {
            "caseNumber": $.trim($("#caseNumber").val()),
            "caseName": $.trim($("#caseName").val()),
            "caseType": $("#caseType").val(),
            "systemIds": systemIds,
            "archiveStatus": $("#archiveStatus").val(),
            "uIds": $("#userId").val(),
            "filters": ""
        },
        page: 1,
        //        beforeRequest : function(){
        //        	var dat=$("#caseList").jqGrid('getGridParam', "postData");
        //        },
        loadComplete: function () {
            $("#loading").css('display', 'none');
        }
    }).trigger("reloadGrid"); //重新载入
}

// 测试案例列表
function initTable() {
    $("#loading").css('display', 'none'); 
    $("#caseList").jqGrid({ 
        datatype: 'json', 
        height: 'auto',
        multiselect: true, 
        postData: {},
        colNames: ['id', '案例编号', '业务类型', '模块', '测试项', '案例描述', '所属系统', '归档状态', '创建人', '案例名称', ' 操作'],
        colModel: [{name: 'id', index: 'id', hidden: true},
            {
                name: "caseNumber",
                index: "caseNumber",
                searchoptions: {sopt: ['cn']},
                formatter: function (value, grid, row, index) {
                    var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                    return '<a class="a_style" onclick="checkCase(' + rows + ')">' + row.caseNumber + '</a>'
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
                searchoptions: {
                    value: function () {
                        var arr = {'0': "请选择"};
                        arr[1] = "未归档";
                        arr[2] = "已归档";
                        return arr;
                    },
                    sopt: ['cn']
                },
                formatter: function (value, grid, row, index) {
                    if (row.archiveStatus == 1) {
                        return '未归档';
                    }
                    if (row.archiveStatus == 2) {
                        return '已归档';
                    }
                }
            }, {
                name: "userName",
                index: "userName",
                searchoptions: {sopt: ['cn']},
                formatter: function (value, grid, row, index) {
                    return row.userName + " | " + row.createDate;
                }
            }, {
                name: "caseName",
                index: "caseName", 
                searchoptions: {sopt: ['cn']},
            },  {
                name: "opt",
                index: "操作",
                align: 'center',
                width: 150,
                search: false,
                formatter: function (value, grid, row, index) {
                    var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                    var opt = [];
                    var a = '<li><a  href="javascript:void(0);" class="edit-opt" onclick="edit(' + rows + ')">编辑</a></li>';
                    if (testCaseEdit == true) {
                        opt.push(a);
                    }
                    if (row.archiveStatus == 1 && testCaseArchive == true) {
                        var b = '<li><a  href="javascript:void(0);" class="edit-opt" onclick="archive(' + rows + ')">归档</a></li>';
                        opt.push(b);
                    }
                    var c = '<li><a  href="javascript:void(0);" class="edit-opt" onclick="deleteTestCase(' + rows + ')">删除</a></li>';
                    if (testCaseDelete == true) {
                        opt.push(c);
                    }
                    var optstr = opt.join("");
                     
                    return  `
						<li role="presentation" class="dropdown">
						<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
						<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${optstr}</ul>
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
            var dat = $("#caseList").jqGrid('getGridParam', "postData");
            ff = JSON.stringify(dat.filters); 
        },
        loadComplete: function () {
            $("#loading").css('display', 'none');
        }
    }).trigger("reloadGrid");
    $("#caseList").jqGrid('filterToolbar', {searchOperators: true});

}


//重置条件
function clearSearch() {
    $('#caseNumber').val("");
    $('#caseName').val("");
    $('#caseType').selectpicker('val', '');
    $('#systemId').selectpicker('val', '');
    $('#archiveStatus').selectpicker('val', '');
    $('#userName').val("");
    $('#userId').val("");
    searchInfo();
}

//清空表格搜索
function tableClearSreach() {
    $(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
    $(".ui-search-toolbar .ui-search-input select").val('0');
    $("#caseList").jqGrid('setGridParam', { 
    	url: "/testManage/testCase/getCaselist",
        postData: { 
            "filters": ""
        },
        page: 1, 
        loadComplete: function () {
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
    //	var rows = select_rows;
    //	var ids=[];
    //	var names=[];
    //	for(var i=0;i<rows.length;i++){
    //		ids.push(rows[i].id);
    //		names+=rows[i].userName+",";
    //		names.push(rows[i].userName);
    //	}
    if (selectUserIds.length <= 0) {
        layer.alert('请选择一列数据！', {icon: 0});
        return false;
    } else {
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
function checkCase(rows) {

    //	$("#datas").bootstrapTable('destroy');
    //  			$("#datas tbody").html("");
    $("#loading").css('display', 'block');
    $("#checkTestCaseModal").modal("show");
    $.ajax({
        url: "/testManage/testCase/selectCaseInfoById",
        method: "post",
        data: {
            "id": rows.id
        },
        success: function (data) {
            $("#check_caseName").text(data.data.caseName);
            $("#check_system").text(data.data.systemName);
            if (data.data.caseType == 1) {
                $("#check_caseType").html("正面案例");
                $("#check_caseType").css("background", "green");
            } else {
                $("#check_caseType").html("反面案例");
                $("#check_caseType").css("background", "red");
            }
            $("#check_Num").text(data.data.caseNumber || '')
            $("#createBy").text(data.data.userName || '');
            $("#createDate").text(data.data.createTime || '');
            $("#lastUpdateBy").text(data.data.lastUpdateUser || '');
            $("#lastUpdateDate").text(data.data.lastUpdateTime || '');
            $("#casePrecondition").text(data.data.casePrecondition || '');
            $("#expectResult").text(data.data.expectResult || '');
            $("#inputData").text(data.data.inputData || '');
            $("#testPoint").text(data.data.testPoint || '');
            $("#moduleName").text(data.data.moduleName || '');
            $("#businessType").text(data.data.businessType || '');
            $("#check_caseDescription").text(data.data.caseDescription || '');
            showField(data.field)
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
    $.ajax({
        url: "/testManage/testCase/selectCaseInfoById",
        type: "post",
        dataType: "json",
        data: {
            "id": rows.id
        },
        success: function (data) {
        	$("#edit_menu").empty();
            $("#edit_caseName").val(data.data.caseName);
            $("#edit_system option").each(function (i, n) {
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
            $("#edit_caseType option").each(function (i, n) {
                if (n.value == data.data.caseType) {
                    n.selected = true;
                }
            });
            $("#edit_casePrecondition").text(data.data.casePrecondition || '');
            $("#edit_expectResult").val(data.data.expectResult);
            $("#edit_inputData").val(data.data.inputData);
            $("#edit_testPoint").val(data.data.testPoint);
            $("#edit_moduleName").val(data.data.moduleName);
            $("#edit_businessType").val(data.data.businessType);
            $("#editCaseDescription").val(data.data.caseDescription);
            if (data.field != undefined) {
                for (var i = 0; i < data.field.length; i++) {
                    appendDataType(data.field[i], 'editFieldDiv', 'edit');
                }
            }
            //形成表格数据
            createEditTable(data.data.caseSteps == null ? "" : data.data.caseSteps);
            $(".selectpicker").selectpicker('refresh');
            $("#loading").css('display', 'none');
            $("#editCaseModal").modal("show");
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
            width: '210px',
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
        //	    var _data={ stepOrder:1,stepDescription:'',stepExpectedResult:'' };
        //	    $("#editCaseSteps").bootstrapTable('append',_data );
        $("#editCaseSteps>tbody").empty();
    }

}


//表格四种操作
function addSteps() {
    var i = $("#editCaseSteps>tbody>tr").length + 1;
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">' + i + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height:84px; "><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
    $("#editCaseSteps>tbody").append(str);
    changeStepOrder2();
}

function addEditTableRow(This) {
    /*
    var _data={};
    $("#editCaseSteps").bootstrapTable('insertRow', { index:( $("#editCaseSteps>tbody>tr	").index( $( This ).parent().parent() ) +  1 ) ,row:_data });*/

    var i = $("#editCaseSteps>tbody>tr").length + 1;
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">' + i + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height:84px; "><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
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
        changeStepOrder2();
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

function addStep_btn() {
    var i = $("#editCaseSteps>tbody>tr").length + 1;
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">' + i + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height:84px; "><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
    $("#editCaseSteps").append(str);
}

//动态改变表格步骤值
function changeStepOrder2() {
    for (var i = 0; i < $("#editCaseSteps>tbody>tr").length; i++) {
        $("#editCaseSteps>tbody>tr").eq(i).find(".stepOrder").text((i + 1));
    }
}

//编辑提交
function editTestCase() {
    $('#editTestCaseForm').data('bootstrapValidator').validate();
    if (!$('#editTestCaseForm').data("bootstrapValidator").isValid()) {
        return;
    }
    var editCaseStep = []
    for (var i = 0; i < $("#editCaseSteps>tbody>tr").length; i++) {
        var obj = {}
        //		obj.id=$( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".hideCaseID" ).text();
        id = $("#editCaseSteps>tbody>tr").eq(i).find(".hideCaseID").text();
        if (id != '') {
            obj.id = $("#editCaseSteps>tbody>tr").eq(i).find(".hideCaseID").text();
        }
        obj.stepOrder = $("#editCaseSteps>tbody>tr").eq(i).find(".stepOrder").text();
        obj.stepDescription = $("#editCaseSteps>tbody>tr").eq(i).find(".def_tableInput").eq(0).val();
        obj.stepExpectedResult = $("#editCaseSteps>tbody>tr").eq(i).find(".def_tableInput").eq(1).val();
        editCaseStep.push(obj);
    }
    var fieldTemplate = getFieldData("editFieldDiv");
    for (var i = 0; i < fieldTemplate.field.length; i++) {
        if (fieldTemplate.field[i].required == "false") {
            if (fieldTemplate.field[i].valueName == "" || fieldTemplate.field[i].valueName == null || fieldTemplate.field[i].valueName == undefined) {
                $("#loading").css('display', 'none');
                layer.alert(fieldTemplate.field[i].labelName + "不能为空", {
                    icon: 2,
                    title: "提示信息"
                });
                return;
            }
        }
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/testCase/editCaseInfo",
        method: "post",
        data: {
        	tblCaseInfo : JSON.stringify({
                "id": editId,
                "caseName": $("#edit_caseName").val(),
                "systemId": $('#edit_system option:selected').val(),
                "caseType": $('#edit_caseType option:selected').val(),
                "casePrecondition": $("#edit_casePrecondition").val(),
                'expectResult': $('#edit_expectResult').val(),
                'inputData': $('#edit_inputData').val(),
                'testPoint': $('#edit_testPoint').val(),
                'moduleName': $('#edit_moduleName').val(),
                'businessType': $('#edit_businessType').val(),
                'caseCatalogId': $('#edit_menu').val(),
                'caseDescription': $('#editCaseDescription').val(),
                'fieldTemplate': JSON.stringify(fieldTemplate)
            }),
            "caseSteps":JSON.stringify( editCaseStep )
        },
        success: function (data) {
            layer.alert('编辑成功!', {
                icon: 1,
            })
            $("#loading").css('display', 'none');
            $("#editCaseModal").modal("hide");
            initTable();
        },
    });
}

// 归档操作
function archive(rows) {
    findArchivedId();
    var ids = [0];
    if (aids.indexOf(rows.caseNumber) == -1) {
        ids.push(rows.caseNumber);
    }
    ;
    layer.alert('确认归档此案例？', {
        icon: 0,
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: "/testManage/testCase/archivingCase",
            method: "post",
            data: {
                "ids": ids
            },
            success: function (data) {
                initTable();
                layer.alert('案例已归档!', {
                    icon: 1,
                })
            },
        });
    })
}

//批量归档
function archiveToCaseLib_btn() {
    findArchivedId();
    //	var ids=[0];
    var ids = $('#caseList').jqGrid('getGridParam', 'selarrrow');
    if (ids.length == 0) {
        layer.alert('请至少选择一个案例进行归档!', {
            icon: 0,
        })
        return;
    } else {
        var caseNumbers = [];
        var newCaseNumbers = [];
        $.each(ids, function (index, value) {
            var rowDate = $("#caseList").jqGrid("getRowData", value);
            var caseNumber = rowDate.caseNumber;
            caseNumbers.push(caseNumber.substring(caseNumber.indexOf('>') + 1, caseNumber.lastIndexOf('<')));
            newCaseNumbers.push(caseNumber.substring(caseNumber.indexOf('>') + 1, caseNumber.lastIndexOf('<')));
        })
        for (var i = 0; i < caseNumbers.length; i++) {
            for (var j = 0; j < aids.length; j++) {
                if (caseNumbers[i] == aids[j]) {
                    newCaseNumbers.splice(i, 1, "");
                }
            }
        }
        newCaseNumbers.push("");
        layer.alert('确认要对所选中的案例进行归档？', {
            icon: 0,
            btn: ['确定', '取消']
        }, function () {
            $.ajax({
                url: "/testManage/testCase/archivingCase",
                method: "post",

                data: {
                    "ids": newCaseNumbers
                },
                success: function (data) {
                    initTable();
                    layer.alert('案例已归档!', {
                        icon: 1,
                    })
                },
            });
        })
    }
}

//查询所有归档案例的id
function findArchivedId() {
    $.ajax({
        url: "/testManage/testCase/getArchivedCaseIds",
        method: "post",
        async: false,
        data: {},
        success: function (data) {
            aids = data.data;
        },
    });
}

//删除操作
function deleteTestCase(rows) {
    var ids = [];
    ids.push(rows.id);
    findArchivedId();
    for (var i = 0; i < aids.length; i++) {
        if (aids[i] == rows.id) {
            layer.alert('此案例已归档，请先从归档库中移除!', {
                icon: 0,
            })
            return;
        }
    }
    layer.alert('确认要删除此案例？', {
        icon: 0,
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: "/testManage/testCase/deleteCaseInfo",
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
                layer.alert('删除成功!', {
                    icon: 1,
                })
            },
        });
    })
}

//删除案例（批量删除）
function deleteTestCase_btn() {
    var ids = [];
    findArchivedId();
    var ids = $('#caseList').jqGrid('getGridParam', 'selarrrow')
    if (ids.length == 0) {
        layer.alert('请至少选择一个案例删除!', {
            icon: 0,
        })
        return;
    }
    for (var i = 0; i < aids.length; i++) {
        for (var j = 0; j < ids.length; j++) {
            if (aids[i] == ids[j]) {
                layer.alert('所选中案例已有归档，请先从归档库中移除!', {
                    icon: 0,
                })
                return;
            }
        }
    }
    layer.alert('确认要删除所选中的案例？', {
        icon: 0,
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: "/testManage/testCase/deleteCaseInfo",
            method: "post",
            data: {
                "ids": ids
            },
            success: function (data) {
                var zTree = $.fn.zTree.getZTreeObj("menuTree");
                node = zTree.getSelectedNodes();
                node[0].caseCount=node[0].caseCount-ids.length;
                zTree.updateNode( node );
                $(".ztree li a.curSelectedNode>.tree_case_num").text("案例数:"+node[0].caseCount)
                initTable();
                layer.alert('删除成功!', {
                    icon: 1,
                })
            },
        });
    })
}

//导出案例
function exportTestCase_btn() {
    //		window.location.href = "/testManage/testCase/exportExcel?excelData="+excelData+"&filters="+ff;
    window.location.href = "/testManage/testCase/exportExcel?caseNumber=" + caseNumber2 + "&caseName=" + caseName2 + "&caseType=" + caseType2 +
        "&systemIds=" + systemIds2 + "&archiveStatus=" + archiveStatus2 + "&uIds=" + uIds2 + "&filters=" + ff + "&caseCatalogId=" + mydata.caseTreeNode.realId;
}


//显示导入案例弹窗
function importTestCase_btn() {
    $("#upfile").val("");
    $("#importPerson").modal("show");
}

//导出模板
function exportTemplate() {
    window.location.href = "/testManage/testCase/exportTemplet";
}

//导入
function upload() {
    var formData = new FormData();
    formData.append("file", document.getElementById("upfile").files[0]);
    formData.append("caseCatalogId", mydata.caseTreeNode.realId );
    if (document.getElementById("upfile").files[0] == undefined) {
        layer.alert("请选择文件", {icon: 0});
        return;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/zuul/testManage/testCase/importExcel",
        type: "POST",
        data: formData,
        /**
         *必须false才会自动加上正确的Content-Type
         */
        contentType: false,
        /**
         * 必须false才会避开jQuery对 formdata 的默认处理
         * XMLHttpRequest会对 formdata 进行正确的处理
         */
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
function newTestCase_btn() {
    newTestCase_reset();
    $("#newTestCaseForm").data('bootstrapValidator').destroy();
    $('#newTestCaseForm').data('bootstrapValidator', null);
    formValidator();
    createAddTable();
    addField()   
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
    	    
    	    $("#newTestCaseModal").modal("show");
        },
    }); 
}

function addField() {
    $.ajax({
        url: "/testManage/fieldTemplate/findFieldByTestCase",
        method: "post",
        success: function (data) {
            if (!data.field) return;
            for (var i = 0; i < data.field.length; i++) {
                appendDataType(data.field[i], 'canEditField', 'new');
            }
        }
    });
}

function appendDataType(thisData, id, status) {
    var obj = $('<div class="def_col_18"></div>');
    switch (thisData.type) {
        case "int":
            obj.attr("dataType", "int");
            var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + ':</div>');
            if (status == "new") {
                var labelContent = $('<div class="def_col_28"><input maxlength="' + thisData.maxLength + '" fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" name="int" value="' + thisData.defaultValue + '" /></div>');
            } else {
                var labelContent = $('<div class="def_col_28"><input maxlength="' + thisData.maxLength + '" fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" name="int" value="' + thisData.valueName + '" /></div>');
            }
            labelContent.children(" input ").bind("keyup", function () {
                this.value = this.value.replace(/\D/gi, "");
            })
            obj.append(labelName, labelContent);
            break;
        case "float":
            obj.attr("dataType", "float")
            var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + ':</div>');
            if (status == "new") {
                var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="number" class="form-control" placeholder="请输入" name="float" value="' + thisData.defaultValue + '" /></div>');
            } else {
                var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="number" class="form-control" placeholder="请输入" name="float" value="' + thisData.valueName + '" /></div>');
            }
            obj.append(labelName, labelContent);
            break;
        case "varchar":
            obj.attr("dataType", "varchar")
            var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + ':</div>');
            if (status == "new") {
                var labelContent = $('<div class="def_col_28"><input  maxlength="' + thisData.maxLength + '"  fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" name="varchar" value="' + thisData.defaultValue + '" /></div>');
            } else {
                var labelContent = $('<div class="def_col_28"><input  maxlength="' + thisData.maxLength + '"  fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" name="varchar" value="' + thisData.valueName + '" /></div>');
            }
            obj.append(labelName, labelContent);
            break;
        case "data":
            obj.attr("dataType", "data")
            var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + ':</div>');
            if (status == "new") {
                var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="new' + thisData.fieldName + '" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="' + thisData.defaultValue + '" /></div>');
            } else {
                var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="edit' + thisData.fieldName + '" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="' + thisData.valueName + '" /></div>');
            }
            obj.append(labelName, labelContent);
            break;
        case "timestamp":
            obj.attr("dataType", "timestamp")
            var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + ':</div>');
            if (status == "new") {
                var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="new' + thisData.fieldName + '" type="text" readonly id="new_' + thisData.fieldName + '" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="' + thisData.defaultValue + '" /></div>');
            } else {
                var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="edit' + thisData.fieldName + '" type="text" readonly id="new_' + thisData.fieldName + '" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="' + thisData.valueName + '" /></div>');
            }
            obj.append(labelName, labelContent);
            break;
        case "enum":
            obj.attr("dataType", "enum")
            var select = $('<select class="selectpicker" requireded="' + thisData.required + '" fName="' + thisData.fieldName + '"></select>')
            var options = JSON.parse(thisData.enums);
            select.append('<option value=""  fName="' + thisData.fieldName + '">请选择</option>');
            for (var i = 0; i < options.length; i++) {
                if (options[i].enumStatus == 1) {
                    if (status == "edit" && options[i].value == thisData.valueName) {
                        select.append('<option value="' + options[i].value + '" selected >' + options[i].value + '</option>');
                    } else {
                        select.append('<option value="' + options[i].value + '">' + options[i].value + '</option>');
                    }

                }
            }
            var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + ':</div>');
            var labelContent = $('<div class="def_col_28"></div>');
            labelContent.append(select);
            obj.append(labelName, labelContent);
            break;
        default:
            break;
    }
    $("#" + id).append(obj);
    if (obj.attr("dataType") == "data") {
        laydate.render({
            elem: "#" + obj.find("input")[0].id,
            trigger: 'click',
            done: function (value, date, endDate) {
                $("#" + obj.find("input")[0].id).next().css("display", "block");
            }
        });
    } else if (obj.attr("dataType") == "timestamp") {
        laydate.render({
            elem: "#" + obj.find("input")[0].id,
            trigger: 'click',
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            done: function (value, date, endDate) {
                $("#" + obj.find("input")[0].id).next().css("display", "block");
            }
        });
    }
    $(".selectpicker").selectpicker('refresh');
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
                    return '<div class="def_tableDiv2"><textarea style="resize:none;"  class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div>';
                }
            }, {
                field: "stepExpectedResult",
                title: "预期结果",
                align: 'center',
                formatter: function (value, row, index) {
                    return '<div class="def_tableDiv2"><textarea style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div>';
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
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height: 84px;">' + i + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height: 84px;"><a class="a_style" style="cursor:pointer" onclick="addTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upAddTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downAddTableRow(this)">下移</a></td> </tr>';
    $("#addCaseSteps>tbody").append(str);
    changeStepOrder();
}

function addTableRow(This) {
    /*
    var _data={};
    $("#editCaseSteps").bootstrapTable('insertRow', { index:( $("#editCaseSteps>tbody>tr	").index( $( This ).parent().parent() ) +  1 ) ,row:_data });*/

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
    var i = $("#addCaseSteps>tbody>tr").length + 1;
    var str = '<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">' + i
        + '</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea  style="resize:none;"></div></td> <td style="text-align: center; width: 210px;line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="addTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upAddTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downAddTableRow(this)">下移</a></td> </tr>';
    $("#addCaseSteps").append(str);
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
    var fieldTemplate = getFieldData("canEditField");
    for (var i = 0; i < fieldTemplate.field.length; i++) {
        if (fieldTemplate.field[i].required == "false") {
            if (fieldTemplate.field[i].valueName == "" || fieldTemplate.field[i].valueName == null || fieldTemplate.field[i].valueName == undefined) {
                $("#loading").css('display', 'none');
                layer.alert(fieldTemplate.field[i].labelName + "不能为空", {
                    icon: 2,
                    title: "提示信息"
                });
                return;
            }
        }
    }
    $.ajax({
        type: "post",
        url: "/testManage/testCase/insertCaseInfo",
        dataType: "json", 
        data: {
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
                'fieldTemplate': JSON.stringify(fieldTemplate)
            }),
        	caseSteps : JSON.stringify(CaseStep)
        },
        success: function (data) {
        	var zTree = $.fn.zTree.getZTreeObj("menuTree");
        	node = zTree.getSelectedNodes();  
        	node[0].caseCount++;
        	zTree.updateNode( node );
            $(".ztree li a.curSelectedNode>.tree_case_num").text("案例数:"+node[0].caseCount)
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

function getFieldData(id) {
    var data = {"field": []};
    for (var i = 0; i < $("#" + id + " > div").length; i++) {
        //int float varchar data timestamp enum
        var obj = {};
        var type = $("#" + id + " > div").eq(i).attr("dataType")
        if (type == "int" || type == "float" || type == "varchar" || type == "data" || type == "timestamp") {
            obj.fieldName = $("#" + id + " > div").eq(i).find("input").attr("fName");
            obj.required = $("#" + id + " > div").eq(i).find("input").attr("requireded");
            obj.valueName = $("#" + id + " > div").eq(i).find("input").val();
            obj.labelName = $("#" + id + " > div").eq(i).children("div").eq(0).text();
        } else if (type == "enum") {
            obj.fieldName = $("#" + id + " > div").eq(i).find("select").attr("fName");
            obj.required = $("#" + id + " > div").eq(i).find("select").attr("requireded");
            obj.valueName = $("#" + id + " > div").eq(i).find("select").val();
            obj.labelName = $("#" + id + " > div").eq(i).children("div").eq(0).text();
        }
        data.field.push(obj);
    }
    return data;
}

/*---------------新建案例操作end----------------------*/


// 新建：清空
function newTestCase_reset() {
    $("#canEditField").empty();
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

function addCheckBox() {
    $("#colGroup").empty();
    var str = "";
    str = '<div class="onesCol"><input type="checkbox" value="caseNumber" onclick="showHideCol( this )" /><span>案例编号</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="caseName" onclick="showHideCol( this )" /><span>案例名称</span></div>' +
        //不需要案例类型 '<div class="onesCol"><input type="checkbox" value="caseType" onclick="showHideCol( this )" /><span>案例类型</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="caseDescription" onclick="showHideCol( this )" /><span>案例描述</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="systemName" onclick="showHideCol( this )" /><span>所属系统</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="archiveStatus" onclick="showHideCol( this )" /><span>归档状态</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="userName" onclick="showHideCol( this )" /><span>创建人</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="businessType" onclick="showHideCol( this )" /><span>业务类型</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="moduleName" onclick="showHideCol( this )" /><span>模块</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="testPoint" onclick="showHideCol( this )" /><span>测试项</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="opt" onclick="showHideCol( this )" /><span>操作</span></div>';
    $("#colGroup").append(str)
}

function showHideCol(This) {
    var colModel = $("#caseList").jqGrid('getGridParam', 'colModel');
    var width = 0;//获取当前列的列宽
    var arr = [];
    for (var i = 0; i < colModel.length; i++) {
        if (colModel[i]["hidden"] == false) {
            arr.push(colModel[i]["hidden"]);
        }
    }
    if ($(This).is(':checked')) {
        $("#caseList").setGridParam().hideCol($(This).attr('value'));
        $("#caseList").setGridWidth($('.wode').width());
        if (arr.length == 1) {
            $("#caseList").jqGrid('setGridState', 'hidden');
        }
    } else {
        $("#caseList").jqGrid('setGridState', 'visible');
        $("#caseList").setGridParam().showCol($(This).attr('value'));
        $("#caseList").setGridWidth($('.wode').width());
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
            /* new_caseType: {
                validators: {
                    notEmpty: {
                        message: '案例类型不能为空'
                    }
                }
            } */
        }
    });
    $('#editTestCaseForm').bootstrapValidator({
        message: '输入有误',//通用的验证失败消息
        feedbackIcons: {//根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            edit_caseName: {
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
            edit_system: {
                validators: {
                    notEmpty: {
                        message: '涉及系统不能为空'
                    }
                }
            },
            /* edit_caseType: {
                validators: {
                    notEmpty: {
                        message: '案例类型不能为空'
                    }
                }
            } */
        }
    });
}

function addClickRow(idArr) {
    for (var i = 0; i < idArr.length; i++) {
        $("#" + idArr[i]).on("click-row.bs.table", function (e, row, $element) {
            $element.children("td").find("input[type=checkbox]").click();
        })
    }

}

function showField(data) {
    $("#checkEditField").empty();
    if (data != null && data != "undefined" && data.length > 0) {
        for (var i = 0; i < data.length; i++) {
            var aLabel = '<div class="def_col_4 font_right fontWeihgt">' + data[i].label + ':</div>';
            var aLabelContent = '<div class="def_col_14">' + data[i].valueName + '</div>';
            $("#checkEditField").append(aLabel, aLabelContent);
        }
    }
}
 