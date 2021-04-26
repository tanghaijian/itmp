/**
 * 项目管理-人员管理主页js
 */
var insert_arr = [];//设置项目组
$(function () {
  pageInit();
  tab_project_group();
  formValidator();
  refactorFormValidator();
  $("#downBtn").click(function () { //搜索展开和收起
    if ($(this).children("span").hasClass("fa-caret-up")) {
      $(this).children("span").removeClass("fa-caret-up");
      $(this).children("span").addClass("fa-sort-desc");
      $("#search_div").slideUp(200);
    } else {
      $(this).children("span").removeClass("fa-sort-desc");
      $(this).children("span").addClass("fa-caret-up");
      $("#search_div").slideDown(200);
    }

  });
  
  //搜索条件：公司  autocomplete组装
  var company = {
    'ele': 'company',
    'url': '/projectManage/personnelManagement/selectCompanyVague',
    'params': {
      'companyName': ''
    },
    'name': 'companyName',
    'id': 'id',
    'top': '26px',
    'userId': $('#company_id'),
    'earth': true
  };
  fuzzy_search_radio(company);
  
  //搜索条件：部门  autocomplete组装
  var department = {
    'ele': 'department',
    'url': '/projectManage/personnelManagement/selectDeptVague',
    'params': {
      'deptName': ''
    },
    'name': 'deptName',
    'id': 'id',
    'top': '26px',
    'userId': $('#department_id'),
    'earth': true
  };
  fuzzy_search_radio(department);
  //搜索条件：项目名称  autocomplete组装
  var project_name = {
    'ele': 'project_name',
    'url': '/projectManage/personnelManagement/selectProjectNameVague',
    'params': {
      'projectName': ''
    },
    'name': 'projectName',
    'id': 'id',
    'top': '26px',
    'userId': $('#project_name_id'),
    'earth': true
  };
  fuzzy_search_radio(project_name);
  
  //搜索条件：项目编号  autocomplete组装
  var project_number = {
    'ele': 'project_number',
    'url': '/projectManage/personnelManagement/selectProjectCodeVague',
    'params': {
      'projectCode': '',
    },
    'name': 'projectCode',
    'id': 'id',
    'top': '26px',
    'userId': $('#project_number_id'),
    'earth': true
  };
  fuzzy_search_radio(project_number);

  //搜索   用户名autocomplete组装
  var UserInfo = {
    'ele': 'UserInfo',
    'url': '/projectManage/personnelManagement/selectUserInfoVague',
    'params': {
      'userAndAccount': '',
      'ids': '',
      //'pageNumber':1,
      //'pageSize':10
    },
    'name': 'userName',
    'id': 'id',
    'top': '40px',
    'userId': $('#UserInfo_id'),
  };
  fuzzy_search_checkbox(UserInfo);
  
  $('#UserInfo_id').change(function(){
      if($('#UserInfo_id').val()){
          $('#UserInfo_id').parent().removeClass('has-error');
          $('#UserInfo_id').parent().find('small').addClass('_hide');
      }else{
          $('#UserInfo_id').parent().addClass('has-error');
          $('#UserInfo_id').parent().find('small').removeClass('_hide');
          return;
      }
  })
})

function pageInit() {
  $("#loading").css('display', 'block');
  $("#list2").jqGrid({
    url: '/projectManage/personnelManagement/getAllUserProject',
    datatype: "json",
    height: 'auto',
    mtype: "post",
    width: $(".content-table").width() * 0.999,
    colNames: ['id', '姓名', '用户名', '所属公司', '所属部门', '项目组', '操作'],
    colModel: [
      { name: 'id', index: 'id', hidden: true, sorttype: 'integer' },
      { name: 'userName', index: 'userName', align: "left" },
      { name: 'userAccount', index: 'userAccount' },
      { name: 'companyName', index: 'companyName' },
      { name: 'deptName', index: 'funCount' },
      {
        name: 'projectName', index: 'projectName',
        formatter: function (value, grid, rows, state) {
          var row = JSON.stringify(rows).replace(/"/g, '&quot;');
          var functionCount = getFunctionCount(rows.id);
          return functionCount || '';
        }
      },
      {
        name: '操作',
        index: '操作',
        fixed: true,
        align: "center",
        sortable: false,
        resize: false,
        search: false,
        width: 100,
        formatter: function (value, grid, rows, state) {
          var _arr = []; _arr.push(rows);
          var row = JSON.stringify(_arr).replace(/"/g, '&quot;');
          return '<a href="javascript:void(0);" onclick="toEditReq(' + row + ');">设置项目组</a>';
        }
      }
    ],
    rowNum: 10,
    rowTotal: 10,
    rowList: [10, 20, 30],
    rownumWidth: 40,
    pager: '#pager2',
    sortable: true,   //是否可排序
    sortorder: 'asc',
    sortname: 'id',
    loadtext: "数据加载中......",
    viewrecords: true, //是否要显示总记录数
    multiselect: true,
    loadComplete: function () {
      $("#loading").css('display', 'none');
    },
    beforeRequest: function () {
      $("#loading").css('display', 'block');
    }
  }).trigger("reloadGrid");
}

//组装用户所在的项目组，将多个项目组名称合并成一行显示（不建议每行都访问一次后台，应该到后台进行组装后统一返回前端）
function getFunctionCount(id) {
  var functionCount;
  $.ajax({
    type: "POST",
    url: "/projectManage/personnelManagement/getProjectInfoByUser",
    dataType: "json",
    data: {
      "userId": id
    },
    async: false,
    success: function (data1) {
      functionCount = data1["projectNames"];
    }
  });
  return functionCount;
}


//设置项目组，选择项目组后带出项目小组
function tab_project_group() {
  $('#projectGroup').change(function () {
    if ($(this).val()) {
      $('#group').empty();
      $.ajax({
        type: 'POST',
        url: '/projectManage/personnelManagement/getProjectGroupByProjectId',
        data: {
          'projectId': $(this).val()
        },
        dataType: 'json',
        success: function (result) {
          // if($('#projectGroup').val() == 0){
          //   console.log(2222)
          // }
          if (result.projectGroup.length) {
            $('select#group').append(`<option value="">查看小组</option>`);
            result.projectGroup.map(function (val, idx) {
              $('select#group').append(`<option value="${val.id}" disabled="disabled">${val.projectGroupName}</option>`);
            })
            $('.selectpicker').selectpicker('refresh');
          } else {
            $('#group').empty();
            $('.selectpicker').selectpicker('refresh');
          }
          $('#group_control').show();
          $('#add_btn').show();
        }
      });
    } else {
      $('#group').empty();
      $('#group_control').hide('show');
      $('#add_btn').hide('show');
    }
  })
}

// 批量设置
function setGroup() {
  var ids = $('#list2').jqGrid('getGridParam', 'selarrrow');
  if (ids.length == 0) {
    layer.alert("请至少选择一项", { icon: 0, title: '提示信息' });
    return false;
  }
  $.ajax({
    type: 'POST',
    url: '/projectManage/personnelManagement/getAllProject',
    dataType: 'json',
    success: function (result) {
      $('#station').val('');
      $('#group').val('');
      $('#projectGroup').val('');
      $('#projectGroup').empty();
      search_checkbox_clear('UserInfo', $('#UserInfo_id'));
      var _arr = ids.map(function (val, idx) {
        return $("#list2").jqGrid("getRowData", val);
      })
      search_checkbox_give('UserInfo', $('#UserInfo_id'), _arr, 'userName', 'id');
      if (result.tblProjectInfo.length) {
        $('#projectGroup').append(`<option value="">请选择</option>`);
        $('#projectGroup').append(`<option value="0">全选</option>`);
        result.tblProjectInfo.map(function (val, idx) {
          $('#projectGroup').append(`<option value="${val.id}">${val.projectName}</option>`);
        })
        $('.selectpicker').selectpicker('refresh');
      } else {
        layer.alert('项目组加载失败!', { title: "提示信息", icon: 2 });
      }
      $('#add').modal('show');
    }
  });
}

//列表末尾的设置项目组链接
function toEditReq(list) {
  $("#memberGroup").empty();
  $('#station').val('');
  $('#group').val('');
  $('#projectGroup').val('');
    $('#projectGroup').empty();
  
    //清空多选框
  search_checkbox_clear('UserInfo', $('#UserInfo_id'));
  //重新赋值多选框
  search_checkbox_give('UserInfo', $('#UserInfo_id'), list, 'userName', 'id');
  $.ajax({
    type: 'POST',
    url: '/projectManage/personnelManagement/getAllProject',
    dataType: 'json',
    contentType: "application/json",
    success: function (result) {
      if (result.tblProjectInfo.length) {
          $('#projectGroup').append(`<option value="">请选择</option>`);
          $('#projectGroup').append(`<option value="0">全选</option>`);
        result.tblProjectInfo.map(function (val, idx) {
          $('#projectGroup').append(`<option value="${val.id}">${val.projectName}</option>`);
        })
        $('.selectpicker').selectpicker('refresh');
      } else {
        layer.alert('项目组加载失败!', { title: "提示信息", icon: 2 });
      }
      $('#add').modal('show');
    }
  });
}

//添加项目组列表
function add(){
	var _add_flag = true;
  $("#addForm").data('bootstrapValidator').validate();
  if ( !$("#addForm").data('bootstrapValidator').isValid() ) {
    return;
  }
  if($('#station').val() && $('#projectGroup').val()){
    var station =  $('#station').prev().prev().children('.filter-option').text();
    var projectGroup =  $('#projectGroup').prev().prev().children('.filter-option').text();
    if(insert_arr.length){
      insert_arr.map(function(val,idx){
        if(val.userPost == $('#station').val() && val.projectId == $('#projectGroup').val()){
          layer.alert('不能重复添加!',{icon:1,title:'提示信息'})
          _add_flag = false;
        }
      })
    }
    if(_add_flag){
      var _u  = $('#UserInfo_id').val().split(',');
      for (var i = 0; i < _u.length; i++) {
        _u[i] = _u[i].replace('"', '');
      }
      var _group =  '';
      $('#group').children('option').each(function(idx,val){
      	if($(val).text() !== '查看小组'){
      		_group += $(val).text() + ',';
      	}
      })
      _group = _group.substr(0, _group.length - 1)
      if($('#projectGroup').val() == 0){
        $('#group_list').empty();
        insert_arr.push({
          'userIds' : _u,
          'userPost' : $('#station').val(), 
          'projectId' : 0, 
        })
        $.ajax({
		    type: 'POST',
		    url: '/projectManage/personnelManagement/previewProject',
		    dataType: 'json',
		    data:{
		    	'projectId' : 0
		    },
		    contentType: "application/json",
		    success: function (result) {
		        result.groupList.map(function (val, idx) {
		        	$('#group_list').append(`
	                    <div class="rowdiv">
	                      <div class="col-md-4 font_center control-label">${station}</div>
	                      <div class="col-md-4 font_center control-label">${val.projectName}</div>
	             		  <div class="col-md-4 font_center control-label">${val.projectGroupName || ''}</div>
	                    </div>
	                `);
		        })
		    }
		});
//        $('#projectGroup').children('option').each(function(idx,val){
//        	if($(val).val() && $(val).text() !== '全选'){
//        		var  _group_str =  _str_render($('#group').children('option').eq(idx).text());
////        		var  _group_str = $('#group').children('option').eq(idx).text() == '查看小组' ? $('#group').children('option').eq(idx).text() : '';
//        	 $('#group_list').append(`
//               <div class="rowdiv">
//                 <div class="col-md-4 font_center control-label">${station}</div>
//                 <div class="col-md-4 font_center control-label">${$(val).text()}</div>
//        		 <div class="col-md-4 font_center control-label">${222}</div>
//               </div>
//             `);
//        	}
//        })
        // var projectGroup =  $('#projectGroup').children('option').text();
        // console.log(projectGroup,22)
        
      }else{
        insert_arr.push({
          'userIds' : _u,
          'userPost' : $('#station').val(), 
          'projectId' : $('#projectGroup').val(), 
        })
        $('#group_list').append(`
          <div class="rowdiv">
            <div class="col-md-4 font_center control-label">${station}</div>
            <div class="col-md-4 font_center control-label">${projectGroup}</div>
            <div class="col-md-4 font_center control-label" title="${_group}">${_group}</div>
          </div>
        `);
      }
    }
    
    $('#group_list_toggle').show();
    $('#group_list').show();
  }
}

function _str_render(val){
	if(val == '查看小组'){
		return ''
	}else{
		return val
	}
}

function addSubmit() {
    if($('#UserInfo_id').val()){
        $('#UserInfo_id').parent().removeClass('has-error');
        $('#UserInfo_id').parent().find('small').addClass('_hide');
    }else{
        $('#UserInfo_id').parent().addClass('has-error');
        $('#UserInfo_id').parent().find('small').removeClass('_hide');
        return;
    }
  $("#addForm").data('bootstrapValidator').validate();
  if ( !$("#addForm").data('bootstrapValidator').isValid() ) {
    return;
  }
  if(insert_arr.length){
      var last_id = insert_arr[insert_arr.length-1]['userIds'];
      insert_arr.map(function(val,idx){
          val['userIds'] = last_id
      })
  }else{
    layer.alert('请点击添加',{icon:2,title:'提示信息'})
      return

  }

  $.ajax({
    type: "POST",
    url: '/projectManage/personnelManagement/insertProjectGroupUser',
    dataType: "json",
    data: {
    	'tblProjectGroupUser' : JSON.stringify(insert_arr)
    },
    success: function (result) {
      if (result.status == 1) {
        layer.alert(
          '保存成功!',
          { title: "提示信息", icon: 1 },
          function () {
            layer.closeAll("dialog");
            $('.modal-backdrop').hide();
            $("#list2").trigger("reloadGrid");
          }
        );
      } else {
        layer.alert('保存失败!', { title: "提示信息", icon: 2 });
      }
      $('#add').modal('hide');
    }
  });
}

//添加下拉选项
function addSelectOptions(formdict, idTypeEle) {
  idTypeEle && Object.values(formdict).map(function (val, idx) {
    $(`#${idTypeEle}`).append(`<option value="${idx}">${val}</option>`);
  })

}

function formValidator() {
  $('#addForm').bootstrapValidator({
    //excluded: [":disabled"],
    //message: 'This value is not valid',//通用的验证失败消息
    feedbackIcons: {//根据验证结果显示的各种图标
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      station: {
        trigger: "change",
        validators: {
          notEmpty: {
            message: '岗位不能为空'
          },
        }
      },
      projectGroup: {
        validators: {
          notEmpty: {
            message: '项目组不能为空'
          },
          callback: {
            message: '请选择项目组',
            callback: function (value, validator) {
              if (value == '') {
                return false;
              } else {
                return true;
              }
            }
          }
        }
      },
      // group: {
      //   trigger: "change",
      //   validators: {
      //     notEmpty: {
      //       message: '小组不能为空'
      //     },
      //   }
      // },

    }
  });
}

function refactorFormValidator(){
  $('#add').on('hidden.bs.modal', function() {
    $("#addForm").data('bootstrapValidator').destroy();
    $('#addForm').data('bootstrapValidator', null);
    formValidator();
    $('.modal-backdrop').hide();
    $('#group_control').hide();
    $('#group_list').empty().hide();
    $('#group_list_toggle').hide();
    insert_arr = [];
  });
}

function search() {
  var searchData = {
    'userName': $.trim($('#name').val()),
    'userAccount': $.trim($('#userName').val()),
    'companyId': $.trim($('#company_id').val()),
    'deptId': $.trim($('#department_id').val()),
    'projectId': $.trim($('#project_name_id').val()),
      'projectCode': $.trim($('#project_number').val()),
  }
  $("#loading").css('display','block');
	$("#list2").jqGrid('setGridParam',{
		url: "/projectManage/personnelManagement/getAllUserProject",
		postData:{
			"search":JSON.stringify(searchData)
		},
		loadComplete :function(){
			$("#loading").css('display','none');		
		},
		page:1,
	}).setGridParam({datatype:'json'}).trigger("reloadGrid", { fromServer: true});//
}

//重置
function _reset() {
  _Clear('_ClearVal');
}

//清空
function _Clear(_input) {
  $(`.${_input}`).each(function (idx, val) {
    $(val).val("");
  })
  $(".btn_clear").css("display", "none");
}

