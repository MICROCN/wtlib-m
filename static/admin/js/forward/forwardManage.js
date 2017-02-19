var url;
var cid = 0;
var imagesPath = homePath + '/admin/images';
var cur_index;
var state = 0;
var flag;
var flag_zhongzhuan;
var flag_yizhongzhuang;
$(function (){
	allStrade();
	$('#state_0').click(function(){
		state = 0;
		allStrade();
	});
	$('#state_1').click(function(){
		state = 1;
		loadInDelivery();
	})
	$('#state_2').click(function(){
		state = 2;
		loadToForward();
	});
	$('#state_3').click(function(){
		state = 3;
		loadIsForward();
	});
	;
	$('#state_4').click(function(){
		state = 4;
		allStorage();
	});

});

//官网发货中
function loadInDelivery(){
	$('#tradeListGrid').datagrid({
	    url:basePath + "/forward/queryInDeliveryList.htm",
			queryParams:{
				state:1
			},
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true, 
        method : "get", 
		sortName: "gmt_accept",
		sortOrder: "desc",	
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
        pageList:[20,50,80],
        columns:[[
				  {field:'strade_id',title:'拼单号',width:50,align:'center'},
		          {field:'sharer_id',title:'接单大C',width:50,align:'center',formatter:function(value,row,index){
					  var shareId = $('#sharer').textbox('getValue');

					  if(flag==1 && shareId!=''){
							return "<font color='red'>"+ row.sharer_id +"</font>";
					  }else{
						    return row.sharer_id;
					  }
				  }},
				  {field:'gmt_accept',title:'配单时间',width:50,align:'center',formatter:formatterDate,sortable:true}
	          ]],
		toolbar:"#tradeManage_toolbar",
		/****************点击+号查看明细**************/
	    view: detailview,
        detailFormatter:function(index,row){
            	return '<div style="padding:2px"><table id="tradeDetailGrid-' + index + '"></table></div>';
        },
		onExpandRow: function(index,row){
			cur_index = index;
			$('#tradeDetailGrid-'+index).datagrid({
				url:basePath + "/forward/queryTradeDetail.htm",
				queryParams:{
					stradeId:row.strade_id
				},
				fitColumns:true,  
				striped : true, 
				rownumbers:true, 
				method : "get", 
				loadMsg:"数据正在加载中，请稍后",
				singleSelect:true,
				height:'auto',
				columns:[[
						{field:'trade_id',title:'订单编号',width:50,align:'center'},
						{field:'item_name',title:'下单商品',width:50,align:'center',formatter: function(value,row,index){
			        		  return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+row.url+"')>"+row.item_name+"</a>  ";
		        	  }},
						{field:'sku',title:'属性要求',width:50,align:'center',formatter: function(value,row,index){
							return row.sku;
						}},
						{field:'product',title:'已下单',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>=4){
								var gmt_ordertime=(typeof(row.gmt_ordertime)!= "undefined")?formatterDate(row.gmt_ordertime):"尚未下单";
								var name=(typeof(row.name)!=  "undefined")?row.name:"尚无";
								var site_order_id=(typeof(row.site_order_id)!=  "undefined")?row.site_order_id:"尚无单号";
								return gmt_ordertime+'<br>'+name+'<br>'+site_order_id;
							}else{
								return "尚未下单";
							}
						}},
						{field:'logistics',title:'已发货',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)==4){
								return "尚无官网发货信息";
							}else if(parseInt(row.status) == 11 || parseInt(row.status) == 12){
								return formatterDate(row.gmt_send) +'<br>'+row.abroad_company+'<br>'+row.abroad_id;
							}else if(parseInt(row.status)==21){
								return formatterDate(row.gmt_send) +'<br>'+row.abroad_company+'<br>'+row.abroad_id;
							}
						}},
						{field:'operation',title:'操作',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)==4){
							return "<input type='button' data-val='OfficialtoTransport' onclick='stocking("+row.trade_id+","+cur_index+",this)' value='入库'/>"
									   +"<br><input  data-val='OfficialShipDelay' onclick='delayTradeForward("+row.trade_id+","+cur_index+",this)' type='button' value='延期'/"
							}else if( parseInt(row.status) == 11 ){
								return "<input type='button' data-val='wareHousing' onclick='stocking("+row.trade_id+","+cur_index+",this)' value='入库'/>"
									   +"<br><input  data-val='wareHousingDelay' onclick='delay("+row.trade_id+","+cur_index+",this)' type='button' value='延期'/>";
							}else if(parseInt(row.status) == 12 ){
								return "已入库";
							}else if(parseInt(row.status)==21){
								return "已延期";
							}
						}}
					]],
				onSelect:function(){
					cur_index = index;
				},
				onResize:function(){
                        $('#tradeListGrid').datagrid('fixDetailRowHeight',index);
                },
		    onLoadSuccess:function(){ 
                        setTimeout(function(){ 
                            $('#tradeListGrid').datagrid('fixDetailRowHeight',index); 
						     },0); 
                } 
			});
			$('#tradeListGrid').datagrid('fixDetailRowHeight',index);
		},
		onCollapseRow:function(index,row){
            
        },
		
	});
}

//待中转
function loadToForward(){
	$('#tradeListGrid').datagrid({
	    url:basePath + "/forward/queryInDeliveryList.htm",
		queryParams:{
			state:2
		},
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true, 
        method : "get", 
		sortName: "gmt_accept",
		sortOrder: "desc",	
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
        pageList:[20,50,80],
        columns:[[
				  {field:'strade_id',title:'拼单号',width:50,align:'center'},
		          {field:'sharer_id',title:'接单大C',width:50,align:'center',formatter:function(value,row,index){
					  var shareId = $('#sharer').textbox('getValue');
					  if(flag_zhongzhuan==1 && shareId!=''){
							return "<font color='red'>"+ row.sharer_id +"</font>";
					  }else{
						    return row.sharer_id;
					  }
				  }},
				  {field:'gmt_accept',title:'接单时间',width:50,align:'center',formatter:formatterDate,sortable:true},
				  {field:'gmt_forward_submit',title:'提交转运清单时间',width:50,align:'center',formatter:formatterDate,sortable:true},
				  {field:'operation',title:'获取转运清单',width:50,align:'center',formatter:function(value,row,index){
					  return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:showForwardList('"+row.strade_id+"')>获取转运清单</a>  ";
				  }}
	          ]],
		toolbar:"#tradeManage_toolbar",
		/****************点击+号查看明细**************/
	    view: detailview,
        detailFormatter:function(index,row){
            	return '<div style="padding:2px"><table id="tradeDetailGrid-' + index + '"></table></div>';
        },
		onExpandRow: function(index,row){
			cur_index = index;
			$('#tradeDetailGrid-'+index).datagrid({
				url:basePath + "/forward/queryTradeDetail.htm",
				queryParams:{
					stradeId:row.strade_id
				},
				fitColumns:true,  
				striped : true, 
				rownumbers:true, 
				method : "get", 
				loadMsg:"数据正在加载中，请稍后",
				singleSelect:true,
				height:'auto',
				dataType:'json',
				columns:[[
						{field:'trade_id',title:'订单编号',width:50,align:'center'},
						{field:'item_name',title:'下单商品',width:50,align:'center',formatter: function(value,row,index){
			        		  return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+row.url+"')>"+row.item_name+"</a>  ";
		        	  }},
						{field:'sku',title:'属性要求',width:50,align:'center',formatter: function(value,row,index){
							return row.sku;
						}},
						{field:'product',title:'已下单',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>=4){
								var gmt_ordertime=(typeof(row.gmt_ordertime)!= "undefined")?formatterDate(row.gmt_ordertime):"尚未下单";
								var name=(typeof(row.name)!=  "undefined")?row.name:"尚无";
								var site_order_id=(typeof(row.site_order_id)!=  "undefined")?row.site_order_id:"尚无单号";
								return gmt_ordertime+'<br>'+name+'<br>'+site_order_id;
							}else{
								return "尚未下单";
							}
						}},
						{field:'logistics',title:'已发货',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>4){
								 var abroadId = $('#abroad_id').textbox('getValue');
								if(flag_zhongzhuan==1 && abroadId!='' && abroadId==row.abroad_id){
									return formatterDate(row.gmt_send)+'<br>'+row.abroad_company+'<br>'+"<font color='red'>"+ row.abroad_id +"</font>";
								}else{
									return formatterDate(row.gmt_send)+'<br>'+row.abroad_company+'<br>'+ row.abroad_id +"</font>";
								}
							}else{
								return "尚未发货";
							}
						}},
						{field:'operation',title:'操作',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)==12){
								return "<img style='width:90px; height:25px' src='"+imagesPath+"/systemImage/stocking.png'>";
							}else{
								return "<img style='width:90px; height:25px' onclick='stocking("+row.trade_id+","+cur_index+")' src='"+imagesPath+"/systemImage/stock.png'>";
							}
						}}
					]],
				onSelect:function(){
					cur_index = index;
				},
				onResize:function(){
                        $('#tradeListGrid').datagrid('fixDetailRowHeight',index);
                },
		        onLoadSuccess:function(){ 
				
                    setTimeout(function(){ 
                        $('#tradeListGrid').datagrid('fixDetailRowHeight',index); 
										},0); 
                },
				toolbar: [{
				text:'选择单号:<input class="easyui-combobox" id="abroadId'+index+'"/>     '
			},{
				text:'   国际物流单号:<input type="easyui-textbox" id="international_id'+index+'" />'
			},{
			   text:'开始国际物流',
			  
			   handler:function(){			
			   	  startInternational();
					  
				}			
			}]		
			});
			$('#stradeId').textbox('setValue',row.strade_id);
			$('#index').textbox('setValue',index);
			$('#tradeListGrid').datagrid('fixDetailRowHeight',index);
			//加载运单号
			$('#abroadId'+cur_index).combobox({
				url:basePath + "/forward/queryAbroadIdList.htm?stradeId="+row.strade_id,
				method : "get",
				valueField:'abroad_id', 
				textField:'abroad_id',
				onSelect:function(result){
					$('#tradeDetailGrid-'+index).datagrid('load',{
						abroadId:result.abroad_id,
						stradeId:row.strade_id
					});
				}
			})
		},
		onCollapseRow:function(index,row){
            
        },		
	});
}

//已中转
function loadIsForward(){
	$('#tradeListGrid').datagrid({
	    url:basePath + "/forward/queryInDeliveryList.htm",
		queryParams:{
			state:3
		},
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true, 
        method : "get",
        sortName: "gmt_forward_send",
				sortOrder: "desc",		
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
        columns:[[
				  {field:'strade_id',title:'拼单号',width:50,align:'center'},
		          {field:'sharer_id',title:'接单大C',width:50,align:'center',formatter:function(value,row,index){
					  var shareId = $('#sharer').textbox('getValue');
					  if(flag_yizhongzhuang==1 && shareId!=''){
							return "<font color='red'>"+ row.sharer_id +"</font>";
					  }else{
						    return row.sharer_id; 
					  }	
				  }},
				  {field:'internation_id',title:'EMS承运',width:50,align:'center'},
				  {field:'gmt_forward_send',title:'时间',width:50,align:'center',formatter:formatterDate,sortable:true},
				  {field:'operation',title:'获取转运清单',width:50,align:'center',formatter:function(value,row,index){
					  return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:showForwardList('"+row.strade_id+"')>获取转运清单</a>  ";
				  }}
	          ]],
					toolbar:"#tradeManage_toolbar",
		/****************点击+号查看明细**************/
	    view: detailview,
        detailFormatter:function(index,row){
            	return '<div style="padding:2px"><table id="tradeDetailGrid-' + index + '"></table></div>';
        },
		onExpandRow: function(index,row){
			cur_index = index;
			$('#tradeDetailGrid-'+index).datagrid({
				url:basePath + "/forward/queryTradeDetail.htm",
				queryParams:{
					stradeId:row.strade_id
				},
				fitColumns:true,  
				striped : true, 
				rownumbers:true, 
				method : "get", 
				loadMsg:"数据正在加载中，请稍后",
				singleSelect:true,
				height:'auto',
				columns:[[
						{field:'trade_id',title:'订单编号',width:50,align:'center'},
						{field:'item_name',title:'下单商品',width:50,align:'center',formatter: function(value,row,index){
			        		  return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+row.url+"')>"+row.item_name+"</a>  ";
		        	  }},
						{field:'sku',title:'属性要求',width:50,align:'center',formatter: function(value,row,index){
							return row.sku;
						}},
						{field:'product',title:'已下单',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>=4){
								var gmt_ordertime=(typeof(row.gmt_ordertime)!= "undefined")?formatterDate(row.gmt_ordertime):"尚未下单";
								var name=(typeof(row.name)!=  "undefined")?row.name:"尚无";
								var site_order_id=(typeof(row.site_order_id)!=  "undefined")?row.site_order_id:"尚无单号";
								return gmt_ordertime+'<br>'+name+'<br>'+site_order_id;
							}else{
								return "尚未下单";
							}
						}},
						{field:'logistics',title:'已发货',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>4){
								return formatterDate(row.gmt_send)+'<br>'+row.abroad_company+'<br>'+"<font color='red'>"+ row.abroad_id +"</font>";
							}else{
								return "尚未发货";
							}
						}}
					]],
				onSelect:function(){
					cur_index = index;
				},
				onResize:function(){
                        $('#tradeListGrid').datagrid('fixDetailRowHeight',index);
                },
		    onLoadSuccess:function(){ 
                        setTimeout(function(){ 
                            $('#tradeListGrid').datagrid('fixDetailRowHeight',index); 
						},0); 
                },
				toolbar:[{
				text:'<input class="easyui-combobox" id="abroadId'+index+'"/>'
				}]	,
			});
			
			$('#tradeListGrid').datagrid('fixDetailRowHeight',index);
			//加载运单号
			$('#abroadId'+cur_index).combobox({
				url:basePath + "/forward/queryAbroadIdList.htm?stradeId="+row.strade_id,
				method : "get",
				valueField:'abroad_id', 
				textField:'abroad_id',
				onSelect:function(result){
					$('#tradeDetailGrid-'+index).datagrid('load',{
						abroadId:result.abroad_id,
						stradeId:row.strade_id
					});
				}
			})
			$('#stradeId').textbox('setValue',row.strade_id);
			
			},
			onCollapseRow:function(index,row){
        $('#tradeManageDetail_toolbar_2').remove('.Datagrid-toolbar');
      },
		
	});
}


//所有拼单
function allStrade(){
$('#tradeListGrid').datagrid({
	    url:basePath + "/forward/queryInDeliveryList.htm",
		queryParams:{
			state:0
		},
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true, 
        method : "get", 
		sortName: "gmt_accept",
		sortOrder: "desc",	
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
        pageList:[20,50,80],
        columns:[[
				  {field:'strade_id',title:'拼单号',width:50,align:'center'},
		          {field:'sharer_id',title:'接单大C',width:50,align:'center',formatter:function(value,row,index){
					  var shareId = $('#sharer').textbox('getValue');
					  
					  if(flag==1 && shareId!=''){
							return "<font color='red'>"+ row.sharer_id +"</font>";
					  }else{
						    return row.sharer_id;
					  }
				  }},
				  {field:'gmt_accept',title:'接单时间',width:50,align:'center',formatter:formatterDate,sortable:true}
	          ]],
		toolbar:"#tradeManage_toolbar",
		/****************点击+号查看明细**************/
	    view: detailview,
        detailFormatter:function(index,row){
            	return '<div style="padding:2px"><table id="tradeDetailGrid-' + index + '"></table></div>';
        },
		onExpandRow: function(index,row){
			cur_index = index;
			$('#tradeDetailGrid-'+index).datagrid({
				url:basePath + "/forward/queryTradeDetail.htm",
				queryParams:{
					stradeId:row.strade_id
				},
				fitColumns:true,  
				striped : true, 
				rownumbers:true, 
				method : "get", 
				loadMsg:"数据正在加载中，请稍后",
				singleSelect:true,
				height:'auto',
				columns:[[
						{field:'trade_id',title:'订单编号',width:50,align:'center'},
						{field:'item_name',title:'下单商品',width:50,align:'center',formatter: function(value,row,index){
			        		  return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+row.url+"')>"+row.item_name+"</a>  ";
		        	  }},
						{field:'sku',title:'属性要求',width:50,align:'center',formatter: function(value,row,index){
							return row.sku;
						}},
						{field:'product',title:'已下单',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>=4){
								var gmt_ordertime=(typeof(row.gmt_ordertime)!= "undefined")?formatterDate(row.gmt_ordertime):"尚未下单";
								var name=(typeof(row.name)!=  "undefined")?row.name:"尚无";
								var site_order_id=(typeof(row.site_order_id)!=  "undefined")?row.site_order_id:"尚无单号";
								return gmt_ordertime+'<br>'+name+'<br>'+site_order_id;
							}else{
								return "尚未下单";
							}
						}},
						{field:'logistics',title:'已发货',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>4){
								var gmt_send_time=(typeof(row.gmt_send)!= "undefined")?formatterDate(row.gmt_send):"尚未下单";
								var abroad_company=(typeof(row.abroad_company)!=  "undefined")?row.abroad_company:"尚无";
								var abroad_id=(typeof(row.abroad_id)!=  "undefined")?row.abroad_id:"尚无单号";
								return gmt_send_time+'<br>'+abroad_company+'<br>'+abroad_id;
								
							}else{
								return "尚未发货";
							}
						}},
						{field:'operation',title:'操作',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>=12){
								return "<img style='width:90px; height:25px' src='"+imagesPath+"/systemImage/stocking.png'>";
							}else if(parseInt(row.status)==11){
								return "<img style='width:90px; height:25px' onclick='stocking("+row.trade_id+","+cur_index+")' src='"+imagesPath+"/systemImage/stock.png'>";
							}else if(parseInt(row.status)<=4){
									return "";
							}else{
									return "";
							}
						}}
					]],
				onSelect:function(){
					cur_index = index;
				},
				onResize:function(){
                        $('#tradeListGrid').datagrid('fixDetailRowHeight',index);
                },
		        onLoadSuccess:function(){ 
                        setTimeout(function(){ 
                            $('#tradeListGrid').datagrid('fixDetailRowHeight',index); 
						},0); 
                } 
			});
			$('#tradeListGrid').datagrid('fixDetailRowHeight',index);
		},
		onCollapseRow:function(index,row){
            
        },
		
	});
}

//全部入库
function allStorage(){
	$('#tradeListGrid').datagrid({
	    url:basePath + "/forward/queryInDeliveryList.htm",
		queryParams:{
			state:4
		},
        fitColumns:true,  
        striped : true, 
        pagination:true,
        rownumbers:true, 
        method : "get",
        sortName: "gmt_forward_send",
		    sortOrder: "desc",		
        loadMsg:"数据正在加载中，请稍后",
        singleSelect:true,
        fit:true,
        pageList:[20,50,80],
        columns:[[
				  {field:'strade_id',title:'拼单号',width:50,align:'center'},
		          {field:'sharer_id',title:'接单大C',width:50,align:'center',formatter:function(value,row,index){
					  var shareId = $('#sharer').textbox('getValue');
					  if(flag_yizhongzhuang==1 && shareId!=''){
							return "<font color='red'>"+ row.sharer_id +"</font>";
					  }else{
						    return row.sharer_id; 
					  }	
				  }},
				  {field:'internation_id',title:'EMS承运',width:50,align:'center'},
				  {field:'gmt_forward_send',title:'时间',width:50,align:'center',formatter:formatterDate,sortable:true},
				  {field:'operation',title:'获取转运清单',width:50,align:'center',formatter:function(value,row,index){
					  return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:showForwardList('"+row.strade_id+"')>获取转运清单</a>  ";
				  }}
	          ]],
		toolbar:"#tradeManage_toolbar",
		/****************点击+号查看明细**************/
	    view: detailview,
        detailFormatter:function(index,row){
            	return '<div style="padding:2px"><table id="tradeDetailGrid-' + index + '"></table></div>';
        },
		onExpandRow: function(index,row){
			cur_index = index;
			$('#tradeDetailGrid-'+index).datagrid({
				url:basePath + "/forward/queryTradeDetail.htm",
				queryParams:{
					stradeId:row.strade_id
				},
				fitColumns:true,  
				striped : true, 
				rownumbers:true, 
				method : "get", 
				loadMsg:"数据正在加载中，请稍后",
				singleSelect:true,
				height:'auto',
				columns:[[
						{field:'trade_id',title:'订单编号',width:50,align:'center'},
						{field:'item_name',title:'下单商品',width:50,align:'center',formatter: function(value,row,index){
			        		  return "<a href='#' class='easyui-linkbutton' plain='true' onclick=javascript:jumpTo('"+row.url+"')>"+row.item_name+"</a>  ";
		        	  }},
						{field:'sku',title:'属性要求',width:50,align:'center',formatter: function(value,row,index){
							return row.sku;
						}},
						{field:'product',title:'已下单',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>=4){
								var gmt_ordertime=(typeof(row.gmt_ordertime)!= "undefined")?formatterDate(row.gmt_ordertime):"尚未下单";
								var name=(typeof(row.name)!=  "undefined")?row.name:"尚无";
								var site_order_id=(typeof(row.site_order_id)!=  "undefined")?row.site_order_id:"尚无单号";
								return gmt_ordertime+'<br>'+name+'<br>'+site_order_id;
							}else{
								return "尚未下单";
							}
						}},
						{field:'logistics',title:'已发货',width:50,align:'center',formatter:function(value,row,index){
							if(parseInt(row.status)>4){
								var gmt_send_time=(typeof(row.gmt_send)!= "undefined")?formatterDate(row.gmt_send):"尚未下单";
								var abroad_company=(typeof(row.abroad_company)!=  "undefined")?row.abroad_company:"尚无";
								var abroad_id=(typeof(row.abroad_id)!=  "undefined")?row.abroad_id:"尚无单号";
								return gmt_send_time+'<br>'+abroad_company+'<br>'+abroad_id;
							}else{
								return "尚未发货";
							}
						}}
					]],
				onSelect:function(){
					cur_index = index;
				},
				onResize:function(){
                        $('#tradeListGrid').datagrid('fixDetailRowHeight',index);
                },
		        onLoadSuccess:function(){ 
                        setTimeout(function(){ 
                            $('#tradeListGrid').datagrid('fixDetailRowHeight',index); 
						},0); 
                },
				toolbar:'#tradeManageDetail_toolbar_2'
			});
			$('#tradeListGrid').datagrid('fixDetailRowHeight',index);
			//加载运单号
			$('#abroadId_2').combobox({
				url:basePath + "/forward/queryAbroadIdList.htm?stradeId="+row.strade_id,
				method : "get",
				valueField:'abroad_id', 
				textField:'abroad_id',
				onSelect:function(result){
					$('#tradeDetailGrid-'+index).datagrid('load',{
						abroadId:result.abroad_id,
						stradeId:row.strade_id
					});
				}
			})
			$('#stradeId').textbox('setValue',row.strade_id);
		},
		onCollapseRow:function(index,row){
            
        },
		
	});
}


function jumpTo(url){
	window.open(url);
}


function doSearch(){
	flag=1;
	flag_zhongzhuan=1;
	flag_yizhongzhuang=1;
	$('#tradeListGrid').datagrid('load',{
		state:state,
		abroadId:$('#abroad_id').textbox('getValue'),
		sharerId:$('#sharer').textbox('getValue')
    });
}

//入库
function stocking(tradeId,index,obj){
	var transitionName = $(obj).attr("data-val");
   $.ajax({
            type:"POST",
            url:basePath + "/forward/stockingTrade.htm",
            data:"tradeId="+tradeId+"&transitionName="+transitionName,
			dataType:'json',
            success:function(msg){
				if(msg.status=="success"){
					$('#tradeDetailGrid-'+index).datagrid('reload');
				}else{
					
				}
             }
		});
}
//转运入库延期
function delayTradeForward(tradeId,index,obj){
	var transitionName = $(obj).attr("data-val");
   $.ajax({
            type:"POST",
            url:basePath + "/forward/delayTradeForward.htm",
            data:"tradeId="+tradeId+"&transitionName="+transitionName,
			dataType:'json',
            success:function(msg){
				if(msg.status=="success"){
					$('#tradeDetailGrid-'+index).datagrid('reload');
				}else{
					
				}
             }
		});
}

function showForwardList(strade_id){
	$('#forwardListDialog').dialog('open');
	$('#forwardListDialog').dialog('setTitle',"拼单号："+strade_id);
	$('#baseInfo').html("大C收货信息:");
	$('#photo_font').html("身份证正面");
	$('#photo_back').html("身份证反面");
	$('#stradeInfo').html("拼单信息");
	$.ajax({
            type:"GET",
            url:basePath + "/forward/queryReceiveInfoByStrade.htm",
            data:"stradeId="+strade_id,
            success:function(msg){
				if(msg){
					var res=eval("(" +msg +")");
					var txt = document.getElementById('baseInfo');
					var str = 	"<table >"+
					"<tr>"+
						"<td align='left'>大C收货信息:</td>"+
					"</tr>"+
					"<tr>"+
						"<td width='45%' align='left'>收货人:</td>"+
						"<td width='50%' align='left'>"+res.receiver_name+"</td>"+
					"</tr>"+
					"<tr>"+
						"<td width='45%' align='left'>手机号:</td>"+
						"<td width='50%' align='left'>"+res.receiver_mobile+"</td>"+
					"</tr>"+
					"<tr>"+
						"<td width='45%' align='left'>座机号:</td>"+
						"<td width='50%' align='left'>"+res.receiver_phone+"</td>"+
					"</tr>"+
					"<tr>"+
						"<td width='45%' align='left'>收件地址:</td>"+
						"<td width='50%' align='left'>"+res.address+"</td>"+
					"</tr>"+
					"<tr>"+
						"<td width='45%' align='left'>身份证号码:</td>"+
						"<td width='50%' align='left'>"+res.id_num+"</td>"+
					"</tr>"+
					"</table>";
					txt.innerHTML = str;
					if(res.id_photo_front!=null){
						$("#photo_font").html("<img  height='100%' width='100%'  src='"+ homePath +"/"+res.id_photo_front+"'/>");
					}
					if(res.id_photo_back!=null){
						$("#photo_back").html("<img  height='100%' width='100%' src='"+ homePath +"/"+res.id_photo_back+"'/>");
					}
					
					/*var baseInfoTxt = document.getElementById('stradeInfo');
					var baseInfoStr = 	"<table >"+
					"<tr>"+
						"<td align='left'>大C收货信息:</td>"+
						"<td align='left'>大C收货信息:</td>"+
						"<td align='left'>大C收货信息:</td>"+
						"<td align='left'>大C收货信息:</td>"+
					"</tr>"+
					"</table>";
					baseInfoTxt.innerHTML = baseInfoStr;*/
					$('#stradeInfo').html('拼单号:'+res.strade_id+ "&nbsp;&nbsp;&nbsp;&nbsp;"+"成单时间:"+formatterDate(res.gmt_share) + "&nbsp;&nbsp;&nbsp;&nbsp;" +"内容品原产国：日本" + "&nbsp;&nbsp;&nbsp;&nbsp;" + "申报总价值:"+res.totalvalue);
				}
				
             }
	});
	
	$('#forwardListGrid').datagrid({
		url:basePath + "/forward/queryForwardList.htm",
		queryParams:{
			stradeId:strade_id
		},
		fitColumns:true,  
		striped : true, 
		rownumbers:true, 
		title:"货物说明：",
		method : "get", 
		loadMsg:"数据正在加载中，请稍后",
		singleSelect:true,
		fit:true,
		columns:[[
			{field:'itemName',title:'品名',width:50,align:'center'},
			{field:'num',title:'数量',width:50,align:'center'},
			{field:'value',title:'价值',width:50,align:'center'}
			]]
	});
}

function startInternational(){
	var index = $('#index').textbox('getValue');
	var data=$('#tradeDetailGrid-'+index).datagrid('getData');
	var flag = false;
	for(var i=0;i<data.rows.length;i++){
		if(data.rows[i].status!=12){
			flag = true;
			
		}
	}
	if(flag == true){
		$.messager.confirm("提示信息","有商品尚未入库",function(r){
			});
	}else{
		$.messager.confirm("提示信息","确认开始国际物流?",function(r){
        if (r){ 
			$.ajax({
				type:"POST",
				url:basePath + "/forward/startInternational.htm",
				data:"internationalId="+$('#'+'international_id'+index).val()+"&stradeId="+$('#'+'stradeId').val(),
				success:function(msg){
					if(msg.indexOf('操作成功')>0){
						$.messager.alert("提示信息","操作成功");
					}else{
						$.messager.alert("提示信息","操作失败");
					}
			}
       });     
        }	                 
    }); 
	}
	
}