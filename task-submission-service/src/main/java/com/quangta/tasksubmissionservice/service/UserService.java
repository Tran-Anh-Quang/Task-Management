package com.quangta.tasksubmissionservice.service;

import com.quangta.tasksubmissionservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:5001")
public interface UserService {
    @GetMapping("/api/user/profile")
    UserDto getUserProfile(@RequestHeader("Authorization") String jwt);
}