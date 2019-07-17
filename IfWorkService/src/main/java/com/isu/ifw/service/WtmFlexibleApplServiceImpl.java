package com.isu.ifw.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isu.ifw.entity.WtmAppl;
import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.entity.WtmApplLine;
import com.isu.ifw.entity.WtmFlexibleAppl;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleApplMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmApplLineRepository;
import com.isu.ifw.repository.WtmApplRepository;
import com.isu.ifw.repository.WtmFlexibleApplRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmApplLineVO;
import com.isu.ifw.vo.WtmFlexibleApplVO;

@Service("wtmFlexibleApplService")
public class WtmFlexibleApplServiceImpl implements WtmApplService {


	@Autowired
	WtmApplMapper applMapper;
	
	@Autowired
	WtmFlexibleApplMapper flexApplMapper;

	@Autowired
	WtmApplRepository wtmApplRepo;
	

	@Autowired
	WtmFlexibleApplRepository wtmFlexibleApplRepo;
	
	@Autowired
	WtmApplLineRepository wtmApplLineRepo;
	
	@Autowired
	WtmApplCodeRepository wtmApplCodeRepo;
	
	@Override
	public WtmFlexibleApplVO getFlexibleAppl(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("empNo", empNo);
		
		return flexApplMapper.getWtmFlexibleAppl(paramMap);
	}
	
	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}
	
	protected WtmApplCode getApplInfo(Long tenantId,String enterCd,String applCd) {
		return wtmApplCodeRepo.findByTenantIdAndEnterCdAndApplCd(tenantId, enterCd, applCd);
	}
	@Transactional
	@Override
	public void imsi(Long tenantId, String enterCd, Long applId, Long flexibleStdMgrId, String workTypeCd, Map<String, Object> paramMap, String userId) {
		WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
		WtmAppl appl = null;
		if(applId != null && !applId.equals("")) {
			appl = wtmApplRepo.findById(applId).get();
		}else {
			appl = new WtmAppl();
		}
		appl.setApplStatusCd(this.APPL_STATUS_IMSI);
		appl.setTenantId(tenantId);
		appl.setEnterCd(enterCd);
		appl.setApplInSabun(userId);
		appl.setApplSabun(userId);
		appl.setApplCd(workTypeCd);
		appl.setApplYmd(WtmUtil.parseDateStr(new Date(), null));
		appl.setUpdateId(userId);
		//appl.
		
		appl = wtmApplRepo.save(appl);
		
		applId = appl.getApplId();
		
		String sYmd = paramMap.get("sYmd").toString();
		String eYmd = paramMap.get("eYmd").toString();
		String ym = sYmd.substring(0, 6);
		
		WtmFlexibleAppl flexibleAppl = wtmFlexibleApplRepo.findByApplId(applId);
		if(flexibleAppl == null) {
			flexibleAppl = new WtmFlexibleAppl();
		}
		flexibleAppl.setFlexibleStdMgrId(flexibleStdMgrId);
		flexibleAppl.setApplId(applId);
		flexibleAppl.setYm(ym);
		flexibleAppl.setSymd(sYmd);
		flexibleAppl.setEymd(eYmd);
		flexibleAppl.setUpdateId(userId);
		//flexibleAppl.setSabun(userId);
		flexibleAppl.setWorkDay("0");
		if(sYmd != null && !sYmd.equals("") && eYmd != null && !eYmd.equals("")) {
			Map<String, Object> m = applMapper.calcWorkDay(paramMap);
			if(m != null) {
				flexibleAppl.setWorkDay(m.get("WORK_CNT").toString());
			}
		}
		
		
		flexibleAppl = wtmFlexibleApplRepo.save(flexibleAppl);
		
		System.out.println("applId : " + appl.getApplId());
		
		//결재라인 저장
		List<WtmApplLine> applLines = wtmApplLineRepo.findByApplIdOrderByApprSeqAsc(applId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", userId);
		paramMap.put("tenantId", tenantId);
		paramMap.put("d", WtmUtil.parseDateStr(new Date(), null));
		//결재라인 조회 기본으로 3단계까지 가져와서 뽑아  쓰자
		List<WtmApplLineVO> applLineVOs = applMapper.getWtmApplLine(paramMap);
		//기본 결재라인이 없으면 저장도 안됨.
		if(applLineVOs != null && applLineVOs.size() > 0){

			//결재라인 코드는 1,2,3으로 되어있다 이렇게 써야한다!!!! 1:1단계, 2:2단계, 3:3단계
			int applCnt = Integer.parseInt(applCode.getApplLevelCd());
			
			//기 저장된 결재라인과 비교
			if(applLines != null && applLines.size() > 0) {
				int whileLoop = 0;
				
				for(WtmApplLine applLine : applLines) {
					
					WtmApplLineVO applLineVO = applLineVOs.get(whileLoop);
					if(whileLoop < applCnt) {
						applLine.setApplId(applId);
						applLine.setApprSeq(applLineVO.getApprSeq() + "");
						applLine.setApprSabun(applLineVO.getSabun());
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
						applLine.setApprSeq(applLineVO.getApprSeq()+"");
						applLine.setApprSabun(applLineVO.getSabun());
						applLine.setUpdateId(userId);
						wtmApplLineRepo.save(applLine);
					}
					lineCnt++;
				}
			}
		}
		//결재라인 저장 끝
		
		
	}
 
	
}
