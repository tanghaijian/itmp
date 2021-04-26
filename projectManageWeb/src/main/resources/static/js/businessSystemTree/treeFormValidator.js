/**
 * Description:表单验证
 * Author:liushan
 * Date: 2018/12/25 下午 3:38
 */

/**
 * 表单验证
 */
function formValidator(){
    $('#addModalForm').bootstrapValidator({
         excluded : [ ':disabled' ],
         feedbackIcons : {
             valid : 'glyphicon glyphicon-ok',
             invalid : 'glyphicon glyphicon-remove',
             validating : 'glyphicon glyphicon-refresh'
         },
         live : 'enabled',
         submitButtons:'button[type="button"]',
         fields : {
             businessTreeName: {
                 trigger: "change",
                 validators: {
                     notEmpty: {
                         message: '条目名称不能为空！'
                     },
                     callback:{
                         message: '条目名称最大输入50个字符！',
                         callback: function (value, validator, $field) {
                             if ($field.val().length <= 50) {
                                 return true;
                             }
                             return false;
                         }
                     }
                 }
             },
             businessTreeShortName: {
                 trigger: "change",
                 validators: {
                     callback:{
                         message: '条目简称最大输入50个字符！',
                         callback: function (value, validator, $field) {
                             if ($field.val().length <= 50) {
                                 return true;
                             }
                             return false;
                         }
                     }
                 }
             },
             businessTreeCode: {
                 validators: {
                     callback:{
                         message: '条目编号最大输入50个字符！',
                         callback: function (value, validator, $field) {
                             if ($field.val().length <= 50) {
                                 return true;
                             }
                             return false;
                         }
                     }
                 }
             },
             remark: {
                 validators: {
                     callback:{
                         message: '条目编号最大输入500个字符！',
                         callback: function (value, validator, $field) {
                             if ($field.val().length <= 500) {
                                 return true;
                             }
                             return false;
                         }
                     }
                 }
             }
         }
     });

    $('#addNextModalForm').bootstrapValidator({
        excluded : [ ':disabled' ],
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        live : 'enabled',
        fields : {
            businessTreeName: {
                trigger: "change",
                validators: {
                    notEmpty: {
                        message: '条目名称不能为空！'
                    },
                    callback:{
                        message: '条目名称最大输入50个字符！',
                        callback: function (value, validator, $field) {
                            if ($field.val().length <= 50) {
                                return true;
                            }
                            return false;
                        }
                    }
                }
            },
            businessTreeShortName: {
                trigger: "change",
                validators: {
                    callback:{
                        message: '条目简称最大输入50个字符！',
                        callback: function (value, validator, $field) {
                            if ($field.val().length <= 50) {
                                return true;
                            }
                            return false;
                        }
                    }
                }
            },
            businessTreeCode: {
                validators: {
                    callback:{
                        message: '条目编号最大输入50个字符！',
                        callback: function (value, validator, $field) {
                            if ($field.val().length <= 50) {
                                return true;
                            }
                            return false;
                        }
                    }
                }
            },
            remark: {
                validators: {
                    callback:{
                        message: '条目编号最大输入500个字符！',
                        callback: function (value, validator, $field) {
                            if ($field.val().length <= 500) {
                                return true;
                            }
                            return false;
                        }
                    }
                }
            }
        }
    });

}

/**
 * 重构表单验证
 */
function refactorFormValidator(){
    $('#addModal').on('hidden.bs.modal', function() {
        $("#addModalForm").data('bootstrapValidator').destroy();
        $('#addModalForm').data('bootstrapValidator', null);
        formValidator();
    });

    $('#addNextModal').on('hidden.bs.modal', function() {
        $("#addNextModalForm").data('bootstrapValidator').destroy();
        $('#addNextModalForm').data('bootstrapValidator', null);
        formValidator();
    });
}