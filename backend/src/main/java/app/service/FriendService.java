package app.service;

import app.dto.friend.FriendRequest;
import app.dto.friend.FriendStatusListResponse;
import app.dto.main.MessageResponse;
import app.dto.profile.PersonListResponse;
import app.exceptions.AppException;
import app.exceptions.UnAuthorizedException;
import org.springframework.http.ResponseEntity;

public interface FriendService {

    ResponseEntity<PersonListResponse> friendsAll(String name, int offset, int itemPerPage, String token) throws UnAuthorizedException;

    ResponseEntity<MessageResponse> friendDelete(Long idFriend, String token) throws AppException;

    ResponseEntity<MessageResponse> friendAdd(Long friendId, String token) throws AppException;

    ResponseEntity<PersonListResponse> friendsRequest(String name, int offset, int itemPerPage, String token) throws UnAuthorizedException;

    ResponseEntity<FriendStatusListResponse> friendAndFriend(FriendRequest request, String token) throws UnAuthorizedException;

    ResponseEntity<PersonListResponse> friendsRecommendations(int offset, int itemPerPage, String token) throws UnAuthorizedException;


}
