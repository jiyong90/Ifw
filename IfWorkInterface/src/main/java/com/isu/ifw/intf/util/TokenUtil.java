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
		payloads.put("CD_COMPANY", "1000");
		payloads.put("NO_EMP", "20081111");
		String jwt = createJWT("id","issuer","subject", payloads,60000l);
		System.out.println(jwt);
		System.out.println(decodeJWT(jwt).toString());
	}
	
	 
	
}
 
