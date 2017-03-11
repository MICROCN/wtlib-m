$.extend($.fn.datagrid.methods, {
	statistics: function (jq) {
		var opt=$(jq).datagrid('options').columns;
		var rows = $(jq).datagrid("getRows");
		
		var footer = new Array();
		footer['sum'] = "";
		footer['avg'] = "";
		footer['max'] = "";
		footer['min'] = "";
		footer['sumForSalesStatis'] = "";
		
		for(var i=0; i<opt[0].length; i++){
			if(opt[0][i].sum){
				footer['sum'] = footer['sum'] + sum(opt[0][i].field)+ ',';
			}
			if(opt[0][i].sumForSalesStatis){
				footer['sumForSalesStatis'] = footer['sumForSalesStatis'] + sumForSalesStatis(opt[0][i].field)+ ',';
			}
			if(opt[0][i].avg){
				footer['avg'] = footer['avg'] + avg(opt[0][i].field)+ ',';
			}
			if(opt[0][i].max){
				footer['max'] = footer['max'] + max(opt[0][i].field)+ ',';
			}
			if(opt[0][i].min){
				footer['min'] = footer['min'] + min(opt[0][i].field)+ ',';
			}
		}

		var footerObj = new Array();
		
		if(footer['sum'] != ""){
			var tmp = '{' + footer['sum'].substring(0,footer['sum'].length - 1) + "}";
			var obj = eval('(' + tmp + ')');
			if(obj[opt[0][0].field] == undefined){
				footer['sum'] += '"' + opt[0][0].field + '":"<b>当页合计:</b>"';
				obj = eval('({' + footer['sum'] + '})');
			}else{
				obj[opt[0][0].field] = "<b>当页合计:</b>" + obj[opt[0][0].field];
			}
			footerObj.push(obj);
		}
		
		if(footer['sumForSalesStatis'] != ""){
			var tmp = '{' + footer['sumForSalesStatis'].substring(0,footer['sumForSalesStatis'].length - 1) + "}";
			var obj = eval('(' + tmp + ')');
			if(obj[opt[0][0].field] == undefined){
				footer['sumForSalesStatis'] += '"' + opt[0][0].field + '":"<b>当页合计:</b>"';
				obj = eval('({' + footer['sumForSalesStatis'] + '})');
			}else{
				obj[opt[0][0].field] = "<b>当页合计:</b>" + obj[opt[0][0].field];
			}
			footerObj.push(obj);
		}
		
		if(footer['avg'] != ""){
			var tmp = '{' + footer['avg'].substring(0,footer['avg'].length - 1) + "}";
			var obj = eval('(' + tmp + ')');
			if(obj[opt[0][0].field] == undefined){
				footer['avg'] += '"' + opt[0][0].field + '":"<b>当页均值:</b>"';
				obj = eval('({' + footer['avg'] + '})');
			}else{
				obj[opt[0][0].field] = "<b>当页均值:</b>" + obj[opt[0][0].field];
			}
			footerObj.push(obj);
		}
		
		if(footer['max'] != ""){
			var tmp = '{' + footer['max'].substring(0,footer['max'].length - 1) + "}";
			var obj = eval('(' + tmp + ')');
			
			if(obj[opt[0][0].field] == undefined){
				footer['max'] += '"' + opt[0][0].field + '":"<b>最大值:</b>"';
				obj = eval('({' + footer['max'] + '})');
			}else{
				obj[opt[0][0].field] = "<b>最大值:</b>" + obj[opt[0][0].field];
			}
			footerObj.push(obj);
		}
		
		if(footer['min'] != ""){
			var tmp = '{' + footer['min'].substring(0,footer['min'].length - 1) + "}";
			var obj = eval('(' + tmp + ')');
			
			if(obj[opt[0][0].field] == undefined){
				footer['min'] += '"' + opt[0][0].field + '":"<b>最小值:</b>"';
				obj = eval('({' + footer['min'] + '})');
			}else{
				obj[opt[0][0].field] = "<b>最小值:</b>" + obj[opt[0][0].field];
			}
			footerObj.push(obj);
		}
		
		
		
		if(footerObj.length > 0){
			$(jq).datagrid('reloadFooter',footerObj); 
		}
		
		
		function sum(filed){
			var sumNum = 0;
			for(var i=0;i<rows.length;i++){
				sumNum += Number(rows[i][filed]);
			}
			return '"' + filed + '":"' + sumNum.toFixed(2) +'"';
		};
		
		function sumForSalesStatis(filed){
			var sumNum = 0;
			if(filed=="realForeignFreight"||filed=="realInsuranceCharge"||filed=="realFactorage"||filed=="financeFee"){
				var curNum = -999999999;
				var s = '';
				for(var i=0;i<rows.length;i++){
					if(rows[i]['planNo'] != s){
						sumNum += Number(rows[i][filed]);
						curNum = Number(rows[i][filed]);
						s = rows[i]['planNo'];
					}
				}
				return '"' + filed + '":"' + sumNum.toFixed(2) +'"';
			}else{
				for(var i=0;i<rows.length;i++){
					sumNum += Number(rows[i][filed]);
				}
				return '"' + filed + '":"' + sumNum.toFixed(2) +'"';
			}
			
		};
		
		function avg(filed){
			var sumNum = 0;
			for(var i=0;i<rows.length;i++){
				sumNum += Number(rows[i][filed]);
			}
			return '"' + filed + '":"'+ (sumNum/rows.length).toFixed(2) +'"';
		}

		function max(filed){
			var max = 0;
			for(var i=0;i<rows.length;i++){
				if(i==0){
					max = Number(rows[i][filed]);
				}else{
					max = Math.max(max,Number(rows[i][filed]));
				}
			}
			return '"' + filed + '":"'+ max +'"';
		}
		
		function min(filed){
			var min = 0;
			for(var i=0;i<rows.length;i++){
				if(i==0){
					min = Number(rows[i][filed]);
				}else{
					min = Math.min(min,Number(rows[i][filed]));
				}
			}
			return '"' + filed + '":"'+ min +'"';
		}
	}
});