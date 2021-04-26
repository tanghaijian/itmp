//公告详情
function getNoticeDetail(noticeId){
	$("#loading").css('display','block');
	$.ajax({
		type:"post", 
		url:"/devManage/notice/selectNoticeById",
		dataType:"json",
		data:{
			"id":noticeId
		},
		success : function(data) {
			 $("#loading").css('display','none');
			 var validDateStr = "";
			 var startTime = data.data.startTime;
			 if(toStr(startTime) != ""){
				 validDateStr += datFmt(new Date(startTime),"yyyy-MM-dd");
				 validDateStr += " - ";
			 }
			 var endTime = data.data.endTime;
			 if(toStr(endTime) != ""){
				 validDateStr += datFmt(new Date(endTime),"yyyy-MM-dd");
			 }
			 
			 $("#noticeContent").append(data.data.noticeContent);
			 //公告类型，1系统公告，2项目公告
			 if (data.data.noticeType == 2) {
				 $("#projectName").closest(".rowdiv").show();
				 $("#projectName").append(data.data.projectNames);
			 } else {
				 $("#projectName").closest(".rowdiv").hide();
			 }
			 $("#validDateStr").append(validDateStr);
		}
	});  
}
//字符串去空处理
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
