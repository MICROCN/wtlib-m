function setFormValue(formId,row){
	$('#'+formId+' input').each(function(){
		className=this.class;
		var name = this.name;
		var val=this.value;
		var value="";
		var type=this.type;
		if(name!=null&&name!=""){
			//if(name.indexOf(".")>0){ //spring 提交到后台的时候，name的值就是我们entity的属性
				name=name.substring(name.indexOf(".")+1,name.length);
				if(name.indexOf(".")>0){
					var arr=name.split(".");
					if(row[arr[0]] != null){
						value=row[arr[0]][arr[1]];
						name=arr.join("");
					}					
				}else{
					value=row[name];
				}
				if(type=='hidden'){
					if($('#'+name)){
						$('#'+name).textbox('setValue', value);	
					}
        		}else if(type="radio"){
        			if(val==value){
        				$(this).attr("checked","checked");
        			}
        		}else if(type="checkbox"){
        			if(val==value){
        				$(this).attr("checked","checked");
        			}
        		}
			//}
		}
	   /*if(type=="hidden"){
		  
	   }*/
	});
}