var url;
var productId;
var lastGrid;
//Ҷ����Ŀid
var cid = -1; // Ĭ�� �մ�ҳ���ʱ�򣬲���ѯproduct list��ֵ���ȵ���Ŀ֮��Ų�ѯ
var weightWay;
var itemIdQuery;

var cids;

$(function(){
    $('#grid1').datagrid({
	    url:basePath + "/queryCategories.htm?parentCid=0&type=1",
        fitColumns:true,  //����Ϊtrue���Զ�ʹ����Ӧ������Է�ֹ����ˮƽ������
        striped : true, //����Ϊtrue��������ʾ�б�����
        method : "get",
        loadMsg:"�������ڼ����У����Ժ�",
        singleSelect:true,
        fit:true,
        height:450,
				pageList:[20,50,80],
        columns:[[
		          {field:'name',title:'һ����Ŀ',width:100,align:'center'}
	          ]],
	    onSelect:function(){
	    	cur_level = 1;
	    	//���3,4,5��
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
	    	//���ر�Ŀ¼�µ���Ʒ
	    	
				  	
	    //����pid������һ������Ŀ
	    	
			//�ж��ĸ�����ĩ�� ��Ŀ
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
	            fitColumns:true,  //����Ϊtrue���Զ�ʹ����Ӧ������Է�ֹ����ˮƽ������
	            striped : true, //����Ϊtrue��������ʾ�б�����
	            method : "get",
	            loadMsg:"�������ڼ����У����Ժ�",
	            singleSelect:true,
	            fit:true,
	            height:450,
				pageList:[20,50,80],
	            columns:[[
	    		          {field:'name',title:'������Ŀ',width:100,align:'center'}
	    	          ]],
	            onSelect:function(){
	            	cur_level = 2;
	            	//���4,5��
	    	    	var fourth_data=$('#grid4').datagrid('getData');
	    	    	if(fourth_data.rows.length>0){
	    	    		$('#grid4').datagrid('loadData',{total:0,rows:[]});
	    	    	}
	    	    	var fifth_data=$('#grid5').datagrid('getData');
	    	    	if(fifth_data.rows.length>0){
	    	    		$('#grid5').datagrid('loadData',{total:0,rows:[]});
	    	    	}
	    	    	var second_row = $('#grid2').datagrid('getSelected');
					//�ж��ĸ�����ĩ�� ��Ŀ
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
	    	            fitColumns:true,  //����Ϊtrue���Զ�ʹ����Ӧ������Է�ֹ����ˮƽ������
	    	            striped : true, //����Ϊtrue��������ʾ�б�����
	    	            method : "get",
	    	            loadMsg:"�������ڼ����У����Ժ�",
	    	            singleSelect:true,
	    	            fit:true,
	    	            height:450,
										pageList:[20,50,80],
	    	            columns:[[
	    	    		          {field:'name',title:'������Ŀ',width:100,align:'center'}
	    	    	          ]],
	    	            onSelect:function(){
	    	            	cur_level = 3;
	    	            	//���5��
	    	    	    	var fifth_data=$('#grid5').datagrid('getData');
	    	    	    	if(fifth_data.rows.length>0){
	    	    	    		$('#grid5').datagrid('loadData',{total:0,rows:[]});
	    	    	    	}
	    	    	    	var third_row = $('#grid3').datagrid('getSelected');
							//�ж��ĸ�����ĩ�� ��Ŀ
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
	    	    	            fitColumns:true,  //����Ϊtrue���Զ�ʹ����Ӧ������Է�ֹ����ˮƽ������
	    	    	            striped : true, //����Ϊtrue��������ʾ�б�����
	    	    	            method : "get",
	    	    	            loadMsg:"�������ڼ����У����Ժ�",
	    	    	            singleSelect:true,
	    	    	            fit:true,
	    	    	            height:450,
								pageList:[20,50,80],
	    	    	            columns:[[
	    	    	    		          {field:'name',title:'�ļ���Ŀ',width:100,align:'center'}
	    	    	    	          ]],
	    	    	            onSelect:function(){
	    	    	            	cur_level = 4;
	    	    	    	    	var fourth_row = $('#grid4').datagrid('getSelected');
									//�ж��ĸ�����ĩ�� ��Ŀ
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
	    	    	    	            fitColumns:true,  //����Ϊtrue���Զ�ʹ����Ӧ������Է�ֹ����ˮƽ������
	    	    	    	            striped : true, //����Ϊtrue��������ʾ�б�����
	    	    	    	            method : "get",
	    	    	    	            loadMsg:"�������ڼ����У����Ժ�",
	    	    	    	            singleSelect:true,
	    	    	    	            fit:true,
	    	    	    	            height:450,
										pageList:[20,50,80],
	    	    	    	            columns:[[
	    	    	    	    		          {field:'name',title:'�弶��Ŀ',width:100,align:'center'}
	    	    	    	    	          ]],
	    	    	 	    	    	onSelect:function(){
	    	  	    	    	            	cur_level = 5;
												var five_row = $('#grid5').datagrid('getSelected');
												//�ж��ĸ�����ĩ�� ��Ŀ
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

