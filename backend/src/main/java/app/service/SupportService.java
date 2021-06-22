package app.service;

import app.dto.main.MessageResponse;
import app.dto.support.SupportOrderDto;
import app.dto.support.SupportRequest;
import app.exceptions.AppException;
import app.exceptions.UnAuthorizedException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SupportService {

    ResponseEntity<MessageResponse> saveAppeal(SupportRequest supportRequest, HttpServletRequest request) throws AppException;

    ResponseEntity<MessageResponse> saveAnswer(long orderId, String text, String token, HttpServletRequest request) throws AppException;

    ResponseEntity<List<SupportOrderDto>> getOrderList(String token) throws UnAuthorizedException;

    ResponseEntity<SupportOrderDto> getOrderWithMessageList(long orderId, String token) throws AppException;

    ResponseEntity<MessageResponse> closeOrder(long id, String token, HttpServletRequest request) throws AppException;

}
