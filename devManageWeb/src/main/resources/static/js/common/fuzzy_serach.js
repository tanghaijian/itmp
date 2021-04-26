
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

//搜索多选
function fuzzy_search_checkbox({ ele, url, params, userId, top, name, id }) {
	var _params = params;
	var _ele = $(`#${ele}`);
	var tit = `#${ele}_tit`;
	var body = `#${ele}_body`;
	var list = `#${ele}_list`;
	var checked = `#${ele}_checked_list`;
	_ele.parent().addClass('_flex_c  _form-control');
	_ele.addClass(`bor_no ${ele}`).attr({
		'id': `${ele}`,
		'autocomplete':"off",
	});
	_ele.after(`
		<ul class="search_list" id='${ele}_body'>
			<li>
				<ul id='${ele}_list' class=""></ul>
			</li>
		</ul>
	`);	
	$(body).css({ top });
	_ele.on('keyup enter',_debounce(function () {

		//已选列表展示
		if (!$(checked).children().length) {
			$(checked).prev().hide();
			$(body).hide();
		} else {
			$(checked).prev().show();
			$(body).show();
		}
		if ($.trim(_ele.val())) {
			let _top = $(body).parent().outerHeight();
			$(body).css({ top:_top });
			if(userId.val()){
//				_params = {
//					'userAndAccount' : $.trim(_ele.val()),
//					'ids' : userId.val()
//				}
				_params['userAndAccount'] = $.trim(_ele.val());
				_params['ids'] = userId.val();
			}else{
//				_params = {
//					'userAndAccount' : $.trim(_ele.val()),
//				}
				_params['userAndAccount'] = $.trim(_ele.val());
			}
			//搜索请求
			$.ajax({
				url: url,
				datatype: 'json',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				method: "post",
				data:_params,
				success: function (data) {
					if (data) {
						$(list).empty();
						$(list).append(`<li class="search_list_item"><div class="fontWeihgt">姓名</div><div class="fontWeihgt pad_20">用户名</div></li>`);
						data.map(function (res) {
							$(list).append(`
							<li class="${ele}_search_item search_list_item" data-id="${res[id]}" title="${res[name]} -- ${res['userAccount']}">
									<div class="">${res[name]}</div><div class=" pad_20">${res['userAccount']}</div>
							</li>`);
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
			$(body).hide();
			$(list).empty();
		}
	},1000))
	//赋值
	$(body).on('click', `.${ele}_search_item`, function () {
			var val = $(this).children().eq(0).text();
			var id = $(this).data('id');
			var is_same = true;
			if(userId.val().length){
				userId.val().split(',').map(function(i){
					if(i == id){
						is_same = false;
					}
				})
			}
			if(!is_same){
				layer.msg("请勿重复添加！！！", { icon: 2 });
				return;
			}
			_ele.val('');
			$(".btn_clear").css("display", "none");
			_ele.before(`<span class="${ele}_tag_item input_tit _show _margin_4" data-id="${id}">${val}</span>`);	
			var _str = '';
			$(`.${ele}_tag_item`).each(function (idx, val) {
				if ($(val).data('id')) {
					_str += $(val).data('id') + ',';
				}
			})
			userId.val(_str.substr(0, _str.length - 1)).change();
			$(body).hide();
	})
	_ele.on('change', function () {
		if (!userId.val()) {
			_ele.val('');
		}
	})
	//点击空白 关闭搜索框
	$(document).on('click', function (e) {
		if (!$(e.target).hasClass(`${ele}_search_item`) && !$(e.target).hasClass(ele) && !$(e.target).hasClass(`${ele}_tag_item`)) {
			$(body).hide();
		}
	})
	//删除已选
	$(document).on('click', `.${ele}_tag_item`, function (idx) {
		$(this).remove().change();
		var _str = '';
		$(`.${ele}_tag_item`).each(function (idx, val) {
			if ($(val).data('id')) {
				_str += $(val).data('id') + ',';
			}
		});
		userId.val(_str.substr(0, _str.length - 1)).change();
		_ele.val('').change();
	})
}

//搜索多选清空
function search_checkbox_clear(ele, userId) {//输入框id,存id元素
	$(`#${ele}`).val('');
	userId.val('');
	$(`.${ele}_tag_item`).each(function (idx, val) {
		$(val).remove();
	});
	$(`#${ele}_body`).hide();
	$(`#${ele}_list`).empty();
}

//搜索多选赋值
function search_checkbox_give(ele, userId, list, fieldname, fieldid) {//输入框id,存id元素,数组数据,展示字段名,展示字段id
	$(`#${ele}`).val('');
	$(`#${ele}`).parent().addClass('_flex_c  _form-control');
	$(`#${ele}`).removeAttr('class').addClass(`bor_no ${ele}`);
	var id_join = '';
	if (list != null && list.length > 0) {
		$.each(list, function (index, element) {
			id_join += element[fieldid] + ',';
			element && $(`#${ele}`).before(`<span class="${ele}_tag_item input_tit _show _margin_4" data-id="${element[fieldid]}">${element[fieldname]}</span>`);
			
		})
		id_join && userId.val(id_join.substr(0, id_join.length - 1)).change();//存多选id
	}
}