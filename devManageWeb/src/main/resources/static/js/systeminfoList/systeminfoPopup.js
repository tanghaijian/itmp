$(function(){
	$("#hostName").click(function(){
		$("#serverModal").modal('show');
		clearServer();
		serverShow();
	})
})

//更新部署配置信息
function updateDeploy(status){  
	$('#deployInfoForm').data('bootstrapValidator').validate();  
	if( !$('#deployInfoForm').data("bootstrapValidator").isValid() ){
		return;
	} 
	var deployId=$.trim( $("#deploy_Id").val() );  
	var idstr=$.trim( $("#deploy_serverId").val() ); 
	var environmentType=''; 
	var systemModuleId='';
	for( var i=0;i<$("#serverDiv .def_colList_menu>ul>li").length;i++ ){
		if( $("#serverDiv .def_colList_menu>ul>li").eq(i).hasClass( "menuSelected_col" ) ){
			systemModuleId=$.trim($("#serverDiv .def_colList_menu>ul>li").eq(i).attr( "_id" )); 
			break;
		}
	} 
	for( var i=0;i<$("#serverDiv .def_rowList_menu>ul>li").length;i++ ){
		if( $("#serverDiv .def_rowList_menu>ul>li").eq(i).hasClass( "menuSelected_row" ) ){
			environmentType=$.trim($("#serverDiv .def_rowList_menu>ul>li").eq(i).attr( "evn" )); 
			break;
		}
	} 
	var ids=[];    
	for( var i=0; i<$("#serverGroup .optionDiv").length;i++ ){ 
		var obj={};
		obj.id=$("#serverGroup .optionDiv").eq(i).attr( "value" );
		obj.hostName=$("#serverGroup .optionDiv").eq(i).attr( "hostname" );
		ids.push( obj );
	}   
	if( ids.length==0 ){
		 layer.alert('请选择服务器主机！', {icon: 0});
		 return;
	}
	var systemDeployPath=$.trim($("#deployPath").val());
	var timeOut=$.trim($("#timeOut").val());
	var retryNumber=$.trim($("#retryNumber").val()); 
	var deploySequence=$.trim($("#deploySequence").val()); 
	
	var addArrInfo=[];
	for( var i=0;i<$("#addConfigContent>.rowdiv").length;i++ ){
		var obj={};
		obj.systemDeployId=deployId; 
		if( $("#addConfigContent>.rowdiv").eq(i).find( ".hideIdValue" ).length==1 ){
			obj.id=$("#addConfigContent>.rowdiv").eq(i).find( ".hideIdValue" ).val();
		}
		obj.stepOrder=i+1;
		obj.operateType=$("#addConfigContent>.rowdiv").eq(i).find( "select[name^='opt']" ).val();
		if( $("#addConfigContent>.rowdiv").eq(i).find( "select[name^='opt']" ).val()==1 ){ 
			obj.waitTime=$("#addConfigContent>.rowdiv").eq(i).find( "input[name^='waitTime']" ).val(); 
			obj.script=$("#addConfigContent>.rowdiv").eq(i).find( "textarea[name^='addScript']" ).val(); 
		}else if( $("#addConfigContent>.rowdiv").eq(i).find( "select[name^='opt']" ).val()==2){
			obj.userName=$("#addConfigContent>.rowdiv").eq(i).find( "input[name^='configUseName']" ).val(); 
			obj.password=$("#addConfigContent>.rowdiv").eq(i).find( "input[name^='configPassWord']" ).val(); 
		}else if($("#addConfigContent>.rowdiv").eq(i).find( "select[name^='opt']" ).val()==3 ){
			obj.waitTime=$("#addConfigContent>.rowdiv").eq(i).find( "input[name^='waitTime']" ).val(); 
		}else if($("#addConfigContent>.rowdiv").eq(i).find( "select[name^='opt']" ).val()==4 ){
			obj.script=$("#addConfigContent>.rowdiv").eq(i).find( "textarea[name^='addScript']" ).val(); 
		}else if($("#addConfigContent>.rowdiv").eq(i).find( "select[name^='opt']" ).val()==5 ){
			obj.script=$("#addConfigContent>.rowdiv").eq(i).find( "textarea[name^='addScript']" ).val(); 
		}
		addArrInfo.push( obj );
	}  
	dataObj={
		templateType:$( "#templateType" ).val(),
		list: addArrInfo,
	}; 
	$( "#loading" ).show();
	$.ajax({
  		url:"/devManage/systeminfo/updateDeploy",
    	method:"post", 
    	data:{
    		deployStr:JSON.stringify({
    			id:deployId,
				environmentType: environmentType,
				systemId: idstr,
				systemModuleId: systemModuleId,
				serverIds: ids,
				systemDeployPath:systemDeployPath,
				timeOut:timeOut,
				retryNumber:retryNumber,
				deploySequence:deploySequence,
				deployStatus:1,
				templateType:$( "#templateType" ).val()
    		}),
    		addArrInfoStr:JSON.stringify(addArrInfo)
    	},
  		success:function( data ){ 
  			if (data.status == 1) {
				parent.functionOpt( data, 6 );
				layer.alert('部署配置成功！', {
					icon : 1,
					title : "提示信息", 
				});
//				if(status == 2){
//					$( '#'+id+' .def_colList_menu>ul>li' ).removeClass("menuSelected_col");
//					$( This ).addClass("menuSelected_col");
//					changeEvn();
//				}else if(status == 3){
//					$( '#'+id+' .def_rowList_menu>ul>li' ).removeClass("menuSelected_row");
//			      	$( This ).addClass("menuSelected_row");
//			      	changeEvn();
//				}
			} else{
				layer.alert('部署配置失败！原因：'+data.errorMessage, {
					icon : 2,
					title : "提示信息"
				});
			}
  			isChangeDeplolySetting = false;
  			$("#loading").hide();
        },
  		error:function(){ 
  			$("#loading").hide();
  		}
 	});
}

//更新自动化测试配置信息
function updateAutoTest(){  
	var systemId=$.trim( $("#autoTestSystemId").val()); 
	var authTestArchitectureType=$.trim( $("#authTestArchitectureType").val());
	
	var addArrInfo=[];
	var isReturn = false; 
	$(".autoTestData").children(".dataDiv").each(function(x){
		var envType = $(this).find("input[name='envType']").val();
//		var systemModuleIds = $("input[name='systemModuleIds']").val();
//		var select = document.getElementsByName("systemModuleIds")[x];
		var select = document.getElementById("moduleSelect_"+x);
	    var moduleIds = [];
	    for(var i=0;i < select.length;i++){
	        if(select.options[i].selected){
	        	moduleIds.push(select[i].value);
	        }
	    }
//		var select = document.getElementsByName("testSceneIds")[x];
		var select = document.getElementById("sceneSelect_"+x);
	    var testSceneIds = [];
	    for(var i=0;i < select.length;i++){
	        if(select.options[i].selected){
	        	testSceneIds.push(select[i].value);
	        }
	    }
	    testSceneIds = testSceneIds.toString();
	    
	    if (testSceneIds != "") {
		    if (authTestArchitectureType == 1 && moduleIds.length <= 0) {
		    	alert("已选的测试场景没有指定一个子系统");
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
	
	$.ajax({
  		url:"/devManage/systeminfo/updateAutoTest",
    	method:"post", 
    	data:{
			systemId:systemId,
    		autoTestJson:JSON.stringify(addArrInfo)
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
					btn:['确定'],
					yes:function(){ 
						parent.pageInit()
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

//全选子模块
function selectAll(obj, bool) {
    var dataDiv = $(obj).closest(".dataDiv")
    dataDiv.find("select[name='systemModuleIds'] option").each( function (k, n) {
		n.selected = bool;
    });
    dataDiv.find(".selectpicker").selectpicker('refresh');
}

/*-----------------服务器弹框--------------*/
function serverShow(){
	var serverIds = []; 
	for( var i=0; i<$("#serverGroup .optionDiv").length;i++ ){ 
		serverIds.push( $("#serverGroup .optionDiv").eq(i).attr( "value" ) );
	} 
	 
	$("#loading").css('display', 'block');
	$("#serverTable").bootstrapTable('destroy');
	$("#serverTable").bootstrapTable({
		url : "/devManage/serverinfo/getAllServerInfo2",
		method : "post",
		queryParamsType : "",
		pagination : true,
		sidePagination : "server",
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 5, 10, 15 ],
		queryParams : function(params) {
			var param = {
				page : params.pageNumber,
				rows : params.pageSize,
				hostName : $.trim($("#host").val()),
				IP : $.trim($("#IP").val()),
				haveHost : JSON.stringify( serverIds )
			};
			return param;
		},
		columns : [ {
			checkbox : true,
			width : "30px"
		},{
			field : "id",
			title : "id",
			visible : false,
			align : 'center'
		},{
			field : "hostName",
			title : "主机名",
			align : 'center'
		},{
			field : "ip",
			title : "IP",
			align : 'center'
		}],
		onLoadSuccess : function() { 
			$("#loading").css('display', 'none');
			$("#serverModal").modal("show");
		},
		onLoadError : function() {

		}
	});
	 
}




/*-----------------服务器弹框--------------*/
function serverShow3(){
	var serverIds = [];
	for( var i=0; i<$("#serverGroup .optionDiv").length;i++ ){
		serverIds.push( $("#serverGroup .optionDiv").eq(i).attr( "value" ) );
	}

	$("#loading").css('display', 'block');
	$("#serverTable").bootstrapTable('destroy');
	$("#serverTable").bootstrapTable({
		url : "/devManage/serverinfo/getAllServerInfo3",
		method : "post",
		queryParamsType : "",
		pagination : true,
		sidePagination : "server",
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 5, 10, 15 ],
		queryParams : function(params) {
			var param = {
				page : params.pageNumber,
				rows : params.pageSize,
				hostName : $.trim($("#host").val()),
				IP : $.trim($("#IP").val()),
				systemId:serSystemId,
				haveHost : JSON.stringify( serverIds )
			};
			return param;
		},
		columns : [ {
			checkbox : true,
			width : "30px"
		},{
			field : "id",
			title : "id",
			visible : false,
			align : 'center'
		},{
			field : "hostName",
			title : "主机名",
			align : 'center'
		},{
			field : "ip",
			title : "IP",
			align : 'center'
		}],
		onLoadSuccess : function() {
			$("#loading").css('display', 'none');
			$("#serverModal").modal("show");
		},
		onLoadError : function() {

		}
	});

}

//清空项目搜索条件
function clearServer(){
	$("#host").val("");
	$("#IP").val("");
}
//选择服务器
function commitServer(){
	var selectContent = $("#serverTable").bootstrapTable('getSelections');  
	if( selectContent == '') {
	   	 layer.alert('请选择一列数据！', {icon: 0});
	       return false;
	}else{
		for( var k in selectContent ){
			var str='<div class="optionDiv" hostName="'+selectContent[k].hostName+'" value="'+selectContent[k].id+'"><span class="addSysNames_span">'+selectContent[k].hostName+' </span><span class="close_x" onclick="delOptionDiv(this,event)">×</span></div>';
			$("#serverGroup").append( str );
			$("#serverModal").modal("hide");
		}
		isChangeDeplolySetting = true;
	}
}
//部署配置，左侧模块点击事件
function colClick(id , This){ 
	if(isChangeDeplolySetting){
		layer.confirm('是否保存？', {
			icon:0,
	        btn: ['是', '否'] //按钮
	    }, function () {
	            layer.closeAll();
	            updateDeploy(2);
	    }, function () {
		        layer.closeAll();
		        $( '#'+id+' .def_colList_menu>ul>li' ).removeClass("menuSelected_col");
				$( This ).addClass("menuSelected_col");
				changeEvn();
	    });
	}else{
		$( '#'+id+' .def_colList_menu>ul>li' ).removeClass("menuSelected_col");
		$( This ).addClass("menuSelected_col");
		changeEvn();
	}
} 

//部署配置，上侧环境点击事件
function rowClick(id , This){
	console.log(isChangeDeplolySetting)
	if(isChangeDeplolySetting){
		layer.confirm('是否保存？', {
			icon:0,
	        btn: ['是', '否'] //按钮
	    }, function () {
	            layer.closeAll();
	            updateDeploy(3);
	    }, function () {
	        layer.closeAll();
	        $( '#'+id+' .def_rowList_menu>ul>li' ).removeClass("menuSelected_row");
	      	$( This ).addClass("menuSelected_row");
	      	changeEvn();
	    });
	}else{
		$( '#'+id+' .def_rowList_menu>ul>li' ).removeClass("menuSelected_row");
      	$( This ).addClass("menuSelected_row");
      	changeEvn();
	}
	
} 
function changeEvn(){ 
	
	$("#deployInfoForm").bootstrapValidator('removeField','opt');
	$("#deployInfoForm").bootstrapValidator('removeField','waitTime');
	$("#deployInfoForm").bootstrapValidator('removeField','addScript');
	$("#deployInfoForm").bootstrapValidator('removeField','configUseName');
	$("#deployInfoForm").bootstrapValidator('removeField','configPassWord');
	
	$("#deployInfoForm").data('bootstrapValidator').destroy(); 
    formValidator();   
	var idstr=$.trim( $("#deploy_serverId").val() ); 
	var systemModuleId=''; 
	for( var i=0;i<$("#serverDiv .def_colList_menu>ul>li").length;i++ ){
		if( $("#serverDiv .def_colList_menu>ul>li").eq(i).hasClass( "menuSelected_col" ) ){
			systemModuleId=$.trim($("#serverDiv .def_colList_menu>ul>li").eq(i).attr( "_id" )); 
			break;
		}
	}
	var environmentType='';
	for( var i=0;i<$("#serverDiv .def_rowList_menu>ul>li").length;i++ ){
		if( $("#serverDiv .def_rowList_menu>ul>li").eq(i).hasClass( "menuSelected_row" ) ){
			environmentType=$.trim($("#serverDiv .def_rowList_menu>ul>li").eq(i).attr( "evn" )); 
			break;
		}
	} 
	$("#loading").show(); 
	$.ajax({
  		url:"/devManage/systeminfo/getOneDeploy",
    	method:"post", 
    	data:{
    		systemId: idstr,
    		environmentType: environmentType, 
			systemModuleId: systemModuleId
    	},
  		success:function( data ){
  			isChangeDeplolySetting = false;
  			$("#serverGroup").empty(); 
  			$("#deploy_Id").val( data.deploy.id ); 
  			if( data.deploy.serverIds!='' ){
  				var serverIds=JSON.parse(data.deploy.serverIds)
  				for( var k in  serverIds ){
  					var str='<div class="optionDiv" hostName="'+serverIds[k].hostName+'" value="'+serverIds[k].id+'"><span class="addSysNames_span">'+serverIds[k].hostName+' </span><span class="close_x" onclick="delOptionDiv(this,event)">×</span></div>';
  					$("#serverGroup").append( str ); 
  				}
  			}  
 		    $("#deployPath").val( data.deploy.systemDeployPath );
 		    $("#timeOut").val( data.deploy.timeOut );
 		    $("#retryNumber").val( data.deploy.retryNumber ); 
 		    $("#deploySequence").val( data.deploy.deploySequence ); 
 		    
 		    $( "#templateType" ).empty();
 		    for( var key in data.templateType){
			   var selected = '';
			   if( key == data.deploy.templateType ){
				   selected = 'selected';
			   }
			   $("#templateType").append( "<option value="+ key +" "+selected+" >"+ data.templateType[key] +"</option>" )
		   }   
		   if( data.deploy.templateType == null ){ 
			   $("#templateType").find("option").eq( 0 ).attr("selected",true); 
			   $("#templateType").attr("oldVal", $("#templateType").find("option").eq(0).val() );
		   }else{
			   $("#templateType").attr("oldVal",data.deploy.templateType);
		   }
		   $('.selectpicker').selectpicker('refresh');
		   dataObj={
				templateType: null,
					list:null,
		   };
		   dataObj={
				templateType: $("#templateType").attr("oldVal"),
			  	list:data.deployScriptList,
		   };
 		   configAddInfo( data.deployScriptList );
 		  $("#loading").hide(); 
        },
  		error:function(){ 
  			$("#loading").hide(); 
  		}
 	});
} 

function delOptionDiv( This,e ){
	$( This ).parent( '.optionDiv' ).remove(); 
	isChangeDeplolySetting = true;
	stopPro( e );
}
//阻止事件冒泡
function stopPro(e){
	if (e && e.stopPropagation) { 
  	 	e.stopPropagation();
    } else if (window.event) {  
    	window.event.cancelBubble = true;
    } 
}

function addConfig(){
	$("#addConfigContent").append( 
		'<div class="rowdiv"><div class="form-group def_col_12">'+     
        '<label class="def_col_15 control-label font_right fontWeihgt optClass"><a class="up_opt" title="上移" onclick="up_opt(this)"></a> <a class="down_opt" title="下移" onclick="down_opt(this)"></a> <span class="checkfont">*</span>操作：</label>'+
        '<div class="def_col_21"><select class="selectpicker" name="opt" onchange="showOtherInfo(this)">'+
        '<option value="">请选择</option><option value="1">执行脚本</option><option value="2">切换用户</option><option value="3">上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option>'+
        '</select></div>'+
        '</div><div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div></div>');
	$('.selectpicker').selectpicker('refresh'); 
	addValidator();
	isChangeDeplolySetting = true;
}
function showOtherInfo(This){
	if( $(This).val()==1 ){ 
		$(This).parent().parent().parent().parent().find( '.waitTime' ).remove();
		$(This).parent().parent().parent().parent().find( '.scriptTop' ).remove();
		$(This).parent().parent().parent().parent().find( '.configUseName' ).remove();
		$(This).parent().parent().parent().parent().find( '.configPassWord' ).remove();
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_22 waitTime">'+     
		        '<label class="def_col_8 control-label font_right fontWeihgt"><span class="checkfont">*</span>等待时间(秒)：</label>'+
		        '<div class="def_col_28"><input type="text" name="waitTime" class="form-control" placeholder="请填写执行后需要等待的时间" /></div></div>'	
		)
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_36 scriptTop">'+     
		        '<label class="def_col_5 control-label font_right fontWeihgt"><span class="checkfont">*</span>脚本：</label>'+
		        '<div class="def_col_31"><textarea name="addScript" class="def_textarea"></textarea></div></div>'	
		)  
	}else if( $(This).val()==2 ){ 
		$(This).parent().parent().parent().parent().find( '.waitTime' ).remove();
		$(This).parent().parent().parent().parent().find( '.scriptTop' ).remove();
		$(This).parent().parent().parent().parent().find( '.configUseName' ).remove();
		$(This).parent().parent().parent().parent().find( '.configPassWord' ).remove();
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_11 configUseName">'+     
		        '<label class="def_col_10 control-label font_right fontWeihgt"><span class="checkfont">*</span>用户名：</label>'+
		        '<div class="def_col_26"><input type="text" name="configUseName" class="form-control" placeholder="请填写用户名" /></div></div>'	
		)
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_11 configPassWord">'+     
		        '<label class="def_col_10 control-label font_right fontWeihgt"><span class="checkfont">*</span>密码：</label>'+
		        '<div class="def_col_26"><input type="text" name="configPassWord" class="form-control" placeholder="请填写密码" /></div></div> '	
		)    
	}else if( $(This).val()==3 ){ 
		$(This).parent().parent().parent().parent().find( '.waitTime' ).remove();
		$(This).parent().parent().parent().parent().find( '.scriptTop' ).remove();
		$(This).parent().parent().parent().parent().find( '.configUseName' ).remove();
		$(This).parent().parent().parent().parent().find( '.configPassWord' ).remove();
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_22 waitTime">'+     
		        '<label class="def_col_50 control-label font_right" style="padding-left:5px">上传部署包件到服务器，路径为部署路径标签指定的路径。</label>'+
		        '</div> '	
		) 
	}else if( $(This).val()==4 ){ 
		$(This).parent().parent().parent().parent().find( '.waitTime' ).remove();
		$(This).parent().parent().parent().parent().find( '.scriptTop' ).remove();
		$(This).parent().parent().parent().parent().find( '.configUseName' ).remove();
		$(This).parent().parent().parent().parent().find( '.configPassWord' ).remove();
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_22 waitTime">'+     
		        '<label class="def_col_50 control-label font_right" style="padding-left:5px">暂停当前执行状态并等待用户点击确认后继续执行部署。</label>'+
		        '</div>'	
		)
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_36 scriptTop">'+     
		        '<label class="def_col_5 control-label font_right fontWeihgt"><span class="checkfont">*</span>等待描述内容：</label>'+
		        '<div class="def_col_31"><textarea name="addScript" class="def_textarea"></textarea></div></div>'	
		) 
	}else if( $(This).val()==5 ){ 
		$(This).parent().parent().parent().parent().find( '.waitTime' ).remove();
		$(This).parent().parent().parent().parent().find( '.scriptTop' ).remove();
		$(This).parent().parent().parent().parent().find( '.configUseName' ).remove();
		$(This).parent().parent().parent().parent().find( '.configPassWord' ).remove();
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_22 waitTime">'+     
		        '<label class="def_col_50 control-label font_right" style="padding-left:5px">对上一步骤的输出信息执行断言。</label>'+
		        '</div>'	
		)
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_36 scriptTop">'+     
		        '<label class="def_col_5 control-label font_right fontWeihgt"><span class="checkfont">*</span>断言内容：</label>'+
		        '<div class="def_col_31"><textarea name="addScript" class="def_textarea">断言日志内容等等输出tail -100f xxx.log(替换)\n#ASSERT#\n断言关键字(替换)</textarea></div></div>'
		) 
	}else if( $(This).val()==6){ 
		$(This).parent().parent().parent().parent().find( '.waitTime' ).remove();
		$(This).parent().parent().parent().parent().find( '.scriptTop' ).remove();
		$(This).parent().parent().parent().parent().find( '.configUseName' ).remove();
		$(This).parent().parent().parent().parent().find( '.configPassWord' ).remove();
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_22 waitTime">'+     
		        '<label class="def_col_50 control-label font_right" style="padding-left:5px">在停止服务之前执行beforestop目录下的SQL文件，支持不停机发布。</label>'+
		        '</div> '	
		) 
	}else if( $(This).val()==7){ 
		$(This).parent().parent().parent().parent().find( '.waitTime' ).remove();
		$(This).parent().parent().parent().parent().find( '.scriptTop' ).remove();
		$(This).parent().parent().parent().parent().find( '.configUseName' ).remove();
		$(This).parent().parent().parent().parent().find( '.configPassWord' ).remove();
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_22 waitTime">'+     
		        '<label class="def_col_50 control-label font_right" style="padding-left:5px">在停止服务之后执行afterstop目录下的SQL文件，当前模块全部目标服务器停止后才执行。</label>'+
		        '</div> '	
		) 
	}else if( $(This).val()==8){ 
		$(This).parent().parent().parent().parent().find( '.waitTime' ).remove();
		$(This).parent().parent().parent().parent().find( '.scriptTop' ).remove();
		$(This).parent().parent().parent().parent().find( '.configUseName' ).remove();
		$(This).parent().parent().parent().parent().find( '.configPassWord' ).remove();
		$(This).parent().parent().parent().parent().append( 
				'<div class="form-group def_col_22 waitTime">'+     
		        '<label class="def_col_50 control-label font_right" style="padding-left:5px">在启动服务之后执行afterstartup目录下的SQL文件，当前模块全部目标服务器启动后才执行。</label>'+
		        '</div> '	
		) 
	}else{ 
		$(This).parent().parent().parent().parent().find( '.waitTime' ).remove();
		$(This).parent().parent().parent().parent().find( '.scriptTop' ).remove();
		$(This).parent().parent().parent().parent().find( '.configUseName' ).remove();
		$(This).parent().parent().parent().parent().find( '.configPassWord' ).remove(); 
	} 
	addValidator();
}
function delRowInfo(This){  
	$(This).parent().parent().remove();  
	isChangeDeplolySetting = true;
}
function addValidator(){
	$("#deployInfoForm").bootstrapValidator('removeField','opt');
	$("#deployInfoForm").bootstrapValidator('removeField','waitTime');
	$("#deployInfoForm").bootstrapValidator('removeField','addScript');
	$("#deployInfoForm").bootstrapValidator('removeField','configUseName');
	$("#deployInfoForm").bootstrapValidator('removeField','configPassWord');
	
	$("#deployInfoForm").bootstrapValidator("addField", "opt", {
        validators: {
            notEmpty: {
                message: '请选择操作'
            } 
        }
	}); 
	$("#deployInfoForm").bootstrapValidator("addField", "waitTime", {
        validators: {
            notEmpty: {
                message: '请输入等待时间(秒)'
            },
            regexp: {            
                regexp: /^[0-9]+$/,
                message: '只能输入正整数'
             } 
        }
	});
	$("#deployInfoForm").bootstrapValidator("addField", "addScript", {
        validators: {
            notEmpty: {
                message: '请输入脚本'
            } 
        }
	});
	$("#deployInfoForm").bootstrapValidator("addField", "configUseName", {
        validators: {
            notEmpty: {
                message: '请输入用户名'
            } 
        }
	});
	$("#deployInfoForm").bootstrapValidator("addField", "configPassWord", {
        validators: {
            notEmpty: {
                message: '请输入密码'
            } 
        }
	}); 
}


//追加根据buildingTool选择 显示Gid Aid
function buildingTool( This ){
	 showArtAndGroupId( $( This ).val()  );
}
function showArtAndGroupId( val ){
	$( ".showTag" ).css( "display","none" );
	$( ".showNexus" ).css( "display","block" );
	if( val == '1' ){
		$( ".showMaven" ).css( "display","block" );
	} else if( val == '2' ){
		$( ".showAnt" ).css( "display","block" );
	}
}

function up_opt(This){
	if( $( This ).parent().parent().parent().prev().length == 0 ){
		layer.alert('已经是第一条了!', {icon: 0});
	}else{
		$( This ).parent().parent().parent().prev().before(  $( This ).parent().parent().parent() ) 
		isChangeDeplolySetting = true;
	} 
}
function down_opt(This){
	if( $( This ).parent().parent().parent().next().length == 0 ){
		layer.alert('已经是最后一条了!', {icon: 0});
	}else{
		$( This ).parent().parent().parent().next().after(  $( This ).parent().parent().parent() )  
		isChangeDeplolySetting = true;
	} 
}

function copyInfo(){
	layer.confirm('确认复制？', {
		icon: 0,	
		btn: ['确定', '取消'] //可以无限个按钮 
	}, function(index, layero){
		var obj={}; 
		obj.deployPath = $("#deployPath").val();
		obj.timeOut = $("#timeOut").val();
		obj.retryNumber = $("#retryNumber").val(); 
		obj.deploySequence = $("#deploySequence").val(); 
		$("#addConfigContent .selectpicker").selectpicker('destroy');
		$("#addConfigContent select").addClass( "selectpicker" );
		
		obj.opt =  $( "#addConfigContent" ).html();
		obj = JSON.stringify( obj )
		sessionStorage.setItem("configData", obj ); 
		$(".selectpicker").selectpicker('refresh');
		layer.closeAll(); 
	}, function(index){ 
		layer.closeAll();
	}); 
}

function pasteInfo(){
	layer.confirm('确认粘贴？', {
		icon: 0,	
		btn: ['确定', '取消'] //可以无限个按钮 
	}, function(index, layero){
		var configData = sessionStorage.getItem("configData")
		configData = JSON.parse( configData )   
		if( configData == null || configData == '' || configData == undefined ){
			layer.alert('请先复制，再粘贴！!', {icon: 0});
			return ; 
		} 
		
		$("#deployPath").val('');
		$("#timeOut").val('');
		$("#retryNumber").val('');
		$("#deploySequence").val('');
		$("#addConfigContent" ).empty() 
		
		$("#deployPath").val( configData.deployPath );
		$("#timeOut").val( configData.timeOut );
		$("#retryNumber").val( configData.retryNumber );
		$("#deploySequence").val( configData.deploySequence );
		$("#addConfigContent" ).append( configData.opt );  
		//删除id，避免复制过来的信息和原信息用的是同一份数据源
		$("#addConfigContent .hideIdValue").each(function(){
            $(this).val("");
		});
		$(".selectpicker").selectpicker('refresh');
		layer.closeAll(); 
	}, function(index){ 
		layer.closeAll();
	}); 
}






















