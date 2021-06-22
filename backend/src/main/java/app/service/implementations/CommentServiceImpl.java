package app.service.implementations;

import app.config.AppConstant;
import app.dto.comment.*;
import app.exceptions.AppException;
import app.exceptions.BadRequestException;
import app.model.Person;
import app.model.Post;
import app.model.PostComment;
import app.model.enums.BlockStatus;
import app.model.enums.DeleteStatus;
import app.model.enums.NotificationType;
import app.repository.PostCommentRepository;
import app.service.CommentService;
import app.service.MainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    private final PostCommentRepository postCommentRepository;
    private final MainService mainService;

    public CommentServiceImpl(PostCommentRepository postCommentRepository,
                              MainService mainService) {
        this.mainService = mainService;
        this.postCommentRepository = postCommentRepository;
    }

    @Override
    public ResponseEntity<CommentListResponse> getComments(long id, int offset, int itemPerPage, String token)
            throws AppException {
        Person person = mainService.getPersonByToken(token);
        Post post = mainService.getPostById(id);
        Page<PostComment> postCommentPage = postCommentRepository.findAllByPost(post, PageRequest.of(offset, itemPerPage));
        List<CommentData> commentDataList = new ArrayList<>();
        postCommentPage.forEach(comment -> commentDataList.add(new CommentData(comment, person)));
        CommentListResponse response = new CommentListResponse(commentDataList);
        response.setOffset(offset);
        response.setTotal(postCommentRepository.countAllByPost(post));
        response.setPerPage(itemPerPage);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommentResponse> addComments(long id, CommentDto commentDto, String token)
            throws AppException {
        Person person = mainService.getPersonByToken(token);
        Post post = mainService.getPostById(id);

        if (post.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)) {
            throw new BadRequestException(String.format(AppConstant.MESSAGE, post.getAuthor().getId()));
        } else {
            if (commentDto.getParentId() != null) {
                PostComment parentComment = mainService.getPostCommentById(commentDto.getParentId());
                if (parentComment.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)) {
                    throw new BadRequestException(String.format(AppConstant.MESSAGE, parentComment.getAuthor().getId()));
                }
            }
            PostComment comment = new PostComment();
            comment.setText(commentDto.getCommentText());
            comment.setParentId(
                    commentDto.getParentId() == null
                            ? null
                            : mainService.getPostCommentById(commentDto.getParentId()));
            comment.setTime(new Date());
            comment.setAuthor(person);
            comment.setPost(commentDto.getParentId() == null ? post : null);
            comment.setDeleteStatus(DeleteStatus.UNDELETED);
            comment.setBlock(BlockStatus.UNBLOCKED);
            postCommentRepository.save(comment);

            //оповещение
            NotificationType type = (commentDto.getParentId() == null
                    ? NotificationType.POST_COMMENT
                    : NotificationType.COMMENT_COMMENT);
            mainService.addNotification(post.getAuthor(), person, type);

            return ResponseEntity.ok(new CommentResponse(new CommentData(comment, person)));
        }
    }

    @Override
    public ResponseEntity<CommentResponse> updateComments(long id, CommentDto dto, String token, long postId)
            throws AppException {

        Person person = mainService.getPersonByToken(token);
        Post post = mainService.getPostById(postId);
        if (!post.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)) {
            PostComment comment = mainService.getPostCommentById(id);
            if (comment.getDeleteStatus().equals(DeleteStatus.UNDELETED)) {
                comment.setText(dto.getCommentText());
                comment.setTime(new Date());
                postCommentRepository.save(comment);
                return ResponseEntity.ok(new CommentResponse(new CommentData(comment, person)));
            } else {
                throw new BadRequestException(String.format("Комментарий id:%d удалён", id));
            }
        } else {
            throw new BadRequestException(String.format(AppConstant.MESSAGE, post.getAuthor().getId()));
        }
    }

    @Override
    public ResponseEntity<RemoveCommentResponse> removeComments(long id, String token, long postId)
            throws BadRequestException {
        Post post = mainService.getPostById(postId);
        if (!post.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)) {
            PostComment comment = mainService.getPostCommentById(id);
            comment.setDeleteStatus(DeleteStatus.DELETED);
            comment.setDeleteDate(new Date());

            deleteChild(comment);
            postCommentRepository.save(comment);
            RemoveCommentData data = new RemoveCommentData(id);
            RemoveCommentResponse response = new RemoveCommentResponse(data);

            return ResponseEntity.ok(response);
        } else {
            throw new BadRequestException(String.format(AppConstant.MESSAGE, post.getAuthor().getId()));
        }

    }

    @Override
    public ResponseEntity<CommentResponse> recoverComment(long postId, long commentId, String token)
            throws AppException {
        Person person = mainService.getPersonByToken(token);
        Post post = mainService.getPostById(postId);
        if (!post.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)) {
            PostComment comment = mainService.getPostCommentById(commentId);
            comment.setDeleteStatus(DeleteStatus.UNDELETED);
            recoverChild(comment);
            postCommentRepository.save(comment);
            return ResponseEntity.ok(new CommentResponse(new CommentData(comment, person)));
        } else {
            throw new BadRequestException(String.format(AppConstant.MESSAGE, post.getAuthor().getId()));
        }
    }

    private void deleteChild(PostComment postComment) {
        List<PostComment> childList = postCommentRepository.findAllByParentId(postComment);
        if (!childList.isEmpty()) {
            childList.forEach(comment -> {
                if (!postCommentRepository.findAllByParentId(comment).isEmpty()) {
                    deleteChild(comment);
                } else {
                    comment.setDeleteStatus(DeleteStatus.DELETED);
                    comment.setDeleteDate(new Date());
                    postCommentRepository.save(comment);
                }
            });
        } else {
            postComment.setDeleteStatus(DeleteStatus.DELETED);
            postComment.setDeleteDate(new Date());
            postCommentRepository.save(postComment);
        }
    }

    private void recoverChild(PostComment postComment) {
        List<PostComment> childList = postCommentRepository.findAllByParentId(postComment);
        if (!childList.isEmpty()) {
            childList.forEach(comment -> {
                if (!postCommentRepository.findAllByParentId(comment).isEmpty()) {
                    recoverChild(comment);
                } else {
                    comment.setDeleteStatus(DeleteStatus.UNDELETED);
                    postCommentRepository.save(comment);
                }
            });
        } else {
            postComment.setDeleteStatus(DeleteStatus.UNDELETED);
            postCommentRepository.save(postComment);
        }
    }

}
