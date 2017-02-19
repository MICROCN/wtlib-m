var url;
var imagesPath = homePath + '/admin/images';
$(function (){	
		
	$('#brandListGrid').datagrid({
		url:basePath  + "/brandList.htm",
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true, 
        method : "get", 
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:false,
        fit:true,
	    pageList:[20,50,80],
        columns:[[
                  {field:"ck",checkbox:true},
		          {field:'id',hidden:true},
		          {field:'fieldvalue',title:'品牌图片',width:50,align:'left',formatter:function(value,row,index){
						if(row.logo != null){
							return "<img style='width:50px;height:50px' src='"+ homePath +"/"+row.logo+"!w50x50.jpg'/>";
						}
		          		
		           	}
		      	  },
		          {field:'name',title:'品牌名称',width:100,align:'center'},
		          {field:'country',title:'国家地区',width:100,align:'center',formatter:function(value,row,index){
		          	    if(1==row.country){
	  	   							return "日本";
	  	   						}else if(2==row.country){
	  	   							return "韩国";
	  	   						}else if(3==row.country){
	  	   							return "美国";
	  	   						}else if(4==row.country){
	  	   							return "英国";
	  	   						}else if(5==row.country){
	  	   							return "西班牙";
	  	   						}else if(6==row.country){
	  	   							return "意大利";
	  	   						}else if(7==row.country){
	  	   							return "法国";
	  	   						}else if(8==row.country){
	  	   							return "德国";
	  	   						}else if(9==row.country){
	  	   							return "瑞士";
	  	   						}else if(10==row.country){
	  	   							return "澳洲";
	  	   						}else if(11==row.country){
	  	   							return "新西兰";
	  	   						}else if(12==row.country){
	  	   							return "台湾";
	  	   						}else if(13==row.country){
	  	   							return "瑞典";
	  	   						}else if(14==row.country){
	  	   							return "以色列";
	  	   						}else if(15==row.country){
	  	   							return "保加利亚";
	  	   						}else if(16==row.country){
	  	   							return "丹麦";
	  	   						}else if(17==row.country){
	  	   							return "捷克";
	  	   						}else if(18==row.country){
	  	   							return "泰国";
	  	   						}else if(19==row.country){
	  	   							return "印度尼西亚";
	  	   						}else if(20==row.country){
	  	   							return "加拿大";
	  	   						}else if(21==row.country){
	  	   							return "波兰";
	  	   						}else if(22==row.country){
	  	   							return "奥地利";
	  	   						}else if(23==row.country){
	  	   							return "斯里兰卡";
	  	   						}else if(24==row.country){
	  	   							return "拉脱维亚";
	  	   						}else if(25==row.country){
	  	   							return "香港";
	  	   						}else if(26==row.country){
	  	   							return "挪威";
	  	   						}else if(27==row.country){
	  	   							return "芬兰";
	  	   						}else if(28==row.country){
	  	   							return "巴西";
	  	   						}else if(29==row.country){
	  	   							return "南非";
	  	   						}else if(30==row.country){
	  	   							return "波利尼西亚";
	  	   						}else if(31==row.country){
	  	   							return "荷兰";
	  	   						}else if(32==row.country){
	  	   							return "比利";
	  	   						}else if(34==row.country){
	  	   							return "俄罗斯";
	  	   						}else if(33==row.country){
	  	   							return "不明";
	  	   						}
		          }}
		          
	          ]],
	     toolbar:'#brandToolbar'
	});
	
	
});

/**
	*添加品牌,打开窗口
	*/
	function addBrand(){
		url = basePath + "/addBrand.htm";
		$('#brandDialog').dialog('open').dialog('setTitle',  '添加品牌信息');
		$('#brandForm').form('clear');

	}
	
	/**
	* brand form页面
	*/
	function saveBrand(){
		$('#brandForm').form('submit',{
			url: url,
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(msg){
					alert(msg); 
					$('#brandDialog').dialog('close');
					$('#brandListGrid').datagrid('reload'); 	
			}
		})
	}
	
	
	/**
	*编辑
	*/
	function edit(){
		var row = $('#brandListGrid').datagrid('getSelected');
		if (row){
			url = basePath + "/updateBrand.htm?brandId=" + row.brandId + "&logoUrlUpdate=" + row.logo;
			var row = $('#brandListGrid').datagrid('getSelections');
			if (row.length>1) {
					$.messager.alert("提示信息","一次只能修改一条记录");
			}else{
				  row = $('#brandListGrid').datagrid('getSelected');
					$('#brandDialog').dialog('open').dialog('setTitle','修改');
					$('#brandForm').form('clear');
					setFormValue('brandForm',row);
					$('#country').combobox('setValue',row.country);
				}
		}else{
			$.messager.alert("提示信息","请选择一条记录");
		}
		
	}
	
	

/**
*删除
*/
function deleteBrand(){
	var row = $('#brandListGrid').datagrid('getSelections');
	var brandIds = [];
	for(var i = 0; i < row.length; i++){
		brandIds.push(row[i].brandId);
	}
	brandIds.join("&");
	 if (row){
		 $.ajax({
			type:"POST",
			url:basePath + "/deleteBrand.htm",
			data: "brandId="+brandIds,
			dataType:'json',
			success:function(result){
				$.messager.alert("提示信息：", "成功删除 <font color='green'>" + result.deletedRows + " </font>行"); 
				$('#brandListGrid').datagrid('reload');
			}
		});
		 
				
    }else{
    	  $.messager.alert('提示信息','请选择一条记录'); 
    } 
}

 function doSearch(){	    	
	    	$('#brandListGrid').datagrid('reload',{
	    		name: $('#names').val()
			});
			
    }	