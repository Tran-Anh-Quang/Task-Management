package com.quangta.task.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/tasks")
    public ResponseEntity<String> getAssignedUserTask() {

        return new ResponseEntity<>("Welcome to task service", HttpStatus.OK);
    }
}
