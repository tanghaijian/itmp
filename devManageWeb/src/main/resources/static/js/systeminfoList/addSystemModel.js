/**
 * 新增子系统模块
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
	   		parameterArr.obj[ obj[0] ] = obj[1];
	   	} 
    }   
    formValidator();
	initPage();
});

//初始化列表数据
function initPage(){    
	var id=parameterArr.obj.id;
	$("#loading").css('display','block');
	$.ajax({
  		url:"/devManage/systeminfo/toAddSystemModule",
    	method:"post", 
    	data:{
    		"id":id
    	},
  		success:function(data){  
  			$("#loading").css('display','none');
  			$(".environmentStatus").remove();
  			$("#systemId").val(data.systemInfo.id);
  			$("#serviceSystemName").text( data.systemInfo.systemName );
  			$("#serviceSystemNum").text( data.systemInfo.systemCode );
  			$("#moduleName").val('');
  			$("#moduleCode").val('');
  			$("#addBuildDependency").selectpicker('val', '');
  			$("#addBuildDependencySequence").val('');
  			
  			$("#addReleaseRepositoryName").val('');
  			$("#addSnapshotRepositoryName").val('');
  			$("#addBuildToolVersion").selectpicker('val', '');
  			$("#addPackageSuffix").selectpicker('val', '');
  			$("#addJdkVersion").selectpicker('val', '');
  			$("#addGroupID").val('');
  			$("#addArtifactID").val('');
  			$("#relativePathAdd").val('');
  			$("#addCompileCommand").val('');
  			if(data.systemScms!="undefined"){
  				for(var i=0;i<data.systemScams.length;i++){ 
	  				$("#addModuleScmDiv").append('<div class="form-group col-md-2 environmentStatus"><input type="hidden" name="systemScmneme" val='+data.systemScams[i].id+'  /><label class="col-sm-8 control-label fontWeihgt"> <input type="checkbox" />'+data.type[i].valueName+'</label></div>');
  				}
  			}
  			 
  			$("#addService").modal("show"); 
  		}
  		
 	}); 
}   


//表单校验
function formValidator(){ 
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
    var systemModuleFlag = $("#systemModuleFlag").val();
	var buildType=$("#addBuildingTool input[name='tools']:checked").val();
	var groupId=$("#addGroupID").val();
	var artifactId=$("#addArtifactID").val();
	var jdkVersion=$("#addJdkVersion").val();

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
			"relativePath":relativePath,
			"systemModuleFlag":systemModuleFlag
		},
	   success:function(data){
		   $("#loading").css('display','none');
		   if(data.status == "success"){
			   parent.functionOpt( data.systemModule , 2 ); 
			   layer.alert('编辑成功', {
                   icon: 1,
                   title: "提示信息",
                   yes:function(){  
  					   parent.layer.closeAll();
 				   }
               });  
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