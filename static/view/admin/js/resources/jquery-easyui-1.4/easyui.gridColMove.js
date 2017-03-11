$.extend($.fn.datagrid.methods,{
	columnMoving:function(jq){
		return jq.each(function(){
			var grid = this;
			
			var directionDiv = $("<div></div>");
			
			directionDiv.hide();
			
			$("body").append(directionDiv);
			
			$(grid).datagrid("getPanel")
					.find(".datagrid-header td[field]:not(td[field='ckb'])").draggable({
				revert:true,
				cursor:"move",
				deltaX:10,
				deltaY:10,
				edge:10,
				proxy:function(source){
					var proxyEl = $("<div></div>");
					
					proxyEl.addClass("dg-proxy dg-proxy-error");
					
					proxyEl.text($(source).text());
					
					proxyEl.appendTo($("body"));
					
					return proxyEl;
				}
			}).droppable({
				accept:".datagrid-header td[field]",
				onDragOver:function(e,source){
					$(source).draggable("proxy").removeClass("dg-proxy-error").addClass("dg-proxy-right");
					
					$(".dg-hide-div").hide();
					
					var thisIndex = $(this).index();
					var sourceIndex = $(source).index();
					
					var className = null;
					
					var height = null;
					
					var thisOffset = null;
					
					var left = null;
					var top = null;
					
					height = $(this).height();
					
					if(sourceIndex > thisIndex){
						className = "dg-move-prev";

						thisOffset = $(this).offset();
						
						left = thisOffset.left;
						top = thisOffset.top;
					}else{
						className = "dg-move-next";
						
						if(thisIndex == $(this).parent().children(":last").index()){
							thisOffset = $(this).offset();
							
							left = thisOffset.left + $(this).width() - directionDiv.width();
							top = thisOffset.top;
						}else{
							thisOffset = $(this).next().offset();
							
							left = thisOffset.left - directionDiv.width();
							top = thisOffset.top;
						}
					}
					
					directionDiv.removeClass().addClass(className);
					directionDiv.css({height:height, left:left, top:top});
					directionDiv.show();
				},
				onDragLeave:function(e,source){
					$(source).draggable("proxy").removeClass("dg-proxy-right").addClass("dg-proxy-error");
					
					directionDiv.hide();
				},
				onDrop:function(e,source){
					directionDiv.remove();
					
					var thisIndex = $(this).index();
					var sourceIndex = $(source).index();
					
					var sourceCol = new Array();
					
					$(source).remove();
					$.each($(grid).datagrid("getPanel")
									.find(".datagrid-body tr"),function(index,obj){
						var sourceTd = $(obj).children("td:eq(" + sourceIndex + ")");
						
						sourceCol.push(sourceTd);
						
						sourceTd.remove();
					});
					
					var prev = sourceIndex > thisIndex;
					
					thisIndex = $(this).index();
					
					if(prev){
						$(this).before($(source));
					}else{
						$(this).after($(source));
					}
					
					$.each($(grid).datagrid("getPanel")
									.find(".datagrid-body tr"),function(index,obj){
						var thisTd = $(obj).children("td:eq(" + thisIndex + ")");
						
						if(prev){
							thisTd.before(sourceCol[index]);
						}else{
							thisTd.after(sourceCol[index]);
						}
					});
					
					$(grid).datagrid("columnMoving").datagrid("columnHiding");
				}
			});
		});
	}
});