/*测试数据
 * var onedata={"systemInfo":{"id":"185","status":1,"createBy":1,"createDate":"2019-08-23 18:01:39","lastUpdateBy":1,"lastUpdateDate":1566554499000,"projectId":null,"projectName":null,"systemName":"zyx1234","systemCode":"zyx1234","systemShortName":null,"sonarScanStatus":2,"jdkVersion":null,"developmentMode":null,"codeReviewUserIds":null,"compileCommand":"","buildToolVersion":null,"packageSuffix":null,"systemType":null,"systemTypeList":null,"codeReviewUserName":null,"groupId":"zyx123GroupID","artifactId":"123ArtifactID","architectureType":1,"snapshotRepositoryName":"zyx123","releaseRepositoryName":"zyx123","scmStrategy":null,"deployType":2,"productionDeployType":1,"codeReviewStatus":2,"buildType":1,"createType":1,"systemFlag":null,"environmentType":null},"status":1}
var childData = {"id":"112","status":1,"createBy":1,"createDate":"2019-08-26 13:51:47","lastUpdateBy":1,"lastUpdateDate":1566798707000,"systemId":185,"moduleName":"childModal1","moduleCode":"num1","buildDependency":1,"buildDependencySequence":null,"dependencySystemModuleIds":null,"groupId":null,"artifactId":"1","compileCommand":"1","buildType":1,"snapshotRepositoryName":"1","releaseRepositoryName":"1","buildToolVersion":"1.5.4","packageSuffix":"war","jdkVersion":"jdk7","relativePath":"1","firstCompileFlag":1,"systemModuleFlag":null}
*/ 
var globalData = {
		onedata: {},
		childArr: [], //子系统数据
		dataBaseArr: [], // 当前系统下配置数据库
		envType:[], // 环境配置 数据
		codeConfigStatus: false,
		autoDeployStatus: false,
		autoTestStatus: false
}
$(document).ready(function(){ 
	var step1 = new SetStep({
		skin:1,
		content: '.stepCont1',
		showBtn: false, 
		description: ['新建及配置系统', '新建及配置子模块', '新建源代码库', '环境配置', '系统/环境/源码库配置', '自动化部署配置', '自动化测试配置', '完成'],
		showBtn:true,
	})
	//第一步按钮，新建构建绑定方法
	$("#startBuild").bind("click",function(){ 
		var id= $("#sysTemId").val();  
		layer.open({
		      type: 2,
		      title:  '新建',
		      shadeClose: true,
		      shade: false, 
		      move: false,
		      area: ['100%', '100%'], 
		      id: "1",
		      tipsMore: true,
		      anim: 2,
		      content:"/devManageui/systeminfo/toSystemDeploy?id="+ id +",createType=,systemId=,artifactId=,buildDependency=,buildDependencySequence=",
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
	});
	//第一步按钮，选择已有系统
	$("#chooseSystem").bind("click",function(){ 
		$("#selSys").modal("show");
	   	clearSearchSys();
	   	systemTableShowAll();
	})
	//选择系统页面搜索功能
	$("#systemSearch").bind("click",function(){  
	   	systemTableShowAll();
	})
	//第二步按钮，新建及配置子模块方法
	$("#startChildBuild").bind("click",function(){ 
		var id= $("#sysTemId").val();  
		layer.open({
		      type: 2,
		      title: '新增子模块',
		      shadeClose: true,
		      shade: false, 
		      move: false,
		      area: ['100%', '100%'], 
		      id: "1",
		      tipsMore: true,
		      anim: 2,
		      content:'/devManageui/systeminfo/toAddSystemModel?id='+ id,
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
	});
	
	//第三步按钮,构建代码库
	$("#buildCodeBase").bind("click",function(){ 
		$("#codeBaseType input[type=radio]").eq( 0 ).prop( "checked", "true" ) 
		$("#microServiceReleaseRepositoryName").val("")
		$.ajax({
	  		url:"/devManage/version/getCodeBaseAddresses",
	    	method:"post",  
	    	data:{codeBaseType: 1}, 
	  		success:function(data){  
	  			$("#codeBaseAddr").empty() 
	  			var options = '<option value="">请选择</option>'; 
	            for (var i in data) {
	                options += '<option value="' + data[i].id + '" ip="' + data[i].ip + '">' + data[i].toolName + '(' + data[i].ip + ')</option>';
	            }
	            $("#codeBaseAddr").append(options);
	            $(".selectpicker").selectpicker('refresh');
	        }, 
	 	});  
		$(".selectpicker").selectpicker('refresh'); 
		$("#createCodeBaseModel").modal("show");  
	}); 
	$("#codeBaseType input[type=radio]").change(function() {
		var data = {};
		if (this.value == '1') {
			data = { codeBaseType: 1 };
		}else if(this.value == '2') {
			data = { codeBaseType: 2 }; 
		}
		$.ajax({
	  		url:"/devManage/version/getCodeBaseAddresses",
	    	method:"post",  
	    	data:data, 
	  		success:function(data){  
	  			$("#codeBaseAddr").empty() 
	  			var options = '<option value="">请选择</option>'; 
	            for (var i in data) {
	                options += '<option value="' + data[i].id + '" ip="' + data[i].ip + '">' + data[i].toolName + '(' + data[i].ip + ')</option>';
	            }
	            $("#codeBaseAddr").append(options);
	            $(".selectpicker").selectpicker('refresh');
	        }, 
	 	});  
	})
	
	//第四步按钮 环境配置
	$( "#environmentConfig" ).bind("click",function(){     
		$.ajax({
	  		url:"/devManage/systeminfo/findEnvIds",
	    	method:"post",  
	    	data:{
	    		"id": $("#sysTemId").val()
			}, 
	  		success:function(data){  
	  			envIds = data.data; 
	  			findEnvironment( data.data );
	        }, 
	 	}); 
	});
	
	//第五步按钮 源码配置
	$("#codeConfig").bind("click",function(){ 
		var id= $("#sysTemId").val();  
		var type= $("#architectureType").val();  
		//源码配置
		layer.open({
		      type: 2,
		      title:  '源码配置 ',
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
	}); 
	
	//第六步按钮 自动化部署配置
	$("#autoDeployConfigBtn").bind("click",function(){ 
		var id= $("#sysTemId").val();  
		var type= $("#architectureType").val();  
		layer.open({
		      type: 2,
		      title: "自动化部署配置",
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
		      content:'/devManageui/systeminfo/toDeploymentConfig?,id='+ id +',architectureType='+type,
		      btn: ['保存','关闭'] ,
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
	}); 
	
	//第七步按钮  自动化测试配置
	$("#autoTestConfigBtn").bind("click",function(){ 
		var id= $("#sysTemId").val();  
		var name= $("#sysTemId").attr("name");  
		var type= $("#architectureType").val();  
		layer.open({
		      type: 2,
		      title: "自动化测试配置",
		      shadeClose: true,
		      shade: false, 
		      move: false,
		      area: ['100%', '100%'], 
		      id: "1",
		      tipsMore: true, 
		      anim: 2,
		      content:'/devManageui/systeminfo/toAutomatedTesting?,id='+id+',architectureType='+type+',systemName='+name,
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
	});  
	 //测试
	/*callbackFirst( onedata ); */
	/*addCodeBaseModal( childData )*/
}); 

//子页面 执行成功统一调用这个方法，根据第二个参数判断执行回调的方法
//  num 的值：      1为  新建系统配置  成功
//            2为  新建子系统配置  成功
function functionOpt( data , num ){
	switch( num ) {
	    case 1:
	        //新建系统配置成功
	        callbackFirst( data );
	        break;
	    case 2:
	    	//新建及配置子模块成功
	        callbackNewChildModal( data );
	        break;
	    case 5:
	    	//系统 环境 源码库配置成功
	        codeConfigSuccess( data );
	        break;
	    case 6:
	    	//自动化部署配置成功
	        autoDeploySuccess( data );
	        break;
	    case 7:
	    	//自动化部署配置成功
	        autoTestSuccess( data );
	        break;
	    default:
	    	break;
	}  
} 
//新建系统配置成功
function callbackFirst( data ){   
	
	$("#sysTemId").val( data.systemInfo.id ); 
	$("#sysTemId").attr("name", data.systemInfo.systemName ); 
	$("#architectureType").val( data.systemInfo.architectureType ); 
	$("#createType").val( data.systemInfo.createType );
	$("#startBuild").text( "点击开始修改" )
	$('#page1 .successP').css( "display","inline-block" );  //成功的字样和图片
	
}
//新建及配置子模块成功
function callbackNewChildModal( data ){  
	globalData.childArr.push( data ); 
	var obj1 = {
		moduleName:data.moduleName,
		moduleCode:data.moduleCode, 
		id:data.id,
		systemId:data.systemId,
		createType:$("#createType").val(),
		buildDependency:data.buildDependency,
		buildDependencySequence:data.buildDependency, 
	};
	var obj2 = {
			moduleName:data.moduleName,
			moduleCode:data.moduleCode, 
			id:data.id,
			systemId:data.systemId, 
	};
	obj1 = JSON.stringify( obj1 ).replace(/"/g, '&quot;'); 
	obj2 = JSON.stringify( obj2 ).replace(/"/g, '&quot;'); 
	var str = '<div class="def_col_12" title="'+ data.moduleName +'（'+ data.moduleCode +'） ">'+
						'<span class="icon_childModal"></span>'+ 
						data.moduleName +'（'+ data.moduleCode +'）'+
						'<div class="reviseChild">'+
							'<a class="a_style" onclick="changeChildModal( '+ obj1 +',this )" >修改</a>'+
							'<a class="a_style" onclick="delChildModal( '+ obj2 +',this )" >删除</a>'+
						'</div> '+
					'</div>';
	$( "#childModalDiv" ).append( str );
	$('#page2 .successP').css( "display","inline-block" );
	$('#page2 .childModalDiv').css( "display","block" );
}
//源码配置成功之后的操作
function codeConfigSuccess(data){ 
	globalData.codeConfigStatus = true;
	$('#page5 .successP').css( "display","inline-block" );
	$("#codeConfig").text( "点击修改配置" );
}
//自动化部署配置成功之后的操作
function autoDeploySuccess(data){
	globalData.autoDeployStatus = true; 
	$('#page6 .successP').css( "display","inline-block" );
	$("#autoDeployConfigBtn").text( "点击修改配置" );
}
//自动化测试配置成功之后的操作
function autoTestSuccess( data ){
	globalData.autoTestStatus = true;
	$('#page7 .successP').css( "display","inline-block" );
	$("#autoTestConfigBtn").text( "点击修改配置" );
}


//子模块修改信息
function changeChildModal(data , This){ 
	layer.open({
	      type: 2,
	      title:  '修改子系统:'+data.moduleCode+ " " +data.moduleName,
	      shadeClose: true,
	      shade: false, 
	      move: false,
	      area: ['100%', '100%'], 
	      id: "1",
	      tipsMore: true,
	      anim: 2,
	      content:"/devManageui/systeminfo/toSystemDeploy?id="+ data.id +",createType="+ data.createType +",systemId="+ data.systemId +",artifactId=,buildDependency="+ data.buildDependency +",buildDependencySequence="+ data.buildDependencySequence ,
	      btn: ['确定','取消'] ,
	      btnAlign: 'c', //按钮居中
	      yes: function(index , layero){
	    	  $( This ).parent().parent().remove();
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
//子模块删除
function delChildModal( obj , This ){  
	layer.confirm('你确定要删除'+obj.moduleName+'（'+obj.moduleCode+'）？', {
	  btn: ['确定','取消'] //按钮
	}, function(){
		layer.closeAll(); 
		$.ajax({
	  		url:"/devManage/systeminfo/deleteSystemModule",
	    	method:"post", 
	    	async:false,
	    	data:{ "id" : obj.id,"systemId" : obj.systemId }, 
	  		success:function(data){   
	  			if(data.status == 1){ 
	  				layer.alert('删除成功!',{
		 				 icon: 1,
		 			})
		 			$( This ).parent().parent().remove(); 
	  				var newArr=[];
	  				for( var i=0;i<globalData.childArr.length;i++ ){ 
	  					if( globalData.childArr[i].id != obj.id ){
	  						newArr.push( globalData.childArr[i] );
	  					}
	  				}
	  				globalData.childArr = newArr; 
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

//新建代码库提交
function submitAddCodeBase(){
	
	var systemId = $( "#sysTemId" ).val();
    var codeBaseType = $("#codeBaseType input[type='radio']:checked").val();
    var toolId = $("#codeBaseAddr").val();
    var codeBaseIp = $('#codeBaseAddr option:selected').attr("ip");
    var codeBaseName = $("#microServiceReleaseRepositoryName").val();
    
    //校验
    if (toolId == "" || toolId == undefined) {
        layer.alert("请选择代码库地址！", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    } 
    if (codeBaseName == "") {
        layer.alert("请填写代码库名称！", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }
    if (!/^[0-9a-zA-Z]+$/.test(codeBaseName)) {
        layer.alert("代码库名称只能由大小写英文和数字构成！", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }
    $.ajax({
        type: "POST",
        url: "/devManage/version/addNewCodeBase",
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        data: {
            systemId:systemId,
            scmType: codeBaseType,
            toolId: toolId,
            repositoryName: codeBaseName,
            ip: codeBaseIp
        },
        beforeSend: function () {
            $("#loading").css('display', 'block');
        },
        success: function (msg) {
            $("#loading").css('display', 'none');
            if (msg.flag) {
                $("#createCodeBaseModel").modal("hide");
                //添加数据库方法先省略
                addCodeBaseModal( msg.data ) 
            } else {
                layer.alert("新增代码库失败,失败原因:" + msg.data.message, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $("#loading").css('display', 'none');
            if (XMLHttpRequest.status != 999) {
                layer.alert("系统异常，请联系管理员", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        }
    }); 
}
//页面添加保存好的数据库(待完善)
function addCodeBaseModal( data ){  
	globalData.dataBaseArr.push( data );
	var obj1 = {
	 
	}; 
	var obj2 = {
			 
	};
	obj1 = JSON.stringify( obj1 ).replace(/"/g, '&quot;');  
	obj2 = JSON.stringify( obj2 ).replace(/"/g, '&quot;');  
	var str = '<div class="def_col_12" title="'+ data.repositoryName +'">'+
						'<span class="icon_database"></span>'+ data.repositoryName+
						/*'<div class="reviseChild">'+
							'<a class="a_style" onclick="changeCodeBase( '+ obj1 +',this )" >修改</a>'+
							'<a class="a_style" onclick="delCodeBase( '+ obj2 +',this )" >删除</a>'+
						'</div> '+*/
					'</div>';
	$( "#codeBaseDiv" ).append( str );
	$('#page3 .successP').css( "display","inline-block" );
	$('#page3 .childModalDiv').css( "display","block" );
}
//修改数据库 （尚未实装）
function changeCodeBase( data , This ){
	console.log( data )
	console.log( This )
}
//删除数据库（尚未实装）
function delCodeBase( data , This ){
	console.log( data )
	console.log( This )
}
 
//环境配置弹窗显示环境   
function findEnvironment(envIds){ 
	$("#environmentTable").bootstrapTable('destroy');
    $("#environmentTable").bootstrapTable({  
    	url:"/devManage/systeminfo/getAllEnvironment",
    	method:"post",
        queryParamsType:"",
        pagination : false,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8', 
        responseHandler:function (res) {
        	var rows=res.list;
            return rows;
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
    $("#environmentModal").modal('show');
} 
//配置环境提交
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
		}
		env=envType.join(',');  
		$.ajax({
	  		url:"/devManage/systeminfo/configEnvironment",
	    	method:"post", 
	    	data:{
	    		"id":$( "#sysTemId" ).val(),
	    		"envType":env
			}, 
	  		success:function(data){  
	  			if(data.status == 1){
	  				layer.alert('配置成功!',{
		 				 icon: 1,
		 			})
		 			globalData.envType = envType; 
		 			$("#page4 .successP").css('display', 'block');
		 			$("#environmentConfig").text( "点击修改配置" );
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

//下一步验证  （ 在setStep.js中被调用  ）
function checkStep( pageCont, curStep, steps ){
	var flag = false;
	switch (curStep) {
	//第一步验证返回 id值是否为空
		case 1:
			if( $( "#sysTemId" ).val() == '' ){
				layer.alert('请先新建系统!', {  icon: 0, title: "提示信息", });
				flag = true;
			}else{  
				//判断 单模块 还是 多模块，用于第二步按钮配置 子模块 是否禁用
				if( $( "#architectureType" ).val() != 1 ){ 
					//第二步无法进行
					$("#startChildBuild").addClass( "disableBtn" );
					$("#startChildBuild").attr('disabled', 'true');
					$("#page2 .remindInfo").css('display', 'block'); 
				}else{ 
					$.ajax({
				  		url:"/devManage/systeminfo/getModulesBySystemId",
				    	method:"post", 
				    	data:{
				    		"systemId":$( "#sysTemId" ).val(), 
						}, 
				  		success:function(data){ 
				  			$( "#childModalDiv" ).empty();
				  			globalData.childArr = [];
				  			$('#page2 .successP').css( "display","none" );
				  			$('#page2 .childModalDiv').css( "display","none" );
				  			for( var i=0;i<data.modules.length;i++ ){
			  					callbackNewChildModal( data.modules[i] );
			  				}
				        }, 
				 	}); 
					
					$("#startChildBuild").removeClass( "disableBtn" )
					$("#startChildBuild").removeAttr('disabled')
					$("#page2 .remindInfo").css('display', 'none'); 
				}  
			}
			break;
		case 2:
			if( $( "#architectureType" ).val() == 1 ){ 
				if( globalData.childArr.length == 0 ){ 
					flag = true;
					layer.alert('没有配置子模块!',{
		 				 icon: 0,
		 			})  
				}else{ 
					getNextStepData( 2 )
				}
			}else {
				getNextStepData( 2 )
			}
			break;
		case 3:
			if( globalData.dataBaseArr.length == 0 ){ 
				flag = true;
				layer.alert('没有新建代码库!',{
	 				 icon: 0,
	 			})  
			}else{ 
				getNextStepData( 3 )
			}
			break;
		case 4:
			if( globalData.envType.length == 0 ){ 
				flag = true;
				layer.alert('没有进行环境配置!',{
	 				 icon: 0,
	 			})  
			}else{
				getNextStepData( 4 )
			}
			break;
		case 5:
			if( globalData.codeConfigStatus == false ){ 
				flag = true;
				layer.alert('没有进行源码配置!',{
	 				 icon: 0,
	 			})  
			}else{
				getNextStepData( 5 )
			}
			break;
		case 6:
			if( $( "#createType" ).val() == 1 ){
				if( globalData.autoDeployStatus == false ){ 
					flag = true;
					layer.alert('没有进行自动化部署配置!',{
		 				 icon: 0,
		 			})  
				}else{
					getNextStepData( 6 )
				}
			} else{
				$("#page7 .successP").css('display', 'none'); 
				$("#autoTestConfigBtn").addClass( "disableBtn" );
				$("#autoTestConfigBtn").attr('disabled', 'true');
				$("#page7 .remindInfo").css('display', 'block');
			}
			break; 
		case 7:
			if( $( "#createType" ).val() == 1 ){
				if( globalData.autoTestStatus == false ){ 
					flag = true;
					layer.alert('没有进行自动化测试配置!',{
		 				 icon: 0,
		 			})  
				}
			}  
			break;
		default:
			break;
	} 
	return flag;
} 

//重置-( 第一步选择系统弹框中的重置按钮   )
function clearSearchSys(){
	$("#SCsystemName").val('');
	$("#SCsystemCode").val('');
	 $(".color1 .btn_clear").css("display","none");
}


function systemTableShowAll(){  
	$("#systemTable").bootstrapTable('destroy');
    $("#systemTable").bootstrapTable({  
    	url:"/devManage/systeminfo/selectAllSystemInfo2",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
        singleSelect : true, 
        queryParams : function(params) {
             var param={ 
            	 systemName:$.trim($("#SCsystemName").val()),
            	 systemCode:$.trim($("#SCsystemCode").val()),
                 pageNumber:params.pageNumber,
                 pageSize:params.pageSize, 
             }
            return param;
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
            field : "systemCode",
            title : "子系统编码",
            align : 'center'
        },{
            field : "systemName",
            title : "子系统名称",
            align : 'center'
        },{
        	field : "systemShortName",
        	title : "子系统简称",
        	align : 'center'
        },{
            field : "projectName",
            title : "所属项目",
            align : 'center'
        }],
        onLoadSuccess:function(){
        	 
        },
        onLoadError : function() {

        } 
    });
}

function commitSys(){
	var selectContent = $("#systemTable").bootstrapTable('getSelections')[0];
	 
    if(typeof(selectContent) == 'undefined') {
   	layer.alert('请选择一列数据！', {icon: 0});
       return false;
    }  
    if( $( "#sysTemId" ).val() != "" ){ 
    	 layer.confirm("系统已存在，继续执行将覆盖原有系统！", {
			  btn: ['确定', '取消'] ,
			  icon:3
			}, function(index, layero){
				$( "#sysTemId" ).val( selectContent.id );
				$( "#sysTemId").attr("name", selectContent.systemName );
			   	$( "#architectureType" ).val( selectContent.architectureType );
			   	$( "#createType" ).val( selectContent.createType );  
				$( "#startBuild").text( "点击开始修改" );
				$( '#page1 .successP').css( "display","inline-block" );
				layer.closeAll();
			}
		)
    }else{
    	$( "#sysTemId" ).val( selectContent.id );
		$( "#sysTemId").attr("name", selectContent.systemName );
	   	$( "#architectureType" ).val( selectContent.architectureType );
	   	$( "#createType" ).val( selectContent.createType );  
		$( "#startBuild").text( "点击开始修改" )
		$( '#page1 .successP').css( "display","inline-block" )
    } 
    
	$("#selSys").modal("hide"); 
}

// 根据  跳过第几步  steps 获取 下一步  是否有 数据存在 ，如果存在 则展示 提示信息 ，并修改按钮文字 ，否则 不展示信息
/*（ 缺陷  ：  因为 没有相关接口 可以 一次性 获取 每一步 操作  是否 拥 有数据！ 所以只能 根据 每一步 去访问 ，这样 就会 同一 接口 在同一步 操作中 执行 两次 ，
 第一次 是从上一步到下一步 判断该步骤 是否有数据，第二次是进入页面 配置 该步骤，访问数据进行展示   ） */  
function getNextStepData( curStep  ){ 
	// console.log( curStep );     //   当前步数；
	switch (curStep) { 
		case 1:
			// 第一步 操作 跳过 按钮 不展示在页面里，
			layer.alert('不带这么玩的,请不要调皮!',{
				 icon: 0,
			})
			break;
		case 2: 
			//根据系统id 查询 该系统下面 所有代码库 并加载到 Dom中
			$.ajax({
		  		url:"/devManage/version/findScmRepositoryBySystemId",
		    	method:"post", 
		    	data:{
		    		"systemId":$( "#sysTemId" ).val(), 
				}, 
		  		success:function(data){   
		  			//先隐藏
		  			$( "#codeBaseDiv" ).empty();
		  			$('#page3 .successP').css( "display","none" );
		  			$('#page3 .childModalDiv').css( "display","none" ); 
		  			//如果进入 map 中 ，说明 有数据  便会在 方法中 会展示。
		  			data.data.map( function (item,index){
		  				addCodeBaseModal( item );
		  			}) 
		        }, 
		 	}); 
			break;
		case 3:
			$.ajax({
		  		url:"/devManage/systeminfo/findEnvIds",
		    	method:"post",  
		    	data:{
		    		"id": $("#sysTemId").val()
				}, 
		  		success:function(data){  
		  			if( ( data.data != undefined ? data.data.length : 0 ) > 0 ){ 
		  				globalData.envType = data.data; 
		  				$("#page4 .successP").css('display', 'block');
		  				$("#environmentConfig").text( "点击修改配置" );  
		  			}else{
		  				//在全局变量中 进行保存
		  				globalData.envType = [];
			  			$("#page4 .successP").css('display', 'none');
		  				$("#environmentConfig").text( "点击开始配置" ); 
		  			}  
		        }, 
		 	}); 
			break;
		case 4:
			$.ajax({
				type: "post",
				url: "/devManage/systeminfo/getSystemScmBySystemId",
				data: {
					id:  $("#sysTemId").val() ,
					architectureType: $("#architectureType").val()
				},
				dataType: "json",
				success: function (data) {  
					if( data.list.length > 0 ){
						codeConfigSuccess( null );
					} else{
						globalData.codeConfigStatus = false;
						$('#page5 .successP').css( "display","none" );
						$("#codeConfig").text( "点击开始配置" );
					}
				},
				error:function(){
		            $("#loading").css('display','none');
		            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
		        }
			}) 
			break;
		case 5:
			if( $( "#createType" ).val() != 1 ){  
				$("#page6 .successP").css('display', 'none'); 
				$("#autoDeployConfigBtn").addClass( "disableBtn" );
				$("#autoDeployConfigBtn").attr('disabled', 'true'); 
				$("#page6 .remindInfo").css('display', 'block');
			}else{
				$("#autoDeployConfigBtn").removeClass( "disableBtn" );
				$("#autoDeployConfigBtn").removeAttr('disabled');
				$("#page6 .remindInfo").css('display', 'none');
				$.ajax({
					url:"/devManage/systeminfo/getDeployList",
				    method:"post",
					data:{
						"systemId": $("#sysTemId").val(),
						"architectureType": $("#architectureType").val(),
					},
				   success:function(data){    
					   if( data.data.length > 0){
						   autoDeploySuccess( null );
					   }else{
						   globalData.autoDeployStatus = false; 
						   $('#page6 .successP').css( "display","none" );
						   $("#autoDeployConfigBtn").text( "点击开始配置" );
					   }
				   }
				});
			}  
			break;
		case 6:
			if( $( "#createType" ).val() != 1 ){  
				$("#page7 .successP").css('display', 'none'); 
				$("#autoTestConfigBtn").addClass( "disableBtn" );
				$("#autoTestConfigBtn").attr('disabled', 'true');
				$("#page7 .remindInfo").css('display', 'none');
			}else{  
				$("#autoTestConfigBtn").removeClass( "disableBtn" );
				$("#autoTestConfigBtn").removeAttr('disabled');
				$("#page7 .remindInfo").css('display', 'none');
				$.ajax({
					url:"/devManage/systeminfo/getAutoTest",
				    method:"post",
					data:{
						"systemId": $("#sysTemId").val(),
					},
				    success:function(data){    
					   if( data.types.length > 0 ){
						   autoTestSuccess( null );
					   }else{
						   globalData.autoTestStatus = false;
						   $('#page7 .successP').css( "display","none" );
						   $("#autoTestConfigBtn").text( "点击开始配置" );
					   }
				   }
				})
			}  
			break; 
		case 7:
			 
			break;
		default:
			break;
	} 
}







