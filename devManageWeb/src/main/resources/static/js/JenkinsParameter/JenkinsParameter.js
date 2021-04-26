/**
 * Description: 构建部署参数配置
 * Author:niexingquan
 * Date: 2020/04/03
 */
 var htmlTable = " <tr>" +
 			"<td style='display:none'><input type='text' value=''></td>" +
    		"<td><input type='text' value=''></td>" +
    		"<td><input type='text' value=''></td>" +
    		"<td><div class='zTreeDemoBackground left'><ul id='treeDemo' class='ztree'></ul></div>	</td>" +
    		"<td><a href='javascript:void(0);' class='invalid' value='1' onclick='delDataDict(this,null)'>删除</a></td> </tr>"
    		
var _buttonType="";  
var ids="";

var zNodes = [];
var setting = {
		check: {
			enable: true,
			//chkStyle: "radio",
			chkboxType:{ "Y":"ps", "N":"ps"}
		},
		data: {
			simpleData: {
				enable: true
			}
		}
		
		};

$(function () {
    downOrUpButton();
    buttonClear();
    initTable();
    _formValidator();
    $("#system_Name").change(function(){
    	if($("#systemId").val()!=""){
    		
    		choiceSystem();
    	}
    	
    });
    var system = {
		    'ele': 'system_Name',
		    'url': '/devManage/systemJenkinsParameter/systemSystemName',
		    'params': {
		      'systemName': ''
		    },
		    'name': 'systemName',
		    'id': 'id',
		    'top': '36px',
		    'userId': $('#systemId'),
		    'earth': true
	  };
    fuzzy_search_radio(system);
    $('#searchInfo_btn').click(function(){
        $("#parameterlist").jqGrid('setGridParam', {
            postData: {
            	parameterName:$('#params_Name').val()
            },
            page: 1,
        }).trigger("reloadGrid");
    })
    $('#clearSearch_btn').click(function(){
        $('#params_Name').val('');
    })
    
    $("#addModel").on('hidden.bs.modal',function(){
        $("#_addFrom").data('bootstrapValidator').destroy();
        $('#_addFrom').data('bootstrapValidator', null);
        _formValidator();
    });
});

//页面查询
function initTable() {
    var page = $('#parameterlist').getGridParam('page');
    $("#parameterlist").jqGrid("clearGridData");
    $("#parameterlist").jqGrid("setGridParam", {page: page != null && page != undefined ? page : 1});
    $("#parameterlist").jqGrid({
        url: "/devManage/systemJenkinsParameter/getSystemJenkinsParameterList",
        datatype: 'json',
        mtype: "POST",
        height: 'auto',
        width: $(".content-table").width() * 0.999,
//        autowidth: true,
        rowNum: 10,
        rowTotal: 200,
        rowList: [10, 20, 30],
        rownumWidth: 40,
        pager: '#parameterPager',
        sortable: false,   //是否可排序
        loadtext: "数据加载中......",
        viewrecords: true, //是否要显示总记录数
        //postData: search_data,
        colNames: ['id', '参数名称', '选择框类型', '操作'],
        colModel: [
            { name: 'id', index: 'id', hidden: true },
            {
                name: "parameterName",
                align: 'center',
                search: false,
            },
            {
                name: "selectType",
                align: 'center',
                search: false,
                formatter: function (value, grid, row, index) {
                	if(value==1){
                         return '单选';
                	}else if(value==2){
                		return "多选";
                	}
                   
                }
            },
            {
                name: "opt",
                align: 'opt',
                search: false,
                align: 'center',
                formatter: function (value, grid, row, index) {
                	if(jenkinsParameter_edit){
                		 var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                         return '<a class="a_style" onclick="_edit(this)" data="'+ rows + '">编辑</a>';
                	}else{
                		return "";
                	}
                   
                }
            }
        ],
        loadComplete: function () {
            $("#loading").css('display', 'none');
        },
        beforeRequest: function () {
            $("#loading").css('display', 'block');
        },
        loadError: function () {
            $("#loading").css('display', 'none');
            layer.alert("查询失败", {
                icon: 2,
                title: "提示信息"
            });
        }
    }).trigger("reloadGrid");
}

//新增项按钮触发
function addPatameterDict() {
   
   
    var _id = new Date().getTime();
    if($("#systemId").val()==""){
    	layer.alert("请先选择系统", {
            icon: 0,
            title: "提示信息"
        });
    	return;
    }
    selectJenkinsTree();
    var tableData = " <tr>" +
		"<td style='display:none'><input type='text'  class='form-control'></td>" +
		"<td><input type='text'  class='form-control'></td>" +
		"<td><input type='text'  class='form-control'></td>" +
		"<td><div class='zTreeDemoBackground left'><input type='text'  class='form-control'  style='display:none' />"+
		"<ul id=treeDemo_"+_id+" class='ztree' style='display:block'></ul></div>	</td>" +
		"<td><a href='javascript:void(0);' class='invalid' value='1' onclick='delDataDict(this,null)'>删除</a></td> </tr>"
		$("#modifyDataDictModalTableBody").append(tableData);
    $.fn.zTree.init($("#treeDemo_"+_id), setting, zNodes);
}
function delDataDict(obj, id) {
	if(id!=null){
		ids+=id+",";
	}
	
     $(obj).parents("tr").remove();
 } 
// 新建
function _add(type) {
	cleanTable();
	ids="";
	_buttonType=type;
    $("#edit_id").val('');
    $('#_addFrom').find('input').each(function (idx, val) {
        $(val).val('');
    })
    $('#microChildService').text('新建注册任务参数化配置');
    $("#addModel").modal("show");
}

// 编辑
function _edit(This) {
	ids="";
	_buttonType="edit";
    $("#edit_id").val('');
    $('#_addFrom').find('input').each(function (idx, val) {
        $(val).val('');
    })
    var data=JSON.parse($(This).attr("data"));
    $('#microChildService').text('编辑构建部署参数');
    $("#loading").css('display', 'block');
    $.ajax({
        type: "post",
        url:  '/devManage/systemJenkinsParameter/selectSystemJenkinsParameterById',
        dataType: "json",
        data:{
			"systemJenkinsParameterId":data.id
		},
		
        success: function (data) {
        	if(data.status == 1){
        		editData(data);
        	}else{
        		layer.alert("查询失败", {
                    icon: 2,
                    title: "提示信息"
                });
        	}
        	$("#loading").css('display', 'none');
        	
        },error:function(){
        	$("#loading").css('display', 'none');
        }
    })
    
    $("#addModel").modal("show");
}

// 新增  编辑
function addOrEditSubmit() {
	$('#_addFrom').data('bootstrapValidator').validate();
	if(!$('#_addFrom').data('bootstrapValidator').isValid()){
		return;
	}
	 var dataDictList = [];
	 var num=1;
	 var typeNum="";
     $('#modifyDataDictModalTableBody tr').each(function () {
         var dataDict = {};
         $(this).children("td").find("input").each(function (i) {
             if (i == 0) {
                dataDict["id"] = $(this).val();
             } else if (i == 1) {
            	 valueName = $(this).val();
            	 if(valueName==""){
            		 layer.alert("第"+num+"行枚举值不能为空", {
                         icon: 0,
                         title: "提示信息"
                     });
            		 typeNum=1;
            		 return false;
            	 }
                 dataDict["parameterValue"] = $(this).val();
             } else if (i == 2) {
            	 var valueSeq=$(this).val();
            	 if(valueSeq==""){
            		 layer.alert("第"+num+"行枚举值不能为空", {
                         icon: 0,
                         title: "提示信息"
                     });
            		 typeNum=1;
            		 return false;
            	 }
            	 dataDict["valueSeq"] = valueSeq;
             }else if(i == 3){ 
            	 var ztree_ele = $(this).next(".ztree").attr('id'); 
            	 var treeObj = $.fn.zTree.getZTreeObj(ztree_ele);
            	 if(treeObj==null){
            		 layer.alert("第"+num+"行jenkins任务tree不能为空", {
                         icon: 0,
                         title: "提示信息"
                     });
            		 typeNum=1;
            		 return false;
            	 }
            	 nodes = treeObj.getCheckedNodes(true);
            	 var tree_arr = ''; 
            	 nodes.map(v=>{
            		 if(!v.isParent && v.realId){
            			 tree_arr += v.realId + ',';
            		 }
            	 })
                 dataDict["systemJenkinsId"] = tree_arr; 
             }
         });
        if(typeNum==1){
        	return false;
        }
         num++;
         dataDict["status"] = $(this).children("td").find(".invalid").attr("value");
         var s = $(this).children("td").find(".invalid").attr("value");
         dataDictList.push(dataDict);
     });
     if(typeNum==1){
    	 return;
     }
     //新增
     if(_buttonType=="add"){
    	 addSubmit(dataDictList);
     }else if(_buttonType=="edit"){//编辑
    	 editSubmit(dataDictList);
     }
     
    
    
}
//编辑提交
function editSubmit(dataDictList){
	$("#loading").show();
	 $.ajax({
        type: "post",
        url:  '/devManage/systemJenkinsParameter/editSystemJenkinsParameter',
        dataType: "json",
        data:{
        	"id":$("#edit_id").val(),
			"parameterName":$("#paramName").val(),
			"systemId":$("#systemId").val(),
			"selectType":$("#environmentType").val(),
			"deleteIds":ids,
			"systemJenkinsParameterValues": JSON.stringify(dataDictList)
		},
        success: function (data) {
       	 if(data.status==1){
       		 layer.alert("编辑成功", {
                    icon: 1,
                    title: "提示信息"
                });
       		$("#addModel").modal("hide");
       		 $("#parameterlist").jqGrid('setGridParam', {
       	        }).trigger("reloadGrid");
       		$("#loading").hide();
       	 }else{
       		layer.alert("编辑失败", {
                icon: 2,
                title: "提示信息"
            }); 
       		$("#loading").hide();
       	 }
        },error:function(){
        	 layer.alert("编辑失败，请联系管理员", {
                 icon: 2,
                 title: "提示信息"
             });
        	$("#loading").hide();
        }
    })
}

//新增提交
function addSubmit(dataDictList){
	$("#loading").show();
	 $.ajax({
         type: "post",
         url:  '/devManage/systemJenkinsParameter/addSystemJenkinsParameter',
         dataType: "json",
         data:{
 			"parameterName":$("#paramName").val(),
 			"systemId":$("#systemId").val(),
 			"selectType":$("#environmentType").val(),
 			"systemJenkinsParameterValues": JSON.stringify(dataDictList)
 		},
         success: function (data) {
        	 if(data.status==1){
        		 layer.alert("新增成功", {
                     icon: 1,
                     title: "提示信息"
                 });
        		 $("#addModel").modal("hide");
        		 $("#parameterlist").jqGrid('setGridParam', {
        	        }).trigger("reloadGrid");
        		 $("#loading").hide();
        	 }else{
        		 layer.alert("新增失败", {
                     icon: 2,
                     title: "提示信息"
                 });
        		 $("#loading").hide();
        	 }
         },error:function(){
        	 $("#loading").hide();
        	 layer.alert("新增失败，请联系管理员", {
                 icon: 2,
                 title: "提示信息"
             });
         }
     })
}

//校验
function _formValidator(){
    $('#_addFrom').bootstrapValidator({
        excluded : [ ':disabled' ],//下拉框验证
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        fields : {
        	systemName : {
            	trigger:"change",
                validators : {
                    notEmpty:{
                        message: '系统不能为空！'
                    }
                }
            },
           
            environmentType : {
                validators : {
                    notEmpty:{
                        message: '选择框类型不能为空！'
                    }
                }
            },
            paramName : {
                validators : {
                    notEmpty:{
                        message: '参数名称不能为空！'
                    }
                }
            },
           
           
        }
    })
}

//点击编辑后显示详情
function editData(data){
	cleanTable();
	$("#edit_id").val(data.data.id);
	$("#paramName").val(data.data.parameterName);
	$("#system_Name").val(data.data.systemName);
	$("#systemId").val(data.data.systemId);
	$("#environmentType").val(data.data.selectType);
	$(".selectpicker").selectpicker('refresh');
	var parameterValuesList=data.data.parameterValuesList;
	if(parameterValuesList!=null){
		for(var i=0;i<parameterValuesList.length;i++){
			var html=" <tr>" +
			"<td  style='display:none'><input type='hidden'  value='"+parameterValuesList[i].id+"'></td>" +
			"<td><input type='text' value='"+parameterValuesList[i].parameterValue+"'   class='form-control'></td>" +
     		"<td><input type='text' value='"+parameterValuesList[i].valueSeq+"'   class='form-control'></td>" +
     		"<td><div class='zTreeDemoBackground left'><input type='text'  class='form-control'  style='display:none' />"+
     		"<ul id=treeDemo_"+parameterValuesList[i].id+" class='ztree' style='display:block'></ul></div>	</td>" +
     		"<td><a href='javascript:void(0);' class='invalid' value='1' onclick='delDataDict(this,"+parameterValuesList[i].id+")'>删除</a></td> </tr>";
			$("#modifyDataDictModalTableBody").append(html);
			var arr = parameterValuesList[i].listZtree;
			if(arr!=null){
				for (var k = 0; k < arr.length; k++) {
					for (var j = k+1; j < arr.length; j++) {
						if (arr[k].id == arr[j].id && arr[k].pId == arr[j].pId) {
							arr.splice(j,1);
							j--;
						}
					}
				}
			}
			
			//zNodes= arr;
			if(arr!=null){
				 if(arr.length){
					 $.fn.zTree.init($("#treeDemo_"+parameterValuesList[i].id), setting, arr);
				 }
			}
			
		}
	}
     
}
//获取枚举值每行的id,并组装到总变量的IDS中
function choiceSystem(){
	
     $('#modifyDataDictModalTableBody tr').each(function () {
         var dataDict = {};
         $(this).children("td").find("input").each(function (i) {
             if (i == 0) {
            	 if($(this).val()!=""){
            			ids+=$(this).val()+",";
            		}
             } 
         });
     });
     $("#modifyDataDictModalTableBody").empty();
}
//清理弹出框数据
function cleanTable(){
	$("#edit_id").val("");
	$("#modifyDataDictModalTableBody").empty();
	$("#paramName").val("");
	$("#system_Name").val("");
	$("#systemId").val("");
	$("#environmentType").val("");
	$(".selectpicker").selectpicker('refresh');
}
//查询系统下目录
function selectJenkinsTree(){
	 $.ajax({
         type: "post",
         url:  '/devManage/systemJenkinsParameter/selectJenkinsTree',
         dataType: "json",
         async: false,
         data:{
 			"systemId":$("#systemId").val()
 		},
         success: function (data) {
        	 var arr = data.ztreeObjs;
				for (var i = 0; i < arr.length; i++) {
					for (var j = i+1; j < arr.length; j++) {
						if (arr[i].id == arr[j].id && arr[i].pId == arr[j].pId) {
							arr.splice(j,1);
							j--;
						}
					}
				}
        	 zNodes= arr;
         },error:function(){
            
         }
     })
}
