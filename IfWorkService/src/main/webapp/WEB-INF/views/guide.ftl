<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
    <title>근태관리 시스템</title>
</head>
<body class="">
	<div class="wrapper">
	    <div id="content" class="full-height bg-light-blue">
	        <div class="container-fluid pb-4 pop-up">
	            <!-- 제목 -->
	            <p class="page-title">관리자 도움말</p>
	            <!-- 내용 -->
	            <div class="contents-wrap inner-wrap">
	                <ul id="accordion" class="accordion">
	                    <li>
	                        <div class="link"><span class="num">01</span>기본 운영규칙 설정하기<i class="fas fa-chevron-down"></i></div>
	                        <div class="submenu">
	                            <ol class="help-desc-wrap">
	                                <li>유연근무관리 > 근무제도관리 > 근무시간표 관리에서 기본 출/퇴근 시간과 휴식시간을 정합니다.
	                                    <p class="sub">(기본 근무제에 대한 기본 세팅이 이미 되어 있는 경우, 설정값을 확인합니다)</p>
	                                    <p class="sub">* 기본적으로 평일/휴일에 대한 근무 유형이 필요합니다.</p>
	                                    <p class="sub">&nbsp;(휴일여부로 평일과 휴일을 구분합니다.)</p>
	                                    <p class="sub">* 휴게시간 차감 기준은 휴식시간범위 / 시간이 있습니다.</p>
	                                    <p class="sub">* 공휴시근무코드 : 평일에 명절과 같은 휴일일 경우 휴일의 휴게시간을 적용할 수 있습니다.</p>
	                                    <p class="sub">* 기본근무시간 : 9 to 6 의 기본근무 시간을 설정합니다.</p>
	                                    <p class="sub">* 지각체크여부 : 계획시간보다 출근타각시간이 늦었을 경우 지각 데이터를 생성합니다.</p>
	                                    <p class="sub">* 조퇴체크여부 : 퇴근계획시간보다 퇴근근타각시간이 빠를 경우 조퇴 데이터 생성합니다.</p>
	                                    <p class="sub">* 결근체크여부 : 계획이 있고 출/퇴근 타각 정보가 없을 경우 결근데이터 생성합니다.</p>
	                                </li>
	                                <li>유연근무관리 > 근무제도관리 > 근무제도관리 에서 기본 근무 및 근무제도를 만들 수 있습니다.
	                                    <p class="sub">(적용중인 근무제는 편집할 수 없습니다.)</p>
	                                    <p class="sub">* 반드시 하나의 기본근무제도는 필요합니다.</p>
	                                    <p class="sub">***** 근무제기준</p>
	                                    <p class="sub">* 탄력근무/완전선택근무/부분선택근무/시차출퇴근/자율출퇴근/근무조의 설정정보를 입력합니다.</p>
	                                    <p class="sub">* 공휴일제외여부 : 근무시간 생성시 법정공휴일 및 회사 공휴일을 휴일로 생성합니다.</p>
										<p class="sub">* 인정근무 단위시간(분) : 출근시간이 8:53일 경우 09:00 로 판단한다. 퇴근시간이 18:05일 경우 18:00로 판단합니다.</p>
										<p class="sub">* 출근자동처리기준 : 사용일 경우 [IFW-A01]의 근무유형의 기본근무시작시간 기준으로 출근이 인정됩니다.</p>
										<p class="sub">* 퇴근자동처리기준 : 사용일 경우 [IFW-A01]의 근무유형의 기본근무종료시간 기준으로 퇴근이 인정됩니다.</p>
										<p class="sub">* 고정OT소진 사용여부 : 고정OT사용여부 </p>
										<p class="sub">* 고정OT소진 방법 : 일별일 경우 [고정OT소진 한계시간(분)] 의 값이 60일 경우 근무일 기준으로 매일 60분씩 자동생성
										일괄일 경우 소정근로를 모두 사용한 후 부터 [고정OT소진 한계시간(분)] 의 값이 1200일 경우 고정OT 20시간이 일한 만큼 생성됩니다.</p>
										<p class="sub">* 간주근무시간 : 출장이나 교육등 출/퇴근의 시간정보가 있는 데이터일 경우 선택된 근무유형에 따라서 휴게시간을 계산하여 인정시간을 계산합니다.</p>
										<p class="sub">***** 근무제패턴</p>
										<p class="sub">* 순서1에 해당하는 요일은 O요일입니다. 는 근무제 시작일의 요일입니다. </p>
										<p class="sub">* 순서는 근무제 시작일로 부터 순차적으로 근무유형을 만들기 위함입니다. 즉 일주일 단위의 패턴을 만들려면 1~7까지 정보를 등록합니다. </p>
	                                </li>
	                            </ol>
	                        </div>
	                    </li>
	                </ul>
	            </div>
	        </div>
	    </div>
	</div>
	<#include "/metaScript.ftl">
	<script type="text/javascript">
	    $(function () {
	        //accordion
	        var Accordion = function (el, multiple) {
	            this.el = el || {};
	            this.multiple = multiple || false;
	
	            // Variables privadas
	            var links = this.el.find('.link');
	            // Evento
	            links.on('click', { el: this.el, multiple: this.multiple }, this.dropdown)
	        }
	
	        Accordion.prototype.dropdown = function (e) {
	            var $el = e.data.el;
	            $this = $(this),
	            $next = $this.next();
	
	            $next.slideToggle();
	            $this.parent().toggleClass('open');
	
	            /* if (!e.data.multiple) {
	                $el.find('.submenu').not($next).slideUp().parent().removeClass('open');
	            }; */
	        }
	
	        var accordion = new Accordion($('#accordion'), false);
	    });
	</script>
	</body>
</html>