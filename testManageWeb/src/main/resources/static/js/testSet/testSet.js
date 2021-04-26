$(document).ready(function () { 
	banEnterSearch( );
	init();
    getAllTestTaskByTestSet();  
    formValidator();
    
    $('.left .readonly').parent().css("position","relative");
    $('.left .readonly').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
});  
var that;
var sessionStorage = window.sessionStorage;
function assignPerson() {
	$("#loading").css('display','block');
	$.ajax({
    	url : "/testManage/testSet/getExcuteUserByRound",
	    method:"post",
		data:{ 
			testSetId: $("#taskTestId").val() ,  
			excuteRound: $("#showExcuteRound").text() , 
		},
	    success:function(data){  
	    	$(".assignTableGroup").empty();
	    	$( ".assignModalTit" ).html( data.testSet.testSetNumber+" "+data.testSet.testSetName );
	    	$(".assignTable").bootstrapTable('destroy');  
	    	for(var i=0;i<data.excuteRoundList.length;i++){
	    		var str='';
	    		str+='<div class="oneAssign"><div class="assignNo"> 第'+(i+1)+'轮</div>'+
	    		'<div class="assignBtnGroup"><button type="button" onclick="addExecuteUserShow('+(i+1)+')" class="btn btn-primary">增加执行人</button>'+
	    		'<button type="button" class="btn btn-default" onclick="removeManyExecuteUser('+i+')">移除执行人</button></div>'+
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
									+ grid.id + ')">移除</a>';
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

//清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}

function addCaseShow(){
	createAddTable();
	getRelateSystem();
	$("#newTestCaseModal").modal('show');
}
//获取关联系统
function getRelateSystem(){
	$("#loading").css('display','block');
	$.ajax({
        type: "post",
        url: "/testManage/testSet/getRelateSystem",
        data: {testSetId : $("#taskTestId").val()},
        dataType: "json",
        success: function(data) {
            $("#systemId").val(data.row.id);
            $("#systemName").val(data.row.systemName);
            $("#loading").css('display','none');
        }
    });
}

function init(){
	$("#loading").css('display','block');
	 $("#caseTable").jqGrid("clearGridData");
     $("#caseTable").jqGrid({
        url:'/testManage/testSet/getTestSetCase',
        datatype: 'json',
        mtype:"POST",
        height: 'auto', 
        autowidth: true,
        multiselect : true, 
        colNames:['id','案例编号','案例标题','正面/反面','涉及系统','创建人'],
        colModel:[
            {name:'id',index:'id',hidden:true},
            {name:'caseNumber',index:' 案例编号',
            	formatter : function(value, grid, row, index) {
                    var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                    return '<a class="a_style" onclick="showCase('+rows+')">'+value+'</a>'
            	}
            },
            {name:'caseName',index:' 案例标题',
            	formatter : function(value, grid, rows) {
            		return htmlEncodeJQ(value);
            	}
            },
            {name:'caseTypeName',index:' 正面/反面'},
            {name:'systemName',index:' 涉及系统'},
            {name:'createBy',index:' 创建人'}
        ],
        rowNum:10,
        rowTotal: 200,
        rowList : [10,20,30],
        rownumWidth: 40,
        pager: '#pager2',
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true //是否要显示总记录数
    }).trigger('reloadGrid'); 
     $("#loading").css('display','none');
}
function getAllTestTaskByTestSet(){ 
	var userIds = $("#userId").val();
	var flag = sessionStorage.getItem("testSetFlag");
	var arr = [];
	var workIds = '';
	if(workTaskId!='$workTaskId' && workTaskId!='' && flag == "false"){
		var workTaskIds = $("#workTaskId").val();
		if(workTaskIds!=''){
			arr = workTaskIds.split(",");
		}else{
			arr=[];
		}
		arr.push(workTaskId);
		workIds = JSON.stringify(arr);
	}else{
		workTaskId='';
		var workTaskIds = $("#workTaskId").val();
		if(workTaskIds!=''){
			arr = workTaskIds.split(",");
		}else{
			arr=[];
		}
		workIds = JSON.stringify(arr);
	}
	
	var taskId = "";
	if(flag == "false"){
		var taskId = testTaskId == "$testTaskId"?"":testTaskId; 
	}
	$("#loading").css('display','block');
	$(".testTask").remove();
	var jsonData = {
			nameOrNumber: $("#nameOrNumber").val(),
			createBy: userIds == ""?"[]":userIds,
			testTaskId: workIds == "[]"?"[]":workIds, 
			taskId: taskId
		};
	$.ajax({
		url:"/testManage/testtask/getAllTestTaskByTestSet",
	    method:"post",
		data:jsonData,
	   success:function(data){ 
		   if(data.rows.length == 0){
			   $("#allTestTask").html( '<div class="haveNoFont"><span>暂无测试任务...</span></div>' );
		   }else{
			   $(".haveNoFont").remove();
		   }
		   for( var i=0;i<data.rows.length;i++ ){
			   var defCreateBy='';
			   if( data.rows[i].createBy == null ){
				   defCreateBy="无";
			   }else{
				   defCreateBy=data.rows[i].createBy;
			   }
			   var str='';
			   var date = getDateStr_yyyy_MM_dd( data.rows[i].planStartDate )+' ~ '+getDateStr_yyyy_MM_dd( data.rows[i].planStartDate );
			   if(getDateStr_yyyy_MM_dd( data.rows[i].planStartDate ) == undefined){
				   date = '无';
			   }
			   var img='<img src="../static/images/testSet/task.png" />';
			   var paramData = JSON.stringify(jsonData).replace(/"/g, '&quot;');
			   str+='<div class="testTask"><div class="taskTop" onclick="showTestTask(this,'+paramData+')" val='+data.rows[i].id+' >'+ img +
			   '<span class="taskName">'+data.rows[i].featureCode+'  '+data.rows[i].featureName+'</span></div>'+
			   '<div class="taskBottom"><div class="taskInfo"><span class="taskStatus">进行中</span><br>'+
			   '<div class="rowdiv"><div class="def_col_23"> 计划周期:'+date+'</div>'+
			   '<div class="def_col_13 font_right"> 负责人：'+ defCreateBy +'</div></div></div>'+
			   '<div class="taskContent"  val="false" ></div></div></div>';
			   $("#allTestTask").append( str );
		   }   
		   $("#loading").css('display','none');
		   sessionStorage.setItem("testSetFlag","true");
	   },
	   error:function(){
		    
	   }
	})
}
function showTestTask(This,paramData){
	if( $( This ).parent().children(".taskBottom").css("display")=="none" ){ 
		if( $( This ).parent().children(".taskBottom").children(".taskContent").attr("val")==="true" ){ 
			$( This ).parent().children(".taskBottom").css('display','block');
		}else{
			$("#loading").css('display','block');
			$( This ).parent().children(".taskBottom").children(".taskContent").attr("val","true"); 
			paramData.featureId = $( This ).attr("val");
			$.ajax({
				url:"/testManage/worktask/getWorkTaskByTestTaskId",
			    method:"post",
				data:paramData,
			    success:function(data){  
			    	paramData = JSON.stringify(paramData).replace(/"/g, '&quot;');
			    	for(var i=0;i<data.rows.length;i++ ){
			    		var str='';
			    		var noBorder=''
		    			if( i==data.rows.length-1 ){
		    				noBorder='noBorder';
			    		}
			    		str+='<div class="testJob"><div class="testName" val='+ data.rows[i].id +' onclick="getAllTestSet(this,'+paramData+')">'+data.rows[i].testTaskName+'</div>'+
			    		'<div class="testContent hideAll '+noBorder+'" val="false"></div></div>'; 
			    		$( This ).parent().children(".taskBottom").children(".taskContent").append( str ); 
			    	} 
				    $( This ).parent().children(".taskBottom").css('display','block');
				    $("#loading").css('display','none');
			    },
			    error:function(){
				    
			    }
			}) 
		} 
	}else{
		$( This ).parent().children(".taskBottom").css('display','none');
	}
}  
function getAllTestSet( This,paramData ){   
	if( $(This).parent().children(".testContent").attr("val")==="false" ){
		$("#loading").css('display','block');
		paramData.testTaskId = $(This).attr("val");
		$.ajax({
			url:"/testManage/testSet/getAllTestSet3",
		    method:"post",
			data:paramData,
		   success:function(data){ 
			    $(This).parent().children(".testContent").attr("val","true");
			    for( var i=0;i<data.rows.length;i++ ){
			    	var str="";
			    	var noBorder="";
			    	if( i==data.rows.length-1 ){
			    		noBorder="noBorder";
			    	}
			    	str+='<div class="testContent_border '+ noBorder +'" onclick="addTestSet( this )" val='+ data.rows[i].id +'><div class="testSuit">'+
			    	'<div class="testSuitName">'+ data.rows[i].testSetNumber +' <span class="testSetName">'+ data.rows[i].testSetName +'</span></div>'+
			    	'<div class="testSuitContent"><div class="def_col_16">创建人:<span class="createBy" val='+ data.rows[i].createBy +' >'+ data.rows[i].createBy +'</span></div>'+  
			    	'<div class="def_col_11">案例数:<span class="caseNum" val='+ data.rows[i].caseNum +'>'+ data.rows[i].caseNum +'</span></div>'+ 
			    	'<div class="def_col_9">轮次:<span class="excuteRound" val='+ data.rows[i].excuteRound +'>'+ data.rows[i].excuteRound +'</span></div>'+
			    	'<input type="hidden" class="relationTestTask" name='+ data.rows[i].testTaskName +' val='+ data.rows[i].testTaskId +' /></div></div></div>';
			    	$(This).parent().children(".testContent").append( str );  
			    }  
			    $("#loading").css('display','none');
		   },
		   error:function(){
			    
		   }
		})  
	}  
	if( $(This).parent().children(".testContent").hasClass("hideAll") ){
		$(This).css({"background-image":"url('../static/images/testSet/hide.png')","backgroundPosition":"1px 1px"});
		$(This).parent().children(".testContent").removeClass("hideAll");
		 
	}else{
		$(This).css({"background-image":"url('../static/images/testSet/show.png')","backgroundPosition":"0 1px"});
		$(This).parent().children(".testContent").addClass("hideAll");
	} 
}
function addTestSet( This,status ){ 
	$(".right_blank").css("display","none");
	$(".right").css("display","block");
	that = This;
	clearTestSet();
	if( status===undefined ){ 
		$( "#taskTestId" ).val( $(This).attr( "val" ) );
		$("#showCaseNum").html( $(This).find(".caseNum").attr( "val" ) );
		$("#showExcuteRound").html( $(This).find(".excuteRound").attr( "val" ) );  
		
		$("#checkTestSetName").val( $(This).find(".testSetName").text() )
		$("#checkExcuteRound").val( $(This).find(".excuteRound").html() ) 
		$("#checkTestTask").attr( "idvalue", $(This).find(".relationTestTask").attr( "val" ) );
		$("#checkTestTask").val( $(This).find(".relationTestTask").attr( "name" ) );
		
		$(".right_div_titRight").css("display",'inherit');
		$(".caseTableDiv").css("display",'block'); 
		jqgridAutoWidth();
		initCaseTable( $(This).attr( "val" ) );
		
	}else if( status==='false' ){
		$(".right_div_titRight").css("display",'none');
		$(".caseTableDiv").css("display",'none');
		$("#showCaseNum").html( "0" );
	}  
	$(".right").css("display",'block');  
}
/*移除执行人*/
function removeExecute(id){
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
		    		assignPerson();
			    	$("#loading").css('display','none');
		    	}
		    },
		    error:function(){
			    
		    }
		}) 
	})
}
/*移除多个执行人*/
function removeManyExecuteUser(round){
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
		    		assignPerson();
			    	$("#loading").css('display','none');
		    	}
		    },
		    error:function(){
			    
		    }
		}) 
	});
	
}

//防止jqgrid变成一条线
function jqgridAutoWidth(){
	$("body",window.parent.document).width( $("body",window.parent.document).width()-1);
	setTimeout(function(){
		$("body",window.parent.document).width( $("body",window.parent.document).width()+1);
	},500);
}

function commitAddTestSet(){
	$("#testSetForm").data('bootstrapValidator').destroy(); 
	formValidator();
	$('#testSetForm').data('bootstrapValidator').validate();
	if(!$('#testSetForm').data('bootstrapValidator').isValid()){
		return ;
	}
	$("#showExcuteRound").html( $("#checkExcuteRound").val() );
	var testSetId = $("#taskTestId").val();
	$.ajax({
		url : "/testManage/testSet/addTestSet",
	    method:"post",
		data:{ 
			id: testSetId,
			testTaskId: $("#checkTestTask").attr("idValue") ,  
			testSetName: $("#checkTestSetName").val(),
			excuteRound: $("#checkExcuteRound").val(),
		},
	    success:function(data){
	    	var addOrUpdate = testSetId == ""?"新增":"修改";
	    	if(data.status == 2){
	    		layer.alert(addOrUpdate+"测试集失败",{icon : 2});
	    	}else{
	    		layer.alert(addOrUpdate+"测试集成功",{icon : 1});
	    		if(addOrUpdate == "新增"){
	    			initCaseTable( data.id );
	    			getAllTestTaskByTestSet();
	    			$( "#taskTestId" ).val( data.id );
	    		}else{
	    			initCaseTable( testSetId );
	    			$( "#taskTestId" ).val( testSetId );
	    		}
		    	$(that).find('.excuteRound').text($("#checkExcuteRound").val());
	    	}
	    },
	    error:function(){
		    
	    }
	}) 
}
function initCaseTable( id ){ 
	$(".right_div_titRight").css("display",'inherit'); 
	$(".caseTableDiv").css("display",'block'); 
	jqgridAutoWidth();
    $("#caseTable").jqGrid('setGridParam',{ 
		postData : {  
			"testSetId" : id, 
		},  
		page:1,
		loadComplete :function(){
		 	 $("#showCaseNum").text($("#caseTable").jqGrid('getGridParam', 'records'));
		 	 $(that).find('.caseNum').text($("#caseTable").jqGrid('getGridParam', 'records'));
	    }
	}).trigger("reloadGrid"); //重新载入   
}

function importExcel(){
	$("#importModal").modal("show");
}

/*导入*/
function upload(){
	var formData = new FormData();
	formData.append("file", document.getElementById("upfile").files[0]);
	formData.append("testSetId",$( "#taskTestId" ).val());
	if(document.getElementById("upfile").files[0] == undefined){
		layer.alert("请选择文件",{icon : 0});
		return ;
	}
	$("#loading").css('display','block');
	$.ajax({
        url: "/zuul/testManage/testSet/importCase",
        type: "POST",
        data: formData,
        /**
        *必须false才会自动加上正确的Content-Type
        */
        contentType: false,
        /**
        * 必须false才会避开jQuery对 formdata 的默认处理
        * XMLHttpRequest会对 formdata 进行正确的处理
        */
        processData: false,
        success: function (data) {
           if(data.status == 2){
        	   layer.alert("导入失败，原因:"+data.errorMessage,{icon:2});
           }else{
        	   layer.alert("导入成功",{icon:1});
        	   initCaseTable( $( "#taskTestId" ).val() );
        	   $("#importModal").modal("hide");
           }
           $("#loading").css('display','none');
        },
        error: function () {
            alert("上传失败！");
        }
    });
}


/*移除多条关联记录*/
function removeManyCase(){
	var testSetId = $("#taskTestId").val();
	var ids=$('#caseTable').jqGrid('getGridParam','selarrrow');
	if(ids.length == 0){
		layer.alert("请选择一行记录",{icon:0});
		return;
	}
	layer.confirm('是否移除选中的案例?', {icon: 3, title:'提示'}, function(index){
		$("#loading").css('display','block');
		  //do something
		$.ajax({
	        type: "post",
	        url: "/testManage/testSet/removeManyTestSetCase",
	        data: {
	        	ids : JSON.stringify(ids)
	        },
	        dataType: "json",
	        success: function(data) {
	            if(data.status == 2){
	            	layer.alert('移除失败',{icon:2});
	            }else{
	            	layer.alert('移除成功',{icon:1});
	            	initCaseTable( $( "#taskTestId" ).val() );
	            }
	            $("#loading").css('display','none');
	        }
	    });
		  layer.close(index);
		});
}

/*废弃测试集*/
function discard(){
	$.ajax({
        type: "post",
        url: "/testManage/testExecution/seeTestCaseExecute",
        data: {
        	testSetCaseExecuteId : $("#taskTestId").val()
        },
        dataType: "json",
        success: function(data) {
            if(data.status == 2){
            	layer.alert('查询异常',{icon:2});
            }else{
            	var message = data.size?"该测试已存在执行记录，是否确认废弃?":"是否废弃该测试集?";
            	layer.confirm(message, {icon: 3, title:'提示'}, function(index){
            		$("#loading").css('display','block');
            		  //do something
            		$.ajax({
            	        type: "post",
            	        url: "/testManage/testSet/removeTestSet",
            	        data: {
            	        	id : $("#taskTestId").val(),
            	        	status : 2
            	        },
            	        dataType: "json",
            	        success: function(data) {
            	            if(data.status == 2){
            	            	layer.alert('废弃失败',{icon:2});
            	            }else{
            	            	layer.alert('废弃成功',{icon:1},function(index){
            	            		location.reload();
            	            	});
            	            }
            	            $("#loading").css('display','none');
            	        }
            	    });
            		  layer.close(index);
            		});
            }
        }
    });
	
}

function showCase(row){
	$("#showSystemName").text(row.systemName);
	$("#showCaseType").text(row.caseTypeName);
	$("#showCaseName").text(row.caseName);
	$("#showCasePrecondition").text(row.casePrecondition);
    $("#expectResult").text(row.expectResult);
    $("#inputData").text(row.inputData);
    $("#testPoint").text(row.testPoint);
    $("#moduleName").text(row.moduleName);
    $("#businessType").text(row.businessType);

	$("#loading").css('display','block');
	$.ajax({
        type: "post",
        url: "/testManage/testSet/showCase",
        data: {
        	caseId : row.id,
        },
        dataType: "json",
        success: function(data) {
            if(data.status == 2){
            	layer.alert('查询失败',{icon:2});
            }else{
            	createStepTable(data.rows);
            }
            $("#showTestCaseModal").modal('show');
            $("#loading").css('display','none');
        }
    });
}

function createStepTable(data){
	$("#showCaseSteps").bootstrapTable('destroy');
    $("#showCaseSteps").bootstrapTable({
        queryParamsType:"",
        data:data,
        columns : [
        {
            field : "stepOrder",
            title : "步骤",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
        	field : "stepDescription",
            title : "步骤描述",
            align : 'center'
        },{
            field : "stepExpectedResult",
            title : "预期结果",
            align : 'center'
        }],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
}

function createAddTable(){
	var data = [];
	$("#editCaseSteps").bootstrapTable('destroy');
    $("#editCaseSteps").bootstrapTable({
        queryParamsType:"",
        data:data,
        columns : [
        {
            field : "stepOrder",
            title : "步骤",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
        	field : "stepDescription",
            title : "步骤描述",
            align : 'center',
            formatter : function (value, row, index) {
                return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock"></textarea></div>';
            }
        },{
            field : "stepExpectedResult",
            title : "预期结果",
            align : 'center',
            formatter : function (value, row, index) {
                return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock"></textarea></div>';
            }
        },{
        	field : "操作",
            title : "操作",
            align : 'center',
            width : 210,
            formatter : function (value, row, index) { 
                var str='<a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | '+
                        '<a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | '+
                        '<a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | '+
                        '<a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a>';
                return str;
            }
        }],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
    if( data.length==0 ){
    	$("#editCaseSteps>tbody").empty();
    }
}

function down(This){
    if( $(This).hasClass("fa-angle-double-down") ){
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
        $(This).parents('.allInfo').children(".connect_div").slideDown(100);
    }else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
        $(This).parents('.allInfo').children(".connect_div").slideUp(100);
    }
}
/*取消增加测试集*/
function cancelTestSet(){
	$(".right").css("display",'none');
	$(".right_blank").css("display","block");
}

function clearTestSet(){
	$("#checkTestSetName").val("");
	$("#checkExcuteRound").val("");
	$("#checkTestTask").val("");
	$("#checkTestTask").attr( "idvalue", '' );
	$("#taskTestId").val("");
}

//案例步骤表格四种操作
function addSteps(){
	var i= $("#editCaseSteps>tbody>tr").length + 1;
	var str='<tr><td class="hideCaseID" style="text-align: center;line-height:84px;"></td><td class="stepOrder" style="text-align: center; width: 50px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px;line-height:84px; "><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
	$("#editCaseSteps>tbody").append( str );
	changeStepOrder();
}
function addEditTableRow( This ){ 
	var i= $("#editCaseSteps>tbody>tr").length + 1;
	var str='<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px; line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
	$( This ).parent().parent().after( str ); 
	changeStepOrder()
}
function delEditTableRow( This ) { 
    $( This ).parent().parent().remove();
    changeStepOrder();
}
function upTableRow( This ){
    var $tr = $(This).parents("tr");
    if ($tr.index() == 0){
        alert("首行数据不可上移");
    }else{
    	$tr.fadeOut(200).fadeIn(100).prev().fadeOut(100).fadeIn(100); 
        $tr.prev().before($tr);
        changeStepOrder();
    }
} 
function downTableRow( This ){
    var $tr = $(This).parents("tr");
    if ( $tr.index() == $( "#editCaseSteps>tbody>tr" ).length - 1) {
        alert("最后一条数据不可下移");
    }else {
        $tr.fadeOut(200).fadeIn(100).next().fadeOut(100).fadeIn(100); 
        $tr.next().after($tr);
        changeStepOrder();
    }
}
function addStep_btn(){
	var i= $("#editCaseSteps>tbody>tr").length + 1;
	var str='<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px;line-height:84px; "><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
	$("#editCaseSteps").append(str);
}
//动态改变表格步骤值
function changeStepOrder(){ 
	for( var i=0;i<$("#editCaseSteps>tbody>tr").length ;i++){ 
		$( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".stepOrder" ).text( ( i + 1) );   
	}
}

//保存编辑页面信息
function submitTestCase(){
	var testCaseStep = [];
	$("#newTestCaseForm").data('bootstrapValidator').destroy(); 
	formValidator();
	$('#newTestCaseForm').data('bootstrapValidator').validate();
	if(!$('#newTestCaseForm').data('bootstrapValidator').isValid()){
		return ;
	}
	for( var i=0;i<$( "#editCaseSteps>tbody>tr" ).length;i++ ){
		var obj={}
		obj.stepOrder=$( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".stepOrder" ).text();
		obj.stepDescription=$( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(0).val();
		if(obj.stepDescription == ""){
			layer.alert("第"+(i+1)+"行步骤描述为空",{icon : 0});
			return ;
		}
		obj.stepExpectedResult=$( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(1).val(); 
		if(obj.stepExpectedResult == ""){
			layer.alert("第"+(i+1)+"行预期结果为空",{icon : 0});
			return ;
		}
		testCaseStep.push( obj ); 
	} 
	var data=JSON.stringify({
		'testSetId': $( "#taskTestId" ).val(),
		'systemId': $( "#systemId" ).val(),
		'caseName': $( "#new_caseName" ).val(),
		'caseType':$("#caseType").val(),
		'casePrecondition': $( "#new_precodition" ).val(),
        'expectResult':$('#new_expectResult').val(),
        'inputData':$('#new_inputData').val(),
        'testPoint':$('#new_testPoint').val(),
        'moduleName':$('#new_moduleName').val(),
        'businessType':$('#new_businessType').val()
	});
	$("#loading").css('display','block');
	$.ajax({
		url : "/testManage/testSet/addTestCase",
	    method:"post",
		data:{
			testSetCaseStr : data,
			testSetCaseStepStr : JSON.stringify(testCaseStep)
		},
	    success:function(data){   
	    	 if(data.status == 2){
	    		 layer.alert("添加失败",{icon : 2});
	    	 }else{
	    		 layer.alert("添加成功",{icon : 1});
	    		 $("#newTestCaseModal").modal("hide");
	    		 initCaseTable($( "#taskTestId" ).val());
	    		 clearAddCase();
	    	 }
	    	 $("#loading").css('display','none');
	    },
	    error:function(){
	    	 
	    }
	});  
}

function clearAddCase(){
	$("#newTestCaseModal :text").val("");
	$("#newTestCaseModal textarea").val("");
	$("#newTestCaseForm").data('bootstrapValidator').destroy();
	formValidator();
}

function formValidator(){
	$('#testSetForm').bootstrapValidator({
    　　　　message: 'This value is not valid',
            　feedbackIcons: {
                　　　　　　　　valid: 'glyphicon glyphicon-ok',
                　　　　　　　　invalid: 'glyphicon glyphicon-remove',
                　　　　　　　　validating: 'glyphicon glyphicon-refresh'
            　　　　　　　　   },
            fields: {
            	checkTestSetName: {
                    validators: {
                        notEmpty: {
                            message: '测试集名称不能为空'
                        }
                    }
                },
                checkExcuteRound: {
                    validators: {
                        notEmpty: {
                            message: '执行轮次不能为空'
                        },
                        regexp: {            
 　	                       regexp: /^([1-9][0-9]*)$/,
  	                       message: '轮次必须为大于0'
 　	                    } 
                    }
                },
                checkTestTask: {
                    validators: {
                        notEmpty: {
                            message: '工作任务不能为空'
                        }
                    }
                }
            }
        });
	$('#newTestCaseForm').bootstrapValidator({
    　　　　message: 'This value is not valid',
            　feedbackIcons: {
                　　　　　　　　valid: 'glyphicon glyphicon-ok',
                　　　　　　　　invalid: 'glyphicon glyphicon-remove',
                　　　　　　　　validating: 'glyphicon glyphicon-refresh'
            　　　　　　　　   },
            fields: {
            	systemName: {
                    validators: {
                        notEmpty: {
                            message: '涉及系统不能为空'
                        }
                    }
                },
                caseType: {
                    validators: {
                        notEmpty: {
                            message: '案例类型不能为空'
                        }
                    }
                },
                new_caseName: {
                    validators: {
                        notEmpty: {
                            message: '案例名称不能为空'
                        }
                    }
                },
            
                /*new_expectResult: {
                   validators: {
                       notEmpty: {
                           message: '预期结果不能为空'
                       } ,
                       stringLength: {
                           max:300,
                           message: '预期结果长度必须小于500字符'
                       }
                   }
               },
               new_inputData: {
                   validators: {
                       notEmpty: {
                           message: '输入数据不能为空'
                       } ,
                       stringLength: {
                           max:300,
                           message: '输入数据长度必须小于500字符'
                       }
                   }
               },
               new_testPoint: {
                   validators: {
                       notEmpty: {
                           message: '测试项不能为空'
                       } ,
                       stringLength: {
                           max:300,
                           message: '测试项长度必须小于500字符'
                       }
                   }
               },
               new_moduleName: {
                   validators: {
                       notEmpty: {
                           message: '模块不能为空'
                       } ,
                       stringLength: {
                           max:300,
                           message: '模块长度必须小于500字符'
                       }
                   }
               },
               new_businessType: {
                   validators: {
                       notEmpty: {
                           message: '业务类型不能为空'
                       } ,
                       stringLength: {
                           max:300,
                           message: '业务类型长度必须小于500字符'
                       }
                   }
               },*/
            }
        });
} 
	

