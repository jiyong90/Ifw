<!-- Optional JavaScript -->

<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="${rc.getContextPath()}/jQuery-3.4.1/jquery-3.4.1.min.js"></script>
<script src="${rc.getContextPath()}/jQuery-3.4.1/jquery.datepicker.js"></script>
<script src="${rc.getContextPath()}/jQuery-3.4.1/jquery.mask.js"></script>
<script src="${rc.getContextPath()}/jQuery-3.4.1/jquery-ui.min.js"></script>
<script src="${rc.getContextPath()}/jQuery-3.4.1/jquery-migrate-3.0.0.min.js"></script>
<script src="${rc.getContextPath()}/jQuery-3.4.1/datepicker_lang_KR.js"></script>
<script src="${rc.getContextPath()}/jQuery-slimScroll-master-1.3.8/jquery.slimscroll.min.js"></script>
<script src="${rc.getContextPath()}/popper-1.15.0/popper.min.js"></script>
<script src="${rc.getContextPath()}/moment/moment.js"></script>
<script src="${rc.getContextPath()}/moment/ko.js"></script>
<script src="${rc.getContextPath()}/tooltip-1.3.2/tooltip.min.js"></script>
<script src="${rc.getContextPath()}/bootstrap-4.3.1-dist/js/bootstrap.min.js"></script>

<!-- for dateTimepicker -->
<script src="${rc.getContextPath()}/tempusdominus-bootstrap4-5.0.0-alpha14/tempusdominus-bootstrap-4.min.js"></script>
<!-- <script src="${rc.getContextPath()}/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script> -->
<!-- <script src="${rc.getContextPath()}/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.ko.js"></script> --> 
	
<!-- Vue 2.5.13 -->
<script src="${rc.getContextPath()}/vue-2.5.13/dist/vue.min.js"></script>
<script src="${rc.getContextPath()}/vue-2.5.13/vue-cookies.js"></script>
<script src="${rc.getContextPath()}/vue-2.5.13/vue-upload-component/vue-upload-component.js"></script>
  
<!-- IBSheet -->
<script src="${rc.getContextPath()}/IBLeaders/Sheet/js/ibleaders.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/IBLeaders/Sheet/js/ibsheetinfo.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/IBLeaders/Sheet/js/ibsheet.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/IBLeaders/Sheet/js/ibsheetcalendar.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/IBLeaders/common.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/IBLeaders/commonIBSheet.js" type="text/javascript"></script>

<script src="${rc.getContextPath()}/jBox-1.0.5/dist/jBox.all.min.js" type="text/javascript"></script>

<!-- main guide slide -->
<script src="${rc.getContextPath()}/bxslider-4-master/src/js/jquery.bxslider.js"></script>

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
    	halfsheetH = "50%";
		fullsheetH = "100%";
		sheetH90 = "100%";
		sheetH80 = "100%";
    </#if>
		
</script>
