

function formValidator() {
	
	$('#HandleDev').bootstrapValidator({
		message: 'This value is not valid',//通用的验证失败消息
		feedbackIcons: {
			//根据验证结果显示的各种图标
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		live: 'enabled',
		fields: {
			taskQuantity: {
				validators: {
					notEmpty: {
						message: '实际工作量不能为空'
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
				trigger: "change",
				validators: {
					notEmpty: {
						message: '任务分派不能为空'
					},
				}
			},

		}
	});
}

//运维项目验证
function oac_FormValidator() {
	
	$('#addTest').bootstrapValidator({
		excluded: [":disabled"],
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
						min: 2,
						max: 100,
						message: '长度必须在2-100之间'
					}
				}
			},
			featureCode: {
				trigger: "change",
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
					stringLength: {
						min: 2,
						max: 500,
						message: '长度必须在2-500之间'
					}
				}
			},
			workLoad: {
				trigger: "change",
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
				trigger: "change",
				validators: {
					notEmpty: {
						message: '测试人员不能为空'
					},
				}
			},

			testStage: {
				validators: {
					notEmpty: {
						message: '测试阶段不能为空'
					},
				}
			},
			add_taskAssignUser: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '任务分配不能为空'
					},
				}
			},
		}
	});
	$('#editTestF').bootstrapValidator({
		excluded: [':disabled'],
		//message: 'This value is not valid',//通用的验证失败消息
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
						min: 2,
						max: 100,
						message: '长度必须在2-100之间'
					}
				}
			},
			edfeatureCode: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '关联开发任务不能为空'
					},
				}
			},
			etaskOverview: {
				validators: {
					notEmpty: {
						message: '任务描述不能为空'
					},
					stringLength: {
						min: 2,
						max: 500,
						message: '长度必须在2-500之间'
					}
				}
			},
			workTaskStatus: {
				validators: {
					notEmpty: {
						message: '任务状态不能为空'
					},
				}
			},
			edworkLoad: {
				trigger: "change",
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
			edtaskUser: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '测试人员不能为空'
					},
				}
			},
			edtestStage: {
				validators: {
					notEmpty: {
						message: '测试阶段不能为空'
					},
				}
			},
			edit_taskAssignUser: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '任务分配不能为空'
					},
				}
			},
			editStartDate:{
            	trigger:"change",
            	validators: {
            		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '开始时间必须小于结束日期！',
                        callback:function(value, validator,$field){
                        	if( value == "" ){
                        		return true;
                        	}else{
                        		if( $( "#editEndDate" ).val() == '' ){
                        			return true;
                        		}else{
                        			var start = new Date( value );
                        			var end = new Date( $( "#editEndDate" ).val() );
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
            editEndDate:{
            	trigger:"change",
            	validators: {
            		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '结束时间必须大于开始日期！',
                        callback:function(value, validator,$field){
                        	if( value == "" ){
                        		return true;
                        	}else{
                        		if( $( "#editStartDate" ).val() == '' ){
                        			return true;
                        		}else{
                        			var start = new Date( $( "#editStartDate" ).val() );
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

}

//新项目验证
function new_FormValidator() {
	
	$('#addTest').bootstrapValidator({
		excluded: [":disabled"],
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
						min: 2,
						max: 100,
						message: '长度必须在2-100之间'
					}
				}
			},
			featureCode: {
				trigger: "change",
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
					stringLength: {
						min: 2,
						max: 500,
						message: '长度必须在2-500之间'
					}
				}
			},
			workLoad: {
				trigger: "change",
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
				trigger: "change",
				validators: {
					notEmpty: {
						message: '测试人员不能为空'
					},
				}
			},
			add_taskAssignUser: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '任务分配不能为空'
					},
				}
			},
		}
	});
	$('#editTestF').bootstrapValidator({
		excluded: [':disabled'],
		//message: 'This value is not valid',//通用的验证失败消息
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
						min: 2,
						max: 100,
						message: '长度必须在2-100之间'
					}
				}
			},
			edfeatureCode: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '关联开发任务不能为空'
					},
				}
			},
			etaskOverview: {
				validators: {
					notEmpty: {
						message: '任务描述不能为空'
					},
					stringLength: {
						min: 2,
						max: 500,
						message: '长度必须在2-500之间'
					}
				}
			},
			workTaskStatus: {
				validators: {
					notEmpty: {
						message: '任务状态不能为空'
					},
				}
			},
			edworkLoad: {
				trigger: "change",
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
			edtaskUser: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '测试人员不能为空'
					},
				}
			},
			edit_taskAssignUser: {
				trigger: "change",
				validators: {
					notEmpty: {
						message: '任务分配不能为空'
					},
				}
			},
			editStartDate:{
            	trigger:"change",
            	validators: {
            		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '开始时间必须小于结束日期！',
                        callback:function(value, validator,$field){
                        	if( value == "" ){
                        		return true;
                        	}else{
                        		if( $( "#editEndDate" ).val() == '' ){
                        			return true;
                        		}else{
                        			var start = new Date( value );
                        			var end = new Date( $( "#editEndDate" ).val() );
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
            editEndDate:{
            	trigger:"change",
            	validators: {
            		callback:{/*自定义，可以在这里与其他输入项联动校验*/
                        message: '结束时间必须大于开始日期！',
                        callback:function(value, validator,$field){
                        	if( value == "" ){
                        		return true;
                        	}else{
                        		if( $( "#editStartDate" ).val() == '' ){
                        			return true;
                        		}else{
                        			var start = new Date( $( "#editStartDate" ).val() );
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

}

/**
 * 重构表单验证
 */
function refactorFormValidator() {
	$('#testnewPerson').on('hidden.bs.modal', function () {
		$("#addTest").data('bootstrapValidator').destroy();
		$('#addTest').data('bootstrapValidator', null);
		oac_FormValidator();
	});

	$('#Handle').on('hidden.bs.modal', function () {
		$("#HandleDev").data('bootstrapValidator').destroy();
		$('#HandleDev').data('bootstrapValidator', null);
		formValidator();
	});
	$('#editTest').on('hidden.bs.modal', function () {
		$("#editTestF").data('bootstrapValidator').destroy();
		$('#editTestF').data('bootstrapValidator', null);
		oac_FormValidator();
	});
	$('#Assignment').on('hidden.bs.modal', function () {
		$("#assignForm").data('bootstrapValidator').destroy();
		$('#assignForm').data('bootstrapValidator', null);
		formValidator();
	});
}