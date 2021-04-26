var myChart1 = "";
var option1 = {};
$(function(){
	// 所有的Input标签，在输入值后出现清空的按钮
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
		  elem: '#searchDate'
			  ,type: 'year'
		});
	
	myChart1 = echarts.init(document.getElementById('main'));
	
	option1 = {
			animation:false,
		    title: {
		        left: 'center',
		        text: '缺陷率整体趋势',
		    },
		    tooltip: {
		    	trigger: 'axis',
		    	formatter:'{b}:{c}%'
		    },
		    grid:{
		    	bottom:10,
		    	containLabel:true
		    },
		    xAxis: {
		        type: 'category',
		        data: ["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
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
		        type: 'line'
		    }]
		};
});

function queryData(){
	var time = $("#searchDate").val();
	if(time == ""){
		layer.alert("时间为空",{icon:0});
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/getDefectTotalReport",
		method: "post",
		data:{time:time},
		dataType: "json",
		success: function (data) {
			$("#loading").css('display', 'none');
			$(".tableTitle").css('display','block');
			createDefectTotalTable(data.defectTotalList);
			option1.series[0].data = data.defectTotalChartsList;
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

function createDefectTotalTable(data){
	$("#defectTotalTable").bootstrapTable('destroy');
    $("#defectTotalTable").bootstrapTable({
        queryParamsType:"",
        data:data,
        columns : [
        {
                field : "time",
                title : "时间",
                align : 'center',
                class : "stepOrder",
                width : '50px',
        },
        {
            field : "taskCount",
            title : "测试任务数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "defectCount",
            title : "缺陷数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "fixedDefectCount",
            title : "修复缺陷数",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "caseCount",
            title : "设计用例数",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "defectPro",
            title : "缺陷率",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "repairRound",
            title : "累计修复轮次",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "avgRepair",
            title : "平均累计修复轮次",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        }],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
}