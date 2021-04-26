/**
 * 废弃
 * @returns
 */
$(document).ready(function(){
	queryRole(0);
	$("#queryBtn").unbind().bind("click", function() {		
		queryRole(1);		
	});
});

//查询角色，type=0:查询全部，type=1按查询条件查询
function queryRole(type) {
	//权限名称
	var roleName = $("#roleName").val();	
	//权限编码
	var roleCode = $("#roleCode").val().toUpperCase();	
	
	var roleJson = JSON.stringify({'roleName':roleName, 'roleCode':roleCode});
	var queryParam = {'roleJson':roleJson};
	if (type == 0) {		
		$("#page").pageInit({url:'../role/getAllRole',callback:function(data,pageIndex,pageSize){initDg(data,pageIndex,pageSize);}});
	} else {
		$("#page").pageInit({url:'../role/getAllRole','queryParam':queryParam ,callback:function(data,pageIndex,pageSize){initDg(data,pageIndex,pageSize);}});
	}
}

function initDg(data,pageIndex,pageSize){
	$("#dg").empty();
	var content = "";
	for(var i = 0 ;i < data.length;i++){
		var role = data[i];
		content += "<tr>";
		content += "<td><label><input name='checkboxID' type='checkbox' class='ace' value='"+role.id+"'/><span class='lbl'></span></label></td>";
		content += "<td>"+eval((pageIndex-1)*pageSize+i+1)+"</td>";
		content += "<td>"+role.roleName+"</td>";
		content += "<td>"+role.roleCode+"</td>";
		content += "<td>"+(role.status == "1"?"正常":"删除")+"</td>";
		
		content += "<td><a title=\"修改\" onclick='roleUpdate(\""+role.id+"\")' class=\"green\" href=\"#\"><i class=\"icon-pencil bigger-130\"></i></a></td>";
		content += "</tr>";
	}
	$("#dg").append(content);
}


function roleAdd(){
	forwordPage("../role/toRoleAdd");
}


function roleUpdate(id){
	forwordPage("../role/toRoleUpdate",{"roleId":id});
}

function selectAll(){
	$("input[name='checkboxID']").prop("checked",$("#all").prop("checked"));
}

function roleDelete(){
	var roleIds = [];
	
	$("input[name='checkboxID']:checked").each(function(){
		var roleId = $(this).val();
		roleIds.push(roleId);
	});
	
	
	if(roleIds.length == 0){
		$.gritter.add({
			title: '非空校验提示',
			text:  '请选择至少一条数据',
			class_name: 'gritter-error gritter-light'
		});
		
	  return false;
	}
	
	var url = "../role/delRole";
	var data = {"roleIds":roleIds.join(",")};
	$( "#dialog-confirm" ).removeClass('hide').dialog({
		resizable: false,
		modal: true,
		title: "删除角色",
		buttons: [
			{
				html: "<i class='icon-trash bigger-110'></i>&nbsp;确定",
				"class" : "btn btn-danger btn-xs",
				 click: function() {
					var $dialog = $(this);
					ajaxPost(url,data,function(){Toast("删除成功"),$dialog.dialog( "close" );$("#page").query();},function(msg){Toast(msg.errorMessage);});
					
				}
			}
			,
			{
				html: "<i class='icon-remove bigger-110'></i>&nbsp; 取消",
				"class" : "btn btn-xs",
				click: function() {
					$( this ).dialog( "close" );
				}
			}
		]
	});
		
}

function cleanValue() {
	$("input").val("");
}