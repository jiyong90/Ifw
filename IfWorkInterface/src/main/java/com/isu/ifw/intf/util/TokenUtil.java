package com.isu.ifw.intf.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtil {
	/*
	@Value("${jwt.sign-key}")
	private static String signKey;
	
	@Value("${jwt.alg:HS256}")
	private static String alg;
	
	@Value("${jwt.expired-time:60000l}")
	private static Long expiredTime;
	*/
							//"h0nda20200401k0reaDzIsuInterface"
	static String signKey = "h0nda20200401k0readziSuinTerFace";
	static String SIGNATURE_ALGORITHM = "HmacSHA256";
	static Long expiredTime = 60000l;
	
	public static String createJWT(String id, String issuer, String subject, Map<String, Object> payloads, long ttlMillis) {
  	  
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(signKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setClaims(payloads)
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
      
        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }  
      
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
        
    }
	
	public static Claims decodeJWT(String jwt) {
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()
	            .setSigningKey(DatatypeConverter.parseBase64Binary(signKey))
	            .parseClaimsJws(jwt).getBody();
	    return claims;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		Map<String, Object> payloads = new HashMap<String, Object>();
		payloads.put("a", "b");
		System.out.println(createJWT("a","ss","ddd", payloads,60000l));
		//System.out.println(UUID.randomUUID().toString());
		System.out.println(decodeJWT("eyJhbGciOiJIUzI1NiJ9.eyJhIjoiYiIsImV4cCI6MTU4Mjc3MjIyM30.IUP4HhySuDBKer8ZHY3-Kp8krbDHuFrc-KZ1aEWfmL0").toString());
		//vefify("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhIiwiaWF0IjoxNTgyNzcxODgzLCJzdWIiOiJkZGQiLCJpc3MiOiJzcyIsImV4cCI6MTU4Mjc3MTk0M30.kALhIMgbixW_hM--HrY2KHKJvm4sab0YuDbXcQWomN4");
	}
	
	
	/*
	public static String createToken() throws NoSuchAlgorithmException {
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", SIGNATURE_ALGORITHM);
		
		Map<String, Object> payloads = new HashMap<>();
		//Long expiredTime = 1000 * 60l; //만요기간 1분
		Date now = new Date();
		System.out.println(now.getTime());
		now.setTime(now.getTime() + expiredTime);
		payloads.put("CD_COMPANY", "1000");
		payloads.put("NO_EMP", "2008856");

		System.out.println(signKey);
		
		String jwt = null;
		jwt = Jwts.builder()
						.setHeader(headers)
						.setClaims(payloads)
						.setExpiration(now)
						.signWith(SignatureAlgorithm.HS256, signKey.getBytes())
						.compact();
		 
		return jwt;
	}
	
	public static void vefify(String jwt) throws ExpiredJwtException, PrematureJwtException {
		try {
		Jws<Claims> claims = Jwts.parser()
							.setSigningKey(signKey.getBytes())
							.parseClaimsJws(jwt);
		System.out.println(claims.getBody().toString());
		}catch(ClaimJwtException a) {
			System.out.println("a");
		}catch(MalformedJwtException c) {
			System.out.println("c");
		}catch(SignatureException e) {
			System.out.println("e");
		}catch(UnsupportedJwtException f) {
			System.out.println("f");
		}
	}
	
	public static String sha256(String msg)  throws NoSuchAlgorithmException {

	    MessageDigest md = MessageDigest.getInstance("SHA-256");

	    md.update(msg.getBytes());
	    return Crypt.crypt(md.digest());
	    //return CryptoUtil.byteToHexString(md.digest());

	}
	
	public boolean verifySignature(String signature, String requestBody) {
		
        if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(requestBody)) {
            return false;
        }
        

        String madeSignature = getHmacSignature(requestBody.getBytes());
        //log.info("Sender-sent signature: {}, made signature :{}", signature, madeSignature);

        return signature.trim().equals(madeSignature);
    }

    public static String getHmacSignature(byte[] requestBody) {
        byte[] key = signKey.getBytes();
        final SecretKeySpec secretKey = new SecretKeySpec(key, SIGNATURE_ALGORITHM);

        try {
            Mac mac = Mac.getInstance(SIGNATURE_ALGORITHM);
            mac.init(secretKey);
            return Base64.encodeBase64String(mac.doFinal(requestBody));
        } catch (Exception ignored) {
            //log.warn("Error occured processing Mac init - Exception : {}, secretKey: {}", ignored, secretKey);
            return "";
        }

    }
    */
 
	
	
}
/*
class Hmac {
    // hash 알고리즘 선택
    private static final String ALGOLISM = "HmacSHA256";
    // hash 암호화 key
    private static final String key = "nsHc6458";
 
 
    public static String hget(String message) {
        try {
            // hash 알고리즘과 암호화 key 적용
            Mac hasher = Mac.getInstance(ALGOLISM);
            hasher.init(new SecretKeySpec(key.getBytes(), ALGOLISM));
 
            // messages를 암호화 적용 후 byte 배열 형태의 결과 리턴
            byte[] hash = hasher.doFinal(message.getBytes());
            return byteToString(hash);
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        catch (InvalidKeyException e){
            e.printStackTrace();
        }
        return "";
    }
 
    // byte[]의 값을 16진수 형태의 문자로 변환하는 함수
    private static String byteToString(byte[] hash) {
        StringBuffer buffer = new StringBuffer();
 
        for (int i = 0; i < hash.length; i++) {
            int d = hash[i];
            d += (d < 0)? 256 : 0;
            if (d < 16) {
                buffer.append("0");
            }
            buffer.append(Integer.toString(d, 16));
        }
        return buffer.toString();
    }
    
    
}

*/

