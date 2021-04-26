
// 防抖
function _debounce2(fn, delay) {
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
function fuzzy_search_radio2({ele, url, top, name, account, id, userId, rows, out  }) {
	var ele_name = '#'+ele;
	var _ele = $(ele_name);
	var list = ele_name+'_list';
	_ele.after('<ul class="search_list" id="'+ele+'_list"></ul>');
	_ele.attr({'autocomplete':"off" });
	$(list).css({ top:top });
	_ele.on('keyup enter',_debounce2(function () {
		if ($.trim(_ele.val())) {
			$(list).empty();
			$(list).append('<li class="search_list_item" style="min-height:50px;"><img class="loadingImg" src="../static/images/loading/loading.gif" /></li>');
			$(list).show();
			$.ajax({
				url: url,
				datatype: 'json',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				method: "post",
				data : {
					userName:$.trim(_ele.val())
				},
				success: function (data) {
					if (data) {
						$(list).empty();
						data[rows].map(function (res) {
							$(list).append('<li class="'+ele+'_search_item search_list_item" data-id="'+res[id]+'"  data-name="'+res[name]+'">'+res[name]+'&nbsp;&nbsp;-&nbsp;&nbsp;'+res[account]+'</li>');
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
			$(list).hide();
		}
	},1000))
	_ele.on('change', function () {
		if (!userId.val()) {
			_ele.val('');
			_ele.siblings('i').removeClass('glyphicon-ok').addClass('glyphicon-remove').parent().parent().removeClass('has-success').addClass('has-error');
			_ele.parent().parent().find('small').show();
		}
		if (userId.val() && _ele.attr('username') && _ele.attr('username') != _ele.val()) {
			userId.val('');
			_ele.val('');
			_ele.change();
		}
	})
	if (!out) {
		$(list).on('click', '.'+ele+'_search_item', function () {
			var val = $(this).data('name');
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
function search_radio_clear2(ele, userId) {//输入框id,存id元素
	$('#'+ele).val('').removeAttr('username');
	userId.val('');
}
//搜索单选赋值
function search_radio_give2(ele, value, userId, id) {//输入框id,值,存id元素,id
	$('#'+ele).val(value).attr('username',value);
	userId.val(id);
}

