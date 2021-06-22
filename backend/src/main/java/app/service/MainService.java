package app.service;

import app.dto.admin.AllStatisticsResponse;
import app.dto.admin.PersonListForAdmin;
import app.exceptions.BadRequestException;
import app.exceptions.UnAuthorizedException;
import app.model.*;
import app.model.enums.NotificationType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface MainService {

    String createToken(Person person);

    Person getPersonByToken(String token) throws UnAuthorizedException;

    Person getPersonById(long id) throws BadRequestException;

    Person getPersonByEmail(String email) throws BadRequestException;

    Post getPostById(long id) throws BadRequestException;

    PostComment getPostCommentById(long id) throws BadRequestException;

    Optional<String> getCookie(HttpServletRequest request, String name);

    String convertToSqlLikeFormat(String string);

    void addNotification(Person person, Person author, NotificationType type);

    Person hidePersonInfo(Person person);

    Person restorePersonInfo(Person person) throws BadRequestException;

    ResponseEntity<PersonListForAdmin> getPersonList();

    AllStatisticsResponse getPersonsCommentsPostsCount();

}