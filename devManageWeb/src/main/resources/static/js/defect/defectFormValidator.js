/**
 * Description:表单验证
 * Author:liushan
 * Date: 2018/12/25 下午 3:38
 */

/**
*@author liushan
*@Description 表单验证
*@Date 2020/8/11
*@return
**/
function formValidator(){
    // 新建
    $('#newDefectFrom').bootstrapValidator({
        excluded : [ ':disabled' ],
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        live : 'enabled',
        fields : {
            defectSummary : {
                validators : {
                    notEmpty:{
                        message: '缺陷摘要不能为空！'
                    }
                }
            },
            repairRound : {
                validators : {
                    notEmpty:{
                        message: '请输入大于0的正数！'
                    },
                    regexp: {
                        regexp: /^\d+(\.\d{0,2})?$/,
                        message: '请输入大于0的正数！'
                    },
                    greaterThan:{
                        value:1,
                        message: '请输入大于0的正数！'
                    },
                    integer:{
                        message: '请不要输入奇怪的数字！'
                    },
                    lessThan:{
                        message: '请输入大于0的正数！'
                    }
                }
            },
            defectType : {
                validators : {
                    notEmpty : {
                        message: '缺陷类型不能为空！'
                    },
                    callback : {
                        message:function(){
                            return '';
                        },
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            defectSource : {
                validators : {
                    notEmpty : {
                        message: '缺陷来源不能为空！'
                    },
                    callback : {
                        message:function(){
                            return '';
                        },
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            severityLevel : {
                validators : {
                    notEmpty : {
                        message: '严重级别不能为空！'
                    },
                    callback : {
                        message:function(){
                            return '';
                        },
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            emergencyLevel : {
                validators : {
                    notEmpty : {
                        message: '紧急程度不能为空！'
                    },
                    callback : {
                        message:function(){
                            return '';
                        },
                        callback : function(value, validator) {
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

    // 编辑
    $('#edit_DefectFrom').bootstrapValidator({
        excluded : [ ':disabled' ],
        message: '不能为空！',//通用的验证失败消息
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        live : 'enabled',
        fields : {
            defectSummary : {
                validators : {
                    notEmpty : {
                        message: '缺陷摘要不能为空！'
                    },
                }
            },
            repairRound : {
                validators : {
                    notEmpty:{
                        message: '请输入大于0的正数！'
                    },
                    regexp: {
                        regexp: /^\d+(\.\d{0,2})?$/,
                        message: '请输入大于0的正数！'
                    },
                    greaterThan:{
                        value:1,
                        message: '请输入大于0的正数！'
                    },
                    integer:{
                        message: '请不要输入奇怪的数字！'
                    },
                    lessThan:{
                        message: '请输入大于0的正数！'
                    }
                }
            },
            defectType : {
                validators : {
                    notEmpty : {
                        message: '缺陷类型不能为空！'
                    },
                    callback : {
                        message:function(){
                            return '';
                        },
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            defectStatus : {
                validators : {
                    notEmpty : {
                        message: '缺陷状态不能为空！'
                    },
                    callback : {
                        message:function(){
                            return '';
                        },
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            defectSource : {
                validators : {
                    notEmpty : {
                        message: '缺陷来源不能为空！'
                    },
                    callback : {
                        message:function(){
                            return '';
                        },
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            severityLevel : {
                validators : {
                    notEmpty : {
                        message: '严重级别不能为空！'
                    },
                    callback : {
                        message:function(){
                            return '';
                        },
                        callback : function(value, validator) {
                            if (value == '') {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            },
            emergencyLevel : {
                validators : {
                    notEmpty : {
                        message: '紧急程度不能为空！'
                    },
                    callback : {
                        message:function(){
                            return '';
                        },
                        callback : function(value, validator) {
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

    $("#rejectDivForm").bootstrapValidator({
        excluded : [ ':disabled','hidden' ],
//        message: '输入不合法!',//通用的验证失败消息
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        live : 'enabled',
        fields : {
            workNum : {
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
            }
        }
    });

    $('#new_WorkTaskForm').bootstrapValidator({
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
                    	 regexp:/^500$|^[0](\.[\d]+)$|^([1-9]|[1-9]\d)(\.\d+)*$|^([1-9]|[1-9]\d|[1-4]\d\d)(\.\d+)*$/,
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

        }
    });
}

/**
 * 重构表单验证
 */
function refactorFormValidator(){
    $('#newDefect').on('hidden.bs.modal', function() {
        $("#newDefectFrom").data('bootstrapValidator').destroy();
        $('#newDefectFrom').data('bootstrapValidator', null);
        formValidator();
    });

    $('#editDefect').on('hidden.bs.modal', function() {
        $("#edit_DefectFrom").data('bootstrapValidator').destroy();
        $('#edit_DefectFrom').data('bootstrapValidator', null);
        formValidator();
    });

    $("#rejectDiv").on('hidden.bs.modal', function() {
        $("#rejectDivForm").data('bootstrapValidator').destroy();
        $('#rejectDivForm').data('bootstrapValidator', null);
        formValidator();
    });

    $('#new_WorkTaskModal').on('hidden.bs.modal', function() {
        $("#new_WorkTaskForm").data('bootstrapValidator').destroy();
        $('#new_WorkTaskForm').data('bootstrapValidator', null);
        formValidator();
    });
}