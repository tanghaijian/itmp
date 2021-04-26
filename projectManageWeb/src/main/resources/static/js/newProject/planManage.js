$(() => {
	initTable();
})

function to_check_chart(){
	window.location.href = "/projectManageui/newProject/toPlanChart?id=" + $("#project_id").val() + "&name=" + $("#project_name").val();
}

//菜单按钮
function initTable() {
	$.ajax({
		url: '/projectManage/plan/getPlanNumberByNumber',
		type: "post",
		async : false,
		dataType: "json",
		data: {
			"projectId": $("#project_id").val(),
			"planNumber": $("#plan_version").val(),
		},
		beforeSend: function () {
			$("#loading").css('display', 'block');
		},
		success: function (data) {
		 	if (data.status == 1) {
				$('#version_id').text('V'+ $('#plan_version').val() + '.0');
				if (!data.data.length) return;
				$('#defaultMenu_body tbody').empty();
				let test = add_parentIds(data.data);
				tree_table.tree_create_table(test, {
					id: 'id',
					level: 'level',//级别
					isChild: 'isChild',//是否包含字节的
					parentIds: 'parentIds',//父id
					code: 'code',//编号
					name: 'name',//名称
					start: 'start',//计划开始
					end: 'end',//计划结束
					progress: 'progress',//进度（%）
					padding_left: 20,//左边距倍率,默认20
				});
		 	}
		 	$("#loading").css('display', 'none');
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

//将数据组装成jquery treeTable所需结构
function add_parentIds(test){
	let num = '';
	for(let i=0;i<test.length;i++){
		let j = i + 1;
		if(j <= test.length - 1){
			if(test[j]['level'] > test[i]['level']){
				test[i]['isChild'] = true;
				test[j]['parentId'] = test[i]['id'];
				num += test[i]['id'] + ',';				
				test[j]['parentIds'] = num;
			}else{
				test[i]['isChild'] = false;
				num = '';
			}
		}
	}
	return test;
}

//组装生成treeTable
var tree_table = (() => {
		function tree_create_table(data, tree_menu_params) {
			if (!data.length) return;
			$('#tree_table tbody').empty();
			data.map(function (val, idx) {
				tree_table.tree_menu_give('#tree_table tbody', val, val[tree_menu_params['level']], tree_menu_params);
			})
			tree_table.tree_tab_handle();
		}

		function tree_tab_handle() {
			$('.tree_arrows').bind('click', function (e) {
		      if (!$(e.target).hasClass('tree_arrows_bottom') && !$(e.target).hasClass('tree_arrows_right')) return;
		      let _this = $(e.target).parents('tr');
		      let parent_id = _this.data('id');
		      let _lev = _this.attr('level');
		      if ($(e.target).hasClass('tree_arrows_right')) {
		        $(e.target).removeClass('tree_arrows_right').addClass('tree_arrows_bottom');
		        _this.siblings().each(function (idx, val) {
		          if ($(val).hasClass('tree_parent_'+ parent_id) || _lev == 0) {
		            $(val).addClass('_hide');
		          }
		        })
		      } else {
		        $(e.target).removeClass('tree_arrows_bottom').addClass('tree_arrows_right');
		        _this.siblings().each(function (idx, val) {
		          if(_lev == 0){
		        	  if(parseInt($(val).attr('level')) - 1 == _lev){
		        		  $(val).removeClass('_hide');
				          $(val).find('.tree_arrows_right').removeClass('tree_arrows_right').addClass('tree_arrows_bottom');
		        	  }
		          }else{
		        	  if ($(val).hasClass('tree_parent_'+ parent_id)  && (parseInt($(val).attr('level')) - 1 == _lev)) {
			            $(val).removeClass('_hide');
			            $(val).find('.tree_arrows_right').removeClass('tree_arrows_right').addClass('tree_arrows_bottom');
			          }
		          }
		          
		        })
		      }
		    })
		}

		function tree_menu_give(ele, val, level, tree_menu_params) {
			let _class = 'tree_parent_menu';
			let icon_class = val[tree_menu_params['isChild']] ? 'tree_arrows_right' : '';
			if (level) {
				let ids = val[tree_menu_params['parentIds']];
				let ids_str = '';
				ids && ids.split(',').map(function (value) {
		          if (value) {
		            ids_str += ' tree_parent_' + value
		          }
		        })
		        _class = `${ids_str} tree_parent_menu`;
			}
			let _str = `
				<tr role="row" pid="${val[tree_menu_params['parentIds']]}" class="${_class}" level="${val.level}" data-id="${val[tree_menu_params['id']]}">
					<td>${val[tree_menu_params['code']]}</td>
					<td class="" style="width:40%;padding-left:${(level + 1) * tree_menu_params['padding_left']}px;">
						<div class="_flex_c">
							<div class="tree_arrows  ${icon_class}"></div>
							<div class="tree_select_text">${val[tree_menu_params['name']]}</div>
						</div>
					</td>
					<td>${formate_date(val[tree_menu_params['start']])}</td>
					<td>${formate_date(val[tree_menu_params['end']])}</td>
					<td>${val[tree_menu_params['progress']]}</td>
				</tr>
			`;
			$(ele).append(_str);
		}

		function formate_date(date) {
			if(!date) return '';
			return new Date(date).toISOString().split('T')[0];
		}
		
		return {
			tree_create_table,
			tree_tab_handle,
			tree_menu_give
		}
	})();