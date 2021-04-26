
//定义setTimeout执行方法
var TimeFn = null;
var errorDefect = '系统内部错误，请联系管理员 ！！！';
var select_rows = new Array();
var tb_select_rows = new Array();

$(document).ready(function() {
    $("#loading").css('display','block');
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

//获取左侧角色信息
function leftMenu( current ) {
    $(".left-box").empty();

	if( current==undefined ){

		current=0;
	}
    $("#loading").css('display','block');
	// 获取所有角色
	$.ajax({
        url:"/system/role/getAllRole",
        method:"post",
        dataType: "json",
		success : function(data) {
            initTable();
            $("#loading").css('display','none');
            var menuDate = JSON.parse(data.data);

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

            // 单击切换按钮
            $(".menu-ul li").click(function() {
                select_rows = new Array();
                $(".menu-ul li").removeClass("current");
                $(this).addClass("current");
                // 取消上次延时未执行的方法
                clearTimeout(TimeFn);
                //执行延时
                TimeFn = setTimeout(function(){
                    //do function在此处写单击事件要执行的代码
                    $("#loading").css('display','block');
                    menuButton();
                    roleUserList();
                    var _id = $(".menu-ul").find(".current").children("span").text();
                    showBut(_id);
                },300);

            });

            // 双击修改角色名称按钮
            $(".menu-ul li").dblclick(function() {
                clearTimeout(TimeFn);
                $("#loading").css('display','none');
                $(this).unbind();
                initMenu();
                $(this).children("a").hide();
                var _name = $(this).children("a")
                    .text();
                var _input = $('<input type="text" value="'
                    + _name
                    + '" class="menu-input" onkeydown="editMenuName(event,this)" />');
                _input.appendTo($(this));

                $(this).children(".menu-input").blur(function(){
                    var _name = $(this).val();
                    var _id = $(this).prev().prev().text();
                    var current=$(this).parent().index();
                    $("#loading").css('display','block');
                    $.ajax({
                        url : "/system/role/updateRoleName",
                        method : "post",
                        data : {
                            "id" : _id,
                            "roleName" : _name
                        },
                        success : function(data) {
                            $(this).bind();
                            $("#loading").css('display','none');
                            if (data.status == 2){
                                layer.alert(data.errorMessage, {
                                    icon: 2,
                                    title: "提示信息"
                                });

                            } else {
                                layer.alert('修改成功！', {
                                    icon: 1,
                                    title: "提示信息"
                                });
                                leftMenu( current );
                            }
                            showBut(_id);
                        },
                        error:function(){
                            $("#loading").css('display','none');
                            layer.alert(errorDefect, {
                                icon: 2,
                                title: "提示信息"
                            });
                        }
                    });
                });
            });

            // 添加按钮
            $(".add-menu").click(function() {
                reset();
                $("#roleModal").modal("show");
                formValidator();
            });
            //展示中间菜单信息
            menuButton();
            //展示右边关联人员
            roleUserList();
            var _id = $(".menu-ul").find(".current").children("span").text();
            showBut(_id);
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
	});

}

//显示修改角色名输入框
function showBut(id){
    if(id == "1"){
        $("#updateRoleBut").css("display","none");
    } else {
        $("#updateRoleBut").css("display","inline-block");
    }
}

//双击角色名，进行修改
function editMenuName(event, that) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    if (e.keyCode == 13) {
        var _name = $(that).val();
        var _id = $(that).prev().prev().text();
        var current=$(that).parent().index();
        $("#loading").css('display','block');
        $.ajax({
            url : "/system/role/updateRoleName",
            method : "post",
            data : {
                "id" : _id,
                "roleName" : _name
            },
            success : function(data) {
                $(that).bind();
                $("#loading").css('display','none');
                if (data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });

                } else {
                    layer.alert('修改成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    leftMenu( current );
                }
                showBut(_id);
            },
            error:function(){
                $("#loading").css('display','none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        });
    }
}

/**
 * 菜单按钮 jqGrid
 * @author
 */
function initTable() {
    $("#tb1").jqGrid("clearGridData");
    $("#tb1").jqGrid({
        url:"/system/role/getRoleMenu",
        datatype: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        height: 'auto',
        mtype : "POST",
        cellEdit:true,
        width: $("#tbDiv").width()*0.999,
        colNames:['id','菜单','按钮'],
        colModel:[
            {name : 'id',index : 'id',hidden:true,key:true},
            {name : 'menu',index : 'menu',formatter : function(value, grid, rows, state){
            	var flag='';
            	if( rows.isSelect==true ){
            		flag='checked';
            	}
            	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
            	return '<input type="checkbox" '+flag+' value='+rows.id+'  onclick="RelativeTreeGridCheck('+row+',this)" />&nbsp;'+rows.menu;
            }},
            {name : 'buttonList',index : 'buttonList', formatter : function(value, grid, rows, state){
                var row = JSON.stringify(rows).replace(/"/g, '&quot;');
            	var checkBoxGroup='';
            	if(  rows.buttonList!=null ){
            		 for( var i=0;i<rows.buttonList.length;i++ ){
            			 var flag='';
            			 if( rows.buttonList[i].isSelect==true ){
                     		flag='checked';
                     	 }
                     	 var menuButtonName = rows.buttonList[i].menuButtonName;
            			 checkBoxGroup+='<input type="checkbox" '+flag+'  value='+rows.buttonList[i].id+'  onclick="RelativeTreeGridCheck('+row+',this)"/>&nbsp;'+menuButtonName+'&nbsp;&nbsp;';
                         var ii = i+1;
                         ii>0?ii%3==0?checkBoxGroup +='<br/>':'':'';
            		 }

            	}
            	return checkBoxGroup;
            }}
        ],
        treeGrid: true,
        treeGridModel: "adjacency",
        ExpandColumn: "menu",
        treeIcons: {plus:'fa fa-caret-right',minus:'fa fa-caret-down',leaf:''},
        sortname:"id",
        sortable:true,
        ExpandColClick: true,
        multiselect: true,
        jsonReader: {
            repeatitems: false,
            root: "result",
        },
        treeReader : {           //设置树形显示时4个关键字段对应的返回数据字段
            level_field: "level",      // 属性层级
            parent_id_field: "parent", //父级rowid
            leaf_field: "isLeaf",      //是否还有子级菜单
            expanded_field: "expanded" //是否加载完毕
        },
        loadComplete :function(xhr){
        	$("#loading").css('display','none');
        },
        loadError:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    }).trigger('reloadGrid');
}

//选择父菜单，联动选中子菜单
function RelativeTreeGridCheck(row,That,event){
    var rowData = $('#tb1').jqGrid('getRowData', row.id,true);
    var childrenData = $('#tb1').jqGrid('getNodeChildren', rowData);
    var parentData = $('#tb1').jqGrid('getNodeParent', rowData);
    var rowChecked = $("#"+row.id).children().children().find("input").is(":checked");
    //  不为0，则为子节点，肯定有父节点，把父节点勾上
    if (childrenData.length != 0) {
        for (var i=0;i<childrenData.length;i++){
            if ( rowChecked == true ){
                $("#"+childrenData[i].id).children().children().find("input").prop("checked",true);
            } else {
               $("#"+childrenData[i].id).children().children().find("input").removeProp("checked");
               $("#"+childrenData[i].id).children().children().removeProp("checked");
            }
        }
    } else {
        // 为0 则为父节点， 如果有子勾选，没有则不操作
        row.level != 0?$("#"+parentData.id).children().children().find("input").prop("checked",true):'';
    }
    var e = arguments.callee.caller.arguments[0]||event;
    if (e && e.stopPropagation) {
        e.stopPropagation();
    } else if (window.event) {
        window.event.cancelBubble = true;
    }
}

/**
 * 已关联人员 单个角色关联人员列表
 */
function roleUserList(){
    var _id = $(".menu-ul").find(".current").children("span").text();
    /*$("#loading").css('display','block');*/
    $("#tb2").bootstrapTable('destroy');
    $("#tb2").bootstrapTable({
        url:"/system/role/getRoleUser",
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        clickToSelect : true, //是否启用点击选中行
        maintainSelected : true,
        queryParams : function(params) {
            var param = {
                pageNumber:params.pageNumber,
                pageSize:params.pageSize,
                id:_id
            }
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
            formatter:function(value,row,index){
                if($.inArray(row.id,tb_select_rows) != -1){
                    return {
                        checked:true
                    }
                }
                return {
                    checked:false
                }
            }
        },{
            field : "id",
            title : "id",
            align : 'center',
            visible : false
        },{
            field : "userName",
            title : "姓名",
            align : 'center'
        },{
            field : "userAccount",
            title : "用户名",
            align : 'center'
        },{
            field : "deptName",
            title : "所属组织",
            align : 'center'
        }],
        onLoadSuccess:function(){
            /*$("#loading").css('display','none');*/
        },
        onLoadError : function() {
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

/**
 * 未关联人员列表
 */
function userList(){
    var roleId = $(".menu-ul").find(".current").children("span").text();
    var userName = $.trim($("#userName").val());
    var employeeNumber = $.trim($("#employeeNumber").val());
    var deptId = $.trim($("#deptName").val());
    var companyId = $.trim($("#companyName").val());

    $("#loading").css('display','block');
    $("#userTable").bootstrapTable('destroy');
    $("#userTable").bootstrapTable({
        url:"/system/role/findUserWithNoRole",
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        clickToSelect : true, //是否启用点击选中行
        maintainSelected : true,
        queryParams : function(params) {
             var param={
                 roleId:roleId,
                 pageNumber:params.pageNumber,
                 pageSize:params.pageSize,
                 userName:userName,
                 employeeNumber:employeeNumber,
                 deptName:deptId,
                 companyName:companyId
             };
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
            formatter:function(value,row,index){
                if($.inArray(row.id.toString(),select_rows) != -1){
                    return {
                        checked:true
                    }
                }
                return {
                    checked:false
                }
            }
        },{
            field : "id",
            title : "id",
            align : 'center',
            visible : false
        },{
            field : "userName",
            title : "姓名",
            align : 'center'
        },{
            field : "userAccount",
            title : "用户名",
            align : 'center'
        },{
            field : "employeeNumber",
            title : "工号",
            align : 'center'
        },{
            field : "deptName",
            title : "所属部门",
            align : 'center'
        },{
            field : "companyName",
            title : "所属公司",
            align : 'center'
        }],
        onLoadSuccess:function(){
        	$("#loading").css('display','none');
        },
        onLoadError : function() {
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        },
        onPageChange:function(){
            $("#loading").css('display','block');
        }
    });

}
//保存角色/权限设置
function updateRoleBtn(){
    var idArr=[];
    $("#tb1 input:checkbox:checked").each(function(){
        idArr.push(Number( $(this).val() ))
    });

	$("#loading").css('display','block');
	  $.ajax({
	        url:"/system/role/updateRoleMenu",
	        method:"post",
	        dataType: "json",
	        traditional:true,
	        data: {
	        	id:$(".menu-ul").find(".current").children("span").text(),
	        	menuIds:idArr
	        },
	        success:function(data){
	        	$("#loading").css('display','none');
	        	if (data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });

                } else {
                    layer.alert('修改成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    select_rows = new Array();
                    $("#tb2").bootstrapTable('refresh');
                    menuButton();
                }
	        },
            error:function(){
              $("#loading").css('display','none');
              layer.alert(errorDefect, {
                  icon: 2,
                  title: "提示信息"
              });
	        }
	    })
}

// 取消按钮
function resetRoleBtn(){
    select_rows = new Array();
    $("#tb2").bootstrapTable('refresh');
    menuButton();
}

// 条件查询未关联人员
function findUserWithNoRole(){
    userList()
}

// 添加角色
function insertRole(){
    $("#roleForm").data('bootstrapValidator').validate();
    if ( !$("#roleForm").data('bootstrapValidator').isValid() ) {
        return false;
    }

    $("#loading").css('display','block');
    $.ajax({
        url:"/system/role/insertRole",
        method:"post",
        dataType: "json",
        contentType:"application/json;charset=utf-8",
        data:JSON.stringify({
            roleName:$.trim($("#roleName").val()),
            status:1
        }),
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
                // window.location.href = "/systemui/error/500";
            } else if (data.result == true){
                leftMenu();
                $("#roleModal").modal("hide");
            } else{
                layer.alert("该角色已存在,请勿重复添加！", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })
}

// 单个角色获取菜单按钮
function menuButton(){
    $("#loading").css('display','block');
	 $("#tb1").jqGrid("clearGridData");
	 $("#tb1").jqGrid('setGridParam',{
		 	url:"/system/role/getRoleMenu",
	        postData:{
	       	     id:$(".menu-ul").find(".current").children("span").text()
	        },
	        page:1
	}).trigger("reloadGrid");
}


// 单个角色添加未关联人员
function insertRoleUser(){
    var roleId = $(".menu-ul").find(".current").children("span").text();
    if (typeof(select_rows) == 'undefined' || select_rows.length <= 0){
        layer.alert("请选中一行", {
            icon: 2,
            title: "提示信息"
        });
    } else {
        var userId=[];
        for (var i = 0; i < select_rows.length; i++) {
            userId.push(select_rows[i]);
        }
        $.ajax({
            url:"/system/role/insertRoleUser",
            method:"post",
            traditional:true,
            data:{
                userId:userId,
                id:roleId
            },
            success:function(data){
                if (data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    layer.alert('添加成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    select_rows = new Array();
//                    findUserWithNoRole();
                    roleUserList();
                }
            },
            error:function(){
                $("#loading").css('display','none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
}

// 弹出关联人员弹框
function userModel(){
    reset();
    // findUserWithNoRole();
    $("#userTable").bootstrapTable('destroy');
    $("#userModal").modal("show");
}

// 取消关联人员
function disassociate(){
    var roleId = $(".menu-ul").find(".current").children("span").text();
    if (typeof(tb_select_rows) == 'undefined' || tb_select_rows.length <= 0){
        layer.alert("请选中一行", {
            icon: 2,
            title: "提示信息"
        });
    } else {
        var userId = [];
        for(var i = 0; i < tb_select_rows.length;i++){
            userId.push(tb_select_rows[i]);
        }
        $("#loading").css('display','block');
        $.ajax({
            url:"/system/role/updateRoleWithUser",
            method:"post",
            traditional:true,
            data:{
                userId:userId,
                roleId:roleId
            },
            success:function(data){
                $("#loading").css('display','none');
                if (data.status == 2){
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
            error:function(){
                $("#loading").css('display','none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
}
// 置为无效
function updateRole(){
    var roleId = $(".menu-ul").find(".current").children("span").text();
    var roleName = $(".menu-ul").find(".current").children("a").text();
    var _roleName = '<span style="color: red">'+ roleName +'</span>';
    layer.confirm('您确定要置  '+ _roleName +'  角色为无效吗？', {
        btn: ['确定','取消'], //按钮
        title: "提示信息"
    }, function(){
        layer.closeAll('dialog');
        $("#loading").css('display','block');
        $.ajax({
            url:"/system/role/updateRole",
            method:"post",
            data:JSON.stringify({
                id:roleId,
                status:2
            }),
            contentType:'application/json;charset=UTF-8',
            success:function(data){
                $("#loading").css('display','none');
                if (data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    layer.alert('该角色置为无效成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    findUserWithNoRole();
                    select_rows = new Array();
                    leftMenu();
                }
            },
            error:function(){
                $("#loading").css('display','none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        });
    })
}

// 清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}

function clearSearch(){
    reset();
    userList();
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

// 表单验证
function formValidator() {
    $('#roleForm').bootstrapValidator({
        excluded : [ ':disabled' ],
        message: 'This value is not valid',//通用的验证失败消息
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        live : 'enabled',
        fields : {
            roleName : {
                validators : {
                    notEmpty : {
                        message : '请输入角色名称！'
                    }
                }
            }
        }
    })
}

/**
 * 重构表单验证
 */
function refactorFormValidator(){
    $('#roleModal').on('hidden.bs.modal', function() {
        $("#roleForm").data('bootstrapValidator').destroy();
        $('#roleForm').data('bootstrapValidator', null);
        formValidator();
    })
}


//表格选中事件
function checkSelectRows(id){
    $(id).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var singleSelect = $(id).bootstrapTable('getOptions').singleSelect;
        var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
        if(singleSelect == true ){// 单选
            select_rows = [];
            select_rows.push(datas[0]);
        } else { // 多选
            examine(e.type,datas,id);                              // 保存到全局 Array() 里
        }
    });
}

//多选
function examine(type,datas,id){
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            select_rows.indexOf(v.id.toString()) == -1 ? select_rows.push(v.id.toString()) : -1;
            if(id == "#tb2"){
                tb_select_rows.indexOf(v.id.toString()) == -1 ? tb_select_rows.push(v.id.toString()) : -1;
            }
        });
    }else{
        $.each(datas,function(i,v){
            select_rows.splice(select_rows.indexOf(v.id.toString()),1);    //删除取消选中行
            if(id == "#tb2"){
            	tb_select_rows.splice(tb_select_rows.indexOf(v.id.toString()),1);    //删除取消选中行
            }
        });
    }
}












