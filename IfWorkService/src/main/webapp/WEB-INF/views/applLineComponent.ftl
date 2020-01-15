<script type="text/javascript">
	var applLine = {
		template : 	"<div class=\"inner-wrap\" v-if=\"bindData\">\
		    <div class=\"title mb-2\">결재선</div>\
		    <ul class=\"info-box approval\">\
		        <div class=\"item\" v-if=\"isApprType('1')\">\
		            <p class=\"title\">신청자</p>\
		            <div class=\"level\" v-for=\"l in bindData\" v-if=\"l.apprTypeCd=='1'\">\
		                <span class=\"name\">{{l.empNm}}</span>\
		                <span v-if=\"l.apprStatusCd\" class=\"status\" :class=\"l.apprStatusCd=='10'?'ing':l.apprStatusCd=='20'?'complete':'cancel'\">{{l.apprStatusNm}}</span>\
		            </div>\
		        </div>\
		        <div class=\"item\" v-if=\"isApprType('2')\">\
		            <p class=\"title\">발신</p>\
		            <div class=\"level\" v-for=\"l in bindData\" v-if=\"l.apprTypeCd=='2'\">\
		                <span class=\"name\">{{l.empNm}}</span>\
		                <span v-if=\"l.apprStatusCd\" class=\"status\" :class=\"l.apprStatusCd=='10'?'ing':l.apprStatusCd=='20'?'complete':'cancel'\">{{l.apprStatusNm}}</span>\
		            </div>\
		        </div>\
		        <div class=\"item\" v-if=\"isApprType('3')\">\
		            <p class=\"title\">수신</p>\
		            <div class=\"level\"  v-for=\"l in bindData\" v-if=\"l.apprTypeCd=='3'\">\
		                <span class=\"name\">{{l.empNm}}</span>\
		                <span v-if=\"l.apprStatusCd\" class=\"status\" :class=\"l.apprStatusCd=='10'?'ing':l.apprStatusCd=='20'?'complete':'cancel'\">{{l.apprStatusNm}}</span>\
		            </div>\
		        </div>\
		    </ul>\
		</div>\
		",
		data : function(){
			return {};
		},
		props : {
			bindData : {
				type: Object,
				required: false,
				default : function(){
					return [];
				}
			}
		},
		computed: {
   			isApprType: function() {
	    		var $this = this;
	    		return function(apprType){
	    			var isExist = false;
	    			if($this.bindData!=null && $this.bindData!=undefined && $this.bindData.length>0) {
	    				$this.bindData.map(function(l){
		    				if(l.apprTypeCd == apprType) {
		    					isExist = true;
		    				}
		    			});
	    			}
	    			
	    			return isExist; 
	    		};
	    	}
   		}
	}

</script>