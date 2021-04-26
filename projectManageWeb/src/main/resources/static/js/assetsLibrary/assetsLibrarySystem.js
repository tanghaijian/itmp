/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 * 资产库（系统视角）
 */
//全局变量
var mydata = {
	search:{
		sysArr: []
	},
    treeArr : [],    //用来储存，点击取得树节点及其所有的祖先节点,显示在头部
    treeSetting :{   // 树的配置信息 
        data: {
            simpleData: {
                enable: true
            },
            key:{
            	name:"documentType"
            }
        },
        view: {
            addDiyDom: addDiyDom
        },
        callback: {
        	beforeClick : checkNode,
            onClick: getDocumentTable
        } 
    }
}  
$(document).ready(function () { 
    var testEditormdView2;
    testEditormdView2 = editormd.markdownToHTML("test-editormd-view2", {
        htmlDecode      : "style,script,iframe",  // you can filter tags decode
        emoji           : true,
        taskList        : true,
        tex             : true,  // 默认不解析
        flowChart       : true,  // 默认不解析
        sequenceDiagram : true,  // 默认不解析
    }); 
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
                var obj = iframeWin.getSystemid();
                if( obj.status == true ){
                	mydata.search.sysArr = [];
                	var str = '';
                	obj.data.map(function (item){ 
                		mydata.search.sysArr.push(item);
                		str += item.systemName+",";
                	});
                	$("#system").val( str );
                	getTree();
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
            content:  '',
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
            content:  '',
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
            content:  '',
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
	 $.ajax({
        url:"/projectManage/systemPerspective/getSomeDocumentType",
        type:"post",
        data:{ 
        	// systemIds:mydata.search.sysArr.join(","), 
        },
        success:function(data){ 
        	mydata.search.sysArr.map(function(value, index, array) {
        		value.documentType = value.systemName;
 				value.children = data.data;
			})
        	var newData = mydata.search.sysArr;  
        	$.fn.zTree.init($("#menuTree"),  mydata.treeSetting , newData );
        },
        error:function(data){
        	 console.log( data );
        }
    })
    return ;
    
    fuzzySearch('menuTree','#treeSearchInput',null,true); //初始化模糊搜索方法
}
//判断节点 是否 为文档
function checkNode(treeId, treeNode, clickFlag){
	var pid = treeNode.getParentNode(); 
    if( pid == null || pid == "" ){
        return false;
    }else{ 
        return true;
    } 
}
//点击判断 非需求目录 需求目录
function getDocumentTable(event, treeId, treeNode, clickFlag){
	$(".noData").css("display","none");
	//设置标题
    mydata.treeArr = [];
    getParentNameArr( treeNode );
    console.log( mydata.treeArr )
    $( "#title" ).text( mydata.treeArr.join(" > ") );  
    var pid = treeNode.getParentNode();
	if( pid.level == 0  ){
		$.ajax({
	        url:"/projectManage/systemPerspective/getFilesUnderDirectory",
	        type:"post",
	        data:{ 
	        	systemId: pid.id,
      			documentType : treeNode.id
	        },
	        success:function(data){   
	        	if( data.data.documents != undefined ){
	        		if( data.data.type == "documents" ){  
	        			getTable( data.data.documents ); 
		           	}else if( data.data.type == "markDown" ){
		           	 
		           	}
	        	}else{
	        		getTable([]); 
	        	} 
	        },
	        error:function(data){
	        	 console.log( data );
	        }
	    }) 
	}
	return;
	 
}

// 非需求目录 --> 生成标题
function getParentNameArr( treeNode ){ 
    mydata.treeArr.unshift( treeNode.documentType )
    var pid = treeNode.getParentNode();
    console.log( pid )
    if( pid == null || pid == "" ){
        return ;
    }else{ 
        getParentNameArr( pid );
    } 
}
// 非需求目录 --> 形成表格
function getTable( data ) { 
	console.log(data)	
    $("#loading").css('display','block');
    $("#documentList").bootstrapTable('destroy');
    $("#documentList").bootstrapTable({ 
    	data : data,
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : false,
        sidePagination: "server", 
        columns : [{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "documentName",
            title : "文档名称",
            align : 'left',
            formatter : function(value, row, index) { 
            	var a;
            	var rows = JSON.stringify(row).replace(/"/g, '&quot;');
            	if( row.saveType == 1 ){  
                	var url = "/projectManage/assetsLibraryRq/downObject?documentS3Bucket=" + encodeURIComponent(row.documentS3Bucket) + "&documentS3Key=" + encodeURIComponent(row.documentS3Key) + "&documentName=" + encodeURIComponent(row.documentName); 
                	a = '<a class="a_style" href="'+ url +'" download="'+value+'">'+ value +'</a>';
            	}else {
            		a = '<a class="a_style">'+ value +'</a>';
            	}
            	return  a;
            }
        },{
            field : "createDate",
            title : "创建信息",
            align : 'center',
            formatter : function(value, row, index) { 
            	return isValueNull( row.createUserName ) + " "+ isValueNull( row.createDate );
            }
        },{
            field : "lastUpdateDate",
            title : "最后更新时间",
            align : 'center',
            formatter : function(value, row, index) { 
            	return isValueNull( row.updateUserName ) + " "+ isValueNull( row.lastUpdateDate );
            }
        },{
            field : "opt",
            title : "操作",
            align : 'center',
            formatter : function(value, row, index) {
            	rows = JSON.stringify(row).replace(/"/g, '&quot;');
            	return '<a class="a_style" onclick="getRelevanceInfo('+ rows +')">关联信息</a>'+' | '+'<a class="a_style" onclick="getHistory('+ rows +')">历史版本</a>'
            }
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
function getRelevanceInfo( row ){
	 layer.open({
       type: 2,
       title: '关联信息',
       shadeClose: true,
       move: false,
       area: ['94%', '90%'],
       offset: "4%",
       shade: 0.3,
       tipsMore: true,
       anim: 2,
       content: '/projectManageui/requirementPerspective/relevanceInfo?id=' + row.id,
       btn: ['关闭'] ,
       btnAlign: 'c', //按钮居中 
       btn:function(){
           layer.closeAll();
       }
   });
}
function getHistory( row ){  
	 layer.open({
         type: 2,
         title: '历史版本',
         shadeClose: true,
         move: false,
         area: ['94%', '90%'],
         offset: "4%",
         shade: 0.3,
         tipsMore: true,
         anim: 2,
         content: '/projectManageui/requirementPerspective/history?id=' + row.id,
         btn: ['关闭'] ,
         btnAlign: 'c', //按钮居中 
         btn:function(){
             layer.closeAll();
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