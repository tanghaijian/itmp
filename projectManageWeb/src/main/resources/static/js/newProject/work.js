/**********
 * 2019-11-14
 * *******/
$(() => {
  initTable();
})

//菜单按钮
function initTable() {
  $.ajax({
      type: "POST",
      url: "/system/role/getUserMenu",
      dataType: "json",
      beforeSend: function () {
          $("#loading").css('display', 'block');
      },
      success: function (msg) {
          if (msg.status == 1) {
              if (!msg.result.length) return;
              $('#workTable').empty();
              load_table(msg);
          }
      },
      error: function (msg) {
          $('#defaultMenuModal').modal("hide");
          layer.alert('服务器内部错误,请联系管理员!', {
              icon: 2,
              title: "提示信息"
          });
      }
  });
}

//加载表格
function load_table(data) {
  data.result.map(function (val, idx) { //一级菜单
      if (!val.parentId) {
          $('#defaultMenu_body tbody').append(`
              <tr role="row" id="${val.id}" class="first_menu">
                  <td class="p_l_10">${val.menuButtonName}</td>
                  <td class="p_l_5">
                      <div class="arrows_right icon_click arrows_"></div>
                      <div class="p_l_5">${val.menuButtonName}</div>
                  </td>
                  <td>${val.startDate}</td>
                  <td>${val.endDate}</td>
                  <td>${val.progress}</td>
              </tr>
          `);
      }
  })
  data.result.map(function (val, idx) { //二级菜单
      $('.first_menu').each(function (index, ele) {
          if (val.parentId && $(ele).attr('id') == val.parentId) {
              $(ele).after(`
                <tr role="row" data-id="${val.parentId}" id="${val.id}" class="parent_${$(ele).attr('id')} _hide second_menu">
                    <td class="p_l_10">${val.menuButtonName}</td>
                    <td class="p_l_5">
                        <div class="arrows_right icon_click arrows_"></div>
                        <div class="p_l_5">${val.menuButtonName}</div>
                    </td>
                    <td>${val.startDate}</td>
                    <td>${val.endDate}</td>
                    <td>${val.progress}</td>
                </tr>
              `);
          }
      })
  })
  data.result.map(function (val, idx) { //三级菜单
      $('.second_menu').each(function (index, ele) {
          if (val.parentId && $(ele).attr('id') == val.parentId) {
            $(ele).after(`
                <tr role="row" data-id="${val.parentId}" id="${val.id}"  class="parent_second_${$(ele).attr('id')} _hide">
                    <td class="p_l_10">${val.menuButtonName}</td>
                    <td class="p_l_5">
                        <div class="arrows_right icon_click arrows_"></div>
                        <div class="p_l_5">${val.menuButtonName}</div>
                    </td>
                    <td>${val.startDate}</td>
                    <td>${val.endDate}</td>
                    <td>${val.progress}</td>
                </tr>
            `);
          }
      })
  })
  $('#defaultMenuModal').modal("show");
  $("#loading").css('display', 'none');
  give_handle();
}

//展开事件
function give_handle() {
  $('.icon_click').bind('click', function () {
      var parent_id = $(this).parent().parent().attr('id');
      if ($(this).hasClass('arrows_right')) {
          $(this).removeClass('arrows_right').addClass('arrows_bottom');
          $(this).parent().parent().siblings().each(function (idx, val) {
              if ($(val).data('id') == parent_id) {
                  $(`.parent_${parent_id}`).removeClass('_hide');
                  $(`.parent_second_${parent_id}`).removeClass('_hide');
              }
          })
      } else {
          $(this).removeClass('arrows_bottom').addClass('arrows_right');
          $(this).parent().parent().siblings().each(function (idx, val) {
              if ($(val).data('id') == parent_id) {
                  $(`.parent_${parent_id}`).addClass('_hide');
                  $(`.parent_second_${parent_id}`).addClass('_hide');
              }
          })
      }
  })
}