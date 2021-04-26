/**
 * 废弃
 * @param $
 * @returns
 */

jQuery(function($) {
	$('.limited').inputlimiter({remText: '%n/%l',});
	$(".chosen-select").chosen({no_results_text:'无匹配'});
	initMenuData();
});


function initMenuData() {
	var data = "";
	var url="../menu/getAllMenu";
	ajaxPost(url,data,showtree,function(msg){Toast(msg.errorMessage);});
}
function showtree(msg){
	var nodeTree = {};
  	 if(msg != null){
   		 var retData = msg.data;
   		 retData = JSON.parse(retData);
   		 for(var i in retData){
   			 var node = retData[i];
   			 nodeTree[i] = node;
   			 nodeTree[i].name = node.menuButtonName;
   			 buildTree(node,nodeTree[i]);
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
   				'unselected-icon' : 'icon-remove'
   			});
   		 }
 }


function buildTree(node,nodeTree){
	 var childMenu = node.childMenu;
	 if(typeof childMenu != "undefined" && childMenu.length > 0){
		 nodeTree.type = "folder";
		 nodeTree['additionalParameters'] = {};
		 nodeTree['additionalParameters']['children'] = {};
		 for(var index in childMenu){
			 var subNode = childMenu[index];
			 var type = childMenu[index].menuButtonType;
			 subNode.name = childMenu[index].menuButtonName+(type==2?"[按钮]":"");
			 nodeTree['additionalParameters']['children'][index] = subNode;
			 buildTree(subNode, nodeTree['additionalParameters']['children'][index]);
		 }
	 }else
		 nodeTree.type = "item";
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
	
	var data = {"roleName":roleName,"status":roleStatus,"description":roleDesc};
	if(selIds.length > 0)
		data = $.extend({},data,{"menuIds":selIds.join(",")});
	var url = "../role/insertRole";
	ajaxPost(url,data,function(){Toast("新增成功");forwordPage("../role/toRoleManage")},function(msg){Toast(msg.errorMessage);});
}

