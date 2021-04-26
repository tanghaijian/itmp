
//定义setTimeout执行方法
var TimeFn = null;
var errorDefect = '系统内部错误，请联系管理员 ！！！';
var select_rows = new Array();
var tb_select_rows = new Array();

$(document).ready(function () {
    $("#loading").css('display', 'block');
    leftMenu();
    formValidator();
    refactorFormValidator();
    checkSelectRows("#tb2");
    checkSelectRows("#userTable");
    //搜索展开和收起
    $("#downBtn").click(function () {
        if( $(this).children("span").hasClass( "fa-caret-up" ) ){
            $(this).children("span").removeClass("fa-caret-up");
            $(this).children("span").addClass("fa-sort-desc");
            $("#search_div").slideUp(200);
        }else {
            $(this).children("span").removeClass("fa-sort-desc");
            $(this).children("span").addClass("fa-caret-up");
            $("#search_div").slideDown(200);
        }
    });
    // 所有的Input标签，在输入值后出现清空的按钮
    $('input[type="text"]').parent().css("position","relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    })
});


function initMenu() {
    $(".menu-ul li").find("input[type='text']").remove();
    $(".menu-ul li").find("a").show();
}

//左边目录角色信息列表
function leftMenu(current) {
    $(".left-box").empty();
    if (current == undefined) {
        current = 0;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/assetLibrary/directoryAuthority/getAllSystemDirectoryRole",
        method: "post",
        dataType: "json",
        data: {
            projectId: $('#project_Id').val(),
        },
        success: function (data) {
            //initTable();
            
            var menuDate = data.systemDirectoryRoles;

            var _ul = $('<ul class="menu-ul"></ul>');
            for (var i = 0; i < menuDate.length; i++) {
                var obj = menuDate[i];
                var _li = $('<li></li>');
                var _span = $('<span class="parent-id">' + obj.id + '</span>');
                var _a = $('<a href="javascript:void(0);" >' + obj.roleName + '</a>');

                _li.appendTo(_ul);
                _span.appendTo(_li);
                _a.appendTo(_li);
                if (i == current) {
                    _li.addClass("current");
                }
            }
            var _add_div = $('<div class="add-menu"><span class="glyphicon glyphicon-plus fa fa-plus"></span></div>');
            _ul.appendTo($(".left-box"));
            _add_div.appendTo($(".left-box"));
            initTable();

            // 单击切换按钮
            $(".menu-ul li").click(function () {
                select_rows = new Array();
                $(".menu-ul li").removeClass("current");
                $(this).addClass("current");
                // 取消上次延时未执行的方法
                clearTimeout(TimeFn);
                //执行延时
                TimeFn = setTimeout(function () {
                    //do function在此处写单击事件要执行的代码
                    $("#loading").css('display', 'block');
                    menuButton();
                    roleUserList();
                    initTable();
                    showBut(_id);
                }, 300);

            });

            // 双击修改角色名称按钮
            $(".menu-ul li").dblclick(function () {
                clearTimeout(TimeFn);
                $("#loading").css('display', 'none');
                $(this).unbind();
                initMenu();
                $(this).children("a").hide();
	//                var _name = $(this).children("a")
	//                    .text();
	//                var _input = $('<input type="text" value="'
	//                    + _name
	//                    + '" class="menu-input" onkeydown="editMenuName(event,this)" />');
	//                _input.appendTo($(this));

                $(this).children(".menu-input").blur(function () {
                    var _name = $(this).val();
                    var _id = $(this).prev().prev().text();
                    var current = $(this).parent().index();
                    $("#loading").css('display', 'block');
                    $.ajax({
                        url: "/projectManage/assetLibrary/directoryAuthority/addOrUpdateSystemDirectoryRole",
                        method: "post",
                        data: {
                            "id": _id,
                            "projectId": $('#project_Id').val(),
                            "roleName": _name
                        },
                        success: function (data) {
                            $(this).bind();
                            $("#loading").css('display', 'none');
                            if (data.status == 2) {
                                layer.alert(data.errorMessage, {
                                    icon: 2,
                                    title: "提示信息"
                                });

                            } else {
                                layer.alert('修改成功！', {
                                    icon: 1,
                                    title: "提示信息"
                                });
                                leftMenu(current);
                            }
                            showBut(_id);
                        },
                        error: function () {
                            $("#loading").css('display', 'none');
                            layer.alert(errorDefect, {
                                icon: 2,
                                title: "提示信息"
                            });
                        }
                    });
                });
            });

            // 添加按钮
            $(".add-menu").click(function () {
                reset();
                $("#roleModal").modal("show");
                formValidator();
            });

            menuButton();
            // 关联人员
            roleUserList();
            var _id = $(".menu-ul").find(".current").children("span").text();
            showBut(_id);
            $("#loading").css('display', 'none');
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

function showBut(id) {
    if (id == "1") {
        $("#updateRoleBut").css("display", "none");
    } else {
        $("#updateRoleBut").css("display", "inline-block");
    }
}

function clearSearch(){
    reset();
    userList();
}

//中间部门，该项目下的目录树形列表（后端查询好像有个bug，此处为新建类项目，后台查的却是运维类项目）
function initTable() {
	$("#loading").css('display', 'block');
	var _id = $(".menu-ul").find(".current").children("span").text();
	console.log(_id);
	$.ajax({
		url:"/projectManage/assetLibrary/directoryAuthority/getSystemDirectoryAuth",
		type:"post",
		dataType:"json",
		data:{
			projectId:$('#project_Id').val() ,//项目id
			roleId:_id
		},
		success:function(data){
			tree_table.tree_create_table(data.auth,{
			    id:'id',
			    parentId:'parentId',//父id
			    parentIds:'parentIds',//父id集合  例   一级"parentIds": ""  二级"parentIds": "299,"  三级"parentIds": "20,299,"
			    level:'level',//级别
			    isLeaf:'isLeaf',//是否有子节点
			    isSelect:'isSelect',//是否选中
			    leaf:'leaf',//是否为子节点
			    name:'dirName',//名称
			    padding_left:20,//左边距倍率,默认20
			    readAuth:'readAuth',//读权限
			    writeAuth:'writeAuth',//写权限
			});
			$("#loading").css('display', 'none');
		},
		error:function (data){
			$("#loading").css('display', 'none');
		}
	})
	
}


//已关联人员 单个角色关联人员列表
function roleUserList() {
    var _id = $(".menu-ul").find(".current").children("span").text();
    console.log(_id);
    $("#loading").css('display', 'block');
    $("#tb2").bootstrapTable('destroy');
    $("#tb2").bootstrapTable({
        url: "/projectManage/assetLibrary/directoryAuthority/getSystemDirectoryRoleUserByRoleId",
        method: "post",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType: "",
        pagination: true,
        sidePagination: "server",
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 25, 50, 100],
        clickToSelect: true, //是否启用点击选中行
        maintainSelected: true,
        queryParams: function (params) {
            var param = {
                pageNumber: params.pageNumber,
                pageSize: params.pageSize,
                roleId: _id,
            }
            return param;
        },
        columns: [{
            checkbox: true,
            width: "30px",
            formatter: function (value, row, index) {
                if ($.inArray(row.id, tb_select_rows) != -1) {
                    return {
                        checked: true
                    }
                }
                return {
                    checked: false
                }
            }
        }, {
            field: "id",
            title: "id",
            align: 'center',
            visible: false
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
            title: "所属组织",
            align: 'center'
        }],
        onLoadSuccess: function () {
            $("#loading").css('display', 'none');
        },
        onLoadError: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

//未关联人员列表
function userList() {
    var no_id = $("#tb2").bootstrapTable('getData');
    let ids = '';
	no_id.map((val) => {
		ids += val.id + ',';
	})
    $("#loading").css('display', 'block');
    $("#userTable").bootstrapTable('destroy');
    $("#userTable").bootstrapTable({
        url: "/system/user/getAllUserModal3",
        method: "post",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType: "",
        pagination: true,
        sidePagination: "server",
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 25, 50, 100],
        clickToSelect: true, //是否启用点击选中行
        maintainSelected: true,
        queryParams: function (params) {
            var param = {
                ids:ids,//已选的id
                userName: $.trim($("#userName").val()),
                companyName: $("#companyName").val(),
                deptName: $("#deptName").val(),
                pageNumber: params.pageNumber,
                pageSize: params.pageSize,
                projectIds: '',
            };
            return param;
        },
        columns: [{
            checkbox: true,
            width: "30px",
            formatter: function (value, row, index) {
                if ($.inArray(row.id.toString(), select_rows) != -1) {
                    return {
                        checked: true
                    }
                }
                return {
                    checked: false
                }
            }
        }, {
            field: "id",
            title: "id",
            align: 'center',
            visible: false
        }, {
            field: "userName",
            title: "姓名",
            align: 'center'
        }, {
            field: "userAccount",
            title: "用户名",
            align: 'center'
        }, {
            field: "employeeNumber",
            title: "工号",
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
        },
        onLoadError: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        },
        onPageChange: function () {
            $("#loading").css('display', 'block');
        }
    });
    $("#userModal").modal("show");
}

//提交目录权限
function updateRoleBtn() {
	var update_arr = [];
	$('input.readAuth').each(function (idx, val) {
		if ($(val).is(":checked")) {
		    update_arr.push({
			      systemDirectoryRoleId:$(".menu-ul").find(".current").children("span").text(),//左侧选中的角色id
				  systemDirectoryId: $(val).data('id'), //文档目录id
			      'readAuth': $(val).is(":checked") ? 1 : 2,
			      'writeAuth': $(val).parent().find('.writeAuth').is(":checked") ? 1 : 2,
		    });
		}
		if ($(val).parent().find('.writeAuth').is(":checked")) {
		    update_arr.push({
			      systemDirectoryRoleId:$(".menu-ul").find(".current").children("span").text(),//左侧选中的角色id
				  systemDirectoryId: $(val).data('id'), //文档目录id
			      'readAuth': $(val).is(":checked") ? 1 : 2,
			      'writeAuth': $(val).parent().find('.writeAuth').is(":checked") ? 1 : 2,
		    });
		}
	})
	var arr = update_arr;
	for (var i = 0; i < arr.length; i++) {
		arr[i].open = true;
		for (var j = i+1; j < arr.length; j++) {
			if (arr[i].systemDirectoryId == arr[j].systemDirectoryId) {
				arr.splice(j,1);
				j--;
			}
		}
	}
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/assetLibrary/directoryAuthority/saveSystemDirectoryAuth",
        type: "post",
        dataType: "json",
        contentType:"application/json",
        data: JSON.stringify(arr),
        success: function (data) {
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else {
                select_rows = new Array();
                $("#tb2").bootstrapTable('refresh');
                menuButton();
                layer.alert('修改成功！', {
                    icon: 1,
                    title: "提示信息"
                });
            }
            $("#loading").css('display', 'none');
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

// 取消按钮
function resetRoleBtn() {
    select_rows = new Array();
    $("#tb2").bootstrapTable('refresh');
    menuButton();
}

// 条件查询未关联人员
function findUserWithNoRole() {
    userList();
}

// 添加角色
function insertRole() {
    $("#roleForm").data('bootstrapValidator').validate();
    if (!$("#roleForm").data('bootstrapValidator').isValid()) {
        return false;
    }

    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/assetLibrary/directoryAuthority/addOrUpdateSystemDirectoryRole",
        method: "post",
        dataType: "json",
        data: {
            id: '',//新增不需要 编辑需要
            projectId: $('#project_Id').val(),		//项目id
            roleName: $.trim($("#roleName").val()),
        },
        success: function (data) {
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                leftMenu();
                $("#roleModal").modal("hide");
            } else {
                layer.alert("该角色已存在,请勿重复添加！", {
                    icon: 2,
                    title: "提示信息"
                });
            }
            $("#loading").css('display', 'none');
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

// 单个角色获取菜单按钮
function menuButton() {
    //    $("#loading").css('display', 'block');
    //    $("#tb1").jqGrid("clearGridData");
    //    $("#tb1").jqGrid('setGridParam', {
    //        url: "/projectManage/assetLibrary/directoryAuthority/getSystemDirectoryAuth",
    //        postData: {
    //        	projectId: $(".menu-ul").find(".current").children("span").text()
    //        },
    //        page: 1
    //    }).trigger("reloadGrid");
}


// 单个角色添加未关联人员
function insertRoleUser() {
    var roleId = $(".menu-ul").find(".current").children("span").text();
    if (typeof (select_rows) == 'undefined' || select_rows.length <= 0) {
        layer.alert("请选中一行", {
            icon: 2,
            title: "提示信息"
        });
    } else {
        var userId = '';
        for (var i = 0; i < select_rows.length; i++) {
            //            userId.push(select_rows[i]);
            userId += select_rows[i] + ',';
        }
        $.ajax({
            url: "/projectManage/assetLibrary/directoryAuthority/addSystemDirectoryRoleUser",
            method: "post",
            data: {
                uids: userId,
                roleId: roleId
            },
            success: function (data) {
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    select_rows = new Array();
                    //                    findUserWithNoRole();
                    roleUserList();
                    $('#userModal').modal('hide');
                    layer.alert('添加成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                }
                $("#loading").css('display', 'none');
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
}

// 弹出关联人员弹框
function userModel() {
    reset();
    findUserWithNoRole();

}


function reset(){
    select_rows = new Array();
    $("#userName").val('');
    $("#employeeNumber").val('');
    $("#deptName").val('');
    $("#companyName").val('');
    $("#roleName").val("");
    $(".btn_clear").css("display","none");
}

// 取消关联人员
function disassociate() {

    var roleId = $(".menu-ul").find(".current").children("span").text();
    if (typeof (tb_select_rows) == 'undefined' || tb_select_rows.length <= 0) {
        layer.alert("请选中一行", {
            icon: 2,
            title: "提示信息"
        });
    } else {
        var userId = '';
        for (var i = 0; i < tb_select_rows.length; i++) {
            //            userId.push(tb_select_rows[i]);
            userId += tb_select_rows[i] + ',';
        }
        $("#loading").css('display', 'block');
        $.ajax({
            url: "/projectManage/assetLibrary/directoryAuthority/cancelSystemDirectoryRoleUser",
            method: "post",
            data: {
                uids: userId,
                roleId: roleId
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                    // window.location.href = "/systemui/error/500";
                } else {
                    layer.alert('取消关联成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    tb_select_rows = new Array();
                    roleUserList();
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
}

// 置为无效
function updateRole() {
    var roleId = $(".menu-ul").find(".current").children("span").text();
    var roleName = $(".menu-ul").find(".current").children("a").text();
    var _roleName = '<span style="color: red">' + roleName + '</span>';
    layer.confirm('您确定要置  ' + _roleName + '  角色为无效吗？', {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        $("#loading").css('display', 'block');
        $.ajax({
            url: "/projectManage/assetLibrary/directoryAuthority/addOrUpdateSystemDirectoryRole",
            method: "post",
            dataType: "json",
            data: {
                id: roleId,
                status: 2
            },
            success: function (data) {
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    select_rows = new Array();
                    leftMenu();
                    layer.alert('该角色置为无效成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                }
                $("#loading").css('display', 'none');
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        });
    })
}

// 清空表格内容
function clearContent(that) {
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display", "none");
}


function checkSelectRows(id) {
    $(id).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
        var singleSelect = $(id).bootstrapTable('getOptions').singleSelect;
        var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
        if(singleSelect == true ){// 单选
            if (id == "#tb2") {
                tb_select_rows = [];
                tb_select_rows.push(datas[0]);
            } else {
                select_rows = [];
                select_rows.push(datas[0]);
            }
        } else { // 多选
            examine(e.type,datas);                              // 保存到全局 Array() 里
        }
    });
}

function examine(type, datas, id) {
    if (type.indexOf('uncheck') == -1) {
        $.each(datas, function (i, v) {
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            select_rows.indexOf(v.id.toString()) == -1 ? select_rows.push(v.id.toString()) : -1;
            if (id == "#tb2") {
                tb_select_rows.indexOf(v.id.toString()) == -1 ? tb_select_rows.push(v.id.toString()) : -1;
            }
        });
    } else {
        $.each(datas, function (i, v) {
            select_rows.splice(select_rows.indexOf(v.id.toString()), 1);    //删除取消选中行
            if (id == "#tb2") {
                tb_select_rows.splice(tb_select_rows.indexOf(v.id.toString()), 1);    //删除取消选中行
            }
        });
    }
}

// 表单验证
function formValidator() {
    $('#roleForm').bootstrapValidator({
        excluded: [':disabled'],
        message: 'This value is not valid',//通用的验证失败消息
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live: 'enabled',
        fields: {
            roleName: {
                validators: {
                    notEmpty: {
                        message: '请输入角色名称！'
                    }
                }
            }
        }
    })
}

// 重构表单验证
function refactorFormValidator() {
    $('#roleModal').on('hidden.bs.modal', function () {
        $("#roleForm").data('bootstrapValidator').destroy();
        $('#roleForm').data('bootstrapValidator', null);
        formValidator();
    })
}


