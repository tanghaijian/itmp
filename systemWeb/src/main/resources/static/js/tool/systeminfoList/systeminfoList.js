/**
 * 废弃
 * Created by 朱颜辞镜花辞树 on 2018/9/18.
 */
//分页所需参数
var searchArr={};
var systemId = 0;
var moduleId = 0;
var systemVersionStr = '';
var commissioningWindow = '';
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
	getSearchData();
	//所有的Input标签，在输入值后出现清空的按钮
    $('input[type*="text"]').parent().css("position","relative");
    $('input[type*="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type*="text"]').bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    })
    pageInit(); 
    versionManageInit();
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
        width: $(".content-table").width()*0.999,
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
	            		str+=" <sup class='micSup'>微服务架构</sup>"
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
                align:"center",
                fixed:true,
                sortable:false,
                resize:false,
                search: false,
                width:200,
                formatter : function(value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');  
                    if(rows.architectureType == undefined){
                    	 return '<a class="a_style" style="cursor:pointer" onclick="config('+ row + ')">配置</a> | <a class="a_style" style="cursor:pointer" onclick="reloadVersion('+ row + ')">版本管理</a>'
                    }else{
                    	if(rows.architectureType == 1){
                       	 return '<a class="a_style" style="cursor:pointer" onclick="config('+ row + ')">配置</a> | <a class="a_style" style="cursor:pointer" onclick="addService('+ row + ')">新建微服务</a> | <a class="a_style" style="cursor:pointer" onclick="reloadVersion('+ row + ')">版本管理</a>'
                       }else {
                       	 return '<a class="a_style" style="cursor:pointer" onclick="config('+ row + ')">配置</a> | <a class="a_style" style="cursor:pointer" onclick="reloadVersion('+ row + ')">版本管理</a>'
                       }
                    }
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
function versionManageInit(){
	jQuery("#list3").jqGrid("clearGridData");
	$("#loading").css('display','block');
    jQuery("#list3").jqGrid({  
        url:'/devManage/systemVersion/getSystemVersionByCon', 
        datatype: "json", 
        height: 'auto',  
        mtype : "post",
        width: $("#versionModal .modal-lg").width()*0.899,
        colNames:[ 'id','version','操作'],
        colModel:[ 
        	{name:'id',index:'id',hidden:true},
            {name:'version',index:'version'},  
            { 
                name:'操作',
                index:'操作',
                align:"center",
                fixed:true,
                sortable:false,
                resize:false,
                search: false,
                width:440,
                formatter : function(value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');  
                    return '<a class="a_style" style="cursor:pointer" onclick="editVersion('+ row + ')">编辑</a>'
                }
            }
        ],  
        jsonReader: {
            repeatitems: false,
            root: "rows", 
        }, 
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
					   reloadVersion({id:systemId});
				   }
			   }
			
			});  
		}
	);
}
//编辑版本
function editVersion(row){
	layer.prompt({
		formType: 0,
		title: '输入版本号',
		value:row.version
		},
		function(value, index, elem){
			$.ajax({
				url:"/devManage/systemVersion/updateSystemVersion",
			    method:"post",
				data:{
					"version":value,
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
					   reloadVersion({id:systemId});
				   }
			   }
			
			});
		}
	);
}
//重载版本表格
function reloadVersion(row){  
	
	if(row.level == '0'){
		systemId = row.id;
	}else if(row.level == '1'){
		moduleId = row.id;
	}
	$("#loading").css('display','block');
	$("#list3").jqGrid('setGridParam',{ 
		url:"/devManage/systemVersion/getSystemVersionByCon",
        postData:{"systemId":systemId == 0?'':systemId,"moduleId":moduleId == 0?'':moduleId},
        loadComplete :function(xhr){  
        	$("#versionModal").modal('show');
        	$("#loading").css('display','none');
        }
    }).trigger("reloadGrid"); //重新载入
}

//新增微服务弹框
function addService(row){
	var id=row.id;
	$("#loading").css('display','block');
	$.ajax({
  		url:"/devManage/systeminfo/toAddSystemModule",
    	method:"post", 
    	data:{
    		"id":id
    	},
  		success:function(data){  
  			$("#loading").css('display','none');
  			$("#addModuleScmDiv").empty();
  			$("#moduleName").val('');
  			$("#moduleCode").val('');
  			$("#systemId").val(data.systemInfo.id);
  			$("#serviceSystemName").text( data.systemInfo.systemName );
  			$("#serviceSystemNum").text( data.systemInfo.systemCode );
  			if(data.systemScms!="undefined"){
  				for(var i=0;i<data.systemScams.length;i++){ 
	  				$("#addModuleScmDiv").append('<div class="form-group col-md-2 environmentStatus"><input type="hidden" name="systemScmneme" val='+data.systemScams[i].id+'  /><label class="col-sm-8 control-label fontWeihgt"> <input type="checkbox" />'+data.type[i].valueName+'</label></div>');
  				}
  			}
  			$("#addService").modal("show"); 
  		}
  		
 	}); 
}
//新增微服务提交
function addMudule(){
	$('#newform').data('bootstrapValidator').validate();
	if(!$('#newform').data('bootstrapValidator').isValid()){
		$('#addService').on('hide.bs.modal', function () {
			     $('#newform').bootstrapValidator('resetForm');
			});
		return;
	}
	
	var systemId = $("#systemId").val();
	var moduleCode = $("#moduleCode").val();
	var moduleName = $("#moduleName").val();
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
			"moduleName":moduleName
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
function config( row ){ 
	var id=row.id;
	var createType=row.createType 
	//if( row.architectureType==1 || row.architectureType==2 ){
	if(row.systemId == undefined){
		//系统配置弹框
		console.log(1)
		$("#loading").css('display','block'); 
		if( createType == null){
			createType = 1 ;
		}
		$.ajax({
	  		url:"/devManage/systeminfo/getOneSystemInfo",
	    	method:"post", 
	    	data:{
	    		"id":id,
	    		"createType": createType
	    	},
	  		success:function(data){ 
	  			$("#loading").css('display','none');
	  			$(".dataEntry").empty();
	  			$("#architectureId").empty();
	  			$(".buildingConfig").empty();
	  			$(".deployConfig").empty();
	  			$("#architectureId").append('<option value="">请选择</option>');
	  			
	  			$("#microServiceTitle").text( data.systemInfo.systemCode+" "+data.systemInfo.systemName );
	  			$("#microServiceName").text( data.systemInfo.systemName );
	  			$("#microServiceNum").text( data.systemInfo.systemCode );
	  			
	  			$("#microServiceSnapshotRepositoryName").text(data.systemInfo.snapshotRepositoryName);
	  			$("#microServiceReleaseRepositoryName").text(data.systemInfo.releaseRepositoryName);
	  			
	  			$("#microServiceID").val( data.systemInfo.id );
	  			$("#microServiceGroupID").val( data.systemInfo.groupId );
	  			$("#microServiceArtifactID").val( data.systemInfo.artifactId );
	  			
	  			var flag1 = '';
	  			var flag2 = '';
	  			if(data.systemInfo.architectureType == 1){
	  				flag1 = "selected";
	  			}else if(data.systemInfo.architectureType == 2){
	  				flag2 = "selected";
	  			}
	  			$("#architectureId").append('<option '+flag1+' value="1">微服务架构 </option><option '+flag2+' value="2">传统架构 </option>');
	  			$("#buildingTool input[type*='radio']").prop("checked", false);
	  			$("#buildingWay input[type*='radio']").prop("checked", false); 
	  			if( data.systemInfo.buildType===null ){   
	  				$("#buildingTool input[value*='1']").prop("checked", 'true'); 
	  			}else{
	  				$("#buildingTool input[value*='"+data.systemInfo.buildType+"']").prop("checked", 'true'); 
	  			} 
	  			if( data.systemInfo.createType===null ){ 
	  				$("#buildingWay input[value*='1']").prop("checked", 'true');  
	  				$(".manualInfo").css("display","none");
	  			}else{
	  				$("#buildingWay input[value*='"+data.systemInfo.createType+"']").prop("checked", 'true'); 
	  				$(".manualInfo").css("display","none");
	  				$(".microServiceData").append('<div class="singleData"><div class="singleDataDiv"><div class="singleDataFont">环境类型</div></div><div class="singleDataDiv"><div class="singleDataFont">托管服务</div></div><div class="singleDataDiv"><div class="singleDataFont">工具地址</div></div><div class="singleDataDiv"><div class="singleDataFont">是否提交</div></div><div class="singleDataDiv"><div class="singleDataFont">系统版本</div></div><div class="singleDataDiv"><div class="singleDataFont">投产窗口</div></div><div class="singleDataDiv2"><div class="singleDataFont2">分支名称</div></div><div class="singleDataDiv2"><div class="singleDataFont2">管理地址</div></div></div>');
	  				systemVersionStr = '';
	  				commissioningWindow = '';
	  				if(data.systemVersionList != null){
	  					$.each(data.systemVersionList,function(index,value){
	  						systemVersionStr += '<option value="'+value.id+'">'+value.version+'</option>'
	  					});
	  				}
	  				if(data.commissioningWindowList != null){
	  					$.each(data.commissioningWindowList,function(index,value){
	  						commissioningWindow += '<option value="'+value.id+'">'+value.windowName+'</option>'
	  					});
	  				}
	  				if(data.list.length>0){
		  				for(var i=0;i<data.list.length;i++){ 
		  	  				(function (i){  
		  	  					$(".microServiceData").append(
		  	  							'<div class="singleData"><input type="hidden" name="systemScmNeme" value=""/><div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="environmentTypeName"> <option value="">请选择</option> <option value="1">DEV</option> <option value="2">SIT</option> <option value="3">UAT</option> <option value="4">PRD</option> </select> </div> </div>'+
		  	  							'<div class="singleDataDiv"> <div class="search_input entrySelect"> <select class="selectpicker microServiceScmType" onchange="getToolId( this )"  name="scmTypeName"> <option value="">请选择</option> <option value="1">Git</option> <option value="2">SVN</option> </select> </div> </div>'+
		  	  							'<div class="singleDataDiv"> <div class="search_input entrySelect"> <select class="selectpicker microServiceScmType"  name="toolId"> <option value="">请选择</option></select> </div> </div>'+
		  	  							'<div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="submitStatus"> <option value="">请选择</option> <option value="1">是</option> <option value="2">否</option> </select> </div> </div>'+
		  	  							'<div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="systemVersion"> <option value="">请选择</option>'+systemVersionStr+' </select> </div> </div>'+
		  	  							'<div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="commissioningWindow"> <option value="">请选择</option> '+commissioningWindow+' </select> </div> </div>'+
		  	  							'<div class="singleDataDiv2"> <div class="search_input entrySelect2"> <input type="text" class="form-control microServiceScmBranch" placeholder="请输入" /> </div> </div> <div class="singleDataDiv2"> <div class="search_input entrySelect2"> <input type="text" class="form-control microServiceScmUrl" placeholder="请输入" /> </div> </div> <div class="delSingData"  onclick="delSingData(this)"> <a class="delFont">删除</a> </div> </div> '); 
		  	  					$("select[name='environmentTypeName']").eq(i).selectpicker('val', data.list[i].environmentType);
		  	  					$("select[name='scmTypeName']").eq(i).selectpicker('val', data.list[i].scmType); 
		  	  					$("select[name='submitStatus']").eq(i).selectpicker('val',data.list[i].submitStatus);
		  	  					$("select[name='systemVersion']").eq(i).selectpicker('val',data.list[i].systemVersionId);
		  	  					$("select[name='commissioningWindow']").eq(i).selectpicker('val',data.list[i].commissioningWindowId);
		  		  				$(".microServiceScmBranch").eq(i).val( data.list[i].scmBranch);	
		  		  				$(".microServiceScmUrl").eq(i).val( data.list[i].scmUrl);
		  		  				$("input[name='systemScmNeme']").eq(i).val( data.list[i].id ); 
		  		  				getToolId( data.list[i],i );
		  	  				})(i); 
		  	  			} 
		  			}else{
		  				$(".microServiceData").append('<div class="singleData"><input type="hidden" name="systemScmNeme" value=""/><div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="environmentTypeName"> <option value="">请选择</option> <option value="1">DEV</option> <option value="2">SIT</option> <option value="3">UAT</option> <option value="4">PRD</option> </select> </div> </div>'+
  	  							'<div class="singleDataDiv"> <div class="search_input entrySelect"> <select class="selectpicker microServiceScmType" onchange="getToolId( this )"  name="scmTypeName"> <option value="">请选择</option> <option value="1">Git</option> <option value="2">SVN</option> </select> </div> </div>'+
  	  							'<div class="singleDataDiv"> <div class="search_input entrySelect"> <select class="selectpicker microServiceScmType"  name="toolId"> <option value="">请选择</option></select> </div> </div>'+
  	  							'<div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="submitStatus"> <option value="">请选择</option> <option value="1">是</option> <option value="2">否</option> </select> </div> </div>'+
  	  							'<div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="systemVersion"> <option value="">请选择</option>'+systemVersionStr+' </select> </div> </div>'+
  	  							'<div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="commissioningWindow"> <option value="">请选择</option> '+commissioningWindow+' </select> </div> </div>'+
  	  							'<div class="singleDataDiv2"> <div class="search_input entrySelect2"> <input type="text" class="form-control microServiceScmBranch" placeholder="请输入" /> </div> </div> <div class="singleDataDiv2"> <div class="search_input entrySelect2"> <input type="text" class="form-control microServiceScmUrl" placeholder="请输入" /> </div> </div> <div class="delSingData"  onclick="delSingData(this)"> <a class="delFont">删除</a> </div> </div> '); 
		  			} 
	  				for( var i=0;i<data.jenkinslist.length;i++ ){
	  					var ip;
	  					for(var key in data.toolList){  
	  						 if(data.toolList[key].id==data.jenkinslist[i].toolId){
	  							ip=data.toolList[key].ip;
	  						 }
	  					} 
	  					var str='<div class="JenkinsJob rowdiv"><input type="hidden" class="hideKeyId" value='+data.jenkinslist[i].rootPom+' />'+
	  		 			'<div class="JobName font_right fontWeihgt def_col_7">Jenkins工具地址：</div>'+
	  		 			'<div class="JobName def_col_7"><select disabled="disabled" class="selectpicker JenkinsTooladdress" name="status"><option value='+data.jenkinslist[i].toolId+' checked>'+ip+'</option>'+
	  					'</select></div><div class="JobName font_right fontWeihgt def_col_6">Jenkins Job Name：</div>'+
	  		 			'<div class="JobName def_col_13"><input disabled="disabled" type="text" value='+data.jenkinslist[i].jobName+' class="form-control" placeholder="请输入" name="JobName"/></div>'+
	  		 			'<div class="JobNameDel fontWeihgt def_col_3"><a onclick="delJenkinsJob(this)">删除</a></div></div>';
	  					if( data.jenkinslist[i].jobType==1 ){
	  						$(".buildingConfig").append( str );
	  					}else if( data.jenkinslist[i].jobType==2 ){
	  						$(".deployConfig").append( str );
	  					} 
	  				}
	  				if(data.systemInfo.createType==2){ 
		  				$(".manualInfo").css("display","block"); 
	  				}  
		  			$('.selectpicker').selectpicker('refresh');
	  			}  
	  			$("#microService").modal("show"); 
	        },
	  		error:function(){ 
	  		}
	 	});  
	} else{
		$("#loading").css('display','block');
		//微服务配置弹框
		$.ajax({
	  		url:"/devManage/systeminfo/getOneSystemModule",
	    	method:"post", 
	    	data:{
	    		"id":id
	    	},
	  		success:function(data){ 
	  			$("#loading").css('display','none');
	  			//checkBoxStats=[]; 
	  			$("#environmentDIV").empty();
	  			$("#microChildService").text( data.systemCode+" "+data.systemName+": "+data.moduleName );
	  			$("#microChildServiceID").val( data.id );
	  			$("#microChildSystemId").val(data.systemId);
	  			$("#microChildServiceSystemName").text( data.systemName );
	  			$("#microChildServiceSystemNum").text( data.systemCode );
	  			$("#microChildServiceName").text( data.moduleName );
	  			$("#microChildServiceNum").text( data.moduleCode );
	  			if(data.systemScms!="undefined"){
	  				for(var i=0;i<data.systemScms.length;i++){ 
		  				var flag='';
		  				for(var j=0;j<data.types2.length;j++){
		  					if( data.systemScms[i].id == data.types2[j].systemScmId ){
		  						flag="checked"; 
		  					} 
		  				} 
		  				$("#environmentDIV").append('<div class="form-group col-md-2 environmentStatus"><input type="hidden" name="systemScmneme" val='+data.systemScms[i].id+'  /><input type="hidden" name="moduleScmNeme"/><label class="col-sm-8 control-label fontWeihgt"> <input type="checkbox" '+flag+'  />'+data.types[i].valueName+'</label></div>');
		  			}
	  			}
	  			for(var i=0;i<data.types2.length;i++){
	  				(function (i){ 
	  					$("input[name='moduleScmNeme']").eq(i).val(data.types2[i].id);
	  				})(i); 
	  			} 
	  			$("#normalService").modal("show"); 
	        },
	  		error:function(){ 
	  		}
	 	}); 
	}    
}

//系统配置提交
function microServiceCommit(){
	var data={}
	var id=$("#microServiceID").val();
	var groupId=$("#microServiceGroupID").val();
	var artifactId=$("#microServiceArtifactID").val();
	var architectureId = $("#architectureId").val(); 
	var snapshotRepositoryName = $("#microServiceSnapshotRepositoryName").val(); 
	var releaseRepositoryName = $("#microServiceReleaseRepositoryName").val(); 
	var buildType=$("#buildingTool input[name='tools']:checked").val();
	var createType=$("#buildingWay input[name='build']:checked").val();
	$("#loading").css('display','block'); 
	var list=[];
	for(var i=0;i<$(".singleData").length-1;i++){
		(function (i){
			var obj={};  
			if($("select[name='environmentTypeName']").eq(i).find("option:selected").attr("value")!=''){
				obj.environmentType=$("select[name='environmentTypeName']").eq(i).find("option:selected").attr("value");
			}
			if($("select[name='scmTypeName']").eq(i).find("option:selected").attr("value")!=''){
				obj.scmType=$("select[name='scmTypeName']").eq(i).find("option:selected").attr("value");
			}
			if($("select[name='toolId']").eq(i).find("option:selected").attr("value")!=''){
				obj.toolId=$("select[name='toolId']").eq(i).find("option:selected").attr("value");
			}
			if($("select[name='submitStatus']").eq(i).find("option:selected").attr("value")!=''){
				obj.submitStatus=$("select[name='submitStatus']").eq(i).find("option:selected").attr("value");
			}
			if($("select[name='systemVersion']").eq(i).find("option:selected").attr("value")!=''){
				obj.systemVersionId=$("select[name='systemVersion']").eq(i).find("option:selected").attr("value");
			}
			if($("select[name='commissioningWindowId']").eq(i).find("option:selected").attr("value")!=''){
				obj.commissioningWindowId=$("select[name='commissioningWindow']").eq(i).find("option:selected").attr("value");
			}
			if($(".microServiceScmBranch").eq(i).val()!=''){
				obj.scmBranch=$(".microServiceScmBranch").eq(i).val();
			}
			if($(".microServiceScmUrl").eq(i).val()!=''){
				obj.scmUrl=$(".microServiceScmUrl").eq(i).val();
			}
			if($("input[name='systemScmNeme']").eq(i).val()!=''){
				obj.id=$("input[name='systemScmNeme']").eq(i).val();
			}
			if (!(JSON.stringify(obj) == "{}")) {
				list.push(obj);
			}
			
		})(i);
	} 
	list = JSON.stringify(list);
	var flagManual="";
	var flagDeploy="";
	if( createType==1 ){
		data={
    		"id":id,
    		"groupId":groupId,
    		"artifactId":artifactId,
    		"systemScm":list,
    		"snapshotRepositoryName":snapshotRepositoryName,
    		"releaseRepositoryName":releaseRepositoryName,
    		"architectureType":architectureId,
    		"createType":createType,
    		"createTypes": createType, 
    		"buildType": buildType,
    	}; 
	}else if( createType==2 ){
		
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
	    		"groupId":groupId,
	    		"artifactId":artifactId,
	    		"systemScm":list,
	    		"systemJenkins":list2,
	    		"snapshotRepositoryName":snapshotRepositoryName,
	    		"releaseRepositoryName":releaseRepositoryName,
	    		"architectureType":architectureId,
	    		"createType":createType,
	    		"createTypes": createType,
	    		"buildType": buildType,
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
  			}else if(data.status == "fail"){
  				//alert("保存失败！！！");
  				layer.alert('保存失败', {
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
  			}else if(data.status = "success"){
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
//微服务配置提交
function microChildServiceCommit(){
	var id=$("#microChildServiceID").val();
	var systemId = $("#microChildSystemId").val();
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
//	console.log(moduleArr);
	$("#loading").css('display','block');
	$.ajax({
  		url:"/devManage/systeminfo/updateSystemModuleScm",
    	method:"post", 
    	data:{
    		 "systemId":systemId,
    		 "systemModuleId":id,
    		 "systemModuleScms":moduleArr
    	},
  		success:function(data){ 
  			$("#loading").css('display','none');
  			if(data.status == "success"){
//  				alert("配置成功！！！");
  				layer.alert('配置成功', {
                    icon: 1,
                    title: "提示信息"
                });
  				pageInit();
  	  			$("#normalService").modal("hide"); 
  			}
  			if(data.status == "fail"){
  				//alert("配置失败！！！");
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
  			 if( typeof(i) === 'undefined'  ){  
  				$(type).parent().parent().parent().next().children('.search_input').children('div').children('select').empty();
  				$(type).parent().parent().parent().next().children('.search_input').children('div').children('select').append('<option value="">请选择</option>');
  				
  				for( var j=0;j<data1.toolList.length;j++ ){
  					if(  data1.toolList[j].toolType == $( type ).val()  ){
  						var obj='<option value='+data1.toolList[j].id+'>'+data1.toolList[j].ip+'</option>'; 
  	  	  				$(type).parent().parent().parent().next().children('.search_input').children('div').children('select').append(obj);
  					}
  				}
  			 } else{ 
  				for(var j=0;j<data1.toolList.length;j++){ 
  					if(  data1.toolList[j].toolType == type.scmType  ){ 
  						var obj='<option value='+data1.toolList[j].id+'>'+data1.toolList[j].ip+'</option>'; 
  	  	  				$("select[name='toolId']").eq(i).append(obj);
  					} 
  	  			}  
  				$("select[name='toolId']").eq(i).selectpicker('val', type.toolId);  
  			 }
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
function addDataEntry() {
	$(".dataEntry").append('<div class="singleData"><input type="hidden" name="systemScmNeme" value=""/><div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="environmentTypeName"> <option value="">请选择</option> <option value="1">DEV</option> <option value="2">SIT</option> <option value="3">UAT</option> <option value="4">PRD</option> </select> </div> </div>'+
				'<div class="singleDataDiv"> <div class="search_input entrySelect"> <select class="selectpicker microServiceScmType" onchange="getToolId( this )"  name="scmTypeName"> <option value="">请选择</option> <option value="1">Git</option> <option value="2">SVN</option> </select> </div> </div>'+
					'<div class="singleDataDiv"> <div class="search_input entrySelect"> <select class="selectpicker microServiceScmType"  name="toolId"> <option value="">请选择</option></select> </div> </div>'+
					'<div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="submitStatus"> <option value="">请选择</option> <option value="1">是</option> <option value="2">否</option> </select> </div> </div>'+
					'<div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="systemVersion"> <option value="">请选择</option>'+systemVersionStr+' </select> </div> </div>'+
					'<div class="singleDataDiv"><div class="search_input entrySelect"> <select class="selectpicker  microServiceEnvironmentType" name="commissioningWindow"> <option value="">请选择</option> '+commissioningWindow+' </select> </div> </div>'+
					'<div class="singleDataDiv2"> <div class="search_input entrySelect2"> <input type="text" class="form-control microServiceScmBranch" placeholder="请输入" /> </div> </div> <div class="singleDataDiv2"> <div class="search_input entrySelect2"> <input type="text" class="form-control microServiceScmUrl" placeholder="请输入" /> </div> </div> <div class="delSingData"  onclick="delSingData(this)"> <a class="delFont">删除</a> </div> </div>')
    $('.selectpicker').selectpicker('refresh');
}
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
    var a = del();
    if(a){
      $(This).parent().remove();
    }
}
function del() {
	var msg = "您确定要删除吗？！";
	if (confirm(msg)==true){
		return true;
	}else{
		return false;
	}
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
function confirmDel(  id ) {   
	console.log( id )
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
 			'<div class="JobName font_right fontWeihgt def_col_7">Jenkins工具地址：</div>'+
 			'<div class="JobName def_col_7"><select class="selectpicker JenkinsTooladdress" name="status"><option value="">请选择</option>';
			for(var i=0;i< data.toolList.length ;i++){
				str+='<option value='+data.toolList[i].id+' >'+data.toolList[i].ip+'</option>';
			}
			str+='</select></div><div class="JobName font_right fontWeihgt def_col_6">Jenkins Job Name：</div>'+
 			'<div class="JobName def_col_13"><input type="text" class="form-control" placeholder="请输入" name="JobName"/></div>'+
 			'<div class="JobNameDel fontWeihgt def_col_3"><a onclick="delJenkinsJob(this)">删除</a></div></div>';
 			$( This ).parent().next().append( str );
			$('.selectpicker').selectpicker('refresh');
		}
	}) 
}

//表单校验
$(function () {
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
                        message: '微服务名称不能为空'
                    }
                   
                }
            },
            moduleCode: {
                validators: {
                	 notEmpty: {
                         message: '微服务编号不能为空'
                     }
                    
                }
            }
            
          }
    });
  
});




