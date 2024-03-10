package com.quangta.task.service.controller;

import com.quangta.task.service.dto.UserDto;
import com.quangta.task.service.entity.Task;
import com.quangta.task.service.entity.TaskStatus;
import com.quangta.task.service.service.TaskService;
import com.quangta.task.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    private final UserService userService;
    @PostMapping
    public ResponseEntity<Task> creatTask(
            @RequestBody Task task,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.getUserProfile(jwt);

        Task createdTask = taskService.createTask(task, userDto.getRole());

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.getUserProfile(jwt);

        Task task = taskService.getTaskById(id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/assigned")
    public ResponseEntity<List<Task>> getAssignedUserTask(
            @PathVariable Long userId,
            @RequestParam(required = false) TaskStatus status,
            @RequestHeader("Authorization") String jwt
    ) {
//        UserDto userDto = userService.getUserProfile(jwt);

        List<Task> tasks = taskService.assignedUsersTask(userId, status);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTask(
            @RequestParam(required = false) TaskStatus status
    ) {
        List<Task> tasks = taskService.getAllTask(status);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{id}/user/{userId}/assigned")
    public ResponseEntity<Task> assignedTaskToUser(
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.getUserProfile(jwt);
        Task task = taskService.assignedToUser(userId, id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task request,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.getUserProfile(jwt);
        Task updatedTask = taskService.updateTask(id, request, userDto.getId());

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> completedTask(
            @PathVariable Long id
    ) throws Exception {
        Task completedTask = taskService.completedTask(id);

        return new ResponseEntity<>(completedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) throws Exception {
        taskService.deleteTask(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
