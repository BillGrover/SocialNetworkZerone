package app.service.implementations;

import app.config.AppConstant;
import app.dto.likes.*;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import app.model.Person;
import app.model.Post;
import app.model.PostComment;
import app.model.PostLike;
import app.model.enums.BlockStatus;
import app.model.enums.NotificationType;
import app.repository.PostLikeRepository;
import app.service.MainService;
import app.service.PostLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    private static final String TYPE_POST = "Post";
    private static final String TYPE_COMMENT = "Comment";
    private static final String BAD_TYPE_ERROR = "Действия с контентом недоступны";

    private final PostLikeRepository postLikeRepository;
    private final MainService mainService;

    public PostLikeServiceImpl(PostLikeRepository postLikeRepository,
                               MainService mainService) {
        this.postLikeRepository = postLikeRepository;
        this.mainService = mainService;
    }

    @Override
    public ResponseEntity<LikeResponse> isLiked(Long userId, Long id, String type) throws BadRequestException {
        Person person = mainService.getPersonById(userId);
        if (type != null && type.equals(TYPE_POST)) {
            Post post = mainService.getPostById(id);
            Optional<PostLike> postLike = postLikeRepository.findByPostAndPerson(post, person);
            return ResponseEntity.ok(new LikeResponse(new IsLikeData(postLike.isPresent())));
        }
        if (type != null && type.equals(TYPE_COMMENT)) {
            PostComment postComment = mainService.getPostCommentById(id);
            Optional<PostLike> postLike = postLikeRepository.findByPostCommentIdAndPerson(postComment, person);
            return ResponseEntity.ok(new LikeResponse(new IsLikeData(postLike.isPresent())));
        }
        throw new BadRequestException(BAD_TYPE_ERROR);
    }

    @Override
    public ResponseEntity<LikeResponse> getLikesList(Long id, String type) throws BadRequestException {
        if (type != null && type.equals(TYPE_POST)) {
            Post post = mainService.getPostById(id);
            LikeResponse response = createLikeResponseByPostLikeList(post);
            return ResponseEntity.ok(response);
        }
        if (type != null && type.equals(TYPE_COMMENT)) {
            PostComment postComment = mainService.getPostCommentById(id);
            LikeResponse response = createLikeResponseByCommentLikeList(postComment);
            return ResponseEntity.ok(response);
        }
        throw new BadRequestException(BAD_TYPE_ERROR);
    }

    @Override
    public ResponseEntity<LikeResponse> addLike(String token, LikeDto likeDto) throws AppException {
        Person person = mainService.getPersonByToken(token);
        if (likeDto.getType().equals(TYPE_POST)) {
            Post post = mainService.getPostById(likeDto.getItemId());
            if (!post.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)) {
                Optional<PostLike> like = postLikeRepository.findByPostAndPerson(post, person);
                if (like.isEmpty()) {
                    PostLike postLike = new PostLike();
                    postLike.setPost(post);
                    postLike.setTime(new Date());
                    postLike.setPerson(person);
                    postLikeRepository.save(postLike);
                    //оповещение
                    mainService.addNotification(post.getAuthor(), person, NotificationType.LIKE);
                    LikeResponse response = createLikeResponseByPostLikeList(post);
                    return ResponseEntity.ok(response);
                }
            } else {
                throw new BadRequestException(String.format(AppConstant.MESSAGE, post.getAuthor().getId()));
            }

        } else if (likeDto.getType().equals(TYPE_COMMENT)) {
            PostComment postComment = mainService.getPostCommentById(likeDto.getItemId());
            if (!postComment.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)) {
                Optional<PostLike> like = postLikeRepository.findByPostCommentIdAndPerson(postComment, person);
                if (like.isEmpty()) {
                    PostLike postLike = new PostLike();
                    postLike.setPostCommentId(postComment);
                    postLike.setTime(new Date());
                    postLike.setPerson(person);
                    postLikeRepository.save(postLike);
                    //оповещение
                    mainService.addNotification(postComment.getAuthor(), person, NotificationType.LIKE);
                    LikeResponse response = createLikeResponseByCommentLikeList(postComment);
                    return ResponseEntity.ok(response);
                }
            } else {
                throw new BadRequestException(String.format(AppConstant.MESSAGE, postComment.getAuthor().getId()));
            }
        }
        throw new BadRequestException(BAD_TYPE_ERROR);
    }

    @Override
    public ResponseEntity<LikeResponse> removeLike(String token, Long id, String type) throws AppException {
        Person person = mainService.getPersonByToken(token);
        if (type.equals(TYPE_POST)) {
            Post post = mainService.getPostById(id);
            Optional<PostLike> like = postLikeRepository.findByPostAndPerson(post, person);
            if (like.isPresent()) {
                postLikeRepository.delete(like.get());
                return ResponseEntity.ok(new LikeResponse(new DeleteLikeData(String.valueOf(post.getLikes().size()))));
            }
        } else if (type.equals(TYPE_COMMENT)) {
            PostComment postComment = mainService.getPostCommentById(id);
            Optional<PostLike> like = postLikeRepository.findByPostCommentIdAndPerson(postComment, person);
            if (like.isPresent()) {
                postLikeRepository.delete(like.get());
                return ResponseEntity.ok(
                        new LikeResponse(new DeleteLikeData(String.valueOf(postComment.getPostLikeList().size())))
                );
            }
        }
        throw new BadRequestException(BAD_TYPE_ERROR);
    }

    /**
     * Методы класса
     */

    private List<String> createPersonIdListByLikeList(List<PostLike> postLikeList) {
        List<String> users = new ArrayList<>();
        postLikeList.forEach(postLike -> users.add(String.valueOf(postLike.getPerson().getId())));
        return users;
    }

    private LikeResponse createLikeResponseByPostLikeList(Post post) {
        List<PostLike> postLikeList = post.getLikes();
        List<String> personIdList = createPersonIdListByLikeList(postLikeList);
        return new LikeResponse(new LikeData(String.valueOf(postLikeList.size()), personIdList));
    }

    private LikeResponse createLikeResponseByCommentLikeList(PostComment postComment) {
        List<PostLike> postCommentPostLikeList = postComment.getPostLikeList();
        List<String> personIdList = createPersonIdListByLikeList(postCommentPostLikeList);
        return new LikeResponse(new LikeData(String.valueOf(postCommentPostLikeList.size()), personIdList));
    }

}
