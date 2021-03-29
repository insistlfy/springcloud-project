package org.lfy.jwt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JwtConfig
 *
 * @author lfy
 * @date 2021/3/26
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    @ApiModelProperty(value = "存放Token的Header key值")
    private String token;

    @ApiModelProperty(value = "自定义密钥，加盐")
    private String secret;

    @ApiModelProperty(value = "超时时间 单位秒")
    private Duration expiration = Duration.ofMinutes(3600);

    @ApiModelProperty(value = "自定义token 前缀字符")
    private String tokenPrefix;

    @ApiModelProperty(value = "accessToken超时时间 单位秒")
    private Duration accessToken = Duration.ofMinutes(3600);

    @ApiModelProperty(value = "刷新token时间 单位秒")
    private Duration refreshToken = Duration.ofMinutes(3600);

    @ApiModelProperty(value = "允许访问的uri")
    private String permitAll;

    @ApiModelProperty(value = "需要校验的uri")
    private String authUrl;


    /**
     * 生成acceptToken
     *
     * @param username String
     * @param claims   Map
     * @return String
     */
    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(expiration.toMillis()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Token 校验
     *
     * @param username String
     * @param token    String
     * @return boolean
     */
    public boolean validateToken(String username, String token) {
        try {
            final String userId = getUserIdFromClaims(token);
            return null != getTokenClaim(token) && userId.equals(username) && !tokenExpired(token);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token!" + e);
        }
    }

    /**
     * 校验Token
     *
     * @param token String
     * @return String
     */
    public boolean validateToken(String token) {
        try {
            return null != getTokenClaim(token) && !tokenExpired(token);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token!" + e);
        }
    }

    /**
     * 获取Token中注册消息
     *
     * @param token String
     * @return Claims
     */
    private Claims getTokenClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new IllegalStateException("Get TokenClaim error!" + e);
        }
    }

    /**
     * Token 是否过期
     *
     * @param token String
     * @return boolean
     */
    public boolean tokenExpired(String token) {
        return getTokenClaim(token).getExpiration().before(new Date());
    }

    /**
     * 生成失效时间
     *
     * @param expiration long
     * @return Date
     */
    private Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从Token获取userId
     *
     * @param token String
     * @return String
     */
    private String getUserIdFromClaims(String token) {
        return getTokenClaim(token).getId();
    }
}
