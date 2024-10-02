package ru.skillbox.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.TagDto;
import ru.skillbox.dto.post.request.PostDto;
import ru.skillbox.dto.post.request.PostSearchDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.exception.AccessDeniedException;
import ru.skillbox.exception.PostNotFoundException;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.LikePost;
import ru.skillbox.model.Post;
import ru.skillbox.model.ReactionType;
import ru.skillbox.model.Tag;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.repository.PostSpecification;
import ru.skillbox.repository.TagRepository;
import ru.skillbox.util.CurrentUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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

    public PostDto createPost(PostDto postDto) {
        Post post = postMapper.convertToEntity(postDto);
        UUID currentUserId = currentUsers.getCurrentUserId();
        post.setAuthorId(currentUserId);
        List<Tag> tags = new ArrayList<>();
        for (TagDto tagName : postDto.getTags()) {
            Tag tag = tagRepository.findByName(tagName.getName())
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName.getName());
                        return newTag;
                    });
            tags.add(tag);
        }
        if (post.getPublishDate() == null) {
            post.setPublishDate(LocalDateTime.now());
            post.setType("POSTED");
        }
        post.setTags(tags);
        Post createdPost = postRepository.save(post);
        log.info("Пользователь: {}, добавил новость", currentUserId);
        return postMapper.convertToDTO(createdPost);
    }

    public PagePostDto searchPosts(PostSearchDto postSearchDto, Pageable pageable) {
        UUID currentUserId = currentUsers.getCurrentUserId();
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

        List<PagePostDto.PostContent> content = postPage.getContent().stream().map(post -> {
            post.updateCommentsCount();
            post.updateLikeAmount();

            // Получаем реакцию текущего пользователя
            Optional<LikePost> myLikePost = post.getLikes().stream()
                    .filter(likePost -> likePost.getAuthorId().equals(currentUserId))
                    .findFirst();

            // Определяем тип реакции, если лайк есть
            String myReaction = myLikePost.map(LikePost::getReactionType).orElse(null);
            boolean isMyLike = myLikePost.isPresent();

            // Формируем список реакций
            List<ReactionType> reactionType = post.getLikes().stream()
                    .collect(Collectors.groupingBy(LikePost::getReactionType, Collectors.counting()))
                    .entrySet().stream()
                    .map(entry -> new ReactionType(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            return new PagePostDto.PostContent(
                    post.getId(),
                    post.getTime(),
                    post.getTimeChanged(),
                    post.getAuthorId(),
                    post.getTitle(),
                    post.getType(),
                    post.getPostText(),
                    post.isBlocked(),
                    post.isDeleted(),
                    post.getCommentsCount(),
                    post.getTags().stream()
                            .map(tag -> new TagDto(tag.getName()))
                            .collect(Collectors.toList()),
                    isMyLike,
                    myReaction,
                    post.getLikeAmount(),
                    reactionType,
                    post.getImagePath(),
                    post.getPublishDate()
            );
        }).collect(Collectors.toList());

        pagePostDto.setContent(content);
        return pagePostDto;
    }

    public PostDto getPostById(Long id) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
        post.updateCommentsCount();
        post.updateLikeAmount();
        boolean isMyLike = post.getLikes().stream()
                .anyMatch(likePost -> likePost.getAuthorId().equals(currentUserId));
        post.setMyLike(isMyLike);
        return postMapper.convertToDTO(post);
    }

    @Transactional
    public PostDto updatePost(PostDto updatePostDto) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Post updatedPost = postRepository.findById(updatePostDto.getId())
                .orElseThrow(() -> new PostNotFoundException("Post with postId " + updatePostDto.getId() + "not found"));
        UUID updatedPostAuthor = updatedPost.getAuthorId();
        if (currentUserId.equals(updatedPostAuthor) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            updatedPost.setTitle(updatePostDto.getTitle());
            updatedPost.setPostText(updatePostDto.getPostText());
            updatedPost.setImagePath(updatePostDto.getImagePath());
            return postMapper.convertToDTO(postRepository.save(updatedPost));
        } else {
            throw new AccessDeniedException("У вас нет разрешения на обновление этого поста");
        }
    }

    public void deletePost(Long postId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Post deletedPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with postId " + postId + "not found"));
        UUID deletedPostAuthor = deletedPost.getAuthorId();
        if (currentUserId.equals(deletedPostAuthor) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            postRepository.deleteById(postId);
        } else {
            throw new AccessDeniedException("У вас нет разрешения на удаление этого поста");
        }
    }
}

