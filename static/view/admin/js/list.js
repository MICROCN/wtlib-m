$("#trash").click(function(){
    var box = $(".odd input[type='checkbox']:checked");
    for(var i = 0 ;i<box.size();i++){
        if(true==box[i].checked){
            var id = $(box[i]).parent().parent().attr("id");
            var code =$(box[i]).closest("table").attr("id");
            var _data = {
                "code":code,
                "id":id
            }
            $.ajax({
                url: "",
                type: "POST",
                dataType: "json",
                contentType: "application/x-www-form-urlencoded",
                data: _data,
                success: function (data) {
                    $(box[i]).parent().parent().parent().remove();
                    alert("删除成功");
                },
                error: function (data) {
                    alert(data.msg);
                }
            });
        }
    };
});
$("#all").click(function() {
    var box = $(".odd input[type='checkbox']:checked").size();
    var lenght = $(".odd input[type='checkbox']").length;
    if(box==lenght){
        $(":checkbox").removeAttr("checked",'false');
    }
    else
        $(":checkbox").prop("checked",'true');
});