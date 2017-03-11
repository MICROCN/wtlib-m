jQuery_1_8_3.extend({
	handleError: function( s, xhr, status, e ) {
	// If a local callback was specified, fire it
	if ( s.error )
	s.error( xhr, status, e );
	// If we have some XML response text (e.g. from an AJAX call) then log it in the console
	else if(xhr.responseText)
	console.log(xhr.responseText);
	}
});

jQuery_1_8_3.extend({
    createUploadIframe: function(id, uri)
	{
			//create frame
            var frameId = 'jUploadFrame' + id;
            var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" style="position:absolute; top:-9999px; left:-9999px"';
			if(window.ActiveXObject)
			{
                if(typeof uri== 'boolean'){
					iframeHtml += ' src="' + 'javascript:false' + '"';

                }
                else if(typeof uri== 'string'){
					iframeHtml += ' src="' + uri + '"';

                }	
			}
			iframeHtml += ' />';
			jQuery_1_8_3(iframeHtml).appendTo(document.body);

            return jQuery_1_8_3('#' + frameId).get(0);			
    },
    
    createUploadForm: function(id, fileElementId, data)
	{
		//create form	
		var formId = 'jUploadForm' + id;
		var fileId = 'jUploadFile' + id;
		var form = jQuery_1_8_3('<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');
		var oldElement = jQuery_1_8_3('#' + fileElementId);
		for(var i in fileElementId){ 
		var oldElement = jQuery_1_8_3('#' + fileElementId[i]);
		if(oldElement.is('form')){
			oldElement=$(oldElement.children());
		}
		var newElement = jQuery_1_8_3(oldElement).clone();
		jQuery_1_8_3(oldElement).attr('id', fileId);
		jQuery_1_8_3(oldElement).before(newElement);
		jQuery_1_8_3(oldElement).appendTo(form);
		}
		//增加文本参数的支持  
        if (data) {  
            for (var i in data) {  
                $('<input type="hidden" name="' + i + '" value="' + data[i] + '" />').appendTo(form);  
            }  
        }  
		//set attributes
		jQuery_1_8_3(form).css('position', 'absolute');
		jQuery_1_8_3(form).css('top', '-1200px');
		jQuery_1_8_3(form).css('left', '-1200px');
		jQuery_1_8_3(form).appendTo('body');		
		return form;
    },

    ajaxFileUpload: function(s) {
        s = jQuery_1_8_3.extend({}, jQuery_1_8_3.ajaxSettings, s);
        var id = new Date().getTime();
        var form =null;
        if(s.form!=null){
        	form=s.form;
        }else{
        	form = jQuery_1_8_3.createUploadForm(id, s.fileElementId,s.data);
        	
        }
		jQuery_1_8_3.createUploadIframe(id, s.secureuri);
		var frameId = 'jUploadFrame' + id;
        // Watch for a new set of requests
        if ( s.global && ! jQuery_1_8_3.active++ )
		{
			jQuery_1_8_3.event.trigger( "ajaxStart" );
		}            
        var requestDone = false;
        // Create the request object
        var xml = {}   ;
        if ( s.global )
            jQuery_1_8_3.event.trigger("ajaxSend", [xml, s]);
        // Wait for a response to come back
        var uploadCallback = function(isTimeout)
		{			
			var io = document.getElementById(frameId);
            try 
			{				
				if(io.contentWindow)
				{
					 xml.responseText = io.contentWindow.document.body?io.contentWindow.document.body.innerHTML:null;
                	 xml.responseXML = io.contentWindow.document.XMLDocument?io.contentWindow.document.XMLDocument:io.contentWindow.document;
					 
				}else if(io.contentDocument)
				{
					 xml.responseText = io.contentDocument.document.body?io.contentDocument.document.body.innerHTML:null;
                	xml.responseXML = io.contentDocument.document.XMLDocument?io.contentDocument.document.XMLDocument:io.contentDocument.document;
				}						
            }catch(e)
			{
				jQuery_1_8_3.handleError(s, xml, null, e);
			}
            if ( xml || isTimeout == "timeout") 
			{				
                requestDone = true;
                var status;
                try {
                    status = isTimeout != "timeout" ? "success" : "error";
                    // Make sure that the request was successful or notmodified
                    if ( status != "error" )
					{
                        // process the data (runs the xml through httpData regardless of callback)
                        //var data = jQuery_1_8_3.uploadHttpData( xml, s.dataType );    
                        // If a local callback was specified, fire it and pass it the data
                        if ( s.success )
                        	var data=$(xml.responseText).html();
                            s.success( eval('(' + data + ')') , status );
    
                        // Fire the global callback
                        if( s.global )
                            jQuery_1_8_3.event.trigger( "ajaxSuccess", [xml, s] );
                    } else
                        jQuery_1_8_3.handleError(s, xml, status);
                } catch(e) 
				{
                    status = "error";
                    jQuery_1_8_3.handleError(s, xml, status, e);
                }

                // The request was completed
                if( s.global )
                    jQuery_1_8_3.event.trigger( "ajaxComplete", [xml, s] );

                // Handle the global AJAX counter
                if ( s.global && ! --jQuery_1_8_3.active )
                    jQuery_1_8_3.event.trigger( "ajaxStop" );

                // Process result
                if ( s.complete )
                    s.complete(xml, status);

                jQuery_1_8_3(io).unbind();

                setTimeout(function()
									{	try 
										{
											jQuery_1_8_3(io).remove();
											jQuery_1_8_3(form).remove();	
											
										} catch(e) 
										{
											jQuery_1_8_3.handleError(s, xml, null, e);
										}									

									}, 100);

                xml = null;

            }
        };
        // Timeout checker
        if ( s.timeout > 0 ) 
		{
            setTimeout(function(){
                // Check to see if the request is still happening
                if( !requestDone ) uploadCallback( "timeout" );
            }, s.timeout);
        }
        try 
		{

			jQuery_1_8_3(form).attr('action', s.url);
			jQuery_1_8_3(form).attr('method', 'POST');
			jQuery_1_8_3(form).attr('target', frameId);
            if(form.encoding)
			{
				jQuery_1_8_3(form).attr('encoding', 'multipart/form-data');      			
            }
            else
			{	
				jQuery_1_8_3(form).attr('enctype', 'multipart/form-data');			
            }			
            jQuery_1_8_3(form).submit();

        } catch(e) 
		{			
            jQuery_1_8_3.handleError(s, xml, null, e);
        }
		
		jQuery_1_8_3('#' + frameId).load(uploadCallback	);
        return {abort: function () {}};	

    },

    uploadHttpData: function( r, type ) {
        var data = !type;
        data = type == "xml" || data ? r.responseXML : r.responseText;
        // If the type is "script", eval it in global context
        if ( type == "script" )
            jQuery_1_8_3.globalEval( data );
        // Get the JavaScript object, if JSON is used.
        if ( type == "json" )
            eval( "data = " + data );
        // evaluate scripts within html
        if ( type == "html" )
            jQuery_1_8_3("<div>").html(data).evalScripts();

        return data;
    }
});

