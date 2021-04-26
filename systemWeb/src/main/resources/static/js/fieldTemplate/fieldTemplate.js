/**
 * 自定义属性配置
 */

var TimeFn = null;
var thisData;
var dataType;
$(document).ready(function() {
	//展示需要配置自定义字段的数据库表
	leftMenu();
	formValidator();
	
	//新增字段
	$("#newFieldBtn").bind("click",function(){ 
		$( "#new_fieldName" ).val("");
		layer.open({
		  type: 1,
	      title: "新增字段",
	      shadeClose: true,
	      shade: 0.3, 
	      area: ['320px', '180px'],  
	      tipsMore: true,  
	      content: $('#newField'),  
	      btn: ['确定','取消'] ,
	      btnAlign: 'c', //按钮居中
	      yes: function( index , layero){  
	    	  checkData();
	      }, 
	      btn2:function(){ 
	    	  layer.closeAll();
	      }
	    });
	});
	$("#newDicBtn").bind("click",function(){
		addOnesDataToDicTable(); 
	});
	$("#saveAll").bind("click",function(){
		saveAll(); 
	}); 
	$("#commitEnu").bind("click",function(){ 
		changeThisSelect(); 
	}); 
}) 
function leftMenu( current ) {
    $(".left-box").empty(); 
	if( current==undefined ){ 
		current=0;
	}
    $("#loading").css('display','block');
	// 条件查询未关联人员
	$.ajax({
        url:"/system/fieldTemplate/getTableName",
        method:"post",
        dataType: "json",
		success : function(data) {  
			 
            $("#loading").css('display','none');
            var menuDate = JSON.parse(data.data); 
            var _ul = $('<ul class="menu-ul"></ul>');
            for (var i = 0; i < menuDate.length; i++) {
                var obj = menuDate[i];
                var _li = $('<li></li>');
                var _span = $('<span class="parent-id">' + obj.tableValue + '</span>');
                var _a = $('<a href="javascript:void(0);" >' + obj.tableKey + '</a>'); 
                _li.appendTo(_ul);
                _span.appendTo(_li);
                _a.appendTo(_li);
                if (i == current) {
                    _li.addClass("current");
                } 
            }
            _ul.appendTo($(".left-box")); 
            initPage();
            // 单击切换按钮
            $(".menu-ul li").click(function() {
            	$("#loading").css('display','block');
                select_rows = new Array();
                $(".menu-ul li").removeClass("current");
                $(this).addClass("current");
                // 取消上次延时未执行的方法
                clearTimeout(TimeFn);
                //执行延时
                TimeFn = setTimeout(function(){
                    //do function在此处写单击事件要执行的代码 
                	initPage() 
                },300);

            });

            // 双击修改角色名称按钮
           /* $(".menu-ul li").dblclick(function() {
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
                    $("#loading").css('display','block');
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
            });*/

            /*// 添加按钮
            $(".add-menu").click(function() {
                reset();
                $("#roleModal").modal("show");
                formValidator();
            }); 

            menuButton(); 
            // 关联人员 
            var _id = $(".menu-ul").find(".current").children("span").text();
             */
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
 
 
//初始化页面信息
function initPage(){ 
	var testWork=[{columnComment: "任务编号"},{columnComment: "任务描述"},{columnComment: "管理岗"},{columnComment: "执行人"},{columnComment: "关联系统"},{columnComment: "关联需求"},{columnComment: "所属处室"},{columnComment: "投产窗口"},{columnComment: "任务状态"},
		{columnComment: "预计系测开始"},{columnComment: "预计系测结束"},{columnComment: "预估系测工作量(人天)"},{columnComment: "预计版测开始"},{columnComment: "预计版测结束"},{columnComment: "预估版测工作量(人天)"},{columnComment: "所属处室"},{columnComment: "投产窗口"}];
	
	var testCase=[{columnComment: "涉及系统"},{columnComment: "案例类型"},{columnComment: "案例名称"},{columnComment: "前置条件"},{columnComment: "正向/反向"}]
	
	var defectData=[{columnComment: "工作任务"},{columnComment: "所属系统"},{columnComment: "缺陷摘要"},{columnComment: "缺陷类型"},{columnComment: "缺陷来源"},
		{columnComment: "严重级别"},{columnComment: "紧急程度"},{columnComment: "修复轮次"},{columnComment: "将此缺陷指派给"},{columnComment: "缺陷描述"}]
	var devWork=[{columnComment: "任务编号"},{columnComment: "任务描述"},{columnComment: "管理岗"},{columnComment: "执行人"},{columnComment: "关联系统"},{columnComment: "关联需求"},{columnComment: "所属处室"},{columnComment: "投产窗口"},{columnComment: "任务状态"},
		{columnComment: "预计系测开始"},{columnComment: "预计系测结束"},{columnComment: "预估系测工作量(人天)"},{columnComment: "预计版测开始"},{columnComment: "预计版测结束"},{columnComment: "预估版测工作量(人天)"},{columnComment: "所属处室"},{columnComment: "投产窗口"}];

	var devTask=[{columnComment: "任务编号"},{columnComment: "任务描述"},{columnComment: "管理岗"},{columnComment: "执行人"},{columnComment: "关联系统"},{columnComment: "关联需求"},{columnComment: "所属处室"},{columnComment: "投产窗口"},{columnComment: "任务状态"},
		{columnComment: "预计系测开始"},{columnComment: "预计系测结束"},{columnComment: "预估系测工作量(人天)"},{columnComment: "预计版测开始"},{columnComment: "预计版测结束"},{columnComment: "预估版测工作量(人天)"},{columnComment: "所属处室"},{columnComment: "投产窗口"}];
	var _id = $(".menu-ul").find(".current").children("span").text(); 
	$.ajax({
        url : "/system/fieldTemplate/findFieldByTableName ",
        method : "post",
        data : {
            "tableName" : _id, 
        }, 
        success : function(data) { 
        	console.log( data );
        	 dataType = data.type;
        	 $("#loading").css('display','none'); 
        	 //不可编辑字段内容
        	 $( ".non-editable" ).empty(); 
        	 
        	 //数据库表中已有的字段：默认字段，只能由建表语句修改
        	 var dataField=[]; 
        	 //测试任务
        	 if( _id == "tbl_requirement_feature" ){
        		  dataField =  testWork;
        	 }else if( _id == "tbl_case_info" ){//测试案例
        		  dataField =  testCase;
        	 }else if( _id == "tbl_defect_info" ){//测试缺陷
        		  dataField =  defectData;
        	 }else if(_id="tbl_requirement_feature_itmpdb"){//开发任务
				 dataField =  devWork;
			 }else if(_id="tbl_dev_task_itmpdb"){//开发工作任务
				 dataField =  devTask;
			 }
        	 var str=$( '<div class="rowdiv"></div>' );
        	 for( var i=0;i<dataField.length;i++ ){ 
        		 var childStr=$('<div class="def_col_6">'+ dataField[i].columnComment +'</div>'); 
        		 str.append( childStr ); 
        		 //6个字段显示一排
        		 if( i%6 == 5 ){
        			 $(".non-editable").append( str );
        			 str=$( '<div class="rowdiv"></div>' );
        		 }
        	 }
        	 $(".non-editable").append( str );
        	 //下面扩展属性字段 
        	 initEditFieldTable( data.field,data.type );
        	 
        }
	}); 
}

//展示自定义字段
function initEditFieldTable( data , type ){ 
	$("#editFieldTable").bootstrapTable('destroy'); 
	if( data == undefined ){
		return ;
	}
	$("#editFieldTable").bootstrapTable({
		pagination : false, 
        sidePagination:'client',
        data:data,
        showFooter: false, 
	    columns : [{
	        field : "fieldName",
	        title : "字段名称",
	        align : 'center',
	        class : 'editFieldName',
	        width : '120px' ,
	        formatter : function(index, rows, value) {
	        	var aClass=''; 
	        	if( rows.status == '2' ){
	        		aClass='invalidClass2';
	            } 
	            return '<span class="'+aClass+'">'+rows.fieldName+'</span>';
	        }
	    },{
	        field : "label",
	        title : "显示名称",
	        align : 'center',
	        class : 'editLabel',
	        width : '120px',
	        formatter : function(index, rows, value) {
	            var str='<input class="form-control" value="'+ rows.label +'" />';
	            return str;
	        }
	    },{
	        field : "type",
	        title : "数据类型",
	        width : '120px',
	        class : 'editType',
	        align : 'center',
	        formatter : function(index, rows, value) { 
	        	var str='<select class="selectpicker sType" value="'+ rows.type +'"><option value>请选择</option>';
	        	for( var i=0;i<type.length;i++ ){
	        		if( rows.type == type[i].typeValue ){
	        			str+='<option value='+ type[i].typeValue +' selected = "selected" >'+ type[i].typeKey +'</option>'
	    	        }else{
	    	        	str+='<option value='+ type[i].typeValue +' >'+ type[i].typeKey +'</option>'
	    	        }
	        	} 
	        	str+='</select>'
	            return str;
	        },events: {
	            'change .sType': function (e, value, row, index) { 
	            	var This=this; 
	            	layer.confirm('切换数据类型会还原数据限制，是否继续？', { 
            		    btn: ['继续','不继续'] //按钮
            		}, function( ){   
            			$(This).attr( "value" , $(This).val() );
            			tableOpt( This , e, value, row, index );
            			layer.closeAll();
            		}, function( ){   
            			$(This).val( $(This).attr( "value" ) );
            			$('.selectpicker').selectpicker('refresh');
            			layer.closeAll();
            		}); 
	            },
	        }
	    },{
	        field : "defaultValue",
	        title : "数据限制",
	        align : 'center',
	        class : 'dataLimit',
	        formatter : function(index, rows, value) {
	        	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
	        	var str='';
	        	if( rows.type == 'int' ){
	        		//数字
	        		str+= '<div class="def_col_18"><label class="def_col_12 control-label font_right fontWeihgt" >最大长度：</label>' 
		            +'<div class="def_col_24"><input type="number" min="0" class="form-control maxLen"   value="'+ rows.maxLength +'"  /></div></div>'
		            +'<div class="def_col_18"><label class="def_col_12 control-label font_right fontWeihgt">默认值：</label>'  
		            +'<div class="def_col_24"><input type="text" class="form-control defVal" value="'+ rows.defaultValue +' " /></div></div>';
	        	}else if( rows.type == 'float' ){
	        		//浮点
	        		str+= '<div class="def_col_36"><label class="def_col_6 control-label font_right fontWeihgt">默认值：</label>' 
		            +'<div class="def_col_30"><input type="text" class="form-control defVal"  value="'+ rows.defaultValue +'"   /></div></div>';
	        	}else if( rows.type == 'varchar' ){
	        		//文本
	        		str+= '<div class="def_col_18"><label class="def_col_12 control-label font_right fontWeihgt">最大长度：</label>' 
		            +'<div class="def_col_24"><input type="number" min="0" class="form-control maxLen" value="'+ rows.maxLength +'" /></div></div>'
		            +'<div class="def_col_18"><label class="def_col_12 control-label font_right fontWeihgt">默认值：</label>'  
		            +'<div class="def_col_24"><input type="text" class="form-control defVal"  value="'+ rows.defaultValue +'"  /></div></div>';
	        	}else if( rows.type == 'data' ){
	        		//日期
	        		str+= ' ';
	        	}else if( rows.type == 'timestamp' ){
	        		//时间戳
	        		str+= ' ';
	        	}else if( rows.type == 'enum' ){
	        		//枚举值
	        		str+= '<div class="def_col_36"><label class="def_col_6 control-label font_right fontWeihgt">枚举：</label>' 
		            +'<div class="def_col_30 paddingC"><select class="selectpicker" title="点击查看">';
		            for( var i=0;i<rows.enums.length;i++ ){
		            	var disable='';
		        		if(  rows.enums[i].enumStatus == 2 ){
		        			disable="disabled";
		        		}
		            	str+='<option value='+ rows.enums[i].sequence +'  statusEn='+ rows.enums[i].enumStatus +' '+disable+' >'+ rows.enums[i].value +'</option>'
		            }
		            str+='</select><button class="btn btn-primary enumerationBtn" onclick="showEnu( this )"  ><span class="fa fa-search"></span></button></div></div>';
	        	} 
	        	return str;
	        } 
	    },{
	        field : "status",
	        title : "允许空值",
	        align : 'center',
	        class : "isNull",
	        width : '100px',
	        formatter : function(index, rows, value) { 
	            var str='<select class="selectpicker">'; 
	            if( rows.required == "false" ){
	            	str+='<option value="true" >是</option><option value="false" selected = "selected">否</option>'; 
    	        }else{
    	        	str+='<option value="true" selected = "selected" >是</option><option value="false">否</option>';
    	        } 
	            str+= "</select>";
	            return str;
	        }
	    },{
	        field : "opt",
	        title : "操作",
	        align : 'center',
	        class : 'fontCenter editOpt',
	        width : '100px',
	        formatter : function(index, rows, value) {  
	            if( rows.status == '1' ){
	            	return '<a class="a_style" value="1" >置为无效</a>';
	            }else if( rows.status == '2' ){
	            	return '<a class="a_style" value="2" >置为有效</a>';
	            }
	            return '';
	        },
	        events: {
	            'click .a_style': function (e, value, row, index) {
	                invalid( this );
	            },
	        }
	    }],
	     
	}); 
	 $('.selectpicker').selectpicker('refresh');
}       
   
//数据类型改变时刷新扩展字段table页面
function tableOpt( This , e, value, rows, index ){
	var row = JSON.stringify(rows).replace(/"/g, '&quot;');
	$( This ).parents( "tr:first" ).children( ".dataLimit" ).empty();
	var str=''; 
	if(  $( This ).val() == 'int' ){
		//数字
		str+= '<div class="def_col_18"><label class="def_col_12 control-label font_right fontWeihgt" >最大长度：</label>' 
        +'<div class="def_col_24"><input type="number" min="0" class="form-control maxLen"  value="'+ rows.maxLength +'"  /></div></div>'
        +'<div class="def_col_18"><label class="def_col_12 control-label font_right fontWeihgt">默认值：</label>'  
        +'<div class="def_col_24"><input type="text" class="form-control defVal" value="'+ rows.defaultValue +'" /></div></div>';
	}else if(  $( This ).val() == 'float' ){
		//浮点
		str+= '<div class="def_col_36"><label class="def_col_6 control-label font_right fontWeihgt">默认值：</label>' 
        +'<div class="def_col_30"><input type="text" class="form-control defVal"  value="'+ rows.defaultValue +'"   /></div></div>';
	}else if(  $( This ).val() == 'varchar' ){
		//文本
		str+= '<div class="def_col_18"><label class="def_col_12 control-label font_right fontWeihgt">最大长度：</label>' 
        +'<div class="def_col_24"><input type="number" min="0" class="form-control maxLen" value='+ rows.maxLength +' /></div></div>'
        +'<div class="def_col_18"><label class="def_col_12 control-label font_right fontWeihgt">默认值：</label>'  
        +'<div class="def_col_24"><input type="text" class="form-control defVal"  value="'+ rows.defaultValue +'"  /></div></div>';
	}else if(  $( This ).val() == 'data' ){
		//日期
		str+= ' ';
	}else if(  $( This ).val() == 'timestamp' ){
		//时间戳
		str+= ' ';
	}else if(  $( This ).val() == 'enum' ){
		//枚举值
		str+= '<div class="def_col_36"><label class="def_col_6 control-label font_right fontWeihgt">枚举：</label>' 
        +'<div class="def_col_30 paddingC"><select class="selectpicker">';
        for( var i=0;i<rows.enums.length;i++ ){
        	str+='<option value='+ rows.enums[i].sequence +' statusEn='+ rows.enums[i].enumStatus +' >'+ rows.enums[i].value +'</option>';
        }
        str+='</select><button class="btn btn-primary enumerationBtn" onclick="showEnu( this )" ><span class="fa fa-search"></span></button></div></div>';
	} 
	$( This ).parents( "tr:first" ).children( ".dataLimit" ).append( str );
	$('.selectpicker').selectpicker('refresh');
}        
function invalid( This ){ 
	if( $( This ).attr( "value" )=="1" ){
		$( This ).attr( "value","2" ); 
		$( This ).parent().parent().find(".editFieldName").children( "span" ).addClass( "invalidClass2" )
		$( This ).text("置为有效");
	}else if( $( This ).attr( "value" )=="2" ){
		$( This ).attr( "value","1" );
		$( This ).parent().parent().find(".editFieldName").children( "span" ).removeClass( "invalidClass2" )
		$( This ).text("置为无效");
	}
}        
function checkData(){
	if( $( "#new_fieldName" ).val().indexOf(" ") != -1 || $( "#new_fieldName" ).val() == ''){
		 layer.alert("输入不能为为空，且不能输入空格", {
             icon: 0,
             title: "提示信息"
         });
	}else { 
		addEditTabelData(); 
	}
}       

//添加自定义属性表格数据
function addEditTabelData(){
	var data = getEditTableData();
	var oneData={};
	oneData.fieldName=$( "#new_fieldName" ).val();
	oneData.label='';
	oneData.enums=[];
	oneData.defaultValue='';
	oneData.maxLength=0;
	oneData.required=true;
	oneData.status=1; 
	oneData.type="";  
	 
	var flag = false;
	data.map(function(ele,index){
		if( ele.fieldName == oneData.fieldName ){
			flag = true;
			layer.alert("字段已经存在，请重新输入！", {
              icon: 0,
              title: "提示信息"
            }); 
		} 
	})
	if(flag){
		return;
	}
	data.push( oneData );  
	
	initEditFieldTable( data , dataType );
	
	$('.selectpicker').selectpicker('refresh');
	layer.closeAll();
}        
  
//枚举类型按钮事件：弹出框显示数据字典内容
function showEnu( This ){
	//清空 枚举弹框 选项
	$( "#newDicName" ).val("");
	$( "#newSequence" ).val("");
	
	thisData=This;
	$("#newDicForm").data('bootstrapValidator').destroy(); 
	formValidator();
	
	var data=[];
	for( var i=0;i<$( This ).parent().find( "select" ).children("option").length;i++ ){
		if( $( This ).parent().find( "select" ).children("option").eq( i ).attr( "value" ) == '' ){
			continue;
		}
		var obj={};
		obj.sequence=$( This ).parent().find( "select" ).children("option").eq( i ).attr( "value" );
		obj.enumStatus=$( This ).parent().find( "select" ).children("option").eq( i ).attr( "statusEn" );
		obj.value=$( This ).parent().find( "select" ).children("option").eq( i ).text();
		data.push( obj );
	}    
	initDictionaryConfigTable( data );  
    $("#enuModul").modal( "show" );
}   

//枚举类型弹框页面表格展示
 function initDictionaryConfigTable( data ){ 
	 $("#dictionaryConfigTable").bootstrapTable('destroy'); 
	 $("#dictionaryConfigTable").bootstrapTable({
	     pagination : false,
	     data:data,
	     columns : [{
	         field : "value",
	         class : "dicValue",
	         title : "字典项名称",
	         align : 'center',
	         formatter : function(index, rows, value) {
	        	 var aclass='';
	        	 if( rows.enumStatus == 2 ){ 
	        		 aclass="invalidClass2"
	        	 } 
	             return  '<span class="'+ aclass +'">'+ rows.value +'</span>';
	         }
	     },{
	         field : "sequence",
	         title : "排序",
	         class : "dicSequence",
	         align : 'center',
	         formatter : function(index, rows, value) {
	        	 var aclass='';
	        	 if( rows.enumStatus == 2 ){ 
	        		 aclass="invalidClass2"
	        	 } 
	             return  '<span class="'+ aclass +'">'+ rows.sequence +'</span>';
	         }
	     },{
	         field : "opt",
	         title : "操作", 
	         align : 'center',
	         class : 'opt dicOpt',
	         formatter : function(index, rows, value) {
	        	 var del='';
	        	 if( rows.enumStatus == 1 ){
	        		 del='<a class="a_style" onclick="setInvalid(this)" value="1" >置为无效</a>';
	        	 }else if( rows.enumStatus == 2 ){
	        		 del='<a class="a_style" onclick="setInvalid(this)" value="2">置为有效</a>';
	        	 } 
	             return  del;
	         }
	     }], 
	 }); 
 }    
        
        
        
//表单验证相关
function formValidator(){
	$("#newDicForm").bootstrapValidator({
        excluded : [ ':disabled','hidden' ],
        message: 'This value is not valid',//通用的验证失败消息
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        }, 
        fields : {
        	newDicName : {
                validators : {
                    notEmpty:{
                        message: '字典项名称不能为空！'
                    } 
                }
            },
            newSequence : {
                validators : {
                    notEmpty:{
                        message: '请输入大于0的正数！'
                    },
                    regexp: {
                        regexp: /^[1-9]\d*$/,
                        message: '请输入的正整数！'
                    } 
                }
            }
        }
    });

}
//添加一条新的字典项名称
function addOnesDataToDicTable(){
	$('#newDicForm').data('bootstrapValidator').validate();  
	if( !$('#newDicForm').data("bootstrapValidator").isValid() ){
		return;
	}
	var allTableData = $( "#dictionaryConfigTable" ).bootstrapTable('getData');
	  
	//判断字典项名称是否重复
	var flag=true;
	for( var i=0;i<allTableData.length;i++ ){
		if( allTableData[i].value == $( "#newDicName" ).val() ){
			flag=false;
		} 
	}
	if( flag ){ 
		//不重复，添加数据，根据排序值 排序
		var oneData={}
		oneData.value = $( "#newDicName" ).val();
		oneData.sequence = $( "#newSequence" ).val();
		oneData.enumStatus = '1';
		allTableData.push( oneData );
		//选择排序法
		for(var i = 0; i < allTableData.length - 1; i++){ 
			for(var j = i + 1; j < allTableData.length; j++){
				if(allTableData[i].sequence > allTableData[j].sequence ){
					var tmp = allTableData[i];
					allTableData[i] = allTableData[j];
					allTableData[j] = tmp;
				}
			}
		}    
		//重载表格
		initDictionaryConfigTable( allTableData );  
	}else{
		layer.alert("数据重复", {
            icon: 2,
            title: "提示信息"
        });
	}
}   
        
function setInvalid(This){ 
	if( $(This).attr("value")==1 ){
		$(This).attr("value",2);
		$(This).parent("td").parent("tr").find( "span" ).addClass( "invalidClass2" ); 
		$(This).text("置为有效");
	}else if($(This).attr("value")==2){
		$(This).attr("value",1);
		$(This).parent("td").parent("tr").find( "span" ).removeClass( "invalidClass2" ); 
		$(This).text("置为无效");
	}
}

//获取扩展属性数据
function getEditTableData(){ 
	var all=[];
	var flag=true;
	for( var i=0;i<$("#editFieldTable tbody>tr").length;i++  ){
		var obj={};
		
		obj.fieldName=$("#editFieldTable tbody>tr").eq(i).find(".editFieldName").text();
		 
		obj.label=$("#editFieldTable tbody>tr").eq(i).children( ".editLabel" ).find("input").val();
		 
		obj.enums=[];
		obj.maxLength=0;
		obj.defaultValue='';
		
		obj.type=$("#editFieldTable tbody>tr").eq(i).children(".editType").find("select").val(); 
		
		if( $("#editFieldTable tbody>tr").eq(i).children(".editType").find("select").val() == "int" ){
			obj.maxLength= $("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find(".maxLen").val();
			obj.defaultValue=$("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find(".defVal").val();
			if( obj.maxLength === '' ){
				flag=false; 
			}
		}else if( $("#editFieldTable tbody>tr").eq(i).children(".editType").find("select").val() == "float" ){ 
			obj.defaultValue=$("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find(".defVal").val(); 
		}else if( $("#editFieldTable tbody>tr").eq(i).children(".editType").find("select").val() == "varchar" ){
			obj.maxLength= $("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find(".maxLen").val();
			obj.defaultValue=$("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find(".defVal").val();
			if( obj.maxLength === '' ){
				flag=false; 
			}
		}else if( $("#editFieldTable tbody>tr").eq(i).children(".editType").find("select").val() == "enum" ){  
			for( var j=0;j<$("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find("select").children("option").length;j++ ){
				if( $("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find("select").children("option").eq( j ).attr( "value" ) == '' ){
					continue;
				}
				var enumObj={};
				enumObj.enumStatus = $("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find("select").children("option").eq( j ).attr( "statusen" );
				enumObj.sequence = $("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find("select").children("option").eq( j ).attr( "value" );
				enumObj.value = $("#editFieldTable tbody>tr").eq(i).children(".dataLimit").find("select").children("option").eq( j ).text();
				obj.enums.push( enumObj );
			}  
		}  
		obj.required = $("#editFieldTable tbody>tr").eq(i).children(".isNull").find("select").val();
		obj.status = $("#editFieldTable tbody>tr").eq(i).children(".editOpt").find(".a_style").attr("value");   
		
		if( obj.label == '' ){
			flag=false; 
		}
		
		all.push( obj );
 	} 
	if( flag == false ){
		layer.alert("显示名称、最大长度值不能为空，请检查表格数据", {
            icon: 2,
            title: "提示信息"
        });
		return  false;
	}
	return all;
}

//保存
function saveAll(){ 
	var obj = {};
	obj.field = {};
	obj.field = getEditTableData();
	if( obj.field == false ){
		return ;
	}
	$.ajax({
        url : "/system/fieldTemplate/saveFieldTemplate",
        method : "post",
        data : {
            "customForm" : $(".menu-ul").find(".current").children("span").text(), 
            "customField" : JSON.stringify( obj ),  
        },
        success : function(data) {
        	 layer.alert('保存成功！', {
                 icon: 1,
                 title: "提示信息"
             });
        }
	}); 
	
}

//枚举类型弹框页面保存
function changeThisSelect(){
	var data=getDicConfigTable(); 
	$( thisData ).parent().find( "select" ).empty();
	for( var i=0;i<data.length;i++ ){
		var disable='';
		if( data[i].enumStatus == 2 ){
			disable="disabled";
		}
		var str='<option value="'+ data[i].sequence +'" statusEn="'+ data[i].enumStatus +'"  '+ disable +' >'+ data[i].value +'</option>';
		$( thisData ).parent().find( "select" ).append( str );
    }
	$('.selectpicker').selectpicker('refresh');
	$("#enuModul").modal( "hide" );
}

//枚举类型弹框获取数据字典数据
function getDicConfigTable(){
	var all=[]
	for( var i=0;i<$("#dictionaryConfigTable tbody>tr" ).length;i++ ){
		var obj={};  
		obj.value=$("#dictionaryConfigTable tbody>tr" ).eq(i).children(".dicValue").text();
		obj.sequence=$("#dictionaryConfigTable tbody>tr" ).eq(i).children(".dicSequence").text();
		obj.enumStatus=$("#dictionaryConfigTable tbody>tr" ).eq(i).children(".dicOpt").find(".a_style").attr( "value" );
		all.push( obj );
	}
	return all;
}








