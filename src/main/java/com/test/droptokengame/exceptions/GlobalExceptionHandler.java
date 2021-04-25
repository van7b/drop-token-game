package com.test.droptokengame.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({
		ColumnAlreadyFilledException.class,
		ColumnOutOfBoundException.class,
		GameNotFoundException.class,
		GameNotInProgressException.class,
		InvalidMoveNumberException.class,
		InvalidPlayerTurnException.class,
		PlayerNotFoundException.class,
		SamePlayersException.class
	})
	@Nullable
	public final ResponseEntity<?> handleException(Exception e, WebRequest request){
		HttpHeaders headers = new HttpHeaders();
		
		if(e instanceof ColumnAlreadyFilledException) {
			HttpStatus status = HttpStatus.BAD_REQUEST;
			ColumnAlreadyFilledException ex = (ColumnAlreadyFilledException) e;
			return handleColumnAlreadyFilledException(ex, headers, status, request);
		} 
		
		else if(e instanceof ColumnOutOfBoundException) {
			HttpStatus status = HttpStatus.BAD_REQUEST;
			ColumnOutOfBoundException ex = (ColumnOutOfBoundException) e;
			return handleColumnOutOfBoundException(ex, headers, status, request);
		}
		
		else if(e instanceof GameNotFoundException) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			GameNotFoundException ex = (GameNotFoundException) e;
			return handleGameNotFoundException(ex, headers, status, request);
		}
		
		else if(e instanceof GameNotInProgressException) {
			HttpStatus status = HttpStatus.GONE;
			GameNotInProgressException ex = (GameNotInProgressException) e;
			return handleGameNotInProgressException(ex, headers, status, request);
		}
		
		else if(e instanceof InvalidMoveNumberException) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			InvalidMoveNumberException ex = (InvalidMoveNumberException) e;
			return handleInvalidMoveNumberException(ex, headers, status, request);
		}
		
		else if(e instanceof InvalidPlayerTurnException) {
			HttpStatus status = HttpStatus.CONFLICT;
			InvalidPlayerTurnException ex = (InvalidPlayerTurnException) e;
			return handleInvalidPlayerTurnException(ex, headers, status, request);
		}
		
		else if(e instanceof PlayerNotFoundException) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			PlayerNotFoundException ex = (PlayerNotFoundException) e;
			return handlePlayerNotFoundException(ex, headers, status, request);
		}
		
		else if(e instanceof SamePlayersException) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			SamePlayersException ex = (SamePlayersException) e;
			return SamePlayersException(ex, headers, status, request);
		}
		
		else {
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			return handleInternalException(e, null, headers, status, request);
		}
	}

	private ResponseEntity<?> handleInternalException(Exception e, @Nullable ExceptionMessage body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, e, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
	}

	private ResponseEntity<?> SamePlayersException(SamePlayersException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		ExceptionMessage message = new ExceptionMessage(
				HttpStatus.NOT_ACCEPTABLE.value(),
				ex.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<ExceptionMessage>(message, HttpStatus.NOT_ACCEPTABLE);
	}

	private ResponseEntity<?> handlePlayerNotFoundException(PlayerNotFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionMessage message = new ExceptionMessage(
				HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<ExceptionMessage>(message, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<?> handleInvalidPlayerTurnException(InvalidPlayerTurnException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionMessage message = new ExceptionMessage(
				HttpStatus.CONFLICT.value(),
				ex.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<ExceptionMessage>(message, HttpStatus.CONFLICT);
	}

	private ResponseEntity<?> handleInvalidMoveNumberException(InvalidMoveNumberException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionMessage message = new ExceptionMessage(
				HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<ExceptionMessage>(message, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<?> handleGameNotInProgressException(GameNotInProgressException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionMessage message = new ExceptionMessage(
			HttpStatus.GONE.value(),
			ex.getMessage(),
			request.getDescription(false));

		return new ResponseEntity<ExceptionMessage>(message, HttpStatus.GONE);
	}

	private ResponseEntity<?> handleGameNotFoundException(GameNotFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionMessage message = new ExceptionMessage(
				HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<ExceptionMessage>(message, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<?> handleColumnOutOfBoundException(ColumnOutOfBoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionMessage message = new ExceptionMessage(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<ExceptionMessage>(message, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ExceptionMessage> handleColumnAlreadyFilledException(ColumnAlreadyFilledException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionMessage message = new ExceptionMessage(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<ExceptionMessage>(message, HttpStatus.BAD_REQUEST);
	}
	
}
