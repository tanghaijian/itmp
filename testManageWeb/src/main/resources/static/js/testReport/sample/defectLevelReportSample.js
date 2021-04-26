var myChart1 = "";
var option1 = {};

$(function(){
	myChart1 = echarts.init(document.getElementById('main'));
	
	option1 = {
			animation:false,
		    title: {
		        left: 'center',
		        text: '缺陷等级分布',
		    },
		    tooltip: {
		        trigger: 'item',
		    },
		    legend: {
		        type: 'scroll',
		        orient: 'vertical',
		        right: 10,
		        top: 20,
		        bottom: 20,
		        data: []
		    },
		    series: [{
	            name: '缺陷等级',
	            type: 'pie',
	            radius: '55%',
	            center: ['40%', '50%'],
	            data: [],
	            emphasis: {
	                itemStyle: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }]
		};
	queryData();
});

function queryData(){
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/getDefectLevelReport",
		method: "post",
		data:{time:time == "$time"?"":time},
		dataType: "json",
		success: function (data) {
			$("#loading").css('display', 'none');
			$(".tableTitle").css('display','block');
			createDefectLevelTable(data.defectLevelList);
			option1.legend.data = data.defectLevelChartsMap.level;
			option1.series[0].data = data.defectLevelChartsMap.defectCount;
			// 使用刚指定的配置项和数据显示图表。
		    myChart1.setOption(option1);
			$("#copyWordCode").removeClass('hideBlock');
			$("#copyWordCode").attr('data-clipboard-text',data.url);
			var clipboard = new ClipboardJS("#copyWordCode");
			console.log( clipboard )
		    clipboard.on('success', function(e) { 
		        layer.msg("<span style='color:white'>复制成功</span>", {
		            area:["150px","48px"],
		            time:2000
		        });
		    });
		}
	});
}

function createDefectLevelTable(data){
	$("#defectLevelTable").bootstrapTable('destroy');
    $("#defectLevelTable").bootstrapTable({
        queryParamsType:"",
        data:data,
        columns : [
        {
        	field : "severityLevelName",
            title : "缺陷等级",
            align : 'center',
            class : "stepOrder",
            width : '30px',
        }
        ,{
            field : "defectCount",
            title : "个数",
            align : 'center',
            class : "stepOrder",
            width : '150px',
        }],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
}
