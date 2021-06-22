package app.service;

import app.dto.dialogs.*;
import app.dto.messages.MessagesByDialogResponse;
import app.dto.messages.UnreadMessagesCountResponse;
import app.exceptions.AppException;
import app.exceptions.UnAuthorizedException;
import org.springframework.http.ResponseEntity;

public interface DialogService {

    ResponseEntity<DialogPostedResponse> createDialog(DialogRequest dialogRequest, String token) throws UnAuthorizedException;

    ResponseEntity<DialogResponse> getDialogs(String token, String query, int offset, int perPage) throws AppException;

    ResponseEntity<UnreadMessagesCountResponse> getUnreadMessages(String token) throws UnAuthorizedException;

    ResponseEntity<MessagesByDialogResponse> getMessagesByDialog(String token, int id, String query, int offset, int itemPerPage) throws UnAuthorizedException;

    ResponseEntity<DialogPostedResponse> deleteDialog(int id);

    void printInfo(String text);

}
