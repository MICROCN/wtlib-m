$("#all").toggle(function(){
    $(":checkbox").attr("checked","checked");
},
    function(){
        $(":checkbox").removeAttr("checked");
    }
)
$("#trash").click(function(){
    var box = $("input:checkbox");
    for(var i = 0 ;i<box.size();i++){
        if(true==box[i].checked)
            $(box[i]).parent().parent().remove();
    };
})
$(function(){
    var scene = $(document).height()-$(window).height();
    var top=$(document).scrollTop();
    if(top>=scene){
        $(".loading").css('display','block');
    }
})