var url;
//当前选中的grid
var cur_level;

var imagesPath = homePath + '/admin/images';
$(function (){
	
	$('#numLimit').hide();
	
	$('#grid1').datagrid({
	    url:basePath + "/queryCategories.htm?parentCid=0&type=1",
        fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
        striped : true, //设置为true将交替显示行背景。
        method : "get",
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
		pageList:[20,50,80],
        height:450,
        columns:[[
		          {field:'name',title:'一级类目',width:100,align:'center'}
	          ]],
	    toolbar:[{text:"一级类目"},'-',{text:"",iconCls:"icon-add",handler:function(){ 
					  				addCategory("添加一级类目",0,1,'grid0');
				  	}}, {text:"",iconCls:"icon-edit",handler:function(){ 
					  				updateCategory("修改一级类目",'grid1');
				  	}}, {text:"",iconCls:"icon-remove",handler:function(){ 
					  				deleteCategory("删除一级类目",'grid1');
				  	}}
	           ],
		view: detailview,
		detailFormatter:function(index,row){
			return "cid:"+row.cid;
		},
		onExpendRow:function(index,row){
			return "cid:"+row.cid;
		},
	    onSelect:function(){
	    	cur_level = 1;
	    	//清空3,4,5表
			$('#numLimit').show();
			//根据cid查询出本类目的子类目的数量限制
						
			
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
	    	//根据pid加载下一级的类目
	    	var row = $('#grid1').datagrid('getSelected');
			getCategoryNumLimit(row.cid);
			showProperty();
	    	$('#grid2').datagrid({
	    		url:basePath + "/queryCategories.htm?parentCid="+ row.cid+"&type=1",
	            fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	            striped : true, //设置为true将交替显示行背景。
	            method : "get",
	            loadMsg:"数据正在加载中，请稍后",
	            singleSelect:true,
	            fit:true,
				pageList:[20,50,80],
	            height:450,
	            columns:[[
	    		          {field:'name',title:'二级类目',width:100,align:'center'}
	    	          ]],
	            toolbar:[{text:"二级类目"},'-',
						{text:"",iconCls:"icon-add",handler:function(){ 
							addCategory("添加二级类目",row.cid,2,'grid1');
							}}, {text:"",iconCls:"icon-edit",handler:function(){ 
					  				updateCategory("修改二级类目",'grid2');
				  	}}, {text:"",iconCls:"icon-remove",handler:function(){ 
					  				deleteCategory("删除二级类目",'grid2');
				  	}}
	                     ],
				view: detailview,
				detailFormatter:function(index,row){
					return "cid:"+row.cid;
				},
				onExpendRow:function(index,row){
					return "cid:"+row.cid;
				},
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
					getCategoryNumLimit(second_row.cid);
					showProperty();
	    	    	$('#grid3').datagrid({
	    	    		url:basePath + "/queryCategories.htm?parentCid="+ second_row.cid+"&type=1",
	    	            fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	    	            striped : true, //设置为true将交替显示行背景。
	    	            method : "get",
	    	            loadMsg:"数据正在加载中，请稍后",
	    	            singleSelect:true,
	    	            fit:true,
						pageList:[20,50,80],
	    	            height:450,
	    	            columns:[[
	    	    		          {field:'name',title:'三级类目',width:100,align:'center'}
	    	    	          ]],
	    	    	    toolbar:[{text:"三级类目"},'-',
	    	    	 		      {text:"",iconCls:"icon-add",handler:function(){ 
	    	    	 		    	 addCategory("添加三级类目",second_row.cid,3,'grid2');
	    	    	 			  	}}, {text:"",iconCls:"icon-edit",handler:function(){ 
					  						 updateCategory("修改三级类目",'grid3');
				  							}}, {text:"",iconCls:"icon-remove",handler:function(){ 
					  						 deleteCategory("删除三级类目",'grid3');
				  							}}
	    	    	 	       ],
						view: detailview,
						detailFormatter:function(index,row){
							return "cid:"+row.cid;
						},
						onExpendRow:function(index,row){
							return "cid:"+row.cid;
						},
	    	            onSelect:function(){
	    	            	cur_level = 3;
	    	            	//清空5表
	    	    	    	var fifth_data=$('#grid5').datagrid('getData');
	    	    	    	if(fifth_data.rows.length>0){
	    	    	    		$('#grid5').datagrid('loadData',{total:0,rows:[]});
	    	    	    	}
	    	    	    	var third_row = $('#grid3').datagrid('getSelected');
							getCategoryNumLimit(third_row.cid);
							showProperty();
	    	    	    	$('#grid4').datagrid({
	    	    	    		url:basePath + "/queryCategories.htm?parentCid="+ third_row.cid+"&type=1",
	    	    	            fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	    	    	            striped : true, //设置为true将交替显示行背景。
	    	    	            method : "get",
	    	    	            loadMsg:"数据正在加载中，请稍后",
	    	    	            singleSelect:true,
	    	    	            fit:true,
								pageList:[20,50,80],
	    	    	            height:450,
	    	    	            columns:[[
	    	    	    		          {field:'name',title:'四级类目',width:100,align:'center'}
	    	    	    	            ]],
	    	    	    	    toolbar:[{text:"四级类目"},'-',
	    	 	    	    	 		  {text:"",iconCls:"icon-add",handler:function(){ 
	    	 	    	    	 				addCategory("添加四级类目",third_row.cid,4,'grid3');
	    	 	    	    	 		  }}, 
	    	 	    	    	 		  {text:"",iconCls:"icon-edit",handler:function(){ 
					  									updateCategory("修改四级类目",'grid4');
				  									}}, {text:"",iconCls:"icon-remove",handler:function(){ 
					  									deleteCategory("删除四级类目",'grid4');
				  									}}
	    	 	    	    	 	    ],
								view: detailview,
								detailFormatter:function(index,row){
									return "cid:"+row.cid;
								},
								onExpendRow:function(index,row){
									return "cid:"+row.cid;
								},
	    	    	            onSelect:function(){
	    	    	            	cur_level = 4;
	    	    	    	    	var fourth_row = $('#grid4').datagrid('getSelected');
									getCategoryNumLimit(fourth_row.cid);
									showProperty();
	    	    	    	    	$('#grid5').datagrid({
	    	    	    	    		url:basePath + "/queryCategories.htm?parentCid="+ fourth_row.cid+"&type=1",
	    	    	    	            fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	    	    	    	            striped : true, //设置为true将交替显示行背景。
	    	    	    	            method : "get",
	    	    	    	            loadMsg:"数据正在加载中，请稍后",
	    	    	    	            singleSelect:true,
	    	    	    	            fit:true,
										pageList:[20,50,80],
	    	    	    	            height:450,
	    	    	    	            columns:[[
	    	    	    	    		          {field:'name',title:'五级类目',width:100,align:'center'}
	    	    	    	    	          ]],
	    	    	    	    	    toolbar:[{text:"五级类目"},'-',
	    	    	 	    	    	 		  {text:"",iconCls:"icon-add",handler:function(){ 
	    	    	 	    	    	 			 	addCategory("添加五级类目",fourth_row.cid,5,'grid4');
	    	    	 	    	    	 		  }},
	    	    	 	    	    	 		  {text:"",iconCls:"icon-edit",handler:function(){ 
					  											 	updateCategory("修改五级类目",'grid5');
				  												}}, 
				  												{text:"",iconCls:"icon-remove",handler:function(){ 
					  												deleteCategory("删除五级类目",'grid5');
				  												}}
	    	    	 	    	    	 	    ],
	    	    	 	    	    	onSelect:function(){
	    	  	    	    	            	cur_level = 5;
												var fifth_row = $('#grid5').datagrid('getSelected');
												showProperty();
												getCategoryNumLimit(fifth_row.cid);	
	    	  	    	    	           },
										view: detailview,
										detailFormatter:function(index,row){
											return "cid:"+row.cid;
										},
										onExpendRow:function(index,row){
											return "cid:"+row.cid;
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
	
	
	$('#siteId').combobox({
		url: basePath + '/category/querySiteList.htm',
		method : "get",
		valueField:'siteId', 
		textField:'siteName'
	});
	
});

function addCategory(title,cid,level,gridId){
	if(gridId=="grid0"){
		$('#addCategoryDialog').dialog('open').dialog('setTitle',title);
	$('#addCategoryForm').form('clear');
	$('#parentCid').textbox('setValue',cid);
	$('#level').textbox('setValue',level);
	url = basePath+"/category/insertCategory.htm";
	}else{
		var row = $('#'+gridId).datagrid('getSelected');
		if(row){
		$('#addCategoryDialog').dialog('open').dialog('setTitle',title);
		$('#addCategoryForm').form('clear');
		$('#parentCid').textbox('setValue',cid);
		$('#level').textbox('setValue',level);
		url = basePath+"/category/insertCategory.htm";
		}
		else{
			$.messager.alert("提示信息","添加失败");
		}
	}

}

function saveCategory(){
	$('#addCategoryForm').form('submit',{   
   		url:url,
   		onSubmit: function(){
               return $(this).form('validate');
           }, 
   	    success:function(data){
			var level = $('#level').textbox('getValue');
			$.messager.alert("提示信息：", data); 
			$('#addCategoryDialog').dialog('close');
			$('#grid'+level).datagrid('reload');
	   	    $('#orderCostDetailListGrid').datagrid('reload');
   	    },error: function(){
				$.messager.alert("提示信息","保存失败");
		}
   	});
}

function updateCategory(title,gridId){
	var row = $('#'+gridId).datagrid('getSelected');
	if (row){
		$('#addCategoryDialog').dialog('open').dialog('setTitle',title);
		$('#addCategoryForm').form('clear');
		setFormValue('addCategoryForm',row);
		url = basePath+"/category/updateCategory.htm?cid="+row.cid;
	}else{
		$.messager.alert("提示信息","请选择一条记录");
    }
}

function deleteCategory(title,gridId){
	var row = $('#'+gridId).datagrid('getSelected');
	var next_level=5;
	if(cur_level != 5){
		next_level = cur_level+1;
	}
	$("#seeImage").html("");
	var data=$('#grid'+next_level).datagrid('getData');
	if (row){		
		$.ajax({
				type: "POST",
				url: basePath + "/selectProductByCategory.htm?cid=" + row.cid,
				dataType:'json',
				success: function(msg){
					if(msg.result == 1 && data.rows.length<=0 || cur_level==5){
		                $.messager.confirm("提示信息","确认删除?",function(r){
							if (r){ 
							   $.ajax({
								   type:"POST",
								   url:url = basePath+"/category/deleteCategory.htm",
								   data:"cid="+row.cid,
								   success:function(msg){
									if(msg.indexOf('操作成功')>0){
										$.messager.alert("提示信息","删除成功");
										$('#'+gridId).datagrid('reload');
									}else{
										$.messager.alert("提示信息","删除失败；请检查该类目下有没有产品");
									}
									}
								});
							}	                 
						}); 
		            }else{
						$.messager.alert("提示信息","不能删除");	
					}
					
				   }
				});
	}else{
		$.messager.alert("提示信息","请选择一条记录");
    }				
}

/**
 * 显示属性
 */
function showProperty(){
	var next_level=5;
	if(cur_level != 5){
		next_level = cur_level+1;
	}
	$("#seeImage").html("");
	var data=$('#grid'+next_level).datagrid('getData');
	
		var row = $('#grid'+cur_level).datagrid('getSelected');
		$('#urlGrid').datagrid({
			url:basePath + "/category/queryCategoryURL.htm?cid="+ row.cid,
	        fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	        striped : true, //设置为true将交替显示行背景。
	        loadMsg:"数据正在加载中，请稍后",
	        singleSelect:true,
	        fit:true,
	        height:250,
			nowrap:false,
			pageList:[20,50,80],
	        columns:[[
					  {field:'url',width:200,align:'left'},
			          {field:'site_name',width:20,align:'center'},
			          //{field:'vid',width:100,align:'center'},
			          {field:'operation',width:20,align:'center',
			        	  formatter: function(value,row,index){
			        		  return "<img style='width:17px; height:17px' onclick='deleteCategoryURL("+row.vid+")' src='"+imagesPath+"/systemImage/delete.png'>";
		        	  }}
		          ]]
		});
		
		$('#brandGrid').datagrid({
			url:basePath + "/category/queryCategoryBrand.htm?cid="+ row.cid,
	        fitColumns:true,  //设置为true将自动使列适应表格宽度以防止出现水平滚动。
	        striped : true, //设置为true将交替显示行背景。
			pagination:true,
	        loadMsg:"数据正在加载中，请稍后",
	        singleSelect:true,
	        fit:true,
	        title:"所含品牌",
	        height:250,
			pageList:[20,50,80],
	        columns:[[
			          {field:'name',width:100,align:'center'},
			          {field:'operation',width:100,align:'center',
			        	  formatter: function(value,row,index){
			        		  return "<img style='width:17px; height:17px' onclick='deleteCategoryBrand("+row.vid+")' src='"+imagesPath+"/systemImage/delete.png'>";
		        	  }}
		          ]],
		    onSelect:function(value,row,index){
		    	showLogo(row.brand_id,row.logo);
		    }
		});
	
	
	$('#allBrandGrid').datagrid({
    		url:basePath  + "/brandList.htm",
    		fitColumns:true,  
    		striped : true,
			  pagination:true,
    		rownumbers:true, 
    		method : "get", 
    		loadMsg:"数据正在加载中，请稍后",
    		singleSelect:true,
    		title:"品牌库",
    		fit:true,
		   	pageList:[20,50,80],
    		columns:[[
	          		{field:'brandId',hidden:true},
	          		{field:'name',title:'品牌名称',width:100,align:'center'},
	          		{field:'operation',width:10,align:'center',
			        	  formatter: function(value,row,index){
			        		  return "<img style='width:17px; height:17px' onclick='addCategoryBrand("+row.brandId+")' src='"+imagesPath+"/systemImage/add.png'>";
		        	}}
          	]]
	});
}

function addCategoryURL(){
	var row = $('#grid'+cur_level).datagrid('getSelected');
	$('#cid').textbox('setValue',row.cid);
	$('#addURLForm').form('submit',{   
   		url:basePath+"/category/insertCategoryURL.htm",
   		onSubmit: function(){
               return $(this).form('validate');
           }, 
   	    success:function(data){
   	    		if(data.indexOf('操作成功')>0)
   	    		{
   	    			$.messager.alert("提示信息","保存成功");
   	    			$('#urlGrid').datagrid('reload');
   	    			$('#addURLForm').form('clear');
   	    		}else{
   	    			$.messager.alert("提示信息","保存失败");
   	    		}
   	    }
   	});
}

function deleteCategoryURL(vid){
	$.messager.confirm("提示信息","确认删除?",function(r){
        if (r){ 
           $.ajax({
        	   type:"POST",
        	   url:url = basePath+"/category/deleteCategoryURL.htm",
        	   data:"vid="+vid,
        	   success:function(msg){
        		if(msg.indexOf('操作成功')>0){
        		  	$.messager.alert("提示信息","删除成功");
        		  	$('#urlGrid').datagrid('reload');
        		}else{
        			$.messager.alert("提示信息","删除失败");
        		}
        	   }
           });
        }	                 
    });
}

/**
 * 品牌CRUD
 */
function addCategoryBrand(brandId){
	var row = $('#grid'+cur_level).datagrid('getSelected');
	var next_level=5;
	if(cur_level != 5){
		next_level = cur_level+1;
	}
	$("#seeImage").html("");
	var data=$('#grid'+next_level).datagrid('getData');
	$.messager.confirm("提示信息","确认对应到所含品牌中?",function(r){
        if (r){ 
           $.ajax({
        	   type:"POST",
        	   url:url = basePath+"/category/insertCategoryBrand.htm",
        	   data:"cid="+row.cid+"&brandId="+brandId,
			   dataType:'json',
        	   success:function(msg){
			   if(data.rows.length<=0 || cur_level==5){
        		if(msg.message == 0){
        		  	$.messager.alert("提示信息","操作成功");
        		  	$('#brandGrid').datagrid('reload');
        		}else if(msg.message == 1){
				    $.messager.alert("提示信息","所含品牌已存在");
				    $('#brandGrid').datagrid('reload');
				}else{
        			$.messager.alert("提示信息","操作失败");
					$('#brandGrid').datagrid('reload');
        		}
			   }else{
						$.messager.alert("提示信息","不能添加");	
					}
        	   }
           });
        }	                 
    });
}

function deleteCategoryBrand(vid){
	$.messager.confirm("提示信息","确认删除?",function(r){
        if (r){ 
           $.ajax({
        	   type:"POST",
        	   url:url = basePath+"/category/deleteCategoryURL.htm",
        	   data:"vid="+vid,
        	   success:function(msg){
        		if(msg.indexOf('操作成功')>0){
        		  	$.messager.alert("提示信息","删除成功");
        		  	$('#brandGrid').datagrid('reload');
        		}else{
        			$.messager.alert("提示信息","删除失败");
        		}
        	   }
           });
        }	                 
    });
}

function showLogo(brandId,logo){
	if(logo != null){
		//version避免缓存
		var version = new Date().getTime();
		$("#seeImage").html("<img onclick='updateLogo("+brandId+")' src='"+ homePath +"/"+logo+"?version="+version+"'/>");
	}
}
function showNewLogo(brandId){
	//version避免缓存
	var version = new Date().getTime();
	$("#seeImage").html("<img onclick='updateLogo("+brandId+")' src='"+basePath+"/brand/showBrandLogo.htm?brandId="+brandId+"&v="+version+"'/>");
}
function updateLogo(brandId){
	$('#logoDialog').dialog('open').dialog('setTitle',"品牌LOGO上传");
	$('#logoForm').form('clear');
	$('#brandId').textbox('setValue',brandId);
}

function saveLogo(){
	$('#logoForm').form('submit',{
		url: basePath+"/brand/updateBrandLogo.htm",
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(msg){
			if(msg.indexOf('操作成功')>0){
    		  	$.messager.alert("提示信息","上传成功");
    		  	$('#logoDialog').dialog('close');
				$('#brandGrid').datagrid('reload');
    		  	//var row = $('#brandGrid').datagrid('getSelected');
    			//showNewLogo(row.brand_id);
    		}else{
    			$.messager.alert("提示信息","上传失败");
    		}
		}
	})
}


//搜索
function brand_doSearch(){
	$('#allBrandGrid').datagrid('load',{    
		name: $('#brand_search').textbox("getValue")
    });  
}


//设置类目的数量限制
function setCategoryNumLimit(){
	var row = $('#grid'+cur_level).datagrid('getSelected');
	$('#cid1').textbox('setValue',row.cid);
	$('#setNumLimitForm').form('submit',{   
   		url:basePath+"/category/insertCategoryNumLimit.htm",
   		onSubmit: function(){
               return $(this).form('validate');
           }, 
   	    success:function(data){
   	    		if(data.indexOf('操作成功')>0)
   	    		{
   	    			$.messager.alert("提示信息","设置成功");
   	    			$('#urlGrid').datagrid('reload');
   	    			$('#addURLForm').form('clear');
   	    		}else{
   	    			$.messager.alert("提示信息","保存失败");
   	    		}
   	    }
   	});	
}
//根据cid查询出本类目的子类目的数量限制
function getCategoryNumLimit(cid){
	var row = $('#grid'+cur_level).datagrid('getSelected');
	$("#categoryName").html(row.name);
	$.ajax({
        	   type:"POST",
        	   url:url = basePath+"/category/getCategoryNumLimit.htm",
        	   data:"cid="+cid,
			   dataType:'json',
        	   success:function(msg){
				
        		  	$('#categoryNumLimit').textbox('setValue',msg);    		
        	   }
           });
} 



