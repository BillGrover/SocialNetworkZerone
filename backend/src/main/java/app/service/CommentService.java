package app.service;

import app.dto.comment.CommentDto;
import app.dto.comment.CommentListResponse;
import app.dto.comment.CommentResponse;
import app.dto.comment.RemoveCommentResponse;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity<CommentListResponse> getComments(long id, int offset, int itemPerPage, String token)
            throws AppException;

    ResponseEntity<CommentResponse> addComments(long id, CommentDto commentDto, String token)
            throws AppException;

    ResponseEntity<CommentResponse> updateComments(long commentId, CommentDto commentDto, String token, long postId)
            throws AppException;

    ResponseEntity<RemoveCommentResponse> removeComments(long commentId, String token, long postId) throws BadRequestException;

    ResponseEntity<CommentResponse> recoverComment(long id, long commentId, String token)
            throws AppException;

}
