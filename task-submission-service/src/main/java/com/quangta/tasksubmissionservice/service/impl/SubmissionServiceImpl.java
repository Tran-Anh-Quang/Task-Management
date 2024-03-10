package com.quangta.tasksubmissionservice.service.impl;

import com.quangta.tasksubmissionservice.dto.TaskDto;
import com.quangta.tasksubmissionservice.entity.Submission;
import com.quangta.tasksubmissionservice.repository.SubmissionRepository;
import com.quangta.tasksubmissionservice.service.SubmissionService;
import com.quangta.tasksubmissionservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;

    private final TaskService taskService;

    @Override
    public Submission submitTask(Long taskId, String githubLink, Long userId, String jwt) throws Exception {
        TaskDto taskDto = taskService.getTaskById(taskId, jwt);
        if (taskDto != null){
            Submission submission = new Submission();

            submission.setTaskId(taskId);
            submission.setGithubLink(githubLink);
            submission.setUserId(userId);
            submission.setSubmissionTime(LocalDateTime.now());

            return submissionRepository.save(submission);
        }
        throw new Exception("Task not found with id " + taskId);
    }

    @Override
    public Submission getTaskSubmissionById(Long submissionId) throws Exception {
        return submissionRepository.findById(submissionId).orElseThrow(() -> new Exception("Task Submission not found with id " + submissionId));
    }

    @Override
    public List<Submission> getAllTaskSubmission() {
        return submissionRepository.findAll();
    }

    @Override
    public List<Submission> getAlTaskSubmissionsByTaskId(Long taskId) {
        return submissionRepository.findByTaskId(taskId);
    }

    @Override
    public Submission acceptDeclineSubmission(Long id, String status) throws Exception {
        Submission submission = getTaskSubmissionById(id);
        submission.setStatus(status);
        if (status.equals("ACCEPTED")){
            taskService.completedTask(submission.getTaskId());
        }

        return submissionRepository.save(submission);
    }
}
