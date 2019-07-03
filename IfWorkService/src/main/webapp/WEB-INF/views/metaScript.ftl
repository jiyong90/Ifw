	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="${rc.getContextPath()}/jQuery-3.4.1/jquery-3.4.1.min.js"></script>
	<script src="${rc.getContextPath()}/popper-1.15.0/popper.min.js"></script>
	<script src="${rc.getContextPath()}/bootstrap-4.3.1-dist/js/bootstrap.min.js"></script>
	<script src="${rc.getContextPath()}/moment/moment.js"></script>
	<script src="${rc.getContextPath()}/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
	<script src="${rc.getContextPath()}/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.ko.js"></script>
  
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
   