<div id="approval" v-cloak>
	<!-- 결재의견 modal start -->
	<div class="modal fade" id="apprOpinionModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">결재 의견</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group col-12">
                        <textarea class="form-control" id="apprOpinion" rows="3" placeholder="결재/반려 의견 작성" v-model="apprOpinion"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="button" id="apprBtn" class="btn btn-default">결재하기</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 결재의견 modal end -->
	<div class="container-fluid">
		<p class="page-title">결재 알림</p>
		<div class="row no-gutters notice-card" v-for="appr in apprList">
			<div class="col-12 col-md-6 col-lg-9">
				<div :class="['rounded-circle notice-mark '] + appr.applCd">{{appr.applNm.substr(0,1)}}</div>
				<div class="inner-wrap">
					<div class="title">{{appr.applNm}}</div>
					<div class="desc">
						<span class="sub-title">사용기한</span> 
						<span>{{moment(appr.sYmd).format('YYYY.MM.DD')}}~{{moment(appr.eYmd).format('YYYY.MM.DD')}}</span>
						<span class="sub-desc" v-if="appr.reasonNm">{{appr.reasonNm}}</span>
					</div>
				</div>
			</div>
			<div class="col-12 col-md-2 col-lg-1">
				<span class="name">{{appr.empNm}}</span>
			</div>
			<div class="col-6 col-md-2 col-lg-1 pr-1">
				<button type="button"
					class="btn btn-block btn-outline btn-approval cancel" @click="approval(appr,'reject')">반송</button>
			</div>
			<div class="col-6 col-md-2 col-lg-1 pl-1">
				<button type="button"
					class="btn btn-block btn-outline btn-approval sign" @click="approval(appr,'apply')">승인</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
   	var approvalVue = new Vue({
   		el: "#approval",
   		data : {
   			apprList: [],
   			apprOpinion: ''
   		},
	    mounted: function(){
	    	this.getApprovalList();
	    },
	    methods : {
	    	getApprovalList: function(){
	    		var $this = this;
	    		
	    		Util.ajax({
					url: "${rc.getContextPath()}/appl",
					type: "GET",
					contentType: 'application/json',
					//data: param,
					dataType: "json",
					success: function(data) {
						$this.apprList = [];
						console.log(data);
						if(data.status=='OK' && data!=null && data.apprList!=null) {
							$this.apprList = data.apprList;
						}
					},
					error: function(e) {
						$this.apprList = [];
					}
				});
	    	},
	    	approval: function(appr, apprStatus){ //결재
	    		var $this = this;
	    	
	    		$("#apprBtn").bind('click',function(){
	    			$("#apprBtn").unbind('click');
	    			
	    			var saveYn = true;
	    			if($this.apprOpinion=='') {
	    				saveYn = false;
	    				$("#alertText").html("의견을 입력해 주세요.");
	    			}
	    			
	    			if(saveYn) {
	    				//$('#apprOpinionModal').modal("hide"); 
	    				$('#apprOpinionModal .close').click();
	    				
		    			var param = {
	    	    			applId: appr.applId,
	    	    			applCd: appr.applCd,
	    	    			apprSeq: appr.apprSeq,
	    	    			apprOpinion: $this.apprOpinion 
	    	    		};
	    	    		
	    	    		Util.ajax({
	    					url: "${rc.getContextPath()}/appl/"+apprStatus,
	    					type: "POST",
	    					contentType: 'application/json',
	    					data: JSON.stringify(param),
	    					dataType: "json",
	    					success: function(data) {
	    						if(data!=null && data.status=='OK') {
									$("#alertText").html("결재되었습니다.");
								} else {
									$("#alertText").html(data.message);
								}
								$("#alertModal").on('hidden.bs.modal',function(){
									$this.getApprovalList();
								});
		  	  	         		$("#alertModal").modal("show"); 
	    					},
	    					error: function(e) {
	    						console.log(e);
	    						$("#alertText").html("확인요청 시 오류가 발생했습니다.");
	      	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
	      	  	         		$("#alertModal").modal("show"); 
	    					}
	    				});
	    			} else {
	    				$("#alertModal").on('hidden.bs.modal',function(){
	    					$("#apprOpinion").focus();
	    				});
  	  	         		$("#alertModal").modal("show"); 
	    			}
	    		});
	    		$('#apprOpinionModal').modal("show"); 
	    	}
	    }
   	});
   	
   	$('[data-dismiss=modal]').on('click', function (e) {
		var $t = $(this),
	        target = $t[0].href || $t.data("target") || $t.parents('.modal') || [];

		$(target).find("textarea").val('').end();
	})
</script>