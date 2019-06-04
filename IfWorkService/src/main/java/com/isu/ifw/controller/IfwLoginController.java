package com.isu.ifw.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.LoginService;
import com.isu.ifw.vo.Login;

@RestController
public class IfwLoginController {

	@Autowired
	LoginService loginService;
	
	//채용사이트에서 로그인
		@RequestMapping(value="/login", method=RequestMethod.POST)
		public Map<String, Object> recruitLogin(@RequestBody Map<String, Object> paramMap) throws Exception {

			ModelAndView mv = new ModelAndView();
			mv.setViewName("jsonView");

			String companyCd = paramMap.get("companyCd") + "";
			String loginId = paramMap.get("loginId") + "";
			String password = paramMap.get("password") + "";
			
			paramMap.put("loginEnterCd", companyCd);
			paramMap.put("loginUserId", loginId);
			paramMap.put("loginPassword", password);

			//Map<String, String> loginTryCntMap = (Map<String, String>) loginService.loginTryCnt(companyCd, loginId, password);
			
			ObjectMapper mapper = new ObjectMapper();
			//System.out.println(mapper.writeValueAsString(loginTryCntMap));
			
			Map<String, Object> resultMap = new HashMap<>();
			/**
			 * 해당 ID가 존재 하지 않음
			 */
			/*
			if (loginTryCntMap.get("ID_EXST").equals("N")) {
				resultMap.put("isUser", "notExist");
				resultMap.put("message", "ID가 없습니다.");
				return resultMap;

			}
			*/
			
			/**
			 * ID가 잠김
			 */
			/*
			if (loginTryCntMap.get("ROCKING_YN").equals("Y")) {
				resultMap.put("isUser", "rocking");
				resultMap.put("message", "ID가 잠김상태 입니다.");
				return resultMap;
			}
			*/

			/**
			 * 로그인 실패 횟수 Over
			 */
			/*
			if (loginTryCntMap.get("LOGIN_FAIL_CNT_YN").equals("Y")) {
				resultMap.put("isUser", "cntOver");
				resultMap.put("message", "로그인 실패 횟수 초과");
				return resultMap;
			}
			*/

			/**
			 * 비밀번호가 틀림
			 */
			/*
			if (loginTryCntMap.get("PSWD_CLCT").equals("Y")) {

				int loginFailCnt = Integer.parseInt(loginTryCntMap.get("LOGIN_FAIL_CNT") == null ? "" : String.valueOf(loginTryCntMap.get("LOGIN_FAIL_CNT"))); // 로그인 실패 횟수

				paramMap.put("loginFailCnt", loginFailCnt);
//				loginService.saveLoingFailCnt(paramMap);
				resultMap.put("isUser", "notMatch");
				resultMap.put("loginFailCntStd", loginTryCntMap.get("LOGIN_FAIL_CNT_STD"));
				resultMap.put("loginFailCnt", loginFailCnt + 1);
				resultMap.put("message", "비밀번호가 틀립니다.");
				return resultMap;
			}
			*/
			//String ssnLocaleCd, String localeCd, String baseLang, String loginUserId, String loginPassword, String ssnSso
			//String ssnLocaleCd = paramMap.get("ssnLocaleCd") + "";
			
			Map<?, ?> mobilcCheckd = mobilcCheck(paramMap);

			if(mobilcCheckd != null) {
				if(mobilcCheckd.get("success").toString().equals("false")) { 
		  			resultMap.put("isUser", "LoginFail");
		  			resultMap.put("message", mobilcCheckd.get("msg"));
		  			return resultMap;
				}
			}
			
			String localeCd = paramMap.get("localeCd") + "";
			String baseLang = paramMap.get("baseLang") + "";
			String loginUserId = paramMap.get("loginId") + "";
			String loginPassword  = paramMap.get("password") + "";
			String ssnSso  = paramMap.get("ssnSso") + "";

			Login loginUserMap = loginService.loginUser(localeCd, localeCd, baseLang, companyCd, loginUserId, loginPassword, ssnSso);
			//System.out.println("loginUserMap : " + mapper.writeValueAsString(loginUserMap));
	  		/**
	  		 * 사용자 정보가 없음
	  		 */
	  		if (loginUserMap == null) {
	  			resultMap.put("isUser", "noLogin");
	  			resultMap.put("message", "사용자 정보가 없습니다.");
	  			return resultMap;
	  		}

	  		resultMap.put("isUser", "exist");
	  		resultMap.put("empKey", paramMap.get("companyCd")+"@"+paramMap.get("loginId"));
	  		resultMap.put("userData", loginUserMap);
	  		resultMap.put("message", "");

			return resultMap;
		}
 
		
		public Map<?, ?> mobilcCheck(@RequestParam Map<String, Object> paramMap) throws Exception {

			String uId = paramMap.get("loginUserId").toString();
			String uPwd = paramMap.get("loginPassword").toString();
			String addr = "http://m.taeyoung.com/m/servlet/mobile?cmd=mkm_login&user_id=" + uId + "&password=" + uPwd;
			StringBuffer jsonHtml = new StringBuffer();
			Map<String, String> map = new HashMap<>();

			try {
				URL u = new URL(addr);
				InputStream uis = u.openStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(uis, "UTF-8"));

				String line = null;

				while ((line = br.readLine()) != null) {
					jsonHtml.append(line + "\n");
				}

				br.close();
				uis.close();

				String data = jsonHtml.toString();
				String [] array;
				array = data.split("\"");
				int k = 0;

				for(int i = 0; i < array.length; i++) {
					if(array[i].equals("key")) {
						map.put(array[i], array[i + 2]);
					}

					if(array[i].equals("msg")) {
						map.put(array[i], array[i + 2]);
					}

					if(array[i].equals("url")) {
						map.put(array[i], array[i + 2]);
					}

					if(array[i].equals("success")) {
						map.put(array[i], array[i + 1].substring(1).replace("}", "").trim());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return map;
		}
}
