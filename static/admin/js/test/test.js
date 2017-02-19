$(function (){
$('#userGrid').datagrid({
	    url:basePath + "/orderList.htm",
        fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
        striped : true, //设置为true将交替显示行背景。
        pagination:true,
        rownumbers:true, 
        method : "get", // method=RequestMethod.GET,  control 如果写明了，用get，那么前台只能用get去请求了。easyUI默认是post请求
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
        columns:[[
              	  {field:"ck",checkbox:true},//有这个属性了，才有选择框出来
		          {field:'id',hidden:true},
		          {field:'name',title:'用户名',width:100,align:'center'},
		          {field:'location',title:'链接地址',width:100,align:'center'}
	          ]],
	   toolbar:"#toolbar"
	});
	
});