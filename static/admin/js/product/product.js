var url;
var productId;
var lastGrid;
//叶子类目id
var cid = -1; // 默认 刚打开页面的时候，不查询product list的值，等点类目之后才查询
var weightWay;
var itemIdQuery;

var cids;

function selectitem(){
	$('#allGoodsGrid').datagrid('reload',{
		itemName: $('#name').val()	
	});	
}
function selectitems(productId){
	$('#assignedGoodsGrid').datagrid('reload',{
		productId:productId,
		itemName: $('#itemname').val()	
	});	
}

function searchProduct(){
	$('#productListGrid').datagrid('load',{
		title: $('#searchTitle').textbox('getValue'),
		cids: cids		
	});
			
}

$(function (){	
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
			 $.ajax({
				type: "POST",
				url: basePath + "/getCid.htm?cid="+row.cid+"&type=1",
				success: function(msg){
					cids = msg;
				   $('#productListGrid').datagrid('reload',{
						cids:msg
					});
					
					$('#allGoodsGrid').datagrid('reload',{
							cid:row.cid
					});	  
					$('#assignedGoodsGrid').datagrid('reload',{
															
					});
				}

				});		
			
			
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
					 $.ajax({
							type: "POST",
							url: basePath + "/getCid.htm?cid="+second_row.cid+"&type=1",
							success: function(msg){
								cids = msg;
								$('#productListGrid').datagrid('reload',{
									cids:msg
								});
								cid=second_row.cid;  //获得当前类目的cid
	    					$('#allGoodsGrid').datagrid('reload',{
									cid:second_row.cid
								});	
								$('#assignedGoodsGrid').datagrid('reload',{
															
								});
							}

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
							$.ajax({
								type: "POST",
								url: basePath + "/getCid.htm?cid="+third_row.cid+"&type=1",
								success: function(msg){
									cids = msg;
									$('#productListGrid').datagrid('reload',{
										cids:msg
									});
									cid=third_row.cid;  //获得当前类目的cid
	    					$('#allGoodsGrid').datagrid('reload',{
									cid:third_row.cid 
								});
									$('#assignedGoodsGrid').datagrid('reload',{
															
									});
								}

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
										$.ajax({
											type: "POST",
											url: basePath + "/getCid.htm?cid="+fourth_row.cid+"&type=1",
											success: function(msg){
												cids = msg;
												$('#productListGrid').datagrid('reload',{
													cids:msg
												});
												cid=fourth_row.cid;  //获得当前类目的cid
	    									$('#allGoodsGrid').datagrid('reload',{
													cid:fourth_row.cid
												});
												$('#assignedGoodsGrid').datagrid('reload',{
															
														});
											}

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
												$.ajax({
													type: "POST",
													url: basePath + "/getCid.htm?cid="+five_row.cid+"&type=1",
													success: function(msg){
														cids = msg;
														$('#productListGrid').datagrid('reload',{
															cids:msg
														});
														cid=five_row.cid;  //获得当前类目的cid
														$('#allGoodsGrid').datagrid('reload',{
															cid:five_row.cid
														});
														$('#assignedGoodsGrid').datagrid('reload',{
															
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
	    }
	});
	$('#productListGrid').datagrid({
	    url:basePath  + "/product/list.htm",
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true, 
        method : "get", 
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
		pageList:[20,50,80],
		onLoadSuccess: function(data){
			if(data.total!=0){
				$('#productListGrid').datagrid('selectRow', 0);
			}
			
		},
		queryParams: {
			cids : cids			
		},
        columns:[[
              	  {field:"ck",checkbox:true},
		          {field:'id',hidden:true},
		          {field:'productId',hidden:true},
		          {field:'title',title:'产品名称',width:100,align:'left'}
	          ]],
	    onSelect:function(){
			assignGoodsToProduct();
		},
	     toolbar:'#productToolbar'
	});
	$('#allGoodsGrid').datagrid({
			pagination:true,
			rownumbers:true, 
			fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
			striped : true, //设置为true将交替显示行背景。
			title:"商品列表",
			height:350,
			fit : true,
			loadMsg:"数据正在加载中，请稍后",
			url: basePath + '/item/list.htm',
			pageList:[20,50,80],
			queryParams: {
				cid : cid		
			},
			columns:[[
					  {field:"ck",checkbox:true},//有这个属性了，才有选择框出来
					  {field:'id',hidden:true},   
					  {field:'fieldvalue',title:'商品主图',width:50,align:'left',formatter:function(value,row,index)
							{
								if(row.imageUris != null && row.imageUris != ""){
									var imageUrisArray = row.imageUris.split(";");
									return "<img style='width:50px;height:50px' src='"+ homePath +"/crawler/"+ imageUrisArray[0] +"!w50x50.jpg'/>";
								}else{
									return null;
								}				          		
							}
					  },
					  {field:'itemName',title:'商品名称',width:100,align:'left',formatter: function(value,row,index){
							return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+row.url+"')>"+row.itemName+"</a>  ";
					  }}
				  ]],
			toolbar: [{
				text:'保存',
				iconCls:'icon-add',
				handler: function(){
					var selectItems = $('#allGoodsGrid').datagrid('getSelections');
					if(selectItems.length != 0){
						var itemIds = [];
						for(var i = 0; i < selectItems.length; i++){
							itemIds.push(selectItems[i].itemId);
						}
						itemIds.join("&");
						//调用function里面的提交方法
						
						handerSumbit(productId,itemIds);
					}else{
						 $.messager.alert('提示信息','请选择一条记录');
					}
				}
			},{
				text:'<input type="text" id="name" style="width:250px"/>',
			},{
			   id:'searchdos',
			   text:'搜索',
			   iconCls:'icon-search', 
			   handler:function(){					  
					 selectitem();
				}			
			}
			]			    
		});
	$('#assignedGoodsGrid').datagrid({
			pagination:true,
			rownumbers:true, 
			fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
			striped : true, //设置为true将交替显示行背景。
			title:"已分配商品列表",
			height:350,
			fit : true,
			loadMsg:"数据正在加载中，请稍后",
			method : "POST",
			pageList:[20,50,80],
			queryParams: {
				 productId: productId
			},
			url: basePath + '/item/assignedProductItemlist.htm',
			columns:[[
					  {field:"ck",checkbox:true},//有这个属性了，才有选择框出来
					  {field:'id',hidden:true}, 
					  {field:'itemName',title:'商品名称',width:100,align:'center'}
				  ]],
			toolbar: [{
				text:'删除',
				iconCls:'icon-remove',
				handler: function(){
					var selectItemIds = $('#assignedGoodsGrid').datagrid('getSelections');
					var itemIds = [];
					for(var i = 0; i < selectItemIds.length; i++){
						itemIds.push(selectItemIds[i].itemId);
					}
					itemIds.join("&");
					removeProductItems(itemIds);
				}
			},{
				text:'<input type="text" id="itemname" style="width:250px"/>',
			},{
			   id:'search',
			   text:'搜索',
			   iconCls:'icon-search', 
			   handler:function(){					  
					 selectitems(productId);
				}			
			}]	
		});
		/**
		$('#weightWay').combobox({
   			onSelect:function(record){
   				if(record.value == 1){
						showProduct();
						weightWay = 1;
					
					$.ajax({
						type:"POST",
						url:basePath + "/product/getProductWeight.htm",
						data:"productId=" + productId,
						dataType: 'json',  
						success:function(result){
							console.info(result.weight);
							if(result != null){
								$('#weightProduct').textbox('setValue',result.weight)
							}					
						}
					});
					
   				}else{
						weightWay = 2;
   					showSk2();	
   				}
   			}
   		});
		**/
		$('#itemId').combobox({
   			onSelect:function(record){
				var skuTable = $("#skuTable");
				$("#skuTable").empty(); //清空table
				$.ajax({  
						type: 'post',  
						data:"itemId=" + record.itemId,
					    url: basePath + '/item/getSkuPropByItemId.htm', 
						cache: false,  
						dataType: 'json',  
						success: function(itemPropertyDtoList){ 
							var skuTable = $("#skuTable");
							if(itemPropertyDtoList != null&&itemPropertyDtoList.length>0){
									$("#skuTable").empty();
									
									$('#weight').textbox('clear');
									var flag;
									if(record.skus==null || record.skus.length==0){
										flag=1;
										$('#setSkuTable').hide();
									}else{
										flag=0;
										$('#setSkuTable').show();
									}
									
									var _tr="";
									var _td="";
									_th="<tr><td style='width:100px;float:left'>商店名</td><td  colspan='5'>商品名</td></tr>"
									_tr="<tr><td>"+getShopsName(record.shopId)+"</td><td colspan='5'><a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+record.url+"')>"+record.itemName+"</a></td></tr>"
									skuTable.append(_th);
									skuTable.append(_tr);
									
									for(var i = 0; i < itemPropertyDtoList.length;i++){
									var ipvList = itemPropertyDtoList[i].listItemPropertyValueEntity;
									var _tr = "";
									var _td = "";
									
									_tr = "<tr><td style='height:120px' id='pops_"+i+"'><input type='checkbox' onclick='getPidAndVids(this)'>[全选]</input>      "+ itemPropertyDtoList[i].itemPropertyEntity.pname ;
									//如果获得是image + text 的话 就将value转换成json对象
									if(itemPropertyDtoList[i].itemPropertyEntity.type=="image,text"){
											
											for(var j = 0; j < itemPropertyDtoList[i].listItemPropertyValueEntity.length;j++){		
												var value=$.parseJSON(ipvList[j].value);
												
												if(flag){
													_td=_td+"<td style='width:100px;height:120px'><img style='width:50px;height:50px;float:right' src='"+homePath+"/crawler/"+value.img+"!w50x50.jpg'/><br><span style='width:100%;dispaly:block;'>"+value.text+"</span></td>";
												}else{
													var radio = "<label style='width: 100%; height: 70%; display: block; margin: 30% 0px;' for="+ ipvList[j].vid +"><input style='float:left;height:50px' type='radio' onclick='searchSkuWeight(this)' id="+ipvList[j].vid+" name='"+ ipvList[j].pid +"' value='"+ ipvList[j].vid +"'><img style='width:50px;height:50px;float:right' src='"+homePath+"/crawler/"+value.img+"!w50x50.jpg'/><br><span style='width:100%;dispaly:block;'>"+value.text +"</span></input>";
													_td = _td +("<td  style='width:100px;height:120px'>" + radio +"</td></label>");								
												}
											}
										}else{
											for(var j = 0; j < itemPropertyDtoList[i].listItemPropertyValueEntity.length;j++){		
												if(flag){
													_td=_td+"<td style='width:80px;height:120px'>"+ipvList[j].value+"</td>";
												}else{
													var radio = "<label style='width: 100%; height: 100%;' for="+ ipvList[j].vid +"><input   type='radio' onclick='searchSkuWeight(this)' id="+ipvList[j].vid+" name='"+ ipvList[j].pid +"' value='"+ ipvList[j].vid +"'>" + ipvList[j].value +"</input>";
													_td = _td +("<td  style='width:100px;height:80px'>" + radio +"</td></label>");							
												}
											}
										}								
									_tr = _tr + _td + "<td/></tr>"; 
									if(flag){
										skuTable.append(_tr);
										_th="<tr><td style='width:180px;'>设置重量(g)</td></tr>"
										_tr="<tr><td>官网参考重量:"+record.crawler_weight+"<br><input style='width:60px;' type='text' value='"+item.weight+"'/><input style='display:none;' type='text' value='"+record.itemId+"'/><input type='button' value='提交' onclick='setItemWeight(this)'/></td></tr>"
										skuTable.append(_th);
										skuTable.append(_tr);
										flag=undefined;
									}else{
										skuTable.append(_tr);
										flag=undefined;									
									}									
								}
							}else{	
									
									$.ajax({	
										type:'GET',
										url:basePath+"/item/getItemById.htm?itemId="+record.itemId,
										dataType:'json',
										success:function(item){
											$("#skuTable").empty();
											var _tr="";
											var _td="";
											_th="<tr><td style='width:100px;'>商店名</td><td>商品名</td><td style='width:180px;'>设置重量(g)</td></tr>"
											_tr="<tr><td>"+getShopsName(record.shopId)+"</td><td><a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+record.url+"')>"+record.itemName+
											"</a></td>"+"<td>官网参考重量:"+record.crawlerWeight+"<br><input style='width:60px;' type='text' value='"+item.weight+"'/><input style='display:none;' type='text' value='"+record.itemId+"'/><input type='button' value='提交' onclick='setItemWeight(this)'/></td></tr>"
											skuTable.append(_th);
											skuTable.append(_tr);
											$('#setSkuTable').hide();				
										},
										error:function(){
											$.messager.alert("提示信息","设置失败,请重试");
										}
									});
									
										
							}	
						},  
						error: function(){  
							return;  
						}  
					});
			}
		})
		
	//jquery easyui相关的数据只能放在document.ready 的{} 里面才有效果
	$("#shopId").combobox({
		 onSelect:function(data){
			itemIdQuery = data.shopId;
			searchSkuWeight();
		 }
	 });
	 
	/*  $("#itemId").combobox({
		 onSelect:function(data){
			itemIdQuery = data.itemId;
			searchSkuWeight();
		 }
	 }); */
		
	$('#searchTitle').textbox('setValue',"");
		
	
});
	//显示sk2，隐藏product
	function showSk2(){
		$("#flowSk2").show();
	    $("#flowProduct").hide();
	}
	//隐藏sk2，显示product
	function showProduct(){
		$("#flowSk2").hide();	
	    $("#flowProduct").show();
	}
	
function jumpTo(url){
	window.open(url);
}

	/**
	*添加产品,打开窗口
	*/
	function addProduct(){
		//能确定哪一个是字目录。然后根据子目录来读取选中的行。
		var row = $('#'+lastGrid+'').datagrid('getSelected');
		if(row){
			url = basePath + "/product/add.htm";
			$('#productDialog').dialog('open').dialog('setTitle',  '添加产品信息');
			$('#productForm').form('clear');
			$('#cid').textbox('setValue',row.cid);
			//加载品牌list
			$('#brandId').combobox({ 
				url:basePath + "/category/selectProductBrand.htm?cid="+ row.cid,
				valueField:'brandId', 
				textField:'name' 
			});
			
		}else{
			$.messager.alert("提示信息","请选择子目录记录");
		}

	}
	
	/**
	*编辑
	*/
	function editProduct(){
		var row = $('#productListGrid').datagrid('getSelected');
		if (row){
			url = basePath + "/product/update.htm?productId=" + row.productId;
			$('#productDialog').dialog('open').dialog('setTitle',  '修改产品信息');
			$('#productForm').form('clear');
			setFormValue('productForm',row);
			
			
			//加载品牌list
			$('#brandId').combobox({ 
				url:basePath + "/category/selectProductBrand.htm?cid="+ row.cid,
				valueField:'brandId', 
				textField:'name' 
			});
			$('#brandId').combobox('setValue',row.brandId);
		}else{
			$.messager.alert("提示信息","请选择一条记录");
		}
		
	}

	/**
	* 提交product form页面
	*/
	function save(){
		//将产品title进行utf8编码 防止特殊格式
		var titileInUTF8= encodeURIComponent($('#title').textbox('getValue'));
		$('#title').textbox('setValue',titileInUTF8);
		$('#productForm').form('submit',{
			url: url,
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				$.messager.alert("提示信息：", result);  
				$('#productDialog').dialog('close');
		  	    $('#productListGrid').datagrid('reload');
			}
		});
	}
	
	/**
	*删除产品,需要更新商品表的Product_id 字段。
	* 即：删除了产品，那么商品和这个被删除的产品的关联关系要去掉
	*/
	function deleteProduct(){
		   var row = $('#productListGrid').datagrid('getSelected');
		if (row){
			$.messager.confirm('重要信息','确认删除?',function(r){
				if (r){ 
					$.ajax({
						type:"POST",
						url:basePath + "/product/deleteProductById.htm?productId=" + row.productId,
						success:function(result){
							if(result = 'success'){
								$.messager.alert("提示信息：", "删除成功"); 
								$('#productListGrid').datagrid('reload');
							}else{
								$.messager.alert("提示信息：", "删除失败"); 
							}						
						}
					});
				}
				});
		}else{
			 $.messager.alert('提示信息','请选择一条记录');
		}
	}

/**
 * 给产品配置商品
 */
function assignGoodsToProduct(){
	var row = $('#productListGrid').datagrid('getSelected');
	
		//$("#assignGoodsToProductWin").dialog({title:'给产品 ' + row.title+'  配置商品'});
		if(row){
		   productId = row.productId;//点击配置商品的时候，拿到选中的产品id
		}
		
		
		
	   /**
		* 已有的商品列表
		*/
		$('#assignedGoodsGrid').datagrid({
			pagination:true,
			rownumbers:true, 
			fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
			striped : true, //设置为true将交替显示行背景。
			title:"已分配商品列表",
			height:350,
			fit : true,
			loadMsg:"数据正在加载中，请稍后",
			method : "POST",
			pageList:[20,50,80],
			queryParams: {
				 productId: productId
			},
			url: basePath + '/item/assignedProductItemlist.htm',
			columns:[[
					  {field:"ck",checkbox:true},//有这个属性了，才有选择框出来
					  {field:'id',hidden:true}, 
					  {field:'itemName',title:'商品名称',width:100,align:'center'}
				  ]],
			toolbar: [{
				text:'删除',
				iconCls:'icon-remove',
				handler: function(){
					var selectItemIds = $('#assignedGoodsGrid').datagrid('getSelections');
					var itemIds = [];
					for(var i = 0; i < selectItemIds.length; i++){
						itemIds.push(selectItemIds[i].itemId);
					}
					itemIds.join("&");
					removeProductItems(itemIds);
				}
			},{
				text:'<input type="text" id="itemname"/>',
			},{
			   id:'search',
			   text:'搜索',
			   iconCls:'icon-search', 
			   handler:function(){					  
					 selectitems(productId);
				}			
			}]	
		});
	
}



//设置产品重量方式  开始
function setProductWeight(){
	//url = basePath + "/addProduct.htm";
	$('#productWeightDialog').dialog('open').dialog('setTitle',  '设置重量信息');
	$('#productWeightForm').form('clear');
	$("#skuTable").empty(); //清空table
		
}

//设置商品item表重量
function setItemWeight(obj){
	var itemId=$(obj).prev().val();
	var itemWeight=$(obj).prev().prev().val();
	//alert(itemId+" ~~~"+itemWeight);
	$.ajax({
		   type: "POST",
		   url: basePath + '/item/setItemWeight.htm',
		   data:"itemId=" + itemId+"&weight=" + itemWeight,
		   success: function(msg){	   		 
				 $.messager.alert("提示信息",msg);
				  
		   }
	});	
}

