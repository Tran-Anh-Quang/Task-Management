package com.quangta.task.service.service.impl;

import com.quangta.task.service.entity.Task;
import com.quangta.task.service.entity.TaskStatus;
import com.quangta.task.service.repository.TaskRepository;
import com.quangta.task.service.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    @Override
    public Task createTask(Task task, String requesterRole) throws Exception {
        if (!requesterRole.equals("ROLE_ADMIN")) {
            throw new Exception("Only admin can create task");
        }
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) throws Exception {
        return taskRepository.findById(id).orElseThrow(() -> new Exception("Task " + id + " not found"));
    }

    @Override
    public List<Task> getAllTask(TaskStatus status) {
        List<Task> allTasks = taskRepository.findAll();

        return allTasks.stream().filter(
                task -> status == null || task.getStatus().name().equalsIgnoreCase(status.toString())
        ).toList();
    }

    @Override
    public Task updateTask(Long id, Task updatedTask, Long userId) throws Exception {

        Task existingTask = getTaskById(id);

        if (updatedTask.getTitle() != null){
            existingTask.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null){
            existingTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getImage() != null){
            existingTask.setImage(updatedTask.getImage());
        }
        if (updatedTask.getStatus() != null){
            existingTask.setStatus(updatedTask.getStatus());
        }
        if (updatedTask.getDeadLine() != null){
            existingTask.setDeadLine(updatedTask.getDeadLine());
        }
        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id) throws Exception {
        getTaskById(id);
        taskRepository.deleteById(id);
    }

    @Override
    public Task assignedToUser(Long userId, Long taskId) throws Exception {
        Task task = getTaskById(taskId);
        task.setAssignedUserId(userId);
        task.setStatus(TaskStatus.ASSIGNED);

        return taskRepository.save(task);
    }

    @Override
    public List<Task> assignedUsersTask(Long userId, TaskStatus status) {
        List<Task> allTasks = taskRepository.findByAssignedUserId(userId);

        return allTasks.stream().filter(
                task -> status == null || task.getStatus().name().equalsIgnoreCase(status.toString())
        ).collect(Collectors.toList());
    }

    @Override
    public Task completedTask(Long taskId) throws Exception {
        Task task = getTaskById(taskId);
        task.setStatus(TaskStatus.DONE);

        return taskRepository.save(task);
    }
}
