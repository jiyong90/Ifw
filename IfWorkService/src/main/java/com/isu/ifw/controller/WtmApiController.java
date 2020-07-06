package com.isu.ifw.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibleaders.ibsheet7.ibsheet.excel.Down2Excel;
import com.ibleaders.ibsheet7.util.Synchronizer;
import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.mapper.WtmInoutHisMapper;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.service.WtmInterfaceService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/api")
public class WtmApiController{

	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Resource
	WtmEmpHisRepository empRepository;
	
	@Autowired
	WtmInoutService inoutService;

	@Autowired
	@Qualifier("WtmTenantModuleRepository")
	CommTenantModuleRepository tenantModuleRepo;
	
	@Autowired
	WtmInoutHisMapper inoutHisMapper;


	@Autowired
	private WtmInterfaceService wtmInterfaceService;

	
	@RequestMapping(value = "/{tsId}/d/{gubun}",method = RequestMethod.POST)
	public void postCode(@PathVariable String tsId,@PathVariable String gubun, @RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CommTenantModule tm = null;
	    tm = tenantModuleRepo.findByTenantKey(tsId);
        Long tenantId = tm.getTenantId();
        
		// 공통코드
		System.out.println("postCode start");
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) paramMap.get("data");
		if(gubun.equalsIgnoreCase("CODE")) {
			wtmInterfaceService.saveCodeIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("EMP")) {
			wtmInterfaceService.saveEmpIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("EMPADDR")) {
			wtmInterfaceService.saveEmpAddrIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("GNT")) {
			wtmInterfaceService.saveGntIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("HOLIDAY")) {
			wtmInterfaceService.saveHolidayIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("ORG")) {
			wtmInterfaceService.saveOrgIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("ORGCONC")) {
			wtmInterfaceService.saveOrgConcIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("TAAAPPL")) {
			wtmInterfaceService.saveTaaApplIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("ORGCHART")) {
			wtmInterfaceService.saveOrgChartIntf(tenantId, dataList);
		}
		System.out.println("postCode end");
		
		return;
	}
	
	
	//출퇴근 상태 정보
	@RequestMapping(value = "/{tsId}/worktime/status", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyWorkStatus(
			@PathVariable String tsId, 
			@RequestParam(value="enterCd", required = true) String enterCd, 
			@RequestParam(value="sabun", required = true) String sabun, 
			HttpServletRequest request) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map <String,Object> resultMap = new HashMap<String,Object>();

		try {
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			Long tenantId = tm.getTenantId();
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
		}catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		rp.put("result", resultMap);
		logger.debug("/api/workstatus e " + rp.toString());
		return rp;
	}
	
	//출근
	@RequestMapping (value="/{tsId}/worktime/in", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestIn(@PathVariable String tsId, 
			@RequestBody Map<String,Object> params, HttpServletRequest request)throws Exception{
	
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("출근 체크 되었습니다.");
		Long tenantId=null;
		String enterCd=null;
		String sabun=null;

		try {
			logger.debug(tsId + "/api/in s " + params.toString());

			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			enterCd = (String)params.get("enterCd");
			sabun = (String)params.get("sabun");
	
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
			Date now = new Date();
			String today = format1.format(now);
			
			Map<String, Object> paramMap = new HashMap();
		
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "IN");
			paramMap.put("ymd", ymd);
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "API");
			
			logger.debug("getParameter in " + paramMap.toString());
			inoutService.updateTimecard2(paramMap);
			logger.debug("getParameter in2" + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());
		} catch(Exception e) {
			logger.debug("inexception : " + e.getMessage());
			rp.setFail(e.getMessage());
		} finally {
			Map<String, Object> resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
			rp.put("result", resultMap);
		}

		logger.debug(tsId + "/api/in e " + rp.toString());
		return rp;
	}
	
	//퇴근
	@RequestMapping (value="/{tsId}/worktime/out", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestOut(@PathVariable String tsId, 
			@RequestBody Map<String,Object> params, HttpServletRequest request)throws Exception{
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 체크 되었습니다.");
		
		Map<String, Object> paramMap = new HashMap();
		Long tenantId=null;
		String enterCd=null;
		String sabun=null;
		try {
			logger.debug(tsId + "/api/out s " + params.toString());

			
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			enterCd = (String)params.get("enterCd");
			sabun = (String)params.get("sabun");
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
			Date now = new Date();
			String today = format1.format(now);

			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "OUT");
			paramMap.put("ymd", ymd);
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "API");
			
			logger.debug("getParameter out " + paramMap.toString());
			inoutService.updateTimecard2(paramMap);
			logger.debug("getParameter out2 " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());
			
		} catch(Exception e) {
			logger.debug("outexception : " + e.getMessage() + paramMap.toString());
			rp.setFail(e.getMessage());
		} finally {
			Map<String, Object> resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
			rp.put("result", resultMap);
		}
		logger.debug(tsId + "/api/out e " + rp.toString());
		return rp;
	}
	
	//퇴근취소
	@RequestMapping(value = "/{tsId}/worktime/cancel", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam cancelOutRequest(@PathVariable String tsId,
			@RequestBody Map<String,Object> params, HttpServletRequest request) throws Exception {		
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 정보가 취소되었습니다.");

		Map<String, Object> paramMap = new HashMap();
		Long tenantId=null;
		String enterCd=null;
		String sabun=null;
		
		try {
			logger.debug(tsId + "/api/cancel s " + params.toString());
 
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			enterCd = (String)params.get("enterCd");
			sabun = (String)params.get("sabun");
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
			Date now = new Date();
			String today = format1.format(now);
			
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "OUTC");
			paramMap.put("ymd", ymd);
			paramMap.put("stdYmd", ymd);
			paramMap.put("inoutDate", null);
			paramMap.put("entryType", "API");
			
			logger.debug("getParameter cancel " + paramMap.toString());
			inoutService.updateTimecardCancel(paramMap);
			logger.debug("getParameter cancel2 " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());
		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		} finally {
			Map<String, Object> resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
			rp.put("result", resultMap);
		}
		
		logger.debug(tsId + "/api/cancel e " + rp.toString());

		return rp;
	}
	
	//외출복귀
	@RequestMapping(value = "/{tsId}/worktime/except", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam exceptRequest(@PathVariable String tsId,
			@RequestBody Map<String,Object> params, HttpServletRequest request) throws Exception {		
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("체크에 성공하였습니다.");

		Map<String, Object> paramMap = new HashMap();
		Long tenantId=null;
		String enterCd=null;
		String sabun=null;
		
		try {
			logger.debug(tsId + "/api/cancel s " + params.toString());
 
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			enterCd = (String)params.get("enterCd");
			sabun = (String)params.get("sabun");
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
			Date now = new Date();
			String today = format1.format(now);
			
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "EXCEPT");
			paramMap.put("ymd", ymd);
			paramMap.put("stdYmd", ymd);
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "API");
			
			logger.debug("getParameter except " + paramMap.toString());
			inoutService.updateTimecardExcept(paramMap);
			logger.debug("getParameter except2 " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());

		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		} finally {
			Map<String, Object> resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
			rp.put("result", resultMap);
		}
		
		logger.debug(tsId + "/api/cancel e " + rp.toString());

		return rp;
	}

	@RequestMapping(value = "/{tsId}/emergency", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam emergencyInout(@PathVariable String tsId,
			@RequestBody Map<String,Object> params, HttpServletRequest request) throws Exception {		
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		try {
			logger.debug("getParameter emergency " + params.toString());

			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm != null && params != null && params.containsKey("data")) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("data");
				for(Map<String, Object> data : list) {
					SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
					String inoutDate = format1.format(data.get("inoutDate"));
					
					data.put("tenantId", tm.getTenantId());
					data.put("entryType", "AWS");
					data.put("inoutDate", inoutDate);
					inoutService.updateTimecard2(data);
				}
			}
			
//			inoutService.updateTimecard2(params);
			logger.debug("getParameter emergency ");

		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		} 
		
		logger.debug(tsId + "/api/cancel e " + rp.toString());

		return rp;
	}
	


	@RequestMapping(value = "/Down2Excel", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void Down2Excel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//====================================================================================================
		// [ 사용자 환경 설정 #0 ]
		//====================================================================================================
		// 대용량 엑셀파일 다운로드를 하는 경우 메모리가 부족한 상황이 생긴다면
		// Down2Excel ibExcel = new Down2Excel(true); //형식으로 사용해 주세요.
		// 메모리를 절약해서 엑셀파일을 생성합니다만 reportXML 사용이 제한됩니다.
		// 메모리 부족 사유가 없다면 옵션을 주지 않는 것이 좋습니다.
		//====================================================================================================
	    Down2Excel ibExcel = new Down2Excel();


		ibExcel.setService(request, response);

		//System.out.println(com.ibleaders.ibsheet7.util.Version.getVersion());

		//====================================================================================================
		// [ 사용자 환경 설정 #1 ]
		//====================================================================================================
		// Html 페이지의 인코딩이 utf-8 로 구성되어 있으면 "ibExcel.setPageEncoding("utf-8");" 로 설정하십시오.
		// 엑셀 문서의 한글이 깨지면 이 값을 공백("")으로 바꿔 보십시오. (설정하지 않거나 공백으로 설정 시 EUC-KR로 처리됩니다.)
		// LoadExcel.jsp 도 동일한 값으로 바꿔 주십시오.
		//====================================================================================================
		ibExcel.setPageEncoding("utf-8");


		//====================================================================================================
		// [ 사용자 환경 설정 #2 ]
		//====================================================================================================
		// 엑셀에 포함될 이미지의 URL 에 가상폴더를 사용할 경우가 조금이라도 있다면 웹루트를 아래 변수에 직접 지정해 주십시오.
		// 엑셀에 포함될 이미지에 가상폴더를 사용하지 않으면 설정하지 마세요.
		//====================================================================================================
		String webRootPath = "/soldev/apps";
		ibExcel.setWebRoot(webRootPath);

		//====================================================================================================
		// [ 사용자 환경 설정 #3 ]
		//====================================================================================================
		// 트리 컬럼에서 레벨별로 … 를 덧붙여서 레벨별로 보기 좋게 만듭니다.
		// 만약 … 대신 다른 문자를 사용하기를 원하시면 아래 유니코드 \u2026 (16진수형태) 대신 다른 문자를 입력하십시오.
		// 트리 컬럼이 없으면 설정하지 마세요.
		//====================================================================================================
		ibExcel.setTreeChar("\u2026");

		//====================================================================================================
		// [ 사용자 환경 설정 #4 ]
		//====================================================================================================
		// 기본 폰트 이름과 폰트 크기를 설정합니다.
		// SheetDesign : 0,3 을 사용하는 경우에만 적용됩니다.
		//====================================================================================================
		ibExcel.setDefaultFontName("맑은고딕");
		ibExcel.setDefaultFontSize((short)10);

		//====================================================================================================
		// [ 사용자 환경 설정 #5 ]
		//====================================================================================================
		// IBSheet의 폰트 이름, 폰트 크기를 사용하지 않고 다음에서 설정한 값으로 강제적으로 적용합니다.
		// SheetDesign : 1, 2 를 사용하는 경우에만 적용됩니다.
		// 사용하지 않으시려면 주석처리 하세요.
		//
		//ibExcel.setFontName("궁서");
		//ibExcel.setFontSize((short)15);

		//====================================================================================================
		// [ 사용자 환경 설정 #6 ]
		//====================================================================================================
		// 줄바꿈 설정을 다음에서 설정한 값으로 강제적으로 적용합니다.
		// 사용하지 않으시려면 주석처리 하세요.
		//
		//ibExcel.setWordWrap(false);

		//====================================================================================================
		// [ 사용자 환경 설정 #7 ]
		//====================================================================================================
		// 엑셀에 포함될 이미지의 URL 이 다른 도메인에 있고 함께 다운로드 받으려면 다음을 설정합니다.
		// 기본값은 false 이며 다른 도메인에 존재하는 이미지는 다운로드 받지 않습니다.
		//ibExcel.setAllowDownloadRemoteImg(true);

		//====================================================================================================
		// [ 사용자 환경 설정 #8 ]
		//====================================================================================================
		// 엑셀에 포함될 이미지의 URL 이 같은 도메인에 있지만 "/image/imgDown.jsp?idx=365" 등과 같은
		// 이미지 로딩 방식을 사용한다면 웹서버 도메인을 설정하세요.
		//ibExcel.setWebServerDomain("http://www.ibleaders.co.kr");

		//====================================================================================================
		// [ 사용자 환경 설정 #9 ]
		//====================================================================================================
		// 엑셀 다운로드 시 서버에 위치한 디자인 파일을 사용하는 경우 디자인 파일이 있는 폴더 위치를 설정하세요.
		// 디자인 파일을 사용하지 않는 경우 주석처리하세요.
		//====================================================================================================
		//String tempRoot = "D:/SVN/src/IBSheet7.TestPage";
		//ibExcel.setTempRoot(tempRoot);

		//====================================================================================================
		// [ 사용자 환경 설정 #10 ]
		//====================================================================================================
		// 엑셀 다운로드 시 헤더행의 글자색을 적용하고 싶은 경우에 설정하세요.
		// #3366FF 형태의 웹 컬러로 설정해주세요.
		// 설정을 원하지 않는 경우 주석처리해주세요.
		//====================================================================================================
		//ibExcel.setHeaderFontColor("#FF2233");

	    //====================================================================================================
	    // [ 사용자 환경 설정 #11 ]
	    //====================================================================================================
	    // 엑셀 다운로드 시 헤더행의 배경색을 적용하고 싶은 경우에 설정하세요.
	    // #3366FF 형태의 웹 컬러로 설정해주세요.
	    // 설정을 원하지 않는 경우 주석처리해주세요.
	    //====================================================================================================
	    //ibExcel.setHeaderBackColor("#4466aa");

	    //====================================================================================================
	    // [ 사용자 환경 설정 #12 ]
	    //====================================================================================================
	    // 엑셀 전문의 MarkupTag Delimiter 사용자 정의 시 설정하세요.
	    // 설정 값은 IBSheet7 환경설정(ibsheet.cfg)의 MarkupTagDelimiter 설정 값과 동일해야 합니다.
	    //====================================================================================================
	    //IBPacketParser.setMarkupTagDelimiter("[s1]","[s2]","[s3]","[s4]");

	    //====================================================================================================
	    // [ 사용자 환경 설정 #13 ]
	    //====================================================================================================
	    // 엑셀 다운로드 시 헤더행의 폰트 Bold 스타일을 적용하고 싶은 경우에 설정하세요.
	    // 설정을 원하지 않는 경우 주석처리해주세요.
	    //====================================================================================================
	    //ibExcel.setHeaderFontBold(true);

	    //====================================================================================================
	    // [ 사용자 환경 설정 #14 ]
	    //====================================================================================================
	    // 엑셀 다운로드 시 포함된 이미지의 비율을 맞추고 싶을때 설정하세요..
	    // 설정을 원하지 않는 경우 주석처리해주세요.
	    // 0 : 셀의 가로/세로에 꽉 차게 이미지를 처리합니다. (기본값)
	    // 1 : 셀의 중앙에 이미지를 원본 크기로 표시합니다. (xls 형식에서는 적용되지 않습니다.)
	    // 2 : 이미지의 원본 가로/세로 비율을 유지하면서 셀에 맞춥니다.
	    // 정상적인 이미지 처리를 위해서는 시트 옵션에서 [Merge : 2] 로 설정을 해야 합니다.
	    //====================================================================================================
	    //ibExcel.setImageProcessType(0);

	    //====================================================================================================
	    // [ 사용자 환경 설정 #15 ]
	    //====================================================================================================
	    // 시트에 포함될 문자열 중 STX(\u0002), ETX(\u0003) 이 포함된 경우에만 설정해주세요.
	    // 설정을 원하지 않는 경우 주석처리해주세요.
	    // 0 : 시트 구분자로 STX, ETX 문자를 사용합니다. (기본값)
	    // 1 : 시트 구분자로 변형된 문자열을 사용합니다. (시트에 설정이 되어 있어야 합니다.)
	    //====================================================================================================
		//ibExcel.setDelimMode(1);

	    //====================================================================================================
	    // [ 사용자 환경 설정 #16 ]
	    //====================================================================================================
	    // 엑셀 다운로드 시 저장되는 임시 파일을 삭제하고 싶은 경우에 설정하세요.
	    // 설정을 원하지 않는 경우 주석처리해주세요.
	    //====================================================================================================
		//ibExcel.setDeleteTempFile(false);

	    boolean bToken = false;

	    try {
			response.reset();

	        // 서버에서 병행처리를 허용할 최대 동시 작업 갯수를 설정한다.
	        Synchronizer.init(5);

	        // 싱크 처리 객체로 부터 처리권한을 확인한다.
	        // 인자를 true로 설정하는 경우 : 싱크 처리 객체에서 자원을 사용가능해질때까지 최대 30초 동안 기다렸다가 자원 사용이 가능해졌을때 권한을 할당 후 true를 반환한다.
	        // 인자를 false로 설정하는 경우 : 자원 사용여부를 확인후 즉시 반환. 사용 가능하면 할당 후 true를 반환하고, 사용이 불가능한 경우 false를 반환한다.
	        bToken = Synchronizer.use(false);
//	         bToken = false;

	        // 싱크 객체로 부터 권한을 정상 할당 받은 경우에만 엑셀 작업을 진행한다.
	        if (bToken) {

	           // 파라메터 정보를 얻음
	            String data = ibExcel.getData();

	            // 파라메터 정보를 다시 설정함 (예, 암호화된 파라메터를 복호화 처리를 하여 다시 설정)
	            ibExcel.setData(data);

	            // ExtendParam 사용 가능
	            //String exParam = ibExcel.getExtendParam();

	            // 엑셀 워크북을 생성
	            Workbook workbook = ibExcel.makeExcel();

	            //IllegalStateException 예방 코드를 response.getOutputStream(); 호출 전 시점으로 이동
				//out.clear();
				//out = pageContext.pushBody();

	            // 다운로드 1. 생성된 엑셀 문서를 바로 다운로드 받음
	            ServletOutputStream out2 = response.getOutputStream();

	            //POI 3.10 엑셀 암호 기능 구현 버전 사용 시, 아래 "workbook.write(out2);"을 하단 주석처리된 코드로 대체한다.
	            workbook.write(out2);
	            //com.ibleaders.ibsheet7.ibsheet.excel.ProtectXLSX.encryptXLSX("d:/", ibExcel.getWorkbookPassword(), workbook, out2);

	             String userAgent = request.getHeader("User-Agent");
	             
	            System.out.println("userAgent : " + userAgent);
	             
	            out2.flush();
	            out2.close();
				/*
	            // 다운로드 2. 생성된 엑셀 문서를 서버에 저장
	            System.out.println("다운로드 2. 생성된 엑셀 문서를 서버에 저장");
	            // 다운로드 받을 파일 이름을 얻음
	            String fileName = ibExcel.getDownloadFileName();
	            FileOutputStream out2 = new FileOutputStream (webRootPath + "/" + fileName);
	            workbook.write(out2);
	            out2.close();

	            // 생성된 엑셀 문서를 다운로드 받음 (예, 엑셀문서를 DRM 처리함)
	            
	            File file = new File( webRootPath + "/" + fileName );
	            int fileLength = (int)file.length();

	            response.setContentLength(fileLength);

	            try {
	                if (file.isFile()) { 
	                    FileInputStream fileIn = new FileInputStream(file);
	                    ServletOutputStream out3 = response.getOutputStream();

	                    byte[] outputByte = new byte[fileLength];

	                    while (fileIn.read(outputByte, 0, fileLength) != -1) {
	                        out3.write(outputByte, 0, fileLength);

	                        System.out.println("outputByte : " + outputByte);
	                        
	                    }
	                    System.out.println("fileIn.close();");
	                    fileIn.close();
	                    System.out.println("out3.flush();");
	                    out3.flush();
	                    System.out.println("out3.close();");
	                    out3.close();
	                }
	            } finally {
	                file.delete();
	            }
	            */

	            // 엑셀 다운 완료 후 싱크 객체로 할당받은 권한을 반환한다.
	            Synchronizer.release();
	            bToken = false;
	        }
	        else {
//	             response.setHeader("Content-Type", "text/html;charset=utf-8");
		        response.setContentType("text/html;charset=utf-8");
		        response.setCharacterEncoding("utf-8");
		        response.setHeader("Content-Disposition", "");

	            //out.println("<script>alert('엑셀 다운로드중 에러가 발생하였습니다.[Server Busy]'); </script>");
	        }

		} catch (Exception e) {
	        //Exception 발생 시, response 헤더 별도 설정하도록 한다.
	        response.setContentType("text/html;charset=utf-8");
	        response.setCharacterEncoding("utf-8");
	        response.setHeader("Content-Disposition", "");

	        //out.println("<script>alert('엑셀 다운로드중 에러가 발생하였습니다.');</script>");
	        //out.flush();

			e.printStackTrace();
		} catch (Error e) {
	        //Exception 발생 시, response 헤더 별도 설정하도록 한다.
	        response.setContentType("text/html;charset=utf-8");
	        response.setCharacterEncoding("utf-8");
	        response.setHeader("Content-Disposition", "");

//			out.println("<script>alert('엑셀 다운로드중 에러가 발생하였습니다.');</script>");
//	        out.flush();

			e.printStackTrace();
		} finally {
		    //공유자원 반환이 되지 않은 상태라면, 반환 처리한다.
		    if (bToken) {
	            Synchronizer.release();
	            bToken = false;
		    }

	        ibExcel.setDownFinish();
	    }

	    // 파일 정상 다운로드시 아래 구문을 실행하지 않으면 서버 Servlet에서  java.lang.IllegalStateException 이 발생한다.
	    // 파일 최 하단에서 호출하도록 하면 다운로드 에러로 인한 Exception 메시지가 출력되지 않으므로 정상 다운시에만 처리하도록 한다.
//	     out.flush();
//	     out = pageContext.pushBody();
	}
}
