/****版本管理***/
var systemName_arr = [];//全部系统
var versions_list = [];//列表版本,分组标签
var get_system_first = true;//是否第一次获取系统
var _temp_msg ;//暂存项目id
var edit_data = {//编辑信息是否改变
    version:false,
    groupFlag:false
}
$(()=>{
    $('#addVersion').on('hide.bs.modal', function() {
        $("#addVersion_form").data('bootstrapValidator').destroy();
        $('#addVersion_form').data('bootstrapValidator', null);
        formValidator();
        edit_data.version = false;
        edit_data.groupFlag = false;
    })
    $('#versionsModal').on('hide.bs.modal', function() {
        get_system_first = true;
    })
    $("#add_Version").bind('change',function(){
        if($("#edit_Version_id").val() && $("#add_Version").val() != $("#edit_Version_id").data('oldVersion')){
            edit_data.version = true;
        }else{
            edit_data.version = false;
        }
    })
    $("#add_label").bind('change',function(){
        if($("#edit_Version_id").val() && $("#add_label").val() != $("#edit_Version_id").data('oldLabel')){
            edit_data.groupFlag = true;
        }else{
            edit_data.groupFlag = false;
        }
    })
    $("#add_system").bind('change',function(){
    	get_entys();
    })
})

//获取环境类型
function get_entys(){
	if($("#add_system").val()){
    	$("#add_environment_Type").empty();
	    $("#loading").css('display', 'block');
	    $.ajax({
			url:"/projectManage/projectVersion/getEnvironmentTypes",
		    method:"post",
			data:{
				"systemIds": $("#add_system").val().join(),
			},
		   success:function(data){
			   if(data.status == 1){
				   data.environmentTypes.length && data.environmentTypes.map(function(v){
					   $("#add_environment_Type").append('<option value='+v.env+'>'+v.envName+'</option>');
				   })
				   $('.selectpicker').selectpicker('refresh');
			   }
			   $("#loading").css('display', 'none');  
		   },
		   error:function(){
			   $("#loading").css('display', 'none');  
		   }
	    });
    }else{
    	$("#add_environment_Type").empty();
    	$('.selectpicker').selectpicker('refresh');
    }
}
//获取版本内容
function _versions(data){
    $('#versions_tit').text(data.projectName+' : 版本管理').data('id',data.id);
    _temp_msg = {
        projectName:data.projectName,
        id:data.id,
    }
    $("#loading").css('display', 'block');
    $.ajax({
		url:"/projectManage/projectVersion/getProjectVersionByCon",
	    method:"post",
		data:{
			"projectId": data.id,
		},
	   success:function(result){
		   loadVersionTable(result.rows);
		   $("#loading").css('display', 'none');
	   },
	   error:function(){
		   $("#loading").css('display', 'none');  
	   }
    });
}

//加载表格
function loadVersionTable(data){
    $("#versionsTable").bootstrapTable('destroy');
	$("#versionsTable").bootstrapTable({
        data : data,
		method : "post",
	    cache: false,
		columns : [ {
			field : "id",
			title : "id",
			visible : false,
			align : 'center'
		},{
			field : "version",
			title : "版本",
			align : 'center'
		},{
			field : "groupFlag",
			title : "版本分组标签",
			align : 'center'
		},{
			field : "status",
			title : "状态",
			align : 'center',
			formatter : function(value,grid,rows,state){
                return value == 1?"已开启":"已关闭"
			}
		},{
			field : "systemNames",
			title : "系统",
            align : 'center',
            class : 'system_list',
            formatter : function(value,grid,rows,state){
                var _str = '';
                value.split(',').map(function(val,idx){
                    _str += `<div class="system_item">${val}</div>`;
                })
                return _str;
            }
		},{
			title : '操作',
			align : 'center',
			fixed : true,
			sortable : false,
			resize : false,
			search : false,
			formatter : function(value, grid, rows,state) {
                versions_list.push(grid);
				var row = JSON.stringify(grid).replace(/"/g, '&quot;');  
                var _str = '';
                if(grid.status == 1){
                    var ids = {
                        ids:grid.ids,
                        status:2,
                    }
                    _str += `<a class='a_style' style='cursor:pointer' onclick='changeStatus(${JSON.stringify(ids)})'>关闭</a>`;
                }else{
                    var ids = {
                        ids:grid.ids,
                        status:1,
                    }
                    _str += `<a class='a_style' style='cursor:pointer' onclick='changeStatus(${JSON.stringify(ids)})'>开启</a>`;
                }
                _str += ' | <a class="a_style" style="cursor:pointer" onclick="insertVersion('+ row + ')">编辑</a>';
                return _str;
			}
        } ],
        onLoadSuccess:function(){
        },
        gridComplete:function(){
            $("#versionsModal").modal('show');
            $("#loading").css('display','none');
        },
        beforeRequest:function(){
            $("#loading").css('display','block');
        },
        onLoadError : function() {

        }
    });
    $("#versionsModal").modal('show');
}

//修改状态
function changeStatus(ids){
	$("#loading").css('display','block');
	$.ajax({
		url:"/projectManage/projectVersion/closeOrOpenSystemVersion",
	    method:"post",
		data:{
			"status":ids.status,
			"ids":ids.ids
		},
	   success:function(data){
		   if(data.status == 1){
                layer.alert('编辑成功', {
                    icon: 1,
                    title: "提示信息"
                });
                _versions(_temp_msg);
		   }else{
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
           }
           $("#loading").css('display','none');
	   }
	
	});
}

//全选-反选
function select_Status(status){
    var ids=[];
    systemName_arr.map(function(val,idx){
        ids.push(val.id);
    })
    if(status){
        
    }else{
        var _ids = $("#add_system").val(),
        c = [..._ids, ...ids],
        e = Array.from(new Set(c)),
        ids = [...e.filter(_=>!_ids.includes(_)),...e.filter(_=>!ids.includes(_))]
    }
    $("#add_system").val(ids).change();
    $('.selectpicker').selectpicker('refresh');
}

//新增 编辑 版本
function insertVersion(row){
    $("#edit_Version_id").val('').data({
        'oldVersion':'',
        'oldLabel':'',
        'ids':'',
    });
    $("#add_Version").val('');
    $("#add_label").val('');
    $("#add_system").val('');
    $("#add_environment_Type").val('');
    $("#addVersion_tit").text('新增版本');
    if(get_system_first){
        $("#loading").css('display', 'block');
        $.ajax({
            url:"/projectManage/projectVersion/getSystemByProjectId",
            method:"post",
            data:{
                projectId:$('#versions_tit').data('id')
            },
            success:function(data) {
                if(data.allSystemList.length){
                    systemName_arr = data.allSystemList;
                    $("#add_system").empty();
                    data.allSystemList.map(function(val,idx){
                        $("#add_system").append(`<option value='${val.id}'>${val.systemName}</option>`)
                    })                  
                    if(row){
                        edit_give(row);
                    }
                    $('.selectpicker').selectpicker('refresh');
                    $("#addVersion").modal('show');
                }else{
                    layer.alert('该项目暂无系统!', {
                        icon: 0,
                        title: "提示信息"
                    });
                }
                $("#loading").css('display', 'none');
            }
        });
    }else{
        if(row){
        	get_entys();
            edit_give(row);
        }
        $('.selectpicker').selectpicker('refresh');
        $("#addVersion").modal('show');
    }
}

//显示编辑区域，并显示编辑前的信息
function edit_give(row){
	$("#loading").css('display', 'block');
    $.ajax({
        url:"/projectManage/projectVersion/toUpdateProjectVersion",
        method:"post",
        data:{
            "version":row.version,
            "groupFlag":row.groupFlag,
            "projectId":$('#versions_tit').data('id'),
            },
        success:function(data) {
            if(data.status == 1){
                $("#edit_Version_id").val('编辑').data({
                    'oldVersion':row.version,
                    'oldLabel':row.groupFlag,
                    'ids':row.ids,
                });
                $("#addVersion_tit").text('编辑版本');
                $("#add_Version").val(row.version);
                $("#add_label").val(row.groupFlag);
                var ids = [],environment_Types=[];
                if(data.systemList.length){
                	data.systemList.map(function(val,idx){
                        ids.push(val.systemId);
//                        environment_Types.push(val.environmentTypes.split(','));
                    })
                    $("#add_system").val(ids);
                	var ets = '';
                	ets = data.systemList[0].environmentTypes && data.systemList[0].environmentTypes.split(',');
                    $("#add_environment_Type").val(ets);
                }
                
                $('.selectpicker').selectpicker('refresh');
                $("#loading").css('display', 'none');
                $("#addVersion").modal('show');
            }
            
        }
    });
    
}

//版本提交
function insertVersion_submit(){
    var _flag = true;
    $('#addVersion_form').data('bootstrapValidator').validate();  
	if( !$('#addVersion_form').data("bootstrapValidator").isValid() ){
		return;
    }
    if(!$("#edit_Version_id").val()){
        versions_list.map(function(val){
            if($("#add_Version").val() == val.version){
                // layer.alert('版本号不能重复!', {
                //     icon: 2,
                //     title: "提示信息"
                // });
                if($("#add_label").val() == val.groupFlag){
                    layer.alert('版本号已存在,版本分组标签不能重复!', {
                        icon: 2,
                        title: "提示信息"
                    });
                    _flag = false;
                    return
                }
            }
        })
    }else{
        if(edit_data.version || edit_data.groupFlag){
            versions_list.map(function(val){
                // if($("#add_Version").val() != $("#edit_Version_id").data('oldVersion') && $("#add_Version").val() == val.version){
                if($("#add_Version").val() == val.version){
                    // if($("#add_label").val() != $("#edit_Version_id").data('oldLabel') && $("#add_label").val() == val.groupFlag && $("#add_system").val().join(',') == val.systemIds){
                    if($("#add_label").val() == val.groupFlag){
                        layer.alert('版本号已存在,版本分组标签不能重复!', {
                            icon: 2,
                            title: "提示信息"
                        });
                        _flag = false;
                        return
                    }
                }
            })
        }
    }
    if(!_flag) return
    var _temp;
    if(!$("#edit_Version_id").val()){
        _temp = {
            "version":$.trim($("#add_Version").val()),
            "groupFlag":$.trim($("#add_label").val()),
            "systemIds":$("#add_system").val().join(','),
            "environmentTypes":$("#add_environment_Type").val() && $("#add_environment_Type").val().join(),
        }
    }else{
        _temp = {
            "version":$.trim($("#add_Version").val()),
            "groupFlag":$.trim($("#add_label").val()),
            "systemIds":$("#add_system").val().join(','),
            "ids":$("#edit_Version_id").data('ids'),
            "environmentTypes":$("#add_environment_Type").val() && $("#add_environment_Type").val().join(),
        }
    }
    $("#loading").css('display','block');
    $.ajax({
        url:$("#edit_Version_id").val() ? "/projectManage/projectVersion/updateProjectVersion" : "/projectManage/projectVersion/addProjectVersion",
        method:"post",
        data:_temp,
        success:function(data){
            if(data.status == 1){
                layer.alert('保存成功', {
                    icon: 1,
                    title: "提示信息"
                });
                $("#addVersion").modal('hide');
                _versions(_temp_msg);
            }else{
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            }
            $("#loading").css('display','none');
        }
    }); 
}

/****版本管理end***/