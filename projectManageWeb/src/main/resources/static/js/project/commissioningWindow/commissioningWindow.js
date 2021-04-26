var idArr= [];
var wid = '';
var windowId = '';
$(function(){  
//    dateComponent();
	formValidator();
	formValidator2();
	dateComponent();
    showWindowType();
    showFeatureStatus();
     
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
    });
    $("#downBtn2").click(function () { 
        if( $(this).children("span").hasClass( "fa-caret-up" ) ){
            $(this).children("span").removeClass("fa-caret-up");
            $(this).children("span").addClass("fa-sort-desc");
            $("#search_div2").slideUp(200);
        }else {
            $(this).children("span").removeClass("fa-sort-desc");
            $(this).children("span").addClass("fa-caret-up");
            $("#search_div2").slideDown(200);
        }
    });
 // 时间控件 配置参数信息
    var date=new Date;
    var year=date.getFullYear(); 
    $("#year").val(  year  );
    $("#year").datetimepicker({
    	startView: 'decade',  
    	minView: 'decade', 
    	format: 'yyyy', 
    	startDate : '2018',
    	maxViewMode: 2, 
    	minViewMode:2, 
    	autoclose: true,
        pickerPosition: "bottom-left"
    });
//    $("#windowDate").datetimepicker({
//        minView:"month",
//        format: "yyyy-mm-dd",
//        autoclose: true,
//        todayBtn: true,
//        language: 'zh-CN',
//        pickerPosition: "bottom-left"
//    });
    $("#add_windowDate").datetimepicker({
    	minView:"month",
    	format: "yyyy-mm-dd",
    	autoclose: true,
    	todayBtn: true,
    	language: 'zh-CN',
    	pickerPosition: "bottom-left"
    });
    $("#edit_windowDate").datetimepicker({
    	minView:"month",
    	format: "yyyy-mm-dd",
    	autoclose: true,
    	todayBtn: true,
    	language: 'zh-CN',
    	pickerPosition: "bottom-left"
    });
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
    //所有的Input标签，在输入值后出现清空的按钮
    $('.color input[type=text]').parent().css("position","relative");
    $('.color input[type=text]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $(".color input[type=text]").bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    })
    pageInit();  
   
});

//表格数据加载
function pageInit(){
	$("#loading").css('display','block');
    $("#list2").jqGrid({
        url:'/projectManage/commissioningWindow/selectCommissioningWindows',
        datatype: 'json', 
        mtype:"POST",
        height: 'auto',
        width: $(".content-table").width()*0.999,
        colNames:['id','窗口名称','投产日期','窗口类型','窗口状态','操作'],
        postData:{
       	 "year" :  $("#year").val(),
		 "status" : 1
       },
        colModel:[ 
            {name:'id',index:'id',hidden:true}, 
            {name:'windowName',index:'windowName'},
            {name:'windowDate',index:'windowDate'},
            {name:'typeName',index:'typeName'},
            {name:'窗口状态',index:'窗口状态',align:"center",
                formatter : function(value, grid, rows, state) {
                    var _status='';
					if(rows.status ==1){
                        _status='有效';
					}else{
                        _status='无效';
					}
					return _status;
                }
            },
            {name:'操作',index:'操作',align:"center",fixed:true,sortable:false,resize:false,search: false,width:300,
            	formatter : function(value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');
//                    return '<a class="a_style" onclick="edit('+ row + ')">编辑</a> | <a class="a_style" onclick="delect('+ row + ')">删除窗口</a> | <a class="a_style" onclick="relation('+ row +')">关联开发任务</a>'
                    var span_ = "<span>&nbsp;|&nbsp;</span>";
                    var a = '<a class="a_style" onclick="edit('+ row + ')">编辑</a>';
                    var b =''
                    if(rows.status ==1){
                        b = '<a class="a_style" onclick="delect('+ row + ','+2+')">置为无效</a>';
                    }else{
                        b = '<a class="a_style" onclick="delect('+ row + ','+1+')">置为有效</a>';
					}
                    var c = '<a class="a_style" onclick="relation('+ row +')">关联开发任务</a>';
                    var d = '<a class="a_style" onclick="export_modal('+ rows.id +')">Release_Notes导出</a>';
                    var opt_status = [];
                    if(windowEdit == true){
                    	opt_status.push(a);
                    }
                    if(windowDelete == true){
                    	opt_status.push(b);
                    }
                    if(windowRelation == true){
                    	opt_status.push(c);
                    }
                    if(windowExport == true){
                    	opt_status.push(d);
                    }
                    return opt_status.join(span_);
                }
            },
        ],
        rowNum:10,
        rowTotal: 200,
        rowList : [10,20,30], 
        rownumWidth: 40,
        pager: '#pager2', 
        sortname: 'id',
				loadtext:"数据加载中......",
        viewrecords: true, //是否要显示总记录数 
        jsonReader: {
            repeatitems: false,
            root: "data", 
        }, 
        gridComplete: function(){
			$("[data-toggle='tooltip']").tooltip(); 
        },
        loadComplete :function(){
        	$("#loading").css('display','none');
        }
    }).trigger("reloadGrid"); 
}


//导出
function export_modal(ID){
	windowId = ID;
	$("#loading").css('display','block');
	$("#presonTable").bootstrapTable('destroy');
    $("#presonTable").bootstrapTable({  
    	url:"/projectManage/commissioningWindow/getSystemsByWindowId",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParams : function(params) {
             var param={ 
            		"windowId":ID
             }
            return param;
        },
        responseHandler:function (res) {
       	 var rows=res.data;
           return {
           	"rows":rows
           };
       },
        columns : [{
            checkbox : true,
            width : "30px",
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "systemCode",
            title : "子系统编号",
            align : 'center'
            	
        },{
            field : "systemName",
            title : "子系统名称",
            align : 'center'
            	
        }],
        onLoadSuccess:function(){
            $("#loading").css('display','none');
        },
        onLoadError : function() {

        }
    }); 
    $("#select_Correlation").modal("show");
}

function _export(){
	var ids = [];
	var rows = $("#presonTable").bootstrapTable('getSelections');
	console.log(rows)
	if(rows.length<=0){
		layer.alert('请选择系统!',{
			 icon: 0,
		}) 
		return ;
	}else{
		for(var i=0; i<rows.length;i++ ){
			ids.push(rows[i].id);
		}
		var systemIds = ids.join(',');
		console.log(ids)
		console.log(systemIds)
		window.location.href = "/projectManage/commissioningWindow/export?windowId="+windowId+"&systemIds="+systemIds;
	}
}

function dateComponent(){
    var locale = {
        "format": 'yyyy-mm-dd',
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
	$("#windowDate").daterangepicker({'locale': locale});
}




//查询信息
function searchInfo(){ 
	$("#loading").css('display','block');
	var time = '';
	var st = '';
	var et = '';
	var nameArr=[];
	time=$("#windowDate").val();
	if(time != ''){
		var timeArr=time.split(' - ');
		st=timeArr[0];
		et=timeArr[1];
	} 
	$("#list2").jqGrid('setGridParam',{ 
		
        postData:{
        	 "windowName" :  $.trim($("#windowName").val()),
//             "windowDate" :  $("#windowDate").val(),
        	 "startDate":st,
        	 "endDate":et,
             "typeName" :  $("#windowType").val(),
             "year" :  $("#year").val(),
			"status": $("#status").val(),
        },   
        page:1
    }).trigger("reloadGrid"); //重新载入
}



//清空搜索内容
function clearSearch() {
    $('#windowDate').val("");
    $('#windowName').val("");
    $('#windowType').selectpicker('val', '');
    $('#status').selectpicker('val', '');
    var now = new Date();
    var year = now.getFullYear();
    $('#year').val(year);
    searchInfo();
}


//点击编辑(数据回显)
function edit( row ){
	$("#loading").css('display','block');
	$("#edit_window").modal("show");
	var i = row.id;  
	console.log(i);
	$.ajax({
		type:"post", 
		url:"/projectManage/commissioningWindow/selectCommissioningWindowById",
		dataType:"json",
		data:{
			"id":i
		},
		success : function(data) {
			 $("#loading").css('display','none');
			 $("#id").html(data.data.id);
			 $("#edit_windowName").val(data.data.windowName);
			 $("#edit_windowType option").each( function (i, n) {
				    if (n.value == data.data.typeName) {
				        n.selected = true;
				    }
				});
				$(".selectpicker").selectpicker('refresh');
			 $("#edit_windowDate").val(data.data.windowDate);
		}
	});  
	
}

//编辑窗口
function editCommissioningWindow(){
	$('#newform2').data('bootstrapValidator').validate();  
	if( !$('#newform2').data("bootstrapValidator").isValid() ){
		return;
	} 
	$.ajax({
		type:"post", 
		url:"/projectManage/commissioningWindow/editCommissioningWindow",
		dataType:"json",
		contentType: "application/json; charset=utf-8",
		data:JSON.stringify({
			'id':$('#id').html(),
			'windowName': $('#edit_windowName').val(),
			'typeName': $('#edit_windowType').val(),
			'windowDate': $('#edit_windowDate').val(),
		}),
		success : function(data) {
			if(data.status=='2'){
				layer.alert('所选日期已存在窗口!',{
					 icon: 2,
				})
			}else{
				layer.alert('编辑成功!',{
					 icon: 1,
				})
				$("#edit_window").modal("hide");
				pageInit();
			}
		}
	});
}

 //删除窗口
function delect( row,status ){
	var i = row.id;
	var massage='';
	if(status == 1){
        massage = '是否置为有效'
	}else{
        massage = '是否置为无效'
	}
	layer.alert(massage,{
		 icon: 0,
		 btn: ['确定','取消'] 
	},function(){
		$.ajax({
	  		url:"/projectManage/commissioningWindow/delectCommissioningWindow",
	    	method:"post", 
	    	data:{
	    		"id":i,
				"status":status
			}, 
	  		success:function(data){  
	  			pageInit();
	  			layer.alert('操作成功!',{
	 				 icon: 1,
	 			})
	        }, 
	 	}); 
	}) 
}

//显示新增窗口Model
function addWindow(type) {
	$('#add_windowName').val("");
	$('#add_windowType').val(type);
	$('#add_windowDate').val("");
	$("#newform").data('bootstrapValidator').destroy(); 
    formValidator();
    $("#add_window").modal("show");
}


//新增窗口
function addCommissioningWindow(){  
	$('#newform').data('bootstrapValidator').validate();  
	if( !$('#newform').data("bootstrapValidator").isValid() ){
		return;
	}
	if( $('#add_windowDate').val()=='' ){
		layer.alert('窗口日期不能为空!',{
			 icon: 0,
		})
		return ;
	}
	if($('#add_windowType').val()=="routine"){
		$.ajax({
			type:"post", 
			url:"/projectManage/commissioningWindow/insertRoutineCommissioningWindow",
			dataType:"json",
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify({
				'windowName': $('#add_windowName').val(),
				'typeName': '常规',
				'windowDate': $('#add_windowDate').val(),
			}),
			success : function(data) {
                if (data.status == "2") {
                    layer.alert('所选日期已存在窗口!', {
                        icon: 2,
                    })
                } else if (data.status == "3"){
                    layer.alert('新增窗口失败!', {
                        icon: 2,
                    })
			 	}else{
					layer.alert('新增常规窗口成功!',{
						 icon: 1,
					},function(index){
						$("#add_window").modal("hide"); 
						layer.close(index);
						searchInfo();
					})
				}
			}
		});  
	}else{
		$.ajax({
			type:"post", 
			url:"/projectManage/commissioningWindow/insertUrgentCommissioningWindow",
			dataType:"json",
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify({
				'windowName': $('#add_windowName').val(),
				'typeName': '紧急',
				'windowDate': $('#add_windowDate').val(),
			}),
			success : function(data) {
				if(data.status=="2"){
					layer.alert('所选日期已存在窗口!',{
						 icon: 2,
					})
				}else if(data.status == "3"){
                    layer.alert('新增窗口失败!', {
                        icon: 2,
                    })
                }else{
					layer.alert('新增紧急窗口成功!',{
						 icon: 1,
					},function(index){
						$("#add_window").modal("hide"); 
						layer.close(index);
						searchInfo();
					})
				}
			}
		});
	}
	
}

//关联开发任务弹窗
function relation(row){
	$("#featureModal").modal("show");
	$("#requirementCode").val('');
	$("#requirementName").val('');
	$("#featureName").val('');
	$("#requirementFeatureStatus").selectpicker('val','02');
	$("#na").html(row.windowName);
	wid = row.id;
	searchFeature();
}
 
//关联开发任务的弹框数据查询
function searchFeature(){  
	//从搜索下拉框中获取开发任务状态，用于在列表中将code转换为具体的名称
	var requirementFeatureStatusList = $("#requirementFeatureStatus").find("option");
	$("#loading").css('display','block');
	$("#featureTable").bootstrapTable('destroy');
    $("#featureTable").bootstrapTable({  
    	url:"/projectManage/commissioningWindow/selectRequirement",
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
            	 requirementCode:$.trim($("#requirementCode").val()),
            	 requirementName:$.trim($("#requirementName").val()),
            	 featureName:$.trim($("#featureName").val()),
            	 requirementFeatureStatus:$("#requirementFeatureStatus").val(),
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
            field : "systemName",
            title : "系统名称",
            align : 'left'
        },{
            field : "requirementCode",
            title : "需求编号名称",
            align : 'left',
            formatter: function (value,row,index){
            	console.log(row);
            	if(row.requirementCode != null && row.requirementName != null){
            	return row.requirementCode+" | "+row.requirementName;
            	}
            }
        },{
            field : "featureCode",
            title : "开发任务编号名称",
            align : 'left',
            formatter: function (value,row,index){
            	console.log(row);
            	return row.featureCode+" | "+row.featureName;
            }
        },{
            field : "requirementFeatureStatus",
            title : "开发任务状态",
            align : 'center',
            formatter: function (value,row,index){
            	for (var i = 0,len = requirementFeatureStatusList.length;i < len;i++) {
                    if(row.requirementFeatureStatus == requirementFeatureStatusList[i].value){
                        return requirementFeatureStatusList[i].innerHTML;
                    }
                }
            }
        }],
        onLoadSuccess:function(data){
        	$("#loading").css('display','none');
        	$("#featureModal").modal("show");
        	if (data.status == 2) {
        		layer.alert(data.errorMessage, {icon: 2});
        	}
        },
        onLoadError : function() {

        }
    });
}

////关联开发任务弹窗搜索
//function searchFeature(){
//	 
//}

function clearSearch2(){
	$("#requirementCode").val("");
	$("#requirementName").val("");
	$("#featureName").val("");
	$('#requirementFeatureStatus').selectpicker('val', '02');
	searchFeature();
}

//选择关联任务确定
function relatedFeature(){ 
	idArr=[];
	var flag=0;
	$(".jobArea").empty();
	var rows=$('#featureTable').bootstrapTable('getAllSelections'); 
	if( rows.length==0 ){
		layer.alert('请选择至少一个系统!',{
				 icon: 0,
		}) 
		return ;
	}
	for(var i=0;i<rows.length;i++){
		var obj={};
		obj.commissioningWindowId=rows[i].commissioningWindowId; 
		obj.id=rows[i].id;
	    idArr.push( obj );  
	}  
	for( var i=0;i<rows.length;i++ ){
		if( rows[i].commissioningWindowId != null ){ 
			 flag=1;
		} 
	} 
	if( flag==0 ){
		confirmCommission();
		return;
	}
	for( var i=0;i<rows.length;i++ ){
		if( rows[i].commissioningWindowId != null  ){
			var str='';
			str+='<div class="oneJob">'+rows[i].featureCode+' | '+rows[i].featureName +' &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp'+rows[i].windowDate +'投产</div>';
			$(".jobArea").append( str );
		} 
	} 
	$("#confirmModel").modal("show");
}

//确认修改关联开发任务
function confirmCommission(){  
	var id=[];
	for( var i=0;i<idArr.length;i++ ){ 
		id.push( idArr[i].id );
	} 
	$.ajax({
		type:"post", 
		url:"/projectManage/commissioningWindow/relationRequirement",
		dataType:"json",
		data:{
			'windowId': wid,
			'ids': id,
		},
		success : function(data) {
			layer.alert('修改成功!',{
				 icon: 1,
			},function(index){
				$("#confirmModel").modal("hide"); 
				$("#featureModal").modal("hide"); 
				layer.close(index);
			})  
		}
	});
}

 

//窗口类型下拉框的显示
function showWindowType(){
	$.ajax({
		url:"/projectManage/commissioningWindow/selectWindowType",
		method:"post",
		dataType:"json",
		success: function(data) {
			var list = data.data;
			 for (var i = 0; i < list.length; i++) {       
				 //先创建好select里面的option元素       
				 var option = document.createElement("option");         
				 //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				 $(option).val(list[i]);            
				 //给option的text赋值,这就是你点开下拉框能够看到的东西          
				 $(option).text(list[i]);              
				 //获取select 下拉框对象,并将option添加进select            
				 $('#windowType').append(option); 
				 }
			 for (var i = 0; i < list.length; i++) {       
				 //先创建好select里面的option元素       
				 var option = document.createElement("option");         
				 //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				 $(option).val(list[i]);            
				 //给option的text赋值,这就是你点开下拉框能够看到的东西          
				 $(option).text(list[i]);              
				 //获取select 下拉框对象,并将option添加进select            
				 $("#add_windowType").append(option);
			 }
			 for (var i = 0; i < list.length; i++) {       
				 //先创建好select里面的option元素       
				 var option = document.createElement("option");         
				 //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				 $(option).val(list[i]);            
				 //给option的text赋值,这就是你点开下拉框能够看到的东西          
				 $(option).text(list[i]);              
				 //获取select 下拉框对象,并将option添加进select            
				 $("#edit_windowType").append(option);
			 }
			 
			 $('.selectpicker').selectpicker('refresh'); 
			 }
		
	}); 
}

function showFeatureStatus(){
	$.ajax({
		url:"/projectManage/commissioningWindow/selectFeatureType",
		method:"post",
		dataType:"json",
		success: function(data) {
			var list = data.data;
			 for (var i = 0; i < list.length; i++) {       
				 //先创建好select里面的option元素       
				 var option = document.createElement("option");         
				 for (var key in list[i]) {
					 //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值    
					 $(option).val(key);            
					 //给option的text赋值,这就是你点开下拉框能够看到的东西          
					 $(option).text(list[i][key]);              
				 }
				 //获取select 下拉框对象,并将option添加进select            
				 $('#requirementFeatureStatus').append(option); 
				 }
			 
			 $('.selectpicker').selectpicker('refresh'); 
			 }
		
	}); 
}
 

//新增窗口表单校验
function formValidator(){ 
   $('#newform').bootstrapValidator({
　　　  message: '输入有误',//通用的验证失败消息
	    feedbackIcons: {//根据验证结果显示的各种图标
　　　　　　　valid: 'glyphicon glyphicon-ok',
　　　　　　　invalid: 'glyphicon glyphicon-remove',
　　　　　　　validating: 'glyphicon glyphicon-refresh'
　　　  },
       fields: {
    	   add_windowName: {
			    validators: {
			        notEmpty: {
			            message: '窗口名称不能为空'
			        } ,
			        stringLength: { 
                      max:300,
                      message: '窗口名称长度必须小于300字符'
                   }  
			    }
			},
			add_windowType: {
				validators: {
					notEmpty: {
			            message: '窗口类型不能为空'
			        } 
			    }
			} 
       }
   }); 
}


//编辑窗口表单校验
function formValidator2(){ 
	$('#newform2').bootstrapValidator({
		message: '输入有误',//通用的验证失败消息
		feedbackIcons: {//根据验证结果显示的各种图标
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			edit_windowName: {
				validators: {
					notEmpty: {
						message: '窗口名称不能为空'
					} ,
					stringLength: { 
						max:300,
						message: '窗口名称长度必须小于300字符'
					}  
				}
			} 
		}
	}); 
}
