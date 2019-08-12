<aside id="alertSidebar" class="control-sidebar" style="display:none;">
    <ul class="alert-list-wrap">
        <li>
            <span class="status SELE_F"></span>
            <div class="desc">선근제 신청이 완료되었습니다.<br>
                출,퇴근시간을 지정해주세요.
                
            </div>
            <div class="btn-wrap">
                <button type="button" class="btn btn-sm btn-inline btn-outline-secondary">작성하기</button>
            </div>
            <button class="btn-close">&#215;</button>
        </li>
        <li>
            <span class="status ELAS"></span>
            <div class="desc">탄근제 신청이 완료되었습니다.</div>
            <button class="btn-close">&#215;</button>
        </li>
        <li>
            <span class="status SELE_C"></span>
            <div class="desc">
                김이수님이 선근제를 신청했습니다.
                <div class="date">2019.08.09 10:24</div>
                
            </div>
            <div class="btn-wrap">
                <button type="button" class="btn btn-sm btn-inline btn-outline-secondary">평가하기</button>
            </div>
            <button class="btn-close">&#215;</button>
        </li>
    </ul>
</aside>

<script type="text/javascript">
$(function(){
	$('#alertLink').on('click', function () {
	    $('#alertSidebar').toggle('active');
	});
});
</script>