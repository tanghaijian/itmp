/**
 * author：王元玲
 */

/**
 * 
 * @param zTreeId ztree对象的id,不需要#
 * @param searchField 输入框选择器
 * @param isHighLight 是否高亮,默认高亮,传入false禁用
 * @param isExpand 是否展开,默认合拢,传入true展开
 * @param rootName 根节点标识属性
 * @param rootValue 根节点标识属性对应的值
 * @returns
 */    
 function fuzzySearch(zTreeId, searchField, isHighLight, isExpand,rootName,rootValue){
		var zTree = $.fn.zTree.getZTreeObj(zTreeId);
	    if(!zTree){
	        alter("获取树对象失败!");
	    }
	    var nameKey = zTree.setting.data.key.name;                    //获取name属性的key
	    isHighLight = isHighLight===false ? false:true;               //除直接输入false的情况外,都默认为高亮
	    isExpand = isExpand ? true:false;
	    zTree.setting.view.nameIsHTML = isHighLight;                  //允许在节点名称中使用html,用于处理高亮
	    var metaChar = '[\\[\\]\\\\\^\\$\\.\\|\\?\\*\\+\\(\\)]';      //js正则表达式元字符集
	    var rexMeta = new RegExp(metaChar, 'gi');                     //匹配元字符的正则表达式
	    
	    // 过滤ztree显示数据
	    function ztreeFilter(zTree,_keywords,callBackFunc) {
	        if(!_keywords){                                           //如果为空，赋值空字符串
	            _keywords ='';
	        }
	        
	        // 查找符合条件的叶子节点
	        function filterFunc(node) {
	            if(node && node.oldname && node.oldname.length>0){
	                node[nameKey] = node.oldname;                     //如果存在原始名称则恢复原始名称
	            }
	            zTree.updateNode(node);                               //更新节点让之前对节点所做的修改生效
	            if (_keywords.length == 0) {                          //如果关键字为空,返回true,表示每个节点都显示
//	            	zTree.showNode(node);
//	            	zTree.expandNode(node,isExpand);                  //关键字为空时是否展开节点
	            	zTree.expandAll();
	                return true;
	            }
	            
	            //节点名称和关键字都用toLowerCase()做小写处理
	            if (node[nameKey] && node[nameKey].toLowerCase().indexOf(_keywords.toLowerCase())!=-1) {
	                if(isHighLight){ //如果高亮，对文字进行高亮处理
	                    var newKeywords = _keywords.replace(rexMeta,function(matchStr){
	                        return '\\' + matchStr;                   //对元字符做转义处理
	                    });
	                    node.oldname = node[nameKey];                 //缓存原有名称用于恢复
	                    var rexGlobal = new RegExp(newKeywords, 'gi');
	                    node[nameKey] = node.oldname.replace(rexGlobal, function(originalText){
	                    	return '<span class="search_highlight">'+originalText+'</span>';
	                    });
	                    zTree.updateNode(node);                      //update让更名和高亮生效
	                }
	                zTree.showNode(node);                             //显示符合条件的节点
	                return true;                                      //带有关键字的节点不隐藏
	            }
	            zTree.hideNode(node);                                 // 隐藏不符合要求的节点
	            return false;                                         //不符合返回false
	        }
	        var nodesShow = zTree.getNodesByFilter(filterFunc);       //获取匹配关键字的节点
	        processShowNodes(nodesShow, _keywords);                   //对获取的节点进行二次处理
	    }
	    
	    //对符合条件的节点做二次处理
	    function processShowNodes(nodesShow,_keywords){
	        if(nodesShow && nodesShow.length>0){
	            if(_keywords.length>0){                               //关键字不为空时对关键字节点的祖先节点和子节点进行二次处理
	                $.each(nodesShow, function(n,obj){
	                    var pathOfOne = obj.getPath();                //向上追溯,获取节点的所有祖先节点(包括自己)
	                    showNodes(pathOfOne,zTree);
	                    var nodes = [];                               //向下追溯,获取节点的所有子节点(包括自己)
	                    nodes = getAllChildrenNodes(nodes,obj);
	                    showNodes(nodes,zTree);
	                });      
	            }else{    
	            	zTree.expandAll(true);
//	                var rootNodes = zTree.getNodesByParam(rootName,rootValue);   //获得所有根节点
//	                $.each(rootNodes,function(n,obj){
//	                	zTree.expandNode(obj,true);                    //展开所有根节点
//	                });
	            }
	        }
	    }
	    
	    var _keywords = $(searchField).val();
	   ztreeFilter(zTree,_keywords);                                   //延时执行筛选方法
	   $(".ztree li span.button.bottom_open").css("background-position","-105px -63px");
 }
 
 //展示符合条件的所有节点的父子节点
 function showNodes(nodes,zTree){
     if(nodes && nodes.length>0){                      //对每个节点进行操作
         for(var i=0,len=nodes.length; i<len; i++){
         	zTree.showNode(nodes[i]);                  //显示节点
         	zTree.expandNode(nodes[i],true);           //展开节点
         }
     }
 }
 
 //获取节点的所有子节点信息
 function getAllChildrenNodes(nodes,treeNode){
	 nodes.push(treeNode);
	 if (treeNode.isParent){
			for(var obj in treeNode.children){
				getAllChildrenNodes(nodes,treeNode.children[obj]);
			}
	    }
	 return nodes;
}
 
 //查询zTree节点的所有父节点的name，并拼接成“父级点/子节点”的形式
 function treeNodeNameSplice(treeNode){
	 if(treeNode == null) return "";
	 var parents = treeNode.getPath();
	 var names = "";
	 for(var i=1,len=parents.length; i<len; i++){
		 if(i == 1){
			 names += parents[i].name;
		 }else{
			 names += "/"+parents[i].name;
		 }
	 }
	 return names;
 }
 
//信息提示框
 function showPopover(target, msg) {
 	target.attr("data-original-title", msg);
 	$('[data-toggle="tooltip"]').tooltip();
 	target.tooltip('show');
 	target.attr("data-placement","right");
 	target.focus();
 	setTimeout(function() {
 		target.attr("data-original-title", "");
 		target.tooltip('destroy');
 	}, 2000);
 }
 
 //获取浏览器可见高度
 function getClientHeight(){
     var clientHeight=document.body.clientHeight;//其它浏览器默认值
     if(navigator.userAgent.indexOf("MSIE 6.0")!=-1){
         clientHeight=document.body.clientHeight;
     }else if(navigator.userAgent.indexOf("MSIE")!=-1) {  //IE7 IE8
         clientHeight=document.documentElement.offsetHeight
     }
  
     if(navigator.userAgent.indexOf("Chrome")!=-1){
         clientHeight=document.body.scrollHeight;
     }
  
     if(navigator.userAgent.indexOf("Firefox")!=-1) {
         clientHeight=document.body.scrollHeight;
     }
     return clientHeight;
 }
 
//阻止浏览器的默认行为 
function stopDefault(e) {
     if (e && e.preventDefault){     //阻止默认浏览器动作(W3C) 
    	 e.preventDefault();
     }else{                          //IE中阻止函数器默认动作的方式 
    	 window.event.returnValue = false;
     }
     return false;
 }

//阻止浏览器冒泡
function stopBubble(e) {
    if (e && e.stopPropagation){           //使用W3C的stopPropagation()方法 
    	 e.stopPropagation();
    }else{                                 //使用IE的cancelBubble = true来取消事件冒泡 
    	window.event.cancelBubble = true;
    }
}

//检测当前浏览器是否支持Input file的Accept属性（仅FF和Chrome支持）
function fileAcceptBrowser(){
	//取得浏览器的userAgent字符串
    var userAgent = navigator.userAgent;    
    var chromeIndex = userAgent.indexOf("Chrome");
    var safariIndex = userAgent.indexOf("Safari");
    var edgeIndex = userAgent.indexOf("Edge");
    var firefoxIndex = userAgent.indexOf("Firefox");
    //FF
    if(firefoxIndex>-1 && chromeIndex==-1 && safariIndex==-1 && edgeIndex==-1){
    	return true;
    }
    //Chrome
    if(firefoxIndex==-1 && chromeIndex>-1 && safariIndex>-1 && edgeIndex==-1){
    	return true;
    }
    return false;
}