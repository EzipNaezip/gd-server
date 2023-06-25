package com.manofsteel.gd.auth.handler;

import com.manofsteel.gd.type.dto.ResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.io.OutputStream;

public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException exception) throws IOException {
		ResponseModel responseModel = ResponseModel.builder()
			.httpStatus(HttpStatus.UNAUTHORIZED)
			.message("인증되지 않은 사용자이거나 권한이 부족합니다.")
			.build();
		response.setStatus(401);
		response.setContentType("application/json");
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(responseModel.toJson().getBytes());
	}
}
