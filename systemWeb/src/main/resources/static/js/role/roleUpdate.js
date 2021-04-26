/**
 * 废弃
 * @param $
 * @returns
 */

jQuery(function($) {
	$('.limited').inputlimiter({remText: '%n/%l',});
	$(".chosen-select").chosen({no_results_text:'无匹配'});
	getRole();
});


function getRole(){
	
	var roleId = $("#roleId").val();
	if(typeof roleId == "undefined" || roleId == ""){
		$.gritter.add({
			title: '非空校验提示',
			text:  '未获取到角色ID',
			class_name: 'gritter-error gritter-light'
		});
		
	  return false;
	}
	var url="../role/findRoleById";
	var data = {"roleId":roleId};
	ajaxPost(url,data,function(msg){
		var retData = JSON.parse(msg.data);
	    var roleName = retData.roleName;
	    var status = retData.status;
	    var roleDesc = retData.description;
	    $("#roleName").val(roleName);
	    $("#status").val(status);
	    $("#roleDesc").val(roleDesc);
	    
	    var menuIds = retData.menuIds;
	    initMenuData(menuIds);
		
	},function(msg){Toast(msg.errorMessage);});
}

function initMenuData(menuIds) {
	var data = "";
	var url="../menu/getAllMenu";
	ajaxPost(url,data,function(msg){
			showlist(msg,menuIds);
	},function(msg){Toast(msg.errorMessage);});
}
function showlist(msg,menuIds){
	var nodeTree = {};
  	 if(msg != null){
   		 var retData = msg.data;
   		 retData = JSON.parse(retData);
   		 for(var i in retData){
   			 var node = retData[i];
   			 nodeTree[i] = node;
   			 nodeTree[i].name = node.menuButtonName;
   			 buildTree(node,nodeTree[i],menuIds);
   			 }
   		 var treeDataSource = new DataSourceTree({data: nodeTree});
   			$('#menuTree').ace_tree({
   				dataSource: treeDataSource ,
   				multiSelect:false,
   				loadingHTML:'<div class="tree-loading"><i class="icon-refresh icon-spin blue"></i></div>',
   				'open-icon' : 'icon-minus',
   				'close-icon' : 'icon-plus',
   				'selectable' : true,
   				'multiSelect':true,
   				'selected-icon' : 'icon-ok',
   				'unselected-icon' : 'icon-remove',
   				'expandAll':true	
   			});
   		 }
 }


function buildTree(node,nodeTree,menuIds){
	 var childMenu = node.childMenu;
	 var childMenuId = node.id;
	 if(typeof childMenu != "undefined" && childMenu.length > 0){
		 nodeTree.type = "folder";
		 nodeTree['additionalParameters'] = {};
		 nodeTree['additionalParameters']['children'] = {};
		 for(var index in childMenu){
			 var subNode = childMenu[index];
			 var type = childMenu[index].menuButtonType;
			 subNode.name = childMenu[index].menuButtonName+(type==2?"[按钮]":"");
			 nodeTree['additionalParameters']['children'][index] = subNode;
			 buildTree(subNode, nodeTree['additionalParameters']['children'][index],menuIds);
		 }
	 }else{
		 nodeTree.type = "item";
		 if(typeof menuIds != "undefined" && menuIds.length > 0){
			 var ids = menuIds.split(",");
		     for(var i = 0 ;i < ids.length;i++){
		    	 if(ids[i] == childMenuId){
		    		 nodeTree['additionalParameters'] = {};
					 nodeTree['additionalParameters']['item-selected'] = true;
					 break;
		    	 }
		     }
	   }
	 }
}


function save(){
	var roleName = $("#roleName").val();
	if(roleName == ""){
		$.gritter.add({
			title: '非空校验提示',
			text:  '请填写角色名称',
			class_name: 'gritter-error gritter-light'
		});
		
	  return false;
	}
	
	var roleStatus = $("#roleStatus").val();
	var roleDesc = $("#roleDesc").val();
	var selIds = [];
	var selectMenus = $("#menuTree").tree('selectedItemsAndParents');
	if(typeof selectMenus != "undefined" && selectMenus.length > 0){
		for(var i = 0 ;i < selectMenus.length;i++){
			var selectMenuId = selectMenus[i].id;
			selIds.push(selectMenuId);
		}
	}
	
	var roleId = $("#roleId").val();
	var data = {"id":roleId,"roleName":roleName,"status":roleStatus,"description":roleDesc};
	if(selIds.length > 0)
		data = $.extend({},data,{"menuIds":selIds.join(",")});
	var url = "../role/updateRole";
	ajaxPost(url,data,function(){Toast("修改成功");forwordPage("../role/toRoleManage")},function(msg){Toast(msg.errorMessage);});
}

