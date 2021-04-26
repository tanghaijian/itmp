
/**
 * 注意！！！！
 * 测试集案例中的 导入、移除、归档、导出、复制、排序 功能已经移到 下面这个js 
 * /testManageWeb/src/main/resources/static/js/testExecution/testSetCaseCommonOpt.js
 * 与测试执行案例的功能共用
 * */

var testSet = {
	id:"",
	testCaseId:"",
}
var editReturn = "";
var archivedCaseNumbers = [];
$(function(){
	$('.modal').on('shown.bs.modal', function (e) {//赋值
	     	$('.bv-hidden-submit').remove();
	});
	testExecution.right_Opt.executionTable.initExecutionTable();
	findTestSet();
	testSet.id = testSetId;
	$(".headReturn").click(function(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭   
	});
})


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

//清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}

function addCaseShow(){
	createAddTable();
	getRelateSystem();
}
//获取关联系统
function getRelateSystem(){
	$("#loading").css('display','block');
	$.ajax({
        type: "post",
        url: "/testManage/testSet/getRelateSystem",
        data: {testSetId : testSetId},
        dataType: "json",
        success: function(data) {
            $("#systemId").val(data.row.id);
						$("#systemName").val(data.row.systemName);
						$("#newTestCaseModal").modal('show');
            $("#loading").css('display','none');
        }
    });
}

var testExecution = {
		right_Opt : {
			executionTable : {
				initExecutionTable : function(){
					 $("#loading").css('display','block');
					 $("#executionTable").bootstrapTable('destroy');
					 $("#executionTable").bootstrapTable({  
						url:'/testManage/testSet/getTestSetCaseBootStrap',
						method:"post",
					    queryParamsType:"",
					    pagination : false,
					    sidePagination: "server",
					    uniqueId : "setCaseId",
					    maintainSelected : true,
					    contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
					    pageNumber : 1,
					    pageSize : 10,
					    pageList : [ 5, 10, 15],
					    queryParams : function(params) {
					    	testSetCase_param.param_list = "";
				            var param = {
				                testSetMap :JSON.stringify({
				                    testSetId : testSetId,
				                })
				            };
				            testSetCase_param.param_list = param;
				        return param;
				    },
				    responseHandler:function(res) {
					    //if(res.status == 1){
					        return res.rows;
					   // }
					},
				    columns : [{
				        checkbox : true,
				        width : "30px",
				        
				    },{
				        field : "id",
				        title : "id",
				        visible : false,
				        align : 'center'
				    },{
				        field : "setCaseId",
				        title : "setCaseId",
				        visible : false,
				        align : 'center'
				    },{
				        field : "orderCase",
				        title : "排序",
				        align : 'center',
				        width : "30px",
				    },{
				        field : "caseNumber",
				        title : "案例编号",
				        align : 'center',
				        width : "100px"
				    },{
				        field : "caseName",
				        title : "案例名称",
				        align : 'center',
				        formatter : function (value, row, index) {
				        	 var rows = JSON.stringify(row).replace(/"/g, '&quot;');
				            return'<a  href="javascript:void(0);" class="edit-opt _show_ellipsis" style="width : 300px;display: block;" title="'+ value +'" onclick="showCase('+rows+')">'+value+'</a>';
				        }
				    },{
				    	field : "moduleName",
				    	title : "模块",
				    	align : 'center',
				    	formatter : function (value, row, index) {
				           return'<div class="_show_ellipsis" style="width : 80px;" title="'+value+'">'+value+'</div>';
				        }
				    },{
				    	field : "testPoint",
				    	title : "测试项",
				    	align : 'center',
				    	formatter : function (value, row, index) {
				            return'<div class="_show_ellipsis" style="width : 80px;" title="'+value+'">'+value+'</div>';
				         }
				    },{
				    	field : "casePrecondition",
				    	title : "前置条件",
				    	align : 'center',
				    	formatter : function (value, row, index) {
				            return'<div class="_show_ellipsis" style="width : 80px;" title="'+value+'">'+value+'</div>';
				         }
				    },{
				    	field : "inputData",
				    	title : "输入数据",
				    	align : 'center',
				    	formatter : function (value, row, index) {
				            return'<div class="_show_ellipsis" style="width : 80px;" title="'+value+'">'+value+'</div>';
				         }
				    },{
				    	field : "expectResult",
				    	title : "预期结果",
				    	align : 'center',
				    	formatter : function (value, row, index) {
				            return'<div class="_show_ellipsis" style="width : 80px;" title="'+value+'">'+value+'</div>';
				         }
				    },{
				    	field : "caseExecuteResult",
				    	title : "执行状态",
				    	align : 'center',
				    	width : "100px",
				    	formatter : function (value, row, index) {
				            if( row.caseExecuteResult==undefined ){
				                return '<span class="spanClass1">未执行</span>'
				            }else{
				                for( var j=0;j<caseExecuteStatusList.length;j++ ){
				                    if( row.caseExecuteResult == caseExecuteStatusList[j][0] ){
				                        return  '<span class="spanClass'+row.caseExecuteResult+'">'+caseExecuteStatusList[j][1]+'</span>';
				                    }
				                }
				                return '<span class="spanClass1">未执行</span>'
				            }
				        }
				    },{
				        field : "opt",
				        title : "操作",
				        align : 'center',
				        width : "100px",
				        formatter : function (value, row, index) {
				        	var opt = [];
				            var editTestCaseOpt =  "<a class='edit-opt' href='javascript:void(0);' onclick='toTestCaseEdit("+ row.setCaseId +")'>编辑</a>";
				            opt.push(editTestCaseOpt);
				            opt.join("");
				            return opt;
				        }
				    }],
				    onLoadSuccess:function(data){
				    	$("#loading").css('display','none');
				    	$("#showCaseNum").text(data.length);
				    },
				    onLoadError : function() {
	
				    },
				    onReorderRowsDrag:function (table, row) {
				        moveIndexTable.beforeIndex = $(row).attr("data-index");
				        return false;
				    },
				    onReorderRowsDrop:function(table, row){
				        moveIndexTable.laterIndex = $(row).attr("data-index");
				        if(moveIndexTable.beforeIndex != moveIndexTable.laterIndex){
				            moveIndexTable.id = $(row).attr("data-uniqueid");
				            testSetCase.order.moveIndexTable(moveIndexTable.id,parseInt(moveIndexTable.beforeIndex)+1,parseInt(moveIndexTable.laterIndex)+1);
				        }
				        return false;
				    },
				 });
				}
			}
		}
}


//编辑
function toTestCaseEdit(id){
	testSet.testCaseId = id;
	editReturn = 1;
	testSetCase.edit.editCaseBtn();//此方法在 /testManageWeb/src/main/resources/static/js/testExecution/testSetCaseCommonOpt.js
	$("#editTestCaseModal").modal("show");
}

function findTestSet(){
	$("#loading").css('display','block');
	$.ajax({
		url : "/testManage/testSet/findOneTestSet",
	    method:"post",
		data:{ 
			testSetId : testSetId
		},
	    success:function(data){   
	    	if(data.status == 2){
	    		layer.alert("查询失败",{icon : 2});
	    	}else{
	    		$("#reqFeatureId").val(data.rows.featureID);
	    		$("#reqFeature").val(data.rows.featureName);
	    		$(".right_div_titFont").text(data.rows.testSetName);
	    		$(".executeUserDiv .rowdiv").html('');
	    		if(data.rows.executeUser != undefined){
	    			$.each(data.rows.executeUser.split(','),function(index,value){
	    				$(".executeUserDiv .rowdiv").append('<div class="def_col_6 executeUser">'+value+'</div>');
	    			});
	    		}else{
	    			$(".executeUserDiv .rowdiv").append('<div class="def_col_36 executeUser">无执行人员</div>');
	    		}
	    		$("#loading").css('display','none');
	    	}
	    },
	    error:function(){
		    layer.alert("系统错误",{icon : 2});
	    }
	}) 
}


function showCase(row){
	$("#showSystemName").text(row.systemName);
	$("#showModuleName").text(row.moduleName);
	//$("#showSmoke").text(row.smokeTestStatus == undefined?"":(row.smokeTestStatus == 1?"是":"否"));
	$("#showCaseName").text(row.caseName);
	$("#showCaseDescription").html(row.caseDescription == undefined?"":row.caseDescription.replace(/(\r\n|\n|\r)/gm, "<br />"));
	$("#showCasePrecondition").html(row.casePrecondition == undefined?"":row.casePrecondition.replace(/(\r\n|\n|\r)/gm, "<br />"));
	$("#showCaseClass").text(row.caseClassName);
	//$("#showDrType").text(row.drType == 1?"正向":"反向");
	$("#showInputData").text(row.inputData);
	$("#showTestPoint").text(row.testPoint);
	$("#showBusinessType").text(row.businessType);
	$("#showExpectResult").text(row.expectResult);
	$("#showCaseType").text(row.caseType == 1?"正面":"反面");
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

            class : "stepOrder",
            width : '50px'
        },{
        	field : "stepDescription",
            title : "步骤描述",
						width : 290,
            formatter : function (value, row, index) {
                return value == null?"":value.replace(/(\r\n|\n|\r)/gm, "<br />");
            }
        },{
            field : "stepExpectedResult",
            title : "预期结果",
						width : 290,
            formatter : function (value, row, index) {
                return value == null?"":value.replace(/(\r\n|\n|\r)/gm, "<br />");
            }
        }],
       onLoadSuccess:function(){
            
        },
        onLoadError : function(){
        }
    });
}

function createAddTable(){
	var data = [];
	$("#addCaseSteps").bootstrapTable('destroy');
    $("#addCaseSteps").bootstrapTable({
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
    	$("#addCaseSteps>tbody").empty();
    }
}

//================================================  步骤操作
function addEditTableRow( This ){ 
	var i= $("#addCaseSteps>tbody>tr").length + 1;
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
    if ( $tr.index() == $( "#addCaseSteps>tbody>tr" ).length - 1) {
        alert("最后一条数据不可下移");
    }else {
        $tr.fadeOut(200).fadeIn(100).next().fadeOut(100).fadeIn(100); 
        $tr.next().after($tr);
        changeStepOrder();
    }
}

function addStep_btn(){
	var i= $("#addCaseSteps>tbody>tr").length + 1;
	var str='<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px;line-height:84px; "><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
	$("#addCaseSteps").append(str);
}

//动态改变表格步骤值
function changeStepOrder(){ 
	for( var i=0;i<$("#addCaseSteps>tbody>tr").length ;i++){ 
		$( "#addCaseSteps>tbody>tr" ).eq( i ).find( ".stepOrder" ).text( ( i + 1) );   
	}
}




/*
//案例步骤表格四种操作
function addSteps(){
	var i= $("#addCaseSteps>tbody>tr").length + 1;
	var str='<tr><td class="hideCaseID" style="text-align: center;line-height:84px;"></td><td class="stepOrder" style="text-align: center; width: 50px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px;line-height:84px; "><a class="a_style" style="cursor:pointer" onclick="addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="downTableRow(this)">下移</a></td> </tr>';
	$("#addCaseSteps>tbody").append( str );
	changeStepOrder();
}

//查询所有归档案例
function findArchivedId() {
	$.ajax({
		url: "/testManage/testCase/getArchivedCaseIds",
		method: "post",
		async: false,
		data: {
		},
		success: function (data) {
			archivedCaseNumbers = data.data;
		},
	});
	
}


function importExcel(){
	$("#importModal").modal("show");
}

function exportExcel(){
	window.location.href = "/testManage/testSet/exportTemplet";
}*/

//新增提交
/*function submitTestCase(){
	var testCaseStep = [];
//	$("#newTestCaseForm").data('bootstrapValidator').destroy(); 
//	formValidator();
	$('#newTestCaseForm').data('bootstrapValidator').validate();
	if(!$('#newTestCaseForm').data('bootstrapValidator').isValid()){
		return ;
	}
	for( var i=0;i<$( "#addCaseSteps>tbody>tr" ).length;i++ ){
		var obj={}
		obj.stepOrder=$( "#addCaseSteps>tbody>tr" ).eq( i ).find( ".stepOrder" ).text();
		obj.stepDescription=$( "#addCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(0).val();
		if(obj.stepDescription == ""){
			layer.alert("第"+(i+1)+"行步骤描述为空",{icon : 0});
			return ;
		}
		obj.stepExpectedResult=$( "#addCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(1).val(); 
		if(obj.stepExpectedResult == ""){
			layer.alert("第"+(i+1)+"行预期结果为空",{icon : 0});
			return ;
		}
		testCaseStep.push( obj ); 
	} 
	var data=JSON.stringify({
		'testSetId': testSetId,
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
	    		 initCaseTable();
	    	 }
	    	 $("#loading").css('display','none');
	    },
	    error:function(){
	    	 
	    }
	});  
}*/

//防止jqgrid变成一条线
/*function jqgridAutoWidth(){
	$("body",window.parent.document).width( $("body",window.parent.document).width()-1);
	setTimeout(function(){
		$("body",window.parent.document).width( $("body",window.parent.document).width()+1);
	},500);
}*/
/*导入*/
/*function upload(){
	var formData = new FormData();
	formData.append("file", document.getElementById("upfile").files[0]);
	formData.append("testSetId",testSetId);
	if(document.getElementById("upfile").files[0] == undefined){
		layer.alert("请选择文件",{icon : 0});
		return ;
	}
	$("#loading").css('display','block');
	$.ajax({
        url: "/testManage/testSet/importCase",
        type: "POST",
        data: formData,
        *//**
        *必须false才会自动加上正确的Content-Type
        *//*
        contentType: false,
        *//**
        * 必须false才会避开jQuery对 formdata 的默认处理
        * XMLHttpRequest会对 formdata 进行正确的处理
        *//*
        processData: false,
        success: function (data) {
           if(data.status == 2){
        	   layer.alert("导入失败，原因:"+data.errorMessage,{icon:2});
           }else{
        	   layer.alert("导入成功",{icon:1});
        	   initCaseTable();
        	   $("#importModal").modal("hide");
           }
           $("#loading").css('display','none');
        },
        error: function () {
            alert("上传失败！");
        }
    });
}*/


/*移除多条关联记录*/
/*function removeManyCase(){
	var testSetId = testSetId;
	var ids=$('#executionTable').jqGrid('getGridParam','selarrrow');
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
	            	initCaseTable();
	            }
	            $("#loading").css('display','none');
	        }
	    });
		  layer.close(index);
		});
}*/
//归档案例
/*function archivedCase() {
	findArchivedId();
	//	var ids=[0];
	var ids = $('#executionTable').jqGrid('getGridParam', 'selarrrow');
	if (ids.length == 0) {
		layer.alert('请至少选择一个案例进行归档!', {
			icon: 0,
		})
		return;
	} else {
		var caseNumbers = [];
		var newCaseNumbers = [];
		//var idss = [];
		$.each(ids,function(index,value){
			var rowDate = $("#executionTable").jqGrid("getRowData",value);
			var caseNumber = rowDate.caseNumber.substring(rowDate.caseNumber.indexOf(">")+1,rowDate.caseNumber.indexOf("</a>"))
			caseNumbers.push(caseNumber);
			newCaseNumbers.push(caseNumber);
			//idss.push(parseInt(rowDate.id));
		})
		
		for (var i = 0; i < caseNumbers.length; i++) {
			for (var j = 0; j < archivedCaseNumbers.length; j++) {
				if (caseNumbers[i] == archivedCaseNumbers[j]) {
					newCaseNumbers.splice(i,1,"");
				}
			}
		}
		
		newCaseNumbers.push(0);
		layer.alert('确认要对所选中的案例进行归档？', {
			icon: 0,
			btn: ['确定', '取消']
		}, function () {
			$.ajax({
				url: "/testManage/testCase/archivingCase",
				method: "post",

				data: {
					"ids": newCaseNumbers
				},
				success: function (data) {
					initCaseTable();
					layer.alert('案例已归档!', {
						icon: 1,
					})
				},
			});
		})
	}
}*/

/*
function formValidator(){
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
												},
												stringLength: {
													max:100,
													message: '案例名称长度必须小于100字符'
												}
                    }
                },
            
                
            }
        });
} */

/*
// 重构表单验证
function refactorFormValidator() {
  $('#newTestCaseModal').on('hidden.bs.modal', function () {
    $("#newTestCaseModal :text").val("");
    $("#newTestCaseModal textarea").val("");
      $("#newTestCaseForm").data('bootstrapValidator').destroy();
      $('#newTestCaseForm').data('bootstrapValidator', null);
      formValidator();
  });
}
*/