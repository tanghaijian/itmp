var allDataObj = {
	stageView: ""
}

//生成 Stage View信息
function createStageView(row) {


	allDataObj.stageView = row;
	$.ajax({
		url: "/devManage/structure/getStageView",
		method: "post",
		data: {
			"toolId": row.toolId,
			"jobName": row.jobName,
			"jenkinsId": row.jenkinsId,
			"jobRunNumber":row.jobRunNumber
		},
		success: function (data) {
			if (data.status == 2) {
				layer.alert(data.message, {
					icon: 2,
					title: "提示信息"
				});
			} else {
				if (data.stageView == null) {
					$("#loadingData").css("display", "block");
					$("#JenkinsJobsTable").css("display", "none");
					return;
				} else {
					$("#loadingData").css("display", "none");
					$("#JenkinsJobsTable").css("display", "inline-block");

					var strhead = '';
					var strbody = '';
					stageViewStats = '';
					if (data.stageView.status == "FAILED" || data.stageView.status == "ABORTED") {
						stageViewStats = 'headBodyBg_fail';
					}

					strbody += '<div class="headBody ' + stageViewStats + '">';
					for (var i = 0; i < data.stageView.stages.length; i++) {
						var content = '';
						var pausedTime = '';
						var describeId = '';

						strhead += '<div class="stage-header"><div>' + data.stageView.stages[i].name + '</div></div>';
						describeId = "describeId=" + data.stageView.stages[i].id;
						if (data.stageView.stages[i].pauseDurationMillis != 0) {
							pausedTime = '<div class="promptInfo"><span>(paused for ' + setTimefun(data.stageView.stages[i].pauseDurationMillis) + ')</span></div>';
						}

						if (data.stageView.stages[i].status == "SUCCESS") {
							content = '<div class="duration durationBg_success" ' + describeId + ' >' +
								setTimefun(data.stageView.stages[i].durationMillis) + pausedTime + '</div>';
						} else if (data.stageView.stages[i].status == "IN_PROGRESS") {
							content = '<div class="duration progress progress-striped active" ' + describeId + ' >' +
								'<div class="progress-bar progress-bar-striped " role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">' +
								'<span class="sr-only">100% Complete</span>loading...' +
								'</div></div>';
						} else if (data.stageView.stages[i].status == "FAILED") {
							content = '<div class="duration durationBg_fail" ' + describeId + ' >' +
								setTimefun(data.stageView.stages[i].durationMillis) + pausedTime + '<div class="cornerMark">failed</div></div>';
						}

						strbody += '<div class="stage-body">' + content + '</div>';
					}
					strbody += '</div>';
					$("#headDiv").empty().append(strhead);
					$("#stageBodyDiv").empty().append(strbody);

					if (data.stageView.status == "FAILED" || data.stageView.status == "SUCCESS") {
						autoSearchInfo();
					}
				}
			}
            //stage view节点点击
			$(".duration").on("click", function () {
				$("#loading").css("display", "block");
				$.ajax({
					url: "/devManage/structure/getStageViewLog",
					method: "post",
					data: {
						"toolId": allDataObj.stageView.toolId,
						"jobName": allDataObj.stageView.jobName,
						"jenkinsId": allDataObj.stageView.jenkinsId,
						"describeId": $(this).attr("describeId"),
						"jobRunNumber":row.jobRunNumber
					},
					success: function (data) {
						$("#loading").css("display", "none");
						$('#StageLogsInfo').empty();
						for (var i = 0; i < data.stageView.stageFlowNodes.length; i++) {
							var str = '<div class="StageLogsDiv"><div class="StageLogsTit" executionId=' + data.stageView.stageFlowNodes[i].id + ' >' +
								'<span class="fa fa-caret-square-o-down"></span>' +
								data.stageView.stageFlowNodes[i].name + '(self time ' + setTimefun(data.stageView.stageFlowNodes[i].durationMillis) + ')' + '</div><div class="StageLogsCont"></div></div>';
							$('#StageLogsInfo').append(str);
						}
						$(".StageLogsTit").on("click", function () {
							if ($(this).children(".fa").hasClass("fa-caret-square-o-down")) {
								$(".StageLogsTit").children(".fa").addClass("fa-caret-square-o-down");
								$(".StageLogsTit").children(".fa").removeClass("fa-caret-square-o-up");
								$(".StageLogsCont").slideUp();

								$(this).children(".fa").removeClass("fa-caret-square-o-down");
								$(this).children(".fa").addClass("fa-caret-square-o-up");
								if ($(this).attr("flag") == "1") {
									$(this).next(".StageLogsCont").slideDown();
								} else {
									var self = this;
									$("#loading").css("display", "block");
									$.ajax({
										url: "/devManage/structure/getStageViewLogDetail",
										method: "post",
										data: {
											"toolId": allDataObj.stageView.toolId,
											"jobName": allDataObj.stageView.jobName,
											"jenkinsId": allDataObj.stageView.jenkinsId,
											"executionId": $(self).attr("executionId"),
											"jobRunNumber":row.jobRunNumber
										},
										success: function (data) {
											$("#loading").css("display", "none");
											$(self).attr("flag", "1");
											var str = "<pre >" + data.stageView.text + "</pre>";
											$(self).next(".StageLogsCont").append(str);
											$(self).next(".StageLogsCont").slideDown();
										}
									});
								}
							} else {
								$(this).children(".fa").addClass("fa-caret-square-o-down");
								$(this).children(".fa").removeClass("fa-caret-square-o-up");
								$(this).next(".StageLogsCont").slideUp();
							}
						});
						layer.open({
							type: 1,
							area: '80%',
							offset: '40px',
							title: 'Stage Logs (build eureka)',
							content: $('#StageLogsInfo'), //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
							cancel: function (index, layero) {
								$('#StageLogsInfo').empty();
								layer.closeAll();
							}
						});
					}
				})
			});
		}
	});
}

//Stage View详情
function createNotes(row) {
	count = count + 1;
	var url = ""
	var data = "";
	url = "/devManage/structure/getLog";
	data = {
		"toolId": row.toolId,
		"jobName": row.jobName,
		"jenkinsId": row.jenkinsId,
		"count": count,
		"line": line,
		"jobRunNumber":row.jobRunNumber
	};

	$.ajax({
		url: url,
		method: "post",
		data: data,
		success: function (data) {
			var scrollIsBottom;
			if ($("#profile").outerHeight() + 37 + 40 - $("#scrollTop").scrollTop() <= $("#scrollTop").outerHeight()) {
				scrollIsBottom = true;
			} else {
				scrollIsBottom = false;
			}
			if (data.building == "false") {
				autoSearchInfo();
			};
			line = data.line;
			$("#logId").append(data.log);
			if (scrollIsBottom) {
				//37是 上方 小标签的高度，40 是  $('#scrollTop') 的 padding
				$('#scrollTop').scrollTop($("#profile").outerHeight() + 37 + 40);
			}

			/*if(data.flag=="false" || end=="true"){

				return false;
			}
			//为了避免异步造成问题
			if((data.building=="false" && lastbuilding=="true") || (data.building=="false" && lastbuilding=="false" && count>30)){
				end="true";
				clearInterval(timer);//结束循环

			}
			line=data.line;
			//num = "";
			if(data.building=="false" && ((lastbuilding=="false" || lastbuilding=="")  && count<=30)){
				//end为了防止最后日志量过大，导致最后2次返回一起，出现正在获取实时日志中请等待.....
				if(end!="true") {
					notes = "正在获取实时日志中请等待.....";
					line = 0;

				}
			} else if(data.building=="false" && (lastbuilding=="false"  && count>30)){
				notes="该系统处于排队中或运行错误请稍后查看!";
				line=0;
			}else{
//				console.log(notes=="正在初始化请等待.....");
				if(notes=="正在获取实时日志中请等待....." || notes=="正在初始化请等待....." ){
					var a = $("#logId");
					a.empty();
					notes="";
					num="";

				}
				num=data.log;

			}
			if(data.building=="false"){
				//判断上一次是否为true
				if((lastbuilding=="true") || ((lastbuilding=="false" && count>30)) ){ //正常结束 或者是一直静默期结束循环
					//	clearInterval(timer);//结束循环
					lastbuilding="";
					end="true";

				}
				if(count==1){
					lastbuilding=data.building;
				}
			}else{
				lastbuilding=data.building;
			}
			appendTo();*/
		}
	});
}

//传入毫秒 专换 时分秒
function setTimefun(time) {
	time = Math.abs(time);
	if (time < 1000) {
		return time + "ms"
	} else if (time < 60 * 1000) {
		return Math.round(time / 1000) + "s";
	} else if (time < 60 * 60 * 1000) {
		return parseInt(time / (60 * 1000)) + "min " + Math.round((time % (60 * 1000)) / 1000) + "s";
	} else {
		return parseInt(time / (60 * 60 * 1000)) + "h " + parseInt((time % (60 * 60 * 1000)) / (60 * 1000)) + "min " + Math.round((time % (60 * 1000)) / 1000) + "s";
	}
	return time;
}

//构建日志Stage View
function getStageViewHis_request(jobRunId) {
	$.ajax({
		url: '/devManage/structure/getStageViewHis',
		method: "post",
		data: {
			"jobRunId": jobRunId,
		},
		success: function (data) {
			if (data.status == 2) {
				layer.alert(data.message, {
					icon: 2,
					title: "提示信息"
				});
			} else {
				if (data.stageView == null) {
					return;
				} else {
					$("#StageView_history").empty();
					var strhead = '';
					stageViewStats = '';
					if (data.stageView.status == "FAILED" || data.stageView.status == "ABORTED") {
						stageViewStats = 'headBodyBg_fail';
					}
					for (var i = 0; i < data.stageView.stages.length; i++) {
						var content = '';
						var pausedTime = '';
						var describeId = '';
						strhead += '<div style="margin-right:20px;"><div class="stage-header"><div>' + data.stageView.stages[i].name + '</div></div>';
						describeId = "describeId=" + data.stageView.stages[i].id;
						if (data.stageView.stages[i].pauseDurationMillis != 0) {
							pausedTime = '<div class="promptInfo"><span>(paused for ' + setTimefun(data.stageView.stages[i].pauseDurationMillis) + ')</span></div>';
						}
						if (data.stageView.stages[i].status == "SUCCESS") {
							content = '<div class="duration durationBg_success" ' + describeId + '  style="text-align: center;">' +
								setTimefun(data.stageView.stages[i].durationMillis) + pausedTime + '</div>';
						} else if (data.stageView.stages[i].status == "IN_PROGRESS") {
							content = '<div class="duration progress progress-striped active" ' + describeId + '>' +
								'<div class="progress-bar progress-bar-striped " role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">' +
								'<span class="sr-only">100% Complete</span>loading...' +
								'</div></div>';
						} else if (data.stageView.stages[i].status == "FAILED") {
							content = '<div class="duration durationBg_fail" ' + describeId + '  style="text-align: center;">' +
								setTimefun(data.stageView.stages[i].durationMillis) + pausedTime + '<div class="cornerMark">failed</div></div>';
						}
						strhead += '<div class="stage-body" style="display: inline-block;">' + content + '</div></div>';
					}
					$("#StageView_history").append(strhead);
					if (data.stageView.status == "FAILED" || data.stageView.status == "SUCCESS") {
						autoSearchInfo();
					}
				}
			}
			$(".duration").on("click", function () {
				$("#loading").css("display", "block");
				$.ajax({
					url: "/devManage/structure/getStageViewLogHis",
					method: "post",
					data: {
						"jobRunId": jobRunId,
						"describeId": $(this).attr("describeId")
					},
					success: function (data) {
						$('#StageLogsInfo').empty();
						if (data.status == 1) {
							for (var i = 0; i < data.stageView.stageFlowNodes.length; i++) {
								var str = '<div class="StageLogsDiv"><div class="StageLogsTit" executionId=' + data.stageView.stageFlowNodes[i].id + ' >' +
									'<span class="fa fa-caret-square-o-down"></span>' +
									data.stageView.stageFlowNodes[i].name + '(self time ' + setTimefun(data.stageView.stageFlowNodes[i].durationMillis) + ')' + '</div><div class="StageLogsCont"></div></div>';
								$('#StageLogsInfo').append(str);
							}
							$(".StageLogsTit").on("click", function () {
								if ($(this).children(".fa").hasClass("fa-caret-square-o-down")) {
									$(".StageLogsTit").children(".fa").addClass("fa-caret-square-o-down");
									$(".StageLogsTit").children(".fa").removeClass("fa-caret-square-o-up");
									$(".StageLogsCont").slideUp();
									$(this).children(".fa").removeClass("fa-caret-square-o-down");
									$(this).children(".fa").addClass("fa-caret-square-o-up");
									if ($(this).attr("flag") == "1") {
										$(this).next(".StageLogsCont").slideDown();
									} else {
										var self = this;
										$("#loading").css("display", "block");
										$.ajax({
											url: "/devManage/structure/getStageViewLogDetailHis",
											method: "post",
											data: {
												"jobRunId": jobRunId,
												"executionId": $(self).attr("executionId")
											},
											success: function (data) {
												if (data.status == 1) {
													$(self).attr("flag", "1");
													var str = "<pre >" + data.stageView.text + "</pre>";
													$(self).next(".StageLogsCont").append(str);
													$(self).next(".StageLogsCont").slideDown();
												} else {
													layer.alert(data.message, { icon: 0 })
												}
												$("#loading").css("display", "none");
											}
										});
									}
								} else {
									$(this).children(".fa").addClass("fa-caret-square-o-down");
									$(this).children(".fa").removeClass("fa-caret-square-o-up");
									$(this).next(".StageLogsCont").slideUp();
								}
							});
							layer.open({
								type: 1,
								area: '80%',
								offset: '40px',
								title: 'Stage Logs (build eureka)',
								content: $('#StageLogsInfo'), //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
								cancel: function (index, layero) {
									$('#StageLogsInfo').empty();
									layer.closeAll();
								}
							});
						} else {
							layer.alert(data.message, { icon: 0 });
						}
						$("#loading").css("display", "none");
					}
				});
			});
		}
	})
}
