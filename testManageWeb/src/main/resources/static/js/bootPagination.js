var rows = [];
var sortField;
function getPageInfo() {
	var pageIndexNum = 1;
	var pageSizeNum = 10;
	var totalRowNum1 = 0;
	if(toStr($("#pageIndex").text()) != "")
		pageIndexNum = $("#pageIndex").text();
	if(toStr($("#pageSize").val()) != "")
		pageSizeNum = $("#pageSize").val();
	if(toStr($("#totalRowNum").text()) != "")
		totalRowNum1 = $("#totalRowNum").text();
	var pageInfo = "pageInfo={pageIndex:"+pageIndexNum;
	pageInfo += ",pageSize:"+pageSizeNum;
	pageInfo += ",totalRowNum:"+totalRowNum1+"}";
	return pageInfo;
}



function getPageJson(){

	var pageIndexNum = 1;
	var pageSizeNum = 10;
	var totalRowNum1 = 0;
	if(toStr($("#pageIndex").text()) != "")
		pageIndexNum = $("#pageIndex").text();
	if(toStr($("#pageSize").val()) != "")
		pageSizeNum = $("#pageSize").val();
	if(toStr($("#totalRowNum").text()) != "")
		totalRowNum1 = $("#totalRowNum").text();
	var pageInfo = {pageIndex:pageIndexNum,pageSize:pageSizeNum,totalRowNum:totalRowNum1};
	return pageInfo;
}
function firstLoadPage() {
	$("#pageIndex").text(1);
	rows = [];
}
function pushRowData(row){
	var truncated = $("#dg").attr("truncated");
	if (truncated && truncated == "true") {
		var ths = $("#dg").prev().find("tr > th");
		for (var i = 0; i < ths.length; i++) {
			var field = $(ths[i]).attr("field");
			if (field && field != "") {
				var dataWidth = $(ths[i]).attr("dataWidth");
				if (dataWidth && dataWidth != "" && row[field]!=null) {
					var _value = row[field].toString();
					if (_value.length > dataWidth) {
						row[field] =   "<div title='"+_value+"'>"+_value.substr(0, dataWidth)+"......</div>";
					}
				}
			}
		}
	}
	rows.push(row);
}
function initPageInfo(totalRowNum) {
	if (totalRowNum == 0) {
		$("#pageIndex").text(0);
		$("#startIndex").text(0);
		$("#endIndex").text(0);
		$("#totalPage").text(0);
		$("#totalRowNum").text(0);
	} else {
		var totalPage = Math.ceil(totalRowNum / $("#pageSize").val());
		$("#totalPage").text(totalPage);
		$("#totalRowNum").text(totalRowNum);
	}
}
function jumpTo(pageIndex) {
	$("#dg").ready(function(){
		// only when the jumping page is not current page, and the jumping page index is allowed
		//, and the jumping page index is small than total page index
		var totalPage = $("#totalPage").text();
		if (pageIndex != $("#pageIndex").text() && pageIndex > 0 && totalPage > 0 && pageIndex <= totalPage) {
			var totalRowNum = $("#totalRowNum").text();
			if (rows.length < totalRowNum) {
				$("#pageIndex").text(pageIndex);
				rows = [];
				query();
			} else {
				calculatePageInfo(pageIndex);
				paintTableByBrowser();
				iconChange();
			}
		}
		
	});
	
	bindSortFunction1();
}
function calculatePageInfo(pageIndex) {
	$("#pageIndex").text(pageIndex);
	var totalRowNum = $("#totalRowNum").text();
	if (totalRowNum > 0) {
		var pageSize = $("#pageSize").val();
		var pageIndex = $("#pageIndex").text();
		var startIndex = (pageIndex - 1) * pageSize + 1;
		var endIndex = pageIndex * pageSize;
		if (endIndex > totalRowNum) {
			endIndex = totalRowNum;
		}
		$("#startIndex").text(startIndex);
		$("#endIndex").text(endIndex);
	}
}


/**
 * para ：当页面的分页大小不按照rows大小来判断的时候用。比如页面为树形结构的时候，rows包含父亲节点数据，也包含了子节点数据，但此时分页标准按照父亲节点数据来计算。那么rows中的所有数据需要展现出来
 * @param totalRowNum
 * @param para
 * @returns
 */
function paintTable(totalRowNum,para) {
	initPageInfo(totalRowNum);
	calculatePageInfo($("#pageIndex").text());
	var totalRowNum = $("#totalRowNum").text();
	if (rows.length < totalRowNum || (typeof para != "undefined" && para == '1')) {
		paintTableByDB();
	} else {
		paintTableByBrowser();
	}
	iconChange();
}
function paintTableByDB() {
	var ths = $("#dg").prev().find("tr > th");
	var content = "";
	for (var i = 0;i < rows.length; i++) {
		var row = rows[i];
		var tr = "<tr>";
		for (var j = 0; j < ths.length; j++) {
			var field = $(ths[j]).attr("field");
			if (field && field != "") {
				tr += "<td>" + nvl(row[field]) + "</td>";
			}
		}
		tr += "</tr>";
		content += tr;
	}
	$("#dg").empty();
	$("#dg").append(content);
}
function paintTableByBrowser() {
	var ths = $("#dg").prev().find("tr > th");
	var content = "";
	var startIndex = $("#startIndex").text();
	var endIndex = $("#endIndex").text();
	for (var i = 0;i < rows.length; i++) {
		if (i >= startIndex - 1 && i <= endIndex - 1) {
			var row = rows[i];
			var tr = "<tr>";
			for (var j = 0; j < ths.length; j++) {
				var field = $(ths[j]).attr("field");
				if (field && field != "") {
					tr += "<td>" + nvl(row[field])  + "</td>";
				}
			}
			tr += "</tr>";
			content += tr;
		}
	}
	$("#dg").empty();
	$("#dg").append(content);
}
function pageSizeChange(pageSize) {
	$("#pageSize").val(pageSize);
	var totalRowNum = parseInt($("#totalRowNum").text());
	if (rows.length < totalRowNum) {
		$("#pageIndex").text(1);
		searchID();
	} else {
		var totalPage = Math.ceil(totalRowNum / pageSize);
		$("#totalPage").text(totalPage);
		calculatePageInfo(1);
		paintTableByBrowser();
		iconChange();
	}
	bindSortFunction1();
}
function nvl(_value) {
	if (_value == null) {
		return "";
	}
	return _value;
}
function iconChange() {
	var pageIndex = parseInt($("#pageIndex").text());
	var totalPage = parseInt($("#totalPage").text());
	if (totalPage <= 1) {
		$("#page_first").parent().addClass("disabled");
		$("#page_prev").parent().addClass("disabled");
		$("#page_next").parent().addClass("disabled");
		$("#page_last").parent().addClass("disabled");
	} else if (pageIndex == 1) {
		$("#page_first").parent().addClass("disabled");
		$("#page_prev").parent().addClass("disabled");
		$("#page_next").parent().removeClass("disabled");
		$("#page_last").parent().removeClass("disabled");
	} else if (pageIndex < totalPage) {
		$("#page_first").parent().removeClass("disabled");
		$("#page_prev").parent().removeClass("disabled");
		$("#page_next").parent().removeClass("disabled");
		$("#page_last").parent().removeClass("disabled");
	} else if (pageIndex == totalPage) {
		$("#page_first").parent().removeClass("disabled");
		$("#page_prev").parent().removeClass("disabled");
		$("#page_next").parent().addClass("disabled");
		$("#page_last").parent().addClass("disabled");
	}
}
function sort(field, index) {
	var tbody = document.getElementById("dg");
	var tr = tbody.rows;
	var trValue = [];
	for (var i = 0;i < tr.length; i++) {
		trValue[i] = tr[i];
	}
	if (sortField == field) {
		trValue.reverse();
	} else {
		trValue.sort(function(tr1, tr2){
			var value1 = tr1.cells[index].innerHTML;
			var value2 = tr2.cells[index].innerHTML;
			//lee modify 2013-5-8
			if(value1.indexOf("<SPAN>")!=-1){
				value1=value1.substring(value1.indexOf("<SPAN>"),value1.indexOf("</SPAN>"));
				value2=value2.substring(value2.indexOf("<SPAN>"),value2.indexOf("</SPAN>"));
			}
			if(value1.indexOf("<span>")!=-1){
				value1=value1.substring(value1.indexOf("<span>"),value1.indexOf("</span>"));
				value2=value2.substring(value2.indexOf("<span>"),value2.indexOf("</span>"));
			}
			if(!isNaN(value1)&&!isNaN(value2)){
				value1 = parseFloat(value1);
				value2 = parseFloat(value2);
				if(value1 < value2)
					return -1;
				else
					if(value1 == value2)
						return 0;
					else
						if(value1 > value2)
							return 1;
			}
			else
			return value1.localeCompare(value2);
		});
	}
	var fragment = document.createDocumentFragment();
	for (var i=0;i<trValue.length;i++) {
		fragment.appendChild(trValue[i]);
	}
	tbody.appendChild(fragment);
	sortField = field;
}

function bindSortFunction() {
	var tableSortable = $("#dg").attr("sortable");
	if (tableSortable && tableSortable == "true") {
		$("#dg").prev().find("tr > th").each(function(index){
			var field = $(this).attr("field");
			if (field && field != "") {
				var thSortable = $(this).attr("sortable");
				if (thSortable && thSortable == "true") {
					$(this).css("cursor", "pointer");
					$(this).bind("click", function(){
						sort(field, index);
					});
				}
			}
		});
	}
}
$(document).ready(function(){
	bindSortFunction();
});
