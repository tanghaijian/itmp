
var _icon_word = "../static/images/devtask/word.png",_icon_excel = "../static/images/devtask/excel.png",_icon_text = "../static/images/devtask/text.png",_icon_pdf = "../static/images/devtask/pdf.png";
var _files = [],_editfiles = [],_checkfiles = [],modalType = '',_handlefiles = [],_SeeFiles = [],_Newhandlefiles = [],_Dhandlefiles = [],_newDhandlefiles = [],Neweditfiles = [],deleteAttaches = [],examineAttaches = [],newexamineAttaches = [];
var taskStateList = '';

var tableIdArr=[ 'userTable' , 'reqTable' ,'TaskTable' ,'systemTable','TestPopupTable','EditPopupTable','withinUserTable' ,'comWindowTable'];
var jQDataType="";
var modified = "<span>&nbsp;修改为&nbsp;</span>";
var userStatus = '';
var windowTypeList = [];
var selectWinIds = [];
var assignUser = '';
var parameterArr = {};
var isBegain = true;
var favoriteContent = '';// 初始筛选器内容
var flagNum="";
var filter = '';// 页面初始化参数
parameterArr.arr = window.location.href.split("?");
parameterArr.parameterArr = parameterArr.arr[1]?parameterArr.arr[1].split("&"):[];
parameterArr.obj = {};
for (var i = 0; i < parameterArr.parameterArr.length; i++) {
    var obj = parameterArr.parameterArr[i].split("=");
    parameterArr.obj[obj[0]] = obj[1];
}
$(function () {
	getFilter();
	
    getProject();
    banEnterSearch();
    taskStateList = $("#taskState").find("option");
    urlParam();
    pageInit(1);
    addTitle();
    addCheckBox();
    tableClearSreach();
    uploadFileList();
    downOrUpButton();
    dateComponent();
    //时间控件 配置参数信息
    var _timeArr_ = ["actualStart", "actualEnd", "planStartDate", "planEndDate",'edit_planStartDate','edit_planEndDate'];
    _timeArr_.forEach(function (item) {
        $("#" + item).datetimepicker({
            minView: "month",
            format: "yyyy-mm-dd",
            autoclose: true,
            todayBtn: true,
            language: 'zh-CN',
            pickerPosition: "top-left"
        }).on('change', function () {
            $(this).parent().children(".btn_clear").css("display", "block");
        });
    })

    var _Time_ = ["startWork", "endWork", "DactualStart"];
    _Time_.forEach(function (item) {
        $("#" + item).datetimepicker({
            minView: "month",
            format: "yyyy-mm-dd",
            autoclose: true,
            todayBtn: true,
            language: 'zh-CN',
            pickerPosition: "bottom-left"
        })
    })

    $("#editStartDate").datetimepicker({
        minView: "month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    }).on('change', function () {
        $('#editTestF').data('bootstrapValidator')
            .updateStatus('editEndDate', 'NOT_VALIDATED', null)
            .validateField('editEndDate');
    });
    $("#editEndDate").datetimepicker({
        minView: "month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    }).on('change', function () {
        $('#editTestF').data('bootstrapValidator')
            .updateStatus('editStartDate', 'NOT_VALIDATED', null)
            .validateField('editStartDate');
    });
    //所有的Input标签，在输入值后出现清空的按钮
    $('input[type="text"]').parent().css("position", "relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange change", function () {
        if ($(this).val() != "") {
            $(this).parent().children(".btn_clear").css("display", "block");
        } else {
            $(this).parent().children(".btn_clear").css("display", "none");
        }
    });

    $('.clear').parent().css("position","relative");
    $('.clear').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $(".clear").change(function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    });

    $('.clear').bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    })

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
					"menuUrl": "/testManageui/workTask/toWorktask",
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
					"menuUrl": "/testManageui/workTask/toWorktask",
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
    
    // 搜索 测试人员
    $("#sel_developer_user").click(function () {
        $("#developer").val(currentUserName);
        $("#man_devUserId").val(currentUserId).attr('username',currentUserName);
    });

    // 新建 任务分配
    $("#new_taskUserId_user").click(function () {
        $("#new_taskUserId").val(currentUserId);
        $("#new_taskUser").val(currentUserName).attr('username',currentUserName).change();
    });

    // 编辑 任务分配
    $("#edit_edtaskUser_user").click(function () {
        $("#edit_taskUserId").val(currentUserId);
        $("#edtaskUser").val(currentUserName).attr('username',currentUserName).change();
    });



    //分派
    $("#assignUser_user").click(function () {
        $("#assignUser").val(currentUserName);
        $("#assignUser").attr("assignUserID", currentUserId);
    });

    $("#windowName").click(function() {
        winReset();
        selectWinIds=[];
        $("#comWindowModal").modal("show");
        searchWindows();

    });
   
    formValidator();
    oac_FormValidator();
    refactorFormValidator();
    collect_handle();

    addClickRow( tableIdArr );

    search_settings();

    if (!uifavorite) return;
    getCollection();

});

//模糊搜索配置
function search_settings(){
    var search_arr = [
        {
            ele : 'new_taskUser',
            userId : 'new_taskUserId',
        },
        {
            ele : 'add_taskAssignUser',
            userId : 'add_taskAssignUserId',
        },
        {
            ele : 'edtaskUser',
            userId : 'edit_taskUserId',
        },
        {
            ele : 'edit_taskAssignUser',
            userId : 'edit_taskAssignUserId',
        },
        {
            ele : 'TransferUser',
            userId : 'TransferUser_Id',
        },
    ]
    search_arr.map(function(val){
        fuzzy_search_radio2({
            ele : val.ele,
            url : '/testManage/testtask/getUserByNameOrACC',
            top : '28px',
            name : 'userName',
            account : 'userAccount',
            id : 'id',
            userId : $("#"+val.userId),
            rows : 'userInfo',
        });
    })
}

// 搜索框 收藏按钮 js 部分
function collect_handle() {
    $(".collection").click(function () {
        if ($(this).children("span").hasClass("fa-heart-o")) {
            id = $(".contentNav  .nav_active", parent.document).attr('val');
            var obj = {
                search: [],  //查询搜索框数据
                table: [],  //表格搜索框数据
            };
            //搜索框数据    格式 {"type":"input / window / select" , "value": {"xxx1": $("#xx").val(),"xxx2": $("#xx").val(),...... }  }
            obj.search.push({ "isCollect": true, "isData":  false } ) //是否收藏
            //第一行
            obj.search.push({ "type": "input", "value": { "taskCode": $("#taskCode").val() }, "isData":  _is_null($("#taskCode")) } );
            obj.search.push({ "type": "input", "value": { "taskName": $("#taskName").val() }, "isData":  _is_null($("#taskName"))});
            obj.search.push({ "type": "window", "value": { "man_devUserId": $("#man_devUserId").val(), "developer": $("#developer").val() }, "isData":  _is_null($("#man_devUserId"),$("#developer"))  });
            obj.search.push({ "type": "select", "value": { "taskState": $("#taskState").val() }, "isData":  _is_null($("#taskState"))  });
            //第二行
            obj.search.push({ "type": "window", "value": { "parallelTask": $("#parallelTask").val() }, "isData":  _is_null($("#parallelTask"))  });
            obj.search.push({ "type": "window", "value": { "relationDemand": $("#relationDemand").val() }, "isData":  _is_null($("#relationDemand"))  });
            obj.search.push({ "type": "window", "value": { "involveSystem": $("#involveSystem").val() }, "isData":  _is_null($("#involveSystem"))  });
            obj.search.push({ "type": "window", "value": { "windowId": $("#windowId").val(), "windowName": $("#windowName").val() }, "isData":  _is_null($("#windowId"),$("#windowName"))  });
            //第三行
            obj.search.push({ "type": "window", "value": { "taskAssignUserId": $("#taskAssignUserId").val(), "taskAssignUser": $("#taskAssignUser").val() }, "isData":  _is_null($("#taskAssignUserId"),$("#taskAssignUser"))  });
            var isResult = obj.search.some(function (item) {
                return item.isData
            })
            if (!isResult) return;
            $("#loading").css('display', 'block');
            //表格数据
            for (var i = 0; i < $('#colGroup .onesCol').length; i++) {
                obj.table.push({ "value": $('#colGroup .onesCol input').eq(i).attr('value'), "checked": $('#colGroup .onesCol input').eq(i).prop('checked') })
            }
            $.ajax({
                type: "post",
                url: "/system/uifavorite/addAndUpdate",
                dataType: "json",
                data: {
                    'menuUrl': pageUrl,
                    'favoriteContent': JSON.stringify(obj),
                },
                success: function (data) {
                    if (data.status == "success") {
                        $('.collection').children("span").addClass("fa-heart");
                        $('.collection').children("span").removeClass("fa-heart-o");
                        $("#loading").css('display', 'none');
                        layer.alert('收藏成功!', {
                            icon: 1,
                        })
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
                }
            });
        } else {
            id = $(".contentNav  .nav_active", parent.document).attr('val');
            var obj = {
                search: [],  //查询搜索框数据
            };
            obj.search.push({ "isCollect": false, "isData":  false  })
            $("#loading").css('display', 'block');
            $.ajax({
                type: "post",
                url: "/system/uifavorite/addAndUpdate",
                dataType: "json",
                data: {
                    'menuUrl': pageUrl,
                    'favoriteContent': JSON.stringify(obj),
                },
                success: function (data) {
                    if (data.status == "success") {
                        $('.collection').children("span").addClass("fa-heart-o");
                        $('.collection').children("span").removeClass("fa-heart");
                        $("#loading").css('display', 'none');
                        layer.alert('取消收藏!', {
                            icon: 2,
                        })
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
                }
            });
        }
    })
}
//获取收藏和列表
function getCollection() {
    if (!uifavorite.search) return;
    var isResult = uifavorite.search.some(function (item) {
        return item.isData
    })
    if (isResult && uifavorite.search[0].isCollect) {
        for (var i in uifavorite.search) {
            if (uifavorite.search[i].type == "select") {
                for (var key in uifavorite.search[i].value) {
                    $("#" + key).selectpicker('val', uifavorite.search[i].value[key]).change();
                }
            } else {
                for (var key in uifavorite.search[i].value) {

                    $("#" + key).val(uifavorite.search[i].value[key]).change();
                }
            }
        }
        var tableFlag = 0;
        for (var i in uifavorite.table) {
            $("#colGroup .onesCol input[value^=" + uifavorite.table[i].value + "]").prop("checked", uifavorite.table[i].checked);
            if (uifavorite.table[i].checked) {
                $("#testlist").setGridParam().hideCol(uifavorite.table[i].value);
            } else {
                tableFlag = 1;
            }
        }
        if (tableFlag == 0) {
            $("#testlist").jqGrid('setGridState', 'hidden');
        }
        $("#testlist").setGridWidth($('.wode').width());
        $(".selectpicker").selectpicker('refresh');
        $(".collection").children("span").addClass("fa-heart");
        $(".collection").children("span").removeClass("fa-heart-o");
    } else {
        $(".collection").children("span").addClass("fa-heart-o");
        $(".collection").children("span").removeClass("fa-heart");
    }
}

var colNameArr = ['id', '任务编号', '任务名称', '状态', '涉及系统', '关联需求', '关联测试任务', '测试人员','测试阶段','投产窗口','任务分配', '案例', '缺陷', '操作'];
var colIndex = ['id','testTaskCode','testTaskName','testTaskStatus','systemName','requirementCode','featureCode','userName','testStage','windowName','taskAssignUserName','caseNum','defectNum'];

//表格数据加载
function pageInit(Num) {
	if(filter==""){
		filter= $("#testlist").jqGrid('getGridParam', "postData");
	}
   /* var page = $('#testlist').getGridParam('page');
    $("#testlist").jqGrid("clearGridData");
    $("#testlist").jqGrid("setGridParam", { page: page != null && page != undefined ? page : 1 });
    */
	jQuery("#testlist").jqGrid({
		url: '/testManage/worktask/getAllWorktask',
		datatype: 'json',
		mtype: "post",
		height: 'auto',
		postData: { 
			"filters":filter
		},
		// multiselect: false,
		autowidth: true,
        //显示水平滚动条
        altRows: true,
        altclass: 'differ',
        shrinkToFit:false,
        autoScroll: true,
		colNames: colNameArr,
		colModel: [
	            { name: 'id', index: 'id', hidden: true },
	            {
	                name: 'testTaskCode', index: 'testTaskCode', sorttype: 'string', searchoptions: { sopt: ['cn'] },
	                formatter: function (value, grid, rows) {
	                    return '<a class="a_style" href="#" onclick="getSee(\'' + rows.id + '\')">' + value + '</a>';
	                }
	            },
	            {
	                name: 'testTaskName', index: 'testTaskName', searchoptions: { sopt: ['cn'] },
	                formatter: function (value, grid, rows) {
	                    return '<a class="a_style" href="#" onclick="getSee(\'' + rows.id + '\')">' + value + '</a>';
	                }
	            },
	            {
	                name: 'testTaskStatus', index: 'testTaskStatus', searchoptions: { sopt: ['cn'] },
	                formatter: function (value, grid, rows) {
	                    var classDoing = 'doing';
	                    for (var i = 0, len = taskStateList.length; i < len; i++) {
	                        if (value == taskStateList[i].value) {
	                            return '<span class="' + classDoing + value + '">' + taskStateList[i].innerHTML + '</span>';
	                        }
	                    }

	                }
	            },
	            { name: 'systemName', index: 'systemName', searchoptions: { sopt: ['cn'] } },
	            { name: 'requirementCode', index: 'requirementCode', searchoptions: { sopt: ['cn'] } },
	            { name: 'featureCode', index: 'featureCode', searchoptions: { sopt: ['cn'] } },
	            { name: 'userName', index: 'userName', searchoptions: { sopt: ['cn'] } },
	            { name: 'testStage', index: 'testStage',searchoptions: { sopt: ['cn'] } ,
	                formatter: function (value, grid, rows, state) {
	                    if( rows.testStage == 1 ){
	                        return '系测';
	                    }else if(  rows.testStage == 2 ){
	                        return '版测';
	                    }
	                    return '';
	                }
	            },
	            { name: 'windowName', index: 'windowName', searchoptions: { sopt: ['cn'] } },
	            { name: 'taskAssignUserName', index: 'taskAssignUserName', searchoptions: { sopt: ['cn'] } },
	            {
	                name: 'caseNum', index: 'caseNum',resize: false,  width: 100, searchoptions:{sopt:['cn']},  formatter: function (value, grid, rows, state) {
	                    if (value == null) {
	                        return '';
	                    } else {
	                        var row = JSON.stringify(rows).replace(/"/g, '&quot;');
	                        return value;
	                    }
	                }
	            },
	            {
	                name: 'defectNum', index: 'defectNum',  resize: false, width: 100, searchoptions:{sopt:['cn']}, formatter: function (value, grid, rows, state) {
	                    if (value == null) {
	                        return '';
	                    } else {
	                        var row = JSON.stringify(rows).replace(/"/g, '&quot;');
	                        return '<a  class="a_style" onclick="showDefect(' + row + ')">' + value + '</span>'
	                    }
	                }
	            },
	            {
	                name: 'opt',
	                index: 'opt',
	                align: "center",
	                fixed: true,
	                sortable: false,
	                resize: false,
	                search: false,
	                width: 170,
	                formatter: function (value, grid, rows, state) {
	                    var editOpt = '<a class="a_style" href="#" onclick="edit(\'' + rows.id + '\')">编辑</a>';
	                    var HandleOpt = '<a class="a_style" href="#" onclick="Handle(\'' + rows.id +"','"+rows.taskAssignUserId + '\')">处理</a>';
	                    var TransferOpt = '<a class="a_style" href="#" onclick="Transfer(\'' + rows.id +"','"+rows.taskAssignUserId +'\')">转派</a>';
	                    var assigOpt = '<a class="a_style" href="#" onclick="assig(\'' + rows.id +"','"+rows.taskAssignUserId + '\')">分派</a>';
	                    var span_opt = '<span> | </span>';
	                    if (rows.testUserId == currentUserId || rows.createBy == currentUserId || rows.taskAssignUserId == currentUserId || rows.tUserIds.indexOf(Number(currentUserId))!=-1) {
	                        var opt_status = [];
	                        if (rows.testTaskStatus == 3) {
	                            if (workEdit == true) {
	                                opt_status.push(editOpt);
	                            }
	                            return opt_status.join(span_opt);
	                        } else if (rows.testTaskStatus == 4) {
	                            if (workEdit == true) {
	                                opt_status.push(editOpt);
	                            }
	                            if (workTransfer == true) {
	                                opt_status.push(TransferOpt);
	                            }
	                            if (workHandle == true) {
	                                opt_status.push(HandleOpt);
	                            }
	                            return opt_status.join(span_opt);

	                        } else if (rows.testTaskStatus == 0) {
	                            var result = "";
	                            if (workEdit == true) {
	                                result += editOpt;
	                            }
	                            return result;
	                        } else if (rows.userName != "" && rows.userName != null && rows.userName != undefined) {
	                            if (workEdit == true) {
	                                opt_status.push(editOpt);
	                            }
	                            if (workTransfer == true) {
	                                opt_status.push(TransferOpt);
	                            }
	                            if (workHandle == true) {
	                                opt_status.push(HandleOpt);
	                            }
	                            return opt_status.join(span_opt);
	                        } else {
	                            var result = "";
	                            if (workEdit == true) {
	                                opt_status.push(editOpt);
	                            }
	                            if (workAssign == true) {
	                                opt_status.push(assigOpt);
	                            }
	                            if (workHandle == true) {
	                                opt_status.push(HandleOpt);

	                            }
	                            return opt_status.join(span_opt);
	                        }
	                    } else {
	                        return '';
	                    }

	                }
	            }
	            ],
		rowNum: 10,
		rowTotal: 200,
		rowList: [10, 30, 50, 100],
		rownumWidth: 40,
		pager: '#testpager',
		sortable: true,   // 是否可排序
		sortorder: 'desc',
		sortname: 'id',
		viewrecords: true, // 是否要显示总记录数
		onPaging:function(){
			if(Num==1){
				Num=2;
				var filterData=$("#testlist").jqGrid('getGridParam', "postData");
				if(filterData.filters!=""){
					filter=filterData;
				}
				if(filter==""){
					filter= $("#testlist").jqGrid('getGridParam', "postData");
				}
			}else{
				filter= $("#testlist").jqGrid('getGridParam', "postData");
			}
			
			$("#testlist").jqGrid("setGridParam", { postData: {
	            "filters":filter
	        }});
		},
		onSortCol:function(){
			if(filter==""){
				filter= $("#testlist").jqGrid('getGridParam', "postData");
			}
			$("#testlist").jqGrid("setGridParam", { postData: {
	            "filters":filter
	        }});
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
        	
        	if(searchNum==13){
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
		gridComplete: function () {
			$("[data-toggle='tooltip']").tooltip();
			getSearchValue();
			
		},
		loadComplete: function (data) {
			$("#loading").css('display', 'none');
			if(data.status == "2"){
				layer.alert(data.message,{icon:0});
			}
				addTableTitle();
				if(isBegain){
					getfilterParam(favoriteContent);
					isBegain = false;
				}
				getSearchValue();
			// $('#cb_list2').hide();
		},
		loadError: function (xhr,status) {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 2 });
		}
	}).trigger("reloadGrid");
	$("#testlist").jqGrid('filterToolbar', { searchOperators: true });
}

//清空搜索内容
function clearSearch() {
    $('#man_devUserId').val("");
    $('#taskCode').val("");
    $('#taskName').val("");
    $("#developer").val("");
    $("#taskState").selectpicker('val', '');
    $("#parallelTask").val("");
    $("#relationDemand").val("");
    $("#involveSystem").val("");
    $("#taskAssignUserId").val("");
    $("#taskAssignUser").val("");
    $("#windowId").val("");
    $("#windowName").val("");
    $(".color1 .btn_clear").css("display","none");
}

//清空表格内容
function clearContent(that) {
    $(that).parent().children('input').val("").change();
    $(that).parent().children(".btn_clear").css("display", "none");
}
//展开收起表格第一行
function showSearchInput(that) {
    if ($(that).text() == '收起筛选') {
        $(that).text("展开筛选");
        $("#cleanrChoose").hide();
        $(".ui-search-toolbar").hide();
    } else {
        $(that).text("收起筛选");
        $("#cleanrChoose").show();
        $(".ui-search-toolbar").show();
    }
}

//自定义筛选器 弹框
function configFilterShow(){
    var inputs = $("[id*=gs_]");
    $.each(inputs,function(index,dom){
        var field = $(dom).attr("id").split("_")[1];
        $("#sh_"+field).val($(dom).val());
    });
    $("#configFilterModal").modal('show');
}
//自定义筛选器 保存提交
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
    if(type == "2"){//修改
        if(filterValue == ""){
            layer.alert("未选择筛选器！", { icon: 0 });
            $("#loading").css('display', 'none');
            return;
        }
        if(filterArr.length==0){
        	layer.alert("请填写一个条件！", { icon: 0 });
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
    }else if(type == "1"){//新增
        var filterName = $("#filterName").val();
        var flag = jugeFilterName(filterName);
        if(!flag){
            $("#loading").css('display', 'none');
            return;
        }
        if(filterArr.length==0){
        	layer.alert("请填写一个条件！", { icon: 0 });
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
                "menuUrl": "/testManageui/workTask/toWorktask"
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
    }else{//删除
    	if(filterValue == ""){
            layer.alert("未选择筛选器！", { icon: 0 });
            $("#loading").css('display', 'none');
            return;
        }
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

//判断过虑器名称
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
//搜索
function searchInfo(param) {
    
    //重新载入
    var gridParam = {
			url: "/testManage/worktask/getAllWorktask",
			loadComplete: function () {
				$("#loading").css('display', 'none');
			}
		};
    if(param != undefined){
		gridParam.postData = {filters:param};
	}
    $("#loading").css('display', 'block');
    $("#testlist").jqGrid('setGridParam', gridParam).trigger("reloadGrid"); // 重新载入
    /*$("#testlist").jqGrid('setGridParam', {
        url: '/testManage/worktask/getAllWorktask',
        postData: {
            "workTask": obj,
            filters: ""
        },
        page: 1
    }).trigger("reloadGrid");*/
}
function tableClearSreach() {
    $(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
    $(".ui-search-toolbar .ui-search-input select").val('0');
    $(".ui-search-toolbar .ui-search-input  .btn_clear").css("display", "none");
    $("#testlist").jqGrid('setGridParam', {
        postData: {
        	 "workTask": "",
            filters: ""
        },
        page: 1,
        loadComplete: function () {
            $("#loading").css('display', 'none');
        }
    }).trigger("reloadGrid"); //重新载入
}

//跳转到测试集列表
//function showTestSet(row) {
//	var curl = testSetMenuUrl.split("?")[0] + "?workTaskId=" + htmlEncodeJQ(row.id);
//	var sessionStorage = window.sessionStorage;
//	sessionStorage.setItem("testSetFlag", "false");
//	window.parent.toPageAndValue(testSetMenuName, testSetMenuId, curl);
//}

//跳转到缺陷列表页面
function showDefect(row) {
    var curl = defectMenuUrl.split("?")[0] + "?workTaskId=" + htmlEncodeJQ(row.id) + "&workTaskCode=" + htmlEncodeJQ(row.testTaskCode);
    window.parent.toPageAndValue("测试缺陷管理", defectMenuId, curl);
    //	window.location.href="toDefect?workTaskId="+htmlEncodeJQ(row.id);
}

//添加
function newPerson_btn() {
    $('.is_new_project').show();
    oac_FormValidator();
    $("#planStartDate").val("");
    $("#planEndDate").val("");

    _files = [];
    $("#loading").css('display', 'block');
    $("#userPopupType").val("insertWorkTask");
    $("#taskUser").empty();
    $("#newFileTable").empty();
    $("#files").val("");
    $("#ataskName").val("");
    $("#featureCode").val("");
    $("#taskOverview").val("");
    $("#startWork").val("");
    $("#startWork").val(datFmt(new Date(), "yyyy-MM-dd"));
    $("#endWork").val("");
    $("#workLoad").val("");
    $("#new_taskUserId").val("");
    $("#new_taskUser").val("");

    $("#testStage").selectpicker('val', '');
    $("#planSitStartDate").val("");
    $("#planSitEndDate").val("");
    $("#estimateSitWorkload").val("");
    $("#planPptStartDate").val("");
    $("#planPptEndDate").val("");
    $("#estimatePptWorkload").val("");
    $("#add_taskAssignUserId").val("");
    $("#add_taskAssignUser").val("");

    getUserName();
    $("#loading").css('display', 'none');
    modalType = 'new';

    $("#testnewPerson").modal("show");

}

//版测系测时间
function editplanDate() {
    if ($("#edtestStage").val() == 1) {
        var edplanSitStart = $("#edplanSitStartDate").val();
        if (edplanSitStart == undefined) {
            $("#edstartWork").val('');
        } else {
            $("#edstartWork").val(edplanSitStart);
        }
        var edplanSitStart = $("#edplanSitEndDate").val();
        if (edplanSitStart == undefined) {
            $("#edendWork").val('');
        } else {
            $("#edendWork").val(edplanSitStart);
        }
        var edSitWorkload = $("#edestimateSitWorkload").val();
        if (edSitWorkload == undefined) {
            $("#edworkLoad").val("").change();
        } else {
            $("#edworkLoad").val(edSitWorkload).change();
        }

    } else if ($("#edtestStage").val() == 2) {
        var edplanPptStart = $("#edplanPptStartDate").val();
        if (edplanPptStart == undefined) {
            $("#edstartWork").val('');
        } else {
            $("#edstartWork").val(edplanPptStart);
        }
        var edplanPptEnd = $("#edplanPptEndDate").val();
        if (edplanPptEnd == undefined) {
            $("#edendWork").val('');
        } else {
            $("#edendWork").val(edplanPptEnd);
        }
        var edPptWorkload = $("#edestimatePptWorkload").val();
        if (edPptWorkload == undefined) {
            $("#edworkLoad").val("").change();
        } else {
            $("#edworkLoad").val(edPptWorkload).change();
        }
    }
}
//版测系测时间
function addplanDate() {
    if ($("#testStage").val() == 1) {
        var planSitStart = $("#planSitStartDate").val();
        if (planSitStart == undefined) {
            $("#startWork").val('');
        } else {
            $("#startWork").val(planSitStart);
        }
        var planSitEnd = $("#planSitEndDate").val();
        if (planSitEnd == undefined) {
            $("#endWork").val('');
        } else {
            $("#endWork").val(planSitEnd);
        }
        var SitWorkload = $("#estimateSitWorkload").val();
        if (SitWorkload == undefined) {
            $("#workLoad").val("").change();
        } else {
            $("#workLoad").val(SitWorkload).change();
        }

    } else if ($("#testStage").val() == 2) {
        var planPptStart = $("#planPptStartDate").val();
        if (planPptStart == undefined) {
            $("#startWork").val('');
        } else {
            $("#startWork").val(planPptStart);
        }
        var planPptEnd = $("#planPptEndDate").val();
        if (planPptEnd == undefined) {
            $("#endWork").val('');
        } else {
            $("#endWork").val(planPptEnd);
        }
        var PptWorkload = $("#estimatePptWorkload").val();
        if (PptWorkload == undefined) {
            $("#workLoad").val("").change();
        } else {
            $("#workLoad").val(PptWorkload).change();
        }
    }
}
//添加
function addTestTask() {
    var files = $("#files").val();
    var obj = {};
    obj.requirementFeatureId = $("#Attribute").attr("featureCode");  //关联开发任务
    obj.testTaskName = $("#ataskName").val();//任务名称
    obj.testTaskOverview = $("#taskOverview").val();//任务描述
    //	obj.planStartDate=$("#startWork").val();//预计开始时间
    //	obj.planEndDate=$("#endWork").val();//预计结束时间
    //	obj.planWorkload=$("#workLoad").val();//预计工作量
    obj.testUserId = $("#new_taskUserId").val();//测试人员
    obj.testStage = $("#testStage").val();//测试阶段
    obj.commissioningWindowId = $("#Attribute").attr("commissioningWindowId");
    obj.featureStatus = $("#Attribute").attr("requirementFeatureStatus");
    obj.taskAssignUserId = $("#add_taskAssignUserId").val();//任务分配
    obj.environmentType = $("#add_environmentType").val();

    obj.planStartDate = $("#planStartDate").val();
    obj.planEndDate = $("#planEndDate").val();

    var objStr = JSON.stringify(obj);
    $('#addTest').data('bootstrapValidator').validate();
    if (!$('#addTest').data('bootstrapValidator').isValid()) {
        return;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        type: "post",
        url: '/testManage/worktask/addTestTask',

        data: {
            objStr,
            "attachFiles": files
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            $("#testnewPerson").modal("hide");
            if (data.status == 1) {
                layer.alert('添加成功！', {
                    icon: 1,
                    title: "提示信息"
                });
                $("#userPopupType").val("");
                flagNum=2;
                $(".ui-search-toolbar .ui-search-input input[type^=text]").val('');
                $(".ui-search-toolbar .ui-search-input select").val('0');
                $(".ui-search-toolbar .ui-search-input  .btn_clear").css("display", "none");
               // $("#gs_testTaskCode").val(data.data.testTaskCode);
                var field=[{"field":"testTaskCode","value":data.data.testTaskCode}];
                
                var filterParam =getfilterParam(JSON.stringify(field));
                $("#testlist").jqGrid("setGridParam", { postData: {
                    "filters":JSON.stringify(filterParam)
                }}).trigger("reloadGrid");
            } else {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });

            }
            
           // $('#gs_testTaskCode').trigger("keyup");

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });


}
//文件上传，并列表展示
function uploadFileList() {
    //列表展示
    $(".upload-files").change(function () {
        var files = this.files;
        var formFile = new FormData();
        /*if(!fileAcceptBrowser()){
            for(var i=0,len=files.length;i<len;i++){
                var file_type = files[i].name.split(".")[1];
                if(file_type!="doc"&&file_type!="docx"&&file_type!="xls"&&file_type!="xlsx"&&file_type!="txt"&&file_type!="pdf"){
                    layer.alert('上传文件格式错误! 请上传后缀名为.doc，.docx，.xls，.xlsx，.txt，.pdf的文件', {icon:0});
                    return false;
                }
            }
        }*/
        var filesSize = 0;
        for (var i = 0, len2 = files.length; i < len2; i++) {
            filesSize += files[i].size;
            //  files.append("files",formFileList[i]);
        }
        if (filesSize > 1048576000) {
            layer.alert('文件太大,请删选！！！', {
                icon: 2,
                title: "提示信息"
            });
            return false;
        }
        outer: for (var i = 0, len = files.length; i < len; i++) {
            var file = files[i];
            if (file.size <= 0) {
                layer.alert(file.name + "文件为空！", { icon: 0 });
                continue;
            }
            if (file.size > 10485760) {
                layer.alert(file.name + '：超过10MB！', {
                    icon: 2,
                    title: "提示信息"
                });
                return false;
            }
            var fileList = [];
            var oldFileList = [];
            if (modalType == 'new') {
                fileList = _files;
            } else if (modalType == 'edit') {
                oldFileList = _editfiles;
                fileList = Neweditfiles;
            } else if (modalType == 'check') {
                fileList = _checkfiles;
            } else if (modalType == 'dHandle') {
                oldFileList = _Dhandlefiles;
                fileList = _newDhandlefiles;
            } else if (modalType == 'Handle') {
                oldFileList = _handlefiles;
                fileList = _Newhandlefiles;
            } else if (modalType == "examine") {
                oldFileList = examineAttaches;
                fileList = newexamineAttaches;
            }
            for (var j = 0; j < fileList.length; j++) {
                if (fileList[j].fileNameOld == file.name) {
                    layer.alert(file.name + " 文件已存在", {
                        icon: 2,
                        title: "提示信息"
                    });
                    continue outer;
                }
            }
            for (var j = 0; j < oldFileList.length; j++) {
                if (oldFileList[j].fileNameOld == file.name) {
                    layer.alert(file.name + " 文件已存在", {
                        icon: 2,
                        title: "提示信息"
                    });
                    continue outer;
                }
            }
            formFile.append("files", file);
            //读取文件
            if (window.FileReader) {
                (function (i) {
                    var file = files[i];
                    var reader = new FileReader();
                    reader.readAsDataURL(file);
                    reader.onerror = function (e) {
                        layer.alert("文件" + file.name + " 读取出现错误", { icon: 0 });
                        return false;
                    };
                    reader.onload = function (e) {
                        if (e.target.result) {
                            console.log("文件 " + file.name + " 读取成功！");
                        }
                    };
                })(i);
            }
            //列表展示
            var _tr = '';
            var file_name = file.name.split("\.")[0];
            var file_type = file.name.split("\.")[1];
            var _td_icon;
            var _td_name = '<span >' + file.name + '</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
            switch (file_type) {
                case "doc":
                case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
                case "xls":
                case "xlsx": _td_icon = '<img src="' + _icon_excel + '" />'; break;
                case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
                case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
                default: _td_icon = '<img src="' + _icon_word + '" />'; break;
            }
            var _table = $(this).parent(".file-upload-select").next(".file-upload-list").children("table");
            _tr += '<tr><td><div class="fileTb">' + _td_icon + '  ' + _td_name + _td_opt + '</tr>';
            _table.append(_tr);

        }
        //上传文件
        $("#loading",window.parent.document).css('display','block');
        $.ajax({
            type: 'post',
            url: '/zuul/testManage/worktask/uploadFile',
            contentType: false,
            processData: false,
            dataType: "json",
            data: formFile,

            success: function (data) {
                for (var k = 0, len = data.length; k < len; k++) {
                    if (modalType == 'new') {
                        _files.push(data[k]);
                    } else if (modalType == 'edit') {
                        Neweditfiles.push(data[k]);
                        //_editfiles.push(data[k]);
                    } else if (modalType == 'check') {

                        _checkfiles.push(data[k]);
                    } else if (modalType == 'dHandle') {
                        _newDhandlefiles.push(data[k]);
                    } else if (modalType == 'Handle') {
                        _Newhandlefiles.push(data[k]);
                    } else if (modalType == "examine") {
                        newexamineAttaches.push(data[k]);
                    }
                    $(".file-upload-tb span").each(function (o) {
                        if ($(this).text() == data[k].fileNameOld) {
                            //$(this).parent().children(".file-url").html(data[k].filePath);
                            $(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
                            $(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
                        }
                    });
                }
                if (modalType == 'new') {
                    $("#files").val(JSON.stringify(_files));
                    clearUploadFile('uploadFile');
                } else if (modalType == 'edit') {
                    //$("#editfiles").val(JSON.stringify(_editfiles));
                    $("#newFiles").val(JSON.stringify(Neweditfiles));
                    clearUploadFile('edituploadFile');
                } else if (modalType == 'check') {
                    $("#checkfiles").val(JSON.stringify(_checkfiles));
                    clearUploadFile('checkuploadFile');
                } else if (modalType == 'dHandle') {
                    $("#NewDHattachFiles").val(JSON.stringify(_newDhandlefiles));
                    clearUploadFile('DHituploadFile');
                } else if (modalType == 'Handle') {
                    $("#newHattachFiles").val(JSON.stringify(_Newhandlefiles));
                    clearUploadFile('HituploadFile');
                } else if (modalType == "examine") {
                    $("#newExamineFiles").val(JSON.stringify(newexamineAttaches));
                    clearUploadFile('examineUploadFile');
                }
                $("#loading",window.parent.document).css('display','none');
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("#loading",window.parent.document).css('display','none');
                layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
            }
        });
    });
}
//移除上传文件
function delFile(that, id) {
    var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
    /*var url = $(that).parent().prev().children().children(".file-url").text();
    var fileS3Bucket = $(that).parent().prev().children().children(".file-bucket").text(); */

    $(that).parent().parent().remove();
    removeFile(fileS3Key);
}

function deleteAttachements(that, attache) {
    $(that).parent().parent().remove();
    var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
    removeFile(fileS3Key);
    deleteAttaches.push(attache);
    /*$.ajax({
            type:'post',
            url:'/testManage/worktask/delFile',
            data:{
                    id:attache.id
            },
            success:function(data){
                    if(data.status=="success"){
                            layer.alert('删除成功！', {
                                    icon: 1,
                                    title: "提示信息"
                            });
                    }else if(data.status == "fail"){
                            layer.alert('删除失败！', {
                                    icon: 2,
                                    title: "提示信息"
                            });
                    }
            }
    });*/
}

//移除暂存数组中的指定文件
function removeFile(fileS3Key) {
    if (modalType == "new") {
        var _file = $("#files").val();
        if (_file != "") {
            var files = JSON.parse(_file);
            for (var i = 0, len = files.length; i < len; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            _files = files;
            $("#files").val(JSON.stringify(files));
        }

    } else if (modalType == 'edit') {
        var _file = $("#editfiles").val();
        var _newfile = $("#newFiles").val();
        if (_file != "") {
            var files = JSON.parse(_file);
            for (var i = 0; i < files.length; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            _editfiles = files;
            $("#editfiles").val(JSON.stringify(files));
        }
        if (_newfile != "") {
            var files = JSON.parse(_newfile);
            for (var i = 0, len = files.length; i < len; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            Neweditfiles = files;
            $("#newFiles").val(JSON.stringify(files));
        }
    } else if (modalType == 'check') {
        var _file = $("#checkfiles").val();
        if (_file != "") {
            var files = JSON.parse(_file);
            for (var i = 0, len = files.length; i < len; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            _checkfiles = files;
            $("#checkfiles").val(JSON.stringify(files));
        }
    } else if (modalType == 'dHandle') {
        var _file = $("#DHattachFiles").val();
        var _newfile = $("#NewDHattachFiles").val();
        if (_file != "") {
            var files = JSON.parse(_file);
            for (var i = 0, len = files.length; i < len; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            _Dhandlefiles = files;
            $("#DHattachFiles").val(JSON.stringify(files));
        }
        if (_newfile != "") {
            var files = JSON.parse(_newfile);
            for (var i = 0, len = files.length; i < len; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            _newDhandlefiles = files;
            $("#NewDHattachFiles").val(JSON.stringify(files));
        }
    } else if (modalType == 'Handle') {
        var _file = $("#HattachFiles").val();
        var _newfile = $("#newHattachFiles").val();
        if (_file != "") {
            var files = JSON.parse(_file);
            for (var i = 0, len = files.length; i < len; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            _handlefiles = files;
            $("#HattachFiles").val(JSON.stringify(files));
        }
        if (_newfile != "") {
            var files = JSON.parse(_newfile);
            for (var i = 0, len = files.length; i < len; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            _Newhandlefiles = files;
            $("#newHattachFiles").val(JSON.stringify(files));
        }
    } else if (modalType == 'examine') {
        var _file = $("#OverSeeFiles").val();
        var _newfile = $("#newExamineFiles").val();
        if (_file != "") {
            var files = JSON.parse(_file);
            for (var i = 0; i < files.length; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            examineAttaches = files;
            $("#OverSeeFiles").val(JSON.stringify(files));
        }
        if (_newfile != "") {
            var files = JSON.parse(_newfile);
            for (var i = 0, len = files.length; i < len; i++) {
                if (files[i].fileS3Key == fileS3Key) {
                    Array.prototype.splice.call(files, i, 1);
                    break;
                }
            }
            newexamineAttaches = files;
            $("#newExamineFiles").val(JSON.stringify(files));
        }
    }
}

//查看
function selectdetail(data) {
    $("#loading").css('display', 'block');
    $( "#titleOfwork" ).children("span").eq(0).click();
    _checkfiles = [];
    var map = data.dev;
    $("#check_planStartDate").text('');
    $("#check_planEndDate").text('');
    if(map.planStartDate){
        $("#check_planStartDate").text(datFmt(new Date(map.planStartDate),"yyyy-MM-dd"));
    }
    if(map.planEndDate){
        $("#check_planEndDate").text(datFmt(new Date(map.planEndDate),"yyyy-MM-dd"));
    }
    if(map.projectType == 2){   //新建类项目
        $('.is_new_project').hide();
    }else{
        $('.is_new_project').show();
    }

    $("#checkfiles").val("");
    $("#checkAttTable").empty();
    $("#taskRemark").empty();
    $("#handleLogs").empty();
    $("#SeeFileTable").empty();
    $("#tyaskRemark").val("");
    $("#SdevCode").html("");
    $("#SdevName").html("");
    $("#SdevOverview").html("");
    $("#SStatus").html("");
    $("#devuserID").html("");
    //	$("#planStartDate").html("");
    //	$("#planEndDate").html("");
    $("#featureName").html("");
    //	$("#SplanWorkload").html("");
    $("#featureOverview").html("");
    $("#requirementFeatureStatus").html("");
    $("#manageUserId").html("");
    $("#executeUserId").html("");
    $("#systemId").html("");
    $("#requirementSource").html("");
    $("#requirementType").html("");
    $("#requirementPriority").html("");
    $("#requirementPanl").html("");
    $("#requirementStatus").html("");
    $("#requirementName").html("");
    $("#applyUserId").html("");
    $("#applyDeptId").html("");
    $("#expectOnlineDate").html("");
    $("#planOnlineDate").html("");
    $("#lastUpdateDate3").html("");
    $("#createDate3").html("");
    $("#openDate").html("");
    $("#requirementOverview").val("");
    $("#testStage2").text("");
    $("#checkEnvironmentType").html('');
    $("#requirementOverview").text(map.requirementOverview);
    $("#SdevCode").html(map.testTaskCode);
    $("#SdevName").html(map.testTaskName);
    $("#SdevOverview").html(map.testTaskOverview);
    var devTaskStatus = show_status( map.testDevTaskStatus )
    $("#implementation_status").empty();
    $("#implementation_status").append( devTaskStatus );
    $("#SStatus").html( map.testDevTaskStatus);
    $("#devuserID").html(map.devuserName);
    $("#testTaskID").html(map.id);
    $("#createBy").html(map.createName);
    if (map.createDate != null) {
        $("#testStage2").text(map.testStage);
    }

    if (map.createDate != null) {
        $("#createDate").html(datFmt(new Date(map.createDate), "yyyy年MM月dd"));
    }

    //		if(map.planStartDate!=null){
    //			var planstartDate=datFmt(new Date(map.planStartDate),"yyyy年MM月dd");
    //			$("#planStartDate").html(planstartDate);
    //		}
    //		if(map.planEndDate!=null){
    //			var planEndDate=datFmt(new Date(map.planEndDate),"yyyy年MM月dd");
    //			$("#planEndDate").html(planEndDate);
    //		}
    //		$("#SplanWorkload").html(map.planWorkload);
    var users = map.executeUser.join(',');
    $("#cases").html("");
    $("#caseExecutes").html("");
    $("#executeUser").html("");
    $("#defectNum").html("");
    $("#checkTaskAssignUser").html("");
    $("#cases").html(map.designCaseNumber);
    $("#caseExecutes").html(map.executeCaseNumber);
    $("#executeUser").html(users);
    $("#defectNum").html(map.defectNum);
    $("#checkTaskAssignUser").html(map.taskAssignUserName);

    $("#checkChange").empty();
    $("#checkRequirementFeatureSource").empty();
    $("#checkImportantRequirementType").empty();
    $("#checkPptDeployTime").empty();
    $("#checkSubmitTestTime").empty();
    $("#checkChange").text(map.requirementChangeNumber == null ? '' : map.requirementChangeNumber);
    $("#checkRequirementFeatureSource").text(map.requirementFeatureSource == null ? '' : map.requirementFeatureSource);
    var importantRequirement = "";
    if(map.importantRequirementType=='1'){
        importantRequirement="是"
    }else if(map.importantRequirementType=='2'){
        importantRequirement="否"
    }
    $("#checkImportantRequirementType").text(importantRequirement);
    $("#checkPptDeployTime").text(map.pptDeployTime == null ? '' : timestampToTime(map.pptDeployTime));
    $("#checkSubmitTestTime").text(map.submitTestTime == null ? '' : timestampToTime(map.submitTestTime));



    $("#actualStartDate").html("");
    if (map.actualStartDate != null) {
        var actualStartDate = datFmt(new Date(map.actualStartDate), "yyyy年MM月dd");
        $("#actualStartDate").html(actualStartDate);
    }
    $("#actualEndDate").html("");
    if (map.actualEndDate != null) {
        var actualEndDate = datFmt(new Date(map.actualEndDate), "yyyy年MM月dd");
        $("#actualEndDate").html(actualEndDate);
    }
    $("#actualWorkload").html("");
    $("#actualWorkload").html(map.actualWorkload);

    $("#featureName").attr( 'val', map.featureId );
    $("#featureName").text(map.featureCode + " | " + map.featureName);
    $("#testWorkModal .modal-title").text(map.featureCode + "&nbsp|&nbsp" + map.featureName);

    $("#requirementCode").text( toStr(map.requirementCode) )
    $("#reqModal .modal-title").text(map.requirementCode);

    $("#featureOverview").html(map.featureOverview);
    $("#requirementFeatureStatus").html(map.requirementFeatureStatusName);

    $("#manageUserId").html(map.manageUserName);
    $("#executeUserId").html(map.executeUserName);
    $("#systemId").html(map.systemName);
    $("#requirementName").html(map.requirementName);
    $("#requirementSource").html(map.requirementSource);
    $("#requirementType").html(map.requirementType);
    $("#requirementPriority").html(map.requirementPriority);
    $("#requirementPanl").html(map.requirementPanl);
    $("#requirementStatus").html(map.requirementStatus);
    $("#applyUserId").html(map.applyUserName);
    $("#applyDeptId").html(map.applyDeptName);

    for(var i=0;i< environmentTypeList.length;i++){
        if(environmentTypeList[i][0] == map.environmentType){
            $("#checkEnvironmentType").html(environmentTypeList[i][1]);
        }
    }


    if (map.expectOnlineDate != null) {
        var expectOnlineDate = datFmt(new Date(map.expectOnlineDate), "yyyy年MM月dd");
        $("#expectOnlineDate").html(expectOnlineDate);
    }
    if (map.planOnlineDate != null) {
        var planOnlineDate = datFmt(new Date(map.planOnlineDate), "yyyy年MM月dd");
        $("#planOnlineDate").html(planOnlineDate);
    }
    if (map.openDate != null) {
        var openDate = datFmt(new Date(map.openDate), "yyyy年MM月dd");
        $("#openDate").html(openDate);
    }
    if (map.createDate3 != null) {
        var createDate3 = datFmt(new Date(map.createDate3), "yyyy年MM月dd");
        $("#createDate3").html(createDate3);
    }

    if (map.lastUpdateDate3 != null) {
        var lastUpdateDate3 = datFmt(new Date(map.lastUpdateDate3), "yyyy年MM月dd");
        $("#lastUpdateDate3").html(lastUpdateDate3);
    }
    if (data.attachements != undefined) {
        var _table = $("#SeeFileTable");
        var attMap = data.attachements;
        //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
        for (var i = 0; i < attMap.length; i++) {
            var _tr = '';
            var file_name = attMap[i].fileNameOld;
            var file_type = attMap[i].fileType;
            var file_id = attMap[i].id;
            var _td_icon;
            //<i class="file-url">'+data.attachements[i].filePath+'</i>
            var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + attMap[i].fileS3Bucket + '</i><i class = "file-s3Key">' + attMap[i].fileS3Key + '</i></td>';
            switch (file_type) {
                case "doc":
                case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
                case "xls":
                case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
                case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
                case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
                default: _td_icon = '<img src="' + _icon_word + '" />'; break;
            }
            var row = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
            _tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + _td_icon + " " + _td_name + '</tr>';
            _table.append(_tr);
            _SeeFiles.push(attMap[i]);
            $("#SeeFiles").val(JSON.stringify(_SeeFiles));
        }
    }

    //备注
    if (data.rmark != undefined) {
        var str = '';
        for (var i = 0; i < data.rmark.length; i++) {
            var style = '';
            if (i == data.rmark.length - 1) {
                style = ' lastLog';
            }
            str += '<div class="logDiv' + style + '"><div class="logDiv_title"><span class="orderNum"></span>' +
                '<span>' + data.rmark[i].userName + '  | ' + data.rmark[i].userAccount + '</span>&nbsp;&nbsp;&nbsp;<span>' + datFmt(new Date(data.rmark[i].createDate), "yyyy-MM-dd hh:mm:ss") + '</span></div>' +
                '<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><span>' + data.rmark[i].testTaskRemark + '</span>' +
                '<div class="file-upload-list">';
            if (data.Attchement.length > 0) {
                str += '<table class="file-upload-tb">';
                var _trAll = '';
                for (var j = 0; j < data.Attchement.length; j++) {
                    var _tr = '';
                    if ((data.Attchement[j].testTaskRemarkId) == (data.rmark[i].id)) {

                        var file_type = data.Attchement[j].fileType;
                        var file_name = data.Attchement[j].fileNameOld;
                        var _td_icon;
                        var _td_name = '<span>' + file_name + '</span>';
                        switch (file_type) {
                            case "doc":
                            case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
                            case "xls":
                            case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
                            case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
                            case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
                            default: _td_icon = '<img src="' + _icon_word + '" />'; break;
                        }
                        var row = JSON.stringify(data.Attchement[j]).replace(/"/g, '&quot;');
                        _tr += '<tr><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + _td_icon + _td_name + "</tr>";

                    }
                    if (_tr != undefined) {
                        _trAll += _tr;
                    }

                }
                str += _trAll + '</table>';
            }

            str += '</div></div></div></div></div>';

        }
        $("#taskRemark").append(str);

    }

    //处理日志
    if (data.logs != undefined) {
        var _span = "";
        var str = '';
        for (var i = 0; i < data.logs.length; i++) {
            var style = '';
            if (i == data.logs.length - 1) {
                style = ' lastLog';
            }
            var style2 = '';
            var addDiv = '';
            var logDetail = '';
            if (data.logs[i].logDetail == null || data.logs[i].logDetail == '') {
                if (data.logAttachement.length > 0) {
                    for (var j = 0; j < data.logAttachement.length; j++) {
                        if (data.logs[i].id != data.logAttachement[j].devTaskLogId) {
                            style2 = 'style="display:none"';
                            addDiv = '</br>';
                        }
                    }
                } else {
                    style2 = 'style="display:none"';
                    addDiv = '</br>';
                }



            } else {
                logDetail = data.logs[i].logDetail;
                logDetail = logDetail.replace(/;/g, "<br/>");
                logDetail = logDetail.replace(/null/g,' ');
            }


            str += '<div class="logDiv' + style + '"><div class="logDiv_title"><span class="orderNum"></span><span>' + data.logs[i].logType + '</span>&nbsp;&nbsp;&nbsp;' +
                '<span>' + data.logs[i].userName + '  | ' + data.logs[i].userAccount + '</span>&nbsp;&nbsp;&nbsp;<span>' + datFmt(new Date(data.logs[i].createDate), "yyyy-MM-dd hh:mm:ss") + '</span></div>' +
                '<div class="logDiv_cont" ><div class="logDiv_contBorder"><div class="logDiv_contRemark" ' + style2 + '>'

            var ifjson = isJSON(logDetail);
            if (ifjson) {
                _span = "";
                if (logDetail == "[]" || logDetail == "" || logDetail == undefined) {
                    _span = '<span>未做任何操作</span>'
                } else {
                    var Detail = JSON.parse(logDetail);
                    for (var s = 0; s < Detail.length; s++) {
                        //alert(Detail[s].newValue);
                        var value = Detail[s].oldValue
                        if (value == "" || value == undefined || value == null) {
                            value = ""
                        }
                        if (Detail[s].remark != "" && Detail[s].remark != undefined) {
                            str += '<span>备注信息：' + Detail[s].remark + '</span></br>'
                        }
                        if ("" != Detail[s].newValue && undefined != Detail[s].newValue) {
                            if (value == null || value == "") {
                                value = "&nbsp;&nbsp"
                            }
                            str += '<span>' + Detail[s].fieldName + "：" + "<span style='font-weight:bold'>" + '"' + value + '"' + "</span>" + modified + "<span style='font-weight:bold'>" + '"' + Detail[s].newValue + '"' + "</span>" + '</span></br>'
                        }
                    }
                }

            } else {
                str += '<span>' + logDetail + '</span>'
            }

            str += '<div class="file-upload-list">';
            if (data.logAttachement.length > 0) {
                str += '<table class="file-upload-tb">';
                var _trAll = '';
                for (var j = 0; j < data.logAttachement.length; j++) {
                    if ((data.logAttachement[j].testTaskLogId) == (data.logs[i].id)) {
                        var attType = '';
                        _span = "";
                        if (data.logAttachement[j].status == 1) {//新增的日志
                            attType = "<lable>新增附件：</lable>";
                        } else if (data.logAttachement[j].status == 2) {//删除的日志
                            attType = "<lable>删除附件：</lable>";
                        }
                        var _tr = '';
                        var file_name = data.logAttachement[j].fileNameOld;
                        var file_type = data.logAttachement[j].fileType;
                        var _td_icon;
                        var _td_name = '<span>' + file_name + '</span></div></td>';
                        //var _td_name = '<span>'+file_name+'</span><i class="file-url">'+data.logAttachement[j].filePath+'</i><i class = "file-bucket">'+data.logAttachement[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.logAttachement[j].fileS3Key+'</i></td>';

                        switch (file_type) {
                            case "doc":
                            case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
                            case "xls":
                            case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
                            case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
                            case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
                            default: _td_icon = '<img src="' + _icon_word + '" />'; break;
                        }
                        var row = JSON.stringify(data.logAttachement[j]).replace(/"/g, '&quot;');
                        _tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + attType + _td_icon + _td_name + '</tr>';
                        _trAll += _tr;
                    }
                }

            }
            if (_trAll == undefined) { _trAll = "" }
            _trAll += _span;
            //_span="";
            str += _trAll + '</table>';
            _trAll = "";
            str += '</div></div>' + addDiv + '</div></div></div>';

        }
        $("#handleLogs").append(str);
    }
    modalType = 'check';
    $("#loading").css('display', 'none');
    $("#selectdetail").modal("show");
}
function show_status( value ){
    var statusVal='';
    for( var i = 0;i<taskStateList.length;i++ ){
        if( taskStateList[i].text == value ){
            statusVal = taskStateList[i].value
        }
    }
    switch (statusVal) {
        case '1':return "<span class='doing1'>" + value + "</span>";break;
        case '2':return "<span class='doing2'>" + value + "</span>";break;
        case '3':return "<span class='doing3'>" + value + "</span>";break;
        case '0':return "<span class='doing4'>" + value + "</span>";break;
        case '4':return "<span class='doing5'>" + value + "</span>";break;
        case '5':return "<span class='doing6'>" + value + "</span>";break;
        case '6':return "<span class='doing7'>" + value + "</span>";break;
        case '7':return "<span class='doing8'>" + value + "</span>";break;
        case '8':return "<span class='doing9'>" + value + "</span>";break;
        case '9':return "<span class='doing10'>" + value + "</span>";break;
        case '10':return "<span class='doing11'>" + value + "</span>";break;
        case '11':return "<span class='doing12'>" + value + "</span>";break;
        default:return "<span class='doing'>" + value + "</span>";break;
    }
}
function timestampToTime(timestamp) {
    if (timestamp == null) {
        return;
    }
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
    return Y + M + D;
}

//分派
function showAssig(data) {
    $("#assignUser").attr("assignUserID", "");
    $("#assignUser").val("");
    $("#userPopupType").val("Assign");
    $("#testAssignRemark").val("");
    var map = data.dev;
    for (var key in map) {
        $("#AtestStage").html("");
        if (map[key].testStage == 1) {
            $("#AtestStage").html("系测");
        } else if (map[key].testStage == 2) {
            $("#AtestStage").html("版测");
        }
        $("#assigDevID").val("");
        $("#assigDevID").val(map[key].id);
        $("#devName").html("");
        $("#devName").html(map[key].testTaskName);
        $("#devOverview").html("");
        $("#devOverview").html(map[key].testTaskOverview);
        $("#planStart").html("");
        if (map[key].planStartDate != null) {
            $("#planStart").html(datFmt(new Date(map[key].planStartDate), "yyyy年MM月dd"));
        }
        $("#planEnd").html("");
        if (map[key].planEndDate != null) {
            $("#planEnd").html(datFmt(new Date(map[key].planEndDate), "yyyy年MM月dd"));
        }
        $("#planWorkload").html("");
        $("#planWorkload").html(map[key].planWorkload);

        $("#assignActualStart").html("");
        if (map[key].actualStartDate != null) {
            $("#assignActualStart").html(datFmt(new Date(map[key].actualStartDate), "yyyy年MM月dd"));
        }
        $("#assignActualEnd").html("");
        if (map[key].actualEndDate != null) {
            $("#assignActualEnd").html(datFmt(new Date(map[key].actualEndDate), "yyyy年MM月dd"));
        }
        $("#assignActualWorkload").html("");
        $("#assignActualWorkload").html(map[key].actualWorkload);
        $("#old_assign_testTaskId").html("");
        $("#old_assign_createBy").html("");
        $("#old_assign_testTaskId").val(map[key].testUserId);
        $("#old_assign_createBy").val(map[key].createBy);
        $("#devUserName").html(map[key].userName);
        var status = map[key].testTaskStatus;
        $("#remove").remove();
        var assigStatus = '';
        var classDoing = 'doing';
        for (var i = 0, len = taskStateList.length; i < len; i++) {
            if (status == taskStateList[i].value) {
                assigStatus = '<span id="remove" class="' + classDoing + status + '">' + taskStateList[i].innerHTML + '</span>'
                break;
            }
        }
        $("#status").append(assigStatus);
        //getAssignUser(map[key].testUserId);

    }
    $('.selectpicker').selectpicker('refresh');
    $("#Assignment").modal("show");
}
//转派
function showTransfer(data) {
    $("#userPopupType").val("Transfer");
    $("#loading").css('display', 'block');
    $("#TransferUser_Id").val('');
    $("#TransferUser").val('');
//	$("#TransferUser").attr("userID", '');
    var map = data.dev;
    for (var key in map) {
        $("#TransferRemarks").val("");
        if (map[key].testStage == 1) {
            $("#HtestStage").html("系测");
        } else if (map[key].testStage == 2) {
            $("#HtestStage").html("版测");
        }
        if(map[key].projectType == 2){
            $('.is_new_project').hide();
        }else{
            $('.is_new_project').show();
        }
        $("#TransferDevID").val("");
        $("#TransferDevID").val(map[key].id);
        $("#TransferdevName").html("");
        $("#TransferdevName").html(map[key].testTaskName);
        $("#TransferOverview").html("");
        $("#TransferOverview").html(map[key].testTaskOverview);
        $("#TransplanStart").html("");
        if(map[key].planStartDate!=null){
            $("#TransplanStart").html(datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd"));
        }
        $("#TransplanEnd").html("");
        if(map[key].planEndDate!=null){
            $("#TransplanEnd").html(datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd"));
        }
        //		$("#TranplanWorkload").html("");
        //		$("#TranplanWorkload").html(map[key].planWorkload);
        $("#TranActualStart").html("");
        if (map[key].actualStartDate != null) {
            var actualStartDate = datFmt(new Date(map[key].actualStartDate), "yyyy年MM月dd");
            $("#TranActualStart").text(actualStartDate);
        }
        $("#TranActualEnd").html("");
        if (map[key].actualEndDate != null) {
            var actualEndDate = datFmt(new Date(map[key].actualEndDate), "yyyy年MM月dd");
            $("#TranActualEnd").html(actualEndDate);
        }
        $("#TranActualWorkload").html("");
        $("#TranActualWorkload").html(map[key].actualWorkload);
        $("#TrandevUserName").html("");
        $("#TrandevUserName").html(map[key].userName);

        $("#old_transfer_testTaskId").val("");
        $("#old_transfer_testTaskId").val(map[key].testUserId);

        $("#old_transfer_createBy").val("");
        $("#old_transfer_createBy").val(map[key].createBy);

        $("#oldUserID").val("");
        $("#oldUserID").val(map[key].testUserId);
        var status = map[key].testTaskStatus;
        $("#Tremove").remove();

        var Tstatus = '';
        var classDoing = 'doing';
        for (var i = 0, len = taskStateList.length; i < len; i++) {
            if (status == taskStateList[i].value) {
                Tstatus = '<span id="Tremove" class="' + classDoing + status + '">' + taskStateList[i].innerHTML + '</span>'
                break;
            }
        }
        $("#Tstatus").append(Tstatus);
        getTransferUser(map[key].testUserId);

    }
    $('.selectpicker').selectpicker('refresh');
    $("#loading").css('display', 'none');
    $("#Transfer").modal("show");
}
//显示转派
function Transfer(id,taskAssignUserId) {
    assignUser = taskAssignUserId;
    $.ajax({
        method: "post",
        url: "/testManage/worktask/getEditDevTask",
        data: { id: id },
        success: function (data) {
            showTransfer(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });
}
//显示分派
function assig(id, taskAssignUserId) {
    assignUser = taskAssignUserId;
    $.ajax({
        method: "post",
        url: "/testManage/worktask/getEditDevTask",
        data: { id: id },
        success: function (data) {
            showAssig(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }
    });
}
//文件下载
function download(row) {
    var fileS3Bucket = row.fileS3Bucket;
    var fileS3Key = row.fileS3Key;
    var fileNameOld = row.fileNameOld;
    window.location.href = "/testManage/worktask/downloadFile?fileS3Bucket=" + fileS3Bucket + "&fileS3Key=" + fileS3Key + "&fileNameOld=" + fileNameOld;


}

//显示编辑信息
function showEditData(data) {
    $('.is_new_project').show();
    oac_FormValidator();
    Neweditfiles = [];
    _editfiles = [];
    deleteAttaches = [];
    $("#userPopupType").val("updatetWorkTask");
    $("#newFiles").val("");
    $("#editfiles").val("");
    $("#loading").css('display', 'block');
    $("#editFileTable").empty();
    //	$("#edstartWork").val("");
    //	$("#edendWork").val("");
    $("#old_edit_taskUserId").val("");
    $("#edit_createById").val("");
    $("#edtaskUser").empty();
    $("#edit_taskAssignUserId").val('');
    $("#edit_taskAssignUser").val('');

    $("#edplanSitStartDate").val("");
    $("#edplanSitEndDate").val("");
    $("#edestimateSitWorkload").val("");
    $("#edplanPptStartDate").val("");
    $("#edplanPptEndDate").val("");
    $("#edestimatePptWorkload").val("");
    $("#workTaskStatus").val("");
    $("#editStartDate").val("");
    $("#editEndDate").val("");
    $("#editWorkLoad").val("");
    $("#editDesignCaseNum").val("");
    $("#editExecuteCaseNum").val("");
    $("#editEnvironmentType").val('');
    var map = data.dev;
    if (data.tblRequirementFeature != null) {
        if (data.tblRequirementFeature.planSitStartDate != null) {
            var sitStart = datFmt(new Date(data.tblRequirementFeature.planSitStartDate), "yyyy-MM-dd");
            $("#edplanSitStartDate").val(sitStart);
        }
        if (data.tblRequirementFeature.planSitEndDate != null) {
            var sitEnd = datFmt(new Date(data.tblRequirementFeature.planSitEndDate), "yyyy-MM-dd");
            $("#edplanSitEndDate").val(sitEnd);
        }
        $("#edestimateSitWorkload").val(data.tblRequirementFeature.estimateSitWorkload);

        if (data.tblRequirementFeature.planPptStartDate != null) {
            var pptStart = datFmt(new Date(data.tblRequirementFeature.planPptStartDate), "yyyy-MM-dd");
            $("#edplanPptStartDate").val(pptStart);
        }
        if (data.tblRequirementFeature.planPptEndDate != null) {
            var pptEnd = datFmt(new Date(data.tblRequirementFeature.planPptEndDate), "yyyy-MM-dd");
            $("#edplanPptEndDate").val(pptEnd);
        }
        $("#edestimatePptWorkload").val(data.tblRequirementFeature.estimatePptWorkload);
    }

    for (var key in map) {
        $("#addTest").data('bootstrapValidator').destroy();
        $('#addTest').data('bootstrapValidator', null);
        $("#editTestF").data('bootstrapValidator').destroy();
        $('#editTestF').data('bootstrapValidator', null);
        if(map[key].projectType == 2){ //新建项目
            $('#edtestStage').val('');
            $('.is_new_project').hide();
            new_FormValidator();
        }else{
            $('.is_new_project').show();
            oac_FormValidator();
        }
        $("#edit_taskUserId").val(map[key].testUserId);
        $("#old_edit_taskUserId").val(map[key].testUserId);
        $("#edit_createById").val(map[key].createBy);
        $("#edtaskUser").val(map[key].userName);
        $("#edtaskUser").attr('username',map[key].userName);
        $("#taskID").val(map[key].id);
        $("#edfeatureCode").val(map[key].featureName);
        $("#edAttribute").attr("edfeatureCode", map[key].requirementFeatureId);
        $("#edAttribute").attr("edcommissioningWindowId", map[key].commissioningWindowId);
        $("#edAttribute").attr("edrequirementFeatureStatus", map[key].featureStatus);
        //getEdFeature(map[key].requirementFeatureId);
        $("#etaskName").val( isValueNull(map[key].testTaskName) );
        $("#etaskOverview").val( isValueNull(map[key].testTaskOverview) );
        $("#edit_planStartDate").val('');
        $("#edit_planEndDate").val('');
        if(map[key].planStartDate!=null){
            var start=datFmt(new Date(map[key].planStartDate),"yyyy-MM-dd");
            $("#edit_planStartDate").val(start);
        }
        if(map[key].planEndDate!=null){
            var end=datFmt(new Date(map[key].planEndDate),"yyyy-MM-dd");
            $("#edit_planEndDate").val(end);
        }
        $("#edtestStage").val(map[key].testStage);
        //		$("#edworkLoad").val(map[key].planWorkload);
//		edUserName(map[key].testUserId);
        $("#workTaskStatus").val(map[key].testTaskStatus);

        if(map[key].actualStartDate!=null){
            var start=datFmt(new Date(map[key].actualStartDate),"yyyy-MM-dd");
            $("#editStartDate").val(start);
        }

        if(map[key].actualEndDate!=null){
            var end=datFmt(new Date(map[key].actualEndDate),"yyyy-MM-dd");
            $("#editEndDate").val(end);
        }

        $("#editWorkLoad").val(map[key].actualWorkload);
        $("#editDesignCaseNum").val(map[key].designCaseNumber);
        $("#editExecuteCaseNum").val(map[key].executeCaseNumber);
        $("#edit_taskAssignUserId").val(map[key].taskAssignUserId);
        $("#edit_taskAssignUser").val(map[key].taskAssignUserName);
        $("#edit_taskAssignUser").attr('username',map[key].taskAssignUserName);

        $("#editEnvironmentType").empty();
        $("#editEnvironmentType").append('<option value="">请选择</option>');
        if(map[key].environmentTypes!=undefined && map[key].environmentTypes!=null){
            var arr = map[key].environmentTypes.split(",");
            for(var j=0;j<arr.length;j++){
                for (var i=0; i<environmentTypeList.length;i++) {
                    if(environmentTypeList[i][0] == arr[j]){
                        var opt = "<option value='" + environmentTypeList[i][0] + "'>" + environmentTypeList[i][1] + "</option>";
                        $("#editEnvironmentType").append(opt);
                    }

                }
            }

        }
        /*for (int i=0; i< environmentTypeList.length;i++) {
            if(map[key].environmentType == environmentTypeList[i][0]){*/

        /*}
    }*/
        $('.selectpicker').selectpicker('refresh');

        $("#editEnvironmentType").val(map[key].environmentType );
    }

    if (data.attachements != undefined) {
        var _table = $("#editFileTable");
        var attMap = data.attachements;
        //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
        for (var i = 0; i < attMap.length; i++) {
            var _tr = '';
            var file_name = attMap[i].fileNameOld;
            var file_type = attMap[i].fileType;
            var file_id = attMap[i].id;
            var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
            var _td_icon;
            //<i class="file-url">'+data.attachements[i].filePath+'</i>
            var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + attMap[i].fileS3Bucket + '</i><i class = "file-s3Key">' + attMap[i].fileS3Key + '</i></td>';
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,' + attache + ')">删除</a></td>';
            switch (file_type) {
                case "doc":
                case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
                case "xls":
                case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
                case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
                case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
                default: _td_icon = '<img src="' + _icon_word + '" />'; break;
            }
            var row = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
            _tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + _td_icon + " " + _td_name + _td_opt + '</tr>';
            _table.append(_tr);
            _editfiles.push(data.attachements[i]);
            $("#editfiles").val(JSON.stringify(_editfiles));
        }
    }
    $('.selectpicker').selectpicker('refresh');
    modalType = 'edit';
    $("#editTest").modal("show");
    $("#loading").css('display', 'none');
    //$('#edtaskUser').selectpicker('refresh');


}

//显示编辑Model
function edit(id) {
    $.ajax({
        method: "post",
        url: "/testManage/worktask/getEditDevTask",
        data: { id: id },
        success: function (data) {
            showEditData(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }


    });
}
//显示处理
function ShowHandle(data) {
    $("#case_Handle").hide();
    deleteAttaches = [];
    $("#loading").css('display', 'block');
    var map = data.dev;
    var DHstatus = '';
    for (var key in map) {
        var status = map[key].testTaskStatus;

        var classDoing = 'doing';
        for (var i = 0, len = taskStateList.length; i < len; i++) {
            if (status == taskStateList[i].value) {
                DHstatus = '<span id="remove" class="' + classDoing + status + '">' + taskStateList[i].innerHTML + '</span>';
                break;
            }
        }

        if (status == "1") {
            _newDhandlefiles = [];
            $("#DHdevID").val("");
            $("#DHattachFiles").val("");
            $("#NewDHattachFiles").val("");
            $("#DHitFileTable").empty();

            $("#DHdevID").val(map[key].id);
            $("#DHtaskName").html("");
            $("#DHtaskName").html(map[key].testTaskName);
            $("#DHdevOverview").html("");
            $("#DHdevOverview").html(map[key].testTaskOverview);
            //			$("#DHplanStart").html("");
            //			if(map[key].planStartDate!=null){
            //				$("#DHplanStart").html(datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd"));
            //			}
            //			$("#DHplanEnd").html("");
            //			if(map[key].planEndDate!=null){
            //				$("#DHplanEnd").html(datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd"));
            //			}
            //			$("#DHplanWorkload").html("");
            //			$("#DHplanWorkload").html(map[key].planWorkload);
            $("#DHdevUserName").html("");
            $("#DHdevUserName").html(map[key].userName);

            $("#old_dHandle_testTaskId").val('');
            $("#old_dHandle_testTaskId").val(map[key].testUserId);

            $("#old_dHandle_createBy").val('');
            $("#old_dHandle_createBy").val(map[key].createBy);

            $("#remove").remove();
            $("#DHstatus").append(DHstatus);
            $("#DactualStart").val(datFmt(new Date(), "yyyy-MM-dd"));
            $("#loading").css('display', 'none');
            $("#dtestTage").html("")
            if (map[key].testStage == 1) {
                $("#dtestTage").html("系测");
            } else if (map[key].testStage == 2) {
                $("#dtestTage").html("版测");
            }
            if(map[key].projectType == 2){
                $('.is_new_project').hide();
            }else{
                $('.is_new_project').show();
            }
            modalType = "dHandle";
            if (data.attachements != undefined) {
                var _table = $("#DHitFileTable");
                var attMap = data.attachements;
                //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                for (var i = 0; i < attMap.length; i++) {
                    var _tr = '';
                    var file_name = attMap[i].fileNameOld;
                    var file_type = attMap[i].fileType;
                    var file_id = attMap[i].id;
                    var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    var _td_icon;
                    //<i class="file-url">'+data.attachements[i].filePath+'</i>
                    var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + attMap[i].fileS3Bucket + '</i><i class = "file-s3Key">' + attMap[i].fileS3Key + '</i></td>';
                    var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,' + attache + ')">删除</a></td>';
                    switch (file_type) {
                        case "doc":
                        case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
                        case "xls":
                        case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
                        case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
                        case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
                        default: _td_icon = '<img src="' + _icon_word + '" />'; break;
                    }
                    var row = JSON.stringify(data.attachements[i]).replace(/"/g, '&quot;');
                    _tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + _td_icon + " " + _td_name + _td_opt + '</tr>';
                    _table.append(_tr);
                    _Dhandlefiles.push(data.attachements[i]);
                    $("#DHattachFiles").val(JSON.stringify(_Dhandlefiles));
                }
            }
            $("#dHandle").modal("show");
        } else if (status == "2") {
            _Newhandlefiles = [];
            $("#HdevID").val("");
            $("#taskQuantity").val("");
            $("#HdevID").val(map[key].id);
            $("#HtaskName").html("");
            $("#HtaskName").html(map[key].testTaskName);
            $("#HdevOverview").html("");
            $("#HdevOverview").html(map[key].testTaskOverview);
            var startDate = datFmt(new Date(map[key].planStartDate), "yyyy年MM月dd");
            var startEnd = datFmt(new Date(map[key].planEndDate), "yyyy年MM月dd");
            $("#HitFileTable").empty();
            $("#HattachFiles").val("");

            $("#HdevUserName").html("");
            $("#HdevUserName").html(map[key].userName);

            $("#old_Handle_testTaskId").val('');
            $("#old_Handle_testTaskId").val(map[key].testUserId);

            $("#old_Handle_createBy").val('');
            $("#old_Handle_createBy").val(map[key].createBy);

            $("#case_Handle").show();
            $("#case_Handle_editDesignCaseNum").val('');
            $("#case_Handle_editDesignCaseNum").val(map[key].designCaseNumber);
            $("#case_Handle_editExecuteCaseNum").val('');
            $("#case_Handle_editExecuteCaseNum").val(map[key].executeCaseNumber);

            if (map[key].actualStartDate != null) {
                $("#actualStart").val(datFmt(new Date(map[key].actualStartDate), "yyyy-MM-dd"));
            } else {
                $("#actualStart").val(datFmt(new Date(), "yyyy-MM-dd"));
            }
            $("#actualEnd").val(datFmt(new Date(), "yyyy-MM-dd"));
            $("#remove").remove();
            $("#Hstatus").append(DHstatus);
            $("#HtestTage").html("")
            if (map[key].testStage == 1) {
                $("#HtestTage").html("系测");
            } else if (map[key].testStage == 2) {
                $("#HtestTage").html("版测");
            }
            $("#loading").css('display', 'none');
            modalType = "Handle";
            if (data.attachements != undefined) {
                var _table = $("#HitFileTable");
                var attMap = data.attachements;
                //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                for (var i = 0; i < attMap.length; i++) {
                    var _tr = '';
                    var file_name = attMap[i].fileNameOld;
                    var file_type = attMap[i].fileType;
                    var file_id = attMap[i].id;
                    var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    var _td_icon;
                    //<i class="file-url">'+data.attachements[i].filePath+'</i>
                    var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + attMap[i].fileS3Bucket + '</i><i class = "file-s3Key">' + attMap[i].fileS3Key + '</i></td>';
                    var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,' + attache + ')">删除</a></td>';
                    switch (file_type) {
                        case "doc":
                        case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
                        case "xls":
                        case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
                        case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
                        case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
                        default: _td_icon = '<img src="' + _icon_word + '" />'; break;
                    }
                    var row = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    _tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + _td_icon + " " + _td_name + _td_opt + '</tr>';
                    _table.append(_tr);
                    _handlefiles.push(attMap[i]);
                    $("#HattachFiles").val(JSON.stringify(_handlefiles));
                }
            }

            $("#Handle").modal("show");
        } else if (status == "3") {
            var map = data.dev;
            $("#OverFileTable").empty();
            $("#CodeDevID").val(map[key].id);
            $("#CodeDevName").html("");
            $("#CodeDevName").html(map[key].testTaskName);
            $("#CodeDevOverview").html("");
            $("#CodeDevOverview").html(map[key].testTaskOverview);
            //			$("#CodeDevplanStart").html("");
            //			if(map[key].planStartDate!=null){
            //				$("#CodeDevplanStart").html(datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd"));
            //			}
            //			$("#CodeDevplanEnd").html("");
            //			if(map[key].planEndDate!=null){
            //				$("#CodeDevplanEnd").html(datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd"));
            //			}
            //			$("#CodeplanWorkload").html(map[key].planWorkload);
            $("#CodedevUserName").html(map[key].userName);

            $("#CodeActualStartDate").html("");
            if (map[key].actualStartDate != null) {
                $("#CodeActualStartDate").html(datFmt(new Date(map[key].actualStartDate), "yyyy年MM月dd"));
            }
            $("#CodeActualEndDate").html("");
            if (map[key].actualEndDate != null) {
                $("#CodeActualEndDate").html(datFmt(new Date(map[key].actualEndDate), "yyyy年MM月dd"));
            }
            $("#CodetestTage").html("")
            if (map[key].testStage == 1) {
                $("#CodetestTage").html("系测");
            } else if (map[key].testStage == 2) {
                $("#CodetestTage").html("版测");
            }
            $("#CodeActualWorkload").html("");
            $("#CodeActualWorkload").html(map[key].actualWorkload);
            $("#remove").remove();
            $("#Codestatus").append(DHstatus);
            $("#loading").css('display', 'none');
            if (data.attachements != undefined) {
                var _table = $("#OverFileTable");
                var attMap = data.attachements;
                //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                for (var i = 0; i < attMap.length; i++) {
                    var _tr = '';
                    var file_name = attMap[i].fileNameOld;
                    var file_type = attMap[i].fileType;
                    var file_id = attMap[i].id;
                    var _td_icon;
                    //<i class="file-url">'+data.attachements[i].filePath+'</i>
                    var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + attMap[i].fileS3Bucket + '</i><i class = "file-s3Key">' + attMap[i].fileS3Key + '</i></td>';
                    //var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this,'+attMap[i].id+')">删除</a></td>';
                    switch (file_type) {
                        case "doc":
                        case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
                        case "xls":
                        case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
                        case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
                        case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
                        default: _td_icon = '<img src="' + _icon_word + '" />'; break;
                    }
                    var row = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    _tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + _td_icon + " " + _td_name + '</tr>';
                    _table.append(_tr);
                    _handlefiles.push(attMap[i]);
                    $("#OverSeeFiles").val(JSON.stringify(_handlefiles));
                }
            }
            $("#Codereview").modal("show");
        } else if (status == "4") {
            modalType = "examine";
            newexamineAttaches = [];
            $("#newExamineFiles").val("");
            $("#OverSeeFiles").val("");
            var map = data.dev;
            $("#OverFileTable").empty();
            $("#CodeDevID").val(map[key].id);
            $("#CodeDevName").html("");
            $("#CodeDevName").html(map[key].testTaskName);
            $("#CodeDevOverview").html("");
            $("#CodeDevOverview").html(map[key].testTaskOverview);
            $("#CodeDevplanStart").html("");
            if (map[key].planStartDate != null) {
                $("#CodeDevplanStart").html(datFmt(new Date(map[key].planStartDate), "yyyy年MM月dd"));
            }
            $("#CodeDevplanEnd").html("");
            if (map[key].planEndDate != null) {
                $("#CodeDevplanEnd").html(datFmt(new Date(map[key].planEndDate), "yyyy年MM月dd"));
            }
            $("#CodeplanWorkload").html(map[key].planWorkload);
            $("#CodedevUserName").html(map[key].userName);

            $("#CodeActualStartDate").html("");
            if (map[key].actualStartDate != null) {
                $("#CodeActualStartDate").html(datFmt(new Date(map[key].actualStartDate), "yyyy年MM月dd"));
            }
            $("#CodeActualEndDate").html("");
            if (map[key].actualEndDate != null) {
                $("#CodeActualEndDate").html(datFmt(new Date(map[key].actualEndDate), "yyyy年MM月dd"));
            }
            $("#CodetestTage").html("")
            if (map[key].testStage == 1) {
                $("#CodetestTage").html("系测");
            } else if (map[key].testStage == 2) {
                $("#CodetestTage").html("版测");
            }
            $("#CodeActualWorkload").html("");
            $("#CodeActualWorkload").html(map[key].actualWorkload);
            $("#remove").remove();
            $("#Codestatus").append(DHstatus);
            $("#loading").css('display', 'none');
            if (data.attachements != undefined) {
                var _table = $("#OverFileTable");
                var attMap = data.attachements;
                //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");

                for (var i = 0; i < attMap.length; i++) {
                    var _tr = '';
                    var file_name = attMap[i].fileNameOld;
                    var file_type = attMap[i].fileType;
                    var file_id = attMap[i].id;
                    var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    var _td_icon;
                    //<i class="file-url">'+data.attachements[i].filePath+'</i>
                    var _td_name = '<span>' + file_name + '</span><i class = "file-bucket">' + attMap[i].fileS3Bucket + '</i><i class = "file-s3Key">' + attMap[i].fileS3Key + '</i></td>';
                    var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,' + attache + ')">删除</a></td>';
                    switch (file_type) {
                        case "doc":
                        case "docx": _td_icon = '<img src="' + _icon_word + '" />'; break;
                        case "xls":
                        case "xlsx": _td_icon = '<img src=' + _icon_excel + ' />'; break;
                        case "txt": _td_icon = '<img src="' + _icon_text + '" />'; break;
                        case "pdf": _td_icon = '<img src="' + _icon_pdf + '" />'; break;
                        default: _td_icon = '<img src="' + _icon_word + '" />'; break;
                    }
                    var row = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    _tr += '<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(' + row + ')">' + _td_icon + " " + _td_name + _td_opt + '</tr>';
                    _table.append(_tr);
                    examineAttaches.push(attMap[i]);
                    $("#OverSeeFiles").val(JSON.stringify(examineAttaches));
                }
            }



            $("#Codereview").modal("show");
        }
    }
}

//获取 处理信息
function Handle(id, taskAssignUserId) {
    assignUser = taskAssignUserId;
    $.ajax({
        method: "post",
        url: "/testManage/worktask/getEditDevTask",
        data: { id: id },
        success: function (data) {
            ShowHandle(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }


    });
}

/*--------------处理----------------*/
//提交待处理
function PostDHandle() {
    var obj = {};

    obj.id = $("#DHdevID").val();
    obj.actualStartDate = $("#DactualStart").val();
    obj.oldTestUserId = $("#old_dHandle_testTaskId").val();
    obj.createBy = $("#old_dHandle_createBy").val();
    obj.taskAssignUserId = assignUser;
    var handle = JSON.stringify(obj);
    $("#loading").css('display', 'block');

    $.ajax({
        method: "post",
        url: "/testManage/worktask/DHandleTest",
        data: {
            handle,
            "DHattachFiles": $("#NewDHattachFiles").val(),
            "deleteFiles": JSON.stringify(deleteAttaches)
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            $("#dHandle").modal("hide");
            flagNum=2;
            searchTable();
            if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });
}
/**
 * 待评审提交
 * @returns
 */
function postExamineHandle() {
    var obj = {};

    obj.id = $("#CodeDevID").val();
    obj.taskAssignUserId = assignUser;
    var handle = JSON.stringify(obj);
    $("#loading").css('display', 'block');

    $.ajax({
        method: "post",
        url: "/testManage/worktask/examineHandle",
        data: {
            handle,
            "DHattachFiles": $("#newExamineFiles").val(),
            "deleteFiles": JSON.stringify(deleteAttaches)
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            $("#Codereview").modal("hide");
            flagNum=2;
            searchTable();
            if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });
}
//提交处理
function PostHandle() {
    var obj = {};

    obj.id = $("#HdevID").val();
    obj.actualStartDate = $("#actualStart").val();
    obj.actualEndDate = $("#actualEnd").val();
    obj.actualWorkload = $("#taskQuantity").val();
    obj.oldTestUserId = $("#old_Handle_testTaskId").val();
    obj.createBy = $("#old_Handle_createBy").val();
    obj.designCaseNumber = $("#case_Handle_editDesignCaseNum").val();
    obj.executeCaseNumber = $("#case_Handle_editExecuteCaseNum").val();
    obj.taskAssignUserId = assignUser;
    var handle = JSON.stringify(obj);

    $('#HandleDev').data('bootstrapValidator').validate();
    if (!$('#HandleDev').data('bootstrapValidator').isValid()) {
        return;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        method: "post",
        url: "/testManage/worktask/HandleTest",
        data: {
            handle,
            "HattachFiles": $("#newHattachFiles").val(),
            "deleteFiles": JSON.stringify(deleteAttaches)
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            $("#Handle").modal("hide");

            if (data.status == 1) {
                layer.alert('处理成功！', {
                    icon: 1,
                    title: "提示信息"
                });

            } else if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
            } else {
                layer.alert("处理失败", {
                    icon: 2,
                    title: "提示信息"
                });

            }
            flagNum=2;
            searchTable();

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }


    });
}
/*--------------处理----------------*/


//提交分派
function PostAssig() {
    var Remark = $("#testAssignRemark").val();
    var obj = {};
    obj.id = $("#assigDevID").val();
    $("#assigDevID").val("");

    obj.userName = $("#assignUser").val();
    obj.testUserId = $("#assignUser").attr("assignUserID");
    obj.oldTestUserId = $("#old_assign_testTaskId").val();
    obj.createBy = $("#old_assign_createBy").val();
    obj.taskAssignUserId = assignUser;

    $('#assignForm').data('bootstrapValidator').validate();
    if (!$('#assignForm').data('bootstrapValidator').isValid()) {
        return;
    }

    $("#loading").css('display', 'block');
    var assig = JSON.stringify(obj);
    $.ajax({
        method: "post",
        url: "/testManage/worktask/assigDev",
        data: {
            assig: assig,
            "Remark": Remark
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            $("#Assignment").modal("hide");

            if (data.status == 1) {
                layer.alert('分派成功！', {
                    icon: 1,
                    title: "提示信息"
                });

            } else if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
            } else {
                layer.alert("分派失败", {
                    icon: 2,
                    title: "提示信息"
                });

            }
            flagNum=2;
            searchTable();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });
}
//提交转派
function PostTransfer() {
    var UsserId = $("#oldUserID").val();
//	var newUserId = $("#TransferUser").attr("userID");
    var newUserId = $("#TransferUser_Id").val();
    var Remark = $("#TransferRemarks").val();
    if (UsserId == newUserId) {
        layer.alert("不能转派给相同人员", {
            icon: 2,
            title: "提示信息"
        });
        return;
    }
    $("#loading").css('display', 'block');
    var obj = {};
    obj.id = $("#TransferDevID").val();
    $("#TransferDevID").val("");
    obj.testUserId = newUserId;
    obj.oldTestUserId = $("#old_transfer_testTaskId").val();
    obj.createBy = $("#old_transfer_createBy").val();
    obj.taskAssignUserId = assignUser;
    var assig = JSON.stringify(obj);
    $.ajax({
        method: "post",
        url: "/testManage/worktask/assigDev",
        data: {
            assig: assig,
            "Remark": Remark
        },
        success: function (data) {
            $("#loading").css('display', 'none');
            $("#Transfer").modal("hide");
            if (data.status == 1) {
                layer.alert('转派成功！', {
                    icon: 1,
                    title: "提示信息"
                });

            } else if (data.status == "noPermission") {
                layer.alert('没有权限', {
                    icon: 2,
                    title: "提示信息"
                });
            } else {
                layer.alert("转派失败", {
                    icon: 2,
                    title: "提示信息"
                });

            }
            flagNum=2;
            searchTable();

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });
}

//清空下方表格内输入框的内容
function cleanrChoose() {
    $(".ui-search-input input").val("");
    $(".ui-search-input input").parent().children(".btn_clear").css("display", "none");
    $("#devlist").jqGrid('clearGridData');  //清空表格
    $("#devlist").jqGrid('setGridParam', {  // 重新加载数据
        datatype: 'json',
        url: ctxStatic + '/jqgrid/data/JSONData.json',
        page: 1
    }).trigger("reloadGrid");
}

function exportPerson_btn() {
    var obj = {};
    
    var inputs = $("[id*=gs_]");
    $.each(inputs,function(index,dom){
        var field = $(dom).attr("id").split("_")[1];
        obj[field]=$(dom).val();
    });
    
    obj.sidx = "id";
    obj.sord = "desc";
    /*var sidx=$("#testlist").jqGrid('getGridParam', "sortname");
    var sord=$("#testlist").jqGrid('getGridParam', "sortorder");*/
    obj = JSON.stringify(obj);

    var data = getJQAllData();
    var excelData = JSON.stringify(data);
    window.location.href = "/testManage/worktask/getExcelAllWork?excelDate=" + obj;
}

function getJQAllData() {
    //拿到grid对象
    var obj = $("#devlist");
    //获取grid表中所有的rowid值
    var rowIds = obj.getDataIDs();
    //初始化一个数组arrayData容器，用来存放rowData
    var arrayData = new Array();
    if (rowIds.length > 0) {
        for (var i = 0; i < rowIds.length; i++) {
            //rowData=obj.getRowData(rowid);//这里rowid=rowIds[i];
            arrayData.push(obj.getRowData(rowIds[i]));
        }
    }
    return arrayData;
}



function getUserName() {
    $.ajax({
        type: "post",
        url: '/testManage/worktask/getAllTestUser',
        dataType: "json",

        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var name = data[i].userName;
                var id = data[i].testUserId;
                if (i < 1) {
                    var opt = "<option value='' selected ='selected'>请选择</option>";
                    $("#taskUser").append(opt);
                }
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#taskUser").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });
}
/*function edUserName(ids) {
	$.ajax({
		type: "post",
		url: '/testManage/worktask/getAllTestUser',
		dataType: "json",

		success: function (data) {
			var opt = "<option value=''  >请选择</option>";
			$("#edtaskUser").append(opt);
			for (var i = 0; i < data.length; i++) {
				var name = data[i].userName;
				var id = data[i].testUserId;

				if (id == ids) {
					//$("#oldName").html(name);
					var opt = "<option value='" + id + "' selected ='selected'>" + name + "</option>";

				} else {
					var opt = "<option value='" + id + "'>" + name + "</option>";
				}

				$("#edtaskUser").append(opt);
			}
			$('.selectpicker').selectpicker('refresh');
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
		}

	});
}*/
//获取转派人员
function getTransferUser(ids) {
    $.ajax({
        type: "post",
        url: '/testManage/worktask/getAllTestUser',
        dataType: "json",
        success: function (data) {

            for (var i = 0; i < data.length; i++) {
                var name = data[i].userName;
                var id = data[i].testUserId;
                if (id == ids) {
                    // $("#TransferUser").val(name);
//					$("#TransferUser").attr("userID", id);
                    $("#TransferUser_Id").val(id);
                }
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });
}
//获取分派人员
function getAssignUser(ids) {
    $.ajax({
        type: "post",
        url: '/testManage/worktask/getAllTestUser',
        dataType: "json",
        success: function (data) {
            var option = "<option value='' selected ='selected'>请选择</option>";
            $("#assignUser").append(option);
            var opt = "";
            for (var i = 0; i < data.length; i++) {
                var name = data[i].userName;
                var id = data[i].testUserId;
                if (id == ids) {
                    $("#OleDveName").html(name);
                    opt += "<option value='" + id + "' >" + name + "</option>";
                } else {
                    opt += "<option value='" + id + "' >" + name + "</option>";
                }


            }
            $("#assignUser").append(opt);
            $('.selectpicker').selectpicker('refresh');
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });
}

function getSee(id) {
    $.ajax({

        method: "post",
        url: "/testManage/worktask/getSeeDetail",
        data: { "id": id },
        success: function (data) {
            selectdetail(data)
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }


    });
}
//编辑
function editDevTask() {
    var obj = {};
    obj.testStage = $("#edtestStage").val();
    obj.requirementFeatureId = $("#edAttribute").attr("edfeatureCode");
    obj.testTaskName = $("#etaskName").val();
    obj.testTaskOverview = $("#etaskOverview").val();
    //	obj.planStartDate=$("#edstartWork").val();
    //	obj.planEndDate=$("#edendWork").val();
    //	obj.planWorkload=$("#edworkLoad").val();
    obj.testUserId = $("#edit_taskUserId").val();
    obj.commissioningWindowId = $("#edAttribute").attr("edcommissioningWindowId");
    obj.featureStatus = $("#edAttribute").attr("edrequirementFeatureStatus");
    obj.id = $("#taskID").val();
    obj.createBy = $("#edit_createById").val();
    obj.oldTestUserId = $("#old_edit_taskUserId").val();
    obj.testTaskStatus = $("#workTaskStatus").val();
    obj.actualStartDate = $("#editStartDate").val();
    obj.actualEndDate = $("#editEndDate").val();
    obj.actualWorkload = $("#editWorkLoad").val();
    obj.designCaseNumber = $("#editDesignCaseNum").val();
    obj.executeCaseNumber = $("#editExecuteCaseNum").val();
    obj.taskAssignUserId = $("#edit_taskAssignUserId").val();
    obj.environmentType = $("#editEnvironmentType").val();

    obj.planStartDate = $("#edit_planStartDate").val();
    obj.planEndDate = $("#edit_planEndDate").val();

    /*var newName=$('#edtaskUser option:selected').text();
    var oldName=$("#oldName").html();
    if(newName!=oldName){
        obj.oldName=$("#oldName").html();
        $("#oldName").html("");
    }
    */
    obj = JSON.stringify(obj);
    $('#editTestF').data('bootstrapValidator').validate();
    if (!$('#editTestF').data('bootstrapValidator').isValid()) {
        return;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        type: "post",
        url: '/testManage/worktask/updateTestTask',

        data: {
            obj,
            "attachFiles": $("#newFiles").val(),
            "deleteFiles": JSON.stringify(deleteAttaches)
        },
        success: function (data) {
            $("#editTest").modal("hide");
            $("#loading").css('display', 'none');
            if (data.status == 1) {
                layer.alert('编辑成功！', {
                    icon: 1,
                    title: "提示信息"
                });

            } else if (data.status == "noPermission") {
                layer.alert(noPermission, {
                    icon: 2,
                    title: "提示信息"
                });
            } else {
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });

            }
            flagNum=2;
            searchTable();

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
        }

    });
}

function searchTable(){
	
	//gs_testTaskCode
	filter= $("#testlist").jqGrid('getGridParam', "postData");
    $("#testlist").jqGrid("setGridParam", { postData: {
        "filters":filter
    }}).trigger("reloadGrid");
}
function down(This) {
    if ($(This).hasClass("fa-angle-double-down")) {
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
    } else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
    }
}
//日期转换
function datFmt(date, fmt) { // author: meizz
    var o = {
        "M+": date.getMonth() + 1, // 月份
        "d+": date.getDate(), // 日
        "h+": date.getHours(), // 小时
        "m+": date.getMinutes(), // 分
        "s+": date.getSeconds(), // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3), // 季度
        "S": date.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

function showThisDiv(This, num) {
    if ($(This).hasClass("def_changeTit")) {
        $("#titleOfwork .def_controlTit").addClass("def_changeTit");
        $(This).removeClass("def_changeTit");
        if (num == 1) {
            $(".dealLog").css("display", "none");
            $(".workRemarks").css("display", "block");
        } else if (num == 2) {
            $(".dealLog").css("display", "block");
            $(".workRemarks").css("display", "none");
        }
    }
}

//清空上传文件
function clearUploadFile(idName) {
    $('#' + idName + '').val('');
}

//显示/隐藏列
function addCheckBox() {
    $("#colGroup").empty();
    var str = "";
    str = '<div class="onesCol"><input type="checkbox" value="testTaskCode" onclick="showHideCol( this )" /><span>任务编号</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="testTaskName" onclick="showHideCol( this )" /><span>任务名称</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="testTaskStatus" onclick="showHideCol( this )" /><span>状态</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="systemName" onclick="showHideCol( this )" /><span>涉及系统</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="requirementCode" onclick="showHideCol( this )" /><span>关联需求</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="featureCode" onclick="showHideCol( this )" /><span>关联测试任务</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="userName" onclick="showHideCol( this )" /><span>测试人员</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="testStage" onclick="showHideCol( this )" /><span>测试阶段</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="windowName" onclick="showHideCol( this )" /><span>投产窗口</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="taskAssignUserName" onclick="showHideCol( this )" /><span>任务分配</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="caseNum" onclick="showHideCol( this )" /><span>案例</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="defectNum" onclick="showHideCol( this )" /><span>缺陷</span></div>' +
        '<div class="onesCol"><input type="checkbox" value="opt" onclick="showHideCol( this )" /><span>操作</span></div>';
    $("#colGroup").append(str)
}
function showHideCol(This) {
    var colModel = $("#testlist").jqGrid('getGridParam', 'colModel');
    var width = 0;//获取当前列的列宽
    var arr = [];

    for (var i = 0; i < colModel.length; i++) {
        if (colModel[i]["hidden"] == false) {
            if (colModel[i]["name"] != "cb") {
                arr.push(colModel[i]["hidden"]);
            }
        }
    }
    if ($(This).is(':checked')) {
        $("#testlist").setGridParam().hideCol($(This).attr('value'));
        $("#testlist").setGridWidth($('.wode').width());
        if (arr.length == 1) {
            $("#testlist").jqGrid('setGridState', 'hidden');
        }
    } else {
        $("#testlist").jqGrid('setGridState', 'visible');
        $("#testlist").setGridParam().showCol($(This).attr('value'));
        $("#testlist").setGridWidth($('.wode').width());
    }
}
function isJSON(str) {
    if (typeof str == 'string') {
        try {
            JSON.parse(str);
            return true;
        } catch (e) {
            return false;
        }
    }
}

function addClickRow( idArr ){
    for( var i=0;i<idArr.length;i++ ){
        $("#"+idArr[i] ).on("click-row.bs.table",function(e,row, $element){
            $element.children( "td" ).find( "input[type=checkbox]" ).click();
        })
    }
}
function showTestWork( This ){
    var id = $(This).attr("val");
    layer.open({
        type: 2,
        title: false,
        closeBtn: 0,
        shadeClose: true,
        shade: false,
        move: false,
        area: ['100%', '100%'],
        id: "1",
        tipsMore: true,
        anim: 2,
        content:'/testManageui/testtask/toTestTaskInfo?id='+ id,
        btn: ['关闭'] ,
        btnAlign: 'c', //按钮居中
        no:function(){
            layer.closeAll();
        }
    });
    return ;
}
function showReq(){
    $("#reqModal").modal("show");
}
function urlParam(){
    if( parameterArr.obj.TaskPType ){
        var arr = parameterArr.obj.TaskPType;
        arr.split(",").map(function(val,index){
            $("#taskState option[value="+ val +"]").attr("selected", true);
        })
    }
    if( parameterArr.obj.userId ){
        $("#man_devUserId").val( parameterArr.obj.userId )
        $("#developer").val( decodeURIComponent(parameterArr.obj.userName)  )
    }
    $(".selectpicker").selectpicker('refresh');
}

function addTitle(){
	for(var i = 1;i < colIndex.length;i++){
		if(colIndex[i] == "testTaskStatus" || colIndex[i] == "testStage" ){
			$("#sp_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not (*abc* or *def*):查找不包含abc或不包含def的结果。" );
		}else if(colIndex[i] == "defectNum" || colIndex[i] == "caseNum" ){
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
        if(colIndex[i] == "testTaskStatus" || colIndex[i] == "testStage" ){
            $("#gs_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not (*abc* or *def*):查找不包含abc或不包含def的结果。" );
        }else if(colIndex[i] == "defectNum" || colIndex[i] == "caseNum" ){
            $("#gs_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n示例如下:\n<100、>100、<=100、>=100、>100 and <200、>=100 and <=200、\n<100 or >200、(>=100 and <=200) or (>=400 and <=500)" );
        }else if(colIndex[i] == "windowName"){
        	$("#gs_"+colIndex[i]).attr("title","语法说明:\n1、>:表示大于。\n2、<:表示小于\n3、>=:表示大于等于。\n4、<=:表示小于等于。\n5、and:表示同时满足条件。\n6、or:表示满足任一条件。\n7、搜索内容必须为yyyyMMdd格式\n示例如下:\n<20200101、>20200101、<=20200101、>=20200101、>20200101 and <20200201、>=20200101 and <=20200201、\n<20200101 or >20200201、(>=20200101 and <=20200201) or (>=20200401 and <=20200501)" );
        }else{
            $("#gs_"+colIndex[i]).attr("title","语法说明:\n1、*:表示匹配0或多个字符。\n2、and:表示同时满足条件。\n3、or:表示满足任一条件。\n4、not:表示否定后面的条件。\n示例如下:\n1、*abc*:查找包含abc的结果。\n2、*abc:查找以abc结尾的结果。\n3、abc*:查找以abc开头的结果。\n4、*abc* and *def*:查找同时包含abc和包含def的结果。\n5、*abc* or *def*:查找包含abc或者包含def的结果。\n6、not *abc*:查找不包含abc的结果。\n7、not *abc* or not *def*:查找不包含abc或不包含def的结果。\n8、not (*abc* and *def*):查找不同时包含abc和def的结果。\n9、not (*abc* or *def*):查找不包含abc或不包含def的结果。\n10、(*ab* and *cd*) or (*df* and *gh*):查找同时包含ab和cd或者同时包含df和gh的结果。" );
        }
    }
}

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

function getFilter(){
	$("#loading").css('display', 'block');
	$("[name=filterType] option[value!='']").remove();
	$.ajax({
		type: "POST",
		url: "/report/defectReport/selectDefectReportList",
		dataType: "json",
		async:false,
		data: {
			"menuUrl": "/testManageui/workTask/toWorktask"
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