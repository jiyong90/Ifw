<!-- S : for calendar -->
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/calendar.css">
<link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/${tsId}/theme.css">
<!-- E : for calendar -->
<!-- for dateTimepicker-->
<link rel="stylesheet" href="${rc.getContextPath()}/tempusdominus-bootstrap4-5.0.0-alpha14/tempusdominus-bootstrap-4.min.css">

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
					return 'ko';
				}
			},
			custombuttons : {
				type: Object,
				required: false,
				default : function(){
					return {};
				}
			},
			header : {
				type: Object,
				required: false,
				default : function(){
					return {
				       //left: 'prev,next today',
				       left: 'prev,next',
				       center: 'title',
				       //right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
				       right: 'dayGridMonth,timeGridWeek,timeGridDay'
					};
				}
			},
			defaultview : {
				type: String,
				required: false,
				default : function(){
					return 'dayGridMonth';
				}
			},
			defaultdate : {
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
			unselectauto : {
				type: Boolean,
				required: false,
				default : function(){
					return false;
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
			},
			nowindicator: {
				type: Boolean,
				required: false,
				default : function(){
					return false;
				}
			},
			scrolltime: { //현재 시간으로 스크롤
				type: String,
				required: false,
				default : function(){
					return '06:00:00';
				}
			},
			handleWindowResize: {
				type: Boolean,
				required: false,
				default : function(){
					return true;
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
			        defaultView: this.defaultview,
			        defaultDate: this.defaultdate,
			        //views: this.views,
			        //validRange: this.validRange,
			        //navLinks: this.navLinks, 
			        selectable: this.selectable,
			        unselectAuto: this.unselectauto,
			        selectMirror: this.selectMirror,
			        editable: this.editable,
			        eventLimit: this.eventLimit, 
			        events: this.events,
			        eventSources: this.eventsources,
			        contentHeight: this.contentHeight,
			        handleWindowResize: this.handleWindowResize
				};
				
				if(this.custombuttons!='') {
					option.customButtons = this.custombuttons;
				}
				
				if(this.defaultview!='') {
					option.defaultView = this.defaultview;
				}
				
				var defalutDate = '';
				<#if workday?? && workday!='' && workday?exists >
					defalutDate = moment('${workday}').format('YYYY-MM-DD');
  		    	<#else>
					defalutDate = '${today}';
  		    	</#if>
				
				if(this.defaultdate!='') {
					option.defaultDate = defalutDate;
				}
				
				if(this.navlinks) {
					option.navLinks = this.navlinks;
				}
				
				if(this.nowindicator) {
					option.nowIndicator = this.nowindicator;
				}
				
				if(this.scrolltime) {
					option.scrollTime = this.scrolltime;
				}
				
				if(this.unselectauto) {
					option.unselectAuto = this.unselectauto;
				}
				
				return option;
			}
		},
		mounted: function() {
			var $this = this;
			document.addEventListener('DOMContentLoaded', function() {
				var calendarEl = document.getElementById('calendar');
				var calendarOptions = $this.calOptions;
				
				calendarOptions.navLinkDayClick = function(info){
					$this.navLinkDayClickCallback(info);
				};
				calendarOptions.datesRender = function(info){
					$this.datesRenderCallback(info);
				};
				calendarOptions.dayRender = function(dayRenderInfo){
					$this.dayRenderCallback(dayRenderInfo);
				};
				calendarOptions.select = function(info){
					$this.selectCallback(info);
				};
				//calendarOptions.selectOverlap = function(info){
				//	$this.selectOverlapCallback(info);
				//};
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
        	navLinkDayClickCallback: function(info){
        		this.$emit('navlinkdayclick', info);
        	},
        	datesRenderCallback: function(info){
        		this.$emit('datesrender', info);
        	},
        	dayRenderCallback: function(dayRenderInfo){
        		
        		/* var formattedDate = this.cal.formatDate(dayRenderInfo.date, {
	    			year: 'numeric',
	    			month: '2-digit',
	    			day: 'numeric'
	    		});
        		dayRenderInfo.date = formattedDate; */
        		
        		this.$emit('dayrender', dayRenderInfo);
        	},
        	selectCallback: function(info){
        		this.$emit('select', info);
        	},
        	/* selectOverlapCallback: function(info){
        		this.$emit('selectoverlap', info);
        	}, */
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
