/**
 * Created by 朱颜辞镜花辞树 on 2018/9/18.
 */
$(function(){
	
	//页面初始化相关动作
	$("#loading").css('display','block');
	//表单校验
	formValidator();
	//获取所属公司下拉列表
	getCompany();
	//获取部门下拉列表
	getDept();
	//获取项目组下拉列表
	getAllproject();
	//获取角色信息
	getRoles();
	//数据加载
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
    })
  //datetimepicker 时间控件初始化
    $("#startWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    });
    $("#endWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    });
    $("#newBirthday").datetimepicker({
        minView:"year",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    });
    $("#new_startWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    });
    $("#editBirthday").datetimepicker({
    	minView:"month",
    	format: "yyyy-mm-dd",
    	autoclose: true,
    	todayBtn: true,
    	language: 'zh-CN',
    	pickerPosition: "bottom-left"
    });
    $("#edit_startWork").datetimepicker({
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
    // 所有的Input标签，在输入值后出现清空的按钮
    $('input[type="text"]').parent().css("position","relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    })
});
// 表格数据加载，使用jqGrid插件
function pageInit(){
	$("#loading").css('display','none');
	$("#list2").jqGrid('clearGridData');
    $("#list2").jqGrid({
        
        datatype: 'json', 
        contentType: "application/json; charset=utf-8",
        mtype:"post",
        height: 'auto',
     /*   multiselect : true,  */
        width: $(".content-table").width()*0.999,
        //列名
        colNames:['id','姓名', '用户名','所属公司','所属处室','在职状态','角色','入职日期','操作'],
        //赋值
        colModel:[
            {name:'id',index:'id',hidden:true},
            {name:'userName',index:'userName', sorttype:'string', searchoptions:{sopt:['cn']}},
            {name:'userAccount',index:'userAccount',searchoptions:{sopt:['cn']}},
            {name:'companyName',index:'companyName',searchoptions:{sopt:['cn']}},
            {name:'deptName',index:'deptName',searchoptions:{sopt:['cn']}}, 
            {name:'userStatus',index:'userStatus',searchoptions:{sopt:['cn']},
            formatter : function(value, grid, rows) {
            	return value=="1"? "在职":"离职";
            }
            },
            {name:'roleName',index:'roleName',searchoptions:{sopt:['cn']},
            	formatter : function(value, grid, rows, state) {
            		var roles = rows.userRoles;
            		var str = "";
            		$.each(roles,function(index,value){
            			str += value.roleName + ",";
            		});
            		return str.substr(0,str.length - 1);
            	} 
            
            },
            {name:'entryDate',index:'entryDate',searchoptions:{sopt:['cn']},
            	formatter : function(value, grid, rows, state) {
            		if(value!=undefined&&value!=""&&value!="null"){
            			return datFmt(new Date(value),"yyyy-MM-dd");
            		}else{
            			return "";
            		}
                    
                }
            },
            {
                name:'操作',
                index:'操作',
                align:"center",
                fixed:true,
                loadonce:true,
                sortable:false,
                resize:false,
                search: false,
                formatter : function(value, grid, rows, state) {
                	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                	var str='<a class="a_style" href="javascript:void(0)" onclick="edit('+ row + ')">编辑</a>';
                    return str+ '&nbsp;&nbsp;&nbsp;&nbsp;<a class="a_style" href="javascript:void(0)" onclick="reset('+ row + ')">重置密码</a>';
                    
                }
            }
        ],
        rowNum:10,
        rowTotal: 200,
        rowList : [10,20,30], 
        rownumWidth: 40,
        pager: '#pager2', 
        sortorder: 'asc',
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true, // 是否要显示总记录数
        loadComplete :function(xhr){ 
        	$("#loading").css('display','none');
        }
    }).trigger("reloadGrid");
   /* jQuery("#list2").jqGrid('navGrid','#pager2',{edit:false,add:false,del:false});*/
}

// 清空搜索内容
function clearSearch() {
    $('#personalName').val("");
    $("#personalName").parent().children(".btn_clear").css("display","none");
    $('#startWork').val("");
    $('#endWork').val("");
    $("#belongCompany").selectpicker('val', '');
    $("#belongOffice").selectpicker('val', '');
    $("#belongProject").selectpicker('val', '');
    $("#thirdParty").selectpicker('val', '');
    $("#status").selectpicker('val', '');
    $("#project").selectpicker('val', '');
}
// 清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}
// 展开收起表格第一行
function  showSearchInput(that) {
    if( $(that).text()=='收起筛选' ){
        $(that).text("展开筛选");
        $("#cleanrChoose").hide();
        $(".ui-search-toolbar").hide();
    }else {
        $(that).text("收起筛选");
        $("#cleanrChoose").show();
        $(".ui-search-toolbar").show();
    }
}
//搜索按钮触发function
function searchInfo(){ 
		$("#loading").css('display','block');
		 var obj = {};
		 obj.userName = $.trim($("#personalName").val());
		
		 obj.companyId =  $("#belongCompany").val();
		 obj.deptId = $("#belongOffice").val();
		 obj.userType = $("#thirdParty").val();
		 obj.userStatus = $("#status").val();
		 obj.entryDate = $("#startWork").val();
		 obj.leaveDate = $("#endWork").val();
		 obj.projectIds = $("#project").val();
		 obj = JSON.stringify(obj); 
	  // 重新载入
		 
	 $("#list2").jqGrid('setGridParam',{ 
		 	url:'/system/user/getAllUser',
		    datatype:'json',
	        postData:{
	         "FindUser":obj
	        }, 
	        page:1
	    }).trigger("reloadGrid");  
	 $("#loading").css('display','none');
	}


// 显示新建人员Model
function newPerson_btn() {
	newReset();
    $("#newPerson").modal("show");
}
// 显示导入人员Model
function importPerson_btn() {
    $("#importPerson").modal("show");
}
// 显示编辑人员Model
function edit( row ) {
// var row = JSON.parse(rows);
	editReset();
	getUser(row);
    $("#editPerson").modal("show");
}

//新建清空
function newReset(){
	$("#newUserName").val('');
	$("#newUserAccount").val('');
	$("#newGender").selectpicker('val','');
	$("#newBirthday").val('');
	$("#newEmployeeNumber").val('');
	$("#newEmail").val('');
	$("#userType").selectpicker('val','');
	$("#newCompany").selectpicker('val','');
	$("#newDept").selectpicker('val','');
	$("#newUserStatus").selectpicker('val','');
	$("#new_startWork").val('');
	$("#newRole").selectpicker('val','');
	$("#scmAccount").val('');
	$("#scmAccountDev").hide();
}

/**
 * 新增弹框点击确认提交
 * @returns
 */

function newCommit(){
	$('#newform').data('bootstrapValidator').validate();
	if (!$('#newform').data('bootstrapValidator').isValid()) {
		return;
	}
	$("#loading").css('display', 'block');
	 var ids = [];
	 var rids="";
	 $("#newRole option:selected").each(function () {
	     ids.push(Number( $(this).val() ));
		 rids=rids+","+$(this).val();
	 })
	if(rids!=""){
		rids = rids.substr(1);
	}
	$.ajax({
		url: "/system/user/saveUser",
		dataType: "json",
		type: "post",
		// contentType: "application/json; charset=utf-8",
		// data:JSON.stringify ({
		// 	"userName" : $("#newUserName").val(),
		// 	"userAccount" : $("#newUserAccount").val(),
		// 	"gender" : $("#newGender").val(),
		// 	"birthday" : $("#newBirthday").val(),
		// 	"employeeNumber" : $("#newEmployeeNumber").val(),
		// 	"email" : $("#newEmail").val(),
		// 	"userType" : $("#userType").val(),
		// 	"companyId" : $("#newCompany").val(),
		// 	"deptId" : $("#newDept").val(),
		// 	"userStatus" : $("#newUserStatus").val(),
		// 	"entryDate" : $("#new_startWork").val(),
		// 	"roleIds" : ids
		// }),
        data:{
            "userName" : $("#newUserName").val(),
            "userAccount" : $("#newUserAccount").val(),
            "gender" : $("#newGender").val(),
            "newBirthday" :$("#newBirthday").val(),
            "employeeNumber" : $("#newEmployeeNumber").val(),
            "email" : $("#newEmail").val(),
            "userType" : $("#userType").val(),
            "companyId" : $("#newCompany").val(),
            "deptId" : $("#newDept").val(),
            "userStatus" : $("#newUserStatus").val(),
            "newEntryDate" :$("#new_startWork").val(),
            "roleIdsArray":rids
        },
		success: function (data) {
			$("#loading").css('display', 'none');
			//data.status后台返回状态，1，正常返回，2，异常返回， repeat：工号重复，repeat2:用户名重复
			if (data.status == "1") {
				layer.alert('保存成功！', { icon: 1 });
				pageInit();
				$("#newPerson").modal("hide");
			} else if (data.status == "2") {
				layer.alert('保存失败！！！', { icon: 2 });
			} else if (data.status == "repeat") {
				layer.alert('该员工号已存在！！！', { icon: 0 });
			} else if (data.status == "repeat2") {
				layer.alert('该用户名已存在！！！', { icon: 0 });
			}
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	});
}
/**
 * 编辑人员弹框清空表单信息
 * @returns
 */
function editReset(){
	$("#editUserName").val('');
	$("#editUserAccount").val('');
	$("#editGender").selectpicker('val','');
	$("#editBirthday").val('');
	$("#editEmployeeNumber").val('');
	$("#editEmail").val('');
	$("#editUserType").selectpicker('val','');
	$("#editCompany").selectpicker('val','');
	$("#editDept").selectpicker('val','');
	$("#editUserStatus").selectpicker('val','');
	$("#edit_startWork").val('');
	$("#editRole").selectpicker('val','');
	$("#scmAccount").val('');
	$("#scmAccountDev").show();
}

 /**
  * 获取条用户信息
  * @param row，选择的当前行的数据信息
  * @returns
  */
function getUser(row){
	$('#editPerson').on('hide.bs.modal', function () {
		$('#editform').bootstrapValidator('resetForm');
	});
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/system/user/findUserById1",
		dataType: "json",
		type: "post",
		data: {
			"userId": row.id
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			//data.status == 1 ，表示后台正常返回
			if(data.status == "1"){
				$("#editUserName").val(data.data.userName);
				$("#editUserAccount").val(data.data.userAccount);
				$("#editGender").val(data.data.gender);
				$("#editBirthday").val(data.data.birthday);
				$("#editEmployeeNumber").val(data.data.employeeNumber);
				$("#editEmail").val(data.data.email);
				$("#editUserType").val(data.data.userType);
				$("#editCompany").val(data.data.companyId);
				$("#editDept").val(data.data.deptId);
				$("#editUserStatus").val(data.data.userStatus);
				$("#edit_startWork").val(data.data.entryDate);
				$("#editRole").val(data.data.roleIds);
				$("#editUserId").val(data.data.id);
				$("#scmAccount").val(data.data.userScmAccount)
			}
			//data.status == 2表示后台出现错误，异常返回
			if (data.status == "2") {
				layer.alert("数据加载失败！", { icon: 2 });
			}
            //下拉框重新刷新
			$('.selectpicker').selectpicker('refresh');
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
}

//编辑提交
function editCommit(){
	$('#editform').data('bootstrapValidator').validate();
	if (!$('#editform').data('bootstrapValidator').isValid()) {
		return;
	}
	$("#loading").css('display', 'block');
	var ids = [];
	var rids="";
	 $("#editRole option:selected").each(function () {
	     ids.push(Number( $(this).val() ));
		 rids=rids+","+$(this).val();
	})
	if(rids!=""){
		rids = rids.substr(1);
	}
	$.ajax({
		url: "/system/user/updateUser",
		dataType: "json",
		type: "post",
		//contentType: "application/json; charset=utf-8",
		// data: JSON.stringify ({
		// 	"id" : $("#editUserId").val(),
		// 	"userName" : $("#editUserName").val(),
		// 	"userAccount" : $("#editUserAccount").val(),
		// 	"gender" : $("#editGender").val(),
		// 	"birthday" : $("#editBirthday").val(),
		// 	"employeeNumber" : $("#editEmployeeNumber").val(),
		// 	"email" : $("#editEmail").val(),
		// 	"userType" : $("#editUserType").val(),
		// 	"companyId" : $("#editCompany").val(),
		// 	"deptId" : $("#editDept").val(),
		// 	"userStatus" : $("#editUserStatus").val(),
		// 	"entryDate" : $("#edit_startWork").val(),
		// 	"roleIds" : ids
		//
		// }),


		data: {
			"id" : $("#editUserId").val(),
			"userName" : $("#editUserName").val(),
			"userAccount" : $("#editUserAccount").val(),
			"gender" : $("#editGender").val(),
			"newBirthday": $("#editBirthday").val(),
			"employeeNumber" : $("#editEmployeeNumber").val(),
			"email" : $("#editEmail").val(),
			"userType" : $("#editUserType").val(),
			"companyId" : $("#editCompany").val(),
			"deptId" : $("#editDept").val(),
			"userStatus" : $("#editUserStatus").val(),
			"newEntryDate" : $("#edit_startWork").val(),
			"userScmAccount" :$("#scmAccount").val(),
			"roleIdsArray":rids

		},

		success: function (data) {
			$("#loading").css('display', 'none');
			if (data.status == "1") {
				layer.alert('编辑成功！', { icon: 1 });
				pageInit();
				$("#editPerson").modal("hide");
			} else if (data.status == "2") {
				layer.alert('编辑失败！！！', { icon: 2 });
			} else if (data.status == "repeat") {
				layer.alert('该员工号已存在！！！', { icon: 0 });
			} else if (data.status == "repeat2") {
				layer.alert('该用户名已存在！！！', { icon: 0 });
			}
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	});
}

//重置密码
function reset( row ) {	
	var id=row.id;
	layer.confirm('确认重置用户  '+row.userAccount+' 的密码',{btn: ['确定','取消'],title:"提示"},function(){
		$.ajax({
	        type:"POST",
	        url:"/system/user/resetPassword",
	        dataType:"json",	
	        data:{
	        	"id":id
	        },
	        success:function(data){
	        	status= data["status"];
	        	var errorMessage= data["errorMessage"];
	        	if(status==1){	   	        		
	        		layer.alert("用户  "+row.userAccount+"  密码重置成功", {
	        		    icon: 1,
	        		    title: "提示信息"
	        		});	        		
	        	}else{
	        		layer.alert(errorMessage, {
	        		    icon: 2,
	        		    title: "提示信息"
	        		});
	        	}
	        } 
		
	    });//$.ajax
    });//	layer.confirm    
}

// 清空下方表格内输入框的内容
function cleanrChoose(){
    $(".ui-search-input input").val("");
    $(".ui-search-input input").parent().children(".btn_clear").css("display","none");
    $("#list2").jqGrid('clearGridData');  // 清空表格
    $("#list2").jqGrid('setGridParam',{  // 重新加载数据
        datatype:'json',
        url:ctxStatic +'/jqgrid/data/JSONData.json',
        page:1,
        loadComplete :function(xhr){ 
        	$("#loading").css('display','none');
        }
    }).trigger("reloadGrid");
} 

//导出人员
function exportPerson_btn(){
	/*var data=getJQAllData();
	var excelData=JSON.stringify(data);*/
	
	 var obj = {};
	 obj.userName = $.trim($("#personalName").val());
	
	 obj.companyId =  $("#belongCompany").val();
	 obj.deptId = $("#belongOffice").val();
	 obj.userType = $("#thirdParty").val();
	 obj.userStatus = $("#status").val();
	 obj.entryDate = $("#startWork").val();
	 obj.leaveDate = $("#endWork").val();
	 obj = JSON.stringify(obj); 
	window.location.href="/system/user/getExcelAllUser?excelData="+obj;

}

//获取页面数据
function getJQAllData() {
	// 拿到grid对象
	var obj = $("#list2");
	// 获取grid表中所有的rowid值
	var rowIds = obj.getDataIDs();
	// 初始化一个数组arrayData容器，用来存放rowData
	var arrayData = new Array();
	if (rowIds.length > 0) {
	  for (var i = 0; i < rowIds.length; i++) {
	  // rowData=obj.getRowData(rowid);//这里rowid=rowIds[i];
	  arrayData.push(obj.getRowData(rowIds[i]));
	  }
	}
	return arrayData;
	}



//获取部门信息
function getDept() {

    $.ajax({
        type: "post",
        url: "/system/user/getDept",
        dataType: "json",
        success: function(data) {
            for(var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].deptName;
        var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#belongOffice").append(opt);
                $("#newDept").append(opt);
                $("#editDept").append(opt);
            }
            $('.selectpicker').selectpicker('refresh'); 
        }
    });
}

//获取所有项目
function getAllproject(){
	 $.ajax({
	        type: "post",
	        url: "/system/user/getAllproject",
	        dataType: "json",
	        success: function(data) {
	            for(var i = 0; i < data.length; i++) {
	                var id = data[i].id;
	                var name = data[i].projectName;
	        var opt = "<option value='" + id + "'>" + name + "</option>";
	                $("#project").append(opt);
	            }
	            $('.selectpicker').selectpicker('refresh'); 
	        }
	    });
}

//获取公司
function getCompany() {
    $.ajax({
        type: "post",
        url: "/system/user/getCompany",
        dataType: "json",
        success: function(data) {
            for(var i = 0; i < data.length; i++) {
                var id = data[i].id;
                var name = data[i].companyName;
        var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#belongCompany").append(opt);
                $("#newCompany").append(opt);
                $("#editCompany").append(opt);
            }
            $('.selectpicker').selectpicker('refresh'); 
        }
    });
}

//获取所有角色
function getRoles() {
	$.ajax({
		type: "post",
		url: "/system/user/getRoles",
		dataType: "json",
		success: function(data) {
			for(var i = 0; i < data.length; i++) {
				if(data[i].projectId == null){
                    var id = data[i].id;
                    var name = data[i].roleName;
                    var opt = "<option value='" + id + "'>" + name + "</option>";
                    $("#newRole").append(opt);
                    $("#editRole").append(opt);
				}
			}
			$('.selectpicker').selectpicker('refresh'); 
		}
	});
}

$(document).ready(function() {
    
     
});
/*
 * js由毫秒数得到年月日
 */  
 function datFmt(date,fmt) { // author: meizz
    var o = {
        "M+": date.getMonth() + 1, // 月份
        "d+": date.getDate(), // 日
        "h+": date.getHours(), // 小时
        "m+": date.getMinutes(), // 分
        "s+": date.getSeconds(), // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3), // 季度
        "S": date.getMilliseconds() // 毫秒
    };  
    //需要转换的格式正则匹配，即需要转换年
    if (/(y+)/.test(fmt)) 
    	fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));  
    for (var k in o)  
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
    return fmt;  
};  

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
    	   newUserName: {
			    validators: {
			        notEmpty: {
			            message: '姓名不能为空'
			        }
			    }
			},
			newUserAccount: {
				validators: {
					notEmpty: {
			            message: '用户名不能为空'
			        }
			    }
			},
			newUserStatus: {
				validators: {
					notEmpty: {
			            message: '在职状态不能为空'
			        }
			    }
			},
			newGender: {
				validators: {
					notEmpty: {
			            message: '性别不能为空'
			        }
			    }
			},
			newEmployeeNumber: {
				validators: {
					notEmpty: {
			            message: '员工号不能为空'
			        }
			    }
			}
       }
   }); 
   
   //编辑框表单校验
   $('#editform').bootstrapValidator({
	　　　  message: '输入有误',//通用的验证失败消息
		    feedbackIcons: {//根据验证结果显示的各种图标
	　　　　　　　valid: 'glyphicon glyphicon-ok',
	　　　　　　　invalid: 'glyphicon glyphicon-remove',
	　　　　　　　validating: 'glyphicon glyphicon-refresh'
	　　　  },
	       fields: {
	    	   editUserName: {
				    validators: {
				        notEmpty: {
				            message: '姓名不能为空'
				        }
				    }
				},
				editUserAccount: {
					validators: {
						notEmpty: {
				            message: '用户名不能为空'
				        }
				    }
				},
				editUserStatus: {
					validators: {
						notEmpty: {
				            message: '在职状态不能为空'
				        }
				    }
				},
				editGender: {
					validators: {
						notEmpty: {
				            message: '性别不能为空'
				        }
				    }
				},
				editEmployeeNumber: {
					validators: {
						notEmpty: {
				            message: '员工号不能为空'
				        }
				    }
				},
			   scmAccount:{
				   validators: {
					   notEmpty: {
						   message: '代码仓库账号不能为空'
					   }
				   }
			   }
	       }
	   }); 
}
