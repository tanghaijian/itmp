/**
 * Description: 用户弹窗操作
 * Author:liushan
 * Date: 2019/6/12 上午 9:39
 */
var userModalOpt = {
    url:"", // 路径
    singleValue:true,  // 单选多选 默认单选，false 多选
    paramDatas:{} // 需要传到后台的参数
};

$(document).ready(function () {
    modal.checkSelectRows("#userTable");
    //搜索展开和收起
    downOrUpButton();
    // 所有的Input标签，在输入值后出现清空的按钮
    buttonClear();
    tableMouseover("#userTable");
    banEnterSearch();
});

/**
 * 用户弹窗操作
 * @param url 路径
 * @param singleValue 是否单选 false 多选 true 单选
 * @param paramDatas 请求后台参数
 */
function userTableShow(){
    var pageNumber = $('#userTable').bootstrapTable('getOptions').pageNumber;
    var pageSize = $('#userTable').bootstrapTable('getOptions').pageSize;
    $("#loading").css('display','block');
    $("#userTable").bootstrapTable('destroy');
    $("#userTable").bootstrapTable({
        url:userModalOpt.url,
        method:"post",
        contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        queryParamsType:"",
        pagination : true,
        sidePagination: "server",
        pageNumber : pageNumber==undefined?1:pageNumber,
        pageSize : pageSize==undefined?10:pageSize,
        pageList :[10, 20, 50, 100],
        singleSelect : userModalOpt.singleValue,//单选
        clickToSelect : true, //是否启用点击选中行
        maintainSelected : true,
        smartDisplay: false,
        queryParams : function(params) {
            var param = {
                pageNumber:params.pageNumber,
                pageSize:params.pageSize,
                userName:$.trim($("#userName").val()),
                deptName:$.trim($("#deptName").val()),
                companyName:$.trim($("#companyName").val())
            };
            Object.assign(param,userModalOpt.paramDatas);
            return param;
        },
        columns : [{
            checkbox : true,
            width : "30px",
            formatter:function(value,row,index){
                for( var i = 0, len = modal.selectRows.length; i < len; i++){
                    if(row.id.toString() == modal.selectRows[i].id.toString()){
                        return {
                            checked:true
                        }
                    }
                }
                return {
                    checked:false
                }

            }
        },{
            field : "id",
            title : "id",
            visible : false,
            
        },{
            field : "userName",
            title : "姓名",
            
        },{
            field : "userAccount",
            title : "用户名",
            
        },{
            field : "deptName",
            title : "所属部门",
            
        },{
            field : "companyName",
            title : "所属公司",
            
        }],
       onLoadSuccess:function(){

            $("#loading").css('display','none');
        },
        onLoadError:errorFunMsg,
        onPageChange:function(){
            $("#loading").css('display','block');
        }
    });
}

//重置
function userReset(){
    modal.selectRows = [];
    $("#userName").val('');
    $("#deptName").val('');
    $("#companyName").val('');
    $(".color1 .btn_clear").css("display","none");
    userTableShow();
}
