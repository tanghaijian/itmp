var chart1 = echarts.init(document.getElementById('chart1'));
var chart2 = echarts.init(document.getElementById('chart2'));
var chart3 = echarts.init(document.getElementById('chart3'));
var chart4 = echarts.init(document.getElementById('chart4'));
var chart5 = echarts.init(document.getElementById('chart5'));
var chart6 = echarts.init(document.getElementById('chart6'));
var chart7 = echarts.init(document.getElementById('chart7'));
var chart8 = echarts.init(document.getElementById('chart8'));
$(document).ready(function () {
	dateComponent();
	getAllProject();
	(function () {
		window.addEventListener("resize", function () {
			this.chart1.resize();
			this.chart2.resize();
			this.chart3.resize();
			this.chart4.resize();
			this.chart5.resize();
			this.chart6.resize();
			this.chart7.resize();
			this.chart8.resize();
		});
	})();
	chart7.on('click', function (params) {
		// 控制台打印数据的名称 
		if (params.data.length == 3) {
			$.ajax({
				url: "/devManage/dashBoard/valueStreamMapping",
				method: "post",
				data: {
					timeTraceId: params.data[2],
				},
				success: function (data) {
					createEcharts8(data)
				}
			});
		}
	});
	//冲刺中的任务
	$('#sprintingDevelopTasksPrompt').on('mouseover', function () { var that = this; layer.tips('展示该系统在当前日期所属的冲刺中的任务情况，按照各个状态统计任务数量。', that, { tips: 1, time: 0, }); });
	$('#sprintingDevelopTasksPrompt').on('mouseout', function () { layer.closeAll(); });
	//最近7日的代码提交
	$('#last7daysCommitTimesPrompt').on('mouseover', function () { var that = this; layer.tips('统计该系统最近7日(含当期日期)的每日代码提交次数。', that, { tips: 1, time: 0, maxWidth: 330, }); });
	$('#last7daysCommitTimesPrompt').on('mouseout', function () { layer.closeAll(); });
	//代码提交总览
	$('#codeCommitPrompt').on('mouseover', function () { var that = this; layer.tips('展示该系统当前日期、最近7日、最近14日的代码提交总次数和总人数。', that, { tips: 1, time: 0, maxWidth: 330, }); });
	$('#codeCommitPrompt').on('mouseout', function () { layer.closeAll(); });
	//最近一次代码扫描问题总览
	$('#lastCodeScanPrompt').on('mouseover', function () { var that = this; layer.tips('展示该系统最近一次使用Sonar扫描后的问题数量统计，按照Sonar中默认的问题类型进行分类统计，鼠标移入后可展示该类型问题的数量和占比。', that, { tips: 1, time: 0, maxWidth: 330, }); });
	$('#lastCodeScanPrompt').on('mouseout', function () { layer.closeAll(); });
	//最近7次代码扫描问题趋势
	$('#last7daysCodeScanPrompt').on('mouseover', function () { var that = this; layer.tips('展示该系统最近7次每次扫描的5类问题数量变化趋势。', that, { tips: 1, time: 0, }); });
	$('#last7daysCodeScanPrompt').on('mouseout', function () { layer.closeAll(); });
	//累计流图
	$('#CFDPrompt').on('mouseover', function () { var that = this; layer.tips('是一个面积图，强调用户故事/需求/工单数量随时间而变化的程度，同时直观显示整体趋势走向。X轴代表时间，Y周代表各个状态下的用户故事/需求/工单数量。我们可以用它来跟踪和预测项目的进展情况，也能借助于这个图来识别潜在的问题和风险。', that, { tips: 1, time: 0, maxWidth: 400, }); });
	$('#CFDPrompt').on('mouseout', function () { layer.closeAll(); });
	//TTM分布图
	$('#TTMPrompt').on('mouseover', function () { var that = this; layer.tips('展示每一天投产的开发任务数，以及这些开发任务每一个的耗费天数。点击某一个点（某一个开发任务），会展示该开发任务的价值流图。', that, { tips: 1, maxWidth: 330, time: 0, }); });
	$('#TTMPrompt').on('mouseout', function () { layer.closeAll(); });
	//价值流图
	$('#VSMPrompt').on('mouseover', function () { var that = this; layer.tips('展示某一个开发任务各阶段的耗时。', that, { tips: 1, time: 0, }); });
	$('#VSMPrompt').on('mouseout', function () { layer.closeAll(); });
	//最近14日构建次数
	$('#last14daysBuildTimesPrompt').on('mouseover', function () { var that = this; layer.tips('展示该系统最近14日（含当前日期）的每日构建次数，绿色为成功次数，红色为失败次数。', that, { tips: 1, time: 0, }); });
	$('#last14daysBuildTimesPrompt').on('mouseout', function () { layer.closeAll(); });
	//最新构建记录
	$('#lastBuildTimesPrompt').on('mouseover', function () { var that = this; layer.tips('展示该系统最近5次构建记录，包括构建环境和构建开始时间，绿色为成功，红色为失败，点击后可以查看详细构建日志。', that, { tips: 1, time: 0, maxWidth: 330, }); });
	$('#lastBuildTimesPrompt').on('mouseout', function () { layer.closeAll(); });
	//最近14日平均构建耗时
	$('#last14daysBuildAveragesTime').on('mouseover', function () { var that = this; layer.tips('展示该系统最近14日，每日构建的平均耗时。', that, { tips: 1, time: 0, }); });
	$('#last14daysBuildAveragesTime').on('mouseout', function () { layer.closeAll(); });
	//构建情况总览
	$('#buildAllPrompt').on('mouseover', function () { var that = this; layer.tips('展示该系统当前日期、最近7日、最近14日的构建次数，以及最近7日的构建总次数、成功次数和失败次数。', that, { tips: 1, time: 0, maxWidth: 330, }); });
	$('#buildAllPrompt').on('mouseout', function () { layer.closeAll(); });
})

function dateComponent() {
	var locale = {
		"format": 'yyyy-mm-dd',
		"separator": " -222 ",
		"applyLabel": "确定",
		"cancelLabel": "取消",
		"fromLabel": "起始时间",
		"toLabel": "结束时间'",
		"customRangeLabel": "自定义",
		"weekLabel": "W",
		"daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
		"monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
		"firstDay": 1
	};
	$("#timeDate").daterangepicker({ 'locale': locale });
}

//获取一级——所有项目
function getAllProject() {
	$("#loading").css("display", "none");
	$.ajax({
		url: "/devManage/dashBoard/getAllProject",
		dataType: "json",
		type: "post",
		success: function (data) {
			$("#cheakProject").empty();
			$(".cheakSystem").css("display", "none");
			$("#cheakProject").append("<option value >请选择项目</option>");
			for (var i = 0; i < data.projectList.length; i++) {
				$("#cheakProject").append("<option value='" + data.projectList[i].id + "'>" + data.projectList[i].projectName + "</option>");
			}
			$('.selectpicker').selectpicker('refresh');
		}
	})
}
//获取二级——所有项目下的系统
function getSystem(This) {
	if ($(This).val() != '') {
		$.ajax({
			url: "/devManage/displayboard/getSystemByPId",
			dataType: "json",
			type: "post",
			data: {
				"projectId": $(This).val()
			},
			success: function (data) {
				$("#cheakSystem").empty();
				$(".cheakSystem").css("display", "block");
				$("#cheakSystem").append("<option value >请选择系统</option>");
				for (var i = 0; i < data.systemInfos.length; i++) {
					$("#cheakSystem").append("<option value='" + data.systemInfos[i].id + "'>" + data.systemInfos[i].systemName + "</option>");
				}
				$('.selectpicker').selectpicker('refresh');
			}
		})
	} else {
		$(".cheakSystem").css("display", "none");
		$(".cheakSprint").css("display", "none");
	}
}

//下拉框选择触发
function selectChange(This) {
	if ($(This).val() == '') {
		return;
	}
	var prevDays = new Date(now.getTime() - (24 * 60 * 60 * 1000 * 90));
	var startDate = formatDate(prevDays);
	var endDate = formatDate(now);
	$("#timeDate").val(formatDate(prevDays) + " - " + formatDate(now));
	$("#loading").css("display", "block");
	$("#projectSelect").val($(This).val());
	$.ajax({
		url: "/devManage/dashBoard/getDashBoard",
		method: "post",
		data: {
			systemId: $(This).val(),
			startDate: startDate,
			endDate: endDate,
		},
		success: function (data) {
			$('.newBuildInfo').empty();
			
			//$( ".valueMap" ).css( "display","none" );
			$(".timeChange").css("display", "block");
			
			//投产窗口展示区
			if (data.windowsList != null) {
				$(".nowwindowType").remove();
				$(".nextWindowType").remove();
				$(".nowWindows").html("本期投产");
				$("#sprintingDevelopTasksPrompt").css("display", "none");
				
				//投产窗口信息，有才展示
				if (data.windowsList.length == 0) {
					$(".nowCompletion").css("display", "none");
					$(".nextCompletion").css("display", "none");
				} else if (data.windowsList.length == 1) {
					
					$(".nowCompletion").css("display", "block");
					$(".nextCompletion").css("display", "none");
					$(".nowWindow").html(data.windowsList[0].windowName);
					if (data.windowsList[0].windowType == 1) {
						var str = '<span class="ruleWindow bgColor1 nowwindowType" style="margin-left:2px;">常规窗口</span>'
						$("#nowCompletionTit").append(str);
						//$(".nowWindowType").html("常规窗口")
					} else if (data.windowsList[0].windowType == 2) {
						var str = '<span class="ruleWindow bgColor1 nowwindowType"  style="margin-left:2px;">非常规窗口</span>'
							$("#nowCompletionTit").append(str);
						//$(".nowWindowType").html("非常规窗口")
					}
					if(data.windowsList[0].windowName!="待定"){
						//$(".nowTime").html(timestampToTime(data.windowsList[0].windowDate));
					}
					
					getWindowData(0, data.windowsList[0].id, $(This).val())
				} else {
					$(".nowCompletion").css("display", "block");
					$(".nextCompletion").css("display", "block");
					$("#sprintingDevelopTasksPrompt").css("display", "none");

					$(".nowWindow").html(data.windowsList[0].windowName);
					$(".nextWindow").html(data.windowsList[1].windowName);
					if(data.windowsList[0].windowName!="待定"){
						//$(".nowTime").html(timestampToTime(data.windowsList[0].windowDate));
					}
					if(data.windowsList[1].windowName!="待定"){
						//$(".nextTime").html(timestampToTime(data.windowsList[1].windowDate));
					}
					
					getWindowData(0, data.windowsList[0].id, $(This).val());
					getWindowData(1, data.windowsList[1].id, $(This).val());
					if (data.windowsList[0].windowType == 1) {
						var str = '<span class="ruleWindow bgColor1 nowwindowType" style="margin-left:2px;">常规窗口</span>'
							$("#nowCompletionTit").append(str);
						//$(".nowWindowType").html("常规窗口")
					} else if (data.windowsList[0].windowType == 2) {
						var str = '<span class="ruleWindow bgColor1 nowwindowType" style="margin-left:2px;">非常规窗口</span>'
							$("#nowCompletionTit").append(str);
						//$(".nowWindowType").html("非常规窗口")
					}
					if (data.windowsList[1].windowType == 1) {
						var str = '<span class="ruleWindow bgColor2 nextWindowType" style="margin-left:2px;">常规窗口</span>'
							$("#nextCompletionTit").append(str);
						/*$(".nextCompletion").html("常规窗口")*/
					} else if (data.windowsList[1].windowType == 2) {
						var str = '<span class="ruleWindow bgColor2 nextWindowType" style="margin-left:2px;">非常规窗口</span>'
							$("#nextCompletionTit").append(str);
							/*$(".nextCompletion").html("非常规窗口")*/
					}
				}
			} else if (data.featureList != null) {//开发任务展示
				$(".nowCompletion").css("display", "block");
				$(".nextCompletion").css("display", "none");
				$(".nowPending").css("display", "block");
				$(".nowPending1").css("display", "block");
				$(".nowTest").css("display", "block");
				$(".nowTest1").css("display", "block");
				$(".nowSingnOff").css("display", "block");

				$("#sprintingDevelopTasksPrompt").css("display", "block");

				$(".nowSingnOff1").css("display", "block");
				$(".nowWindows").html("冲刺中的开发任务");
				$(".nowWindow").html("");
				$(".nowTime").html("");
				$(".nowwindowType").remove();
				for (var i = 0; i < data.sprintInfo.length; i++) {
					var str = '<span class="ruleWindow bgColor1 nowwindowType" title="' + data.sprintInfo[i].sprintName + '" >' + data.sprintInfo[i].sprintName + '</span>'
					$("#nowCompletionTit").prepend(str);

				}

				$(".nowAllPro").html(0);
				$(".nowWaitpro").html(0);
				$(".nowDoing").html(0);
				$(".nowDone").html(0);
                $(".nowPending1").html(0);
                $(".nowTest1").html(0);
                $(".nowSingnOff1").html(0);
				for (var i = 0; i < data.featureList.length; i++) {
					if (data.featureList[i].requirementFeatureStatus == '10') {
						$(".nowAllPro").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '01') {
						$(".nowWaitpro").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '02') {
						$(".nowDoing").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '03') {
						$(".nowDone").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '04') {
						$(".nowPending1").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '05') {
						$(".nowTest1").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '06') {
						$(".nowSingnOff1").html(data.featureList[i].countStatus);
					}
				}
			}
			//代码管理区
			$(".today_Time").html(data.scmCount1.countScm)
			$(".scmCount7_Time").html(data.scmCount7.countScm)
			$(".scmCount14_Time").html(data.scmCount14.countScm)

			$(".today_Preson").html(data.scmCount1.countScmPeople)
			$(".scmCount7_Preson").html(data.scmCount7.countScmPeople)
			$(".scmCount14_Preson").html(data.scmCount14.countScmPeople)

			//构建区
			$(".jenkinsCount1").html(data.jenkinsCount1.inCount)
			$(".jenkinsCount7").html(data.jenkinsCount7.inCount)
			$(".jenkinsCount14").html(data.jenkinsCount14.inCount)

			$('.jenkinsListSuccess').html(0)
			$('.jenkinsListFail').html(0)
			for (var i = 0; i < data.jenkinsList7.length; i++) {
				if (data.jenkinsList7[i].buildStatus == 0) {
					$('.jenkinsListInCount').html(data.jenkinsList7[i].inCount)
				} else if (data.jenkinsList7[i].buildStatus == 2) {
					$('.jenkinsListSuccess').html(data.jenkinsList7[i].inCount)
				} else if (data.jenkinsList7[i].buildStatus == 3) {
					$('.jenkinsListFail').html(data.jenkinsList7[i].inCount)
				}
			}
			if (data.top5JenkinsList.length == 0) {
				var str = '<br>暂无数据<br><br>';
				$('.newBuildInfo').append(str);
			}
			for (var i = 0; i < data.top5JenkinsList.length; i++) {
				var datas = JSON.stringify(data.top5JenkinsList[i]).replace(/"/g, '&quot;');
				var str = '<div class="oneRecord"  onclick="showBuildInfo(' + datas + ')">';
				if (data.top5JenkinsList[i].buildStatus == 2) {
					str += '<div class="picStatus">';
				} else if (data.top5JenkinsList[i].buildStatus == 3) {
					str += '<div class="picStatus bgPos">';
				} else {
					str += '<div>';
				}
				str += '</div><span>' + data.top5JenkinsList[i].systemName + ' | ';
				if (data.top5JenkinsList[i].createType == 1) {
					str += getEnvironmentType(data.top5JenkinsList[i].environmentType) + '&nbsp&nbsp自动环境'
				} else if (data.top5JenkinsList[i].createType == 2) {
					str += data.top5JenkinsList[i].jobName + '手动环境';
				}
				str += '</span><span> ' + timestampToTime2(data.top5JenkinsList[i].startDate) + '</span></div>';
				$('.newBuildInfo').append(str);
			}
			initPage(data);
		},
	});
}
function getEnvironmentType(environmentType) {
	var str = ""
	$.ajax({
		type: "POST",
		url: "/devManage/dashBoard/getEnvironmentType",
		dataType: "json",
		data: {
			"environmentType": environmentType
		},
		async: false,
		success: function (data) {
			str = data.typeValue;
		}
	});
	return str;
}

function getWindowData(num, id, sid) {
	$.ajax({
		url: "/devManage/dashBoard/getFratureStatus",
		method: "post",
		data: {
			windowId: id,
			systemId: sid
		},
		success: function (data) {
			$(".nowPending").css("display", "none");
			$(".nowPending1").css("display", "none");
			$(".nowTest").css("display", "none");
			$(".nowTest1").css("display", "none");
			$(".nowSingnOff").css("display", "none");
			$(".nowSingnOff1").css("display", "none");
			if (num == 0) {
				
				$(".nowAllPro").html(0);
				$(".nowWaitpro").html(0);
				$(".nowDoing").html(0);
				$(".nowDone").html(0);
				$(".nowPending1").html(0);
				$(".nowTest1").html(0);
				$(".nowSingnOff1").html(0);
				for (var i = 0; i < data.featureList.length; i++) {
					
					
					if (data.featureList[i].requirementFeatureStatus == '10') {
						$(".nowAllPro").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '01') {
						$(".nowWaitpro").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '02') {
						$(".nowDoing").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '03') {
						$(".nowDone").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '04') {
						$(".nowPending1").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '05') {
						$(".nowTest1").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '06') {
						$(".nowSingnOff1").html(data.featureList[i].countStatus);
					}
				}
			} else if (num == 1) {
				$(".nextAllPro").html(0);
				$(".nextWaitpro").html(0);
				$(".nextDoing").html(0);
				$(".nextDone").html(0);
				$(".nowPending1").html(0);
				$(".nowTest1").html(0);
				$(".nowSingnOff1").html(0);
				for (var i = 0; i < data.featureList.length; i++) {
					if (data.featureList[i].requirementFeatureStatus == '10') {
						$(".nextAllPro").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '01') {
						$(".nextWaitpro").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '02') {
						$(".nextDoing").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '03') {
						$(".nextDone").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '04') {
						$(".nowPending1").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '05') {
						$(".nowTest1").html(data.featureList[i].countStatus);
					} else if (data.featureList[i].requirementFeatureStatus == '06') {
						$(".nowSingnOff1").html(data.featureList[i].countStatus);
					}
				}
			}
		},
	});
}
//判断是否为空
function is_Null(val){
	if (val == '%') return '0.0%'
	return val ? val : 0;
}
//判断%
function is_percentage(val){
	if (val == '0') return '0.0%'
	return val ? val*10 + '%' : '0.0%';
}

//构建记录弹窗信息
function showBuildInfo(data1) {
	$("#loading").css("display", "block");
	if (data1.buildStatus == 2) {
		$('#buildingSuccess').css('display', 'block');
		$('#buildingFail').css('display', 'none');
	} else if (data1.buildStatus == 3) {
		$('#buildingSuccess').css('display', 'none');
		$('#buildingFail').css('display', 'block');
	}

	$.ajax({
		url: "/devManage/structure/getBuildMessageById",
		method: "post",
		data: {
			jobRunId: data1.jenkinsId,
			createType: data1.createType
		},
		success: function (data) {
			$("#detailInfoSuccessStartTime").text(data.startDate);
			$("#detailInfoSuccessEndDate").text(data.endDate);
			$("#buildLogsInfoPre").text(data.buildLogs);
			$("#SonarResult").empty();
			var str = '';
			console.log(data.isSonar)
			if( data.isSonar == "flase" || data.list.length == 0){
				str = "<div class=''>未扫描代码，无数据。</div>"
			}else{
				for(var i=0;i<data.list.length;i++){
					str+='<div class="oneResult">'+
						'<input type="hidden" class="di_projectKey" value='+data.list[i].projectKey+' />'+
						'<input type="hidden" class="di_toolId" value='+data.list[i].toolId+' />'+
						'<input type="hidden" class="di_projectDateTime" value='+data.list[i].projectDateTime+' />'+
						'<div class="rowdiv">';
					if( data.list[i].moduleame!=undefined ){
						str+='<div class="form-group col-md-6"><label class="col-sm-4 control-label fontWeihgt">名称：</label><label class="col-sm-8 control-label font_left">'+data.list[i].moduleame+'</label></div>';
					}
					str+='<div class="form-group col-md-6"><label class="col-sm-4 control-label fontWeihgt">结束构建时间：</label><label class="col-sm-8 control-label font_left">'+data.list[i].xValue+'</label></div></div>'+
						'<div class="rowdiv" style="display: flex;flex-direction: row;">';
					str+='<div class="logTypeDiv" value="BUG"><div class="rowdiv"><span class="logTypeDivNum">'+is_Null(data.list[i].bug)+'</span>&nbsp;&nbsp;<span class="resultIcon1"></span></div><div><span class="fa fa-bug fontIconSize"></span><span>bugs</span></div></div>'+
						'<div class="logTypeDiv" value="VULNERABILITY"><div class="rowdiv"><span class="logTypeDivNum">'+is_Null(data.list[i].Vulnerabilities)+'</span>&nbsp;&nbsp;<span class="resultIcon2"></span></div><div><span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities</span></div></div>'+
						'<div class="logTypeDiv" value="CODE_SMELL"><div class="rowdiv"><span class="logTypeDivNum">'+is_Null(data.list[i].CodeSmells)+'</span>&nbsp;&nbsp;<span class="resultIcon3"></span></div><div><span class="fa fa-support fontIconSize"></span><span>Code Smells</span></div></div>'+
						'<div class="logTypeDiv" value="tests"><div class="rowdiv"><span class="logTypeDivNum">'+is_Null(data.list[i].tests)+'</span>&nbsp;&nbsp;<span class="resultIcon5"></span></div><div><span class="fa fa-file-text-o fontIconSize"></span><span>Tests</span></div></div>';
					str+='<div class="logTypeDiv"><div class="rowdiv"><span class="logTypeDivNum">'+is_Null(data.list[i].Coverage)+'</span>&nbsp;&nbsp;<span class="resultIcon4"></span></div><div><span class="fa fa-file-text fontIconSize"></span><span>Coverage</span></div></div>'+
						'<div class="logTypeDiv" style="border:none;"><div class="rowdiv"><span class="logTypeDivNum">'+is_Null(data.list[i].duplications)+'</span>&nbsp;&nbsp;<span class="resultIcon4"></span></div><div><span class="fa fa-refresh fontIconSize"></span><span>Duplications</span></div></div></div>'+
						'<div class="questionList"></div></div>';
				}
			} 
			$("#SonarResult").append(str);
			$("#detailInfoDIV").modal("show");
			$("#checkedRecord").modal("show");
			$("#loading").css("display", "none");
		},
		error: function () {
		}
	});


}

function showDetails(This) {

	var value = '';
	value = "." + $(This).attr("value");
	if ($(This).parent().parent().find(".questionList").find(value).length > 0) {
		$(This).parent().parent().find(".questionList .listTitle .fa-angle-double-up").addClass("fa-angle-double-down");
		$(This).parent().parent().find(".questionList .listTitle .fa").removeClass("fa-angle-double-up");
		$(This).parent().parent().find(".questionList .questionContent").css("display", "none");
		$(This).parent().parent().find(".questionList " + value + " .fa-angle-double-down").addClass("fa-angle-double-up");
		$(This).parent().parent().find(".questionList " + value + " .fa-angle-double-up").removeClass("fa-angle-double-down");
		$(This).parent().parent().find(".questionList " + value + " .questionContent").css("display", "block");
	} else {
		if ($(This).parent().parent().children(".di_projectKey").val() == 'null') {

			alert("手动构建，没有配置sonar扫描!");
		} else {
			$("#loading").css('display', 'block');
			$(This).parent().parent().find(".questionList .listTitle .fa-angle-double-up").addClass("fa-angle-double-down");
			$(This).parent().parent().find(".questionList .listTitle .fa").removeClass("fa-angle-double-up");
			$(This).parent().parent().find(".questionList .questionContent").css("display", "none");
			loadingPage(This);
		}
	}
}
function loadingPage(This) {
	var page = 1;
	$.ajax({
		url: "/devManage/structure/getSonarIssule",
		method: "post",
		data: {
			toolId: $(This).parent().parent().children(".di_toolId").val(),
			dateTime: $(This).parent().parent().children(".di_projectDateTime").val(),
			projectKey: $(This).parent().parent().children(".di_projectKey").val(),
			type: $(This).attr("value"),
			p: page,
		},
		success: function (data) {
			$(This).parent().parent().children(".questionList").children(".sonarList").children(".questionContent").children(".questionFooter").remove();
			var data = JSON.parse(data);
			var str = '<div class="sonarList ' + $(This).attr("value") + '" ><div class="listTitle">';
			if ($(This).attr("value") == 'BUG') {
				str += '<span class="fa fa-bug fontIconSize"></span><span>Bugs 问题清单</span>';
			} else if ($(This).attr("value") == 'CODE_SMELL') {
				str += '<span class="fa fa-support fontIconSize"></span><span>Code Smells 问题清单</span>';
			} else if ($(This).attr("value") == 'VULNERABILITY') {
				str += '<span class="fa fa-unlock-alt fontIconSize"></span><span>Vulnerabilities 问题清单</span>';
			}
			str += '<span class="fa fa-remove smallIcon" onclick="romoveSonarList(this)"></span> <span class="fa fa-angle-double-up smallIcon" onclick="down2(this)"></span></div><div class="questionContent"></div></div>';
			$(This).parent().parent().children(".questionList").append(str);
			var lastPath = '';
			for (var i = 0; i < data.issues.length; i++) {
				var onesDiv = '';
				if (lastPath != data.issues[i].component) {
					onesDiv += '<div class="questionPath"> ' + data.issues[i].component + ' </div>';
				}
				onesDiv += '<div class="questionBody"><div class="rowdiv"><div class="erorrCont fontWeihgt def_col_31">' + data.issues[i].message + '</div><div class="lineCont def_col_5 font_right">Line:' + data.issues[i].line + '</div></div>' +
					'<div class="rowdiv"><span>' + data.issues[i].author + '</span>&nbsp;&nbsp;&nbsp;<span>' + ((data.issues[i].creationDate.replace(/T/, ' ')).split("+"))[0] + '</span></div></div>';

				lastPath = data.issues[i].component;
				$(This).parent().parent().children(".questionList").children(".sonarList").children(".questionContent").append(onesDiv);
			}
			if (Math.ceil(data.total / data.ps) == data.p || data.total <= data.ps) {
				var footer = '<div class="questionFooter">显示 <span>' + data.total + '</span> / <span>' + data.total + '</span></div>';
			} else {
				var footer = '<div class="questionFooter">显示 <span>' + data.p * data.ps + '</span> / <span>' + data.total + '</span> <span class="a_style" value=' + page + ' onclick="moreLoadingPage(this)">更多...</span></div>';
			}
			$(This).parent().parent().children(".questionList").children(".sonarList").children(".questionContent").append(footer);
			$("#loading").css('display', 'none');
		},
	});
}
function down2(This) {
	if ($(This).hasClass("fa-angle-double-down")) {
		$(This).removeClass("fa-angle-double-down");
		$(This).addClass("fa-angle-double-up");
		$(This).parent().parent(".sonarList").children(".questionContent").slideDown(100);
	} else {
		$(This).addClass("fa-angle-double-down");
		$(This).removeClass("fa-angle-double-up");
		$(This).parent().parent(".sonarList").children(".questionContent").slideUp(100);
	}
}
function initPage(data) {
	chart1.clear();
	chart2.clear();
	chart3.clear();
	chart4.clear();
	chart5.clear();
	chart6.clear();
	chart8.clear(); 
	$("#title_").css( "display",'none' );
	
	createEcharts1(data.ScmCountList7);
	createEcharts2(data.jenkinsCountList);
	createEcharts3(data.jenkinsMinuteList);
	createEcharts6(data.timeTraceList);
	createEcharts7(data.timeTraceList1); 
	
	if (data.systemModuleList.length <= 0) {
		createEcharts4(data.moduleTrend);
		createEcharts5(data.moduleCount);
		$("#changeChildSystem").css("display", "none");
	} else {
		$("#changeChildSystem").css("display", "block");
		$("#childSystem").empty();
		$("#childSystem").append("<option value >请选择</option>")
		for (var i = 0; i < data.systemModuleList.length; i++) {
			$("#childSystem").append("<option value=" + data.systemModuleList[i].id + " >" + data.systemModuleList[i].moduleName + "</option>")
		}
		$('.selectpicker').selectpicker('refresh');
	}
	$("#loading").css("display", "none");

}



function timestampToTime(timestamp) {
	var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
	var Y = date.getFullYear() + '-';
	var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
	var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
	return Y + M + D;
}
function timestampToTime2(timestamp) {
	var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
	var Y = date.getFullYear() + '-';
	var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
	var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
	var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
	var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
	var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
	return Y + M + D + ' ' + h + m + s;
}
function down(This) {
	if ($(This).hasClass("fa-angle-double-down")) {
		$(This).removeClass("fa-angle-double-down");
		$(This).addClass("fa-angle-double-up");
		$(This).parents('.allInfo').children(".def_content").slideDown(100);
	} else {
		$(This).addClass("fa-angle-double-down");
		$(This).removeClass("fa-angle-double-up");
		$(This).parents('.allInfo').children(".def_content").slideUp(100);
	}
}

//最近7日的代码提交
function createEcharts1(data) {
	if (data == '') {
		return;
	}
	var x = [];
	var y = [];
	for (var i = 0; i < data.length; i++) {
		var xData = data[i].createDate.split(" ")[0];
		x.push(xData);
		y.push(data[i].countScm);
	}
	var option1 = {
		animation: false,
		title: {
			top: 6,
			left: 8,
			textStyle: {
				fontSize: 12,
				color: '#666666',
			}
		},
		tooltip: {
			trigger: 'axis',
			position: function (pt) {
				return [pt[0], '10%'];
			}
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			show: false,
			data: x,
			axisLabel: {
				show: false,
				interval: 0,
			},
			axisTick: {
				show: false
			},
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			},
			axisPointer: {
				label: {
					formatter: function (params) {
						return params.value;
					}
				}
			},
		},
		yAxis: {
			splitLine: { show: false },
			type: 'value',
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			},
			axisLabel: {
				textStyle: {
					color: '#666666',
					fontSize: 9
				}
			}
		},
		grid: {
			left: '5',
			right: '15',
			bottom: '10',
			top: '10',
			borderWidth: 0,
			containLabel: true
		},
		series: [{
			data: y,
			name: '次数',
			type: 'line',
			smooth: true,
			areaStyle: {
				color: ['#E5F4DD']
			},
			itemStyle: {
				normal: {
					lineStyle: {
						color: '#8BCE69'
					}
				}
			},
		}]
	};
	chart1.clear();
	chart1.setOption(option1);
}

//最近14日构建次数
function createEcharts2(data) {
	var x = [];
	var y1 = [];
	var y2 = [];
	for (var i = 0; i < data.length; i++) {
		var xData = data[i].createDate.split(" ")[0];
		x.push(xData);
		y1.push(data[i].successCount);
		y2.push(data[i].failCount);
	}
	option2 = {
		animation: false,
		tooltip: {
			trigger: 'axis',
			axisPointer: {            // 坐标轴指示器，坐标轴触发有效
				type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			},
			position: function (pt) {
				return [pt[0], '10%'];
			}
		},
		grid: {
			left: '5',
			right: '5',
			bottom: '20',
			top: '20',
			borderWidth: 0,
			containLabel: true
		},
		xAxis: {
			type: 'category',
			data: x,
			splitLine: {
				show: false
			},
			axisLabel: {
				show: false
			},
			axisTick: {
				show: false
			},
			axisLine: {
				show: true,
				onZero: true,
				lineStyle: {
					color: "#999999",
				}
			}
		},
		yAxis: {
			type: 'value',
			splitLine: {
				show: false
			},
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			},
			axisLabel: {
				textStyle: {
					color: '#666666',
					fontSize: 9
				}
			}
		},
		series: [
			{
				name: '成功',
				type: 'bar',
				stack: '总量',
				barWidth: 11,
				label: {
					normal: {
						show: false,
						position: 'insideRight'
					}
				},
				itemStyle: {
					normal: {
						color: "#98D87D"
					}
				},
				data: y1
			},
			{
				name: '失败',
				type: 'bar',
				stack: '总量',
				barWidth: 11,
				label: {
					normal: {
						show: false,
						position: 'insideRight'
					}
				},
				itemStyle: {
					normal: {
						color: "#FF6666"
					}
				},
				data: y2
			}
		]
	};
	chart2.clear();
	chart2.setOption(option2);
}

//最近14日平均构建耗时
function createEcharts3(data) {
	var x = [];
	var y = [];
	for (var i = 0; i < data.length; i++) {
		var xData = data[i].createDate.split(" ")[0];
		x.push(xData);
		y.push(data[i].minuteCount);
	}
	var option3 = {
		tooltip: {
			trigger: 'axis',
			position: function (pt) {
				return [pt[0], '10%'];
			}
		},
		animation: false,
		xAxis: {
			type: 'category',
			boundaryGap: false,
			data: x,
			axisLabel: {
				show: false,
				interval: 0,
				rotate: -20
			},
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			},
			axisTick: {
				show: false
			},
			axisPointer: {
				label: {
					formatter: function (params) {
						return params.value;
					}
				}
			},
		},
		yAxis: {
			splitLine: { show: false },
			type: 'value',
			axisLabel: {
				textStyle: {
					color: '#666666',
					fontSize: 9
				},
				formatter: '{value}',
			},
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			},
		},
		grid: {
			left: '5',
			right: '15',
			bottom: '10',
			top: '10',
			borderWidth: 0,
			containLabel: true
		},
		series: [{
			data: y,
			type: 'line',
			name: "耗时(秒)",
			smooth: true,
			areaStyle: {
				color: ['#FFE0FF']
			},
			itemStyle: {
				normal: {
					lineStyle: {
						color: '#FF66FF'
					}
				}
			},
		}]
	};
	chart3.clear();
	chart3.setOption(option3);
}

//最近7日代码扫描问题趋势
function createEcharts4(data) {
	var x = [];
	var yBugsCount = [];
	var yVulnerabilitiesCount = [];
	var ySmellsCount = [];
	var yCoverageCount = [];
	var yDuplicationsCount = [];
	for (var i = data.length-1; i >= 0; i--) {
		var xData = data[i].createDate.split(" ")[0];
		x.push(xData);
		yBugsCount.push(data[i].bugsCount);
		yVulnerabilitiesCount.push(data[i].vulnerabilitiesCount);
		ySmellsCount.push(data[i].smellsCount);
		yCoverageCount.push(data[i].coverageCount);
		yDuplicationsCount.push(data[i].duplicationsCount);
	}
	var option5 = {
		tooltip: {
			trigger: 'axis',
			position: function (pt) {
				return [pt[0], '10%'];
			}
		},
		animation: false,
		color: ['#49A9EE', '#FFD86E', '#98D87D', '#8996E6', '#F3857B'],
		legend: {
			icon: 'circle',
			padding: 1,
			itemGap: 4,
			itemWidth: 9,
			data: ['Bugs', 'Code Smells', 'Vulnerabilities', 'Duplications', 'Coverage'],
			textStyle: {
				fontSize: 10
			}
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			data: x,
			axisLabel: {
				show: false,
				interval: 0,
				rotate: -20
			},
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			},
			axisTick: {
				show: false
			},
			axisPointer: {
				label: {
					formatter: function (params) {
						return params.value;
					}
				}
			},
		},
		yAxis: {
			splitLine: { show: false },
			type: 'value',
			axisLabel: {
				textStyle: {
					color: '#666666',
					fontSize: 9
				}
			},
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			}
		},
		grid: {
			left: '5',
			right: '15',
			bottom: '10',
			top: '30',
			borderWidth: 0,
			containLabel: true
		},
		series: [
			{
				data: yBugsCount,
				name: 'Bugs',
				type: 'line',
				smooth: true,
				areaStyle: {
					color: ['#C8E5FA']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#49A9EE'
						}
					}
				},
			}, {
				data: ySmellsCount,
				type: 'line',
				name: 'Code Smells',
				smooth: true,
				areaStyle: {
					color: ['#FFF3D3']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#FFD86E'
						}
					}
				},
			}, {
				data: yVulnerabilitiesCount,
				type: 'line',
				name: 'Vulnerabilities',
				smooth: true,
				areaStyle: {
					color: ['#E0F3D8']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#98D87D'
						}
					}
				},
			}, {
				data: yDuplicationsCount,
				type: 'line',
				name: 'Duplications',
				smooth: true,
				areaStyle: {
					color: ['#DCDFF7']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#8996E6'
						}
					}
				},
			}, {
				data: yCoverageCount,
				type: 'line',
				name: 'Coverage',
				smooth: true,
				areaStyle: {
					color: ['#FBDAD7']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#F3857B'
						}
					}
				},
			}
		]
	};
	chart5.clear();
	chart5.setOption(option5);
}
//最近一次代码扫描问题总览
function createEcharts5(data) {
	if (data[0] != undefined) {
		var weatherIcons = {
			'bugs': "#springUrl('')/devManageui/images/dashBoard/bugs.png",
			'codeSmells': "#springUrl('')/devManageui/images/dashBoard/codeSmells.png",
			'vulnerabilities': "#springUrl('')/devManageui/images/dashBoard/vulnerabilities.png"
		};
		var myRich = {
			title: {
				color: '#eee',
				align: 'center'
			},
			bugs: {
				height: 16,
				align: 'left',
				backgroundColor: {
					image: weatherIcons.bugs
				}
			},
			codeSmells: {
				height: 16,
				align: 'left',
				backgroundColor: {
					image: weatherIcons.codeSmells
				}
			},
			vulnerabilities: {
				height: 16,
				align: 'left',
				backgroundColor: {
					image: weatherIcons.vulnerabilities
				}
			},
		};
		option4 = {
			tooltip: {
				trigger: 'item',
				formatter: "{b} : {c} ({d}%)"
			},
			animation: false,
			// color: ['#49A9EE', '#FFD86E', '#98D87D', '#8996E6', '#F3857B'],
			color: ['#49A9EE', '#FFD86E', '#98D87D'],
			grid: {
				left: '5',
				right: '5',
				bottom: '20',
				top: '60',
				containLabel: true
			},
			series: [
				{
					type: 'pie',
					radius: '50%',
					center: ['50%', '50%'],
					selectedMode: 'single',
					data: [
						{
							value: data[0].sonarBugs,
							name: 'Bugs',
							label: {
								normal: {
									formatter: '{{b}|}  bugs  \n  {rate|{d}%}',
									rich: myRich
								}
							},
						},
						{
							value: data[0].sonarCodeSmells, name: 'CodeSmells', selected: true,
							label: {
								normal: {
									formatter: '{{b}|}  codeSmells  \n  {rate|{d}%}',
									rich: myRich
								}
							}
						},
						{
							value: data[0].sonarVulnerabilities, name: 'Vulnerabilities',
							label: {
								normal: {
									formatter: '{{b}|}  vulnerabilities  \n  {rate|{d}%}',
									rich: myRich
								}
							}
						},
					],
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
		$('#Coverage').text('');
		$('#Duplications').text('');
		$('#UnitTestNumber').text('');
		chart4.clear();
		chart4.setOption(option4);
		$('#Coverage').text(is_percentage(data[0].sonarCoverage));
		$('#Duplications').text(is_percentage(data[0].sonarDuplications));
		$('#UnitTestNumber').text(is_Null(data[0].sonarUnitTestNumber));
		$('#title_').show();
	} else {
		chart4.clear();
		$('#Coverage').text('');
		$('#Duplications').text('');
		$('#UnitTestNumber').text('');
		$('#title_').hide();
	}
}

//累计流图
function createEcharts6(data) {
	var x = [];
	var yReady = [];
	var yProcess = [];
	var yAchieve = [];
	var yWaitValid = [];
	var yInVerification = [];
	var yStayOnline = [];
	var yOnLine = [];

	for (var i = 0; i < data.length; i++) {
		x.push(timestampToTime(data[i].cliskDate));
		yReady.push(data[i].ready);
		yProcess.push(data[i].process);
		yAchieve.push(data[i].achieve);
		yWaitValid.push(data[i].waitValid);
		yInVerification.push(data[i].inVerification);
		yStayOnline.push(data[i].stayOnline);
		yOnLine.push(data[i].onLine);
	}
	var option6 = {
		tooltip: {
			trigger: 'axis',
			extraCssText:'width:130px',
			position: function (pt, params, dom, rect, size) { 
				if( ( size.viewSize[0] - pt[0] ) > 130 ){
					return [pt[0], '10%'];
				} else{
					return [ size.viewSize[0] - 130 , '10%'];
				}
			}
		},
		animation: false,
		color: ['#5B9BD5', '#ED7D31', '#A5A5A5', '#FFC000', '#F3857B', '#70AD47', '#255E91'],
		legend: {
			icon: 'circle',
			padding: 1,
			itemGap: 4,
			itemWidth: 9,
			data: ['就绪', '实现中', '实现完成', '待验证', '验证中', '待上线', '已上线'],
			textStyle: {
				fontSize: 10
			}
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			data: x,
			axisLabel: {
				show: false,
				interval: 0,
			},
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			},
			axisTick: {
				show: false
			},
			axisPointer: {
				label: {
					formatter: function (params) {
						return params.value;
					}
				}
			},
		},
		yAxis: {
			splitLine: { show: false },
			type: 'value',
			axisLabel: {
				textStyle: {
					color: '#666666',
					fontSize: 9
				}
			},
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			}
		},
		grid: {
			left: '5',
			right: '15',
			bottom: '10',
			top: '30',
			borderWidth: 0,
			containLabel: true
		},
		series: [
			{
				data: yOnLine,
				type: 'line',
				name: '已上线',
				stack: '总量',
				areaStyle: {
					color: ['#5B9BD5']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#5B9BD5'
						}
					}
				},
			}, {
				data: yStayOnline,
				type: 'line',
				name: '待上线',
				stack: '总量',
				areaStyle: {
					color: ['#ED7D31']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#ED7D31'
						}
					}
				},
			}, {
				data: yInVerification,
				type: 'line',
				name: '验证中',
				stack: '总量',
				areaStyle: {
					color: ['#A5A5A5']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#A5A5A5'
						}
					}
				},
			}, {
				data: yWaitValid,
				type: 'line',
				name: '待验证',
				stack: '总量',
				areaStyle: {
					color: ['#FFC000']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#FFC000'
						}
					}
				},
			}, {
				data: yAchieve,
				type: 'line',
				name: '实现完成',
				stack: '总量',
				areaStyle: {
					color: ['#F3857B']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#F3857B'
						}
					}
				},
			}, {
				data: yProcess,
				type: 'line',
				name: '实现中',
				stack: '总量',
				areaStyle: {
					color: ['#70AD47']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#70AD47'
						}
					}
				},
			}, {
				data: yReady,
				name: '就绪',
				type: 'line',
				stack: '总量',
				areaStyle: {
					color: ['#255E91']
				},
				itemStyle: {
					normal: {
						lineStyle: {
							color: '#255E91'
						}
					}
				},
			},
		]
	};
	chart6.clear();
	chart6.setOption(option6);
}

//TTM分布图：最近12个月开发任务状态分布图
function createEcharts7(data) {
	var dateArr = [];
	var pointArr = [];
	var lineArr = [];
	for (var key in data) {
		dateArr.push(timestampToTime(Number(key)));
		if (data[key].length > 0) {
			var num = [];
			num[1] = 0;
			for (var i = 0; i < data[key].length; i++) {
				var arr = [];
				arr[0] = timestampToTime(Number(key));
				arr[1] = data[key][i].taskDays;
				arr[2] = data[key][i].id;
				pointArr.push(arr);
				num[1] += Number(data[key][i].taskDays);
			}
			num[0] = timestampToTime(Number(key));
			num[1] = (num[1] / data[key].length).toFixed(2);
			lineArr.push(num)
		}
	}
	option7 = {
		grid: {
			left: '5',
			right: '15',
			bottom: '10',
			top: '30',
			borderWidth: 0,
			containLabel: true
		},
		tooltip: {
			trigger: 'axis',
			extraCssText:'width:130px',
			position: function (pt, params, dom, rect, size) { 
				if( ( size.viewSize[0] - pt[0] ) > 130 ){
					return [pt[0], '10%'];
				} else{
					return [ size.viewSize[0] - 130 , '10%'];
				}
			}
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			data: dateArr,
			axisLabel: {
				show: false,
			},
			axisLine: {
				lineStyle: {
					color: '#999999',
					width: 1
				}
			},
			axisPointer: {
				snap: true,
				label: {
					formatter: function (params) {
						return params.value;
					}
				}
			},
		},
		yAxis: {
			splitLine: { show: false },
			type: 'value',
			axisLabel: {
				textStyle: {
					color: '#666666',
					fontSize: 9
				}
			},
			axisLine: {
				lineStyle: {
					color: '#666666',
					width: 1
				}
			}
		},
		series: [{
			name: '散点',
			symbolSize: 8,
			data: pointArr,
			type: 'scatter'
		}, {
			name: '平均数',
			type: 'line',
			data: lineArr
		}]
	};

	chart7.clear();
	chart7.setOption(option7);

}
function ifNull(data) {
	if (data == null) {
		data = "";
	} else {
		data = timestampToTime2(data);
	}
	return data;
}

//价值流图
function createEcharts8(data) {
	var x = [];
	var dataArr = [];
	var newDataArr = [];
	var dataStr = ['计划', '等待1', '开发', '等待2', '测试', '等待3', '上线'];
	var newStr = [];
	$(".valueMap").css("display", "block");

	for (var i = 0; i < data.dates.length; i++) {
		x.push(timestampToTime(data.dates[i]));
	}
	for (var i = 0; i < data.maps.length; i++) {
		if ((data.maps[i].endDate != null) && (data.maps[i].startDate != null)) {
			dataArr.push(data.maps[i]);
			newStr.push(dataStr[i]);
		}
	}

	for (var i = 0; i < dataArr.length; i++) {
		var obj = { name: newStr[i], type: 'line' };
		var arr = [];
		if (dataArr[i].status % 2 == 0) {
			arr[0] = [timestampToTime(dataArr[i].startDate), 1]
			arr[1] = [timestampToTime(dataArr[i].endDate), 1]
		} else {
			arr[0] = [timestampToTime(dataArr[i].startDate), -1]
			arr[1] = [timestampToTime(dataArr[i].endDate), -1]
		}
		obj.data = arr;
		newDataArr.push(obj);
	}

	var option8 = {
		tooltip: {
			trigger: 'none',
			axisPointer: {
				type: 'cross'
			}
		},
		legend: {
			data: newStr,
			top: 10,
		},
		grid: {
			left: '3%',
			right: '4%',
			bottom: '3%',
			top: 38,
			containLabel: true
		},
		xAxis: {
			type: 'category',
			data: x,
			axisLabel: {
				show: false,
			},
			axisPointer: {
				label: {
					formatter: function (params) {
						return params.value;
					}
				}
			},
		},
		yAxis: {
			type: 'value',
			max: 1.5,
			min: -1.5,
			axisLabel: {
				show: false,
			},
			axisTick: {
				show: false
			},
			axisPointer: {
				show: false,
			},
		},
		series: newDataArr
	};

	chart8.clear();
	chart8.setOption(option8);
	this.chart8.resize();
}
function romoveSonarList(This) {
	$(This).parent().parent(".sonarList").remove();
}

//切换子系统
function childSystemChange() {
	if ($("#childSystem").val() == "") {
		return;
	}
	$.ajax({
		url: "/devManage/dashBoard/getSonarByModuleId",
		method: "post",
		data: {
			systemModuleId: $("#childSystem").val(),
		},
		success: function (data) {
			createEcharts4(data.moduleTrend);
			createEcharts5(data.moduleCount);
		},
	});
}

//时间区间选择确定后，触发事件
function timeCycle(This) {
	var arr = $("#timeDate").val().split(" - ");
	if ($("#cheakProject").val() == '') {
		retuen;
	}
	$.ajax({
		url: "/devManage/dashBoard/getTheCumulative",
		method: "post",
		data: {
			systemId: $("#cheakSystem").val(),
			startDate: arr[0],
			endDate: arr[1],
		},
		success: function (data) {
			createEcharts6(data.timeTraceList);
			createEcharts7(data.timeTraceList1);
		},
	});
}


