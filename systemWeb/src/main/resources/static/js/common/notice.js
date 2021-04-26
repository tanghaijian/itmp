/******
 * 公告信息
 * 最顶层公告展示
 * *******/

function showAnnouncement(list) {
  announcementObj.arr = list;
  list.map(function (val, index) {
    if (index < 3) {
      addOnesAnnouncement(val);
    }
  })
}

//添加展示新的公告
function addOnesAnnouncement(data) {
  var str = '<div class="alert alert-warning alert-dismissible fade in" role="alert" onclick="showAnnounInfo( ' + data.id + ' )">' +
    '<button type="button" onclick="delAnnouncement( ' + data.id + ',this,event )" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>' +
    '<strong class="spanCont">' + announcementObj.title + ':</strong>' +
    '<span class="spanCont">' +
    data.noticeContent +
    '</span>' +
    '</div>';
  $("#announcement").append(str);
  var ele = $("#announcement>.alert-warning").last()[0];
  if (ele.offsetWidth < ele.scrollWidth) {
    var key = "timer" + data.id;
    announcementObj.timer[key] = setInterval(function() {
      var start = $(ele).children(".spanCont").text().substring(0, 1);
      // 获取到 后面的所有字符
      var end = $(ele).children(".spanCont").text().substring(1);
      // 重新拼接得到新的字符串，并赋值给 this.msg
      $(ele).children(".spanCont").text(end + start);
    }, 400)
  }
}

//获取公告信息
function showAnnounInfo(id) {
  layer.open({
    type: 2,
    title: announcementObj.title,
    shadeClose: true,
    shade: false,
    move: false,
    area: ['60%', '60%'],
    id: "noticeLayer",
    offset: "15% 20%",
    shade: 0.3,
    tipsMore: true,
    anim: 2,
    content: '/devManageui/notice/toNoticeDetail?noticeId=' + id,
    btn: ['关闭'],
    btnAlign: 'c', //按钮居中 
    no: function () {
      layer.closeAll();
    }
  });
}

//删除页面公告，不展示
function delAnnouncement(id, self, e) {
  var key = "timer" + id;
  if (typeof (announcementObj.timer[key]) != undefined) {
    clearInterval(announcementObj.timer[key])
    delete announcementObj.timer[key]
  }
  for (var i = 0; i < announcementObj.arr.length; i++) {
    if (announcementObj.arr[i].id == id) {
      announcementObj.arr.splice(i, 1);
      break;
    }
  }
  $(self).parent().fadeOut(300, function () {
    $(this).remove();
    /* $( this ).parent.remove();   */
    if (announcementObj.arr.length >= 3) {
      addOnesAnnouncement(announcementObj.arr[2]);
    }
  })
  stopDefaultEvent(e)
}

//阻止事件冒泡
function stopDefaultEvent(e) {
  if (e && e.stopPropagation) {
    e.stopPropagation();
  } else if (window.event) {
    window.event.cancelBubble = true;
  }
}