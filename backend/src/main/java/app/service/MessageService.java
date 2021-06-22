package app.service;

import app.dto.messages.MessageAddedResponse;
import app.dto.messages.MessageRequest;
import app.exceptions.BadRequestException;
import app.exceptions.UnAuthorizedException;
import org.springframework.http.ResponseEntity;

public interface MessageService {
    ResponseEntity<MessageAddedResponse> postMessage(int id, MessageRequest messageRequest, String token) throws UnAuthorizedException, BadRequestException;
}
