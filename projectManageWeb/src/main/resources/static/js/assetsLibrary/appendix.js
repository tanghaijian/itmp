/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 */
$(document).ready(function () {
    getHistory();
})

//获取系统信息 ， 形成表格显示
function getHistory(){
    $("#appendixTable").bootstrapTable({
        /*url:"/devManage/systeminfo/selectAllSystemInfo",*/
        method:"post",
        queryParamsType:"",
        pagination : false,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        singleSelect : true,//单选
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
            field : "projectName",
            title : "所属项目",
            align : 'center'
        }],
        onLoadSuccess:function(){

        },
        onLoadError : function() {

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
    var id = 10;
    return id;
}