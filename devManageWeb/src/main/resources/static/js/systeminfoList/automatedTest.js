
/**
 * 自动化测试配置
 */
var parameterArr={};//存储url参数
$(document).ready(function(){
	parameterArr.arr = window.location.href.split( "?" ); 
    parameterArr.parameterArr = parameterArr.arr[1].split( "," );
    parameterArr.obj={};
    for( var i = 0 ; i < parameterArr.parameterArr.length ; i++ ){
	   	var obj = parameterArr.parameterArr[i].split( "=" );  
	   	if( obj[1] == "undefined" ){
	   		parameterArr.obj[ obj[0] ] = '';
	   	}else{

	   		//parameterArr.obj[ obj[0] ] = decodeURI(obj[1]);
			parameterArr.obj[ obj[0] ] = decodeURI(obj[1]);
	   	} 
    }   
	initPage();
});

//初始化列表数据
function initPage(){  
	var authTestArchitectureType = parameterArr.obj.architectureType;
	$("#authTestArchitectureType").val( authTestArchitectureType ); 
	$("#autoTestSystemId").val( parameterArr.obj.id );  
	$.ajax({
		url:"/devManage/systeminfo/getAutoTest",
	    method:"post",
		data:{
			"systemId":parameterArr.obj.id,
		},
	   success:function(data){  
		   if( data.types.length <= 0){
			   layer.alert('请先配置该系统服务环境!', {
                   icon: 0,
                   title: "提示信息"
               });
			   return;
		   } 
		   
		   var modelStr = "";
		   for(var i=0;i<data.systemModule.length;i++){
			   modelStr += '<option value="'+data.systemModule[i].id+'">'+data.systemModule[i].moduleName+'</option>'
		   }
		   var testSceneStr = "";
		   for(var i=0;i<data.testScene.length;i++){
			   testSceneStr += '<option value="'+data.testScene[i].valueCode+'">'+data.testScene[i].valueName+'</option>'
		   }
		   
		   var uiTestSceneStr = "";
		   for(var i=0;i<data.uiTestScene.length;i++){
			   uiTestSceneStr += '<option value="'+data.uiTestScene[i].valueCode+'">'+data.uiTestScene[i].valueName+'</option>'
		   }
		   
		   $(".autoTestData").empty();
		   //组装自动化测试表头
		   $(".uiAutoTest").empty();
		   var str1 = '<div class="singleData">' +
	   		'<div class="singleDataDiv" style="width:15%"><div class="singleDataFont">环境类型</div></div>' +
			'<div class="singleDataDiv" style="width:15%"><div class="singleDataFont">系统</div></div>' +
			'<div class="singleDataDiv moduleDiv" style="width:25%"><div class="singleDataFont">子系统</div></div>' +
			'<div class="singleDataDiv moduleDiv" style="width:15%"><div class="singleDataFont"></div></div>' +
			'<div class="singleDataDiv" style="width:25%"><div class="singleDataFont">测试场景</div></div>' +
			'</div>';
		   $(".autoTestData").append( str1 );
		   $(".uiAutoTest").append( str1 ); 
		   
		   for(var i=0;i<data.types.length;i++){ 
			   //api自动化测试表体
			   var str2 = '<div class="singleData dataDiv">' + 
				'<div class="singleDataDiv" style="width:15%"><div class="search_input entrySelect"><input type="hidden" name="envType" value="'+data.types[i].envType+'"/> '+data.types[i].envName+' </div> </div>'+
				'<div class="singleDataDiv" style="width:15%"><div class="search_input entrySelect">'+parameterArr.obj.systemName+'</div> </div>'+
				'<div class="singleDataDiv moduleDiv" style="width:25%"><div class="search_input selectDiv"> <select class="selectpicker moduleSelect_'+i+'" multiple name="systemModuleIds">'+modelStr+'</select> </div></div>'+
				'<div class="singleDataDiv moduleDiv" style="width:15%"><div class="search_input entrySelect"><button type="button" class="btn btn-default"  onclick="selectAll(this,true)">全选</button>&nbsp;&nbsp;<button type="button" class="btn btn-default"onclick="selectAll(this,false)" >清空</button></div></div>'+
				'<div class="singleDataDiv" style="width:25%"><div class="search_input selectDiv"> <select class="selectpicker sceneSelect_'+i+'" multiple name="testSceneIds">'+testSceneStr+'</select> </div> </div>'+
				'</div>';
			   //ui自动化测试表体
			   var str3 = '<div class="singleData dataDiv">' + 
				'<div class="singleDataDiv" style="width:15%"><div class="search_input entrySelect"><input type="hidden" name="envType" value="'+data.types[i].envType+'"/> '+data.types[i].envName+' </div> </div>'+
				'<div class="singleDataDiv" style="width:15%"><div class="search_input entrySelect">'+parameterArr.obj.systemName+'</div> </div>'+
				'<div class="singleDataDiv moduleDiv" style="width:25%"><div class="search_input selectDiv"> <select class="selectpicker moduleSelect_'+i+'" multiple name="systemModuleIds">'+modelStr+'</select> </div></div>'+
				'<div class="singleDataDiv moduleDiv" style="width:15%"><div class="search_input entrySelect"><button type="button" class="btn btn-default"  onclick="selectAll(this,true)">全选</button>&nbsp;&nbsp;<button type="button" class="btn btn-default"onclick="selectAll(this,false)" >清空</button></div></div>'+
				'<div class="singleDataDiv" style="width:25%"><div class="search_input selectDiv"> <select class="selectpicker sceneSelect_'+i+'" multiple name="testSceneIds">'+uiTestSceneStr+'</select> </div> </div>'+
				'</div>';
			   $(".autoTestData").append( str2 ); 
			   $(".uiAutoTest").append( str3 ); 
			   
			   var currentDevRow = $(".autoTestData").children(".dataDiv").eq(i);
			   var currentDevRow1 = $(".uiAutoTest").children(".dataDiv").eq(i);

			   for(var j=0;j<data.autoTest.length;j++){
				   if (data.autoTest[j].environmentType == data.types[i].envType) {
					   currentDevRow.find("select[name='systemModuleIds'] option").each( function (k, n) {
						   if (n.value == data.autoTest[j].systemModuleId) {
							   n.selected = true;
						   }
		               });
					   
					   var testSceneArr = data.autoTest[j].testScene.split(",");
					   currentDevRow.find("select[name='testSceneIds'] option").each( function (k, n) {
	                       for (var ii = 0; ii < testSceneArr.length ; ii++){
	                    	   if (n.value == testSceneArr[ii]) {
								   n.selected = true;
							   }
	                       }
	                   });
					   
				   }
			   } 
			   for(var j=0;j<data.uiAutoTest.length;j++){
				   if (data.uiAutoTest[j].environmentType == data.types[i].envType) {
					   currentDevRow1.find("select[name='systemModuleIds'] option").each( function (k, n) { 
						   if (n.value == data.uiAutoTest[j].systemModuleId) {
							   n.selected = true;
						   }
		               });
					   
					   var testSceneArr = data.uiAutoTest[j].testScene.split(",");
					   currentDevRow1.find("select[name='testSceneIds'] option").each( function (k, n) {
	                       for (var ii = 0; ii < testSceneArr.length ; ii++){
	                    	   if (n.value == testSceneArr[ii]) {
								   n.selected = true;
							   }
	                       }
	                   });
					   
				   }
			   } 
		   }
		   
		   $(".selectpicker").selectpicker('refresh');
		   
//		   if (authTestArchitectureType == 2) { //1=多模块架构；2=传统架构
//			   $(".autoTestData").find(".moduleDiv").hide();
//		   }
	   } 
	}); 
}  

//全选
function selectAll(obj, bool) {
    var dataDiv = $(obj).closest(".dataDiv")
    dataDiv.find("select[name='systemModuleIds'] option").each( function (k, n) {
		n.selected = bool;
    });
    dataDiv.find(".selectpicker").selectpicker('refresh');
}

function down(This){
    if( $(This).hasClass("fa-angle-double-down") ){
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
    }else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
    }
} 

//更新自动化测试配置
function updateAutoTest(){  
	var systemId=$.trim( $("#autoTestSystemId").val()); 
	var authTestArchitectureType=$.trim( $("#authTestArchitectureType").val());
	
	var addArrInfo=[];
	var addArrInfo2 =[];
	var isReturn = false; 
	$(".autoTestData").children(".dataDiv").each(function(x){
		var envType = $(this).find("input[name='envType']").val(); 
		var select = $(".moduleSelect_"+x).find( "select" )[0]; 
	    var moduleIds = [];
	    for(var i=0;i < select.length;i++){
	        if(select.options[i].selected){
	        	moduleIds.push(select[i].value);
	        }
	    } 
		var select = $(".sceneSelect_"+x).find( "select" )[0];
	    var testSceneIds = [];
	    for(var i=0;i < select.length;i++){
	        if(select.options[i].selected){
	        	testSceneIds.push(select[i].value);
	        }
	    }
	    testSceneIds = testSceneIds.toString();
	    
	    if (testSceneIds != "") {
		    if (authTestArchitectureType == 1 && moduleIds.length <= 0) { 
		    	layer.alert('自动化配置已选的测试场景没有指定一个子系统!', {
					icon : 2,
					title : "提示信息"
				});
		    	isReturn = true;
			}
	    	
	    	if (moduleIds.length > 0) {
	    		for(var i=0;i < moduleIds.length;i++){
	    			var obj={};
	    			obj.systemId=systemId;
	    			obj.environmentType=envType; 
	    			obj.systemModuleId=moduleIds[i]; 
	    			obj.testScene=testSceneIds; 
	    			obj.testType = 1;
	    			addArrInfo.push( obj );
	    		}
	    	} else {
	    		var obj={};
	    		obj.systemId=systemId;
    			obj.environmentType=envType; 
    			obj.systemModuleId=null; 
    			obj.testScene=testSceneIds; 
    			obj.testType = 1;
    			addArrInfo.push( obj );
	    	}
	    }
	});
	if (isReturn) {
		return;
	}
	$(".uiAutoTest").children(".dataDiv").each(function(x , element){
		var envType = $(this).find("input[name='envType']").val(); 
		var select = $(".moduleSelect_"+x).find( "select" )[1]; 
	    var moduleIds = [];
	    for(var i=0;i < select.length;i++){
	        if(select.options[i].selected){
	        	moduleIds.push(select[i].value);
	        }
	    } 
		var select = $(".sceneSelect_"+x).find( "select" )[1];
	    var testSceneIds = [];
	    for(var i=0;i < select.length;i++){
	        if(select.options[i].selected){
	        	testSceneIds.push(select[i].value);
	        }
	    }
	    testSceneIds = testSceneIds.toString();
	    
	    if (testSceneIds != "") {
		    if (authTestArchitectureType == 1 && moduleIds.length <= 0) {
		    	layer.alert('UI自动化配置已选的测试场景没有指定一个子系统!', {
					icon : 2,
					title : "提示信息"
				}); 
		    	isReturn = true;
			}
	    	
	    	if (moduleIds.length > 0) {
	    		for(var i=0;i < moduleIds.length;i++){
	    			var obj={};
	    			obj.systemId=systemId;
	    			obj.environmentType=envType; 
	    			obj.systemModuleId=moduleIds[i]; 
	    			obj.testScene=testSceneIds; 
	    			obj.testType = 2;
	    			addArrInfo2.push( obj );
	    		}
	    	} else {
	    		var obj={};
	    		obj.systemId=systemId;
    			obj.environmentType=envType; 
    			obj.systemModuleId=null; 
    			obj.testScene=testSceneIds; 
    			obj.testType = 2;
    			addArrInfo2.push( obj );
	    	}
	    }
	});
	if (isReturn) {
		return;
	}
	
	$.ajax({
  		url:"/devManage/systeminfo/updateAutoTest",
    	method:"post", 
    	data:{
			systemId:systemId,
    		autoTestJson:JSON.stringify(addArrInfo),
    		uiAutoTestJson:JSON.stringify(addArrInfo2),
    	},
  		success:function( data ){ 
  			if (data.status == 2) {
				layer.alert('部署配置失败！原因：'+data.errorMessage, {
					icon : 2,
					title : "提示信息"
				});
			} else {
				layer.alert('部署配置成功！', {
					icon : 1,
					title : "提示信息",
					yes:function(){ 
	  				   		parent.functionOpt( data,7 )
	  				   		parent.layer.closeAll();
	 				}
				}); 
//				searchInfo();
			}
        },
  		error:function(){ 
  		}
 	});
}