package ru.skillbox.dto.post.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PostSearchDto {
    private List<Long> ids;
    private List<Long> accountIds;
    private List<Long> blockedIds;
    private String author;
    private String title;
    private String postText;
    private Boolean withFriends;
    private Boolean isDeleted;
    private List<String> tags;
    private Long dateFrom;
    private Long dateTo;
}
