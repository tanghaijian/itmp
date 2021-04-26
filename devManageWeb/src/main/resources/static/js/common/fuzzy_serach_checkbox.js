// 防抖
function _debounce(fn, delay) {
	var timer;
	return function () {
		var context = this;
		var args = arguments;
		clearTimeout(timer);
		timer = setTimeout(function () {
			fn.apply(context, args);
		}, delay)
	}
}

//搜索多选实例
/*var remarkUser = {
    'ele': 'remarkUser',//搜索框
    'url': '/system/user/getAllUserModal',//模糊搜索请求url
    'params':{            
        'systemName':'',//模糊匹配参数                                    匹配参数必须放在第一位
        'notWithUserIds':'',   //已选排除id     参数id必须放在第二位
        'pageNumber':1,
        'pageSize':20
    },
    'name':'userName',//展示字段名
    'id':'id',//展示字段id
    'top':'26px',//搜索列表top值(默认26)
	'userId':$('#remarkUserId'),//存id元素
	'checkbox':true,//多选
};
fuzzy_search(remarkUser);*/

//创建动态搜索列表(多选
function create_search_list_checkbox(ele) {
	var _ele = $(`#${ele}`);
	_ele.parent().addClass('_flex_c  _form-control');
	_ele.addClass(`bor_no ${ele} search_checkbox_ipt`).attr({
		'id': `${ele}`,
	});
	_ele.before(`<span class="${ele}_tit input_tit" id='${ele}_tit'></span>`);
	_ele.after(`
          <ul class="search_list" id='${ele}_body'>
                          <li>
                  <h6 class="red">已选项</h6>
                  <ul id='${ele}_checked_list' class="o_list"></ul>
                  
              </li>
              <li>
                  <ul id='${ele}_list' class="Candidate_item"></ul>
              </li>
          </ul>
      `);
}

//模糊搜索
function fuzzy_search(obj) {
	if (Object.keys(obj).includes('checkbox')) {
		fuzzy_search_checkbox(obj);
	} else {
		fuzzy_search_radio(obj);
	}
}
//搜索多选
function fuzzy_search_checkbox({ ele, url, params, userId, top, left, name, id, saveId, title, change, create, row, not_removeAttr,fixed }) {
	var _ele = $(`#${ele}`);
	var tit = `#${ele}_tit`;
	var body = `#${ele}_body`;
	var list = `#${ele}_list`;
	var checked = `#${ele}_checked_list`;
	var notId_flag = true;
	if (change || create) {

	} else {
		_ele.parent().addClass('_flex_c  _form-control');
		if (not_removeAttr) {

		} else {
			_ele.removeClass('form-control').addClass('_form-control');//
		}
		_ele.addClass(`bor_no  ${ele} search_checkbox_ipt`).attr({
			'id': `${ele}`,
			'autocomplete': "off",
		});
		if (row) {
		} else {
			_ele.before(`<span class="${ele}_tit input_tit" id='${ele}_tit'></span>`);
			_ele.after(`
				<ul class="search_list" id='${ele}_body'>
						<li>
								<h6 class="red">已选项</h6>
								<ul id='${ele}_checked_list' class="o_list"></ul>
						</li>
						<li>
								<ul id='${ele}_list' class="Candidate_item"></ul>
						</li>
				</ul>
			`);
		}
	}
	_ele.on('keyup focus', _debounce(function () {
		if (!$(tit).text()) {
			$(checked).empty();
		}
		if(userId.val()){
			$(body).css({ top : '49px' });
		}else{
			$(body).css({ top });
		}
		//已选列表展示
		if (!$(checked).children().length) {
			$(checked).prev().hide();
			$(body).hide();
		} else {
			$(checked).prev().show();
			$(body).show();
		}
		if(!userId.val()){
			$(body).hide();
		}
		if ($.trim(_ele.val())) {
			if (row) {
			} else {
				$(body).css({ top });
			}
			params[Object.keys(params)[0]] = $.trim(_ele.val());
			params['pageSize'] = 99;
			if (saveId) {
				params['notWithUserIds'] = saveId;
			} else {
				params['notWithUserIds'] = userId.val();
			}
			if (!notId_flag) {
				params['notWithUserIds'] = '';
			}
			//搜索请求
			$.ajax({
				url: url,
				datatype: 'json',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				method: "post",
				data: params,
				success: function (data) {
					if(userId.val()){
						$(body).css({ top : '49px' });
					}else{
						$(body).css({ top });
					}
					if (data.rows) {
						$(list).empty();
						$(list).append(`<li class="red p_l_5">总共:${data.rows.length}条</li>`);
						data.rows.map(function (res) {
							$(list).append(`<li class="${ele}_search_item search_list_item" userid="${res[id]}" title="${res[name]}" data='${JSON.stringify(res)}'>${res[name]}</li>`);
							$(body).show();
						})
					} else {
						$(body).hide();
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
				}
			});
		} else {
			if (row) {
			} else {
				userId.removeAttr('data');
//				$(body).show();
				$(list).empty();
			}
		}
	}, 300))
	//赋值
	$(body).on('click', `.${ele}_search_item`, function () {
		if (row) {
		} else {
			var _flag = true;
			var val = $(this).text();
			var id = $(this).attr('userid');
			var data = $(this).attr('data');
			$(this).remove()
			_ele.val('');
			$(`.${ele}_tag_item`).each(function (idx, val) {
				if ($(val).attr('userid') == id) {
					layer.msg('不能重复添加!!!', {
						icon: 2,
						title: '提示信息'
					})
					_flag = false;
				}
			})
			if (!_flag) return
			$(".btn_clear").css("display", "none");
			$(checked).append(`<li class="${ele}_tag_item tag_item ${ele}_tag_item${id}" userid="${id}" title="${val}" data='${data}'>${val}</li>`);
			var len = $(checked).children().length;
			$(tit).text(`已选${len}项`);
			$(tit).show();
			//隐藏id赋值
			if (userId.val()) {
				var _str = '';
				let _tit = '';
				$(`.${ele}_tag_item`).each(function (idx, val) {
					if ($(val).attr('userid')) {
						_str += $(val).attr('userid') + ',';
					}
					_tit += $(val).text() + ',';
				})
				userId.val(_str.substr(0, _str.length - 1)).change();
				
				$(tit).attr('title',_tit);
			} else {
				userId.val(id).change();
			}
			_ele.val('').change();
			$(body).css({ top : '49px' });
			notId_flag = true;
		}
	})
	_ele.on('blur', function () {
		if (!userId.val()) {
			_ele.val('');
			notId_flag = false;
		}
	})
	//点击空白 关闭搜索框
	$(document).on('click', function (e) {
		if (!$(e.target).hasClass(`${ele}_search_item`) && !$(e.target).hasClass(ele) && !$(e.target).hasClass(`${ele}_tag_item`)) {
			$(body).hide();
		}
		//删除全部
		if ($(e.target).hasClass(`${ele}_tit`)) {
			$(tit).text('').hide();
			$(checked).empty();
			userId.val('').change();
			_ele.val('').change();
			$(tit).attr('title','');
		}
	})
	//删除已选
	$(document).on('click', `.${ele}_tag_item`, function (idx) {
		$(this).remove().change();
		if (row) {
			
		} else {
			var len = $(checked).children().length;
			$(tit).text(`已选${len}项`);
			if (!$(checked).children().length) {
				$(checked).prev().hide();
				$(tit).text('').hide();
				userId.val('');
			} else {
				var _str = '';
				let _tit = '';
				$(`.${ele}_tag_item`).each(function (idx, val) {
					if ($(val).attr('userid')) {
						_str += $(val).attr('userid') + ',';
					}
					_tit += $(val).text() + ',';
				});
				userId.val(_str.substr(0, _str.length - 1));
				$(tit).attr('title',_tit);
			}
			_ele.val('').change();
			$(body).hide();
		}
	})
}
//搜索多选清空
function search_checkbox_clear(ele, userId, row) {//输入框id,存id元素
	if (row) {
		
	} else {
		$(`#${ele}`).val('');
		$(`#${ele}`).attr('title','');
		userId.val('');
		$(`#${ele}_tit`).text('').attr('title','');
		$(`#${ele}_tit`).hide();
		$(`#${ele}_body`).hide();
		$(`#${ele}_list`).empty();
		$(`#${ele}_checked_list`).empty();

	}
}
//搜索多选赋值
function search_checkbox_give(ele, userId, list, fieldname, fieldid, row, is_validate) {//输入框id,存id元素,数组数据,展示字段名,展示字段id,是否清空后验证
	$(`#${ele}`).val('');
	$(`#${ele}`).parent().addClass('_flex_c  _form-control');
	var id_join = '';
	if (list != null && list.length > 0) {
		$.each(list, function (index, element) {
			if (element[fieldid] != undefined && element[fieldid] != null && element[fieldname] != undefined && element[fieldname] != null) {
				id_join += element[fieldid] + ',';
				if (row) {
					element && $(`#${ele}`).before(`<span class="${ele}_tag_item input_tit _show _margin_4" userid="${element[fieldid]}">${element[fieldname]}</span>`);
				} else {
					element && $(`#${ele}_checked_list`).append(`<li class="tag_item ${ele}_tag_item" userid="${element[fieldid]}">${element[fieldname]}</li>`);
				}
			}
		})
		if (id_join != '') {
			id_join && userId.val(id_join.substr(0, id_join.length - 1));//存多选id
			if (is_validate) {
				userId.change();
			}
		}

		if (row) {

		} else {
			var len = list.length;
			len && $(`#${ele}_tit`).show().text(`已选${len}项`);//已选提示
		}

	}
}


