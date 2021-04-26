﻿/**
 * Created by 朱颜辞镜花辞树 on 2018/9/18.
 */

var _icon_word ="../static/images/file/word.png";
var _icon_excel ="../static/images/file/excel.png";
var _icon_text = "../static/images/file/text.png";
var _icon_pdf = "../static/images/file/pdf.png";
var _icon_PNG = "../static/images/file/PNG.png";
var _icon_JPG = "../static/images/file/JPG.png";
var _icon_WENHAO = "../static/images/file/wenhao.png";
var _files = [];
var _editfiles = [];
var _checkfiles = [];
var modalType = '';
var workStatus ='';

//数据字典
//需求状态
var reqStatusList = '';
//需求来源
var reqSourceList = '';
//需求类型
var reqTypeList = '';
//需求优先级
var reqPriorityList = '';
//需求计划
var reqPlanNameList = '';
//是否重点需求
var impReqStatusList = '';
//重点需求类型
var impReqTypeList = '';
//重点需求是否延误
var impReqDelayList = '';
//需求是否挂起
var hangupStatusList = '';
//需求性质
var reqPropertyList = '';
//需求分类
var reqClassifyList = '';
//细分类型
var reqSubdivisionList = '';
//是否数据迁移
var dataMigrationList = '';
//任务状态
var featureStatusList = '';
//开发工作任务状态
var devTaskStatus = '';

var editAttList = [];
var formFileList = [];

var errorDefect = '请仔细检查数据！！！';

var delFile=[];
var excelData1='';
$(function(){
    pageInit();  
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
    //所有的Input标签，在输入值后出现清空的按钮
    buttonClear();
    /*$('.color1 input[type="text"]').parent().css("position","relative");
    $('.color1 input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('.color1 input[type="text"]').bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    })
   */
    $( ".headReturn" ).bind('click',function (){
    	layer.closeAll();
    })
});
//清空表格内容
/*function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}*/


function down(This){
    if( $(This).hasClass("fa-angle-double-down") ){
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
        $(This).parents('.allInfo').children(".connect_div").slideDown(100);
    }else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
        $(This).parents('.allInfo').children(".connect_div").slideUp(100);
    }
}
 
//初始化页面
$(document).ready(function() {
	uploadFileList();
    edit_uploadList();   
    
    reqStatusList = $("#reqStatus1").find("option");
    reqSourceList = $("#reqSource").find("option");
    reqTypeList = $("#reqType").find("option");
    reqPriorityList=$("#reqPriority").find("option");
    reqPlanList=$("#reqPlan").find("option");
	
    impReqStatusList=$("#impReqStatus").find("option");
    impReqTypeList=$("#impReqType").find("option");	   
    impReqDelayList=$("#impReqDelay").find("option");   
    hangupStatusList=$("#hangupStatus").find("option");	
    
    reqPropertyList=$("#reqProperty").find("option");
    reqClassifyList=$("#reqClassify").find("option");
    reqSubdivisionList=$("#reqSubdivision").find("option");
    dataMigrationList=$("#dataMigration").find("option");
    
	featureStatusList=$("#SCfeatureStatus").find("option");
	devTaskStatus = $("#devTaskStatus").find("option");
	//时间控件 配置参数信息
    $("#planOnlineDate").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
    $("#actualOnlineDate").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
    
    $("#editPlanOnlineDate").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
    $("#editActualOnlineDate").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
  //$("#reqStatus").setReqStutsList();//调用下面js中的方法
    
   /* $("#putExcel").click(function () {
    	
        method1('list2')
    })*/
    
    
    $( "#details .focusInfo" ).bind("click",function(){
    	var flag;
    	if( $( this ).hasClass("noheart") ){
    		$( this ).removeClass("noheart")
    		$(this).attr("title","点击不再关注");
    		flag = 1;
    	}else {
    		$( this ).addClass("noheart")
    		$(this).attr("title","点击关注");
    		flag = 2;
    	} 
    	$.ajax({
            type:"POST",
            url:"/projectManage/requirement/changeAttention",
            dataType:"json",
            data:{
            	id:$( "#details .focusInfo" ).attr("id"),
            	attentionStatus: flag
            }, 
            success:function( data ){
            	 if( data.status == "success" ){
            		 if( flag ==1 ){
            			 layer.alert("关注成功！",{icon:1});
            		 }else{
            			 layer.alert("取消关注成功！",{icon:1});
            		 }
            	 }else{
            		 layer.alert("系统内部错误！！！",{icon:2});
            	 }
            }    	
        }); 
    })
});

/*jQuery.fn.extend({
	setReqStutsList:function(type){
    	var target = this;
        $.ajax({
            type:"POST",
            url:"/projectManage/requirement/getDataDicList",
            dataType:"json",
            data:{
            	datadictype:'reqStatus'        	
        	},
            success:function(reqStatus){//reqStatus为后端传过来的List集合      
            	target.find('option').remove().end();        	
                if (reqStatus.length == 0) {  
                	target.append($("<option></option>").attr("value", 0).text("查无资料"));               	
                }else{
                   $.each(reqStatus, function(key, tblDataDic) {//遍历                    	   
                       if (type == null) {  
                           //將value中的属性值赋給option的value和文本內容
                    	   target.append($("<option></option>")
                               .attr("value", tblDataDic.valueCode).text(tblDataDic.valueName));   
                        } else {                         	
                        	target.append($("<option></option>")
                                .attr("value",tblDataDic.valueCode).text(tblDataDic.valueName));
                        } 
                   }); 
                }
                //$("#reqStatus option:first").prop("selected", 'selected');
                target.selectpicker('render');
            	target.selectpicker('refresh');
            }
        });
    }
*/

//表格数据加载
function pageInit(){ 
	$("#loading").css('display','block');
	/*$("#list2").jqGrid("clearGridData"); */
    $("#list2").jqGrid({ 
    	 url:'/projectManage/requirement/getAllRequirement', 
         datatype: "json", 
         height: 'auto',  
         mtype : "post",
        multiselect : true,
        width: $(".content-table").width()*0.999,
         colNames:['id','需求编号','需求名称','需求状态','需求来源','需求类型','提出人','归属部门','需求功能点','操作'],
         colModel:[
            {name:'id',index:'id',hidden:true,sorttype:'integer'},
            {name:'requirementCode',index:'requirementCode',align:"left",
                formatter: function (value, grid, rows, state) { 
                	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                	return '<a href="javascript:void(0);" onclick="details('+rows.id+','+rows.parentId+');">'+value+'</a>';
                }
            },
            {name:'requirementName',index:'requirementName',sorttype:'string',
            	align:"left", 
                formatter: function (value, grid, rows, state) { 
                	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                	var reqPriority="";
                	for (var i = 0,len = reqPriorityList.length;i < len;i++) {
                        if(rows.requirementPriority.toLowerCase() == reqPriorityList[i].value.toLowerCase()){
                        	reqPriority =reqPriorityList[i].innerHTML;             
                        }
                    }               	  
                	var color=getColor(reqPriority);                   	
                	var str='<div class="nameNeed" style="background-color:'+ color+'">'+reqPriority+'</div>';
                    return str+'  &nbsp;&nbsp;<a href="javascript:void(0);" onclick="details('+rows.id+','+rows.parentId+');">'+value+'</a>';
                }
            },
            /*需求状态*/
            {name:'requirementStatus',index:'requirementStatus',width: 200,
            	formatter : function(value, grid,rows, state) {
            		var _status='';
            		if(rows.requirementStatus!=null){
	                    for (var i = 0,len = reqStatusList.length;i < len;i++) {
	                        if(rows.requirementStatus.toLowerCase() == reqStatusList[i].value.toLowerCase()){
	                        	_status = reqStatusList[i].innerHTML+
	                        	"<input type='hidden' id='list_reqStatusList' value='"+reqStatusList[i].innerHTML+"'>";                      
	                        }              
	                    }
            		}
                    return _status
                }
            },
            /*需求来源*/
            {name:'requirementSource',index:'requirementSource',
            	formatter : function(value, grid,rows, state) {
            		var _status='';
            		if(rows.requirementSource!=null){
	                    for (var i = 0,len = reqSourceList.length;i < len;i++) {
	                        if(rows.requirementSource.toLowerCase() == reqSourceList[i].value.toLowerCase()){
	                            _status = reqSourceList[i].innerHTML+
	                            "<input type='hidden' id='list_reqSourceList' value='"+reqSourceList[i].innerHTML+"'>";                            
	                        }
	                    }
            		}
                    return _status
                }
            },
            /*需求类型*/
            {name:'requirementType',index:'requirementType',
            	formatter : function(value, grid,rows, state) {
            		var _status='';
            		if(rows.requirementType!=null){
	                    for (var i = 0,len = reqTypeList.length;i < len;i++) {
	                        if(rows.requirementType.toLowerCase() == reqTypeList[i].value.toLowerCase()){
	                            _status =reqTypeList[i].innerHTML+
	                            "<input type='hidden' id='list_reqTypeList' value='"+reqTypeList[i].innerHTML+"'>";                           
	                        }
	                    }
            		}
                    return _status
                }
            }, 
            {name:'userName',index:'userName'},
            {name:'deptName',index:'deptName'},
            {name:'funCount',index:'funCount'
            	/*name:'需求功能点',
                index:'需求功能点',
                formatter : function(value, grid,rows, state) {
                	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                	var functionCount=getFunctionCount(rows.id);
                	return functionCount;
                }*/
            },
            {
                name:'操作',
                index:'操作',
                fixed:true,
                align:"center",
                sortable:false,
                resize:false,
                search: false,
				width:100,
                formatter : function(value, grid,rows, state) {
                	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                	var edit='';
                	if(reqEdit==true){
                		edit='<a href="javascript:void(0);" onclick="toEditReq('+rows.id+');">编辑</a>';  
                	}
                	return edit;
                }
            }  
        ],
        rowNum:10,  
        rowTotal: 10,
        rowList : [10,20,30], 
        rownumWidth: 40,
        pager: '#pager2',
        sortable:true,   //是否可排序
        sortorder: 'asc',
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true, //是否要显示总记录数 
        loadComplete :function(){
        	$("#loading").css('display','none');
        },
        beforeRequest:function(){
            $("#loading").css('display','block');
		}
    }).trigger("reloadGrid");   
} 

function getColor(reqPriority){
	var color="";
	if(reqPriority=="低"){
		color="#108EE9;";
	}else if(reqPriority=="中"){
		color="#FF9966;";
	}else if(reqPriority=="高"){
		color="#FF3366;";
	}else if(reqPriority=="紧急"){
		color="#CC33CC;";
	}else if(reqPriority=="督办"){
		color="#990099;";
	}else{
		color="#33FFFF;";
	}	
	return color;
}

/*function getFunctionCount(id){	
	var functionCount;
	$.ajax({
        type:"POST",
        url:"/projectManage/requirement/getFunctionCountByReqId",
        dataType:"json",
        data:{
        	"reqId":id	        	
    	},
    	async:false,
        success:function(data1){
        	functionCount =data1["data"];
        }    	
    });
	return functionCount;
}*/
//详情页面
function details(rid,parentId){	
	$("#loading").css( "display","block" );
	$("#checkSysModal").empty();
	cleanDetails(); 
	$.ajax({		
		type:"POST",
        url:"/projectManage/requirement/findRequirementById",
        dataType:"json",
        data:{
        	rIds:rid,
        	parentIds:parentId
    	},
  		success:function(data){    
  			//关注状态
  			$( "#details .focusInfo" ).attr("id",rid);
  			if( data.attentionStatus == 2 ){
  				//不关注
  				$( "#details .focusInfo" ).addClass("noheart");
  				$( "#details .focusInfo" ).attr("title","点击关注"); 
  			}else if( data.attentionStatus == 1 ){
  				//关注
  				$( "#details .focusInfo" ).removeClass("noheart");
  				$( "#details .focusInfo" ).attr("title","点击不再关注"); 
  			} 
  			
  			$("#itcdReqId").val();
  			$("#itcdReqId").val(data['data'].itcdRequirementId); 			
  			$("#reqCodeDetails").text( data["data"].requirementCode +" | " + data["data"].requirementName);
  			addSysModal( data.data.list )
  			showField( data.fields )
  			//基本信息		
  			if(data["data"].requirementSource!=null){
	  			for (var i = 0,len = reqSourceList.length;i < len;i++) {
	                if(data["data"].requirementSource.toLowerCase() == reqSourceList[i].value.toLowerCase()){               	
	                	$("#reqSourceNameDetails").html(reqSourceList[i].innerHTML);  
	                }
	            }
  			}
  			if(data["data"].requirementType!=null){
	  			for (var i = 0,len = reqTypeList.length;i < len;i++) {
	                if(data["data"].requirementType.toLowerCase() == reqTypeList[i].value.toLowerCase()){               	
	                	$("#reqTypeNameDetails").html(reqTypeList[i].innerHTML);  
	                }
	            }
  			}
  			if(data["data"].requirementPriority!=null){
	  			for (var i = 0,len = reqPriorityList.length;i < len;i++) {
	                if(data["data"].requirementPriority.toLowerCase() == reqPriorityList[i].value.toLowerCase()){               	
	                	$("#reqPriorityNameDetails").html(reqPriorityList[i].innerHTML);  
	                }
	            }
  			}
  			if(data["data"].requirementStatus!=null){
	  			for (var i = 0,len = reqStatusList.length;i < len;i++) {
	                if(data["data"].requirementStatus.toLowerCase() == reqStatusList[i].value.toLowerCase()){               	
	                	$("#reqStatusNameDetails").html(reqStatusList[i].innerHTML);  
	                }
	            }
  			}
  			if(data["data"].requirementPlan!=null){
  				for (var i = 0,len = reqPlanList.length;i < len;i++) {  				
  	                if(data["data"].requirementPlan.toLowerCase() == reqPlanList[i].value.toLowerCase()){               	
  	                	$("#reqPlanNameDetails").html(reqPlanList[i].innerHTML);  
  	                }
  	            }
  			} 					
  			$("#expectOnlineDateDetails").html(timestampToTime(data["data"].expectOnlineDate));			
  			$("#planOnlineDateDetails").html(timestampToTime(data["data"].planOnlineDate));
  			$("#actualOnlineDateDetails").html(timestampToTime(data["data"].actualOnlineDate));
  			$("#userNameDetails").html(data["data"].userName);			
  			$("#deptNameDetails").html(data["data"].deptName);
  			$("#devDeptNameDetails").html(data["data"].devDeptName);
  			var triFather= data["triFather"];
  			var triSon=data["triSon"];
  			if(triFather==undefined){
  				$("#relevancyDetails").html(triSon);  //关联需求		
  			}else{
  				$("#relevancyDetails").html(triFather.requirementCode+" | "+triFather.requirementName+", "+triSon);  //关联需求	
  			}  			
  			$("#createDateDetails").html(timestampToTime(data["data"].createDate));			
  			$("#lastUpdateDateDetails").html(timestampToTime(data["data"].lastUpdateDate));
  			$("#openDateDetails").html(timestampToTime(data["data"].openDate));
  			$("#requirementOverviewDetails").html(data["data"].requirementOverview);
  			$("#checkReqs").html('');
  			var requirementNames = data["data"].requirementNames;
  			if(requirementNames != null){
  				$("#checkReqs").html(requirementNames.join(",")); 
  			}
  			$("#checkCloseTime").html(data["data"].closeTime);
  			$("#relevancyDetails").html(data["data"].parentCode);
  			//重点需求
  			$("#impReqStatusNameDetails").html(data["data"].impReqStatusName);	
  			for (var i = 0,len = impReqStatusList.length;i < len;i++) {
                if(data["data"].importantRequirementStatus == impReqStatusList[i].value){
                	$("#impReqStatusNameDetails").html(impReqStatusList[i].innerHTML);  
                }
            }
  			for (var i = 0,len = impReqTypeList.length;i < len;i++) {
                if(data["data"].importantRequirementType == impReqTypeList[i].value){
                	$("#impReqTypeDetails").html(impReqTypeList[i].innerHTML);  
                }
            }  	
  			for (var i = 0,len = impReqDelayList.length;i < len;i++) {
                if(data["data"].importantRequirementDelayStatus == impReqDelayList[i].value){
                	$("#impReqDelayStatusNameDetails").html(impReqDelayList[i].innerHTML);  
                }
            }		
  			$("#impReqOnlineQuarterDetails").html(data["data"].importantRequirementOnlineQuarter+" 季度");
  			$("#impReqDelayReasonDetails").html(data["data"].importantRequirementDelayReason);			
  			//成本与收益
  			$("#directIncomeDetails").html(data["data"].directIncome); 
  			$("#forwardIncomeDetails").html(data["data"].forwardIncome);
  			$("#recessiveIncomeDetails").html(data["data"].recessiveIncome);
  			$("#directCostReductionDetails").html(data["data"].directCostReduction);
  			$("#forwardCostReductionDetails").html(data["data"].forwardCostReduction);  			
  			$("#anticipatedIncomeDetails").html(data["data"].anticipatedIncome);
  			$("#estimateCostDetails").html(data["data"].estimateCost);
  			//其他信息 		
  			for (var i = 0,len = hangupStatusList.length;i < len;i++) {
                if(data["data"].hangupStatus == hangupStatusList[i].value){
                	$("#hangupStatusNameDetails").html(hangupStatusList[i].innerHTML);  
                }
            }
  			$("#hangupDateDetails").html(timestampToTime(data["data"].hangupDate));
  			$("#changeCountDetails").html(data["data"].changeCount);  			
  			$("#devManageUserNameDetails").html(data["data"].devManageUserName); 
  			$("#reqManageUserNameDetails").html(data["data"].reqManageUserName);
  			$("#reqAcceptanceUserNameDetails").html(data["data"].reqAcceptanceUserName); 	
  			for (var i = 0,len = reqPropertyList.length;i < len;i++) {
                if(data["data"].requirementProperty == reqPropertyList[i].value){               	
                	$("#requirementPropertyDetails").html(reqPropertyList[i].innerHTML);
                }
            }  			  				
  			for (var i = 0,len = reqClassifyList.length;i < len;i++) {
                if(data["data"].requirementClassify == reqClassifyList[i].value){               	
                	$("#requirementClassifyDetails").html(reqClassifyList[i].innerHTML);  
                }
            } 			
  			for (var i = 0,len = reqSubdivisionList.length;i < len;i++) {
                if(data["data"].requirementSubdivision == reqSubdivisionList[i].value){               	
                	$("#requirementSubdivisionDetails").html(reqSubdivisionList[i].innerHTML);  
                }
            }		
  			$("#planIntegrationTestDateDetails").html(timestampToTime(data["data"].planIntegrationTestDate));
  			$("#actualIntegrationTestDateDetails").html(timestampToTime(data["data"].actualIntegrationTestDate));
  			$("#acceptanceDescriptionDetails").html(data["data"].acceptanceDescription);
  			if(data["data"].acceptanceTimeliness=="1"){
  				$("#acceptanceTimelinessDetails").html("及时");
  			}else{
  				$("#acceptanceTimelinessDetails").html("非及时");
  			} 			
  			for (var i = 0,len = dataMigrationList.length;i < len;i++) {
                if(data["data"].dataMigrationStatus == dataMigrationList[i].value){               	
                	$("#dataMigrationStatusNameDetails").html(dataMigrationList[i].innerHTML);  
                }
            }
  			$("#workloadDetails").html(data["data"].workload);  			
  			$("#connectDiv").empty();
			var code = data.featureCode == undefined ? "": data.featureCode +" | ";
			var trf= data["trf"];
//			console.log( data["trf"] );
			if(trf!=undefined){
				for(var i=0;i<trf.length;i++){
					var noBorder="";
					var featureStatusObj = getStatusName( trf[i].requirementFeatureStatus ); 
					
					if( i == 0 ){
						noBorder = "noBorder"
					}
					var str='<div class="oneTask '+noBorder+'">'+
					    '<div class="name">'+
					        '<span>'+trf[i].featureCode+'</span>'+
					        '<span class="status status'+featureStatusObj.num+'">'+featureStatusObj.name+'</span>'+
					    '</div>'+
					    '<div class="describe">'+trf[i].featureName+'</div>'+
					    '<div class="rowdiv">'+
					        '<div class="def_col_22 large">'+
					            '<span class="fa fa-th-large"></span> '+
					            '<span> 关联系统:'+delUndefined(trf[i].systemName)+'</span>'+
					        '</div>'+
					        '<div class="def_col_14 user"> '+
					            '<span class="fa fa-user"></span>'+
					            '<span> 执行人:'+delUndefined(trf[i].userName)+'</span>'+
					        '</div>'+
					    '</div>'+
					'</div>';
					$("#connectDiv").append( str ); 
					var workTask = findWorkTask(trf[i].id);
					if(workTask.length){
						for(var j=0;j<workTask.length;j++){
							var workStr='';
							var noBorder=''; 
							var workTaskObj =  getWorkTaskStatus( workTask[j].devtaskStatus ); 
							var workStr='<div class="oneWorkTask '+noBorder+'">'+
							    '<div class="name">'+
								    '<div class="rowdiv">'+
								        '<div class="def_col_24 large">'+
								            '<span class="fa fa-check-square"></span> '+
								            '<span title="'+workTask[j].devTaskCode+'"> '+workTask[j].devTaskCode+'</span>'+
								        '</div>'+
								        '<div class="def_col_12 user"> '+
								            '<span class="fa fa-user"></span>'+
								            '<span title="'+delUndefined(workTask[j].devUsername)+'"> '+delUndefined(workTask[j].devUsername)+'</span>'+
								        '</div>'+
								    '</div>'+   
							        '<span class="status status0'+workTaskObj.num+'">'+workTaskObj.name+'</span>'+
							    '</div>'+
							    '<div class="describe">'+workTask[j].devTaskName+'</div>'+ 
							'</div>';
							$("#connectDiv").append( workStr );
						}
					}
					 
				}
			}
			attList(data.attachements,"#checkFileTable");
			
			layer.open({ 
			  type: 1,
			  title: false,
			  closeBtn: 0,
		      shadeClose: true,
		      shade: false, 
		      move: false,
		      area: ['100%', '100%'], 
		      id: "1",
		      tipsMore: true,
		      anim: 2,
		      content:  $('#details'),
		      success: function(layero, index){
		    	  $("#loading").css( "display","none" );
	    	  }
		    });
        },
  		error:function(){ 
  		}        
    }); 
}

//获取状态对应的名称
function getStatusName( status ){
	var obj={};
	for (var j = 0,len = featureStatusList.length;j < len;j++) { 
		if(status == featureStatusList[j].value){
			obj.name = featureStatusList[j].innerHTML;
			obj.num = featureStatusList[j].value;
        }
    }
	return obj;
}
//获取工作任务状态名称
function getWorkTaskStatus( status ){
	var obj={};
	obj.name = "无状态";
	obj.num = 0;
	for (var j = 0,len = devTaskStatus.length;j < len;j++) {  
		if(status == devTaskStatus[j].value){ 
			obj.name = devTaskStatus[j].innerHTML;
			obj.num = devTaskStatus[j].value;
        }
    }
	return obj;
}

function delUndefined(str){
	if(str==undefined){
		str="";
	}
	return str;
}

//清空详情页面字段信息
function cleanDetails(){		
		$("#reqCodeDetails").html('');
		$("#reqNameDetails").html('');
		//基本信息
		$("#reqSourceNameDetails").html('');
		$("#reqTypeNameDetails").html('');
		$("#reqPriorityNameDetails").html('');
		$("#reqStatusNameDetails").html('');
		$("#reqPlanNameDetails").html('');	
		$("#expectOnlineDateDetails").html('');		
		$("#planOnlineDateDetails").html('');
		$("#actualOnlineDateDetails").html('');
		$("#userNameDetails").html('');	
		$("#deptNameDetails").html('');
		$("#devDeptNameDetails").html('');		
		$("#relevancyDetails").html(''); //关联需求				
					
		$("#createDateDetails").html('');
		$("#lastUpdateDateDetails").html('');
		$("#openDateDetails").html('');
		$("#requirementOverviewDetails").html('');	  			
		//重点需求
		$("#impReqStatusNameDetails").html('');
		$("#impReqTypeDetails").html('');
		$("#impReqDelayStatusNameDetails").html('');
		$("#impReqOnlineQuarterDetails").html('');
		$("#impReqDelayReasonDetails").html('');
		//成本与收益
		$("#directIncomeDetails").html('');
		$("#forwardIncomeDetails").html('');
		$("#recessiveIncomeDetails").html('');
		$("#directCostReductionDetails").html('');
		$("#forwardCostReductionDetails").html('');		
		$("#anticipatedIncomeDetails").html('');
		$("#estimateCostDetails").html('');
		//其他信息 			
		$("#hangupStatusNameDetails").html('');
		$("#hangupDateDetails").html('');
		$("#changeCountDetails").html('');
		$("#devManageUserNameDetails").html('');
		$("#reqManageUserNameDetails").html('');
		$("#reqAcceptanceUserNameDetails").html('');		
		$("#requirementPropertyDetails").html('');
		$("#requirementClassifyDetails").html('');
		$("#requirementSubdivisionDetails").html('');		
		$("#planIntegrationTestDateDetails").html('');
		$("#actualIntegrationTestDateDetails").html('');
		$("#acceptanceDescriptionDetails").html('');
		$("#acceptanceTimelinessDetails").html('');
		$("#dataMigrationStatusNameDetails").html('');
		$("#workloadDetails").html('');
}

//获取工作任务详情
function findWorkTask(id){	
	var workTask;
	$.ajax({
        type:"POST",
        url:"/devManage/devtask/getOneDevTask",
        dataType:"json",
        data:{
        	"id":id	        	
    	},
    	async:false,
        success:function(data1){
        	workTask =data1["list"];  	
        }    	
    });
	return workTask;
}

function timestampToTime(timestamp) {
	var Y,M,D;
	if(timestamp!=null){
	    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
	    Y = date.getFullYear() + '年';
	    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '月';
	    D = date.getDate() + '日';
	    //var h = date.getHours() + ':';
	    //var m = date.getMinutes() + ':';
	    //var s = date.getSeconds();	    
	}
	return Y+M+D;
}
 
function timestampToTime1(timestamp) {
	var Y,M,D;
	if(timestamp!=null){
	    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
	    Y = date.getFullYear() + '-';
	    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	    D = date.getDate();	    
	}else{
		Y='';
		M=''
		D=''
	}
	return Y+M+D;
}
/*清空搜索框*/
function clearSearch() {	
    $('#reqCode').val("");
    $('#reqName').val("");
    $("#devManageUserName").val("");
    $("#devManageUserId").val("");
    $("#reqManageUserName").val("");
    $("#reqManageUserId").val("");  
    $("#reqStatus").selectpicker('val', '');
    $("#systemName").val("");
    $("#systemId").val("");
    $("#featureName").val("");
    $("#featureId").val("");
}

//搜索
function searchInfo(){ 		
	$("#loading").css('display','block');
	$("#list2").jqGrid('setGridParam',{
		url: "/projectManage/requirement/getAllRequirement",
		postData:{
			"findRequirment":getResult()
		},
		loadComplete :function(){
			$("#loading").css('display','none');		
		},
        page:1
	}).setGridParam({datatype:'json'}).trigger("reloadGrid", { fromServer: true});//重新载入 
}


//封装查询条件
function getResult(){
	 var obj = {};	 	 
	 obj.requirementCode = $("#reqCode").val().trim();	
	 obj.requirementName  = $("#reqName").val().trim();	
	 obj.devManageIds = $("#devManageUserId").val().trim();
	 obj.reqManageIds = $("#reqManageUserId").val().trim();
	 var status = $("#reqStatus").val();
	 var str = (status == null);
	 obj.requirementStatus = getStatus(str,status);
	 obj.systemId = $("#systemId").val().trim();
	 obj.featureId = $("#featureId").val().trim();
	 obj = JSON.stringify(obj);
	 excelData1=obj;
	 return obj;
}

function getStatus(str,status){
	if(str==false){
		status=status.join(",")
	}else{
		status=null;
	}
	return status;
}

//模糊查询延迟加载
/*$(function () {
	var loseInputMillsecs = 500;
	var clocker = null;
	
	$("#featureName").on('input propertychange', function(){
		innerKeydown();		
	})
	
	function innerKeydown(){
		if(null == clocker){
			clocker = setTimeout(loadData,loseInputMillsecs);
			console.info(clocker);
		}else{		//连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData,loseInputMillsecs);
		}
	}
	
	function loadData(){
		if($("#featureName").val().trim() == '') {
	        $('.selectBox').hide()
	        return
	    }
		$.ajax({
			type: "post",
			url: "/projectManage/requirementFeature/getAllFeature",
			data: {'featureName': $("#featureName").val().trim()},
			dataType: "json",
			success: function (data) {
				var trf= data["trf"];
	        	if (trf.length>0) {
	        		var itemStr = ''
	        		for (var i = 0; i < trf.length; i++) {
	        			itemStr += '<li class="selectItem">'+trf[i].featureName+'</li>'
	        		}
	        		$('.selectUl').html(itemStr)
	        		$('.selectBox').show()
	        	} else {
	        		$('.selectBox').hide()
	        	}
			}
		})
		clocker = null;
	}		
	
	$("#selectBox").on('click', '.selectItem', function(){
		$("#featureName").val($(this).html())
		$("#selectBox").hide()
	})
	
	$(document).click(function(){
		$("#selectBox").hide()
	})
});*/
//部门模糊搜索
$(function () {
	var loseInputMillsecs = 500;
	var clocker = null;
	//新增页面归属部门模糊搜索
	$("#applyDeptName").on('input propertychange', function(){
		innerKeydown();		
	})
	
	function innerKeydown(){
		if(null == clocker){
			clocker = setTimeout(loadData,loseInputMillsecs);
			console.info(clocker);
		}else{		//连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData,loseInputMillsecs);
		}
	}
	
	function loadData(){
		if($("#applyDeptName").val().trim() == '') {
	        $('.newSelectBox').hide()
	        return
	    }
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: {'deptName': $("#applyDeptName").val().trim()},
			dataType: "json",
			success: function (data) {
				var depts= data.depts;
	        	if (depts.length>0) {
	        		var itemStr = ''
	        		for (var i = 0; i < depts.length; i++) {
	        			itemStr += '<li class="selectItem" id="'+depts[i].id+'">'+depts[i].deptName+'</li>'
	        		}
	        		$('.newSelectUl').html(itemStr)
	        		$('.newSelectBox').show()
	        	} else {
	        		$('.newSelectBox').hide()
	        	}
			}
		})
		clocker = null;
	}		
	
	$("#newSelectBox").on('click', '.selectItem', function(){
		$("#applyDeptName").val($(this).html());
		$("#applyDeptId").val($(this).attr("id"));
		$("#newSelectBox").hide()
	})
	
	$(document).click(function(){
		$("#newSelectBox").hide()
	})
	//新增页面开发处室模糊搜索
	$("#developmentDeptName").on('input propertychange', function(){
		innerKeydown2();		
	})
	
	function innerKeydown2(){
		if(null == clocker){
			clocker = setTimeout(loadData2,loseInputMillsecs);
			console.info(clocker);
		}else{		//连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData2,loseInputMillsecs);
		}
	}
	
	function loadData2(){
		if($("#developmentDeptName").val().trim() == '') {
	        $('.newSelectBox1').hide()
	        return;
	    }
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: {'deptName': $("#developmentDeptName").val().trim()},
			dataType: "json",
			success: function (data) {
				var depts= data.depts;
	        	if (depts.length>0) {
	        		var itemStr = ''
	        		for (var i = 0; i < depts.length; i++) {
	        			itemStr += '<li class="selectItem" id="'+depts[i].id+'">'+depts[i].deptName+'</li>'
	        		}
	        		$('.newSelectUl1').html(itemStr)
	        		$('.newSelectBox1').show()
	        	} else {
	        		$('.newSelectBox1').hide()
	        	}
			}
		})
		clocker = null;
	}		
	
	$("#newSelectBox1").on('click', '.selectItem', function(){
		$("#developmentDeptName").val($(this).html());
		$("#developmentDeptId").val($(this).attr("id"));
		$("#newSelectBox1").hide()
	})
	
	$(document).click(function(){
		$("#newSelectBox1").hide()
	})
	
	//编辑页面归属部门模糊搜索
	$("#editApplyDeptName").on('input propertychange', function(){
		innerKeydown3();		
	})
	
	function innerKeydown3(){
		if(null == clocker){
			clocker = setTimeout(loadData3,loseInputMillsecs);
			console.info(clocker);
		}else{		//连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData3,loseInputMillsecs);
		}
	}
	
	function loadData3(){
		if($("#editApplyDeptName").val().trim() == '') {
	        $('.editSelectBox').hide()
	        return;
	    }
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: {'deptName': $("#editApplyDeptName").val().trim()},
			dataType: "json",
			success: function (data) {
				var depts= data.depts;
	        	if (depts.length>0) {
	        		var itemStr = ''
	        		for (var i = 0; i < depts.length; i++) {
	        			itemStr += '<li class="selectItem" id="'+depts[i].id+'">'+depts[i].deptName+'</li>'
	        		}
	        		$('.editSelectUl').html(itemStr)
	        		$('.editSelectBox').show()
	        	} else {
	        		$('.editSelectBox').hide()
	        	}
			}
		})
		clocker = null;
	}		
	
	$("#editSelectBox").on('click', '.selectItem', function(){
		$("#editApplyDeptName").val($(this).html());
		$("#editApplyDeptId").val($(this).attr("id"));
		$("#editSelectBox").hide()
	})
	
	$(document).click(function(){
		$("#editSelectBox").hide()
	})
	
	//编辑页面开发处室模糊搜索
	$("#editDevelopmentDeptName").on('input propertychange', function(){
		innerKeydown4();		
	})
	
	function innerKeydown4(){
		if(null == clocker){
			clocker = setTimeout(loadData4,loseInputMillsecs);
			console.info(clocker);
		}else{		//连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData4,loseInputMillsecs);
		}
	}
	
	function loadData4(){
		if($("#editDevelopmentDeptName").val().trim() == '') {
	        $('.editSelectBox1').hide()
	        return;
	    }
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: {'deptName': $("#editDevelopmentDeptName").val().trim()},
			dataType: "json",
			success: function (data) {
				var depts= data.depts;
	        	if (depts.length>0) {
	        		var itemStr = ''
	        		for (var i = 0; i < depts.length; i++) {
	        			itemStr += '<li class="selectItem" id="'+depts[i].id+'">'+depts[i].deptName+'</li>'
	        		}
	        		$('.editSelectUl1').html(itemStr)
	        		$('.editSelectBox1').show()
	        	} else {
	        		$('.editSelectBox1').hide()
	        	}
			}
		})
		clocker = null;
	}		
	
	$("#editSelectBox1").on('click', '.selectItem', function(){
		$("#editDevelopmentDeptName").val($(this).html());
		$("#editDevelopmentDeptId").val($(this).attr("id"));
		$("#editSelectBox1").hide()
	})
	
	$(document).click(function(){
		$("#editSelectBox1").hide()
	})
});


/*导出*/
function ExcelRequirement(){	
	var url=encodeURI("/projectManageui/requirement/getExcelRequirement?excelData="+excelData1);
	window.location.href=url;
}

//新增需求弹窗
function newReq_btn(){	
	$('#newReq').on('hide.bs.modal', function () {
		$('#newform').bootstrapValidator('resetForm');
	});
	laydate.render({
        show: true,
        elem: '#closeTime',
        type: 'datetime'
    });
	formFileList = [];
	editAttList = [];
	$("#newSysInput").empty();
	$("#canEditField").empty();
	$("#loading").css('display','block');
	$.ajax({
		url:"/projectManageui/requirement/getData",
		dataType:"json",
		type:"post",
		success:function(data){
			$("#loading").css('display','none');
			$("#newFileTable").empty();
			$("#requirementName").val("");
			$("#requirementOverview").val("");
			$("#requirementStatus").prop('selectedIndex', 0);
			$("#requirementSource").prop('selectedIndex', 0);
			$("#requirementType").prop('selectedIndex', 0);
			$("#applyUserId").val("");
			$("#applyUserName").val('');
			$("#applyDeptId").empty();		
			$("#applyDeptId").append('<option value="">请选择</option>');	
			$("#developmentManageUserId").val('');
			$("#developmentManageUserName").val('');
			$("#developmentDeptId").empty();
			$("#developmentDeptId").append('<option value="">请选择</option>');	
			$("#requirementManageUserId").val('');			
			$("#requirementManageUserName").val('');
			$("#requirementAcceptanceUserId").val('');
			$("#requirementAcceptanceUserName").val('');
			$("#requirementPriority").prop('selectedIndex', 0);
			$("#planOnlineDate").val('');
			$("#actualOnlineDate").val('');
			$("#importantRequirementStatus").prop('selectedIndex', 0);			
			$("#changeCount").val('');
			$("#workload").val('');
			$("#acceptanceDescription").val(''); 	
			$("#systemIds").val(''); 	
			$("#systems").val(''); 	
			$("#closeTime").val(''); 	
			$("#requirementIds").val(''); 	
			$("#requirements").val(''); 	
			$("#moduleId").val(''); 	
			$("#module").val(''); 	
			$("#parentId").val(''); 	
			$("#parent").val(''); 	
			
			for(var a=0;a<data.depts.length;a++){
				$("#applyDeptId").append(' <option value="'+data.depts[a].id+'">'+data.depts[a].deptName+'</option>');
			}			
			for(var a=0;a<data.depts.length;a++){
				$("#developmentDeptId").append(' <option value="'+data.depts[a].id+'">'+data.depts[a].deptName+'</option>');
			}
			$('.selectpicker').selectpicker('refresh'); 
		}
	});
	$("#loading").css('display','block');
	$.ajax({
		url:"/projectManage/requirement/getRequirementFiled",
		dataType:"json",
		type:"post",
		success:function(data){
			if(!data.fields) return;
        	for( var i=0;i<data.fields.length;i++ ){  
        		if( data.fields[i].status == 1 ){
        			appendDataType( data.fields[i] , 'canEditField' ,'new'); 
        		}  
        	}  
			$('.selectpicker').selectpicker('refresh'); 
		}
	});
	modalType = 'new';
	$("#newReq").modal("show");
	_files=[];
	delFile=[];
}

//新增需求提交
function addCommit(){  
	$('#newform').data('bootstrapValidator').validate();
	if(!$('#newform').data('bootstrapValidator').isValid()){
		$('#newReq').on('hide.bs.modal', function () {
			     $('#newform').bootstrapValidator('resetForm');
			});
		return;
	} 
	var List = [];	
	for( var i = 0;i< $("#newSysInput>div").length ; i++){
		var obj = {
			systemId: $("#newSysInput>div").eq( i ).find( ".systemIds" ).val(),
			assetSystemTreeId: $("#newSysInput>div").eq( i ).find( ".new_moduleName" ).attr("assetsystemtreeid"),
			functionCount:$("#newSysInput>div").eq( i ).find( ".new_functionCount" ).val()
		}
		if( obj.systemId == '' || obj.systemId == undefined || obj.systemId == null ){
			layer.alert( "系统不能为空", {
                icon: 2,
                title: "提示信息"
            });
			return;
		} 
		if( obj.functionCount == '' || obj.functionCount == undefined || obj.functionCount == null ){
			layer.alert( "功能点数不能为空", {
                icon: 2,
                title: "提示信息"
            });
			return;
		}
		List.push( obj );
	}   
	
	//获取系统模块 
	var requirementName = $("#requirementName").val();
	var requirementOverview = $("#requirementOverview").val();	
	var requirementStatus = $("#requirementStatus").val();
	var requirementSource = $("#requirementSource").val();
	var requirementType = $("#requirementType").val();
	var applyUserId = $("#applyUserId").val();
	var applyDeptId = $("#applyDeptId").val();
	var developmentManageUserId = $("#developmentManageUserId").val();
	var developmentDeptId = $("#developmentDeptId").val();
	var requirementManageUserId = $("#requirementManageUserId").val();
	var requirementAcceptanceUserId = $("#requirementAcceptanceUserId").val();
	var requirementPriority = $("#requirementPriority").val();	
	var planOnlineDate = $("#planOnlineDate").val();
	var actualOnlineDate = $("#actualOnlineDate").val();
	var importantRequirementStatus = $("#importantRequirementStatus").val();
	var changeCount = $("#changeCount").val();
	var workload = $("#workload").val();
	var acceptanceDescription = $("#acceptanceDescription").val();
	var closeTime = $("#closeTime").val();
	var requirementIds = $("#requirementIds").val();
	var systemIds = $("#systemIds").val();
	var moduleId = $("#moduleId").val(); 
	var parentId = $("#parentId").val();
	
	var fieldTemplate = getFieldData( "canEditField" ); 
	for( var i=0;i< fieldTemplate.field.length;i++ ){   
	 	if(  fieldTemplate.field[i].required == "false" ){
	 		if(  fieldTemplate.field[i].valueName == ""||  fieldTemplate.field[i].valueName == null|| fieldTemplate.field[i].valueName == undefined ){
	 			$("#loading").css('display','none');
	 			layer.alert( fieldTemplate.field[i].labelName+"不能为空", {
	                 icon: 2,
	                 title: "提示信息"
	             });
	     		 
	     		return;
	     	}
	 	}
	}
	$("#loading").css('display','block');	
	$.ajax({
		url:"/projectManage/requirement/addRequirement",
		dataType:"json",
		type:"post",
		data:{
			 "requirementName" : requirementName,
			 "requirementOverview" : requirementOverview,
			 "requirementStatus" : requirementStatus,
			 "requirementSource" :requirementSource,
			 "requirementType" : requirementType,			 
			 "applyUserId" :applyUserId,
			 "applyDeptId" : applyDeptId,
			 "developmentManageUserId" : developmentManageUserId,		 
			 "developmentDeptId" : developmentDeptId,
			 "requirementManageUserId":requirementManageUserId,			 
			 "requirementAcceptanceUserId" : requirementAcceptanceUserId,
			 "requirementPriority" : requirementPriority,		 
			 "startDate" : planOnlineDate,
			 "endDate":actualOnlineDate,
			 "importantRequirementStatus":importantRequirementStatus,
			 "changeCount":changeCount,			 
			 "workload1":workload,
			 "acceptanceDescription":acceptanceDescription,
			 "time":closeTime,
			 "requirementIds":requirementIds,
			 "parentId":parentId,
			 "reqSysList":JSON.stringify(List),
			 "fieldTemplate": JSON.stringify(fieldTemplate),
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){
				uploadFile(data.reqId);
			}else if (data.status == "2"){
				layer.alert('保存失败！！！', {icon: 2});
			}else if(data.status =="003"){				
				layer.alert('系统和模块重复！！！', {icon: 0});
			}
		}
	});
}
/*编辑需求弹窗*/
function toEditReq(rid){
	clearEditReq();
	$('#editReq').on('hide.bs.modal', function () {
		     $('#editform').bootstrapValidator('resetForm');
	}); 
	laydate.render({
        show: true,
        elem: '#editCloseTime',
        type: 'datetime'
    });
	$( "#editSysInput" ).empty(); 
	$( "#editFieldDiv" ).empty();    
	formFileList = [];
	editAttList = [];
	$("#loading").css('display','block');	
	$.ajax({		
		type:"POST",
        url:"/projectManageui/requirement/getData",
        dataType:"json",
        data:{
        	rIds:rid
    	},
  		success:function(data){  
  			$("#loading").css('display','none');  			
  			$("#editFileTable").empty();
  			$("#editReqId").val(data["data"].id);
			$("#editRequirementName").val(data["data"].requirementName);
			$("#editRequirementOverview").val(data["data"].requirementOverview);
			$("#editRequirementStatus").val(data["data"].requirementStatus.toUpperCase());
			$("#editRequirementSource").val(data["data"].requirementSource.toUpperCase());
			$("#editRequirementType").val(data["data"].requirementType.toUpperCase());
			$("#editApplyUserId").val(data["data"].applyUserId);
			$("#editApplyUserName").val(data["data"].userName);
			$("#editApplyDeptId").empty();		
			$("#editApplyDeptId").append('<option value="">请选择</option>');					
			$("#editDevelopmentManageUserId").val(data["data"].developmentManageUserId);
			$("#editDevelopmentManageUserName").val(data["data"].devManageUserName);
			$("#editDevelopmentDeptId").empty();
			$("#editDevelopmentDeptId").append('<option value="">请选择</option>');	
			$("#editRequirementManageUserId").val(data["data"].requirementManageUserId);			
			$("#editRequirementManageUserName").val(data["data"].reqManageUserName);
			$("#editRequirementAcceptanceUserId").val(data["data"].requirementAcceptanceUserId);
			$("#editRequirementAcceptanceUserName").val(data["data"].reqAcceptanceUserName);
			$("#editRequirementPriority").val(data["data"].requirementPriority.toUpperCase())			
			$("#editPlanOnlineDate").val(timestampToTime1(data["data"].planOnlineDate));
			$("#editActualOnlineDate").val(timestampToTime1(data["data"].actualOnlineDate));			
			$("#editImportantRequirementStatus").val(data["data"].importantRequirementStatus);			
			$("#editChangeCount").val(data["data"].changeCount);
			$("#editWorkload").val(data["data"].workload);
			$("#editAcceptanceDescription").val(data["data"].acceptanceDescription);
			$("#editRequirementIds").val(data["data"].requirementIds);		
			$("#editRequirements").val(data["data"].requirementNames);		
//			$("#editSystemIds").val(data["data"].systemIds);		
//			$("#editSystems").val(data["data"].systemNames);	 	
			$("#editCloseTime").val(data["data"].closeTime);
			$("#editParent").val(data["data"].parentCode);
			$("#editParentId").val(data["data"].parentId);
			
			showList( data["data"].list );
			
			
			var field = data.fields;
			if( field != undefined ){
		    	for( var i = 0;i < field.length;i++ ){
		        	appendDataType( field[i] , 'editFieldDiv' , 'edit' ); 
		        }
		    }
			
			for(var a=0;a<data.depts.length;a++){
				var flag = "";
				if(data.depts[a].id == data["data"].applyDeptId){
					flag = "selected";
				}
				$("#editApplyDeptId").append(' <option '+flag+' value="'+data.depts[a].id+'">'+data.depts[a].deptName+'</option>');
			}
			
			for(var a=0;a<data.depts.length;a++){
				var flag = "";
				if(data.depts[a].id == data["data"].developmentDeptId){
					flag = "selected";
				}
				$("#editDevelopmentDeptId").append(' <option '+flag+' value="'+data.depts[a].id+'">'+data.depts[a].deptName+'</option>');
			}
			 									
			$('.selectpicker').selectpicker('refresh'); 
			attList(data.attachements,"#editFileTable");
		}
	})	
	modalType = 'edit';
	$("#editReq").modal("show");
	delFile=[];
}

function clearEditReq(){
	$("#editFileTable").empty();
	$("#editReqId").val('');
	$("#editRequirementName").val('');
	$("#editRequirementOverview").val('');
	$("#editRequirementStatus").prop('selectedIndex', 0);
	$("#editRequirementSource").prop('selectedIndex', 0);
	$("#editRequirementType").prop('selectedIndex', 0);
	$("#editApplyUserId").val('');
	$("#editApplyUserName").val('');	
	$("#editDevelopmentManageUserId").val('');
	$("#editDevelopmentManageUserName").val('');
	$("#editRequirementManageUserId").val('');			
	$("#editRequirementManageUserName").val('');
	$("#editRequirementAcceptanceUserId").val('');
	$("#editRequirementAcceptanceUserName").val('');
	$("#editRequirementPriority").prop('selectedIndex', 0);			
	$("#editPlanOnlineDate").val('');
	$("#editActualOnlineDate").val('');			
	$("#editImportantRequirementStatus").prop('selectedIndex', 0);			
	$("#editChangeCount").val('');
	$("#editWorkload").val('');
	$("#editAcceptanceDescription").val('');
	$("#editParentId").val('');
	$("#editParent").val('');
}
//编辑需求提交
function editCommit(){  
	$('#editform').data('bootstrapValidator').validate();
	if(!$('#editform').data('bootstrapValidator').isValid()){
		$('#editReq').on('hide.bs.modal', function () {
			     $('#editform').bootstrapValidator('resetForm');
			});
		return;
	}
	var List = [];	
	for( var i = 0;i< $("#editSysInput>div").length ; i++){
		var obj = {
			systemId: $("#editSysInput>div").eq( i ).find( ".systemIds" ).val(),
			assetSystemTreeId: $("#editSysInput>div").eq( i ).find( ".new_moduleName" ).attr("assetsystemtreeid"),
			functionCount:$("#editSysInput>div").eq( i ).find( ".new_functionCount" ).val()
		}
		if( obj.systemId == '' || obj.systemId == undefined || obj.systemId == null ){
			layer.alert( "系统不能为空", {
                icon: 2,
                title: "提示信息"
            });
			return;
		} 
		if( obj.functionCount == '' || obj.functionCount == undefined || obj.functionCount == null ){
			layer.alert( "功能点数不能为空", {
                icon: 2,
                title: "提示信息"
            });
			return;
		}
		List.push( obj );
	}   
	var requirementId = $("#editReqId").val();
	var requirementName = $("#editRequirementName").val();
	var requirementOverview = $("#editRequirementOverview").val();	
	var requirementStatus = $("#editRequirementStatus").val();
	var requirementSource = $("#editRequirementSource").val();
	var requirementType = $("#editRequirementType").val();
	var applyUserId = $("#editApplyUserId").val();
	var applyDeptId = $("#editApplyDeptId").val();
	var developmentManageUserId = $("#editDevelopmentManageUserId").val();
	var developmentDeptId = $("#editDevelopmentDeptId").val();
	var requirementManageUserId = $("#editRequirementManageUserId").val();
	var requirementAcceptanceUserId = $("#editRequirementAcceptanceUserId").val();
	var requirementPriority = $("#editRequirementPriority").val();	
	var planOnlineDate = $("#editPlanOnlineDate").val();
	var actualOnlineDate = $("#editActualOnlineDate").val();
	var importantRequirementStatus = $("#editImportantRequirementStatus").val();
	var changeCount = $("#editChangeCount").val();
	var workload = $("#editWorkload").val();
	var acceptanceDescription = $("#editAcceptanceDescription").val();
	var requirementIds = $("#editRequirementIds").val();
	var closeTime = $("#editCloseTime").val();
	var parentId = $("#editParentId").val();
	
	var fieldTemplate = getFieldData( "editFieldDiv" ); 
	for( var i=0;i< fieldTemplate.field.length;i++ ){   
	 	if(  fieldTemplate.field[i].required == "false" ){
	 		if(  fieldTemplate.field[i].valueName == ""||  fieldTemplate.field[i].valueName == null|| fieldTemplate.field[i].valueName == undefined ){
	 			$("#loading").css('display','none');
	 			layer.alert( fieldTemplate.field[i].labelName+"不能为空", {
	                 icon: 2,
	                 title: "提示信息"
	             });
	     		 
	     		return;
	     	}
	 	}
	} 
	
	$("#loading").css('display','block');	
	$.ajax({
		url:"/projectManage/requirement/editRequirement",
		dataType:"json",
		type:"post",
		data:{
			 "id":requirementId,
			 "requirementName" : requirementName,
			 "requirementOverview" : requirementOverview,
			 "requirementStatus" : requirementStatus,
			 "requirementSource" :requirementSource,
			 "requirementType" : requirementType,			 
			 "applyUserId" :applyUserId,
			 "applyDeptId" : applyDeptId,
			 "developmentManageUserId" : developmentManageUserId,		 
			 "developmentDeptId" : developmentDeptId,
			 "requirementManageUserId":requirementManageUserId,			 
			 "requirementAcceptanceUserId" : requirementAcceptanceUserId,
			 "requirementPriority" : requirementPriority,		 
			 "startDate" : planOnlineDate,
			 "endDate":actualOnlineDate,
			 "importantRequirementStatus":importantRequirementStatus,
			 "changeCount":changeCount,			 
			 "workload1":workload,
			 "acceptanceDescription":acceptanceDescription,
			 "requirementIds":requirementIds,
			 "time":closeTime, 
			 "parentId":parentId,
			 "reqSysList":JSON.stringify(List),
			 "fieldTemplate":JSON.stringify(fieldTemplate),
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){						
				uploadFile(data.reqId);		
				if(requirementStatus=="REQ_CANCELED"){
					updateFeature(data.reqId)
				}
				/*deleteAtt(delFile,requirementStatus);*/				
			}else if (data.status == "2"){
				layer.alert('保存失败！！！', {icon: 2});
			}else if( data.status  == "003" ){
				layer.alert('系统和模块重复！！！', {icon: 2});
			}
		}
	});
}

function updateFeature(reqId){
    $.ajax({
        type:"POST",
        url:'/zuul/devManage/devtask/cancelStatus',
        dataType:"json",
        data:{
        	"requirementId":reqId	        	
    	},
    	cache:false,
    	async:false,
    	success:function (data) {            
            if (data.status == 2){
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });
            } else if(data.status == 1){
                layer.alert('操作成功！', {
                    icon: 1,
                    title: "提示信息"
                });                 
            }
        },    	
    });
}
//查看页面查看附件
function showFile(){
	var requirementId =  $("#itcdReqId").val();
	if(requirementId== ''){
		layer.alert('该需求下没有附件！', {icon: 0});
		return;
	}
	layer.open({
	  type: 2,
	  area: ['700px', '450px'],
	  fixed: false, //不固定
	  maxmin: true,
	  btnAlign: 'c',
	  title:"相关附件",
	  content: reqAttUrl+"?reqId="+requirementId+"&reqtaskId="+requirementId+"&taskreqFlag=requirement"
	});

}
//表单校验
$(function () {
    $('#newform').bootstrapValidator({
    	excluded:[":disabled"],
　　　　	message: 'This value is not valid',//通用的验证失败消息
        　	feedbackIcons: {//根据验证结果显示的各种图标
            　　　　　　　　valid: 'glyphicon glyphicon-ok',
            　　　　　　　　invalid: 'glyphicon glyphicon-remove',
            　　　　　　　　validating: 'glyphicon glyphicon-refresh'
        　　	},
        fields: {
        	requirementName: {
                validators: {
                    notEmpty: {
                        message: '名称不能为空'
                    },
                    stringLength: {
                        min:5,
                        max: 300,
                        message: '长度必须在5-300之间'
                    }
                }
            },
            requirementOverview: {
                validators: {
                	 notEmpty: {
                         message: '描述不能为空'
                     },
                     stringLength: {
                         min: 0,
                         max: 200,
                         message: '长度不能大于200'
                     }
                }
            },     
            requirementStatus:{
            	validators : {
            		notEmpty: {
                        message: '需求状态不能为空'
                    },
                    callback : {
                    	message: '请选择一个需求状态',
                    	callback: function (value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }

                        }
                    }
                }
            },
            requirementSource:{
            	validators: {
            		notEmpty: {
                        message: '需求来源不能为空'
                    },
                    callback : {
                    	message: '请选择一个需求来源',
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
            	}
            },
            requirementType:{
            	validators: {
            		notEmpty: {
                        message: '需求类型不能为空'
                    },
                    callback : {
                    	message: '请选择一个需求类型',
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
            	}
            },
            applyUserName:{
            	 trigger:"change",
            	 validators: {
             		notEmpty: {
                        message: '提出人不能为空'
                	},
             	}
            },
            applyDeptId:{
            	validators: {
            		notEmpty: {
                        message: '归属部门不能为空'
                    },
                    callback : {
                    	message: '请选择一个归属部门',
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
            	}
            },
            requirementPriority:{
            	validators: {
            		notEmpty: {
                        message: '需求优先级不能为空'
                    },
                    callback : {
                    	message: '请选择一个需求优先级',
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }              	 	
            	}
            },
            changeCount:{
        		validators: {
                    numeric: {
                    	message: '只能输入数字'
   	                },
   	                regexp: {
   	                	regexp: /^\d+(\.\d{0,2})?$/,
   	                	message: '请输入大于0的正数'
   	                }
           		}
            },
            workload:{
        		validators: {
                    numeric: {
                    	message: '只能输入数字'
   	                },
   	                regexp: {
   	                	regexp: /^\d+(\.\d{0,2})?$/,
   	                	message: '请输入大于0的正数'
   	                }
           		}
            }
                        
        }
    });
    $('#editform').bootstrapValidator({
    	excluded:[":disabled"],
　　　　	message: 'This value is not valid',//通用的验证失败消息
        　	feedbackIcons: {//根据验证结果显示的各种图标
            　　　　　　　　valid: 'glyphicon glyphicon-ok',
            　　　　　　　　invalid: 'glyphicon glyphicon-remove',
            　　　　　　　　validating: 'glyphicon glyphicon-refresh'
        　　	},
        fields: {
        	editRequirementName: {
                validators: {
                    notEmpty: {
                        message: '名称不能为空'
                    },
                    stringLength: {
                        min:5,
                        max: 300,
                        message: '长度必须在5-300之间'
                    }
                }
            },
            editRequirementOverview: {
                validators: {
                	 notEmpty: {
                         message: '描述不能为空'
                     },
                     stringLength: {
                         min: 0,
                         max: 200,
                         message: '长度不能大于200'
                     }
                }
            },     
            editRequirementStatus:{
            	validators : {
            		notEmpty: {
                        message: '需求状态不能为空'
                    },
                    callback : {
                    	message: '请选择一个需求状态',
                    	callback: function (value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }

                        }
                    }
                }
            },
            editRequirementSource:{
            	validators: {
            		notEmpty: {
                        message: '需求来源不能为空'
                    },
                    callback : {
                    	message: '请选择一个需求来源',
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
            	}
            },
            editRequirementType:{
            	validators: {
            		notEmpty: {
                        message: '需求类型不能为空'
                    },
                    callback : {
                    	message: '请选择一个需求类型',
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
            	}
            },
            editApplyUserName:{
            	 trigger:"change",
            	 validators: {
             		notEmpty: {
                        message: '提出人不能为空'
                	},
             	}
            },
            editApplyDeptId:{
            	validators: {
            		notEmpty: {
                        message: '归属部门不能为空'
                    },
                    callback : {
                    	message: '请选择一个归属部门',
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
            	}
            },
            editRequirementPriority:{
            	validators: {
            		notEmpty: {
                        message: '需求优先级不能为空'
                    },
                    callback : {
                    	message: '请选择一个需求优先级',
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }              	 	
            	}
            },            
            editChangeCount:{
        		validators: {
                    numeric: {
                    	message: '只能输入数字'
   	                },
   	                regexp: {
   	                	regexp: /^\d+(\.\d{0,2})?$/,
   	                	message: '请输入大于0的正数'
   	                }
           		}
            },
            editWorkload:{
        		validators: {
                    numeric: {
                    	message: '只能输入数字'
   	                },
   	                regexp: {
   	                	regexp: /^\d+(\.\d{0,2})?$/,
   	                	message: '请输入大于0的正数'
   	                }
           		}
            }
        }
    });    
});

function appendDataType( thisData , id ,status){  
	var obj=$('<div class="def_col_18"></div>');  
	switch ( thisData.type ) {
		case "int":
			obj.attr( "dataType","int");
			var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_28"><input maxlength="'+ thisData.maxLength +'" fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="int" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_28"><input maxlength="'+ thisData.maxLength +'" fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="int" value="'+ thisData.valueName +'" /></div>' );
			}
			labelContent.children( " input " ).bind("keyup",function (){
				this.value=this.value.replace(/\D/gi,"");
			})
			obj.append( labelName , labelContent ); 
			break;
		case "float": 
			obj.attr( "dataType","float")
			var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
			if( status== "new" ){	
				var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="number" class="form-control" placeholder="请输入" name="float" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="number" class="form-control" placeholder="请输入" name="float" value="'+ thisData.valueName +'" /></div>' );
			}
			obj.append( labelName , labelContent );				
			break;
		case "varchar":
			obj.attr( "dataType","varchar") 
			var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_28"><input  maxlength="'+ thisData.maxLength +'"  fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="varchar" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_28"><input  maxlength="'+ thisData.maxLength +'"  fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="varchar" value="'+ thisData.valueName +'" /></div>' );
			}
			obj.append( labelName , labelContent );		
			break;
		case "data":
			obj.attr( "dataType","data")  
			var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="new'+ thisData.fieldName +'" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="edit'+ thisData.fieldName +'" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="'+ thisData.valueName +'" /></div>' ); 	
			}
			obj.append( labelName , labelContent );
			break;
		case "timestamp": 
			obj.attr( "dataType","timestamp")  
			var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="new'+ thisData.fieldName +'" type="text" readonly id="new_'+ thisData.fieldName +'" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="edit'+ thisData.fieldName +'" type="text" readonly id="new_'+ thisData.fieldName +'" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="'+ thisData.valueName +'" /></div>' );
			}  
			obj.append( labelName , labelContent );
			break;
		case "enum": 
			obj.attr( "dataType","enum")
			var select=$( '<select class="selectpicker" requireded="'+ thisData.required +'" fName="'+ thisData.fieldName +'"></select>' )
			var options=JSON.parse(  thisData.enums  );
			select.append( '<option value=""  fName="'+ thisData.fieldName +'">请选择</option>'  );
			for( var i=0;i<options.length;i++ ){
				if( options[i].enumStatus == 1 ){ 
					if(  status== "edit" && options[i].value == thisData.valueName  ){
						select.append( '<option value="'+options[i].value+'" selected >'+options[i].value+'</option>'  );
					}else{
						select.append( '<option value="'+options[i].value+'">'+options[i].value+'</option>'  );
					}
					 
				}
			}  
			var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
			var labelContent=$( '<div class="def_col_28"></div>' );
			labelContent.append( select  );
			obj.append( labelName , labelContent );
			break;
		default:
			break;
	} 
	$( "#"+id ).append( obj );
	if( obj.attr( "dataType") == "data" ){ 
		 laydate.render({
	        elem: "#"+ obj.find( "input" )[0].id, 
	        trigger: 'click',
	        done:function(value,date,endDate){
	            $( "#"+ obj.find( "input" )[0].id ).next().css("display","block");
	        }
	    });
	}else if( obj.attr( "dataType") == "timestamp" ){ 
		 laydate.render({
		        elem: "#"+ obj.find( "input" )[0].id, 
		        trigger: 'click',
		        type: 'datetime', 
		        format: 'yyyy-MM-dd HH:mm:ss', 
		        done:function(value,date,endDate){
		            $( "#"+ obj.find( "input" )[0].id ).next().css("display","block");
		        }
		    });
		} 
	$(".selectpicker").selectpicker('refresh');
}
function getFieldData( id ){
	var data = {"field":[]};
	for( var i=0;i< $( "#"+ id +" > div" ).length;i++ ){ 
    	//int float varchar data timestamp enum
    	var obj={};
    	var type = $( "#"+ id +" > div" ).eq( i ).attr( "dataType" )
    	if( type == "int" || type == "float" || type == "varchar" || type == "data" || type == "timestamp" ){ 
    		obj.fieldName=$( "#"+ id +" > div" ).eq( i ).find( "input" ).attr( "fName" ); 
    		obj.required=$( "#"+ id +" > div" ).eq( i ).find( "input" ).attr( "requireded" );  
    		obj.valueName=$( "#"+ id +" > div" ).eq( i ).find( "input" ).val(); 
    		obj.labelName=$( "#"+ id +" > div" ).eq( i ).children("div").eq( 0 ).text(); 
    	}else if(  type == "enum"  ){ 
    		obj.fieldName=$( "#"+ id +" > div" ).eq( i ).find( "select" ).attr( "fName" );
    		obj.required=$( "#"+ id +" > div" ).eq( i ).find( "select" ).attr( "requireded" );  
    		obj.valueName=$( "#"+ id +" > div" ).eq( i ).find( "select" ).val();
    		obj.labelName=$( "#"+ id +" > div" ).eq( i ).children("div").eq( 0 ).text(); 
    	}
    	data.field.push( obj );
    } 
	return data;
}
function showField( data ){ 
	$( "#checkEditField" ).empty();
	if(data != null && data != "undefined" && data.length > 0 ){
        for( var i=0;i<data.length;i++ ){
            var aLabel='<div class="form-group col-md-4">  <label class="control-label fontWeihgt">'+ data[i].label +'：</label>' +
            '<label class="control-label font_left">'+ data[i].valueName +'</label></div>';
            $( "#checkEditField" ).append( aLabel);
        }
    }
}

//【添加子系统】
function addSysInput( idName ){ 
	var str = '<div class="rowdiv">'+
		'<div class="form-group def_col_12">'+
	    '<label class="def_col_12 control-label font_right">系统：</label>'+
	    '<div class="def_col_24">'+
	        '<input type="hidden" class="systemIds"/>'+
	        '<input type="text" class="form-control pointStyle readonly new_systemName" readonly placeholder="请输入" onclick="showSysModal(this)" name="systems"/>'+
	    '</div></div>'+
	    '<div class="form-group def_col_12">'+
	    '<label class="def_col_12 control-label font_right">模块：</label>'+
	    '<div class="def_col_24">'+ 
	    '<input type="text" class="form-control pointStyle readonly new_moduleName" readonly placeholder="请输入" onclick="showMenu(this)"  name="module"/>'+
	    '</div></div>'+
	    '<div class="form-group def_col_8">'+
	    '<label class="def_col_18 control-label font_right">功能点数：</label>'+
	    '<div class="def_col_18">'+
	    '<input type="number" class="form-control pointStyle new_functionCount" placeholder="请输入" name="module" value="0" />'+
	    '</div></div>'+     
	    '<div class="form-group def_col_4 del"><a onclick="delSysModal(this)">删除</a>'+
	    '</div></div>'; 
	$("#" + idName ).append(str);
	$(".selectpicker").selectpicker('refresh');
}

//详情显示子系统
function showList( data ){
	console.log( data );
	if( data == null ){
		return ;
	}
	for( var i=0;i<data.length;i++ ){
		var str = '<div class="rowdiv">'+
		'<div class="form-group def_col_12">'+
	    '<label class="def_col_12 control-label font_right">系统：</label>'+
	    '<div class="def_col_24">'+
	        '<input type="hidden" class="systemIds" value="'+ isValueNull( data[i].systemId ) +'" />'+
	        '<input type="text" class="form-control pointStyle readonly new_systemName" readonly placeholder="请输入" onclick="showSysModal(this)" name="systems"  syscode="'+ isValueNull(data[i].systemCode) +'"  value="'+ data[i].systemName +'"/>'+
	    '</div></div>'+
	    '<div class="form-group def_col_12">'+
	    '<label class="def_col_12 control-label font_right">模块：</label>'+
	    '<div class="def_col_24">'+ 
	    '<input type="text" class="form-control pointStyle readonly new_moduleName" readonly placeholder="请输入" onclick="showMenu(this)" name="module" value="'+ isValueNull( data[i].assetSystemName ) +'" assetsystemtreeid="'+ isValueNull(data[i].assetSystemTreeId) +'" />'+
	    '</div></div>'+
	    '<div class="form-group def_col_8">'+
	    '<label class="def_col_18 control-label font_right">功能点数：</label>'+
	    '<div class="def_col_18">'+
	    '<input type="number" class="form-control pointStyle new_functionCount" placeholder="请输入" name="module" value="'+ isValueNull(data[i].functionCount) +'" />'+
	    '</div></div>'+     
	    '<div class="form-group def_col_4 del"><a onclick="delSysModal(this)">删除</a>'+
	    '</div></div>';
		
		$("#editSysInput").append(str);
	} 
	 
	$(".selectpicker").selectpicker('refresh');
}
function delSysModal( self ){
	$( self ).parent().parent().remove();
}

//添加子系统，下一层
function addSysModal( data ){ 
	if( data == null ){
		return ;
	}
	for( var i=0;i<data.length;i++ ){
		var str = '<div class="rowdiv"><div class="form-group col-md-4">'+
				  '<label class="control-label fontWeihgt">系统:</label>'+
				  '<label class="control-label font_left"><span>'+isValueNull( data[i].systemName )+'</span></label></div>'+
				  '<div class="form-group col-md-4">'+
				  '<label class="control-label fontWeihgt">模块:</label>'+
				  '<label class="control-label font_left"><span>'+isValueNull( data[i].assetSystemName )+'</span></label></div>'+
				  '<div class="form-group col-md-4">'+
				  '<label class="control-label fontWeihgt">功能点数:</label>'+
				  '<label class="control-label font_left"><span>'+isValueNull( data[i].functionCount )+'</span></label></div></div>';
		$( "#checkSysModal" ).append( str );
	}
}

//同步IT全流程附件
function synItcdAttr(){
    var idArr=$('#list2').jqGrid('getGridParam','selarrrow')
    if(idArr.length>0 ){
    	var ids=[];
    	for(var i=0;i<idArr.length;i++){
            var obj = $("#list2").jqGrid('getRowData', idArr[i]);
            ids.push(obj.id)
		}
        $.ajax({
            url:"/projectManage/requirement/synItcdAtta",
            contentType : "application/json" ,
            //dataType:"json",
            type:"post",
            data:JSON.stringify(ids),
            success:function(data){
                if(data.status == "success"){
                    layer.alert('同步成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                }else{
                    layer.alert('同步失败！', {
                        icon: 2,
                        title: "提示信息"
                    });
                }
            },
            error:function(){
                $("#loading").css('display','none');
                layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
            }
        })

    }else {
        layer.alert("请选择至少一个需求", { icon: 3});
    }
}
//导入
function insertRequirement(){
	$("#upfile").val("");
	$("#importPerson").modal("show");
}
function upload(){
	var formData = new FormData();
	formData.append("file", document.getElementById("upfile").files[0]);
	if (document.getElementById("upfile").files[0] == undefined) {
		layer.alert("请选择文件", { icon: 0 });
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/projectManage/requirement/importItcdAtta",
		type: "POST",
		data: formData, 
		contentType: false, 
		processData: false,
		success: function (data) {
			if (data.status == 2) {
				layer.alert("导入失败，原因:" + data.errorMessage, { icon: 2 });
				$("#importPerson").modal("hide");
			} else {
				layer.alert("导入成功", { icon: 1 });
				$("#importPerson").modal("hide");
				searchInfo();
			}
			$("#loading").css('display', 'none');
		},
		error: function () {
			layer.alert("上传失败！", { icon: 2 });
			$("#loading").css('display', 'none');
		}
	});
}

