//�ӳٵִ�
function delayReceive(obj){
	 var btn=$(obj);
		var tradeId= btn.closest("table").find("input[name='tradeId3']").val(); 
		var stradeId= btn.closest("table").find("input[name='stradeId3']").val();
		$.post("$globalVar.basePath()/sharer/strade/delayReceive.htm?tradeId="+tradeId,function(data){
				if(data.status=="success"){
						alert("�����ɹ�!");
						if(data.code=="true"){
								 location.reload();
								 return;
					  }
						btn.addClass("chidden");
		        btn.parents().children("span").removeClass("chidden");
						btn.attr("disabled","disabled");
						var num=parseInt(btn.closest("div[id='receiveWait_main']").find("input[name='order_wait_num']").val())+1;
						var allNum=parseInt(btn.closest("div[id='receiveWait_main']").find("input[name='order_wait_all']").val());
						btn.closest("div[id='receiveWait_main']").find("input[name='order_wait_num']").val(num);
						btn.closest("div[id='receiveWait_main']").find("span[name='officialWait']").html('��ȷ��'+"("+num+"/"+allNum+")");
					
				}else{
				   alert(data.message);
				} 
		},"json");
}

//�ӳ�Сc�ջ�ʱ��
function addDelayconfirm(obj){
	  var btn=$(obj);
		var tradeId= btn.closest("table").find("input[class='tradeId']").val(); 
		var daynum= btn.prev("select").val();
		$.post("$globalVar.basePath()/sharer/strade/delayConfirm.htm?tradeId="+tradeId+"&dayNum="+daynum,function(data){
				if(data.status=="success"){
						alert("�����ɹ�!");
					  location.reload();
						return;
				}else{
				   alert(data.message);
				} 
		},"json");
}

//��cȷ�Ϸ���
function sureSendgoods(obj){
	 var btn=$(obj);
	 
		var tradeId= $(obj).closest("table").find("input[class='tradeId']").val(); 
		var stradeId= $(obj).closest("table").find("input[class='stradeId']").val(); 
		var domesticCompany=$(obj).closest("td").find("input[name='company_home']").val();
		var domesticId=$(obj).closest("td").find("input[name='logis_num']").val();
		var radioVal=$(obj).closest("td").find("input[type='radio']:checked").val();
		if(radioVal==1){
			if(domesticCompany==null || domesticCompany==""){
				alert("��ѡ���ݹ�˾");
				return false;
			}
			if(domesticId==null || domesticId==""){
				alert("����д�˵���");
				return false;
			}
		}
		
		$.post("$globalVar.basePath()/sharer/trade/checkSendItem2.htm?tradeId="+tradeId,function(data){
			
			if(data.status=="success"){
				if(radioVal==1){
					if(domesticCompany==null || domesticCompany==""){
						alert("��ѡ���ݹ�˾");
						return false;
					}
					if(domesticId==null || domesticId==""){
						alert("����д�˵���");
						return false;
					}
				}
				$.post("$globalVar.basePath()/sharer/strade/sendItemInHome.htm?tradeId="+tradeId+"&stradeId="+stradeId+"&domesticCompany="+domesticCompany+"&domesticId="+domesticId+"&type="+radioVal,function(data2){
					if(data2.status=="success"){
	            var href='';
						  
					}else{
						alert("����ʧ��");
					}
				},"json");
			}else{
				alert("�ظ������������ѷ���");
			}
		},"json");
}

$(function(){
	  $('.clock').each(function(){
	      var d=$(this).prev('.latestdate').val();
	      if(d!=null&&d!=undefined&&d!=''){
	      	 $(this).countdown(d, function(event) {
						   $(this).html(event.strftime('��ʣ%D��%HСʱ%M��'));
					 });
	      }	
	      	
	  }); 
	  
	  $('input[name="addDelayreceivetime"]').click(function(){
	  	  delayconfirm(this);
	  });
	  
	  $('input[name="addDelayreceivetime"]').click(function(){
	  	  delayconfirm(this);
	  });
	  
	  
	  
});
elayreceivetime"]').click(function(){
	  	  delayconfirm(this);
	  });
	  
	  
	  
});
