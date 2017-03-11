/**
 * Created by cbjcl on 2017/3/7.
 */
$("#submitButton").click(function(){
    var userValue=$("#username").val();
    var wordValue=$("#password").val();
    var strcheck=/[`~!#$%^&*()_+<>?:"{},.\/;'[\]]/im;
    if(strcheck.test(userValue)){
        $("#username").html("用户名不能包含空格\\<>等非法字符");
    }
    else if(strcheck.test(wordValue)){
        $("#password").html("密码不能包含空格\\<>等非法字符");
    }
    else{
        var _data={
            'username':userValue,
            'password':wordValue
        };
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url:"http://localhost:8080/user/login.do",
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            data:JSON.stringify(_data),
            success:function(data){
                if(data.code!="10000"){
                    $("#password").html(data.msg);
                }else{
                    window.location.href="http://localhost:8080/view/component/default.html"
                }
            }
        });
    }
});