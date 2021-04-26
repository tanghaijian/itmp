/**
 * Description: 测试案例历史执行结果查看
 * Author:liushan
 * Date: 2020/3/19 下午 5:03
 */

var testCaseExecutionResult_Param = {
    testCaseId:"",
    testSetId:"",
    caseNumber:"",
    refreshtoType:"",
    caseExecuteResult_Dic:[]
};

$(document).ready(function () {

    if(testCaseExecutionResult_Param.refreshtoType == 1){
        // 测试案例执行记录页面
        testCaseExecutionResult.initExecutionTable();
    } else if(testCaseExecutionResult_Param.refreshtoType == 2){
        // 测试案例执行案例详细信息页面
        testCaseExecutionResult.getExecuteCaseDetails(testCaseExecutionResult_Param.testCaseId,testCaseExecutionResult_Param.testSetId);
    }
});

var testCaseExecutionResult = {

    // 测试执行记录列表
    initExecutionTable: function testCaseExecutionTable() {
        $("#loading").css('display','block');
        $("#testCaseExecutionTable").bootstrapTable('destroy');
        $("#testCaseExecutionTable").bootstrapTable({
            url: '/testManage/testExecution/getAllExecuteCase',
            method: "post",
            queryParamsType: "",
            pagination: true,
            sidePagination: "server",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            pageNumber: 1,
            pageSize: 10,
            pageList: [5, 10, 15],
            queryParams: function (params) {
                var param = {
                    testSetId: testCaseExecutionResult_Param.testSetId,
                    caseNumber:testCaseExecutionResult_Param.caseNumber,
                    pageNumber: params.pageNumber,
                    pageSize: params.pageSize
                };
                return param;
            },
            columns: [{
                field: "id",
                title: "id",
                visible: false,
                align: 'center'
            }, {
                field : "caseName",
                title : "案例名称",
                align : 'center' ,
                formatter : function (value, row, index) {
                    return "<a href='javascript:void(0);' class='edit-opt' onclick='testCaseExecutionResult.checkTestCase("+ row.id +',\"'+row.caseNumber+'\",\"'+row.caseName+'\",'+row.testSetId+")'>"+ value +"</a>";
                }
            },{
                field: "caseExecuteResult",
                title: "执行状态",
                align: 'center',
                width: "100px",
                formatter: function (value, row, index) {
                    for (var k = 0; k < testCaseExecutionResult_Param.caseExecuteResult_Dic.length; k++) {
                        if (value == testCaseExecutionResult_Param.caseExecuteResult_Dic[k][0]) {
                            return '<span class="spanClass' + value + '">' + testCaseExecutionResult_Param.caseExecuteResult_Dic[k][1] + '</span>';
                        }
                    }
                }
            }, {
                field: "ResultName",
                title: "执行人",
                align: 'center',
                formatter: function (value, row, index) {
                    return row.resultName + " | " + datFmt(new Date(row.lastUpdateDate), "yyyy-MM-dd hh:mm:ss");
                }
            }],
            onLoadSuccess: function () {
                $("#loading").css('display','none');
            },
            onLoadError: errorFunMsg
        });
    },

    // 查看测试执行案例详细信息
    checkTestCase : function (id,caseNumber,caseName,testSetId){
        parent.layer.open({
            type: 2,
            area: ['900px', '80%'],
            fixed: false,
            shadeClose: false,
            title:  '<span>测试案例执行详细页面'+ ' | ' +caseNumber + ' | ' + caseName + '</span>',
            content:  '/testManageui/testExecution/toCheckTestCaseExecution?testCaseId='+id+"&testSetId="+testSetId,
            maxmin: true,
            btn: '关闭' ,
            btnAlign: 'c',
            yes: function(index, layero){
                parent.layer.close(index);
            }
        });
    },

    // 测试执行单个测试案例信息
    getExecuteCaseDetails : function (  id,setId ){
        $(".right_blank").css("display","none");
        $(".right").css("display","block");
        $("#checkUpfileTable").empty();
        $("#loading").css('display','block');
        $.ajax({
            url : "/testManage/testExecution/getExecuteCaseDetails",
            method:"post",
            data:{
                "testSetCaseExecuteId":id,
                'testSetCaseId':setId
            },
            success:function(data){
                modalType ='seeFile';
                if( data.listCase.caseExecuteResult == 2 ){
                    $(".successBg").css( "display","inhert" );
                }else if( data.listCase.caseExecuteResult == 3 ){
                    $(".failBg").css( "display","inhert" );
                }
                $( "#checkNameAndTime" ).html( data.userName +" | "+ data.listCase.createDate );
                $( "#checkCasePrecondition" ).html( data.listCase.casePrecondition );
                $( "#checkExcuteRemark" ).html( data.listCase.excuteRemark );

                $( "#check_expectResult" ).html( data.listCase.expectResult );
                $( "#check_inputData" ).html( data.listCase.inputData );
                $( "#check_testPoint" ).html( data.listCase.testPoint );
                $( "#check_moduleName" ).html( data.listCase.moduleName );
                $( "#check_businessType" ).html( data.listCase.businessType );

                $( "#checkRelatedDefects" ).empty();
                for( var i=0;i<data.listDefect.length;i++ ){
                    var str='';
                    str+='<div class=rowdiv><div class="def_col_4">';

                    for( var j=0;j<$( "#relatedDefectsType option" ).length;j++ ){
                        if( data.listDefect[i].defectType==$( "#relatedDefectsType option" ).eq( j ).val() ){
                            str+='<span class="classColor'+data.listDefect[i].defectType+'">'+ $( "#relatedDefectsType option" ).eq( j ).text() +'</span>'+
                                '<span class="classColor'+data.listDefect[i].defectType+'">P'+ $( "#relatedDefectsType option" ).eq( j ).val() +'</span>';
                        }
                    }
                    str+=' </div><div class="def_col_6 defectCode" title="' + data.listDefect[i].defectCode +'">' + data.listDefect[i].defectCode +
                        '</div><div class="def_col_26 fontWrop">' + data.listDefect[i].defectSummary + '</div></div>';
                    $( "#checkRelatedDefects" ).append( str );
                }

                $("#checkCaseSteps").bootstrapTable('destroy');
                $("#checkCaseSteps").bootstrapTable({
                    queryParamsType:"",
                    pagination : false,
                    data:data.listStep,
                    sidePagination: "server",
                    contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
                    columns : [{
                        field : "id",
                        title : "id",
                        visible : false,
                        align : 'center'
                    },{
                        field : "stepOrder",
                        title : "步骤",
                        width:50,
                        align : 'center'
                    },{
                        field : "stepDescription",
                        title : "步骤描述",
                        width:300,
                        align : 'center'
                    },{
                        field : "stepExpectedResult",
                        title : "预期结果",
                        align : 'center'
                    },{
                        field : "stepExecuteResult",
                        title : "执行结果",
                        width:100,
                        align : 'center',
                        formatter : function (value, row, index) {
                            for (var k = 0; k < testCaseExecutionResult_Param.caseExecuteResult_Dic.length; k++) {
                                if (value == testCaseExecutionResult_Param.caseExecuteResult_Dic[k][0]) {
                                    return '<span class="spanClass' + value + '">' + testCaseExecutionResult_Param.caseExecuteResult_Dic[k][1] + '</span>';
                                }
                            }
                        }
                    },{
                        field : "stepActualResult",
                        title : "执行情况",
                        align : 'center'
                    }],
                    onLoadSuccess:function(){
                    },
                    onLoadError : function(){
                    }
                });
                if((data.listFile).length>0){
                    var _table=$("#checkUpfileTable");
                    var attMap=data.listFile;
                    for(var i=0 ;i<attMap.length;i++){
                        var _tr = '';
                        var file_name = attMap[i].fileNameOld;
                        var file_type = attMap[i].fileType;

                        var _td_name = '<span>'+file_name+'</span><i class = "file-bucket">'+attMap[i].fileS3Bucket+'</i><i class = "file-s3Key">'+attMap[i].fileS3Key+'</i></td>';
                        var _td_icon = filePicClassPath(file_type); // common.js
                        var row =  JSON.stringify( {fileS3Bucket:attMap[i].fileS3Bucket,fileS3Key:attMap[i].fileS3Key,fileNameOld:attMap[i].fileNameOld}).replace(/"/g, '&quot;');
                        _tr+='<tr><td><div class="fileTb" style="cursor:pointer" onclick ="testCaseExecutionResult.download('+row+')">'+_td_icon+" "+_td_name+'</tr>';
                        _table.append(_tr);
                    }
                }
                $( ".right_div" ).css( "display","none" ); //显示右边的案例div
                $( ".rightCaseDivCheck" ).css( "display","block" ); //显示右边的案例div
                $("#loading").css('display','none');
            },
            error:errorFunMsg
        });


    },

    // 下载文件
    download : function (row){
        var fileS3Bucket = row.fileS3Bucket;
        var fileS3Key = row.fileS3Key;
        var fileNameOld = row.fileNameOld;
        window.location.href = "/testManage/worktask/downloadFile?fileS3Bucket="+fileS3Bucket+"&fileS3Key="+fileS3Key+"&fileNameOld="+fileNameOld;
    }

};