var imagesPath = homePath + '/admin/images';
 var url;


/*
	    * 查询方法
	    */
	    function doSearch(){	    	
	    	//var t = $("#tradeId").textbox("getValue");
	    	$('#orderListGrid').datagrid('reload',{
	    		url:basePath  + "/order/list.htm",
	    		 status: $("#status").combobox('getValue'),
	    		tradeId: $('#tradeId').val(),
	    		buyerId: $('#buyerId').val(),
	    		loginId: $('#loginId').val(),
	    		title: $('#title').val()
			});
			
			
	    }
       
    
    
    $(function (){	
    		
    	$('#orderListGrid').datagrid({
    	    url:basePath  + "/order/list.htm",
            fitColumns:true,  
            striped : true, 
            pagination:true,
            rownumbers:true, 
            method : "get",
			sortName: "gmt_order",
			sortOrder: "desc",
            loadMsg:"数据正在加载中，请稍后",
            singleSelect:true,
            pageList:[20,50,80],
            fit:true,
            columns:[[
    		          {field:'id',hidden:true},
    		          {field:'trade_id',title:'订单号',width:100,align:'center'},
    		          {field:'fieldvalue',title:'买家信息',width:100,align:'center',formatter:function(value,row,index){
    		          		return row.buyer_id + "/" +row.name + "/" + row.login_id;
    		          	}
    		          },
    		          {field:'title',title:'产品',width:100,align:'center'},
    		          {field:'gmt_order',title:'下单时间',width:100,align:'center',formatter:formatterDate,sortable:true},
    		          {field:'toBeDone',title:'支付信息',width:100,align:'center',formatter:function(value,row,index){
						          if(row.platform=="1"){
							          return '微信支付'+"/" +row.gmt_pay + "/" + row.pay_id;
						          }else if(row.platform=="2"){
									  return '支付宝'+"/" +row.gmt_pay + "/" + row.pay_id;
								  }
					  }},
    		          {field:'status',title:'订单状态',width:100,align:'center',formatter:function(value,row,index){
							return row.tradeStatusDesc;
    		          	},sortable:true},
    		            {field:'operation',title:'操作',width:100,align:'center',
				        	  formatter: function(value,row,index){
				        		  return "<a onclick='tradeDetail("+row.trade_id+")'>详情</a>";
		        	  		}
		        	  	}
    		         
    	          ]],	   
    	     toolbar:'#orderToolbar'
    	});	   	   
    });
    	
    	
    	function tradeDetail(trade_id){
    		  var row = $('#orderListGrid').datagrid('getSelected');
    			window.open(basePath + '/order/getTradeInfoById.htm?tradeId='+trade_id);
    	}  	   	  	   	