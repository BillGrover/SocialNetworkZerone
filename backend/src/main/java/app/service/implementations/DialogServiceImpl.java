package app.service.implementations;

import app.dto.dialogs.*;
import app.dto.messages.*;
import app.exceptions.*;
import app.model.*;
import app.model.enums.MessageReadStatus;
import app.repository.DialogRepository;
import app.repository.MessageRepository;
import app.service.DialogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DialogServiceImpl implements DialogService {

    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;
    private final MainServiceImpl mainService;

    private final Logger logger = LogManager.getLogger(DialogServiceImpl.class);
    private final Marker info = MarkerManager.getMarker("DIALOG");

    public DialogServiceImpl(
            DialogRepository dialogRepository,
            MessageRepository messageRepository,
            MainServiceImpl mainService) {
        this.dialogRepository = dialogRepository;
        this.messageRepository = messageRepository;
        this.mainService = mainService;
    }

    /**
     * Создание диалога
     *
     * @param dialogRequest запрос содержит данные для создания диалога.
     * @param token         токен текущего юзера.
     * @return данные о созданном диалоге.
     */
    @Override
    public ResponseEntity<DialogPostedResponse> createDialog(DialogRequest dialogRequest, String token)
            throws UnAuthorizedException {

        //Пока непноятно будет ли приходить больше одного recipientId в dialogRequest,
        // поэтому просто берём по индексу 0.
        int dialogStarterId = (int) mainService.getPersonByToken(token).getId();
        int dialogTargetId = dialogRequest.getUsersIds().get(0);

        Dialog dialog = dialogRepository.findByBothPersonId(dialogStarterId, dialogTargetId);
        if(dialog == null){
            dialog = new Dialog(dialogStarterId, dialogTargetId);
            dialogRepository.saveAndFlush(dialog);
        }
        DialogPostedResponse response = new DialogPostedResponse(dialog.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Получение списка диалогов
     */
    @Override
    public ResponseEntity<DialogResponse> getDialogs(String token, String query, int offset, int perPage)
            throws AppException {
        Person currentPerson = mainService.getPersonByToken(token);
        int currentPersonId = (int) currentPerson.getId();

        if (dialogRepository.count() == 0)
            return new ResponseEntity<>(new DialogResponse(0, perPage, new ArrayList<>()), HttpStatus.OK);

        List<Dialog> dialogs = dialogRepository.findAll(currentPersonId);
        List<DialogDto> data = new ArrayList<>();

        for (Dialog dialog : dialogs) {
            Message lastMessage = messageRepository.findLastMessage(dialog.getId());
            if (lastMessage == null)
                lastMessage = createWelcomeMessage(dialog);

            int dialogTargetId = dialog.getDialogTargetId();
            Person interlocutor = mainService.getPersonById(
                    currentPersonId == dialogTargetId ? dialog.getDialogStarterId() : dialogTargetId);

            data.add(
                    new DialogDto(
                            dialog.getId(),
                            messageRepository.countUnread(currentPersonId, dialog.getId()),
                            lastMessage,
                            currentPerson,
                            interlocutor));
        }

        DialogResponse response =
                new DialogResponse(
                        dialogRepository.countByCurrentPerson(currentPersonId),
                        perPage,
                        data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Метод создаёт первое сообщение в диалоге от имени создателя диалога и сохраняет его в базе.
     */
    private Message createWelcomeMessage(Dialog dialog) throws BadRequestException {
        Message message = new Message();
        message.setAuthor(mainService.getPersonById(dialog.getDialogStarterId()));
        message.setRecipient(mainService.getPersonById(dialog.getDialogTargetId()));
        message.setText("Привет!");
        message.setMessageReadStatus(MessageReadStatus.SENT);
        message.setTime(new Date());
        message.setDialog(dialog);
        messageRepository.saveAndFlush(message);
        return message;
    }

    /**
     * Получение количества непрочитанных сообщений
     */
    @Override
    public ResponseEntity<UnreadMessagesCountResponse> getUnreadMessages(String token) throws UnAuthorizedException {
        int currentPersonId = (int) mainService.getPersonByToken(token).getId();
        UnreadMessagesCountResponse response
                = new UnreadMessagesCountResponse(
                new UnreadMessageCountDto(
                        messageRepository.countUnread(currentPersonId)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Получение всех сообщений конкретного диалога.
     */
    @Override
    public ResponseEntity<MessagesByDialogResponse> getMessagesByDialog(
            String token, int dialogId, String query, int offset, int itemPerPage) throws UnAuthorizedException {

        int currentPersonId = (int) mainService.getPersonByToken(token).getId();

        List<Message> messages = messageRepository.findAllByDialogId(dialogId);
        //изменения статуса сообщений одним стримом:
        messages.stream()
                .filter(m -> m.getRecipient().getId() == currentPersonId)
                .forEach(m -> {
                    m.setMessageReadStatus(MessageReadStatus.READ);
                    messageRepository.saveAndFlush(m);
                });
        List<MessageDto> data = messages.stream().map(m -> new MessageDto(m, currentPersonId)).collect(Collectors.toList());

        MessagesByDialogResponse response = new MessagesByDialogResponse(
                data.size(),
                itemPerPage,
                data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Удаление диалога со всеми его сообщениями
     */
    @Override
    public ResponseEntity<DialogPostedResponse> deleteDialog(int id) {
        messageRepository.removeByDialogId(id);
        dialogRepository.deleteById(id);
        return new ResponseEntity<>(new DialogPostedResponse(id), HttpStatus.OK);
    }

    public void printInfo(String text) {
        text = String.format("*  Срабатывание контроллера: %s  *", text);
        logger.info(info, text);
    }

}
