package com.quangta.tasksubmissionservice.controller;

import com.quangta.tasksubmissionservice.dto.UserDto;
import com.quangta.tasksubmissionservice.entity.Submission;
import com.quangta.tasksubmissionservice.service.SubmissionService;
import com.quangta.tasksubmissionservice.service.TaskService;
import com.quangta.tasksubmissionservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    private final UserService userService;

    private final TaskService taskService;
    @PostMapping()
    public ResponseEntity<Submission> submitTask(
            @RequestParam Long taskId,
            @RequestParam String githubLink,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.getUserProfile(jwt);
        Submission submission = submissionService.submitTask(taskId, githubLink, userDto.getId(), jwt);

        return new ResponseEntity<>(submission, HttpStatus.CREATED);
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<Submission> getSubmissionById(
            @PathVariable Long submissionId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.getUserProfile(jwt);
        Submission submission = submissionService.getTaskSubmissionById(submissionId);

        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Submission>> getAllSubmissions(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.getUserProfile(jwt);
        List<Submission> submissions = submissionService.getAllTaskSubmission();

        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Submission>> getAllSubmissionsByTaskId(
            @PathVariable Long taskId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.getUserProfile(jwt);
        List<Submission> submissions = submissionService.getAlTaskSubmissionsByTaskId(taskId);

        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Submission> acceptOrDeclineSubmission(
            @PathVariable Long id,
            @RequestParam("status") String status,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.getUserProfile(jwt);
        Submission submission = submissionService.acceptDeclineSubmission(id, status);

        return new ResponseEntity<>(submission, HttpStatus.OK);
    }
}
