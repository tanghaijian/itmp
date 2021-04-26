/**
 * Description:表单验证
 * Author:liushan
 * Date: 2018/12/25 下午 3:38
 */

/**
 * 表单验证
 */

function formValidator() {
    $('#newDefectFrom').bootstrapValidator({
        excluded: [':disabled'],
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
            new_assignUserName: {
                trigger: "change",
                validators: {
                    notEmpty: {
                        message: '主修复人不能为空！'
                    },
                }
            },
            defectSummary: {
                validators: {
                    notEmpty: {
                        message: '缺陷摘要不能为空！'
                    },
					stringLength: {
						min: 2,
						max: 100,
						message: '长度必须在2-100之间'
					}
                }
            },
//            new_windowName: {
//                trigger: "change",
//                validators: {
//                    notEmpty: {
//                        message: '投产窗口不能为空'
//                    },
//                }
//            },
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
            },
            estimateWorkload: {
                trigger: "blur keyup",
                validators: {
                    regexp: {
                        regexp: /^\d*\.{0,1}\d{0,1}$/,
                        message: '请输入一个非负数（若为小数，小数点后保留1位）'
                    }
                }
            }
        }
    });

    $('#edit_DefectFrom').bootstrapValidator({
        excluded: [':disabled', ':hidden', ':not(:visible)'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	editProjectName: {
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
					stringLength: {
						min: 2,
						max: 100,
						message: '长度必须在2-100之间'
					}
                }
            },
//            edit_windowName: {
//                trigger: "change",
//                validators: {
//                    notEmpty: {
//                        message: '投产窗口不能为空！'
//                    },
//                }
//            },
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
                    integer: {
                        message: '请不要输入奇怪的数字！'
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
            defectStatus: {
                validators: {
                    notEmpty: {
                        message: '缺陷状态不能为空！'
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
            },
            edit_estimateWorkload: {
                trigger: "blur keyup",
                validators: {
                    regexp: {
                        regexp: /^\d*\.{0,1}\d{0,1}$/,
                        message: '请输入一个非负数（若为小数，小数点后保留1位）'
                    }
                }
            }
        }
    });
}

/**
 * 重构表单验证
 */
function refactorFormValidator() {
    $('#newDefect').on('hidden.bs.modal', function () {
        $("#newDefectFrom").data('bootstrapValidator').destroy();
        $('#newDefectFrom').data('bootstrapValidator', null);
        formValidator();
    });

    $('#editDefect').on('hidden.bs.modal', function () {
        $("#edit_DefectFrom").data('bootstrapValidator').destroy();
        $('#edit_DefectFrom').data('bootstrapValidator', null);
        formValidator();
    })
    
    $('#systemModal').on('hidden.bs.modal', function () {
    	$("#sel_systemName").val('');
    	$("#sel_systemCode").val('');
    })
}