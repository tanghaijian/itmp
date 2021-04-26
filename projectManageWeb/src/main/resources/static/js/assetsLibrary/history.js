/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 * 资产库（需求视角）：历史版本js
 */
var parameterArr = {};
parameterArr.arr = window.location.href.split("?");
parameterArr.parameterArr = parameterArr.arr[1].split(",");
parameterArr.obj = {};
for (var i = 0; i < parameterArr.parameterArr.length; i++) {
    var obj = parameterArr.parameterArr[i].split("=");
    parameterArr.obj[obj[0]] = obj[1];
}
$(document).ready(function () { 
    getHistory();
})
var mydata = {
	exportUrl : ''
}
//获取系统信息 ， 形成表格显示
function getHistory(){
	$.ajax({
        url:"/projectManage/systemPerspective/getDocumentHistory",
        type:"post",
        data:{ 
        	documentId : parameterArr.obj.id
        },
        success:function(data){
        	console.log( data )
        	getTable( data ); 
        },
        error:function(data){
        	 console.log( data );
        }
    }) 
    return ; 
}

//组装文档历史列表展示
function getTable( data ){
	$("#historyTable").bootstrapTable({ 
		data:data.data,
        method:"post",
        queryParamsType:"",
        pagination : false,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8', 
        columns : [{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "documentVersion",
            title : "版本号",
            align : 'left',
            formatter : function(value, row, index) {
            	return "V"+ value + ".0";
            }
        },{
            field : "updateUserName",
            title : "修改人",
            align : 'left'
        },{
            field : "lastUpdateDate",
            title : "更新时间",
            align : 'left'
        },{
            field : "relatedRequirement",
            title : "关联需求变更单",
            align : 'left',
            formatter : function(value, row, index) {
            	if( value == null ){
            		return "";
            	}else{
            		return isValueNull( value.requirementCode ) + " " + isValueNull( value.requirementName );
            	} 
            }
        },{
            field : "opt",
            title : "操作",
            align : 'center',
            formatter : function(value, row, index) {
                // return '<a class="a_style" onclick="downLoadFile('+ rows +')" >下载</a>';
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                var str = '';
                
                //普通附件存储形式，直接下载
                if (row.saveType == 1) {
                    var url = "/projectManage/assetsLibraryRq/downObject?documentS3Bucket=" + encodeURIComponent(row.documentS3Bucket) 
                    + "&documentS3Key=" + encodeURIComponent(row.documentS3Key) + "&documentName=" + encodeURIComponent(row.documentName);
					str += '<a class="a_style readAuth_btn" href="' + url + '" download="' + row.documentName + '">下载</a>';
                }else if(row.saveType == 2){  //markdown形式存储
                    str += '<a class="a_style readAuth_btn" onclick="exportFile(' + rows + ')">导出</a>';
				}
                return str;
            }
        }],
        onLoadSuccess:function(){

        },
        onLoadError : function() {

        }
    });
} 

//导出
function exportFile(row){ 
	$("#export_Modal").modal("show");
	mydata.exportUrl = "/projectManage/documentChapters/exportByDocumentIdAndVersion?systemDirectoryDocumentId="+row.systemDirectoryDocumentId+"&version="+row.documentVersion; 
}

//导出提交
function exportCommit(){
	if( $('#ulFileExport input[name="file"]:checked').val()==undefined ) {
		layer.alert("请选择导出类型!", {
            icon: 0,
            title: "提示信息"
        });
	}else{
		mydata.exportUrl += "&type="+ $('#ulFileExport input[name="file"]:checked').val();
	}
	window.location.href = mydata.exportUrl;
	$("#export_Modal").modal("hide");
}

/* function downLoadFile( rows ){
	if (parameterArr.obj.type == 1) {
        $.ajax({
	        url:"/zuul/system/file/downloadFile",
	        type:"post",
	        data:{ 
	        	fileS3Bucket: rows.documentS3Bucket,
	        	fileS3Key: rows.documentS3Key,
	        	FileNameOld: rows.documentName,
	        },
	        success:function(data){
	        	 layer.alert("下载成功",{icon : 1});
	        },
	        error:function(data){ 
	        	 layer.alert("下载失败",{icon : 2});
	        }
	    }) 
    } else if (parameterArr.obj.type == 2) { 
	   $("#export_Modal").modal("show");
	   mydata.exportUrl = "/projectManage/documentChapters/exportByDocumentIdAndVersion?systemDirectoryDocumentId="+ rows.systemDirectoryDocumentId +"&version="+rows.documentVersion;
	} 
} */

/* function exportCommit(){
	if( $('#ulFileExport input[name="file"]:checked').val()==undefined ) {
		layer.alert("请选择!", {
            icon: 0,
            title: "提示信息"
        });
	}else{
		mydata.exportUrl += "&type="+ $('#ulFileExport input[name="file"]:checked').val();
	}
	window.location.href = mydata.exportUrl;
	$("#export_Modal").modal("hide");
} */



