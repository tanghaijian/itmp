/**
 * Description: 业务树/系统树信息
 * Author:liushan
 * Date: 2019/5/14 下午 2:36
 * assetTreeType 1 表示业务树 2 表示系统树  都在各自的vm定义
 */

var jsOpt = {
    assetTreeUrl:"/projectManage/assetTree/",
    BusinessTreeUrl:"/projectManage/business/",
    BusinessTreeWebUrl:"/projectManageui/business/",
    errorMessage:"系统内部错误，请联系管理员 ！！！",
    successMessage:"操作成功",
    noPermission:"没有操作权限 ！！！"
};

var tableOpt = {
    newrowid:'' // 创建一个新的节点id
};

$(document).ready(function() {
    initTable(assetTreeType);
    tableMouseover("#AssetBusinessTreeTable");
    $("#loading").css('display','block');
});

// 层级列表 assetTreeType 区分层级是业务还是子系统层级
function initTable(assetTreeType) {
    $("#loading").css('display','block');
    $("#AssetBusinessTreeTable").jqGrid("clearGridData");
    $("#AssetBusinessTreeTable").jqGrid({
        url:jsOpt.assetTreeUrl+"assetTreeList",
        datatype: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        height: 'auto',
        mtype : "POST",
        postData:{
            assetTreeType:assetTreeType
        },
        cellEdit:true,
        width: $(".box-body").width()*1,
        colNames:['id','层级排序','层级名称','创建人','更新人','操作'],
        colModel:[{
            name : 'id',
            hidden:true,
            key:true,
            formatter : function(value, grid, rows, state) {
                return '<input type="hidden" class="idInfo" value="'+rows.id+'" />';
            }
        }, {
            name : 'tierNumber',
            width:50,
            formatter : function(value, grid, rows, state) {
                return '第<span type="text" class="tierNumberInfo">'+ value +'</span>层' ;
            }
        }, {
            name : 'assetTreeName',
            width:100,
            formatter:function(value, grid, rows, state){
                return '<div class="table_Td"><div class="fontContent">'+ value +'</div><input class="inputContent form-control assetTreeNameInfo" placeholder="请输入" type="text"  value="'+ value +'"/></div>';
            }
        }, {
            name : 'createName',
            width:80,
            formatter:function(value, grid, rows, state){
                if(value == ''){
                    return '';
                }
                return value + '  |  ' + rows.createDate;
            }
        }, {
            name : 'lastUpdateName',
            width:80,
            formatter:function(value, grid, rows, state){
                if(value == ''){
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
            classes: "tableOpt",
            width:150,
            formatter : function(cellvalue, options, rows) {
                var span_ = "<span class='edit'>&nbsp;|&nbsp;</span>";
                var span_save = "<span class='save'>&nbsp;|&nbsp;</span>";
                var span_editOpt = "<span class='editOpt'>&nbsp;|&nbsp;</span>";

                var saveOpt = "<a class='a_style save' onclick='saveAssetTree(this)'>保存</a>";
                var backInsertOpt = "<a class='a_style save' onclick='backInsert("+1+",this)'>取消</a>";

                var addNextOneOpt = "<a class='a_style edit' onclick='addNextOne("+ rows.id +","+  rows.tierNumber +",this)'>新增下一级</a>";
                var editOpt = "<a class='a_style edit' onclick='edit("+ rows.id +",this)'>编辑</a>";

                var submit1 = "<a class='a_style editOpt' onclick='submitOpt("+ rows.id +",this)'>确定</a>";
                var backInsertOpt1 = "<a class='a_style editOpt' onclick='backInsert("+2+",this)'>取消</a>";

                var opt_status = [];
                if(savePer == true){
                    opt_status.push(saveOpt);
                }

                if(backPer == true){
                    opt_status.push(backInsertOpt);
                }


                var opt = [];
                if(assetTreeType == 2 && rows.tierNumber != 1){
                    if(addNextPer == true){
                        opt.push(addNextOneOpt);
                    }
                } else if(assetTreeType == 1 ){
                    if(addNextPer == true){
                        opt.push(addNextOneOpt);
                    }
                }

                if(editPer == true ){
                    opt.push(editOpt);
                }

                var editOpt1 = [];
                if(submitPer == true){
                    editOpt1.push(submit1);
                }

                if(backPer == true){
                    editOpt1.push(backInsertOpt1);
                }


                return opt.join(span_) + editOpt1.join(span_editOpt) + opt_status.join(span_save);
            }
        }
        ],
        sortable:true,
        ExpandColClick: true,
        jsonReader: {
            repeatitems: false,
            root: "data"
        },
        loadComplete :function(xhr){
            $("#loading").css('display','none');
            /*if(assetTreeType == 2 ){
                if( $( "#AssetBusinessTreeTable > tbody > tr[class^='jqgrow']" ).length>=2 ){
                    for( var i=0;i<2;i++ ){
                        $( "#AssetBusinessTreeTable > tbody > tr[class^='jqgrow']" ).eq( i ).find( ".tableOpt" ).empty();
                        $( "#AssetBusinessTreeTable > tbody > tr[class^='jqgrow']" ).eq( i ).find( ".tableOpt" ).attr("title" , '');
                    }
                } else {
                    for( var i=0;i<$( "#AssetBusinessTreeTable > tbody > tr[class^='jqgrow']" ).length;i++ ){
                        $( "#AssetBusinessTreeTable > tbody > tr[class^='jqgrow']" ).eq( i ).find( ".tableOpt" ).empty();
                        $( "#AssetBusinessTreeTable > tbody > tr[class^='jqgrow']" ).eq( i ).find( ".tableOpt" ).attr("title" , '');
                    }
                }
            }*/
        },
        loadError:errorFunMsg
    }).trigger('reloadGrid');
}

// 增加下一个层级
function addNextOne(id,tierNumber,That){
    var tierNumberInfo = [];
    $("#AssetBusinessTreeTable>tbody>tr .tierNumberInfo").each(function(){
        tierNumberInfo.push(Number($(this).text()))
    });
    var maxTierNumber = Math.max.apply(Math,tierNumberInfo);
    if(tierNumber == maxTierNumber){
        var ids = jQuery("#menuList").jqGrid('getDataIDs');
        var rowid = Math.max.apply(Math,ids);
        tableOpt.newrowid = rowid + 1;

        var dataRow = {
            id:tableOpt.newrowid,
            tierNumber:tierNumber+1,
            assetTreeName:'',
            createName:'',
            lastUpdateName:'',
            操作:''
        };
        $("#AssetBusinessTreeTable").jqGrid("addRowData", tableOpt.newrowid, dataRow,'after',id);

        for( var i=0;i<$("#AssetBusinessTreeTable>tbody>tr").length ;i++){
            $( "#AssetBusinessTreeTable>tbody>tr" ).eq( i+1 ).find( ".tierNumberInfo" ).text( ( i + 1) );
        }

        tableOptReset(tableOpt.newrowid);
    } else {
        $("#loading").css('display','block');
        $.ajax({
            url:jsOpt.assetTreeUrl+"getTreeListByTierId",
            method:"post",
            dataType:"json",
            data:{
                assetTreeTierId:id,
                assetTreeType:assetTreeType
            },
            success:function(data){
                $("#loading").css('display','none');
                if (data.status == 2){
                    layer.alert(data.errorMessage,{
                        icon:5,
                        title:"提示信息"
                    });
                }else if (data.status == 1){
                    if(data.flag == true){
                        var dis;
                        $("#AssetBusinessTreeTable input").each(function(){
                            if( $(this).css("display") == "inline-block"){
                                dis = "inline-block";
                            }
                        });
                        if (dis == "inline-block" ){
                            layer.alert("请关闭：未关闭的输入框！！！",{
                                icon:0,
                                title:"提示信息"
                            });
                        } else {
                            var ids = jQuery("#menuList").jqGrid('getDataIDs');
                            var rowid = Math.max.apply(Math,ids);
                            tableOpt.newrowid = rowid + 1;

                            var dataRow = {
                                id:tableOpt.newrowid,
                                tierNumber:tierNumber+1,
                                assetTreeName:'',
                                createName:'',
                                lastUpdateName:'',
                                操作:''
                            };
                            $("#AssetBusinessTreeTable").jqGrid("addRowData", tableOpt.newrowid, dataRow,'after',id);

                            for( var i=0;i<$("#AssetBusinessTreeTable>tbody>tr").length ;i++){
                                $( "#AssetBusinessTreeTable>tbody>tr" ).eq( i+1 ).find( ".tierNumberInfo" ).text( ( i + 1) );
                            }

                            tableOptReset(tableOpt.newrowid);
                        }
                    } else if(data.flag == false){
                        layer.alert("不允许新增下一级", {
                            icon: 0,
                            title: "提示信息"
                        });
                    }
                }
            },
            error:errorFunMsg
        })
    }
}

// 编辑层级
function edit(id,That){
    $(That).parent().parent().find(".table_Td .fontContent").css("display","none");
    $(That).parents("table tr").find("input").css("display","inline-block");
    $(That).parents("table tr").find(".edit").css("display","none");
    $(That).parents("table tr").find(".editOpt").css("display","inline-block");
}

// 提交层级
function submitOpt(id,That){
    var assetTreeName = $.trim($(That).parents("table tr").find(".assetTreeNameInfo").val());
    if(assetTreeName==''){
        layer.alert("层级名称不能为空！",{
            icon:0,
            title:"提示信息"
        });
        return;
    }
    $("#loading").css('display','block');
    $.ajax({
        url:jsOpt.assetTreeUrl+"editAssetTree",
        type:"post",
        data:{
            id:id,
            assetTreeName:assetTreeName
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage,{
                    icon:5,
                    title:"提示信息"
                });
            }else {
                layer.alert("操作成功！",{
                    icon:6,
                    title:"提示信息"
                });
                initTable();
            }
        },
        error:errorFunMsg
    })

}

// grid添加新的一行
function insertFirst_btn() {
    var dis;
    $("#AssetBusinessTreeTable input").each(function(){
        if( $(this).css("display") == "inline-block"){
            dis = "inline-block";
        }
    });
    if (dis == "inline-block" ){
        layer.alert("请关闭：未关闭的输入框！！！",{
            icon:0,
            title:"提示信息"
        });
    } else {
        var tierNumberInfo = [];
        $("#AssetBusinessTreeTable>tbody>tr .tierNumberInfo").each(function(){
            tierNumberInfo.push(Number($(this).text()))
        });
        var maxNum = Math.max.apply(Math,tierNumberInfo);

        var ids = jQuery("#AssetBusinessTreeTable").jqGrid('getDataIDs');
        var rowid = Math.max.apply(Math,ids);
        tableOpt.newrowid = rowid + 1;
        var dataRow = {
            tierNumber:maxNum == "-Infinity"?1:maxNum+1,
            assetTreeName:'',
            createName:'',
            lastUpdateName:'',
            操作:''
        };
        //将新添加的行插入到第一列
        $("#AssetBusinessTreeTable").jqGrid("addRowData", tableOpt.newrowid, dataRow, "last");
        $('#AssetBusinessTreeTable').jqGrid('editRow', tableOpt.newrowid, true);

        tableOptReset(tableOpt.newrowid);

    }
}

// newrowid 层级id 清空层级的样式
function tableOptReset(newrowid){
    $("#" + newrowid).find("a").css("display","inline-block");
    $("#" + newrowid).find("input").css("display","inline-block");
    $("#" + newrowid).find(".fontContent").css("display","none");
    $("#" + newrowid).find(".save").css("display","inline-block");
    $("#" + newrowid).find(".edit").css("display","none");
    $("#" + newrowid).find(".editOpt").css("display","none");
}

// 取消
function backInsert(opt,That){
    $(That).parent().parent().find(".table_Td .fontContent").css("display","inline-block");
    $(That).parents("table tr").find("input").css("display","none");
    $(That).parents("table tr").find(".edit").css("display","inline-block");
    $(That).parents("table tr").find(".editOpt").css("display","none");
    if(opt == 1){
        $("#AssetBusinessTreeTable").jqGrid("delRowData",tableOpt.newrowid);
        for( var i=0;i<$("#AssetBusinessTreeTable>tbody>tr").length ;i++){
            $( "#AssetBusinessTreeTable>tbody>tr" ).eq( i+1 ).find( ".tierNumberInfo" ).text( ( i + 1) );
        }
    }
}

// 保存层级
function saveAssetTree(That){
    layer.confirm("确定保存吗？",{
    },function(){
        var assetTreeName = $.trim($(That).parents("table tr").find(".assetTreeNameInfo").val());
        if(assetTreeName==''){
            layer.alert("层级名称不能为空！",{
                icon:0,
                title:"提示信息"
            });
            return;
        }
        layer.closeAll('dialog');
        $("#loading").css('display','block');
        $.ajax({
            url:jsOpt.assetTreeUrl+"saveAssetTree",
            type:"post",
            data:{
                assetTreeType:assetTreeType,
                tierNumber:$.trim($(That).parents("table tr").find(".tierNumberInfo").text()),
                assetTreeName:assetTreeName
            },
            success:function(data){
                $("#loading").css('display','none');
                if (data.status == 2){
                    layer.alert(data.errorMessage,{
                        icon:5,
                        title:"提示信息"
                    });
                }else {
                    layer.alert("操作成功！",{
                        icon:6,
                        title:"提示信息"
                    });
                    initTable();
                }
            },
            error:errorFunMsg
        })
    })
}
