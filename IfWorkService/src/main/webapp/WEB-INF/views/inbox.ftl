<div id="inbox" class="dropdown-menu" v-cloak>
	<div v-if="inboxCount>0">
	    <div class="msg-desc" v-if="planYn">
	    	<p>근무계획을 작성해 주세요.</p>
	    	<div class="btn-wrap">
		    	<button class="btn btn-default btn-flat btn-sm" @click="location.href='${rc.getContextPath()}/console/${tsId}/views/workCalendar?calendarType=Day';">작성하기</button>
	    	</div>
	    </div>
	    <ul class="msg-list">
	        <li v-for="i in inboxList">{{ i.title }}</li>
	    </ul>
    </div>
    <div v-else>
    	<div class="msg-desc">
    		<p>알림이 없습니다.</p>
    	</div>
    </div>
</div>

<script type="text/javascript">
	var inboxVue = new Vue({
		el : '#inbox',
		data : {
			inboxCount: 0,
			inboxList : [],
			planYn: false
		},
		mounted : function() {
			this.getInboxList();
		},
		methods : {
			getInboxList: function(){
				var $this = this;
				Util.ajax({
					url: "${rc.getContextPath()}/noti/inbox/list",
					type: "GET",
					contentType : 'application/json',
					dataType : "json",
					success: function(response) {
						if(response!=null && response.status=='OK') {
							$this.inboxList = response.indboxList;
						}
					}, 
					error: function(e) { 
						console.log(e);
					}
				});
			}/* ,
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
			} */
		}
	});
	
	//connect("/api/${tenantId}/${enterCd}/${empNo}/noti", inboxVue.webSocketCallback);
</script>

