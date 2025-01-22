package tw.pan.utils.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import tw.pan.entity.dto.ErrorDto;
import tw.pan.utils.exception.DatabaseOperateException;
import tw.pan.utils.exception.PageNotFoundException;
import tw.pan.utils.exception.RequestErrorException;

@ControllerAdvice
public class SimpleExceptionHandler {
	
	@ExceptionHandler(SecurityException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "auth Unauthorized")
	public ErrorDto authException(Exception e, HttpServletRequest request) {
		return new ErrorDto()
				.setStatus(HttpStatus.UNAUTHORIZED.value())
				.setMessage(e.getMessage())
				.setPath(request.getRequestURI());
	}
	
	@ExceptionHandler({AccessDeniedException.class, MalformedJwtException.class})
	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "access denied")
	public ErrorDto accessDeniedHandler(Exception e, HttpServletRequest request) {
		return new ErrorDto()
				.setStatus(HttpStatus.FORBIDDEN.value())
				.setMessage(e.getMessage())
				.setPath(request.getRequestURI());
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "result data null")
	public ErrorDto EmptyResultErrorHandler(EmptyResultDataAccessException e, HttpServletRequest request) {
		return new ErrorDto()
				.setStatus(HttpStatus.NO_CONTENT.value())
				.setMessage(e.getMessage())
				.setPath(request.getRequestURI());
	}

	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "duplicate key")
	public ErrorDto duplicateKeyHandler(DuplicateKeyException e, HttpServletRequest request) {
		return new ErrorDto()
				.setStatus(HttpStatus.CONFLICT.value())
				.setMessage(e.getMessage())
				.setPath(request.getRequestURI());
	}

	@ExceptionHandler(DatabaseOperateException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "database operate error")
	public ErrorDto sqlOperateHandler(HttpServletRequest request) {
		return new ErrorDto()
				.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.setPath(request.getRequestURI());
	}
	
	@ExceptionHandler(PageNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "page not found")
	public ErrorDto pageNotFoundHandler(HttpServletRequest request) {
		return new ErrorDto()
				.setStatus(HttpStatus.NOT_FOUND.value())
				.setPath(request.getRequestURI());
	}
	
	@ExceptionHandler({RequestErrorException.class, ValidationException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorDto badRequestHandler(Exception e, HttpServletRequest request) {
		return new ErrorDto()
				.setStatus(HttpStatus.BAD_REQUEST.value())
				.setMessage(e.getMessage())
				.setPath(request.getRequestURI());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDto serverErrorHandler(Exception e) {
		return new ErrorDto()
				.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.setMessage(e.getLocalizedMessage());
	}
	
}
