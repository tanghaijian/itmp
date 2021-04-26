/**
 * Created by 朱颜辞镜花辞树 on 2018/9/18.
 */
//分页所需参数 
var searchArr={};
//总页数
searchArr.total=0;
//当前页
searchArr.pageNum=1; 
//页面条目数
searchArr.row=[10,20,30]; 
//当前页面条目数
searchArr.select=searchArr.row[0];
//查询条件
var environmentTypeData;
//自动部署部署方式
searchArr.postData={
	"pageNum":searchArr.pageNum,
	"pageSize":searchArr.select, 
	"systemInfo":{},
	"scmBuildStatus":$("#scmBuildStatus").val()
}; 
//查询参数 
searchArr.postData.systemInfo.systemName = $("#seacrhSystemName").val();
searchArr.postData.systemInfo.systemCode =  $("#seacrhSystemNum").val();
searchArr.postData.systemInfo.projectId = $("#seacrhProject").val();  
$("#loading").css('display','block');
var allData=[];
var checkBoxDataArr=[]; 
var sonarData; 
 
$(function(){  
	getEnvironmentType();
	pageInit(); 
	dateComponent();
	addseacrhProjectOption();  
	//setTimeout("searchInfo()",1000*60);
	var t1=window.setInterval(autoSearchInfo, 1000*60);
	
// 搜索框 收藏按钮 js 部分
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
    $(".collection").click(function () {
        if( $(this).children("span").hasClass("fa-heart-o") ){
            $(this).children("span").addClass("fa-heart");
            $(this).children("span").removeClass("fa-heart-o");
        }else {
            $(this).children("span").addClass("fa-heart-o");
            $(this).children("span").removeClass("fa-heart");
        }
    })
    //所有的Input标签，在输入值后出现清空的按钮
    $('input').parent().css("position","relative");
    $('input').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $("input").bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    }) 
    $("#sonarTime").bind("input propertychange",function(){ 
    	
    })
});

function addseacrhProjectOption(){ 
	$.ajax({
  		url:"/devManage/structure/getAllproject",
    	method:"post",  
  		success:function(data){  
  			 for(var i=0;i<data.list.length;i++){
  				 $("#seacrhProject").append("<option value="+data.list[i].id+">"+data.list[i].projectName+"</option>");
  			 } 
	  		 $('.selectpicker').selectpicker('refresh'); 
        },
  		error:function(){ 
  		}
 	});
}
//表格数据加载
function pageInit(){
	checkBoxDataArr=[];
	allData=[];
	$("#list2").jqGrid("clearGridData");
    $("#list2").jqGrid({ 
    	 url:'/devManage/deploy/getAllManualSystemInfo',  
         datatype: "json", 
         height: 'auto',  
         mtype : "POST", 
         cellEdit:true,  
         postData:{
         	"pageNum":searchArr.pageNum,
         	"pageSize":searchArr.row[0],
         },
         width: $(".content-table").width()*0.999,
         colNames:['','id','key_id','子系统编号', '子系统名称','微服务类型','上次部署时间','操作'],
         colModel:[{
        	  	 name:'checkBox',
        		 index:'checkBox',
        		 width:20,
        		 formatter : function(value, grid, rows, state) {
                     var row = JSON.stringify(rows).replace(/"/g, '&quot;'); 
                     if( rows.createType==2 ){
                    	 return '<input type="checkbox"  disabled="true"  />'; 
                     }else if( rows.createType==1 ){
                    	 return '<input type="checkbox" id="check'+rows.key_id+'" onclick="isChecked( '+row+',this )" />'; 
                     }
                 } 
        	 },
            {name:'id',index:'id',hidden:true}, 
            {name:'key_id',index:'key_id',hidden:true,key:true},
            {name:'systemCode',index:'systemCode'},
            {name:'systemName',index:'systemName', formatter : function(value, grid, rows, state) {
            	var str=rows.systemName;
            	if( rows.architectureType==1 ){
            		str+=" <sup class='micSup'>微服务架构</sup>"
            	} 
                return str; 
            }},
           
            
            {name:'architectureType',index:'architectureType',hidden : true}, 
            {name:'lastBuildTime',index:'lastBuildTime', formatter : function(value, grid, rows, state){
            	if( rows.level==1 ){
            		if( rows.createType==2 ){
            			return  '';
                	} 
            	}  
            	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
            	if(rows.nowStatus=='true' ){ 
            		if(rows.createType==1){
            			var str='';
            			var arr=rows.nowEnvironmentType.split(",");
            			for( var i=0;i<arr.length;i++ ){
            				str+=arr[i]+"<br>";
            			}
            			return str; 
            		}else if(rows.createType==2){
            			var str='';
            			var arr=rows.nowJobName.split(",");
            			for( var i=0;i<arr.length;i++ ){
            				str+=arr[i]+"<br>";
            			}
            			return str;
            		}else{
            			return "";
            		}  
            	}else{
            		if( rows.buildStatus=='4' ){
                  		 // 没有上次部署时间
                   		return  '';
                   	}else{
                   		var type='';
                   		for(var k in environmentTypeData){
                   			if( k== rows.environmentType ){
                   				type=environmentTypeData[k];
                   			}
                   		} 
                   		if(rows.createType==1){
                   		if( rows.buildStatus==2 ){
                   			
                   			return 	 rows.lastBuildTime+" "+type+"  <span style='color:green;'>成功</span>";
                   		}else if( rows.buildStatus==3 ){
                   			return 	 rows.lastBuildTime+" "+type+"  <span style='color:red;'>失败</span>";
                   		} 
                   	} else{
                        if( rows.buildStatus==2 ){
                   			
                   			return 	 rows.lastBuildTime+" "+rows.lastJobName+"  <span style='color:green;'>成功</span>";
                   		}else if( rows.buildStatus==3 ){
                   			return 	 rows.lastBuildTime+" "+rows.lastJobName+"  <span style='color:red;'>失败</span>";
                   		} 
                   		
                   	} 
                   	}
            	} 
            }},
            {
                name:'操作',
                index:'操作',
                align:"center",
                fixed:true,
                sortable:false,
                resize:false,
                classes: 'optClass',
                search: false,
                width:240,
                formatter : function(value, grid, rows, state) {  
                	var type='';  
            		for(var k in environmentTypeData){
               			if( k== rows.nowEnvironmentType ){
               				type=environmentTypeData[k];
               			}
               		} 
                	var row = JSON.stringify(rows).replace(/"/g, '&quot;'); 
                	if( rows.level==1 ){
                		 
                	}else if( rows.level==0 ){
                			var str='<li role="presentation" class="dropdown">';
                			if( rows.createType==1 ){
                				 var envids="";
                                if(rows.choiceEnvids==""){
                					 
                				 }else{
                				 var envs=rows.choiceEnvids.split(",");
                				 for(var i=0;i<envs.length;i++){
                					 var id=envs[i].split(":")[0];
                					 var value=envs[i].split(":")[1];
                					 envids+='<li><a name='+id+' onclick="startBuilding('+ row + ',this)">'+value+'</a></li>';
                					 
                				 }
                				 }
                				 
//                    		     if(rows.envids.indexOf("1") < 0){
//                    		    	 envids+='<li><a name="1" onclick="startBuilding('+ row + ',this)">DEV</a></li>';
//                    		     }
//                    		     if(rows.envids.indexOf("2") <0){
//                    		    	 envids+='<li><a name="2" onclick="startBuilding('+ row + ',this)">SIT</a></li>';
//                    		     }
//                    		     if(rows.envids.indexOf("3")< 0){
//                    		    	 envids+='<li><a name="3" onclick="startBuilding('+ row + ',this)">UAT</a></li>';
//                    		     }
//                    		     if(rows.envids.indexOf("4") < 0){
//                    		    	 envids+='<li><a name="4" onclick="startBuilding('+ row + ',this)">PRD</a></li>';
//                    		     }
                    		     str+='<a class="dropdown-toggle a_style" data-toggle="dropdown" role="button"><span class="fa fa-cog"></span> 部署 <span class="fa fa-angle-down"></span></a>'+
                                 '<ul class="dropdown-menu" >' +envids+ 
                                 '</ul></li>'; 
                            }else if( rows.createType==2 ){
                				str+= '<a class="dropdown-toggle a_style" data-toggle="dropdown" onclick="getAllJobName( this,'+row+' )" role="button"><span class="fa fa-cog"></span> 部署 <span class="fa fa-angle-down"></span></a>'+
                                '<ul class="dropdown-menu" ></ul></li>'; 
                			}
                		    str+=' | <li role="presentation" class="dropdown">' +
                            '<a class="dropdown-toggle a_style" data-toggle="dropdown" role="button">'+
                            '</span> 查看历史 <span class="fa fa-angle-down"></span></a>'+
                            '<ul class="dropdown-menu" >' +
                            '<li><a class="a_style" onclick="buildHistory('+ row + ')">部署历史</a></li>';
                		    if( rows.createType==1 ){
                		    	str+= '<li><a class="a_style" onclick="sonarHistory('+ row + ')">扫描历史</a></li>';
                		    }
                		    str+='</ul></li>'+
                			' | <a class="a_style"  onclick="scheduledTask('+ row + ')"   >定时任务</a> ';
                		    return str;
                		    
                			   
                	} 
                	return '';
                 }
            }
        ],   
        treeGrid: true,
        treeGridModel: "adjacency",
        ExpandColumn: "systemCode",
        treeIcons: {plus:'fa fa-caret-right',minus:'fa fa-caret-down',leaf:''},
        sortname:"key_id",
        sortable:true,
        ExpandColClick: true,
        jsonReader: {
            repeatitems: false,
            root: "rows",  
        }, 
        loadui: "disable",
        pager: '#pager',  
        loadComplete :function(xhr){ 
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
    }).trigger('reloadGrid');  
} 

//添加翻页栏
function addPage(searchArr){   
	 
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
	
	str+='<td id="input_pager" dir="ltr" style="position: relative;"> <input  class="pageIn ui-pg-input form-control" type="text" size="2" maxlength="7" value='+searchArr.pageNum+' role="textbox"> 共 <span id="sp_1_pager">'+Math.ceil( searchArr.total/searchArr.select )+'</span> 页</td>';
	
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
					searchArr.postData.pageNum=$(this).val();
					searchArr.pageNum=searchArr.postData.pageNum; 
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
		searchArr.postData.pageNum=searchArr.pageNum;
		addPage(searchArr);
		searchInfo();  
	}  
} 
function searchInfo(){ 
	checkBoxDataArr=[];
	allData=[]; 
	 //重新载入 
	$("#loading").css('display','block');
	$("#list2").jqGrid('setGridParam',{ 
		url:"/devManage/deploy/getAllManualSystemInfo", 
        postData:searchArr.postData, 
        loadComplete :function(xhr){
        	allData=xhr.rows; 
        	searchArr.total=xhr.total;  
        	$("#loading").css('display','none'); 
        	addPage(searchArr);
        },  
    }).trigger("reloadGrid");  
}

function autoSearchInfo(){ 
	

	
//	checkBoxDataArr=[];
//	allData=[]; 
//	 //重新载入 
	$("#list2").jqGrid('setGridParam',{ 
		url:"/devManage/deploy/getAllManualSystemInfo", 
        postData:searchArr.postData, 
        loadComplete :function(xhr){
        	allData=xhr.rows; 
        	searchArr.total=xhr.total;  
        	addPage(searchArr);
        },  
    }).trigger("reloadGrid");  
}

function changeSelect(This){  
	searchArr.select=$(This).val(); 
	searchArr.pageNum=1;  
	searchArr.postData={
			"pageNum":searchArr.pageNum,
			"pageSize":searchArr.select, 
	} 
	searchInfo(); 
}
function searchBoxInfo(){ 
	searchArr.pageNum=1; 
	searchArr.postData.systemInfo={
		"systemName" : $("#seacrhSystemName").val(),
		"systemCode" : $("#seacrhSystemNum").val(),
		"projectId" : $("#seacrhProject").val(),  
	}
	searchArr.postData={
			"pageNum":searchArr.pageNum,
			"pageSize":searchArr.select, 
			"systemInfo":JSON.stringify(searchArr.postData.systemInfo),
			"scmBuildStatus":$("#scmBuildStatus").val()
	}  
	searchInfo();
	addPage(searchArr);
}  
function startBuilding(row,This){   
	var env=$(This).attr("name")
	var row = JSON.stringify(row).replace(/"/g, '&quot;');  
	$("#promptBox .modal-footer2").empty();
	$("#promptBox .modal-footer2").append('<button type="button" class="btn btn-primary" onclick="commitInfo('+row+','+env+')">确定</button><button type="button" class="btn btn-default" data-dismiss="modal">取消</button>')
	$("#normalService").modal("hide"); 
	$("#promptBox").modal("show"); 
}
function commitInfo(row,env){   
	var modules='';
	if( row.architectureType==1 ){
		for(var key in checkBoxDataArr){
			if(checkBoxDataArr[key].parent==row.key_id){
				modules+= checkBoxDataArr[key].id +",";
			}
		}
		if( modules=='' ){
			alert( '未选择子服务，请选择子服务后尝试部署！' );
			return ;
		} 
	}  
	
	$("#loading").css('display','block');
	$.ajax({
  		url:"/devManage/deploy/buildJobAutoDeploy",
    	method:"post", 
    	data:{
    		sysId:row.id,
    		systemName:row.systemName,
    		serverType:row.architectureType,
    		modules:modules,
    		env:env,
    	},
  		success:function(data){ 
  			if (data.status == 2) {
  				alert(data.message);
  				$("#promptBox").modal("hide");
  	  			$("#loading").css('display','none');
  	  			return;
  			}
  			pageInit();
  			$("#promptBox").modal("hide");
  			$("#loading").css('display','none');
        },
  		error:function(){ 
  		}
 	}); 
}

function buildHistory(row){ 
	$("#loading").css('display','block');
	$("#buildHistoryTable").bootstrapTable('destroy');  
	$("#buildHistoryTable").bootstrapTable({
		url:"/devManage/deploy/getAllDeployMessage",
		method:"post",
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8', 
    	pagination : true,  
        queryParamsType : "", 
        sidePagination: "server",   
        showHeader:false,
        queryParams : function(params) { 
            var param = {
            	pageSize : params.pageSize,
                pageNumber : params.pageNumber,
                systemId : row.id, 
                createType:row.createType
            }; 
            return param;
        },
        columns : [{
            field : "id",
            title : "id", 
            align : 'left', 
            visible : false
        },{
            field : "envType",
            title : "envType",
            align : 'left',
            formatter : function(value, row, index) { 
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');  
                var classA='';
  				var spanFont=''; 
  				var type=''
  				if(row.buildStatus==3){
  					classA="fa-close iconFail2";
  					spanFont='部署失败';
  				}else if(row.buildStatus==2){
  					classA="fa-check iconSuccess2";
  					spanFont='部署成功';
  				}else {
  					return '';
  				} 
  				 
  				if(row.createType==1){  
  	           		for(var k in environmentTypeData){
  	           			if( k== row.createType ){
  	           				type=environmentTypeData[k];
  	           			}
  	           		} 
  				 }else if(row.createType==2){
  					type=row.jobName;
  				 }  
            	return '<div class="allInfo"><div class="rowdiv buildHistoryInfo"><div class="form-group col-md-4"  style="padding-top: 6px;"><div class="successInfo2"><span class="fa '+classA+'"></span> '+spanFont+" "+type+'</div></div><div class="form-group col-md-3 " style="padding-top: 6px;"><label class="col-sm-5 control-label fontWeihgt">开始部署时间：</label><label class="col-sm-7 control-label font_left">'+row.startDate+'</label></div><div class="form-group col-md-3" style="padding-top: 6px;"><label class="col-sm-5 control-label fontWeihgt">结束部署时间：</label><label class="col-sm-7 control-label font_left">'+row.endDate+'</label></div><div class="form-group col-md-1"><button type="button" onclick="detailInfo('+rows+','+index+')" class="btn infoBtm">查看信息</button></div></div></div>';
            }
        }],
        onLoadSuccess:function(){
        	$("#buildHistoryDIV").modal("show"); 
        	$("#loading").css('display','none');
        },
        onLoadError : function() {

        }
    }); 
	 
}
function detailInfo( row , index ){  
	if( row.buildStatus==2 ){
		$("#buildingFail").css("display","none");
		$("#buildingSuccess").css("display","block");
	}else if( row.buildStatus==3 ){
		$("#buildingFail").css("display","block");
		$("#buildingSuccess").css("display","none");
	}
	$("#buildLogsInfoPre").text();
	$("#loading").css('display','block'); 
	$.ajax({
  		url:"/devManage/deploy/getDeployMessageById",
    	method:"post", 
    	data:{
    		jobRunId: row.id, 
    		createType: row.createType
    	},
  		success:function(data){ 
  			 $("#SonarResult").empty(); 
  			  $("#detailInfoSuccessStartTime").text( data.startDate ); 
  			  $("#detailInfoSuccessEndDate").text( data.endDate ); 
  			  $("#buildLogsInfoPre").text(data.buildLogs);
  			  $("#scanningTime").text(data.endDate);
  			  var str='';
  			  for(var i=0;i<data.list.length;i++){
  				str+='<div class="oneResult">'+ 
  				'<input type="hidden" class="di_projectKey" value='+data.list[i].projectKey+' />'+
  				'<input type="hidden" class="di_toolId" value='+data.list[i].toolId+' />'+
  				'<input type="hidden" class="di_projectDateTime" value='+data.list[i].projectDateTime+' />'+
  				'<div class="rowdiv">';
  				if( data.list[i].moduleame!=undefined ){
  					str+='<div class="form-group col-md-6"><label class="col-sm-4 control-label fontWeihgt">名称：</label><label class="col-sm-8 control-label font_left">'+data.list[i].moduleame+'</label></div>';
  				} 
  				str+='<div class="form-group col-md-6"><label class="col-sm-4 control-label fontWeihgt">结束部署时间：</label><label class="col-sm-8 control-label font_left">'+data.list[i].xValue+'</label></div></div>'+
                '<div class="rowdiv" style="display: flex;flex-direction: row;">';
  				
  				if( index == 0 ){
  					str+='<div class="logTypeDiv" value="BUG" onclick="showDetails(this)"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].bug+'</span>&nbsp;&nbsp;<span class="resultIcon1"></span></div><div><span class="fa fa-bug fontIconSize"></span><span>bugs</span></div></div>'+
  					'<div class="logTypeDiv" value="VULNERABILITY" onclick="showDetails(this)"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].Vulnerabilities+'</span>&nbsp;&nbsp;<span class="resultIcon2"></span></div><div><span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities</span></div></div>'+
  					'<div class="logTypeDiv" value="CODE_SMELL" onclick="showDetails(this)"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].CodeSmells+'</span>&nbsp;&nbsp;<span class="resultIcon3"></span></div><div><span class="fa fa-support fontIconSize"></span><span>Code Smells</span></div></div>';
  				}else{
  					if(row.manualFrist=='true'){
  						str+='<div class="logTypeDiv" value="BUG" onclick="showDetails(this)"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].bug+'</span>&nbsp;&nbsp;<span class="resultIcon1"></span></div><div><span class="fa fa-bug fontIconSize"></span><span>bugs</span></div></div>'+
  	  					'<div class="logTypeDiv" value="VULNERABILITY" onclick="showDetails(this)"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].Vulnerabilities+'</span>&nbsp;&nbsp;<span class="resultIcon2"></span></div><div><span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities</span></div></div>'+
  	  					'<div class="logTypeDiv" value="CODE_SMELL" onclick="showDetails(this)"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].CodeSmells+'</span>&nbsp;&nbsp;<span class="resultIcon3"></span></div><div><span class="fa fa-support fontIconSize"></span><span>Code Smells</span></div></div>';
  						
  					}else{
  					
  					str+='<div class="logTypeDiv" value="BUG"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].bug+'</span>&nbsp;&nbsp;<span class="resultIcon1"></span></div><div><span class="fa fa-bug fontIconSize"></span><span>bugs</span></div></div>'+
  					'<div class="logTypeDiv" value="VULNERABILITY"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].Vulnerabilities+'</span>&nbsp;&nbsp;<span class="resultIcon2"></span></div><div><span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities</span></div></div>'+
  					'<div class="logTypeDiv" value="CODE_SMELL"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].CodeSmells+'</span>&nbsp;&nbsp;<span class="resultIcon3"></span></div><div><span class="fa fa-support fontIconSize"></span><span>Code Smells</span></div></div>';
  					}
  				}
  				str+='<div class="logTypeDiv"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].Coverage+'</span>&nbsp;&nbsp;<span class="resultIcon4"></span></div><div><span class="fa fa-file-text fontIconSize"></span><span>Coverage</span></div></div>'+
				'<div class="logTypeDiv" style="border:none;"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].duplications+'</span>&nbsp;&nbsp;<span class="resultIcon4"></span></div><div><span class="fa fa-refresh fontIconSize"></span><span>Duplications</span></div></div></div>'+
				'<div class="questionList"></div></div>';
  			  } 
  			  $("#SonarResult").append( str );
  			  $("#loading").css('display','none');
  			  $("#detailInfoDIV").modal("show");
        }, 
  		error:function(){ 
  		}
 	});  	  
}
function creatAllJob(This){ 
	if( checkBoxDataArr.length==0 ){
		alert("请选择服务！");
		return ; 
	}else{ 
		var data=[];
		for(var key in checkBoxDataArr){ 
			if( checkBoxDataArr[key].level==0 ){
				var obj={};
				obj.sysId=checkBoxDataArr[key].id;
				obj.systemName=checkBoxDataArr[key].systemName;
				obj.serverType=checkBoxDataArr[key].architectureType; 
				obj.modules='';
				data.push( obj );
			}else if( checkBoxDataArr[key].level==1 ){
				var flag=true;
				for( var i in data ){ 
					if( checkBoxDataArr[key].parent == data[i].key_id ){ 
						data[i].modules+=checkBoxDataArr[key].id+",";
						flag=false; 
					} 
				}  
				if( flag==true ){
					for( var i in allData ){
						if( allData[i].key_id==checkBoxDataArr[key].parent ){
							var obj={};
							obj.sysId=allData[i].id;
							obj.systemName=allData[i].systemName;
							obj.serverType=allData[i].architectureType;
							obj.key_id=allData[i].key_id;
							obj.modules=checkBoxDataArr[key].id+",";
							data.push( obj );
						}
					}
				}  
			} 	
		}   
		var i=0;
		for( var j=0;j<data.length;j++ ){
			if(data[i].architectureType==1){
				i++;
			}
		}
		if( i>10 ){
			alert(" 普通服务的数量不得超过10个！");
			return;
		}else{  
		  $.ajax({
		  		url:"/devManage/deploy/creatJenkinsDeployJobBatch",
		    	method:"post", 
		    	data:{
		    		data:JSON.stringify( data ),
		    		env:$(This).attr("name"),
		    	},
		  		success:function(data){ 
		  			if (data.status == 2) {
		  				alert(data.message);
		  	  			return;
		  			}
		  			setTimeout("searchBoxInfo()", 1000 )  
		        },
		  		error:function(){ 
		  		}
		 	});  
		}
	} 
}

function timeCycle(This){ 
	$(".leftMenu_ul").empty();
	$("#sonarDate a").removeClass('chooseItem');
	$(This).addClass('chooseItem');
	var id=$("#sonarRowId").val();
	var type=$("#sonarRowType").val();
	var createType=$("#sonarRowCreateType").val();
	var startDate;
	var endDate;   
	var arr=[];
	if( $(This).attr("idValue")==0 ){
		startDate=getTodayDate();  
		endDate=getTodayDate();
	}else if( $(This).attr("idValue")==1 ){
		startDate=getWeekStartDate();
		endDate=getWeekEndDate();
	}else if( $(This).attr("idValue")==2 ){
		startDate=getMonthStartDate();
		endDate=getMonthEndDate();
	}else if( This.element.attr("idValue")==3 ){
		arr = $("#sonarTime").val().split(' - '); 
		startDate=arr[0];
		endDate=arr[1]; 
	}     
	$("#sonarTime").val( startDate+" - "+endDate );
	requestSonarHistory( id , type , startDate , endDate ,createType); 
}

function sonarHistory( row ){ 
	console.log( row );
	$(".leftMenu_ul").empty(); 
	$("#sonarDate a").removeClass("chooseItem");
	$("#sonarDate a[idValue='1']").addClass("chooseItem");
	$("#sonarHistoryTitle").text( row.systemCode+" "+row.systemName+" 扫描历史" );
	$("#sonarRowId").val( row.id  ); 
	$("#sonarRowType").val( row.architectureType ); 
	$("#sonarRowCreateType").val( row.createType );
	var startDate=getWeekStartDate();
	var endDate=getWeekEndDate(); 
	$("#sonarTime").val( startDate+" - "+endDate );
	requestSonarHistory( row.id , row.architectureType , startDate , endDate , row.createType );    
}
function requestSonarHistory( id , type ,startDate ,endDate ,createType){ 
	if( createType==1 ){
		if( type==1 ){ 
			$.ajax({
		  		url:"/devManage/deploy/getSonarMessageMincro",
		    	method:"post", 
		    	data:{
		    		systemId:id,
		    		startDate:startDate,
		    		endDate:endDate,
		    	},
		  		success:function(data){
		  			showMincroEchartsContent( data ); 
		        }, 
		 	});
		}else if( type==2 ){  
			$.ajax({
		  		url:"/devManage/deploy/getSonarMessage",
		    	method:"post", 
		    	data:{
		    		systemId:id,
		    		startDate:startDate,
		    		endDate:endDate,
		    	},
		  		success:function(data){    
		  			showEchartsContent( data ); 
		        }, 
		 	});
		} 
	}else if( createType==2 ){
		$.ajax({
	  		url:"/devManage/deploy/getSonarMessageManual",
	    	method:"post", 
	    	data:{
	    		systemId:id,
	    		startDate:startDate,
	    		endDate:endDate,
	    	},
	  		success:function(data){    
	  			showMincroEchartsContent( data );  
	        }, 
	 	});
		
	}
	$("#sonarHistory").modal("show");
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
//清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
} 

function clearSearch() {
    $('#seacrhSystemName').val("");
    $('#seacrhSystemNum').val("");
    $("#seacrhProject").selectpicker('val', ''); 
}
 
function isChecked( row,That ){  
	if( $( That ).is(':checked')==true ){
		if( row.level==0 ){
			if( row.architectureType==1 ){
				 //选中微服务下所有子服务  
				//判断 是否有 子节点 已经 被选中 如果有 先清空，然后重新 循环找出所有子节点 
				for( var i in checkBoxDataArr ){ 
					if( checkBoxDataArr[i].parent==row.key_id ){
						checkBoxDataArr.splice($.inArray(checkBoxDataArr[i],checkBoxDataArr),1);
					 }  
				}  
				for( var i in allData ){
					 if( allData[i].parent==row.key_id ){
						 checkBoxDataArr.push( allData[i] );  
						 $("#check"+allData[i].key_id ).prop("checked",true);  
					 }  
				} 
			}else if( row.architectureType==2 ){  
				checkBoxDataArr.push( row ); 
			}
		}else if( row.level==1 ){
			checkBoxDataArr.push( row );
		}   
	}else{  
		//先取消 子节点 在删除其 选中节点  
		for( var i in allData ){
			 if( row.key_id==allData[i].parent ){
				 $("#check"+allData[i].key_id ).prop("checked",false);  
				 checkBoxDataArr.splice($.inArray(allData[i],checkBoxDataArr),1);
			 }  
		} 
		for( var i in checkBoxDataArr ){
			if( row.key_id==checkBoxDataArr[i].key_id ){  
				 checkBoxDataArr.splice($.inArray(checkBoxDataArr[i],checkBoxDataArr),1);
			} 
		}   
	}  
}  

function stringToArr( data ){ 
	 var len = data.length;//str为要处理的字符串 
	 result = data.substring(1,len-1);//result为你需要的字符串 
	 return result.split(',');
}
function showEchartsContent( data ){
	$( "#sonarHistory .leftMenu" ).css("display","none"); 
	$( ".sonarTable" ).css("width","1008"); 
	var tableNum=1;
	if(data.status==2){
		$(".sonarTable").css("display","none");
		$(".promptInformation").css("display","block");
		return ;
	}
	$(".sonarTable").css("display","block");
	$(".promptInformation").css("display","none");
	for(var key in data){
		if( key=="Bugs" || key=="Code Smells" || key=="Coverage" || key=="Duplications" || key=="Vulnerabilities" ){ 
			echarts.init( document.getElementById('sonarTable'+tableNum) ).dispose();  
			echarts.init( document.getElementById('sonarTable'+tableNum) ).setOption( bulidEcharts( key, data ) ); 
			tableNum++;
		}
	} 
}
function showMincroEchartsContent( data ){  
	if( data.list.length==0 ){
		$( "#sonarHistory .leftMenu" ).css("display","none"); 
		$( ".sonarTable" ).css("width","1008"); 
		$(".sonarTable").css("display","none");
		$(".promptInformation").css("display","block");
		return ;
	}
	sonarData=data.list;
	$( "#sonarHistory .leftMenu" ).css("display","block"); 
	$( ".sonarTable" ).css("width","898"); 
	$(".sonarTable").css("display","block");
	$(".promptInformation").css("display","none");
	var tableNum=1;
	if( data.createType==1 ){
		for( var i=0;i<data.list.length;i++){
			if(i==0){
				$(".leftMenu_ul").append("<li class='chooseItem2' onclick='changeMincroEcharts(this,"+i+")'>"+ data.list[i].moduleName +"</li>")
				for( var key in data.list[i] ){
					if( key=="Bugs" || key=="Code Smells" || key=="Coverage" || key=="Duplications" || key=="Vulnerabilities" ){ 
						echarts.init( document.getElementById('sonarTable'+tableNum) ).dispose();  
						echarts.init( document.getElementById('sonarTable'+tableNum) ).setOption( bulidEcharts( key, data.list[i] ) ); 
						tableNum++;
					}
				} 
			}else{
				$(".leftMenu_ul").append("<li onclick='changeMincroEcharts(this,"+i+")'>"+ data.list[i].moduleName +"</li>")
			}   
		}
	}else if( data.createType==2 ){
		for( var i=0;i<data.list.length;i++){
			if(i==0){
				$(".leftMenu_ul").append("<li class='chooseItem2' onclick='changeMincroEcharts(this,"+i+")'>"+ data.list[i].jobName +"</li>")
				for( var key in data.list[i] ){
					if( key=="Bugs" || key=="Code Smells" || key=="Coverage" || key=="Duplications" || key=="Vulnerabilities" ){ 
						echarts.init( document.getElementById('sonarTable'+tableNum) ).dispose();  
						echarts.init( document.getElementById('sonarTable'+tableNum) ).setOption( bulidEcharts( key, data.list[i] ) ); 
						tableNum++;
					}
				} 
			}else{
				$(".leftMenu_ul").append("<li onclick='changeMincroEcharts(this,"+i+")'>"+ data.list[i].jobName +"</li>")
			}   
		}
	} 
	 
	 
}
function changeMincroEcharts( This,i ){ 
	$(".leftMenu_ul li").removeClass('chooseItem2');
	$(This).addClass('chooseItem2'); 
	var tableNum=1;
	for( var key in sonarData[i] ){
		if( key=="Bugs" || key=="Code Smells" || key=="Coverage" || key=="Duplications" || key=="Vulnerabilities" ){ 
			echarts.init( document.getElementById('sonarTable'+tableNum) ).dispose();  
			echarts.init( document.getElementById('sonarTable'+tableNum) ).setOption( bulidEcharts( key, sonarData[i] ) );
			tableNum++;
		}
	}
}
function bulidEcharts(key, data ){ 
	var option={
	          title: {
	              text: key ,
	              top:  6,
	              left: 8,
	              textStyle:{
	            	  fontSize:12,
	            	  color:'#666666',
	              } 
	            },
	            tooltip: {
	            	trigger: 'none',
	                axisPointer: {
	                    type: 'cross'
	                }
	            },
	            backgroundColor: '#F4F4FB', 
	            xAxis: {
	              type: 'category', 
	              boundaryGap: false,
	              data: stringToArr( data.xValue ) ,
	              axisLabel:{ 
	            	show: false,  
	              	interval:0 ,
	              	rotate:-20
	          	  },
	          	  axisPointer: {
	                label: {
	                    formatter: function (params) {
	                        return  params.value + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
	                    }
	                }
	            },
	          },
	          yAxis: {
	          	splitLine:{show : false},
	            type: 'value', 
	          },
	          grid: {  
	          	left: '15',  
	          	right: '15',  
	          	bottom: '20', 
	          	top: '40',
	            borderWidth:0, 
	          	containLabel: true  
	          }, 
	          /*dataZoom:{
	          	realtime:true, //拖动滚动条时是否动态的更新图表数据
	          	height:15,//滚动条高度
	          	start:0,//滚动条开始位置（共100等份）
	          	end:20//结束位置（共100等份）
	        	}, */ 
	          series: [{
	              data: stringToArr( data[key] ),
	              type: 'line',
	              smooth: true,
	              areaStyle: { 
	              	color:['#D2E5F8']
	              },
	              itemStyle : {  
	            	normal : {  
	            		lineStyle:{  
	                      color:'#49A9EE'  
	            		}  
	             	}  
	              },
	          }]
	      };
	return option;
}
 
function showDetails(This){  
	
	var value='';
	value = "."+$(This).attr( "value" ); 
	if( $(This).parent().parent().find(".questionList").find( value ).length > 0 ){
		 $(This).parent().parent().find(".questionList .listTitle .fa-angle-double-up").addClass("fa-angle-double-down"); 
		 $(This).parent().parent().find(".questionList .listTitle .fa").removeClass("fa-angle-double-up"); 
		 $(This).parent().parent().find(".questionList .questionContent").css("display","none"); 
		 $(This).parent().parent().find(".questionList "+value+" .fa-angle-double-down").addClass("fa-angle-double-up");
		 $(This).parent().parent().find(".questionList "+value+" .fa-angle-double-up").removeClass("fa-angle-double-down");
		 $(This).parent().parent().find(".questionList "+value+" .questionContent").css("display","block");
	}else{
		if( $(This).parent().parent().children(".di_projectKey").val()=='null' ){
			
			//alert("手动部署，没有配置sonar扫描!");
			alert("部署，没有配置sonar扫描!");
		}else{
			$("#loading").css('display','block');
			 $(This).parent().parent().find(".questionList .listTitle .fa-angle-double-up").addClass("fa-angle-double-down");
			 $(This).parent().parent().find(".questionList .listTitle .fa").removeClass("fa-angle-double-up"); 
			 $(This).parent().parent().find(".questionList .questionContent").css("display","none");  
			 loadingPage( This ); 
		} 
	} 
}
function loadingPage( This ){  
	var page=1;
	$.ajax({
		url:"/devManage/deploy/getSonarIssule",
		method:"post", 
		data:{
			toolId:$(This).parent().parent().children(".di_toolId").val(),
			dateTime:$(This).parent().parent().children(".di_projectDateTime").val(),
			projectKey:$(This).parent().parent().children(".di_projectKey").val(),
			type:$(This).attr( "value" ),
			p:page,
		},
		success:function(data){  
			$(This).parent().parent().children(".questionList").children(".sonarList").children(".questionContent").children(".questionFooter").remove();
			var data=JSON.parse( data );
			var str='<div class="sonarList '+$(This).attr( "value" )+'" ><div class="listTitle">'; 
			if( $(This).attr( "value" )=='BUG' ){
				str+='<span class="fa fa-bug fontIconSize"></span><span>Bugs 问题清单</span>';
			}else if( $(This).attr( "value" )=='CODE_SMELL' ){
				str+='<span class="fa fa-support fontIconSize"></span><span>Code Smells 问题清单</span>';
			}else if( $(This).attr( "value" )=='VULNERABILITY' ){
				str+='<span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities 问题清单</span>';
			}  
			str+='<span class="fa fa-remove smallIcon" onclick="romoveSonarList(this)"></span> <span class="fa fa-angle-double-up smallIcon" onclick="down2(this)"></span></div><div class="questionContent"></div></div>'; 
			$(This).parent().parent().children(".questionList").append( str );
			var lastPath=''; 
			for(var i=0;i<data.issues.length;i++){
				var onesDiv='';
				if(lastPath!=data.issues[i].component){
					onesDiv +='<div class="questionPath"> '+ data.issues[i].component +' </div>';
				}
				onesDiv +='<div class="questionBody"><div class="rowdiv"><div class="erorrCont fontWeihgt def_col_31">'+ data.issues[i].message +'</div><div class="lineCont def_col_5 font_right">Line:'+ data.issues[i].line +'</div></div>'+
	  			'<div class="rowdiv"><span>'+ data.issues[i].author +'</span>&nbsp;&nbsp;&nbsp;<span>'+ data.issues[i].creationDate.replace(/T/,' ') +'</span></div></div>';
  				
				lastPath=data.issues[i].component;
				$(This).parent().parent().children(".questionList").children(".sonarList").children(".questionContent").append( onesDiv );
			} 
			if( Math.ceil(data.total/data.ps)==data.p || data.total <= data.ps ){
				var footer='<div class="questionFooter">显示 <span>'+data.total+'</span> / <span>'+data.total+'</span></div>'; 	
			}else{   
				var footer='<div class="questionFooter">显示 <span>'+data.p*data.ps+'</span> / <span>'+data.total+'</span> <span class="a_style" value='+page+' onclick="moreLoadingPage(this)">更多...</span></div>';  
			}
			$(This).parent().parent().children(".questionList").children(".sonarList").children(".questionContent").append( footer );
			$("#loading").css('display','none');
		}, 
	}); 
	  
}
function moreLoadingPage(This){ 
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/deploy/getSonarIssule",
		method:"post", 
		data:{
			toolId:$(This).parent().parent(".questionContent").parent(".sonarList").parent(".questionList").parent(".oneResult").children(".di_toolId").val(),
			dateTime:$(This).parent().parent(".questionContent").parent(".sonarList").parent(".questionList").parent(".oneResult").children(".di_projectDateTime").val(),
			projectKey:$(This).parent().parent(".questionContent").parent(".sonarList").parent(".questionList").parent(".oneResult").children(".di_projectKey").val(),
			type:$(This).parent().parent(".questionContent").parent(".sonarList").attr( "id" ),
			p: ( Number( $(This).attr("value") )+1 ) ,
		},
		success:function(data){  
			var node=$(This).parent(".questionFooter").parent(".questionContent"); 
			var data=JSON.parse( data ); 
			var lastPath=''; 
			for(var i=0;i<data.issues.length;i++){
				var onesDiv='';
				if( lastPath=='' ){ 
				}else{
					if(lastPath!=data.issues[i].component){
						onesDiv +='<div class="questionPath"> '+ data.issues[i].component +' </div>';
					}
					onesDiv +='<div class="questionBody"><div class="rowdiv"><div class="erorrCont fontWeihgt def_col_31">'+ data.issues[i].message +'</div><div class="lineCont def_col_5 font_right">Line:'+ data.issues[i].line +'</div></div>'+
		  			'<div class="rowdiv"><span>'+ data.issues[i].author +'</span>&nbsp;&nbsp;&nbsp;<span>'+ data.issues[i].creationDate.replace(/T/,' ') +'</span></div></div>';
	  				
				}
				lastPath=data.issues[i].component;
				node.append( onesDiv );
			} 
			$(This).parent(".questionFooter").remove(); 
			if( Math.ceil(data.total/data.ps)==data.p ){
				var footer='<div class="questionFooter">显示 <span>'+data.total+'</span> / <span>'+data.total+'</span></div>'; 	
			}else{   
				var footer='<div class="questionFooter">显示 <span>'+data.p*data.ps+'</span> / <span>'+data.total+'</span> <span class="a_style" value='+data.p+' onclick="moreLoadingPage(this)">更多...</span></div>';  
			} 
			node.append( footer ); 
			$("#loading").css('display','none');
		} 
	}); 
}
function romoveSonarList(This){
	$(This).parent().parent(".sonarList").remove(); 
}
function down2(This){
	 if( $(This).hasClass("fa-angle-double-down") ){
	        $(This).removeClass("fa-angle-double-down");
	        $(This).addClass("fa-angle-double-up");
	        $(This).parent().parent(".sonarList").children(".questionContent").slideDown(100);
	    }else {
	        $(This).addClass("fa-angle-double-down");
	        $(This).removeClass("fa-angle-double-up");
	        $(This).parent().parent(".sonarList").children(".questionContent").slideUp(100);
	} 
}
function dateComponent(){ 
    var locale = {
        "format": 'yyyy-mm-dd',
        "separator": " -222 ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "起始时间",
        "toLabel": "结束时间'",
        "customRangeLabel": "自定义",
        "weekLabel": "W",
        "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
        "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        "firstDay": 1
    };
	$("#sonarTime").daterangepicker({'locale': locale}); 
}
function getAllJobName(This,rows){ 
	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
	$.ajax({
  		url:"/devManage/deploy/getDeployJobName",
    	method:"post", 
    	data:{
    		systemId:rows.id, 
    	},
  		success:function(data){  
  			$(This).next(".dropdown-menu").empty();
  			for( var i=0;i<data.list.length;i++ ){
  				var uid=data.list[i].rootPom; 
//  				startManualBuilding()
  				var str='<li><a class="a_style"   onclick="manualBuilding(\''+uid+'\',\''+data.list[i].jobName+'\','+row+')">jobName:'+data.list[i].jobName+'</a></li>';
  				$(This).next(".dropdown-menu").append( str );
  			} 
        }, 
 	});  
}
function manualBuilding( id ,jobName,rows){ 
	$.ajax({
  		url:"/devManage/deploy/buildJobManualDeploy",
    	method:"post", 
    	data:{
    		systemJenkisId: id, 
    		jsonParam: "",
    	},
  		success:function(data){  
  			pageInit();
  			//$("#manualBuildingDIV").modal("hide");
  			$("#loading").css('display','none'); 
        }, 
 	});  

	
	
	
	
	$("#manualBuildingTable tbody").empty();
	$.ajax({
  		url:"/devManage/deploy/getJobNameParam",
    	method:"post", 
    	data:{
    		systemJenkisId:id, 
    	},
  		success:function(data){ 
  			$( "#manualJobName" ).text( jobName ); 
  			$( "#thisManualJobName" ).val( data.id )
  			//$("#manualBuildingDIVTitle").html( rows.systemCode+" "+rows.systemName+" <span class='fontWeihgt'>手动部署参数化</span>"); 
  			$("#manualBuildingDIVTitle").html( rows.systemCode+" "+rows.systemName+" <span class='fontWeihgt'>部署参数化</span>"); 
  			$("#manualBuildingDIV").modal("show");   
  			var dataList=$.parseJSON(data.paramJson);
  			var str="";  
            if( dataList.length%2!=0 ){
            	 dataList.push( {} );
            } 
  			for( var i=0;i<dataList.length;i++ ){
  				var tdStr="";
  				if( i%2==0 ){
  					tdStr+='<tr><td class="font_right">';
  				}else{
  					tdStr+='<td class="font_right">';
  				} 
  				if( dataList[i].name ){
  					tdStr+='<span class="paramName">'+dataList[i].name+'</span></td><td>'	
  				}else{
  					tdStr+='</td><td>'
  				}
  				switch( dataList[i].type )
  				{
  				case 'String':  
  					tdStr+='<input type="text" class="form-control" name="my'+dataList[i].type+'" value='+dataList[i].defaultValue+'  />'; 
  				  break;
  				case 'Boolean': 
  					tdStr+='<select class="selectpicker" name="my'+dataList[i].type+'"><option value="">请选择</option>';
  					if( dataList[i].defaultValue==true ){
  						tdStr+='<option value="true" checked>true</option><option value="false">false</option></select>';
  	  				}else{
  	  					tdStr+='<option value="true">true</option><option value="false" selected = "selected" >false</option></select>';
  					} 
  				  break;
  				case 'Choice': 
  					tdStr+='<select class="selectpicker" name="my'+dataList[i].type+'"><option value="">请选择</option>'; 
  					for( var key = 0;key<dataList[i].choices.length;key++ ){ 
  						if(key==0){
  							tdStr+='<option value='+dataList[i].choices[key]+' selected = "selected" >'+dataList[i].choices[key]+'</option>';
  						}else{
  							tdStr+='<option value='+dataList[i].choices[key]+'>'+dataList[i].choices[key]+'</option>'; 
  						}
  					}
  					tdStr+='</select>';
    			  break; 
  				case 'undefined':
  					tdStr+=''; 
  				  break;
  				default:  
  					tdStr+='';
  				}
  				
  				if( i%2==1 ){
  					tdStr+='</td></tr>';
  				}else{
  					tdStr+='</td>';
  				}  
  				str+=tdStr;
  			}
  			 
  			$( "#manualBuildingTable tbody" ).append( str );
  			$('.selectpicker').selectpicker('refresh'); 
        }, 
 	});  
}
function startManualBuilding(){
	$("#loading").css('display','block'); 
	var data=[];  
	for( var i=0;i<$("#manualBuildingTable tbody td").length;i++  ){ 
		(function(i){  
			if(  !( $("#manualBuildingTable tbody td").eq(i).is(":empty") )  ){ 
				 if( $("#manualBuildingTable tbody td").eq(i).find("input[name*='myString']").size()!=0 ){
					 data.push( $("#manualBuildingTable tbody td").eq(i).find("input[name*='myString']").val() );
					 return;
				 }else if( $("#manualBuildingTable tbody td").eq(i).find("select[name*='myBoolean']").size()!=0 ){
					 data.push(  $("#manualBuildingTable tbody td").eq(i).find("select[name*='myBoolean']").val() );
					 return;
				 }else if( $("#manualBuildingTable tbody td").eq(i).find("select[name*='myChoice']").size()!=0 ){
					 data.push( $("#manualBuildingTable tbody td").eq(i).find("select[name*='myChoice']").val() );
					 return;
				 }  
				 data.push( $("#manualBuildingTable tbody td").eq(i).children("span").text() );
			 } 
		})(i)
	} 
	if( data.length%2==0 ){  
		var obj="";
		var id=$("#thisManualJobName").val().toString();
		for(var i=0;i<data.length;i=i+2){ 
			var key,value;
			key=data[i];
			value=data[i+1];
			obj+='"'+key+'":"'+data[i+1]+'",';
		} 
		obj=obj.substr(0,obj.length-1);
		obj="{"+obj+"}";   
		$.ajax({
	  		url:"/devManage/deploy/buildJobManual",
	    	method:"post", 
	    	data:{
	    		systemJenkisId: id, 
	    		jsonParam: obj,
	    	},
	  		success:function(data){  
	  			pageInit();
	  			$("#manualBuildingDIV").modal("hide");
	  			$("#loading").css('display','none'); 
	        }, 
	 	});  
	}else{
		alert( "数据出错！" );
		$("#manualBuildingDIV").modal("hide"); 
	}
	 
}

function getEnvironmentType(){
	$.ajax({
  		url:"/devManage/structure/getEnv",
    	method:"post", 
    	contentType:"application/json;charset=utf-8",
    	async: false,
  		success:function(data){  
  			console.log( data );
  			environmentTypeData=data;
        }, 
 	}); 
}

function refresh(){
	var ids="";
	var obj = $("#list2").jqGrid("getRowData");
	for (i = 0; i < obj.length; i++){
		var name=obj[i].lastBuildTime;
		var  architectureType=obj[i].architectureType;
		if(architectureType=="1" || architectureType=="2"){
		if(name.indexOf("正在部署")>-1){
			ids=ids+obj[i].id+",";
		}
	}
	}
	if(ids!=""){
		ids = ids.substr(0,ids.length-1);
	}
	
} 
function scheduledTask(row){ 
	
	var flag=row.createType;
//	alert(flag);
//	return false;
	var systemId=row.id;
	if(flag=="2"){
		$("#list4").jqGrid("clearGridData");
		//手动部署
		$("#scheduledModal").modal('hide');
		$("#scheduledManualModal").modal('show');
		$("#list4").jqGrid({  
	        url:'/devManage/deploy/getCorn', 
	        datatype: "json", 
	        height: 'auto',  
	        mtype : "post",
	        postData:{
	         	"systemId":row.id,
	         	"type":row.createType,
	         },
	        width: $("#scheduledModal .modal-lg").width()*0.899,
	        colNames:[ 'stringId','手动任务名称','定时表达式','操作'],
	        colModel:[ 
	        	{name:'stringId',index:'stringId',hidden:true}, 
	            {name:'jobName',index:'jobName'},  
	            {
		       		name:'jobCron',
		       		index:'jobCron',
	       			width:100,
	       			formatter : function(value, grid, rows, state) {
	                    var row = JSON.stringify(rows).replace(/"/g, '&quot;'); 
	                	if(rows.jobCron=='null' || rows.jobCron==null){
	                   		return '<input type="text" id='+rows.stringId+' value=" "  />'; 
	                	}else{
	                		return '<input type="text" id='+rows.stringId+' value='+rows.jobCron+'  />'; 
	                	}  
	                } 
	       	    }, 
	            { 
	                name:'操作',
	                index:'操作',
	                align:"center",
	                fixed:true,
	                sortable:false,
	                resize:false,
	                search: false,
	                width:50,
	                formatter : function(value, grid, rows, state) {
	                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');  
	                    return '<a class="a_style" style="cursor:pointer" onclick="setCron('+ row + ','+flag+','+systemId+')">生效</a>'
	                }
	            } 
	        ],  
	        jsonReader: {
	            repeatitems: false,
	            root: "rows", 
	        }, 
	        loadtext:"数据加载中......",	  
	    }).trigger("reloadGrid");  
	}else{  
		
		var systemIds=row.id;
		$("#list3").jqGrid("clearGridData");
		
		var data={
	         	"systemId":systemIds,
	         	"type":row.createType,
	        };
		$("#list3").jqGrid("setGridParam", { postData: data }).trigger("reloadGrid");

		$("#scheduledManualModal").modal('hide');
		$("#scheduledModal").modal('show');
		$("#list3").jqGrid({  
	        url:'/devManage/deploy/getCorn', 
	        datatype: "json", 
	        height: 'auto',  
	        mtype : "post",
	        postData:data,
	        width: $("#scheduledModal .modal-lg").width()*0.899,
	        colNames:[ 'stringId','environmentType','环境名称','定时表达式','操作'],
	        colModel:[ 
        	{name:'stringId',index:'stringId',hidden:true},
            {name:'environmentType',index:'environmentType',hidden:true}, 
            {name:'environmentTypeName',width:100,index:'environmentTypeName',
            	formatter : function(value, grid, rows, state) {
            		var str='';
            		str+='<span class="fontContent">'+rows.environmentTypeName+'</span>'+
            		'<div class="def_tableDiv">'+
            		'<select class="selectpicker" id="deptName"  name='+rows.stringId+'>'+
               			'<option value="">请选择</option>';
	            		for(var k in environmentTypeData){
	            			str+='<option value='+k+'>'+environmentTypeData[k]+'</option>';
	            		}
            		str+='</select></div>';
            		return str;
    	        }
            },  
            {
       	  		name:'jobCron',
       			index:'jobCron', 
       			formatter : function(value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;'); 
                	if(rows.jobCron=='null' || rows.jobCron==null){
                   		return '<input id='+rows.stringId+' type="text"  value=" "  />'; 
                	}else{  
                		return '<input id='+rows.stringId+'  type="text" value="'+rows.jobCron+'" />'; 
                	} 
                } 
       	 	}, 
       	 	{ 
            	name:'操作',
            	index:'操作',
            	align:"center",
            	fixed:true,
            	sortable:false,
            	resize:false,
            	search: false,
            	width:100,
            	formatter : function(value, grid, rows, state) {
            		var row = JSON.stringify(rows).replace(/"/g, '&quot;');  
                	return '<a class="a_style" style="cursor:pointer" onclick="setCron('+ row + ','+flag+','+systemId+')">生效</a> '+
                			'  <a class="a_style cancelBtn" style="cursor:pointer" onclick="cancel('+ row +')">取消</a> ';
            	}
       	 	}],  
       	 	jsonReader: {
       	 		repeatitems: false,
       	 		root: "rows", 
       	 	}, 
       	 	loadtext:"数据加载中......",  
		}).trigger("reloadGrid");   
	} 
} 
function setCron(row,type,systemId){ 
	var url=""
	var data="";
	if(row.stringId=="cronvaluetest"){
		//insert
		var jobCron=$("#cronvaluetest").val();
		//var envName=$("#deptName").val();
		var envName=$("select[name='cronvaluetest'] option:selected").text();
		var env=$("select[name='cronvaluetest'] option:selected").val();
		url="/devManage/deploy/insertCorn";
		data={
	    		"environmentType":env,
	    		"environmentTypeName":envName,
	    		"jobCron":jobCron,
	    		"systemId":systemId
	    		
	    };
		//$("select[name='cronvaluetest']").val();
//		alert(jobCron);
//		alert(envName);
//		alert(env);
//		return false;
	}else{
		var jobCron=$("#"+row.stringId).val();	 
		url="/devManage/deploy/setCornOne";
		data={
				"type":type,
				"jobCron":jobCron,
				"systemJenkinsId":row.stringId
			};
	}
//	alert(row.stringId);
//	//alert(row.id);
//	return false;
	
	$.ajax({
		url:url,
	    method:"post",
		data:data,
	   success:function(data){ 
		   $("#loading").css('display','none');
		   if(data.status == 2){
			   layer.alert(data.message, {
                    icon: 2,
                    title: "提示信息"
                });
		   }else{
			   layer.alert('成功', {
                    icon: 1,
                    title: "提示信息"
                });
			   scheduledReload(type,systemId);
			
		   }
	   } 
	});	 
}





//function insertCron(systemId,env,jobCron){
//	alert(type);
//	return false;
//			$.ajax({
//				url:"/devManage/structure/insertCornOne",
//			    method:"post",
//				data:{
//					"env":env,
//					"systemId":systemId,
//					"jobCron":jobCron
//				},
//			   success:function(data){
//				 //  layer.close(index);
//				   $("#loading").css('display','none');
//				   if(data.status == 2){
//					   layer.alert(data.errorMessage, {
//	                        icon: 2,
//	                        title: "提示信息"
//	                    });
//				   }else{
//					   layer.alert('成功', {
//	                        icon: 1,
//	                        title: "提示信息"
//	                    });
//					  // reloadVersion({id:systemId});
//				   }
//			   }
//			
//			});
//		
//}


function scheduledReload(type,systemId){ 
	
	 //重新载入 
	$("#loading").css('display','block');
	if(type==2){
		$("#list4").jqGrid('setGridParam',{ 
			url:"/devManage/deploy/getCorn", 
	        postData:{
	          	"systemId":systemId,
	          	"type":type,
	          }, 
	        loadComplete :function(xhr){
	        
	        	$("#loading").css('display','none'); 
	        	
	        },  
	    }).trigger("reloadGrid"); 
	}else{
	$("#list3").jqGrid('setGridParam',{ 
		url:"/devManage/deploy/getCorn", 
	    postData:{
		     "systemId":systemId,
		     "type":type,
		},
        loadComplete :function(xhr){
        	
        	$("#loading").css('display','none'); 
        	
        },  
    }).trigger("reloadGrid"); 
	}
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
function insertVersion(  ){  
	var ids = $("#list3").jqGrid('getDataIDs'); 
    var rowid = Math.max.apply(Math,ids); 
    var newrowid = "cronvalue";  
    var dataRow = {
    	stringId: newrowid+"test",    
    	jobCron: '',
        '操作':''
    };
	$("#list3").jqGrid("addRowData", newrowid, dataRow,"last"); 
	$('.selectpicker').selectpicker('refresh'); 
	
	$("#"+newrowid).find(".cancelBtn").css("display","inline-block");  
	$("#"+newrowid).find(".def_tableDiv").css("display","block");
} 

function cancel(row){  
	$("#list3").jqGrid("delRowData",row.stringId);
}





