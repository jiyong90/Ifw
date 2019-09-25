<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
	<#include "/metaScript.ftl">
	<!-- 근태 for HR custom  -->
	<link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/layout4HR.css">
</head>
<body>
<div class="modal fade" id="alertModal" tabindex="-1" role="dialog" style="z-index:9999;">
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
	<script> 
		var loginUrl = "http://smarthrd.servicezone.co.kr/Info.do?code=905";
		var redirectText = "401redirectHrPage"; 
		var locationurl = "${rc.getContextPath()}/hr/";</script>
    <div class="wrapper">
        <div id="content">
            <#include "/${pageName}.ftl">
            <!-- <#include "/footer.ftl"> -->
        </div>
    </div>
</body>

</html>
