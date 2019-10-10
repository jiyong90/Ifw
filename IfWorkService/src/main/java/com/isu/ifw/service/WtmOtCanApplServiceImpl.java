package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isu.ifw.entity.WtmAppl;
import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.entity.WtmApplLine;
import com.isu.ifw.entity.WtmOtAppl;
import com.isu.ifw.entity.WtmOtCanAppl;
import com.isu.ifw.entity.WtmOtSubsAppl;
import com.isu.ifw.entity.WtmWorkDayResult;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.mapper.WtmOtCanApplMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmApplLineRepository;
import com.isu.ifw.repository.WtmApplRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmOtApplRepository;
import com.isu.ifw.repository.WtmOtCanApplRepository;
import com.isu.ifw.repository.WtmOtSubsApplRepository;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.repository.WtmWorkDayResultRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmApplLineVO;
import com.isu.option.vo.ReturnParam;

@Service("wtmOtCanApplService")
public class WtmOtCanApplServiceImpl implements WtmApplService {

	@Autowired
	WtmApplMapper applMapper;
	@Autowired
	WtmApplRepository wtmApplRepo;
	/**
	 * 속성값 조회
	 */
	@Autowired
	WtmPropertieRepository wtmPropertieRepo;

	@Autowired
	WtmApplLineRepository wtmApplLineRepo;

	@Autowired
	WtmApplCodeRepository wtmApplCodeRepo;

	@Autowired
	WtmOtApplRepository wtmOtApplRepo;
	
	@Autowired
	WtmOtCanApplRepository wtmOtCanApplRepo;
	
	@Autowired
	WtmOtSubsApplRepository wtmOtSubsCanApplRepo;

	@Autowired
	WtmOtSubsApplRepository wtmOtSubsApplRepo;
	
	@Autowired
	WtmWorkCalendarRepository wtmWorkCalendarRepository;
	@Autowired
	WtmFlexibleStdMgrRepository wtmFlexibleStdMgrRepo;
	@Autowired
	WtmWorkDayResultRepository wtmWorkDayResultRepo;
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
	
	@Autowired
	WtmOtCanApplMapper wtmOtCanApplMapper;
	
	@Override
	public Map<String, Object> getAppl(Long applId) {
		try {
			Map<String, Object> otAppl = wtmOtCanApplMapper.otCanApplfindByApplId(applId);
			 
			otAppl.put("applLine", applMapper.getWtmApplLineByApplId(applId));
			
			return otAppl;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<WtmApplLineVO> getApplLine(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap,
			String userId) {
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("tenantId", tenantId);
		return applMapper.getWtmApplLine(paramMap);
	}

	@Override
	public List<Map<String, Object>> getPrevApplList(Long tenantId, String enterCd, String sabun,
			Map<String, Object> paramMap, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getLastAppl(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getApprList(Long tenantId, String enterCd, String empNo,
			Map<String, Object> paramMap, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void request(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap,
			String sabun, String userId) throws Exception {
		ReturnParam rp = imsi(tenantId, enterCd, applId, workTypeCd, paramMap, this.APPL_STATUS_APPLY_ING, sabun, userId);
		
		//rp.put("flexibleApplId", flexibleAppl.getFlexibleApplId());
		
		//결재라인 상태값 업데이트
		//WtmApplLine line = wtmApplLineRepo.findByApplIdAndApprSeq(applId, apprSeq);
		
		if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
			applId = Long.valueOf(rp.get("applId").toString());
			List<WtmApplLine> lines = wtmApplLineRepo.findByApplIdOrderByApprSeqAsc(applId);
			 
			if(lines != null && lines.size() > 0) {
				for(WtmApplLine line : lines) {
					//첫번째 결재자의 상태만 변경 후 스탑
					line.setApprStatusCd(APPR_STATUS_REQUEST);
					line = wtmApplLineRepo.save(line);
					break;
					 
				}
			}
		}
		
	}

	@Transactional
	@Override
	public ReturnParam apply(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap,
			String sabun, String userId) throws Exception {
		ReturnParam rp = new ReturnParam();
		paramMap.put("applId", applId);
		
		String applSabun = paramMap.get("applSabun").toString();
		
		rp = this.validate(tenantId, enterCd, applSabun, "", paramMap);
		//rp = validate(applId);
		if(rp.getStatus().equals("FAIL")) {
			throw new Exception(rp.get("message").toString());
		}
		
		//결재라인 상태값 업데이트
		//WtmApplLine line = wtmApplLineRepo.findByApplIdAndApprSeq(applId, apprSeq);
		List<WtmApplLine> lines = wtmApplLineRepo.findByApplIdOrderByApprSeqAsc(applId);

		//마지막 결재자인지 확인하자
		boolean lastAppr = false;
		if(lines != null && lines.size() > 0) {
			for(WtmApplLine line : lines) {
				if(line.getApprSeq() == apprSeq && line.getApprSabun().equals(sabun)) {
					line.setApprStatusCd(APPR_STATUS_APPLY);
					line.setApprDate(WtmUtil.parseDateStr(new Date(), null));
					//결재의견
					if(paramMap != null && paramMap.containsKey("apprOpinion")) {
						line.setApprOpinion(paramMap.get("apprOpinion").toString());
						line.setUpdateId(userId);
					}
					line = wtmApplLineRepo.save(line);
					lastAppr = true;
				}else {
					if(lastAppr) {
						line.setApprStatusCd(APPR_STATUS_REQUEST);
						line = wtmApplLineRepo.save(line);
					}
					lastAppr = false;
				}
			}
		}
		
		
		//신청서 메인 상태값 업데이트
		WtmAppl appl = wtmApplRepo.findById(applId).get();
		appl.setApplStatusCd((lastAppr)?APPL_STATUS_APPR:APPL_STATUS_APPLY_ING);
		appl.setApplYmd(WtmUtil.parseDateStr(new Date(), null));
		appl.setUpdateId(userId);
		
		appl = wtmApplRepo.save(appl);
		
		if(lastAppr) {
			//취소하는 근무시간 정보를 지운다.
			WtmOtCanAppl otCanAppl = wtmOtCanApplRepo.findByApplId(applId);
			 
			Long deletedApplId = null;
			
			WtmWorkDayResult dayResult = wtmWorkDayResultRepo.findById(otCanAppl.getWorkDayResultId()).get();
			//지우려는 정보의 신청정보가 있다면 관련된 정보도 같이 지워준다 대체휴일과 같은 정보..
			if(dayResult.getApplId() != null) {
				deletedApplId = dayResult.getApplId(); 
			}
			
			wtmWorkDayResultRepo.delete(dayResult);
			 
			rp.put("sabun", dayResult.getSabun());
			rp.put("symd", dayResult.getYmd());
			rp.put("eymd", dayResult.getYmd());
			
			
			if(deletedApplId != null) {
				//대체 휴일 정보를 찾자
				List<WtmOtSubsAppl> otSubsAppls = wtmOtSubsApplRepo.findByApplId(deletedApplId);
				if(otSubsAppls != null && otSubsAppls.size() > 0) {
					String currYmd = null;
					paramMap.put("tenantId", tenantId);
					paramMap.put("enterCd", enterCd);
					Map<String, Map<String, Date>> resetBaseTime = new HashMap<String, Map<String, Date>>();
					for(WtmOtSubsAppl otSubsAppl : otSubsAppls) {
						List<String> timeTypeCd = new ArrayList<>();
						timeTypeCd.add("BASE");
						timeTypeCd.add("SUBS"); 
						
						List<WtmWorkDayResult> workDayResults = wtmWorkDayResultRepo.findByTenantIdAndEnterCdAndSabunAndTimeTypeCdInAndYmdBetweenOrderByPlanSdateAsc(tenantId, enterCd, applSabun, timeTypeCd, otSubsAppl.getSubYmd(), otSubsAppl.getSubYmd());
						 
						Date sdate = otSubsAppl.getSubsSdate();
						Date edate = otSubsAppl.getSubsEdate();
						 
						int cnt = 0;
						Boolean isPrev = null;
						for(WtmWorkDayResult res : workDayResults) {
							 
							if(res.getTimeTypeCd().equals(WtmApplService.TIME_TYPE_SUBS) && res.getPlanSdate().compareTo(sdate) == 0 && res.getPlanEdate().compareTo(edate) == 0) {
								if(cnt == 0) {
									//시작시간이 대체휴일이면 다음 데이터 여부를 판단하고 다음데이터가 SUBS BASE로 변경하자
									if(workDayResults.size() == (cnt+1) || workDayResults.get(cnt+1).getTimeTypeCd().equals(WtmApplService.TIME_TYPE_SUBS) ) {
										//뒤에 데이터가 없으면
										res.setTimeTypeCd(WtmApplService.TIME_TYPE_BASE);
										res.setApplId(otSubsAppl.getOldSubsApplId());
										wtmWorkDayResultRepo.save(res);
										break;
									}else { 
										WtmWorkDayResult modiResult = workDayResults.get(cnt+1);
										modiResult.setPlanSdate(sdate);
										modiResult.setApplId(otSubsAppl.getOldSubsApplId());
										
										wtmWorkDayResultRepo.deleteById(res.getWorkDayResultId());
										wtmWorkDayResultRepo.save(modiResult);
										break;
									}
								}else {
									// 삭제하려는 데이터면 이전 데이터가 SUBS 인지를 체크 한다.
									if(workDayResults.get(cnt-1).getTimeTypeCd().equals(WtmApplService.TIME_TYPE_SUBS)) {
										isPrev = false;
									}else {
										isPrev = true;
									}
									// 삭제하려는 데이터가 마지막인지 확인하자
									if(workDayResults.size() == (cnt+1)) {
										if(isPrev) {
											//이전 데이터로 지우려는 데이터의 종료일로 바꿔주면 땡
											WtmWorkDayResult modiResult = workDayResults.get(cnt-1);
											modiResult.setPlanEdate(edate);
											
											wtmWorkDayResultRepo.deleteById(res.getWorkDayResultId());
											wtmWorkDayResultRepo.save(modiResult);
											break;
										}else {
											// SUBS 
											// SUBS(지우려는 데이터) -> BASE 로 변
											res.setTimeTypeCd(WtmApplService.TIME_TYPE_BASE);
											res.setApplId(otSubsAppl.getOldSubsApplId());

											wtmWorkDayResultRepo.save(res);
											break;
										}
									}else {
										//마지막 데이터가 아니면 다음 데이터의 timeTypeCd를 확인하자
										if(workDayResults.get(cnt+1).getTimeTypeCd().equals(WtmApplService.TIME_TYPE_SUBS)) {
											if(isPrev) { 
												//이전 데이터로 지우려는 데이터의 종료일로 바꿔주면 땡
												WtmWorkDayResult modiResult = workDayResults.get(cnt-1);
												modiResult.setPlanEdate(edate);
												
												wtmWorkDayResultRepo.deleteById(res.getWorkDayResultId());
												wtmWorkDayResultRepo.save(modiResult);
												break;
											}else { 
												//SUBS
												// SUBS(지우려는 데이터) -> BASE 로 변
												//SUBS
												res.setTimeTypeCd(WtmApplService.TIME_TYPE_BASE);
												res.setApplId(otSubsAppl.getOldSubsApplId()); 
												wtmWorkDayResultRepo.save(res);
												break;
											}
										}else { 
											if(isPrev) { 
												//1. BASE
												//2. SUBS
												//3. BASE 인 상황  1,2번을 보내드리고 3번으로 통합하자
												wtmWorkDayResultRepo.deleteById(workDayResults.get(cnt-1).getWorkDayResultId());
												wtmWorkDayResultRepo.deleteById(res.getWorkDayResultId());

												WtmWorkDayResult modiResult = workDayResults.get(cnt+1); 
												modiResult.setPlanSdate(workDayResults.get(cnt-1).getPlanSdate());  
												wtmWorkDayResultRepo.save(modiResult);
												break;
											}else {
												//이후 데이터로 지우려는 데이터의 시작일로 바꿔주면 땡
												WtmWorkDayResult modiResult = workDayResults.get(cnt+1);
												modiResult.setPlanSdate(sdate); 
												wtmWorkDayResultRepo.deleteById(res.getWorkDayResultId());
												wtmWorkDayResultRepo.save(modiResult);
												break;
											}
											
										} 
									}
									
								}
								
								
							}
							cnt++;
						}
						 
					}
				
				}
			}
		}
		
		return rp;
	}

	@Override
	public void reject(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap,
			String sabun, String userId) throws Exception {
		if(paramMap == null || !paramMap.containsKey("apprOpinion") && paramMap.get("apprOpinion").equals("")) {
			throw new Exception("사유를 입력하세요.");
		}
		String apprOpinion = paramMap.get("apprOpinion").toString();
		
		List<WtmApplLine> lines = wtmApplLineRepo.findByApplIdOrderByApprSeqAsc(applId);
		if(lines != null && lines.size() > 0) {
			for(WtmApplLine line : lines) {
				if(line.getApprSeq() <= apprSeq) {
					line.setApprStatusCd(APPR_STATUS_REJECT);
					line.setApprDate("");
					if(line.getApprSeq() == apprSeq) {
						line.setApprOpinion(apprOpinion);
					}
				}else {
					line.setApprStatusCd("");
				}
				line.setUpdateId(userId);
				wtmApplLineRepo.save(line);
			}
		}
		
		WtmAppl appl = wtmApplRepo.findById(applId).get();
		appl.setApplStatusCd(APPL_STATUS_APPLY_REJECT);
		appl.setApplYmd(WtmUtil.parseDateStr(new Date(), null));
		appl.setUpdateId(userId);	
		wtmApplRepo.save(appl);
		
	}

	@Override
	public ReturnParam imsi(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap,
			String status, String sabun, String userId) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
		//Long flexibleStdMgrId = Long.parseLong(paramMap.get("flexibleStdMgrId").toString());
		//신청서 최상위 테이블이다. 
		WtmAppl appl = saveWtmAppl(tenantId, enterCd, applId, workTypeCd, status, sabun, userId);
		
		applId = appl.getApplId();
		 
		String workDayResultId = paramMap.get("workDayResultId").toString();
		String reason = paramMap.get("reason").toString();
		
		WtmWorkDayResult result = wtmWorkDayResultRepo.findById(Long.parseLong(workDayResultId)).get();
		
		WtmOtAppl otAppl = wtmOtApplRepo.findByApplId(result.getApplId());
		
//		
//		WtmWorkCalendar calendar = wtmWorkCalendarRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);
//		
//		String subYn = "";
//		if(paramMap.containsKey("subYn")) {
//			subYn = paramMap.get("subYn")+"";
//		}
		//근무제 신청서 테이블 조회
		WtmOtCanAppl otCanAppl = saveWtmOtCanAppl(tenantId, enterCd, applId, otAppl.getOtApplId(), result.getWorkDayResultId(), result.getYmd(), result.getTimeTypeCd(), result.getPlanSdate(), result.getPlanEdate(), result.getPlanMinute(), result.getApprSdate(), result.getApprEdate(), result.getApprMinute(), reason, sabun, userId);
				
		saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun, userId);
		paramMap.put("applId", appl.getApplId());
		//rp.put("flexibleApplId", flexibleAppl.getFlexibleApplId());
		rp.put("applId", appl.getApplId());
			
		return rp;
	}

	protected WtmOtCanAppl saveWtmOtCanAppl(Long tenantId, String enterCd, Long applId, Long otApplId, Long workDayResultId, String ymd, String timeTypeCd, Date planSdate, Date planEdate, Integer planMinute, Date apprSdate, Date apprEdate, Integer apprMinute,  String reason, String sabun, String userId) {
		 
		WtmOtCanAppl otAppl = wtmOtCanApplRepo.findByApplId(applId);
		if(otAppl == null) {
			otAppl = new WtmOtCanAppl();
		}
		otAppl.setApplId(applId);
		otAppl.setOtApplId(otApplId);
		otAppl.setTimeTypeCd(timeTypeCd);
		otAppl.setWorkDayResultId(workDayResultId);
		otAppl.setPlanSdate(planSdate);
		otAppl.setPlanEdate(planEdate);
		otAppl.setPlanMinute(planMinute);
		otAppl.setApprSdate(apprSdate);
		otAppl.setApprEdate(apprEdate);
		otAppl.setApprMinute(apprMinute);
		otAppl.setReason(reason);
		otAppl.setUpdateId(userId);
		
		return wtmOtCanApplRepo.save(otAppl);
	}
	protected WtmApplCode getApplInfo(Long tenantId,String enterCd,String applCd) {
		return wtmApplCodeRepo.findByTenantIdAndEnterCdAndApplCd(tenantId, enterCd, applCd);
	}
	
	@Override
	public ReturnParam preCheck(Long tenantId, String enterCd, String sabun, String workTypeCd,
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnParam validate(Long tenantId, String enterCd, String sabun, String workTypeCd,
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		return rp;
	}

	@Override
	public void sendPush() {
		// TODO Auto-generated method stub
		
	}
	
	protected WtmAppl saveWtmAppl(Long tenantId, String enterCd, Long applId, String workTypeCd, String applStatusCd, String sabun, String userId) {
		WtmAppl appl = null;
		if(applId != null && !applId.equals("")) {
			appl = wtmApplRepo.findById(applId).get();
		}else {
			appl = new WtmAppl();
		}
		appl.setApplStatusCd(applStatusCd);
		appl.setTenantId(tenantId);
		appl.setEnterCd(enterCd);
		appl.setApplInSabun(sabun);
		appl.setApplSabun(sabun);
		appl.setApplCd(workTypeCd);
		appl.setApplYmd(WtmUtil.parseDateStr(new Date(), null));
		appl.setUpdateId(userId); 
		//appl.
		
		return wtmApplRepo.save(appl);
	}

protected void saveWtmApplLine(Long tenantId, String enterCd, int apprLvl, Long applId, String sabun, String userId) {
		
		//결재라인 저장
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<WtmApplLine> applLines = wtmApplLineRepo.findByApplIdOrderByApprSeqAsc(applId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("tenantId", tenantId);
		paramMap.put("d", WtmUtil.parseDateStr(new Date(), null));
		//결재라인 조회 기본으로 3단계까지 가져와서 뽑아  쓰자
		List<WtmApplLineVO> applLineVOs = applMapper.getWtmApplLine(paramMap);
		//기본 결재라인이 없으면 저장도 안됨.
		if(applLineVOs != null && applLineVOs.size() > 0){

			//결재라인 코드는 1,2,3으로 되어있다 이렇게 써야한다!!!! 1:1단계, 2:2단계, 3:3단계
			int applCnt = apprLvl;
			
			//기 저장된 결재라인과 비교
			if(applLines != null && applLines.size() > 0) {
				int whileLoop = 0;
				
				for(WtmApplLine applLine : applLines) {
					
					WtmApplLineVO applLineVO = applLineVOs.get(whileLoop);
					if(whileLoop < applCnt) {
						applLine.setApplId(applId);
						applLine.setApprSeq(applLineVO.getApprSeq());
						applLine.setApprSabun(applLineVO.getSabun());
						applLine.setApprTypeCd(APPL_LINE_S);
						applLine.setUpdateId(userId);
						wtmApplLineRepo.save(applLine);
					}else {
						//기존 결재라인이 더 많으면 지운다. 임시저장이니.. 바뀔수도 있을 것 같아서..
						wtmApplLineRepo.delete(applLine);
					}
					whileLoop++;
				} 
			}else {
				//신규생성
				int lineCnt = 0; 
				for(WtmApplLineVO applLineVO : applLineVOs) {
					if(lineCnt < applCnt) {
						WtmApplLine applLine = new WtmApplLine();
						applLine.setApplId(applId);
						applLine.setApprSeq(applLineVO.getApprSeq());
						applLine.setApprSabun(applLineVO.getSabun());
						applLine.setApprTypeCd(APPL_LINE_S);
						applLine.setUpdateId(userId);
						wtmApplLineRepo.save(applLine);
					}
					lineCnt++;
				}
			}
		}
		//결재라인 저장 끝
	}

	@Override
	public void delete(Long applId) {
		wtmOtSubsCanApplRepo.deleteByApplId(applId);
		wtmOtCanApplRepo.deleteByApplId(applId);
		wtmApplLineRepo.deleteByApplId(applId);
		wtmApplRepo.deleteById(applId);
	}
	

}
