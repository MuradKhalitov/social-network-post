package ru.skillbox.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skillbox.dto.PostDto;
import ru.skillbox.dto.response.BriefNewsDTO;
import ru.skillbox.exception.NewsNotFoundException;
import ru.skillbox.mapper.NewsMapper;
import ru.skillbox.model.Post;
import ru.skillbox.model.User;
import ru.skillbox.repository.NewsRepository;
import ru.skillbox.repository.UserRepository;
import ru.skillbox.util.CurrentUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsMapper newsMapper;
    private final UserService userService;

    @Autowired
    public NewsService(NewsRepository newsRepository, UserRepository userRepository, NewsMapper newsMapper, UserService userService) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.newsMapper = newsMapper;
        this.userService = userService;
    }

    public PostDto createNews(PostDto postDto) {
        Post post = newsMapper.convertToEntity(postDto);
        String currentUsername = CurrentUsers.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername).get();
        post.setAuthor(user);
        log.info("Пользователь: {}, добавил новость", currentUsername);
        Post createdPost = newsRepository.save(post);
        return newsMapper.convertToDTO(createdPost);
    }

    public List<BriefNewsDTO> getAllNews(PageRequest pageRequest) {
        Page<Post> page = newsRepository.findAll(pageRequest);
        List<BriefNewsDTO> briefNewsDTOList = page.getContent().stream()
                .map(newsMapper::convertToBriefDTO)
                .collect(Collectors.toList());
        return briefNewsDTOList;
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

    public List<BriefNewsDTO> getFilteredNewsByAuthor(Long authorIds) {
        if (!(authorIds == null)) {
            List<Post> filterPostList = newsRepository.findByAuthorId(authorIds);
            List<BriefNewsDTO> briefNewsDTOList = filterPostList.stream()
                    .map(newsMapper::convertToBriefDTO)
                    .collect(Collectors.toList());
            return briefNewsDTOList;
        }
        return new ArrayList<>();
    }
}

