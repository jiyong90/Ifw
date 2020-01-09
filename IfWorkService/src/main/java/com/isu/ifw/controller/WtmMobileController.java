package com.isu.ifw.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.entity.WtmMobileToken;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmMobileTokenRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.service.LoginService;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.service.WtmMobileService;
import com.isu.ifw.util.Aes256;
import com.isu.ifw.util.MobileUtil;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


@RestController
public class WtmMobileController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Autowired
	WtmMobileService mobileService;
	
	@Autowired
	LoginService loginService;

	@Autowired
	@Qualifier("wtmOtApplService")
	WtmApplService otApplService;

	@Resource
	WtmApplCodeRepository applCodeRepository;
	
	@Resource
	WtmMobileTokenRepository tokenRepository;

	@Autowired
	WtmApplMapper applMapper;

	@Resource
	WtmEmpHisRepository empRepository;

	@Autowired
	WtmWorkCalendarRepository workCalendarRepo;

	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Autowired
	TenantConfigManagerService tcms;

	@Autowired
	@Qualifier(value="flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;

	
	/**
	 * dashboard
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/dashboard", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyDashboard(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, HttpServletRequest request) throws Exception {


		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/{tenantId}/dashboard " + empKey);
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
			Date now = new Date();
			String today = format1.format(now);
	
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("ymd", today);
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			Map<String, Object> statusMap = flexEmpMapper.getFlexibleRangeInfoMobile(paramMap);
	
			Map dashboards = new HashMap();
			Map status = new HashMap();
			Map data = new HashMap();
	
	
			data.put("param1", statusMap.get("param1") == null? "-" : statusMap.get("param1").toString());
//			String date = "-";
//			if(statusMap.get("param2") != null && statusMap.get("param3") != null) {
//				date += statusMap.get("param2").toString() + "\n" + statusMap.get("param3").toString();
//			}
			data.put("param2", statusMap.get("param2") == null? "-" : statusMap.get("param2").toString());
			data.put("param3", statusMap.get("param3") == null? "-" : statusMap.get("param3").toString());
			status.put("part1", data);
	
			data = new HashMap();
			data.put("param4", statusMap.get("param4") == null? "-" : statusMap.get("param4").toString());
			data.put("param5", statusMap.get("param5") == null? "-" : statusMap.get("param5").toString());
			data.put("param6", statusMap.get("param6") == null? "-" : statusMap.get("param6").toString());
			status.put("part3", data);
	
			String adapter = tcms.getConfigValue(tenantId, "HR.ADAPTER_URL", true, "");
			Map restCnt = mobileService.getDataMap(adapter+"/GetDataMap.do", "getMyRestCnt", enterCd, sabun);
			data = new HashMap();
			data.put("param7", restCnt == null || restCnt.get("useCnt") == null? "-" : restCnt.get("useCnt").toString());
			data.put("param8", restCnt == null || restCnt.get("usedCnt") == null? "-" : restCnt.get("usedCnt").toString());
			data.put("param9", restCnt == null || restCnt.get("restCnt") == null? "-" : restCnt.get("restCnt").toString());
			status.put("part2", data);
			
			dashboards.put("DASHBOARD1", status);
	
			rp.put("result", dashboards);
		} catch(Exception e) {
			rp.put("result", null);
			logger.debug(e.getMessage());
		}
		return rp;
	}


	//로그인
	@RequestMapping(value = "/mobile/{tenantId}/certificate", method = RequestMethod.POST)
	public @ResponseBody ReturnParam certificate(@PathVariable Long tenantId, 
			@RequestBody(required = true) Map<String, Object> params,
			HttpServletRequest request) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			String userToken = params.get("userToken").toString();
			System.out.println(params.toString());
			String enterCd = params.get("loginEnterCd").toString();
			String sabun = params.get("loginUserId").toString();
			String password = params.get("loginPassword").toString();
			String tenantKey = params.get("tenantKey").toString();
			String empKey = enterCd + "@" + sabun;
			
			empKey = MobileUtil.encEmpKey(userToken, empKey);
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보가 존재하지 않습니다.");
				return rp;
			}
			
			UUID token = UUID.randomUUID();
				
			Map<String, Object> userData = loginService.getUserData(tenantId, enterCd, sabun, password);
			
			WtmMobileToken mobileToken = tokenRepository.findByTenantIdAndEmpKey(tenantId, enterCd+"@"+sabun);
			if(mobileToken == null) {
				mobileToken = new WtmMobileToken();
				mobileToken.setEmpKey(enterCd+"@"+sabun);
				mobileToken.setTenantId(tenantId);
			} 
			mobileToken.setToken(token.toString());
			mobileToken = tokenRepository.save(mobileToken);
			
			Map<String, Object> sessionData = new HashMap();
			sessionData.put("orgNm", emp.getOrgCd());
			sessionData.put("authCode", "");
			sessionData.put("empNm", emp.getEmpNm());
			sessionData.put("id", enterCd+sabun);
			sessionData.put("accessToken", token.toString());
			
			Map<String, Object> result = new HashMap();
			result.put("sessionData", sessionData);
			
			
			logger.debug("111111111111111111111111111111111111100 " + empKey);
//			empKey = empKey.replace("+", "%2B"); 

			result.put("empKey", empKey);
			logger.debug("111111111111111111111111111111111111101 " + empKey);
			rp.put("result", result);
		} catch(Exception e) {
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	//인증
	@RequestMapping(value = "/mobile/{tenantId}/validate", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam validate(@PathVariable Long tenantId, 
					HttpServletRequest request,
					@RequestParam(value = "locale", required = true) String locale,
					@RequestParam(value = "empKey", required = true) String empKey,
					@RequestParam(value = "userToken", required = true) String userToken,
					@RequestParam(value = "accessToken", required = true) String accessToken) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			String enterCd = MobileUtil.parseEmpKey(empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(empKey, "sabun");
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보가 존재하지 않습니다.");
				return rp;
			}
			WtmMobileToken mobileToken = tokenRepository.findByTenantIdAndEmpKey(tenantId, enterCd+"@"+sabun);
			if(mobileToken == null) {
				rp.setFail("로그인 정보가 존재하지 않습니다.");
				return rp;
			} 
			logger.debug("1111111111 " + mobileToken.getToken() + ", " + accessToken);
			if(!mobileToken.getToken().equals(accessToken)) {
				rp.setFail("사용자 인증이 만료되었습니다.");
				return rp;
			}
			
//			Map<String, Object> userData = loginService.getUserData(tenantId, enterCd, sabun, password);
			
			Map<String, Object> sessionData = new HashMap();
			sessionData.put("orgNm", emp.getOrgCd());
			sessionData.put("authCode", "");
			sessionData.put("empNm", emp.getEmpNm());
			sessionData.put("id", enterCd+sabun);
			sessionData.put("accessToken", accessToken);
			
			Map<String, Object> result = new HashMap();
			result.put("sessionData", sessionData);
			result.put("empKey", enterCd+"@"+sabun);
			
			rp.put("result", result);
		} catch(Exception e) {
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	/**
	 * 내 근무 대쉬보드
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/my/status", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyworkStatus(@PathVariable Long tenantId,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey, 
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/{tenantId}/dashboard " + empKey);
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
			Date now = new Date();
			String today = format1.format(now);
	
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("ymd", today);
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			Map<String, Object> statusMap = flexEmpMapper.getFlexibleRangeInfoMobile(paramMap);
	
			List<Map<String, Object>> statusList = new ArrayList();
			
			if(statusMap != null) {
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBQjk2OEI5ODYyMzcxMUU4QjZDMEVDNEJDNDdBQzZDRCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBQjk2OEI5OTYyMzcxMUU4QjZDMEVDNEJDNDdBQzZDRCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkFCOTY4Qjk2NjIzNzExRThCNkMwRUM0QkM0N0FDNkNEIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkFCOTY4Qjk3NjIzNzExRThCNkMwRUM0QkM0N0FDNkNEIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Pe0TJgAABa9JREFUeNrsXWtsFFUUnuluWdpSuxqVpgg2QaP4JFg1xGgFNagh4uOH+CA+/vj6Qfxh9IchhBj9owYNNv1RFRITCQF8JKBFDdVoQkCDie8IglBKG419xNKWTnf9TnpHzt7MttuZnenc6fmSj3t7Z2f2zDdnzpx79k6w8/m8JQgfFSKBCC1CC0RoEVqEFkSCNP1j27YxBtdv6KpH0wFeYojJObDFRI++yyCR3ajxtIlCV5sYotOGh77d4EMxta0OPFoQow3GaPe6hr6YPksk65D0ToQWiNAitECEFqFFaIEILUILRGgRWoQWiNAitOAMTKxH8x84l9Rv6NocUztnmS70Faw/H3xEQkc4GJTQEQ3+YP0fwbdjamcV+LLJQnMc7l7XsDGOhuHZkeVCS9YhoaMk1MFzFgfYn7xubpFttMLoCOj4PHZtkoS+GTwoWUc46DPQ5lETPXo7eC94VYBjzNZChuPhgBVs24kgIoObbHpZyKTVpGXKCDrQNKs/25G53K5tX4DmEFiphh7DZwLNQAMLfdOyW+nKrwBXgheD5we48j3gt+COr/Z+/kNIIlNc38uGlkLEfR6fa0XzhJtGgpfic860CA2Rb0HzBnh5CJrsBNdC8M4ovTksr64I4Mlr0XwWksiWisMH8T1NZfbmZja0vthnIeoxNO+woRexv+9nWtqnyPej2ajd9lvBT8GToJ+3RKnadRG4GrxRjZ0L7sb3LSmTZ3Nh271Chgaa2T2uvHoh+DC42XfoaF5+2z3oPwlmStgnBV7PbimKX6sgxE/l8jwIuwZNm3Wm1PgX+HOQY45V1mZP18y7+v8TzzlDdt4ZmWy/XCqDiYedGhfLGZ7df3g//GgqjkTf0eoKTRUxPyvpB8DFEPlIueMGxH4UzbsJSXROuTHa7+sKr4UhssIW8LuECF3tFaMpDk1U893GwsaOsCzDBczDqynzuEYNfU0X1n8iW2EPzF16ZT6VqfJ7iMrBk53Vfb+UOnmpAd+b6GG4CyfZV+R2rmUiu/E5TPDa8yjs+jDY4fZ8EJULQ6tskPQupXndcMj2Dhtel0mG8SK0QIQWoUVoQTlQUq0Dqco5aM6ytNU3EcOGHfNV5tMdQcYTndA4MappbAKbYmDrDeAx1XdgG/3S8gwE/8fo0IETWYTmi5iIrOfw5CBU5WuHnWmjhQZeUNNIF/3gWMT2necxgeHlAXKCVaYLfS3rr8EtSlPK5RHbdyHrD4FZ2DHHKqyxNJkuNK/ouUX3P6cxXIxA5BHNHkKV6UILRGgRWiBCi9AzXuhTrL9AtY0R28fz9gwmJ+6v9BdoaZ/RU/AD4CLV34KTfNPS1vxirNSVnSRQJbuwOTX5mGyJ1SwtjevHdzraROqA6UK/At7HTqrO4zN1AfLjGh/7ZazCtSf7wY+NDh2YHPyKZpmHx0zXW1G8WncafB+8A3Y6pns0iU0iX4fb9WzlvUMY68Hf86zCX8OLYbW6Mwh04Wg1FJVbd7Lvpmn9ZGtDqM7yrzW+ppnuhp5ElUmZ4L1oetnfJa1twAXhYvyG/b5U44Ms7PRi/GiJ9nZahkLSu4QI7eh3D7zZtgqLRWMzQeiwi+a82rcCIh9X/TkszTsuHh0c7exBl1YTDT7Z2Fps+ZkIPQVARErD6N2W3z02fwI+JTG6fGLTAvLLwG/Y8KsYvxMcEKHLKzY9FP9mQyesGYayPQzxoHsQzbMTTGQaWf85taK/2OSE3sb6XoT2xlvW+EvspaBBsRjWg3dL6PBGNqbHSnQe/QDYrY29ZI2vNnK9f7u2nV6sfF5i9NSwT69fICbzh+EhbO/QtjfOhKwjx8ZGfR5LL2PqGCnS9xobSoC2XMecK3SLqku0wdP81ptfV8fYhmN0eWxvUxkFvWD0kcf2PSC9FErl0FbTVVY6tilNWmz5P2cTNGERiNAitAgtEKHjjP8EGAApHZTWqLpuNQAAAABJRU5ErkJggg=="
						,statusMap.get("param1") == null? "-" : statusMap.get("param1").toString(), "~" + statusMap.get("param3") == null? "-" : statusMap.get("param3").toString(),"", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCNDgyNzE2MTYyMzcxMUU4OEIxOTk0OEVFRTA0MEY4QiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCNDgyNzE2MjYyMzcxMUU4OEIxOTk0OEVFRTA0MEY4QiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkI0ODI3MTVGNjIzNzExRTg4QjE5OTQ4RUVFMDQwRjhCIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkI0ODI3MTYwNjIzNzExRTg4QjE5OTQ4RUVFMDQwRjhCIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+cuXoBwAAA9VJREFUeNrsnUtoE1EUhie2WhVNFBfWau2iQhERH+DCVzRSBYUiiCgo1CCIiBARK7qxalduCj4gUncKVZT6QNz4wEcXVhAVq9BCRSRorS98pSqKjf8hNziMbXpnMqlD5v/h595OZiaTL3fOnHM7SQKpVMqg8q9hREDQBE0RtGdVrLNSIBDI6UnCkepGNDG4ufXWjahXXjyO6ySaDfARHFddLvsaLKkYqhEdU2/qJry4MR6BLMdRq45rR6GEjmK7Z1GhHRNjNC+GBE0RtHcV0JnrWLJs+T6VOQx3+DwhU/+LZENeeO1w0PT3Z4f76YUb7ty83pRzHg3th4tceoFBjw66UA7bHYWb3AgdRTz5s2qEK5WhRbPU6e93yZn52NUS3KIEytVPfqeMynIcsw6mdwRNETRBUwRN0ARNEXRByEll2I6qqI/o7A1SJ6DLyZiho6BCRxj+SnTGWLg1n6CfcPaOs3eM0QRNETRBUwRN0ARNETRBUwRN0P7Wf/s8SWlDdwWa8TY3e9ZTX5bU2PcUNGfhSfBGbNPmS9AAsR7NGSN9j7IdJbDtdID7Ngjk23ClWiT3dbf5NXQscgBZNBWusAFZNMrPoUNu3J5tZP+3mHy6oMyy7DTcaQOyv2M0Tv0uNIuzjEy5sbvFAlog12LbVD/rT/QyZE9mHYA2Gs0VuKYfyL8H2CxmgXyfI1oPcsQGZFG7amW0b4dL4Hkc0e5CllAkqdxceCb6xxmj8wDZBPsRK0P7kAWufO4vqrIJVoYuqM4CWSQft9um+n2AfQntTozaBOc6nOuXxjGugZ8C+FKOaOdqVGFC7vx5qzIHyRqq4BXwDLWePH5RleA9BG2/ePmJJp4lhq9Fc0qV0nJ30G54F0OH+29Ei4Kb0WrG6PzpnKlfiVFeQtD6ad0ceJrmqH5nWRQkaD3IUTQP4U70F2isb57lk4tlkqD1NN+UL9dorL/O1O/CCP9O0HoyzylvkZQty2iWr604YFp0gRdDfTUbf7/zYwL8AEAPwVUmwJPhenTvwZkvJZR8+zBB66dsUphslvJaLZIceY+K2R/h1+i/hA/CIzOluJGeZHpD0PZgn0ezEn5leUiKklLLsm54Fba5zLkOZ7CvoZEUbyt810jP2pnVAe+Fpey+amPXvQP0fV2C/0BzQoyQIbG4XM1rPMdj7x3uVi6YUXVmHCPof6En1SjOdT8f0Cxk6PChCJqgCZoiaIImaCIgaIKmCJqgCZoaEjmZVAqFI9UkZ/M3AZyAfkHG+Qsd/ELBHPnogo4TdlbI8cFWCvDHfZl1EDRF0ATtd/0RYAD/ZwKJUzMmhwAAAABJRU5ErkJggg=="
						,"총 근무 가능",""+statusMap.get("param4") == null? "-" : statusMap.get("param4").toString(), "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDMzIyODFFOTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDMzIyODFFQTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkMzMjI4MUU3NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkMzMjI4MUU4NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8++6xboAAADFlJREFUeNrsXQuUVlUV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"
						,"잔여(기본)", ""+statusMap.get("param5") == null? "-" : statusMap.get("param5").toString(), "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCQ0E1NDQ5QjYyMzcxMUU4OTZEMURBMjUwQzQ1ODU3QyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCQ0E1NDQ5QzYyMzcxMUU4OTZEMURBMjUwQzQ1ODU3QyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkJDQTU0NDk5NjIzNzExRTg5NkQxREEyNTBDNDU4NTdDIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkJDQTU0NDlBNjIzNzExRTg5NkQxREEyNTBDNDU4NTdDIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+0KekLgAAB5JJREFUeNrsXQtsFFUU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"
						,"잔여(연장)", ""+statusMap.get("param6") == null? "-" : statusMap.get("param6").toString(), "", ""));
			}
			rp.put("result", statusList);
		} catch(Exception e) {
			rp.put("result", null);
			logger.debug(e.getMessage());
		}
		return rp;
	}	
	
	/**
	 * 부서원 근태현황 (기간 리스트)
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/team/termlist", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getTermList(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, 
			@RequestParam(value="id", required = true) String month,
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		try {
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
				
			
//			sabun = "317290";//"317268";
			logger.debug("/mobile/"+ tenantId+"/team/termlist s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("month", month);
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			List<Map<String, Object>> l = mobileService.getTermList(paramMap);
			l = MobileUtil.parseMobileList(l);
			
			rp.put("result", l);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/termlist s " + rp.toString());
		return rp;
	}
	
	/**
	 * 부서원 근태현황 (직원 리스트)
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/team/teamlist", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getTeamList(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, 
			@RequestParam(value="id", required = true) String id,
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try { 
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/"+ tenantId+"/team/teamlist s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			String sdate = id.split("@")[0];
			String orgCd = id.split("@")[1];
			String workTypeCd = id.split("@")[2];
			
			String[] orgList = orgCd.split(",");
			
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("sdate", sdate);
			paramMap.put("orgList", orgList);
			paramMap.put("workTypeCd", workTypeCd);
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			List<Map<String, Object>> l = mobileService.getTeamList(paramMap);
			l = MobileUtil.parseMobileList(l);
	
			rp.put("result", l);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail(e.getMessage());
		}

		logger.debug("/mobile/"+ tenantId+"/team/teamlist s " + rp.toString());
		return rp;
	}
	
	/**
	 * 부서원 근태현황 (직원 리스트)
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/team/teamdetail", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getTeamDetail(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, 
			@RequestParam(value="id", required = true) String id,
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			String userToken = request.getParameter("userToken");
		
			Aes256 aes = new Aes256(userToken);
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/"+ tenantId+"/team/teamdetail s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			String targetSabun = id.split("@")[0];
			String sdate = id.split("@")[1];
			String edate = id.split("@")[2];
			
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", targetSabun);
			paramMap.put("sdate", sdate);
			paramMap.put("edate", edate);
			
			List<Map<String, Object>> l = mobileService.getTeamDetail(paramMap);
			l = MobileUtil.parseMobileList(l);
			
			rp.put("result", l);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/teamdetail s " + rp.toString());
		return rp;
	}
}