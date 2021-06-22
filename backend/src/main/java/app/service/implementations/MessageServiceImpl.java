package app.service.implementations;

import app.dto.messages.MessageAddedResponse;
import app.dto.messages.MessageDto;
import app.dto.messages.MessageRequest;
import app.exceptions.BadRequestException;
import app.exceptions.UnAuthorizedException;
import app.model.Dialog;
import app.model.Message;
import app.model.Person;
import app.model.enums.MessageReadStatus;
import app.repository.DialogRepository;
import app.repository.MessageRepository;
import app.service.MainService;
import app.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {

    private final MainService mainService;
    private final DialogRepository dialogRepo;
    private final MessageRepository messageRepo;

    public MessageServiceImpl(MainService mainService, DialogRepository dialogRepo, MessageRepository messageRepo) {
        this.mainService = mainService;
        this.dialogRepo = dialogRepo;
        this.messageRepo = messageRepo;
    }

    /**
     * Метод сохраняет новые сообщения в базе
     *
     * @param dialogId идентификатор диалога к которому принадлежит сообщение.
     * @param request  содержит текст сообщения.
     * @param token    токен текущего юзера.
     * @return статус ОК и заполненный DTO с сообщением
     * @throws UnAuthorizedException если пользователь не авторизован.
     * @throws BadRequestException   если некорректный id диалога.
     */
    @Override
    public ResponseEntity<MessageAddedResponse> postMessage(int dialogId, MessageRequest request, String token)
            throws UnAuthorizedException, BadRequestException {

        Dialog dialog = dialogRepo.findById(dialogId)
                .orElseThrow(() -> new BadRequestException("Dialog (id = " + dialogId + ") not found"));

        Person author = mainService.getPersonByToken(token);

        Person interlocutor = mainService.getPersonById(
                dialog.getDialogStarterId() == author.getId() ? dialog.getDialogTargetId() : dialog.getDialogStarterId());

        Message message = new Message();
        message.setTime(new Date());
        message.setText(request.getMessageText());
        message.setMessageReadStatus(MessageReadStatus.SENT);
        message.setAuthor(author);
        message.setRecipient(interlocutor);
        message.setDialog(dialog);
        messageRepo.saveAndFlush(message);

        return new ResponseEntity<>(
                new MessageAddedResponse(
                        new MessageDto(message, (int) author.getId())),
                HttpStatus.OK);
    }
}
