/**
 * Description: 报表中心-缺陷统计图
 * Author:
 * Date: 2020/4/20
 */

var is_check_Collect_flag = true;//是否继续收藏
var collection_obj = {
	menuUrl: '../report/toDefectInfoReport',
	filterName: '',
	search: [],
	isCollect: false
};

var now = new Date();
var nowTime = now.getTime() ;
var day = now.getDay();
var oneDayTime = 24*60*60*1000 ;
var MondayTime = nowTime - (day-1)*oneDayTime ;//显示周一
var SundayTime =  nowTime + (7-day)*oneDayTime ;//显示周日
var MondayDate = (new Date(MondayTime)).toISOString().split("T")[0];
var SundayDate = (new Date(SundayTime)).toISOString().split("T")[0];

$(document).ready(function () {
	drap_chart();
	system_req();
	downOrUpButton();
	other();
	collect_handle();
	getCollection();
	searchInfo();
});

//3&4&5存量：缺陷中紧急程度为345的存量，'1&2存量'：缺陷中紧急程度为12的存量
function drap_chart(data) {
	var type_data = [
		{
				name: '3&4&5存量',
				type: 'bar',
				stack: '广告',
				label: {
						show: true,
				},
				data: []
		},
        {
            name: '1&2存量',
            type: 'bar',
            stack: '广告',
            label: {
                show: true,
            },
            data: []
        },{
            name: '每日新增',
            type: 'bar',
            stack: '广告',
            label: {
                show: true,
            },
            data: []
        },
	]
	var legend_data = ['每日新增','1&2存量','3&4&5存量'],week_data = [];
	if(data){
		data.map(function(v){
			week_data.push(v.date);
			type_data[0].data.push(v.mildNumber);
			type_data[1].data.push(v.seriousNumber);
			type_data[2].data.push(v.theNewNumber);
		})
	}
	var Date_ = $('#Date').val() && $('#Date').val().split(' - ');
	var myChart = echarts.init(document.getElementById('main_chart'));
	myChart.clear();
	var option = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
		legend: {
			data: legend_data
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
function searchInfo(data) {
	var systemIds_ = $('#systemIds').val() || [];
	var Source_ = $('#Source').val() || [];
	var Date_ = $('#Date').val() && $('#Date').val().split(' - ');
	var sub_data = { 
		"systemId": systemIds_,
		"defectSource": Source_,
		"startTime": isValueNull(Date_[0]), 
		"endTime":isValueNull(Date_[1])
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testDefectInfo",
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		type: 'post',
		data: JSON.stringify(sub_data),
		success: function (data) {
			if(data.defectInfoList.length){
				drap_chart(data.defectInfoList);
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
		minDate: 1999 - 12 - 12,
		maxDate: 2050 - 12 - 30,
		'locale': locale
	}).on('cancel.daterangepicker', function (ev, picker) {
	}).on('apply.daterangepicker', function (ev, picker) {
		$("#Date").change();
	});
	$("#Date").val(MondayDate+' - '+SundayDate);
}

//子系统查询下拉框填充数据
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
//					{ "type": "input",  "value": { "Date": $("#Date").val() }, "isData": _is_null($("#Date")) },
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
				// layer.alert('已取消收藏!', {
				// 	icon: 1,
				// })
				layer.closeAll('dialog');
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

// 选择收藏方案下拉框填充数据
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

//选择收藏方案下拉框，选择切换，触发页面重新刷新图表信息
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
