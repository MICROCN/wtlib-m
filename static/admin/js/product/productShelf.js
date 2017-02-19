var url;
var productId;
var lastGrid;
//叶子类目id
var cid = -1; // 默认 刚打开页面的时候，不查询product list的值，等点类目之后才查询
var weightWay;
var itemIdQuery;

var cids;

$(function(){
    $('#grid1').datagrid({
	    url:basePath + "/queryCategories.htm?parentCid=0&type=1",
        fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
        striped : true, //设置为true将交替显示行背景。
        method : "get",
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
        height:450,
				pageList:[20,50,80],
        columns:[[
		          {field:'name',title:'一级类目',width:100,align:'center'}
	          ]],
	    onSelect:function(){
	    	cur_level = 1;
	    	//清空3,4,5表
	    	var third_data=$('#grid3').datagrid('getData');
	    	if(third_data.rows.length>0){
	    		$('#grid3').datagrid('loadData',{total:0,rows:[]});
	    	}
	    	var fourth_data=$('#grid4').datagrid('getData');
	    	if(fourth_data.rows.length>0){
	    		$('#grid4').datagrid('loadData',{total:0,rows:[]});
	    	}
	    	var fifth_data=$('#grid5').datagrid('getData');
	    	if(fifth_data.rows.length>0){
	    		$('#grid5').datagrid('loadData',{total:0,rows:[]});
	    	}
	    	var row = $('#grid1').datagrid('getSelected');
	    	//加载本目录下的商品
	    	
				  	
	    //根据pid加载下一级的类目
	    	
			//判断哪个是最末级 类目
			if(row.isParent == 'n'){
				lastGrid = "grid1";
			}else{
				lastGrid = "grid2";
			}
			$("#cid").val(row.cid);
		
			var query = $("#productOnshelfForm").serialize();
		var href=basePath+"/item/getItems.htm";
		$.post(href,query,function(data) {
			$("#itemList").html(data);
		});
			/*
			 $.ajax({
				type: "POST",
				url: basePath + "/getCid.htm?cid="+row.cid+"&type=1",
				success: function(msg){
					cids = msg;
					
					$('#allGoodsGrid').datagrid('reload',{
							cid:row.cid
					});	
				}

				});	
				*/
			
	    	$('#grid2').datagrid({
	    		url:basePath + "/queryCategories.htm?parentCid="+ row.cid+"&type=1",
	            fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	            striped : true, //设置为true将交替显示行背景。
	            method : "get",
	            loadMsg:"数据正在加载中，请稍后",
	            singleSelect:true,
	            fit:true,
	            height:450,
				pageList:[20,50,80],
	            columns:[[
	    		          {field:'name',title:'二级类目',width:100,align:'center'}
	    	          ]],
	            onSelect:function(){
	            	cur_level = 2;
	            	//清空4,5表
	    	    	var fourth_data=$('#grid4').datagrid('getData');
	    	    	if(fourth_data.rows.length>0){
	    	    		$('#grid4').datagrid('loadData',{total:0,rows:[]});
	    	    	}
	    	    	var fifth_data=$('#grid5').datagrid('getData');
	    	    	if(fifth_data.rows.length>0){
	    	    		$('#grid5').datagrid('loadData',{total:0,rows:[]});
	    	    	}
	    	    	var second_row = $('#grid2').datagrid('getSelected');
					//判断哪个是最末级 类目
					if(second_row.isParent == 'n'){
						lastGrid = "grid2";
						
						
					}else{
						lastGrid = "grid3";
					}
					 $("#cid").val(second_row.cid);
		
			var query = $("#productOnshelfForm").serialize();
		var href=basePath+"/item/getItems.htm";
		$.post(href,query,function(data) {
			$("#itemList").html(data);
		});
					
					
					//$('#productListGrid').datagrid('reload',{
						//	cid:second_row.cid
						//});
					
	    	    	$('#grid3').datagrid({
	    	    		url:basePath + "/queryCategories.htm?parentCid="+ second_row.cid+"&type=1",
	    	            fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	    	            striped : true, //设置为true将交替显示行背景。
	    	            method : "get",
	    	            loadMsg:"数据正在加载中，请稍后",
	    	            singleSelect:true,
	    	            fit:true,
	    	            height:450,
										pageList:[20,50,80],
	    	            columns:[[
	    	    		          {field:'name',title:'三级类目',width:100,align:'center'}
	    	    	          ]],
	    	            onSelect:function(){
	    	            	cur_level = 3;
	    	            	//清空5表
	    	    	    	var fifth_data=$('#grid5').datagrid('getData');
	    	    	    	if(fifth_data.rows.length>0){
	    	    	    		$('#grid5').datagrid('loadData',{total:0,rows:[]});
	    	    	    	}
	    	    	    	var third_row = $('#grid3').datagrid('getSelected');
							//判断哪个是最末级 类目
							if(third_row.isParent == 'n'){
								lastGrid = "grid3";
								
								
							}else{
								lastGrid = "grid4";
							}
							$("#cid").val(third_row.cid);
		
			var query = $("#productOnshelfForm").serialize();
		var href=basePath+"/item/getItems.htm";
		$.post(href,query,function(data) {
			$("#itemList").html(data);
		});
	
	    	    	    	$('#grid4').datagrid({
	    	    	    		url:basePath + "/queryCategories.htm?parentCid="+ third_row.cid+"&type=1",
	    	    	            fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	    	    	            striped : true, //设置为true将交替显示行背景。
	    	    	            method : "get",
	    	    	            loadMsg:"数据正在加载中，请稍后",
	    	    	            singleSelect:true,
	    	    	            fit:true,
	    	    	            height:450,
								pageList:[20,50,80],
	    	    	            columns:[[
	    	    	    		          {field:'name',title:'四级类目',width:100,align:'center'}
	    	    	    	          ]],
	    	    	            onSelect:function(){
	    	    	            	cur_level = 4;
	    	    	    	    	var fourth_row = $('#grid4').datagrid('getSelected');
									//判断哪个是最末级 类目
									if(fourth_row.isParent == 'n'){
										lastGrid = "grid4";
										
										
									}else{
										lastGrid = "grid5";
									}
										$("#cid").val(fourth_row.cid);
		
			var query = $("#productOnshelfForm").serialize();
		var href=basePath+"/item/getItems.htm";
		$.post(href,query,function(data) {
			$("#itemList").html(data);
		});
									
									
									/* $('#productListGrid').datagrid('reload',{
											cid:fourth_row.cid
										}); */
									
	    	    	    	    	$('#grid5').datagrid({
	    	    	    	    		url:basePath + "/queryCategories.htm?parentCid="+ fourth_row.cid+"&type=1",
	    	    	    	            fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	    	    	    	            striped : true, //设置为true将交替显示行背景。
	    	    	    	            method : "get",
	    	    	    	            loadMsg:"数据正在加载中，请稍后",
	    	    	    	            singleSelect:true,
	    	    	    	            fit:true,
	    	    	    	            height:450,
										pageList:[20,50,80],
	    	    	    	            columns:[[
	    	    	    	    		          {field:'name',title:'五级类目',width:100,align:'center'}
	    	    	    	    	          ]],
	    	    	 	    	    	onSelect:function(){
	    	  	    	    	            	cur_level = 5;
												var five_row = $('#grid5').datagrid('getSelected');
												//判断哪个是最末级 类目
												if(five_row){
													lastGrid = "grid5";
													
													
												}
												  
												  $("#cid").val(five_row.cid);
		
			var query = $("#productOnshelfForm").serialize();
		var href=basePath+"/item/getItems.htm";
		$.post(href,query,function(data) {
			$("#itemList").html(data);
		});
												  
												  
	    	  	    	    	           }
	    	    	    	    	});
	    	    	    	    }
	    	    	    	});
	    	    	    }
	    	    	});
	    	    }
	    	});
	    }
	});
});

