<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>

<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/interaction/main.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/bootstrap/main.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/locales-all.min.js'></script>
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
			/* views: {
				type: Object,
				required: false,
				default : function(){
					return {
					    dayGrid: {
					    	titleFormat: { year: 'numeric', month: '2-digit', day: '2-digit' }
					    }
					};
				}
			}, */
			navLinks : {
				type: Boolean,
				required: false,
				default : function(){
					return true; // can click day/week names to navigate views
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
			}
		}, 
		computed: {
			calOptions : function(){
				var $this = this;
				return {
					plugins: this.plugins,
			        themeSystem: this.themeSystem,
			        height: this.height,
			        locale: this.locale,
			        header: this.header,
			        defaultView: this.defaultView,
			        defaultDate: this.defaultDate,
			        //views: this.views,
			        navLinks: this.navLinks, 
			        selectable: this.selectable,
			        selectMirror: this.selectMirror,
			        editable: this.editable,
			        eventLimit: this.eventLimit, 
			        events: this.events
				};
			}
		},
		mounted: function() {
			var $this = this;
			document.addEventListener('DOMContentLoaded', function() {
				var calendarEl = document.getElementById('calendar');
				var calendarOptions = $this.calOptions;
				
				calendarOptions.datesRender = function(info){
					$this.datesRenderCallBack(info);
				}
				//calendarOptions.dayRender = function(dayRenderInfo){
				//	$this.dayRenderCallBack(dayRenderInfo);
				//}
				
				$this.cal = new FullCalendar.Calendar(calendarEl,calendarOptions);
				$this.cal.render();
				
				$this.renderCallback();
			});
			
			
        },
        methods: {
        	renderCallback: function(){
        		this.$emit('update');
        	},
        	datesRenderCallBack: function(info){
        		this.$emit('datesrender', info);
        	}/* ,
        	dayRenderCallBack: function(dayRenderInfo){
        		
        		var formattedDate = this.cal.formatDate(dayRenderInfo.date, {
	    			year: 'numeric',
	    			month: '2-digit',
	    			day: 'numeric'
	    		});
        		dayRenderInfo.date = formattedDate;
        		
        		this.$emit('dayrender', dayRenderInfo);
        	} */
        }
	};

</script>
