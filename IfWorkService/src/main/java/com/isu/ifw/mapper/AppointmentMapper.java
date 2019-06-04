package com.isu.ifw.mapper;

import java.util.Map;

public interface AppointmentMapper {
	
	/**
	 * 공고 저장
	 * @param paramMap
	 * @return
	 */
	public int saveAnnouncementInfomation(Map<String, Object> paramMap);
	
	/**
	 * 기본정보 & 보훈 & 장애 저장
	 * @param paramMap
	 * @return
	 */
	public int saveBasicInfomation(Map<String, Object> paramMap);
	
	/**
	 * 가족정보 저장
	 * @param paramMap
	 * @return
	 */
	public int saveFamilyInfomation(Map<String, Object> paramMap);
	
	/**
	 * 학교정보 저장
	 * @param paramMap
	 * @return
	 */
	public int saveSchoolInfomation(Map<String, Object> paramMap);
	
	/**
	 * 경력정보 저장
	 * @param paramMap
	 * @return
	 */
	public int saveCareerInfomation(Map<String, Object> paramMap);
	
	/**
	 * 어학정보 저장
	 * @param paramMap
	 * @return
	 */
	public int saveLanguageInfomation(Map<String, Object> paramMap);
	
	/**
	 * 자격증정보 저장
	 * @param paramMap
	 * @return
	 */
	public int saveLicenseInfomation(Map<String, Object> paramMap);
	
	public void saveAnno(Map<String, Object> paramMap);
	public void deleteAnno(Map<String, Object> paramMap);
	public void saveBasic(Map<String, Object> paramMap);
	public void deleteBasic(Map<String, Object> paramMap);
	public void saveContact(Map<String, Object> paramMap);
	public void deleteContact(Map<String, Object> paramMap);
	public void saveHighSchool(Map<String, Object> paramMap);
	public void saveUniversitySchool(Map<String, Object> paramMap);
	public void deleteSchool(Map<String, Object> paramMap);
	public void saveCareer(Map<String, Object> paramMap);
	public void deleteCareer(Map<String, Object> paramMap);
	public void saveMilitary(Map<String, Object> paramMap);
	public void deleteMilitary(Map<String, Object> paramMap);
	
	
	
}
