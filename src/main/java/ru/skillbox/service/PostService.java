package ru.skillbox.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.PostDto;
import ru.skillbox.dto.SearchDto;
import ru.skillbox.dto.response.BriefPostDTO;
import ru.skillbox.dto.response.PostResponse;
import ru.skillbox.exception.NewsNotFoundException;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.Post;
import ru.skillbox.model.Tag;
import ru.skillbox.model.User;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.repository.PostSpecification;
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
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, TagRepository tagRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
    }

    public PostDto createNews(PostDto postDto) {
        Post post = postMapper.convertToEntity(postDto);
        Long userId = CurrentUsers.getCurrentUserId();
        User user = userRepository.findById(userId).get();
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
        log.info("Пользователь: {}, добавил новость", user.getUsername());
        Post createdPost = postRepository.save(post);

        return postMapper.convertToDTO(createdPost);
    }

    public PostResponse searchPosts(SearchDto searchDto, Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(PostSpecification.filterBySearchDto(searchDto), pageable);

        // Формирование PostResponse
        PostResponse postResponse = new PostResponse();
        postResponse.setTotalElements(postPage.getTotalElements());
        postResponse.setTotalPages(postPage.getTotalPages());
        postResponse.setNumber(postPage.getNumber());
        postResponse.setSize(postPage.getSize());
        postResponse.setFirst(postPage.isFirst());
        postResponse.setLast(postPage.isLast());
        postResponse.setNumberOfElements(postPage.getNumberOfElements());
        postResponse.setPageable(pageable);
        postResponse.setEmpty(postPage.isEmpty());

        // Маппим Post в PostContent
        List<PostResponse.PostContent> content = postPage.getContent().stream().map(post -> new PostResponse.PostContent(
                post.getId(),
                post.getTime(),
                post.getTimeChanged(),
                post.getAuthor().getId(),
                post.getTitle(),
                "POSTED",  // Тип можно изменить на основе логики
                post.getPostText(),
                post.isBlocked(),
                post.isDelete(),
                post.getCommentsCount() != null ? post.getCommentsCount() : 0,
                post.getTags().stream().map(Tag::getName).collect(Collectors.toList()),
                post.getLikeAmount() != null ? post.getLikeAmount() : 0,
                false,  // Здесь можно добавить логику для определения, поставил ли пользователь лайк
                post.getImagePath(),
                post.getPublishDate()
        )).collect(Collectors.toList());

        postResponse.setContent(content);
        return postResponse;
    }

//    public List<BriefPostDTO> getAllNews(PageRequest pageRequest) {
//        Page<Post> page = postRepository.findAll(pageRequest);
//        List<BriefPostDTO> briefPostDTOList = page.getContent().stream()
//                .map(postMapper::convertToBriefDTO)
//                .collect(Collectors.toList());
//        return briefPostDTOList;
//    }
//    public ResponseList<PostResponse> filter(SearchDto request) {
//        Page<Post> page = postRepository.findAll(
//                HotelSpecification.withRequest(request),
//                PageRequest.of(request.getPageNumber(), request.getPageSize())
//        );
//        ResponseList<PostResponse> response = new ResponseList<>();
//        response.setItems(page.getContent().stream().map(this::hotelToResponse).toList());
//        response.setTotalCount(page.getTotalElements());
//        return response;
//    }


    public PostDto getPostById(Long id) {
        return postRepository.findById(id)
                .map(postMapper::convertToDTO)
                .orElseThrow(() -> new NewsNotFoundException("News with id " + id + " not found"));
    }

    @Transactional
    public PostDto updateNews(Long id, PostDto updatePostDto) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername).get();

        Post oldPost = postRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("Post with id " + id + "not found"));
        User authorNews = oldPost.getAuthor();

        if (currentUser.getId().equals(authorNews.getId())) {
            oldPost.setTitle(updatePostDto.getTitle());
            oldPost.setPostText(updatePostDto.getPostText());
            oldPost.setImagePath(updatePostDto.getImagePath());
            Post updatedPost = postRepository.saveAndFlush(oldPost);

            return postMapper.convertToDTO(updatedPost);
        }
        return null;
    }


    public void deleteNews(Long id) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername).get();
        Post deletedPost = postMapper.convertToEntity(getPostById(id));
        User authorNews = deletedPost.getAuthor();
        if (currentUser.getId().equals(authorNews.getId()) || CurrentUsers.hasRole("ADMIN") || CurrentUsers.hasRole("MODERATOR")) {
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

