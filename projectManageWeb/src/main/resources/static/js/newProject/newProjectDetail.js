/**************
 * 新建类项目详情
 * niexingquan
 * 2019-11-12
 */
var announcementObj = {
  arr: [],
  timer: {},
  title: '项目公告'
};

$(() => {
  Main_request();
})

/**
 * @description 导航样式 和请求
 */
function nav_href(result) {
  $(".nav_bar>div").hover(function () {   //导航hover样式切换
    $("#home").removeClass("myActive");
    $(this).find('.nav_tit').addClass('titActive');
    $(this).find('.nav_item').addClass('myActive');
    $(this).siblings().find('.nav_tit').removeClass('titActive');
    $(this).siblings().find('.nav_item').removeClass('myActive');
  }, function () {
    $('.nav_tit').removeClass('titActive');
    $('.nav_item').removeClass('myActive');
    $("#home").addClass("myActive");
    $("#home").siblings().addClass("titActive");
  })
  /**
   * @description 查询其他菜单路径
   * @param menuName  菜单名称
   */
  $.ajax({
    url: "/projectManage/projectHome/selectMenuButtonInfo",
    type: "post",
    dataType: "json",
    data: {
      "menuName": '冲刺管理,任务看板,开发任务管理,运维项目管理,公告管理',
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      let content_arr = {
        'risk': {
          menuButtonId: '9902',
          menuButtonName: '新建类风险管理',
          url: '/projectManageui/newProject/toRiskManage'
        },
        'problem': {
          menuButtonId: '9903',
          menuButtonName: '新建类问题管理',
          url: '../projectManageui/newProject/toQuestionManage'
        },
        'change': {
          menuButtonId: '9904',
          menuButtonName: '新建类变更管理',
          url: '../projectManageui/newProject/toUpdateManage'
        },
        'plan': {
          menuButtonId: '9905',
          menuButtonName: '新建类项目计划管理',
          url: '../projectManageui/newProject/toPlanChart'
        },
        'doc': {
          menuButtonId: '9906',
          menuButtonName: '新建类项目资产库',//../projectManageui/assetLibrary/systemPerspective/toEdit
          url: '../projectManageui/documentLibrary/toEdit',
        },
      }
      data.projectUrl.length && data.projectUrl.map(val=>{
        if(val.menuButtonName == '冲刺管理'){
          content_arr['sprint'] = val;
        }
        if(val.menuButtonName == '任务看板'){
          content_arr['board'] = val;     //【看板】，进入看板页面，默认显示当前项目
        }
        if(val.menuButtonName == '开发任务管理'){
          content_arr['job'] = val;
        }
        if(val.menuButtonName == '运维项目管理'){
          content_arr['team'] = val;
          content_arr['team']['url'] = '../projectManageui/oamproject/toEditProject';
        }
        if(val.menuButtonName == '公告管理'){
          content_arr['notice'] = val;
        }
      })
      for (let key in content_arr) {
        // 主页右上方   图标配置
        $("#" + key).bind("click", function () {
          let param = "?id=" + result.id;
          if (key == "notice") {  //【公告】，进入项目公告管理页面，默认显示当前项目的公告记录
            param = "?projectIds=" + result.id+"&type=1";
          }
          if (key == "doc") {   //【文档】，进入项目资产库系统视角
            param += ",name=" + encodeURIComponent(result.projectHome.projectName);
          }
          if (key == "risk" || key == "problem" || key == "change") {
            //risk      【风险】，进入项目风险管理页面，展示当前项目的风险记录
            //problem   【问题】，进入项目问题管理页面，展示当前项目的问题记录
            //change    【变更】，进入项目变更管理页面，展示当前项目的变更记录
            param += "&userId=" + result.projectHome.userId + "&userName=" + result.projectHome.userName + "&name=" + result.projectHome.projectName + "&type=1&toProjectType=2";
          }
          if (key == "plan") {  //【计划】，进入项目计划管理页面，展示当前项目的计划进度管理；
            param += "&name=" + result.projectHome.projectName + "&requestUserId=0";
          }
          if (key == "team") {  //【团队】，进入项目管理页面，默认显示当前项目；
            param = "?id=" + result.id + "&type=1" + "&home=1";
          }
          if (key == "sprint" || key == 'job') {
            let sy_id = '',sy_Name = '',sy_Code = '';
            if(result.interactedSystem.length){
              sy_id = result.interactedSystem.map(function(val){
                return val.systemId
              })
              sy_id.join(',');
              sy_Name = result.interactedSystem.map(function(val){
                return val.interactedSystem
              })
              sy_Name.join(',');
              sy_Code = result.interactedSystem[0].systemCode;
            }
            if(key == "sprint"){  //【冲刺】，进入冲刺管理页面，默认显示当前项目关联的系统下冲刺任务；
              param = "?projectId=" + result.id + "&projectName=" + encodeURIComponent(result.projectHome.projectName) + "&systemId=" + sy_id + "&systemName=" + sy_Name;
            }
            if(key == "job"){  //【任务】，进入开发任务页面，默认显示当前项目关联的系统下开发任务
              param = "?planId=" + result.id + "&planName=" + result.projectHome.projectName + 
                "&systemId=" + sy_id + "&systemName=" + sy_Name + "&systemCode=" + sy_Code;
            }
          }
          window.parent.toPageAndValue(content_arr[key].menuButtonName, content_arr[key].menuButtonId, content_arr[key].url + param);
        })
      }
      $("#loading").css('display', 'none');
    }
  })
}

/**
 * @description 查询项目主页所有数据
 * @param id
 * @param type   项目类型    2传统  1敏捷
 */
function Main_request() {
  $.ajax({
    url: "/projectManage/projectHome/home",
    type: "post",
    dataType: "json",
    data: {
      "id": $("#detail_id").val(),
      "type": $("#detail_type").val(),
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      summarize(data.projectHome, data.peripheralSystemList, data.systemUserNameList, data.interactedSystem);
      nav_href(data);
      if (data.notice) {
        data.notice.length && showAnnouncement(data.notice);
      }
      update_work(data.riskInfo);
      if ($("#detail_type").val() == 2) { //传统
        all_work(data.dadaDicSource, false);
        milestone_give(data.projectPlan);
        $('#projectUsers').addClass('min_h80');
        $('#project_Overview').addClass('min_h624');
        $('.work_body').addClass('p_t_10');
      } else if ($("#detail_type").val() == 1) { //敏捷
        $('#project_Overview').addClass('min_h664');
        $('#dynamic_body').addClass('min_max_hei');
        $('#sprint_tit').text('冲刺情况').after(`
	              <ul class="flex sprint_body bor_b">
	                <li class="sprint_item sprint_active">当前冲刺</li>
	                <li class="sprint_item">下一冲刺</li>
	              </ul>
	              <div class="work_body flex justify_c_c" style="overflow: auto;"></div>
	            `);
        all_work(data.dadaDicSource, data.nextDadaDicSource);
        $('#milestone_body').hide();
        $('#milestone_tit').text('燃尽图').after(`
	          <div class="p_10" id="exponential_chart" style="width: 450px;height: 300px;"></div>
	        `);
        chart_map(data.countWorkload, data.period - 1, data.projectPeriod);
      }
      if(data.messageInfo && data.messageInfo.length){
        project_dynamic(data.messageInfo);
      }else{
        $('#dynamic_body').append(`<h6 class="dynamic_item">暂无动态...</h6>`);
      }
      $("#loading").css('display', 'none');
      $('.sprint_item').bind('click', function () {
        $(this).addClass('sprint_active').siblings().removeClass('sprint_active');
        var _idx = $(this).index();
        $('.work_body').eq(_idx).show().siblings('.work_body').hide();
      })
    }
  })
}

/**
 * @description 项目概述
 */
function summarize(overview, system, users, interactedSystem) {
  if (overview) {
    $('.projectName').text(isValueNull(overview.projectName));
    $('#projectCode').text(isValueNull(overview.projectCode));
    var status_str = '';
    //进行状态：，未开始、结项为灰色，其他状态为绿色
    if (overview.projectStatus == 1 || overview.projectStatus == 4) {   
      status_str += `<span class="status status0">${select_dic('#PROJECT_STATUS', overview.projectStatus)}</span>`;
    } else {
      status_str += `<span class="status status1">${select_dic('#PROJECT_STATUS', overview.projectStatus)}</span>`;
    }
    //项目状态：根据项目计划情况进行判断，如有项目计划延期，则项目状态为“延期”，
    //显示为红色标签，如无项目计划延期，则状态为“正常”，显示为绿色标签；未开始或结项的项目，没有项目状态标签
    if (overview.status){
      if (overview.status == 2) {
        status_str += `<span class="status status2">${select_dic('#_STATUS', overview.status)}</span>`;
      } else if(overview.status == 1){
        status_str += `<span class="status status1">${select_dic('#_STATUS', overview.status)}</span>`;
      }
    }else{
      status_str += '';
    }
    if (overview.developmentMode) {
      status_str += `<span class="status status3">${select_dic('#DEVELOPMENT_MODE', overview.developmentMode)}</span>`;
    }
    
    $('#all_status').html(`${status_str}`);
    interactedSystem.length && interactedSystem.map(function (val) {
      $('#interactedSystem').append(`
        <li class="">
          <img class="img img_15" id="u669_img" src="../../../projectManageui/static/images/detail/u669.png">
          <span>${val.interactedSystem == null ? '' : val.interactedSystem}</span>
        </li>
      `);
    })
    $('#projectType').text(select_dic('#PROJECT_TYPE', overview.projectType));
    var planDate = isValueNull(overview.planStartDate) + '~' + isValueNull(overview.planEndDate);
    $('#projectWeek').text(planDate == '~' ? '' : planDate);
    $('#budgetCode').text(isValueNull(overview.budgetNumber));
    $('#projectManager').text(isValueNull(overview.userName));
  }
  system.length && system.map(function (val) {
    $('#rimSystem').append(`
      <li class="">
        <img class="img img_15" src="../../../projectManageui/static/images/detail/u671.png">
        <span>${val.systemName == null ? '' : val.systemName}</span>
      </li>
    `);
  })
  users.length && users.map(function (val, idx) {
    if (idx <= 8) {
      $('#projectUsers').append(`
        <li class="def_col_18">
          <i class="fa fa-user-o" aria-hidden="true"></i>
          <span>${val.userName == null ? '' : val.userName}</span>
        </li>
      `);
    }
  })
}

/**
 * @description 总任务赋值
 */
function all_work(taskForce, springNextWork) {
	if(taskForce.dadaDic && taskForce.dadaDic.length){
		$('.work_body').eq(0).append(`
			<div class="record">
		      <p class="overHidden">总任务数</p>
		      <h4 class="big_blue">${taskForce.dadaDicCount || 0}</h4>
		    </div>
		`);
		taskForce.dadaDic.map(function(v){
			$('.work_body').eq(0).append(`
			    <div class="record">
			      <p class="overHidden" title="${v.valueName}">${v.valueName}</p>
			      <h4 class="big_blue">${v.count || 0}</h4>
			    </div>		
			 `);
		})
	}else{
		  $('.work_body').eq(0).text('暂无数据!').css({minHeight:'86px'});
	}
  if(springNextWork){
	  if(springNextWork.dadaDic && springNextWork.dadaDic.length){
		  $('.work_body').eq(1).hide();
		  $('.work_body').eq(1).append(`
				<div class="record">
			      <p class="overHidden">总任务数</p>
			      <h4 class="big_blue">${springNextWork.dadaDicCount || 0}</h4>
			    </div>
			`);
		  springNextWork.dadaDic.map(function(v){
				$('.work_body').eq(1).append(`
				    <div class="record">
				      <p class="overHidden" title="${v.valueName}">${v.valueName}</p>
				      <h4 class="big_blue">${v.count || 0}</h4>
				    </div>		
				 `);
			})
	  }else{
		  $('.work_body').eq(1).text('暂无数据!').css({minHeight:'86px'}).hide();
	  }
  }
}

/**
 * @description 变更---------风险
 */
function update_work(work) {
  let wid = (+$('#falls').width() - 10) / 2;
  $('#update_work').html(`
    <div class="def_col_7">
      <img class="img" id="u645_img" src="../../../projectManageui/static/images/detail/u645.png">
      <p class="">变更</hp>
    </div>
    <div class="def_col_9 ">
      <p class="">本周新增</p>
      <h4 class="big_blue">${work.changeWeekAdd || 0}</h4>
    </div>
    <div class="def_col_9 ">
      <p class="">变更总数</p>
      <h4 class="big_blue">${work.changeInfoCount || 0}</h4>
    </div>
    <div class="def_col_9 ">
      <p class="">待确认数</p>
      <h4 class="big_blue">${work.confirmationNumber || 0}</h4>
    </div>
  `).css({width:wid+'px'});
  $('#risk_work').html(`
    <div class="def_col_7">
      <img class="img" id="u626_img" src="../../../projectManageui/static/images/detail/u626.png">
      <p class="">风险</p>
    </div>
    <div class="def_col_9 ">
      <p class="">本周新增</p>
      <h4 class="big_blue">${work.riskWeekAdd || 0}</h4>
    </div>
    <div class="def_col_9 ">
      <p class="">风险总数</p>
      <h4 class="big_blue">${work.riskInfoCount || 0}</h4>
    </div>
    <div class="def_col_9 ">
      <p class="">未解决数</p>
      <h4 class="big_blue">${work.outStandingNumber || 0}</h4>
    </div>
  `).css({width:wid+'px'});
  $(window).resize(function(){
    let wid = (+$('#falls').width() - 10) / 2;
    $('#update_work').css({width:wid+'px'});
    $('#risk_work').css({width:wid+'px'});
  })
}

/**
 * @description 里程碑
 */
function milestone_give(milestones) {
  milestones.length && milestones.map(function (val) {
    var schedule_color = '';
    if (val.progress == 100) {
      schedule_color = 'bg_cyan';
    } else if (val.progress > 50) {
      schedule_color = 'bg_blue';
    } else {
      schedule_color = 'bg_red';
    }
    $('#milestone_body').append(`
      <p class="milestone rowdiv m_t_10">
        <span class="def_col_12 overHidden" title="${val.planName}">${val.planName}</span>
        <span class="def_col_11">${val.planStartDate}~${val.planEndDate}</span>
        <span class="def_col_12 schedule"><span class="${schedule_color}" style="width:${val.currentProgress}%;"></span></span>
        <span class="def_col_3">${val.currentProgress}%</span>
      </p>
    `);
  })
}

/**
 * @description 燃尽图
 */
function chart_map(countWorkload, period, projectPeriod) {
  var weed = (countWorkload / period).toFixed(1); //
  var estimateRemainWorkload = [];   //实际
  var estimateWorkload = [];   //预计
  var date = [];

  if (projectPeriod != null && projectPeriod.length>0){
      for (var i = 0; i < projectPeriod.length; i++) {
    	  if(new Date().getTime() > new Date(projectPeriod[i].date).getTime()){
    		  estimateRemainWorkload[i] = projectPeriod[i].estimateRemainWorkload == null ? 0 : projectPeriod[i].estimateRemainWorkload;
    	  }
          if (i == projectPeriod.length - 1) {
              estimateWorkload[i] = 0;
          } else {
              estimateWorkload[i] = (countWorkload - (weed * i)).toFixed(1);
          }
          date[i] = projectPeriod[i].date;
      }
  }


  var myChart = echarts.init(document.getElementById('exponential_chart'));
  var options = {
    title: {},
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      right: 40,
      // data: ['实际', '参考']
      data: ['参考', '实际']
    },
    xAxis: {
      type: 'category',
      name: '日期',
      boundaryGap: false,
      splitLine: {
        show: false
      },
      data: date
    },
    yAxis: {
      name: '工时'
    },
    series: [
      {
        name: '参考',
        // name: '实际',
        type: 'line',
        data: estimateWorkload,
        itemStyle: {
          normal: {
            // 折点颜色样式
            color: 'green',
            lineStyle: {
              // 折线颜色样式
              color: 'green'
            }
          }
        },
        // data: this.opinionData,
      },
      {
        // name: '参考',
        name: '实际',
        type: 'line',
        data: estimateRemainWorkload,
        itemStyle: {
          normal: {

            // 折点颜色样式
            color: 'orange',
            lineStyle: {
              // 折线颜色样式
              color: 'orange'
            }
          }
        },
      },
    ]
  }
  myChart.setOption(options);
}

/**
 * @description 项目动态
 */
function project_dynamic(messageInfo) {
  let _class = '';
  if($("#detail_type").val() == 1){
    _class = 'text_ell3';
  }
  messageInfo.map(function (val) {
    $('#dynamic_body').append(`
      <li class="dynamic_item">
        <p class="rowdiv">
          <span class="def_col_20 overHidden bold p_l_0" title="${val.messageTitle}">${val.messageTitle}</span>
          <span class="def_col_16">${val.createDate}</span>
        </p>
        <p class="p_t_0 text_ell ${_class}" title="${val.messageContent || ''}">${val.messageContent || ''}</p>
      </li>
    `);
  })
}

/**
 * @description 字典查数据
 */
function select_dic(ele, value) {
  var _str = '';
  $(ele).find('option').each(function (idx, val) {
    if (value == val.value) {
      _str = val.innerText
    }
  })
  return _str
}
