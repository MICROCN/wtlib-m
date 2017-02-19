function handerSumbit(productId,itemIds){
	$.ajax({
		   type: "POST",
		   url: basePath + '/item/assignItemsToProduct.htm',
		   data: "productId="+productId+"&itemIds="+itemIds,
		   success: function(msg){
		   		$('#assignedGoodsGrid').datagrid('reload');
				$('#allGoodsGrid').datagrid('reload');
			     $.messager.alert("提示信息",msg);		

		   }
		});
}

/**
 * 移除产品关联的商品
 */
function removeProductItems(itemIds){
		 var row = $('#assignedGoodsGrid').datagrid('getSelected');
		if (row){
			$.messager.confirm('重要信息','删除该商品会删掉其关联图片 确认删除?',function(r){
					if (r){ 
						$.ajax({
							type: "POST",
			 		    url: basePath + '/item/removeItemsFromProduct.htm',
			        data: "itemIds="+itemIds,
							success:function(result){
								if(result = 'success'){
									$('#assignedGoodsGrid').datagrid('reload');
					 			  $('#allGoodsGrid').datagrid('reload');
									$.messager.alert("提示信息：", "删除成功"); 
								
								}else{
									$.messager.alert("提示信息：", "删除失败"); 
								}						
							}
						});
					}
				});
		}else{
			 $.messager.alert("提示信息：", "请选择一条记录"); 
		}
}
