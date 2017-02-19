$(function(){
    var scene = $(document).height()-$(window).height();
    var top=$(document).scrollTop();
    if(top>=scene){
        $(".loading").css('display','block');
    }
})