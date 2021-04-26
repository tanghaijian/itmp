/**
*@author liushan
*@Description 测试执行管理
 * 新增，复制，删除，导入，导出，关联案例，引入测试集案例，归档，排序 在testSetCaseCommonOpt.js
*@Date 2020/4/9
*@Param 
*@return
**/

var caseRows = {
    excuteRound:1
};
var executeResult = '';

var testSet = {
    id:"",
    testSetName:"",
    testCaseId:"",
    testSetCase:{},
    newTableData:"",
    excuteRemark:"",
    editFiles:"",
    pageSize:20,
    casePageSize:100,
    flag:true,
    case_flag:true,
    case_show_num:0
};
let previous = null; // 记录上一次运行的时间
let previous_case = null; // 记录上一次运行的时间

var defect = {
    //图文混编编辑器
    new_editor:""
};

var execute = {
    // 测试集案例执行状态
    dicType :{
        fail_bug:0,
        success:2,
        fail:3,
        hangUp:4,
        unmake:5
    },

    // 测试集案例执行图标
    picPath:{
        edit:"../static/images/testExecution/bianji.png",
        check:"../static/images/testExecution/chakanjilu.png",
        success:"../static/images/testExecution/chenggong.png",
        fail:"../static/images/testExecution/shibai.png",
        fail_bug:"../static/images/testExecution/bug.png",
        hangUp:"../static/images/testExecution/guaqi.png",
        unmake:"../static/images/testExecution/chexiao.png"
    }
};

$(document).ready(function () {
    testExecution.left_Opt.getAllExecuteCase(1);
    $(".def_choice").on("click",function(){
        if( $( this ).hasClass( "def_Selection" ) ){
            $( this ).removeClass( "def_Selection" );
        }else{
            $( this ).addClass( "def_Selection" );
        }
    });
    $(".bigScreenBtn").on("click",function(){
        if( $( this ).parent().parent().hasClass( "bigScreen" ) ){
            $( this ).parent().parent().removeClass( "bigScreen" );
        }else{
            $( this ).parent().parent().addClass( "bigScreen" );
        }
    });

    //富文本编辑初始化
    var new_editor_E = window.wangEditor;
    defect.new_editor = new new_editor_E('#defectOverview');
    defect.new_editor.customConfig.uploadImgShowBase64 = true;
    defect.new_editor.customConfig.pasteIgnoreImg = true;
    defect.new_editor.customConfig.menus = false;
    defect.new_editor.customConfig.pasteTextHandle = function (content) {
        // content 即粘贴过来的内容（html 或 纯文本），可进行自定义处理然后返回
        if (content == '' && !content) return '';
        var str = content
        str = str.replace(/<xml>[\s\S]*?<\/xml>/ig, '');
        str = str.replace(/<style>[\s\S]*?<\/style>/ig, '');
        str = str.replace(/<\/?[^>]*>/g, '');
        str = str.replace(/[ | ]*\n/g, '\n');
        str = str.replace(/&nbsp;/ig, '');
        return str
    };
    defect.new_editor.create();
    defect.new_editor.$textElem.attr('contenteditable', true);


    //搜索操作单元事件
    $("#new_windowName").click(function () {
        testExecution.modal.window.send();
    });
//    var __click_handle = {
//        new_assignUserName: 'new', /*新建主修复人*/
//        new_testUserName: 'new2',//新建测试人
//        new_developer: 'new3', // 新建开发人员
//        new_windowName: '' // 新建投产窗口
//    };
//    for (var key in __click_handle) {
//        (function (key, param) {
//            $("#" + key).click(function () {
//                if(param == "new4"){
//                    testExecution.modal.window.send();
//                } else {
//                    testExecution.modal.user(param);
//                }
//            });
//        })(key, __click_handle[key])
//    }
     //


    //日期时间范围
    laydate.render({
        elem: '#timeDate',
        type: 'date',
        format: 'yyyy-MM-dd',
        range: "-",
        trigger: 'click'
    });

    upFileList();
    testExecution.formValidator();
    testExecution.refactorFormValidator();
    testExecution.right_Opt.execution_edit_button.defect.select_project();

    //搜索展开和收起
    downOrUpButton();
    search_settings();
});

//模糊搜索配置
function search_settings(){
//	new_assignUserName: 'new', /*新建主修复人*/
//  new_testUserName: 'new2',//新建测试人
//  new_developer: 'new3', // 新建开发人员
	var search_arr = [
		{
			ele : 'new_assignUserName',
			userId : 'new_assignUserId',
		},
		{
			ele : 'new_testUserName',
			userId : 'new_testUserId',
		},
		{
			ele : 'new_developer',
			userId : 'new_developerId',
		},
	]
	search_arr.map(function(val){
		fuzzy_search_radio2({
			ele : val.ele, 
			url : '/testManage/testtask/getUserByNameOrACC', 
			top : '28px', 
			name : 'userName', 
			account : 'userAccount', 
			id : 'id', 
			userId : $("#"+val.userId), 
			rows : 'userInfo', 
		});
	})
}

// 测试执行页面操作
var testExecution = {
    // 测试执行左边操作
    left_Opt : {
        // 滑动分页高度控制
        throttle:function (fn, wait, time, This,type) {
            let timer = null;
            let now = +new Date();
            if(type == 1){
                if (!previous) previous = now;
                // 当上一次执行的时间与当前的时间差大于设置的执行间隔时长的话，就主动执行一次
                if (now - previous > time) {
                    fn(This);
                }
            } else {
                if (!previous_case) previous_case = now;
                // 当上一次执行的时间与当前的时间差大于设置的执行间隔时长的话，就主动执行一次
                if (now - previous_case > time) {
                    fn(This);
                }
            }
        },

        // 滑动分页显示
        moreExecuteCase:function(This){
            var viewH = $(This).height();// 可见高度
            var contentH = $(This).get(0).scrollHeight;// 内容高度
            var scrollTop = $(This).scrollTop();// 滚动高度
            if (contentH - viewH - scrollTop <= 30) {
                if(testSet.flag){
                    testSet.flag = false;
                    if( Number($( This ).attr("page"))*testSet.pageSize < Number($( This ).attr("total"))){
                        $( This ).attr("page" , Number( $( This ).attr("page") )+1 );
                        testExecution.left_Opt.getAllExecuteCase(2);
                    }
                }
            } else {
                testSet.flag = true;
            }
        },

        // 显示测试工作任务列表
        getAllExecuteCase : function (flag){
            flag==1?$("#taskTree").attr("page",1):$("#taskTree").attr("page");
            $("#loading").css('display','block');
            var num='';
            if( !$("#onlyDoing").hasClass("def_Selection") ){
                num=2;
            }
            var own = '';
            if( !$("#onlyDoing2").hasClass("def_Selection") ){
                own = parent.$("#userId").val();
            }
            $.ajax({
                url : "/testManage/testSet/selectTestTaskWithTestSet",
                method:"post",
                contentType:"application/json",
                data:JSON.stringify({
                    own : own,
                    testTaskName:htmlEncodeJQ($.trim($("#checkTaskName").val())),
                    testSetName :htmlEncodeJQ($.trim( $("#checkTestSetName").val())),
                    testSetCaseExecuteResult: $("#executeResult").val() ? $("#executeResult").val().join(",") : "" ,
                    testTaskStatus: num,
                    pageNumber:$("#taskTree").attr("page"),
                    pageSize:testSet.pageSize
                }),
                success:function(data){
                    if(flag == 1){$("#taskTree").empty();}
                    if (data.status == 2){
                        layer.alert(data.errorMessage, {
                            icon: 2,
                            title: "提示信息"
                        });
                        $("#taskTree").html( '<div class="haveNoFont"><span>暂无测试工作任务...</span></div>' );
                    } else if(data.status = 1){
                        if(data.testTaskList == undefined || data.testTaskList.length == 0){
                            $("#taskTree").html( '<div class="haveNoFont"><span>暂无测试工作任务...</span></div>' );
                        } else {
                            $("#taskTree").attr("total",data.total);
                            $(".haveNoFont").remove();
                            for( var k=0;k<data.testTaskList.length;k++  ){
                                var str='';
                                str+='<div class="testTask">'+
                                    '<div class="taskTop" onclick="testExecution.left_Opt.getAllTestSetByFeatureId(this ,'+ data.testTaskList[k].id +')" val='+ data.testTaskList[k].id +'><img src="../static/images/testExe/task.png"><span class="taskName">'+data.testTaskList[k].testTaskCode+' '+data.testTaskList[k].testTaskName+'</span></div>'+
                                    '<div class="taskBottom" status="false" ><div class="taskContent"></div></div></div>';
                                $("#taskTree").append( str );
                            }
                        }
                    }
                    testSet.flag = true;
                    previous = +new Date();// 执行函数后，马上记录当前时间
                    $("#loading").css('display','none');
                },
                error:errorFunMsg
            })
        },

        // 显示测试集数据
        getAllTestSetByFeatureId : function( This, testTaskId ){
            $(".taskName ").removeClass("testSetBack");
            $(This).children(".taskName").addClass("testSetBack");
            if( $(This).parent().children(".taskBottom").css("display")==="none" ){
                $("#loading").css('display','block');
                $.ajax({
                    url : "/testManage/testSet/getTestSetBytestTaskId",
                    method:"post",
                    contentType:"application/json",
                    data:JSON.stringify({
                        testTaskId : testTaskId,
                        testSetName : htmlEncodeJQ($.trim($("#checkTestSetName").val()))
                    }),
                    success:function(data){
                        if (data.status == 2){
                            layer.alert(data.errorMessage, {
                                icon: 2,
                                title: "提示信息"
                            });
                        } else if(data.status == 1){
                            var rows = data.testSetList;
                            if( $(This).parent().children(".taskBottom").attr("status")==="false" ){
                                $(This).parent().children(".taskBottom").attr("status","true");
                                if(rows != undefined){
                                    for( var i=0;i< rows.length;i++ ){
                                        var str='';
                                        var noBorder='';
                                        if( i == rows.length-1 ){
                                            noBorder='noBorder';
                                        }
                                        str+='<div class="testJob" val='+ rows[i].testSetId +'><div class="testName"><div class="testNameFont" onclick="testExecution.left_Opt.moreCase(this,'+rows[i].testSetId+",\'"+rows[i].testSetName+'\')" >'+ rows[i].testSetName+'</div></div>'+
                                            '<div class="testContent hideAll '+noBorder+'">';
                                        str+='</div></div>';
                                        $(This).parent().children(".taskBottom").children(".taskContent").append( str );
                                    }
                                }
                            }
                            $(This).parent().children(".taskBottom").css("display","block");
                        }
                        $("#loading").css('display','none');
                    },
                    error:errorFunMsg
                })
            }else{
                $(This).parent().children(".taskBottom").css("display","none");
            }
        },

        // 点击测试集显示测试案例数据
        moreCase : function (that,testSetId ,testSetName){
            $(".testNameFont ").removeClass("testSetBack");
            $(that).addClass("testSetBack");
            testSet.id = "";
            testSet.testSetName = "";
            testSet.id = testSetId;
            testSet.testSetName = testSetName;
            $(".right_blank").css("display","none");
            $(".right").css("display","block");
            $("#exeExecuteState").selectpicker('val', '');
            $( "#executeHiddenID" ).val("");
            $( "#exeExcuteRound" ).val( "" );
            $( "#exeTitleName" ).html( "" );
            $( "#executeHiddenID" ).val( testSet.id );
            $( "#exeExcuteRound" ).val( 1 );
            $( "#exeTitleName" ).html( testSet.testSetName );
            $( "#excuteRound" ).val( "" );
            $( "#excuteRound" ).val(1 );
            $( ".right_div" ).css( "display","none" ); //显示右边的案例div
            $(".batchExecutivePage").css("display","block");
            testExecution.right_Opt.executionTable.initExecutionTable();
        }
    },

    // 测试执行右边操作
    right_Opt : {

        // 执行列表页面操作
        executionTable : {

            // 测试案例列表数据
            initExecutionTable : function (){
                $("#loading").css('display','block');
                $("#executionTable_div").attr("page",1);
                $("#executionTable").bootstrapTable('destroy');
                $("#executionTable").bootstrapTable({
                    url:'/testManage/testExecution/getTestCaseExecute',
                    method:"post",
                    queryParamsType:"",
                    pagination : false,
                    sidePagination: "server",
                    maintainSelected : true,
                    uniqueId : "setCaseId",
                    contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
                    queryParams : function() {
                        testSetCase_param.param_list = "";
                        var param = {
                            testSetMap :JSON.stringify({
                                testSetId :  testSet.id,
                                caseExecuteResultList :  $("#exeExecuteState").val() ? $("#exeExecuteState").val().join(",") : "",
                                pageNumber:1,
                                pageSize:testSet.casePageSize
                            })
                        };
                        testSetCase_param.param_list = param;
                        $("#loading").css('display','block');
                        return param;
                    },
                    responseHandler:function(res) {
                        if(res.status == 1 ){
                            if(res.feature != undefined){
                                // 引入测试集关联案例 搜索条件赋值
                                $("#reqFeatureId").val(res.feature.id);
                                $("#reqFeature").val(res.feature.featureName);
                            }
                            $("#executionTable_div").attr("total",res.total);
                            $("#span_case_total").text(res.total);
                            testSet.case_show_num = res.rows.length;
                            $("#span_case_show").text( testSet.case_show_num);
                            $("#span_case_remain").text(Number(res.total) -  Number(testSet.case_show_num));
                            return res.rows;
                        }
                    },
                    columns : [{
                        checkbox : true
                    },{
                        field : "setCaseId",
                        title : "setCaseId",
                        visible :  false,
                        align : 'center'
                    },{
                        field : "orderCase",
                        title : "排序",
                        width: "20",
                        align : 'center'
                    },{
                        field : "caseNumber",
                        title : "案例编号",
                        align : 'center',
                        visible :  false
                    },{
                        field : "caseName",
                        title : "案例名称",
                        align : 'center' ,
                        formatter : function (value, row, index) {
                            return'<a  href="javascript:void(0);" class="edit-opt table_w" title="'+ isValueNull(value) +'" onclick="testExecution.right_Opt.executionTable.getAllExecuteCases(true,'+row.setCaseId+')">'+isValueNull(value)+'</a>';
                        }
                    },{
                        field : "businessType",
                        title : "业务类型",
                        align : 'center',
                        formatter : function (value, row, index) {
                            return'<span class="table_w" title="'+ isValueNull(value) +'">'+isValueNull(value)+'</span>';
                        }
                    },{
                        field : "moduleName",
                        title : "模块",
                        width: "20",
                        align : 'center',
                        formatter : function (value, row, index) {
                            return'<span class="table_w" title="'+isValueNull(value)+'">'+isValueNull(value)+'</span>';
                        }
                    },{
                        field : "testPoint",
                        title : "测试项",
                        width: "20",
                        align : 'center',
                        formatter : function (value, row, index) {
                            return'<span class="table_w" title="'+isValueNull(value)+'">'+isValueNull(value)+'</span>';
                        }
                    },{
                        field : "casePrecondition",
                        title : "预置条件",
                        width: "20",
                        align : 'center',
                        formatter : function (value, row, index) {
                            return'<span class="table_w" title="'+isValueNull(value)+'">'+isValueNull(value)+'</span>';
                        }
                    },{
                        field : "inputData",
                        title : "输入数据",
                        width: "20",
                        align : 'center',
                        formatter : function (value, row, index) {
                            return'<span class="table_w" title="'+isValueNull(value)+'">'+isValueNull(value)+'</span>';
                        }
                    },{
                        field : "caseStep",
                        title : "案例步骤",
                        width: "20",
                        align : 'center',
                        formatter : function (value, row, index) {
                            if(value != null && value.length > 0){
                                var title = "";
                                for(var i = 0;i < value.length;i++){
                                    title += "步骤："+value[i].stepOrder
                                        +"  步骤描述："+value[i].stepDescription
                                        +"  预期结果："+value[i].stepExpectedResult
                                        +' \n ';
                                }
                                return'<span class="table_w" title="'+title+'">'+value[0].stepDescription+'</span>';
                            }
                            return "";
                        }
                    },{
                        field : "expectResult",
                        title : "预期结果",
                        width: "20",
                        align : 'center',
                        formatter : function (value, row, index) {
                            return'<span class="table_w" title="'+isValueNull(value)+'">'+isValueNull(value)+'</span>';
                        }
                    },{
                        field : "caseActualResult",
                        title : "实际结果",
                        align : 'center',
                        formatter : function (value, row, index) {
                            return "<input class='form-control table_w' readonly  id='actual_list_edit"+row.setCaseId+"' value='"+isValueNull(value)+"' "+
                            		"ondblclick='testSetCase.edit.change_result_status(this,"+row.setCaseId+")' title='"+isValueNull(value)+"' >";
                        }
                    },{
                        field : "caseExecuteResult",
                        title : "执行状态",
                        align : 'center',
                        width : "100px",
                        formatter : function (value, row, index) {
                            for( var j=0;j<$( "#exeExecuteState option" ).length;j++ ){
                                if( row.caseExecuteResult==$( "#exeExecuteState option" ).eq( j ).val() ){
                                    return  '<span  id="caseExecuteResult'+row.setCaseId+'" class="spanClass'+row.caseExecuteResult+'">'+$( "#exeExecuteState option" ).eq( j ).text()+'</span>';
                                }
                            }
                        }
                    },{
                        field : "opt",
                        title : "操作",
                        align : 'center',
                        class:'opt_handle',
                        formatter : function (value, row, index) {
                            var editTestCaseOpt =  "<a class='edit-opt' href='javascript:void(0);' onclick='testExecution.right_Opt.execution_edit_button.toTestCaseEdit("+ row.setCaseId +")'><img title='编辑' src='"+ execute.picPath.edit +"'></a>";
                            var executionResultOpt =  "<a class='edit-opt' href='javascript:void(0);' onclick='testExecution.right_Opt.executionTable.toTestCaseExecutionResult("+ row.testSetId +',\"'+row.caseNumber+'\",\"'+row.caseName+"\")'><img title='查看执行记录' src='"+execute.picPath.check+"'></a>";
                            var successOpt =  "<a class='edit-opt' href='javascript:void(0);' onclick='testExecution.right_Opt.executionTable.getAllExecuteCases("+execute.dicType.success+","+ row.setCaseId +")'><img title='执行成功' src='"+execute.picPath.success+"'></a>";
                            var failOpt =  "<a class='edit-opt' href='javascript:void(0);' onclick='testExecution.right_Opt.executionTable.getAllExecuteCases("+execute.dicType.fail+","+ row.setCaseId +")'><img title='执行失败' src='"+execute.picPath.fail+"'></a>";
                            var failBugOpt =  "<a class='edit-opt' href='javascript:void(0);' onclick='testExecution.right_Opt.executionTable.getAllExecuteCases("+execute.dicType.fail_bug+","+ row.setCaseId +")'><img title='执行失败并提交Bug' src='"+execute.picPath.fail_bug+"'></a>";
                            var hangUpOpt =  "<a class='edit-opt' href='javascript:void(0);' onclick='testExecution.right_Opt.executionTable.getAllExecuteCases("+execute.dicType.hangUp+","+ row.setCaseId +")'><img title='挂起' src='"+execute.picPath.hangUp+"'></a>";
                            var unmakeOpt =  "<a class='edit-opt' href='javascript:void(0);' onclick='testExecution.right_Opt.executionTable.getAllExecuteCases("+execute.dicType.unmake+","+ row.setCaseId +")'><img title='撤销' src='"+execute.picPath.unmake+"'></a>";
                            var opt = [];
                            if(permission.edit){
                                opt.push(editTestCaseOpt);
                            }
                            if(permission.success){
                                opt.push(successOpt);
                            }
                            if(permission.fail){
                                opt.push(failOpt);
                            }
                            if(permission.failBug){
                                opt.push(failBugOpt);
                            }
                            if(permission.hangUp){
                                opt.push(hangUpOpt);
                            }
                            if(permission.unmake){
                                opt.push(unmakeOpt);
                            }
                            if(row.caseExecuteResult != 1 && permission.executionResult == true){
                                opt.push(executionResultOpt);
                            }
                            return  opt.join("");
                        }
                    }],
                    onLoadSuccess:function(){
                        $("#loading").css('display','none');
                    },
                    onReorderRowsDrag:function (table, row) {
                        moveIndexTable.beforeIndex = $(row).attr("data-index");
                        return false;
                    },
                    onReorderRowsDrop:function(table, row){
                        moveIndexTable.laterIndex = $(row).attr("data-index");
                        if(moveIndexTable.beforeIndex != moveIndexTable.laterIndex){
                            moveIndexTable.id = $(row).attr("data-uniqueid");
                            testSetCase.order.moveIndexTable(moveIndexTable.id,parseInt(moveIndexTable.beforeIndex)+1,parseInt(moveIndexTable.laterIndex)+1);
                        }
                        return false;
                    },
                    onLoadError : errorFunMsg
                });
            },

            // 测试案例列表
            appendTable:function(This){
                var viewH = $(This).height();// 可见高度
                var contentH = $(This).get(0).scrollHeight;// 内容高度
                var scrollTop = $(This).scrollTop();// 滚动高度
                if (contentH - viewH - scrollTop <= 50) {
                    if(testSet.case_flag){
                        testSet.case_flag = false;
                        if( Number($( This ).attr("page"))*testSet.casePageSize < Number($( This ).attr("total"))){
                            $( This ).attr("page" , Number( $( This ).attr("page") )+1 );
                            $("#loading").css('display','block');
                            $.ajax({
                                url:"/testManage/testExecution/getTestCaseExecute",
                                method:"post",
                                data:{
                                    testSetMap :JSON.stringify({
                                        testSetId :  testSet.id,
                                        caseExecuteResultList :  $("#exeExecuteState").val() ? $("#exeExecuteState").val().join(",") : "",
                                        pageNumber:$("#executionTable_div").attr("page"),
                                        pageSize:testSet.casePageSize
                                    })
                                },
                                success:function(data){
                                    if (data.status == 2){
                                        layer.alert(data.errorMessage, {
                                            icon: 2,
                                            title: "提示信息"
                                        });
                                    } else if(data.status == 1){

                                        $("#span_case_total").text(data.total);
                                        testSet.case_show_num = testSet.case_show_num + data.rows.length;
                                        $("#span_case_show").text(testSet.case_show_num);
                                        $("#span_case_remain").text(Number(data.total) -  Number(testSet.case_show_num));

                                        $("#executionTable_div").attr("total",data.total);
                                        $("#executionTable").bootstrapTable("append",data.rows);
                                    }
                                    testSet.case_flag = true;
                                    previous_case = +new Date();// 执行函数后，马上记录当前时间
                                    $("#loading").css('display','none');
                                },
                                error:errorFunMsg
                            })
                        }
                    }
                } else {
                    testSet.case_flag = true;
                }
            },

            // 测试执行页面数据回显
            getAllExecuteCases : function( type,testCaseId ){
                if(type == true){
                    // 测试执行页面
                    editReturn = 2;
                    modalType ='new';
                    testExecution.resetFunction.executeCaseReset();
                    $( ".rightCaseDivNew" ).css( "display","block" ); //显示右边的案例div
                    $(".right").css("display","block");
                }
                $("#loading").css('display','block');
                $.ajax({
                    url:"/testManage/testExecution/getTestCaseWithStepAndRemarkById",
                    method:"post",
                    data:{
                        testCaseId:testCaseId,
                        testSetId:testSet.id
                    },
                    success:function(data){
                        if (data.status == 2){
                            layer.alert(data.errorMessage, {
                                icon: 2,
                                title: "提示信息"
                            });
                        } else if(data.status == 1){
                            // 回显
                            var obj = data.testSetCase;
                            testSet.testCaseId = testCaseId;
                            testSet.testSetCase = {};
                            testSet.testSetCase = obj;
                            if(type == true){
                                $("#loading").css('display','none');
                                // 测试执行页面
                                $( ".rightCaseDivNew .right_div_titFont" ).html( obj.caseNumber + " " + obj.caseName + "  " + executeResult)   //修改右边案例的标题
                                $("#newCasePrecondition" ).text( obj.casePrecondition );
                                $("#check_new_CaseDescription").text( obj.caseDescription );
                                $("#expectResult").html(obj.expectResult);
                                $("#check_caseActualResult").html(obj.caseActualResult);
                                $("#inputData").html(obj.inputData);
                                $( "#testPoint").html(obj.testPoint);
                                $( "#moduleName").html(obj.moduleName);
                                $( "#businessType").html(obj.businessType);
                                $( "#check_systemName").html(obj.systemName);

                                // 案例步骤
                                testExecution.right_Opt.executionTable.CaseStepsTable(data.testSetCaseStep);

                                // 备注
                                if(data.checkExcuteRemark != null){
                                    for( var i=0;i<data.checkExcuteRemark.length;i++ ){
                                        $("#checkExcuteRemark2").append("<div class='oneHistory'><span class='fontWeihgt'>执行人:</span>"+data.checkExcuteRemark[i].executeUserName+"  "+data.checkExcuteRemark[i].createDate+"<br>"
                                            +"<span class='fontWeihgt'>执行结果:</span>"+data.checkExcuteRemark[i].executeResult+"<br>"
                                            +"<span class='fontWeihgt'>备注:</span>"+data.checkExcuteRemark[i].excuteRemark+"<br></div>");
                                    }
                                }
                            } else{
                                // 测试集案例列表操作
                                testSet.editFiles = "";
                                testSet.newTableData = "";
                                testSet.excuteRemark = "";
                                if(data.testSetCaseStep.length > 0){
                                    for (var i = 0; i < data.testSetCaseStep.length; i++) {
                                        data.testSetCaseStep[i].stepExecuteResult = type==0 ||type==4 || type==5 ?3:type
                                    }
                                }
                                testSet.newTableData = JSON.stringify( data.testSetCaseStep );
                                testExecution.right_Opt.execution_edit_button.getTestCaseStep(type,false,testCaseId);
                            }
                        }
                    },
                    error:errorFunMsg
                })
            },

            // 测试步骤数据列表显示
            CaseStepsTable : function( data ){
                $("#CaseSteps").bootstrapTable('destroy');
                $("#CaseSteps").bootstrapTable({
                    queryParamsType: "",
                    pagination: false,
                    data: data,
                    sidePagination: "server",
                    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                    columns : [{
                        field : "id",
                        title : "id",
                        visible :  false,
                        align : 'center'
                    },{
                        field : "stepOrder",
                        title : "步骤",
                        align : 'center',
                        width : 50,
                    },{
                        field : "stepDescription",
                        title : "步骤描述",
                        width : 300,
                        align : 'center'
                    },{
                        field : "stepExpectedResult",
                        title : "预期结果",
                        align : 'center'
                    },{
                        field : "stepExecuteResult",
                        title : "执行结果",
                        align : 'center',
                        width : 100,
                        class : "stepExecuteResult",
                        formatter : function(value, row, index){
                            var str='<div class="def_tableDiv"><select class="selectpicker executionSelect" name="envSelect" >';
                            str+=' </select></div>';
                            return str;
                        }
                    },{
                        field : "stepActualResult",
                        title : "执行实际情况",
                        align : 'center',
                        class : "stepActualResult",
                        formatter : function(value, row, index){
                            var str='<div class="def_tableDiv2"><input class="form-control def_tableInput showBlock"  type="text" value=""></div>';
                            return str;
                        }
                    }],
                    onLoadSuccess:function(){

                    },
                    onLoadError : function(){
                    }
                });
                $( ".def_tableDiv .executionSelect" ).append( $("#executionSelect").find("*").clone(true) );
                $("#CaseSteps .selectpicker").selectpicker('refresh');
            },

            // 跳转测试案例执行记录页面
            toTestCaseExecutionResult : function (testSetId,caseNumber,caseName){
                layer.open({
                    type: 2,
                    area: ['900px', '80%'],
                    fixed: false,
                    shadeClose: false,
                    title:  '<span>测试案例历史执行记录' + ' | ' +caseNumber + ' | ' + caseName + '</span>',
                    content:  '/testManageui/testExecution/toTestCaseExecutionResult?testSetId='+testSetId +"&caseNumber="+caseNumber,
                    maxmin: true,
                    btn: '关闭' ,
                    btnAlign: 'c',
                    yes: function(){
                        layer.closeAll();
                    }
                });
            },

            // 批量通过按钮操作
            batchPass : function (){
                var data=$("#executionTable").bootstrapTable('getSelections');
                if(data.length == 0){
                    layer.alert("请选择执行案例",{icon : 6});
                    return;
                }
                var setCaseId_arr = [];
                for( var i=0;i<data.length;i++ ){
                    data[i].excuteRound = $( "#exeExcuteRound" ).val();
                    setCaseId_arr.push(data[i].setCaseId);
                }
                $("#loading").css('display','block');
                $.ajax({
                    url : "/testManage/testExecution/updateTestCaseExecute",
                    method:"post",
                    data:{
                        "allExecuteDate": JSON.stringify( data )
                    },
                    success:function(data){
                        if( data.status == 1 ){
                            setCaseId_arr.map(function(val){                             
                                $('#caseExecuteResult' + val).parent().html('<span  id="caseExecuteResult' + val +
                                '" class="spanClass2">Pass</span>');  
                            })
                            layer.alert("执行成功",{icon : 1});
                            // $("#exeExecuteState").selectpicker('val', '');
                            // testExecution.right_Opt.executionTable.initExecutionTable();
                        }else{
                            layer.alert("执行失败",{icon : 2});
                        }
                        $("#loading").css('display','none');
                    },
                    error:errorFunMsg
                });
            },

            // 新增，复制，删除，导入，导出，关联案例，引入测试集案例，归档，排序 在testSetCaseCommonOpt.js
        },

        // 测试案例执行按钮操作
        execution_edit_button : {

            // 进入编辑页面
            toTestCaseEdit : function (testCaseId){
                testSet.testCaseId = "";
                testSet.testCaseId = testCaseId;
                editReturn = 1;
                testSetCase.edit.editCaseBtn();
            },

            // 测试案例执行操作 五大按钮
            getTestCaseStep : function ( type,flag,testCaseId ){
                if(flag == undefined){
                    testSet.editFiles = "";
                    testSet.newTableData = "";
                    testSet.excuteRemark = "";
                    // 测试执行页面
                    var tableData = $("#CaseSteps").bootstrapTable('getData');
                    var newTableData = [];
                    for( var i=0;i<tableData.length;i++ ){
                        var rows = tableData[i];
                        rows.stepExecuteResult=$("#CaseSteps>tbody>tr .stepExecuteResult").eq( i ).find("select").val();
                        rows.stepActualResult=$("#CaseSteps>tbody>tr .stepActualResult").eq( i ).find(".def_tableInput").val();
                        newTableData.push( rows );
                    }
                    testSet.newTableData = JSON.stringify( newTableData );
                    testSet.editFiles = $("#files").val();
                    testSet.excuteRemark = $("#excuteRemark").val();
                    // 执行成功
                    if(type == 2){
                        for(var j=0;j<tableData.length;j++){
                            if( tableData[j].stepExecuteResult == 3){
                                layer.alert("案例步骤有失败步骤,不能通过",{icon : 2});
                                return false;
                            }
                        }
                    }
                }

                if(type != 0){
                    $("#loading").css('display','block');
                    $.ajax({
                        url:"/testManage/testExecution/updateTestSetCaseAndInsertExecute",
                        method:"post",
                        data:{
                            id:testSet.testCaseId,
                            testSetId:testSet.id,
                            excuteRound:caseRows.excuteRound,
                            caseExecuteResult:type,
                            enforcementResults:testSet.newTableData,
                            files:testSet.editFiles,
                            excuteRemark: testSet.excuteRemark
                        },
                        success:function(data){
                            if( data.status == 1 ){
                                for( var j=0;j<$( "#exeExecuteState option" ).length;j++ ){
                                    if( type == $( "#exeExecuteState option" ).eq( j ).val() ){
                                        $('#caseExecuteResult' + testCaseId).parent().html('<span  id="caseExecuteResult'+testCaseId+'" class="spanClass'+ type +'">'+$( "#exeExecuteState option" ).eq( j ).text()+'</span>');
                                    }
                                }
                                layer.alert("操作成功",{icon : 1});
                                if(flag == undefined){
                                    testExecution.resetFunction.returnBlackList();
                                }
                                // testExecution.right_Opt.executionTable.initExecutionTable();
                            } else {
                                layer.alert("操作失败",{icon : 2});
                            }
                            $("#loading").css('display','none');
                        },
                        error:errorFunMsg
                    });
                } else {
                    $("#loading").css('display','none');
                    // 执行失败并提交Bug
                    layer.confirm("执行成功,确认要提交缺陷吗？",{
                        btn: ['确定','取消'], //按钮
                        title: "提示信息"
                    },function(){
                        $('#commitBug_type').val(type);
                        $('#commitBug_setCaseId').val(testCaseId);

                        testExecution.resetFunction.newDefect_reset();
                        defect.new_editor.txt.html("测试环境：<br>" + "数据库：<br>" + "测试工号：<br>" + "测试数据：<br>" + "测试步骤：<br>" + "预期结果：<br>" +
                            "实际结果：<br>" + "存在问题：<br>" + "根本原因描述："); //根本原因放到缺陷描述里
                        field.addField();
                        $("#new_windowName").attr('type','text');
                        $("#new_windowName").removeAttr("disabled");
                        $("#new_repairRound").val(1);
                        laydate.render({
                            show: true,
                            elem: '#closeTime',
                            type: 'datetime'
                        });
                        $("select[name='detectedSystemVersionId']").unbind('shown.bs.select');
                        $("select[name='detectedSystemVersionId']").on('shown.bs.select', function () {
                            systemVersionBindCreateOptions(this);
                        });
                        $("select[name='repairSystemVersionId']").unbind('shown.bs.select');
                        $("select[name='repairSystemVersionId']").on('shown.bs.select', function () {
                            systemVersionBindCreateOptions(this);
                        });
                        laydate.render({
                            show: true,
                            elem: '#expectRepairDate',
                            position: 'abolute'
                        });

                        $("#testTaskId").val(testSet.testSetCase.taskId);
                        $("#testTaskName").val(isValueNull(testSet.testSetCase.taskName));
                        $("#testTaskName").attr("disabled","disabled");
                        $("#new_defectSource").val(testSet.testSetCase.testStage);
                        $("#new_defectSource").attr("disabled","disabled");
                        $("#new_requirementCode").val(testSet.testSetCase.requirementCode);
                        $("#new_commissioningWindowId").val(testSet.testSetCase.commissioningWindowId);
                        $("#systemId").val(testSet.testSetCase.systemId);
                        $("#system_Name").val(isValueNull(testSet.testSetCase.systemName));
                        $("#system_Name").attr("disabled","disabled");
                        $("#testcaseNumber").val(testSet.testSetCase.caseNumber);
                        $("#testCaseName").val(isValueNull(testSet.testSetCase.caseName));
                        $(".selectpicker").selectpicker('refresh');
                        $("#newDefectFrom").data('bootstrapValidator').destroy();
                        $('#newDefectFrom').data('bootstrapValidator', null);
                        testExecution.formValidator();
                        layer.closeAll('dialog');
                        $("#commitBug").modal( "show" );
                    },function(){
                        layer.closeAll('dialog');
                    })
                }
            },

            // 测试执行失败并提交bug
            defect:{
                // 选择项目
                select_project:function (){
                    fuzzy_search_radio({
                        'ele': 'newProjectName',
                        'url': '/testManage/testtask/getProjectListByProjectName',
                        'params': {
                            projectName: ''
                        },
                        'name': 'projectName',
                        'code': 'projectCode',
                        'id': 'id',
                        'top': '29px',
                        'dataName': 'projectInfoList',
                        'userId': $('#newProjectId'),
                        'out': true,
                        'not_jqgrid': true
                    });
                    $('#newProjectName_list').on('click', '.newProjectName_search_item', function () {
                        var val = $(this).text();
                        var id = $(this).data('id');
                        $('#newProjectId').val(id);
                        $('#newProjectName').val(val).attr('username',val).change();
                        $('#newProjectName_list').hide();
                        if($(this).data('type') == 2){    //新建类
                            $('.is_new_project').hide();
                            $("#new_windowId").val('');
                            $("#new_windowName").val('');
                            $("#new_windowName").attr('type','hidden');
                        }else{
                            $("#new_windowName").attr('type','text');
                            $('.is_new_project').show();
                        }
                        $('#newDefectFrom').bootstrapValidator('resetForm');
                        testExecution.formValidator();
                    })
                },
                // 提交缺陷
                submitDefect:function () {
                    $('#newDefectFrom').data('bootstrapValidator').validate();
                    if (!$('#newDefectFrom').data('bootstrapValidator').isValid()) {
                        return false;
                    }
                    var assignUserId = $("#new_assignUserId").val();
                    if (assignUserId == '') {
                        layer.alert("必须选择指派人！", {
                            icon : 2,
                            title : "提示信息"
                        })
                    } else {
                        testExecution.right_Opt.execution_edit_button.defect.updateDefectStatus(2);
                    }
                },

                // 暂存缺陷
                stagDefect : function () {
                    $('#newDefectFrom').data('bootstrapValidator').validate();
                    if (!$('#newDefectFrom').data('bootstrapValidator').isValid()) {
                        return false;
                    }
                    testExecution.right_Opt.execution_edit_button.defect.updateDefectStatus(1);
                },

                // 新增 编辑 缺陷 后台
                updateDefectStatus:function (defectStatus) {
                    var files = new FormData();
                    if(defectFormFileList.length > 0 ){
                        var filesSize = 0;
                        for (var i = 0,len2 = defectFormFileList.length;i < len2;i++){
                            filesSize += defectFormFileList[i].size;
                            files.append("defectFiles",defectFormFileList[i]);
                        }

                        if(filesSize > 1048576000){
                            layer.alert('文件太大,请删选！！！', {
                                icon: 2,
                                title: "提示信息"
                            });
                            return false;
                        }

                    }
                    $("#loading").css('display', 'block');
                    $("#new_defectSource").removeAttr("disabled");
                    var obj = {};
                    obj.projectId = $("#newProjectId").val();
                    obj.assignUserId = $("#new_assignUserId").val();
                    obj.testTaskId = $("#testTaskId").val();
                    obj.requirementCode = $("#new_requirementCode").val();
                    obj.systemId = $("#systemId").val();
                    obj.defectSummary = htmlEncodeJQ($.trim($("#new_defectSummary").val()));
                    obj.repairRound = htmlEncodeJQ($.trim($("#new_repairRound").val()));
                    obj.defectOverview = htmlEncodeJQ(defect.new_editor.txt.html());
                    obj.defectType = $.trim($("#new_defectType").find("option:selected").val());
                    obj.defectSource = $.trim($("#new_defectSource").find("option:selected").val());
                    obj.severityLevel = $.trim($("#new_severityLevel").find("option:selected").val());
                    obj.emergencyLevel = $.trim($("#new_emergencyLevel").find("option:selected").val());
                    obj.defectStatus = defectStatus;
                    obj.fieldTemplate = field.getFieldData("canEditField");
                    obj.testUserId = $("#new_testUserId").val();
                    obj.developUserId = $("#new_developerId").val();
                    obj.remark = $("#new_remark").val();
                    obj.commissioningWindowId = $("#new_windowId").val();
                    obj.projectGroupId = '';
                    obj.closeTime = '';
                    obj.assetSystemTreeId = '';
                    obj.detectedSystemVersionId = '';
                    obj.repairSystemVersionId = '';
                    obj.expectRepairDate = '';
                    obj.estimateWorkload = '';
                    obj.rootCauseAnalysis = '';

                    for (var i = 0; i < obj.fieldTemplate.field.length; i++) {
                        if (obj.fieldTemplate.field[i].required == "false") {
                            if ((defectStatus != 1 ) && (obj.fieldTemplate.field[i].valueName == "" || obj.fieldTemplate.field[i].valueName == null || obj.fieldTemplate.field[i].valueName == undefined )) {
                                $("#loading").css('display', 'none');
                                layer.alert(obj.fieldTemplate.field[i].labelName + "不能为空", {
                                    icon: 2,
                                    title: "提示信息"
                                });

                                return;
                            }
                        }
                    }
                    files.append("defectInfo",JSON.stringify(obj));

                    // 测试案例执行
                    files.append("testSetCase",JSON.stringify({
                        id:testSet.testCaseId,
                        testSetId:testSet.id,
                        excuteRound:caseRows.excuteRound,
                        caseExecuteResult:3
                    }));
                    files.append("enforcementResults",testSet.newTableData);
                    files.append("testSetCaseFiles",testSet.editFiles);
                    files.append("excuteRemark",testSet.excuteRemark);
                    // 新增缺陷
                    $.ajax({
                        type: "post",
                        url : '/zuul/testManage/testExecution/insertDefectWithxecution',
                        dataType: "json",
                        data:files,
                        cache: false,
                        processData: false,
                        contentType: false,
                        success: function(data) {
                            $("#loading").css('display','none');
                            if (data.status == 2){
                                layer.alert(data.errorMessage, {
                                    icon: 2,
                                    title: "提示信息"
                                });
                            } else if(data.status == 1){
                                var type = $('#commitBug_type').val();
                                var testCaseId = $('#commitBug_setCaseId').val();
                                for( var j = 0;j < $( "#exeExecuteState option" ).length;j ++ ){
                                    if( type == $( "#exeExecuteState option" ).eq( j ).val() ){
                                        $('#caseExecuteResult' + testCaseId).parent().html('<span  id="caseExecuteResult'+testCaseId+
                                        '" class="spanClass'+ type +'">'+$( "#exeExecuteState option" ).eq( j ).text()+'</span>');
                                    }
                                }
                                layer.alert('操作成功！', {
                                    icon: 1,
                                    title: "提示信息"
                                });

                                $("#commitBug").modal("hide");
                                $( ".rightCaseDivNew" ).css( "display","none" );
                                formFileList = [];
                                editAttList = [];
                                // testExecution.right_Opt.executionTable.initExecutionTable();
                            }
                        },
                        error:errorFunMsg
                    })

                }
            }
        }
    },
    //测试执行上面部分操作
    top_Opt:{
        //挂起
        handSelect:function () {
            var data = $("#executionTable").bootstrapTable('getSelections'),_orderCase=[]
            console.log(data);
            for (let i=0;i<data.length;i++){
                _orderCase.push(data[i].orderCase)
            }
            $('#executionTable').bootstrapTable('uncheckBy', {field: 'orderCase', values:_orderCase});
        },
        //撤销
        revokeSelect:function () {
            var data = $("#executionTable").bootstrapTable('getSelections');
            console.log(data);
        },
        //失败
        failSelect:function () {
            var data = $("#executionTable").bootstrapTable('getSelections');
            console.log(data);
        },
    },

    // 清空操作
    resetFunction : {
        // 搜索重置
        reset:function (){
            $("#checkTaskName").val("");
            $("#checkTestSetName").val("");
            $("#executeResult").val("");
            $("#taskTree").attr("page",1);
            $("#taskTree").attr("total","");
            $(".selectpicker").selectpicker('refresh');
            if( !$("#onlyDoing").hasClass("def_Selection") ){
                $("#onlyDoing").addClass("def_Selection")
            }
            if( !$("#onlyDoing2").hasClass("def_Selection") ){
                $("#onlyDoing2").addClass("def_Selection")
            }
        },

        // 测试执行页面清空
        executeCaseReset:function (){
            testSet.testCaseId ="";
            $( ".rightCaseDivNew .right_div_titFont" ).html( '' );
            $(".def_tableDiv2 .def_tableInput").val('');
            $( "#newCasePrecondition" ).html( '' );
            $( "#check_new_CaseDescription" ).html( '' );
            $( "#check_caseActualResult" ).html( '' );
            $( "#expectResult").html( '' );
            $( "#check_systemName").html( '' );
            $( "#inputData").html( '' );
            $( "#testPoint").html( '' );
            $( "#moduleName").html( '' );
            $( "#businessType").html( '' );
            $("#checkExcuteRemark2").empty();
            $( ".rightCaseDivCheck" ).css( "display","none" );
            $( ".right_div" ).css( "display","none" );
            $( ".rightCaseDivEdit" ).css( "display","none" );
            $( ".rightCaseDivNew" ).css( "display","none" );
            $("#newUpfileTable tbody").empty();
            $("#excuteRemark").val('');
            $("#files").val('');
            _files = [];
        },

        // 新建缺陷清空操作
        newDefect_reset:function (){
            $(".selectpicker").selectpicker('refresh');
            /*新建的字段*/
            $("#testTaskId").val('');
            $("#testTaskName").val('');
            $("#testTaskName").attr("disabled", false);
            $("#testTaskName").removeClass("pointStyle");
            $("#systemId").val('');
            $("#systemCode").val('');
            $("#system_Name").val('');
            $("#testCaseId").val('');
            $("#new_caseNumber").val('');
            $("#testCaseName").val('');
            $("#new_repairRound").val('');
            $("#new_defectSummary").val('');
            $("#new_defectType").selectpicker('val', '');
            $("#new_defectSource").selectpicker('val', '');
            $("#new_defectSource").attr("disabled", false);
            $("#new_severityLevel").selectpicker('val', '');
            $("#new_emergencyLevel").selectpicker('val', '');
            $("#new_assignUserId").val('');
            $("#new_assignUserName").val('');
            $("#newFileTable tbody").html("");
            $("#system_Name").attr("disabled", false);
            $("#system_Name").removeClass("pointStyle");
            $("#new_requirementCode").val('');
            $("#new_commissioningWindowId").val('');
            $("#new_testUserId").val('');
            $("#new_testUserName").val('');
            $("#new_remark").val('');
            $("#new_windowId").val('');
            $("#new_windowName").val('');
            $("#projectGroupId").val('');
            $("#projectGroupId").attr("idValue", "");
            $("#closeTime").val('');
            $("#assetSystemTreeId").val('');
            $("#assetSystemTreeId").attr("idValue", "");
            $("#detectedSystemVersionId").selectpicker('val', '');
            $("#repairSystemVersionId").selectpicker('val', '');
            $("#expectRepairDate").val('');
            $("#estimateWorkload").val('');
            $('#newProjectId').val('');
            $('#newProjectName').val('').attr('username','').change();
            search_radio_clear('newProjectName',$("#newProjectId"));
            $("#canEditField").empty();
            $("#new_windowName").attr('type','text');
            $('.is_new_project').show();
            // 清空定义数据
            defectFormFileList = [];
            defect.new_editor.txt.clear();
        },

        // 执行页面取消返回测试集案例列表
        returnBlackList:function(){
            $(".rightCaseDivNew").css("display","none");
            $(".batchExecutivePage").css("display","block");
        },

        // 编辑页面返回
        editReturnBlack:function(){
            $(".rightCaseDivEdit").css("display","none");
            // 列表编辑进入
            if(editReturn == 1){
                $(".batchExecutivePage").css("display","block");
            } else {
                // 执行页面进入
                $(".rightCaseDivNew").css("display","block");
            }
        },

        // 投产窗口弹框清空
        windowModal:function(){
            select_rows = new Array();
            $("#win_windowName").val('');
            $("#timeDate").val('');
            $("#win_windowType").selectpicker('val', '');
            $("#comWindowModal .color1 .btn_clear").css("display", "none");
        }
    },

    // 弹框操作
    modal:{
        // 人员弹框
        user: function (param){
            layer.open({
                type: 2,
                area: ['900px', '80%'],
                fixed: false,
                btnAlign: 'c',
                shadeClose: true,
                resize: false,
                title: "选择人员",
                anim: 'up',
                maxmin: true,
                content: "/systemui/user/toUserModal",
                btn: ["确定", "取消"],
                yes: function(index, layero){
                    var userSelectRows = window[layero.find('iframe')[0]["name"]].modal.selectRows;
                    if (typeof(userSelectRows) == undefined || userSelectRows.length == 0) {
                        parent.layer.alert("请至少选择一条数据", {
                            icon: 0,
                            title: "提示信息"
                        });
                        return false;
                    } else {
                        var id = userSelectRows[0].id;
                        var userName = userSelectRows[0].userName;
                        if(param == "new"){
                            $("#new_assignUserId").val(id);
                            $("#new_assignUserName").val(userName);
                        } else if(param == "new2"){
                            $("#new_testUserId").val(id);
                            $("#new_testUserName").val(userName);
                        } else if(param == "new3"){
                            $("#new_developerId").val(id);
                            $("#new_developer").val(userName);
                        }
                        layer.close(index);
                    }
                },
                success: function (layero, index) {
                    var notWithUserID = "";
                    if(param == "new"){
                        notWithUserID = $("#new_assignUserId").val();
                    } else if(param == "new2"){
                        notWithUserID = $("#new_testUserId").val();
                    } else if(param == "new3"){
                        notWithUserID = $("#new_developerId").val();
                    }

                    var iframeWindow = window[layero.find('iframe')[0]["name"]];
                    iframeWindow.userModalOpt.url = "/system/user/getAllUserModal";
                    iframeWindow.userModalOpt.singleValue = true;
                    iframeWindow.userModalOpt.paramDatas = {systemId:$("#systemId").val(),notWithUserID: notWithUserID};
                    iframeWindow.userTableShow();
                }
            });
        },

        // 投产窗口
        window: {
            send: function(){
                testExecution.resetFunction.windowModal();
                $("#userbutton2_1").css("display", "inline-block");
                $("#userbutton2").css("display", "none");
                $("#userbutton1").css("display", "none");
                $("#userbutton3").css("display", "none");
                ComWindowShow(true);
                $("#comWindowModal").modal("show");
            },

            commit: function () {
                var rowData = $("#ComWindowTable").bootstrapTable('getSelections');
                if (typeof(rowData) == 'undefined' || rowData.length == 0) {
                    layer.alert("请选择一条数据", {
                        icon: 2,
                        title: "提示信息"
                    });
                    return false;
                } else {
                    $("#new_windowId").val(rowData[0].id);
                    $("#new_windowName").val(rowData[0].windowName).change();
                    $("#comWindowModal").modal("hide");
                }
            }
        }


    },

    // vm中的操作
    vmOpt:{
        down :function (This){
            if( $(This).hasClass("fa-angle-double-down") ){
                $(This).removeClass("fa-angle-double-down");
                $(This).addClass("fa-angle-double-up");
                $(This).parents('.allInfo').children(".def_content").slideDown(100);
            }else {
                $(This).addClass("fa-angle-double-down");
                $(This).removeClass("fa-angle-double-up");
                $(This).parents('.allInfo').children(".def_content").slideUp(100);
            }
        }
    },

    // 表单验证
    formValidator:function(){

        // 新建缺陷
        $('#newDefectFrom').bootstrapValidator({
            excluded: [':disabled',':hidden'],
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            live: 'enabled',
            fields: {
                newProjectName: {
                    trigger: "change",
                    validators: {
                        notEmpty: {
                            message: '关联项目不能为空！'
                        },
                    }
                },
                system_Name: {
                    trigger: "change",
                    validators: {
                        notEmpty: {
                            message: '所属系统不能为空！'
                        },
                    }
                },
                defectSummary: {
                    validators: {
                        notEmpty: {
                            message: '缺陷摘要不能为空！'
                        },
                    }
                },
                new_windowName: {
                    trigger: "change",
                    validators: {
                        notEmpty: {
                            message: '投产窗口不能为空'
                        },
                    }
                },
                repairRound: {
                    validators: {
                        notEmpty: {
                            message: '请输入大于0的正数！'
                        },
                        regexp: {
                            regexp: /^\d+(\.\d{0,2})?$/,
                            message: '请输入大于0的正数！'
                        },
                        greaterThan: {
                            value: 1,
                            message: '请输入大于0的正数！'
                        },
                        lessThan: {
                            message: '请输入大于0的正数！'
                        }
                    }
                },
                defectType: {
                    validators: {
                        notEmpty: {
                            message: '缺陷类型不能为空！'
                        },
                        callback: {
                            message: function () {
                                return '';
                            },
                            callback: function (value, validator) {
                                if (value == '') {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    }
                },
                defectSource: {
                    validators: {
                        notEmpty: {
                            message: '缺陷来源不能为空！'
                        },
                        callback: {
                            message: function () {
                                return '';
                            },
                            callback: function (value, validator) {
                                if (value == '') {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    }
                },
                severityLevel: {
                    validators: {
                        notEmpty: {
                            message: '严重级别不能为空！'
                        },
                        callback: {
                            message: function () {
                                return '';
                            },
                            callback: function (value, validator) {
                                if (value == '') {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    }
                },
                emergencyLevel: {
                    validators: {
                        notEmpty: {
                            message: '紧急程度不能为空！'
                        },
                        callback: {
                            message: function () {
                                return '';
                            },
                            callback: function (value, validator) {
                                if (value == '') {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        });
    },

    // 重构表单验证
    refactorFormValidator:function(){
        $('#commitBug').on('hidden.bs.modal', function() {
            $('#newDefectFrom').bootstrapValidator('resetForm');
        });
    }
    
};


