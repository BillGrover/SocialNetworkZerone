package app.service.implementations;

import app.dto.notification.NotificationDto;
import app.dto.notification.NotificationListResponse;
import app.dto.profile.PersonDto;
import app.exceptions.AppException;
import app.model.Notification;
import app.model.Person;
import app.repository.NotificationRepository;
import app.service.MainService;
import app.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${notification.lifeMS}")
    private Long notificationLife;

    private final MainService mainService;
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(MainService mainService,
                                   NotificationRepository notificationRepository) {
        this.mainService = mainService;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public ResponseEntity<NotificationListResponse> notificationAll(String token)
            throws AppException {

        Person person = mainService.getPersonByToken(token);
        NotificationListResponse response = getNotificationListResponse(person, false);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<NotificationListResponse> notificationDelete(long id, boolean all, String token)
            throws AppException {
        Person person = mainService.getPersonByToken(token);
        if (all) {
            NotificationListResponse response = getNotificationListResponse(person, true);
            return ResponseEntity.ok(response);
        } else {
            notificationRepository.deleteById(id);
        }
        return ResponseEntity.ok(new NotificationListResponse());

    }


    private NotificationListResponse getNotificationListResponse(Person person, boolean statusRead) {
        List<Notification> notificationList = notificationRepository
                .findAllByTargetPersonAndReadStatusNotOrderByIdDesc(person, "DEL");
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        for (Notification notification : notificationList) {
            // ???????????????? ?????????????? ???????????? ???????????????????? ?? ?????????????? ???????????????? ????????????????????
            long notificationAge = System.currentTimeMillis() - notification.getTime().getTime();
            if (notificationAge > notificationLife) {
                notification.setReadStatus("DEL");
                notificationRepository.save(notification);
            } else {
                NotificationDto notificationDto = new NotificationDto();
                notificationDto.setId(notification.getId());
                notificationDto.setSentTime(notification.getTime().getTime());
                Person author = notification.getAuthorPerson();
                notificationDto.setEntityAuthor(new PersonDto(author));
                String info;
                switch (notification.getNotificationType().toString()) {
                    case ("POST"):
                        info = "?????????????????????? ?????????? ????????.";
                        break;
                    case ("POST_COMMENT"):
                        info = "?????????????????????? ?????????? ???????????????????? ?? ??????????.";
                        break;
                    case ("COMMENT_COMMENT"):
                        info = "???? ???????????????? ?????????????????????? ???? ?????? ????????????????????.";
                        break;
                    case ("FRIEND_REQUEST"):
                        info = "?????????? ???????????? ?? ???????????? ???? " + author.getFirstName() + " " + author.getLastName();
                        break;
                    case ("MESSAGE"):
                        info = "???? ???????????????? ?????????? ??????????????????.";
                        break;
                    case ("FRIEND_BIRTHDAY"):
                        info = "?? ???????????? ?????????? " + author.getFirstName() + " " + author.getLastName() + " ???????? ????????????????.";
                        break;
                    case ("LIKE"):
                        info = "????????";
                        break;
                    default:
                        info = "?????? ???? ?????????? ???? ??????";
                }
                notificationDto.setInfo(info);
                notificationDto.setEventType(notification.getNotificationType().toString());
                notificationDto.setReadStatus(notification.getReadStatus());
                notificationDtoList.add(notificationDto);

                if (statusRead) {
                    notification.setReadStatus("READ");
                    notificationRepository.save(notification);
                }
            }
        }

        NotificationListResponse response = new NotificationListResponse();
        response.setTimestamp(System.currentTimeMillis());
        response.setTotal(notificationRepository.countByTargetPerson(person));
        response.setData(notificationDtoList);
        return response;
    }


}
