<script type="text/javascript">
   	$(function() {
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"id",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"직종",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"title",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetCountPosition(4);
		sheet1.SetUnicodeByte(3);

		var url     = "${rc.getContextPath()}/noti/inbox/list";
		var allFlag = "전체";
//		$(window).smartresize(sheetResize); sheetInit();
	});

</script>
<div id="commonCodeMgr">
 	<div class="container-fluid">
		<p class="page-title"></p>
		<div class="row no-gutters">
			<div class="col-12 col-md-6 col-lg-9">
				<div class="inner-wrap">
					<div class="desc">
						<script type="text/javascript"> createIBSheet("sheet1", "100%", "57px", "kr") </script>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

