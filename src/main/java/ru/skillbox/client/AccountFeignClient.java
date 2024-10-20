package ru.skillbox.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.skillbox.config.FeignClientConfig;
import ru.skillbox.dto.AccountDto;
import ru.skillbox.dto.AccountSearchDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "social-network-account", configuration = FeignClientConfig.class)
public interface AccountFeignClient {

    @GetMapping("/api/v1/account/{id}")
    AccountDto getAccountById(@PathVariable("id") UUID accountId);
    @GetMapping("/api/v1/account/searchs")
    public List<AccountDto> searchAccountsByAuthor(@RequestParam String author);
}
