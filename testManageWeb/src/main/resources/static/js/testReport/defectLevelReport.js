var myChart1 = "";
var option1 = {};

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
	initTime();
	
	laydate.render({ 
		  elem: '#searchDate'
		  ,range: '~'
		});

    
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
});
/*时间框初始值*/
function initTime(){
	var dateArr = getCurrentDateStr().split('-');
	var preMonth = parseInt(dateArr[1])-1;
	preMonth = preMonth < 1?12:preMonth;
	var iniDate = dateArr[0] +'-'+ (preMonth<10?"0":"") + preMonth +'-26 ~ '+ dateArr[0] +'-'+ dateArr[1] + '-25';
	$("#searchDate").val(iniDate);
}

function queryData(){
	var time = $("#searchDate").val();
	var systemIds = $("#systemId").selectpicker('val');
	if(time == ""){
		layer.alert("时间为空",{icon:0});
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/getDefectLevelReport",
		method: "post",
		data:{time:time},
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

function clearSearch(){
	$("#searchDate").val('');
	$("#systemId").selectpicker('val','');
}