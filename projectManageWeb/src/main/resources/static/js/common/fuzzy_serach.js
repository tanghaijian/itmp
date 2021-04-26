
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

//搜索单选
function fuzzy_search_radio(ele, url, top, name, id, userId, out  ) {
	var ele_name = '#'+ele;
	var _ele = $(ele_name);
	var list = ele_name+'_list';
	_ele.after('<ul class="search_list" id="'+ele+'_list"></ul>');
	_ele.attr({'autocomplete':"off" });
	$(list).css({ top:top });
	_ele.on('keyup enter',_debounce(function () {
		if ($.trim(_ele.val())) {
			$(list).empty();
			$(list).append('<li class="search_list_item" style="min-height:50px;"><img class="loadingImg" src="../static/images/loading/loading.gif" /></li>');
			$(list).show();
			$.ajax({
				url: url,
				datatype: 'json',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				method: "post",
				data: {
					documentName:$.trim(_ele.val())
				},
				success: function (data) {
					if (data) {
						$(list).empty();
						data.map(function (res) {
							$(list).append('<li class="'+ele+'_search_item search_list_item" data-id="'+res.id+'">'+res.documentName+'</li>');
							$(list).show();
						})
					} else {
						$(list).hide();
					}
				},
				error: function () {
					layer.alert("系统内部错误，请联系管理员 ！！！", { icon: 0 });
				}
			})
		} else {
			userId.val('');
			_ele.val('');
		}
	},1000))
	_ele.on('change', function () {
		if (!userId.val()) {
			userId.val('');
			_ele.val('');
		}
		if (userId.val() && _ele.attr('username') && _ele.attr('username') != _ele.val()) {
			userId.val('');
			_ele.val('').change();
		}
	})
	if (!out) {
		$(list).on('click', '.'+ele+'_search_item', function () {
			var val = $(this).text();
			var id = $(this).data('id');
			userId.val(id);
			_ele.val(val).attr('username',val).change();
			$(list).hide();
		})
	}
	$(document).on('click', function (e) {
		if (!$(e.target).hasClass(ele) && !$(e.target).hasClass(ele+'_search_item')) {
			$(list).hide();
		}
	})

}
//搜索单选清空
function search_radio_clear(ele, userId) {//输入框id,存id元素
	$('#'+ele).val('').removeAttr('username');
	userId.val('');
}
//搜索单选赋值
function search_radio_give(ele, value, userId, id) {//输入框id,值,存id元素,id
	$('#'+ele).val(value).attr('username',value);
	userId.val(id);
}

