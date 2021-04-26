/*****
 * 2020-01-16
 * niexingquan
 * 该js前端页面引用已注释，移至docPermission/docPermission.js
 * ******/
var selectUserIds = [];
var current_id = '';

var is_set_auth = false;

$(() => {
  //权限设置（按目录）
  $("#listsPower").on("click", function () {
    $(".headRight>.btn").css("display", "none");
    $("#cancel_btn").css("display", "inline-block");
    $("#return").css("display", "inline-block");
    
    $(".headTitle").hide();
    $(".headTitle_assco").show();
    $('.notRequirement').hide();
    $('.powerDiv_List').show();
    is_set_auth = true;
    if(is_set_auth){
	    var selectedNodes = $.fn.zTree.getZTreeObj("menuTree").getSelectedNodes();
	    current_id = selectedNodes[0].id;
	    get_User_Authorities(current_id);
	    get_All_post(current_id);
    }
  })
  $('#select_all_user').on("click", function () {
	  userTableShowAll();
  })
  $('#select_all_post').on("click", function () {
	  post_TableShowAll();
  })
  $('#cancel_btn').on("click", function () {
	  $(".headTitle").show();
      $(".headTitle_assco").hide();
      
      $('.notRequirement').show();
      $('.powerDiv_List').hide();
      
      $(".headRight>.btn").css("display", "inline-block");
      $("#cancel_btn").css("display", "none");
      $("#return").css("display", "none");
  })
  
  //提交文档权限
    $("#return").on("click", function () {
    	set_user_auth();
    	$(".headTitle").show();
        $(".headTitle_assco").hide();
//    	is_set_auth = false;
//        $(".headRight>.btn").css("display", "inline-block");
//        $("#return").css("display", "none");
//        $('.notRequirement').show();
//        $('.powerDiv_List').hide();
//        $(".headTitle").text(parameterArr.obj.name);
    })
})

/*================================按目录       */

//查询人员权限
function get_User_Authorities() {
  $("#loading").css('display', 'block');
  $.ajax({
    url: "/projectManage/assetLibrary/directoryAuthority/getUserAuthorities",
    type: "post",
    dataType: "json",
    data: {
      directoryId: current_id,  //目录id
      // pageNumber: 1,
      // pageSize: 10
    },
    success: function (d) {
      let arr = [];
      d.rows.map(val=>{
        d.otherData.map(v=>{
          if(val.userId == v.id){
            arr.push({
              id : val.id,
              userName : v.userName,
              userId : val.userId,
              userAccount : v.userAccount,
              deptName : v.deptName,
              systemDirectoryId : val.systemDirectoryId,
              readAuth : val.readAuth,
              writeAuth : val.writeAuth,
            });
          }
        })
      })
      create_user_table(arr);
    },
    error: function (data) {
    }
  })
}

function create_user_table(data) {
  //  $("#loading").css('display', 'block');
  $("#user_Authoritie_list").bootstrapTable('destroy');
  $("#user_Authoritie_list").bootstrapTable({
    data: data,
    method: "post",
    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
    queryParamsType: "",
    pagination: false,
    sidePagination: "server",
    singleSelect: false,
    pageNumber: 1,
    pageSize: 10,
    pageList: [5, 10, 15],
    columns: [{
      field: "id",
      title: "id",
      visible: false,
      align: 'center'
    }, {
      field: "userAccount",
      title: "姓名账号",
      align: 'left',
      formatter: function (value, row, index) {
    	  return `<span class="" id="">${row.userName}</span>&nbsp;&nbsp; | &nbsp;&nbsp;<span class="" id="">${row.userAccount}</span>`;
      }
    }, {
      field: "deptName",
      title: "所属部门",
      align: 'center',
    }, {
      field: "writeAuth",
      title: "权限",
      align: 'center',
      class:'auth_userPostId',
      formatter: function (value, row, index) {
        let readAuth = '';
        if (row.readAuth && row.readAuth == 1) {
          readAuth = 'checked';
        }
        let writeAuth = '';
        if (row.writeAuth && row.writeAuth == 1) {
          writeAuth = 'checked';
        }
        return `
          <input class="readAuth" id="${row.id}" systemDirectoryId="${row.systemDirectoryId}" userId="${row.userId}" type="checkbox" ${readAuth}/>
          <span class="" id="">读</span>
          <input class="writeAuth" userPostId="${row.userPostId}" type="checkbox" ${writeAuth}/>
          <span class="" id="">写</span>
        `;
      }
    }, {
      field: "opt",
      title: "操作",
      align: 'center',
      formatter: function (value, row, index) {
        let arr = [];
        arr.push({
          id : row.id,
          systemDirectoryId : row.systemDirectoryId,
          userId : row.userId,
        })
        return `<a class="a_style" onclick='del_user_Authorities(${JSON.stringify(arr)})'>移除</a>`;
      }
    }],
    onLoadSuccess: function (data) {
      $("#loading").css('display', 'none');
    },
    onLoadError: function () {
      $("#loading").css('display', 'none');
      layer.alert("系统内部错误!", {
        icon: 2,
        title: "提示信息"
      });
    },
    onPageChange: function () {
      $("#loading").css('display', 'block');
    }
  });
  $("#loading").css('display', 'none');
}

//获取所有角色
function get_All_post() {
	console.log(parameterArr.obj.id)
  $("#loading").css('display', 'block');
  $.ajax({
    url: "/projectManage/oamproject/getProjectUserPost",
    type: "post",
    dataType: "json",
    data: {
      projectId: parameterArr.obj.id  //项目id
    },
    success: function (data) {
      if (data.status == 1) { 
        get_Post_Authorities(data.userPosts);
      }
    },
    error: function (data) {
    }
  })
}

//查询角色权限 
function get_Post_Authorities(userPosts) {
	$("#loading").css('display', 'block');
  $.ajax({
    url: "/projectManage/assetLibrary/directoryAuthority/getPostAuthorities",
    type: "post",
    dataType: "json",
//    contentType: "application/json",
    data: {
      directoryId: current_id
    },
    success: function (data) {
//      if (data.status == 1) {
        // if (!data.rows.length) return;
        let _arr = [];
        userPosts.map(val=>{
          data.rows.map(v=>{
            if(val.userPostId == v.userPost){
              _arr.push({
                id:v.id,
                systemDirectoryId:v.systemDirectoryId,
                readAuth:v.readAuth,
                writeAuth:v.writeAuth,
                userPostId:val.userPostId,
                userPostName:val.userPostName,
              });
            }
          })
        })
        /* let temp_arr = [];
        $("#post_Authoritie_list > tbody").find('.auth_userPostId').each((idx,val) => {
          data.rows.map(v => {
            if ($(val).find('.readAuth').attr('userPostId') == v.userPost) {
              $(val).find('.readAuth').attr({
                'id':v.id,
                'systemDirectoryId':v.systemDirectoryId,
              });
              if(v.readAuth && v.readAuth == 1){
                $(val).find('.readAuth').attr({
                  'checked':true,
                });              
              }
              if(v.writeAuth && v.writeAuth == 1){
                $(val).find('.writeAuth').attr({
                  'checked':true,
                });              
              }
            }
          })
        })
        $("#loading").css('display', 'block'); */
        $("#post_Authoritie_list").bootstrapTable('destroy');
        $("#post_Authoritie_list").bootstrapTable({
          // url: "/projectManage/oamproject/getProjectUserPost",
          data:_arr,
          method: "post",
          queryParamsType: "",
          pagination: true,
          sidePagination: "server",
          contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
          pageNumber: 1,
          pageSize: 10,
          pageList: [5, 10, 15],
          //singleSelect : true,//单选
          singleSelect: false,
          // queryParams: function (params) {
          //   var param = {
          //     projectId: parameterArr.obj.id,  //项目id
          //     userPostIds : ""
          //   }
          //   return param;
          // },
          // responseHandler: function (res) {
          //   return{    
          //     "total":res._arr.length,
          //     "rows":res.userPosts
          //   }
          // },
          columns: [{
              field: "userPostId",
              title: "id",
              visible: false,
              align: 'center'
            }, {
              field: "userPostName",
              title: "角色名称",
              align: 'left',
            },{
              field: "readAuth",
              title: "权限",
              align: 'center',
              class:'auth_userPostId',
              formatter: function (value, row, index) {
                let readAuth = '';
                if (row.readAuth && row.readAuth == 1) {
                  readAuth = 'checked';
                }
                let writeAuth = '';
                if (row.writeAuth && row.writeAuth == 1) {
                  writeAuth = 'checked';
                }
                return `
                      <input class="readAuth" id="${row.id}" systemDirectoryId="${row.systemDirectoryId}" userPostId="${row.userPostId}" type="checkbox" ${readAuth}/>
                      <span class="" id="">读</span>
                      <input class="writeAuth" userPostId="${row.userPostId}" type="checkbox" ${writeAuth}/>
                      <span class="" id="">写</span>
                    `;
              }
            }, {
              field: "opt",
              title: "操作",
              align: 'center',
              formatter: function (value, row, index) {
                return `<a class="a_style" onclick='del_Post_Authorities(this)'>移除</a>`;
              }
            }
          ],
          onLoadSuccess: function (data) {
            get_Post_Authorities();
            // $("#loading").css('display', 'none');
          },
          onLoadError: function () {
            $("#loading").css('display', 'none');
            layer.alert("系统内部错误!", {
              icon: 2,
              title: "提示信息"
            });
          },
          onPageChange: function () {
            $("#loading").css('display', 'block');
          }
        });
        $("#loading").css('display', 'none');
    //  }
    },
    error: function (data) {
    }
  })
}

//设置人员权限 
function add_user_Authorities(arr,status) {
  $("#loading").css('display', 'block');
  $.ajax({
    url: "/projectManage/assetLibrary/directoryAuthority/addOrUpdateUserAuthority",
    type: "post",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(arr),
    success: function (data) {
      if (data.flag) {
    	  selectUserIds = [];
        
        if(status == 0){
        	$("#userModal").modal("hide");
        	layer.alert('添加成功！', { icon: 1 });
            get_User_Authorities();
//            get_All_post();
        }else{
        	layer.alert('保存成功！', { icon: 1 });
        	$(".headRight>.btn").css("display", "inline-block");
            $("#return").css("display", "none");
            $("#cancel_btn").css("display", "none");
            $('.notRequirement').show();
            $('.powerDiv_List').hide();
        }
         $("#loading").css('display', 'none');
      }
    },
    error: function (data) {
      console.log(data);
    }
  })
}

//设置角色权限 
function add_post_Authorities(arr,status) {
	$("#loading").css('display', 'block');
  $.ajax({
    url: "/projectManage/assetLibrary/directoryAuthority/addOrUpdatePostAuthority",
    type: "post",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(arr),
    success: function (data) {
      if (data.flag) {
    	  if(status == 0){
    		  get_All_post();
    		  $('#user_post_Modal').modal('hide');
    		  $("#loading").css('display', 'none');
        	  layer.alert('添加角色成功！', { icon: 1 });
    	  }else{
    		  $("#loading").css('display', 'none');
    	  }
    	  
      }
    },
    error: function (data) {
    }
  })
}

//删除人员权限 
function del_user_Authorities(arr) {
  $("#loading").css('display', 'block');
  $.ajax({
    url: "/projectManage/assetLibrary/directoryAuthority/deleteUserAuthority",
    type: "post",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(arr),
    success: function (data) {
      if (data.flag) {
        layer.alert('删除成功！', { icon: 1 });
        get_User_Authorities();
      }
    },
    error: function (data) {
    }
  })
}

//删除角色权限 
function del_Post_Authorities(This) {
  let post_arr = [];
  post_arr.push({
    id: $(This).parent().prev().find('.readAuth').attr('id'), 
    systemDirectoryId: current_id,
    userPost: $(This).parent().prev().find('.readAuth').attr('userpostid'),
  })
//  post_arr.push({
//	  id: , 
//	  systemDirectoryId: current_id,
//	  userPost: $(This).parent().prev().find('.readAuth').attr('userpostid'),
//  })
  $("#loading").css('display', 'block');
  $.ajax({
    url: "/projectManage/assetLibrary/directoryAuthority/deletePostAuthority",
    type: "post",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(post_arr),
    success: function (data) {
      if (data.flag) {
        layer.alert('删除成功！', { icon: 1 });
        get_All_post();
      }
    },
    error: function (data) {
    }
  })
  
}

//所有权限提交
function set_user_auth(){
  let user_arr =  [];
  $("#user_Authoritie_list tbody").find('.auth_userPostId').each((idx,val) => {
    user_arr.push({
      id: $(val).find('.readAuth').attr('id'), 
      systemDirectoryId: $(val).find('.readAuth').attr('systemDirectoryId'),
      userId: $(val).find('.readAuth').attr('userId'),
      readAuth: $(val).find('.readAuth').is(":checked") ? 1 : 2,
      writeAuth: $(val).find('.writeAuth').is(":checked") ? 1 : 2
    })
  })
  let post_arr = [];
  $("#post_Authoritie_list tbody").find('.auth_userPostId').each((idx,val) => {
    post_arr.push({
      id: $(val).find('.readAuth').attr('id'), 
      systemDirectoryId: current_id,
      userPost: $(val).find('.readAuth').attr('userpostid'),
      readAuth: $(val).find('.readAuth').is(":checked") ? 1 : 2,
      writeAuth: $(val).find('.writeAuth').is(":checked") ? 1 : 2
    })
  })

  add_user_Authorities(user_arr,2);
  add_post_Authorities(post_arr,2);
}


//=================================  选择人员

//获取所有人员
function userTableShowAll() {
	let ids = '';
	$("#user_Authoritie_list > tbody").find('.auth_userPostId').each((idx,val) => {
		ids += $(val).find('.readAuth').attr('userid') + ',';
	})
	$("#loading").css('display', 'block');
  $("#userTable").bootstrapTable('destroy');
  $("#userTable").bootstrapTable({
    url: "/system/user/getAllUserModal3",
    method: "post",
    queryParamsType: "",
    pagination: true,
    sidePagination: "server",
    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
    pageNumber: 1,
    pageSize: 10,
    pageList: [5, 10, 15],
    //singleSelect : true,//单选
    singleSelect: false,
    queryParams: function (params) {
      var param = {
    	ids:ids,//已选的id
        userName: $.trim($("#userName").val()),
        companyName: $("#companyName").val(),
        deptName: $("#deptName").val(),
        pageNumber: params.pageNumber,
        pageSize: params.pageSize,
        projectIds: parameterArr.obj.id,
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
    	$("#userModal").modal("show");
    	$("#loading").css('display', 'none');
    },
    onLoadError: function () {

    },
    onCheckAll: function (rows) {//全选
      for (var i = 0; i < rows.length; i++) {
        //if(selectSysIds .indexOf(rows[i])<=-1){
        selectUserIds.push(rows[i]);
        //}

      }
    },
    onUncheckAll: function (rows) {
      for (var i = 0; i < rows.length; i++) {
        if (selectUserIds.indexOf(rows[i]) > -1) {
          selectUserIds.splice(selectUserIds.indexOf(rows[i]), 1);
        }
      }
    },
    onCheck: function (row) {//选中复选框
      //if(selectSysIds.indexOf(row)<=-1){
      selectUserIds.push(row);
      //}

    },
    onUncheck: function (row) {//取消复选框
      if (selectUserIds.indexOf(row) > -1) {
        selectUserIds.splice(selectUserIds.indexOf(row), 1);
      }
    }
  });
  
}

function clearSearchUser() {
  $("#userName").val('');
  $("#deptName").val('');
  $("#companyName").val('');
}

function commitUser() {
  if (selectUserIds.length <= 0) {
    layer.alert('请选择一列数据！', { icon: 0 });
    return false;
  } else {
    let temp_arr = selectUserIds.map(val=>{
      return {
        id : '',
        systemDirectoryId : current_id,
        userId : val.id,
        readAuth : 2,
        writeAuth : 2,
      }
    })
    add_user_Authorities(temp_arr,0);
  }

}

//获取角色
function post_TableShowAll() {
	let userPostIds = '';
	$("#post_Authoritie_list > tbody").find('.auth_userPostId').each((idx,val) => {
		userPostIds += $(val).find('.readAuth').attr('userPostId') + ',';
	})
  $("#user_post_Table").bootstrapTable('destroy');
  $("#user_post_Table").bootstrapTable({
    url: "/projectManage/oamproject/getProjectUserPost",
    method: "post",
    queryParamsType: "",
    pagination: true,
    sidePagination: "server",
    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
    pageNumber: 1,
    pageSize: 10,
    pageList: [5, 10, 15],
    //singleSelect : true,//单选
    singleSelect: false,
    queryParams: function (params) {
      var param = {
    	projectId :parameterArr.obj.id,
    	userPostIds : userPostIds//已选择的岗位id 需要排除掉
      }
      return param;
    },
    responseHandler: function (res) {
	    return{    
	      "total":res.userPosts.length,
	      "rows":res.userPosts
	    }
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
      field: "userPostName",
      title: "角色",
      align: 'center'
    }, ],
    onLoadSuccess: function () {
      
    },
    onLoadError: function () {

    },
    onCheckAll: function (rows) {//全选
//      for (var i = 0; i < rows.length; i++) {
//        //if(selectSysIds .indexOf(rows[i])<=-1){
//        selectUserIds.push(rows[i]);
//        //}
//
//      }
    },
    onUncheckAll: function (rows) {
//      for (var i = 0; i < rows.length; i++) {
//        if (selectUserIds.indexOf(rows[i]) > -1) {
//          selectUserIds.splice(selectUserIds.indexOf(rows[i]), 1);
//        }
//      }
    },
    onCheck: function (row) {//选中复选框
      //if(selectSysIds.indexOf(row)<=-1){
//      selectUserIds.push(row);
      //}

    },
    onUncheck: function (row) {//取消复选框
//      if (selectUserIds.indexOf(row) > -1) {
//        selectUserIds.splice(selectUserIds.indexOf(row), 1);
//      }
    }
  });
  $("#user_post_Modal").modal("show");
}

function commit_post_User() {
let select_arr = $('#user_post_Table').bootstrapTable('getSelections');
  if (select_arr.length <= 0) {
    layer.alert('请选择一列数据！', { icon: 0 });
    return false;
  } else {
	  let post_arr = [];
	  select_arr.map(val => {
	    post_arr.push({
	      id: '', 
	      systemDirectoryId: current_id,
	      userPost: val.userPostId,
	      readAuth: 2,
	      writeAuth: 2
	    })
	  })
    add_post_Authorities(post_arr,0);
  }

}