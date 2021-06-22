package app.service;

import app.dto.likes.LikeDto;
import app.dto.likes.LikeResponse;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;

public interface PostLikeService {

    ResponseEntity<LikeResponse> isLiked(Long userId, Long itemId, String type) throws BadRequestException;

    ResponseEntity<LikeResponse> getLikesList(Long itemId, String type) throws BadRequestException;

    ResponseEntity<LikeResponse> addLike(String token, LikeDto likeDto) throws AppException;

    ResponseEntity<LikeResponse> removeLike(String token, Long itemId, String type) throws AppException;

}
