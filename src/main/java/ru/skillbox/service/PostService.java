package ru.skillbox.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.client.AccountFeignClient;
import ru.skillbox.client.FriendsFeignClient;
import ru.skillbox.dto.AccountDto;
import ru.skillbox.dto.AccountSearchDto;
import ru.skillbox.dto.TagDto;
import ru.skillbox.dto.kafka.BotPost;
import ru.skillbox.dto.kafka.NotificationPost;
import ru.skillbox.dto.post.request.PostDto;
import ru.skillbox.dto.post.request.PostSearchDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.exception.AccessDeniedException;
import ru.skillbox.exception.ErrorMessage;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AccountFeignClient accountFeignClient;
    private final FriendsFeignClient friendsFeignClient;
    private static final String POSTED = "POSTED";

    @Autowired
    public PostService(PostRepository postRepository, TagRepository tagRepository, PostMapper postMapper, CurrentUsers currentUsers, KafkaTemplate<String, Object> kafkaTemplate, AccountFeignClient accountFeignClient, FriendsFeignClient friendsFeignClient) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
        this.currentUsers = currentUsers;
        this.kafkaTemplate = kafkaTemplate;
        this.accountFeignClient = accountFeignClient;
        this.friendsFeignClient = friendsFeignClient;
    }

    public PagePostDto searchPosts(PostSearchDto postSearchDto, Pageable pageable) {
        UUID currentUserId = currentUsers.getCurrentUserId();

        // Если задано имя автора, получаем его UUID через Feign-клиент
        if (postSearchDto.getAuthor() != null && !postSearchDto.getAuthor().isEmpty()) {
            List<UUID> authorIds = getAuthorIds(postSearchDto.getAuthor());
            authorIds.add(currentUserId);
            postSearchDto.setAccountIds(authorIds);
            log.info(authorIds.toString());
        }

        List<UUID> friends = Boolean.TRUE.equals(postSearchDto.getWithFriends()) ? friendsFeignClient.getFriendsIds(currentUserId) : Collections.emptyList();

        Page<Post> postPage = postRepository.findAll(PostSpecification.filterBySearchDto(postSearchDto, currentUserId, friends), pageable);

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
                    .toList();

            // Проверка даты публикации и обновление типа поста, если тип еще не "POSTED"
            if (!POSTED.equals(post.getType()) && post.getPublishDate() != null && post.getPublishDate().isBefore(LocalDateTime.now().plusHours(3))) {
                post.setType(POSTED);
                postRepository.save(post);
            }

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
                            .toList(),
                    isMyLike,
                    myReaction,
                    post.getLikeAmount(),
                    reactionType,
                    post.getImagePath(),
                    post.getPublishDate()
            );
        }).toList();

        pagePostDto.setContent(content);
        return pagePostDto;
    }

    public PostDto getPostById(Long postId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessage.POST_NOT_FOUND.format(postId)));
        post.updateCommentsCount();
        post.updateLikeAmount();
        boolean isMyLike = post.getLikes().stream()
                .anyMatch(likePost -> likePost.getAuthorId().equals(currentUserId));
        post.setMyLike(isMyLike);
        return postMapper.convertToDTO(post);
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
            post.setPublishDate(LocalDateTime.now().plusHours(3));
            post.setType(POSTED);
        }
        post.setTags(tags);
        Post createdPost = postRepository.save(post);
        log.info("Пользователь: {}, добавил новость", currentUserId);

        kafkaTemplate.send("notification-topic", NotificationPost.builder()
                .authorId(currentUserId.toString())
                .notificationType("POST")
                .content(post.getPostText())
                .build());

        AccountDto accountDto = accountFeignClient.getAccountById(currentUserId);

        String fullName = accountDto.getFirstName() + " " + accountDto.getLastName();

        kafkaTemplate.send("bot-topic", BotPost.builder()
                .authorName(fullName)
                .title(post.getTitle())
                .postText(post.getPostText())
                .build());

        return postMapper.convertToDTO(createdPost);
    }

    @Transactional
    public PostDto updatePost(PostDto updatePostDto) {
        Long postId = updatePostDto.getId();
        UUID currentUserId = currentUsers.getCurrentUserId();
        Post updatedPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessage.POST_NOT_FOUND.format(postId)));
        UUID updatedPostAuthor = updatedPost.getAuthorId();

        if (currentUserId.equals(updatedPostAuthor) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            updatedPost.setTitle(updatePostDto.getTitle());
            updatedPost.setPostText(updatePostDto.getPostText());
            updatedPost.setImagePath(updatePostDto.getImagePath());

            List<Tag> existingTags = updatedPost.getTags();

            Set<Tag> newTags = updatePostDto.getTags().stream()
                    .map(tagDto -> tagRepository.findByName(tagDto.getName())
                            .orElseGet(() -> {
                                Tag newTag = new Tag();
                                newTag.setName(tagDto.getName());
                                return tagRepository.save(newTag);
                            }))
                    .collect(Collectors.toSet());

            existingTags.removeIf(tag -> !newTags.contains(tag));

            newTags.forEach(tag -> {
                if (!existingTags.contains(tag)) {
                    existingTags.add(tag);
                }
            });

            updatedPost.setTags(existingTags);

            return postMapper.convertToDTO(postRepository.save(updatedPost));
        } else {
            throw new AccessDeniedException("У вас нет разрешения на обновление этого поста");
        }
    }

    public void deletePost(Long postId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Post deletedPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessage.POST_NOT_FOUND.format(postId)));
        UUID deletedPostAuthor = deletedPost.getAuthorId();
        if (currentUserId.equals(deletedPostAuthor) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            postRepository.delete(deletedPost);
        } else {
            throw new AccessDeniedException("У вас нет разрешения на удаление этого поста");
        }
    }

    public List<UUID> getAuthorIds(String author) {
        AccountSearchDto searchDto = new AccountSearchDto();
        searchDto.setAuthor(author);

        Pageable pageable = PageRequest.of(0, 30, Sort.by("firstName").ascending()); // Настройки пагинации

        Page<AccountDto> accounts = accountFeignClient.searchAccount(searchDto, pageable);

        return accounts.getContent().stream()
                .map(AccountDto::getId)
                .toList();
    }
}

