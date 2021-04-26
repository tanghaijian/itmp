var curDate = (new Date()).getTime();
$(function(){
    //所有的Input标签，在输入值后出现清空的按钮
    $('input[type="text"]').parent().css("position", "relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange", function () {
        if ($(this).val() != "") {
            $(this).parent().children(".btn_clear").css("display", "block");
        } else {
            $(this).parent().children(".btn_clear").css("display", "none");
        }
    });


    laydate.render({
        elem: '#startDate'
        ,type: 'month'
        ,max: curDate
    });

    queryData()

});
function clearSearch() {
    $("#startDate").val('');
}

function queryData(){
    var time = $("#startDate").val()||'2020-10';
    if(time == ""){
        layer.alert("时间为空",{icon:0});
        return;
    }
    $("#loading").css('display', 'block');
    $.ajax({
        url: "/testManage/testReport/queryReport",
        method: "post",
        data:{time:time},
        dataType: "json",
        success: function (_data) {
            $("#loading").css('display', 'none');
            var data=[
                {
                    yearMonth:'2020年10月',
                    reportName:'软件质量分析月报',
                    id:1
                },
                {
                    yearMonth:'2020年9月',
                    reportName:'软件质量分析月报',
                    id:2
                },
                {
                    yearMonth:'2020年8月',
                    reportName:'软件质量分析月报',
                    id:3
                },
                {
                    yearMonth:'2020年7月',
                    reportName:'软件质量分析月报',
                    id:4
                }
            ]
            createMonthReportListTable(data);

        }
    });
}

function createMonthReportListTable(data){
    $("#monthReportListTable").bootstrapTable('destroy');
    $("#monthReportListTable").bootstrapTable({
        queryParamsType:"",
        data:data,
        columns : [
            {
                field : "index",
                title : "序号",
                align : 'center',
                class : "stepOrder",
                width : '30px',
                formatter : function (value, row, index) {
                    return index+1;
                }
            }
            ,{
                field : "yearMonth",
                title : "时间",
                align : 'center',
                class : "stepOrder",
                width : '80px',
                formatter : function (value, row, index) {
                    return value;
                }
            },{
                field : "reportName",
                title : "月报名称",
                align : 'center',
                class : "stepOrder",
                width : '150px',
                formatter : function (value, row, index) {
                    return '<span class="to_month_report" onclick="toMonthReportDetails('+row.id+')" data-id="'+row.id+'">'+row.yearMonth+value+'</span>';
                }
            }],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
}
function toMonthReportDetails(monthId) {
    console.log(monthId);
    window.open('/testManageui/testMonthReport/toTestMonthReport?monthId='+monthId)
}
