
$(function(){ 
	formValidator();
    dateComponent();
    showDeptName(); 
    pageInit();
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

    $('#template').change(function(){
        var val = $(this).val();
        $("#tb1").jqGrid('setGridParam',{
            url:"/projectManage/userPostPower/getRoleMenu",
            postData:{
                id:val
            },
            page:1
        }).trigger("reloadGrid");
    })
});

//wiki链接跳转页面
function toWiki(ID,projectName){
	var content_arr = {
      menuButtonId: '9001',
      menuButtonName: 'wiki',
      url: '/projectManageui/oamproject/toWiki?id=' + ID+'&projectName=' + projectName
    }
	window.parent.toPageAndValue(content_arr.menuButtonName, content_arr.menuButtonId, content_arr.url);
}

//表格数据加载
function pageInit(){
	$("#loading").css('display','block');
    $("#list2").jqGrid({
        url:'/projectManage/oamproject/selectOamProject',
        datatype: 'json', 
        mtype:"POST",
        height: 'auto',
        width: $(".content-table").width()*0.999,
        colNames:['id','项目组名称','项目状态','涉及系统','项目管理岗','开发管理岗','操作'],
        postData:{
        	 "projectStatusName" :  $("#projectStatusName").val(),	
        	 "projectName" :  $.trim($("#projectName").val()),
             "projectManageName" :  $.trim($("#projectManageName").val()),
             "developManageName" :  $.trim($("#developManageName").val()),
        },
        colModel:[ 
            {name:'id',index:'id',hidden:true}, 
            {name:'projectName',index:'projectName',formatter : function(value, grid, rows) {
              	 var row = JSON.stringify(rows).replace(/"/g, '&quot;');
               	 return "<a class='a_style' onclick='showOamproject("+row+")'>"+rows.projectName+" <span class='pjNameType'></span></a>";}},
            {name:'projectStatusName',index:'projectStatusName'},
            {name:'systemName',index:'systemName'},
            {name:'projectManageUsers',index:'projectManageUsers'},
            {name:'developManageUsers',index:'developManageUsers'},
            {name:'操作',index:'操作',align:"center",fixed:true,sortable:false,resize:false,search: false,width:350,
            	formatter : function(value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');
//                    return '<a class="a_style" onclick="edit('+ row + ')">编辑</a> | <a class="a_style" onclick="end('+ row + ')">结束项目</a>'
                    var span_ = "<span>&nbsp;|&nbsp;</span>";
                    var a = '<a class="a_style" onclick="edit('+ row + ')">编辑</a>';
                    var c = '<a class="a_style" onclick="authority('+ rows.id + ')">岗位权限</a>';
                    var b = '<a class="a_style" onclick="end('+ row + ')">结束项目</a>';
                    var d = '<a class="a_style" onclick="_versions('+row + ',1)">版本管理</a>';
                    var e = '<a class="a_style" onclick="toRisk('+ rows.id + ",\'"+ rows.projectName +"\'"+')">风险管理</a>';
                    var f = '<a class="a_style" onclick="toWiki('+ rows.id + ",\'"+ rows.projectName +"\'"+')">wiki</a>';
                    var opt_status = [];
                    //拥有【编辑】权限
                    if(projectEdit == true){
                    	opt_status.push(a);
                    }
                    //拥有【岗位权限】权限
                    if(userPost == true)
                    {
                        opt_status.push(c);
                    }
                    //拥有【结束项目】权限
                    if(projectEnd == true){
                    	opt_status.push(b);
                    }
                    //所有人都能版本管理
                    opt_status.push(d);
                    //拥有【风险管理】权限
                    if(toRiskPer == true){
                        opt_status.push(e);
                    }
                    
                    //拥有【wiki】权限
                    if(toWikiButton == true){
                    	opt_status.push(f);
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

//【新增项目】-关联子系统弹窗页面【搜索】事件
function presonTableShow(){  
	var arr=[]; 
	$("#loading").css('display','block');
	for( var i=0;i<$("#relevanceSys .addSysNames").length;i++ ){  
		arr.push( Number( $("#relevanceSys .addSysNames").eq(i).attr("value") ) );
	}
	if( arr.length==0 ){
		arr.push(0) 
	}
	$("#presonTable").bootstrapTable('destroy');
    $("#presonTable").bootstrapTable({  
    	url:"/projectManage/oamproject/selectSystemInfo",
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
                 systemIds:arr,
             }
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px"
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
        	$("#loading").css('display','none');	
        },
        onLoadError : function() {

        }
    });
}

//【新增项目】-项目周期时间格式
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
	$("#add_time").daterangepicker({'locale': locale});
}


//清空搜索内容
function clearSearch() {
    $('#projectName').val("");
    $('#projectManageName').val("");
    $('#developManageName').val("");
    $('#projectStatusName').selectpicker('val', '');
    var select=$('#projectStatusName').find('option'); 
    select.first().attr("selected","true")
    $('.selectpicker').selectpicker('refresh'); 
    //    pageInit(); 
    //searchInfo();
}

//清空关联系统搜索内容
function clearSearch2() {
    $('#SCsystemName').val("");
    $("#SCsystemCode").val(""); 
    presonTableShow();
}


//清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}

//查询信息
function searchInfo(){ 
	$("#loading").css('display','block');
	$("#list2").jqGrid('setGridParam',{ 
        postData:{
             "projectName" :  $.trim($("#projectName").val()),
             "projectManageName" :  $.trim($("#projectManageName").val()),
             "developManageName" :  $.trim($("#developManageName").val()),
             "projectStatusName" :  $("#projectStatusName").val(),
        },  
        page:1
    }).trigger("reloadGrid"); //重新载入
}


//点击名称显示项目详情
function showOamproject(row) {
	var i = row.id;
	window.location.href ="/projectManageui/oamproject/toEditProject?id=" + i + "&type=2";
}

//点击编辑
function edit( row ){ 
	var i = row.id;  
	window.location.href ="/projectManageui/oamproject/toEditProject?id=" + i + "&type=1";
}
 
//结束项目
function end( row ){
	var i = row.id;
		layer.alert('确认要结束此项目？',{
			 icon: 0,
			 btn: ['确定','取消'] 
		},function(){
			$.ajax({
		  		url:"/projectManage/oamproject/endProject",
		    	method:"post", 
		    	data:{
		    		"id":i
				}, 
		  		success:function(data){  
//		  			pageInit();
//		  			layer.alert('项目已结束!',{
//		 				 icon: 1,
//		 			})
		 			if( data.status == 1 ){
						 layer.alert("项目已结束!",{icon : 1});
						 pageInit();
					 }if(data.status == 2){
						 layer.alert("操作失败",{icon : 2});
						 pageInit();
					 }
		        }, 
		 	}); 
		})  
}

// 风险管理
function toRisk(id,name) {
    $("#loading").css('display', 'block');
    layer.open({
        type: 2,
        title: false,
        shadeClose: true,
        shade: false,
        move: false,
        area: ['100%', '100%'],
        tipsMore: true,
        anim: 2,
        btn: ['关闭'] ,
        btnAlign: 'c', //按钮居中
        content: "/projectManageui/oamproject/toRisk?id=" + id+"&name="+name,
        success: function (layero, index) {
            $("#loading").css("display", "none");
        }
    });
}

//显示新增项目Model
function addProject() {
	$('#add_projectName').val("");
	$('#add_projectCode').val("");
	$('#add_projectOverview').val("");
	$('#add_time').val("");
	$('#add_longTimeStatus').attr("checked",'true');
	$('#relevanceSys').empty();
	$("#newform").data('bootstrapValidator').destroy();
    formValidator();
    $("#add_project").modal("show");
}

//获取所有表格数据//新增项目
function addPrejectCommit(){  
	$('#newform').data('bootstrapValidator').validate();  
	if( !$('#newform').data("bootstrapValidator").isValid() ){
		return;
	} 
	
	var time,st,et,status;
	var systemIdArr=[];
	time=$("#add_time").val();
	if(time != ''){
		var timeArr=time.split(' - ');
		console.log( timeArr );
		st=timeArr[0];
		et=timeArr[1];
	}
	if( $("#add_longTimeStatus").is(':checked') ){
		status=1;
	}else{
		status=2;
	}
//	for(var i=0;i<$("#relevanceSys").children( ".addSysNames" ).children( ".addSysNames_span" ).length;i++){ 
//		nameArr.push( $("#relevanceSys").children( ".addSysNames" ).children( ".addSysNames_span" ).eq(i).text() );
//	} 
	for( var i=0;i<$("#relevanceSys .addSysNames").length;i++ ){  
		systemIdArr.push( Number( $("#relevanceSys .addSysNames").eq(i).attr("value") ) );
	}
	$.ajax({
		type:"post", 
		url:"/projectManage/oamproject/insertOamProject",
		dataType: "json",
		data:{
			"tblProjectInfo":JSON.stringify({
				'projectName': $('#add_projectName').val(),
				'projectCode': $('#add_projectCode').val(),
				'projectTypeName': $('#add_projectType').val(),
				'projectOverview': $('#add_projectOverview').val(),
				'planStartDate': st,
				'planEndDate': et,
				'longTimeStatus': status,
				'systemIds': systemIdArr
			})
		},
		success : function(data) {
//			layer.alert('新增成功!',{
//				 icon: 1,
//			},function(index){
//				$("#add_project").modal("hide"); 
//				layer.close(index);
//				searchInfo();
//			})
			if( data.status == 1 ){
				 layer.alert("新增成功",{icon : 1});
				 $("#add_project").modal("hide");
				 searchInfo();
			 }if(data.status == 2){
				 layer.alert("新增失败",{icon : 2});
				 $("#add_project").modal("hide");
				 searchInfo();
			 }
		}
	});  
}
 
//【新增项目】关联系统页面的显示
function selectCorrelation(e){
	$("#select_Correlation").modal("show");
	if (e && e.stopPropagation) { 
  	 	e.stopPropagation();
    } else if (window.event) {  
    	window.event.cancelBubble = true;
    } 
	presonTableShow();
}
 
//搜索栏的状态下拉框
function showDeptName(){
	$.ajax({
		url:"/projectManage/oamproject/selectStatusName",
		method:"post",
		async:false, 
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
				 $('#projectStatusName').append(option);           
				 }
			 $('.selectpicker').selectpicker('refresh'); 
			 }
		
	}); 
}

//废弃，参照editProject.js中对应方法
function insertRoleUser(){ 
	 var rows=$('#presonTable') .bootstrapTable('getAllSelections');  
	 for(var i=0;i<rows.length;i++){
		 var obj='<div class="addSysNames" value="'+rows[i].id+'" ><span class="addSysNames_span">'+rows[i].systemName+'</span>'+
		 '<span class="close_x" onclick="delAddSysName(this,event)">×</span></div>';
		 $("#relevanceSys").append(obj);
		 
	 }
	 $("#select_Correlation").modal("hide");
} 
function delAddSysName(This,e){
	$(This).parent(".addSysNames").remove();
	stopPro(e);
} 
//阻止事件冒泡
function stopPro(e){
	if (e && e.stopPropagation) { 
  	 	e.stopPropagation();
    } else if (window.event) {  
    	window.event.cancelBubble = true;
    } 
}
 
//表单校验
function formValidator(){ 
   $('#newform').bootstrapValidator({
　　　  message: '输入有误',//通用的验证失败消息
	    feedbackIcons: {//根据验证结果显示的各种图标
　　　　　　　valid: 'glyphicon glyphicon-ok',
　　　　　　　invalid: 'glyphicon glyphicon-remove',
　　　　　　　validating: 'glyphicon glyphicon-refresh'
　　　  },
       fields: {
    	   add_projectName: {
			    validators: {
			        notEmpty: {
			            message: '项目组名称不能为空'
			        } ,
			        stringLength: { 
                      max:300,
                      message: '项目组名称长度必须小于300字符'
                   }  
			    }
			},
			add_projectCode: {
				validators: {
					notEmpty: {
			            message: '项目编码不能为空'
			        } ,
			        stringLength: { 
                      max:300,
                      message: '项目编码长度必须小于300字符'
                   } 
			    }
			} 
       }
   }); 
   $('#roleForm').bootstrapValidator({
       excluded : [ ':disabled' ],
       message: 'This value is not valid',//通用的验证失败消息
       feedbackIcons : {
           valid : 'glyphicon glyphicon-ok',
           invalid : 'glyphicon glyphicon-remove',
           validating : 'glyphicon glyphicon-refresh'
       },
       live : 'enabled',
       fields : {
           roleName : {
               validators : {
                   notEmpty : {
                       message : '请输入角色名称！'
                   }
               }
           }
       }
   })
    $('#addVersion_form').bootstrapValidator({
        message: 'This value is not valid',//通用的验证失败消息
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        fields : {
                add_Version : {
                    validators : {
                        notEmpty : {
                            message : '版本号不能为空'
                        }
                    }
                },
                // add_label : {
                //     validators : {
                //         notEmpty : {
                //             message : '版本分组标签不能为空'
                //         }
                //     }
                // },
                add_system : {
                    validators : {
                        notEmpty : {
                            message : '系统不能为空'
                        }
                    }
                },
        }
    })
}





//定义setTimeout执行方法
 var TimeFn = null;
 var errorDefect = '系统内部错误，请联系管理员 ！！！';
 var select_rows = new Array();
 var tb_select_rows = new Array();
 function authority(id){
     leftMenu('',id);
     // formValidator();
     // refactorFormValidator();
 //    checkSelectRows("#tb2");
 //    checkSelectRows("#userTable");
     $('#authorityModal').modal('show');
 }
 function initMenu() {
     $(".menu-ul li").find("input[type='text']").remove();
     $(".menu-ul li").find("a").show();
 } 
 
 function  setId_handle(id){
 	$.ajax({
      url:"/projectManage/userPostPower/setId",
       method:"post",
       data:{
          'id':id,
       },
 		success : function(data) {
 			initTable();
 		},
	})
 }
 
 //【岗位权限】页面数据组装
 function leftMenu(current,id) {
     $(".left-box").empty();
 	 if( current==undefined ){
 	 	current=0;
 	 }
     $("#loading").css('display','block');
 	// 条件查询未关联人员
 	$.ajax({
//         url:"/system/role/getAllRole",
         url:"/projectManage/userPostPower/getUserPostByProject",//项目岗位信息
         method:"post",
         data:{
            'projectId':id,
         },
         dataType: "json",
 		success : function(data) {
             var menuDate = JSON.parse(data.data);
             var ID = menuDate[0].id;
             setId_handle(ID);
             var _ul = $('<ul class="menu-ul"></ul>');
             for (var i = 0; i < menuDate.length; i++) {
                 var obj = menuDate[i];
                 var _li = $('<li></li>');
                 var _span = $('<span class="parent-id">' + obj.id + '</span>');
                 var _a = $('<a href="javascript:void(0);" >' + obj.roleName + '</a>');
                 _li.appendTo(_ul);
                 _span.appendTo(_li);
                 _a.appendTo(_li);
                 if (i == current) {
                     _li.addClass("current");
                 } 
             }
             /*var _add_div = $('<div class="add-menu"><span class="glyphicon glyphicon-plus fa fa-plus"></span></div>');*/
             _ul.appendTo($(".left-box"));
             //_add_div.appendTo($(".left-box"));
//             // 单击切换按钮
             $(".menu-ul li").click(function() {
                 select_rows = new Array();
                 $(".menu-ul li").removeClass("current");
                 $(this).addClass("current");
                 // 取消上次延时未执行的方法
                 clearTimeout(TimeFn);
                 //执行延时
                 TimeFn = setTimeout(function(){
                     //do function在此处写单击事件要执行的代码
                     $("#loading").css('display','block');
                     menuButton();
                     roleUserList();
                     var _id = $(".menu-ul").find(".current").children("span").text();
                     showBut(_id);
                 },300);
             });
             // 双击修改角色名称按钮
             $(".menu-ul li").dblclick(function() {
                 clearTimeout(TimeFn);
                 $("#loading").css('display','none');
                 $(this).unbind();
                 initMenu();
                 $(this).children("a").hide();
                 var _name = $(this).children("a")
                     .text();
                 var _input = $('<input type="text" value="'
                     + _name
                     + '" class="menu-input" onkeydown="editMenuName(event,this)" />');
                 _input.appendTo($(this));
                 $(this).children(".menu-input").blur(function(){
                     var _name = $(this).val();
                     var _id = $(this).prev().prev().text();
                     var current=$(this).parent().index();
                     $.ajax({
                         url : "/system/role/updateRoleName",
                         method : "post",
                         data : {
                             "id" : _id,
                             "roleName" : _name
                         },
                         success : function(data) {
                             $(this).bind();
                             $("#loading").css('display','none');
                             if (data.status == 2){
                                 layer.alert(data.errorMessage, {
                                     icon: 2,
                                     title: "提示信息"
                                 });
                             } else {
                                 layer.alert('修改成功！', {
                                     icon: 1,
                                     title: "提示信息"
                                 });
                                 leftMenu( current );
                             }
                             showBut(_id);
                         },
                         error:function(){
                             $("#loading").css('display','none');
                             layer.alert(errorDefect, {
                                 icon: 2,
                                 title: "提示信息"
                             });
                         }
                     });
                 });
             });
             // 添加按钮
             $(".add-menu").click(function() {
                 reset();
                 $("#roleModal").modal("show");
                 formValidator();
             });
             
             //菜单按钮权限
             menuButton();
//             // 关联人员
            roleUserList();
             var _id = $(".menu-ul").find(".current").children("span").text();
             showBut(_id);
	 	 	
         },
         error:function(){
             $("#loading").css('display','none');
             layer.alert(errorDefect, {
                 icon: 2,
                 title: "提示信息"
             });
         }
 	}); 
 }
 function showBut(id){
     if(id == "1"){
         $("#updateRoleBut").css("display","none");
     } else {
         $("#updateRoleBut").css("display","inline-block");
     }
 }
 function editMenuName(event, that) {
     var e = event || window.event || arguments.callee.caller.arguments[0];
     if (e.keyCode == 13) {
         var _name = $(that).val();
         var _id = $(that).prev().prev().text();
         var current=$(that).parent().index();
         $("#loading").css('display','block');
         $.ajax({
             url : "/system/role/updateRoleName",
             method : "post",
             data : {
                 "id" : _id,
                 "roleName" : _name
             },
             success : function(data) {
                 $(that).bind();
                 $("#loading").css('display','none');
                 if (data.status == 2){
                     layer.alert(data.errorMessage, {
                         icon: 2,
                         title: "提示信息"
                     });
                 } else {
                     layer.alert('修改成功！', {
                         icon: 1,
                         title: "提示信息"
                     });
                     leftMenu( current );
                 }
                 showBut(_id);
             },
             error:function(){
                 $("#loading").css('display','none');
                 layer.alert(errorDefect, {
                     icon: 2,
                     title: "提示信息"
                 });
             }
         });
     }
 }
 /**
  * 菜单按钮 jqGrid
  * @author
  */
 function initTable() {
     $.ajax({
        url:"/projectManage/userPostPower/getNoProjectRole",
        method:"post",
        dataType: "json",
        success : function(data) {
            $('#template').empty();
            $('#template').append(`<option value="">请选择</option>`);
          data.result.map(function(idx,val){
                $('#template').append(`<option value="${idx.id}">${idx.roleName}</option>`);
          })
            $('#template').selectpicker('refresh');
      }
    })
     $("#tb1").jqGrid("clearGridData");
     $("#tb1").jqGrid({
//         url:"/system/role/getRoleMenu",
         url:"/projectManage/userPostPower/getRoleMenu",
         datatype: "json", 
         contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
         height: 'auto',  
         mtype : "POST",
         cellEdit:true,   
 //        width: $(".MenuManagementAuthority").width()*0.999,
         width: 480,
         colNames:['id','菜单','按钮'], 
         colModel:[
             {name : 'id',index : 'id',hidden:true,key:true}, 
             {name : 'menu',index : 'menu',formatter : function(value, grid, rows, state){ 
             	var flag='';	
             	if( rows.isSelect==true ){
             		flag='checked';
             	}
             	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
             	return '<input type="checkbox" '+flag+' value='+rows.id+'  onclick="RelativeTreeGridCheck('+row+',this)" />&nbsp;'+rows.menu;
             }},
             {name : 'buttonList',index : 'buttonList', formatter : function(value, grid, rows, state){
                 var row = JSON.stringify(rows).replace(/"/g, '&quot;');
             	var checkBoxGroup='';
             	if(  rows.buttonList!=null ){ 
             		 for( var i=0;i<rows.buttonList.length;i++ ){
             			 var flag='';
             			 if( rows.buttonList[i].isSelect==true ){
                      		flag='checked';
                      	 }
                      	 var menuButtonName = rows.buttonList[i].menuButtonName;
             			 checkBoxGroup+='<input type="checkbox" '+flag+'  value='+rows.buttonList[i].id+'  onclick="RelativeTreeGridCheck('+row+',this)"/>&nbsp;'+menuButtonName+'&nbsp;&nbsp;';
                          var ii = i+1;
                          ii>0?ii%3==0?checkBoxGroup +='<br/>':'':'';
             		 }
             	}   
             	return checkBoxGroup; 
             }}
         ], 
         treeGrid: true,
         treeGridModel: "adjacency",
         ExpandColumn: "menu",
         treeIcons: {plus:'fa fa-caret-right',minus:'fa fa-caret-down',leaf:''},  
         sortname:"id",
         sortable:true,
         ExpandColClick: true,  
         multiselect: true,
         jsonReader: {
             repeatitems: false,
             root: "result", 
         },
         treeReader : {           //设置树形显示时4个关键字段对应的返回数据字段
             level_field: "level",      // 属性层级
             parent_id_field: "parent", //父级rowid 
             leaf_field: "isLeaf",      //是否还有子级菜单
             expanded_field: "expanded" //是否加载完毕
         },
         loadComplete :function(xhr){ 
         	$("#loading").css('display','none');
         },
         loadError:function(){
             $("#loading").css('display','none');
             layer.alert(errorDefect, {
                 icon: 2,
                 title: "提示信息"
             });
         }
     }).trigger('reloadGrid');  
 }
 function RelativeTreeGridCheck(row,That,event){
     var rowData = $('#tb1').jqGrid('getRowData', row.id,true);
     var childrenData = $('#tb1').jqGrid('getNodeChildren', rowData);
     var parentData = $('#tb1').jqGrid('getNodeParent', rowData);
     var rowChecked = $("#"+row.id).children().children().find("input").is(":checked");
     //  不为0，则为子节点，肯定有父节点，把父节点勾上
     if (childrenData.length != 0) {
         for (var i=0;i<childrenData.length;i++){
             if ( rowChecked == true ){
                 $("#"+childrenData[i].id).children().children().find("input").prop("checked",true);
             } else {
                $("#"+childrenData[i].id).children().children().find("input").removeProp("checked");
                $("#"+childrenData[i].id).children().children().removeProp("checked");
             }
         }
     } else {
         // 为0 则为父节点， 如果有子勾选，没有则不操作
         row.level != 0?$("#"+parentData.id).children().children().find("input").prop("checked",true):'';
     }
     var e = arguments.callee.caller.arguments[0]||event;
     if (e && e.stopPropagation) {
         e.stopPropagation();
     } else if (window.event) {
         window.event.cancelBubble = true;
     }
 }
 /**
  * 已关联人员 单个角色关联人员列表
  */
 function roleUserList(){
     var _id = $(".menu-ul").find(".current").children("span").text();
     /*$("#loading").css('display','block');*/
     $("#tb2").bootstrapTable('destroy'); 
     $("#tb2").bootstrapTable({
         url:"/system/role/getRoleUser",
         method:"post",
         contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
         queryParamsType:"",
         pagination : true,
         sidePagination: "server",
         pageNumber : 1,
         pageSize : 10,
         pageList : [ 10, 25, 50, 100 ],
         //clickToSelect : true, //是否启用点击选中行
         //maintainSelected : true,
         queryParams : function(params) {
             var param = {
                 pageNumber:params.pageNumber,
                 pageSize:params.pageSize,
                 id:_id
             }
             return param;
         },
         columns : [/*{
             checkbox : true,
             width : "30px",
             formatter:function(value,row,index){
                 if($.inArray(row.id,tb_select_rows) != -1){
                     return {
                         checked:true
                     }
                 }
                 return {
                     checked:false
                 }
             }
         },*/{
             field : "id",
             title : "id",
             align : 'center',
             visible : false
         },{
             field : "userName",
             title : "姓名",
             align : 'center'
         },{
             field : "userAccount",
             title : "用户名",
             align : 'center'
         },{
             field : "deptName",
             title : "所属组织",
             align : 'center'
         }],
         onLoadSuccess:function(){
             /*$("#loading").css('display','none');*/
         },
         onLoadError : function() {
             $("#loading").css('display','none');
             layer.alert(errorDefect, {
                 icon: 2,
                 title: "提示信息"
             });
         }
     });
 }
 /**
  * 未关联人员列表
  */
 function userList(){
     var roleId = $(".menu-ul").find(".current").children("span").text();
     var userName = $.trim($("#userName").val());
     var employeeNumber = $.trim($("#employeeNumber").val());
     var deptId = $.trim($("#deptName").val());
     var companyId = $.trim($("#companyName").val());
     $("#loading").css('display','block');
     $("#userTable").bootstrapTable('destroy');
     $("#userTable").bootstrapTable({
         url:"/system/role/findUserWithNoRole",
         method:"post",
         contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
         queryParamsType:"",
         pagination : true,
         sidePagination: "server",
         pageNumber : 1,
         pageSize : 10,
         pageList : [ 10, 25, 50, 100 ],
         clickToSelect : true, //是否启用点击选中行
         maintainSelected : true,
         queryParams : function(params) {
              var param={
                  roleId:roleId,
                  pageNumber:params.pageNumber,
                  pageSize:params.pageSize,
                  userName:userName,
                  employeeNumber:employeeNumber,
                  deptName:deptId,
                  companyName:companyId
              };
             return param;
         },
         columns : [{
             checkbox : true,
             width : "30px",
             formatter:function(value,row,index){
                 if($.inArray(row.id.toString(),select_rows) != -1){
                     return {
                         checked:true
                     }
                 }
                 return {
                     checked:false
                 }
             }
         },{
             field : "id",
             title : "id",
             align : 'center',
             visible : false
         },{
             field : "userName",
             title : "姓名",
             align : 'center'
         },{
             field : "userAccount",
             title : "用户名",
             align : 'center'
         },{
             field : "employeeNumber",
             title : "工号",
             align : 'center'
         },{
             field : "deptName",
             title : "所属部门",
             align : 'center'
         },{
             field : "companyName",
             title : "所属公司",
             align : 'center'
         }],
         onLoadSuccess:function(){
         	$("#loading").css('display','none');
         },
         onLoadError : function() {
             $("#loading").css('display','none');
             layer.alert(errorDefect, {
                 icon: 2,
                 title: "提示信息"
             });
         },
         onPageChange:function(){
             $("#loading").css('display','block');
         }
     });
 }
 function updateRoleBtn(){
     var idArr=[];
     $("#tb1 input:checkbox:checked").each(function(){
         idArr.push(Number( $(this).val() ))
     });
 	$("#loading").css('display','block');
 	  $.ajax({
 	        url:"/system/role/updateRoleMenu",
 	        method:"post",
 	        dataType: "json",
 	        traditional:true,
 	        data: {
 	        	id:$(".menu-ul").find(".current").children("span").text(),
 	        	menuIds:idArr
 	        },
 	        success:function(data){
 	        	$("#loading").css('display','none');
 	        	if (data.status == 2){
                     layer.alert(data.errorMessage, {
                         icon: 2,
                         title: "提示信息"
                     });
                 } else {
                     layer.alert('修改成功！', {
                         icon: 1,
                         title: "提示信息"
                     });
                     select_rows = new Array();
                     $("#tb2").bootstrapTable('refresh');
                     menuButton(); 
                 }   
 	        },
             error:function(){
               $("#loading").css('display','none');
               layer.alert(errorDefect, {
                   icon: 2,
                   title: "提示信息"
               });
 	        }
 	    })
 }
 // 取消按钮
 function resetRoleBtn(){
     select_rows = new Array();
     $("#tb2").bootstrapTable('refresh');
     menuButton();
 }
 // 条件查询未关联人员
 function findUserWithNoRole(){
     userList()
 }  
 // 添加角色
 function insertRole(){
     $("#roleForm").data('bootstrapValidator').validate();
     if ( !$("#roleForm").data('bootstrapValidator').isValid() ) {
         return false;
     }
     $("#loading").css('display','block');
     $.ajax({
         url:"/system/role/insertRole",
         method:"post",
         dataType: "json",
         contentType:"application/json;charset=utf-8",
         data:JSON.stringify({
             roleName:$.trim($("#roleName").val()),
             status:1
         }),
         success:function(data){
             $("#loading").css('display','none');
             if (data.status == 2){
                 layer.alert(data.errorMessage, {
                     icon: 2,
                     title: "提示信息"
                 });
                 // window.location.href = "/systemui/error/500";
             } else if (data.result == true){
                 leftMenu();
                 $("#roleModal").modal("hide");
             } else{
                 layer.alert("该角色已存在,请勿重复添加！", {
                     icon: 2,
                     title: "提示信息"
                 });
             }
         },
         error:function(){
             $("#loading").css('display','none');
             layer.alert(errorDefect, {
                 icon: 2,
                 title: "提示信息"
             });
         }
     })
 }
 // 单个角色获取菜单按钮
 function menuButton(){
     $("#loading").css('display','block');
 	 $("#tb1").jqGrid("clearGridData");
 	 $("#tb1").jqGrid('setGridParam',{ 
 		 	url:"/projectManage/userPostPower/getRoleMenu",
 	        postData:{
 	       	     id:$(".menu-ul").find(".current").children("span").text()
 	        },  
 	        page:1
 	}).trigger("reloadGrid");  
 }
 // 单个角色添加未关联人员
 // function insertRoleUser(){
 //     var roleId = $(".menu-ul").find(".current").children("span").text();
 //     if (typeof(select_rows) == 'undefined' || select_rows.length <= 0){
 //         layer.alert("请选中一行", {
 //             icon: 2,
 //             title: "提示信息"
 //         });
 //     } else {
 //         var userId=[];
 //         for (var i = 0; i < select_rows.length; i++) {
 //             userId.push(select_rows[i]);
 //         }
 //         $.ajax({
 //             url:"/system/role/insertRoleUser",
 //             method:"post",
 //             traditional:true,
 //             data:{
 //                 userId:userId,
 //                 id:roleId
 //             },
 //             success:function(data){
 //                 if (data.status == 2){
 //                     layer.alert(data.errorMessage, {
 //                         icon: 2,
 //                         title: "提示信息"
 //                     });
 //                 } else {
 //                     layer.alert('添加成功！', {
 //                         icon: 1,
 //                         title: "提示信息"
 //                     });
 //                     select_rows = new Array();
 // //                    findUserWithNoRole();
 //                     roleUserList();
 //                 }
 //             },
 //             error:function(){
 //                 $("#loading").css('display','none');
 //                 layer.alert(errorDefect, {
 //                     icon: 2,
 //                     title: "提示信息"
 //                 });
 //             }
 //         })
 //     }
 // }
 // 弹出关联人员弹框
 function userModel(){
     reset();
     // findUserWithNoRole();
     $("#userTable").bootstrapTable('destroy');
     $("#userModal").modal("show");
 }
 // 取消关联人员
 function disassociate(){
     var roleId = $(".menu-ul").find(".current").children("span").text();
     if (typeof(tb_select_rows) == 'undefined' || tb_select_rows.length <= 0){
         layer.alert("请选中一行", {
             icon: 2,
             title: "提示信息"
         });
     } else {
         var userId = [];
         for(var i = 0; i < tb_select_rows.length;i++){
             userId.push(tb_select_rows[i]);
         }
         $("#loading").css('display','block');
         $.ajax({
             url:"/system/role/updateRoleWithUser",
             method:"post",
             traditional:true,
             data:{
                 userId:userId,
                 roleId:roleId
             },
             success:function(data){
                 $("#loading").css('display','none');
                 if (data.status == 2){
                     layer.alert(data.errorMessage, {
                         icon: 2,
                         title: "提示信息"
                     });
                     // window.location.href = "/systemui/error/500";
                 } else {
                     layer.alert('取消关联成功！', {
                         icon: 1,
                         title: "提示信息"
                     });
                     tb_select_rows = new Array();
                     roleUserList();
                 }
             },
             error:function(){
                 $("#loading").css('display','none');
                 layer.alert(errorDefect, {
                     icon: 2,
                     title: "提示信息"
                 });
             }
         })
     }
 }
 // 置为无效
 function updateRole(){
     var roleId = $(".menu-ul").find(".current").children("span").text();
     var roleName = $(".menu-ul").find(".current").children("a").text();
     var _roleName = '<span style="color: red">'+ roleName +'</span>';
     layer.confirm('您确定要置  '+ _roleName +'  角色为无效吗？', {
         btn: ['确定','取消'], //按钮
         title: "提示信息"
     }, function(){
         layer.closeAll('dialog');
         $("#loading").css('display','block');
         $.ajax({
             url:"/system/role/updateRole",
             method:"post",
             data:JSON.stringify({
                 id:roleId,
                 status:2
             }),
             contentType:'application/json;charset=UTF-8',
             success:function(data){
                 $("#loading").css('display','none');
                 if (data.status == 2){
                     layer.alert(data.errorMessage, {
                         icon: 2,
                         title: "提示信息"
                     });
                 } else {
                     layer.alert('该角色置为无效成功！', {
                         icon: 1,
                         title: "提示信息"
                     });
                     findUserWithNoRole();
                     select_rows = new Array();
                     leftMenu();
                 }
             },
             error:function(){
                 $("#loading").css('display','none');
                 layer.alert(errorDefect, {
                     icon: 2,
                     title: "提示信息"
                 });
             }
         });
     })
 }
 // 清空表格内容
 function clearContent( that ){
     $(that).parent().children('input').val("");
     $(that).parent().children(".btn_clear").css("display","none");
 }
// function clearSearch(){
//     reset();
//     userList();
// }
 function reset(){
     select_rows = new Array();
     $("#userName").val('');
     $("#employeeNumber").val('');
     $("#deptName").val('');
     $("#companyName").val('');
     $("#roleName").val("");
     $(".btn_clear").css("display","none");
 }

  
 

 /**
  * 重构表单验证
  */
 function refactorFormValidator(){
     $('#roleModal').on('hidden.bs.modal', function() {
         $("#roleForm").data('bootstrapValidator').destroy();
         $('#roleForm').data('bootstrapValidator', null);
         formValidator();
     })
 }



 function checkSelectRows(id){
     $(id).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
         var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
         examine(e.type,datas,id);                              // 保存到全局 Array() 里
     });
 }
 function examine(type,datas,id){
     if(type.indexOf('uncheck')==-1){
         $.each(datas,function(i,v){
             // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
             select_rows.indexOf(v.id.toString()) == -1 ? select_rows.push(v.id.toString()) : -1;
             if(id == "#tb2"){
                 tb_select_rows.indexOf(v.id.toString()) == -1 ? tb_select_rows.push(v.id.toString()) : -1;
             }
         });
     }else{
         $.each(datas,function(i,v){
             select_rows.splice(select_rows.indexOf(v.id.toString()),1);    //删除取消选中行
             if(id == "#tb2"){
             	tb_select_rows.splice(tb_select_rows.indexOf(v.id.toString()),1);    //删除取消选中行
             }
         });
     }
 }
