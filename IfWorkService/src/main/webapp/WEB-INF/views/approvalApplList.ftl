<#include "/applLineComponent.ftl">
<div id="approvalApplList">
	<div id="approval" v-cloak class="bg-light-blue">
		<!-- 유연근무제신청 상세보기 modal start -->
		<div class="modal fade show" id="flexibleAppl" tabindex="-1" role="dialog" v-if="appl">
			<div class="modal-dialog modal-md" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<h5 class="modal-title">유연근무제신청</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form>
							<div class="modal-app-wrap">
								<div class="inner-wrap">
									<div class="title">유연근무제 신청일자</div>
									<div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.sYmd">
                                        	{{moment(appl.sYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.eYmd">
                                        	{{moment(appl.eYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                    </span>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">근무기간</div>
									<div class="desc">
										<template v-if="appl.workDay">
											{{appl.workDay}}
										</template>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">사유</div>
									<div class="desc">
										<template v-if="appl.reason">
											{{appl.reason}}
										</template>
									</div>
								</div>
								<div class="accordion-wrap inner-wrap">
									<ul id="accordion" class="accordion" v-if="appl.hasOwnProperty('elasDetails')">
										<li v-for="e in appl.elasDetails">
											<div class="link" @click="accordionDropdown($event.target)">{{moment(e.startYmd).format('YYYY-MM-DD')}} ~ {{moment(e.endYmd).format('YYYY-MM-DD')}}<i class="ico arrow-down"></i></div>
											<div class="submenu">
												<ul class="all-time-wrap">
													<li>
														<span class="title">근무시간</span>
														<span class="time bold">{{minuteToHHMM(e.workMinute, 'detail')}}</span>
													</li>
													<li>
														<div class="total">
															<span class="title">연장합산</span>
															<span class="time bold">{{minuteToHHMM(e.otMinute, 'detail')}}</span>
														</div>
														<ul class="time-list">
															<li>
																<span class="title">조출시간</span>
																<span class="time">{{minuteToHHMM(e.otbMinute, 'detail')}}</span>
															</li>
															<li>
																<span class="title">잔업시간</span>
																<span class="time">{{minuteToHHMM(e.otaMinute, 'detail')}}</span>
															</li>
															<li>
																<span class="title">휴일시간</span>
																<span class="time">{{minuteToHHMM(e.holidayMinute, 'detail')}}</span>
															</li>
														</ul>
													</li>
												</ul>
												<template v-if="e.hasOwnProperty('details')">
													<p class="title time-desc-title">출,퇴근시간</p>
													<ul class="time-desc-wrap">
														<li v-for="d in e.details">
															<div class="date">{{moment(d.ymd).format('YYYY-MM-DD')}}({{d.weekday}})
																<template v-if="d.planSdate && d.planEdate">{{moment(d.ymd+' '+d.planSdate).format('HH:mm')}}~{{moment(d.ymd+' '+d.planEdate).format('HH:mm')}}</template>
															</div>
															<ul class="time-desc">
																<li><span class="title">근무시간</span><span class="time">{{minuteToHHMM(d.workMinute, 'detail')}}</span></li>
																<li><span class="title">조출시간</span><span class="time">{{minuteToHHMM(d.otbMinute, 'detail')}}</span></li>
																<li><span class="title">잔업시간</span><span class="time">{{minuteToHHMM(d.otaMinute, 'detail')}}</span></li>
																<li><span class="title">휴일시간</span><span class="time">{{minuteToHHMM(d.holidayMinute, 'detail')}}</span></li>
															</ul>
														</li>
													</ul>
												</template>
											</div>
										</li>
									</ul>
								</div>
								<appl-line :bind-data="appl.applLine"></appl-line>
							</div>
							<div class="text-center" v-if="applType=='02'">
								<button type="button" class="btn btn-approval cancel" @click="approval('reject')">반송</button>
								<button type="button" class="btn btn-approval sign" @click="approval('apply')">승인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 유연근무제신청 상세보기 modal end -->
		<!-- 연장근무신청 상세보기 modal start -->
		<div class="modal fade show" id="otAppl" tabindex="-1" role="dialog"  v-if="appl">
			<div class="modal-dialog modal-md" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<template v-if="appl.applCd=='OT_CAN'">
							<h5 class="modal-title">연장근로 취소신청</h5>
						</template>
						<template v-else="">
							<h5 class="modal-title" v-if="appl.holidayYn!='Y'">연장근로신청</h5>
							<h5 class="modal-title" v-else>휴일근로신청</h5>
						</template>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form>
							<div class="modal-app-wrap">
								<div class="inner-wrap">
									<template v-if="appl.targetList && appl.targetList!=null && (Object.keys(appl.targetList).length>1 || (Object.keys(appl.targetList).length==1 && !appl.targetList.hasOwnProperty(appl.applSabun)))">
										<p class="page-sub-title mb-1" v-if="appl.holidayYn!='Y'">연장근로 대상자</p>
										<p class="page-sub-title mb-1" v-else>휴일근로 대상자</p>
										<div class="select-list-wrap position-relative">
											<div class="loading-spinner" style="display:none;"></div>
											<span class="targetor" v-for="(v, k) in appl.targetList">
						   					<span class="name">{{v.empNm}}</span>
						   					<span class="time">잔여 {{minuteToHHMM(v.restOtMinute, 'short')}}</span>
						                </span>
										</div>
										<hr class="separate-bar">
									</template>
									<template  v-if="appl.targetList && appl.targetList!=null && (Object.keys(appl.targetList).length==1 && appl.targetList.hasOwnProperty(appl.applSabun))">
										<p class="page-sub-title mb-0">잔여 연장근로시간</p>
										<span class="time-wrap"  v-for="(v, k) in appl.targetList">
				                        <i class="fas fa-clock"></i><span class="time point">{{minuteToHHMM(v.restOtMinute, 'detail')}}</span>
				                    </span>
										<hr class="separate-bar">
									</template>
									<div class="title" v-if="appl.holidayYn!='Y'">연장근로시간</div>
									<div class="title" v-else>휴일근로시간</div>
									<div class="desc">
                                    <span class="time-wrap">
                                        <i class="fas fa-clock"></i>
                                        <span class="time">
                                        	<template v-if="appl.otMinute">
                                        		{{minuteToHHMM(appl.otMinute, 'detail')}}
                                        	</template>
                                        </span>
                                    </span>
										<span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.otSdate">
                                        	{{moment(appl.otSdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="start-time">
                                        	<template v-if="appl.otSdate">
                                        	{{moment(appl.otSdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.otEdate">
                                        	{{moment(appl.otEdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="end-time">
                                        	<template v-if="appl.otEdate">
                                        	{{moment(appl.otEdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">사유구분</div>
									<div class="desc">
										<template v-if="appl.reasonNm">
											{{appl.reasonNm}}
										</template>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">설명</div>
									<div class="desc">
										<template v-if="appl.reason">
											{{appl.reason}}
										</template>
									</div>
								</div>
								<div class="inner-wrap" v-show="appl.holidayYn=='Y'">
									<div class="title">휴일대체방법</div>
									<div class="desc">
										<template v-if="appl.subYn">
											{{appl.subYn=='Y'?'휴일대체':'수당지급'}}
										</template>
									</div>
								</div>
								<div class="inner-wrap" v-show="appl.holidayYn=='Y' && appl.subYn=='Y'">
									<div class="title">대체일시</div>
									<template v-if="appl.subs" v-for="sub in appl.subs">
										<div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(sub.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(sub.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{minuteToHHMM(sub.subsMinute,'detail')}}</span>
                                    </span>
										</div>
										<div class="sub-desc" v-if="(sub.workSDate && sub.workEDate) || appl.oldSubs">
											<template v-if="sub.workSDate && sub.workEDate">
												*해당일 근무시간은 {{moment(sub.workSDate).format('HH:mm')}}~{{moment(sub.workEDate).format('HH:mm')}} 입니다.
											</template>
											<template v-if="appl.oldSubs">
												<template v-for="oldSub in appl.oldSubs" v-if="oldSub.otSubsApplId == sub.oldSubsApplId">
													<br>*이전 대체휴일은  {{moment(oldSub.subsSdate).format('YYYY-MM-DD HH:mm')}}~{{moment(oldSub.subsEdate).format('YYYY-MM-DD HH:mm')}} 입니다.
												</template>
											</template>
										</div>
									</template>
								</div>
								<div class="inner-wrap" v-if="appl.applCd=='OT_CAN' && appl.cancelReason">
									<div class="title">취소사유</div>
									<div class="desc">
										{{appl.cancelReason}}
									</div>
								</div>
								<appl-line :bind-data="appl.applLine"></appl-line>
								<hr class="bar">
							</div>
							<div class="btn-wrap text-center" v-if="applType=='01' && appl.applSabun == appl.sabun">
								<!-- <template v-if="(appl.cancelYn==null||appl.cancelYn==undefined||appl.cancelYn!='Y') && appl.applStatusCd=='99'">
                                    <button type="button" class="btn btn-default rounded-0" v-if="appl.holidayYn!='Y'" data-toggle="modal" data-target="#cancelOpinionModal">연장근로신청 취소하기</button>
                                    <button type="button" class="btn btn-default rounded-0" v-else data-toggle="modal" data-target="#cancelOpinionModal">휴일근로신청 취소하기</button>
                                </template>
                                <template v-else> -->
								<button type="button" class="btn btn-default rounded-0" v-if="appl.recoveryYn" data-toggle="modal" data-target="#confirmModal">회수하기</button>
								<!-- </template> -->
							</div>
							<div class="text-center" v-if="applType=='02'">
								<button type="button" class="btn btn-approval cancel" @click="approval('reject')">반송</button>
								<button type="button" class="btn btn-approval sign" @click="approval('apply')">승인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 연장근무신청 상세보기 modal end -->
		<!-- 연장근무취소사유 modal start -->
		<div class="modal fade show" id="cancelOpinionModal" tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<h5 class="modal-title">취소 사유</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form class="needs-validation" novalidate>
							<div class="modal-app-wrap">
								<div class="form-row no-gutters">
									<div class="form-group col-12">
										<label for="reason">취소 사유</label>
										<textarea class="form-control" id="cancelOpinion" rows="3" placeholder="취소 사유를 작성해주시기 바랍니다."
												  required=""></textarea>
									</div>
								</div>
							</div>
							<div class="btn-wrap text-center">
								<button type="button" class="btn btn-secondary rounded-0"
										data-dismiss="modal">취소</button>
								<button type="button" class="btn btn-default rounded-0" @click="otCancelAppl">확인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 연장근무취소사유 modal end -->
		<!-- 회수하기 modal start -->
		<div class="modal fade show" id="confirmModal" tabindex="-1" role="dialog">
			<div class="modal-dialog" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<!-- <h5 class="modal-title">모달제목이 들어갑니다.</h5> -->
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form class="needs-validation" novalidate>
							<div class="modal-app-wrap">
								회수 시 신청서 데이터가 모두 삭제됩니다.<br>회수하시겠습니까?
							</div>
							<div class="btn-wrap text-center">
								<button type="button" class="btn btn-secondary rounded-0" data-dismiss="modal">취소</button>
								<button type="button" class="btn btn-default rounded-0" @click="recoveryAppl">확인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 회수하기 modal end -->
		<!-- 근태사유서신청 상세보기 modal start -->
		<div class="modal fade show" id="inOutChangeAppl" tabindex="-1" role="dialog" v-if="appl">
			<div class="modal-dialog modal-md" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<h5 class="modal-title">근태사유서신청</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form>
							<div class="modal-app-wrap">
								<div class="inner-wrap">
									<div class="title">계획 근무시간</div>
									<div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.planSdate">
                                        	{{moment(moment(appl.planSdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.planEdate">
                                        	{{moment(moment(appl.planEdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">출/퇴근 시각</div>
									<div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.entrySdate">
                                        	{{moment(moment(appl.entrySdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.entryEdate">
                                        	{{moment(moment(appl.entryEdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">변경 출/퇴근 시각</div>
									<div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.chgSdate">
                                        	{{moment(moment(appl.chgSdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.chgEdate">
                                        	{{moment(moment(appl.chgEdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">사유</div>
									<div class="desc">
										<template v-if="appl.reason">
											{{appl.reason}}
										</template>
									</div>
								</div>
								<appl-line :bind-data="appl.applLine"></appl-line>
							</div>
							<div class="text-center" v-if="applType=='02'">
								<button type="button" class="btn btn-approval cancel" @click="approval('reject')">반송</button>
								<button type="button" class="btn btn-approval sign" @click="approval('apply')">승인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 근태사유서신청 상세보기 modal end -->
		<!-- 대체휴일 정정신청 상세보기 modal start -->
		<div class="modal fade show" id="chgSubsModal" tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false" v-if="appl">
			<div class="modal-dialog modal-lg" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<h5 class="modal-title">대체휴일 정정요청</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form class="needs-validation" novalidate>
							<div class="modal-app-wrap">
								<div class="inner-wrap">
									<div class="title">휴일근로시간</div>
									<div class="desc">
                                    <span class="time-wrap">
                                        <i class="fas fa-clock"></i>
                                        <span class="time">
                                        	<template v-if="appl.otMinute">
                                        		{{minuteToHHMM(appl.otMinute, 'detail')}}
                                        	</template>
                                        </span>
                                    </span>
										<span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.otSdate">
                                        	{{moment(appl.otSdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="start-time">
                                        	<template v-if="appl.otSdate">
                                        	{{moment(appl.otSdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.otEdate">
                                        	{{moment(appl.otEdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="end-time">
                                        	<template v-if="appl.otEdate">
                                        	{{moment(appl.otEdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">대체휴일</div>
									<template v-if="appl.oldSubs" v-for="sub in appl.oldSubs">
										<div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(sub.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(sub.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{minuteToHHMM(sub.subsMinute,'detail')}}</span>
                                    </span>
										</div>
										<div class="sub-desc" v-if="sub.workSDate&&sub.workEDate">*해당일 근무시간은 {{moment(sub.workSDate).format('HH:mm')}}~{{moment(sub.workEDate).format('HH:mm')}} 입니다.</div>
									</template>
								</div>
								<hr class="separate-bar">
								<div class="inner-wrap">
									<div class="big-title">{{moment(chgSubsTarget.sDate).format('YYYY-MM-DD HH:mm')}} ~ {{moment(chgSubsTarget.eDate).format('YYYY-MM-DD HH:mm')}} 의 변경 대체휴일</div>
									<template v-if="appl.subs" v-for="sub in appl.subs">
										<div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(sub.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(sub.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{minuteToHHMM(sub.subsMinute,'detail')}}</span>
                                    </span>
										</div>
										<div class="sub-desc" v-if="sub.workSDate&&sub.workEDate">*해당일 근무시간은 {{moment(sub.workSDate).format('HH:mm')}}~{{moment(sub.workEDate).format('HH:mm')}} 입니다.</div>
									</template>
								</div>

								<appl-line :bind-data="appl.applLine"></appl-line>
							</div>
							<div class="btn-wrap text-center" v-if="applType=='01' && appl.applSabun == appl.sabun">
								<button type="button" class="btn btn-default rounded-0" v-if="appl.recoveryYn" data-toggle="modal" data-target="#confirmModal">회수하기</button>
							</div>
							<div class="text-center" v-if="applType=='02'">
								<button type="button" class="btn btn-approval cancel" @click="approval('reject')">반송</button>
								<button type="button" class="btn btn-approval sign" @click="approval('apply')">승인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 대체휴일 정정신청 상세보기 modal end -->
		<!-- 결재의견 modal start -->
		<div class="modal fade" id="apprOpinionModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-lg" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" v-if="apprType=='apply'">결재 의견</h5>
						<h5 class="modal-title" v-else>반려 의견</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<div class="form-group col-12">
							<textarea class="form-control" id="apprOpinion" rows="3" :placeholder="apprType=='apply'?'결재 의견 작성':'반려 의견 작성'" v-model="apprOpinion"></textarea>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
						<button type="button" id="apprBtn" class="btn btn-default" v-if="apprType=='apply'" @click="apply">결재하기</button>
						<button type="button" id="apprBtn" class="btn btn-default" v-else  @click="apply">반려하기</button>
					</div>
				</div>
			</div>
		</div>
		<!-- 결재의견 modal end -->

		<!-- 보상휴가신청 상세보기 modal start -->
		<div class="modal fade show" id="compAppl" tabindex="-1" role="dialog"  v-if="appl">
			<div class="modal-dialog modal-md" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<template v-if="appl.applCd=='COMP_CAN'">
							<h5 class="modal-title">보상휴가 취소신청</h5>
						</template>
						<template v-else="">
							<h5 class="modal-title">보상휴가신청</h5>
						</template>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form>
							<div class="modal-app-wrap">
								<div class="inner-wrap">
									<template v-if="appl.applCd=='COMP'">
										<p class="page-sub-title mb-0">잔여 보상휴가시간</p>
										<span class="time-wrap">
				                        <i class="fas fa-clock"></i><span class="time point">{{minuteToHHMM(appl.restMinute, 'detail')}}</span>
				                    </span>
										<hr class="separate-bar">
									</template>
									<div class="inner-wrap">
										<div class="title">근태</div>
										<div class="desc">
											<template v-if="appl.taaNm">
												{{appl.taaNm}}
											</template>
										</div>
									</div>
									<div class="inner-wrap">
										<div class="title">보상휴가시간</div>
										<div class="desc">
	                                    <span class="time-wrap">
	                                        <i class="fas fa-clock"></i>
	                                        <span class="time">
	                                        	<template v-if="appl.compMinute">
	                                        		{{minuteToHHMM(appl.compMinute, 'detail')}}
	                                        	</template>
	                                        </span>
	                                    </span>
											<span class="date-wrap">
	                                        <span class="start-date">
	                                        	<template v-if="appl.symd">
	                                        	{{moment(appl.symd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                        <span class="ml-1 mr-1">~</span>
	                                        <span class="end-date">
	                                        	<template v-if="appl.eymd">
	                                        	{{moment(appl.eymd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                    </span>
										</div>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">사유</div>
									<div class="desc">
										<template v-if="appl.reason">
											{{appl.reason}}
										</template>
									</div>
								</div>
								<appl-line :bind-data="appl.applLine"></appl-line>
								<hr class="bar">
							</div>
							<div class="text-center" v-if="applType=='02'">
								<button type="button" class="btn btn-approval cancel" @click="approval('reject')">반송</button>
								<button type="button" class="btn btn-approval sign" @click="approval('apply')">승인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 보상휴가신청 상세보기 modal end -->

		<!-- 휴가신청 상세보기 modal start -->
		<div class="modal fade show" id="taaAppl" tabindex="-1" role="dialog"  v-if="appl">
			<div class="modal-dialog modal-md" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<h5 class="modal-title">휴가신청</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form>
							<div class="modal-app-wrap">
								<div class="inner-wrap">
									<div class="inner-wrap">
										<div class="title">휴가구분</div>
										<div class="desc">
											<template v-if="appl.taaNm">
												{{appl.taaNm}}
											</template>
										</div>
									</div>
									<div class="inner-wrap">
										<div class="title">휴가기간</div>
										<template v-if = "taaApplDetList.length > 0">
											<div class="desc" v-for = "det in taaApplDetList">
	                                    <span class="date-wrap">
	                                        <span class="start-date">
	                                        	<template v-if="det.symd">
	                                        	{{moment(det.symd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                        <span class="ml-1 mr-1">~</span>
	                                        <span class="end-date">
	                                        	<template v-if="det.eymd">
	                                        	{{moment(det.eymd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                    </span>
											</div>
										</template>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">사유</div>
									<div class="desc">
										<template v-if="taaApplDetList[0]">
											{{taaApplDetList[0].note}}
										</template>
									</div>
								</div>
								<appl-line :bind-data="appl.applLine"></appl-line>
								<hr class="bar">
							</div>
							<div class="text-center" v-if="applType=='02'">
								<button type="button" class="btn btn-approval cancel" @click="approval('reject')">반송</button>
								<button type="button" class="btn btn-approval sign" @click="approval('apply')">승인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 휴가신청 상세보기 modal end -->

		<!-- 휴가취소신청 상세보기 modal start -->
		<div class="modal fade show" id="taaCanAppl" tabindex="-1" role="dialog"  v-if="appl">
			<div class="modal-dialog modal-md" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<h5 class="modal-title">휴가취소신청</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form>
							<div class="modal-app-wrap">
								<div class="inner-wrap">
									<div class="inner-wrap">
										<div class="title">휴가구분</div>
										<div class="desc">
											<template v-if="appl.taaNm">
												{{appl.taaNm}}
											</template>
										</div>
									</div>
									<div class="inner-wrap">
										<div class="title">휴가기간</div>
										<template v-if = "taaApplDetList.length > 0">
											<div class="desc" v-for = "det in taaApplDetList">
	                                    <span class="date-wrap">
	                                        <span class="start-date">
	                                        	<template v-if="det.symd">
	                                        	{{moment(det.symd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                        <span class="ml-1 mr-1">~</span>
	                                        <span class="end-date">
	                                        	<template v-if="det.eymd">
	                                        	{{moment(det.eymd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                    </span>
											</div>
										</template>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">사유</div>
									<div class="desc">
										<template v-if="wtmTaaCanAppl.note">
											{{wtmTaaCanAppl.note}}
										</template>
									</div>
								</div>
								<appl-line :bind-data="appl.applLine"></appl-line>
								<hr class="bar">
							</div>
							<div class="text-center" v-if="applType=='02'">
								<button type="button" class="btn btn-approval cancel" @click="approval('reject')">반송</button>
								<button type="button" class="btn btn-approval sign" @click="approval('apply')">승인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 휴가취소신청 상세보기 modal end -->

		<!-- 출장신청 상세보기 modal start -->
		<div class="modal fade show" id="regaAppl" tabindex="-1" role="dialog"  v-if="appl">
			<div class="modal-dialog modal-md" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<h5 class="modal-title">출장신청</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form>
							<div class="modal-app-wrap">
								<div class="inner-wrap">
									<div class="inner-wrap">
										<div class="title">간주시간 구분</div>
										<div class="desc">
											<template v-if="appl.taaNm">
												{{appl.taaNm}}
											</template>
										</div>
									</div>
									<div class="inner-wrap">
										<div class="title">기간</div>
										<template v-if = "taaApplDetList.length > 0">
											<div class="desc" v-for = "det in taaApplDetList">
	                                    <span class="date-wrap">
	                                        <span class="start-date">
	                                        	<template v-if="det.symd">
	                                        	{{moment(det.symd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                        <span class="ml-1 mr-1">~</span>
	                                        <span class="end-date">
	                                        	<template v-if="det.eymd">
	                                        	{{moment(det.eymd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                    </span>
											</div>
										</template>
									</div>
								</div>
								<div class="inner-wrap">
									<div class="title">사유</div>
									<div class="desc">
										<template v-if="taaApplDetList[0]">
											{{taaApplDetList[0].note}}
										</template>
									</div>
								</div>
								<appl-line :bind-data="appl.applLine"></appl-line>
								<hr class="bar">
							</div>
							<div class="text-center" v-if="applType=='02'">
								<button type="button" class="btn btn-approval cancel" @click="approval('reject')">반송</button>
								<button type="button" class="btn btn-approval sign" @click="approval('apply')">승인</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 출장신청 상세보기 modal end -->
	</div>
 	<div class="container-fluid bg-white mgr-wrap">
	 	<div class="ibsheet-wrapper">
	 		<form id="sheetForm" name="sheetForm">
				<div class="sheet_search outer">
					<div>
						<table>
							<input type="hidden" id="applType" name="applType" value="01" />
							<tr>
								<td>
								    <span class="magnifier"><i class="fas fa-search"></i></span>
								    <span class="search-title">Search</span>
								</td>

								<td>
									<span class="label">상신일 </span>
									<input type="text" id="sYmd" name="sYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
									~
									<input type="text" id="eYmd" name="eYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#eYmd" placeholder="연도-월-일" autocomplete="off"/>
								</td>
								<td>
									<span class="label">사번/성명 </span>
									<input type="text" id="searchKeyword" name="searchKeyword" />
								</td>
								<td>
									<a href="javascript:doAction1('Search');" class="button">조회</a>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</form>

			<div class="ruleTab">
				<div class="innertab inner">
					<div id="includeTab" class="tab">
						<ul class="outer tab_bottom">
							<li><a name="01" href="#tabs-1">결재 신청내역조회</a></li>
							<li><a name="02" href="#tabs-2">미결함</a></li>
							<li><a name="03" href="#tabs-3">기결함</a></li>
						</ul>

						<div id="tabs-1">
							<div class="layout_tabs">
								<table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
									<tr>
										<td>
											<script type="text/javascript"> createIBSheet("sheet1", "100%", fullsheetH, "kr"); </script>
										</td>
									</tr>
								</table>
							</div>
						</div>

						<div id="tabs-2">
							<div class="layout_tabs">
								<table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
									<tr>
										<td>
											<script type="text/javascript"> createIBSheet("sheet2", "100%", fullsheetH, "kr"); </script>
										</td>
									</tr>
								</table>
							</div>
						</div>

						<div id="tabs-3">
							<div class="layout_tabs">
								<table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
									<tr>
										<td>
											<script type="text/javascript"> createIBSheet("sheet3", "100%", fullsheetH, "kr"); </script>
										</td>
									</tr>
								</table>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

	var approvalVue = new Vue({
		el: "#approval",
		components: {
			'appl-line': applLine
		},
		data : {
			apprList: [],
			apprType: '', // 승인 or 반려
			applType: '${applType}', // 신청 내역 조회('01') or 미결('02') or 기결('03')
			apprOpinion: '',
			appl: {}, //신청서view
			appr: {}, //승인할 신청서
			chgSubsTarget: {}, //대체휴일 정정 데이터
			taaAppl:{},
			taaApplDetList:[],
			wtmTaaCanAppl:{},
			wtmTaaCode:{}

		},
		mounted: function(){
			// this.getApprovalList(this.applType); //신청내역 조회
		},
		methods : {
			getApprovalList: function(applType){
				doAction1("Search");
			},
			viewAppl: function(appr){
				var $this = this;
				/* if(appr.applCd=='OT') {
                    //연장근무신청서
                    this.getOTAppl(appr.applId);
                } else if(appr.applCd=='SELE_F' || appr.applCd=='SELE_C') {
                    //선근제 신청서
                    this.getFlexibleSeleAppl(appr.applId);
                } */
				$this.appl = appr.appl;
				$this.appl['applCd'] = appr.applCd;

				if(appr.applCd=='OT' || appr.applCd=='OT_CAN') {
					//연장근무신청서
					$("#otAppl").modal("show");
				} else if(appr.applCd=='SELE_F' || appr.applCd=='SELE_C' || appr.applCd=='ELAS' || appr.applCd=='DIFF') {
					//선근제 신청서
					$("#flexibleAppl").modal("show");
				} else if(appr.applCd=='ENTRY_CHG') {
					//근태 사유서
					$("#inOutChangeAppl").modal("show");
				} else if(appr.applCd=='SUBS_CHG') {

					if($this.appl.hasOwnProperty("subs") && $this.appl.hasOwnProperty("oldSubs")
							&& $this.appl.subs!=null && $this.appl.oldSubs!=null
							&& $this.appl.subs!=undefined && $this.appl.oldSubs!=undefined
							&& $this.appl.subs!="" && $this.appl.oldSubs!='' ) {

						$this.appl.oldSubs.map(function(o){
							$this.appl.subs.map(function(s){
								if(s.hasOwnProperty('oldSubsApplId') && s.oldSubsApplId!=null && s.oldSubsApplId==o.otSubsApplId) {
									$this.chgSubsTarget = {
										sDate : o.subsSdate,
										eDate : o.subsEdate
									};
								}
							});
						});
					}


					//대체휴가 정정
					$("#chgSubsModal").modal("show");
				} else if(appr.applCd=='COMP' || appr.applCd=='COMP_CAN') {
					//근태 사유서
					$("#compAppl").modal("show");

					//휴가신청
				} else if(appr.applCd=='ANNUAL' || appr.applCd=='REGA' ) {
					$this.taaAppl = appr.appl.taaAppl;
					$this.taaApplDetList = appr.appl.taaApplDetList;
					$this.wtmTaaCode = appr.appl.wtmTaaCode;

					if(appr.applCd == 'ANNUAL') {
						$("#taaAppl").modal("show");
					} else if(appr.applCd == 'REGA') {
						$("#regaAppl").modal("show");
					}

				} else if(appr.applCd=='ANNUAL_CAN') {
					//휴가취소신청
					$this.taaAppl = appr.appl.taaAppl;
					$this.taaApplDetList = appr.appl.taaApplDetList;
					$this.wtmTaaCanAppl = appr.appl.wtmTaaCanAppl;
					$("#taaCanAppl").modal("show");

				} else if(appr.applCd=='REGA_CAN') {
					//출장취소신청
					$this.taaAppl = appr.appl.taaAppl;
					$this.taaApplDetList = appr.appl.taaApplDetList;
					$this.wtmTaaCanAppl = appr.appl.wtmTaaCanAppl;
					$("#taaCanAppl").modal("show");
				}
				$("#loading").hide();

			},
			getOTAppl: function(applId){
				var $this = this;

				var param = {
					applId: applId
				};

				Util.ajax({
					url: "${rc.getContextPath()}/otAppl",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						$this.appl = {};
						if(data!=null) {
							$this.appl = data;
							console.log(data);
							$("#otAppl").modal("show");
						}
					},
					error: function(e) {
						console.log(e);
						$this.appl = {};
					}
				});

			},
			getFlexibleSeleAppl: function(applId){
				var $this = this;

				var param = {
					applId: applId
				};

				Util.ajax({
					url: "${rc.getContextPath()}/flexibleAppl",
					type: "GET",
					contentType: 'application/json',
					dataType: "json",
					data: param,
					success: function(data) {
						$this.appl = {};
						if(data!=null) {
							$this.appl = data;
							$("#flexibleAppl").modal("show");
						}

					},
					error: function(e) {
						$this.flexibleAppl = {};
					}
				});
			},
			approval: function(apprType){ //결재
				this.apprType = apprType;

				$('#apprOpinionModal').modal("show");
			},
			apply: function(){
				var $this = this;

				var saveYn = true;

				//반려일 때만 결재의견 체크
				if($this.apprType=='reject' && $this.apprOpinion=='') {
					saveYn = false;
					$("#alertText").html("의견을 입력해 주세요.");
				}

				if(saveYn) {
					//$('#apprOpinionModal').modal("hide");
					$("#loading").show();
					var appr = $this.appr;
					var param = appr;

					if($this.apprOpinion!='')
						param['apprOpinion'] = $this.apprOpinion;

					//연장근무신청 대상자
					if(appr.appl.hasOwnProperty('targetList') && appr.appl.targetList!=null && appr.appl.targetList!=undefined) {
						var applSabuns = [];
						/* appr.appl.targetList.map(function(t){
                            applSabuns.push(t.sabun);
                        }); */

						$.each(appr.appl.targetList, function(k,v){
							applSabuns.push(k);
						});

						param['applSabuns'] = JSON.stringify(applSabuns);
					}

					if(appr.applCd=='OT') {
						param['ymd'] = moment(appr.appl.ymd).format('YYYYMMDD');
						param['otSdate'] = moment(appr.appl.otSdate).format('YYYYMMDDHHmm');
						param['otEdate'] = moment(appr.appl.otEdate).format('YYYYMMDDHHmm');
					}else if(appr.applCd=='ENTRY_CHG') {
						param['ymd'] = moment(appr.appl.ymd).format('YYYYMMDD');

						if(appr.appl.planSdate!=null && appr.appl.planSdate!=undefined && appr.appl.planSdate!='')
							param['planSdate'] = appr.appl.planSdate;
						if(appr.appl.planEdate!=null && appr.appl.planEdate!=undefined && appr.appl.planEdate!='')
							param['planEdate'] = appr.appl.planEdate;
						if(appr.appl.entrySdate!=null && appr.appl.entrySdate!=undefined && appr.appl.entrySdate!='')
							param['entrySdate'] = appr.appl.entrySdate;
						if(appr.appl.entryEdate!=null && appr.appl.entryEdate!=undefined && appr.appl.entryEdate!='')
							param['entryEdate'] = appr.appl.entryEdate;
						if(appr.appl.chgSdate!=null && appr.appl.chgSdate!=undefined && appr.appl.chgSdate!='')
							param['chgSdate'] = appr.appl.chgSdate;
						if(appr.appl.chgEdate!=null && appr.appl.chgEdate!=undefined && appr.appl.chgEdate!='')
							param['chgEdate'] = appr.appl.chgEdate;

					} else if(appr.applCd=='ANNUAL') { // 휴가신청
						param['symd'] = moment(appr.appl.symd).format('YYYYMMDD');
						param['eymd'] = moment(appr.appl.eymd).format('YYYYMMDD');
						param['sabun'] = appr.appl.sabun;
					}

					Util.ajax({
						url: "${rc.getContextPath()}/appl/"+$this.apprType,
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							$("#loading").hide();
							$('#apprOpinionModal .close').click();

							if(data!=null && data.status=='OK') {
								$("#alertText").html("결재되었습니다.");
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
									$this.getApprovalList($this.applType);
									$('.close').click();
								});
							} else {
								$("#alertText").html(data.message);
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
								});
							}

							$("#alertModal").modal("show");
						},
						error: function(e) {
							$("#loading").hide();
							console.log(e);
							$("#alertText").html("확인요청 시 오류가 발생했습니다.");
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
							});
							$("#alertModal").modal("show");
						}
					});
				} else {
					$("#alertModal").on('hidden.bs.modal',function(){
						$("#apprOpinion").focus();
					});
					$("#alertModal").modal("show");
				}
			},
			otCancelAppl: function(){ //연장근무취소신청
				var $this = this;

				$("#loading").show();

				var appl = $this.appl;

				var param = {
					workDayResultId: appl.workDayResultId,
					otApplId: appl.otApplId,
					applId: appl.applId,
					status: appl.applStatusCd,
					workTypeCd : 'OT_CAN',
					reason: $("#cancelOpinion").val()
				};

				Util.ajax({
					url: "${rc.getContextPath()}/otCanAppl/request",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						$("#loading").hide();
						if(data!=null && data.status=='OK') {
							$("#alertText").html("취소요청 되었습니다.");
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
								$("#cancelOpinionModal").modal("hide");
								$("#otAppl").modal("hide");
								$this.getApprovalList($this.applType);
							});
						} else {
							$("#alertText").html(data.message);
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
							});
						}

						$("#alertModal").modal("show");
					},
					error: function(e) {
						$("#loading").hide();
						console.log(e);
						$("#alertText").html("연장근무 취소 시 오류가 발생했습니다.");
						$("#alertModal").on('hidden.bs.modal',function(){
							$("#alertModal").off('hidden.bs.modal');
						});
						$("#alertModal").modal("show");
					}
				});

			},
			recoveryAppl: function(){ //연장근무 회수
				var $this = this;

				var appl = $this.appl;

				$("#loading").show();

				var param = {
					applCd : appl.applCd,
					applId : appl.applId
				};

				Util.ajax({
					url: "${rc.getContextPath()}/appl/delete",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						$("#loading").hide();
						if(data!=null && data.status=='OK') {
							$("#alertText").html("회수되었습니다.");
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');

								$("#confirmModal").modal("hide");

								if(appl.applCd == 'SUBS_CHG') {
									$("#chgSubsModal").modal("hide");
								} else {
									$("#otAppl").modal("hide");
								}
								$this.getApprovalList($this.applType);
							});
						} else {
							$("#alertText").html(data.message);
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
							});
						}

						$("#alertModal").modal("show");
					},
					error: function(e) {
						$("#loading").hide();
						console.log(e);
						$("#alertText").html("회수 시 오류가 발생했습니다.");
						$("#alertModal").on('hidden.bs.modal',function(){
							$("#alertModal").off('hidden.bs.modal');
						});
						$("#alertModal").modal("show");
					}
				});

			},
			accordionDropdown: function(target) {
				var $el = $('#accordion');
				$this = $(target),
						$next = $this.next();

				$next.slideToggle();
				$this.parent().toggleClass('open');

				$el.find('.submenu').not($next).slideUp().parent().removeClass('open');

			}
		}
	});

	var newIframe;
	var oldIframe;
	var iframeIdx;

	newIframe = $('#tabs-1 layout_tabs');
	iframeIdx = 0;
	$( "#includeTab" ).tabs({
		beforeActivate: function(event, ui) {
			iframeIdx = ui.newTab.index();
			newIframe = $(ui.newPanel).find('layout_tabs');
			oldIframe = $(ui.oldPanel).find('layout_tabs');
			//showIframe();
		},
		activate: function(event, ui) {
			sheet1.RemoveAll();
			sheet2.RemoveAll();
			sheet3.RemoveAll();

			if(iframeIdx == 0) {

				$("#tabs-1").show();
				$("#tabs-2").hide();
				$("#tabs-3").hide();
				$("#applType").val("01");
				approvalVue.applType = '01';

			} else if(iframeIdx == 1) {
				$("#tabs-1").hide();
				$("#tabs-2").show();
				$("#tabs-3").hide();
				$("#applType").val("02");
				approvalVue.applType = '02';
			} else if(iframeIdx == 2) {
				$("#tabs-1").hide();
				$("#tabs-2").hide();
				$("#tabs-3").show();
				$("#applType").val("03");
				approvalVue.applType = '03';
			}
			doAction1("Search");
			sheetResize();
		}
	});

   	$(function() {
   		//resize
		$(window).smartresize(sheetResize);
   	
   		$('#sYmd, #eYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
   		<#--$("#sYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");-->
	    <#--$("#eYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");-->
	    
	    new jBox('Tooltip', {
       	    attach: '#Tooltip-otApplList',
       	    target: '#Tooltip-otApplList',
       	    theme: 'TooltipBorder',
       	    trigger: 'click',
       	    adjustTracker: true,
       	    closeOnClick: 'body',
       	    closeButton: 'box',
       	    animation: 'move',
       	    position: {
       	      x: 'left',
       	      y: 'top'
       	    },
       	    outside: 'y',
       	    pointer: 'left:20',
       	    offset: {
       	      x: 25
       	    },
       	    content: '결재 신청 내역을 조회합니다.',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	});
   		
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,MergeSheet:msPrevColumnMerge,Page:100};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"결재번호",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applId",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결재코드",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"신청서",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결재상태",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applStatusCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결재상세",	Type:"Image",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"applDetail",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:1 },
			{Header:"상신일",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"applYmd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"		,	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"applSabun",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"시작일",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"종료일",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사유",		Type:"Text",		Hidden:0,	Width:250,	Align:"Left",	ColMerge:0,	SaveName:"reason",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 }
		];
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);

		sheet1.SetImageList(0,"${rc.getContextPath()}/IBLeaders/Sheet/icon/icon_popup.png");

		//사업장
		var reasonCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "REASON_CD"), "");
		sheet1.SetColProperty("reasonCd", {ComboText:reasonCdList[0], ComboCode:reasonCdList[1]} );
		var applStatusCd = stfConvCode(codeList("${rc.getContextPath()}/code/list", "APPL_STATUS_CD"), "");
		sheet1.SetColProperty("applStatusCd", {ComboText:applStatusCd[0], ComboCode:applStatusCd[1]} );


		// 미결함
		var initdata2 = {};

		initdata2.Cfg = {SearchMode:smLazyLoad,MergeSheet:msPrevColumnMerge,Page:100};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata2.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"결재번호",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applId",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결재코드",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"신청서",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결재상태",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applStatusCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결재상세",	Type:"Image",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"applDetail",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:1 },
			{Header:"상신일",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"applYmd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"		,	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"applSabun",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"시작일",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"종료일",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사유",		Type:"Text",		Hidden:0,	Width:250,	Align:"Left",	ColMerge:0,	SaveName:"reason",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"반려",		Type:"Html", 		Hidden:1,  	Width:100,  Align:"Center", ColMerge:0, SaveName:"cancelImg",  		KeyField:0, Format:"",      PointCount:0,   UpdateEdit:1,   InsertEdit:0,   EditLen:1 },
			{Header:"승인",		Type:"Html", 		Hidden:1,  	Width:100,  Align:"Center", ColMerge:0, SaveName:"applyImg",  		KeyField:0, Format:"",      PointCount:0,   UpdateEdit:1,   InsertEdit:0,   EditLen:1 },
			{Header:"1",	    Type:"Text",	    Hidden:1,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"apprSeq",			KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"2",	    Type:"Text",	    Hidden:1,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"apprSabun",		KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"3",	    Type:"Text",	    Hidden:1,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"apprDate",		KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"4",	    Type:"Text",	    Hidden:1,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"apprStatusCd",	KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"5",	    Type:"Text",	    Hidden:1,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"apprTypeCd",		KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"6",	    Type:"Text",	    Hidden:1,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"applKey",			KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },


		];

		IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUnicodeByte(3);

		//사업장
		sheet2.SetColProperty("reasonCd", {ComboText:reasonCdList[0], ComboCode:reasonCdList[1]} );
		sheet2.SetColProperty("applStatusCd", {ComboText:applStatusCd[0], ComboCode:applStatusCd[1]} );

		sheet2.SetImageList(0,"${rc.getContextPath()}/IBLeaders/Sheet/icon/icon_popup.png");

		var initdata3 = {};

		initdata3.Cfg = {SearchMode:smLazyLoad,MergeSheet:msPrevColumnMerge,Page:100};
		initdata3.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata3.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"결재번호",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:1,	SaveName:"applId",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"신청서",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결재상태",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applStatusCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결재상세",	Type:"Image",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"applDetail",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:1 },
			{Header:"결재코드",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"상신일",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"applYmd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"		,	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"applSabun",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"시작일",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"종료일",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사유",		Type:"Text",		Hidden:0,	Width:250,	Align:"Left",	ColMerge:0,	SaveName:"reason",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 }
		];

		IBS_InitSheet(sheet3, initdata3);
		sheet3.SetEditable(true);
		sheet3.SetVisible(true);
		sheet3.SetUnicodeByte(3);

		//사업장
		sheet3.SetColProperty("reasonCd", {ComboText:reasonCdList[0], ComboCode:reasonCdList[1]} );
		sheet3.SetColProperty("applStatusCd", {ComboText:applStatusCd[0], ComboCode:applStatusCd[1]} );

		sheet3.SetImageList(0,"${rc.getContextPath()}/IBLeaders/Sheet/icon/icon_popup.png");

		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			if(iframeIdx == 0) {
				sheet1.DoSearch( "${rc.getContextPath()}/appl/approvalApplGrList" , $("#sheetForm").serialize());
			} else if(iframeIdx == 1) {
				sheet2.DoSearch( "${rc.getContextPath()}/appl/approvalApplGrList" , $("#sheetForm").serialize());
			} else if(iframeIdx == 2) {
				sheet3.DoSearch( "${rc.getContextPath()}/appl/approvalApplGrList" , $("#sheetForm").serialize());
			}
			sheetResize();
			break;
		case "Down2Excel":
			var downcol = makeHiddenSkipCol(sheet1);
			var param = {DownCols:downcol, SheetDesign:1, Merge:1};
			sheet1.Down2Excel(param); 
			break;
		case "Save":
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave( "${rc.getContextPath()}/otAppl/saveApplSts", $("#sheetForm").serialize());
			break;
		}
	}

	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
			for(i=1;i<=sheet1.RowCount();i++){
		   	  	var applStatusCd = sheet1.GetCellValue(i, "applStatusCd");
		   	  	//결재상태가 '결재처리중' 또는 '처리완료' 상태일때만 변경 가능하도록
		   	  	if(applStatusCd == "21"){
		   	  		sheet1.SetCellFontBold(i, "applStatusCd", 1);
		   	  	}else if(applStatusCd == "22" || applStatusCd == "32"){
					sheet1.SetCellFontColor(i, "applStatusCd","#ee6a65");
					sheet1.SetCellFontBold(i, "applStatusCd", 1);
		   	  	}
		   	}
			sheet1.RenderSheet(2);

		} catch (ex) {
			swtAlert("OnSearchEnd Event Error " + ex);
		}
	}

	// 조회 후 에러 메시지
	function sheet2_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
			for(i=1;i<=sheet2.RowCount();i++){
				var applStatusCd = sheet2.GetCellValue(i, "applStatusCd");
				//결재상태가 '결재처리중' 또는 '처리완료' 상태일때만 변경 가능하도록
				if(applStatusCd == "21"){
					sheet2.SetCellFontBold(i, "applStatusCd", 1);
				}else if(applStatusCd == "22" || applStatusCd == "32"){
					sheet2.SetCellFontColor(i, "applStatusCd","#ee6a65");
					sheet2.SetCellFontBold(i, "applStatusCd", 1);
				}
			}
			sheet2.RenderSheet(2);

		} catch (ex) {
			swtAlert("OnSearchEnd Event Error " + ex);
		}
	}

	// 조회 후 에러 메시지
	function sheet3_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
			for(i=1;i<=sheet3.RowCount();i++){
				var applStatusCd = sheet3.GetCellValue(i, "applStatusCd");
				//결재상태가 '결재처리중' 또는 '처리완료' 상태일때만 변경 가능하도록
				if(applStatusCd == "21"){
					sheet3.SetCellFontBold(i, "applStatusCd", 1);
				}else if(applStatusCd == "22" || applStatusCd == "32"){
					sheet3.SetCellFontColor(i, "applStatusCd","#ee6a65");
					sheet3.SetCellFontBold(i, "applStatusCd", 1);
				}
			}
			sheet3.RenderSheet(2);

		} catch (ex) {
			swtAlert("OnSearchEnd Event Error " + ex);
		}
	}
	

	//셀 클릭시 이벤트
	function sheet1_OnClick(Row, Col, Value) {
		try{
			if(Row > 0 && sheet1.ColSaveName(Col) == "applDetail" ){
				var applId = sheet1.GetCellValue( sheet1.GetSelectRow(), "applId");
				var applCd = sheet1.GetCellValue( sheet1.GetSelectRow(), "applCd");
				var applSabun = sheet1.GetCellValue( sheet1.GetSelectRow(), "applSabun");

				var param = {
					"applId" : applId,
					"applCd" : applCd,
					"applSabun" : applSabun
				}

				$("#loading").show();
				Util.ajax({
					url: "${rc.getContextPath()}/appl/getApplDetail",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						if(data.status=='OK' && data!=null) {
							// approvalVue.appl = data.appl;
							approvalVue.viewAppl(data);
						}
					},
					error: function(e) {
						$("#loading").hide();
						approvalVue.apprList = [];
					}
				});
			}
		}catch(ex){
			$("#loading").hide();
			swtAlert("OnClick Event Error : " + ex);
		}
	}

	//셀 클릭시 이벤트
	function sheet2_OnClick(Row, Col, Value) {
		try{

			if(Row > 0 && (sheet2.ColSaveName(Col) == "applDetail" || sheet2.ColSaveName(Col) == "applyImg" || sheet2.ColSaveName(Col) == "cancelImg") ){

				var colName = sheet2.ColSaveName(Col);

				var applId = sheet2.GetCellValue( sheet1.GetSelectRow(), "applId");
				var applCd = sheet2.GetCellValue( sheet1.GetSelectRow(), "applCd");
				var applSabun = sheet2.GetCellValue( sheet1.GetSelectRow(), "applSabun");

				var apprSeq = sheet2.GetCellValue( sheet1.GetSelectRow(), "apprSeq");
				var apprStatusCd = sheet2.GetCellValue( sheet1.GetSelectRow(), "apprStatusCd");

				var param = {
					"applId" : applId,
					"applCd" : applCd,
					"applSabun" : applSabun
				}


				$("#loading").show();
				Util.ajax({
					url: "${rc.getContextPath()}/appl/getApplDetail",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						if(data.status=='OK' && data!=null) {
							// approvalVue.appl = data.appl;
							data.applId = applId;
							data.apprSeq = apprSeq;
							data.applSabun = applSabun;
							approvalVue.appr = data;
							if(colName == "applyImg" ) {
								approvalVue.approval("apply");
							} else if(colName == "cancelImg"){
								approvalVue.approval("reject");
							} else {
								approvalVue.viewAppl(data);
							}
						}
					},
					error: function(e) {
						$("#loading").hide();
						approvalVue.apprList = [];
					}
				});
			}
		}catch(ex){
			$("#loading").hide();
			swtAlert("OnClick Event Error : " + ex);
		}
	}

	//셀 클릭시 이벤트
	function sheet3_OnClick(Row, Col, Value) {
		try{
			if(Row > 0 && sheet3.ColSaveName(Col) == "applDetail" ){
				var applId = sheet3.GetCellValue( sheet1.GetSelectRow(), "applId");
				var applCd = sheet3.GetCellValue( sheet1.GetSelectRow(), "applCd");
				var applSabun = sheet3.GetCellValue( sheet1.GetSelectRow(), "applSabun");

				var param = {
					"applId" : applId,
					"applCd" : applCd,
					"applSabun" : applSabun
				}

				$("#loading").show();
				Util.ajax({
					url: "${rc.getContextPath()}/appl/getApplDetail",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						if(data.status=='OK' && data!=null) {
							// approvalVue.appl = data.appl;
							approvalVue.viewAppl(data);
						}
					},
					error: function(e) {
						$("#loading").hide();
						approvalVue.apprList = [];
					}
				});
			}
		}catch(ex){
			$("#loading").hide();
			swtAlert("OnClick Event Error : " + ex);
		}
	}

	function setApply() {

	}

	function setCancel() {

	}

</script>