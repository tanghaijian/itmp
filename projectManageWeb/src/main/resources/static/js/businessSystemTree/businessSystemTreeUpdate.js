/**
 * Description: 业务树/系统树信息,新更改需求
 * Author:liushan
 * Date: 2019/5/14 下午 2:36
 */

var tableOpt = {
    splitTier:{},
    maxTierId:'',
    formFileList:[],
    oldTierName:'',
    tierForExport:{},
    treeNodeId:"",
    treeSplitNode:{},// 当前节点数据
    parentNode:{},// 父节点
    assetTreeTierId:"",
    currentTierNumber:""
};


$(document).ready(function() {
    leftTreeMenu();
    formValidator();
    refactorFormValidator();
    downOrUpButton();
    tableMouseover("#treeTable");
    buttonClear();

    if(jsOpt.assetTreeType == 2){
        $(".addTier").css("display","none");
        $(".editTier").css("display","none");
    }

    //上传文件
    $("#opt_uploadFile").change(function(){
        resetFileTable();
        var file = this.files[0];
        var _fileName = file.name;
        var flag = etectionFile(file,tableOpt.formFileList,_fileName,null);
        if(flag != false){
            tableOpt.formFileList.push(file);
            //列表展示
            /*var name =  JSON.stringify({fileName:_fileName}).replace(/"/g, '&quot;');*/
            var _tr = '';
            var index = _fileName .lastIndexOf("\.");
            var file_type  = _fileName .substring(index + 1, _fileName.length);
            var _td_icon = '';
            var _td_name = '<span>'+_fileName+'</span><i class="file-Id"></i></div></td>';
            /* var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="removeFile('+name+',this)">删除</a></td>';*/
            _td_icon =  classPath(file_type,_td_icon);
            _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+'</tr>';
            $("#optFileTable").append(_tr);
        }
        resetFile();
    });
});

//初始化Jqgrid,数据列表显示
function initTable() {
    $("#loading").css('display','block');
    var page = $('#treeTable').getGridParam('page');
    $("#treeTable").jqGrid("clearGridData");
    $("#treeTable").jqGrid("setGridParam",{
        page:page!= null && page != undefined?page:1,
        postData:{
            id:tableOpt.treeNodeId,
            businessSystemTreeName:htmlEncodeJQ($.trim($("#businessTreeName").val())),
            businessSystemTreeCode:htmlEncodeJQ($.trim($("#businessTreeCode").val())),
            remark:htmlEncodeJQ($.trim($("#remark").val())),
            businessSystemTreeStatus:$("#businessTreeStatus").find("option:selected").val()
        }
    });

    $("#treeTable").jqGrid({
        url:jsOpt.BusinessSystemTreeUrl+"treeList",
        datatype: 'json',
        postData:{
            id:tableOpt.treeNodeId
        },
        mtype:"POST",
        height: '100%',
        autowidth: true,
        rowNum:10,
        rowTotal: 200,
        rowList : commonTableOpt.rowList,
        rownumWidth: 40,
        pager: '#ListPager',
        sortable:true,   //是否可排序
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true, //是否要显示总记录数
        multiselect : true,
        colNames:['id','条目名称','条目简称','条目编码','上一级条目','状态','最后更新人','操作'],
        colModel:[{
            name : 'id',
            index : 'id',
            hidden:true
        },{
            name : 'businessSystemTreeName',
            index : 'businessSystemTreeName',
            sortable:false
        }, {
            name : 'businessSystemTreeShortName',
            index : 'businessSystemTreeShortName',
            sortable:false
        }, {
            name : 'businessSystemTreeCode',
            index : 'businessSystemTreeCode',
            sortable:false
        },{
            name : 'parentTreeNames',
            index : 'parentTreeNames',
            sortable:false,
            hidden:true
        },{
            name : 'businessSystemTreeStatus',
            index : 'businessSystemTreeStatus',
            sortable:false,
            width:80,
            formatter:function(value, grid, rows, state){
                var businessSystemTreeStatus = $("#businessTreeStatus").find("option");
                for (var i = 0,len = businessSystemTreeStatus.length;i < len;i++) {
                    if(value == businessSystemTreeStatus[i].value){
                        return businessSystemTreeStatus[i].innerHTML;
                    }
                }
            }
        }, {
            name : 'lastUpdateName',
            index : 'lastUpdateName',
            width:180,
            formatter:function(value, grid, rows, state){
                if(value == '' || value == null){
                    return '';
                }
                return value + '  |  ' + rows.lastUpdateDate
            }
        },{
            name:'操作',
            index:'操作',
            fixed:true,
            loadonce:true,
            sortable:false,
            resize:false,
            search: false,
            align:'center',
            width:250,
            formatter : function(value, grid, rows, state) {
                var span_ = "<span class='edit'>&nbsp;|&nbsp;</span>";
                tableOpt.currentTierNumber = rows.assetTreeTierNumber;
                var editOpt = "<a class='a_style' onclick='getTierEntity("+ rows.id +","+ 1 + ")'>编辑</a>";
                var splitOpt ='<a href="#" class="a_style splitOpt" onclick="getTierEntity('+ rows.id + ',' + 2 + ')">拆分下一级条目</a>';
                var updateStatusOpt ='<a href="#" class="a_style" onclick="getTierEntity('+ rows.id + ',' + 3 + ')">置为无效</a>';
                var opt = [];

                if(jsOpt.assetTreeType == 2 ){
                    if( tableOpt.treeNodeId != 0 && rows.businessSystemTreeStatus == 1){
                        if(editPer == true){
                            opt.push(editOpt);
                        }

                        if(tableOpt.treeNodeId != 0 && rows.assetTreeTierId != tableOpt.maxTierId && splitPer == true){
                            opt.push(splitOpt);
                        }
                        if( tableOpt.treeNodeId != 0 && rows.assetTreeTierNumber != 1 && updateStatusNoPer == true){
                            opt.push(updateStatusOpt);
                        }
                        return opt.join(span_);
                    } else if( rows.businessSystemTreeStatus == 2 ){
                        if(updateStatusPer == true){
                            var updateStatusOpt ='<a href="#" class="a_style edit" onclick="getTierEntity('+ rows.id + ',' + 4 + ')">置为有效</a>';
                            return updateStatusOpt;
                        }
                    }
                } else if( jsOpt.assetTreeType == 1 ){
                    if( rows.businessSystemTreeStatus == 1){
                        if(editPer == true){
                            opt.push(editOpt);
                        }

                        if(rows.assetTreeTierId != tableOpt.maxTierId && splitPer == true){
                            opt.push(splitOpt);
                        }

                        if(updateStatusNoPer == true){
                            opt.push(updateStatusOpt);
                        }
                        return opt.join(span_);
                    } else if( rows.businessSystemTreeStatus == 2 ){
                        if(updateStatusPer == true){
                            var updateStatusOpt ='<a href="#" class="a_style edit" onclick="getTierEntity('+ rows.id + ',' + 4 + ')">置为有效</a>';
                            return updateStatusOpt;
                        }
                    }
                }
                return '';
            }
        }
        ],
        loadComplete :function(data){
            $("#loading").css('display','none');
            tableOpt.tierForExport = $("#treeTable").jqGrid('getGridParam', "postData");
            showCol();
        },
        loadError:errorFunMsg
    }).trigger('reloadGrid');
}

function showCol(){
    if(jsOpt.assetTreeType == 2){//系统树维护
        if(tableOpt.treeNodeId == 0 || tableOpt.currentTierNumber == 1){
            $(".addTier").css("display","none");
            $(".editTier").css("display","none");
        } else {
            if(tableOpt.parentNode.assetTreeTierId == tableOpt.maxTierId){
                $(".addTier").css("display","none");
            } else {
                $(".addTier").css("display","inline-block");// 显示新建条目按钮
            }
            $(".editTier").css("display","inline-block");
        }
    } else {//业务树维护
        if(tableOpt.parentNode.assetTreeTierId == tableOpt.maxTierId){
            $(".addTier").css("display","none");
        } else {
            $(".addTier").css("display","inline-block");// 显示新建条目按钮
        }
    }
}

/**
*@author liushan
*@Description 获取实体类，更新opt参数操作，指定具体操作
*@Date 2020/7/29
*@Param 
*@return 
**/
function getTierEntity(id,opt){
    $("#loading").css('display','block');
    $.ajax({
        url:jsOpt.BusinessSystemTreeUrl+"getEntityInfo",
        method:"post",
        dataType:"json",
        data:{
            id:id
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage,{
                    icon:5,
                    title:"提示信息"
                });
            }else if (data.status == 1){
                switch (opt){
                    case 1:
                        edit(data.entityInfo); // 编辑
                        break;
                    case 2:
                        splitInsertNextTier(data.entityInfo); // 拆分下一级条目
                        break;
                    case 3:
                        updateTierStatus(data.entityInfo,2); // 置为无效
                        break;
                    case 4:
                        updateTierStatus(data.entityInfo,1); // 置为有效
                        break;
                    default:break;
                }
            }
        },
        error:errorFunMsg
    })
}

// 置为无效
function updateTierStatus(entityInfo,status){
    var message = "确定置为无效";
    if(status == 1){
        message = "确定置为有效";
    }
    layer.confirm(message,{
        btn: ['确定','取消'],
        icon: 0
    },function () {
        layer.closeAll('dialog');
        var paramData = {
            id:entityInfo.id,
            businessSystemTreeStatus:status
        };
        tableOpt.treeSplitNode = {};
        var treeObj = $.fn.zTree.getZTreeObj("tree");
        tableOpt.treeSplitNode = treeObj.getNodesByParam("id", entityInfo.id, tableOpt.parentNode)[0];
        if(tableOpt.treeSplitNode == undefined){
            tableOpt.treeSplitNode = entityInfo
        }

        updateBusinessTree(paramData);
    })

}

// 拆分下一级条目
function splitInsertNextTier(entityInfo){
    resetAddNext();
    tableOpt.splitTier = entityInfo;
    var treeObj = $.fn.zTree.getZTreeObj("tree");
    tableOpt.treeSplitNode = treeObj.getNodesByParam("id", entityInfo.id, tableOpt.parentNode)[0];
    $("#addNextModal").modal("show");
}

//拆分下级条目提交
function confirmSplitInsertNextTier(){
    $('#addNextModalForm').data('bootstrapValidator').validate();
    if(!$('#addNextModalForm').data('bootstrapValidator').isValid()){
        return false;
    }

    var parentIds = tableOpt.splitTier.parentIds;
    parentIds = parentIds == null || parentIds == ''?tableOpt.splitTier.id:parentIds+","+tableOpt.splitTier.id;
    var paramData = {
        parentId:tableOpt.splitTier.id,
        parentIds:parentIds,
        assetTreeTierId:tableOpt.splitTier.assetTreeTierId,
        businessSystemTreeName:htmlEncodeJQ($.trim($("#addNext_businessTreeName").val())),
        businessSystemTreeShortName:htmlEncodeJQ($.trim($("#addNext_businessTreeShortName").val())),
        businessSystemTreeCode:htmlEncodeJQ($.trim($("#addNext_businessTreeCode").val())),
        remark:htmlEncodeJQ($.trim($("#addNext_remark").val()))
    };
    insertBusinessTree(paramData,2);
}

//编辑条目
function edit(entityInfo){
    resetAdd();
    addTier('编辑条目');
    tableOpt.splitTier = entityInfo;
    tableOpt.oldTierName = '';
    tableOpt.oldTierName = isValueNull(entityInfo.businessSystemTreeName);

    var treeObj = $.fn.zTree.getZTreeObj("tree");
    tableOpt.treeSplitNode = treeObj.getNodesByParam("id", entityInfo.id, tableOpt.parentNode)[0];

    $("#add_businessTreeName").attr("tierId",entityInfo.id);
    $("#add_businessTreeName").val(tableOpt.oldTierName);
    $("#add_businessTreeShortName").val(isValueNull(entityInfo.businessSystemTreeShortName));
    if(jsOpt.assetTreeType == 2 &&(tableOpt.currentTierNumber == 1 || tableOpt.currentTierNumber == 2)){
        $("#add_businessTreeCode").attr("disabled",true);
    } else {
        $("#add_businessTreeCode").attr("disabled",false);
    }
    $("#add_businessTreeCode").val(isValueNull(entityInfo.businessSystemTreeCode));
    $("#add_remark").val(isValueNull(entityInfo.remark));
}

// 搜索
function searchInfo(){
    $("#loading").css('display','block');
    $("#treeTable").jqGrid('setGridParam',{
        postData : {
            businessSystemTreeName:htmlEncodeJQ($.trim($("#businessTreeName").val())),
            businessSystemTreeCode:htmlEncodeJQ($.trim($("#businessTreeCode").val())),
            remark:htmlEncodeJQ($.trim($("#remark").val())),
            businessSystemTreeStatus:$("#businessTreeStatus").find("option:selected").val()
        },
        page:1,
        loadComplete :function(){
            $("#loading").css('display','none');
            tableOpt.tierForExport = $("#treeTable").jqGrid('getGridParam', "postData");
        }
    }).trigger("reloadGrid"); //重新载入
}

/*--------------------页面按钮操作 start------------------------*/
// 新增条目 编辑条目
function addTier(titleName){
    resetAdd();
    tableOpt.treeSplitNode = tableOpt.parentNode;
    $("#myModalLabel").text(titleName);
    $("#addModal").modal("show");
}

// 新建/编辑条目提交按钮
function confirmAddOrEditTier(){
    $('#addModalForm').data('bootstrapValidator').validate();
    if(!$('#addModalForm').data('bootstrapValidator').isValid()){
        return false;
    }
    var tierId = $("#add_businessTreeName").attr("tierId");
    var tierName = htmlEncodeJQ($.trim($("#add_businessTreeName").val()));
    var paramData = {
        id:tierId,
        businessSystemTreeShortName:htmlEncodeJQ($.trim($("#add_businessTreeShortName").val())),
        businessSystemTreeCode:htmlEncodeJQ($.trim($("#add_businessTreeCode").val())),
        remark:htmlEncodeJQ($.trim($("#add_remark").val())),
        assetTreeTierId:tableOpt.assetTreeTierId
    };

    // 新增
    if(tierId == ''){
        paramData['businessSystemTreeName'] = tierName;
        if(tableOpt.parentNode.id != 0){
            var parentIds = tableOpt.parentNode.parentIds;
            parentIds = parentIds == null || parentIds == ''?tableOpt.parentNode.id:parentIds+","+tableOpt.parentNode.id;
            paramData['parentIds'] = parentIds;
            paramData['parentId'] =  tableOpt.parentNode.id;
        }
        insertBusinessTree(paramData,1)
    } else {
        // 编辑
        if(tableOpt.oldTierName != tierName){
            paramData['businessSystemTreeName'] = tierName;
            if( tableOpt.treeNodeId != 0){
                paramData["parentId"] = tableOpt.splitTier.id;
                paramData["parentIds"] = tableOpt.splitTier.parentIds;
            }
        }
        updateBusinessTree(paramData)
    }

}

// 导入条目
function importTier(){
    resetFileTable();
    $("#importModal").modal("show");
}

// 导入条目 后台操作
function importTierTree(){
    if(tableOpt.formFileList[0]  == undefined){
        return;
    } else {
        var _fileName = tableOpt.formFileList[0].name;
        var index = _fileName .lastIndexOf("\.");
        var file_type  = _fileName .substring(index + 1, _fileName.length);
        if(file_type != "xls" && file_type != "xlsx"){
            layer.alert("请选择EXCEL文件！", {
                icon: 0,
                title: "提示信息"
            });
            return
        }
    }
    var param = new FormData();

    var obj = {
        assetTreeTierId:tableOpt.assetTreeTierId,
        id:tableOpt.treeSplitNode.id
    };
    param.append("bsVo",JSON.stringify(obj));
    param.append("file",tableOpt.formFileList[0]);

    $("#loading").css('display','block');
    $.ajax({
        url:"/zuul"+jsOpt.BusinessSystemTreeUrl+'import',
        type:"post",
        data:param,
        cache: false,
        processData: false,
        contentType: false,
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                var excelRow = JSON.parse(data.excelRow); // 数据重复
                var message = '';
                if(excelRow.length > 0 ){
                    for(var i = 0,len = excelRow.length;i < len;i++){
                        message += excelRow[i];
                    }
                }

                if(message == ''){
                    layer.alert('操作成功 ！', {
                        icon: 1,
                        title: "提示信息"
                    });
                } else {
                    layer.alert(message, {
                        icon: 0,
                        title: "提示信息"
                    });
                }

                // 手动刷新导入的所有父节点
                var treeObj = $.fn.zTree.getZTreeObj("tree");
                treeObj.reAsyncChildNodes(tableOpt.treeSplitNode, "refresh");
                var nodeParentIds = JSON.parse(data.nodeParentIds);
                if(nodeParentIds != undefined && nodeParentIds.length > 0){
                    for(var i = 0,len = nodeParentIds.length; i <len;i++){
                        if(tableOpt.treeSplitNode.id != nodeParentIds[i]){
                            var node = treeObj.getNodesByParam("id", nodeParentIds[i], null)[0];
                            if(node != undefined && node.open == true){
                                treeObj.reAsyncChildNodes(node, "refresh");
                            }
                        }
                    }
                }

                $("#treeTable").jqGrid("setGridParam",{page:1});
                initTable();
                $("#importModal").modal("hide");
            }
        },
        error:errorFunMsg
    })
}

// 删除条目
function deleteTiers(){
    var treeTableIds =$('#treeTable').jqGrid('getGridParam','selarrrow');
    if(treeTableIds.length == 0){
        layer.alert("请至少选择一个条目!", {
        	icon: 0,
            title: "提示信息"
        });
        return  false;
    } else {
        layer.confirm("确认删除吗？",{
            btn: ['确定','取消'], //按钮
            title: "提示信息"
        },function(){
            layer.closeAll('dialog');
            $("#loading").css('display','block');

            $.ajax({
                url:jsOpt.BusinessSystemTreeUrl+ "remove",
                type:"post",
                traditional:true,
                data:{
                    ids:treeTableIds
                },
                success:function(data){
                    $("#loading").css('display','none');
                    if (data.status == 2){
                        layer.alert(data.errorMessage, {
                            icon: 2,
                            title: "提示信息"
                        });
                    } else if(data.status == 1){
                        layer.alert('操作成功 ！', {
                            icon: 1,
                            title: "提示信息"
                        });
                        // 删除节点以及子节点
                        var treeObj = $.fn.zTree.getZTreeObj("tree");
                        for(var i = 0,len = treeTableIds.length; i < len; i++){
                            var treeSplitNode = treeObj.getNodesByParam("id",treeTableIds[i], tableOpt.parentNode)[0];
                            treeObj.removeChildNodes(treeSplitNode);// 移除子节点
                            treeObj.removeNode(treeSplitNode);// 移除
                        }
                        initTable();
                        $(".optModal").modal("hide");
                    }
                },
                error:errorFunMsg
            })
        });
    }
}

// 导出条目
function exportOutTier(){
    tableOpt.tierForExport["type"] = 2;
    tableOpt.tierForExport["assetTreeTierId"] = tableOpt.assetTreeTierId;
    createNewForm(jsOpt.BusinessSystemTreeUrl+"export",tableOpt.tierForExport);
}

// 下载导入模板
function exportTemplet(){
    tableOpt.tierForExport["type"] = 3;
    tableOpt.tierForExport["assetTreeTierId"] = tableOpt.assetTreeTierId;
    createNewForm(jsOpt.BusinessSystemTreeUrl+"export",tableOpt.tierForExport);
}

function insertBusinessTree(paramData,type){
    $("#loading").css('display','block');
    $.ajax({
        url:jsOpt.BusinessSystemTreeUrl+ "insert",
        type:"post",
        data:paramData,
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                if(data.flag == true){
                    layer.alert('操作成功 ！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    // 手动增加节点
                    var treeObj = $.fn.zTree.getZTreeObj("tree");
                    if(tableOpt.treeSplitNode.businessSystemTrees != null){
                        var entity = JSON.parse(data.entityInfo);
                        entity["businessSystemTrees"] = [];
                        entity["parentId"] = tableOpt.treeSplitNode.id;
                        treeObj.addNodes(tableOpt.treeSplitNode,0, entity);
                    } else {
                        treeObj.reAsyncChildNodes(tableOpt.treeSplitNode, "refresh");
                    }

                    if(type==1){
                        $("#treeTable").jqGrid("setGridParam",{page:1});
                        initTable();
                    }

                    $(".optModal").modal("hide");
                } else if(data.flag == false){
                    layer.alert("该条目名称已存在，请修改名称", {
                        icon: 0,
                        title: "提示信息"
                    });
                }

            }
        },
        error:errorFunMsg
    })
}

//更新业务树
function updateBusinessTree(paramData){
    $("#loading").css('display','block');
    $.ajax({
        url:jsOpt.BusinessSystemTreeUrl+ "update",
        type:"post",
        data:paramData,
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                if(data.flag == true){
                    layer.alert('操作成功 ！', {
                        icon: 1,
                        title: "提示信息"
                    });

                    var treeObj = $.fn.zTree.getZTreeObj("tree");
                    if(tableOpt.treeSplitNode != undefined){
                        if(paramData.businessSystemTreeName != undefined){ // 树修改名称
                            tableOpt.treeSplitNode["businessSystemTreeName"] = paramData.businessSystemTreeName;
                            treeObj.updateNode( tableOpt.treeSplitNode);
                        } else if( paramData.businessSystemTreeStatus != undefined){// 移除或添加
                        	if(tableOpt.parentNode.open == true){
                        	    if (paramData.businessSystemTreeStatus == 2) {// 置为无效
                        	        treeObj.removeChildNodes(tableOpt.treeSplitNode);// 移除子节点
                        	        treeObj.removeNode(tableOpt.treeSplitNode);// 移除
                        	    } else if (paramData.businessSystemTreeStatus == 1) {// 置为有效
                        	        treeObj.addNodes(tableOpt.parentNode, 0, tableOpt.treeSplitNode);
                        	    }
                        	}
                        }
                    }
                    initTable();
                    $(".optModal").modal("hide");
                } else if(data.flag == false){
                    layer.alert("该条目名称已存在，请修改名称", {
                        icon: 0,
                        title: "提示信息"
                    });
                }
            }
        },
        error:errorFunMsg
    })
}

//新建条目，清空弹窗内容
function resetAdd(){
    $("#myModalLabel").empty();
    $("#add_businessTreeName").val("");
    $("#add_businessTreeName").attr("tierId",'');
    $("#add_businessTreeShortName").val("");
    $("#add_businessTreeCode").val("");
    $("#add_remark").val("");
    tableOpt.splitTier = {};
    tableOpt.oldTierName = '';
    tableOpt.oldTierName = '';
    tableOpt.treeSplitNode = {};
    $("#addModal .btn_clear").css("display","none");

}

//新增下一级条目，清空弹窗内容
function resetAddNext(){
    tableOpt.splitTier = {};
    tableOpt.treeSplitNode = {};
    $("#addNext_businessTreeName").val("");
    $("#addNext_businessTreeShortName").val("");
    $("#addNext_businessTreeCode").val("");
    $("#addNext_remark").val("");
    $("#addNextModal .btn_clear").css("display","none");
}

/*--------------------页面按钮操作 end------------------------*/

function resetSelect(){
    $("#businessTreeName").val("");
    $("#businessTreeCode").val("");
    $("#remark").val("");
    $("#businessTreeStatus").selectpicker("val","");
    $(".color1 .btn_clear").css("display","none");
}

function resetFileTable(){
    tableOpt.formFileList = [];
    $("#optFileTable tbody").html("");
}