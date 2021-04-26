/**
*@author liushan
*@Description 貌似是没用到的方法 ，怕因为影响未删除，正文请在testExrcutionManagement.js编写
*@Date 2020/4/9
*@Param 
*@return 
**/

var typeName="";

/*-----------------工作任务弹框--------------*/
function EtestTaskShow() {
	$("#loading").css('display', 'block');
	$("#testTaskTable").bootstrapTable('destroy');
	$("#testTaskTable")
			.bootstrapTable(
					{
						url : "/testManage/modal/getAllTestTask",
						method : "post",
						contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
						queryParamsType : "",
						pagination : true,
						sidePagination : "server",
						pageNumber : 1,
						pageSize : 10,
						pageList : [ 10, 25, 50, 100 ],
						singleSelect : true,// 单选
						queryParams : function(params) {
							var param = {
								pageNumber : params.pageNumber,
								pageSize : params.pageSize,
								testTaskCode : $.trim($("#taskCode").val()),
								testTaskName : $.trim($("#taskName").val()),
								testTaskStatus : $.trim($("#taskState").find(
										"option:selected").val())
							};
							return param;
						},
						columns : [
								{
									checkbox : true,
									width : "30px"
								},
								{
									field : "id",
									title : "id",
									visible : false,
									align : 'center'
								},
								{
									field : "testTaskCode",
									title : "任务编号",
									align : 'center'
								},
								{
									field : "testTaskName",
									title : "任务名称",
									align : 'center'
								},
								{
									field : "testTaskStatus",
									title : "状态",
									align : 'center',
									formatter : function(value, row, index) {
										var className = "doing";
										var taskStateList = $("#taskState")
												.find("option");
										for (var i = 0, len = taskStateList.length; i < len; i++) {
											if (row.testTaskStatus == taskStateList[i].value) {
												className += row.testTaskStatus;
												return "<span>"
														+ taskStateList[i].innerHTML
														+ "</span>";
											}
										}
									}
								}, {
									field : "systemId",
									title : "systemId",
									visible : false,
									align : 'center'
								}, {
									field : "systemName",
									title : "systemName",
									visible : false,
									align : 'center'
								} ],
						onLoadSuccess : function() {
							$("#loading").css('display', 'none');
						},
						onLoadError : function() {

						}
					});
}

// 工作确认
function selectTask(typeName) {
	if(typeName=="search"){
		var rowData = $("#testTaskTable").bootstrapTable('getSelections')[0];
		if (typeof (rowData) == 'undefined') {
			layer.alert("请选择一条数据", {
				icon : 2,
				title : "提示信息"
			})
		} else {
			$("#editTestTaskName").val(rowData.testTaskName);
			$("#editTestTaskName").attr( 'idValue',rowData.id );//工作任务ID
			$("#editSystemName").attr( 'idValue',rowData.systemId );//涉及系统
	    	$("#editSystemName").val( rowData.systemName );
			$("#testTaskModal").modal("hide");
		}
	}else if(typeName=="defect"){
		var rowData = $("#testTaskTable").bootstrapTable('getSelections')[0];
		if (typeof (rowData) == 'undefined') {
			layer.alert("请选择一条数据", {
				icon : 2,
				title : "提示信息"
			})
		} else {
			$("#testTaskId").val(rowData.id);
            $("#testTaskName").val(rowData.testTaskName);
            $("#systemId").val(rowData.systemId);
            $("#system_Name").val(rowData.systemName);
            $("#new_requirementCode").val(rowData.requirementCode);
            $("#new_commissioningWindowId").val(rowData.commissioningWindowId);
            $("#testTaskModal").modal("hide");
		}
	}
	 $('.selectpicker').selectpicker('refresh');
}
// 清除工作任务搜索
function cleanTextTask() {
	$("#taskCode").val("");
	$("#taskName").val("");
	$("#taskState").selectpicker('val', '');
}
// 显示工作任务
function shouTestTask(date) {
	if(date=="search"){
		cleanTextTask();
		EtestTaskShow();
		typeName="search"
		$("#testTaskModal").modal("show");	
	}else if(date=="defect"){
		cleanTextTask();
		EtestTaskShow();
		typeName="defect"
		$("#testTaskModal").modal("show");
	}

}



function getExcelByExcuteRound(testSetId,excuteRound){
	window.location.href="/testManage/testExecution/getExcelByExcuteRound?testSetId="+testSetId+"&excuteRound="+excuteRound;
}

function oponCuteRound( This ){
    $("#loading").css('display','block');
    if( $(This).parent().parent().children(".testContent").hasClass("hideAll") ){
        $(This).css({"background-image":"url('../static/images/testSet/hide.png')","backgroundPosition":"1px 1px"});
        $(This).parent().parent().children(".testContent").removeClass("hideAll");

    }else{
        $(This).css({"background-image":"url('../static/images/testSet/show.png')","backgroundPosition":"0 1px"});
        $(This).parent().parent().children(".testContent").addClass("hideAll");
    }
    $("#loading").css('display','none');
}

function oponCuteRound2(This){
    $("#loading").css('display','block');
    $( This ).parent().parent().parent().children(".testContent2").empty();
    if( $(This).parent().parent().parent().children(".testContent2").hasClass("hideAll") ){
        /*if( $(This).parent().parent().parent().children(".testContent2").attr("val")==='false' ){
            $(This).parent().parent().parent().children(".testContent2").attr("val","true"); */
        $.ajax({
            url : " /testManage/testSet/getTestSetCaseByTestSetId",
            method:"post",
            data:{
                page: 1,
                pageSize: casePageSize,
                testSetId: $(This).parent().parent().parent().parent().parent().attr("val"),/*$(This).parent().parent().parent().parent().parent().attr("val")*/
                executeRound:  $(This).attr("val"),
                executeResult : $("#executeResult").val(),
            },
            success:function(data){
                $(This).parent().parent().parent().children(".testContent2").attr( "total", data.total );
                for( var i=0;i<data.rows.length;i++ ){
                    var obj = JSON.stringify( data.rows[i] ).replace(/"/g, '&quot;');
                    var str='';
                    var casebg='';
                    if( data.rows[i].caseExecuteResult==2 || data.rows[i].caseExecuteResult==3 ){
                        casebg='casebg2';
                    }else if( data.rows[i].caseExecuteResult==4 ){
                        casebg='casebg4';
                    }else if( data.rows[i].caseExecuteResult==5 ){
                        casebg='casebg5';
                    }else{
                        casebg='casebg1';
                    }
                    var edit="";
                    if(testCaseStepEdit==true){
                        edit='<div class="btn-group place_right placeBottom"><span type="button" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">…</span>'+
                            '<ul class="dropdown-menu"><li><a onclick="editCaseBtn('+ data.rows[i].caseId +')">编辑</a></li></ul>'+
                            '</div>'
                    }

                    str+='<div class="oneCase">'+
                        '<div class="caseTitle '+ casebg +'">'+
                        '<div class="caseTitleLeft" onclick="getAllExecuteCases(this,'+obj+')">'+  data.rows[i].caseNumber+' '+data.rows[i].caseName+'</div>'+edit+''+
                        '</div><div class="caseBody"></div></div>';
                    $(This).parent().parent().parent().children(".testContent2").append( str );
                }

            },
            error:function(XMLHttpRequest, textStatus, errorThrown){
                layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
            }
        })
        /*} */
        $(This).css({"background-image":"url('../static/images/testSet/hide.png')","backgroundPosition":"1px 1px"});
        $(This).parent().parent().parent().children(".testContent2").removeClass("hideAll");

    }else{
        $(This).css({"background-image":"url('../static/images/testSet/show.png')","backgroundPosition":"0 1px"});
        $(This).parent().parent().parent().children(".testContent2").addClass("hideAll");
    }
    $("#loading").css('display','none');
}

function batchExecutivePage(){
    $(".right_blank").css("display","none");
    $(".right").css("display","block");
    $("#exeExecuteState").selectpicker('val', '');
    $( "#executeHiddenID" ).val("");
    $( "#exeExcuteRound" ).val( "" );
    $( "#exeTitleName" ).html( "" );
    $( "#executeHiddenID" ).val( testSet.id );
    $( "#exeExcuteRound" ).val( 1 );
    $( "#exeTitleName" ).html( testSet.testSetName );
    $( "#excuteRound" ).val( "" );
    $( "#excuteRound" ).val(1 );
    $( ".right_div" ).css( "display","none" ); //显示右边的案例div
    $(".batchExecutivePage").css("display","block");
    testExecution.right_Opt.executionTable.initExecutionTable();
}

function getCaseExecuteResult(testSetId,caseNumber){
    $.ajax({
        url : "/testManage/testExecution/getCaseExecuteResult",
        method:"post",
        async:false,
        data:{
            'testSetId':testSetId,
            'caseNumber':caseNumber
        },
        success:function(data){
            executeResult = data.executeResult;
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    })
}

function getCaseExecute(testSetId,caseNumber){
    $.ajax({
        url : "/testManage/testExecution/getCaseExecute",
        method:"post",
        async:false,
        data:{
            'testSetId':testSetId,
            'caseNumber':caseNumber
        },
        success:function(data){
            $("#checkExcuteRemark2").empty();
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                if(data.data != null){
                    for( var i=0;i<data.data.length;i++ ){
                        str = "<div class='oneHistory'><span class='fontWeihgt'>执行人:</span>"+data.data[i].executeUserName+"  "+data.data[i].createDate+"<br>"
                            +"<span class='fontWeihgt'>执行结果:</span>"+data.data[i].executeResult+"<br>"
                            +"<span class='fontWeihgt'>备注:</span>"+data.data[i].excuteRemark+"<br></div>"
                        $("#checkExcuteRemark2").append(str);
                    }
                }
            }

        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    })
}
