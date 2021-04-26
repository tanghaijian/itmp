var span_ = " | ";
var param = {};
$(function(){
	$("#searchBatchName").val(testTaskName0 == "$testTaskName"?"":testTaskName0);
	param.testTaskType = testTaskType$;
	param.testTaskName = $("#searchBatchName").val();
	initTable();
	formValidator();
	downOrUpButton();
	jqgridAutoWidth();
	buttonClear(); 
})
//切换样式
function changeStyle(){
	window.location.href = "/testManageui/testSet/toTestSet?testTaskType="+testTaskType$+"&testTaskName="+testTaskName0;
}

function clearTestSetForm(){
	$("#testSetId").val("");
	$("#testSetName").val("");
	$("#checkTestTask").val(testTaskName0 == "$testTaskName"?"":testTaskName0);
	$("#checkTestTask").attr('idValue',testTaskId0 == "$testTaskId"?"":testTaskId0);
	$("#testSetDescription").val("");
	$("#testStage").val("");
	$("#excuteRound").val("");
	$("#newTestSetForm").data('bootstrapValidator').destroy();
	formValidator();
}

function addTestSet(){
	clearTestSetForm();
	$("#testSetModalLabel").text("新建测试集");
	$("#newTestSetModal").modal('show');
}

//测试集列表
function initTable(){
	$("#loading").css('display','block');
    $("#testSetList").jqGrid("clearGridData");
    $("#testSetList").jqGrid({
        url:"/testManage/testSet/getAllTestSet",
        datatype: 'json',
        mtype:"POST",
        height: 'auto',
        width: $(".content-table").width()*0.999,
        postData : {
        	//"testSetStr" : JSON.stringify(param)
        },
        colNames:['id','测试集编号','测试集名称','创建人','用例数','关联工作任务'/*,'关联需求'*/,' 操作'],
        colModel : [{name:'id',index:'id',hidden:true},
        	{
        	 name : "testSetNumber",
             index : "testSetNumber",
             searchoptions:{sopt:['cn']},
             /*formatter : function(value, grid, row, index) {
             	var rows = JSON.stringify(row).replace(/"/g, '&quot;');
             	if(testTaskType$ != 3){
             		return '<a class="a_style" href="#" onclick="getTestSetCase( ' + rows + ')">' + value + '</a>';
             	}else{
             		return value;
             	}
             	
             }*/
        },{
            name : "testSetName",
            index : "testSetName",
            searchoptions:{sopt:['cn']},
            formatter : function(value, grid, row, index) {
            	var rows = JSON.stringify(row).replace(/"/g, '&quot;');
            	if(testTaskType$ != 3){
            		return '<a class="a_style" href="#" onclick="getTestSetCase( ' + rows + ')">' + value + '</a>';
            	}else{
            		return value;
            	}
            	
            }
        },{
            name : "createBy",
            index : "createBy",
            width : 50,
            searchoptions:{sopt:['cn']},
        },{
        	searchoptions:{sopt:['cn']},
        	width : 50,
            name : "caseNum",
            index : "caseNum"
        },{
            name : "testTaskName",
            index : "testTaskName",
						width : 100,
            searchoptions:{sopt:['cn']},
        },/*{
            name : "requirementCode",
            index : "requirementCode", 
            width : 70,
            searchoptions:{sopt:['cn']},
        },*/{
            name : "opt",
            index : "操作",
            width : 160,
            search: false,
            formatter : function(value, grid, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                var opt = [];
                var a = '<a  href="javascript:void(0);" class="edit-opt" onclick="edit(' + rows + ')">编辑</a>';
                
                var b = '<a  href="javascript:void(0);" class="edit-opt" onclick="assignPerson(' + row.id+','+row.excuteRound + ')">分派执行人</a>';
                
                var d = '<a  href="javascript:void(0);" class="edit-opt" onclick="copyTestSet(' + rows + ')">复制</a>';
                
                var c = '<a  href="javascript:void(0);" class="edit-opt" onclick="discard(' + row.id + ',this)">废弃</a>';
                
                var e = '<a  href="javascript:void(0);" class="edit-opt" onclick="beginExecute(' + rows + ')">开始执行</a>';
              //  if(row.permission == 1){
	                if(testTaskType$ == 1){
	                	if(editFlag){
		                	opt.push(a);
		                }
		                if(allocateFlag){
		                	opt.push(b);
		                }
		                if(copyFlag){
		                	opt.push(d);
		                }
		                if(discardFlag && (uid == row.excuteUserId||uid == row.manageUserId)){
		                	opt.push(c);
		                }
		                if(executeFlag){
		                	opt.push(e);
		                }
	                }/*else if(testTaskType$ == 2){
	                	if(uateditFlag){
		                	opt.push(a);
		                }
		                if(uatallocateFlag){
		                	opt.push(b);
		                }
		                if(uatcopyFlag){
		                	opt.push(d);
		                }
		                if(uatdiscardFlag){
		                	opt.push(c);
		                }
		                if(uatexecuteFlag){
		                	opt.push(e);
		                }
	                }else if(testTaskType$ == 3){
	                	if(skilleditFlag){
	                		opt.push(a);
	                	}
	                	if(skilldiscardFlag){
		                	opt.push(c);
		                }
	                }*/
              //  }
                return opt.join(span_);
            }
        },
        ],
        rowNum:10,
        rowTotal: 200,
        rowList : commonTableOpt.rowList,
        rownumWidth: 40,
        pager: '#testSetPager',
        sortname: 'id',
        viewrecords: true, //是否要显示总记录数
        jsonReader: {
            repeatitems: false,
        }, 
        loadComplete :function(data){
        	$("#loading").css('display','none');
        },
        beforeRequest: function () {
            var re_page = $(this).getGridParam('page'); //获取返回的当前页
            var re_total= $(this).getGridParam('lastpage'); //获取总页数
            if( re_total != 0 && re_page > re_total){
                $(this).setGridParam({page:re_total}).trigger("reloadGrid"); //设置页码为1 并重新加载
            }
            $("#loading").css('display', 'block');
        }
    }).trigger("reloadGrid");
}

//展示案例
function getTestSetCase(row){
	layer.open({
		   type: 2, 
		   title: false,
		   closeBtn: 0,
		   move: false,
		   content: '/testManageui/testSet/toTestSetCase' + '?' + 'testSetId=' + row.id,
		   area:["100%","100%"]
	    });  
}
//编辑
function edit(row){
	clearTestSetForm();
	$("#testSetModalLabel").text("编辑测试集");
	$("#testSetId").val(row.id);
	$("#testSetName").val(row.testSetName);
	$("#excuteRound").val(row.excuteRound);
	$("#checkTestTask").val(row.testTaskName);
	$("#checkTestTask").attr('idValue',row.testTaskId);
	$("#testStage").val(row.testStage == 1?"系测":"版测");
	$("#testSetDescription").val(row.remark);
	$("#newTestSetModal").modal('show');
}
//复制测试集
function copyTestSet(row){
	clearTestSetForm();
	$("#checkTestTask").val(row.testTaskName);
	$("#checkTestTask").attr('idValue',row.testTaskId);
	$("#excuteRound").val(row.excuteRound);
	$("#newTestSetModal [name=save]").attr('copy',row.id);
	$("#testStage").val(row.testStage == 1?"系测":"版测");
	$("#testSetModalLabel").text("复制测试集");
	$("#newTestSetModal").modal('show');
}
/* 废弃测试集 */
function discard(id,This){
	$(This).blur();
	layer.confirm('是否废弃该测试集?', {icon: 3, title:'提示'}, function(index){
		$("#loading").css('display','block');
		  // do something
		$.ajax({
	        type: "post",
	        url: "/testManage/testSet/removeTestSet",
	        data: {
	        	id : id,
	        	status : 2
	        },
	        dataType: "json",
	        success: function(data) {
	            if(data.status == 2){
	            	layer.alert('废弃失败，'+data.errorMessage,{icon:2});
	            }else{
	            	layer.alert('废弃成功',{icon:1});
	            	searchInfo();
	            }
	            $("#loading").css('display','none');
	        }
	    });
		  layer.close(index);
		});
}

//开始执行
function beginExecute(row){
	var testTaskName = row.testTaskName;
	var sessionStorage = window.sessionStorage;
	sessionStorage.setItem("testSetFlag","false");
	var id = row.id;
	var testSetName = row.testSetName;
	var curl = testExecutionMenuUrl.split("?")[0] + "?testSetId=" + htmlEncodeJQ(id)+"&testSetName="+htmlEncodeJQ(testSetName) +"&testTaskName="+htmlEncodeJQ(testTaskName);
	window.parent.toPageAndValue(testExecutionMenuName, testExecutionMenuId, curl);
	
}
//分派
function assignPerson(id,excuteRound) {
	$("#loading").css('display','block');
	if(id != undefined){
		$("#taskTestId").val(id);
	}else{
		var testSetId = $("#taskTestId").val();
	}
	$("#assExcuteRound").val(excuteRound);
	$.ajax({
    	url : "/testManage/testSet/getExcuteUserByRound",
	    method:"post",
		data:{ 
			testSetId: id, 
			excuteRound:excuteRound 
		},
	    success:function(data){  
	    	$(".assignTableGroup").empty();
	    	$( ".assignModalTit" ).html( data.testSet.testSetNumber+" "+data.testSet.testSetName );
	    	$(".assignTable").bootstrapTable('destroy');  
	    	for(var i=0;i<data.excuteRoundList.length;i++){
	    		var str='';
	    		str+=/*'<div class="oneAssign"><div class="assignNo"> 第'+(i+1)+'轮</div>'*/ 
	    		'<div class="assignBtnGroup"><button type="button" onclick="addExecuteUserShow('+(i+1)+')" class="btn btn-primary">增加执行人</button>'+
	    		'<button type="button" class="btn btn-default" onclick="removeManyExecuteUser('+i+','+excuteRound+')">移除执行人</button></div>'+
	    		'<div class="assignTableDiv">'+
	    		'<table class="assignTable" id="assignTable'+i+'"></table></div></div>'; 
	    		$(".assignTableGroup").append( str ); 
	    		$("#assignTable"+i).bootstrapTable({
	    	        method:"post",
	    	        data: data.excuteRoundList[i],
	    	        queryParamsType:"",
	    	        contentType : 'application/x-www-form-urlencoded; charset=UTF-8', 
	    	        queryParams : function(params) {

	    	        },
	    	        columns : [{
	    	            checkbox : true,
	    	            width : "30px"
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
	    	            title : "账号",
	    	            align : 'center'
	    	        },{
	    	            field : "companyName",
	    	            title : "所属公司",
	    	            align : 'center'
	    	        },{
	    	            title : "操作",
	    	            align : 'center',
	    	            formatter : function(value, grid, rows,
								state) {
							var result = "";
							result += '<a class="a_style" href="javascript:;" onclick="removeExecute('
									+ grid.id + ','+excuteRound+')">移除</a>';
							return result;
						}
	    	        }],
	    	        onLoadSuccess:function(){
	    	        	
	    	        },
	    	        onLoadError : function() {

	    	        }
	    	    });   	
	    	}   
	    	$("#loading").css('display','none');
	    	$("#assignPersonModal").modal("show"); 
	    },
	    error:function(){
		    
	    }
	})          
}

/* 移除执行人 */
function removeExecute(id,round,This){
	//$(This).blur();
	layer.confirm("是否移除该执行人?",{icon : 3},function(){
		$("#loading").css('display','block');
		$.ajax({
			url : "/testManage/testSet/removeExcuteUser",
		    method:"post",
			data:{ 
				id : id
			},
		    success:function(data){   
		    	if(data.status == 2){
		    		layer.alert("移除失败",{icon : 2});
		    	}else{
		    		layer.alert("移除成功",{icon : 1});
		    		var testSetId = $("#taskTestId").val();
		    		var round1 = $("#assExcuteRound").val();
		    		assignPerson(testSetId,round1);
			    	$("#loading").css('display','none');
		    	}
		    },
		    error:function(){
			    
		    }
		}) 
	})
}
/* 移除多个执行人 */
function removeManyExecuteUser(round,This){
	//$(This).blur();
	var selectContents = $("#assignTable"+round).bootstrapTable('getSelections');
	var ids = [];
	$.each(selectContents,function(index,value){
		ids.push(value.id);
	})
	if(ids.length == 0){
		layer.alert("请选择一行记录",{icon:0});
		return;
	}
	layer.confirm("是否移除选中执行人?",{icon : 3},function(){
		$("#loading").css('display','block');
		$.ajax({
			url : "/testManage/testSet/removeManyExcuteUser",
		    method:"post",
			data:{ 
				idStr : JSON.stringify(ids)
			},
		    success:function(data){   
		    	if(data.status == 2){
		    		layer.alert("移除失败",{icon : 2});
		    	}else{
		    		layer.alert("移除成功",{icon : 1});
		    		var testSetId = $("#taskTestId").val();
		    		var round1 = $("#assExcuteRound").val();
		    		assignPerson(testSetId,round1);
			    	$("#loading").css('display','none');
		    	}
		    },
		    error:function(){
			    
		    }
		}) 
	});
	
}

// 防止jqgrid变成一条线
function jqgridAutoWidth(){
	$("body",window.parent.document).width( $("body",window.parent.document).width()-1);
	setTimeout(function(){
		$("body",window.parent.document).width( $("body",window.parent.document).width()+1);
	},500);
}

//条件搜索
function searchInfo(){ 
	var testSetNumber = $.trim($("#searchTestSetCode").val());
	var testSetName = $.trim($("#searchTestSetName").val());
	var createBys = $("#userId").val();
	var testTaskIds = $("#workTaskId").val();
    $("#loading").css('display','block');
   
    $("#testSetList").jqGrid('setGridParam',{
        postData : {
        	testSetNumber:testSetNumber,
        	testSetName:testSetName,
        	createBys:createBys,
        	testTaskIds:testTaskIds
        },
        page:1,
//        beforeRequest : function(){
//        	var dat=$("#caseList").jqGrid('getGridParam', "postData");
//        	console.log( dat );
//        },
        loadComplete :function(){
            $("#loading").css('display','none');
        }
    }).trigger("reloadGrid"); //重新载入
}
//新增
function submitTestSet(This){
	$("#newTestSetForm").data('bootstrapValidator').destroy(); 
	formValidator();
	$('#newTestSetForm').data('bootstrapValidator').validate();
	if(!$('#newTestSetForm').data('bootstrapValidator').isValid()){
		return ;
	}
	var testSetId = $("#testSetId").val();
	var taskId = $("#checkTestTask").attr("idValue");
	var testSetName = $("#testSetName").val();
	$.ajax({
		url : "/testManage/testSet/addTestSet",
	    method:"post",
		data:{ 
			id: testSetId,
			testTaskId: taskId ,  
			testSetName: testSetName,
			excuteRound : /*$.trim($("#excuteRound").val())*/ 1,//只有一个轮次了
			//remark: $("#testSetDescription").val(),
			copy: $(This).attr("copy")
		},
	    success:function(data){
	    	var addOrUpdate = testSetId == ""?"新增":"修改";
	    	if(data.status == 2){
	    		layer.alert(addOrUpdate+"测试集失败",{icon : 2});
	    	}else{
	    		layer.alert(addOrUpdate+"测试集成功",{icon : 1});
	    		$("#newTestSetModal").modal('hide');
	    		searchInfo();
	    	}
	    },
	    error:function(){
		    
	    }
	}) 
}

//重置条件
function clearSearch(){
	$("#searchReqNumber").val("");
	$("#searchReqName").val("");
	$("#searchBatchNumber").val("");
	$("#searchBatchName").val("");
	$("#searchTestSetName").val("");
	$("#searchTestSetCode").val("");
	$("#workTaskId").val("");
	$("#workTaskName").val("");
	$("#createBy").val("");
	$("#userId").val("");
	$(".color1 .btn_clear").css("display","none");
}

function formValidator(){
	$('#newTestSetForm').bootstrapValidator({
    　　　　message: 'This value is not valid',
            　feedbackIcons: {
                　　　　　　　　valid: 'glyphicon glyphicon-ok',
                　　　　　　　　invalid: 'glyphicon glyphicon-remove',
                　　　　　　　　validating: 'glyphicon glyphicon-refresh'
            　　　　　　　　   },
            fields: {
            	testSetName: {
                    validators: {
                        notEmpty: {
                            message: '测试集名称不能为空'
                        },
    					stringLength: {
    						min: 2,
    						max: 100,
    						message: '长度必须在2-100之间'
    					}
                    }
                },
                checkTestTask: {
                    validators: {
                        notEmpty: {
                            message: '测试工作任务不能为空'
                        }
                    }
                },
               /* excuteRound:{
                	validators: {
                        notEmpty: {
                            message: '测试工作任务不能为空'
                        }
                    }
                }*/
            }
        });
} 
