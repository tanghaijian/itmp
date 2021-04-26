/**
 * Created by 朱颜辞镜花辞树 on 2018/10/9.
 */ 

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

/*人员弹框*/
var thisInput = '';
function userModalShow(that){
	$("#loading").css("display","block");
	thisInput = that;
	$("#deptName").empty();
	$("#companyName").empty();
	$("#userModal").modal("show");
	
	userArr = [];
	var userIdStr = $(that).prev().val();
	if(userIdStr != ""){
		 var userIdAttr = userIdStr.split(',');
		 var userNameArr = $(that).val().split(',');
		$.each(userNameArr,function(index,value){
				userArr.push({id:userIdAttr[index],userName:value});
		})
	}
		 
	 clearSearchUser();
	 getDept();
	 getCompany();
	 userTableShow();
}

var userArr = [];

//====================人员弹框start=======================
function userTableShow(){
	   $("#userTable").bootstrapTable('destroy');
	    $("#userTable").bootstrapTable({  
	    	url:"/system/user/getAllUserModal",
	    	method:"post",
	        queryParamsType:"",
	        pagination : true,
	        sidePagination: "server",
	        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
	        pageNumber : 1,
	        pageSize : 10,
	        pageList : [ 5, 10, 15],
	        queryParams : function(params) {
	             var param={ 
	            	 systemId:$("#microServiceID").val(),
         		 userName: $.trim($("#userName").val()),
         		 companyName :  $("#companyName").val(),
         		 deptName : $("#deptName").val(),
	                 pageNumber:params.pageNumber,
	                 pageSize:params.pageSize, 
	             }
	            return param;
	        },
	        columns : [{
	            checkbox : true,
	            width : "30px",
	            formatter : stateFormatter
	        },{
	            field : "id",
	            title : "id",
	            visible : false,
	            align : 'center'
	        },{
	            field : "userName",
	            title : "姓名",
	            align : 'center'
	        },{
	            field : "userAccount",
	            title : "用户名",
	            align : 'center'
	        },{
	        	field : "companyName",
	        	title : "所属公司",
	        	align : 'center'
	        },{
	            field : "deptName",
	            title : "所属处室",
	            align : 'center'
	        }],
	        onLoadSuccess:function(){
	        	$("#loading").css("display","none");
	        },
	        onLoadError : function() {

	        },
	        onCheck:function(row,dom){
	        	userArr.push(row);
	        },
	        onUncheck:function(row,dom){
	        	$.each(userArr,function(index,value){
	        		if(value!=undefined && row.id == value.id){
	        			userArr.splice(index,1);
	        		}
	        	})
	        },
	        onCheckAll:function(rows){
	        	var length = userArr.length;
	        	$.each( rows,function(index,value){
	        		var i = 0;
	        		for( ;i < length;i++){
	        			if(userArr[i].id == value.id){
	        				break;
	        			}
	        		}
	        		if(i == length){
     				userArr.push(value);
     			}
	        	} );
	        },
	        onUncheckAll:function(rows){
	        	for(var i = userArr.length-1;i >= 0;i--){
	        		for(let value2 of rows){
	        			if( userArr[i].id == value2.id){
	        				userArr.splice(i,1);
	        				break;
	        			}
	        		}
	        	}
	        }
	    });   
}

function stateFormatter(value, grid, rows, state) {
	var flag = false;
	$.each(userArr,function(index,value){
		if(value.id == grid.id){
			flag = true;
		}
	})
	return flag;
	}

//填充部门下拉框
function getDept() {
 $("#deptName").append("<option value=''>请选择</option>");
 $.ajax({
     type: "post",
     url: "/system/user/getDept",
     dataType: "json",
     success: function(data) {
         for (var i = 0; i < data.length; i++) {
             var id = data[i].id;
             var name = data[i].deptName;
             var opt = "<option value='" + id + "'>" + name + "</option>";
             $("#deptName").append(opt);
         }
         $('.selectpicker').selectpicker('refresh');
     }
 });
}

//填充公司下拉框
function getCompany() {
 $("#companyName").append("<option value=''>请选择</option>");
 $.ajax({
     type: "post",
     url: "/system/user/getCompany",
     dataType: "json",
     success: function(data) {
         for(var i = 0; i < data.length; i++) {
             var id = data[i].id;
             var name = data[i].companyName;
             var opt = "<option value='" + id + "'>" + name + "</option>";
             $("#companyName").append(opt);
         }
         $('.selectpicker').selectpicker('refresh');
     }
 });
}
//弹框提交，因为弹框和主页面在同一个页面，因此提交后可以直接把选择的值赋给当前页面的字段
function commitUser(){
	var selectContent = userArr;
 if(selectContent.length <=0) {
 	 layer.alert('请选择一列数据！', {icon: 0});
    return false;
 }else{
 	var ids = '';
 	var usernames = '';
 	for(var i =0;i<selectContent.length;i++){
 		ids += selectContent[i].id+",";
 		usernames += selectContent[i].userName+",";
 	}
		$(thisInput).prev().val(ids.substr(0,ids.length-1));
		$(thisInput).val(usernames.substr(0,usernames.length-1));
		$("#userModal").modal("hide");
	 }
}
//清空查询
function clearSearchUser(){
	 $("#userName").val('');
	 $("#employeeNumber").val('');
	 $("#deptName").val('');
	 $("#companyName").val('');
	 $(".color1 .btn_clear").css("display","none");
}
//====================人员弹框end=======================

 


