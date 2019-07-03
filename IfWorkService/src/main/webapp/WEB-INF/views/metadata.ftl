	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
  <!-- Tell the browser to be responsive to screen width -->
  	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  	
  	<link rel="shortcut icon" href="soldev/img/favicon.ico" />
    <!-- Bootstrap CSS -->
    <!-- <link rel="stylesheet" href="bootstrap-4.3.1-dist/css/bootstrap-reboot.min.css"> -->
    <link rel="stylesheet" href="${rc.getContextPath()}/bootstrap-4.3.1-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/bootstrap-4.3.1-dist/css/bootstrap-grid.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/NotosansKR.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/layout.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/common.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/fontawesome-free-5.8.2-web/css/all.min.css">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="${rc.getContextPath()}/jQuery-3.4.1/jquery-3.4.1.min.js"></script>
	<script src="${rc.getContextPath()}/popper-1.15.0/popper.min.js"></script>
	<script src="${rc.getContextPath()}/bootstrap-4.3.1-dist/js/bootstrap.min.js"></script>
	<script src="${rc.getContextPath()}/moment/moment.js"></script>
	<script src="${rc.getContextPath()}/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
	<script src="${rc.getContextPath()}/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.ko.js"></script>
  
  <style type="text/css">
  	/* for Vue.js */
  	[v-cloak] {display: none}
  </style>
  
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
   