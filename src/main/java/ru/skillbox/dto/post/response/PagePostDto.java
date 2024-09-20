package ru.skillbox.dto.post.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagePostDto {

    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private List<PostContent> content;
    private Sort sort;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private Pageable pageable;
    private boolean empty;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostContent {
        private long id;
        private LocalDateTime time;
        private LocalDateTime timeChanged;
        private UUID authorId;
        private String title;
        private String type;
        private String postText;
        private boolean isBlocked;
        private boolean isDeleted;
        private int commentsCount;
        private List<String> tags;
        private int likeAmount;
        private boolean myLike;
        private String imagePath;
        private LocalDateTime publishDate;
    }
}

