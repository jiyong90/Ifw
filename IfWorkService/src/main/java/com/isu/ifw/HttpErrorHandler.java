package com.isu.ifw;

import java.security.InvalidParameterException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * HTTP 오류에 대한 처리를 하는 핸들러
 * 기본적으로 error.jsp 에 HTTP 예외를 위임한다.
 * 만일 하위에서 에러 페이지를 수정하려고 하면, getErrorPageName() 이라는 메소드를 오버라이드 해야한다.
 * 
 * @author tykim
 *
 */
public class HttpErrorHandler {
	/**
	 * 인증 예외를 처리한다. - 401 코드 반환
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value=CertificateException.class)
	public ModelAndView certificateExcetionHandler(CertificateException e){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("status", "000");
		returnMap.put("message", e.getLocalizedMessage());
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorObject", returnMap);
		mav.setViewName(getErrorPageName());
		return mav;
	}
	
	/**
	 * 입력 파라미터 오류에 대한 처리를 한다. - 400 코드 반환
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value=InvalidParameterException.class)
	public ModelAndView invalidParameterExceptionHandler(InvalidParameterException e){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("status", "000");
		returnMap.put("message", e.getLocalizedMessage());
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorObject", returnMap);
		mav.setViewName(getErrorPageName());
		return mav;
	}
	
	/**
	 * 중복데이터에 대한 처리를 한다. - 409 코드 반환
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(value=DuplicateKeyException.class)
	public ModelAndView conflictExceptionHandler(DuplicateKeyException e){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("status", "000");
		returnMap.put("message", e.getLocalizedMessage());
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorObject", returnMap);
		mav.setViewName(getErrorPageName());
		return mav;
	}
	/**
	 * 입력 파라미터 오류에 대한 처리를 한다. - 500 코드 반환
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value=Exception.class)
	public ModelAndView runtimeExceptionHandler(Exception e){
		e.printStackTrace();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("status", "000");
		returnMap.put("message", e.getLocalizedMessage());
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorObject", returnMap);
		mav.setViewName(getErrorPageName());
		return mav;
	}
	
	
	String errorPagaName = "error";
	
	/**
	 * 에러페이지 이름을 반환한다.
	 * @return "기본으로 'error'" 라는 문자열을 반환한다.
	 */
	protected String getErrorPageName(){
		return this.errorPagaName;
	}
	
	/**
	 * 에러 페이지 이름을 세팅한다.
	 * @param errorPageName
	 */
	protected void setErrorPageName(String errorPageName){
		this.errorPagaName = errorPageName;
	}
}
