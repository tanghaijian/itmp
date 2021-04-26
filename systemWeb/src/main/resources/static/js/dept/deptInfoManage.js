
/**
 * 部门管理js（废弃）
 * @returns
 */

$(function(){
	$(document).click(function(event){
		var id = $(event.target).attr('id');
		if(id != 'companyName'&&$(event.target).closest("#companyTree").length == 0)
			$("#companyTree").remove();
     		event.preventDefault();
	});
	$('.limited').inputlimiter({remText: '%n/%l',});
	$(".chosen-select").chosen({no_results_text:'无匹配'});
	clearDept();
	initDeptTree();
	$("#companyName").unbind('click').bind('click',function(){
		if(!$(this).is(":disabled"))
			initCompanyTree();
		});
	
});


function initCompanyTree(){
	
	var companyTreeObj = $("#companyTree");
	if(companyTreeObj.length == 0)
		$("<div id='companyTree' class='tree'></div>").insertAfter($("#companyName"));
	var nodeTree = {};
	var url = "../company/getAllCompany";
	ajaxPost(url,{},function(msg){
	 if(msg != null){
	   		 var retData = msg.data;
	   		 retData = JSON.parse(retData);
	   		 for(var i in retData){
	   			 var node = retData[i];
	   			 nodeTree[i] = node;
	   			 nodeTree[i].name = node.companyName;
	   			 buildCompanyTree(node,nodeTree[i]);
	   			 }
	   		 var treeDataSource = new DataSourceTree({data: nodeTree});
	   			$('#companyTree').ace_tree({
	   				dataSource: treeDataSource ,
	   				multiSelect:false,
	   				loadingHTML:'<div class="tree-loading"><i class="icon-refresh icon-spin blue"></i></div>',
	   				'open-icon' : 'icon-minus',
	   				'close-icon' : 'icon-plus',
	   				'selectable' : true,
	   				'selected-icon' : 'icon-ok',
	   				'unselected-icon' : 'icon-remove',
	   				'expandAll':true,
	   			});
	   			
	   			$("#companyTree").unbind("selected").bind("selected",function(event,data){
	   				var companyId = data.info[0].id;
	   				var companyName = data.info[0].name;
	   				$("#companyId").val(companyId);
	   				$("#companyName").val(companyName);
	   			});
	   			
	   			$("#companyTree").unbind("opened").bind("opened",function(event,data){
	   				$("#companyTree div.tree-selected").removeClass("tree-selected");
	   				$("#companyTree.icon-ok").removeClass("icon-ok").addClass("icon-remove");
	   				var companyId = data.id;
	   				var companyName = data.name;
	   				$("#companyId").val(companyId);
	   				$("#companyName").val(companyName);
	   			});
	   			
	   			$("#companyTree").unbind("closed").bind("closed",function(event,data){
	   				$("#companyTree div.tree-selected").removeClass("tree-selected");
	   				$("#companyTree.icon-ok").removeClass("icon-ok").addClass("icon-remove");
	   				var companyId = data.id;
	   				var companyName = data.name;
	   				$("#companyId").val(companyId);
	   				$("#companyName").val(companyName);
	   			});
	   	 	}
	    });
	
}



function initDeptTree(){
	
	var nodeTree = {};
    var url = "../dept/getAllDept";
    ajaxPost(url,{},function(msg){
   	 if(msg != null){
   		 var retData = msg.data;
   		 retData = JSON.parse(retData);
   		 for(var i in retData){
   			 var node = retData[i];
   			 nodeTree[i] = node;
   			 nodeTree[i].name = node.deptName+"["+node.companyName+"]";
   			 buildTree(node,nodeTree[i]);
   			 }
   		 var treeDataSource = new DataSourceTree({data: nodeTree});
   			$('#tree').ace_tree({
   				dataSource: treeDataSource ,
   				multiSelect:false,
   				loadingHTML:'<div class="tree-loading"><i class="icon-refresh icon-spin blue"></i></div>',
   				'open-icon' : 'icon-minus',
   				'close-icon' : 'icon-plus',
   				'selectable' : true,
   				'selected-icon' : 'icon-ok',
   				'unselected-icon' : 'icon-remove'
   			});
   			
   			$("#tree").unbind("selected").bind("selected",function(event,data){
   				var deptId = data.info[0].id;
   				var deptName = data.info[0].name;
   				$("#currentSelect").empty();
   				$("#currentSelect").append('<i class="icon-double-angle-right" id="currentSelect"></i>当前选中：'+deptName);
   				$("#selectId").val(deptId);
   				showDeptDetail(deptId);
   			});
   			
   			$("#tree").unbind("opened").bind("opened",function(event,data){
   				$("#tree div.tree-selected").removeClass("tree-selected");
   				$("#tree.icon-ok").removeClass("icon-ok").addClass("icon-remove");
   				clearDept();
   				var deptId = data.id;
   				var deptName = data.name;
   				$("#currentSelect").empty();
   				$("#currentSelect").append('<i class="icon-double-angle-right" id="currentSelect"></i>当前选中：'+deptName);
   				$("#selectId").val(deptId);
   				showDeptDetail(deptId);
   			});
   			
   			$("#tree").unbind("closed").bind("closed",function(event,data){
   				$("#tree div.tree-selected").removeClass("tree-selected");
   				$("#tree.icon-ok").removeClass("icon-ok").addClass("icon-remove");
   				clearDept();
   				var deptId = data.id;
   				var deptName = data.name;
   				$("#currentSelect").empty();
   				$("#currentSelect").append('<i class="icon-double-angle-right" id="currentSelect"></i>当前选中：'+deptName);
   				$("#selectId").val(deptId);
   				showDeptDetail(deptId);
   			});
   		 }
   	 });
	
}	



function buildCompanyTree(node,nodeTree){
	 var childCompany = node.childCompany;
	 var companyId = $("#companyId").val();
	 var flag = $("#flag").val();
	 if(typeof childCompany != "undefined" && childCompany.length > 0){
		 nodeTree.type = "folder";
		 nodeTree['additionalParameters'] = {};
		 nodeTree['additionalParameters']['children'] = {};
		 for(var index in childCompany){
			 var subNode = childCompany[index];
			 subNode.name = childCompany[index].companyName;
			 nodeTree['additionalParameters']['children'][index] = subNode;
			 buildCompanyTree(subNode, nodeTree['additionalParameters']['children'][index]);
		 }
	 }else{
		 nodeTree.type = "item";
		 if(companyId == node.id && flag != 0){
			 nodeTree['additionalParameters'] = {};
			 nodeTree['additionalParameters']['item-selected'] = true;
		 }
		 
	 }
}


function buildTree(node,nodeTree){
	 var childDept = node.childDept;
	 if(typeof childDept != "undefined" && childDept.length > 0){
		 nodeTree.type = "folder";
		 nodeTree['additionalParameters'] = {};
		 nodeTree['additionalParameters']['children'] = {};
		 for(var index in childDept){
			 var subNode = childDept[index];
			 subNode.name = subNode.deptName;
			 nodeTree['additionalParameters']['children'][index] = subNode;
			 buildTree(subNode, nodeTree['additionalParameters']['children'][index]);
		 }
	 }else
		 nodeTree.type = "item";
}

function clearDept(){
	$("#deptName").val("");
	$("#deptDesc").val("");
	$("#companyId").val("");
	$("#companyName").val("");
	$("#companyTree div.tree-selected").removeClass("tree-selected");
	$("#companyTree.icon-ok").removeClass("icon-ok").addClass("icon-remove");
	$("#deptStatus").val(1);
}


function showDeptDetail(id){
	
	clearDept();
	var flag = $("#flag").val();
	if(flag == 0)
		return false;
	if(flag == 1){
		$("#companyTree").remove();
		$("#companyName").attr("disabled",true);
	}
	$("#companyName").attr("disabled",false);
	
	
	var url = "../dept/selectDeptById";
	var data = {"id":id};
	ajaxPost(url,data,function(msg){
		if(msg != null){
			var dept = JSON.parse(msg.data);
			$("#deptName").val(dept.deptName);
			$("#companyId").val(dept.companyId);
			$("#deptDesc").val(dept.description);
			$("#deptStatus").val(dept.status);
			$("#companyName").val(dept.companyName);
			var parentDeptId = dept.parentDeptId;
			if(typeof parentDeptId != "undefined" && flag == 2){
				$("#companyTree").remove();
				$("#companyName").attr("disabled",true);
			}
			$(".chosen-select").trigger("chosen:updated");
			
		}
	},function(msg){
		Toast(msg.errorMessage);
	});
}


function addNew(obj){
	clearDept();
	$(".btn-active").removeClass("btn-active");
	$(obj).addClass("btn-active");
	$("#flag").val(0);
	$("#companyName").attr("disabled",false);
}

function saveNew(){
	var url = "../dept/insertDept";
	var data = getData();
	if(data == false)
		return false;
	ajaxPost(url,data,function(msg){Toast("添加新部门成功");forwordPage("../dept/toDeptManage");},function(msg){Toast(msg.errorMessage);});
}

function addChild(obj){
	$("#companyTree").remove();
	$(".btn-active").removeClass("btn-active");
	$(obj).addClass("btn-active");
	$("#flag").val(1);
	$("#companyName").attr("disabled",true);
	var parentId = $("#selectId").val();
	if(typeof parentId != "undefined" && parentId.length > 0)
		showDeptDetail(parentId);
}
function saveChild(){
	var parentId = $("#selectId").val();
	if(typeof parentId == "undefined"||parentId.length == 0){
		$.gritter.add({
			title: '非空校验提示',
			text:  '未选择上一级部门，请从左边选取',
			class_name: 'gritter-error gritter-light'
		});
		
	  return false;
	}
	var data = getData();
	if(data == false)
		return false;
	data = $.extend({},data,{"parentDeptId":parentId});
	var url="../dept/insertDept";
	ajaxPost(url,data,function(msg){Toast("添加子部门成功");forwordPage("../dept/toDeptManage");},function(msg){Toast(msg.errorMessage);});
}

function edit(obj){
	$(".btn-active").removeClass("btn-active");
	$(obj).addClass("btn-active");
	$("#flag").val(2);
	var id = $("#selectId").val();
	if(typeof id != "undefined" && id.length > 0)
		showDeptDetail(id);
}
function saveEdit(){

	var id = $("#selectId").val();
	if(typeof id == "undefined"||id.length == 0){
		$.gritter.add({
			title: '非空校验提示',
			text:  '未获取到部门ID，请从左边选择后重新提交',
			class_name: 'gritter-error gritter-light'
		});
		
	  return false;
	}
	var data = getData();
	if(data == false)
		return false;
	data = $.extend({},data,{"id":id});
	var url="../dept/updateDept";
	ajaxPost(url,data,function(msg){Toast("修改部门成功");forwordPage("../dept/toDeptManage");},function(msg){Toast(msg.errorMessage);});

}



function save(){
	
	//0,新增，1，新增子，2。修改
	var flag = $("#flag").val();
	
	if(flag == 0)
		saveNew();
	else
		if(flag == 1)
			saveChild();
		else
			saveEdit();
}
function getData(){
	var deptName = $("#deptName").val();
	if(typeof deptName == "undefined"||deptName.length == 0){
		$.gritter.add({
			title: '非空校验提示',
			text:  '请填写部门名称',
			class_name: 'gritter-error gritter-light'
		});
		
	  return false;
	}
	
	var companyId = $("#companyId").val();
	if(typeof companyId == "undefined" || companyId == ""){
		$.gritter.add({
			title: '非空校验提示',
			text:  '请选择部门所属公司',
			class_name: 'gritter-error gritter-light'
		});
		
	  return false;
	}
	var deptDesc = $("#deptDesc").val();
	var deptStatus = $("#deptStatus").val();
	var data = {"deptName":deptName,"companyId":companyId,"description":deptDesc,"status":deptStatus};
	return data;
}