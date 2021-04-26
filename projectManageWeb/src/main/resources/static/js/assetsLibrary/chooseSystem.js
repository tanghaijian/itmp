/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 */
var selectSysIds = [];
$(document).ready(function () {
    getSystem();
    downOrUpButton();
    $("#systemSearch").on("click",function () {
        getSystem();
    })
})

//获取系统信息 ， 形成表格显示
function getSystem(){  
	$("#systemTable").bootstrapTable('destroy');
    $("#systemTable").bootstrapTable({  
    	url:"/devManage/systeminfo/selectAllSystemInfo2",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
        //singleSelect : true,//单选
        queryParams : function(params) {
             var param={ 
            	 systemName:$.trim($("#systemName").val()),
            	 systemCode:$.trim($("#systemCode").val()),
                 pageNumber:params.pageNumber,
                 pageSize:params.pageSize, 
             }
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px"
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "systemCode",
            title : "子系统编码",
            align : 'center'
        },{
            field : "systemName",
            title : "子系统名称",
            align : 'center'
        },{
        	field : "systemShortName",
        	title : "子系统简称",
        	align : 'center'
        },{
            field : "projectName",
            title : "所属项目",
            align : 'center'
        }],
        onLoadSuccess:function(){
        	 
        },
        onLoadError : function() {

        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectSysIds .indexOf(rows[i])<=-1){
        		selectSysIds.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectSysIds.indexOf(rows[i])>-1){
        			selectSysIds.splice(selectSysIds.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectSysIds.indexOf(row)<=-1){
        	 selectSysIds.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectSysIds.indexOf(row)>-1){
        		selectSysIds.splice(selectSysIds.indexOf(row),1);
        	}
        }
    });
}
//清空搜索
function clearSearchSys(){
    $("#systemName").val("");
    $("#systemCode").val("");
}
//获取系统id 返回给主页面
function getSystemid(){
	var flag;
	if( selectSysIds.length > 0 ){
		flag = true;
	}else{
		flag = false;
		layer.alert('请选择一列数据！', {icon: 0});
	}
	var data = {
			data : selectSysIds,
			status : flag
	}
	return data;
}
// 搜索展开和收起
function downOrUpButton() {
    $("#downBtn").on("click",function () {
        if ($(this).children("span").hasClass("fa-caret-up")) {
            $(this).children("span").removeClass("fa-caret-up");
            $(this).children("span").addClass("fa-sort-desc");
            $(this).prev().slideUp(200);
        } else {
            $(this).children("span").removeClass("fa-sort-desc");
            $(this).children("span").addClass("fa-caret-up");
            $(this).prev().slideDown(200);
        }
    });
}