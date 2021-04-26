/**
 * Description: 数据库管理JavaScript
 * Author:niexingquan
 * Date: 2019/10/24
 */

var search_data = {
    systemId: '',
    systemModuleId: '',
    environmentType: ''
}
var edit_flag  = true; //控制密码隐藏

$(function () {
    downOrUpButton();
    buttonClear();
    initTable();
    getProjectGroup();
    _formValidator();
    $("#addModel").on('hidden.bs.modal',function(){
        $("#check_password").prop("checked",false);
        $("#data_password").attr('password','');
        $("#_addFrom").data('bootstrapValidator').destroy();
        $('#_addFrom').data('bootstrapValidator', null);
        _formValidator();
    });
    $('#systemId').bind('change',function(){
    //   $('#systemModuleId').empty();
      $('#systemModuleId').not($('#systemModuleId option:first-child')).empty();
      $('#environmentType').empty();
      getEnvAndModuleBySystemId($(this).val(),1)
    })
    $('select.systemId').bind('change',function(){
      $('select.systemModuleId').empty();
      $('select.environmentType').empty();
      getEnvAndModuleBySystemId($(this).val(),2)
    })
    $("#check_password").click(function(){
        if($(this).prop("checked")){
            $("#data_password").val($("#data_password").attr('password'));
        }
        else{
            var _val = $("#data_password").attr('password');
            $("#data_password").val(_val.replace(/\S/g, '●'));
        }
    })
    document.getElementById("data_password").addEventListener('input', function () {
        var _this = this;
        var newPassword = _this.value;
        var oldPassword = _this.getAttribute("password");
        var deta = newPassword.length-oldPassword.length;

        var truePassword = "";
        var p = _this.selectionEnd;//光标结束时的位置

        //密码敏感处理
        for(var i=0; i<newPassword.length; i++){
            var c = newPassword.charAt(i);
            if(i<p && c!='●'){
                truePassword += c;
            }else if(i<p && c=='●'){
                truePassword +=  oldPassword.charAt(i);
            }else {
                truePassword += oldPassword.substr(oldPassword.length-newPassword.length+p,newPassword.length-p);
                break;
            }

        }
        newPassword = truePassword.replace(/\S/g, '●');

        _this.setAttribute('password', truePassword);
        if($("#edit_id").val()){
            _this.value = newPassword;
        }
        // 解决在win8中光标总是到input框的最后	
        _this.selectionEnd = p;
        _this.selectionStart = p;

        //console.log(truePassword);
    },false);
});

//查询当前用户所在项目组
function getProjectGroup(){
    $("#loading").show();
    $.ajax({
        type: "post",
        url: '/devManage/dbConfig/getSystemByUserId',
        dataType: "json",
        success: function (data) {
            // if (data.status == 1) {
                $.each(data.systemInfos,function(idx,val){
                    $('#systemId').append(`<option value="${val.id}">${val.systemName}</option>`);
                    $('select.systemId').append(`<option value="${val.id}">${val.systemName}</option>`);
                })
                $(".selectpicker").selectpicker('refresh');
            // }else{
            //     layer.alert(data.errorMessage, {
            //         icon: 2,
            //         title: "提示信息"
            //     });
            // }
            
        }
    })
}

//查询系统模块和环境
function getEnvAndModuleBySystemId(Id,Idx){
  $.ajax({
    type: "post",
    url: '/devManage/dbConfig/getEnvAndModuleBySystemId',
    dataType: "json",
    async:false,
    data:{
      'systemId':Id
    },
    success: function (data) {
        if (Idx == 1) {
            $('#systemModuleId').empty();
            $('#environmentType').empty();
            $('#systemModuleId').append(`<option value="">请选择</option>`);
            $('#environmentType').append(`<option value="">请选择</option>`);
            data.modules.length && $.each(data.modules,function(idx,val){
                $('#systemModuleId').append(`<option value="${val.id}">${val.moduleName}</option>`);
            })
            data.envList.length && $.each(data.envList,function(idx,val){
                $('#environmentType').append(`<option value="${val.envVaule}">${val.envName}</option>`);
            })
        }else{
          $('select.systemModuleId').empty();
          $('select.environmentType').empty();
          $('select.systemModuleId').append(`<option value="">请选择</option>`);
          $('select.environmentType').append(`<option value="">请选择</option>`);
          data.modules.length && $.each(data.modules,function(idx,val){
              $('select.systemModuleId').append(`<option value="${val.id}">${val.moduleName}</option>`);
          })
          data.envList.length && $.each(data.envList,function(idx,val){
              $('select.environmentType').append(`<option value="${val.envVaule}">${val.envName}</option>`);
          })
            // layer.alert(data.errorMessage, {
            //     icon: 2,
            //     title: "提示信息"
            // });
        }
        $(".selectpicker").selectpicker('refresh');
    },error:function(){
      if (Idx == 1) {
        $('#systemModuleId').empty();
        $('#environmentType').empty();
      }else{
        $('select.systemModuleId').empty();
        $('select.environmentType').empty();
      }
      $(".selectpicker").selectpicker('refresh');
    }
  })
}

//初始化列表
function initTable() {
    var page = $('#dataBaseList').getGridParam('page');
    $("#dataBaseList").jqGrid("clearGridData");
    $("#dataBaseList").jqGrid("setGridParam", {page: page != null && page != undefined ? page : 1});
    $("#dataBaseList").jqGrid({
        url: "/devManage/dbConfig/getAllDbConfig",
        datatype: 'json',
        mtype: "POST",
        height: 'auto',
        width: $(".content-table").width() * 0.999,
        autowidth: true,
        rowNum: 10,
        rowTotal: 200,
        rowList: [10, 20, 30],
        rownumWidth: 40,
        pager: '#dataBaseListPager',
        sortable: false,   //是否可排序
        loadtext: "数据加载中......",
        viewrecords: true, //是否要显示总记录数
        postData: search_data,
        colNames: ['id', '系统', '子系统', '环境', '驱动类', 'URL', '用户名', '操作'],
        colModel: [
            { name: 'id', index: 'id', hidden: true },
            {
                name: "systemName",
                align: 'center',
                search: false,
            },
            {
                name: "moduleName",
                align: 'center',
                search: false,
            },
            {
                name: "environmentTypeName",
                align: 'center',
                search: false,
            },
            {
                name: "driverClassName",
                align: 'center',
                search: false,
            },
            {
                name: "url",
                align: 'center',
                search: false,
            },
            {
                name: "userName",
                align: 'center',
                search: false,
            },
            {
                name: "opt",
                align: 'opt',
                search: false,
                align: 'center',
                formatter: function (value, grid, row, index) {
                    var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                    return '<a class="a_style" onclick="_edit(this)" data="'+ rows + '">编辑</a>&nbsp;&nbsp;<a class="a_style" onclick="_delete('+ row.id + ')">删除</a>'
                }
            }
        ],
        loadComplete: function () {
            $("#loading").css('display', 'none');
        },
        beforeRequest: function () {
            $("#loading").css('display', 'block');
        },
        loadError: function () {
            $("#loading").css('display', 'none');
        }
    }).trigger("reloadGrid");
}

// 新建
function _add() {
    $("#check_password_body").hide();
    $("#edit_id").val('');
    $('#_addFrom').find('select').each(function (idx, val) {
        $(val).selectpicker('val', '');
    })
    $('#_addFrom').find('input').each(function (idx, val) {
        $(val).val('');
    })
    $(".selectpicker").selectpicker('refresh');
    $('#microChildService').text('新建数据库');
    $("#addModel").modal("show");
}

// 编辑
function _edit(This) {
    $("#check_password_body").show();
    $('#_addFrom').find('select').each(function (idx, val) {
        $(val).selectpicker('val', '');
    })
    $('#_addFrom').find('input').each(function (idx, val) {
        $(val).val('');
    })  
    $('#microChildService').text('编辑数据库');
    var data = JSON.parse($(This).attr('data'));
    $("#edit_id").val(data.id);
    $("select.systemId").val(data.systemId);
    getEnvAndModuleBySystemId(data.systemId,2);
    $("select.systemModuleId").selectpicker('val',data.systemModuleId);
    $("select.environmentType").selectpicker('val',data.environmentType);
    $(".driverClassName").val(data.driverClassName);
    $(".url").val(data.url);
    $("#data_userName").val(data.userName);
    $("#data_password").attr('password',data.password);
    $("#data_password").val($("#data_password").attr('password').replace(/\S/g, '●'));
    $(".selectpicker").selectpicker('refresh');
    edit_flag  = false;
    $("#addModel").modal("show");
}

// 新增  编辑
function addSubmit() {
    $('#_addFrom').data('bootstrapValidator').validate();
    if (!$('#_addFrom').data('bootstrapValidator').isValid()) {
        return false;
    }
    $("#loading").css('display', 'block');
    var data = {
        id: $("#edit_id").val() || '',
        systemId: $.trim($("select.systemId").val()),
        systemModuleId: $.trim($("select.systemModuleId").val()),
        environmentType: $.trim($("select.environmentType").val()),
        driverClassName: $.trim($(".driverClassName").val()),
        url:$.trim($(".url").val()),
        userName: $.trim($("#data_userName").val()),
        password: !$("#edit_id").val() ? $.trim($("#data_password").val()) : $.trim($("#data_password").attr('password')),
    }
    $.ajax({
        type: "post",
        url: !$("#edit_id").val() ? '/devManage/dbConfig/addDbConfig' : '/devManage/dbConfig/updateDbConfig',
        dataType: "json",
        data: data,
        success: function (result) {
            if (result.status == 1) {
                $("#addModel").modal('hide');
                layer.alert('保存成功!', {
                    icon: 1,
                    title: "提示信息"
                });
                $("#dataBaseList").trigger("reloadGrid");
            } else {
                layer.alert(data.message, {
                    icon: 2,
                    title: "提示信息"
                });
            }
            $("#loading").hide();
        },error:function(){
           
        }
    })
    
}

// 删除
function _delete(Id) {
    
    layer.confirm('你确定要删除吗？', {
        btn: ['确定','取消'],
        title:'提示信息',
        icon:2
    }, function(){
        layer.closeAll(); 
        $("#loading").css('display', 'block');
        $.ajax({
            type: "post",
            url: '/devManage/dbConfig/deleteDbConfig',
            dataType: "json",
            data: {
                id:Id
            },
            success: function (result) {
                if (result.status == 1) {
                    layer.alert('删除成功!', {
                        icon: 1,
                        title: "提示信息"
                    });
                    $("#dataBaseList").trigger("reloadGrid");
                } else {
                    layer.alert(data.message, {
                        icon: 2,
                        title: "提示信息"
                    });
                }
                $("#loading").hide();
            },error:function(){
            }
        })
        layer.close();
    })
    
}

//查询
function searchInfo() {
    $('#search_div').find('select').each(function (idx, val) {
        search_data[$(val).attr('id')] = $(val).selectpicker('val');
    })
    $("#dataBaseList").jqGrid('setGridParam', {
        postData: search_data,
        page: 1,
    }).trigger("reloadGrid");
}

//重置
function clearSearch() {
    $('#search_div').find('select').each(function (idx, val) {
        $(val).selectpicker('val', '');
        search_data[$(val).attr('id')] = '';
    })
    $('#systemModuleId').empty();
    $('#environmentType').empty();
    $(".selectpicker").selectpicker('refresh');
}

//校验
function _formValidator(){
    $('#_addFrom').bootstrapValidator({
        excluded : [ ':disabled' ],//下拉框验证
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        // live : 'enabled',
        fields : {
            systemId : {
                validators : {
                    notEmpty:{
                        message: '系统不能为空！'
                    }
                }
            },
            // systemModuleId : {
            //     validators : {
            //         notEmpty:{
            //             message: '子系统不能为空！'
            //         }
            //     }
            // },
            environmentType : {
                validators : {
                    notEmpty:{
                        message: '环境不能为空！'
                    }
                }
            },
            driverClassName : {
                validators : {
                    notEmpty:{
                        message: '驱动类不能为空！'
                    }
                }
            },
            url : {
                validators : {
                    notEmpty:{
                        message: 'url不能为空！'
                    }
                }
            },
            data_userName : {
                validators : {
                    notEmpty:{
                        message: '用户名不能为空！'
                    }
                }
            },
            data_password : {
                validators : {
                    notEmpty:{
                        message: '密码不能为空！'
                    }
                }
            },
        }
    })
}