$(function(){
	getSystem();
	pageInit();
	//搜索展开和收起
    $("#downBtn").click(function () { 
        if( $(this).children("span").hasClass( "fa-caret-up" ) ){
            $(this).children("span").removeClass("fa-caret-up");
            $(this).children("span").addClass("fa-sort-desc");
            $("#search_div").slideUp(200);
        }else {
            $(this).children("span").removeClass("fa-sort-desc");
            $(this).children("span").addClass("fa-caret-up");
            $("#search_div").slideDown(200);
        }
    });
    $("#ck").click(function(){
        if($(this).prop("checked")){
          $("#edit_serverUserPassword").prop("type","text");
        }
        else{
          $("#edit_serverUserPassword").prop("type","password");
        }
      })
});
	//表格数据加载
	function pageInit(){
		$("#serverStatusName").selectpicker('val','1');
	    $("#list2").jqGrid({
	        url:'/devManage/serverinfo/getAllServerInfo',
	        datatype: 'json', 
	        mtype:"POST",
	        height: 'auto',
	        width: $(".content-table").width()*0.999,  
	        postData:{status:'1'},
	        height: 'auto',
	        colNames:['id','主机名称','系统名称','IP','SSH端口','SSH用户名'/*,'SSH密码'*/,'状态','操作'],
	        colModel:[ 
	            {name:'id',index:'id',hidden:true}, 
	            {name:'hostName',index:'hostName', searchoptions: {sopt: ['cn']},
	            	formatter : function(value, grid, rows, state) {
	            		return htmlEncodeJQ(value);
	                }
	            },
                {name:'systemName',index:'systemName', searchoptions: {sopt: ['cn']},
                    formatter : function(value, grid, rows, state) {
                		if(value==null){
                            return "";
						}else{
                            return htmlEncodeJQ(value);
						}
                    }
                },
	            {name:'ip',index:'ip', searchoptions: {sopt: ['cn']},
	            	formatter : function(value, grid, rows, state) {
	            		return htmlEncodeJQ(value);
	                }
	            },
	            {name:'sshPort',index:'sshPort'},
	            {name:'sshUserAccount',index:'sshUserAccount', searchoptions: {sopt: ['cn']},
	            	formatter : function(value, grid, rows, state) {
	            		return htmlEncodeJQ(value);
	                }
	            },
	           /* {name:'sshUserPassword',index:'sshUserPassword', searchoptions: {sopt: ['cn']},
	            	formatter : function(value, grid, rows, state) {
	            		return htmlEncodeJQ(value);
	                }
	            },*/
	            {name:'status',index:'status', searchoptions: {sopt: ['cn']}, 
	            	formatter : function(value, grid, rows, state) {
	            		return value == '1'?'有效':'无效'
	                }
	            },
	            {name:'操作',index:'操作',align:"center", 
	            	formatter : function(value, grid, rows, state) {
	            		if(rows.status == 1){
		                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');
		                    var str = '';
		                    if(serverEdit){
		                    	str += '<a class="a_style" onclick="edit('+ row + ')">编辑</a>';
		                    }
		                    if(serverRemove){
		                    	str += '| <a class="a_style" onclick="del('+ row + ')">删除</a>';
		                    }
		                    return str;
	            		}else{
	            			return "";
	            		}
	                }
	            },
	        ],
	        rowNum:10,
	        rowTotal: 200,
	        rowList : [10,20,30], 
	        rownumWidth: 40,
	        pager: '#pager2',
	        sortable: true,   //是否可排序
	        sortorder: 'asc', 
	        sortname: 'id',
	        loadtext:"数据加载中......",
	        viewrecords: true, //是否要显示总记录数 
	        jsonReader: {
	            repeatitems: true, 
	        },
	    }).trigger("reloadGrid"); 
	}
	
//子系统下拉框数据初始化
function getSystem(){
	$.ajax({
        type:"POST",
        url:"/devManage/systeminfo/findSystemByProject",
        dataType:"json",
        success:function(data){
        	var list = data.rows;
//        	for(var i=0;i<list.length;i++){
//        		$("#system").append(' <option value="'+list[i].id+'">'+list[i].systemName+'</option>');
//        	}
        	for (var i = 0; i < list.length; i++) {
				//先创建好select里面的option元素       
				var option = document.createElement("option");
				//转换DOM对象为JQ对象,好用JQ里面提供的方法 给option的value赋值         
				$(option).val(list[i].id);
				//给option的text赋值,这就是你点开下拉框能够看到的东西          
				$(option).text(list[i].systemName);
				//获取select 下拉框对象,并将option添加进select            
				$('#system').append(option);
			}
        }
	});
}

//显示新增页面
	function toAddService(){
        $.ajax({
            type:"POST",
            url:"/devManage/systeminfo/findSystemByProject",
            dataType:"json",
            success:function(data){
            	var systemInfos = data.rows;
                $("#addSystem").empty();
                $("#addSystem").append('<option value="">请选择</option>');

				$.each(systemInfos, function(key, system) {//遍历
                    $("#addSystem").append(' <option value="'+system.id+'">'+system.systemName+'</option>');
				});
                $('.selectpicker').selectpicker('refresh');
            }
    	});
		$("#add_server").modal('show');
	}

	//显示编辑页面
	function edit(row){
		clearValidate();
		var id = row.id;
		var hostName = row.hostName;
		var ip = row.ip;
		var sshPort = row.sshPort
		var sshUserAccount = row.sshUserAccount;
		var sshUserPassword = row.sshUserPassword;
        var systemId= row.systemId;

		$("#id").val(id);
		$("#edit_serverName").val(hostName);
		$("#edit_serverIP").val(ip);
		$("#edit_serverPort").val(sshPort);
		$("#edit_serverUserAcount").val(sshUserAccount);
		$("#edit_serverUserPassword").val(sshUserPassword);


        $.ajax({
            type:"POST",
            url:"/devManage/systeminfo/findSystemByProject",
            dataType:"json",
            success:function(data){
            	var systemInfos = data.rows;
                $("#editSystem").empty();
                $("#editSystem").append('<option value="">请选择</option>');

                $.each(systemInfos, function(key, system) {//遍历
                    var flag = "";
                	if (system.id==systemId) {
                		flag = "selected";
					}
					$("#editSystem").append(' <option '+flag+' value="'+system.id+'">'+system.systemName+'</option>');

                });
                $('.selectpicker').selectpicker('refresh');
            }
        });
		$("#edit_server").modal('show');
	}
	
	function clearContent( that ){
	    $(that).parent().children('input').val("");
	    $(that).parent().children(".btn_clear").css("display","none");
	} 
	
	//查询信息
	function searchInfo(){ 
		$("#list2").jqGrid('setGridParam',{ 
	        postData:{
	             "hostName" :  $("#hostName").val(),
	             "status" :  $("#serverStatusName").val(),
	             "ip" : $("#ipName").val(),
	             "systemId" : $("#system").val()
	        },  
	        page:1
	    }).trigger("reloadGrid"); //重新载入
	}
	
	//新增服务器
	function addServerCommit(){
		var flag = true;
		clearValidate();
		var systemId=$('#addSystem').val();
		var hostName = $('#add_serverName').val();
		var ip = $('#add_serverIP').val();
		var sshPort = $('#add_serverPort').val();
		var sshUserAccount = $('#add_serverUserAcount').val();
		var sshUserPassword = $('#add_serverUserPassword').val();
		
		if (sshPort == "") {
			sshPort = 22;
		}
		if(systemId == ""){
			flag = validateIsNull('addSystem');
		}
		if(ip == ""){
			flag = validateIsNull('add_serverIP');
		}
		if(sshUserAccount == ""){
			flag = validateIsNull('add_serverUserAcount');
		}
		if(sshUserPassword == ""){
			flag = validateIsNull('add_serverUserPassword');
		}

		if(!flag){
			return false;
		}
		var serverInfoJson = JSON.stringify({
            	systemId:systemId,
				hostName: hostName,
				ip: ip,
				sshPort: sshPort,
				sshUserAccount: sshUserAccount,
				sshUserPassword: sshUserPassword
			});
		$.ajax({
			type:"post", 
			url:"/devManage/serverinfo/addServerInfo",
//			transformRequest : function(obj) {
//				return angular.toJson(obj);
//			},
			data:{serverinfoStr:serverInfoJson},
			success : function(data) {
				if (data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    layer.alert('新增服务器成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    $("#add_server").modal("hide");
    				pageInit();
                }
			}
		});  
	}
	
	//编辑服务器
	function editServerCommit(){  
		var flag = true;
		clearValidate();
        var systemId=$('#editSystem').val();
		var id = $("#id").val();
		var hostName = $('#edit_serverName').val();
		var ip = $('#edit_serverIP').val();
		var sshPort = $('#edit_serverPort').val();
		var sshUserAccount = $('#edit_serverUserAcount').val();
		var sshUserPassword = $('#edit_serverUserPassword').val();
		
		if (sshPort == "") {
			sshPort = 22;
		}
		if(systemId == ""){
			flag = validateIsNull('editSystem');
		}
		if(ip == ""){
			flag = validateIsNull('edit_serverIP');
		}
		if(sshUserAccount == ""){
			flag = validateIsNull('edit_serverUserAcount');
		}
		if(sshUserPassword == ""){
			flag = validateIsNull('edit_serverUserPassword');
		}
		//flag：页面校验是否通过false没有
		if(!flag){
			return false;
		}
		var serverinfoStr = JSON.stringify({
			'id': id,
			'hostName': hostName,
			'ip': ip,
			'sshPort': sshPort,
            'systemId':systemId,
			'sshUserAccount': sshUserAccount,
			'sshUserPassword': sshUserPassword
		});
		$.ajax({
			type:"post", 
			url:"/devManage/serverinfo/updateServerInfo",
//			transformRequest : function(obj) {
//				return angular.toJson(obj);
//			},
			data:{serverinfoStr:serverinfoStr},
			success : function(data) {
				if (data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    layer.alert('编辑服务器成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
                    $("#edit_server").modal("hide");
    				pageInit();
                }
			}
		});  
	}
	//修改服务器状态
	function del(row){
		$.ajax({
			type:"post", 
			url:"/devManage/serverinfo/updateServerInfoStatus",
//			transformRequest : function(obj) {
//				return angular.toJson(obj);
//			},
			data:{
				'id': row.id,
				'status': 2
			},
			success : function(data) {
				if (data.status == 2){
                    layer.alert(data.errorMessage, {
                        icon: 2,
                        title: "提示信息"
                    });
                } else {
                    layer.alert('删除服务器成功！', {
                        icon: 1,
                        title: "提示信息"
                    });
    				pageInit();
                }
			}
		});  
	}
	//验证空
	function validateIsNull(inputId){
		$('#'+inputId).parents('.rowdiv').addClass('has-error');
		$('#'+inputId).next('.help-block').css('display','block');
		return false;
	}
	//清空验证消息
	function clearValidate(){
		$('.rowdiv').removeClass('has-error');
		$('.help-block').hide();
	}
	
	//清空搜索内容
	function clearSearch() {
	    $('#hostName').val("");
	    $('#serverStatusName').selectpicker('val', '');
	    $('#ipName').val("");
	    $('#system').selectpicker('val', '');
	}
