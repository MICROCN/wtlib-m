$(document).ready(function(){
    $("#login").click(function () {
    	 if($("#rememberMe").is(":checked") == true){
             // 存储一个带7天期限的 cookie
             $.cookie("rememberMe", "true", { expires: 7 });
             $.cookie("usernameCookie", $("#userName").val(),{expires:7});
             $.cookie("passwordCookie", $("#passWord").val(),{expires:7});
         }else{
             $.cookie("rememberMe", "false", { expires: -1 });
             $.cookie("usernameCookie", '', { expires: -1 });
             $.cookie("passwordCookie", '', { expires: -1 });
         }
		 
		var username = $("#userName").val();
		var password = $("#passWord").val();
	   
		if(username == ""){
			$("#userNameInfo").css("color","red");
			$("#userNameInfo").text("请输入用户名！");
			return;
		}
		
		if(password == ""){
			$("#passWordInfo").css("color","red");
			$("#passWordInfo").text("请输入密码！");
			return;
		}

    });
	
    if ($.cookie("rememberMe") == "true") {
        $("#rememberMe").attr("checked", true);
        $("#userName").val($.cookie("usernameCookie"));
        $("#passWord").val($.cookie("passwordCookie"));
       
    } 
    
    $("#passWord").blur(function(){
        var password = $("#passWord").val();
    	if(password == ""){
    		$("#passWordInfo").css("color","red");
    		$("#passWordInfo").text("请输入密码！");
    	}else{
    		$("#passWordInfo").text(" ");
		}
    });
    	
    $("#userName").blur(function(){
    	var username = $("#userName").val();
   	    if(username == ""){
   	    	$("#userNameInfo").css("color","red");
   	    	$("#userNameInfo").text("请输入用户名！");
    	}else{
   	    	$("#userNameInfo").text(" ");
		}
    });
   
    function showIndex(){
    	location.href="./index.jsp";
    };
   
   $(document).click(function(c){
	   
   });
    
	$(document).keydown(function(b) {
		if (b.keyCode == 13) {
			$("#login").click();
		}
	});
});

