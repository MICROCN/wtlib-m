$(function(){
    var scene = $(document).height()-$(window).height();
    var top=$(document).scrollTop();
    if(top>=scene){
        $(".loading").css('display','block');
    }
})
$(".list-info").click(function(){
    $(".search").css('display','none');
    $(".container").css('display','none');
    $(".info-head").css('display','flex');
    $(".info").css('display','block');
})
$(".icon-back").click(function(){
    $(".search").css('display','flex');
    $(".container").css('display','block');
    $(".info-head").css('display','none');
    $(".info").css('display','none');
})