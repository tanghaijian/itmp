
function loginExt(){
		var userAccount  = $("#userAccount").val();	
		if(userAccount == "")
		{
		  $(".txtlayer").show();
          $("#errspan").html("请输入用户");
		  $("#userAccount").focus();
		  return false;
		}
		
		var password = $("#password").val();
		if(password == "")
		{
		   $(".txtlayer").show();
          $("#errspan").html("请输入密码");
		  $("#password").focus();
		 	 return false;
		 }		
       
		$("form[name='form1']").attr('action','/systemui/login');	
 				
		document.forms[0].submit();

}


