package ru.skillbox.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class SearchDto {
    private List<Long> ids;
    private List<Long> accountIds;
    private List<Long> blockedIds;
    private String author;
    private String title;
    private String postText;
    private Boolean withFriends;
    private Boolean isDelete;
    private List<String> tags;
    private Long dateFrom;
    private Long dateTo;
}
