/**
 * 工具信息配置
 */

var _GIT_IMG = "../static/images/tool/git.png";
var _SVN_IMG = "../static/images/tool/svn.png";
var _ARTIFACTORY_IMG = "../static/images/tool/Artifactory.png";
var _JENKINS_IMG = "../static/images/tool/jenkins.png";
var _NEXUS_IMG = "../static/images/tool/nexus.png";
var _SONAR_IMG = "../static/images/tool/Sonar.png";
var errorDefect = '系统内部错误，请联系管理员 ！！！';

var typeId = '';

$(document).ready(function() {
    $("#loading").css('display','block');
    list();
    formValidator();
    refactorFormValidator(); 
});

//页面列表
function list(){
    $(".allTableDiv").empty();
    $.ajax({
        url:"/devManage/tool/list",
        method:"post",
        success:function(data){

            if(data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else {
                var i=0;
                for(var key in data){
                    var arr = key.split(",");
                    var keyName = arr[0];
                    var keyId = arr[1];
                    initTable(i,data[key],keyName,keyId);
                    i++;
                }
            }
            $("#loading").css('display','none');
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

//展示数据
function initTable(i,data,key,keyId) {
    var result = JSON.stringify(data).replace(/"/g, '&quot;');
    var str ="<div class='tableDiv'><div class='tableTit'><span id='key'>"+key;
    if (toolSave == true ){
        str += "&nbsp;&nbsp;&nbsp;&nbsp;" +
            "</span><a href='#'  onclick='insert("+result+","+keyId+",this)'>添加</a></div>";
    }
    $(".allTableDiv").append(str);
    var j=0;
    for(var key1 in data){
        initTable1(i,key1,data[key1],key);
        j++; 
    }

}

//展示具体一类工具信息
function initTable1(num,key1,data,functionType){

    var toolImgPath = isToolImgPath(key1);

    $(".tableDiv").eq(num).append('<div class="tableContent">' +
        '<img class="serviceLoge" src='+toolImgPath+' />&nbsp;&nbsp;&nbsp;'
        +key1+'<table id="tb'+key1+'"></table></div></div>');

    $("#tb"+key1).bootstrapTable('destroy');
    $("#tb"+key1).bootstrapTable({
        pagination : false,
        showHeader: false,
        sidePagination:'client',
        data:data,
        showFooter: false,
        columns : [{
            field : "toolName",
            title : "toolName",
            align : 'left',
            formatter : function(value, row, index) {
                var toolName = row.toolName == null?'':row.toolName;
                return '<div class="table_Td toolName"><div class="fontBold">工具名：</div><div class="fontContent" title ="'+toolName+'">'+toolName+'</div></div>';
            }
        },{
            field : "ip",
            title : "ip",
            align : 'left',
            formatter : function(value, row, index) {
                var ip = row.ip == null?'':row.ip;
                return '<div class="table_Td ip"><div class="fontBold">IP地址：</div><div class="fontContent">'+ip+'</div></div>';
            }
        },{
            field : "port",
            title : "port",
            align : 'left',
            formatter : function(value, row, index) {
                var port = row.port == null?'':row.port;
                return '<div class="table_Td port"><div class="fontBold">端口号：</div><div class="fontContent">'+port+'</div></div>';
            }
        },{
            field : "userName",
            title : "userName",
            align : 'left',
            formatter : function(value, row, index) {
                var userName = row.userName == null ?'':row.userName;
                return '<div class="table_Td username"><div class="fontBold">用户名：</div><div class="fontContent">'+userName+'</div></div>';
            }

        },{
            field : "jenkinsCredentialsId",
            title : "jenkinsCredentialsId",
            align : 'left',
            formatter : function(value, row, index) {
                    if ( functionType == "源代码托管"){
                        var jenkinsCredentialsId = row.jenkinsCredentialsId == null ?'':row.jenkinsCredentialsId;
                        return '<div class="table_Td jenkinsCredentialsId"><div class="fontBold">JENKINS_ID：</div><div class="fontContent">'+jenkinsCredentialsId+'</div></div>';
                    } else {
                        false
                        return '';
                    }
            }
        }, {
            field : "sonarApiToken",
            title : "sonarApiToken",
            align : 'left',
            formatter : function(value, row, index) {
                    if ( functionType == "代码扫描"){
                        var sonarApiToken = row.sonarApiToken == null?'':row.sonarApiToken;
                        return '<div class="table_Td sonarApiToken"><div class="fontBold">ApiToken：</div><div class="fontContent" title ="'+sonarApiToken+'">'+sonarApiToken+'</div></div>';
                    } else {
                        false
                        return '';
                    }
            }
        },{
            field : "jenkinsPluginName",
            title : "jenkinsPluginName",
            align : 'left',
            formatter : function(value, row, index) {
                    if ( functionType == "代码扫描"){
                        var jenkinsPluginName = row.jenkinsPluginName == null?'':row.jenkinsPluginName;
                        return '<div class="table_Td jenkinsPlugin"><div class="fontBold">JENKINS插件名称：</div><div class="fontContent">'+jenkinsPluginName+'</div></div>';
                    } else {
                        false
                        return '';
                    }
            }
        },{
            field : "artifactRepositoryId",
            title : "artifactRepositoryId",
            align : 'left',
            formatter : function(value, row, index) {
                if ( functionType == "CI/CD工具"){
                    var artifactRepositoryId = row.artifactRepositoryId == null?'':row.artifactRepositoryId;
                    return '<div class="table_Td artifactPlugin"><div class="fontBold">制品仓库管理员ID：</div><div class="fontContent">'+artifactRepositoryId+'</div></div>';
                } else {
                    false
                    return '';
                }
            }
        },{
            field : "id",
            title : "id",
            visble:false,
            formatter : function(value, row, index) {
                return '<input type="hidden" class="idInfo" value="'+row.id+'" />';
            }
        },{
            field : "homePath",
            title : "homePath",
            align : 'left',
            visble:false,
            formatter : function(value, row, index) {
                var homePath = row.homePath == null?'':row.homePath;
                return '<input class="inputContent form-control pathInfo" type="hidden" value="'+homePath+'"/>';
            }
        },{
            field : "opt",
            title : "opt",
            align : 'right',
            formatter : function(value, row, index) {
                var obj = {};
                obj["id"] = row.id;
                obj["key"] = key1;
                obj["functionKey"] = functionType;
                var rows = JSON.stringify(obj).replace(/"/g, '&quot;');
                var a = '<div class="botton_btn"><botton class="btn btn-primary editBtn" onclick="edit(' + rows +')">编辑</botton>';
                var b = '<botton class="btn btn-primary deleteBtn" onclick="deleteTool(' + row.id +')">删除</botton>';;
                var opt = [];
                if (toolEdit == true){
                    opt.push(a);
                }
                if (toolDelete == true) {
                    opt.push(b);
                }
                return opt.join("");
            }
        }],
        onLoadSuccess:function(){
        },
        onLoadError : function() {

        }
    });
}

//编辑工具面板展示
function edit( toolInfo ) {
    tokenStyle();
    $.ajax({
        url:"/devManage/tool/getToolEntity",
        type:"post",
        data:{
            toolId:toolInfo.id
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                var rows = data.toolInfo;
                isShowFunctionType(toolInfo.functionKey);
                $(".toolName_edit").html(toolInfo.key);

                var toolImgPath = isToolImgPath(toolInfo.key);
                $(".Loge").attr("src",toolImgPath);
                $("#id_edit").val(rows.id);
                $("#toolName_edit").val("");
                $("#toolName_edit").val(rows.toolName);
                $("#context_edit").val("");
                $("#context_edit").val(rows.context);
                $("#ip_edit").val(rows.ip);
                $("#homePath_edit").val(rows.homePath);
                $("#port_edit").val(rows.port);
                $("#userName_edit").val(rows.userName);
                $("#password_edit").val(rows.password);
                $("#jenkinsCredentialsId_edit").val(rows.jenkinsCredentialsId);
                $("#sonarApiToken_edit").val(rows.sonarApiToken);
                $("#jenkinsPluginName_edit").val(rows.jenkinsPluginName);
                $("#edit_artifactRepositoryId").val(rows.artifactRepositoryId);
                $("#editApiToken").val(rows.gitApiToken);
                if(rows.toolType == 1){
                	$(".eapi").css("display","block");
                	$("#editApiToken").css("display","block");
                }else{
                	$(".eapi").css("display","none");
                	$("#editApiToken").css("display","none");
                }

                var environmentType = rows.environmentType;
                if (environmentType != null && environmentType.length > 0){
                    var environmentTypeArr = environmentType.split(",");
                    $("#edit_environmentType_select option").each( function (i, n) {
                        for (var ii = 0,len = environmentTypeArr.length; ii < len ; ii++){
                            if (n.value == environmentTypeArr[ii]) {
                                n.selected = true;
                            }
                        }
                    });
                }

                $("#accessProtocol_edit option").each( function (i, n) {
                    if (n.value == rows.accessProtocol) {
                        n.selected = true;
                    }
                });

                $(".selectpicker").selectpicker('refresh');
                $("#editModal").modal("show");
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

function commitInfo() {
    $("#editForm").data('bootstrapValidator').validate();
    if ( !$("#editForm").data('bootstrapValidator').isValid() ) {
        return;
    }
    $("#promptBox").modal("show");

}

//更新提交
function updateTool(){
    var select = document.getElementById("edit_environmentType_select");
    var str = [];
    for(var i=0;i < select.length;i++){
        if(select.options[i].selected){
            str.push(select[i].value);
        }
    }

    $.ajax({
        url:"/devManage/tool/update",
        method:"post",
        data:JSON.stringify({
            "id":$.trim($("#id_edit").val()),
            "toolName":$.trim($("#toolName_edit").val()),
            "homePath":$.trim($("#homePath_edit").val()),
            "context":$.trim($("#context_edit").val()),
            "ip":$.trim($("#ip_edit").val()),
            "port":$.trim($("#port_edit").val()),
            "userName":$.trim($("#userName_edit").val()),
            "password":$.trim($("#password_edit").val()),
            "jenkinsCredentialsId":$.trim($("#jenkinsCredentialsId_edit").val()),
            "sonarApiToken":$.trim($("#sonarApiToken_edit").val()),
            "jenkinsPluginName":$.trim($("#jenkinsPluginName_edit").val()),
            "artifactRepositoryId":$.trim($("#edit_artifactRepositoryId").val()),
            "environmentType":str.toString(),
            "accessProtocol":$.trim($("#accessProtocol_edit").find("option:selected").val()),
            "gitApiToken" : $.trim($("#editApiToken").val())
        }),
        dataType: 'json',
        contentType:'application/json;charset=UTF-8',
        success:function(data){
            if(data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else {
                layer.alert("修改成功",{
                    icon:1,
                    title:"提示信息"
                });
                $("#editModal").modal("hide");
                $("#promptBox").modal("hide");
                $("#loading").css('display','none');
                list();
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

//删除工具
function deleteTool(toolId){
    layer.confirm("确定删除此条配置吗？",{
        btn: ['确定','取消'],
        title: "提示信息"
    },function(){
        layer.closeAll('dialog');
        $.ajax({
            url:"/devManage/tool/removeTool",
            method:"post",
            data:{
                "id":toolId
            },
            dataType: 'json',
            success:function(data){
                if(data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    layer.alert("删除成功",{
                        icon:1,
                        title:"提示信息"
                    });
                    $("#loading").css('display','none');
                    list();
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
    })
}

function insert(data,keyId,that){
    tokenStyle();
    $("#functionType").val(keyId);
    var functionType = $.trim($(that).parent().children("#key").text());
    $("#apiToken").val('');
    typeId = isShowFunctionType(functionType);
    $("#insertModal").modal("show");

}

//添加
function insertTool(){
    $("#toolForm").data('bootstrapValidator').validate();
    if ( !$("#toolForm").data('bootstrapValidator').isValid() ) {
        return;
    }

    var select = document.getElementById("environmentType");
    var str = [];
    for(var i=0;i < select.length;i++){
        if(select.options[i].selected){
            str.push(select[i].value);
        }
    }

    var type_Id = $.trim($(typeId).find("option:selected").val());
    $.ajax({
        url:"/devManage/tool/insert",
        method:"post",
        dataType: 'json',
        contentType:'application/json;charset=UTF-8',
        data:JSON.stringify({
            "toolName":$.trim($("#toolName").val()),
            "functionType":$("#functionType").val(),
            "toolType":type_Id,
            "homePath":$.trim($("#homePath").val()),
            "context":$.trim($("#context").val()),
            "ip":$.trim($("#ip").val()),
            "port":$.trim($("#port").val()),
            "userName":$.trim($("#userName").val()),
            "password":$.trim($("#password").val()),
            "jenkinsCredentialsId":$.trim($("#jenkinsCredentialsId").val()),
            "sonarApiToken":$.trim($("#sonarApiToken").val()),
            "jenkinsPluginName":$.trim($("#jenkinsPluginName").val()),
            "artifactRepositoryId":$.trim($("#artifactRepositoryId").val()),
            "environmentType":str.toString(),
            "accessProtocol":$.trim($("#accessProtocol_sel").find("option:selected").val()),
            "gitApiToken":$.trim($("#apiToken").val())
        }),
        success:function(data){
            if(data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else {
                layer.alert("添加成功", {
                    icon: 1,
                    title: "提示信息"
                });
                list();
                tokenStyle();
                $("#insertModal").modal("hide");
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

function tokenStyle(){
    $(".token").css("display","none");
    $(".jenkinsID").css("display","none");
    $(".git").css("display","none");
    $(".sonar").css("display","none");
    $(".jenkinsPluginName").css("display","none");
    $(".art").css("display","none");
    $(".artifactId").css("display","none");

    $("#productManagement").css("display","none");
    $("#sourceCodeList").css("display","none");
    $("#codeScanning").css("display","none");
    $("#CI_CD").css("display","none");

    $("#edit_environmentType").css("display","none");
    $("#new_environmentType").css("display","none");

    $("#toolTypeName").empty();
    $("#editForm small").html('');
    $("#toolForm small").html('');
    typeId = '';
    reset();
}

function change(){
	if($("#toolTypeNameSourceCode").val()==1){
		$(".api").css("display","block");
	}else{
		$(".api").css("display","none");
	}
}

function isShowFunctionType(functionType){
    var _typeId =  '';
    if (functionType == "源代码托管"){
        $(".jenkinsID").css("display","inline-block");
        $(".git").css("display","inline-block");
        $("#sourceCodeList").css("display","block");
        $("#accessProtocol_sel").css("display","block");
        $("#accessProtocol_sel_edit").css("display","block");
        _typeId = "#sourceCodeList";
    }
    if (functionType == "代码扫描"){
        $(".token").css("display","inline-block");
        $(".sonar").css("display","inline-block");
        $(".jenkinsPluginName").css("display","inline-block");
        $("#codeScanning").css("display","block");
        $("#edit_environmentType").css("display","block");
        $("#new_environmentType").css("display","block");
        _typeId = "#codeScanning";
    }
    if (functionType == "CI/CD工具"){
        $(".art").css("display","inline-block");
        $(".artifactId").css("display","inline-block");
        $("#CI_CD").css("display","block");
        $("#edit_environmentType").css("display","block");
        $("#new_environmentType").css("display","block");
        _typeId = "#CI_CD";
    }
    if (functionType == "制品管理"){
        $("#productManagement").css("display","block");
        _typeId = "#productManagement";
    }
    return _typeId;
}

function isToolImgPath(key){
    var toolImgPath = '';
    switch (key){
        case "GIT":
            toolImgPath = _GIT_IMG;
            break;
        case "SVN":
            toolImgPath = _SVN_IMG;
            break;
        case "SONAR":
            toolImgPath = _SONAR_IMG;
            break;
        case "JENKINS":
            toolImgPath = _JENKINS_IMG;
            break;
        case "ARTIFACTORY":
            toolImgPath = _ARTIFACTORY_IMG;
            break;
        case "NEXUS":
            toolImgPath = _NEXUS_IMG;
            break;
        default:
            break;
    }

    return toolImgPath;
}

// 清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}

function reset() {
    $("#toolTypeName").selectpicker('val', '');
    $("#homePath").val("");
    $("#toolName").val("");
    $("#context").val("");
    $("#ip").val("");
    $("#port").val("");
    $("#userName").val("");
    $('#password').val("");
    $("#jenkinsCredentialsId").val("");
    $("#jenkinsPluginName").val("");
    $("#sonarApiToken").val("");
    $(".btn_clear").css("display","none");
    $("#environmentType").selectpicker('val', null);
    $("#edit_environmentType_select").selectpicker('val', null);
    $("#accessProtocol").selectpicker('val', '');
    $("#accessProtocol_edit").selectpicker('val', '');
}

// 重构表单验证
function refactorFormValidator(){
    $('#insertModal').on('hidden.bs.modal', function() {
        $("#toolForm").data('bootstrapValidator').destroy();
        $('#toolForm').data('bootstrapValidator', null);
        formValidator();
    })
    $('#editModal').on('hidden.bs.modal', function() {
        $("#editForm").data('bootstrapValidator').destroy();
        $('#editForm').data('bootstrapValidator', null);
        formValidator();
    })
}

// 表单验证
function formValidator(){
    $('#toolForm').bootstrapValidator({
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        live : 'enabled',
        fields : {
            toolName : {
                validators : {
                    notEmpty : {
                        message: ''
                    }
                }
            },
            toolTypeNameSourceCode : {
                validators : {
                    notEmpty :'',
                    callback : {
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            toolTypeNameCodeScanning : {
                validators : {
                    notEmpty : {
                        message : ''
                    },
                    callback : {
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            toolTypeNameCICD : {
                validators : {
                    notEmpty : {
                        message : ''
                    },
                    callback : {
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            productManagement : {
                validators : {
                    notEmpty : {
                        message : ''
                    },
                    callback : {
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            homePath : {
                validators : {
                    notEmpty : {
                        message : ''
                    }
                }
            },
            ip : {
                validators : {
                    notEmpty : {
                        message : ''
                    }
                }
            },
            port : {
                validators : {
                    notEmpty : {
                        message : ''
                    }
                }
            },
            apiToken : {
                validators : {
                    notEmpty : {
                        message : 'API TOKEN不能为空'
                    }
                }
            }
        }
    });
    $('#editForm').bootstrapValidator({
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        live : 'enabled',
        fields : {

            toolName_edit : {
                validators : {
                    notEmpty : {
                        message: ''
                    }
                }
            },
            toolTypeName : {
                validators : {
                    notEmpty : {
                        message : ''
                    }
                }
            },
            homePath : {
                validators : {
                    notEmpty : {
                        message : ''
                    }
                }
            },
            ip : {
                validators : {
                    notEmpty : {
                        message : ''
                    }
                }
            },
            port : {
                validators : {
                    notEmpty : {
                        message : ''
                    }
                }
            },
            editApiToken : {
                validators : {
                    notEmpty : {
                        message : ''
                    }
                }
            }
        }
    })
}
