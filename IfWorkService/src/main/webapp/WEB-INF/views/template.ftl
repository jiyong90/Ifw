<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
	<#include "/metaScript.ftl">
    <title>근태관리 시스템</title>
</head>
<body>
	<div class="modal fade" id="alertModal" tabindex="-1" role="dialog">
        <div class="modal-dialog " role="document">
            <div class="modal-content">
                <!-- alert 메세지일 경우, header 생략 -->
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
	<#include "/navTop.ftl">
    <div class="wrapper">
        <#include "/navLeft.ftl">
        <div id="content">
            <#include "/${pageName}.ftl">
            <#include "/footer.ftl">
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
