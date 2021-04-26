/**
 * Description: 报表中心-缺陷报表
 * Author:
 * Date: 2020/3/25
 */


// $.ajax({
//     url:"/report/projectWeekly/taskDeliver",
//     dataType:'json',
//     type:'post',
//     success : function(data){
//         alert(data);
//     },
//     error:function(){
//         $("#loading").css('display','none');
//         layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
//     }
// })



	var iframe_flag = true;//是否请求
	var is_check_Collect_flag = true;//是否继续收藏
	var collection_obj = {
	  menuUrl : '../report/toDefectReport',
	  filterName : '',
	  search : [],
	  isCollect : false
	};
	//iframe报表区传入参数：根据ureport2的预览功能提供的Url进行组装
	var iframeUrl = '/report/ureport/preview?_u=s3:';
	var report_1 = 'defect_1.ureport.xml&_i=1&_r=1';
	var report_2 = 'defect_2.ureport.xml&_i=1&_r=1';
	var report_3 = 'defect_3.ureport.xml&_i=1&_r=1';
	var report_3b = 'd_3_plus.ureport.xml&_i=1&_r=1';//测试
	//var report_3b = 'defect_3plus.ureport.xml&_i=1&_r=1';//生产
	var report_4 = 'defect_3.ureport.xml&_i=1&_r=1';
	var report_5 = 'defect_5.ureport.xml&_i=1&_r=1';
	var report_6 = 'defect_6.ureport.xml&_i=1&_r=1';
	$(document).ready(function () {
	    system_req();
	    downOrUpButton();
	    other();
	    collect_handle();
	    getCollection(); 
	    change_report({order:0});
	    buttonClear();
	});
	
	//图标中查询下拉框触发方法
	/**
	 * 
	 * @param {systemId:系统ID,
	 *         defectType:缺陷类型,
	 *         defectSource:缺陷来源,
	 *         severityLevel:缺陷等级,
	 *         emergencyLevel:紧急程度,
	 *         defectStatus:缺陷状态,
	 *         createDate:创建日期,
	 *         endDate:结束日期,
	 *         windowDate:投产开始日期,
	 *         endWindowDate:投产结束日期,
	 *         commissioningWindowId:投产窗口,
	 *         order:第几个iframe，0表示所有}
	 * @returns
	 */
	function change_report({systemId,defectType,defectSource,severityLevel,emergencyLevel
	  ,defectStatus,createDate,endDate,windowDate,endWindowDate,commissioningWindowId,order}){
	  $("#loading").css('display','block');
	  var systemIds_ = $('#systemIds').val() || '';
	  var defectType_ = ($('#defectTypes').val() || '') && $('#defectTypes').val().join(',');
	  var defectSource_ = $('#defectSource').val() || '';
	  var severityLevel_ = ($('#defectLevel').val() || '') && $('#defectLevel').val().join(',');
	  var defectStatus_ = $('#defectStatus').val() || '';
	  var emergencyLevel_ = $('#emergencyLevel').val() || '';
	  var system = systemIds_ && systemIds_.join(',');
	  var status = defectStatus_ && defectStatus_.join(',');
	  var source = defectSource_ && defectSource_.join(',');
	  var level = emergencyLevel_ && emergencyLevel_.join(',');
	  var window = $('#commitWindowId').val();
	  var createDate_ = $('#defectDate').val() && $('#defectDate').val().split(' - ');
	  var windowDate_ = $('#commitWindowDate').val() && $('#commitWindowDate').val().split(' - ');
	  var WindowId = '';
	  if(defectStatus){
	    status = defectStatus;
	  }
	  if(defectSource){
	    source = defectSource;
	  }
	  if(emergencyLevel){
	    source = emergencyLevel;
	  }
	  if(commissioningWindowId){
	    WindowId = commissioningWindowId;
	  }
	  if(systemId){
	    system = systemId;
	  }
	  var iframeParams= '&systemId='+system+'&defectType='+defectType_+'&defectSource='+source+
	      '&severityLevel='+severityLevel_+'&emergencyLevel='+level+'&defectStatus='+status+
	      '&createDate='+isValueNull(createDate_[0])+'&endDate='+isValueNull(createDate_[1])
	      +'&windowDate='+isValueNull(windowDate_[0])+'&endWindowDate='+isValueNull(windowDate_[1])+'&commissioningWindowId='+WindowId;
	  //order对应的第几个
	  if(order == 1){
	    $('#iframe_table1').attr('src',iframeUrl + report_1 + iframeParams );
	    $('#iframe_table1_a').attr('href',iframeUrl + report_1 + iframeParams );
	  }else if(order == 2){
	    $('#iframe_table2').attr('src',iframeUrl + report_2 + iframeParams );
	    $('#iframe_table2_a').attr('href',iframeUrl + report_2 + iframeParams );
	  }else if(order == 3){
	    $('#iframe_chart1').attr('src',iframeUrl + report_3 + iframeParams );
	    $('#iframe_chart1_a').attr('href',iframeUrl + report_3 + iframeParams );
	    $('#iframe_chart1b').attr('src',iframeUrl + report_3b + iframeParams );
	    $('#iframe_chart1b_a').attr('href',iframeUrl + report_3b + iframeParams );
	  }else if(order == 4){
	    $('#iframe_chart2').attr('src',iframeUrl + report_4 + iframeParams );
	    $('#iframe_chart2_a').attr('href',iframeUrl + report_4 + iframeParams );
	  }else if(order == 5){
	    $('#iframe_table3').attr('src',iframeUrl + report_5 + iframeParams );
	    $('#iframe_table3_a').attr('href',iframeUrl + report_5 + iframeParams );
	  }else if(order == 6){
	    $('#iframe_table4').attr('src',iframeUrl + report_6 + iframeParams );
	    $('#iframe_table4_a').attr('href',iframeUrl + report_6 + iframeParams );
	  }else if(order == 0){
	    $('#iframe_table1').attr('src',iframeUrl + report_1 + iframeParams );
	    $('#iframe_table2').attr('src',iframeUrl + report_2 + iframeParams );
	    $('#iframe_chart1').attr('src',iframeUrl + report_3 + iframeParams );
	    $('#iframe_chart2').attr('src',iframeUrl + report_4 + iframeParams );
	    $('#iframe_table3').attr('src',iframeUrl + report_5 + iframeParams );
			$('#iframe_table4').attr('src',iframeUrl + report_6 + iframeParams );
			
			$('#iframe_chart1b').attr('src',iframeUrl + report_3b + iframeParams );
	    $('#iframe_chart1b_a').attr('href',iframeUrl + report_3b + iframeParams );
	    
	    $('#iframe_table1_a').attr('href',iframeUrl + report_1 + iframeParams );
	    $('#iframe_table2_a').attr('href',iframeUrl + report_2 + iframeParams );
	    $('#iframe_chart1_a').attr('href',iframeUrl + report_3 + iframeParams );
	    $('#iframe_chart2_a').attr('href',iframeUrl + report_4 + iframeParams );
	    $('#iframe_table3_a').attr('href',iframeUrl + report_5 + iframeParams );
	    $('#iframe_table4_a').attr('href',iframeUrl + report_6 + iframeParams );
	  }
	  setTimeout(function(){
	    iframe_flag = true;
	    $("#loading").css('display','none');
	  },1000)
	}
	
	//统计
	function searchInfo() {
	    $("#loading").css('display','block');
	    iframe_flag = false;
	    var systemIds = $('#systemIds').val() || '';
	    var defectType = ($('#defectTypes').val() || '') && $('#defectTypes').val().join(',');
	    var defectSource = $('#defectSource').val() || '';
	    var severityLevel = ($('#defectLevel').val() || '') && $('#defectLevel').val().join(',');
	    var defectStatus = $('#defectStatus').val() || '';
	    var emergencyLevel = $('#emergencyLevel').val() || '';
	    var system = systemIds && systemIds.join(',');
	    var status = defectStatus && defectStatus.join(',');
	    var source = defectSource && defectSource.join(',');
	    var level = emergencyLevel && emergencyLevel.join(',');
	    var window = $('#commitWindowId').val();
	    var createDate = $('#defectDate').val() && $('#defectDate').val().split(' - ');
	    var windowDate = $('#commitWindowDate').val() && $('#commitWindowDate').val().split(' - ');
	    $('#defectStatus_table1').val(defectStatus);
	    $('#defectSource_table1').val(defectSource);
	    $('#defectStatus_table2').val(defectStatus);
	    $('#emergencyLevel_table2').val(emergencyLevel);
	    $('#defectStatus_chart1').val(defectStatus);
	    $('#emergencyLevel_chart1').val(emergencyLevel);
	    $('#defectStatus_chart2').val(defectStatus);
	    $('#commitWindowId_chart2').val($('#commitWindowId').val());
	    $('#commitWindowName_chart2').val($('#commitWindowName').val());
	    $('#defectStatus_table3').val(defectStatus);
	    $('#system_table3').val(systemIds);
	    $('#defectSource_table4').val(defectSource);
	    $('.selectpicker').selectpicker('refresh');
	    change_report({order:0});
	}
	
	//清空
	function clearSearch() {
	  $('#systemIds').val('');
	  $('#defectTypes').val('');
	  $('#defectSource').val('');
	  $('#defectLevel').val('');
	  $('#emergencyLevel').val('');
	  $('#defectStatus').val('');
	  $('#defectDate').val('');
	  $('#commitWindowDate').val('');
	  $(".collection").children("span").removeClass("fa-heart").addClass("fa-heart-o");
	  $('.selectpicker').selectpicker('refresh');
	}
	
	//其他配置
	function other() {
	  var search_ele = ['systemIds','defectTypes','defectSource','defectLevel','emergencyLevel','defectStatus','defectDate','commitWindowDate'];
	  search_ele.map(function(ele){
		  $('#'+ ele).on('change', function () {
			  if(is_check_Collect_flag){
				  $(".collection").children("span").removeClass("fa-heart").addClass("fa-heart-o");
			  }
		  })
	  })
	  var locale = {
	  	applyLabel: "确认",
	    cancelLabel: "取消",
	    resetLabel: "重置",
	    //"applyLabel": "确定",
	    //"cancelLabel": "取消",
	    "fromLabel": "起始时间",
	    "toLabel": "结束时间",
	    "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
	    "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	    "firstDay": 1
	  };
	  $("#defectDate").daterangepicker({
	  	timePicker: true, //显示时间
	    timePicker24Hour: true, //时间制
	    timePicker12Hour:false,
	    timePickerSeconds: true, //时间显示到秒
	    format: 'YYYY-MM-DD HH:mm',
	    startDate: new Date(),
	    endDate: new Date(),
	    minDate:1999-12-12,
	    maxDate:2050-12-30,
	    timePicker: true,
	    timePickerIncrement: 1,
	    'locale': locale
	  }).on('cancel.daterangepicker', function (ev, picker) {
	  }).on('apply.daterangepicker', function (ev, picker) {
	    $("#defectDate").change();
	  });
	  $("#commitWindowDate").daterangepicker({
	  	timePicker: true, //显示时间
	    timePicker24Hour: true, //时间制
	    timePicker12Hour:false,
	    timePickerSeconds: true, //时间显示到秒
	    format: 'YYYY-MM-DD HH:mm',
	    startDate: new Date(),
	    endDate: new Date(),
	    minDate:1999-12-12,
	    maxDate:2050-12-30,
	    timePicker: true,
	    timePickerIncrement: 1,
	    'locale': locale
	  }).on('cancel.daterangepicker', function (ev, picker) {
	  }).on('apply.daterangepicker', function (ev, picker) {
	    $("#commitWindowDate").change();
	  });
	
	  $('#defectStatus_table1').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_table1').val() && $('#defectStatus_table1').val().join(',');
	    var source = $('#defectSource_table1').val() && $('#defectSource_table1').val().join(',');
	    change_report({defectSource:source,defectStatus:status,order:1});
	  })
	  $('#defectSource_table1').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_table1').val() && $('#defectStatus_table1').val().join(',');
	    var source = $('#defectSource_table1').val() && $('#defectSource_table1').val().join(',');
	    change_report({defectSource:source,defectStatus:status,order:1});
	  })
	
	  $('#defectStatus_table2').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_table2').val() && $('#defectStatus_table2').val().join(',');
	    var level = $('#emergencyLevel_table2').val() && $('#emergencyLevel_table2').val().join(',');
	    change_report({emergencyLevel:level,defectStatus:status,order:2});
	  })
	  $('#emergencyLevel_table2').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_table2').val() && $('#defectStatus_table2').val().join(',');
	    var level = $('#emergencyLevel_table2').val() && $('#emergencyLevel_table2').val().join(',');
	    change_report({emergencyLevel:level,defectStatus:status,order:2});
	  })
	
	  $('#defectStatus_chart1').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_chart1').val() && $('#defectStatus_chart1').val().join(',');
	    var level = $('#emergencyLevel_chart1').val() && $('#emergencyLevel_chart1').val().join(',');
	    change_report({emergencyLevel:level,defectStatus:status,order:3});
	  })
	  $('#emergencyLevel_chart1').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_chart1').val() && $('#defectStatus_chart1').val().join(',');
	    var level = $('#emergencyLevel_chart1').val() && $('#emergencyLevel_chart1').val().join(',');
	    change_report({emergencyLevel:level,defectStatus:status,order:3});
	  })
	
	  $('#defectStatus_chart2').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_chart2').val() && $('#defectStatus_chart2').val().join(',');
	    var window = $('#commitWindowId_chart2').val() && $('#commitWindowId_chart2').val().join(',');
	    change_report({commissioningWindowId:window,defectStatus:status,order:4});
	  })
	
	  $('#defectStatus_table3').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_table3').val() && $('#defectStatus_table3').val().join(',');
	    var system = $('#system_table3').val() && $('#system_table3').val().join(',');
	    change_report({systemId :system,defectStatus:status,order:5});
	  })
	  $('#system_table3').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_table3').val() && $('#defectStatus_table3').val().join(',');
	    var system = $('#system_table3').val() && $('#system_table3').val().join(',');
	    change_report({systemId:system,defectStatus:status,order:5}); 
	  })
	
	  $('#defectSource_table4').on('change', function () {
	    if (!iframe_flag) return;
	    var source = $('#defectSource_table4').val() && $('#defectSource_table4').val().join(',');
	    change_report({defectSource:source,order:6});
	  })
	}
	
	//系统
	function system_req() {
	    $("#loading").css('display','block');
	    $.ajax({
	      url:"/testManage/modal/selectAllSystemInfo",
	      dataType:'json',
	      type:'post',
	      data:{
	        systemName:'',
	          systemCode:'',
	          pageNumber:1,
	          pageSize:99
	      },
	      success : function(data){
	        data.rows.length && data.rows.map(val=>{
	            $("#systemIds").append(`
	              <option value="${val.id}">${val.systemName}</option>
	            `);
	            $('#system_table3').append(`
	              <option value="${val.id}">${val.systemName}</option>
	            `);
	        })
	        $('.selectpicker').selectpicker('refresh');
	        $("#loading").css('display','none');
	    },
	    error:function(){
	      $("#loading").css('display','none');
	      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	    }
	  })
	}
	
	//========================投产窗口start=====================
	function commitWindow() {
	  $("#comWindowTable").bootstrapTable('destroy');
	  $("#comWindowTable").bootstrapTable({
	    url: "/projectManage/commissioningWindow/selectCommissioningWindows2",
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
	        "windowName": $.trim($("#win_windowName").val()),
	        "windowDate": $("#win_windowDate").val(),
	        "windowType": $("#win_windowType").val(),
	        pageNumber: params.pageNumber,
	        pageSize: params.pageSize,
	      }
	      return param;
	    },
	    columns: [{
	      checkbox: true,
	      width: "30px",
	    }, {
	      field: "id",
	      title: "id",
	      visible: false,
	      align: 'center'
	    }, {
	      field: "windowName",
	      title: "窗口名称",
	      align: 'center'
	    }, {
	      field: "windowDate",
	      title: "投产日期",
	      align: 'center'
	    }, {
	      field: "typeName",
	      title: "窗口类型",
	      align: 'center'
	    }],
	    onLoadSuccess: function () {
	      $("#loading").css('display', 'none');
	      $("#comWindowModal").modal('show');
	    },
	    onLoadError: function () {
	      $("#loading").css('display', 'none');
	    }
	  });
	}
	
	function commitWin() {
	  var selectContent = $("#comWindowTable").bootstrapTable('getSelections')[0];
	  if (typeof (selectContent) == 'undefined') {
	    layer.alert('请选择一列数据！', { icon: 0 });
	    return false;
	  } else {
	    var commissioningWindowId = selectContent.id;
	    $("#commitWindowId_chart2").val(commissioningWindowId);
	    $("#commitWindowName_chart2").val(selectContent.windowName);
	    var status = $('#defectStatus_chart2').val() && $('#defectStatus_chart2').val().join(',');
	    change_report({commissioningWindowId:commissioningWindowId,defectStatus:status,order:4});
	    $("#comWindowModal").modal("hide");
	  }
	
	}
	
	function winReset() {
	  $("#win_windowName").val("");
	  $("#win_windowDate").val("");
	  $("#win_windowType").selectpicker('val', '');
	}
	
	//========================投产窗口end=====================
	/**
	* 判断元素是否为空值
	*/
	function _is_null(ele){
	  return $('#'+ele).val() ? true : false;
	}
	
	function change_str(ele){
	  if($('#'+ele).val()){
	    var arr = $('#'+ele).val().map(v=>{ 
	      return v > 9 ? v : '0'+v
	    })
	    return arr.join();
	  }else{
	    return '';
	  }
	}
	
	function change_str_num(str){
	  if(str){
		return str.split(',').map(v=>{ 
	      return v > 9 ? v : +v
	    })
	  }else{
	    return '';
	  }
	}
	
	// 搜索框 收藏按钮控制 js 部分
	function collect_handle() {
	  $(".collection").click(function () {
		  collection_obj.search = [];
	    if ($(this).children("span").hasClass("fa-heart-o")) {
	      collection_obj.search.push(
	        { "type": "select", "value": { "systemIds": change_str('systemIds')}, "isData":_is_null("systemIds") },
	        { "type": "select", "value": { "defectTypes": change_str('defectTypes')}, "isData":_is_null("defectTypes") },
	        { "type": "select", "value": { "defectSource": change_str('defectSource')}, "isData":_is_null("defectSource") },
	        { "type": "select", "value": { "defectLevel": change_str('defectLevel')}, "isData":_is_null("defectLevel") },
	        { "type": "select", "value": { "emergencyLevel": change_str('emergencyLevel')}, "isData":_is_null("emergencyLevel") },
	        { "type": "select", "value": { "defectStatus": change_str('defectStatus')}, "isData":_is_null("defectStatus") },
	        { "type": "input", "value": { "defectDate": $("#defectDate").val()}, "isData":_is_null("defectDate") },
	        { "type": "input", "value": { "commitWindowDate": $("#commitWindowDate").val()},"isData": _is_null("commitWindowDate")}
	      )
	      var isResult = collection_obj.search.some(function (item) {
	        return item.isData
	      })
	      collection_obj.isCollect = isResult;
	      if(isResult){
	    	  $('#collect_Name').val('');
	          $('#collect_Modal').modal('show');
	      }
	    } else {
	      layer.confirm('确定要取消收藏吗?', {btn: ['确定', '取消'],icon:0, title: "提示"}, function () {
	        collection_obj.isCollect = false;
	        collect_submit();
	      })
	    }
	  })
	}
	
	//收藏提交
	function collect_submit(){
	  var sub_data = {};
	  var sub_url = 'collectCondition';
	  var is_name = true;
	  if(collection_obj.isCollect){
	    if(!$('#collect_Name').val()){
	      is_name = false;
	    }
	    sub_data = {
	      'menuUrl': collection_obj.menuUrl,
	      'filterName': $('#collect_Name').val(),
	      'defectReport': JSON.stringify(collection_obj.search),
	    }
	  }else{
	    sub_data = { id : $("#projectType2").val()};
	    sub_url = 'updateDefectReport';
	  };
	  if(!is_name) {
	    layer.alert('请填写方案名称!', {
	      icon: 0,
	    })
	    return;
	  };
	  $("#loading").css('display', 'block');
	  $.ajax({
	    type: "post",
	    url: "/report/defectReport/" + sub_url,
	    dataType: "json",
	    data: sub_data,
	    success: function (data) {
	        if(collection_obj.isCollect){
	          if (data.code == 1) {
		          $(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
		          layer.alert('收藏成功!', {
		            icon: 1,
		          })
		          $('#collect_Modal').modal('hide');
	          }
	        }else{
	          clearSearch();
		      $(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
		      layer.alert('已取消收藏!', {
		        icon: 1,
		      })
		    }
	        getCollection();
	        $("#loading").css('display', 'none');
	    },
	    error: function () {
	      $("#loading").css('display', 'none');
	      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2 });
	    }
	  });
	}
	
	//  获取收藏信息
	function getCollection() {
	  $("#projectType2").empty();
	  $("#projectType2").append('<option value="">选择收藏方案</option>');
	  $("#loading").css('display','block');
	  $.ajax({
	    url:"/report/defectReport/selectDefectReportList",
	    dataType:'json',
	    type:'post',
	    data:{
	      menuUrl:'../report/toDefectReport',
	    },
	    success : function(data){
	      if(data.length){
	        data.map(v=>{
	          $("#projectType2").append(`<option value="${v.id}">${v.filterName}</option>`);
	        })
	      }
	      $("#projectType2").selectpicker('refresh');
	      $("#loading").css('display','none');
	    },
	    error:function(){
	      $("#loading").css('display','none');
	      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	    }
	  })
	}
	
	//切换收藏
	function tab_option(This){
	  clearSearch();
	  var _id = $(This).val();
	  if(!_id) {
		  $(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
		  return;
	  };
	  is_check_Collect_flag = true;
	  $("#loading").css('display','block');
	  $.ajax({
	    url:"/report/defectReport/selectDefectReportById",
	    dataType:'json',
	    type:'post',
	    data:{
	      id:_id,
	    },
	    success : function(data){
	      var msg = JSON.parse(data.favoriteContent);
	      if(msg){
	    	for(var i=0;i<msg.length;i++){
	          if (msg[i].type == "select") {
	            for (var key in msg[i].value) {
	              $("#" + key).val(change_str_num(msg[i].value[key]));
	            }
	          } else {
	            for (var key in msg[i].value) {
	              $("#" + key).val(msg[i].value[key]);
	            }
	          }
	        }
	        $(".selectpicker").selectpicker('refresh');
	        $(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
	        searchInfo();
	      }
	      $("#loading").css('display','none');
	    },
	    error:function(){
	      $("#loading").css('display','none');
	      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
	    }
	  })
	}
	
	//小于10转换数字
	function change_num(val){
		return val > 9 ? val : +val
	}
	
