/**
 * Created by ztt
 */
var _icon_word ="../static/images/devtask/word.png";
var _icon_excel ="../static/images/devtask/excel.png";
var _icon_text = "../static/images/devtask/text.png";
var _icon_pdf = "../static/images/devtask/pdf.png";
var _icon_img = "../static/images/devtask/img.png";
var _icon_other = "../static/images/devtask/other.png";
var _files = [];
var _editfiles = [];
var _checkfiles = [];
var modalType = '';
var workStatus ='';
var system = {
    systemId:"",
    reqFeatureIds:"",
    devTaskList:[],
    codeReviewStatus:2,
    codeReViewManage:"",
    codeReviewSubmitBtn:true, // 实施完成状态是否允许提交
    codeReviewDevTaskListMsg:"" // 需要实施完成的工作任务
};
var is_check_Collect_flag = true;//是否继续收藏

/**
 * @description 收藏功能配置
 */
var collection2_obj = {
		  menuUrl : '/devManageui/devtask/toDevTask',
		  filterName : '',
		  search : [],
		  isCollect : false
		};
var windowId = '';
var ids = [];
var devtaskStatusList ='';//状态
var excuteUserName='';
var deployStatusData =[];//部署状态
var reqFeaturePriorityList = [];//优先级 

var editDevtaskStatus = '';
$(function(){
//	pageInit();
	default_list();
	addCheckBox();
	accept_add_File();
	select_project();
	//所有的Input标签，在输入值后出现清空的按钮
	buttonClear();
    
	 banEnterSearch();
	uploadFileList();
	getDeployStatus();
   // devtaskStatusList = $("#devTaskStatus").find("option");
    devtaskStatusList = getReqFeatureStatus();
		reqFeaturePriorityList = $("#reqFeaturePriority").find("option");
		if(!$('#system_id').val()){
//			$("#devManPost").val(uid);
//			$("#devManPostName").val(username).change();
		}else{     //其他页面跳转赋值
			$('#systemId').val($('#system_id').val());
			$('#systemNameReq').val($('#system_Name').val());
		}
    //搜索展开和收起
    downOrUpButton();
    dateComponent();	
    if(devtaskStatusList!=null){
    	$.each(devtaskStatusList,function(index,value){
    		$("#devTaskStatus").append('<option value="'+value.valueCode+'">'+value.valueName+'</option>')
    	})
    }
		findByStatus();
	//时间控件 配置参数信息
    $("#planVersionName").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    });
    
    $("#startWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
    $("#endWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }); 
    
    $("#handlestartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
    $("#handleendWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }); 

   $("#eastartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
    $("#eaendWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }); 

    $("#epstartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
    
    $("#ependWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    });
     
    $("#sWorkStart").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left",
    }).on('hide',function(e) {
    	                $('#splitForm').data('bootstrapValidator')
    	                    .updateStatus('startWork', 'NOT_VALIDATED',null)
    	                    .validateField('startWork');
    	            });
   
    $("#sWorkEndStart").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('hide',function(e) {
    	                $('#splitForm').data('bootstrapValidator')
    	                    .updateStatus('endWork', 'NOT_VALIDATED',null)
    	                    .validateField('endWork');
    	            }); 
    
    
    
    $("#newsystemName").change(function(){
    	var systemId = $("#newsystemId").val();
    	if(systemId!=null&& systemId!=''){
	    	$.ajax({
	    		url:"/devManage/systemVersion/getSystemVersionByCon",
	    		dataType:"json",
	    		type:"post",
	    		data:{
	    			systemId:systemId,
	    			status:1
	    		},
	    		success:function(data){
	    			$("#newsystemVersionBranch").empty();
	    			$("#newsystemVersionBranch").append("<option value=''>请选择</option>");
	    			$("#repairVersionBranch").empty();
	    			$("#repairVersionBranch").append("<option value=''>请选择</option>");
	    			var systemVersionStr='';
	    			var repairVersionStr='';
	  				$.each(data.rows,function(index,value){
						systemVersionStr += '<option value="'+value.id+','+value.groupFlag+'">'+value.version+'--> '+toStr(value.groupFlag)+'</option>';
						repairVersionStr += '<option value="'+value.id+'">'+value.version+'</option>';
	  				});
	  				$("#newsystemVersionBranch").append( systemVersionStr );
	  				$("#repairVersionBranch").append( repairVersionStr );
	    			
	    			$('.selectpicker').selectpicker('refresh');
	    			// console.log("项目模型："+$("#ndevelopmentMode").val())
						if($("#ndevelopmentMode").val() != 2){//关联的系统是敏态 可以选择选择冲刺
							$.ajax({
								url:"/devManage/devtask/getSprintBySystemId",
								data:{
									systemId:$("#newsystemId").val(),
									projectId:$("#newProject_listId").val()
								},
								type:"post",
								dataType:"json",
								success:function(data){
									$("#newSprintId").empty();
									$("#newSprintId").append("<option value=''>请选择</option>");
									if(data.sprintInfos.length>0 && $("#newProject_listId").val()){
										$("#newsprintDiv").show();
										for(var i = 0;i<data.sprintInfos.length;i++){
											$("#newSprintId").append('<option value="'+data.sprintInfos[i].id+'">'+data.sprintInfos[i].sprintName+'</option>')
										}
									}
											$('.selectpicker').selectpicker('refresh'); 
								}
							})
							//故事点
							$("#newstoryPointDiv").show();
							$("#editdevTaskStatus").empty();
							$("#editdevTaskStatus").append("<option value=''>请选择</option>");
						}else{
							$("#newsprintDiv").hide();
							$("#newSprintId").empty();
							$("#newstoryPointDiv").hide();
						}
	    		}
	    	})
    	}
    });

    // 批量修改 中 任务状态
    $("#updatedevTaskStatus").change(function(){
        // 需要评审
        if(system.codeReviewStatus == 1 && $("#updatedevTaskStatus").val() == "03"){
            $("#loading").css('display','block');
            $.ajax({
                url:"/devManage/devtask/selectDevTaskByReqFeatureIds",
                dataType:"json",
                type:"post",
                traditional: true,
                data:{
                    "ids":system.reqFeatureIds
                },
                success:function(data){
                    $("#loading").css('display','none');
                    if(data.devTasks.length > 0){
                        system.codeReviewSubmitBtn = false;
                        system.codeReviewDevTaskListMsg = "<div>";
                        for (var i = 0; i < data.devTasks.length; i++) {
                            system.codeReviewDevTaskListMsg += "<div><a class='a_style' href='#' onclick='parent.parent.toPageAndValue(\""+
                                isValueNull(data.codeReViewManage.menuButtonName)+"\","
                                +data.codeReViewManage.id+",\""
                                +data.codeReViewManage.url+"?devTaskCode="+data.devTasks[i].devTaskCode+"\")'>"+data.devTasks[i].devTaskCode+"</a></div>";
                        }
                        system.codeReviewDevTaskListMsg += '未通过代码审核，请到“代码审核管理”菜单审核！</div>';
                        layer.alert(system.codeReviewDevTaskListMsg, {
                            title: "提示信息",
                            closeBtn: 0,
                            btn:["取消"]
                        });
                    } else {
                        system.codeReviewSubmitBtn = true;
                        system.codeReviewDevTaskListMsg = "";
                    }
                },
                error:errorFunMsg
            })
        } else {
            system.codeReviewSubmitBtn = true;
        }
    });
    
    $("#newrequirementName").change(function(){
    	var systemId = $("#newsystemId").val();
    	var requirementId= $("#newrequirementId").val();
    	if(systemId!=""&&requirementId!=""){
    		getFeatureBySystemAndRequirement(systemId,requirementId);
    	}
    	
    });
    $("#newsystemName").change(function(){
    	var systemId = $("#newsystemId").val();
    	var requirementId= $("#newrequirementId").val();
    	if(systemId!=""&&requirementId!=""){
    		getFeatureBySystemAndRequirement(systemId,requirementId);
    	}
    	
    });
    other();
    collect_handle();
    getCollection2(); 
});

/**
 * @description 表格数据加载
 * @param id
 */
function pageInit(){
	var devTaskStatus = $("#devTaskStatus").val();
	if(devTaskStatus!=null && devTaskStatus!=''){
		devTaskStatus = devTaskStatus.join(",");
	}
	var reqFeaturePriority = $("#reqFeaturePriority").val();
    if(reqFeaturePriority!=null && reqFeaturePriority!=''){
   	 reqFeaturePriority = reqFeaturePriority.join(",");
    }
	var createStartDate = '';
	var createEndDate = '';
	var time=$("#dev_createDate").val();
	if(time != undefined){
		var timeArr=time.split(' - ');
		createStartDate=timeArr[0];
		createEndDate=timeArr[1];
	}
    jQuery("#list2").jqGrid({
        url:'/devManage/devtask/getAllDevTask2',
        datatype: 'json', 
        mtype:"post",
        height: 'auto',
        multiselect : true, 
        width: $(".content-table").width()*0.999,
        autoScroll: false, //固定列
        shrinkToFit: false,//固定列
        postData:{
//            "requirementFeatureStatus" : devTaskStatus, 
//						manageUserIds:uid+",",
//						"systemIds" :  $("#systemId").val(),
						
			        	 "featureCode" :$.trim( $("#devTaskCode").val()),
			             "featureName" : $.trim( $("#featureName").val()),
			             "requirementFeatureStatus" : devTaskStatus,
			             "requirementCode" : $.trim( $("#requirement_Code").val()),
			             "requirementName" : $.trim( $("#requirementName").val()),
			             "manageUserName" : $("#devManPostName").val(),
			             "executeUserName":$("#execteUserName").val(),
			             "systemIds" :  $("#systemId").val(),
			             "windowPdate" :  $("#planVersionName").val(),
			             "systemVersionName":$.trim( $("#sel_systemVersionName").val()),
			             "createType": $("#createTye").val(),
			             "sprints":$("#sprintIds").val(),
			             "startDate":createStartDate,
			             "endDate":createEndDate,
			             "reqFeaturePrioritys":reqFeaturePriority,
			           
       },
        colNames:['id','systemCodeReviewStatus','systemId','developmentMode','任务编号', '任务名称','状态','子系统','关联需求'/*,'所属处室'*/,'管理岗','执行人','投产窗口','冲刺','优先级','部署状态','预估剩余工作量(人天)','操作','投产时间'],
        colModel:[ 
            {name:'id',width:30,index:'id',hidden:true},
            {name:'systemCodeReviewStatus',index:'systemCodeReviewStatus',hidden:true},
            {name:'systemId',index:'systemId',hidden:true},
            {name:'developmentMode',index:'developmentMode',hidden:true ,frozen: true },
            {name:'featureCode',index:'featureCode',width:110, /* searchoptions:{sopt:['cn']},*/ formatter : function(value, grid, rows) {
            	if(value==undefined){
            		value='';
            	}
               	 return "<a class='a_style' onclick='showDevTask( "+rows.id+",\" "+rows.featureCode+"\",\" "+rows.featureName+"\")'>"+value+"</a>";
                }},
            {name:'featureName',index:'featureName',width: 300,/*searchoptions:{sopt:['cn']},*/formatter : function(value, grid, rows) {
	           	 var str = '';
	           	 if(rows.createType == 1){
	           		str += "<sup class='micSup'>自建任务</sup>";
	           	 }
	           	 return "<a class='a_style' onclick='showDevTask("+rows.id+",\" "+rows.featureCode+"\",\" "+rows.featureName+"\")'>"+rows.featureName+"</a>"+str;
            }},
            {name:'requirementFeatureStatus',width:120,index:'requirementFeatureStatus', stype : 'select',searchoptions:{
                value:function(){
                    var arr = {};
                    for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
                       arr[devtaskStatusList[i].valueCode] = devtaskStatusList[i].valueName;
                    }
                    return arr;
                },sopt:['cn']},
                formatter : function(value, grid, rows) {
                	for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
                       if(devtaskStatusList[i].valueCode == rows.requirementFeatureStatus){
                    	   value=  devtaskStatusList[i].valueName;
                    	   break;
                       }
                     }
	        		if(rows.requirementFeatureStatus == '01'){
	        			return "<span class='doing1'>"+value+"</span>";
	        		}else if(rows.requirementFeatureStatus == '02'){
	        			return "<span class='doing2'>"+value+"</span>";
	        		}else if(rows.requirementFeatureStatus == '03'){
	        			return '<span class="doing3">'+value+'</span>';
	        		}else if(rows.requirementFeatureStatus == '00'){
	        			return '<span class="doing4">'+value+'</span>';
	        		}else if(rows.requirementFeatureStatus == '04'){
	        			return '<span class="doing5">'+value+'</span>';
	        		}else if(rows.requirementFeatureStatus == '05'){
	        			return '<span class="doing6">'+value+'</span>';
	        		}else if(rows.requirementFeatureStatus == '06'){
	        			return '<span class="doing7">'+value+'</span>';
	        		}else{
	        			return value;
	        		}
                	
        	  }
            },
            {name:'systemName',index:'systemName',/*searchoptions:{sopt:['cn']}*/}, 
            {name:'requirementCode',index:'requirementCode',/*searchoptions:{sopt:['cn']}*/},
//            {name:'deptName',index:'deptName',/*searchoptions:{sopt:['cn']}*/},
            {name:'manageUserName',width:100,index:'manageUserName',/*searchoptions:{sopt:['cn']}*/},
            {name:'executeUserName',width:100,index:'executeUserName',/*searchoptions:{sopt:['cn']}*/},
            {name:'windowName',width:130,index:'windowName',/*searchoptions:{sopt:['cn']},*/formatter:function(value, grid, rows){
            	if(rows.requirementId!=null /*&& rows.commissioningWindowId!=null && rows.windowName!=null*/){
            		var str = '';
            		var diffWindowData = getDiffWindow(rows);
            		if(diffWindowData!=null && diffWindowData.length>0){
            			var info = ' <span>与该任务属于同需求下的开发任务的投产窗口不一致：</span><br/>  ';
            			for(var i=0;i<diffWindowData.length;i++){
                    		info +=' <span>'+ diffWindowData[i].featureCode+' | '+toStr(diffWindowData[i].windowName==null?'无':diffWindowData[i].windowName)+' 投产 | '+diffWindowData[i].featureName+'</span><br/>  ';
                    	}
            			return '<span title="'+info+'" data-html="true" class="autotip" data-toggle="tooltip" ><i class="fa fa-exclamation-circle fa-lg"></i>'+toStr(value)+'</span>';
                	}else{
                		return toStr(value) ;
                	}
            	}else{
            		return toStr(value) ;
            	}
            }},{name:'sprintName',index:'sprintName'},
            {name:"requirementFeaturePriority",width:80,index:'requirementFeaturePriority',formatter:function(value,grid,rows){
        		for (var i = 0,len = reqFeaturePriorityList.length;i < len;i++) {
                    if(reqFeaturePriorityList[i].value == rows.requirementFeaturePriority){
                 	   value=  reqFeaturePriorityList[i].innerHTML;
                 	   break;
                    }
                  }
        		 return toStr(value);
            	}
            },
			{name:'deployStatus',index:'部署状态',
				formatter : function(value, grid,rows) {
					var deployName = getDeployByReqFeatureId(rows.id);
					return deployName;
				}
            },
            {name:'estimateRemainWorkload',width: 80,index:'estimateRemainWorkload'},
            {
                name:'opt',
                index:'操作',
                width: 80,
                fixed:true,
                align:"center",
                sortable:false,
                resize:false,
               /* search: false,*/
                formatter : function(value, grid, rows, state) {
                	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                	var str = "";
                	var str2= '';
                	var str3='';
                	var str4='';
                	//组装操作权限
                	if(rows.createType == 1){//自建任务
                			if(rows.requirementFeatureStatus != "00"){
                				if(mergeEditPermission ==true){
	                				str2= ' <a class="a_style" style="cursor:pointer" onclick="findSyn('+ row + ')">合并</a>';
                				}
                			}
                	}
                	if(editPermission==true){
            			str='<a class="a_style" style="cursor:pointer" onclick="edit('+ row + ')">编辑</a>  ';
            		}
                	//未实施完成并且未取消
                	if(rows.requirementFeatureStatus != "03" && rows.requirementFeatureStatus != "00"){
                		var transferStr = '';
                		if(transferPermission == true){
                			transferStr = '<a class="a_style" style="cursor:pointer" onclick="transfer('+ row +')">转派</a> ';
                		}
                		var splitStr = '';
                		if(splitPermission == true){
                			splitStr = '<a class="a_style" style="cursor:pointer" onclick="split('+ row +')">拆分</a> ';
                		}
                		var handleStr = '';
                		if(handleEditPermission ==true){
                			handleStr = '<a class="a_style" style="cursor:pointer" onclick="handle('+ row + ')">处理</a> ';
                		}
                        str3 = transferStr + splitStr + handleStr;
                	}
                	if(rows.checkStatus == 2){ //待验收
                		str4 = '<a class="a_style" href="#" onclick="CheckHandle(\'' + rows.id +'\')">用户验收</a>';
                	}
                    var opt_str = str+str3+str2+str4;
                    if(opt_str){
                    	return `
							<li role="presentation" class="dropdown">
								<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
								<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
							</li>
                    	`;
                    }else{
                    	return '';
                    }
                }
            },
            {name:'windowDate',index:'windowDate',hidden:true}
        ],
        rowNum:10,
        rowTotal: 200,
        rowList : [5,10,20,30], 
        rownumWidth: 40,
        pager: '#pager2',
        sortable:true,   //是否可排序
        sortorder: 'desc',
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true, //是否要显示总记录数
        beforeRequest:function(){
        	$("#loading").css('display','block');
        },
        gridComplete: function(){
			$("[data-toggle='tooltip']").tooltip();
        },
        loadComplete :function(){
        	$("#colGroup").find('input').each(function(idx,val){
          		if ($(val).is(':checked')) {
          			$(collection_obj.table_list).setGridParam().hideCol($(val).attr('value'));
          			$(collection_obj.table_list).setGridWidth($('.wode').width());
          		}
          	})
        	$("#loading").css('display','none');
        },
        loadError:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 2});
        }
    }).trigger("reloadGrid");
     $("#list2").jqGrid('setFrozenColumns');//设置冻结列生效
 /*   $("#list2").jqGrid('filterToolbar',{searchOperators : true});*/
    
}

/**
 * @description 表格数据加载
 * @param id
 */
function getDeployByReqFeatureId(id){
    var deployName ='';
    $.ajax({
        type:"POST",
        url:"/devManage/devtask/findDeployByReqFeatureId",
        dataType:"json",
        data:{
            "featureId":id
        },
        async:false,
        success:function(data1){
            deployName = data1["deployName"] || '';
        }
    });
    return deployName;
}

function findByStatus(){
	var ids = $("#requirementFeatureStatus").val();
	var arr = ids.split(",");
	$("#devTaskStatus option").each( function (i, n) {
	    for (var j = 0,len = arr.length; j < len ; j++){
	        if (n.value == arr[j]) {
	            n.selected = true;
	        }
	    }
	});
	}

//获取同需求下其他开发任务的投产窗口，在设置投产窗口时进行提示：与该任务属于同需求下的开发任务的投产窗口不一致
function getDiffWindow(rows){
	var diffbrother;
	var windowId = rows.commissioningWindowId;
	if(rows.commissioningWindowId == null){
		windowId = 0;
	}
	$.ajax({
		url:"/devManage/devtask/getBrotherDiffWindow",
		dataType:"json",
		type:"post",
		async:false,
		data:{
			id:rows.id,
			requirementId:rows.requirementId,
			commissioningWindowId:windowId
		},
		success:function(data){
			diffbrother = data['brothers'];
		},
		error:function(){
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
	return diffbrother;
}

//合并开发任务
function findSyn(row){
	var id = row.id;
	var requirementId = row.requirementId;
	var systemId = row.systemId;
	$.ajax({
		url:'/devManage/devtask/findSynDevTask',
		dataType:'json',
		type:'post',
		data:{
			id:id,
			requirementId:requirementId,
			systemId:systemId
		},
		success : function(data){
			if(data.length>0){
				if(data.length == 1){
					var str = "您确定要合并"+data[0].featureCode+" | "+data[0].featureName+"吗？";
					layer.confirm(str, {
						  btn: ['确定', '取消'] ,
						  icon:3
						}, function(index, layero){
						$.ajax({
							url:'/devManage/devtask/merge',
							dataType:'json',
							type:'post',
							data:{
								id:id,
								synId:data[0].id,
								taskId:data[0].taskId,
								featureCode:data[0].featureCode
							},
							success : function(data){
								if(data.status == "success"){
									layer.alert('合并成功！', {icon: 1});
									pageInit();
								
								}else if(data.status == "2"){
									layer.alert('合并失败！！！', {icon: 2});
								}
							},
							error:function(){
					            $("#loading").css('display','none');
					            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
					        }
						})
					})
				}else{
					$("#reqFId").val('');
					$("#reqFId").val(id);
					$("#mergeDiv").empty();
					var str ="";
					for(var i=0;i<data.length;i++){
						str+="<div><input name='synDT' value="+data[i].taskId+" featureCode="+data[i].featureCode+" type='radio' id="+data[i].id+"  />"+data[i].featureCode+" | "+data[i].featureName+"</div><br/>";
					}
					$("#mergeDiv").append(str);
					
					$("#mergeDevTask").modal("show");
				}
			}else{
				layer.alert('没有需要合并的同步开发任务！！！', {icon: 0});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
}

//表头合并按钮
function mergeCommit(){
	var id = $("#reqFId").val();
	var taskId = $("#mergeDiv input[name='synDT']:checked").val();  
	var selectId = $("input[name='synDT']").filter(':checked').attr( 'id' ); 
	var featureCode = $("input[name='synDT']").filter(':checked').attr( 'featureCode' ); 
	if(selectId == undefined){
		layer.alert('请选择一个同步任务！！！', {icon: 0});
	}
	$("#loading").css('display','block');
	$.ajax({
		url:'/devManage/devtask/merge',
		dataType:'json',
		type:'post',
		data:{
			id:id,
			synId:selectId,
			taskId:taskId,
			featureCode:featureCode,
		},
		success : function(data){
			 $("#loading").css('display','none');
			if(data.status == "success"){
				layer.alert('合并成功！', {icon: 1});
				pageInit();
				$("#mergeDevTask").modal("hide");
			}else if(data.status == "2"){
				layer.alert('合并失败！！！', {icon: 2});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
}

var is_first_search = false;

//查询信息
function searchInfo(){
	if(!is_first_search){
		$('#list_body2').remove();
		$('#list_body').show();
		pageInit();
		is_first_search = true;
		return;
	}
	var devTaskStatus = $("#devTaskStatus").val();
	if(devTaskStatus!=null && devTaskStatus!=''){
		devTaskStatus = devTaskStatus.join(",");
	}
	 var reqFeaturePriority = $("#reqFeaturePriority").val();
     if(reqFeaturePriority!=null && reqFeaturePriority!=''){
    	 reqFeaturePriority = reqFeaturePriority.join(",");
     }
	var planVersion = $("#planVersion").val();
	let createStartDate = '';
	let createEndDate = '';
	var time=$("#dev_createDate").val();
	if(time != undefined){
		var timeArr=time.split(' - ');
		createStartDate=timeArr[0];
		createEndDate=timeArr[1];
	}
	$("#loading").css('display','block');
	$("#list2").jqGrid('setGridParam',{ 
         postData:{
        	 "featureCode" :$.trim( $("#devTaskCode").val()),
             "featureName" : $.trim( $("#featureName").val()),
             "requirementFeatureStatus" : devTaskStatus,
             "requirementCode" : $.trim( $("#requirement_Code").val()),
             "requirementName" : $.trim( $("#requirementName").val()),
             "manageUserName" : $("#devManPostName").val(),
             "executeUserName":$("#execteUserName").val(),
             "systemIds" :  $("#systemId").val(),
             "windowPdate" :  $("#planVersionName").val(),
             "systemVersionName":$.trim( $("#sel_systemVersionName").val()),
             "createType": $("#createTye").val(),
             "sprints":$("#sprintIds").val(),
             "startDate":createStartDate,
             "endDate":createEndDate,
             "reqFeaturePrioritys":reqFeaturePriority,
         },
        page:1
    }); 
	$("#list2").jqGrid('setGridParam',{
		url:"/devManage/devtask/getAllDevTask2",
		loadComplete :function(){
        	$("#colGroup").find('input').each(function(idx,val){
          		if ($(val).is(':checked')) {
          			$(collection_obj.table_list).setGridParam().hideCol($(val).attr('value'));
          			$(collection_obj.table_list).setGridWidth($('.wode').width());
          		}
          	})
			$("#loading").css('display','none');		
		}	
	}).trigger("reloadGrid"); //重新载入
	
}


//查看
function showDevTask(id,code,name) {
	layer.open({
      type: 2,
      title:  '<i class="focusInfo" title="关注"></i><span>'+ code + ' | ' + name + '</span>',
      shadeClose: true,
      shade: false, 
      move: false,
      area: ['100%', '100%'], 
      tipsMore: true,
      anim: 2,
      content:  '/devManageui/devtask/toInfo?rowId='+id,
      btn: '关闭' ,
      btnAlign: 'c', //按钮居中
      yes: function(){
          layer.closeAll();
      }
    });
}
//提交备注
function saveRemark(){
	var remark = $("#remarkDetail").val();
	var id = $("#checkReqFeatureId").val();
	if($("#remarkDetail").val()==''&& $("#checkfiles").val()==''){
		layer.alert('备注内容和附件不能同时为空！！！', {icon: 0});
		return;
	}
	/*var _fileStr = [];
	for(var i=0;i<_checkfiles.length;i++){
		_fileStr.push(_checkfiles[i].fileNameOld);
	}
	var s = _fileStr.join(",")+",";
	for(var i=0;i<_fileStr.length;i++){
		if(s.replace(_fileStr[i]+",","").indexOf(_fileStr[i]+",")>-1){
			layer.alert(_fileStr[i]+"文件重复！！！", {icon: 0});
			return ;
		}
	}*/
	$.ajax({
		url:"/devManage/devtask/addRemark",
		dataType:"json",
		type:"post",
		data:{
			id:id,
			requirementFeatureRemark:remark,
			attachFiles :$("#checkfiles").val() 
		},
		success : function(data){
			if(data.status == "success"){
				layer.alert('保存成功！', {icon: 1});
				showDevTask(id);
				 _checkfiles = [];
				$("#checkfiles").val('');
			}else if(data.status =="2"){
				layer.alert('保存失败！！！', {icon: 2});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
}
//转派提交
function transferCommit(){
	if($("#transferId").val()==''){
		layer.alert("转派人不能为空！！！",{icon:0});
		return;
	}
	if($("#transferId").val()== $("#tEcecuteUserId").val()){
		layer.alert("不可以转派给同一个人！！！",{icon:0});
		return;
	}
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/transfer",
		dataType:"json",
		type:"post",
		data:{
			id:$("#transferDevtaskId").val(),
			executeUserId : $("#transferId").val(),
			transferRemark : $("#transferRemark").val()
		},
		success : function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){
				layer.alert('转派成功！', {icon: 1});
				pageInit();
				$("#transferDevTask").modal("hide");
			}else if(data.status =="2"){
				layer.alert('转派失败！！！', {icon: 2});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
}
//拆分提交
function splitCommit(){
	var reqFeatureId = $("#splitDevtaskId").val();
	var devTaskName= $("#sWorkSummary").val();
	var devTaskOverview = $("#sWorkOverView").val();
	var planStartDate = $("#sWorkStart").val();
	var planEndDate = $("#sWorkEndStart").val();
	var devUserId = $("#sWorkDivid").val();
	var commissioningWindowId = $("#splitCommitWindowId").val();
	var requirementFeatureStatus = $("#splitStatus").val();
	var planWorkload = $("#sWorkPlanWorkload").val();
	
	$('#splitForm').data('bootstrapValidator').validate();
	if(!$('#splitForm').data('bootstrapValidator').isValid()){
		return;
	}
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/splitDevTask",
		type:"post",
		dataType:"json",
		data:{
			"id":reqFeatureId,
			"devTaskName":devTaskName,
			"devTaskOverview":devTaskOverview,
			"planWorkload":planWorkload,
			"devUserId":devUserId,
			"startDate":planStartDate,
			"endDate":planEndDate,
			"commissioningWindowId":commissioningWindowId,
			"requirementFeatureStatus":requirementFeatureStatus,
			"sprintId":$("#ssprintId").val()
			
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){
				layer.alert('拆分成功！', {icon: 1});
				pageInit();
				$("#splitDevTask").modal("hide");
			}else if(data.status == "2"){
				layer.alert('拆分失败！！！', {icon: 2});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})	
}

//同任务
function sameReq(){
	var summary = $("#splitSummary").text();
	var workname = '';
	if((summary.indexOf('|'))==-1){
		workname = summary.slice(0);
	}else{
		workname = summary.slice(summary.indexOf('|')+2);//正常是+1  +2是因为|后还有个空格
	}
	$("#sWorkSummary").val(workname).change();
	$("#sWorkOverView").val($("#sOverView").text()).change();
}

//转派 弹框
function transfer(row){
	var id = row.id;
	var systemId = row.systemId;
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/getOneDevTask3",
		dataType:"json",
		type:"post",
		data:{
			"id":id,
			"systemId":systemId
		},
		success:function(data){
			$("#loading").css('display','none');
			$("#tRequirementDiv").hide();
			$("#tDefectDiv").hide();
			$("#tRequstNumberDiv").hide();
			
			$("#transferRemark").val('');
			$("#transferSystemId").val('');
			$("#transferSystemId").val(systemId);
			$("#transferId").val('');
			$("#tEcecuteUserId").val('');
			excuteUserName = '';
			excuteUserName = data.executeUserId;
			$("#transferUserName").val('');
			$("#transferDevtaskId").val(data.id);
			$("#tSystemId").text('');
			$("#tDefect").text('');
			$("#tDeptId").text('');
			$("#tRequirementId").text('');
			$("#tRequstNumber").text('');
		    $("#transferCommitWindowId").val(data.commissioningWindowId);
		    $("#transferStatus").val(data.requirementFeatureStatus);
			$("#tSummary").text(toStr(data.featureCode) +" | "+ toStr(data.featureName));
			$("#tOverView").text(data.featureOverview);
			var statusName = '';
			for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
                if(devtaskStatusList[i].valueCode == data.requirementFeatureStatus){
                	statusName=  devtaskStatusList[i].valueName;
             	   break;
                }
              }
			$("#tDevtaskStatus").text(statusName);
			$("#tManageUser").text(data.manageUserName);
			$("#tEcecuteUserId").val(data.executeUserId);
			$("#tEcecuteUser").text(data.executeUserName);
			$("#tSystemId").text(data.systemName);
			
			if(data.requirementFeatureSource!=undefined){
				if(data.requirementFeatureSource==1){
					$("#tRequirementDiv").show();
					$("#tDefectDiv").hide();
					$("#tRequstNumberDiv").hide();
					$("#tRequirementId").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
				}else if(data.requirementFeatureSource==2){
					$("#tRequirementDiv").hide();
					$("#tDefectDiv").hide();
					$("#tRequstNumberDiv").show();
					$("#tRequstNumber").text(data.questionNumber);
				}else if(data.requirementFeatureSource==3){
					$("#tRequirementDiv").hide();
					$("#tDefectDiv").show();
					$("#tRequstNumberDiv").hide();
					var dftName='';
					for(var i=0;i<data.defectInfos.length;i++){
						if(data.defectInfos[i].requirementFeatureId == id){
							dftName+= data.defectInfos[i].defectCode+",";
						}
					}
					$("#tDefect").text(dftName.substring(0,dftName.length-1));
				}
			}
			
			$("#tDeptId").text(data.deptName);
			
			$("#tWorkStart").val(data.planStartDate);
			$("#tWorkEndStart").val(data.planEndDate);
			$("#tWorkPlanWorkload").val(data.planWorkload);
			
			var a = '';
			if(data.temporaryStatus == 1){
				a = "是";
			}
			if(data.temporaryStatus == 2){
				a="否";
			}
			$("#tTemporaryTask").text(a);
			
            $('.selectpicker').selectpicker('refresh'); 
          
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
	$("#transferDevTask").modal("show");
	
}
//拆分弹框
function split(row){  
	layer.open({
      type: 2, 
      title: "拆分  " + toStr(row.featureCode) +" | "+ toStr(row.featureName),
      shadeClose: true,
      shade: false, 
      area: ['100%', '100%'],  
      tipsMore: true, 
      move: false,
      anim: 2,
      content: '/devManageui/devtask/toSplit?rowId='+row.id+',systemId='+row.systemId,
      btn: ['确定','取消'] ,
      btnAlign: 'c', //按钮居中
      yes: function( index , layero){  
    	  var body = layer.getChildFrame('body', index );
    	  var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
          iframeWin.splitCommit();   
      }, 
      btn2:function(){ 
    	  layer.closeAll();
      }
    });
}
//查看人员未完成工作量 当前任务所属系统下所有开发任务下所有工作任务  统计 姓名 工单数量 预估工作量，拆分时显示
function checkNoCompleteWorkload(){
	var systemId = $("#splitSystemId").val();
	if(systemId == null || systemId == ''){
		layer.alert("该任务还未关联系统，不可以查看！",{icon:1});
	}
	$.ajax({
		url:"/devManage/worktask/countWorkloadBysystemId",
		dataType:"json",
		type:"post",
		data:{
			systemId:systemId
		},
		success:function(data){
			countWorkloadTable(data.countDatas);
			$(".countWorkloadDiv").css( "display","block" );
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
}

//展示剩余工作量
function countWorkloadTable(data){
	$("#countWorkloads").bootstrapTable('destroy');
	$("#countWorkloads").bootstrapTable({
		data : data,
		method : "post",
	    cache: false,
		columns : [{
			field : "userName",
			title : "开发人员姓名",
			align : 'center'
		},{
			field : "workNum",
			title : "工单数量",
			align : 'center'
		},{
			field : "planWorkLoads",
			title : "预计总工作量",
			align : 'center',
		}]
	});
}

//编辑弹出框
function edit(row){
    getProjectGroup();
	layer.open({
      type: 2, 
      title: '编辑：'+row.featureCode+" | "+row.featureName,
      shadeClose: true,
      shade: false, 
      move: false,
      area: ['94%', '90%'],
      offset: "4%",
      shade: 0.3,	
      tipsMore: true, 
      anim: 2,
      content:  '/devManageui/devtask/toEdit?rowId='+row.id,
      btn: ['确定','取消'] ,
      btnAlign: 'c', //按钮居中
      yes: function( index , layero){  
    	  var body = layer.getChildFrame('body', index );
    	  var iframeWin = window[layero.find('iframe')[0]['name']];//得到iframe页的窗口对象，执行iframe页的方法：
          iframeWin.editDevTask(); 
      }, 
      btn2:function(){ 
    	  layer.closeAll();
      }
    });
}
//部门模糊搜索
$(function () {
	var loseInputMillsecs = 500;
	var clocker = null;
	//编辑页面部门模糊搜索
	$("#editDeptName").on('input propertychange', function(){
		innerKeydown();		
	})
	
	function innerKeydown(){
		if(null == clocker){
			clocker = setTimeout(loadData,loseInputMillsecs);
			//console.info(clocker);
		}else{		//连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData,loseInputMillsecs);
		}
	}
	
	function loadData(){
		if($("#editDeptName").val().trim() == '') {
	        $('.selectBox').hide()
	        return
	    }
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: {'deptName': $("#editDeptName").val().trim()},
			dataType: "json",
			success: function (data) {
				var depts= data.depts;
	        	if (depts.length>0) {
	        		var itemStr = ''
	        		for (var i = 0; i < depts.length; i++) {
	        			itemStr += '<li class="selectItem" id="'+depts[i].id+'">'+depts[i].deptName+'</li>'
	        		}
	        		$('.selectUl').html(itemStr)
	        		$('.selectBox').show()
	        	} else {
	        		$('.selectBox').hide()
	        	}
			},
			error:function(){
	            $("#loading").css('display','none');
	            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	        }
		})
		clocker = null;
	}		
	
	$("#selectBox").on('click', '.selectItem', function(){
		$("#editDeptName").val($(this).html());
		$("#editdeptId").val($(this).attr("id"));
		$("#selectBox").hide()
	})
	
	$(document).click(function(){
		$("#selectBox").hide()
	})
	//新增页面模糊搜索
	$("#newDeptName").on('input propertychange', function(){
		innerKeydown2();		
	})
	
	function innerKeydown2(){
		if(null == clocker){
			clocker = setTimeout(loadData2,loseInputMillsecs);
			//console.info(clocker);
		}else{		//连续击键，重新开始计时
			clearTimeout(clocker);
			clocker = setTimeout(loadData2,loseInputMillsecs);
		}
	}
	
	function loadData2(){
		if($("#newDeptName").val().trim() == '') {
	        $('.selectBox').hide()
	        return;
	    }
		$.ajax({
			type: "post",
			url: "/system/dept/getDeptByDeptName",
			data: {'deptName': $("#newDeptName").val().trim()},
			dataType: "json",
			success: function (data) {
				var depts= data.depts;
	        	if (depts.length>0) {
	        		var itemStr = ''
	        		for (var i = 0; i < depts.length; i++) {
	        			itemStr += '<li class="selectItem" id="'+depts[i].id+'">'+depts[i].deptName+'</li>'
	        		}
	        		$('.selectUl').html(itemStr)
	        		$('.selectBox').show()
	        	} else {
	        		$('.selectBox').hide()
	        	}
			},
			error:function(){
	            $("#loading").css('display','none');
	            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	        }
		})
		clocker = null;
	}		
	
	$("#newselectBox").on('click', '.selectItem', function(){
		$("#newDeptName").val($(this).html());
		$("#newdeptId").val($(this).attr("id"));
		$("#newselectBox").hide()
	})
	
	$(document).click(function(){
		$("#newselectBox").hide()
	})
	
});
//处理提交
function handleDevTask(){
	if($('#handlecheckStatus').val() == 2){
		layer.msg('该开发任务需要先完成“用户验收”方可“完成实施”！', {icon: 0});
		return;
	}
	$('#handleForm').data('bootstrapValidator').validate();
	if(!$('#handleForm').data('bootstrapValidator').isValid()){
		$('#handleDevTask').on('hide.bs.modal', function () {
			     $('#handleForm').bootstrapValidator('resetForm');
			});
		return ;
	}
    if(system.codeReviewStatus == 1){
        $("#loading").css('display','block');
        $.ajax({
            url:"/devManage/devtask/selectDevTaskByReqFeatureIds",
            dataType:"json",
            type:"post",
            data:{
                "ids":system.reqFeatureIds
            },
            success:function(data){
                $("#loading").css('display','none');
                if(data.devTasks.length > 0){
                    system.codeReviewDevTaskListMsg = "<div>";
                    for (var i = 0; i < data.devTasks.length; i++) {
                        system.codeReviewDevTaskListMsg += "<div><a class='a_style' href='#' onclick='parent.parent.toPageAndValue(\""+
                            isValueNull(data.codeReViewManage.menuButtonName)+"\","
                            +data.codeReViewManage.id+",\""
                            +data.codeReViewManage.url+"?devTaskCode="+data.devTasks[i].devTaskCode+"\")'>"+data.devTasks[i].devTaskCode+"</a></div>";
                    }
                    system.codeReviewDevTaskListMsg += '未通过代码审核，请到“代码审核管理”菜单审核！</div>';
                    layer.alert(system.codeReviewDevTaskListMsg, {
                        title: "提示信息",
                        closeBtn: 0,
                        btn:["取消"]
                    });
                    return false;
                } else {
                    handleDevTaskCode();
                }
            },
            error:errorFunMsg
        })
    } else {
        handleDevTaskCode();
    }
}

function handleDevTaskCode(){
    var id = $("#reqFeatureId").val();
    var actualStartDate = $("#handlestartWork").val();
    var actualEndDate = $("#handleendWork").val();
    var actualWorkload = $("#handleactWorkload").val();

    if(workStatus=="false"){
        layer.confirm('该任务下尚有未实施完成的工作任务，是否要确定处理完成？', {
            btn: ['确定', '取消'] ,
            icon:3
        }, function(index, layero){//确定
            $("#loading").css('display','block');
            $.ajax({
                url:"/devManage/devtask/handleDevTask",
                type:"post",
                dataType:"json",
                data:{
                    "id":id,
                    "startDate":actualStartDate,
                    "endDate":actualEndDate,
                    "actualWorkload":actualWorkload,
                    "handleRemark":$("#handleRemark").val()
                },
                success: function(data){
                    workStatus="";
                    $("#loading").css('display','none');
                    if(data.status == "success"){
                        layer.alert('处理成功！', {icon: 1});
                        pageInit();
                        $("#handleDevTask").modal("hide");
                    }else if (data.status == "2"){
                        layer.alert('处理失败！！！', {icon: 2});
                    }
                },
                error:function(){
                    $("#loading").css('display','none');
                    layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
                }
            });
        }, function(index){//取消
            $('#handleDevTask').on('hide.bs.modal', function () {
                $('#handleForm').bootstrapValidator('resetForm');
            });
        });
    }else{
        $.ajax({
            url:"/devManage/devtask/handleDevTask",
            type:"post",
            dataType:"json",
            data:{
                "id":id,
                "startDate":actualStartDate,
                "endDate":actualEndDate,
                "actualWorkload":actualWorkload,
                "handleRemark":$("#handleRemark").val()
            },
            success: function(data){
                $("#loading").css('display','none');
                if(data.status == "success"){
                    layer.alert('处理成功！', {icon: 1});
                    pageInit();
                    $("#handleDevTask").modal("hide");
                }else if (data.status == "2"){
                    layer.alert('处理失败！！！', {icon: 2});
                }
            },
            error:function(){
                $("#loading").css('display','none');
                layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
            }
        });
    }
}

//处理弹出框
function handle(row){
	$('#handleDevTask').on('hide.bs.modal', function () {
		     $('#handleForm').bootstrapValidator('resetForm');
		});
	var id = row.id;
	$("#loading").css('display','block');
	$.ajax({
		url:"/devManage/devtask/getOneDevTask",
		type:"post",
		dataType:"json",
		data:{
			"id":id
		},
		success: function(data) {
			$("#loading").css('display','none');
            system.codeReviewStatus = "";
			system.codeReviewStatus = data.systemCodeReviewStatus;
			$("#handleConnectDiv").empty();
			$("#handleBrotheDiv").empty();
			$("#handleRequirementDiv").hide();
			$("#handleDefectDiv").hide();
			$("#handleRequstNumberDiv").hide();
			$("#handledeptName").text('');
			$("#handlepreStartDate").text('');
			$("#handlepreEndDate").text('');
			$("#handlepreWorkload").text('');
			$("#handlehandSug").text('');
			$("#handlesystemName").text('');
			$("#handlerequirement").text('');
			$("#handleDefect").text('');
			$("#handleRequstNumber").text('');
			$("#handlestartWork").val("");
			$("#handleactWorkload").val("");
			$("#handleendWork").val("");
			$("#handleRemark").val("");
			$("#handlestartWork").val("");
			$("#handleendWork").val("");
			$("#handleactWorkload").val("");
			
			$("#handlecheckStatus").val(data.checkStatus);
			
			$("#reqFeatureId").val(data.id);
			$("#handledevTaskTitle").text( toStr(data.featureCode)+" | "+data.featureName );
			$("#handledevTaskOverview").text(data.featureOverview);
			var statusName = '';
			for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
                if(devtaskStatusList[i].valueCode == data.requirementFeatureStatus){
                	statusName=  devtaskStatusList[i].valueName;
             	   break;
                }
              }
			$("#handledevTaskStatus").text(statusName);
			$("#handledevManPost").text(data.manageUserName);
			$("#handleexecutor").text(data.executeUserName);
			$("#handlesystemName").text(data.systemName);
			if(data.requirementFeatureSource!=undefined){
				if(data.requirementFeatureSource==1){
					$("#handleRequirementDiv").show();
					$("#handleDefectDiv").hide();
					$("#handleRequstNumberDiv").hide();
					$("#handlerequirement").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
				}else if(data.requirementFeatureSource==2){
					$("#handleRequirementDiv").hide();
					$("#handleDefectDiv").hide();
					$("#handleRequstNumberDiv").show();
					$("#handleRequstNumber").text(data.questionNumber);
				}else if(data.requirementFeatureSource==3){
					$("#handleRequirementDiv").hide();
					$("#handleDefectDiv").show();
					$("#handleRequstNumberDiv").hide();
					var dftName='';
					for(var i=0;i<data.defectInfos.length;i++){
						if(data.defectInfos[i].requirementFeatureId == id){
							dftName+= data.defectInfos[i].defectCode+",";
						}
					}
					$("#handleDefect").text(dftName.substring(0,dftName.length-1));
				}
			}
			
			$("#handledeptName").text(data.deptName);
			
			$("#handlestartWork").val(data.actualStartDate==undefined ? data.minActStartDate : data.actualStartDate);
			$("#handleendWork").val(data.actualEndDate == undefined ? data.maxActEndDate : data.actualEndDate);
			$("#handleactWorkload").val(data.actualWorkload == undefined ? data.sumActWorkLoad : data.actualWorkload);
			
			var status = "";
			if(data.temporaryStatus =="1"){
				status = "是";
			}else if(data.temporaryStatus =="2"){
				status = "否";
			}
			$("#handletemporaryTask").text(status);//1是2否
			$("#handlepreStartDate").text(data.planStartDate);
			$("#handlepreEndDate").text(data.planEndDate);
			$("#handlepreWorkload").text(data.planWorkload);
			
			$("#handlehandSug").text(data.handleSuggestion);
			//下属工作任务
			workStatus ="";
            system.reqFeatureIds = "";
            system.codeReViewManage = "";
			if(data.list.length>0){
                system.reqFeatureIds = row.id;
                system.codeReViewManage = data.codeReViewManage;
				for(var i=0;i<data.list.length;i++){
					if(data.list[i].devtaskStatus!=3 && data.list[i].devtaskStatus!=0){
						workStatus = "false";
						break;
					}
				}
				for(var i=0;i<data.list.length;i++){
					$("#handleConnectDiv").append( '<div class="rowdiv"><div class="form-group col-md-2"><label class=" control-label font_left" ><a class="a_style" onclick="getSee('+data.list[i].id+')">'+toStr(data.list[i].devTaskCode)+'</a></label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+data.list[i].devTaskName+'</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+data.list[i].devtaskStatusName+'</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+toStr(data.list[i].devUsername)+'</label></div><div class="form-group col-md-3"><label class=" control-label font_left" >'+toStr(data.list[i].actualStartDate)+'~'+toStr(data.list[i].actualEndDate)+'</label></div><div class="form-group col-md-1"><label class=" control-label font_left" >'+toStr(data.list[i].actualWorkload)+'人天</label></div></div>');
				}
			}else{
				workStatus="true";
			}
			// 相关开发任务
			if(data.brother!=undefined){
				for(var i=0;i<data.brother.length;i++){
					$("#handleBrotheDiv").append( '<div class="rowdiv"><div class="form-group col-md-2"><label class=" control-label font_left" ><a class="a_style" onclick="showDevTask('+data.brother[i].id+')">'+toStr(data.brother[i].featureCode)+'</a></label></div><div class="form-group col-md-3"><label class=" control-label font_left" >'+data.brother[i].featureName+'</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+data.brother[i].statusName+'</label></div><div class="form-group col-md-2"><label class=" control-label font_left" >'+toStr(data.brother[i].executeUserName)+'</label></div><div class="form-group col-md-3"><label class="col-sm-6 control-label  fontWeihgt">预排期：</label><label class="col-sm-6 control-label font_left fontWeihgt" >'+toStr(data.brother[i].windowName)+'</label></div></div>');
				}
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
    $("#handleDevTask").modal("show");
	
}
//新增提交
function addCommit(){
	console.log("devtask_add!!!");
	var executeProjectGroupId=getCheckedZtree();
	var assetSystemTreeId=getCheckedModalZtree();
	var featureName = $("#nfeatureName").val();
	var featureOverview = $("#nfeatureOverview").val();
	var manageUserId = $("#newdevManageUser").val();
	var executeUserId = $("#newexecuteUser").val();
	var systemId = $("#newsystemId").val();
	var deptId = $("#newdeptId").val();
	var planStartDate = $("#startWork").val();
	var planEndDate = $("#endWork").val();
	var estimateWorkload = $("#nestimateWorkload").val();
	var commissioningWindowId = $("#newcommitWindowId").val();
//	var requirementFeatureSource = $("#newrequirementFeatiureSource").val(); //新增  默认值
	var requirementFeatureSource = 1; //新增  默认值1
	//var questionNumber = $("#newquestion").val();
//    var newdefectId;
    var requirementId= $("#newrequirementId").val();
//	if(requirementFeatureSource==1){
//        requirementId= $("#newrequirementId").val();
//	}else if(requirementFeatureSource == 2){
//        newdefectId = $("#newSynDefectId").val();
//	}else if(requirementFeatureSource == 3){
//        newdefectId = $("#newdefectId").val();
//	}
	$('#newform').data('bootstrapValidator').validate();
	if(!$('#newform').data('bootstrapValidator').isValid()){
		return;
	}
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
	
	if(!system.codeReviewSubmitBtn){
		
	}
	var versionArr = $("#newsystemVersionBranch").val().split(",");
	var repairSystemVersion= $("#repairVersionBranch").val();
	 
	var systemVersionId = versionArr[0];
	var systemScmBranch = versionArr[1];  
	
	/*var _fileStr = [];
	for(var i=0;i<_files.length;i++){
		_fileStr.push(_files[i].fileNameOld);
	}
	var s = _fileStr.join(",")+",";
	for(var i=0;i<_fileStr.length;i++){
		if(s.replace(_fileStr[i]+",","").indexOf(_fileStr[i]+",")>-1){
			layer.alert(_fileStr[i]+"文件重复！！！", {icon: 0});
			return ;
		}
	}*/
	$("#loading").css('display','block');	
	$.ajax({
		url:"/devManage/devtask/addDevTask",
		dataType:"json",
		type:"post",
		data:{
			 "featureName" : featureName,
			 "featureOverview" : featureOverview,
			 "manageUserId" : manageUserId,
			 "executeUserId" :executeUserId,
			 "systemId" : systemId,
			 "requirementId" : requirementId,
			 "deptId" :deptId,
			 "startDate" : planStartDate,
			 "endDate" : planEndDate,
			 "estimateWorkload" : estimateWorkload,
			 "commissioningWindowId":commissioningWindowId,
			 "requirementFeatureSource":requirementFeatureSource,
			 //"questionNumber":questionNumber,
//			 "defectIds":newdefectId,
			 "systemVersionId":systemVersionId,
			 "repairSystemVersionId":repairSystemVersion,
			 "systemScmBranch":systemScmBranch,
			 "requirementFeaturePriority":$("#newreqFeaturePriority").val(), 			 
			 "sprintId":$("#newSprintId").val(),
			 "storyPoint":$("#newstoryPoint").val(),
			 "attachFiles" :$("#files").val(),
			"executeProjectGroupId":executeProjectGroupId,
			"assetSystemTreeId":assetSystemTreeId,
			"fieldTemplate":JSON.stringify(fieldTemplate),
			"projectPlanId" : $("#newProjectPlanId").val(),
            "projectId":$("#newProject_listId").val()
		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status == "success"){
				layer.alert('保存成功！', {icon: 1});
				pageInit();
  				$("#newDevTask").modal("hide"); 
			}else if (data.status == "2"){
				layer.alert('保存失败！！！', {icon: 2});
			}else if(data.status =="repeat"){
				layer.alert('该任务名称已存在！！！', {icon: 0});
			}
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
}
//新增 清空
function newClearForm(){
	$("#newProject_listId").val("");
	$("#newProject_list").val("");
	$("#modalInput").val("");

	$("#newdefectId").val("");
	$("#newdefectCode").val("");
	$("#newSynDefectId").val("");
	$("#newSynDefectCode").val("");
	$( "#projectInput" ).val("");
	$.fn.zTree.init($("#projectOwn"), setting, "");
	
	$.fn.zTree.init($("#modalOwn"), modalSetting, "");

	$('#newDevTask').on('hide.bs.modal', function () {
     $('#newform').bootstrapValidator('resetForm');
	});
	$("#newsprintDiv").hide();
	$("#newstoryPointDiv").hide(); 
	  
	$("#nfeatureName").val("");
	$("#nfeatureOverview").val("");
	
	$("#startWork").val(""); 
	
	$("#endWork").val(""); 
	
	$("#nestimateWorkload").val("");
	$("#newquestionNumber").val("");
	$("#newsystemId").val('');
	$("#newsystemName").val('');
	$("#newdevManageUser").val('');
	$("#newdevManageUserName").val('');
	$("#newFileTable").empty();
	$("#newdevManageUser").val('');
	$("#newdevManageUser").val('');
	$("#newexecuteUser").val('');
	$("#newexecuteUserName").val('');
	$("#files").val('');
	$("#newdeptId").val('');
	$("#newDeptName").val('');
	$("#newrequirementId").val('');
	$("#newrequirementName").val('');
	$("#newcommitWindowId").empty();
	$("#newcommitWindowId").append('<option value="">请选择</option>');
	$("#newreqFeaturePriority").empty();
	$("#newreqFeaturePriority").append('<option value="">请选择</option>');
	$("#newrequirementFeatiureSource").empty();
	$("#newrequirementFeatiureSource").append('<option value="">请选择</option>');
	$("#newsystemVersionBranch").empty();
	$("#repairVersionBranch").empty();
	$("#newsystemVersionBranch").append('<option value="">请选择</option>');
	$("#newSprintId").empty();
	$("#newSprintId").append('<option value="">请选择</option>');
	$("#newstoryPoint").val('');
	$("#canEditField").empty();
	$("#newProjectPlanId").val('');
	$("#newProjectPlanName").val('');
	if(reqFeaturePriorityList!=null){
		for (var i = 0,len = reqFeaturePriorityList.length;i < len;i++) {
			$("#newreqFeaturePriority").append('<option value="'+reqFeaturePriorityList[i].value+'"> '+reqFeaturePriorityList[i].innerHTML+'</option>')
		}
	} 
}
//新增弹框
function newDevTask_btn(){
	$('.is_new_project').show();
	$('.is_oac_project').show();
	$("#loading").css('display','block'); 
	//初始化项目组数据 
	newClearForm();  
	$.ajax({
		url:"/devManage/devtask/toAddData",
		dataType:"json",
		type:"post",
		success:function(data){ 
			if(data.cmmitWindows!=null && data.cmmitWindows!=undefined){
				for(var c=0;c<data.cmmitWindows.length;c++){
					$("#newcommitWindowId").append(' <option value="'+data.cmmitWindows[c].id+'">'+data.cmmitWindows[c].windowName+'</option>');
				} 
			}
			if(data.dics!=null && data.dics!=undefined){
				for(var i=0;i<data.dics.length;i++){
					$("#newrequirementFeatiureSource").append('<option value="'+data.dics[i].valueCode+'">'+data.dics[i].valueName+'</option>');
				}
			}
			if(!data.fields) return;
        	for( var i=0;i<data.fields.length;i++ ){  
        		if( data.fields[i].status == 1 ){
        			appendDataType( data.fields[i] , 'canEditField' ,'new'); 
        		}  
        	}  
			$('.selectpicker').selectpicker('refresh');
			$("#loading").css('display','none');
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
	modalType = 'new';
	//$('#newsystem_div').hide();
	if($('#z_project_Id').val()){
		$('#newProject_listId').val($('#z_project_Id').val());
		$('#newProject_list').val($('#z_project_Name').val());
		//$('#newsystem_div').show();
	}
	if($('#system_id').val().length == 1){
		$('#newsystemId').val($('#system_id').val()).attr( "systemCode",$('#system_Code').val());
		$('#newsystemName').val($('#system_Name').val()).change();
		//$('#newsystem_div').show();
	}
	$("#newDevTask").modal("show");
	_files=[];
	
}

function appendDataType( thisData , id ,status){  
	var obj=$('<div class="form-group def_col_12"></div>');  
	switch ( thisData.type ) {
		case "int":
			obj.attr( "dataType","int");
			var labelName=$( '<label class="def_col_12 control-label">'+ thisData.label +'：</label>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_24"><input maxlength="'+ thisData.maxLength +'" fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="int" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input maxlength="'+ thisData.maxLength +'" fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="int" value="'+ thisData.valueName +'" /></div>' );
			}
			labelContent.children( " input " ).bind("keyup",function (){
				this.value=this.value.replace(/\D/gi,"");
			})
			obj.append( labelName , labelContent ); 
			break;
		case "float": 
			obj.attr( "dataType","float")
			var labelName=$( '<label class="def_col_12 control-label">'+ thisData.label +'：</label>' );
			if( status== "new" ){	
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="number" class="form-control" placeholder="请输入" name="float" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="number" class="form-control" placeholder="请输入" name="float" value="'+ thisData.valueName +'" /></div>' );
			}
			obj.append( labelName , labelContent );				
			break;
		case "varchar":
			obj.attr( "dataType","varchar") 
			var labelName=$( '<label class="def_col_12 control-label">'+ thisData.label +'：</label>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_24"><input  maxlength="'+ thisData.maxLength +'"  fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="varchar" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input  maxlength="'+ thisData.maxLength +'"  fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="varchar" value="'+ thisData.valueName +'" /></div>' );
			}
			obj.append( labelName , labelContent );		
			break;
		case "data":
			obj.attr( "dataType","data")  
			var labelName=$( '<label class="def_col_12 control-label">'+ thisData.label +'：</label>');
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="new'+ thisData.fieldName +'" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="edit'+ thisData.fieldName +'" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="'+ thisData.valueName +'" /></div>' ); 	
			}
			obj.append( labelName , labelContent );
			break;
		case "timestamp": 
			obj.attr( "dataType","timestamp")  
			var labelName=$( '<label class="def_col_12 control-label">'+ thisData.label +'：</label>' );
			if( status== "new" ){
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="new'+ thisData.fieldName +'" type="text" readonly id="new_'+ thisData.fieldName +'" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="'+ thisData.defaultValue +'" /></div>' );
			}else{
				var labelContent=$( '<div class="def_col_24"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="edit'+ thisData.fieldName +'" type="text" readonly id="new_'+ thisData.fieldName +'" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="'+ thisData.valueName +'" /></div>' );
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
			var labelName=$( '<label class="def_col_12 control-label">'+ thisData.label +'：</label>' );
			var labelContent=$( '<div class="def_col_24"></div>' );
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

//重置
function clearSearch() {
    $('#dev_createDate').val("");
    $('#devTaskCode').val("");
    $("#featureName").val("");
    $('#devManPost').val("");
    $('#devManPostName').val('');
    $('#execteUserName').val('');
    $('#execteUserId').val('');
   /* $("#devManPost").val(uid);
    $("#devManPostName").val(username);*/
    $("#systemNameReq").val('');
    $("#systemId").val('');
    $("#devTaskStatus").selectpicker('val', '');
    $("#reqFeaturePriority").selectpicker('val', '');
    $("#planVersion").val('');
    $("#planVersionName").val('');
    $("#requirementId").val('');
    $("#requirementName").val('');
    $("#systemId").val('');
    $("#sprintNames").val('');
    $("#sprintIds").val('');
    $("#sel_systemVersionName").val('');
    
    $("#requirement_Code").val('')
    $("#requirementName").val('')
   
    $(".collection2").children("span").removeClass("fa-heart").addClass("fa-heart-o");
    $("#createTye").selectpicker('val', '');
    $(".color1 .btn_clear").css("display","none");
}
//清空表格内容
/*function clearContent( that ){
    $(that).parent().children('input').val("").change();
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
//文件上传，并列表展示
function uploadFileList(){ 
	//列表展示
	$("#uploadFile").change(function(){ 
		var files = this.files;
		var formFile = new FormData();
		
		/*if(!fileAcceptBrowser()){
			for(var i=0,len=files.length;i<len;i++){
				var file_type = files[i].name.split(".")[1];
				if(file_type!="doc"&&file_type!="docx"&&file_type!="xls"&&file_type!="xlsx"&&file_type!="txt"&&file_type!="pdf"){
					layer.alert('上传文件格式错误! 请上传后缀名为.doc，.docx，.xls，.xlsx，.txt，.pdf的文件', {icon:0});
					return false;
				}
			}
		}*/
		
		outer:for(var i=0,len=files.length;i<len;i++){
			var file = files[i];
			if(file.size<=0){
				layer.alert(file.name+"文件为空！", {icon: 0});
				continue;
			}
			var fileList = [];
    		if(modalType == 'new'){
    			fileList=_files;
        	}else if(modalType == 'edit'){
        		fileList=_editfiles;
        	}else if(modalType == 'check'){
        		fileList=_checkfiles;
        	}
    		
    		for(var j=0;j<fileList.length;j++){
    			if(fileList[j].fileNameOld ==file.name){
    				layer.alert(file.name+"文件已存在！！！",{icon:0});
    				continue outer;
    			}
    		}
    		
			formFile.append("files", file);
			//读取文件
			if (window.FileReader) {    
				(function(i){
					var file = files[i];
					var reader = new FileReader();    
					reader.readAsDataURL(file);   
				    reader.onerror = function(e) {
				    	  layer.alert("文件" + file.name +" 读取出现错误", {icon: 0});
				        return false;
				    }; 
					reader.onload = function (e) {
						if(e.target.result) {
							//console.log("文件 "+file.name+" 读取成功！");
						}
					}; 
				})(i);   
			} 
			//列表展示
			var _tr = '';
			var file_name = file.name.split("\.")[0];
			var file_type = file.name.split("\.")[1];
			var _td_icon;
			var _td_name = '<span >'+file.name+'</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
			var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
			switch(file_type){
				case "doc":
				case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
				case "xls":
				case "xlsx":_td_icon = '<img src="'+_icon_excel+'" />';break;
				case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
				case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
				case "png":
				case "jpeg":
				case "bmp":
				case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
				default:_td_icon = '<img src="'+ _icon_other+'" />';break;
			}
			var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
			_tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>'; 
			_table.append(_tr);  
			
		} 
		//上传文件
    	$.ajax({
	        type:'post',
	        url:'/zuul/devManage/devtask/uploadFile',
	        contentType: false,  
	        processData: false,
	        dataType: "json",
	        data:formFile,
	        success:function(data){ 
	        	for(var k=0,len=data.length;k<len;k++){
	        		
	        		if(modalType == 'new'){
	        			_files.push(data[k]); 
	        		}else if(modalType == 'edit'){
	        			_editfiles.push(data[k]); 
	        		}else if(modalType == 'check'){
	        			_checkfiles.push(data[k]);
	        		}
		        	$(".file-upload-tb span").each(function(o){ 
		        		if($(this).text() == data[k].fileNameOld){ 
		        			$(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
		        			$(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
		        		}
		        	});
	        	}
	        	if(modalType == 'new'){
	        		$("#files").val(JSON.stringify(_files));
	        		clearUploadFile('uploadFile');
	        	}else if(modalType == 'edit'){
	        		$("#editfiles").val(JSON.stringify(_editfiles));
	        		clearUploadFile('edituploadFile');
	        	}else if(modalType == 'check'){
	        		$("#checkfiles").val(JSON.stringify(_checkfiles));
	        		clearUploadFile('checkuploadFile');
	        	}
	        },
	        error:function(){
	            $("#loading").css('display','none');
	            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	        }
	    });
	});
}

//清空上传文件
function clearUploadFile(idName){
	//$(idName).wrap('<form></form>');
	//$(idName).unwrap();
	$('#'+idName+'').val('');
}

//移除上传文件
function delFile(that){
	var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text(); 
	$(that).parent().parent().remove();
	removeFile(fileS3Key);
}

//移除暂存数组中的指定文件
function removeFile(fileS3Key){
	if(modalType == "new"){
		var _file = $("#files").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_files = files;
			$("#files").val(JSON.stringify(files));
		}
		
	}else if(modalType == 'edit'){
		var _file = $("#editfiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_editfiles = files;
			$("#editfiles").val(JSON.stringify(files));
		}
	}else if(modalType == 'check'){
		var _file = $("#checkfiles").val();
		if(_file != ""){
			var files = JSON.parse(_file);
			for(var i=0,len=files.length;i<len;i++){
				if(files[i].fileS3Key == fileS3Key){
					Array.prototype.splice.call(files,i,1);
					break;
				}
			}
			_checkfiles  = files;
			$("#checkfiles").val(JSON.stringify(files));
		}
	}
}

//文件下载
function download(that){
	var fileS3Bucket = $(that).children(".file-bucket").text();
	var fileS3Key = $(that).children(".file-s3Key").text();
	var fileNameOld = $(that).children("span").text();
	var url = encodeURI("/devManage/devtask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld);
	window.location.href = url;
	
}
//导出
function exportTask_btn(){
	var obj = {};
	obj.featureCode = $.trim( $("#devTaskCode").val());
	obj.featureName =  $.trim( $("#featureName").val());
	var devTaskStatus = $("#devTaskStatus").val();
	if(devTaskStatus!=null && devTaskStatus!=''){
		devTaskStatus = devTaskStatus.join(",");
	}
	obj.requirementFeatureStatus = devTaskStatus;
	// obj.requirementIds = $("#requirementId").val();
	obj.requirementCode = $.trim( $("#requirement_Code").val());//
	obj.requirementName = $.trim( $("#requirementName").val());//

	obj.manageUserName = $("#devManPostName").val();//
	obj.executeUserName = $("#execteUserName").val();//
	obj.systemIds = $("#systemId").val();//
	obj.windowPdate = $("#planVersionName").val();//
	obj.systemVersionName = $("#sel_systemVersionName").val();

	obj.createType = $("#createTye").val();
	obj.sprints = $("#sprintIds").val();
	var reqFeaturePriority = $("#reqFeaturePriority").val();
	if(reqFeaturePriority!=null && reqFeaturePriority!=''){
		reqFeaturePriority = reqFeaturePriority.join(",");
	}
	obj.reqFeaturePrioritys =reqFeaturePriority ;
	var time=$("#dev_createDate").val();
	var createStartDate = '';
	var createEndDate = '';
	if(time != undefined){
		var timeArr=time.split(' - ');
		createStartDate=timeArr[0];
		createEndDate=timeArr[1];
	}
	obj.sidx = "id";
	obj.sord = "desc";
	var obj1 = JSON.stringify(obj);
	window.location.href = "/zuul/devManage/devtask/exportExcel?reqFeatue="+encodeURIComponent(obj1, 'utf-8')+"&startDate="+createStartDate+"&endDate="+createEndDate;
	 
	
}

//点击调整排期按钮
function toWindow(){
	idArr=[];
	var idArr=$('#list2').jqGrid('getGridParam','selarrrow')
	if( idArr.length==0 ){
		layer.alert('请选择至少一个开发任务!',{
				 icon: 0,
		}) 
		return ;
	}else{
		//console.log(idArr);
		ids = idArr;
		$("#scheduling").modal("show");
		searchWindows();
		}
}

//调整排期弹窗
function searchWindows(){  
	$("#windowTable").bootstrapTable('destroy');
    $("#windowTable").bootstrapTable({  
    	url:"/devManage/devtask/selectWindows",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 5,
        pageList : [ 5, 10, 15],
        queryParams : function(params) {
             var param={ 
            		 windowName:$.trim($("#windowName").val()),
            		 pageNumber:params.pageNumber,
                     pageSize:params.pageSize, 
             }
            return param;
        },
        
        columns : [{
        	 field : "id",
             title : "", 
             width : 30,
             formatter : function ( value, rows ,index ){
            	 var row = JSON.stringify(rows).replace(/"/g, '&quot;');
            	 return "<input type='checkbox' onclick='onlyOneJob( "+row+",this )' class='onlyOneJob' />";
             }
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "windowName",
            title : "窗口名称",
            align : 'center'
        },{
            field : "windowDate",
            title : "投产日",
            align : 'center'
        }],
        onLoadSuccess:function(){
        	$("#scheduling").modal("show");
        },
        onLoadError : function() {

        }
    });
}
function onlyOneJob(row,This){  
	$("#windowTable .onlyOneJob").prop('checked',false);
	$(This).prop('checked',true);
	windowId = row.id;
	//console.log(windowId);
}
function clearSearchWindows(){
	$("#windowName").val("");
	searchWindows();
}

function relatedWindow(){
	var rows = [];
	var flag=0;
	$(".jobArea").empty();
	for(var i = 0; i<ids.length; i++){
		var rowData = $("#list2").jqGrid('getRowData',ids[i]);
		rows.push(rowData);
	}
	for(var i = 0; i<rows.length; i++){
		if(rows[i].windowName != ''){
			flag = 1;
		}
	}
	if( flag==0 ){
		confirmCommission();
		return;
	}
	for( var i=0;i<rows.length;i++ ){
		if( rows[i].windowName != ''  ){
			var str='';
			str+='<div class="oneJob">'+rows[i].featureCode+' | '+rows[i].featureName +' &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp'+rows[i].windowDate +'投产</div>';
			$(".jobArea").append( str );
		} 
	} 
	$("#confirmModel").modal("show");
}


//调整排期确定
function confirmCommission(){
	$.ajax({
		type:"post", 
		url:"/projectManage/commissioningWindow/relationRequirement",
		dataType:"json",
		data:{
			ids:ids,
			windowId : windowId,
		},
		success : function(data) {
			layer.alert('调整排期成功!',{
				 icon: 1,
			},function(index){
				$("#confirmModel").modal("hide"); 
				$("#scheduling").modal("hide"); 
				layer.close(index);
				$("#list2").trigger("reloadGrid");
			})  
		},
		error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	});
}
// ===================批量修改==================================

function toAllUpdate(){
    system.systemId = "";
    system.reqFeatureIds = "";
    system.codeReviewStatus = "";
    system.codeReviewSubmitBtn = true;
	$("#reqFeatureIds").val('');
	$("#updateexecuteUserId").val('');
	$("#updateexecuteUserName").val('');
	$("#updateSprintId").empty();
	$("#updateSprintId").append("<option value=''>请选择</option>");
	$("#updatedevTaskStatus").empty();
	$("#updatedevTaskStatus").append("<option value=''>请选择</option>");

	var idArr=$('#list2').jqGrid('getGridParam','selarrrow')
	if( idArr.length==0 ){
		layer.alert('请选择至少一个开发任务!',{
				 icon: 0,
		}) 
		return ;
	}else{
		var systemArr = [];
		for(var j=0;j<idArr.length;j++){
			systemArr.push( $("#list2").jqGrid('getRowData',idArr[j]));
		}
		var hash1 = [];
		var mode = [];
		for (var i = 0; i < systemArr.length; i++) {
			var obj = {
				id:systemArr[i].systemId,
                codeReviewStatus:systemArr[i].systemCodeReviewStatus
			};
		     hash1.push(obj);
		     mode.push(systemArr[i].developmentMode);
		 }

		var hash2 = [];
		for (var i = 0; i < hash1.length; i++) {
		    if(hash2.indexOf(hash1[i].id)==-1){
		      hash2.push(hash1[i].id);
		      system.codeReviewStatus = hash1[i].codeReviewStatus;
		     }
		 }
		
		 if(hash2.length>1){
			 layer.alert('请选择属于同一系统的开发任务!',{
				 icon: 0,
			 });
			 return;
		 }else{
			system.systemId = hash2[0];
			 if(mode[0] == 2){//稳态
				$("#update_SprintDiv").hide();
				$("#updateSprintId").selectpicker('val','');
			 }else if(mode[0] == 1){
				 $("#update_SprintDiv").show();
			 }
			var reqFeatureIds =idArr;
             system.reqFeatureIds = idArr;
			$("#reqFeatureIds").val(reqFeatureIds);
			for(var i=0;i<devtaskStatusList.length;i++){
				if(devtaskStatusList[i].valueCode!=""){
					$("#updatedevTaskStatus").append('<option value="'+devtaskStatusList[i].valueCode+'">'+devtaskStatusList[i].valueName+'</option>');
				}
			}	
			getUpdateData(hash2.join(''));

			//增加系统版本和项目小组
			 getBatchVersion(hash2.join(''));
			 getBatchGroup(hash2.join(''));
			
			$("#allUpdateModel").modal("show"); 
			
		 }	
	}
}
function getUpdateData(systemId){
	$.ajax({
		url:"/devManage/displayboard/getSprintBySystemId",
		dataType:"json",
		type:"post",
		data:{systemId:systemId,projectId:$("#newProject_listId").val(),},
		success:function(data){
			if(data.sprintInfos!=undefined && data.sprintInfos.length>0){

				for(var i=0;i<data.sprintInfos.length;i++){
					$("#updateSprintId").append('<option value="'+data.sprintInfos[i].id+'">'+data.sprintInfos[i].sprintName+'</option>')
				}
			}
			$('.selectpicker').selectpicker('refresh'); 
		}
	
	})
}




function getBatchVersion(systemId){
	$("#batchVersion").empty();
	$('.selectpicker').selectpicker('refresh');
	$.ajax({
		url:"/devManage/systemVersion/getSystemVersionByCon",
		dataType:"json",
		type:"post",
		data:{
			systemId:systemId,
			status:1
		},
		success:function(data){
			if(data.rows!=undefined && data.rows.length>0){
				$("#batchVersion").append('<option value="">请选择</option>');
				for(var i=0;i<data.rows.length;i++){
					$("#batchVersion").append('<option value="'+data.rows[i].id+'">'+data.rows[i].version+ '</option>')
				}
			}
			$('.selectpicker').selectpicker('refresh');
		}

	})
}

function updateSprints(){
    if(system.codeReviewSubmitBtn == false){
        layer.alert(system.codeReviewDevTaskListMsg, {
            title: "提示信息",
            closeBtn: 0,
            btn:["取消"]
        });
        return false;
    }
	var executeProjectGroupId=getCheckedBatchZtree();
	
	if($("#updateSprintId").val()=='' && $("#updatedevTaskStatus").val()=='' && $("#updateexecuteUserId").val()=='' && executeProjectGroupId=='' && $("#batchVersion").val()==''){
		layer.alert('请至少填写其中一项！',{ icon: 0,});
		return;
	}
	$("#loading").css('display','block');

	$.ajax({
		url:"/devManage/devtask/updateSprints",
		dataType:"json",
		type:"post",
		data:{
			ids: $("#reqFeatureIds").val(),
			sprintId: $("#updateSprintId").val(),
			devTaskStatus :$("#updatedevTaskStatus").val(),
			executeUserId:$("#updateexecuteUserId").val(),
			systemVersionId:$("#batchVersion").val(),
			executeProjectGroupId:executeProjectGroupId

		},
		success:function(data){
			$("#loading").css('display','none');
			if(data.status =="success"){
				layer.alert("修改成功！",{icon:1});
				$("#allUpdateModel").modal("hide");
				pageInit();
			}else{
				layer.alert("修改失败！",{icon:2});
			}
		}
	})
}


//==============================end================================
//查看页面查看附件
function showFile(){
	var reqFeatureId = $("#checktaskId").val();
	var requirementId =  $("#checkItcdReqId").val();
	
	if(requirementId== ''){
		layer.alert('该任务下没有附件！', {icon: 0});
		return;
	}
	/*window.open("http://10.1.13.105:7001/ITCDWeb/attachment/findAttachment?reqId="+13199+"&reqtaskId="+13199+"&taskreqFlag=requirement",'height=100, width=100');
 	window.location.href = "http://10.1.13.105:7001/ITCDWeb/attachment/findAttachment2?reqId="+13199+"&reqtaskId="+reqFeatureId+"&taskreqFlag=task"
	*/
	layer.open({
	  type: 2,
	  area: ['700px', '450px'],
	  fixed: false, //不固定
	  maxmin: true,
	  btnAlign: 'c',
	  title:"相关附件",
	  content: reqAttUrl+"?reqId="+htmlEncodeJQ(requirementId)+"&reqtaskId="+htmlEncodeJQ(reqFeatureId)+"&taskreqFlag=task"
	});
}
//查看需求的附件
function showReqFile(){
	var requirementId =  $("#checkItcdReqId").val();
	if(requirementId== ''){
		layer.alert('该任务所关联的需求下没有附件！', {icon: 0});
		return;
	}
	layer.open({
	  type: 2,
	  area: ['700px', '450px'],
	  fixed: false, //不固定
	  maxmin: true,
	  btnAlign: 'c',
	  title:"相关需求附件",
	  content: reqAttUrl+"?reqId="+requirementId+"&reqtaskId="+requirementId+"&taskreqFlag=requirement"
	});
}

//获取部署状态
function getDeployStatus(){
	$.ajax({
		url:"/devManage/devtask/getDeployStatus",
		method:"post", 
		dataType:"json",
  		success:function(data){  
  			deployStatusData = data.deployStatus;
        }, 
        error:function(){
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	})
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
        	newProjectGroup: {
        		trigger:"change",
                validators: {
                	 notEmpty: {
                         message: '项目小组不能为空'
                     },
                    
                }
            },
        	featureName: {
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
            featureOverview: {
                validators: {
                	 notEmpty: {
                         message: '描述不能为空'
                     },
                    
                }
            },
            nestimateWorkload:{
            	validators: {
               	 notEmpty: {
                        message: '工作量不能为空'
                    },
                    numeric: {
						   message: '只能输入数字'
	                	},
	                    regexp: {
	                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
	                	      message: '请输入大于0且小于等于500的正数'
	                	 },
               }
            },
            newstoryPoint:{
            	validators: {
                   numeric: {
					   message: '只能输入数字'
                	},
                    regexp: {
                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
                	      message: '请输入大于0且小于等于500的正数'
                	 },
                  }
            },
            newdevManageUserName:{
            	 trigger:"change",
            	validators: {
                  	 notEmpty: {
                           message: '管理岗不能为空'
                       },
                  }
            },
            newexecuteUserName:{
            	 trigger:"change",
            	validators: {
                  	 notEmpty: {
                           message: '执行人不能为空'
                       },
                  }
						},
			newProject_list:{
            	trigger:"change",
            	validators:{
            		notEmpty:{
            			message:"关联项目不能为空"
            		},
            	}
            },
            newsystemName:{
            	trigger:"change",
            	validators:{
            		notEmpty:{
            			message:"关联系统不能为空"
            		},
            	}
            },
            newreqFeaturePriority:{
            	trigger:"change",
            	validators:{
            		notEmpty:{
            			message:"优先级不能为空"
            		},
            	}
            },
            new_startWork:{
            	trigger:"change",
            	validators: {
            		notEmpty:{
            			message:"预计开始时间不能为空"
            		},
            		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '开始时间必须小于结束日期！',
                        callback:function(value, validator,$field){
                        	if( value == "" ){
                        		return true;
                        	}else{
                        		if( $( "#endWork" ).val() == '' ){
                        			return true;
                        		}else{
                        			var start = new Date( value );
                        			var end = new Date( $( "#endWork" ).val() );
                        			if( start.getTime() > end.getTime() ){
                        				return false;
                        			} 
                        			return true;
                        		}
                        	}
                        }
            		}
            	}
            },
            new_endWork:{
            	trigger:"change",
            	validators: {
            		notEmpty:{
            			message:"预计结束时间不能为空"
            		},
            		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '结束时间必须大于开始日期！',
                        callback:function(value, validator,$field){
                        	if( value == "" ){
                        		return true;
                        	}else{
                        		if( $( "#startWork" ).val() == '' ){
                        			return true;
                        		}else{
                        			var start = new Date( $( "#startWork" ).val() );
                        			var end = new Date( value ); 
                        			if( start.getTime() > end.getTime() ){
                        				return false;
                        			} 
                        			return true;
                        		}
                        	}
                        }
            		}
            	}
            }
        }
    }); 
    $("#editForm").bootstrapValidator({
    　　　　message: 'This value is not valid',//通用的验证失败消息
       　feedbackIcons: {//根据验证结果显示的各种图标
           　　　　　　　　valid: 'glyphicon glyphicon-ok',
           　　　　　　　　invalid: 'glyphicon glyphicon-remove',
           　　　　　　　　validating: 'glyphicon glyphicon-refresh'
       　　　　　　　　   },
       fields: {
    	   efeatureName: {
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
           efeatureOverview: {
               validators: {
               	 notEmpty: {
                        message: '描述不能为空'
                    },
               }
           },
           editdevManageUserName:{
           	validators: {
                 	 notEmpty: {
                          message: '管理岗不能为空'
                      },
                 }
           },
           editexecuteUserName:{
           	validators: {
                 	 notEmpty: {
                          message: '执行人不能为空'
                      },
                 }
					 },
					 
           editsystemName:{
        	   trigger:"change",
        	   validators:{
           		notEmpty:{
           			message:"关联系统不能为空"
           		},
           	}
        	   
           },
           eestimateWorkload:{
           	validators: {
              	 notEmpty: {
                       message: '工作量不能为空'
                   },
                   numeric: {
						   message: '只能输入数字'
	                	},
	                    regexp: {
	                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
	                	      message: '请输入大于0且小于等于500的正数'
	                	 },
        			}
              
           },
           eactualWorkload:{
        		validators: {
                      numeric: {
   						   message: '只能输入数字'
   	                	},
   	                    regexp: {
   	                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
   	                	      message: '请输入大于0且小于等于500的正数'
   	                	 },
           			}
           },
           editstoryPoint:{
           	validators: {
                  numeric: {
					   message: '只能输入数字'
               	},
                   regexp: {
               	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
               	      message: '请输入大于0且小于等于500的正数'
               	 },
              }
           },
       }
   });
    
    $("#splitForm").bootstrapValidator({
    	 excluded:[":disabled"],//关键配置1，表示只对于禁用域不进行验证，其他的表单元素都要验证
　　　　message: 'This value is not valid',//通用的验证失败消息
	   　feedbackIcons: {//根据验证结果显示的各种图标
	       　　　　　　　　valid: 'glyphicon glyphicon-ok',
	       　　　　　　　　invalid: 'glyphicon glyphicon-remove',
	       　　　　　　　　validating: 'glyphicon glyphicon-refresh'
	   　　　　　　　　   },
	   fields: {
		   sWorkSummary:{
			   trigger:"change",
          	 validators: {
              	 notEmpty: {
                       message: '摘要不能为空'
                   },
                   stringLength: {
                       min:5,
                       max: 300,
                       message: '长度必须在5-300之间'
                   }
              }
          },
          sWorkOverView:{
        	  trigger:"change",
          	 validators: {
              	 notEmpty: {
                       message: '描述不能为空'
                   },
                   stringLength: {
                       min: 0,
                       max: 500,
                       message: '长度必须小于500'
                   }
              }
          },
          startWork:{
          	validators: {
             	 notEmpty: {
                      message: '计划开始日期不能为空'
                  },
             }
          },
          endWork:{
          	validators: {
                	 notEmpty: {
                         message: '计划结束日期不能为空'
                     },
                }
          },
          sWorkPlanWorkload:{
        	  validators: {
             	 notEmpty: {
                      message: '预计工作量不能为空'
                  },
                  regexp: {
               	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
               	      message: '请输入大于0且小于等于500的正数'
               	 },
             }
          }
          
          
	   }
    });
  
    $("#handleForm").bootstrapValidator({
　　　　message: 'This value is not valid',//通用的验证失败消息
	   　feedbackIcons: {//根据验证结果显示的各种图标
	       　　　　　　　　valid: 'glyphicon glyphicon-ok',
	       　　　　　　　　invalid: 'glyphicon glyphicon-remove',
	       　　　　　　　　validating: 'glyphicon glyphicon-refresh'
	   　　　　　　　　   },
	   fields: {
		   startWork:{
	          	validators: {
	             	  notEmpty: {
	                      message: '实际开始日期不能为空'
	                  },
	                  callback:{/*自定义，可以在这里与其他输入项联动校验*/
	                        message: '开始日期必须小于结束日期！',
	                        callback:function(value, validator,$field){
	                        	var start = new Date( value );
                    			var end = new Date( $( "#handleendWork" ).val() ); 
                    			if( start.getTime() > end.getTime() ){
                    				return false;
                    			} 
                    			return true;
	                        }
	            		}
	             }
	          },
	          endWork:{
	          	validators: {
	                	 notEmpty: {
	                         message: '实际结束日期不能为空'
	                     },
	                     callback:{/*自定义，可以在这里与其他输入项联动校验*/
		                        message: '结束日期必须大于开始日期！',
		                        callback:function(value, validator,$field){
		                        	var start = new Date( $( "#handlestartWork" ).val() );
	                    			var end = new Date( value ); 
	                    			if( start.getTime() > end.getTime() ){
	                    				return false;
	                    			} 
	                    			return true;
		                        }
		            		}
	                }
	          },
		   handleactWorkload :{
			   validators: {
	             	 notEmpty: {
	                      message: '工作量不能为空'
	                  },
	                  numeric: {
  						   message: '只能输入数字'
  	                	},
	                  regexp: {
	                	      regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
	                	      message: '请输入大于0且小于等于500的正数'
	                	 },
	             }
		   }
	   }
    });
});
function showThisDiv(This,num){
	if( $(This).hasClass("def_changeTit") ){
		$("#titleOfwork .def_controlTit").addClass("def_changeTit");
		$(This).removeClass("def_changeTit");
		if( num==1 ){
			$(".dealLog").css("display","none");
			$(".workRemarks").css("display","block");
		}else if( num==2 ){
			$(".dealLog").css("display","block");
			$(".workRemarks").css("display","none");
		}
	}   
}

//获取开发任务状态
function getReqFeatureStatus(){
	var devtaskStatusList = '';
	$.ajax({
		url:"/devManage/devtask/getReqFeatureStatus",
		dataType:"json",
		type:"post",
		async:false, 
		success:function(data){
			devtaskStatusList = data.reqFeatureStatus;
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		}
		
	});
	return devtaskStatusList;
}

var zTree;


function getProjectGroup(){

	$.ajax({
		url:"/devManage/devtask/getProjectGroup",
		dataType:"json",
		type:"post",

		success:function(data){ 
			var znodes=JSON.parse( data.zNodes ); 
			$.fn.zTree.init($("#projectOwn"),setting,znodes);   
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		}

	});
	return devtaskStatusList;
} 


function getProjectGropId(){
	$( "#projectInput" ).val("").change();
	var projectId=$("#newProject_listId").val();
	var systemCode=$("#newsystemId").attr( "systemCode"); 
	$.ajax({
		url:"/devManage/devtask/getProjectGroupByProjectId1",
		dataType:"json",
		data:{
			"projectId":projectId
		},
		type:"post",
		success:function(data){  
			$("#projectOwn").empty();
			if( data.zNodes != ''){ 
				var znodes=JSON.parse( data.zNodes  ); 
				$.fn.zTree.init($("#projectOwn"), setting, znodes); 
				if(znodes.length==1){
					var zTreea = $.fn.zTree.getZTreeObj("projectOwn");
					var node = zTreea.getNodeByParam("id",znodes[0].id);
					zTreea.selectNode(node,true);
					$( "#projectInput" ).val( node.name ).change();
				}  
			} 
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		}

	}); 
	$.ajax({
		url:"/projectManage/systemTree/getSystemTreeByCode",
		dataType:"json",
		data:{
			"projectId":$("#newProject_listId").val()
		},
		type:"post",
		success:function(data){
//			var data = {
//					data :[{businessSystemTreeName:'66666',id:2}]
//			}
			var znodes=data.data; 
			if(znodes==""){
//				$("#modalInput" ).val('').change();
				$.fn.zTree.init($("#modalOwn"), modalSetting, "");
				$("#newform").bootstrapValidator('removeField','modalInput');
			}else { 
				$.fn.zTree.init($("#modalOwn"), modalSetting, znodes); 
				if(znodes.length==1){
					var zTreea = $.fn.zTree.getZTreeObj("modalOwn");
					var node = zTreea.getNodeByParam("id",znodes[0].id); 
					zTreea.selectNode(node,true);
					$( "#modalInput" ).val( node.businessSystemTreeName ).change();
				} 
//				$("#newform").bootstrapValidator("addField", "modalInput", {
//					trigger:"change",  
//					validators: {
//				       	 notEmpty: {
//				                message: '模块不能为空'
//				            },
//				       }
//				}); 
			} 
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		}

	}); 
}

function getCheckedZtree(){
	var zTree = $.fn.zTree.getZTreeObj("projectOwn"); 
	var nodes = zTree.getSelectedNodes(); 
	var id="";
	for (var i=0, l=nodes.length; i<l; i++) { 
		id=nodes[i].id;   //获取选中节点的id值 
	}
	return id;
}
function getCheckedModalZtree(){
	var zTree = $.fn.zTree.getZTreeObj("modalOwn"); 
	var nodes = zTree.getSelectedNodes();  
	var id="";
	for (var i=0, l=nodes.length; i<l; i++) { 
		id=nodes[i].id;   //获取选中节点的id值 
	}
	return id;
}
function getCheckedBatchZtree(){
	var zTree = $.fn.zTree.getZTreeObj("batchProject");
	if(zTree!=null) {
		var nodes = zTree.getSelectedNodes();
		var id = "";
		for (var i = 0, l = nodes.length; i < l; i++) {
			id = nodes[i].id;   //获取选中节点的id值
		}
	}else{
		id="";
	}
	return id;
}

function getBatchGroup(systemId){
	$( "#batchGroup" ).val("");

	$.ajax({
		url:"/devManage/devtask/getProjectGroupByProjectId",
		dataType:"json",
		data:{
			systemId:systemId
		},
		type:"post",
		success:function(data){


			var znodes=JSON.parse( data.zNodes );

			if(znodes==""){

			}else {



				$.fn.zTree.init($("#batchProject"), settingBatch, znodes);


				if(znodes.length==1){
					var zTreea = $.fn.zTree.getZTreeObj("batchProject");
					var node = zTreea.getNodeByParam("id",znodes[0].id);
					zTreea.selectNode(node,true);
					$( "#batchGroup" ).val( node.name );
				}

			}

		 },
		 error:function(){
		layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		 }

	});



}

function batchshowMenu() {

	var cityObj = $("#batchGroup");
	var cityOffset = $("#batchGroup").offset();
	$("#batchMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", onBodyDownBatch);
}
//导入
function upload() {
	var formData = new FormData();
	formData.append("file", document.getElementById("upfile").files[0]);
	if (document.getElementById("upfile").files[0] == undefined) {
		layer.alert("请选择文件", { icon: 0 });
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/devManage/devtask/importExcel",
		type: "POST",
		data: formData,
		/**
		 *必须false才会自动加上正确的Content-Type
		 */
		contentType: false,
		/**
		 * 必须false才会避开jQuery对 formdata 的默认处理
		 * XMLHttpRequest会对 formdata 进行正确的处理
		 */
		processData: false,
		success: function (data) {
			if (data.status == 2) {
				layer.alert("导入失败，原因:" + data.errorMessage, { icon: 2 });
				$("#importReqFea").modal("hide");
			} else {

				if(data.notice!=null && data.notice!=""){
					layer.alert(data.notice, { icon: 1 });
				}else {

					layer.alert("导入成功", {icon: 1});
				}
				$("#importReqFea").modal("hide");
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

//显示导入案例弹窗
function importTestCase_btn() {
	$("#upfile").val("");
	$("#importReqFea").modal("show");
}

function downloadReqExcel() {
	window.location.href = "/devManage/devtask/downloadReqExcel"
}


function copyReq() { 
	var idArr=$('#list2').jqGrid('getGridParam','selarrrow')  
	if( idArr.length>1 || idArr.length==0 ){
		layer.alert('请选择一个开发任务!',{
			icon: 0,
		})
		return ;
	}else { 
		$("#loading").css('display', 'block');
		newClearForm();
		var obj=$("#list2").jqGrid('getRowData', idArr[0]); 
		$("#newDevTask").modal("show");
	    $.ajax({
			url:"/devManage/devtask/getOneDevTask3",
			dataType:"json",
		    type:"post",
			data:{
				"id": obj.id
			},
			success:function(data){
				if(data.status == "2"){
					$("#loading").css('display','none');
					layer.alert("数据加载失败！",{icon:2});
					return;
				}else{  
					//清空表单
					newClearForm();  
					
					$("#dftReqFeatureId").val(data.id);
					
					$("#nfeatureName").val(data.featureName);
					$("#nfeatureOverview").val(data.featureOverview);
					
					$("#startWork").val(data.planStartDate);  
					$("#endWork").val(data.planEndDate); 
					
					$("#nestimateWorkload").val(data.planWorkload);   
					$("#newdevManageUser").val(data.manageUserId);
					$("#newdevManageUserName").val(data.manageUserName); 
					$("#newexecuteUser").val(data.executeUserId);
					$("#newexecuteUserName").val(data.executeUserName);
					$("#newsystemId").val(data.systemId); 
					$("#newsystemName").val( data.systemName ); 
					 
					
					$("#newrequirementId").val(data.requirementId);
					$("#newrequirementName").val(data.requirementCode).change();
					
					//$("#newquestion").val(data.questionNumber).change();
					  
					$("#newdeptId").val(data.deptId);
					$("#newDeptName").val(data.deptName).change(); 
					 
					$("#newcommitWindowId").val(data.commissioningWindowId);
					$("#newcommitWindowName").val(data.windowName).change();
					  
		            //  项目小组  模块
					checkedGroup(data.executeProjectGroupId,data.systemId,data.assetSystemTreeId,data.projectId);
 
					if(reqFeaturePriorityList!=null){
						for (var i = 0,len = reqFeaturePriorityList.length;i < len;i++) {
							var flag = '';
							if(reqFeaturePriorityList[i].value == data.requirementFeaturePriority){
								flag = "selected";
							}
			                $("#newreqFeaturePriority").append('<option '+flag+' value="'+reqFeaturePriorityList[i].value+'"> '+reqFeaturePriorityList[i].innerHTML+'</option>')
			             }
					} 
					// 自定义字段
					var field = data.fields;
					if( field != undefined ){
				    	for( var i = 0;i < field.length;i++ ){
				    		if( field[i].status == 1 ){
				    			appendDataType( field[i] , 'canEditField' , 'edit' );  
				    		}  
				        }
				    }
					 
					if($("#newsystemId").val()!=null && $("#newsystemId").val()!=''){
						$.ajax({
				    		url:"/devManage/systemVersion/getSystemVersionByCon",
				    		dataType:"json",
				    		type:"post",
				    		data:{
				    			systemId:$("#newsystemId").val(),
				    			status:1
				    		},
				    		success:function(data2){
				    			$("#newsystemVersionBranch").empty();
				    			$("#newsystemVersionBranch").append("<option value=''>请选择</option>");
				    			$("#repairVersionBranch").empty();
				    			$("#repairVersionBranch").append("<option value=''>请选择</option>");
				    			 
				    			var systemVersionStr='';
				    			var editrepairVersionStr=''; 
				    			
				  				$.each(data2.rows,function(index,value){
				  					var flag = '';
				  					var flag1 = '';
				  					if(data.systemScmBranch == value.groupFlag && data.systemVersionId == value.id){
			    						flag = 'selected';
			    					}
				  					 
				  					if( data.repairSystemVersionId == value.id ){
			    						flag1 = 'selected';
			    					}
									systemVersionStr += '<option '+flag+' value="'+value.id+','+value.groupFlag+'">'+value.version+'--> '+toStr(value.groupFlag)+'</option>';
									editrepairVersionStr += '<option '+flag+' value="'+value.id+'">'+value.version+'</option>';
				  				});
				  				$("#newsystemVersionBranch").append(systemVersionStr);
				  				$("#repairVersionBranch").append(editrepairVersionStr);
				    			 
				    			$('.selectpicker').selectpicker('refresh'); 
				    			
				    		}
				    	});
						if(data.developmentMode == 1){
							$.ajax({
								 url:"/devManage/devtask/getSprintBySystemId",
								 data:{
									 systemId:$("#newsystemId").val(),
									 projectId:$("#newProject_listId").val(),
								 },
								 type:"post",
								 dataType:"json",
								 success:function(data2){
									 $("#editSprintId").empty();
									 $("#editSprintId").append("<option value=''>请选择</option>");
									 if(data2.sprintInfos.length>0  && $("#newProject_listId").val()){
										 $("#newsprintDiv").show();
										 for(var i = 0;i<data2.sprintInfos.length;i++){
											 var flag = '';
											 if(data2.sprintInfos[i].id == data.sprintId){
												 flag = 'selected'
											 }
											 $("#newSprintId").append('<option '+flag+' value="'+data2.sprintInfos[i].id+'">'+data2.sprintInfos[i].sprintName+'</option>')
										 }
									 }
					        		$('.selectpicker').selectpicker('refresh'); 
								 }
							 });
							$("#newstoryPointDiv").show();
							$("#newstoryPoint").val(data.storyPoint); 
						}
					}
					if(data.requirementFeatureSource == '1'){
						$("#questDiv").hide();
			    		$("#reqDiv").show();
//			    		$("#dftDiv").hide();
					}else if(data.requirementFeatureSource == '2'){
						$("#questDiv").show();
			    		$("#reqDiv").hide();
//			    		$("#dftDiv").hide();
					}else if(data.requirementFeatureSource =='3'){
//			    		$("#reqDiv").hide();
//			    		$("#questDiv").hide();
//			    		$("#dftDiv").show();
					} 
					
					/*var dftName='';
					var dftId ='';
                    var dftSynName='';
                    var dftSynId ='';
					if(data.defectInfos!=null && data.defectInfos!=undefined){
						for(var i=0;i<data.defectInfos.length;i++){
							if(data.defectInfos[i].requirementFeatureId == data.id){
                                if(data.defectInfos[i].createType==1){
                                    dftName+= data.defectInfos[i].defectCode+",";
                                    dftId += data.defectInfos[i].id+",";
                                }else{
                                    dftSynName+= data.defectInfos[i].defectCode+",";
                                    dftSynId += data.defectInfos[i].id+",";
                                }
							}
						}
						$("#newdefectId").val(dftId);
						$("#newdefectCode").val(dftName.substring(0,dftName.length-1)).change();
                        $("#newSynDefectId").val(dftSynId);
                        $("#newSynDefectCode").val(dftSynName.substring(0,dftSynName.length-1)).change();
					}*/
					if(data.dics!=null && data.dics!=undefined){
						for(var i=0;i<data.dics.length;i++){
							var flag='';
							if(data.dics[i].valueCode == data.requirementFeatureSource){
								flag = "selected";
							}
							$("#newrequirementFeatiureSource").append('<option '+flag+' value="'+data.dics[i].valueCode+'">'+data.dics[i].valueName+'</option>');
						}
					} 
					
					// 附件
					_editfiles=[];
					if(data.attachements!=undefined && data.attachements!=null){
						if(data.attachements.length>0){
							var _table=$("#newFileTable");
							// var
							// _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
							for(var i=0;i<data.attachements.length;i++){
								var _tr = '';
								var file_name = data.attachements[i].fileNameOld;
								var file_type = data.attachements[i].fileType;
								var file_id =  data.attachements[i].id;
								var _td_icon;
								var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.attachements[i].fileS3Bucket+'</i><i class = "file-s3Key">'+data.attachements[i].fileS3Key+'</i></td>';
								var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
								switch(file_type){
									case "doc":
									case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
									case "xls":
									case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
									case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
									case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
									case "png":
									case "jpeg":
									case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
									default:_td_icon = '<img src="'+ _icon_other+'" />';break;
								}
								_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(this)">'+_td_icon+" "+_td_name+_td_opt+'</tr>'; 
								_table.append(_tr); 
								_editfiles.push(data.attachements[i]);
								$("#files").val(JSON.stringify(_editfiles));
							}
						}
					}
					
					$('.selectpicker').selectpicker('refresh'); 
				} 
				$("#loading").css('display','none');
			},
			error:function(){
	            $("#loading").css('display','none');
	            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	        }
		}) 
	}  
}


function checkedGroup(id,systemId,codeId,projectId) { 
	// 初始化
	$.ajax({
		url:"/devManage/devtask/getProjectGroupByProjectId1",
		dataType:"json",
		data:{
			projectId:projectId
		},
		type:"post",
		success:function(data){
			if(data.zNodes!="") { 
				var znodes = JSON.parse(data.zNodes); 
				if (znodes == "") {
					$.fn.zTree.init($("#projectOwn"), setting, "");
				} else {
					$.fn.zTree.init($("#projectOwn"), setting, znodes); 
					var zTree = $.fn.zTree.getZTreeObj("projectOwn");
					var node = zTree.getNodeByParam("id", id); 
					zTree.cancelSelectedNode();
					zTree.selectNode(node, true); 
					if( node==null ){
						$("#projectInput").val('');
					}else{
						$("#projectInput").val(node.name);
					} 
				}
			} 
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		} 
	});   
	$.ajax({
		url:"/projectManage/systemTree/getSystemTreeByCode",
		dataType:"json",
		data:{
			"projectId":$("#newProject_listId").val()
		},
		type:"post",
		success:function(data){  
			if(data.data!="") { 
				var znodes = data.data; 
				if (znodes == "") {
//					$("#modalInput").val('').change();
//					$.fn.zTree.init($("#modalOwn"), modalSetting, "");
//					$("#newform").bootstrapValidator('removeField','modalInput');
				} else {
					$.fn.zTree.init($("#modalOwn"), modalSetting, znodes); 
					var zTree = $.fn.zTree.getZTreeObj("modalOwn");
					var node = zTree.getNodeByParam("id", codeId); 
					
					zTree.cancelSelectedNode();
					zTree.selectNode(node, true);  
					if( node==null ){
						$("#modalInput").val('').change();
					}else{ 
						$("#modalInput").val(node.businessSystemTreeName).change();
					}
//					$("#newform").bootstrapValidator("addField", "modalInput", {
//						trigger:"change",  
//						validators: {
//					       	 notEmpty: {
//					                message: '模块不能为空'
//					            },
//					       }
//					}); 
				}
			}else{
				$("#newform").bootstrapValidator('removeField','modalInput');
			}
		},
		error:function(){
			layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
		} 
	}); 
}

function jiraMethod(){
    $.ajax({
        method:"post",
        url:"/devManage/jiraWorkTask/jiraWorkTaskMethod",
        success : function(data) {
            if (data.status == 2) {
                layer.alert("jira同步失败", { icon: 2 });
            } else {
                layer.alert("jira同步成功", {icon: 1});
            }
        }
    });
}


function dateComponent(){
	
    var locale = {
        "format": 'yyyy-MM-dd',
        "separator": " -222 ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "起始时间",
        "toLabel": "结束时间",
        "customRangeLabel": "自定义",
        "weekLabel": "W",
        "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
        "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        "firstDay": 1
    };
	$("#dev_createDate").daterangepicker({'locale': locale});//, "timePicker": true,"timePicker24Hour": true
	$('#dev_createDate').on('cancel.daterangepicker', function(ev, picker) {
		$("#dev_createDate").parent().find('.btn_clear').show();
	});
	$('#dev_createDate').on('apply.daterangepicker', function(ev, picker) {
		  $("#dev_createDate").parent().find('.btn_clear').show();
	});
}

//验收操作
function CheckHandle(ID){
	$("#acceptId").val("");
	$("#acceptFileTable").empty();
	$("#acceptId").val(ID);
	accept_fileList = [];
	$("#acceptModal").modal('show');
}

var accept_fileList = [];
//验收上传文档
function accept_add_File(){
	$("#accept_add_File").on('change',function(){
		var files = this.files;
		outer:for(var i=0,len=files.length;i<len;i++){
			var file = files[i];
			if(file.size<=0){
				layer.alert(file.name+"文件为空！", {icon: 0});
				continue;
			}
    		for(var j=0;j<accept_fileList.length;j++){
    			if(accept_fileList[j].fileNameOld ==file.name){
    				layer.alert(file.name+"文件已存在！！！",{icon:0});
    				continue outer;
    			}
    		}
			//读取文件
			if (window.FileReader) {    
				(function(i){
					var file = files[i];
					var reader = new FileReader();    
					reader.readAsDataURL(file);   
				    reader.onerror = function(e) {
				    	  layer.alert("文件" + file.name +" 读取出现错误", {icon: 0});
				        return false;
				    }; 
				})(i);   
			}
			accept_fileList.push(file);
			//列表展示
			var _tr = '';
			var file_name = file.name.split("\.")[0];
			var file_type = file.name.split("\.")[1];
			var _td_icon;
			var _td_name = '<span >'+file.name+'</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
			switch(file_type){
				case "doc":
				case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
				case "xls":
				case "xlsx":_td_icon = '<img src="'+_icon_excel+'" />';break;
				case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
				case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
				case "png":
				case "jpeg":
				case "bmp":
				case "jpg":_td_icon = '<img src="'+_icon_img +'"/>';break;
				default:_td_icon = '<img src="'+ _icon_other+'" />';break;
			}
			_tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name + '</tr>'; 
			$("#acceptFileTable").append(_tr);  
		}
	})
}


function CheckHandle_submit(){
	if (!accept_fileList.length) {
		layer.msg("请选择文件", { icon: 0 });
		return;
	}
//	var files = this.files;
	var files = $("#accept_add_File").get(0).files;
	var formFile = new FormData();
	for(var i=0,len=files.length;i<len;i++){
		var file = files[i];
		formFile.append("files", file);
	}
	formFile.append("id", $("#acceptId").val());
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/zuul/devManage/devtask/checkStatus",
		type: "POST",
		data: formFile,
		contentType: false,
		processData: false,
		success: function (data) {
			if(data.status == 1){
				layer.alert("提交成功!", { icon: 1 });
				$("#list2").trigger("reloadGrid"); //重新载入
			}else{
				layer.alert(data.errorMessage, { icon: 2 });
			}
			$("#loading").css('display', 'none');
			$("#acceptModal").modal("hide");
		},
		error: function () {
			layer.alert("上传失败！", { icon: 2 });
			$("#loading").css('display', 'none');
		}
	});
}
//查询系统和需求下有多少开发任务
function getFeatureBySystemAndRequirement(systemId,requirementId){
	
	$.ajax({
		url:"/devManage/devtask/getFeatureBySystemAndRequirement",
		dataType:"json",
		data:{
			"systemId":systemId,
			"requirementId":requirementId
		},
		type:"post",
		success: function (data) {
			 if(data.feature.length > 0){
                 var featureData = "<div>该系统和需求下已存在开发任务！";
                 for (var i = 0; i < data.feature.length; i++) {
                	 featureData+= "<div>任务编号："+data.feature[i].featureCode+"</div>";
                 }
                 featureData += '</div>';
                 layer.alert(featureData, {
                     title: "提示信息",
                     closeBtn: 0,
                     btn:["取消"]
                 });
             } 
		},
		error: function () {
			layer.alert("查询失败！", { icon: 2 });
			$("#loading").css('display', 'none');
		}
	});
}


//收藏JS
//搜索框 收藏按钮控制 js 部分
function collect_handle() {
$(".collection2").click(function () {
	  collection2_obj.search = [];
  if ($(this).children("span").hasClass("fa-heart-o")) {
  	collection2_obj.search.push(
      { "type": "input", "value": { "devTaskCode": $("#devTaskCode").val()},"isData": _is_null("devTaskCode") },
      { "type": "input", "value": { "featureName": $("#featureName").val()}, "isData":_is_null("featureName") },
      { "type": "select", "value": { "devTaskStatus": change_str('devTaskStatus')}, "isData":_is_null("devTaskStatus") },
      { "type": "input", "value": { "requirement_Code": $("#requirement_Code").val()}, "isData":_is_null("requirement_Code") },
      { "type": "input", "value": { "requirementName": $("#requirementName").val()}, "isData":_is_null("requirementName") },
      { "type": "input", "value": { "devManPostName": $("#devManPostName").val()}, "isData":_is_null("devManPostName") },
      { "type": "input", "value": { "devManPost": $("#devManPost").val()}, "isData":_is_null("devManPost") },
      { "type": "input", "value": { "execteUserName": $("#execteUserName").val()}, "isData":_is_null("execteUserName") },
      { "type": "input", "value": { "execteUserId": $("#execteUserId").val()}, "isData":_is_null("execteUserId") },
      { "type": "input", "value": { "systemNameReq": $("#systemNameReq").val()}, "isData":_is_null("systemNameReq") },
      { "type": "input", "value": { "systemId": $("#systemId").val()}, "isData":_is_null("systemId") },
      { "type": "input", "value": { "planVersionName": $("#planVersionName").val()}, "isData":_is_null("planVersionName") },
      { "type": "input", "value": { "sel_systemVersionName": $("#sel_systemVersionName").val()}, "isData":_is_null("sel_systemVersionName") }, 
      { "type": "select", "value": { "createTye": change_str('createTye')}, "isData":_is_null("createTye") },
      { "type": "input", "value": { "sprintNames": $("#sprintNames").val()}, "isData":_is_null("sprintNames") },
      { "type": "input", "value": { "sprintIds": $("#sprintIds").val()}, "isData":_is_null("sprintIds") },
      { "type": "input", "value": { "dev_createDate": $("#dev_createDate").val()}, "isData":_is_null("dev_createDate") },
      { "type": "input", "value": { "reqFeaturePriority": $("#reqFeaturePriority").val()}, "isData":_is_null("reqFeaturePriority") },
  	)
    var isResult = collection2_obj.search.some(function (item) {
      return item.isData
    })
    collection2_obj.isCollect = isResult;
    if(isResult){
  	  $('#collect_Name').val('');
        $('#collect_Modal').modal('show');
    }
  } else {
    layer.confirm('确定要取消收藏吗?', {btn: ['确定', '取消'],icon:0, title: "提示"}, function () {
  	  collection2_obj.isCollect = false;
      collect_submit2();
    })
  }
})
}
//收藏提交
function collect_submit2(){
var sub_data = {};
var sub_url = 'collectCondition';
var is_name = true;
if(collection2_obj.isCollect){
  if(!$('#collect_Name').val()){
    is_name = false;
  }
  sub_data = {
    'menuUrl': collection2_obj.menuUrl,
    'filterName': $('#collect_Name').val(),
    'defectReport': JSON.stringify(collection2_obj.search),
  }
}else{
  sub_data = { id : $("#projectType2").val()};
  sub_url = 'updateDefectReport';
};
if(!is_name) {
  layer.alert('请填写方案名称!', {
    icon: 0,
  })
  return;
};
$("#loading").css('display', 'block');
$.ajax({
  type: "post",
  url: "/report/defectReport/" + sub_url,
  dataType: "json",
  data: sub_data,
  success: function (data) {
      if(collection2_obj.isCollect){
        if (data.code == 1) {
	          $(".collection2").children("span").addClass("fa-heart").removeClass("fa-heart-o");
	          layer.alert('收藏成功!', {
	            icon: 1,
	          })
	          $('#collect_Modal').modal('hide');
        }
      }else{
      	clearSearch();
	      $(".collection2").children("span").addClass("fa-heart-o").removeClass("fa-heart");
	      layer.alert('已取消收藏!', {
	        icon: 1,
	      })
	    }
      getCollection2();
      $("#loading").css('display', 'none');
  },
  error: function () {
    $("#loading").css('display', 'none');
    layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
  }
});
}
function _is_null(ele){
	  return $('#'+ele).val() ? true : false;
	}
function getCollection2() {
	  $("#projectType2").empty();
	  $("#projectType2").append('<option class="defaultSelect" value="">选择收藏方案</option>');
	  $("#loading").css('display','block');
	  $.ajax({
	    url:"/report/defectReport/selectDefectReportList",
	    dataType:'json',
	    type:'post',
	    data:{
	      menuUrl:'/devManageui/devtask/toDevTask',
	    },
	    success : function(data){
	      if(data.length){
	        data.map(v=>{
	        	if(v.lastUseFlag==1){
	        		 $("#projectType2 .defaultSelect").after(`<option value="${v.id}">${v.filterName}</option>`); 
	    		  }else{
	    			  $("#projectType2").append(`<option value="${v.id}">${v.filterName}</option>`);  
	    		  }
	        })
	      }
	      $("#projectType2").selectpicker('refresh');
	      $("#loading").css('display','none');
	    },
	    error:function(){
	      $("#loading").css('display','none');
	      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	    }
	  })
	}
//切换收藏
function tab_option(This){
	clearSearch();
var _id = $(This).val();
if(!_id) {
	  $(".collection2").children("span").addClass("fa-heart-o").removeClass("fa-heart");
	  return;
};
is_check_Collect_flag = true;
$("#loading").css('display','block');
$.ajax({
  url:"/report/defectReport/selectDefectReportById",
  dataType:'json',
  type:'post',
  data:{
  "menuUrl": "/devManageui/devtask/toDevTask",
   id:_id,
  },
  success : function(data){
    var msg = JSON.parse(data.favoriteContent);
    if(msg){
  	for(var i=0;i<msg.length;i++){
        if (msg[i].type == "select") {
          for (var key in msg[i].value) {
            $("#" + key).val(change_str_num(msg[i].value[key],key));
          }
        } else {
          for (var key in msg[i].value) {
            $("#" + key).val(msg[i].value[key]);
          }
        }
      }
      $(".selectpicker").selectpicker('refresh');
      $(".collection2").children("span").addClass("fa-heart").removeClass("fa-heart-o");
      searchInfo();
    }
    $("#loading").css('display','none');
  },
  error:function(){
    $("#loading").css('display','none');
    layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
  }
})
}
function change_str(ele){
	  if($('#'+ele).val()){
		  if(ele=="createTye"){
			  return $('#'+ele).val(); 
		  }else{
			  var arr = $('#'+ele).val().map(v=>{ 
				  if(ele=="devTaskStatus"){
					  return v
				  }else{
					  return v > 9 ? v : '0'+v
				  }
				  
			  })
			  return arr.join();
		  }
	    
	  }else{
	    return '';
	  }
	}

function change_str_num(str,key){
	  if(str){
		return str.split(',').map(v=>{ 
			if(key=="devTaskStatus"){
				 return v
			}else{
				 return v > 9 ? v : +v
			}
	     
	    })
	  }else{
	    return '';
	  }
	}

function other() {
	  var search_ele = ['devTaskCode','featureName','devTaskStatus','requirement_Code','requirementName','devManPostName','execteUserName','systemNameReq','planVersionName','sel_systemVersionName','createTye','sprintNames  ','dev_createDate','reqFeaturePriority'];
	  search_ele.map(function(ele){
		  $('#'+ ele).on('change', function () {
			  if(is_check_Collect_flag){
				  $(".collection2").children("span").removeClass("fa-heart").addClass("fa-heart-o");
			  }
		  })
	  })
}

