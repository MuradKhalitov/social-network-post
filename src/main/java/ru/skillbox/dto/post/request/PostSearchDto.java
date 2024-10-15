package ru.skillbox.dto.post.request;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PostSearchDto {
    private List<Long> ids;
    private List<UUID> accountIds;
    private List<Long> blockedIds;
    private String author;
    private String title;
    private String postText;
    private Boolean withFriends;
    private Boolean isDeleted;
    private List<String> tags;
    private OffsetDateTime dateFrom;
    private OffsetDateTime dateTo;
}
