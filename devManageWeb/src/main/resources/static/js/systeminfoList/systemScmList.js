$("#loading").css('display', 'block');
//从url拿到的参数保存在下面这个变量里面
var parameterArr;
//后台获取到的所需全局变量
var param = {
	dics:'',
	list:'',
	systemModuleList:'',
	scmRepositorys:'',
}; 
$(document).ready(function(){ 	
	//拆分url链接 获取其中的id 和 type
	parameterArr={};   
    parameterArr.arr = window.location.href.split( "?" ); 
    parameterArr.parameterArr = parameterArr.arr[1].split( "," );
    parameterArr.obj={};
    for( var i = 0 ; i < parameterArr.parameterArr.length ; i++ ){
	   	var obj = parameterArr.parameterArr[i].split( "=" );  
	   	parameterArr.obj[ obj[0] ] = obj[1];
    }
    if( ( parameterArr.obj.architectureType != "1" && parameterArr.obj.architectureType != "2" ) ||  parameterArr.obj.id == undefined || parameterArr.obj.id == null || parameterArr.obj.id == '' ){
    	layer.alert('未配置系统架构，请进行配置!',{
			 icon: 2,
			 yes:function(){
				parent.layer.closeAll();
			 },
			 cancel: function(index, layero){ 
				 parent.layer.closeAll();
			 }     
		})
		return ;
    }
    getList( parameterArr );
    $("#addBtn").bind("click",function (){
    	addConfig();
    })
});
//请求列表数据
function getList( obj ){ 
	$.ajax({
		type: "post",
		url: "/devManage/systeminfo/getSystemScmBySystemId",
		data: {
			id: obj.obj.id,
			architectureType: obj.obj.architectureType
		},
		dataType: "json",
		success: function (data) { 
			param.dics = data.dics;
			param.list = data.list;
			param.systemModuleList = data.systemModuleList; 
			param.scmRepositorys = data.scmRepositorys;  
			initTable( data );
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	}) 
}
//重置 创建 表格
function initTable( data ){  
	var showChildSystem = true;
	if( parameterArr.obj.architectureType == 2 && parameterArr.obj.architectureType != undefined ){ 
		showChildSystem = false;
	}
	
	$("#codeConfigTable").bootstrapTable('destroy');
	$("#codeConfigTable").bootstrapTable({  
		data: data.list,
		queryParamsType : "",
		pagination : false, 
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8', 
		columns : [{
			field : "environmentType",
			title : "环境类型",
			align : 'center',
			formatter : function ( value,row,index ){ 
				var str='<div class="def_tableDiv" ><select class="selectpicker eTypeClass" >';
				for( var i = 0 ; i < data.dics.length ; i++ ){
					var selected=''
					if( row.environmentType == data.dics[i].valueCode ){ selected = "selected"  };
					str+= '<option val="'+data.dics[i].valueCode+'" '+ selected +' >'+data.dics[i].valueName+'</option>'
				}
				str+='</select></div>'
				return str;
			}
		},{
			field : "system",
			title : "子系统",
			align : 'center',
			visible : showChildSystem,
			formatter : function ( value,row,index ){ 
				/*moduleIds*/
				var str='<div class="def_tableDiv" ><select class="selectpicker childSysClass" multiple>';
				for( var i = 0 ; i < data.systemModuleList.length ; i++ ){
					var selected = '';
					if( row.moduleIds != null  ){
						if( row.moduleIds.indexOf( JSON.stringify( data.systemModuleList[i].id ) ) > -1  ){ selected = "selected"  };
					} 
					str+= '<option val="'+data.systemModuleList[i].id+'" '+ selected +' >'+data.systemModuleList[i].moduleName+'</option>'
				}
				str+='</select></div>'
				return str;
			}
		},{
			field : "systemRepositoryId",
			title : "仓库名称",
			align : 'center',
			formatter : function ( value,row,index ){ 
				var str='<div class="def_tableDiv" ><select class="selectpicker scmRepositoryClass" >';
				for( var i = 0 ; i < data.scmRepositorys.length ; i++ ){
					var selected='';
					if( row.systemRepositoryId == data.scmRepositorys[i].id ){ selected = "selected"  };
					str+= '<option val="'+data.scmRepositorys[i].id+'" '+ selected +' >'+data.scmRepositorys[i].repositoryName+'</option>'
				}
				str+='</select></div>'
				return str;
			}
		},{
			field : "system",
			title : "源代码地址",
			align : 'center',
			formatter : function ( value,row,index ){ 
				var scmUrl = '';
				if( row.scmBranch != null && row.scmBranch != '' ){
					scmUrl = row.scmUrl
				}
				var str='<div class="def_tableDiv" ><input class="form-control def_tableInput scmUrlClass" type="text" value="'+scmUrl+'"></div>'
				return str;
			}
		},{
			field : "scmBranch",
			title : "分支",
			align : 'center',
			formatter : function ( value,row,index ){ 
				var scmBranch = '';
				if( row.scmBranch != null && row.scmBranch != '' ){
					scmBranch = row.scmBranch
				}
				var str='<div class="def_tableDiv" ><input class="form-control def_tableInput scmBranchClass" type="text" value="'+scmBranch+'"></div>'
				return str;
			}
		},{
			field : "opt",
			title : "操作",
			align : 'center',
			formatter : function ( value,row,index ){   
				var str='<a class="a_style opt_a" idVal="'+ row.id +'" style="cursor:pointer" onclick="del(this,'+ row.id +',\''+ row.idsString +'\','+ row.environmentType +','+ row.systemId +')">删除</a>'
				return str;
			}
			
		}],
		onLoadSuccess : function() { 
			   
		},
		onLoadError : function() {

		}
	});  
	$('.selectpicker').selectpicker('refresh');
	if( data.list.length == 0 ){
		$("#codeConfigTable>tbody").empty();	
	}
}
//删除 配置
function del( This , id , idsString , environmentType , systemId ){
	layer.confirm('你确定要删除本条记录吗？', {
	  btn: ['确定','取消'] //按钮
	}, function(){
		layer.closeAll();
		if( parameterArr.obj.architectureType == 1 || parameterArr.obj.architectureType == 2 ){
			if( parameterArr.obj.architectureType == 2 ){
				if( id == '' || id == undefined ){
					$( This ).parent().parent().remove();
					return ;
				}
				var url = "/devManage/systeminfo/delScm";
				var data = { "id" : id };
			}else if( parameterArr.obj.architectureType == 1 ){
				if( idsString == '' || idsString == undefined ){
					$( This ).parent().parent().remove();
					return ;
				}
				var url = "/devManage/systeminfo/delModuleScm";
				var data = { "idsString" : idsString  , "environmentType": environmentType , "systemId": systemId };
			} 
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
			 			$( This ).parent().parent().remove(); 
		  			}else{
		  				layer.alert('删除失败!',{
			 				 icon: 2,
			 			})
		  			} 
		        }, 
		 	}); 
		} 
	}, function(){
		layer.closeAll();
	});
	 
}
//按钮点击触发-添加配置 
function addConfig(){    
	var length = $( "#codeConfigTable>tbody>tr" ).length + 1;
	var str='<tr data-index="'+length+'">'+
			'<td style="text-align: center; ">'+
			'<div class="def_tableDiv" ><select class="selectpicker eTypeClass" >';
			for( var i = 0 ; i < param.dics.length ; i++ ){ 
				str+= '<option val="'+param.dics[i].valueCode+'" >'+param.dics[i].valueName+'</option>'
			}
			str+='</select></div></td>';
			if( parameterArr.obj.architectureType == 1 && parameterArr.obj.architectureType != undefined ){
				str+='<td style="text-align: center; ">'+
			    '<div class="def_tableDiv" ><select class="selectpicker childSysClass" multiple>';
				for( var i = 0 ; i < param.systemModuleList.length ; i++ ){  
					str+= '<option val="'+param.systemModuleList[i].id+'">'+param.systemModuleList[i].moduleName+'</option>'
				}
				str+='</select></div></td>';  
			} 
			str+='<td style="text-align: center; ">'+
			'<div class="def_tableDiv" ><select class="selectpicker scmRepositoryClass" >';
			for( var i = 0 ; i < param.scmRepositorys.length ; i++ ){ 
				str+= '<option val="'+param.scmRepositorys[i].id+'">'+param.scmRepositorys[i].repositoryName+'</option>'
			}
			str+='</select></div></td>';    
			str+='<td style="text-align: center; "><div class="def_tableDiv" ><input class="form-control def_tableInput scmUrlClass" type="text" value=""></div></td>';  	
			str+='<td style="text-align: center; "><div class="def_tableDiv" ><input class="form-control def_tableInput scmBranchClass" type="text" value=""></div></td>'; 
			str+='<td style="text-align: center; "><a class="a_style opt_a" idVal="" style="cursor:pointer" onclick="del(this)">删除</a></td></tr>'; 
	$( "#codeConfigTable>tbody" ).append( str ); 	
	$('.selectpicker').selectpicker('refresh');
}
//源码配置提交
function submit(){ 
	var data = [];
	var systemArr=[]; 
	var flag = true;//用于标识页面校验结果，如果为false表示有错，不能提交
	if( $( "#codeConfigTable>tbody>tr" ).length <= 0  ){
        layer.alert('未检测到配置信息，请进行配置！',{
            icon: 2,
        })
        return ;
    }
	$( "#codeConfigTable>tbody>tr" ).each(function(index,element) {
		if( parameterArr.obj.architectureType == 1 ){//微服务架构
			if( $( element ).find( ".childSysClass" ).eq( 1 ).find("option:selected").length == 0 ){
				layer.alert('子系统数据不能为空',{
					icon: 2,
				})
				flag = false;
				return false; 
			}else{
				$( element ).find( ".childSysClass" ).eq( 1 ).find("option:selected").each(function (index1,element2){ 
					var obj = {};  
					//systemId
					obj.systemId = parameterArr.obj.id; 
					obj.systemModuleId = $( element2 ).attr( "val" );
					//environmentType  
					obj.environmentType =$( element ).find( ".eTypeClass" ).eq( 1 ).find("option:selected").attr( "val" ); 
					//scmRepositoryName 
					obj.scmRepositoryName =$( element ).find( ".scmRepositoryClass" ).eq( 1 ).find("option:selected").val();
					obj.systemRepositoryId = $( element ).find( ".scmRepositoryClass" ).eq( 1 ).find("option:selected").attr( "val" );
					   
					obj.scmUrl = $( element ).find( ".scmUrlClass" ).val();
					obj.scmBranch = $( element ).find( ".scmBranchClass" ).val(); 
					data.push( obj );
					
					var chidrenArr=[ obj.environmentType , obj.systemModuleId ];
					systemArr.push( chidrenArr );
				});
			} 
		}else if( parameterArr.obj.architectureType == 2 ){//传统架构
			var obj = {}; 
			obj.id = $( element ).find( ".opt_a" ).attr( "idVal" ); 
			//systemId
			obj.systemId = parameterArr.obj.id;
			//environmentType  
			obj.environmentType = $( element ).find( ".eTypeClass" ).eq( 1 ).find("option:selected").attr( "val" ); 
			//scmRepositoryName 
			obj.scmRepositoryName = $( element ).find( ".scmRepositoryClass" ).eq( 1 ).find("option:selected").val(); 
			obj.systemRepositoryId = $( element ).find( ".scmRepositoryClass" ).eq( 1 ).find("option:selected").attr( "val" ); 
			 
			obj.scmUrl = $( element ).find( ".scmUrlClass" ).val();
			obj.scmBranch = $( element ).find( ".scmBranchClass" ).val(); 
			data.push( obj );
			systemArr.push( obj.environmentType );
		} 
	}); 
	for(let i=0;i<data.length;i++){ 
	     for(var key in data[i] ){
	    	 if(key == 'scmBranch') {
	    		 continue;
	    	 }
	    	 if( ( data[i][key] == ''|| data[i][key] == null || data[i][key] == undefined ) && ( key != 'id' ) ){
	    		 var strName =''; 
	    		 switch(key) {
		    	     case "environmentType": strName ='环境类型'; break;
		    	     case "systemModuleId": strName ='子系统'; break;
		    	     case "scmRepositoryName": strName ='仓库名称'; break;
		    	     case "scmUrl": strName ='源代码地址'; break;
//		    	     case "scmBranch": strName ='分支'; break; 
		    	     default: strName ='';break; 
		    	 } 
	    		 layer.alert(strName+'数据不能为空',{
					 icon: 2,
				 })
				 return; 
	    	 }
	    	 if( key == 'scmUrl' ){
	    		 var reg = new RegExp(/((http|svn)?:)\/\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]/g);  
	    		 if( !reg.test( data[i][key] ) ){
	    			 layer.alert("源代码地址格式错误，格式为：( http|svn )://xxx/xx",{icon : 2});
	  				 return;
	  			 }  
	    	 }
	     }
	} 
	if( !flag ){
		return;
	}
	/*校验条件方法*/ 
	let nary=systemArr.sort(); 
	for(let i=0;i<nary.length-1;i++){ 
	    if (JSON.stringify(nary[i]) == JSON.stringify(nary[i+1]) ){ 
			if( parameterArr.obj.architectureType == 2 ){ 
				layer.alert('环境类型必须唯一!',{
					 icon: 2,
				});
			}else if( parameterArr.obj.architectureType == 1 ){ 
				layer.alert('同一环境类型下子系统必须唯一!',{
					 icon: 2,
				})
			}
	    	return; 
	    }
	}     
	//传统架构
	if( parameterArr.obj.architectureType == 2 ){ 
		var url = "/devManage/systeminfo/updateSystemScm";
		var dataA = { "syetemScm" : JSON.stringify( data ) };
	}else if( parameterArr.obj.architectureType == 1 ){ //微服务架构
		var url = "/devManage/systeminfo/updateSystemModuleScm1";
		var dataA = { "syetemScm" : JSON.stringify( data ) };
	}else{
		layer.alert('数据错误，请联系管理员!',{
			 icon: 2,
		})
		return;
	}  
	$.ajax({
  		url:url,
    	method:"post", 
    	async:false,
    	data:dataA, 
  		success:function(data){  
  			if(data.status == 1){
  				layer.alert('保存成功!',{
	 				 icon: 1,
	 				 yes:function(){ 
	 					parent.functionOpt( data, 5 );
	 					parent.layer.closeAll();
	 				 }
	 			}) 
  			}else{
  				layer.alert('保存失败!',{
	 				 icon: 2,
	 			})
  			}
        }, 
 	});
}








