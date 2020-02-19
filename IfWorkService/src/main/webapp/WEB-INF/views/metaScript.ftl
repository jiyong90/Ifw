<#include "/scriptZip.ftl">
<script type="text/javascript">
    
    var Util = {
		ajax : function(option){ 
			var defaultoOption = { 
				beforeSend : function(xhr){  
		            xhr.setRequestHeader("AJAX", true); //필수
				}, 
		 		complete : function(data) {
					var text = data.responseText;
					if( typeof(redirectText) != 'undefined' && text.indexOf(redirectText) != -1)
        				window.parent.location.href=loginUrl;
                },
				error : this.ajaxError	
			};
			var defaultoOption = $.extend(true, defaultoOption, option);
			
			$.ajax(defaultoOption);
		},
		ajaxError : function(xhr, status, error){
			if(xhr.status == 401){
				location.href = "${rc.getContextPath()}/login/${tsId}"; 
			}else{
				location.href = "${rc.getContextPath()}/login/${tsId}";
			}
		}
	};
	function getEncodeURI(param){
	    var shallowEncoded = $.param( param, true ); 
	    //var shallowDecoded = decodeURIComponent( shallowDecoded );
	    return shallowEncoded;    
	};
	
	function minuteToHHMM(min, type) {
		if(min!=null && min!=undefined && min!='') {
    		if(type==null || type=='')
	   	    	type='short';
    		
	   	    var min = Number(min);
	   	    var hours   = Math.floor(min / 60);
	   	    var minutes = Math.floor(min - (hours * 60));

	   	 	if(type=='detail') {
	   	 		var h = hours==0?'':hours+'시간 ';
	   	 		var m = minutes==0?'':minutes+'분';
	   	    	return h+''+m;
	   	 	}
	   	    	
	   	    if (hours   < 10) {hours   = "0"+hours;}
	   	    if (minutes < 10) {minutes = "0"+minutes;}
	   	    
	   	    if(type=='short')
	   	   		return hours+':'+minutes;
		} else {
			return '';
		}
   	};
	
 	$(document).on('show.bs.modal', '.modal', function (event) {
        var zIndex = 1040 + (10 * $('.modal:visible').length);
        $(this).css('z-index', zIndex);
        setTimeout(function() {
            $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
        }, 0);
    });
</script>
<script type="text/javascript">
    var sheetH40 = "calc(50vh - 180px)";
    var halfsheetH = "calc(50vh - 140px)";
    var fullsheetH = "calc(100vh - 232px)";
    var sheetH90 = "calc(100vh - 270px)";
    var sheetH80 = "calc(100vh - 308px)";

    <#if isEmbedded?? && isEmbedded?exists && isEmbedded >
    	sheetH40 = "calc(50vh - 134px)";
    	halfsheetH = "calc(50vh - 94px)";
		fullsheetH = "calc(100vh - 140px)";
		sheetH90 = "calc(100vh - 178px)";
		sheetH80 = "calc(100vh - 216px)";
    </#if>
		
</script>
