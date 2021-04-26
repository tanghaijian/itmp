/**
 * 运维类项目编辑查看页面js
 */
var arr=[];
$(function() {
	pageInit(); 
	getStatus();
//	getDept();
//	getCompany();
	formValidator();
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
});

// 详情数据加载
function pageInit() {
	var selectObj=showPost(); 
	$("#loading").css('display','block');
	$("#memberGroup").empty();
	$("#systems").empty();
	$.ajax({
		url : "/projectManage/oamproject/selectProjectAndUserById",
		type : "post",
		dataType : "json",
		data:{
			"id":$("#id").val(),
			"homeType":$("#type").val()
		},
		success : function(data) {
	        
			var pro = data.data;
			$("#projectCode").html(pro.projectCode);
			$("#code").val(pro.projectCode);
			$("#projectName").html(pro.projectName);
			$("#name").val(pro.projectName);
			$("#projectStatus").html(pro.projectStatusName);
//			$("#status1").select(pro.projectStatusName);
			$("#status option").each( function (i, n) {
			    if (n.value == pro.projectStatusName) {
			        n.selected = true;
			    }
			});
			$(".selectpicker").selectpicker('refresh');
			var system = "";
			if( pro.systemList.length==1 ){
				system=pro.systemList[0].systemName;
				var s='<div class="addSysNames" value='+pro.systemList[0].id+' ><span class="addSysNames_span">'+pro.systemList[0].systemName+'</span>'+
				'<span class="close_x" onclick="delAddSysName(this,event)">×</span></div>';
				$("#systems").append(s);
			}else{
				for(var i = 0; i<pro.systemList.length;i++){ 
					 system += pro.systemList[i].systemName+" / ";
					 var s='<div class="addSysNames" value='+pro.systemList[i].id+' ><span class="addSysNames_span">'+pro.systemList[i].systemName+'</span>'+
					 '<span class="close_x" onclick="delAddSysName(this,event)">×</span></div>';
					 $("#systems").append(s);
				}
			} 
			$("#systemName").html(system);
//			$("#system").val(s);
			$("#projectOverview").text(isValueNull(pro.projectOverview));   
			$("#overview").text(isValueNull(pro.projectOverview));  
			
			var str='';
			//显示项目组人员
			showPrejectMember( selectObj, data ,str ); 
			// type=1,是编辑,type=2,是查看 
			$('.selectpicker').selectpicker('refresh');
			if ($("#type").val() == 1) {   
				$('[class*="def_hide"]').css("display", 'block');
				$('[class*="def_show"]').css("display",'none'); 
			} else if ($("#type").val() == 2) { 
				$('[class*="def_show"]').css("display", 'block');
				$('[class*="def_hide"]').css("display", 'none');
				$('.btnSave').css("display",'none');
			}
			if($("#is_home_page").val()){  //项目主页进来的
				$('[class*="def_show"]').css("display", 'block');
				$('[class*="def_hide"]').css("display", 'none');
				$('#back_btn').css("display", 'none');
				$('.def_hideBtn').css("display", 'block');
			}
			$("#loading").css('display','none');
		}
	});
}
//显示该项目下的人员
function showPrejectMember(selectObj , data ,str ){  
	for(var i = 0; i < data.data.list.length; i++ ){   
		var selectStr='<ul>';
		for(var j = 0; j < data.data.list[i].list.length; j++ ){ 
			selectStr+='<li class="rowdiv"><input class="pguId" type="hidden" value='+ data.data.list[i].list[j].peojectGroupUserId+'  /><input class="memberVal" type="hidden" value='+ data.data.list[i].list[j].id+'  /><div class="def_pjMember_num">&nbsp;<i class="def_pjDiv_del_hide control_i"  onclick="deleteMembor(this)"></i></div>'+
            '<div class="def_pjMember_div jobSelect"><label class="def-pj-label font_left">'+data.data.list[i].list[j].valueName+'</label><div class="def_selectHide"><select onchange="selectChange(this)" class="selectpicker" name="userPost">'; 
			selectStr+='<option value="">--请选择--</option>';	
			for(var key in selectObj){
            	if(key==data.data.list[i].list[j].userPost){
            		selectStr+='<option value='+key+' selected="selected">'+selectObj[key]+'</option>';
            	}else{
            		selectStr+='<option value='+key+'>'+selectObj[key]+'</option>';	
            	} 
            } 
			var deptOrcom='';
			if( data.data.list[i].list[j].deptOrCompany!=null ){
				deptOrcom=data.data.list[i].list[j].deptOrCompany;
			}
            selectStr+='</select></div></div><div class="def_pjMember_div"><label class="def-pj-label font_left">'+data.data.list[i].list[j].userName+'</label></div>'+
            '<div class="def_pjMember_div"><label class="def-pj-label font_left">'+deptOrcom+'</label></div><div class="def_pjMember_div"><label class="def-pj-label font_left">';
            if(data.data.list[i].list[j].userType !=null && data.data.list[i].list[j].userType == 1){
				selectStr+='内部人员'; 
			}
			if(data.data.list[i].list[j].userType !=null && data.data.list[i].list[j].userType == 2){ 
				selectStr+='外部人员'; 
			}
		}  
		selectStr+="</label></div></li></ul>";
		var member=$('<div class="def_pjMember"></div>').append( selectStr );  
		var pjDiv=$('<div class="def_pjDiv_cont"></div>').append( member ); 
		var memberGroup=$('<div class="def_pjDiv" value="'+data.data.list[i].id+'" id="pjGroup'+data.data.list[i].id+'"><div class="def_pjDiv_tit"></i><span class="pjName">'+data.data.list[i].projectGroupName+
		        '</span><button class="btn btn-primary def_hideBtn def_small_btn" onclick="edit(this)">编辑人员</button>'+
		        '<button class="btn btn-default def_Btn def_small_btn" onclick="hide(this)">取消</button>'+ 
		        '<button class="btn btn-primary def_Btn def_small_btn" onclick="add(this)">添加</button>'+
		        '</div></div>').append( pjDiv );  
		if( data.data.list[i].level==0 ){ 
			$("#memberGroup").append( memberGroup );
		}else{ 
			$("#pjGroup"+data.data.list[i].parent+">.def_pjDiv_cont>.def_pjMember").append( memberGroup );  
		}
	} 
}
//项目编辑添加人员
function add(This){
	//排除重复添加
//	for( var i=0;i<$(This).parent().next().children(".def_pjMember").children("ul").children(".rowdiv").length;i++ ){ 
//		arr.push(Number( $(This).parent().next().children(".def_pjMember").children("ul").children(".rowdiv").eq(i).children( ".memberVal" ).val() ) );
//	} 
	clearSearch();
	$("#user_groupId").val( $(This).parent().parent().attr("id") );  
	$("#userModal").modal("show");
}

//添加人员弹窗，搜索事件
function add2(){
	addUser2();
	$("#userModal").modal("show");
}

//清空人员弹窗搜索条件并重新加载数据
function clearSearch(){
    $("#userName").val('');
    $("#employeeNumber").val('');
    $('#deptName').val('');
    $('#companyName').val('');
}
//添加人员弹窗，查询结果组装
function addUser2(){
	$("#loading").css('display','block');
	$("#userTable").bootstrapTable('destroy');
    $("#userTable").bootstrapTable({  
    	url:"/projectManage/oamproject/selectUser",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 10, 15, 20],
        queryParams : function(params) {
             var param={ 
            	 userName:$.trim($("#userName").val()),
            	 employeeNumber:$.trim($("#employeeNumber").val()),
            	 deptName:$.trim($("#deptName").val()),
            	 companyName:$.trim($("#companyName").val()),
                 pageNumber:params.pageNumber,
                 pageSize:params.pageSize, 
             }
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px"
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

        }
    });
}


//关联人员的搜索（废弃）
function addUser(){ 
	$("#userModal").modal("show");
	$("#userTable").bootstrapTable('destroy');
    $("#userTable").bootstrapTable({  
        columns : [{
            checkbox : true,
            width : "30px"
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

        }
    });
} 

//把弹窗选中的人员信息给编辑页面
function insertUser(This){ 
	var selectObj=showPost();   
	var rows=$('#userTable') .bootstrapTable('getAllSelections'); 
	var ids = [];
	for(var i=0; i <rows.length; i++){
		ids.push(rows[i].id);	 
	}  
	$.ajax({
		url:"/projectManage/oamproject/selectUsers",
		method:"post",
		dataType:"json",
		traditional:true,
		data:{
			"ids": ids,
		},
		success: function(data) {
			 console.log( data );
			 for( var i =0;i<data.data.length;i++ ){
				 var selectStr='<li class="rowdiv"><input class="pguId" type="hidden" value=""  /><input class="memberVal" type="hidden" value='+ data.data[i].id+'  /><div class="def_pjMember_num">&nbsp;<i class="def_pjDiv_del_hide control_i def_pjDiv_del_show"  onclick="deleteMembor(this)"></i></div>'+
	            '<div class="def_pjMember_div jobSelect"><label class="def-pj-label font_left hideBlock">--请选择--</label><div class="def_selectHide showBlock"><select onchange="selectChange(this)" class="selectpicker" name="userPost"><option value="" >--请选择--</option>'; 
				for(var key in selectObj){
	            	selectStr+='<option value='+key+'>'+selectObj[key]+'</option>';	
	            } 
	            selectStr+='</select></div></div><div class="def_pjMember_div"><label class="def-pj-label font_left userName">'+data.data[i].userName+'</label></div>';
	            if(data.data[i].deptOrCompany != null){
	            selectStr+='<div class="def_pjMember_div"><label class="def-pj-label font_left">'+data.data[i].deptOrCompany+'</label></div>';
	            }if(data.data[i].deptOrCompany == null){
	            	selectStr+='&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp';
	            }
				if( data.data[i].userType == 1){
					selectStr+='<div class="def_pjMember_div"><label class="def-pj-label font_left">内部人员</label></div></li>'; 
				}
				if( data.data[i].userType == 2){ 
					selectStr+='<div class="def_pjMember_div"><label class="def-pj-label font_left">外部人员</label></div></li>'; 
				}
				$("#"+$("#user_groupId").val()+">.def_pjDiv_cont>.def_pjMember>ul" ).append( selectStr ); 
			 } 
			 $('.selectpicker').selectpicker('refresh');
		} 
	});
	console.log( $("#user_groupId").val() );
	console.log( rows ); 
	 
	$("#userModal").modal("hide");
	 
}

//搜索部门名称显示在部门下拉框
//function getDept(){
//	$.ajax({
//		url:"/projectManage/oamproject/selectDeptName",
//		method:"post",
//		dataType:"json",
//		success: function(data) {
//			$('#deptName').empty();
//			 $('#deptName').append("<option value='' selected='selected' >请选择</option>")
//			var list = data.data;
//			 for (var i = 0; i < list.length; i++) {       
//				 //先创建好select里面的option元素       
//				 var option = document.createElement("option");         
//				 //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
//				 $(option).val(list[i]);            
//				 //给option的text赋值,这就是你点开下拉框能够看到的东西          
//				 $(option).text(list[i]);              
//				 //获取select 下拉框对象,并将option添加进select            
//				 $('#deptName').append(option);           
//				 }
//			 $('.selectpicker').selectpicker('refresh'); 
//			 } 
//	}); 
//}

//搜索公司名称显示在搜索下拉框
//function getCompany(){
//	$.ajax({
//		url:"/projectManage/oamproject/selectCompanyName",
//		method:"post",
//		dataType:"json",
//		success: function(data) {
//			 $('#companyName').empty();
//			 $('#companyName').append("<option value='' selected='selected' >请选择</option>")
//			 var list = data.data;
//			 for (var i = 0; i < list.length; i++) {       
//				 //先创建好select里面的option元素       
//				 var option = document.createElement("option");         
//				 //转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
//				 $(option).val(list[i]);            
//				 //给option的text赋值,这就是你点开下拉框能够看到的东西          
//				 $(option).text(list[i]);              
//				 //获取select 下拉框对象,并将option添加进select            
//				 $('#companyName').append(option);           
//				 }
//			 $('.selectpicker').selectpicker('refresh'); 
//			 }
//		
//	}); 
//}

//编辑页面项目状态下拉框
function getStatus(){
	$.ajax({
		url:"/projectManage/oamproject/selectStatusName",
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
				 $('#status').append(option);           
				 }
			 $('.selectpicker').selectpicker('refresh'); 
			 }
		
	}); 
}

function selectCorrelation2(){
	$('#SCsystemName').val("");
	$("#SCsystemCode").val(""); 
	$("#select_Correlation").modal("show");
	selectCorrelation();
}
 


//编辑关联系统页面的显示
function selectCorrelation(){
	var arr=[]; 
	$("#loading").css('display','block');
	for( var i=0;i<$("#systems .addSysNames").length;i++ ){  
		arr.push( Number( $("#systems .addSysNames").eq(i).attr("value") ) );
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
                 systemIds:arr 
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
        	$("#select_Correlation").modal("show");
        	$("#loading").css('display','none');
        },
        onLoadError : function() {

        }
    });
}

function clearSearch2(){
	   $('#SCsystemName').val("");
	   $("#SCsystemCode").val(""); 
	   selectCorrelation();
}
 
 
//保存编辑项目
function save(){  
	$('#prejectForm').data('bootstrapValidator').validate();  
	if( !$('#prejectForm').data("bootstrapValidator").isValid() ){
		return;
	} 
	var systemsArr=[];
	for(var i=0;i<$("#systems .addSysNames").length;i++){
		systemsArr.push( $("#systems .addSysNames").eq(i).attr("value") )
	}
	var list=[]; 
	for(var i=0;i<$("div[id^='pjGroup']").length;i++ ){ 
		var obj={}
		obj.id=$("div[id^='pjGroup']").eq(i).attr("value");
		obj.list=[];
		for(var j=0;j<$("div[id^='pjGroup']").eq(i).children('.def_pjDiv_cont').children('.def_pjMember').children("ul").children("li").length;j++){
			 var presonInfo={};
			 presonInfo.id=$("div[id^='pjGroup']").eq(i).children('.def_pjDiv_cont').children('.def_pjMember').children("ul").children("li").eq(j).children(".memberVal").val();
			 if( $("div[id^='pjGroup']").eq(i).children('.def_pjDiv_cont').children('.def_pjMember').children("ul").children("li").eq(j).children(".pguId").val()!="" ){
				 presonInfo.peojectGroupUserId=$("div[id^='pjGroup']").eq(i).children('.def_pjDiv_cont').children('.def_pjMember').children("ul").children("li").eq(j).children(".pguId").val(); 
			 }
			 if($("div[id^='pjGroup']").eq(i).children('.def_pjDiv_cont').children('.def_pjMember').children("ul").children("li").eq(j).find(".selectpicker").val()==''){
				 layer.alert('请选择 '+$("div[id^='pjGroup']").eq(i).children('.def_pjDiv_tit').children('.pjName').text()+' 下的 '+$("div[id^='pjGroup']").eq(i).children('.def_pjDiv_cont').children('.def_pjMember').children("ul").children("li").eq(j).find(".userName").text()+' 的职位！',{
	  				 icon: 0,
	  			})
	  			return ;
			 }else{
				 presonInfo.userPost=$("div[id^='pjGroup']").eq(i).children('.def_pjDiv_cont').children('.def_pjMember').children("ul").children("li").eq(j).find(".selectpicker").val();
			 }
			 obj.list.push( presonInfo );
		}
		list.push( obj );
	}   
	$.ajax({
  		url:"/projectManage/oamproject/editProject",
    	method:"post",  
    	dataType: "json",
    	data:{
				"tblProjectInfo":JSON.stringify({
    		"id": $("#id").val(),
				"projectName": $("#name").val(),
				"projectCode": $("#code").val(),
				"projectStatusName": $("#status").val(),
				"systemIds": systemsArr,
				"projectOverview": $("#overview").val() ,
				"list": list,
				})
			}, 
  		success:function(data){   
//  			layer.alert('保存成功!',{
// 				 icon: 1,
// 			})
  			if( data.status == 1 ){
				 layer.alert("保存成功!",{icon : 1});
				 pageInit(); 
				 getStatus();
			 }if(data.status == 2){
				 layer.alert("保存失败",{icon : 2});
			 }
        }, 
 	}); 
	
}

//未用
function addProjectGroup(){
	var tableInputShow=0;
	 
	var ids = $("#projectGroupTable").jqGrid('getDataIDs');
    var rowid = Math.max.apply(Math,ids);
    var newrowid = rowid + 1;
    var dataRow = {
        id:newrowid,   
        projectGroupName:'',
        parent: null,
        '操作':''
    }; 
    $("#projectGroupTable").jqGrid("addRowData", newrowid, dataRow, "last");
    $('#projectGroupTable').jqGrid('editRow', newrowid, true);
    
	$("#"+newrowid).find(".optBtnGroup1").addClass("hideBlock");
	$("#"+newrowid).find(".optBtnGroup2").removeClass("hideBlock");
	$("#"+newrowid).find(".def_tableInput").css("display","block");
    
}
//维护项目组织弹窗
function mainProjectGroup(){
	console.log("维护项目组织ID:"+$("#id").val())
	$("#projectGroupTable").jqGrid("clearGridData");
    $("#projectGroupTable").jqGrid({ 
    	 url:'/projectManage/oamproject/selectProjectGroup',  
         datatype: "json", 
         height: 'auto',  
         mtype : "POST", 
         cellEdit:true,   
         width: 910,
         postData:{
        	 "id":$("#id").val()
         },  
         colNames:['id','项目小组','排序','操作'],
         colModel:[ 
            {name:'id',index:'id',hidden:true,key:true},
            {name:'projectGroupName',index:' 项目小组',  
            	formatter : function(value, grid, rows, state) {   
	            	return  '<span class="fontContent">'+rows.projectGroupName+'</span><div class="def_tableDiv"><input onclick="stopPro(event)" type="text" class="form-control def_tableInput" placeholder="请输入" value="'+rows.projectGroupName+'" /></div>';
	            }
            },
            {name:'排序',index:'排序',
            	formatter : function(value, grid, rows, state) {   
	            	return  '<span class="fontContent">'+rows.orderSeq+'</span><div class="def_tableDiv"><input onclick="stopPro(event)" type="number" class="form-control def_tableInput" placeholder="请输入" value="'+rows.orderSeq+'" /></div>';
	            }
            }, 
            {
                name:'操作',
                index:'操作',
                align:"center",
                width:240,
                fixed:true,
                sortable:false,
                resize:false,
                search: false, 
                formatter : function(value, grid, rows, state) {
                	var row = JSON.stringify(rows).replace(/"/g, '&quot;'); 
                	return '<div class="optBtnGroup1 " ><a onclick="editRowCont(this,'+row+')" class="a_style opt_editBtn">编辑</a><a onclick="saveRowCont(this,'+row+')" class="a_style opt_saveBtn hideBlock">保存</a>  |  <a onclick="newRowCont(this,'+row+')" class="a_style">新增下级</a>  |  <a onclick="delRowCont('+row+')" class="a_style">删除</a></div>'+
                	'<div class="optBtnGroup2 hideBlock" ><a onclick="addRowCont(this,'+row+')" class="a_style">添加</a>  |  <a onclick="backRowCont('+row+')" class="a_style">取消</a></div>';
                }
            }
        ],   
        treeGrid: true,
        treeGridModel: "adjacency",
        ExpandColumn: "projectGroupName",
        treeIcons: {plus:'fa fa-caret-right',minus:'fa fa-caret-down',leaf:''},
        sortname:"key_id",
        sortable:true,
        ExpandColClick: true,
        jsonReader: {
            repeatitems: false,
            root: "data", 
        },  
        loadtext:"数据加载中......",  
    }).trigger('reloadGrid'); 
	$("#projectGroupModal").modal("show"); 
}

//编辑项目小组，显示编辑框
function editRowCont(This,row){
	$(This).addClass("hideBlock");
	$(This).parent().children(".opt_saveBtn").removeClass("hideBlock"); 
	$(This).parent().parent().parent().find(".def_tableInput").css("display","block"); 
}

//保存项目小组
function saveRowCont(This,row){  
	$.ajax({
  		url:"/projectManage/oamproject/editProjectGroup",
    	method:"post", 
    	data:{
			"projectGroupName": $(This).parent().parent().parent().find(".def_tableInput").eq(0).val(),
			"orderSeq": $(This).parent().parent().parent().find(".def_tableInput").eq(1).val(),
			"id": row.id
		},  
  		success:function(data){  
  			$(This).parent().parent().parent().find(".fontContent").eq(0).html( $(This).parent().parent().parent().find(".def_tableInput").eq(0).val() );
  			$(This).parent().parent().parent().find(".fontContent").eq(1).html( $(This).parent().parent().parent().find(".def_tableInput").eq(1).val() ); 
  			$(This).addClass("hideBlock");
  			$(This).parent().children(".opt_editBtn").removeClass("hideBlock");
  			$(This).parent().parent().parent().find(".def_tableInput").css("display","none"); 
  			layer.alert('修改成功!',{
  				 icon: 1,
  			}) 
        }, 
 	}); 
}

//维护项目组织-新增下级
function newRowCont(This,row){ 
	var ids = $("#projectGroupTable").jqGrid('getDataIDs');
    var rowid = Math.max.apply(Math,ids);
    var newrowid = rowid + 1;
    var dataRow = {
        id:newrowid,   
        projectGroupName:'',
        parent: row.id,
        '操作':''
    };
	$("#projectGroupTable").jqGrid("addRowData", newrowid, dataRow,'after',row.id);
	$("#"+newrowid).find(".optBtnGroup1").addClass("hideBlock");
	$("#"+newrowid).find(".optBtnGroup2").removeClass("hideBlock");
	$("#"+newrowid).find(".def_tableInput").css("display","block");
} 

//维护项目组织-删除
function delRowCont(row){
	layer.alert('确定删除？',{
		 icon: 0,
		 btn: ['确定','取消'] 
	},function(){
		$.ajax({
	  		url:"/projectManage/oamproject/deletePeojectGroup",
	    	method:"post", 
	    	data:{
				"projectGroupId": row.id,
			}, 
	  		success:function(data){  
//	  			mainProjectGroup();
//	  			layer.alert('删除成功!',{
//	 				 icon: 1,
//	 			})
	  			if( data.status == 1 ){
					 layer.alert("删除成功!",{icon : 1});
					 mainProjectGroup();
				 }if(data.status == 2){
					 layer.alert("删除失败",{icon : 2});
					 mainProjectGroup();
				 }
	        }, 
	 	}); 
	})  
}
function backRowCont(row){  
	 $("#projectGroupTable").jqGrid("delRowData",row.id);
}

//维护项目组织-编辑保存/新增添加
function addRowCont(This,row){
		console.log("add:"+$("#id").val())
		$.ajax({
	  		url:"/projectManage/oamproject/saveProjectGroup",
	    	method:"post", 
	    	data:{
	    		"projectGroupName": $(This).parent().parent().parent().find(".def_tableInput").eq(0).val(),
				"orderSeq": $(This).parent().parent().parent().find(".def_tableInput").eq(1).val(),
				"parent": row.parent,
				"projectId": $("#id").val(),
			}, 
	  		success:function(data){   
	  			mainProjectGroup(); 
	        }, 
		}); 
}

//编辑人员-添加弹框中保存返回
function insertRoleUser(){ 
	 var rows=$('#presonTable') .bootstrapTable('getAllSelections'); 
	 for(var i=0;i<rows.length;i++){
		 var obj='<div class="addSysNames" value="'+rows[i].id+'" ><span class="addSysNames_span">'+rows[i].systemName+'</span>'+
		 '<span class="close_x" onclick="delAddSysName(this,event)">×</span></div> '
		 $("#systems").append(obj); 
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

// 人员岗位下拉框的显示
function showPost() {
	var obj={};
	$.ajax({
		url : "/projectManage/oamproject/selectUserPost",
		method : "post",
		dataType : "json",
		async: false,
		success : function(data) {  
			obj= data.data;
		}
	});
	return obj;
}

function edit(This) { 
	$(This).parent().parent().children(".def_pjDiv_cont").find(".jobSelect .def-pj-label").addClass( "hideBlock" );
	$(This).parent().parent().children(".def_pjDiv_cont").find(".jobSelect .def_selectHide").addClass( "showBlock" );
	$(This).parent().parent().find(".def_hideBtn").addClass( "hideBlock" );
	
	$(This).parent().parent().find(".def_Btn").css("display", "block");
	$(This).parent().parent().find(".def_pjDiv_del_hide").addClass("def_pjDiv_del_show");
}
function hide(This) { 
	$(This).parent().parent().children(".def_pjDiv_cont").find(".jobSelect .def-pj-label").removeClass( "hideBlock" );
	$(This).parent().parent().children(".def_pjDiv_cont").find(".jobSelect .def_selectHide").removeClass( "showBlock" );
	$(This).parent().parent().find(".def_hideBtn").removeClass( "hideBlock" );
	 
	$(This).parent().parent().find(".def_Btn").css("display", "none");
	$(This).parent().parent().find(".def_pjDiv_del_hide").removeClass("def_pjDiv_del_show")
}

function sure(){
	pageInit();
	$("#projectGroupModal").modal("hide"); 
}

function deleteMembor(This) {
	if ($(This).hasClass("def_pjDiv_del_show")) {
		if ($(This).parent().hasClass("def_pjDiv_tit")) {
			$(This).parent().parent().remove();
		} else {
			if ($(This).parent().parent().parent().find(".rowdiv").length > 1) {
				$(This).parent().parent().remove();
			} else {
				alert("项目组中至少有一人，如果不想保留项目组，请在维护项目组织中删除项目组！");
			}
		}
	}
}
function selectChange(This){
	$(This).parent().parent().parent().children("label").text( $(This).find("option:selected").text()  ) ;
}



//表单校验
function formValidator(){ 
   $('#prejectForm').bootstrapValidator({
　　　  message: '输入有误',//通用的验证失败消息
	    feedbackIcons: {//根据验证结果显示的各种图标
　　　　　　　valid: 'glyphicon glyphicon-ok',
　　　　　　　invalid: 'glyphicon glyphicon-remove',
　　　　　　　validating: 'glyphicon glyphicon-refresh'
　　　  },
       fields: {
    	   code: {
			    validators: {
			        notEmpty: {
			            message: '项目编号不能为空'
			        } ,
			        stringLength: { 
                      max:300,
                      message: '项目编号长度必须小于300字符'
                   }  
			    }
			},
			name: {
				validators: {
					notEmpty: {
			            message: '项目名称不能为空'
			        } ,
			        stringLength: { 
                      max:300,
                      message: '项目名称长度必须小于300字符'
                   } 
			    }
			} 
       }
   }); 
}

function isValueNull(value) {
    return value != 'null' && value ? value : '';
}