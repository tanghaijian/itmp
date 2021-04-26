/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 * 资产库(需求视角)，查询：关联需求跳转页面
 */ 
var selectReqIds = [];
$(document).ready(function(){
	getReqStatus();
	reqTableShowAll();
	$("#reqSearch").on("click",function(){
		reqTableShowAll();
	})
}) 
//需求表格
function reqTableShowAll(){
	$("#listReq").bootstrapTable('destroy');
    $("#listReq").bootstrapTable({  
    	url:"/projectManage/requirement/getAllRequirement2",
    	method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
       // singleSelect : true,//单选
        queryParams : function(params) {
             var param={ 
            	 requirementCode : $.trim($("#reqCode").val()),
            	 requirementName  :$.trim( $("#reqName").val()),
            	 requirementStatus : $("#reqStatus").val(),
        		 pageNumber:params.pageNumber,
        		 pageSize:params.pageSize, 
             }
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
            formatter:function(value, row, index) {
            	if(reqStatus == 'cancel'){
            		if (row.reqStatusName == "已取消"){
                        return {
                            disabled : true,//设置是否可用
                            checked : false//设置选中
                        	};
                        	return value;
                    }
            	}
                
             }
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "REQUIREMENT_CODE",
            title : "需求编号",
            align : 'center'
        },{
            field : "REQUIREMENT_NAME",
            title : "需求名称",
            align : 'center'
        },{
        	field : "reqStatusName",
        	title : "需求状态",
        	align : 'center'
        },{
            field : "reqSourceName",
            title : "需求来源",
            align : 'center'
        },{

            field : "reqTypeName",
            title : "需求类型",
            align : 'center'
        }],
        onLoadSuccess:function(){
        	 
        },
        onLoadError : function() {

        },
        onCheckAll:function(rows){//全选
        	for(var i=0;i<rows.length;i++){
        		//if(selectReqIds .indexOf(rows[i])<=-1){
        		selectReqIds.push(rows[i]);
        		//}
        	}
        },
        onUncheckAll: function (rows) {
        	for(var i=0;i<rows.length;i++){
        		if(selectReqIds.indexOf(rows[i])>-1){
        			selectReqIds.splice(selectReqIds.indexOf(rows[i]),1);
        		}
        	}
        },
        onCheck:function(row){//选中复选框
        	//if(selectReqIds.indexOf(row)<=-1){
        	  selectReqIds.push( row );
        	//}
        },
        onUncheck:function(row){//取消复选框
        	if(selectReqIds.indexOf(row)>-1){
        		selectReqIds.splice(selectReqIds.indexOf(row),1);
        	}
        }
    });
}

//获取需求状态，填充需求状态下拉框查询条件
function getReqStatus(){
	 $.ajax({
	  type:"POST",
      url:"/projectManage/requirement/getDataDicList",
      dataType:"json",
      data:{
      	datadictype:'reqStatus'        	
  	},
  	 success:function(data){
  		$("#reqStatus").empty();
  		$("#reqStatus").append("<option value=''>请选择</option>")
  		for(var i=0;i<data.length;i++){
            var opt = "<option value='" + data[i].valueCode + "'>" + data[i].valueName + "</option>";
            $("#reqStatus").append(opt);
            }
		$('.selectpicker').selectpicker('refresh'); 
  	 }
	 });
} 
//重置
function clearSearchReq() {	
    $('#reqCode').val("");
    $('#reqName').val("");
    $("#reqStatus").selectpicker('val', '');
    $(".color1 .btn_clear").css("display","none");
} 

//选择关联的需求后，返回选择的数据
function getAssociatedDemandId(){
	var flag;
	if( selectReqIds.length > 0 ){
		flag = true;
	}else{
		flag = false;
		layer.alert('请选择一列数据！', {icon: 0});
	}
	var data = {
			data : selectReqIds,
			status : flag
	}
	return data;
}