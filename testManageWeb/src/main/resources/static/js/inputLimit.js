(function($) {
	$.fn.inputlimiter = function(options) {
		var opts = $.extend({}, $.fn.inputlimiter.defaults, options);
		if (!$(this).parent("div").next("div[name='"+opts.boxId+"']").length) {
			$('<div class="col-sm-3 inline"></div>').insertAfter($(this).parent("div")).attr({name:opts.boxId}).hide();
		}
		var inputlimiterKeyup = function(e) {
				var $this = $(this),count = counter($this.val());
				if (!opts.allowExceed && count > opts.limit) {
					$this.val(truncater($this.val()));
				}
					remText = opts.remTextFilter(opts, count);
					$this.parent("div").next("div[name='"+opts.boxId+"']").html(remText).show();
					//$('#' + opts.boxId).html(remText).show();
		},
			inputlimiterKeypress = function(e) {
				var count = counter($(this).val());
				if (!opts.allowExceed && count > opts.limit) {
					var modifierKeyPressed = e.ctrlKey || e.altKey || e.metaKey;
					if (!modifierKeyPressed && (e.which >= 32 && e.which <= 122) && this.selectionStart === this.selectionEnd) {
						return false;
					}
				}
			},
			
			counter = function(value) {
				if (opts.limitBy.toLowerCase() === "words") {
					return (value.length > 0 ? $.trim(value).replace(/\ +(?= )/g, '').split(' ').length : 0);
				}
				var count = value.length,
					newlines = value.match(/\n/g);
				if (newlines && opts.lineReturnCount > 1) {
					count += newlines.length * (opts.lineReturnCount - 1);
				}
				return count;
			},
			truncater = function(value) {
				if (opts.limitBy.toLowerCase() === "words") {
					return $.trim(value).replace(/\ +(?= )/g, '').split(' ').splice(0, opts.limit).join(' ') + ' ';
				}
				return value.substring(0, opts.limit);
			};
		$(this).each(function(i) {
			var $this = $(this);
			if ((!options || !options.limit) && opts.useMaxlength && parseInt($this.attr('maxlength')) > 0 && parseInt($this.attr('maxlength')) != opts.limit) {
				$this.inputlimiter($.extend({}, opts, {
					limit: parseInt($this.attr('maxlength'))
				}));
			} else {
				if (!opts.allowExceed && opts.useMaxlength && opts.limitBy.toLowerCase() === "characters") {
					$this.attr('maxlength', opts.limit);
				}
				$this.unbind('.inputlimiter');
				$this.bind('keyup.inputlimiter', inputlimiterKeyup);
				$this.bind('keypress.inputlimiter', inputlimiterKeypress);
			}
		});
	};
	$.fn.inputlimiter.remtextfilter = function(opts, count) {
		var remText = opts.remText;
		remText = remText.replace(/\%n/g, count);
		remText = remText.replace(/\%l/g,opts.limit);
		return remText;
	};
	$.fn.inputlimiter.defaults = {
		limit: 255,
		boxAttach: true,
		boxId: 'limiterBox',
		boxClass: 'limiterBox',
		remText: '%n character%s remaining.',
		remTextFilter: $.fn.inputlimiter.remtextfilter,
		remTextHideOnBlur: true,
		remFullText: null,
		limitTextShow: true,
		limitText: 'Field limited to %n character%s.',
		limitTextFilter: $.fn.inputlimiter.limittextfilter,
		zeroPlural: true,
		allowExceed: false,
		useMaxlength: true,
		limitBy: 'characters',
		lineReturnCount: 1
	};
})(jQuery);