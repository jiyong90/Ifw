package com.isu.ifw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.AppointmentService;
import com.isu.ifw.service.StdManagementService;
import com.isu.ifw.vo.StdManagement;

@RestController
public class AppointmentController {
	
	@Autowired
	StdManagementService stdManagementService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	/**
	 * 발령 처리
	 * @param session
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendApplicantInfo", method=RequestMethod.POST)
	public Map<String, Object> sendApplicantInfo(@RequestBody Map<String, Object> paramMap) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String[] userKeySpl = paramMap.get("userKey").toString().split("@");

		String enterCd = userKeySpl[0]; //enterCd
		String sabun = userKeySpl[1]; //sabun
		paramMap.put("ssnLocaleCd", "ko_KR");
		paramMap.put("loginEnterCd", enterCd);
		paramMap.put("loginUserId", sabun);
		
		StdManagement keyCheck = stdManagementService.getKeyCheckResult(paramMap);
		if ("SUCCESS".equals(keyCheck.getApikeyResult()) && "SUCCESS".equals(keyCheck.getSecretResult())) {
			Map<String, Object> rvMap = (Map<String, Object>) paramMap.get("resumeValues");
			String annoId = rvMap.get("annoId").toString();
			String annoTitle = rvMap.get("annoTitle").toString();
			Map<String, Object> applicants = (Map<String, Object>) rvMap.get("applicants");
			
			appointmentService.saveApplicants(annoId, annoTitle, applicants);
			
			resultMap.put("status", "SUCCESS");
		} else {
			resultMap.put("status", "FAIL");
			resultMap.put("message", "APIKEY OR SECRET ERROR");
		}
		
		return resultMap;
	}
	
	@RequestMapping("/finalPass")
	public String finalPass(@RequestBody Map<String, Object> paramMap) throws Exception {
//
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("jsonView");

		HashMap<String, Object> mapElement = new HashMap<String, Object>();

		
		Map<String, Object> resumeValues = (Map<String, Object>) paramMap.get("resumeValues");
		Map<String, Object> applicants = (Map<String, Object>) resumeValues.get("applicants");
		String user[] = paramMap.get("userKey").toString().split("@");
		String annoId = resumeValues.get("annoId").toString();
		String annoTitle = resumeValues.get("annoTitle").toString();

		mapElement.put("recSeq", annoId); //공고 ID
		mapElement.put("recNm", annoTitle); //공고 명
		mapElement.put("enterCd", user[0]); //채용 로그인 사용자
		mapElement.put("sabun", user[1]); //채용 로그인 사용자
 
		for(String applId : applicants.keySet()) {
		
			Map<String, Object> applicant = (Map<String, Object>) applicants.get(applId);
			 

			//기본사항
			List<Map<String, Object>> basicpart = new ArrayList<Map<String, Object>>();

			if(applicant.get("basicpart") != null) {
				basicpart = (List<Map<String, Object>>) applicant.get("basicpart");
			}

			//연락처사항 
			List<Map<String, Object>> basicetcpart = new ArrayList<Map<String, Object>>();

			if(applicant.get("basicetcpart") != null) {
				basicetcpart = (List<Map<String, Object>>) applicant.get("basicetcpart");
			}

			//고등학교
			List<Map<String, Object>> highschoolpart = new ArrayList<Map<String, Object>>();

			if(applicant.get("highschoolpart") != null) {
				highschoolpart = (List<Map<String, Object>>) applicant.get("highschoolpart");
			}

			//대학교  
			List<Map<String, Object>> universitypart = new ArrayList<Map<String, Object>>();
			
			if(applicant.get("universitypart") != null) {
				universitypart = (List<Map<String, Object>>) applicant.get("universitypart");
			}

			//경력사항
			List<Map<String, Object>> careerpart = new ArrayList<Map<String, Object>>();
			

			if(applicant.get("careerpart") != null) {
				careerpart = (List<Map<String, Object>>) applicant.get("careerpart");
			}

			//병역사항 
			List<Map<String, Object>> militarypart = new ArrayList<Map<String, Object>>();
			if(applicant.get("militarypart") != null) {
				militarypart = (List<Map<String, Object>>) applicant.get("militarypart");
			}

			//기본정보 delete
			appointmentService.deleteBasic(mapElement);
			//연락처 delete
			appointmentService.deleteContact(mapElement);
			//학력 delete
			appointmentService.deleteSchool(mapElement);
			//경력 delete
			appointmentService.deleteCareer(mapElement);
			//병역 delete
			appointmentService.deleteMilitary(mapElement);
			//채용공고 delete
			appointmentService.deleteAnno(mapElement);

			//기본사항
			for (Map<String, Object> basicMap : basicpart) {
//				if(j > 0) {
//					break;
//				}
				//HashMap<String, String> basicMap = new HashMap<String, String>();

				basicMap.put("enterCd", user[0]);
				basicMap.put("sabun", user[1]);
				basicMap.put("applId", applId);
				basicMap.put("recSeq", annoId);

				basicMap.put("ename", basicMap.get("eName")); //영문성명
				basicMap.put("name", basicMap.get("name"));	 //성명
				basicMap.put("birthYmd", basicMap.get("birthDate")); //생년월일 YYYYMMDD
				basicMap.put("mailAddr", basicMap.get("email")); //개인이메일

				//기본정보 테이블에 insert
				appointmentService.saveBasic(basicMap);
			}

			//연락처
			for (Map<String, Object> basicetcMap : basicetcpart) {

				basicetcMap.put("enterCd", user[0]);
				basicetcMap.put("sabun", user[1]);
				basicetcMap.put("applId", applId);
				basicetcMap.put("recSeq", annoId);

				basicetcMap.put("mobileNo", basicetcMap.get("mobilePhone")); //휴대폰번호
				basicetcMap.put("telNo", basicetcMap.get("tel"));	 //자택전화번호

				//연락처 테이블에 insert
				appointmentService.saveContact(basicetcMap);
			}

			//고등학교 
			for (Map<String, Object> highschoolMap : highschoolpart) {
				Map<String, Object> schoolJo = null;
				if(highschoolMap.containsKey("school") && highschoolMap.get("school") != null && !highschoolMap.get("school").equals("")) {
					schoolJo = (Map<String, Object>) highschoolMap.get("school");
				}
				Map<String, Object> hDurationJo = null;
				if(highschoolMap.containsKey("hDuration") && highschoolMap.get("hDuration") != null && !highschoolMap.get("hDuration").equals("")) {
					hDurationJo = (Map<String, Object>) highschoolMap.get("hDuration");
				}


				highschoolMap.put("enterCd", user[0]);
				highschoolMap.put("sabun", user[1]);
				highschoolMap.put("applId", applId);
				highschoolMap.put("recSeq", annoId);

				highschoolMap.put("acaType", highschoolMap.get("lastSchool")); //최종학력여부(true, false)
				highschoolMap.put("acaSchNm", (schoolJo!=null)?schoolJo.get("schoolName"):""); //학교명
				highschoolMap.put("acaPlaceNm", highschoolMap.get("region")); //소재지
				highschoolMap.put("acaYn",  (hDurationJo != null)?hDurationJo.get("graduated"):"" ); //졸업구분(졸업, 재학, 수료, 중퇴)
				highschoolMap.put("acaSYm", (hDurationJo != null)?hDurationJo.get("start"):"" ); //입학일자
				highschoolMap.put("acaEYm",  (hDurationJo != null)?hDurationJo.get("end"):""); //졸업일자

				//학력 테이블에 insert
				appointmentService.saveHighSchool(highschoolMap);
			}

			//대학교
			for (Map<String, Object> universityMap : universitypart) {
				Map<String, Object> schoolJo = null;
				if(universityMap.containsKey("school") && universityMap.get("school") != null && !universityMap.get("school").equals("")) {
					schoolJo = (Map<String, Object>) universityMap.get("school");
				}
				Map<String, Object> uDurationJo =  null;
				if(universityMap.containsKey("uDuration") && universityMap.get("uDuration") != null && !universityMap.get("uDuration").equals("")) {
					uDurationJo = (Map<String, Object>) universityMap.get("uDuration");
				}
				Map<String, Object> majorJo = null;
				if(universityMap.containsKey("major") && universityMap.get("major") != null && !universityMap.get("major").equals("")) {
					majorJo = (Map<String, Object>) universityMap.get("major");
				}
				Map<String, Object> scoreJo =  null;
				if(universityMap.containsKey("score") && universityMap.get("score") != null && !universityMap.get("score").equals("")) {
					scoreJo =(Map<String, Object>) universityMap.get("score");
				}

				universityMap.put("enterCd", user[0]);
				universityMap.put("sabun", user[1]);
				universityMap.put("applId", applId);
				universityMap.put("recSeq", annoId);

				universityMap.put("acaType", universityMap.get("lastSchool")); //최종학력여부(true, false)
				universityMap.put("eType", universityMap.get( "schoolYn")); //본교여부(true, false)
				universityMap.put("acaSchNm", schoolJo.get("schoolName")); //학교명
				universityMap.put("acaPlaceNm", universityMap.get("region")); //소재지
				universityMap.put("acaDegCd", universityMap.get("univLevel")); //학력(전문학사,학사,석사,박사)
				universityMap.put("acaYn", (uDurationJo.get("graduated") != null)?uDurationJo.get("graduated"):""); //졸업구분(졸업, 재학, 수료, 중퇴)
				universityMap.put("entryType", (uDurationJo.get("admission") != null)?uDurationJo.get("admission") : ""); //입학구분(입학, 편입)
				universityMap.put("acaSYm", (uDurationJo.get("start") != null)?uDurationJo.get("start"):""); //입학일자
				universityMap.put("acaEYm", uDurationJo.get("end")); //졸업일자
				universityMap.put("acamajNm", (majorJo.get("name") != null)?majorJo.get("name"):""); //전공명
				universityMap.put("acaPointManjum", (scoreJo.get("den") != null)?scoreJo.get("den"):""); //학점만점
				universityMap.put("acaPoint", (scoreJo.get("num") != null)?scoreJo.get("num"):""); //학점

				//학력 테이블에 insert
				appointmentService.saveUniversitySchool(universityMap);
			}

			//경력
			for (Map<String, Object> careerMap : careerpart) {
				Map<String, Object> durationJo = null;
				if(careerMap.containsKey("duration") && careerMap.get("duration") != null && !careerMap.get("duration").equals("")) {
					durationJo = (Map<String, Object>) careerMap.get("duration");
				}
				careerMap.put("enterCd", user[0]);
				careerMap.put("sabun", user[1]);
				careerMap.put("applId", applId);
				careerMap.put("recSeq", annoId);

				careerMap.put("sdate", (durationJo != null)?durationJo.get("start"):""); //입사일
				careerMap.put("edate", (durationJo != null)?durationJo.get("end"):""); //퇴사일
				careerMap.put("cmpNm", (careerMap.get("companyName") != null)?careerMap.get("companyName"):""); //직장명
				careerMap.put("jobNm", (careerMap.get("companyJob") != null)?careerMap.get( "companyJob"):""); //담당업무

				//경력 테이블에 insert
				appointmentService.saveCareer(careerMap);
			}

			//병역 
			for (Map<String, Object> militaryMap : militarypart) {

				Map<String, Object> militaryRangeJo = null;
				
				if(militaryMap.containsKey("militaryRange") && militaryMap.get("militaryRange") != null && !militaryMap.get("militaryRange").equals("")) {
					militaryRangeJo = (Map<String, Object>) militaryMap.get("militaryRange");
				}
				
 
				militaryMap.put("enterCd", user[0]);
				militaryMap.put("sabun", user[1]);
				militaryMap.put("applId", applId);
				militaryMap.put("recSeq", annoId);

				militaryMap.put("armySYmd", (militaryRangeJo != null)?militaryRangeJo.get("start"):""); //복무시작일
				militaryMap.put("armyEYmd", (militaryRangeJo != null)?militaryRangeJo.get("end"):""); //복무종료일

				militaryMap.put("transferCd", militaryMap.get("serviceKind")); //병역구분(필 or 미필 or 면제 or 비대상(여성,외국인))
				//militaryMap.put("armyDCd", getJsonCheckData(militarypart.getJSONObject(j), "militaryClass")); //병과
				militaryMap.put("armyGradeCd", militaryMap.get( "militaryLevel")); //계급
				militaryMap.put("armyMajor", militaryMap.get( "militaryReason")); //면제사유
				militaryMap.put("dischargeCd", militaryMap.get( "militaryKind")); //전역구분
				militaryMap.put("armyCd", militaryMap.get( "militaryType")); //군별

				//병역 테이블에 insert
				appointmentService.saveMilitary(militaryMap);
			}
		}

		//채용 공고 등록
		appointmentService.saveAnno(mapElement);

		return "SUCCESS";
	}
 
}
