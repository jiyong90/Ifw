<#include "/websocket.ftl">

<nav class="navbar navbar-expand-lg navbar-light" id="inbox">
    <!-- collapse button -->
    <!-- <button type="button" id="sidebarCollapse" class="btn btn-info">
        <i class="fas fa-bars"></i>
    </button> -->

    <h1 class="logo">
    <a href="${rc.getContextPath()}/console/${tsId}/">
        <img src="${rc.getContextPath()}/soldev/img/gnb_logo.png" alt="로고">
        </a>
    </h1>
    <div class="pr-3 d-lg-none">
        <button class="btn btn-dark d-inline-block d-lg-none ml-auto" type="button" data-toggle="collapse"
            data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
            aria-label="Toggle navigation">
            <i class="fas fa-align-justify"></i>
        </button>
    </div>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="nav navbar-nav ml-auto nav-top gnb-nav-wrap">
            <li class="nav-item">
                    <a class="" href="#"><span class="ico-wrap"><i class="sp_ico calendar">&#xe900;</i></span></a>
            </li>
            <li class="nav-item dropdown notifications-menu">
                    <a data-toggle="dropdown" href="#">
                    	<span class="ico-wrap"><i class="sp_ico alarm">&#xe802;</i><span :class="{'new':inboxCount > 0}"  v-cloak></span></span>
                    </a>
                    <ul class="dropdown-menu">
                    	<li class="header">You have {{inboxCount}} notifications</li>
                    	<li>
			                <!-- inner menu: contains the actual data -->
			                <ul class="menu">
			                  	  <li v-for="m in data">
								    {{ m.title }}
								  </li>
			                  </li>
		              	  	</ul>
		              	  </li>
                    </ul>
            </li>
            <li class="nav-item">
                    <a class="" href="${rc.getContextPath()}/logout/${tsId}"><span class="ico-wrap"><i class="sp_ico power">&#xe801;</i></span></a>
            </li>
            <!-- <li class="nav-item">
                <a class="nav-link" href="#"><i class="fas fa-power-off"></i></a>
            </li> -->
        </ul>
    </div>
</nav>
<script type="text/javascript">

$(document).ready(function(){
	var inboxCountVue = new Vue({
		el : '#inbox',
		data : {
			inboxCount : '',
			data : []
		},
		mounted : function() {
			var $this = this;
			Util.ajax({
				url: "${rc.getContextPath()}/noti/inbox/list",
				type: "GET",
				contentType : 'application/json',
				dataType : "json",
				success: function(data) {
					console.log(data);
					$this.inboxCount = data.length;
					$this.data = data;
				}, error: function(data) { alert(data.message); }
			})
		},
		methods : {
			webSocketCallback : function(paramMap){
				var $this = this;
				if(paramMap.body){
					var data = JSON.parse(paramMap.body);
					console.log(paramMap);
					console.log(data);
					$this.inboxCount = 1;
					$this.title = data.title;
					alert("알림도착 : " + $this.title);
				}
			}
		}
	});

	connect("/api/${tenantId}/${enterCd}/${empNo}/noti", inboxCountVue.webSocketCallback);
	
});
</script>