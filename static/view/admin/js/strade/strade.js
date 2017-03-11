var imagesPath = homePath + '/admin/images';
 var url;


/*
	    * 查询方法
	    */
	    function doSearch(){	    	
	    	$('#stradeListGrid').datagrid('reload',{
				stradeId :$("#stradeId").textbox('getValue') == '' ? -1 : $("#stradeId").textbox('getValue'),
				sharerId : $("#sharerId").textbox('getValue') == '' ? -1 : $("#sharerId").textbox('getValue') ,
				status: $("#status").combobox('getValue'),
	    		name: $('#name').val(),
	    		loginId: $('#loginId').val()
			});
			
			
	    }
    
    
   
    


    
    
    
    
    $(function (){	
    		
    	$('#stradeListGrid').datagrid({
    	    url:basePath  + "/strade/stradeList.htm",
            fitColumns:true,  
            striped : true, 
            pagination:true,
            rownumbers:true, 
            method : "get", 
			sortName:"gmt_accept",
			sortOrder: "desc",	
            loadMsg:"数据正在加载中，请稍后",
            singleSelect:true,
            fit:true,
            columns:[[
    		          {field:'id',hidden:true},
    		          {field:'strade_id',title:'拼单号',width:100,align:'center'},
    		          {field:'fieldvalue',title:'大C信息',width:100,align:'center',formatter:function(value,row,index){
    		          		return row.sharer_id + "/" +row.name + "/" + row.login_id;
    		          	}
    		          },
    		          {field:'gmt_accept',title:'配单时间',width:100,align:'center',formatter:formatterDate,sortable:true},
    		          {field:'status',title:'拼单状态',width:100,align:'center',formatter:function(value,row,index){
    		          	  if(row.status=="1"){
    		          	     return '新单';
    		          	  }else if(row.status=="2"){
    		          	     return '待下单';
    		          	  }else if(row.status=="3"){
    		          	     return '待官网发货'
    		          	  }else if(row.status=="4"){
    		          	     return '待大C收货';
    		          	  }else if(row.status=="5"){
    		          	     return '待国内发货';
    		          	  }else if(row.status=="6"){
    		          	  	return '待确认';
    		          	  }else if(row.status=="7"){
    		          	  	return '结束';
    		          	  }else if(row.status=="8"){
    		          	  	return '非正常结束';
    		          	  }else if(row.status=="9"){
    		          	  	return '待中转';
    		          	  }
    		          	}},
    		          {field:'operation',title:'操作',width:100,align:'center',
				        	  formatter: function(value,row,index){
				        		  return "<a onclick='stradeDetail("+row.strade_id+")'>详情</a>";
		        	  		}
		        	  	}
    		         
    	          ]],	   
    	     toolbar:'#stradeToolbar'
    	});	   	   
    });
    	
    	
    	function stradeDetail(strade_id){
    		  var row = $('#stradeListGrid').datagrid('getSelected');
    			window.open(basePath + '/strade/getStradeById.htm?stradeId='+strade_id);
    	}  	   	  	   	
	 	