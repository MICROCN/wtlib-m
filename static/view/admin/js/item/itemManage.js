var url;
var cid = 0;
var imagesPath = homePath + '/admin/images';
$(function (){
	$('#itemGrid').datagrid({
	    url:basePath + "/item/getItemProduct.htm",
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true,
        nowrap:false,		
        method : "get", 
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
		pageList:[20,50,80],
        columns:[[
				  {field:'url',title:'url',width:20,align:'center',formatter: function(value,row,index){
							if(row.image_uris!=null){
								return "<img style='width:50px;height:50px' src='"+homePath+"/crawler/"+row.image_uris+"!w50x50.jpg'/>";
							}
		        	  }},
				  
		          {field:'item',title:'商品',width:200,align:'left',formatter: function(value,row,index){
			        		  return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+row.url+"')>"+row.item_name+"</a>  ";
		        	  }},
				  {field:'operation',width:30,align:'center',
			        	  formatter: function(value,row,index){
							  if(row.status=='y'){
								  return "<img style='width:90px; height:25px' onclick=javascript:frozenItem('"+row.item_id+"') src='"+imagesPath+"/systemImage/frozen.png'>";
							  }else if(row.status=='f'){
								  return "<img style='width:90px; height:25px' onclick=javascript:unFrozenItem('"+row.item_id+"') src='"+imagesPath+"/systemImage/unfrozen.png'>";
							  }
			        		  
		        	}},
				  {field:'title',title:'对应产品',width:100,align:'center',formatter: function(value,row,index){
						  
			        		  return row.title;
						 
		        	  }}
	          ]],
		toolbar:"#searchtb"
	});
	//加载下拉框
	loadCategoryCombo();
});


function jumpTo(url){
	window.open(url);
}

//冻结
function frozenItem(itemId){
	$.ajax({
            type:"POST",
            url:basePath + "/item/frozenItem.htm",
            data:"itemId="+itemId+"&status=f",
            success:function(msg){
				if(msg.indexOf('操作成功')>0){
					$.messager.alert("提示信息","冻结成功");
					$('#itemGrid').datagrid('reload');
				}else{
					$.messager.alert("提示信息","冻结失败");
				}
             }
       });
}

//取消冻结
function unFrozenItem(itemId){
	$.ajax({
            type:"POST",
            url:basePath + "/item/frozenItem.htm",
            data:"itemId="+itemId+"&status=y",
            success:function(msg){
				if(msg.indexOf('操作成功')>0){
					$.messager.alert("提示信息","取消冻结成功");
					$('#itemGrid').datagrid('reload');
				}else{
					$.messager.alert("提示信息","取消冻结失败");
				}
             }
       });
}

function doSearch(){
	$('#itemGrid').datagrid('load',{    
		cid: cid,
		status:$('#status').combobox('getValue'),
		url:$('#source_url').textbox('getValue'),
		itemName:$('#itemName').val()
    });
}

function doClear(){
	$("#searchFormId").form("clear");
	$('#itemGrid').datagrid('load',{    
		//cid: 0,
		//status:$('#status').combobox('getValue'),
		//url:$('#source_url').textbox('getValue')
    }); 
}

function loadCategoryCombo(){
	$('#categroy1').combobox({
		url:basePath + "/queryCategories.htm?parentCid=0&type=1",
		method : "get",
		valueField:'cid', 
		textField:'name',
		onSelect:function(row){
			cid = row.cid;
			loadUrl(cid);
			$('#categroy3').combobox('clear');
			$('#categroy4').combobox('clear');
			$('#categroy5').combobox('clear');
			$('#categroy2').combobox({
				url:basePath + "/queryCategories.htm?parentCid="+cid+"&type=1",
				method : "get",
				valueField:'cid', 
				textField:'name',
				onSelect:function(row){
					cid = row.cid;
					loadUrl(cid);
					$('#categroy4').combobox('clear');
					$('#categroy5').combobox('clear');
					$('#categroy3').combobox({
						url:basePath + "/queryCategories.htm?parentCid="+row.cid+"&type=1",
						method : "get",
						valueField:'cid', 
						textField:'name',
						onSelect:function(row){
							cid = row.cid;
							loadUrl(cid);
							$('#categroy5').combobox('clear');
							$('#categroy4').combobox({
								url:basePath + "/queryCategories.htm?parentCid="+row.cid+"&type=1",
								method : "get",
								valueField:'cid', 
								textField:'name',
								onSelect:function(row){
									cid = row.cid;
									loadUrl(cid);
									$('#categroy5').combobox({
										url:basePath + "/queryCategories.htm?parentCid="+row.cid+"&type=1",
										method : "get",
										valueField:'cid', 
										textField:'name',
										onSelect:function(row){
											cid = row.cid;
											loadUrl(cid);
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
	loadUrl(cid);
}

function loadUrl(cid1){
	$('#source_url').combobox({
			url:basePath + "/category/queryCategoryURL.htm?cid="+cid1,
			method : "get",
			valueField:'url', 
			textField:'url'
	})
}