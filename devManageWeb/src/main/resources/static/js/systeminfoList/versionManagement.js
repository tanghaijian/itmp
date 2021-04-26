$("#loading").css('display', 'block');
var systemId = null;
var moduleId = null;
var parameterArr={};//存储从页面url传过来的参数
var environmentTypes_arr = [];
$(document).ready(function(){
	parameterArr={};   
    parameterArr.arr = window.location.href.split( "?" ); 
    parameterArr.parameterArr = parameterArr.arr[1].split( "," );
    parameterArr.obj={};
    for( var i = 0 ; i < parameterArr.parameterArr.length ; i++ ){
	   	var obj = parameterArr.parameterArr[i].split( "=" );  
	   	parameterArr.obj[ obj[0] ] = obj[1];
    } 
	initPage();
});

//初始化页面数据
function initPage(){ 
	systemId = parameterArr.obj.systemId;
	moduleId = parameterArr.obj.moduleId == "" ? null : parameterArr.obj.moduleId;
	$.ajax({
		url:"/devManage/systemVersion/getSystemVersionByCon",
	    method:"post",
		data:{
			"systemId": systemId,
			"moduleId": moduleId,
			"status":null
		},
	   success:function(data){
		   loadVersionTable(data.rows);
	   },
	   complete:function(data){
		   $("#loading").css('display', 'none');  
	   }
	});
	$("#loading").css('display', 'block');
    $.ajax({
		url:"/devManage/systemVersion/getEnvironmentTypes",
	    method:"post",
		data:{
			"systemId": systemId,
		},
	   success:function(data){
		   if(data.status == 1){
			   environmentTypes_arr = data.environmentTypes;
		   }
		   $("#loading").css('display', 'none');  
	   },
	   error:function(){
		   $("#loading").css('display', 'none');  
	   }
    });
}
//加载版本表格
function loadVersionTable(data){
	$("#list3").bootstrapTable('destroy');
	$("#list3").bootstrapTable({
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
			formatter : function(value,grid,rows,
					state){
				return value == 1?"已开启":"已关闭";
			}
		},{
			title : '操作',
			align : 'center',
			fixed : true,
			sortable : false,
			resize : false,
			search : false,
			formatter : function(value, grid, rows,
					state) {
				var row = JSON.stringify(grid).replace(/"/g, '&quot;');  
				var str = '';
				if(systemVersionChange){
					if(grid.status == 1){
						str += '<a class="a_style" style="cursor:pointer" onclick="changeStatus('+ grid.id + ',2)">关闭</a>';
					}else{
						str += '<a class="a_style" style="cursor:pointer" onclick="changeStatus('+ grid.id + ',1)">开启</a>';
					}
				}
				if(systemVersionEdit){
					str += ' | <a class="a_style" style="cursor:pointer" onclick="editVersion('+ row + ')">编辑</a>';
				}
                return str;
			}
		} ]
	});
} 
//修改状态
function changeStatus(id,status){
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/systemVersion/closeOrOpenSystemVersion",
	    method:"post",
		data:{
			"groupFlag":null,
			"version":null,
			"status":status,
			"id":id
		},
	   success:function(data){
		   $("#loading").css('display','none');
		   if(data.status == 2){
			   layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
		   }else{
			   layer.alert('编辑成功', {
                    icon: 1,
                    title: "提示信息"
                });
			    initPage()
		   }
	   }
	
	});
}

//新增版本
function insertVersion(){
	layer.prompt({
		title: '输入版本号'
	},
	function(value, index, elem){
		var environmentTypes = $("#add_environment_Type").val();
		if(environmentTypes){
			environmentTypes = $("#add_environment_Type").val().join();
		}
		$.ajax({
			url:"/devManage/systemVersion/addSystemVersion",
		    method:"post",
			data:{
				"groupFlag":$("#addGroupFlag").val(),
				"version":value,
				"systemId":systemId,
				"moduleId":moduleId,
				"environmentTypes":environmentTypes,
			},
		   success:function(data){
			   layer.close(index);
			   $("#loading").css('display','none');
			   if(data.status == 2){
				   layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
			   }else{
				   layer.alert('保存成功', {
                        icon: 1,
                        title: "提示信息"
                    });
				   initPage()
			   }
		   }
		
		});  
	});
	$(".layui-layer-content").find('input').before("<label class='updLabel'>版本号</label><br/>");
	$(".layui-layer-content").append("<label class='updLabel'>版本分组标签</label>");
	$(".layui-layer-content").append("<input type='text' class='layui-layer-input' id='addGroupFlag' value=''/>");
	$(".layui-layer-content").append("<label class='updLabel'>环境类型</label>");
	$(".layui-layer-content").append('<select class="selectpicker" id="add_environment_Type" multiple data-none-selected-text="请选择" data-actions-box="true"></select>');
	$("#add_environment_Type").val('');
	$("#add_environment_Type").empty();
	environmentTypes_arr.map(function(v){
	   $("#add_environment_Type").append('<option value='+v.env+'>'+v.envName+'</option>');
   })
   $('.selectpicker').selectpicker('refresh');
}
//编辑版本
function editVersion(row){
	layer.prompt({
		formType: 0,
		title: '编辑版本',
		title2: '输入版本号',
		value:row.version
	},	
	function(value, index, elem){
		var environmentTypes = $("#add_environment_Type").val();
		if(environmentTypes){
			environmentTypes = $("#add_environment_Type").val().join();
		}
		$.ajax({
			url:"/devManage/systemVersion/updateSystemVersion",
		    method:"post",
			data:{
				"groupFlag":$("#updGroupFlag").val(),
				"version":value,
				"status":null,
				"id":row.id,
				"environmentTypes":environmentTypes,
			},
		   success:function(data){
			   layer.close(index);
			   $("#loading").css('display','none');
			   if(data.status == 2){
				   layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
			   }else{
				   layer.alert('编辑成功', {
                        icon: 1,
                        title: "提示信息"
                    });
				   initPage()
			   }
		   }		
		});
	});
	$(".layui-layer-content").find('input').before("<label class='updLabel'>版本号</label><br/>");
	$(".layui-layer-content").append("<label class='updLabel'>版本分组标签</label>");
	$(".layui-layer-content").append("<input type='text' class='layui-layer-input' id='updGroupFlag' value='"+row.groupFlag+"'/>");
	$(".layui-layer-content").append("<label class='updLabel'>环境类型</label>");
	$(".layui-layer-content").append('<select class="selectpicker" id="add_environment_Type" multiple data-none-selected-text="请选择" data-actions-box="true"></select>');
	$("#add_environment_Type").empty();
	$("#add_environment_Type").val('');
	environmentTypes_arr.map(function(v){
	   $("#add_environment_Type").append('<option value='+v.env+'>'+v.envName+'</option>');
   })
   var environmentTypes = row.environmentTypes || '';
   $("#add_environment_Type").val(environmentTypes.split(','));
   $('.selectpicker').selectpicker('refresh');
}