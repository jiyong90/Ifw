<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/websocket.ftl">
	<#include "/metadata.ftl">
	<#include "/metaScript.ftl">
    <title>근태관리 시스템</title>
</head>
<body>
	<!-- loading image start -->
    <!-- loading image end -->
	<#include "/navTop.ftl">
    <div class="wrapper">
        <#include "/navLeft.ftl">
        <#include "/inbox.ftl">
        <div id="content">
        	<div id="loading" class="dim" style="display:none;" >
                <i class="fa fa-spinner fa-pulse fa-5x fa-fw"></i>
                <span class="sr-only">Loading...</span>
            </div>
            <!-- alert modal start -->
			<div class="modal fade" id="alertModal" tabindex="-1" role="dialog" style="z-index:1600;">
		        <div class="modal-dialog " role="document">
		            <div class="modal-content">
		                <!-- <div class="modal-header">
		    	                <h5 class="modal-title"></h5>
		    	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		    	                    <span aria-hidden="true">&times;</span>
		    	                </button>
		    	            </div> -->
		                <div class="modal-body">
		                    <p id="alertText" class="text-center"></p>
		                </div>
		                <div class="modal-body text-center">
		                    <button type="button" class="btn btn-default" data-dismiss="modal">확인</button>
		                </div>
		            </div>
		        </div>
		    </div>
		    <!-- alert modal end -->
            <#include "/${pageName}.ftl">
            <!-- <#include "/footer.ftl"> -->
        </div>
    </div>
    <script type="text/javascript">
	    $(function () {
	        $('#sidebarCollapse').on('click', function () {
	            $('#sidebar').toggleClass('active');
	        });
	
	    });
    </script>
</body>

</html>
