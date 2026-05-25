package com.ecommerce.orderservice.feign;

import com.ecommerce.orderservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @GetMapping("/auth/{id}")
    UserDto getUser(@PathVariable Long id);
}