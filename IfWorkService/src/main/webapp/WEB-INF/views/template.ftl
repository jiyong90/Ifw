<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
	<#include "/metaScript.ftl">
    <title>근태관리 시스템</title>
</head>
<body>
	<#include "/navTop.ftl">
    <div class="wrapper">
        <#include "/navLeft.ftl">
        <div id="content">
            <#include "/${pageName}.ftl">
            <footer class="text-center mt-auto pt-4">Copyright © 2019 ISUSYSTEM. All rights reserved</footer>
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
