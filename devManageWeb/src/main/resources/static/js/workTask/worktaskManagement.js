var _icon_word ="../static/images/devtask/word.png";
var _icon_excel ="../static/images/devtask/excel.png";
var _icon_text = "../static/images/devtask/text.png";
var _icon_pdf = "../static/images/devtask/pdf.png";
var _icon_PNG = "../static/images/file/PNG.png";
var _icon_JPG = "../static/images/file/JPG.png";
var _icon_WENHAO = "../static/images/file/wenhao.png";
var _files = [];
var _editfiles = [];
var _checkfiles = [];
var _handlefiles =[];
var _Newhandlefiles =[];
var _SeeFiles =[];
var _NoreviewFailed=[];
var _newNoReviewFailed=[];
var _Dhandlefiles =[];
var _newDhandlefiles=[];
var Neweditfiles = [];

var NewCodeFiles=[];
var OldCodeFiles = [];
var deleteAttaches = [];
var modalType = '';
var sDevPriority = '';
var requirementFeatureSourceList = '';
var taskStateList = '';
var selectSprints = [];
var modified  = "<span>&nbsp;修改为&nbsp;</span>";

//需求数据字典
var reqStatusList = '';
var reqSourceList = '';
var reqTypeList = '';
var reqPriorityList = '';
var impReqStatusList = '';
var impReqTypeList = '';
var impReqDelayList = '';
var hangupStatusList = '';
var reqPropertyList = '';
var reqClassifyList = '';
var reqSubdivisionList = '';
var dataMigrationList = '';
var featureStatusList = '';
var devtaskStatusList ='';//状态
$(function(){
	//  pageInit();
	search_settings();
	default_list();
	addCheckBox();
    reqStatusList = $("#reqStatus1").find("option");
    reqSourceList = $("#reqSource").find("option");
    reqTypeList = $("#reqType").find("option");
    reqPriorityList=$("#reqPriority").find("option");
    reqPlanList=$("#reqPlan").find("option");
    impReqStatusList=$("#impReqStatus").find("option");
    impReqTypeList=$("#impReqType").find("option");
    impReqDelayList=$("#impReqDelay").find("option");
    hangupStatusList=$("#hangupStatus").find("option");
    reqPropertyList=$("#reqProperty").find("option");
    reqClassifyList=$("#reqClassify").find("option");
    reqSubdivisionList=$("#reqSubdivision").find("option");
    dataMigrationList=$("#dataMigration").find("option");
    featureStatusList=$("#SCfeatureStatus").find("option");
    devtaskStatusList = getReqFeatureStatus();
    //所有的Input标签，在输入值后出现清空的按钮
    buttonClear();
    
    banEnterSearch();
//    $("#developer").val(currentUserName).change();
//    $("#man_devUserId").val(currentUserId);
    sDevPriority = $("#sDevPriority").find("option");
    requirementFeatureSourceList = $("#workSource").find("option");
    taskStateList = $("#taskState").find("option");
    uploadFileList();
    findByStatus();
    downOrUpButton();
    dateComponent();
    /*  getWithinDept();
      getwithinCompany();*/
    getWorkProjectByUser();
    //时间控件 配置参数信息
    $("#startWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
        $(this).parent().children(".btn_clear").css("display","block");
        $("#addDev").data('bootstrapValidator')
            .updateStatus("endWork", 'NOT_VALIDATED')
            .validateField("endWork");
    });

    $("#endWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
        $(this).parent().children(".btn_clear").css("display","block");
        $("#addDev").data('bootstrapValidator')
            .updateStatus("startWork", 'NOT_VALIDATED')
            .validateField("startWork");
    });

    $("#actualStart").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
        $(this).parent().children(".btn_clear").css("display","block");
    });

    $("#actualEnd").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
        $(this).parent().children(".btn_clear").css("display","block");
    });

    dataControl("actualStart","actualEnd");

    $("#DactualStart").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    });
    $("#reviewActualStart").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    }).on('change',function(){
        $(this).parent().children(".btn_clear").css("display","block");
    });

    $("#reviewActualEnd").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "bottom-left"
    }).on('change',function(){
        $(this).parent().children(".btn_clear").css("display","block");
    });
    dataControl("reviewActualStart","reviewActualEnd");

    $("#edstartWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
        $(this).parent().children(".btn_clear").css("display","block");
        $("#editDev").data('bootstrapValidator')
            .updateStatus("edendWork", 'NOT_VALIDATED')
            .validateField("edendWork");
    });

    $("#edendWork").datetimepicker({
        minView:"month",
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        language: 'zh-CN',
        pickerPosition: "top-left"
    }).on('change',function(){
        $(this).parent().children(".btn_clear").css("display","block");
        $("#editDev").data('bootstrapValidator')
            .updateStatus("edstartWork", 'NOT_VALIDATED')
            .validateField("edstartWork");
    });





    /*$("#new_taskUser").click(function(){
        devManPopup();
    });*/

/*    $("#edtaskUser").click(function(){
        devManPopup();
    });*/

    // 搜索 开发人员
    $("#sel_developer_user").click(function(){
        $("#developer").val(currentUserName).change();
        $("#man_devUserId").val(currentUserId);
    });
    $("#add_DevUserId").click(function(){
        $("#new_taskUserId").val(currentUserId).change();
        $("#new_taskUser").val(currentUserName).attr('username',currentUserName).change();
    });
    $("#edit_edtaskUser_user").click(function(){
        $("#edit_taskUserId").val(currentUserId).change();
        $("#edtaskUser").val(currentUserName).attr('username',currentUserName).change();
    });


/*    $("#transferUser_user").click(function(){
        $("#TransferUser").val(currentUserName);
    });
*/
    // 转派 转派给谁
    $("#transferUser_user").click(function(){
        $("#TransferUserId").val(currentUserId).change();
        $("#TransferUser").val(currentUserName).attr('username',currentUserName).change();
    });

    // 分派
    $("#assignUser_user").click(function(){
        $("#assignUserID").val(currentUserId).change();
        $("#assignUser").val(currentUserName).attr('username',currentUserName).change();
    })

    formValidator();
    refactorFormValidator();


    var clipboard = new ClipboardJS("#copyWordCode");
    clipboard.on('success', function(e) {
        layer.msg("<span style='color:white'>复制成功</span>", {
            area:["150px","48px"],
            time:2000
        });
    });
    clipboard.on('error', function(e) {
        layer.msg("<span style='color:white'>复制失败,请手动复制</span>", {
            area:["150px","48px"],
            time:2000
        });
    });
    //ztt 新增页面
    $("#featureCode").change(function(){
        var developmentMode = $("#ndevelopmentMode").val();
        if(developmentMode== 1){//关联的系统是敏态 可以选择选择冲刺

            $("#newsprintDiv").show();

        }else{
            $("#newsprintDiv").hide();
            $("#newSprintId").val('');
            $("#newSprintName").val('');
        }
    })

    //ztt 编辑页面选择开发任务后 change事件
    $("#edfeatureCode").change(function(){
        var developmentMode = $("#edevelopmentMode").val();
        if(developmentMode== 1){//关联的系统是敏态 可以选择选择冲刺
            
            $("#sprintDiv").show();
            $("#workTaskStatus").attr("disabled",false);

        }else{
            $("#sprintDiv").hide();
            $("#editSprintId").val('');
            $("#editSprintName").val('');;
            $("#workTaskStatus").attr("disabled",true);
        }
    });
    $( "#selectdetail .focusInfo" ).bind("click",function(){
        var flag;
        if( $( this ).hasClass("noheart") ){
            $( this ).removeClass("noheart")
            $(this).attr("title","点击不再关注");
            flag = 1;
        }else {
            $( this ).addClass("noheart")
            $(this).attr("title","点击关注");
            flag = 2;
        }
        $.ajax({
            type:"POST",
            url:"/devManage/worktask/changeAttention",
            dataType:"json",
            data:{
                "id":$( "#selectdetail .focusInfo" ).attr("id"),
                "attentionStatus": flag
            },
            success:function( data ){
                if( data.status == "success" ){
                    if( flag ==1 ){
                        layer.alert("关注成功！",{icon:1});
                    }else{
                        layer.alert("取消关注成功！",{icon:1});
                    }
                }else{
                    layer.alert("系统内部错误！！！",{icon:2});
                }
            }
        });
    })
});

function selectMyself(that){
    $($(that).parent().parent().children()[0]).val(currentUserId);
    $($(that).parent().parent().children()[1]).val(currentUserName);
}

//冲刺弹窗
function sprintAll(){
    selectSprints=[];
    $("#sprintModal").modal("show");
    $("#sprintTable").bootstrapTable('destroy');
    $("#sprintTable").bootstrapTable({
        url:"/devManage/sprintManage/getAllsprint",
        method:"post",
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        pageNumber : 1,
        pageSize : 10,
        pageList : [ 5, 10, 15],
        // singleSelect : true,//单选
        queryParams : function(params) {
            var param={
                "sprintName" :  $.trim($("#sprintName2").val()),
                "systemName" :  $("#ssystemName").val(),
                pageNum:params.pageNumber,
                pageSize:params.pageSize,
            }
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
        },{
            field : "id",
            title : "id",
            visible : false,
            align : 'center'
        },{
            field : "sprintName",
            title : "冲刺名称",
            align : 'center'
        },{
            field : "systemName",
            title : "所属系统",
            align : 'center'
        },{
            field : "sprintStartDate",
            title : "开始日期",
            align : 'center'
        },{
            field : "sprintEndDate",
            title : "结束日期",
            align : 'center'
        }],
        onLoadSuccess:function(){

        },
        onLoadError : function() {

        },
        onCheckAll:function(rows){//全选
            for(var i=0;i<rows.length;i++){
                selectSprints.push(rows[i]);
            }
        },
        onUncheckAll: function (rows) {
            for(var i=0;i<rows.length;i++){
                if(selectSprints.indexOf(rows[i])>-1){
                    selectSprints.splice(selectSprints.indexOf(rows[i]),1);
                }
            }
        },
        onCheck:function(row){//选中复选框
            //if(selectWinIds.indexOf(row)<=-1){
            selectSprints.push( row );
            //}
        },
        onUncheck:function(row){//取消复选框
            if(selectSprints.indexOf(row)>-1){
                selectSprints.splice(selectSprints.indexOf(row),1);
            }
        }
    });
}
function commitSprint(){
    if(selectSprints.length<=0){
        layer.alert('请选择一列数据！', {icon: 0});
        return false;
    }else{
        var ids = '';
        var names = '';
        for(var i=0;i<selectSprints.length;i++){
            ids += selectSprints[i].sprintIdList+",";
            names += selectSprints[i].sprintName+',';
        }
        $("#sprintIds").val(ids);
        $("#sprintNames").val(names.substring(0,names.length-1)).change();
        $("#sprintModal").modal("hide");
    }


}
function clearSearchSpr(){
    $("#sprintName2").val("");
    $("#ssystemName").val("");
}

//表格数据加载
function pageInit(){
    var obj1 = {};
//    obj1.userName = $.trim($("#man_devUserId").val());
//    var  devTaskStatus = $("#taskState").val();
//    if(devTaskStatus!=null){
//        obj1.workTaskStatus= devTaskStatus.join(",");
//    }
    obj1.devTaskCode =$.trim($("#taskCode").val());
    obj1.devTaskName = $.trim($("#taskName").val());
    var  devTaskStatus = $("#taskState").val();
    if(devTaskStatus!=null){
        obj1.workTaskStatus= devTaskStatus.join(",");
    }
    obj1.requirementNewCode = $.trim($("#requirement_Code").val());
    obj1.requirementNewName = $.trim($("#requirement_Name").val());
    
    obj1.userNewName = $.trim($("#developer").val());
    obj1.featureCode = $.trim($("#parallelTask").val());
    obj1.systemIds = $("#involveSystem_ids").val();
    obj1.requirementFeatureSource = $.trim($("#workSource").find("option:selected").val());
    obj1.devTaskPrioritys = $.trim($("#sDevPriority").val());
    var createStartDate = '';
    var createEndDate = '';
    var time = $("#dev_createDate").val();
    if(time != undefined){
        var timeArr = time.split(' - ');
        createStartDate = timeArr[0];
        createEndDate = timeArr[1];
    }
    obj1.sprints = $("#sprintIds").val();
    //重新载入
    //ajaxGridOptions: {traditional: true},
    
    obj1 = JSON.stringify(obj1);
//    var obj = {};
//    obj.userName = $.trim(currentUserId);
    var page = $('#devlist').getGridParam('page');
    $("#devlist").jqGrid("clearGridData");
    $("#devlist").jqGrid("setGridParam",{page:page!= null && page != undefined?page:1});
    $("#devlist").jqGrid({
        url:'/devManage/worktask/getAllWorktask',
        datatype: 'json',
        contentType: "application/json; charset=utf-8",
        mtype:"post",
        height: 'auto',
        postData:{
        	"startDate":createStartDate,
            "endDate":createEndDate,
//            "workTask":JSON.stringify(obj),
            "workTask":obj1,
            "getProjectIds":$("#sel_projectName").val() != null?$("#sel_projectName").val().toString():""
        },
        multiselect : true,
        width: $(".content-table").width()*0.999,
        colNames:['id','任务编号', '任务名称','状态','子系统','关联需求','开发任务','优先级','任务来源','开发人员','预估剩余工作量(人天)','操作'],
        colModel:[
            {name:'id',index:'id',hidden:true},
            {name:'devTaskCode',index:'devTaskCode' , sorttype:'string',
                formatter : function(value, grid, rows) {
                    return '<a class="a_style" href="#" onclick="getSee(' + rows.id +','+rows.requirementFeatureId+')">'+value+'</a>';
                }
            },
            {name:'devTaskName',index:'devTaskName',
                formatter : function(value,grid,rows){
                    return '<a class="a_style" href="#" onclick="getSee(' + rows.id+','+ rows.requirementFeatureId +')">'+value+'</a>';
                }
            },
            {name:'devTaskStatus',index:'devTaskStatus',width:115,
                formatter : function(value, grid, rows) {
                    var classDoing = 'doing';
                    for (var i = 0,len = taskStateList.length;i < len;i++) {
                        if(value == taskStateList[i].value){
                            return '<span class="'+classDoing+value+'">'+taskStateList[i].innerHTML+'</span>';
                        }
                    }
                }
            },
            {name:'systemName',index:'systemName'},
            {name:'requirementCode',index:'requirementCode',
                formatter : function(value, grid, rows) {
                    value == null?value='':'';
                    return '<a href="javascript:void(0);" onclick="details('+rows.requirementId+','+rows.parentId+');">'+value+'</a>';
                }
            },
            {name:'featureCode',index:'featureCode',width:70,
                formatter : function(value, grid, rows) {
                    value == null?value='':'';
                    return "<a class='a_style' onclick='showDevTask("+rows.requirementFeatureId+")'>"+value+"</a>";
                }
            },
            {name:'devTaskPriority',index:'devTaskPriority',width:50,
                formatter : function(value, grid, rows) {
                    if(value!=null||value!=undefined){
                        for (var i = 0,len = sDevPriority.length;i < len;i++) {
                            if(value == sDevPriority[i].value){
                                return sDevPriority[i].innerHTML;
                            }
                        }
                    }else{
                        return "";
                    }
                }
            },
            {name:'requirementFeatureSource',index:'requirementFeatureSource',width:80,
                formatter : function(value, grid, rows) {
                    if(value!=null||value!=undefined){
                        for (var i = 0,len = requirementFeatureSourceList.length;i < len;i++) {
                            if(value == requirementFeatureSourceList[i].value){
                                return requirementFeatureSourceList[i].innerHTML;
                            }
                        }
                    }else{
                        return "";
                    }
                }
            },
            {name:'userName',index:'userName',width:70 },
            {name:'estimateRemainWorkload',index:'estimateRemainWorkload',width:50},
            {
                name:'opt',
                index:'操作',
                align:"left",
                width:80,
                sortable:false,
                formatter : function(value, grid, rows, state) {
                	//更多操作下的操作，对应的按钮权限在vm中定义
                    var editOpt = '<a class="a_style" href="#" onclick="edit(\'' + rows.id +'\')">编辑</a>';
                    var HandleOpt = '<a class="a_style" href="#" onclick="Handle(\'' + rows.id +'\')">处理</a>';
                    var TransferOpt = '<a classdevTaskId="a_style" href="#" onclick="Transfer(\'' + rows.id +'\')">转派</a>';
                    var assigOpt = '<a class="a_style" href="#" onclick="assig(\'' + rows.id +'\')">分派</a>';
                    var codeOpt = "<a onclick='restoreSearchOptions()' href='/devManageui/codeReviewControl/toReviewPage?devTaskScmId=" 
                    	+ (rows.tbtsID == null ? "" : rows.tbtsID) + "&devTaskId=" + (rows.devTaskId == null ? "" : rows.devTaskId) 
                    	+ "&scmType=" + (rows.scmType == null ? "" : rows.scmType) + "&codeReviewOrViewResult=2'>代码查看</a>";
                    var opt_status = [];
                    
                    //当前人员是开发人员，或者创建人员或者管理岗人员（关联的开发任务的管理岗）
                    if(rows.devUserId == currentUserId || rows.createBy == currentUserId || rows.manageUserId==currentUserId){
                      //开发工作任务状态：0取消，1待开发，2开发中，3开发完成，4pendding,5代码评审未通过，7Sign-Off,8closed
                    	//开发完成状态，只有编辑
                    	if(rows.devTaskStatus==3){
                            var result="";
                            if (workEdit == true){
                                opt_status.push(editOpt);
                            }
                            //workAppraisal 【评审结果】的权限并且有代码提交记录devTaskId:tbl_dev_task_scm对应的dev_task_id
                            if(workAppraisal==true && rows.devTaskId != null){
                                opt_status.push(codeOpt);
                            }
                            let opt_str = opt_status.join('');
                            return `
	    						<li role="presentation" class="dropdown">
	    							<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
	    							<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
	    						</li>
	    					`;
                        }else if(rows.devTaskStatus==0){
                        	//取消状态，有编辑和代码评审
                            var result="";
                            if (workEdit == true){
                                opt_status.push(editOpt);
                            }
                            if(workAppraisal==true && rows.devTaskId != null){
                                opt_status.push(codeOpt);
                            }
                            let opt_str = opt_status.join('');
                            return `
	    						<li role="presentation" class="dropdown">
	    							<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
	    							<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
	    						</li>
	    					`;
                        }else if(rows.devTaskStatus==4||rows.devTaskStatus==5){
                        	//pendding和代码评审未通过：编辑
                            var result="";
                            if (workEdit == true){
                                result += editOpt;
                            }
                            let opt_str = result;
                            return `
	    						<li role="presentation" class="dropdown">
	    							<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
	    							<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
	    						</li>
	    					`;
                        }else if(rows.userName!=""&&rows.userName!=null&&rows.userName!=undefined){//开发人员不为空
                           //开发人员拥有的权限：编辑，转派，处理，代码评审
                        	result +='';
                            if(workEdit==true){
                                opt_status.push(editOpt);
                            }

                            if(workTransfer==true){
                                opt_status.push(TransferOpt);
                            }
                            if(workHandle==true){
                                opt_status.push(HandleOpt);
                            }
                            if(workAppraisal==true && rows.devTaskId != null){
                                opt_status.push(codeOpt);
                            }
                            let opt_str = opt_status.join('');
                            return `
	    						<li role="presentation" class="dropdown">
	    							<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
	    							<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
	    						</li>
	    					`;
                        }else{
                        	//创建者和管理岗的人有用的权限：编辑，分派，处理权限
                            var result="";
                            if(workEdit==true){
                                opt_status.push(editOpt);
                            }
                            if(workAssign==true){
                                opt_status.push(assigOpt);
                            }
                            if(workHandle==true){
                                opt_status.push(HandleOpt);
                            }
                            let opt_str = opt_status.join('');
                            return `
	    						<li role="presentation" class="dropdown">
	    							<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
	    							<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
	    						</li>
	    					`;
                        }

                    } else {
                    	//其他人只有代码评审的权限
                        if(workAppraisal==true && rows.devTaskId != null){
                            opt_status.push(codeOpt);
                            let opt_str = opt_status.join('');
                            return `
	    						<li role="presentation" class="dropdown">
	    							<a class="dropdown-toggle a_style _select_box_show" data-toggle="dropdown" role="button">更多操作</a>
	    							<ul class="dropdown-menu _select_box_menu" id="dropdown_menu">${opt_str}</ul>
	    						</li>
	    					`;
                        }
                        return  "";
                    }
                }
            }
        ],
        rowNum:10,
        rowTotal: 200,
        rowList : [10,20,30],
        rownumWidth: 40,
        pager: '#devpager',
        sortable:true,   //是否可排序
        sortorder: 'desc',
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true, //是否要显示总记录数
        loadComplete :function(){
        	$("#colGroup").find('input').each(function(idx,val){
          		if ($(val).is(':checked')) {
          			$(collection_obj.table_list).setGridParam().hideCol($(val).attr('value'));
          			$(collection_obj.table_list).setGridWidth($('.wode').width());
          		}
          	})
            var myGrid = $("#devlist");
            $("#cb_"+myGrid[0].id).hide();
            $("#loading").css('display','none');
        },
        beforeSelectRow: function(rowid, e){
            $("#devlist").jqGrid('resetSelection');
            return(true);
        },
        beforeRequest:function(){
            $("#loading").css('display','block');
        }
    }).trigger("reloadGrid");
}


function findByStatus(){
    var ids = $("#devTaskStatus").val();
    var arr = ids.split(",");
    $("#taskState option").each( function (i, n) {
        for (var j = 0,len = arr.length; j < len ; j++){
            if (n.value == arr[j]) {
                n.selected = true;
            }
        }
    });
}




/*-----------查看开发任务信息-------------*/
//查看
function showDevTask(id) {
    var id = id;
    $("#loading").css('display','block');
    $.ajax({
        url:"/devManage/devtask/getOneDevTask2",
        type:"post",
        dataType:"json",
        data:{
            "id":id
        },
        success: function(data) {
            $("#loading").css('display','none');
            $("#checkoutrequirementDiv").hide();
            $("#checkDefectDiv").hide();
            $("#checkRequstNumberDiv").hide();
            $("#checksystemName").text('');
            $("#checkdeptName").text('');
            $("#checkReqFeaturefiles").val('');
            $("#checkReqFeatureConnectDiv").empty();
            $("#brother_div").empty();
            $("#checkFileTable").empty();
            $("#remarkBody").empty();
            $("#remarkDetail").val('');
            $("#checkReqAttTable").empty();
            $("#checkReqFeatureHandleLogs").empty();
            $("#brother_div").empty();
            $("#checkdevTaskTitle").text( toStr(data.featureCode) +"     "+ toStr(data.featureName) );
            $("#checkdevTaskOverview").text(data.featureOverview);
            var statusName = '';
            for (var i = 0,len = devtaskStatusList.length;i < len;i++) {
                if(devtaskStatusList[i].valueCode == data.requirementFeatureStatus){
                    statusName=  devtaskStatusList[i].valueName;
                    break;
                }
            }
            $("#checkdevTaskStatus").text(statusName);
            $("#checkdevManPost").text("");
            $("#checkdevManPost").text(data.manageUserName);
            $("#checkexecutor").text("");
            $("#checkexecutor").text(data.executeUserName);
            $("#checksystemName").text("");
            $("#checksystemName").text(data.systemName);
            $("#checkoutrequirement").text("");
            $("#checkRequstNumber").text("");

            $("#checkWindowName").text("");
            $("#checkWindowName").text(data.windowName);
            $("#checkSystemVersion").text("");
            $("#checkSystemVersion").text(toStr(data.versionName)+"-->"+toStr(data.systemScmBranch));
            //开发任务来源
            if(data.requirementFeatureSource!=undefined){
                if(data.requirementFeatureSource==1){//业务需求
                    $("#checkoutrequirementDiv").show();
                    $("#checkDefectDiv").hide();
                    $("#checkRequstNumberDiv").hide();
                    $("#checkoutrequirement").text(toStr(data.requirementCode)+" | "+toStr(data.requirementName));
                }else if(data.requirementFeatureSource==2){//生产问题
                    $("#checkoutrequirementDiv").hide();
                    $("#checkDefectDiv").hide();
                    $("#checkRequstNumberDiv").show();
                    $("#checkRequstNumber").text(data.questionNumber);
                }else if(data.requirementFeatureSource==3){//测试缺陷
                    $("#checkoutrequirementDiv").hide();
                    $("#checkDefectDiv").show();
                    $("#checkRequstNumberDiv").hide();
                    var dftName='';
                    for(var i=0;i<data.defectInfos.length;i++){
                        if(data.defectInfos[i].requirementFeatureId == id){
                            dftName+= data.defectInfos[i].defectCode+",";
                        }
                    }
                    $("#checkDefect").text(dftName.substring(0,dftName.length-1));
                }
            }
            $("#checkdeptName").text(data.deptName);
            var status = "";
            if(data.temporaryStatus =="1"){
                status = "是";
            }else if(data.temporaryStatus =="2"){
                status = "否";
            }
            $("#checktemporaryTask").text(status);//1是2否
            $("#checkpreStartDate").text('');
            $("#checkpreStartDate").text(data.planStartDate);
            $("#checkpreEndDate").text('');
            $("#checkpreEndDate").text(data.planEndDate);
            $("#checkpreWorkload").text('');
            $("#checkpreWorkload").text(data.planWorkload);
            $("#checkactStartDate").text('');
            $("#checkactStartDate").text(data.actualStartDate);
            $("#checkactEndDate").text('');
            $("#checkactEndDate").text(data.actualEndDate);
            $("#checkactWorkload").text('');
            $("#checkactWorkload").text(data.actualWorkload);
            $("#checkhandSug").text('');
            $("#checkhandSug").text(data.handleSuggestion);
            $("#checkReqFeatureId").val(id);
            var type = '';
            if(data.createType == "1" ){
                type = "自建";
            }else if(data.createType == "2"){
                type = "同步";
            }
            $("#checkcreateType").text(type);
            //相关工作任务的显示
            if(data.list!=undefined){
                for(var i=0;i<data.list.length;i++){
                    $("#checkReqFeatureConnectDiv").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="getSee('+data.list[i].id+','+id+')">'+data.list[i].devTaskCode+'</a>'+'   '+data.list[i].devTaskName+'</div>'+
                        '<div class="def_col_36">实际工作情况：'+toStr(data.list[i].actualStartDate)+'~'+toStr(data.list[i].actualEndDate)+' '+toStr(data.list[i].actualWorkload)+'人天</div>'+
                        '<div class="def_col_36">'+data.list[i].devtaskStatusName+' '+toStr(data.list[i].devUsername)+'</div></div>');
                }
            }
            //相关开发任务的显示
            if(data.brother!=undefined){
                for(var i=0;i<data.brother.length;i++){
                    if(data.brother[i].featureCode==undefined){
                        data.brother[i].featureCode="";
                    }
                    $("#brother_div").append( '<div class="rowdiv "><div class="def_col_36 fontWeihgt"><a class="a_style" onclick="showDevTask('+data.brother[i].id+')">'+data.brother[i].featureCode+'</a>'+'  '+data.brother[i].featureName+'</div>'+
                        '<div class="def_col_36">实际工作情况：'+toStr(data.brother[i].actualStartDate)+'~'+toStr(data.brother[i].actualEndDate)+' '+toStr(data.brother[i].actualWorkload)+'人天</div>'+
                        '<div class="def_col_36">'+data.brother[i].statusName+' '+toStr(data.brother[i].executeUserName)+' 预排期：'+toStr(data.brother[i].windowName)+'</div>');
                }
            }
            //相关附件显示
            if(data.attachements!=undefined){
                var _table = $("#checkReqFeatureFileTable");
                for(var i=0;i<data.attachements.length;i++){
                    var _tr = '';
                    var file_name = data.attachements[i].fileNameOld;
                    var file_type = data.attachements[i].fileType;
                    var _td_icon;
                    var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.attachements[i].fileS3Bucket+'</i><i class = "file-s3Key">'+data.attachements[i].fileS3Key+'</i></td>';
                    switch(file_type){
                        case "doc":
                        case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                        case "xls":
                        case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                        case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                        case "pdf":_td_icon = '<td><img src="'+_icon_pdf+'" />';break;
                        default:_td_icon = '<img src="'+_icon_word+'" />';break;
                    }
                    _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download1(this)">'+_td_icon+" "+_td_name+'</tr>';

                    _table.append(_tr);
                }
            }
            //备注
            _checkfiles = [];
            if(data.remarks!=undefined){
                var str ='';
                for(var i=0;i<data.remarks.length;i++){
                    var style= '';
                    if(i==data.remarks.length-1){
                        style= ' lastLog';
                    }
                    str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span>'+
                        '<span>'+data.remarks[i].userName+'  | '+data.remarks[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.remarks[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
                        '<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><span>'+data.remarks[i].requirementFeatureRemark+'</span>'+
                        '<div class="file-upload-list">';
                    if(data.remarks[i].remarkAttachements.length>0){
                        str +='<table class="file-upload-tb">';
                        var _trAll = '';
                        for(var j=0;j<data.remarks[i].remarkAttachements.length;j++){

                            var _tr = '';
                            var file_name = data.remarks[i].remarkAttachements[j].fileNameOld;
                            var file_type = data.remarks[i].remarkAttachements[j].fileType;
                            var _td_icon;
                            var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.remarks[i].remarkAttachements[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.remarks[i].remarkAttachements[j].fileS3Key+'</i></td>';
                            switch(file_type){
                                case "doc":
                                case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                                case "xls":
                                case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                                case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                                case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                                default:_td_icon = '<img src="'+_icon_word+'" />';break;
                            }
                            _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download1(this)">'+_td_icon+_td_name+'</tr>';
                            _trAll +=_tr;
                        }
                        str+= _trAll+'</table>';
                    }

                    str += '</div></div></div></div></div>';

                }
                $("#remarkBody").append(str);

            }

            //处理日志
            if(data.logs!=undefined){
                var str ='';
                for(var i=0;i<data.logs.length;i++){
                    var style= '';
                    if(i==data.logs.length-1){
                        style= ' lastLog';
                    }
                    var addDiv = '';
                    var logDetail = '';
                    var style2 = '';

                    if((data.logs[i].logDetail==null || data.logs[i].logDetail=='')&&(data.logs[i].logAttachements==null || data.logs[i].logAttachements.length<=0)){
                        if(data.logs[i].logType!="新增开发任务"){
                            logDetail = "未作任何修改";
                        }
                        if(logDetail==''){
                            style2= 'style="display: none;"';
                        }
                        addDiv = '<br>';
                    }else{
                        logDetail = data.logs[i].logDetail;
                        logDetail=logDetail.replace(/；/g,"<br/>");
                        logDetail = logDetail.replace(/null/g,' ');
                    }

                   //组装处理日志
                    str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span><span>'+data.logs[i].logType+'</span>&nbsp;&nbsp;&nbsp;'+
                        '<span>'+data.logs[i].userName+'  | '+data.logs[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.logs[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
                        '<div class="logDiv_cont" ><div class="logDiv_contBorder"><div class="logDiv_contRemark" '+style2+'><span>'+logDetail+'</span>'+
                        '<div class="file-upload-list">';
                    if(data.logs[i].logAttachements.length>0){
                        str +='<table class="file-upload-tb">';
                        var _trAll = '';
                        for(var j=0;j<data.logs[i].logAttachements.length;j++){
                            var attType = '';
                            if(data.logs[i].logAttachements[j].status == 1){//新增的日志
                                attType = "<lable>新增附件：</lable>";
                            }else if(data.logs[i].logAttachements[j].status == 2){//删除的日志
                                attType = "<lable>删除附件：</lable>";
                            }
                            var _tr = '';
                            var file_name = data.logs[i].logAttachements[j].fileNameOld;
                            var file_type = data.logs[i].logAttachements[j].fileType;
                            var _td_icon;
                            var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+data.logs[i].logAttachements[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.logs[i].logAttachements[j].fileS3Key+'</i></td>';
                            switch(file_type){
                                case "doc":
                                case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                                case "xls":
                                case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                                case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                                case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                                default:_td_icon = '<img src="'+_icon_word+'" />';break;
                            }
                            _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download(this)">'+attType+_td_icon+_td_name+'</tr>';
                            _trAll +=_tr;
                        }
                        str+= _trAll+'</table>';
                    }

                    str += '</div></div>'+addDiv+'</div></div></div>';

                }
                $("#checkReqFeatureHandleLogs").append(str);
            }

        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
    modalType = 'check';
    $("#checkDevTask").modal("show");
}

//文件下载
function download1(that){
    var fileS3Bucket = $(that).children(".file-bucket").text();
    var fileS3Key = $(that).children(".file-s3Key").text();
    var fileNameOld = $(that).children("span").text();
    var url = encodeURI("/devManage/devtask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld);
    window.location.href = url;

}

// 为空或者未定义或者为NULL或者是字符串去空格
function toStr(value1){
    if(value1==undefined ||value1==null||value1=="null"||value1=="NULL"){
        return "";
    }else if(!isNaN(value1)){
        return value1;
    }else{
        return value1.toString().trim();
    }
}

//提交备注
function saveRemark(){
    var remark = $("#remarkDetail").val();
    var id = $("#checkReqFeatureId").val();
    if($("#remarkDetail").val()==''){
        layer.alert('备注内容不能为空！！！', {icon: 0});
        return;
    }
    $.ajax({
        url:"/devManage/devtask/addRemark",
        dataType:"json",
        type:"post",
        data:{
            "id":id,
            "requirementFeatureRemark":remark,
            "attachFiles" :$("#checkfiles").val()
        },
        success : function(data){
            if(data.status == "success"){
                layer.alert('保存成功！', {icon: 1});
                showDevTask(id);
                _checkfiles = [];
                $("#checkfiles").val('');
            }else if(data.status =="2"){
                layer.alert('保存失败！！！', {icon: 2});
            }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
}

/*-----------需求编码链接-------------*/
function details(rid,parentId){
    $("#checkSysModal").empty();
    cleanDetails();
    $.ajax({
        type:"POST",
        url:"/projectManage/requirement/findRequirementById",
        dataType:"json",
        data:{
            "rIds":rid,
            "parentIds":parentId
        },
        success:function(data){
            if(data.status == 1){
                $("#itcdReqId").val();
                $("#itcdReqId").val(data['data'].itcdRequirementId);
                $("#reqCodeDetails").text( data["data"].requirementCode +" | " + data["data"].requirementName);
                addSysModal( data.data.list )
                showField( data.fields )
                //基本信息
                if(data["data"].requirementSource!=null){
                    for (var i = 0,len = reqSourceList.length;i < len;i++) {
                        if(data["data"].requirementSource.toLowerCase() == reqSourceList[i].value.toLowerCase()){
                            $("#reqSourceNameDetails").html(reqSourceList[i].innerHTML);
                        }
                    }
                }
                if(data["data"].requirementType!=null){
                    for (var i = 0,len = reqTypeList.length;i < len;i++) {
                        if(data["data"].requirementType.toLowerCase() == reqTypeList[i].value.toLowerCase()){
                            $("#reqTypeNameDetails").html(reqTypeList[i].innerHTML);
                        }
                    }
                }
                if(data["data"].requirementPriority!=null){
                    for (var i = 0,len = reqPriorityList.length;i < len;i++) {
                        if(data["data"].requirementPriority.toLowerCase() == reqPriorityList[i].value.toLowerCase()){
                            $("#reqPriorityNameDetails").html(reqPriorityList[i].innerHTML);
                        }
                    }
                }
                if(data["data"].requirementStatus!=null){
                    for (var i = 0,len = reqStatusList.length;i < len;i++) {
                        if(data["data"].requirementStatus.toLowerCase() == reqStatusList[i].value.toLowerCase()){
                            $("#reqStatusNameDetails").html(reqStatusList[i].innerHTML);
                        }
                    }
                }
                if(data["data"].requirementPlan!=null){
                    for (var i = 0,len = reqPlanList.length;i < len;i++) {
                        if(data["data"].requirementPlan.toLowerCase() == reqPlanList[i].value.toLowerCase()){
                            $("#reqPlanNameDetails").html(reqPlanList[i].innerHTML);
                        }
                    }
                }
                $("#expectOnlineDateDetails").html(timestampToTime(data["data"].expectOnlineDate));
                $("#planOnlineDateDetails").html(timestampToTime(data["data"].planOnlineDate));
                $("#actualOnlineDateDetails").html(timestampToTime(data["data"].actualOnlineDate));
                $("#userNameDetails").html(data["data"].userName);
                $("#deptNameDetails").html(data["data"].deptName);
                $("#devDeptNameDetails").html(data["data"].devDeptName);
                var triFather= data["triFather"];
                var triSon=data["triSon"];
                if(triFather==undefined){
                    $("#relevancyDetails").html(triSon);  //关联需求
                }else{
                    $("#relevancyDetails").html(triFather.requirementCode+" | "+triFather.requirementName+", "+triSon);  //关联需求
                }
                $("#createDateDetails").html(timestampToTime(data["data"].createDate));
                $("#lastUpdateDateDetails").html(timestampToTime(data["data"].lastUpdateDate));
                $("#openDateDetails").html(timestampToTime(data["data"].openDate));
                $("#requirementOverviewDetails").html(data["data"].requirementOverview);
                $("#checkReqs").html('');
                var requirementNames = data["data"].requirementNames;
                if(requirementNames != null){
                    $("#checkReqs").html(requirementNames.join(","));
                }
                $("#checkCloseTime").html(data["data"].closeTime);
                $("#relevancyDetails").html(data["data"].parentCode);
                //重点需求
                $("#impReqStatusNameDetails").html(data["data"].impReqStatusName);
                for (var i = 0,len = impReqStatusList.length;i < len;i++) {
                    if(data["data"].importantRequirementStatus == impReqStatusList[i].value){
                        $("#impReqStatusNameDetails").html(impReqStatusList[i].innerHTML);
                    }
                }
                for (var i = 0,len = impReqTypeList.length;i < len;i++) {
                    if(data["data"].importantRequirementType == impReqTypeList[i].value){
                        $("#impReqTypeDetails").html(impReqTypeList[i].innerHTML);
                    }
                }
                for (var i = 0,len = impReqDelayList.length;i < len;i++) {
                    if(data["data"].importantRequirementDelayStatus == impReqDelayList[i].value){
                        $("#impReqDelayStatusNameDetails").html(impReqDelayList[i].innerHTML);
                    }
                }
                $("#impReqOnlineQuarterDetails").html(data["data"].importantRequirementOnlineQuarter+" 季度");
                $("#impReqDelayReasonDetails").html(data["data"].importantRequirementDelayReason);
                //成本与收益
                $("#directIncomeDetails").html(data["data"].directIncome);
                $("#forwardIncomeDetails").html(data["data"].forwardIncome);
                $("#recessiveIncomeDetails").html(data["data"].recessiveIncome);
                $("#directCostReductionDetails").html(data["data"].directCostReduction);
                $("#forwardCostReductionDetails").html(data["data"].forwardCostReduction);
                $("#anticipatedIncomeDetails").html(data["data"].anticipatedIncome);
                $("#estimateCostDetails").html(data["data"].estimateCost);
                //其他信息
                for (var i = 0,len = hangupStatusList.length;i < len;i++) {
                    if(data["data"].hangupStatus == hangupStatusList[i].value){
                        $("#hangupStatusNameDetails").html(hangupStatusList[i].innerHTML);
                    }
                }
                $("#hangupDateDetails").html(timestampToTime(data["data"].hangupDate));
                $("#changeCountDetails").html(data["data"].changeCount);
                $("#devManageUserNameDetails").html(data["data"].devManageUserName);
                $("#reqManageUserNameDetails").html(data["data"].reqManageUserName);
                $("#reqAcceptanceUserNameDetails").html(data["data"].reqAcceptanceUserName);
                for (var i = 0,len = reqPropertyList.length;i < len;i++) {
                    if(data["data"].requirementProperty == reqPropertyList[i].value){
                        $("#requirementPropertyDetails").html(reqPropertyList[i].innerHTML);
                    }
                }
                for (var i = 0,len = reqClassifyList.length;i < len;i++) {
                    if(data["data"].requirementClassify == reqClassifyList[i].value){
                        $("#requirementClassifyDetails").html(reqClassifyList[i].innerHTML);
                    }
                }
                for (var i = 0,len = reqSubdivisionList.length;i < len;i++) {
                    if(data["data"].requirementSubdivision == reqSubdivisionList[i].value){
                        $("#requirementSubdivisionDetails").html(reqSubdivisionList[i].innerHTML);
                    }
                }
                $("#planIntegrationTestDateDetails").html(timestampToTime(data["data"].planIntegrationTestDate));
                $("#actualIntegrationTestDateDetails").html(timestampToTime(data["data"].actualIntegrationTestDate));
                $("#acceptanceDescriptionDetails").html(data["data"].acceptanceDescription);
                if(data["data"].acceptanceTimeliness=="1"){
                    $("#acceptanceTimelinessDetails").html("及时");
                }else{
                    $("#acceptanceTimelinessDetails").html("非及时");
                }
                for (var i = 0,len = dataMigrationList.length;i < len;i++) {
                    if(data["data"].dataMigrationStatus == dataMigrationList[i].value){
                        $("#dataMigrationStatusNameDetails").html(dataMigrationList[i].innerHTML);
                    }
                }
                $("#workloadDetails").html(data["data"].workload);
                $("#connectDiv").empty();
                var code = data.featureCode == undefined ? "": data.featureCode +" | ";
                var trf= data["trf"];
                console.log( data["trf"] );
                if(trf!=undefined){
                    for(var i=0;i<trf.length;i++){
                        var noBorder="";
                        var featureStatusObj = getStatusName( trf[i].requirementFeatureStatus );
                        
                        if( i == 0 ){
                            noBorder = "noBorder"
                        }
                        var str='<div class="oneTask '+noBorder+'">'+
                            '<div class="name">'+
                            '<span>'+trf[i].featureCode+'</span>'+
                            '<span class="status status'+featureStatusObj.num+'">'+featureStatusObj.name+'</span>'+
                            '</div>'+
                            '<div class="describe">'+trf[i].featureName+'</div>'+
                            '<div class="rowdiv">'+
                            '<div class="def_col_22 large">'+
                            '<span class="fa fa-th-large"></span> '+
                            '<span> 关联系统:'+delUndefined(trf[i].systemName)+'</span>'+
                            '</div>'+
                            '<div class="def_col_14 user"> '+
                            '<span class="fa fa-user"></span>'+
                            '<span> 执行人:'+delUndefined(trf[i].userName)+'</span>'+
                            '</div>'+
                            '</div>'+
                            '</div>';
                        $("#connectDiv").append( str );
                        var workTask = findWorkTask(trf[i].id);
                        for(var j=0;j<workTask.length;j++){
                            var workStr='';
                            var noBorder='';
                            var workTaskObj =  getWorkTaskStatus( workTask[j].devtaskStatus );
                            var workStr='<div class="oneWorkTask '+noBorder+'">'+
                                '<div class="name">'+
                                '<div class="rowdiv">'+
                                '<div class="def_col_24 large">'+
                                '<span class="fa fa-check-square"></span> '+
                                '<span title="'+workTask[j].devTaskCode+'"> '+workTask[j].devTaskCode+'</span>'+
                                '</div>'+
                                '<div class="def_col_12 user"> '+
                                '<span class="fa fa-user"></span>'+
                                '<span title="'+delUndefined(workTask[j].devUsername)+'"> '+delUndefined(workTask[j].devUsername)+'</span>'+
                                '</div>'+
                                '</div>'+
                                '<span class="status status0'+workTaskObj.num+'">'+workTaskObj.name+'</span>'+
                                '</div>'+
                                '<div class="describe">'+workTask[j].devTaskName+'</div>'+
                                '</div>';

                            $("#connectDiv").append( workStr );
                        }

                    }
                }
                attReqList(data.attachements,"#checkReqFileTable");
            }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
//    cleanDetails();
    $("#details").modal("show");
}

//详情显示时，先清楚之前的内容
function cleanDetails(){
    $("#reqCodeDetails").html('');
    $("#reqNameDetails").html('');
    //基本信息
    $("#reqSourceNameDetails").html('');
    $("#reqTypeNameDetails").html('');
    $("#reqPriorityNameDetails").html('');
    $("#reqStatusNameDetails").html('');
    $("#reqPlanNameDetails").html('');
    $("#expectOnlineDateDetails").html('');
    $("#planOnlineDateDetails").html('');
    $("#actualOnlineDateDetails").html('');
    $("#userNameDetails").html('');
    $("#deptNameDetails").html('');
    $("#devDeptNameDetails").html('');
    $("#relevancyDetails").html(''); //关联需求

    $("#createDateDetails").html('');
    $("#lastUpdateDateDetails").html('');
    $("#openDateDetails").html('');
    $("#requirementOverviewDetails").html('');
    //重点需求
    $("#impReqStatusNameDetails").html('');
    $("#impReqTypeDetails").html('');
    $("#impReqDelayStatusNameDetails").html('');
    $("#impReqOnlineQuarterDetails").html('');
    $("#impReqDelayReasonDetails").html('');
    //成本与收益
    $("#directIncomeDetails").html('');
    $("#forwardIncomeDetails").html('');
    $("#recessiveIncomeDetails").html('');
    $("#directCostReductionDetails").html('');
    $("#forwardCostReductionDetails").html('');
    $("#anticipatedIncomeDetails").html('');
    $("#estimateCostDetails").html('');
    //其他信息
    $("#hangupStatusNameDetails").html('');
    $("#hangupDateDetails").html('');
    $("#changeCountDetails").html('');
    $("#devManageUserNameDetails").html('');
    $("#reqManageUserNameDetails").html('');
    $("#reqAcceptanceUserNameDetails").html('');
    $("#requirementPropertyDetails").html('');
    $("#requirementClassifyDetails").html('');
    $("#requirementSubdivisionDetails").html('');
    $("#planIntegrationTestDateDetails").html('');
    $("#actualIntegrationTestDateDetails").html('');
    $("#acceptanceDescriptionDetails").html('');
    $("#acceptanceTimelinessDetails").html('');
    $("#dataMigrationStatusNameDetails").html('');
    $("#workloadDetails").html('');
}

//添加模块时弹框
function addSysModal( data ){
    if( data == null ){
        return ;
    }
    for( var i=0;i<data.length;i++ ){
        var str = '<div class="rowdiv"><div class="form-group col-md-4">'+
            '<label class="control-label fontWeihgt">系统:</label>'+
            '<label class="control-label font_left"><span>'+isValueNull( data[i].systemName )+'</span></label></div>'+
            '<div class="form-group col-md-4">'+
            '<label class="control-label fontWeihgt">模块:</label>'+
            '<label class="control-label font_left"><span>'+isValueNull( data[i].assetSystemName )+'</span></label></div>'+
            '<div class="form-group col-md-4">'+
            '<label class="control-label fontWeihgt">功能点数:</label>'+
            '<label class="control-label font_left"><span>'+isValueNull( data[i].functionCount )+'</span></label></div></div>';
        $( "#checkSysModal" ).append( str );
    }
}

//将开发任务状态CODE转换为名称
function getStatusName( status ){
    var obj={};
    for (var j = 0,len = featureStatusList.length;j < len;j++) {
        if(status == featureStatusList[j].value){
            obj.name = featureStatusList[j].innerHTML;
            obj.num = featureStatusList[j].value;
        }
    }
    return obj;
}

//处理undefined的显示
function delUndefined(str){
    if(str==undefined){
        str="";
    }
    return str;
}

//将工作任务状态CODE转换为名称
function getWorkTaskStatus( status ){
    var obj={};
    obj.name = "无状态";
    obj.num = 0;
    for (var j = 0,len = devTaskStatus.length;j < len;j++) {
        if(status == devTaskStatus[j].value){
            obj.name = devTaskStatus[j].innerHTML;
            obj.num = devTaskStatus[j].value;
        }
    }
    return obj;
}

function timestampToTime(timestamp) {
    var Y,M,D;
    if(timestamp!=null){
        var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        Y = date.getFullYear() + '年';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '月';
        D = date.getDate() + '日';
        //var h = date.getHours() + ':';
        //var m = date.getMinutes() + ':';
        //var s = date.getSeconds();
    }
    return Y+M+D;
}

var editAttList = [];

// 附件列表后台接口
function attReqList(att,FileTable){
    attReqListTable(att,FileTable);
    if (att != null && att.length > 0){
        for( var i = 0,len = att.length;i < len ; i++){
            editAttList.push(att[i].fileNameOld);
        }
    }
}
/*附件列表*/
function attReqListTable(data,idName){
    fileTableName = idName;
    $(idName).bootstrapTable("destroy");
    $(idName).bootstrapTable({
        pagination : false,
        showHeader: false,
        sidePagination:'client',
        data:data,
        showFooter: false,
        columns : [{
            field : "fileNameOld",
            title : "fileNameOld",
            align : 'left',
            formatter : function(value, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                var classType = '';
                classType = classPath(row.fileType,classType);

                if (idName == "#checkFileTable"){
                    return '<div class="fileTb" style="cursor:pointer;">'+classType
                        +'    <a style="color:#454545 !important;" download="'+1+'" onclick="downloadFile('+rows+')">'+row.fileNameOld+'</a></div>';
                } else if (idName == "#editFileTable"){
                    return '<div class="fileTb">'+classType+'    '+row.fileNameOld+'</div>';
                }
            }
        },{
            field : "opt",
            title : "操作",
            align : 'center',
            formatter : function(value, row, index) {
                var rows = JSON.stringify(row).replace(/"/g, '&quot;');
                if (idName == "#checkReqFileTable"){
                    return '';
                }

            }
        }],
        onLoadSuccess:function(){
            $("#loading").css('display','none');
        },
        onLoadError : function() {

        },
        formatNoMatches: function(){
            return "";
        }
    });
}

//根据附件类型，显示不同的图标
function classPath(fileType,classType){
    switch (fileType){
        case "doc":
        case "docx":
            classType = '<img src="'+_icon_word+'" />';
            break;
        case "xls":
        case "xlsx":
            classType = '<img src="'+_icon_excel+'" />';
            break;
        case "txt":
            classType = '<img src="'+_icon_text+'" />';
            break;
        case "pdf":
            classType = '<img src="'+_icon_pdf+'" />';
            break;
        case "JPG":
        case "jpg":
            classType ='<img src="'+_icon_JPG+'" />';
            break;
        case "PNG":
        case "png":
            classType ='<img src="'+_icon_PNG+'" />';
            break;
        default:
            classType ='<img src="'+_icon_WENHAO+'" />';
            break;
    }
    return classType;
}

//通过ID获取开发工作任务
function findWorkTask(id){
    var workTask;
    $.ajax({
        type:"POST",
        url:"/devManage/devtask/getOneDevTask",
        dataType:"json",
        data:{
            "id":id
        },
        async:false,
        success:function(data1){
            workTask =data1["list"];
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
    return workTask;
}
/*-----------查看需求信息-------------*/



// 获取项目信息
function getWorkProjectByUser(){

    $.ajax({
        method:"post",
        url:"/devManage/worktask/findProjectByUser",
        success : function(data) {
            if(data != null && data.data != null && data.data != undefined){
                var rows = data.data;
                for (var i = 0; i < rows.length; i++) {
                    var id = rows[i].id;
                    var projectName = rows[i].projectName;
                    var opt = "<option value='" + id + "'>" + projectName + "</option>";
                    $("#sel_projectName").append(opt);
                }
                $('.selectpicker').selectpicker('refresh');
            }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });

}

//清空搜索内容
function clearSearch() {
    $('#dev_createDate').val("");
    $('#man_devUserId').val("");
    $('#taskCode').val("");
    $('#taskName').val("");
    $("#developer").val("");
    $("#taskState").selectpicker('val', '');
    $("#workSource").val("");
    $("#parallelTask").val("");
    $("#requirement_Code").val("");
    $("#requirement_Name").val("");
    $("#involveSystem").val("");
    $("#sel_projectName").val('');
    $("#sel_projectName").selectpicker('val', '');
    $("#workSource").selectpicker('val', '');
    $("#sDevPriority").selectpicker('val', '');
    $("#sprintNames").val('');
    $("#sprintIds").val('');
    
    search_checkbox_clear('involveSystem', $("#involveSystem_ids"));
    $(".selectpicker").selectpicker('refresh');
    $(".btn_clear").css("display","none");
    $(".collection2").children("span").removeClass("fa-heart").addClass("fa-heart-o");
}

//清空表格内容
function clearContent( that ){
    $(that).parent().children('input').val("");
    $(that).parent().children(".btn_clear").css("display","none");
}
//展开收起表格第一行
function  showSearchInput(that) {
    if( $(that).text()=='收起筛选' ){
        $(that).text("展开筛选");
        $("#cleanrChoose").hide();
        $(".ui-search-toolbar").hide();
    }else {
        $(that).text("收起筛选");
        $("#cleanrChoose").show();
        $(".ui-search-toolbar").show();
    }
}

var is_first_search = false;
//搜索
function searchInfo(){
	if(!is_first_search){
		$('#list_body2').remove();
		$('#list_body').show();
		pageInit();
		is_first_search = true;
		return;
	}
    $("#loading").css('display','block');
    var obj = {};
    
    obj.devTaskCode =$.trim($("#taskCode").val());
    obj.devTaskName = $.trim($("#taskName").val());
    obj.userNewName = $.trim($("#developer").val());
    var  devTaskStatus = $("#taskState").val();
    if(devTaskStatus!=null){
        obj.workTaskStatus= devTaskStatus.join(",");
    }
    
    obj.featureCode = $.trim($("#parallelTask").val());
    obj.requirementNewCode = $.trim($("#requirement_Code").val());
    obj.requirementNewName = $.trim($("#requirement_Name").val());
    obj.systemIds = $("#involveSystem_ids").val();
    
    obj.requirementFeatureSource = $.trim($("#workSource").find("option:selected").val());
    obj.devTaskPrioritys = $.trim($("#sDevPriority").val());
    let createStartDate = '';
    let createEndDate = '';
    var time = $("#dev_createDate").val();
    if(time != undefined){
        var timeArr = time.split(' - ');
        createStartDate = timeArr[0];
        createEndDate = timeArr[1];
    }else{
    	createStartDate = '';
        createEndDate = '';
    }
    obj.sprints = $("#sprintIds").val();

    //重新载入
    //ajaxGridOptions: {traditional: true},

    $("#devlist").jqGrid('setGridParam',{
        url:'/devManage/worktask/getAllWorktask',
        postData:{
            "startDate":createStartDate,
            "endDate":createEndDate,
            "workTask":  JSON.stringify(obj),
            "getProjectIds":$("#sel_projectName").val() != null?$("#sel_projectName").val().toString():""
        },
        loadComplete :function(){
        	$("#colGroup").find('input').each(function(idx,val){
          		if ($(val).is(':checked')) {
          			$(collection_obj.table_list).setGridParam().hideCol($(val).attr('value'));
          			$(collection_obj.table_list).setGridWidth($('.wode').width());
          		}
          	})
			$("#loading").css('display','none');		
		},	
        page:1
    }).trigger("reloadGrid");
}

//添加
function newPerson_btn() {
    $(".countWorkloadDiv").css( "display","none" );
    _files=[];
    $("#loading").css('display','block');
    $("#userPopupType").val("insertWorkTask");
    $("#newFileTable").empty();
    $("#addfiles").val("");
    $("#devTaskPriority").selectpicker('val', '');
    $("#taskUser").empty();
    $("#ataskName").val("");
    $("#featureCode").val("");
    $("#taskOverview").val("");
    $("#startWork").val("");
    $("#startWork").val(datFmt(new Date(),"yyyy-MM-dd"));
    $("#endWork").val("");
    $("#workLoad").val("");
    $("#new_taskUserId").val("");
    $("#new_taskUser").val("");
    $("#new_devTaskType").selectpicker('val', '');
    $("#wSystemId").val('');
    $("#canEditField").empty();
    /*getFeature();*/
    getUserName();
    $.ajax({
        url:"/devManage/worktask/getDevTaskFiled",
        type:"post",
        async:false,
        dataType:"json",
        success: function(data) {
            for( var i=0;i<data.fields.length;i++ ){
                if( data.fields[i].status == 1 ){
                    appendDataType( data.fields[i] , 'canEditField' ,'new');
                }
            }
            $('.selectpicker').selectpicker('refresh');
            modalType = 'new'
            $("#newPerson").modal("show");
        }
    });
    $("#loading").css('display','none');
}

//查看
function selectdetail(data){
    $("#check_devTaskType").text("");
    $("#check_devTaskType").text(data.devTaskType);

    $("#windowProduction").text("");
    $("#windowProduction").text( toStr(data.devTasks.windowName) );

    $("#systemVersion").text("");

    $("#systemVersion").text(toStr(data.devTasks.versionName)+"-->"+toStr(data.devTasks.systemScmBranch));

    $("#loading").css('display','block');
    $("#checkAttTable").empty();
    $("#SeeFileTable").empty();
    $("#taskRemark").empty();
    $("#checkfiles").val("");
    $("#handleLogs").empty();
    var map=data.dev;
    $("#SdevCode").html("");
    $("#SdevOverview").html("");
    $("#SStatus").html("");
    $("#devuserID").html("");

    $("#featureName").html("");

    $("#featureOverview").html("");
    $("#requirementFeatureStatus").html("");
    $("#manageUserId").html("");
    $("#executeUserId").html("");
    $("#systemId").html("");
    $("#requirementSource").html("");
    $("#requirementType").html("");
    $("#requirementPriority").html("");
    $("#requirementPanl").html("");
    $("#requirementStatus").html("");
    $("#applyUserId").html("");
    $("#requirmentOverview").text("");
    $("#applyDeptId").html("");
    $("#expectOnlineDate").html("");
    $("#planOnlineDate").html("");
    $("#lastUpdateDate3").html("");
    $("#createDate3").html("");
    $("#openDate").html("");
    $("#tyaskRemark").val("");
    $("#planStartDate").html("");
    $("#planEndDate").html("");
    $("#SplanWorkload").html("");
    $("#actualEndDate").html("");
    $("#actualStartDate").html("");
    $("#actualWorkload").html("");
    $("#sprintName").text("");
    //增加优先级
    $("#viewDevTaskPriority").text("");

    $("#SdevCode").html("["+map.devTaskCode+"]"+"  |  "+ map.devTaskName);
    $("#SdevOverview").html(map.devTaskOverview);
    var devTaskStatus=map.devStatus;
    $("#SStatus").html(devTaskStatus);

    $("#DevTaskID").val(map.id);
    $("#devuserID").html(map.devuserName);

    $("#createBy").html(map.createName);

    $("#createDate").html(datFmt(new Date(map.createDate),"yyyy年MM月dd"));
    if(map.planStartDate!=null){
        var planstartDate=datFmt(new Date(map.planStartDate),"yyyy年MM月dd");
        $("#planStartDate").html(planstartDate);
    }
    if(map.planEndDate!=null){
        var planEndDate=datFmt(new Date(map.planEndDate),"yyyy年MM月dd");
        $("#planEndDate").html(planEndDate);
    }

    if(map.actualStartDate!=null){
        var actualStartDate=datFmt(new Date(map.actualStartDate),"yyyy年MM月dd");
        $("#actualStartDate").html(actualStartDate);
    }
    if(map.actualEndDate!=null){
        var actualEndDate=datFmt(new Date(map.actualEndDate),"yyyy年MM月dd");
        $("#actualEndDate").html(actualEndDate);
    }
    $("#actualWorkload").html(map.actualWorkload);
    $("#featureName").html(map.featureCode+"&nbsp|&nbsp"+map.featureName);
    $("#SplanWorkload").html(map.planWorkload);
    // $("#estimateRemainWorkload").html(map.planWorkload);
    $("#remainWorkload").html(map.estimateRemainWorkload);
    $("#featureOverview").html(map.featureOverview);
    var featureStatus=map.requirementFeatureStatus;
    $("#requirementFeatureStatus").html(featureStatus);

    $("#requirementName").html("");
    $("#requirementName").html(map.requirementName);
    if(map.requirementOverview==null){
        $("#requirmentOverview").text("");
    }else{
        $("#requirmentOverview").text(map.requirementOverview);
    }

    $("#manageUserId").html(map.manageUserName);
    $("#executeUserId").html(map.executeUserName);
    $("#systemId").html(map.systemName);

    $("#requirementSource").html(map.requirementSource);
    $("#requirementType").html(map.requirementType);
    $("#requirementPriority").html(map.requirementPriority);
    $("#requirementPanl").html(map.requirementPanl);
    $("#requirementStatus").html(map.requirementStatus);
    $("#applyUserId").html(map.applyUserName);
    $("#applyDeptId").html(map.applyDeptName);
    $("#sprintName").html(map.sprintName);


    $("#viewDevTaskPriority").html(map.devTaskPriority);
    // if(map.devTaskPriority != null){
    //     for (var i = 0,len = sDevPriority.length;i < len;i++) {
    //         if(map.devTaskPriority == sDevPriority[i].value){
    //             return sDevPriority[i].innerHTML;
    //         }
    //     }
    // }else{
    //     $("#viewDevTaskPriority").html(map.devTaskPriority);
    // }

    showField( data.fields )

    if(map.expectOnlineDate!=null){
        var expectOnlineDate=datFmt(new Date(map.expectOnlineDate),"yyyy年MM月dd");
        $("#expectOnlineDate").text(expectOnlineDate);
    }
    if(map.planOnlineDate!=null){
        var planOnlineDate=datFmt(new Date(map.planOnlineDate),"yyyy年MM月dd");
        $("#planOnlineDate").html(planOnlineDate);
    }
    if(map.openDate!=null){
        var openDate=datFmt(new Date(map.openDate),"yyyy年MM月dd");
        $("#openDate").html(openDate);
    }
    if(map.createDate3!=null){
        var createDate3=datFmt(new Date(map.createDate3),"yyyy年MM月dd");
        $("#createDate3").html(createDate3);
    }

    if(map.lastUpdateDate3!=null){
        var lastUpdateDate3=datFmt(new Date(map.lastUpdateDate3),"yyyy年MM月dd");
        $("#lastUpdateDate3").html(lastUpdateDate3);
    }
    $("#SeeFiles").val("");
    if(data.attachements!=undefined){
        var _table=$("#SeeFileTable");
        var attMap=data.attachements;
        //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
        for(var i=0 ;i<attMap.length;i++){
            var _tr = '';
            var file_name = attMap[i].fileNameOld;
            var file_type = attMap[i].fileType;
            var file_id =  attMap[i].id;
            var _td_icon;
            //<i class="file-url">'+data.attachements[i].filePath+'</i>
            var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
            switch(file_type){
                case "doc":
                case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                case "xls":
                case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                default:_td_icon = '<img src="'+_icon_word+'" />';break;
            }
            var row =  JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
            _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+" "+_td_name+'</tr>';
            _table.append(_tr);
            _SeeFiles.push(attMap[i]);
            $("#SeeFiles").val(JSON.stringify(_SeeFiles));
        }
    }

    //备注
    if(data.rmark!=undefined){
        var str ='';
        for(var i=0;i<data.rmark.length;i++){
            var style= '';
            if(i==data.rmark.length-1){
                style= ' lastLog';
            }
            str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span>'+
                '<span>'+data.rmark[i].userName+'  | '+data.rmark[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.rmark[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
                '<div class="logDiv_cont"><div class="logDiv_contBorder"><div class="logDiv_contRemark"><span>'+data.rmark[i].devTaskRemark+'</span>'+
                '<div class="file-upload-list">';
            if(data.Attchement.length>0){
                str +='<table class="file-upload-tb">';
                var _trAll = '';
                for(var j=0;j<data.Attchement.length;j++){
                    var _tr = '';
                    if((data.Attchement[j].devTaskRemarkId)==(data.rmark[i].id)){

                        var file_type = data.Attchement[j].fileType;
                        var file_name = data.Attchement[j].fileNameOld;
                        var _td_icon;
                        var _td_name = '<span>'+file_name+'</span>';
                        switch(file_type){
                            case "doc":
                            case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                            case "xls":
                            case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                            case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                            case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                            default:_td_icon = '<img src="'+_icon_word+'" />';break;
                        }
                        var row =  JSON.stringify( data.Attchement[j]).replace(/"/g, '&quot;');
                        _tr+='<tr><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+_td_name+"</tr>";

                    }
                    if(_tr!=undefined){
                        _trAll +=_tr;
                    }

                }
                str+= _trAll+'</table>';

            }

            str += '</div></div></div></div></div>';

        }
        $("#taskRemark").append(str);

    }
    //处理日志
    if(data.logs!=undefined){
        var _span="";
        var str ='';
        for(var i=0;i<data.logs.length;i++){
            var style= '';
            if(i==data.logs.length-1){
                style= ' lastLog';
            }

            var style2='';
            var addDiv = '';
            var logDetail = '';
            if(data.logs[i].logDetail==null || data.logs[i].logDetail==''){
                if(data.logAttachement.length>0){
                    for(var j=0;j<data.logAttachement.length;j++){
                        if(data.logs[i].id!=data.logAttachement[j].devTaskLogId){
                            style2= 'style="display:none"';
                            addDiv = '</br>';
                        }
                    }
                }else{
                    style2= 'style="display:none"';
                    addDiv = '</br>';
                }



            }else{
                logDetail = data.logs[i].logDetail;
                logDetail=logDetail.replace(/;/g,"<br/>");
                logDetail = logDetail.replace(/null/g,' ');
            }



            str+='<div class="logDiv'+style+'"><div class="logDiv_title"><span class="orderNum"></span><span>'+data.logs[i].logType+'</span>&nbsp;&nbsp;&nbsp;'+
                '<span>'+data.logs[i].userName+'  | '+data.logs[i].userAccount+'</span>&nbsp;&nbsp;&nbsp;<span>'+datFmt(new Date(data.logs[i].createDate),"yyyy-MM-dd hh:mm:ss")+'</span></div>'+
                '<div class="logDiv_cont" ><div class="logDiv_contBorder"><div class="logDiv_contRemark" '+style2+'>'

            var ifjson =isJSON(logDetail);

            if(ifjson){
                _span="";
                if(logDetail=="[]"||logDetail==""||logDetail==undefined){

                    _span='<span>未经任何操作</span>'
                }else{
                    var Detail=JSON.parse(logDetail);
                    for (var s=0;s<Detail.length;s++) {
                        //alert(Detail[s].newValue);
                        var value=Detail[s].oldValue;
                        if(value==""||value==undefined||value==null){
                            value=""
                        }
                        if(Detail[s].remark!=""&&Detail[s].remark!=undefined){
                            str+='<span>备注信息：'+Detail[s].remark+'</span></br>'
                        }
                        if(undefined!=Detail[s].newValue){//""!=Detail[s].newValue&&
                            if(value==null||value==""){
                                value="&nbsp;&nbsp";
                            }
                            newValue=Detail[s].newValue;
                            if(newValue==null||newValue==""){
                                newValue="&nbsp;&nbsp";
                            }
                            str+='<span>'+Detail[s].fieldName+"："+"<span style='font-weight:bold'>"+'"'+value+'"'+"</span>"+modified+"<span style='font-weight:bold'>"+'"'+newValue+'"'+'</span></br>'
                        }
                    }
                }
            }else{
                if(logDetail!=""&&logDetail!=undefined){
                    str+='<span>'+logDetail+'</span>'
                }

            }

            str+='<div class="file-upload-list">';
            if(data.logAttachement.length>0){
                str +='<table class="file-upload-tb">';
                var _trAll = '';
                for(var j=0;j<data.logAttachement.length;j++){
                    if((data.logAttachement[j].devTaskLogId)==(data.logs[i].id)){
                        _span="";
                        var attType = '';
                        if(data.logAttachement[j].status == 1){//新增的日志
                            attType = "<lable>新增附件：</lable>";
                        }else if(data.logAttachement[j].status == 2){//删除的日志
                            attType = "<lable>删除附件：</lable>";
                        }
                        var _tr = '';
                        var file_name = data.logAttachement[j].fileNameOld;
                        var file_type = data.logAttachement[j].fileType;
                        var _td_icon;
                        var _td_name = '<span>'+file_name+'</span>';
                        //var _td_name = '<span>'+file_name+'</span><i class="file-url">'+data.logAttachement[j].filePath+'</i><i class = "file-bucket">'+data.logAttachement[j].fileS3Bucket+'</i><i class = "file-s3Key">'+data.logAttachement[j].fileS3Key+'</i></td>';

                        switch(file_type){
                            case "doc":
                            case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                            case "xls":
                            case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                            case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                            case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                            default:_td_icon = '<img src="'+_icon_word+'" />';break;
                        }
                        var row =  JSON.stringify( data.logAttachement[j]).replace(/"/g, '&quot;');
                        _tr+='<tr><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+attType+_td_icon+_td_name+"</tr>";

                        _trAll +=_tr;
                    }

                }

            }
            if(_trAll==undefined){_trAll=""}
            _trAll+=_span;

            str+= _trAll+'</table>';
            _trAll="";
            str += '</div></div>'+addDiv+'</div></div></div>';

        }
        $("#handleLogs").append(str);
    }
    modalType = 'check';
    $("#loading").css('display','none');
    $("#selectdetail").modal("show");
}

//分派
function showAssig(data) {
    $("#assignUserID").val("");
    $("#assignUser").attr("assignUserID","");
    $("#assignUser").val("");
    $("#userPopupType").val("Assign");
    $("#AssignRemark").val("");
    $("#assigDevID").val("");
    $("#devName").html("");
    $("#devOverview").html("");
    var map=data.dev;
    var assigStatus = '';
    for(var key in map){
        $("#assigDevID").val(map[key].id);
        $("#devName").html(map[key].devTaskName);
        $("#devOverview").html(map[key].devTaskOverview);
        $("#planStart").html("");
        if(map[key].planStartDate!=null){
            $("#planStart").html(datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd"));
        }
        $("#planEnd").html("");
        if(map[key].planEndDate!=null){
            $("#planEnd").html(datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd"));
        }
        $("#planWorkload").html("");
        $("#planWorkload").html(map[key].planWorkload);

        $("#assignActualStart").html("");
        if(map[key].actualStartDate!=null){
            $("#assignActualStart").html(datFmt(new Date(map[key].actualStartDate),"yyyy年MM月dd"));
        }
        $("#assignActualEnd").html("");
        if(map[key].actualEndDate!=null){
            $("#assignActualEnd").html(datFmt(new Date(map[key].actualEndDate),"yyyy年MM月dd"));
        }
        $("#assignActualWorkload").html("");
        $("#assignActualWorkload").html(map[key].actualWorkload);

        $("#devUserName").html(map[key].userName);
        var status=map[key].devTaskStatus;
        $("#remove").remove();

        var classDoing = 'doing';
        for (var i = 0,len = taskStateList.length;i < len;i++) {
            if(status == taskStateList[i].value){
                assigStatus = '<span id="remove" class="'+classDoing+status+'">'+taskStateList[i].innerHTML+'</span>'
                break;
            }
        };
        $("#status").append(assigStatus);

        //getAssignUser(map[key].devUserId);

    }
    $('.selectpicker').selectpicker('refresh');
    $("#Assignment").modal("show");
}
//转派
function showTransfer(data) {
    $("#userPopupType").val("Transfer");
    $("#loading").css('display','block');
    $("#TransferUser").val("");
    var map=data.dev;
    var Tstatus = '';
    for(var key in map){
        $("#Transfer_defectID").val("");
        $("#DevtRemark").val("");
        $("#TransferDevID").val("");
        $("#TransferDevID").val(map[key].id);
        $("#TransferdevName").html("");
        $("#TransferdevName").html(map[key].devTaskName);
        $("#TransferOverview").html("");
        $("#TransferOverview").html(map[key].devTaskOverview);
        $("#TransplanStart").html("");

        if(map[key].planStartDate!=null){
            $("#TransplanStart").html(datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd"));
        }
        $("#TransplanEnd").html("");
        if(map[key].planEndDate!=null){
            $("#TransplanEnd").html(datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd"));
        }
        $("#TranplanWorkload").html("");
        $("#TranplanWorkload").html(map[key].planWorkload);

        $("#TransActualStart").html("");
        if(map[key].actualStartDate!=null){
            $("#TransActualStart").html(datFmt(new Date(map[key].actualStartDate),"yyyy年MM月dd"));
        }
        $("#TransActualEnd").html("");
        if(map[key].actualEndDate!=null){
            $("#TransActualEnd").html(datFmt(new Date(map[key].actualEndDate),"yyyy年MM月dd"));
        }
        $("#TransActualWorkload").html("");
        $("#TransActualWorkload").html(map[key].actualWorkload);

        $("#TrandevUserName").html("");
        $("#TrandevUserName").html(map[key].userName);
        $("#oldUserID").val("");
        $("#oldUserID").val(map[key].devUserId);
        $("#Transfer_defectID").val(map[key].defectID);
        var status=map[key].devTaskStatus;
        getTransferUser(map[key].devUserId);
        $("#Tremove").remove();

        var classDoing = 'doing';
        for (var i = 0,len = taskStateList.length;i < len;i++) {
            if(status == taskStateList[i].value){
                Tstatus = '<span id="Tremove" class="'+classDoing+status+'">'+taskStateList[i].innerHTML+'</span>'
                break;
            }
        };
        $("#Tstatus").append(Tstatus);

    }
    $('.selectpicker').selectpicker('refresh');
    $("#loading").css('display','none');
    $("#Transfer").modal("show");
}
//显示转派
function Transfer(id) {
    $.ajax({
        method:"post",
        url:"/devManage/worktask/getEditDevTask",
        data:{"id":id},
        success : function(data) {
            showTransfer(data);
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }

    });
}
//显示分派
function assig(id) {
    $.ajax({
        method:"post",
        url:"/devManage/worktask/getEditDevTask",
        data:{"id":id},
        success : function(data) {
            showAssig(data);
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
}


//显示编辑信息
function showEditData(data){
    Neweditfiles=[];
    deleteAttaches = [];
    $("#defectID").val("");
    $("#userPopupType").val("updatetWorkTask");
    $("#loading").css('display','block');
    $("#sprintDiv").hide();
    $("#editfiles").val("");
    $("#newFiles").val("");
    $("#edstartWork").val("");
    $("#edendWork").val("");
    $("#editFileTable").empty();
    $("#edtaskUser").val("");
    $("#workTaskStatus").val("");
    $("#editSystemId").val("");
    $("#edDevTaskPriority").selectpicker('val', '');
    $("#edit_devTaskType").selectpicker('val', '');
    $("#opt_solution_submitUserId").val("");
    $("#opt_solution_oldAssignUserId").val("");
    $("#editSprintId").val('');
    $("#editSprintName").val('');
    if(data.defect!=null&&data.defect!=undefined){
        $("#opt_solution_submitUserId").val(data.defect.submitUserId);
        $("#opt_solution_oldAssignUserId").val(data.defect.assignUserId);
        $("#defectID").val(data.defect.id);
    }

    $("#editSprintId").empty();
    $("#editSprintId").append('<option value="">请选择</option>');
    var map=data.dev;
    for(var key in map){
        $("#edit_taskUserId").val(map[key].devUserId);
        $("#edtaskUser").val(map[key].userName);
        $("#edDevTaskPriority").val(map[key].devTaskPriority);
        $("#edit_devTaskType").val(map[key].devTaskType);
        $("#taskID").val(map[key].id);
        $("#edfeatureCode").val(map[key].featureName);
        $("#edAttribute").attr("edfeatureCode",map[key].requirementFeatureId);
        $("#edAttribute").attr("edcommissioningWindowId",map[key].commissioningWindowId);
        $("#edAttribute").attr("edrequirementFeatureStatus",map[key].requirementFeatureStatus);
        //getEdFeature(map[key].requirementFeatureId);
        $("#etaskName").val( isValueNull( map[key].devTaskName ) );
        $("#etaskOverview").val( isValueNull(map[key].devTaskOverview) );
        $("#editSystemId").val(map[key].systemId);
        $("#editSprintId").val(map[key].sprintId);
        $("#editSprintName").val(map[key].sprintName);
        if(map[key].developmentMode == 1){
            /*$.ajax({
                 url:"/devManage/devtask/getSprintBySystemId",
                 data:{
                     systemId:$("#editSystemId").val()
                 },
                 type:"post",
                 dataType:"json",
                 success:function(data2){
                     $("#editSprintId").empty();
                     $("#editSprintId").append("<option value=''>请选择</option>");
                     if(data2.sprintInfos.length>0){
                         $("#sprintDiv").show();
                         for(var i = 0;i<data2.sprintInfos.length;i++){
                             var flag = '';
                             if(data2.sprintInfos[i].id == map[key].sprintId){
                                 flag = 'selected'
                             }
                             $("#editSprintId").append('<option '+flag+' value="'+data2.sprintInfos[i].id+'">'+data2.sprintInfos[i].sprintName+'</option>')
                         }
                     }
                    $('.selectpicker').selectpicker('refresh');
                 },
                 error:function(XMLHttpRequest, textStatus, errorThrown){
                     layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
                 }
             });*/
            $("#sprintDiv").show();
            $("#workTaskStatus").attr("disabled",false);
        }else{
            $("#workTaskStatus").attr("disabled",false);
        }

        if(map[key].planStartDate!=null){
            var start=datFmt(new Date(map[key].planStartDate),"yyyy-MM-dd");
            $("#edstartWork").val(start);
        }
        if(map[key].planEndDate!=null){
            var end=datFmt(new Date(map[key].planEndDate),"yyyy-MM-dd");
            $("#edendWork").val(end);
        }
        $("#edworkLoad").val(map[key].planWorkload).change();
        $("#estimateRemainWorkload").val(map[key].estimateRemainWorkload).change();
        $("#workTaskStatus").val(map[key].devTaskStatus);
    }
    var field = data.fields;
    $("#editFieldDiv").empty();
    if( field != undefined ){
        for( var i = 0;i < field.length;i++ ){
            if( field[i].status == 1 ){
                appendDataType( field[i] , 'editFieldDiv' , 'edit' );
            }
        }
    }
    if(data.attachements!=undefined){
        var _table=$("#editFileTable");
        var attMap=data.attachements;
        //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
        for(var i=0 ;i<attMap.length;i++){
            var _tr = '';
            var file_name = attMap[i].fileNameOld;
            var file_type = attMap[i].fileType;
            var file_id =  attMap[i].id;
            var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
            var _td_icon;
            //<i class="file-url">'+data.attachements[i].filePath+'</i>
            var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,'+attache+')">删除</a></td>';
            switch(file_type){
                case "doc":
                case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                case "xls":
                case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                default:_td_icon = '<img src="'+_icon_word+'" />';break;
            }
            var row =  JSON.stringify( attMap[i]).replace(/"/g, '&quot;');
            _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+" "+_td_name+_td_opt+'</tr>';
            _table.append(_tr);
            _editfiles.push(data.attachements[i]);
            $("#editfiles").val(JSON.stringify(_editfiles));
        }
    }
    $('.selectpicker').selectpicker('refresh');
    $("#loading").css('display','none');
    modalType = 'edit';
    $("#showeditDev").modal("show");
}

//显示编辑Model
function edit(id) {
    $.ajax({
        method:"post",
        url:"/devManage/worktask/getEditDevTask",
        data:{"id":id},
        success : function(data) {
            showEditData(data);
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }

    });
}
//显示处理
function ShowHandle(data) {
    _Newhandlefiles=[];
    _newNoReviewFailed=[];
    _newDhandlefiles=[];
    NewCodeFiles=[];
    deleteAttaches = [];
    var DHstatus = '';

    $("#loading").css('display','block');

    var map=data.dev;
    var codeReviewStatus = data.systemCodeReviewStatus;
    // 是否代码评审（1:是，2:否）

    for(var key in map){
        if(codeReviewStatus==1){
            if (map[key].codeReviewStatus == 1){
                $("#codeReviewStatusDiv").css("display","inline-block");
                $("#postHandle_Btu").addClass("buttonColor");
                $("#postHandle_Btu").attr("onclick", "null");
                //$("#postHandle_Btu").removeAttr('onclick');
                $("#codeReviewStatus").text('未评审');

                //$("#postReviewHandle_Btu").css("display","none");
                /* $("#postCode_Btu").css("display","inline-block");
                 $("#postCodeReview_Btu").css("display","inline-block");*/
            } else if(map[key].codeReviewStatus == 3){
                $("#codeReviewStatusDiv").css("display","inline-block");
                $("#postHandle_Btu").addClass("buttonColor");
                $("#postHandle_Btu").attr("onclick", "null");
                $("#codeReviewStatus").text('评审未通过');
            }else if(map[key].codeReviewStatus == 2){
                $("#codeReviewStatusDiv").css("display","inline-block");
                $("#postHandle_Btu").attr("onclick", "PostHandle()");
                $("#postHandle_Btu").removeClass("buttonColor");
                //$("#codeReviewStatusDiv").css("display","none");
                $("#postCode_Btu").css("display","none");
                $("#postCodeReview_Btu").css("display","none");
                $("#postReviewHandle_Btu").css("display","inline-block");
                $("#postHandle_Btu").css("display","inline-block");
                $("#codeReviewStatus").text('评审通过');
            }else{
                $("#postHandle_Btu").attr("onclick", "PostHandle()");
                $("#postHandle_Btu").removeClass("buttonColor");
                $("#codeReviewStatusDiv").css("display","none");
                $("#postCode_Btu").css("display","none");
                $("#postCodeReview_Btu").css("display","none");
                $("#postReviewHandle_Btu").css("display","inline-block");
                $("#postHandle_Btu").css("display","inline-block");
            }
        }else{
            $("#postHandle_Btu").attr("onclick", "PostHandle()");
            $("#postHandle_Btu").removeClass("buttonColor");
            $("#codeReviewStatusDiv").css("display","none");
            $("#postCode_Btu").css("display","none");
            $("#postCodeReview_Btu").css("display","none");
            $("#postReviewHandle_Btu").css("display","inline-block");
            $("#postHandle_Btu").css("display","inline-block");
        }


        //根据开发工作任务状态，显示不同的内容
        var status=map[key].devTaskStatus;
        var devTaskId = map[key].id;

        var classDoing = 'doing';
        for (var i = 0,len = taskStateList.length;i < len;i++) {
            if(status == taskStateList[i].value){
                DHstatus = '<span id="remove" class="'+classDoing+status+'">'+taskStateList[i].innerHTML+'</span>'
                break;
            }
        };
        //待开发
        if(status=="1"){
            $("#DHitFileTable").empty();
            $("#NewDHattachFiles").val("");
            $("#DHattachFiles").val("");

            $("#DHdevID").val("");
            $("#DHdevID").val(devTaskId);
            $("#DHtaskName").html("");
            $("#DHtaskName").html(map[key].devTaskName);
            $("#DHdevOverview").html("");
            $("#DHdevOverview").html(map[key].devTaskOverview);
            $("#DHplanStart").html("");
            if(map[key].planStartDate!=null){
                $("#DHplanStart").html(datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd"));
            }
            $("#DHplanEnd").html("");
            if(map[key].planEndDate!=null){
                $("#DHplanEnd").html(datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd"));
            }
            $("#DHplanWorkload").html("");
            $("#DHplanWorkload").html(map[key].planWorkload);
            $("#DHdevUserName").html("");
            $("#DHdevUserName").html(map[key].userName);
            $("#remove").remove();
            $("#DHstatus").append(DHstatus);
            $("#DactualStart").val(datFmt(new Date(),"yyyy-MM-dd"));

            modalType="dHandle";
            if(data.attachements!=undefined){
                var _table=$("#DHitFileTable");
                var attMap=data.attachements;
                //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                for(var i=0 ;i<attMap.length;i++){
                    var _tr = '';
                    var file_name = attMap[i].fileNameOld;
                    var file_type = attMap[i].fileType;
                    var file_id =  attMap[i].id;
                    var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    var _td_icon;
                    //<i class="file-url">'+data.attachements[i].filePath+'</i>
                    var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
                    var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,'+attache+')">删除</a></td>';
                    switch(file_type){
                        case "doc":
                        case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                        case "xls":
                        case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                        case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                        case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                        default:_td_icon = '<img src="'+_icon_word+'" />';break;
                    }
                    var row =  JSON.stringify( data.attachements[i]).replace(/"/g, '&quot;');
                    _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+" "+_td_name+_td_opt+'</tr>';
                    _table.append(_tr);
                    _Dhandlefiles.push(data.attachements[i]);
                    $("#DHattachFiles").val(JSON.stringify(_Dhandlefiles));
                }
            }
            $("#dHandle").modal("show");

        }else if(status=="2"){//开发中
            $("#newHattachFiles").val("");
            $("#HattachFiles").val("");
            $("#HitFileTable").empty();
            $("#taskQuantity").val("");
            $("#HdevID").val("");
            $("#HdevID").val(map[key].id);
            $("#HtaskName").html(map[key].devTaskName);
            $("#HdevOverview").html(map[key].devTaskOverview);
            var startDate=datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd");
            var startEnd=datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd");
            $("#HplanStart").html(startDate);
            $("#HplanEnd").html(startEnd);
            $("#HplanWorkload").html(map[key].planWorkload);
            $("#HdevUserName").html(map[key].userName);
            if(map[key].actualStartDate!=null){
                $("#actualStart").val(datFmt(new Date(map[key].actualStartDate),"yyyy-MM-dd"));
            }
            $("#actualEnd").val(datFmt(new Date(),"yyyy-MM-dd"));
            $("#remove").remove();
            $("#Hstatus").append(DHstatus);

            modalType="Handle";
            if(data.attachements!=undefined){
                var _table=$("#HitFileTable");
                var attMap=data.attachements;
                //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                for(var i=0 ;i<attMap.length;i++){
                    var _tr = '';
                    var file_name = attMap[i].fileNameOld;
                    var file_type = attMap[i].fileType;
                    var file_id =  attMap[i].id;
                    var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    var _td_icon;
                    //<i class="file-url">'+data.attachements[i].filePath+'</i>
                    var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
                    var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,'+attache+')">删除</a></td>';
                    switch(file_type){
                        case "doc":
                        case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                        case "xls":
                        case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                        case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                        case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                        default:_td_icon = '<img src="'+_icon_word+'" />';break;
                    }
                    var row =  JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+" "+_td_name+_td_opt+'</tr>';
                    _table.append(_tr);
                    _handlefiles.push(attMap[i]);
                    $("#HattachFiles").val(JSON.stringify(_handlefiles));
                }
            }
            $("#Handle").modal("show");
        }else if(status=="3"){//开发完成
            var map=data.dev;
            $("#OverFileTable").empty();
            //$("#OverSeeFiles").val();

            $("#CodeDevNameOK").html(map[key].devTaskName);
            $("#CodeDevOverviewOK").html("");
            $("#CodeDevOverviewOK").html(map[key].devTaskOverview);

            if(map[key].planStartDate!=null){
                $("#CodeDevplanStartOK").html(datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd"));
            }
            $("#CodeDevplanEndOK").html("");
            if(map[key].planEndDate!=null){
                $("#CodeDevplanEndOK").html(datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd"));
            }
            $("#CodeplanWorkloadOK").html(map[key].planWorkload);
            $("#CodedevUserNameOK").html(map[key].userName);

            $("#CodeActualStartDateOK").html("");
            if(map[key].actualStartDate!=null){
                $("#CodeActualStartDateOK").html(datFmt(new Date(map[key].actualStartDate),"yyyy年MM月dd"));
            }
            $("#CodeActualEndDateOK").html("");
            if(map[key].actualEndDate!=null){
                $("#CodeActualEndDateOK").html(datFmt(new Date(map[key].actualEndDate),"yyyy年MM月dd"));
            }
            $("#CodetestTageOK").html("")
            if(map[key].testStage==1){
                $("#CodetestTageOK").html("系测");
            }else if(map[key].testStage==2){
                $("#CodetestTageOK").html("版测");
            }
            $("#CodeActualWorkloadOK").html("");
            $("#CodeActualWorkloadOK").html(map[key].actualWorkload);
            $("#remove").remove();
            $("#CodestatusOK").append(DHstatus);

            if(data.attachements!=undefined){
                var _table=$("#OverFileTable");
                var attMap=data.attachements;
                //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                for(var i=0 ;i<attMap.length;i++){
                    var _tr = '';
                    var file_name = attMap[i].fileNameOld;
                    var file_type = attMap[i].fileType;
                    var file_id =  attMap[i].id;
                    var _td_icon;
                    //<i class="file-url">'+data.attachements[i].filePath+'</i>
                    var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
                    //var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this,'+attMap[i].id+')">删除</a></td>';
                    switch(file_type){
                        case "doc":
                        case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                        case "xls":
                        case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                        case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                        case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                        default:_td_icon = '<img src="'+_icon_word+'" />';break;
                    }
                    var row =  JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                    _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+" "+_td_name+'</tr>';
                    _table.append(_tr);
                    /*_handlefiles.push(attMap[i]);
                    $("#OverSeeFiles").val(JSON.stringify(_handlefiles));*/
                }
            }

            $("#CodereviewOK").modal("show");
        }/*else if(status=="4"){
			var map=data.dev;
				$("#CodeFileTable").empty();
				$("#OldCodeFiles").val();
				$("#NewCodeFiles").val();
				$("#CodeDevID").val(map[key].id);
				$("#CodeDevName").html(map[key].devTaskName);
				$("#CodeDevOverview").html(map[key].devTaskOverview);
				$("#CodeDevplanStart").html("");
				if(map[key].planStartDate!=null){
					$("#CodeDevplanStart").html(datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd"));
				}
				$("#CodeDevplanEnd").html("");
				if(map[key].planEndDate!=null){
					$("#CodeDevplanEnd").html(datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd"));
				}


				$("#CodeplanWorkload").html(map[key].planWorkload);
				$("#CodedevUserName").html(map[key].userName);

				$("#CodeActualStartDate").html("");
				if(map[key].actualStartDate!=null){
					$("#CodeActualStartDate").html(datFmt(new Date(map[key].actualStartDate),"yyyy年MM月dd"));
				}
				$("#CodeActualEndDate").html("");
				if(map[key].actualEndDate!=null){
					$("#CodeActualEndDate").html(datFmt(new Date(map[key].actualEndDate),"yyyy年MM月dd"));
				}
				$("#CodeActualWorkload").html("");
				$("#CodeActualWorkload").html(map[key].actualWorkload);
				$("#remove").remove();
				$("#Codestatus").append(DHstatus);
				$("#loading").css('display','none');
				modalType="review";
				if(data.attachements!=undefined){
					var _table=$("#CodeFileTable");
					var attMap=data.attachements;
					//var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
					for(var i=0 ;i<attMap.length;i++){
						var _tr = '';
						var file_name = attMap[i].fileNameOld;
						var file_type = attMap[i].fileType;
						var file_id =  attMap[i].id;
                        var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
						var _td_icon;
						//<i class="file-url">'+data.attachements[i].filePath+'</i>
						var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
						var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,'+attache+')">删除</a></td>';
						switch(file_type){
							case "doc":
							case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
							case "xls":
							case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
							case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
							case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
							default:_td_icon = '<img src="'+_icon_word+'" />';break;
						}
						var row =  JSON.stringify( data.attachements[i]).replace(/"/g, '&quot;');
						_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+" "+_td_name+_td_opt+'</tr>';
						_table.append(_tr);
						OldCodeFiles.push(data.attachements[i]);
						$("#OldCodeFiles").val(JSON.stringify(OldCodeFiles ));
					}
				}
			 $("#Codereview").modal("show");

		}else if(status=="5"){
			$("#reviewID").val("");
			$("#newreviewFiles").val("");

			var map=data.dev;
			$("#reviewID").val(map[key].id);
			$("#reviewFileTable").empty();
			$("#reviewTaskName").html("");
			$("#reviewTaskName").html(map[key].devTaskName);
			$("#reviewTaskOverview").html("");
			$("#reviewTaskOverview").html(map[key].devTaskOverview);
			$("#reviewTaskStart").html("");
			if(map[key].planStartDate!=null){
				$("#reviewTaskStart").html(datFmt(new Date(map[key].planStartDate),"yyyy年MM月dd"));
			}
			$("#reviewTaskEnd").html("");
			if(map[key].planEndDate!=null){
				$("#reviewTaskEnd").html(datFmt(new Date(map[key].planEndDate),"yyyy年MM月dd"));
			}
			$("#reviewTaskLoad").html(map[key].planWorkload);
			$("#reviewUserName").html(map[key].userName);

			$("#reviewActualStart").val("");
			if(map[key].actualStartDate!=null){
				$("#reviewActualStart").val(datFmt(new Date(map[key].actualStartDate),"yyyy-MM-dd"));
			}
			$("#reviewActualEnd").val("");
			if(map[key].actualEndDate!=null){
				$("#reviewActualEnd").val(datFmt(new Date(map[key].actualEndDate),"yyyy-MM-dd"));
			}

			$("#reviewTaskQuantity").val("");
			$("#reviewTaskQuantity").val(map[key].actualWorkload);
			$("#remove").remove();
			$("#reviewStatus").append(DHstatus);
			$("#loading").css('display','none');
			modalType="NOreview";
			if(data.attachements!=undefined){
				var _table=$("#reviewFileTable");
				var attMap=data.attachements;
				//var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
				for(var i=0 ;i<attMap.length;i++){
					var _tr = '';
					var file_name = attMap[i].fileNameOld;
					var file_type = attMap[i].fileType;
					var file_id =  attMap[i].id;
                    var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
					var _td_icon;
					//<i class="file-url">'+data.attachements[i].filePath+'</i>
					var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
					var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,'+attache+')">删除</a></td>';
					switch(file_type){
						case "doc":
						case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
						case "xls":
						case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
						case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
						case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
						default:_td_icon = '<img src="'+_icon_word+'" />';break;
					}
					var row =  JSON.stringify( data.attachements[i]).replace(/"/g, '&quot;');
					_tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+" "+_td_name+_td_opt+'</tr>';
					_table.append(_tr);
					_NoreviewFailed.push(data.attachements[i]);
					$("#reviewFiles").val(JSON.stringify(_NoreviewFailed));
				}
			}
		 $("#reviewFailed").modal("show");


		}*/
        $("#loading").css('display','none');
    }
}

/*---------------------处理------------------------*/
//评审通过
function adopt(){
    var id=$("#CodeDevID").val();
    $("#loading").css('display','block');
    $.ajax({
        method:"post",
        url:"/devManage/worktask/reviewAdopt",
        data:{"id":id,
            "adoptFiles":$("#NewCodeFiles").val(),
            "deleteAttaches":JSON.stringify(deleteAttaches)
        },
        success : function(data) {
            $("#loading").css('display','none');
            $("#Codereview").modal("hide");
            pageInit();
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
}
//评审未通过
function Nadopt(){
    var id=$("#CodeDevID").val();
    $("#loading").css('display','block');
    $.ajax({
        method:"post",
        url:"/devManage/worktask/reviewNAdopt",
        data:{"id":id,
            "adoptFiles":$("#NewCodeFiles").val(),
            "deleteAttaches":JSON.stringify(deleteAttaches)
        },
        success : function(data) {
            $("#loading").css('display','none');
            $("#Codereview").modal("hide");
            pageInit();
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }

    });
}
//提交待处理
function PostDHandle() {
    var obj = {};
    obj.id=$("#DHdevID").val();
    obj.actualStartDate=$("#DactualStart").val();
    var handle=JSON.stringify(obj);
    $("#loading").css('display','block');
    $.ajax({
        method:"post",
        url:"/devManage/worktask/DHandleDev",
        data:{
            "handle":handle,
            "DHattachFiles":$("#NewDHattachFiles").val(),
            "deleteAttaches":JSON.stringify(deleteAttaches)
        },
        success : function(data) {
            $("#loading").css('display','none');
            $("#dHandle").modal("hide");
            pageInit();
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
}
//提交代码评审
function PostCode() {
    var obj = {};
    obj.id=$("#HdevID").val();

    obj.actualStartDate=$("#actualStart").val();
    obj.actualEndDate=$("#actualEnd").val();
    obj.actualWorkload= $("#taskQuantity").val();
    var handle=JSON.stringify(obj);
    $('#HandleDev').data('bootstrapValidator').validate();
    if(!$('#HandleDev').data('bootstrapValidator').isValid()){
        return;
    }
    $("#loading").css('display','block');
    $.ajax({
        method:"post",
        url:"/devManage/worktask/CodeHandleDev",
        data:{
            "handle":handle,
            "HattachFiles":$("#newHattachFiles").val(),
            "deleteAttaches":JSON.stringify(deleteAttaches)
        },
        success : function(data) {
            $("#loading").css('display','none');
            $("#Handle").modal("hide");
            pageInit();

        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }

    });
}
//提交实施中到实施完成
function PostHandle() {
    var obj = {};
    obj.id=$("#HdevID").val();
    obj.actualStartDate=$("#actualStart").val();
    obj.actualEndDate=$("#actualEnd").val();
    obj.actualWorkload= $("#taskQuantity").val();
    var handle=JSON.stringify(obj);

    $('#HandleDev').data('bootstrapValidator').validate();
    if(!$('#HandleDev').data('bootstrapValidator').isValid()){
        return;
    }
    $("#loading").css('display','block');
    $.ajax({
        method:"post",
        url:"/devManage/worktask/HandleDev",
        data:{
            "handle":handle,
            "HattachFiles":$("#newHattachFiles").val(),
            "deleteAttaches":JSON.stringify(deleteAttaches)
        },
        success : function(data) {
            $("#loading").css('display','none');
            $("#Handle").modal("hide");
            pageInit();

        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }

    });
}
//提交实施中到代码评审
function PostCodeReview() {
    var obj = {};
    obj.id=$("#reviewID").val();

    obj.actualStartDate=$("#reviewActualStart").val();
    obj.actualEndDate=$("#reviewActualEnd").val();
    obj.actualWorkload= $("#reviewTaskQuantity").val();
    var handle=JSON.stringify(obj);
    $('#reviewDev').data('bootstrapValidator').validate();
    if(!$('#reviewDev').data('bootstrapValidator').isValid()){
        return;
    }
    $("#loading").css('display','block');
    $.ajax({
        method:"post",
        url:"/devManage/worktask/CodeHandleDev",
        data:{
            "handle":handle,
            "HattachFiles":$("#newreviewFiles").val(),
            "deleteAttaches":JSON.stringify(deleteAttaches)
        },
        success : function(data) {
            $("#loading").css('display','none');
            $("#reviewFailed").modal("hide");
            pageInit();

        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }

    });
}
//提交处理
function PostReviewHandle() {
    var obj = {};
    obj.id=$("#reviewID").val();
    obj.actualStartDate=$("#reviewActualStart").val();
    obj.actualEndDate=$("#reviewActualEnd").val();
    obj.actualWorkload= $("#reviewTaskQuantity").val();
    var handle=JSON.stringify(obj);

    $('#reviewDev').data('bootstrapValidator').validate();
    if(!$('#reviewDev').data('bootstrapValidator').isValid()){
        return;
    }
    $("#loading").css('display','block');
    $.ajax({
        method:"post",
        url:"/devManage/worktask/HandleDev",
        data:{
            "handle":handle,
            "HattachFiles":$("#newreviewFiles").val(),
            "deleteAttaches":JSON.stringify(deleteAttaches)},
        success : function(data) {
            $("#loading").css('display','none');
            $("#reviewFailed").modal("hide");
            pageInit();

        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }

    });
}
/*---------------------处理------------------------*/

//获取
function Handle(id) {
    $.ajax({
        method:"post",
        url:"/devManage/worktask/getEditDevTask",
        data:{"id":id},
        success : function(data) {

            //data["codeReviewStatus"] = codeReviewStatus;
            ShowHandle(data);
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }

    });
}

//提交分派
function PostAssig() {
    var Remark= $("#AssignRemark").val();

    var obj = {};
    obj.id=$("#assigDevID").val();
    $("#assigDevID").val("");
    obj.userName =$("#assignUser").val();
    obj.devUserId=$("#assignUserID").val();
    $('#assignForm').data('bootstrapValidator').validate();
    if(!$('#assignForm').data('bootstrapValidator').isValid()){
        return;
    }
    $("#loading").css('display','block');
    var assig=JSON.stringify(obj);
    $.ajax({
        method:"post",
        url:"/devManage/worktask/assigDev",
        data:{"handle":assig,
            "Remark":Remark},
        success : function(data) {
            $("#loading").css('display','none');

            if (data.status == 1){
                layer.alert('编辑成功！', {
                    icon: 1,
                    title: "提示信息"
                });

            }else{
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });

            }
            $("#Assignment").modal("hide");
            pageInit();
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }

    });
}
//提交转派
function PostTransfer() {
    var UsserId=$("#oldUserID").val();
    var newUserId=$("#TransferUserId").val();
    if(UsserId==newUserId){
        layer.alert("不能转派给相同人员",{
            icon:2,
            title:"提示信息"
        })
        return;
    }
    var Remark= $("#DevtRemark").val();
    $("#loading").css('display','block');
    var obj = {};
    obj.id=$("#TransferDevID").val();
    $("#TransferDevID").val("");
    obj.devUserId=newUserId;
    obj.defectID=$("#Transfer_defectID").val();
    var assig=JSON.stringify(obj);
    $.ajax({
        method:"post",
        url:"/devManage/worktask/assigDev",
        data:{"assig":assig,
            "Remark":Remark},
        success : function(data) {
            $("#loading").css('display','none');
            if (data.status == 1){
                layer.alert('转派成功！', {
                    icon: 1,
                    title: "提示信息"
                });

            }else{
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });

            }
            $("#Transfer").modal("hide");
            pageInit();
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
}

//清空下方表格内输入框的内容
function cleanrChoose(){
    $(".ui-search-input input").val("");
    $(".ui-search-input input").parent().children(".btn_clear").css("display","none");
    $("#devlist").jqGrid('clearGridData');  //清空表格
    $("#devlist").jqGrid('setGridParam',{  // 重新加载数据
        datatype:'json',
        url:ctxStatic +'/jqgrid/data/JSONData.json',
        page:1
    }).trigger("reloadGrid");
}

function exportPerson_btn(){
    var obj = {};
    obj.devTaskCode =$.trim($("#taskCode").val());
    obj.devTaskName = $.trim($("#taskName").val());
    // obj.userName = $.trim($("#man_devUserId").val());
    obj.userNewName = $.trim($("#developer").val());
    var  devTaskStatus = $("#taskState").val();
    if(devTaskStatus!=null){
        obj.workTaskStatus= devTaskStatus.join(",");
    }

    obj.featureCode = $.trim($("#parallelTask").val());
    // obj.requirementCode = $.trim($("#relationDemand").val());
    // obj.systemName = $.trim($("#involveSystem").val());
    obj.requirementNewCode = $.trim($("#requirement_Code").val());
    obj.requirementNewName = $.trim($("#requirement_Name").val());
    obj.systemIds = $("#involveSystem_ids").val();

    obj.requirementFeatureSource = $.trim($("#workSource").find("option:selected").val());
    obj.devTaskPriority = $.trim($("#sDevPriority").val());
    var createStartDate = '';
    var createEndDate = '';
    var time=$("#dev_createDate").val();
    if(time != undefined){
        var timeArr=time.split(' - ');
        createStartDate=timeArr[0];
        createEndDate=timeArr[1];
    }
    obj.sprints = $("#sprintIds").val();

    obj.sidx = "id";
    obj.sord = "desc";
    obj = JSON.stringify(obj);
    var data=getJQAllData();
    var excelData=JSON.stringify(data);

    var getProjectIds = $("#sel_projectName").val() != null?$("#sel_projectName").val().toString():"";
    window.location.href="/devManage/worktask/getExcelAllWork?excelDate="+obj+"&getProjectIds="+getProjectIds+"&startDate="+createStartDate+"&endDate="+createEndDate;
}
function getJQAllData() {
    //拿到grid对象
    var obj = $("#devlist");
    //获取grid表中所有的rowid值
    var rowIds = obj.getDataIDs();
    //初始化一个数组arrayData容器，用来存放rowData
    var arrayData = new Array();
    if (rowIds.length > 0) {
        for (var i = 0; i < rowIds.length; i++) {
            //rowData=obj.getRowData(rowid);//这里rowid=rowIds[i];
            arrayData.push(obj.getRowData(rowIds[i]));
        }
    }
    return arrayData;
}

//获取开发人员
function getUserName() {
    $.ajax({
        type: "post",
        url:'/devManage/worktask/getAllDevUser',
        dataType: "json",

        success: function(data) {
            for(var i = 0; i < data.length; i++) {
                var name = data[i].userName;
                var id = data[i].userId;
                if(i<1){
                    var opt = "<option value='' selected ='selected'>请选择</option>";
                    $("#taskUser").append(opt);

                }
                var opt = "<option value='" + id + "'>" + name + "</option>";
                $("#taskUser").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
}

//获取转派人员
function getTransferUser(ids) {
    $.ajax({
        type: "post",
        url:'/devManage/worktask/getAllDevUser',
        dataType: "json",
        success: function(data) {

            for(var i = 0; i < data.length; i++) {
                var name = data[i].userName;
                var id = data[i].userId;

                if(id==ids){
                    // $("#TransferUser").val(name);
                    $("#TransferUser").attr("userID",id);
                }
            }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
}
/*//获取分派人员
function getAssignUser(ids) {
    $.ajax({
        type: "post",
        url:'/devManage/worktask/getAllDevUser',
        dataType: "json",
        success: function(data) {

            for(var i = 0; i < data.length; i++) {
                var name = data[i].userName;
                var id = data[i].userId;
                if(i<1){
                    var opt = "<option value='' selected ='selected'>请选择</option>";
                      $("#assignUser").append(opt);

                  }
                	$("#OleDveName").html(name);
                	 var opt = "<option value='" + id + "'  >" + name + "</option>";
                	 var opt = "<option value='" + id + "' >" + name + "</option>";
                $("#assignUser").append(opt);
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}
*/
function  getSee(id,requirementFeatureId){
    $("#requirementFeatureId").val(requirementFeatureId);
    $.ajax({
        method:"post",
        url:"/devManage/worktask/getSeeDetail",
        data:{"id":id,
            requirementFeatureId:requirementFeatureId},
        success : function(data) {
            $( "#selectdetail .focusInfo" ).attr("id",id);
            if( data.attentionStatus == 2 ){
                //不关注
                $( "#selectdetail .focusInfo" ).addClass("noheart");
                $( "#selectdetail .focusInfo" ).attr("title","点击关注");
            }else if( data.attentionStatus == 1 ){
                //关注
                $( "#selectdetail .focusInfo" ).removeClass("noheart");
                $( "#selectdetail .focusInfo" ).attr("title","点击不再关注");
            }
            selectdetail(data)
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
}
//编辑提交
function editDevTask(){
    $('#editDev').data('bootstrapValidator').validate();
    if(!$('#editDev').data('bootstrapValidator').isValid()){
        return;
    }
    var fieldTemplate = getFieldData( "editFieldDiv" );
    for( var i=0;i< fieldTemplate.field.length;i++ ){
        if(  fieldTemplate.field[i].required == "false" ){
            if(  fieldTemplate.field[i].valueName == ""||  fieldTemplate.field[i].valueName == null|| fieldTemplate.field[i].valueName == undefined ){
                $("#loading").css('display','none');
                layer.alert( fieldTemplate.field[i].labelName+"不能为空", {
                    icon: 2,
                    title: "提示信息"
                });

                return;
            }
        }
    }

    var obj={};
    obj.fieldTemplate = fieldTemplate;
    obj.requirementFeatureId = $("#edAttribute").attr("edfeatureCode");
    obj.devTaskName=$("#etaskName").val();
    obj.devTaskOverview=$("#etaskOverview").val();
    obj.planStartDate=$("#edstartWork").val();
    obj.planEndDate=$("#edendWork").val();
    obj.planWorkload=$("#edworkLoad").val();
    obj.estimateRemainWorkload=$("#estimateRemainWorkload").val();
    console.log("obj:"+obj)
    obj.devTaskPriority=$("#edDevTaskPriority").val();//优先级
    obj.devTaskType=$("#edit_devTaskType").val();//优先级
    var devTaskStatus=$("#workTaskStatus").val();
    obj.devTaskStatus=devTaskStatus;
    obj.devUserId=$("#edit_taskUserId").val();
    obj.commissioningWindowId=$("#edAttribute").attr("edcommissioningWindowId");
    obj.requirementFeatureStatus=$("#edAttribute").attr("edrequirementFeatureStatus");
    obj.id=$("#taskID").val();
    obj.sprintId = $("#editSprintId").val();
    var defectID = $("#defectID").val();
    obj.defectID=defectID;
    obj.systemId=$("#editSystemId").val();
    obj=JSON.stringify(obj);


    /*if(defectID!=null&&defectID!=""&&devTaskStatus==3){
        $("#rejectDiv").modal("show");
    }else{*/
    $("#loading").css('display','block');
    $.ajax({
        type:"post",
        url:'/devManage/worktask/updateDevTask',
        //contentType: "application/json; charset=utf-8",
        data:{"obj":obj,
            "attachFiles":$("#newFiles").val(),
            "deleteAttaches":JSON.stringify(deleteAttaches)
        },
        success : function(data) {
            $("#showeditDev").modal("hide");
            $("#loading").css('display','none');
            if (data.status == 1){
                layer.alert('编辑成功！', {
                    icon: 1,
                    title: "提示信息"
                });

            }else{
                layer.alert(data.errorMessage, {
                    icon: 2,
                    title: "提示信息"
                });

            }
            $("#showeditDev").modal("hide");
            pageInit();

        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });
    //}
}
//添加
function addDevTask(){
    $('#addDev').data('bootstrapValidator').validate();
    if(!$('#addDev').data('bootstrapValidator').isValid()){
        return;
    }
    var files=$("#addfiles").val();
    var obj={};
    obj.requirementFeatureId = $("#Attribute").attr("featureCode");  //关联开发任务
    obj.devTaskPriority=$("#devTaskPriority").val();//优先级
    obj.devTaskType=$("#new_devTaskType").val();//
    obj.devTaskName=$("#ataskName").val();//任务名称
    obj.devTaskOverview=$("#taskOverview").val();//任务描述
    obj.planStartDate=$("#startWork").val();//预计开始时间
    obj.planEndDate=$("#endWork").val();//预计结束时间
    obj.planWorkload=$("#workLoad").val();//预计工作量
    obj.devUserId=$("#new_taskUserId").val();//任务分配
    obj.sprintId=$("#newSprintId").val();//冲刺
    obj.commissioningWindowId= $("#Attribute").attr("commissioningWindowId");
    obj.requirementFeatureStatus=$("#Attribute").attr("requirementFeatureStatus");
    var fieldTemplate = getFieldData( "canEditField" );
    for( var i=0;i< fieldTemplate.field.length;i++ ){
        if(  fieldTemplate.field[i].required == "false" ){
            if(  fieldTemplate.field[i].valueName == ""||  fieldTemplate.field[i].valueName == null|| fieldTemplate.field[i].valueName == undefined ){
                $("#loading").css('display','none');
                layer.alert( fieldTemplate.field[i].labelName+"不能为空", {
                    icon: 2,
                    title: "提示信息"
                });

                return;
            }
        }
    }
    obj.fieldTemplate = fieldTemplate;
    var objStr=JSON.stringify(obj);
    $("#loading").css('display','block');
    $.ajax({
        type:"post",
        url:'/devManage/worktask/addDevTask',
        //contentType: "application/json; charset=utf-8",
        data:{
            "objStr":objStr,
            "attachFiles":files
        },
        success : function(data) {
            $("#loading").css('display','none');
            $("#newPerson").modal("hide");
            $("#userPopupType").val("");
            pageInit();
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    });

}

//查看人员未完成工作量 当前任务所属系统下所有开发任务下所有工作任务  统计 姓名 工单数量 预估工作量  ztt
function checkNoCompleteWorkload(){
    var featureCode = $("#featureCode").val();
    if(featureCode == null || featureCode == ''){
        layer.alert("请先选择一个开发任务！",{icon:0});
        return;
    }
    var systemId = $("#wSystemId").val();
    if(systemId == null || systemId == ''){
        layer.alert("所选开发没有系统id，不可以查看人员未完成工作量！",{icon:0});
        return;
    }

    $.ajax({
        url:"/devManage/worktask/countWorkloadBysystemId",
        dataType:"json",
        type:"post",
        data:{
            "systemId":systemId
        },
        success:function(data){
            countWorkloadTable(data.countDatas);
            $(".countWorkloadDiv").css( "display","block" );
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
        }
    })
}
function countWorkloadTable(data){
    $("#countWorkloads").bootstrapTable('destroy');
    $("#countWorkloads").bootstrapTable({
        data : data,
        method : "post",
        cache: false,
        columns : [{
            field : "userName",
            title : "开发人员姓名",
            align : 'center'
        },{
            field : "workNum",
            title : "工单数量",
            align : 'center'
        },{
            field : "planWorkLoads",
            title : "预计总工作量",
            align : 'center',
        }]
    });
}


function down(This){
    if( $(This).hasClass("fa-angle-double-down") ){
        $(This).removeClass("fa-angle-double-down");
        $(This).addClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideDown(100);
        $(This).parents('.allInfo').children(".connect_div").slideDown(100);
    }else {
        $(This).addClass("fa-angle-double-down");
        $(This).removeClass("fa-angle-double-up");
        $(This).parents('.allInfo').children(".def_content").slideUp(100);
        $(This).parents('.allInfo').children(".connect_div").slideUp(100);
    }
}

//日期转换
function datFmt(date,fmt) { // author: meizz
    var o = {
        "M+": date.getMonth() + 1, // 月份
        "d+": date.getDate(), // 日
        "h+": date.getHours(), // 小时
        "m+": date.getMinutes(), // 分
        "s+": date.getSeconds(), // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3), // 季度
        "S": date.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
function uplike(){
    $("#ataskName").val($("#featureCode option:selected").text());
}

function formValidator(){
    $('#addDev').bootstrapValidator({
        excluded:[":disabled"],
        // message: 'This value is not valid',//通用的验证失败消息
        feedbackIcons: {
            //根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            ataskName: {
                validators: {
                    notEmpty: {
                        message: '任务名称不能为空'
                    },
                    stringLength: {
                        max: 100,
                        message: '长度必须小于100字符'
                    }
                }
            },
            featureCode: {
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '关联开发任务不能为空'
                    },
                }
            },
            taskOverview: {
                validators: {
                    notEmpty: {
                        message: '任务描述不能为空'
                    },
                    /* stringLength: {
                         min: 0,
                         max: 500,
                         message: '长度必须小于500'
                     }*/
                }
            },
            workLoad: {
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '预计工作量不能为空'
                    },
                    numeric: {
                        message: '只能输入数字'
                    },
                    regexp: {
                        regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
                        message: '请输入0-500的数字'
                    },
                }
            },
            taskUser: {
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '任务分配不能为空'
                    },
                }
            },
            devTaskType: {
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '任务类型不能为空'
                    },
                }
            },
            startWork:{
                trigger:"change",
                validators: {
                    callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '开始时间必须小于结束日期！',
                        callback:function(value, validator,$field){
                            if( value == "" ){
                                return true;
                            }else{
                                if( $( "#endWork" ).val() == '' ){
                                    return true;
                                }else{
                                    var start = new Date( value );
                                    var end = new Date( $( "#endWork" ).val() );
                                    if( start.getTime() > end.getTime() ){
                                        return false;
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            },
            endWork:{
                trigger:"change",
                validators: {
                    callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '结束时间必须大于开始日期！',
                        callback:function(value, validator,$field){
                            if( value == "" ){
                                return true;
                            }else{
                                if( $( "#startWork" ).val() == '' ){
                                    return true;
                                }else{
                                    var start = new Date( $( "#startWork" ).val() );
                                    var end = new Date( value );
                                    if( start.getTime() > end.getTime() ){
                                        return false;
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
    });
    $('#editDev').bootstrapValidator({
        excluded:[":disabled"],
        message: 'This value is not valid',//通用的验证失败消息
        feedbackIcons: {
            //根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            etaskName: {
                validators: {
                    notEmpty: {
                        message: '任务名称不能为空'
                    },
                    stringLength: {
                        max: 100,
                        message: '长度必须小于100字符'
                    }
                }
            },
            edfeatureCode: {
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '关联开发任务不能为空'
                    },
                }
            },
            workTaskStatus: {
                validators: {
                    notEmpty: {
                        message: '任务状态不能为空'
                    },
                }
            },
            devTaskType: {
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '任务类型不能为空'
                    },
                }
            },
            etaskOverview: {
                validators: {
                    notEmpty: {
                        message: '任务描述不能为空'
                    },
                    /* stringLength: {
                         min: 0,
                         max: 500,
                         message: '长度必须小于500'
                     }*/
                }
            },
            edworkLoad: {
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '预计工作量不能为空'
                    },
                    numeric: {
                        message: '只能输入数字'
                    },

                    regexp: {
                        regexp:/^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
                        message: '请输入0-500的数字'
                    },
                }

            },
            // estimateRemainWorkload: {
            //     trigger:"change",
            //     validators: {
            //         notEmpty: {
            //             message: '预估剩余工作量不能为空'
            //         },
            //         numeric: {
            //             message: '只能输入数字'
            //         },
            //         regexp: {
            //             regexp:/^500$|^[0](\.[\d]+)$|^([0-9]|[0-9]\d)(\.\d+)*$|^([0-9]|[0-9]\d|[1-4]\d\d)(\.\d+)*$/,
            //             message: '请输入0-500的数字'
            //         },
            //     }
            //
            // },

            edtaskUser: {
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '任务分配不能为空'
                    },
                }
            },
            edstartWork:{
                trigger:"change",
                validators: {
                    callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '开始时间必须小于结束日期！',
                        callback:function(value, validator,$field){
                            if( value == "" ){
                                return true;
                            }else{
                                if( $( "#edendWork" ).val() == '' ){
                                    return true;
                                }else{
                                    var start = new Date( value );
                                    var end = new Date( $( "#edendWork" ).val() );
                                    if( start.getTime() > end.getTime() ){
                                        return false;
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            },
            edendWork:{
                trigger:"change",
                validators: {
                    callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '结束时间必须大于开始日期！',
                        callback:function(value, validator,$field){
                            if( value == "" ){
                                return true;
                            }else{
                                if( $( "#edstartWork" ).val() == '' ){
                                    return true;
                                }else{
                                    var start = new Date( $( "#edstartWork" ).val() );
                                    var end = new Date( value );
                                    if( start.getTime() > end.getTime() ){
                                        return false;
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
    });
    $('#HandleDev').bootstrapValidator({
        message: 'This value is not valid',//通用的验证失败消息
        feedbackIcons: {
            //根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            taskQuantity: {
                validators: {
                    notEmpty: {
                        message: '任务工作量不能为空'
                    },
                    numeric: {
                        message: '只能输入数字'
                    },
                    regexp: {
                        regexp:/^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
                        message: '请输入0-500的数字'
                    },
                }
            }
        }
    });
    $('#reviewDev').bootstrapValidator({
        message: 'This value is not valid',//通用的验证失败消息
        feedbackIcons: {
            //根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            reviewTaskQuantity: {
                validators: {
                    notEmpty: {
                        message: '任务工作量不能为空'
                    },
                    numeric: {
                        message: '只能输入数字'
                    },
                    regexp: {
                        regexp: /^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
                        message: '请输入0-500的数字'
                    },
                }
            }
        }
    });
    $('#assignForm').bootstrapValidator({
        message: 'This value is not valid',//通用的验证失败消息
        feedbackIcons: {
            //根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            assignUser: {
                trigger:"change",
                validators: {
                    notEmpty: {
                        message: '任务分派不能为空'
                    },
                }
            },

        }
    });


}


function showThisDiv(This,num){
    if( $(This).hasClass("def_changeTit") ){
        $("#titleOfwork .def_controlTit").addClass("def_changeTit");
        $(This).removeClass("def_changeTit");
        if( num==1 ){
            $(".dealLog").css("display","none");
            $(".workRemarks").css("display","block");
        }else if( num==2 ){
            $(".dealLog").css("display","block");
            $(".workRemarks").css("display","none");
        }
    }
}

//移除上传文件
function delFile(that,id){
    var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
    $(that).parent().parent().remove();
    removeFile(fileS3Key);
}

function deleteAttachements(that,attache){
    $(that).parent().parent().remove();
    var fileS3Key = $(that).parent().prev().children().children(".file-s3Key").text();
    removeFile(fileS3Key);
    deleteAttaches.push(attache);
    /* $.ajax({
         type:'post',
         url:'/devManage/worktask/delFile',
         data:{
             attacheId:attache.id
         },
         success:function(data){
             if(data.status=="success"){
                 layer.alert('删除成功！', {
                     icon: 1,
                     title: "提示信息"
                 });
             }else if(data.status == "fail"){
                 layer.alert('删除失败！', {
                     icon: 1,
                     title: "提示信息"
                 });
             }
         }
     });*/
}
//文件上传，并列表展示
function uploadFileList(){
    //列表展示
    $(".upload-files").change(function(){
        var files = this.files;
        var formFile = new FormData();
        var filesSize = 0;

        for (var i = 0,len2 = files.length;i < len2;i++){
            filesSize += files[i].size;
            //  files.append("files",formFileList[i]);
        }
        if(filesSize > 1048576000){
            layer.alert('文件太大,请删选！！！', {
                icon: 2,
                title: "提示信息"
            });
            return false;
        }
        /*if(!fileAcceptBrowser()){
            for(var i=0,len=files.length;i<len;i++){
                var file_type = files[i].name.split(".")[1];
                if(file_type!="doc"&&file_type!="docx"&&file_type!="xls"&&file_type!="xlsx"&&file_type!="txt"&&file_type!="pdf"){
                    layer.alert('上传文件格式错误! 请上传后缀名为.doc，.docx，.xls，.xlsx，.txt，.pdf的文件', {icon:0});
                    return false;
                }
            }
        }*/

        outer:for(var i=0,len=files.length;i<len;i++){
            var file = files[i];
            if(file.size<=0){
                layer.alert(file.name+"文件为空！", {icon: 0});
                continue;
            }
            if(file.size > 10485760){
                layer.alert(file.name+'：超过10MB！', {
                    icon: 2,
                    title: "提示信息"
                });
                return false;
            }

            var fileList = [];
            var oldFileList = [];
            if(modalType == 'new'){
                fileList=_files;
            }else if(modalType == 'edit'){
                fileList=Neweditfiles;
                oldFileList=_editfiles;
            }else if(modalType == 'check'){
                fileList=_checkfiles;
            }else if(modalType=='dHandle'){
                fileList=_newDhandlefiles;
                oldFileList=_Dhandlefiles;
            }else if(modalType=='Handle'){
                oldFileList=_handlefiles
                fileList=_Newhandlefiles;
            }else if(modalType=='review'){
                fileList=NewCodeFiles;
                oldFileList=OldCodeFiles;
            }else if(modalType=="NOreview"){
                fileList=_newNoReviewFailed;
                oldFileList=_NoreviewFailed;
            }
            for(var j=0;j<fileList.length;j++){
                if(fileList[j].fileNameOld ==file.name){
                    layer.alert(file.name +" 文件已存在", {
                        icon: 2,
                        title: "提示信息"
                    });
                    continue outer;
                }
            }
            for(var j=0;j<oldFileList.length;j++){
                if(oldFileList[j].fileNameOld ==file.name){
                    layer.alert(file.name +" 文件已存在", {
                        icon: 2,
                        title: "提示信息"
                    });
                    continue outer;
                }
            }
            formFile.append("files", file);
            //读取文件
            if (window.FileReader) {
                (function(i){
                    var file = files[i];
                    var reader = new FileReader();
                    reader.readAsDataURL(file);
                    reader.onerror = function(e) {
                        layer.alert("文件" + file.name +" 读取出现错误", {icon: 0});
                        return false;
                    };
                    reader.onload = function (e) {
                        if(e.target.result) {
                            console.log("文件 "+file.name+" 读取成功！");
                        }
                    };
                })(i);
            }

            //列表展示
            var _tr = '';
            var file_name = file.name.split("\.")[0];
            var file_type = file.name.split("\.")[1];
            var _td_icon;
            var _td_name = '<span >'+file.name+'</span><i class="file-url"></i><i class = "file-bucket"></i><i class = "file-s3Key"></i></div></td>';
            var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="delFile(this)">删除</a></td>';
            switch(file_type){
                case "doc":
                case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                case "xls":
                case "xlsx":_td_icon = '<img src="'+_icon_excel+'" />';break;
                case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                default:_td_icon = '<img src="'+_icon_word+'" />';break;
            }
            var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
            _tr+='<tr><td><div class="fileTb">'+_td_icon+'  '+_td_name+_td_opt+'</tr>';
            _table.append(_tr);

        }
        //上传文件
        $("#loading",window.parent.document).css('display','block');
        $.ajax({
            type:'post',
            url:'/zuul/devManage/worktask/uploadFile',
            contentType: false,
            processData: false,
            dataType: "json",
            data:formFile,

            success:function(data){
                for(var k=0,len=data.length;k<len;k++){
                    if(modalType == 'new'){
                        _files.push(data[k]);
                    }else if(modalType == 'edit'){
                        Neweditfiles.push(data[k]);
                    }else if(modalType == 'check'){
                        _checkfiles.push(data[k]);
                    }else if(modalType=='dHandle'){
                        _newDhandlefiles.push(data[k]);
                    }else if(modalType=='Handle'){
                        _Newhandlefiles.push(data[k]);
                    }else if(modalType=='review'){
                        NewCodeFiles.push(data[k]);
                    }else if(modalType=='NOreview'){
                        _newNoReviewFailed.push(data[k]);
                    }
                    $(".file-upload-tb span").each(function(o){
                        if($(this).text() == data[k].fileNameOld){
                            //$(this).parent().children(".file-url").html(data[k].filePath);
                            $(this).parent().children(".file-bucket").html(data[k].fileS3Bucket);
                            $(this).parent().children(".file-s3Key").html(data[k].fileS3Key);
                        }
                    });
                }
                if(modalType == 'new'){
                    $("#addfiles").val(JSON.stringify(_files));
                    clearUploadFile('uploadFile');
                }else if(modalType == 'edit'){
                    //$("#editfiles").val(JSON.stringify(_editfiles));
                    $("#newFiles").val(JSON.stringify(Neweditfiles));
                    clearUploadFile('edituploadFile');
                }else if(modalType == 'check'){
                    $("#checkfiles").val(JSON.stringify(_checkfiles));
                    clearUploadFile('checkuploadFile');
                }else if(modalType=='dHandle'){
                    $("#NewDHattachFiles").val(JSON.stringify(_newDhandlefiles));
                    clearUploadFile('DHituploadFile');
                }else if(modalType=='Handle'){
                    $("#newHattachFiles").val(JSON.stringify(_Newhandlefiles));
                    clearUploadFile('HituploadFile');
                }else if(modalType=='review'){
                    $("#NewCodeFiles").val(JSON.stringify(NewCodeFiles));
                    clearUploadFile('CodeFile');
                }else if(modalType=='NOreview'){
                    $("#newreviewFiles").val(JSON.stringify(_newNoReviewFailed));
                    clearUploadFile('reviewUploadFile');
                }
                $("#loading",window.parent.document).css('display','none');
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){
                $("#loading",window.parent.document).css('display','none');
                layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
            }
        });
    });
}
/**
 * 重构表单验证
 */
function refactorFormValidator(){
    $('#newPerson').on('hidden.bs.modal', function() {
        $("#addDev").data('bootstrapValidator').destroy();
        $('#addDev').data('bootstrapValidator', null);
        formValidator();
    });

    $('#Handle').on('hidden.bs.modal', function() {
        $("#HandleDev").data('bootstrapValidator').destroy();
        $('#HandleDev').data('bootstrapValidator', null);
        formValidator();
    });
    $('#showeditDev').on('hidden.bs.modal', function() {
        $("#editDev").data('bootstrapValidator').destroy();
        $('#editDev').data('bootstrapValidator', null);
        formValidator();
    });
    $('#Assignment').on('hidden.bs.modal', function() {
        $("#assignForm").data('bootstrapValidator').destroy();
        $('#assignForm').data('bootstrapValidator', null);
        formValidator();
    });
    $('#reviewFailed').on('hidden.bs.modal', function() {
        $("#reviewDev").data('bootstrapValidator').destroy();
        $('#reviewDev').data('bootstrapValidator', null);
        formValidator();
    });


}
//清空上传文件
function clearUploadFile(idName){
    /*$(idName).wrap('<form></form>');
    $(idName).unwrap();*/
    $('#'+idName+'').val('');
}
//文件下载
function download(row){
    var fileS3Bucket = row.fileS3Bucket;
    var fileS3Key = row.fileS3Key;
    var fileNameOld = row.fileNameOld;
    window.location.href = "/devManage/worktask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld;


}
//移除暂存数组中的指定文件
function removeFile(fileS3Key){
    if(modalType == "new"){
        var _file = $("#addfiles").val();
        if(_file != ""){
            var files = JSON.parse(_file);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _files = files;
            $("#addfiles").val(JSON.stringify(files));
        }

    }else if(modalType == 'edit'){
        var _file = $("#editfiles").val();
        var _newfile = $("#newFiles").val();
        if(_file != ""){
            var files = JSON.parse(_file);
            for(var i=0;i<files.length;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _editfiles = files;
            $("#editfiles").val(JSON.stringify(files));
        }
        if(_newfile != ""){
            var files = JSON.parse(_newfile);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            Neweditfiles = files;
            $("#newFiles").val(JSON.stringify(files));
        }
    }else if(modalType == 'check'){
        var _file = $("#checkfiles").val();
        if(_file != ""){
            var files = JSON.parse(_file);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _checkfiles  = files;
            $("#checkfiles").val(JSON.stringify(files));
        }
    }else if(modalType == 'dHandle'){
        var _file = $("#DHattachFiles").val();
        var _newfile = $("#NewDHattachFiles").val();
        if(_file != ""){
            var files = JSON.parse(_file);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _Dhandlefiles  = files;
            $("#DHattachFiles").val(JSON.stringify(files));
        }
        if(_newfile != ""){
            var files = JSON.parse(_newfile);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _newDhandlefiles = files;
            $("#NewDHattachFiles").val(JSON.stringify(files));
        }
    }else if(modalType == 'Handle'){
        var _file = $("#HattachFiles").val();
        var _newfile = $("#newHattachFiles").val();
        if(_file != ""){
            var files = JSON.parse(_file);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _handlefiles  = files;
            $("#HattachFiles").val(JSON.stringify(files));
        }
        if(_newfile != ""){
            var files = JSON.parse(_newfile);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _Newhandlefiles = files;
            $("#newHattachFiles").val(JSON.stringify(files));
        }
    }else if(modalType == 'review'){
        var _file = $("#OldCodeFiles").val();
        var _newfile = $("#NewCodeFiles").val();
        if(_file != ""){
            var files = JSON.parse(_file);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            OldCodeFiles = files;
            $("#OldCodeFiles").val(JSON.stringify(files));
        }
        if(_newfile != ""){
            var files = JSON.parse(_newfile);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            NewCodeFiles= files;
            $("#NewCodeFiles").val(JSON.stringify(files));
        }
    }else if(modalType == 'NOreview'){
        var _file = $("#reviewFiles").val();
        var _newfile = $("#newreviewFiles").val();
        if(_file != ""){
            var files = JSON.parse(_file);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _NoreviewFailed = files;
            $("#reviewFiles").val(JSON.stringify(files));
        }
        if(_newfile != ""){
            var files = JSON.parse(_newfile);
            for(var i=0,len=files.length;i<len;i++){
                if(files[i].fileS3Key == fileS3Key){
                    Array.prototype.splice.call(files,i,1);
                    break;
                }
            }
            _newNoReviewFailed= files;
            $("#newreviewFiles").val(JSON.stringify(files));
        }
    }
}

function isJSON(str) {
    if (typeof str == 'string') {
        try {
            JSON.parse(str);
            return true;
        } catch(e) {
            return false;
        }
    }
}

function appendDataType( thisData , id ,status){
    var obj=$('<div class="def_col_18"></div>');
    switch ( thisData.type ) {
        case "int":
            obj.attr( "dataType","int");
            var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
            if( status== "new" ){
                var labelContent=$( '<div class="def_col_28"><input maxlength="'+ thisData.maxLength +'" fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="int" value="'+ thisData.defaultValue +'" /></div>' );
            }else{
                var labelContent=$( '<div class="def_col_28"><input maxlength="'+ thisData.maxLength +'" fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="int" value="'+ thisData.valueName +'" /></div>' );
            }
            labelContent.children( " input " ).bind("keyup",function (){
                this.value=this.value.replace(/\D/gi,"");
            })
            obj.append( labelName , labelContent );
            break;
        case "float":
            obj.attr( "dataType","float")
            var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
            if( status== "new" ){
                var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="number" class="form-control" placeholder="请输入" name="float" value="'+ thisData.defaultValue +'" /></div>' );
            }else{
                var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="number" class="form-control" placeholder="请输入" name="float" value="'+ thisData.valueName +'" /></div>' );
            }
            obj.append( labelName , labelContent );
            break;
        case "varchar":
            obj.attr( "dataType","varchar")
            var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
            if( status== "new" ){
                var labelContent=$( '<div class="def_col_28"><input  maxlength="'+ thisData.maxLength +'"  fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="varchar" value="'+ thisData.defaultValue +'" /></div>' );
            }else{
                var labelContent=$( '<div class="def_col_28"><input  maxlength="'+ thisData.maxLength +'"  fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" type="text" class="form-control" placeholder="请输入" name="varchar" value="'+ thisData.valueName +'" /></div>' );
            }
            obj.append( labelName , labelContent );
            break;
        case "data":
            obj.attr( "dataType","data")
            var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
            if( status== "new" ){
                var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="new'+ thisData.fieldName +'" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="'+ thisData.defaultValue +'" /></div>' );
            }else{
                var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="edit'+ thisData.fieldName +'" type="text" readonly class="form-control pointStyle" placeholder="请输入" name="data" value="'+ thisData.valueName +'" /></div>' );
            }
            obj.append( labelName , labelContent );
            break;
        case "timestamp":
            obj.attr( "dataType","timestamp")
            var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
            if( status== "new" ){
                var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="new'+ thisData.fieldName +'" type="text" readonly id="new_'+ thisData.fieldName +'" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="'+ thisData.defaultValue +'" /></div>' );
            }else{
                var labelContent=$( '<div class="def_col_28"><input fName="'+ thisData.fieldName +'" requireded="'+ thisData.required +'" id="edit'+ thisData.fieldName +'" type="text" readonly id="new_'+ thisData.fieldName +'" class="form-control pointStyle" placeholder="请输入" name="timestamp" value="'+ thisData.valueName +'" /></div>' );
            }
            obj.append( labelName , labelContent );
            break;
        case "enum":
            obj.attr( "dataType","enum")
            var select=$( '<select class="selectpicker" requireded="'+ thisData.required +'" fName="'+ thisData.fieldName +'"></select>' )
            var options=JSON.parse(  thisData.enums  );
            select.append( '<option value=""  fName="'+ thisData.fieldName +'">请选择</option>'  );
            for( var i=0;i<options.length;i++ ){
                if( options[i].enumStatus == 1 ){
                    if(  status== "edit" && options[i].value == thisData.valueName  ){
                        select.append( '<option value="'+options[i].value+'" selected >'+options[i].value+'</option>'  );
                    }else{
                        select.append( '<option value="'+options[i].value+'">'+options[i].value+'</option>'  );
                    }

                }
            }
            var labelName=$( '<div class="def_col_8 font_right ">'+ thisData.label +'：</div>' );
            var labelContent=$( '<div class="def_col_28"></div>' );
            labelContent.append( select  );
            obj.append( labelName , labelContent );
            break;
        default:
            break;
    }
    $( "#"+id ).append( obj );
    if( obj.attr( "dataType") == "data" ){
        laydate.render({
            elem: "#"+ obj.find( "input" )[0].id,
            trigger: 'click',
            done:function(value,date,endDate){
                $( "#"+ obj.find( "input" )[0].id ).next().css("display","block");
            }
        });
    }else if( obj.attr( "dataType") == "timestamp" ){
        laydate.render({
            elem: "#"+ obj.find( "input" )[0].id,
            trigger: 'click',
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            done:function(value,date,endDate){
                $( "#"+ obj.find( "input" )[0].id ).next().css("display","block");
            }
        });
    }
    $(".selectpicker").selectpicker('refresh');
}
function getFieldData( id ){
    var data = {"field":[]};
    for( var i=0;i< $( "#"+ id +" > div" ).length;i++ ){
        //int float varchar data timestamp enum
        var obj={};
        var type = $( "#"+ id +" > div" ).eq( i ).attr( "dataType" )
        if( type == "int" || type == "float" || type == "varchar" || type == "data" || type == "timestamp" ){
            obj.fieldName=$( "#"+ id +" > div" ).eq( i ).find( "input" ).attr( "fName" );
            obj.required=$( "#"+ id +" > div" ).eq( i ).find( "input" ).attr( "requireded" );
            obj.valueName=$( "#"+ id +" > div" ).eq( i ).find( "input" ).val();
            obj.labelName=$( "#"+ id +" > div" ).eq( i ).children("div").eq( 0 ).text();
        }else if(  type == "enum"  ){
            obj.fieldName=$( "#"+ id +" > div" ).eq( i ).find( "select" ).attr( "fName" );
            obj.required=$( "#"+ id +" > div" ).eq( i ).find( "select" ).attr( "requireded" );
            obj.valueName=$( "#"+ id +" > div" ).eq( i ).find( "select" ).val();
            obj.labelName=$( "#"+ id +" > div" ).eq( i ).children("div").eq( 0 ).text();
        }
        data.field.push( obj );
    }
    return data;
}


function showField( data ){
    $( "#checkEditField" ).empty();
    if(data != null && data != "undefined" && data.length > 0 ){
        for( var i=0;i<data.length;i++ ){
            var aLabel='<div class="form-group col-md-4"><label class="col-sm-4 control-label font_left fontWeihgt">'+ data[i].label +'：</label>'+
                '<label class="col-sm-8 select-label">'+ data[i].valueName +'</label>';
            $( "#checkEditField" ).append( aLabel );
        }
    }
}



function copyReq() {
    var idArr=$('#devlist').jqGrid('getGridParam','selarrrow')
    if( idArr.length>1 || idArr.length==0 ){
        layer.alert('请选择一个开发工作任务!',{
            icon: 0,
        })
        return ;
    }else {
        $("#loading").css('display', 'block');
        var obj=$("#devlist").jqGrid('getRowData', idArr[0]);
        newPerson_btn();
        $.ajax({
            method:"post",
            url:"/devManage/worktask/getEditDevTask",
            data:{"id":obj.id},
            success : function(data) {
                var map=data.dev;
                for(var key in map){
                    $("#new_taskUserId").val(map[key].devUserId);
                    $("#new_taskUser").val(map[key].userName);
                    $("#devTaskPriority").val(map[key].devTaskPriority);
                    $("#new_devTaskType").val(map[key].devTaskType);


                    $("#featureCode").val(map[key].featureName);
                    $("#Attribute").attr("featurecode",map[key].requirementFeatureId);
                    $("#Attribute").attr("commissioningwindowid",map[key].commissioningWindowId);
                    $("#Attribute").attr("requirementfeaturestatus",map[key].requirementFeatureStatus );
                    //getEdFeature(map[key].requirementFeatureId);

                    $("#ataskName").val(map[key].devTaskName);
                    $("#taskOverview").val(map[key].devTaskOverview);
                    $("#wSystemId").val(map[key].systemId);
                    $("#newSprintId").val(map[key].sprintId);
                    $("#newSprintName").val(map[key].sprintName);

                    if(map[key].planStartDate!=null){
                        var start=datFmt(new Date(map[key].planStartDate),"yyyy-MM-dd");
                        $("#startWork").val(start);
                        $('#startWork').datetimepicker('setEndDate', start );
                    }
                    if(map[key].planEndDate!=null){
                        var end=datFmt(new Date(map[key].planEndDate),"yyyy-MM-dd");
                        $("#endWork").val(end);
                        $('#endWork').datetimepicker('setStartDate', end );
                    }
                    $("#workLoad").val(map[key].planWorkload).change();
                }
                var field = data.fields;
                $("#canEditField").empty();
                if( field != undefined ){
                    for( var i = 0;i < field.length;i++ ){
                        if( field[i].status == 1 ){
                            appendDataType( field[i] , 'canEditField' , 'edit' );
                        }
                    }
                }
                if(data.attachements!=undefined){
                    var _table=$("#newFileTable");
                    var attMap=data.attachements;
                    //var _table=$(this).parent(".file-upload-select").next(".file-upload-list").children("table");
                    for(var i=0 ;i<attMap.length;i++){
                        var _tr = '';
                        var file_name = attMap[i].fileNameOld;
                        var file_type = attMap[i].fileType;
                        var file_id =  attMap[i].id;
                        var attache = JSON.stringify(attMap[i]).replace(/"/g, '&quot;');
                        var _td_icon;
                        //<i class="file-url">'+data.attachements[i].filePath+'</i>
                        var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
                        var _td_opt = '<td><a href="javascript:void(0);" class="del-file-button" onclick="deleteAttachements(this,'+attache+')">删除</a></td>';
                        switch(file_type){
                            case "doc":
                            case "docx":_td_icon = '<img src="'+_icon_word+'" />';break;
                            case "xls":
                            case "xlsx":_td_icon = '<img src='+_icon_excel+' />';break;
                            case "txt":_td_icon = '<img src="'+_icon_text+'" />';break;
                            case "pdf":_td_icon = '<img src="'+_icon_pdf+'" />';break;
                            default:_td_icon = '<img src="'+_icon_word+'" />';break;
                        }
                        var row =  JSON.stringify( attMap[i]).replace(/"/g, '&quot;');
                        _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="download('+row+')">'+_td_icon+" "+_td_name+_td_opt+'</tr>';
                        _table.append(_tr);
                        _editfiles.push(data.attachements[i]);
                        $("#addfiles").val(JSON.stringify(_editfiles));
                    }
                }
                $('.selectpicker').selectpicker('refresh');
                $("#newPerson").modal("show");
                $("#loading").css('display','none');
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){
                layer.alert("系统内部错误，请联系管理员 ！！！", {icon: 0});
            }

        });
        $("#newDevTask").modal("show");

    }
}

function dateComponent(){

    var locale = {
        "format": 'yyyy-MM-dd',
        "separator": " -222 ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "起始时间",
        "toLabel": "结束时间",
        "customRangeLabel": "自定义",
        "weekLabel": "W",
        "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
        "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        "firstDay": 1
    };
    $("#dev_createDate").daterangepicker({'locale': locale});//, "timePicker": true,"timePicker24Hour": true
	$('#dev_createDate').on('cancel.daterangepicker', function(ev, picker) {
		$("#dev_createDate").parent().find('.btn_clear').show();
	});
	$('#dev_createDate').on('apply.daterangepicker', function(ev, picker) {
		  $("#dev_createDate").parent().find('.btn_clear').show();
	});
}


//同步jira附件弹窗
function syncJira(){
    $("#workCodes").val('');
    $("#workCodes_modal").modal("show");
}


//同步jira提交
var syncJiraKey;
function syncJira_submit(){
    if(!$("#workCodes").val()){
        layer.alert("请输入编号!!!",{icon : 0});
        return;
    }
    var workCodeJiraIds = $.trim($("#workCodes").val()).replace("，",",");
    $("#loading").css('display', 'block');
    $.ajax({
        method:"post",
        url:"/devManage/worktask/jiraFileByCode",
        data:{
            "workCodeJiraIds":workCodeJiraIds,
            key:syncJiraKey
        },
        success : function(data) {
            if( data.status == 1 ){
                if(data.code != null && data.code != undefined && data.code.length > 0){
                    layer.alert(data.code.join("<br>"));
                } else {
                    layer.alert("同步成功",{icon : 1});
                }
                syncJiraKey = data.key;
                $("#workCodes_modal").modal("hide");
            } else {
                layer.alert("同步失败",{icon : 2});
            }
            $("#loading").css('display', 'none');
        }
    })
}

//获取开发任务状态
function getReqFeatureStatus(){
    var devtaskStatusList = '';
    $.ajax({
        url:"/devManage/devtask/getReqFeatureStatus",
        dataType:"json",
        type:"post",
        async:false,
        success:function(data){
            devtaskStatusList = data.reqFeatureStatus;
        },
        error:function(){
            layer.alert("系统内部错误，请联系管理员！！！",{icon:2});
        }

    });
    return devtaskStatusList;
}




//显示/隐藏列
function addCheckBox() {
	$("#colGroup").empty();
	var str = "";                   
	str = '<div class="onesCol"><input type="checkbox" value="devTaskCode" onclick="showHideCol( this )" /><span>任务编号</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="devTaskName" onclick="showHideCol( this )" /><span>任务名称</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="devTaskStatus" onclick="showHideCol( this )" /><span>状态</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="systemName" onclick="showHideCol( this )" /><span>子系统</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="requirementCode" onclick="showHideCol( this )" /><span>关联需求</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="featureCode" onclick="showHideCol( this )" /><span>开发任务</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="devTaskPriority" onclick="showHideCol( this )" /><span>优先级</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="requirementFeatureSource" onclick="showHideCol( this )" /><span>任务来源</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="userName" onclick="showHideCol( this )" /><span>开发人员</span></div>' +
		'<div class="onesCol"><input type="checkbox" value="estimateRemainWorkload" onclick="showHideCol( this )" /><span>剩余工作量</span></div>'+
		'<div class="onesCol"><input type="checkbox" value="opt" onclick="showHideCol( this )" /><span>操作</span></div>';
		
	$("#colGroup").append(str)
}

function showHideCol(This) {
	var colModel = $("#devlist").jqGrid('getGridParam', 'colModel');
	var width = 0;// 获取当前列的列宽
	var arr = [];
	if(!colModel) return
	for (var i = 0; i < colModel.length; i++) {
		if (colModel[i]["hidden"] == false) {
			if (colModel[i]["name"] != "cb") {
				arr.push(colModel[i]["hidden"]);
			}
		}
	}
	if ($(This).is(':checked')) {
		$("#devlist").setGridParam().hideCol($(This).attr('value'));
		$("#devlist").setGridWidth($('.wode').width());
		if (arr.length == 1) {
			$("#devlist").jqGrid('setGridState', 'hidden');
		}
	} else {
		$("#devlist").jqGrid('setGridState', 'visible');
		$("#devlist").setGridParam().showCol($(This).attr('value'));
		$("#devlist").setGridWidth($('.wode').width());
	}
}

function default_list(){
	var data = [];
    $("#list2").jqGrid({
    	data:data,
        datatype: 'local',
        contentType: "application/json; charset=utf-8",
        mtype:"post",
        height: 'auto',
        multiselect : true,
        width: $(".content-table").width()*0.999,
        colNames:['id','任务编号', '任务名称','状态','子系统','关联需求','开发任务','优先级','任务来源','开发人员','预估剩余工作量(人天)','操作'],
        colModel:[
            {name:'id',index:'id',hidden:true},
            {name:'devTaskCode',index:'devTaskCode', sorttype:'string',
            },
            {name:'devTaskName',index:'devTaskName',
            },
            {name:'devTaskStatus',index:'devTaskStatus',width:115,
            },
            {name:'systemName',index:'systemName'},
            {name:'requirementCode',index:'requirementCode',
            },
            {name:'featureCode',index:'featureCode',width:70,
            },
            {name:'devTaskPriority',index:'devTaskPriority',width:50,
            },
            {name:'requirementFeatureSource',index:'requirementFeatureSource',width:80,
            },
            {name:'userName',index:'userName',width:70 },
            {name:'estimateRemainWorkload',index:'estimateRemainWorkload',width:50},
            {
                name:'opt',
                index:'操作',
                align:"left",
                width:80,
                sortable:false,
            }
        ],
        rowNum:10,
        rowTotal: 200,
        rowList : [10,20,30],
        rownumWidth: 40,
        pager: '#pager2',
        sortable:true,   //是否可排序
        sortorder: 'desc',
        sortname: 'id',
        loadtext:"数据加载中......",
        viewrecords: true, //是否要显示总记录数
        loadComplete :function(){
            $("#loading").css('display','none');
        },
        beforeRequest:function(){
            $("#loading").css('display','block');
        }
    }).trigger("reloadGrid");
}

//模糊搜索配置 
function search_settings(){
	var search_arr = [
		{
			ele : 'new_taskUser',
			userId : 'new_taskUserId',
		},
		{
			ele : 'TransferUser',
			userId : 'TransferUserId',
		},
		{
			ele : 'assignUser',
			userId : 'assignUserID',
		},
		{
			ele : 'edtaskUser',
			userId : 'edit_taskUserId',
		}
	]
	search_arr.map(function(val){
		fuzzy_search_radio2({
			ele : val.ele, 
			url : '/system/user/getUserByNameOrACC', 
			top : '28px', 
			name : 'userName', 
			account : 'userAccount', 
			id : 'id', 
			userId : $("#"+val.userId), 
			rows : 'userInfo', 
		});
	})
}

