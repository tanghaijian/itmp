var curTimer; //公共定时器，
var fileArr;

/**
 * 跳转到页面
 * @param url 页面url
 * @param params 参数
 * @returns
 */
function forwordPage(url,params){

    var menuObj = $("#sidebar").find("li[class='open active']:last").find("a:eq(0)");
    var menuId = menuObj.attr("id");
	$(".main-content").empty();
	var userId = $("#userId").val();
	//fc防止缓存
	var opt = {menuId:menuId,userId:userId,fc:new Date().getTime()};
	var data = {};
	if(typeof params != "undefined")
	    data =  $.extend({}, opt,params);
	else
		data = opt;
	//动态加载css和js
	$(".main-content").load(url,data,function(responseText,status){
		$('loadblock script').remove();
		$('loadblock link').remove();
		if(status == "error"){
			 $(".main-content").html(responseText);
		}
		else{
			var keyUrl = url.split("?")[0];
			fileArr = typeof urlMap[keyUrl] == "undefined"?[]:urlMap[keyUrl];
			if(typeof fileArr != "undefined"&&fileArr.length > 0){
				loadResource(0);
			}
	}
	});

}

/**
 * 跳转
 * @param url 页面url
 * @param params 参数
 * @param token token
 * @returns
 */
function forwordPage(url,params,token){

    var menuObj = $("#sidebar").find("li[class='open active']:last").find("a:eq(0)");
    var menuId = menuObj.attr("id");
	/*$(".main-content").empty();*/
	var userId = $("#userId").val();
	//fc防止缓存
	var opt = {menuId:menuId,userId:userId,fc:new Date().getTime()};
	var data = {};
	if(typeof params != "undefined")
	    data =  $.extend({}, opt,params);
	else
		data = opt;
	$("#myiframe").attr("src",url);
	
//	$.ajax({
//	    type: "post",
//	    contentType: "application/json",
//	    url: url,
//	    data: data,
//	    beforeSend: function (request) {
//	    	request.setRequestHeader("token", token);
//	    },
//	    success: function (responseText) { 
//	    	$('loadblock script').remove();
//			$('loadblock link').remove();
//			console.log( responseText )
//			//$(".main-content").attr("src",url);
//	    },
//	    error: function () {
//	    	var keyUrl = url.split("?")[0];
//			fileArr = typeof urlMap[keyUrl] == "undefined"?[]:urlMap[keyUrl];
//			if(typeof fileArr != "undefined"&&fileArr.length > 0){
//				loadResource(0);
//			}
//		}
//	});

}

//防止依赖出错，进行顺序加载js
function loadResource(count){
   if (count < fileArr.length) {
		 loadFile(fileArr[count], function () {
			 count ++;
		 loadResource(count);
		});
	}
}

//加载js和css文件
function loadFile(file,fn){
	var typeIndex = file.lastIndexOf(".");
	if(typeIndex < 0)
		return;
	var fileType = file.substring(typeIndex+1,file.length);
	if(fileType.toUpperCase() == "JS")
		loadJS(file,fn);
	else
		if(fileType.toUpperCase() == "CSS")
			loadCSS(file,fn);
}

//加载js文件
function loadJS(file,fn){
	var obj = $("script[src='"+file+"']");
	if(obj.length > 0)
		return;
	var script = document.createElement('script');
	script.type='text/javascript';
	script.src=file;
	script.onload = script.onreadystatechange = function () {
		if (!script.readyState || 'loaded' === script.readyState || 'complete' === script.readyState) {
		fn && fn();
		}
		};
	var load = document.getElementsByTagName('loadblock'); 
    load[0].appendChild(script);  
}
//加载css文件
function loadCSS(file,fn){
	var obj = $("style[href='"+file+"']");
	if(obj.length > 0)
		return;
	var css = document.createElement('link');
	css.rel='stylesheet';
	css.href=file;
	css.onload = css.onreadystatechange = function () {
		if (!css.readyState || 'loaded' === css.readyState || 'complete' === css.readyState) {
		fn && fn();
		}
		};
	var load = document.getElementsByTagName('loadblock'); 
    load[0].appendChild(css); 
	
}



/**
 * 吐丝消息
 */
function Toast(msg,duration){
	duration=isNaN(duration)?2000:duration;
	var m = document.createElement('div');
	m.innerHTML = msg;
	m.style.cssText="width: 30%;min-width: 100px;opacity: 0.75;height: 30px;color: rgb(255, 255, 255);line-height: 30px;text-align: center;border-radius: 5px;position: fixed;top: 40%;left: 35%;z-index: 999999;background: rgb(0, 0, 0);font-size: 12px;";
	document.body.appendChild(m);
	setTimeout(function() {
	var d = 0.5;
	m.style.webkitTransition = '-webkit-transform ' + d + 's ease-in, opacity ' + d + 's ease-in';
	m.style.opacity = '0';
	setTimeout(function() { document.body.removeChild(m) }, d * 1000);
	}, duration);
}

/**
 * ajax请求
 * @param url 请求url
 * @param data 请求参数
 * @param sucCallback 成功回调
 * @param errCallback 失败回调
 * @returns
 */
function ajaxPost(url,data,sucCallback,errCallback){
  $.ajax(
		 {
			 type : "POST", 
			 url : url,
			 dataType : "json", 
 		 	 contentType :"application/x-www-form-urlencoded;charset=UTF-8",
 		 	 data : data,
 			 success:function(msg)
 			  {
    	       if(msg.status == 1 && typeof sucCallback == "function")
    	    	   sucCallback(msg);
    	       else if(typeof errCallback == "function")
    	    	   errCallback(msg);
    	       $("#loading-container").hide();
 			  },
 		     error:function(msg,status,throwable){
 		    	 console.log("处理失败！" + throwable);
 				 $("#loading-container").hide();
 			  }
    	 
		 });
}

/**
 * ajax请求
 * @param url 请求url
 * @param data 请求参数
 * @param sucCallback 成功回调
 * @param errCallback 失败回调
 * @param token  token
 * @returns
 */
function ajaxPost(url, data, sucCallback, errCallback, token) {
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : data,
		beforeSend : function(request) {
			request.setRequestHeader("token", token);
		},
		success : function(msg) {
			if (msg.status == 1 && typeof sucCallback == "function")
				sucCallback(msg);
			else if (typeof errCallback == "function")
				errCallback(msg);
			$("#loading-container").hide();
		},
		error : function(msg, status, throwable) {
			console.log("处理失败！" + throwable);
			$("#loading-container").hide();
		}

	});
}

/**
 * ajax请求，contentType=json
 * @param url 请求url
 * @param data 请求数据
 * @param callback1 成功回调
 * @param callback2 失败回调
 * @returns
 */
function ajaxPostByJsonData(url,data,callback1,callback2){
	  $.ajax(
			 {
				 type : "POST", 
				 url : url,
				 dataType : "json", 
	 		 	 contentType :"application/json",
	 		 	 data : data,
	 			 success:function(msg)
	 			  {
	    	       if(msg.status == 1 && typeof callback1 == "function")
	    	    	   callback1(msg);
	    	       else if(typeof callback2 == "function")
	    	    	   callback2(msg);
	    	       $("#loading-container").hide();
	 			  },
	 		     error:function(msg){
	 		    	alert("处理失败！" + msg.responseText||msg.status);
	 				 $("#loading-container").hide();
	 			  }
	    	 
			 });
		}
function SelectAll() {
	$("#dg tr:visible").find("input[name='checkboxID']").each(function(){
		$(this).attr("checked", true);
	});
}
function UnSelectAll(){
	$("#dg tr:visible").find("input[name='checkboxID']").each(function(){
		$(this).attr("checked", false);
	});
}

String.prototype.trim=function() {  
    return this.replace(/(^\s*)|(\s*$)/g,'');  
};  

// 获取字符串长度，汉字3个字节
function getStrLen(str)
{
  var len = 0 ;
  for(var i = 0 ; i< str.length;i++)
  {
	  var length  = str.charCodeAt(i);
	  if(length >= 0 && length <= 128)
	  {
		  len += 1;
	  }
	  else
		  len += 3;
  }
  
  return len;
}

// 验证日期：yyyy-MM-dd
function checkDate(obj,message,textName){
	if(obj!=undefined && obj!=null){
		var value1=obj.val();
		if(value1.length==0||value1==0){
			alert(message);
			obj.focus();
			return false;
		}else if(!/^\d{4}[-]{1}([0]{1}[0-9]{1}||([1]{1}[0-2]{1}))[-]{1}([0-2]{1}[0-9]{1}||[3]{1}[0-1]{1})$/.test(value1)){
			alert(message+"，格式不对，正确格式是（yyyy-MM-dd [2013-03-18]）");
			obj.focus();
			return false;
		}
		return true;
	}
}
// 验证日期通过值：yyyy-MM-dd
function checkDateByValue(value1,message,textName){
	if(value1!=undefined && value1!=null){
 		if(value1.length==0||value1==0){
			alert(message);
 			return false;
		}else if(!/^\d{4}[-]{1}([0]{1}[0-9]{1}||([1]{1}[0-2]{1}))[-]{1}([0-2]{1}[0-9]{1}||[3]{1}[0-1]{1})$/.test(value1)){
			alert(textName+"值"+value1+" 格式不对，正确格式是（yyyy-MM-dd [例如：2013-01-10]）");
 			return false;
		}
		return true;
	}
}
// 验证时间戳:yyyy-MM-dd HH:mm
function checkTimeStampByValue(value1,message,textName){
	if(value1!=undefined && value1!=null){
 		if(value1.length==0||value1==0){
			alert(message);
 			return false;
		}else if(!/^\d{4}[-]{1}([0]{1}[0-9]{1}||([1]{1}[0-2]{1}))[-]{1}([0-2]{1}[0-9]{1}||[3]{1}[0-1]{1})[ ]{1}([0-1]{1}[0-9]{1}||[2]{1}[0-4]{1})[:]{1}[0-5]{1}[0-9]{1}$/.test(value1)){
			alert(textName+"值"+value1+" 格式不对，正确格式是（yyyy-MM-dd HH:mm [例如：2013-01-10 09:30]）");
 			return false;
		}
		return true;
	}
}
// 验证时间戳:yyyy-MM-dd HH:mm:ss
function checkTimeStampSSByValue(value1,message,textName){
	if(value1!=undefined && value1!=null){
 		if(value1.length==0||value1==0){
			alert(message);
 			return false;
		}else if(!/^\d{4}[-]{1}([0]{1}[0-9]{1}||([1]{1}[0-2]{1}))[-]{1}([0-2]{1}[0-9]{1}||[3]{1}[0-1]{1})[ ]{1}([0-1]{1}[0-9]{1}||[2]{1}[0-4]{1})[:]{1}[0-5]{1}[0-9]{1}[:]{1}[0-5]{1}[0-9]{1}$/.test(value1)){
			alert(textName+"值"+value1+" 格式不对，正确格式是（yyyy-MM-dd HH:mm:ss [例如：2013-01-10 09:30:30]）");
 			return false;
		}
		return true;
	}
}
// 验证时间戳 ：MM/dd/yyyy HH:mm
function checkTimeStampByValue22(value1,message,textName){
	if(value1!=undefined && value1!=null){
 		if(value1.length==0||value1==0){
			alert(message);
 			return false;
		}else if(!/^([0]{1}[0-9]{1}||([1]{1}[0-2]{1}))[\/]{1}([0-2]{1}[0-9]{1}||[3]{1}[0-1]{1})[\/]{1}\d{4}[ ]{1}([0-1]{1}[0-9]{1}||[2]{1}[0-4]{1})[:]{1}[0-5]{1}[0-9]{1}$/.test(value1)){
			alert(textName+"值"+value1+" 格式不对，正确格式是（MM/dd/yyyy HH:mm [例如：04/09/2013 16:30]）");
 			return false;
		}
		return true;
	}
}
// 检查时间:HH:mm
function checkTimeByValue(value1,message,textName){
	if(value1!=undefined && value1!=null){
 		if(value1.length==0||value1==0){
			alert(message);
 			return false;
		}else if(!/^([0-1]{1}[0-9]{1}||[2]{1}[0-4]{1})[:]{1}[0-5]{1}[0-9]{1}$/.test(value1)){
			alert(textName+"值"+value1+" 格式不对，正确格式是（HH:mm [例如：09:30]）");
 			return false;
		}
		return true;
	}
}
// 检查日期:yyyy-MM-dd
function checkDateByValue(value1,message,textName){
	if(value1!=undefined && value1!=null){
 		if(value1.length==0||value1==0){
			alert(message);
 			return false;
		}else if(!/^\d{4}[-]{1}([0]{1}[0-9]{1}||([1]{1}[0-2]{1}))[-]{1}([0-2]{1}[0-9]{1}||[3]{1}[0-1]{1})$/.test(value1)){
			alert(textName+"值"+value1+" 格式不对，正确格式是（yyyy-MM-dd [例如：2013-01-10]）");
 			return false;
		}
		return true;
	}
}
// 得到当前时间:yyyy-MM-dd
function getCurrentDateStr(){
	var date=new Date();
 	var month=date.getMonth();
	if(month<9){
		month="0"+(month+1);
	}else{
		month=(month+1);
	}
	var day = date.getDate();
	if (day < 10) {
		day = "0" + day;
	}
 	return date.getFullYear()+"-"+month+"-"+day;
 }



// 得到当前时间:yyyy年MM月dd日
function getCurrentDateStrCN(){
	var date=new Date();
 	var month=date.getMonth();
	if(month<9){
		month="0"+(month+1);
	}else{
		month=(month+1);
	}
	var day = date.getDate();
	if (day < 10) {
		day = "0" + day;
	}
 	return date.getFullYear()+"月"+month+"月"+day+"日";
 }
// 得到日期时间的格式化:yyyy-MM-dd HH:mm
function getDateStr(millisec){
	if(millisec==""||millisec==0||!/^\d+$/.test(millisec)){
		return "";
	}
	var date=new Date();
	date.setTime(millisec);
 	var month=date.getMonth();
	if(month<9){
		month="0"+(month+1);
	}else{
		month=(month+1);
	}
	var day = date.getDate();
	if (day < 10) {
		day = "0" + day;
	}
	var hours = date.getHours();
	if (hours < 10) {
		hours = "0" + hours;
	}
	var minutes = date.getMinutes();
	if (minutes < 10) {
		minutes = "0" + minutes;
	}
	/*
	 * var seconds = date.getSeconds(); if (seconds < 10) { seconds = "0" +
	 * seconds; }
	 */
 	return date.getFullYear()+"-"+month+"-"+day+" "+hours+":"+minutes;
 }
// 得到日期时间的格式化:yyyy-MM-dd HH:mm:ss
function getyyyy_MM_dd_HH_mm_ss(millisec){
	if(millisec==""||millisec==0||!/^\d+$/.test(millisec)){
		return "";
	}
	var date=new Date();
	date.setTime(millisec);
 	var month=date.getMonth();
	if(month<9){
		month="0"+(month+1);
	}else{
		month=(month+1);
	}
	var day = date.getDate();
	if (day < 10) {
		day = "0" + day;
	}
	var hours = date.getHours();
	if (hours < 10) {
		hours = "0" + hours;
	}
	var minutes = date.getMinutes();
	if (minutes < 10) {
		minutes = "0" + minutes;
	}
	 var seconds = date.getSeconds();
	if (seconds < 10) {
		seconds = "0" + seconds;
	} 
 	return date.getFullYear()+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
 }
// 得到日期时间的格式化:yyyy-MM-dd HH:mm
function getDateFormateStr(date){
  	var month=date.getMonth();
	if(month<9){
		month="0"+(month+1);
	}else{
		month=(month+1);
	}
	var day = date.getDate();
	if (day < 10) {
		day = "0" + day;
	}
	var hours = date.getHours();
	if (hours < 10) {
		hours = "0" + hours;
	}
	var minutes = date.getMinutes();
	if (minutes < 10) {
		minutes = "0" + minutes;
	}
	/*
	 * var seconds = date.getSeconds(); if (seconds < 10) { seconds = "0" +
	 * seconds; }
	 */
 	return date.getFullYear()+"-"+month+"-"+day+" "+hours+":"+minutes;
 }
// 得到日期的格式化:yyyy-MM-dd
function getDateStr_yyyy_MM_dd(millisec){
	if(millisec==""||millisec==0){
		return "";
	}else if(!/^\d+$/.test(millisec)){
		return millisec;
	}
	var date=new Date();
	date.setTime(millisec);
 	var month=date.getMonth();
	if(month<9){
		month="0"+(month+1);
	}else{
		month=(month+1);
	}
	var day = date.getDate();
	if (day < 10) {
		day = "0" + day;
	}
	 
 	return date.getFullYear()+"-"+month+"-"+day ;
 }
// 为空或者未定义或者为NULL或者是字符串去空格
function toStr(value1){
	if(value1==undefined ||value1==null||value1=="null"||value1=="NULL"){
 		return "";
 	}else if(!isNaN(value1)){
 		return value1;
 	}else{
	   return value1.toString().trim();
 	}
}
/*
 * //数值保留2位小数 function getFormatDouble(value1){ if(value1==undefined
 * ||value1==null||value1=="null"||value1=="NULL"){ return ""; }else
 * if(!isNaN(value1)){ return value1; }else{ if(value1.length==0){ return
 * "0.00"; }else if(value1.length==1){ return value1+".00"; }else
 * if(value1.indexOf(".")==-1){ return value1+".00"; }else
 * if(value1.indexOf(".")!=-1){ indexben=value1.indexOf(".")+1;
 * indexlength=value1.length; if(indexlength-indexben>2){ var
 * temp=value1.substr(indexben+2,1); if(){
 *  } } return value1+".00"; } return value1.trim(); } }
 */
// 比较日期：格式yyyy-MM-dd
function compareDate(beginDateStr,endDateStr,message){
	var  a11=beginDateStr.split("-");
	var  a22=endDateStr.split("-");
	var date11=new Date();  date22=new Date();
	date11.setFullYear(a11[0]);
	date11.setMonth (a11[1]-1);
	date11.setDate(a11[2] );
	date22.setFullYear(a22[0]);
	date22.setMonth (a22[1]-1);
	date22.setDate(a22[2] );
	if(date11>date22){
		alert(message);
		return false;
	}
	return true;
}
// 比较日期：格式yyyy-MM-dd,begin<=end为true
function compareDateExt(beginDateStr,endDateStr){
	var  a11=beginDateStr.split("-");
	var  a22=endDateStr.split("-");
	var date11=new Date();  date22=new Date();
	date11.setFullYear(a11[0]);
	date11.setMonth (a11[1]-1);
	date11.setDate(a11[2] );
	date22.setFullYear(a22[0]);
	date22.setMonth (a22[1]-1);
	date22.setDate(a22[2] );
	if(date11>date22){
		return false;
	}
	return true;
}
// 比较时间：格式yyyy-MM-dd HH:mi
function compareTimeStamp(beginTimeStampStr,endTimeStampStr,message){
	var  a11=beginTimeStampStr.split("-");
	var  a22=endTimeStampStr.split("-");
	var date11=new Date();  date22=new Date();
	date11.setFullYear(a11[0]);
	date11.setMonth (a11[1]-1);
	date11.setDate(a11[2].substr(0,2) );
	date11.setHours(a11[2].substr(3,2), a11[2].substr(6,2), 0, 0) ;// Date(a11[2].substr(0,2)
																	// );
	date22.setFullYear(a22[0]);
	date22.setMonth (a22[1]-1);
	date22.setDate(a22[2].substr(0,2) );
	date22.setHours(a22[2].substr(3,2), a22[2].substr(6,2), 0, 0) ;// Date(a11[2].substr(0,2)
																	// );

	if(date11>date22){
		alert(message);
		return false;
	}
	return true;
}

// 比较时间：格式yyyy-MM-dd HH:mi:ss ,begin<=end返回true
function compareTimeStamp1(beginTimeStampStr,endTimeStampStr){
	var  a11=beginTimeStampStr.split("-");
	var  a22=endTimeStampStr.split("-");
	var date11=new Date();  date22=new Date();
	date11.setFullYear(a11[0]);
	date11.setMonth (a11[1]-1);
	date11.setDate(a11[2].substr(0,2) );
	date11.setHours(a11[2].substr(3,2), a11[2].substr(6,2), a11[2].substr(9,2), 0) ;// Date(a11[2].substr(0,2)
																					// );
	date22.setFullYear(a22[0]);
	date22.setMonth (a22[1]-1);
	date22.setDate(a22[2].substr(0,2) );
	date22.setHours(a22[2].substr(3,2), a22[2].substr(6,2), a22[2].substr(9,2), 0) ;// Date(a11[2].substr(0,2)
																					// );

	if(date11>date22){
		return false;
	}
	return true;
}

// 计算时间差天数
function getDays(startDate,endDate) {
	 if(startDate > endDate) {
		 return -1;
	 }
	var millisec = endDate.getTime() - startDate.getTime();
	var days = Math.floor(millisec/(24*3600*1000));
	return days;
}

// 检查IP
function checkIp(obj,flag){
		if(obj!=undefined && obj!=null){
			var value1=obj.val();
 			if(value1.length==0){
				if(flag){
					alert(message);
					obj.focus();
					return false;
				}else{
					return true;
				}
			}else if(!/^[1-2]{0,1}[0-9]{0,1}[0-9]{0,1}[.]{1}[1-2]{0,1}[0-9]{0,1}[0-9]{0,1}[.]{1}[1-2]{0,1}[0-9]{0,1}[0-9]{0,1}[.]{1}[1-2]{0,1}[0-9]{0,1}[0-9]{0,1}$/.test(value1)){
					alert("IP格式不对，正确格式是(255.255.255.1)");
					obj.focus();
					return false;
			}
 			return true;
		}else{
			alert("IP对象不存在！");
			return false;
		}
		
	}

function checkIpAddr(ip)
{
  var ss = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;	
  if(ss.test(ip)&&(RegExp.$1 < 256 && RegExp.$2<256 && RegExp.$3 < 256 && RegExp.$4 < 256))
	   return true;
  else
	   return false;

}

// 检查MAC地址
function checkMac(mac)
{
  var ss = /[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}/;	
  if(ss.test(mac))
	   return true;
  else
	   return false;

}

// 检查手机号
function cellPhone(obj,textName,isNullToFalse){
	var value1=obj.val();
	if(value1!=undefined && value1!=null){
 		if(value1==null||value1.length==0){
 			if(isNullToFalse){
 				alert(textName+"不能为空！");
 				return false;
 			}else{
 				return true;
 			}
 		}else if(!/^([130]{1}||[131]{1}||[133]{1}||[134]{1}||[135]{1}||[136]{1}||[137]{1}||[138]{1}||[139]{1}||[186]{1}||[187]{1}||[188]{1}||[189]{1}||[158]{1})\d{8}$/.test(value1)){
			alert(textName+"="+value1+" 格式不对，正确格式是（13388888888）");
 			return false;
		}
		return true;
	}else{
		alert("对象未定义");
		return false;
	}
} 

/**
 * 判断字符串Text是否带得有特殊字符
 * @param text
 * @param specialCharacter 可为空：默认特殊字符：[~'!@#$%^&*()-+_=:]
 * @returns
 */
function existCharacter(text, specialCharacter) {
	var exist = false;
	if (toStr(specialCharacter) == "") {
		specialCharacter = "[~'!@#$%^&*+=]";
	}
	var pattern = new RegExp(specialCharacter);
    if(toStr(text) != ""){
        if(pattern.test(text)){
        	exist = true;
        }
    }
    return exist;
}
/**
 * 设置结束时间必须晚于开始时间
 *  @param id
 *  add by  ztt
 * */
function dataControl(startDate,endDate){
	 $('#'+startDate+'').datetimepicker().on('changeDate', function(e) {
  	   var endTimeStart = $('#'+startDate+'').val();
  	   $('#'+endDate+'').datetimepicker('setStartDate', endTimeStart);
	 });
	 $('#'+endDate+'').datetimepicker().on('changeDate', function(e) {
	   var startTimeStart = $('#'+endDate+'').val();
	   $('#'+startDate+'').datetimepicker('setEndDate', startTimeStart );
	 });
} 

/**
 * 日期转换
 * add by ztt
 * */
function datFmt(date,fmt) { 
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
};
