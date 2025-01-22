package tw.pan.service.impl.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON).body("Logout Success");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(responseEntity.getStatusCode().value());
		response.setContentType(responseEntity.getHeaders().getContentType().toString());
		response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
	}

}
