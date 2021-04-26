/**
 * Description: 报表中心-测试任务报表
 * Author:
 * Date: 2020/3/25
 */

	var iframe_flag = true;//是否请求
	var is_check_Collect_flag = true;//是否继续收藏
	var collection_obj = {
	  menuUrl : '../report/toTestWorkReport',
	  filterName : '',
	  search : [],
	  isCollect : false
	};
	var iframeUrl = '/report/ureport/preview?_u=s3:';//ureport2预览统一url
	var report_1 = 'test_1.ureport.xml&_i=1&_r=1';//测试任务状态饼图参数
	var report_1b = 'testingTasksList_1.ureport.xml&_i=1&_r=1';//测试任务状态表格参数
	var report_2 = 'test_2.ureport.xml&_i=1&_r=1';//执行人对应的测试任务数量参数
	var report_3 = 'test_3.ureport.xml&_i=1&_r=1';//系统对应的测试任务数量参数
	$(document).ready(function () {
	    system_req();
	    downOrUpButton();
	    other();
	    collect_handle();
	    getCollection(); 
	    change_report({order:0});
	    buttonClear();
	});
	
	//单个条件变化，每次只刷新数据改变的图形，其他图形不变,order表示第几个iframe，0表示所有
	function change_report({systemId,defectStatus,createDate,endDate,
		windowDate,endWindowDate,commissioningWindowId,order}){
	  $("#loading").css('display','block');
	  var systemIds_ = $('#systemIds').val() || '';
	  var defectStatus_ = $('#defectStatus').val() || '';
	  var system = systemIds_ && systemIds_.join(',');
	  var status = defectStatus_ && defectStatus_.join(',');
	  var createDate_ = $('#defectDate').val() && $('#defectDate').val().split(' - ');
	  var windowDate_ = $('#commitWindowDate').val() && $('#commitWindowDate').val().split(' - ');
	  var WindowId = '';
	  if(defectStatus){
	    status = defectStatus;
	  }
	  if(commissioningWindowId){
	    WindowId = commissioningWindowId;
	  }
	  if(systemId){
	    system = systemId;
	  }
	  var iframeParams= '&systemId='+system+'&defectStatus='+status+
	      '&createDate='+isValueNull(createDate_[0])+'&endDate='+isValueNull(createDate_[1])
				+'&windowDate='+isValueNull(windowDate_[0])+'&endWindowDate='+
				isValueNull(windowDate_[1])+'&commissioningWindowId='+WindowId;
	  if(order == 1){
	    $('#iframe_chart1').attr('src',iframeUrl + report_1 + iframeParams );
	    $('#iframe_chart1_a').attr('href',iframeUrl + report_1 + iframeParams );
	    
	    $('#iframe_chart1b').attr('src',iframeUrl + report_1b + iframeParams );
	    $('#iframe_chart1b_a').attr('href',iframeUrl + report_1b + iframeParams );
	  }else if(order == 2){
	    $('#iframe_table1').attr('src',iframeUrl + report_2 + iframeParams );
		$('#iframe_table1_a').attr('href',iframeUrl + report_2 + iframeParams );
	  }else if(order == 3){
	    $('#iframe_table2').attr('src',iframeUrl + report_3 + iframeParams );
	    $('#iframe_table2_a').attr('href',iframeUrl + report_3 + iframeParams );
	  }else if(order == 0){
		$('#iframe_chart1').attr('src',iframeUrl + report_1 + iframeParams );
	    $('#iframe_table1').attr('src',iframeUrl + report_2 + iframeParams );
	    $('#iframe_table2').attr('src',iframeUrl + report_3 + iframeParams );
	    
	    $('#iframe_chart1b').attr('src',iframeUrl + report_1b + iframeParams );
	    $('#iframe_chart1b_a').attr('href',iframeUrl + report_1b + iframeParams );
			
		$('#iframe_chart1_a').attr('href',iframeUrl + report_1 + iframeParams );
	    $('#iframe_table1_a').attr('href',iframeUrl + report_2 + iframeParams );
	    $('#iframe_table2_a').attr('href',iframeUrl + report_3 + iframeParams );
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
	    var defectStatus = $('#defectStatus').val() || '';
	    $('#defectStatus_table1').val(defectStatus);
	    $('#defectStatus_chart1').val(defectStatus);
	    $('#system_table2').val(systemIds);
	    $('.selectpicker').selectpicker('refresh');
	    change_report({order:0});
	}
	
	//清空
	function clearSearch() {
	  $('#systemIds').val('');
	  $('#defectStatus').val('');
	  $('#defectDate').val('');
	  $('#commitWindowDate').val('');
	  $(".collection").children("span").removeClass("fa-heart").addClass("fa-heart-o");
	  $('.selectpicker').selectpicker('refresh');
	}
	
	//其他配置
	function other() {
	  var search_ele = ['systemIds','defectStatus','defectDate','commitWindowDate'];
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
	
	  $('#defectStatus_chart1').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_chart1').val() && $('#defectStatus_chart1').val().join(',');
	    var commitWindowId = $('#commitWindowId_chart1').val();
	    change_report({commissioningWindowId:commitWindowId,defectStatus:status,order:1});
	  })
	  $('#commitWindowId_chart1').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_chart1').val() && $('#defectStatus_chart1').val().join(',');
	    var commitWindowId = $('#commitWindowId_chart1').val();
	    change_report({commissioningWindowId:commitWindowId,defectStatus:status,order:1});
	  })
	
	  $('#defectStatus_table1').on('change', function () {
	    if (!iframe_flag) return;
	    var status = $('#defectStatus_table1').val() && $('#defectStatus_table1').val().join(',');
	    change_report({defectStatus:status,order:2});
		})
		
	  $('#system_table2').on('change', function () {
	    if (!iframe_flag) return;
	    var system = $('#system_table2').val() && $('#system_table2').val().join(',');
	    change_report({systemId:system,order:3});
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
	            $('#system_table2').append(`
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
	    $("#commitWindowId_chart1").val(commissioningWindowId);
	    $("#commitWindowName_chart1").val(selectContent.windowName);
	    var status = $('#defectStatus_table1').val() && $('#defectStatus_table1').val().join(',');
	    change_report({commissioningWindowId:commissioningWindowId,defectStatus:status,order:1});
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
	      menuUrl:collection_obj.menuUrl,
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
	
