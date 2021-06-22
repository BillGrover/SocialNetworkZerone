package app.service.implementations;

import app.config.AppConstant;
import app.config.EmailTemplate;
import app.dto.main.MessageResponse;
import app.dto.support.*;
import app.exceptions.*;
import app.model.*;
import app.model.enums.SupportOrderStatus;
import app.repository.SupportMessageRepository;
import app.repository.SupportOrderRepository;
import app.service.AdminService;
import app.service.SupportService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SupportServiceImpl implements SupportService {

    private static final String EMAIL_SUBJECT_TEMPLATE = "Обращение в службу поддержки #%d";

    private final SupportOrderRepository supportOrderRepository;
    private final SupportMessageRepository supportMessageRepository;
    private final EmailServiceImpl emailService;
    private final AdminService adminService;

    public SupportServiceImpl(SupportOrderRepository supportOrderRepository,
                              SupportMessageRepository supportMessageRepository,
                              EmailServiceImpl emailService,
                              AdminService adminService) {
        this.supportOrderRepository = supportOrderRepository;
        this.supportMessageRepository = supportMessageRepository;

        this.emailService = emailService;
        this.adminService = adminService;
    }

    @Override
    public ResponseEntity<MessageResponse> saveAppeal(SupportRequest supportRequest, HttpServletRequest request)
            throws AppException {
        Optional<SupportOrder> orderOptional = supportOrderRepository
                .findByEmailAndStatusNot(supportRequest.getEmail(), SupportOrderStatus.CLOSED);
        SupportOrder order;
        if (orderOptional.isPresent()) {
            order = orderOptional.get();
        } else {
            order = new SupportOrder();
            order.setStatus(SupportOrderStatus.NEW);
            order.setDate(new Date());
            order.setEmail(supportRequest.getEmail());
            order.setName(supportRequest.getName());
            order.setTitle(
                    supportRequest.getMessage().length() > 100
                            ? supportRequest.getMessage().substring(0, 97) + "..."
                            : supportRequest.getMessage()
            );
            order = supportOrderRepository.saveAndFlush(order);
        }

        SupportMessage supportMessage = new SupportMessage();
        supportMessage.setDate(new Date());
        supportMessage.setText(supportRequest.getMessage());
        supportMessage.setOrder(order);

        supportMessageRepository.save(supportMessage);

        sendOrderNotificationEmail(order, request);

        return ResponseEntity.ok(MessageResponse.ok());
    }

    @Override
    public ResponseEntity<MessageResponse> saveAnswer(long orderId, String text, String token, HttpServletRequest request)
            throws AppException {

        adminService.checkAdminByToken(token);
        SupportOrder order = findOrderById(orderId);
        Admin admin = adminService.findAdminById(1L);
        SupportMessage message = new SupportMessage();
        message.setDate(new Date());
        message.setAdmin(admin);
        message.setText(text);
        message.setOrder(order);
        supportMessageRepository.save(message);

        order.setStatus(SupportOrderStatus.OPEN);
        supportOrderRepository.save(order);

        sendOrderNotificationEmail(order, request);

        return ResponseEntity.ok(new MessageResponse("Ответ отправлен"));
    }


    @Override
    public ResponseEntity<List<SupportOrderDto>> getOrderList(String token) throws UnAuthorizedException {
        adminService.checkAdminByToken(token);
        return ResponseEntity.ok(supportOrderRepository.findAll()
                .stream()
                .sorted((o1, o2) -> Long.compare(o2.getId(), o1.getId()))
                .map(SupportOrderDto::new)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<SupportOrderDto> getOrderWithMessageList(long orderId, String token)
            throws AppException {
        adminService.checkAdminByToken(token);
        SupportOrder order = findOrderById(orderId);
        List<SupportMessageDto> messageList = supportMessageRepository.findByOrderId(orderId)
                .stream().map(SupportMessageDto::new).collect(Collectors.toList());
        SupportOrderDto response = new SupportOrderDto(order);
        response.setMessageList(messageList);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MessageResponse> closeOrder(long id, String token, HttpServletRequest request)
            throws AppException {

        Admin admin = adminService.checkAdminByToken(token);
        SupportOrder order = findOrderById(id);

        SupportMessage message = new SupportMessage();
        message.setDate(new Date());
        message.setOrder(order);
        message.setAdmin(admin);
        message.setText("Обращение закрыто");
        supportMessageRepository.save(message);

        order.setStatus(SupportOrderStatus.CLOSED);
        supportOrderRepository.save(order);

        sendOrderNotificationEmail(order, request);

        return ResponseEntity.ok(new MessageResponse(String.format("Обращение #%d закрыто", id)));
    }

    private void sendOrderNotificationEmail(SupportOrder order, HttpServletRequest request) {
        String email = order.getEmail();
        long id = order.getId();
        String subject = String.format(EMAIL_SUBJECT_TEMPLATE, id);
        List<SupportMessage> list = supportMessageRepository.findByOrderId(id);
        StringBuilder messageBox = new StringBuilder();
        list.forEach(supportMessage -> {
            if (supportMessage.getAdmin() != null) {
                messageBox.append(
                        String.format(EmailTemplate.SUPPORT_ROW_GREEN.getTemplate(),
                                supportMessage.getAdmin().getName(),
                                supportMessage.getText(),
                                new SimpleDateFormat(AppConstant.DATE_FORMAT).format(supportMessage.getDate()))
                );
            } else {
                messageBox.append(
                        String.format(EmailTemplate.SUPPORT_ROW.getTemplate(),
                                supportMessage.getText(),
                                new SimpleDateFormat(AppConstant.DATE_FORMAT).format(supportMessage.getDate()))
                );
            }
        });
        String link = emailService.getHostAndPort(request) + "/contacting-support";
        String body = String.format(
                EmailTemplate.SUPPORT_BODY.getTemplate(),
                id,
                messageBox.toString(),
                link
        );
        emailService.sendHtmlEmail(email, subject, body);
    }

    private SupportOrder findOrderById(long id) throws BadRequestException {
        return supportOrderRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Обращение id:%d не найденно", id)));
    }

}
