var url;

$(function (){	
		
	$('#usermanagerListGrid').datagrid({
	    	url:basePath  + "/usermanagerList.htm",
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true, 
        method : "get", 
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
        columns:[[
              {field:"ck",checkbox:true},
		          {field:'id',hidden:true},
		          {field:'userid',title:'ID',width:100,align:'center'},
		          {field:'name',title:'昵称',width:100,align:'center'},
		          {field:'phone',title:'手机号',width:100,align:'center'},
		          {field:'registrationtime',title:'注册时间',width:100,align:'center'},
		          {field:'signintime',title:'上次登录时间',width:100,align:'center'},      
	          ]],
	     toolbar:'#userManagerToolbar'
	});
	
	
});
