<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/calendar.css">

<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/locales-all.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/moment/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/moment-timezone/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/bootstrap/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/interaction/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/luxon/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/rrule/main.min.js'></script>

<script type="text/javascript">
	
	var fullCalendarComponent = {
		template : "<div id='calendar'></div>",
		data : function() {
		  	return {
		    	cal: null
		    }
		}, 
		props : {
			plugins : {
				type : Array,
				required: false,
				default : function(){
					//return ['interaction', 'dayGrid', 'timeGrid', 'list', 'bootstrap'];
					return ['interaction', 'dayGrid', 'timeGrid', 'bootstrap'];
				}
			},
			themeSystem : {
				type: String,
				required: false,
				default : function(){
					return 'bootstrap';
				}
			},
			height: {
				type: String,
				required: false,
				default : function(){
					return 'parent';
				}
			},
			locale : {
				type: String,
				required: false,
				default : function(){
					return 'en';
				}
			},
			header : {
				type: Object,
				required: false,
				default : function(){
					return {
				       left: 'prev,next today',
				       center: 'title',
				       //right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
				       right: 'dayGridMonth,timeGridWeek,timeGridDay'
					};
				}
			},
			defaultView : {
				type: String,
				required: false,
				default : function(){
					return 'dayGridMonth';
				}
			},
			defaultDate : {
				type: String,
				required: false,
				default : function(){
					return '${today}';
				}
			},
			views: {
				type: Object,
				required: false,
				default : function(){
					return {
					    dayGrid: {
					    	eventLimit: 2 //화면에 보여줄 이벤트 개수
					    	//titleFormat: { year: 'numeric', month: '2-digit', day: '2-digit' }
					    }
					};
				}
			},
			/*
			validRange: {
			    type: Object,
			    required: false,
			    default: function(){
			    	return {
			    		//start: '2019-07-01',
					    //end: '2019-08-15' //해당 기간의 날짜를 아예 안보이게 함
			    	};
			    }
			},
			*/
			navlinks : {
				type: Boolean,
				required: false,
				default : function(){
					return false; // can click day/week names to navigate views
				}
			},
			selectable : {
				type: Boolean,
				required: false,
				default : function(){
					return true;
				}
			},
			selectMirror : {
				type: Boolean,
				required: false,
				default : function(){
					return true;
				}
			},
			editable : {
				type: Boolean,
				required: false,
				default : function(){
					return true;
				}
			},
			eventLimit : {
				type: Boolean,
				required: false,
				default : function(){
					return true; // allow "more" link when too many events
				}
			},
			events : {
				type : Array,
				required: false,
				default : function(){
					return [];
				}
			},
			eventsources : {
				type : Array,
				required: false,
				default : function(){
					return [];
				}
			}
		}, 
		computed: {
			calOptions : function(){
				var $this = this;
				var option = {
					plugins: this.plugins,
			        themeSystem: this.themeSystem,
			        height: this.height,
			        locale: this.locale,
			        header: this.header,
			        defaultView: this.defaultView,
			        defaultDate: this.defaultDate,
			        //views: this.views,
			        //validRange: this.validRange,
			        //navLinks: this.navLinks, 
			        selectable: this.selectable,
			        selectMirror: this.selectMirror,
			        editable: this.editable,
			        eventLimit: this.eventLimit, 
			        events: this.events,
			        eventSources: this.eventsources
				};
				
				if(this.navlinks) {
					option.navLinks = this.navlinks;
				}
				
				return option;
			}
		},
		mounted: function() {
			var $this = this;
			document.addEventListener('DOMContentLoaded', function() {
				var calendarEl = document.getElementById('calendar');
				var calendarOptions = $this.calOptions;
				
				calendarOptions.datesRender = function(info){
					$this.datesRenderCallback(info);
				};
				//calendarOptions.dayRender = function(dayRenderInfo){
				//	$this.dayRenderCallback(dayRenderInfo);
				//};
				calendarOptions.select = function(info){
					$this.selectCallback(info);
				};
				//calendarOptions.selectAllow = function(info){
				//	$this.selectAllowCallback(info);
				//};
				calendarOptions.dateClick = function(info){
					$this.dateClickCallback(info);
				};
				calendarOptions.eventRender = function(info){
					$this.eventRenderCallback(info);
				};
				calendarOptions.eventClick = function(info){
					$this.eventClickCallback(info);
				};
				 
				$this.cal = new FullCalendar.Calendar(calendarEl,calendarOptions);
				$this.cal.render();
				
				$this.renderCallback();
			});
			
        },
        methods: {
        	renderCallback: function(){
        		this.$emit('update');
        	},
        	datesRenderCallback: function(info){
        		this.$emit('datesrender', info);
        	},
        	/* ,
        	dayRenderCallback: function(dayRenderInfo){
        		
        		var formattedDate = this.cal.formatDate(dayRenderInfo.date, {
	    			year: 'numeric',
	    			month: '2-digit',
	    			day: 'numeric'
	    		});
        		dayRenderInfo.date = formattedDate;
        		
        		this.$emit('dayrender', dayRenderInfo);
        	} */
        	selectCallback: function(info){
        		this.$emit('select', info);
        	},
        	selectAllowCallback: function(info){
        		this.$emit('selectallow', info);
        	},
        	dateClickCallback: function(info){
        		this.$emit('dateclick', info);
        	},
        	eventRenderCallback: function(info){
        		this.$emit('eventrender', info);
        	},
        	eventClickCallback: function(info){
        		this.$emit('eventclick', info);
        	}
        	
        }
	};

</script>
