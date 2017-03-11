var weightWay=0;//weightWay=1   产品重量设置   weightWay=2 根据sku分别设置  weightWay=3 全选模式
var popArrayS=new Array();
var itemsDatas=null;
//设置产品重量方式  开始
function setProductWeight(){
	var row = $('#productListGrid').datagrid('getSelected');
  	if (row){
  		productId = row.productId;
  		$.ajax({
  			type: "POST",
  			dataType:'json',
  			data:"productId=" + productId,
  			url:basePath + "/item/getItemsToSetWeight.htm",
  			success:function(items) {
				itemsDatas=items;
  				if(!(items.allHasSkus)){
  					 	$('#weightWay1').show();
  					 	$('#weightWay2').hide();
  					 	showProduct();
						$.ajax({
							type:"GET",
							url:basePath+"/product/getProductWeight.htm?productId="+productId,
							dataType:'json',
							success:function(pe){
								var weight=pe.weight;
								$('#weightProduct').textbox('setValue',weight);		
							}
						});
						//不能再vm文件上直接添加样式，否则创建页面的时候就会加载datagrid 出现bug
						$('#nonskuTable').attr('class','easyui-datagrid');
  					 	
						LoadnonskuTable(items);
						
						weightWay=1;
  					}else{
  							$('#weightWay2').show();
  							$('#weightWay1').hide();
  							showSk2();
							weightWay=2;
							$('#itemId').combobox({ 
							url:basePath  + "/item/getitemName.htm?productId="+ row.productId,
							valueField:'itemId', 
							textField:'itemName'
							
							});
							weightWay=2;
  					}
  			}
  		});
  		
		$('#productWeightDialog').dialog('open').dialog('setTitle',  '设置重量信息');
		$('#productWeightForm').form('clear');
		$("#skuTable").empty(); //清空sku表的信息
		$("#flowSk2").hide();
		$("#flowProduct").hide();
   	}else{
   		$.messager.alert("提示信息","请选择一条记录");
   	}
   }
   
//设置产品重量方式  结束
function saveProductWeight(){
	if(weightWay == 1){
		//仅仅保存产品重量
		$.ajax({
		   type: "POST",
		   url: basePath + '/product/setProductWeight.htm',
		   data:"productId=" + productId +" &weight=" + $('#weightProduct').textbox('getValue') ,
		   success: function(msg){
		   		 
				  $.messager.alert("提示信息",msg);
				  $('#productWeightDialog').dialog('close');
		   }
		});
	}else{		
		popArrayS==new Array();//清空SKU变量的数组
		$('#setSkuTable').css('display','none');
		$('#saveSkuWeightLink').next().remove();//清空 提示设置的信息
		$.messager.confirm('重要信息','确认已经完成重量设置?',function(r){
				if (r){ 
					$('#productWeightDialog').dialog('close');
				}
		});
	}
}


function saveSkuWeight(){	
	$('#saveSkuWeightLink').next().remove();//清空 设置的信息
	if(weightWay == 2){
		// 保存sku 重量
		var props = "";
		$('input:radio:checked').each(function(index,domEle){
			//写入代码
			props = props + domEle.name + ":" + domEle.value + ",";
		}); 
		props = props.substr(0,props.length - 1);
		//props = props + ';';
		var weight=$('#weight').textbox('getValue');
		var itemId = $("#itemId").textbox("getValue");
		if(props!=null && weight != null && weight != "" && props != ""){  //只有有sku的产品才能发送ajax请求进行保存
			 $.ajax({
			   type: "POST",
			   url: basePath + '/product/setProductSkuWeight.htm',
			   data:"props=" + props +" &weight=" + $('#weight').textbox('getValue') +"&itemId=" + itemId,
			   dataType:'json',
			   success: function(msg){
				   if(msg ==new String("success")){
					  $('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">保存成功</span>');   
				   }else{
					  $('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">保存失败！可能的原因:本商品的该sku组合不存在 或者数据异常</span>');    
					  $('#saveSkuWeightLink').attr('disabled',true);
				   }
					
				}
			});
			popArrayS==new Array();//清空SKU变量的数组
		}else{
			 $('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">请选择属性或者设置重量</span>');   
		}
		 
	}else if(weightWay == 3){
		if(popArrayS.length == 1){
			var props='';
			var itemId = $("#itemId").textbox("getValue");
			var isAllSuccess3=0;
			var weight=$('#weight').textbox('getValue');
			if(typeof(weight)!="undefined" && weight != null && weight != ""){
				for(var i=0;i<popArrayS[0].length;i++){	
					var itemId = $("#itemId").textbox("getValue");
					props=popArrayS[0][i];
					$.ajax({
						type: "POST",
						async: false,
						url: basePath + '/product/setProductSkuWeight.htm',
						data:"props=" + props +" &weight=" + $('#weight').textbox('getValue') +"&itemId=" + itemId,
						dataType:'json',
						success: function(msg){			
							 if(msg ==new String("success")){
								isAllSuccess3++;
							 }
						}
					});
				}		
				if(isAllSuccess3 == popArrayS[0].length){
					$('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">全部保存成功</span>');		
				}else{		
					$('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">有数据没有保存成功,重新保存 可能的原因:本商品的该sku组合不存在</span>');
					$('#saveSkuWeightLink').attr('disabled',true);
				}
				//清空SKU变量的数组
				popArrayS==new Array();//清空SKU变量的数组
			}else{	
				 $('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">请选择属性或者设置重量</span>');  
			}
			isAllSuccess3=undefined;	
		}else if(popArrayS.length == 2){//两行多选
			var props='';
			var itemId = $("#itemId").textbox("getValue");
			var isAllSuccess2=0;
			var weight=$('#weight').textbox('getValue');
			if(typeof(weight)!="undefined" && weight != null && weight != "" && popArrayS[0] != null && popArrayS[0] != "" && popArrayS[1] != null && popArrayS[1] != "" ){
				for(var i=0;i<popArrayS[0].length;i++){
					for(var j=0;j<popArrayS[1].length;j++){
						props=popArrayS[0][i]+","+popArrayS[1][j];	
						if(props != ","){
							$.ajax({
								type: "POST",
								async: false,
								url: basePath + '/product/setProductSkuWeight.htm',
								data:"props=" + props +" &weight=" + $('#weight').textbox('getValue') +"&itemId=" + itemId,
								dataType:'json',
								success: function(msg){	
									 if(msg ==new String("success")){
										isAllSuccess2++;	
									}
								}
							});
						}else{
							 $('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">请选择属性或者设置重量</span>');
						}		
					}			
				}
				if(isAllSuccess2 == (popArrayS[0].length*popArrayS[1].length)){
					$('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">全部保存成功</span>');		
				}else{		
					$('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">有数据没有保存成功,请重新保存</span>');
					$('#saveSkuWeightLink').attr('disabled',true);
				}
				//清空SKU变量的数组
				popArrayS==new Array();//清空SKU变量的数组
			}else{
				 $('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">请选择属性或者设置重量</span>');  
			}
			
			isAllSuccess2=undefined;
			
		}
	}	
}
/*
*查询产品sku重量
*/
function querySkuWeight(){
	var props = "";
	$('input:radio:checked').each(function(index,domEle){
		props = props + domEle.name + ":" + domEle.value + ",";
	}); 
	props = props.substr(0,props.length - 1);
	//props = props + ';';
	$.ajax({
		   type: "POST",
		   url:basePath + "/product/getProductSkuWeight.htm",
		   data:"props=" + props +"&shopId=" + $('#shopId').combobox('getValue') + "&productId=" + productId,
		   dataType: 'json',  
		   success: function(result){
			  //$('#productWeightDialog').dialog('close');
			  //$.messager.alert("提示信息",msg);
			  
			  if(result != null){
				$('#weight').textbox('setValue',result);
			  }else{
				  $('#weight').textbox('clear');
				  
			  }
		   }
	}); 
}



function searchSkuWeight(obj){
	var itemIdQuery = $("#itemId").textbox("getValue");
	var props = "";
	$('#saveSkuWeightLink').next().remove();//清空 的信息提示
	$('input:radio:checked').each(function(index,domEle){
		props = props + domEle.name + ":" + domEle.value + ",";
	}); 
	props = props.substr(0,props.length - 1);
	//如果指点了一下的话 就生成一个单独的 数组 
	var index=$(obj).closest('tr').find('td:first').attr('id');		
	var index=index.substr(index.indexOf('_')+1);
	var propSingle=$(obj).attr('name')+":"+$(obj).val();
	var propSingleArray=new Array();
	propSingleArray[0]=propSingle;
	popArrayS[index]=propSingleArray;
	
	
	

		$.ajax({
		   type: "POST",
		   url:basePath + "/product/getProductSkuWeight.htm",
		   data:"props=" + props +"&itemId=" + itemIdQuery,
		   dataType: 'json',  
		   success: function(result){
			  if(result != null){
				$('#weight').textbox('setValue',result.weight);
				$('#referenceWeight').html(result.crawler_weight+'g');
				$('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">查询成功</span>');  
			  }else{
				  $('#weight').textbox('clear');
				  $('#saveSkuWeightLink').after('&nbsp;&nbsp;&nbsp;<span style="color:red;">注意:本商品的 该sku组合 不存在!</span>');   
			  }
		   }
		});
	
	propLengthArray=null;//清空
	 
}

/**
	架子啊没有sku的列表
*/
function LoadnonskuTable(item){
		var shops=item.shops;
		var items=item.items;
		
		$('#nonskuTable').datagrid({
				rownumbers:true,
				fit:true,
				fitColumns:true,
				singleSelect:true,
				striped:true,
				columns:[[
					  {field:'shopId',title:'店铺',width:50,align:'center',formatter: function(value, row, index){
						  var  shopId=row.shopId;
						  if(shops != null){
							//获得商店id
							for(var i=0;i<shops.length;i++){
								if(shops[i].shopId==shopId){
									return shops[i].name;
								}
							}	  
							return "暂无商店";
						  }else{
							   return "暂无商店";
						  }
							
					  }},
					  {field:'itemName',title:'商品名称',width:100,align:'center',formatter: function(value,row,index){
							return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+row.url+"')>"+row.itemName+"</a>  ";
					  }},
					  {field:'weight',title:'重量(g)',width:100,align:'center',formatter: function(value,row,index){
							return "官网参考重量:"+row.crawlerWeight;
					  }}
					  
				  ]],	
			});
			$('#nonskuTable').datagrid('loadData', items);
		}
/*获得商店名*/		
function getShopsName(id){

	var shops=itemsDatas.shops;
	for(var i=0;i<shops.length;i++){
		if(shops[i].shopId==id){
			return shops[i].name;
		}
	}
	return "暂无商店";
}	



//全选获得pid+vid 的数组

function getPidAndVids(obj){
	$('#saveSkuWeightLink').next().remove();//清空 保存成功 的信息
	if($(obj).attr('checked')){	
		var index=$(obj).parent().attr('id');		
		var index=index.substr(index.indexOf('_')+1);
		var radios=$(obj).parent().nextAll("td").find("input");
		var popArray=new Array();
		for(var i=0;i<radios.length;i++){		
			var pop=$(radios[i]).attr('name')+':'+$(radios[i]).val();
			$(radios[i]).attr('disabled',true);
			$(radios[i]).attr('checked',false);
			popArray[i]=pop;
		}
		popArrayS[index]=popArray;	
		weightWay=3;
	}else{
		var radios=$(obj).parent().nextAll("td").find("input");
		var popArray=new Array();
		for(var i=0;i<radios.length;i++){		
			$(radios[i]).attr('disabled',false);
		}
		var index=$(obj).parent().attr('id');		
		var index=index.substr(index.indexOf('_')+1);
		popArrayS[index]=null;
		var flag=0;//flag用来表示null的个数  只要全部是null 就说明不是模式三
		for(var i=0;i<popArrayS.length;i++){
			if(popArrayS[i]==null || typeof(popArrayS[i]=="undefined")){
				flag++;
			}
		}
		if(flag == popArrayS.length){
			weightWay=2;	
		}else{
			weightWay=3;
		}
		
	}
}

