	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
  <!-- Tell the browser to be responsive to screen width -->
  	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  	
  	<#if tsId?exists && tsId?has_content>
		<link rel="shortcut icon" href="${rc.getContextPath()}/soldev/img/favicon_${tsId}.ico">
	<#else>
		<link rel="shortcut icon" href="${rc.getContextPath()}/soldev/img/favicon.ico" />
	</#if>
  	
    <!-- Bootstrap CSS -->
    <!-- <link rel="stylesheet" href="bootstrap-4.3.1-dist/css/bootstrap-reboot.min.css"> -->
    <link rel="stylesheet" href="${rc.getContextPath()}/bootstrap-4.3.1-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/bootstrap-4.3.1-dist/css/bootstrap-grid.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/jBox-1.0.5/dist/jBox.all.min.css">
    
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/NotosansKR.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/layout.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/common.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/calendar.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/fontawesome-free-5.8.2-web/css/all.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/fontawesome-free-5.8.2-web/css/fontawesome.min.css">

    <!-- for dateTimepicker -->
    <link rel="stylesheet" href="${rc.getContextPath()}/tempusdominus-bootstrap4-5.0.0-alpha14/tempusdominus-bootstrap-4.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/bxslider-4-master/src/css/jquery.bxslider.css">
    
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/customHR/css/style.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/customHR/css/date.css">
  	<#if tsId?exists && tsId?has_content>
    	<link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/${tsId}/theme.css">
   	<#else>
    	<link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/isu/theme.css">
	</#if>
    <!-- <link rel="stylesheet" href="${rc.getContextPath()}/IBLeaders/layout.css"> -->
    <!-- <link rel="stylesheet" href="${rc.getContextPath()}/jQuery-3.4.1/jquery-ui.css"> -->
    
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    

  <style type="text/css">
  	/* for Vue.js */
  	[v-cloak] {display: none}
  </style>
  
<#if mainTitle?exists && mainTitle?has_content>
	<title>${mainTitle}</title>
<#else>
	<title>근태관리 시스템</title>
</#if>
  
   