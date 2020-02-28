package com.isu.ifw.intf.crypt;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import com.isu.ifw.intf.util.AdapterProp;
import com.isu.ifw.intf.util.Aes256;


/**
 * Aes256 암호화 처리
 * 
 * @author Hongs
 *
 */
public class Crypt {

	/*
	 * 암호화 키컬럼
	 */
	public static final String CRYPT_KEY_COLUMN_NAME = "userToken";

	/**
	 * 암호화
	 * 
	 * @param cryptKey
	 *            암호화 키
	 * @param columnNm
	 *            대상 컬럼
	 * @param values
	 *            대상 데이터(배열)
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 */
	public static String[] encrypt(String cryptKey, String columnNm, String[] values)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		if (values == null) {
			return null;
		}

		if (cryptKey == null) {
			return values;
		}

		String[] rtn = values;

		if (isEncColumn(columnNm)) {
			for (int i = 0; i < values.length; i++) {
				rtn[i] = encrypt(cryptKey, columnNm, values[i]);
			}
		}

		return rtn;
	}

	/**
	 * 암호화
	 * 
	 * @param cryptKey
	 *            암호화 키
	 * @param columnNm
	 *            대상 컬럼
	 * @param value
	 *            대상 데이터
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 */
	public static String encrypt(String cryptKey, String columnNm, String value)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		if (value == null) {
			return null;
		}

		if (cryptKey == null) {
			return value;
		}

		Aes256 aes = new Aes256(cryptKey);
		String rtn = value;

		if (isEncColumn(columnNm)) {
			try {
				rtn = aes.encrypt(value);
				;
			} catch (Exception e) {
				rtn = value;
			}
		}

		return rtn;
	}

	/**
	 * 복호화
	 * 
	 * @param cryptKey
	 *            암호화 키
	 * @param columnNm
	 *            대상 컬럼
	 * @param values
	 *            대상 데이터(배열)
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 */
	public static String[] decrypt(String cryptKey, String columnNm, String[] values)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		if (values == null) {
			return null;
		}

		if (cryptKey == null) {
			return values;
		}

		String[] rtn = values;

		if (isEncColumn(columnNm)) {
			for (int i = 0; i < values.length; i++) {
				rtn[i] = decrypt(cryptKey, columnNm, values[i]);
			}
		}

		return rtn;
	}

	/**
	 * 복호화
	 * 
	 * @param cryptKey
	 *            암호화 키
	 * @param columnNm
	 *            대상 컬럼
	 * @param value
	 *            대상 데이터
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 */
	public static String decrypt(String cryptKey, String columnNm, String value)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		if (value == null) {
			return null;
		}

		if (cryptKey == null) {
			return value;
		}

		Aes256 aes = new Aes256(cryptKey);
		String rtn = value;

		if (isEncColumn(columnNm)) {
			try {
				rtn = aes.decrypt(value);
			} catch (Exception e) {
				rtn = value;
			}
		}

		return rtn;
	}

	/**
	 * 암호화 컬럼 여부
	 * 
	 * @param colNm
	 *            대상 컬럼
	 * @return
	 */
	public static boolean isEncColumn(String colNm) {
		String[] encCols = AdapterProp.getEncCols();
		if (encCols != null) {
			for (String encKey : encCols) {
				if (encKey.equals(colNm)) {
					System.out.println(colNm+"은 암호화 컬럼입니다.");
					return true;
				}
			}
		}
		System.out.println(colNm+"은 암호화 컬럼이 아닙니다.");
		return false;
	}
}
