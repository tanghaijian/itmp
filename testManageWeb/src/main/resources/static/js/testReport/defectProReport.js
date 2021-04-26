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
	showSystems();
	
	laydate.render({ 
		  elem: '#searchDate'
		  ,range: '~'
		});

    
	myChart1 = echarts.init(document.getElementById('main'));
	
	option1 = {
			animation:false,
		    title: {
		        left: 'center',
		        text: '本月缺陷率统计(按系统)',
		    },
		    tooltip: {
		    	formatter:'{b}:{c}%'
		    },
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
		        type: 'bar',
		        itemStyle:{
		            normal:{
		                color:'rgb(1,93,142)'
		            }
		        },
		    }]
		};
});
/*获取系统名称*/
function showSystems() {
	$.ajax({
		url: "/testManage/testReport/getAllSystem",
		method: "post",
		dataType: "json",
		success: function (data) {
			var list = data.systemList;
			system_arr = data.data;
			for (var i = 0; i < list.length; i++) {
				//先创建好select里面的option元素       
				var option = document.createElement("option");
				//转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				$(option).val(list[i].systemCode);
				//给option的text赋值,这就是你点开下拉框能够看到的东西          
				$(option).text(list[i].systemName);
				//获取select 下拉框对象,并将option添加进select            
				$('#systemId').append(option);
			}
			$('.selectpicker').selectpicker('refresh');
		}
	});
}
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
		url: "/testManage/testReport/getDefectProData",
		method: "post",
		data:{time:time,systemIdStr:systemIds == null?"":systemIds.join(',')},
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
            formatter : function (value, row, index) {
            	if(value != 0){
            		return '<a href="#" onclick="toTestTask(\''+row.systemName+'\')">'+value+'</a>';
            	}else{
            		return 0;
            	}
            }	
        },{
            field : "defectNumber",
            title : "缺陷数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
            	if(value != 0){
            		return '<a href="#" onclick="toDefect(\''+row.systemName+'\')">'+value+'</a>';
            	}else{
            		return 0;
            	}
            }	
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
/*跳转测试任务*/
function toTestTask(systemName){ 
	var date = $("#searchDate").val();
	var dateArr = date.split(' ~ ');
	var dateStr = ">="+dateArr[0].replace(new RegExp('-',"g" ),"")+" and <="+dateArr[1].replace(new RegExp('-',"g" ),"");
	var url = testTaskMenuUrl+"?systemName="+systemName+"&windowDate="+dateStr;
	window.parent.toPageAndValue( testTaskMenuName , testTaskMenuId , url );
}
/*跳转缺陷*/
function toDefect(systemName){
	var date = $("#searchDate").val();
	var dateArr = date.split(' ~ ');
	var dateStr = ">="+dateArr[0].replace(new RegExp('-',"g" ),"")+" and <="+dateArr[1].replace(new RegExp('-',"g" ),"");
	var url = defectUrl+"&systemName="+systemName+"&windowDate="+dateStr;
	window.parent.toPageAndValue( defectName , detectId , url );
}

function clearSearch(){
	$("#searchDate").val('');
	$("#systemId").selectpicker('val','');
}