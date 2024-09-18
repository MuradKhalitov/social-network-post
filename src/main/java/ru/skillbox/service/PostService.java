package ru.skillbox.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.post.request.PostDto;
import ru.skillbox.dto.post.request.PostSearchDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.exception.NewsNotFoundException;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.Account;
import ru.skillbox.model.Post;
import ru.skillbox.model.Tag;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.repository.PostSpecification;
import ru.skillbox.repository.TagRepository;
import ru.skillbox.repository.AccountRepository;
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
    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final CurrentUsers currentUsers;

    @Autowired
    public PostService(PostRepository postRepository, AccountRepository accountRepository, TagRepository tagRepository, PostMapper postMapper, CurrentUsers currentUsers) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
        this.currentUsers = currentUsers;
    }
    public PostDto createNews(PostDto postDto) {
        Post post = postMapper.convertToEntity(postDto);
        UUID userId = currentUsers.getCurrentUserId();
        Account account = accountRepository.findById(userId).get();

        post.setAuthor(account);
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
        log.info("Пользователь: {}, добавил новость", account.getEmail());
        Post createdPost = postRepository.save(post);

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
                post.getAuthor().getId(),
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
                .orElseThrow(() -> new NewsNotFoundException("News with id " + id + " not found"));
    }

    @Transactional
    public PostDto updateNews(Long id, PostDto updatePostDto) {
        UUID userId = currentUsers.getCurrentUserId();
        Account currentAccount = accountRepository.findById(userId).get();

        Post oldPost = postRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("Post with id " + id + "not found"));
        Account authorNews = oldPost.getAuthor();

        if (currentAccount.getId().equals(authorNews.getId())) {
            oldPost.setTitle(updatePostDto.getTitle());
            oldPost.setPostText(updatePostDto.getPostText());
            oldPost.setImagePath(updatePostDto.getImagePath());
            Post updatedPost = postRepository.saveAndFlush(oldPost);

            return postMapper.convertToDTO(updatedPost);
        }
        return null;
    }


    public void deleteNews(Long id) {
        UUID userId = currentUsers.getCurrentUserId();
        Account currentAccount = accountRepository.findById(userId).get();
        Post deletedPost = postRepository.findById(id).get();
        Account authorNews = deletedPost.getAuthor();
        if (currentAccount.getId().equals(authorNews.getId()) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            postRepository.deleteById(id);
        } else new NewsNotFoundException("News with id " + id + " not found");

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

