var url;

  function doSearch(){	   
	    	$('#buyerListGrid').datagrid('reload',{	
	    		buyerId:$('#buyerId').val(),
	    		loginId:$('#loginId').val(),
	    		name: $('#name').val()
			});
			
			
	    }

$(function (){	
		
	$('#buyerListGrid').datagrid({
	    url:basePath  + "/buyer/buyerList.htm",
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true, 
        method : "get", 
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
        columns:[[
		          {field:'id',hidden:true},
		          {field:'buyerId',title:'ID',width:100,align:'center'},
		          {field:'loginId',title:'登录id',width:100,align:'center'},
		          {field:'password',title:'密码',width:100,align:'center'},
		          {field:'name',title:'姓名',width:100,align:'center'},
		          {field:'loginTime',title:'最近一次登录时间',width:100,align:'center',formatter:formatterDate}   			  
	          ]],
	      toolbar:'#sharerToolbar'
	});
});

