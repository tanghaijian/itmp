/**
 * Description: 报表中心-任务交付累计流图
 * Author:
 * Date: 2020/4/20
 */

var is_check_Collect_flag = true;//是否继续收藏
var collection_obj = {
	menuUrl: '../report/toWorkWeekReport',
	filterName: '',
	search: [],
	isCollect: false
};
/**
 * 交付状态：
 * 统计时间大于上线时间：已上线
   统计的时间大于测试完成时:待上线
   统计的时间大于首次实施中的时间：验证中
   统计的时间大于首次上测试环境的时间：待验证
   统计的时间大于状态变成实施完成的时间：实现完成
   统计时间大于第一次提交代码的时间：实现中
   统计时间大于开发任务创建的时间：就绪。
 */
var de_status = [
	{
		key:1,
		value:'就绪',
	},
	{
		key:2,
		value:'实现中',
	},
	{
		key:3,
		value:'实现完成',
	},
	{
		key:4,
		value:'待验证',
	},
	{
		key:5,
		value:'验证中',
	},
	{
		key:6,
		value:'待上线',
	},
	{
		key:7,
		value:'已上线',
	},
]
var legend_data = [];

var now = new Date();
var nowTime = now.getTime() ;
var day = now.getDay();
var oneDayTime = 24*60*60*1000 ;
var MondayTime = nowTime - (day-1)*oneDayTime ;//显示周一
var SundayTime =  nowTime + (7-day)*oneDayTime ;//显示周日
var MondayDate = (new Date(MondayTime)).toISOString().split("T")[0];
var SundayDate = (new Date(SundayTime)).toISOString().split("T")[0];

$(document).ready(function () {
	de_status.map(function(v){
		legend_data.push(v.value);
	})
	system_req();
	downOrUpButton();
	other();
	collect_handle();
	getCollection();
	searchInfo();
});

//图，以查询条件中的时间区间每天统计各种状态的开发任务数量
function drap_chart(data) {
	var type_data = [
		{
			name: '就绪',
			type: 'line',
			stack: '总量',
			areaStyle: {},data:[]
		},
		{
			name: '实现中',
			type: 'line',
			stack: '总量',
			areaStyle: {},data:[]
		},
		{
			name: '实现完成',
			type: 'line',
			stack: '总量',
			areaStyle: {},data:[]
		},
		{
			name: '待验证',
			type: 'line',
			stack: '总量',
			areaStyle: {},data:[]
		},
		{
			name: '验证中',
			type: 'line',
			stack: '总量',
			areaStyle: {},data:[]
		},
		{
			name: '待上线',
			type: 'line',
			stack: '总量',
			areaStyle: {},data:[]
		},
		{
			name: '已上线',
			type: 'line',
			stack: '总量',
			areaStyle: {},data:[]
		},
	]
	var week_data = [];
	data.length && data.map(function(v){
		type_data[0].data.push(v.ready);
		type_data[1].data.push(v.process);
		type_data[2].data.push(v.achieve);
		type_data[3].data.push(v.waitValid);
		type_data[4].data.push(v.inVerification);
		type_data[5].data.push(v.stayOnline);
		type_data[6].data.push(v.onLine);
		week_data.push(v.cliskDate)
	})
	var myChart = echarts.init(document.getElementById('main_chart'));
	myChart.clear();
	var option = {
		title: {
			//text: '任务交付累计流图'
		},
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'cross',
				label: {
					backgroundColor: '#6a7985'
				}
			}
		},
		legend: {
			data: legend_data
		},
		toolbox: {
			feature: {
				saveAsImage: {}
			}
		},
		grid: {
			left: '3%',
			right: '4%',
			bottom: '3%',
			containLabel: true
		},
		xAxis: [
			{
				type: 'category',
				boundaryGap: false,
				data: week_data
			}
		],
		yAxis: [
			{
				type: 'value'
			}
		],
		series: type_data
	};
	myChart.setOption(option);
}


//统计
/**
 * //Status_arr 存储多选的交付状态。
 * 存储的数组初始为[0,0,0,0,0,0,0]，下标0-6表示下拉框中的状态,
 * 如Status_arr[0]表示就绪，Status_arr[0]=1表示选择了就绪，Status_arr[0]=0表示没选择就绪
 * @returns
 */
function searchInfo() {
	var systemIds_ = $('#systemIds').val() || [];
	var Source_ = $('#Source').val() || [];
	var Status_ = $('#Status').val() || [];
	
	var Status_arr = [0,0,0,0,0,0,0];
	if(Status_.length){
		if(Status_.includes('1')){
			Status_arr[0] = 1;
		}
		if(Status_.includes('2')){
			Status_arr[1] = 1;
		}
		if(Status_.includes('3')){
			Status_arr[2] = 1;
		}
		if(Status_.includes('4')){
			Status_arr[3] = 1;
		}
		if(Status_.includes('5')){
			Status_arr[4] = 1;
		}
		if(Status_.includes('6')){
			Status_arr[5] = 1;
		}
		if(Status_.includes('7')){
			Status_arr[6] = 1;
		}
	}else{
		  Status_arr = [];
	}
	var Date_ = $('#Date').val() && $('#Date').val().split(' - ');
	var sub_data = { 
		"systemId": systemIds_,
		"requirementFeatureSource": Source_,
		"deliveryStatus": Status_arr, 
		"startTime": isValueNull(Date_[0]), 
		"endTime":isValueNull(Date_[1])
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/report/taskDeliver",
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		type: 'post',
		data: JSON.stringify(sub_data),
		success: function (data) {
			if(data.timeTraceList.length){
				drap_chart(data.timeTraceList);
			}
			$("#loading").css('display', 'none');
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
}

//清空
function clearSearch() {
	$('#systemIds').val('[]');
	$('#Source').val('[]');
	$('#Status').val('[]');
	$("#Date").val(MondayDate+' - '+SundayDate);
	$(".collection").children("span").removeClass("fa-heart").addClass("fa-heart-o");
	$('.selectpicker').selectpicker('refresh');
}

//其他配置
function other() {
	var search_ele = ['systemIds', 'Source', 'Status', 'Date'];
	search_ele.map(function (ele) {
		$('#' + ele).on('change', function () {
			if (is_check_Collect_flag) {
				$(".collection").children("span").removeClass("fa-heart").addClass("fa-heart-o");
			}
		})
	})
	de_status.map(function(v){
		$('#Status').append('<option value="'+v.key+'">'+v.value+'</option>');
	})
	$('#Status').selectpicker('refresh');
	var locale = {
		applyLabel: "确认",
		cancelLabel: "取消",
		resetLabel: "重置",
		"fromLabel": "起始时间",
		"toLabel": "结束时间",
		"daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
		"monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
		"firstDay": 1
	};
	$("#Date").daterangepicker({
		format: 'YYYY-MM-DD',
		startDate: new Date(MondayTime),
		endDate: new Date(SundayTime),
		minDate: 2000 - 01 - 01,
		maxDate: 2050 - 12 - 30,
		'locale': locale
	}).on('cancel.daterangepicker', function (ev, picker) {
	}).on('apply.daterangepicker', function (ev, picker) {
		$("#Date").change();
	});
    $("#Date").val(MondayDate+' - '+SundayDate);
}

//子系统查询下拉框数据填充
function system_req() {
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/modal/selectAllSystemInfo",
		dataType: 'json',
		type: 'post',
		data: {
			systemName: '',
			systemCode: '',
			pageNumber: 1,
			pageSize: 99
		},
		success: function (data) {
			data.rows.length && data.rows.map(val => {
				$("#systemIds").append(`
	              <option value="${val.id}">${val.systemName}</option>
	            `);
			})
			$('.selectpicker').selectpicker('refresh');
			
			$("#loading").css('display', 'none');
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
}

// 搜索框 收藏按钮控制 js 部分
function collect_handle() {
	$(".collection").click(function () {
		collection_obj.search = [];
		if ($(this).children("span").hasClass("fa-heart-o")) {
			collection_obj.search.push(
				{ "type": "select", "value": { "systemIds": $('#systemIds').val() }, "isData": _is_null($('#systemIds')) },
				{ "type": "select", "value": { "Source": $('#Source').val() }, "isData": _is_null($('#Source')) },
				{ "type": "select", "value": { "Status": $('#Status').val() }, "isData": _is_null($('#Status')) },
				{ "type": "input",  "value": { "Date": $("#Date").val() }, "isData": _is_null($("#Date")) },
			)
			var isResult = collection_obj.search.some(function (item) {
				return item.isData
			})
			collection_obj.isCollect = isResult;
			if (isResult) {
				$('#collect_Name').val('');
				$('#collect_Modal').modal('show');
			}
		} else {
			layer.confirm('确定要取消收藏吗?', { btn: ['确定', '取消'], icon: 0, title: "提示" }, function () {
				collection_obj.isCollect = false;
				collect_submit();
			})
		}
	})
}

//收藏提交
function collect_submit() {
	var sub_data = {};
	var sub_url = 'collectCondition';
	var is_name = true;
	if (collection_obj.isCollect) {
		if (!$('#collect_Name').val()) {
			is_name = false;
		}
		sub_data = {
			'menuUrl': collection_obj.menuUrl,
			'filterName': $('#collect_Name').val(),
			'defectReport': JSON.stringify(collection_obj.search),
		}
	} else {
		sub_data = { id: $("#projectType2").val() };
		sub_url = 'updateDefectReport';
	};
	if (!is_name) {
		layer.alert('请填写方案名称!', {
			icon: 0,
		})
		return;
	};
	$("#loading").css('display', 'block');
	$.ajax({
		type: "post",
		url: "/report/defectReport/" + sub_url,
		dataType: "json",
		data: sub_data,
		success: function (data) {
			if (collection_obj.isCollect) {
				if (data.code == 1) {
					$(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
					layer.alert('收藏成功!', {
						icon: 1,
					})
					$('#collect_Modal').modal('hide');
				}
			} else {
				clearSearch();
				$(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
				layer.alert('已取消收藏!', {
					icon: 1,
				})
			}
			getCollection();
			$("#loading").css('display', 'none');
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	});
}

//  获取收藏信息
function getCollection() {
	$("#projectType2").empty();
	$("#projectType2").append('<option value="">选择收藏方案</option>');
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/report/defectReport/selectDefectReportList",
		dataType: 'json',
		type: 'post',
		data: {
			menuUrl: collection_obj.menuUrl,
		},
		success: function (data) {
			if (data.length) {
				data.map(v => {
					$("#projectType2").append(`<option value="${v.id}">${v.filterName}</option>`);
				})
			}
			$("#projectType2").selectpicker('refresh');
			$("#loading").css('display', 'none');
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
}

//切换收藏
function tab_option(This) {
	clearSearch();
	var _id = $(This).val();
	if (!_id) {
		$(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
		return;
	};
	is_check_Collect_flag = true;
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/report/defectReport/selectDefectReportById",
		dataType: 'json',
		type: 'post',
		data: {
			id: _id,
		},
		success: function (data) {
			var msg = JSON.parse(data.favoriteContent);
			if (msg) {
				for (var i = 0; i < msg.length; i++) {
					for (var key in msg[i].value) {
						$("#" + key).val(msg[i].value[key]);
					}
				}
				$(".selectpicker").selectpicker('refresh');
				$(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
				searchInfo();
			}
			$("#loading").css('display', 'none');
		},
		error: function () {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
		}
	})
}






