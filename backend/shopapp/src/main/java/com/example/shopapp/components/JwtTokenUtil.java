package com.example.shopapp.components;

import com.example.shopapp.Model.User;
import com.example.shopapp.exception.InvalidParamException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoder;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
//    @Value("${jwt.expiration}")
//    private long expiration;
//    @Value("${jwt.secretKey}")
//    private String secretKey;
    private final JwtProperties jwtProperties;
    public String generateToken(User user) throws Exception{
        Map<String,Object> claims = new HashMap<>();
//        this.generateSecretKey();
        claims.put("phoneNumber",user.getPhoneNumber());
        claims.put("userId",user.getId());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis()+jwtProperties.getExpiration() * 1000L))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e){
//            System.out.println("Cannot create jwt token, error:"+e.getMessage());
            throw  new InvalidParamException("Cannot create jwt token, error:"+e.getMessage());
//            return null;
        }
    }
    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256-bit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
    private Key getSignKey(){
        byte[] bytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        // Keys.hmacShaKeyFor(Decoders.BASE64.decode("cFmLSOGF4ZzbDeQWuekjEoOasYeDECiB/XbHqogCays="))
        return Keys.hmacShaKeyFor(bytes);
    }
    public Claims extractAllclaims(String token){
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsTFunction){
      final Claims claims = this.extractAllclaims(token);
      return claimsTFunction.apply(claims);
    }

    // check expiration
    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    public String extractPhoneNumber(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);
        return phoneNumber.equals(userDetails.getUsername())&&!isTokenExpired(token);
    }
}
