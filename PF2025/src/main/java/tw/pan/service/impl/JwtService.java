package tw.pan.service.impl;

import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import tw.pan.utils.cache.Cache;

@Service
public class JwtService extends Cache<String, SecretKey> {

	// [安全]隨機
	private final SecureRandom secureRandom = new SecureRandom();
	
	public String currentAccount() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public String issueToken(String account) {
		byte[] keyBytes = new byte[32];
		secureRandom.nextBytes(keyBytes);
		SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
		super.setData(account, secretKey);
		return Jwts.builder().id(UUID.randomUUID().toString()).subject(account)
				.expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)).signWith(secretKey).compact();
	}
	
	public Claims verifyToken(String token) {
		return Jwts.parser().verifyWith(super.getData(currentAccount())).build()
				.parseSignedClaims(token.replace("Bearer ", "")).getPayload();
	}
	
}
