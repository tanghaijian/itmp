/**
 * Description: 缺陷管理JavaScript
 * Author:liushan
 * Date: 2018/12/10 下午 2:21
 */

var userModalStatus = '', testTaskModalStatus = '', submitDefectStatus = '', systemModalStatus = '',
    defectStatusList = '', solveStatusList = '', rejectionList = '', severityLevelList = '', emergencyLevelList = '',
    defectTypeList = '', defectSourceList = '', fileTableName = '';
var _flag = 1;
var editAttList = [], formFileList = [], testtaskStatusList = [];
var defectInfoForExportObj = {};
//图文混编编辑器
var editor;   		//新建页面
var editEditor;     //编辑页面

 var rootCauseAnalysisEditorForAdd;//新增页面根本原因分析编辑器
 var rootCauseAnalysisEditorForEdit;//编辑页面根本原因分析编辑器


var errorDefect = '系统内部错误，请联系管理员 ！！！';
var noPermission = '没有操作权限 ！！！';
var defectUrl = '/testManage/';

var jiantou = "<span>&nbsp;&nbsp;修改为&nbsp;&nbsp;</span>";

var _top = '';//更多操作
// var scrollAction = { x: 'undefined', y: 'undefined' }, scrollDirection;
var scroll_flag = false;//更多操作滚动
var systemModal_flag = false;

var contentArr = []; // 搜索条件
var filter = '';// 页面初始化参数
var favoriteContent = '';// 初始筛选器内容
var isBegain = true; //判断是否第一次进入

var parameterArr = {};
var flagNum="";//判断是否通过处理数据后刷新列表
parameterArr.arr = window.location.href.split("?");
parameterArr.parameterArr = parameterArr.arr[1]?parameterArr.arr[1].split("&"):[];
parameterArr.obj = {};
for (var i = 0; i < parameterArr.parameterArr.length; i++) {
    var obj = parameterArr.parameterArr[i].split("=");
    parameterArr.obj[obj[0]] = obj[1];
}

$(document).ready(function () {
	getFilter();
    getExsitParam();
	initTable();
	search_settings();
    _select_box_show();
    select_project();
    defectStatusList = $("#defectStatus").find("option");
    solveStatusList = $("#opt_solution").find("option");
    rejectionList = $("#opt_rejection").find("option");
    severityLevelList = $("#severityLevel").find("option");
    emergencyLevelList = $("#emergencyLevel").find("option");
    defectTypeList = $("#defectType").find("option");
    defectSourceList = $("#defectSource").find("option");
    testtaskStatusList = $("#testTaskStatus").find("option");

    $("#loading").css('display', 'block');
    var reqFid = $("#reqFiD").val();
    var reqFName = $("#reqFName").val(); 
    if( parameterArr.obj.userId == undefined ){
    	if (( reqFid == "NULL" || reqFid == "" || reqFid == "$reqFiD" ) && ( reqFName == "NULL" || reqFName == "" || reqFName == "$reqFName") && $("#workTaskCode").val() == "" ) {
            $("#reqFiD").val("");
            $("#reqFName").val("");
            window.addEventListener('pageshow', function (event) {
                if (window.performance.navigation.type != 2) {
                    getCollection();
                }
            });
        } else {
            var defectStatus_sel = [2, 3, 4, 5, 7, 8, 9, 10, 11];
            $("#defectStatus option").each(function (i, n) {
                for (var ii = 0, len = defectStatus_sel.length; ii < len; ii++) {
                    if (n.value == defectStatus_sel[ii]) {
                        n.selected = true;
                    }
                }
            });
            $("#defectSource option").each(function (i, n) {
                for (var ii = 0, len = defectStatus_sel.length; ii < len; ii++) {
                    if (n.value == statusVal) {
                        n.selected = true;
                    }
                }
            });
        }
    } else{
    	$("#reqFName").val("")
    }
    
    urlParam(); 
    
    uploadFileList();
    edit_uploadList();
    opt_uploadFile();
    dateComponent();
    //搜索操作单元事件
//    var __click_handle = {
//        new_assignUserName: 'new', /*新建人员*/
//        new_testUserName: 'new2',//新建测试人
//        new_developer: 'new3',
//        edit_assignUserName: 'edit', /*编辑人员*/
//        edit_testUserName: 'edit2',//编辑测试人
//        edit_developer: 'edit3',
//        submitUserName: 'select', /*查询人员*/
//        assignUser: 'select2',
//        testUser: 'select3',
//        developer: 'select4',
//        donginguser: 'workSelect', /*工作任务 查询测试人员*/
//        opt_assignUserName: 'opt', /*转交操作，人员展示*/
//    }
//    for (var key in __click_handle) {
//        (function (key, param) {
//            $("#" + key).click(function () {
//                userModalStatus = param;
//                userInit();
//            });
//        })(key, __click_handle[key])
//    }

    /*查询系统*/
    $("#systemName").click(function () {
        systemModalStatus = "select";
        systemTableShow(false);
        $("#system_select_btn").attr("onclick",'systemTableShow(false)');
        $("#systemModal").modal("show");
    });

    // 新建 系统
    $("#system_Name").click(function () {
        systemModalStatus = "new";
        if(!$("#newProjectId").val()){
            layer.alert('请先选择项目!',{icon:0})
            return;
        }
        sysReset();
//        get_project_system(1);
        project_systemTableShow(true);
        $("#system_select_btn").attr("onclick",'project_systemTableShow(true)');
    });

    // 编辑 系统
    $("#edit_system_Name").click(function () {
        systemModalStatus = "edit";
        if(!$("#editProjectId").val()){
            layer.alert('请先选择项目!',{icon:0})
            return;
        }
        sysReset();
//        get_project_system(2);
        project_systemTableShow(true);
        $("#system_select_btn").attr("onclick",'project_systemTableShow(true)');
    });


    /*查询需求*/
    $("#requirementName").click(function () {
        reqInit();
    });

    /*工作任务弹框*/
    $("#testTaskName").click(function () {
        testTaskInit();
    });

    $("#edit_testTaskName").click(function () {
        testTaskInit();
    });
    /*投产窗口*/
    $("#windowName").click(function () {
        ComWindowInit();
    });
    $("#new_windowName").click(function () {
        ComWindowInit2();
    });
    $("#edit_windowName").click(function () {
        if ($(this).hasClass("canntClick")) {
            return;
        }
        ComWindowInit3();
    });
    
    $("[name=filter]").change(function(){
		var value = $(this).val();
		if(value == "1"){
			$(".newFilter").removeClass('hide');
			$(".editFilter").addClass('hide');
			$(".fieldTable").removeClass('hide');
			$("#configFilter").selectpicker('val',"");
		}else if(value == "2"){
			$(".editFilter").removeClass('hide');
			$(".newFilter").addClass('hide');
			$(".fieldTable").removeClass('hide');
		}else{
			$(".editFilter").removeClass('hide');
			$(".newFilter").addClass('hide');
			$(".fieldTable").addClass('hide');
		}
	});
	
	$('#configFilter').on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue) {
		var id = $(this).selectpicker('val');
		if(id == ""){
			return;
		}
			var inputs = $("[id*='sh_']");
			$.each(inputs,function(index,dom){
				$(dom).val("");
			});
			$("#loading").css('display', 'block');
			$.ajax({
				type: "POST",
				url: "/report/defectReport/selectDefectReportById",
				dataType: "json",
				asycn:false,
				data: {
					"menuUrl": "/testManageui/defect/toDefect",
					"id": id
				},
				success: function (data) {
					$("#loading").css('display', 'none');
					var content = data.favoriteContent;
					if(content != ""){
						var searchArr = JSON.parse(content);
						$.each(searchArr,function(index,value){
							$("#sh_"+value.field).val(value.value);
						});
					}
					
				}
			});
	});
	
	$('#filterType').on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue) {
		var id = $(this).selectpicker('val');
		if(id == ""){
			return;
		}
			var inputs = $("[id*='gs_']");
			$.each(inputs,function(index,dom){
				$(dom).val("");
			});
			$("#loading").css('display', 'block');
			$.ajax({
				type: "POST",
				url: "/report/defectReport/selectDefectReportById",
				dataType: "json",
				data: {
					"menuUrl": "/testManageui/defect/toDefect",
					"id": id
				},
				success: function (data) {
					$("#loading").css('display', 'none');
					var content = data.favoriteContent;
					var filterParam = getfilterParam(content);
					searchInfo(JSON.stringify(filterParam));
				}
			});
	});

    //常规用法
    laydate.render({
        elem: '#submitDate',
        btns: ['now', 'confirm'],
        trigger: 'click',
        done: function (value, date, endDate) {
            $("#submitDate").next().css("display", "block");
        }
    });
    formValidator();
    refactorFormValidator();
    addCheckBox();

    //富文本编辑初始化
    var E = window.wangEditor;

    editor = new E('#defectOverview');
    editor.customConfig.uploadImgShowBase64 = true;
    editor.customConfig.pasteIgnoreImg = true;
    editor.customConfig.menus = false;
    editor.customConfig.pasteTextHandle = function (content) {
        // content 即粘贴过来的内容（html 或 纯文本），可进行自定义处理然后返回
        if (content == '' && !content) return ''
        var str = content
        str = str.replace(/<xml>[\s\S]*?<\/xml>/ig, '')
        str = str.replace(/<style>[\s\S]*?<\/style>/ig, '')
        str = str.replace(/<\/?[^>]*>/g, '')
        str = str.replace(/[ | ]*\n/g, '\n')
        str = str.replace(/&nbsp;/ig, '')
        return str
    }
    editor.create();
    editor.$textElem.attr('contenteditable', true)

    editEditor = new E('#edit_defectOverview');
    editEditor.customConfig.uploadImgShowBase64 = true;
    editEditor.customConfig.pasteIgnoreImg = true;
    editEditor.customConfig.menus = false;
    editEditor.customConfig.pasteTextHandle = function (content) {
        // content 即粘贴过来的内容（html 或 纯文本），可进行自定义处理然后返回
        if (content == '' && !content) return ''
        var str = content
        str = str.replace(/<xml>[\s\S]*?<\/xml>/ig, '')
        str = str.replace(/<style>[\s\S]*?<\/style>/ig, '')
        str = str.replace(/<\/?[^>]*>/g, '')
        str = str.replace(/[ | ]*\n/g, '\n')
        str = str.replace(/&nbsp;/ig, '')
        return str
    }
    editEditor.create();
    editEditor.$textElem.attr('contenteditable', true)

    rootCauseAnalysisEditorForAdd = new E("#rootCauseAnalysis");
    rootCauseAnalysisEditorForAdd.customConfig.uploadImgShowBase64 = true;
    rootCauseAnalysisEditorForAdd.customConfig.pasteIgnoreImg = true;
    rootCauseAnalysisEditorForAdd.customConfig.menus = false;
    rootCauseAnalysisEditorForAdd.customConfig.pasteTextHandle = function (content) {
        // content 即粘贴过来的内容（html 或 纯文本），可进行自定义处理然后返回
        if (content == '' && !content) return ''
        var str = content
        str = str.replace(/<xml>[\s\S]*?<\/xml>/ig, '')
        str = str.replace(/<style>[\s\S]*?<\/style>/ig, '')
        str = str.replace(/<\/?[^>]*>/g, '')
        str = str.replace(/[ | ]*\n/g, '\n')
        str = str.replace(/&nbsp;/ig, '')
        return str
    }
    rootCauseAnalysisEditorForAdd.create();
    rootCauseAnalysisEditorForAdd.$textElem.attr('contenteditable', true);

    rootCauseAnalysisEditorForEdit = new E("#edit_rootCauseAnalysis");
    rootCauseAnalysisEditorForEdit.customConfig.uploadImgShowBase64 = true;
    rootCauseAnalysisEditorForEdit.customConfig.pasteIgnoreImg = true;
    rootCauseAnalysisEditorForEdit.customConfig.menus = false;
    rootCauseAnalysisEditorForEdit.customConfig.pasteTextHandle = function (content) {
        // content 即粘贴过来的内容（html 或 纯文本），可进行自定义处理然后返回
        if (content == '' && !content) return ''
        var str = content
        str = str.replace(/<xml>[\s\S]*?<\/xml>/ig, '')
        str = str.replace(/<style>[\s\S]*?<\/style>/ig, '')
        str = str.replace(/<\/?[^>]*>/g, '')
        str = str.replace(/[ | ]*\n/g, '\n')
        str = str.replace(/&nbsp;/ig, '')
        return str
    }
    rootCauseAnalysisEditorForEdit.create();
    rootCauseAnalysisEditorForEdit.$textElem.attr('contenteditable', true);

    //搜索展开和收起
    downOrUpButton();

    // 所有的Input标签，在输入值后出现清空的按钮
    buttonClear();
    tableMouseover("#defectList");

    var formArr = ["rejectDivForm"];
    banEnterSearch();
    
    addTitle();
});

//更多操作弹窗
$(document).scroll(function (e) {
	var scrolltop = $(document).scrollTop();
	var top = $('._select_box_menu').css('top');
	if (scroll_flag) {
        // scrollFunc();
        // if (scrollDirection == 'down') {
		// 	//页面向下滚动要做的事情
        //     $('._select_box_menu').css('top', _top - scrolltop + 'px');
		// }
		// else if (scrollDirection == 'up') {
        //     //页面向上滚动要做的事情     
        //     $('._select_box_menu').hide();	
        // }
        
    }
    $('._select_box_menu').hide();
});
//判断页面滚动方向
// function scrollFunc() {
// 	if (typeof scrollAction.x == 'undefined') {
// 		scrollAction.x = window.pageXOffset;
// 		scrollAction.y = window.pageYOffset;
// 	}
// 	var diffX = scrollAction.x - window.pageXOffset;
// 	var diffY = scrollAction.y - window.pageYOffset;
// 	if (diffX < 0) {
// 		// Scroll right
// 		scrollDirection = 'right';
// 	} else if (diffX > 0) {
// 		// Scroll left
// 		scrollDirection = 'left';
// 	} else if (diffY < 0) {
// 		// Scroll down
// 		scrollDirection = 'down';
// 	} else if (diffY > 0) {
// 		// Scroll up
// 		scrollDirection = 'up';
// 	} else {
// 		// First scroll event
// 	}
// 	scrollAction.x = window.pageXOffset;
// 	scrollAction.y = window.pageYOffset;
// }
//更多操作弹窗
function _select_box_show() {
	$(document).on('click', '._select_box_show', function (e) {
        $('._select_box_menu').hide();
		var top = $(this).offset().top - $(document).scrollTop();
        var left = $(this).offset().left - $(document).scrollLeft();
		$(this).next().css({
			position: 'fixed',
			top: top + $(this).height() + 'px',
			left: left - 15 + 'px',
		});
		_top = top + $(this).height();
		scroll_flag = true;
		
		if($(this).hasClass('active')){
			$(this).next('._select_box_menu').hide();
			$(this).removeClass('active').children('span').removeClass('fa-angle-down').addClass('fa-angle-up');
			scroll_flag = false;
		}else{
			$(this).addClass('active').children('span').removeClass('fa-angle-up').addClass('fa-angle-down');
			$(this).next('._select_box_menu').show();
		}
	})
	$(document).on('click', function (e) {
        if(!$(e.target).hasClass('_select_box_show') && !$(e.target).hasClass('_select_box_menu')){
            $('._select_box_menu').hide();
            $('._select_box_menu').prev().removeClass('active').children('span').removeClass('fa-angle-down').addClass('fa-angle-up');
        }
	})
}

function selectMyselfDefect(that) {
    $($(that).parent().parent().children('input[type^="hidden"]')).val(currentUserId);
    $($(that).parent().parent().children('input[type^="text"]')).val(currentUserName);
}

// 时间格式
function dateComponent() {
    var locale = {
        "format": 'yyyy-mm-dd',
        "separator": " -222 ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "起始时间",
        "toLabel": "结束时间'",
        "customRangeLabel": "自定义",
        "weekLabel": "W",
        "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
        "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        "firstDay": 1
    };
    $("#timeDate").daterangepicker({'locale': locale});
}

var colNameArr = ['id','缺陷编号','紧急程度', '缺陷摘要', '缺陷状态','缺陷类型','缺陷来源','缺陷等级', '所属需求', '测试任务', '工作任务', '涉及系统', '投产窗口', '修复轮次', '主修复人', '测试人', ' 提出人', ' 开发人员', ' 操作'];
var colIndex = ['id','defectCode','emergencyLevelStr','defectSummary','defectStatusList','defectTypeStr','defectSourceStr','severityLevelStr','requirementCode','featureCode','testTaskCode','systemName','windowName','repairRoundStr','assignUserName','testUserName','submitUserName','developUserName'];
/*缺陷列表*/
function initTable() {
	console.log( $("#defectStatus").val() );
    var page = $('#defectList').getGridParam('page');
    $("#defectList").jqGrid("clearGridData");
    $("#defectList").jqGrid("setGridParam", {page: page != null && page != undefined ? page : 1});

    $("#defectList").jqGrid({
        url: defectUrl + "defect/list",
        datatype: 'json',
        mtype: "POST",
        height: '100%',
        rowNum: 10,
        rowTotal: 200,
        rowList: [10, 30, 50, 100],
        rownumWidth: 40,
        pager: '#defectListPager',
        sortable: true,   //是否可排序
        sortname: 'id',
        sortorder: 'desc',
        loadtext: "数据加载中......",
        viewrecords: true, //是否要显示总记录数
        
        autowidth: true,
        //显示水平滚动条
        altRows: true,
        altclass: 'differ',
        shrinkToFit:false,
        autoScroll: true,
        postData: {
        	"filters":filter
        },
        colNames: colNameArr,
        colModel: [{ name: 'id', index: 'id', hidden: true },
        	{
            name: "defectCode",
            index: "defectCode",
            align: 'center',
            width: 110,
            searchoptions: {sopt: ['cn']},
            formatter: function (value, grid, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                return '<a  class="a_style" onclick="checkDefect(' + row.id + ')">' + row.defectCode + '</a>'
            }
        },{
            name: "emergencyLevelStr",
            index: "emergencyLevelStr",
            align: 'center',
            width: 80,
            searchoptions: {
                sopt: ['cn']
            },
            formatter: function (value, grid, row, index) {
                var classColor1 = 'classColor';
                for (var i = 0, len = emergencyLevelList.length; i < len; i++) {
                    if (row.emergencyLevel == emergencyLevelList[i].value) {
                        classColor1 += row.emergencyLevel;
                        return "<span class=" + classColor1 + ">" + emergencyLevelList[i].innerHTML + "</span>";
                    }
                }
            }
        }, {
            name: "defectSummary",
            index: "defectSummary",
            align: 'left',
            width: 250,
            searchoptions: {sopt: ['cn']},
            formatter: function (value, grid, row, index) {
                return "<span>" + row.defectSummary + "</span></div>";
            }
        }, {
            name: "defectStatusList",
            index: "defectStatusList",
            align: 'center',
            width: 100,
            searchoptions: {
                sopt: ['cn']
            },
            formatter: function (value, grid, row, index) {
                for (var i = 0, len = defectStatusList.length; i < len; i++) {
                    if (row.defectStatus == defectStatusList[i].value) {
                        var _status = "<input type='hidden' id='list_defectStatus' value='" + defectStatusList[i].innerHTML + "'>";
                        return defectStatusList[i].innerHTML + _status
                    }
                }
            }
        },{
            name: "defectTypeStr",
            index: "defectTypeStr",
            width: 150,
            align: 'center',
            searchoptions: {sopt: ['cn']}
        },{
            name: "defectSourceStr",
            index: "defectSourceStr",
            width: 150,
            align: 'center',
            searchoptions: {sopt: ['cn']}
        },{
            name: "severityLevelStr",
            index: "severityLevelStr",
            width: 150,
            align: 'center',
            searchoptions: {sopt: ['cn']}
        }, {
            name: "requirementCode",
            index: "requirementCode",
            align: 'center',
            width: 110,
            searchoptions: {sopt: ['cn']}
        }, {
            name: "featureCode",
            index: "featureCode",
            align: 'center',
            width: 110,
            searchoptions: {sopt: ['cn']},
            formatter: function (value, grid, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                var code = '';
                if (row.featureCode != null && row.featureCode != undefined) {
                    code = row.featureCode;
                }
                return '<a  class="a_style" onclick="showtestTask(' + row.featureId + ')">' + code + '</a>'
            }
        }, {
            name: "testTaskCode",
            index: "testTaskCode",
            align: 'center',
            width: 150,
            searchoptions: {sopt: ['cn']},
            formatter: function (value, grid, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                var code = '';
                if (row.testTaskCode != null && row.testTaskCode != undefined) {
                    code = row.testTaskCode;
                }
                return '<a  class="a_style" onclick="getSee(' + row.testTaskId + ')">' + code + '</a>'
            }
        }, {
            name: "systemName",
            index: "systemName",
            width: 150,
            align: 'center',
            searchoptions: {sopt: ['cn']}
        }, {
            name: "windowName",
            index: "windowName",
            width: 110,
            align: 'center',
            searchoptions: {sopt: ['cn']}
        }, {
            name: "repairRoundStr",
            index: "repairRoundStr",
            width: 70,
            align: 'center',
            searchoptions: {sopt: ['cn']}
        }, {
            name: "assignUserName",
            index: "assignUserName",
            width: 70,
            align: 'center',
            searchoptions: {sopt: ['cn']}
        }, {
            name: "testUserName",
            index: "testUserName",
            width: 70,
            align: 'center',
            searchoptions: {sopt: ['cn']}
        }, {
            name: "submitUserName",
            index: "submitUserName",
            width: 150,
            align: 'center',
            searchoptions: {sopt: ['cn']},
            formatter: function (value, grid, row, index) {
                value = value == null ? '' : value + "<span>&nbsp;|&nbsp;</span>";
                return value + row.submitDate;
            }
        }, {
            name: "developUserName",
            index: "developUserName",
            width: 70,
            align: 'center',
            searchoptions: {sopt: ['cn']}
        }, {
            name: "opt",
            index: "opt",
            align: 'center',
            width: 100,
            search: false,
            sortable: false,
            resize: false,
            formatter: function (value, grid, row, index) {
                var flag = "false";
                if (row.userSet != null) {
                    for (var i = 0; i < row.userSet.length; i++) {
                        if (row.userSet[i] == currentUserId) {
                            flag = "true";
                        }
                    }
                }
                var span_ = "<span>&nbsp;|&nbsp;</span>";
                var a = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity( ' + row.id + ',' + 1 + ',' + flag + ' )">编辑</a>';//不要
                var a1 = '<a  href="javascript:void(0);" class="edit-opt" onclick="checkDefect(' + row.id + ')">查看</a>';//不要

                var g = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 3 + ')">撤销</a>';
                var h = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 4 + ')">关闭</a>';

                var opt_status = [];

                if (row.submitUserId == currentUserId || row.assignUserId == currentUserId || flag == "true" || row.testUserId == currentUserId) {
                    if (defectEdit == true) {
                        opt_status.push(a);
                    }
                    // 新建状态:编辑 提交 撤销 关闭 删除
                    if (row.defectStatus == 1) {
                        var c = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 1 + ',' + flag + ')">提交</a>';
                        var c2 = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 6 + ',' + flag + ')">删除</a>';
                        if (defectSubmit == true) {
                            opt_status.push(c);
                        }
                        if (defectRepeal == true) {
                            opt_status.push(g);
                        }
                        if (defectDelete == true) {
                            opt_status.push(c2);
                        }
                    }

                    var d = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 7 + ',' + flag + ')">转交</a>';//不要
                    var f = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 8 + ',' + flag + ')">驳回</a>';
                    // 待确认状态 开发：编辑 转交 确认 驳回（ 测试： 编辑，撤销 ，关闭）
                    if (row.defectStatus == 2) {
                        /*var f1 = '<a  href="javascript:void(0);" class="edit-opt" onclick="affirmDefectModal(' + rows + ','+ 9 +')">确认缺陷</a>';*/
                        var e = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 10 + ',' + flag + ')">确认</a>';
                        if (defectSend == true) {
                            opt_status.push(d);
                        }
                        if (defectAffirm == true) {
                            opt_status.push(e);
                        }
                        if (defectReject == true) {
                            opt_status.push(f);
                        }
                    }

                    //拒绝状态：测试：编辑  撤销 再次提交 --> 待确认
                    if (row.defectStatus == 3) {
                        var _a3 = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 11 + ',' + flag + ')">再次提交</a>';
                        if (defectSubmitAgain == true) {
                            opt_status.push(_a3);
                        }
                        if (defectRepeal == true) {
                            opt_status.push(g);
                        }
                    }

                    var _a1 = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 12 + ',' + flag + ')">修复完成</a>';
                    // 修复中状态 开发：编辑 修复完成
                    if (row.defectStatus == 4) {
                        if (defectRepairComplete == true) {
                            opt_status.push(_a1);
                        }
                    }

                    var _a6 = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ',' + 14 + ',' + flag + ')">重新打开</a>';
                    //已修复  编辑 关闭 再次打开 复测失败 状态变成：重新打开
                    if (row.defectStatus == 5) {
                        /*var _a5 = '<a  href="javascript:void(0);" class="edit-opt" onclick="getDefectEntity(' + row.id + ','+ 13 +')">再次提交</a>';
                         if (defectAgain == true){
                         opt_status.push(_a5);
                         }*/
                        if (defectAgainOpen == true) {
                            opt_status.push(_a6);
                        }

                        if (defectClose == true) {
                            opt_status.push(h);
                        }
                    }

                    // 关闭 ： 重新打开  10
                    if (row.defectStatus == 7) {
                        if (defectAgainOpen == true) {
                            opt_status.push(_a6);
                        }
                    }

                    // 重新打开： 修复完毕  状态：已修复5
                    if (row.defectStatus == 10) {
                        if (defectRepairComplete == true) {
                            opt_status.push(_a1);
                        }
                    }

                    if (row.defectStatus == 6 || row.defectStatus == 8) {
                        var opt = a;
                        defectEdit == true ? opt : opt = '';
                        return opt;
                    }
                    var opt_str = opt_status.join('');
                    return `
						<li role="presentation" class="dropdown">
							<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
							<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
						</li>
					`;
                    // return `
                    //     <div class="dropdown">
                    //         <a class="btn btn-secondary dropdown-toggle" href="#" role="button" 
                    //             id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    //         更多操作
                    //         </a>
                    //         <div class="dropdown-menu defect_dropdown_opt" aria-labelledby="dropdownMenuLink">${opt_str}</div>
                    //     </div>
                    // `;
                } else {
                    return '';
                }

            }
        }],
        gridComplete: function () {
			getSearchValue();
		},
        loadComplete: function (data) {
            $("#loading").css('display', 'none');
            if(data.status == "2"){
				layer.alert(data.message,{icon:0});
			}
            addTableTitle();
            if(isBegain){    //只有首次进入才需要加载输入框内容
            	getfilterParam(favoriteContent);
            	isBegain = false;
            }
			getSearchValue();
            defectInfoForExportObj = $("#defectList").jqGrid('getGridParam', "postData");
//            $(".ui-jqgrid-htable").css('width', '1634px');
//            $("table#defectList").css('width', '1634px');
        },
        beforeRequest: function () {
            $("#loading").css('display', 'block');
            var inputs = $("[id*=gs_]");
        	var searchNum=0;
        	$.each(inputs,function(index,dom){
        		var field = $(dom).attr("id").split("_")[1];
        		if($(dom).val()==""){
        			searchNum=searchNum+1;
        		}
        	});
        	
        	if(searchNum==19){
        		if(flagNum!=2){
        			layer.alert("请填写搜索条件!", { icon: 0 });
            		$("#loading").css('display', 'none');
          	 		return false;
          	 		flagNum=1;
        		}else{
        			flagNum=1;
        		}
        		
        	}
        },
        loadError: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    }).trigger("reloadGrid");
    $("#defectList").jqGrid('filterToolbar', {searchOperators: true});
}

//自定义筛选器 重置
function clearConfigFilter(){
	var inputs = $("[id*=sh_]");   //获取所有筛选器输入框
	$.each(inputs,function(index,dom){
		$(dom).val("");
	})
}

//自定筛选器 搜索
function configFilterSearch(){
	//根据筛选器弹框页面输入内容 赋值给列表表格搜索条件
	var content = [];
	var isAllNull = true;
	var inputs = $("[id*=sh_]");   //获取所有筛选器输入框
	$.each(inputs,function(index,dom){
		var id = $(dom).attr("id");
		var value = $(dom).val();
		var contentJson = {};
		contentJson.field = id.split('_')[1];
		contentJson.value = value;
		$("#gs_"+id.split('_')[1]).val('');
		if(value != ''){
			content.push(contentJson);
			$("#gs_"+id.split('_')[1]).val(value);
			isAllNull = false;
		}
	});
	if(isAllNull){
		layer.alert("搜索条件不能全部为空！", { icon: 0 });
		return;
	}
	content = JSON.stringify(content);
	var filterParam = getfilterParam(content);
	filterParam = JSON.stringify(filterParam);
	searchInfo(filterParam);
	$("#configFilterModal").modal('hide');
}

//根据筛选器内容 组装 搜索条件filter
function getfilterParam(content){
	var filterParam = {};
	filterParam.groupOp = "AND";
	var rules = [];
	if(content != ""){
		var searchArr = JSON.parse(content);
		$.each(searchArr,function(index,value){
			var searchJson = {};
			searchJson.field = value.field;
			searchJson.op = "cn";
			searchJson.data = value.value;
			rules.push(searchJson);
			$("#gs_"+value.field).val(value.value);
		});
	}
	filterParam.rules = rules;
	return filterParam;
}

// 获取搜索的数据
function getSearchValue(){
	var condition = "";
	for(var i = 1;i < colIndex.length;i++){
		var searchVal = $("#gs_"+colIndex[i]).val();
		if(searchVal != ""){
			condition += "["+colNameArr[i]+":"+searchVal+"]"+" ";
		}
	}
	if(condition != ""){
		$("#condition").text("筛选条件："+condition);
	}else{
		$("#condition").text("");
	}
}

function getFilter(){
	$("#loading").css('display', 'block');
	$("[name=filterType] option[value!='']").remove();
	$.ajax({
		type: "POST",
		url: "/report/defectReport/selectDefectReportList",
		dataType: "json",
		async:false,
		data: {
			"menuUrl": "/testManageui/defect/toDefect"
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			$.each(data,function(index,value){
				if(value.filterName!=null){
					// 先创建好select里面的option元素
					var option = document.createElement("option");
					// 转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值
					$(option).val(value.id);
					// 给option的text赋值,这就是你点开下拉框能够看到的东西
					$(option).text(value.filterName);
					// 获取select 下拉框对象,并将option添加进select
					$('[name=filterType]').append(option);
					if(value.lastUseFlag==1){
						$('#filterType').selectpicker('val',value.id);
						var filterParam = getfilterParam(value.favoriteContent);
						favoriteContent = value.favoriteContent;
						filter = JSON.stringify(filterParam);
					}
					/*if(index == data.length-1 && isBegain){   // 最新筛选器且是第一次进页面
						$('#filterType').selectpicker('val',value.id);
						var filterParam = getfilterParam(value.favoriteContent);
						favoriteContent = value.favoriteContent;
						filter = JSON.stringify(filterParam);
					}*/
				}
			});
			$('[name=filterType]').selectpicker('refresh');
		}
	});
}

function configFilterShow(){
	var inputs = $("[id*=gs_]");
	$.each(inputs,function(index,dom){
		var field = $(dom).attr("id").split("_")[1];
		$("#sh_"+field).val($(dom).val());
	});
	$("#configFilterModal").modal('show');
}

function configFilterCommit(){
	var type = $("[name=filter]:checked").val();
	var filterValue = $("#configFilter").selectpicker("val");
	var filterName = $("#configFilter").find("option:selected").text();
	var inputs = $("[id*=sh_]");
	var filterArr = [];
	$.each(inputs,function(index,dom){
		var contentJson = {};
		var id = $(dom).attr('id');
		var value = $(dom).val();
		contentJson.field = id.split("_")[1];
		contentJson.value = value;
		if(value != ""){
			filterArr.push(contentJson);
		}
	});
	$("#loading").css('display', 'block');
	if(type == "2"){
		if(filterValue == ""){
			layer.alert("未选择筛选器！", { icon: 0 });
			$("#loading").css('display', 'none');
			return;
		}
		$.ajax({
			type: "POST",
			url: "/report/defectReport/updateInfo",
			dataType: "json",
			data: {
				"id":filterValue,
				"filterName": filterName,
				"favoriteContent": JSON.stringify(filterArr)
			},
			success: function (data) {
				$("#loading").css('display', 'none');
				if(data.code = 1){
					layer.alert("修改成功！", { icon: 1 });
				}else{
					layer.alert("修改失败！", { icon: 2 });
				}
			}
		});
	}else if(type == "1"){
		var filterName = $("#filterName").val();
		var flag = jugeFilterName(filterName);
		if(!flag){
			$("#loading").css('display', 'none');
			return;
		}
		$.ajax({
			type: "POST",
			url: "/report/defectReport/collectCondition",
			dataType: "json",
			data: {
				"filterName": filterName,
				"defectReport": JSON.stringify(filterArr),
				"menuUrl": "/testManageui/defect/toDefect"
			},
			success: function (data) {
				$("#loading").css('display', 'none');
				if(data.code = 1){
					layer.alert("添加成功！", { icon: 1 });
					getFilter();
					$("#configFilterModal").modal('hide');
				}else{
					layer.alert("添加失败！", { icon: 2 });
				}
			}
		});
	}else{
		$.ajax({
			type: "POST",
			url: "/report/defectReport/updateDefectReport",
			dataType: "json",
			data: {
				"id": filterValue
			},
			success: function (data) {
				$("#loading").css('display', 'none');
				if(data > 0){
					getFilter();
					layer.alert("删除成功！", { icon: 1 });
				}else{
					layer.alert("删除失败！", { icon: 2 });
				}
			}
		});
	}
}

// 判断过虑器名称
function jugeFilterName(filterName){
	var options = $("#configFilter").find("option");
	var isRepeat = false; 
	if(filterName == ""){
		layer.alert("筛选器名称为空",{icon:0});
		return false;
	}
	$.each(options,function(index,dom){     // 判断筛选器重复
		var text = $(dom).text();
		if(text == filterName){
			isRepeat = true;
			return false;
		}
	});
	if(isRepeat){
		layer.alert("筛选器名称重复",{icon:0});
		return false;
	}
	return true;
}

function addTitle(){
	for(var i = 1;i < colIndex.length;i++){
		if(colIndex[i] == "emergencyLevelStr" || colIndex[i] == "defectStatusList" || colIndex[i] == "severityLevel" || colIndex[i] == "defectSourceSql" || colIndex[i] == "defectTypeStr"){
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not (*abc* or *def*):查找不包含abc或不包含def的结果。" );
		}else if(colIndex[i] == "repairRoundStr"){
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n示例如下:\n<100、>100、<=100、>=100、>100 and <200、>=100 and <=200、\n<100 or >200、(>=100 and <=200) or (>=400 and <=500)" );
		}else if(colIndex[i] == "windowName"){
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n7、搜索内容必须为yyyyMMdd格式\n示例如下:\n<20200101、>20200101、<=20200101、>=20200101、>20200101 and <20200201、>=20200101 and <=20200201、\n<20200101 or >20200201、(>=20200101 and <=20200201) or (>=20200401 and <=20200501)" );
		}else{
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、and:表示同时满足条件。\n3、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* and *def*:查找同时包含abc和包含def的结果。\n5、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not *abc* or not *def*:查找不包含abc或不包含def的结果。\n8、not (*abc* and *def*):查找不同时包含abc和def的结果。\n9、not (*abc* or *def*):查找不包含abc或不包含def的结果。\n10、(*ab* and *cd*) or (*df* and *gh*):查找同时包含ab和cd或者同时包含df和gh的结果。" );
		}
	}
}

function addTableTitle(){
	for(var i = 1;i < colIndex.length;i++){
		if(colIndex[i] == "emergencyLevelStr" || colIndex[i] == "defectStatusList" || colIndex[i] == "severityLevel" || colIndex[i] == "defectSourceSql" || colIndex[i] == "defectTypeStr"){
			$("#gs_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not (*abc* or *def*):查找不包含abc或不包含def的结果。" );
		}else if(colIndex[i] == "repairRoundStr"){
			$("#gs_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n示例如下:\n<100、>100、<=100、>=100、>100 and <200、>=100 and <=200、\n<100 or >200、(>=100 and <=200) or (>=400 and <=500)" );
		}else if(colIndex[i] == "windowName"){
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n7、搜索内容必须为yyyyMMdd格式\n示例如下:\n<20200101、>20200101、<=20200101、>=20200101、>20200101 and <20200201、>=20200101 and <=20200201、\n<20200101 or >20200201、(>=20200101 and <=20200201) or (>=20200401 and <=20200501)" );
		}else{
			$("#gs_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、and:表示同时满足条件。\n3、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* and *def*:查找同时包含abc和包含def的结果。\n5、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not *abc* or not *def*:查找不包含abc或不包含def的结果。\n8、not (*abc* and *def*):查找不同时包含abc和def的结果。\n9、not (*abc* or *def*):查找不包含abc或不包含def的结果。\n10、(*ab* and *cd*) or (*df* and *gh*):查找同时包含ab和cd或者同时包含df和gh的结果。" );
		}
	}
}

/*从其它页面跳转过来的*/
function getExsitParam(){
	var filterParam = {};
	filterParam.groupOp = "AND";
	var rules = [];
	var content = [];
	if(existSystemName != "$systemName"){
		var searchJson = {};
		searchJson.field = "systemName";
		searchJson.op = "cn";
		searchJson.data = existSystemName;
		rules.push(searchJson);
		$("#gs_systemName").val(existSystemName);
		var contentJson = {field:"systemName",value:existSystemName};
		content.push(contentJson);
	}
	if(existReqFCode != "$reqFName" && existReqFCode != "NULL"){
		var searchJson = {};
		searchJson.field = "featureCode";
		searchJson.op = "cn";
		searchJson.data = existReqFCode;
		rules.push(searchJson);
		$("#gs_featureCode").val(existReqFCode);
		var contentJson = {field:"featureCode",value:existReqFCode};
		content.push(contentJson);
	}
	if(workTaskCode_defect != "$workTaskCode"){
		var searchJson = {};
		searchJson.field = "testTaskCode";
		searchJson.op = "cn";
		searchJson.data = workTaskCode_defect;
		rules.push(searchJson);
		$("#gs_testTaskCode").val(workTaskCode_defect);
		var contentJson = {field:"testTaskCode",value:workTaskCode_defect};
		content.push(contentJson);
	}
	if(existWindowDate != "$windowDate"){
		existWindowDate = replaceSearchDate(existWindowDate);
		var searchJson = {};
		searchJson.field = "windowName";
		searchJson.op = "cn";
		searchJson.data = existWindowDate;
		rules.push(searchJson);
		$("#gs_windowName").val(existWindowDate);
		var contentJson = {field:"windowName",value:existWindowDate};
		content.push(contentJson);
	}
	if(reqStatus != "$reqStatus"){
		var searchJson = {};
		searchJson.field = "defectStatusList";
		searchJson.op = "cn";
		searchJson.data = reqStatus;
		rules.push(searchJson);
		$("#gs_defectStatusList").val(reqStatus);
		var contentJson = {field:"defectStatusList",value:reqStatus};
		content.push(contentJson);
	}
	if( workTaskCode_defect!= "$workTaskCode" ||existSystemName != "$systemName" 
		|| existWindowDate != "$windowDate" || (existReqFCode != "$reqFName"&&existReqFCode != "NULL")
		|| reqStatus != "$reqStatus"){
		$('#filterType').selectpicker('val',"");
		$('#filterType').selectpicker("refresh");
		filterParam.rules = rules;
		filter = JSON.stringify(filterParam);
		favoriteContent = JSON.stringify(content);
	}
}

function replaceSearchDate(ele){
	if(ele.indexOf("&lt;=") != -1 || ele.indexOf("&lt;") != -1
			|| ele.indexOf("&gt;=") != -1 || ele.indexOf("&gt;") != -1) {
		 ele = ele.replace("&lt;=", "<=");
		 ele = ele.replace("&lt;", "<");
		 ele = ele.replace("&gt;=", ">=");
		 ele = ele.replace("&gt;", ">");
	}
	return ele;
}
//清空搜索框
function clearSearch(){
	var inputs = $("[id*=gs_]");
	$.each(inputs,function(index,dom){
		$(dom).val("");
	})
}

// 根据id获取实体类数据
function getDefectEntity(defectId, Opt, flag) {
    $.ajax({
        url: defectUrl + "defect/getDefectEntity",
        type: "post",
        data: {
            defectId: defectId
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                var de = data.defectInfo;
                de["flag"] = flag;
                optFun(de, Opt, data.field);
            }
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })
}

// 不同的opt 走不同的操作
function optFun(defectInfo, Opt, field) {
    switch (Opt) {
        case 1:
            edit(defectInfo, field); // 编辑
            break;
        /*case 2:
         checkDefect(defectInfo);
         break;*/
        case 3:
            repeal(defectInfo);// 撤销
            break;
        case 4:
            opt_close(defectInfo);
            break;
        case 5:
            submitDefect_a(defectInfo);
            break;
        case 6:
            deleteDefect(defectInfo);
            break;
        case 7:
            send(defectInfo);
            break;
        case 8:
            rejectDefectModal(defectInfo);
            break;
        /*  case 9:rejectDefectModal
         affirmDefectModal(defectInfo);
         break;*/
        case 10:
            affirm(defectInfo);
            break;
        case 11:
            submitAgainModal(defectInfo);
            break;
        case 12:
            repairCompleteModal(defectInfo);
            break;
        case 13:
            againModal(defectInfo);
            break;
        case 14:
            againOpen(defectInfo); //重新打开
            break;
        default:
            break;
    }
}


function tableClearSreach() {
    $(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
    $(".ui-search-toolbar .ui-search-input select").val('0');
}

function addCheckBox() {
    $("#colGroup").empty();
    var str = "";
    str = '<div class="onesCol"><input type="checkbox" value="defectCode" onclick="showHideCol( this )" /><span>缺陷编号</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="emergencyLevelStr" onclick="showHideCol( this )" /><span>紧急程度</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="defectSummary" onclick="showHideCol( this )" /><span>缺陷摘要</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="defectStatusList" onclick="showHideCol( this )" /><span>缺陷状态</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="defectTypeStr" onclick="showHideCol( this )" /><span>缺陷类型</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="defectSourceStr" onclick="showHideCol( this )" /><span>缺陷来源</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="severityLevelStr" onclick="showHideCol( this )" /><span>缺陷等级</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="requirementCode" onclick="showHideCol( this )" /><span>所属需求</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="featureCode" onclick="showHideCol( this )" /><span>测试任务</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="testTaskCode" onclick="showHideCol( this )" /><span>工作任务</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="systemName" onclick="showHideCol( this )" /><span>涉及系统</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="windowName" onclick="showHideCol( this )" /><span>投产窗口</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="assignUserName" onclick="showHideCol( this )" /><span>主修复人</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="testUserName" onclick="showHideCol( this )" /><span>测试人</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="submitUserName" onclick="showHideCol( this )" /><span>提出人</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="developUserName" onclick="showHideCol( this )" /><span>开发人员</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="opt" onclick="showHideCol( this )" /><span>操作</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="repairRoundStr" onclick="showHideCol( this )" /><span>修复轮次</span></div>';
    $("#colGroup").append(str);
}

function showHideCol(This) {
    var colModel = $("#defectList").jqGrid('getGridParam', 'colModel');
    var width = 0;//获取当前列的列宽
    var arr = [];
    for (var i = 0; i < colModel.length; i++) {
        if (colModel[i]["hidden"] == false) {
            arr.push(colModel[i]["hidden"]);
        }
    }
    if ($(This).is(':checked')) {
        $("#defectList").setGridParam().hideCol($(This).attr('value'));
        $("#defectList").setGridWidth($('.wode').width());
        if (arr.length == 1) {
            $("#defectList").jqGrid('setGridState', 'hidden');
        }
    } else {
        $("#defectList").jqGrid('setGridState', 'visible');
        $("#defectList").setGridParam().showCol($(This).attr('value'));
        $("#defectList").setGridWidth($('.wode').width());
    }
}

function searchInfo(param) {
    $("#loading").css('display', 'block');
    var gridParam = {
			loadComplete: function () {
				$("#loading").css('display', 'none');
				defectInfoForExportObj = $("#defectList").jqGrid('getGridParam', "postData");
			}
		};
	if(param != undefined){
		gridParam.postData = {filters:param};
	}
	$("#loading").css('display', 'block');
	$("#defectList").jqGrid('setGridParam', gridParam).trigger("reloadGrid"); // 重新载入
}

function searchInfo1() {
    /*$(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
    $(".ui-search-toolbar .ui-search-input select").val('0');*/

    $("#loading").css('display', 'block');
    $("#defectList").jqGrid('setGridParam', {
        postData: defectInfoForExportObj,
        page: 1,
        loadComplete: function () {
            $("#loading").css('display', 'none');
            defectInfoForExportObj = $("#defectList").jqGrid('getGridParam', "postData");
        }
    }).trigger("reloadGrid"); //重新载入
}

function againOpen(row) {
    layer.confirm("确认重新打开该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        opt_updateDefectStatus(row, 10);
    })
}

/*再次打开*/
function againModal(row) {
    reset_opt();
    formFileList = [];
    var updateStatus = '';
    for (var i = 0, len = defectStatusList.length; i < len; i++) {
        if (2 == defectStatusList[i].value) {
            updateStatus = defectStatusList[i].innerHTML;
            break;
        }
    }
    // var row = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
    layer.confirm("确认再次打开该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        opt_updateDefectStatus(row, 2);
    })
}

/*撤销*/
function repeal(rows) {
    layer.confirm("确认撤销该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');

        //var row = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
        opt_updateDefectStatus(rows, 6);
    })
}

/*关闭*/
function opt_close(rows) {
    layer.confirm("确认关闭该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        //var row = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
        opt_updateDefectStatus(rows, 7);
    })
}

/*修改缺陷状态*/
function opt_updateDefectStatus(row, defectStatus) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: defectUrl + "defect/updateDefectStatus",
        type: "post",
        data: {
            defectId: row.id,
            defectStatus: defectStatus,
            submitUserId: row.submitUserId,
            flag: row.flag
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                layer.alert('操作成功 ！', {
                    icon: 1,
                    title: "提示信息"
                });
                flagNum=2;
                initTable();
            }
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })

}

/*修复完成 状态变为 待检测*/
var _repairCompleteDefectData = {};
function repairCompleteModal(rows) {
    reset_opt();
    formFileList = [];
    // _repairCompleteDefectData = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
    _repairCompleteDefectData = rows;
    $(".solution").css("display", "block");
    $("#opt_Restored").css("display", "inline-block");
    $("#flag2").val(_repairCompleteDefectData.flag);
    $("#rejectDiv").modal("show");
}

/*修复完成，请求后台操作*/
function repairComplete() {
    var solution = $.trim($("#opt_solution").find("option:selected").val())
    if (solution == '') {
        layer.alert("必须选择解决情况！", {
            icon: 2,
            title: "提示信息"
        })
    } else {
        $("#loading").css('display', 'block');

        $.ajax({
            url: defectUrl + "defect/updateDefectwithTBC",
            type: "post",
            data: {
                defectId: _repairCompleteDefectData.id,
                defectRemark: htmlEncodeJQ($.trim($("#opt_defectRemark").val())),
                solveStatus: solution,
                defectStatus: 5,
                submitUserId: _repairCompleteDefectData.submitUserId,
                oldAssignUserId: _repairCompleteDefectData.assignUserId,
                flag: $("#flag2").val()
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {

                    remarkUploadFile(data.logId);
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
}

/*再次提交 操作: 状态变为待确认 */
var __defectAgainData = {};
function submitAgainModal(row) {
    reset_opt();
    formFileList = [];
    /*var data = rows.replace(/\n/g,"\\r\\n");
     var row = JSON.parse(data);*/
    __defectAgainData = row;
    var classColor = 'rejectionSituation';
    var rejection = $("#opt_rejection").find("option");
    for (var i = 0, len = rejection.length; i < len; i++) {
        if (row.rejectReason == rejection[i].value) {
            classColor += row.rejectReason;
            var opt = "<span class='" + classColor + "'>" + rejection[i].innerHTML + "</span>";
            $("#opt_rejectionShow").append(opt);
        }
    }
    $(".opt_rejectionDiv").css("display", "block");
    $("#opt_submitAgainDefect").css("display", "inline-block");
    $("#flag2").val(__defectAgainData.flag);
    $("#rejectDiv").modal("show");
}

/*再次提交请求后台 状态变为待确认*/
function submitAgainDefect() {
    $("#loading").css('display', 'block');

    $.ajax({
        url: defectUrl + "defect/updateDefectStatus",
        type: "post",
        data: {
            defectId: __defectAgainData.id,
            defectRemark: htmlEncodeJQ($.trim($("#opt_defectRemark").val())),
            defectStatus: 2,
            submitUserId: __defectAgainData.submitUserId,
            flag: $("#flag2").val()
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            if (data.status == 2) {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if (data.status == 1) {
                remarkUploadFile(data.logId);
            }
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })

}

/*确认操作：状态改为处理中*/
function affirm(row) {
    //var row = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
    layer.confirm("确定确认该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {
        layer.closeAll('dialog');
        $("#loading").css('display', 'block');
        $.ajax({
            url: defectUrl + "defect/updateDefectwithTBC",
            type: "post",
            data: {
                defectId: row.id,
                defectStatus: 4,
                submitUserId: row.submitUserId,
                oldAssignUserId: row.assignUserId,
                flag: row.flag
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    layer.alert('操作成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    flagNum=2;
                    initTable();
                    $("#rejectDiv").modal("hide");
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    })
}

/*驳回操作 状态变为 拒绝*/
var rejectDefectData = {};
function rejectDefectModal(rows) {
    reset_opt();
    formFileList = [];
    //rejectDefectData = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
    rejectDefectData = rows;
    $(".rejection").css("display", "block");
    $("#opt_RejectDefect").css("display", "inline-block");
    $("#flag2").val(rejectDefectData.flag);
    $("#rejectDiv").modal("show");
}

/*驳回*/
function opt_rejectDefect() {
    var rejection = $.trim($("#opt_rejection").find("option:selected").val());
    if (rejection == '') {
        layer.alert("必须选择驳回理由！", {
            icon: 2,
            title: "提示信息"
        })
    } else {
        $("#loading").css('display', 'block');

        $.ajax({
            url: defectUrl + "defect/updateDefectwithTBC",
            type: "post",
            data: {
                defectId: rejectDefectData.id,
                defectRemark: htmlEncodeJQ($.trim($("#opt_defectRemark").val())),
                rejectReason: rejection,
                defectStatus: 3,
                submitUserId: rejectDefectData.submitUserId,
                oldAssignUserId: rejectDefectData.assignUserId,
                flag: $("#flag2").val()
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    remarkUploadFile(data.logId);
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
}

/*转交*/
var sendRow = {};
function send(rows) {
    reset_opt();
    formFileList = [];
    sendRow = rows;
    $("#opt_assignUserId").val(sendRow.assignUserId);
    $("#opt_systemId").val(sendRow.systemId);
    $("#opt_systemId").attr("systemName", sendRow.systemName);
    $("#opt_submitDefect").css("display", "inline-block");
    $(".transmit").css("display", "block");
    $("#flag2").val(sendRow.flag);
    $("#rejectDiv").modal("show");
}

/*转交操作 ---> 提交 还是待确认的状态*/
function opt_submitDefect() {
    var assignUserId = $("#opt_assignUserId").val();
    if (assignUserId == '' || sendRow.assignUserId == assignUserId) {
        layer.alert("请选择转派人!(不能是相同用户)", {
            icon: 2,
            title: "提示信息"
        })
    } else {
        $("#loading").css('display', 'block');
        $.ajax({
            url: defectUrl + "defect/updateDefectwithTBC",
            type: "post",
            data: {
                defectId: sendRow.id,
                defectRemark: htmlEncodeJQ($.trim($("#opt_defectRemark").val())),
                assignUserId: assignUserId,
                defectStatus: 2,
                submitUserId: sendRow.submitUserId,
                oldAssignUserId: sendRow.assignUserId,
                flag: $("#flag2").val()
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    remarkUploadFile(data.logId);
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }
}

/*编辑缺陷*/
function edit(defectInfo, fld) {
	$('.is_new_project').show();
    newDefect_reset();
    defectAttIds = [];
    testTaskModalStatus = "edit";
    submitDefectStatus = "edit";
    $("#flag").val(defectInfo.flag);
    attList(defectInfo.id, "#edit_newFileTable");
    if (fld != undefined) {
        for (var i = 0; i < fld.length; i++) {
            field.appendDataType(fld[i], 'editFieldDiv', 'edit');
        }
    }
    $('#edit_testTaskName').parent().css("position", "relative");
    $('#edit_testTaskName').parent().append("<span onclick='clearContent2(this)' class='btn_clear' style='display:block;'></span>");
    var a = $("#edit_testTaskName").val();
    if (a == null || a == '') {
        $("#edit_windowName").removeClass("canntClick");
    } else {
        $("#edit_windowName").addClass("canntClick");
    }
    $("#editDefect").modal("show");
}

function editMessage(data) {
    search_radio_give('editProjectName', data.projectName, $("#editProjectId"), data.projectId);
	if(data.projectType == 2){    //新建类
		$('.is_new_project').hide();
	}else{
		$('.is_new_project').show();
	}
    // 待测试
    if (data.testTaskId != null) {
        // getTestTask(data.testTaskId,data.defectSource);
        $("#edit_testTaskId").val(data.testTaskId);
        $("#edit_testTaskName").val(isValueNull(data.testTaskName));
        if (data.testStage != 'undefined' && data.testStage != null) {
            $("#edit_defectSource option").each(function (i, n) {
                if (n.value == data.testStage) {
                    n.selected = true;
                }
            });
            $("#edit_defectSource").attr("disabled", "true");
        }

        $(".selectpicker").selectpicker('refresh');

    } else {
        $("#edit_defectSource option").each(function (i, n) {
            if (n.value == data.defectSource) {
                n.selected = true;
            }
        });
        $("#edit_defectSource").attr("disabled", false);
        $("#edit_system_Name").attr("disabled", false);
    }

    $("#edit_requirementCode").val(data.requirementCode);
    $("#edit_commissioningWindowId").val(data.commissioningWindowId);

    $("#edit_defectId").val(data.id);
    $("#edit_systemId").val(data.systemId);
    $("#edit_system_Name").val(isValueNull(data.systemName));
    $("#edit_discovery_Environment").val(isValueNull(data.discoveryEnvironment));
    $("#edit_systemCode").val(isValueNull(data.systemCode));
    $("#edit_repairRound").val(data.repairRound);
    $("#edit_testCaseId").val(data.testCaseId);
    // 测试案例
    $("#edit_testCaseName").val('');
    editEditor.txt.html(htmlDecodeJQ(data.defectOverview));

    $("#edit_defectSummary").val(isValueNull(data.defectSummary));

    $("#edit_defectType option").each(function (i, n) {
        if (n.value == data.defectType) {
            n.selected = true;
        }
    });

    $("#edit_severityLevel option").each(function (i, n) {
        if (n.value == data.severityLevel) {
            n.selected = true;
        }
    });
    $("#edit_emergencyLevel option").each(function (i, n) {
        if (n.value == data.emergencyLevel) {
            n.selected = true;
        }
    });
    $("#edit_defectStatus option").each(function (i, n) {
        if (n.value == data.defectStatus) {
            n.selected = true;
        }
    });

    if (data.defectStatus == 1) {
        $("#edit_defectStatus").attr("disabled", "true");
    } else {
        $("#edit_defectStatus").attr("disabled", false);
    }

    $("#edit_assignUserId").val(data.assignUserId);
    $("#edit_assignUserName").val(data.assignUserName);
    $("#edit_assignUserName").attr('username',data.assignUserName);

    $("#edit_oldAssignUserId").val(data.assignUserId);
    $("#edit_submitUserId").val(data.submitUserId);

    $("#edit_testUserId").val(data.testUserId);
    $("#edit_testUserName").val(data.testUserName);
    $("#edit_testUserName").attr('username',data.testUserName);

    $("#edit_developerId").val(data.developUserId);
    $("#edit_developer").val(data.developUserName);
    $("#edit_developer").attr('username',data.developUserName);

//    $("#edit_remark").val(data.remark);
    if(data.projectType == 2){
    	
    }else{
    	$("#edit_windowId").val(data.commissioningWindowId);
        $("#edit_windowName").val(data.windowName);
    }
    

    $("#edit_newFileTable").val('');
    $(".selectpicker").selectpicker('refresh');

    if (data.defectStatus == 1) {
        $("#edit_submitDefect").css("display", "inline-block");
        $("#edit_stagDefect").css("display", "inline-block");
    } else {
        $("#edit_submit").css("display", "inline-block");
    }

    $("#edit_projectGroupId").val(isValueNull(data.projectGroupName));
    $("#edit_projectGroupId").attr("idValue", isValueNull(data.projectGroupId));
    if (isValueNull(data.projectGroupName).length > 0) {
        $("#edit_projectGroupId").next().show();
    }
    $("#edit_assetSystemTreeId").val(isValueNull(data.assetSystemTreeName));
    $("#edit_assetSystemTreeId").attr("idValue",isValueNull(data.assetSystemTreeId));
    if(isValueNull(data.assetSystemTreeName).length > 0){
        $("#edit_assetSystemTreeId").next().show();
    }
    laydate.render({
        elem: '#eidt_closeTime',
        type: 'datetime',
        value: isValueNull(data.closeTime),
        isInitValue: true
    });
    systemVersionBindCreateOptionsForEdit($("select[name='edit_detectedSystemVersionId']"), isValueNull(data.detectedSystemVersionId));
    $("select[name='edit_detectedSystemVersionId']").unbind('shown.bs.select');
    $("select[name='edit_detectedSystemVersionId']").on('shown.bs.select', function () {
        systemVersionBindCreateOptionsForEdit(this);
    });
    systemVersionBindCreateOptionsForEdit($("select[name='edit_repairSystemVersionId']"), isValueNull(data.repairSystemVersionId));
    $("select[name='edit_repairSystemVersionId']").unbind('shown.bs.select');
    $("select[name='edit_repairSystemVersionId']").on('shown.bs.select', function () {
        systemVersionBindCreateOptionsForEdit(this);
    });
    laydate.render({
        elem: '#edit_expectRepairDate',
        value: isValueNull(data.expectRepairDate),
        isInitValue: true
    });
    $("#edit_estimateWorkload").val(isValueNull(data.estimateWorkload));
     rootCauseAnalysisEditorForEdit.txt.html(htmlDecodeJQ(data.rootCauseAnalysis));
}

// 新建缺陷
function newDefect_btn() {
    newDefect_reset();
    testTaskModalStatus = "new";
    submitDefectStatus = "new";
    defectAttIds = [];
    editor.txt.html("测试环境：<br>" + "数据库：<br>" + "测试工号：<br>" + "测试数据：<br>" + "测试步骤：<br>" + "预期结果：<br>" +
        "实际结果：<br>" + "存在问题：<br>"+"根本原因描述："); //根本原因放到缺陷描述里
    //添加自定义字段
    field.addField();
    $("#new_windowName").removeAttr("disabled");
    $("#new_repairRound").val(1);
    laydate.render({
        show: true,
        elem: '#closeTime',
        type: 'datetime'
    });
    $("select[name='detectedSystemVersionId']").unbind('shown.bs.select');
    $("select[name='detectedSystemVersionId']").on('shown.bs.select', function () {
        systemVersionBindCreateOptions(this);
    });
    $("select[name='repairSystemVersionId']").unbind('shown.bs.select');
    $("select[name='repairSystemVersionId']").on('shown.bs.select', function () {
        systemVersionBindCreateOptions(this);
    });
    laydate.render({
        show: true,
        elem: '#expectRepairDate',
        position: 'abolute'
    });
    $("#newDefect").modal("show");
}
//清空表格内容
function clearContent(that) {
    $(that).parent().children('input').val("").change();
    $(that).parent().children(".btn_clear").css("display", "none");
}
function clearContent2(that) {
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display", "none");
    $("#edit_windowName").removeClass("canntClick");
    $('#edit_windowName').removeAttr("disabled");
    $("#edit_windowName").val('').change();
    $('#edit_defectSource').removeAttr("disabled");
    $("#edit_defectSource").selectpicker('refresh');
    $("#edit_defectSource").val('').change();
    $('#edit_systemId').val('');
    $("#edit_system_Name").val('');
}

// 删除缺陷
function deleteDefect(rows) {

    layer.confirm("确定删除该缺陷吗？", {
        btn: ['确定', '取消'], //按钮
        title: "提示信息"
    }, function () {

        layer.closeAll('dialog');

        //var data = JSON.parse(rows.replace(/\n/g,"\\r\\n"));
        var data = rows;
        $("#loading").css('display', 'block');
        $.ajax({
            url: defectUrl + "defect/removeDefect",
            method: "post",
            data: {
                id: data.id,
                submitUserId: data.submitUserId,
                flag: data.flag
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    layer.alert('删除缺陷成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    flagNum=2;
                    initTable();
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    });
}

// 暂存缺陷
function stagDefect() {
    updateDefectStatus(1);
}

// 表格暂存的按钮
function stagDefect_a(row) {
    submitDefectStatus = "edit";
    //var data = JSON.parse(row.replace(/\n/g,"\\r\\n"));
    $("#edit_defectId").val(row.id);
    updateDefectStatus(1);
}

// 列表的提交按钮
function submitDefect_a(row) {
    formFileList = [];
    //var data = JSON.parse(row.replace(/\n/g,"\\r\\n"));
    var data = row;
    submitDefectStatus = "edit";

    $('#edit_DefectFrom').data('bootstrapValidator').validate();
    if (!$('#edit_DefectFrom').data('bootstrapValidator').isValid()) {
        return false;
    }

    if (data.assignUserId == null) {
        layer.alert("必须选择指派人！", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    } else {
        $("#edit_assignUserId").val(data.assignUserId);
        $("#edit_defectId").val(data.id);

        updateDefectStatus(2);
    }
}

// 提交缺陷 状态变为：待确认
function submitDefect() {
    var flag = false;
    var assignUserId = '';
    if (submitDefectStatus == "new") {
        $('#newDefectFrom').data('bootstrapValidator').validate();
        if (!$('#newDefectFrom').data('bootstrapValidator').isValid()) {
            flag == true;
            return false;
        }
        assignUserId = $("#new_assignUserId").val();
        if ($.trim(getText(editor.txt.html())) == '') {
            flag == true;
            layer.alert("缺陷描述不能为空！", {
                icon: 0,
                title: "提示信息"
            })
            return false;
        }

    } else if (submitDefectStatus == "edit") {
        $('#edit_DefectFrom').data('bootstrapValidator').validate();
        if (!$('#edit_DefectFrom').data('bootstrapValidator').isValid()) {
            flag == true;
            return false;
        }

        assignUserId = $("#edit_assignUserId").val();
        if (getText(editEditor.txt.html()) == '') {
            flag == true;
            layer.alert("缺陷描述不能为空！", {
                icon: 0,
                title: "提示信息"
            })
            return false;
        }
    }

    if (assignUserId == '') {
        layer.alert("必须选择主修复人！", {
            icon: 0,
            title: "提示信息"
        })
        flag == true;
        return false;
    }
    if (!flag) {
        updateDefectStatus(2);
    }

}

/*编辑：确定按钮*/
function opt_submit() {
    $('#edit_DefectFrom').data('bootstrapValidator').validate();
    if (!$('#edit_DefectFrom').data('bootstrapValidator').isValid()) {
        return false;
    }
    if (getText(editEditor.txt.html()) == '') {
        layer.alert("缺陷描述不能为空！", {
            icon: 0,
            title: "提示信息"
        });
        return;
    }
    var assignUserId = $("#edit_assignUserId").val();
    if (assignUserId == "") {
        layer.alert("必须选择主修复人！", {
            icon: 0,
            title: "提示信息"
        });
        return false;
    } else {
        submitDefectStatus = "edit";
        var status = $.trim($("#edit_defectStatus").find("option:selected").val());
        updateDefectStatus(status);
    }
}

// 新增 编辑 缺陷
function updateDefectStatus(defectStatus) {
    var files = new FormData();
    if (formFileList.length > 0) {
        var filesSize = 0;
        for (var i = 0, len2 = formFileList.length; i < len2; i++) {
            filesSize += formFileList[i].size;
            files.append("files", formFileList[i]);
        }

        if (filesSize > 1048576000) {
            layer.alert('文件太大,请删选！！！', {
                icon: 2,
                title: "提示信息"
            });
            return false;
        }

    }

    if (submitDefectStatus == "new") {
        var obj = {};
        obj.projectId = $("#newProjectId").val();
        
        obj.assignUserId = $("#new_assignUserId").val();
        obj.testTaskId = $("#testTaskId").val();
        obj.requirementCode = $("#new_requirementCode").val();
        obj.systemId = $("#systemId").val();
//        obj.commissioningWindowId = $("#new_commissioningWindowId").val();
        obj.defectSummary = htmlEncodeJQ($.trim($("#new_defectSummary").val()));
        obj.repairRound = htmlEncodeJQ($.trim($("#new_repairRound").val()));
        obj.defectOverview = htmlEncodeJQ(editor.txt.html());
        obj.defectType = $.trim($("#new_defectType").find("option:selected").val());
        obj.defectSource = $.trim($("#new_defectSource").find("option:selected").val());
        obj.severityLevel = $.trim($("#new_severityLevel").find("option:selected").val());
        obj.emergencyLevel = $.trim($("#new_emergencyLevel").find("option:selected").val());
        obj.defectStatus = defectStatus;
        obj.fieldTemplate = field.getFieldData("canEditField");
        obj.testUserId = $("#new_testUserId").val();
        obj.developUserId = $("#new_developerId").val();
        obj.remark = $("#new_remark").val();
        obj.commissioningWindowId = $("#new_windowId").val();
        obj.discoveryEnvironment = $("#new_discovery_Environment").val();

        obj.projectGroupId = '';
        obj.closeTime = '';
        obj.detectedSystemVersionId = '';
        obj.repairSystemVersionId = '';
        obj.expectRepairDate = '';
        obj.estimateWorkload = '';
        
        obj.assetSystemTreeId = $("#assetSystemTreeId").attr("idValue");//模块
        obj.rootCauseAnalysis = htmlEncodeJQ(rootCauseAnalysisEditorForAdd.txt.html());//根本原因

        for (var i = 0; i < obj.fieldTemplate.field.length; i++) {
            if (obj.fieldTemplate.field[i].required == "false") {
                if ((defectStatus != 1 ) && (obj.fieldTemplate.field[i].valueName == "" || obj.fieldTemplate.field[i].valueName == null || obj.fieldTemplate.field[i].valueName == undefined )) {
                    $("#loading").css('display', 'none');
                    layer.alert(obj.fieldTemplate.field[i].labelName + "不能为空", {
                        icon: 2,
                        title: "提示信息"
                    });

                    return;
                }
            }
        }

        files.append("defectInfo", JSON.stringify(obj));

        $("#loading").css('display', 'block');
        // 新增缺陷
        $.ajax({
            type: "post",
            url: "/zuul" + defectUrl + 'defect/insertDefect',
            dataType: "json",
            data: files,
            cache: false,
            processData: false,
            contentType: false,
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    layer.alert('操作成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    $("#newDefect").modal("hide");

                    reset_opt();
                    formFileList = [];
                    editAttList = [];
                    //reset();
                    flagNum=2;
                    $(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
    				$(".ui-search-toolbar .ui-search-input select").val('0');
    				$(".ui-search-toolbar .ui-search-input  .btn_clear").css("display", "none");
    				var field=[{"field":"defectCode","value":data.defectData.defectCode}];
                    var filterParam =getfilterParam(JSON.stringify(field));
                    $("#defectList").jqGrid("setGridParam", { postData: {
                        "filters":JSON.stringify(filterParam)
                    }}).trigger("reloadGrid");
                    
                    //searchInfo1();
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    } else if (submitDefectStatus == "edit") {
        // 编辑缺陷
        $("#loading").css('display', 'block');
        var obj = {};
        obj.projectId = $("#editProjectId").val();
        
        obj.id = $("#edit_defectId").val();
        obj.assignUserId = $("#edit_assignUserId").val();
        obj.testTaskId = $("#edit_testTaskId").val();
        obj.requirementCode = $("#edit_requirementCode").val();
        obj.systemId = $("#edit_systemId").val();
        obj.testsetCaseExecuteId = $("#edit_testCaseId").val();
        obj.caseNumber = $("#edit_caseNumber").val();
        obj.commissioningW53ewindowId = $("#edit_commissioningWindowId").val();
        obj.defectSummary = htmlEncodeJQ($.trim($("#edit_defectSummary").val()));
        obj.repairRound = htmlEncodeJQ($.trim($("#edit_repairRound").val()));
        obj.defectOverview = htmlEncodeJQ(editEditor.txt.html());
        obj.defectType = $.trim($("#edit_defectType").find("option:selected").val());
        obj.defectSource = $.trim($("#edit_defectSource").find("option:selected").val());
        obj.severityLevel = $.trim($("#edit_severityLevel").find("option:selected").val());
        obj.emergencyLevel = $.trim($("#edit_emergencyLevel").find("option:selected").val());
        obj.oldAssignUserId = $("#edit_oldAssignUserId").val();
        obj.submitUserId = $("#edit_submitUserId").val();
        obj.defectStatus = defectStatus;
        obj.fieldTemplate = field.getFieldData("editFieldDiv");
        obj.flag = $("#flag").val();
        obj.testUserId = $("#edit_testUserId").val();
        obj.developUserId = $("#edit_developerId").val();
//        obj.remark = $("#edit_remark").val();
        obj.commissioningWindowId = $("#edit_windowId").val();
        obj.discoveryEnvironment = $("#edit_discovery_Environment").val();

        obj.projectGroupId = '';
        obj.closeTime = '';
        
        obj.detectedSystemVersionId = '';
        obj.repairSystemVersionId = '';
        obj.expectRepairDate = '';
        obj.estimateWorkload = '';
        
        obj.assetSystemTreeId = $("#edit_assetSystemTreeId").attr("idValue");//模块
        obj.rootCauseAnalysis = htmlEncodeJQ(rootCauseAnalysisEditorForEdit.txt.html());//根本原因

        for (var i = 0; i < obj.fieldTemplate.field.length; i++) {
            if (obj.fieldTemplate.field[i].required == "false") {
                if (obj.fieldTemplate.field[i].valueName == "" || obj.fieldTemplate.field[i].valueName == null || obj.fieldTemplate.field[i].valueName == undefined) {
                    $("#loading").css('display', 'none');
                    layer.alert(obj.fieldTemplate.field[i].labelName + "不能为空", {
                        icon: 2,
                        title: "提示信息"
                    });
                    return;
                }
            }
        }

        files.append("defectInfo", JSON.stringify(obj));
        files.append("removeAttIds", defectAttIds);
        $.ajax({
            type: "post",
            url: "/zuul" + defectUrl + 'defect/updateDefect',
            dataType: "json",
            data: files,
            cache: false,
            processData: false,
            contentType: false,
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == 2) {
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == "noPermission") {
                    layer.alert(noPermission, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else if (data.status == 1) {
                    layer.alert('操作成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    $("#editDefect").modal("hide");
                    reset_opt();
                    formFileList = [];
                    editAttList = [];
                    //reset();
                    flagNum=2;
                    searchInfo1();
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        })
    }


}

function reset() {
    $("#submitDate").val('');
    $("#defectCode").val('');
    $("#defectSummary").val('');
    $("#windowId").val('');
    $("#windowName").val('');
    $("#createDate").val('');
    $("#submitUserId").val('');
    $("#submitUserName").val('');
    $("#defectStatus").selectpicker('val', '0');
    $("#defectSource").selectpicker('val', '');
    $("#severityLevel").selectpicker('val', '');
    $("#emergencyLevel").selectpicker('val', '');
    $("#windowName").selectpicker('val', '');
    $("#requirementId").val('');
    $("#requirementName").val('');
    $("#requirementCode").val('');
    $("#sel_systemId").val('');
    $("#systemName").val('');
    $("#defectType").selectpicker('val', '');
    $("#taskCode").val('');
    $("#taskName").val('');
    $("#taskState").selectpicker('val', '');
    $("#donginguserId").val('');
    $("#donginguser").val('');
    $("#assignUserId").val('');
    $("#assignUser").val('');
    $("#testUserId").val('');
    $("#testUser").val('');
    $("#developerId").val('');
    $("#developer").val('');
    $("#workTaskCode").val('');
    $("#repairRound").val('');
    /*workTaskId = ''; */
    winReset();
    reqReset();
    sysReset();
    userReset();
    select_rows = new Array();
    $(".color1 .btn_clear").css("display", "none");
    $("#reqFiD").val("");
    $("#reqFName").val("");
}

function newDefect_reset() {
	search_radio_clear('editProjectName',$("#editProjectId"));
	search_radio_clear('newProjectName',$("#newProjectId"));
	$('.is_new_project').show();
    $("#canEditField").empty();
    $("#editFieldDiv").empty();
    /*新建的字段*/
    $("#testTaskId").val('');
    $("#testTaskName").val('');
    $("#systemId").val('');
    $("#systemCode").val('');
    $("#system_Name").val('');
    $("#testCaseId").val('');
    $("#new_caseNumber").val('');
    $("#testCaseName").val('');
    $("#new_repairRound").val('');
    editor.txt.clear();
    $("#new_defectSummary").val('');
    $("#new_defectType").selectpicker('val', '');
    $("#new_defectSource").selectpicker('val', '');
    $("#new_defectSource").attr("disabled", false);
    $("#new_severityLevel").selectpicker('val', '');
    $("#new_emergencyLevel").selectpicker('val', '');
    $("#new_assignUserId").val('');
    $("#new_assignUserName").val('');
    $("#newFileTable tbody").html("");
    $("#system_Name").attr("disabled", false);
    $("#system_Name").addClass("pointStyle");
    $("#new_requirementCode").val('');
    $("#new_commissioningWindowId").val('');
    $("#new_testUserId").val('');
    $("#new_testUserName").val('');
    $("#new_remark").val('');
    $("#new_windowId").val('');
    $("#new_discovery_Environment").val('');
    $("#new_windowName").val('');
    $("#projectGroupId").val('');
    $("#projectGroupId").attr("idValue", "");
    $("#closeTime").val('');
    $("#assetSystemTreeId").val('');
    $("#assetSystemTreeId").attr("idValue", "");
    $("#detectedSystemVersionId").selectpicker('val', '');
    $("#repairSystemVersionId").selectpicker('val', '');
    $("#expectRepairDate").val('');
    $("#estimateWorkload").val('');
     rootCauseAnalysisEditorForAdd.txt.clear();


    /*编辑的字段*/
    $("#edit_windowId").val('');
    $("#edit_discovery_Environment").val('');
    $("#edit_windowName").val('');
    
    $("#edit_testTaskId").val('');
    $("#edit_testTaskName").val('');
    $("#edit_systemId").val('');
    $("#edit_systemCode").val('');
    $("#edit_system_Name").val('');
    $("#edit_testCaseId").val('');
    $("#edit_caseNumber").val('');
    $("#edit_testCaseName").val('');
    $("#edit_repairRound").val('');
    editEditor.txt.clear();
    $("#edit_defectSummary").val('');
    $("#edit_oldAssignUserId").val('');
    $("#edit_submitUserId").val('');
    $("#edit_requirementCode").val('');
    $("#edit_commissioningWindowId").val('');
    $("#edit_defectType").selectpicker('val', '');
    $("#edit_defectStatus").selectpicker('val', '');
    $("#edit_defectSource").selectpicker('val', '');
    $("#edit_defectSource").attr("disabled", false);
    $("#edit_severityLevel").selectpicker('val', '');
    $("#edit_emergencyLevel").selectpicker('val', '');
    $("#edit_assignUserId").val('');
    $("#edit_assignUserName").val('');
    $("#edit_newFileTable tbody").html("");
    $("#edit_testUserName").val('');
    $("#edit_testUserId").val();
//    $("#edit_remark").val();

    $("#edit_stagDefect").css("display", "none");
    $("#edit_Restored").css("display", "none");
    $("#edit_RejectDefect").css("display", "none");
    $("#edit_submitAgainDefect").css("display", "none");
    $("#edit_submitDefect").css("display", "none");
    $("#edit_closeDefect").css("display", "none");
    $("#edit_submit").css("display", "none");
    $("#edit_windowId").val('');
    $("#edit_windowName").val('');


    $("#edit_system_Name").attr("disabled", false);
    $("#edit_system_Name").addClass("pointStyle");

    $("#edit_projectGroupId").val('');
    $("#edit_projectGroupId").attr("idValue", "");
    $("#eidt_closeTime").val('');
    $("#edit_assetSystemTreeId").val('');
    $("#edit_assetSystemTreeId").attr("idValue", "");
    $("#edit_detectedSystemVersionId").selectpicker('val', '');
    $("#edit_repairSystemVersionId").selectpicker('val', '');
    $("#edit_expectRepairDate").val('');
    $("#edit_estimateWorkload").val('');
     rootCauseAnalysisEditorForEdit.txt.clear();

    // 清空定义数据
    formFileList = [];
    editAttList = [];
}

function reset_opt() {
    $("#opt_defectRemark").val('');
    $("#opt_assignUserId").val('');
    $("#opt_systemId").val('');
    $("#opt_systemId").attr("systemName", "");
    $("#opt_assignUserName").val('');
    $("#optFileTable tbody").html("");
    $("#opt_rejection").selectpicker('val', '');
    $("#opt_solution").selectpicker('val', '');
    $("#opt_rejectionShow").selectpicker('val', '');

    $("#opt_rejectionShow").empty();

    $("#check_severityLevel").empty();
    $("#check_emergencyLevel").empty();
    $("#check_assignUserName").empty();
    $("#check_submitUserName").empty();
    $("#check_systemName").empty();
    $("#check_defectCode").empty();
    $("#check_testTaskName").empty();
    $("#check_testCaseName").empty();
    $("#check_defectType").empty();
    $("#check_defectSource").empty();
    $("#check_defectOverview").empty();
    $("#check_reqFetureCode").empty();
    $("#check_reqFetureName").empty();
    $("#check_testTaskCode").empty();
    $("#check_req_code").empty();
    $("#check_table tbody").html("");


    $("#dev_check_reqFetureCode").empty();
    $("#dev_check_reqFetureName").empty();
    $("#dev_check_testTaskCode").empty();
    $("#dev_check_testTaskName").empty();
    $("#dev_check_severityLevel").empty();
    $("#dev_check_emergencyLevel").empty();
    $("#dev_check_assignUserName").empty();
    $("#dev_check_submitUserName").empty();
    $("#dev_check_systemName").empty();
    $("#dev_check_defectCode").empty();
    $("#dev_check_testTaskName").empty();
    $("#dev_check_testCaseName").empty();
    $("#dev_check_defectType").empty();
    $("#dev_check_defectSource").empty();
    $("#dev_check_defectOverview").empty();
    $("#dev_check_table tbody").html("");

    $(".solution").css("display", "none");
    $("#opt_Restored").css("display", "none");
    $(".transmit").css("display", "none");
    $(".rejection").css("display", "none");
    $(".opt_rejectionDiv").css("display", "none");
    $("#opt_RejectDefect").css("display", "none");
    $("#opt_submitDefect").css("display", "none");
    $("#opt_submitAgainDefect").css("display", "none");
    $("#check_solveStatus_span").css("display", "none");
    $("#check_rejectReason_span").css("display", "none");
    $("#edit_submitDefect").css("display", "none");
    $("#edit_stagDefect").css("display", "none");

    dev_defect_reset();

    submitDefectStatus = '';
    rejectDefectData = {};
    sendRow = {};
    __defectAgainData = {};
    _repairCompleteDefectData = {};
}

/*查询当前信息*/
function checkDefect(defectId) {
    $("#loading").css('display', 'block');
    layer.open({
        type: 2,
        title: false,
        closeBtn: 0,
        shadeClose: true,
        shade: false,
        move: false,
        area: ['100%', '100%'],
        tipsMore: true,
        anim: 2,
        content: "/testManageui/defect/toCheckDefect?" + "defectId=" + defectId,
        success: function (layero, index) {
            $("#loading").css("display", "none");
        }
    });
    /*window.location.href =   "/testManageui/defect/toCheckDefect?" + "defectId=" + defectId ;*/
}

function checkDefectMessage(rows) {
    $("#check_systemName").text(rows.systemName);
    $("#check_defectCode").text(rows.defectCode);

    $("#check_req_code").text(rows.requirementCode);

    if (rows.testTaskCode != null) {
        $("#check_testTaskCode").html("<a class='a_style' onclick='getSee(" + rows.testTaskId + ")'>" + rows.testTaskCode + "</a>");
        $("#check_testTaskName").html("<a class='a_style' onclick='getSee(" + rows.testTaskId + ")'>" + isValueNull(rows.testTaskName) + "</a>");
    }

    if (rows.testSetCaseExecuteId != null) {
        $("#check_testCaseName").html('<a  class="a_style" onclick="getExecuteCaseDetails(' + rows.testSetCaseExecuteId + ',' + null + ')">' + rows.testCaseName + '</a>');
    }

    $("#check_submitUserName").text(rows.submitUserName);
    $("#check_assignUserName").text(rows.assignUserName != null ? rows.assignUserName : "");
    $("#check_defectOverview").html(rows.defectOverview);
    $("#check_defectSummary").text(rows.defectSummary);

    if (rows.defectStatus == 5 && rows.solveStatus != null) {
        $("#check_solveStatus_span").css("display", "inline");
        for (var i = 0, len = solveStatusList.length; i < len; i++) {
            if (rows.solveStatus == solveStatusList[i].value) {
                $("#check_solveStatus").text(solveStatusList[i].innerHTML);
                break;
            }
        }
    }

    if (rows.defectStatus == 3 && rows.rejectReason != null) {
        $("#check_rejectReason_span").css("display", "inline");
        for (var i = 0, len = rejectionList.length; i < len; i++) {
            if (rows.rejectReason == rejectionList[i].value) {
                $("#check_rejectReason").text(rejectionList[i].innerHTML);
                break;
            }
        }
    }

    rows["checkDefectType"] = "#check_defectType";
    rows["checkDefectSource"] = "#check_defectSource";
    rows["checkDefectStatus"] = "#check_defectStatus";
    rows["checkSeverityLevel"] = "#check_severityLevel";
    rows["checkEmergencyLevel"] = "#check_emergencyLevel";
    dicDefectSelect(rows);
}

function dicDefectSelect(rows) {
    for (var i = 0, len = defectTypeList.length; i < len; i++) {
        if (rows.defectType == defectTypeList[i].value) {
            $(rows.checkDefectType).html(defectTypeList[i].innerHTML);
            break;
        }
    }

    for (var i = 0, len = defectSourceList.length; i < len; i++) {
        if (rows.defectSource == defectSourceList[i].value) {
            $(rows.checkDefectSource).html(defectSourceList[i].innerHTML);
            break;
        }
    }

    for (var i = 0, len = defectStatusList.length; i < len; i++) {
        if (rows.defectStatus == defectStatusList[i].value) {
            $(rows.checkDefectStatus).html(defectStatusList[i].innerHTML);
            break;
        }
    }

    for (var i = 0, len = severityLevelList.length; i < len; i++) {
        if (rows.severityLevel == severityLevelList[i].value) {
            var classColor = "classColor" + rows.severityLevel;
            var severityLevelData = "<span class='" + classColor + "'>" + severityLevelList[i].innerHTML + "</span>";
            $(rows.checkSeverityLevel).html(severityLevelData);
            break;
        }
    }

    for (var i = 0, len = emergencyLevelList.length; i < len; i++) {
        if (rows.emergencyLevel == emergencyLevelList[i].value) {
            var classColor = "classColor" + rows.emergencyLevel;
            var emergencyLevellData = "<span class='" + classColor + "'>" + emergencyLevelList[i].innerHTML + "</span>";
            $(rows.checkEmergencyLevel).html(emergencyLevellData);
            break;
        }
    }
}

function down(This) {
    if ($(This).hasClass("fa-angle-double-down")) {
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
        $(This).parents('.allInfo').children(".connect_div").slideDown(100);
    } else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
        $(This).parents('.allInfo').children(".connect_div").slideUp(100);
    }
}

function showMoreInfo(This) {
    if ($(This).next(".file-upload-list").css("display") == "none") {
        $(This).html("收起备注信息 >");
        $(This).next(".file-upload-list").css("display", "block");
    } else {
        $(This).html("更多备注信息 >");
        $(This).next(".file-upload-list").css("display", "none");
    }
}

function down2(This) {
    if ($(This).hasClass("fa-angle-double-down")) {
        if (_flag == 1) {
            $("#loading").css('display', 'block');
            $.ajax({
                url: defectUrl + "defect/getDefectLogById",
                method: "post",
                data: {
                    defectId: $("#checkDefectID").val()
                },
                success: function (data) {
                    $("#loading").css('display', 'none');
                    if (data.status == 2) {
                        layer.alert(data.errorMessage, {
                            icon: 2,
                            title: "提示信息"
                        });
                    } else if (data.status == 1) {
                        if (data.data != null) {
                            var str = '';
                            for (var i = 0; i < data.data.length; i++) {
                                if (i == ( data.data.length - 1 )) {
                                    str += '<div class="logDiv lastLog">';
                                } else {
                                    str += '<div class="logDiv">'
                                }
                                var log_Type = data.data[i].logType;


                                str += '<div class="logDiv_title"><span class="orderNum"></span><span class="fontWeihgt">' + log_Type + '</span>&nbsp;&nbsp;&nbsp;' +
                                    '<span>' + data.data[i].userName + '</span>&nbsp;&nbsp;&nbsp;<span>' + data.data[i].createDate + '</span></div>' +
                                    '<div class="logDiv_cont"><div class="logDiv_contBorder">';
                                var _span = '';
                                switch (data.data[i].logType) {
                                    case "新建缺陷":
                                        str += "<br />";
                                        break;
                                    case "更新状态":
                                        var childStr = '';
                                        if (data.data[i].logDetail != null && data.data[i].logDetail.length > 0) {
                                            var logDetail = JSON.parse(data.data[i].logDetail);
                                            if (logDetail != null && logDetail.length > 0) {
                                                for (var j = 0; j < logDetail.length; j++) {
                                                    if (j == 0) {
                                                        if (logDetail[0].remark != undefined) {
                                                            childStr += "备注内容：<span class='span_font-weight'>" + logDetail[0].remark + "</span><br >";
                                                        } else {
                                                            var oldName = logDetail[j].oldValue;
                                                            var newName = logDetail[j].newValue;

                                                            var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                                                            if (logDetail[j].fieldName == "缺陷描述") {
                                                                childStr += '<span>缺陷描述已修改。</span>'
                                                            } else {
                                                                childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                                                            }
                                                        }
                                                    } else {
                                                        var oldName = logDetail[j].oldValue;
                                                        var newName = logDetail[j].newValue;

                                                        var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                                                        if (logDetail[j].fieldName == "缺陷描述") {
                                                            childStr += '<span>缺陷描述已修改。</span>'
                                                        } else {
                                                            childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                                                        }
                                                    }
                                                }
                                            } else {
                                                _span = '<span>未经任何操作</span><br/>';
                                            }
                                        } else {
                                            _span = '<span>未经任何操作</span><br/>';
                                        }
                                        if (data.data[i].logAttachementList != null && data.data[i].logAttachementList.length > 0) {
                                            _span = '';

                                            var status_1 = 0;
                                            var status_2 = 0;
                                            childStr += '<div class="file-upload-list"><table class="file-upload-tb">';
                                            for (var j = 0; j < data.data[i].logAttachementList.length; j++) {

                                                var logAttachementList = data.data[i].logAttachementList;

                                                if (logAttachementList[j].status == 1) {

                                                    childStr += '<tr><td>';
                                                    var classType = '';
                                                    var LogFileType = logAttachementList[j].fileType;
                                                    classType = classPath(LogFileType, classType);

                                                    if (status_1 == 0) {
                                                        childStr += "<span>新增附件：</span></td><td>";
                                                    } else {
                                                        childStr += "<span></span></td><td>";
                                                    }

                                                    childStr += classType;

                                                    var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                                    childStr += '&nbsp;<a  class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                                    status_1++;
                                                } else if (logAttachementList[j].status == 2) {

                                                    childStr += '<tr><td>';

                                                    var classType = '';
                                                    var LogFileType = logAttachementList[j].fileType;
                                                    classType = classPath(LogFileType, classType);

                                                    if (status_2 == 0) {
                                                        childStr += "<span>删除附件：</span></td><td>";
                                                    } else {
                                                        childStr += "<span></span></td><td>";
                                                    }

                                                    childStr += classType;

                                                    var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                                    childStr += '&nbsp;<a class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                                    status_2++;
                                                }

                                            }
                                            childStr += '</table></div>';
                                        }
                                        childStr += _span;
                                        str += '<div class="logDiv_contRemark">';
                                        str += childStr;
                                        str += '</div>';
                                        break;
                                    case "修改内容":
                                        var childStr = '';
                                        var logDetail = JSON.parse(data.data[i].logDetail);
                                        if (logDetail != null && logDetail.length > 0) {
                                            for (var j = 0; j < logDetail.length; j++) {

                                                if (j == 0) {
                                                    if (logDetail[0].remark != undefined) {
                                                        childStr += "备注内容：<span class='span_font-weight'>" + logDetail[0].remark + "</span><br >";
                                                    } else {
                                                        var oldName = logDetail[j].oldValue;
                                                        var newName = logDetail[j].newValue;

                                                        var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                                                        if (logDetail[j].fieldName == "缺陷描述") {
                                                            childStr += '<span>缺陷描述已修改。</span>'
                                                        } else {
                                                            childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                                                        }
                                                    }
                                                } else {
                                                    var oldName = logDetail[j].oldValue;
                                                    var newName = logDetail[j].newValue;

                                                    var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                                                    if (logDetail[j].fieldName == "缺陷描述") {
                                                        childStr += '<span>缺陷描述已修改。</span>'
                                                    } else {
                                                        childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + jiantou + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                                                    }
                                                }
                                            }
                                        } else {
                                            _span = '<span>未经任何操作</span><br/>';
                                        }

                                        if (data.data[i].logAttachementList != null && data.data[i].logAttachementList.length > 0) {
                                            _span = '';
                                            var status_1 = 0;
                                            var status_2 = 0;
                                            childStr += '<div class="file-upload-list"><table class="file-upload-tb">';
                                            for (var j = 0; j < data.data[i].logAttachementList.length; j++) {
                                                var logAttachementList = data.data[i].logAttachementList;

                                                if (logAttachementList[j].status == 1) {
                                                    childStr += '<tr><td>';
                                                    var classType = '';
                                                    var LogFileType = logAttachementList[j].fileType;
                                                    classType = classPath(LogFileType, classType);

                                                    if (status_1 == 0) {
                                                        childStr += "<span>新增附件：</span></td><td>";
                                                    } else {
                                                        childStr += "<span></span></td><td>";
                                                    }

                                                    childStr += classType;
                                                    var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                                    childStr += '&nbsp;<a  class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                                    status_1++;
                                                } else if (logAttachementList[j].status == 2) {
                                                    childStr += '<tr><td>';

                                                    var classType = '';
                                                    var LogFileType = logAttachementList[j].fileType;
                                                    classType = classPath(LogFileType, classType);

                                                    if (status_2 == 0) {
                                                        childStr += "<span>删除附件：</span></td><td>";
                                                    } else {
                                                        childStr += "<span></span></td><td>";
                                                    }
                                                    childStr += classType;

                                                    var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                                                    childStr += '&nbsp;<a class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                                                    status_2++;
                                                }

                                            }
                                            childStr += '</table></div>';
                                        }
                                        childStr += _span;
                                        str += '<div class="logDiv_contRemark">';
                                        str += childStr;
                                        str += '</div>';
                                        break;
                                    default:
                                        break;
                                }
                                str += '</div></div></div>';
                            }

                            $("#dealLogDiv").append(str);
                            $(This).removeClass("fa-angle-double-down");
                            $(This).addClass("fa-angle-double-up");
                            $(This).parents('.allInfo').children(".def_content").slideDown(100);
                        }
                    }
                    _flag = 0;
                },
                error: function () {
                    $("#loading").css('display', 'none');
                    layer.alert(errorDefect, {
                        icon: 2,
                        title: "提示信息"
                    });
                }
            });
        } else if (_flag == 0) {
            $(This).removeClass("fa-angle-double-down");
            $(This).addClass("fa-angle-double-up");
            $(This).parents('.allInfo').children(".def_content").slideDown(100);
        }
    } else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
    }

}

function getTestTask(testTaskId) {
    $("#loading").css('display', 'block');
    $.ajax({
        url: defectUrl + "worktask/getTestTask",
        method: "post",
        data: {
            id: testTaskId
        },
        success: function (data) {
            var testTask = data.data;
            $("#loading").css('display', 'none');
            if (testTaskModalStatus == "edit") {
                $("#edit_testTaskId").val(testTaskId);
                $("#edit_testTaskName").val(testTask.testTaskName);
                if (testTask.testStage != 'undefined' && testTask.testStage != null) {
                    $("#edit_defectSource option").each(function (i, n) {
                        if (n.value == testTask.testStage) {
                            n.selected = true;
                        }
                    });
                    $("#edit_defectSource").attr("disabled", "true");
                }
                $(".selectpicker").selectpicker('refresh');
            } else if (testTaskModalStatus == "check") {
                $("#check_testTaskName").html(testTask.testTaskName);
            }
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });


}

function logDetailList(fieldName, oldName, newName) {
    var arr = [];
    switch (fieldName) {
        case "缺陷状态":
            for (var k = 0; k < defectStatusList.length; k++) {
                if (defectStatusList[k].value != '') {
                    if (oldName == defectStatusList[k].value) {
                        oldName = defectStatusList[k].innerHTML;
                    }
                    if (newName == defectStatusList[k].value) {
                        newName = defectStatusList[k].innerHTML;
                    }
                }
            }
            break;
        case "缺陷类型":
            for (var k = 0; k < defectTypeList.length; k++) {
                if (defectTypeList[k].value != '') {
                    if (oldName == defectTypeList[k].value) {
                        oldName = defectTypeList[k].innerHTML;
                    }
                    if (newName == defectTypeList[k].value) {
                        newName = defectTypeList[k].innerHTML;
                    }
                }
            }
            break;
        case "缺陷来源":
            for (var k = 0; k < defectSourceList.length; k++) {
                if (defectSourceList[k].value != '') {
                    if (oldName == defectSourceList[k].value) {
                        oldName = defectSourceList[k].innerHTML;
                    }
                    if (newName == defectSourceList[k].value) {
                        newName = defectSourceList[k].innerHTML;
                    }
                }
            }
            break;
        case "严重级别":
            for (var k = 0; k < severityLevelList.length; k++) {
                if (severityLevelList[k].value != '') {
                    if (oldName == severityLevelList[k].value) {
                        oldName = severityLevelList[k].innerHTML;
                    }
                    if (newName == severityLevelList[k].value) {
                        newName = severityLevelList[k].innerHTML;
                    }
                }
            }
            break;
        case "紧急程度":
            for (var k = 0; k < emergencyLevelList.length; k++) {
                if (emergencyLevelList[k].value != '') {
                    if (oldName == emergencyLevelList[k].value) {
                        oldName = emergencyLevelList[k].innerHTML;
                    }
                    if (newName == emergencyLevelList[k].value) {
                        newName = emergencyLevelList[k].innerHTML;
                    }
                }
            }
            break;
        case "驳回理由":
            for (var k = 0; k < rejectionList.length; k++) {
                if (rejectionList[k].value != '') {
                    if (oldName == rejectionList[k].value) {
                        oldName = rejectionList[k].innerHTML;
                    }
                    if (newName == rejectionList[k].value) {
                        newName = rejectionList[k].innerHTML;
                    }
                }
            }
            break;
        case "解决情况状态":
            for (var k = 0; k < solveStatusList.length; k++) {
                if (solveStatusList[k].value != '') {
                    if (oldName == solveStatusList[k].value) {
                        oldName = solveStatusList[k].innerHTML;
                    }
                    if (newName == solveStatusList[k].value) {
                        newName = solveStatusList[k].innerHTML;
                    }
                }
            }
            break;
        default:
            break;
    }
    arr.push(oldName);
    arr.push(newName);
    return arr;
}

/*--------导出缺陷-----------*/
function exportDefect_btn() {
    var exportDefect_form = document.createElement("form");
    exportDefect_form.action = defectUrl + "defect/export";
    exportDefect_form.target = "_self";
    exportDefect_form.method = "post";
    exportDefect_form.style.display = "none";
    for (var x in defectInfoForExportObj) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = defectInfoForExportObj[x];
        exportDefect_form.appendChild(opt);
    }
    document.body.appendChild(exportDefect_form);
    exportDefect_form.submit();
}
//收藏
function collection() {
    if ($(".collection").children("span").hasClass("fa-heart-o")) {
        id = $(".contentNav  .nav_active", parent.document).attr('val');
        var obj = {
            search: [],
            table: [],
        };
        //搜索框数据    格式 {"type":"input / window / select" , "value": {"xxx1": $("#xx").val(),"xxx2": $("#xx").val(),...... }  }
        obj.search.push({"isCollect": true, "isData": false}) //是否收藏
        //第一行
        obj.search.push({
            "type": "input",
            "value": {"defectCode": $("#defectCode").val()},
            "isData": _is_null($("#defectCode"))
        });
        obj.search.push({
            "type": "input",
            "value": {"defectSummary": $("#defectSummary").val()},
            "isData": _is_null($("#defectSummary"))
        });
        obj.search.push({
            "type": "window",
            "value": {"submitUserId": $("#submitUserId").val(), "submitUserName": $("#submitUserName").val()},
            "isData": _is_null($("#submitUserId"), $("#submitUserName"))
        });
        obj.search.push({
            "type": "input",
            "value": {"submitDate": $("#submitDate").val()},
            "isData": _is_null($("#submitDate"))
        });
        //第二行
        obj.search.push({
            "type": "select",
            "value": {"defectStatus": $("#defectStatus").val()},
            "isData": _is_null($("#defectStatus"))
        });
        obj.search.push({
            "type": "select",
            "value": {"defectSource": $("#defectSource").val()},
            "isData": _is_null($("#defectSource"))
        });
        obj.search.push({
            "type": "select",
            "value": {"severityLevel": $("#severityLevel").val()},
            "isData": _is_null($("#severityLevel"))
        });
        obj.search.push({
            "type": "select",
            "value": {"emergencyLevel": $("#emergencyLevel").val()},
            "isData": _is_null($("#emergencyLevel"))
        });
        //第三行
        obj.search.push({
            "type": "window",
            "value": {"windowId": $("#windowId").val(), "windowName": $("#windowName").val()},
            "isData": _is_null($("#windowId"), $("#windowName"))
        });
        obj.search.push({
            "type": "window",
            "value": {"requirementCode": $("#requirementCode").val(), "requirementName": $("#requirementName").val()},
            "isData": _is_null($("#requirementCode"), $("#requirementName"))
        });
        obj.search.push({
            "type": "window",
            "value": {"sel_systemId": $("#sel_systemId").val(), "systemName": $("#systemName").val()},
            "isData": _is_null($("#sel_systemId"), $("#systemName"))
        });
        obj.search.push({
            "type": "select",
            "value": {"defectType": $("#defectType").val()},
            "isData": _is_null($("#defectType"))
        });
        //第四行
        obj.search.push({
            "type": "input",
            "value": {"reqFName": $("#reqFName").val()},
            "isData": _is_null($("#reqFName"))
        });
        obj.search.push({
            "type": "input",
            "value": {"assignUserId": $("#assignUserId").val(), "assignUser": $("#assignUser").val()},
            "isData": _is_null($("#assignUserId"), $("#assignUser"))
        });
        obj.search.push({
            "type": "input",
            "value": {"testUserId": $("#testUserId").val(), "testUser": $("#testUser").val()},
            "isData": _is_null($("#testUserId"), $("#testUser"))
        });
        obj.search.push({
            "type": "input",
            "value": {"developUserId": $("#developerId").val(), "developUserName": $("#developer").val()},
            "isData": _is_null($("#developerId"), $("#developer"))
        });
        var isResult = obj.search.some(function (item) {
            return item.isData
        })
        if (!isResult) return
        $("#loading").css('display', 'block');
        //表格数据
        for (var i = 0; i < $('#colGroup .onesCol').length; i++) {
            obj.table.push({
                "value": $('#colGroup .onesCol input').eq(i).attr('value'),
                "checked": $('#colGroup .onesCol input').eq(i).prop('checked')
            })
        }
        layer.closeAll('dialog');
        $.ajax({
            type: "post",
            url: "/system/uifavorite/addAndUpdate",
            dataType: "json",
            data: {
                'menuUrl': pageUrl,
                'favoriteContent': JSON.stringify(obj)
            },
            success: function (data) {
                if (data.status == "success") {
                    $(".collection").children("span").addClass("fa-heart");
                    $(".collection").children("span").removeClass("fa-heart-o");
                    $("#loading").css('display', 'none');
                    layer.alert('收藏成功 ！', {
                        icon: 1,
                    })
                } else if (data.status == 2) {
                    $("#loading").css('display', 'none');
                    layer.alert('收藏失败 ！', {
                        icon: 2,
                    })
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        });
    } else {
        $("#loading").css('display', 'block');
        id = $(".contentNav  .nav_active", parent.document).attr('val');

        var obj = {
            search: [],
        };
        obj.search.push({"isCollect": false, "isData": false}) //是否收藏
        $.ajax({
            type: "post",
            url: "/system/uifavorite/addAndUpdate",
            dataType: "json",
            data: {
                'menuUrl': pageUrl,
                'favoriteContent': JSON.stringify(obj)
            },
            success: function (data) {
                $("#loading").css('display', 'none');
                if (data.status == "success") {
                    $(".collection").children("span").addClass("fa-heart-o");
                    $(".collection").children("span").removeClass("fa-heart");
                    layer.alert('取消收藏 ！', {
                        icon: 2,
                    })
                }
            },
            error: function () {
                $("#loading").css('display', 'none');
                layer.alert(errorDefect, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        });
    }
}
//获取收藏
function getCollection() {
    if (!uifavorite) return
    if (!uifavorite.search) return
    var isResult = uifavorite.search.some(function (item) {
        return item.isData
    })
    if (isResult && uifavorite.search[0].isCollect) {
        for (var i in uifavorite.search) {
            if (uifavorite.search[i].type == "select") {
                for (var key in uifavorite.search[i].value) {
                    $("#" + key).selectpicker('val', uifavorite.search[i].value[key]);
                }
            } else {
                for (var key in uifavorite.search[i].value) {
                    $("#" + key).val(uifavorite.search[i].value[key]);
                }
            }
        }
        var tableFlag = 0;
        for (var i in uifavorite.table) {
            $("#colGroup .onesCol input[value^=" + uifavorite.table[i].value + "]").prop("checked", uifavorite.table[i].checked);
            if (uifavorite.table[i].checked) {
                $("#defectList").setGridParam().hideCol(uifavorite.table[i].value);
            } else {
                tableFlag = 1;
            }
        }
        if (tableFlag == 0) {
            $("#defectList").jqGrid('setGridState', 'hidden');
        }
        $("#defectList").setGridWidth($('.wode').width());
        $(".selectpicker").selectpicker('refresh');
        $(".collection").children("span").addClass("fa-heart");
        $(".collection").children("span").removeClass("fa-heart-o");
    } else {
        $(".collection").children("span").addClass("fa-heart-o");
        $(".collection").children("span").removeClass("fa-heart");
    }
}

var _seefiles = [];
// 关联测试案例详情页面
function getExecuteCaseDetails(id, setId) {
    $("#checkUpfileTable").empty();
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/testExecution/getExecuteCaseDetails",
        method: "post",
        data: {
            "testSetCaseExecuteId": id,
            'testSetCaseId': setId
        },
        success: function (data) {
            modalType = 'seeFile';
            if (data.listCase.caseExecuteResult == 2) {
                $(".successBg").css("display", "inhert");
                $(".failBg").css("display", "none");
            } else if (data.listCase.caseExecuteResult == 3) {
                $(".successBg").css("display", "none");
                $(".failBg").css("display", "inhert");
            }
            $("#checkTitle").html(data.listCase.caseNumber + " " + data.listCase.caseName);
            $("#checkNameAndTime").html(data.userName + " | " + data.listCase.createDate);
            $("#checkCasePrecondition").html(data.listCase.casePrecondition);
            $("#checkExcuteRemark").html(data.listCase.excuteRemark);

            $("#checkRelatedDefects").empty();
            for (var i = 0; i < data.listDefect.length; i++) {
                var str = '';
                str += '<div class=rowdiv><div class="def_col_5">';

                for (var j = 0; j < $("#relatedDefectsType option").length; j++) {
                    if (data.listDefect[i].defectType == $("#relatedDefectsType option").eq(j).val()) {
                        str += '<span class="classColor' + data.listDefect[i].defectType + '">' + $("#relatedDefectsType option").eq(j).text() + '</span>' +
                            '<span class="classColor' + data.listDefect[i].defectType + '">P' + $("#relatedDefectsType option").eq(j).val() + '</span>';
                    }
                }
                str += ' </div><div class="def_col_6 defectCode" title="' + data.listDefect[i].defectCode + '">' + data.listDefect[i].defectCode +
                    '</div><div class="def_col_25 fontWrop">' + data.listDefect[i].defectSummary + '</div></div>';
                $("#checkRelatedDefects").append(str);
            }

            $("#checkCaseSteps").bootstrapTable('destroy');
            $("#checkCaseSteps").bootstrapTable({
                queryParamsType: "",
                pagination: false,
                data: data.listStep,
                sidePagination: "server",
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                columns: [{
                    field: "id",
                    title: "id",
                    visible: false,
                    align: 'center'
                }, {
                    field: "stepOrder",
                    title: "步骤",
                    width: 50,
                    align: 'center'
                }, {
                    field: "stepDescription",
                    title: "步骤描述",
                    width: 300,
                    align: 'center'
                }, {
                    field: "stepExpectedResult",
                    title: "预期结果",
                    align: 'center'
                }, {
                    field: "stepExecuteResult",
                    title: "执行结果",
                    width: 100,
                    align: 'center',
                    formatter: function (value, row, index) {
                        for (var j = 0; j < $("#executionSelect option").length; j++) {
                            if (row.stepExecuteResult == $("#executionSelect option").eq(j).val()) {
                                return '<span class="spanClass' + row.stepExecuteResult + '">' + $("#executionSelect option").eq(j).text() + '</span>';
                            }
                        }
                    }
                }, {
                    field: "stepActualResult",
                    title: "执行情况",
                    align: 'center'
                }],
                onLoadSuccess: function () {
                },
                onLoadError: function () {
                }
            });
            if ((data.listFile).length > 0) {
                var _table = $("#checkUpfileTable");
                var attMap = data.listFile;
                //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                for (var i = 0; i < attMap.length; i++) {
                    var _tr = '';
                    var file_name = attMap[i].fileNameOld;
                    var file_type = attMap[i].fileType;
                    var _td_icon = filePicClassPath(file_type); // common.js

                    var row = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    var _td_name = '<a style="font-family:Helvetica Neue,Helvetica,Arial,sans-serif;" onclick="downloadTaseCase(' + row + ')">' + file_name + '</a><i class = "file-bucket">' + attMap[i].fileS3Bucket + '</i><i class = "file-s3Key">' + attMap[i].fileS3Key + '</i></td>';

                    _tr += '<tr><td><div class="fileTb" style="cursor:pointer" >' + _td_icon + " " + _td_name + '</tr>';
                    _table.append(_tr);
                    _seefiles.push(data.listFile[i]);
                    $("#seeFiles").val(JSON.stringify(_seefiles));
                }
            }
            $(".right_div").css("display", "none"); //显示右边的案例div
            $(".rightCaseDivCheck").css("display", "block"); //显示右边的案例div
            $("#loading").css('display', 'none');
            $("#checkTestCaseModal").modal("show");
        },
        error: function () {
            $("#loading").css('display', 'none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

function downloadTaseCase(row) {
    var fileS3Bucket = row.fileS3Bucket;
    var fileS3Key = row.fileS3Key;
    var fileNameOld = row.fileNameOld;
    window.location.href = "/testManage/worktask/downloadFile?fileS3Bucket=" + fileS3Bucket + "&fileS3Key=" + fileS3Key + "&fileNameOld=" + fileNameOld;
}

function showProjectGroups() {
    var systemId = $("#systemId").val();
    if (systemId == "") {
        layer.alert("请先选择系统！", {
            icon: 0,
            title: "提示信息"
        });
        return false;
    } else {
        projectGroupTreeSetting.async.otherParam = ["systemId", systemId];
        $.fn.zTree.init($("#projectGroupMenuTree"), projectGroupTreeSetting);
        var projectGroupOffset = $("#projectGroupId").offset();
        var projectGroupObj = $("#projectGroupId");
        $("#projectGroupMenuContent").css({
            left: projectGroupOffset.left + "px",
            top: projectGroupOffset.top + projectGroupObj.outerHeight() + "px"
        }).slideDown("fast");
        $("body").bind("mousedown", onProjectMenuContentBodyDown);
    }
}

function showAssetSystemTrees() {
    var systemId = $("#systemId").val();
    var systemCode = $("#systemCode").val();
    if (systemId == "") {
        layer.alert("请先选择系统！", {
            icon: 0,
            title: "提示信息"
        });
        return false;
    }
    if (systemCode == "") {
        layer.alert("系统编码为空！", {
            icon: 0,
            title: "提示信息"
        });
        return false;
    }
    assetSystemTreeSetting.async.otherParam = ["systemCode", systemCode];
    $.fn.zTree.init($("#assetSystemTree"), assetSystemTreeSetting);
    var assetSystemTreeOffset = $("#assetSystemTreeId").offset();
    var assetSystemTreeObj = $("#assetSystemTreeId");
    $("#assetSystemTreeContent").css({
        left: assetSystemTreeOffset.left + "px",
        top: assetSystemTreeOffset.top + assetSystemTreeObj.outerHeight() + "px"
    }).slideDown("fast");
    $("body").bind("mousedown", onAssetSystemTreeContentBodyDown);
}

function showProjectGroupsForEdit() {
    var systemId = $("#edit_systemId").val();
    if (systemId == "") {
        layer.alert("请先选择系统！", {
            icon: 0,
            title: "提示信息"
        });
        return false;
    } else {
        projectGroupTreeSetting.callback.onClick = projectGroupTreeOnClickForEdit;
        projectGroupTreeSetting.callback.onAsyncSuccess = projectGroupTreeOnAsyncSuccessForEdit;
        projectGroupTreeSetting.async.otherParam = ["systemId", systemId];
        $.fn.zTree.init($("#projectGroupMenuTree"), projectGroupTreeSetting);
        var projectGroupOffset = $("#edit_projectGroupId").offset();
        var projectGroupObj = $("#edit_projectGroupId");
        $("#projectGroupMenuContent").css({
            left: projectGroupOffset.left + "px",
            top: projectGroupOffset.top + projectGroupObj.outerHeight() + "px"
        }).slideDown("fast");
        $("body").bind("mousedown", onProjectMenuContentBodyDownForEdit);
    }
}

function showAssetSystemTreesForEdit() {
    var systemId = $("#edit_systemId").val();
    var systemCode = $("#edit_systemCode").val();
    if (systemId == "") {
        layer.alert("请先选择系统！", {
            icon: 0,
            title: "提示信息"
        });
        return false;
    }
    if (systemCode == "") {
        layer.alert("系统编码为空！", {
            icon: 0,
            title: "提示信息"
        });
        return false;
    }
    assetSystemTreeSetting.callback.onClick = assetSystemTreeOnClickForEdit;
    assetSystemTreeSetting.callback.onAsyncSuccess = assetSystemTreeOnAsyncSuccessForEdit;
    assetSystemTreeSetting.async.otherParam = ["systemCode", systemCode];
    $.fn.zTree.init($("#assetSystemTree"), assetSystemTreeSetting);
    var assetSystemTreeOffset = $("#edit_assetSystemTreeId").offset();
    var assetSystemTreeObj = $("#edit_assetSystemTreeId");
    $("#assetSystemTreeContent").css({
        left: assetSystemTreeOffset.left + "px",
        top: assetSystemTreeOffset.top + assetSystemTreeObj.outerHeight() + "px"
    }).slideDown("fast");
    $("body").bind("mousedown", onAssetSystemTreeContentBodyDown);
}

function onProjectMenuContentBodyDown(event) {
    if (!(event.target.id == "projectGroupId" || event.target.id == "projectGroupMenuContent" || $(event.target).parents("#projectGroupMenuContent").length > 0)) {
        hideProjectGroupMenu();
    }
}

function onProjectMenuContentBodyDownForEdit(event) {
    if (!(event.target.id == "edit_projectGroupId" || event.target.id == "projectGroupMenuContent" || $(event.target).parents("#projectGroupMenuContent").length > 0)) {
        hideProjectGroupMenuForEdit();
    }
}

function onAssetSystemTreeContentBodyDown(event) {
    if (!(event.target.id == "assetSystemTreeId" || event.target.id == "assetSystemTreeContent" || $(event.target).parents("#assetSystemTreeContent").length > 0)) {
        hideAssetSystemTree();
    }
}

function onAssetSystemTreeContentBodyDownForEdit(event) {
    if (!(event.target.id == "edit_assetSystemTreeId" || event.target.id == "assetSystemTreeContent" || $(event.target).parents("#assetSystemTreeContent").length > 0)) {
        hideAssetSystemTreeForEdit();
    }
}

function hideProjectGroupMenu() {
    $("#projectGroupMenuContent").fadeOut("fast");
    $("body").unbind("mousedown", onProjectMenuContentBodyDown);
}

function hideProjectGroupMenuForEdit() {
    $("#projectGroupMenuContent").fadeOut("fast");
    $("body").unbind("mousedown", onProjectMenuContentBodyDownForEdit);
}

function hideAssetSystemTree() {
    $("#assetSystemTreeContent").fadeOut("fast");
    $("body").unbind("mousedown", onAssetSystemTreeContentBodyDown);
}

function hideAssetSystemTreeForEdit() {
    $("#assetSystemTreeContent").fadeOut("fast");
    $("body").unbind("mousedown", onAssetSystemTreeContentBodyDownForEdit);
}

function projectGroupTreeOnClick(e, treeId, treeNode) {
    var selectedNodes = $.fn.zTree.getZTreeObj("projectGroupMenuTree").getSelectedNodes();
    $("#projectGroupId").val(selectedNodes[0].name);
    $("#projectGroupId").attr("idValue", selectedNodes[0].id);
    $("#projectGroupId").next().show();
}

function projectGroupTreeOnClickForEdit(e, treeId, treeNode) {
    var selectedNodes = $.fn.zTree.getZTreeObj("projectGroupMenuTree").getSelectedNodes();
    $("#edit_projectGroupId").val(selectedNodes[0].name);
    $("#edit_projectGroupId").attr("idValue", selectedNodes[0].id);
    $("#edit_projectGroupId").next().show();
}

function assetSystemTreeOnClick(e, treeId, treeNode) {
    var selectedNodes = $.fn.zTree.getZTreeObj("assetSystemTree").getSelectedNodes();
    $("#assetSystemTreeId").val(selectedNodes[0].businessSystemTreeName);
    $("#assetSystemTreeId").attr("idValue", selectedNodes[0].id);
    $("#assetSystemTreeId").next().show();
}

function assetSystemTreeOnClickForEdit(e, treeId, treeNode) {
    var selectedNodes = $.fn.zTree.getZTreeObj("assetSystemTree").getSelectedNodes();
    $("#edit_assetSystemTreeId").val(selectedNodes[0].businessSystemTreeName);
    $("#edit_assetSystemTreeId").attr("idValue", selectedNodes[0].id);
    $("#edit_assetSystemTreeId").next().show();
}

function projectGroupTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
    var zTree = $.fn.zTree.getZTreeObj("projectGroupMenuTree");
    zTree.expandAll(false);
    var selectedProjectGroupId = $("#projectGroupId").attr("idValue");
    var node = zTree.getNodeByParam("id", selectedProjectGroupId, null);
    zTree.selectNode(node, false, false);
}

function projectGroupTreeOnAsyncSuccessForEdit(event, treeId, treeNode, msg) {
    var zTree = $.fn.zTree.getZTreeObj("projectGroupMenuTree");
    zTree.expandAll(false);
    var selectedProjectGroupId = $("#edit_projectGroupId").attr("idValue");
    var node = zTree.getNodeByParam("id", selectedProjectGroupId, null);
    zTree.selectNode(node, false, false);
}

function assetSystemTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
    var zTree = $.fn.zTree.getZTreeObj("assetSystemTree");
    zTree.expandAll(false);
    var selectedAssetSystemTreeId = $("#assetSystemTreeId").attr("idValue");
    var node = zTree.getNodeByParam("id", selectedAssetSystemTreeId, null);
    zTree.selectNode(node, false, false);
}

function assetSystemTreeOnAsyncSuccessForEdit(event, treeId, treeNode, msg) {
    var zTree = $.fn.zTree.getZTreeObj("assetSystemTree");
    zTree.expandAll(false);
    var selectedAssetSystemTreeId = $("#edit_assetSystemTreeId").attr("idValue");
    var node = zTree.getNodeByParam("id", selectedAssetSystemTreeId, null);
    zTree.selectNode(node, false, false);
}

var projectGroupTreeSetting = {
    async: {
        enable: true,
        type: "POST",
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        url: "/projectManage/project/getProjectGroupTreeBySystemId",
        otherParam: ["systemId", $("#systemId").val()]
    },
    view: {
        dblClickExpand: false,
        selectedMulti: false//表示禁止多选
    },
    data: {
        simpleData: {
            enable: true,//表示使用简单数据模式
            idKey: "id",//设置之后id为在简单数据模式中的父子节点关联的桥梁
            pidKey: "pId",//设置之后pid为在简单数据模式中的父子节点关联的桥梁和id互相对应
            rootPId: null//pid为null的表示根节点
        },
        key: {
            title: "projectName"
        }
    },
    callback: {
        onClick: projectGroupTreeOnClick,
        onAsyncSuccess: projectGroupTreeOnAsyncSuccess,
        onAsyncError: function (event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status != 999) {
                layer.alert("获取项目组异常", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        }
    }
};

var assetSystemTreeSetting = {
    async: {
        enable: true,
        type: "POST",
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        url: "/projectManage/systemTree/getAssetSystemTreeBySystemCode",
        otherParam: ["systemCode", $("#systemCode").val()]
    },
    view: {
        dblClickExpand: false,
        selectedMulti: false//表示禁止多选
    },
    data: {
        key: {
            name: "businessSystemTreeName",
        },
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: null
        }
    },
    callback: {
        onClick: assetSystemTreeOnClick,
        onAsyncSuccess: assetSystemTreeOnAsyncSuccess,
        onAsyncError: function (event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status != 999) {
                layer.alert("获取系统资产树异常", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        }
    }
};

function systemVersionBindCreateOptions(object, selectedValue) {
    var oldValue = "";
    var obj = $(object);
    $.ajax({
        type: "POST",
        url: "/devManage/version/getSystemVersions",
        dataType: "json",
        data: {
            systemId: $("#systemId").val()
        },
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        beforeSend: function () {
            oldValue = obj.val();
            obj.find("option").not(":first").remove();
        },
        success: function (msg) {
            if (msg.flag) {
                for (var i in msg.data) {
                    obj.append('<option value="' + msg.data[i].id + '">' + msg.data[i].version + '</option>');
                }
            } else {
                layer.alert("加载系统版本选项失败", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status != 999) {
                layer.alert("系统异常，请联系管理员", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        complete: function (XMLHttpRequest, textStatus) {
            obj.selectpicker('refresh');
            if (selectedValue != undefined) {
                obj.selectpicker("val", selectedValue);
            } else {
                obj.selectpicker("val", oldValue);
            }
        }
    });
}

function systemVersionBindCreateOptionsForEdit(object, selectedValue) {
    var oldValue = "";
    var obj = $(object);
    $.ajax({
        type: "POST",
        url: "/devManage/version/getSystemVersions",
        dataType: "json",
        data: {
            systemId: $("#edit_systemId").val()
        },
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        beforeSend: function () {
            oldValue = obj.val();
            obj.find("option").not(":first").remove();
        },
        success: function (msg) {
            if (msg.flag) {
                for (var i in msg.data) {
                    obj.append('<option value="' + msg.data[i].id + '">' + msg.data[i].version + '</option>');
                }
            } else {
                layer.alert("加载系统版本选项失败", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status != 999) {
                layer.alert("系统异常，请联系管理员", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        complete: function (XMLHttpRequest, textStatus) {
            obj.selectpicker('refresh');
            if (selectedValue != undefined) {
                obj.selectpicker("val", selectedValue);
            } else {
                obj.selectpicker("val", oldValue);
            }
        }
    });
}

function estimateWorkloadReformat(obj) {
    if (obj.value.indexOf(".") == 0 && obj.value != "") {
        obj.value = parseFloat(obj.value);
    }
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d).*$/, '$1$2.$3');
    $("#newDefectFrom").data("bootstrapValidator").updateStatus("estimateWorkload", "NOT_VALIDATED", null);
}

function estimateWorkloadReformatForEdit(obj) {
    if (obj.value.indexOf(".") == 0 && obj.value != "") {
        obj.value = parseFloat(obj.value);
    }
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d).*$/, '$1$2.$3');
    $("#edit_DefectFrom").data("bootstrapValidator").updateStatus("edit_estimateWorkload", "NOT_VALIDATED", null);
}

function clearSelectedProjectGroup(obj) {
    $(obj).prev().val('');
    $(obj).prev().attr('idValue', '');
    $(obj).hide();
}

function clearSelectedAssetSystemTree(obj) {
    $(obj).prev().val('');
    $(obj).prev().attr('idValue', '');
    $(obj).hide();
}


//===========================项目========================
//选择项目配置
function select_project(){
	fuzzy_search_radio({   
	    'ele': 'newProjectName',
	    'url': '/testManage/testtask/getProjectListByProjectName',
	    'params': {
	    		projectName: '',
	    },
	    'name': 'projectName',
	    'code': 'projectCode',
	    'id': 'id',
	    'top': '29px',
	    'dataName': 'projectInfoList',
	    'userId': $('#newProjectId'),
	    'out': true,
	    'not_jqgrid': true,
	});
	fuzzy_search_radio({   
	    'ele': 'editProjectName',
	    'url': '/testManage/testtask/getProjectListByProjectName',
	    'params': {
	    		projectName: '',
	    },
	    'name': 'projectName',
	    'code': 'projectCode',
	    'id': 'id',
	    'top': '29px',
	    'dataName': 'projectInfoList',
	    'userId': $('#editProjectId'),
	    'out': true,
	    'not_jqgrid': true,
	});
	$('#newProjectName_list').on('click', '.newProjectName_search_item', function () {
		var data = JSON.parse($(this).attr('data'));
		var val = data.projectName;
		var id = $(this).data('id');
		var systemId = data.systemId;
        var systemName = data.systemName;
        var systemCode = data.systemCode;
		$('#newProjectId').val(id);
		$('#newProjectName').val(val).attr('username',val).change();
		if($(this).data('type') == 2){    //新建类
			$("#new_windowId").val('');
	        $("#new_windowName").val('');
			$('.is_new_project').hide(); 
		}else{
			$('.is_new_project').show();
		}
		if(systemId){
			$('#systemId').val(systemId);
			$('#system_Name').val(systemName).change();
			$('#systemCode').val(systemCode);
		}else{
			get_project_system(1);
		}
		$('#newProjectName_list').hide();
	})
	$('#editProjectName_list').on('click', '.editProjectName_search_item', function () {
		var data = JSON.parse($(this).attr('data'));
		var val = data.projectName;
		var id = $(this).data('id');
		var systemId = data.systemId;
        var systemName = data.systemName;
        var systemCode = data.systemCode;
		$('#editProjectId').val(id);
		$('#editProjectName').val(val).attr('username',val).change();
		if($(this).data('type') == 2){ //新建项目
			$("#edit_windowId").val('');
	        $("#edit_windowName").val('');
			$('.is_new_project').hide();
		}else{
			$('.is_new_project').show();
		}
		if(systemId){
			$('#edit_systemId').val(systemId);
			$('#edit_system_Name').val(systemName).change();
			$('#edit_systemCode').val(systemCode);
		}else{
			get_project_system(2);
		}
		$('#editProjectName_list').hide();
	})
}


//获取项目下的系统
function get_project_system(status) {
	var projectId = '';
	if (status  == 1) {
		projectId = $("#newProjectId").val();
	} else if (status  == 2) {
		projectId = $("#editProjectId").val();
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testtask/selectAllSystemInfo2",
		dataType: "json",
		type: "post",
		data: {
			systemName: '',
			systemCode: '',
			projectId: projectId,
			pageNumber: 1,
			pageSize: 99,
		},
		success: function (data) {
			if(data.rows.length){
				var systemIds = data.rows.map(function(v){
					return v.id;
				})
				if(status  == 2){
					if(!systemIds.includes(+$('#edit_systemId').val())){
						$('#edit_systemId').val('');
						$('#edit_system_Name').val('').change();
						$('#edit_systemCode').val('');
					}
				}else if(status  == 1){
					if(!systemIds.includes(+$('#systemId').val())){
						$('#systemId').val('');
						$('#system_Name').val('').change();
						$('#systemCode').val('');
					}
				}
			}else{
				$('#edit_systemId').val('');
				$('#edit_system_Name').val('').change();
				$('#edit_systemCode').val('');
				$('#systemId').val('');
				$('#system_Name').val('').change();
				$('#systemCode').val('');
			}
			$("#loading").css('display', 'none');
		}
	})
}


function urlParam(){
	if( parameterArr.obj.defectStatus ){ 
		var arr = parameterArr.obj.defectStatus;
		arr.split(",").map(function(val,index){
			$("#defectStatus option[value="+ val +"]").attr("selected", true);
		})
	} 
	if( parameterArr.obj.userId ){  
		$("#assignUserId").val( parameterArr.obj.userId )
		$("#assignUser").val( decodeURIComponent(parameterArr.obj.userName)  )
	}
	$(".selectpicker").selectpicker('refresh');
}



