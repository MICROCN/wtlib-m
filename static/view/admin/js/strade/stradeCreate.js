	var AssignedtradeIdMap=new Map();//存放已经在欲关联池的订单id
	var totalWeight=0;//右侧已经关联的总重量
	var totalForeignMoney=0;
	var totalChineseMoney=0;
	var tradeNum=0;	
	//定一个一个Trade对象 
	 function Trade(tradeId,securitynum,cid,limitnum,num,allRightItemNum,weight,foreignMoney,chineseMoney,itemId,shopId,specSharer,province){
   		var obj = new Object;
  	    this.tradeId = tradeId;//订单id
	   	this.securitynum = securitynum;//该订单的合理性限制
		this.cid=cid;//该订单的商品所在的类目
	   	this.limitnum=limitnum;	//是否限购
	   	this.num=num;//order里面的商品数量
		this.allRightItemNum=allRightItemNum;
		this.weight=weight;
		this.foreignMoney=foreignMoney;
		this.chineseMoney=chineseMoney;
		this.itemId=itemId;
		this.shopId=shopId;
		this.specSharer=specSharer;
		this.province=province;
	    return this;
  	}

	$(function(){
		
		initQuery();//初始化查询
		
		//添加按钮时间绑定
		$('#assignTo').click(function(){		
			var tradeIds;//获得欲关联的tradeIds;
		    var checkedTradeDiv=$("#unTradesPoolBody").find("input[type=checkbox]:checked").parent();
	    	for(var i=0;i<checkedTradeDiv.length;i++){		   		
	    		var tradeId=$(checkedTradeDiv[i]).find("label[name='tradeId']").attr("id");//获得左侧订单Id  		
	    		var tradeSecuritynum=$(checkedTradeDiv[i]).find("input[name='tradeSecuritynum']").val(); //获得左侧合理性限制 
	    		if(typeof(tradeSecuritynum)!="undefined"){
						tradeSecuritynum=tradeSecuritynum.trim();
				}else{
						tradeSecuritynum=0;
					}		
	    		var cid=$(checkedTradeDiv[i]).find("input[name='tradeItemCid']").val();//获得左侧订单cid
	  			var tradeLimitnum=$(checkedTradeDiv[i]).find("input[name='tradeLimitnum']").val();//获得左侧限购标志
	  			var num=$(checkedTradeDiv[i]).find("label[name='itemNum']").html().trim();//获得左侧订单下的商品数量
				var allRightItemNum=$(checkedTradeDiv[i]).find("label[name='allRightItemNum']").html();	
				var weight=$(checkedTradeDiv[i]).find("label[name='itemWeight']").html();//订单的重量
				var foreignMoney=$(checkedTradeDiv[i]).find("label[name='foreignMoney']").html();
				var chineseMoney=$(checkedTradeDiv[i]).find("label[name='chineseMoney']").html()
				var itemId=$(checkedTradeDiv[i]).find("input[name='itemId']").val();
				var shopId=$(checkedTradeDiv[i]).find("input[name='shopId']").val();
				var specSharer=$(checkedTradeDiv[i]).find("span[name='specsharer']").html().trim(); 
				var province=$(checkedTradeDiv[i]).find("span[name='province']").html().trim();
				if(typeof(province) != "undefined" && province != "无"){
					var flag=0;
					$("td[name=province]").find("span").each(function(index,p){
						if(province == $(p).html().trim()){
							flag=1;
						}
					});	
					if(flag==0){
						var provinces=$("td[name=province]").find("span");
						if(((provinces.length) % 4) == 0 && (provinces.length)>0){
							$("td[name=province]").append("<br><span>"+province+"</span>  ");
						}else{
							$("td[name=province]").append("&nbsp;<span>"+province+"</span>   ");
						}		
					}
					flag=undefined;
				}
				if(specSharer != "无" && typeof(specSharer) != "undefined" ){
					var flag=0;
					$("td[name=specSharer]").find("span").each(function(index,p){
						if(specSharer == $(p).html().trim()){
							flag=1;
						}
					});
					if(flag==0){
						var specSharers=$("td[name=specSharer]").find("span");
						if(((specSharers.length) % 4) == 0 && (specSharers.length)>0){
							$("td[name=specSharer]").append("<br><span style='padding-right:10px;'>"+specSharer+"</span>  ");
						}else{
							$("td[name=specSharer]").append("<span style='padding-right:10px;'>"+specSharer+"</span>   ");
						}	
					}
					flag=undefined;
				}    		
				var trade=new Trade(tradeId,tradeSecuritynum,cid,tradeLimitnum,num,allRightItemNum,weight,foreignMoney,chineseMoney,itemId,shopId,specSharer,province);	//创建新的订单对象		
				var isAddleAbles=isAddleAble(trade);//判断是否可以直接添加
	    		if( typeof(isAddleAbles) == "undefined"){// undefined 代表直接添加这个div
					$('#TradesPoolBody').append("<label><div class='tradDetail'>"+$(checkedTradeDiv[i]).html()+"</div></label>");			
				}else{
					var value=isAddleAbles;
					$(checkedTradeDiv[i]).find("label[name='allRightItemNum']").html(value.allRightItemNum);//修改自己的已有数量
					var isLimitAbbles=isLimitAbble(trade);
					if(isLimitAbbles == 0){
						$(checkedTradeDiv[i]).find("input[name='tradeLimitnum']").next().html("<span style='color:red'>已挑选同类限购商品</span>");//提示购买了相同商店已经限购的商品
					}	
					if(value.allRightItemNum>value.securitynum && value.securitynum>0){//存在合理性限制，并且数量和大于合理性限制 改变背景色
						$('#TradesPoolBody').append("<label><div class='tradDetail' style='background-color:rgba(224, 44, 38, 0.2) '>"+$(checkedTradeDiv[i]).html()+"</div></label>");	
					}else{
						$('#TradesPoolBody').append("<label><div class='tradDetail'>"+$(checkedTradeDiv[i]).html()+"</div></label>");	
					}
				}			
				//计算右侧的总信息
				totalWeight=parseInt(totalWeight)+parseInt(trade.weight);//计算已经关联的总重量			
				totalForeignMoney=(parseFloat(totalForeignMoney)+parseFloat(trade.foreignMoney)).toFixed(2);//计算已经关联的外币金额				
				totalChineseMoney=(parseFloat(totalChineseMoney)+parseFloat(trade.chineseMoney)).toFixed(2);//计算已经关联的本金额
				AssignedtradeIdMap.put(tradeId,trade);
				tradeNum++;
				$("#AssignedtradeIds").val(AssignedtradeIds);
				$("#AssignTradesPool").find("span[name='totalWeight']").html(totalWeight);
				$("#AssignTradesPool").find("span[name='totalForeignMoney']").html(totalForeignMoney);
				$("#AssignTradesPool").find("span[name='totalChineseMoney']").html(totalChineseMoney);
				$("#AssignTradesPool").find("span[name='tradeNum']").html(tradeNum);
				var trade=undefined;
				$("#assignTo").attr("disabled",true);//添加按钮不可用
	    	};
			//如果重量大于9500 小于 10500 时按钮为白色  超过为红色  小于为蓝色
			if(totalWeight>0){
				$("#createStrade").attr("disabled",false);//设置生成拼单按钮用
				if(totalWeight<9500){
				$("#createStrade").next().empty();
				$("#createStrade").addClass("underWeight");
				$("#createStrade").removeClass("overWeight");
				$("#createStrade").next().html("<span style='color:rgba(14, 17, 230, 0.32)'>重量尚不足</span>")
				}else if(totalWeight<=10500){
					$("#createStrade").next().empty();
					$("#createStrade").removeClass("overWeight");
					$("#createStrade").removeClass("underWeight");
					$("#createStrade").next().html("<span style='color:black'>重量已达标</span>")
				}else {
					$("#createStrade").next().empty();
					$("#createStrade").removeClass("underWeight");
					$("#createStrade").addClass("overWeight");					
					$("#createStrade").next().html("<span style='color:rgba(237, 251, 19, 0.55)'>重量已超过</span>")
				}
			}else{
				$("#createStrade").next().empty();
				$("#createStrade").removeClass("overWeight");
				$("#createStrade").removeClass("underWeight");
				$("#createStrade").attr("disabled",true);
			}
			
			//每次添加或者删除的时候、去掉全选按钮的选中状态
			$("input[name='allChoose']:checked").each(function(index,check){
				$(check).attr("checked",false);
			});	
				searchTrades();//更新左侧信息状态
		});
		
		
	    //删除按钮
	    $('#cancelAssign').click(function(){
	    	var checkedTradeDiv=$("#TradesPoolBody").find("input[type=checkbox]:checked").parent();
	    	for(var i=0;i<checkedTradeDiv.length;i++){			    		
	    		
	    		var tradeIdAssign=$(checkedTradeDiv[i]).find("label[name='tradeId']").attr("id");
				$(checkedTradeDiv[i]).parent().remove();//移除该div
				
				var trade=AssignedtradeIdMap.get(tradeIdAssign);		
				AssignedtradeIdMap.remove(tradeIdAssign);//移除在与关联中该对象
				isDelimitAbble(trade)//判断限购标志的删除
				var isDeleteTrades=isDeleteTrade(trade);
				//更新右侧信息
				var province=trade.province;
				if(typeof(province) != "undefined" && province != "无"){
					$("td[name=province]").find("span").each(function(index,p){
						if(province == $(p).html()){
							var flag=1;
							var provinces=$("#TradesPoolBody").find("span[name='province']");
							for(var i=0;i<provinces.length;i++){
								if(province == $(provinces[i]).html().trim()){//存在有该地址
									flag=0;
								}
							}
							
							if(flag==1){
								$(p).remove();
							}
							flag=undefined;
						}
					});
				}
				var specSharer=trade.specSharer;
				if(typeof(specSharer) != "undefined" && specSharer != "无"){
					$("td[name=specSharer]").find("span").each(function(index,p){
						if(specSharer == $(p).html()){
							var flag=1;
							$("#TradesPoolBody").find("span[name='specsharer']").each(function(index,sp){
								if(specSharer == $(sp).html().trim()){
									flag = 0;
								}
							});
							if(flag==1){
								$(p).remove();
							}
							flag=undefined;
						}
					});
				}
				totalWeight=parseInt(totalWeight)-parseInt(trade.weight);
				totalForeignMoney=parseFloat((totalForeignMoney)-parseFloat(trade.foreignMoney)).toFixed(2);
				totalChineseMoney=parseFloat((totalChineseMoney)-parseFloat(trade.chineseMoney)).toFixed(2);
				$("#AssignTradesPool").find("span[name='totalWeight']").html(totalWeight);
				$("#AssignTradesPool").find("span[name='totalForeignMoney']").html(totalForeignMoney);
				$("#AssignTradesPool").find("span[name='totalChineseMoney']").html(totalChineseMoney);
				tradeNum--;
				$("#AssignTradesPool").find("span[name='tradeNum']").html(tradeNum);
				
	    	};
			
			$("[name='allChoose']").each(function(index,check){//每次添加或者删除的时候、去掉全选按钮的选中状态
				$(check).attr("checked",false);
			});    
			if(totalWeight<=0){
				$("#createStrade").next().empty();
				$("#createStrade").removeClass("overWeight");
				$("#createStrade").removeClass("underWeight");
				$("#createStrade").attr("disabled",true);//设置生成拼单按钮不可用
			}else{
				$("#createStrade").attr("disabled",false);
				if(totalWeight<9500){//根据重量修改按钮的颜色和提示
					$("#createStrade").next().empty();
					$("#createStrade").addClass("underWeight");
					$("#createStrade").removeClass("overWeight");
					$("#createStrade").next().html("<span style='color:rgba(14, 17, 230, 0.32)'>重量尚不足</span>")
				}else if(totalWeight<=10500){
					$("#createStrade").next().empty();
					$("#createStrade").removeClass("overWeight");
					$("#createStrade").removeClass("underWeight");
					$("#createStrade").next().html("<span style='color:black'>重量达标</span>")
				}else {
					$("#createStrade").next().empty();
					$("#createStrade").removeClass("underWeight");
					$("#createStrade").addClass("overWeight");					
					$("#createStrade").next().html("<span style='color:rgba(237, 251, 19, 0.55)'>重量已超过</span>")
				}
			}		
			$("#cancelAssign").attr("disabled",true);
			$("#cancelAssign").addClass("disableInput");
			searchTrades();
	    });
	    
	     //全选按钮点击选中
	    $("input[name='allChoose']").click(function(){ 	   	
	    	if($(this).attr("checked")){//如果是选中全选按钮的话、下面的全部选中
	    		var allCheckedTrade=$(this).parentsUntil("label").parent().next().find("input[type=checkbox]");
				for(var i=0;i<allCheckedTrade.length;i++){
					$(allCheckedTrade[i]).attr("checked",true);	
				}
				if($(this).attr("id")=="unAssignedChoose"){
					
					$("#assignTo").attr("disabled",false);
					$("#assignTo").removeClass("disableInput");
				}else{
					$("#cancelAssign").attr("disabled",false);
					$("#cancelAssign").removeClass("disableInput");
				}
	    	}else{//如果是取消选中按钮的话、下面的选择按钮全部取消选中
	    		var allCheckedTrade=$(this).parentsUntil("label").parent().next().find("input[type=checkbox]");
				for(var i=0;i<allCheckedTrade.length;i++){
					$(allCheckedTrade[i]).attr("checked",false);	
				} 
				if($(this).attr("id")=="unAssignedChoose"){					
					$("#assignTo").attr("disabled",true);
					$("#assignTo").addClass("disableInput");
				}else{
					$("#cancelAssign").attr("disabled",true);
					$("#cancelAssign").addClass("disableInput");
				}
	    	}		
	    });	    
	});
	
	//初始加载数据
		function initQuery(){	
			var sumHref=basePath+"/strade/tradeFormSum.htm?";//更新状态统计表
			$.post(sumHref,function(data){
				$("#tradeFormSum").append(data);
			});
			var href=basePath+"/strade/stradeunAssignTreadesPool.htm?";
			$.post(href,function(data) {	
				$("#unTradesPoolBody").append(data);
			});
		}
	
	//点击选择该拼单 
		function assginKey(obj){
			if($(obj).attr("checked")){
				if($(obj).parentsUntil("div[name='poolBody']").parent().attr("id")=="unTradesPoolBody"){	
					$("#assignTo").attr("disabled",false);
					$("#assignTo").removeClass("disableInput");
				}else{
					$("#cancelAssign").attr("disabled",false);
					$("#cancelAssign").removeClass("disableInput");
				}
			}else{
				var flag=0;
				if($(obj).parentsUntil("div[name='poolBody']").parent().attr("id")=="unTradesPoolBody"){	
					$("#unTradesPoolBody").find("input[type=checkbox]").each(function(index,obt){
						if($(obt).attr("checked")){
							flag++;
						}
					});
					if(flag==0){
						//如果全部撤销则不能点击
						$("#assignTo").attr("disabled",true);
						$("#assignTo").addClass("disableInput");
					}	
					flag=0;
				}else{
					$("#TradesPoolBody").find("input[type=checkbox]").each(function(index,obt){
						if($(obt).attr("checked")){
							flag++;
						}
					});
					if(flag==0){
						//如果全部撤销则不能点击按钮
						$("#cancelAssign").attr("disabled",true);
						$("#cancelAssign").addClass("disableInput");
					}	
					flag=0;
				}
						
			}

		}
	

	//生成拼单按钮
	   function createStrade(){
			if(confirm("确定生成拼单?")){
				var AssignedtradeIds;  
				$("#TradesPoolBody").find("label[name='tradeId']").each(function(index,tradIds){
					if(typeof(AssignedtradeIds)=="undefined"){
						AssignedtradeIds=$(tradIds).html();			
					}else{
						AssignedtradeIds=AssignedtradeIds+","+$(tradIds).html();
					}
				});   
				var specSharer=$("#specSharer").find("option:selected").val();
				if(specSharer == null || typeof(specSharer)=="undefined" || typeof(AssignedtradeIds)=="undefined" || AssignedtradeIds == null ){
				   alert("请选择拼主或者添加订单");
				   return ;
				}else{
					 href=basePath+"/strade/createStrade.htm?assignedtradeIds="+AssignedtradeIds+"&specSharer="+specSharer;
					 $.post(href,function(data){
						   if(data.isSuccessed==true){
								 alert("生成拼单成功")
								 $("#TradesPoolBody").empty();	
								 $("#AssignTradesPool").find("span[name='totalWeight']").html(0);
								 $("#AssignTradesPool").find("span[name='totalForeignMoney']").html(0);
								 $("#AssignTradesPool").find("span[name='totalChineseMoney']").html(0);
								 $("#AssignTradesPool").find("span[name='tradeNum']").html(0);
								 $("td[name=specSharer]").empty();
								 $("td[name=province]").empty();
								 totalWeight=0;//重置总重量			
								 totalForeignMoney=0;//重置外币金额				
								 totalChineseMoney=0;//重置本金额
								 tradeNum=0;//重置件数
								 //更新左侧的查询信息
								var allSharers=data.allSharers;
								var sharer=$("#leftPoolBar").find('#specsharers');
								$(sharer).empty();
								$(sharer).append("<option value=''>全部订单</option>");
								$(sharer).append("<option value='all_sharer'>指定大C</option>");
								$(sharer).append("<option value='none_sharer'>未指定大C</option>");
								for(var i=0;i<allSharers.length;i++){
									$(sharer).append("<option value='"+allSharers[i]+"'>"+allSharers[i]+"</option>")
								}
								var province=$("#leftPoolBar").find('#provinces');
								var provinces=data.allProvinces;
								$(province).empty();
								$(province).append("<option value=''>全部省份</option>");
								for(var i=0;i<provinces.length;i++){
									$(province).append("<option vlaue='"+provinces[i]+"' name='province'>"+provinces[i]+"</option>")
								}
								$("#createStrade").next().empty();
								$("#createStrade").removeClass("overWeight");
								$("#createStrade").removeClass("underWeight");
								$("#createStrade").attr("disabled",true);
								AssignedtradeIdMap=new Map();
						   }else if(data.isSuccessed==false){
							  alert(data); 
						   }else{
							  alert("出现异常");
							 
						   }			   
					},'json');
			   }
				AssignedtradeIds = undefined;   
		   }else{
				return ;
		   }		
		}
   
		//点击查询按钮 刷新左侧的页面数据
		function searchTrades(){
			var AssignedtradeIds;//获得所有关联的tradeIds
			$("#TradesPoolBody").find("label[name='tradeId']").each(function(index,tradIds){
				if(typeof(AssignedtradeIds)=="undefined"){
					AssignedtradeIds=$(tradIds).html();			
				}else{
					AssignedtradeIds=AssignedtradeIds+","+$(tradIds).html();
				}
			});
			$("#AssignedtradeIds").val(AssignedtradeIds);
			var query=$("#tradeFormInfo").serialize();//生成查询数据 
			$("#unTradesPoolBody").empty();
			$("#tradeFormSum").empty();
			$("#AssignedtradeIds").val("");//清空存储已经预关联的订单的id的input容器
			var sumHref=basePath+"/strade/tradeFormSum.htm?"+query;//更新状态统计表
			$.post(sumHref,function(data){
				$("#tradeFormSum").append(data);
			});
			var href=basePath+"/strade/stradeunAssignTreadesPool.htm?"+query;
			$.post(href,function(data){
				$("#unTradesPoolBody").append(data);
			});
			AssignedtradeIds=undefined;
		}
	
	//判断删除时的合理性标准
	   function isDeleteTrade(trade){
		   var obj=undefined;
		   var len=AssignedtradeIdMap.keys.length;
		   for(var i=0;i<len;i++){
			   var k=AssignedtradeIdMap.keys[i];
			   var value=AssignedtradeIdMap.get(k);
			   var kindOf=dequals(trade,value);
			   if(kindOf==1 || kindOf==2){	  
				  obj=value;//存在相同的cid 返回相关具有相同cid的订单
				  break;
			   }	 
		   }
		   return obj;
	   }
   
	   //判断添加订单时的合理性标准
	   function isAddleAble(trade){
		   var obj=undefined;
		   var len=AssignedtradeIdMap.keys.length;
		   for(var i=0;i<len;i++){
			   var k=AssignedtradeIdMap.keys[i];
			   var value=AssignedtradeIdMap.get(k);
			   var kindOf=equals(trade,value);
			   if(kindOf==1 || kindOf==2){	  
				  obj=value;//存在相同的cid
				  break;
			   }	 
		   }
		   return obj;
	   }
	
		//限制添加时的购买标志
		function isLimitAbble(trade){
			 var obj = undefined;
			 var len = AssignedtradeIdMap.keys.length;
		   for(var i=0;i<len;i++){
			   var k = AssignedtradeIdMap.keys[i];
			   var value = AssignedtradeIdMap.get(k);
			   var kindOf = limitNumCheck(trade,value);
			   if(kindOf == 0){
				  //存在相同的cid
				  obj = kindOf;
				  break;
			   }	 
		   }
		   return obj;
		}
	
		//是否删除限制购买标志
		function isDelimitAbble(trade){	
			var flag = 0;
			var len = AssignedtradeIdMap.keys.length;
			for (var i=0;i<len;i++){
				var k = AssignedtradeIdMap.keys[i];
				var value = AssignedtradeIdMap.get(k);
				var kindOf = deLimitNumCheck(trade,value);
				if(kindOf == 0){
					flag++;
				}
			}
			if(flag<2){//flag < 2 说明右侧只有一件该限购商品
				var div = $('#TradesPoolBody').find("input[name='itemId']").filter("[value='"+trade.itemId+"']");
				$(div).parentsUntil("div[class='tradDetail']").find("input[name='tradeLimitnum']").next().empty();
			}
			var flag = 0;
		}
	
		//判断添加订单时的限购限制
		function limitNumCheck(trade,obj){
			if(trade.limitnum>0){
				if(trade.itemId == obj.itemId && trade.shopId==obj.shopId){
					if( (trade.skuId == obj.skuId && trade.skuId != null) || typeof(trade.skuId) == "undefined"){//如果该商品没有sku 则说明这个商品在已经关联的右侧存在相同限购商品  如果有sku 而且sku相等说明该型号的商品也存在了限购标准
						var div = $('#TradesPoolBody').find("input[name='itemId']").filter("[value='"+obj.itemId+"']");
						$(div).parentsUntil("div").find("input[name='tradeLimitnum']").next().empty();//清空显示挑选限购标志的内容
						$(div).parentsUntil("div").find("input[name='tradeLimitnum']").next().html("<span style='color:red'>已挑选同类限购商品</span>");//显示限购标志
						return 0 //存在限购限制
					}else{
						return 1;//不存在限购限制
					}
				}else{
					   return 1; //不存在相同限购标准的trade
				}
			}else{
				return 1;//该商品不限购
			}
		}
	
		//判断删除订单时的限购限制
		function deLimitNumCheck(trade,obj){
			if(trade.limitnum>0){
				if(trade.itemId == obj.itemId && trade.shopId == obj.shopId){
					if( (trade.skuId == obj.skuId && trade.skuId != null) || typeof(trade.skuId) == "undefined"){//如果该商品没有sku 则说明这个商品在已经关联的右侧存在相同限购商品  如果有sku 而且sku相等说明该型号的商品也存在了限购标准
						return 0 //仍然存在限购限制
					}else{
						return 1;//不存在限购限制
					}
				}else{
					   return 1; //不存在相同限购标准的trade
				}
			}else{
				return 1;//该商品不限购 可以删除
			}
			
		}

		//判断数据添加的合理性
		function equals(trade,obj){
			if(typeof(obj) != "undefined"){		
				if(trade.tradeId != obj.tradeId && trade.cid==obj.cid){//已经关联的订单里面有相同的cid时 判断限购的合理性有没有超出要求
					var cidLabel = $('#TradesPoolBody').find("input[name='tradeItemCid']").filter("[value='"+obj.cid+"']");	
					var allRightItemNum = $(cidLabel).parent().find("label[name='allRightItemNum']").html().trim();//获得所得订单的数量的现值
					allRightItemNum = parseInt(obj.allRightItemNum)+parseInt(trade.num);
					obj.allRightItemNum = allRightItemNum;
					trade.allRightItemNum = allRightItemNum;	
					$(cidLabel).parent().find("label[name='allRightItemNum']").html(allRightItemNum);//设置所得订单的重量为累加值
					if(trade.securitynum>0){//合理性限制大于0  存在类目数量限制
						if(allRightItemNum>trade.securitynum){
							$(cidLabel).parentsUntil("div[class='tradDetail']").parent().find("div").css("background-color","rgba(229, 230, 243, 0.0)");
							var divs=$(cidLabel).parents("div[class='tradDetail']");
							for(var i=0 ;i<divs.length;i++){

								$(divs[i]).css("background-color","rgba(224, 44, 38, 0.2)");//超出了合理性限制 将div的颜色设为红色
							}
							return 1;//提示合理性
						}else{
							return 2;//未超过合理性限制
						}	
					}else{
						return 2;//可添加 该商品不存在不存在和理性要求  
					}
				}else{
					return 3;//可添加  右侧不存在相同cid的商品
				}	
			}
	    }
	   //判断数据删除的修改
	   function dequals(trade,obj){
		   if(typeof(obj) != "undefined"){  
				if(trade.tradeId !=obj.tradeId && trade.cid==obj.cid){ //已经关联的订单里面有相同的cid时 判断限购的合理性有少于超出要求		
					var cidLabel=$('#TradesPoolBody').find("input[name='tradeItemCid']").filter("[value='"+obj.cid+"']");		
					var allRightItemNum=$(cidLabel).parent().find("label[name='allRightItemNum']").html().trim();//获得所得订单的数量的现值
					allRightItemNum=parseInt(allRightItemNum)-parseInt(trade.num);
					obj.allRightItemNum=allRightItemNum;
					trade.allRightItemNum=allRightItemNum;				
					$(cidLabel).parent().find("label[name='allRightItemNum']").html(allRightItemNum);//设置所得订单的数量的现值
					if(trade.securitynum>0){//合理性限制大于0  存在类目数量限制
						if(allRightItemNum<=trade.securitynum){					
							//$(cidLabel).parentsUntil("div").parent().css("background-color","rgba(229, 230, 243, 0.89)");//未超出合理性限制  清为原来的颜色
							var divs=$(cidLabel).parents("div[class='tradDetail']");
							for(var i=0 ;i<divs.length;i++){

								$(divs[i]).css("background-color","rgba(229, 230, 243, 0.89)");//超出了合理性限制 将div的颜色设为红色
							}
							return 1;//取消提示
						}else{
							return 2;//仍然超过合理性限制
						}	
					}else{
						return 2;//不存在和理性要求  可以删除
					}
				}else{
					return 3;//可删除
				}	   
		   }   
	    }
   
		function Map() {     
			this.keys = new Array();//存放键的数组(遍历用到)     
			this.data = new Array();//存放数据     
			//放入一个键值对     
			this.put = function(key, value) {     
				if(this.data[key] == null){     
					this.keys.push(key);     
				}     
				this.data[key] = value;     
			};        		 
			//获取某键对应的值    
			this.get = function(key) {     
				return this.data[key];     
			};     
			  
		   // 删除一个键值对
			this.remove = function(key) {     
				this.keys.remove(key);     
				this.data[key] = null;     
			};     
		} 
		Array.prototype.remove = function(s) {     
			for (var i = 0; i < this.length; i++) {     
				if (s == this[i])     
					this.splice(i, 1);     
			}     
		}	
		function jumpTo(url){
			window.open(url);
		}