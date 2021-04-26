/**
 * Created by 朱颜辞镜花辞树 on 2018/9/18.
 * 系统配置页面js
 */
//分页所需参数
var searchArr={}; 
var systemId = null;
var moduleId = null;
var systemVersionStr = '';
var commissioningWindow = '';
var environmentTypeStr = '';
var sId = null;
var envIds = [];
var toolData = '';
var sType = '';
//总页数
searchArr.total=0;
//当前页
searchArr.pageNum=1; 
//页面条目数
searchArr.row=[10,20,30]; 
//当前页面条目数
searchArr.select=searchArr.row[0];
//查询条件
searchArr.postData={
	"pages":searchArr.pageNum,
	"row":searchArr.select,
	"systemName" : $("#systemName").val(),
    "systemCode" :  $("#systemCode").val(),
    "architectureType" :  $("#architectureType").val(),
    "status" :  $("#status").val()
}  
$(function(){ 
	/*getSearchData();*/
	formValidator();
	$('#addSystemModal').on('hide.bs.modal', function () {
		$('#addSyatemForm').bootstrapValidator('resetForm');
	});
	//所有的Input标签，在输入值后出现清空的按钮
    $('.color1 input[type*="text"]').parent().css("position","relative");
    $('.color1 input[type*="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('.color1 input[type*="text"]').bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    })
    pageInit(); 
    //搜索展开和收起
    $("#downBtn").click(function () { 
        if( $(this).children("span").hasClass( "fa-caret-up" ) ){
            $(this).children("span").removeClass("fa-caret-up");
            $(this).children("span").addClass("fa-sort-desc");
            $("#search_div").slideUp(200);
        }else {
            $(this).children("span").removeClass("fa-sort-desc");
            $(this).children("span").addClass("fa-caret-up");
            $("#search_div").slideDown(200);
        }
    }) 
// 搜索框 收藏按钮 js 部分
    $(".collection").click(function () {
        if( $(this).children("span").hasClass("fa-heart-o") ){
            $(this).children("span").addClass("fa-heart");
            $(this).children("span").removeClass("fa-heart-o");
        }else {
            $(this).children("span").addClass("fa-heart-o");
            $(this).children("span").removeClass("fa-heart");
        }
    })   
});


//表格数据加载 
function pageInit(){ 
	jQuery("#list2").jqGrid("clearGridData");
	$("#loading").css('display','block');
    jQuery("#list2").jqGrid({  
        url:'/devManage/systeminfo/getAllSystemInfo', 
        datatype: "json", 
        height: 'auto',  
        mtype : "post",
        postData:{
        	"pages":searchArr.pageNum,
        	"row":searchArr.row[0],
        },    
        autowidth:true, 
        colNames:[  'id','projectId','key_id','系统编号', '系统名称','所属项目','GroupID','ArtifactID','系统类型','状态','操作'],
        colModel:[ 
        	{name:'id',index:'id',hidden:true},
            {name:'projectId',index:'id',hidden:true},
            {name:'key_id',index:'key_id',hidden:true,key:true},
            {name:'systemCode',index:'system_code'},
            {name:'systemName',index:'system_name', formatter : function(value, grid, rows, state) {
            	var str=rows.systemName;
            	if(rows.architectureType != undefined){
	            	if( rows.architectureType==1 ){
	            		str+=" <sup class='micSup'>多模块系统</sup>"
	            	}
            	}
                return str; 
            }},
            {name:'projectName',index:'PROJECT_NAME'},
            {name:'groupId',index:'group_Id'},
            {name:'artifactId',index:'artifact_Id'}, 
            {name:'architectureType',hidden:true,index:'architecture_type'},
            {name:'status',index:'status', width:80},
            { 
                name:'操作',
                index:'操作',
                align:"left",
                fixed:true,
                sortable:false,
                resize:false,
                classes: 'optClass',
                search: false,
                width:400,
                formatter : function(value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;'); 
                    var str = '';
                    if(rows.level == 0){
                    	str = '<a class="a_style" style="cursor:pointer" onclick="add_sub_system('+ row + ')">新建子系统</a>';
                    	str += ' | <a class="a_style" style="cursor:pointer" onclick="edit_system('+ row + ')">编辑</a>';
                    }else if(rows.level == 1){   //子系统
                    	if(systemConfig){//拥有配置权限
                        	str += '<a class="a_style" style="cursor:pointer" onclick="config('+ row + ')">配置</a>';
                        }
                        if(systemVersion){//拥有版本权限
//    						if(!rows.parent){
    							str += ' | <a class="a_style" style="cursor:pointer" onclick="versionManageInit('+ row + ')">版本管理</a>';
//    						}
                        }
                        
                        
                    	str += ' | <a class="a_style" style="cursor:pointer" onclick="codeConfig('+ rows.id + ','
                    	+ rows.architectureType + ',\''+ rows.systemName +'\')">源码配置</a>';
                    	
                    	if(rows.architectureType == 1 && systemAddModule){//微服务架构并且拥有按钮权限
                    		str+=' | <a class="a_style" style="cursor:pointer" onclick="addService('+ row + ')">新增模块</a>'
                        }
//                    	if(rows.createType == 1 && systemDeploy){
//                    		str+=' | <a class="a_style" style="cursor:pointer" onclick="deploymentConfig('+ row + ')">部署配置</a>'
//                      }
                    	if(rows.createType == 1){//自动创建模式拥有【部署】按钮
                    		str+=' | <li role="presentation" class="dropdown">' +
                    				'<a class="dropdown-toggle a_style" data-toggle="dropdown" role="button">'+
                    				'</span>部署 <span class="fa fa-angle-down"></span></a>'+
                    				'<ul class="dropdown-menu" >' ;
                    		if(systemDeploy){//拥有部署配置权限
                    			str+= '<li><a class="a_style" style="cursor:pointer" onclick="deploymentConfig('+ row + ')">部署配置</a></li>';
                    		}
                    		if(authTest){//拥有【】权限
                    			str+= '<li><a class="a_style" style="cursor:pointer" onclick="authTestConfig('+ row + ')">自动化测试配置</a></li>'
                    		}
                    		str+='</ul></li>';
                    	}
                    	if(systemEnvironment){//拥有【】权限
                    		str+=' | <a class="a_style" style="cursor:pointer" onclick="environmentConfig('+ row + ')">环境配置</a>'
                    	}
                    	if(deleteSystem){//拥有【】权限
                    		str+=' | <a class="a_style" style="cursor:pointer" onclick="deleteSystemInfo('+ rows.id +')">删除</a>'
                    	}

                    }else if(rows.level == 2){   //子模块
                    	if(systemConfig){//拥有【】权限
                        	str += '<a class="a_style" style="cursor:pointer" onclick="config('+ row + ')">配置</a>';
                        }
                    	if(deleteModule && toStr(rows.architectureType) == "") {//拥有【】权限并且改行未定义架构模式
                        	str += ' | <a class="a_style" style="cursor:pointer" onclick="deleteModuleInfo('+ rows.id + ',' 
                        	+ rows.systemId + ')">删除</a>';
                        }
                    }
                    return str;
                }
            }
        ],  
        treeGrid: true, 
        treeGridModel: "adjacency",
        ExpandColumn: "systemCode", 
        treeIcons: {plus:'fa fa-caret-right',minus:'fa fa-caret-down',leaf:''},
        ExpandColClick: true,
        jsonReader: {
            repeatitems: false,
            root: "rows", 
        }, 
        pager: '#pager', 
        sortable: true,   //是否可排序
        sortorder: 'asc', 
        sortname: 'id',
        loadtext:"数据加载中......",
        //viewrecords: true, //是否要显示总记录数  
        loadComplete :function( xhr ){
        	$("#loading").css('display','none');
        	if( xhr===undefined ){ 
        	}else{
        		allData=xhr.rows;  
            	$("#loading").css('display','none');            	 
            	if( xhr.total==0 ){
            		searchArr.total=1; 
            	}else{ 
                	searchArr.total=xhr.total; 
            	}  
            	addPage( searchArr ); 
        	} 
        } 
    }).trigger("reloadGrid");    
}

//删除子模块
function deleteModuleInfo(id, systemId){
	layer.confirm('你确定要删除本条记录吗？', {
	  btn: ['确定','取消'] //按钮
	}, function(){
		layer.closeAll();
		var url = "/devManage/systeminfo/deleteSystemModule";//删除子模块
		var data = { "id" : id,"systemId" : systemId}; 
		$.ajax({
	  		url:url,
	    	method:"post", 
	    	async:false,
	    	data:data, 
	  		success:function(data){   
	  			if(data.status == 1){
	  				layer.alert('删除成功!',{
		 				 icon: 1,
		 			})
	  				pageInit();
	  			}else{
	  				layer.alert('删除失败!',{
		 				 icon: 2,
		 			})
	  			} 
	        }, 
	 	}); 
	}, function(){
		layer.closeAll();
	});
}

//删除系统
function deleteSystemInfo(id){
	layer.confirm('你确定要删除本条记录吗？', {
	  btn: ['确定','取消'] //按钮
	}, function(){
		layer.closeAll();
		url = "/devManage/systeminfo/deleteSystem";//删除系统
		var data = { "id" : id}; 
		$.ajax({
	  		url:url,
	    	method:"post", 
	    	async:false,
	    	data:data, 
	  		success:function(data){   
	  			if(data.status == 1){
	  				layer.alert('删除成功!',{
		 				 icon: 1,
		 			})
	  				pageInit();
	  			}else{
	  				layer.alert('删除失败!',{
		 				 icon: 2,
		 			})
	  			} 
	        }, 
	 	}); 
	}, function(){
		layer.closeAll();
	});
}

//查询部署配置信息，并显示
function deploymentConfig( rows ){
	layer.open({
	      type: 2,
	      title: rows.systemName ,
	      shadeClose: true,
	      shade: false, 
	      move: false,
	      area: ['100%', '100%'], 
	      id: "1",
	      tipsMore: true,
	      cancel: function(index, layero){ 
	    	  sessionStorage.clear();
	    	  layer.closeAll(); 
	      },
	      anim: 2,
	      content:'/devManageui/systeminfo/toDeploymentConfig?,id='+rows.id+',architectureType='+rows.architectureType,
	      btn: ['确定','取消'] ,
	      btnAlign: 'c', //按钮居中
	      yes: function(index , layero){
	    	  var body = layer.getChildFrame('body', index );
	    	  var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
	          iframeWin.updateDeploy(); 
	      },
	      btn2:function(){ 
	    	  sessionStorage.clear();
	    	  layer.closeAll(); 
		 } 
	}); 
	return ;
	$("#deployInfoForm").bootstrapValidator('removeField','opt');
	$("#deployInfoForm").bootstrapValidator('removeField','waitTime');
	$("#deployInfoForm").bootstrapValidator('removeField','addScript');
	$("#deployInfoForm").bootstrapValidator('removeField','configUseName');
	$("#deployInfoForm").bootstrapValidator('removeField','configPassWord');
	
	$("#deployInfoForm").data('bootstrapValidator').destroy(); 
    formValidator(); 
	clearDefListMenu(); 
	$("#addConfigContent").empty();
	$("#deploymentConfigTit").html( rows.systemName );
	$("#deploy_serverId").val( rows.id ); 
	$.ajax({
		url:"/devManage/systeminfo/getModuleAndScm",
	    method:"post",
		data:{
			"systemId":rows.id,
		},
	   success:function(data){  
		   if( data.types.length <= 0){
			   layer.alert('请先配置该系统服务环境!', {
                   icon: 0,
                   title: "提示信息"
               });
			   return;
		   } 
		   var str = "";
		   var moduleStr = "";
		   if( data.deploy.id == null ){
			   $("#deploy_Id").val( null ); 
		   }else{
			   $("#deploy_Id").val( data.deploy.id );
		   } 
		   var idStr='serverDiv'; 
		   $("#deploymentConfig").modal('show');
		   if( rows.level==0 ){
			   if( rows.architectureType==1 ){  //微服务架构，显示子模块
				   if( data.systemModule.length==0 ){
					   $("#serverDiv .def_colList_menu").css('display','none');
				   }else{
					   $("#serverDiv .def_colList_menu").css('display','block');
					   for(var i=0;i<data.systemModule.length;i++){ 
						   if(i==0){
							   var liStr = '<li class="menuSelected_col" _Id="'+data.systemModule[i].id+'" title="'+data.systemModule[i].moduleName+'" onclick="colClick( \''+idStr+'\',this )">'+data.systemModule[i].moduleName+'</li>';
						   }else{
							   var liStr = '<li _Id="'+data.systemModule[i].id+'" title="'+data.systemModule[i].moduleName+'" onclick="colClick( \''+idStr+'\',this )">'+data.systemModule[i].moduleName+'</li>';
						   }
						   $("#serverDiv .def_colList_menu>ul").append( liStr );
					   } 
				   }  
			   }else{
				   $("#serverDiv .def_colList_menu").css('display','none');
			   } 
			   for(var i=0;i<data.types.length;i++){ 
				   if(i==0){
					   var liStr = '<li class="menuSelected_row" evn="'+data.types[i].envType+'" onclick="rowClick( \''+idStr+'\',this )">'+data.types[i].envName+'</li>';
				   }else{
					   var liStr = '<li evn="'+data.types[i].envType+'" onclick="rowClick( \''+idStr+'\',this )">'+data.types[i].envName+'</li>';
				   }
				   $("#serverDiv .def_rowList_menu>ul").append( liStr );
			   } 
			   
			   if( data.deploy.serverIds!='' ){//显示服务器列表
				   var serverIds=JSON.parse(data.deploy.serverIds);
				   for( var k in  serverIds ){
						var str='<div class="optionDiv" hostName="'+serverIds[k].hostName+'" value="'+serverIds[k].id+'"><span class="addSysNames_span">'+serverIds[k].hostName+' </span><span class="close_x" onclick="delOptionDiv(this,event)">×</span></div>';
						$("#serverGroup").append( str ); 
					} 
			   } 
		   }    
		   $("#deployPath").val(data.deploy.systemDeployPath);
		   $("#timeOut").val(data.deploy.timeOut);
		   $("#retryNumber").val(data.deploy.retryNumber); 
		   $("#deploySequence").val(data.deploy.deploySequence); 
		   configAddInfo(data);
	   } 
	});   
} 

//自动测试部署
function authTestConfig( rows ){
	layer.open({
	      type: 2,
	      title: rows.systemName ,
	      shadeClose: true,
	      shade: false, 
	      move: false,
	      area: ['100%', '100%'], 
	      id: "1",
	      tipsMore: true, 
	      anim: 2,
	      content:'/devManageui/systeminfo/toAutomatedTesting?,id='+rows.id+',architectureType='+rows.architectureType+',systemName='+rows.systemName,
	      btn: ['确定','取消'] ,
	      btnAlign: 'c', //按钮居中
	      yes: function(index , layero){
	    	  var body = layer.getChildFrame('body', index );
	    	  var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
	          iframeWin.updateAutoTest(); 
	      },
	      no:function(){ 
	    	  layer.closeAll(); 
		 } 
	}); 
	
	return; 
	
	//架构类型
	var authTestArchitectureType = rows.architectureType;
	$("#authTestArchitectureType").val( authTestArchitectureType ); 
	$("#autoTestSystemId").val( rows.id );  
	$.ajax({
		url:"/devManage/systeminfo/getAutoTest",
	    method:"post",
		data:{
			"systemId":rows.id,
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
		   //组装自动化测试表单字段
		   $(".autoTestData").empty();
		   //表头
		   $(".autoTestData").append('<div class="singleData">' +
    		'<div class="singleDataDiv" style="width:15%"><div class="singleDataFont">环境类型</div></div>' +
    		'<div class="singleDataDiv" style="width:15%"><div class="singleDataFont">系统</div></div>' +
    		'<div class="singleDataDiv moduleDiv" style="width:25%"><div class="singleDataFont">子系统</div></div>' +
    		'<div class="singleDataDiv moduleDiv" style="width:15%"><div class="singleDataFont"></div></div>' +
    		'<div class="singleDataDiv" style="width:25%"><div class="singleDataFont2">测试场景</div></div>' +
			'</div>');
		   $("#autoTestConfig").modal('show');
		   //表体
		   for(var i=0;i<data.types.length;i++){ 
			   $(".autoTestData").append(
				    '<div class="singleData dataDiv">' + 
					'<div class="singleDataDiv" style="width:15%"><div class="search_input entrySelect"><input type="hidden" name="envType" value="'+data.types[i].envType+'"/> '+data.types[i].envName+' </div> </div>'+
					'<div class="singleDataDiv" style="width:15%"><div class="search_input entrySelect">'+rows.systemName+'</div> </div>'+
					'<div class="singleDataDiv moduleDiv" style="width:25%"><div class="search_input entrySelect"> <select class="selectpicker" multiple id="moduleSelect_'+i+'" name="systemModuleIds">'+modelStr+'</select> </div></div>'+
					'<div class="singleDataDiv moduleDiv" style="width:15%"><div class="search_input entrySelect"><button type="button" class="btn btn-default"  onclick="selectAll(this,true)">全选</button>&nbsp;&nbsp;<button type="button" class="btn btn-default"onclick="selectAll(this,false)" >清空</button></div></div>'+
					'<div class="singleDataDiv" style="width:25%"><div class="search_input entrySelect"> <select class="selectpicker" multiple id="sceneSelect_'+i+'" name="testSceneIds">'+testSceneStr+'</select> </div> </div>'+
					'</div> '); 
			   
			   var currentDevRow = $(".autoTestData").children(".dataDiv").eq(i)

			   //填充下拉框并选中，子系统，
			   for(var j=0;j<data.autoTest.length;j++){
				   if (data.autoTest[j].environmentType == data.types[i].envType) {
					   currentDevRow.find("select[name='systemModuleIds'] option").each( function (k, n) {
						   if (n.value == data.autoTest[j].systemModuleId) {
							   n.selected = true;
						   }
		               });
					   //填充下拉框并选中，测试场景
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
		   }
		   $(".autoTestData").find(".selectpicker").selectpicker('refresh');
//		   if (authTestArchitectureType == 2) { //1=多模块架构；2=传统架构
//			   $(".autoTestData").find(".moduleDiv").hide();
//		   }
	   } 
	});
} 

//环境配置弹窗显示环境
function environmentConfig( row ){
	sId = row.id;
	$("#environmentModal").modal('show');
	findEnvIds(row);
	findEnvironment(envIds);
}

//通过ID获取环境信息
function findEnvIds(row){
	var id = row.id;
	$.ajax({
  		url:"/devManage/systeminfo/findEnvIds",
    	method:"post", 
    	async:false,
    	data:{
    		"id":id
		}, 
  		success:function(data){  
  			envIds = data.data;
        }, 
 	}); 
}
//环境名称
function findEnvironment(envIds){
	$("#environmentTable").bootstrapTable('destroy');
    $("#environmentTable").bootstrapTable({  
    	url:"/devManage/systeminfo/getAllEnvironment",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
//        pageNumber : 1,
//        pageSize : 10,
//        pageList : [ 5, 10, 15],
//        queryParams : function(params) {
//             var param={ 
//            	
//             }
//            return param;
//        },
        responseHandler:function (res) {
        	 var rows=res.list;
            return {
            	"rows":rows
            };
        },
        columns : [{
            checkbox : true,
            width : "30px",
            formatter : function(value, row, index) {
            	if(envIds==undefined){
            		return false;
            	}
            	for(var i=0;i<envIds.length;i++){
            		if(envIds[i]==row.envType){
            			return true;
            		}
            	}
            }
        },{
            field : "envType",
            title : "envType",
            visible : false,
            align : 'center'
        },{
            field : "envName",
            title : "环境名称",
            align : 'center'
            	
        }],
        onLoadSuccess:function(){
        	 
        },
        onLoadError : function() {

        }
    }); 
}

//提交配置环境
function commitEnvironment(){
	var rows = $("#environmentTable").bootstrapTable('getSelections');
	if(rows.length<=0){
		layer.alert('请选择环境配置!',{
			 icon: 0,
		}) 
		return ;
	}else{
		var envType = [];
		var env = '';
		for(var i=0; i<rows.length;i++ ){
			envType.push(rows[i].envType);
			env=envType.join(',');
		}
			$.ajax({
		  		url:"/devManage/systeminfo/configEnvironment",
		    	method:"post", 
		    	data:{
		    		"id":sId,
		    		"envType":env
				}, 
		  		success:function(data){  
		  			if(data.status == 1){
		  				layer.alert('配置成功!',{
			 				 icon: 1,
			 			})
			 			$("#environmentModal").modal('hide');
		  			}else{
		  				layer.alert('配置失败!',{
			 				 icon: 2,
			 			})
		  			}
		        }, 
		 	}); 
		
		
	}
}

//部署配置新增部署内容
function configAddInfo(data){
	$("#addConfigContent").empty();
	if( data.deployScriptList.length>0 ){
		   for(var i=0;i<data.deployScriptList.length;i++ ){ 
			   var newStr="";
			   var newStr='<div class="rowdiv"><div class="form-group def_col_12">'+
			   '<input class="hideIdValue" type="hidden" value='+data.deployScriptList[i].id+' />'+
		       '<label class="def_col_15 control-label font_right fontWeihgt"><span class="checkfont">*</span>操作：</label>'+
		       '<div class="def_col_21"><select class="selectpicker" name="opt" onchange="showOtherInfo(this)"><option value="">请选择</option>';
		        
			   if( data.deployScriptList[i].operateType==1 ){ 
				   
				   newStr+='<option value="1" selected >执行脚本</option><option value="2">切换用户</option><option value="3">上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
				   '<div class="form-group def_col_22 waitTime">'+     
			       '<label class="def_col_8 control-label font_right fontWeihgt"><span class="checkfont">*</span>等待时间(秒)：</label>'+
			       '<div class="def_col_28"><input type="text" name="waitTime" class="form-control" placeholder="请填写执行后需要等待的时间" value='+data.deployScriptList[i].waitTime+' /></div></div>'+
			       '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div>'+
			       '<div class="form-group def_col_36 scriptTop">'+     
			       '<label class="def_col_5 control-label font_right fontWeihgt"><span class="checkfont">*</span>脚本：</label>'+
			       '<div class="def_col_31"><textarea name="addScript" class="def_textarea">'+data.deployScriptList[i].script+'</textarea></div></div></div>';   
			   
			   }else if( data.deployScriptList[i].operateType==2 ){
				   newStr+='<option value="1">执行脚本</option><option value="2" selected>切换用户</option><option value="3">上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
				   '<div class="form-group def_col_11 configUseName">'+     
			       '<label class="def_col_10 control-label font_right fontWeihgt"><span class="checkfont">*</span>用户名：</label>'+
			       '<div class="def_col_26"><input type="text" name="configUseName" class="form-control" placeholder="请填写用户名" value='+data.deployScriptList[i].userName+'  /></div></div>'+ 
			       '<div class="form-group def_col_11 configPassWord">'+     
			       '<label class="def_col_10 control-label font_right fontWeihgt"><span class="checkfont">*</span>密码：</label>'+
			       '<div class="def_col_26"><input type="text" name="configPassWord" class="form-control" placeholder="请填写密码" value='+data.deployScriptList[i].password+'  /></div></div>'+	
				   '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div></div>';
			   }else if( data.deployScriptList[i].operateType==3 ){
				   newStr+='<option value="1">执行脚本</option><option value="2" >切换用户</option><option value="3" selected>上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
			       '<div class="form-group def_col_22 waitTime">'+     
			       '<label class="def_col_50 control-label font_right" style="padding-left:5px">上传部署包件到服务器，路径为部署路径标签指定的路径。</label>'+
			       '</div>'+	
				   '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div></div>'; 
			   }else if( data.deployScriptList[i].operateType==4 ){
				   newStr+='<option value="1" >执行脚本</option><option value="2">切换用户</option><option value="3">上传包件</option><option value="4" selected>暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
				   '<div class="form-group def_col_22 waitTime">'+     
			       '<label class="def_col_50 control-label font_right" style="padding-left:5px">暂停当前执行状态并等待用户点击确认后继续执行部署。</label>'+
			       '</div>'+
			       '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div>'+
			       '<div class="form-group def_col_36 scriptTop">'+     
			       '<label class="def_col_5 control-label font_right fontWeihgt"><span class="checkfont">*</span>等待描述内容：</label>'+
			       '<div class="def_col_31"><textarea name="addScript" class="def_textarea">'+data.deployScriptList[i].script+'</textarea></div></div></div>';   
			   }else if( data.deployScriptList[i].operateType==5 ){
				   newStr+='<option value="1" >执行脚本</option><option value="2">切换用户</option><option value="3">上传包件</option><option value="4" selected>暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
				   '<div class="form-group def_col_22 waitTime">'+     
			       '<label class="def_col_50 control-label font_right" style="padding-left:5px">对上一步骤的输出信息执行断言。</label>'+
			       '</div>'+
			       '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div>'+
			       '<div class="form-group def_col_36 scriptTop">'+     
			       '<label class="def_col_5 control-label font_right fontWeihgt"><span class="checkfont">*</span>断言内容：</label>'+
			       '<div class="def_col_31"><textarea name="addScript" class="def_textarea">'+data.deployScriptList[i].script+'</textarea></div></div></div>';   
			   }else if( data[i].operateType==6 ){
				   newStr+='<option value="1">执行脚本</option><option value="2" >切换用户</option><option value="3">上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
				   '<option value="6" selected>停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
   			       '<div class="form-group def_col_22 waitTime">'+     
   			       '<label class="def_col_50 control-label font_right" style="padding-left:5px">在停止服务之前执行beforestop目录下的SQL文件，支持不停机发布。</label>'+
   			       '</div>'+	
   				   '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div></div>'; 
   			   }else if( data[i].operateType==7 ){
				   newStr+='<option value="1">执行脚本</option><option value="2" >切换用户</option><option value="3">上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
				   '<option value="6">停止前执行SQL</option><option value="7" selected>停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
   			       '<div class="form-group def_col_22 waitTime">'+     
   			       '<label class="def_col_50 control-label font_right" style="padding-left:5px">在停止服务之后执行afterstop目录下的SQL文件，当前模块全部目标服务器停止后才执行。</label>'+
   			       '</div>'+	
   				   '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div></div>'; 
   			   }else if( data[i].operateType==8 ){
				   newStr+='<option value="1">执行脚本</option><option value="2" >切换用户</option><option value="3">上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
				   '<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8" selected>启动后执行SQL</option></select></div></div>'+
   			       '<div class="form-group def_col_22 waitTime">'+     
   			       '<label class="def_col_50 control-label font_right" style="padding-left:5px">在启动服务之后执行afterstartup目录下的SQL文件，当前模块全部目标服务器启动后才执行。</label>'+
   			       '</div>'+	
   				   '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div></div>'; 
   			   }else{
				   newStr+='<option value="1">执行脚本</option><option value="2" >切换用户</option><option value="3" selected>上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+ 	
				   '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div></div>';  
			   }
			    
			   $("#addConfigContent").append( newStr ); 
		   }   
		   $('.selectpicker').selectpicker('refresh');
		   addValidator();
	   } 
}
function clearDefListMenu(){
	$("#deploymentConfig .def_colList li").remove();
	$("#deploymentConfig .def_rowList li").remove();
	$("#deploymentConfig #serverGroup").empty();
} 
//添加翻页栏
function addPage( searchArr ){  
	$("#pager_center").empty(); 
	var aClass='';
	// pageNum total row  
	var str='<table class="ui-pg-table ui-common-table ui-paging-pager"><tbody><tr>'
	if( searchArr.pageNum==1 ){
		aClass='ui-disabled';
	}else{
		aClass='';
	}
	str+='<td onclick="changePage(this)" id="first_pager" class="ui-pg-button '+aClass+'" title=""><span class="fa fa-step-backward"></span></td><td onclick="changePage(this)"  id="prev_pager" class="ui-pg-button '+aClass+'" title=""><span class="fa fa-backward"></span></td><td class="ui-pg-button ui-disabled"><span class="ui-separator"></span></td>';		
	
	str+='<td id="input_pager" dir="ltr" style="position: relative;"> <input class="pageIn ui-pg-input form-control" type="text" size="2" maxlength="7" value='+searchArr.pageNum+' role="textbox"> 共 <span id="sp_1_pager">'+Math.ceil( searchArr.total/searchArr.select )+'</span> 页</td>';
	
	if( searchArr.pageNum==(  Math.ceil( searchArr.total/searchArr.select )   )||Math.ceil( searchArr.total/searchArr.select )==0 ){
		aClass='ui-disabled';  
	}else{
		aClass='';
	}
	str+='<td class="ui-pg-button ui-disabled" style="cursor: default;"><span class="ui-separator"></span></td><td id="next_pager" onclick="changePage(this)"  class="ui-pg-button '+aClass+'" title=""><span class="fa fa-forward"></span></td><td onclick="changePage(this)" id="last_pager" class="ui-pg-button '+aClass+'" title=""><span class="fa fa-step-forward"></span></td><td dir="ltr"><select class="ui-pg-selbox form-control" onchange="changeSelect(this)" size="1" role="listbox" title="">';
	for(var i=0;i<searchArr.row.length;i++){
		if( searchArr.row[i]==0  ){
			alert(" 泰拳警告 !");
			return ;
		}else if( searchArr.row[i]==searchArr.select  ){ 
			str+='<option role="option" value='+searchArr.row[i]+' selected >'+searchArr.row[i]+'</option>';
		}else{
			str+='<option role="option" value='+searchArr.row[i]+' >'+searchArr.row[i]+'</option>';
		} 
	} 
	str+='</select></td></tr></tbody></table>';
	$("#pager_center").append( str );
	$(".pageIn").keydown(function(event){
		var e = event || window.event;
		if(e.keyCode==13){
			var reg=/^[1-9]\d*$/;
			if( reg.test($(this).val()) ){
				if( $(this).val()<=Math.ceil(searchArr.total/searchArr.select) ){
					searchArr.postData.pages=$(this).val();
					searchArr.pageNum=searchArr.postData.pages; 
					searchInfo();
					return ;
				}
			}
			$(this).val(searchArr.pageNum );
		}
	});
}

//翻页
function changePage( This ){   
	if( $(This).hasClass('ui-disabled') ){
		return;
	}else{  
		if( $(This).attr('id')=='first_pager' ){
			searchArr.pageNum=1;
		}else if( $(This).attr('id')=='prev_pager' ){
			searchArr.pageNum--;
		}else if( $(This).attr('id')=='next_pager' ){
			searchArr.pageNum++;
		}else if( $(This).attr('id')=='last_pager' ){
			searchArr.pageNum=(  Math.ceil( searchArr.total/searchArr.select )   );
		} 
		searchArr.postData={
				"pages":searchArr.pageNum, 
		} 
		addPage(searchArr);
		searchInfo();  
	}  
}
function changeSelect(This){  
	searchArr.select=$(This).val();
	searchArr.pageNum=1; 
	searchArr.postData={
			"pages":searchArr.pageNum,
			"row":searchArr.select, 
	} 
	searchInfo();  
}
//查询
function searchInfo(){  
	$("#loading").css('display','block');
	$("#list2").jqGrid('setGridParam',{ 
		url:"/devManage/systeminfo/getAllSystemInfo",
        postData:searchArr.postData,
        loadComplete :function(xhr){  
        	searchArr.total=xhr.total;   
        	addPage(searchArr);
        	$("#loading").css('display','none');
        }
    }).trigger("reloadGrid"); //重新载入
}
function searchBoxInfo(){
	searchArr.pageNum=1; 
	searchArr.postData={
			"pages":searchArr.pageNum,
			"row":searchArr.select,
			"systemName" : $.trim($("#systemName").val()),
		    "systemCode" : $.trim( $("#systemCode").val()),
		    "architectureType" :  $("#architectureType").val(),
		    "status" :  $("#status").val()
	} 
	searchInfo();
	addPage( searchArr );
} 
//版本管理
function versionManageInit(row){
	if(row.level == '1'){
		systemId = row.id;
		moduleId = '';
	}else if(row.level == '2'){
		moduleId = row.id;
		systemId = row.systemId;
	}
	layer.open({
	      type: 2,
	      title:  '版本管理 : '+ row.systemCode + ' ' + row.systemName ,
	      shadeClose: true,
	      shade: false, 
	      move: false,
	      area: ['100%', '100%'], 
	      id: "1",
	      tipsMore: true,
	      anim: 2,
	      content:'/devManageui/systeminfo/toVersionManagement?systemId='+systemId+',moduleId='+moduleId,
	      btn: ['关闭'],
	      btnAlign: 'c', //按钮居中 
	      no:function(){
	    	  layer.closeAll(); 
		 } 
	}); 
	return ;  
	/*$("#loading").css('display', 'block');
	$("#versionModal").modal('show');
	$.ajax({
		url:"/devManage/systemVersion/getSystemVersionByCon",
	    method:"post",
		data:{
			"systemId":systemId,
			"moduleId":moduleId,
			"status":null
		},
	   success:function(data){
		   loadVersionTable(data.rows);
	   },
	   complete:function(data){
		   $("#loading").css('display', 'none');  
	   }
	});  */
}
//加载版本表格
function loadVersionTable(data){
	$("#list3").bootstrapTable('destroy');
	$("#list3").bootstrapTable({
		data : data,
		method : "post",
	    cache: false,
		columns : [ {
			field : "id",
			title : "id",
			visible : false,
			align : 'center'
		},{
			field : "version",
			title : "版本",
			align : 'center'
		},{
			field : "groupFlag",
			title : "版本分组标签",
			align : 'center'
		},{
			field : "status",
			title : "状态",
			align : 'center',
			formatter : function(value,grid,rows,
					state){
				return value == 1?"已开启":"已关闭";
			}
		},{
			title : '操作',
			align : 'center',
			fixed : true,
			sortable : false,
			resize : false,
			search : false,
			formatter : function(value, grid, rows,
					state) {
				var row = JSON.stringify(grid).replace(/"/g, '&quot;');  
				var str = '';
				if(systemVersionChange){
					if(grid.status == 1){
						str += '<a class="a_style" style="cursor:pointer" onclick="changeStatus('+ grid.id + ',2)">关闭</a>';
					}else{
						str += '<a class="a_style" style="cursor:pointer" onclick="changeStatus('+ grid.id + ',1)">开启</a>';
					}
				}
				if(systemVersionEdit){
					str += ' | <a class="a_style" style="cursor:pointer" onclick="editVersion('+ row + ')">编辑</a>';
				}
                return str;
			}
		} ]
	});
}
//修改状态
function changeStatus(id,status){
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/systemVersion/updateSystemVersion",
	    method:"post",
		data:{
			"groupFlag":null,
			"version":null,
			"status":status,
			"id":id
		},
	   success:function(data){
		   $("#loading").css('display','none');
		   if(data.status == 2){
			   layer.alert(data.errorMessage, {
                  icon: 2,
                  title: "提示信息"
               });
		   }else{
			   layer.alert('编辑成功', {
                  icon: 1,
                  title: "提示信息"
               });
			   versionManageInit({level:""});
		   }
	   } 
	});
}

//新增版本
function insertVersion(){
	layer.prompt({
		title: '输入版本号'
	},
	function(value, index, elem){
		$.ajax({
			url:"/devManage/systemVersion/addSystemVersion",
		    method:"post",
			data:{
				"groupFlag":$("#addGroupFlag").val(),
				"version":value,
				"systemId":systemId,
				"moduleId":moduleId
			},
		   success:function(data){
			   layer.close(index);
			   $("#loading").css('display','none');
			   if(data.status == 2){
				   layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
			   }else{
				   layer.alert('保存成功', {
                        icon: 1,
                        title: "提示信息"
                    });
				   versionManageInit({level:""});
			   }
		   }
		
		});  
	});
	$(".layui-layer-content").append("<label class='updLabel'>版本号</label><br/>");	
	$(".layui-layer-content").append("<input type='text' class='layui-layer-input' id='addGroupFlag' value=''/>");
	$(".layui-layer-content").append("<label class='updLabel'>版本分组标签</label>");
}
//编辑版本
function editVersion(row){
	layer.prompt({
		formType: 0,
		title: '编辑版本',
		title2: '输入版本号',
		value:row.version
	},	
	function(value, index, elem){		
		$.ajax({
			url:"/devManage/systemVersion/updateSystemVersion",
		    method:"post",
			data:{
				"groupFlag":$("#updGroupFlag").val(),
				"version":value,
				"status":null,
				"id":row.id
			},
		   success:function(data){
			   layer.close(index);
			   $("#loading").css('display','none');
			   if(data.status == 2){
				   layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
			   }else{
				   layer.alert('编辑成功', {
                        icon: 1,
                        title: "提示信息"
                    });
				   versionManageInit({level:""});
			   }
		   }		
		});
	});
	$(".layui-layer-content").append("<label class='updLabel'>版本号</label><br/>");	
	$(".layui-layer-content").append("<input type='text' class='layui-layer-input' id='updGroupFlag' value='"+row.groupFlag+"'/>");
	$(".layui-layer-content").append("<label class='updLabel'>版本分组标签</label>");
}

//新增微服务弹框
function addService(row){  
	layer.open({
	      type: 2,
	      title:  '新增子模块: '+ row.systemCode + ' ' + row.systemName ,
	      shadeClose: true,
	      shade: false, 
	      move: false,
	      area: ['100%', '100%'], 
	      id: "1",
	      tipsMore: true,
	      anim: 2,
	      content:'/devManageui/systeminfo/toAddSystemModel?id='+row.id,
	      btn: ['确定','取消'] ,
	      btnAlign: 'c', //按钮居中
	      yes: function(index , layero){
	    	  var body = layer.getChildFrame('body', index );
	    	  var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
	          iframeWin.addMudule(); 
	      },
	      no:function(){
	    	  layer.closeAll(); 
		 } 
	}); 
	return ;  
}
//新增微服务提交
function addMudule(){  
	$('#newform').data('bootstrapValidator').validate();  
	if( !$('#newform').data("bootstrapValidator").isValid() ){
		return;
	}	
	var systemId = $("#systemId").val();
	var moduleCode = $("#moduleCode").val();
	var moduleName = $("#moduleName").val();
	var buildDependency = $("#addBuildDependency").val();
	var buildDependencySequence = $("#addBuildDependencySequence").val();
	
	var buildType=$("#addBuildingTool input[name='tools']:checked").val();
	var groupId='';
	var artifactId='';
	var jdkVersion='';
	//maven构建
	if( buildType==1 ){
		groupId=$("#addGroupID").val();
		artifactId=$("#addArtifactID").val();
		jdkVersion=$("#addJdkVersion").val();
	}else if( buildType==2 ){//ant构建
		jdkVersion=$("#addJdkVersion").val();
	} 

	var relativePath=$("#relativePathAdd").val();
	if(relativePath==""){
		layer.alert('请填写编译相对路径', {
			icon: 2,
			title: "提示信息"
		});
		return;
	}


	var releaseRepositoryName = $("#addReleaseRepositoryName").val();
	var snapshotRepositoryName = $("#addSnapshotRepositoryName").val();
	var buildToolVersion = $("#addBuildToolVersion").val();
	var packageSuffix = $("#addPackageSuffix").val();
	var compileCommand = $("#addCompileCommand").val();
	
	var moduleArr = [];
	for(var i=0;i<$(".environmentStatus").length;i++){
		(function (i){
			if( $(".environmentStatus").eq(i).find("input[type='checkbox']").is(':checked') ){   
				var scmid={};
				scmid.systemScmId =  $( $(".environmentStatus").eq(i).find("input[name='systemScmneme']")).eq(0).attr('val');
				if($( $(".environmentStatus").eq(i).find("input[name='moduleScmNeme']")).eq(0).attr('value')!=''){
					scmid.id = $( $(".environmentStatus").eq(i).find("input[name='moduleScmNeme']")).eq(0).attr('value') ;
				} 
				moduleArr.push(scmid);
			}
		})(i);  
	}
	moduleArr=JSON.stringify(moduleArr);
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/systeminfo/addSystemModule",
	    method:"post",
		data:{
			"moduleArr":moduleArr,
			"systemId":systemId,
			"moduleCode":moduleCode,
			"moduleName":moduleName,
			"buildDependency":buildDependency,
			"buildDependencySequence":buildDependencySequence,
			"buildType":buildType,
			"releaseRepositoryName": releaseRepositoryName,
			"snapshotRepositoryName": snapshotRepositoryName,
			"buildToolVersion": buildToolVersion,
			"packageSuffix": packageSuffix,
			"jdkVersion": jdkVersion,
			"groupId": groupId,
			"artifactId": artifactId,
			"compileCommand":compileCommand,
			"relativePath":relativePath
		},
	   success:function(data){
		   $("#loading").css('display','none');
		   if(data.status == "success"){
			   layer.alert('编辑成功', {
                   icon: 1,
                   title: "提示信息"
               });
			  // alert("保存成功！！！");
			   pageInit();
 				$("#addService").modal("hide");
		   }else if(data.status == "fail"){
			   layer.alert('保存失败', {
                   icon: 2,
                   title: "提示信息"
               });
			   //alert("保存失败！！！");
		   }
	   }
	
	});
	
}
//是否显示代码审计人
function codeReviewShow( This ){
	 var value = $(This).selectpicker('val');
	 if(value == "1"){
		 $("#codeReviewUserIds").parents('.form-group').removeClass('hidden');
	 }else{
		 $("#codeReviewUserIds").val("");
		 $("#codeReviewUserName").val("");
		 $("#codeReviewUserIds").parents('.form-group').addClass('hidden');
	 }
}

//新增子系统
function add_sub_system(row){
	layer.open({
	      type: 2,
	      title:  '新建子系统 : '+ row.systemCode + ' ' + row.systemName ,
	      shadeClose: true,
	      shade: false, 
	      move: false,
	      area: ['100%', '100%'], 
	      id: "1",
	      tipsMore: true,
	      anim: 2,
	      content:"/devManageui/systeminfo/toSystemDeploy?id=,createType=," +
	      		"systemId=,artifactId=,buildDependency=,buildDependencySequence=,systemCode="+row.systemCode+",parentId="+row.id,
	      btn: ['确定','取消'] ,
	      btnAlign: 'c', //按钮居中
	      yes: function(index , layero){
	    	  var body = layer.getChildFrame('body', index );
	    	  var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
	          iframeWin.submit(); 
	      },
	      no:function(){
	    	  layer.closeAll(); 
		 } 
	}); 
}

function config( row ){   
	layer.open({
	      type: 2,
	      title:  '配置 : '+ row.systemCode + ' ' + row.systemName ,
	      shadeClose: true,
	      shade: false, 
	      move: false,
	      area: ['100%', '100%'], 
	      id: "1",
	      tipsMore: true,
	      anim: 2,
	      content:"/devManageui/systeminfo/toSystemDeploy?id="+row.id+",createType="
		      +row.createType+",systemId=" +row.systemId+",artifactId=" 
		      +row.artifactId+",buildDependency=" +row.buildDependency+",buildDependencySequence=" 
		      +row.buildDependencySequence,
	      btn: ['确定','取消'] ,
	      btnAlign: 'c', //按钮居中
	      yes: function(index , layero){
	    	  var body = layer.getChildFrame('body', index );
	    	  var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
	          iframeWin.submit(); 
	      },
	      no:function(){
	    	  layer.closeAll(); 
		 } 
	}); 
	
}
//全选
function allCheck( This ){ 
	if( $( This ).is(':checked') ){
		$(".singleData :checkbox:not(:eq(0))").prop("checked",'checked'); 
	}else{
		$(".singleData :checkbox:not(:eq(0))").prop("checked",'');
	} 
}
/*//版本选择
function selectVersion(){
	var systemId = $("#microServiceID").val();
	$("#versionSelModal .modal-body").empty();
	$("#versionSelModal").modal('show');
	$("#loading").css('display','block');
	$.ajax({
  		url:"/devManage/systemVersion/getSystemVersionByCon",
    	method:"post", 
    	data:{
    		 "systemId":systemId,
    		 "moduleId":null,
    		 "status":1
    	},
  		success:function(data){ 
  			$("#loading").css('display','none');
  			if(data.status == 1){
  				var systemVersionStr='';
  				$.each(data.rows,function(index,value){
						systemVersionStr += '<option value="'+value.id+'">'+value.version+'</option>'
					});
  				$("#versionSelModal .modal-body").append('<select class="selectpicker" id="selVersion">'+
                        	'<option value="">请选择</option>'+ systemVersionStr+
                    	'</select>');
  				$('.selectpicker').selectpicker('refresh');
  			}
  			if(data.status == 2){
  				layer.alert('查询失败'+data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
  			}
  			
        },
  		error:function(){ 
  		}
 	});
}

function commitVersion(){
	var version = $("#selVersion").selectpicker('val');
	$(".singleData :checkbox:checked").parents('.singleData').find("[name=systemVersion]").selectpicker('val',version);
	$("#versionSelModal").modal('hide');
}*/

//系统配置提交
function microServiceCommit(){
	$('#configInfoForm').data('bootstrapValidator').validate();  
	if( !$('#configInfoForm').data("bootstrapValidator").isValid() ){
		return;
	}
	var data={}
	var id=$("#microServiceID").val();
	var buildType=$("#buildingTool input[name='tools']:checked").val();
	var groupId='';
	var artifactId='';
	var jdkVersion='';
	if( buildType==1 ){
		groupId=$("#microServiceGroupID").val();
		artifactId=$("#microServiceArtifactID").val();
		jdkVersion=$("#editJdkVersion").val();
	}else if( buildType==2 ){
		jdkVersion=$("#editJdkVersion").val();
	} 
	
	var architectureId = $("#architectureId").val(); 
	var deployType = $("#deployType").val(); 
	var productionDeployType = $("#productionDeployType").val(); 
	var snapshotRepositoryName = $("#microServiceSnapshotRepositoryName").val(); 
	var releaseRepositoryName = $("#microServiceReleaseRepositoryName").val();  
	var createType=$("#buildingWay input[name='build']:checked").val();
	var codeReviewStatus=$("#codeReviewStatus").val();
	var developMode=$("#developMode").val();
	var codeReviewUserIds=$("#codeReviewUserIds").val();
	var editCompileCommand=$( "#editCompileCommand" ).val();
	var sonarScanStatus=$( "#sonarScanStatus" ).val();
	var buildToolVersion=$("#editBuildToolVersion").val();
	var packageSuffix=$("#editPackageSuffix").val();
	
	/*var list=[];
	var changeList=[];
	var envType=[];  
	 //判断环境类型是否重复  
	for(var i=0;i<$("#codeConfig .singleData").length-1;i++){
		var obj={};
		var  warningFont='';   
		if($("select[name='environmentTypeName']").eq(i).find("option:selected").attr("value")!=''){
			obj.environmentType=$("select[name='environmentTypeName']").eq(i).find("option:selected").attr("value");  
			envType.push( obj.environmentType );
		} else{
			 warningFont="环境类型";
			 warningShow( warningFont );
			 return ;
		}
		if($("select[name='scmTypeName']").find("option:selected").attr("value")!=''){
			obj.scmType=$("select[name='scmTypeName']").find("option:selected").attr("value");
		}else{
			 warningFont="托管服务";
			 warningShow( warningFont );
			 return ;
		}
		if($("select[name='toolId']").eq(i).find("option:selected").attr("value")!=''){
			obj.toolId=$("select[name='toolId']").eq(i).find("option:selected").attr("value");
		}else{
			 warningFont="工具地址";
			 warningShow( warningFont );
			 return ;
		}   
		if($(".microServiceScmBranch").eq(i).val()!=''){
			obj.scmBranch=$(".microServiceScmBranch").eq(i).val();
		}else{
			 warningFont="分支名称";
			 warningShow( warningFont );
			 return ;
		}
		if($(".microServiceScmUrl").eq(i).val()!=''){
			var reg = new RegExp(/((http|svn)?:)\/\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]/g); 
			
			if(reg.test( $(".microServiceScmUrl").eq(i).val() ) ){
				obj.scmUrl=$(".microServiceScmUrl").eq(i).val();
			}else{
				layer.alert("源代码地址格式错误，格式为：xxx://xxx/xx",{icon : 2});
				return;
			}
		}else{
			 warningFont="管理地址";
			 warningShow( warningFont );
			 return ;
		}  
		if($(".systemScmNeme").eq(i).val()!=''){
			obj.id=$("input[name='systemScmNeme']").eq(i).val();
		}
		if($(".microServiceScmSubmitSuperUser").eq(i).val()!=''){
			obj.submitSuperUserNames=$("input[name='microServiceScmSubmitSuperUser']").eq(i).val();
		} 
		if($(".microServiceSubmitUserName").eq(i).val()!=''){
			obj.submitUserNames=$("input[name='microServiceSubmitUserName']").eq(i).val();
		} 
		
		
		if( $("input[name='systemScmNeme']").eq(i).attr("value")!='' ){ 
			if($("select[name='environmentTypeName']").eq(i).find("option:selected").attr("value")!= $("select[name='environmentTypeName']").eq(i).attr("typeValue") ){
				changeList.push( obj );
			} else{
				list.push( obj );
			}
		}else{
			list.push( obj );
		}
		 
	}    
	
	if($("select[name='scmTypeName']").find("option:selected").attr("value")!='' && $(".microServiceData .singleData").length < 2){
		layer.alert('请至少添加一条数据！', {
            icon: 2,
            title: "提示信息"
        });  
		return ;
	} 
	
//	console.log($("select[name='scmTypeName']").find("option:selected").attr("value"))
	if($("#scmTypeName").val()==2){
		console.log($("#scmTypeName").val())
		if( isRepeat(envType) ){  
			layer.alert('环境类型重复，请修改！', {
	            icon: 2,
	            title: "提示信息"
	        });  
			return ;
		}  
	}
	list=JSON.stringify(list);
	changeList=JSON.stringify(changeList);*/
	var flagManual="";//手动创建任务中构建部署用于判断填写的数据中是否有相同的行
	var flagDeploy="";//手动创建任务中部署配置中用于判断填写的数据是否有相同的行
	if( createType==1 ){
		data={
    		"id":id,
    		"systemName":$("#microServiceName").val(),
            "systemCode":$("#microServiceNum").val(),
    		"groupId":groupId,
    		"artifactId":artifactId,/*
    		"systemScm":list,
    		"newSystemScm":changeList,*/
    		"snapshotRepositoryName":snapshotRepositoryName,
    		"releaseRepositoryName":releaseRepositoryName,
    		"architectureType":architectureId,   		
    		"deployType":deployType,
    		"productionDeployType":productionDeployType, 		
    		"createType":createType,
    		"createTypes": createType, 
    		"buildType": buildType,
    		"codeReviewStatus":codeReviewStatus,
    		"jdkVersion":jdkVersion,
    		"buildToolVersion":buildToolVersion,
    		"packageSuffix":packageSuffix,
    		"developmentMode":developMode,
    		"compileCommand":editCompileCommand,
    		"codeReviewUserIds":codeReviewUserIds,
    		"sonarScanStatus":sonarScanStatus
    	}; 
	}else if( createType==2 ){ //手动构建
		$('#buildingForm').data('bootstrapValidator').validate();  
		if( !$('#buildingForm').data("bootstrapValidator").isValid() ){
			return;
		}  
		var judgeJobName=new Array();
		var list2=[];
		for( var i=0;i<$(".manualInfo .buildingConfig .JenkinsJob").length;i++ ){
			(function (i){ 
				
				var obj={};
				if(  $(".manualInfo .buildingConfig .JenkinsJob").eq(i).children(".hideKeyId").val()!="" ){
					obj.id=$(".manualInfo .buildingConfig .JenkinsJob").eq(i).children(".hideKeyId").val();
				}
				obj.toolId=$(".manualInfo .buildingConfig .JenkinsJob").eq(i).find("select[class*='JenkinsTooladdress']").val();
				obj.jobName=$(".manualInfo .buildingConfig .JenkinsJob").eq(i).find("input[name*='JobName']").val();
				obj.jobPath=$(".manualInfo .buildingConfig .JenkinsJob").eq(i).find("input[name*='JobPath']").val();
				obj.jobType=1;
				if($.inArray(obj.jobName,judgeJobName)>-1){
//					layer.alert(obj.jobName+'已重复请修改', {
//	                    icon: 2,
//	                    title: "提示信息"
//	                });
					flagManual=flagManual+obj.jobName+",";
					
				}else{
					judgeJobName.push(obj.jobName);
				
				}
				list2.push( obj );
			})(i); 
		}
		var judgeJobNames=new Array();
		for( var i=0;i<$(".manualInfo .deployConfig .JenkinsJob").length;i++ ){
			(function (i){  
				var obj={};
				if(  $(".manualInfo .deployConfig .JenkinsJob").eq(i).children(".hideKeyId").val()!="" ){
					obj.id=$(".manualInfo .deployConfig .JenkinsJob").eq(i).children(".hideKeyId").val();
				}
				obj.toolId=$(".manualInfo .deployConfig .JenkinsJob").eq(i).find("select[class*='JenkinsTooladdress']").val();
				obj.jobName=$(".manualInfo .deployConfig .JenkinsJob").eq(i).find("input[name*='JobName']").val();  
				obj.jobPath=$(".manualInfo .deployConfig .JenkinsJob").eq(i).find("input[name*='JobPath']").val();
				if($.inArray(obj.jobName,judgeJobNames)>-1){
//					layer.alert(obj.jobName+'已重复请修改', {
//	                    icon: 2,
//	                    title: "提示信息"
//	                });
//					return false ;
					flagDeploy=flagDeploy+obj.jobName+",";
				}else{
				judgeJobNames.push(obj.jobName);
				}
				obj.jobType=2;
				list2.push( obj );
			})(i); 
		} 
		list2=JSON.stringify(list2);
		data={
	    		"id":id,
	    		"systemName":$("#microServiceName").val(),
            	"systemCode":$("#microServiceNum").val(),
	    		"groupId":groupId,
	    		"artifactId":artifactId,
	    		/*"systemScm":list, 
	    		"newSystemScm":changeList,*/
	    		"systemJenkins":list2,
	    		"snapshotRepositoryName":snapshotRepositoryName,
	    		"releaseRepositoryName":releaseRepositoryName,
	    		"architectureType":architectureId,	    		
	    		"deployType":deployType,
	    		"productionDeployType":productionDeployType, 		    		
	    		"createType":createType,
	    		"createTypes": createType,
	    		"buildType": buildType,
	    		"codeReviewStatus":codeReviewStatus,
	    		"jdkVersion":jdkVersion,
	    		"buildToolVersion":buildToolVersion,
	    		"packageSuffix":packageSuffix,
	    		"developmentMode":developMode,
	    		"compileCommand":editCompileCommand,
	    		"codeReviewUserIds":codeReviewUserIds,
	    		"sonarScanStatus":sonarScanStatus
	    };
	} 
	if(flagManual!="" || flagDeploy!=""){
		layer.alert(flagManual+flagDeploy+'已重复请修改', {
	        icon: 2,
	        title: "提示信息"
		});
		$("#loading").css('display','none');
		return false;
	}
	$("#loading").css('display','block'); 
	
	$.ajax({
  		url:"/devManage/systeminfo/updateSystemInfo",
    	method:"post", 
    	data: data,
  		success:function(data){ 
  			$("#loading").css('display','none');
  			if(data.status == "repeat"){ 
  				//alert("源码配置环境类型重复，请修改！！！");
  				layer.alert('源码配置环境类型重复，请修改！！', {
                    icon: 2,
                    title: "提示信息"
                });
  			}else if(data.status == 2){
  				//alert("保存失败！！！");
  				layer.alert('保存失败，原因：'+data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
  			}
  			else if(data.status == "rejobName"){
  				layer.alert(data.message, {
                    icon: 2,
                    title: "提示信息"
                });
  				//alert(data.message);
  			}else if(data.status = 1){
  				//alert("配置成功！！！");
  				layer.alert('配置成功', {
                    icon: 1,
                    title: "提示信息"
                });
  				pageInit();
  				$("#microService").modal("hide");  
  			}
        },
  		error:function(){ 
  		}
 	});   
} 
function warningShow( warningFont ){
	layer.alert('源码管理配置中的'+warningFont+'不能为空', {
        icon: 2, 
    });  
}
//微服务配置提交
function microChildServiceCommit(){
	$('#normalServiceForm').data('bootstrapValidator').validate();  
	if( !$('#normalServiceForm').data("bootstrapValidator").isValid() ){
		return;
	}
	var id=$("#microChildServiceID").val();
	var systemId = $("#microChildSystemId").val();
	var addmoduleName = $("#microChildServiceName").val();
	var addmoduleCode = $("#microChildServiceNum").val();
	var buildDependency = $("#normalBuildDependency").val();
	var buildDependencySequence = $("#normalBuildDependencySequence").val();
	
	var buildType=$("#childBuildingTool input[name='tools']:checked").val();
	var groupId='';
	var artifactId='';
	var jdkVersion='';
	if( buildType==1 ){
		groupId=$("#childGroupID").val();
		artifactId=$("#childArtifactID").val();
		jdkVersion=$("#childJdkVersion").val();
	}else if( buildType==2 ){
		jdkVersion=$("#childJdkVersion").val();
	} 

	var relativePath=$("#relativePath").val();
	if(relativePath==""){
		layer.alert('请填写编译相对路径', {
			icon: 2,
			title: "提示信息"
		});
		return;
	}

	var releaseRepositoryName = $("#childReleaseRepositoryName").val();
	var snapshotRepositoryName = $("#childSnapshotRepositoryName").val();
	var buildToolVersion = $("#childBuildToolVersion").val();
	var packageSuffix = $("#childPackageSuffix").val();
	var compileCommand = $("#childCompileCommand").val();
	var moduleArr = [];
	var fflag=false;
	for(var i=0;i<$(".environmentStatus").length;i++){
		(function (i){
			if( $(".environmentStatus").eq(i).find("input[type='checkbox']").is(':checked') ){   
				var scmid={};
				scmid.systemScmId =  $( $(".environmentStatus").eq(i).find("input[name='systemScmneme']")).eq(0).attr('val');
				if($( $(".environmentStatus").eq(i).find("input[name='moduleScmNeme']")).eq(0).attr('value')!=''){
					scmid.id = $( $(".environmentStatus").eq(i).find("input[name='moduleScmNeme']")).eq(0).attr('value') ;
				} 
				moduleArr.push(scmid);
			}
		})(i);  
	}
	moduleArr=JSON.stringify(moduleArr);  
	$("#loading").css('display','block');
	$.ajax({
  		url:"/devManage/systeminfo/updateSystemModuleScm",
    	method:"post", 
    	data:{
    		 "systemId":systemId,
    		 "id":id,
    		 "moduleName":addmoduleName,
    		 "moduleCode":addmoduleCode,
    		 "systemModuleScms":moduleArr,
    		 "buildDependency":buildDependency,
    		 "buildDependencySequence":buildDependencySequence,
    		 "buildType":buildType,
 			"releaseRepositoryName": releaseRepositoryName,
 			"snapshotRepositoryName": snapshotRepositoryName,
 			"buildToolVersion": buildToolVersion,
 			"packageSuffix": packageSuffix,
 			"jdkVersion": jdkVersion,
 			"groupId": groupId,
 			"artifactId": artifactId,
 			"compileCommand":compileCommand,
			"relativePath":relativePath
    	},
  		success:function(data){ 
  			$("#loading").css('display','none');
  			if(data.status == "success"){
  				layer.alert('配置成功', {
                    icon: 1,
                    title: "提示信息"
                });
  				pageInit();
  	  			$("#normalService").modal("hide"); 
  			}
  			if(data.status == "fail"){
  				layer.alert('配置失败', {
                    icon: 2,
                    title: "提示信息"
                });
  			}
  			
        },
  		error:function(){ 
  		}
 	});
}

//清空搜索内容
function clearSearch() {
    $('#systemName').val("");
    $('#systemCode').val("");
    $("#architectureType").selectpicker('val', '');
    $("#status").selectpicker('val', '');
}
 
//清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}
function getToolId( type,i ){
	
	$.ajax({
  		url:"/devManage/systeminfo/getToolByType",
    	method:"post",
    	async: true, 
  		success:function(data1){ 
//  			scmI = i;  
  			 if( typeof(i) === 'undefined'  ){  
//  				$(type).parent().parent().parent().next().children('.search_input').children('div').children('select').empty();
//  				$(type).parent().parent().parent().next().children('.search_input').children('div').children('select').append('<option value="">请选择</option>');
//  				
//  				for( var j=0;j<data1.toolList.length;j++ ){
//  					if(  data1.toolList[j].toolType == $( type ).val()  ){
//  						var obj='<option value='+data1.toolList[j].id+'>'+data1.toolList[j].toolName+'</option>';
//  	  	  				$(type).parent().parent().parent().next().children('.search_input').children('div').children('select').append(obj);
//  					}
//  				}
  				var str ='';
  				$(".abc").find("select" ).empty();
  				for(var j=0;j<data1.toolList.length;j++){  
  					if(  data1.toolList[j].toolType == type.options[type.options.selectedIndex].value  ){ 
  						 
  						var obj='<option value='+data1.toolList[j].id+'>'+data1.toolList[j].toolName+'</option>';
  	  	  				$(".abc").find("select" ).append(obj);
  						str += obj;
  					} 
  	  			}  
  				toolData=str;  
  				$('.selectpicker').selectpicker('refresh');
  			 } else{ 
//  				var str ='';
//  				$(".abc").find("select" ).empty();
  				
  				for(var j=0;j<data1.toolList.length;j++){  
  					if(  data1.toolList[j].toolType == type.scmType  ){   
  						 if(data1.toolList[j].id == type.toolId){ 
  							var obj='<option value='+data1.toolList[j].id+' selected  >'+data1.toolList[j].toolName+'</option>'; 
//	  						str += obj;
  						 }else{
  							var obj='<option value='+data1.toolList[j].id+'>'+data1.toolList[j].toolName+'</option>'; 
  						 }
  						$(".abc").find("select" ).eq(i).append(obj);
  					} 
  	  			}  
//  				toolData=str;  
  				$('.selectpicker').selectpicker('refresh');
  			  
        }
  		},
  		error:function(){ 
  		}
 	});
	
	if($("#scmTypeName").val()!=''){
		$(".def_btnGroup").css("display","block");
		$(".dataEntry").css("display","block");
	}else {
		$(".def_btnGroup").css("display","none");
		$(".dataEntry").css("display","none");
	}
}  
function getToolId2( type){
	console.log(type)
	$.ajax({
		url:"/devManage/systeminfo/getToolByType",
		method:"post",
		async: true, 
		success:function(data1){
			var str ='';
				for(var j=0;j<data1.toolList.length;j++){  
					if(  data1.toolList[j].toolType == type  ){ 
						 
						var obj='<option value='+data1.toolList[j].id+'>'+data1.toolList[j].toolName+'</option>';
	  	  				$(".abc").find("select" ).append(obj);
						str += obj;
					} 
	  			}  
				toolData=str;  
				$('.selectpicker').selectpicker('refresh');
		},
		error:function(){ 
		}
	});
	
}  
//显示新建人员Model
function workSuccess() {
    $("#workSuccess2").modal("show");
}

//新增环境。作废
function addDataEntry() { 
	
	$(".dataEntry").append('<div class="singleData"><input type="hidden" name="systemScmNeme" value=""/><div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="environmentTypeName"> <option value="">请选择</option> '+environmentTypeStr+' </select> </div> </div>'+
//				'<div class="singleDataDiv"> <div class="search_input entrySelect"> <select class="selectpicker microServiceScmType" onchange="getToolId( this )"  name="scmTypeName"> <option value="">请选择</option> <option value="1">Git</option> <option value="2">SVN</option> </select> </div> </div>'+
					'<div class="singleDataDiv"> <div class="search_input entrySelect"> <select class="selectpicker microServiceScmType abc"  name="toolId">'+toolData+'</select> </div> </div>'+
					'<div class="singleDataDiv"> <div class="search_input entrySelect"> <input type="text" class="form-control microServiceScmBranch" placeholder="请输入" /> </div> </div>'+
					'<div class="singleDataDiv"> <div class="search_input entrySelect"> <input type="text" class="form-control microServiceScmUrl" placeholder="请输入" /> </div> </div> '+
					'<div class="delSingData"> <a class="delFont"   onclick="delSingData(this)">删除</a> </div> </div>')
    $('.selectpicker').selectpicker('refresh');
	inputClear();
	
}

//作废
function getSearchData(){
	$.ajax({
		url:'/devManage/systeminfo/getSearchData',
		dataType:'json',
		type:'post',
		success:function(data){ 
			$("#architectureType").empty();
			$("#architectureType").append('<option value="">请选择</option> ');
			for(var i = 0; i < data.dics.length; i++) {
                var statusCode = data.dics[i].valueCode;
                var statusName = data.dics[i].valueName;
                var opt = "<option value='" + statusCode + "'>" + statusName + "</option>";
                $("#architectureType").append(opt);
            }
            $('.selectpicker').selectpicker('refresh'); 
		}
	})
}

function delSingData(This) {
	layer.confirm('你确定要删除这条记录吗？', {
		icon: 0,	
		btn: ['确定', '取消'] //可以无限个按钮 
	}, function(index, layero){
		$(This).parent().parent().remove();
		layer.closeAll();
	}, function(index){ 
		layer.closeAll();
	});
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

function showCompilePack( This ){
	if( $( This ).val()==1 ){
		
	}
}

//任务创建方式,radiobox切换显示
function buildingWay(This){
	if( $(This).val() == "1"){ 
		$(".manualInfo").css("display","none");
	}else if( $(This).val() == "2"){ 
		$(".manualInfo").css("display","block");
	}else{
		$(".autoInfo").css("display","none");
		$(".manualInfo").css("display","none");
	}
}

//手动创建，删除job任务时触发
function delJenkinsJob(This){ 
	if( $( This ).parent().parent().children(".hideKeyId").val()=='' ){ 
		$( This ).parent().parent().remove();
	}else{
		var id= $( This ).parent().parent().children(".hideKeyId").val().toString() ;  
		var JobName=$( This ).parent().parent().find("input[name*='JobName']").val();
		$("#promptBox .modal-footer2").empty();
		$("#promptBox .modal-footer2").append('<button type="button" class="btn btn-primary" onclick="confirmDel(\''+id+'\')">确定</button><button type="button" class="btn btn-default" data-dismiss="modal">取消</button>')
		 
		$("#promptBoxSysName").html( JobName );
		$("#promptBox").modal("show");  
	}
	 
}
//手动创建时，确定删除jenkins的Job后，删除job任务
function confirmDel(  id ) {    
	$.ajax({
		url:'/devManage/systeminfo/deleteSystemJenkinsManual',
		dataType:'json',
		type:'post',
		data:{
   		 "id": id, 
		},
		success:function(data){   
			$(".manualInfo input[value*='"+id+"']").parent().remove();
			$("#promptBox").modal("hide");
		}
	}) 
}

//手动创建时，生成三方jenkins的job表单信息
function addJenkinsJob(This){
	$.ajax({
		url:'/devManage/systeminfo/getToolinfoType',
		dataType:'json',
		type:'post',
		data:{
   		 "type":4, 
		},
		success:function(data){
			var str='<div class="JenkinsJob rowdiv"><input type="hidden" class="hideKeyId" value="" />'+
 			'<div class="form-group def_col_10"><div class="JobName font_right fontWeihgt def_col_16">Jenkins工具地址：</div>'+
 			'<div class="JobName def_col_16"><select class="selectpicker JenkinsTooladdress" name="JenkinsTooladdress"><option value="">请选择</option>';
			for(var i=0;i< data.toolList.length ;i++){
				str+='<option value='+data.toolList[i].id+' >'+data.toolList[i].toolName+'</option>';
			}
			str+='</select></div></div><div class="form-group def_col_10"><div class="JobName font_right fontWeihgt def_col_16">Jenkins Job Name：</div>'+
 			'<div class="JobName def_col_16"><input type="text" class="form-control" placeholder="请输入" name="JobName"/></div></div>'+
 			'<div class="form-group def_col_10"><div class="JobName font_right fontWeihgt def_col_16">Jenkins Job Path：</div>'+
 			'<div class="JobPath def_col_16"><input type="text" class="form-control" placeholder="请输入" name="JobPath"/></div></div>'+
 			'<div class="JobNameDel fontWeihgt def_col_3"><a onclick="delJenkinsJob(this)">删除</a></div></div>';
 			$( This ).parent().next().append( str );
			$('.selectpicker').selectpicker('refresh'); 
			addValShow();
		}
	}) 
}

//新建系统弹框显示
function newSystem_btn(){
	layer.open({
	      type: 2,
	      title:  '新建系统',
	      shadeClose: true,
	      shade: false, 
	      move: false,
	      area: ['100%', '100%'], 
	      id: "1",
	      tipsMore: true,
	      anim: 2,
	      content:'/devManageui/systeminfo/toSystemDeploy?id=,createType=,systemId=,artifactId=,buildDependency=,buildDependencySequence=',
	      btn: ['确定','取消'] ,
	      btnAlign: 'c', //按钮居中
	      yes: function(index , layero){
	    	  var body = layer.getChildFrame('body', index );
	    	  var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
	          iframeWin.submit(); 
	      },
	      no:function(){
	    	  layer.closeAll(); 
		 } 
	}); 
	return ; 
}

//编辑系统模块显示
function edit_system(row){
	$('#editSystem_id').val(row.id);
	$('#addSystemCode').val(row.systemCode);
	$('#addSystemName').val(row.systemName);
	
	$('#addSystem').modal('show');
}

//添加系统
function addSystem(){
    $("#addSyatemForm").data('bootstrapValidator').validate();
    if ( !$("#addSyatemForm").data('bootstrapValidator').isValid() ) {
        return;
    }
    $("#loading").css('display','block');
    $.ajax({
        url:"/devManage/systeminfo/updateTopSystem",
        dataType: 'json',
        type:'post',
        data:{
            "id":$("#editSystem_id").val(),
            "systemName":$.trim($("#addSystemName").val()),
            "systemCode":$.trim($("#addSystemCode").val())
        },
        success:function(data){
           if(data.status == 2){
               layer.alert(data.errorMessage, {
                   icon: 2,
                   title: "提示信息"
               });
           } else {
        		pageInit();
                layer.alert("修改成功", {
                    icon: 1,
                    title: "提示信息"
                });
                $("#addSystem").modal("hide");
            }
            $("#loading").css('display','none');
        },
        error:function(){
            $("#loading").css('display','none');
            layer.alert(errorDefect, {
                icon: 2,
                title: "提示信息"
            });
        }
    })
}
//表单校验
 function formValidator(){
	 $('#addSyatemForm').bootstrapValidator({
         message: 'This value is not valid',//通用的验证失败消息
         feedbackIcons: {//根据验证结果显示的各种图标
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
             addSystemName: {
                 validators: {
                     notEmpty: {
                         message: '系统名称不能为空'
                     } ,
                     stringLength: {
                         max:100,
                         message: '系统名称长度必须小于100字符'
                     }
                 }
             },
             addSystemCode: {
                 validators: {
                     notEmpty: {
                         message: '系统编号不能为空'
                     } ,
                     stringLength: {
                         max:100,
                         message: '系统编号长度必须小于100字符'
                     },
                     regexp: {
                         regexp: /^[-_/.0-9a-zA-Z]+$/,
                         message: '只能输入数字、英文或-_/.'
                     }
                 }
             }
         }
     });
    $('#newform').bootstrapValidator({
　　　　  message: 'This value is not valid',//通用的验证失败消息
　	    feedbackIcons: {//根据验证结果显示的各种图标
　　　　　　　　valid: 'glyphicon glyphicon-ok',
　　　　　　　　invalid: 'glyphicon glyphicon-remove',
　　　　　　　　validating: 'glyphicon glyphicon-refresh'
　　　　  },
        fields: {
			moduleName: {
			    validators: {
			        notEmpty: {
			            message: '模块名称不能为空'
			        } ,
			        stringLength: {
                       max:100,
                       message: '模块名称长度必须小于100字符'
                    } 
			    }
			},
			moduleCode: {
				validators: {
					notEmpty: {
						message: '模块编号不能为空'
			        } ,
			        stringLength: {
                       max:100,
                       message: '模块编号长度必须小于100字符'
                    },
                    regexp: {            
                       regexp: /^[-_/.0-9a-zA-Z]+$/,
                       message: '只能输入数字、英文或-_/.'
                    } 
			    }
			},
			addBuildDependency: {
	    		   validators: {
	    			   notEmpty: {
	    				   message: '请选择构建依赖'
		               } 
		    	   }
		     }
        }
    });
    $('#configInfoForm').bootstrapValidator({
    　　　  message: 'This value is not valid',//通用的验证失败消息
    　		feedbackIcons: {//根据验证结果显示的各种图标
    　　　　　　　　valid: 'glyphicon glyphicon-ok',
    　　　　　　　　invalid: 'glyphicon glyphicon-remove',
    　　　　　　　　validating: 'glyphicon glyphicon-refresh'
    　　　},
       fields: {
    	   microServiceName: {
               validators: {
                   notEmpty: {
                       message: '系统名称不能为空'
                   } ,
                   stringLength: {
                       max:100,
                       message: '系统名称长度必须小于100字符'
                   }
               }
           },
           microServiceNum: {
               validators: {
                   notEmpty: {
                       message: '系统编号不能为空'
                   } ,
                   stringLength: {
                       max:100,
                       message: '系统编号长度必须小于100字符'
                   },
                   regexp: {
                       regexp: /^[-_/.0-9a-zA-Z]+$/,
                       message: '只能输入数字、英文或-_/.'
                   }
               }
           },
//    	   groupID: {
//    		   validators: {
//    			   notEmpty: {
//    				   message: 'GroupID不能为空'
//	               },
//	               stringLength: {
//                       max:50,
//                       message: 'GroupID长度必须小于50字符'
//                   },
//                   regexp: {            
//                       regexp: /^[-_/.0-9a-zA-Z]+$/,
//                       message: '只能输入数字、英文或-_/.'
//                   }, 
//	    	   }
//	       },
//	       artifactID: {
//	    	   validators: {
//	    		   notEmpty: {
//	    			   message: 'ArtifactID不能为空'
//	               },
//	               stringLength: {
//                       max:50,
//                       message: 'ArtifactID长度必须小于50字符'
//                   },
//                   regexp: {            
//                       regexp: /^[-_/.0-9a-zA-Z]+$/,
//                       message: '只能输入数字、英文或-_/.'
//                   }, 
//	           }
//	       },
	       architectureId: {
    		   validators: {
    			   notEmpty: {
    				   message: '请选择系统架构'
	               } 
	    	   }
	       },
	       deployType: {
    		   validators: {
    			   notEmpty: {
    				   message: '请选择部署方式'
	               } 
	    	   }
	       },
	       productionDeployType: {
    		   validators: {
    			   notEmpty: {
    				   message: '请选择生产环境部署方式'
	               } 
	    	   }
	       },
	       developMode: {
    		   validators: {
    			   notEmpty: {
    				   message: '请选择开发模式'
	               } 
	    	   }
	       },
	       snapshotRepositoryName: {
    		   validators: {
    			   stringLength: {
                       max:50,
                       message: '快照仓库名称长度必须小于50字符'
                   }, 
	    	   }
	       },
	       releaseRepositoryName: {
    		   validators: {
    			   stringLength: {
                       max:50,
                       message: '发布仓库名称长度必须小于50字符'
                   }, 
	    	   }
	       },
       }
    }); 
    $("#normalServiceForm").bootstrapValidator({ 
　	    feedbackIcons: {//根据验证结果显示的各种图标
　　　　　　　　valid: 'glyphicon glyphicon-ok',
　　　　　　　　invalid: 'glyphicon glyphicon-remove',
　　　　　　　　validating: 'glyphicon glyphicon-refresh'
　　　　  },
        fields: {
        	microChildServiceName: {
			    validators: {
			        notEmpty: {
			            message: '模块名称不能为空'
			        } ,
			        stringLength: {
                       max:100,
                       message: '模块名称长度必须小于100字符'
                    } 
			    }
			},
			microChildServiceNum: {
				validators: {
					notEmpty: {
						message: '模块编号不能为空'
			        } ,
			        stringLength: {
                       max:100,
                       message: '模块编号长度必须小于100字符'
                    },
                    regexp: {            
                        regexp: /^[-_/.0-9a-zA-Z]+$/,
                        message: '只能输入数字、英文或-_/.'
                    }
			    }
			},
			normalArtifactID: {
				validators: {
					notEmpty: {
						message: 'ArtifactID不能为空'
			        } ,
			        stringLength: {
                       max:100,
                       message: 'ArtifactID长度必须小于50字符'
                    },
                    regexp: {            
                       regexp: /^[-_/.0-9a-zA-Z]+$/,
                       message: '只能输入数字、英文或-_/.'
                    } 
			    }
			} 
        }
    });
    $("#deployInfoForm").bootstrapValidator({ 
　	    feedbackIcons: {//根据验证结果显示的各种图标
　	　　　　　　　　valid: 'glyphicon glyphicon-ok',
　	　　　　　　　　invalid: 'glyphicon glyphicon-remove',
　	　　　　　　　　validating: 'glyphicon glyphicon-refresh'
　	　　　　  },
　	        fields: {
　	        	deployPath: {
　				    validators: {
　				        notEmpty: {
　				            message: '部署路径不能为空'
　				        } ,
　				        stringLength: {
　	                       max:100,
　	                       message: '部署路径长度必须小于200字符'
　	                    } 
　				    }
　				},
　				timeOut: {
　					validators: {
　						notEmpty: {
　							message: '超时设置不能为空'
　				        }, 
　	                    regexp: {            
　	                       regexp: /^(0|\+?[1-9][0-9]*)$/,
　	                       message: '只能输入大于等于0的整数'
　	                    } 
　				    }
　				},
　				retryNumber: {
　					validators: {
　						notEmpty: {
　							message: '重试次数不能为空'
　				        }, 
　	                    regexp: {            
　	                       regexp: /^(0|\+?[1-9][0-9]*)$/,
　	                       message: '只能输入大于等于0的整数'
　	                    },
	                    stringLength : {
                            max : 2,
                            message : '长度2位字符'
                        }
　				    }
　				},
				deploySequence: {
					validators: {
	                    regexp: {            
	                       regexp: /^(0|\+?[1-9][0-9]*)$/,
	                       message: '只能输入大于等于0的整数'
	                    },
	                    stringLength : {
                            max : 2,
                            message : '长度2位字符'
                        }
				    }
				}
　	        }
　	    }); 
}
function addValShow() {
	$('#buildingForm').bootstrapValidator("addField","JenkinsTooladdress",{
		feedbackIcons: {//根据验证结果显示的各种图标
    　　　　　　　　valid: 'glyphicon glyphicon-ok',
    　　　　　　　　invalid: 'glyphicon glyphicon-remove',
    　　　　　　　　validating: 'glyphicon glyphicon-refresh'
    　　　},
		validators: {
		        notEmpty: {
		            message: '请选择Jenkins工具地址'
		        },
		        stringLength: {
		        	max:20,
		        	message: 'Jenkins工具地址长度必须小于20字符'
		        }
		}
	})
	$('#buildingForm').bootstrapValidator("addField","JobName",{  
		feedbackIcons: {//根据验证结果显示的各种图标
    　　　　　　　　valid: 'glyphicon glyphicon-ok',
    　　　　　　　　invalid: 'glyphicon glyphicon-remove',
    　　　　　　　　validating: 'glyphicon glyphicon-refresh'
    　　　},
		validators: {
			notEmpty: {
				message: 'Jenkins Job Name不能为空'
	        },
	        stringLength: {
	        	max:50,
	        	message: 'Jenkins Job Name长度必须小于50字符'
	        }, 
	    }
	})	
}

// 为空或者未定义或者为NULL或者是字符串去空格
function toStr(value1){
	if(value1==undefined ||value1==null||value1=="null"||value1=="NULL"){
 		return "";
 	}else if(!isNaN(value1)){
 		return value1;
 	}else{
	   return value1.toString().trim();
 	}
}
 
function inputClear(){
    $('.microServiceData input[type="text"]').parent().css("position","relative"); 
    if(  $('.microServiceData input[type="text"]').parent().children( "span[class*='btn_clear']" ),length  == 0  ){
    	$('.microServiceData input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    }  
} 
//数组查重s
function isRepeat(arr) {
   var hash = {};
   for(var i in arr) {
       if(hash[arr[i]])
       {
           return true;
       }
       // 不存在该元素，则赋值为true，可以赋任意值，相应的修改if判断条件即可
       hash[arr[i]] = true;
    }
   return false;
}
//源码配置
function codeConfig( id , type ,name){ 
	layer.open({
	      type: 2,
	      title:  '源码配置 : '+ name ,
	      shadeClose: true,
	      shade: false, 
	      move: false,
	      area: ['100%', '100%'], 
	      id: "1",
	      tipsMore: true,
	      anim: 2,
	      content:  '/devManageui/systeminfo/toSystemScm?id='+id+',architectureType='+type,
	      btn: ['确定','取消'] ,
	      btnAlign: 'c', //按钮居中
	      yes: function(index , layero){
	    	  var body = layer.getChildFrame('body', index );
	    	  var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
	          iframeWin.submit(); 
	      },
	      no:function(){
	    	  layer.closeAll(); 
		 } 
	    }); 
}
function functionOpt(){
	pageInit();
}
