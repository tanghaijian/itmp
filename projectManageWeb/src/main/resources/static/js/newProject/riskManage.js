/**************
 *  风险管理
 *  新建类项目, 项目群,  测试综合管理 , 公用页面
 *  niexingquan
 *  2019-11-29
 */

var file_param = {
    formFileList:[], // 上传文件
    editAttList :[], // 编辑页面，已经上传的文件
    fileTableName:"", // 文件名称
    removeFileId:[]
}

var modal = {
    system:"",
    feature:"",
    window:"",
    requirement:""
};
var select_rows = new Array();

$(() => {

    // 运维类项目进入的
    if(toProjectType == 1){
        opsClassProject.list();
        scroll_add_data();
        opsClassProject.file.uploadFileList();

        //定义各种弹窗触发
        //新增风险：涉及子系统
        $("#systemName").click(function(){
            var projectId=''
            if($("#add_project_id").length>0){ //风险管理自身模块
                projectId=$.trim($("#add_project_id").val())
                if(!projectId){
                    layer.msg('请先选择项目!',{icon:0});
                    return;
                }
            }else{ //其他调用风险管理模块
                projectId=$.trim($("#projectId").val())
            }
            if(projectId) {$('#systemModal').attr("data-projectId",projectId)}
            opsClassProject.modal.system.resetSystem();
            modal.system = 1;
            systemTableShow(true); // modal.js
            $("#systemModal").modal('show');
        });

        //查询：涉及子系统
        $("#sel_system").click(function(){
            opsClassProject.modal.system.resetSystem();
            modal.system = 2;
            systemTableShow(true); // modal.js
            $("#systemModal").modal('show');
        });

        //新增风险：测试任务
        $("#requirementFeatureName").click(function(){
            opsClassProject.modal.feature.resetFeature();
            modal.feature = 1;
            DevPopup(true);// modal.js
            $("#DevPopup").modal("show");
        });

        //查询：测试任务
        $("#sel_Feature").click(function(){
            opsClassProject.modal.feature.resetFeature();
            modal.feature = 2;
            DevPopup(true);// modal.js
            $("#DevPopup").modal("show");
        });

        //新增风险：投产窗口
        $("#commissioningWindowName").click(function () {
            opsClassProject.modal.commissioning.resetComm();
            modal.window = 1;
            ComWindowShow(true);
            $("#comWindowModal").modal("show");
        });

        //查询：投产窗口
        $("#sel_window").click(function () {
            opsClassProject.modal.commissioning.resetComm();
            modal.window = 2;
            ComWindowShow(true);
            $("#comWindowModal").modal("show");
        });

        //新增风险：需求编号
        $("#requirementCode").click(function () {
            opsClassProject.modal.requirement.resetReq();
            modal.requirement = 1;
            reqTableShow(true);
            $("#selReq").modal("show");
        });

        //查询：需求编号
        $("#sel_requirement").click(function () {
            opsClassProject.modal.requirement.resetReq();
            modal.requirement = 2;
            reqTableShow(true);
            $("#selReq").modal("show");
        });
    }else if(toProjectType == 2){// 新建类项目进入的
        // 新建类项目进入的
        pageInit();
    }
    formValidator();
    refactorFormValidator();
    downOrUpButton();
    buttonClear();
    tableMouseover("#project_list");
    $(document).on('change', '#riskProbability', function () {
        riskPriority_give();
    })
    $(document).on('change', '#riskFactor', function () {
        riskPriority_give();
    })
    
    //由菜单：测试管理-风险管理进入
    if(entryType == 1){
    	fuzzy_search_radio({   
    	    'ele': 'sel_project_name',
    	    'url': '/testManage/testtask/getProjectListByProjectName',
    	    'params': {
	    		projectName: '',
    	    },
    	    'name': 'projectName',
    	    'id': 'id',
    	    'top': '29px',
    	    'dataName': 'projectInfoList',
    	    'userId': $('#sel_project_id'),
    	    'not_jqgrid': true,
    	});
    	fuzzy_search_radio({   
    	    'ele': 'add_project_name',
    	    'url': '/testManage/testtask/getProjectListByProjectName',
    	    'params': {
	    		projectName: '',
    	    },
    	    'name': 'projectName',
    	    'id': 'id',
    	    'top': '29px',
    	    'dataName': 'projectInfoList',
    	    'userId': $('#add_project_id'),
    	    'not_jqgrid': true,
    	});
    }
})

/**
 * @description 优先级赋值
 */
function riskPriority_give() {
    if ($('#riskProbability').val() && $('#riskFactor').val()) {
        let _val = parseInt($('#riskProbability').val()) * parseInt($('#riskFactor').val());
        let _lea = '';
        /*
        根据选择的危险度和发生概率，计算得到优先级，规则为：危险度×发生概率，优先级>=10 值为A，
        7=<优先级<10 值为B，4=<优先级<7 值为D，优先级<4 值为D; 
        危险度对应到致命、严重、一般、轻微的取值分别为：4、3、2、1；发生概率对应到高、中、低的分别取值为：3、2、1
        */
        if (_val >= 10) {
            _lea = 1;
        } else if (7 <= _val && _val < 10) {
            _lea = 2;
        } else if (4 <= _val && _val < 7) {
            _lea = 3;
        } else if (_val < 4) {
            _lea = 4;
        }
        $('#riskPriority').selectpicker('val', _lea);
    }
}

/**
 * @description 表格数据加载
 * @param projectId  项目id  或  programId  项目群id
 * @param projectType  项目类型  
 */
function pageInit() {
    let _postData = $('#project_type').val() == 1 ? 'projectId' : 'programId';
    $("#project_list").jqGrid({
        url: $('#project_type').val() == 1 ? '/projectManage/risk/getRiskInfo' : '/projectManage/risk/getRiskInfoByProgram',
        datatype: 'json',
        mtype: "POST",
        height: 'auto',
        rownumbers: true,
        width: $(".content-table").width() * 0.999,
        postData: {
            [_postData]: $("#projectId").val(),
            projectType: toProjectType
        },
        colNames: ['id', '风险描述', '优先级', '影响', '状态', '缓解措施', '责任人', '缓解措施执行情况', '操作'],
        colModel: [
            { name: 'id', hidden: true },
            { name: 'riskDescription', sortable: false },
            { name: 'riskPriorityName', sortable: false },
            { name: 'riskInfluence', sortable: false },
            { name: 'statusName', sortable: false },
            { name: 'copingStrategy', sortable: false },
            { name: 'responsibleUser', sortable: false },
            { name: 'copingStrategyRecord', width: 150, sortable: false },
            {
                name: '操作', align: "center", fixed: true, sortable: false, resize: false, search: false, width: 130,
                formatter: function (value, grid, rows, state) {
                    var row = JSON.stringify(rows).replace(/"/g, '&quot;');
                    var span_ = "<span>&nbsp;|&nbsp;</span>";
                    var a = '<a class="a_style" onclick="addProject(2,' + rows.id + ')">编辑</a>';
                    var b = '<a class="a_style" onclick="addProject(3,' + rows.id + ')">查看</a>';
                    var c = '<a class="a_style" onclick="_delete(' + rows.id + ')">删除</a>';
                    var opt_status = [];
                    opt_status.push(a);
                    opt_status.push(b);
                    opt_status.push(c);
                    return opt_status.join(span_);
                }
            },
        ],
        rowNum: -1,
//        rowTotal: 200,
//        rowList: [10, 20, 30],
        rownumWidth: 50,
        sortable: false,
        loadtext: "数据加载中......",
        viewrecords: true, //是否要显示总记录数
        jsonReader: {
            repeatitems: false,
            root: "data",
        },
        gridComplete: function () {
        },
        beforeRequest: function () {
            $("#loading").css('display', 'block');
        },
        loadComplete: function () {
            $("#loading").css('display', 'none');
        }
    }).trigger("reloadGrid");
    $("#project_list").jqGrid('setLabel','rn', '序号', {'text-align':'center','width':'50px'},'');
}

/**
 * @description 清空value
 */
function _clear_val(parent) {
    $(parent).find('input').each(function (idx, val) {
        $(val).val('').removeAttr('disabled').removeClass('bor_none shadow_none');
        $(val).parent().parent().find('.redStar').removeClass('_hide');
    })
    $(parent).find('textarea').each(function (idx, val) {
        $(val).val('').css({ height: 'unset' }).removeAttr('disabled').removeClass('bor_none shadow_none');
        $(val).parent().parent().find('.redStar').removeClass('_hide');
        $(val).removeClass('ov_hidden');
    })
    $(parent).find('select').each(function (idx, val) {
        $(val).val('');
        $(val).parent().parent().find('.bootstrap-select').removeClass('shadow_none');
        $(val).parent().parent().find('.dropdown-toggle').removeClass('bor_none').children().removeClass('col_black');;
        $(val).parent().parent().find('.dropdown-toggle').removeAttr('disabled');
        $(val).parent().parent().find('.bs-caret').removeClass('_hide');
        $(val).parent().parent().parent().find('.redStar').removeClass('_hide');
    })
    $('.selectpicker').selectpicker('refresh');
    $('#add_modal').find('.modal-dialog').removeClass('_Max_dialog');
    $('#add_modal').find('.modal-body').removeClass('bg_f2').find('.check_tit').hide();
    $('#add_modal').find('.modal-body').children().eq(0).removeClass('def_col_25').addClass('def_col_36').siblings().hide();
    $('#add_modal').find('.modal-footer').children().eq(1).text('取消');
    $('#add_modal').find('.modal-footer').children().eq(0).show();
    $("#add_tit").text('新建风险记录');
    $("#edit_id").val('');
    $(".btn_clear").css("display","none");
    file_param.formFileList = [];
    file_param.editAttList = [];
    file_param.removeFileId = [];
    $("#environmentType").selectpicker('val', '');
    $("#newFileTable tbody").html("");
}

/**
 * @description 参数空：新建,4运维类项目和测试菜单风险编辑 2新建项目风险编辑 3新建项目风险查看
 * @param id
 * @param projectType
 */
function addProject(type, ID) {
    _clear_val('#add_form');
    if (type == 2 || type == 3 || type == 4) {
        $.ajax({
            type: "post",
            url: "/projectManage/risk/getRiskInfoById",
            dataType: "json",
            data: {
                id: ID,
                projectType: toProjectType
            },
            beforeSend: function () {
                $("#loading").css('display', 'block');
            },
            success: function (result) {
                var data = result.data;
                if (result.status == 1) {
                    if (type == 2 || type == 3 || type == 4) {
                        $("#add_tit").text('编辑风险记录');
                        $("#edit_id").val(data.id);
                        for (var key in data) {
                            $('#add_form').find('input').each(function (idx, val) {
                                var name = $(val).attr("name");
                                if (key == name) {
                                    $(val).val(data[key]);
                                }
                                if (type == 3) {
                                    $(val).addClass('bor_none shadow_none').attr({ 'placeholder': '', 'disabled': 'disabled' });
                                    $(val).parent().parent().find('.redStar').addClass('_hide');
                                }
                            })
                            $('#add_form').find('select').each(function (idx, val) {
                                let name = $(val).attr("name");
                                if (key == name) {           //编辑
                                    $(val).val(data[key]);
                                }
                                if (type == 3) {                  //查看
                                    $(val).parent().parent().find('.bootstrap-select').addClass('shadow_none');
                                    $(val).parent().parent().find('.dropdown-toggle').addClass('bor_none').attr('disabled', 'disabled').children().addClass('col_black');
                                    $(val).parent().parent().find('.bs-caret').addClass('_hide');
                                    $(val).parent().parent().parent().find('.redStar').addClass('_hide');
                                    let _html = $(val).parent().parent().find('.dropdown-toggle').html().replace('请选择', '');
                                    $(val).parent().parent().find('.dropdown-toggle').html(_html);
                                }
                            })
                            $('.selectpicker').selectpicker('refresh');
                            $('#add_form').find('textarea').each(function (idx, val) {
                                let name = $(val).attr("name");
                                if (key == name && type == 2) {
                                    $(val).val(data[key]);
                                }
                                if (key == name && type == 3) {
                                    let len = (data[key].split('\n')).length;
                                    $(val).val(data[key]).css({ height: len * 30 + 'px' });
                                    $(val).addClass('bor_none shadow_none p_t_0').attr({ 'placeholder': '', 'disabled': 'disabled' });
                                    $(val).parent().parent().find('.redStar').addClass('_hide');
                                }
                            })
                        }
                        $('#responsibleUserName').val(data['responsibleUser']);
                    }

                    if(type == 4){
                    	if(entryType == 1){
                    		search_radio_clear('add_project_name', $('#add_project_id'));
                        	search_radio_give('add_project_name', data.projectName, $('#add_project_id'), data.projectId);
                    	}
                    	
                    	
                        $("#riskDescription").val(isValueNull(data.riskDescription));
                        $("#remark").val(isValueNull(data.remark));
                        $("#systemId").val(data.systemId);
                        $("#commissioningWindowId").val(data.commissioningWindowId);
                        $("#requirementFeatureId").val(data.requirementFeatureId);
                        $("#requirementId").val(data.requirementId);
                        $("#deptName").selectpicker('val', data.deptId);
                        // 附件
                        if(result.attachements != undefined && result.attachements.length > 0){
                            file_param.editAttList = result.attachements;
                            for(var i = 0,len = result.attachements.length; i < len; i++ ){
                                var file = result.attachements[i];
                                var _fileName = file.fileNameOld;
                                var name =  JSON.stringify({id:file.id,fileNameOld:_fileName,fileS3Bucket:file.fileS3Bucket,fileS3Key:file.fileS3Key}).replace(/"/g, '&quot;');
                                var _tr = '';
                                var _td_icon = filePicClassPath(file.fileType); // common.js
                                var _td_name = '<span>'+_fileName+'</span><i class="file-Id"></i></div></td>';
                                var _td_opt = '<td class="font_right"><a href="javascript:void(0);" class="del-file-button" onclick="opsClassProject.file.deleteAtt('+name+',1,this)">删除</a></td>';
                                _tr+='<tr><td><div class="fileTb fileTb_span" onclick="opsClassProject.file.downloadFile('+ name +')" >'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
                                $("#newFileTable").append(_tr);
                            }
                        }
                    }

                    if (type == 3) {      //查看
                        var logs = result.logs;
                        $("#add_tit").text('查看风险记录');
                        $('#add_modal').find('.modal-dialog').addClass('_Max_dialog');
                        $('#add_modal').find('.modal-body').addClass('bg_f2').find('.check_tit').show();
                        $('#add_modal').find('.modal-body').children().eq(0).removeClass('def_col_36').addClass('def_col_25').siblings().show();
                        $('#add_modal').find('.modal-footer').children().eq(1).text('关闭');
                        $('#add_modal').find('.modal-footer').children().eq(0).hide();
                        $('#logs_body').empty();
                        $('.btn_clear').hide();
                        $('textarea').addClass('ov_hidden');
                        logs.length && logs.map(function (val, idx) {
                            let _className = val.logType == '新增风险' ? '' : 'logDiv_contRemark';
                            $('#logs_body').append(`
			                  <div class="logDiv">
			                    <div class="logDiv_title">
			                      <span class="orderNum"></span>
			                      <span>${val.logType}</span>&nbsp;&nbsp;&nbsp;
			                      <span>${val.userName}  | ${val.userAccount}</span>&nbsp;&nbsp;&nbsp;
			                      <span>${val.createDate}</span>
			                    </div>
			                    <div class="logDiv_cont">
			                      <div class="logDiv_contBorder">
			                        <div class="${_className}">
			                          <span>${val.logDetail || ''}</span>
			                        </div>
			                      </div>
			                    </div>
			                  </div>
			                `);
                        })
                    }
                    $("#loading").css('display', 'none');
                    $("#add_modal").modal('show');
                } if (result.status == 2) {
                    layer.alert(result.errorMessage, { icon: 2 });
                }
            }
        });
    }
    if (!type) {
        $('#responsibleUserId').val($('#_userId').val());
        $('#responsibleUserName').val($('#_userName').val());
        $("#add_modal").modal('show');
    }
}

/**
 * @description 新增,编辑提交
 */
function add_submit() {
    $('#add_form').data('bootstrapValidator').validate();
    if (!$('#add_form').data("bootstrapValidator").isValid()) {
        return;
    }
    var formData = new FormData();
    var submit_data = {};
    $('#add_form').find('input').each(function (idx, val) {
        var id = $(val).attr("name");
        submit_data[id] = $.trim($(val).val());
    })
    $('#add_form').find('select').each(function (idx, val) {
        var id = $(val).attr("name");
        submit_data[id] = $(val).val();
    })
    $('#add_form').find('textarea').each(function (idx, val) {
        var id = $(val).attr("name");
        submit_data[id] = $(val).val();
    })
    if ($("#edit_id").val()) {
        submit_data['id'] = $("#edit_id").val();
        formData.append("projectType", toProjectType);
    } else {
        if($('#project_type').val() == 1){
            submit_data['projectId'] = $("#projectId").val();
        }else{
            submit_data['programId'] = $("#projectId").val();
        }
    }
    delete submit_data.responsibleUserName;

    formData.append("riskInfo", JSON.stringify(submit_data));
    $.ajax({
        type: "post",
        url: $("#edit_id").val() ? "/zuul/projectManage/risk/updateRisk" : "/zuul/projectManage/risk/insertRiskInfo",
        dataType: "json",
        data: formData,
        cache: false,
        processData: false,
        contentType: false,
        beforeSend: function () {
            $("#loading").css('display', 'block');
        },
        success: function (data) {
            if (data.status == 1) {
                layer.alert("提交成功", { icon: 1 });
                $("#add_modal").modal("hide");
                $("#project_list").trigger("reloadGrid");
            } if (data.status == 2) {
                layer.alert("提交失败", { icon: 2 });
            }
            $("#loading").css('display', 'none');
        }
    });
}

/**
 * @description 删除记录
 * @param id
 */
function _delete(ID) {
    layer.confirm('确定要删除吗?', {
        btn: ['确定', '取消'],
        icon: 2,
        title: "提示"
    }, function () {
        $.ajax({
            type: "post",
            url: "/projectManage/risk/deleteRiskInfo",
            dataType: "json",
            data: {
                id: ID
            },
            beforeSend: function () {
                $("#loading").css('display', 'block');
            },
            success: function (data) {
                if (data.status == 1) {
                    layer.alert("删除成功", { icon: 1 });
                    $("#project_list").trigger("reloadGrid");
                } if (data.status == 2) {
                    layer.alert("删除失败", { icon: 2 });
                }
                $("#loading").css('display', 'none');
            }
        });
    })
}

/**
 * @description 表单校验
 */
function formValidator() {
    let form_arr = ['riskDescription', 'riskType', 'riskFactor', 'riskProbability', 'riskPriority', 'riskInfluence', 'riskTriggers', 'riskStatus'];
    // let fields = {};
    // form_arr.map((val, idx) => {
    // })
    $('#add_form').bootstrapValidator({
        message: '此项不能为空',//通用的验证失败消息
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        live: 'enabled',
        fields: {
            riskDescription: {
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            riskType: {
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            riskFactor: {
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            riskProbability: {
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            riskInfluence: {
                trigger: 'change',
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            riskTriggers: {
                trigger: 'change',
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            riskStatus: {
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            systemName: {
                trigger: "change",
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            commissioningWindowName: {
                trigger: "change",
                validators: {
                    notEmpty: {
                        message: '此项不能为空'
                    }
                }
            },
            add_project_name: {
            	trigger: "change",
            	validators: {
            		notEmpty: {
            			message: '此项不能为空'
            		}
            	}
            },
        }
    })
}

/**
 * @description 重构表单验证
 */
function refactorFormValidator() {
    $('#add_modal').on('hidden.bs.modal', function () {
        $("#add_form").data('bootstrapValidator').destroy();
        $('#add_form').data('bootstrapValidator', null);
        formValidator();
    })
}

/**
 * @description 人员弹窗
 * @param userName    姓名
 * @param companyName  公司
 * @param deptName    部门
 */
function userTableShowAll() {
    $("#userTable").bootstrapTable('destroy');
    $("#userTable").bootstrapTable({
        url: "/system/user/getAllUserModal2",
        method: "post",
        queryParamsType: "",
        pagination: true,
        sidePagination: "server",
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber: 1,
        pageSize: 10,
        pageList: [5, 10, 15],
        singleSelect: true,//单选
        queryParams: function (params) {
            var param = {
                userName: $.trim($("#userName").val()),
                companyName: $("#companyName").val(),
                deptName: $("#deptName").val(),
                pageNumber: params.pageNumber,
                pageSize: params.pageSize,
            }
            return param;
        },
        columns: [{
            checkbox: true,
            width: "30px"
        }, {
            field: "id",
            title: "id",
            visible: false,
            align: 'center'
        }, {
            field: "userName",
            title: "姓名",
            align: 'center'
        }, {
            field: "userAccount",
            title: "用户名",
            align: 'center'
        }, {
            field: "companyName",
            title: "所属公司",
            align: 'center'
        }, {
            field: "deptName",
            title: "所属处室",
            align: 'center'
        }],
        onLoadSuccess: function () {
            $("#userModal").modal('show');
        },
        onLoadError: function () {

        },
    });
}

/**
 * @description 清空人员搜索
 */
function clearSearchUser() {
    $("#userName").val('');
    $("#deptName").val('');
    $("#companyName").val('');
}

/**
 * @description 选择人员
 */
function commitUser() {
    var selectContent = $("#userTable").bootstrapTable('getSelections')[0];
    if (typeof (selectContent) == 'undefined') {
        layer.alert('请选择一列数据！', { icon: 0 });
        return false;
    } else {
        $("#responsibleUserId").val(selectContent.id);
        $("#responsibleUserName").val(selectContent.userName).change();
        $("#userModal").modal("hide");
    }
}

/**
 * @description liushan:运维类项目的风险管理
 */
var opsClassProject = {
	num : 0,
	total : 0,
	page : 1,
    // 列表
    list : function(){
        $("#project_list").jqGrid({
            url: '/projectManage/risk/getRiskInfo',
            datatype: 'json',
            mtype: "POST",
            height: 'auto',
            rowNum: -1,
            autowidth: true,
            rownumbers: true,
            rownumWidth:50,
            width: $(".content-table").width() * 0.999,
            sortable: false,
            loadtext: "数据加载中......",
            viewrecords: true, //是否要显示总记录数
            jsonReader: {
                repeatitems: false,
                root: "data"
            },
            postData: {
                riskDescription :  $.trim($("#sel_riskDescription").val()),
                systemId :  $.trim($("#sel_system_id").val()),
                commissioningWindowId :  $.trim($("#sel_window_id").val()),
                requirementFeatureId :  $("#sel_Feature_id").val(),
                requirementId :  $("#sel_requirement_id").val(),
                deptName :  $("#sel_deptName").val() != null ?$("#sel_deptName").val().toString():"",
                environmentType :  $("#sel_environmentType").val(),
                projectId: $("#projectId").val(),
                projectType: toProjectType,
                pageSzie: entry_pageSzie
            },
            colNames: [ '风险描述', '涉及子系统', '投产窗口', '测试任务', '需求编号', '所属处室', '环境',  '操作'],
            colModel: [
                { name: 'riskDescription', sortable: false },
                { name: 'systemName', sortable: false },
                { name: 'commissioningWindowName', sortable: false },
                { name: 'requirementFeatureName', sortable: false },
                { name: 'requirementCode', sortable: false },
                { name: 'deptName', sortable: false },
                {
                    name: 'environmentType', sortable: false,
                    formatter: function (value, grid, rows, state) {
                        for (var i = 0, len = environmentTypeDic.length; i < len; i++) {
                            if (value == environmentTypeDic[i][0]) {
                                return environmentTypeDic[i][1];
                            }
                        }
                        return "";
                    }
                },
                {
                    name: '操作', align: "center", fixed: true, sortable: false, resize: false, search: false, width: 130,
                    formatter: function (value, grid, rows, state) {
                    	opsClassProject.num ++;
                        var span_ = "<span>&nbsp;|&nbsp;</span>";
                        var a = '<a class="a_style" onclick="addProject(4,' + rows.id + ')">编辑</a>';
                        var c = '<a class="a_style" onclick="_delete(' + rows.id + ')">删除</a>';
                        var opt_status = [];
                        opt_status.push(a);
                        // 权限控制：只有自己和测试管理岗、测试组长、（组长及以上级别）才能删除任务的控制。
                        if(currentUserId == rows.createBy || (rows.groupUser != null && rows.groupUser.split(",").indexOf(currentUserId) != -1)){
                            opt_status.push(c);
                        }
                        return opt_status.join(span_);
                    }
                }
            ],
            gridComplete: function () {
            },
            beforeRequest: function () {
            	opsClassProject.page = 1;
                $("#loading").css('display', 'block');
            },
            loadComplete: function (data) {
            	opsClassProject.total = data.total;
                $("#loading").css('display', 'none');
            }
        }).trigger("reloadGrid");
        $("#project_list").jqGrid('setLabel','rn', '序号', {'text-align':'center','width':'50px'},'');
    },

    // 列表搜索
    searchInfo  :function(){
        $("#loading").css('display','block');
        scroll_init()
        $("#project_list").jqGrid('setGridParam',{
            postData:{
                riskDescription :  $.trim($("#sel_riskDescription").val()),
                systemId :  $.trim($("#sel_system_id").val()),
                commissioningWindowId :  $.trim($("#sel_window_id").val()),
                requirementFeatureId :  $("#sel_Feature_id").val(),
                requirementId :  $("#sel_requirement_id").val(),
                deptName :  $("#sel_deptName").val() != null ?$("#sel_deptName").val().toString():"",
                environmentType :  $("#sel_environmentType").val(),
                projectId: $("#projectId").val() || $("#sel_project_id").val(),
                pageSzie: entry_pageSzie,
                projectType: toProjectType
            },
            page:1
        }).trigger("reloadGrid"); //重新载入
    },
    

    // 列表加载更多
    load_more: function(page){
    	
    	var _page = opsClassProject.page;
    	if(page){
    		_page = page;
    	}
    	$.ajax({
            type: "post",
            url: '/projectManage/risk/getRiskInfo',
            dataType: "json",
            data: {
                riskDescription :  $.trim($("#sel_riskDescription").val()),
                systemId :  $.trim($("#sel_system_id").val()),
                commissioningWindowId :  $.trim($("#sel_window_id").val()),
                requirementFeatureId :  $("#sel_Feature_id").val(),
                requirementId :  $("#sel_requirement_id").val(),
                deptName :  $("#sel_deptName").val() != null ?$("#sel_deptName").val().toString():"",
                environmentType :  $("#sel_environmentType").val(),
                projectId: $("#projectId").val()||$("#sel_project_id").val(),
                projectType: toProjectType,
                pageSzie: 20,
                page: _page
            },
            beforeSend: function () {
                $("#loading").show();
            },
            success: function (data) {
            	opsClassProject.total = data.total;
        		if(data.data.length){
        			data.data.map(function(v){
            			$("#project_list").jqGrid('addRowData', v.id, v, 'last');
            		})
        		}
            	$("#loading").hide();
            }
    	})

    },
    
    // 搜索清空
    clearSearch:function(){
    	if(entryType == 1){//测试管理-风险管理菜单进入
    		opsClassProject.page = 1;
    		search_radio_clear('sel_project_name', $("#sel_project_id"));
    	}
    	
        $("#sel_riskDescription").val('');
        $("#sel_system_id").val('');
        $("#sel_system").val('');
        $("#sel_window_id").val('');
        $("#sel_window").val('');
        $("#sel_Feature_id").val('');
        $("#sel_Feature").val('');
        $("#sel_requirement_id").val('');
        $("#sel_requirement").val('');
        $("#sel_deptName").selectpicker('val', '');
        $("#sel_environmentType").selectpicker('val', '');
        $(".color1 .btn_clear").css("display", "none");
    },

    // 提交，添加/修改风险数据
    add_submit: function (){
        $('#add_form').data('bootstrapValidator').validate();
        if (!$('#add_form').data("bootstrapValidator").isValid()) {
            return;
        }

        var formData = new FormData();
        if (file_param.formFileList.length > 0) {
            var filesSize = 0;
            for (var i = 0, len2 = file_param.formFileList.length; i < len2; i++) {
                filesSize += file_param.formFileList[i].size;
                formData.append("files", file_param.formFileList[i]);
            }

            if (filesSize > commonFileOpt.fileSizeTotal) {
                layer.alert(commonFileOpt.fileSizeTotalMsg, {
                    icon: 2,
                    title: "提示信息"
                });
                return false;
            }
        }
        var riskInfo = {
            projectId:$("#projectId").val()  || $("#add_project_id").val(),
            riskDescription:htmlEncodeJQ($.trim($("#riskDescription").val())),
            systemId:$("#systemId").val(),
            commissioningWindowId:$("#commissioningWindowId").val(),
            requirementFeatureId:$("#requirementFeatureId").val(),
            requirementId:$("#requirementId").val(),
            deptId:$("#deptName").val(),
            environmentType:$("#environmentType").val(),
            remark:htmlEncodeJQ($.trim($("#remark").val())),
            responsibleUserId:$("#_userId").val()
        };
        if($("#edit_id").val()){
            riskInfo['id'] = $("#edit_id").val();
            formData.append("projectType", toProjectType);
            formData.append("removeFileIds", file_param.removeFileId);
        }

        formData.append("riskInfo", JSON.stringify(riskInfo));
        $.ajax({
            type: "post",
            url: $("#edit_id").val() ? "/zuul/projectManage/risk/updateRisk" : "/zuul/projectManage/risk/insertRiskInfo",
            dataType: "json",
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
            beforeSend: function () {
                $("#loading").css('display', 'block');
            },
            success: function (data) {
                if (data.status == 1) {
                    layer.alert("提交成功", { icon: 1 });
                    $("#add_modal").modal("hide");
                    $("#project_list").trigger("reloadGrid");
                    scroll_init()
                } if (data.status == 2) {
                    layer.alert("提交失败", { icon: 2 });
                }
                $("#loading").css('display', 'none');
            },
            error:errorFunMsg
        });
    },

    modal:{

        // 系统
        system:{
            commitSys:function (){
                var selectContent = $("#systemTable").bootstrapTable('getSelections')[0];
                if(typeof(selectContent) == 'undefined') {
                    layer.alert('请选择一列数据！', {icon: 0});
                    return false;
                }else{
                    if(modal.system == 1){
                        $("#systemId").val(selectContent.id);
                        $("#systemName").val(selectContent.systemName).change();
                    } else if(modal.system == 2){
                        $("#sel_system_id").val(selectContent.id);
                        $("#sel_system").val(selectContent.systemName).change();
                    }
                }
                $("#systemModal").modal('hide');
            },

            // 清空
            resetSystem:function(){
                modal.system = "";
                $("#sel_systemName").val("");
                $("#sel_systemCode").val("");
                select_rows = new Array();
                $("#systemModal .color1 .btn_clear").css("display","none");
            }
        },

        // 测试任务
        feature:{
            commitFeature:function(){
                var rowData = $("#TestPopupTable").bootstrapTable('getSelections')[0];
                if(typeof(rowData) == 'undefined') {
                    layer.alert("请选择一条数据",{
                        icon:2,
                        title:"提示信息"
                    })
                } else {
                    if(modal.feature == 1){
                        $("#requirementFeatureId").val(rowData.ID);
                        $("#requirementFeatureName").val(rowData.FEATURE_NAME).change();
                        $("#requirementId").val(rowData.REQUIREMENT_ID).change(function () {
                            $(this).parent().children(".btn_clear").css("display", "block");
                        }).change();
                        $("#requirementCode").val(rowData.REQ_CODE).change(function () {
                            $(this).parent().children(".btn_clear").css("display", "block");
                        }).change();
                    } else if(modal.feature == 2){
                        $("#sel_Feature_id").val(rowData.ID);
                        $("#sel_Feature").val(rowData.FEATURE_NAME).change();
                    }
                    $("#DevPopup").modal("hide");
                }
            },

            resetFeature:function () {
                modal.feature = "";
                $('#RelationCode').val("");
                $('#RelationName').val("");
                $("#PoStatus").selectpicker('val', '');
                $("#DevPopup .color1 .btn_clear").css("display","none");
            }
        },

        // 投产窗口
        commissioning:{

            commitWin:function(){
                var rowData = $("#ComWindowTable").bootstrapTable('getSelections')[0];
                if (typeof(rowData) == 'undefined' || rowData.length == 0) {
                    layer.alert("请选择一条数据", {
                        icon: 2,
                        title: "提示信息"
                    });
                    return false;
                } else {
                    if(modal.window == 1){
                        $("#commissioningWindowId").val(rowData.id);
                        $("#commissioningWindowName").val(rowData.windowName).change();
                    } else if(modal.window == 2){
                        $("#sel_window_id").val(rowData.id);
                        $("#sel_window").val(rowData.windowName).change();
                    }
                    $("#comWindowModal").modal("hide");
                }
            },

            resetComm:function(){
                modal.window = "";
                select_rows = new Array();
                $("#win_windowName").val('');
                $("#timeDate").val('');
                $("#win_windowType").selectpicker('val', '');
                $("#comWindowModal .color1 .btn_clear").css("display", "none");
            }
        },

        // 需求
        requirement:{
            commitReq:function(){
                var rowData = $("#listReq").bootstrapTable('getSelections')[0];
                if (typeof(rowData) == 'undefined' || rowData.length == 0) {
                    layer.alert("请至少选择一条数据", {
                        icon: 2,
                        title: "提示信息"
                    });
                    return false;
                } else {
                    if(modal.requirement == 1){
                        $("#requirementId").val(rowData.id).change(function () {
                            $(this).parent().children(".btn_clear").css("display", "block");
                        }).change();
                        $("#requirementCode").val(rowData.REQUIREMENT_CODE).change(function () {
                            $(this).parent().children(".btn_clear").css("display", "block");
                        }).change();
                    } else if(modal.requirement == 2){
                        $("#sel_requirement_id").val(rowData.id).change(function () {
                            $(this).parent().children(".btn_clear").css("display", "block");
                        }).change();
                        $("#sel_requirement").val(rowData.REQUIREMENT_CODE).change(function () {
                            $(this).parent().children(".btn_clear").css("display", "block");
                        }).change();
                    }
                    $("#selReq").modal("hide");
                }
            },

            resetReq:function () {
                modal.requirement = "";
                select_rows = new Array();
                $("#reqCode").val('');
                $("#reqName").val('');
                $("#reqStatus").selectpicker('val', '');
                $("#selReq .color1 .btn_clear").css("display", "none");
            }
        }
    },

    file:{
        // 文件上传列表格式
        uploadFileList : function (){
            $("#uploadFile").change(function(){
                var files = this.files;
                if (files.length > 0){
                    for (var i = 0,len = files.length; i < len; i++){
                        var file = files[i];
                        var _fileName = file.name;

                        var flag = opsClassProject.file.etectionFile(file,file_param.formFileList,_fileName,file_param.editAttList);
                        if (flag != false){
                            file_param.formFileList.push(file);

                            //列表展示
                            var name =  JSON.stringify({fileNameOld:_fileName}).replace(/"/g, '&quot;');
                            var _tr = '';
                            var index = _fileName .lastIndexOf("\.");
                            var file_type  = _fileName .substring(index + 1, _fileName.length);
                            var _td_icon = filePicClassPath(file_type); // common.js
                            var _td_name = '<span>'+_fileName+'</span><i class="file-Id"></i></div></td>';
                            var _td_opt = '<td class="font_right"><a href="javascript:void(0);" class="del-file-button" onclick="opsClassProject.file.deleteAtt('+name+',2,this)">删除</a></td>';
                            var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                            _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
                            _table.append(_tr);
                        }
                    }
                }
                $("#uploadFile").val("");
            });
        },

        // 删除附件
        deleteAtt : function (rows,type,that){
            $(that).parent().parent().remove();
            if(type == 1){file_param.removeFileId.push(rows.id);}
            file_param.formFileList.splice(file_param.formFileList.indexOf(rows.fileNameOld),1);
            file_param.editAttList.splice(file_param.editAttList.indexOf(rows.fileNameOld),1);
        },

        // 下载文件
        downloadFile:function (row){
            createNewForm("/zuul/system/file/downloadFile",row,"get");
        },

        // 检测文件
        etectionFile : function (file,formFileList,_fileName,editAttList){
            var _fileSize = file.size;
            var _fileNameSpan = '<span style="color: red">'+_fileName+'</span>';
            if (_fileSize <= 0){
                layer.alert('不能上传空文件！', {
                    icon: 2,
                    title: "提示信息"
                });
                return false;
            }

            if(_fileSize > commonFileOpt.fileSizeOne){
                layer.alert(_fileNameSpan+commonFileOpt.fileSizeOneMsg, {
                    icon: 2,
                    title: "提示信息"
                });
                return false;
            }

            //读取文件
            if (window.FileReader) {
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onerror = function(e) {
                    layer.alert("文件" + _fileNameSpan +" 读取出现错误", {
                        icon: 2,
                        title: "提示信息"
                    });
                    return false;
                };
                reader.onload = function (e) {
                    if(e.target.result) {
                        /*console.log("文件 "+file.name+" 读取成功！");*/
                    }
                };
            }


            if(formFileList != null && formFileList.length > 0 ){
                for(var ii = 0,len = formFileList.length;ii < len ; ii++){
                    if(formFileList[ii].name == _fileName){
                        layer.alert(_fileNameSpan +"  文件已存在，请勿重新添加", {
                            icon: 2,
                            title: "提示信息"
                        });
                        return false;
                    }
                }
            }

            if (editAttList != null && editAttList.length > 0){
                for(var i = 0,len = editAttList.length;i < len;i++){
                    if(_fileName == editAttList[i].fileNameOld){
                        layer.alert(_fileNameSpan +"  文件已存在，请勿重新添加", {
                            icon: 2,
                            title: "提示信息"
                        });
                        return false;
                    }
                }
            }

        }
    }
}


var beforeScrollTop = $('.ui-jqgrid-bdiv').scrollTop();// 滚动高度
var testSet_flag = true;
function scroll_add_data(){
	  var ele = $('.ui-jqgrid-bdiv');
	  ele.on('scroll',function(){
	    var afterScrollTop = ele.scrollTop();// 滚动高度
	    var delta = afterScrollTop - beforeScrollTop;
	    if( delta === 0 ) {
	      return false;
	    }
	    if(delta > 0){
	      var viewH = ele.height();// 可见高度
	      var contentH = ele.get(0).scrollHeight;// 内容高度
//	       console.log("down");
	      beforeScrollTop = afterScrollTop;
	      if (contentH - viewH - afterScrollTop <= 30) {
	        if(testSet_flag){
	          testSet_flag = false;
	          console.log(opsClassProject.num , opsClassProject.total)
	          if( opsClassProject.num < opsClassProject.total){
	        	  opsClassProject.page += 1;
	            opsClassProject.load_more(opsClassProject.page);
	          }
	        }
	      } else {
	        testSet_flag = true;
	      }
	    }else{
//	       console.log("up");
	    }
	  })
}
function scroll_init() {
    opsClassProject.num=0
    opsClassProject.page=1
    beforeScrollTop = 0;// 滚动高度初始化
    testSet_flag = true;
    $('.ui-jqgrid-bdiv').scrollTop(0);
}