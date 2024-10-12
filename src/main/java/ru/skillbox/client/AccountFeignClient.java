package ru.skillbox.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.skillbox.config.FeignClientConfig;
import ru.skillbox.dto.AccountDto;

import java.util.UUID;

@FeignClient(name = "social-network-account", configuration = FeignClientConfig.class)
public interface AccountFeignClient {

    @GetMapping("/api/v1/account/{id}")
    AccountDto getAccountById(@PathVariable("id") UUID accountId);
}
