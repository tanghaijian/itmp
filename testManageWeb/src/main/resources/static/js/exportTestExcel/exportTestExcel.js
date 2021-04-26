var curDate = (new Date()).getTime();
var myChart1 = "";
var myChart2 = "";
var myChart3 = "";
var myChart4 = "";
var myChart5 = "";
var myChart6 = "";
var option1 = {};
var option2 = {};
var option3 = {};
var reg = /^([0]|[1-9][0-9]*)$/
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

	showSystems();
	
	laydate.render({ 
		  elem: '#startDate'
		  ,type: 'month'
		  ,max: curDate
		});
	
	
	myChart1 = echarts.init(document.getElementById('main'));
	myChart2 = echarts.init(document.getElementById('main2'));
	myChart3 = echarts.init(document.getElementById('main3'));
	myChart4 = echarts.init(document.getElementById('main4'));
	myChart5 = echarts.init(document.getElementById('main5'));
	myChart6 = echarts.init(document.getElementById('main6'));

    // 指定图表的配置项和数据
	option1 = {
			animation:false,
		    title: {
		        left: 'center',
		        text: '缺陷率整体趋势图(按月)',
		    },
		    xAxis: {
		        type: 'category',
		        data: [],
		        axisLabel:{
		        	rotate:40
		        }
		    },
		    yAxis: {
		    	name: '缺陷率(%)',
		        type: 'value'
		    },
		    series: [{
		        label: {
		                normal: {
		                    show: true,
		                    position: 'top'
		                }
		            },
		        data: [],
		        type: 'line'
		    }]
		};
	
	option2 = {
			animation:false,
		    title: {
		        left: 'center',
		        text: '本月缺陷率统计(按项目)',
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
		        type: 'bar'
		    }]
		};
	
	option3 = {
			animation:false,
		    title : {
		        text: '缺陷等级分布情况',
		        x:'center'
		    },
		    legend: {
		        type: 'scroll',
		        orient: 'vertical',
		        right: 10,
		        top: 20,
		        bottom: 20,
		        data: ["a","b","c"],

		    },
		    series : [
		        {
		            name: '姓名',
		            type: 'pie',
		            radius : '55%',
		            center: ['40%', '50%'],
		            data: [],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            }
		        }
		    ]
		};

});
function clearSearch(){
	$("#startDate").val('');
	$("#endDate").val('');
	$("#systemId").selectpicker('val','');
}

function queryHisByTime(){
	var time = $("#startDate").val();
	if(time == ""){
		layer.alert("时间为空",{icon:0});
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/queryHisByTime",
		method: "post",
		data:{time:time},
		dataType: "json",
		success: function (data) {
			$("#loading").css('display', 'none');
			createDevVersionTable(data.devVersionReportList);
			createDevSystemTable(data.defectSystemList);
			$("#saveHis").removeClass('hideBlock');
		}
	});
}

function queryData(){
	var time = $("#startDate").val();
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
		success: function (data) {
			$("#loading").css('display', 'none');
			createDevVersionTable(data.devVersionReportList);
			createDevSystemTable(data.defectSystemList);
			$("#saveHis").removeClass('hideBlock');
		}
	});
}

function createDevVersionTable(data){
	$("#devVertionTable").bootstrapTable('destroy');
    $("#devVertionTable").bootstrapTable({
        queryParamsType:"",
        data:data,
        columns : [
        {
            field : "yearMonth",
            title : "时间",
            align : 'center',
            class : "stepOrder",
            width : '100px',
            formatter : function (value, row, index) {
                return value.split('-')[0]+"年"+value.split('-')[1]+"月";
            }
        },{
            field : "planWindowsNumber",
            title : "计划内版本次数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "tempWindowsNumber",
            title : "临时版本次数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "tempAddTaskNumber",
            title : "临时增加任务数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
                return '<div class="def_tableDiv2"><input type="text" class="form-control" onchange="calculatePercent()" id="addNumber" value="0"/></div>';
            }
        },{
            field : "tempDelTaskNumber",
            title : "临时删除任务数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
                return '<div class="def_tableDiv2"><input type="text" class="form-control" onchange="calculatePercent()" id="delNumber" value="0"/></div>';
            }
        },{
            field : "totalTaskNumber",
            title : "测试任务总数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
                return '<span id="totalTaskNumber">'+value+'</span>';
            }
        },{
            field : "requirementNumber",
            title : "业务需求数",
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
            field : "changePercent",
            title : "变更率",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
                return '<span id="changePercent">0</span>';
            }
        }],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
}

function calculatePercent(){
	var addNumber = $("#addNumber").val();
	var delNumber = $("#delNumber").val();
	var totalTaskNumber = $("#totalTaskNumber").text();
	if(!reg.test(addNumber)){
		$("#addNumber").val("");
		return;
	}
	if(!reg.test(delNumber)){
		$("#delNumber").val("");
		return;
	}
	var percent = (parseInt(addNumber)+parseInt(delNumber))/parseInt(totalTaskNumber)*100;
	$("#changePercent").text(totalTaskNumber == 0?"0%":percent.toFixed(2)+"%");
}

function createDevSystemTable(data){
	$("#devSystemTable").bootstrapTable('destroy');
    $("#devSystemTable").bootstrapTable({
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
            	var time = $("#startDate").val();
                return time.split('-')[0]+"年"+time.split('-')[1]+"月";
            }
        },{
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
        },{
            field : "lastmonthUndefectedNumber",
            title : "上月漏检缺陷数",
            align : 'center',
            class : "stepOrder",
            width : '30px',
            formatter : function (value, row, index) {
                return '<div class="def_tableDiv2"><input type="text" class="form-control" name="lastMonthDefectNum'+index+'" value="0"/></div>';
            }
        },{
            field : "lastmonthUndefectedBelonger",
            title : "上月漏检归属",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
                return '<div class="def_tableDiv2"><input type="text" class="form-control" name="lastMonthDefectBelonger'+index+'"/></div>';
            }
        }],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
}

function showSystems() {
	$.ajax({
		url: "/testManage/testCase/getAllSystem",
		method: "post",
		dataType: "json",
		success: function (data) {
			var list = data.data;
			system_arr = data.data;
			for (var i = 0; i < list.length; i++) {
				//先创建好select里面的option元素       
				var option = document.createElement("option");
				//转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				$(option).val(list[i].id);
				//给option的text赋值,这就是你点开下拉框能够看到的东西          
				$(option).text(list[i].systemName);
				//获取select 下拉框对象,并将option添加进select            
				$('#systemId').append(option);
			}
			$('.selectpicker').selectpicker('refresh');
		}
	});
}

function saveHis(){
	var allDevVersionTableData = $('#devVertionTable').bootstrapTable('getData');
	var allSystemTableData = $('#devSystemTable').bootstrapTable('getData');
	var flag = true;
	$.each(allDevVersionTableData,function(index,value){
		value.tempAddTaskNumber = $("#addNumber").val();
		value.tempDelTaskNumber = $("#delNumber").val();
		value.changePercent = $("#changePercent").text().split("%")[0];
		if(value.changePercent > 100){
			layer.alert("临时增加任务数 与 临时删除任务数 之和不能大于任务总数",{icon:0});
			flag = false;
			return false;
		}
	})
	$.each(allSystemTableData,function(index,value){
		value.yearMonth = $("#startDate").val();
		value.defectPercent = parseFloat(value.defectPercent).toFixed(2);
		value.avgRepairRound = parseFloat(value.avgRepairRound).toFixed(2);
		value.lastmonthUndefectedNumber = $("[name=lastMonthDefectNum"+index+"]").val();
		if(!reg.test(value.lastmonthUndefectedNumber)){
			layer.alert("第"+(index+1)+"行漏检缺陷数不为整数!",{icon:0});
			flag = false;
			return false;
		}
		value.lastmonthUndefectedBelonger = $("[name=lastMonthDefectBelonger"+index+"]").val();
	})
	if(!flag){
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/queryHis",
		method: "post",
		data:{
			time:$("#startDate").val()
		},
		dataType: "json",
		success: function (data) {
			if(data.flag){
				save(allDevVersionTableData,allSystemTableData);
			}else{
				layer.confirm('存在历史数据，是否覆盖?', function(index){
					  //do something
					save(allDevVersionTableData,allSystemTableData);
					  layer.close(index);
					});       
			}
			$("#loading").css('display', 'none');
		}
	});
	
}

function save(allDevVersionTableData,allSystemTableData){
	$.ajax({
		url: "/testManage/testReport/insertHis",
		method: "post",
		data:{
			allDevVersionTableData:JSON.stringify(allDevVersionTableData),
			allSystemTableData:JSON.stringify(allSystemTableData)
		},
		dataType: "json",
		success: function (data) {
			if(data.status == 1){
				layer.alert("保存成功",{icon:1});
			}else{
				layer.alert("保存失败，"+data.errorMessage,{icon:2});
			}
			$("#loading").css('display', 'none');
		}
	});
}

function exportExcel(){
	var startDate = $("#startDate").val()
//	var systemId = $("#systemId").selectpicker('val');
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/getChartData",
		method: "post",
		data:{
			time : startDate
//			systemIdStr : JSON.stringify(systemId)
		},
		success: function (data) {
			$("#loading").css('display', 'none');
			option1.xAxis.data = data.defectPro.time;
			option1.series[0].data = data.defectPro.defectPro;
			// 使用刚指定的配置项和数据显示图表。
		    myChart1.setOption(option1);
		    var defectProBase64 = myChart1.getDataURL({type:"jpeg",pixelRatio: 2,
		        backgroundColor: '#fff'});
		    option2.xAxis.data = data.defectProMonth.systemName;
			option2.series[0].data = data.defectProMonth.defectPro;
			// 使用刚指定的配置项和数据显示图表。
		    myChart2.setOption(option2);
		    var defectProMonthBase64 = myChart2.getDataURL({type:"jpeg",pixelRatio: 2,
		        backgroundColor: '#fff'});
		    
		    option2.xAxis.data = data.defectProYear.systemName;
			option2.series[0].data = data.defectProYear.defectPro;
			// 使用刚指定的配置项和数据显示图表。
		    myChart3.setOption(option2);
		    var defectProYearBase64 = myChart3.getDataURL({type:"jpeg",pixelRatio: 2,
		        backgroundColor: '#fff'});
		    
		    option2.xAxis.data = data.defectProProjectMonth.projectName;
			option2.series[0].data = data.defectProProjectMonth.defectPro;
			// 使用刚指定的配置项和数据显示图表。
		    myChart4.setOption(option2);
		    var defectProProjectMonthBase64 = myChart4.getDataURL({type:"jpeg",pixelRatio: 2,
		        backgroundColor: '#fff'});
		    
		    option2.xAxis.data = data.defectProProjectYear.projectName;
			option2.series[0].data = data.defectProProjectYear.defectPro;
			// 使用刚指定的配置项和数据显示图表。
		    myChart5.setOption(option2);
		    var defectProProjectYearBase64 = myChart5.getDataURL({type:"jpeg",pixelRatio: 2,
		        backgroundColor: '#fff'});
		    
		    option3.legend.data = data.defectLevel.level;
			option3.series[0].data = data.defectLevel.defectCount;
			// 使用刚指定的配置项和数据显示图表。
		    myChart6.setOption(option3);
		    var defectLevelBase64 = myChart6.getDataURL({type:"jpeg",pixelRatio: 2,
		        backgroundColor: '#fff'});
		    var params = {};
		    params.defectProImg = defectProBase64;
		    params.defectProMonthImg = defectProMonthBase64;
		    params.defectProYearImg = defectProYearBase64;
		    params.defectProProjectMonthImg = defectProProjectMonthBase64;
		    params.defectProProjectYearImg = defectProProjectYearBase64;
		    params.defectLevelImg = defectLevelBase64;
		    params.time = startDate;
		    post("/testManage/testReport/exportReport",params);
		}
	});
	
//	window.location.href = "/testManage/testReport/exportReport?startDate="+startDate+"&endDate="+endDate+"&systemIdStr="+systemId;
}

function post(url, params) { 
    // 创建form元素
    var temp_form = document.createElement("form");
    // 设置form属性
    temp_form .action = url;      
    temp_form .target = "_self";
    temp_form .method = "post";      
    temp_form .style.display = "none";
    // 处理需要传递的参数 
    for (var x in params) { 
        var opt = document.createElement("textarea");      
        opt.name = x;      
        opt.value = params[x];      
        temp_form .appendChild(opt);      
    }      
    document.body.appendChild(temp_form);
    // 提交表单      
    temp_form .submit();     
} 