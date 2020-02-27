package com.isu.ifw.intf.wrapper;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import com.isu.ifw.intf.crypt.Crypt;



/**
 * RESTFul을 통해서 전송받은 데이터중 암호화 컬럼의 데이터를 복호화 하기 위한 wrapper class
 * 
 * <pre>
 * method에 따라 데이터 전달 방식이 상이하여 개별 처리
 *   - GET : 기본적으로 request의 param 데이터를 수정할 수 없기 떄문에 별도의 객체(Map)에 전달 받은 파라미터를 담고 복호화 처리.
 *   - POST,PUT... : RESTFul에서 GET을 제외한 나머지 구조는 전부 request body에 JSON구조의 데이터를 전송하므로
 *                   별도의 객체(JSONObject)에 getInputStream()를 통해서 읽어드린 내용을 변환 복호화 처리
 * </pre>
 * 
 * @author Hongs
 *
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private static final Logger Log = Logger.getLogger(RequestWrapper.class);

	private Map<String, String[]> params;
	private JSONObject bodyJSON;

	String cryptKey = null;	

	/**
	 * 암호화 키
	 * 
	 * @return
	 */
	public String getCryptkey() {
		return this.cryptKey;
	}

	public RequestWrapper(HttpServletRequest request) throws ServletException {
		super(request);

		String method = request.getMethod();
		this.params = new HashMap<String, String[]>(request.getParameterMap());
		
		if(this.params != null && this.params.size() > 0) {
			Enumeration<String> e = getParameterNames();

			while (e.hasMoreElements()) {
				String key = e.nextElement();
				String[] values = this.params.get(key);
				
				if(values != null) {
					String[] realValues = new String[values.length];
					for(int i = 0; i < values.length; i++) {
						String value = values[i];
						realValues[i] = value.replaceAll("%2B", "+").replaceAll("%3D", "=");
						
						//System.out.println("\t " + value + " :: " + realValues[i]);
					}
					
					this.params.put(key, realValues);
				}
			}
		}

		Log.debug("URI : " + request.getRequestURI());
		Log.debug("METHOD : " + method);

		// 전송 데이터에서 암호화 키(userToken)를 추출하여 복호화 처리를 수행한다.
		if ("GET".equals(method)) {
			String[] tmp = this.params.get(Crypt.CRYPT_KEY_COLUMN_NAME);

			if (tmp != null) {
				this.cryptKey = tmp[0];
			}

			String paramString = "";
			Enumeration<String> e = getParameterNames();

			while (e.hasMoreElements()) {
				String columnNm = e.nextElement();
				String[] values = getParameterValues(columnNm);
				
				paramString += columnNm + " : [";

				if (values != null) {
					for(int i = 0; i < values.length; i++) {
						paramString += values[i];
						
						if(i < values.length-1) {
							paramString += ", ";
						}
					}
					 
					try {
						values = Crypt.decrypt(this.cryptKey, columnNm, values);
					} catch (Exception ee) {
						ee.printStackTrace();
					}

					setParameter(columnNm, values);
				}
				
				paramString += "]\n";
			}
			
			Log.debug("PARAM : " + paramString);
		} else {
			BufferedInputStream bis = null;
			ByteArrayOutputStream buffer = null;

			try {
				bis = new BufferedInputStream(request.getInputStream());
				buffer = new ByteArrayOutputStream();

				int nRead;
				byte[] data = new byte[1024];

				while ((nRead = bis.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
				buffer.flush();

				String paramValue = buffer.toString();
				Log.debug("PARAM : " + paramValue);

				if (paramValue != null && !"".equals(paramValue)) {
					this.bodyJSON = new JSONObject(paramValue);

					if (this.bodyJSON.has(Crypt.CRYPT_KEY_COLUMN_NAME))
						this.cryptKey = this.bodyJSON.getString(Crypt.CRYPT_KEY_COLUMN_NAME);
					changeDecryptParam(this.bodyJSON);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (buffer != null) {
						buffer.close();
						buffer = null;
					}
				} catch (Exception ee) {
				}

				try {
					if (bis != null) {
						bis.close();
						bis = null;
					}
				} catch (Exception ee) {
				}
			}
		}
	}

	/**
	 * JSONObject 구조에서 암호화된 데이터를 복호화 처리한다.
	 * 
	 * @param jObj
	 */
	private void changeDecryptParam(JSONObject jObj) {
		Iterator<String> keysItr = jObj.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = jObj.get(key);
			
			//Log.debug("JSON 복호화 시도중 :key:"+key);
			

			if (value instanceof JSONArray) {
			//	Log.debug("JSON 복호화 시도중..값의 유형은 JSONArray입니다");
				changeDecryptParam((JSONArray) value);
			} else if (value instanceof JSONObject) {
			//	Log.debug("JSON 복호화 시도중..값의 유형은 JSONObject 입니다.");
				changeDecryptParam((JSONObject) value);
			} else {
				try {
					if (value != null && value != JSONObject.NULL && value instanceof String) {
			//			Log.debug("JSON 복호화 시도중..값의 유형은 문자열 입니다.");
						value = Crypt.decrypt(cryptKey, key, (String) value);
					}
					else {
			//			Log.debug("JSON 복호화 시도중..값의 유형은 이도 저도 아닙니다."+value.getClass().getName());
					}
				} catch (Exception ee) {
					ee.printStackTrace();
				}

				jObj.put(key, value);
			}
		}
	}

	/**
	 * JSONArray 구조에서 암호화된 데이터를 복호화 처리한다.
	 * 
	 * @param jObj
	 */
	private void changeDecryptParam(JSONArray jArr) {
		for (int i = 0; i < jArr.length(); i++) {
			Object value = jArr.get(i);
			if (value instanceof JSONArray) {
				changeDecryptParam((JSONArray) value);
			} else if (value instanceof JSONObject) {
				changeDecryptParam((JSONObject) value);
			}
		}
	}

	@Override
	public String getParameter(String name) {

		String[] paramArray = getParameterValues(name);

		if (paramArray != null && paramArray.length > 0) {

			return paramArray[0];

		} else {

			return null;

		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final InputStream inputStream = new ByteArrayInputStream(bodyJSON.toString().getBytes(StandardCharsets.UTF_8));

		ServletInputStream servletInputStream = new ServletInputStream() {
			public int read() throws IOException {
				return inputStream.read();
			}

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// TODO Auto-generated method stub

			}
		};

		return servletInputStream;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return Collections.unmodifiableMap(params);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(params.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] result = null;
		String[] temp = params.get(name);

		if (temp != null) {

			result = new String[temp.length];
			System.arraycopy(temp, 0, result, 0, temp.length);

		}

		return result;
	}

	public void setParameter(String name, String value) {
		String[] oneParam = { value };
		setParameter(name, oneParam);

	}

	public void setParameter(String name, String[] values) {
		params.put(name, values);

	}

}
