package com.isu.ifw.controller;


import java.net.URLDecoder;
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
import com.isu.ifw.service.WtmCalendarService;
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

	@Autowired
	WtmCalendarService wtmCalendarService;
	
	/**
	 * dashboard
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/mobile/{tenantId}/my/dashboard", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyDashboard(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, HttpServletRequest request) throws Exception {


		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
			
			
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
			data.put("param10", statusMap.get("param10") == null? "-" : statusMap.get("param10").toString());
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

			logger.debug("1111111111111 0" + empKey);

			empKey = MobileUtil.encEmpKey(userToken, empKey);
			logger.debug("1111111111111 1" + empKey);

			
			 //{"deviceType":"android","osVersion":28,"tenantKey":"hdngv","loginPassword":"20000101","loginUserId":"20000101","deviceModel":"SM-G965N","locale":"ko_KR","pushToken":"cZoiIYlt5ew:APA91bHZsjQ42zSKByHVCKELzQkA_nRs1L7k0qqg3lxbTTrhahPTW8Vfj-hlRdWEae4Eaz-3oOeqY7nlg3uslRDWgiGX3oe-1LSNV7CjUoRmVclkvk8box5jxjXN84qJbXMYtQ7IpUKN","deviceId":"051e0704-45e6-42bf-94f6-7760afac1e0a","loginEnterCd":"H133","userToken":"26577433-1626-4d32-87f5-cb26a1891efc"}"

			 
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				logger.debug("1111111111111 2");
				rp.setFail("사용자 정보가 존재하지 않습니다.");
				return rp;
			}
			logger.debug("1111111111111 3 " + emp.toString());
			
			UUID token = UUID.randomUUID();
				
			Map<String, Object> userData = loginService.getUserData(tenantId, enterCd, sabun, password);

			logger.debug("1111111111111 4 " + userData.toString());

			WtmMobileToken mobileToken = tokenRepository.findByTenantIdAndEmpKey(tenantId, enterCd+"@"+sabun);
			if(mobileToken == null) {
				mobileToken = new WtmMobileToken();
				mobileToken.setEmpKey(enterCd+"@"+sabun);
				mobileToken.setTenantId(tenantId);
			} 
			mobileToken.setToken(token.toString());
			mobileToken = tokenRepository.save(mobileToken);
			logger.debug("1111111111111 5 ");
			
			Map<String, Object> sessionData = new HashMap();
			sessionData.put("orgNm", emp.getOrgCd());
			sessionData.put("authCode", "");
			sessionData.put("empNm", emp.getEmpNm());
			sessionData.put("id", enterCd+sabun);
			sessionData.put("accessToken", token.toString());
			
			logger.debug("1111111111111 6 ");
			Map<String, Object> result = new HashMap();
			result.put("sessionData", sessionData);
			
			
			logger.debug("1111111111111 7 ");
//			empKey = empKey.replace("+", "%2B"); 

			result.put("empKey", empKey);
			logger.debug("1111111111111 8 ");
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
		try {
			String enterCd = MobileUtil.parseEmpKey(empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(empKey, "sabun");
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				logger.debug("사용자 정보가 존재하지 않습니다.");

//				rp.setFail("사용자 정보가 존재하지 않습니다.");
				return rp;
			}
			WtmMobileToken mobileToken = tokenRepository.findByTenantIdAndEmpKey(tenantId, enterCd+"@"+sabun);
			if(mobileToken == null) {
				logger.debug("로그인 정보가 존재하지 않습니다.");
//				rp.setFail("로그인 정보가 존재하지 않습니다.");
				return rp;
			} 
			logger.debug("1111111111 " + mobileToken.getToken() + ", " + accessToken);
			if(!mobileToken.getToken().equals(accessToken)) {
//				rp.setFail("사용자 인증이 만료되었습니다.");
				logger.debug("1111111111 validate " + rp.toString());
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
			logger.debug("1111111 dddd " + e.getMessage());
			//			rp.setFail(e.getMessage());
		}
		logger.debug("1111111111 validate " + rp.toString());
		rp.setSuccess("");
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
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
			
			
			System.out.println("enterCd : " + enterCd);
			System.out.println("sabun : " + sabun );
			System.out.println(WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/"+tenantId+"/my/status " + empKey);
			
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
						,statusMap.get("param1") == null? "-" : statusMap.get("param1").toString(), statusMap.get("param3") == null? "-" :  "~" + statusMap.get("param3").toString(),"", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCNDgyNzE2MTYyMzcxMUU4OEIxOTk0OEVFRTA0MEY4QiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCNDgyNzE2MjYyMzcxMUU4OEIxOTk0OEVFRTA0MEY4QiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkI0ODI3MTVGNjIzNzExRTg4QjE5OTQ4RUVFMDQwRjhCIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkI0ODI3MTYwNjIzNzExRTg4QjE5OTQ4RUVFMDQwRjhCIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+cuXoBwAAA9VJREFUeNrsnUtoE1EUhie2WhVNFBfWau2iQhERH+DCVzRSBYUiiCgo1CCIiBARK7qxalduCj4gUncKVZT6QNz4wEcXVhAVq9BCRSRorS98pSqKjf8hNziMbXpnMqlD5v/h595OZiaTL3fOnHM7SQKpVMqg8q9hREDQBE0RtGdVrLNSIBDI6UnCkepGNDG4ufXWjahXXjyO6ySaDfARHFddLvsaLKkYqhEdU2/qJry4MR6BLMdRq45rR6GEjmK7Z1GhHRNjNC+GBE0RtHcV0JnrWLJs+T6VOQx3+DwhU/+LZENeeO1w0PT3Z4f76YUb7ty83pRzHg3th4tceoFBjw66UA7bHYWb3AgdRTz5s2qEK5WhRbPU6e93yZn52NUS3KIEytVPfqeMynIcsw6mdwRNETRBUwRN0ARNEXRByEll2I6qqI/o7A1SJ6DLyZiho6BCRxj+SnTGWLg1n6CfcPaOs3eM0QRNETRBUwRN0ARNETRBUwRN0P7Wf/s8SWlDdwWa8TY3e9ZTX5bU2PcUNGfhSfBGbNPmS9AAsR7NGSN9j7IdJbDtdID7Ngjk23ClWiT3dbf5NXQscgBZNBWusAFZNMrPoUNu3J5tZP+3mHy6oMyy7DTcaQOyv2M0Tv0uNIuzjEy5sbvFAlog12LbVD/rT/QyZE9mHYA2Gs0VuKYfyL8H2CxmgXyfI1oPcsQGZFG7amW0b4dL4Hkc0e5CllAkqdxceCb6xxmj8wDZBPsRK0P7kAWufO4vqrIJVoYuqM4CWSQft9um+n2AfQntTozaBOc6nOuXxjGugZ8C+FKOaOdqVGFC7vx5qzIHyRqq4BXwDLWePH5RleA9BG2/ePmJJp4lhq9Fc0qV0nJ30G54F0OH+29Ei4Kb0WrG6PzpnKlfiVFeQtD6ad0ceJrmqH5nWRQkaD3IUTQP4U70F2isb57lk4tlkqD1NN+UL9dorL/O1O/CCP9O0HoyzylvkZQty2iWr604YFp0gRdDfTUbf7/zYwL8AEAPwVUmwJPhenTvwZkvJZR8+zBB66dsUphslvJaLZIceY+K2R/h1+i/hA/CIzOluJGeZHpD0PZgn0ezEn5leUiKklLLsm54Fba5zLkOZ7CvoZEUbyt810jP2pnVAe+Fpey+amPXvQP0fV2C/0BzQoyQIbG4XM1rPMdj7x3uVi6YUXVmHCPof6En1SjOdT8f0Cxk6PChCJqgCZoiaIImaCIgaIKmCJqgCZoaEjmZVAqFI9UkZ/M3AZyAfkHG+Qsd/ELBHPnogo4TdlbI8cFWCvDHfZl1EDRF0ATtd/0RYAD/ZwKJUzMmhwAAAABJRU5ErkJggg=="
						,"총 근무 가능",""+statusMap.get("param4") == null? "-" : statusMap.get("param4").toString(), "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDMzIyODFFOTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDMzIyODFFQTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkMzMjI4MUU3NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkMzMjI4MUU4NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8++6xboAAADFlJREFUeNrsXQuUVlUV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"
						,"잔여(기본)", ""+statusMap.get("param5") == null? "-" : statusMap.get("param5").toString(), "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCQ0E1NDQ5QjYyMzcxMUU4OTZEMURBMjUwQzQ1ODU3QyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCQ0E1NDQ5QzYyMzcxMUU4OTZEMURBMjUwQzQ1ODU3QyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkJDQTU0NDk5NjIzNzExRTg5NkQxREEyNTBDNDU4NTdDIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkJDQTU0NDlBNjIzNzExRTg5NkQxREEyNTBDNDU4NTdDIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+0KekLgAAB5JJREFUeNrsXQtsFFUU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"
						,"잔여(연장)", ""+statusMap.get("param6") == null? "-" : statusMap.get("param6").toString(), "", ""));
			}else {
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBQjk2OEI5ODYyMzcxMUU4QjZDMEVDNEJDNDdBQzZDRCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBQjk2OEI5OTYyMzcxMUU4QjZDMEVDNEJDNDdBQzZDRCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkFCOTY4Qjk2NjIzNzExRThCNkMwRUM0QkM0N0FDNkNEIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkFCOTY4Qjk3NjIzNzExRThCNkMwRUM0QkM0N0FDNkNEIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Pe0TJgAABa9JREFUeNrsXWtsFFUUnuluWdpSuxqVpgg2QaP4JFg1xGgFNagh4uOH+CA+/vj6Qfxh9IchhBj9owYNNv1RFRITCQF8JKBFDdVoQkCDie8IglBKG419xNKWTnf9TnpHzt7MttuZnenc6fmSj3t7Z2f2zDdnzpx79k6w8/m8JQgfFSKBCC1CC0RoEVqEFkSCNP1j27YxBtdv6KpH0wFeYojJObDFRI++yyCR3ajxtIlCV5sYotOGh77d4EMxta0OPFoQow3GaPe6hr6YPksk65D0ToQWiNAitECEFqFFaIEILUILRGgRWoQWiNAitOAMTKxH8x84l9Rv6NocUztnmS70Faw/H3xEQkc4GJTQEQ3+YP0fwbdjamcV+LLJQnMc7l7XsDGOhuHZkeVCS9YhoaMk1MFzFgfYn7xubpFttMLoCOj4PHZtkoS+GTwoWUc46DPQ5lETPXo7eC94VYBjzNZChuPhgBVs24kgIoObbHpZyKTVpGXKCDrQNKs/25G53K5tX4DmEFiphh7DZwLNQAMLfdOyW+nKrwBXgheD5we48j3gt+COr/Z+/kNIIlNc38uGlkLEfR6fa0XzhJtGgpfic860CA2Rb0HzBnh5CJrsBNdC8M4ovTksr64I4Mlr0XwWksiWisMH8T1NZfbmZja0vthnIeoxNO+woRexv+9nWtqnyPej2ajd9lvBT8GToJ+3RKnadRG4GrxRjZ0L7sb3LSmTZ3Nh271Chgaa2T2uvHoh+DC42XfoaF5+2z3oPwlmStgnBV7PbimKX6sgxE/l8jwIuwZNm3Wm1PgX+HOQY45V1mZP18y7+v8TzzlDdt4ZmWy/XCqDiYedGhfLGZ7df3g//GgqjkTf0eoKTRUxPyvpB8DFEPlIueMGxH4UzbsJSXROuTHa7+sKr4UhssIW8LuECF3tFaMpDk1U893GwsaOsCzDBczDqynzuEYNfU0X1n8iW2EPzF16ZT6VqfJ7iMrBk53Vfb+UOnmpAd+b6GG4CyfZV+R2rmUiu/E5TPDa8yjs+jDY4fZ8EJULQ6tskPQupXndcMj2Dhtel0mG8SK0QIQWoUVoQTlQUq0Dqco5aM6ytNU3EcOGHfNV5tMdQcYTndA4MappbAKbYmDrDeAx1XdgG/3S8gwE/8fo0IETWYTmi5iIrOfw5CBU5WuHnWmjhQZeUNNIF/3gWMT2necxgeHlAXKCVaYLfS3rr8EtSlPK5RHbdyHrD4FZ2DHHKqyxNJkuNK/ouUX3P6cxXIxA5BHNHkKV6UILRGgRWiBCi9AzXuhTrL9AtY0R28fz9gwmJ+6v9BdoaZ/RU/AD4CLV34KTfNPS1vxirNSVnSRQJbuwOTX5mGyJ1SwtjevHdzraROqA6UK/At7HTqrO4zN1AfLjGh/7ZazCtSf7wY+NDh2YHPyKZpmHx0zXW1G8WncafB+8A3Y6pns0iU0iX4fb9WzlvUMY68Hf86zCX8OLYbW6Mwh04Wg1FJVbd7Lvpmn9ZGtDqM7yrzW+ppnuhp5ElUmZ4L1oetnfJa1twAXhYvyG/b5U44Ms7PRi/GiJ9nZahkLSu4QI7eh3D7zZtgqLRWMzQeiwi+a82rcCIh9X/TkszTsuHh0c7exBl1YTDT7Z2Fps+ZkIPQVARErD6N2W3z02fwI+JTG6fGLTAvLLwG/Y8KsYvxMcEKHLKzY9FP9mQyesGYayPQzxoHsQzbMTTGQaWf85taK/2OSE3sb6XoT2xlvW+EvspaBBsRjWg3dL6PBGNqbHSnQe/QDYrY29ZI2vNnK9f7u2nV6sfF5i9NSwT69fICbzh+EhbO/QtjfOhKwjx8ZGfR5LL2PqGCnS9xobSoC2XMecK3SLqku0wdP81ptfV8fYhmN0eWxvUxkFvWD0kcf2PSC9FErl0FbTVVY6tilNWmz5P2cTNGERiNAitAgtEKHjjP8EGAApHZTWqLpuNQAAAABJRU5ErkJggg=="
						, "-" , "-","",""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCNDgyNzE2MTYyMzcxMUU4OEIxOTk0OEVFRTA0MEY4QiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCNDgyNzE2MjYyMzcxMUU4OEIxOTk0OEVFRTA0MEY4QiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkI0ODI3MTVGNjIzNzExRTg4QjE5OTQ4RUVFMDQwRjhCIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkI0ODI3MTYwNjIzNzExRTg4QjE5OTQ4RUVFMDQwRjhCIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+cuXoBwAAA9VJREFUeNrsnUtoE1EUhie2WhVNFBfWau2iQhERH+DCVzRSBYUiiCgo1CCIiBARK7qxalduCj4gUncKVZT6QNz4wEcXVhAVq9BCRSRorS98pSqKjf8hNziMbXpnMqlD5v/h595OZiaTL3fOnHM7SQKpVMqg8q9hREDQBE0RtGdVrLNSIBDI6UnCkepGNDG4ufXWjahXXjyO6ySaDfARHFddLvsaLKkYqhEdU2/qJry4MR6BLMdRq45rR6GEjmK7Z1GhHRNjNC+GBE0RtHcV0JnrWLJs+T6VOQx3+DwhU/+LZENeeO1w0PT3Z4f76YUb7ty83pRzHg3th4tceoFBjw66UA7bHYWb3AgdRTz5s2qEK5WhRbPU6e93yZn52NUS3KIEytVPfqeMynIcsw6mdwRNETRBUwRN0ARNEXRByEll2I6qqI/o7A1SJ6DLyZiho6BCRxj+SnTGWLg1n6CfcPaOs3eM0QRNETRBUwRN0ARNETRBUwRN0P7Wf/s8SWlDdwWa8TY3e9ZTX5bU2PcUNGfhSfBGbNPmS9AAsR7NGSN9j7IdJbDtdID7Ngjk23ClWiT3dbf5NXQscgBZNBWusAFZNMrPoUNu3J5tZP+3mHy6oMyy7DTcaQOyv2M0Tv0uNIuzjEy5sbvFAlog12LbVD/rT/QyZE9mHYA2Gs0VuKYfyL8H2CxmgXyfI1oPcsQGZFG7amW0b4dL4Hkc0e5CllAkqdxceCb6xxmj8wDZBPsRK0P7kAWufO4vqrIJVoYuqM4CWSQft9um+n2AfQntTozaBOc6nOuXxjGugZ8C+FKOaOdqVGFC7vx5qzIHyRqq4BXwDLWePH5RleA9BG2/ePmJJp4lhq9Fc0qV0nJ30G54F0OH+29Ei4Kb0WrG6PzpnKlfiVFeQtD6ad0ceJrmqH5nWRQkaD3IUTQP4U70F2isb57lk4tlkqD1NN+UL9dorL/O1O/CCP9O0HoyzylvkZQty2iWr604YFp0gRdDfTUbf7/zYwL8AEAPwVUmwJPhenTvwZkvJZR8+zBB66dsUphslvJaLZIceY+K2R/h1+i/hA/CIzOluJGeZHpD0PZgn0ezEn5leUiKklLLsm54Fba5zLkOZ7CvoZEUbyt810jP2pnVAe+Fpey+amPXvQP0fV2C/0BzQoyQIbG4XM1rPMdj7x3uVi6YUXVmHCPof6En1SjOdT8f0Cxk6PChCJqgCZoiaIImaCIgaIKmCJqgCZoaEjmZVAqFI9UkZ/M3AZyAfkHG+Qsd/ELBHPnogo4TdlbI8cFWCvDHfZl1EDRF0ATtd/0RYAD/ZwKJUzMmhwAAAABJRU5ErkJggg=="
						,"총 근무 가능", "-" , "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDMzIyODFFOTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDMzIyODFFQTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkMzMjI4MUU3NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkMzMjI4MUU4NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8++6xboAAADFlJREFUeNrsXQuUVlUV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"
						,"잔여(기본)", "-", "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCQ0E1NDQ5QjYyMzcxMUU4OTZEMURBMjUwQzQ1ODU3QyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCQ0E1NDQ5QzYyMzcxMUU4OTZEMURBMjUwQzQ1ODU3QyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkJDQTU0NDk5NjIzNzExRTg5NkQxREEyNTBDNDU4NTdDIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkJDQTU0NDlBNjIzNzExRTg5NkQxREEyNTBDNDU4NTdDIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+0KekLgAAB5JJREFUeNrsXQtsFFUU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"
						,"잔여(연장)", "-", "", ""));
			}
			
			String adapter = tcms.getConfigValue(tenantId, "HR.ADAPTER_URL", true, "");
			if(adapter != "") {
				Map restCnt = mobileService.getDataMap(adapter+"/GetDataMap.do", "getMyRestCnt", enterCd, sabun);

				if(restCnt.get("creCnt")!=null) {
					statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDOTk0MkM0ODYyMzcxMUU4QUYyQ0FBRjVDN0RENkM5MSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDOTk0MkM0OTYyMzcxMUU4QUYyQ0FBRjVDN0RENkM5MSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkM5OTQyQzQ2NjIzNzExRThBRjJDQUFGNUM3REQ2QzkxIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkM5OTQyQzQ3NjIzNzExRThBRjJDQUFGNUM3REQ2QzkxIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+MemidAAADMpJREFUeNrsXQ2UVVUV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"
							,"발생연차", ""+restCnt.get("creCnt"), "", "일"));
				}
				if(restCnt.get("restCnt")!=null) {
					statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDRkIwNTkyRDYyMzcxMUU4OEI5RkVCRkUxNDE5NDRDNyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDRkIwNTkyRTYyMzcxMUU4OEI5RkVCRkUxNDE5NDRDNyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkNGQjA1OTJCNjIzNzExRTg4QjlGRUJGRTE0MTk0NEM3IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkNGQjA1OTJDNjIzNzExRTg4QjlGRUJGRTE0MTk0NEM3Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+KvcPmAAABHJJREFUeNrsnc9LFVEUx2fS8qnpy12oiYtAKCT6Byyj2gUtWrUwFyERZFGSZqSiRka1qEB0aSQIQbRppWnarlXmrpW4CBHBX5laqX1PjvGYZt67972Z3sx73wOHccbbeWc+3jn3nHvvm8zt7W2D4r/sIQKCJmgKQQdWchM1ME0zpQ+oqT39BIdG6ODE2Eh9UG4cfg3gcBH6FH41pWJLJaH4Hz260fqDXsLN7Q8IZPGjzvLreqaEjlydJygNT3JupoCmEDRBEzQlOTETpSYnTp25Z2UOe5P8jGjMz8uSDQXhvqHFMedLSdpZhXaOjw73p5xHQ9qhOR7dYHFAO1w0hX/3DNrvRejI4YMfV/Z5Uhna5Jj1+Ge7yJM56WkJbpMZlKuL2U4ZleUBZh1M7wiaQtAETSFogiZoCkFnjOhWhp9RFW0Rm34H1QV9iIwZOjIqdNRAV4jNKIJO+Al6irN3nL1jjKYEZ+eQb3Kw8+thHN5Ay6AXZttK37FH+wP5PfQoVOLqZYYO/yCXxVx+RdD+QpZq9irCxmvGaDWAd3Gohw4AWrcG5Dq0H2TWoQZZ9k90QQVkF857wgLZtx6NhD4Ph3yFphsogNbi2IngEPlzMla38qX2xTh+Omn9uhlgDUBsSQQZdsSXPAV/1uDPRih6NG6q1djZk7agoKto3+di56HNTjX0PPRjTDOB3ZMAcp+mP61hCR3XDPVtZLLZsMHqcXZptPsHcLIZ8awdNvRTnJ7cYH2OiuRY/ocCdL5me9PlsY44NXaBXegSk/M0ICfrfyAGQ5ntK3HQSk07sudvKgFslYGv0sWfmrCndytOs314pHXtLMPOpr1nIzYL7JfQ49AmXBtKYGfJxZ+VsIP2VayefS4MvnL2jpWhUiopE0U3ds8RFjoI2h8R0O0x51kLuspl4CvStHPEZfmoVNNOtcvAVxV20EMe2XnrkZ2JdPVoPwZD3VTpF/S7w/Vvmnac2ss8yqamnaWw9GiZ5GlTnMQRyH0YxH44/K7JUpXvN/6EPv6n+46NbCBUiD9XFO9VJpQ6/QCt8oXO2AYl3G7wN9tZ2D0fHx020xE6KARN0ARNCWjWgYFCJuwfQAsUmku2IS+Puu1gR96qIKsdEQU769D7sNPtYOeRsbOIoPKdbUkzW2DneRh6dLMiZMO6+VvW2qBTmhhRtCPt7jhAlus3FSEblt8tYQkdhUn4EHG5aR0pcPkD7PHZ/0CU4LJyMeVwXd5zMa1hR1ZYZhyuVxh6bxmodKn8qv0uz8O0wrLoUjholdfpWmFh1sH0jqApBJ1FBYtNSjHQOM326b4trNxlAC3XtFPh0UpN4EB7tTLywSM7k+nq0UFYYZEVEKcdpV6ssEhpvuWz/2kDLXuYZ63CIJHOQ7tdtsp2QOcU7cwZDivgsLtu+TOvaEf85gpLUIQrLEzvKARN0ARNIWiCJmgiIOiMEt1JpWgSy1CZKFG/QU+TsX+hgy8U9ICPCuhewo4LuVelocn/3JdZB0FTCJqgs11+CzAAbWp24z150LwAAAAASUVORK5CYII="
							,"잔여연차", ""+restCnt.get("restCnt"), "", "일"));
				}
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
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
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
			if(l == null || l.size() <= 0) {
				rp.setFail("조회 결과가 없습니다.");
				return rp;
			}
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
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/"+ tenantId+"/team/teamlist s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			String sdate = id.split("@")[0];
			String month = id.split("@")[1];
			String workTypeCd = id.split("@")[2];
			
	//		String[] orgList = orgCd.split(",");
			
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("sdate", sdate);
			paramMap.put("month", month);
			paramMap.put("workTypeCd", workTypeCd);
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			List<Map<String, Object>> l = mobileService.getTeamList(paramMap);
			if(l == null || l.size() <= 0) {
				rp.setFail("조회 결과가 없습니다.");
				return rp;
			}
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
		
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
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
			if(l == null || l.size() <= 0) {
				rp.setFail("조회 결과가 없습니다.");
				return rp;
			}
			l = MobileUtil.parseMobileList(l);
			
			rp.put("result", l);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/teamdetail s " + rp.toString());
		return rp;
	}
	
	/**
	 * 내 근무상태 정보
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/my/info", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyworkInfo(@PathVariable Long tenantId,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey, 
			@RequestParam(value="id", required = true) String ymd, 
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
			
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/"+tenantId+"/dashboard s" + empKey);
			
			Map<String, Object> resultMap = new HashMap();
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId" , tenantId);
			paramMap.put("enterCd" , enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("ymd", ymd);
			
			
			Map<String, Object> data = wtmCalendarService.getEmpWorkCalendarDayInfo(paramMap);
			resultMap.put("data", data);
			rp.put("result", resultMap);
			logger.debug("/mobile/"+tenantId+"/dashboard e " + rp.toString());
		} catch(Exception e) {
			rp.put("result", null);
			logger.debug(e.getMessage());
		}
		return rp;
	}
	
	/**
	 * 근무계획시간 조회
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/team/plan", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> applyValidate(@PathVariable Long tenantId, 
			@RequestBody Map<String, Object> paramMap
			,HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		try {
			String userToken = paramMap.get("userToken").toString();
			String empKey = paramMap.get("empKey").toString();
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}

			logger.debug("/mobile/"+ tenantId+"/team/plan s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			Map<String, Object> result = new HashMap();
			Map<String, Object> data = (Map<String, Object>) paramMap.get("data");
			data.put("tenantId", tenantId);
			data.put("enterCd", enterCd);
			Map<String,Map<String,Object>> itemPropertiesMap = new HashMap<String,Map<String,Object>>();
			
			if(paramMap.get("eventSource").equals("ymd") || paramMap.get("eventSource").equals("sabun")) {
				if(data.containsKey("ymd") && !data.get("ymd").toString().equals("")
						&& data.containsKey("sabun") && !data.get("sabun").toString().equals("")) {
					data.put("plan", "");
					data.put("emp", "");
					if(data.get("sabun").toString().length() < 2 ) {
						result.put("data", data);
						rp.put("result", result);
						rp.setFail("두글자 이상 입력해주세요.");
						return rp;
					}
					
					List<Map<String, Object>> l = mobileService.getPlan(data);
					if(l == null || l.size() <= 0) {
						data.put("plan", "");
						data.put("emp", "");
//						itemPropertiesMap.put("emp", null);
						
						Map<String,Object> temp = new HashMap<String,Object>();
						itemPropertiesMap.put("emp", temp);
						
						result.put("data", data);
						result.put("itemAttributesMap", itemPropertiesMap);
						rp.put("result", result);
						rp.setFail("조회 결과가 없습니다.");
						return rp;
					}
					
//					int cnt = 0;
					
					List<Map<String,Object>> itemCollection = new ArrayList<Map<String,Object>>();
					data.put("plan", "");
					data.put("emp", "");
					
					Map<String,Object> blank = new HashMap<String,Object>();
					blank.put("text", "선택");
					blank.put("value", "");
					itemCollection.add(blank);
					
					for(Map<String, Object> row : l) {
//						if(cnt == 0) {
//							data.put("emp", data.get("emp") +"@"+ row.get("plan").toString());
//							data.put("plan", row.get("plan").toString());
//						}
						String value = row.get("sabun").toString();
						String text = row.get("emp").toString();
						Map<String,Object> item = new HashMap<String,Object>();
						item.put("text", text);
						item.put("value", value);
						itemCollection.add(item);
//						cnt++;
					}
					
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("collection", itemCollection);
					
					itemPropertiesMap.put("emp", item);
					result.put("itemAttributesMap", itemPropertiesMap);
				}
			} else if(paramMap.get("eventSource").equals("emp")){
					Map<String, Object> param = new HashMap();
					param.putAll(data);
					param.put("sabun", data.get("emp").toString());
					List<Map<String, Object>> l = mobileService.getPlan(param);
					if(l!= null && l.size()>0) {
						Map<String, Object> p = l.get(0);
						data.put("plan", p.get("plan"));
					}
				}

			result.put("data", data);
			rp.put("result", result);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/plan e " + rp.toString());
		return rp;
	}
	
	
	/**
	 * 타각시간 조회
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/team/inout", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> getTeamInoutList(@PathVariable Long tenantId, 
			@RequestBody Map<String, Object> paramMap
			,HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		try {
			String userToken = paramMap.get("userToken").toString();
			String empKey = paramMap.get("empKey").toString();
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}

			logger.debug("/mobile/"+ tenantId+"/team/plan s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			Map<String, Object> result = new HashMap();
			Map<String, Object> data = (Map<String, Object>) paramMap.get("data");
			data.put("tenantId", tenantId);
			data.put("enterCd", enterCd);
			Map<String,Map<String,Object>> itemPropertiesMap = new HashMap<String,Map<String,Object>>();
			
			if(paramMap.get("eventSource").equals("ymd") || paramMap.get("eventSource").equals("sabun")) {
				if(data.containsKey("ymd") && !data.get("ymd").toString().equals("")
						&& data.containsKey("sabun") && !data.get("sabun").toString().equals("")) {
					if(data.get("sabun").toString().length() < 2 ) {
						rp.setFail("두글자 이상 입력해주세요.");
						return rp;
					}
					
					List<Map<String, Object>> l = mobileService.getPlan(data);
					if(l == null || l.size() <= 0) {
						data.put("plan", "");
						data.put("emp", "");
						itemPropertiesMap.put("emp", null);
						
						result.put("data", data);
						result.put("itemAttributesMap", itemPropertiesMap);
						rp.setFail("조회 결과가 없습니다.");
						return rp;
					}
					
					int cnt = 0;
					
					List<Map<String,Object>> itemCollection = new ArrayList<Map<String,Object>>();
					for(Map<String, Object> row : l) {
						if(cnt == 0) {
							data.put("emp", data.get("emp") +"@"+ row.get("plan").toString());
							data.put("plan", row.get("plan").toString());
						}
						String value = row.get("emp") +"@"+ row.get("plan").toString();
						String text = row.get("emp").toString();
						Map<String,Object> item = new HashMap<String,Object>();
						item.put("text", text);
						item.put("value", value);
						itemCollection.add(item);
						cnt++;
					}
					
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("collection", itemCollection);
					
					itemPropertiesMap.put("emp", item);
					result.put("itemAttributesMap", itemPropertiesMap);
				}
			} else {
				String temp = data.get("emp").toString().split("@")[1];
				data.put("plan", temp);
			}

			result.put("data", data);
			rp.put("result", result);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/plan e " + rp.toString());
		return rp;
	}
	
	@RequestMapping(value = "/mobile/demo/{uId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam demo(@PathVariable Long uId,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey, 
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			if(uId == 1) {
				List<Map<String, Object>> statusList = new ArrayList();
				
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBQjk2OEI5ODYyMzcxMUU4QjZDMEVDNEJDNDdBQzZDRCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBQjk2OEI5OTYyMzcxMUU4QjZDMEVDNEJDNDdBQzZDRCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkFCOTY4Qjk2NjIzNzExRThCNkMwRUM0QkM0N0FDNkNEIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkFCOTY4Qjk3NjIzNzExRThCNkMwRUM0QkM0N0FDNkNEIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Pe0TJgAABa9JREFUeNrsXWtsFFUUnuluWdpSuxqVpgg2QaP4JFg1xGgFNagh4uOH+CA+/vj6Qfxh9IchhBj9owYNNv1RFRITCQF8JKBFDdVoQkCDie8IglBKG419xNKWTnf9TnpHzt7MttuZnenc6fmSj3t7Z2f2zDdnzpx79k6w8/m8JQgfFSKBCC1CC0RoEVqEFkSCNP1j27YxBtdv6KpH0wFeYojJObDFRI++yyCR3ajxtIlCV5sYotOGh77d4EMxta0OPFoQow3GaPe6hr6YPksk65D0ToQWiNAitECEFqFFaIEILUILRGgRWoQWiNAitOAMTKxH8x84l9Rv6NocUztnmS70Faw/H3xEQkc4GJTQEQ3+YP0fwbdjamcV+LLJQnMc7l7XsDGOhuHZkeVCS9YhoaMk1MFzFgfYn7xubpFttMLoCOj4PHZtkoS+GTwoWUc46DPQ5lETPXo7eC94VYBjzNZChuPhgBVs24kgIoObbHpZyKTVpGXKCDrQNKs/25G53K5tX4DmEFiphh7DZwLNQAMLfdOyW+nKrwBXgheD5we48j3gt+COr/Z+/kNIIlNc38uGlkLEfR6fa0XzhJtGgpfic860CA2Rb0HzBnh5CJrsBNdC8M4ovTksr64I4Mlr0XwWksiWisMH8T1NZfbmZja0vthnIeoxNO+woRexv+9nWtqnyPej2ajd9lvBT8GToJ+3RKnadRG4GrxRjZ0L7sb3LSmTZ3Nh271Chgaa2T2uvHoh+DC42XfoaF5+2z3oPwlmStgnBV7PbimKX6sgxE/l8jwIuwZNm3Wm1PgX+HOQY45V1mZP18y7+v8TzzlDdt4ZmWy/XCqDiYedGhfLGZ7df3g//GgqjkTf0eoKTRUxPyvpB8DFEPlIueMGxH4UzbsJSXROuTHa7+sKr4UhssIW8LuECF3tFaMpDk1U893GwsaOsCzDBczDqynzuEYNfU0X1n8iW2EPzF16ZT6VqfJ7iMrBk53Vfb+UOnmpAd+b6GG4CyfZV+R2rmUiu/E5TPDa8yjs+jDY4fZ8EJULQ6tskPQupXndcMj2Dhtel0mG8SK0QIQWoUVoQTlQUq0Dqco5aM6ytNU3EcOGHfNV5tMdQcYTndA4MappbAKbYmDrDeAx1XdgG/3S8gwE/8fo0IETWYTmi5iIrOfw5CBU5WuHnWmjhQZeUNNIF/3gWMT2necxgeHlAXKCVaYLfS3rr8EtSlPK5RHbdyHrD4FZ2DHHKqyxNJkuNK/ouUX3P6cxXIxA5BHNHkKV6UILRGgRWiBCi9AzXuhTrL9AtY0R28fz9gwmJ+6v9BdoaZ/RU/AD4CLV34KTfNPS1vxirNSVnSRQJbuwOTX5mGyJ1SwtjevHdzraROqA6UK/At7HTqrO4zN1AfLjGh/7ZazCtSf7wY+NDh2YHPyKZpmHx0zXW1G8WncafB+8A3Y6pns0iU0iX4fb9WzlvUMY68Hf86zCX8OLYbW6Mwh04Wg1FJVbd7Lvpmn9ZGtDqM7yrzW+ppnuhp5ElUmZ4L1oetnfJa1twAXhYvyG/b5U44Ms7PRi/GiJ9nZahkLSu4QI7eh3D7zZtgqLRWMzQeiwi+a82rcCIh9X/TkszTsuHh0c7exBl1YTDT7Z2Fps+ZkIPQVARErD6N2W3z02fwI+JTG6fGLTAvLLwG/Y8KsYvxMcEKHLKzY9FP9mQyesGYayPQzxoHsQzbMTTGQaWf85taK/2OSE3sb6XoT2xlvW+EvspaBBsRjWg3dL6PBGNqbHSnQe/QDYrY29ZI2vNnK9f7u2nV6sfF5i9NSwT69fICbzh+EhbO/QtjfOhKwjx8ZGfR5LL2PqGCnS9xobSoC2XMecK3SLqku0wdP81ptfV8fYhmN0eWxvUxkFvWD0kcf2PSC9FErl0FbTVVY6tilNWmz5P2cTNGERiNAitAgtEKHjjP8EGAApHZTWqLpuNQAAAABJRU5ErkJggg=="
						,"잔여연차", "15","개", "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBQjk2OEI5ODYyMzcxMUU4QjZDMEVDNEJDNDdBQzZDRCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBQjk2OEI5OTYyMzcxMUU4QjZDMEVDNEJDNDdBQzZDRCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkFCOTY4Qjk2NjIzNzExRThCNkMwRUM0QkM0N0FDNkNEIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkFCOTY4Qjk3NjIzNzExRThCNkMwRUM0QkM0N0FDNkNEIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Pe0TJgAABa9JREFUeNrsXWtsFFUUnuluWdpSuxqVpgg2QaP4JFg1xGgFNagh4uOH+CA+/vj6Qfxh9IchhBj9owYNNv1RFRITCQF8JKBFDdVoQkCDie8IglBKG419xNKWTnf9TnpHzt7MttuZnenc6fmSj3t7Z2f2zDdnzpx79k6w8/m8JQgfFSKBCC1CC0RoEVqEFkSCNP1j27YxBtdv6KpH0wFeYojJObDFRI++yyCR3ajxtIlCV5sYotOGh77d4EMxta0OPFoQow3GaPe6hr6YPksk65D0ToQWiNAitECEFqFFaIEILUILRGgRWoQWiNAitOAMTKxH8x84l9Rv6NocUztnmS70Faw/H3xEQkc4GJTQEQ3+YP0fwbdjamcV+LLJQnMc7l7XsDGOhuHZkeVCS9YhoaMk1MFzFgfYn7xubpFttMLoCOj4PHZtkoS+GTwoWUc46DPQ5lETPXo7eC94VYBjzNZChuPhgBVs24kgIoObbHpZyKTVpGXKCDrQNKs/25G53K5tX4DmEFiphh7DZwLNQAMLfdOyW+nKrwBXgheD5we48j3gt+COr/Z+/kNIIlNc38uGlkLEfR6fa0XzhJtGgpfic860CA2Rb0HzBnh5CJrsBNdC8M4ovTksr64I4Mlr0XwWksiWisMH8T1NZfbmZja0vthnIeoxNO+woRexv+9nWtqnyPej2ajd9lvBT8GToJ+3RKnadRG4GrxRjZ0L7sb3LSmTZ3Nh271Chgaa2T2uvHoh+DC42XfoaF5+2z3oPwlmStgnBV7PbimKX6sgxE/l8jwIuwZNm3Wm1PgX+HOQY45V1mZP18y7+v8TzzlDdt4ZmWy/XCqDiYedGhfLGZ7df3g//GgqjkTf0eoKTRUxPyvpB8DFEPlIueMGxH4UzbsJSXROuTHa7+sKr4UhssIW8LuECF3tFaMpDk1U893GwsaOsCzDBczDqynzuEYNfU0X1n8iW2EPzF16ZT6VqfJ7iMrBk53Vfb+UOnmpAd+b6GG4CyfZV+R2rmUiu/E5TPDa8yjs+jDY4fZ8EJULQ6tskPQupXndcMj2Dhtel0mG8SK0QIQWoUVoQTlQUq0Dqco5aM6ytNU3EcOGHfNV5tMdQcYTndA4MappbAKbYmDrDeAx1XdgG/3S8gwE/8fo0IETWYTmi5iIrOfw5CBU5WuHnWmjhQZeUNNIF/3gWMT2necxgeHlAXKCVaYLfS3rr8EtSlPK5RHbdyHrD4FZ2DHHKqyxNJkuNK/ouUX3P6cxXIxA5BHNHkKV6UILRGgRWiBCi9AzXuhTrL9AtY0R28fz9gwmJ+6v9BdoaZ/RU/AD4CLV34KTfNPS1vxirNSVnSRQJbuwOTX5mGyJ1SwtjevHdzraROqA6UK/At7HTqrO4zN1AfLjGh/7ZazCtSf7wY+NDh2YHPyKZpmHx0zXW1G8WncafB+8A3Y6pns0iU0iX4fb9WzlvUMY68Hf86zCX8OLYbW6Mwh04Wg1FJVbd7Lvpmn9ZGtDqM7yrzW+ppnuhp5ElUmZ4L1oetnfJa1twAXhYvyG/b5U44Ms7PRi/GiJ9nZahkLSu4QI7eh3D7zZtgqLRWMzQeiwi+a82rcCIh9X/TkszTsuHh0c7exBl1YTDT7Z2Fps+ZkIPQVARErD6N2W3z02fwI+JTG6fGLTAvLLwG/Y8KsYvxMcEKHLKzY9FP9mQyesGYayPQzxoHsQzbMTTGQaWf85taK/2OSE3sb6XoT2xlvW+EvspaBBsRjWg3dL6PBGNqbHSnQe/QDYrY29ZI2vNnK9f7u2nV6sfF5i9NSwT69fICbzh+EhbO/QtjfOhKwjx8ZGfR5LL2PqGCnS9xobSoC2XMecK3SLqku0wdP81ptfV8fYhmN0eWxvUxkFvWD0kcf2PSC9FErl0FbTVVY6tilNWmz5P2cTNGERiNAitAgtEKHjjP8EGAApHZTWqLpuNQAAAABJRU5ErkJggg=="
						,"개정연차", "4","개", "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDMzIyODFFOTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDMzIyODFFQTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkMzMjI4MUU3NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkMzMjI4MUU4NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8++6xboAAADFlJREFUeNrsXQuUVlUV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"
						,"일 근무", "8", "시간", "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDMzIyODFFOTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDMzIyODFFQTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkMzMjI4MUU3NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkMzMjI4MUU4NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8++6xboAAADFlJREFUeNrsXQuUVlUV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"
						,"주 근무", "40", "시간", "", ""));
				statusList.add(MobileUtil.getStatusMap("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDMzIyODFFOTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDMzIyODFFQTYyMzcxMUU4QjVDNUZBNkU2NjZENkVCNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkMzMjI4MUU3NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkMzMjI4MUU4NjIzNzExRThCNUM1RkE2RTY2NkQ2RUI0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8++6xboAAADFlJREFUeNrsXQuUVlUV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"
						,"월별 근태 현황", "", "", "", "view://inoutList"));
				rp.put("result", statusList);
			} else if(uId == 2) {
				Map<String, Object> data = new HashMap();
				Map<String, Object> result = new HashMap();
				
				data.put("ymd", "20200217");
				result.put("data", data);
				
				List<Map<String,Object>> itemCollection = new ArrayList<Map<String,Object>>();
				Map<String,Object> propertiesMap1 = new HashMap();
				Map<String,Object> propertiesMap = new HashMap();
				Map<String,Object> itemPropertiesMap = new HashMap();

				Map<String,Object> item = new HashMap<String,Object>();
				item = new HashMap<String,Object>();
				item.put("text", "재직증명서");
				item.put("value", "1");
				itemCollection.add(item);

				item = new HashMap<String,Object>();
				item.put("text", "원천징수");
				item.put("value", "2");
				itemCollection.add(item);
				propertiesMap.put("collection", itemCollection);
				itemPropertiesMap.put("appl", propertiesMap);
				
				item = new HashMap<String,Object>();
				itemCollection = new ArrayList();
				item.put("text", "은행제출");
				item.put("value", "1");
				itemCollection.add(item);
				
				propertiesMap1.put("collection", itemCollection);
				itemPropertiesMap.put("reason", propertiesMap1);
				result.put("itemAttributesMap", itemPropertiesMap);
				rp.put("result", result);
			} else if(uId == 3) {
				List<Map<String, Object>> statusList = new ArrayList();

				Map<String, Object> statusMap = new HashMap();
				statusMap.put("title", "개인정보수집이용 동의서");
				statusMap.put("caption_lb", "2020.03.01까지");
				statusMap.put("status", "작성완료");
				statusList.add(statusMap);
				
				statusMap = new HashMap();
				statusMap.put("title", "취업조건고지서");
				statusMap.put("caption_lb", "2020.03.01까지");
				statusMap.put("status", "미작성");
				statusList.add(statusMap);

				rp.put("result", statusList);
			} else if(uId == 4) {
				Map<String, Object> data = new HashMap();
				Map<String, Object> result = new HashMap();
				
				data.put("test2", "성명, e-메일주소, 연락처, 주민등록번호, 성별, 주소, 학력, 경력, 자기소개서,안면");
				data.put("test3", "이력서 작성, 근로계약서 체결, 명부/임금대장 작성, 기본업무 연락");
				data.put("test4", "원칙적으로 개인정보의 수집 또는 이용 목적 달성 시 지체 없이 파기.  다만, 이용자의 권리 남용, 악용 방지,\r\n" + 
									"권리침해/명예훼손 분쟁 및 수사협조 등의 요청이 있었을 경우에는 이의 재발에 대비하여 회원의\r\n" + 
									"이용계약 해지시부터 1년 동안 개인정보를  보관할 수 있음");
	
				result.put("data", data);
				rp.put("result", result);
			}
		} catch(Exception e) {
			rp.put("result", null);
			logger.debug(e.getMessage());
		}
		return rp;
	}	
}