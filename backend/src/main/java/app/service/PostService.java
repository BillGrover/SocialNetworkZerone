package app.service;

import app.dto.FeedsResponseDto;
import app.dto.main.PaginationSettings;
import app.dto.post.PostListResponse;
import app.dto.post.PostResponse;
import app.exceptions.AppException;
import app.exceptions.UnAuthorizedException;
import org.springframework.http.ResponseEntity;

public interface PostService {

    ResponseEntity<FeedsResponseDto> getPosts(String name, int offset, int itemPerPage, String token) throws UnAuthorizedException;

    ResponseEntity<PostListResponse> getPostSearch(
            String textPublisher,
            long datePublisherFrom,
            long datePublisherTo,
            String author,
            String tags,
            PaginationSettings settings,
            String token) throws UnAuthorizedException;

    ResponseEntity<PostResponse> getPostById(Long id, String token) throws AppException;

    void deleteComments();


}
