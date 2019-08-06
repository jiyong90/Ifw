<div id="timeCalendar" class="calendar-wrap" v-cloak>
    <div id='calendar-container'>
		<full-calendar ref="fullCalendar" :header="header" :defaultview="view" :defaultdate="workday" @update="renderCallback" @datesrender="datesRenderCallback" @select="selectCallback" @eventrender="eventRenderCallback"></full-calendar>
    </div>
</div>
<script type="text/javascript">
   	var timeCalendarVue = new Vue({
   		el: "#timeCalendar",
  			components : {
  				FullCalendar : fullCalendarComponent
  		    },
  		    data : {
  		    	header: {
  		    		left: 'prev,next',
			        center: 'title',
			        right: ''
  		    	},
  		    	view: 'timeGridDay',
  		    	workday: '',
  		    	workTermTime: {}, //선택한 기간의 근무제 정보
  		    	workDayResult: []
  		    },
  		    mounted: function(){
  		    	<#if workday?? && workday!='' && workday?exists >
  		    		this.workday = moment('${workday}').format('YYYY-MM-DD');
  		    	<#else>
  		    		this.workday = '${today}';
  		    	</#if>
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    		
  		    	},
  		    	datesRenderCallback: function(info){
  		    		var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;

  		    		if(info.view.type == 'timeGridDay') { //month change
  		    			var ymd = moment(calendar.getDate()).format('YYYYMMDD');
	   		    		var param = {
		   		    		ymd : ymd
		   		    	};
	   		    		
	   		    		Util.ajax({
							url: "${rc.getContextPath()}/flexibleEmp/dayResults",
							type: "GET",
							contentType: 'application/json',
							data: param,
							dataType: "json",
							success: function(data) {
								$this.getWorkDayResult(ymd, data);
							},
							error: function() {
								
							}
						});
	   		    		
	  		    	}
  		    		
  		    	},
  		    	selectCallback : function(info){ //day select
					
  		    		
  		    	},
  		    	eventRenderCallback : function(info){
  		    		
  		    		
  		    	},
  	         	addEvent : function(Obj){
  	         		if(Obj!=null) {
  	         			var calendar = this.$refs.fullCalendar.cal;
  	         			
  	         			var event = calendar.getEventById(Obj.id);
  	         			
	         			if(event!=null) {
	         				event.remove();
	         			}
	         			//이벤트 새로 생성
         				calendar.batchRendering(function() {
  	  	         			calendar.addEvent(Obj);
  	  	         		});
  	         		}
  	         	},
  	         	getWorkDayResult: function(ymd, data){
  	         		var $this = this;
  	         		
  	         		if(data!=null) {
  	         			//출퇴근 타각 표시
  	         			if(data.hasOwnProperty('entry')) {
  	         				var entry = data.entry;
  	         				
  	         				var sEdate = new Date(entry.entrySdate);
  	         				sEdate.setMinutes(sEdate.getMinutes()+3);
  	         				
  	         				var eEdate = new Date(entry.entryEdate);
  	         				eEdate.setMinutes(eEdate.getMinutes()+3);
  	         				
  	         				var sEntry = {
 	         					id: 'entrySdate',
								start: entry.entrySdate,
	  		  		        	end: moment(sEdate).format('YYYY-MM-DDTHH:mm:ss'),
	  		  		        	editable: false,
	  		  		        	color: '#0b1baa'
  	         				};
  	         				$this.addEvent(sEntry);
  	         				
  	         				var eEntry = {
 	         					id: 'entryEdate',
								start: entry.entryEdate,
	  		  		        	end: moment(eEdate).format('YYYY-MM-DDTHH:mm:ss'),
	  		  		        	editable: false,
	  		  		        	color: '#ff448a'
  	         				};
  	         				$this.addEvent(eEntry); 
  	         			}
  	         			
  	         			if(data.hasOwnProperty('workResult')) {
  	         				console.log(data.workResult);
  	         				data.workResult.map(function(w){
  	         					var result = {
  	   	         					id: w.timeTypeCd,
  	   	         					title: w.timeTypeNm,
  	  								start: w.sDate,
  	  	  		  		        	end: w.eDate
  	    	         			};
  	         					
  	    	         			$this.addEvent(result); 
  	         				});
  	         				
  	         			}
  	         		}
  	         	}
  		    }
   	});

   	
</script>

