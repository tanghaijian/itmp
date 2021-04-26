var setting = {
	data:{//表示tree的数据格式
		simpleData:{
			enable:true,//表示使用简单数据模式
			idKey:"id",//设置之后id为在简单数据模式中的父子节点关联的桥梁
			pidKey:"pId",//设置之后pid为在简单数据模式中的父子节点关联的桥梁和id互相对应
			rootPId:"0"//pid为null的表示根节点
		}
	},
	view:{//表示tree的显示状态
		selectedMulti :false//表示禁止多选
	},  
	callback: {
		onClick: zTreeOnClick
	}
}; 
var settingBatch = {
	data:{//表示tree的数据格式
		simpleData:{
			enable:true,//表示使用简单数据模式
			idKey:"id",//设置之后id为在简单数据模式中的父子节点关联的桥梁
			pidKey:"pId",//设置之后pid为在简单数据模式中的父子节点关联的桥梁和id互相对应
			rootPId:"0"//pid为null的表示根节点
		}
	},
	view:{//表示tree的显示状态
		selectedMulti :false//表示禁止多选
	},
	callback: {
		onClick: zTreeOnClickBatch
	}
}; 
var modalSetting = {
	data:{//表示tree的数据格式
		key: {
            name: "businessSystemTreeName", 
        },
        simpleData: {
            enable: true,
            idKey:"id",
            pIdKey:"parentId",
            rootPId:null
        }
	},
	view:{//表示tree的显示状态
		selectedMulti :false//表示禁止多选
	},  
	callback: {
		onClick: modalzTreeOnClick
	}
}; 
function zTreeOnClickBatch(e, treeId, treeNode) {

	var zTree = $.fn.zTree.getZTreeObj("batchProject"),
		nodes = zTree.getSelectedNodes(),
		v = "";
	nodes.sort(function compare(a,b){return a.id-b.id;});
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
	}
	if (v.length > 0 ) v = v.substring(0, v.length-1);
	$("#batchGroup").val( treeNode.name );
} 
function zTreeOnClick(e, treeId, treeNode) { 
	var zTree = $.fn.zTree.getZTreeObj("projectOwn"),
	nodes = zTree.getSelectedNodes(),
	v = "";
	nodes.sort(function compare(a,b){return a.id-b.id;});
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
	}
	if (v.length > 0 ) v = v.substring(0, v.length-1); 
	$("#projectInput").val( treeNode.name ).change();
}
function modalzTreeOnClick(e, treeId, treeNode){
	var zTree = $.fn.zTree.getZTreeObj("modalOwn"),
	nodes = zTree.getSelectedNodes(),
	v = "";
	nodes.sort(function compare(a,b){return a.id-b.id;});
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
	}
	if (v.length > 0 ) v = v.substring(0, v.length-1); 
	var name=getAllParentName(treeNode);
	$("#modalInput").val( name).change();
}
function getAllParentName(treeNode){
	if (treeNode == null) {
		return "";
	}
    var filename = treeNode.businessSystemTreeName;
    var pNode = treeNode.getParentNode();
    if (pNode != null) {
        filename = getAllParentName(pNode) + "/" + filename;
    }
    return filename;

}
function showMenu() {
	var cityObj = $("#projectInput");
	var cityOffset = $("#projectInput").offset();
	$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown);
}
function showTree() {
	var cityObj = $("#modalInput");
	var cityOffset = $("#modalInput").offset();
	$("#modalContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	$("body").bind("mousedown", onBodyModalDown); 
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function hideModal() {
	$("#modalContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyModalDown );
}
function hideMenuBatch() {
	$("#batchMenuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDownBatch);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
}
function onBodyModalDown( event ){
	if (!(event.target.id == "menuBtn" || event.target.id == "modalContent" || $(event.target).parents("#modalContent").length>0)) {
		hideModal();
	}
}
function onBodyDownBatch(event) {

	if (!(event.target.id == "menuBtn" || event.target.id == "batchMenuContent" || $(event.target).parents("#batchMenuContent").length>0)) {
		hideMenuBatch();
	}
}