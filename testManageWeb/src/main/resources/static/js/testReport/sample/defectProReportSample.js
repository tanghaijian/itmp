var myChart1 = "";
var option1 = {};

$(function(){
	myChart1 = echarts.init(document.getElementById('main'));
	queryData();
	option1 = {
			animation:false,
		    title: {
		        left: 'center',
		        text: timeRange+'缺陷率统计(按系统)',
		    },
		    tooltip: {formatter:'{b}:{c}%'},
		    grid:{
		    	bottom:10,
		    	containLabel:true
		    },
		    xAxis: {
		        type: 'category',
		        data: [],
		        axisLabel:{
		        	rotate:40,
		        	interval: 0,
		        	fontSize:11
		        }
		    },
		    yAxis: {
		    	name: '缺陷率(%)',
		        type: 'value'
		    },
		    series: [{
		        data: [],
		        type: 'bar'
		    }]
		};
});

function queryData(){
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/getDefectProData",
		method: "post",
		data:{time:timeRange,systemIdStr:systemIds == "$systemIds"?"":systemIds},
		dataType: "json",
		success: function (data) {
			$("#loading").css('display', 'none');
			$(".tableTitle").css('display','block');
			createDevSystemTable(data.defectOamSystemList,'oamDefectTable');
			createDevSystemTable(data.defectNewSystemList,'newDefectTable');
			option1.xAxis.data = data.defectProMonthMap.systemName;
			option1.series[0].data = data.defectProMonthMap.defectPro;
			// 使用刚指定的配置项和数据显示图表。
		    myChart1.setOption(option1);
			$("#saveHis").removeClass('hideBlock');
		}
	});
}

function createDevSystemTable(data,tableId){
	$("#"+tableId).bootstrapTable('destroy');
    $("#"+tableId).bootstrapTable({
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
            field : "systemName",
            title : "系统名称",
            align : 'center',
            class : "stepOrder",
            width : '150px',
        },{
            field : "taskNumber",
            title : "测试任务数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "defectNumber",
            title : "缺陷数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "repairedDefectNumber",
            title : "修复缺陷数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "unrepairedDefectNumber",
            title : "遗留缺陷数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "designCaseNumber",
            title : "设计用例数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "defectPercent",
            title : "缺陷率",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
                return '<span>'+parseFloat(value).toFixed(2)+'%'+'</span>';
            }
        },{
            field : "totalRepairRound",
            title : "累计修复轮次",
            align : 'center',
            class : "stepOrder",
            width : '30px',
        },{
            field : "avgRepairRound",
            title : "平均修复轮次",
            align : 'center',
            class : "stepOrder",
            width : '30px',
            formatter : function (value, row, index) {
                return '<span>'+parseFloat(value).toFixed(2)+'</span>';
            }
        }],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
}