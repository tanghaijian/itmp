var reg = /^([0]|[1-9][0-9]*)$/;
$(function(){
	// 所有的Input标签，在输入值后出现清空的按钮
	$('input[type="text"]').parent().css("position", "relative");
	$('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
	$('input[type="text"]').bind("input propertychange", function () {
		if ($(this).val() != "") {
			$(this).parent().children(".btn_clear").css("display", "block");
		} else {
			$(this).parent().children(".btn_clear").css("display", "none");
		}
	});
	laydate.render({ 
		  elem: '#searchDate'
			  ,type: 'year'
		});
});

function queryData(){
	var time = $("#searchDate").val();
	if(time == ""){
		layer.alert("时间为空",{icon:0});
		return;
	}
	$("#loading").css('display', 'block');
	$.ajax({
		url: "/testManage/testReport/getDevVersionReport",
		method: "post",
		data:{time:time},
		dataType: "json",
		success: function (data) {
			$("#loading").css('display', 'none');
			$(".tableTitle").css('display','block');
			createDevVersionTable(data.summarys);
			$("#copyWordCode").removeClass('hideBlock');
			$("#copyWordCode").attr('data-clipboard-text',data.url);
			var clipboard = new ClipboardJS("#copyWordCode");
			console.log( clipboard )
		    clipboard.on('success', function(e) { 
		        layer.msg("<span style='color:white'>复制成功</span>", {
		            area:["150px","48px"],
		            time:2000
		        });
		    });
		}
	});
}

function createDevVersionTable(data){
	$("#devVersionTable").bootstrapTable('destroy');
    $("#devVersionTable").bootstrapTable({
        queryParamsType:"",
        data:data,
        columns : [
        {
                field : "yearMonth",
                title : "时间",
                align : 'center',
                class : "stepOrder",
                width : '50px',
        },
        {
            field : "planWindowsNumber",
            title : "计划内版本次数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "tempWindowsNumber",
            title : "临时版本次数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "tempAddTaskNumber",
            title : "临时增加任务数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
                return '<div class="def_tableDiv2"><input type="text" class="form-control" onchange="calculatePercent('+index+')" id="addNumber'+index+'" value="0"/></div>';
            }
        },{
            field : "tempDelTaskNumber",
            title : "临时删除任务数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
                return '<div class="def_tableDiv2"><input type="text" class="form-control" onchange="calculatePercent('+index+')" id="delNumber'+index+'" value="0"/></div>';
            }
        },{
            field : "totalTaskNumber",
            title : "测试任务总数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
            	return '<span id="totalTaskNumber'+index+'">'+value+'</span>';
            }
        },{
            field : "requirementNumber",
            title : "业务需求数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "defectNumber",
            title : "缺陷数",
            align : 'center',
            class : "stepOrder",
            width : '50px',
        },{
            field : "changePercent",
            title : "变更率",
            align : 'center',
            class : "stepOrder",
            width : '50px',
            formatter : function (value, row, index) {
                return '<span id="changePercent'+index+'">0%</span>';
            }
        }],
        onLoadSuccess:function(){
        },
        onLoadError : function(){
        }
    });
}
// 计算版本变更率
function calculatePercent(i){
	var addNumber = $("#addNumber"+i).val();
	var delNumber = $("#delNumber"+i).val();
	var totalTaskNumber = $("#totalTaskNumber"+i).text();
	if(!reg.test(addNumber)){
		$("#addNumber"+i).val("");
		return;
	}
	if(!reg.test(delNumber)){
		$("#delNumber"+i).val("");
		return;
	}
	var percent = (parseInt(addNumber)+parseInt(delNumber))/parseInt(totalTaskNumber)*100;
	$("#changePercent"+i).text(totalTaskNumber == 0?"0%":percent.toFixed(2)+"%");
}