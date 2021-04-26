
$(() => {
  // chart_map();
  // check_work();
})

function chart_map() {
  // $.ajax({
  //   url : "/pro/ganttChart/", 
  //   type : "post",
  //   data : "proId=" + proId,
  //   dataType : "JSON",
  //   cache : false, //关闭AJAX缓存
  //   success : function(data) {
  //   }
  // })
}

//修改计划提交
function update_submit() {
  var submit_data = {
    id: $('#edit_id').val(),
    planName: $('#planName').val(),
    startDate: $('#startDate').val(),
    startDate_milestone: $('#startDate_milestone').is(":checked"),
    endDate: $('#endDate').val(),
    endDate_milestone: $('#endDate_milestone').is(":checked"),
    Manager: $('#Manager').val(),
    result: $('#result').val(),
    remark: $('#remark').val(),
  }
  console.log(submit_data)
}

//查看开发任务
function check_work() {
  // $("#loading").css('display', 'block');
  // $.ajax({
  //     url: defectUrl + "defect/getDefectLogById",
  //     method: "post",
  //     data: {
  //         defectId: $("#checkDefectID").val()
  //     },
  //     success: function (data) {
  //         $("#loading").css('display', 'none');
  if (logs.status == 2) {
    layer.alert(data.errorMessage, {
      icon: 2,
      title: "提示信息"
    });
  } else if (logs.status == 1) {
    if (logs.data != null) {
      var str = '';
      for (var i = 0; i < data.data.length; i++) {
        if (i == (data.data.length - 1)) {
          str += '<div class="logDiv lastLog">';
        } else {
          str += '<div class="logDiv">'
        }
        var log_Type = data.data[i].logType;


        str += '<div class="logDiv_title"><span class="orderNum"></span><span class="fontWeihgt">' + log_Type + '</span>&nbsp;&nbsp;&nbsp;' +
          '<span>' + data.data[i].userName + '</span>&nbsp;&nbsp;&nbsp;<span>' + data.data[i].createDate + '</span></div>' +
          '<div class="logDiv_cont"><div class="logDiv_contBorder">';
        var _span = '';
        switch (data.data[i].logType) {
          case "新建缺陷":
            str += "<br />";
            break;
          case "更新状态":

            var childStr = '';
            if (data.data[i].logDetail != null && data.data[i].logDetail.length > 0) {
              var logDetail = JSON.parse(data.data[i].logDetail);
              if (logDetail != null && logDetail.length > 0) {
                for (var j = 0; j < logDetail.length; j++) {
                  if (j == 0) {
                    if (logDetail[0].remark != undefined) {
                      childStr += "备注内容：<span class='span_font-weight'>" + logDetail[0].remark + "</span><br >";
                    } else {
                      var oldName = logDetail[j].oldValue;
                      var newName = logDetail[j].newValue;

                      var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                      childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />';

                    }
                  } else {
                    var oldName = logDetail[j].oldValue;
                    var newName = logDetail[j].newValue;

                    var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                    childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />';

                  }
                }
              } else {
                _span = '<span>未经任何操作</span><br/>';
              }
            } else {
              _span = '<span>未经任何操作</span><br/>';
            }
            if (data.data[i].logAttachementList != null && data.data[i].logAttachementList.length > 0) {
              _span = '';

              var status_1 = 0;
              var status_2 = 0;
              childStr += '<div class="file-upload-list"><table class="file-upload-tb">';
              for (var j = 0; j < data.data[i].logAttachementList.length; j++) {

                var logAttachementList = data.data[i].logAttachementList;

                if (logAttachementList[j].status == 1) {

                  childStr += '<tr><td>';
                  var classType = '';
                  var LogFileType = logAttachementList[j].fileType;
                  classType = classPath(LogFileType, classType);

                  if (status_1 == 0) {
                    childStr += "<span>新增附件：</span></td><td>";
                  } else {
                    childStr += "<span></span></td><td>";
                  }

                  childStr += classType;

                  var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                  childStr += '&nbsp;<a  class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                  status_1++;
                } else if (logAttachementList[j].status == 2) {

                  childStr += '<tr><td>';

                  var classType = '';
                  var LogFileType = logAttachementList[j].fileType;
                  classType = classPath(LogFileType, classType);

                  if (status_2 == 0) {
                    childStr += "<span>删除附件：</span></td><td>";
                  } else {
                    childStr += "<span></span></td><td>";
                  }

                  childStr += classType;

                  var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                  childStr += '&nbsp;<a class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                  status_2++;
                }

              }
              childStr += '</table></div>';
            }
            childStr += _span;
            str += '<div class="logDiv_contRemark">';
            str += childStr;
            str += '</div>';
            break;
          case "修改内容":
            var childStr = '';
            var logDetail = JSON.parse(data.data[i].logDetail);
            if (logDetail != null && logDetail.length > 0) {
              for (var j = 0; j < logDetail.length; j++) {
                if (j == 0) {
                  if (logDetail[0].remark != undefined) {
                    childStr += "备注内容：<span class='span_font-weight'>" + logDetail[0].remark + "</span><br >";
                  } else {
                    var oldName = logDetail[j].oldValue;
                    var newName = logDetail[j].newValue;

                    var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                    if (logDetail[j].fieldName == "缺陷描述") {
                      childStr += '<span>缺陷描述已修改。</span><br />'
                    } else if (logDetail[j].fieldName == "扩展字段") {
                      var obj1 = JSON.parse(oldName).field;
                      var obj2 = JSON.parse(newName).field;
                      childStr += '<span>' + logDetail[j].fieldName + '：</span>';
                      childStr += dealExtend(obj1, obj2);
                    } else {
                      childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                    }
                  }
                } else {
                  var oldName = logDetail[j].oldValue;
                  var newName = logDetail[j].newValue;

                  var arr = logDetailList(logDetail[j].fieldName, oldName, newName);
                  if (logDetail[j].fieldName == "缺陷描述") {
                    childStr += '<span>缺陷描述已修改。</span><br />'
                  } else if (logDetail[j].fieldName == "扩展字段") {
                    var obj1 = JSON.parse(oldName).field;
                    var obj2 = JSON.parse(newName).field;
                    childStr += '<span>' + logDetail[j].fieldName + '：</span>';
                    childStr += dealExtend(obj1, obj2);
                  } else {
                    childStr += '<span>' + logDetail[j].fieldName + '：<span class="span_font-weight">"' + arr[0] + '"</span>' + + '<span class="span_font-weight">"' + arr[1] + '"</span></span><br />'
                  }
                }
              }
            } else {
              _span = '<span>未经任何操作</span><br/>';
            }

            if (data.data[i].logAttachementList != null && data.data[i].logAttachementList.length > 0) {
              _span = '';
              var status_1 = 0;
              var status_2 = 0;
              childStr += '<div class="file-upload-list"><table class="file-upload-tb">';
              for (var j = 0; j < data.data[i].logAttachementList.length; j++) {
                var logAttachementList = data.data[i].logAttachementList;

                if (logAttachementList[j].status == 1) {
                  childStr += '<tr><td>';
                  var classType = '';
                  var LogFileType = logAttachementList[j].fileType;
                  classType = classPath(LogFileType, classType);

                  if (status_1 == 0) {
                    childStr += "<span>新增附件：</span></td><td>";
                  } else {
                    childStr += "<span></span></td><td>";
                  }

                  childStr += classType;
                  var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                  childStr += '&nbsp;<a  class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                  status_1++;
                } else if (logAttachementList[j].status == 2) {
                  childStr += '<tr><td>';

                  var classType = '';
                  var LogFileType = logAttachementList[j].fileType;
                  classType = classPath(LogFileType, classType);

                  if (status_2 == 0) {
                    childStr += "<span>删除附件：</span></td><td>";
                  } else {
                    childStr += "<span></span></td><td>";
                  }
                  childStr += classType;

                  var row = JSON.stringify(logAttachementList[j]).replace(/"/g, '&quot;');
                  childStr += '&nbsp;<a class="span_a_style" download="' + 1 + '" onclick="downloadFile(  ' + row + '  )">' + logAttachementList[j].fileNameOld + '</a></td><tr>';
                  status_2++;
                }

              }
              childStr += '</table></div>';
            }
            childStr += _span;
            str += '<div class="logDiv_contRemark">';
            str += childStr;
            str += '</div>';
            break;
          default:
            break;
        }
        str += '</div></div></div>';
      }
      $("#dealLogDiv").append(str);
      $("#checkDefectDiv").modal('show');
    }
  }
  // _flag = 0;
  //     },
  //     error: function () {
  //         $("#loading").css('display', 'none');
  //         layer.alert(errorDefect, {
  //             icon: 2,
  //             title: "提示信息"
  //         });
  //     }
  // });
}

var defectStatusList = [1, 2, 3, 4, 5]
function logDetailList(fieldName, oldName, newName) {
  var arr = [];
  switch (fieldName) {
    case "缺陷状态":
      for (var k = 0; k < defectStatusList.length; k++) {
        if (defectStatusList[k].value != '') {
          if (oldName == defectStatusList[k].value) {
            oldName = defectStatusList[k].innerHTML;
          }
          if (newName == defectStatusList[k].value) {
            newName = defectStatusList[k].innerHTML;
          }
        }
      }
      break;
    case "缺陷类型":
      for (var k = 0; k < defectTypeList.length; k++) {
        if (defectTypeList[k].value != '') {
          if (oldName == defectTypeList[k].value) {
            oldName = defectTypeList[k].innerHTML;
          }
          if (newName == defectTypeList[k].value) {
            newName = defectTypeList[k].innerHTML;
          }
        }
      }
      break;
    case "缺陷来源":
      for (var k = 0; k < defectSourceList.length; k++) {
        if (defectSourceList[k].value != '') {
          if (oldName == defectSourceList[k].value) {
            oldName = defectSourceList[k].innerHTML;
          }
          if (newName == defectSourceList[k].value) {
            newName = defectSourceList[k].innerHTML;
          }
        }
      }
      break;
    case "严重级别":
      for (var k = 0; k < severityLevelList.length; k++) {
        if (severityLevelList[k].value != '') {
          if (oldName == severityLevelList[k].value) {
            oldName = severityLevelList[k].innerHTML;
          }
          if (newName == severityLevelList[k].value) {
            newName = severityLevelList[k].innerHTML;
          }
        }
      }
      break;
    case "紧急程度":
      for (var k = 0; k < emergencyLevelList.length; k++) {
        if (emergencyLevelList[k].value != '') {
          if (oldName == emergencyLevelList[k].value) {
            oldName = emergencyLevelList[k].innerHTML;
          }
          if (newName == emergencyLevelList[k].value) {
            newName = emergencyLevelList[k].innerHTML;
          }
        }
      }
      break;
    default:
      break;
  }
  arr.push(oldName);
  arr.push(newName);
  return arr;
}