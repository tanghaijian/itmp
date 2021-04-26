

//同任务
function sameReq( This ){
	  $(This).parents(".onedatas").find(".sWorkSummary1").eq(0).val($("#ataskName").val()).change();
	  $(This).parents(".onedatas").find(".sWorkOverView1").eq(0).val($("#taskOverview").val()).change();
}

// 添加工作任务
function addWorkJob(){
	var option = $("#splitDevTaskPriority").find("option"); 
	var splitDevTaskType = $("#splitDevTaskType").find("option");
	var userId = 'user_'+ +new Date();
	var str = '<div class="onedatas canEdit">'+
	    '<div class="del_icon" onclick="delWorks(this)"> <span class="fa fa-times"></span> </div>'+
		'<div class="rowdiv">'+
			'<div class="form-group def_col_36">'+
				'<label class="def_col_4 control-label fontWeihgt"><span class="redStar">*</span>任务名称：</label>'+
				'<div class="def_col_28"><input type="text" class="form-control sWorkSummary1 placeholder="请输入" name="sWorkSummary1" /></div>'+
				'<button type="button" onclick="sameReq(this)" class=" btn btn-primary" style="margin-left: 20px;">同任务</button>'+
			'</div>'+
		'</div>'+
		'<div class="rowdiv">'+
			'<div class="form-group def_col_36">'+
				'<label class="def_col_4 control-label fontWeihgt"><span class="redStar">*</span>任务描述：</label>'+
				'<div class="def_col_32">'+
					'<textarea class="form-control sWorkOverView1" placeholder="请输入" name="sWorkOverView1"></textarea>'+
				'</div>'+
			'</div>'+
		'</div>'+ 
		'<div class="rowdiv">'+
			'<div class="form-group def_col_12">'+
				'<label class="def_col_12 control-label fontWeihgt">预计开始时间：</label>'+
				'<div class="def_col_24">'+
					'<input size="16" type="text" readonly class="form-control pointStyle readonly form_datetime sWorkStart1" readonly placeholder="请选择日期" name="startWork1" />'+
					'<span onclick="clearCon(this)" class="btn_clear"></span>'+
				'</div>'+
			'</div>'+
			'<div class="form-group def_col_12">'+
				'<label class="def_col_12 control-label fontWeihgt">预计结束时间：</label>'+
				'<div class="def_col_24">'+
					'<input size="16" type="text" readonly class="form-control pointStyle readonly form_datetime sWorkEnd1" readonly placeholder="请选择日期" name="endWork1" />'+
					'<span onclick="clearCon(this)" class="btn_clear"></span>'+
				'</div>'+
			'</div>'+
			'<div class="form-group def_col_12">'+
				'<label class="def_col_19  control-label fontWeihgt"><span class="redStar">*</span>预计工作量(人天)：</label>'+
				'<div class="def_col_17 "><input type="text" class="form-control sWorkPlanWorkload1" placeholder="请输入" name="sWorkPlanWorkload1" /></div> '+
			'</div>'+
		'</div>'+
		'<div class="rowdiv ss">'+
			'<div class="form-group def_col_12">'+
				'<label class="def_col_12 control-label fontWeihgt">优先级：</label>'+
				'<div class="def_col_24 chanceMyself">'+
					'<select class="selectpicker splitDevTaskPriority1" name="splitDevTaskPriority">';
					 for( var i = 0; i < option.length ; i++ ){
						 str+='<option value="'+ option[i].value +'">'+ option[i].text +'</option>';
					 }
					 str+='</select>'+
				'</div>'+
			'</div>'+
			'<div class="form-group def_col_12">'+
				'<label class="def_col_12 control-label fontWeihgt"><span class="redStar">*</span>任务分配：</label>'+
				'<div class="def_col_24 chanceMyself">'+
					'<input class="sWorkDivid1" type="hidden" />'+
					'<input onclick="tShowWithinManPopup(this)" id="'+userId+'" type="text" class="form-control pointStyle readonly def_col_22 sWorkDividUserName1" name="sWorkDividUserName1" />'+
					'<div class="def_col_14"><button type="button" class="btn btn-primary chanceMyselfBtm" onclick = "selectMyselfDefect(this)">选自己</button></div>'+
				'</div>'+
			'</div>'+ 
        '</div>'+
	'</div>';
	$( "#works" ).append( str );
	$('.selectpicker').selectpicker('refresh');
	$('input[type="text"]').parent().css("position","relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange",function(){
    	if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    }) 
	addDateChoose(); 
	addValidator();
}

//初始化时间选择器
function addDateChoose(){ 
//	laydate.render({
//        elem: '.sWorkStart1'
//    });
//    laydate.render({
//        elem: '.sWorkEnd1'
//    });
    $(".sWorkStart1").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left",
    }).on('changeDate',function(e) {   
    	 $( e.currentTarget ).change(); 
    	 $( this ).parent().parent().next().find("input").change(); 
    	 $( this ).parent().find('.btn_clear').show();
    }); 
    $(".sWorkEnd1").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('changeDate',function(e) {
        $( e.currentTarget ).change();  
		$( this ).parent().parent().prev().find("input").change(); 
		$( this ).parent().find('.btn_clear').show();
    });   
}
 
function delWorks( This ){
	$( This ).parents(".onedatas").remove();
	addValidator();
}

//添加校验
function addValidator(){
	$("#new_WorkTaskForm").bootstrapValidator('removeField','sWorkSummary1');
	$("#new_WorkTaskForm").bootstrapValidator('removeField','sWorkOverView1');
	$("#new_WorkTaskForm").bootstrapValidator('removeField','startWork1');
	$("#new_WorkTaskForm").bootstrapValidator('removeField','endWork1');
	$("#new_WorkTaskForm").bootstrapValidator('removeField','sWorkPlanWorkload1');
	$("#new_WorkTaskForm").bootstrapValidator('removeField','sWorkDividUserName1');

	$("#new_WorkTaskForm").bootstrapValidator("addField", "sWorkDividUserName1", {
		  trigger:"change",
	   	  validators: {
	       	 notEmpty: {
	                message: '执行人不能为空'
	            },
	       }
	}); 
	$("#new_WorkTaskForm").bootstrapValidator("addField", "sWorkSummary1", {
	  trigger:"change",
   	  validators: {
       	 notEmpty: {
                message: '任务名称不能为空'
            },
//            stringLength: {
//                min:5,
//                max: 300,
//                message: '长度必须在5-300之间'
//            }
       }
	}); 
	$("#new_WorkTaskForm").bootstrapValidator("addField", "sWorkOverView1", {
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
	}); 
	$("#new_WorkTaskForm").bootstrapValidator("addField", "startWork1", {
		trigger:"change",
	   	validators: {
      	   notEmpty: {
               message: '开始日期不能为空'
           },
           callback:{/*自定义，可以在这里与其他输入项联动校验*/
               message: '开始时间必须小于结束日期！',
               callback:function(value, validator,$field){ 
               	if( value == "" ){
               		return true;
               	}else{
               		if( $field.parent().parent().next().find( "input" ).val() == '' ){
               			return true;
               		}else{
               			var start = new Date( value );
               			var end = new Date( $field.parent().parent().next().find( "input" ).val() );
               			if( start.getTime() > end.getTime() ){
               				return false;
               			} 
               			return true;
               		}
               	}
            }
   		 }
	    }
	});
	$("#new_WorkTaskForm").bootstrapValidator("addField", "endWork1", {
		trigger:"change",
	   	validators: {
         	 notEmpty: {
                  message: '结束日期不能为空'
             },
             callback:{/*自定义，可以在这里与其他输入项联动校验*/
                 message: '结束日期必须大于开始日期！',
                 callback:function(value, validator,$field){ 
                 	if( value == "" ){
                 		return true;
                 	}else{
                 		if( $field.parent().parent().prev().find( "input" ).val() == '' ){
                 			return true;
                 		}else{
                 			var start = new Date($field.parent().parent().prev().find( "input" ).val() );
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
	});
    $("#new_WorkTaskForm").bootstrapValidator("addField", "sWorkPlanWorkload1", {
        validators: {
            notEmpty: {
                message: '预计工作量不能为空'
            },
            regexp: {
                regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
                message: '请输入大于0且小于等于500的正数'
            },
        }
    });
} 

function clearCon( that ){
	 $(that).parent().children('input').val("").change();
}







