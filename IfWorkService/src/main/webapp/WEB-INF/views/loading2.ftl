<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
</head>
<body>
로딩중...1
	<script>

		var cookies = document.cookie.split(";");
		
		for (var i = 0; i < cookies.length; i++) {
		    var cookie = cookies[i];
		    var eqPos = cookie.indexOf("=");
		    var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
		    document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
		}
		
		window.location.reload(true);
		location.href = "${rc.getContextPath()}/console/${tsId}";
    </script>

</body>

</html>
