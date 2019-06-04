package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.mapper.AppointmentMapper;

@Service("AppointmentService")
public class AppointmentService {
	
	@Autowired
	AppointmentMapper appointmentMapper;
	
	/**
	 * 복리후생 알림을 보내기 위한 항목을 가져와
	 * 해당 항목에 속한 대상자 정보
	 * @throws Exception 
	 */
	@Transactional
	public void saveApplicants(String annoId, String annoTitle, Map<String,Object> applicants) throws Exception{

		ObjectMapper mapper = new ObjectMapper();
		if(applicants != null) {
			Map<String, Object> annoMap = new HashMap<>();
			annoMap.put("annoId", annoId);
			annoMap.put("annoTitle", annoTitle);
			
			Map<String, Object> convertMap = new HashMap<String, Object>(); 
			try {
				//int resultCnt = dao.update("mergeTSTF501", annoMap);
				int resultCnt = appointmentMapper.saveAnnouncementInfomation(annoMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for(String k : applicants.keySet()) {
				String applId = k;
				Map<String, Object> itemMap = (Map<String, Object>) applicants.get(k);
				//기본정보 & 보훈 & 장애 *********************************************************************************
				Map<String, Object> tsft502Map = new HashMap<String, Object>();
				tsft502Map.put("annoId", annoId);
				tsft502Map.put("applId", applId);
				
				if(itemMap.containsKey("basicpart") && itemMap.get("basicpart") != null && !itemMap.get("basicpart").equals("")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) itemMap.get("basicpart");
					if(list.size() > 0) {
						for(Map<String, Object> m : list) {
							if(m != null && !m.isEmpty()) {
								if(m.containsKey("name") && m.get("name") != null) {
									tsft502Map.put("name", m.get("name"));
								}else {
									tsft502Map.put("name", "");
								}
								if(m.containsKey("eName") && m.get("eName") != null) {
									tsft502Map.put("ename", m.get("eName"));
								}else {
									tsft502Map.put("ename", "");
								}
								if(m.containsKey("cName") && m.get("cName") != null) {
									tsft502Map.put("cname", m.get("cName"));
								}else {
									tsft502Map.put("cname", "");
								}
								if(m.containsKey("birthDate") && m.get("birthDate") != null) {
									tsft502Map.put("birthDate", m.get("birthDate"));
								}else {
									tsft502Map.put("birthDate", "");
								}
								if(m.containsKey("gender") && m.get("gender") != null) {
									tsft502Map.put("gender", m.get("gender"));
								}else {
									tsft502Map.put("gender", "");
								}
								if(m.containsKey("marriageYn") && m.get("marriageYn") != null) {
									tsft502Map.put("isMarried", m.get("marriageYn"));
								}else {
									tsft502Map.put("isMarried", "");
								}
							}
						}
					}
				}else {

					tsft502Map.put("name", "");
					tsft502Map.put("ename", "");
					tsft502Map.put("cname", "");
					tsft502Map.put("birthDate", "");
					tsft502Map.put("gender", "");
					tsft502Map.put("isMarried", "");
				}
				if(itemMap.containsKey("basicetcpart") && itemMap.get("basicetcpart") != null && !itemMap.get("basicetcpart").equals("")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) itemMap.get("basicetcpart");
					if(list.size() > 0) {
						for(Map<String, Object> m : list) {
							if(m != null && !m.isEmpty()) {
								//주소
								if(m.containsKey("address") && m.get("address") != null && !m.get("address").equals("")) {
									Map<String, Object> addrMap = (Map<String, Object>) m.get("address");
									if(addrMap.containsKey("zipNo") && addrMap.get("zipNo") != null) {
										tsft502Map.put("addrZipcode", addrMap.get("zipNo"));
									}else {
										tsft502Map.put("addrZipcode", "");
									}
									if(addrMap.containsKey("roadAddrPart1") && addrMap.get("roadAddrPart1") != null) {
										tsft502Map.put("addrPart1", addrMap.get("roadAddrPart1"));
									}else {
										tsft502Map.put("addrPart1", "");
									}
									if(addrMap.containsKey("roadAddrPart2") && addrMap.get("roadAddrPart2") != null) {
										tsft502Map.put("addrPart2", addrMap.get("roadAddrPart2"));
									}else {
										tsft502Map.put("addrPart2", "");
									}
									if(addrMap.containsKey("addrDetail") && addrMap.get("addrDetail") != null) {
										tsft502Map.put("addrDetail", addrMap.get("addrDetail"));
									}else {
										tsft502Map.put("addrDetail", "");
									}
								}else {
									tsft502Map.put("addrZipcode", "");
									tsft502Map.put("addrPart1", "");
									tsft502Map.put("addrPart2", "");
									tsft502Map.put("addrDetail", "");  
								}
								//연락처
								if(m.containsKey("mobilePhone") && m.get("mobilePhone") != null ) {
									tsft502Map.put("phone", m.get("mobilePhone"));
								}else {
									tsft502Map.put("phone", "");
								}
								//이메일
								if(m.containsKey("email") && m.get("email") != null ) {
									tsft502Map.put("email", m.get("email"));
								}else { 
									tsft502Map.put("email", "");
								}
									
							} 
						}
					} 
					
				}else {
					tsft502Map.put("addrZipcode", "");
					tsft502Map.put("addrPart1", "");
					tsft502Map.put("addrPart2", "");
					tsft502Map.put("addrDetail", "");  
					tsft502Map.put("phone", "");
					tsft502Map.put("email", "");
				}
				//장애 보훈
				if(itemMap.containsKey("etcpart") && itemMap.get("etcpart") != null && !itemMap.get("etcpart").equals("")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) itemMap.get("etcpart");
					if(list.size() > 0) {
						for(Map<String, Object> m : list) {
							if(m != null && !m.isEmpty()) {
								if(m.containsKey("isHandicap") && m.get("isHandicap") != null) {
									tsft502Map.put("isHandicap", m.get("isHandicap"));
								}else {
									tsft502Map.put("isHandicap", "");
								}
								if(m.containsKey("isLesion") && m.get("isLesion") != null) {
									tsft502Map.put("isLession", m.get("isLesion"));
								}else {
									tsft502Map.put("isLession", "");
								}
							}
						}
					}
				}else { 
					tsft502Map.put("isHandicap", "");
					tsft502Map.put("isLession", ""); 
				}
				List<Map<String, Object>> mergeRows = new ArrayList<>();
				mergeRows.add(tsft502Map);
				System.out.println("tsft502Map : " + mapper.writeValueAsString(tsft502Map));
				convertMap = new HashMap<String, Object>();
				convertMap.put("mergeRows", mergeRows);
				try {
					//int resultCnt = saveApplicantInfomation("mergeTSTF502", convertMap);
					int resultCnt = appointmentMapper.saveBasicInfomation(convertMap);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				// 기본정보 & 보훈 & 장애 END *********************************************************************************
				//가족
				List<Map<String, Object>> tsft503List = new ArrayList<>();
				if(itemMap.containsKey("familypart") && itemMap.get("familypart") != null && !itemMap.get("familypart").equals("")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) itemMap.get("familypart");
					if(list.size() > 0) {
						for(Map<String, Object> m : list) {
							Map<String, Object> tsft503Map = new HashMap<String, Object>();
							tsft503Map.put("applId",applId);
							if(m != null && !m.isEmpty()) {
								if(m.containsKey("familyCareer") && m.get("familyCareer") != null) {
									tsft503Map.put("famCompany", m.get("familyCareer"));
								}else {
									tsft503Map.put("famCompany", "");
								}
								if(m.containsKey("familyRelation") && m.get("familyRelation") != null) {
									tsft503Map.put("famRel", m.get("familyRelation"));
								}else {
									tsft503Map.put("famRel", "");
								}
								if(m.containsKey("familyName") && m.get("familyName") != null) {
									tsft503Map.put("famName", m.get("familyName"));
								}else {
									tsft503Map.put("famName", "");
								}
								if(m.containsKey("familyAge") && m.get("familyAge") != null) {
									tsft503Map.put("famAge", m.get("familyAge"));
								}else {
									tsft503Map.put("famAge", "");
								}
								if(m.containsKey("familyRank") && m.get("familyRank") != null) {
									tsft503Map.put("famCompanyClass", m.get("familyRank"));
								}else {
									tsft503Map.put("famCompanyClass", "");
								}
								if(m.containsKey("familyTogetherYn") && m.get("familyTogetherYn") != null) {
									tsft503Map.put("famIsTogether", m.get("familyTogetherYn"));
								}else {
									tsft503Map.put("famIsTogether", "");
								}
							}else { 
								
								tsft503Map.put("famCompanyClass", "");
								tsft503Map.put("famRel", "");
								tsft503Map.put("famName", "");
								tsft503Map.put("famAge", "");
								tsft503Map.put("famCompany", "");
								tsft503Map.put("famIsTogether", "");
							}
							tsft503List.add(tsft503Map);
						}
					}
				}else {
					Map<String, Object> tsft503Map = new HashMap<String, Object>();
					tsft503Map.put("applId",applId);
					tsft503Map.put("famCompanyClass", "");
					tsft503Map.put("famRel", "");
					tsft503Map.put("famName", "");
					tsft503Map.put("famAge", "");
					tsft503Map.put("famCompany", "");
					tsft503Map.put("famIsTogether", "");
					tsft503List.add(tsft503Map);
				}
				convertMap = new HashMap<String, Object>();
				convertMap.put("mergeRows", tsft503List);
				try {
					//int resultCnt = saveApplicantInfomation("mergeTSTF503", convertMap);
					int resultCnt = appointmentMapper.saveFamilyInfomation(convertMap);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
				//가족 END *********************************************************************************
				
				//학력
				List<Map<String, Object>> tsft504List = new ArrayList<>();
				if(itemMap.containsKey("highschoolpart") && itemMap.get("highschoolpart") != null && !itemMap.get("highschoolpart").equals("")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) itemMap.get("highschoolpart");
					if(list.size() > 0) {
						for(Map<String, Object> m : list) {
							Map<String, Object> tsft504Map = new HashMap<String, Object>();
							tsft504Map.put("applId",applId);
							tsft504Map.put("schGubun", "H");
							if(m != null && !m.isEmpty()) {
								if(m.containsKey("hDuration") && m.get("hDuration") != null && !m.get("hDuration").equals("")) {
									Map<String, Object> hDurationMap = (Map<String, Object>) m.get("hDuration");
									if(hDurationMap != null && !hDurationMap.isEmpty()) {
										if(hDurationMap.containsKey("start") && hDurationMap.get("start") != null){
											tsft504Map.put("schStartdate", hDurationMap.get("start"));
										}else {
											tsft504Map.put("schStartdate", "");
										}
										if(hDurationMap.containsKey("end") && hDurationMap.get("end") != null){
											tsft504Map.put("schEnddate", hDurationMap.get("end"));
										}else {
											tsft504Map.put("schEnddate", "");
										}
									}else {
										tsft504Map.put("schStartdate", "");
										tsft504Map.put("schEnddate", "");
									}
								}else {
									tsft504Map.put("schStartdate", "");
									tsft504Map.put("schEnddate", "");
								} 
								if(m.containsKey("school") && m.get("school") != null && !m.get("school").equals("")) {
									Map<String, Object> schoolMap = (Map<String, Object>) m.get("school");
									if(schoolMap != null && !schoolMap.isEmpty()) {
										if(schoolMap.containsKey("schoolName") && schoolMap.get("schoolName") != null){
											tsft504Map.put("schName", schoolMap.get("schoolName"));
										}else {
											tsft504Map.put("schName", "");
										}
									}else {
										tsft504Map.put("schName", ""); 
									}
								}else {
									tsft504Map.put("schName", "");
								} 
								
								if(m.containsKey("schoolLine") && m.get("schoolLine") != null) {
									tsft504Map.put("schMajor", m.get("schoolLine"));
								}else {
									tsft504Map.put("schMajor", "");
								}

								//고등학교는 주야/ 학점 없음
								tsft504Map.put("schGrade", "");
								tsft504Map.put("schDnl", "");
								tsft504Map.put("schClass", "");
								tsft504Map.put("note", "");
							}else { 
								tsft504Map.put("schName", "");
								tsft504Map.put("schMajor", "");
								tsft504Map.put("schStartdate", "");
								tsft504Map.put("schEnddate", "");
								tsft504Map.put("schGrade", "");
								tsft504Map.put("schClass", "");
								tsft504Map.put("schDnl", "");
								tsft504Map.put("note", "");
							}
							tsft504List.add(tsft504Map);
						}
					}
				}else {
					Map<String, Object> tsft504Map = new HashMap<String, Object>();
					tsft504Map.put("applId",applId);
					tsft504Map.put("schGubun", "H");
					tsft504Map.put("schName", "");
					tsft504Map.put("schMajor", "");
					tsft504Map.put("schStartdate", "");
					tsft504Map.put("schEnddate", "");
					tsft504Map.put("schGrade", "");
					tsft504Map.put("schClass", "");
					tsft504Map.put("schDnl", "");
					tsft504Map.put("note", "");
					tsft504List.add(tsft504Map);
				}
				//대학교
				if(itemMap.containsKey("universitypart") && itemMap.get("universitypart") != null && !itemMap.get("universitypart").equals("")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) itemMap.get("universitypart");
					if(list.size() > 0) {
						for(Map<String, Object> m : list) {
							Map<String, Object> tsft504Map = new HashMap<String, Object>();
							tsft504Map.put("applId",applId);
							tsft504Map.put("schGubun", "U");
							if(m != null && !m.isEmpty()) {
								if(m.containsKey("uDuration") && m.get("uDuration") != null && !m.get("uDuration").equals("")) {
									Map<String, Object> uDurationMap = (Map<String, Object>) m.get("uDuration");
									if(uDurationMap != null && !uDurationMap.isEmpty()) {
										if(uDurationMap.containsKey("start") && uDurationMap.get("start") != null){
											tsft504Map.put("schStartdate", uDurationMap.get("start"));
										}else {
											tsft504Map.put("schStartdate", "");
										}
										if(uDurationMap.containsKey("end") && uDurationMap.get("end") != null){
											tsft504Map.put("schEnddate", uDurationMap.get("end"));
										}else {
											tsft504Map.put("schEnddate", "");
										}
									}else {
										tsft504Map.put("schStartdate", "");
										tsft504Map.put("schEnddate", "");
									}
								}else {
									tsft504Map.put("schStartdate", "");
									tsft504Map.put("schEnddate", "");
								} 
								if(m.containsKey("school") && m.get("school") != null && !m.get("school").equals("")) {
									Map<String, Object> schoolMap = (Map<String, Object>) m.get("school");
									if(schoolMap != null && !schoolMap.isEmpty()) {
										if(schoolMap.containsKey("schoolName") && schoolMap.get("schoolName") != null){
											tsft504Map.put("schName", schoolMap.get("schoolName"));
										}else {
											tsft504Map.put("schName", "");
										}
									}else {
										tsft504Map.put("schName", ""); 
									}
								}else {
									tsft504Map.put("schName", "");
								} 
								
								if(m.containsKey("major") && m.get("major") != null && !m.get("major").equals("")) {
									Map<String, Object> majorMap = (Map<String, Object>) m.get("major");
									if(majorMap != null && !majorMap.isEmpty()) {
										if(majorMap.containsKey("name") && majorMap.get("name") != null){
											tsft504Map.put("schMajor", majorMap.get("name"));
										}else {
											tsft504Map.put("schMajor", "");
										}
									}else {
										tsft504Map.put("schMajor", ""); 
									}
								}else {
									tsft504Map.put("schMajor", "");
								} 
								
								if(m.containsKey("score") && m.get("score") != null && !m.get("score").equals("")) {
									Map<String, Object> scoreMap = (Map<String, Object>) m.get("score");
									if(scoreMap != null && !scoreMap.isEmpty()) {
										String schGrade = ""; 
										if(scoreMap.containsKey("num") && scoreMap.get("num") != null){
											schGrade = scoreMap.get("num").toString();
										}
										if(scoreMap.containsKey("den") && scoreMap.get("den") != null){
											schGrade = schGrade + "/" + scoreMap.get("den").toString();
										}
										tsft504Map.put("schGrade", schGrade); 
									}else {
										tsft504Map.put("schGrade", ""); 
									}
								}else {
									tsft504Map.put("schGrade", "");
								} 
								
								if(m.containsKey("univLevel") && m.get("univLevel") != null) {
									tsft504Map.put("schClass", m.get("univLevel"));
								}else {
									tsft504Map.put("schClass", "");
								}
								
								if(m.containsKey("daynnight") && m.get("daynnight") != null) {
									tsft504Map.put("schDnl", m.get("daynnight"));
								}else {
									tsft504Map.put("schDnl", "");
								}
  
								tsft504Map.put("note", "");
							}else { 
								tsft504Map.put("schName", "");
								tsft504Map.put("schMajor", "");
								tsft504Map.put("schStartdate", "");
								tsft504Map.put("schEnddate", "");
								tsft504Map.put("schGrade", "");
								tsft504Map.put("schClass", "");
								tsft504Map.put("schDnl", "");
								tsft504Map.put("note", "");
							}
							tsft504List.add(tsft504Map);
						}
					}
				}else {
					Map<String, Object> tsft504Map = new HashMap<String, Object>();
					tsft504Map.put("applId",applId);
					tsft504Map.put("schGubun", "U");
					tsft504Map.put("schName", "");
					tsft504Map.put("schMajor", "");
					tsft504Map.put("schStartdate", "");
					tsft504Map.put("schEnddate", "");
					tsft504Map.put("schGrade", "");
					tsft504Map.put("schClass", "");
					tsft504Map.put("schDnl", "");
					tsft504Map.put("note", "");
					tsft504List.add(tsft504Map);
				}
				
				if(tsft504List.size() > 0) {
					convertMap = new HashMap<String, Object>();
					convertMap.put("mergeRows", tsft504List);
					try {
						//int resultCnt = saveApplicantInfomation("mergeTSTF504", convertMap);
						int resultCnt = appointmentMapper.saveSchoolInfomation(convertMap);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				//학력 END *********************************************************************************
				//경력
				List<Map<String, Object>> tsft505List = new ArrayList<>();
				if(itemMap.containsKey("careerpart") && itemMap.get("careerpart") != null && !itemMap.get("careerpart").equals("")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) itemMap.get("careerpart");
					if(list.size() > 0) {
						for(Map<String, Object> m : list) {
							Map<String, Object> tsft505Map = new HashMap<String, Object>();
							tsft505Map.put("applId",applId);
							if(m != null && !m.isEmpty()) {
								if(m.containsKey("careerType") && m.get("careerType") != null) {
									tsft505Map.put("careerType", m.get("careerType"));
								}else {
									tsft505Map.put("careerType", "");
								}
								
								if(m.containsKey("companyName") && m.get("companyName") != null) {
									tsft505Map.put("careerCompanyName", m.get("companyName"));
								}else {
									tsft505Map.put("careerCompanyName", "");
								}
								if(m.containsKey("partName") && m.get("partName") != null) {
									tsft505Map.put("careerOrgName", m.get("partName"));
								}else {
									tsft505Map.put("careerOrgName", "");
								}
								if(m.containsKey("taskContent") && m.get("taskContent") != null) {
									tsft505Map.put("careerJob", m.get("taskContent"));
								}else {
									tsft505Map.put("careerJob", "");
								}
								if(m.containsKey("Reason") && m.get("Reason") != null) {
									tsft505Map.put("careerReason", m.get("Reason"));
								}else {
									tsft505Map.put("careerReason", "");
								}
								if(m.containsKey("annualSalary") && m.get("annualSalary") != null) {
									tsft505Map.put("careerSalary", m.get("annualSalary"));
								}else {
									tsft505Map.put("careerSalary", "");
								}
								tsft505Map.put("note", "");
							}else { 

								tsft505Map.put("careerType", "");
								tsft505Map.put("careerCompanyName", "");
								tsft505Map.put("careerOrgName", "");
								tsft505Map.put("careerJob", "");
								tsft505Map.put("careerReason", "");
								tsft505Map.put("careerSalary", "");
								tsft505Map.put("note", "");
							}
							tsft505List.add(tsft505Map);
						}
					}
				}else {
					Map<String, Object> tsft505Map = new HashMap<String, Object>();
					tsft505Map.put("applId",applId);
					tsft505Map.put("careerType", "");
					tsft505Map.put("careerCompanyName", "");
					tsft505Map.put("careerOrgName", "");
					tsft505Map.put("careerJob", "");
					tsft505Map.put("careerReason", "");
					tsft505Map.put("careerSalary", "");
					tsft505Map.put("note", "");
					tsft505List.add(tsft505Map);
				}
				convertMap = new HashMap<String, Object>();
				convertMap.put("mergeRows", tsft505List);
				try {
					//int resultCnt = saveApplicantInfomation("mergeTSTF505", convertMap);
					int resultCnt = appointmentMapper.saveCareerInfomation(convertMap);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				//어학
				List<Map<String, Object>> tsft506List = new ArrayList<>();
				if(itemMap.containsKey("languagepart") && itemMap.get("languagepart") != null && !itemMap.get("languagepart").equals("")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) itemMap.get("languagepart");
					if(list.size() > 0) {
						for(Map<String, Object> m : list) {
							Map<String, Object> tsft506Map = new HashMap<String, Object>();
							tsft506Map.put("applId",applId);
							if(m != null && !m.isEmpty()) {
								if(m.containsKey("languageName") && m.get("languageName") != null && !m.get("languageName").equals("")) {
									Map<String, Object> langMap = (Map<String, Object>) m.get("languageName");
									if(langMap.containsKey("name") && langMap.get("name") != null){
										tsft506Map.put("langName",langMap.get("name").toString());
									}
								}else {
									tsft506Map.put("langName", "");
								}
								 
								if(m.containsKey("lastScore") && m.get("lastScore") != null) {
									tsft506Map.put("langGrade", m.get("lastScore"));
								}else {
									tsft506Map.put("langGrade", "");
								}
								if(m.containsKey("issueDate") && m.get("issueDate") != null) {
									tsft506Map.put("langDate", m.get("issueDate"));
								}else {
									tsft506Map.put("langDate", "");
								}
								tsft506Map.put("note", "");
							}else { 

								tsft506Map.put("langName", "");
								tsft506Map.put("langGrade", "");
								tsft506Map.put("langDate", ""); 
								tsft506Map.put("note", "");
							}
							tsft506List.add(tsft506Map);
						}
					}
				}else {
					Map<String, Object> tsft506Map = new HashMap<String, Object>();
					tsft506Map.put("applId",applId);
					tsft506Map.put("langName", "");
					tsft506Map.put("langGrade", "");
					tsft506Map.put("langDate", ""); 
					tsft506Map.put("note", "");
					tsft506List.add(tsft506Map);
				}
				convertMap = new HashMap<String, Object>();
				convertMap.put("mergeRows", tsft506List);
				try {
					//int resultCnt = saveApplicantInfomation("mergeTSTF506", convertMap);
					int resultCnt = appointmentMapper.saveLanguageInfomation(convertMap);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				//자격
				
				List<Map<String, Object>> tsft507List = new ArrayList<>();
				if(itemMap.containsKey("licensepart") && itemMap.get("licensepart") != null && !itemMap.get("licensepart").equals("")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) itemMap.get("licensepart");
					if(list.size() > 0) {
						for(Map<String, Object> m : list) {
							Map<String, Object> tsft507Map = new HashMap<String, Object>();
							tsft507Map.put("applId",applId);
							if(m != null && !m.isEmpty()) {
								if(m.containsKey("licenseName") && m.get("licenseName") != null && !m.get("licenseName").equals("")) {
									Map<String, Object> liceMap = (Map<String, Object>) m.get("licenseName");
									if(liceMap.containsKey("name") && liceMap.get("name") != null){
										tsft507Map.put("liceName",liceMap.get("name").toString());
									}
								}else {
									tsft507Map.put("liceName", "");
								}
								 
								if(m.containsKey("issueOrg") && m.get("issueOrg") != null) {
									tsft507Map.put("liceOrgName", m.get("issueOrg"));
								}else {
									tsft507Map.put("liceOrgName", "");
								}
								if(m.containsKey("issueDate") && m.get("issueDate") != null) {
									tsft507Map.put("liceDate", m.get("issueDate"));
								}else {
									tsft507Map.put("liceDate", "");
								}
								tsft507Map.put("note", "");
							}else { 

								tsft507Map.put("liceName", "");
								tsft507Map.put("liceOrgName", "");
								tsft507Map.put("liceDate", ""); 
								tsft507Map.put("note", "");
							}
							tsft507List.add(tsft507Map);
						}
					}
				}else {
					Map<String, Object> tsft507Map = new HashMap<String, Object>();
					tsft507Map.put("applId",applId);
					tsft507Map.put("liceName", "");
					tsft507Map.put("liceOrgName", "");
					tsft507Map.put("liceDate", ""); 
					tsft507Map.put("note", "");
					tsft507List.add(tsft507Map);
				}
				convertMap = new HashMap<String, Object>();
				convertMap.put("mergeRows", tsft507List);
				try {
					//int resultCnt = saveApplicantInfomation("mergeTSTF507", convertMap);
					int resultCnt = appointmentMapper.saveLicenseInfomation(convertMap);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
			}  

		}
		 
	}
	public void deleteBasic(Map<String, Object> mapElement) {
		appointmentMapper.deleteBasic(mapElement);
	}
	//연락처 delete
	public void deleteContact(Map<String, Object> mapElement) {
		appointmentMapper.deleteContact(mapElement);
	}
	//학력 delete
	public void deleteSchool(Map<String, Object> mapElement) {
		appointmentMapper.deleteSchool(mapElement);
	}
	//경력 delete
	public void deleteCareer(Map<String, Object> mapElement) {
		appointmentMapper.deleteCareer(mapElement);
	}
	//병역 delete
	public void deleteMilitary(Map<String, Object> mapElement) {
		appointmentMapper.deleteMilitary(mapElement);
	}
	//채용공고 delete
	public void deleteAnno(Map<String, Object> mapElement) {
		appointmentMapper.deleteAnno(mapElement);
	} 
	public void saveBasic(Map<String, Object> basicMap) {
		appointmentMapper.saveBasic(basicMap);
	}
	public void saveContact(Map<String, Object> contactMap) {
		appointmentMapper.saveContact(contactMap);
	}
	public void saveHighSchool(Map<String, Object> highschoolMap) {
		appointmentMapper.saveHighSchool(highschoolMap);
	} 
	public void saveUniversitySchool(Map<String, Object> universityMap) {
		appointmentMapper.saveUniversitySchool(universityMap);	
	}
	
	public void saveCareer(Map<String, Object> careerMap) {
		appointmentMapper.saveCareer(careerMap);
	}
	
	public void saveMilitary(Map<String, Object> militaryMap) {
		appointmentMapper.saveMilitary(militaryMap);
	}
	
	public void saveAnno(Map<String, Object> mapElement) {
		appointmentMapper.saveAnno(mapElement);
	}
	
}
