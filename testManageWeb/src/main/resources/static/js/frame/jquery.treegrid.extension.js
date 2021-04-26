(function ($) {
    "use strict";

    $.fn.treegridData = function (options, param) {
        //如果是调用方法
        if (typeof options == 'string') {
            return $.fn.treegridData.methods[options](this, param);
        }
        
        //如果是初始化组件
        options = $.extend({}, $.fn.treegridData.defaults, options || {});
        var target = $(this);
//        debugger;
        //得到根节点
        target.getRootNodes = function (data) {
            var result = [];
            $.each(data, function (index, item) {
                if (!item[options.parentColumn]) {
                    result.push(item);
                }
            });
            return result;
        };
        var j = 0;
        //递归获取子节点并且设置子节点
        target.getChildNodes = function (data, parentNode, parentIndex, tbody) {
            $.each(data, function (i, item) {
                if (item[options.parentColumn] == parentNode[options.id]) {
                    var tr = $('<tr></tr>');
                    tr.addClass('treegrid-' + (++j));
                    tr.addClass('treegrid-parent-' + parentIndex);
                    $.each(options.columns, function (index, column) {
                        var td = $('<td></td>');
                        if (column.formatter != null && column.formatter != undefined) {
                            td.html(column.formatter(item[column.field],item, index));
                        } else {
                            td.html(item[column.field]);
                        }
                        tr.append(td);
                    });
                    tbody.append(tr);
                    target.getChildNodes(data, item, j, tbody)
                    
                }
            });
        };
        target.addClass('table');
        if (options.striped) {
            target.addClass('table-striped');
        }
        if (options.bordered) {
            target.addClass('table-bordered');
        }
        if (options.url) {
            $.ajax({
                type: options.type,
                url: options.url,
                data: options.ajaxParams,
                dataType: "JSON",
                success: function (data, textStatus, jqXHR) {
                    //构造表头
                    var thr = $('<tr></tr>');
                    $.each(options.columns, function (i, item) {
                        var th = $('<th style="padding:10px;"></th>');
                        th.text(item.title);
                        item.visible== true ? th.hide():th.show();
                        thr.append(th);
                    });
                    var thead = $('<thead></thead>');
                    thead.append(thr);
                    target.append(thead);

                    //构造表体
                    var tbody = $('<tbody></tbody>');
                    var rootNode = target.getRootNodes(data);
                    $.each(rootNode, function (i, item) {
                        var tr = $('<tr></tr>');
                        tr.addClass('treegrid-' + (++j));
                        $.each(options.columns, function (index, column) {
                            var td = $('<td></td>');
                            if(column.formatter != null && column.formatter != undefined){
                            	td.html(column.formatter(item[column.field],item,index));
                            }else{
                            	td.html(item[column.field]);
                            }
                            column.visible== true ? td.hide():td.show();
                            tr.append(td);
                        });
                        tbody.append(tr);
                        target.getChildNodes(data, item, j, tbody);
                    });
                    target.append(tbody);
                    target.treegrid({
                        expanderExpandedClass: options.expanderExpandedClass,
                        expanderCollapsedClass: options.expanderCollapsedClass
                    });
                    if (!options.expandAll) {
                        target.treegrid('collapseAll');
                    }
                    $.each(options.columns, function (i, item) {
                        if(item.visible == true){
                    		target.find("thead>tr>th:nth-child("+(i+1)+")").hide();
                    		target.find("tbody>tr>td:nth-child("+(i+1)+")").hide();
                        } 
                    });
                    
                    $.each(options.columns, function (i, item) {
                        if(item.align == "left"){
                    		target.find("thead>tr>th:nth-child("+(i+1)+")").css("text-align","left");
                    		target.find("tbody>tr>td:nth-child("+(i+1)+")").css("text-align","left");
                        }else if(item.align == "center"){
                    		target.find("thead>tr>th:nth-child("+(i+1)+")").css("text-align","center");
                    		target.find("tbody>tr>td:nth-child("+(i+1)+")").css("text-align","center");
                        } else if(item.align == "right"){
                    		target.find("thead>tr>th:nth-child("+(i+1)+")").css("text-align","right");
                    		target.find("tbody>tr>td:nth-child("+(i+1)+")").css("text-align","right");
                        }
                    });
                }
            });
        }else {
            //构造表头
            var thr = $('<tr></tr>');
            $.each(options.columns, function (i, item) {
                var th = $('<th style="padding:10px;"></th>');
                th.text(item.title);
                thr.append(th);
            });
            var thead = $('<thead></thead>');
            thead.append(thr);
            target.append(thead);

            //构造表体
            var tbody = $('<tbody></tbody>');
            var rootNode = target.getRootNodes(options.data);
            $.each(rootNode, function (i, item) {
                var tr = $('<tr></tr>');
                tr.addClass('treegrid-' + (++j));
                $.each(options.columns, function (index, column) {
                    var td = $('<td></td>');
                    if(column.formatter != null && column.formatter != undefined){
                    	td.html(column.formatter(item[column.field],item,index));
                    }else{
                    	td.html(item[column.field]);
                    }
                    tr.append(td);
                });
                tbody.append(tr);
                target.getChildNodes(options.data, item, j, tbody);
            });
            target.append(tbody);
            target.treegrid({
                expanderExpandedClass: options.expanderExpandedClass,
                expanderCollapsedClass: options.expanderCollapsedClass
            });
            if (!options.expandAll) {
                target.treegrid('collapseAll');
            }
            
            $.each(options.columns, function (i, item) {
                if(item.visible == false){
            		target.find("thead>tr>th:nth-child("+(i+1)+")").hide();
            		target.find("tbody>tr>td:nth-child("+(i+1)+")").hide();
                } 
            });
            
            $.each(options.columns, function (i, item) {
                if(item.align == "left"){
            		target.find("thead>tr>th:nth-child("+(i+1)+")").css("text-align","left");
            		target.find("tbody>tr>td:nth-child("+(i+1)+")").css("text-align","left");
                }else if(item.align == "center"){
            		target.find("thead>tr>th:nth-child("+(i+1)+")").css("text-align","center");
            		target.find("tbody>tr>td:nth-child("+(i+1)+")").css("text-align","center");
                } else if(item.align == "right"){
            		target.find("thead>tr>th:nth-child("+(i+1)+")").css("text-align","right");
            		target.find("tbody>tr>td:nth-child("+(i+1)+")").css("text-align","right");
                }
            });
        }
        return target;
    };

    $.fn.treegridData.methods = {
        getAllNodes: function (target, data) {
            return target.treegrid('getAllNodes');
        },
        destroy: function (target) {
        	target.html('');
        },
        getParentNode:function (target, data){
        	 return target.treegrid('getParentNode');
        },
        getChildNodes:function (target, data){
        	return target.treegrid('getChildNodes');
        },
        expand:function (target, data){
        	target.treegrid('expand');
        },
        collapse:function (target, data){
        	target.treegrid('collapse');
        },
        expandAll:function (target, data){
        	target.treegrid('expandAll');
        },
        collapseAll:function (target, data){
        	target.treegrid('collapse');
        },
        getRootNodes:function (target, data){
        	 return target.treegrid('getRootNodes');
        },
        isLeaf:function (target, data){
        	return target.treegrid('isLeaf');
        },
        isExpanded:function (target, data){
        	return target.treegrid('isExpanded');
        },
        isCollapsed:function (target, data){
        	return target.treegrid('isCollapsed');
        },
        loadData:function (target, data){
            var tbody = $('<tbody></tbody>');
            var rootNode = target.getRootNodes(data);
            $.each(rootNode, function (i, item) {
                var tr = $('<tr></tr>');
                tr.addClass('treegrid-' + (++j));
                $.each(options.columns, function (index, column) {
                    var td = $('<td></td>');
                    if(column.formatter != null && column.formatter != undefined){
                    	td.html(column.formatter(item[column.field],item,index));
                    }else{
                    	td.html(item[column.field]);
                    }
                    tr.append(td);
                });
                tbody.append(tr);
                target.getChildNodes(data, item, j, tbody);
            });
            target.append(tbody);
            target.treegrid({
                expanderExpandedClass: options.expanderExpandedClass,
                expanderCollapsedClass: options.expanderCollapsedClass
            });
        }
    };

    $.fn.treegridData.defaults = {
        id: 'id',
        parentColumn: 'parentId',
        data: [],    //构造table的数据集合
        type: "GET", //请求数据的ajax类型
        url: null,   //请求数据的ajax的url
        ajaxParams: {}, //请求数据的ajax的data属性
        expandColumn: null,//在哪一列上面显示展开按钮
        expandAll: true,  //是否全部展开
        striped: false,   //是否各行渐变色
        bordered: false,  //是否显示边框
        columns: [],
        expanderExpandedClass: 'fa fa-minus-square-o',//展开的按钮的图标
        expanderCollapsedClass: 'fa fa-plus-square-o'//缩起的按钮的图标
    };
})(jQuery);