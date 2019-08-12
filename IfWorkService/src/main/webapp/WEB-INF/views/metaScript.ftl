<!-- Optional JavaScript -->

<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="${rc.getContextPath()}/jQuery-3.4.1/jquery-3.4.1.min.js"></script>
<!-- <script src="${rc.getContextPath()}/jQuery-3.4.1/jquery.datepicker.js"></script> -->
<script src="${rc.getContextPath()}/jQuery-3.4.1/jquery.mask.js"></script>
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

<script type="text/javascript">
 	var Util = {
		ajax : function(option){ 
			var defaultoOption = { 
				beforeSend : function(xhr){  
		            xhr.setRequestHeader("AJAX", true); //필수
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
</script>
   