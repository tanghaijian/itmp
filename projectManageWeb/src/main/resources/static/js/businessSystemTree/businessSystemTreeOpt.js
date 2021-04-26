/**
*@author liushan
*@Description  业务/系统左侧树的操作
 * 具体操作参照ztree官方API
*@Date 2019/9/3
*@Param 
*@return 
**/

var treeOpt = {
    zTreeObj:'',
    treeNodeId:'',
    treeNode:'',
    perTime:100
};

var setting = {
    async: {
        enable: true,
        url: getUrl
    },
    data: {
        keep: {
            parent: false
        },
        key: {
            name: "businessSystemTreeName",
            children:"businessSystemTrees",
            isParent:"parentNode"
        },
        simpleData: {
            enable: true,
            idKey:"id",
            pIdKey:"parentId",
            rootPId:null
        }
    },
    view: {
        nameIsHTML:true,
        selectedMulti: true,  //允许同时选中多个节点
        showLine: true,       //显示节点之间的连线
        showIcon: true,
        icon:1
    },
    callback: {
        beforeExpand: beforeExpand,
        onAsyncSuccess: onAsyncSuccess,
        onAsyncError: onAsyncError,
        onClick: onClickInitTable
    }
};

// 菜单树
function leftTreeMenu(){
    $("#loading").css('display','block');
    $.ajax({
        url:jsOpt.BusinessSystemTreeUrl+"getBusinessTreeList",
        type:"post",
        dataType: "json",
        data:{
            nodeId:""
        },
        success : function(data) {
            $("#loading").css('display','none');
            treeOpt.zTreeObj = $.fn.zTree.init($("#tree"), setting, data);
            var nodes = treeOpt.zTreeObj.getNodes()[0];
            if(treeOpt.zTreeObj != null) treeOpt.zTreeObj.expandNode(nodes,true);
            tableOpt.maxTierId = data[0].businessSystemTreeId;
            tableOpt.assetTreeTierId = data[0].assetTreeTierId;
            onClickInitTable(event, "0",nodes);
        },
        error:errorFunMsg
    });
}

// 树列表点击操作
function onClickInitTable(event, treeId, treeNode){
    resetSelect();
    tableOpt.treeNodeId = "";
    tableOpt.treeSplitNode = {};
    tableOpt.parentNode = {};
    tableOpt.assetTreeTierId = "";
    tableOpt.currentTierNumber = "";
    
    tableOpt.treeNodeId = treeNode.id;
    tableOpt.treeSplitNode = treeNode;
    tableOpt.parentNode = treeNode;
    tableOpt.assetTreeTierId = treeNode.assetTreeTierId;
    initTable();
}

/**
*@author liushan
*@Description 返回树访问后台地址
*@Date 2020/7/29
*@Param treeId 树节点id,树信息
*@return
**/
function getUrl(treeId, treeNode) {
    return jsOpt.BusinessSystemTreeUrl+"getBusinessTreeList?nodeId=" + treeNode.id;
}

/**
*@author liushan
*@Description
*@Date 2020/7/29
*@Param
*@return
**/
function beforeExpand(treeId, treeNode) {
    if (!treeNode.isAjaxing) {
        ajaxGetNodes(treeNode, "refresh");
        return true;
    } else {
        layer.alert("正在下载数据中，请稍后展开节点。。。", {
            icon: 0,
            title: "提示信息"
        });
        return false;
    }
}

function ajaxGetNodes(treeNode, reloadType) {
    var zTree = $.fn.zTree.getZTreeObj("tree");
    if (reloadType == "refresh") {
        treeNode.icon = "../../projectManageui/static/images/loading1.gif";
        zTree.updateNode(treeNode);
    }
    zTree.reAsyncChildNodes(treeNode, reloadType, true);
}

/**
*@author liushan
*@Description 加载成功
*@Date 2020/8/12
 * @param null
*@return 
**/
function onAsyncSuccess(event, treeId, treeNode, msg) {
    if (!msg || msg.length == 0) {
        return;
    }
    var zTree = $.fn.zTree.getZTreeObj("tree"),
        totalCount = treeNode.count;
    if (treeNode.businessSystemTrees.length < totalCount) {
        setTimeout(function() {ajaxGetNodes(treeNode);}, treeOpt.perTime);
    } else {
        treeNode.icon = "";
        zTree.updateNode(treeNode);
        zTree.selectNode(treeNode.businessSystemTrees[0]);
    }
}

/**
*@author liushan
*@Description 加载失败
*@Date 2020/8/12
 * @param null
*@return 
**/
function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
    if (XMLHttpRequest.status != 999) {
        layer.alert("异步获取数据出现异常！", {
            icon: 2,
            title: "提示信息"
        });
        var zTree = $.fn.zTree.getZTreeObj("tree");
        treeNode.icon = "";
        zTree.updateNode(treeNode);
    }
}