<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>wtlib</title>
<script src="/wtlib-web/static/js/jquery-1.3.2.min.js"></script>

</head>
<body>
	<button id="click">fafdasfasfasdfasdfasdfasfasfasf</button>
</body>
<script>
	$("#click").click(function (){
	var _data= {
          "loginId": "admin",
          "password": "cbjcl0204"
      }
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url:"/wtlib-web/login",
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            data:JSON.stringify(_data),
            success:function(data){
                console.log(data);
                console.log(data.content);
                if(data.code!="10000"){
                    $("#errorMessage").html(data.msg);
                    $("#verify_img").attr("src","/dcs/verify_code?time="+new Date().getTime());
                }else{
                    window.location.href="/dcs/excel/select_limit"
                }
            }
        });
	})
</script>
</html>