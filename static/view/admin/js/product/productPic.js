var itemGridRowIndex;

//关联商品图片方式  开始
function setProductPic(){
	var row = $('#productListGrid').datagrid('getSelected');
  	if (row){
			 $('#editProductWin').dialog('open');  // open a window 
			 productId = row.productId;
			 
			 $('#itemImageGrid').datagrid({
				pagination:true,
				rownumbers:true, 
				fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
				striped : true,   //设置为true将交替显示行背景。
				autoRowHeight:false,
				height:550,
				fit : true,
				loadMsg:"数据正在加载中，请稍后",
				method : "POST",
				title : '全部商品图片',
				pageList:[20,50,80],
				queryParams: {
					 productId: productId
				},
				url: basePath + '/item/getItemImages.htm',
				columns:[[
						  {field:"ck",checkbox:true},//有这个属性了，才有选择框出来
						  {field:'id',hidden:true}, 
						  {field:'itemId',hidden:true},
						  {field:'itemName',title:'商品名称',width:50,align:'center'},
						  {field:'imageUri',title:'imageUri',hidden:true},
						  {field:'fieldvalue',title:'商品图片',width:50,align:'left',formatter:function(value,row,index){
								if(row.imageUri != undefined && row.imageUri != null && row.imageUri !=  ''){
									return "<img style='display: block;float: left;' src='"+ homePath +"/crawler/"+row.imageUri+"' onload='getImgSize(this)'/>"; 
								}
				          }
		      	  }
				  ]],
				toolbar : $("#productPicToolbar"),
			    onLoadSuccess:function(data){
			    	if (data.rows.length > 0) {
		                 //调用mergeCellsByField()合并单元格
		                 mergeCellsByField("itemImageGrid", "itemId,itemName");
		        	}
			    }
			  });
			  
			  $('#productImageGrid').datagrid({
				pagination:true,
				rownumbers:true, 
				fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
				striped : true, //设置为true将交替显示行背景。
				autoRowHeight:false,//取消自动适应高度.
				height:550,
				fit : true,
				loadMsg:"数据正在加载中，请稍后",
				title : '已关联商品图片',
				method : "POST",
				pageList:[20,50,80],
				queryParams: {
					 productId: productId
				},
				url: basePath + '/item/getProductAssignedImage.htm',
				columns:[[
						  {field:"ck",checkbox:true},//有这个属性了，才有选择框出来
						  {field:'id',hidden:true}, 
						  //{field:'itemId',hidden:true},
						  //{field:'itemName',title:'商品名称',width:50,align:'center'},
						  {field:'imageUri',title:'imageUri',hidden:true},
						  {field:'fieldvalue',title:'商品图片',width:50,align:'left',height:50,formatter:function(value,row,index){
							    if(row.imageUri != undefined && row.imageUri != null && row.imageUri !=  ''){
									
									return "<img style='display: block;float: left;' src='"+ homePath +"/crawler/"+row.imageUri+"' onload='getImgSize(this)'/>"; 
								}
				          }
		      	  }
				  ]],
				toolbar : $("#productDeleteToolbar")
			  });
			  
   	}else{
   		$.messager.alert("提示信息","请选择一条记录");
   	}
}

//保存所有选中的商品图片
function saveProductPic(){
	var row = $('#itemImageGrid').datagrid('getSelections');
	if (row){
		var imageUris = [];
		for(var i = 0; i < row.length; i++){
			imageUris.push(row[i].imageUri);
		}
		//imageUris.join("&");
		$.ajax({
		   type: "POST",
		   url: basePath + '/item/saveImageUrisToProduct.htm',
		   data: "imageUris="+imageUris + "&productId=" + productId,
		   success: function(msg){
					$('#productImageGrid').datagrid('reload'); 
				   $.messager.alert("提示信息",msg);
		   }
		});

	}else{
   		$.messager.alert("提示信息","请选择一条记录");
   	}
}

function deleteProductPic(){
	var row = $('#productImageGrid').datagrid('getSelections');
	if (row){
		var imageUris = [];
		for(var i = 0; i < row.length; i++){
			imageUris.push(row[i].imageUri);
		}
		imageUris.join("&");
		$.ajax({
		   type: "POST",
		   url: basePath + '/item/deleteProductPic.htm',
		   data: "imageUris="+imageUris + "&productId=" + productId,
		   success: function(msg){
				   $.messager.alert("提示信息",msg);
				   $('#productImageGrid').datagrid('reload'); 
		   }
		});

	}else{
   		$.messager.alert("提示信息","请选择一条记录");
   	}
}

function getImgSize(obj){	
	var imgHeight=obj.height;
	var imgWidth=obj.width;
	$(obj).after("<sapan style='text-align: center;line-height: 50px;'>   "+imgWidth+"px*"+imgHeight+"px</span>");
	$(obj).css('height','50px');
	$(obj).css('width','50px');
	$(obj).parent().css('height','50px');
}
		


