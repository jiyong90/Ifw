package com.isu.ifw.intf.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isu.ifw.intf.crypt.Crypt;


/**
 * 반환 데이터중 암호화 컬럼의 데이터를 암호화 하기 위한 wrapper class
 * 
 * 반환 데이터가 bas64이거나 contentType이 application/octet-stream 인 경우에는 암호화 처리 안함
 * 
 * @author Hongs
 *
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

	private static final Logger Log = LoggerFactory.getLogger("ifwDbLog");

	protected ByteArrayServletOutputStream captureServletOutputStream;
	protected ServletOutputStream servletOutputStream;
	protected PrintWriter printWriter;

	public ResponseWrapper(HttpServletResponse response) {
		super(response);
		this.captureServletOutputStream = new ByteArrayServletOutputStream();
	}

	public void flushOutputStreamOrWriter() throws IOException {
		if (servletOutputStream != null) {
			servletOutputStream.flush();
		}
		if (printWriter != null) {
			printWriter.flush();
		}
	}

	@Override
	public PrintWriter getWriter() throws java.io.IOException {
		if (servletOutputStream == null) {
			if (printWriter == null) {
				printWriter = new PrintWriter(new OutputStreamWriter(captureServletOutputStream, getCharacterEncoding()));
			}
			return printWriter;
		}
		throw new IllegalStateException();
	}

	@Override
	public ServletOutputStream getOutputStream() throws java.io.IOException {
		if (printWriter == null) {
			if (servletOutputStream == null) {
				servletOutputStream = captureServletOutputStream;
			}
			return servletOutputStream;
		}
		throw new IllegalStateException();
	}

	/**
	 * 반환 데이터에 대한 암호화 처리 후 byte[]를 반환
	 * 
	 * @param cryptKey
	 * @return
	 * @throws IOException
	 */
	public byte[] toByteArray(String cryptKey) throws IOException {
		this.flushOutputStreamOrWriter();
		byte[] bytes = captureServletOutputStream.toByteArray();

		if (!Base64.isBase64(bytes)) {
			String contentType = getContentType();
			if (contentType != null && contentType.indexOf("application/octet-stream") > -1) {

			} else {
				String data = new String(bytes);
				JSONObject JSONData = null;

				try {
					JSONData = new JSONObject(data);
					Log.debug("RETURN VALUE : " + data);
				} catch (Exception ee) {
					JSONData = null;
				}

				if (JSONData != null) {
					changeEncryptParam(cryptKey, JSONData);
					return JSONData.toString().getBytes(StandardCharsets.UTF_8);
				}
			}
		}

		return bytes;
	}

	/**
	 * JSONObject 구조의 데이터 암호화 처리
	 * 
	 * @param cryptKey
	 * @param jObj
	 */
	private void changeEncryptParam(String cryptKey, JSONObject jObj) {
		Iterator<String> keysItr = jObj.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = jObj.get(key);

			if (value instanceof JSONArray) {
				changeEncryptParam(cryptKey, (JSONArray) value);
			} else if (value instanceof JSONObject) {
				changeEncryptParam(cryptKey, (JSONObject) value);
			} else {
				try {
					if (value != null && value != JSONObject.NULL) {
						value = Crypt.encrypt(cryptKey, key, String.valueOf(value));
						//System.out.println("암호화 값:"+value);
					}
				} catch (Exception ee) {
					ee.printStackTrace();
				}

				jObj.put(key, value);
			}
		}
	}

	/**
	 * JSONArray 구조의 데이터 암호화 처리
	 * 
	 * @param cryptKey
	 * @param jArr
	 */
	private void changeEncryptParam(String cryptKey, JSONArray jArr) {
		for (int i = 0; i < jArr.length(); i++) {
			Object value = jArr.get(i);
			if (value instanceof JSONArray) {
				changeEncryptParam(cryptKey, (JSONArray) value);
			} else if (value instanceof JSONObject) {
				changeEncryptParam(cryptKey, (JSONObject) value);
			}
		}
	}

	/**
	 * 반환 데이터을 임시 저장하기 위한 output stream
	 * 
	 * @author Hongs
	 *
	 */
	public class ByteArrayServletOutputStream extends ServletOutputStream {
		protected ByteArrayOutputStream buf = null;

		public ByteArrayServletOutputStream() {
			buf = new ByteArrayOutputStream();
		}

		public byte[] toByteArray() {
			return buf.toByteArray();
		}

		@Override
		public void write(int b) {
			buf.write(b);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {

		}
	}
}
