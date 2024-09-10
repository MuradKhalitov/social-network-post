package ru.skillbox.dto.comment.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCommentDto {

    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private List<CommentContent> content;
    private Sort sort;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private Pageable pageable;
    private boolean empty;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentContent {
        private long id;
        private String commentType;
        private LocalDateTime time;
        private LocalDateTime timeChanged;
        private long authorId;
        private long parentId;
        private String commentText;
        private long postId;
        private boolean isBlocked;
        private boolean isDelete;
        private int likeAmount;
        private boolean myLike;
        private int commentsCount;
        private String imagePath;
    }
}

