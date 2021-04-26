/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 * 资产库（需求视角），查询：开发任务弹出页面js
 */ 
var selectTaskIds  = [];
$(document).ready(function(){
	taskPopup();
})  

//页面数据列表展示
function taskPopup(){
	var featureStatusList=$("#TaskPType").find("option");
	$("#loading").css('display','block');
    $("#TaskTable").bootstrapTable('destroy');
    $("#TaskTable").bootstrapTable({  
    	url:"/devManage/devtask/getAllFeature",
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
            	featureName:$.trim($("#TaskPName").val()),
            	featureCode:$.trim($("#TaskPCode").val()),
            	requirementFeatureStatus:$.trim($("#TaskPType").val()),
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
            field : "FEATURE_code",
            title : "任务编码",
            align : 'center'
        },{
            field : "FEATURE_NAME",
            title : "任务名称",
            align : 'center'
        },{
        	field : "REQUIREMENT_FEATURE_STATUS",
        	title : "任务状态",
        	align : 'center',
        	formatter : function(value,rows, index) {
        		for (var i = 0,len = featureStatusList.length;i < len;i++) {
        			if(rows.REQUIREMENT_FEATURE_STATUS == featureStatusList[i].value){
        				var _status = "<input type='hidden' id='list_featureStatusList' value='"+featureStatusList[i].innerHTML+"'>";
                        return featureStatusList[i].innerHTML+_status
                    }
                }
            }
        }],
        onLoadSuccess:function(){
        	   $("#loading").css('display','none');
        },
        onLoadError : function() {

        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectUserIds.indexOf(rows[i])<=-1){
        		selectTaskIds.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {//全不选
        	for(var i=0;i<rows.length;i++){
        		if(selectTaskIds.indexOf(rows[i])>-1){
        			selectTaskIds.splice(selectTaskIds.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectUserIds.indexOf(row)<=-1){
        	selectTaskIds.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectTaskIds.indexOf(row)>-1){
        		selectTaskIds.splice(selectTaskIds.indexOf(row),1);
        	}
        }
    }); 
}
//重置
function clearSearch() {	
    $('#TaskPName').val("");
    $('#TaskPCode').val("");
    $("#TaskPType").selectpicker('val', ''); 
} 

//弹窗选择数据后返回该数据，由父页面调用该方法
function getDevTaskId(){
	var flag;
	if( selectTaskIds.length > 0 ){
		flag = true;
	}else{
		flag = false;
		layer.alert('请选择一列数据！', {icon: 0});
	}
	var data = {
			data : selectTaskIds,
			status : flag
	}
	return data;
} 