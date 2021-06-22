package app.service.implementations;

import app.dto.FeedsResponseDto;
import app.dto.main.PaginationSettings;
import app.dto.post.*;
import app.exceptions.AppException;
import app.exceptions.UnAuthorizedException;
import app.model.*;
import app.model.enums.DeleteStatus;
import app.repository.*;
import app.service.MainService;
import app.service.PostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private static final long DATE_01_01_1900 = -2208988800000L;

    @Value("${limit.comment.lifetimePerSec}")
    private int lifeTimePerSec;

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MainService mainService;
    private final PostCommentRepository postCommentRepository;
    private final TagRepository tagRepository;

    public PostServiceImpl(PostLikeRepository postLikeRepository,
                           PostRepository postRepository,
                           MainService mainService,
                           PostCommentRepository postCommentRepository,
                           TagRepository tagRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.mainService = mainService;
        this.postCommentRepository = postCommentRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public ResponseEntity<FeedsResponseDto> getPosts(String name, int offset, int limit, String token)
            throws UnAuthorizedException {
        deleteComments();
        Pageable pagination = PageRequest.of(offset / limit, limit);
        Person person = mainService.getPersonByToken(token);
        List<Post> postList;
        int total;
        if (name == null) {
            postList = postRepository.findAllByTimeBeforeOrderByTimeDesc(new Date(), pagination);
            total = postRepository.countAllBy();
        } else {
            postList = postRepository.findAllByTitleContainingAndTimeBeforeOrderByTimeDesc(name, new Date(), pagination);
            total = postRepository.countByTitleContaining(name);
        }

        FeedsResponseDto response = new FeedsResponseDto();
        response.setTimestamp(System.currentTimeMillis());
        response.setTotal(total);
        response.setOffset(offset);
        response.setPerPage(limit);
        response.setData(
                postList
                        .stream()
                        .map(post -> {
                            PostDto postDto = null;
                            postDto = new PostDto(post, person);
                            return postDto;
                        })
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PostListResponse> getPostSearch(String text,
                                                          long millisecondsFrom,
                                                          long millisecondsTo,
                                                          String author,
                                                          String tags,
                                                          PaginationSettings settings,
                                                          String token) throws UnAuthorizedException {
        Date dateFrom;
        Date dateTo;
        if (millisecondsFrom == 0) {
            dateFrom = new Date(DATE_01_01_1900);
        } else {
            dateFrom = new Date(millisecondsFrom);
        }
        if (millisecondsTo == 0) {
            dateTo = new Date();
        } else {
            dateTo = new Date(millisecondsTo);
        }

        List<Post> postList
                = postRepository.search(
                mainService.convertToSqlLikeFormat(text),
                dateFrom,
                dateTo,
                mainService.convertToSqlLikeFormat(author));

        if (tags != null && tags.length() > 0 && postList.isEmpty()) {
            String[] tagArray = tags.split(",");
            Set<Tag> tagSet = new HashSet<>();
            Set<Post> postSet = new HashSet<>();
            for (String tagName : tagArray) {
                Optional<Tag> tagOptional = tagRepository.findFirstByNameIgnoreCase(tagName);
                tagOptional.ifPresent(tagSet::add);
            }
            if (!tagSet.isEmpty()) {
                for (Post post : postList) {
                    Set<Tag> postTagSet = post.getPostToTags()
                            .stream()
                            .map(PostToTag::getTag)
                            .collect(Collectors.toSet());
                    for (Tag tag : postTagSet) {
                        if (tagSet.contains(tag)) {
                            postSet.add(post);
                        }
                    }
                }
            }
            postList = postSet
                    .stream()
                    .sorted((o1, o2) -> o2.getTime().compareTo(o1.getTime()))
                    .collect(Collectors.toList());
        }

        Person person = mainService.getPersonByToken(token);
        PostListResponse response = new PostListResponse();
        response.setData(
                postList
                        .stream()
                        .map(post -> new PostResponseData(post, person))
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PostResponse> getPostById(Long id, String token) throws AppException {
        Person person = mainService.getPersonByToken(token);
        Post post = mainService.getPostById(id);
        PostResponse response = new PostResponse(new PostResponseData(post, person));
        return ResponseEntity.ok(response);
    }


    public void deleteComments() {
        List<PostComment> deleteCommentsList = postCommentRepository.findAllByDeleteStatus(DeleteStatus.DELETED);
        deleteCommentsList.forEach(postComment -> {
            Date deleteTime = new Date(postComment.getDeleteDate().getTime() + lifeTimePerSec * 1000L);
            if (deleteTime.before(new Date())) {
                postLikeRepository.deleteAll(postLikeRepository.findAllByPostCommentId(postComment));
                deleteChild(postComment);
                postCommentRepository.delete(postComment);
            }
        });
    }

    private void deleteChild(PostComment postComment) {
        List<PostComment> childList = postCommentRepository.findAllByParentId(postComment);
        if (!childList.isEmpty()) {
            childList.forEach(comment -> {
                if (!postCommentRepository.findAllByParentId(comment).isEmpty()) {
                    deleteChild(comment);
                } else {
                    postLikeRepository.deleteAll(postLikeRepository.findAllByPostCommentId(postComment));
                    postCommentRepository.delete(comment);
                }
            });
        } else {
            postLikeRepository.deleteAll(postLikeRepository.findAllByPostCommentId(postComment));
            postCommentRepository.delete(postComment);
        }
    }

}
