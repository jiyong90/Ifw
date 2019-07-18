<div id="approval" class="container-fluid" v-cloak>
	<p class="page-title">결재 알림</p>
	<div class="row no-gutters notice-card" v-for="appr in apprList">
		<div class="col-12 col-md-6 col-lg-9">
			<div class="rounded-circle notice-mark type01">{{appr.applNm.substr(0,1)}}</div>
			<div class="inner-wrap">
				<div class="title">{{appr.applNm}}</div>
				<div class="desc">
					<span class="sub-title">사용기한</span> <span>{{appr.sYmd}}~{{appr.eYmd}}</span>
				</div>
			</div>
		</div>
		<div class="col-12 col-md-2 col-lg-1">
			<span class="name">{{appr.empNm}}</span>
		</div>
		<div class="col-6 col-md-2 col-lg-1 pr-1">
			<button type="button"
				class="btn btn-block btn-outline btn-approval cancel">반송</button>
		</div>
		<div class="col-6 col-md-2 col-lg-1 pl-1">
			<button type="button"
				class="btn btn-block btn-outline btn-approval sign">승인</button>
		</div>
	</div>
</div>
<script type="text/javascript">
   	var approvalVue = new Vue({
   		el: "#approval",
   		data : {
   			apprList: []
   		},
	    mounted: function(){
	    	this.getApprovalList();
	    },
	    methods : {
	    	getApprovalList: function(){
	    		var $this = this;
	    		/* var param = {
	    			status: ''	
	    		}; */
	    		
	    		Util.ajax({
					url: "${rc.getContextPath()}/flexibleAppr",
					type: "GET",
					contentType: 'application/json',
					//data: param,
					dataType: "json",
					success: function(data) {
						$this.approvalList = [];
						console.log(data);
						if(data.status=='OK' && data!=null && data.apprList!=null) {
							$this.apprList = data.apprList;
							console.log(data.apprList);
						}
					},
					error: function(e) {
						$this.approvalList = [];
					}
				});
	    	}
	    }
   	});
</script>