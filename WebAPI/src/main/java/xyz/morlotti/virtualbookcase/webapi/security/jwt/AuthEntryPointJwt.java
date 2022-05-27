package xyz.morlotti.virtualbookcase.webapi.security.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint
{
	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException
	{
		logger.error("Unauthorized error: {}", authException.getMessage(), authException);

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized, please provide a token");
	}
}
