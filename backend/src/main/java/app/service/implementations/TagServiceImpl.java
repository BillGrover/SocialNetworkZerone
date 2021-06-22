package app.service.implementations;

import app.dto.main.MessageResponse;
import app.dto.tags.*;
import app.exceptions.BadRequestException;
import app.model.*;
import app.repository.Post2TagRepository;
import app.repository.TagRepository;
import app.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final Post2TagRepository post2TagRepository;

    public TagServiceImpl(TagRepository tagRepository, Post2TagRepository post2TagRepository) {
        this.tagRepository = tagRepository;
        this.post2TagRepository = post2TagRepository;
    }

    @Override
    public ResponseEntity<TagListResponse> getTags(String tag, int offset, int itemPerPage) {
        Page<Tag> tagPage = tagRepository.findAllByNameIgnoreCase(tag, PageRequest.of(offset, itemPerPage));
        List<TagData> tags = new ArrayList<>();
        tagPage.forEach(tag1 -> tags.add(new TagData(tag1)));
        TagListResponse response = new TagListResponse(tags);
        response.setTotal(tagRepository.findAllByNameIgnoreCase(tag).size());
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TagResponse> addTag(TagDto tagDto) {
        Tag tag = new Tag(tagDto.getTag());
        tagRepository.save(tag);
        TagResponse response = new TagResponse(new TagData(tag));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MessageResponse> removeTag(Long id) throws BadRequestException {

        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isPresent()) {
            if (!tagOptional.get().getPost2tags().isEmpty()) {
                Set<PostToTag> postToTagList = tagOptional.get().getPost2tags();
                postToTagList.forEach(post2TagRepository::delete);
            }
            tagRepository.delete(tagOptional.get());
            return ResponseEntity.ok(MessageResponse.ok());
        } else {
            throw new BadRequestException(String.format("Тег id:%d не найден", id));
        }

    }

    @Override
    public void addTags2Posts(Post post, List<String> newTagNameList) {
        if (newTagNameList != null) {
            removeTagListFromPost(post);
            newTagNameList.forEach(newTagName -> {
                Tag tag;
                Optional<Tag> optionalTag = tagRepository.findFirstByNameIgnoreCase(newTagName);
                if (optionalTag.isPresent()) {
                    tag = optionalTag.get();
                } else {
                    tag = new Tag(newTagName);
                    tagRepository.save(tag);
                }
                PostToTag postToTag = new PostToTag(post, tag);
                post2TagRepository.save(postToTag);
            });
        }
    }

    private void removeTagListFromPost(Post post) {
        post2TagRepository.deleteAllByPost(post);
    }

}
