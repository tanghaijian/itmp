/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 */
//全局变量
var mydata = {
    treeArr : [],    //用来储存，点击取得树节点及其所有的祖先节点,显示在头部
    treeSetting :{   // 树的配置信息
        data: {
            simpleData: {
                enable: true
            }
        },
        view: {
            addDiyDom: addDiyDom
        },
        callback: {
            onClick: getDocumentTable
        }
    }
}
//菜单栏 --> 假数据
var zNodes =[
    { id:'a1', pId:0, name:"父节点1 - 展开", needs: 1 , open:true},
    { id:'a11', pId:'a1', name:"父节点11 - 折叠", needs: 1 },
    { id:'a111', pId:'a11', name:"叶子节点111", needs: 1 },
    { id:'a112', pId:'a11', name:"叶子节点112", needs: 1 },
    { id:'a113', pId:'a11', name:"叶子节点113", needs: 2},
    { id:'a114', pId:'a11', name:"叶子节点114", needs: 2},
    { id:12, pId:1, name:"父节点12 - 折叠"},
    { id:121, pId:12, name:"叶子节点121"},
    { id:122, pId:12, name:"叶子节点122"},
    { id:123, pId:12, name:"叶子节点123"},
    { id:124, pId:12, name:"叶子节点124"},
    { id:13, pId:1, name:"父节点13 - 没有子节点", isParent:true},
    { id:2, pId:0, name:"父节点2 - 折叠"},
    { id:21, pId:2, name:"父节点21 - 展开", open:true},
    { id:211, pId:21, name:"叶子节点211"},
    { id:212, pId:21, name:"叶子节点212"},
    { id:213, pId:21, name:"叶子节点213"},
    { id:214, pId:21, name:"叶子节点214"},
    { id:22, pId:2, name:"父节点22 - 折叠"},
    { id:221, pId:22, name:"叶子节点221"},
    { id:222, pId:22, name:"叶子节点222"},
    { id:223, pId:22, name:"叶子节点223"},
    { id:224, pId:22, name:"叶子节点224"},
    { id:23, pId:2, name:"父节点23 - 折叠"},
    { id:231, pId:23, name:"叶子节点231"},
    { id:232, pId:23, name:"叶子节点232"},
    { id:233, pId:23, name:"叶子节点233"},
    { id:234, pId:23, name:"叶子节点234"},
    { id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true}
];

$(document).ready(function () {
    getTree();
    var testEditormdView2;
    testEditormdView2 = editormd.markdownToHTML("test-editormd-view2", {
        htmlDecode      : "style,script,iframe",  // you can filter tags decode
        emoji           : true,
        taskList        : true,
        tex             : true,  // 默认不解析
        flowChart       : true,  // 默认不解析
        sequenceDiagram : true,  // 默认不解析
    });
    //选择关联信息
    $("#relInfo").on('click',function () {
        layer.open({
            type: 2,
            title: '选择关联信息',
            shadeClose: true,
            move: false,
            area: ['94%', '90%'],
            offset: "4%",
            shade: 0.3,
            tipsMore: true,
            anim: 2, 
            content:  '/projectManageui/assetRq/toAssociatedDemand',
            btn: ['确定','取消'] ,
            btnAlign: 'c', //按钮居中
            yes: function( index , layero){
                var body = layer.getChildFrame('body', index );
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var obj = iframeWin.getAssociatedDemandId(); 
                if( obj.status == true ){
                	console.log( obj.data );
                	layer.closeAll();
                } 
            },
            btn2:function(){
                layer.closeAll();
            }
        });
    })
    //点击选择系统 跳出选择系统页面
    $("#system").on('click',function () {
        layer.open({
            type: 2,
            title: '选择系统',
            shadeClose: true,
            move: false,
            area: ['94%', '90%'],
            offset: "4%",
            shade: 0.3,
            tipsMore: true,
            anim: 2,
            content:   '/projectManageui/requirementPerspective/toSystem',
            btn: ['确定','取消'] ,
            btnAlign: 'c', //按钮居中
            yes: function( index , layero){
                var body = layer.getChildFrame('body', index );
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var id = iframeWin.getSystemid();
                console.log( id );
            },
            btn2:function(){
                layer.closeAll();
            }
        });
    })
    //选择开发任务
    $("#devTask").on('click',function () {
        layer.open({
            type: 2,
            title: '选择开发任务',
            shadeClose: true,
            move: false,
            area: ['94%', '90%'],
            offset: "4%",
            shade: 0.3,
            tipsMore: true,
            anim: 2, 
            content:  '/projectManageui/assetRq/toDevelopmentTask',
            btn: ['确定','取消'] ,
            btnAlign: 'c', //按钮居中
            yes: function( index , layero){
                var body = layer.getChildFrame('body', index );
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var obj = iframeWin.getDevTaskId(); 
                if( obj.status == true ){
                	console.log( obj.data );
                	layer.closeAll();
                } 
            },
            btn2:function(){
                layer.closeAll();
            }
        });
    })
    //点击版本历史 跳出版本历史页面
    $("#history").on('click',function () {
        layer.open({
            type: 2,
            title: '选择系统',
            shadeClose: true,
            move: false,
            area: ['94%', '90%'],
            offset: "4%",
            shade: 0.3,
            tipsMore: true,
            anim: 2,
            content:  '/projectManageui/requirementPerspective/toAssociatedDemand',
            btn: ['确定','取消'] ,
            btnAlign: 'c', //按钮居中
            yes: function( index , layero){
                var body = layer.getChildFrame('body', index );
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var id = iframeWin.getSystemid();
                console.log( id );
            },
            btn2:function(){
                layer.closeAll();
            }
        });
    })
    //点击更多附件 跳出附件页面
    $("#appendixMore").on('click',function () {
        layer.open({
            type: 2,
            title: '选择系统',
            shadeClose: true,
            move: false,
            area: ['94%', '90%'],
            offset: "4%",
            shade: 0.3,
            tipsMore: true,
            anim: 2,
            content:  '../../assetsLibrary/appendix.html',
            btn: ['确定','取消'] ,
            btnAlign: 'c', //按钮居中
            yes: function( index , layero){
                var body = layer.getChildFrame('body', index );
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var id = iframeWin.getSystemid();
            },
            btn2:function(){
                layer.closeAll();
            }
        });
    })
    //点击更多关联信息  跳出关联信息页面
    $("#relevanceInfoMore").on('click',function () {
        layer.open({
            type: 2,
            title: '选择系统',
            shadeClose: true,
            move: false,
            area: ['94%', '90%'],
            offset: "4%",
            shade: 0.3,
            tipsMore: true,
            anim: 2,
            content:  '../../assetsLibrary/relevanceInfo.html',
            btn: ['确定','取消'] ,
            btnAlign: 'c', //按钮居中
            yes: function( index , layero){
                var body = layer.getChildFrame('body', index );
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var id = iframeWin.getSystemid();
            },
            btn2:function(){
                layer.closeAll();
            }
        });
    })
});
//菜单栏
function getTree(){
    $.fn.zTree.init($("#menuTree"),  mydata.treeSetting , zNodes);
    fuzzySearch('menuTree','#treeSearchInput',null,true); //初始化模糊搜索方法
}
//点击判断 非需求目录 需求目录
function getDocumentTable(event, treeId, treeNode, clickFlag){
    $(".noData").css("display","none");
    if( treeNode.needs == 1 ){       //非需求目录
        $(".notRequirement").css("display","flex");
        $(".requirement").css("display","none");
        //设置标题
        mydata.treeArr = [];
        getParentNameArr( treeNode );
        $( "#title" ).text( mydata.treeArr.join(" > ") );

        //生成表格
        getTable( treeNode );
    }else{         //需求目录
        $(".notRequirement").css("display","none");
        $(".requirement").css("display","flex");
    }
}

// 非需求目录 --> 生成标题
function getParentNameArr( treeNode ){
    mydata.treeArr.unshift( treeNode.name )
    if( treeNode.level == 0 ){
        return ;
    }else{
        var parentObj = treeNode.getParentNode();
        getParentNameArr( parentObj );
    }
}
// 非需求目录 --> 形成表格
function getTable( treeNode ) {
    $("#loading").css('display','block');
    $("#documentList").bootstrapTable('destroy');
    $("#documentList").bootstrapTable({
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 25, 50, 100 ],
        queryParams : function(params) {
            var param = {
                pageNumber:params.pageNumber,
                pageSize:params.pageSize
            };
            return param;
        },
        columns : [{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "doc",
            title : "文档名称",
            align : 'center'
        },{
            field : "info",
            title : "创建信息",
            align : 'center'
        },{
            field : "last",
            title : "最后更新时间",
            align : 'center'
        },{
            field : "opt",
            title : "操作",
            align : 'center'
        }],
        onLoadSuccess:function(data){
            $("#loading").css('display','none');
        },
        onLoadError : function() {
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        },
        onPageChange:function(){
            $("#loading").css('display','block');
        }
    });
}
function addDiyDom( treeId, treeNode ){
    if( treeNode.id == 'a112' ){
        var IDMark_A = "_a";
        var aObj = $("#" + treeNode.tId + IDMark_A);
        var showStr = '<span onfocus="this.blur();"><span class="button fileIcon"></span></span>';
        aObj.append(showStr);
    }
}