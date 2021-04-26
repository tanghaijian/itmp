/**
 * Created by 朱颜辞镜花辞树 on 2018/9/18.
 */
$(function(){
    pageInit();  
// 搜索框 收藏按钮 js 部分
    $(".collection").click(function () {
        if( $(this).children("span").hasClass("fa-heart-o") ){
            $(this).children("span").addClass("fa-heart");
            $(this).children("span").removeClass("fa-heart-o");
        }else {
            $(this).children("span").addClass("fa-heart-o");
            $(this).children("span").removeClass("fa-heart");
        }
    })
    //所有的Input标签，在输入值后出现清空的按钮
    $('input').parent().css("position","relative");
    $('input').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $("input").bind("input propertychange",function(){
        if( $(this).val()!="" ){
            $(this).parent().children(".btn_clear").css("display","block");
        }else {
            $(this).parent().children(".btn_clear").css("display","none");
        }
    })
});
//表格数据加载
function pageInit(){
	$("#list2").jqGrid("clearGridData");
    $("#list2").jqGrid({ 
    	 url:'/devManageui/systeminfo/getAllSystemInfo', 
         datatype: "json", 
         height: 'auto',  
         mtype : "post",
         multiselect: true, 
         width: $(".content-table").width()*0.999,
        colNames:['id','系统编号', '系统名称','所属项目','开发中任务数','上次构建时间','操作'],
        colModel:[
            {name:'id',index:'id',hidden:true,sorttype:'integer'},
            {name:'key_id',index:'key_id',hidden:true,key:true},
            {name:'系统编号',index:'系统编号', sorttype:'string'},
            {name:'系统名称',index:'系统名称'},
            {name:'所属项目',index:'所属项目'},
            {name:'开发中任务数',index:'开发中任务数'}, 
            {name:'上次构建时间',index:'上次构建时间'},
            {
                name:'操作',
                index:'操作',
                align:"center",
                fixed:true,
                sortable:false,
                resize:false,
                search: false,
                formatter : function(value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                    return '<a class="a_style" onclick="edit('+ row + ')">编辑</a>'
                }
            }
        ],
        rowNum:16,
        rowTotal: 200,
        rowList : [10,20,30],
        loadonce:true, //如果为ture则数据只从服务器端抓取一次，之后所有操作都是在客户端执行，翻页功能会被禁用
        rownumbers: true,
        rownumWidth: 40,
        pager: '#pager2',
        sortable:true,   //是否可排序
        sortorder: 'asc',
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true, //是否要显示总记录数 
    });  
} 
//清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}  
  