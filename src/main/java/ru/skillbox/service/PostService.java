package ru.skillbox.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.post.request.PostDto;
import ru.skillbox.dto.post.request.PostSearchDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.exception.AccessDeniedException;
import ru.skillbox.exception.NewsNotFoundException;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.Post;
import ru.skillbox.model.Tag;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.repository.PostSpecification;
import ru.skillbox.repository.TagRepository;
import ru.skillbox.util.CurrentUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final CurrentUsers currentUsers;

    @Autowired
    public PostService(PostRepository postRepository, TagRepository tagRepository, PostMapper postMapper, CurrentUsers currentUsers) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
        this.currentUsers = currentUsers;
    }

    public PostDto createNews(PostDto postDto) {
        Post post = postMapper.convertToEntity(postDto);
        UUID currentUserId = currentUsers.getCurrentUserId();
        post.setAuthorId(currentUserId);
        post.setCommentsCount(0);
        List<Tag> tags = new ArrayList<>();
        for (String tagName : postDto.getTags()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return newTag;
                    });
            tags.add(tag);
        }
        post.setTags(tags);
        Post createdPost = postRepository.save(post);
        log.info("Пользователь: {}, добавил новость", currentUserId);
        return postMapper.convertToDTO(createdPost);
    }

    public PagePostDto searchPosts(PostSearchDto postSearchDto, Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(PostSpecification.filterBySearchDto(postSearchDto), pageable);

        // Формирование PagePostDto
        PagePostDto pagePostDto = new PagePostDto();
        pagePostDto.setTotalElements(postPage.getTotalElements());
        pagePostDto.setTotalPages(postPage.getTotalPages());
        pagePostDto.setNumber(postPage.getNumber());
        pagePostDto.setSize(postPage.getSize());
        pagePostDto.setFirst(postPage.isFirst());
        pagePostDto.setLast(postPage.isLast());
        pagePostDto.setNumberOfElements(postPage.getNumberOfElements());
        pagePostDto.setPageable(pageable);
        pagePostDto.setEmpty(postPage.isEmpty());

        // Маппим Post в PostContent
        List<PagePostDto.PostContent> content = postPage.getContent().stream().map(post -> new PagePostDto.PostContent(
                post.getId(),
                post.getTime(),
                post.getTimeChanged(),
                post.getAuthorId(),
                post.getTitle(),
                "POSTED",  // Тип можно изменить на основе логики
                post.getPostText(),
                post.isBlocked(),
                post.isDeleted(),
                post.getCommentsCount() != null ? post.getCommentsCount() : 0,
                post.getTags().stream().map(Tag::getName).collect(Collectors.toList()),
                post.getLikeAmount() != null ? post.getLikeAmount() : 0,
                false,  // Здесь можно добавить логику для определения, поставил ли пользователь лайк
                post.getImagePath(),
                post.getPublishDate()
        )).collect(Collectors.toList());

        pagePostDto.setContent(content);
        return pagePostDto;
    }


    public PostDto getPostById(Long id) {
        return postRepository.findById(id)
                .map(postMapper::convertToDTO)
                .orElseThrow(() -> new NewsNotFoundException("Post with id " + id + " not found"));
    }

    public PostDto updateNews(Long postId, PostDto updatePostDto) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Post updatedPost = postRepository.findById(postId)
                .orElseThrow(() -> new NewsNotFoundException("Post with postId " + postId + "not found"));
        UUID updatedPostAuthor = updatedPost.getAuthorId();
        System.out.println("currentUserId: " + currentUserId);
        System.out.println("PostAuthor   : " + updatedPostAuthor);

        if (currentUserId.equals(updatedPostAuthor)
        ){
                //|| currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            updatedPost.setTitle(updatePostDto.getTitle());
            updatedPost.setPostText(updatePostDto.getPostText());
            updatedPost.setImagePath(updatePostDto.getImagePath());
            return postMapper.convertToDTO(postRepository.save(updatedPost));
        } else {
            throw new AccessDeniedException("У вас нет разрешения на обновление этого поста");
        }
    }

    public void deleteNews(Long postId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Post deletedPost = postRepository.findById(postId)
                .orElseThrow(() -> new NewsNotFoundException("Post with postId " + postId + "not found"));
        ;
        UUID deletedPostAuthor = deletedPost.getAuthorId();
        if (currentUserId.equals(deletedPostAuthor) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            postRepository.deleteById(postId);
        } else {
            throw new AccessDeniedException("У вас нет разрешения на удаление этого поста");
        }
    }

    public Tag createOrGetTag(String tagName) {
        Optional<Tag> existingTag = tagRepository.findByName(tagName);
        return existingTag.orElseGet(() -> {
            Tag newTag = new Tag();
            newTag.setName(tagName);
            return tagRepository.save(newTag);
        });
    }

    public Post addTagsToPost(Long postId, List<String> tagNames) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NewsNotFoundException("Post not found"));
        List<Tag> tags = tagNames.stream()
                .map(this::createOrGetTag)
                .collect(Collectors.toList());
        post.setTags(tags);
        return postRepository.save(post);
    }
}

