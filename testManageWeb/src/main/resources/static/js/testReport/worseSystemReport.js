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
		        text: '质量较差系统缺陷率走势',
		    },
		    tooltip: {
		    	trigger: 'axis'
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
		    series: []
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
		url: "/testManage/testReport/getWorseSystemReport",
		method: "post",
		data:{time:time},
		dataType: "json",
		success: function (data) {
			$("#loading").css('display', 'none');
			$(".tableTitle").css('display','block');
			var seriesArr = [];
			$.each(data.tables,function(index,value){
				createWorseSystemTable(value,index);
				var json = {
						name: value[0].systemName,
			            type: 'line',
			            data: data.defectPros[index]
				};
				seriesArr.push(json);
			});
			option1.series = seriesArr;
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

function createWorseSystemTable(data,index){
	$("#worseSystemTable"+index).bootstrapTable('destroy');
    $("#worseSystemTable"+index).bootstrapTable({
        queryParamsType:"",
        data:data,
        columns : [
        {
                field : "systemName",
                title : "系统名称",
                align : 'center',
                class : "stepOrder",
                width : '50px',
        },
        {
            field : "1",
            title : "1月",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "2",
            title : "2月",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "3",
            title : "3月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "4",
            title : "4月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "5",
            title : "5月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "6",
            title : "6月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "7",
            title : "7月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "8",
            title : "8月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "9",
            title : "9月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "10",
            title : "10月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "11",
            title : "11月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "12",
            title : "12月",
            align : 'center',
            class : "stepOrder",
            width : '50px'
        },{
            field : "defectTotalPro",
            title : "总缺陷率",
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