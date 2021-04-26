
(function ($) {
	var page = {};
	var pageObj = {};
	var pageReady = false;
	var queryParams = {};
	/*var token=$("#tokenid").val();*/
	$.fn.pageInit = function (opts) {
		var $this = $(this);
		page.token = typeof opts == "undefined" ? "" : (typeof opts.token == "undefined" ? "" : opts.token);
		page.url = typeof opts == "undefined" ? "" : (typeof opts.url == "undefined" ? "" : opts.url);
		page.callback = typeof opts == "undefined" ? "" : (typeof opts.callback == "undefined" ? "" : opts.callback);
		page.template = typeof opts == "undefined" ? "../systemui/xml/pagination.xml" : (typeof opts.template == "undefined" ? "../systemui/xml/pagination.xml" : opts.template);
		page.queryParam = typeof opts == "undefined" ? {} : (typeof opts.queryParam == "undefined" ? {} : opts.queryParam);
		if (page.template == "")
			return;
		$.ajax({
			type: "GET",
			url: page.template,
			dataType: "xml",
			beforeSend: function (request) {
				request.setRequestHeader("token", token);
			},
			success: function (retXML) {
				var pageHtml = $(retXML).find('#pagination').text();
				$this.empty();
				$this.append(pageHtml);
				initPageObj($this);
				defaultInitPage();
				pageReady = true;
				$this.query(page.queryParam);
			},
			error: function (data, textstatus, e) {
				// console.log("load pagination error:" + e);
			}
		});
	};

	function initPageObj($this) {
		// pageObj = {
		// 	pageSizeObj :  $this.find("select[name='pageSize']"),
		// 	pageIndexObj : $this.find("span[name='pageIndex']"),
		// 	pageFirstObj : $this.find("a[name='page_first']"),
		// 	pagePrevObj : $this.find("a[name='page_prev']"),
		// 	pageNextObj : $this.find("a[name='page_next']"),
		// 	pageLastObj : $this.find("a[name='page_last']"),
		// 	totalPageObj : $this.find("span[name='totalPage']"),
		// 	pageJumpNumObj : $this.find("input[name='pageJumpNum']"),
		// 	pageGoObj : $this.find("a[name='ucStatusFLGo']"),
		// 	startIndexObj : $this.find("span[name='startIndex']"),
		// 	endIndexObj : $this.find("span[name='endIndex']"),
		// 	totalRowNumObj : $this.find("span[name='totalRowNum']"),
		// }
		pageObj = {
			pageFirstObj: [$this.find("a[name='page_first']"), 'first'],
			pagePrevObj: [$this.find("a[name='page_prev']"), 'prev'],
			pageNextObj: [$this.find("a[name='page_next']"), 'next'],
			pageLastObj: [$this.find("a[name='page_last']"), 'last'],
			pageGoObj: $this.find("a[name='ucStatusFLGo']"),
			pageSizeObj: $this.find("select[name='pageSize']"),
			pageIndexObj: $this.find("span[name='pageIndex']"),
			totalPageObj: $this.find("span[name='totalPage']"),
			pageJumpNumObj: $this.find("input[name='pageJumpNum']"),
			startIndexObj: $this.find("span[name='startIndex']"),
			endIndexObj: $this.find("span[name='endIndex']"),
			totalRowNumObj: $this.find("span[name='totalRowNum']"),
		}
		for (var key in pageObj) {
			if(key == 'pageFirstObj' || key == 'pagePrevObj' || key == 'pageNextObj' || key == 'pageLastObj'){
				pageObj[key][0].unbind().bind('click', function () {
					if ($(this).hasClass("disabled"))
						return false;
					jump(pageObj[key][1],$this);
				});
			}
		}
		// pageObj.pageFirstObj.unbind().bind('click', function () {
		// 	if ($(this).parent().hasClass("disabled"))
		// 		return false;
		// 	jump('first', $this);
		// });
		// pageObj.pagePrevObj.unbind().bind('click', function () {
		// 	if ($(this).parent().hasClass("disabled"))
		// 		return false;
		// 	jump('prev', $this);
		// });
		// pageObj.pageNextObj.unbind().bind('click', function () {
		// 	if ($(this).parent().hasClass("disabled"))
		// 		return false;
		// 	jump('next', $this);
		// });
		// pageObj.pageLastObj.unbind().bind('click', function () {
		// 	if ($(this).parent().hasClass("disabled"))
		// 		return false;
		// 	jump('last', $this);
		// });
		pageObj.pageGoObj.unbind().bind('click', function () {
			var val = pageObj.pageJumpNumObj.val();
			if (isNaN(parseInt(val, 10)) == false)
				jump('go', $this);
		});
		pageObj.pageSizeObj.unbind().bind('change', function () {
			initNotSizeObj();
			$this.query(queryParams);
		});
	}

	function initNotSizeObj() {
		pageObj.pageIndexObj.text(1);
		pageObj.totalPageObj.text(0);
		pageObj.pageJumpNumObj.val("");
		pageObj.endIndexObj.text(0);
		pageObj.startIndexObj.text(0);
		pageObj.totalRowNumObj.text(0);
	}

	function defaultInitPage() {
		pageObj.pageSizeObj.val(10);
		pageObj.pageIndexObj.text(1);
		pageObj.totalPageObj.text(0);
		pageObj.pageJumpNumObj.val("");
		pageObj.endIndexObj.text(0);
		pageObj.startIndexObj.text(0);
		pageObj.totalRowNumObj.text(0);
	}

	function jump(flag, $this) {
		$this.query(queryParams, flag);
	}

	$.fn.query = function (opts, flag) {
		if (pageReady == false) {
			alert("正在初始化，请稍后再试");
			return false;
		}

		var url = page.url;
		var pageIndex = pageObj.pageIndexObj.text();
		var pageSize = pageObj.pageSizeObj.val();
		if (typeof flag != "undefined") {
			switch (flag) {
				case 'first':pageIndex = page.firstPage;break;//记录的第一页
				case 'prev':pageIndex = page.prevPage;break;
				case 'next':pageIndex = page.nextPage;break;
				case 'last':pageIndex = page.lastPage;break;
				case 'go':pageObj.pageJumpNumObj.val();break;
				default:pageIndex = page.firstPage;break;
			}
			// if (flag == 'first')
			// 	pageIndex = page.firstPage;//记录的第一页
			// else
			// 	if (flag == 'prev')
			// 		pageIndex = page.prevPage;
			// 	else
			// 		if (flag == 'next')
			// 			pageIndex = page.nextPage;
			// 		else
			// 			if (flag == 'last')
			// 				pageIndex = page.lastPage;
			// 			else
			// 				if (flag == 'go')
			// 					pageIndex = pageObj.pageJumpNumObj.val();
		}
		if (typeof opts == "undefined")
			opts = {};
		queryParams = opts;
		if (parseInt(pageIndex, 10) < 1)
			pageIndex = 1;
		var data = $.extend({}, opts, { 'pageIndex': pageIndex, 'pageSize': pageSize });
		var callback = page.callback;
		$.ajax({
			type: "POST",
			url: url,
			dataType: "json",
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			data: data,
			beforeSend: function (request) {
				request.setRequestHeader("token", token);
			},
			success: function (msg) {
				if (msg.status == 1) {
					var pageInfo = JSON.parse(msg.data);
					var dataList = pageInfo.list;
					initPage(pageInfo);
					if (typeof page.callback == "function") {
						page.callback(dataList, pageIndex, pageSize);
					}

				} else {
					alert(msg.errormessage);
				}
				$("#loading-container").hide();
			},
			error: function (msg, status, throwable) {
				console.log("处理失败！" + throwable);
				$("#loading-container").hide();
			}


		});
	};

	function initPage(pageInfo) {

		//记录上一页，下一页，首页，末尾页
		page.prevPage = pageInfo.prePage;
		page.nextPage = pageInfo.nextPage;
		page.firstPage = pageInfo.firstPage;
		page.lastPage = pageInfo.lastPage;
		var pageNum = pageInfo.pageNum;
		pageObj.pageIndexObj.text(pageNum);
		var totalPage = pageInfo.pages;
		pageObj.totalPageObj.text(totalPage);
		var totalRowNum = pageInfo.total;
		pageObj.totalRowNumObj.text(totalRowNum);
		var startRow = pageInfo.startRow;
		pageObj.startIndexObj.text(startRow);
		var endIndex = pageInfo.endRow;
		pageObj.endIndexObj.text(endIndex);
		if (totalPage <= 1) {
			pageObj.pageFirstObj[0].parent().addClass("disabled");
			pageObj.pagePrevObj[0].parent().addClass("disabled");
			pageObj.pageNextObj[0].parent().addClass("disabled");
			pageObj.pageLastObj[0].parent().addClass("disabled");
		} else if (pageNum == 1) {
			pageObj.pageFirstObj[0].parent().addClass("disabled");
			pageObj.pagePrevObj[0].parent().addClass("disabled");
			pageObj.pageNextObj[0].parent().removeClass("disabled");
			pageObj.pageLastObj[0].parent().removeClass("disabled");
		} else if (pageNum < totalPage) {
			pageObj.pageFirstObj[0].parent().removeClass("disabled");
			pageObj.pagePrevObj[0].parent().removeClass("disabled");
			pageObj.pageNextObj[0].parent().removeClass("disabled");
			pageObj.pageLastObj[0].parent().removeClass("disabled");
		} else if (pageNum == totalPage) {
			pageObj.pageFirstObj[0].parent().removeClass("disabled");
			pageObj.pagePrevObj[0].parent().removeClass("disabled");
			pageObj.pageNextObj[0].parent().addClass("disabled");
			pageObj.pageLastObj[0].parent().addClass("disabled");
		}
	}

})(jQuery);

