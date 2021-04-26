/**************
 * 新建类项目群管理主页js
 * niexingquan
 * 2019-11-12
 */
var animate_time = 600;//动画时间
var announcementObj = {
  arr: [],
  timer: {},
  title: '项目公告'
};
$(() => {
  Main_request();
})

//项目群主页信息
function Main_request() {
  $.ajax({
    url: "/projectManage/program/programProjectHome",
    type: "post",
    dataType: "json",
    data: {
      "id": $("#project_id").val(),
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
    	//概况
      summarize(data.programInfoHome, data.programProject);
      //导航图标
      nav_href(data);
      //项目公告：最顶层公告
      if (data.projectNotice) {
        data.projectNotice.length && showAnnouncement(data.projectNotice);
      }
      
      //风险与变更
      update_work(data.riskInfoAndChangeInfo);
      
      //项目群过程
      if (data.programProject.length) {
        if (data.plan.length) {
          let new_arr = []
          data.programProject.map(function (val, idx) {
            data.plan.map(function (value, index) {
              if (val.projectId == value.projectId) {
                new_arr.push({
                  developmentMode: value.developmentMode,
                  projectName: value.projectName,
                  milestones: value.milestones,
                  minus: value.minus,
                  projectCount: value.projectCount,
                  projectId: value.projectId,
                })
              } else {
                val.milestones && new_arr.push({
                  developmentMode: val.developmentMode,
                  projectName: val.projectName,
                  milestones: val.milestones,
                  projectId: val.projectId,
                })
              }
            })
          })
          process_give(unique(new_arr,'projectId'));
        } else {
          process_give(data.programProject);
        }
      }
      
      //项目动态
      if(data.messageInfo && data.messageInfo.length){
        project_dynamic(data.messageInfo);
      }else{
        $('#dynamic_body').append(`<h6 class="dynamic_item">暂无动态...</h6>`);
      }
      $("#loading").css('display', 'none');
    }
  })
}

//导航事件
function nav_href(data) {
  $(".nav_bar>div").hover(function () {
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
    'notice': {
      menuButtonId: '9912',
      menuButtonName: '公告管理',
      url: '../devManageui/notice/toNoticeManage'
    },
  }
  for (let key in content_arr) {
    $("#" + key).bind("click", function () {
      let id_str = '',param = '';
      data.programProject.length && data.programProject.map(function (val,idx) {
        id_str += idx == data.programProject.length - 1 ? val.projectId : val.projectId + ',';
      })
      if (key == "notice") {
        param = "?projectIds=" + id_str;
      }else{
        param = "?id=" + data.programInfoHome.id + "&userId=" + data.programInfoHome.userId + "&userName=" + data.programInfoHome.userName + "&name=" + data.programInfoHome.programName + "&type=2";
      }
      window.parent.toPageAndValue(content_arr[key].menuButtonName, content_arr[key].menuButtonId, content_arr[key].url + param);
    })
  }
}

//项目概述
function summarize(overview, programProject) {
  $('.projectName').text(isValueNull(overview.programName));
  $('#projectCode').text(isValueNull(overview.programNumber));
  $('#projectManager').text(isValueNull(overview.userName));
  programProject.length && programProject.map(function (val) {
    var status_str1 = '';
    if(!val.projectScheduleStatus){
    	status_str1 = `<span class="status"></span>`;
    }else{
    	if (val.projectScheduleStatus == 1 || val.projectScheduleStatus == 4) {
	      status_str1 = `<span class="status status0">${select_dic('#_STATUS', val.projectScheduleStatus)}</span>`;
	    } else{
	      status_str1 = `<span class="status status1">${select_dic('#_STATUS', val.projectScheduleStatus)}</span>`;
	    }
    }
    var status_str = '<span class="status"></span>';
    if (val.projectStatus == 1) {
      status_str = `<span class="status status1">${select_dic('#PROJECT_STATUS', val.projectStatus)}</span>`;
    } else if (val.projectStatus == 2) {
      status_str = `<span class="status status2">${select_dic('#PROJECT_STATUS', val.projectStatus)}</span>`;
    }
    $('#rimSystem').append(`
      <li class="rowdiv p_b_5">
        <p class="def_col_16 p_l_0 _show_ellipsis" title="${val.projectName}">${val.projectName}</p>
        <p class="def_col_20 p_l_0">${status_str1}${status_str}</p>
      </li>
    `);
  })
  programProject.length && programProject.map(function (val) {
    $('#projectUsers').append(`
      <li class="rowdiv">
        <p class="def_col_18 p_l_0 _show_ellipsis" title="${val.projectName}">${val.projectName}</p>
        <p class="def_col_18 p_l_0 _show_ellipsis">项目经理: ${val.userName}</p>
      </li>
    `);
  })
}

//变更---------风险
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
  `).css({width:wid+'px'});;
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

//项目群过程
function process_give(process) {
  let chart_num = [];
  process.map(function (val, idx) {
    var mil_str = '';
    val.milestones && val.milestones.length && val.milestones.map(function (value) {
      var schedule_color = '';
      if (value.progress == 100) {
        schedule_color = 'bg_cyan';
      } else if (value.progress > 50) {
        schedule_color = 'bg_blue';
      } else {
        schedule_color = 'bg_red';
      }
      if (val.developmentMode == 2) {
        mil_str += `
          <p class="rowdiv milestone p_10">
            <span class="def_col_11 overHidden" title="${value.name}">${value.name}</span>
            <span class="def_col_12">${value.date}</span>
            <span class="def_col_12 schedule">
              <span class="${schedule_color}" style="width:${value.progress}%;"></span>
            </span>
            <span class="def_col_3">${value.progress}%</span>
          </p>
        `;
      }
    })
    var pro_color = '';
    if (val.developmentMode == 2) {
      pro_color = 'status1';
    } else {
      pro_color = 'status4';
      mil_str += `<div class="p_10 m_auto" id="chart_map${idx}" style="width: 450px;height: 300px;"></div>`;
      chart_num.push({ ele: `chart_map${idx}`, data: val.milestones ,count: val.projectCount ,minus: val.minus});
    }
    $('#milestone_body').append(`
      <li>
        <p class="rowdiv">
          <i class="def_col_2 tree_icon"></i>
          <span class="def_col_23 bold _show_ellipsis" title="${val.projectName}">${val.projectName}</span>
          <span class="def_col_4 status p_l_0 ${pro_color}">${val.developmentMode == 2 ? '传统' : '敏捷'}</span>
        </p>
        <div class="milestone_item _hide">${mil_str}</div>
      </li>
    `);
  })
  chart_num.length && chart_num.map(function (val) {
    val && chart_map(val.ele,val.count ,val.data,val.minus);
  })
  $('.tree_icon').eq(0).addClass('tree_icon_open');//默认第一个展开
  $('.milestone_item').eq(0).slideDown(animate_time);
  $('.tree_icon').bind('click', function () {
    if (!$(this).hasClass('tree_icon_open')) {
      $(this).addClass('tree_icon_open');
      $(this).parent().parent().siblings().find('.tree_icon').removeClass('tree_icon_open');
      $(this).parent().siblings('.milestone_item').slideDown(animate_time);
      $(this).parent().parent().siblings().find('.milestone_item').slideUp(animate_time);
    } else {
      $(this).parent().siblings('.milestone_item').slideUp(animate_time);
      $(this).removeClass('tree_icon_open').parent().parent().siblings().find('.tree_icon').removeClass('tree_icon_open');
    }
  })
}

//燃尽图
function chart_map(ele,count, data,len) {
  var weed = (count / len).toFixed(1);
  var estimateRemainWorkload = [];   //实际
  var estimateWorkload = [];   //预计
  var date = [];
  data && data.map(function (val, idx) {
	  	estimateRemainWorkload.push(val.estimateRemainWorkload || 0);
	if(new Date().getTime() > new Date(val.dataTime).getTime()){
		if (idx == data.length - 1) {
	      estimateWorkload.push(0);
	    } else {
	      estimateWorkload.push((count - (weed * idx)).toFixed(1) || '');
	    }
	}
    date.push(val.dataTime || ''); 
  })
  var myChart = echarts.init(document.getElementById(ele));
  var options = {
    title: {},
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      right: 40,
      data: ['参考', '实际']
    },
    xAxis: {
      name: '日期',
      type: 'category',
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
        type: 'line',
        // stack: '剩余工作量',
        data: estimateRemainWorkload,
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
        name: '实际',
        type: 'line',
        data: estimateWorkload,
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

// 项目动态
function project_dynamic(messageInfo) {
  messageInfo.map(function (val) {
    $('#dynamic_body').append(`
      <li class="dynamic_item p_10">
        <p class="rowdiv">
          <span class="def_col_20 overHidden bold p_l_0" title="${val.messageTitle}">${val.messageTitle}</span>
          <span class="def_col_16">${val.createDate}</span>
        </p>
        <p class="p_t_0 text_ell" title="${val.messageContent || ''}">${val.messageContent || ''}</p>
      </li>
    `);
  })
}

//字典查数据
function select_dic(ele, value) {
  var _str = '';
  $(ele).find('option').each(function (idx, val) {
    if (value == val.value) {
      _str = val.innerText
    }
  })
  return _str
}

function unique(arr,name){            
  for(var i=0; i<arr.length; i++){
    for(var j=i+1; j<arr.length; j++){
      if(arr[i][name]==arr[j][name]){   
        arr.splice(j,1);
        j--;
      }
    }
  }
  return arr;
}