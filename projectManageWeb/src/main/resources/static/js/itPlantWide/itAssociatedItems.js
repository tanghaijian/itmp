/******
 * 
 * IT全流程测试-markdown文档内容-关联条目页面js
 * *******/

var setting2 = {
	data: {
		simpleData: {
			enable: true
		},
		key: {
            name: "chaptersName"
        },
	},
	callback: {
		onClick: getDocument_ztree
	}
};

var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		},
		key: {
            name: "chaptersName"
        },
	},
	callback: {
	}
};

var code;

//IT全流程测试-markdown文档内容-关联条目，初始化树形文档结构
$(document).ready(function () {
	$("#associated_item_btn").on("click", function () {
		$("#loading").css('display', 'block');
		$.ajax({
				url: "/projectManage/itReDocumentChapters/getChaptersTree",
				type: "post",
				data: {
					systemDirectoryDocumentId: parameterArr.obj.id,
				},
				success: function (data) {
					if(data.status == 1){
						search_radio_clear('doc_Name', $("#doc_Id"));
						$.fn.zTree.init($("#menuTree"), setting2, data.data);
						$('#documentList').empty();
						$('.noData').show();
						$("#associated_item_modal").modal('show');
					}
					$("#loading").css('display', 'none');
				},
				error: function (data) {
					$("#loading").css('display', 'none');
					layer.alert("系统内部错误!", {
							icon: 2,
							title: "提示信息"
					});
				}
		})
	})
	
	//aotocomplete查询框内容
	fuzzy_search_radio('doc_Name','/projectManage/itReDocumentChapters/selectDocumentByPage','documentName','id','28px',$("#doc_Id"),true);
	
	//已关联文档点击事件
	$('#doc_Name_list').on('click', '.doc_Name_search_item', function () {
		var val = $(this).text();
		var id = $(this).data('id');
		$("#doc_Id").val(id);
		$("#doc_Name").val(val).attr('username', val).change();
		$('#doc_Name_list').hide();
		get_search_ztree(id);
	})
	
	//【本文档】点击事件查询
	$('#prePower').click(function () {
		search_radio_clear('doc_Name', $("#doc_Id"));
		$('#doc_Id').val(parameterArr.obj.id);
		get_search_ztree(parameterArr.obj.id);
	})
	// $.fn.zTree.init($("#menuTree"), setting2, zNodes2);
});

var acc_DocumentId = '';//关联章节id
function getDocument_ztree(event, treeId, treeNode, clickFlag) {
	acc_DocumentId = treeNode.id;
	get_search_ztree();
}

//查询关联的文档和章节，并以树形展示
function get_search_ztree(ID) {
	if (!$('#doc_Id').val()) return;
	if (!acc_DocumentId) return;
	$('#loading').show();
	$.ajax({
		url: "/projectManage/itReDocumentChapters/getRelationDocumentAndChapters",
		type: "post",
		data: {
			systemDirectoryDocumentId1: $('#doc_Id').val(),
			systemDirectoryDocumentChapterId1: acc_DocumentId,
			systemDirectoryDocumentId2: ID,
		},
		success: function (data) {
			$('#doc_name_list').empty();
			$.fn.zTree.init($("#documentList"), setting, data.chapters);
			setCheck();
			$("#py").bind("change", setCheck);
			$("#sy").bind("change", setCheck);
			$("#pn").bind("change", setCheck);
			$("#sn").bind("change", setCheck);
			if(data.documents.length){
				var arr = data.documents;
				for(var i=0; i<arr.length; i++){
			        for(var j=i+1; j<arr.length; j++){
			            if(arr[i].id == arr[j].id){  
			                arr.splice(j,1);//取后值
			                j--;//取后值
			            }
			        }
			    }
				arr.map(function(val) {
					$('#doc_name_list').append('<span class="doc_name" onclick="get_search_ztree('+val.id+')">'+val.documentName+'</span>&nbsp;');
				})
			} 
			if(data.chapters.length){
				$(".tableArea .noData").css("display", "none");
			}else{
				$(".tableArea .noData").css("display", "block");
			}
			$('#loading').hide();
		},
		error: function (data) {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误!", {
					icon: 2,
					title: "提示信息"
			});
		}
	})
	
	
}

//提交关联条目
function save_node_submit() {
	var treeObj = $.fn.zTree.getZTreeObj("documentList"),
		nodes = treeObj.getCheckedNodes(true),
		node_sub_arr = '';
	for (var i = 0; i < nodes.length; i++) {
		node_sub_arr += nodes[i].id + ",";

	}
	$('#loading').show();
	$.ajax({
		url: "/projectManage/itReDocumentChapters/insertChaptersRelation",
		type: "post",
		data: {
			currentUserAccount:currentUserAccount,
			systemDirectoryDocumentId1: parameterArr.obj.id,
			systemDirectoryDocumentChapterId1: acc_DocumentId,
			systemDirectoryDocumentId2: $('#doc_Id').val(),
			chaptersIdStr: node_sub_arr
		},
		success: function (data) {
			layer.alert("保存成功!", {
				icon: 1,
				title: "提示信息"
			});
			$('#loading').hide();
			//$("#associated_item_modal").modal("hide");
		},
		error: function (data) {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误!", {
					icon: 2,
					title: "提示信息"
			});
		}
	})
}

//章节变化表
function node_change(){
	$('#loading').show();
	$.ajax({
		url: "/projectManage/itReDocumentChapters/getAllRelationChapters",
		type: "post",
		data: {
		 systemDirectoryDocumentId1: parameterArr.obj.id,
		},
		success: function (data) {
			$('#associated_information_tbody').empty();
			if(data.status == 1){
				if(!data.rows.length){
					layer.alert("暂无关联文档!", {
						icon: 0,
						title: "提示信息"
					});
				}else{
					data.rows.map(function(val){
						var _ass_arr = '';
						val.relateChapters.length && val.relateChapters.map(function(v){
							_ass_arr += '<div style="padding: 5px;border-bottom:1px solid #E9E9E9;">'+v.chaptersName+'</div>';
						})
						$('#associated_information_tbody').append('<tr>'+
								'<td style="position: relative;"> <div style="position: absolute;top: 40%;">'+val.chaptersName+'</div></td>'+
								'<td style="padding: 0px;">'+_ass_arr+'</td>'+
						'</tr>');
					})
					$('#associated_information_Modal').modal('show');
				}
			}else{
				
			}
			$('#loading').hide();
		},
		error: function (data) {
			$("#loading").css('display', 'none');
			layer.alert("系统内部错误!", {
					icon: 2,
					title: "提示信息"
			});
		}
	 })
}

//========================      ztree   选中事件
function setCheck() {
	var zTree = $.fn.zTree.getZTreeObj("documentList"),
		py = $("#py").attr("checked") ? "p" : "",
		sy = $("#sy").attr("checked") ? "s" : "",
		pn = $("#pn").attr("checked") ? "p" : "",
		sn = $("#sn").attr("checked") ? "s" : "",
		type = { "Y": py + sy, "N": pn + sn };
	zTree.setting.check.chkboxType = type;
	showCode('setting.check.chkboxType = { "Y" : "' + type.Y + '", "N" : "' + type.N + '" };');
}

function showCode(str) {
	if (!code) code = $("#code");
	code.empty();
	code.append("<li>" + str + "</li>");
}