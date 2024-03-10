package com.quangta.task.service.service;

import com.quangta.task.service.entity.Task;
import com.quangta.task.service.entity.TaskStatus;

import java.util.List;

public interface TaskService {

    /**
     * Create a new task
     *
     * @param  task pass a task with properties' task
     * @param  requesterRole pass a role of requester
     * @return Task
     */
    Task createTask(Task task, String requesterRole) throws Exception;

    /**
     * Get a task
     *
     * @param  id pass a task id
     * @return Task
     */
    Task getTaskById(Long id) throws Exception;

    /**
     * Get all tasks
     *
     * @param  status pass a status task
     * @return list of tasks
     */
    List<Task> getAllTask(TaskStatus status);

    /**
     * Update task
     *
     * @param  id pass a task id
     * @param  updatedTask pass a task with new properties
     * @param  userId pass a user id that was assigned to this task
     * @return updated task
     */
    Task updateTask(Long id, Task updatedTask, Long userId) throws Exception;

    /**
     * Delete a task
     *
     * @param  id pass a task id
     */
    void deleteTask(Long id) throws Exception;

    /**
     * Assign a task
     *
     * @param  userId pass a user id
     * @param  taskId pass a task id
     * @return Task
     */
    Task assignedToUser(Long userId, Long taskId) throws Exception;

    /**
     * Get all tasks of a user
     *
     * @param  userId pass a user id
     * @param  status pass a status task
     * @return list of tasks
     */
    List<Task> assignedUsersTask(Long userId, TaskStatus status);

    /**
     * Complete a task
     *
     * @param  taskId pass a task id
     * @return Task
     */
    Task completedTask(Long taskId) throws Exception;
}
