function addCache(){
	$('#addCacheForm').form('submit',{   
   		url:basePath+"/cache/putCache.htm",  
   	    success:function(result){    		
   	    	alert(result);  		
   	    }
   	});
}


function deleteCache(){
		$('#memForm').form('submit',{   
   		url:basePath+"/cache/deleteCache.htm",  
   	    success:function(result){    		
   	    	alert(result);  		
   	    }
   	});
	
	
}
