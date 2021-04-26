/**
 * 部署配置
 */

var parameterArr={};//url中的参数
var dataObj={
	templateType:'',
	list:'',
};
var serSystemId;
//记录页面是否有过修改变动
var isChangeDeplolySetting = false;
$(document).ready(function(){
	parameterArr.arr = window.location.href.split( "?" ); 
    parameterArr.parameterArr = parameterArr.arr[1].split( "," );
    parameterArr.obj={};
    for( var i = 0 ; i < parameterArr.parameterArr.length ; i++ ){
	   	var obj = parameterArr.parameterArr[i].split( "=" );  
	   	if( obj[1] == "undefined" ){
	   		parameterArr.obj[ obj[0] ] = '';
	   	}else{
	   		parameterArr.obj[ obj[0] ] = obj[1];
	   	} 
    }   
	initPage();
	
	//摸板下拉框change事件
	$("#templateType").change(function(){
	    var This=this;
	    layer.confirm('确定切换？(切换会清除下面操作数据)', {
			icon: 0,	
			btn: ['确定', '不切换'] //可以无限个按钮 
		}, function(index, layero){ 
			var templateType = $(This).val();
			if( templateType == dataObj.templateType ){
				configAddInfo(dataObj.list);
			}else{
				$(This).prop("oldVal", templateType );
			    $.ajax({
					url: "/devManage/systeminfo/getDeployScripTemplate",
					method: "post",
					dataType: "json",
					data:{
						templateType: templateType  
					},
					success: function (data) {
						configAddInfo(data.scriptTemplateList)
					},
					error: function () {
						layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
					}
				}) 
			} 
			layer.closeAll();
		}, function(index){ 
			 $(This).val( $(This).attr("oldVal") );
			 $('.selectpicker').selectpicker('refresh');
			 layer.closeAll();
		}); 
   })
});

//记录页面是否有变动
function change_status_handle(){
	var arr = ['#serverGroup','#deployPath','#templateType','#timeOut','#retryNumber','#deploySequence'
		,'select[name="opt"]','input[name="waitTime"]','textarea[name="addScript"]','input[name="configUseName"]','input[name="configPassWord"]'];
	arr.map(function(v){
		$(v).bind('change',function(){		
			isChangeDeplolySetting = true;
		})
	})
}
//初始页面
function initPage(){
	serSystemId=parameterArr.obj.id;
	$("#deployInfoForm").bootstrapValidator('removeField','opt');
	$("#deployInfoForm").bootstrapValidator('removeField','waitTime');
	$("#deployInfoForm").bootstrapValidator('removeField','addScript');
	$("#deployInfoForm").bootstrapValidator('removeField','configUseName');
	$("#deployInfoForm").bootstrapValidator('removeField','configPassWord');
	
	$("#deployInfoForm").data('bootstrapValidator').destroy(); 
    formValidator(); 
	clearDefListMenu(); 
	$("#addConfigContent").empty(); 
	$("#deploy_serverId").val( parameterArr.obj.id ); 
	$("#loading").show();
	$.ajax({
		url:"/devManage/systeminfo/getModuleAndScm",
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
			   change_status_handle();
			   $("#loading").hide();
			   return;
		   }  
		   isChangeDeplolySetting = false;
		   
		   var str = "";
		   var moduleStr = "";
		   if( data.deploy.id == null ){
			   $("#deploy_Id").val( null ); 
		   }else{
			   $("#deploy_Id").val( data.deploy.id );
		   } 
		   var idStr='serverDiv';  
		   //架构类型，1微服务，2传统
		   if( parameterArr.obj.architectureType==1 ){  
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
		   //模版类型 
		   $("#templateType").empty();
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
		   
		   for(var i=0;i<data.types.length;i++){ 
			   if(i==0){
				   var liStr = '<li class="menuSelected_row" evn="'+data.types[i].envType+'" onclick="rowClick( \''+idStr+'\',this )">'+data.types[i].envName+'</li>';
			   }else{
				   var liStr = '<li evn="'+data.types[i].envType+'" onclick="rowClick( \''+idStr+'\',this )">'+data.types[i].envName+'</li>';
			   }
			   $("#serverDiv .def_rowList_menu>ul").append( liStr );
		   } 
		   
		   if( data.deploy.serverIds!='' ){
			   var serverIds=JSON.parse(data.deploy.serverIds);
			   for( var k in serverIds ){
					var str='<div class="optionDiv" hostName="'+serverIds[k].hostName+'" value="'+serverIds[k].id+'"><span class="addSysNames_span">'+serverIds[k].hostName+' </span><span class="close_x" onclick="delOptionDiv(this,event)">×</span></div>';
					$("#serverGroup").append( str ); 
				} 
		   }     
		   $("#deployPath").val(data.deploy.systemDeployPath);
		   $("#timeOut").val(data.deploy.timeOut);
		   $("#retryNumber").val(data.deploy.retryNumber); 
		   $("#deploySequence").val(data.deploy.deploySequence); 
		   configAddInfo( data.deployScriptList );
		  
	   } 
	});   
} 
function clearDefListMenu(){
	$("#deploymentConfig .def_colList li").remove();
	$("#deploymentConfig .def_rowList li").remove();
	$("#deploymentConfig #serverGroup").empty();
} 
//根据操作类型，生成不同的表单
function configAddInfo(data){
	$("#addConfigContent").empty();
	if( data.length>0 ){
		   for(var i=0;i<data.length;i++ ){ 
			   var newStr="";
			   var newStr='<div class="rowdiv"><div class="form-group def_col_12">'+
			   '<input class="hideIdValue" type="hidden" value='+data[i].id+' />'+
		       '<label class="def_col_15 control-label font_right fontWeihgt optClass">'+
		       '<a class="up_opt" title="上移" onclick="up_opt(this)" ></a> '+
		       '<a class="down_opt" title="下移" onclick="down_opt(this)" ></a> '+
		       ' <span class="checkfont">*</span>操作：</label>'+
		       '<div class="def_col_21"><select class="selectpicker" name="opt" onchange="showOtherInfo(this)"><option value="">请选择</option>';
		        //操作 1=执行脚本，2=切换用户，3=上传程序，4=暂停执行，5=执行断言
			   if( data[i].operateType==1 ){ 
				   
				   newStr+='<option value="1" selected >执行脚本</option><option value="2">切换用户</option><option value="3">上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
				   '<div class="form-group def_col_22 waitTime">'+     
			       '<label class="def_col_8 control-label font_right fontWeihgt"><span class="checkfont">*</span>等待时间(秒)：</label>'+
			       '<div class="def_col_28"><input type="text" name="waitTime" class="form-control" placeholder="请填写执行后需要等待的时间" value='+data[i].waitTime+' /></div></div>'+
			       '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div>'+
			       '<div class="form-group def_col_36 scriptTop">'+     
			       '<label class="def_col_5 control-label font_right fontWeihgt"><span class="checkfont">*</span>脚本：</label>'+
			       '<div class="def_col_31"><textarea name="addScript" class="def_textarea">'+data[i].script+'</textarea></div></div></div>';   
			   
			   }else if( data[i].operateType==2 ){
				   newStr+='<option value="1">执行脚本</option><option value="2" selected>切换用户</option><option value="3">上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
				   '<div class="form-group def_col_11 configUseName">'+     
			       '<label class="def_col_10 control-label font_right fontWeihgt"><span class="checkfont">*</span>用户名：</label>'+
			       '<div class="def_col_26"><input type="text" name="configUseName" class="form-control" placeholder="请填写用户名" value='+data[i].userName+'  /></div></div>'+ 
			       '<div class="form-group def_col_11 configPassWord">'+     
			       '<label class="def_col_10 control-label font_right fontWeihgt"><span class="checkfont">*</span>密码：</label>'+
			       '<div class="def_col_26"><input type="text" name="configPassWord" class="form-control" placeholder="请填写密码" value='+data[i].password+'  /></div></div>'+	
				   '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div></div>';
			   }else if( data[i].operateType==3 ){
				   newStr+='<option value="1">执行脚本</option><option value="2" >切换用户</option><option value="3" selected>上传包件</option><option value="4">暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
			       '<div class="form-group def_col_22 waitTime">'+     
			       '<label class="def_col_50 control-label font_right" style="padding-left:5px">上传部署包件到服务器，路径为部署路径标签指定的路径。</label>'+
			       '</div>'+	
				   '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div></div>'; 
			   }else if( data[i].operateType==4 ){
				   newStr+='<option value="1" >执行脚本</option><option value="2">切换用户</option><option value="3">上传包件</option><option value="4" selected>暂停执行</option><option value="5">执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
				   '<div class="form-group def_col_22 waitTime">'+     
			       '<label class="def_col_50 control-label font_right" style="padding-left:5px">暂停当前执行状态并等待用户点击确认后继续执行部署。</label>'+
			       '</div>'+
			       '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div>'+
			       '<div class="form-group def_col_36 scriptTop">'+     
			       '<label class="def_col_5 control-label font_right fontWeihgt"><span class="checkfont">*</span>等待描述内容：</label>'+
			       '<div class="def_col_31"><textarea name="addScript" class="def_textarea">'+data[i].script+'</textarea></div></div></div>';   
			   }else if( data[i].operateType==5 ){
				   newStr+='<option value="1" >执行脚本</option><option value="2">切换用户</option><option value="3">上传包件</option><option value="4">暂停执行</option><option value="5" selected>执行断言</option>' + 
'<option value="6">停止前执行SQL</option><option value="7">停止后执行SQL</option><option value="8">启动后执行SQL</option></select></div></div>'+
				   '<div class="form-group def_col_22 waitTime">'+     
			       '<label class="def_col_50 control-label font_right" style="padding-left:5px">对上一步骤的输出信息执行断言。</label>'+
			       '</div>'+
			       '<div class="def_col_2 font_center delFont2"><a onclick="delRowInfo(this)">删除</a></div>'+
			       '<div class="form-group def_col_36 scriptTop">'+     
			       '<label class="def_col_5 control-label font_right fontWeihgt"><span class="checkfont">*</span>断言内容：</label>'+
			       '<div class="def_col_31"><textarea name="addScript" class="def_textarea">'+data[i].script+'</textarea></div></div></div>';   
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
	change_status_handle();
	$("#loading").hide();
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
	 
	//校验表单
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
//  	   groupID: {
//  		   validators: {
//  			   notEmpty: {
//  				   message: 'GroupID不能为空'
//	               },
//	               stringLength: {
//                     max:50,
//                     message: 'GroupID长度必须小于50字符'
//                 },
//                 regexp: {            
//                     regexp: /^[-_/.0-9a-zA-Z]+$/,
//                     message: '只能输入数字、英文或-_/.'
//                 }, 
//	    	   }
//	       },
//	       artifactID: {
//	    	   validators: {
//	    		   notEmpty: {
//	    			   message: 'ArtifactID不能为空'
//	               },
//	               stringLength: {
//                     max:50,
//                     message: 'ArtifactID长度必须小于50字符'
//                 },
//                 regexp: {            
//                     regexp: /^[-_/.0-9a-zA-Z]+$/,
//                     message: '只能输入数字、英文或-_/.'
//                 }, 
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