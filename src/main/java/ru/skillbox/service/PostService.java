package ru.skillbox.service;

import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.PostDto;
import ru.skillbox.exception.PostNotFoundException;
import ru.skillbox.model.Comment;
import ru.skillbox.model.Post;
import ru.skillbox.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<PostDto> getPostsByAuthorId(Long authorId) {
        if (!(authorId == null)) {
            return postRepository.findByAuthorId(authorId).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
//        return postRepository.findByAuthorId(authorId).stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
    }

    public PostDto getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(this::convertToDto).orElseThrow(() -> new PostNotFoundException("Post with id: " + id + " not found"));//orElse(null);
    }

    public PostDto createPost(PostDto postDto) {
        postDto.setTime(LocalDateTime.now());
        postDto.setTimeChanged(LocalDateTime.now());
        Post post = convertToEntity(postDto);
        post = postRepository.save(post);
        return convertToDto(post);
    }

    public PostDto updatePost(Long id, PostDto postDto) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setTitle(postDto.getTitle());
            post.setPostText(postDto.getPostText());
            post.setTimeChanged(LocalDateTime.now());
            post.setTags(postDto.getTags());
            post = postRepository.save(post);
            return convertToDto(post);
        } else {
            return null;
        }
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // Преобразование между Entity и DTO
    private PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setAuthorId(post.getAuthorId());
        postDto.setTitle(post.getTitle());
        postDto.setPostText(post.getPostText());
        postDto.setTime(post.getTime());
        postDto.setTimeChanged(post.getTimeChanged());
        postDto.setBlocked(post.isBlocked());
        postDto.setDelete(post.isDelete());
        postDto.setLikeAmount(post.getLikeAmount());
        postDto.setTags(post.getTags());
        return postDto;
    }

    private Post convertToEntity(PostDto postDto) {
        Post post = new Post();
        post.setAuthorId(postDto.getAuthorId());
        post.setTitle(postDto.getTitle());
        post.setPostText(postDto.getPostText());
        post.setTime(postDto.getTime());
        post.setTimeChanged(postDto.getTimeChanged());
        post.setBlocked(postDto.isBlocked());
        post.setDelete(postDto.isDelete());
        post.setLikeAmount(postDto.getLikeAmount());
        post.setTags(postDto.getTags());
        return post;
    }
}
