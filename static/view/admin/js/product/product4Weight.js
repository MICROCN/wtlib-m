$(function(){
		/**当输入框输入内容时触发**/
		$(".toSetInput").attr("readonly",true);
		$(".toSetInput").focus(function(){
			 $(this).focusEnd(); //将鼠标移到末尾
			 $(this).attr("readonly",false);
		});	
		
		$('.toSetInput').bind({mouseover:function(e){
				$(this)[0].focus();
			}
		});
		
		$.fn.setCursorPosition = function(position){  
			if(this.lengh == 0) return this;  
			return $(this).setSelection(position, position);  
		}  
      
		$.fn.setSelection = function(selectionStart, selectionEnd) {  
			if(this.lengh == 0) return this;  
			input = this[0];  
		  
			if (input.createTextRange) {  
				var range = input.createTextRange();  
				range.collapse(true);  
				range.moveEnd('character', selectionEnd);  
				range.moveStart('character', selectionStart);  
				range.select();  
			} else if (input.setSelectionRange) {  
				input.focus();  
				input.setSelectionRange(selectionStart, selectionEnd);  
			}  
		  
			return this;  
		}  
		  
		$.fn.focusEnd = function(){  
			this.setCursorPosition(this.val().length);  
		}  
	});

//保存重量
function saveWeights(){
	   $('#saveWeight').next().empty();//清空 的信息提示
	   //检查checkbox选择了几个全选按钮决定保存方式
	   var itemId = $("#itemChoosed").attr("itemIda");
	   var checks = $("#skuTable").find("input[type='checkbox']:checked");
	   var weight = parseInt($('#setWeight').val());
	   weightWay = checks.length;  
    if(weightWay == 0){//单项设置重量 
		var size = $("#saveWeight").attr("ipSize").trim();
		if(size == 0 || typeof(size) == "undefined"){
			setItemWeight(itemId,weight);
		}else{
			// 保存sku 重量
			var props = "";
			$('#skuTable input[type="radio"]:checked').each(function(index,domEle){
				props = props + $(domEle).attr("data-val") + ",";
			});
			props = props.substr(0,props.length - 1);
			saveSkuWeight(props,weight,itemId);//设置重量
		}	
	}else if(weightWay == 1){//一行多选设置重量
		var popArrayS = new Array();
		var ipSize = $("#saveWeight").attr("ipSize");
		var checkedBox = $("#skuTable").find("input[type='checkbox']:checked");
		var checkBoxId = $(checkedBox).attr("name");	
		var rowId = checkBoxId.substr(checkBoxId.indexOf("_")+1);//判断是第几行被全选
		var inputProps= $(checkedBox).parents("td").next().find("input[type='radio']");	
		inputProps.each(function(i,pro){
			popArrayS[i]=$(pro).attr("data-val");
		});
		//拼接props
		var props="";		
		if (ipSize == 1){//如果只有一行
			for(var i=0;i<popArrayS.length;i++){
				props=popArrayS[i];	
				saveSkuWeight(props,weight,itemId);//设置重量
			}
		}else{
			var uncheckedBox = $("#skuTable").find("input[type='checkbox']:not(:checked)");
			var singleProp= $(uncheckedBox).parents("td").next().find("input[type='radio']").attr("data-val");
			if(rowId == "1"){
				for(var i=0;i<popArrayS.length;i++){
				  props=popArrayS[i]+","+singleProp;
			   	  saveSkuWeight(props,weight,itemId);//设置重量
				}
			}else{
				for(var i=0;i<popArrayS.length;i++){
				  props=singleProp+","+popArrayS[i];
			   	  saveSkuWeight(props,weight,itemId);//设置重量
				}
			}				
		}
	}else if(weightWay == 2){
		var props=""
		var popArrayS = new Array();
		$("#skuTable").find("input[type='checkbox']:checked").each(function(dex,checkBox){
			var index=dex;
			var inputs=$(checkBox).parents("td").next().find("input[type='radio']");
			popArrayS[index]=new Array();
			for(var i=0;i<inputs.length;i++){
				popArrayS[index][i]=$(inputs[i]).attr("data-val");
			}
		});
		for(var i=0;i<popArrayS[0].length;i++){
			for(var j=0;j<popArrayS[1].length;j++){
				props=popArrayS[0][i]+","+popArrayS[1][j];
				if(props != ","){
					saveSkuWeight(props,weight,itemId);//设置重量
				}	
			}
		}
	
	}
}
//设置sku重量
function saveSkuWeight(props,weight,itemId){	
		if(checkSkuWeightSet(weight,props,itemId)){  //只有有sku的产品才能发送ajax请求进行保存
			weight =Math.round( parseInt(weight) * 1.1);
			$.ajax({
				type: "POST",
				async: false,
				url: basePath + '/product/setProductSkuWeight.htm',
				data:"props=" + props +" &weight=" + weight +"&itemId=" + itemId,
				dataType:'json',
				success: function(msg){			
					 if(msg ==new String("success")){
						$('#saveWeight').next().html('&nbsp;&nbsp;&nbsp;<span style="color:red;">保存成功</span>');   
					 }
				}
			});	 
		}else{
			$('#saveWeight').next().html('&nbsp;&nbsp;&nbsp;<span style="color:red;">请选择属性或者设置重量(重量必须大于0)</span>');   
		} 
}
//没有sku 设置商品重量
function setItemWeight(itemId,itemWeight){
	if(checkWeightSet(itemId,itemWeight)){
		itemWeight = Math.round(parseInt(itemWeight)*1.1);
		$.ajax({
		   type: "POST",
		   url: basePath + '/item/setItemWeight.htm',
		   data:"itemId=" + itemId+"&weight=" + itemWeight,
		   success: function(msg){	   		 
				 $('#saveWeight').next().html('&nbsp;&nbsp;&nbsp;<span style="color:red;">保存成功</span>');   
		   }
		});		
	}else{
		$('#saveWeight').next().html('&nbsp;&nbsp;&nbsp;<span style="color:red;">请选择属性或者设置重量(重量必须大于0)</span>');   
	}
}
//判断sku重量是否可以设置
function checkSkuWeightSet(weight,props,itemId){
	if(weight == null || typeof(weight) == "undefined" || parseInt(weight) <= 0 || itemId == null || typeof(itemId) == "undefined" || props == null ||typeof(props) == "undefined"){
		return false;
	}else{
		return true;
	}
}
function checkWeightSet(itemId,itemWeight){
	if(itemWeight == null || typeof(itemWeight) == "undefined" || parseInt(itemWeight) <= 0 || itemId == null || typeof(itemId) == "undefined" ){
		return false;
	}else{
		return true;
	}
	
}
//品牌的查询代码
var brandNamefill=0
function searchBrands(){
	$(".autoSearch div").empty();
	
	var brandName=$("#searchBrand").val().trim();
	if(brandName == "" || typeof(brandName) == "undefined"){
		$(".autoSearch div").hide();
	}else if(brandNamefill == 0){
		url=basePath+"/brandList.htm?name="+brandName;
		$.post(url,function(data){
			var brands=data.rows;
			if(brands.length<6 && brands.length>0){
				var contain = $(".autoSearch div");
				var newHeight = brands.length*20;
				$(contain).css("height",newHeight+"px");
				$(".autoSearch div").show();
			}else if(brands.length>=6){
				$(".autoSearch div").css("height","120px");
				$(".autoSearch div").show();
			}else{
				$(".autoSearch div").hide();
			}
			
			for(var i=0;i<brands.length;i++){
				$('#brandLists').append("<span name='pBrandNames' bid='"+brands[i].brandId+"'>"+brands[i].name+"</span><br>")	
			}		
		},'json');	
	
	}
}
 $("span[name='pBrandNames']").live('click',function(){
	brandNamefill=1;
	var brandNameChoosed=$(this).text();
	var brandIdChoosed = $(this).attr("bid");
	$("#searchBrand").val(brandNameChoosed);
	$("#brand").val(brandIdChoosed);
	brandNamefill=0;
	$('#brandLists').empty();
	$('#brandLists').hide();
 });
 //点击sku查询出有sku的商品的重量
 function searchSkuWeight(obj){
	var itemId = $("#itemChoosed").attr("itemIda");
	$("#crawlerWeight").html();
	var props = "";
	$('#saveWeight').next().empty();//清空 的信息提示
	$('#skuTable input[type="radio"]:checked').each(function(index,domEle){
		props = props + $(domEle).attr("data-val") + ",";
	}); 
	var size = $("#saveWeight").attr("ipSize").trim();
	var checked = $('#skuTable input[type="radio"]:checked');
	if(size == 2 && checked.length < 2){
		//alert(size);
		props = props.substr(0,props.length - 1);
		propsA = $(obj).attr("data-val");
		grayOthers(obj,itemId,propsA);
	}else{
		props = props.substr(0,props.length - 1);
		propsA = $(obj).attr("data-val");
		grayOthers(obj,itemId,propsA);
		$.ajax({
		   type: "POST",
		   url:basePath + "/product/getProductSkuWeight.htm",
		   data:"props=" + props +"&itemId=" + itemId,
		   dataType: 'json',  
		   async:false,
		   success: function(result){
			  if(result != null){
			  	$('#setWeight').val(Math.round(parseFloat(result.weight)/1.1));
			  	$("#crawlerWeight").html(result.crawler_weight);
				$('#saveWeight').next().html('&nbsp;&nbsp;&nbsp;<span style="color:red;">查询成功</span>');  
			  }else{
				$('#setWeight').val();
				$('#saveWeight').next().html('&nbsp;&nbsp;&nbsp;<span style="color:red;">注意:本商品的 该sku组合 不存在!</span>');   
			  }
		   }
		});
	}
	
}
function grayOthers(obj,itemId,props){
	
	var checkedBox = $(obj).parents("td").prev().find("input[type='checkbox']");
	var checkBoxId = $(checkedBox).attr("name");	
	var rowId = checkBoxId.substr(checkBoxId.indexOf("_")+1)
	//(rowId);
	//alert(props);
	if(rowId == "1"){
			var radios = $(obj).parents("tr").next().find("input:radio"); 
			for(var i=0;i<radios.length;i++){
			  var allProps = "";
			  allProps=props+","+$(radios[i]).attr("data-val");
			  //alert(allProps);
			  isGray($(radios[i]),itemId,allProps);//是否灰化
			}
		}else{
			var radios = $(obj).parents("tr").prev().find("input:radio"); 
			for(var i=0;i<radios.length;i++){
			  var allProps = "";
			  allProps =$(radios[i]).attr("data-val")+","+props;
			  isGray($(radios[i]),itemId,allProps);//是否灰化
			}
		}
}

//判断是否置黑
function isGray(obj,itemId,props){
	$.ajax({
		   type: "POST",
		   url:basePath + "/product/getProductSkuWeight.htm",
		   data:"props=" + props +"&itemId=" + itemId,
		   dataType: 'json',  
		   async:false,
		   success: function(result){
			  if(result != null){
				$(obj).attr("disabled",false);  
				return true;
			  }else{
				$(obj).attr("disabled",true);
				return false;
			  }
		   }
		});
	
	
}

//点击链接进行跳转
function jumpTo(url){
		window.open(url);
}
//全选属性
function chooseAll(obj){
	if($(obj).is(':checked')){
		var tds=$(obj).parents("td").nextAll();
		$(tds).each(function(index,radio){
			$(radio).find("input[type='radio']").attr("disabled",true);
			$(radio).find("input[type='radio']").attr('checked',false);
		});
	}else{
		var tds=$(obj).parents("td").nextAll();
		$(tds).each(function(index,radio){
			$(radio).find("input[type='radio']").attr("disabled",false);
			$(radio).find("input[type='radio']").attr('checked',false);
		});
	}		
}
//设置为主图
function setAsMainPic(obj){
	//设置该图为主图
	if(preMainPic != obj){
		$(obj).siblings("a").hide();
		//设置上一个图为幅图
		$(preMainPic).siblings("a").show();
		preMainPic = obj;
	}else{
		
	}	
}

//移除图片覆盖
function removeCorrect(obj){
		$(obj).hide();		
}
//添加图片覆盖
function addCorrect(obj){
	$(obj).siblings("a").show();
	var radio=$(obj).siblings("input:radio");
	if($(radio).is(":checked")){
		$(radio).attr("checked",false);
		preMainPic = undefined;
	}
}
//保存pid的中文名
function saveItemPropertyName(itemId,pids){
	 var url = basePath+"/item/updateItemProperty.htm?itemId="+itemId+"&pidAndCnPnames="+pids;
	 $.post(url,'',function(data){
	 	if(data.status == "success"){
	 		
	 	}else{
	 		alert("保存属性名"+pids+"失败");
	 	}
	 },'json');
}
//保存vid的中文名
function saveItemPropertyValueName(itemId,vids){
	var url = basePath+"/item/updateItemPropertyValue.htm?itemId="+itemId+"&pidAndCnPvnames="+vids;
	 $.post(url,'',function(data){
	 	if(data.status == "success"){
	 		
	 	}else{
	 		alert("保存属性名"+vids+"失败");
	 	}
	 },'json');
}