
/* 依赖
 * jquery.jqGrid.js
 * 
 * */


/* 页面添加  */
/*
	<div class="collection" onclick="add_collection(this)" style="width: 150px;position: static;">
		<span class="fa fa-heart-o" style="font-size: 16px;color: #FD70A0;"></span>&nbsp;收藏隐藏列
		<input type="hidden" id="Collection_id" />
	</div>
*/


/* js添加配置  */
/*
	var collection_obj = {
		  menuUrl : '../devtask/toDevTask',  //页面路径
		  filterName : uid,    //当前用户id
		  search : '',           //筛选字段名
		  isCollect : false ,
		  table_list : '#list2' ,	//表格id
	};
*/


/* loadComplete函数内添加  */
/*
	$("#colGroup").find('input').each(function(idx,val){
		if ($(val).is(':checked')) {
			$(collection_obj.table_list).setGridParam().hideCol($(val).attr('value'));
			$(collection_obj.table_list).setGridWidth($('.wode').width());
		}
	})
*/


$(()=>{
	getCollection();
})

//收藏隐藏列
function add_collection(This){
	collection_obj.search = '';
    if ($(This).children("span").hasClass("fa-heart-o")) {
    	$("#colGroup").find('input').each(function(idx,val){
    		if ($(val).is(':checked')) {
    			collection_obj.search += $(val).val() + ',';
    		}
    	})
//    	collection_obj.isCollect = collection_obj.search.length ? true : false;
    	collection_obj.isCollect = true;
		if(collection_obj.isCollect){
			collect_submit();
//			  $('#collect_Name').val('');
//		      $('#collect_Modal').modal('show');
	    }
    } else {
      layer.confirm('确定要取消收藏吗?', {btn: ['确定', '取消'],icon:0, title: "提示"}, function () {
    	  collection_obj.isCollect = false;
    	  collect_submit();
      }, function () {
				layer.close(layer.index);
				$(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
			})
    }
//	console.log(collection_obj.search)
}

//收藏提交
function collect_submit(){
//	  if(!$('#collect_Name').val()) {
//		    layer.alert('请填写方案名称!', {
//		      icon: 0,
//		    })
//		    return;
//      };
	  var sub_data = {};
	  var sub_url = 'collectCondition';
	  if(collection_obj.isCollect){
		    sub_data = {
		      'menuUrl': collection_obj.menuUrl,
//		      'filterName': $('#collect_Name').val(),
		      'filterName': collection_obj.filterName,
		      'defectReport': collection_obj.search,
		    }
	  }else{
		    sub_data = { id : $("#Collection_id").val()};
		    sub_url = 'updateDefectReport'; //取消收藏
	  };
//	  $.ajax({
//		    type: "post",
//		    url: "/report/defectReport/updateDefectReport",
//		    dataType: "json",
//		    data: { id : 70},})
	  
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
//			          $('#collect_Modal').modal('hide');
		          }
		        }else{
		          layer.closeAll('dialog');
			      $(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
//			      layer.alert('已取消收藏!', {
//			        icon: 1,
//			      })
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

//获取收藏信息
function getCollection() {
//  $("#projectType2").empty();
//  $("#projectType2").append('<option value="">请选择隐藏列方案</option>');
  $("#loading").css('display','block');
  $.ajax({
    url:"/report/defectReport/selectDefectReportList",
    dataType:'json',
    type:'post',
    data:{
      menuUrl : collection_obj.menuUrl,
    },
    success : function(data){
      if(data.length){
    	  var msg = data[0].favoriteContent.split(',');
    	  $("#Collection_id").val(data[0].id);
    	  $(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
    	  msg.map(v=>{
//          $("#projectType2").append(`<option value="${v.id}">${v.filterName}</option>`);
        	$("#colGroup").find('input').each(function(idx,val){
	      		if (v == $(val).val()) {
	      			showHideCol($(val).get(0));
	      			$(val).get(0).checked = true;
	      		}
	      	})
        })
      }
//      $("#projectType2").selectpicker('refresh');
      $("#loading").css('display','none');
    },
    error:function(){
      $("#loading").css('display','none');
      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
    }
  })
}

//切换收藏
/*function tab_option(This){
  var _id = $(This).val();
  if(!_id) {
	  $(".collection").children("span").addClass("fa-heart-o").removeClass("fa-heart");
	  return;
  };
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
	    	$("#colGroup").find('input').each(function(idx,val){
	      		if (msg[i] == $(val).val()) {
	      			$(val).get(0).checked = true;
	      		}
	      	})
    	}
        $(".collection").children("span").addClass("fa-heart").removeClass("fa-heart-o");
      }
      $("#loading").css('display','none');
    },
    error:function(){
      $("#loading").css('display','none');
      layer.alert("系统内部错误，请联系管理员！！！", { icon: 2});
    }
  })
}*/

