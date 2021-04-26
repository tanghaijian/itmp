var mydata = {  
	thisNode:"",
	caseTreeNode:"",
	treeSetting: {   // 树的配置信息
	    async: {
	        enable: true,
	        url: "/testManage/testCase/getCaseCatalog",
	        autoParam: ["id=systemId"],
	        dataFilter: ajaxDataFilter
	    },
	    data: {
	        simpleData: {
	            enable: true
	        }
	    },
	    edit : {
	    	isMove: false,
            enable: true,
            editNameSelectAll: true, 
            showRenameBtn: showRenameBtn,
            showRemoveBtn: showRemoveBtn
        },
	    view: { 
	    	selectedMulti: false,
             addHoverDom: addHoverDom,
             removeHoverDom: removeHoverDom,
	    },
	    callback: { 
			beforeRemove: beforeRemove,
			onClick: zTreeOnClick, 
			onRename: onRename
		}
	}
}  
$(document).ready(function () {
	getTree();
	$( ".showBtn" ).on("click",function(){
		if( $(this).children().children(".fa").hasClass("fa-angle-double-left") ){
			$(this).children().children(".fa").removeClass("fa-angle-double-left");
			$(this).children().children(".fa").addClass("fa-angle-double-right");
			$(".ztree").css("display","none")
			$("#treeSearchInput").css("display","none")
			$(".treeSearch").css("paddingLeft","0px");
			$(".treeDiv").css("width","50px");
			$("#caseList").setGridWidth($(".wode").width());
		}else{
			$(this).children().children(".fa").removeClass("fa-angle-double-right");
			$(this).children().children(".fa").addClass("fa-angle-double-left");
			$(".ztree").css("display","block")
			$("#treeSearchInput").css("display","inline-block")
			$(".treeDiv").css("width","300px");
			$(".treeSearch").css("paddingLeft","10px"); 
			$("#caseList").setGridWidth($(".wode").width());
		}
	})
});
//菜单栏
function getTree(){ 
	$.ajax({
	   url:"/projectManage/assetsLibraryRq/getSystemDirectory",
	   type:"post",
	   data:{ 
		   search: false, 
		   requireId:'',
		   systemId:'',
		   reqTaskId:'',
	   },
	   success:function(data){   
		    var newData = [];
		    data.znodes.map(function(value, index, array) { 
				let newVal = value;
				newVal.isParent = true;
				newData.push( newVal );
			})  
			$.fn.zTree.init($("#menuTree"),  mydata.treeSetting , newData );
		    fuzzySearch('menuTree', '#treeSearchInput', null, false); //初始化模糊搜索方法
	   },
	   error:function(data){
	       	 console.log( data );
	   }
	})  
}
function ajaxDataFilter(treeId, parentNode, childNodes) { 
    return childNodes.znodes;
} 
function zTreeOnClick(event, treeId, treeNode){
	$("body").css('width',$("body").width() + 1);
	setTimeout(function(){
		$("body").css('width',$("body").width() - 1);
	},500)
	if( treeNode.level >= 1 ){
		mydata.caseTreeNode = treeNode;
		$("body>.main-content>.content-btn").css('display', 'block'); 
		$("body>.main-content").css('display', 'block'); 
		$("#caseList").jqGrid('setGridParam', {
			url: "/testManage/testCase/getCaselist",
			datatype: 'json',
			contentType: "application/json; charset=utf-8",
			mtype: "post",
			width: "100%",
			postData: {
				"caseNumber": "",
				"caseName": "",
				"caseType": "",
				"systemIds": treeNode.systemId,
				"caseCatalogId":treeNode.realId,
				"archiveStatus": "",
				"uIds": $("#userId").val(),
				"filters": ""
			},
			page: 1, 
			loadComplete: function (res) {
				if(res&&treeNode){ //数量对不上时 修改
					if(treeNode.caseCount!==res.records){
						var zTree = $.fn.zTree.getZTreeObj("menuTree");
						if(zTree){
							treeNode.caseCount=res.records
							zTree.updateNode( treeNode );
							$(".ztree li a.curSelectedNode>.tree_case_num").text("案例数:"+treeNode.caseCount)
						}
					}

				}
				$("#loading").css('display', 'none');
			}
		}).trigger("reloadGrid"); //重新载入
	}else{ 
		$("body>.main-content>.content-btn").css('display', 'none'); 
		$("body>.main-content").css('display', 'block'); 
		$("#caseList").jqGrid('setGridParam', {
			url: "/testManage/testCase/getCaselist",
			datatype: 'json',
			contentType: "application/json; charset=utf-8",
			mtype: "post",
			width: "100%",
			postData: {
				"caseNumber": "",
				"caseName": "",
				"caseType": "",
				"systemId": treeNode.id,
				"caseCatalogId":"",
				"archiveStatus": "",
				"uIds": $("#userId").val(),
				"filters": ""
			},
			page: 1, 
			loadComplete: function () {
				$("#loading").css('display', 'none');
			}
		}).trigger("reloadGrid"); //重新载入
	}
}
function addHoverDom ( treeId, treeNode ){
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
	var addStr = "";
	if( treeNode.level == 0 ){
		addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' onfocus='this.blur();'></span>";
	}else{
		addStr = "<span class='tree_case_num'>案例数:"+ (treeNode.caseCount || 0) +"</span><span class='button add' id='addBtn_" + treeNode.tId
		+ "' onfocus='this.blur();'></span>";
	}  
	sObj.after(addStr);
	var btn = $("#addBtn_" + treeNode.tId);
	if (btn) btn.bind("click", function () {
		mydata.thisNode = treeNode; 
		$("#catalogName").val("")
		$("#addMenuModal").modal("show");
	});
}
  
function removeHoverDom ( treeId, treeNode ){
	$("#addBtn_" + treeNode.tId).parent().children(".tree_case_num").remove(); 
	$("#addBtn_" + treeNode.tId).unbind().remove();  
	 
}
function showRemoveBtn(treeId, treeNode) { 
	if( treeNode.level == 0 ){
		return false;
	}
	return true;
}
function showRenameBtn(treeId, treeNode) { 
	if( treeNode.level == 0 ){
		return false;
	}
	return true;
}
function addCatalogName(){
	if( $("#catalogName").val() == "" ){
		layer.alert("目录名称不能为空!", { icon: 2, title: "提示信息" });
		return;
	}
	var dataObj = {};
	let parentIdArr = [];
	if( mydata.thisNode.level == 0 ){
		dataObj.systemId = mydata.thisNode.id;
		dataObj.parentId = "";
	}else{
		dataObj.systemId = mydata.thisNode.systemId;
		dataObj.parentId = mydata.thisNode.realId;
	}  
	dataObj.catalogName = $("#catalogName").val();  
	getParentNameArrID( mydata.thisNode );
	dataObj.parentIds = parentIdArr.join(","); 
	//获取祖先节点ID   
	$.ajax({
        url: "/testManage/testCase/addOrUpdateCaseCatalog",
        type: "post",
        data: dataObj,
        success: function (data) {   
        	var newData = data;
        	var zTree = $.fn.zTree.getZTreeObj("menuTree");
        	
        	newData.tblCaseCatalog.archivedCaseCount = 0;
        	newData.tblCaseCatalog.name = data.tblCaseCatalog.catalogName; 
        	newData.tblCaseCatalog.realId = data.tblCaseCatalog.id;
        	newData.tblCaseCatalog.id = "log_"+data.tblCaseCatalog.id;
        	newData.tblCaseCatalog.pId = mydata.thisNode.id;
        	
        	zTree.addNodes(mydata.thisNode, data.tblCaseCatalog );
        	$("#addMenuModal").modal("hide");
        	layer.alert("添加成功!", {
                icon: 1,
                title: "提示信息"
            });
        },
        error: function (data) {
            $("#loading").css('display', 'none');
            layer.alert("系统内部错误!", {
                icon: 2,
                title: "提示信息"
            });
	    }
    })  
    
    function getParentNameArrID(treeNode) {   
		parentIdArr.unshift(treeNode.id);  
		if (treeNode.level <= 1) { 
			return ;
		} else {
			var parentObj = treeNode.getParentNode();
			getParentNameArrID(parentObj);
		}
	}
} 
function beforeRemove(treeId, treeNode){ 
	var flag = confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
	if( flag ){
		$.ajax({
	        url: "/testManage/testCase/addOrUpdateCaseCatalog",
	        type: "post",
	        data: {
	        	id: treeNode.realId,
	        	status: "2",
	        },
	        success: function (data) { 
	        	$("#loading").css('display', 'none');  
	        },
	        error: function (data) {
	            $("#loading").css('display', 'none');
	            layer.alert("系统内部错误!", {
	                icon: 2,
	                title: "提示信息"
	            });
		    }
	    }) 
	} 
    return flag;
}
function onRename(e, treeId, treeNode, isCancel){ 
	$.ajax({
        url: "/testManage/testCase/addOrUpdateCaseCatalog",
        type: "post",
        data: {
        	id: treeNode.realId,
        	catalogName: treeNode.name,
        },
        success: function (data) { 
        	$("#loading").css('display', 'none');  
        },
        error: function (data) {
            $("#loading").css('display', 'none');
            layer.alert("系统内部错误!", {
                icon: 2,
                title: "提示信息"
            });
	    }
    }) 
}


