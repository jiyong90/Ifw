package com.isu.ifw.intf.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.isu.ifw.intf.wrapper.ResponseWrapper;
import com.isu.ifw.intf.wrapper.RequestWrapper;

/**
 * 암/복호화 처리를 위한 filter class
 *
 * @author Hongs
 *
 */
@Component("encryptFilter") 
public class EncryptFilter implements Filter {

	private String encoding;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.encoding = filterConfig.getInitParameter("encoding");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		request.setCharacterEncoding(this.encoding);
		response.setCharacterEncoding(this.encoding);
		String path = ((HttpServletRequest) request).getRequestURI();
		System.out.println("path : " + path.indexOf("fileuploadJFileUpload.do"));
		if(path.indexOf("fileuploadJFileUpload.do") == -1
				&& path.indexOf("GetDataList.do") == -1 
				&& path.indexOf("GetDataMap.do") == -1
				&& path.indexOf("MobileAlert.do") == -1
				&& path.indexOf("autoReply") == -1
				){
			System.out.println("****** doFilter!!!!!!!!!!!!!!!!!!!");
			// 암, 복호화를 위한 request, response를 각각 wrapper 처리
			RequestWrapper rw = new RequestWrapper((HttpServletRequest) request);
			ResponseWrapper respw = new ResponseWrapper((HttpServletResponse) response);
	
			chain.doFilter(rw, respw);
	
			// response의 결과에서 암호화 대상 컬럼 데이터 처리를 위한 로직
			byte[] bytes = respw.toByteArray(rw.getCryptkey());
	
			OutputStream out = null;
			try {
				out = response.getOutputStream();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			if (out == null) {
				response.getWriter().write(new String(bytes));
			} else {
				out.write(bytes);
			}
		}else{
			chain.doFilter(request, response);
		}
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		addSameSite(httpServletResponse , "None");
	}

	@Override
	public void destroy() {

	}

	private void addSameSite(HttpServletResponse response, String sameSite) {

		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		boolean firstHeader = true;
		for (String header : headers) { // there can be multiple Set-Cookie attributes
			if (firstHeader) {
				response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
				firstHeader = false;
				continue;
			}
			response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
		}
	}

}
