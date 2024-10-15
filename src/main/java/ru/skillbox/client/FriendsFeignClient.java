package ru.skillbox.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.skillbox.config.FeignClientConfig;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "social-network-friends", configuration = FeignClientConfig.class)
public interface FriendsFeignClient {

    @GetMapping("/api/v1/friends/accountIds")
    List<UUID> getFriendsIds(@RequestParam UUID accountId);
}
