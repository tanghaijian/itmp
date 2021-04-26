/**************
*  问题管理
*  新建类项目和项目群公用页面
*  niexingquan
*  2019-12-10
*/
$(() => {
  pageInit();
  formValidator();
  refactorFormValidator();
  downOrUpButton();
  buttonClear();
  TimeConfig('#happenDate');
  TimeConfig('#endDate');
})

/**
 * @description 表格数据加载
 * @param projectId  项目id  或  programId  项目群id
 */
function pageInit() {
  let _postData = $('#project_type').val() == 1 ? 'projectId' : 'programId';
  $("#project_list").jqGrid({
    url: $('#project_type').val() == 1 ? '/projectManage/question/getQuestions' : '/projectManage/question/getQuestionByProgram',
    datatype: 'json',
    mtype: "POST",
    height: 'auto',
    width: $(".content-table").width() * 0.999,
    postData: {
      [_postData]: $("#projectId").val()
    },
    colNames: ['id', '序号', '问题描述', '优先级', '发生日期', '结束日期', '缓解措施执行情况', '责任人', '备注', '操作'],
    colModel: [
      { name: 'id', hidden: true },
      { name: 'number', sortable: false },
      { name: 'questionDescription', sortable: false },
      { name: 'questionPriorityName', sortable: false },
      { name: 'happenDate', sortable: false },
      { name: 'endDate', sortable: false },
      { name: 'copingStrategyRecord', sortable: false },
      { name: 'responsibleUser', sortable: false },
      { name: 'remark', width: 150 , sortable: false},
      {
        name: '操作', align: "center", fixed: true, sortable: false, resize: false, search: false, width: 130,
        formatter: function (value, grid, rows, state) {
          var row = JSON.stringify(rows).replace(/"/g, '&quot;');
          var span_ = "<span>&nbsp;|&nbsp;</span>";
          var a = '<a class="a_style" onclick="addProject(2,' + rows.id + ')">编辑</a>';
          var b = '<a class="a_style" onclick="addProject(3,' + rows.id + ')">查看</a>';
          var c = '<a class="a_style" onclick="_delete(' + rows.id + ')">删除</a>';
          var opt_status = [];
          opt_status.push(a);
          opt_status.push(b);
          opt_status.push(c);
          return opt_status.join(span_);
        }
      },
    ],
    rowNum: -1,
//    rowTotal: 200,
//    rowList: [10, 20, 30],
    rownumWidth: 30,
    sortable: false,
    loadtext: "数据加载中......",
    viewrecords: true, //是否要显示总记录数 
    jsonReader: {
      repeatitems: false,
      root: "data",
    },
    gridComplete: function () {
    },
    beforeRequest: function () {
      $("#loading").css('display', 'block');
    },
    loadComplete: function () {
      $("#loading").css('display', 'none');
    }
  }).trigger("reloadGrid");
}

/**
 * @description 清空value
 */
function _clear_val(parent) {
  $(parent).find('input').each(function (idx, val) {
    $(val).val('').removeAttr('disabled').removeClass('bor_none shadow_none');
    $(val).parent().parent().find('.redStar').removeClass('_hide');
  })
  $(parent).find('textarea').each(function (idx, val) {
    $(val).val('').css({height:'unset'}).removeAttr('disabled').removeClass('bor_none shadow_none');
    $(val).parent().parent().find('.redStar').removeClass('_hide');
    $(val).removeClass('ov_hidden');
  })
  $(parent).find('select').each(function (idx, val) {
    $(val).val('');
    $(val).parent().parent().find('.bootstrap-select').removeClass('shadow_none');
    $(val).parent().parent().find('.dropdown-toggle').removeClass('bor_none').children().removeClass('col_black');;
    $(val).parent().parent().find('.dropdown-toggle').removeAttr('disabled');
    $(val).parent().parent().find('.bs-caret').removeClass('_hide');
    $(val).parent().parent().parent().find('.redStar').removeClass('_hide');
  })
  $('.selectpicker').selectpicker('refresh');
  $('#add_modal').find('.modal-dialog').removeClass('_Max_dialog');
  $('#add_modal').find('.modal-body').removeClass('bg_f2').find('.check_tit').hide();
  $('#add_modal').find('.modal-body').children().eq(0).removeClass('def_col_23').addClass('def_col_36').siblings().hide();
  $('#add_modal').find('.modal-footer').children().eq(1).text('取消');
  $('#add_modal').find('.modal-footer').children().eq(0).show();
  $("#add_tit").text('新建问题记录');
  $("#edit_id").val('');
}

/**
 * @description 新建,编辑,查看
 * @param id
 */
function addProject(type, ID) {
  _clear_val('#add_form');
  if (type == 2 || type == 3) {   //2编辑      3查看
    $.ajax({
      type: "post",
      url: "/projectManage/question/getQuestionById",
      dataType: "json",
      data: {
        id: ID
      },
      beforeSend: function () {
        $("#loading").css('display', 'block');
      },
      success: function (result) {
        var data = result.data;
        if (result.status == 1) {
          if (type == 2 || type == 3) {
            $("#add_tit").text('编辑问题记录');
            $("#edit_id").val(data.id);
            for (var key in data) {
              $('#add_form').find('input').each(function (idx, val) {
                var name = $(val).attr("name");
                if (key == name) {
                  $(val).val(data[key]);
                }
                if (type == 3) {
                  $(val).addClass('bor_none shadow_none').attr({ 'placeholder': '', 'disabled': 'disabled' });
                  $(val).parent().parent().find('.redStar').addClass('_hide');
                }
              })
              $('#add_form').find('select').each(function (idx, val) {
                let name = $(val).attr("name");
                if (key == name) {           //编辑
                  $(val).val(data[key]);
                  $('.selectpicker').selectpicker('refresh');
                }
                if (type == 3) {                  //查看
                  $(val).parent().parent().find('.bootstrap-select').addClass('shadow_none');
                  $(val).parent().parent().find('.dropdown-toggle').addClass('bor_none').attr('disabled', 'disabled').children().addClass('col_black');
                  $(val).parent().parent().find('.bs-caret').addClass('_hide');
                  $(val).parent().parent().parent().find('.redStar').addClass('_hide');
                  let _html = $(val).parent().parent().find('.dropdown-toggle').html().replace('请选择', '');
                  $(val).parent().parent().find('.dropdown-toggle').html(_html);
                }
              })
              $('#add_form').find('textarea').each(function (idx, val) {
                let name = $(val).attr("name");
                if (key == name && type == 2) {
                  $(val).val(data[key]);
                }
                if (key == name && type == 3) {
                  let len = (data[key].split('\n')).length;
                  $(val).val(data[key]).css({height:len * 30 +'px'});
                  $(val).addClass('bor_none shadow_none p_t_0').attr({ 'placeholder': '', 'disabled': 'disabled' });
                  $(val).parent().parent().find('.redStar').addClass('_hide');
                }
              })
            }
             $('#responsibleUserName').val(data['responsibleUser']);
          }
          if (type == 3) {    //查看
            var logs = result.logs;
            $("#add_tit").text('查看问题记录');
            $('#add_modal').find('.modal-dialog').addClass('_Max_dialog');
            $('#add_modal').find('.modal-body').addClass('bg_f2').find('.check_tit').show();
            $('#add_modal').find('.modal-body').children().eq(0).removeClass('def_col_36').addClass('def_col_23').siblings().show();
            $('#add_modal').find('.modal-footer').children().eq(1).text('关闭');
            $('#add_modal').find('.modal-footer').children().eq(0).hide();
            $('#logs_body').empty();
            $('.btn_clear').hide();
            $('textarea').addClass('ov_hidden');
            logs.length && logs.map(function (val, idx) {
              let _className = val.logType == '新增问题' ?  '' : 'logDiv_contRemark';
              $('#logs_body').append(`
                  <div class="logDiv">
                    <div class="logDiv_title">
                      <span class="orderNum"></span>
                      <span>${val.logType}</span>&nbsp;&nbsp;&nbsp;
                      <span>${val.userName}  | ${val.userAccount}</span>&nbsp;&nbsp;&nbsp;
                      <span>${val.createDate}</span>
                    </div>
                    <div class="logDiv_cont">
                      <div class="logDiv_contBorder">
                        <div class="${_className}">
                          <span>${val.logDetail || ''}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                `);
            })
          }
          $("#loading").css('display', 'none');
          $("#add_modal").modal('show');
        } if (result.status == 2) {
          layer.alert(result.errorMessage, { icon: 2 });
        }
      }
    });
  }
  if (!type) {
    $('#responsibleUserId').val($('#_userId').val());
    $('#responsibleUserName').val($('#_userName').val());
    $("#add_modal").modal('show');
  }
}

/**
 * @description 新增,编辑提交
 */
function add_submit() {
  $('#add_form').data('bootstrapValidator').validate();
  if (!$('#add_form').data("bootstrapValidator").isValid()) {
    return;
  }
  var submit_data = {};
  $('#add_form').find('input').each(function (idx, val) {
    var id = $(val).attr("name");
    submit_data[id] = $.trim($(val).val());
  })
  $('#add_form').find('select').each(function (idx, val) {
    var id = $(val).attr("name");
    submit_data[id] = $(val).val();
  })
  $('#add_form').find('textarea').each(function (idx, val) {
    var id = $(val).attr("name");
    submit_data[id] = $(val).val();
  })
  if ($("#edit_id").val()) {
    submit_data['id'] = $("#edit_id").val();
  } else {
    if($('#project_type').val() == 1){
      submit_data['projectId'] = $("#projectId").val();
    }else{
      submit_data['programId'] = $("#projectId").val();
    }
  }
  delete submit_data.responsibleUserName;
  $.ajax({
    type: "post",
    url: $("#edit_id").val() ? "/projectManage/question/updateQuestion" : "/projectManage/question/insertQuestion",
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(submit_data),
    beforeSend: function () {
      $("#loading").css('display', 'block');
    },
    success: function (data) {
      if (data.status == 1) {
        layer.alert("提交成功", { icon: 1 });
        $("#add_modal").modal("hide");
        $("#project_list").trigger("reloadGrid");
      } if (data.status == 2) {
        layer.alert("提交失败", { icon: 2 });
      }
      $("#loading").css('display', 'none');
    }
  });
}

/**
 * @description 删除记录
 * @param id
 */
function _delete(ID) {
  layer.confirm('确定要删除吗?', {
    btn: ['确定', '取消'],
    icon:2,
    title: "提示"
  }, function () {
    $.ajax({
      type: "post",
      url: "/projectManage/question/deleteQuestion",
      dataType: "json",
      data: {
        id: ID
      },
      beforeSend: function () {
        $("#loading").css('display', 'block');
      },
      success: function (data) {
        if (data.status == 1) {
          layer.alert("删除成功", { icon: 1 });
          $("#project_list").trigger("reloadGrid");
        } if (data.status == 2) {
          layer.alert("删除失败", { icon: 2 });
        }
        $("#loading").css('display', 'none');
      }
    });
  })
}

/**
 * @description 表单校验
 */
function formValidator() {
  let form_arr = ['questionDescription', 'questionReasonType', 'questionImportance', 'questionEmergencyLevel', 'questionPriority', 'happenDate', 'endDate', 'happenStage'];
  $('#add_form').bootstrapValidator({
    message: '此项不能为空',//通用的验证失败消息
    feedbackIcons: {
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      questionDescription: {
        validators: {
          notEmpty: {
            message: '此项不能为空'
          }
        }
      },
      questionReasonType: {
        validators: {
          notEmpty: {
            message: '此项不能为空'
          }
        }
      },
      questionImportance: {
        validators: {
          notEmpty: {
            message: '此项不能为空'
          }
        }
      },
      questionEmergencyLevel: {
        validators: {
          notEmpty: {
            message: '此项不能为空'
          }
        }
      },
      questionPriority: {
        validators: {
          notEmpty: {
            message: '此项不能为空'
          }
        }
      },
      happenDate: {
        trigger:'change',
        validators: {
          notEmpty: {
            message: '此项不能为空'
          }
        }
      },
      endDate: {
        trigger:'change',
        validators: {
          notEmpty: {
            message: '此项不能为空'
          }
        }
      },
      happenStage: {
        validators: {
          notEmpty: {
            message: '此项不能为空'
          }
        }
      },
    }
  })
}

/**
 * @description 重构表单验证
 */
function refactorFormValidator() {
  $('#add_modal').on('hidden.bs.modal', function () {
    $("#add_form").data('bootstrapValidator').destroy();
    $('#add_form').data('bootstrapValidator', null);
    formValidator();
  })
}

/**
 * @description 人员弹窗
 * @param userName    姓名
 * @param companyName  公司
 * @param deptName    部门
 */
function userTableShowAll() {
  $("#userTable").bootstrapTable('destroy');
  $("#userTable").bootstrapTable({
    url: "/system/user/getAllUserModal2",
    method: "post",
    queryParamsType: "",
    pagination: true,
    sidePagination: "server",
    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
    pageNumber: 1,
    pageSize: 10,
    pageList: [5, 10, 15],
    singleSelect: true,//单选
    queryParams: function (params) {
      var param = {
        userName: $.trim($("#userName").val()),
        companyName: $("#companyName").val(),
        deptName: $("#deptName").val(),
        pageNumber: params.pageNumber,
        pageSize: params.pageSize,
      }
      return param;
    },
    columns: [{
      checkbox: true,
      width: "30px"
    }, {
      field: "id",
      title: "id",
      visible: false,
      align: 'center'
    }, {
      field: "userName",
      title: "姓名",
      align: 'center'
    }, {
      field: "userAccount",
      title: "用户名",
      align: 'center'
    }, {
      field: "companyName",
      title: "所属公司",
      align: 'center'
    }, {
      field: "deptName",
      title: "所属处室",
      align: 'center'
    }],
    onLoadSuccess: function () {
      $("#userModal").modal('show');
    },
    onLoadError: function () {

    },
  });
}

/**
 * @description 清空人员搜索
 */
function clearSearchUser() {
  $("#userName").val('');
  $("#deptName").val('');
  $("#companyName").val('');
}

/**
 * @description 选择人员
 */
function commitUser() {
  var selectContent = $("#userTable").bootstrapTable('getSelections')[0];
  if (typeof (selectContent) == 'undefined') {
    layer.alert('请选择一列数据！', { icon: 0 });
    return false;
  } else {
    $("#responsibleUserId").val(selectContent.id);
    $("#responsibleUserName").val(selectContent.userName).change();
    $("#userModal").modal("hide");
  }
}

/**
 * @description 时间配置
 */
function TimeConfig(ele,startDate){
  $(ele).datetimepicker({
    minView:"month",
    format: "yyyy-mm-dd",
    autoclose: true,
    todayBtn: true,
    language: 'zh-CN',
    startDate:startDate,
    pickerPosition: "top-left"
  }).on('changeDate', function(e){
    if(e.currentTarget.id == 'happenDate'){
      $('#endDate').val('').change();
      $('#endDate').datetimepicker('remove');
      TimeConfig('#endDate',e.target.value);
    }
    $(ele).val(e.target.value);
  });
}