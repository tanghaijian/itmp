$(document).ready(function(){
		var error = false;

		
		$("#oldpass").blur(function(){
			var oldpass = $("#oldpass").val();
			var $this = $(this);
			if(oldpass =='') {
				showError($this, '密码不能为空');
				errorIco.insertAfter($this);
				error = true;

			}
		});

		
		$("#newpass").blur(function(){
			var newpass = $("#newpass").val();
			var $this = $(this);
			if(newpass == '') {
				showError($this, '新密码不能为空');
				error = true;
			}else if(!checkPwd(newpass)){
				showError($this, '新密码不符合规则，长度必须大于8位，且保函数字、字母和特殊字符');
				error = true;
			}
			else {
				var oldpass = $("#oldpass").val();
				if(newpass == oldpass){
					showError($this, '不能与原来密码相同');
					error = true;
				}else
					hideError($this);
			}
		});

		$("#newpassAgain").blur(function(){
			var newpass = $("#newpass").val();
			var $this = $(this);
			if(newpass == '') {
				showError($("#newpass"), '新密码不能为空');
				error = true;
				return;
			}

			var newpassAgain = $("#newpassAgain").val();
			if(newpassAgain != newpass) {
				showError($this, '与输入的新密码不一致');
				error = true;
			}
			else {
				hideError($("#newpass"));
				hideError($this);
			}
		});
		
		$("#submit").click(function(event){
			error = false;
			$("#oldpass").blur();
			$("#newpass").blur();
			$("#newpassAgain").blur();
			if(!error) {
				chg();
			}

			event.preventDefault();
			return false;
		});
		
		
	});

	function showError(formSpan, errorText) {
		var errorIco = formSpan.next(".icon-remove-sign");
		if(errorIco.length == 0)
			 $('<i class="icon-remove-sign"></i>').insertAfter(formSpan);
		formSpan.closest("div").next().html(errorText).show();
	}
	
	
	function hideError(formSpan){
		
		var errorIco = formSpan.next(".icon-remove-sign");
		if(errorIco.length > 0)
			errorIco.remove();
		formSpan.closest("div").next().empty().hide();
	}
	
	
	function chg(){
		var oldpass = $("#oldpass").val();
		var newpass = $("#newpass").val();
		var data={'oldPassword':oldpass,'password':newpass};
		var url = "modifyPass";
		ajaxPost(url,data,function(msg){
				window.location="main";
			},function(msg){
				$("#modifySuccess").html("<strong>Fail!</strong>: "+msg.errorMessage);
				$("#modifySuccess").css({'display':'inline-block'});
			});
	}
	
	function checkPwd(value) {
		var flag = true;
		var upper = new RegExp(/[A-Z]/);
		var lower = new RegExp(/[a-z]/);
		var number = new RegExp(/\d/);
		var sign = new RegExp(/\W/);
		var sign1 = new RegExp(/\_/);
	    var length = value.length;
	   
		if (length < 8) {
			flag = false;
			return flag;
		}
		if (!(upper.test(value) || lower.test(value))) {
		    flag = false;
			return flag;
		}
		if (!number.test(value)) {
			flag = false;
			return flag;
		}
		if (!(sign.test(value)||sign1.test(value))) {
			flag = false;
			return flag;
		}
		
		return flag;
	}