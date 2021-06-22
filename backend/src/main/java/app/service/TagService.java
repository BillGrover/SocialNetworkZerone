package app.service;

import app.dto.main.MessageResponse;
import app.dto.tags.TagDto;
import app.dto.tags.TagResponse;
import app.dto.tags.TagListResponse;
import app.exceptions.BadRequestException;
import app.model.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TagService {
    ResponseEntity<TagListResponse> getTags(String tag, int offset, int itemPerPage);

    ResponseEntity<TagResponse> addTag(TagDto tagDto);

    ResponseEntity<MessageResponse> removeTag(Long id) throws BadRequestException;

    void addTags2Posts(Post post, List<String> newTags);

}
