/**
 * 废弃，用户管理请查看useManagement/useManagement.js
 * 
 */

$(document).ready(function() {
	searchID();
});

function searchID(){
	firstLoadPage();
	query();
}
function query(){
	var userName = $("#qry_userName").val();
	var userAccount = $("#qry_userAccount").val();
	var userStatus = $("#qry_userStatus").val();
	var data="userName="+userName+"&userAccount="+userAccount+"&userStatus="+userStatus+"&"+getPageInfo();
	data = encodeURI(data);
	var url = "/systemui/user/getAllUserByAjax";
	ajaxPost(url,data,showlist,function(msg){alert(msg.resultMsg);},token);
}
function showlist(msg){
	$("#block").show();
	$("#doing").show();
	if(msg.data != null){
		var retData = JSON.parse(msg.data);
		var list = retData.list;
		for(var i=0;i<list.length;i++){
			var bean = list[i];
			pushRowData({
				checkField:"<label><input name='checkboxID' type=\"checkbox\" class=\"ace\" /><span class=\"lbl\"></span>" +
						"<input type='hidden' name='userId' value='"+bean.id+"' />" +
						"</label>",
				seqNum:eval((retData.pageNum-1)*retData.pageSize+i+1),
				userAccount:bean.userAccount,
				userName:bean.userName,
				userStatus:toStr(bean.userStatus==1?"正常":"删除"),
				operate:"<a title=\"修改\" onclick='userUpdate(this)' class=\"green\" href=\"#\"><i class=\"icon-pencil bigger-130\"></i></a>" +
						"&nbsp;&nbsp;&nbsp;<a title=\"制作人员卡\" onclick='createUserCard(this)' class=\"green\" href=\"#\"><i class=\"icon-cogs bigger-130\"></i></a>"
				
			});
		}
		paintTable(retData.total);
		rows = [];
	}
	$("#doing").hide();
	$("#block").hide();
}
//制作人员卡
function createUserCard(obj){
	
	var trObj = $(obj).closest("tr");
	var userId = trObj.find("input[name='userId']").val();
	var roleId = trObj.find("input[name='roleIds']").val();
	if (roleId.split(",").length > 1) {
		alert("人员不能包含多个角色");
		return;
	}
	var data = JSON.stringify({userId:userId,roleId:roleId});
	toConsole("processCardMade",{data:data,url:'userManage'});
	
}
//新增
function userAdd(){
	$(".main-content").empty();
	//fc参数防止缓存
	toConsole('userAdd');
}
//修改
function userUpdate(obj){
	
	var id = $(obj).closest("tr").find("input[name='userId']").val();
	var roleIds = ","+$(obj).closest("tr").find("input[name='roleIds']").val()+",";
	$(".main-content").empty();
	$(".main-content").load('userUpdate',{"id":id,"rls":roleIds});
}


function reSetPassword(){
	var userIds = "";
	$("#dg").find("tr").each(function(){
		if($(this).find("input[name='checkboxID']").attr("checked") == "checked"){
			userIds += $(this).closest("tr").find("input[name='userId']").val()+",";
		}
	});
	if(userIds == ""){
		alert("请选择一条数据。");
		return;
	}
	userIds = userIds.substring(0, userIds.length-1);
	var data={userIds:userIds,sqlType:4};
	var url = "modifyUserInfoByAjax";
	ajaxPost(url,data,reSetPasswordAfter,function(msg){alert(msg.resultMsg);});
}

function reSetPasswordAfter(){
	alert("重置密码成功。");
}

function userDelete(){
	var userIds = "";
	$("#dg").find("tr").each(function(){
		if($(this).find("input[name='checkboxID']").attr("checked") == "checked"){
			userIds += $(this).closest("tr").find("input[name='userId']").val()+",";
		}
	});
	if(userIds == ""){
		alert("请选择一条数据。");
		return;
	}
	if (!confirm("确认删除？")) {
		return;
	}
	userIds = userIds.substring(0, userIds.length-1);
	var data={userIds:userIds,sqlType:3};
	var url = "modifyUserInfoByAjax";
	ajaxPost(url,data,userDeleteAfter,function(msg){alert(msg.resultMsg);});
}

function userDeleteAfter(){
	alert("删除成功。");
	searchID();
}

function cleanValue(){
	$(".queryRow").find("input").val("");
	$(".queryRow").find("select").val("0");
}