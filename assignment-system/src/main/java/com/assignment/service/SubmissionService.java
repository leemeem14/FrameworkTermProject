package com.assignment.service;

import com.assignment.entity.Assignment;
import com.assignment.entity.Submission;
import com.assignment.entity.User;
import com.assignment.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final AssignmentService assignmentService;
    private final UserService userService;

    public Submission createSubmission(Long assignmentId, Long userId, String filename, String originalFilename, Long fileSize) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        User user = userService.getUserById(userId);

        Submission submission = Submission.builder()
                .assignment(assignment)
                .user(user)
                .filename(filename)
                .originalFilename(originalFilename)
                .fileSize(fileSize)
                .build();

        return submissionRepository.save(submission);
    }

    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    public List<Submission> getSubmissionsByUserAndAssignment(Long userId, Long assignmentId) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        User user = userService.getUserById(userId);
        return submissionRepository.findByAssignmentAndUser(assignment, user);
    }

    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("제출 정보를 찾을 수 없습니다"));
    }

    public Submission getSubmissionByFilename(String filename) {
        return submissionRepository.findByFilename(filename)
                .orElse(null);
    }
}

