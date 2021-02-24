package xyz.morlotti.virtualbookcase.userwebsite.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.*;

import lombok.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class TokenUtils
{
	public static final String TOKEN_COOKIE_NAME = "token";

	public static final String TOKEN_HEADER_NAME = "Authorization";

	private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

	@Setter
	@Getter
	@AllArgsConstructor
	@ToString
	public static class UserInfo
	{
		private String id;
		private String login;
		private String email;
		private String role;
		private String token;
	}

	@Value("${virtualbookcase.app.jwt.secret}")
	private String jwtSecret;

	public UserInfo getUserInfoFromJwtToken(String token)
	{
		System.out.println("jwtSecret: " + jwtSecret);
		if(token != null && token.startsWith("Token:"))
		{
			Claims body;

			try
			{
				body = Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token.substring(6)).getBody();

				/**/

				String login = body.getSubject();

				String[] parts = body.getIssuer().split("\\|");

				if(parts.length == 3)
				{
					return new UserInfo(
						parts[0],   // id
						login,
						parts[1],   // email
						parts[2],   // role
						token
					);
				}
			}
			catch(Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}

		return null;
	}

	public static void createTokenCookie(HttpServletResponse httpServletResponse, String token)
	{
		Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
		cookie.setMaxAge(60 * 60 * 24 * 30);
		cookie.setPath("/");

		httpServletResponse.addCookie(cookie);
	}

	public static void deleteTokenCookie(HttpServletResponse httpServletResponse)
	{
		Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, null);
		cookie.setMaxAge(60 * 60 * 24 * 30);
		cookie.setPath("/");

		httpServletResponse.addCookie(cookie);
	}
}
