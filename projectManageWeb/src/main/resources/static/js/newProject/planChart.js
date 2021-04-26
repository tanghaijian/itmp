/********
 * 2019/11/14
 * niexingquan
 * 项目计划甘特图
 * *********/

var set_user_status = 1;
var approve_num = 0; //审批人数量
var saveRequired_num = 0;//保存修改次数
var is_official = 1;//版本状态   1草稿

$(() => {
  user_search('responsibleUser','responsibleUserId');
  for(let i=1;i<9;i++){
    user_search('approve_User'+i,'approve_UserId'+i);
  }
  // formValidator();
  // refactorFormValidator();
});

//人员搜索
function user_search(ipt,idEle){
  fuzzy_search_radio({
    'ele': ipt,
    'url': '/projectManage/plan/selectUserInfoVague',
    'params': {
      'userAndAccount': '',
      'ids': '',
    },
    'name': 'userName',
    'id': 'id',
    'top': '28px',
    'userId': $("#"+idEle),
  });
}

//加载数据
function loadFromLocalStorage() {
  var ret;
  // if (localStorage) {
  //   if (localStorage.getObject("teamworkGantDemo")) {
  //     ret = localStorage.getObject("teamworkGantDemo");
  //   }
  // }

  //if not found create a new example task
  if (!ret || !ret.tasks || ret.tasks.length == 0) {
	  $.ajax({
      url: "/projectManage/plan/getProjectPlan",
      type: "post",
      async : false,
      dataType: "json",
      data: {
        "projectId": $("#project_id").val(),
      },
      beforeSend: function () {
        $("#loading").css('display', 'block');
      },
      success: function (data) {
        if(data.status == 1){
          let version = !data.planNumber ? '草稿' : 'V'+ data.planNumber + '.0';
          $('#version_id').text(version);
          $('#plan_Status').val(data.planStatus);
          $('#approveRequestId').val(data.approveRequestId);

          $('#plan_version').val(data.planNumber);
          for(let i=1;i<9;i++){
            search_radio_clear('approve_User'+i,$('#approve_UserId' + i));
          }
          $('.select_user_body').hide();
          data.approveUserConfig.length && data.approveUserConfig.map((val,idx)=>{
            let i = idx + 1;
            $('.select_user_body').eq(idx).show();
            search_radio_give('approve_User'+i,val.userName,$('#approve_UserId' + i),val.userId);
          })
          if(data.planStatus == 1){
            $('#editResources').hide();
            $('#responsibleUserId').val(data.userId);
          }else{
            $('#editResources').show();
            search_radio_give('responsibleUser',data.data[0].userName,$('#responsibleUserId'),data.data[0].userId);
          }

          $('.submit_btn').hide();
          $('.update_btn').hide();
          $('#draft_status').hide();
          $('#update_status').hide();
          $('#approve_status').hide();
          $('#official_status').hide();
          $('#tab_check_List').hide();
          $('#modifying_status').hide();
          $('#modifying_status2').hide();
          saveRequired_num = 0;
          is_official = data.planStatus;
          if(data.planStatus == 1){ //草稿
            $('#draft_status').show();
            $('.submit_btn').show();
          }else if(data.planStatus == 2){ //未审批
            $('#update_status').show();
            $('.update_btn').show();
            // $('#approve_status').show();//要删
          }else if(data.planStatus == 3){ //审批中
            $('#approve_status').show();
          }else if(data.planStatus == 4){ //正式
            $('#official_status').show();
            $('#tab_check_List').show();
            $('.update_btn').eq(0).show();
          }
          ret = gantt_data(data.data);
          //actualize data
          // var offset = new Date().getTime() - ret.tasks[0].start;
          // for (var i = 0; i < ret.tasks.length; i++) {
          //     ret.tasks[i].start = ret.tasks[i].start + offset;
          // }
          ret = ret;
        }
        $("#loading").css('display', 'none');
      }
    })
//    ret = getDemoProject();
  }
 return ret;
  
}

//暂存
function saveLocalStorage() {
  var prj = ge.saveProject();
  localStorage.setObject("teamworkGantDemo", prj);
}

//切换列表视图
function to_check_List(){
	window.location.href = "/projectManageui/newProject/toPlanManage?id=" + $("#project_id").val() + "&name=" + $("#project_name").val()  + "&version=" + $("#plan_version").val();
}

//修改计划提交请求
function update_submit() {
  var prj = ge.saveProject();
  let projectId = $("#project_id").val();
  let responsibleUserId = $("#responsibleUserId").val();
  if(!responsibleUserId){
    layer.alert('责任人不能为空!',{icon:0});
    return;
  }
  let sub_arr = prj.tasks.map(function(val,idx){
	let _id = val.id;
	if(String(_id).indexOf('tmp_') != -1){
		_id = _id.split('tmp_')[1];
	}
    return {
      "id" : _id,
      "projectId" : projectId,
      "planCode" : val.code,
      "planName" : val.name,
      "planStartDate" : val.start,
      "planStartMilestone" : val.startIsMilestone,
      "planEndDate" : val.end,
      "planEndMilestone" : val.endIsMilestone,
      "planDuration" : val.duration,
      "planSchedule" : val.progress,
      "planLevel" : val.level,
      "planOrder" : idx,
      "deliverables" : val.description,//成果物
      "responsibleUserId" : responsibleUserId
    }
  })
  let sub_data = {
    "projectId": projectId,
    "projectPlanList": JSON.stringify(sub_arr),
  }
  let _url = 'updateProjectPlan';
  if($('#approveRequestId').val()){
    _url = 'updateProjectPlan2'; 
    sub_data['approveRequestId'] = $('#approveRequestId').val();
  }
  if($('#plan_Status').val() == 1){
    _url = 'insertProjectPlan';
  }
  if($('#plan_Status').val() == 2 || $('#plan_Status').val() == 4){
    if(!$('#approve_UserId1').val()){
      layer.alert('一级审批人不能为空!',{icon:0});
      return;
    }
    let approveUsers = [
      {
        userId : $('#approve_UserId1').val(),
        approveLevel : 1,
      },
    ]
    if($('#approve_UserId1').val() && $('#approve_UserId2').val()){
      approveUsers.push({
        userId : $('#approve_UserId2').val(),
        approveLevel : 2,
      })
    }
    if($('#approve_UserId1').val() && $('#approve_UserId2').val() && $('#approve_UserId3').val()){
      approveUsers.push({
        userId : $('#approve_UserId3').val(),
        approveLevel : 3,
      })
    }
    if($('#approve_UserId1').val() && $('#approve_UserId2').val()
      && $('#approve_UserId3').val() && $('#approve_UserId4').val()){
      approveUsers.push({
        userId : $('#approve_UserId4').val(),
        approveLevel : 4,
      })
    }
    if($('#approve_UserId1').val() && $('#approve_UserId2').val()
      && $('#approve_UserId3').val() && $('#approve_UserId4').val() && $('#approve_UserId5').val()){
      approveUsers.push({
        userId : $('#approve_UserId5').val(),
        approveLevel : 5,
      })
    }
    if($('#approve_UserId1').val() && $('#approve_UserId2').val()
      && $('#approve_UserId3').val() && $('#approve_UserId4').val() 
      && $('#approve_UserId5').val() && $('#approve_UserId6').val()){
      approveUsers.push({
        userId : $('#approve_UserId6').val(),
        approveLevel : 6,
      })
    }
    if($('#approve_UserId1').val() && $('#approve_UserId2').val()
      && $('#approve_UserId3').val() && $('#approve_UserId4').val() 
      && $('#approve_UserId5').val() && $('#approve_UserId6').val() && $('#approve_UserId7').val()){
      approveUsers.push({
        userId : $('#approve_UserId7').val(),
        approveLevel : 7,
      })
    }
    if($('#approve_UserId1').val() && $('#approve_UserId2').val()
      && $('#approve_UserId3').val() && $('#approve_UserId4').val() 
      && $('#approve_UserId5').val() && $('#approve_UserId6').val() 
      && $('#approve_UserId7').val() && $('#approve_UserId8').val()){
      approveUsers.push({
        userId : $('#approve_UserId8').val(),
        approveLevel : 8,
      })
    }
    sub_data['approveUsers'] = JSON.stringify(approveUsers);
    sub_data['updateMessage'] = $('#user_remark').val();
  }else{

  }
  
  $.ajax({
    url: '/projectManage/plan/' + _url,
    type: "post",
    async : false,
    dataType: "json",
    data: sub_data,
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      if(data.status == 1){
        $('#user_modal').modal('hide');
        layer.alert('提交成功',{icon:1});
        saveRequired_num = 0;
        ret = loadFromLocalStorage();
        ge.loadProject(ret);
        ge.checkpoint();
      }else{
        layer.alert(data.errorMessage,{icon:2});
      }
      $('#user_modal').modal('hide');
      $("#loading").css('display', 'none');
    }
  })
  
}

//修改操作人
function editResources(){
  set_user_status = 1;
  $('#user_tit').text('设置责任人');
  $('#user_modal').modal('show');
}

//提交
function confirm_submit(){
  set_user_status = 2;
  if($('#plan_Status').val() == 1){
    update_submit();
  }else{
    $('#user_tit').text('设置责任人和审批人');
    $('.select_user_body').hide();
    $('.select_user_body').eq(0).show();
    approve_num = 0;
    for(var i=1;i<8;i++){
      if($('#approve_UserId' + i).val()){
        approve_num = i - 1;
        $('.select_user_body').eq(approve_num).show();
      }
    }
    $('#approve_user_body').show();
    $('#user_modal').modal('show');
  }
}

//提交人员
function Submit_user(){
  // $('#user_Form').data('bootstrapValidator').validate();  
  // if( !$('#user_Form').data("bootstrapValidator").isValid() ){
  //   return;
  // } 
  if(set_user_status == 1){
    $('#user_modal').modal('hide');
  }else{
    update_submit();
  }
}

//查看历史版本
function version_logs(){
  $.ajax({
    url: '/projectManage/plan/getAllPlanNumber',
    type: "post",
    async : false,
    dataType: "json",
    data: {
      "projectId": $("#project_id").val()
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      if(data.status == 1){
        $('#show_version_logs').empty();
        data.data.length && data.data.map(val=>{
          $('#show_version_logs').append(`<li class="a_style" onclick="version_tabs(${val.projectPlanNumber})">V${val.projectPlanNumber}.0</li>`).show();
        })
      }else{
        layer.alert(data.errorMessage,{icon:2});
      }
      $("#loading").css('display', 'none');
    }
  })
}

//历史版本切换
function version_tabs(num){
  $('#plan_version').val(num);
  if($('#version').val() == num){
    loadFromLocalStorage();
  }else{
    $.ajax({
      url: '/projectManage/plan/getPlanNumberByNumber',
      type: "post",
      async : false,
      dataType: "json",
      data: {
        "projectId": $("#project_id").val(),
        "planNumber": num,
      },
      beforeSend: function () {
        $("#loading").css('display', 'block');
      },
      success: function (data) {
        if(data.status == 1){
          ret = gantt_data(data.data);
          ge.loadProject(ret);
          ge.checkpoint();
          layer.alert('版本切换成功!',{icon:1});
          $('#show_version_logs').hide();
          $('#version_id').text('V'+ num + '.0');
          $('#draft_status').hide();
          $('#update_status').hide();
        }else{
          layer.alert(data.errorMessage,{icon:2});
        }
        $("#loading").css('display', 'none');
      }
    })
  } 
}

//返回正式版本
function back_history_version(){
  $.ajax({
    url: '/projectManage/plan/getProjectPlan1',
    type: "post",
    async : false,
    dataType: "json",
    data: {
      "projectId": $("#project_id").val(),
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      if(data.status == 1){
        $('.submit_btn').hide();
        $('.update_btn').hide();
        $('.version_status').hide();
        $('#official_status').show();
        layer.alert('切换到正式版本!',{icon:1});
        ret = gantt_data(data.data);
        ge.loadProject(ret);
        ge.checkpoint();
        $('#version_id').text('V'+ data.planNumber + '.0');
      }else{
        layer.alert(data.errorMessage,{icon:2});
      }
      $("#loading").css('display', 'none');
    }
  })
}

//取消待审批修改
function off_update_approve(){
  $.ajax({
    url: '/projectManage/plan/callOffUpdate',
    type: "post",
    async : false,
    dataType: "json",
    data: {
      "projectId": $("#project_id").val(),
      "approveRequestId": $('#approveRequestId').val(),
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      if(data.status == 1){
        old_data = data.data;
        let version = !data.planNumber ? '草稿' : 'V'+ data.planNumber + '.0';
        $('#version_id').text(version);
        $('#plan_Status').val(data.planStatus);
        $('#approveRequestId').val(data.approveRequestId);
        $('#version').val(data.planNumber);
        $('.submit_btn').show(); //要删
        layer.alert('取消修改成功!',{icon:1});
        if(data.planStatus == 1){ //草稿
          $('#draft_status').show();
          $('.submit_btn').show();
        }else if(data.planStatus == 4){ //正式
          $('#official_status').show();
          $('#tab_check_List').show();
        }
        ret = loadFromLocalStorage();
        ge.loadProject(ret);
        ge.checkpoint();
        $("#loading").css('display', 'none');
      }else{
        layer.alert(data.errorMessage,{icon:2});
      }
      $("#loading").css('display', 'none');
    }
  })
}

//审批情况
function approve_log(){
  $.ajax({
    url: '/projectManage/plan/getPlanApproveRequest',
    type: "post",
    async : false,
    dataType: "json",
    data: {
      "projectId": $("#project_id").val(),
      "approveRequestId": $("#approveRequestId").val(),
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      if(data.status == 1){
        $("#check_status").text('');
        $("#check_user").text('');
        $("#check_date").text('');
        $("#check_Workload").text('');
        $("#up_before_tbody").empty();
        $("#up_after_tbody").empty();
        $("#logs_body").empty();
        $("#remark").text('');

        $("#current_userId").val(data.userId1);
        $("#approve_user_id").val(data.id);
        $("#check_status").text(data.requestLog[data.requestLog.length - 1].logType);
        $("#check_user").text(data.approveRequest.userName);
        $("#check_date").text(new Date(data.approveRequest.commitDate).toISOString().split('T')[0]);
        $("#check_Workload").text(data.approveRequest.requestReason);
        data.projectPlan.length && data.projectPlan.map((val,idx)=>{
          $('#up_before_tbody').append(`
            <tr class="rowdiv">
              <td>${idx + 1}</td>
              <td>${val.planCode}</td>
              <td>${val.planName}</td>
              <td>${val.planStartDate}~<br>${val.planEndDate}</td>
              <td>${val.responsibleUser}</td>
              <td>${val.deliverables}</td>
            </tr>
          `);
        })
        data.requestDetail.length && data.requestDetail.map((val,idx)=>{
        $('#up_after_tbody').append(`
          <tr class="rowdiv">
            <td class="">${idx + 1}</td>
            <td class="">${val.planCode || ''}</td>
            <td class="">${val.planName || ''}</td>
            <td class="">${val.planStartDate || ''}~<br>${val.planEndDate || ''}</td>
            <td class="">${val.responsibleUser || ''}</td>
            <td class="">${val.deliverables || ''}</td>
          </tr>
        `);
        })
        data.requestLog.map(val=>{
          $('#logs_body').append(`
            <div class="logDiv">
              <div class="logDiv_title">
                <span class="orderNum"></span>
                <span class="w_150_block">${val.logType}</span>&nbsp;&nbsp;&nbsp;
                <span>${val.userName}  | ${val.userAccount}</span>&nbsp;&nbsp;&nbsp;<br>
                <span style="padding-left: 21px;">${val.createDate}</span>
              </div>
              <div class="logDiv_cont">
                <div class="logDiv_contBorder">
                  <div class="">
                    <span>${val.logDetail || ''}</span>
                  </div>
                </div>
              </div>
            </div>
          `);
        })
        $('#remark').removeAttr('disabled');
        if(data.succ == 1){ //下一级
          $('.approve_btn1').show();
        }else if(data.succ == 2){ //办结
          $('.approve_btn2').show();
          $('.approve_btn1').eq(1).show();
        }else{   //无按钮权限
          $('#remark').attr('disabled',true);
        }
        $('#approve_Modal').modal('show');
      }else{
        layer.alert(data.errorMessage,{icon:2});
      }
      $("#loading").css('display', 'none');
    }
  })
}

//审批提交
function approve_flow(status){
  // if(status == 3 && !$("#remark").val()){
  //   layer.alert('请填写审批意见!',{icon:2});
  //   return;
  // }
  $.ajax({
    url: '/projectManage/plan/approve',
    type: "post",
    async : false,
    dataType: "json",
    data: {
      "projectId": $("#project_id").val(),
      "operate": status,
      "projectPlanApproveRequestId": $("#approveRequestId").val(),
      "approveSuggest": $("#remark").val(),
      "userId": $("#current_userId").val(),
      "id": $("#approve_user_id").val(),
    },
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      if(data.status == 1){
        layer.alert('审批提交成功!',{icon:1});
        ret = loadFromLocalStorage();
        ge.loadProject(ret);
        ge.checkpoint();
      }else{
        layer.alert(data.errorMessage,{icon:2});
      }
      $("#loading").css('display', 'none');
    }
  })
}

//表单校验
function formValidator() {
  $('#user_Form').bootstrapValidator({
    message: '不能为空',//通用的验证失败消息
    feedbackIcons: {
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields:{
      responsibleUser:{
        validators: {
          notEmpty: {
            message: '责任人不能为空'
          }
        }
      },
      approve_User1:{
        validators: {
          notEmpty: {
            message: '一级审批人不能为空'
          }
        }
      },
    }
  })
}

//重构表单验证
function refactorFormValidator() {
  $('#user_modal').on('hidden.bs.modal', function () {
    $("#user_Form").data('bootstrapValidator').destroy();
    $('#user_Form').data('bootstrapValidator', null);
    formValidator();
  })
}

function gantt_data(data){
  return {
    tasks:data,
    selectedRow: 2,
    deletedTaskIds: [],
    resources: [{ id: "1", name: "特朗普" }],
    canWrite: true,
    canDelete: true,
    canWriteOnParent: true,
    canAdd: true
  }
}

//添加审批人
function add_approve_user(This){
  approve_num ++;
  // if(approve_num >= 8){
  //   $(This).attr('disabled','disabled');
  // }else{
  //   $(This).removeAttr('disabled');
    $('.select_user_body').eq(approve_num).show();
  // }
}