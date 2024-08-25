package ru.skillbox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.LikeDto;
import ru.skillbox.model.Like;
import ru.skillbox.repository.LikeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public List<LikeDto> getLikesByPostId(Long postId) {
        return likeRepository.findByPostId(postId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LikeDto> getLikesByCommentId(Long commentId) {
        return likeRepository.findByCommentId(commentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LikeDto getLikeById(Long id) {
        Optional<Like> like = likeRepository.findById(id);
        return like.map(this::convertToDto).orElse(null);
    }

    public LikeDto createLike(LikeDto likeDto) {
        Like like = convertToEntity(likeDto);
        like = likeRepository.save(like);
        return convertToDto(like);
    }

    public void deleteLike(Long id) {
        likeRepository.deleteById(id);
    }

    // Преобразование между Entity и DTO
    private LikeDto convertToDto(Like like) {
        LikeDto likeDto = new LikeDto();
        likeDto.setId(like.getId());
        likeDto.setUserId(like.getUserId());
        likeDto.setPostId(like.getPostId());
        likeDto.setCommentId(like.getCommentId());
        return likeDto;
    }

    private Like convertToEntity(LikeDto likeDto) {
        Like like = new Like();
        like.setUserId(likeDto.getUserId());
        like.setPostId(likeDto.getPostId());
        like.setCommentId(likeDto.getCommentId());
        return like;
    }
}
