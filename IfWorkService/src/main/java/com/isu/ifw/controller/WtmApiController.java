package com.isu.ifw.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.mapper.WtmInoutHisMapper;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.util.MobileUtil;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/api")
public class WtmApiController {

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

	//출퇴근 상태 정보
	@RequestMapping(value = "/{tsId}/inoutstatus", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyWorkStatus(
			@PathVariable String tsId, 
			@RequestParam(value="empKey", required = true) String empKey, 
			HttpServletRequest request) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		String enterCd = MobileUtil.parseEmpKey(empKey, "enterCd"); 
		String sabun = MobileUtil.parseEmpKey(empKey, "sabun"); 
		
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
	@RequestMapping (value="/{tsId}/in", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestIn(@PathVariable String tsId, 
			@RequestBody Map<String,Object> params, HttpServletRequest request)throws Exception{
	
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("출근 체크 되었습니다.");

		try {
			logger.debug(tsId + "/api/in s " + params.toString());

			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			Long tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			String enterCd = (String)params.get("enterCd");
			String sabun = (String)params.get("sabun");
	
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
			
			logger.debug(tsId + "/api/in s2 " + paramMap.toString());
		
			Map<String, Object> yn = inoutHisMapper.getMyUnplannedYn(paramMap);
			if(yn != null && yn.get("unplannedYn") != null && "Y".equals(yn.get("unplannedYn").toString())) {
				inoutService.updateTimecardUnplanned(paramMap);
			} else {
				inoutService.updateTimecard(paramMap);
			}
			
			logger.debug("in : " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());

		} catch(Exception e) {
			logger.debug("inexception : " + e.getMessage());
			rp.setFail(e.getMessage());
		}
		logger.debug(tsId + "/api/in e " + rp.toString());
		return rp;
	}
	
	//퇴근
	@RequestMapping (value="/{tsId}/out", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestOut(@PathVariable String tsId, 
			@RequestBody Map<String,Object> params, HttpServletRequest request)throws Exception{
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 체크 되었습니다.");
		
		Map<String, Object> paramMap = new HashMap();
		try {
			logger.debug(tsId + "/api/out s " + params.toString());

			
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			Long tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			String enterCd = (String)params.get("enterCd");
			String sabun = (String)params.get("sabun");
			
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
			
			logger.debug(tsId + "/api/out s2 " + paramMap.toString());
			
			Map<String, Object> yn = inoutHisMapper.getMyUnplannedYn(paramMap);
			if(yn != null && yn.get("unplannedYn") != null && "Y".equals(yn.get("unplannedYn").toString())) {
				inoutService.updateTimecardUnplanned(paramMap);
			} else {
				inoutService.updateTimecard(paramMap);
			}

		} catch(Exception e) {
			logger.debug("outexception : " + e.getMessage() + paramMap.toString());
			rp.setFail(e.getMessage());
		}
		logger.debug(tsId + "/api/out e " + rp.toString());
		return rp;
	}
	
	//퇴근취소
	@RequestMapping(value = "/{tsId}/cancel", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam cancelOutRequest(@PathVariable String tsId,
			@RequestBody Map<String,Object> params, HttpServletRequest request) throws Exception {		
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 정보가 취소되었습니다.");

		Map<String, Object> paramMap = new HashMap();

		try {
			logger.debug(tsId + "/api/cancel s " + params.toString());
 
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			Long tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			String enterCd = (String)params.get("enterCd");
			String sabun = (String)params.get("sabun");
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
			Date now = new Date();
			String today = format1.format(now);
			
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "OUTC");
			paramMap.put("ymd", ymd);
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "API");
			
			logger.debug(tsId + "/api/cancel s2 " + paramMap.toString());
			inoutService.updateTimecardCancel(paramMap);
			
		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		
		logger.debug(tsId + "/api/cancel e " + rp.toString());

		return rp;
	}
	
//	@Autowired
//	@Qualifier(value="flexibleEmpService")
//	private WtmFlexibleEmpService flexibleEmpService;
//	
//	@Autowired
//	private WtmValidatorService validatorService;
//	
//	@RequestMapping(value="/secret", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody ReturnParam getSecret(@RequestBody Map<String, Object> paramMap, HttpServletRequest request ) throws Exception {
//		ReturnParam rp = new ReturnParam();
//		rp.setSuccess("");
//		try {
//			String apiKey = paramMap.get("apiKey").toString();
//			String secret = paramMap.get("secret").toString();
//			
//			this.certificate(apiKey, secret, request.getRemoteHost());
//		} catch (CertificateException e1) {
//			rp.setFail(e1.getMessage());
//			return rp;
//		}
//		
//		return rp;
//	}
//
//	@Autowired
//	private RestTemplate restTemplate;
//
//	@RequestMapping(value="/callback", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public void callback(HttpServletRequest request , HttpServletResponse response) {
//		System.out.println("444444444444444444444444444444444444444444");
//		
//		Enumeration<String> p = request.getParameterNames();
//        System.out.println("callback param start");
//        while(p.hasMoreElements()) {
//        	String key = p.nextElement();
//        	System.out.println(key + " : " + request.getParameter(key));
//        }
//        
//        String code = request.getParameter("code");
//        System.out.println("callback param end");
//        
//        Enumeration<String> k = request.getHeaderNames();
//        System.out.println("callback header start");
//        while(k.hasMoreElements()) {
//        	String key = k.nextElement();
//        	System.out.println(key + " : " + request.getHeader(key));
//        }
//        System.out.println("callback header end");
//        
//        /*
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(); 
//        factory.setReadTimeout(3000); 
//        factory.setConnectTimeout(3000); 
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build(); 
//        factory.setHttpClient(httpClient); 
//        RestTemplate restTemplate = new RestTemplate(factory);
//        */
//        String url = "http://10.30.30.188:8380/ifo/oauth/token";
//        
//        String clientCredentials = "foo:var";
//      //  String base64ClientCredentials = new String(Base64.encodeBase64(clientCredentials.getBytes()));
//        
//        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
//        param.add("code", code); 
//        param.add("grant_type", "authorization_code"); 
//        param.add("redirect_uri", "http://10.30.30.188/ifw/api/callback"); 
//        param.add("scope", "read"); 
//        
//        
//        HttpHeaders headers = new HttpHeaders();
//       // headers.set("Authorization", "Basic "+base64ClientCredentials);
//        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//
//        /*
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(param, headers);
//         
//        ResponseEntity<Map> res = restTemplate.postForEntity(url, entity, Map.class);
//        
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//			System.out.println(mapper.writeValueAsString(res.getBody()));
//		} catch (JsonProcessingException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//        */
//        
//      try {
//          //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//          //System.out.println(auth.getName());
//          //System.out.println(auth.getPrincipal().toString());
//          HttpSession session = request.getSession();
//          
//          Enumeration<String> sess = session.getAttributeNames();
//          System.out.println("callback session start");
//          while(sess.hasMoreElements()) {
//        	  String sk = sess.nextElement();
//        	  System.out.println(sk + " : " + session.getAttribute(sk)); 
//          }
//          System.out.println("callback session end");
//          
//          //DefaultOAuth2ClientContext client = (DefaultOAuth2ClientContext) session.getAttribute("scopedTarget.oauth2ClientContext");
//          //System.out.println("client.getAccessToken() : " + client.getAccessToken());
//          /*
//         List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//         authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//               
//           Authentication authentication = 
//                   new UsernamePasswordAuthenticationToken("TMP4", "a1234", authorities);
//           
//            SecurityContextHolder.getContext().setAuthentication(authentication); // put that in the security context
//            */
//            response.sendRedirect("/ifw/console/isu");
//      } catch(Exception e) {
//    	  e.printStackTrace();
//         try {
//            ((HttpServletResponse) response).sendRedirect("/login/isu");
//            return;
//         } catch (IOException e1) {
//            e1.printStackTrace();
//         }
//         e.printStackTrace();
//      }
//	} 
//	
//	@RequestMapping(value="/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ReturnParam test(HttpServletRequest request ) throws Exception {
//		ReturnParam rp = new ReturnParam();
//		rp.setSuccess("");
//		flexibleEmpService.createWorkteamEmpData(new Long("1"), "ISU", new Long("5"), "112313");	
//		
//		return rp;
//	}
//	
//	@RequestMapping(value="/worktime/valid", method = RequestMethod.GET)
//	public ReturnParam worktimeValid(@RequestParam(required=true) String apiKey,
//								@RequestParam(required=true) String secret,
//								@RequestParam(required=true) String enterCd,
//								@RequestParam(required=true) String sabun,
//								@RequestParam(required=true) String sdate,
//								@RequestParam(required=true) String edate,
//								@RequestParam(required=false) String applId,
//			HttpServletRequest request ){
//		ReturnParam rp = new ReturnParam();
//		rp.setSuccess("");
//		try {
//			this.certificate(apiKey, secret, request.getRemoteHost());
//		} catch (CertificateException e1) {
//			rp.setFail(e1.getMessage());
//			return rp;
//		}
//		
//		String f = "yyyyMMddHHmm";
//		if(sdate.length() != edate.length()) {
//			rp.setFail("시작시분과 종료시분에 정보의 길이가 맞지 않습니다.");
//			return rp;
//		}
//		
//		if(sdate.length() == 12) {
//			f = "yyyyMMddHHmm";
//		}else if(sdate.length() == 14) {
//			f = "yyyyMMddHHmmss";
//		}else { 
//			rp.setFail("일자 정보의 포맷은 yyyyMMddHHmm 또는 yyyyMMddHHmmss 입니다.");
//			return rp;
//		}
//		Long tenantId = this.getTenantId(apiKey);
//		SimpleDateFormat sdf = new SimpleDateFormat(f);
//		Date sd, ed;
//		try {
//			sd = sdf.parse(sdate);
//			ed = sdf.parse(edate);
//		} catch (ParseException e) { 
//			rp.setFail("일자 정보의 포맷은 yyyyMMddHHmm 또는 yyyyMMddHHmmss 입니다.");
//			return rp;
//		}
//		
//		if(sd.compareTo(ed) > 0) {
//			rp.setFail("시작일자가 종료일자보다 큽니다.");
//			return rp;
//		}
//		
//		
//		rp = validatorService.checkDuplicateWorktime(tenantId, enterCd, sabun, sd, ed, Long.parseLong(applId));
//		
//		return rp;
//	}
//	
//	
//	@RequestMapping(value="/workappl/valid", method = RequestMethod.GET)
//	public ReturnParam workapplValid(@RequestParam(required=true) String apiKey,
//								@RequestParam(required=true) String secret,
//								@RequestParam(required=true) String enterCd,
//								@RequestParam(required=true) String sabun,
//								@RequestParam(required=true) String symd,
//								@RequestParam(required=true) String eymd,
//								@RequestParam(required=false) String applId,
//			HttpServletRequest request ){
//		ReturnParam rp = new ReturnParam();
//		rp.setSuccess("");
//		try {
//			this.certificate(apiKey, secret, request.getRemoteHost());
//		} catch (CertificateException e1) {
//			rp.setFail(e1.getMessage());
//			return rp;
//		}
//		
//		String f = "yyyyMMdd";
//		if(symd.length() != eymd.length()) {
//			rp.setFail("시작시분과 종료시분에 정보의 길이가 맞지 않습니다.");
//			return rp;
//		}
//		
//		if(symd.length() != 8) {
//			rp.setFail("일자 정보의 포맷은 yyyyMMddHHmm 또는 yyyyMMddHHmmss 입니다.");
//			return rp;
//		}
//		Long tenantId = this.getTenantId(apiKey);
//		SimpleDateFormat sdf = new SimpleDateFormat(f);
//		Date sd, ed;
//		try {
//			sd = sdf.parse(symd);
//			ed = sdf.parse(eymd);
//		} catch (ParseException e) { 
//			rp.setFail("일자 정보의 포맷은 yyyyMMdd 입니다.");
//			return rp;
//		}
//		
//		if(sd.compareTo(ed) > 0) {
//			rp.setFail("시작일자가 종료일자보다 큽니다.");
//			return rp;
//		}
//		rp = validatorService.checkDuplicateFlexibleWork(tenantId, enterCd, sabun, symd, eymd, Long.parseLong(applId));
//		
//		return rp;
//	}
//	
//	@RequestMapping(value="/taaappl/valid", method = RequestMethod.GET)
//	public ReturnParam taaApplValid(@RequestParam(required=true) String apiKey,
//								@RequestParam(required=true) String secret,
//								@RequestParam(required=true) String enterCd,
//								@RequestParam(required=true) String taaCd,
//								@RequestParam(required=true) String sabun,
//								@RequestParam(required=true) String symd,
//								@RequestParam(required=false) String shm,
//								@RequestParam(required=true) String eymd,
//								@RequestParam(required=false) String ehm,
//								@RequestParam(required=false) String applId,
//								@RequestParam(required=false) String locale,
//			HttpServletRequest request ){
//		ReturnParam rp = new ReturnParam();
//		rp.setSuccess("");
//		try {
//			this.certificate(apiKey, secret, request.getRemoteHost());
//		} catch (CertificateException e1) {
//			rp.setFail(e1.getMessage());
//			return rp;
//		}
//		
//		String f = "yyyyMMdd";
//		if(symd.length() != eymd.length()) {
//			rp.setFail("시작시분과 종료시분에 정보의 길이가 맞지 않습니다.");
//			return rp;
//		}
//		
//		if(symd.length() != 8) {
//			rp.setFail("일자 정보의 포맷은 yyyyMMdd 입니다.");
//			return rp;
//		}
//		Long tenantId = this.getTenantId(apiKey);
//		SimpleDateFormat sdf = new SimpleDateFormat(f);
//		Date sd, ed;
//		try {
//			sd = sdf.parse(symd);
//			ed = sdf.parse(eymd);
//		} catch (ParseException e) { 
//			rp.setFail("일자 정보의 포맷은 yyyyMMdd 입니다.");
//			return rp;
//		}
//		
//		if(sd.compareTo(ed) > 0) {
//			rp.setFail("시작일자가 종료일자보다 큽니다.");
//			return rp;
//		}
//		
//		rp = validatorService.validTaa(tenantId, enterCd, sabun, WtmApplService.TIME_TYPE_TAA, taaCd, symd, shm, eymd, ehm, Long.parseLong(applId), locale);
//		
//		return rp;
//	}
//	
}
