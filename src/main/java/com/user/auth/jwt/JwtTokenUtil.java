package com.user.auth.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable{

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	@Value("${jwt.secret}")
	private String secretKey;

	/**
	 * Generate jwt token
	 * @param userDetails
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		return doGenerateJwtToken(map, userDetails.getUsername());
		
	}

	private String doGenerateJwtToken(Map<String, Object> map, String username) {
		
		String jwttoken = Jwts.builder().setClaims(map).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
		.signWith(SignatureAlgorithm.HS512, secretKey)
		.compact();
		
		return jwttoken;
	}
	
	public boolean validateToken(String token, UserDetails userDetails){
		
		String userName = getUserNameFromToken(token);
		
		if(userName.equals(userDetails.getUsername()) && !isTokeExpired(token)) {
			return true;
		}
		return false;
	}

	private boolean isTokeExpired(String token) {
		
		Date expirationDate = getExpirationDateFromToken(token);
		
		return expirationDate.before(new Date());
	}

	private Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public String getUserNameFromToken(String token) {
		
		return getClaimFromToken(token, Claims::getSubject);
		
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		
		Claims claims = getAllClaimFromToken(token);
		
		return claimResolver.apply(claims);
		
	}

	private Claims getAllClaimFromToken(String token) {
		
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		
	}
	
}
