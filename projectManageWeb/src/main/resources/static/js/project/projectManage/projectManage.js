$(function(){ 
    pageInit();  
    dateComponent();
    showDeptName();
//    $("#select_Correlation").modal("show");
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
    $('input[type=text]').parent().css("position","relative");
    $('input[type=text]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $("input[type=text]").bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    })
});

//表格数据加载
function pageInit(){
    $("#list2").jqGrid({
        url:'/projectManage/project/selectProjects',
        datatype: 'json', 
        mtype:"POST",
        height: 'auto',
        width: $(".content-table").width()*0.999,
        colNames:['id','项目编号','项目名称','项目类型','计划起止日期','项目状态','牵头部门','操作'],
        colModel:[ 
            {name:'id',index:'id',hidden:true}, 
            {name:'projectCode',index:'projectCode',formatter : function(value, grid, rows) {
              	 var row = JSON.stringify(rows).replace(/"/g, '&quot;');
               	 return "<div class='' onclick='showDevTask("+row+")'>"+rows.projectCode+"</div>";}},
            {name:'projectName',index:'projectName',formatter : function(value, grid, rows) {
              	 var row = JSON.stringify(rows).replace(/"/g, '&quot;');
               	 return "<div class='' onclick='showDevTask("+row+")'>"+rows.projectName+" <span class='pjNameType'></span></div>";}},
            {name:'projectTypeName',index:'projectTypeName'},
            {name:'planStartDate',index:'planStartDate',formatter : function(value, grid, rows) {
              	 var row = JSON.stringify(rows).replace(/"/g, '&quot;');
               	 return rows.planStartDate+'-'+rows.planEndDate;}},
            {name:'projectStatusName',index:'projectStatusName'},
            {name:'deptName',index:'deptName'},
            {name:'操作',index:'操作',align:"center",fixed:true,sortable:false,resize:false,search: false,
            	formatter : function(value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                    return '<a class="a_style" onclick="edit('+ row + ')">编辑</a> | <a class="a_style" onclick="guanli('+ row + ')">管理</a>'
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
    }).trigger("reloadGrid"); 
}

function presonTableShow(){  
	$("#presonTable").bootstrapTable('destroy');
    $("#presonTable").bootstrapTable({  
    	url:"/projectManage/project/selectSystemInfo",
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
            	 systemName:$("#SCsystemName").val(),
            	 systemCode:$("#SCsystemCode").val(),
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
            visable : false,
            align : 'center'
        },{
            field : "systemCode",
            title : "子系统编码",
            align : 'center'
        },{
            field : "systemShortName",
            title : "子系统简称",
            align : 'center'
        },{
            field : "systemName",
            title : "子系统名称",
            align : 'center'
        },{
            field : "systemType",
            title : "子系统分类",
            align : 'center'
        },{
            field : "归属组织",
            title : "归属组织",
            align : 'center'
        }],
        onLoadSuccess:function(){
        	 
        },
        onLoadError : function() {

        }
    });
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
	$("#time").daterangepicker({'locale': locale});
	$("#add_time").daterangepicker({'locale': locale});
}

//清空搜索内容
function clearSearch() {
    $('#projectCode').val("");
    $("#projectName").val("");
    $('#projectType').selectpicker('val', '');
    $('#projectStatus').selectpicker('val', '');
   
    $("#time").val("");
    $("#projectTarget").val("");  
    $("#projectOverview").val("");
    $("#userName").val(""); 
}
//清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}

//查询信息
function searchInfo(){ 
	$("#list2").jqGrid('setGridParam',{ 
        postData:{
        	 "projectCode" : $("#projectCode").val(),
             "projectName" :  $("#projectName").val(),
             "projectTypeName" :  $("#projectType").val(),
             "projectStatusName" :  $("#projectStatus").val(),
             "time" :  $("#time").val(),
             "projectTarget" :  $("#projectTarget").val(),
             "projectOverview" :  $("#projectOverview").val(),
             "userName" :  $("#userName").val()
        },  
        page:1
    }).trigger("reloadGrid"); //重新载入
}


//显示
function showDevTask(row) {
	var id = row.id;
	$.ajax({
		url:"/projectManage/project/selectProjectById",
		type:"post",
		dataType:"json",
		data:{
			"id":id
		},
		success: function(data) {
			$("#checkdevTaskTitle").text( "编号"+" | "+data.featureName ); 
			$("#checkdevTaskOverview").text(data.featureOverview);
			$("#checkdevTaskStatus").text();
			$("#checkdevManPost").text();
			$("#checkexecutor").text();
			$("#checksystemName").text(data.systemName);
			$("#checkoutrequirement").text(data.requirementName);
			$("#checkdeptName").text();
			$("#checktemporaryTask").text();
			$("#checkpreStartDate").text();
			$("#checkpreEndDate").text();
			$("#checkpreWorkload").text();
			$("#checkactStartDate").text();
			$("#checkactEndDate").text();
			$("#checkactWorkload").text();
			$("#checkhandSug").text(); 
		}
		
			
		
	});
	
	
	
	
    
}
//显示新增人员Model
function addProject() {
    $("#add_preject").modal("show");
}
 
//
function selectCorrelation(){
	$("#select_Correlation").modal("show");
	presonTableShow();
}
 
//新增页面的所属部门下拉框的显示
function showDeptName(){
	$.ajax({
		url:"/projectManage/project/selectAllDeptName",
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
				 $('#deptName').append(option); 
				 }
			 	 $('.selectpicker').selectpicker('refresh'); 
			 }
		
	});
	$.ajax({
		url:"/projectManage/project/selectTypeAndStatusList",
		method:"post",
		dataType:"json",
		data:{
			"termCode":"TBL_PROJECT_INFO_PROJECT_STATUS"
		},
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
				 $('#projectStatus').append(option);           
				 }
			 $('.selectpicker').selectpicker('refresh'); 
			 }
		
	}); 	
	console.log(  111 );
	$.ajax({
		url:"/projectManage/project/selectTypeAndStatusList",
		method:"post",
		dataType:"json",
		data:{
			"termCode":"TBL_PROJECT_INFO_PROJECT_TYPE"
		},
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
				 $('#projectType').append(option);           
				 }
			 	for (var i = 0; i < list.length; i++) {       
				 //先创建好select里面的option元素       
				 var option = document.createElement("option");         
				 //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				 $(option).val(list[i]);            
				 //给option的text赋值,这就是你点开下拉框能够看到的东西          
				 $(option).text(list[i]);              
				 //获取select 下拉框对象,并将option添加进select            
				 $('#add_projectType').append(option);           
				 } 	
			 $('.selectpicker').selectpicker('refresh'); 
			 }
		
	}); 
	 
}
 
