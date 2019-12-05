<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
    <title>근태관리 시스템</title>
</head>
<body class="">
	<div class="wrapper">
	    <div id="content" class="full-height">
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
	                                <li>근무제도관리 > 근무 시간표 관리에서 기본 출/퇴근 시간과 휴식시간을 정합니다.
	                                    <p class="sub">(기본 근무제에 대한 기본 세팅이 이미 되어 있는 경우, 설정값을 확인합니다)</p>
	                                </li>
	                                <li>근무제도관리 > 근무제도관리 에서 기본 근무를 새로 만들거나, 기존의 기본 근무제를 편집합니다.
	                                    <p class="sub">(기본 근무제는 근무제도 유형이 ‘기본근무'인 근무제를 말합니다)</p>
	                                </li>
	                                <li>근무제도관리 > 기본근무시간 관리 에서 사업장을 추가하고 2 단계에서 만든 기본 근무제를 할당합니다.</li>
	                            </ol>
	                        </div>
	                    </li>
	                    <li>
	                        <div class="link"><span class="num">02</span>개인별 근무 시간 관리<i class="fas fa-chevron-down"></i></div>
	                        <div class="submenu">
	                            <ol class="help-desc-wrap">
	                                <li>근무제도관리 > 근무 시간표 관리에서 기본 출/퇴근 시간과 휴식시간을 정합니다.
	                                    <p class="sub">(기본 근무제에 대한 기본 세팅이 이미 되어 있는 경우, 설정값을 확인합니다)</p>
	                                </li>
	                                <li>근무제도관리 > 근무제도관리 에서 기본 근무를 새로 만들거나, 기존의 기본 근무제를 편집합니다.
	                                    <p class="sub">(기본 근무제는 근무제도 유형이 ‘기본근무'인 근무제를 말합니다)</p>
	                                </li>
	                                <li>근무제도관리 > 기본근무시간 관리 에서 사업장을 추가하고 2 단계에서 만든 기본 근무제를 할당합니다.</li>
	                            </ol>
	                        </div>
	                    </li>
	                    <li>
	                        <div class="link"><span class="num">03</span>기본 운영규칙 설정하기<i class="fas fa-chevron-down"></i></div>
	                        <div class="submenu">
	                            <ol class="help-desc-wrap">
	                                <li>근무제도관리 > 근무 시간표 관리에서 기본 출/퇴근 시간과 휴식시간을 정합니다.
	                                    <p class="sub">(기본 근무제에 대한 기본 세팅이 이미 되어 있는 경우, 설정값을 확인합니다)</p>
	                                </li>
	                                <li>근무제도관리 > 근무제도관리 에서 기본 근무를 새로 만들거나, 기존의 기본 근무제를 편집합니다.
	                                    <p class="sub">(기본 근무제는 근무제도 유형이 ‘기본근무'인 근무제를 말합니다)</p>
	                                </li>
	                                <li>근무제도관리 > 기본근무시간 관리 에서 사업장을 추가하고 2 단계에서 만든 기본 근무제를 할당합니다.</li>
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
	