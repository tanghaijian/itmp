/**
 * 系统信息管理-菜单管理
 */

var errorDefect = '系统内部错误，请联系管理员 ！！！';
$(document).ready(function() {
    initTable();
    $("#loading").css('display','block');
});
var insert_status;

//初始化页面数据
function initTable() {
    $("#loading").css('display','block');
    $("#menuList").jqGrid("clearGridData");
    $("#menuList").jqGrid({
        url:"/system/menu/list",
        datatype: "json",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        height: 'auto',
        mtype : "POST",
        cellEdit:true,
        width: $(".box-body").width()*1,
        colNames:['id','菜单名称','类型','排序','链接','权限标识符','样式','是否开放','是否有效','操作'],
        colModel:[
            {
                name : 'id',
                index : 'id',
                hidden:true,
                key:true,
                formatter : function(value, grid, rows, state) {
                    return '<input type="hidden" class="idInfo" value="'+rows.id+'" />';
                }
            }, {
                name : 'menuButtonName',
                index : 'menuButtonName',
                formatter : function(value, grid, rows, state) {
                    return '<div class="table_Td"><div class="fontContent">'+rows.menuButtonName+'</div><input class="inputContent form-control menuButtonNameInfo" type="text"/></div>';
                }
            }, {
                name : 'menuButtonType',
                index : 'menuButtonType',
                width:80,
                formatter:function(value, grid, rows, state){
                    var menuButtonType = rows.menuButtonType;
                    if (menuButtonType == 1){
                        return '<div class="table_Td"><div class="fontContent">'+ '菜单' +'</div><select class="inputContent form-control menuButtonTypeInfo"><option value="1">'+ '菜单' +'</option><option value="2">'+ '按钮' +'</option></div>';
                    } else {
                       return '<div class="table_Td"><div class="fontContent">'+ '按钮' +'</div><select class="inputContent form-control menuButtonTypeInfo"><option value="2">'+ '按钮' +'</option><option value="1">'+ '菜单' +'</option></div>';
                    }
                }
            }, {
                name : 'menuOrder',
                index : 'menuOrder',
                width:60,
                align:'center',
                formatter : function(value, grid, rows, state) {
                    return '<div class="table_Td"><div class="fontContent">'+rows.menuOrder+'</div><input class="inputContent form-control menuOrderInfo" type="number" min="0" max="1000000" /></div>';
                }
            }, {
                name : 'url',
                index : 'url',
                width:200,
                formatter : function(value, grid, rows, state) {
                    var row='';
                    /*if ($.trim(rows.url) == "" ){
                        row +='<div class="table_Td"><div class="fontContent">'+rows.url+'</div></div>';
                    } else {*/
                        row +='<div class="table_Td"><div class="fontContent">'+rows.url+'</div><input class="inputContent form-control urlInfo" type="text" value="'+rows.url+'" /></div>';
                    /*}*/
                    return row;
                }
            }, {
                name : 'menuButtonCode',
                index : 'menuButtonCode',
                width:200,
                formatter : function(value, grid, rows, state) {
                    var menuButtonCode = '';
                    if(rows.menuButtonCode != null){
                        menuButtonCode = rows.menuButtonCode;
                    }
                    return '<div class="table_Td"><div class="fontContent">'+menuButtonCode+'</div><input class="inputContent form-control menuButtonCodeInfo" type="text" value="'+menuButtonCode+'" /></div>';
                }
            },{
                name : 'css',
                index : 'css',
                formatter : function(value, grid, rows, state) {
                    return '<div class="table_Td"><div class="fontContent">'+rows.css+'</div><input class="inputContent form-control cssInfo" type="text" value="'+rows.css+'" /></div>';
                }
            }, {
                name : 'openStatus',
                index : 'openStatus',
                width:80,
                formatter:function(value, grid, rows, state){
                	if (rows.openStatus == 1){
                        return '<div class="table_Td"><div class="fontContent">'+ '是' +'</div><select class="inputContent form-control menuButtonOpenStatus"><option value="1">'+ '是' +'</option><option value="2">'+ '否' +'</option></div>';
                    } else if(rows.openStatus == 2) {
                       return '<div class="table_Td"><div class="fontContent">'+ '否' +'</div><select class="inputContent form-control menuButtonOpenStatus"><option value="2">'+ '否' +'</option><option value="1">'+ '是' +'</option></div>';
                    }else{
                    	return '<div class="table_Td"><div class="fontContent">'+ '' +'</div><select class="inputContent form-control menuButtonOpenStatus"><option value="1">'+ '是' +'</option><option value="2">'+ '否' +'</option></div>';
                    }
                }
            }, {
                name : 'validStatus',
                index : 'validStatus',
                width:80,
                formatter:function(value, grid, rows, state){
                    return rows.validStatus == 2?'<div class="table_Td status">'+ '无效' +'</div>':'<div class="table_Td status">'+ '有效' +'</div>';
                }
            }, {
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
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                    var status_1 = 1;
                    var status_2 = 2;
                    var span_ = "<span>&nbsp;|&nbsp;</span>";
                    var saveBack_a_span = '<span class="saveBack_a">&nbsp;|&nbsp;</span>';
                    var status_2_span = '<span class="status_2">&nbsp;|&nbsp;</span>';
                    var insertBack_a_span = '<span class="insertBack_a">&nbsp;|&nbsp;</span>';

                    var b = '<a href="#" class="a_style saveBack_a" onclick="save('+ rows.validStatus +',this)">保存</a>';
                    var c = '<a href="#" class="a_style saveBack_a" onclick="back(this)">取消</a>';

                    var a = '<a href="#" class="a_style status_2" onclick="edit('+ rows.id + ',this)">编辑</a>';
                    var d ='<a href="#" class="a_style status_2" onclick="getMenuEntity('+ rows.id + ','+ 1 +')">新增下级</a>';
                    var eOptValue1 = {id:rows.id,parentIds:rows.parentIds,status:status_2};
                    eOptValue1 = JSON.stringify(eOptValue1).replace(/"/g, '&quot;');
                    var e ='<a href="#" class="a_style status_2" onclick="updateMenu('+ eOptValue1 + ')">置为无效</a>';
                    var eOptValue2 = {id:rows.id,parentIds:rows.parentIds};
                    eOptValue2 = JSON.stringify(eOptValue2).replace(/"/g, '&quot;');
                    var f ='<a href="#" class="a_style status_2" onclick="deleteMenu('+ eOptValue2 + ')">删除</a>';

                    var g ='<a href="#" class="a_style insertBack_a" onclick="insertMenu('+ rows.id + ',' + insert_status+','+rows.validStatus+',this)">添加</a>';
                    var h ='<a href="#" class="a_style insertBack_a" onclick="backInsert()">取消 </a>';

                    var eOptValue3 = {id:rows.id,parentIds:rows.parentIds,status:status_1};
                    eOptValue3 = JSON.stringify(eOptValue3).replace(/"/g, '&quot;')
                    var i = '<a href="#" class="a_style" onclick="updateMenu('+ eOptValue3 + ')">置为有效</a>';
                    var eOptValue4 = {id:rows.id,parentIds:rows.parentIds};
                    eOptValue4 = JSON.stringify(eOptValue4).replace(/"/g, '&quot;')
                    var j ='<a href="#" class="a_style" onclick="deleteMenu('+ eOptValue4  + ')">删除</a>';

                    var opt_h = [];
                    opt_h.push(g);
                    opt_h.push(h);
                    if(rows.validStatus==1 ){
                        var optArr = [];
                        if(menuEdit == true){
                            optArr.push(a);
                        }
                        if(newMenu == true && rows.menuButtonType == 1){
                            optArr.push(d);
                        }
                        if(status2 == true ){
                            optArr.push(e);
                        }
                        if(menuDelete == true ){
                            optArr.push(f);
                        }
                        var opt_b = [];
                        opt_b.push(b);
                        opt_b.push(c);
                        return optArr.join(status_2_span)+opt_b.join(saveBack_a_span)+opt_h.join(insertBack_a_span);
                    } else if(rows.validStatus==2){
                        var opt_2 = [];
                        if (status1 == true){
                            opt_2.push(i);
                        }
                        if(menuDelete == true){
                            opt_2.push(j);
                        }
                        return opt_2.join(span_)+opt_h.join(insertBack_a_span);
                    }

                }
            }
        ],
        treeGrid: true,
        treeGridModel: "adjacency",
        ExpandColumn: "menuButtonName",
        treeIcons: {plus:'fa fa-caret-right',minus:'fa fa-caret-down',leaf:''},
        sortname:"menuOrder",
        sortable:true,
        ExpandColClick: true,
        multiselect: true,
        jsonReader: {
            repeatitems: false,
            root: "data"
        },
        treeReader : {           //设置树形显示时4个关键字段对应的返回数据字段
            level_field: "level",      // 属性层级
            parent_id_field: "parent", //父级rowid
            leaf_field: "isLeaf",      //是否还有子级菜单
            expanded_field: "expanded" //是否加载完毕
        },
        loadComplete :function(xhr){
            $("#loading").css('display','none');
            var ids = $("#menuList").jqGrid("getDataIDs");
            //获取所有行的id
            var rowDatas = $("#menuList").jqGrid("getRowData");
            //获取所有行的数据
            for(var i=0;i < rowDatas.length;i++){
                var rowData = rowDatas[i];
                var validStatus = $(rowData.validStatus.toString()).text();
                if(validStatus == "无效"){
                    $("#"+ids[i]+ " td").css("background-color","#F2F2F2");
                    $("#"+ids[i]+ " div").css("text-decoration","line-through");
                }
            }
        },
        loadError:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        },
        gridComplete: function() { //隐藏title
            var aTr = $('#menuList').find('tbody tr.jqgrow');
             for (var i = 0; i < aTr.length; i++) {
                 var item = $(aTr[i]).children();
                  for (var j = 0; j < item.length; j++) {
                     $(item[j]).attr('title', '')
                 }
              }
           },
    }).trigger('reloadGrid');
}

//【getMenuEntity】新增下级 opt=1
function getMenuEntity(menuId,Opt){
    $.ajax({
        url:"/system/menu/getMenuEntity",
        method:"post",
        dataType:"json",
        data:{
            menuId:menuId
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage,{
                    icon:5,
                    title:"提示信息"
                });
            }else if (data.status == 1){
            	//组装下级菜单html
                optMenu(data.menuInfo,Opt)
            }
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })
}
//组装下级菜单
function optMenu(menuInfo,Opt){
    switch (Opt){
        case 1:
            insertChildrenMenu(menuInfo);
            break;
        default:break;
    }
}


//选中行启用行编辑
function edit(menuId,That) {
    insert_status = 1;
    $.ajax({
        url:"/system/menu/getMenuEntity",
        method:"post",
        dataType:"json",
        data:{
            menuId:menuId
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage,{
                    icon:5,
                    title:"提示信息"
                });
            }else if (data.status == 1){
                var rows = data.menuInfo;
                $(".menuButtonNameInfo").val(rows.menuButtonName);
                $(".menuOrderInfo").val(rows.menuOrder);
                $(".urlInfo").val(rows.url);
                $(".cssInfo").val(rows.css);
                $(".menuButtonCodeInfo").val(rows.menuButtonCode);

                var dis;
                $("#menuList input").each(function(){
                    if( $(this).css("display") == "inline-block"){
                        dis = "inline-block";
                    }
                });
                if (dis == "inline-block" ){
                    layer.alert("请关闭：未关闭的输入框！！！",{
                        icon:2,
                        title:"提示信息"
                    });
                } else {
                    $(That).parent().children("a").css("display","inline-block");
                    $(That).parent().children(".saveBack_a").css("display","inline-block");
                    $(That).parent().children(".status_2").css("display","none");
                    $(That).parent().children(".insertBack_a").css("display","none");
                    $(That).parent().parent().find(".table_Td .fontContent").css("display","none");
                    $(That).parents("table tr").find("input").css("display","inline-block");
                    $(That).parents("table tr").find("select").css("display","inline-block");
                }

            }
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })
}

//取消编辑
function back(That){
    $(That).parents("table tr").find("input").css("display","none");
    $(That).parents("table tr").find("select").css("display","none");
    $(That).parent().children("a").css("display","none");
    $(That).parent().children(".saveBack_a").css("display","none");
    $(That).parent().children(".status_2").css("display","inline-block");
    $(That).parent().parent().find(".table_Td .fontContent").css("display","inline-block");
}

//保存当前修改的菜单信息
function save(validStatus,That){
    layer.confirm("您确定要修改当前菜单信息吗？",{
        },function(){
        layer.closeAll('dialog');
        $("#loading").css('display','block');
        $.ajax({
            url:"/system/menu/updateThisMenu",
            method:"post",
            dataType:"json",
            contentType:"application/json;charset=utf-8",
            data:JSON.stringify({
                id:$.trim($(That).parents("table tr").find(".idInfo").val()),
                menuButtonName:$.trim($(That).parents("table tr").find(".menuButtonNameInfo").val()),
                menuButtonType:$.trim($(That).parents("table tr").find(".menuButtonTypeInfo").val()),
                menuOrder:$.trim($(That).parents("table tr").find(".menuOrderInfo").val()),
                url:$.trim($(That).parents("table tr").find(".urlInfo").val()),
                css:$.trim($(That).parents("table tr").find(".cssInfo").val()),
                menuButtonCode:$.trim($(That).parents("table tr").find(".menuButtonCodeInfo").val()),
                validStatus:validStatus,
                openStatus:$.trim($(That).parents("table tr").find(".menuButtonOpenStatus").val())
            }),
            success:function(data){
                $("#loading").css('display','none');
                if (data.status == 2){
                    layer.alert(data.errorMessage,{
                        icon:5,
                        title:"提示信息"
                    });
                }else {
                    layer.alert("菜单修改成功！",{
                        icon:6,
                        title:"提示信息"
                    });
                    initTable();
                }
            },
            error:function(){
                $("#loading").css('display','none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    })
}

var newrowid ;

//增加下级html组装
function insertChildrenMenu(rows){
    insert_status = 2;
    var dis;
    $("#menuList input").each(function(){
        if( $(this).css("display") == "inline-block"){
            dis = "inline-block";
        }
    });
    //一次只能新增一次
    if (dis == "inline-block" ){
        layer.alert("请关闭：未关闭的输入框！！！",{
            icon:2,
            title:"提示信息"
        });
    } else {
        var ids = jQuery("#menuList").jqGrid('getDataIDs');
        var rowid = Math.max.apply(Math,ids);
        newrowid = rowid + 1;
        var dataRow = {
            id:rows.id,
            parentId:rows.parentId,
            parentIds:rows.parentIds,
            menuButtonName:'',
            menuButtonType:1,
            menuOrder:'',
            url:'',
            css:'',
            validStatus:1,
            openStatus:1,
            操作:''
        };
        var data = {
            isLeaf:false,
            expanded:true
        };
        //改变上级数据
        $("#menuList").jqGrid("setRowData", rows.id, data,data);

        //将新添加的行插入到第一列
        $("#menuList").jqGrid("addRowData", newrowid, dataRow,'after',rows.id);
        $('#menuList').jqGrid('editRow', newrowid, true);

        $("#"+newrowid).find("a").css("display","inline-block");
        $("#"+newrowid).find(".insertBack_a").css("display","inline-block");
        $("#"+newrowid).find(".fontContent").css("display","none");
        $("#"+newrowid).find(".saveBack_a").css("display","none");
        $("#"+newrowid).find(".status_2").css("display","none");
        $("#"+newrowid).find("input").css("display","inline-block");
        $("#"+newrowid).find("select").css("display","inline-block");
    }


}

// grid添加新的一行
function insertFirstMenu_btn() {
    insert_status = 1;
    var dis;
    $("#menuList input").each(function(){
        if( $(this).css("display") == "inline-block"){
            dis = "inline-block";
        }
    });
    if (dis == "inline-block" ){
        layer.alert("请关闭：未关闭的输入框！！！",{
            icon:2,
            title:"提示信息"
        });
    } else {
        var ids = jQuery("#menuList").jqGrid('getDataIDs');
        var rowid = Math.max.apply(Math,ids);
        newrowid = rowid + 1;
        var dataRow = {
            id:newrowid,
            menuButtonName:'',
            menuButtonType:1,
            menuButtonCode:'',
            menuOrder:'',
            url:'',
            css:'',
            validStatus:1,
            openStatus:1,
            操作:''
        };
        //将新添加的行插入到第一列
        $("#menuList").jqGrid("addRowData", newrowid, dataRow, "first");
        $('#menuList').jqGrid('editRow', newrowid, true);

        $("#"+newrowid).find("a").css("display","inline-block");
        $("#"+newrowid).find(".insertBack_a").css("display","inline-block");
        $("#"+newrowid).find(".fontContent").css("display","none");
        $("#"+newrowid).find(".saveBack_a").css("display","none");
        $("#"+newrowid).find(".status_2").css("display","none");
        $("#"+newrowid).find("input").css("display","inline-block");
        $("#"+newrowid).find("select").css("display","inline-block");
    }
}

function backInsert(){
    $("#menuList").jqGrid("delRowData",newrowid);
}

//【添加】保存菜单按钮信息
function insertMenu(menuId,insert_status,validStatus,That){

    $.ajax({
        url:"/system/menu/getMenuEntity",
        method:"post",
        dataType:"json",
        data:{
            menuId:menuId
        },
        success:function(data){
            $("#loading").css('display','none');
            if (data.status == 2){
                layer.alert(data.errorMessage,{
                    icon:5,
                    title:"提示信息"
                });
            }else if (data.status == 1){
                var rows = data.menuInfo;

                var id;
                var parentIds;
                if (insert_status == 1){
                    id = null;
                    parentIds = null;
                } else {
                    id = menuId;
                    parentIds =rows.parentIds;
                }
                var menuButtonName = $.trim($(That).parents("table tr").find(".menuButtonNameInfo").val());
                var menuButtonType;
                var type = $.trim($(That).parents("table tr").find(".menuButtonTypeInfo").val());
                type == 1?menuButtonType="菜单":menuButtonType="按钮";

                if (menuButtonName == ""){
                    layer.alert("请输入正确的"+ menuButtonType +"名称",{
                        icon:2,
                        title:"提示信息"
                    });
                } else {
                    layer.confirm("您确定添加  "+ menuButtonName +"    "+menuButtonType+"信息吗？",{
                    },function(){
                        layer.closeAll('dialog');
                        $("#loading").css('display','block');
                        $.ajax({
                            url:"/system/menu/insertMenu",
                            dataType:'json',
                            method:'post',
                            contentType:'application/json;charset=UTF-8',
                            data:JSON.stringify({
                                id:id,
                                parentIds:parentIds,
                                menuButtonName:$.trim($(That).parents("table tr").find(".menuButtonNameInfo").val()),
                                menuButtonType:type,
                                menuOrder:$.trim($(That).parents("table tr").find(".menuOrderInfo").val()),
                                url:$.trim($(That).parents("table tr").find(".urlInfo").val()),
                                css:$.trim($(That).parents("table tr").find(".cssInfo").val()),
                                menuButtonCode:$.trim($(That).parents("table tr").find(".menuButtonCodeInfo").val()),
                                validStatus:validStatus,
                                status:1,
                                openStatus:$.trim($(That).parents("table tr").find(".menuButtonOpenStatus").val()),
                            }),
                            success:function(data){
                                $("#loading").css('display','none');
                                if (data.status == 2){
                                    layer.alert(data.errorMessage,{
                                        icon:5,
                                        title:"提示信息"
                                    });
                                }else {
                                    layer.alert("添加菜单成功！",{
                                        icon:6,
                                        title:"提示信息"
                                    });
                                    initTable();
                                }
                            },
                            error:function(){
                                $("#loading").css('display','none');
                                layer.alert(errorDefect, {
                                    icon: 2,
                                    title: "提示信息"
                                });
                            }
                        })
                    })
                }
            }
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })

}

// 置当前菜单为无效 status_1：有效  status_2: 无效
function updateMenu(row){
    var layerStatus;
    if (row.status == 1){
        layerStatus = "有效";
    } else {
        layerStatus = "无效";
    }
    layer.confirm('您确定要置当前菜单' +'为：'+ layerStatus +'吗？', {
        btn: ['确定','取消'], //按钮
        title: "提示信息"
    }, function(){
        layer.closeAll('dialog');
        $("#loading").css('display','block');
        $.ajax({
            url:"/system/menu/updateMenu",
            method:"post",
            data:JSON.stringify({
                id:row.id,
                parentIds:row.parentIds,
                validStatus:row.status
            }),
            contentType:'application/json;charset=UTF-8',
            success:function(data){
                $("#loading").css('display','none');
                if (data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 5,
                        title: "提示信息"
                    });
                } else {
                    layer.alert('该菜单置为'+layerStatus+'成功！', {
                        icon: 6,
                        title: "提示信息"
                    });
                    initTable();
                }
            },
            error:function(){
                $("#loading").css('display','none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        });
    })
}

//删除
function deleteMenu(row){
    layer.confirm("您确定删除当前菜单信息吗？" + "<br/>" +
        "(删除后，页面无法恢复)",{
    },function(){
        layer.closeAll('dialog');
        $("#loading").css('display','block');
        $.ajax({
            url:"/system/menu/deleteMenu",
            dataType:'json',
            method:'post',
            contentType:'application/json;charset=UTF-8',
            data:JSON.stringify({
                id:row.id,
                parentIds:row.parentIds,
                validStatus:2,
                status:2
            }),
            success:function(data){
                $("#loading").css('display','none');
                if (data.status == 2){
                    layer.alert(data.errorMessage,{
                        icon: 5,
                        title: "提示信息"
                    })
                } else {
                    layer.alert('删除菜单成功！',{
                        icon: 6,
                        title: "提示信息"
                    });
                    initTable();
                }
            },
            error:function(){
                $("#loading").css('display','none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    })

}