/**
 * 控制对话框移动范围
 */
function inWindowMove(){
		var left = $(this).dialog('dialog').position().left;
		var top = $(this).dialog('dialog').position().top;
		var width = $(this).dialog('options').width;
		var height = $(this).dialog('options').height;
		var wiWidth = $(window).width();
		var wiHeight = $(window).height();
		if (left < 0){
		 	$(this).dialog('move',{
		 	left:0
		 	});
		}
		if (top < 0){
		 	$(this).dialog('move',{
		 	top:0
		 	});
		}
		if (left > wiWidth-20){
		 	$(this).dialog('move',{
		 	left:wiWidth-20
		 	});
		}
		if (top > wiHeight-20){
		 	$(this).dialog('move',{
		 	top:wiHeight-20
		 	});
		}
		
}

/**
 * 初始化对话框显示位置
 */
function dialogCenter(){
		var width = $(this).dialog('options').width;
		var height = $(this).dialog('options').height;
		var wiWidth = $(window).width();
		var wiHeight = $(window).height();
		var left = (wiWidth-width)*0.5;
		var top = (wiHeight-height)*0.4;
//		alert($(this).dialog('options').width);
//		alert($(this).dialog('options').height);
		if (left < 0){
		 	$(this).dialog('move',{
		 	left:0
		 	});
		}else{
			$(this).dialog('move',{
				left:left
			});
		}
		if (top < 0){
		 	$(this).dialog('move',{
		 	top:0
		 	});
		}else{
			$(this).dialog('move',{
				top:top
			});
		}
	
}	

//单元格合并方法
	function mergeCellsByField(tableID,colList){
		var ColArray = colList.split(",");
		var tTable = $("#" + tableID);
	    var TableRowCnts = tTable.datagrid("getRows").length;
	    var TableColCnts = ColArray.length - 1;
	    var tmpA = 1;
	    var tmpB = 0;
	    var PerTxt = "";
	    var CurTxt = "";
	    
	    for (var i = 0; i <= TableRowCnts; i++){
	    	if (i == TableRowCnts){
	    		CurTxt = "";
	    	}
	    	else{
	    		CurTxt = tTable.datagrid("getRows")[i][ColArray[0]];
	    	}
	    	if (PerTxt == CurTxt){
	    		tmpA += 1;
	    	}
	    	else{
	    		tmpB += tmpA;    		
	    		for (var j = TableColCnts; j >= 0; j--) {
	                tTable.datagrid("mergeCells", {
	                    index: i - tmpA,
	                    field: ColArray[j],//合并字段
	                    rowspan: tmpA,
	                    colspan: null
	                });
	            }
	    		tmpA = 1;
	    	}
	    	PerTxt = CurTxt;
	    }
	    
	}
	
	
	 function formatterDate(val) {
    	if (val != null) {
    	var date = new Date(val);
    	return date.getFullYear() + '年' + (date.getMonth() + 1) + '月'
    	+ date.getDate()+'日'
    	+ date.getHours()+':'
    	+ date.getMinutes();
    	}
   	}