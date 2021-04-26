/**
 * Description:测试集案例公共操作
 * 新增，复制，删除，导入，导出，关联案例，引入测试集案例，归档，排序
 * Author:liushan
 * Date: 2020/4/7 上午 9:29
 */

var testSetCase_param = {
    caseType:1, //案例类型前台删除，数据库不能为空，保留传参数据
    param_list:"", // 列表查询条件
    new_edit_system:"" // 判断是新增new还是编辑edit 系统弹框
};

// 移动表格排序
var moveIndexTable = {
    id:"",
    moveIndex:"",
    beforeIndex:"",
    laterIndex:""
};

var editReturn;

var archivedCaseNumbers = [];//已归档的案例编号 根据编号来判断
$(document).ready(function () {

    // 新增
    $("#systemName").click(function(){
        testSetCase.resetFunction.clearSystem();
        select_rows = new Array();
        testSetCase_param.new_edit_system = "new";
        systemTableShow(true); // modal.js
        $("#systemModal").modal('show');
    });

    // 编辑
    $("#editSystemName").click(function(){
        testSetCase.resetFunction.clearSystem();
        select_rows = new Array();
        testSetCase_param.new_edit_system = "edit";
        systemTableShow(true); // modal.js
        $("#systemModal").modal('show');
    });
    
    $("#submit_caseActualResult").on('dblclick',function(){
    	testSetCase.edit.change_result();
    });

    testSetCase.formValidator();
    testSetCase.refactorFormValidator();
});


var testSetCase = {

    // 新建操作
    insert :{
        // 创建新增弹框案例步骤
        createAddTable:function (){
            var data = [];
            $("#newCaseSteps").bootstrapTable('destroy');
            $("#newCaseSteps").bootstrapTable({
                queryParamsType:"",
                data:data,
                columns : [
                    {
                        field : "stepOrder",
                        title : "步骤",
                        align : 'center',
                        class : "stepOrder",
                        width : '50px',
                    },{
                        field : "stepDescription",
                        title : "步骤描述",
                        align : 'center',
                        formatter : function (value, row, index) {
                            return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock"></textarea></div>';
                        }
                    },{
                        field : "stepExpectedResult",
                        title : "预期结果",
                        align : 'center',
                        formatter : function (value, row, index) {
                            return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock"></textarea></div>';
                        }
                    },{
                        field : "操作",
                        title : "操作",
                        align : 'center',
                        width : 210,
                        formatter : function (value, row, index) {
                            var str='<a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.addEditTableRow( this )">增加下一步骤 </a> | '+
                                '<a class="a_style defColor" style="cursor:pointer" onclick="testSetCase.insert.delEditTableRow(this)">删除</a> | '+
                                '<a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.upTableRow(this)">上移</a> | '+
                                '<a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.downTableRow(this)">下移</a>';
                            return str;
                        }
                    }],
                onLoadSuccess:function(){
                },
                onLoadError : function(){
                }
            });
            if( data.length==0 ){
                $("#newCaseSteps>tbody").empty();
            }
        },

        //获取关联系统
        getRelateSystem:function (){
            $("#loading").css('display','block');
            $.ajax({
                type: "post",
                url: "/testManage/testSet/getRelateSystem",
                data: {testSetId : testSet.id},
                dataType: "json",
                success: function(data) {
                    if(data.status == 2){
                        layer.alert("操作失败",{icon : 2});
                    }else{
                        $("#systemId").val(data.row.id);
                        $("#systemName").val(data.row.systemName);
                    }
                    $("#loading").css('display','none');
                }
            });
        },

        // 新增步骤表格
        addStep_btn:function (){
            var i= $("#newCaseSteps>tbody>tr").length + 1;
            var str='<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px;line-height:84px; "><a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="testSetCase.insert.delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.downTableRow(this)">下移</a></td> </tr>';
            $("#newCaseSteps").append(str);
        },

        addSteps:function (){
            var i= $("#newCaseSteps>tbody>tr").length + 1;
            var str='<tr><td class="hideCaseID" style="text-align: center;line-height:84px;"></td><td class="stepOrder" style="text-align: center; width: 50px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px;line-height:84px; "><a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="testSetCase.insert.delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.downTableRow(this)">下移</a></td> </tr>';
            $("#newCaseSteps>tbody").append( str );
            testSetCase.insert.changeStepOrder();
        },

        // 增加下一步骤
        addEditTableRow:function ( This ){
            var i= $("#newCaseSteps>tbody>tr").length + 1;
            var str='<tr><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px; line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="testSetCase.insert.delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.insert.downTableRow(this)">下移</a></td> </tr>';
            $( This ).parent().parent().after( str );
            testSetCase.insert.changeStepOrder();
        },

        // 删除
        delEditTableRow : function ( This ) {
            $( This ).parent().parent().remove();
            testSetCase.insert.changeStepOrder();
        },

        // 上移
        upTableRow:function ( This ){
            var $tr = $(This).parents("tr");
            if ($tr.index() == 0){
                alert("首行数据不可上移");
            }else{
                $tr.fadeOut(200).fadeIn(100).prev().fadeOut(100).fadeIn(100);
                $tr.prev().before($tr);
                testSetCase.insert.changeStepOrder();
            }
        },

        // 下移
        downTableRow:function ( This ){
            var $tr = $(This).parents("tr");
            if ( $tr.index() == $( "#newCaseSteps>tbody>tr" ).length - 1) {
                alert("最后一条数据不可下移");
            }else {
                $tr.fadeOut(200).fadeIn(100).next().fadeOut(100).fadeIn(100);
                $tr.next().after($tr);
                testSetCase.insert.changeStepOrder();
            }
        },

        //动态改变表格步骤值
        changeStepOrder:function (){
            for( var i=0;i<$("#newCaseSteps>tbody>tr").length ;i++){
                $( "#newCaseSteps>tbody>tr" ).eq( i ).find( ".stepOrder" ).text( ( i + 1) );
            }
        },

        // 新增 或 编辑 系统赋值
        commitSys:function (){
            var selectContent = $("#systemTable").bootstrapTable('getSelections')[0];
            if(typeof(selectContent) == 'undefined') {
                layer.alert('请选择一列数据！', {icon: 0});
                return false;
            }else{
                if(testSetCase_param.new_edit_system == "new"){
                    $("#systemId").val(selectContent.id);
                    $("#systemName").val(selectContent.systemName);
                } else if(testSetCase_param.new_edit_system == "edit"){
                    $("#editSystemName").val(selectContent.systemName);
                    $("#editSystemName").attr( 'idValue',selectContent.id);
                }
            }
            $("#systemModal").modal('hide');
        },

        // 新增按钮操作
        newPass : function (){
            testSetCase.resetFunction.newReset();
            testSetCase.insert.createAddTable();
            testSetCase.insert.getRelateSystem();
//            testSetCase.formValidator();
            $("#newTestCaseModal").modal('show');
        },

        //保存新建页面信息
        submitTestCase:function (status){
            var testCaseStep = [];
//            $("#newTestCaseForm").data('bootstrapValidator').destroy();
//            testSetCase.formValidator();
            $('#newTestCaseForm').data('bootstrapValidator').validate();
            if(!$('#newTestCaseForm').data('bootstrapValidator').isValid()){
                return ;
            }

            var data;
            if(status == 1){     //测试集新增
                for( var i=0;i<$( "#addCaseSteps>tbody>tr" ).length;i++ ){
                    var obj={}
                    obj.stepOrder=$( "#addCaseSteps>tbody>tr" ).eq( i ).find( ".stepOrder" ).text();
                    obj.stepDescription=$( "#addCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(0).val();
                    if(obj.stepDescription == ""){
                        layer.alert("第"+(i+1)+"行步骤描述为空",{icon : 0});
                        return ;
                    }
                    obj.stepExpectedResult=$( "#addCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(1).val();
                    if(obj.stepExpectedResult == ""){
                        layer.alert("第"+(i+1)+"行预期结果为空",{icon : 0});
                        return ;
                    }
                    testCaseStep.push( obj );
                }
            	data = JSON.stringify({
            		'testSetId': testSetId,
            		'systemId': $( "#systemId" ).val(),
            		'caseName': $( "#new_caseName" ).val(),
            		'caseType':$("#caseType").val(),
            		'casePrecondition': $( "#new_precodition" ).val(),
                    'expectResult':$('#new_expectResult').val(),
                    'inputData':$('#new_inputData').val(),
                    'testPoint':$('#new_testPoint').val(),
                    'moduleName':$('#new_moduleName').val(),
                    'businessType':$('#new_businessType').val(),
                    'caseDescription':$('#new_CaseDescription').val()
            	});
            }else{         //执行新增
                for( var i=0;i<$( "#newCaseSteps>tbody>tr" ).length;i++ ){
                    var obj={}
                    obj.stepOrder=$( "#newCaseSteps>tbody>tr" ).eq( i ).find( ".stepOrder" ).text();
                    obj.stepDescription=$( "#newCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(0).val();
                    if(obj.stepDescription == ""){
                        layer.alert("第"+(i+1)+"行步骤描述为空",{icon : 0});
                        return ;
                    }
                    obj.stepExpectedResult=$( "#newCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(1).val();
                    if(obj.stepExpectedResult == ""){
                        layer.alert("第"+(i+1)+"行预期结果为空",{icon : 0});
                        return ;
                    }
                    testCaseStep.push( obj );
                }
            	data = JSON.stringify({
                    'testSetId': testSet.id,
                    'systemId': $( "#systemId" ).val(),
                    'caseName': htmlEncodeJQ($.trim($( "#new_caseName" ).val())),
                    'caseType':testSetCase_param.caseType,
                    'casePrecondition': htmlEncodeJQ($.trim($( "#new_precodition" ).val())),
                    'expectResult':htmlEncodeJQ($.trim($('#new_expectResult').val())),
                    'inputData':htmlEncodeJQ($.trim($('#new_inputData').val())),
                    'testPoint':htmlEncodeJQ($.trim($('#new_testPoint').val())),
                    'moduleName':htmlEncodeJQ($.trim($('#new_moduleName').val())),
                    'businessType':htmlEncodeJQ($.trim($('#new_businessType').val())),
                    'caseDescription':htmlEncodeJQ($.trim($('#new_CaseDescription').val())),
                    'caseActualResult':htmlEncodeJQ($.trim($('#new_caseActualResult').val()))
                });
            }
            
            $("#loading").css('display','block');
            $.ajax({
                url : "/testManage/testSet/addTestCase",
                method:"post",
                data:{
                    testSetCaseStr : data,
                    testSetCaseStepStr : JSON.stringify(testCaseStep)
                },
                success:function(data){
                    if(data.status == 2){
                        layer.alert("添加失败",{icon : 2});
                    }else{
                        layer.alert("添加成功",{icon : 1});
                        $("#newTestCaseModal").modal("hide");
                        // 刷新表格
                    	testExecution.right_Opt.executionTable.initExecutionTable();
                    }
                    $("#loading").css('display','none');
                },
                error:function(){

                }
            });
        }
    },

    // 编辑操作
    edit : {

        // 编辑页面数据回显
        editCaseBtn : function (){
            testSetCase.resetFunction.editReset();
            $("#loading").css('display','block');
            $(".right_blank").css("display","none");
            $(".right").css("display","block");
            $.ajax({
                url : "/testManage/testExecution/getUpdateCase",
                method:"post",
                data:{
                    "testSetCaseId" : testSet.testCaseId
                },
                success:function(data){
                    if( data.status == 2 ){
                        layer.alert("查询失败",{icon : 2});
                    } else {
                        //根据返回参数打印到页面上
//                        $("#editForm").data('bootstrapValidator').destroy();
//                        testSetCase.formValidator();
                        $("#editHideId").val( testSet.testCaseId );//关联任务
                        $("#caseNumber").val( data.CASE_NUMBER );//案例编号
                        $("#editTestTaskName").attr( 'idValue',data.TEST_TASK_ID );//关联任务
                        $("#editTestTaskName").val( isValueNull(data.TEST_TASK_NAME) );
                        $("#editSystemName").attr( 'idValue',data.SYSTEM_ID );//涉及系统
                        $("#editSystemName").val( isValueNull(data.SYSTEM_NAME) );
                        $("#editCaseName").val(isValueNull( data.CASE_NAME) );//案例名称
                        $("#edit_CaseDescription").val( isValueNull(data.CASE_DESCRIPTION) );
                        $("#editCasePrecondition").val( isValueNull(data.CASE_PRECONDITION) ); //前置条件
                        $("#edit_expectResult").val(isValueNull(data.expectResult));
                        $("#edit_inputData").val(isValueNull(data.inputData));
                        $("#edit_testPoint").val(isValueNull(data.testPoint));
                        $("#edit_moduleName").val(isValueNull(data.moduleName));
                        $("#edit_businessType").val(isValueNull(data.businessType));
                        $("#edit_caseActualResult").val(isValueNull(data.caseActualResult));
                        //形成表格数据
                        testSetCase.edit.createEditTable( data.caseStep );
                        testSetCase.resetFunction.closeSee();
                        $( ".rightCaseDivEdit" ).css( "display","block" ); //显示右边的案例div
                    }
                    $("#loading").css('display','none');
                },
                error:errorFunMsg
            });
        },

        // 测试步骤
        createEditTable : function ( data ){
            $("#editCaseSteps").bootstrapTable('destroy');
            $("#editCaseSteps").bootstrapTable({
                queryParamsType:"",
                pagination : false,
                data:data,
                columns : [{
                    field : "id",
                    title : "id",
                    class :  "hideCaseID",
                    align : 'center'
                },{
                    field : "stepOrder",
                    title : "步骤",
                    align : 'center',
                    class : "stepOrder",
                    width : '50px',
                },{
                    field : "stepDescription",
                    title : "步骤描述",
                    align : 'center',
                    formatter : function (value, row, index) {
                        return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock">'+isValueNull(row.stepDescription)+'</textarea></div>';
                    }
                },{
                    field : "stepExpectedResult",
                    title : "预期结果",
                    align : 'center',
                    formatter : function (value, row, index) {
                        return '<div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock">'+isValueNull(row.stepExpectedResult)+'</textarea></div>';
                    }
                },{
                    field : "操作",
                    title : "操作",
                    align : 'center',
                    class : "handleBtn",
                    width : '210px',
                    formatter : function (value, row, index) {
                        var str='<a class="a_style" style="cursor:pointer" onclick="testSetCase.edit.addEditTableRow( this )">增加下一步骤 </a> | '+
                            '<a class="a_style defColor" style="cursor:pointer" onclick="testSetCase.edit.delEditTableRow(this)">删除</a> | '+
                            '<a class="a_style" style="cursor:pointer" onclick="testSetCase.edit.upTableRow(this)">上移</a> | '+
                            '<a class="a_style" style="cursor:pointer" onclick="testSetCase.edit.downTableRow(this)">下移</a>';
                        return str;
                    }
                }],
                onLoadSuccess:function(){
                },
                onLoadError : function(){
                }
            });
            if( data.length != undefined && data.length==0 ){
                $("#editCaseSteps>tbody").empty();
            }
        },

        // 添加测试案例步骤
        addSteps : function (){
            var i= $("#editCaseSteps>tbody>tr").length + 1;
            var str='<tr><td class="hideCaseID" style="text-align: center;"></td><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px;line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="testSetCase.edit.addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="testSetCase.edit.delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.edit.upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.edit.downTableRow(this)">下移</a></td> </tr>';
            $("#editCaseSteps>tbody").append( str );
            testSetCase.edit.changeStepOrder();
        },

        // 增加下一步骤
        addEditTableRow : function ( This ){
            var i= $("#editCaseSteps>tbody>tr").length + 1;
            var str='<tr><td class="hideCaseID" style="text-align: center;"></td><td class="stepOrder" style="text-align: center; width: 50px;line-height:84px;">'+ i +'</td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; "><div class="def_tableDiv2"><textarea  style="resize:none;" class="form-control def_tableInput showBlock" value=""></textarea></div></td> <td style="text-align: center; width: 210px;line-height:84px;"><a class="a_style" style="cursor:pointer" onclick="testSetCase.edit.addEditTableRow( this )">增加下一步骤 </a> | <a class="a_style defColor" style="cursor:pointer" onclick="testSetCase.edit.delEditTableRow(this)">删除</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.edit.upTableRow(this)">上移</a> | <a class="a_style" style="cursor:pointer" onclick="testSetCase.edit.downTableRow(this)">下移</a></td> </tr>';
            $( This ).parent().parent().after( str );
            testSetCase.edit.changeStepOrder();
        },

        // 动态改变表格步骤值
        changeStepOrder : function (){
            for( var i=0;i<$("#editCaseSteps>tbody>tr").length ;i++){
                $( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".stepOrder" ).text( ( i + 1) );
            }
        },

        // 删除测试案例步骤
        delEditTableRow : function ( This ) {
            $( This ).parent().parent().remove();
            testSetCase.edit.changeStepOrder();
        },

        // 测试案例步骤上移
        upTableRow : function( This ){
            var $tr = $(This).parents("tr");
            if ($tr.index() == 0){
                alert("首行数据不可上移");
            }else{
                $tr.fadeOut(200).fadeIn(100).prev().fadeOut(100).fadeIn(100);
                $tr.prev().before($tr);
                testSetCase.edit.changeStepOrder();
            }
        },

        // 测试案例步骤下移
        downTableRow : function ( This ){
            var $tr = $(This).parents("tr");
            if ( $tr.index() == $( "#editCaseSteps>tbody>tr" ).length - 1) {
                alert("最后一条数据不可下移");
            }else {
                $tr.fadeOut(200).fadeIn(100).next().fadeOut(100).fadeIn(100);
                $tr.next().after($tr);
                testSetCase.edit.changeStepOrder();
            }
        },

        // 编辑页面 提交按钮
        UpdateCaseStep : function (){
            $('#editForm').data('bootstrapValidator').validate();
            if( !$('#editForm').data("bootstrapValidator").isValid() ){
                return;
            }
            var testCaseStep = [];
            for( var i=0;i<$( "#editCaseSteps>tbody>tr" ).length;i++ ){
                var obj={}
                obj.id=$( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".hideCaseID" ).text();
                obj.stepOrder=$( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".stepOrder" ).text();
                if( $( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(0).val()=='' ){
                    layer.alert("表格数据不能为空，请检查",{icon : 0});
                    return;
                }else{
                    obj.stepDescription=$( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(0).val();
                }
                if( $( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(1).val()=='' ){
                    layer.alert("表格数据不能为空，请检查",{icon : 0});
                    return;
                }else{
                    obj.stepExpectedResult=$( "#editCaseSteps>tbody>tr" ).eq( i ).find( ".def_tableInput" ).eq(1).val();
                }
                testCaseStep.push( obj );
            }

            testCaseStep=JSON.stringify(testCaseStep);
            var data = JSON.stringify({
                'id':testSet.testCaseId,
                'testTaskId': $( "#editTestTaskName" ).attr( 'idvalue' ),
                'systemId': $( "#editSystemName" ).attr( 'idvalue' ),
                'caseName': htmlEncodeJQ($.trim($( "#editCaseName" ).val())),
                'caseNumber': $( "#caseNumber" ).val(),
                'casePrecondition':htmlEncodeJQ($.trim($( "#editCasePrecondition" ).val())),
                'expectResult':htmlEncodeJQ($.trim($('#edit_expectResult').val())),
                'inputData':htmlEncodeJQ($.trim($('#edit_inputData').val())),
                'testPoint':htmlEncodeJQ($.trim($('#edit_testPoint').val())),
                'moduleName':htmlEncodeJQ($.trim($('#edit_moduleName').val())),
                'businessType':htmlEncodeJQ($.trim($('#edit_businessType').val())),
                caseDescription:htmlEncodeJQ($.trim($('#edit_CaseDescription').val())),
                caseActualResult:htmlEncodeJQ($.trim($('#edit_caseActualResult').val()))
            }) ;

            var param = {
                testSetCase:data,
                testCaseStep:testCaseStep
            };
            testSetCase.edit.updateCaseWithStep(param);
        },

        // 列表改变实际结果状态
        change_result_status:function (This,setCaseId){
//            $(This).removeAttr('readonly');
//            $(This).attr('status',1);
            $('#caseActualResult_setCaseId').val(setCaseId);
            $('#caseActualResult_ipt').val('');
            $('#caseActualResult_ipt').val($(This).val());
            $('#caseActualResultModal').modal('show');
        },

        //实际结果提交
        change_result:function (){
        	var _val = $('#caseActualResult_ipt').val();
//            if($.trim(_val)){
                editReturn = 1;
                var data = JSON.stringify({
                    'id':$('#caseActualResult_setCaseId').val(),
                    caseActual:1, // 判断是不是只修改实际结果
                    caseActualResult:htmlEncodeJQ($.trim(_val)) // 实际结果
                }) ; // 列表获取的数据 格式
                var param = {
                    testSetCase:data,
                    testCaseStep:""
                };
                testSetCase.edit.updateCaseWithStep(param);
//            }
//            $(This).attr('readonly',true);
//            $(This).attr('status',2);
        },

        // 编辑后台接口
        updateCaseWithStep:function(param){
            $("#loading").css('display','block');
            $.ajax({
                url : "/testManage/testExecution/UpdateCaseStep",
                method:"post",
                data:param,
                success:function(data){
                    if( data.status == 1 ){
                        layer.alert("保存成功",{icon : 1});
                        $(".rightCaseDivEdit").css("display","none");
                        if(editReturn == 1){
                            $(".batchExecutivePage").css("display","block");
                            $("#editTestCaseModal").modal("hide");
                            $('#caseActualResultModal').modal('hide');
                            testExecution.right_Opt.executionTable.initExecutionTable();
                        } else {
                            // 执行页面进入
                            $(".rightCaseDivNew").css("display","block");
                            testExecution.right_Opt.executionTable.getAllExecuteCases(true,testSet.testCaseId);
                        }
                    } else {
                        layer.alert("保存失败",{icon : 2});
                        $("#loading").css('display','none');
                    }
                },
                error:errorFunMsg
            });
        }
    },

    // 复制操作
    copy:{
        copyPass : function (){
            var data=$("#executionTable").bootstrapTable('getSelections');
            var arr = [];
            if(data.length==0){
                layer.alert("请选择复制案例",{icon : 0});
                return;
            }
           
            for( var i=0;i<data.length;i++ ){
                arr.push( data[i].setCaseId );
            }
            $("#loading").css('display','block');
            $.ajax({
                url : "/testManage/testSet/copyInsertTestCases",
                method:"post",
                traditional: true,
                data:{
                    testSetId:testSet.id,
                    testCaseIds:arr
                },
                success:function(data){
                    if( data.status == 1 ){
                        layer.alert("执行成功",{icon : 1});
                        $("#exeExecuteState").selectpicker('val', '');
                        testExecution.right_Opt.executionTable.initExecutionTable();
                    }else{
                        layer.alert("执行失败",{icon : 2});
                        $("#loading").css('display','none');
                    }
                },
                error:errorFunMsg
            });
        },

    },

    // 批量移除案例按钮操作
    move:{
        movePass : function (){
            var data=$("#executionTable").bootstrapTable('getSelections');
            var ids = [],_ids=[];
            if(data.length==0){
                layer.alert("请选择移除案例",{icon : 6});
                return;
            }
            for( var i=0;i<data.length;i++ ){
                _ids.push(data[i].setCaseId)
                ids.push( {id:data[i].setCaseId,orderCase:data[i].orderCase} );
            }
            $("#loading").css('display','block');
            $.ajax({
                url: "/testManage/testSet/removeManyTestSetCase",
                method:"post",
                data: {
                    testSetId: testSet.id,
                    ids : JSON.stringify(ids)
                },
                success:function(data){
                    if( data.status == 1 ){
                        layer.alert("执行成功",{icon : 1});
                        $("#exeExecuteState").selectpicker('val', '');
                        //testExecution.right_Opt.executionTable.initExecutionTable(); //刷新数据
                      /*  for(var j = 0; j < ids.length;j++){
                            $('#executionTable').bootstrapTable('removeByUniqueId', ids[j].id);
                            if(j==ids.length-1){
                                testSetCase.updateTableOrderCaseRow(); //更新序号
                            }
                        }
                        removeByUniqueId 会有导致下一次select获取值为空的bug
                        */
                        var tableData=$("#executionTable").bootstrapTable('getData'),_tableData=[];
                        for (var i=0;i<tableData.length;i++){
                            if(_ids.indexOf(tableData[i].setCaseId)==-1){
                                var _item=tableData[i]
                                _item.orderCase=_tableData.length+1
                                _tableData.push(_item)
                            }
                        }
                        $("#executionTable").bootstrapTable('load',_tableData); //重新加载 消除多次后select不到的bug
                    } else {
                        layer.alert("执行失败",{icon : 2});
                    }
                    $("#loading").css('display','none');
                },
                error:errorFunMsg
            });
        },
    },

    // 排序操作
    order:{
        // 表格行拖动后台
        moveIndexTable :function (id,beforeIndex,laterIndex){
            $("#loading").css('display','block');
            $.ajax({
                url:"/testManage/testSet/moveIndexTestSetCase",
                method:"post",
                data:{
                    testSetId :  testSet.id,
                    testCaseId:id,
                    beforeIndex:beforeIndex,
                    laterIndex:laterIndex
                },
                success:function(data){
                    if (data.status == 2){
                        layer.alert(data.errorMessage, {
                            icon: 2,
                            title: "提示信息"
                        });
                    } else if(data.status == 1){
                       testSetCase.updateTableOrderCaseRow();
                    }
                    $("#loading").css('display','none');
                },
                error:errorFunMsg
            })
        },

        // 批量排序案例按钮操作
        orderPass : function (){
            var data=$("#executionTable").bootstrapTable('getSelections');
            if(data.length == 0){
                layer.alert("请选择需要排序的案例",{icon : 0});
                return;
            }
            layer.prompt({
                formType: 2,
                title:"请输入排序号",
                content:"<input type='number' min='1' max='100000' maxlength='11' value='' id='orderCaseNum'  name='orderCaseNum' class='form-control' /><span id='orderCase_span' style='display: none;color: red'>请输入正整数(1~100000)</span>",
                yes:function(index){
                    var val = $("#orderCaseNum").val();
                    if(isNaN(val) == true || val.indexOf("-") != -1 || val.indexOf(".") != -1
                        || val == 0 || val.length > 11 || val > 100000 || val == "" || val == undefined){
                        $("#orderCase_span").css("display","block");
                        return false;
                    } else{
                        $("#orderCase_span").css("display","none");
                    }
                    testSetCase.order.orderNumFun(data,val);
                    layer.close(index);
                }
            })
        },

        // 批量排序请求后台
        orderNumFun:function(data,orderNum){
            var testSetCaseList = [];
            for( var i=0;i<data.length;i++ ){
                if(data[i].orderCase == orderNum){
                    layer.alert("请填写未选中的排序号",{icon : 0});
                    return;
                }
                testSetCaseList.push( {id:data[i].setCaseId,orderCase:data[i].orderCase} );
            }
            $("#loading").css('display','block');
            $.ajax({
                url: "/testManage/testSet/moveTestSetCase",
                method:"post",
                contentType:"application/json",
                data: JSON.stringify({
                    testSetCases : JSON.stringify(testSetCaseList),
                    testSetId :  testSet.id,
                    laterIndex:orderNum
                }),
                success:function(data){
                    if( data.status == 1 ){
                        layer.alert("执行成功",{icon : 1});
                        testExecution.right_Opt.executionTable.initExecutionTable();
                    } else {
                        layer.alert("执行失败",{icon : 2});
                        $("#loading").css('display','none');
                    }
                },
                error:errorFunMsg
            });
        }
    },

    // 关联案例 见/testManageWeb/src/main/resources/static/js/testSet/testSetPupop.js
    
    // 引入测试集案例 见/testManageWeb/src/main/resources/static/js/testSet/testSetPupop.js

    // 归档
     archivedCase : {
    	archivedCaseCommit:function(){
    		testSetCase.archivedCase.findArchivedId();
	    	var rowDate = $("#executionTable").bootstrapTable('getSelections');
	    	if (rowDate.length == 0) {
	    		layer.alert('请至少选择一个案例进行归档!', {
	    			icon: 0
	    		});
	    		return;
	    	} else {
	    		var caseNumbers = [];
	    		var newCaseNumbers = [];
	    		$.each(rowDate,function(index,value){
	    			//var caseNumber = value.caseNumber.substring(value.caseNumber.indexOf(">")+1,value.caseNumber.indexOf("</a>"))
	    			var caseNumber = value.caseNumber;
	    			caseNumbers.push(caseNumber);
	    			newCaseNumbers.push(caseNumber);
	    		});
	    		
	    		for (var i = 0; i < caseNumbers.length; i++) {
	    			for (var j = 0; j < archivedCaseNumbers.length; j++) {
	    				if (caseNumbers[i] == archivedCaseNumbers[j]) {
	    					newCaseNumbers.splice(i,1,"");
	    				}
	    			}
	    		}
	    		
	    		newCaseNumbers.push(0);
	    		layer.alert('确认要对所选中的案例进行归档？', {
	    			icon: 0,
	    			btn: ['确定', '取消']
	    		}, function () {
	    			$.ajax({
	    				url: "/testManage/testCase/archivingCase",
	    				method: "post",
	    				data: {
	    					"ids": newCaseNumbers
	    				},
	    				success: function () {
	    					testExecution.right_Opt.executionTable.initExecutionTable();
	    					layer.alert('案例已归档!', {
	    						icon: 1
	    					})
	    				},
                        error:errorFunMsg
	    			});
	    		})
	    	}
    	},
    	//查询所有归档案例
    	findArchivedId:function () {
    		$.ajax({
    			url: "/testManage/testCase/getArchivedCaseIds",
    			method: "post",
    			async: false,
    			data: {
    			},
    			success: function (data) {
    				archivedCaseNumbers = data.data;
    			},
                error:errorFunMsg
    		});
    		
    	}
    },

    // 导入 操作
    importExcel:{
    	importModal:function(status){
    		$("#is_test_set").val(status);
    		if(status == 1){
    			$("input[name='caseCode']").removeAttr('checked');
    			$("#is_test_set_body").show();
    		}else{
    			$("#is_test_set_body").hide();
    		}
    		$("#importModal").modal("show");
    	},

        exportExcel:function (){
            window.location.href = "/testManage/testSet/exportTemplet";
        },

    	upload:function (){
			if(document.getElementById("upfile").files[0] == undefined){
                layer.alert("请选择文件",{icon : 0});
                return ;
            }
    		
    		var formData = new FormData();
    		formData.append("file", document.getElementById("upfile").files[0]);
    		formData.append("testSetId",testSet.id);
    		if($("#is_test_set").val() == 1){
    			var _val = $('input[name="caseCode"]:checked').val() || 0;
    			formData.append("type",_val);
    		}
    		

    		$("#loading").css('display','block');
    		$.ajax({
    	        url: "/zuul/testManage/testSet/importCase",
    	        type: "POST",
    	        data: formData,
    	        /**
    	        *必须false才会自动加上正确的Content-Type
    	        */
    	        contentType: false,
    	        /**
    	        * 必须false才会避开jQuery对 formdata 的默认处理
    	        * XMLHttpRequest会对 formdata 进行正确的处理
    	        */
    	        processData: false,
    	        success: function (data) {
    	           if(data.status == 2){
    	        	   layer.alert("导入失败，原因:"+data.errorMessage,{icon:2});
    	           }else{
    	        	   layer.alert("导入成功",{icon:1});
                       testExecution.right_Opt.executionTable.initExecutionTable();
    	        	   $("#importModal").modal("hide");
    	           }
    	           $("#loading").css('display','none');
    	        },
    	        error: errorFunMsg
    	    });
    	}
    },
    

    // 导出操作
    exportCase:function (){
        var data = $("#executionTable").bootstrapTable('getSelections');
        if(data.length == 0){
            // 导出列表展示
            if(testSetCase_param.param_list == ""){
                testSetCase_param.param_list = {
                    testSetMap :JSON.stringify({
                        testSetId :  testSet.id
                    })
                };
            }
        } else {
            // 导出选中的
            var caseIds = $.map(data, function (row) {
                return row.setCaseId
            });
            testSetCase_param.param_list = {
                testSetMap :JSON.stringify({
                    testSetId :  testSet.id,
                    caseIds :caseIds
                })
            };
        }
        createNewForm("/testManage/testExecution/exportTestSetCase",testSetCase_param.param_list);
        $("#").attr('disabled','disabled');
        setTimeout(function(){
            $("#").removeAttr('disabled');
        },3000)
    },

    updateTableOrderCaseRow:function(){
       /* var len = $("#executionTable").find("tr").length;
        for(var i=1;i<len;i++){
            $("#executionTable").bootstrapTable('updateRow', {index: i-1, row: {"orderCase":parseInt(i)}});
        }*/

        //  逐个修改很慢,获取数据全部替换
        var tableData=$("#executionTable").bootstrapTable('getData')||[];
        for (var i=0;i<tableData.length;i++){
            tableData[i].orderCase=i+1
        }
        $("#executionTable").bootstrapTable('load',tableData); //重新加载 消除多次后select不到的bug
    },

    // 清空操作
    resetFunction:{
        // 新增 或 编辑 系统弹框清除
        clearSystem:function (){
            $("#sel_systemName").val("");
            $("#sel_systemCode").val("");
            select_rows = new Array();
            testSetCase_param.new_edit_system = "";
        },

        // 清空新建弹框
        clearAddCase:function (){
//            $("#newTestCaseModal :text").val("");
//            $("#newTestCaseModal textarea").val("");
//            $("#newTestCaseForm").data('bootstrapValidator').destroy();
//            testSetCase.formValidator();
        },

        // 关闭页面
        closeSee : function (){
            $( ".rightCaseDivCheck" ).css( "display","none" );
            $( ".right_div" ).css( "display","none" );
            $( ".rightCaseDivEdit" ).css( "display","none" );
            $( ".rightCaseDivNew" ).css( "display","none" );
        },

        // 新建清空
        newReset:function(){
            $("#executeHiddenID").val('');
            $( "#systemId" ).val('');
            $( "#new_caseName" ).val('');
            $("#caseType").val('');
            $( "#new_precodition" ).val('');
            $('#new_expectResult').val('');
            $('#new_inputData').val('');
            $('#new_testPoint').val('');
            $('#new_moduleName').val('');
            $('#new_businessType').val('');
            $('#new_CaseDescription').val('');
            $('#new_caseActualResult').val('');
            $('#newCaseSteps tbody').empty();
        },

        // 编辑清空
        editReset:function () {
            $("#editHideId").val('' );
            $("#caseNumber").val( '' );
            $("#editTestTaskName").attr( 'idValue','' );
            $("#editTestTaskName").val( '' );
            $("#editSystemName").attr( 'idValue','' );
            $("#editSystemName").val( '' );
            $("#editCaseName").val( '' );
            $("#edit_CaseDescription").val( '' );
            $("#editCasePrecondition").val( '' );
            $("#edit_expectResult").val('');
            $("#edit_inputData").val('');
            $("#edit_testPoint").val('');
            $("#edit_moduleName").val('');
            $("#edit_businessType").val('');
            $("#edit_caseActualResult").val('');
            $('#editCaseSteps tbody').empty();
        }
    },
    //重构表单验证
    refactorFormValidator: function() {
	      $('#newTestCaseModal').on('hidden.bs.modal', function () {
		        $("#newTestCaseModal :text").val("");
		        $("#newTestCaseModal textarea").val("");
		          $("#newTestCaseForm").data('bootstrapValidator').destroy();
		          $('#newTestCaseForm').data('bootstrapValidator', null);
		          testSetCase.formValidator();
	      });
	      $('#editTestCaseModal').on('hidden.bs.modal', function () {
		    	   $("#editTestCaseModal :text").val("");
		    	   $("#editTestCaseModal textarea").val("");
		         $("#editForm").data('bootstrapValidator').destroy();
		         $('#editForm').data('bootstrapValidator', null);
		         testSetCase.formValidator();
	     });
    },
    // 表单验证
    formValidator:function (){

        // 新建表单验证
        $('#newTestCaseForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                systemName: {
                    validators: {
                        notEmpty: {
                            message: '涉及系统不能为空'
                        }
                    }
                },
                caseType: {
                    validators: {
                        notEmpty: {
                            message: '案例类型不能为空'
                        }
                    }
                },
                new_caseName: {
                    validators: {
                        notEmpty: {
                            message: '案例名称不能为空'
                        },
                        stringLength: {
                            max:100,
                            message: '案例名称长度必须小于100字符'
                        }
                    }
                }
            }
        }),

        // 编辑表单验证
        $('#editForm').bootstrapValidator({
            message: 'This value is not 1valid',
            feedbackIcons : {
                valid : 'glyphicon glyphicon-ok',
                invalid : 'glyphicon glyphicon-remove',
                validating : 'glyphicon glyphicon-refresh'
            },
            fields : {
                editTestTaskName : {
                    validators : {
                        notEmpty:{
                            message: "关联任务不能为空!"
                        }
                    }
                },
                editSystemName : {
                    validators : {
                        notEmpty: {
                            message: "涉及系统不能为空!"
                        }
                    }
                },
                editCaseName : {
                    validators : {
                        notEmpty:{
                            message: "案例名称不能为空!"
                        },
                        stringLength: {
                            max:100,
                            message: '案例名称长度必须小于100字符'
                        }
                    }
                }
            }
        })
    }
};




