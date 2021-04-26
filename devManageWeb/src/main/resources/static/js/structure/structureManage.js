/**
 * Created by 朱颜辞镜花辞树 on 2018/9/18.
 */
const privateDev=1, publicDev=2, privateXice=3, publicXice=4, privateUat=5, publicUat=6, privateBance=7, publicBance=8, prdIn=9, prdOut=10;
var structureEnv,structureRow;
//分页所需参数
var searchArr={};
var timer=null;
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
var count=0;
//自动构建构建方式
searchArr.postData={
	"refreshIds":'',
	"checkIds":'',
	"checkModuleIds":'',
	"pageNum":searchArr.pageNum,
	"pageSize":searchArr.select,
	"systemInfo":{},
	"scmBuildStatus":$("#scmBuildStatus").val()
};
var treeClick={
	"refreshIds":[],
	"checkIds":[],
	"checkModuleIds":[],
};
//查询参数
searchArr.postData.systemInfo.systemName = $("#seacrhSystemName").val();
searchArr.postData.systemInfo.systemCode =  $("#seacrhSystemNum").val();
searchArr.postData.systemInfo.projectIds = $("#seacrhProject").val();
$("#loading").css('display','block');
var allData=[];
var checkBoxDataArr=[];
var sonarData;

//树形配置
var setting = {
	data: {
		simpleData: {
			enable: true
		},
		treeNode: {
			name: 'name'
		}
	},
	callback: {
		onClick: ztreeOnClick,
		onCollapse:true,
	}
};

//历史树形配置
var setting_history = {
		data: {
			simpleData: {
				enable: true
			},
			treeNode: {
				name: 'name'
			}
		},
		callback: {
			onClick: ztreeOnClick_history,
			onCollapse:true,
		}
};

$(function(){
	//构建部署下拉环境
	getEnvironmentType();
	//初始化页面
	pageInit();
	//时间控件初始化
	dateComponent();
	//项目组下拉框
	addseacrhProjectOption();
	
	var t1 = window.setInterval(autoSearchInfo, 1000 * 60);

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
	$('input[type="text"]').parent().css("position","relative");
	$('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
	$('input[type="text"]').bind("input propertychange",function(){
		if( $(this).val()!="" ){
			$(this).parent().children(".btn_clear").css("display","block");
		}else {
			$(this).parent().children(".btn_clear").css("display","none");
		}
	})
	$("#sonarTime").bind("input propertychange",function(){

	})

	getEnvData();
	
	$('#logModal').on('hide.bs.modal', function () {
		end="true";
		clearInterval(timer);//结束循环
	})
});

/**
 * @description 添加下拉框项目选项
 */
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
		url:'/devManage/structure/getAllSystemInfo',
		datatype: "json",
		height: 'auto',
		mtype : "POST",
		cellEdit:true,
		multiselect: true,
		postData:{
			"pageNum":searchArr.pageNum,
			"pageSize":searchArr.row[0],
		},
		width: $(".content-table").width()*0.999,
		//  '开发中任务数'
		colNames:['','id','key_id','系统编号', '系统名称','所属项目','微服务类型','上次构建时间','操作'],
		colModel:[{
			name:'checkBox',
			index:'checkBox',
			width:20,
			formatter : function(value, grid, rows, state) {
				var row = JSON.stringify(rows).replace(/"/g, '&quot;');
				if( rows.createType==2 ){
					return '<input type="checkbox"  disabled="true"  />';
				}else if( rows.createType==1 ){
					if( rows.check=="true" ){
						return '<input type="checkbox" id="check'+rows.key_id+'" checked onclick="isChecked( '+row+',this )" />';
					}else{
						return '<input type="checkbox" id="check'+rows.key_id+'" onclick="isChecked( '+row+',this )" />';
					}
				}
			}
		},
			{name:'id',index:'id',hidden:true},
			{name:'key_id',index:'key_id',hidden:true,key:true},
			{
				name:'systemCode',index:'systemCode',classes:'treeClickClass',
				formatter : function(value, grid, rows, state) {
					var span='';
					if( rows.level == "0" ){
						if( rows.architectureType == 1 ){
							return "<span type='1' value="+rows.id+">"+rows.systemCode+"</span>";
						}else if( rows.architectureType == 2 ){
							return "<span type='2' value="+rows.id+">"+rows.systemCode+"</span>";
						}
					}
					return "<span type='' >"+rows.systemCode+"</span>";
				}
			},
			{name:'systemName',index:'systemName', formatter : function(value, grid, rows, state) {
					var str=rows.systemName;
					if( rows.architectureType==1 ){
						str+=" <sup class='micSup'>多模块架构</sup>"
					}
					return str;
				}},
			{name:'projectName',index:'projectName',width:120},
//            {name:'taskCount',index:'taskCount',width:80,align:"center",formatter : function(value, grid, rows, state) {
//
//            	if( rows.level==0 ){
//            		return rows.taskCount;
//            	}else if( rows.level==1 ){
//            		return '';
//            	}
//            	return '';
//            }},
			{name:'architectureType',index:'architectureType',hidden : true},
			{name:'lastBuildTime',index:'lastBuildTime',classes: 'lastBuildTime', formatter : function(value, grid, rows, state){
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
								var jobRunNumber="";
								var envNameflag=arr[i];
								envNameflag=envNameflag.replace("正在构建","");

								var manualJobName="";
//            				str+=arr[i]+"<a onclick=getLogNew("+rows.systemId+","+rows.createType+",\""+envNameflag+"\",\""+manualJobName+"\")>实时日志</a><br>";

								str+="<a href='#' onclick=getLogNew("+rows.systemId+","+rows.createType+",\""+jobRunNumber+"\",\""+envNameflag+"\",\""+manualJobName+"\")>"+arr[i]+"</a><br>";
							}
							return str;
						}else if(rows.createType==2){

							var str='';
							var arr=rows.nowJobName.split(",");
							for( var i=0;i<arr.length;i++ ){



//            				str+=arr[i]+"<a onclick=getLogNew("+rows+","","+arr[i]+")>查看日志</a><br>";
								var envNameflag="";
								var manualJobName=arr[i];
								manualJobName=manualJobName.replace("正在构建","");
								var jobRunNumber="";
								if(manualJobName.indexOf("编号:")!=-1){

									jobRunNumber=manualJobName.split("编号:")[1];
									jobRunNumber=jobRunNumber.replace(")","");


								}
//            				str+=arr[i]+"<a onclick=getLogNew("+rows.systemId+","+rows.createType+",\""+envNameflag+"\",\""+manualJobName+"\")>实时日志</a><br>";
								manualJobName=manualJobName.split("(编号:")[0];
								if (manualJobName.indexOf("/") != -1) {//如果有路径，去除
									var manualJobNameArr = manualJobName.split("/");
									manualJobName = manualJobNameArr[manualJobNameArr.length - 1];
								}
								str+="<a href='#'  onclick=getLogNew("+rows.systemId+","+rows.createType+","+jobRunNumber+",\""+envNameflag+"\",\""+manualJobName+"\")>"+arr[i]+"</a><br>";
							}
							return str;
						}else{
							return "";
						}
					}else{
						if( rows.buildStatus=='4' ){
							// 没有上次构建时间
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
				align:"left",
				fixed:true,
				sortable:false,
				resize:false,
				classes: 'optClass',
				search: false,
				width:300,
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
							//var sonarEnvids="";
							if(rows.choiceEnvids==""){

							}else{
								var envs=rows.choiceEnvids.split(",");
								for(var i=0;i<envs.length;i++){
									var id=envs[i].split(":")[0];
									var value=envs[i].split(":")[1];
									envids+='<li><a name='+id+' onclick="startBuilding('+ row + ',this)">'+value+'</a></li>';
									// sonarEnvids+='<li> <a name='+id+' onclick="startSonarBuilding('+ row + ',this)">'+value+'</a></li>';
								}
							}
							if (buildFlag == true ){
								str+='<a class="dropdown-toggle a_style" data-toggle="dropdown" role="button"><span class="fa fa-cog"></span> 构建/扫描 <span class="fa fa-angle-down"></span></a>'+
									'<ul class="dropdown-menu ul_envids" >' +envids+
									'</ul></li>';
							}


							// //增加扫描按钮
							// if(true == true ){
							// 	str+='<a class="dropdown-toggle a_style" data-toggle="dropdown" role="button"><span class="fa fa-cog"></span> 扫描 <span class="fa fa-angle-down"></span></a>'+
							// 		'<ul class="dropdown-menu" style="left:950px " >' +sonarEnvids+
							// 		'</ul></li>';
							// }


						}else if( rows.createType==2 ){
							if (buildFlag == true ){
								str+= '<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" onclick="getAllJobName( this,'+row+' )" role="button"><span class="fa fa-cog"></span> 构建 <span class="fa fa-angle-down"></span></a>'+
									'<ul class="_select_box_menu" id="dropdown_menu"></ul></li> ';
							}
						}
						str+='  <li role="presentation" class="dropdown">' +
							'<a class="dropdown-toggle a_style" data-toggle="dropdown" role="button">'+
							'</span> 查看历史 <span class="fa fa-angle-down"></span></a>'+
							'<ul class="dropdown-menu" >' ;
						if (buildHistoryFlag == true ){
							str+= '<li><a class="a_style" onclick="buildHistory('+ row + ')">构建/扫描历史</a></li>';
						}
						if (sonarHistoryFlag == true ){
							str+= '<li><a class="a_style" onclick="sonarHistory('+ row + ')">扫描历史</a></li>';
						}
						str+='</ul></li>';
						if (scheduledFlag == true ){
							if(rows.createType==1){
								str+=' | <a class="a_style" onclick="scheduledTask('+ row + ')">定时任务</a> ';
							}

						}
						if (breakBuilding == true ){
							str+=' | <a class="a_style" onclick="getBreakName('+ row + ')">强制结束</a> ';
						}
						return str;
						// onclick="scheduledTask('+ row + ')"

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
			level_field: "level",
			parent_id_field: "parent",
			leaf_field: "isLeaf",
			expanded_field: "expanded"
		},
		loadui: "disable",
		pager: '#pager',
		loadComplete :function(xhr){

			getTreeClick();

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

/**
 * @description 添加翻页栏
 */
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

/**
 * @description 页码切换改变请求参数
 */
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

/**
 * @description 搜索
 * @param refreshIds	
 * @param checkIds 
 * @param checkModuleIds  
 * @param systemInfo  
 */
function searchInfo(){
	//清空checkBox数组
	checkBoxDataArr=[];
	allData=[];
	//清空tree打开数组
	searchArr.postData.refreshIds='';
	searchArr.postData.checkIds='';
	searchArr.postData.checkModuleIds='';
	treeClick={
		"refreshIds":[],
		"checkIds":[],
		"checkModuleIds":[],
	};
	//重新载入
	$("#loading").css('display','block');
	$("#list2").jqGrid('setGridParam',{
		url:"/devManage/structure/getAllSystemInfo",
		postData:searchArr.postData,
		loadComplete :function(xhr){
			getTreeClick();
			allData=xhr.rows;
			searchArr.total=xhr.total;
			$("#loading").css('display','none');
			addPage(searchArr);
		},
	}).trigger("reloadGrid");
}

/**
 * @description 定时刷新列表请求
 */
function autoSearchInfo(){

	searchArr.postData.refreshIds='';
	searchArr.postData.checkIds='';
	searchArr.postData.checkModuleIds='';
	
	treeClick.checkIds=[];
	treeClick.checkModuleIds=[];
	
	for(var i=0;i<checkBoxDataArr.length;i++){
		if( checkBoxDataArr[i].level=="0" ){
			treeClick.checkIds.push( checkBoxDataArr[i].id );
		}else if( checkBoxDataArr[i].level=="1" ){
			treeClick.checkModuleIds.push( checkBoxDataArr[i].id );
		}
	}
	searchArr.postData.checkIds=treeClick.checkIds.join(",");
	searchArr.postData.checkModuleIds=treeClick.checkModuleIds.join(",");
	searchArr.postData.refreshIds=treeClick.refreshIds.join(",");
	/*$("#list2").jqGrid('setGridParam',{
		url:"/devManage/structure/getAllSystemInfo",
		postData:searchArr.postData,
		loadComplete :function(xhr){
			getTreeClick();
			if( xhr===undefined ){
			}else{
				allData=xhr.rows;
				searchArr.total=xhr.total;
				addPage(searchArr);
			}
		},
	}).trigger("reloadGrid");*/
	$.ajax({
        url: '/devManage/structure/getAllSystemInfo',
        type: "POST",
	       dataType:"json",
	       data:searchArr.postData,
	       success: function (result) {
	    	    getTreeClick();
				if( result===undefined ){
				}else{
					allData=result.rows;
					searchArr.total=result.total;
					addPage(searchArr);
				}
		        var vmDataArr = result.rows;
			    for (var i=0;i<vmDataArr.length;i++){
			        let rows = vmDataArr[i];
			        let row_data = JSON.stringify(vmDataArr[i]).replace(/"/g, '&quot;');
			        var str = '';
			        if (rows.level == 1) {
						if (rows.createType == 2) {
							str = '';
						}
					}
			        
			        if( rows.level == 0 ){
						if( rows.createType == 1 ){
							if(rows.choiceEnvids == ""){
							}else{
								if (buildFlag == true ){
									$("#"+rows.key_id).find('.ul_envids').empty();
									var envs = rows.choiceEnvids.split(",");
									var envids = '';
									for(var q = 0;q < envs.length;q++){
										var id = envs[q].split(":")[0];
										var value = envs[q].split(":")[1];
										envids += '<li><a name='+id+' onclick="startBuilding('+ row_data + ',this)">'+value+'</a></li>';
									}
									$("#"+rows.key_id).find('.ul_envids').html(envids);
								}
							}
						}
			        }
			        
					if (rows.nowStatus == 'true') {
						if (rows.createType == 1) {
							var arr = rows.nowEnvironmentType.split(",");
							for (var j = 0; j < arr.length; j++) {
								var jobRunNumber="";
								var envNameflag = arr[j];
								envNameflag=envNameflag.replace("正在构建","");
								var manualJobName="";
								str += "<a  href='#' title='" + arr[j] + "' onclick=getLogNew(" + rows.systemId + "," + rows.createType
								+",\"" + jobRunNumber + "\",\"" 
										+ envNameflag + "\",\"" + manualJobName + "\")>" + arr[j] + "</a><br>";
							}
						} else if (rows.createType == 2) {
							var arr = rows.nowJobName.split(",");
							for (var j = 0; j < arr.length; j++) {
								var envNameflag = "";
								var manualJobName = arr[j];
								manualJobName = manualJobName.replace("正在构建", "");
								var jobRunNumber="";
								if(manualJobName.indexOf("编号:")!=-1){
									jobRunNumber=manualJobName.split("编号:")[1];
									jobRunNumber=jobRunNumber.replace(")","");
								}
								manualJobName=manualJobName.split("(编号:")[0];
								if (manualJobName.indexOf("/") != -1) {//如果有路径，去除
									var manualJobNameArr = manualJobName.split("/");
									manualJobName = manualJobNameArr[manualJobNameArr.length - 1];
								}
								str += "<a  href='#' title='" + arr[j] + "'  onclick=getLogNew("+rows.systemId + "," 
									+ rows.createType +","+jobRunNumber+",\""+envNameflag+"\",\""+manualJobName+"\")>" + arr[j] + "</a><br>";
							}
						} else {
							str = '';
						}
						/*if (rows.createType == 1) {
							var arr = rows.nowEnvironmentType.split(",");
							for (var j = 0; j < arr.length; j++) {
								var envNameflag = arr[j];
								if (envNameflag.indexOf("测试") !=-1) {//显示部署日志不是实时的
									envNameflag = envNameflag.replace("正在自动化测试", "");
									str += "<a  href='#' title='" + arr[j] + "' onclick=getTestLog(" + rows.deployType + "," + rows.systemId + "," 
										+ rows.createType + ",\"" + envNameflag + "\",\"" + manualJobName + "\")>" + arr[j] + "</a><br>";
								} else {
									envNameflag = envNameflag.replace("正在构建", "");
									var manualJobName = "";
									str += "<a  href='#' title='" + arr[j] + "' onclick=getLogNew(" + rows.deployType + "," + rows.systemId + "," + rows.createType + ",\"" 
										+ envNameflag + "\",\"" + manualJobName + "\")>" + arr[j] + "</a><br>";
								}
							}
						} else if (rows.createType == 2) {
							var arr = rows.nowJobName.split(",");
							for (var j = 0; j < arr.length; j++) {
								var envNameflag = "";
								var deployType = "";
								var manualJobName = arr[j];
								manualJobName = manualJobName.replace("正在构建", "");
								str += "<a  href='#' title='" + arr[j] + "'  onclick=getLogNew(\"" + deployType + "\"," + rows.systemId + "," 
									+ rows.createType + ",\"" + envNameflag + "\",\"" + manualJobName + "\")>" + arr[j] + "</a><br>";
							}
						} else {
							str = '';
						}*/
					} else {
						if (rows.buildStatus == '4') {// 没有上次部署时间
							str = '';
						} else {
							var type = '';
							for (var k in environmentTypeData) {
								if (k == rows.environmentType) {
									type = environmentTypeData[k];
								}
							}
							if (rows.createType == 1) {
								if (rows.buildStatus == 2) {
									str =  rows.lastBuildTime + " " + type + "  <span style='color:green;'>成功</span>";
								} else if (rows.buildStatus == 3) {
									str =   rows.lastBuildTime + " " + type + "  <span style='color:red;'>失败</span>";
								}
								else if (rows.buildStatus == 5) {
									str =   rows.lastBuildTime + " " + type + "  <span style='color:red;'>自动化测试失败</span>";
								}
							} else {
								if (rows.buildStatus == 2) {
									str =   rows.lastBuildTime + " " + rows.lastJobName + "  <span style='color:green;'>成功</span>";
								} else if (rows.buildStatus == 3) {
									str =   rows.lastBuildTime + " " + rows.lastJobName + "  <span style='color:red;'>失败</span>";
								}
							}
						}
					}
			    	$("#"+rows.key_id).find('.lastBuildTime').html(str);
			    }
	       }
    });
}

/**
 * @description 切换条码 刷新列表
 */
function changeSelect(This){
	searchArr.select=$(This).val();
	searchArr.pageNum=1;
	searchArr.postData={
		"pageNum":searchArr.pageNum,
		"pageSize":searchArr.select,
	}
	searchInfo();
}

/**
 * @description 搜索 
 */
function searchBoxInfo(){
	searchArr.pageNum=1;
	searchArr.postData.systemInfo={
		"systemName" : $("#seacrhSystemName").val(),
		"systemCode" : $("#seacrhSystemNum").val(),
		"projectIds" : $("#seacrhProject").val(),
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

/**
 * @description 点击构建/扫描，选择环境后触发
 */
function startBuilding(row,This){
	var env=$(This).attr("name")

	getVersion(row.systemId,env);

	$("#artDiv").hide();
	$("#versionDiv").hide();
	$('input:radio[name=artNexus]').attr('checked',false);
	//$('input[type=radio][name="sonarScanner"]:checked').prop("checked", false);
	var obj3=document.getElementById("structureModal");
	obj3.style.height= "300px";
	$("#note").hide();
	$("#structureModal").modal('show');
	structureRow=row;
	structureEnv=env;

	detailSonarFlag(row.sonarScanStatus);
}

/**
 * @description 开始sona构建 
 */
function startSonarBuilding(row,This){
	var env=$(This).attr("name")
	commitSonar(row,env);
}


function detailSonarFlag(sonarScanStatus) {
	if(sonarScanStatus=="1"){
		$("input[name='sonarScanner'][value='1']").attr("checked","checked");
	}else if(sonarScanStatus=="2"){
		$("input[name='sonarScanner'][value='2']").attr("checked","checked");
	}
}

/**
 * @description 构建配置
 */
function getVersion(systemId,env){
	$("#versionSelect").empty();
	$('#versionSelect').selectpicker('refresh');
	var tdStr;
	tdStr="";
	$.ajax({
		url:"/devManage/systemVersion/getSystemVersionByConNew",
		method:"post",
		data:{
			systemId:systemId,
			moduleId:"",
			status:"1",
			env:env
		},
		success:function(data){
			var rows=data.rows;
			for(var i=0;i<rows.length;i++){
				if(i==(rows.length-1)){
					$("#versionSelect").prepend("<option selected='true' value=" + rows[i].id + ">" + rows[i].version + "</option>");
				}else {

					$("#versionSelect").prepend("<option value=" + rows[i].id + ">" + rows[i].version + "</option>");
				}

			}
			$('#versionSelect').selectpicker('refresh');

		},
		error:function(){
		}
	});


}

/**
 * @description 构建开始
 */
function commitBefor(){
	var sonarFlag=$("input[name='sonarScanner']:checked").val();
	if(sonarFlag=="" || sonarFlag==null){
		layer.alert('请选择是否扫描', {
			icon: 2,
			title: "提示信息"
		});

		return false;
	}
	var artNexus=$("input[name='artNexus']:checked").val();

	var artType="";
	var version="";
	if(artNexus=="" || artNexus==undefined){
		layer.alert('请选择是否制品入库', {
			icon: 2,
			title: "提示信息"
		});
		return false;
	}
	if(artNexus=="1"){
		artType=$("#artType").val();
		//version=$("#versionSelect").val();

		version=$('#versionSelect option:selected').text();
		if(version=="" || version==undefined ){
			layer.alert('请去系统配置增加系统版本!', {
				icon: 2,
				title: "提示信息"
			});
			return false;
		}

	}
	commitInfo(structureRow,structureEnv,sonarFlag,artType,version);
}

/**
 * @description sona构建 请求
 */
function commitSonar(row,env){

	var modules=[];
	var hash=[];
	if( row.architectureType==1 ){
		for(var key in checkBoxDataArr){
			if(checkBoxDataArr[key].parent==row.key_id){
				hash.push( checkBoxDataArr[key].id );
			}
		}
		if( hash.length==0 ){
			layer.alert('未选择子服务，请选择子服务后尝试构建！', {
				icon: 2,
				title: "提示信息"
			});
			return ;
		}
		for (var i = 0; i < hash.length; i++) {
			if( modules.indexOf( hash[i] )==-1){
				modules.push( hash[i] );
			}
		}
	}
	modules=modules.join(",");

	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/structure/creatJenkinsSonarJob",
		method:"post",
		data:{
			sysId:row.id,
			systemName:row.systemName,
			serverType:row.architectureType,
			modules:modules,
			env:env

		},
		success:function(data){

			if (data.status == 2) {

				layer.alert(data.message, {
					icon: 2,
					title: "提示信息"
				});
				$("#promptBox").modal("hide");
				$("#loading").css('display','none');
				return;
			}
			autoSearchInfo();
			$("#promptBox").modal("hide");
			$("#loading").css('display','none');
			//新增日志处理
			detaillog(data);

		},
		error:function(){
			layer.alert("出错！请稍后", {
				icon: 2,
				title: "提示信息"
			});
		}
	});
}

/**
 * @description 构建信息
 */
function commitInfo(row,env,sonarflag,artType,version){ 
	var modules=[];
	var hash=[];
	if( row.architectureType==1 ){
		for(var key in checkBoxDataArr){
			if(checkBoxDataArr[key].parent==row.key_id){
				hash.push( checkBoxDataArr[key].id );
			}
		}
		if( hash.length==0 ){
			layer.alert('未选择子服务，请选择子服务后尝试构建！', {
				icon: 2,
				title: "提示信息"
			});
			return ;
		}
		for (var i = 0; i < hash.length; i++) {
			if( modules.indexOf( hash[i] )==-1){
				modules.push( hash[i] );
			}
		}
	}
	modules=modules.join(",");

	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/structure/creatJenkinsJob",
		method:"post",
		data:{
			sysId:row.id,
			systemName:row.systemName,
			serverType:row.architectureType,
			modules:modules,
			env:env,
			sonarflag:sonarflag,
			version:version,
			artType:artType
		},
		success:function(data){
			$("#structureModal").modal('hide');
			structureRow="";
			structureEnv="";
			if (data.status == 2) {

				layer.alert(data.message, {
					icon: 2,
					title: "提示信息"
				});
				$("#promptBox").modal("hide");
				$("#loading").css('display','none');
				return;
			}
			autoSearchInfo();
			$("#promptBox").modal("hide");
			$("#loading").css('display','none');
			//新增日志处理
			detaillog(data);

		},
		error:function(){

			$("#structureModal").modal('hide');
			structureRow="";
			structureEnv="";
		}
	});
}

/**
 * @description 构建日志
 */
function detaillog(data){
	var jobRunNumber="";
	if(data.jobRunNumber==null || data.jobRunNumber==""){

	}else{
		jobRunNumber=data.jobRunNumber;
	}
	var row={};
	row.toolId=data.toolId;
	row.jobName=data.jobName;
	row.jenkinsId=data.jenkinsId;
	row.jobRunNumber=jobRunNumber;
	closelog();
	getLog(row);
}

//====================================部署历史statr================================//
function ztreeOnClick_history(event, treeId, treeNode, clickFlag) {
	if(!treeNode.isParent){
		$('._select_box_menu2').hide();
		get_history(1,treeNode.realId);
	}
}

//构建历史
function buildHistory(rows){
	$("#envType_body").hide();
	$("#envType_ztree_body").hide();
	$("#History_id").val(rows.id);
	$("#History_createType").val(rows.createType);
	get_history(2);
	//手动构建
	if(rows.createType==2 && buildFlag == true){
		$('#loading').show();
		$.ajax({
			url:"/devManage/structure/getJobName",
			method:"post",
			data:{
				systemId:rows.id,
			},
			success:function(data){
				var str = '';
				if(data.status == 1){
					if(data.ztreeObjs.length){
						var arr = data.ztreeObjs;
						for (var i = 0; i < arr.length; i++) {
//							arr[i].open = true;
							for (var j = i+1; j < arr.length; j++) {
								if (arr[i].id == arr[j].id && arr[i].pId == arr[j].pId) {
									arr.splice(j,1);
									j--;
								}
							}
						}
						$.fn.zTree.init($("#buildHistory_tree"), setting_history, arr);
						fuzzySearch('buildHistory_tree', '#buildHistory_treeSearchInput', null, true); //初始化模糊搜索方法
					}
				}
				$("#envType_ztree_body").show();
				$('#loading').hide();
			},
			error:function(){
				$('#loading').hide();
			}
		});
		
	}else{//自动构建
		$("#envType").empty();
		$("#envType").append('<option value="">请选择筛选条件</option>');
		var choiceEnvids_arr = [],choiceAtrEnvids = [];
		if(rows.choiceEnvids){ // 源码环境
			choiceEnvids_arr = rows.choiceEnvids.split(",");
		}
		if(rows.choiceAtrEnvids){ // 制品环境
			choiceAtrEnvids = rows.choiceAtrEnvids.split(",");
		}
		var Env_arr = [...new Set([...choiceEnvids_arr,...choiceAtrEnvids])];
		for(var i=0;i<Env_arr.length;i++){
			var id=Env_arr[i].split(":")[0];
			var value=Env_arr[i].split(":")[1];
			$("#envType").append('<option value='+id+'>'+value+'</option>');
		}
		$("#envType").selectpicker('refresh');
		$("#envType_body").show();
	}
}

/**
 * @description 获取历史表格
 */
function get_history(status,ID){
	var envType = $("#envType").val(), jenkinsId = '', flag = 2;
	if(status == 1){ //手动
		envType = '';
		jenkinsId = ID;
		flag = 2;
	}else if(status == 2){ //全部
		envType = '';
		flag = 1;
		$("#envType").val('');
		$("#envType").selectpicker('refresh');
	}
	if(!envType && status == 3) return;
	$("#loading").css('display','block');
	$("#buildHistoryTable").bootstrapTable('destroy');
	$("#buildHistoryTable").bootstrapTable({
		url:"/devManage/structure/getAllBuildMessage",
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
				systemId : $("#History_id").val(),
				createType:$("#History_createType").val(),
				jenkinsId:jenkinsId,
				envType:envType,
				flag:flag  
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
					spanFont='构建/扫描失败';
				}else if(row.buildStatus==2){
					classA="fa-check iconSuccess2";
					spanFont='构建/扫描成功';
				}else {
					return '';
				}

				if(row.createType==1){
					for(var k in environmentTypeData){
						if( k== row.envType ){
							type=environmentTypeData[k];
						}
					}
				}else if(row.createType==2){
					type=row.jobName;
				}
				var all_sec = ((new Date(row.endDate).getTime() - new Date(row.startDate).getTime())/1000);
				var sec_date = all_sec%60 + '秒';
				var min_date = (all_sec - all_sec%60)/60 + '分';
				var all_date = min_date + sec_date;
				if(all_sec < 60){
					all_date = sec_date;
				}
				 if(row.createType==1){
					return '<div class="allInfo"><div class="rowdiv buildHistoryInfo">'+
						'<div class="form-group def_col_8" style="height:auto;"><div class="successInfo2" style="height:auto;text-overflow: unset;overflow: unset;white-space: unset;word-break: break-all;line-height: unset;"><span class="fa '+classA+'"></span> '+ spanFont+" "+type+'</div></div>'+
						'<div class="form-group def_col_13" title="'+row.moduleNames+'"  style="height:auto;word-break: break-all;">'+row.moduleNames+'</div>'+
						'<div class="form-group def_col_3">'+row.userName+'</div>'+
						'<div class="form-group def_col_5" title="'+row.startDate+' | '+row.endDate+'">'+row.startDate+'  '+row.endDate+'</div>'+
						'<div class="form-group def_col_3">'+all_date+'</div>'+
						'<div class="form-group def_col_4"><button type="button" onclick="detailInfo('+rows+','+
							index+')" class="btn infoBtm">查看信息</button></div></div></div>';
				 }else  if(row.createType==2){
					var moduleNames = '';
					if(row.buildParameter){
						row.buildParameter.split(';').map(function(v){
							moduleNames += v + '<br>';
						})
					}
				 	return '<div class="allInfo"><div class="rowdiv buildHistoryInfo">'+
				 		'<div class="form-group def_col_8" style="height:auto;"><div class="successInfo2" style="height:auto;text-overflow: unset;overflow: unset;white-space: unset;word-break: break-all;line-height: unset;"><span class="fa '+classA+'"></span> '+ spanFont+" "+type+'</div></div>'+
				 		'<div class="form-group def_col_13" title="'+moduleNames+'"  style="height:auto;">'+moduleNames+'</div>'+
				 		'<div class="form-group def_col_3">'+row.userName+'</div>'+
				 		'<div class="form-group def_col_5" title="'+row.startDate+' | '+row.endDate+'">'+row.startDate+'  '+row.endDate+'</div>'+
				 		'<div class="form-group def_col_3">'+all_date+'</div>'+
				 		'<div class="form-group def_col_4"><button type="button" onclick="detailInfo('+rows+','+
				 			index+')" class="btn infoBtm">查看信息</button></div></div></div>';
				 }

			}
		}],
		onLoadSuccess:function(){
			$("#buildHistoryDIV").modal("show");
			$("#loading").css('display','none');
		},
		onLoadError : function() {
			$("#loading").css('display','none');
		}
	});
}

//====================================部署历史end================================//

/**
 * @description 构建扫描详情信息
 */
function detailInfo( row , index ){
//	$("#buildLogsInfo").hide();
//	if(row.envType==prdIn || row.envType==prdOut){
//		$("#buildLogsInfo").hide();
//	}else{
//		$("#buildLogsInfo").show();
//	}
	$("#buildLogsInfoSonar").show();
	//手动构建，不显示扫描结果
	if(row.createType==2){
		$("#buildLogsInfoSonar").hide();
	}
	//构建成功
	if( row.buildStatus==2 ){
		$("#buildingFail").css("display","none");
		$("#buildingSuccess").css("display","block");
	}else if( row.buildStatus==3 ){//构建失败
		$("#buildingFail").css("display","block");
		$("#buildingSuccess").css("display","none");
	}
	$("#buildLogsInfoPre").html();
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/structure/getBuildMessageById",
		method:"post",
		data:{
			jobRunId: row.id,
			createType: row.createType
		},
		success:function(data){
			//组装stage信息块
			getStageViewHis_request(row.id);
			$("#SonarResult").empty(); 
			//基本信息
			$("#detailInfoSuccessStartTime").text( data.startDate );
			$("#detailInfoSuccessEndDate").text( data.endDate );
			//jenkins日志信息
			$("#buildLogsInfoPre").html(data.buildLogs);
			$("#scanningTime").text(data.endDate);
			
			//组装sonar扫描信息
			var str='';
			if( data.isSonar == "flase" || data.list.length == 0){
				str = "<div class=''>未扫描代码，无数据。</div>"
			}else{
				for(var i=0;i<data.list.length;i++){
					str+='<div class="oneResult">'+
						'<input type="hidden" class="di_projectKey" value='+data.list[i].projectKey+' />'+
						'<input type="hidden" class="di_toolId" value='+data.list[i].toolId+' />'+
						'<input type="hidden" class="di_projectDateTime" value='+data.list[i].projectDateTime+' />'+
						'<div class="rowdiv">';
					if( data.list[i].moduleame!=undefined ){
						str+='<div class="form-group col-md-6"><label class="col-sm-4 control-label fontWeihgt">名称：</label><label class="col-sm-8 control-label font_left">'+data.list[i].moduleame+'</label></div>';
					}
					str+='<div class="form-group col-md-6"><label class="col-sm-4 control-label fontWeihgt">结束构建时间：</label><label class="col-sm-8 control-label font_left">'+data.list[i].xValue+'</label></div></div>'+
						'<div class="rowdiv" style="display: flex;flex-direction: row;">';

					if( index == 0 ){
						str+='<div class="logTypeDiv" value="BUG" onclick="showDetails(this,'+data.list[i].bug+')"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].bug+'</span>&nbsp;&nbsp;<span class="resultIcon1"></span></div><div><span class="fa fa-bug fontIconSize"></span><span>bugs</span></div></div>'+
							'<div class="logTypeDiv" value="VULNERABILITY" onclick="showDetails(this,'+data.list[i].Vulnerabilities+')"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].Vulnerabilities+'</span>&nbsp;&nbsp;<span class="resultIcon2"></span></div><div><span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities</span></div></div>'+
							'<div class="logTypeDiv" value="CODE_SMELL" onclick="showDetails(this,'+data.list[i].CodeSmells+')"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].CodeSmells+'</span>&nbsp;&nbsp;<span class="resultIcon3"></span></div><div><span class="fa fa-support fontIconSize"></span><span>Code Smells</span></div></div>'+
							'<div class="logTypeDiv" value="tests" onclick="showDetails(this,'+data.list[i].tests+')"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].tests+'</span>&nbsp;&nbsp;<span class="resultIcon5"></span></div><div><span class="fa fa-file-text-o fontIconSize"></span><span>Tests</span></div></div>';
					}else{
						if(row.manualFrist=='true'){
							str+='<div class="logTypeDiv" value="BUG" onclick="showDetails(this,'+data.list[i].bug+')"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].bug+'</span>&nbsp;&nbsp;<span class="resultIcon1"></span></div><div><span class="fa fa-bug fontIconSize"></span><span>bugs</span></div></div>'+
								'<div class="logTypeDiv" value="VULNERABILITY" onclick="showDetails(this,'+data.list[i].Vulnerabilities+')"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].Vulnerabilities+'</span>&nbsp;&nbsp;<span class="resultIcon2"></span></div><div><span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities</span></div></div>'+
								'<div class="logTypeDiv" value="CODE_SMELL" onclick="showDetails(this,'+data.list[i].CodeSmells+')"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].CodeSmells+'</span>&nbsp;&nbsp;<span class="resultIcon3"></span></div><div><span class="fa fa-support fontIconSize"></span><span>Code Smells</span></div></div>'+
								'<div class="logTypeDiv" value="tests" onclick="showDetails(this,'+data.list[i].tests+')"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].tests+'</span>&nbsp;&nbsp;<span class="resultIcon5"></span></div><div><span class="fa fa-file-text-o fontIconSize"></span><span>Tests</span></div></div>';
								
						}else{
							str+='<div class="logTypeDiv" value="BUG"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].bug+'</span>&nbsp;&nbsp;<span class="resultIcon1"></span></div><div><span class="fa fa-bug fontIconSize"></span><span>bugs</span></div></div>'+
								'<div class="logTypeDiv" value="VULNERABILITY"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].Vulnerabilities+'</span>&nbsp;&nbsp;<span class="resultIcon2"></span></div><div><span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities</span></div></div>'+
								'<div class="logTypeDiv" value="CODE_SMELL"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].CodeSmells+'</span>&nbsp;&nbsp;<span class="resultIcon3"></span></div><div><span class="fa fa-support fontIconSize"></span><span>Code Smells</span></div></div>'+
								'<div class="logTypeDiv" value="tests"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].tests+'</span>&nbsp;&nbsp;<span class="resultIcon5"></span></div><div><span class="fa fa-file-text-o fontIconSize"></span><span>Tests</span></div></div>';
								
						}
					}
					str+='<div class="logTypeDiv"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].Coverage+'</span>&nbsp;&nbsp;<span class="resultIcon4"></span></div><div><span class="fa fa-file-text fontIconSize"></span><span>Coverage</span></div></div>'+
						//	'<div class="logTypeDiv"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].Coverage+'</span>&nbsp;&nbsp;<span class="resultIcon4"></span></div><div><span class="fa fa-file-text fontIconSize"></span><span>Complexityx</span></div></div>'+
						'<div class="logTypeDiv" style="border:none;"><div class="rowdiv"><span class="logTypeDivNum">'+data.list[i].duplications+'</span>&nbsp;&nbsp;<span class="resultIcon4"></span></div><div><span class="fa fa-refresh fontIconSize"></span><span>Duplications</span></div></div></div>'+
						'<div class="questionList"></div></div>';
				}
			} 
			$("#SonarResult").append( str );
			
			$("#loading").css('display','none');
			$("#detailInfoDIV").modal("show");
		},
		error:function(){
		}
	});
}

//废弃
function creatAllJob(This){
	if( checkBoxDataArr.length==0 ){
		//alert("请选择服务！");
		layer.alert('请选择服务！', {
			icon: 2,
			title: "提示信息"
		});
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

			layer.alert('普通服务的数量不得超过10个！', {
				icon: 2,
				title: "提示信息"
			});
			return;
		}else{
			$.ajax({
				url:"/devManage/structure/creatJenkinsJobBatch",
				method:"post",
				data:{
					data:JSON.stringify( data ),
					env:$(This).attr("name"),
				},
				success:function(data){
					if (data.status == 2) {

						layer.alert(data.message, {
							icon: 2,
							title: "提示信息"
						});

						return;
					}
					setTimeout("searchBoxInfo()", 1000 )
					if(data.error!=""){
						layer.alert(data.error, {
							icon: 2,
							title: "提示信息"
						});
					}
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

//sona 扫描历史
function sonarHistory( row ){
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

//sona 扫描历史请求
function requestSonarHistory( id , type ,startDate ,endDate ,createType){
	if( createType==1 ){
		if( type==1  ){
			$.ajax({
				url:"/devManage/structure/getSonarMessageMincro",
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
				url:"/devManage/structure/getSonarMessage",
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
			url:"/devManage/structure/getSonarMessageManual",
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
	$('#scmBuildStatus').selectpicker('val', '');
	$("#seacrhProject").selectpicker('val', '');
}

//勾选微服务状态
function isChecked( row,That ){
	var newcheckBoxDataArr=[];
	if( $( That ).is(':checked')==true ){
		if( row.level==0 ){
			if( row.architectureType==1){
				//选中微服务下所有子服务
				//判断 是否有 子节点 已经 被选中 如果有 先清空，然后重新 循环找出所有子节点
				for( var i in checkBoxDataArr ){
					if( checkBoxDataArr[i].parent!=row.key_id ){
						newcheckBoxDataArr.push( checkBoxDataArr[i] );
					}
				}
				checkBoxDataArr=newcheckBoxDataArr;
				for( var i in allData ){
					if( allData[i].parent==row.key_id ){
						checkBoxDataArr.push( allData[i] );
						$("#check"+allData[i].key_id ).prop("checked",true);
					}
				}
				checkBoxDataArr.push( row );
			}else if( row.architectureType==2 ){
				checkBoxDataArr.push( row );
			}
		}else if( row.level==1 ){
			checkBoxDataArr.push( row );
		}
	}else{
		if( row.level==0 ){
			if( row.architectureType==1){
				for( var i in allData ){
					if( row.key_id==allData[i].parent ){
						$("#check"+allData[i].key_id ).prop("checked",false);
						var newcheck=[];
						for( var j=0;j<checkBoxDataArr.length;j++ ){
							if( checkBoxDataArr[j].key_id != allData[i].key_id ){
								newcheck.push( checkBoxDataArr[j] );
							}
						}
						checkBoxDataArr=[];
						checkBoxDataArr=newcheck;
					}
				}
			}
		}
		$("#check"+row.key_id ).prop("checked",false);
		for( var j=0;j<checkBoxDataArr.length;j++ ){
			if( checkBoxDataArr[j].key_id != row.key_id  ){
				newcheckBoxDataArr.push( checkBoxDataArr[j] );
			}
		}
		checkBoxDataArr=newcheckBoxDataArr;
	}

}

function stringToArr( data ){
	var len = data.length;//str为要处理的字符串
	result = data.substring(1,len-1);//result为你需要的字符串
	return result.split(',');
}

/**
 * @description 生成扫描信息图表
 */
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
		if( key=="Bugs" || key=="Code Smells" || key=="Coverage" || key=="Duplications" || key=="Vulnerabilities" || key=="tests" ){
			echarts.init( document.getElementById('sonarTable'+tableNum) ).dispose();
			echarts.init( document.getElementById('sonarTable'+tableNum) ).setOption( bulidEcharts( key, data ) );
			tableNum++;
		}
	}
}

/**
 * @description 生成扫描信息图表
 */
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
					if( key=="Bugs" || key=="Code Smells" || key=="Coverage" || key=="Duplications" || key=="Vulnerabilities" || key=="tests"  ){
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
					if( key=="Bugs" || key=="Code Smells" || key=="Coverage" || key=="Duplications" || key=="Vulnerabilities"|| key=="tests"  ){
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

/**
 * @description 生成扫描信息图表
 */
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

/**
 * @description 扫描信息图表   配置
 */
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

/**
 * @description sonar扫描  点击扫描结果图标显示详情
 */
function showDetails(This,flag){ 
	var value='';
	value = "."+$(This).attr( "value" );
	if( flag==undefined ){ 
		//alert("手动部署，没有配置sonar扫描!"); 
		layer.alert('构建时没有选择sonar扫描!', {
			icon: 2,
			title: "提示信息"
		});
		return false;
	}
	//判断此选择是否已经在下方出现，如果有则打开此选择，如果没有则请求后台生成DOM元素。 
	var element = $(This).parent().parent().children(".questionList").children(".sonarList"); 
	var hasFlag = false;
	for( var i=0;i<element.length;i++ ){ 
		if( element.eq( i ).attr( "idValue" ) == $(This).attr( "value" )  ){
			hasFlag = true;
			element.find(".listTitle .fa-angle-double-up").addClass("fa-angle-double-down");
			element.find(".listTitle .fa").removeClass("fa-angle-double-up");
			element.find(".questionContent").css("display","none");
			
			element.eq( i ).find(".fa-angle-double-down").addClass("fa-angle-double-up");
			element.eq( i ).find(".fa-angle-double-up").removeClass("fa-angle-double-down");
			element.eq( i ).find(".questionContent").css("display","block");
		}
	}  
	if( !hasFlag ){
		if( $(This).parent().parent().children(".di_projectKey").val()=='null' ){ 
			//alert("手动构建，没有配置sonar扫描!"); 
			layer.alert('不支持sonar扫描!', {
				icon: 2,
				title: "提示信息"
			});
		}else{
			$("#loading").css('display','block');
			element.find(".listTitle .fa-angle-double-up").addClass("fa-angle-double-down");
			element.find(".listTitle .fa").removeClass("fa-angle-double-up");
			element.find(".questionContent").css("display","none");
			//请求方法
			loadingPage( This );
		}   
	} 
}

/**
 * @description 获取 sonar扫描数据  
 * @param toolId
 * @param dateTime
 * @param projectKey
 * @param type
 * @param p
 */
function loadingPage( This ){
	var page=1;
	$.ajax({
		url:"/devManage/structure/getSonarIssule",
		method:"post",
		data:{
			toolId:$(This).parent().parent().children(".di_toolId").val(),
			dateTime:$(This).parent().parent().children(".di_projectDateTime").val(),
			projectKey:$(This).parent().parent().children(".di_projectKey").val(),
			type:$(This).attr( "value" ),
			p:page,
		},
		success:function(myData){   
			var data = JSON.parse(myData);
			var str='<div class="sonarList"  idValue="'+$(This).attr( "value" )+'" ><div class="listTitle">';
			if( $(This).attr( "value" )=='BUG' ){
				str+='<span class="fa fa-bug fontIconSize"></span><span>Bugs 问题清单</span>';
			}else if( $(This).attr( "value" )=='CODE_SMELL' ){
				str+='<span class="fa fa-support fontIconSize"></span><span>Code Smells 问题清单</span>';
			}else if( $(This).attr( "value" )=='VULNERABILITY' ){
				str+='<span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities 问题清单</span>';
			}else if( $(This).attr( "value" )=='tests' ){
				str+='<span class="fa fa-file-text-o fontIconSize"></span><span>Tests 问题清单</span>';
			}
			str+='<span class="fa fa-remove smallIcon" onclick="romoveSonarList(this)"></span> <span class="fa fa-angle-double-up smallIcon" onclick="down2(this)"></span></div><div class="questionContent"></div></div>';
			$(This).parent().parent().children(".questionList").append( str );
			var lastPath='';
			
			var element = $(This).parent().parent().children(".questionList").children(".sonarList"); 
			var questionContent;
			var hasFlag = false;
			for( var i=0;i<element.length;i++ ){ 
				if( element.eq( i ).attr( "idValue" ) == $(This).attr( "value" )  ){
					questionContent = element.eq( i ).children( ".questionContent" );
					 
				}
			}
			
			//tests的数据 与 其他数据（ bugs Vulnerabilities Code Smells ） 的数据格式不同 ，所以要 区分 ，单独处理
			if( $(This).attr( "value" ) == 'tests' ){  
				for(var i=0;i<data.components.length;i++){ 
					var onesDiv='';
					var messageStr = data.components[i].key.split(':')[1]; 
					onesDiv +='<div class="questionBody"><div class="rowdiv"><div class="erorrCont fontWeihgt def_col_31">'+ messageStr +'</div><div class="lineCont def_col_5 font_right">'+ data.components[i].measures[0].value +'</div></div></div>';
					questionContent.append( onesDiv );
				}
				if( Math.ceil(data.paging.total/data.paging.pageSize ) == data.paging.pageIndex || data.paging.total <= data.paging.pageSize ){
					var footer='<div class="questionFooter">显示 <span>'+data.paging.total+'</span> / <span>'+data.paging.total+'</span></div>';
				}else{
					var footer='<div class="questionFooter">显示 <span>'+data.paging.pageIndex*data.paging.pageSize+'</span> / <span>'+data.paging.total+'</span> <span class="a_style" value='+page+' onclick="moreLoadingPage(this)">更多...</span></div>';
				}
				questionContent.append( footer ); 
			}else{    
				for(var i=0;i<data.issues.length;i++){
					var onesDiv='';
					if(lastPath!=data.issues[i].component){
						onesDiv +='<div class="questionPath"> '+ data.issues[i].component +' </div>';
					}
					onesDiv +='<div class="questionBody"><div class="rowdiv"><div class="erorrCont fontWeihgt def_col_31">'+ data.issues[i].message +'</div><div class="lineCont def_col_5 font_right">Line:'+ data.issues[i].line +'</div></div>'+
						'<div class="rowdiv"><span>'+ data.issues[i].author +'</span>&nbsp;&nbsp;&nbsp;<span>'+ data.issues[i].creationDate.replace(/T/,' ') +'</span></div></div>';

					lastPath=data.issues[i].component;
					questionContent.append( onesDiv );
				}
				if( Math.ceil(data.total/data.ps)==data.p || data.total <= data.ps ){
					var footer='<div class="questionFooter">显示 <span>'+data.total+'</span> / <span>'+data.total+'</span></div>';
				}else{
					var footer='<div class="questionFooter">显示 <span>'+data.p*data.ps+'</span> / <span>'+data.total+'</span> <span class="a_style" value='+page+' onclick="moreLoadingPage(this)">更多...</span></div>';
				}
				questionContent.append( footer ); 
			} 
			$("#loading").css('display','none');
		},
	});

}

/**
 * @description 更多 sonar扫描数据  
 * @param toolId
 * @param dateTime
 * @param projectKey
 * @param type
 * @param p
 */
function moreLoadingPage(This){
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/structure/getSonarIssule",
		method:"post",
		data:{
			toolId:$(This).parent().parent(".questionContent").parent(".sonarList").parent(".questionList").parent(".oneResult").children(".di_toolId").val(),
			dateTime:$(This).parent().parent(".questionContent").parent(".sonarList").parent(".questionList").parent(".oneResult").children(".di_projectDateTime").val(),
			projectKey:$(This).parent().parent(".questionContent").parent(".sonarList").parent(".questionList").parent(".oneResult").children(".di_projectKey").val(),
			type:$(This).parent().parent(".questionContent").parent(".sonarList").attr( "idValue" ),
			p: ( Number( $(This).attr("value") )+1 ) ,
		},
		success:function(myData){
			
			var node=$(This).parent(".questionFooter").parent(".questionContent");
			var data=JSON.parse( myData );
			
			if( $(This).parent(".questionFooter").parent(".questionContent").parent(".sonarList").attr("idValue") == 'tests' ){
				for(var i=0;i<data.components.length;i++){
					var messageStr = data.components[i].key.split(':')[1]; 
					onesDiv +='<div class="questionBody"><div class="rowdiv"><div class="erorrCont fontWeihgt def_col_31">'+ messageStr +'</div><div class="lineCont def_col_5 font_right">'+ data.components[i].measures[0].value +'</div></div></div>';
					node.append( onesDiv );
				}
				$(This).parent(".questionFooter").remove();
				if( Math.ceil(data.paging.total/data.paging.pageSize ) == data.paging.pageIndex || data.paging.total <= data.paging.pageSize ){
					var footer='<div class="questionFooter">显示 <span>'+data.paging.total+'</span> / <span>'+data.paging.total+'</span></div>';
				}else{
					var footer='<div class="questionFooter">显示 <span>'+data.paging.pageIndex*data.paging.pageSize+'</span> / <span>'+data.paging.total+'</span> <span class="a_style" value='+page+' onclick="moreLoadingPage(this)">更多...</span></div>';
				}
			}else{ 
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

/**
 * @description 时间选择框配置
 */
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

var ztree_rows = ''; 
/**
 * @description 手动部署操作
 * @param systemId
 */
function getAllJobName(This,rows){
	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
	ztree_rows = '';
	ztree_rows = rows;
	if ($(This).hasClass('active')) {
	  $(This).next().hide();
	  return;
    }
	$('#loading').show();
	$(This).next().empty();
	$.ajax({
		url:"/devManage/structure/getJobName",
		method:"post",
		data:{
			systemId:rows.id,
		},
		success:function(data){
			var str = '';
			if(data.status == 1){
				if(data.ztreeObjs.length){
					var arr = data.ztreeObjs;
					for (var i = 0; i < arr.length; i++) {
//						arr[i].open = true;
						for (var j = i+1; j < arr.length; j++) {
							if (arr[i].id == arr[j].id && arr[i].pId == arr[j].pId) {
								arr.splice(j,1);
								j--;
							}
						}
					}
					str += '<div class="treeSearch">'+
								'<input class="treeSearchInput" id="treeSearchInput" type="text" placeholder="查询目录名称">'+
								'<span class="fa fa-search" id="faSearch"></span>'+
							'</div>';
					str += '<ul id="treeDemo'+rows.id+'" class="ztree"></ul>';
					$(This).next().append(str );
					$.fn.zTree.init($("#treeDemo"+rows.id), setting, arr);
					fuzzySearch('treeDemo'+rows.id, '#treeSearchInput', null, true); //初始化模糊搜索方法
				}
			}
			$('#loading').hide();
		},
		error:function(){
			$('#loading').hide();
		}
	});
}

//	ztree 点击
function ztreeOnClick(event, treeId, treeNode, clickFlag) {
	if(!treeNode.isParent){
		$('._select_box_menu').hide();
		manualBuilding( treeNode.realId ,treeNode.name,ztree_rows);
	}
}

/**
 * @description 获取构建参数
 * @param systemJenkisId
 */
function manualBuilding( id ,jobName,rows){
	var systemCode = rows.systemCode;
	var systemName = rows.systemName;
	$("#manualBuildingTable tbody").empty();
	$('#loading').show();
	$.ajax({
		url:"/devManage/structure/getJobNameParam",
		method:"post",
		data:{
			systemJenkisId:id,
		},
		success:function(data){
			$( "#manualJobName" ).text( jobName );
			$( "#thisManualJobName" ).val( data.id )
			//$("#manualBuildingDIVTitle").html( rows.systemCode+" "+rows.systemName+" <span class='fontWeihgt'>手动构建参数化</span>");
			$("#manualBuildingDIVTitle").html( systemCode+" "+systemName+" <span class='fontWeihgt'>构建参数化</span>");
			
			if(data.paramJson){						
				var dataList=$.parseJSON(data.paramJson);
				var str="";
	//            if( dataList.length%2!=0 ){
	//            	 dataList.push( {} );
	//            }
				for( var i=0;i<dataList.length;i++ ){
					var tdStr="";
					tdStr+='<tr><td class="font_right">';
	//  				if( i%2==0 ){
	//  					tdStr+='<tr><td class="font_right">';
	//  				}else{
	//  					tdStr+='<td class="font_right">';
	//  				}
					if( dataList[i].name ){
						tdStr+='<span class="paramName">'+dataList[i].name+'</span></td><td>'
					}else{
						tdStr+='</td><td>'
					}
					switch( dataList[i].type )
					{
						case 'String':
							tdStr+='<input type="text" class="form-control" name="my'+dataList[i].type+'" value="'+dataList[i].defaultValue+'"  />';
							break;
						case 'Boolean':
							tdStr+='<select class="selectpicker" name="my'+dataList[i].type+'" data-dropup-auto="false"><option value="">请选择</option>';
							if( dataList[i].defaultValue==true ){
								tdStr+='<option value="true" checked>true</option><option value="false">false</option></select>';
							}else{
								tdStr+='<option value="true">true</option><option value="false" selected = "selected" >false</option></select>';
							}
							break;
						case 'Choice':
							tdStr+='<select class="selectpicker" name="my'+dataList[i].type+'" data-dropup-auto="false"><option value="">请选择</option>';
							for( var key = 0;key<dataList[i].choices.length;key++ ){
								if(key==0){
									tdStr+='<option value="'+dataList[i].choices[key]+'" selected = "selected" >'+dataList[i].choices[key]+'</option>';
								}else{
									tdStr+='<option value="'+dataList[i].choices[key]+'">'+dataList[i].choices[key]+'</option>';
								}
							}
							tdStr+='</select>';
							break;
						case 'Checkbox':
							tdStr+='<input class="selectpicker" type="checkbox" name="selectAllCheckbox" value="" onchange="selectAllCheckbox(this)">&nbsp;&nbsp;全选</option>';
							tdStr+='<br/>';
							for( var key = 0;key<dataList[i].choices.length;key++ ){
								tdStr+='<input class="selectpicker" type="checkbox" name="my'+dataList[i].type+'" value="'+dataList[i].choices[key]+'">&nbsp;&nbsp;'+dataList[i].choices[key]+'</option>';
								tdStr+='<br/>';
							}
							break;
						case 'undefined':
							tdStr+='';
							break;
						default:
							tdStr+='';
					}
					if( dataList[i].description ){
						tdStr+='</td><td><span class="description">'+dataList[i].description+'</span></td>';
					}else{
						tdStr+='</td><td></td>';
					}
	
	//  				if( i%2==1 ){
	//  					tdStr+='</td></tr>';
	//  				}else{
	//  					tdStr+='</td>';
	//  				}
					tdStr+='</tr>';
					str+=tdStr;
				}
	
				$( "#manualBuildingTable tbody" ).append( str );
			}
			$('.selectpicker').selectpicker('refresh');
			$("#manualBuildingDIV").modal("show");
			$('#loading').hide();
		},
		error:function(){
			$('#loading').hide();
		}
	});
}

//手动构建开始
function startManualBuilding(){
	$("#loading").css('display','block');
//	var data=[];
//	for( var i=0;i<$("#manualBuildingTable tbody td").length;i++  ){
//		(function(i){
//			if(  !( $("#manualBuildingTable tbody td").eq(i).is(":empty") )  ){
//				 if( $("#manualBuildingTable tbody td").eq(i).find("input[name*='myString']").size()!=0 ){
//					 data.push( $("#manualBuildingTable tbody td").eq(i).find("input[name*='myString']").val() );
//					 return;
//				 }else if( $("#manualBuildingTable tbody td").eq(i).find("select[name*='myBoolean']").size()!=0 ){
//					 data.push(  $("#manualBuildingTable tbody td").eq(i).find("select[name*='myBoolean']").val() );
//					 return;
//				 }else if( $("#manualBuildingTable tbody td").eq(i).find("select[name*='myChoice']").size()!=0 ){
//					 data.push( $("#manualBuildingTable tbody td").eq(i).find("select[name*='myChoice']").val() );
//					 return;
//				 }
//				 data.push( $("#manualBuildingTable tbody td").eq(i).children("span").text() );
//			 }
//		})(i)
//	}



//	if( data.length%2==0 ){
	var obj="";
	var id=$("#thisManualJobName").val().toString();
//		for(var i=0;i<data.length;i=i+2){
//			var key,value;
//			key=data[i];
//			value=data[i+1];
//			obj+='"'+key+'":"'+data[i+1]+'",';
//		}

	$("#manualBuildingTable tbody tr").each(function(i){
		var name = $(this).find("td:first span").text();
		var value = "";
		if( $(this).find("input[name*='myString']").size()!=0 ){
			value = $(this).find("input[name*='myString']").val();
		} else if( $(this).find("select[name*='myBoolean']").size()!=0 ){
			value = $(this).find("select[name*='myBoolean']").val();
		} else if( $(this).find("select[name*='myChoice']").size()!=0 ){
			value = $(this).find("select[name*='myChoice']").val();
		} else if( $(this).find("input[name*='myCheckbox']").size()!=0 ){
			$(this).find("input[name*='myCheckbox']").each(function(){
				if ($(this).prop('checked')) {
					value += $(this).val() + "##";//726有参数值包含逗号所以用##分隔
				}
			});
			if (value.length > 0) {
				value = value.substring(0, value.length - 2);
			}
		}
		obj+='"'+name+'":"'+value+'",';
	});
	obj=obj.substr(0,obj.length-1);
	obj="{"+obj+"}";
	$.ajax({
		url:"/devManage/structure/buildJobManual",
		method:"post",
		data:{
			systemJenkisId: id,
			jsonParam: obj,
		},
		success:function(data){
			autoSearchInfo();
			$("#manualBuildingDIV").modal("hide");
			$("#loading").css('display','none');
			//新增日志处理
			if(data.status=="2"){

				layer.alert('参数有误,请联系管理员!', {
					icon: 2,
					title: "提示信息"
				});
			}else{
				//新增日志处理
				detaillog(data);
			}
		},
	});
//	}else{
//
//		layer.alert('数据出错！', {
//            icon: 2,
//            title: "提示信息"
//        });
//		$("#manualBuildingDIV").modal("hide");
//	}

}

/**
 * @description 获取所有 环境类型
 */
function getEnvironmentType(){
	$.ajax({
		url:"/devManage/structure/getEnv",
		method:"post",
		contentType:"application/json;charset=utf-8",
		async: false,
		success:function(data){
			environmentTypeData=data;
		},
	});
}

/**
 * @description 刷新状态
 */
function refresh(){
	var ids="";
	var obj = $("#list2").jqGrid("getRowData");
	for (i = 0; i < obj.length; i++){
		var name=obj[i].lastBuildTime;
		var  architectureType=obj[i].architectureType;
		if(architectureType=="1" ){
			if(name.indexOf("正在构建")>-1){
				ids=ids+obj[i].id+",";
			}
		}
	}
	if(ids!=""){
		ids = ids.substr(0,ids.length-1);
	}

}

/**
 * @description 定时任务(源码)
 * @param systemId
 * @param type
 */
function scheduledTask( row ){
	var flag=row.createType;
	var systemId=row.id;
	if(flag=="2"){
		//手动构建
		$("#list4").bootstrapTable('destroy');
		$("#list4").bootstrapTable({
			url : '/devManage/structure/getCorn',
			method : "post",
			queryParamsType : "",
			pagination : true,
			sidePagination: "server",
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			queryParams : function(params) {
				var param = {
					"systemId" : row.id,
					"type" : row.createType,
				};
				return param;
			},
			columns :[{
				field : "stringId",
				title : "stringId",
				visible : false,
				align : 'center'
			},{
				field : "jobName",
				title : "手动任务名称",
				align : 'center',
			},{
				field : "jobCron",
				title : "定时表达式",
				align : 'center',
				formatter : function(value, row, index) {
					var rows = JSON.stringify(row).replace(/"/g, '&quot;');
					if(row.jobCron=='null' || row.jobCron==null){
						return '<div class="def_tableDiv2"><input class="jobCronClass form-control def_tableInput " type="text"  value=""  /></div>';
					}else{
						return '<div class="def_tableDiv2"><input class="jobCronClass form-control def_tableInput" type="text" value="'+row.jobCron+'" /></div>';
					}
				}
			},

				// {
				// 	field : "isRun",
				// 	title : "运行状态",
				// 	align : 'center',
				// 	formatter : function(value, row, index) {
				//
				// 		if(row.isRun==true ){
				// 			return "运行中";
				//
				// 		}else{
				// 			return "空闲中";
				// 		}
				// 	}
				// },

				{
				field : "操作",
				title : "操作",
				align : 'center',
				formatter : function(value, row, index){
					var rows = JSON.stringify(row).replace(/"/g, '&quot;');
					return '<a class="a_style" style="cursor:pointer" onclick="setCron('+ rows + ','+flag+','+systemId+',this)">生效</a>'
				}
			}],
			onLoadSuccess : function() {
				$("#loading").css('display', 'none');
				$("#scheduledModal").modal('hide');
				$("#scheduledManualModal").modal('show');
			},
			onLoadError : function() {
			}
		});
	}else{
		var systemIds=row.id;
		$("#list3").bootstrapTable('destroy');
		$("#list3").bootstrapTable({
			url : '/devManage/structure/getCorn',
			method : "post",
			queryParamsType : "",
			pagination : true,
			sidePagination: "server",
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			pageNumber : 1,
			pageSize : 10,
			pageList : [ 5, 10, 15],
			queryParams : function(params) {
				var param = {
					"systemId" : row.id,
					"type" : row.createType,
				};
				return param;
			},
			columns : [{
				field : 'stringId',
				class : 'stringId hideBlock',
				title : 'stringId',
				align : 'center'
			},{
				field : 'environmentType',
				title : 'environmentType',
				visible : false,
				align : 'center'
			},{
				field : 'environmentTypeName',
				title : '环境名称',
				align : 'center',
				formatter : function(value, row, index){
					var str='';
					str+='<span class="fontContent">'+row.environmentTypeName+'</span>'+
						'<div class="def_tableDiv">'+
						'<select class="selectpicker" name="envSelect">'+
						'<option value="">请选择</option>';
					for(var k in environmentTypeData){
						str+='<option value='+k+'>'+environmentTypeData[k]+'</option>';
					}
					str+='</select></div>';
					return str;
				}
			},{
				field : 'jobCron',
				title : '定时表达式',
				align : 'center',
				formatter : function(value, row, index) {
					var rows = JSON.stringify(row).replace(/"/g, '&quot;');
					if(row.jobCron=='null' || row.jobCron==null){
						return '<div class="def_tableDiv2"><input class="jobCronClass form-control def_tableInput " type="text"  value=""  /></div>';
					}else{
						return '<div class="def_tableDiv2"><input class="jobCronClass form-control def_tableInput" type="text" value="'+row.jobCron+'" /></div>';
					}
				}
			},
			// 	{
			// 	field : "isRun",
			// 	title : "运行状态",
			// 	align : 'center',
			// 	formatter : function(value, row, index) {
			//
			// 		if(row.isRun==false || row.isRun=="false" ){
			// 			return "空闲中";
			// 		}else{
			// 			return "运行中";
			// 		}
			// 	}
			// },
				{
				field : "操作",
				title : "操作",
				align : 'center',
				formatter : function(value,  rows, index) {
					var row = JSON.stringify(rows).replace(/"/g, '&quot;');
					return '<a class="a_style" style="cursor:pointer" onclick="setCron('+ row + ','+flag+','+systemId+',this)">生效</a> '+
						'<a class="a_style cancelBtnDel"  style="cursor:pointer" onclick="deleteCron('+ row + ','+flag+','+systemId+',this)">删除</a> '+
						'  <a class="a_style cancelBtn" style="cursor:pointer" onclick="cancel( '+ row+',this)">取消</a> ';
				}
			}],
			onLoadSuccess : function() {
				$("#loading").css('display', 'none');
				$("#scheduledManualModal").modal('hide');
				$("#scheduledModal").modal('show');
			},
			onLoadError : function() {
			}
		});
	}
}

/**
 * @description 设置 定时任务
 * @param environmentType
 * @param environmentTypeName
 * @param jobCron
 * @param systemId
 */
function setCron(row,type,systemId,This){
	$("#loading").css('display','block');
	var url=""
	var data="";
	var jobCron=$(This).parent().parent().find(  '.jobCronClass'  ).val();
	if(row.stringId=="1111"){
		if(jobCron==""){
			layer.alert('请填写定时表达式', {
				icon: 2,
				title: "提示信息"
			});
			$("#loading").css('display', 'none');
			return false;
		}


		var envName=$(This).parent().parent().find("select[name='envSelect'] option:selected").text();
		var env=$(This).parent().parent().find("select[name='envSelect'] option:selected").val();
		url="/devManage/structure/insertCorn";
		data={
			"environmentType":env,
			"environmentTypeName":envName,
			"jobCron":jobCron,
			"systemId":systemId

		};
	}else{
		url="/devManage/structure/setCornOne";
		data={
			"type":type,
			"jobCron":jobCron,
			"systemJenkinsId":row.stringId
		};
	}
	$.ajax({
		url:url,
		method:"post",
		data:data,
		success:function(data){
			// $("#loading").css('display','none');
			if(type=="1"){
				$("#list3").bootstrapTable('refresh',{silent:true});
			}else{
				$("#list4").bootstrapTable('refresh',{silent:true});
			}
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
			}
		}
	});
}

/**
 * @description 删除 定时任务
 * @param type
 * @param jobCron
 * @param systemJenkinsId
 */
function deleteCron(row, type, systemId, This) {
	$("#loading").css('display','block');
	var url = ""
	var data = "";
		url="/devManage/structure/setCornOne";
		data={
			"type":type,
			"jobCron":"",
			"systemJenkinsId":row.stringId
		};

	$.ajax({
		url:url,
		method:"post",
		data:data,
		success:function(data){
			 $("#loading").css('display','none');
			if(type=="1"){
				$("#list3").bootstrapTable('refresh',{silent:true});
			}else{
				$("#list4").bootstrapTable('refresh',{silent:true});
			}
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
			}
		}
	});
}

/**
 * @description 新增定时任务
 */
function insertVersion( ){
	for( var i=0;i<$(".stringId").length;i++ ){
		if( $(".stringId").eq(i).text()=="1111" ){
			layer.alert('表格中以存在一条新增定时任务，请确定是否生效！', {
				icon: 0,
				title: "提示信息"
			});
			return ;
		}
	}
	var datas = $("#list3").bootstrapTable('getData');
	dataRow = {
		"stringId": '1111',
		"environmentType": "",
		"environmentTypeName": "",
		"jobCron" : ''
	};
	$("#list3").bootstrapTable('prepend',dataRow);
	$('.selectpicker').selectpicker('refresh');

	for( var i=0;i<$(".stringId").length;i++ ){
		if( $(".stringId").eq(i).text()=="1111" ){
			$(".stringId").eq(i).parent().find(".cancelBtn").css("display","inline-block");

			$(".stringId").eq(i).parent().find(".cancelBtnDel").css("display","none");
			$(".stringId").eq(i).parent().find(".def_tableDiv").css("display","block");
		}
	}
}

function cancel( row,This ){
	$("#list3").bootstrapTable('refresh',{silent:true});
}

/**
 * @description 强制结束
 */
function getBreakName( row ){
	var flag=row.createType;
	var systemId=row.id;

	//手动构建
	$("#list5").bootstrapTable('destroy');
	$("#list5").bootstrapTable({
		url : '/devManage/structure/getBreakName',
		method : "post",
		queryParamsType : "",
		pagination : true,
		sidePagination: "server",
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		queryParams : function(params) {
			var param = {
				"systemId" : systemId,
				"createType" :flag,
				"jobType":1
			};
			return param;
		},
		columns :[{
			field : "toolId",
			title : "toolId",
			visible : false,
			align : 'center'
		},{
			field : "jobName",
			title : "任务名称",
			align : 'center',
		},{
			field : "envName",
			title : "构建环境",
			align : 'center',
		},{
			field : "jobRunNumber",
			title : "构建编号",
			align : 'center',
		}
		,{
			field : "操作",
			title : "操作",
			align : 'center',
			formatter : function(value, row, index){
				var rows = JSON.stringify(row).replace(/"/g, '&quot;');
				//|| <a class="a_style" style="cursor:pointer" onclick="getLog('+ rows + ')">实时日志</a>
				return '<a class="a_style" style="cursor:pointer" onclick="breakBuildings('+ rows + ')">强制结束</a>  '

			}
		}],
		onLoadSuccess : function() {
			$("#loading").css('display', 'none');
			$("#breakModal").modal('show');
		},
		onLoadError : function() {
		}
	});

}

/**
 * @description 强制结束
 * @param toolId
 * @param jobName
 * @param jenkinsId
 * @param createType
 * @param jobRunNumber
 */
function breakBuildings(row){
	$("#loading").css('display','block');
	var url=""
	var data="";
	url="/devManage/structure/breakJob";
	//url="/devManage/structure/getLog";

	data={
		"toolId":row.toolId,
		"jobName":row.jobName,
		"jenkinsId":row.jenkinsId,
		"createType":row.createType,
		"jobRunNumber":row.jobRunNumber
	};

	$.ajax({
		url:url,
		method:"post",
		data:data,
		success:function(data){
//		   setTimeout(function () {
//
//		    }, 5000);
//			setInterval(appendTo,3000);
//			num=data.log;
			$("#list5").bootstrapTable('refresh',{silent:true}); 
			$("#loading").css('display','none');
			if(data.status == 2){
				layer.alert(data.message, {
					icon: 2,
					title: "提示信息"
				});
			}else{
				autoSearchInfo();
				layer.alert('已打断成功，请稍后！', {
					icon: 1,
					title: "提示信息"
				});
			}
		}
	});
}

function pause(row){ 
	timer=setInterval(function(){
		getLogloop(row);
	}, 4000);

	//getLogloop(row);
}

function getLog(row){ 
	$("#logId").empty().text("正在初始化请等待.....");  
	$("#loadingData").css( "display","block" );
	$("#JenkinsJobsTable").css( "display","none" ); 
	$('#scrollTop a[href="#StageView"]').tab('show');
	$("#logModal").modal('show');
	line=0;
	setTimeout(function() {
		pause(row);
	}, 1000);

}

function getLogNew(systemId,createType,jobRunNumber,envName,ManualjobName){
	closelog();
	$.ajax({
		url:'/devManage/structure/getLogParam',
		method:"post",
		data:{
			"systemId" : systemId,
			"createType" :createType,
			"jobType":1,
			"envName":envName,
			"ManualjobName":ManualjobName,
			"flag":1,
			"jobRunNumber":jobRunNumber
		},
		success:function(data){
			var row={};
			row.toolId=data.toolId;
			row.jobName=data.jobName;
			row.jenkinsId=data.jenkinsId;
			row.jobRunNumber=jobRunNumber;
			if(row.toolId=="" || row.jenkinsId==""){
				layer.alert("已经结束!无法查看实时日志", {
					icon: 2,
					title: "提示信息"
				});


			}else{ 
				$("#logId").empty().text("正在初始化请等待.....");  
				$("#loadingData").css( "display","block" );
				$("#JenkinsJobsTable").css( "display","none" ); 
				$('#scrollTop a[href="#StageView"]').tab('show');
				
				$("#logModal").modal('show');
				setTimeout(function() {
					pause(row);
				}, 1000);

			}
		}

	});

}

//日志中出现调用其它job的链接
function goOtherPageLog(url,toolId){
	$.ajax({
	    type: "post",
    	url: "/devManage/structure/goOtherPageLog",
    	dataType: "json",
    	data: {
    		url: url,
    		toolId:toolId
    	},
    	success: function (data) {
    		if (data.status == 1) {
    			$("#otherPageLogPre").html(data.log);
    			$("#otherPageLogDIV").modal("show");
    		} else {
    			layer.alert(data.message, {
    				icon: 2,
    				title: "提示信息"
    			}); 
    		}
    	},error:function(){
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
    }); 
}

var lastbuilding="";
var line=0;
var notes="";
function getLogloop(row){  
	//获取 stage view 数据
	if( $("#scrollTop>.nav-tabs>.active>a").attr( "aria-controls" ) == "profile" ){
		createNotes( row );
	}else if( $("#scrollTop>.nav-tabs>.active>a").attr( "aria-controls" ) == "StageView" ){
		createStageView( row );
	}  
}

var end=""; 
var num = "";
function appendTo(){
	var a = $("#logId");
	var h="";
//	console.log("appedline"+line);
	if(line==0){
		a.empty();
		h = '<p>' +notes+ '</p>';
	}else{
		h= h = '<p>' +num+ '</p>'; 
	} 
	a.append(h);
	var b=$("#scrollTop"); 
}

function closelog(){
	end="";
	line=0;
	notes="";
	num="";
	lastbuilding="";
	clearInterval(timer);
	lastbuilding="";
	count=0;
//	console.log("closeline=="+line);
//	console.log("closennum=="+num);
}

function getEnvData(){
	var obj=$("#batchenv");
	var str="";
	for(var k in environmentTypeData){

		var value=environmentTypeData[k];
		str	=str+"<li><a name="+k+" onclick='creatAllJob(this)'>"+value+"</a></li>"

	}
	obj.append(str);

}

function getTreeClick(){
	$(".treeClickClass>.tree-wrap>.treeclick").bind("click",function (){
		if( $(this).parent().next().children().attr( "type" ) == "1" ){
			if( $(this).hasClass( "fa-caret-right" ) ){
				var newArr=[];
				for( var i=0;i<treeClick.refreshIds.length;i++ ){
					if( treeClick.refreshIds[i] != $(this).parent().next().children().attr( "value" ) ){
						newArr.push(  treeClick.refreshIds[i]  );
					}
				}
				treeClick.refreshIds=newArr;
			}else{
				if( treeClick.refreshIds.indexOf( $(this).parent().next().children().attr( "value" ) ) ==  -1 ){
					treeClick.refreshIds.push( $(this).parent().next().children().attr( "value" ) );
				}
			}
		}
	})
	$(".treeClickClass>.cell-wrapper").bind("click",function (){
		if( $(this).children().attr( "type" ) == "1" ){
			if( $(this).prev().children().hasClass( "fa-caret-right" ) ){
				var newArr=[];
				for( var i=0;i<treeClick.refreshIds.length;i++ ){
					if( treeClick.refreshIds[i] != $(this).parent().next().children().attr( "value" ) ){
						newArr.push(  treeClick.refreshIds[i]  );
					}
				}
				treeClick.refreshIds=newArr;
			}else{
				if( treeClick.refreshIds.indexOf( $(this).children().attr( "value" ) ) ==  -1 ){
					treeClick.refreshIds.push( $(this).children().attr( "value" ) );
				}
			}
		}
	})
}


function sonarScanner(This){
	if( $(This).val() == "1"){
		$(".manualInfo").css("display","none");
	}else if( $(This).val() == "2"){
		$(".manualInfo").css("display","block");
	}else{
		$(".autoInfo").css("display","none");
		$(".manualInfo").css("display","none");
	}
}

//是否制品入库
function artNexus(This){
	if( $(This).val() == "1"){
		// $("#structureModal").style.setProperty(("height","680px");
		var obj1=document.getElementById("structureModal");
		obj1.style.height= "400px";
		//$("#structureModal").style.height("680px");
		$("#artDiv").show();
		$("#versionDiv").show();
		$("#note").show();

	}else {
		$("#note").hide();
		var obj2=document.getElementById("structureModal");
		obj2.style.height= "300px";
		//$("#structureModal").style.height("280px");
		$("#artDiv").hide();
		$("#versionDiv").hide(); 
		//	$("#structureModal").style.setProperty(("height","280px");  
	}
}

function searchBuildFun(self){  
	var  str = $( self ).val().toString(); 
	if( str == "" ){
		$(self).parent().parent().children( "li" ).css( "display","list-item" );
	}else{
		for( var i = 0 ; i < $(self).parent().parent().children( "li" ).length ; i++  ){
			var nameStr = $(self).parent().parent().children( "li" ).eq( i ).attr("value").toString();
			if( nameStr.indexOf( str ) != -1 ){
				$(self).parent().parent().children( "li" ).eq( i ).css( "display","list-item" );
			}else{
				$(self).parent().parent().children( "li" ).eq( i ).css( "display","none" );
			} 
		}		
	}  
}

function selectAllCheckbox(thisObj) {
	$(thisObj).parent().find("input[type='checkbox']").prop("checked", $(thisObj).prop('checked'));
}



