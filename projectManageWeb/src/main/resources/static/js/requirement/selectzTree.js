var setting = {
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
		onClick: zTreeOnClick
	}
};   

//点击节点事件
function zTreeOnClick(e, treeId, treeNode) { 
	var zTree = $.fn.zTree.getZTreeObj("projectOwn"),
	nodes = zTree.getSelectedNodes(),
	v = "";
	nodes.sort(function compare(a,b){return a.id-b.id;});
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
	}
	if (v.length > 0 ) v = v.substring(0, v.length-1);  
	$( paramList.modalSelf ).val( treeNode.businessSystemTreeName );
	$( paramList.modalSelf ).attr( "assetSystemTreeId", treeNode.id );
} 

/**
 * 获取模块信息，获取的对象是系统树中的模块层级
 * 此处url对应有bug
 * @param self
 * @returns
 */
function showMenu( self ) { 
	paramList.modalSelf = self;
	$("#projectOwn").empty();
	$.ajax({
		url:"/projectManage/systemTree/getSystemTreeByCode",
		dataType:"json",
		type:"post", 
		data:{
			"systemCode": $(self).parent().parent().prev().find( "input[type='text']" ).attr( "sysCode" ) 
		},
		success:function(data){   
			var znodes= data.data ;  
			$.fn.zTree.init($("#projectOwn"),setting,znodes);
			var zTree = $.fn.zTree.getZTreeObj("projectOwn");   
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		} 
	});
	var cityObj = $( self );
	var cityOffset = $( self ).offset();
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