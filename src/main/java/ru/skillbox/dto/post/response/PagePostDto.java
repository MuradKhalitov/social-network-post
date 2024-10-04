package ru.skillbox.dto.post.response;

import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.dto.TagDto;
import ru.skillbox.model.ReactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
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

    @Getter
    @Setter
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
        private List<TagDto> tags;
        private boolean myLike;
        private String myReaction;
        private int likeAmount;
        private List<ReactionType> reactionType;
        private String imagePath;
        private LocalDateTime publishDate;
    }
}

