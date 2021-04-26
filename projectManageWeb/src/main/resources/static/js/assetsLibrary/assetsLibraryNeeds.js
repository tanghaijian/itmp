/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 * 资产库（需求视角）
 */
//全局变量
var mydata = {
    status: "false",
    search: {
        reqArr: [],
        sysArr: [],
        taskArr: [],
    },
    requireId: '',
    treeArr: [],    //用来储存，点击取得树节点及其所有的祖先节点,显示在头部
    sysUrl: "/projectManage/assetsLibraryRq/getSystemDirectory",
    deptUrl: "/projectManage/assetsLibraryRq/getDeptdirectory",
    treeSetting: {   // 树的配置信息
        async: {
            enable: true,
            url: "/projectManage/assetsLibraryRq/getDeptdirectory",
            autoParam: ['id=znodeId', 'type'],
            otherParam: {
                "search": function () {
                    return mydata.status;
                },
                "requireId": function () {
                    return mydata.search.reqArr.join(",");
                },
                "systemId": function () {
                    return mydata.search.sysArr.join(",");
                },
                "reqTaskId": function () {
                    return mydata.search.taskArr.join(",");
                }
            },
            dataFilter: filter
        },
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

function filter(treeId, parentNode, childNodes) {
    childNodes.znodes.map(function (item) {
        item.isParent = item.parent;
    })
    return childNodes.znodes;
}

$(document).ready(function () {
    getTree();
    var testEditormdView2;
    testEditormdView2 = editormd.markdownToHTML("test-editormd-view2", {
        htmlDecode: "style,script,iframe",  // you can filter tags decode
        emoji: true,
        taskList: true,
        tex: true,  // 默认不解析
        flowChart: true,  // 默认不解析
        sequenceDiagram: true,  // 默认不解析
    });
    $("#searchBtn").on("click", function () {
         
        if (mydata.search.reqArr.length != 0 || mydata.search.sysArr.length != 0 || mydata.search.taskArr.length != 0) {
            mydata.status = "true";
			$("#title").text("");
	
			$(".noData").css("display","block");
			$(".myTableDiv").css("display","none");
            getTree();
        } else {
        	mydata.status = false;
            layer.alert("请至少填写一项查询条件", {
                icon: 0,
                title: "提示信息"
            });
        }
    });
    $("#clearBtn").on("click", function () {
        mydata.search.reqArr = [];
        mydata.search.sysArr = [];
        mydata.search.taskArr = [];
        $("#relInfo").val("");
        $("#system").val("");
        $("#devTask").val("");
    });
    //切换视图按钮
    $("#changeViewBtn").on('click', function () {
        //清空
        mydata.search.reqArr = [];
        mydata.search.sysArr = [];
        mydata.search.taskArr = [];
		
		$("#relInfo").val("");
        $("#system").val("");
        $("#devTask").val("");
        $("#title").text("");

		$(".noData").css("display","block");
		$(".myTableDiv").css("display","none");
		 mydata.status = false;
		if ($("#headr").hasClass("dept_v")) {
            $("#headr").removeClass("dept_v").addClass("system_v");
            $("#headr").text("资产库（系统视图）");
			$("#changeViewBtn").text("切换部门视角");
			mydata.treeSetting.async.url = mydata.sysUrl;
        } else {
            $("#headr").removeClass("system_v").addClass("dept_v");
            $("#headr").text("资产库（部门视图）");
			$("#changeViewBtn").text("切换系统视角");
			mydata.treeSetting.async.url = mydata.deptUrl;
        }
		getTree();
    })
    //选择关联信息
    $("#relInfo").on('click', function () {
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
            content: '/projectManageui/assetRq/toAssociatedDemand',
            btn: ['确定', '取消'],
            btnAlign: 'c', //按钮居中
            yes: function (index, layero) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var obj = iframeWin.getAssociatedDemandId();//调用子窗口的方法，获取选择的关联需求ID
                if (obj.status == true) {
                    mydata.search.reqArr = [];
                    var str = '';
                    obj.data.map(function (item) {
                        mydata.search.reqArr.push(item.id);
                        str += item.REQUIREMENT_NAME + ",";
                    });
                    $("#relInfo").val(str);
                    layer.closeAll();
                }
            },
            btn2: function () {
                layer.closeAll();
            }
        });
    })
    //点击选择系统 跳出选择系统页面
    $("#system").on('click', function () {
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
            content: '/projectManageui/requirementPerspective/toSystem',
            btn: ['确定', '取消'],
            btnAlign: 'c', //按钮居中
            yes: function (index, layero) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var obj = iframeWin.getSystemid();
                if (obj.status == true) {
                    mydata.search.sysArr = [];
                    var str = '';
                    obj.data.map(function (item) {
                        mydata.search.sysArr.push(item.id);
                        str += item.systemName + ",";
                    });
                    $("#system").val(str);
                    layer.closeAll();
                }
            },
            btn2: function () {
                layer.closeAll();
            }
        });
    })
    //选择开发任务
    $("#devTask").on('click', function () {
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
            content: '/projectManageui/assetRq/toDevelopmentTask',
            btn: ['确定', '取消'],
            btnAlign: 'c', //按钮居中
            yes: function (index, layero) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var obj = iframeWin.getDevTaskId();
                if (obj.status == true) {
                    mydata.search.taskArr = [];
                    var str = '';
                    obj.data.map(function (item) {
                        mydata.search.taskArr.push(item.id);
                        str += item.FEATURE_NAME + ",";
                    });
                    $("#devTask").val(str);
                    layer.closeAll();
                }
            },
            btn2: function () {
                layer.closeAll();
            }
        });
    })
    //点击版本历史 跳出版本历史页面
    $("#history").on('click', function () {
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
            content: '/projectManageui/requirementPerspective/toAssociatedDemand',
            btn: ['确定', '取消'],
            btnAlign: 'c', //按钮居中
            yes: function (index, layero) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var id = iframeWin.getSystemid();
                console.log(id);
            },
            btn2: function () {
                layer.closeAll();
            }
        });
    })
    //点击更多附件 跳出附件页面
    $("#appendixMore").on('click', function () {
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
            content: '../../assetsLibrary/appendix.html',
            btn: ['确定', '取消'],
            btnAlign: 'c', //按钮居中
            yes: function (index, layero) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var id = iframeWin.getSystemid();
            },
            btn2: function () {
                layer.closeAll();
            }
        });
    })
    //点击更多关联信息  跳出关联信息页面
    $("#relevanceInfoMore").on('click', function () {
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
            content: '../../assetsLibrary/relevanceInfo.html',
            btn: ['确定', '取消'],
            btnAlign: 'c', //按钮居中
            yes: function (index, layero) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];  //得到iframe页的窗口对象，执行iframe页的方法：
                var id = iframeWin.getSystemid();
            },
            btn2: function () {
                layer.closeAll();
            }
        });
    })
    $("#chapters_select").change(function(){
	  var opt=$("#chapters_select").val();
	  var type=$("#chapters_select").attr('type');
	  get_version_ass(opt, type,2);
	});
});

//菜单栏
function getTree() {
    $.fn.zTree.init($("#menuTree"), mydata.treeSetting);
    fuzzySearch('menuTree', '#treeSearchInput', null, true); //初始化模糊搜索方法
}

//点击判断 非需求目录 需求目录
function getDocumentTable(event, treeId, treeNode, clickFlag) {
    if (treeNode.docType == null) {
        //非需求目录
        $(".noData").css("display", "block");
        $(".myTableDiv").css("display", "none");
    } else {
        $(".noData").css("display", "none");
        $(".myTableDiv").css("display", "block");
        //设置标题
        mydata.treeArr = [];
        getParentNameArr(treeNode);
        $("#title").text(mydata.treeArr.join(" > "));

        getTable(treeNode);
    }
    /*
    //生成表格
   	$(".notRequirement").css("display","none");
   	$(".requirement").css("display","flex");
   */
}

// 非需求目录 --> 生成标题
function getParentNameArr(treeNode) {
    mydata.treeArr.unshift(treeNode.name)
    if (treeNode.level == 0) {
        return;
    } else {
        var parentObj = treeNode.getParentNode();
        getParentNameArr(parentObj);
    }
}

// 非需求目录 --> 形成表格
function getTable(treeNode) {
    mydata.requireId = treeNode.requireId;
    $("#loading").css('display', 'block');
    $("#documentList").bootstrapTable('destroy');
    $("#documentList").bootstrapTable({
        url: "/projectManage/assetsLibraryRq/getDocuments",
        method: "post",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType: "",
        pagination: false,
        sidePagination: "server",
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 25, 50, 100],
        queryParams: function (params) {
            var param = {
                requireId: treeNode.requireId,
                docType: treeNode.docType,
                systemId: treeNode.systemId,
            };
            return param;
        },
        responseHandler: function (res) {
            return res.documents;
        },
        columns: [{
            field: "id",
            title: "id",
            visible: false,
            align: 'center'
        }, {
            field: "documentName",
            title: "文档名称",
            align: 'left',
            formatter: function (value, row, index) {
                var a;
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                if (row.saveType == 1) {
                    var url = "/projectManage/assetsLibraryRq/downObject?documentS3Bucket=" + encodeURIComponent(row.documentS3Bucket) + "&documentS3Key=" + encodeURIComponent(row.documentS3Key) + "&documentName=" + encodeURIComponent(row.documentName);
                    a = '<a class="a_style" href="' + url + '" download="' + value + '">' + value + '</a><span title="附件类文件，点击可以下载" class="downfilesIcon"></span>';
                } else if (row.saveType == 2){
                    a = '<a class="a_style"  onclick="showMakeDown(\'' + row.id + '\')">' + value + '</a><span title="Markdown文件" class="marksIcon"></span>';
                }
                return a;
            }
        }, {
            field: "createDate",
            title: "创建信息",
            align: 'center',
            formatter: function (value, row, index) {
                return isValueNull(row.createUserName) + " " + isValueNull(row.createDate);
            }
        },
        {
            field: "systemName",
            title: "所属系统",
            align: 'center',

        },


        {
            field: "lastUpdateDate",
            title: "最后更新时间",
            align: 'center',
            formatter: function (value, row, index) {
                return isValueNull(row.updateUserName) + " " + isValueNull(row.lastUpdateDate);
            }
        }, {
            field: "opt",
            title: "操作",
            align: 'center',
            formatter: function (value, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                if(row.saveType == 2){ //文档
                	return '<a class="a_style" onclick="getRelevanceInfo(' + rows + ')">关联信息</a>' + ' | '
	                    + '<a class="a_style" onclick="getHistory(' + rows + ')">历史版本</a>' + ' | '
	                    + '<a class="a_style" onclick="select_version(' + rows+ ')">版本分析</a>';
                }else if(row.saveType == 1){ //附件
                	return '<a class="a_style" onclick="getRelevanceInfo(' + rows + ')">关联信息</a>' + ' | '
                    + '<a class="a_style" onclick="getHistory(' + rows + ')">历史版本</a>';
                }
            }
        }],
        onLoadSuccess: function (data) {
            $("#loading").css('display', 'none');
        },
        onLoadError: function () {
            $("#loading").css('display', 'none');
            layer.alert("系统内部错误!", {
                icon: 2,
                title: "提示信息"
            });
        },
        onPageChange: function () {
            $("#loading").css('display', 'block');
        }
    });
}

//跳转 查看页面
function showMakeDown(id) {
    //status( 1为查看  2为编辑  )
    //type (2  只有查看功能)
    var url = '/projectManageui/requirementPerspective/toEditAndCheckMarkDown?id=' + id + ',status=1,type=2';
    openMakeDownPage(url)
}

function openMakeDownPage(url) {
    layer.open({
        type: 2,
        title: false,
        move: false,
        closeBtn: 0,
        area: ['100%', '100%'],
        shade: 0.3,
        tipsMore: true,
        anim: 2,
        content: url,
    });
    $("#newMKDocDiv").modal("hide");
    $("#loading").hide();
}

function addDiyDom(treeId, treeNode) {
    if (treeNode.id == 'a112') {
        var IDMark_A = "_a";
        var aObj = $("#" + treeNode.tId + IDMark_A);
        var showStr = '<span onfocus="this.blur();"><span class="button fileIcon"></span></span>';
        aObj.append(showStr);
    }
}

function getRelevanceInfo(row) {
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
        btn: ['关闭'],
        btnAlign: 'c', //按钮居中 
        btn: function () {
            layer.closeAll();
        }
    });
}

function getHistory(row) {
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
        btn: ['关闭'],
        btnAlign: 'c', //按钮居中 
        btn: function () {
            layer.closeAll();
        }
    });
}

//版本对比
function select_version(rows) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/projectManage/documentChapters/getVersionContrast",
        type: "post",
        data: {
            systemDirectoryDocumentId: rows.id,
            requirementCode: rows.requirementCode
        },
        success: function (data) {
            if (data.status == 1) {
                $('#versions_comparison_tit').text(data.name);
                $('#document_tit').text(rows.documentName);
                $('#document_tit2').text(rows.documentName);
                $('#versions_comparison_tit').text(rows.documentName);
                var insertList = `<td class="a_style" style="cursor: pointer;">${data.insertNum}</td>`;
                if(data.insertList.length){
                	insertList = `
                		<td class="a_style" onclick='get_version_ass(${JSON.stringify(data.insertList)},1,1)' style="cursor: pointer;">${data.insertNum}</td>
                	`;
                }
                var updateList = `<td class="a_style" style="cursor: pointer;">${data.updateNum}</td>`;
                if(data.updateList.length){
                	updateList = `
                		<td class="a_style" onclick='get_version_ass(${JSON.stringify(data.updateList)},2,1)' style="cursor: pointer;">${data.updateNum}</td>
                	`;
                }
                var deleteList = `<td class="a_style" style="cursor: pointer;">${data.deleteNum}</td>`;
                if(data.deleteList.length){
                	deleteList = `
                		<td class="a_style" onclick='get_version_ass(${JSON.stringify(data.deleteList)},3,1)' style="cursor: pointer;">${data.deleteNum}</td>
                	`;
                }
                $('#versions_comparison_tbody').html(`
                    <tr>
                        <th>添加的条目数量</th>
                        ${insertList}
                        <td>${data.insertPer}</td>
                    </tr>
                    <tr>
                        <th>修改的条目数量</th>
                        ${updateList}
                        <td>${data.updatePer}</td>
                    </tr>
                    <tr>
                        <th>删除的条目数量</th>
                        ${deleteList}
                        <td>${data.deletePer}</td>
                    </tr>
                    <tr>
                        <th>合计</th>
                        <td style="cursor: pointer;">${data.sumNum}</td>
                        <td>${data.sumPer}</td>
                    </tr>
                `);
                $('#versions_comparison_Modal').modal('show');
            }
            $("#loading").css('display', 'none');
        }
    })
}

//版本对比详情
function get_version_ass(arr, type ,status) {
    $("#loading").css('display', 'block');
    let re_id = arr[0].id;
    if(status == 2){
    	re_id = arr;
    }else{
    	if(!arr.length){
    		layer.alert('暂无条目对比!',{icon:0})
    		return;
    	}else{
    		$('#chapters_select').empty();
    	    $('#chapters_select').removeAttr('type');
    		$('#chapters_select').attr('type',type);
    		arr.map(v=>{
    			$('#chapters_select').append(`
    	    			<option value="${v.id}">${v.chapterName}</option>
    	    	`);
    		})
    	}
    }
    $.ajax({
        url: "/projectManage/documentLibrary/selectChaptersMarkDownContrast",
        type: "post",
        data: {
            type,
            id:re_id  //章节id
        },
        success: function (msg) {
        	if(msg.status == 1){
	        	$('#createUserName').text(isValueNull(msg.latestVersion.chapters.checkoutUserName));
	        	$('#createDate').text(isValueNull(msg.latestVersion.chapters.lastUpdateDate));
	            $('#test-editormd-view55').html('');
	            if (msg.lastVersion) {
	                $('#test-editormd-view55').append('<textarea id="append-test55">' + isValueNull(msg.lastVersion) + '</textarea>');
	            }
	            editormd.markdownToHTML("test-editormd-view55", {
	                htmlDecode: "style,script,iframe",
	                emoji: true,
	                taskList: true,
	                tex: true,  // 默认不解析
	                flowChart: true,  // 默认不解析
	                sequenceDiagram: true,  // 默认不解析
	            });
	            $('#test-editormd-view66').html('');
	            if (msg.latestVersion.markdown) {
	                $('#test-editormd-view66').append('<textarea id="append-test66">' + isValueNull(msg.latestVersion.markdown) + '</textarea>');
	            }
	            editormd.markdownToHTML("test-editormd-view66", {
	                htmlDecode: "style,script,iframe",
	                emoji: true,
	                taskList: true,
	                tex: true,  // 默认不解析
	                flowChart: true,  // 默认不解析
	                sequenceDiagram: true,  // 默认不解析
	            });
	            $('#versions_detail_Modal').modal('show');
        	}
            $("#loading").css('display', 'none');
        }
    })

}
