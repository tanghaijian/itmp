function initemCheck(callback,opts){
	var check = false;
	$('.empty_check').each(function(index){
		var name = $(this).attr("name");
		if(empty_check($(this),name))
			check = true;
		
	});
	$('.double_check').each(function(index){
		var name = $(this).attr("name");
		if(double_check($(this),name))
			check = true;
		
	});
	$('.select_check').each(function(index){
		var name = $(this).attr("name");
		if(select_check($(this),name))
			check = true;
		
	});
	$('.date_check').each(function(index){
		var name = $(this).attr("name");
		if(date_check($(this),name))
			check = true;
		
	});
	$('.number_check').each(function(index){
		var name = $(this).attr("name");
		if(number_check($(this),name))
			check = true;
		
	});
	
	if(!check){
		if(typeof opts == "undefined")
		    callback();
		else
			callback(opts);
	}
}
function number_check(obj,name){
	if(obj!=undefined && obj!=null){
		var numberValue=obj.val();
		var tableName = obj.siblings("input[name='tableName']").val();
		var refId = obj.siblings("input[name='refId']").val();
		var columnName = obj.siblings("input[name='columnName']").val();
		var data="numberValue="+numberValue+"&tableName="+tableName+"&refId="+refId+"&columnName="+columnName;
		data = encodeURI(data);
		var returnFlag = false;
		$.ajax({
				type : "POST", 
				url : "checkNumberByAjax",
				async : false,
				dataType : "json", 
		 		contentType :"application/x-www-form-urlencoded;charset=UTF-8",
		 		data : data,
		 		success:function(msg){
		    	       if(msg.resultSuccess == 1){
		    	    	   var flag = msg.resultData;
		    	    	   if(flag == 2){
		    	    		   $.gritter.add({
		    	   				title: '唯一性校验提示',
		    	   				text: name+'与历史数据重复。',
		    	   				class_name: 'gritter-error gritter-light'
			    	   			});
			    	   			obj.focus();
			    	   			returnFlag = true;
		    	    	   }
		    	       }
		    	       else{
		    	    	   $.gritter.add({
		    					title: '唯一性校验提示',
		    					text: '查询'+name+'与历史数据失败。',
		    					class_name: 'gritter-error gritter-light'
		    				});
		    				obj.focus();
		    				returnFlag = true;
		    	       }
		 			  },
		 		     error:function(msg){
		 		    	  $.gritter.add({
		    					title: '唯一性校验提示',
		    					text: '查询'+name+'与历史数据失败。',
		    					class_name: 'gritter-error gritter-light'
		    				});
		    				obj.focus();
		    				returnFlag = true;
		 			  }
		    	 
				 });
		
		return returnFlag;
	}
	return true;
}
function double_check(obj,name){
	if(obj!=undefined && obj!=null){
		var value=obj.val();
		if(value.length==0){
			$.gritter.add({
				title: '数字校验提示',
				text: name+'不能为空。',
				class_name: 'gritter-error gritter-light'
			});
			obj.focus();
			return true;
		}else if(!/^(\d+[.]{1}\d{1,2})$/.test(value)&&!/^\d+$/.test(value)){
			$.gritter.add({
				title: '数字校验提示',
				text: name+"格式错误； 应该是数字或者小数！小数保留2位小数。",
				class_name: 'gritter-error gritter-light'
			});
			obj.focus();
			return true;
		}
		return false;
	}
	return true;
}

function empty_check(obj,name){
	if(obj!=undefined && obj!=null){
		var value=obj.val();
		if(toStr(value)==""){
			$.gritter.add({
				title: '非空校验提示',
				text: name+'不能为空。',
				class_name: 'gritter-error gritter-light'
			});
			obj.focus();
			return true;
		}
		return false;
	}
	return true;
}

function select_check(obj,name){
	if(obj!=undefined && obj!=null){
		var value=obj.val();
		if(toStr(value)=="" || value == 0){
			$.gritter.add({
				title: '下拉框校验提示',
				text: '请选择'+name,
				class_name: 'gritter-error gritter-light'
			});
			obj.focus();
			return true;
		}
		return false;
	}
	return true;
}

//验证日期：yyyy-MM-dd
function date_check(obj,name){
	if(obj!=undefined && obj!=null){
		var value1=obj.val();
		if(value1.length==0||value1==0){
			$.gritter.add({
				title: '日期校验提示',
				text: name+"不能为空。",
				class_name: 'gritter-error gritter-light'
			});
			obj.focus();
			return true;
		}else if(!/^\d{4}[-]{1}([0]{1}[0-9]{1}||([1]{1}[0-2]{1}))[-]{1}([0-2]{1}[0-9]{1}||[3]{1}[0-1]{1})$/.test(value1)){
			$.gritter.add({
				title: '日期校验提示',
				text: name+"，格式不对，正确格式是（yyyy-MM-dd [2017-01-01]）",
				class_name: 'gritter-error gritter-light'
			});
			obj.focus();
			return true;
		}
		return false;
	}
	return true;
}