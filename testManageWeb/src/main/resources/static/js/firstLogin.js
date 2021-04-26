$(document).ready(function(){
		var error = false;


		$("#newpass").blur(function(){
			var newpass = $("#newpass").val();
			if(newpass == '') {
				showError('newpass', '新密码不能为空');
				error = true;
			}else
				if(!checkPwd(newpass)){
					showError("newpass","密码不符合规则，必须大于8位且包含字母、数字和特殊字符");
					error = true;
				}
				else {
					$("#newpass").css({"border-color":"green"});
					$("#newpassTip").css({"display":"none"});
			}
		});

		$("#newpassAgain").blur(function(){
			var newpass = $("#newpass").val();
			if(newpass == '') {
				showError('newpass', '新密码不能为空');
				error = true;
				return;
			}

			var newpassAgain = $("#newpassAgain").val();
			if(newpassAgain != newpass) {
				showError('newpassAgain', '与输入的新密码不一致');
				error = true;
			}
			else {
				$("#newpassAgain").css({"border-color":"green"});
				$("#newpassAgainTip").css({"display":"none"});
			}
		});
		
		$("#submit").click(function(event){
			error = false;
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
		$("#" + formSpan).css({"border-color":"red"});
		$("#" + formSpan + "Tip").empty();
        $("#" + formSpan + "Tip").append(errorText);
        $("#" + formSpan + "Tip").css({"display":"inline"});
	}
	
	
	function chg(){
		var newpass = $("#newpass").val();
		var data={password:newpass};
		var url = "firstChg";
		ajaxPost(url,data,function(msg){
				window.location="main";
			},function(msg){
				$("#modifySuccess").html("<strong>Fail!</strong> 密码修改失败！"+msg.errorMessage);
				$("#modifySuccess").css({'display':'inline'});
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