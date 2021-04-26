var id = '';
var selectSystemIds = [];

$(function(){  
		 pageInit();
		 dateComponent();
     formValidator();
     getWorkProjectByUser();
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
   
});

//表格数据加载
function pageInit(){
	$("#loading").css('display','block');
    $("#list2").jqGrid({
        url:'/devManage/notice/getAllNotice',
        datatype: 'json', 
        contentType: "application/json; charset=utf-8",
        mtype:"POST",
        height: 'auto',
				width: $(".content-table").width()*0.999,
				postData : {
        	"programId" : $("#project_id").val(),
        	"type": $('#project_type').val(),
        },
        colNames:['id','公告内容','公告类型','创建人','有效期','操作'],
        colModel:[ 
            {name:'id',index:'id',hidden:true}, 
            {name:'noticeContent',index:'noticeContent', width:300,
            	formatter : function(value, grid, rows) {
        			return "<a class='a_style' onclick='showNoticeDetail("+rows.id+")'>"+value+"</a>";
        		}	
            },
            {name:'noticeType',index:'noticeType', width:80,
            	formatter : function(value, grid, rows) {
            		var noticeTypeStr = "";
	            	if(rows.noticeType == 1){
	            		noticeTypeStr += "系统公告";
	        		}
	            	if(rows.noticeType == 2){
	            		noticeTypeStr += "项目公告";
	        		}
	            	return noticeTypeStr;
        		}	
            },
            {name:'userName',index:'userName',
            	formatter : function(value, grid, rows) {
            		return rows.userName + " | " + rows.createDate;
            	}	
            },
            {name:'validDate',index:'validDate', width:100,
            	formatter : function(value, grid, rows) {
            		var validDateStr = "";
	            	if(toStr(rows.startTime) != ""){
	            		validDateStr += datFmt(new Date(rows.startTime),"yyyy-MM-dd");
	            		validDateStr += " - ";
	        		}
	            	if(toStr(rows.endTime) != ""){
	            		validDateStr += datFmt(new Date(rows.endTime),"yyyy-MM-dd");
	        		}
	            	return validDateStr;
	        	}
            },
            {name:'操作',index:'操作',align:"center",fixed:true,sortable:false,resize:false,search: false,width:200,
            	formatter : function(value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                    var span_ = "<span>&nbsp;|&nbsp;</span>";
                    var opt_status = [];
                    var a = '<a class="a_style" onclick="editNotice('+ row + ')">编辑</a>';
                    //具有编辑权限
                    if(noticeEdit == true){
                    	opt_status.push(a);
                    }
                    var b = '<a class="a_style" onclick="deleteNotice('+ row + ')">废弃</a>';
                    //具有废弃权限
                    if(noticeDelete == true){
                    	opt_status.push(b);
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
        gridComplete: function(){
			$("[data-toggle='tooltip']").tooltip(); 
        },
        loadComplete :function(){
        	$("#loading").css('display','none');
        }
    }).trigger("reloadGrid"); 
}

//条件搜索
function searchInfo(){
    $("#loading").css('display','block');
    $("#list2").jqGrid('setGridParam',{
        postData : {
					"programId" : '',
        	"type": '',
        	"noticeContent" : $.trim($("#noticeContent").val()),
        	"status": $('#status').val(),
        	"createDateStr": $('#createDateStr').val(),
        	"validDateStr":$("#validDateStr").val(),
        },
        page:1,
        loadComplete :function(){
            $("#loading").css('display','none');
        }
    }).trigger("reloadGrid"); //重新载入
}

//重置条件
function clearSearch(){
	$('#noticeContent').val("");
	$("#status").selectpicker('val', '');
	$("#createDateStr").val('');
	$("#validDateStr").val('');
	searchInfo();
}

//系统公告查看
function showNoticeDetail(id) {
	layer.open({     
      type: 2, 
      title: '系统公告',
      shadeClose: true,
      shade: false, 
      move: false,
      area: ['60%', '60%'], 
      id: "noticeLayer",
      offset: "4%",
      shade: 0.3,	
      tipsMore: true, 
      anim: 2,
      content: '/devManageui/notice/toNoticeDetail?noticeId='+id,
      btn: ['关闭'] ,
      btnAlign: 'c', //按钮居中
      no:function(){ 
    	  layer.closeAll();
      }
    });
}

//新增
function addNotice(){
	searchAdd();
	$("#newform").data('bootstrapValidator').destroy(); 
    formValidator();
	$("#new_notice").modal("show");
}

//新增数据清空
function searchAdd(){
	$("#new_noticeContent").val("");
	$("#new_noticeType").selectpicker('val', '');
	$("#new_projectName").selectpicker('val', '');
	$("#new_validDateStr").val("");
}

//改变公告类型联动
function changeNoticeType(obj) {
	var noticeType = $(obj).val();
	if (noticeType == 2) { //项目公告
		$(obj).closest(".rowdiv").next().show();
	} else {
		$(obj).closest(".rowdiv").next().hide();
	}
}

//新增提交
function addNoticeSubmit(){
	$('#newform').data('bootstrapValidator').validate();  
	if( !$('#newform').data("bootstrapValidator").isValid() ){
		return;
	}
	var noticeContent = $('#new_noticeContent').val();
	var noticeType = $('#new_noticeType').val();
	var projectIds = $('#new_projectName').val();
	var validDateStr = $('#new_validDateStr').val();
	
	if(noticeContent==''){
		layer.alert('公告内容不能为空!',{
			 icon: 0,
		})
		return ;
	}
	if(noticeType==''){
		layer.alert('公告类型不能为空!',{
			 icon: 0,
		})
		return ;
	}
	if (noticeType == 2) {
		if(projectIds==null){
			layer.alert('项目组不能为空!',{
				 icon: 0,
			})
			return ;
		}
		projectIds = projectIds.toString();
	} else {
		projectIds = null;
	}
	
	if(validDateStr==''){
		layer.alert('公告有效日期不能为空!',{
			icon: 0,
		})
		return ;
	}
	
	var arr = validDateStr.split(" - ");
	var startTime = arr[0].trim();
	var endTime = arr[1].trim();
	
	$.ajax({
		type:"post", 
		url:"/devManage/notice/insertNotice",
		dataType:"json",
		contentType: "application/json; charset=utf-8",
		data:JSON.stringify({
			noticeContent:noticeContent,
			noticeType:noticeType,
			projectIds:projectIds,
			startTime:startTime,
			endTime:endTime,
		}),
		success : function(data) {
			if(data.status == 1){
				layer.alert('新增公告成功!',{
					 icon: 1,
				},function(index){
					$("#new_notice").modal("hide"); 
					layer.close(index);
					searchInfo();
				})
			} else {
				layer.alert(data.errorMessage, {
				icon: 2,
				title: "提示信息"
				});
		   }
		}, error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	}); 
}

//编辑
function editNotice(row){
	$("#loading").css('display','block');
	$("#edit_notice").modal("show");
	id = row.id;
	$.ajax({
		type:"post", 
		url:"/devManage/notice/selectNoticeById",
		dataType:"json",
		data:{
			"id":id
		},
		success : function(data) {
			 $("#loading").css('display','none');
			 $("#edit_id").val(data.data.id);
			 $("#edit_noticeContent").val(data.data.noticeContent);
			 $("#edit_noticeType").selectpicker('val', data.data.noticeType);
			 changeNoticeType($("#edit_noticeType"));
			 if (data.data.noticeType == 2) {
				 var arr = data.data.projectIds.split(",");
				 $("#edit_projectName").selectpicker('val', arr);
			 }
			 var validDateStr = "";
			 if(toStr(data.data.startTime) != ""){
				 validDateStr += datFmt(new Date(data.data.startTime),"yyyy-MM-dd");
				 validDateStr += " - ";
			 }
			 if(toStr(data.data.endTime) != ""){
				validDateStr += datFmt(new Date(data.data.endTime),"yyyy-MM-dd");
			 }
			 $("#edit_validDateStr").val(validDateStr);
		}
	});
}

//编辑提交
function editNoticeSubmit(){
	$('#newform2').data('bootstrapValidator').validate();  
	if( !$('#newform2').data("bootstrapValidator").isValid() ){
		return;
	}
	var id = $('#edit_id').val();
	var noticeContent = $('#edit_noticeContent').val();
	var noticeType = $('#edit_noticeType').val();
	var projectIds = $('#edit_projectName').val();
	var validDateStr = $('#edit_validDateStr').val();
	
	if(noticeContent==''){
		layer.alert('公告内容不能为空!',{
			 icon: 0,
		})
		return ;
	}
	if(noticeType==''){
		layer.alert('公告类型不能为空!',{
			 icon: 0,
		})
		return ;
	}
	if (noticeType == 2) {
		if(projectIds==null){
			layer.alert('项目组不能为空!',{
				 icon: 0,
			})
			return ;
		}
		projectIds = projectIds.toString();
	} else {
		projectIds = null;
	}
	
	if(validDateStr==''){
		layer.alert('公告有效日期不能为空!',{
			icon: 0,
		})
		return ;
	}
	
	var arr = validDateStr.split(" - ");
	var startTime = arr[0].trim();
	var endTime = arr[1].trim();
	
	$.ajax({
		type:"post", 
		url:"/devManage/notice/updateNotice",
		dataType:"json",
		contentType: "application/json; charset=utf-8",
		data:JSON.stringify({
			id:id,
			noticeContent:noticeContent,
			noticeType:noticeType,
			projectIds:projectIds,
			startTime:startTime,
			endTime:endTime,
		}),
		success : function(data) {
			if(data.status == 1){
				layer.alert('编辑公告成功!',{
					 icon: 1,
				},function(index){
					$("#edit_notice").modal("hide"); 
					layer.close(index);
					searchInfo();
				})
			} else {
				layer.alert(data.errorMessage, {
				icon: 2,
				title: "提示信息"
				});
		   }
		}, error:function(){
            $("#loading").css('display','none');
            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
        }
	}); 
}

//删除
function deleteNotice(row){
	var id = row.id;
	layer.alert('确认要删除此公告？',{
		 icon: 0,
		 btn: ['确定','取消'] 
	},function(){
		$.ajax({
	  		url:"/devManage/notice/deleteNotice",
	    	method:"post", 
	    	data:{
	    		"id":id
			}, 
	  		success:function(data){
	  			if(data.status == 1){
	  				layer.alert('删除成功!',{icon: 1});
	  				pageInit();
	  			}if(data.status == 2){
	  				layer.alert('删除失败!',{icon: 2});
	  				pageInit();
	  			}
	        }, error:function(){
	            $("#loading").css('display','none');
	            layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	        }
	 	}); 
	})
}


//获取项目信息
function getWorkProjectByUser(){
 $.ajax({
     method:"post",
     url:"/devManage/worktask/findProjectByUser",
     success : function(data) {
         if(data != null && data.data != null && data.data != undefined){
             var rows = data.data;
             var projectNameOpt = "";
             for (var i = 0; i < rows.length; i++) {
                 var id = rows[i].id;
                 var projectName = rows[i].projectName;
                 projectNameOpt += "<option value='" + id + "'>" + projectName + "</option>";
             }
             $("#new_projectName").append(projectNameOpt);
             $("#edit_projectName").append(projectNameOpt);
             $('.selectpicker').selectpicker('refresh');
         }
     },
     error:function(XMLHttpRequest, textStatus, errorThrown){ 
 		layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
 	}
 });

}

//日期控件初始化
function dateComponent() {
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
	$("#createDateStr").daterangepicker({ 'locale': locale });
	$("#validDateStr").daterangepicker({ 'locale': locale });
	$("#new_validDateStr").daterangepicker({ 'locale': locale });
	$("#edit_validDateStr").daterangepicker({ 'locale': locale });
}

//表单验证
function formValidator(){
	 $('#newform').bootstrapValidator({
		excluded:[":disabled"],
	　　　  message: '输入有误',//通用的验证失败消息
		    feedbackIcons: {//根据验证结果显示的各种图标
	　　　　　　　valid: 'glyphicon glyphicon-ok',
	　　　　　　　invalid: 'glyphicon glyphicon-remove',
	　　　　　　　validating: 'glyphicon glyphicon-refresh'
	　　　  },
	       fields: {
	    	   new_sprintName: {
				    validators: {
				        notEmpty: {
				            message: '冲刺任务名称不能为空'
				        } ,
				        stringLength: { 
	                      max:300,
	                      message: '冲刺任务名称长度必须小于500字符'
	                   }  
				    }
				},
				new_system: {
					 trigger:"change",
					validators: {
						notEmpty: {
				            message: '涉及系统不能为空'
				        } 
				    }
				}
	       }
	   }); 
	 $('#newform2').bootstrapValidator({
		 excluded:[":disabled"],
		 message: '输入有误',//通用的验证失败消息
		 feedbackIcons: {//根据验证结果显示的各种图标
			 valid: 'glyphicon glyphicon-ok',
			 invalid: 'glyphicon glyphicon-remove',
			 validating: 'glyphicon glyphicon-refresh'
		 },
		 fields: {
			 edit_sprintName: {
				 validators: {
					 notEmpty: {
						 message: '冲刺任务名称不能为空'
					 } ,
					 stringLength: { 
						 max:300,
						 message: '冲刺任务名称长度必须小于500字符'
					 }  
				 }
			 },
			 edit_system: {
				 trigger:"change",
				 validators: {
					 notEmpty: {
						 message: '涉及系统不能为空'
					 } 
				 }
			 }
		 }
	 }); 
}

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
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));  
    for (var k in o)  
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
    return fmt;  
}
 
 function toStr(value1){
		if(value1==undefined ||value1==null||value1=="null"||value1=="NULL"){
	 		return "";
	 	}else if(!isNaN(value1)){
	 		return value1;
	 	}else{
		   return value1.toString().trim();
	 	}
	}
