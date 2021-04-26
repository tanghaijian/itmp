var setting = {
	data:{//表示tree的数据格式
		simpleData:{
			enable:true,//表示使用简单数据模式
			idKey:"id",//设置之后id为在简单数据模式中的父子节点关联的桥梁
			pidKey:"pId",//设置之后pid为在简单数据模式中的父子节点关联的桥梁和id互相对应
			rootId:"0"//pid为null的表示根节点
		}
	},

	check: {
		enable: true,

		chkStyle : "checkbox",

		chkboxType: { "Y": "", "N": "" }
	},
	view:{//表示tree的显示状态
		selectedMulti :true
	},  
	callback: {
		onCheck: zTreeOnClick
	}
};

function zTreeOnClick(e, treeId, treeNode) { 
	var zTree = $.fn.zTree.getZTreeObj("projectOwn"),
		nodes=zTree.getCheckedNodes(true);
	//nodes = zTree.getSelectedNodes();

	v = "";
	//nodes.sort(function compare(a,b){return a.id-b.id;});
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
	}

	if (v.length > 0 ) v = v.substring(0, v.length-1);

	$("#cheakProjectGroup").val( v );
}

function showMenu() {
	var cityObj = $("#cheakProjectGroup");
	var cityOffset = $("#cheakProjectGroup").offset();
	$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
} 