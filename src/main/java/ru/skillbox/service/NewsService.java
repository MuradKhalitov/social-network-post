package ru.skillbox.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skillbox.dto.PostDto;
import ru.skillbox.dto.response.BriefPostDTO;
import ru.skillbox.exception.NewsNotFoundException;
import ru.skillbox.mapper.NewsMapper;
import ru.skillbox.model.Post;
import ru.skillbox.model.Tag;
import ru.skillbox.model.User;
import ru.skillbox.repository.NewsRepository;
import ru.skillbox.repository.TagRepository;
import ru.skillbox.repository.UserRepository;
import ru.skillbox.util.CurrentUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final NewsMapper newsMapper;
    private final UserService userService;

    @Autowired
    public NewsService(NewsRepository newsRepository, UserRepository userRepository, TagRepository tagRepository, NewsMapper newsMapper, UserService userService) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.newsMapper = newsMapper;
        this.userService = userService;
    }

    public PostDto createNews(PostDto postDto) {
        Post post = newsMapper.convertToEntity(postDto);
        String currentUsername = CurrentUsers.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername).get();
        post.setAuthor(user);
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
        log.info("Пользователь: {}, добавил новость", currentUsername);
        Post createdPost = newsRepository.save(post);

        return newsMapper.convertToDTO(createdPost);
    }

    public List<BriefPostDTO> getAllNews(PageRequest pageRequest) {
        Page<Post> page = newsRepository.findAll(pageRequest);
        List<BriefPostDTO> briefPostDTOList = page.getContent().stream()
                .map(newsMapper::convertToBriefDTO)
                .collect(Collectors.toList());
        return briefPostDTOList;
    }

    public PostDto getNewsById(Long id) {
        return newsRepository.findById(id)
                .map(newsMapper::convertToDTO)
                .orElseThrow(() -> new NewsNotFoundException("News with id " + id + " not found"));
    }

    public PostDto updateNews(Long id, PostDto updatePostDto) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User currentUser = userService.findByUsername(currentUsername);

        Post oldPost = newsMapper.convertToEntity(getNewsById(id));
        User authorNews = oldPost.getAuthor();

        if (currentUser.getId().equals(authorNews.getId())) {
            oldPost.setTitle(updatePostDto.getTitle());
            oldPost.setPostText(updatePostDto.getPostText());
            Post updatedPost = newsRepository.save(oldPost);

            return newsMapper.convertToDTO(updatedPost);
        }
        return null;
    }


    public ResponseEntity<Void> deleteNews(Long id) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User currentUser = userService.findByUsername(currentUsername);
        Post deletedPost = newsMapper.convertToEntity(getNewsById(id));
        User authorNews = deletedPost.getAuthor();
        if (currentUser.getId().equals(authorNews.getId()) || CurrentUsers.hasRole("ADMIN") || CurrentUsers.hasRole("MODERATOR")) {
            newsRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    public List<BriefPostDTO> getFilteredNewsByAuthor(Long authorIds) {
        if (!(authorIds == null)) {
            List<Post> filterPostList = newsRepository.findByAuthorId(authorIds);
            List<BriefPostDTO> briefPostDTOList = filterPostList.stream()
                    .map(newsMapper::convertToBriefDTO)
                    .collect(Collectors.toList());
            return briefPostDTOList;
        }
        return new ArrayList<>();
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
        Post post = newsRepository.findById(postId).orElseThrow(() -> new NewsNotFoundException("Post not found"));
        List<Tag> tags = tagNames.stream()
                .map(this::createOrGetTag)
                .collect(Collectors.toList());
        post.setTags(tags);
        return newsRepository.save(post);
    }
}

