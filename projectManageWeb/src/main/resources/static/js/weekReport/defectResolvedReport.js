/**
 * Description: 报表中心-缺陷待解决统计图
 * Author:
 * Date: 2020/4/20
 */

var is_check_Collect_flag = true;//是否继续收藏
//查询收藏obj
var collection_obj = {
	menuUrl: '../report/toDefectResolvedReport',
	filterName: '',
	search: [],
	isCollect: false
};

$(document).ready(function () {
	drap_chart();
	system_req();
	downOrUpButton();
	collect_handle();
	getCollection();
	var search_ele = ['systemIds', 'defectSource', 'emergencyLevel'];
	search_ele.map(function (ele) {
		$('#' + ele).on('change', function () {
			if (is_check_Collect_flag) {
				$(".collection").children("span").removeClass("fa-heart").addClass("fa-heart-o");
			}
		})
	})
	searchInfo();
});


/**
 * 展示未解决的缺陷图，按照当天距离缺陷提出日期的天数，分为5个等级
 * 1-2:当天距离缺陷提出日期1-2天，
 * 3-5：当天距离缺陷提出日期3-5天，
 * 6-10：当天距离缺陷提出日期6-10天，
 * 11-20：当天距离缺陷提出日期11-20天，
 * 20以上：当天距离缺陷提出日期20天以上
 * @param data
 * @returns
 */
function drap_chart(data) {
	var map_data = [['product', '1-2', '3-5', '6-10', '11-20', '20以上']];
	data && data.map(function (v,idx) {
		map_data.push([v.systemName,v.oneClass,v.twoClass,v.threeClass,v.fourClass,v.fiveClass]);
	})
	var myChart = echarts.init(document.getElementById('main_chart'));
	myChart.clear();
	var option = {
		legend: {},
		tooltip: {},
		toolbox: {},
		dataset: {
			source: map_data
		},
		xAxis: { type: 'category' },
		yAxis: {},
		series: [
			{ type: 'bar' },
			{ type: 'bar' },
			{ type: 'bar' },
			{ type: 'bar' },
			{ type: 'bar' },
		]
	}
	myChart.setOption(option);
}

//根据查询条件统计结果
function searchInfo() {
	var systemIds_ = $('#systemIds').val() || [];
	var defectSource = $('#defectSource').val() || [];
	var emergencyLevel = $('#emergencyLevel').val() || [];
	var sub_data = {
		"systemId": systemIds_,
		"defectSource": defectSource,
		"emergencyLevel": emergencyLevel,
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/report/defectResolved",
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		type: 'post',
		data: JSON.stringify(sub_data),
		success: function (data) {
			if(data.length){
				drap_chart(data);
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
	$('#defectSource').val('[]');
	$('#emergencyLevel').val('[]');
	$(".collection").children("span").removeClass("fa-heart").addClass("fa-heart-o");
	$('.selectpicker').selectpicker('refresh');
}

//填充子系统下拉框内容
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
					{ "type": "select", "value": { "defectSource": $('#defectSource').val() }, "isData": _is_null($('#defectSource')) },
					{ "type": "select", "value": { "emergencyLevel": $('#emergencyLevel').val() }, "isData": _is_null($('#emergencyLevel')) },
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

//选择收藏方案后，触发页面重新统计
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

