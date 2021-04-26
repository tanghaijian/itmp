var errorDefect = '系统内部错误，请联系管理员 ！！！';
var userArr = [];
var taskArr = [];
var caseArr = [];
var leadCaseArr = [];
$(function(){
	$("#createBy").click(function(){
		$("#userbutton").attr("data-user",$(this).attr("data-user"));
		userArr = [];
		clearSearchUser();
   	 	//userTableShow("/system/user/getAllUserModal2",'');
		//下面两个方法在 /testManageWeb/src/main/resources/static/js/common/modal.js
		 getProject();//
	     userTableShow(null, null, false);
   	 	$("#searchFlag").val("");
		$("#userModal").modal('show');
	});
	$("#donginguser").click(function(){
		$("#userbutton").attr("data-user",$(this).attr("data-user"));
		 clearSearchUser();
		 //下面两个方法在 /testManageWeb/src/main/resources/static/js/common/modal.js
		 getProject();//
	     userTableShow(null, null, false);
	     $("#userModal").modal("show");
	});
	
	$("[name=checkTestTask]").click(function(){
		var dataTask = $(this).attr("data-task");
		taskArr = [];
		var isSingle = true;
		if(dataTask == 'search'){
			isSingle = false;
		}
		$("#taskbutton").attr("data-task",dataTask);
		clearSearchTask();
		$("#donginguserId").val(uid);
	    $("#donginguser").val(username);
	    $("#taskState").selectpicker('val', '2');
	    $("#taskState").selectpicker('refresh');
		taskTableShow(isSingle);
		
		$("#isSingleFlag").val(isSingle);
		$("#testTaskModal").modal('show');
	});
	//关联案例 弹框
	$("#relateCase").click(function(){
		caseArr = [];
		clearSearchCase();
		caseTableShow();
		$("#testCaseModal").modal('show');
	})
	//引入测试集案例 弹框
	$("#leadInCase").click(function(){
		leadCaseArr = [];
		clearSearchCase();
		var reqFeature = $("#reqFeature").val();
		var reqFeatureId = $("#reqFeatureId").val();
		testSet_fuzzySearch.search_radio_give('leadTestTask',reqFeature,$("#leadTestTaskId"),reqFeatureId);
		$("#leadTestTask").val(reqFeature).change();
		$('#check_box').html('');
		$('#check_box').html(
				'<label class=" font_left fontWeihgt"> 引入规则：</label>'+
				'<label><input type="radio" name="leadType" value="1" checked="checked" />  案例编号一致   </label>'+
				' <label> <input type="radio" name="leadType" value="2"/>  重新生成案例编号    </label>'
         );
		caseTableShow2();
		$("#leadInTestCaseModal").modal('show');
	})
	testSet_fuzzySearch.fuzzy_search_radio('leadTestTask','/testManage/testtask/getAllReqFeatureByCodeOrName','codeOrName','id','28px',$("#leadTestTaskId"),true);
	$('#leadTestTask_list').on('click', '.leadTestTask_search_item', function () {
		var val = $(this).text();
		var id = $(this).data('id');
		$("#leadTestTaskId").val(id);
		$("#leadTestTask").val(val).attr('username', val).change();
		$('#leadTestTask_list').hide();
	})
	
	// 见 /testManageWeb/src/main/resources/static/js/testExecution/testSetCaseCommonOpt.js
	/*$("#testSetCaseSystemName").click(function(){
		clearSystem();
		testSetCaseSystemTableShow();
		$("#testSetCaseSystemModal").modal('show');
	})*/
});
function new_taskTableShow(){
	var isSingle = $("#isSingleFlag").val();//"true" "false"
	if(isSingle == "true"){
		isSingle = true;
	}else if(isSingle == "false"){
		isSingle = false;
	}
	
	taskTableShow(isSingle);
}

function userTableShowSelect() {
    var notWithUserID = '';
    var systemId = '';
    if ($("userbutton").attr("data-user") == "search") {//多选
        userTableShow(null, null, false);
    } else if ($("userbutton").attr("data-user") == "workSelect") {
        userTableShow(null, null, false);
    } else if ($("userbutton").attr("data-user") == "workSelect") {
    	notWithUserID
    	userTableShow(notWithUserID, null, false);
    } else {//单选
        if ($("userbutton").attr("data-user") == "new") {
            notWithUserID = $("#new_assignUserId").val();
            systemId = $("#systemId").val();
            $("#userSystemName").val($("#system_Name").val());
        } 
        userTableShow(notWithUserID, systemId, true);
    }
}
function getWorkTask(that){
	$.ajax({
		url:"/testManage/worktask/getWorkTaskByTestTaskId",
		type:"post",
		dataType:"json",
		data:{
			featureId:$(that).prev().val(),
		},
		success:function(data){
			var str = '<option value="">请选择</option>';
			if(data.rows!=null){
				for(var index in data.rows){
					str += '<option value="'+data.rows[index].id+'">'+data.rows[index].testTaskName+'</option>'
				}
			}
			$("#leadWordTask").empty();
			$("#leadWordTask").append(str);
			$(".selectpicker").selectpicker('refresh')
		}
	})
}
function getTestSet(that){
	$.ajax({
		url:"/testManage/testSet/getAllTestSet3",
		type:"post",
		dataType:"json",
		data:{
			testTaskId:$(that).val()
		},
		success:function(data){
			var str = '<option value="">请选择</option>';
			if(data.rows!=null){
				for(var index in data.rows){
					str += '<option value="'+data.rows[index].id+'">'+data.rows[index].testSetName+'</option>'
				}
			}
			$("#leadTestSet").empty();
			$("#leadTestSet").append(str);
			$(".selectpicker").selectpicker('refresh')
		}
	})
}
// =========================工作任务弹框start=====================
function taskTableShow(isSingle){
	$("#loading").css('display','block');
	 $("#listTask").bootstrapTable('destroy');
	 $("#listTask").bootstrapTable({  
		url:'/testManage/modal/getAllTestTask', 
		method:"post",
	    queryParamsType:"",
	    pagination : true,
	    sidePagination: "server",
	    contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
	    pageNumber : 1,
	    pageSize : 10,
	    pageList : [ 5, 10, 15],
	    singleSelect : isSingle,// 单选
	    queryParams : function(params) {
         var param={ 
        	testTaskCode : $.trim($("#taskCode").val()),
        	testTaskName : $.trim( $("#taskName").val()),
        	workTaskStatus : $.trim( $("#taskState").val()),
        	userName:$("#donginguserId").val() != null?$("#donginguserId").val().toString():'',
        	pageNumber:params.pageNumber,
        	pageSize:params.pageSize 
         }
        return param;
    },
    columns : [{
        checkbox : true,
        width : "30px",
        formatter : function(value, grid, rows,
				state){
        	var flag = false;
        	$.each(taskArr,function(index,value){
        		if(value.id == grid.id){
        			flag = true;
        		}
        	})
        	return flag;
        }
    },{
        field : "id",
        title : "id",
        visible : false,
        align : 'center'
    },{
        field : "testTaskCode",
        title : "任务编号",
        align : 'center'
    },{
        field : "testTaskName",
        title : "任务名称",
        align : 'center'
    },{
    	field : "testTaskStatus",
    	title : "任务状态",
    	align : 'center',
    	formatter : function(value, row, index) {
            //var className = "doing";
           // var taskStateList = $("#taskState").find("option");
            for (var i = 0,len = taskStateList.length;i < len;i++) {
                if(row.testTaskStatus == taskStateList[i][0]){
                   // className +=row.testTaskStatus;
                    return "<span class=''>"+taskStateList[i][1]+"</span>";
                }
            }
        }
    },{
        field : "systemName",
        title : "所属系统",
        align : 'center'
    },{
        field : "requirementCode",
        title : "所属需求",
        align : 'center'
    }],
    onLoadSuccess:function(){
    	$("#loading").css('display','none');
    },
    onLoadError : function() {

    },
    onCheck:function(row,dom){
    	if(!isSingle){
    		taskArr.push(row);
    	}
    },
    onUncheck:function(row,dom){
    	$.each(taskArr,function(index,value){
    		if(value!=undefined && row.id == value.id){
    			taskArr.splice(index,1);
    		}
    	})
    },
    onCheckAll:function(rows){
    	var length = taskArr.length;
    	$.each( rows,function(index,value){
    		var i = 0;
    		for( ;i < length;i++){
    			if(taskArr[i].id == value.id){
    				break;
    			}
    		}
    		if(i == length){
    			taskArr.push(value);
			}
    	} );
    },
    onUncheckAll:function(rows){
    	for(var i = taskArr.length-1;i >= 0;i--){
    		for(let value2 of rows){
    			if( taskArr[i].id == value2.id){
    				taskArr.splice(i,1);
    				break;
    			}
    		}
    	}
    }
});
}
// 清空
function clearSearchTask(){
	$("#taskCode").val('');
	$("#taskName").val('');
	$("#taskState").selectpicker('val','');
	$("#donginguserId").val('');
    $("#donginguser").val('');
    $(".color1 .btn_clear").css("display","none");
}

function commitTask(){
	var selectContents = $("#listTask").bootstrapTable('getSelections');
	var dataTask = $("#taskbutton").attr("data-task") == 'search';
	selectContents = dataTask?taskArr:selectContents;
    if(typeof(selectContents[0]) == 'undefined') {
    	 layer.alert('请选择一列数据！', {icon: 0});
       return false;
    }else{
		 if(dataTask){
			 var ids = [];
			 var name = '';
			 $.each(selectContents,function(index,value){
				 ids.push(value.id);
				 name += value.testTaskName + ',';
			 })
			 $("#workTaskId").val(ids);
				$("#workTaskName").val(name.substr(0,name.length-1));
				$("#workTaskName").next().css('display','block');
		 }else{
			 $("#checkTestTask").attr("idValue",selectContents[0].id);
			 $("#checkTestTask").val(selectContents[0].testTaskName);
			 $("#testStage").val(selectContents[0].testStage==1? "系测":"版测");
			 $("#testSetName").val(selectContents[0].testTaskName);
		 }
		$("#testTaskModal").modal("hide");
	 }
}
/* 新增执行人触发弹窗 */
function addExecuteUserShow(round){
	$("#userbutton").attr("data-user","add");
	$("#userbutton").attr("round",round);
	userArr = [];
	clearSearchUser();
	//userTableShow("/testManage/testSet/getUserTable",round);
	
	//下面两个方法在 /testManageWeb/src/main/resources/static/js/common/modal.js
	 getProject();//
    userTableShow(null, null, false);
	$("#searchFlag").val(round);
	$("#userModal").modal('show');
}
/* 搜索人员 */
function searchUser(){
	var round = $("#searchFlag").val();
	if(round == ""){
		userTableShow("/system/user/getAllUserModal2",'');
	}else{
		userTableShow("/testManage/testSet/getUserTable",round);
	}
}

// ====================人员弹框start=======================
function userTableShow(url,round){
	var testSetId = $("#taskTestId").val();
	$("#loading").css('display','block');
	   $("#userTable").bootstrapTable('destroy');
	    $("#userTable").bootstrapTable({  
	    	url:url,
	    	method:"post",
	        queryParamsType:"",
	        pagination : true,
	        sidePagination: "server",
	        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
	        pageNumber : 1,
	        pageSize : 10,
	        pageList : [ 5, 10, 15],
	        singleSelect : false,// 多选
	        queryParams : function(params) {
	             var param={ 
            		 userName: $.trim($("#userName").val()),
            		 companyName :  $.trim($("#companyName").val()),
            		 deptName : $.trim($("#deptName").val()),
	                 pageNumber:params.pageNumber,
	                 pageSize:params.pageSize, 
	                 testSetId:testSetId,
	                 executeRound:round
	             }
	            return param;
	        },
	        columns : [{
	            checkbox : true,
	            width : "30px",
	            formatter : function(value, grid, rows,
						state){
	            	var flag = false;
	            	$.each(userArr,function(index,value){
	            		if(value.id == grid.id){
	            			flag = true;
	            		}
	            	})
	            	return flag;
	            }
	        },{
	            field : "id",
	            title : "id",
	            visible : false,
	            align : 'center'
	        },{
	            field : "userName",
	            title : "姓名",
	            align : 'center'
	        },{
	            field : "userAccount",
	            title : "用户名",
	            align : 'center'
	        },{
	        	field : "companyName",
	        	title : "所属公司",
	        	align : 'center'
	        },{
	            field : "deptName",
	            title : "所属处室",
	            align : 'center'
	        }],
	        onLoadSuccess:function(){
	        	$("#loading").css('display','none');
	        },
	        onLoadError : function() {

	        },
	        onCheck:function(row,dom){
	        	userArr.push(row);
	        },
	        onUncheck:function(row,dom){
	        	$.each(userArr,function(index,value){
	        		if(value!=undefined && row.id == value.id){
	        			userArr.splice(index,1);
	        		}
	        	})
	        },
	        onCheckAll:function(rows){
	        	var length = userArr.length;
	        	$.each( rows,function(index,value){
	        		var i = 0;
	        		for( ;i < length;i++){
	        			if(userArr[i].id == value.id){
	        				break;
	        			}
	        		}
	        		if(i == length){
        				userArr.push(value);
        			}
	        	} );
	        },
	        onUncheckAll:function(rows){
	        	for(var i = userArr.length-1;i >= 0;i--){
	        		for(let value2 of rows){
	        			if( userArr[i].id == value2.id){
	        				userArr.splice(i,1);
	        				break;
	        			}
	        		}
	        	}
	        }
	    });   
} 

function clearSearchUser(){
	 $("#userName").val('');
	 $("#deptName").val('');
	 $("#companyName").val('');
	 $(".color1 .btn_clear").css("display","none");
	 $("#project").selectpicker("val",'');
	 select_rows = [];
}
/*
function commitUser(){
	var selectContents = userArr;
    if(typeof(selectContents[0]) == 'undefined') {
    	 layer.alert('请选择一列数据！', {icon: 0});
       return false;
    }else{
    	var ids = [];
    	var name = '';
		 $.each(selectContents,function(index,value){
			 ids.push(value.id);
			 name += value.userName + ',';
		 })
		 if($("#userbutton").attr("data-user")== 'search'){
			$("#userId").val(JSON.stringify(ids));
			$("#createBy").val(name.substr(0,name.length-1));
			$("#createBy").next().css('display','block');
		 }else{
			 var round = $("#userbutton").attr("round");
			 addExecuteUser(ids,round);
		 }
		$("#userModal").modal("hide");
	 }
}*/

function commitUser() {
    if ($("#userbutton").attr("data-user")== 'search'|| $("#userbutton").attr("data-user")== 'workSelect'|| $("#userbutton").attr("data-user")== 'add') { // 多选
        var rowData = select_rows;
        if (typeof(rowData) == 'undefined' || rowData.length == 0) {
            layer.alert("请至少选择一条数据", {
                icon: 2,
                title: "提示信息"
            });
            return false;
        } else {
            var ids = [];
            var names = [];
            for (var i = 0, len = rowData.length; i < len; i++) {
                ids.push(rowData[i].id);
                names.push(rowData[i].userName);
            }
            if ($("#userbutton").attr("data-user")== 'search') {
                $("#userId").val(ids);
                $("#createBy").val(names).change(function () {
                    $(this).parent().children(".btn_clear").css("display", "block");
                }).change();
            } else if ( $("#userbutton").attr("data-user")== 'workSelect') {
                $("#donginguserId").val(ids);
                $("#donginguser").val(names).change(function () {
                    $(this).parent().children(".btn_clear").css("display", "block");
                }).change();
            } else if ( $("#userbutton").attr("data-user")== 'add'){
            	 var round = $("#userbutton").attr("round");
    			 addExecuteUser(ids,round);
            }
        }
    } else {// 单选
        var rowData = $("#userTable").bootstrapTable('getSelections')[0];
        if (typeof(rowData) == 'undefined') {
            layer.alert("请选择一条数据", {
                icon: 2,
                title: "提示信息"
            });
            return false;
        } else {
            if ($("userbutton").attr("data-user") == "new") {
                $("#new_assignUserId").val(rowData.id);
                $("#new_assignUserName").val(rowData.userName);
            } 
        }
    }
    $("#userModal").modal("hide");
}
function addExecuteUser(userIds,round){
	var testSetId = $("#taskTestId").val();
	$("#loading").css('display','block');
	$.ajax({
        type: "post",
        url: "/testManage/testSet/addExcuteUser",
        dataType: "json",
        data: {
        	testSetId : testSetId,
        	executeRound : round,
        	userIdStr : JSON.stringify(userIds)
        },
        success: function(data) {
            if(data.status == 2){
            	layer.alert("新增执行人失败",{icon : 2});
            }else{
            	layer.alert("新增执行人成功",{icon : 1});
         
            	assignPerson(testSetId,round);
            	$("#loading").css('display','none');
            }
        }
    });
}

// ====================关联测试案例弹框start=======================
function caseTableShow(){
	var testSetId1 = $("#taskTestId").val()==null? testSet.id:$("#taskTestId").val()
	$("#loading").css('display','block');
	   $("#listCase").bootstrapTable('destroy');
	    $("#listCase").bootstrapTable({  
	    	url:"/testManage/testCase/getCaseInfo",
	    	method:"post",
	        queryParamsType:"",
	        pagination : true,
	        sidePagination: "server",
	        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
	        pageNumber : 1,
	        pageSize : 10,
	        pageList : [ 5, 10, 15],
	        queryParams : function(params) {
	             var param={ 
	            	 testSetId:testSetId1,
	            	 caseNumber: $.trim($("#caseCode").val()),
	            	 caseName: $.trim($("#caseName").val()),
	            	 businessType: $.trim($("#case_businessType").val()),
	            	 testPoint: $.trim($("#case_testPoint").val()),
	            	 moduleName: $.trim($("#case_moduleName").val()),
	                 page:params.pageNumber,
	                 rows:params.pageSize 
	             }
	            return param;
	        },
	        columns : [{
	            checkbox : true,
	            width : "30px",
	            formatter : function(value, grid, rows,
						state){
	            	var flag = false;
	            	$.each(caseArr,function(index,value){
	            		if(value.id == grid.id){
	            			flag = true;
	            		}
	            	})
	            	return flag;
	            }
	        },{
	            field : "id",
	            title : "id",
	            visible : false,
	            align : 'center'
	        },{
	            field : "caseNumber",
	            title : "案例编号",
	            align : 'center'
	        },{
	            field : "caseName",
	            title : "案例名称",
	            align : 'center'
	        },{
	        	field : "systemName",
	        	title : "涉及系统",
	        	align : 'center'
	        },{
	            field : "userName",
	            title : "创建人",
	            align : 'center'
	        },{
	            field : "businessType",
	            title : "业务类型",
	            align : 'center'
	        },{
	            field : "moduleName",
	            title : "模块",
	            align : 'center'
	        },{
	            field : "testPoint",
	            title : "测试项",
	            align : 'center'
	        }],
	        onLoadSuccess:function(){
	        	$("#loading").css('display','none');
	        },
	        onLoadError : function() {

	        },
	        onCheck:function(row,dom){
	        	caseArr.push(row);
	        },
	        onUncheck:function(row,dom){
	        	$.each(caseArr,function(index,value){
	        		if(value!=undefined && row.id == value.id){
	        			caseArr.splice(index,1);
	        		}
	        	})
	        },
	        onCheckAll:function(rows){
	        	var length = caseArr.length;
	        	$.each( rows,function(index,value){
	        		var i = 0;
	        		for( ;i < length;i++){
	        			if(caseArr[i].id == value.id){
	        				break;
	        			}
	        		}
	        		if(i == length){
	        			caseArr.push(value);
        			}
	        	} );
	        },
	        onUncheckAll:function(rows){
	        	for(var i = caseArr.length-1;i >= 0;i--){
	        		for(let value2 of rows){
	        			if( caseArr[i].id == value2.id){
	        				caseArr.splice(i,1);
	        				break;
	        			}
	        		}
	        	}
	        }
	    });   
} 
/* 清空 */
function clearSearchCase(){
	 $("#caseName").val('');
	 $("#caseCode").val('');
	 $(".color1 .btn_clear").css("display","none");
	 $("#leadTestTask").val('');
	 $("#leadTestTaskId").val('');
	 $("#case_businessType").val('');
	 $("#case_testPoint").val('');
	 $("#case_moduleName").val('');
	 $("#leadWordTask").selectpicker('val','');
	 $("#leadTestSet").selectpicker('val','');
	 testSet_fuzzySearch.search_radio_clear('leadTestTask', $("#leadTestTaskId"))
}
//提交关联测试案例
function commitCase(){
	var selectContents = caseArr;
	var testSetId1;
	if($("#taskTestId").val()!=null){
		testSetId1 = $("#taskTestId").val();
	}else{
		testSetId1 = testSet.id;
	}
	var caseNumbers = [];
	$.each(selectContents,function(index,value){
		caseNumbers.push(value.caseNumber);
	})
	$("#loading").css('display','block');
	$.ajax({
        type: "post",
        url: "/testManage/testSet/relateTestSetCase",
        dataType: "json",
        data: {
        	'caseStr' : JSON.stringify(selectContents),
        	'idStr' : JSON.stringify(caseNumbers),
        	'testSetId': testSetId1
        },
        success: function(data) {
           if(data.status == 2){
        	   layer.alert("关联失败",{icon : 2});
           }else{
        	   testExecution.right_Opt.executionTable.initExecutionTable();
               //$("#executionTable_div").attr("total",data.total);
        	   $("#testCaseModal").modal('hide');
        	   layer.alert("关联成功",{icon : 1});
           }
           $("#loading").css('display','none');
        }
    });
}
var leadSearchParam = {
	 otherTestSetId : "",
	 testSetId : "",
	 testTaskId : "",
	 workTaskId : "",
}
// ===================引入测试集案例==============================
function caseTableShow2(){
	var testSetId1 = $("#taskTestId").val()==null? testSet.id:$("#taskTestId").val();
	leadSearchParam.otherTestSetId = testSetId1;
	leadSearchParam.testSetId =  $("#leadTestSet").val();
	leadSearchParam.testTaskId = $("#leadTestTaskId").val();
	leadSearchParam.workTaskId = $("#leadWordTask").val();
	
	$("#loading").css('display','block');
	   $("#listCase2").bootstrapTable('destroy');
	    $("#listCase2").bootstrapTable({  
	    	url:"/testManage/testSet/getOtherTestSetCase",
	    	method:"post",
	        queryParamsType:"",
	        pagination : true,
	        sidePagination: "server",
	        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
	        pageNumber : 1,
	        pageSize : 10,
	        pageList : [ 5, 10, 15],
	        queryParams : function(params) {
	             var param={ 
	            	 otherTestSetId:testSetId1,
	            	 testSetId: $("#leadTestSet").val(),
	            	 testTaskId: $("#leadTestTaskId").val(),
	            	 //testTaskId: 5,
	            	 workTaskId: $("#leadWordTask").val(),
	                 page:params.pageNumber,
	                 rows:params.pageSize 
	             }
	            return param;
	        },
	        columns : [{
	            checkbox : true,
	            width : "30px",
	            formatter : function(value, grid, rows,state){
	            	var flag = false;
	            	$.each(leadCaseArr,function(index,value){
	            		if(value.id == grid.id){
	            			flag = true;
	            		}
	            	})
	            	return flag;
	            }
	        },{
	            field : "id",
	            title : "id",
	            visible : false,
	            align : 'center'
	        },{
	            field : "caseNumber",
	            title : "案例编号",
	            align : 'center'
	        },{
	            field : "caseName",
	            title : "案例名称",
	            align : 'center'
	        },{
	        	field : "systemName",
	        	title : "涉及系统",
	        	align : 'center'
	        },{
	            field : "userName",
	            title : "创建人",
	            align : 'center'
	        }],
	        onLoadSuccess:function(){
	        	$("#loading").css('display','none');
	        },
	        onLoadError : function() {

	        },
	        onCheck:function(row,dom){
	        	leadCaseArr.push(row);
	        },
	        onUncheck:function(row,dom){
	        	$.each(leadCaseArr,function(index,value){
	        		if(value!=undefined && row.id == value.id){
	        			leadCaseArr.splice(index,1);
	        		}
	        	})
	        },
	        onCheckAll:function(rows){
	        	var length = leadCaseArr.length;
	        	$.each( rows,function(index,value){
	        		var i = 0;
	        		for( ;i < length;i++){
	        			if(leadCaseArr[i].id == value.id){
	        				break;
	        			}
	        		}
	        		if(i == length){
	        			leadCaseArr.push(value);
        			}
	        	} );
	        },
	        onUncheckAll:function(rows){
	        	for(var i = leadCaseArr.length-1;i >= 0;i--){
	        		for(let value2 of rows){
	        			if( leadCaseArr[i].id == value2.id){
	        				leadCaseArr.splice(i,1);
	        				break;
	        			}
	        		}
	        	}
	        }
	    });   
} 
//引入选择案例
function leadCaseCommit(){
	var selectContents = leadCaseArr;
	if(typeof(selectContents[0]) == 'undefined'){
		layer.alert("请选择一列数据！",{icon:0});
	}else{
		var testSetId1;
		if($("#taskTestId").val()!=null){
			testSetId1 = $("#taskTestId").val();
		}else{
			testSetId1 = testSet.id;
		}
		var caseNumbers = [];
		$.each(selectContents,function(index,value){
			caseNumbers.push(value.caseNumber);
		})
		$("#loading").css('display','block');
		$.ajax({
	        type: "post",
	        url: "/testManage/testSet/leadInTestSetCase",
	        dataType: "json",
	        data: {
	        	'caseStr' : JSON.stringify(selectContents),
	        	'idStr' : JSON.stringify(caseNumbers),
	        	'testSetId': testSetId1,
	        	flag : $("input[name='leadType']:checked").val()
	        },
	        success: function(data) {
	           if(data.status == 2){
	        	   layer.alert("引入失败",{icon : 2});
	           }else{
	        	   testExecution.right_Opt.executionTable.initExecutionTable();
                   //$("#executionTable_div").attr("total",data.total);
	        	   $("#leadInTestCaseModal").modal('hide');
	        	   layer.alert("引入成功",{icon : 1});
	           }
	           $("#loading").css('display','none');
	        }
	    });
	}
}
//引入全部案例
function leadAllCaseCommit(){
	$.ajax({
		url:"/testManage/testSet/leadInAllTestSetCase",
		type:"post",
		dataType:"json",
		data:{
			otherTestSetId : leadSearchParam.otherTestSetId,
			testSetId : leadSearchParam.testSetId,
			testTaskId : leadSearchParam.testTaskId,
			//testTaskId : 5,
			workTaskId : leadSearchParam.workTaskId,
			flag : $("input[name='leadType']:checked").val()
		},
		 success: function(data) {
           if(data.status == 2){
        	   layer.alert(data.message,{icon : 2});
           }else{
        	   testExecution.right_Opt.executionTable.initExecutionTable();
               //$("#executionTable_div").attr("total",data.total);
        	   $("#leadInTestCaseModal").modal('hide');
        	   layer.alert("引入成功",{icon : 1});
           }
           $("#loading").css('display','none');
        }
	})
}


var testSet_fuzzySearch = {

		//防抖
		_debounce : function (fn, delay) {
			var timer;
			return function () {
					var context = this;
					var args = arguments;
					clearTimeout(timer);
					timer = setTimeout(function () {
							fn.apply(context, args);
					}, delay)
			}
		},

		//搜索单选
		fuzzy_search_radio : function (ele, url, top, name, id, userId, out  ) {
			var ele_name = '#'+ele;
			var _ele = $(ele_name);
			var list = ele_name+'_list';
			_ele.after('<ul class="search_list" id="'+ele+'_list"></ul>');
			_ele.attr({'autocomplete':"off" });
			$(list).css({ top:top });
			_ele.on('keyup enter',testSet_fuzzySearch._debounce(function () {
				if ($.trim(_ele.val())) {
					$.ajax({
						url: url,
						datatype: 'json',
						contentType: "application/x-www-form-urlencoded; charset=utf-8",
						method: "post",
						data: {
							codeOrName:$.trim(_ele.val())
						},
						success: function (data) {
							if (data.list.length) {
								$(list).empty();
								data.list.map(function (res) {
									$(list).append('<li class="'+ele+'_search_item search_list_item" data-id="'+res.id+'">'+res.featureName+'</li>');
									$(list).show();
								})
							} else {
								$(list).hide();
							}
						},
						error: function () {
							layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
						}
					})
				} else {
					userId.val('');
					_ele.val('');
				}
			},1000))
			_ele.on('change', function () {
				if (!userId.val()) {
					userId.val('');
					_ele.val('');
				}
				if (userId.val() && _ele.attr('username') && _ele.attr('username') != _ele.val()) {
					userId.val('');
					_ele.val('').change();
				}
			})
			if (!out) {
				$(list).on('click', '.'+ele+'_search_item', function () {
					var val = $(this).text();
					var id = $(this).data('id');
					userId.val(id);
					_ele.val(val).attr('username',val).change();
					$(list).hide();
				})
			}
			$(document).on('click', function (e) {
				if (!$(e.target).hasClass(ele) && !$(e.target).hasClass(ele+'_search_item')) {
					$(list).hide();
				}
			})

		},
		//搜索单选清空
		search_radio_clear : function (ele, userId) {//输入框id,存id元素
			$('#'+ele).val('').removeAttr('username');
			userId.val('');
		},
		//搜索单选赋值
		search_radio_give : function (ele, value, userId, id) {//输入框id,值,存id元素,id
			$('#'+ele).val(value).attr('username',value);
			userId.val(id);
		}
}


